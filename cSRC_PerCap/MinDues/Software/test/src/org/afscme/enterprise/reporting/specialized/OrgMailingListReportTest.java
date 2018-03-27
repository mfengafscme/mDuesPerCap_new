
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
public class OrgMailingListReportTest extends TestCase {

    public OrgMailingListReportTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(OrgMailingListReportTest.class);
        return suite;
    }

    /** Test of generate method, of class org.afscme.enterprise.reporting.specialized.OrgMailingListReport. */
    public void testGenerate() {
        System.out.println("testGenerate");

        // Add your test code below by replacing the default call to fail.
        OrgMailingListReport mlrpt = new OrgMailingListReport();
        mlrpt.setMailingListId(1);
        try {
            mlrpt.generate(new FileOutputStream(new File("c:\\test\\OrgMailingListReport.txt")));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Test of getMailingListId method, of class org.afscme.enterprise.reporting.specialized.OrgMailingListReport. */
    public void testGetMailingListId() {
        System.out.println("testGetMailingListId");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }

    /** Test of setMailingListId method, of class org.afscme.enterprise.reporting.specialized.OrgMailingListReport. */
    public void testSetMailingListId() {
        System.out.println("testSetMailingListId");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }

    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}


}
