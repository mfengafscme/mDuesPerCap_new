
package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;

/**
 * This is a sample specialized report.
 */
public class NameCountReport implements ReportHandler {

    /**
     * The SQL that implements the report
     */
    private static final String QUERY = 
        " select count(first_nm) total, states.com_cd_desc " +
        " from person p " +
        " inner join person_address a on p.person_pk  = a.person_pk " +
        " inner join common_codes states on com_cd_cd = state AND states.com_cd_type_key = 'State'" +
        " where first_nm like ? " +
        " group by states.com_cd_desc " +
        " order by total desc ";
     
    /** The name to search on */
    private String firstName;
    
    /**
     * Generates a report
     * 
     * @param stream The report is generated into this stream.
     */
    public int generate(OutputStream stream) throws Exception {
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        
        Connection con = DBUtil.getConnection();
        
        // Construct the specialized SQL query or a series of queries for this report
        PreparedStatement stmt = con.prepareStatement(QUERY);
        if (firstName != null) {
            stmt.setString(1, firstName);
        } else {
            stmt.setString(1, "%");
        }
        ResultSet rs = stmt.executeQuery();  

        //write the header
        writer.write("Count of people in each state");
        if (firstName != null)
            writer.write(" with the first name '" + firstName + "'");
        writer.newLine();
        writer.newLine();
        
        //write the states
        int count = 0;
        while (rs.next()) {
            count++;
            int total = rs.getInt(1);
            String state = rs.getString(2);
            
            writer.write(state + ": ");
            writer.write(String.valueOf(total));
            writer.newLine();
        }
        
        DBUtil.cleanup(con, stmt, rs);

        writer.flush();
        writer.close();
        
        return count;
    }
    
    /** 
     * Gets the name to search on
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** 
     * Sets the name to search on
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName() {
        return null;
    }    
    
}
