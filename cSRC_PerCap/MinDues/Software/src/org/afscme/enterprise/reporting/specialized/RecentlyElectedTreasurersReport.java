/** Class Name  : recentlyElectedTreasurersReport.java
    Date Written: 20031021
    Author      : Kyung A. Callahan
    Description : This is a recently elected treasurers report program.
    Note        : 
    Maintenance : Kyung 20031113 fix sort order and add 
                  dept = 4001 to correlated phone select statement.
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
import java.text.SimpleDateFormat;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import java.util.*;
import java.util.GregorianCalendar;

// This is a recently elected treasurer specialized report.
public class RecentlyElectedTreasurersReport implements ReportHandler 
{
    /**
    * Define variables
    */
    private String reportType;
    private String state = "",
                   council = "",
                   local = "",
                   type = "",
                   currentType = "",
                   currentCouncil = "",
                   currentState = "",
                   currentLocal = "",
                   sqlQuery = "",
                   officerName = "",
                   officerTitle = "",
                   officerAddress = "",
                   officerCity = "",
                   officerState = "",
                   officerZip = "",
                   officerPhone = "",
                   changeDate = "",
                   stringCurrentDate = "",
                   sqlQuery1 = "",
                   termExpirationDate = "";
    private int date = 0, 
                month = 0, 
                year = 0,
                spaceCounter=0,
                count = 0;
    /**
    * Generates a recently elected president report.
    */
    public int generate(OutputStream stream) throws Exception
    {
        BufferedWriter writer = null;
        Connection con = null;
        con = DBUtil.getConnection();
	PreparedStatement stmt = null;
	ResultSet rs = null;
   	try 
        {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            setSqlQuery();
            stmt = con.prepareStatement(sqlQuery);
            rs = stmt.executeQuery();
            while (rs.next()) 
            {
                count++;
                type = rs.getString("type");
                state = rs.getString("state");
                council = rs.getString("council");
                local = rs.getString("local");
                termExpirationDate = rs.getString("term_expiration_date");
                officerName = rs.getString("officer_name");
                officerTitle = rs.getString("officer_title");
                officerAddress = rs.getString("officer_address");
                officerCity = rs.getString("officer_city");
                officerState = rs.getString("officer_state");
                officerZip = rs.getString("officer_zip");
                changeDate = rs.getString("last_modified_date");
                officerPhone = rs.getString("officer_phone");
                if (count == 1) 
                {
                    currentType = type;
                    currentState = state;
                    currentCouncil = council;
                    currentLocal = local;
                    headingRoutine(writer);
                    councilBreak(writer);
                    localBreak(writer);
                }
                /**
                * Check control breaks
                */
                if (!currentType.equals(type)) 
                {
                    currentType = type;
                    headingRoutine(writer);
                }
                if (!currentState.equals(state)) 
                {
                    stateBreak(writer);
                    councilBreak(writer);
                    localBreak(writer);
                }
                else if (!currentCouncil.equals(council)) 
                {
                    councilBreak(writer);
                    localBreak(writer);
                }
                else if (!currentLocal.equals(local)) 
                {
                    localBreak(writer);
                } 
                processDetail(writer);
            } // end of while block   
	} // end of try block
        catch (SQLException e) 
        {
            writer.write(" ");
            writer.newLine();
            writer.newLine();
            writer.write("An error has occurred in program - RecentlyElectedPresidentsReport.java");
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
	}
        return 1;
    } // end of generate method
    /**
    * Write column space method which wirtes the space 
    * given how much space to write
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
    * Write value method which wirtes the given value 
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
    * Get report type method 
    */
    public String getReportType() 
    {
        return reportType;
    }
    /**
    * Set report type method 
    */
    public void setReportType(String reportType) 
    {
        this.reportType = reportType;
    }  
    /**
    * Get file name method 
    */
    public String getFileName() 
    {
        return null;
    }
    /**
    * Report heading method 
    */
    private void headingRoutine(BufferedWriter writer) throws IOException
    {
        writer.write(" ");
        writer.newLine();
        writeColumnSpace(writer, 4);
        writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
        writer.newLine();
        writeColumnSpace(writer, 15);
        writer.write("Recently Elected Treasurers as of ");
        // get one month prior from current date
        GregorianCalendar currentGregorean = new GregorianCalendar();
        currentGregorean.add(currentGregorean.MONTH, -0);
        date = currentGregorean.get(currentGregorean.DATE);
        month = currentGregorean.get(currentGregorean.MONTH);
        year = currentGregorean.get(currentGregorean.YEAR);
        stringCurrentDate = Integer.toString(month)
            + "/" + Integer.toString(date)
            + "/" + Integer.toString(year);
        writeValue(writer, stringCurrentDate, 10);
        writer.newLine();
        if (currentType.equals("S")) {
            writeColumnSpace(writer, 34);
            writer.write("Retiree");
            writer.newLine();
        }    
        writeCurrentDate(writer);
        writer.newLine();
        writer.newLine();
        writer.write("ChangedDate  Address                      ");
        writer.write("  City           State Zip ExpirationDate");
        writer.newLine();
        writer.newLine();
    } // end of heading reoutine
    /**
    * Write current date method 
    */
    private void writeCurrentDate(BufferedWriter writer) throws IOException
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "MM/dd/yyyy";
        java.text.SimpleDateFormat sdf = 
            new java.text.SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());          
        writeColumnSpace(writer, 32);
        writer.write(sdf.format(cal.getTime()));
        writer.newLine();
    } // end of write current date
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
            "person.last_nm + ', ' + person.first_nm + ' ' as officer_name, " +
            "convert(char(12), officer_history.pos_expiration_dt, 101) as term_expiration_date, " +
            "person_address.addr1 as officer_address, " +
            "person_address.city as officer_city, " +
            "person_address.state as officer_state, " +
            "person_address.zipcode as officer_zip, " + 
            "afscme_offices.afscme_office_pk, " +
            "afscme_offices.afscme_title_desc as officer_title, " +
            "convert(char(12), officer_history.lst_mod_dt, 101) as last_modified_date, " +
            "convert(char(12), officer_history.created_dt, 101) as created_date, " +
            "(select '(' + person_phone.area_code + ')' + " + 
            "substring(person_phone.phone_no, 1,3) + '-' + " + 
            "substring(person_phone.phone_no, 4,4) " +
            "from person_phone " +
            "where person_phone.person_pk = person.person_pk " + 
            "and person_phone.phone_prmry_fg = 1 " +
            "and person_phone.dept = 4001) as officer_phone, " +
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
            "common_codes, " +
            "afscme_offices " +
            "where aff_organizations.aff_pk = officer_history.aff_pk " +
            "and officer_history.person_pk = person.person_pk " +
            "and officer_history.pos_end_dt is null /* current officer position */ " +
            "and person.person_pk = person_address.person_pk " +
            "and person_address.addr_prmry_fg = 1 /* member's primary address */ " +
            "and officer_history.afscme_office_pk = 29 /* officer who hods the treasurer's position */ " +
            "and (officer_history.lst_mod_dt > DATEADD(month, -1, getdate()) " +
            "or officer_history.created_dt > DATEADD(month, -1, getdate())) " +
            "and afscme_offices.afscme_office_pk = officer_history.afscme_office_pk " +
            "and person_address.addr_type = common_codes.com_cd_pk " +
            "order by sort_type, " + 
            "Aff_Organizations.aff_statenat_type, " +
            "sort_council, " + 
            "sort_local, " +
            "sort_sublocal, " +
            "officer_name ";
    } // end of set sql query
   /**
     * Local break method which counts locals with expired officers and 
     * clears out local control fields
     */
    private void localBreak(BufferedWriter writer) 
        throws IOException, SQLException
    {
        writeColumnSpace(writer, 7);
        if (currentType.equals("L")) writer.write("Local ");
        else writer.write("Retiree Sub Chapter ");    
        writeValue(writer, local, 4);
        writer.newLine();
        writer.newLine();
        currentLocal=local;
    } // end of local break
    /**
     * Council break method which prints the council total line, rolls the 
     * council totals into state total, and zeros out the council totals.
     */
    private void councilBreak(BufferedWriter writer) 
        throws IOException, SQLException
    {
        writeValue(writer, state, 2);
        writeColumnSpace(writer, 2);
        writeValue(writer, council, 4);
        writer.newLine();
        writer.newLine();
        currentLocal = local;
        currentCouncil = council;
    } // end of councilBreak method
    /**
     * State break method which prints the state total line, 
     * rolls the state totals into final totals and 
     * clears out state totals.
     */
    private void stateBreak(BufferedWriter writer) 
        throws IOException, SQLException
    {
        //writeValue(writer, currentState, 2);
        //writeColumnSpace(writer, 2);
        //writeValue(writer, currentCouncil, 4);
        writer.newLine();
        currentLocal = local;
        currentCouncil = council;
        currentState = state;
    }
    /**
     * Process detail method 
     */
    private void processDetail(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        if (officerName.length() > 25)
        {
            officerName = officerName.substring(0, 24);
        }
        writeValue(writer, officerName, 25);
        if (officerPhone == null || officerPhone.equals("( )-"))     
            officerPhone = "             ";
        writeColumnSpace(writer, 1);
        writeValue(writer, officerPhone, 13);
        writer.newLine();
        writeValue(writer, changeDate, 10);
        writeColumnSpace(writer, 1);
        if (officerAddress.length() > 30)
        {
            officerAddress = officerAddress.substring(0, 29);
        }
        writeValue(writer, officerAddress, 30);
        if (officerCity.length() > 15)
        {
            officerCity = officerCity.substring(0, 14);
        }
        writeColumnSpace(writer, 1);
        writeValue(writer, officerCity, 15);
        writeColumnSpace(writer, 1);
        writeValue(writer, officerState, 2);
        writeColumnSpace(writer, 1);
        if (officerZip == null)     
            officerZip = "     ";
        writeValue(writer, officerZip, 5);
        writeColumnSpace(writer, 1);
        writeValue(writer, termExpirationDate, 10);
        writer.newLine();
        writer.newLine();
    } // end of process detail
} // end of recentlyElectedTreasurersReport class