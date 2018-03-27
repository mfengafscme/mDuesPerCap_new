/*
 * MembershipActivityReportTest.java
 * JUnit based test
 *
 * Created on August 4, 2003, 3:50 PM
 */

package org.afscme.enterprise.reporting.specialized;

import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import junit.framework.*;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.reporting.specialized.MonthStateADRCount;
import org.afscme.enterprise.reporting.specialized.CouncilADRCount;
import org.afscme.enterprise.reporting.specialized.AffiliateADRCount;
import org.afscme.enterprise.reporting.specialized.MembershipActivityReportInfo;
import org.afscme.enterprise.reporting.specialized.MembershipActivityReport;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.codes.CodeData;
import java.io.*;

/**
 *
 * @author skhan
 */
public class MembershipActivityReportTest extends TestCase {
    
    public MembershipActivityReportTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MembershipActivityReportTest.class);
        return suite;
    }
    
    /** Test of generate method, of class org.afscme.enterprise.reporting.specialized.MembershipActivityReport. */
    public void testGenerate() {
        System.out.println("testGenerate");
        
        // Add your test code below by replacing the default call to fail.
        MembershipActivityReport mlrpt = new MembershipActivityReport();
        try {
            mlrpt.generate(new FileOutputStream(new File("c:\\test\\memebershipActivityReport.txt")));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Test of getMembershipActivityInfo method, of class org.afscme.enterprise.reporting.specialized.MembershipActivityReport. */
    public void testGetMembershipActivityInfo() {
        System.out.println("testGetMembershipActivityInfo");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    
}
