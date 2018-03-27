package org.afscme.enterprise.affiliate.officer.ejb;

import java.util.Collection;
import junit.framework.*;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.officer.*;
import org.afscme.enterprise.affiliate.officer.ejb.*;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.officer.*;
import org.afscme.enterprise.util.JNDIUtil;

public class MaintainAffiliateOfficersBeanTest extends TestCase {
    
    private static MaintainAffiliateOfficers affOfficerBean;
    static {
        try {
            affOfficerBean = JNDIUtil.getMaintainAffiliateOfficersHome().create();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    private static MaintainAffiliates affilBean;
    static {
        try {
            affilBean = JNDIUtil.getMaintainAffiliatesHome().create();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
          
    private static final Integer AFF_PK = new Integer(1);
    private static final Integer USER_PK = new Integer("10000001");
    private static final Integer AFSCME_OFFICE_PK = new Integer("1");
    private static final Integer OFFICE_PK = new Integer("3");
    private static final Boolean TRUE = new Boolean(true);
    private static final Boolean FALSE = new Boolean(false);
    
    public MaintainAffiliateOfficersBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainAffiliateOfficersBeanTest.class);
        return suite;
    }
    
    /** Test of addOfficerTitle method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testaddOfficerTitle() {
               
        OfficeData data = new OfficeData();
        data.setAffPk(new Integer(1));
        data.setOfficePk(new Integer(1));
        data.setAffiliateTitle(null);
        data.setNumWithTitle(new Integer(1));
        data.setMonthOfElection(new Integer(1));
        data.setLengthOfTerm(new Integer(1));
        data.setTermEnd(new Integer(2004));
        data.setDelegatePriority(new Integer(1));
        data.setReportingOfficer(FALSE);
        data.setExecBoard(FALSE);
        
        int val = affOfficerBean.addOfficerTitle(data, USER_PK);
        System.out.println("testAddOfficerTitle method result: " + val);
        
    }
    
    /** Test of getAFSCMEOfficerTitles method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
    public void testgetAFSCMEOfficerTitles() {
               
        Collection results = affOfficerBean.getAFSCMEOfficerTitles();
        if (results == null) {
            fail("No Titles found");
        }
        else { 
            System.out.println("Number of titles returned: " + results.size());
            
        }
              
    }
    
    /** Test of getCommentHistoryForOfficerTitles method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
    public void testgetCommentHistoryForOfficerTitles() {
               
        Collection CommentData = affOfficerBean.getCommentHistoryForOfficerTitles(AFF_PK);
        if (CommentData == null) {
            fail("No comments found");
        }
        else { 
            System.out.println(CommentData);
        }
              
    }
    
    /** Test of getCommentForOfficerTitles method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
    public void testgetCommentForOfficerTitles() {
               
        CommentData data = affOfficerBean.getCommentForOfficerTitles(AFF_PK);
        if (data == null) {
            fail("No comments found");
        }
        else { 
            System.out.println("Comment: " + data.getComment());
        }
              
    }
    
    /** Test of getOfficerHistory method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
    public void testgetOfficerHistory() {
               
        Collection OfficerData = affOfficerBean.getOfficerHistory(AFF_PK);
        if (OfficerData == null) {
            fail("No officer history records found");
        }
        else { 
            System.out.println("Officer titles returned: " + OfficerData.size());
        }
              
    }
    
    /** Test of getOfficerTitles method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
//    public void testgetOfficerTitles() {
//               
//        Collection OfficeData = affOfficerBean.getOfficerTitles(AFF_PK, sortData);
//        if (OfficeData == null) {
//            fail("No officer history records found");
//        }
//        else { 
//            System.out.println("Officer titles returned: " + OfficeData.size());
//        }
//              
//    }
    
    /** Test of getOfficers method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
    public void testgetOfficers() {
               
        Collection OfficerData = affOfficerBean.getOfficers(AFF_PK);
        if (OfficerData == null) {
            fail("No officers found");
        }
        else { 
            System.out.println(OfficerData);
        }
              
    }
    
    /** Test of testremoveOfficerTitle method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
    public void testremoveOfficerTitle() {
        
        System.out.println("start testremoveOfficerTitle");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        affOfficerBean.removeOfficerTitle(AFF_PK, OFFICE_PK, AFSCME_OFFICE_PK);
  
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testremoveOfficerTitle");
    }
    
    /** Test of setVacantOffice method, of class org.afscme.enterprise.affiliate.officer.ejb.MaintainAffiliateOfficersBean. */
//    public void testsetVacantOfficer() {
//             
//        System.out.println("start testsetVacantOfficer");
//        System.out.println(" ");System.out.println(" ");System.out.println(" ");
//       
//        affOfficerBean.setVacantOfficer(new Integer(1),  new Integer(3));
//        
//        System.out.println(" ");System.out.println(" ");System.out.println(" ");
//        System.out.println("end testsetVacantOfficer");
//              
//    }
}

