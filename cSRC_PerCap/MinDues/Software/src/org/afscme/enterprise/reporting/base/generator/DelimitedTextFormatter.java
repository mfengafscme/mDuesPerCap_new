/*
 * DelimitedTextFormatter.java
 *
 * A formatter for generating delimited text report.
 */

package org.afscme.enterprise.reporting.base.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.afscme.enterprise.common.ConfigurationData;
import org.afscme.enterprise.reporting.base.access.OutputColumnData;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.TextUtil;




public class DelimitedTextFormatter implements ReportFormatter {
    
    private ReportData reportData = null;
    private List columnsData = null;
    private ResultSet resultset = null;
    private BufferedWriter writer;
    private int recordCount = 0;
    
    private char delimiter = '\t';  // default to TAB
    
    /** Creates a new instance of DelimitedTextFormatter */
    public DelimitedTextFormatter(ReportData reportData, OutputFormat format, List columnsData, OutputStream out) {
        this.reportData = reportData;
        this.columnsData = columnsData;
        
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
        ConfigurationData systemConfig = ConfigUtil.getConfigurationData();
        
        switch (format.getFormat()) {
            case OutputFormat.TAB:
                delimiter = '\t';
                break;
			case OutputFormat.MAIL_MERGE:
			case OutputFormat.MAILING_HOUSE:
            case OutputFormat.COMMA:
                delimiter = ',';
                break;
            case OutputFormat.SEMICOLON:
                delimiter = ';';
                break;
            default:
                delimiter = '\t';
        }
    }
    
    public int formatReport() throws IOException, SQLException {
        return recordCount;
    }
        
    public void readData(ResultSet rs)  throws IOException, SQLException {
        resultset = rs;
        
        String emptyString = "";
        
        // write the column header once        
        Iterator ir = columnsData.iterator();
        boolean firstColumn = true;
        OutputColumnData columnData;
        while (ir.hasNext()) {
            columnData = (OutputColumnData)ir.next();
            if (!firstColumn)
                writer.write(delimiter);
            else
                firstColumn = false;
            writer.write(columnData.getColumnName());
        }
        writer.newLine();
        writer.newLine();
        
        // write the data
        int numCols = columnsData.size();
        firstColumn = true;
        
        while (resultset.next()) {

            for (int i = 1; i <= numCols; i++) {
                if (!firstColumn)
                    writer.write(delimiter);
                else
                    firstColumn = false;
                String value = TextUtil.format(resultset, i, "");

                if (value.indexOf(delimiter) != -1)
                    value = "\""+value+"\"";
                
                writer.write(value);
            }
            
            recordCount++;
            writer.newLine();
            firstColumn = true;
        }
        
        writer.flush();
    }
}
