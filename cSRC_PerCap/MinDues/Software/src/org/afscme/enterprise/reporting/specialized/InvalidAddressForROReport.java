/** Class Name  : InvalidAddressForROReport.java
    Date Written: 20031029
    Author      : Kyung A. Callahan
    Description : This is an invalid address for reporting officer report program.
    Note        : 
    Maintenance : Kyung 20031110 
                  1. If select is returning a null, 
                     set all possible column values to a blank. 
                  2. Correct the sort statement.
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

// This is a affiliate counts by affiliate status specialized report.
public class InvalidAddressForROReport implements ReportHandler 
{
    // define instanace variables
    private String reportType;
    private String sqlQuery = "",
                   state = "",
                   council = "",
                   local = "",
                   localName = "",
                   localAddress = "",
                   localCity = "",
                   localState = "",
                   localZip = "",
                   localZipPlus = "",
                   localPhone = "",
                   memberAddress = "",
                   memberCity = "",
                   memberState = "",
                   memberZip = "",
                   memberZipPlus = "",
                   currentState = "",
                   currentCouncil = "",
                   currentLocal = "",
                   membersFullName = "", 
                   memberExpirationDate = "",
                   memberEmail = "",
                   memberPhone = "";
    private     int localTotalmember = 0,
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
            if (reportType.equals("L")) stmt.setString(1,reportType);
            else stmt.setString(1, "%");
            rs = stmt.executeQuery();
            // process results set
            while (rs.next()) 
            {
                getColumnVariable(rs);
                // Check for st, council or local break
                if ((!currentState.equals(state)) 
                    || (!currentCouncil.equals(council)) 
                    || (!currentLocal.equals(local)))
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
            writer.write("An error has occurred in program - InvalidAddressForROReport.");
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
    public String getReportType() 
    {
        return reportType;
    }
    public void setReportType(String reportType) 
    {
        this.reportType = reportType;
    }  
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
            writeColumnSpace(writer, 25);
            writer.write("Invalid Address for Reporting Officer");
        }
        else {
            writeColumnSpace(writer, 25);
            writer.write("Invalid Address for Reporting Officer");
            writer.newLine();
            writeColumnSpace(writer, 40);
            writer.write("Retiree");
        }
        writer.newLine();
        printCurrentDate(writer);
        writer.newLine();
    }
    /**
    * Getcurrent date
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
            "Aff_Organizations.aff_type as type, " + 
            "Aff_Organizations.aff_statenat_type as state, " + 
            "Aff_Organizations.aff_councilRetiree_chap as council, " +
            "Aff_Organizations.aff_localSubChapter as local, " + 
            "aff_organizations.aff_subunit as sub_local, " +
            "aff_abbreviated_nm as local_name, " + 
            "org_address.addr1 as local_address, " +
            "org_address.city as local_city, " +
            "org_address.state as local_state, " +
            "(org_address.zipcode) + (org_address.zip_plus) as local_zip, " +
            "'(' + org_phone.area_code + ')' + " + 
            "substring(org_phone.phone_no, 1,3) + '-' + " + 
            "substring(org_phone.phone_no, 4,4) as local_phone, " +
            "convert(char(12), officer_history.pos_expiration_dt, 101) as term_expiration_date, " +
            "person.last_nm + ', ' + person.first_nm + ' ' as person_full_name, " +
            "person_address.addr1 as person_address, " +
            "person_address.city as person_city, " +
            "person_address.state as person_state, " +
            "person_address.zipcode as person_zip, " + 
            "person_address.zip_plus as person_zip_plus, " +
            "'(' + person_phone.area_code + ')' + " + 
            "substring(person_phone.phone_no, 1,3) + '-' + " + 
            "substring(person_phone.phone_no, 4,4) as person_phone, " +
            "person_email.person_email_addr as person_email, " +
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
            "officer_history, " +
            "person, " +
            "person_address, " +
            "aff_officer_groups, " +
            "org_locations, " +
            "org_parent, " +
            "org_address, " +
	    "org_phone, " +
            "afscme_offices, " +
            "person_phone, " +
            "person_email " +
            "where Aff_Organizations.aff_pk = officer_history.aff_pk " +
            "and org_parent.org_pk = org_locations.org_pk " +
            "and location_primary_fg = 1 /* local's primary location */ " +
            "and Org_Address.org_locations_pk = Org_Locations.org_locations_pk " +
            "and org_phone.org_locations_pk =* Org_Locations.org_locations_pk " +
            "and org_phone.org_phone_type = 73001 /* locals office phone number */ " +
            "and officer_history.person_pk = person.person_pk " +
            "and officer_history.office_group_id = aff_officer_groups.office_group_id " +
            "and afscme_offices.afscme_office_pk = aff_officer_groups.afscme_office_pk " +
            "and officer_history.aff_pk = aff_officer_groups.aff_pk " +
            "and officer_history.pos_end_dt is null /* active officer */ " +
            "and person.person_pk = person_address.person_pk " +
            "and person_address.addr_prmry_fg = 1 /* officer's primary address */ " +
            "and aff_organizations.aff_pk = org_parent.org_pk " +
            "and org_parent.org_pk = org_locations.org_pk " +
            "and aff_organizations.aff_status = 17002 /* chartered affiliate */ " +
            "and person_address.addr_bad_fg = 1 " + 
            "and officer_history.afscme_office_pk = 32 " +
            "and person_phone.person_pk =* person.person_pk " +
            "and person_phone.phone_prmry_fg = 1 /* member's primary phone */ " +
	 "and person_phone.dept = 4001 /* membership dept's person's phone number */ " +
            "and person_email.person_pk =* person.person_pk " +
            "and person_email.email_type = 71001 /* member's primary email */ " +
            "and Aff_Organizations.aff_type = ? " +
            "order by sort_type, " + 
            "Aff_Organizations.aff_statenat_type, " +
            "sort_council, " + 
            "sort_local, " +
            "sort_sublocal, " +
            "person_full_name ";
    } 
    // end of set sql query
    /**
    * Get columns from result set
    */
    private void getColumnVariable(ResultSet rs) 
        throws IOException, SQLException
    {
        count++;
        state = rs.getString("state");
        council = rs.getString("council");
        local = rs.getString("local");
        localName = rs.getString("local_name");
        localAddress = rs.getString("local_address");
        if (localAddress == null) localAddress = " ";
        if (localAddress.length() > 30)
        {
            localAddress = localAddress.substring(0, 29);
        }
        localCity = rs.getString("local_city");
        if (localCity == null) localCity = " ";
        localState = rs.getString("local_state");
        if (localState == null) localState = " ";
        localZip = rs.getString("local_zip");
        if (localZip == null) localZip = " ";
        localPhone = rs.getString("local_phone");
        if (localPhone == null) localPhone = " ";
        membersFullName = rs.getString("person_full_name");
        if (membersFullName.length() > 30)
        {
            membersFullName = membersFullName.substring(0, 29);
        }
        memberAddress = rs.getString("person_address");
        if (memberAddress == null) memberAddress = " ";
        if (memberAddress.length() > 30)
        {
            memberAddress = memberAddress.substring(0, 29);
        }
        memberCity = rs.getString("person_city");
        if (memberCity == null) memberCity = " ";
        memberState = rs.getString("person_state");
        if (memberState == null) memberState = " ";
        memberZip = rs.getString("person_zip");
        if (memberZip == null) memberZip = " ";
        memberZipPlus = rs.getString("person_zip_plus");
        if (memberZipPlus == null) memberZipPlus = " ";
        memberPhone = rs.getString("person_phone");
        if ((memberPhone == null) || (memberPhone.equals("( )-")))     
            memberPhone = "             ";
        memberExpirationDate = rs.getString("term_expiration_date");
        if (memberExpirationDate == null) memberExpirationDate = " ";
        memberEmail = rs.getString("person_email");
        if (memberEmail == null) memberEmail = " ";
    } // end of set sql query
    /**
    * State, council, and local break routine
    */
    private void stateCouncilLocalBreak(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        // print total member line
        if (count != 1)
        {
            writer.newLine();
            writer.write("Total RO with invalid address: ");
            writeValue(writer, Integer.toString(localTotalmember), 6);
            writer.newLine();
        }
        writer.newLine();
        writer.write("----------------------------------------");
        writer.write("----------------------------------------");
        writer.write("----");
        writer.newLine();                   
        writer.newLine();                   
        writeValue(writer, state, 2);
        if ((council == null) || (council.equals("    "))) writeColumnSpace(writer, 4);
        else writeValue(writer, council, 4);
        writeValue(writer, local, 4);
        writer.newLine();                
        writer.newLine();                
        writeValue(writer, localName, 30);
        writeColumnSpace(writer, 35);
        writeValue(writer, localPhone, 13);
        writer.newLine();
        writeValue(writer, localAddress, 30);
        writer.newLine();
        writeValue(writer, localCity, 15);
        writeColumnSpace(writer, 1);
        writeValue(writer, localState, 2);
        writeColumnSpace(writer, 1);
        if (localZip == null) 
        {
            writeValue(writer, "", 5);
        }
        else 
        {
            writeValue(writer, localZip, 5);
        }                     
        writeColumnSpace(writer, 1);
        if(localZipPlus == null) 
        {
            writeValue(writer, "", 4);
        }
        else 
        {
            writeValue(writer, localZipPlus, 4);
        } 
        writer.newLine();
        writer.newLine();
        writer.newLine();
        writer.newLine();
        writer.write("Name                              " + 
            "Address                   " +
            "     Phone/Exp. Date ");
        writer.newLine();
        writer.newLine();
    } // end of state, council, and local break method
    private void finalRoutine(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        // write final member total
        writer.newLine();
        writer.write("Total RO with invalid address: ");
        writeValue(writer, Integer.toString(localTotalmember), 6);
        writer.newLine();
        writer.write("----------------------------------------");
        writer.write("----------------------------------------");
        writer.write("----");
        writer.newLine();                   
        writer.newLine();                   
        writer.newLine();                   
        writer.newLine();                   
        writer.write("Final Total RO with invalid address: ");
        writeValue(writer, Integer.toString(localFinalTotalmember), 6);
            writer.newLine();
    } // end of process detail
    /**
    * Process detail
    */
    private void processDetail(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        localFinalTotalmember++;                
        writeValue(writer, membersFullName, 30);
        writeColumnSpace(writer, 4);
        writeValue(writer, memberAddress, 30);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberPhone, 13);
        writer.newLine();
        writeColumnSpace(writer, 34);
        writeValue(writer, memberCity, 15);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberState, 2);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberZip, 5);
        writeColumnSpace(writer, 1);
        if(memberZipPlus == null) 
        {
            writeValue(writer, "", 4);
        }
        else 
        {
            writeValue(writer, memberZipPlus, 4);
        }                 
        writeColumnSpace(writer, 2);
        writeValue(writer, memberExpirationDate, 10);
        writer.newLine();
        writeColumnSpace(writer, 34);
        writeValue(writer, memberEmail, 5);
        writer.newLine();
        writer.newLine();
        if (!currentState.equals(state))
        {
            currentState=state;
            currentCouncil=council;
            currentLocal=local;
            localTotalmember = 0;
        }
        if (!currentCouncil.equals(council))
        {
            currentCouncil=council;
            currentLocal=local;
            localTotalmember = 0;
        }            
        if (!currentLocal.equals(local))
        {
            currentLocal=local;
            localTotalmember = 0;
        }            
        localTotalmember++;
    } // end of process detail
} // End of NonMailableRoster class