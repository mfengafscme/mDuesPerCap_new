/*
 * BaseReportBeanTest.java
 * JUnit based test
 *
 * Created on August 9, 2002, 5:43 PM
 */

package org.afscme.enterprise.reporting.base.ejb;

import junit.framework.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.apache.fop.apps.FOPException;
import org.afscme.enterprise.reporting.base.access.OutputColumnData;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.access.ReportOutputFieldData;
import org.afscme.enterprise.reporting.base.access.ReportSortFieldData;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.reporting.base.generator.PDFFormatter;
import org.afscme.enterprise.reporting.base.email.EmailGenerator;
import org.afscme.enterprise.reporting.base.email.ReportEmailData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;

/**
 *
 * @author yqi
 */
public class BaseReportBeanTest extends TestCase {
    
    public BaseReportBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BaseReportBeanTest.class);
        
        return suite;
    }
    
    /** Test the DelimitedTextFormatter */
   public void testGenerateReportText() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\n**** Start 'GenerateReport TEXT' test ******\n");
                
        // get the bean's home object and create a remote object for it.
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        
        String uid = "yqi";
        Integer reportPK = new Integer(4);
        MediaType media = null;
        OutputFormat format = new OutputFormat(OutputFormat.COMMA);
        
        baseReport.generateReport(uid, null, reportPK, media, format);
        
        System.out.println("\n**** End 'geneateReport TEXT' test ******\n");
        
        baseReport.remove();
        
    }
    /** Test the LabelFormatter */
    public void testGenerateReportLabel() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\n**** Start 'GenerateReport LABEL' test ******\n");
        
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        
        String uid = "yqi";
        Integer reportPK = new Integer(5);
        MediaType media = null;
        OutputFormat format = new OutputFormat(OutputFormat.LABEL);
        
        baseReport.generateReport(uid, null, reportPK, media, format);

        System.out.println("\n**** End 'geneateReport LABEL' test ******\n");
        
        baseReport.remove(); 
    }
    
    /** Test the PDFFormatter */
    public void testGenerateReportPDF() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\n**** Start 'GenerateReport PDF' test ******\n");
        
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        
        String uid = "yqi";
        Integer reportPK = new Integer(5);
        MediaType media = new MediaType(MediaType.CD);
        OutputFormat format = new OutputFormat(OutputFormat.PDF);
        
        baseReport.generateReport(uid, null, reportPK, media, format);

        System.out.println("\n**** End 'geneateReport PDF' test ******\n");
        
        baseReport.remove(); 
    } 
    
    /** Test getAllReportsForUser */
    public void testGetAllReportsForUser() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\n  Start testing get all reports for user ");
        
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        
        Integer userPK = new Integer(7);
        List reports = baseReport.getAllReportsForUser(userPK);
        
        if (reports != null) {
            Iterator it = reports.iterator();
            ReportData reportData;
            while (it.hasNext()) {
                reportData = (ReportData)it.next();
                System.out.println("---- Report Name = " + reportData.getName());
                System.out.println("---- Report ownerPK = " + reportData.getOwnerPK());
            }
        }
        
        System.out.println("\n End testing get all reports for user ");
    }
    
    /** Test getAllRegularReportsForUser */
    public void testGetRegularReportsForUser() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\n  Start testing get all regular reports for user ");
        
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        
        Integer userPK = new Integer(7);
        List reports = baseReport.getRegularReportsForUser(userPK);
        
        if (reports != null) {
            Iterator it = reports.iterator();
            ReportData reportData;
            while (it.hasNext()) {
                reportData = (ReportData)it.next();
                System.out.println("---- Report Name = " + reportData.getName());
                System.out.println("---- Report ownerPK = " + reportData.getOwnerPK());
            }
        }
        
        System.out.println("\n End testing get all regular reports for user ");
    }        
    
    /** Test getAllMailingReportsForUser */
    public void testGetMailingReportsForUser() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\n  Start testing get all mailing reports for user ");
        
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        
        Integer userPK = new Integer(7);
        List reports = baseReport.getMailingListReportsForUser(userPK);
        
        if (reports != null) {
            Iterator it = reports.iterator();
            ReportData reportData;
            while (it.hasNext()) {
                reportData = (ReportData)it.next();
                System.out.println("---- Report Name = " + reportData.getName());
                System.out.println("---- Report ownerPK = " + reportData.getOwnerPK());
            }
        }
        
        System.out.println("\n End testing get all mailing reports for user ");
    } 
}
