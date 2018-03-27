
package org.afscme.enterprise.reporting.specialized;
import junit.framework.*;
import org.afscme.enterprise.reporting.ReportHandler;
import java.io.*;

/**
 *
 * @author skhan
 */
public class EnrichedDataReportTest extends TestCase {

    public EnrichedDataReportTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EnrichedDataReportTest.class);
        return suite;
    }

    /** Test of generate method, of class org.afscme.enterprise.reporting.specialized.EnrichedDataReport. */
    public void testGenerate() {
        System.out.println("testGenerate");

        // Add your test code below by replacing the default call to fail.
        EnrichedDataReport edrpt = new EnrichedDataReport();
        edrpt.setAffPk(new Integer(1));
        try {
            edrpt.generate(new FileOutputStream(new File("c:\\test\\EnrichedDataReport.txt")));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Test of getMailingListId method, of class org.afscme.enterprise.reporting.specialized.EnrichedDataReport. */
    public void testGetAffPk() {
        System.out.println("testGetAffPk");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }

    /** Test of setMailingListId method, of class org.afscme.enterprise.reporting.specialized.EnrichedDataReport. */
    public void testAffPk() {
        System.out.println("testAffPk");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
}
