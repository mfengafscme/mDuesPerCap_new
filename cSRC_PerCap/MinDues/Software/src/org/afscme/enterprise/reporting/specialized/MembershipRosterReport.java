/** Class Name  : MembershipRosterReport.java
    Date Written: 20031028
    Author      : Kyung A. Callahan
    Description : This is a membership roster report program.
    Note        : 
    Maintenance : Kyung 20031115 get membership dept's person's phone number.
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

// This is a membership roster specialized report.
public class MembershipRosterReport implements ReportHandler 
{
    // define instanace variables
    private String reportType;
    private String sqlQuery = "",
                   state = "",
                   council = "",
                   local = "",
                   subLocal = "",
                   localName = "",
                   localAddress = "",
                   localCity = "",
                   localState = "",
                   localZip = "",
                   localZipPlus = "",
                   memberAddress = "",
                   memberCity = "",
                   memberState = "",
                   memberZip = "",
                   memberZipPlus = "",
                   currentState = "",
                   currentCouncil = "",
                   currentLocal = "",
                   membersFullName = "", 
                   memberEmail = "",
                   memberPrefix = "",
                   memberSuffix = "",
                   memberSSN = "",
                   memberStatus = "",
                   memberGender = "",
                   memberPrimaryLanguage = "",
                   memberPhone = "";
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
            writer.write("An error has occurred in program - MembershipRosterReport.java");
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
            writeColumnSpace(writer, 36);
            writer.write("Membership Roster");
        }
        else {
            writeColumnSpace(writer, 29);
            writer.write("Membership Roster for Retiree");
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
            //"select top 5000 " +
            "select " + 
            "aff_Organizations.aff_statenat_type as state, " + 
            "aff_Organizations.aff_councilRetiree_chap as council, " +
            "aff_Organizations.aff_localSubChapter as local, " + 
            "aff_Organizations.aff_subunit as sub_local, " + 
            "aff_Organizations.aff_abbreviated_nm as local_name, " + 
            "org_address.addr1 as local_address, " +
            "org_address.city as local_city, " +
            "org_address.state as local_state, " +
            "(org_address.zipcode) + (org_address.zip_plus) as local_zip, " +
            "person.last_nm + ', ' + person.first_nm + ' ' as person_full_name, " +
            "person_address.addr1 as person_address, " +
            "person_address.city as person_city, " +
            "person_address.state as person_state, " +
            "person_address.zipcode as person_zip, " +
            "person_address.zip_plus as person_zip_plus, " +
            "'(' + person_phone.area_code + ')' + substring(person_phone.phone_no, 1,3) + '-' + substring(person_phone.phone_no, 4,4) as person_phone, " +
            "person_email.person_email_addr as person_email, " +
            "(select common_codes.com_cd_desc from common_codes where common_codes.com_cd_pk = person.prefix_nm) as person_prefix, " +
            "(select common_codes.com_cd_desc from common_codes where common_codes.com_cd_pk = person.suffix_nm) as person_suffix, " +
            "substring(person.ssn, 1,3) + '-' + substring(person.ssn, 4,2) + '-' + substring(person.ssn, 6,4) as person_ssn, " +
            "(select common_codes.com_cd_cd from common_codes where common_codes.com_cd_pk = person_demographics.gender) as person_gender, " +
            "(select common_codes.com_cd_desc from common_codes where common_codes.com_cd_pk = person_language.language) as person_language, " +
            "(select common_codes.com_cd_cd from common_codes where common_codes.com_cd_pk = aff_members.mbr_status) as person_status, " +
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
            "org_locations, " +
            "org_parent, " +
            "org_address, " +
            "aff_members, " +
            "common_codes, " +
            "person, " +
            "person_address, " +
            "person_phone, " +
            "person_email, " +
            "person_demographics, " +
            "person_language " +
            "where aff_organizations.aff_pk = org_parent.org_pk " +
            "and org_parent.org_pk = org_locations.org_pk " +
            "and location_primary_fg = 1 /* local's primary location */ " +
            "and Org_Address.org_locations_pk = Org_Locations.org_locations_pk " +
            "and aff_organizations.aff_pk = aff_members.aff_pk " +
            "and aff_organizations.aff_status = 17002 /* chartered affiliate */ " +
            "and aff_members.mbr_status = common_codes.com_cd_pk " +
            "and aff_members.person_pk = person.person_pk " +
            "and person.person_pk = person_address.person_pk " +
            "and person_address.addr_prmry_fg = 1 /* member's primary address */ " +
            "and aff_Organizations.aff_type = ? " +
            "and person_phone.person_pk =* person.person_pk " +
            "and person_phone.phone_prmry_fg = 1 /* member's primary phone */ " +
            "and person_phone.dept = 4001 /* membership dept's person phone number */ " +
            "and person_email.person_pk =* person.person_pk " +
            "and person_email.email_type = 71001 /* member's primary email */ " +
            "and person_demographics.person_pk =* person.person_pk " +
            "and person_language.person_pk =* person.person_pk " +
            "and person_language.primary_language_fg = 1 /* member's primary language */ " +
            "and aff_members.person_pk = person.person_pk " +
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
        localName = rs.getString("local_name");
        localAddress = rs.getString("local_address");
        subLocal = rs.getString("sub_local");
        if (subLocal == null) subLocal = "    ";
        if (localAddress.length() > 30)
        {
            localAddress = localAddress.substring(0, 29);
        }
        localCity = rs.getString("local_city");
        localState = rs.getString("local_state");
        localZip = rs.getString("local_zip");
        if (TextUtil.isEmpty(localZip)) localZip = " ";
        membersFullName = rs.getString("person_full_name");
        if (TextUtil.isEmpty(membersFullName)) membersFullName = " ";
        if (membersFullName.length() > 30)
        {
            membersFullName = membersFullName.substring(0, 29);
        }
        memberAddress = rs.getString("person_address");
        if (TextUtil.isEmpty(memberAddress)) memberAddress = " ";
        if (memberAddress.length() > 30)
        {
            memberAddress = memberAddress.substring(0, 29);
        }
        memberCity = rs.getString("person_city");
        if (TextUtil.isEmpty(memberCity)) memberCity = " ";
        memberState = rs.getString("person_state");
        if (TextUtil.isEmpty(memberState)) localZip = " ";
        memberZip = rs.getString("person_zip");
        if (TextUtil.isEmpty(memberZip)) memberZip = " ";
        memberZipPlus = rs.getString("person_zip_plus");
        if (TextUtil.isEmpty(memberZipPlus)) memberZipPlus = " ";
        memberPhone = rs.getString("person_phone");
        if ((TextUtil.isEmpty(memberPhone)) || (memberPhone.equals("( )-")))     
            memberPhone = "             ";
        memberEmail = rs.getString("person_email");
        if (TextUtil.isEmpty(memberEmail)) memberEmail = " ";
        if (memberEmail.length() > 30)
        {
            memberEmail = memberEmail.substring(0, 29);
        }
        memberPrefix = rs.getString("person_prefix");
        if (TextUtil.isEmpty(memberPrefix)) memberPrefix = " ";
        else memberPrefix = memberPrefix + ".";
        memberSuffix = rs.getString("person_suffix");
        if (TextUtil.isEmpty(memberSuffix)) memberSuffix = " ";
        else memberSuffix = memberSuffix + ".";
        memberSSN = rs.getString("person_ssn");
        if (TextUtil.isEmpty(memberSSN)) memberSSN = " ";
        memberStatus = rs.getString("person_status");
        if (TextUtil.isEmpty(memberStatus)) memberStatus = " ";
        memberGender = rs.getString("person_gender");
        if (TextUtil.isEmpty(memberGender)) memberGender = " ";
        memberPrimaryLanguage = rs.getString("person_language");
        if (TextUtil.isEmpty(memberPrimaryLanguage)) memberPrimaryLanguage = " ";
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
        writeValue(writer, localName, 30);
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
        writer.write("Prefix Name                           ");
        writer.write("     Suffix SSN      Status Gender");
        writer.write(" Locl SLocl");
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
        writeValue(writer, memberPrefix, 5);
        writeColumnSpace(writer, 2);
        writeValue(writer, membersFullName, 30);
        writeColumnSpace(writer, 6);
        writeValue(writer, memberSuffix, 4);
        writeColumnSpace(writer, 3);
        writeValue(writer, memberSSN, 11);
        writeColumnSpace(writer, 3);
        writeValue(writer, memberStatus, 1);
        writeColumnSpace(writer, 1);
        writeValue(writer, memberGender, 1);
        writeColumnSpace(writer, 6);
        writeValue(writer, local, 4);
        writeColumnSpace(writer, 2);
        writeValue(writer, subLocal, 4);
        writer.newLine();
        writeColumnSpace(writer, 7);
        writeValue(writer, memberAddress, 30);
        writer.write("      Phone: ");
        writeValue(writer, memberPhone, 13);
        writer.newLine();
        writeColumnSpace(writer, 7);
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
        writer.write("       Email: ");
        writeValue(writer, memberEmail, 40);
        writer.newLine();
        writeColumnSpace(writer, 42);
        writer.write(" P.L.");
        writer.write(" : ");
        writeValue(writer, memberPrimaryLanguage, 25);
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
        writer.write(" Local total member for ");
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
        writer.write("** Council total member for ");
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
        writer.write("******* State total member for ");
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
        writer.write("***Final Total Member: ");
        writeValue(writer, Integer.toString(localFinalTotalmember), 6);
        writer.newLine();
        currentCouncil = council;
        writer.write("------------------------------------------");
        writer.write("------------------------------------------");
        writer.newLine();                   
    } // end of final routine method
} // End of MembershipRoster class