/*
 * LabelFormatter.java
 *
 * The label formatter for formating a mailing list report into pre-defined label format.
 */

package org.afscme.enterprise.reporting.base.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.afscme.enterprise.reporting.base.LabelConfigurationData;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.util.ConfigUtil;

public class LabelFormatter implements ReportFormatter
{
    private static Logger log = Logger.getLogger(LabelFormatter.class);
    private static final char PAGE_BREAK = 0x0C;

    protected int labelsPerLine; // number of labels horizontally on a page.
    protected int linesPerPage; // number of labels vertically on a page.

    private ReportData reportData;
    private ResultSet resultset;
    private int recordCounter;

    private List columnList; // a list of column names in the resultset

    private BufferedWriter writer;
    public LabelFormatter()
    {
    }
    /** Creates a new instance of LabelFormatter */
    public LabelFormatter(
        ReportData reportData,
        OutputStream out,
        ResultSet resultset)
        throws SQLException
    {
        this.reportData = reportData;
        this.writer = new BufferedWriter(new OutputStreamWriter(out));

        LabelConfigurationData labelConfig =
            ConfigUtil.getLabelConfigurationData();
        labelsPerLine = labelConfig.getLabelsPerLine();
        linesPerPage = labelConfig.getLinesPerPage();

        // get a list of columns in this resultset
        ResultSetMetaData rsmd = resultset.getMetaData();
        if (rsmd != null)
        {
            int cc = rsmd.getColumnCount();
            columnList = new ArrayList(cc);
            for (int i = 1; i <= cc; i++)
            {
                columnList.add(rsmd.getColumnName(i));
            }
        }
    }

    public int formatReport()
    {
        return recordCounter;
    }

    public void readData(ResultSet rs) throws IOException, SQLException
    {

        resultset = rs;

        // create 'labelsPerLine' number of labels a time.
        List labelLine = new ArrayList(labelsPerLine); // a line of labels
        Iterator ir;

        int lineCount = 0; // index on the current label line
        int labelCount; // index on the current label within a label line

        Label label;
        Object obj;
        String col;

        while (resultset.next())
        {
            // traverse "labelsPerLine" db rows and create that many labels
            // insert these labels into "labelLine"
            labelCount = 1;
            resultset.previous();

            while ((labelCount <= labelsPerLine) && (resultset.next()))
            {

                // create a new label and set the fields for the label from the current database row
                // XXX - need to update following with actual fields names when ready
                label = new Label();

                label.setCarrierRoute(getColumn("carrier_route_info"));
                label.setOfficerTitle(getColumn("title"));
                label.setAffiliateType(getColumn("aff_type"));
                if(label.getAffiliateType().length()==0)
                    label.setAffiliateType(getColumn("affiliate_id"));
                    
                label.setAffiliateCode(getColumn("aff_localSubChapter"));
				label.setAffiliateStateCode(getColumn("aff_stateNat_type_cd"));
				if(label.getAffiliateStateCode().length()==0)
				    label.setAffiliateStateCode(getColumn("aff_stateNat_type"));
                label.setMemberNo(getColumn("person_pk"));
                label.setFullName(getColumn("full_name"));
                label.setAltMailingName(getColumn("alternate_mailing_nm"));
                label.setPrefix(getColumn("prefix_nm"));
                label.setFirstName(getColumn("first_nm"));
                label.setMiddleName(getColumn("middle_nm"));
                label.setLastName(getColumn("last_nm"));
             
				label.setAddressID(getColumn("address_id"));
                label.setAddress1(getColumn("addr1"));
                label.setAddress2(getColumn("addr2"));
                label.setCity(getColumn("city"));
                
				label.setState(getColumn("state_cd"));
				if(label.getState().length()==0)
				    label.setState(getColumn("state"));
                label.setZip(getColumn("zipcode"));
                
                label.setProvince(getColumn("province"));
				label.setCountry(getColumn("country"));
                
                labelLine.add(label);
                labelCount++;
                recordCounter++;
            }

            // write a line of labels out to file at once.
            writeSingleLabelLine(labelLine);
            lineCount++;

            // check if we need to insert a page break here.
            if (lineCount == linesPerPage)
            {
                writer.write(PAGE_BREAK);
                lineCount = 0;
            }
            else
            {
                // creating two empty lines between label lines
                writer.newLine();
                writer.newLine();
            }

            // clean up the "labelLine" list for next use
            ir = labelLine.iterator();
            while (ir.hasNext())
            {
                obj = ir.next();
                obj = null;
            }
            labelLine.clear();

            // continue with the rest of labels if any.
        }

        writer.flush();
    }

    private void writeSingleLabelLine(List labelLine) throws IOException
    {
        int numLabels = labelLine.size();
        // the actual number of labels for this label line (numLabels <= labelsPerLine)

        int lineIndex = 1;
        int i;
        while (lineIndex <= 7)
        { // total of 7 fixed text lines within a label
            for (i = 0; i < numLabels; i++)
                writer.write(
                    ((Label) labelLine.get(i)).getLabelLine(lineIndex));
            writer.newLine();
            lineIndex++;
        }
    }

    private String getColumn(String columnName) throws SQLException
    {
        String result = null;
        if (columnExists(columnName))
            result = resultset.getString(columnName);
        return result == null ? "" : result;
    }

    private String getColumn(String columnName[]) throws SQLException
    {
        for (int i = 0; i < columnName.length; i++)
            if (columnExists(columnName[i]))
                return getColumn(columnName[i]);
        return "";
    }

    /* Because label formatter relies on certain database fields in the resultset,
     * we need to check if they exist because calling 'resultset.getString()'. This
     * shall avoid causing SQLException when a field is not there in the resultset.
     */
    private boolean columnExists(String columnName)
    {
        if (columnList.contains(columnName))
            return true;
        else
            return false;
    }

}
