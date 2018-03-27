package org.afscme.enterprise.rebate.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;
import java.rmi.RemoteException;
import java.math.BigDecimal;
import javax.ejb.*;
import org.afscme.enterprise.util.*;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.rebate.PRB12MonthRebateAmount;
import org.apache.log4j.Logger;
import org.afscme.enterprise.rebate.*;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.person.PRBCheckInfo;
import org.afscme.enterprise.person.PRBRequestData;
import org.afscme.enterprise.person.PRBApplicationData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.codes.Codes.RebateStatus;
import org.afscme.enterprise.codes.Codes.RebateAppEvalCode;
import org.afscme.enterprise.codes.Codes.RebateAcceptanceCode;
import org.afscme.enterprise.codes.Codes.PRBRosterStatus;
import org.afscme.enterprise.reporting.specialized.PreliminaryRosterAffiliate;

/**
 * Encapsulates access to political rebate data.
 *
 * @ejb:bean name="MaintainPoliticalRebate" display-name="MaintainPoliticalRebate"
 * jndi-name="MaintainPoliticalRebate"
 * type="Stateless" view-type="local"
 */

public class MaintainPoliticalRebateBean extends SessionBase {
    
    private static Logger log = Logger.getLogger(MaintainPoliticalRebateBean.class);
    
    ///////////////////////////////////////////////
    // COMMON METHODS
    ///////////////////////////////////////////////
    
    /** Check if the affiliate already exists under the specified Rebate Application */
    private static final String SQL_SELECT_EXISTENCE_PRB_APPLICATION_AFFILIATES = 
    "SELECT 1 " +
    "FROM PRB_App_Affs " +
    "WHERE prb_app_pk=? and aff_pk=?";
    
    /** Selects an individual's political rebate request affiliates count */
    private static final String SQL_SELECT_PRB_REQUEST_AFFILIATES_COUNT =
    "SELECT count(*) " +
    "FROM PRB_Requests r " +
    "JOIN PRB_Request_Affs a on a.rqst_pk = r.rqst_pk " +
    "JOIN Aff_Organizations o on o.aff_pk = a.aff_pk " +
    "WHERE r.person_pk=? and r.rqst_pk=?";
    
    /** Selects an individual's political rebate application affiliates count */
    private static final String SQL_SELECT_PRB_APPLICATION_AFFILIATES_COUNT =
    "SELECT count(*) " +
    "FROM PRB_Apps app " +
    "JOIN PRB_Requests r on app.prb_app_pk = r.prb_app_pk " +
    "JOIN PRB_App_Affs aff on app.prb_app_pk = aff.prb_app_pk " +
    "JOIN Aff_Organizations org on org.aff_pk = aff.aff_pk " +
    "WHERE r.person_pk=? and r.prb_app_pk=?";
    
    /** Selects an individual's political rebate request affiliates information */
    private static final String SQL_SELECT_PRB_REQUEST_AFFILIATES =
    "SELECT distinct a.aff_pk, aff_type, aff_code, aff_localSubChapter, " +
    "       aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, " +
    "       isnull(rqst_duration_in_aff, 0), isnull(rqst_filed_with, 0) " +
    "FROM PRB_Requests r " +
    "JOIN PRB_Request_Affs a on a.rqst_pk = r.rqst_pk " +
    "JOIN Aff_Organizations o on o.aff_pk = a.aff_pk " +
    "WHERE r.person_pk=? and r.rqst_pk=?";
    
    /** Selects an individual's political rebate application affiliates information */
    private static final String SQL_SELECT_PRB_APPLICATION_AFFILIATES =
    "SELECT distinct aff.aff_pk, aff_type, aff_code, aff_localSubChapter, " +
    "       aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, " +
    "       isnull(app_duration_in_aff, 0), isnull(app_filed_with, 0) " +
    "FROM PRB_Apps app " +
    "JOIN PRB_Requests r on r.prb_app_pk = app.prb_app_pk " +
    "JOIN PRB_App_Affs aff on aff.prb_app_pk = app.prb_app_pk " +
    "JOIN Aff_Organizations org on org.aff_pk = aff.aff_pk " +
    "WHERE r.person_pk=? and r.prb_app_pk=?";
    
    
    /** Inserts an individual's political rebate request affiliate information */
    private static final String SQL_INSERT_PRB_REQUEST_AFFILIATE =
    "INSERT INTO PRB_Request_Affs " +
    "(aff_pk, rqst_filed_with, rqst_duration_in_aff, rqst_months_in_affiliate, rqst_pk) " +
    "VALUES (?,?,?,null,?) ";
    
    /** Updates an individual's political rebate request affiliate information */
    private static final String SQL_UPDATE_PRB_REQUEST_AFFILIATE =
    "UPDATE PRB_Request_Affs " +
    "SET    rqst_filed_with=?, rqst_duration_in_aff=? " +
    "WHERE  aff_pk=? and rqst_pk=?";
    
    /** Inserts an individual's political rebate request affiliate information */
    private static final String SQL_INSERT_PRB_APPLICATION_AFFILIATE =
    "INSERT INTO PRB_App_Affs " +
    "(aff_pk, app_filed_with, app_duration_in_aff, app_months_in_aff, prb_app_pk) " +
    "VALUES (?,?,?,null,?) ";
    
    /** Updates an individual's political rebate application affiliate information */
    private static final String SQL_UPDATE_PRB_APPLICATION_AFFILIATE =
    "UPDATE PRB_App_Affs " +
    "SET    app_filed_with=?, app_duration_in_aff=? " +
    "WHERE  aff_pk=? and prb_app_pk=?";
    
    /** Removes an affiliate from the political rebate request */
    private static final String SQL_DELETE_PRB_REQUEST_AFFILIATE =
    "DELETE FROM PRB_Request_Affs " +
    "WHERE rqst_pk=? and aff_pk=? ";
    
    /** Removes an affiliate from the political rebate application */
    private static final String SQL_DELETE_PRB_APPLICATION_AFFILIATE =
    "DELETE FROM PRB_App_Affs " +
    "WHERE prb_app_pk=? and aff_pk=? ";
    
    
    /**
     * @return Integer
     */
    private Integer setIntToInteger(int i) {
        return (i<1) ? null : new Integer(i);
    }
    
    /**
     * @return String
     */
    private String setIntToString(int i) {
        return (i<1) ? null : new Integer(i).toString();
    }
    
    /**
     * @return boolean true if the year is the prior rebate year
     */
    public boolean isPriorRebateYear(Integer rbtYear) {
        Calendar cal = Calendar.getInstance();
        return (rbtYear.intValue() < cal.get(Calendar.YEAR)-1) ? true : false;
    }
    
    /**
     * @return int - The current rebate year
     */
    public int getCurrentRebateYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR)-1;
    }
    
    
    /**
     * @J2EE_METHOD  --  getPRBAffiliatesCount
     * Check if the affiliate already exists under the specified Rebate Application 
     *
     * @param prbAppPk The Application Primary Key
     * @param affPk The Affiliate Primary Key
     *
     * @return true if exists, false otherwise
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isPRBAppAffiliateExist(Integer prbAppPk, Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EXISTENCE_PRB_APPLICATION_AFFILIATES);
            ps.setInt(1, prbAppPk.intValue());
            ps.setInt(2, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return exists;
    }

    
    /**
     * @J2EE_METHOD  --  getPRBAffiliatesCount
     * Retrieves the total count of affiliates associated with a person's
     * rebate request or application.
     *
     * @param personPk Person Primary Key
     * @param prbPk PRB Request or Application Primary Key
     * @param prbLevel The Rebate Process Level - PRB Request or Application
     *
     * @return the total count
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getPRBAffiliatesCount(Integer personPk, int prbPk, int prbLevel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;
        
        try {
            con = DBUtil.getConnection();
            if (prbLevel == PRBConstants.PRB_REQUEST)
                ps = con.prepareStatement(SQL_SELECT_PRB_REQUEST_AFFILIATES_COUNT);
            else
                ps = con.prepareStatement(SQL_SELECT_PRB_APPLICATION_AFFILIATES_COUNT);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, prbPk);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return total;
    }
    
    
    /**
     * @J2EE_METHOD  --  getPRBAffiliates
     * Retrieves the affiliates associated with a person's rebate request or application.
     *
     * @param personPk Person Primary Key
     * @param prbPk Rebate Request/Application Primary Key
     * @param prbLevel The Rebate Process Level - PRB Request or Application
     *
     * @return the Collection of PRBAffiliateData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPRBAffiliates(Integer personPk, int prbPk, int prbLevel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PRBAffiliateData prbData = null;
        AffiliateIdentifier affId = null;
        List list = new ArrayList(2);
        
        try {
            con = DBUtil.getConnection();
            if (prbLevel == PRBConstants.PRB_REQUEST)
                ps = con.prepareStatement(SQL_SELECT_PRB_REQUEST_AFFILIATES);
            else
                ps = con.prepareStatement(SQL_SELECT_PRB_APPLICATION_AFFILIATES);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, prbPk);
            rs = ps.executeQuery();
            while (rs.next()) {
                prbData = new PRBAffiliateData();
                affId = new AffiliateIdentifier();
                prbData.setAffPk(new Integer(rs.getInt(1)));
                affId.setType(new Character(rs.getString(2).charAt(0)));
                affId.setCode(new Character(rs.getString(3).charAt(0)));
                affId.setLocal(rs.getString(4));
                affId.setState(rs.getString(5));
                affId.setSubUnit(rs.getString(6));
                affId.setCouncil(rs.getString(7));
                prbData.setDurationPk(new Integer(rs.getInt(8)));
                prbData.setFiledWithPk(new Integer(rs.getInt(9)));
                prbData.setTheAffiliateIdentifier(affId);
                list.add(prbData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  setPRBAffiliate
     * Updates an association between a person's political rebate request or application
     * and an affiliate.
     *
     * @param prbAffData the Affiliate Request data
     * @param rqstPk Political Rebate Request Primary Key
     * @param prbLevel The Rebate Process Level - PRB Request or Application
     *
     * @return int The result value.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int setPRBAffiliate(PRBAffiliateData prbAffData, Integer prbPk, int prbLevel) {
        Connection con = null;
        PreparedStatement ps = null;
        int rs = 0;
        try {
            con = DBUtil.getConnection();
            if (prbLevel == PRBConstants.PRB_REQUEST)
                ps = con.prepareStatement(SQL_UPDATE_PRB_REQUEST_AFFILIATE);
            else
                ps = con.prepareStatement(SQL_UPDATE_PRB_APPLICATION_AFFILIATE);
            DBUtil.setNullableInt(ps, 1, prbAffData.getFiledWithPk());
            DBUtil.setNullableInt(ps, 2, prbAffData.getDurationPk());
            ps.setInt(3, prbAffData.getAffPk().intValue());
            ps.setInt(4, prbPk.intValue());
            rs = ps.executeUpdate();
            // Update unsuccessful - try to Add
            if (rs == 0) {
                rs = createPRBAffiliate(prbAffData, prbPk, prbLevel);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return rs;
    }
    
    
    /**
     * @J2EE_METHOD  --  createPRBAffiliate
     * Creates an association between a person's political rebate request or application
     * and an affiliate.
     *
     * @param the PRBAffiliateData object
     * @param prbPk Political Rebate Request Primary Key
     * @param prbLevel The Rebate Process Level - PRB Request or Application
     *
     * @return int The result value
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int createPRBAffiliate(PRBAffiliateData prbAffData, Integer prbPk, int prbLevel) {
        Connection con = null;
        PreparedStatement ps = null;
        int rs = 0;
        
        // Get the total PRB Request Affiliates count first.
        // Business Rule: A person/member cannot request a Rebate for more than 3 Affiliates
        // across all requests for the rebate year
        if (prbLevel == PRBConstants.PRB_REQUEST) {
            if (getPRBAffiliatesCount(prbAffData.getTheRecordData().getPk(), prbPk.intValue(), prbLevel) >= PRBConstants.MAX_REQUEST_AFFILIATE_ALLOWED)
                return PRBConstants.ERROR_MAX_REQUEST_AFFILIATE_ALLOWED;
        }
        
        try {
            con = DBUtil.getConnection();
            if (prbLevel == PRBConstants.PRB_REQUEST)
                ps = con.prepareStatement(SQL_INSERT_PRB_REQUEST_AFFILIATE);
            else
                ps = con.prepareStatement(SQL_INSERT_PRB_APPLICATION_AFFILIATE);
            ps.setInt(1, prbAffData.getAffPk().intValue());
            DBUtil.setNullableInt(ps, 2, prbAffData.getFiledWithPk());
            DBUtil.setNullableInt(ps, 3, prbAffData.getDurationPk());
            ps.setInt(4, prbPk.intValue());
            rs = ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return rs;
    }
    
    /**
     * @J2EE_METHOD  --  removePRBAffiliate
     * Removes the association of an afffiliate with a person's rebate request or application.
     *
     * @param prbPk Political Rebate Request or Application Primary Key
     * @param affPk Affiliate Primary Key
     * @param prbLevel The Rebate Process Level - PRB Request or Application
     *
     * @return TRUE if removal completes, and FALSE if an error occurs.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean removePRBAffiliate(Integer prbPk, Integer affPk, int prbLevel) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result;
        
        try {
            con = DBUtil.getConnection();
            
            // Remove the Affiliate from the political rebate request or application
            if (prbLevel == PRBConstants.PRB_REQUEST)
                ps = con.prepareStatement(SQL_DELETE_PRB_REQUEST_AFFILIATE);
            else
                ps = con.prepareStatement(SQL_DELETE_PRB_APPLICATION_AFFILIATE);
            ps.setInt(1, prbPk.intValue());
            ps.setInt(2, affPk.intValue());
            result = (ps.executeUpdate() != 0);
        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return result;
    }
    
    
    ///////////////////////////////////////////////
    // POLITICAL REBATE REQUEST
    ///////////////////////////////////////////////
    
    /** Selects request primary key */
    private static final String SQL_SELECT_PRB_REQUEST_PK =
    "SELECT rqst_pk " +
    "FROM   PRB_Requests " +
    "WHERE  person_pk=? and rqst_dt=? and rqst_rebate_year=? ";
    
    /** Selects a political rebate request */
    private static final String SQL_SELECT_PRB_REQUEST =
    "SELECT prb_app_pk, isnull(rqst_status, 0), rqst_dt, rqst_rebate_year, " +
    "       rqst_cert_mail_num, rqst_denied_fg, rqst_denied_dt, isnull(rqst_denied_reason, 0), " +
    "       rqst_resubmit_fg, rqst_resubmit_denied_dt, isnull(rqst_resubmit_denied_reason, 0), " +
    "       rqst_keyed_dt, comment_txt " +
    "FROM PRB_Requests " +
    "WHERE person_pk=? and rqst_pk=? ";
    
    /** Selects all political rebate requests for an individual for a rebate year */
    private static final String SQL_SELECT_PRB_REQUESTS =
    "SELECT r.rqst_pk, rqst_dt, isnull(prb_app_pk, 0), " +
    "       (SELECT cc.com_cd_desc FROM Common_Codes cc WHERE cc.com_cd_pk = r.rqst_status) " +
    "FROM PRB_Requests r " +
    "WHERE person_pk=? and rqst_rebate_year=? " +
    "ORDER BY rqst_dt ";
    
    /** Inserts a political rebate request */
    private static final String SQL_INSERT_PRB_RBT_YEAR_INFO =
    "INSERT INTO PRB_Rbt_Year_Info " +
    " (person_pk, rbt_year, comment_txt, person_rbt_year_status) " +
    "VALUES (?, ?, ?, null) ";
    
    /** Inserts a political rebate request */
    private static final String SQL_INSERT_PRB_REQUEST =
    "INSERT INTO PRB_Requests " +
    " (person_pk, prb_app_pk, rqst_status, rqst_dt, rqst_rebate_year, rqst_cert_mail_num, " +
    "  rqst_denied_fg, rqst_denied_dt, rqst_denied_reason, rqst_resubmit_fg, rqst_resubmit_denied_dt, " +
    "  rqst_resubmit_denied_reason, rqst_keyed_dt, comment_txt, " +
    "  created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) " +
    "VALUES (?,null,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),?,getdate(),?) ";
    
    /** Updates the political request record */
    private static final String SQL_UPDATE_PRB_REQUEST =
    "UPDATE PRB_Requests " +
    "SET rqst_status=?, rqst_dt=?, " +
    "    rqst_cert_mail_num=?, rqst_denied_fg=?, rqst_denied_dt=?, rqst_denied_reason=?, " +
    "    rqst_resubmit_fg=?, rqst_resubmit_denied_dt=?, rqst_resubmit_denied_reason=?, " +
    "    rqst_keyed_dt=?, comment_txt=?, lst_mod_dt=getdate(), lst_mod_user_pk=? " +
    "WHERE rqst_pk=? and person_pk=? and rqst_rebate_year=?";
    /**
     * @J2EE_METHOD  --  getPRBRequestPk
     * Retrieves the political rebate request primary key based on personPk, requestDate, and rebate year
     *
     * @param personPk Person Primary Key
     * @param rqstDt Request Date
     * @param rqstRbtYear Request Rebate Year
     *
     * @return Integer the Request Primary Key.
     */
    public Integer getPRBRequestPk(int personPk, Timestamp rqstDt, String rqstRbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer rqstPk = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_REQUEST_PK);
            ps.setInt(1, personPk);
            ps.setTimestamp(2, rqstDt);
            ps.setInt(3, new Integer(rqstRbtYear).intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                rqstPk = new Integer(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return rqstPk;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBRequest
     * Retrieves the data associated with a political rebate request.
     *
     * @param personPk Person Primary Key
     * @param prbRqstPk Rebate Request Primary Key
     *
     * @return the PRBRequestData object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PRBRequestData getPRBRequest(Integer personPk, int prbRqstPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PRBRequestData result = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_REQUEST);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, prbRqstPk);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = new PRBRequestData();
                result.setRqstPk(prbRqstPk);
                result.setPrbAppPK(setIntToInteger(rs.getInt(1)));
                result.setRqstStatus(new Integer(rs.getInt(2)));
                result.setRqstDt(rs.getTimestamp(3));
                result.setRqstRebateYear(setIntToString(rs.getInt(4)));
                result.setRqstCertMailNum(rs.getString(5));
                result.setRqstDeniedFg(DBUtil.getBooleanFromShort(rs.getShort(6)));
                result.setRqstDeniedDt(rs.getTimestamp(7));
                result.setRqstDeniedReason(setIntToInteger(rs.getInt(8)));
                result.setRqstResubmitFg(DBUtil.getBooleanFromShort(rs.getShort(9)));
                result.setRqstResubmitDeniedDt(rs.getTimestamp(10));
                result.setRqstResubmitDeniedReason(setIntToInteger(rs.getInt(11)));
                result.setRqstKeyedDt(rs.getTimestamp(12));
                result.setCommentsTxt(rs.getString(13));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBRequests
     * Retrieves information on Political Rebate requests made for this person for a given
     *  year..
     *
     * @param personPk Person Primary Key
     * @param requestYear Year for the Requests
     *
     * @return the List of PRBRequestData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPRBRequests(Integer personPk, String requestYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new LinkedList();
        PRBSummary result = null;
        AffiliateIdentifier affId = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_REQUESTS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, new Integer(requestYear).intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                result = new PRBSummary();
                result.setPk(rs.getInt(1));
                result.setRbtRequestDate(DateUtil.getSimpleDateString(rs.getTimestamp(2)));
                Integer appPk = setIntToInteger(rs.getInt(3));
                if (appPk != null) {
                    result.setRbtMailedDate(DateUtil.getSimpleDateString(getPRBAppMailedDate(appPk.intValue())));
                }
                result.setRbtStatus(rs.getString(4));
                result.setPrbAffiliateData(getPRBAffiliates(personPk, result.getPk(), PRBConstants.PRB_REQUEST));
                result.setFirstAffiliateIdentifier();
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  addPRBRbtYearInfo
     * Adds a political rebate year to the database, for a person, at a given rebate year
     *
     * IF a record does not exist for the rbt_year THEN insert ELSE update the record
     * in PRB_Rbt_Year_Info AND SET the person_rbt_year_status to "In Progress".
     *
     * @param prbRequestData Rebate Request Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void addPRBRbtYearInfo(PRBRequestData prbRequestData) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean rs = false;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PRB_RBT_YEAR_INFO);
            ps.setInt(1, prbRequestData.getPersonPk());
            DBUtil.setNullableInt(ps, 2, prbRequestData.getRqstRebateYear());
            DBUtil.setNullableVarchar(ps, 3,  prbRequestData.getCommentsTxt());
            rs = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        if (!rs) {
            updatePRBYearInfoStatus(RebateStatus.IP.intValue(), prbRequestData.getPersonPk(), new Integer(prbRequestData.getRqstRebateYear()));
        }
    }
    
    
    /**
     * @J2EE_METHOD  --  addPRBRequest
     * Adds a political rebate request to the database, for a person, at a given date (and
     *  referencing a rebate year).
     *
     * IF a record does not exist for the person_pk and rqst_dt THEN insert ELSE get the
     * PRB Request primary key and return it to the caller
     *
     * @param prbRequestData Rebate Request Data
     * @param userPk User PK of the person who creates the record
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Integer addPRBRequest(PRBRequestData prbRequestData, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        Integer rqstPk = null;
        
        int rqstStatus = RebateStatus.A.intValue();
        
        if ((prbRequestData.getRqstDeniedFg().booleanValue() &&
            !prbRequestData.getRqstResubmitFg().booleanValue()) || 
            prbRequestData.getRqstResubmitDeniedDt() != null) {
            rqstStatus = RebateStatus.D.intValue();
        }     
        
        // Add the new rebate year record to the PRB_Rbt_Year_Info table
        addPRBRbtYearInfo(prbRequestData);
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PRB_REQUEST);
            DBUtil.setNullableInt(ps, 1, prbRequestData.getPersonPk());
            DBUtil.setNullableInt(ps, 2, rqstStatus);
            DBUtil.setNullableTimestamp(ps, 3, prbRequestData.getRqstDt());
            DBUtil.setNullableInt(ps, 4, prbRequestData.getRqstRebateYear());
            DBUtil.setNullableVarchar(ps, 5, prbRequestData.getRqstCertMailNum());
            DBUtil.setBooleanAsShort(ps, 6, prbRequestData.getRqstDeniedFg().booleanValue());
            DBUtil.setNullableTimestamp(ps, 7, prbRequestData.getRqstDeniedDt());
            DBUtil.setNullableInt(ps, 8, prbRequestData.getRqstDeniedReason());
            DBUtil.setBooleanAsShort(ps, 9, prbRequestData.getRqstResubmitFg().booleanValue());
            DBUtil.setNullableTimestamp(ps, 10, prbRequestData.getRqstResubmitDeniedDt());
            DBUtil.setNullableInt(ps, 11, prbRequestData.getRqstResubmitDeniedReason());
            ps.setTimestamp(12, prbRequestData.getRqstKeyedDt());
            DBUtil.setNullableVarchar(ps, 13,  prbRequestData.getCommentsTxt());
            ps.setInt(14, userPk.intValue());
            ps.setInt(15, userPk.intValue());
            rqstPk = DBUtil.insertAndGetIdentity(con, ps);
        } catch (SQLException e) {
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        // Get the PRB Request primary key based on person pk and request date
        if (rqstPk == null) {
            rqstPk = getPRBRequestPk(prbRequestData.getPersonPk(), prbRequestData.getRqstDt(), prbRequestData.getRqstRebateYear());
        }
        
        // Return political rebate request primary key
        return rqstPk;
    }
    
    
    /**
     * @J2EE_METHOD  --  updatePRBRequest
     * Allows the Political Rebate Request data for a person to be updated.
     *
     * IF rqstDenied = TRUE OR rqstResubmitDeniedDt is not null
     * THEN set rqstStatus = "Denied"
     * ELSE set rqstStatus = "Approved"
     *
     * IF rqstStatus = "Denied" AND all other requests for this person are "Denied"
     * THEN update PRB_Rbt_Year_Info.person_rbt_year_status to "Denied"
     *
     * IF rqstStatus = "Approved" THEN update PRB_Rbt_Year_Info.person_rbt_year_status
     * to "In Process"
     *
     * @param prbRequestData Data object containing the Request data.
     * @param userPk User PK of the person who creates the record
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePRBRequest(PRBRequestData prbRequestData, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        
        int rqstStatus = RebateStatus.A.intValue();
        
        if ((prbRequestData.getRqstDeniedFg().booleanValue() &&
            !prbRequestData.getRqstResubmitFg().booleanValue()) || 
            prbRequestData.getRqstResubmitDeniedDt() != null) {
            rqstStatus = RebateStatus.D.intValue();
        }
       
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_REQUEST);
            DBUtil.setNullableInt(ps, 1, rqstStatus);
            DBUtil.setNullableTimestamp(ps, 2, prbRequestData.getRqstDt());
            DBUtil.setNullableVarchar(ps, 3, prbRequestData.getRqstCertMailNum());
            DBUtil.setBooleanAsShort(ps, 4, prbRequestData.getRqstDeniedFg().booleanValue());
            DBUtil.setNullableTimestamp(ps, 5, prbRequestData.getRqstDeniedDt());
            DBUtil.setNullableInt(ps, 6, prbRequestData.getRqstDeniedReason());
            DBUtil.setBooleanAsShort(ps, 7, prbRequestData.getRqstResubmitFg().booleanValue());
            DBUtil.setNullableTimestamp(ps, 8, prbRequestData.getRqstResubmitDeniedDt());
            DBUtil.setNullableInt(ps, 9, prbRequestData.getRqstResubmitDeniedReason());
            ps.setTimestamp(10, prbRequestData.getRqstKeyedDt());
            DBUtil.setNullableVarchar(ps, 11,  prbRequestData.getCommentsTxt());
            ps.setInt(12, userPk.intValue());
            ps.setInt(13, prbRequestData.getRqstPk());
            DBUtil.setNullableInt(ps, 14, prbRequestData.getPersonPk());
            DBUtil.setNullableInt(ps, 15, prbRequestData.getRqstRebateYear());
            boolean rs = (ps.executeUpdate() != 0);
            if (!rs) {
                addPRBRequest(prbRequestData, userPk);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        // Update the rebate status in PRB_Rbt_Year_Info table
        int rbtStatus = getRebateRequestStatusCode(prbRequestData.getPersonPk(), new Integer(prbRequestData.getRqstRebateYear()));
        updatePRBYearInfoStatus(rbtStatus, prbRequestData.getPersonPk(), new Integer(prbRequestData.getRqstRebateYear()));
    }
    
    ///////////////////////////////////////////////
    // POLITICAL REBATE APPLICATION
    ///////////////////////////////////////////////
    
    /** Selects app_mailed_dt from PRB_Apps table  */
    private static final String SQL_SELECT_PRB_APP_MAILED_DT =
    "SELECT app_mailed_dt " +
    "FROM   PRB_Apps " +
    "WHERE  prb_app_pk=?";
    
    /** Selects variable_name from app_config_data table  */
    private static final String SQL_SELECT_APP_CONFIG_VALUE =
    "SELECT variable_value " +
    "FROM   COM_App_Config_Data " +
    "WHERE  variable_name=?";
    
    /** Selects a political rebate application */
    private static final String SQL_SELECT_PRB_APPLICATION =
    "SELECT app_mailed_dt, app_returned_dt, isnull(prb_comment_anal_cd, 0), " +
    "       isnull(prb_evaluation_cd, 0), a.comment_txt, aff_roster_generated_fg, rqst_rebate_year " +
    "FROM   PRB_Apps a " +
    "JOIN   PRB_Requests r on r.prb_app_pk = a.prb_app_pk " +
    "WHERE  r.person_pk=? and r.prb_app_pk=? ";
    
    /** Selects all political rebate application for an individual for the rebate year */
    private static final String SQL_SELECT_PRB_APPLICATIONS =
    "SELECT distinct a.prb_app_pk, app_mailed_dt, " +
    "       (SELECT cc.com_cd_desc FROM Common_Codes cc WHERE cc.com_cd_pk = a.app_status) " +
    "FROM PRB_Apps a " +
    "JOIN PRB_Requests r on r.prb_app_pk = a.prb_app_pk " +
    "WHERE person_pk=? and rqst_rebate_year=? " +
    "ORDER BY app_mailed_dt ";
    
    /** Updates the political application record */
    private static final String SQL_UPDATE_PRB_APPLICATION =
    "UPDATE PRB_Apps " +
    "SET app_mailed_dt=?, app_returned_dt=?, prb_comment_anal_cd=?, " +
    "    prb_evaluation_cd=?, comment_txt=?, " +
    "    lst_mod_dt=getdate(), lst_mod_user_pk=? " +
    "WHERE prb_app_pk=? ";
    
    /** Updates the political application status */
    private static final String SQL_UPDATE_PRB_APPLICATION_STATUS =
    "UPDATE PRB_Apps " +
    "SET app_status=?, aff_roster_generated_fg=?, " +
    "    lst_mod_dt=getdate(), lst_mod_user_pk=? " +
    "WHERE prb_app_pk=? ";
    
    
    /**
     * @J2EE_METHOD  --  getPRBAppMailedDate
     * This method retrieves the political rebate application mailed date
     *
     * @return Timestamp the PRB Application Mailed Date
     * @param int The primary key of the political rebate application
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Timestamp getPRBAppMailedDate(int prbAppPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp mailedDt = null;
        
        // This is not a valid appPk
        if (prbAppPk <= 0) {
            return mailedDt;
        }
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_APP_MAILED_DT);
            ps.setInt(1, prbAppPk);
            rs = ps.executeQuery();
            if (rs.next()) {
                mailedDt = rs.getTimestamp(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return mailedDt;
    }
    
    /**
     * @J2EE_METHOD  --  getAppConfigData
     * Retrieves the value of the specified configuration data
     *
     * @param variableName The Config Variable Name
     * @return the Config Value of the variableName
     */
    public String getAppConfigData(String variableName) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_APP_CONFIG_VALUE);
            ps.setString(1, variableName);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBApplication
     * Retrieves the data associated with a political rebate application.
     *
     * IF PRB_Apps.app_mailed_dt > COM_App_Config_Data.variable_value
     * WHERE
     * COM_App_Config_Data.variable_name = "Rebate Application Mailed Date"
     * THEN set PRB_Apps.prb_evaluation_cd = "Not Timely"
     *
     * @param personPk Person Primary Key
     * @param prbAppPk Rebate Application Primary Key
     *
     * @return the PRBApplicationData object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PRBApplicationData getPRBApplication(Integer personPk, Integer prbAppPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PRBApplicationData result = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_APPLICATION);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, prbAppPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = new PRBApplicationData();
                result.setPersonPK(personPk);
                result.setPrbAppPK(prbAppPk);
                result.setAppMailedDt(rs.getTimestamp(1));
                result.setAppReturnedDt(rs.getTimestamp(2));
                result.setPrbCommentAnalCd(setIntToInteger(rs.getInt(3)));
                result.setPrbEvaluationCd(setIntToInteger(rs.getInt(4)));
                result.setCommentTxt(rs.getString(5));
                result.setAffRosterGeneratedFg(DBUtil.getBooleanFromShort(rs.getShort(6)));
                result.setRbtYear(setIntToInteger(rs.getInt(7)));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        if (result!=null) {
            // Get the duration of App Mailed Date
            // If the duration is passed the number of days allowed, then set the
            // Application Evaluation Code to Not Timely...
            String val = getAppConfigData(PRBConstants.CONFIG_VARIABLE_APP_MAILED_DT);
            if (result.getAppMailedDt() != null) {
                long dayDiff = (System.currentTimeMillis() - result.getAppMailedDt().getTime()) / 86400000;
                if (val!=null && dayDiff > new Integer(val).longValue()) {
                    result.setPrbEvaluationCd(RebateAppEvalCode.NT);
                }
            }
        }
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBApplications
     * Retrieves information on Political Rebate Application made for this person for a given
     * year..
     *
     * @param personPk Person Primary Key
     * @param prbYear Year for the Rebate
     *
     * @return the Collection of PRBApplicationData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPRBApplications(Integer personPk, String prbYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new LinkedList();
        PRBSummary result = null;
        AffiliateIdentifier affId = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_APPLICATIONS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, new Integer(prbYear).intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                result = new PRBSummary();
                result.setPk(rs.getInt(1));
                result.setRbtMailedDate(DateUtil.getSimpleDateString(rs.getTimestamp(2)));
                result.setRbtStatus(rs.getString(3));
                result.setPrbAffiliateData(getPRBAffiliates(personPk, result.getPk(), PRBConstants.PRB_APPLICATION));
                result.setFirstAffiliateIdentifier();
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  updatePRBApplication
     * Updates the data associated with a Political Rebate application.
     *
     * @param prbAppData The Application Data
     * @param userPk The Primary Key of the user who modified the record
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePRBApplication(PRBApplicationData prbAppData, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_APPLICATION);
            DBUtil.setNullableTimestamp(ps, 1, prbAppData.getAppMailedDt());
            DBUtil.setNullableTimestamp(ps, 2, prbAppData.getAppReturnedDt());
            DBUtil.setNullableInt(ps, 3, prbAppData.getPrbCommentAnalCd());
            DBUtil.setNullableInt(ps, 4, prbAppData.getPrbEvaluationCd());
            DBUtil.setNullableVarchar(ps, 5, prbAppData.getCommentTxt());
            ps.setInt(6, userPk.intValue());
            ps.setInt(7, prbAppData.getPrbAppPK().intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  updatePRBApplicationStatus
     * Updates the status associated with a Political Rebate application.
     *
     * @param prbAppPk Primary Key of Political Rebate Application
     * @param userPk The Primary Key of the user who modified the record
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePRBApplicationStatus(Integer prbAppPk, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_APPLICATION_STATUS);
            DBUtil.setNullableInt(ps, 1, RebateStatus.A.intValue());
            DBUtil.setBooleanAsShort(ps, 2, true);
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, prbAppPk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    
    /**
     * @J2EE_METHOD  --  updatePRBApplicationStatus
     * Updates the status associated with a Political Rebate application.
     *
     * @param prbAppPk Primary Key of Political Rebate Application
     * @param userPk The Primary Key of the user who modified the record
     * @param status The Rebate status
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePRBApplicationStatus(Integer prbAppPk, Integer userPk, int status) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_APPLICATION_STATUS);
            DBUtil.setNullableInt(ps, 1, status);
            if (status == RebateStatus.A.intValue())
                DBUtil.setBooleanAsShort(ps, 2, true);
            else
                DBUtil.setBooleanAsShort(ps, 2, false);
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, prbAppPk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    ////////////////////////////////////////////////////
    // POLITICAL REBATE ANNUAL REBATE INFORMATION
    ////////////////////////////////////////////////////
    
    /** Selects an individual's political rebate records summary by year */
    private static final String SQL_SELECT_PERSON_PRB =
    "SELECT rbt_year " +
    "FROM  PRB_Rbt_Year_Info " +
    "WHERE person_pk=? " +
    "ORDER BY rbt_year DESC";
    
    /** Selects an individual's political rebate check info */
    private static final String SQL_SELECT_RBT_CHECK_NUMBER =
    "SELECT isnull(rbt_check_nbr_1, 0), isnull(rbt_check_nbr_2, 0) " +
    "FROM  PRB_Rebate_Check_Info " +
    "WHERE person_pk=? and rbt_year=?";
    
    /** Selects an individual's political rebate request status */
    private static final String SQL_SELECT_RBT_REQUEST_STATUS =
    "SELECT isnull(prb_app_pk, 0), isnull(com_cd_desc, ' ') " +
    "FROM PRB_Requests r " +
    "LEFT OUTER JOIN Common_Codes c on c.com_cd_pk = r.rqst_status " +
    "WHERE r.person_pk=? and r.rqst_rebate_year=?";
    
    /** Selects an individual's political rebate application status */
    private static final String SQL_SELECT_RBT_APPLICATION_STATUS =
    "SELECT com_cd_desc " +
    "FROM PRB_Apps a " +
    "JOIN Common_Codes c on c.com_cd_pk = a.app_status ";
    
    /** Selects an individual's political rebate roster affiliate status */
    private static final String SQL_SELECT_RBT_ROSTER_STATUS =
    "SELECT com_cd_desc " +
    "FROM PRB_Roster_Persons r " +
    "JOIN PRB_Rbt_Year_Info y on y.person_pk = r.person_pk and y.rbt_year = r.rbt_year " +
    "LEFT OUTER JOIN Common_Codes c on c.com_cd_pk = r.roster_aff_status " +
    "WHERE y.person_pk=? and y.rbt_year=?";
    
    /** Selects an individual's political annual rebate information - acceptance code status */
    private static final String SQL_SELECT_RBT_ACCEPTANCE_CD_STATUS =
    "SELECT com_cd_desc " +
    "FROM PRB_Roster_Persons r " +
    "JOIN Common_Codes c on c.com_cd_pk = r.roster_acceptance_cd " +
    "WHERE person_pk=? and rbt_year=?";
    
    /** Selects an individual's political rebate dues paid information for annual rebate */
    private static final String SQL_SELECT_PRB_ANNUAL_REBATE_AFFILIATES =
    "SELECT a.aff_pk, aff_type, aff_code, aff_localSubChapter, " +
    "       aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, " +
    "       isnull(roster_duration_in_aff, 0), isnull(roster_filed_with, 0), isnull(roster_acceptance_cd, 0), " +
    "       isnull(rebate_year_mbr_type, 0), isnull(rebate_year_mbr_status, 0), isnull(rebate_year_mbr_dues_rate, 0), " +
    "       rbt_check_nbr, r.lst_mod_dt, r.lst_mod_user_pk " +
    "FROM PRB_Roster_Persons r " +
    "JOIN Aff_Organizations a on a.aff_pk = r.aff_pk " +
    "JOIN PRB_Rbt_Year_Info y on y.person_pk = r.person_pk and y.rbt_year = r.rbt_year " +
    "WHERE y.person_pk=? and y.rbt_year=? ";
    
    
    /** Selects an individual's political rebate check information based on the rebate year */
    private static final String SQL_SELECT_PRB_CHECK_INFO =
    "SELECT rbt_check_nbr_1, rbt_check_amt_1, rbt_check_1_run_dt, " +
    "       rbt_check_1_returned_fg, rbt_check_nbr_2, rbt_check_amt_2, " +
    "       rbt_check_2_run_dt, judicial_pnl_case_num, suppl_check_nbr, " +
    "       suppl_check_amt, suppl_check_run_dt, comment_txt " +
    "FROM PRB_Rbt_Year_Info y " +
    "JOIN PRB_Rebate_Check_Info c on c.person_pk = y.person_pk and c.rbt_year = y.rbt_year " +
    "WHERE y.person_pk=? and y.rbt_year=? ";
    
    /** Selects an affiliate identifier */
    private static final String SQL_SELECT_AFFILIATE_IDENTIFIER =
    "SELECT aff_type, aff_code, aff_localSubChapter, aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap " +
    "FROM Aff_Organizations " +
    "WHERE aff_pk=? ";
    
    /** Removes an affiliate from the political annual rebate */
    private static final String SQL_DELETE_AFFILIATE_ANNUAL_REBATE =
    "DELETE FROM PRB_Roster_Persons " +
    "WHERE person_pk=? and rbt_year=? and aff_pk=? ";
    
    /** Inserts an individual's political rebate annual rebate (dues paid) information */
    private static final String SQL_INSERT_PRB_ROSTER_PERSONS =
    "INSERT INTO PRB_Roster_Persons " +
    " (aff_pk, person_pk, roster_duration_in_aff, roster_acceptance_cd, roster_filed_with, " +
    "  rebate_year_mbr_type, rebate_year_mbr_status, rebate_year_mbr_dues_rate, rbt_year, rebate_year_amt, " +
    "  roster_aff_status, rbt_check_nbr, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk, file_generated_dt) " +
    "VALUES (?,?,?,?,?,?,?,?,?,null,?,null,getdate(),?,getdate(),?,null) ";
    
    /** Updates an individual's political rebate annual rebate (dues paid) information */
    private static final String SQL_UPDATE_PRB_ROSTER_PERSONS =
    "UPDATE PRB_Roster_Persons " +
    "SET roster_duration_in_aff=?, roster_acceptance_cd=?, roster_filed_with=?, " +
    "    rebate_year_mbr_type=?, rebate_year_mbr_status=?, rebate_year_mbr_dues_rate=?, " +
    "    lst_mod_dt=GetDate(), lst_mod_user_pk=? " +
    "WHERE aff_pk=? and person_pk=? and rbt_year=?";
    
    /** Creates an individual's political rebate check information based on the rebate year */
    private static final String SQL_CREATE_PRB_CHECK_INFO =
    "INSERT INTO PRB_Rebate_Check_Info " +
    " (person_pk, rbt_year, lst_mod_dt, lst_mod_user_pk, created_dt, created_user_pk, " +
    "  rbt_check_nbr_1, rbt_check_amt_1, rbt_check_1_run_dt, rbt_check_1_returned_fg, " +
    "  rbt_check_nbr_2, rbt_check_amt_2, rbt_check_2_run_dt, rbt_check_2_returned_fg, " +
    "  judicial_pnl_case_num, suppl_check_nbr, suppl_check_amt, suppl_check_run_dt, suppl_check_returned_fg) " +
    "VALUES (?, ?, getDate(), ?, getDate(), ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?, 0) ";
    
    /** Updates an individual's political rebate check information based on the rebate year */
    private static final String SQL_UPDATE_PRB_CHECK_INFO =
    "UPDATE PRB_Rebate_Check_Info " +
    "SET rbt_check_nbr_1=?, rbt_check_amt_1=?, rbt_check_1_run_dt=?, rbt_check_1_returned_fg=?, " +
    "    rbt_check_nbr_2=?, rbt_check_amt_2=?, rbt_check_2_run_dt=?, judicial_pnl_case_num=?, " +
    "    suppl_check_nbr=?, suppl_check_amt=?, suppl_check_run_dt=?, " +
    "    lst_mod_dt=getDate(), lst_mod_user_pk=? " +
    "WHERE person_pk=? and rbt_year=?";
    
    /** Updates the political rebate comment */
    private static final String SQL_UPDATE_PRB_YEAR_INFO_COMMENT =
    "UPDATE PRB_Rbt_Year_Info " +
    "SET comment_txt=? " +
    "WHERE person_pk=? and rbt_year=?";
    
    /** Updates the political rebate year info status */
    private static final String SQL_UPDATE_PRB_YEAR_INFO_STATUS =
    "UPDATE PRB_Rbt_Year_Info " +
    "SET person_rbt_year_status=? " +
    "WHERE person_pk=? and rbt_year=?";
    
    
    /**
     * @J2EE_METHOD  --  getPersonPRBSummaryByYear
     * This method retrieves a political rebate list of the all the years in which
     * an individual has applied for a Rebate
     *
     * @return A list of PRBSummaryByYear data object
     * @param Integer The primary key of an individual who has applied for a rebate
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public java.util.List getPersonPRBSummaryByYear(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new LinkedList();
        PRBSummaryByYear result = null;
        Integer rbtYear = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_PRB);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                result = new PRBSummaryByYear();
                rbtYear = new Integer(rs.getInt(1));
                result.setPrbYear(rbtYear);
                if (isPriorRebateYear(rbtYear)) {
                    result.setPrbStatus(getAnnualRebateStatus(personPk, rbtYear));
                } else {
                    result.setPrbStatus(getRebateStatus(personPk, rbtYear));
                }
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  getRebateStatus
     * This method retrieves a political rebate status for
     * an individual who has applied for a Rebate based on the rebate year.
     * Valid rebate status for the current rebate year is: Check Issued, Denied, In Progress.
     * Valid rebate status for the prior rebate year is: Check Issued, Denied.
     *
     * @return A String of rebate status.
     * @param Integer The primary key of an individual
     * @param Integer The primary key of the rebate year
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getRebateStatus(Integer personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean denied = false;
        boolean approved = false;
        String rbtStatus;
        Map prbAppPks = new HashMap();
        
        try {
            con = DBUtil.getConnection();
            
            // Level 1:
            // If all rebate requests are denied - return status "Denied"
            // If no rebate application record created - return status "In Progress"
            // If rebate application record exists - check Rebate Application
            ps = con.prepareStatement(SQL_SELECT_RBT_REQUEST_STATUS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                prbAppPks.put(toString().valueOf(rs.getInt(1)), null);
                denied = (rs.getString(2).equalsIgnoreCase(PRBConstants.STATUS_DENIED)) ? true : false;
            }
            
            if (denied || prbAppPks.isEmpty()) {
                DBUtil.cleanup(con, ps, rs);
                return (denied) ? PRBConstants.STATUS_DENIED : PRBConstants.STATUS_IN_PROGRESS;
            }
            
            // Level 2:
            // If all rebate applications are denied - return status "Denied"
            // If No Approved record exists - return status "In Progress"
            // If Approved record exists - check Annual Rebate Information
            String appPk = "";
            Iterator it = prbAppPks.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                appPk = (appPk == "") ? "(" +key : appPk.concat(", " +key);
            }
            if (appPk.length() > 0) {
                String query = SQL_SELECT_RBT_APPLICATION_STATUS + "WHERE prb_app_pk IN " + appPk + ")";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    denied = rs.getString(1).equalsIgnoreCase(PRBConstants.STATUS_DENIED) ? true : false;
                    approved = rs.getString(1).equalsIgnoreCase(PRBConstants.STATUS_APPROVED) ? true : false;
                }
                if (denied || !approved) {
                    DBUtil.cleanup(con, ps, rs);
                    return (denied) ? PRBConstants.STATUS_DENIED : PRBConstants.STATUS_IN_PROGRESS;
                }
            }
            
            // Level 3:
            // Get Annual Rebate Information status
            rbtStatus = getAnnualRebateStatus(personPk, rbtYear);
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return rbtStatus;
    }
    
    
    /**
     * @J2EE_METHOD  --  getRebateRequestStatusCode
     * This method retrieves a political rebate request status for
     * an individual who has applied for a Rebate based on the rebate year.
     *
     * @return int The rebate status code.
     * @param int The primary key of an individual
     * @param Integer The primary key of the rebate year
     */
    public int getRebateRequestStatusCode(int personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean denied = false;
        
        try {
            con = DBUtil.getConnection();
            
            // If all rebate requests are denied - return status "Denied"
            ps = con.prepareStatement(SQL_SELECT_RBT_REQUEST_STATUS);
            ps.setInt(1, personPk);
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                denied = (rs.getString(2).equalsIgnoreCase(PRBConstants.STATUS_DENIED)) ? true : false;
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return (denied) ? RebateStatus.D.intValue() : RebateStatus.IP.intValue();
    }
    
    /**
     * @J2EE_METHOD  --  getAnnualRebateStatus
     * This method calculates the Annual Rebate status based on the Roster Status, the Acceptance Code
     * of each Affiliate, and the Check Number if at least one of the Affiliates approved for
     * an individual who has applied for a Rebate based on the rebate year.
     * Valid rebate status for the current rebate year is: Check Issued, Denied, In Progress.
     * Valid rebate status for the prior rebate year is: Check Issued, Denied.
     *
     * @return A String of rebate status.
     * @param Integer The primary key of an individual
     * @param Integer The primary key of the rebate year
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getAnnualRebateStatus(Integer personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean denied = false;
        
        try {
            con = DBUtil.getConnection();
            
            // If rebate check number is issued - return status "Check Issued"
            ps = con.prepareStatement(SQL_SELECT_RBT_CHECK_NUMBER);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1)>0 || rs.getInt(2)>0) {
                    DBUtil.cleanup(con, ps, rs);
                    return PRBConstants.STATUS_CHECK_ISSUED;
                }
            }
            if (isPriorRebateYear(rbtYear)) {
                DBUtil.cleanup(con, ps, rs);
                return PRBConstants.STATUS_DENIED;
            }
            
            // If there is no annual rebate info - return status "In Progress"
            if (getRosterStatus(personPk, rbtYear) == null) {
                DBUtil.cleanup(con, ps, rs);
                return PRBConstants.STATUS_IN_PROGRESS;
            }
            
            // If all Acceptance Code status is denied - return status "Denied", otherwise return status "In Progress"
            ps = con.prepareStatement(SQL_SELECT_RBT_ACCEPTANCE_CD_STATUS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                denied = (rs.getString(1).equalsIgnoreCase(PRBConstants.STATUS_DENIED) ||
                rs.getString(1).equalsIgnoreCase("")) ? true : false;
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return (denied) ? PRBConstants.STATUS_DENIED : PRBConstants.STATUS_IN_PROGRESS;
    }
    
    /**
     * @J2EE_METHOD  --  getRosterStatus
     * This method gets the Roster Status of all Affiliate for an individual based on the rebate year.
     *
     * @return A String of roster status.
     * @param Integer The primary key of an individual
     * @param Integer The primary key of the rebate year
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getRosterStatus(Integer personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = "";
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_RBT_ROSTER_STATUS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                status = (rs.getString(1) == null) ? "" : rs.getString(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return status;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBAnnualRebateAffiliates
     * Retrieves the Affiliate Dues Paid List of the political rebate annual rebate information
     * for a person based on the rebate year.
     *
     * @param personPk Person Primary Key
     * @param rbtYear Rebate Year
     * @return the List of PRBAffiliateData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPRBAnnualRebateAffiliates(Integer personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PRBAffiliateData prbData = null;
        AffiliateIdentifier affId = null;
        RecordData recData = null;
        List list = new ArrayList(2);
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_ANNUAL_REBATE_AFFILIATES);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                prbData = new PRBAffiliateData();
                recData = new RecordData();
                affId = new AffiliateIdentifier();
                prbData.setAffPk(new Integer(rs.getInt(1)));
                affId.setType(new Character(rs.getString(2).charAt(0)));
                affId.setCode(new Character(rs.getString(3).charAt(0)));
                affId.setLocal(rs.getString(4));
                affId.setState(rs.getString(5));
                affId.setSubUnit(rs.getString(6));
                affId.setCouncil(rs.getString(7));
                prbData.setDurationPk(new Integer(rs.getInt(8)));
                prbData.setFiledWithPk(new Integer(rs.getInt(9)));
                prbData.setAcceptanceCodePk(new Integer(rs.getInt(10)));
                prbData.setRbtMbrTypePk(new Integer(rs.getInt(11)));
                prbData.setRbtMbrStatusPk(new Integer(rs.getInt(12)));
                prbData.setDuesTypePk(new Integer(rs.getInt(13)));
                prbData.setRbtCheckNumber(setIntToInteger(rs.getInt(14)));
                recData.setModifiedDate(rs.getTimestamp(15));
                recData.setModifiedBy(new Integer(rs.getInt(16)));
                prbData.setTheAffiliateIdentifier(affId);
                prbData.setTheRecordData(recData);
                list.add(prbData);
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  updatePRBRosterPerson
     * Updates data associated with Roster Person
     *
     * @param the PRBAffiliateData object
     * @param rbtYear the rebate year
     * @return TRUE if update/insert successfully, FALSE otherwise
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean updatePRBRosterPerson(PRBAffiliateData prbAffData, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean rs;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_ROSTER_PERSONS);
            DBUtil.setNullableInt(ps, 1, prbAffData.getDurationPk());
            DBUtil.setNullableInt(ps, 2, prbAffData.getAcceptanceCodePk());
            DBUtil.setNullableInt(ps, 3, prbAffData.getFiledWithPk());
            DBUtil.setNullableInt(ps, 4, prbAffData.getRbtMbrTypePk());
            DBUtil.setNullableInt(ps, 5, prbAffData.getRbtMbrStatusPk());
            DBUtil.setNullableInt(ps, 6, prbAffData.getDuesTypePk());
            ps.setInt(7, prbAffData.getTheRecordData().getModifiedBy().intValue());
            ps.setInt(8, prbAffData.getAffPk().intValue());
            ps.setInt(9, prbAffData.getTheRecordData().getPk().intValue());
            ps.setInt(10, rbtYear.intValue());
            rs = (ps.executeUpdate() != 0);
            if (!rs) {
                rs = createPRBRosterPerson(prbAffData, rbtYear);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return rs;
    }
    
    /**
     * @J2EE_METHOD  --  createPRBRosterPerson
     * Creates data associated with Roster Person
     *
     * @param the PRBAffiliateData object
     * @param rbtYear the rebate year
     * @return TRUE if insert successfully, FALSE otherwise
     */
    public boolean createPRBRosterPerson(PRBAffiliateData prbAffData, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean rs;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PRB_ROSTER_PERSONS);
            ps.setInt(1, prbAffData.getAffPk().intValue());
            ps.setInt(2, prbAffData.getTheRecordData().getPk().intValue());
            DBUtil.setNullableInt(ps, 3, prbAffData.getDurationPk());
            DBUtil.setNullableInt(ps, 4, prbAffData.getAcceptanceCodePk());
            DBUtil.setNullableInt(ps, 5, prbAffData.getFiledWithPk());
            DBUtil.setNullableInt(ps, 6, prbAffData.getRbtMbrTypePk());
            DBUtil.setNullableInt(ps, 7, prbAffData.getRbtMbrStatusPk());
            DBUtil.setNullableInt(ps, 8, prbAffData.getDuesTypePk());
            ps.setInt(9, rbtYear.intValue());
            ps.setInt(10, PRBRosterStatus.P.intValue());
            ps.setInt(11, prbAffData.getTheRecordData().getCreatedBy().intValue());
            ps.setInt(12, prbAffData.getTheRecordData().getModifiedBy().intValue());
            rs = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return rs;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBCheckInfo
     * Retrieves political rebate check information for a person based on the rebate year.
     *
     * @param personPk Person Primary Key
     * @param rbtYear The Rebate Year
     * @return the PRBCheckInfo object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PRBCheckInfo getPRBCheckInfo(Integer personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PRBCheckInfo prbCheckInfo = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRB_CHECK_INFO);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                prbCheckInfo = new PRBCheckInfo();
                prbCheckInfo.setPersonPK(personPk);
                prbCheckInfo.setRebateYear(rbtYear);
                prbCheckInfo.setCheckNumber(setIntToInteger(rs.getInt(1)));
                if (rs.getDouble(2) > 0)
                    prbCheckInfo.setAmount(new Double(rs.getDouble(2)));
                prbCheckInfo.setDate(rs.getTimestamp(3));
                prbCheckInfo.setReturnedFlag(DBUtil.getBooleanFromShort(rs.getShort(4)));
                prbCheckInfo.setCheckNumber2(setIntToInteger(rs.getInt(5)));
                if (rs.getDouble(6) > 0)
                    prbCheckInfo.setAmount2(new Double(rs.getDouble(6)));
                prbCheckInfo.setDate2(rs.getTimestamp(7));
                prbCheckInfo.setCaseNumber(setIntToInteger(rs.getInt(8)));
                prbCheckInfo.setSupplCheckNumber(setIntToInteger(rs.getInt(9)));
                if (rs.getDouble(10) > 0)
                    prbCheckInfo.setSupplAmount(new Double(rs.getDouble(10)));
                prbCheckInfo.setSupplDate(rs.getTimestamp(11));
                prbCheckInfo.setComment(rs.getString(12));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return prbCheckInfo;
    }
    
    /**
     * @J2EE_METHOD  --  updatePRBCheckInfo
     * Update political rebate check information for a person based on the rebate year.
     *
     * @param the PRBCheckInfo object
     * @param the primary key of the current login user
     * @return TRUE if check info update successfully, FALSE otherwise
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean updatePRBCheckInfo(PRBCheckInfo prbCheckInfo, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean rs;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_CHECK_INFO);
            DBUtil.setNullableInt(ps, 1, prbCheckInfo.getCheckNumber());
            DBUtil.setNullableDouble(ps, 2, prbCheckInfo.getAmount());
            DBUtil.setNullableTimestamp(ps, 3, prbCheckInfo.getDate());
            DBUtil.setNullableBooleanAsShort(ps, 4, prbCheckInfo.getReturnedFlag());
            DBUtil.setNullableInt(ps, 5, prbCheckInfo.getCheckNumber2());
            DBUtil.setNullableDouble(ps, 6, prbCheckInfo.getAmount2());
            DBUtil.setNullableTimestamp(ps, 7, prbCheckInfo.getDate2());
            DBUtil.setNullableInt(ps, 8, prbCheckInfo.getCaseNumber());
            DBUtil.setNullableInt(ps, 9, prbCheckInfo.getSupplCheckNumber());
            DBUtil.setNullableDouble(ps, 10, prbCheckInfo.getSupplAmount());
            DBUtil.setNullableTimestamp(ps, 11, prbCheckInfo.getSupplDate());
            ps.setInt(12, userPk.intValue());
            ps.setInt(13, prbCheckInfo.getPersonPK().intValue());
            ps.setInt(14, prbCheckInfo.getRebateYear().intValue());
            rs = (ps.executeUpdate() != 0);
            if (!rs) {
                rs = createPRBCheckInfo(prbCheckInfo, userPk);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        if (prbCheckInfo.getComment() != null) {
            updatePRBYearInfoComment(prbCheckInfo.getComment(), prbCheckInfo.getPersonPK(), prbCheckInfo.getRebateYear());
        }
        return rs;
    }
    
    /**
     * @J2EE_METHOD  --  createPRBCheckInfo
     * Creates political rebate check information for a person based on the rebate year.
     *
     * @param the PRBCheckInfo object
     * @param the Primary Key of the current login user
     * @return TRUE if check info created successfully, FALSE otherwise
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean createPRBCheckInfo(PRBCheckInfo prbCheckInfo, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean rs;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_CREATE_PRB_CHECK_INFO);
            ps.setInt(1, prbCheckInfo.getPersonPK().intValue());
            ps.setInt(2, prbCheckInfo.getRebateYear().intValue());
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, userPk.intValue());
            DBUtil.setNullableInt(ps, 5, prbCheckInfo.getCheckNumber());
            DBUtil.setNullableDouble(ps, 6, prbCheckInfo.getAmount());
            DBUtil.setNullableTimestamp(ps, 7, prbCheckInfo.getDate());
            DBUtil.setNullableBooleanAsShort(ps, 8, prbCheckInfo.getReturnedFlag());
            DBUtil.setNullableInt(ps, 9, prbCheckInfo.getCheckNumber2());
            DBUtil.setNullableDouble(ps, 10, prbCheckInfo.getAmount2());
            DBUtil.setNullableTimestamp(ps, 11, prbCheckInfo.getDate2());
            DBUtil.setNullableInt(ps, 12, prbCheckInfo.getCaseNumber());
            DBUtil.setNullableInt(ps, 13, prbCheckInfo.getSupplCheckNumber());
            DBUtil.setNullableDouble(ps, 14, prbCheckInfo.getSupplAmount());
            DBUtil.setNullableTimestamp(ps, 15, prbCheckInfo.getSupplDate());
            rs = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return rs;
    }
    
    
    /**
     * @J2EE_METHOD  --  updatePRBYearInfoComment
     * Update political rebate comment.
     *
     * @param String comment
     * @param Integer person primary key
     * @param Integer rbtYear rebate year
     */
    public void updatePRBYearInfoComment(String comment, Integer personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_YEAR_INFO_COMMENT);
            ps.setString(1, comment);
            ps.setInt(2, personPk.intValue());
            ps.setInt(3, rbtYear.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  updatePRBYearInfoStatus
     * Update political rebate status in PRB_Rbt_Year_Info.
     *
     * @param int rbtStatus
     * @param int person primary key
     * @param Integer rbtYear rebate year
     */
    public void updatePRBYearInfoStatus(int rbtStatus, int personPk, Integer rbtYear) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_YEAR_INFO_STATUS);
            ps.setInt(1, rbtStatus);
            ps.setInt(2, personPk);
            ps.setInt(3, rbtYear.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    
    /**
     * @J2EE_METHOD  --  getAffiliateIdentifier
     * Retrieves an Affiliate Identifier based on affPk.
     *
     * @param affPk Affiliate Primary Key
     * @return the AffiliateIdentifier object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public AffiliateIdentifier getAffiliateIdentifier(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AffiliateIdentifier affId = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_IDENTIFIER);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                affId = new AffiliateIdentifier();
                affId.setType(new Character(rs.getString(1).charAt(0)));
                affId.setCode(new Character(rs.getString(2).charAt(0)));
                affId.setLocal(rs.getString(3));
                affId.setState(rs.getString(4));
                affId.setSubUnit(rs.getString(5));
                affId.setCouncil(rs.getString(6));
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return affId;
    }
    
    
    /**
     * @J2EE_METHOD  --  removeAffiliateFromAnnualRebate
     * Removes the association of an afffiliate with a person's annual rebate information.
     *
     * @param personPK Person Primary Key
     * @param rbtYear Rebate Year
     * @param affPK Affiliate Primary Key
     *
     * @return TRUE if removal completes, and FALSE if an error occurs.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean removeAffiliateFromAnnualRebate(Integer personPk, Integer rbtYear, Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result;
        
        try {
            con = DBUtil.getConnection();
            
            // Remove the Affiliate from the Annual Rebate
            ps = con.prepareStatement(SQL_DELETE_AFFILIATE_ANNUAL_REBATE);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, rbtYear.intValue());
            ps.setInt(3, affPk.intValue());
            result = (ps.executeUpdate() != 0);
        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return result;
    }
    
    
    //////////////////////////////////////////////////////////////
    // POLITICAL REBATE APPLICATION FILE REPORT,
    // PRELIMINARY ROSTER and REBATE UPDATE FILE REPORT,
    // REBATE CHECK FILE REPORT
    //////////////////////////////////////////////////////////////
    /** Selects all political rebate requests for everyone in a rebate year who
     * is eligible for an Application */
    private static final String SQL_SELECT_APPLICATION_ELIGIBLE_REQUESTS =
    "SELECT rqst_pk, person_pk " +
    "FROM   PRB_Requests " +
    "WHERE  rqst_rebate_year=? and " +
    "       (rqst_status is null or rqst_status != ?) and " +
    "       prb_app_pk is null " +
    "ORDER BY person_pk";
    
    /** Selects all members in a rebate year who is eligible for a rebate check */
    private static final String SQL_SELECT_MEMBERS_WITH_ELIGIBLE_CHECK =
    "SELECT distinct aff_pk, r.person_pk " +
    "FROM  PRB_Roster_Persons r " +
    "JOIN  PRB_Rbt_Year_Info y on y.rbt_year = r.rbt_year " +
    "WHERE r.rbt_year=? and " +
    "      r.roster_acceptance_cd not in (0, ?) and " +
    "      r.rbt_check_nbr is null " +
    "ORDER BY r.person_pk ";
    
    private static final String SQL_SELECT_MEMBER_CHECK_AMOUNT =
    "SELECT com_cd_cd, isnull(rebate_year_mbr_dues_rate, 0) " +
    "FROM PRB_Roster_Persons r " +
    "JOIN Common_Codes cc ON cc.com_cd_pk = r.roster_duration_in_aff " +
    "WHERE person_pk=? and rbt_year=? ";
    
    private static final String SQL_SELECT_MEMBER_AFFILIATE_CHECK_AMOUNT =
    "SELECT com_cd_cd, isnull(rebate_year_mbr_dues_rate, 0) " +
    "FROM PRB_Roster_Persons r " +
    "JOIN Common_Codes cc ON cc.com_cd_pk = r.roster_duration_in_aff " +
    "WHERE person_pk=? and aff_pk=? and rbt_year=? ";
    
    private static final String SQL_SELECT_PRELIMINARY_ROSTER_AFFILIATES =
    "SELECT distinct aff.aff_pk, aff_code, aff_type, aff_localSubChapter, " +
    "       aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap " +
    "FROM   PRB_Roster_Persons roster  " +
    "JOIN   Person person ON person.person_pk = roster.person_pk  " +
    "JOIN   PRB_Rbt_Year_Info rebate ON roster.person_pk = rebate.person_pk " +
    "JOIN   Aff_Organizations aff ON aff.aff_pk = roster.aff_pk " +
    "LEFT OUTER JOIN Person_Address address ON address.person_pk = person.person_pk and " +
    "       address_pk IN ( SELECT address_pk  " +
    "                       FROM person_SMA  " +
    "                       WHERE person_pk = person.person_pk AND current_fg = 1)  " +
    "WHERE  roster_aff_status=? and  " +
    "       rebate.rbt_year=? and " +    
    "       file_generated_dt is null "; // HLM: Fix defect #110
    
    /** Inserts the prb application record */
    private static final String SQL_INSERT_PRB_APPLICATION =
    "INSERT INTO PRB_Apps " +
    "	 (app_mailed_dt, app_returned_dt, comment_txt, prb_comment_anal_cd, " +
    "	  prb_evaluation_cd, app_status, lst_mod_dt, lst_mod_user_pk, created_dt, " +
    "	  created_user_pk, aff_roster_generated_fg) " +
    "VALUES (?, null, null, null, ?, ?, getDate(), ?, getDate(), ?, 0)";
    
    /** Updates the appPk of the political request record */
    private static final String SQL_UPDATE_PRB_REQUEST_APP_PK =
    "UPDATE PRB_Requests " +
    "SET    prb_app_pk=?, lst_mod_dt=getDate(), lst_mod_user_pk=? " +
    "WHERE  rqst_pk=? ";
    
    /** Updates the final roster status, check number and amount for the rebate year */
    private static final String SQL_UPDATE_FINAL_ROSTER =
    "UPDATE PRB_Roster_Persons " +
    "SET    rbt_check_nbr=?, rebate_year_amt=?, roster_aff_status=?, " +
    "       lst_mod_dt=getDate(), lst_mod_user_pk=? " +
    "WHERE  rbt_year=? and person_pk=?";
    
    /**
     * @J2EE_METHOD  --  getPreliminaryRosterAffiliates
     * This method supports the Preliminary Roster/Rebate Update File report.
     * Retrieves information on Political Rebate requests made for the current rebate year
     * that are in the preliminary state.
     *
     * @return the Array List of PreliminaryRosterAffiliates objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ArrayList getPreliminaryRosterAffiliates(int order, String sortBy) {
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList();
        
        try {
            con = DBUtil.getConnection();
            
            // add sort fields
            if (!TextUtil.isEmptyOrSpaces(sortBy)) {
                StringBuffer sb = new StringBuffer(sortBy);
                if (order < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                builder.addOrderBy(sb.toString().trim());
            } else {
                // Add default Sort field
                builder.addOrderBy(" aff_type ASC ");
                builder.addOrderBy(" aff_localSubChapter ASC ");
                builder.addOrderBy(" aff_stateNat_type ASC ");
            }
            
            ps = builder.getPreparedStatement(SQL_SELECT_PRELIMINARY_ROSTER_AFFILIATES, con);
            ps.setInt(1, PRBRosterStatus.P.intValue());
            ps.setInt(2, getCurrentRebateYear());
            rs = ps.executeQuery();
            while (rs.next()) {
                PreliminaryRosterAffiliate result = new PreliminaryRosterAffiliate();
                result.setAffPk(TextUtil.toString(new Integer(rs.getInt(1))));
                result.setCode(new Character(rs.getString(2).toCharArray()[0]));
                result.setType(new Character(rs.getString(3).toCharArray()[0]));
                result.setLocal(rs.getString(4));
                result.setState(rs.getString(5));
                result.setSubUnit(rs.getString(6));
                result.setCouncil(rs.getString(7));
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBApplicationEligible
     * This method supports the Political Rebate Application File report.
     * Retrieves information on Political Rebate requests made for the current rebate year
     * that have not been included in a prior application run and have not
     * been denied
     *
     * @return the List of PRBRequestData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPRBApplicationEligible() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new LinkedList();
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_APPLICATION_ELIGIBLE_REQUESTS);
            ps.setInt(1, getCurrentRebateYear());
            ps.setInt(2, RebateStatus.D.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                PRBRequestData result = new PRBRequestData();
                result.setRqstPk(rs.getInt(1));
                result.setPersonPk(rs.getInt(2));
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  updatePRBRequestAppPk
     * This method supports the Political Rebate Application File report.
     * Updates the AppPk of the Political Rebate Request record.
     *
     * @param rqstPk Primary key of the Request
     * @param appPk Primary key of the Application
     * @param userPk Primary key of the login user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePRBRequestAppPk(int rqstPk, Integer appPk, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PRB_REQUEST_APP_PK);
            ps.setInt(1, appPk.intValue());
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, rqstPk);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  createPRBApplicationRecord
     * This method supports the Political Rebate Application File report.
     * The method creates an Application record with the supplied
     * mailed date.  Also sets the default for the Evaluation Code to "Not Returned"
     * and includes the Dues Paid To and Duration from the Request record.
     *
     * @return Integer the application primary key
     *
     * @param Timestamp The application mailed date
     * @param userPk Primary key of the login user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Integer createPRBApplicationRecord(Timestamp appMailedDate, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        Integer appPk = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PRB_APPLICATION);
            DBUtil.setNullableTimestamp(ps, 1, appMailedDate);
            DBUtil.setNullableInt(ps, 2, RebateAppEvalCode.NR.intValue());
            DBUtil.setNullableInt(ps, 3, RebateStatus.IP.intValue());
            ps.setInt(4, userPk.intValue());
            ps.setInt(5, userPk.intValue());
            appPk = DBUtil.insertAndGetIdentity(con, ps);
        } catch (SQLException e) {
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return appPk;
    }
    
    /**
     * @J2EE_METHOD  --  getPRBCheckEligible
     * Retrieves a list of Members that have been approved for a Rebate and
     * are eligible to receive a Rebate Check.
     *
     * Eligible is defined as the following:
     * at least one affiliate must have an approved of the rebate
     * (PRB_Roster_Persons.roster_acceptance_cd IN( "Council Accepted","Local Accepted")
     * and the approved rebate affiliate(s) cannot have already been part of a previous
     * check run (PRB_Roster_Persons.rbt_check_nbr is not null)
     *
     * @return List of eligible member to receive a Rebate
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPRBCheckEligible() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        PRBAffiliateData prbAffData = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBERS_WITH_ELIGIBLE_CHECK);
            ps.setInt(1, getCurrentRebateYear());
            ps.setInt(2, RebateAcceptanceCode.D.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                prbAffData = new PRBAffiliateData();
                prbAffData.setAffPk(new Integer(rs.getInt(1)));
                prbAffData.setPersonPk(new Integer(rs.getInt(2)));
                result.add(prbAffData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  calculatePRBCheckAmount
     * This method calculates the rebate check amount for each eligible member.
     *
     * @return double the calculated rebate check amount
     * @param personPk Primary key of the Roster Member/Person
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public double calculatePRBCheckAmount(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer duration=null;
        int duesType=0;
        double checkAmount = 0.0;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_CHECK_AMOUNT);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, getCurrentRebateYear());
            rs = ps.executeQuery();
            while (rs.next()) {
                checkAmount += calculatePRBCheckAmount(setIntToInteger(rs.getInt(1)), rs.getInt(2));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return checkAmount;
    }
    
    /**
     * @J2EE_METHOD  --  calculatePRBCheckAmount
     * This method calculates the rebate check amount for each eligible member
     * breaks down by Affiliate.
     *
     * @return double the calculated rebate check amount
     * @param personPk Primary key of the Roster Member/Person
     * @param affPk Primary key of the affiliate associates with Roster person
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public double calculatePRBCheckAmount(Integer personPk, Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int duesType=0;
        Integer duration=null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_AFFILIATE_CHECK_AMOUNT);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            ps.setInt(3, getCurrentRebateYear());
            rs = ps.executeQuery();
            if (rs.next()) {
                duration = setIntToInteger(rs.getInt(1));
                duesType = rs.getInt(2);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }        
        return (calculatePRBCheckAmount(duration, duesType));
    }
    
    /**
     * @J2EE_METHOD  --  calculatePRBCheckAmount
     * This method calculates the rebate check amount.
     *
     * @return double the calculated rebate check amount
     * @param duration 
     * @param duesType
     */
    public double calculatePRBCheckAmount(Integer duration, int duesType) {
        double checkAmount = 0.0;        
        if (duration!=null && duesType>0) {
            PRB12MonthRebateAmount amount = getPRB12MonthAmount(new Integer(getCurrentRebateYear()));
            if (amount != null) {
                switch (duesType) {
                    case 42001:
                        checkAmount = (duration.doubleValue() * amount.getPrbFullTime().doubleValue());
                        break;
                    case 42002:
                        checkAmount = (duration.doubleValue() * amount.getPrbPartTime().doubleValue());
                        break;
                    case 42003:
                        checkAmount = (duration.doubleValue() * amount.getPrbLowerPartTime().doubleValue());
                        break;
                    case 42004:
                        checkAmount = (duration.doubleValue() * amount.getPrbRetiree().doubleValue());
                        break;
                }
            }
        }
        return checkAmount;
    }
    
    /**
     * @J2EE_METHOD  --  updateFinalRoster
     * This method updates the Members to the Final Roster
     * who were part of the Check File to receive a rebate check.
     *
     * @param personPk Primary key of the Member/Person
     * @param rbtCheckNumber The check number
     * @param rbtCheckAmount The check amount
     * @param userPk Primary key of the login user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updateFinalRoster(int personPk, int rbtCheckNumber, BigDecimal rbtCheckAmount, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_FINAL_ROSTER);
            ps.setInt(1, rbtCheckNumber);
            ps.setBigDecimal(2, rbtCheckAmount);
            ps.setInt(3, PRBRosterStatus.F.intValue());
            ps.setInt(4, userPk.intValue());
            ps.setInt(5, getCurrentRebateYear());
            ps.setInt(6, personPk);
            result = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        /*
         * Set the rebate year info status to Check Issued?
         * This field is a calculated field, therefore
         * might not need to be updated
        if (result) {
            updatePRBYearInfoStatus("Check Issued", personPk, new Integer(getCurrentRebateYear()));
        }
         */
    }
    
    //////////////////////////////////////////////////
    // MAINTAIN 12-MONTH REBATE AMOUNT
    /////////////////////////////////////////////////
    
    /** Selects all 12 Month Rebate records */
    private static final String SQL_SELECT_12MONTH_REBATES =
    "SELECT rbt_year, rbt_pct, rbt_full_time_amt, rbt_part_time_amt, rbt_lower_part_time_amt, rbt_retiree_amt " +
    "FROM PRB_12_Month_Rebate " +
    "ORDER BY rbt_year DESC";
    
    /** Selects 12 Month Rebate record */
    private static final String SQL_SELECT_12MONTH_REBATE =
    "SELECT rbt_pct, rbt_full_time_amt, rbt_part_time_amt, rbt_lower_part_time_amt, rbt_retiree_amt " +
    "FROM PRB_12_Month_Rebate " +
    "WHERE rbt_year=?";
    
    /** Inserts 12 Month Rebate record */
    private static final String SQL_INSERT_12MONTH_REBATE =
    "INSERT into PRB_12_Month_Rebate " +
    " (rbt_year, rbt_pct, rbt_full_time_amt, rbt_part_time_amt, " +
    "  rbt_lower_part_time_amt, rbt_retiree_amt, created_dt, " +
    "  created_user_pk, lst_mod_dt, lst_mod_user_pk) " +
    "VALUES (?,?,?,?,?,?,?,?,?,?) ";
    
    /** Inserts 12 Month Rebate record */
    private static final String SQL_UPDATE_12MONTH_REBATE =
    "UPDATE PRB_12_Month_Rebate " +
    "SET rbt_pct=?, rbt_full_time_amt=?, rbt_part_time_amt=?, rbt_lower_part_time_amt=?, rbt_retiree_amt=?, lst_mod_user_pk=?, lst_mod_dt=? " +
    "WHERE rbt_year=?";
    
    /**
     * @J2EE_METHOD  --  addPRBYear
     * This method adds another Rebate Year to the system for capturing the 12-Month Rebate
     *  Amount.
     *
     * INSERT INTO PRB_12_Month_Rebate
     * VALUES (prbYear, prbPercentage, prbFullTime, prbPartTime, prbLowerPartTime, prbRetiree,
     *  createdBy, createdDate, modifiedBy, modifiedDate)
     *
     * @param PRB12MonthAmount The dataobject containing updated values to be stored in the
     *  database
     * @param Integer The primary key of the user who is currently logged in to the system
     * @return boolean Returns true if added successfully, false otherwise
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean addPRBYear(PRB12MonthRebateAmount prb12MonthRebateAmount, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        int result = 0;
        
        //insert political 12 month rebate amount object into the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_12MONTH_REBATE);
            ps.setInt(1, prb12MonthRebateAmount.getPrbYear().intValue());
            ps.setDouble(2, prb12MonthRebateAmount.getPrbPercentage().doubleValue());
            ps.setDouble(3, prb12MonthRebateAmount.getPrbFullTime().doubleValue());
            ps.setDouble(4, prb12MonthRebateAmount.getPrbPartTime().doubleValue());
            ps.setDouble(5, prb12MonthRebateAmount.getPrbLowerPartTime().doubleValue());
            ps.setDouble(6, prb12MonthRebateAmount.getPrbRetiree().doubleValue());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setInt(8, userPK.intValue());
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            ps.setInt(10, userPK.intValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return (result>0) ? true : false;
    }
    
    /**
     * @J2EE_METHOD  --  getPRB12MonthAmount
     * This method retrieves a 12 Month Amount from the database that matches the rebate
     *  year input.
     *
     * @param prbYear Rebate year to retrieve a specific set of 12 month amounts
     * @return PRB12MonthRebateAmount A single instance of the dataobject that hold amounts
     *  for one year
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PRB12MonthRebateAmount getPRB12MonthAmount(Integer prbYear) {
        PRB12MonthRebateAmount result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_12MONTH_REBATE);
            ps.setInt(1, prbYear.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = new PRB12MonthRebateAmount();
                result.setPrbYear(prbYear);
                result.setPrbPercentage(new Double(rs.getDouble(1)));
                result.setPrbFullTime(new Double(rs.getDouble(2)));
                result.setPrbPartTime(new Double(rs.getDouble(3)));
                result.setPrbLowerPartTime(new Double(rs.getDouble(4)));
                result.setPrbRetiree(new Double(rs.getDouble(5)));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * @J2EE_METHOD  --  getPRB12MonthAmount
     * This method retrieves a historical list of the 12 Month Amounts from the database
     *  for each rebate year.
     *
     * @return A list of PRB12MonthRebateAmount dataobjects
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public java.util.List getPRB12MonthAmount() {
        List list = new LinkedList();
        PRB12MonthRebateAmount result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_12MONTH_REBATES);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = new PRB12MonthRebateAmount();
                result.setPrbYear(new Integer(rs.getInt(1)));
                result.setPrbPercentage(new Double(rs.getDouble(2)));
                result.setPrbFullTime(new Double(rs.getDouble(3)));
                result.setPrbPartTime(new Double(rs.getDouble(4)));
                result.setPrbLowerPartTime(new Double(rs.getDouble(5)));
                result.setPrbRetiree(new Double(rs.getDouble(6)));
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  updatePRB12MonthAmount
     * This method allows one set of Rebate Amount data to be updated.
     *
     * UPDATE PRB_12_Month_Rebate
     * SET rbt_pct =      * @prbPercentage,
     * rbt_full_time_amt =      * @prbFullTime,
     * rbt_part_time_amt =      * @prbPartTime,
     * rbt_lower_part_time_amt =      * @prbLowerPartTime,
     * rbt_retiree_amt =      * @prbRetiree,
     * lst_mod_user_pk =      * @modifiedBy,
     * lst_mod_dt =      * @modifiedDate
     * WHERE rbt_year =      * @prbYear
     *
     * @param PRB12MonthAmount The dataobject containing updated values to be stored in the database
     * @param Integer The primary key of the user who is currently logged in to the system
     * @return void Returns nothing.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePRB12MonthAmount(PRB12MonthRebateAmount prb12MonthRebateAmount, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        // Update political 12 month rebate amount object into the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_12MONTH_REBATE);
            ps.setDouble(1, prb12MonthRebateAmount.getPrbPercentage().doubleValue());
            ps.setDouble(2, prb12MonthRebateAmount.getPrbFullTime().doubleValue());
            ps.setDouble(3, prb12MonthRebateAmount.getPrbPartTime().doubleValue());
            ps.setDouble(4, prb12MonthRebateAmount.getPrbLowerPartTime().doubleValue());
            ps.setDouble(5, prb12MonthRebateAmount.getPrbRetiree().doubleValue());
            ps.setInt(6, userPK.intValue());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            ps.setInt(8, prb12MonthRebateAmount.getPrbYear().intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return;
    }
    
    /**
     * @J2EE_METHOD  --  MaintainPoliticalRebateBean
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public MaintainPoliticalRebateBean() {
    }
}