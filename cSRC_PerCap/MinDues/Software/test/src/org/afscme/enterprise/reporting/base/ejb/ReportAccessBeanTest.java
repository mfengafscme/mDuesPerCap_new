/*
 * ReportAccessBeanTest.java
 * JUnit based test
 *
 * Created on July 31, 2002, 3:35 PM
 */

package org.afscme.enterprise.reporting.base.ejb;

import junit.framework.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportOutputFieldData;
import org.afscme.enterprise.reporting.base.access.ReportSortFieldData;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;


public class ReportAccessBeanTest extends TestCase {
    
    public ReportAccessBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ReportAccessBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.reporting.base.ejb.ReportAccessBean. */
    public void testEjbCreate() {
        System.out.println("testEjbCreate");
        
        // Add your test code below by replacing the default call to fail.
        //fail("The test case is empty.");
    }
    
    /** Test of getReportFields method, of class org.afscme.enterprise.reporting.base.ejb.ReportAccessBean. */
    public void testGetReportFields() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetReportFields");
        
        // get the bean's home object and create a remote object for it.
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        // call the test target method.
        Map reportFields = reportAccess.getReportFields();
        
        // now we can delete the bean
        reportAccess.remove();
        
        // check the result.
        if (reportFields == null || reportFields.size() == 0)
            fail("Didn't get any fields");
        Iterator ir = reportFields.entrySet().iterator();
        Map.Entry currEntry;
        ReportField currField;
        Integer currPK;
        while (ir.hasNext()) {
            currEntry = (Map.Entry)ir.next();
            currPK = (Integer)currEntry.getKey();
            currField = (ReportField)(currEntry.getValue());
            System.out.println("******** Field pk = " + currPK.toString());
            System.out.println("printWidth = " + currField.getPrintWidth());
            System.out.println("entityType = " + currField.getEntityType());
            System.out.println("categoryName = " + currField.getCategoryName());
            System.out.println("tableName = " + currField.getTableName());
            System.out.println("columnName = " + currField.getColumnName());
            System.out.println("displayName = " + currField.getDisplayName());
            System.out.println("commonCodeTypeKey = " + currField.getCommonCodeTypeKey());
            if (currField.hasParent())
                fail("a child at top level!");
            if (currField.hasChildren()) {
                Set children = currField.getChildren();
                Iterator cir = children.iterator();
                while (cir.hasNext()) {
                    ReportField child = (ReportField)cir.next();
                    System.out.println("------------- child pk = " + child.getPk());
                    System.out.println("printWidth = " + child.getPrintWidth());
                }
                System.out.println("------------- end of a child -------");
            }
        }
            
    }
    
    /** Test of getReportData method, of class org.afscme.enterprise.reporting.base.ejb.ReportAccessBean. */
    /*
    public void testGetReportData() {
        System.out.println("testGetReportData");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
     */
    
    /** Test of getAllReports method, of class org.afscme.enterprise.reporting.base.ejb.ReportAccessBean. */
    public void testGetAllReports() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetAllReports");
        

        // get the bean's home object and create a remote object for it.
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        // call the target method
        List reports = reportAccess.getAllReports();
        
        // now we can delete the bean
        reportAccess.remove();
        
        // check the result
        ReportData reportData = null;
        if (reports != null) {
            Iterator ir = reports.iterator();
            while (ir.hasNext()) {
                reportData = (ReportData)ir.next();
                System.out.println("report name = " + reportData.getName());
                System.out.println("report desc = " + reportData.getDescription());
                System.out.println("reportLastUID = " + reportData.getLastUpdateUID());
                System.out.println("reportLastDate = " + reportData.getLastUpdateDate().toString());
            }
        }
        
        System.out.println("%%%%%%%   Done with 'getAllReports' test %%%%%%%% \n");
    }
    
    /** Test of getAllMailingReports method, of class org.afscme.enterprise.reporting.base.ejb.ReportAccessBean. */
    public void testGetAllMailingReports() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetAllMailingReports");
        

        // get the bean's home object and create a remote object for it.
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        // call the target method
        List reports = reportAccess.getAllMailingReports();
        
        // now we can delete the bean
        reportAccess.remove();
        
        // check the result
        ReportData reportData = null;
        if (reports != null) {
            Iterator ir = reports.iterator();
            while (ir.hasNext()) {
                reportData = (ReportData)ir.next();
                System.out.println("report name = " + reportData.getName());
                System.out.println("report desc = " + reportData.getDescription());
                System.out.println("reportLastUID = " + reportData.getLastUpdateUID());
                System.out.println("reportLastDate = " + reportData.getLastUpdateDate().toString());
                if (reportData.isMailingList()) System.out.println("This is a mailing list report");
                if (reportData.getCanAddEntities()) System.out.println("This report can pend");
                if (reportData.getNeedUpdateCorrespondence()) System.out.println("yes, need update corre");
            }
        }
        
        System.out.println("%%%%%%%   Done with 'getAllMailingReports' test %%%%%%%% \n");
    }    
   
    public void testGetReport() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\ntestGetReport\n"); 
        
        // get the bean's home object and create a remote object for it.
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        Report report = reportAccess.getReport(new Integer(1));
        
        reportAccess.remove();
        
        printReportData(report.getReportData());
        printReportOutputFields(report.getOutputFields());
        printReportSortFields(report.getSortFields());
        printReportCriteriaFields(report.getCriteriaFields());
        
        System.out.println("$$$$$$$  Done with 'getReport' test  $$$$$$$$$$$\n");
        
    }
    
    public void testCreateReport() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\ntestCreateReport\n");
        
        // get the bean's home object and create a remote object for it.
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        // create reportData object
        ReportData reportData = new ReportData();
        reportData.setName("CreatedTestReport");
        reportData.setDescription("A created test report");
        reportData.setCanAddEntities(true);
        reportData.setMailingList(false);
        reportData.setCustom(true);
        reportData.setNeedUpdateCorrespondence(false);
        reportData.setCustomHandlerClassName(null);
        reportData.setCountReport(false);
        reportData.setLastUpateDate(new Timestamp(new Date().getTime()));
        reportData.setLastUpdateUID("afscmeYing");
        reportData.setOwnerPK(new Integer(6)); // yqi

        Integer fieldPK;
        
        // create output fields
        Map outputFields = new HashMap();
        ReportOutputFieldData outputField;
        short outOrder = 1;
        for (int i = 3; i <= 5; i++) {
            fieldPK = new Integer(i); 
            outputField = new ReportOutputFieldData();
            outputField.setFieldPK(fieldPK);
            outputField.setOutputOrder(outOrder++);
            outputFields.put(fieldPK, outputField);
        }
        
        // create sort fields
        Map sortFields = new HashMap();
        ReportSortFieldData sortField;
        short sortOrder = 1;
        for (int i = 3; i <= 4; i++) {
            fieldPK = new Integer(i);
            sortField = new ReportSortFieldData();
            sortField.setFieldPK(fieldPK);
            sortField.setFieldSortOrder(sortOrder++);
            sortFields.put(fieldPK, sortField);
        }
        
        // create selection criteria fields
        Map criteriaFields = new HashMap();
        List fieldCriteria;
        ReportCriterionData criterion;
        fieldPK = new Integer(2);          // first field with only one criterion
        fieldCriteria = new ArrayList();
        criterion = new ReportCriterionData();
        criterion.setFieldPK(fieldPK);
        criterion.setCriterionSequence(1);
        criterion.setOperator("LT");
        criterion.setValue1("100");
        criterion.setEditable(true);
        fieldCriteria.add(criterion);
        criteriaFields.put(fieldPK, fieldCriteria);
        fieldPK = new Integer(3);          // second field with two criteria
        fieldCriteria = new ArrayList(); 
        criterion = new ReportCriterionData();      //--- first criterion
        criterion.setFieldPK(fieldPK);
        criterion.setCriterionSequence(1);
        criterion.setOperator("GE");
        criterion.setValue1("50");
        criterion.setEditable(true);
        fieldCriteria.add(criterion);
        criterion = new ReportCriterionData();       //--- second criterion
        criterion.setFieldPK(fieldPK);
        criterion.setCriterionSequence(2);
        criterion.setOperator("BT");
        criterion.setValue1("30");
        criterion.setValue2("40");
        criterion.setEditable(false);
        fieldCriteria.add(criterion);
        criteriaFields.put(fieldPK, fieldCriteria);
        fieldPK = new Integer(4);          // third field with two code values
        fieldCriteria = new ArrayList();
        criterion = new ReportCriterionData();      //*** first criterion
        criterion.setFieldPK(fieldPK);
        criterion.setCriterionSequence(1);
        criterion.setOperator("IN");
        criterion.setEditable(true);
        criterion.setCodeField(true);
        criterion.setCodePK(new Integer(2001));
        fieldCriteria.add(criterion);
        criterion = new ReportCriterionData();      //*** second criterion
        criterion.setFieldPK(fieldPK);
        criterion.setCriterionSequence(2);
        criterion.setOperator("IN");
        criterion.setEditable(true);
        criterion.setCodeField(true);
        criterion.setCodePK(new Integer(2002));
        fieldCriteria.add(criterion);
        criteriaFields.put(fieldPK, fieldCriteria);
            
        // create a report object and set all
        Report report = new Report();
        report.setReportData(reportData);
        report.setOutputFields(outputFields);
        report.setSortFields(sortFields);
        report.setCriteriaFields(criteriaFields);
        
        // call the Bean method to create the report
        ReportData newReportData = reportAccess.createReport(report);
        System.out.println("The newly created reportPK = " + newReportData.getPk());
        
        
        // get the whole report again and print it.
        Report newReport = reportAccess.getReport(newReportData.getPk());
        
        printReportData(newReport.getReportData());
        printReportOutputFields(newReport.getOutputFields());
        printReportSortFields(newReport.getSortFields());
        printReportCriteriaFields(newReport.getCriteriaFields());
        
        
        reportAccess.remove();
        
        System.out.println("$$$$$$  Done with 'createReport' test $$$$$$$$$$\n");
    }
    
    public void testDeleteReport() throws NamingException, CreateException, RemoveException, FinderException {
        System.out.println("\nDelete a report test\n");
        
        // get the bean's home object and create a remote object for it.
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        /*
        reportAccess.deleteReport(new Integer(14));
        */
        
        reportAccess.remove();
        
    }
    
    
    private void printReportData(ReportData reportData) {
        System.out.println("report name = " + reportData.getName());
        System.out.println("report desc = " + reportData.getDescription());
        System.out.println("pend_fg = " + reportData.getCanAddEntities());
        System.out.println("mailing_fg = " + reportData.isMailingList());
        System.out.println("custom_fg = " + reportData.isCustom());
        System.out.println("update_corr_fg = " + reportData.getNeedUpdateCorrespondence());
        System.out.println("Custom handler class = " + reportData.getCustomHandlerClassName());
        System.out.println("count_fg = " + reportData.isCountReport());
        System.out.println("reportLastDate = " + reportData.getLastUpdateDate().toString());
        System.out.println("reportLastDate = " + reportData.getLastUpdateUID());
        System.out.println("reportOwnerPK = " + reportData.getOwnerPK().intValue());
    } 
    
    private void printReportOutputFields(Map outputFields) {
        Iterator ir = outputFields.values().iterator();
        
        while (ir.hasNext()) {
            ReportOutputFieldData outputField = (ReportOutputFieldData)ir.next();
            System.out.println("fieldPK = " + outputField.getFieldPK().intValue());
            System.out.println("output order = " + outputField.getOutputOrder());
        }
    }
    
    private void printReportSortFields(Map sortFields) {
        Iterator ir = sortFields.values().iterator();
        
        while (ir.hasNext()) {
            ReportSortFieldData sortField = (ReportSortFieldData)ir.next();
            System.out.println("fieldPK = " + sortField.getFieldPK().intValue());
            System.out.println("sort order = " + sortField.getFieldSortOrder());
            System.out.println("sort direction = " + sortField.getFieldSortDirection());
        }
    }
    
    private void printReportCriteriaFields(Map criteriaFields) {
        Map.Entry field;
        List fieldCriteria;
        ReportCriterionData criterion;
        Iterator ir2;
        
        Iterator ir1 = criteriaFields.entrySet().iterator();
        while (ir1.hasNext()) {
            field = (Map.Entry)ir1.next();
            System.out.println("FieldPK = " + ((Integer)field.getKey()).intValue());
            fieldCriteria = (List)field.getValue();
            ir2 = fieldCriteria.iterator();
            while (ir2.hasNext()) {
                criterion = (ReportCriterionData)ir2.next();
                System.out.println("    criterion sequence# = " + criterion.getCriterionSequence());
                System.out.println("    operator = " + criterion.getOperator());
                System.out.println("    editable_fg = " + criterion.isEditable());
                System.out.println("    value1 = " + criterion.getValue1());
                System.out.println("    value2 = " + criterion.getValue2());
                System.out.println("    isCodeField = " + criterion.isCodeField());
                if (criterion.isCodeField())
                    System.out.println("    com_codePK = " + criterion.getCodePK().intValue());
                else
                    System.out.println("    com_codePK = " + criterion.getCodePK());
                
                System.out.println("    **************\n");

            }
            
            System.out.println("****** done with one field ******\n");
        }
    }
    
    
}
