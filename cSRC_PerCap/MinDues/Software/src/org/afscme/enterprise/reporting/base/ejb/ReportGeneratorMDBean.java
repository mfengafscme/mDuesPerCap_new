package org.afscme.enterprise.reporting.base.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.common.ejb.MessageBase;
import org.afscme.enterprise.log.SystemLog;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.email.EmailGenerator;
import org.afscme.enterprise.reporting.base.email.ReportEmailData;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;
import org.afscme.enterprise.reporting.base.generator.ReportMessageData;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;



/**
 * This message driven bean handles requests for generating a report.
 * @ejb:bean name="ReportGeneratorMD" display-name="ReportGeneratorMD"
 *           destination-type="javax.jms.Queue"
 *
 * @ejb:transaction type="Required"
 *
 * @jboss:destination-jndi-name name="queue/ReportGeneratorMD"
 *                          
 */

public class ReportGeneratorMDBean extends MessageBase {
    
    protected ReportAccess reportAccess;
    protected Map reportFields;
    protected MaintainCodes maintainCodes;

    protected static Logger log = Logger.getLogger(ReportGeneratorMDBean.class);

    public void ejbCreate() {
        
        ConfigUtil.init();
        
        try {
            reportAccess = JNDIUtil.getReportAccessHome().create();
            reportFields = reportAccess.getReportFields();
            maintainCodes = JNDIUtil.getMaintainCodesHome().create();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (CreateException exp) {
            throw new EJBException(exp);
        }
    }
    
    public void ejbRemove() {
        try {
            reportAccess.remove();
            maintainCodes.remove();
        }
        catch (RemoveException exp) {
            throw new EJBException(exp);
        }
    }

    /**
     * Called by the container when a message arrives requesting generation of a report 
     *
     * @ejb:transaction type="Supports"
     */
    public void onMessage(Message message) {
        
        try {
            ObjectMessage msg = (ObjectMessage)message;
        
            ReportMessageData reportMsg = (ReportMessageData)msg.getObject();

            try {
                processReportMessage(reportMsg);
            } catch (Exception msgException) {
                SystemLog.logApplicationError(msgException, null, null);
                log.error("Error calling processReportMessage in ReportGeneratorMDBean", msgException);
                throw new EJBException(msgException);
            }
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }
    }
    
    /**
     * Makes the appropriate calls to generate a report, and send an email to the
     * appropriate user
     */
    private void processReportMessage(ReportMessageData msg) throws Exception {
        try {
            
            //get the report
            Report report = reportAccess.getReport(msg.getReportPK());
            if (msg.getRuntimeCriteria() != null)
                report.setCriteriaFields(msg.getRuntimeCriteria());
            
            log.debug("isFilterDuplicateAddresses ======================================>" + msg.isFilterDuplicateAddresses());
            
            //get the handler
            ReportHandler handler;
            if (msg.isCustomReport())
                handler = new ReportGenerator(reportFields, report, msg.getOutputFormat(), ReportGenerator.ALL_ROWS, msg.getAccessibleAffiliates(), msg.isFilterDuplicateAddresses());
            else
                handler = msg.getReportHandler();

            //create a file, and generate the report to it 
            ReportData reportData = report.getReportData();
            String fileName = getTempFileName(reportData.getName(), msg.getOutputFormat());
            log.debug("filename======>" + fileName);
            FileOutputStream fos = new FileOutputStream(fileName);
            int count = handler.generate(fos);
            fos.close();

            //open the genearted file
            FileInputStream fis = new FileInputStream(fileName);

            //get the report file name
            String reportFileName  = handler.getFileName();
            if (reportFileName == null)
                reportFileName = reportData.getName();
            
            // email the formatted report            
            ReportEmailData email = new ReportEmailData();
            email.setGeneratedReport(fis);
            MediaType type = msg.getMediaType();
            if (type != null)
                email.setSubject(type.getType());
            else
                email.setSubject("Report Generated");
            email.setReportName(reportFileName);
            email.setOutputFormat(msg.getOutputFormat().getFormatString());
            email.setRequestorUserID(msg.getRequestorUID());
            email.setRequestedTime(msg.getRequestedTime());
            email.setMailingList(reportData.isMailingList());
            email.setNumOfMail(count); 
            if (msg.getOutputFormat().getFormat() == OutputFormat.PDF)
                email.setReportMimeType(ReportEmailData.PDF_MIME);
            else
                email.setReportMimeType(ReportEmailData.TEXT_MIME);

            EmailGenerator.sendEmail(email);
            //******************************************************************************************************************
            //Log the report generated
            SystemLog.logReportGenerated(reportFileName, msg.getOutputFormat().getFormatString(), msg.getRequestorUID(), new java.sql.Timestamp(new java.util.Date().getTime()));
            //*******************************************************************************************************************
            fis.close();
        }
        catch (Exception exp) {
            SystemLog.logApplicationError(exp, null, msg.getRequestorUID());
            throw new EJBException(exp);
        }
    } 

    /**
     * Gets a unique file name.  The file name is used as the target for the output of a report
     * 
     * @param reportName Name of the report to generate a temp file name for.
     */
    private String getTempFileName(String reportName, OutputFormat outputFormat) {
        String tempDir = ConfigUtil.getConfigurationData().getTempDir();
        String time = DateUtil.getCurrentDateTimeAsTimestamp().toString().replace(':', '#'); // ':' is an illegal file name char        
        
        if (outputFormat.getFormat() == OutputFormat.PDF)
            return  tempDir + File.separator + reportName + '-' + time + ".pdf";
        else
            return tempDir + File.separator + reportName + '-' + time + ".txt";
    }
}

