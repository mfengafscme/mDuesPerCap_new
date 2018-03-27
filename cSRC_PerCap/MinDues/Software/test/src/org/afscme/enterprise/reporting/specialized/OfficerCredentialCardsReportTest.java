
package org.afscme.enterprise.reporting.specialized;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import junit.framework.*;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import java.io.*;

/**
 *
 * @author skhan
 */
public class OfficerCredentialCardsReportTest extends TestCase {

    public OfficerCredentialCardsReportTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(OfficerCredentialCardsReportTest.class);
        return suite;
    }

    /** Test of generate method, of class org.afscme.enterprise.reporting.specialized.OfficerCredentialCardsReport. */
    public void testGenerate() {
        System.out.println("testGenerate");

        // Add your test code below by replacing the default call to fail.
        OfficerCredentialCardsReport mlrpt = new OfficerCredentialCardsReport();
        mlrpt.setFromDate("2000-01-01");
        mlrpt.setToDate("2004-01-01");
        try {
            mlrpt.generate(new FileOutputStream(new File("c:\\test\\OfficerCredentialCardsReport.txt")));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Test of getMailingListId method, of class org.afscme.enterprise.reporting.specialized.OfficerCredentialCardsReport. */
    public void testGetMailingListId() {
        System.out.println("testGetMailingListId");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }

    /** Test of setMailingListId method, of class org.afscme.enterprise.reporting.specialized.OfficerCredentialCardsReport. */
    public void testSetMailingListId() {
        System.out.println("testSetMailingListId");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }

    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}


}
