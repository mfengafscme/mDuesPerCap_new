
package org.afscme.enterprise.log;

import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.naming.Context; 
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.Queue;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.afscme.enterprise.util.JMSUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;

/**
 * Contains static methods for loggin system events.
 * Comments in each method describe the interface to that method, there may be additional calculated information that is logged.
 * See Framework Complex and Background Processing docuemnt for the output format of each logged event.
 * We use Log4J.  And the log message level is at "INFO".
 */
public class SystemLog {
    
    private static String RT = System.getProperty("line.separator");
    protected static Logger logger = Logger.getLogger(SystemLog.class);
    private static String INDENT = "    ";  // four spaces
    	
    /**
     * Logged whenever a user requests generation of a mailing list
     * @param name The name of the mailing list generated 
     * @param format The output format requested 
     * @param userId ID of the user that performed the generation
     * @param requestedTime The time the generation was requested.
     */
    public static void logMailingListGenerated(String name, String format, String userId, Timestamp requestedTime) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            
            String rtStr = requestedTime.toString();
            int nanoIndex = rtStr.indexOf('.');
            String rt = rtStr.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Mailing List Generated, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Mailing List Name : ");
            sb.append(name);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Output Format : ");
            sb.append(format);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Requestor User ID : ");
            sb.append(userId);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Requested Time : ");
            sb.append(rt);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            /************************************************************************************************/
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Mailing List Generated");
            log.setEventUserId(userId.toString());
            sb.delete(0, sb.length());
            sb.append("Mailing List Name : ");
            sb.append(name);            
            sb.append(";Output Format : ");
            sb.append(format);            
            sb.append(";Requestor User ID : ");
            sb.append(userId);            
            sb.append(";Requested Time : ");
            sb.append(rt);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());            
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            /********************************************************************************************************/
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }
    }

    /**
     * Logged whenever a user requests generation of a report
     * @param name The name of the mailing list generated 
     * @param format The output format requested 
     * @param userId ID of the user that performed the generation
     * @param requestedTime The time the generation was requested.
     */
    public static void logReportGenerated(String name, String format, String userId, Timestamp requestedTime) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String rtStr = requestedTime.toString();
            int nanoIndex = rtStr.indexOf('.');
            String rt = rtStr.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Report Generated, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Report Name : ");
            sb.append(name);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Output Format : ");
            sb.append(format);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Requestor User ID : ");
            sb.append(userId);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Requested Time : ");
            sb.append(rt);         
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            /************************************************************************************************/
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Report Generated");
            log.setEventUserId(userId.toString());
            sb.delete(0, sb.length());
            sb.append("Report Name : ");
            sb.append(name);            
            sb.append(";Output Format : ");
            sb.append(format);            
            sb.append(";Requestor User ID : ");
            sb.append(userId);            
            sb.append(";Requested Time : ");
            sb.append(rt);         
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());            
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            /********************************************************************************************************/

            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged whenever a user requests generation of an AFLCIO Extract
     * @param requestedTime The time the generation was requested
     * @param runDate The time the generation began
     * @param fileName The name of the output file generated
     * @param recordCount The number of records in the output file
     * @param userId ID of the user that performed the generation
     */
    public static void logAFLCIOExtractGenerated(Timestamp requestedTime, Timestamp runDate, Timestamp completionTime, String fileName, int recordCount, String userId) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String str = requestedTime.toString();
            int nanoIndex = str.indexOf('.');
            String rt = str.substring(0, nanoIndex);
            
            str = completionTime.toString();
            nanoIndex = str.indexOf('.');
            String ct = str.substring(0, nanoIndex);
            
            str = runDate.toString();
            int spIndex = str.indexOf(' ');
            String rd = str.substring(0, spIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : AFLCIO Extract Generated, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Requestor User ID : ");
            sb.append(userId);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Requested Time : ");
            sb.append(rt);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Run Date : ");
            sb.append(rd);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Completion Time : ");
            sb.append(ct);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Record Count : ");
            sb.append(recordCount);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Output File Name : ");
            sb.append(fileName);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged whenever a generated AFLCIO extract is sent via FTP.
     * @param bytesSent The number of bytes sent 
     * @param startTime The time the transmission was started
     * @param elapsedTime The number of seconds the transfer took.
     * @param destinationHost The URI the file was transferred to.
     */
    public static void logAFLCIOExtractSent(int bytesSent, Timestamp startTime, int elapsedTime, String destinationHost) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String str = startTime.toString();
            int nanoIndex = str.indexOf('.');
            String st = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : AFLCIO Extract Sent, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Bytes Sent : ");
            sb.append(bytesSent);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Transmission Start Time : ");
            sb.append(st);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Elapsed Transfer Time : ");
            sb.append(elapsedTime);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Destination Host : ");
            sb.append(destinationHost);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged when an affiliate Member or Officer file is successfully processed and queued for update.
     * @param affiliateName The name of the affiliate providing the file
     * @param creationDate Creation date of the import file
     * @param recordCount Number of records processed
     * @param elapsedTime The number of seconds the input processing took
     */
    public static void logAffiliateDataProcessed(String affiliateName, Timestamp creationDate, int recordCount, int elapsedTime) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String str = creationDate.toString();
            int nanoIndex = str.indexOf('.');
            String cd = str.substring(0, nanoIndex);
            
            // get the date proceed timestamp
            Timestamp dateProcessed = new Timestamp(new Date().getTime());
            str = dateProcessed.toString();
            nanoIndex = str.indexOf('.');
            String dp = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Affiliate Data Processed, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Affiliate Name : ");
            sb.append(affiliateName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Creation Date : ");
            sb.append(cd);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Record Count : ");
            sb.append(recordCount);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Elapsed Time : ");
            sb.append(elapsedTime);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Date File Processed : ");
            sb.append(dp);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged if the system detects an error in an affiliate update file prior to processing.
     * This would indicate no records could be processed, due to a general formatting or other error in the file.
     * @param affiliateName Name of the affiliate the submitted the file
     * @param description Description of the error
     * @param fileName Name of the file being imported
     * @param time Time the error occurred
     */
    public static void logAffiliateFileError(String affiliateName, String description, String fileName, Timestamp time) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String str = time.toString();
            int nanoIndex = str.indexOf('.');
            String et = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Affiliate File Error, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Description : ");
            sb.append(description);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Affiliate : ");
            sb.append(affiliateName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("File Name : ");
            sb.append(fileName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Time : ");
            sb.append(et);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged whenever the system completes the 'Apply Update' specification.
     * @param additions The number of additions made in the update.
     * @param changes The number of changes made in the update.
     * @param deletions The number of deletions made in the update.
     */
    public static void logUpdateApplied(int additions, int changes, int deletions) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Update Applied, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Additions : ");
            sb.append(additions);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Updates : ");
            sb.append(changes);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Deletions : ");
            sb.append(deletions);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged when a user cancels an update in the 'Apply Update' specification
     * @param additions The number of additions that would have been made in the update.
     * @param changes The number of changes that would have been made in the update.
     * @param deletions The number of deletions that would have been made in the update.
     * @param String fileName Name of the original update file.
     */
    public static void logUpdateCancelled(int additions, int changes, int deletions, String fileName) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Update Canceled, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Source : ");
            sb.append(fileName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Additions : ");
            sb.append(additions);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Updates : ");
            sb.append(changes);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Deletions : ");
            sb.append(deletions);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged when the 'Apply Update' specification cannot be completed because of an error.
     * @param fieldName Name of the field being updated
     * @param reason Reason for the failure.
     */
    public static void logApplyUpdateError(String fieldName, String reason) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Update Error, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Field : ");
            sb.append(fieldName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Reason : ");
            sb.append(reason);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
	}
	
    /**
     * Logged for each record that cannot be updated during the 'Apply Update' specification.
     * @param fieldName Name of the field being updated
     * @param time Time at which the error occurred.
     * @param description Description of the error that occurred
     */
    public static void logRecordUpdateError(String fieldName, Timestamp time, String description) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            
            String str = time.toString();
            int nanoIndex = str.indexOf('.');
            String et = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Record Update Error, ");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Description : ");
            sb.append(description);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Field : ");
            sb.append(fieldName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Time : ");
            sb.append(et);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged during the processing of an affiliate update file.
     * Records information about matching performed between the file and existing database data.
     * @param affiliateName Name of the affiliate that provided the file
     * @param matched The number of records matched.
     * @param notMatched The number of records that were not matched.
     * @param creationDate The date the update file was created.
     * @param elapsedTime The number of seconds elapsed during the matching process
     */
    public static void logDataMatchingPerformed(String affiliateName, int matched, int notMatched, Timestamp creationDate, int elapsedTime) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String str = creationDate.toString();
            int nanoIndex = str.indexOf('.');
            String cd = str.substring(0, nanoIndex);
            
            Timestamp dateProcessed = new Timestamp(new Date().getTime());
            str = dateProcessed.toString();
            nanoIndex = str.indexOf('.');
            String dp = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Data Matching Performed");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Affiliate : ");
            sb.append(affiliateName);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Creation Date : ");
            sb.append(cd);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Records Matched : ");
            sb.append(matched);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Records Not Matched : ");
            sb.append(notMatched);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Records Processed : ");
            sb.append(matched + notMatched);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Elapsed Time : ");
            sb.append(elapsedTime);
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Date Processed : ");
            sb.append(dp);
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }

    /**
     * Logged when the framework catches an Exception.
     * @param ex The associated exception, if available.
     * @param URI The URI that was being accessed, if available.
     * @param userId ID of the user that initiated the exception, if available
     */
    public static void logApplicationError(Exception e, String URI, String userId) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            
            // get the time
            Timestamp time = new Timestamp(new Date().getTime());
            String str = time.toString();
            int nanoIndex = str.indexOf('.');
            String et = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            sb.append(INDENT);
            sb.append("Event : Application Error");
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Time : ");
            sb.append(et);
            sb.append(RT);
            if (URI != null) {
                sb.append(INDENT);
                sb.append("URI : ");
                sb.append(URI);
                sb.append(RT);
            }
            if (userId != null) {
                sb.append(INDENT);
                sb.append("User ID : ");
                sb.append(userId);
                sb.append(RT);
            }
            sb.append(INDENT);
            sb.append("Message : ");
            sb.append(e.getMessage());
            sb.append(RT);
            sb.append(INDENT);
            sb.append("Stack Trace : ");
            
            StackTraceElement[] stackArray = e.getStackTrace();
            for (int i = 0; i < stackArray.length; i++) {
                sb.append(RT);
                sb.append(INDENT);
                sb.append(INDENT);  // double indent
                sb.append(stackArray[i].toString());
            }     
            
            JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            /********************************************************************************************/
            //This code below will create an object message an write to application log table
            /*******************************************************************************************/
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Application Error");
            log.setEventUserId(userId);
            sb.delete(0, 30);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            /***********************************************************************************************/
            
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
     
    /***********************************************************************************************************************/
    /**
     * Logged when an affiliate Member or Officer file is successfully processed and queued for update.
     * @param affiliateName The name of the affiliate providing the file
     * @param creationDate Creation date of the import file
     * @param recordCount Number of records processed
     * @param elapsedTime The number of seconds the input processing took
     * @param user user id or name of the person performing the operation
     */
    public static void logAffiliateDataProcessed(String affiliateName, Timestamp creationDate, int recordCount, double elapsedTime, String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();

            String str = creationDate.toString();
            int nanoIndex = str.indexOf('.');
            String cd = str.substring(0, nanoIndex);
            
            // get the date proceed timestamp
            Timestamp dateProcessed = new Timestamp(new Date().getTime());
            str = dateProcessed.toString();
            nanoIndex = str.indexOf('.');
            String dp = str.substring(0, nanoIndex);
            
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Affiliate Data Processed");
            log.setEventUserId(user);
            
            StringBuffer sb = new StringBuffer();
            /*sb.append(INDENT);
            sb.append("Event : Affiliate Data Processed, ");
            sb.append(RT);
            sb.append(INDENT);*/
            sb.append("Affiliate Name : ");
            sb.append(affiliateName);
            //sb.append(RT);
            sb.append(INDENT);
            sb.append(";Creation Date : ");
            sb.append(cd);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append(";Record Count : ");
            sb.append(recordCount);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append(";Elapsed Time : ");
            sb.append(elapsedTime);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append(";Date File Processed : ");
            sb.append(dp);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            //JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
    
    /**
     * Logged when a user cancels an update in the 'Apply Update' specification
     * @param additions The number of additions that would have been made in the update.
     * @param changes The number of changes that would have been made in the update.
     * @param deletions The number of deletions that would have been made in the update.
     * @param String fileName Name of the original update file.
     *@param user user id or name of the person performing the operation
     */
    public static void logUpdateCancelled(int additions, int changes, int deletions, String fileName, String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Update Canceled");
            log.setEventUserId(user);
            
            StringBuffer sb = new StringBuffer();
            /*sb.append(INDENT);
            sb.append("Event : Update Canceled, ");
            sb.append(RT);
            sb.append(INDENT);*/
            sb.append("Source : ");
            sb.append(fileName);
            /*
            sb.append(RT);            
            sb.append(INDENT);
            sb.append(";User : ");
            sb.append(user);
            sb.append(RT);            
            sb.append(INDENT);*/
            sb.append(";Additions : ");
            sb.append(additions);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append(";Updates : ");
            sb.append(changes);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append(";Deletions : ");
            sb.append(deletions);
			
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
    /**
     * Logged whenever the system completes the 'Apply Update' specification.
     * @param additions The number of additions made in the update.
     * @param changes The number of changes made in the update.
     * @param deletions The number of deletions made in the update.
     *@param user user id or name of the person performing the operation
     */
    public static void logUpdateApplied(int additions, int changes, int deletions, String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Update Applied");
            log.setEventUserId(user);
            
            StringBuffer sb = new StringBuffer();
            //sb.append(INDENT);
            //sb.append("Event : Update Applied, ");
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Additions : ");
            sb.append(additions);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Updates : ");
            sb.append(changes);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Deletions : ");
            sb.append(deletions);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
    
    /**
     * Logged if the system detects an error in an affiliate update file prior to processing.
     * This would indicate no records could be processed, due to a general formatting or other error in the file.
     * @param affiliateName Name of the affiliate the submitted the file
     * @param description Description of the error
     * @param fileName Name of the file being imported
     * @param time Time the error occurred
     *@param user user id or name of the person performing the operation
     */
    public static void logAffiliateFileError(String affiliateName, String description, String fileName, Timestamp time, String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Affiliate File Error");
            log.setEventUserId(user);
            
            String str = time.toString();
            int nanoIndex = str.indexOf('.');
            String et = str.substring(0, nanoIndex);
            logger.debug("description======>" + description);
            logger.debug("affiliateName======>" + affiliateName);
            logger.debug("user======>" + user);
            StringBuffer sb = new StringBuffer();
            /*sb.append(INDENT);
            sb.append("Event : Affiliate File Error, ");
            sb.append(RT);
            sb.append(INDENT);*/
            sb.append("Description : ");
            sb.append(description);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Affiliate : ");
            sb.append(affiliateName);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("File Name : ");
            sb.append(fileName);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Time : ");
            sb.append(et);
            //JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            logger.debug("comments=>" + sb.toString());
            log.setEventData(sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
    
        /**
     * Logged when the 'Apply Update' specification cannot be completed because of an error.
     * @param fieldName Name of the field being updated
     * @param reason Reason for the failure.
     * @param user user id or name of the person performing the operation
     */
    public static void logApplyUpdateError(String fieldName, String reason,String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Update Error");
            log.setEventUserId(user);
            
            StringBuffer sb = new StringBuffer();
            /*sb.append(INDENT);
            sb.append("Event : Update Error, ");
            sb.append(RT);
            sb.append(INDENT);*/
            sb.append("Field : ");
            sb.append(fieldName);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Reason : ");
            sb.append(reason);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            //JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
	}
    /**
     * Logged for each record that cannot be updated during the 'Apply Update' specification.
     * @param fieldName Name of the field being updated
     * @param time Time at which the error occurred.
     * @param description Description of the error that occurred
     *@param user user id or name of the person performing the operation
     */
    public static void logRecordUpdateError(String fieldName, Timestamp time, String description, String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Record Update Error");
            log.setEventUserId(user);
            
            String str = time.toString();
            int nanoIndex = str.indexOf('.');
            String et = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            /*sb.append(INDENT);
            sb.append("Event : Record Update Error, ");
            sb.append(RT);
            sb.append(INDENT);*/
            sb.append("Description : ");
            sb.append(description);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Field : ");
            sb.append(fieldName);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Time : ");
            sb.append(et);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            //JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
    
    /**
     * Logged during the processing of an affiliate update file.
     * Records information about matching performed between the file and existing database data.
     * @param affiliateName Name of the affiliate that provided the file
     * @param matched The number of records matched.
     * @param notMatched The number of records that were not matched.
     * @param creationDate The date the update file was created.
     * @param elapsedTime The number of seconds elapsed during the matching process
     */
    public static void logDataMatchingPerformed(String affiliateName, int matched, int notMatched, Timestamp creationDate, double elapsedTime, String user) {
        try {
            QueueConnection con = JMSUtil.getConnection();
            ApplicationLog log  = new ApplicationLog();
            log.setEventName("Data Matching Performed");
            log.setEventUserId(user);
            
            String str = creationDate.toString();
            int nanoIndex = str.indexOf('.');
            String cd = str.substring(0, nanoIndex);
            
            Timestamp dateProcessed = new Timestamp(new Date().getTime());
            str = dateProcessed.toString();
            nanoIndex = str.indexOf('.');
            String dp = str.substring(0, nanoIndex);
            
            StringBuffer sb = new StringBuffer();
            /*sb.append(INDENT);
            sb.append("Event : Data Matching Performed");
            sb.append(RT);
            sb.append(INDENT);*/
            sb.append("Affiliate : ");
            sb.append(affiliateName);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Creation Date : ");
            sb.append(cd);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Records Matched : ");
            sb.append(matched);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Records Not Matched : ");
            sb.append(notMatched);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Records Processed : ");
            sb.append(matched + notMatched);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Elapsed Time : ");
            sb.append(elapsedTime);
            //sb.append(RT);
            //sb.append(INDENT);
            sb.append("Date Processed : ");
            sb.append(dp);
            log.setEventData(sb.toString());
            logger.debug("comments=>" + sb.toString());
            //JMSUtil.sendTextMessage(con, JMSUtil.SYSTEM_LOG_QUEUE, sb.toString());
            JMSUtil.sendObjectMessage(con, JMSUtil.SYSTEM_LOG_QUEUE,log);
            con.close();
        }
        catch (NamingException exp) {
            throw new EJBException(exp);
        }
        catch (JMSException exp) {
            throw new EJBException(exp);
        }		
    }
    
    //*****************************************************************************************************************
    
}
