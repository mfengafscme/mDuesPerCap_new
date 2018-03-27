/*
 * ReportFormatter.java
 *
 * Created on August 20, 2003, 5:43 PM
 */

package org.afscme.enterprise.reporting.base.generator;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author  kvogel
 */
public interface ReportFormatter {

    public void readData(ResultSet rs)  throws IOException, SQLException;
    public int formatReport() throws Exception;

}
