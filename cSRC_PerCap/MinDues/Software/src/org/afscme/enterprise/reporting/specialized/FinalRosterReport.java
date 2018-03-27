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
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.codes.Codes.PRBRosterStatus;


/**
 * This is a Final Roster report.
 * SUMMARY: This report provides the final listing of the 
 * individuals that were given a Rebate for the current rebate 
 * year.
 */
public class FinalRosterReport implements ReportHandler {
     
    /* Selects the final listing of the individuals that were given a 
     * Rebate for the current rebate year */
    private static final String QUERY_FINAL_ROSTER = 
    "SELECT distinct(roster.person_pk), aff_stateNat_type, aff_councilRetiree_chap, " +
    "       aff_type, aff_localSubChapter, " +
    "       ssn, last_nm, first_nm, middle_nm, " +
    "       addr1, addr2, city, state, zipcode, country, zip_plus, rbt_check_amt_1, " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_type), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_status), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = rebate_year_mbr_dues_rate), " +
    "       (SELECT com_cd_desc FROM common_codes cc where cc.com_cd_pk = roster_duration_in_aff) " +
    "FROM   PRB_Roster_Persons roster " +
    "JOIN   PRB_Rebate_Check_Info checkInfo ON roster.person_pk = checkInfo.person_pk " +
    "JOIN   Person person ON person.person_pk = roster.person_pk " +
    "JOIN   Aff_Organizations aff ON aff.aff_pk = roster.aff_pk " + 
    "LEFT OUTER JOIN Person_Address address ON address.person_pk = person.person_pk and " +
    "       address_pk IN (SELECT address_pk " +
    "                      FROM person_SMA " +
    "                      WHERE person_pk = person.person_pk AND current_fg = 1) " +
    "WHERE  roster_aff_status=? and " +
    "       checkInfo.rbt_year=? " +
    "ORDER BY aff_stateNat_type, aff_councilRetiree_chap, aff_type, aff_localSubChapter, ssn";
    
    /**
     * createFinalRoster
     *
     * @param stream The report is generated into this stream.
     */
    public void createFinalRoster(OutputStream stream) throws Exception {                
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
            ps = con.prepareStatement(QUERY_FINAL_ROSTER);
            ps.setInt(1, PRBRosterStatus.F.intValue());
            ps.setInt(2, rebateYear);
            rs = ps.executeQuery();
            
            writer.write("STATE,COUNCIL,UNITCODE,SSN,LAST NAME,FIRST NAME," +
                         "MI,ADDRESS,AUXILIARY ADDRESS,CITY,ST,ZIPCODE,COUNTRY,CHECK AMOUNT," +
                         "MEMBER TYPE,MEMBER STATUS,DUES TYPE,NUMBER OF MONTHS");
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
                String country = TextUtil.isEmptyOrSpaces(rs.getString(16)) ? "" : " " + rs.getString(16);                                 
                String checkAmount = TextUtil.toString(new Double(rs.getDouble(17))); 
                String memberType = TextUtil.isEmptyOrSpaces(rs.getString(18)) ? "" : " " + rs.getString(18); 
                String memberStatus = TextUtil.isEmptyOrSpaces(rs.getString(19)) ? "" : " " + rs.getString(19); 
                String memberDuesType = TextUtil.isEmptyOrSpaces(rs.getString(20)) ? "" : " " + rs.getString(20);
                String duration = TextUtil.isEmptyOrSpaces(rs.getString(21)) ? "" : " " + rs.getString(21); 

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
                             country + "," +
                             checkAmount + "," +
                             memberType + "," +
                             memberStatus + "," +
                             memberDuesType + "," +
                             duration);
                writer.newLine();
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
            writer.flush();
            writer.close();
        }
    }
    
    
    /**
     * Generates a report
     *
     * @param stream The report is generated into this stream.
     */
    public int generate(OutputStream stream) throws Exception {        
        try {
            createFinalRoster(stream);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return 0;
    }
    
    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName() {
        return "Rebate Final Roster";
    }
    
}
