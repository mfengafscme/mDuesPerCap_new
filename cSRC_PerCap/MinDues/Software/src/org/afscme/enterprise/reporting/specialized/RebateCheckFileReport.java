package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Iterator;
import java.util.Calendar;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.person.PRBCheckInfo;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebate;


/**
 * This is a Political Rebate Check File report.
 *
 * SUMMARY: Creates a list of Members that have been approved for a Rebate 
 * and are eligilbe to receive a Rebate, calculates the check 
 * amount, assigns the check number and date, create the Check File
 * in the format to be used by Accounting.
 *
 */
public class RebateCheckFileReport implements ReportHandler {
    
    /** The starting check number for the rebate */
    private int startingCheckNumber;
    private Integer userPk;
    
    /* Selects a list of the individuals that are getting checks */
    private static final String QUERY_CHECK_FILE = 
    "SELECT distinct(checkinfo.person_pk), first_nm, last_nm, rbt_check_1_run_dt, rbt_check_nbr_1, " +
    "       rbt_check_amt_1, aff_councilRetiree_chap, aff_localSubChapter, " +
    "       addr1, addr2, city, state, zipcode, zip_plus, " +
    "       (SELECT com_cd_desc FROM Common_Codes cc WHERE cc.com_cd_pk = country)  " +
    "FROM   PRB_Rebate_Check_Info checkInfo " +
    "JOIN   PRB_Roster_Persons roster ON roster.person_pk = checkInfo.person_pk " +
    "JOIN   Person person ON person.person_pk = checkInfo.person_pk " +
    "JOIN   Aff_Organizations aff ON aff.aff_pk = roster.aff_pk " +    
    "LEFT OUTER JOIN Person_Address address ON address.person_pk = checkInfo.person_pk and " +
    "       address_pk IN (SELECT address_pk " +
    "                      FROM person_SMA " +
    "                      WHERE person_pk = checkInfo.person_pk AND current_fg = 1) " +    
    "WHERE  rbt_check_nbr_1 is not null and " +
    "       checkInfo.rbt_year=? " +
    "ORDER BY rbt_check_nbr_1";

    /* Selects the total items and amount for check file */
    private static final String QUERY_CHECK_FILE_TOTALS = 
    "SELECT count(*), sum(rbt_check_amt_1) " +
    "FROM   prb_rebate_check_info " +
    "WHERE  rbt_check_nbr_1 is not null and rbt_year=?";
    
    /**
     * performPRBCheckRun: Create a list of Members that have been
     * approved for a Rebate and are eligilbe to receive a Rebate, calculates the check 
     * amount, assigns the check number and date, create the Check File, create the 
     * Check Register report, and set Members to the Final Roster.
     */
    public void performPRBCheckRun() throws Exception {  
        MaintainPoliticalRebate maintainPoliticalRebate = null;
        
        try {
            maintainPoliticalRebate = JNDIUtil.getMaintainPoliticalRebateHome().create();
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (CreateException ce) {
            throw new Exception(ce);
        }
        
        // Get a list of Members that have been approved for a Rebate 
        // and are eligilbe to receive a Rebate.
        List list = maintainPoliticalRebate.getPRBCheckEligible();
        if (list == null || list.size()<=0) {
            return;
        }
        
        // Set the starting check number
        int checkNumber = getStartingCheckNumber();
        int priorPersonPk=0;
        double checkAmount=0.0;
        PRBCheckInfo checkInfo;
        PRBAffiliateData affData = null;
        Iterator itr = list.iterator();
        
        while (itr.hasNext()) {
            affData = (PRBAffiliateData) itr.next();

            if (priorPersonPk != affData.getPersonPk().intValue()) {   

                // Calculates the check amount for each eligible member.
                checkAmount = maintainPoliticalRebate.calculatePRBCheckAmount(affData.getPersonPk());
                
                // Assigns the check number and date.
                checkInfo = maintainPoliticalRebate.getPRBCheckInfo(affData.getPersonPk(), new Integer(getCurrentRebateYear()));
                if (checkInfo == null) {
                    checkInfo = new PRBCheckInfo();
                    checkInfo.setPersonPK(affData.getPersonPk());
                    checkInfo.setRebateYear(new Integer(getCurrentRebateYear()));
                    checkInfo.setCheckNumber(new Integer(checkNumber));
                    checkInfo.setAmount(new Double(checkAmount));
                    checkInfo.setDate(DateUtil.getCurrentDateAsTimestamp());
                    maintainPoliticalRebate.createPRBCheckInfo(checkInfo, getUserPk());
                } else {
                    checkInfo.setCheckNumber(new Integer(checkNumber));
                    checkInfo.setAmount(new Double(checkAmount));
                    checkInfo.setDate(DateUtil.getCurrentDateAsTimestamp());
                    maintainPoliticalRebate.updatePRBCheckInfo(checkInfo, getUserPk());
                }
    
                // Updates the Members to the Final Roster who were 
                // part of the Check File to receive a rebate check.
                maintainPoliticalRebate.updateFinalRoster(affData.getPersonPk().intValue(), 
                                                          checkNumber,
                                                          new BigDecimal(checkAmount),
                                                          getUserPk());
                
                // Increment the check number for the next Member
                checkNumber++;                
                
            } else {
                // Reset check amount for the next Member
                checkAmount = 0;
            }
            
            // Set current processed person Pk to the prior value
            priorPersonPk = affData.getPersonPk().intValue();
        }        
    }        
    
   /**
     * createCheckFile: A list of the individuals that are getting checks
     * 
     * @param stream The report is generated into this stream.
     */
    public void createCheckFile(OutputStream stream) throws Exception {                
        BufferedWriter writer = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            con = DBUtil.getConnection();
            
            // Construct the specialized SQL query or a series of queries for this report
            ps = con.prepareStatement(QUERY_CHECK_FILE);
            ps.setInt(1, getCurrentRebateYear());
            rs = ps.executeQuery();            

            writer.write("First Name,Last Name,Check Date,Check Number,Check Amount,Council Number," +
                         "Local Number,Address 1 + Address 2 + City,State + ZipCode + ZipPlus");
            writer.newLine();
            while (rs.next()) {
                String firstName =  TextUtil.isEmptyOrSpaces(rs.getString(2)) ? "" : rs.getString(2);
                String lastName =  TextUtil.isEmptyOrSpaces(rs.getString(3)) ? "" : rs.getString(3);                
                String checkDate = (rs.getTimestamp(4)==null) ? "" : DateUtil.getSimpleDateString(rs.getTimestamp(4));
                String checkNumber = TextUtil.toString(new Integer(rs.getInt(5)));                              
                String checkAmount = TextUtil.toString(new Double(rs.getDouble(6))); 
                String council = TextUtil.isEmptyOrSpaces(rs.getString(7)) ? "" : rs.getString(7);
                String local = TextUtil.isEmptyOrSpaces(rs.getString(8)) ? "" : rs.getString(8);                
                String address = (TextUtil.isEmptyOrSpaces(rs.getString(9)) ? "" : rs.getString(9)) + //addr1
                                 (TextUtil.isEmptyOrSpaces(rs.getString(10)) ? "" : " " + rs.getString(10)) + //addr2 
                                 (TextUtil.isEmptyOrSpaces(rs.getString(11)) ? "" : " " + rs.getString(11)) + "," + //city
                                 (TextUtil.isEmptyOrSpaces(rs.getString(12)) ? "" : " " + rs.getString(12)) + //state
                                 (TextUtil.isEmptyOrSpaces(rs.getString(13)) ? "" : " " + rs.getString(13)) + //zipcode
                                 (TextUtil.isEmptyOrSpaces(rs.getString(14)) ? "" : " " + rs.getString(14)); //zip+4                                 

                // Write out to stream
                writer.write(firstName + "," + lastName + "," + checkDate + "," +
                             checkNumber + "," + checkAmount + "," + 
                             council + "," + local + "," + address);                                                 
                writer.newLine();                
            }
    
            // Get the rebate check file totals
            DBUtil.cleanup(con, ps, rs);
            con = DBUtil.getConnection();
            ps = con.prepareStatement(QUERY_CHECK_FILE_TOTALS);
            ps.setInt(1, getCurrentRebateYear());
            rs = ps.executeQuery();            
            if (rs.next()) {
                writer.write("Total Items: " + rs.getInt(1));
                writer.newLine();                
                writer.write("Total Amount: " + rs.getDouble(2));
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
            performPRBCheckRun();
            createCheckFile(stream);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return 0;
    }

    /**
     * @return int - The current rebate year
     */
    public int getCurrentRebateYear() {
        Calendar cal = Calendar.getInstance();        
        return cal.get(Calendar.YEAR)-1;
    }
    
    /** Getter for property startingCheckNumber.
     * @return Value of property startingCheckNumber.
     *
     */
    public int getStartingCheckNumber() {
        return startingCheckNumber;
    }
    
    /** Setter for property startingCheckNumber.
     * @param startingCheckNumber New value of property startingCheckNumber.
     *
     */
    public void setStartingCheckNumber(int startingCheckNumber) {
        this.startingCheckNumber = startingCheckNumber;
    }
    
    /** Getter for property userPk.
     * @return Value of property userPk.
     *
     */
    public java.lang.Integer getUserPk() {
        return userPk;
    }
    
    /** Setter for property userPk.
     * @param userPk New value of property userPk.
     *
     */
    public void setUserPk(java.lang.Integer userPk) {
        this.userPk = userPk;
    }
    
    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName() {
        return "Rebate Check File";
    }
    
}
