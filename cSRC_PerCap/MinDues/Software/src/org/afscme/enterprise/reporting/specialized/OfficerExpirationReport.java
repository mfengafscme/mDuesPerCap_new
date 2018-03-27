/** Class Name  : OfficerExpirationReport.java
    Date Written: 20031002
    Author      : Kyung A. Callahan
    Description : This is a officer expiration listing report program.
    Note        : 
    Maintenance : Kyung 20031112 fix sort order.
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

// This is a affiliate counts by affiliate type specialized report.
public class OfficerExpirationReport implements ReportHandler 
{
    // The report type to determine what kinds of report (summary or detail).
    private String reportType;
    private String retireeHeadingPrinted = "",
                   currentType = "";
    // Generates a affiliate counts by affiliate type report.
    public int generate(OutputStream stream) throws Exception
    {
        BufferedWriter writer = null;
        Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;;
   	try 
        {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            String type = "",
                   state = "",
                   council = "",
                   local = "",
                   localName = "",
                   localAddress = "",
                   localCity = "",
                   localState = "",
                   localZip = "",
                   localZipPlus = "",
                   termExpirationDate = "",
                   termAvailableDate = "",
                   officerLast = "",
                   officerFirst = "",
                   officerMiddle = "",
                   officerAddress = "",
                   officerCity = "",
                   officerState = "",
                   officerZip = "",
                   officerZipPlus = "",
                   officerTitle = "",
                   currentState = "",
                   currentCouncil = "",
                   currentLocal = "",
                   officerFullName = "", 
                   positionEndDate = "",
                   currentOffice = "",
                   office = "",
                   saveOfficerTitle = "";
            int officerAddrBadFlag = 0,
                officerAddrPrivateFlag = 0,
                localTotalOfficer = 0,
                localFinalTotalOfficer = 0,
                totalNoOffice = 0,
                saveMaximumNoOffice = 0,
                maximumNoOffice = 0,
                i=0;
            int noOfficeVacancy =0;
            Date todaysDate = new Date();
            //The SQL that implements the report
            String sqlQuery = 
                "select  " +
                "Aff_Organizations.aff_type as type,  " +
                "Aff_Organizations.aff_statenat_type as state, " + 
                "Aff_Organizations.aff_councilRetiree_chap as council, " +
                "Aff_Organizations.aff_localSubChapter as local, " + 
                "aff_abbreviated_nm as local_name, " + 
                "org_address.addr1 as local_address, " +
                "org_address.city as local_city, " +
                "org_address.state as local_state, " +
                "(org_address.zipcode) + (org_address.zip_plus) as local_zip, " +
                "convert(char(12), officer_history.pos_expiration_dt, 101) as term_expiration_date, " +
                "convert(char(12), dateadd(year, cast((select common_codes.com_cd_cd from common_codes where common_codes.com_cd_pk = aff_officer_groups.length_of_term) as int), officer_history.pos_expiration_dt), 101) as term_available_date, " +
                "person.last_nm as officer_last, " +
                "person.first_nm as officer_first, " +
                "substring(person.middle_nm,1,1) as officer_middle, " +
                "person_address.addr1 as officer_address, " +
                "person_address.city as officer_city, " +
                "person_address.state as officer_state, " +
                "person_address.zipcode as officer_zip, " + 
                "person_address.zip_plus as officer_zip_plus, " +
                "afscme_offices.afscme_title_desc as officer_title, " +
                "person_address.addr_bad_fg as officer_address_bad_fg, " +
                "person_address.addr_private_fg as officer_address_private_fg, " +
                "convert(char(12), officer_history.pos_end_dt, 101) as enddt, " +
                "aff_officer_groups.max_number_in_office as maximum_no_office, " +
                "aff_officer_groups.afscme_office_pk as office, " +
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
                "common_codes, " +
                "org_locations, " +
                "org_parent, " +
                "org_address, " +
                "afscme_offices " +
                "where Aff_Organizations.aff_pk = officer_history.aff_pk " +
                "and officer_history.person_pk = person.person_pk " +
                "and officer_history.office_group_id = aff_officer_groups.office_group_id " +
                "and afscme_offices.afscme_office_pk = aff_officer_groups.afscme_office_pk " +
                "and officer_history.aff_pk = aff_officer_groups.aff_pk " +
                "and officer_history.pos_end_dt is null " +
                "and person.person_pk = person_address.person_pk " +
                "and person_address.addr_prmry_fg = 1 " +
                "and person_address.addr_type = common_codes.com_cd_pk " +
                "and aff_organizations.aff_pk = org_parent.org_pk " +
                "and org_parent.org_pk = org_locations.org_pk " +
                "and location_primary_fg = 1 " +
                "and Org_Address.org_locations_pk = Org_Locations.org_locations_pk " +
                "order by sort_type, " + 
                "Aff_Organizations.aff_statenat_type, " +
                "sort_council, " + 
                "sort_local, " +
                "sort_sublocal, " +
                "afscme_offices.priority ";
            con = DBUtil.getConnection();
            stmt = con.prepareStatement(sqlQuery);
            rs = stmt.executeQuery();
            int count = 0;
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            String DATE_FORMAT = "MM/dd/yyyy";
            java.text.SimpleDateFormat sdf = 
                new java.text.SimpleDateFormat(DATE_FORMAT);
            printHeading(writer);
            // process rows
            while (rs.next()) 
            {
                count++;
                localFinalTotalOfficer++;                
                totalNoOffice++;
                officerFullName = "";
                type = rs.getString("type");
                state = rs.getString("state");
                council = rs.getString("council");
                local = rs.getString("local");
                localName = rs.getString("local_name");
                localAddress = rs.getString("local_address");
                localCity = rs.getString("local_city");
                localState = rs.getString("local_state");
                localZip = rs.getString("local_zip");
                termExpirationDate = rs.getString("term_expiration_date");
                termAvailableDate = rs.getString("term_available_date");
                officerTitle = rs.getString("officer_title");
                if (officerTitle.length() > 33)
                {
                    officerTitle = officerTitle.substring(0, 32);
                }
                officerLast = rs.getString("officer_last");
                officerFirst = rs.getString("officer_first");                
                officerMiddle = rs.getString("officer_middle");
                officerAddress = rs.getString("officer_address");
                if (TextUtil.isEmpty(officerAddress)) officerAddress = " ";
                if (officerAddress.length() > 30)
                {
                    officerAddress = officerAddress.substring(0, 29);
                }
                officerCity = rs.getString("officer_city");
                officerState = rs.getString("officer_state");
                officerZip = rs.getString("officer_zip");
                officerZipPlus = rs.getString("officer_zip_plus");
                officerAddrBadFlag = rs.getInt("officer_address_bad_fg");
                officerAddrPrivateFlag = rs.getInt("officer_address_private_fg");
                positionEndDate = rs.getString("enddt");
                office = rs.getString("office");
                maximumNoOffice = rs.getInt("maximum_no_office");
		if(!TextUtil.isEmpty(officerFirst)) 
                {
                    officerFullName += (officerFirst + " ");
		}
		if(!TextUtil.isEmpty(officerMiddle)) 
                {
                    officerFullName += (officerMiddle+ " ");
		}
		if(!TextUtil.isEmpty(officerLast)) 
                {
                    officerFullName += (officerLast);
		}
                if (officerFullName.length() > 25)
                {
                    officerFullName = officerFullName.substring(0, 25);
                }
                if (officerFullName.length() > 25)
                {
                    officerFullName = officerFullName.substring(0, 24);
                }
                if (TextUtil.isEmpty(officerFullName)) officerFullName = " ";
                if (count == 1) currentType=type;
                if (!currentType.equals(type)) {
                    currentType=type;
                    printHeading(writer);
                }
                // Check for office break and if vacant office exists,
                // print vacant offices with the vacant title and blank officer
                // name and address.
                if (!currentOffice.equals(office))
                {
                    if (count != 1)
                    {
                        noOfficeVacancy = saveMaximumNoOffice - totalNoOffice;
                        if (noOfficeVacancy > 0)
                        {
                            // print office vacnacy
                            for (i = 1; i <= noOfficeVacancy; i=i+1) 
                            { 
                                writeValue(writer, saveOfficerTitle, 33);
                                writer.newLine();
                                writer.newLine();
                                writer.newLine();
                                writer.newLine();
                            }                      
                        }
                    }
                } // end of else current office = office
                // Check for st, council or local break
                if (!currentState.equals(state) 
                    || !currentCouncil.equals(council) 
                    || !currentLocal.equals(local))
                {
                    // print total officer line
                    if (count != 1)
                    {
                        writer.newLine();
                        writer.write("Total Officer: ");
                        writeValue(writer, Integer.toString(localTotalOfficer), 6);
                        writer.newLine();
                    }
                    writer.newLine();
                    writer.write("----------------------------------------");
                    writer.write("----------------------------------------");
                    writer.write("----");
                    writer.newLine();                   
                    writer.newLine();                   
                    writer.write("State: ");
                    writeValue(writer, state, 2);
                    writeColumnSpace(writer, 4);
                    if (currentType.equals("L")) writer.write("Council: ");
                    else writer.write("Retiree: ");    
                    writeValue(writer, council, 4);
                    if (currentType.equals("L")) writer.write("Local ");
                    else writer.write("Retiree Sub Chapter ");    
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
                    if(localZip == null) 
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
                    writer.write("Title                             " + 
                        "Name                      " +
                        "Expiration  Available");
                    writer.newLine();
                    writer.newLine();
                } // end of state, council or local break
                writeValue(writer, officerTitle, 33);
                writeColumnSpace(writer, 1);
                writeValue(writer, officerFullName, 25);
                // determine if the officer term has expired
                Date currentDate = new Date();
                Date expirationdate = sdf.parse(termExpirationDate);
                if (currentDate.after(expirationdate))
                {
                    writeValue(writer, "*" + termExpirationDate, 10);
                }
                else
                {
                    writeValue(writer, " " + termExpirationDate, 10);
                }
                writeValue(writer, termAvailableDate, 10);
                writer.newLine();
                // if officer address is private or bad, print local address
                if (officerAddrPrivateFlag == 1 || officerAddrBadFlag == 1)
                {
                    writeColumnSpace(writer, 34);
                    writeValue(writer, localAddress, 30);
                    writer.newLine();
                    writeColumnSpace(writer, 34);
                    writeValue(writer, localCity, 15);
                    writeColumnSpace(writer, 1);
                    writeValue(writer, localState, 2);
                    writeColumnSpace(writer, 1);
                    if(localZip == null) 
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
                } // end of if officer address is private or bad
                else
                {
                    writeColumnSpace(writer, 34);
                    writeValue(writer, officerAddress, 30);
                    writer.newLine();
                    writeColumnSpace(writer, 34);
                    writeValue(writer, officerCity, 15);
                    writeColumnSpace(writer, 1);
                    writeValue(writer, officerState, 2);
                    writeColumnSpace(writer, 1);
                    writeValue(writer, officerZip, 5);
                    writeColumnSpace(writer, 1);
                    if(officerZipPlus == null) 
                    {
                        writeValue(writer, "", 4);
                    }
                    else 
                    {
                        writeValue(writer, officerZipPlus, 4);
                    }                 
                }
                writer.newLine();
                writer.newLine();
                if (!currentState.equals(state))
                {
                    currentState=state;
                    currentCouncil=council;
                    currentLocal=local;
                    localTotalOfficer = 0;
                    totalNoOffice = 0;
                    saveOfficerTitle = officerTitle;
                    currentOffice = office;
                    saveMaximumNoOffice = rs.getInt("maximum_no_office");
                }
                if (!currentCouncil.equals(council))
                {
                    currentCouncil=council;
                    currentLocal=local;
                    localTotalOfficer = 0;
                    totalNoOffice = 0;
                    saveOfficerTitle = officerTitle;
                    currentOffice = office;
                    saveMaximumNoOffice = rs.getInt("maximum_no_office");
                }            
                if (!currentLocal.equals(local))
                {
                    currentLocal=local;
                    localTotalOfficer = 0;
                    totalNoOffice = 0;
                    saveOfficerTitle = officerTitle;
                    currentOffice = office;
                    saveMaximumNoOffice = rs.getInt("maximum_no_office");
                }            
                if (!currentOffice.equals(office))
                {
                    totalNoOffice = 0;
                    saveOfficerTitle = officerTitle;
                    currentOffice = office;
                    saveMaximumNoOffice = rs.getInt("maximum_no_office");
                }            
                localTotalOfficer++;
            } // end of while block
            // write office vacancy(ies)
            noOfficeVacancy = maximumNoOffice - totalNoOffice;
            if (noOfficeVacancy > 0)
            {
                for (i = 1; i <= noOfficeVacancy; i=i+1) 
                { 
                    writeValue(writer, saveOfficerTitle, 33);
                    writer.newLine();
                    writer.newLine();
                    writer.newLine();
                    writer.newLine();
                }                      
            }
            // write final officer total
            writer.newLine();
            writer.write("Total Officer: ");
            writeValue(writer, Integer.toString(localTotalOfficer), 6);
            writer.newLine();
            writer.write("----------------------------------------");
            writer.write("----------------------------------------");
            writer.write("----");
            writer.newLine();                   
            writer.newLine();                   
            writer.newLine();                   
            writer.newLine();                   
           writer.write("Final Total Officer: ");
            writeValue(writer, Integer.toString(localFinalTotalOfficer), 6);
            writer.newLine();
	} // end of try block
        catch (SQLException e) 
        {
            writer.write(" ");
            writer.newLine();
            writer.newLine();
            writer.write("An error has occurred in program - OfficerExpirationReport.java");
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
    private void printHeading(BufferedWriter writer) throws IOException
    {
        writer.write(" ");
        writer.newLine();
        writeColumnSpace(writer, 9);
        writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
        writer.newLine();
        writeColumnSpace(writer, 30);
        writer.write("Officer Expiration Listing");
        writer.newLine();
        if (currentType.equals("L")) {
            retireeHeadingPrinted = "Yes";
            writeColumnSpace(writer, 39);
            writer.write("Retiree");
            writer.newLine();
        }
        printCurrentDate(writer);
        writer.newLine();
    }
    private void printCurrentDate(BufferedWriter writer) throws IOException
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "MM/dd/yyyy";
        java.text.SimpleDateFormat sdf = 
            new java.text.SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());          
        writeColumnSpace(writer, 38);
        writer.write(sdf.format(cal.getTime()));
        writer.newLine();
        writer.newLine();
    }
} // End of OfficerExpiration class