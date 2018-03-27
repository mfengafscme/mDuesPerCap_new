package org.afscme.enterprise.reporting.base.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.jms.QueueConnection;

import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.reporting.base.generator.ReportMessageData;
import org.afscme.enterprise.util.JMSUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;

/**
 * This session bean handles all requests from GUI and interface with ACL,
 * ReportAccessBean, and ReportGeneratorMDB. 
 * @ejb:bean name="BaseReport"
 * display-name="BaseReport" jndi-name="BaseReport" type="Stateless"
 * view-type="local"
 */

public class BaseReportBean extends SessionBase
{

    protected ReportAccess reportAccess;
    protected MaintainCodes maintainCodes;
    protected QueueConnection queueConnection;

    protected Map reportFields;

    protected static Logger log = Logger.getLogger(BaseReportBean.class);

    /* -------- Bean Creation method ---------------------------------- */
    /** Loads all non-changeable data into local member variables */
    public void ejbCreate() throws CreateException
    {
        try
        {
            reportAccess = JNDIUtil.getReportAccessHome().create();
            reportFields = reportAccess.getReportFields();
            maintainCodes = JNDIUtil.getMaintainCodesHome().create();
            queueConnection = JMSUtil.getConnection();
        }
        catch (Exception exp)
        {
            throw new EJBException(exp);
        }
    }

    public void ejbRemove()
    {
        try
        {
            reportAccess.remove();
            maintainCodes.remove();
            queueConnection.close();
        }
        catch (Exception exp)
        {
            throw new EJBException(exp);
        }
    }

    /**
     * Create a new report. 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void createReport(Report report)
    {
        reportAccess.createReport(report);
    }

    /**
     * Delete a report. Only the owner of this report can delete it.
     * @ejb:interface-method view-type="local" 
     * @ejb:transaction type="Supports"
     */
    public void deleteReport(Integer userPK, Integer reportPK)
    {
        try
        {
            ReportData reportData = reportAccess.getReportData(reportPK);
            if (reportData.getOwnerPK().intValue() == userPK.intValue())
                reportAccess.deleteReport(reportPK);
        }
        catch (FinderException exp)
        {
            throw new EJBException(exp);
        }
    }

    /**
     * Retrieve all reports (a list of ReportData) for this user
     * @ejb:interface-method view-type="local" 
     * @ejb:transaction type="Supports"
     */
    public List getAllReportsForUser(Integer userPK)
    {
        List reportList = null;

        List allReports = reportAccess.getAllReports();
        ReportData reportData;
        if (allReports != null)
        {
            reportList = new ArrayList();
            Iterator it = allReports.iterator();
            while (it.hasNext())
            {
                reportData = (ReportData) it.next();
                if (reportData.getOwnerPK().intValue() == userPK.intValue())
                    reportList.add(reportData);
            }
        }

        return reportList;
    }

    /**
     * Retrieve all regular (none mailing list) reports for the user
     * @ejb:interface-method view-type="local" 
     * @ejb:transation type="Supports"
     */
    public List getRegularReportsForUser(Integer userPK)
    {
        List reportList = null;

        List regularReports = reportAccess.getAllRegularReports();
        ReportData reportData;
        if (regularReports != null)
        {
            reportList = new ArrayList();
            Iterator it = regularReports.iterator();
            while (it.hasNext())
            {
                reportData = (ReportData) it.next();
                if (reportData.getOwnerPK().intValue() == userPK.intValue())
                    reportList.add(reportData);
            }
        }

        return reportList;
    }

    /**
     * Retrieve all mailing list reports for the user 
     * @ejb:interface-method view-type="local" 
     * @ejb:transtion type="Supports"
     */
    public List getMailingListReportsForUser(Integer userPK)
    {
        List reportList = null;

        List mailingReports = reportAccess.getAllMailingReports();
        ReportData reportData;
        if (mailingReports != null)
        {
            reportList = new ArrayList();
            Iterator it = mailingReports.iterator();
            while (it.hasNext())
            {
                reportData = (ReportData) it.next();
                if (reportData.getOwnerPK().intValue() == userPK.intValue())
                    reportList.add(reportData);
            }
        }

        return reportList;
    }

    //  Note, the "generateReport" methods below are only for generating report
    // to be send as email.
    //  For on-screen report generation, we do not go through any EJB because
    // the possible return of
    //  large ResultSet we may have.

    /**
     * This method generate a custom report to output formats other than
     * "screen". In addtion, it does not have runtime editable selection
     * criteria. 
     * 
     * @ejb:interface-method view-type="local" 
     * @ejb:transaction type="Supports"
     */
    public void generateReport(
        String uid,
        Set accessibleAffs,
        Integer reportPK,
        MediaType type,
        OutputFormat format,
        boolean filterDuplicateAddresses)
    {
        try
        {
            JMSUtil.sendObjectMessage(
                queueConnection,
                JMSUtil.REPORT_QUEUE,
                createReportMessage(
                    reportPK,
                    uid,
                    accessibleAffs,
                    true,
                    type,
                    format,
                    null,
                    null,
                    filterDuplicateAddresses));
        }
        catch (Exception exp)
        {
            log.error(exp);
            throw new EJBException(exp);
        }
    }
    
	/**
	 * This method generate a custom report to output formats other than
	 * "screen". In addtion, it does not have runtime editable selection
	 * criteria. 
	 * 
	 * @ejb:interface-method view-type="local" 
	 * @ejb:transaction type="Supports"
	 */
	public void generateReport(
		String uid,
		Set accessibleAffs,
		Integer reportPK,
		MediaType type,
		OutputFormat format)
	{
		try
		{
			JMSUtil.sendObjectMessage(
				queueConnection,
				JMSUtil.REPORT_QUEUE,
				createReportMessage(
					reportPK,
					uid,
					accessibleAffs,
					true,
					type,
					format,
					null,
					null,
					false));
		}
		catch (Exception exp)
		{
			log.error(exp);
			throw new EJBException(exp);
		}
	}

    /**
     * This method generate a custom report to output formats other than
     * "screen". In addtion, it has runtime editable selection criteria.
     * @ejb:interface-method view-type="local" 
     * @ejb:transaction type="Supports"
     */
    public void generateReport(
        String uid,
        Set accessibleAffs,
        Integer reportPK,
        MediaType type,
        OutputFormat format,
        Map runtimeCriteria,
        boolean filterDuplicateAddresses)
    {
        try
        {
            JMSUtil.sendObjectMessage(
                queueConnection,
                JMSUtil.REPORT_QUEUE,
                createReportMessage(
                    reportPK,
                    uid,
                    accessibleAffs,
                    true,
                    type,
                    format,
                    runtimeCriteria,
                    null,
                    filterDuplicateAddresses));
        }
        catch (Exception exp)
        {
            log.error(exp);
            throw new EJBException(exp);
        }
    }
    
	/**
	 * This method generate a custom report to output formats other than
	 * "screen". In addtion, it has runtime editable selection criteria.
	 * @ejb:interface-method view-type="local" 
	 * @ejb:transaction type="Supports"
	 */
	public void generateReport(
		String uid,
		Set accessibleAffs,
		Integer reportPK,
		MediaType type,
		OutputFormat format,
		Map runtimeCriteria)
	{
		try
		{
			JMSUtil.sendObjectMessage(
				queueConnection,
				JMSUtil.REPORT_QUEUE,
				createReportMessage(
					reportPK,
					uid,
					accessibleAffs,
					true,
					type,
					format,
					runtimeCriteria,
					null,
					false));
		}
		catch (Exception exp)
		{
			log.error(exp);
			throw new EJBException(exp);
		}
	}

    /**
     * This method generate a specialized report to a specialized format. The
     * runtime criteria is captured in the "specialized handler" for this
     * report. 
     * @ejb:interface-method view-type="local" 
     * @ejb:transaction type="Supports"
     */
    public void generateReport(
        String uid,
        Integer reportPK,
        MediaType type,
        ReportHandler reportHandler)
    {
        try
        {
            OutputFormat format = new OutputFormat(OutputFormat.SPECIALIZED);
            JMSUtil.sendObjectMessage(
                queueConnection,
                JMSUtil.REPORT_QUEUE,
                createReportMessage(
                    reportPK,
                    uid,
                    null,
                    false,
                    type,
                    format,
                    null,
                    reportHandler,
                    false));
        }
        catch (Exception exp)
        {
            log.error(exp);
            throw new EJBException(exp);
        }
    }

    
    private ReportMessageData createReportMessage(
        Integer reportPK,
        String reqUID,
        Set accessibleAffs,
        boolean isCustom,
        MediaType type,
        OutputFormat format,
        Map runtimeCriteria,
        ReportHandler handler,
        boolean filterDuplicateAddresses)
    {
        ReportMessageData msg = new ReportMessageData();
        msg.setReportPK(reportPK);
        msg.setRequestorUID(reqUID);
        msg.setAccessibleAffiliates(accessibleAffs);
        msg.setCustomReport(isCustom);
        msg.setMediaType(type);
        msg.setOutputFormat(format);
        msg.setRequestedTime(new Date());
        msg.setRuntimeCriteria(runtimeCriteria);
        msg.setReportHandler(handler);
        msg.setFilterDuplicateAddresses(filterDuplicateAddresses);

        return msg;
    }

}
