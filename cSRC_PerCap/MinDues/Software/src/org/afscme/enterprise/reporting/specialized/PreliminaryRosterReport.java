package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.util.Calendar;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.codes.Codes.PRBRosterStatus;
import org.afscme.enterprise.codes.Codes.RebateAppEvalCode;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.rebate.PRBConstants;
import org.apache.log4j.Logger;


/**
 * This is a Preliminary Roster Report.
 * SUMMARY: Creates a list of Requesters that have met the
 * criteria for the Preliminary Roster and will have their information 
 * passed to the affiliate for review and final approval.  
 *
 */
public class PreliminaryRosterReport implements ReportHandler {
    
    private static Logger logger =  Logger.getLogger(PreliminaryRosterReport.class);    
    private String affPks;
    
    
    /** Selects app_mailed_dt from PRB_Apps table  */    
    private static final String SQL_SELECT_APP_CONFIG_VALUE =
    "SELECT variable_value " +
    "FROM   COM_App_Config_Data " +
    "WHERE  variable_name=?";    
    
    /* Selects a list of pks and acceptance codes */
    private static final String SQL_SELECT_EVALUATION_DATA =
    " SELECT prb_app_pk, app_mailed_dt " +
    " FROM PRB_Apps " +
    " WHERE app_returned_dt IS NULL " +
    " AND app_mailed_dt IS NOT NULL " +
    " AND (prb_evaluation_cd IS NULL OR prb_evaluation_cd = " + RebateAppEvalCode.NR + ")";
    
    /* Updates acceptance code */
    private static final String SQL_UPDATE_EVALUATION_CD = 
    " UPDATE PRB_apps SET prb_evaluation_cd = " + RebateAppEvalCode.NT +
    " WHERE prb_app_pk = ?";
    
    /* Selects a list of the individuals that are getting checks */
    private static final String SQL_SELECT_PRELIMINARY_ROSTER = 
    "SELECT distinct(roster.person_pk), aff_stateNat_type, aff_councilRetiree_chap, " +
    "       aff_type, aff_localSubChapter, " +
    "       ssn, last_nm, first_nm, middle_nm, " +
    "       addr1, addr2, city, state, zipcode, zip_plus, " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_type), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_status), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_dues_rate), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = roster_duration_in_aff), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = roster_acceptance_cd) " +
    "FROM   PRB_Roster_Persons roster " +
    "JOIN   Person person ON person.person_pk = roster.person_pk " +
    "JOIN   PRB_Rbt_Year_Info rebate ON roster.person_pk = rebate.person_pk " + 
    "JOIN   Aff_Organizations aff ON aff.aff_pk = roster.aff_pk " +
    "LEFT OUTER JOIN Person_Address address ON address.person_pk = person.person_pk and " +
    "       address_pk IN ( SELECT address_pk " +
    "                       FROM person_SMA " +
    "                       WHERE person_pk = person.person_pk AND current_fg = 1) ";
    
    private static final String SQL_SELECT_PRELIMINARY_ROSTER_WHERE_CLAUSE = 
    "roster_aff_status=? and " +
    "rebate.rbt_year=? and " +
    "file_generated_dt is null ";

    private static final String SQL_SELECT_PRELIMINARY_ROSTER_ORDER_BY = 
    "ORDER BY aff_stateNat_type, aff_localSubChapter, ssn, last_nm";
    
   /**
     * createPRBPreliminaryRosterFile
     * 
     * @param stream The report is generated into this stream.
     */
    public void createPRBPreliminaryRosterFile(OutputStream stream) throws Exception {                
        BufferedWriter writer = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();        
        int rebateYear = cal.get(Calendar.YEAR)-1;
                
        try {            
            con = DBUtil.getConnection();            
            checkEvaluationCode();
            
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            
            // Construct the specialized SQL query or a series of queries for this report
            String query =  SQL_SELECT_PRELIMINARY_ROSTER + 
                            " WHERE " + ((getAffPks().length() > 0) ? "aff."+getAffPks()+" AND " : " ") +
                            SQL_SELECT_PRELIMINARY_ROSTER_WHERE_CLAUSE + 
                            SQL_SELECT_PRELIMINARY_ROSTER_ORDER_BY;
            logger.debug("Query for Preliminary Roster Report: " +query);
            ps = con.prepareStatement(query);
            ps.setInt(1, PRBRosterStatus.P.intValue());
            ps.setInt(2, rebateYear);
            rs = ps.executeQuery();            
            writer.write(",,,,,,,AFSCME - POLITICAL REBATE - PRELIMINARY ROSTER,,,,,,,,,");
            writer.newLine();            
            writer.write("STATE,COUNCIL,UNITCODE,SSN,LAST NAME," +
                         "FIRST NAME,MI,ADDRESS,AUXILIARY ADDRESS,CITY,ST,ZIP," +
                         "MEMBER TYPE,MEMBER STATUS,DUES TYPE,NUMBER OF MONTHS,ACCP CD");
            writer.newLine();
            while (rs.next()) {
                String affState = TextUtil.isEmptyOrSpaces(rs.getString(2)) ? "" : rs.getString(2);                
                String council = TextUtil.isEmptyOrSpaces(rs.getString(3)) ? "" : rs.getString(3);
                String unitCode = (TextUtil.isEmptyOrSpaces(rs.getString(4)) ? "" : rs.getString(4)) + //affType
                                  (TextUtil.isEmptyOrSpaces(rs.getString(5)) ? "" : " " + rs.getString(5)); //local number              
                String ssn = TextUtil.isEmptyOrSpaces(rs.getString(6)) ? "" : rs.getString(6);
                String lastName = TextUtil.isEmptyOrSpaces(rs.getString(7)) ? "" : rs.getString(7);
                String firstName = TextUtil.isEmptyOrSpaces(rs.getString(8)) ? "" : " " + rs.getString(8);
                String middleName = TextUtil.isEmptyOrSpaces(rs.getString(9)) ? "" : " " + rs.getString(9); 
                String addr1 = TextUtil.isEmptyOrSpaces(rs.getString(10)) ? "" : rs.getString(10);
                String addr2 = TextUtil.isEmptyOrSpaces(rs.getString(11)) ? "" : " " + rs.getString(11);
                String city = TextUtil.isEmptyOrSpaces(rs.getString(12)) ? "" : " " + rs.getString(12);
                String state = TextUtil.isEmptyOrSpaces(rs.getString(13)) ? "" : " " + rs.getString(13);
                String zipCode = (TextUtil.isEmptyOrSpaces(rs.getString(14)) ? "" : rs.getString(14)) + //zipcode
                                 (TextUtil.isEmptyOrSpaces(rs.getString(15)) ? "" : " " + rs.getString(15)); //zip+4                                                                  
                String memberType = TextUtil.isEmptyOrSpaces(rs.getString(16)) ? "" : " " + rs.getString(16); 
                String memberStatus = TextUtil.isEmptyOrSpaces(rs.getString(17)) ? "" : " " + rs.getString(17); 
                String memberDuesType = TextUtil.isEmptyOrSpaces(rs.getString(18)) ? "" : " " + rs.getString(18); 
                String duration = TextUtil.isEmptyOrSpaces(rs.getString(19)) ? "" : " " + rs.getString(19); 
                String acceptanceCode = TextUtil.isEmptyOrSpaces(rs.getString(20)) ? "" : " " + rs.getString(20);                 

                // Write out to stream
                writer.write(affState + "," +
                             council + "," +
                             unitCode + "," +
                             ssn + "," +
                             lastName + "," + 
                             firstName + "," +
                             middleName + "," +
                             addr1 + "," +
                             addr2 + "," +
                             city + "," +
                             state + "," +
                             zipCode + "," +
                             memberType + "," +
                             memberStatus + "," +
                             memberDuesType + "," +
                             duration + "," +
                             acceptanceCode);                
                writer.newLine();                
            }    
        } finally {
            DBUtil.cleanup(con, ps, rs);
            writer.flush();
            writer.close();
        }
    }
    
    
    private void checkEvaluationCode() throws Exception {                 
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        // Get the config data of Application Mailed Date
        String val = getAppConfigData(PRBConstants.CONFIG_VARIABLE_APP_MAILED_DT);        
        if (val==null) return;

        try {
            con = DBUtil.getConnection();            
            ps = con.prepareStatement(SQL_SELECT_EVALUATION_DATA);
            rs = ps.executeQuery();
            while (rs.next()) {                
                // Get the duration of App Mailed Date
                // If the duration is passed the number of days allowed, then set the
                // Application Evaluation Code to Not Timely...                     
                long dayDiff = (System.currentTimeMillis() - rs.getTimestamp(2).getTime()) / 86400000;
                if (val!=null && dayDiff > new Integer(val).longValue()) {
                    updateEvaluationCode(new Integer(rs.getInt(1)));
                }            
            }                
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }                   
    }

    private void updateEvaluationCode(Integer appPk) throws Exception {                 
        Connection con = null;
        PreparedStatement ps = null;

        if (appPk==null || appPk.intValue()<=0)
            return;
        
        try {
            con = DBUtil.getConnection();            
            ps = con.prepareStatement(SQL_UPDATE_EVALUATION_CD);
            ps.setInt(1, appPk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }                   
    }
    
    /**
     * @J2EE_METHOD  --  getAppConfigData
     * Retrieves the value of the specified configuration data
     *
     */
    private String getAppConfigData(String variableName) throws Exception {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();              
            ps = con.prepareStatement(SQL_SELECT_APP_CONFIG_VALUE);
            ps.setString(1, variableName);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }        
        return result;
    }    
    
    /**
     * Generates a report
     *
     * @param stream The report is generated into this stream.
     */
    public int generate(OutputStream stream) throws Exception {        
        try {
            createPRBPreliminaryRosterFile(stream);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return 0;
    }
    
    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName() {
        return "Rebate Preliminary Roster";
    }
    
    /** Getter for property affPks.
     * @return Value of property affPks.
     *
     */
    public java.lang.String getAffPks() {
        return affPks;
    }
    
    /** Setter for property affPks.
     * @param affPks New value of property affPks.
     *
     */
    public void setAffPks(java.lang.String affPks) {
        this.affPks = affPks;
    }
    
}
