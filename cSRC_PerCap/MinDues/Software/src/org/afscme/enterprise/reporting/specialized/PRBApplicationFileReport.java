package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Iterator;
import java.util.Calendar;
import java.math.BigDecimal;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.person.PRBRequestData;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebate;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.codes.Codes.RebateStatus;


/**
 * This is a Political Rebate Application File report.
 *
 * SUMMARY:
 * Compiles a list of Political Rebate Requests that have not been
 * included in a prior application run and have not been denied and
 * combines multiple requests from a single Requester into one application.
 *
 * Creates an Application record with the supplied mailed date,
 * sets the default for the Evaluation Code to "Not Returned" and
 * includes the Dues Paid To and Duration from the Request record.
 *
 * Generates the file to produce the "mailing labels".
 */
public class PRBApplicationFileReport implements ReportHandler {
    
    /** The application mailed date */
    private Timestamp appMailedDate;
    private Integer userPk;
    
    private static final String QUERY =
    // HLM: Fix defect #114
    "SELECT distinct first_nm, middle_nm, last_nm, ssn, aff_localSubChapter, " +
    "       aff_councilRetiree_chap, addr1, addr2, city, " +
    "       state, zipcode, zip_plus, " +
    "       (SELECT variable_value FROM COM_App_Config_Data WHERE variable_name='FiscalYearEnd') " +
    "FROM PRB_Requests r " +
    "JOIN PRB_Request_Affs aff ON aff.rqst_pk = r.rqst_pk " +
    "JOIN Aff_Organizations o ON o.aff_pk = aff.aff_pk " +
    "JOIN Person p ON p.person_pk  = r.person_pk " +
    "LEFT OUTER JOIN Person_Address addr ON addr.person_pk = p.person_pk and " +
    "       address_pk IN (SELECT address_pk " +
    "                      FROM person_SMA " +
    "                      WHERE person_pk = p.person_pk AND current_fg = 1) " +
    "WHERE r.prb_app_pk is null and " +
    "      rqst_status != ? and " +
    "      rqst_rebate_year = ? " +
    "ORDER BY last_nm, first_nm, state, city ";
    
    /**
     * performPRBApplicationRun : Compiles a list of Political Rebate Requests
     * that have not been included in a prior application run and have not
     * been denied and combines multiple requests from a single Requester into
     * one application.
     */
    public void performPRBApplicationRun() throws Exception {
        MaintainPoliticalRebate maintainPoliticalRebate = null;
        
        try {
            maintainPoliticalRebate = JNDIUtil.getMaintainPoliticalRebateHome().create();
        } catch (NamingException ne) {
            throw new Exception(ne);
        } catch (CreateException ce) {
            throw new Exception(ce);
        }
        
        // Get the list of all Political Rebate Requests
        // in the current rebate year
        List rqstList = maintainPoliticalRebate.getPRBApplicationEligible();
        if (rqstList == null || rqstList.size()<=0) {
            return;
        }
        
        Integer appPk = null;
        int priorPersonPk = 0;
        PRBRequestData rqstData = null;
        Iterator itr = rqstList.iterator();
        
        while (itr.hasNext()) {
            rqstData = (PRBRequestData) itr.next();
            
            // Create an Application record -
            // One application per person with multiple requests
            if (rqstData.getPersonPk() != priorPersonPk)
                appPk = maintainPoliticalRebate.createPRBApplicationRecord(getAppMailedDate(), getUserPk());
            
            if (appPk != null) {
                // Apply the Application Primary Key to Request record
                maintainPoliticalRebate.updatePRBRequestAppPk(rqstData.getRqstPk(), appPk, getUserPk());
                
                // Get the Affiliates associated with this Request
                List rqstAffList = maintainPoliticalRebate.getPRBAffiliates(new Integer(rqstData.getPersonPk()),
                                                                            rqstData.getRqstPk(),
                                                                            PRBConstants.PRB_REQUEST);
                // Associate the Request Affiliates to Application Affiliates
                PRBAffiliateData rqstAffData = null;
                Iterator itr2 = rqstAffList.iterator();
                while (itr2.hasNext()) {
                    rqstAffData = (PRBAffiliateData) itr2.next();
                    if (rqstAffData != null) {
                        // Fix defect #740. Check if this Affiliate already exists
                        // under Application Rebate before adding.
                        if (!maintainPoliticalRebate.isPRBAppAffiliateExist(appPk, rqstAffData.getAffPk()))
                            maintainPoliticalRebate.createPRBAffiliate(rqstAffData, appPk, PRBConstants.PRB_APPLICATION);
                    }
                }
            }
            // Set current processed person Pk to the prior value
            priorPersonPk = rqstData.getPersonPk();
        }
    }
    
    /**
     * Generates a report
     *
     * @param stream The report is generated into this stream.
     */
    public int generate(OutputStream stream) throws Exception {
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
            ps = con.prepareStatement(QUERY);
            ps.setInt(1, RebateStatus.D.intValue());
            ps.setInt(2, rebateYear);
            rs = ps.executeQuery();
            
            writer.write("First Name + Middle Name + Last Name,Local Number,Council Number," +
            "Address 1 + Address 2 + City, State + ZipCode + ZipPlus," +
            "SSN,Rebate Year End Date");
            writer.newLine();
            
            while (rs.next()) {
                String fullName = (TextUtil.isEmptyOrSpaces(rs.getString(1)) ? "" : rs.getString(1)) + //first name
                (TextUtil.isEmptyOrSpaces(rs.getString(2)) ? "" : " " + rs.getString(2)) + //middle name
                (TextUtil.isEmptyOrSpaces(rs.getString(3)) ? "" : " " + rs.getString(3)); //last name
                
                String address = (TextUtil.isEmptyOrSpaces(rs.getString(7)) ? "" : rs.getString(7)) + //addr1
                (TextUtil.isEmptyOrSpaces(rs.getString(8)) ? "" : " " + rs.getString(8)) + //addr2
                (TextUtil.isEmptyOrSpaces(rs.getString(9)) ? "" : " " + rs.getString(9)) + "," + //city
                (TextUtil.isEmptyOrSpaces(rs.getString(10)) ? "" : " " + rs.getString(10)) + //state
                (TextUtil.isEmptyOrSpaces(rs.getString(11)) ? "" : " " + rs.getString(11)) + //zipcode
                (TextUtil.isEmptyOrSpaces(rs.getString(12)) ? "" : " " + rs.getString(12)); //zip+4
                
                writer.write(fullName + "," + //first name + middle name + last name
                rs.getString(5) + "," + //local number
                rs.getString(6) + "," + //council number
                address + "," + //addr1 + addr2 + city, + state + zipcode + zipplus
                (TextUtil.isEmptyOrSpaces(rs.getString(4)) ? "": rs.getString(4)) + "," + //ssn
                (TextUtil.isEmptyOrSpaces(rs.getString(13)) ? "": rs.getString(13))); //rebate year end date
                writer.newLine();
            }
            // HLM: Fix defect #114
            // Perform Polical Rebate Application Run after the report is generated           
            performPRBApplicationRun();                    
            
        } finally {
            DBUtil.cleanup(con, ps, rs);
            writer.flush();
            writer.close();
        }
        return 0;
    }
    
    /** Getter for property appMailedDate.
     * @return Value of property appMailedDate.
     *
     */
    public Timestamp getAppMailedDate() {
        return appMailedDate;
    }
    
    /** Setter for property appMailedDate.
     * @param appMailedDate New value of property appMailedDate.
     *
     */
    public void setAppMailedDate(Timestamp appMailedDate) {
        this.appMailedDate = appMailedDate;
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
        return "Rebate Application File";
    }
    
}
