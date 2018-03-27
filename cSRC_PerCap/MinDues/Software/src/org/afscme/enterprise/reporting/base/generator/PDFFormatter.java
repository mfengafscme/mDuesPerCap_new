package org.afscme.enterprise.reporting.base.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import org.afscme.enterprise.reporting.base.PDFConfigurationData;
import org.afscme.enterprise.reporting.base.access.OutputColumnData;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.avalon.framework.logger.Log4JLogger;
import org.apache.fop.apps.Driver;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;



/**
 * This is a PDF formatter that is used to format data from a JDBC ResultSet
 * into a PDF file.  It uses a third party library "FOP" from Apache FOP project.
 */
public class PDFFormatter implements ReportFormatter {
    
    // Following are a few fixed parameters
    private static final String pageSequenceName = "afscme";  // we only have one type of page
    private static final String unit = "in";  // measuring unit in inches
    
    private PDFConfigurationData pdfConfig;
    
    private PrintWriter foWriter;
    private OutputStream out;
    private int totalRows;
    
    // calculated variables
    private String bodyWidth;
    private String halfWidth;
    private float bodyW;  // this will be used to calculate number of columns in one page.
    private String currDate;
    
    // data from caller
    ReportData reportData = null;
    List columnsData = null;  // a list of OutputColumnData objects
    ResultSet resultset = null;
    
    String foFileName;
    
    // record counter
    int recordCounter = 0;
    boolean countFinished = false;
    
    public PDFFormatter(ReportData reportData, List columnsData, OutputStream out) {
        this.reportData = reportData;
        this.columnsData = columnsData;
        this.out = out;
        
        // get the all relevent configuration data
        pdfConfig = ConfigUtil.getPDFConfigurationData();
        
        // calculate all measurements
        calculateAllValues();
    }
    
    public void readData(ResultSet rs)  throws IOException, SQLException {
        resultset = rs;
        foFileName = generateXSLFoFile();
    }
    
    public int formatReport() throws Exception {
        
        // Prepare the "fo" source file from the file system
        BufferedReader fin = new BufferedReader(new FileReader(foFileName));
        
        // setup and run the FOP driver
        Driver fopDriver = new Driver(new InputSource(fin), out);
        fopDriver.setLogger(new Log4JLogger(Logger.getLogger(PDFFormatter.class)));
        fopDriver.setRenderer(Driver.RENDER_PDF);
        fopDriver.run();
        
        // flush and close output stream
        fin.close();
        
        return totalRows;
    }
    
    private void calculateAllValues() {
        // calculate body width
        bodyW = Float.parseFloat(pdfConfig.getPageWidth()) - Float.parseFloat(pdfConfig.getLeftMargin()) - Float.parseFloat(pdfConfig.getRightMargin());
        bodyWidth = Float.toString(bodyW);
        
        // calculate half body width -- for placing header/footer contents
        halfWidth = Float.toString(bodyW / 2f);
        
        // calculate the date/time
        Date date = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();
        currDate = df.format(date);
    }
    
    
    private String generateXSLFoFile() throws IOException, SQLException {
        // NOTE, this resultset should be a scrollable resultset. Therefore, the statement creation should
        // have made sure of that.
        
        // prepare the FO output file
        String foFileName =
        ConfigUtil.getConfigurationData().getTempDir() +
        File.separator +
        reportData.getName() +
        '-' +
        currDate.replace(':', '#') +
        ".fo";
        foWriter = new PrintWriter(new FileWriter(foFileName));
        
        // create the xsl:fo file from the resultset
        writeFO();
        foWriter.flush();
        foWriter.close();
        
        return foFileName;
    }
    
    private void writeFO() throws IOException, SQLException {
        write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");
        write("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
        writeFOLayoutMasterSet();
        writeFirstPage();        // the title page
        writeFOPageSequences();  // multiple fo:page-sequences to reduce memory footprint.
        write("</fo:root>");
    }
    
    private void writeFOLayoutMasterSet() throws IOException {
        write("<fo:layout-master-set>");
        write("<fo:simple-page-master master-name=\"" + pageSequenceName + "\"");
        write("page-height=\"" + pdfConfig.getPageHeight() + unit + "\"");
        write("page-width=\"" + pdfConfig.getPageWidth() + unit + "\"");
        write("margin-top=\"" + pdfConfig.getTopMargin() + unit + "\"");
        write("margin-bottom=\"" + pdfConfig.getBottomMargin() + unit + "\"");
        write("margin-left=\"" + pdfConfig.getLeftMargin() + unit + "\"");
        write("margin-right=\"" + pdfConfig.getRightMargin() + unit + "\">");
        write("<fo:region-before extent=\"" + pdfConfig.getBeforeRegion() + unit + "\"/>");
        write("<fo:region-body margin-top=\"" + pdfConfig.getBeforeRegion() + unit + "\" margin-bottom=\"" + pdfConfig.getAfterRegion() + unit + "\"/>");
        write("<fo:region-after extent=\"" + pdfConfig.getAfterRegion() + unit + "\"/>");
        write("</fo:simple-page-master>");
        write("</fo:layout-master-set>");
        
    }
    
    private void writeFirstPage() throws IOException {
        
        startFOPageSequence();
        
        // start a flow
        write("<fo:flow flow-name=\"xsl-region-body\">");
        write("<fo:block space-before.optimum=\"1in\" text-align=\"center\" font-weight=\"bold\" font-family=\"" + pdfConfig.getTitleFont() + "\" font-size=\"" + pdfConfig.getTitleFontSize() + "\" background-color=\"#006600\" color=\"white\">");
        write("AFSCME CUSTOM REPORT");
        write("</fo:block>");
        write("<fo:block space-before.optimum=\"1in\" text-align=\"start\" font-weight=\"bold\" font-family=\"" + pdfConfig.getTitleContentFont() + "\" font-size=\"" + pdfConfig.getTitleContentFontSize() + "\">");
        write("Report Name");
        write("</fo:block>");
        write("<fo:block space-before.optimum=\"12pt\" text-align=\"start\" font-family=\"" + pdfConfig.getTitleContentFont() + "\" font-size=\"" + pdfConfig.getTitleContentFontSize() + "\">");
        write(reportData.getName());
        write("</fo:block>");
        write("<fo:block space-before.optimum=\"0.5in\" text-align=\"start\" font-weight=\"bold\" font-family=\"" + pdfConfig.getTitleContentFont() + "\" font-size=\"" + pdfConfig.getTitleContentFontSize() + "\">");
        write("Report Description");
        write("</fo:block>");
        write("<fo:block space-before.optimum=\"12pt\" text-align=\"start\" font-family=\"" + pdfConfig.getTitleContentFont() + "\" font-size=\"" + pdfConfig.getTitleContentFontSize() + "\">");
        write(reportData.getDescription());
        write("</fo:block>");
        write("</fo:flow>");
        
        endFOPageSequence();
    }
    
    private void writeFOPageSequences() throws IOException, SQLException {
        // loop through the entire table. We will create a fo:page-sequence every
        // maxRowsInSequence of rows. Each fo:page-sequence will contain one table.
        // we need to check that the current cusor <= tableRows
        
        // calculate section's column indices
        int totalCols = columnsData.size();
        int startCol = 1;  // col index on Resultset for the current section.
        int endCol = 0;
        int colProcessed = 0;
        
        int i = 0;
        OutputColumnData currCol;
        float currTotalWidth = 0;
        float colPadding = Float.parseFloat(pdfConfig.getColumnPadding());
        
        while (startCol <= totalCols) {
            i = endCol;
            currTotalWidth = 0;
            
            while ((currTotalWidth < bodyW) && (i <= (totalCols-1))) {
                currCol = (OutputColumnData)columnsData.get(i);
                currTotalWidth = currTotalWidth + currCol.getColumnWidth() + (2f * colPadding);
                i++; // move to next
            }
            
            if (currTotalWidth <= bodyW)
                endCol = i;
            else
                endCol = i - 1;
            
            // create the current vertical section. It may contains multiple fo:sequences
            // depends on how many "maxRowsInSequence" rows in this vertical section.
            totalRows = writeSection(startCol, endCol);
            if (!countFinished) {
                recordCounter = totalRows;
                countFinished = true;
            }
            
            // move the starCol index to the next section.
            startCol = endCol + 1;
        }
    }
    
    /*
     * write a section of FO content.
     * A section is a vertical content where all pages have the same set of table column.
     * Note the starCol and endCol are indices to ResultSet. They starts with 1, not 0.
     */
    private int writeSection(int startCol, int endCol) throws IOException, SQLException {
        int totalRows = 0;
        
        // chop a vertical section into multiple page-sequences, each with one table in it.
        int rowCounter = 0;
        OutputColumnData currCol;
        int columnStart = startCol-1;
        String colStr;
        
        // first, move the cursor back to the position before the first row
        resultset.beforeFirst();
        
        // traverse the entire resultset from top to bottom, accessing columns from "startCol" to "endCol".
        while (resultset.next()) {  // at least one row if we get into the while loop.
            // start a new page sequence
            startFOPageSequence();
            
            // start a flow
            write("<fo:flow flow-name=\"xsl-region-body\">");
            
            if (pdfConfig.isHyphenationOn())
                write("<fo:block hyphenate=\"true\" font-family=\"" + pdfConfig.getTableContentFont() + "\" font-size=\"" + pdfConfig.getTableContentFontSize() + "\">");
            else
                write("<fo:block font-family=\"" + pdfConfig.getTableContentFont() + "\" font-size=\"" + pdfConfig.getTableContentFontSize() + "\">");
            
            // start a table
            write("<fo:table table-layout=\"fixed\" width=\"" + bodyWidth + unit + "\">");
            
            // specify table columns
            for (int i = columnStart; i < endCol; i++) {
                currCol = (OutputColumnData)columnsData.get(i);
                write("<fo:table-column column-width=\"" + currCol.getColumnWidth() + unit + "\"/>");
                
            }
            
            // specify table header
            write("<fo:table-header>");
            
            write("<fo:table-row keep-together=\"always\">");
            
            for (int i = columnStart; i < endCol; i++) {
                currCol = (OutputColumnData)columnsData.get(i);
                write("<fo:table-cell padding-start=\"" + pdfConfig.getColumnPadding() + unit + "\" padding-end=\"" + pdfConfig.getColumnPadding() + unit + "\">");
                write("<fo:block font-family=\"" + pdfConfig.getTableColumnHeaderFont() + "\" font-size=\"" + pdfConfig.getTableColumnHeaderFontSize() + "\" font-weight=\"bold\">");
                writeData(currCol.getColumnName());
                write("</fo:block>");
                write("</fo:table-cell>");
                
            }
            write("</fo:table-row>");
            write("</fo:table-header>");
            
            // start the table body
            write("<fo:table-body>");
            
            // move the cursor back to the starting position so that we can loop
            resultset.previous();
            
            // fill in the rows.
            rowCounter = 1;  // reset the rowCounter
            while ((rowCounter <= pdfConfig.getMaxRowsInSequence()) && resultset.next()) {
                write("<fo:table-row>");
                
                // treverse the current section of columns on the current row.
                for (int j = startCol; j <= endCol; j++) {
                    write("<fo:table-cell padding-start=\"" + pdfConfig.getColumnPadding() + unit + "\" padding-end=\"" + pdfConfig.getColumnPadding() + unit + "\">");
                    
                    write("<fo:block>");
                    colStr = TextUtil.format(resultset, j, "");
                    writeData(colStr);
                    write("</fo:block>");
                    write("</fo:table-cell>");
                    
                }
                write("</fo:table-row>");
                
                rowCounter++;
                totalRows++;
            }
            
            write("</fo:table-body>");
            write("</fo:table>");
            write("</fo:block>");
            write("</fo:flow>");
            
            endFOPageSequence();
        }
        
        return totalRows;
    }

    /**
     * Writes the string directly to the fo file
     */
    private void write(String str) throws IOException {
        foWriter.println(str);
    }
    
    /**
     * Writes the given string to the fo file, escaping special XML characters first
     */
    private void writeData(String str) throws IOException {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '&':
                    foWriter.write("&amp;");
                    break;
                case '<':
                    foWriter.write("&lt;");
                    break;
                case '>':
                    foWriter.write("&gt;");
                    break;
                case '"':
                    foWriter.write("&quot;");
                    break;
                default:
                    if (ch > 127)
                        foWriter.write("&#" + String.valueOf(new Integer(str.charAt(i))) + ";");
                    else
                        foWriter.write(ch);
            }
        }
    }
    
    
    private void startFOPageSequence() throws IOException {
        write("<fo:page-sequence master-reference=\"" + pageSequenceName + "\" language=\"en\">");
        
        writeFOBeforeRegion();  // the page header on every page
        writeFOAfterRegion();   // the page footer on every page
    }
    
    private void endFOPageSequence() throws IOException {
        write("</fo:page-sequence>");
        
    }
    
    private void writeFOBeforeRegion() throws IOException {
        write("<fo:static-content flow-name=\"xsl-region-before\">");
        write("<fo:table table-layout=\"fixed\" width=\"" + bodyWidth + unit + "\">");
        write("<fo:table-column column-width=\"" + halfWidth + unit + "\"/>");
        write("<fo:table-column column-width=\"" + halfWidth + unit + "\"/>");
        write("<fo:table-body>");
        write("<fo:table-row>");
        write("<fo:table-cell>");
        write("<fo:block text-align=\"start\" font-family=\"" + pdfConfig.getPageHeaderFooterFont() + "\" font-size=\"" + pdfConfig.getPageHeaderFooterFontSize() + "\">" + currDate + "</fo:block>");
        write("</fo:table-cell>");
        write("<fo:table-cell>");
        write("<fo:block text-align=\"end\" font-family=\"" + pdfConfig.getPageHeaderFooterFont() + "\" font-size=\"" + pdfConfig.getPageHeaderFooterFontSize() + "\">p. <fo:page-number/></fo:block>");
        write("</fo:table-cell>");
        write("</fo:table-row>");
        write("</fo:table-body>");
        write("</fo:table>");
        write("<fo:block><fo:leader leader-pattern=\"rule\" rule-style=\"solid\"/></fo:block>");
        write("</fo:static-content>");
        
    }
    
    private void writeFOAfterRegion() throws IOException {
        write("<fo:static-content flow-name=\"xsl-region-after\">");
        write("<fo:block><fo:leader leader-pattern=\"rule\" rule-style=\"solid\"/></fo:block>");
        write("<fo:table table-layout=\"fixed\" width=\"" + bodyWidth + unit + "\">");
        write("<fo:table-column column-width=\"" + halfWidth + unit + "\"/>");
        write("<fo:table-column column-width=\"" + halfWidth + unit + "\"/>");
        write("<fo:table-body>");
        write("<fo:table-row>");
        write("<fo:table-cell>");
        write("<fo:block text-align=\"start\" font-family=\"" + pdfConfig.getPageHeaderFooterFont() + "\" font-size=\"" + pdfConfig.getPageHeaderFooterFontSize() + "\">" + "AFSCME proprietary and confidential information" + "</fo:block>");
        write("</fo:table-cell>");
        write("<fo:table-cell>");
        write("<fo:block text-align=\"end\" font-family=\"" + pdfConfig.getPageHeaderFooterFont() + "\" font-size=\"" + pdfConfig.getPageHeaderFooterFontSize() + "\">" + currDate + "</fo:block>");
        write("</fo:table-cell>");
        write("</fo:table-row>");
        write("</fo:table-body>");
        write("</fo:table>");
        write("</fo:static-content>");
    }
}
