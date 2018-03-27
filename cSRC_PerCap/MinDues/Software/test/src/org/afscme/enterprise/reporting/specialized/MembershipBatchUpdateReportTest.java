
package org.afscme.enterprise.reporting.specialized;

import junit.framework.*;
import org.afscme.enterprise.reporting.ReportHandler;
import java.io.*;

/**
 *
 * @author skhan
 */
public class MembershipBatchUpdateReportTest extends TestCase {

    public MembershipBatchUpdateReportTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MembershipBatchUpdateReportTest.class);
        return suite;
    }

    /** Test of generate method, of class org.afscme.enterprise.reporting.specialized.MembershipBatchUpdateReport. */
    public void testGenerate() {
        System.out.println("testGenerate");

        // Add your test code below by replacing the default call to fail.
        MembershipBatchUpdateReport mburpt = new MembershipBatchUpdateReport();
        try {
            mburpt.	generate(new FileOutputStream(new File("c:\\test\\MembershipBatchUpdateReport.txt")));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
