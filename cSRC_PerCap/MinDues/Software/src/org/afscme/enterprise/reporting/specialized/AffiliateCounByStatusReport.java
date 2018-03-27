/** Class Name  : AffiliateCountByStatusReport.java
    Date Written: 20030905
    Author      : Kyung A. Callahan
    Description : This is a Affiliate Counts by Affiliate Type report program.
    Note        : Change class name when time permits.
    Maintenance : Kyung 20031105 performance tunning and add catch statement.
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
/**
* This is a affiliate counts by affiliate status specialized report.
*/
public class AffiliateCounByStatusReport implements ReportHandler 
{
    /**
    * The report type to determine what kinds of report (summary or detail).
    */
    private String reportType;
    private int spaceCounter;
    private String currentSortType="";
    /**
    * Generates a affiliate counts by affiliate type report.
    */
    public int generate(OutputStream stream) throws Exception
    {
        BufferedWriter writer = null;
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            if (reportType.equals("Summary")) 
            {
                doSummaryReport(writer);
            }  // end of if (reportType.equals("Summary")) block
            else if (reportType.equals("Detail")) 
            {
                doDetailReport(writer);
            } // end of else if (reportType.equals("Detail")) block
            else 
            {
                doDetailReport(writer);
            }
        return 1;
    } // end of generate method
    /**
    * Print spaces.
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
    * Print value.
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
    * Set report type
    */
    public void setReportType(String reportType) 
    {
        this.reportType = reportType;
    }  
    /**
    * Get file name
    */
    public String getFileName() 
    {
        return null;
    }
    /**
    * Print current date
    */
    private void printCurrentDate(BufferedWriter writer) throws IOException
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "MM/dd/yyyy";
        java.text.SimpleDateFormat sdf = 
            new java.text.SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());          
        writeColumnSpace(writer, 27);
        writer.write(sdf.format(cal.getTime()));
        writer.newLine();
        writer.newLine();
    } // End of printCurrentDate method 
    /**
    * Right justify count
    */
    public void rightJustifyCount(String rowCount, int noSpacePrior, BufferedWriter writer)
        throws IOException
    {
        int spaceCounter=0;
        spaceCounter = rowCount.length();
        spaceCounter = 7 - spaceCounter;
        spaceCounter = spaceCounter + noSpacePrior;
        this.spaceCounter = spaceCounter;
        writeColumnSpace(writer, spaceCounter);
        writeValue(writer, rowCount, rowCount.length());
    } // End of rightJustifyCount method 
    /**
    * Print detail report
    */
    private void doDetailReport(BufferedWriter writer) 
        throws IOException, SQLException
    {
        Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;;
        con = DBUtil.getConnection();
        try
        {
            writer.newLine();
            writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
            writer.newLine();
            writeColumnSpace(writer, 10);
            writer.write("Detail for Affiliate Counts by Affiliate Type");
            writer.newLine();
            /**
            * The SQL that implements the report
            */
            String sqlQuery = 
                "select " +
                "aff_statenat_type as state, " + 
                "aff_type as type, " +
                "aff_councilRetiree_chap as council, " + 
                "aff_localSubChapter as local, " + 
                "aff_subunit as sub_local, " +
                "aff_abbreviated_nm as name, " + 
                "com_cd_cd as status, " +
                "case " +
                "when aff_type = 'c' then 1 " +
                "when aff_type = 'l' then 1 " + 
                "when aff_type = 'u' then 1 " + 
                "when aff_type = 'r' then 4  " +
                "when aff_type = 's' then 4 " +
                "end as sort_type, " +
                "cast(aff_councilRetiree_chap as int) as sort_council, " +
                "cast(aff_localSubChapter as int) as sort_local, " +
                "cast(aff_subunit as int) as sort_sublocal " +
                "from aff_organizations, " + 
                "common_codes " +
                "where aff_status = com_cd_pk " + 
                "order by sort_type, " + 
                "aff_statenat_type, " + 
                "sort_council, " + 
                "sort_local, " +
                "sort_sublocal ";
            stmt = con.prepareStatement(sqlQuery);
            rs = stmt.executeQuery();
            printCurrentDate(writer);
            writeColumnSpace(writer, 1);
            writer.write("State Council Local SubUnit    AffiliateName                 Status");
            writer.newLine();
            writer.newLine();
            /**
            * Write detail lines
            */
            int count = 0;
            String currentState="", 
                   currentCouncil="", 
                    prtState="", 
                    prtCouncil="",
                    state="",
                    council="",
                    local="",
                    name="",
                    status="",
                    sortType="",
                    subLocal="",
                    retireeHeadingPrinted = "";
            while (rs.next()) 
            {
                count++;
                state = rs.getString("state");
                council = rs.getString("council");
                local = rs.getString("local");
                name = rs.getString("name");
                status = rs.getString("status");
                sortType = rs.getString("sort_type");
                subLocal = rs.getString("sub_local");
                if (count == 1) currentSortType = sortType;
                /**
                * Check to print the retiree heading
                */
                if (!currentSortType.equals(sortType) && retireeHeadingPrinted != null) {
                    currentSortType = sortType;
                    printHeading(writer);
                    retireeHeadingPrinted = "Yes";
                }                
                if(currentState.equals(state) && currentCouncil.equals(council)) {
                    prtState="  ";
                    prtCouncil="    ";
                }
                else
                {
                    prtState=state;
                    prtCouncil=council;
                }
                writeColumnSpace(writer, 1);
                writeValue(writer, prtState, 2);
                writeColumnSpace(writer, 4);
                writeValue(writer, prtCouncil, 4);
                writeColumnSpace(writer, 4);
                writeValue(writer, local, 4);
                writeColumnSpace(writer, 3);
                writeValue(writer, subLocal, 4);
                writeColumnSpace(writer, 6);
                writeValue(writer, name, 29);
                writeColumnSpace(writer, 1);
                writeValue(writer, status, 1);
                writer.newLine();
                if (currentState.equals(state))
                {
                }
                else
                {
                    currentState=state;
                }
                if (currentCouncil.equals(council))
                {
                }
                else
                {
                    currentCouncil=council;
                }
            } // end of while block                        
        } // end of try block
        catch (SQLException e) 
        {
            writer.write(" ");
            writer.newLine();
            writer.newLine();
            writer.write("An error has occurred in program - AffiliateCountByStatusReport.java");
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
    } // End of doDetailReport method
    /**
    * Print summary report
    */
    private void doSummaryReport(BufferedWriter writer) 
        throws IOException, SQLException
    {
        int totalCount=0;
        Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;;
        con = DBUtil.getConnection();
        // heading line 1
        writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
        writer.newLine();
        writeColumnSpace(writer, 10);
        writer.write("Summary for Affiliate Counts by Affiliate Type");
        writer.newLine();
        printCurrentDate(writer);
        writeColumnSpace(writer, 1);
        writer.write("Type");
	writeColumnSpace(writer, 5);
	writer.write("----------------Chartered---------------");
	writeColumnSpace(writer, 6);
	writer.write("Total");
	writeColumnSpace(writer, 6);
	writer.write("Total");
	writer.newLine();
        // heading line 2
        writeColumnSpace(writer, 14);
        writer.write("Yes");
	writeColumnSpace(writer, 10);
	writer.write("No");
	writeColumnSpace(writer, 3);
	writer.write("Pending");
	writeColumnSpace(writer, 6);
	writer.write("Admin");
	writeColumnSpace(writer, 5);
	writer.write("Active");
	writeColumnSpace(writer, 3);
	writer.write("Inactive");
	writer.newLine();
        writer.newLine();
   	try
	{ 
            // Detail line 1 - council
            writeColumnSpace(writer, 1);
            writer.write("Council");
            // Get count of chartered council
            String sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'C' " + 
                "and com_cd_cd = 'C' ";
            con = DBUtil.getConnection();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            String rowCount="";
            int noSpacePrior=0;
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 2;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
                
            // Get count of not chartered council
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'C' " + 
                "and com_cd_cd = 'N' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
                
            // Get count of pending charter council
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'C' " + 
                "and com_cd_cd = 'PC' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
               
            // Get count of administratorship council
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'C' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
                
            // Get count of total active council
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'C' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC' " + 
                "or  com_cd_cd = 'PC' " + 
                "or  com_cd_cd = 'PD' " + 
                "or  com_cd_cd = 'N' " + 
                "or  com_cd_cd = 'C') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
                
            // Get count of total inactive council
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'C' " + 
                "and (com_cd_cd = 'M' " + 
                "or  com_cd_cd = 'D') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
            writer.newLine();               
            writer.newLine();
            // Detail line 2 - local
            writeColumnSpace(writer, 1);
            writer.write("Local");
            // Get count of chartered local
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and com_cd_cd = 'C' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
                
            // Get count of not chartered local
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and com_cd_cd = 'N' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
                
            // Get count of pending charter local
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and com_cd_cd = 'PC' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
                
            // Get count of administratorship local
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
              
            // Get count of total active local
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC' " + 
                "or  com_cd_cd = 'PC' " + 
                "or  com_cd_cd = 'PD' " + 
                "or  com_cd_cd = 'N' " + 
                "or  com_cd_cd = 'C') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
            
            // Add to total active local count 
            totalCount = totalCount + Integer.parseInt(rowCount);
                
            // Get count of total inactive  local
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and (com_cd_cd = 'M' " + 
                "or  com_cd_cd = 'D') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }                 
            writer.newLine();               
            writer.newLine();               
            // Detail line 3 - units
            writeColumnSpace(writer, 1);
            writer.write("Units");
            // Get count of chartered units
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'U' " + 
                "and com_cd_cd = 'C' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
               
            // Get count of not chartered units
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'U' " + 
                "and com_cd_cd = 'N' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
            
            // Get count of pending charter units
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'L' " + 
                "and com_cd_cd = 'PC' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
            
            // Get count of administratorship units
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'U' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
            
            // Get count of total active units
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'U' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC' " + 
                "or  com_cd_cd = 'PC' " + 
                "or  com_cd_cd = 'PD' " + 
                "or  com_cd_cd = 'N' " + 
                "or  com_cd_cd = 'C') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
            
            // Add to total active units count
            totalCount = totalCount + Integer.parseInt(rowCount);
            
            // Get count of total inactive units
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'U' " + 
                "and (com_cd_cd = 'M' " + 
                "or  com_cd_cd = 'D') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }                                 
            writer.newLine(); 
            
            // Write total active line of locals and untis
            writer.newLine(); 
            noSpacePrior = 54;
            rightJustifyCount(Integer.toString(totalCount), noSpacePrior, writer);
            writer.newLine();               
            writer.newLine();
            // Detail line 4 - retiree chapter
            writeColumnSpace(writer, 1);
            writer.write("Ret Chap");
            // Get count of chartered 
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'R' " + 
                "and com_cd_cd = 'C' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 1;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
            
            // Get count of not chartered 
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'R' " + 
                "and com_cd_cd = 'N' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
             
            // Get count of pending charter 
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'R' " + 
                "and com_cd_cd = 'PC' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
            
            // Get count of administratorship 
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'R' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
             
            // Get count of total active 
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'R' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC' " + 
                "or  com_cd_cd = 'PC' " + 
                "or  com_cd_cd = 'PD' " + 
                "or  com_cd_cd = 'N' " + 
                "or  com_cd_cd = 'C') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
            // Add to total active count
            totalCount = 0;
            totalCount = totalCount + Integer.parseInt(rowCount);
            
            // Get count of total inactive 
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'R' " + 
                "and (com_cd_cd = 'M' " + 
                "or  com_cd_cd = 'D') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }                 
            writer.newLine();               
            writer.newLine();           
            // Detail line 5 - sub chapter
            writeColumnSpace(writer, 1);
            writer.write("Sub Chap");
            // Get count of chartered 
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'S' " + 
                "and com_cd_cd = 'C' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 1;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
          
            // Get count of not chartered 
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'S' " + 
                "and com_cd_cd = 'N' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
            
            // Get count of pending charter 
            sql = 
                "select " + 
                "count(com_cd_cd) " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'S' " + 
                "and com_cd_cd = 'PC' ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block                
          
            // Get count of administratorship 
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'S' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            } // end of while block
                
            // Get count of total active 
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'S' " + 
                "and (com_cd_cd = 'RA' " + 
                "or  com_cd_cd = 'UA' " + 
                "or  com_cd_cd = 'AC' " + 
                "or  com_cd_cd = 'PC' " + 
                "or  com_cd_cd = 'PD' " + 
                "or  com_cd_cd = 'N' " + 
                "or  com_cd_cd = 'C') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
             
            // Add to total active count to get sum of retiree and retiree sub chapter
            totalCount = totalCount + Integer.parseInt(rowCount);
                
            // Get count of total inactive 
            sql = 
                "select " + 
                "count(com_cd_cd) as row_count " + 
                "from aff_organizations, common_codes " + 
                "where aff_status = com_cd_pk " +
                "and aff_type = 'S' " + 
                "and (com_cd_cd = 'M' " + 
                "or  com_cd_cd = 'D') ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            rowCount="";
            while (rs.next()) 
            {
                rowCount = rs.getString(1);
                noSpacePrior = 4;
                rightJustifyCount(rowCount, noSpacePrior, writer);
            }
            writer.newLine();               
             
            // Write total active line of locals and untis
            writer.newLine(); 
            noSpacePrior = 54;
            rightJustifyCount(Integer.toString(totalCount), noSpacePrior, writer);
            writer.newLine();               
            writer.newLine();           
        } // end of try block
        finally 
        {
           DBUtil.cleanup(con, stmt, rs);
	   writer.flush();
           writer.close();
	} 
    }  // End of doSummaryReport method
    /**
    * Print heading
    */
    private void printHeading(BufferedWriter writer) throws IOException
    {
        writer.newLine();
        writer.write("American Federation of State, County and Municipal Employees, AFL-CIO");
        writer.newLine();
        writeColumnSpace(writer, 10);
        writer.write("Detail for Affiliate Counts by Affiliate Type");
        writer.newLine();
        if (currentSortType.equals("4")) {
            writeColumnSpace(writer, 28);
            writer.write("Retiree");
            writer.newLine();
            printCurrentDate(writer);
            writer.newLine();
            writer.newLine();
            writer.write("State Retiree SubChapter        AffiliateName                 Status");
            writer.newLine();
            writer.newLine();
        }
    }
} // End of AffiliateCountByStatus class