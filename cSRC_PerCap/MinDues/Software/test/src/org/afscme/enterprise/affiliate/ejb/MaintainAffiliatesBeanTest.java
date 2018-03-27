/*
 * MaintainAffiliatesBeanTest.java
 * JUnit based test
 *
 * Created on April 28, 2003, 9:20 AM
 */

package org.afscme.enterprise.affiliate.ejb;

import junit.framework.*;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.organization.*;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.codes.ejb.*;
import org.afscme.enterprise.affiliate.ejb.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 *
 * @author frusso
 */
public class MaintainAffiliatesBeanTest extends TestCase {
    
    private static MaintainAffiliates affilBean;
    static {
        try {
            affilBean = JNDIUtil.getMaintainAffiliatesHome().create();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    private static Integer TEST_PK = null;
    
    private static final Integer USER_PK = new Integer("10000001");
    private static final Boolean TRUE = new Boolean(true);
    private static final Boolean FALSE = new Boolean(false);
    
    public MaintainAffiliatesBeanTest(String testName) {
        super(testName);
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainAffiliatesBeanTest.class);
        return suite;
    }
    
    /** Test of addAffiliate method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testAddAffiliate() {
        System.out.println("start testAddAffiliate");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        NewAffiliate aff = new NewAffiliate();
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('C'), "0", "VA", "0", "100");
        aff.setAffiliateId(affId);
        aff.setAffiliateName("Test Affiliate from Add 1");
        aff.setAffiliateRegionCodePk(new Integer(61001));
        aff.setAffiliateStatusCodePk(new Integer(17002));
        aff.setAfscmeLegislativeDistrict(new Integer(21018));
        aff.setAllowSubLocals(TRUE);
        aff.setAnnualCardRunTypeCodePk(new Integer(25001));
        aff.setCharterDate(new Timestamp(new java.util.Date().getTime()));
        aff.setMultipleEmployers(TRUE);

        int pk = affilBean.addAffiliate(aff, USER_PK); 
        if (pk < 1) {
            fail("add failed. pk returned wasn't valid.");
        } else {
            TEST_PK = new Integer(pk);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testAddAffiliate");
    }
    
    /** Test of addLocalsServiced method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testAddLocalsServiced() {
        System.out.println("testAddLocalsServiced");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of addOfficerTitle method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testAddOfficerTitle() {
        System.out.println("testAddOfficerTitle");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAffiliatedAdminCouncil method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetAffiliatedAdminCouncil() {
        System.out.println("testGetAffiliatedAdminCouncil");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAffiliatedCouncils method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetAffiliatedCouncils() {
        System.out.println("testGetAffiliatedCouncils");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAffiliatedDistrictCouncil method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetAffiliatedDistrictCouncil() {
        System.out.println("testGetAffiliatedDistrictCouncil");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAffiliateData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetAffiliateData() {
        System.out.println("start testGetAffiliateData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('C'), "0", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        System.out.println("Retrieving Affiliate with pk = " + pk);
        AffiliateData data = affilBean.getAffiliateData(pk);
        if (data == null) {
            fail("Affiliate Data was null.");
        } else {
            System.out.println("+++++++++ Affiliate Data retrieved successfully: " + data);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetAffiliateData");
    }
    
    /** Test of getAffiliatePk method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetAffiliatePk()  {
        System.out.println("start testGetAffiliatePk");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('C'), "0", "VA", "0", "1000", new Character('A'), null);
        
        System.out.println("+++++++++ Pk retrieved is: " + affilBean.getAffiliatePk(affId));
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetAffiliatePk");
    }
    
    /** Test of getAffiliateHierarchy method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetAffiliateHierarchy() {
        System.out.println("start testGetAffiliateHierarchy");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        List hierarchy = affilBean.getAffiliateHierarchy(pk);
        if (CollectionUtil.isEmpty(hierarchy)) {
            fail("No hierarchy found for pk = " + pk);
        } 
        AffiliateHierarchyEntry entry = null;
        for (Iterator it = hierarchy.iterator(); it.hasNext(); ) {
            entry = (AffiliateHierarchyEntry)it.next();
            System.out.println(entry);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetAffiliateHierarchy");
    }
    
    /** Test of getAFSCMEOfficeTitles method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetAFSCMEOfficeTitles() {
        System.out.println("testGetAFSCMEOfficeTitles");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAutoEBoardOfficers method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetAutoEBoardOfficers() {
        System.out.println("testGetAutoEBoardOfficers");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAutoEBoardTitleData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetAutoEBoardTitleData() {
        System.out.println("testGetAutoEBoardTitleData");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getChangeHistoryData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetChangeHistoryData() {
        System.out.println("testGetChangeHistoryData");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getCharterData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetCharterData() {
        System.out.println("start testGetCharterData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        CharterData data = affilBean.getCharterData(pk);
        if (data != null) {
            System.out.println("Charter Data found for Affiliate with pk =  " + pk);
            System.out.println("Charter Data = " + data.toString());
        } else {
            fail("Charter Data NOT found for Affiliate with pk = " + TEST_PK);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetCharterData");
    }
    
    /** Test of getCommentForAffiliate method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetCommentForAffiliate() {
        System.out.println("start testGetCommentForAffiliate");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "1000", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        if (pk == null) {
            fail("Could not find Affiliate with ID = " + affId.toString());
        }
        CommentData cd = affilBean.getCommentForAffiliate(pk);
        if (cd == null || TextUtil.isEmptyOrSpaces(cd.getComment())) {
            fail("Could not find recent Comment for Affiliate with Pk = " + pk);
        }
        System.out.println("***** Comment found: " + cd.toString());
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetCommentForAffiliate");
    }
    
    /** Test of getCommentForOfficerTitles method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetCommentForOfficerTitles() {
        System.out.println("testGetCommentForOfficerTitles");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getCommentHistoryForAffiliate method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetCommentHistoryForAffiliate() {
        System.out.println("start testGetCommentHistoryForAffiliate");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "1000", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        if (pk == null) {
            fail("Could not find Affiliate with ID = " + affId.toString());
        }
        Collection c = affilBean.getCommentHistoryForAffiliate(pk);
        if (c == null) {
            fail("Could not find Comments for Affiliate with Pk = " + pk);
        }
        System.out.println("Found " + c.size() + " comment(s).");
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            CommentData cd = (CommentData)it.next();
            System.out.println("    Comment value: " + cd.toString());
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetCommentHistoryForAffiliate");
    }
    
    /** Test of getCommentHistoryForOfficerTitles method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetCommentHistoryForOfficerTitles() {
        System.out.println("testGetCommentHistoryForOfficerTitles");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getConstitutionData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetConstitutionData() {
        System.out.println("start testGetConstitutionData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        ConstitutionData data = affilBean.getConstitutionData(pk);
        if (data != null) {
            System.out.println("Constitution Data found for Affiliate with pk =  " + pk);
            System.out.println("Constitution Data = " + data.toString());
        } else {
            fail("Constitution Data NOT found for Affiliate with pk = " + pk);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetConstitutionData");
    }
   
    /** Test of getEmployerSectors method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetEmployerSectors() {
        System.out.println("start testGetEmployerSectors");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "1000", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        if (pk == null) {
            fail("Could not find Affiliate with ID = " + affId.toString());
        }
        
        Collection c = affilBean.getEmployerSectors(pk);
        if (c == null) {
            fail("Could not find Employer Sectors for Affiliate with Pk = " + pk);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetEmployerSectors");
    }
    
    /** Test of getFinancialData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetFinancialData() {
        System.out.println("start testGetFinancialData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        FinancialData data = affilBean.getFinancialData(pk);
        if (data != null) {
            System.out.println("Financial Data found for Affiliate with pk =  " + pk);
            System.out.println("Financial Data = " + data.toString());
        } else {
            fail("Financial Data NOT found for Affiliate with pk = " + pk);
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetFinancialData");
    }
    
    /** Test of getMembershipReportingData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetMembershipReportingData() {
        System.out.println("testGetMembershipReportingData");
        
        affilBean.getMembershipReportingData(new Integer(1));
    }
    
    /** Test of getOfficerHistory method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetOfficerHistory() {
        System.out.println("testGetOfficerHistory");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getOfficers method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetOfficers() {
        System.out.println("testGetOfficers");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getOfficerTitles method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testGetOfficerTitles() {
        System.out.println("testGetOfficerTitles");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getParentAffiliate method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetParentAffiliate() {
        System.out.println("start testGetParentAffiliate");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        AffiliateData data = affilBean.getParentAffiliate(pk);
        if (data == null) {
            fail("No Parent found for pk = " + pk);
        } else {
            System.out.println("Parent found for Affiliate with pk =  " + pk);
            System.out.println("Parent Data = " + data.toString());
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetParentAffiliate");
    }
    
    /** Test of findParentCriteria method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testFindParentCriteria() {
        System.out.println("start testFindParentCriteria");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        Collection parents = affilBean.findParentCriteria(affId);
        if (CollectionUtil.isEmpty(parents)) {
            fail("No Parents found for id = " + affId.toString());
        } else {
            System.out.println("Parents found for Affiliate with id =  " + affId.toString());
            System.out.println("Number of parents = " + parents.size());
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testFindParentCriteria");
    }
    
    /** Test of getAffiliateSubHierarchy method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testGetAffiliateSubHierarchy() {
        System.out.println("start testGetAffiliateSubHierarchy");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('C'), "0", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        List subAffils = affilBean.getAffiliateSubHierarchy(pk);
        if (subAffils == null || subAffils.size() < 1) {
            fail("No Affiliate found with pk = " + pk);
        } else if (subAffils.size() == 1) {
            fail("No Sub Affiliates found for Affiliate with pk = " + pk);
        } else {
            System.out.println("Affiliate with pk = " + pk + " has a SubHierarchy with " + subAffils.size() + " items.");
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetAffiliateSubHierarchy");
    }
    
    /** Test of isValidAffiliate method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testIsValidAffiliate() {
        System.out.println("start testIsValidAffiliate");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        if (affilBean.isValidAffiliate(pk)) {
            System.out.println("Affiliate with pk = " + pk + " was NOT valid. ********** CORRECT!!!");
        } else {
            fail("Affiliate with pk = " + pk + " was valid. ********** INCORRECT!!!");
        }
        affId = new AffiliateIdentifier(new Character('L'), "2000", "VA", "0", "1000", new Character('B'), null);
        pk = affilBean.getAffiliatePk(affId);
        if (!affilBean.isValidAffiliate(pk)) {
            System.out.println("Affiliate with pk = " + pk + " was NOT valid. ********** CORRECT!!!");
        } else {
            fail("Affiliate with pk = " + pk + " was valid. ********** INCORRECT!!!");
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testIsValidAffiliate");
    }
    
    /** Test of removeAffiliatedCouncil method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testRemoveAffiliatedCouncil() {
        System.out.println("testRemoveAffiliatedCouncil");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of removeLocalsServiced method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testRemoveLocalsServiced() {
        System.out.println("testRemoveLocalsServiced");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of removeOfficerTitle method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testRemoveOfficerTitle() {
        System.out.println("testRemoveOfficerTitle");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }*/
    
    /** Test of recordChangeToHistory(AffiliateData, AffiliateData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testRecordChangeToHistoryAffiliateData() {
        System.out.println("start testRecordChangeToHistoryAffiliateData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateData oldData = new AffiliateData();
        AffiliateData newData = new AffiliateData();
        oldData.setAffPk(new Integer(1));
        newData.setAffPk(oldData.getAffPk());
        
        oldData.setAbbreviatedName("Old Name");
        newData.setAbbreviatedName("New Name");
        oldData.setAfscmeRegionCodePk(new Integer(61001));
        newData.setAfscmeRegionCodePk(new Integer(61002));
        oldData.setAllowSubLocals(FALSE);
        newData.setAllowSubLocals(TRUE);
        oldData.setAnnualCardRunTypeCodePk(new Integer(25001));
        newData.setAnnualCardRunTypeCodePk(new Integer(25003));
        oldData.setEmployerSector(null);
        Collection c = new ArrayList();
        c.add(new Integer(20001));
        newData.setEmployerSector(c);
        oldData.setMultipleEmployers(FALSE);
        newData.setMultipleEmployers(TRUE);
        oldData.setMultipleOffices(FALSE);
        newData.setMultipleOffices(TRUE);
        oldData.setMemberRenewalCodePk(null);
        newData.setMemberRenewalCodePk(new Integer(23001));
        oldData.setWebsite(null);
        newData.setWebsite("newsite.com");
        
        affilBean.recordChangeToHistory(oldData, newData, USER_PK);
        affilBean.recordChangeToHistory(newData, oldData, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryAffiliateData");
    }
    
    /** Test of recordChangeToHistory(CharterData, CharterData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testRecordChangeToHistoryCharterData() {
        System.out.println("start testRecordChangeToHistoryCharterData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        CharterData oldData = new CharterData();
        CharterData newData = new CharterData();
        
        oldData.setAffPk(new Integer(1));
        newData.setAffPk(oldData.getAffPk());
        // code
        oldData.setCharterCodeCodePk(new Integer(18001));
        newData.setCharterCodeCodePk(new Integer(18002));
        // date
        oldData.setCharterDate(Timestamp.valueOf("2000-04-01 00:00:00.000000000"));
        newData.setCharterDate(Timestamp.valueOf("2000-05-01 00:00:00.000000000"));
        // jurisdiction
        oldData.setJurisdiction("Old Jurisdiction");
        newData.setJurisdiction("New Jurisdiction");
        // name
        oldData.setName("Old Name");
        newData.setName("New Name");
        // council affiliations
        // counties
        oldData.setCounties(null);
        Collection c = new ArrayList();
        c.add("Fairfax");
        c.add("Various");
        newData.setCounties(c);
        // effective date
        oldData.setLastChangeEffectiveDate(Timestamp.valueOf("2003-04-01 00:00:00.000000000"));
        newData.setLastChangeEffectiveDate(Timestamp.valueOf("2003-05-01 00:00:00.000000000"));
        
        affilBean.recordChangeToHistory(oldData, newData, USER_PK);
        affilBean.recordChangeToHistory(newData, oldData, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryCharterData");
    }
    
    /** Test of recordChangeToHistory(ConstitutionData, ConstitutionData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testRecordChangeToHistoryConstitutionData() {
        System.out.println("start testRecordChangeToHistoryConstitutionData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        ConstitutionData oldData = new ConstitutionData();
        ConstitutionData newData = new ConstitutionData();
        
        oldData.setAffPk(new Integer(1));
        newData.setAffPk(oldData.getAffPk());
        
        //Affiliation Agreement Date
        oldData.setAffiliationAgreementDate(DateUtil.getTimestamp("05/01/2000"));
        newData.setAffiliationAgreementDate(DateUtil.getTimestamp("05/01/2003"));
        
        //Constitutional Regions
        oldData.setConstitutionalRegions(FALSE);
        newData.setConstitutionalRegions(TRUE);
        //Meeting Frequency
        oldData.setMeetingFrequencyCodePk(new Integer(50001));
        newData.setMeetingFrequencyCodePk(new Integer(50003));
        //Method Of Officer Election
        oldData.setMethodOfOfficerElectionCodePk(new Integer(51001));
        newData.setMethodOfOfficerElectionCodePk(new Integer(51002));
        //Most Current Approval Date
        oldData.setMostCurrentApprovalDate(DateUtil.getTimestamp("06/01/2000"));
        newData.setMostCurrentApprovalDate(DateUtil.getTimestamp("06/01/2003"));
        
        affilBean.recordChangeToHistory(oldData, newData, USER_PK);
        affilBean.recordChangeToHistory(newData, oldData, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryConstitutionData");
    }
    
    /** Test of recordChangeToHistory(FinancialData, FinancialData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testRecordChangeToHistoryFinancialData() {
        System.out.println("start testRecordChangeToHistoryFinancialData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        FinancialData oldData = new FinancialData();
        FinancialData newData = new FinancialData();
        
        oldData.setAffPk(new Integer(1));
        newData.setAffPk(oldData.getAffPk());
        
        //Affiliation Agreement Date
        oldData.setEmployerIDNumber(null);
        newData.setEmployerIDNumber("New Employer ID Number");
        
        affilBean.recordChangeToHistory(oldData, newData, USER_PK);
        affilBean.recordChangeToHistory(newData, oldData, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryFinancialData");
    }
    
    /** Test of recordChangeToHistory(LocationData, LocationData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testRecordChangeToHistoryLocationData() {
        System.out.println("start testRecordChangeToHistoryLocationData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        LocationData oldData = new LocationData();
        LocationData newData = new LocationData();
        
        oldData.setOrgPk(new Integer(1));
        newData.setOrgPk(oldData.getOrgPk());
        
        // TITLE_OF_LOCATION
        newData.setLocationNm("new Location Title");
        // PRIMARY_LOCATION_FLAG
        newData.setPrimaryLocationBoolean(TRUE);
        
        LinkedList addresses = new LinkedList();
        
        OrgAddressRecord address = new OrgAddressRecord();
        address.setType(org.afscme.enterprise.codes.Codes.OrgAddressType.REGULAR);
        // MAIN_ATTENTION
        address.setAttentionLine("New Attention");
        // MAIN_ADDRESS_1
        address.setAddr1("New Address 1");
        // MAIN_ADDRESS_2
        address.setAddr2("New Address 2");
        // MAIN_CITY
        address.setCity("New City");
        // MAIN_STATE
        address.setState("10011");
        // MAIN_ZIP_CODE
        address.setZipCode("11111");
        // MAIN_ZIP_4
        address.setZipPlus("1111");
        // MAIN_COUNTY
        address.setCounty("New County");
        // MAIN_PROVINCE
        address.setProvince("New Province");
        // MAIN_COUNTRY
        address.setCountryPk(new Integer("9001"));
        // MAIN_BAD_ADDRESS_FLAG
        address.setBad(true);
        // MAIN_DATE_MARKED_BAD
        address.setBadDate(DateUtil.getCurrentDateAsTimestamp());
        addresses.add(address);
        
        address = new OrgAddressRecord();
        address.setType(org.afscme.enterprise.codes.Codes.OrgAddressType.SHIPPING);
        // SHIP_ATTENTION
        address.setAttentionLine("New Ship Attention");
        // SHIP_ADDRESS_1
        address.setAddr1("New Ship Address 1");
        // SHIP_ADDRESS_2
        address.setAddr2("New Ship Address 2");
        // SHIP_CITY
        address.setCity("New Ship City");
        // SHIP_STATE
        address.setState("10012");
        // SHIP_ZIP_CODE
        address.setZipCode("22222");
        // SHIP_ZIP_4
        address.setZipPlus("2222");
        // SHIP_COUNTY
        address.setCounty("New Ship County");
        // SHIP_PROVINCE
        address.setProvince("New Ship Province");
        // SHIP_COUNTRY
        address.setCountryPk(new Integer("9002"));
        // SHIP_BAD_ADDRESS_FLAG
        address.setBad(true);
        // SHIP_DATE_MARKED_BAD
        address.setBadDate(DateUtil.getCurrentDateAsTimestamp());
        addresses.add(address);
        
        LinkedList phones = new LinkedList();
        OrgPhoneData phone = new OrgPhoneData();
        phone.setPhoneType(org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_OFFICE);
        // OFFICE_COUNTRY_CODE
        phone.setCountryCode("1");
        // OFFICE_AREA_CODE
        phone.setAreaCode("703");
        // OFFICE_PHONE_NUMBER
        phone.setPhoneNumber("1111111");
        // OFFICE_BAD_PHONE_FLAG
        phone.setPhoneBadFlag(TRUE);
        // OFFICE_DATE_MARKED_BAD
        phone.setPhoneBadDate(DateUtil.getCurrentDateAsTimestamp());
        phones.add(phone);
        
        phone = new OrgPhoneData();
        phone.setPhoneType(org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_FAX);
        // FAX_COUNTRY_CODE
        phone.setCountryCode("1");
        // FAX_AREA_CODE
        phone.setAreaCode("202");
        // FAX_PHONE_NUMBER
        phone.setPhoneNumber("2222222");
        // FAX_BAD_PHONE_FLAG
        phone.setPhoneBadFlag(TRUE);
        // FAX_DATE_MARKED_BAD
        phone.setPhoneBadDate(DateUtil.getCurrentDateAsTimestamp());
        phones.add(phone);
        
        newData.setOrgAddressData(addresses);
        newData.setOrgPhoneData(phones);
        
        affilBean.recordChangeToHistory(oldData, newData, USER_PK);
        affilBean.recordChangeToHistory(newData, oldData, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryLocationData");
    }
    
    /** Test of recordChangeToHistory(MRData, MRData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testRecordChangeToHistoryMRData() {
        System.out.println("start testRecordChangeToHistoryMRData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        MRData oldData = new MRData();
        MRData newData = new MRData();
        
        oldData.setAffPk(new Integer(1));
        newData.setAffPk(oldData.getAffPk());
        
        //oldData.getAffiliateStatusCodePk()
        oldData.setAffStatus(new Integer(17002));
        newData.setAffStatus(new Integer(17007));
        
        //oldData.getMbrshpInfoRptgSrcCodePk() 
        oldData.setInformationSource(new Integer(47001));
        newData.setInformationSource(new Integer(47002));
        
        //oldData.getNewAffiliateID()
        oldData.setNewAffiliateId(new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000"));
        newData.setNewAffiliateId(new AffiliateIdentifier(new Character('L'), "200", "DC", "0", "2000"));
        
        //oldData.getUnitWideNoMbrCard()
        oldData.setNoCards(false);
        newData.setNoCards(true);
        
        //oldData.getUnitWideNoPEMail()
        oldData.setNoPEMail(false);
        newData.setNoPEMail(true);
        
        affilBean.recordChangeToHistory(oldData, newData, USER_PK);
        affilBean.recordChangeToHistory(newData, oldData, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryMRData");
    }
    
    /** Test of recordChangeToHistory(NewAffiliate, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testRecordChangeToHistoryNewAffiliate() {
        System.out.println("start testRecordChangeToHistoryNewAffiliate");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryNewAffiliate");
    }
    
    /** Test of recordChangeToHistory(OfficeData, OfficeData, Integer) method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testRecordChangeToHistoryOfficeData() {
        System.out.println("start testRecordChangeToHistoryOfficeData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testRecordChangeToHistoryOfficeData");
    }
    
    /** Test of searchAffiliates method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testSearchAffiliates1() {
        System.out.println("start testSearchAffiliates1");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        // @TODO: add calls to getSearchAffiliatesCount...
        AffiliateCriteria criteria = new AffiliateCriteria();

        criteria.setAffiliateStatusCodePk(null);
        criteria.setAffiliateIdCouncil(null);
        criteria.setAffiliateIdState("VA");
        criteria.setAffiliateIdType(new Character('L'));
        criteria.setAffiliateIdLocal("2000");
        criteria.setAffiliateIdSubUnit(null);
        criteria.setAfscmeLegislativeDistrictCodePk(null);
        criteria.setAfscmeRegionCodePk(null);
        criteria.setAllowSubLocals(TRUE);
        criteria.setEmployerSectorCodePk(null);
        criteria.setIncludeSubUnits(TRUE);
        criteria.setMultipleEmployers(TRUE);
        criteria.setMultipleOffices(TRUE);
        criteria.setNewAffiliateIdentifierSourceType(new Character('L'));
        criteria.setNewAffiliateIdentifierSourceLocal("1000");
        criteria.setNewAffiliateIdentifierSourceState("VA");
        criteria.setNewAffiliateIdentifierSourceSubUnit(null);
        criteria.setNewAffiliateIdentifierSourceCouncil(null);
        criteria.setNewAffiliateIdentifierSourceCode(null);
        criteria.setWebsite("http://a.b.com");
        Collection results = affilBean.searchAffiliates(criteria);
        if (results == null) {
            fail("--------- TEST FAILED: search results was null");
        } else if (results.size() == 0) {
            fail("--------- TEST FAILED: search results was empty");
        }
        System.out.println("+++++++++ TEST PASSED: Resulting pk's are: ");
        for (Iterator it = results.iterator(); it.hasNext(); ) {
            AffiliateResult result = (AffiliateResult)it.next();
            System.out.println("    " + result.getAffPk().toString());
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testSearchAffiliates1");
    }
    
    /** Test of searchAffiliates method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testSearchAffiliates2() {
        System.out.println("start testSearchAffiliates2");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        // @TODO: add calls to getSearchAffiliatesCount...
        AffiliateCriteria criteria = new AffiliateCriteria();

        criteria.setAffiliateStatusCodePk(null);
        criteria.setAffiliateIdType(new Character('L'));
        criteria.setAffiliateIdLocal("100");
        criteria.setAffiliateIdState("OR");
        criteria.setAffiliateIdSubUnit(null);
        criteria.setAffiliateIdCouncil(null);
        criteria.setAffiliateIdCode(null);
        criteria.setAfscmeLegislativeDistrictCodePk(null);
        criteria.setAfscmeRegionCodePk(null);
        criteria.setAllowSubLocals(null);
        criteria.setEmployerSectorCodePk(null);
        criteria.setIncludeSubUnits(null);
        criteria.setMultipleEmployers(null);
        criteria.setMultipleOffices(null);
        criteria.setNewAffiliateIdentifierSourceCode(null);
        criteria.setNewAffiliateIdentifierSourceCouncil(null);
        criteria.setNewAffiliateIdentifierSourceLocal(null);
        criteria.setNewAffiliateIdentifierSourceState(null);
        criteria.setNewAffiliateIdentifierSourceSubUnit(null);
        criteria.setNewAffiliateIdentifierSourceType(null);
        criteria.setWebsite(null);
        Collection results = affilBean.searchAffiliates(criteria);
        if (results == null) {
            fail("--------- TEST FAILED: search results was null");
        } else if (results.size() == 0) {
            fail("--------- TEST FAILED: search results was empty");
        }
        System.out.println("+++++++++ TEST PASSED: Resulting pk's are: ");
        for (Iterator it = results.iterator(); it.hasNext(); ) {
            AffiliateResult result = (AffiliateResult)it.next();
            System.out.println("    " + result.getAffPk().toString());
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testSearchAffiliates2");
    }
    
    /** Test of searchAffiliates method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testSearchAffiliates3() {
        System.out.println("start testSearchAffiliates3");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        // @TODO: add calls to getSearchAffiliatesCount...
        AffiliateCriteria criteria = new AffiliateCriteria();

        criteria.setAffiliateStatusCodePk(null);
        criteria.setAffiliateIdType(new Character('C'));
        criteria.setAffiliateIdLocal(null);
        criteria.setAffiliateIdState("VA");
        criteria.setAffiliateIdSubUnit(null);
        criteria.setAffiliateIdCouncil("100");
        criteria.setAffiliateIdCode(null);
        criteria.setAfscmeLegislativeDistrictCodePk(null);
        criteria.setAfscmeRegionCodePk(null);
        criteria.setAllowSubLocals(null);
        criteria.setEmployerSectorCodePk(null);
        criteria.setIncludeSubUnits(null);
        criteria.setMultipleEmployers(null);
        criteria.setMultipleOffices(null);
        criteria.setNewAffiliateIdentifierSourceCode(null);
        criteria.setNewAffiliateIdentifierSourceCouncil(null);
        criteria.setNewAffiliateIdentifierSourceLocal(null);
        criteria.setNewAffiliateIdentifierSourceState(null);
        criteria.setNewAffiliateIdentifierSourceSubUnit(null);
        criteria.setNewAffiliateIdentifierSourceType(null);
        criteria.setWebsite(null);
        Collection results = affilBean.searchAffiliates(criteria);
        if (results == null) {
            fail("--------- TEST FAILED: search results was null");
        } else if (results.size() == 0) {
            fail("--------- TEST FAILED: search results was empty");
        }
        System.out.println("+++++++++ TEST PASSED: Resulting pk's are: ");
        for (Iterator it = results.iterator(); it.hasNext(); ) {
            AffiliateResult result = (AffiliateResult)it.next();
            System.out.println("    " + result.getAffPk().toString());
        }
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testSearchAffiliates3");
    }
    
    /** Test of searchChangeHistory method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testSearchChangeHistory() {
        System.out.println("testSearchChangeHistory");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateChangeCriteria acc = new AffiliateChangeCriteria();
        acc.setAffPk(TEST_PK);
        acc.setChangeDateFrom(DateUtil.getCurrentDateAsTimestamp());
        Collection results = affilBean.searchChangeHistory(acc);
        if (results == null) {
            fail("Change History Search was null.");
        } else {
            System.out.println("Change History Search returned " + results.size() + " item(s).");
            for (Iterator it = results.iterator(); it.hasNext(); ) {
                AffiliateChangeResult acr = (AffiliateChangeResult)it.next();
                System.out.println("    " + acr.toString());
            }
        }
    }
    
    /** Test of setAffiliatedCouncil method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testSetAffiliatedCouncil() {
        System.out.println("testSetAffiliatedCouncil");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of updateAffiliateData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testUpdateAffiliateData() {
        System.out.println("start testUpdateAffiliateData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateData ad = affilBean.getAffiliateData(TEST_PK);
        ad.setAbbreviatedName("UPDATED AFFILIATE FROM TEST");
        ad.setAfscmeRegionCodePk(new Integer(61004));
        ad.setAllowSubLocals(FALSE);
        ad.setAnnualCardRunTypeCodePk(new Integer(25003));
        ad.setMultipleEmployers(FALSE);
        ad.setMultipleOffices(FALSE);
        ad.setWebsite("http://test.com");
        ad.setComment("Testing update");
        RecordData record = new RecordData();
        record.setCreatedBy(USER_PK);
        record.setModifiedBy(USER_PK);
        record.setCreatedDate(DateUtil.getCurrentDateAsTimestamp());
        record.setModifiedDate(DateUtil.getCurrentDateAsTimestamp());
        affilBean.updateAffiliateData(ad, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testUpdateAffiliateData");
    }
    
    /** Test of updateCharterData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testUpdateCharterData() {
        System.out.println("start testUpdateCharterData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        CharterData data = affilBean.getCharterData(pk);
        Collection counties = new ArrayList(3);
        counties.add("Loudon");
        counties.add("Fairfax");
        counties.add("Fauquier");
        data.setCounties(counties);
        data.setJurisdiction("Jurisdiction from Test cases.");
        data.setLastChangeEffectiveDate(DateUtil.getCurrentDateAsTimestamp());
        data.setName("Name from Test cases.");
        affilBean.updateCharterData(data, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testUpdateCharterData");
    }
    
    /** Test of updateFinancialData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testUpdateFinancialData() {
        System.out.println("start testUpdateFinancialData");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        AffiliateIdentifier affId = new AffiliateIdentifier(new Character('L'), "100", "VA", "0", "1000", new Character('A'), null);
        Integer pk = affilBean.getAffiliatePk(affId);
        
        FinancialData data = affilBean.getFinancialData(pk);
        data.setEmployerIDNumber("111111111");
        affilBean.updateFinancialData(data, USER_PK);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testUpdateFinancialData");
    }
    
    /** Test of updateMembershipReportingData method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    public void testUpdateMembershipReportingData() {
        System.out.println("testUpdateMembershipReportingData");
        
        MRData mrData = affilBean.getMembershipReportingData(new Integer(1));
        mrData.setNoPEMail(true);
        affilBean.updateMembershipReportingData(new Integer(1), mrData, new Integer(10000001));
    }
    
    /** Test of updateOfficers method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testUpdateOfficers() {
        System.out.println("testUpdateOfficers");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of updateOfficerTitles method, of class org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesBean. */
    /*public void testUpdateOfficerTitles() {
        System.out.println("testUpdateOfficerTitles");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }*/
    
    
}
