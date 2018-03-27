package org.afscme.enterprise.update.ejb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.text.DateFormat;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.*;
import javax.naming.NamingException;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.Queue;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.update.filequeue.FileEntry;
import org.afscme.enterprise.update.PreUpdateMessage;
import org.afscme.enterprise.update.UpdateMessage;
import org.afscme.enterprise.update.Codes.UpdateFileQueue;
import org.afscme.enterprise.update.Codes.UpdateFileStatus;
import org.afscme.enterprise.update.Codes.UpdateFileType;
import org.afscme.enterprise.update.Codes.UpdateType;
import org.afscme.enterprise.update.Codes.Sort;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.JMSUtil;
import org.apache.log4j.Logger;

/**
 * Handles access to the imported update files.
 *
 * @ejb:bean name="FileQueue" display-name="FileQueue"
 * jndi-name="FileQueue"
 * type="Stateless" view-type="local"
 */

public class FileQueueBean extends SessionBase {
    
    private static Logger logger = Logger.getLogger(FileQueueBean.class);
    
    // a few code types used by file update
    private static final String UPDATE_FILE_STATUS = "UpdateFileStatus";
    private static final String UPDATE_FILE_TYPE = "UpdateFileType";
    private static final String UPDATE_FILE_QUEUE = "UpdateFileQueue";
    private static final String UPDATE_TYPE = "UpdateType";
    
    private static final String FILE_STATUS_PENDING = "2";    //status when applying to db
    private static final String FILE_STATUS_REVIEW = "1";     //status uploaded and ready to apply 
    private static final String FILE_STATUS_PROCESSED = "3";  //status applied to db
    private static final String FILE_STATUS_REJECTED = "4";   //status to reject apply to db
    private static final String FILE_STATUS_UPLOADED = "5";   //status when uploading file to queue
    private static final String FILE_STATUS_INVALID = "6";    //status when file upload is bad    
    
    private static final String FILE_QUEUE_INITIAL = "I";
    private static final String FILE_QUEUE_GOOD = "G";
    private static final String FILE_QUEUE_BAD = "B";
    
    protected static     int     order          =   0;    
    protected static     int     preOrder       =   0;
    protected            boolean startedWhere   =   false;
    // maintain code bean
    private MaintainCodes codeBean = null;
    
    // maintain affiliate bean
    private MaintainAffiliates affBean = null;
    
    // JMS queue connection
    private QueueConnection queueConnection;
    
    /* --------  SQL statements used ----------------------------------- */
    
    private static final String SQL_INSERT_UPDATE_FILE =
        "INSERT INTO AUP_Queue_Mgmt (aff_pk, file_nm, file_storage_path, " + 
        "       upd_file_status, upd_file_type, file_queue, upd_file_valid_dt, " + 
        "       upd_file_received_dt, upd_type, created_dt, created_user_pk, " +
        "       lst_mod_dt, lst_mod_user_pk) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, getDate(), ?, getDate(), ?) ";

    private static final String SQL_SELECT_FILE =
        "SELECT queue_pk, file_nm, file_storage_path, " +
        "       upd_file_status, upd_file_type, file_queue, " +
        "       upd_file_comments, upd_file_processed_dt, upd_file_valid_dt, " +
        "       upd_file_received_dt, upd_type, aff_pk, org_pk, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +
        "FROM   AUP_Queue_Mgmt " +
        "WHERE  queue_pk = ? ";
    
    private static final String SQL_UPDATE_FILE_QUEUE = 
        "UPDATE AUP_Queue_Mgmt " + 
        "SET    file_queue = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  queue_pk = ? ";
    
    private static final String SQL_UPDATE_FILE_STATUS =
        "UPDATE AUP_Queue_Mgmt " +
        "SET    upd_file_status = ?, " + 
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  queue_pk = ? ";
    
    private static final String SQL_UPDATE_FILE_STATUS_PROCESSED =
        "UPDATE AUP_Queue_Mgmt " +
        "SET    upd_file_status = ?, upd_file_processed_dt = GETDATE(), " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  queue_pk = ? ";
    
    private static final String SQL_UPDATE_FILE_STATUS_WITH_COMMENTS = 
        "UPDATE AUP_Queue_Mgmt " +
        "SET    upd_file_status = ?, upd_file_comments = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +        
        "WHERE  queue_pk = ? ";
    
    private static final String SQL_SELECT_ALL_FILES = 
        "SELECT queue_pk, file_nm, file_storage_path, " +
        "       upd_file_status, upd_file_type, file_queue, " +
        "       upd_file_comments, upd_file_processed_dt, upd_file_valid_dt, " +
        "       upd_file_received_dt, upd_type, aff_pk, org_pk, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +
        "FROM   AUP_Queue_Mgmt          " ;
//        "order by                   ?   "    ;
//        "ORDER BY queue_pk ";

    /*************************************************************************************/
    private static final String SQL_SELECT_AFF_FILES = 
        "SELECT     queue_pk, file_nm, file_storage_path, " +
        "           upd_file_status, upd_file_type, file_queue, " +
        "           upd_file_comments, upd_file_processed_dt, upd_file_valid_dt, " +
        "           upd_file_received_dt, upd_type, aff_pk, org_pk, " +
        "           created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt " +
        "FROM                           "   +
        "           AUP_Queue_Mgmt      "   +
        "WHERE                          "   +
        "           aff_pk      in  (?) "   ;
    /*************************************************************************************/
    private static final String SQL_SELECT_FILES =
        "select                                                             "   +
        "        queue_pk                                                   "   +
        "        , file_nm                                                  "   +
        "        , file_storage_path                                        "   +
        "        , upd_file_status                                          "   +
        "        , upd_file_type                                            "   +
        "        , file_queue                                               "   +
        "        , upd_file_comments                                        "   +
        "        , upd_file_processed_dt                                    "   +
        "        , upd_file_valid_dt                                        "   +
        "        , upd_file_received_dt                                     "   +
        "        , upd_type                                                 "   +
        "        , q.aff_pk                                                 "   +
        "        , q.created_user_pk                                        "   +
        "        , q.created_dt                                             "   +
        "        , q.lst_mod_user_pk                                        "   +
        "        , q.lst_mod_dt                                             "   +
        "from                                                               "   +
        "        AUP_queue_mgmt		q                                   "   +
        "        , aff_organizations    a                                   "   +
        "where                                                              "   +
        "        q.aff_pk              =   a.aff_pk                         "   ; 
    /*************************************************************************************************/
    //the following statements are used to order the sql results based on used choice
    private static final String SQL_COMMENT_ORDER = 
        "order by   upd_file_comments       "    ;
    
    private static final String SQL_RDATE_ORDER = 
        "order by   upd_file_received_dt    "    ;
    
    private static final String SQL_VDATE_ORDER = 
        "order by   upd_file_valid_dt       "    ;
    
    private static final String SQL_STATUS_ORDER = 
        "order by   upd_file_status         "    ;
    
    private static final String SQL_FTYPE_ORDER = 
        "order by   upd_file_type           "    ;
    
    private static final String SQL_PROCESSED_ORDER = 
        "order by   upd_file_processed_dt   "    ;
   
    private static final String SQL_UPDATETYPE_ORDER = 
        "order by   upd_type                "    ;
    
    private static final String SQL_AFF_TYPE_ORDER = 
        "order by   aff_type                "    ;
    private static final String SQL_LOC_ORDER = 
        "order by   aff_localSubChapter     "    ;
    private static final String SQL_STATE_ORDER = 
        "order by   a.aff_stateNat_type     "    ;
    private static final String SQL_SUB_UNIT_ORDER = 
        "order by   aff_subUnit             "    ;
    private static final String SQL_CN_CHAP_ORDER = 
        "order by   aff_councilRetiree_chap "    ;
    private static final String SQL_QPK_ORDER = 
        "order by   queue_pk                "    ;
    /**************************************************************************************/
    //the following statements are used to dynamically build a where clause
    //based on field values selected by the user
    private static final String SQL_WHERE           =
    "where                                  "   ;
    private static final String SQL_AND             =
    "and                                    "   ;
    private static final String SPACE       =   " ";
    private static final String QUOTE       =   "'";
    private static final String COMMA       =   ",";
    private static final String EQUAL       =   "=";
    private static final String GREATERTHAN =   ">";
    private static final String LESSTHAN    =   "<";
    private static final String NOT         =   "!";
    private static final String SQL_COUNCIL         =
    "aff_councilRetiree_chap  "   ;
    
    private static final String SQL_LOCAL           =
    "aff_localSubChapter      "   ;
    
    private static final String SQL_AFF_PK          =
    "aff_pk                   "   ;
    
     private static final String SQL_STATE          =
    "aff_stateNat_type        "   ;
    
    private static final String SQL_SUB_UNIT        =
    "aff_subUnit              "   ;
 
    private static final String SQL_AFF_TYPE        =
    "aff_type                 "   ;
    
    private static final String SQL_UPD_TYPE        =
    "upd_type                 "   ;
    
    private static final String SQL_UPD_PRCD_DATE   =
    "upd_file_processed_dt    "   ;
    
    
    
   
    //**********************************************************************************
    private static final String SQL_SELECT_FILE_NAME = 
        "SELECT file_storage_path, file_nm FROM AUP_Queue_Mgmt WHERE queue_pk = ?";
    
    /**
     * Load the common codes used here
     */
    public void ejbCreate() {
        try {
            codeBean = JNDIUtil.getMaintainCodesHome().create();
            affBean = JNDIUtil.getMaintainAffiliatesHome().create();
            
            queueConnection = JMSUtil.getConnection();
        } catch (NamingException ne) {
            throw new EJBException(ne);
        } catch (CreateException ce) {
            throw new EJBException(ce);
        }        
    } 
    
    public void ejbRemove() {
        try {
            codeBean.remove();
            affBean.remove();
            
            queueConnection.close();
        } catch (JMSException je) {
            throw new EJBException(je);
        } catch (RemoveException re) {
            throw new EJBException(re);
        }
    }    
    
    /**
     * Stores a new file
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param content The contents of the file
     * @param affPk The affiliate primary key
     * @param validDate The valid date entered by the user during update
     * @param updateType pointer to UpdateType common code (Member, Officer, Rebate etc...)
     * @param userPk The user who initiated this file upload.
     */
    public void storeFile(byte[] content, Integer affPk, Timestamp validDate, int updateType, int fileType, Integer userPk) { 
        logger.debug("----------------------------------------------------");
        logger.debug("storeFile called.");
        logger.debug("Storing a file queue with pk = " + affPk);
        Integer queuePk;
        
        // construct a unique file name
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        String currDate = DateFormat.getDateTimeInstance().format(date);
        String fileName =   "";
        
        switch (fileType) {
            case UpdateFileType.OFFICER:
            case UpdateFileType.PARTICIPATION:                
                fileName = "UPD_" + currDate.replace(':', '_') + ".xml";
                break;
            default:
                fileName = "UPD_" + currDate.replace(':', '_') + ".txt";
                break;
        }
        
        String filePath = null;
        switch (fileType) {
            case UpdateFileType.PARTICIPATION:
                filePath = ConfigUtil.getConfigurationData().getAfscmeUploadFolder() + File.separator;
                break;
            default:
                filePath = ConfigUtil.getConfigurationData().getAffiliateUploadFolder() + File.separator;
                break;
        }
        
        // write the file content into the local file.
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(filePath + fileName);
            fo.write(content);
        } catch(IOException ie) {
            throw new EJBException(ie);
        } finally {
            try { fo.close(); } catch (Exception e) {} // ignore exception on close...
        }
        
        // insert a new entry into the file queue table
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(SQL_INSERT_UPDATE_FILE);
            DBUtil.setNullableInt(ps, 1, affPk);
            DBUtil.setNullableVarchar(ps, 2, fileName);
            DBUtil.setNullableVarchar(ps, 3, filePath);
            ps.setInt(4, UpdateFileStatus.UPLOADED);
            ps.setInt(5, fileType);
            ps.setInt(6, UpdateFileQueue.INITIAL);
            DBUtil.setNullableTimestamp(ps, 7, validDate);
            ps.setTimestamp(8, time);
            if (updateType == 0) 
                ps.setInt(9, UpdateType.FULL);
            else
                ps.setInt(9, updateType);
            ps.setInt(10, userPk.intValue());
            ps.setInt(11, userPk.intValue());
       
            // Note, the ps is cleaned by DBUtil.insertAntGetIdentity().
            queuePk = DBUtil.insertAndGetIdentity(con, ps);
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        logger.debug("file stored in the db with queuePk = " + queuePk);
        
        try {
           
            switch (fileType) {
                
                case UpdateFileType.PARTICIPATION:

                    // if participation update file, then just send msg to apply update
                    UpdateMessage updMsg = new UpdateMessage();
                    updMsg.setQueuePk(queuePk);
                    updMsg.setUserPk(userPk);
                    JMSUtil.sendObjectMessage(queueConnection, JMSUtil.UPDATE_QUEUE, updMsg);
                    break;
                    
                default:
                    
                    // otherwise, generate pre update summary                    
                    PreUpdateMessage msg = new PreUpdateMessage();
                    msg.setQueuePk(queuePk);
                    msg.setUserPk(userPk);
                    msg.setFileType(fileType);
                    JMSUtil.sendObjectMessage(queueConnection, JMSUtil.UPDATE_QUEUE, msg);
                }
           
        } catch (NamingException ne) {
            throw new EJBException(ne);
        } catch (JMSException je) {
            throw new EJBException(je);
        }
        logger.debug("message sent to the jms queue");
    }
    
    /**
     * Sets a file's status to bad.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param queuePk  primary key of the file to mark bad.
     * @param userPk  user requesting to mark file bad.
     */
    public void markFileBad(Integer queuePk, Integer userPk) { 
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_QUEUE);
            ps.setInt(1, UpdateFileQueue.BAD);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }        
    }
    
    /**
     * Sets a file's status to good.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param queuePk  primary key of the file to mark good.
     * @param userPk  user requesting to mark file good.
     */
    public void markFileGood(Integer queuePk, Integer userPk) { 
        logger.debug("----------------------------------------------------");
        logger.debug("markFileGood called.");
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_QUEUE);
            ps.setInt(1, UpdateFileQueue.GOOD);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        } 
    }
    
    /**
     * Sets a file's status to 'review'. This is set by the pre-update
     * background process when the work is done.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk  primary key of the file to mark review.
     * @param userPk  user requesting to mark file review.
     */
    public void markFileForReview(Integer queuePk, Integer userPk) {
        logger.debug("----------------------------------------------------");
        logger.debug("markFileForReview called.");
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_STATUS);

            ps.setInt(1, UpdateFileStatus.REVIEW);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }         
    }
    
    /**
     * Sets a file's status to 'pending'. This is set by the update
     * background process while the work is being done.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk  primary key of the file to mark pending.
     * @param userPk  user requesting to mark file pending.
     */
    public void markFilePending(Integer queuePk, Integer userPk) {
        logger.debug("----------------------------------------------------");
        logger.debug("markFilePending called.");
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_STATUS);
            
            ps.setInt(1, UpdateFileStatus.PENDING);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }         
    }
    
    /**
     * Sets a file's status to 'processed'
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk  primary key of the file to mark processed.
     * @param userPk  user requesting to mark file processed.
     */
    public void markFileProcessed(Integer queuePk, Integer userPk) {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_STATUS_PROCESSED);
            ps.setInt(1, UpdateFileStatus.PROCESSED);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        } 
    }
    
    /**
     * Sets a file's status to rejected
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk  primary key of the file to mark bad.
     * @param comments Comments entered by the user stating the reason for the objection
     * @param userPk  user entering the rejection comments.
     */
    public void markFileRejected(Integer queuePk, String comments, Integer userPk) { 
        if (queuePk == null || queuePk.intValue() < 1) {
            throw new EJBException("queuePk was invalid.");
        }
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_STATUS_WITH_COMMENTS);
            ps.setInt(1, UpdateFileStatus.REJECTED);
            DBUtil.setNullableVarchar(ps, 2, comments);
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        } 
    }
    
    /**
     * Sets a file's status to 'invalid' if the file is marked bad.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk  primary key of the file to mark invalid.
     * @param comments Comments entered by the Apply Update stating the reason for the file to be invalid
     * @param userPk  user requesting to mark file invalid.
     */
    public void markFileInvalid(Integer queuePk, String comments, Integer userPk) { 
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(SQL_UPDATE_FILE_STATUS_WITH_COMMENTS);
            ps.setInt(1, UpdateFileStatus.INVALID);
            DBUtil.setNullableVarchar(ps, 2, comments);            
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, queuePk.intValue());

            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        } 
    }
    
    /**
     * Retrieves path to a file, given it's primary key.  This path needs to be accessible
     *  to all application server objects.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return The full path name of the file
     */
    public String getFileName(Integer queuePk) { 
        String fullFileName = null;
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement(SQL_SELECT_FILE_NAME);
            ps.setInt(1, queuePk.intValue());

            rs = ps.executeQuery();
            
            if (rs.next())
                fullFileName = rs.getString("file_storage_path") + rs.getString("file_nm");
                
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        } 
        
        return fullFileName;
    }
    
    /**
     * Retrieve a file queue record (FileEntry) from the database.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue.
     * @return FileEntry object
     */
    public FileEntry getFileEntry(Integer queuePk) {
        FileEntry fileEntry = null;
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_FILE);
            ps.setInt(1, queuePk.intValue());
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                fileEntry = new FileEntry();
                fileEntry.setQueuePk(new Integer(rs.getInt("queue_pk")));
                fileEntry.setFileName(rs.getString("file_nm"));
                fileEntry.setFilePath(rs.getString("file_storage_path"));
                fileEntry.setStatus(rs.getInt("upd_file_status"));
                fileEntry.setFileType(rs.getInt("upd_file_type"));
                fileEntry.setFileQueue(rs.getInt("file_queue"));
                fileEntry.setComments(rs.getString("upd_file_comments"));
                fileEntry.setProcessedDate(rs.getTimestamp("upd_file_processed_dt"));
                fileEntry.setValidDate(rs.getTimestamp("upd_file_valid_dt"));
                fileEntry.setReceivedDate(rs.getTimestamp("upd_file_received_dt"));
                fileEntry.setUpdateType(rs.getInt("upd_type"));
                fileEntry.setAffPk(new Integer(rs.getInt("aff_pk")));
                
                if (fileEntry.getFileType() != UpdateFileType.PARTICIPATION) {
                    
                    // get the affId (if not participation update file)
                    fileEntry.setAffId(affBean.getAffiliateData(fileEntry.getAffPk()).getAffiliateId());
                }    
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        } 
        
        return fileEntry;
        
    }
    /**
     * Gets a list of all the files for a particaular affiliate.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk primary key of the affiliate to get files for
     * @return List of FileEntry objects.
     */
    public List getFileList(Integer pk) { 
        return null;
    }
    /*********************************************************************************************/
    /**
     * Gets a list of all the files for a particaular affiliate.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk primary key of the affiliate to get files for
     * @return List of FileEntry objects.
     */
    public void setSortOrder(int order) { 
        this.order = order;
    }
    /***************************************************************************************************/
    /**
     * Gets a list of all the files for a particaular affiliate.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPks set of the affiliates to get files for
     * @return List of FileEntry objects.
     */
    public List getFileList(Set affPks) { 
        /****************************************************************/
        /* Define local variables
         ***************************************************************/
        List        allFiles    = new ArrayList();
        FileEntry   fileEntry;
        Connection  con         = null;
        Statement   stmt        = null;
        PreparedStatement ps    = null;
        ResultSet   rs          = null;
        Integer     affs        = null;
        String      where       = "";
        /****************************************************************/
        /* make a sql where clause to fit within a in clause
         ****************************************************************/
        Iterator    iAff        =   affPks.iterator();

        logger.info("affPks.isEmpty()=======================>" + affPks.isEmpty());
        while(iAff.hasNext()){
            if((affs != null)){
                where            =   where    +  " ,";
            }
            //get the next value
            logger.info("in=======================>" + affs);
            affs                =  (Integer) iAff.next() ;
            where               =   affs.toString();
        }
        try {
            
                
            con                 =   DBUtil.getConnection();
            ps                  =   con.prepareStatement(SQL_SELECT_AFF_FILES);
            
            
            ps.setString(1, where);
            
            rs                  =   ps.executeQuery();
            
            while (rs.next()) {
                fileEntry = new FileEntry();
                fileEntry.setQueuePk(new Integer(rs.getInt("queue_pk")));
                fileEntry.setFileName(rs.getString("file_nm"));
                fileEntry.setFilePath(rs.getString("file_storage_path"));
                fileEntry.setStatus(rs.getInt("upd_file_status"));
                fileEntry.setFileType(rs.getInt("upd_file_type"));
                fileEntry.setFileQueue(rs.getInt("file_queue"));
                fileEntry.setComments(rs.getString("upd_file_comments"));
                fileEntry.setProcessedDate(rs.getTimestamp("upd_file_processed_dt"));
                fileEntry.setValidDate(rs.getTimestamp("upd_file_valid_dt"));
                fileEntry.setReceivedDate(rs.getTimestamp("upd_file_received_dt"));
                fileEntry.setUpdateType(rs.getInt("upd_type"));
                fileEntry.setAffPk(new Integer(rs.getInt("aff_pk")));
                
                // get the affId
                fileEntry.setAffId(affBean.getAffiliateData(fileEntry.getAffPk()).getAffiliateId());
                
                allFiles.add(fileEntry);
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, stmt, rs);
        } 
        
        return allFiles;
    }
    /**
     * Gets a list of all the files in the queue.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @return List of FileEntry objects
     */
    public List getFileList() {
        return getFileList("");
    }
    /**
     * Gets a list of all the files in the queue.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @return List of FileEntry objects
     */
    public List getFileList(String whereSql) { 
        List allFiles = new ArrayList();
        
        Connection con              = null;
        Statement stmt              = null;
        ResultSet rs                = null;
        PreparedStatement ps        = null;
        FileEntry fileEntry;
        String            orderType = "";
        String            sql       = "";  
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            //******************************************************************************
            //swap the previous order to make sure we are changing sort order 
            if((order != 0) && (preOrder == order)){
                orderType       =   "Asc";
                preOrder        =   0;
            }else{
                orderType       =   "Desc";
                preOrder        =   order;
            }
            //****************************************************************************
            
            //if whereSql is not null then concatenate it
            if(!whereSql.equals("")){
                sql = SQL_SELECT_FILES + whereSql;
            }else{
                sql = SQL_SELECT_FILES;
            }
            //*************************************************************************
            //choose the sort order based on selection on the UI and make the order by clause
            switch(order){
                case Sort.RDATE:
                    
                    sql =   sql +   SPACE   +   SQL_RDATE_ORDER     +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.VALID_DATE:
                    
                    sql =   sql +   SPACE   +    SQL_VDATE_ORDER     +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.COMMENTS:
                    
                    sql =   sql +   SPACE   +   SQL_COMMENT_ORDER   +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.STATUS:
                    
                    sql =   sql +   SPACE   +   SQL_STATUS_ORDER    +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.FTYPE:
                    
                    sql =   sql +   SPACE   +   SQL_FTYPE_ORDER     +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.PROCESSED:
                    
                    sql =   sql +   SPACE   +   SQL_PROCESSED_ORDER +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.UPDATE_TYPE:
                    
                    sql =   sql +   SPACE   +   SQL_UPDATETYPE_ORDER+   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.TYPE:
                    
                    sql =   sql +   SPACE   +   SQL_AFF_TYPE_ORDER  +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.LOC:
                    
                    sql =   sql +   SPACE   +   SQL_LOC_ORDER      +   SPACE   +   SPACE   +   orderType;
                    break;                   
                case Sort.STATE:
                    
                    sql =   sql +   SPACE   +   SQL_STATE_ORDER     +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.SUB_UNIT:
                    
                    sql =   sql +   SPACE   +   SQL_SUB_UNIT_ORDER  +   SPACE   +   SPACE   +   orderType;
                    break;
                case Sort.CN_CHAP:
                    
                    sql =   sql +   SPACE   +   SQL_CN_CHAP_ORDER   +   SPACE   +   SPACE   +   orderType;
                    break;
                default:
                    
                    sql =   sql +   SPACE   +   SQL_QPK_ORDER;
                    break;
            }
            logger.debug("sql=============>" + sql);
            rs = stmt.executeQuery(sql);
            
            
            while (rs.next()) {
                fileEntry = new FileEntry();
                fileEntry.setQueuePk(new Integer(rs.getInt("queue_pk")));
                fileEntry.setFileName(rs.getString("file_nm"));
                fileEntry.setFilePath(rs.getString("file_storage_path"));
                fileEntry.setStatus(rs.getInt("upd_file_status"));
                fileEntry.setFileType(rs.getInt("upd_file_type"));
                fileEntry.setFileQueue(rs.getInt("file_queue"));
                fileEntry.setComments(rs.getString("upd_file_comments"));
                fileEntry.setProcessedDate(rs.getTimestamp("upd_file_processed_dt"));
                fileEntry.setValidDate(rs.getTimestamp("upd_file_valid_dt"));
                fileEntry.setReceivedDate(rs.getTimestamp("upd_file_received_dt"));
                fileEntry.setUpdateType(rs.getInt("upd_type"));
                fileEntry.setAffPk(new Integer(rs.getInt("aff_pk")));
                
                // get the affId
                fileEntry.setAffId(affBean.getAffiliateData(fileEntry.getAffPk()).getAffiliateId());
                
                allFiles.add(fileEntry);
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, stmt, rs);
        } 
        
        return allFiles;
    }
    /**
     *gets the list of file from AUP_Queue_Mgmtbased on the param passed
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     * @return List of FileEntry objects
     *
     *
     *@param council
     *
     */
     
    public List getFileList(String      council     , 
                            String      affLocal    , 
                            String      affState    ,
                            String      affSubunit  ,
                            Character   affType     ,
                            String      fromDate    ,
                            String      toDate      ,
                            int         updateType){
        
         String where           =   "";
         //*************************************************************************
         //dynamically make a where clause and the pass it to add to the main sql      
         if((council     !=  null) && (!council.equals(""))){
             where          =  where + makeWhere(SQL_COUNCIL,  EQUAL,   council );  
         }
         
         if((affLocal    !=  null) && (!affLocal.equals(""))){                                  
             where          =  where + makeWhere(SQL_LOCAL  ,  EQUAL,   affLocal)   ;
         }
         
         if((affState    !=  null) && (!affState.equals(""))){
             where          =  where + makeWhere(SQL_STATE  ,   EQUAL,  affState)   ;
         }
         
         if((affSubunit  !=  null) && (!affSubunit.equals(""))){
             where          =  where + makeWhere(SQL_SUB_UNIT,  EQUAL,  affSubunit)   ;
         }
         
         if(((affType !=null ) && (!affType.toString().equals(" ")))){
             logger.debug("affType ===========>" + affType.charValue());
             where          =  where + makeWhere(SQL_AFF_TYPE , EQUAL, affType)   ;
         }
         
         if(updateType   !=  0  ) {
             where          =  where + makeWhere(SQL_UPD_TYPE , EQUAL, updateType)   ;
         }
         
         if(((fromDate != null) && (!fromDate.equals(""))) && ((toDate !=null) && (!toDate.equals("")))){
             
             where          =  where +  makeWhere(SQL_UPD_PRCD_DATE, GREATERTHAN + EQUAL , fromDate)   ;             
             where          =  where +  makeWhere(SQL_UPD_PRCD_DATE, LESSTHAN    + EQUAL , toDate, 1)   ;            
         }else if((fromDate != null) && (!fromDate.equals(""))){
            
             where          =  where +  makeWhere(SQL_UPD_PRCD_DATE, GREATERTHAN + EQUAL , fromDate)   ;
             
         }
         logger.debug("----------------------------------------------------");
         logger.debug("where===>" + where);
         
         
         return getFileList(where);
    }//end of getFileList
    
    //******************************************************************************************
    //Accepts a String value
    private String makeWhere(String fieldName, String operator, String value){
               
       
            return SPACE + SQL_AND + fieldName + SPACE + operator + SPACE + QUOTE + value + QUOTE;
  
        
    }//end of makeWhere method
    //******************************************************************************************
    //Accepts a String value
    private String makeWhere(String fieldName, String operator, String value, int dayToAdd){
               
       
            return SPACE + SQL_AND + fieldName + SPACE + operator + SPACE + "DATEADD(DD, " + dayToAdd + COMMA +  QUOTE + value + QUOTE + ")" ;
  
        
    }//end of makeWhere method
    //*******************************************************************************
    //Accepts int value
    private String makeWhere(String fieldName, String operator, int value){
         
             return SPACE + SQL_AND + fieldName + SPACE + operator + SPACE +  value ;

         
    }//end of makeWhere method
    //**********************************************************************************
    //accepts Character value
    private String makeWhere(String fieldName, String operator, Character value){
         
             return SPACE + SQL_AND + fieldName + SPACE + operator + SPACE + QUOTE + value + QUOTE; 

        
    }//end of makeWhere method
}