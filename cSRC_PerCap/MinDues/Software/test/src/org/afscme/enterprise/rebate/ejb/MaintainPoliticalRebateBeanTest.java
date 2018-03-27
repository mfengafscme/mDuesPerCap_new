/*
 * MaintainPoliticalRebateBeanTest.java
 * JUnit based test
 *
 * Created on July 22, 2003, 3:26 PM
 */

package org.afscme.enterprise.rebate.ejb;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.rmi.RemoteException;
import javax.ejb.*;
import junit.framework.*;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.rebate.PRB12MonthRebateAmount;
import org.apache.log4j.Logger;
import org.afscme.enterprise.rebate.*;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.person.PRBCheckInfo;
import org.afscme.enterprise.person.PRBRequestData;
import org.afscme.enterprise.person.PRBApplicationData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.codes.Codes.RebateAcceptanceCode;
import org.afscme.enterprise.codes.Codes.RebateAppEvalCode;


/**
 *
 * @author hmaiwald
 */
public class MaintainPoliticalRebateBeanTest extends TestCase {
    
    // This is a set of Political Rebate test data
    protected static Integer personPk = new Integer(10000002);
    protected static Integer prbPk = new Integer(2);
    
     
    public MaintainPoliticalRebateBeanTest(java.lang.String testName) {
        super(testName);
    }
        
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainPoliticalRebateBeanTest.class);        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testEjbCreate() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testEjbCreate: Creating MaintainPoliticalRebateBean bean.");
        if (prbBean == null)
            fail("MaintainPoliticalRebate was null.");
        prbBean.remove();
    }    
    
    /** Test of getPRBAffiliatesCount method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBAffiliatesCount() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBAffiliatesCount: Retrieves the total count of affiliates associated with a person's rebate request.");
        int rs = prbBean.getPRBAffiliatesCount(personPk, prbPk.intValue(), PRBConstants.PRB_REQUEST);
        System.out.println("PRB Request: Total # of Affiliates associate with personPk (" + personPk + ") and rqstPk (" + prbPk + ") is " + rs);
        
        System.out.println("testGetPRBAffiliatesCount: Retrieves the total count of affiliates associated with a person's rebate application.");
        rs = prbBean.getPRBAffiliatesCount(personPk, prbPk.intValue(), PRBConstants.PRB_APPLICATION);
        System.out.println("PRB Application: Total # of Affiliates associate with personPk (" + personPk + ") and appPk (" + prbPk + ") is " + rs);
        prbBean.remove();        
    }
    
    /** Test of getPRBAffiliates method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBAffiliates() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBAffiliates: Retrieves the affiliates associated with a person's rebate request.");
        List affList = prbBean.getPRBAffiliates(personPk, prbPk.intValue(), PRBConstants.PRB_REQUEST);
        System.out.println("PRB Request: The Affiliates associate with personPk (" + personPk + ") and rqstPk (" + prbPk + ") is " + affList);
        
        System.out.println("testGetPRBAffiliates: Retrieves the affiliates associated with a person's rebate application.");
        affList = prbBean.getPRBAffiliates(personPk, prbPk.intValue(), PRBConstants.PRB_APPLICATION);
        System.out.println("PRB Application: The Affiliates associate with personPk (" + personPk + ") and rqstPk (" + prbPk + ") is " + affList);
        prbBean.remove();        
    }
    
    /** Test of setPRBAffiliate method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testSetPRBAffiliate() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        // Setup test record
        PRBAffiliateData prbAffData = new PRBAffiliateData();
        RecordData recData = new RecordData();
        recData.setPk(personPk);
        prbAffData.setAcceptanceCodePk(RebateAcceptanceCode.D);
        prbAffData.setAffPk(new Integer(1));
        prbAffData.setTheRecordData(recData);
        
        // Update PRB Request Affiliate association 
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testSetPRBAffiliate: Updates an association between a person's political rebate request and an affiliate.");
        int rs = prbBean.setPRBAffiliate(prbAffData, prbPk, PRBConstants.PRB_REQUEST);
        if (rs == 0) {
            fail("Unable to update the association between a person's political rebate request and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
        } else {
            System.out.println("Successfully update the association between a person's political rebate request and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
        }

        // Update PRB Application Affiliate association 
        System.out.println("testSetPRBAffiliate: Updates an association between a person's political rebate application and an affiliate.");
        rs = prbBean.setPRBAffiliate(prbAffData, prbPk, PRBConstants.PRB_APPLICATION);
        prbBean.remove();        
        if (rs == 0) {
            fail("Unable to update the association between a person's political rebate application and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
        } else {
            System.out.println("Successfully update the association between a person's political rebate application and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
        }
    }
    
    /** Test of createPRBAffiliate method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testCreatePRBAffiliate() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testCreatePRBAffiliate: Creates an association between a person's political rebate request and an affiliate.");

        // Setup test record
        int rs;
        PRBAffiliateData prbAffData = new PRBAffiliateData();
        RecordData recData = new RecordData();
        recData.setPk(personPk);
        prbAffData.setAcceptanceCodePk(RebateAcceptanceCode.D);
        prbAffData.setAffPk(new Integer(6));
        prbAffData.setTheRecordData(recData);        
        
        if (prbBean.getPRBAffiliatesCount(personPk, prbPk.intValue(), PRBConstants.PRB_REQUEST) == 0) {
            rs = prbBean.createPRBAffiliate(prbAffData, prbPk, PRBConstants.PRB_REQUEST);
            if (rs == 0) {
                fail("Unable to create the association between a person's political rebate request and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
            } else {
                System.out.println("Successfully create the association between a person's political rebate request and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
            }
        } else {
            System.out.println("The association between a person's political rebate request and an affiliate for personPk="+personPk+" and Affiliate Data="+prbAffData+" already exists.");
        }
        
        if (prbBean.getPRBAffiliatesCount(personPk, prbPk.intValue(), PRBConstants.PRB_APPLICATION) == 0) {
            rs = prbBean.createPRBAffiliate(prbAffData, prbPk, PRBConstants.PRB_APPLICATION);
            if (rs == 0) {
                fail("Unable to create the association between a person's political rebate application and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
            } else {
                System.out.println("Successfully create the association between a person's political rebate application and an affiliate for personPk=" + personPk + " and Affiliate Data=" + prbAffData);
            }
        } else {
            System.out.println("The association between a person's political rebate application and an affiliate for personPk="+personPk+" and Affiliate Data="+prbAffData+" already exists.");
        }
        prbBean.remove();        
    }
    
    /** Test of removePRBAffiliate method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testRemovePRBAffiliate() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        boolean rs;
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testRemovePRBAffiliate: Removes the association of an afffiliate with a person's rebate request.");        

        if (prbBean.getPRBAffiliatesCount(personPk, prbPk.intValue(), PRBConstants.PRB_REQUEST) == 0) {
            rs = prbBean.removePRBAffiliate(prbPk, new Integer(6), PRBConstants.PRB_REQUEST);
            if (rs) {
                System.out.println("Successfully remove the association between a person's political rebate request and an affiliate for rqstPk=" + prbPk + " and affPk=6.");
            } else {
                fail("Unable to remove the association between a person's political rebate request and an affiliate for rqstPk=" + prbPk + " and affPk=6.");
            }
        } else {
            System.out.println("Unable to remove the association between a person's political rebate request and an affiliate for rqstPk=" + prbPk + " and affPk=6 because it does not exist.");            
        }
        
        System.out.println("testRemovePRBAffiliate: Removes the association of an afffiliate with a person's rebate application.");
        if (prbBean.getPRBAffiliatesCount(personPk, prbPk.intValue(), PRBConstants.PRB_APPLICATION) == 0) {
            rs = prbBean.removePRBAffiliate(prbPk, new Integer(6), PRBConstants.PRB_APPLICATION);
            prbBean.remove();        
            if (rs) {
                System.out.println("Successfully remove the association between a person's political rebate application and an affiliate for appPk=" + prbPk + " and affPk=6.");
            } else {
                fail("Unable to remove the association between a person's political rebate application and an affiliate for appPk=" + prbPk + " and affPk=6.");
            }
        } else {
            System.out.println("Unable to remove the association between a person's political rebate application and an affiliate for rqstPk=" + prbPk + " and affPk=6 because it does not exist.");            
        }
    }
        
    /** Test of getPRBRequest method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBRequest() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBRequest: Retrieves the data associated with a political rebate request.");
        PRBRequestData rqstData = prbBean.getPRBRequest(personPk, prbPk.intValue());
        prbBean.remove();        
        if (rqstData == null) {
            fail("Unable to retrieve the political rebate request for (personPk="+personPk+" and requestPk="+prbPk+")");
        } else {
            System.out.println("The political rebate request for (personPk="+personPk+" and requestPk="+prbPk+") is "+rqstData);
        }
    }
    
    /** Test of getPRBRequests method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBRequests() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBRequests: Retrieves information on Political Rebate requests made for this person for a given year.");                
        List list = prbBean.getPRBRequests(personPk, "2000");
        prbBean.remove();        
        if (list==null)
            fail("Unable to retrieve the political rebate requests for (personPk="+personPk+" and rebate year=2000)");
        else
            System.out.println("The political rebate requests for (personPk="+personPk+" and rebate year=2000) is "+list);
    }
    
    /** Test of addPRBRbtYearInfo method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testAddPRBRbtYearInfo() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testAddPRBRbtYearInfo: Adds a political rebate year to the database, for a person, at a given rebate year.");
        PRBRequestData prbRequestData = new PRBRequestData();
        prbRequestData.setPersonPk(personPk.intValue());
        prbRequestData.setRqstRebateYear("2003");
        prbRequestData.setCommentsTxt("Unit Test: MaintainPoliticalRebateBeanTest.testAddPRBRbtYearInfo() ..");        
        prbBean.addPRBRbtYearInfo(prbRequestData);
        prbBean.remove();        
    }
    
    /** Test of addPRBRequest method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testAddPRBRequest() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testAddPRBRequest: Adds a political rebate request to the database, for a person, at a given date (and referencing a rebate year)");
        PRBRequestData prbRequestData = new PRBRequestData();
        prbRequestData.setPersonPk(personPk.intValue());
        prbRequestData.setRqstRebateYear("2003");
        prbRequestData.setRqstDt(DateUtil.getCurrentDateAsTimestamp());
        prbRequestData.setRqstKeyedDt(DateUtil.getCurrentDateAsTimestamp());
        prbRequestData.setRqstDeniedFg(new Boolean(false));
        prbRequestData.setRqstResubmitFg(new Boolean(false));
        prbRequestData.setCommentsTxt("Unit Test: MaintainPoliticalRebateBeanTest.testAddPRBRequest() ..");        
        Integer rqstPk = prbBean.addPRBRequest(prbRequestData, new Integer(10000001));
        
        prbBean.remove();        
        if (rqstPk == null) {
            fail("Unable to add the new political rebate request to the database with Request Data="+prbRequestData);
        } else {
            System.out.println("The new record for political rebate request is created successfully with RequestPk="+rqstPk);
        }
    }
    
    /** Test of updatePRBRequest method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRBRequest() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRBRequest: Allows the Political Rebate Request data for a person to be updated");
        PRBRequestData prbRequestData = new PRBRequestData();
        prbRequestData.setPersonPk(personPk.intValue());
        prbRequestData.setRqstRebateYear("2000");
        prbRequestData.setRqstDt(DateUtil.getCurrentDateAsTimestamp());
        prbRequestData.setRqstKeyedDt(DateUtil.getCurrentDateAsTimestamp());
        prbRequestData.setRqstDeniedFg(new Boolean(false));
        prbRequestData.setRqstResubmitFg(new Boolean(false));
        prbRequestData.setCommentsTxt("Unit Test: MaintainPoliticalRebateBeanTest.testUpdatePRBRequest() ..");        
        prbBean.updatePRBRequest(prbRequestData, new Integer(10000001));
        prbBean.remove();        
        System.out.println("Update political rebate request (Request Data="+prbRequestData+")");
    }
    
    /** Test of getPRBAppMailedDate method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBAppMailedDate() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBAppMailedDate: Retrieves the political rebate application mailed date.");        
        Timestamp ts = prbBean.getPRBAppMailedDate(prbPk.intValue());
        prbBean.remove();        
        System.out.println("The PRB Application Mailed Date for AppPk"+prbPk+" is "+ts);        
    }
        
    /** Test of getPRBApplication method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBApplication() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBApplication: Retrieves the data associated with a political rebate application.");        
        PRBApplicationData appData = prbBean.getPRBApplication(personPk, prbPk);
        prbBean.remove();        
        System.out.println("The political rebate application for (personPk="+personPk+" and appPk="+prbPk+") is "+appData);        
    }
    
    /** Test of getPRBApplications method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBApplications() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBApplications: Retrieves information on Political Rebate Application made for this person for a given year.");
        List list = prbBean.getPRBApplications(personPk, "2000");
        prbBean.remove();        
        if (list==null)
            fail("Unable to get the list of political rebate application for (personPk="+personPk+" and rebate year=2000)");
        else
            System.out.println("Political rebate applications for (personPk="+personPk+" and rebate year=2000) is "+list);                    
    }
    
    /** Test of updatePRBApplication method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRBApplication() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRBApplication: Updates the data associated with a Political Rebate application.");
        PRBApplicationData appData = new PRBApplicationData();
        appData.setPersonPK(personPk);
        appData.setPrbAppPK(prbPk);
        appData.setPrbEvaluationCd(RebateAppEvalCode.QB);
        appData.setCommentTxt("Unit Test: MaintainPoliticalRebateBean.testUpdatePRBApplication()");
        prbBean.updatePRBApplication(appData, new Integer(10000001));
        prbBean.remove();        
        System.out.println("Updated political rebate application for (political rebate application data="+appData+")");                    
    }
    
    /** Test of updatePRBApplicationStatus method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRBApplicationStatus() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRBApplicationStatus: Updates the status associated with a Political Rebate application.");
        prbBean.updatePRBApplicationStatus(prbPk, new Integer(10000001));
        prbBean.remove();        
        System.out.println("Updated political rebate application status for (applicationPk="+prbPk+")");                            
    }
    
    /** Test of getPersonPRBSummaryByYear method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPersonPRBSummaryByYear() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPersonPRBSummaryByYear: Retrieves a political rebate list of the all the years in which an individual has applied for a Rebate.");
        List list = prbBean.getPersonPRBSummaryByYear(personPk);
        prbBean.remove();        
        if (list==null)
            fail("Unable to get the summary list of political rebate year for (personPk="+personPk+")");
        else
            System.out.println("The summary list of political rebate year for (personPk="+personPk+") is "+list);                                
    }
    
    /** Test of getRebateStatus method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetRebateStatus() throws Exception {        
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetRebateStatus: Retrieves a political rebate status for an individual who has applied for a Rebate based on the rebate year.");
        String status = prbBean.getRebateStatus(personPk, new Integer(2000));
        prbBean.remove();        
        System.out.println("The political rebate status for personPk="+personPk+" and rebate year=2000) is "+status);                                        
    }
    

    /** Test of getAnnualRebateStatus method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetAnnualRebateStatus() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetAnnualRebateStatus: This method calculates the Annual Rebate status based on the Roster Status, the Acceptance Code " +
                           "of each Affiliate, and the Check Number if at least one of the Affiliates approved for " +
                           "an individual who has applied for a Rebate based on the rebate year.");
        
        String status = prbBean.getAnnualRebateStatus(personPk, new Integer(2000));
        prbBean.remove();        
        System.out.println("The political rebate annual rebate status for personPk="+personPk+" and rebate year=2000) is "+status);                                        
    }
    
    /** Test of getRosterStatus method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetRosterStatus() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetRosterStatus: Retrieves the Roster Status of all Affiliate for an individual based on the rebate year.");
        String status = prbBean.getRosterStatus(personPk, new Integer(2000));
        prbBean.remove();        
        System.out.println("The roster status for personPk="+personPk+" and rebate year=2000) is "+status);                                        
    }
    
    /** Test of getPRBAnnualRebateAffiliates method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBAnnualRebateAffiliates() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBAnnualRebateAffiliates: Retrieves the Affiliates associated with the political rebate annual rebate information for a person based on the rebate year.");
        List list = prbBean.getPRBAnnualRebateAffiliates(personPk, new Integer(2000));
        prbBean.remove();        
        if (list==null)
            fail("Unable to get the list of affiliates associated with the political rebate annual rebate for (personPk="+personPk+" in rebate year=2000)");
        else
            System.out.println("The list of affiliates associated with the political rebate annual rebate for (personPk="+personPk+" in rebate year=2000) is "+list);                                
    }
    
    /** Test of updatePRBRosterPerson method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRBRosterPerson() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRBRosterPerson: Updates data associated with Roster Person.");
        PRBAffiliateData prbAffData = new PRBAffiliateData();
        RecordData recData = new RecordData();
        recData.setModifiedBy(new Integer(10000001));
        recData.setPk(personPk);        
        prbAffData.setAcceptanceCodePk(RebateAcceptanceCode.D);
        prbAffData.setAffPk(new Integer(1));
        prbAffData.setTheRecordData(recData);        
        boolean rs = prbBean.updatePRBRosterPerson(prbAffData, new Integer(2000));
        prbBean.remove();        
        if (rs)
            System.out.println("Successfully updated the affiliate associated with the political rebate annual rebate. Affiliate Data is: "+ prbAffData);
        else
            fail("Unable to update the affiliate associated with the political rebate annual rebate in rebate year=2000. Affiliate Data is: " + prbAffData);
    }
    
    /** Test of getPRBCheckInfo method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBCheckInfo() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBCheckInfo: Retrieves political rebate check information for a person based on the rebate year.");
        PRBCheckInfo checkInfo = prbBean.getPRBCheckInfo(personPk, new Integer(2002));
        prbBean.remove();        
        if (checkInfo==null)
            fail("Unable to retrieve rebate check information for (personPk="+personPk+" in rebate year=2000)");
        else
            System.out.println("The rebate check information for (personPk="+personPk+" in rebate year=2000) is "+checkInfo);
    }
    
    /** Test of updatePRBCheckInfo method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRBCheckInfo() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRBCheckInfo: Update political rebate check information for a person based on the rebate year.");        
        PRBCheckInfo checkInfo = new PRBCheckInfo();
        checkInfo.setComment("Unit Test: MaintainPoliticalRebateBeanTest.testUpdatePRBCheckInfo() ..");
        checkInfo.setPersonPK(personPk);
        checkInfo.setRebateYear(new Integer(1999));
        checkInfo.setCheckNumber(new Integer(234));
        boolean rs = prbBean.updatePRBCheckInfo(checkInfo, new Integer(10000001));
        prbBean.remove();        
        if (rs)
            System.out.println("Successfully update the rebate check information for (Rebate Check Data="+checkInfo+")");
        else 
            fail("Unable to update the rebate check information for (Rebate Check Data="+checkInfo+")");
    }
        
    /** Test of removeAffiliateFromAnnualRebate method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testRemoveAffiliateFromAnnualRebate() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testRemoveAffiliateFromAnnualRebate: Removes the association of an afffiliate with a person's annual rebate information.");

        List list = prbBean.getPRBAnnualRebateAffiliates(personPk, new Integer(1999));
        if (list!=null && list.size()>0) {
            boolean rs = prbBean.removeAffiliateFromAnnualRebate(personPk, new Integer(1999), new Integer(1));
            if (rs)
                System.out.println("Successfully remove the affiliate from annual rebate for (personPk="+personPk+", rebateYear=1999, and affiliatePk=1)");
            else                
                fail("Unable to remove the affiliate from annual rebate for (personPk="+personPk+", rebateYear=1999, and affiliatePk=1)");
        } else {
            System.out.println("The association of an affiliate with a person's annual rebate for (personPk="+personPk+", rebateYear=1999, and affiliatePk=1) cannot be removed because it does not exist.");
        }
        prbBean.remove();        
    }
    
    /** Test of addPRBYear method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testAddPRBYear() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testAddPRBYear: Adds another Rebate Year (1999) to the system for capturing the 12-Month Rebate Amount.");
        
        if (prbBean.getPRB12MonthAmount(new Integer(1999)) == null) {
            PRB12MonthRebateAmount data = new PRB12MonthRebateAmount();
            data.setPrbYear(new Integer(1999));
            data.setPrbPercentage(new Double(1.2));
            data.setPrbFullTime(new Double(1.25));
            data.setPrbPartTime(new Double(5.02));
            data.setPrbLowerPartTime(new Double(4.4));
            data.setPrbRetiree(new Double(3.35));
            boolean rs = prbBean.addPRBYear(data, new Integer(10000001));
            prbBean.remove();        
            if (rs)
                System.out.println("Successfully add a new rebate year to the system for capturing the 12-Month Rebate Amount. 12-Month Rebate Amount Data is: "+data);
            else 
                fail("Unable to add a new rebate year to the system for capturing the 12-Month Rebate Amount. 12-Month Rebate Amount Data is: "+data);
        } else {
            System.out.println("Rebate year 1999 already exists.");
        }
    }
    
    /** Test of getPRB12MonthAmount method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRB12MonthAmount() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRB12MonthAmount: Retrieves a 12 Month Amount from the database that matches the rebate year input.");
        PRB12MonthRebateAmount data = prbBean.getPRB12MonthAmount(new Integer(1999));
        prbBean.remove();        
        if (data==null)
            fail("Unable to retrieve the 12-Month Rebate Amount for rebate year 1999");
        else 
            System.out.println("The 12-Month Rebate Amount data for rebate year 1999 is: "+data);
    }
    
    /** Test of updatePRB12MonthAmount method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRB12MonthAmount() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRB12MonthAmount: Allows one set of Rebate Amount data to be updated");
        PRB12MonthRebateAmount data = new PRB12MonthRebateAmount();
        data.setPrbYear(new Integer(1999));
        data.setPrbPercentage(new Double(1.5));
        data.setPrbFullTime(new Double(2.25));
        data.setPrbPartTime(new Double(5.02));
        data.setPrbLowerPartTime(new Double(5.4));
        data.setPrbRetiree(new Double(7.35));
        prbBean.updatePRB12MonthAmount(data, new Integer(10000001));
        prbBean.remove();        
        System.out.println("The 12-Month Rebate Amount data is update. 12-Month rebate data is: "+data);
    }
        
    /** Test of createPRBApplicationRecord method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testCreatePRBApplicationRecord() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testCreatePRBApplicationRecord: Creates an Application record with the supplied mailed date");
        Integer appPk = prbBean.createPRBApplicationRecord(DateUtil.getCurrentDateAsTimestamp(), new Integer(10000001));
        
        prbBean.remove();                        
        if (appPk == null) 
            fail("Unable to create a new Rebate Application Record.");
        else 
            System.out.println("The Application Primary Key for the new Rebate Application record is "+appPk);
    }
    
    /** Test of createPRBCheckInfo method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testCreatePRBCheckInfo() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testCreatePRBCheckInfo: Creates political rebate check information for a person based on the rebate year.");
        System.out.println("Should be called in testUpdatePRBCheckInfo if update failed..");        
    }
        
    /** Test of getAffiliateIdentifier method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetAffiliateIdentifier() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetAffiliateIdentifier: ");
        AffiliateIdentifier affID = prbBean.getAffiliateIdentifier(new Integer(1));
        prbBean.remove();                        
        if (affID == null)
            fail("Unable to retrieve the Affiliate Identifier for AffID 1");
        else 
            System.out.println("The Affiliate Identifier for AffID 1 is: "+affID);
    }
    
    /** Test of getPRBApplicationEligible method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBApplicationEligible() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBApplicationEligible: Retrieves information on Political Rebate " +
                           "requests made for the current rebate year that have not been included in " +
                           "a prior application run and have not been denied.");
        
        List result = prbBean.getPRBApplicationEligible();
        prbBean.remove();                                
        
        if (result == null) 
            System.out.println("No eligible request for Rebate Application exists.");
        else 
            System.out.println("The eligible list for Rebate Application is: "+result);
    }
    
    /** Test of updatePRBRequestAppPk method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdatePRBRequestAppPk() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdatePRBRequestAppPk: Updates the AppPk of the Political Rebate Request record.");
        
        prbBean.updatePRBRequestAppPk(1, new Integer(1), new Integer(10000001));
        prbBean.remove();                                        
        System.out.println("The Political Rebate Request with (Pk=1) is updated with appPk=1");
    }
    
    /** Test of getPRBCheckEligible method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testGetPRBCheckEligible() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testGetPRBCheckEligible: Retrieves a list of Members that have been "+
                           "approved for a Rebate and are eligible to receive a Rebate Check.");
        List result = prbBean.getPRBCheckEligible();
        prbBean.remove();                                        
        if (result == null) 
            System.out.println("No member eligible to receive a rebate check found.");
        else 
            System.out.println("The eligible list for receiving rebate check is: "+result);
    }
    
    /** Test of calculatePRBCheckAmount method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testCalculatePRBCheckAmount() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testCalculatePRBCheckAmount: Calculates the rebate check amount for each eligible member.");

        double checkAmount = prbBean.calculatePRBCheckAmount(personPk, new Integer(1));
        prbBean.remove();                                        
        System.out.println("The Rebate Check Amount calculated for personPk="+personPk+" and affID=1 is $"+checkAmount);
    }
    
    /** Test of updateFinalRoster method, of class org.afscme.enterprise.rebate.ejb.MaintainPoliticalRebateBean. */
    public void testUpdateFinalRoster() throws Exception {
        MaintainPoliticalRebate prbBean = JNDIUtil.getMaintainPoliticalRebateHome().create();
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testUpdateFinalRoster: Updates the Members to the Final Roster " +
                           "who were part of the Check File to receive a rebate check.");
        
        prbBean.updateFinalRoster(personPk.intValue(), 100, new BigDecimal(250.50), new Integer(10000001));
        prbBean.remove();                                        
        System.out.println("The Final Roster updated for personPk="+personPk+" Check Number=100, Check Amount=250.50");
    }    
}
