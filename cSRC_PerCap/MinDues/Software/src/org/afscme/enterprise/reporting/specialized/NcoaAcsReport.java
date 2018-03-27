/** Class Name  : NcoaAcsReport.java
    Date Written: 20031104
    Author      : Kyung A. Callahan
    Description : This is a NCOA/ACS specialized report program.
    Note        : 
    Maintenance : Kyung 20031113 Fix sort order
 */
package org.afscme.enterprise.reporting.specialized;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import  java.text.SimpleDateFormat;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import java.util.*;
import java.util.GregorianCalendar;

// This is a NCOA/ACS specialized report.
public class NcoaAcsReport implements ReportHandler 
{
    // define instanace variables
    private String reportType;
    private String sqlQuery = "",
                   state = "",
                   council = "",
                   local = "",
                   memberOldAddress = "",
                   memberOldCity = "",
                   memberOldState = "",
                   memberOldZip = "",
                   memberOldZipPlus = "",
                   memberNewAddress = "",
                   memberNewCity = "",
                   memberNewState = "",
                   memberNewZip = "",
                   memberNewZipPlus = "",
                   ncoaChangedDate = "",
                   ncoaSource = "",
                   currentState = "",
                   currentCouncil = "",
                   currentLocal = "",
                   membersFullName = ""; 
    private     int localTotalmember = 0,
                councilTotalmember = 0,
                stateTotalmember = 0,
                localFinalTotalmember = 0,
                count = 0;
    /**
    * Generates a non-mailable roster report.
    */
    public int generate(OutputStream stream) throws Exception
    {
        BufferedWriter writer = null;
        Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;;
   	try 
        {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            printHeading(writer);
            setSqlQuery();
            con = DBUtil.getConnection();
            stmt = con.prepareStatement(sqlQuery);
            if (reportType != null) stmt.setString(1,reportType);
            else stmt.setString(1, "%");
            rs = stmt.executeQuery();
            // process results set
            while (rs.next()) 
            {
                getColumnVariable(rs);
                if (!currentState.equals(state) 
                    || !currentCouncil.equals(council) 
                    || !currentLocal.equals(local))
                {
                    stateCouncilLocalBreak(writer);
                } // end of state, council or local break
                processDetail(writer);
            } // end of while block
            finalRoutine(writer);
        } // end of try block
        catch (SQLException e) 
        {
            writer.write(" ");
            writer.newLine();
            writer.newLine();
            writer.write("An error has occurred in program - NcoaAcsReport.java");
            writer.newLine();
            writer.write("Error message: ");
            writer.write(e.getMessage());
            writer.newLine();
        }         
        finally 
        {
           DBUtil.cleanup(con, stmt, rs);
	   writer.flush();
           writer.close();
	} // end of finally block
        return 1;
    } // end of generate method
    /**
    * Write spaces
    */
    private void writeColumnSpace(BufferedWriter writer, int totalSpace)
	throws IOException
    {
	for(int i  = 0; i < totalSpace; i++) 
        {
		writer.write(" ");
	}
    }
    /**
    * Write value
    */
    private void writeValue(BufferedWriter writer, 
                            String value, 
                            int maximumLength)
    throws IOException
    {
	while(value.length() < maximumLength) 
        {
	    value += " ";
        }
	writer.write(value);
    }
    /**
    * Get report type
    */
    public String getReportType() 
    {
        return reportType;
    }
    /**
    * set report type
    */
    public void setReportType(String reportType) 
    {
        this.reportType = reportType;
    }  
    /**
    * get file name
    */
    public String getFileName() 
    {
        return null;
    }
    /**
    * Print heading
    */
    private void printHeading(BufferedWriter writer) throws IOException
    {
        writer.write(" ");
        writer.newLine();
        writeColumnSpace(writer, 9);
        writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
        writer.newLine();
        if (reportType.equals("L")) {
            writeColumnSpace(writer, 37);
            writer.write("NCOA/ACS Report");
        }
        else {
            writeColumnSpace(writer, 30);
            writer.write("NCOA/ACS Report for Retiree");
        }
        writer.newLine();
        printCurrentDate(writer);
        writer.newLine();
    }
    /**
    * Get current date
    */
    private void printCurrentDate(BufferedWriter writer) throws IOException
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "MM/dd/yyyy";
        java.text.SimpleDateFormat sdf = 
            new java.text.SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());          
        if (reportType.equals("L")) writeColumnSpace(writer, 39);
        else writeColumnSpace(writer, 39);
        writer.write(sdf.format(cal.getTime()));
        writer.newLine();
        writer.newLine();
    }
    /**
    * Set SQL that implements the report
    */
    private void setSqlQuery() 
        throws IOException, SQLException
    {
        sqlQuery =
            "select " + 
            "Aff_Organizations.aff_statenat_type as state, " + 
            "Aff_Organizations.aff_councilRetiree_chap as council, " +
            "Aff_Organizations.aff_localSubChapter as local, " + 
            "person.last_nm + ', ' + person.first_nm as person_full_name, " +
            "ncoa_transactions.extracted_sma_addr1 as old_address1, " +
            "ncoa_transactions.extracted_sma_addr2 as old_address2, " +
            "ncoa_transactions.extracted_sma_city as old_city, " +
            "ncoa_transactions.extracted_sma_state old_state, " +
            "ncoa_transactions.extracted_sma_zipcode as old_zipcode, " +
            "ncoa_transactions.vendor_addr1 as new_address1, " +
            "ncoa_transactions.vendor_addr2 as new_address2, " +
            "ncoa_transactions.vendor_city as new_city, " +
            "ncoa_transactions.vendor_state new_state, " +
            "ncoa_transactions.vendor_zipcode as new_zipcode, " +
            "ncoa_transactions.vendor_zip_plus as new_zip_plus, " +
            "ncoa_transactions.vendor_source as ncoa_source, " +
            "convert(char(12), ncoa_transactions.vendor_ncoa_change_dt, 101) as ncoa_changed_date, " +
            "cast((select common_codes.com_cd_sort_key " +
            "from common_codes " + 
            "where common_codes.com_cd_cd = aff_organizations.aff_statenat_type " + 
            "and common_codes.com_cd_type_key = 'affiliatestate') as int) " +
            "as sort_state, " +
            "case " +
            "when aff_type = 'c' then 1 " +
            "when aff_type = 'l' then 1 " + 
            "when aff_type = 'u' then 1 " + 
            "when aff_type = 'r' then 2 " +
            "when aff_type = 's' then 2 " +
            "end as sort_type, " +
            "cast(aff_councilRetiree_chap as int) as sort_council, " +
            "cast(aff_localSubChapter as int) as sort_local, " +
            "cast(aff_subunit as int) as sort_sublocal " +
            "from aff_organizations, " + 
            "aff_members, " +
            "person, " +
            "person_address, " +
            "ncoa_transactions " +
            "where Aff_Organizations.aff_pk = aff_members.aff_pk " +
            "and aff_members.mbr_status = 31001 /* active member */ " +
            "and person.person_pk = aff_members.person_pk " +
            "and person_address.person_pk = person.person_pk " +
            "and person_address.addr_prmry_fg = 1 " +
            "and person_address.address_pk = ncoa_transactions.address_pk " +
            "and aff_Organizations.aff_type = ? " +
            "order by sort_type, " + 
            "Aff_Organizations.aff_statenat_type, " +
            "sort_council, " + 
            "sort_local, " +
            "sort_sublocal, " +
            "person_full_name ";
    } // end of set sql query
    /**
    * Get the column values from resultSet
    */
    private void getColumnVariable(ResultSet rs) 
        throws IOException, SQLException
    {
        count++;
        state = rs.getString("state");
        council = rs.getString("council");
        local = rs.getString("local");
        membersFullName = rs.getString("person_full_name");
        if (TextUtil.isEmpty(membersFullName)) membersFullName = " ";
        if (membersFullName.length() > 30)
        {
            membersFullName = membersFullName.substring(0, 29);
        }
        memberOldAddress = rs.getString("old_address1");
        if (TextUtil.isEmpty(memberOldAddress)) memberOldAddress = " ";
        if (memberOldAddress.length() > 30)
        {
            memberOldAddress = memberOldAddress.substring(0, 29);
        }
        memberOldCity = rs.getString("old_city");
        if (TextUtil.isEmpty(memberOldCity)) memberOldCity = " ";
        memberOldState = rs.getString("old_state");
        if (TextUtil.isEmpty(memberOldState)) memberOldState = " ";
        memberOldZip = rs.getString("old_zipcode");
        if (TextUtil.isEmpty(memberOldZip)) memberOldZip = " ";
        //memberOldZipPlus = rs.getString("old_zip_plus");
        if (TextUtil.isEmpty(memberOldZipPlus)) memberOldZipPlus = " ";
        memberNewAddress = rs.getString("new_address1");
        if (TextUtil.isEmpty(memberNewAddress)) memberNewAddress = " ";
        if (memberNewAddress.length() > 30)
        {
            memberNewAddress = memberNewAddress.substring(0, 29);
        }
        memberNewCity = rs.getString("new_city");
        if (TextUtil.isEmpty(memberNewCity)) memberNewCity = " ";
        memberNewState = rs.getString("new_state");
        if (TextUtil.isEmpty(memberNewState)) memberNewState = " ";
        memberNewZip = rs.getString("new_zipcode");
        if (TextUtil.isEmpty(memberNewZip)) memberNewZip = " ";
        memberNewZipPlus = rs.getString("new_zip_plus");
        if (TextUtil.isEmpty(memberNewZipPlus)) memberNewZipPlus = " ";
        ncoaChangedDate = rs.getString("ncoa_changed_date");
        if (TextUtil.isEmpty(ncoaChangedDate)) ncoaChangedDate = " ";
        ncoaSource = rs.getString("ncoa_source");
        if (TextUtil.isEmpty(ncoaSource)) ncoaSource = " ";
    } // end of get column variables
    /**
    * Council break routine
    */
    private void printLocalNameAddress(BufferedWriter writer) 
        throws IOException, Exception
    {
        writeValue(writer, state, 2);
        if (council == null || council.equals("    ")) writeColumnSpace(writer, 4);
        else writeValue(writer, council, 4);
        writeValue(writer, local, 4);
        writer.newLine();                
        writer.newLine();                
        writer.write("Name/Old Address                 New");
        writer.write(" Address                      Source DateChanged");
        writer.newLine();
        writer.newLine();
        writer.newLine();
    } // end of print local name and address method
    /**
    * Process detail
    */
    private void processDetail(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        localFinalTotalmember++; 
        /**
         * Print member name, ncoa source, and ncoa date changed
         */
        writeValue(writer, membersFullName, 30);
        writeColumnSpace(writer, 36);
        writeValue(writer, ncoaSource, 1);
        writeColumnSpace(writer, 6);
        writeValue(writer, ncoaChangedDate, 10);
        writer.newLine();
        writer.newLine();
        /**
         * Print old address
         */
        writeValue(writer, memberOldAddress, 30);
        writeColumnSpace(writer, 3);
        writeValue(writer, memberNewAddress, 30);
        writer.newLine();
        writeValue(writer, memberOldCity, 15);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberOldState, 2);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberOldZip, 5);
        writeColumnSpace(writer, 1);
        if(memberOldZipPlus == null) 
        {
            writeValue(writer, "", 4);
        }
        else 
        {
            writeValue(writer, memberOldZipPlus, 4);
        }  
        /**
         * Print new address
         */
        writeColumnSpace(writer, 4);
        writeValue(writer, memberNewCity, 15);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberNewState, 2);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberNewZip, 5);
        writeColumnSpace(writer, 1);
        if(memberNewZipPlus == null) 
        {
            writeValue(writer, "", 4);
        }
        else 
        {
            writeValue(writer, memberNewZipPlus, 4);
        }  
        writer.newLine();
        writer.newLine();
        localTotalmember++;
        councilTotalmember++;
        stateTotalmember++;
    } // end of process detail
    /**
    * State, council, and local break routine
    */
    private void stateCouncilLocalBreak(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
            if (count == 1) 
            {
                currentLocal = local;
                currentState = state;
                currentCouncil = council;
                writer.write("------------------------------------------");
                writer.write("------------------------------------------");
                writer.newLine();                   
                writer.newLine();                   
                printLocalNameAddress(writer);
            }
            else if (!currentState.equals(state)) 
            {
                localBreak(writer);
                councilBreak(writer);
                stateBreak(writer);
                printLocalNameAddress(writer);
            }
            else if (!currentCouncil.equals(council)) 
            {
                localBreak(writer);
                councilBreak(writer);
                printLocalNameAddress(writer);
            }
            else
            {
                localBreak(writer);
                printLocalNameAddress(writer);
            }
    } // end of state, council, and local break method
    /**
    * local break routine
    */
    private void localBreak(BufferedWriter writer) 
        throws IOException, Exception, SQLException
    {
        writer.newLine();
        writer.write(" Local total Address Change for ");
        writeValue(writer, currentState, 2);
        if (currentCouncil == null || currentCouncil.equals("    ")) writeColumnSpace(writer, 4);
        else writeValue(writer, currentCouncil, 4);
        writeValue(writer, currentLocal, 4);
        writer.write(": ");
        writeValue(writer, Integer.toString(localTotalmember), 6);
        writer.newLine();                   
        writer.newLine();                   
        writer.write("------------------------------------------");
        writer.write("------------------------------------------");
        writer.newLine();                   
        writer.newLine();                   
        currentLocal=local;
        localTotalmember = 0;
    } // end of local break method
    /**
    * Council break routine
    */
    private void councilBreak(BufferedWriter writer) 
        throws IOException, Exception
    {
        writer.newLine();
        writer.write("** Council total address change for ");
        writeValue(writer, currentState, 2);
        if (currentCouncil == null || currentCouncil.equals("    ")) writeColumnSpace(writer, 4);
        else writeValue(writer, currentCouncil, 4);
        writer.write(": ");
        writeValue(writer, Integer.toString(councilTotalmember), 6);
        writer.newLine();
        writer.newLine();
        writer.write("------------------------------------------");
        writer.write("------------------------------------------");
        writer.newLine();                   
        writer.newLine();                   
        currentCouncil=council;
        currentLocal=local;
        localTotalmember = 0;
        councilTotalmember = 0;
    } // end of council break method
    /**
    * State break routine
    */
    private void stateBreak(BufferedWriter writer) 
        throws IOException, Exception
    {
        writer.newLine();
        writer.write("******* State total address change for ");
        writeValue(writer, currentState, 2);
        writer.write(" : ");
        writeValue(writer, Integer.toString(stateTotalmember), 6);
        writer.newLine();
        writer.newLine();
        writer.write("------------------------------------------");
        writer.write("------------------------------------------");
        writer.newLine();                   
        writer.newLine();                   
        currentState=state;
        currentCouncil=council;
        currentLocal=local;
        localTotalmember = 0;
        councilTotalmember = 0;
        stateTotalmember = 0;
    } // end of state break method
    private void finalRoutine(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        localBreak(writer);
        councilBreak(writer);
        stateBreak(writer);
        writer.newLine();                   
        writer.newLine();                   
        writer.newLine();                   
        writer.write("***Final total address change: ");
        writeValue(writer, Integer.toString(localFinalTotalmember), 6);
        writer.newLine();
        currentCouncil = council;
        writer.write("------------------------------------------");
        writer.write("------------------------------------------");
        writer.newLine();                   
    } // end of final routine method
} // End of NcoaAcsReport class