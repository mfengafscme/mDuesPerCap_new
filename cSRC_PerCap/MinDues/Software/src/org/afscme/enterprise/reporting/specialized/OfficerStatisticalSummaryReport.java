/** Class Name  : officerStatisticalSummaryReport.java
    Date Written: 20031008
    Author      : Kyung A. Callahan
    Description : This is a officer statistical summary program.
    Note        : 
    Maintenance : Kyung 20031112 Fix affiliated local and non-affiliated local count.
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

// This is an officer statistical summary specialized report.
public class OfficerStatisticalSummaryReport implements ReportHandler 
{
    private String reportType = "";
    private int spaceCounter=0,
                /**
                 * define local level count instance variables 
                 */
                localLocalCount=0,
                localOfficerCount=0,
                localStewardCount=0,
                localRoCount=0,
                localExpiredOfficerCount=0,
                localTemporaryOfficerCount=0,
                localExpiredPresidentCount=0,
                localExpiredTreasurerCount=0,
                localExpiredSecretaryCount=0,
                localDelinquentOneCount=0,
                localDelinquentTwoCount=0,
                localDelinquentThreeCount=0,
                localLocalWithExpiredOfficersCount=0,
                localLocalsWithoutOfficersCount=0,
                localAffiliatedLocalsCount=0,
                localUnaffiliatedLocalsCount=0,
                /**
                 * define council level count instance varilables 
                 */
                councilLocalCount=0,
                councilOfficerCount=0,
                councilStewardCount=0,
                councilRoCount=0,
                councilTemporaryOfficerCount=0,
                councilExpiredOfficerCount=0,
                councilExpiredPresidentCount=0,
                councilExpiredTreasurerCount=0,
                councilExpiredSecretaryCount=0,
                councilDelinquentOneCount=0,
                councilDelinquentTwoCount=0,
                councilDelinquentThreeCount=0,
                councilLocalWithExpiredOfficersCount=0,
                councilLocalsWithoutOfficersCount=0,
                councilAffiliatedLocalsCount=0,
                councilUnaffiliatedLocalsCount=0,
                /**
                 * define state level count instance varilables 
                 */
                stateLocalCount=0,
                stateOfficerCount=0,
                stateStewardCount=0,
                stateRoCount=0,
                stateExpiredOfficerCount=0,
                stateTemporaryOfficerCount=0,
                stateExpiredPresidentCount=0,
                stateExpiredTreasurerCount=0,
                stateExpiredSecretaryCount=0,
                stateDelinquentOneCount=0,
                stateDelinquentTwoCount=0,
                stateDelinquentThreeCount=0,
                stateLocalWithExpiredOfficersCount=0,
                stateLocalsWithoutOfficersCount=0,
                stateAffiliatedLocalsCount=0,
                stateUnaffiliatedLocalsCount=0,
                /**
                 * define final level count instance varilables 
                 */
                grandLocalCount=0,
                grandOfficerCount=0,
                grandStewardCount=0,
                grandRoCount=0,
                grandExpiredOfficerCount=0,
                grandTemporaryOfficerCount=0,
                grandExpiredPresidentCount=0,
                grandExpiredTreasurerCount=0,
                grandExpiredSecretaryCount=0,
                grandDelinquentOneCount=0,
                grandDelinquentTwoCount=0,
                grandDelinquentThreeCount=0,
                grandLocalWithExpiredOfficersCount=0,
                grandLocalsWithoutOfficersCount=0,
                grandAffiliatedLocalsCount=0,
                grandUnaffiliatedLocalsCount=0,                
                afscmeOfficePk = 0,
                memberStatus=0,
                noSpacePrior=4,
                date = 0, 
                month = 0, 
                year = 0,
                count = 0,
                workRoCount = 0,
                saveAfscmeOfficePk = 0;
    private String state = "",
                   council = "",
                   local = "",
                   type = "",
                   currentCouncil = "",
                   currentState = "",
                   currentLocal = "",
                   sqlQuery = "",
                   stringTwoMonthsPrior = "",
                   stringFourMonthsPrior = "",
                   termExpirationDate = "";
    private Date expirationDate = new Date(),
                 twoMonthsPrior = new Date(),
                 fourMonthsPrior = new Date(),
                 currentDate = new Date();
    private Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    private String DATE_FORMAT = "MM/dd/yyyy";
    private java.text.SimpleDateFormat sdf = 
        new java.text.SimpleDateFormat(DATE_FORMAT);
    /**
    * Generates a officer statistical summary report.
    */
    public int generate(OutputStream stream) throws Exception
    {
        BufferedWriter writer = null;
        Connection con = null;
        con = DBUtil.getConnection();
	//PreparedStatement stmt = null;
	ResultSet rs = null;
        // get 2 months prior to current date 
        GregorianCalendar currentGregorean = new GregorianCalendar();
        currentGregorean.add(currentGregorean.MONTH, -1);
        date = currentGregorean.get(currentGregorean.DATE);
        month = currentGregorean.get(currentGregorean.MONTH);
        year = currentGregorean.get(currentGregorean.YEAR);
        stringTwoMonthsPrior = Integer.toString(month)
            + "/" + Integer.toString(date)
            + "/" + Integer.toString(year);
        twoMonthsPrior = sdf.parse(stringTwoMonthsPrior);
        // get 4 months prior to current date
        currentGregorean.add(currentGregorean.MONTH, -2);
        date = currentGregorean.get(currentGregorean.DATE);
        month = currentGregorean.get(currentGregorean.MONTH);
        year = currentGregorean.get(currentGregorean.YEAR);
        stringFourMonthsPrior = Integer.toString(month)
            + "/" + Integer.toString(date)
            + "/" + Integer.toString(year);
        fourMonthsPrior = sdf.parse(stringFourMonthsPrior);
  	try 
        {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            setSqlQuery();
            PreparedStatement stmt = con.prepareStatement(sqlQuery);
            stmt.setString(1, reportType);
            rs = stmt.executeQuery();
            printHeading(writer);
            while (rs.next()) 
            {
                count++;
                state = rs.getString("state");
                council = rs.getString("council");
                local = rs.getString("local");
                afscmeOfficePk = rs.getInt("afscme_office_pk");
                memberStatus = rs.getInt("member_status");
                termExpirationDate = rs.getString("term_expiration_date");
                /**
                * Initialize the control fields, if first time through while loop
                */
                if (count == 1) 
                {
                    currentState = state;
                    currentCouncil = council;
                    currentLocal = local;
                }
                /**
                * Check control breaks
                */
                if (!currentState.equals(state)) 
                {
                    localBreak(writer);
                    councilBreak(writer);
                    stateBreak(writer);
                }
                else if (!currentCouncil.equals(council)) 
                {
                    localBreak(writer);
                    councilBreak(writer);
                }
                else if (!currentLocal.equals(local)) 
                {
                    localBreak(writer);
                } 
                processDetail(writer);
            } // end of while block   
            finalRoutine(writer);            
            DBUtil.cleanup(con, stmt, rs);
	} // end of try block
        catch (SQLException e) 
        {
            writer.write(" ");
            writer.newLine();
            writer.newLine();
            writer.write("An error has occurred in program - OfficerStatisticalSunmmaryReport.java");
            writer.newLine();
            writer.write("Error message: ");
            writer.write(e.getMessage());
            writer.newLine();
        }         
        finally 
        {
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
    private void printHeading(BufferedWriter writer) throws IOException
    {
        writer.write(" ");
        writer.newLine();
        writeColumnSpace(writer, 34);
        writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
        writer.newLine();
        writeColumnSpace(writer, 50);
        if (reportType.equals("L"))
            writer.write("Regular Officer Statistical Summary");
        else
            writer.write("Retiree Officer Statistical Summary");
        writer.newLine();
        writeCurrentDate(writer);
        writer.newLine();
        writer.newLine();
        writer.write("St Concil Local  Number  Number    Number   ");
        writer.write("Locals    Number   Number --Expired---  ");
        writer.write("Mnths Expired LocalsW/ LocalsW/O  Affil. Unaffil.");
        writer.newLine();
        writer.write("               OfLocals OfOfcrs  ofStewds ");
        writer.write("w/RoOnly Temp Ofcrs Expired PR   TR   SC  ");
        writer.write("1-2  3-4   5+ ExpOfcrs Officers   Locals   Locals");
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
        writeColumnSpace(writer, 62);
        writer.write(sdf.format(cal.getTime()));
        writer.newLine();
    } // end of write current date
    /**
    * Right justify count method which right justify the count given the count, 
    * no of space in front of the count, and writer.
    */
    public void rightJustifyCount(String count, int noSpacePrior, BufferedWriter writer)
        throws IOException
    {
        int spaceCounter=0;
        spaceCounter = count.length();
        spaceCounter = 5 - spaceCounter;
        spaceCounter = spaceCounter + noSpacePrior;
        this.spaceCounter = spaceCounter;
        writeColumnSpace(writer, spaceCounter);
        writeValue(writer, count, count.length());
    } // end of right justify count
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
            "Aff_Organizations.aff_subunit as sub_local, " +
            "convert(char(12), officer_history.pos_expiration_dt, 101) as term_expiration_date, " +
            "officer_history.afscme_office_pk, " +
            "(select aff_members.mbr_status from aff_members " + 
            "where aff_members.aff_pk = aff_organizations.aff_pk " + 
            "and aff_members.person_pk = officer_history.person_pk) " + 
            "as member_status, " +
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
            "from aff_organizations full outer join officer_history " +
            "on aff_organizations.aff_pk = officer_history.aff_pk " +
            "where officer_history.pos_end_dt is null /* current officer */ " +
            "and Aff_Organizations.aff_status = 17002 /* chartered */ " +
            "and Aff_Organizations.aff_type = ? /* local or subchapter */ " +
            "order by sort_type, " + 
            "Aff_Organizations.aff_statenat_type, " +
             "sort_council, " + 
             "sort_local, " +
             "sort_sublocal ";
    } // end of set sql query
    /**
     * Increment local level instance variables
     */
    private void countForLocal() 
        throws IOException, SQLException
    {
        // count officer term expiration by months
        if (currentDate.after(expirationDate)) 
        {
            localExpiredOfficerCount++;
            if (twoMonthsPrior.before(expirationDate))
                localDelinquentOneCount++;
            else if (fourMonthsPrior.before(expirationDate))
                localDelinquentTwoCount++;
            else localDelinquentThreeCount++;
        }
        if (currentDate.after(expirationDate) && afscmeOfficePk == 9)
        {
            localExpiredPresidentCount++;
        }
        if (currentDate.after(expirationDate) && afscmeOfficePk == 29)
        {
            localExpiredTreasurerCount++;
        }
        if (currentDate.after(expirationDate) && afscmeOfficePk == 28)
        {
            localExpiredSecretaryCount++;
        }
        if (afscmeOfficePk == 46) localStewardCount++;
        if (memberStatus == 31003) localTemporaryOfficerCount++;
        if (afscmeOfficePk == 32) localRoCount++;
        localOfficerCount++;
    } // end of count for local method
    /**
     * Local break method which counts locals with expired officers and
     * clears out local control fields
     */
    private void localBreak(BufferedWriter writer) 
        throws IOException, SQLException
    {
        // count affiliated and unaffiliated locals
        localAffiliatedLocalsCount = 0;
        localUnaffiliatedLocalsCount = 0;
        if (TextUtil.isEmpty(currentCouncil)) localUnaffiliatedLocalsCount = 1;
        else localAffiliatedLocalsCount = 1;
        // count locals with reporting officer only
        if (localRoCount == 1 && localOfficerCount == 1) localRoCount = 1;
        else localRoCount = 0;
        // count locals with expired officers
        if (localExpiredOfficerCount > 0) localLocalWithExpiredOfficersCount = 1;
        else localLocalWithExpiredOfficersCount = 0;
        // count locals without officers
        if (localOfficerCount == 0) localLocalsWithoutOfficersCount = 1;
        else localLocalsWithoutOfficersCount = 0;
       // set number of local count to 1, if sql query returned any rows
        if (count > 0)
        {
            localLocalCount=1;
        }
        else
        {
            localLocalCount=0;
            localOfficerCount=0;
            localStewardCount=0;
            localRoCount=0;
            localExpiredOfficerCount=0;
            localTemporaryOfficerCount=0;
            localExpiredPresidentCount=0;
            localExpiredTreasurerCount=0;
            localExpiredSecretaryCount=0;
            localDelinquentOneCount=0;
            localDelinquentTwoCount=0;
            localDelinquentThreeCount=0;
            localLocalWithExpiredOfficersCount=0;
            localLocalsWithoutOfficersCount=0;
            localAffiliatedLocalsCount=0;
            localUnaffiliatedLocalsCount=0;
        }
        /*
        writeValue(writer, currentState, 2);
        writeColumnSpace(writer, 1);
        writeValue(writer, currentCouncil, 4);
        writeColumnSpace(writer, 2);
        writeValue(writer, currentLocal, 4);
        writeColumnSpace(writer, 1);
        noSpacePrior=3;
        rightJustifyCount(Integer.toString(localLocalCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(localOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localStewardCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localRoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localTemporaryOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localExpiredOfficerCount), noSpacePrior, writer);
        noSpacePrior=0;
        rightJustifyCount(Integer.toString(localExpiredPresidentCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localExpiredTreasurerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localExpiredSecretaryCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localDelinquentOneCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localDelinquentTwoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localDelinquentThreeCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(localLocalWithExpiredOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localLocalsWithoutOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localAffiliatedLocalsCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(localUnaffiliatedLocalsCount), noSpacePrior, writer);
        writer.newLine();
        writer.newLine();
        */
        // roll the local totals to council totals
        councilLocalCount += localLocalCount;
        councilOfficerCount += localOfficerCount;
        councilStewardCount += localStewardCount;
        councilRoCount += localRoCount;
        councilTemporaryOfficerCount+= localTemporaryOfficerCount;
        councilExpiredOfficerCount+= localExpiredOfficerCount;
        councilExpiredPresidentCount+= localExpiredPresidentCount;
        councilExpiredTreasurerCount+= localExpiredTreasurerCount;
        councilExpiredSecretaryCount+= localExpiredSecretaryCount;
        councilDelinquentOneCount += localDelinquentOneCount;
        councilDelinquentTwoCount += localDelinquentTwoCount;
        councilDelinquentThreeCount += localDelinquentThreeCount;
        councilLocalWithExpiredOfficersCount += localLocalWithExpiredOfficersCount;
        councilLocalsWithoutOfficersCount += localLocalsWithoutOfficersCount;
        councilAffiliatedLocalsCount += localAffiliatedLocalsCount;
        councilUnaffiliatedLocalsCount += localUnaffiliatedLocalsCount;
        // zero out the local totals
        localLocalCount = 0;
        localOfficerCount= 0;
        localStewardCount=0;
        localRoCount = 0;
        localTemporaryOfficerCount=0;
        localExpiredOfficerCount=0;
        localExpiredPresidentCount=0;
        localExpiredTreasurerCount=0;
        localExpiredSecretaryCount=0;
        localDelinquentOneCount=0;
        localDelinquentTwoCount=0;
        localDelinquentThreeCount=0;
        localLocalWithExpiredOfficersCount=0;
        localLocalsWithoutOfficersCount=0;
        localAffiliatedLocalsCount=0;
        localUnaffiliatedLocalsCount=0;
        // count locals with RO only xxxxxxx
        if (saveAfscmeOfficePk == 32 && workRoCount == 1) 
        {
            councilRoCount++;
            saveAfscmeOfficePk = 0;
            workRoCount = 0;
        }
        currentLocal=local;
    } // end of local break
    /**
     * Council break method which prints the council total line, rolls the 
     * council totals into state total, and zeros out the council totals.
     */
    private void councilBreak(BufferedWriter writer) 
        throws IOException, SQLException
    {
        // write council totals
        writeValue(writer, currentState, 2);
        writeColumnSpace(writer, 1);
        writeValue(writer, currentCouncil, 4);
        writeColumnSpace(writer, 7);
        noSpacePrior=3;
        rightJustifyCount(Integer.toString(councilLocalCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(councilOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilStewardCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilRoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilTemporaryOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilExpiredOfficerCount), noSpacePrior, writer);
        noSpacePrior=0;
        rightJustifyCount(Integer.toString(councilExpiredPresidentCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilExpiredTreasurerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilExpiredSecretaryCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilDelinquentOneCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilDelinquentTwoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilDelinquentThreeCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(councilLocalWithExpiredOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilLocalsWithoutOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilAffiliatedLocalsCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(councilUnaffiliatedLocalsCount), noSpacePrior, writer);
        writer.newLine();
        writer.newLine();
        // roll the totals to state total
        stateLocalCount += councilLocalCount;
        stateOfficerCount += councilOfficerCount;
        stateStewardCount += councilStewardCount;
        stateRoCount += councilRoCount;
        stateTemporaryOfficerCount+= councilTemporaryOfficerCount;
        stateExpiredOfficerCount+= councilExpiredOfficerCount;
        stateExpiredPresidentCount+= councilExpiredPresidentCount;
        stateExpiredTreasurerCount+= councilExpiredTreasurerCount;
        stateExpiredSecretaryCount+= councilExpiredSecretaryCount;
        stateDelinquentOneCount += councilDelinquentOneCount;
        stateDelinquentTwoCount += councilDelinquentTwoCount;
        stateDelinquentThreeCount += councilDelinquentThreeCount;
        stateLocalWithExpiredOfficersCount += councilLocalWithExpiredOfficersCount;
        stateLocalsWithoutOfficersCount += councilLocalsWithoutOfficersCount;
        stateAffiliatedLocalsCount += councilAffiliatedLocalsCount;
        stateUnaffiliatedLocalsCount += councilUnaffiliatedLocalsCount;
        // zero out the council totals
        councilLocalCount = 0;
        councilOfficerCount= 0;
        councilStewardCount=0;
        councilRoCount = 0;
        councilTemporaryOfficerCount=0;                    
        councilExpiredOfficerCount=0;
        councilExpiredPresidentCount=0;
        councilExpiredTreasurerCount=0;
        councilExpiredSecretaryCount=0;
        councilDelinquentOneCount=0;
        councilDelinquentTwoCount=0;
        councilDelinquentThreeCount=0;
        councilLocalWithExpiredOfficersCount=0;
        councilLocalsWithoutOfficersCount=0;
        councilAffiliatedLocalsCount=0;
        councilUnaffiliatedLocalsCount=0;
        /**
         * Update council and local control fields 
         */
        currentLocal=local;
        currentCouncil=council;
    } // end of councilBreak method
    /**
     * State break method which prints the state total line, 
     * rolls the state totals into final totals and 
     * clears out state totals.
     */
    private void stateBreak(BufferedWriter writer) 
        throws IOException, SQLException
    {
        // write state totals
        writeValue(writer, currentState, 2);
        writeColumnSpace(writer, 12);
        noSpacePrior=3;
        rightJustifyCount(Integer.toString(stateLocalCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(stateOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateStewardCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateRoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateTemporaryOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateExpiredOfficerCount), noSpacePrior, writer);
        noSpacePrior=0;
        rightJustifyCount(Integer.toString(stateExpiredPresidentCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateExpiredTreasurerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateExpiredSecretaryCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateDelinquentOneCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateDelinquentTwoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateDelinquentThreeCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(stateLocalWithExpiredOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateLocalsWithoutOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateAffiliatedLocalsCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(stateUnaffiliatedLocalsCount), noSpacePrior, writer);
        writer.newLine();
        writer.newLine();
        // roll up the state totals into final totals
        grandLocalCount += stateLocalCount;
        grandOfficerCount += stateOfficerCount;
        grandStewardCount += stateStewardCount;
        grandRoCount += stateRoCount;
        grandTemporaryOfficerCount+= stateTemporaryOfficerCount;
        grandExpiredOfficerCount+= stateExpiredOfficerCount;
        grandExpiredPresidentCount+= stateExpiredPresidentCount;
        grandExpiredTreasurerCount+= stateExpiredTreasurerCount;
        grandExpiredSecretaryCount+= stateExpiredSecretaryCount;
        grandDelinquentOneCount+=  stateDelinquentOneCount;
        grandDelinquentTwoCount+=  stateDelinquentTwoCount;
        grandDelinquentThreeCount+=  stateDelinquentThreeCount;
        grandLocalWithExpiredOfficersCount+=  stateLocalWithExpiredOfficersCount;
        grandLocalsWithoutOfficersCount+=  stateLocalsWithoutOfficersCount;
        grandAffiliatedLocalsCount+=  stateAffiliatedLocalsCount;
        grandUnaffiliatedLocalsCount+=  stateUnaffiliatedLocalsCount;
        // zero out the state totals
        stateLocalCount = 0;
        stateOfficerCount= 0;
        stateStewardCount=0;
        stateRoCount = 0;
        stateTemporaryOfficerCount=0;
        stateExpiredOfficerCount=0;
        stateExpiredPresidentCount=0;
        stateExpiredTreasurerCount=0;
        stateExpiredSecretaryCount=0;
        stateDelinquentOneCount=0;
        stateDelinquentTwoCount=0;
        stateDelinquentThreeCount=0;
        stateLocalWithExpiredOfficersCount=0;
        stateLocalsWithoutOfficersCount=0;
        stateAffiliatedLocalsCount=0;
        stateUnaffiliatedLocalsCount=0;
        currentState = state;
        currentCouncil = council;
        currentLocal = local;
    }
    /**
     * Final routine method which prints the final total line. 
     */
    private void finalRoutine(BufferedWriter writer) 
        throws IOException, SQLException
    {
        localBreak(writer);
        councilBreak(writer);
        stateBreak(writer);
        writer.newLine();
        writer.write("Final Total  ");
        rightJustifyCount(Integer.toString(grandLocalCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(grandOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandStewardCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandRoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandTemporaryOfficerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandExpiredOfficerCount), noSpacePrior, writer);
        noSpacePrior=0;
        rightJustifyCount(Integer.toString(grandExpiredPresidentCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandExpiredTreasurerCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandExpiredSecretaryCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandDelinquentOneCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandDelinquentTwoCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandDelinquentThreeCount), noSpacePrior, writer);
        noSpacePrior=4;
        rightJustifyCount(Integer.toString(grandLocalWithExpiredOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandLocalsWithoutOfficersCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandAffiliatedLocalsCount), noSpacePrior, writer);
        rightJustifyCount(Integer.toString(grandUnaffiliatedLocalsCount), noSpacePrior, writer);
        writer.newLine();
    } // end of final routine
    /**
     * Process detail method 
     */
    private void processDetail(BufferedWriter writer) 
        throws IOException, SQLException, Exception
    {
        if (termExpirationDate != null) 
        {
            expirationDate = sdf.parse(termExpirationDate);
            countForLocal();
        }
        else 
        {
            localExpiredOfficerCount=0;            
            localDelinquentOneCount=0;           
            localDelinquentTwoCount=0;           
            localDelinquentThreeCount=0;
            localExpiredPresidentCount=0;            
            localExpiredTreasurerCount=0;            
            localExpiredSecretaryCount=0;
            localStewardCount=0;            
            localTemporaryOfficerCount=0;
            localRoCount=0;
            localOfficerCount=0;
        }
    } // end of process detail
} // end of OfficerStatisticalSummaryDetailReport