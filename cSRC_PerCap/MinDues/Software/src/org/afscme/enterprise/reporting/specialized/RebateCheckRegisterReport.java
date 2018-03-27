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
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebate;


/**
 * This is a Political Rebate Check Register report.
 *
 * SUMMARY: Creates the Check Register report of those members 
 * that a check will be sent to.
 *
 */
public class RebateCheckRegisterReport implements ReportHandler {
            
    /* Selects a list of the members that have received Rebate checks */
    private static final String QUERY_CHECK_REGISTER = 
    "SELECT distinct(checkinfo.person_pk), aff_stateNat_type, aff_councilRetiree_chap, aff_localSubChapter, " +
    "       first_nm, middle_nm, last_nm, " +
    "       rbt_check_nbr_1, rbt_check_amt_1, roster.aff_pk " +
    "FROM   PRB_Rebate_Check_Info checkInfo " +
    "JOIN   PRB_Roster_Persons roster ON roster.person_pk = checkInfo.person_pk " +
    "JOIN   Person person ON person.person_pk = checkInfo.person_pk " +
    "JOIN   Aff_Organizations aff ON aff.aff_pk = roster.aff_pk " +
    "WHERE  rbt_check_nbr_1 is not null " +
    // HLM Fix defect #749
    //"       and rbt_check_1_run_dt < getDate() and " +
    "       and checkInfo.rbt_year=? " +
    "ORDER BY aff_stateNat_type, aff_councilRetiree_chap, aff_localSubChapter, rbt_check_nbr_1";

   /**
     * createCheckRegister: A list of the members that have received Rebate checks
     *
     * @param stream The report is generated into this stream.
     */
    public void createCheckRegister(OutputStream stream) throws Exception {                
        BufferedWriter writer = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        MaintainPoliticalRebate s_maintainPoliticalRebate = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        Calendar cal = Calendar.getInstance(); 
        int rebateYear = cal.get(Calendar.YEAR)-1;

        try {
            String currentState = "";
            String currentLocal = "";
            String currentCouncil = "";
            boolean firstTimeThrough = true;
            double localTotal = 0;
            double councilTotal = 0;
            double stateTotal = 0;
            double grandTotal = 0;
            String local = "";
            String council = "";
            
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            con = DBUtil.getConnection();
            // Construct the specialized SQL query or a series of queries for this report
            ps = con.prepareStatement(QUERY_CHECK_REGISTER);
            ps.setInt(1, rebateYear);
            rs = ps.executeQuery();            
            writer.write(",,,,,AFSCME - REBATE SYSTEM CURRENT CHECK REGISTER,,,,,,");
            writer.newLine();
            writer.write("STATE,COUNCIL,LOCAL,PAYEE,CHECK NUMBER,CHECK AMOUNT," +
                         "LOCAL CHECK AMOUNT, TOTAL CHECK AMOUNT, LOCAL TOTAL, COUNCIL TOTAL, STATE TOTAL, GRAND TOTAL");
            writer.newLine();
            while (rs.next()) {                                   
                String state = TextUtil.isEmptyOrSpaces(rs.getString(2)) ? "" : rs.getString(2);
                
                if (firstTimeThrough) {
                    currentState = state;
                    writer.write(state + ",,,,,,,,,,,");
                    writer.newLine();
                }                
               
               local = TextUtil.isEmptyOrSpaces(rs.getString(4)) ? "" : rs.getString(4);                
                if (!local.equalsIgnoreCase(currentLocal)) {
                    // Print council total line
                    if (!firstTimeThrough) {
                        writer.write(",," + currentLocal +",,,,,," + localTotal + ",,test,");
                        writer.newLine();
                    }
                    localTotal = 0;                   
                    currentLocal = local;
                }
               
                council = TextUtil.isEmptyOrSpaces(rs.getString(3)) ? "" : rs.getString(3);
                if (!firstTimeThrough && !council.equalsIgnoreCase(currentCouncil)) {
                    // Print council total line
                    if (!TextUtil.isEmptyOrSpaces(currentCouncil)) {
                        writer.write("," + currentCouncil +",,,,,,,," + councilTotal + ",,");
                        writer.newLine(); 
                    }
                    councilTotal = 0;
                    currentCouncil = council;
                }               
                
                if (!state.equalsIgnoreCase(currentState)) {
                    // Print state total line
                    writer.write(currentState +",,,,,,,,,," + stateTotal + ",");
                    writer.newLine();                    
                    stateTotal = 0;                    
                    // Print next state header line
                    writer.write(state + ",,,,,,,,,,,");
                    writer.newLine();                    
                    currentState = state;
                }                
                
                String name = (TextUtil.isEmptyOrSpaces(rs.getString(5)) ? "" : rs.getString(5)) + //first name
                              (TextUtil.isEmptyOrSpaces(rs.getString(6)) ? "" : " " + rs.getString(6)) + //middle name
                              (TextUtil.isEmptyOrSpaces(rs.getString(7)) ? "" : " " + rs.getString(7)); //last name
                String checkNumber = TextUtil.toString(new Integer(rs.getInt(8)));
                String checkAmount = TextUtil.toString(rs.getBigDecimal(9)); 
                Integer affPk = new Integer(rs.getInt(10));
                Integer personPk= DBUtil.getIntegerOrNull(rs, 1);
                
                double localAmount = s_maintainPoliticalRebate.calculatePRBCheckAmount(personPk, affPk);
                
                localTotal += localAmount;
                councilTotal += localAmount;
                stateTotal += localAmount;
                grandTotal += localAmount;     
                
                // Write out to stream
                writer.write("," + 
                             council + "," +
                             local + "," +
                             name + "," +
                             checkNumber + "," +
                             localAmount + "," +
                             checkAmount + ",,,,,");
                writer.newLine();
                firstTimeThrough = false;
            }
            writer.write(",," + local +",,,,,," + localTotal + ",,,");            
            writer.newLine();
            if (!TextUtil.isEmptyOrSpaces(council)) {
                writer.write("," + council +",,,,,,,," + councilTotal + ",,");
                writer.newLine();
            }
            writer.write(currentState +",,,,,,,,,," + stateTotal + ",");
            writer.newLine();               
            writer.write(",,,,,,,,,,," + grandTotal);
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
            createCheckRegister(stream);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return 0;
    }
    
    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName() {
        return "Rebate Check Register";
    }
    
}
