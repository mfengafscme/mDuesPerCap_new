/*
 * SystemLogTest.java
 * JUnit based test
 *
 * Created on September 4, 2002, 3:47 PM
 */

package org.afscme.enterprise.log;

import junit.framework.*;

import java.sql.Timestamp;
import java.util.Date;
import org.afscme.enterprise.log.SystemLog;


public class SystemLogTest extends TestCase {
    
    public SystemLogTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(SystemLogTest.class);
        return suite;
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    public void testLogMailingListGenerated() {
        System.out.println("Log Mailing List Generated");
        SystemLog.logMailingListGenerated("Log Test Mailing List", "Label", "afscmeUID", new Timestamp(new Date().getTime()));
    }
    
    public void testLogReportGenerated() {
        System.out.println("Log Report Generated");
        SystemLog.logReportGenerated("Test Report", "PDF", "afscmeUID", new Timestamp(new Date().getTime()));
    }
    
    public void testLogAFLCIOExtractGenerated() {
        System.out.println("Log AFLCIO Extract Generated");
        long gtime = new Date().getTime();
        SystemLog.logAFLCIOExtractGenerated(new Timestamp(gtime), new Timestamp(gtime+100), new Timestamp(gtime+200), "AFLCIOExtract", 67, "afscmeUID");
    }
    
    public void testLogAFLCIOExtractSent() {
        System.out.println("Log AFLCIO Extract Sent");
        long stime = new Date().getTime();
        SystemLog.logAFLCIOExtractSent(500, new Timestamp(stime), 350, "http://www.afscme.org/");
    }
    
    public void testLogAffiliateDataProcessed() {
        System.out.println("Log Affiliate Data Processed");
        SystemLog.logAffiliateDataProcessed("affiliateName", new Timestamp(new Date().getTime()), 900, 324);
    }
    
    public void testLogAffiliateFileError() {
        System.out.println("Log Affiliate File Error");
        SystemLog.logAffiliateFileError("affiliate name", "affiliate file error", "AffiliateError", new Timestamp(new Date().getTime()));
    }
    
    public void testLogUpdateApplied() {
        System.out.println("Log Update Applied");
        SystemLog.logUpdateApplied(2, 5, 1);
    }
    
    public void testLogUpdateCancelled() {
        System.out.println("Log Update Cancelled");
        SystemLog.logUpdateCancelled(4, 7, 1, "UpdateFileName");
    }
    
    public void testLogApplyUpdateError() {
        System.out.println("Log Update Error");
        SystemLog.logApplyUpdateError("UpdateFile", "apply update error reason");
    }
    
    public void testLogRecordUpdateError() {
        System.out.println("Log Record Update Error");
        SystemLog.logRecordUpdateError("File Name", new Timestamp(new Date().getTime()), "Record Update Error Description");
    }
    
    public void testLogDataMatchingPerformed() {
        System.out.println("Log Data Matching Performed");
        SystemLog.logDataMatchingPerformed("Aff Name", 8, 0, new Timestamp(new Date().getTime()), 134);
    }
    
    public void testLogApplicationError() {
        System.out.println("Log Application Error");
        Exception exp = new Exception("Just Testing Exception");
        SystemLog.logApplicationError(exp, "http://localhost/", "afscmeUID");
    }
    

}
