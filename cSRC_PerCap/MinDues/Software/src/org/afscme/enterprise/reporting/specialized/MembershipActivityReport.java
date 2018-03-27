package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * This is a membership activity report.
 */
public class MembershipActivityReport implements ReportHandler
{

    /**
     * The SQL that implements the report
     */
    private static String SQL_SELECT_MEMBERSHIP_ACTIVITY_INFO =
        "SELECT Aff_Organizations.aff_councilRetiree_chap, Aff_Organizations.aff_type, "
            + "Aff_Organizations.aff_localSubchapter, Aff_Organizations.aff_code, "
            + "Aff_Organizations.aff_stateNat_type, Aff_Mbr_Activity.Membership_activity_count, "
            + "Aff_Mbr_Activity.Membership_activity_type, Aff_Mbr_Activity.time_pk, Aff_Mbr_Activity.aff_pk, Aff_Organizations.aff_subUnit "
            + "FROM Aff_Organizations, Aff_Mbr_Activity, TIME_DIM WHERE Aff_Organizations.aff_pk = Aff_Mbr_Activity.aff_pk "
            + "AND Time_Dim.time_pk = Aff_Mbr_Activity.time_pk AND Time_Dim.calendar_year = ? "
            + "ORDER BY Aff_Organizations.aff_councilRetiree_chap ASC, Aff_Organizations.aff_Type ASC, "
            + "Aff_Organizations.aff_localSubchapter ASC, Aff_Organizations.aff_code ASC";

    /**
     * Generates a membership activity report.
     * @param stream The membership activity report is generated into this stream.
     * @return A return code.
     */
    public int generate(OutputStream stream) throws Exception
    {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(stream));
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int yearLength = String.valueOf(year).length();
            String currentYear =
                String.valueOf(year).substring(yearLength - 2, yearLength);
            String currentMonth = formatMonth(month);
            Map stateMap =
                JNDIUtil.getMaintainCodesHome().create().getCodesByCode(
                    "State");
            ArrayList states = new ArrayList(stateMap.values());
            MembershipActivityReportInfo mari = getMembershipActivityInfo(year);
            int pageCount = 0;

            // loop through all the states, only write for those states having affiliates
            for (int i = 0; i < states.size(); i++)
            {
                MonthStateADRCount monthStateADRCount = null;
                String state = null;
                int count = 0;

                // loop through to ensure the state in question has affiliates
                do
                {
                    state = ((CodeData) states.get(i)).getCode();
                    monthStateADRCount =
                        mari.getMonthStateADRCount(count + 1, state);
                }
                while (monthStateADRCount == null && (++count < month));

                if (monthStateADRCount != null)
                {
                    // there is at least one row for the state in question so start writing
                    // write the header information
                    writer.newLine();
                    writer.newLine();
                    writer.write(
                        " 020301                         AMERICAN FEDERATION OF STATE, COUNTY AND MUNICIPAL EMPLOYEES, AFL-CIO                       PAGE   "
                            + (++pageCount)
                            + " "
                            + "\n\n");
                    writer.write(
                        " SUM430P                                   MEMBERSHIP ACTIVITY LOG - FROM 01"
                            + currentYear
                            + "  THRU "
                            + currentMonth
                            + currentYear
                            + "\n"
                            + "\n");

                    // write mm/yy
                    for (int j = 0; j < month; j++)
                    {
                        if (j == 0)
                        {
                            writer.write(
                                "                "
                                    + formatMonth(j + 1)
                                    + currentYear);
                        }
                        else
                        {
                            writer.write(
                                "      " + formatMonth(j + 1) + currentYear);
                        }
                    }
                    writer.write("\n");
                    writer.write("    " + state);

                    // write ADD DROP for each mm/yy
                    for (int j = 0; j < month; j++)
                    {
                        if (j == 0)
                        {
                            writer.write("         " + "ADD DROP");
                        }
                        else
                        {
                            writer.write("  " + "ADD DROP");
                        }
                    }
                    writer.write("\n");

                    // write REV for each mm/yy
                    for (int j = 0; j < month; j++)
                    {
                        if (j == 0)
                        {
                            writer.write("                 " + "REV");
                        }
                        else
                        {
                            writer.write("       " + "REV");
                        }
                    }
                    writer.write("\n\n");

                    ArrayList affiliates = mari.getAffiliatesByState(state);
                    String previousCouncillRetiree = null;
                    // loop through all the affiliates of the state in question
                    for (int j = 0; j < affiliates.size(); j++)
                    {
                        AffiliateData affiliateData =
                            (AffiliateData) affiliates.get(j);
                        if (previousCouncillRetiree == null
                            || affiliateData.getAffiliateId().getCouncil().equals(
                                previousCouncillRetiree))
                        {
                            previousCouncillRetiree =
                                affiliateData.getAffiliateId().getCouncil();

                            // loop through each month
                            for (int k = 0; k < month; k++)
                            {
                                // write the affiliate identifier
                                if (k == 0)
                                {
                                    writer.write(
                                        " "
                                            + formatCouncil(
                                                affiliateData
                                                    .getAffiliateId()
                                                    .getCouncil())
                                            + " "
                                            + formatAffiliateId(
                                                affiliateData
                                                    .getAffiliateId()
                                                    .getType()
                                                    + (affiliateData
                                                        .getAffiliateId()
                                                        .getType()
                                                        .charValue()
                                                        == 'U'
                                                        ? affiliateData
                                                            .getAffiliateId()
                                                            .getSubUnit()
                                                        : affiliateData
                                                            .getAffiliateId()
                                                            .getLocal())));
                                    writer.write(" ");
                                }
                                MonthStateADRCount msadr =
                                    mari.getMonthStateADRCount(k + 1, state);
                                if (msadr != null)
                                {
                                    ArrayList councilADRCounts =
                                        msadr.getCouncilADRCounts();
                                    boolean exists = false;

                                    outer : for (
                                        int l = 0;
                                            l < councilADRCounts.size();
                                            l++)
                                    {
                                        CouncilADRCount councilADRCount =
                                            (
                                                CouncilADRCount) councilADRCounts
                                                    .get(
                                                l);
                                        ArrayList affiliateADRCounts =
                                            councilADRCount
                                                .getAffiliateADRCounts();

                                        // write add count and drop count for the  affiliate in question
                                        for (int m = 0;
                                            m < affiliateADRCounts.size();
                                            m++)
                                        {
                                            AffiliateADRCount aadr =
                                                (
                                                    AffiliateADRCount) affiliateADRCounts
                                                        .get(
                                                    m);
                                            // if the affiliate belongs to the particular council, break and go to the
                                            // next affiliate
                                            if (affiliateData
                                                .getAffPk()
                                                .equals(
                                                    aadr
                                                        .getAffiliateData()
                                                        .getAffPk()))
                                            {
                                                writer.write(
                                                    formatCountAffiliate(
                                                        aadr.getAddCount())
                                                        + formatCountAffiliate(
                                                            aadr
                                                                .getDropCount()));
                                                exists = true;
                                                break outer;
                                            }
                                        }
                                    }

                                    // if the affiliate doesn't have any record fot the month/state in question,
                                    // write zero
                                    if (!exists)
                                    {
                                        writer.write(
                                            formatCountAffiliate(0)
                                                + formatCountAffiliate(0));
                                    }
                                }
                                // if there is no record for the month/stae in question, write zero
                                else
                                {
                                    writer.write(
                                        formatCountAffiliate(0)
                                            + formatCountAffiliate(0));
                                }
                            }
                            writer.newLine();

                            // perform the same logic as above, this time for the revised count
                            for (int k = 0; k < month; k++)
                            {
                                if (k == 0)
                                {
                                    writer.write("               ");
                                }
                                else
                                {
                                    writer.write("     ");
                                }
                                MonthStateADRCount msadr =
                                    mari.getMonthStateADRCount(k + 1, state);
                                if (msadr != null)
                                {
                                    ArrayList councilADRCounts =
                                        msadr.getCouncilADRCounts();
                                    boolean exists = false;

                                    outer : for (
                                        int l = 0;
                                            l < councilADRCounts.size();
                                            l++)
                                    {
                                        CouncilADRCount councilADRCount =
                                            (
                                                CouncilADRCount) councilADRCounts
                                                    .get(
                                                l);
                                        ArrayList affiliateADRCounts =
                                            councilADRCount
                                                .getAffiliateADRCounts();

                                        for (int m = 0;
                                            m < affiliateADRCounts.size();
                                            m++)
                                        {
                                            AffiliateADRCount aadr =
                                                (
                                                    AffiliateADRCount) affiliateADRCounts
                                                        .get(
                                                    m);
                                            if (affiliateData
                                                .getAffPk()
                                                .equals(
                                                    aadr
                                                        .getAffiliateData()
                                                        .getAffPk()))
                                            {
                                                writer.write(
                                                    formatCountAffiliate(
                                                        aadr
                                                            .getRevisedCount()));
                                                exists = true;
                                                break outer;
                                            }
                                        }
                                    }
                                    if (!exists)
                                    {
                                        writer.write(formatCountAffiliate(0));
                                    }
                                }
                                else
                                {
                                    writer.write(formatCountAffiliate(0));
                                }
                            }
                            writer.newLine();
                            writer.newLine();
                        }
                        // write total for the council in question
                        else
                        {
                            writer.newLine();

                            // write add count for each month
                            writer.write(
                                " "
                                    + formatCouncil(previousCouncillRetiree)
                                    + " ADD--->");
                            for (int k = 0; k < month; k++)
                            {
                                if (k != 0)
                                {
                                    writer.write("   ");
                                }
                                MonthStateADRCount msadr =
                                    mari.getMonthStateADRCount(k + 1, state);
                                if (msadr == null)
                                {
                                    writer.write(formatCount(0));
                                }
                                else
                                {
                                    CouncilADRCount councilADRCount =
                                        msadr.getCouncilADRCount(
                                            previousCouncillRetiree);
                                    if (councilADRCount != null)
                                    {
                                        writer.write(
                                            formatCount(
                                                councilADRCount.getAddCount()));
                                    }
                                    else
                                    {
                                        writer.write(formatCount(0));
                                    }
                                }
                            }
                            writer.newLine();

                            // write revised count for each month
                            writer.write("      REV--->");
                            for (int k = 0; k < month; k++)
                            {
                                if (k != 0)
                                {
                                    writer.write("   ");
                                }
                                MonthStateADRCount msadr =
                                    mari.getMonthStateADRCount(k + 1, state);
                                if (msadr == null)
                                {
                                    writer.write(formatCount(0));
                                }
                                else
                                {
                                    CouncilADRCount councilADRCount =
                                        msadr.getCouncilADRCount(
                                            previousCouncillRetiree);
                                    if (councilADRCount != null)
                                    {
                                        writer.write(
                                            formatCount(
                                                councilADRCount
                                                    .getRevisedCount()));
                                    }
                                    else
                                    {
                                        writer.write(formatCount(0));
                                    }
                                }
                            }
                            writer.newLine();

                            // write drop count for each month
                            writer.write("      DROP-->");
                            for (int k = 0; k < month; k++)
                            {
                                if (k != 0)
                                {
                                    writer.write("   ");
                                }
                                MonthStateADRCount msadr =
                                    mari.getMonthStateADRCount(k + 1, state);
                                if (msadr == null)
                                {
                                    writer.write(formatCount(0));
                                }
                                else
                                {
                                    CouncilADRCount councilADRCount =
                                        msadr.getCouncilADRCount(
                                            previousCouncillRetiree);
                                    if (councilADRCount != null)
                                    {
                                        writer.write(
                                            formatCount(
                                                councilADRCount
                                                    .getDropCount()));
                                    }
                                    else
                                    {
                                        writer.write(formatCount(0));
                                    }
                                }
                            }
                            previousCouncillRetiree =
                                affiliateData.getAffiliateId().getCouncil();
                            j -= 1;
                            writer.newLine();
                            writer.newLine();
                            writer.newLine();
                            writer.newLine();
                        }
                    }
                    writer.newLine();

                    // this must be the last council for the state in question
                    // write count information for this council
                    writer.write(
                        " "
                            + formatCouncil(previousCouncillRetiree)
                            + " ADD--->");
                    for (int k = 0; k < month; k++)
                    {
                        if (k != 0)
                        {
                            writer.write("   ");
                        }
                        MonthStateADRCount msadr =
                            mari.getMonthStateADRCount(k + 1, state);
                        if (msadr == null)
                        {
                            writer.write(formatCount(0));
                        }
                        else
                        {
                            CouncilADRCount councilADRCount =
                                msadr.getCouncilADRCount(
                                    previousCouncillRetiree);
                            if (councilADRCount != null)
                            {
                                writer.write(
                                    formatCount(councilADRCount.getAddCount()));
                            }
                            else
                            {
                                writer.write(formatCount(0));
                            }
                        }
                    }
                    writer.newLine();

                    writer.write("      REV--->");
                    for (int k = 0; k < month; k++)
                    {
                        if (k != 0)
                        {
                            writer.write("   ");
                        }
                        MonthStateADRCount msadr =
                            mari.getMonthStateADRCount(k + 1, state);
                        if (msadr == null)
                        {
                            writer.write(formatCount(0));
                        }
                        else
                        {
                            CouncilADRCount councilADRCount =
                                msadr.getCouncilADRCount(
                                    previousCouncillRetiree);
                            if (councilADRCount != null)
                            {
                                writer.write(
                                    formatCount(
                                        councilADRCount.getRevisedCount()));
                            }
                            else
                            {
                                writer.write(formatCount(0));
                            }
                        }
                    }
                    writer.newLine();

                    writer.write("      DROP-->");
                    for (int k = 0; k < month; k++)
                    {
                        if (k != 0)
                        {
                            writer.write("   ");
                        }
                        MonthStateADRCount msadr =
                            mari.getMonthStateADRCount(k + 1, state);
                        if (msadr == null)
                        {
                            writer.write(formatCount(0));
                        }
                        else
                        {
                            CouncilADRCount councilADRCount =
                                msadr.getCouncilADRCount(
                                    previousCouncillRetiree);
                            if (councilADRCount != null)
                            {
                                writer.write(
                                    formatCount(
                                        councilADRCount.getDropCount()));
                            }
                            else
                            {
                                writer.write(formatCount(0));
                            }
                        }
                    }
                    writer.newLine();
                    writer.newLine();
                    writer.newLine();

                    // write count information for the state
                    writer.write(" " + state + "   ADD--->");
                    for (int j = 0; j < month; j++)
                    {
                        if (j != 0)
                        {
                            writer.write("   ");
                        }
                        monthStateADRCount =
                            mari.getMonthStateADRCount(j + 1, state);
                        if (monthStateADRCount != null)
                        {
                            writer.write(
                                formatCount(monthStateADRCount.getAddCount()));
                        }
                        else
                        {
                            writer.write(formatCount(0));
                        }
                    }
                    writer.newLine();

                    writer.write("      REV--->");
                    for (int j = 0; j < month; j++)
                    {
                        if (j != 0)
                        {
                            writer.write("   ");
                        }
                        monthStateADRCount =
                            mari.getMonthStateADRCount(j + 1, state);
                        if (monthStateADRCount != null)
                        {
                            writer.write(
                                formatCount(
                                    monthStateADRCount.getRevisedCount()));
                        }
                        else
                        {
                            writer.write(formatCount(0));
                        }
                    }
                    writer.newLine();

                    writer.write("      DROP-->");
                    for (int j = 0; j < month; j++)
                    {
                        if (j != 0)
                        {
                            writer.write("   ");
                        }
                        monthStateADRCount =
                            mari.getMonthStateADRCount(j + 1, state);
                        if (monthStateADRCount != null)
                        {
                            writer.write(
                                formatCount(monthStateADRCount.getDropCount()));
                        }
                        else
                        {
                            writer.write(formatCount(0));
                        }
                    }
                }
            }
            writer.newLine();
            writer.newLine();

            // create headers for the grand total
            writer.write(
                " 020301                         AMERICAN FEDERATION OF STATE, COUNTY AND MUNICIPAL EMPLOYEES, AFL-CIO                       PAGE   "
                    + (++pageCount)
                    + " "
                    + "\n\n");
            writer.write(
                " SUM430P                                   MEMBERSHIP ACTIVITY LOG - FROM 01"
                    + currentYear
                    + "  THRU "
                    + currentMonth
                    + currentYear
                    + "\n"
                    + "\n");
            for (int i = 0; i < month; i++)
            {
                if (i == 0)
                {
                    writer.write(
                        "                " + formatMonth(i + 1) + currentYear);
                }
                else
                {
                    writer.write("      " + formatMonth(i + 1) + currentYear);
                }
            }
            writer.write("\n");
            for (int i = 0; i < month; i++)
            {
                if (i == 0)
                {
                    writer.write("               " + "ADD DROP");
                }
                else
                {
                    writer.write("  " + "ADD DROP");
                }
            }
            writer.write("\n");
            for (int i = 0; i < month; i++)
            {
                if (i == 0)
                {
                    writer.write("                 " + "REV");
                }
                else
                {
                    writer.write("       " + "REV");
                }
            }
            writer.newLine();
            writer.newLine();
            writer.write(" GRAND TOTAL");
            writer.newLine();
            writer.newLine();
            writer.newLine();

            // write grand total count information
            writer.write("      ADD--->");
            for (int i = 0; i < month; i++)
            {
                if (i != 0)
                {
                    writer.write("   ");
                }
                writer.write(formatCount(mari.getAddCount(i + 1)));
            }
            writer.newLine();

            writer.write("      REV--->");
            for (int i = 0; i < month; i++)
            {
                if (i != 0)
                {
                    writer.write("   ");
                }
                writer.write(formatCount(mari.getRevisedCount(i + 1)));
            }
            writer.newLine();

            writer.write("      DROP-->");
            for (int i = 0; i < month; i++)
            {
                if (i != 0)
                {
                    writer.write("   ");
                }
                writer.write(formatCount(mari.getDropCount(i + 1)));
            }
        }
        finally
        {
            writer.flush();
            writer.close();
        }
        return 0;
    }

    /**
     * Gets information pertaining to a membership activity report.
     * @param year The year for which membership activity info should extracted.
     * @return The information pertaining to the membership activity report.
     */
    public MembershipActivityReportInfo getMembershipActivityInfo(int year)
        throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MembershipActivityReportInfo mari = new MembershipActivityReportInfo();

        try
        {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBERSHIP_ACTIVITY_INFO);
            ps.setInt(1, year);
            rs = ps.executeQuery();

            while (rs.next())
            {
                int month = rs.getInt(8);
                String state = rs.getString(5);
                String councilRetireeChap = rs.getString(1);
                int affPk = rs.getInt(9);
                int membershipActivityType = rs.getInt(7);
                int membershipActivityCount = rs.getInt(6);
                MonthStateADRCount msadr =
                    mari.getMonthStateADRCount(month, state);

                if (msadr == null)
                {
                    // create new MonthStateADRCount for this month/state
                    msadr = new MonthStateADRCount(month, state);
                    mari.addMonthStateADRCount(msadr);
                }

                CouncilADRCount cadr =
                    msadr.getCouncilADRCount(councilRetireeChap);
                if (cadr == null)
                {
                    // create new CouncilADRCount for this MonthStateADRCount
                    cadr = new CouncilADRCount(councilRetireeChap);
                    msadr.addCouncilADRCount(cadr);
                }

                AffiliateADRCount aadr = cadr.getAffiliateADRCount(affPk);
                if (aadr == null)
                {
                    // create new AffiliateADRCount for this CouncilADRCount
                    aadr = new AffiliateADRCount();
                    AffiliateData affData = new AffiliateData();
                    affData.setAffPk(new Integer(affPk));
                    AffiliateIdentifier affId = new AffiliateIdentifier();
                    affId.setLocal(rs.getString(3));
                    affId.setSubUnit(rs.getString(10));
                    affId.setType(new Character(rs.getString(2).charAt(0)));
                    affId.setCode(new Character(rs.getString(4).charAt(0)));
                    affId.setCouncil(councilRetireeChap, true);
                    affData.setAffiliateId(affId);
                    aadr.setAffiliateData(affData);
                    cadr.addAffiliateADRCount(aadr);
                    mari.addAffiliateByState(affData, state);
                }

                // determine the type of count
                if (membershipActivityType == 30001)
                {
                    aadr.setAddCount(membershipActivityCount);
                }
                if (membershipActivityType == 30003)
                {
                    aadr.setDropCount(membershipActivityCount);
                }
                if (membershipActivityType == 30002)
                {
                    aadr.setRevisedCount(membershipActivityCount);
                }
            }
        }
        finally
        {
            DBUtil.cleanup(con, ps, rs);
        }
        return mari;
    }

    /**
     * Formats month in the format mm.
     * @param month The numeric value of the month.
     * @return The formatted month.
     */
    private String formatMonth(int month)
    {
        String formattedMonth = String.valueOf(month);
        if (formattedMonth.length() == 1)
        {
            return "0" + formattedMonth;
        }
        return formattedMonth;
    }

    /**
     * Formats a count to ensure that the length is five characters. Adds commas and blanks if needed.
     * @param count The count to be formatted.
     * @return The formatted count.
     */
    private String formatCountAffiliate(int count)
    {
		return TextUtil.padLeading(String.valueOf(count), 5, ' ');      
    }

    /**
     * Formats an affiliate id to ensure that the length is six characters. Adds blanks if needed.
     * @param affId The affiliate id to be formatted.
     * @return The formatted affiliate id.
     */
    private String formatAffiliateId(String affId)
    {
        while (affId.length() < 6)
        {
            affId += " ";
        }
        return affId;
    }

    /**
     * Formats an council to ensure that the length is four characters. Adds blanks if needed.
     * @param council The council to be formatted.
     * @return The formatted council.
     */
    private String formatCouncil(String council)
    {
        while (council.length() < 4)
        {
            council += " ";
        }
        return council;
    }

    /**
     * Formats a count to ensure that the length is seven characters. Adds commas and blanks if needed.
     * @param count The count to be formatted.
     * @return The formatted count.
     */
    private String formatCount(int count)
    {
        return TextUtil.padLeading(String.valueOf(count), 7, ' ');             
    }

    /** Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     *
     */
    public String getFileName()
    {
        return null;
    }

}
