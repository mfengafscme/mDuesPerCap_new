package org.afscme.enterprise.reporting.specialized;

import java.sql.*;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.codes.Codes.PRBRosterStatus;
import org.afscme.enterprise.reporting.ReportHandler;
import org.apache.log4j.Logger;


/**
 * This is a Rebate Update File Report.
 * SUMMARY: Creates the Update file that the affiliate will
 * update selective information to approve or deny the rebate request
 */
public class RebateUpdateFileReport implements ReportHandler {
        
    private static Logger logger =  Logger.getLogger(RebateUpdateFileReport.class);
    public static final int RECORD_LEN = 284;    
    private String affPks;
    
    /* Selects a list of the individuals that are getting checks */
    private static final String SQL_SELECT_REBATE_UPDATE_FILE = 
    "SELECT distinct(person.person_pk), aff_type, aff_localSubChapter, " +
    "       aff_stateNat_type, aff_subUnit, aff_councilRetiree_chap, " +
    "       ssn, isnull(duplicate_ssn_fg, 0), first_nm, middle_nm, last_nm,  " +
    "       addr1, addr2, city, state, zipcode, zip_plus, province, " +
    "       (SELECT com_cd_cd FROM common_codes cc where cc.com_cd_pk = country), " +
    "       (SELECT com_cd_cd FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_type), " +
    "       (SELECT com_cd_cd FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_status), " +
    "       (SELECT com_cd_cd FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_dues_rate), " +
    "       (SELECT com_cd_cd FROM common_codes cc where cc.com_cd_pk = roster_duration_in_aff), " +
    "       (SELECT com_cd_cd FROM common_codes cc where cc.com_cd_pk = roster_acceptance_cd) " +
    "FROM   PRB_Roster_Persons roster " +
    "JOIN   Person person ON person.person_pk = roster.person_pk " +
    "JOIN   PRB_Rbt_Year_Info rebate ON roster.person_pk = rebate.person_pk " +
    "JOIN   Aff_Organizations aff ON aff.aff_pk = roster.aff_pk " +
    "LEFT OUTER JOIN Person_Address address ON address.person_pk = person.person_pk and " +
    "       address_pk IN (SELECT address_pk " +
    "                      FROM person_SMA " +
    "                      WHERE person_pk = person.person_pk AND current_fg = 1) ";
    
    private static final String SQL_SELECT_REBATE_UPDATE_FILE_WHERE_CLAUSE = 
    "roster_aff_status=? and " +
    "rebate.rbt_year=? and " +
    "file_generated_dt is null ";
    
    private static final String SQL_SELECT_REBATE_UPDATE_FILE_ORDER_BY = 
    "ORDER BY aff_localSubChapter, ssn, last_nm";

    
    /* Update the file_generate_dt field from table prb_roster_persons */
    private static final String SQL_UPDATE_ROSTER_FILE_GENERATED_DATE =
    "UPDATE PRB_Roster_Persons " +
    "SET    file_generated_dt=? ";

    private static final String SQL_UPDATE_ROSTER_FILE_GENERATED_DATE_WHERE_CLAUSE =
    "roster_aff_status=? and rbt_year=? ";

    
    
   /**
     * updateRosterFileGeneratedDate() - This method will update the
     * prb_roster_persons.file_generated_dt field so next time
     * when a report is generated, it won't pick up the data that already
     * ran by the previous report.
     */
    public void updateRosterFileGeneratedDate() {
        Connection con = null;
        PreparedStatement ps = null;
        Calendar cal = Calendar.getInstance();        
        
        try {
            con = DBUtil.getConnection();
            String query =  SQL_UPDATE_ROSTER_FILE_GENERATED_DATE + 
                            " WHERE " + ((getAffPks().length() > 0) ? getAffPks()+" AND " : " ") + 
                            SQL_UPDATE_ROSTER_FILE_GENERATED_DATE_WHERE_CLAUSE;
            ps = con.prepareStatement(query);
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setInt(2, PRBRosterStatus.P.intValue());
            ps.setInt(3, cal.get(Calendar.YEAR)-1);
            ps.executeUpdate();
        } catch (SQLException e) {
        } finally {
            DBUtil.cleanup(con, ps, null);
        }        
    }
        
   /**
     * createPRBUpdateFile
     * 
     * @param stream The report is generated into this stream.
     */
    public void createPRBUpdateFile(OutputStream stream) throws Exception {                
        BufferedWriter writer = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();        
        int rebateYear = cal.get(Calendar.YEAR)-1;
        
        try {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            con = DBUtil.getConnection();
            
            // Construct the specialized SQL query or a series of queries for this report
            String query =  SQL_SELECT_REBATE_UPDATE_FILE + 
                            " WHERE " + ((getAffPks().length() > 0) ? "aff."+getAffPks()+" AND " : " ") + 
                            SQL_SELECT_REBATE_UPDATE_FILE_WHERE_CLAUSE + 
                            SQL_SELECT_REBATE_UPDATE_FILE_ORDER_BY;
            logger.debug("Query for Rebate Update File Report: " +query);
            ps = con.prepareStatement(query);
            ps.setInt(1, PRBRosterStatus.P.intValue());
            ps.setInt(2, rebateYear);
            rs = ps.executeQuery();

            while (rs.next()) {
                Integer mbrNumber = new Integer(rs.getInt(1)); 
                String affType = TextUtil.isEmptyOrSpaces(rs.getString(2)) ? "" : rs.getString(2);
                String local = TextUtil.isEmptyOrSpaces(rs.getString(3)) ? "" : rs.getString(3);
                String affState = TextUtil.isEmptyOrSpaces(rs.getString(4)) ? "" : rs.getString(4);                
                String unitCode = TextUtil.isEmptyOrSpaces(rs.getString(5)) ? "" : rs.getString(5);                
                String council = TextUtil.isEmptyOrSpaces(rs.getString(6)) ? "" : rs.getString(6);                
                String ssn = TextUtil.isEmptyOrSpaces(rs.getString(7)) ? "000000000" : rs.getString(7);
                Integer dupSSSNFg = new Integer(rs.getInt(8));
                String firstName = TextUtil.isEmptyOrSpaces(rs.getString(9)) ? "" : rs.getString(9);
                String middleName = TextUtil.isEmptyOrSpaces(rs.getString(10)) ? "" : rs.getString(10);                                
                String lastName = TextUtil.isEmptyOrSpaces(rs.getString(11)) ? "" : rs.getString(11);
                String addr1 = TextUtil.isEmptyOrSpaces(rs.getString(12)) ? "" : rs.getString(12);
                String addr2 = TextUtil.isEmptyOrSpaces(rs.getString(13)) ? "" : rs.getString(13);
                String city = TextUtil.isEmptyOrSpaces(rs.getString(14)) ? "" : rs.getString(14);
                String state = TextUtil.isEmptyOrSpaces(rs.getString(15)) ? "" : rs.getString(15);
                String zipCode = TextUtil.isEmptyOrSpaces(rs.getString(16)) ? "00000" : rs.getString(16); 
                String zipPlus = TextUtil.isEmptyOrSpaces(rs.getString(17)) ? "0000" : rs.getString(17);
                String province = TextUtil.isEmptyOrSpaces(rs.getString(18)) ? "" : rs.getString(18);
                String country = TextUtil.isEmptyOrSpaces(rs.getString(19)) ? "" : rs.getString(19);
                String memberType = TextUtil.isEmptyOrSpaces(rs.getString(20)) ? " " : rs.getString(20); 
                String memberStatus = TextUtil.isEmptyOrSpaces(rs.getString(21)) ? "" : rs.getString(21); 
                String memberDuesType = TextUtil.isEmptyOrSpaces(rs.getString(22)) ? "" : rs.getString(22); 
                String duration = TextUtil.isEmptyOrSpaces(rs.getString(23)) ? "" : rs.getString(23); 
                String acceptanceCode = TextUtil.isEmptyOrSpaces(rs.getString(24)) ? "" : rs.getString(24); 
                               
                // Set the position of each field
                char[] data = new char[RECORD_LEN];
                setPosition(0, 1, affType, data);
                setPosition(1, 4, local, data);                               
                setPosition(5, 2, affState, data);
                setPosition(7, 4, unitCode, data);
                setPosition(11, 4, council, data);
                setPosition(15, 8, TextUtil.toString(mbrNumber), data);
                setPosition(23, 9, ssn, data);
                setPosition(32, 1, TextUtil.toString(dupSSSNFg), data);
                setPosition(33, 25, firstName, data);
                setPosition(58, 25, middleName, data);
                setPosition(83, 25, lastName, data);
                setPosition(108, 50, addr1, data);
                setPosition(158, 50, addr2, data);
                setPosition(208, 25, city, data);
                setPosition(233, 25, province, data);
                setPosition(258, 2, state, data);
                setPosition(260, 12, zipCode, data);
                setPosition(272, 4, zipPlus, data);
                setPosition(276, 2, country, data);
                setPosition(278, 1, memberType, data);
                setPosition(279, 1, memberStatus, data);
                setPosition(280, 1, memberDuesType, data);                
                setPosition(281, 2, duration, data);
                setPosition(283, 1, acceptanceCode, data);
                
                // Write out to stream
                writer.write(data);    
                writer.newLine();                
            }    
        } finally {
            DBUtil.cleanup(con, ps, rs);
            writer.flush();
            writer.close();
        }
    }
    
    /**
     * Set field position for rebate update file record
     */
    public static void setPosition(int position, int offset, String value, char[] data) {        
        int p = position;
        
        for (int i=0; i<value.length(); i++, p++) 
            data[p] = value.charAt(i);
        
        for (int j=value.length(); j<offset; j++, p++)
            data[p] = ' ';        
    }
    
    
    /**
     * Generates a report
     *
     * @param stream The report is generated into this stream.
     */
    public int generate(OutputStream stream) throws Exception {        
        try {
            createPRBUpdateFile(stream);
            updateRosterFileGeneratedDate();
        } catch (Exception e) {
            throw new Exception(e);
        }
        return 0;
    }
    
    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName() {
        return "Rebate Update File";
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
