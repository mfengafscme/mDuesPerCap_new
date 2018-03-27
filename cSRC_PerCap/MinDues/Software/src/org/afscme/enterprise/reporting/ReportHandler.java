package org.afscme.enterprise.reporting;

import java.io.OutputStream;
import java.io.Serializable;
import org.afscme.enterprise.reporting.base.access.Report;

/**
 * Interface implemented by ReportGenerator (for the custom querries),
 * and all the specialized report handlers
 */
public interface ReportHandler extends Serializable {
        
    /**
     * Generate the report to the given output stream.
     *
     * @return The count of items generated.  This may not always be meaningful for specialized reports.
     */
    public int generate(OutputStream out) throws Exception;
    
    /**
     * Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     */
    public String getFileName();
}
