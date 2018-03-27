/*
 * MaintainMembersBeanTest.java
 * JUnit based test
 *
 * Created on May 20, 2003, 7:02 PM
 */

package org.afscme.enterprise.member.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import junit.framework.*;
import org.afscme.enterprise.common.ejb.*;
import org.afscme.enterprise.member.*;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateErrorCodes;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.common.SortData;
import org.apache.log4j.Logger;

/**
 *
 * @author gdecorte
 */
public class MaintainMembersBeanTest extends TestCase 
{
    
    public MaintainMembersBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainMembersBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testEjbCreate() 
    {
        System.out.println("testEjbCreate");
        System.out.println("Creating MaintainMembersTest bean");
	try  	
        {
            MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
	    if (maintainMembers == null)
	    fail("maintainMembers was null");
            maintainMembers.remove();
        }
        catch (NamingException ne) {
            throw new EJBException("Unable to get MaintainMembers from MaintainMemberBean.ejbCreate()" + ne);
        }
        catch (CreateException ce) {
            throw new EJBException("Unable to get MaintainMembers from MaintainMembersBean.ejbCreate()" + ce);
        }
        catch (RemoveException re) {
            throw new EJBException("Unable to remove MaintainMembers during test" + re);
        }
    } // end test ejb create
    
    /** Test of addMember method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testAddMember() throws NamingException, CreateException, RemoveException
//    {
//        //This test, or others, should test at last the following cases: Where an existing person
//        //is added as a member, when a new person and a new member are added. Another method should
//        //test when an existing member is added to a new affiliate
//        
//        //add new person as member
//        
//        System.out.println();
//        System.out.println("testAddMember");
//        System.out.println();
//        
//        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
//        Integer userPk = new Integer(10000001);
//        Integer deptPk = new Integer(4001);
//        Integer affPk = new Integer(2);
//        
//        NewPerson newPerson = new NewPerson();
//        NewMember newMember = new NewMember();
//        
//        //set some fields in newPerson and then add to newMember
//        newPerson.setLastNm("Test Last Name 1");
//        newPerson.setFirstNm("Jules");
//        newPerson.setMemberFg(new Boolean(true));
//        newPerson.setSsnValid(new Boolean(true));
//        newPerson.setSsn("227557777");
//        newMember.setTheNewPerson(newPerson);
//        // Member status of active
//        newMember.setMbrStatus(new Integer(31001));
//        //member type of regular
//        newMember.setMbrType(new Integer(29001));
//        
//        newMember.setAffPk(affPk);
//        
//        int rc = maintainMembers.addMember(newMember, userPk, deptPk, affPk);
//        System.out.println("testAddMember - right after addMember call, return code is: " + rc);
//        
//        System.out.println();
//        System.out.println("end of testAddMember");
//    }
    
//    /** Test of addMemberAffiliateData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testAddMemberAffiliateData() {
//        System.out.println("testAddMemberAffiliateData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    
    /** Test of isMemberBarred method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testIsMemberBarred() throws NamingException, CreateException, RemoveException
    {
        System.out.println();
        System.out.println("testIsMemberBarred");
        System.out.println();
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        Integer personPk = new Integer(10000001);
        //boolean barredFg;
        //barredFg = maintainMembers.isMemberBarred(personPk) == null)) 
        
        if (maintainMembers.isMemberBarred(personPk)) 
        {
            System.out.println("testisMemberBarred - This member is barred ");
        }
        if (!(maintainMembers.isMemberBarred(personPk))) {
            System.out.println("testisMemberBarred - This member is not barred ");
        
        }
        
      //  assertNotNull(new Boolean(barredFg));      
        
        maintainMembers.remove();
        System.out.println("end testIsMemberBarred");
    } // end testIsMember
    
//    /** Test of addParticipationData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testAddParticipationData() {
//        System.out.println("testAddParticipationData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
    
    /** Test of addToWeeklyCardRun method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testAddToWeeklyCardRun() throws NamingException, CreateException, RemoveException
    {
        System.out.println("testAddToWeeklyCardRun : starting");
        System.out.println();
        System.out.println();
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        Integer personPk = new Integer(10000210);
        Integer affPk = new Integer(7);
        
        maintainMembers.addToWeeklyCardRun(personPk, affPk);
        
        System.out.println();
        System.out.println("testAddToWeeklyCardRun : ending");
          
        
    }
 
    
    /** Test of addToWeeklyCardRun method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. 
     This one tests the case where only a personPk is given, and the person shoudl be added to 
     the card run for each affiliate they belong to 
     */
    public void testAddToWeeklyCardRun2() throws NamingException, CreateException, RemoveException
    {
        System.out.println("testAddToWeeklyCardRun2 : starting");
        System.out.println();
        System.out.println();
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        Integer personPk = new Integer(10000310);
             
        maintainMembers.addToWeeklyCardRun(personPk);
        
        System.out.println();
        System.out.println("testAddToWeeklyCardRun2 : ending");
          
        
    }
    
    
//    /** Test of deleteParticipationData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testDeleteParticipationData() {
//        System.out.println("testDeleteParticipationData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    
//    /** Test of getAffiliateEmployerData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testGetAffiliateEmployerData() {
//        System.out.println("testGetAffiliateEmployerData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    
//    /** Test of getCommentHistory method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testGetCommentHistory() {
//        System.out.println("testGetCommentHistory");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    
//    /** Test of getMemberAffiliateData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testGetMemberAffiliateData() {
//        System.out.println("testGetMemberAffiliateData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
    
    /** Test of getMemberAffiliatesSummary method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testGetMemberAffiliatesSummary() throws NamingException, CreateException, RemoveException
    {
        System.out.println();
        System.out.println("testGetMemberAffiliatesSummary");
        System.out.println();
       
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        Integer personPk = new Integer(10000002);
        Collection vduAffiliates = null; // needed because message signature had to change to support vdu data level access control		
        
        Collection list = new ArrayList();
        
        list = maintainMembers.getMemberAffiliatesSummary(personPk, vduAffiliates);
        assertNotNull(list);
        
        System.out.println("testGetMemberAffiliatesSummary - contents of list (collection of member affiliate results : " +
               TextUtil.toString(list));
        
        System.out.println();
        maintainMembers.remove();
        
        System.out.println("end of testGetMemberAffiliatesSummary");
        
    }
    
//    /** Test of getOfficerInfoForMember method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testGetOfficerInfoForMember() {
//        System.out.println("testGetOfficerInfoForMember");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
    
    /** Test of getMemberDetail method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testGetMemberDetail() throws NamingException, CreateException, RemoveException 
    {
        System.out.println();
        System.out.println("testGetMemberDetail");
        MemberData memberData = null;
	  Collection vduAffiliates = null;	// required for message signature changed because of view data utility requirements
        
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        Integer personPk = new Integer(10000002);
        Integer dept = new Integer(4001);
        
        memberData = maintainMembers.getMemberDetail(personPk, dept, vduAffiliates);
        
        assertNotNull(memberData);
        
        System.out.println("testGetMemberDetail - contents of memberData : " + TextUtil.toString(memberData));
        
        System.out.println();
        maintainMembers.remove();
        System.out.println("end testGetMemberDetail");
    }
//    /** Test of getTimePk method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testGetTimePk() {
//        System.out.println("testGetTimePk");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
    
    /** Test of getParticipationData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testGetParticipationData()  throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationData");          
        System.out.println("testGetParticipationData");
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();    
        
        ParticipationData pd = setUpParticipation();
        
        System.out.println("ADDING NEW PARTICIPATION");
        maintainMembers.addParticipationData(pd);
        pd = null;
        pd = maintainMembers.getParticipationData(new Integer("10000380"),new Integer("1"));
        
        System.out.println("ASSERTING Participation Data is not null");
        assertNotNull(pd);
        
        maintainMembers.deleteParticipationData(new Integer("10000380"),new Integer("1"));      
        maintainMembers.remove();            
        System.out.print("END testGetParticipationData");
    }
    
    /** Test of getParticipationSummary method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testGetParticipationSummary() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationSummary");              
        System.out.println("testGetParticipationSummary");
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        
        System.out.println("Getting Summary before add");
        SortData sd = new SortData();
        sd.setSortField(ParticipationData.SORT_BY_GROUP);
        Collection summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        int count = 0;
        if (summaryList == null) {
            System.out.println("Count BEFORE ADD = NULL");
        }
        else {
            System.out.println("Count BEFORE ADD = " + summaryList.size());
            count = summaryList.size();
        }
        
        ParticipationData pd = setUpParticipation();
        
        System.out.println("ADDING NEW PARTICIPATION");
        maintainMembers.addParticipationData(pd);
        summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        if (summaryList == null) {
            System.out.println("Add failed.  Summary is NULL after add");            
            fail("Add failed.  Summary is NULL after add");
        } else {
            System.out.println("Count After Add = " + summaryList.size());
            if ((summaryList.size() - 1) == count) {
                System.out.println("PASS");
            } else {
                fail("Summary count same as before add");
            }
        }
        maintainMembers.deleteParticipationData(new Integer("10000380"),new Integer("1"));                
        maintainMembers.remove();            
        System.out.print("END testGetParticipationSummary");
    }
        
    /** Test of deleteParticipationData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testDeleteParticipationData() throws NamingException, CreateException, RemoveException {
        System.out.println("testDeleteParticipationData");            
        System.out.println("testDeleteParticipationData");
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        
        System.out.println("Getting Summary before add");
        SortData sd = new SortData();
        sd.setSortField(ParticipationData.SORT_BY_GROUP);
        Collection summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        int count = 0;
        if (summaryList == null) {
            System.out.println("Count BEFORE ADD = NULL");
        }
        else {
            System.out.println("Count BEFORE ADD = " + summaryList.size());
            count = summaryList.size();
        }
        
        ParticipationData pd = setUpParticipation();
        
        System.out.println("ADDING NEW PARTICIPATION");
        maintainMembers.addParticipationData(pd);
        summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        if (summaryList == null) {
            System.out.println("Add failed.  Summary is NULL after add");            
            fail("Add failed.  Summary is NULL after add");
        } else {
            System.out.println("Count After Add = " + summaryList.size());
            if ((summaryList.size() - 1) == count) {
                System.out.println("PASS");
            } else {
                fail("Summary count same as before add");
            }
        }
        System.out.println("DELETING NEWLY ADDED PARTICIPATION");
        
        maintainMembers.deleteParticipationData(new Integer("10000380"),new Integer("1"));
        summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        
        System.out.println("Count After DELETE = " + summaryList.size());
        if ((summaryList.size()) == count) {
            System.out.println("PASS");
        } else {
            fail("Summary count same as before add");
        }      
                
        maintainMembers.remove();            
        System.out.print("END testAddPariticpationData");
    }
 
    /** Test of editParticipationData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testEditParticipationData() throws NamingException, CreateException, RemoveException {
        System.out.println("testEditParticipationData");             
        System.out.println("testEditParticipationData");
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();      
        ParticipationData pd = setUpParticipation();
        
        System.out.println("ADDING NEW PARTICIPATION FOR EDIT");
        maintainMembers.addParticipationData(pd);
        System.out.println("GETTING ADDED PARTICIPATION");
        pd = null;
        pd = maintainMembers.getParticipationData(new Integer("10000380"),new Integer("1"));
        System.out.println("COMMENT TEXT FETCHED FROM TABLE AFTER INSERT " + pd.getTheCommentData().getComment()); 

        pd = setUpParticipation("NEW COMMENT TEXT");
        maintainMembers.editParticipationData(pd, new Integer("1"));
        
        pd = maintainMembers.getParticipationData(new Integer("10000380"),new Integer("1"));
        System.out.println("COMMENT TEXT FETCHED FROM TABLE AFTER *** EDIT *** " + pd.getTheCommentData().getComment());         

        if (pd.getTheCommentData().getComment().equals("Test Comment"))
            fail("Edit did not occur, value is the same");
        
        maintainMembers.deleteParticipationData(new Integer("10000380"),new Integer("1"));                
        maintainMembers.remove();            
        System.out.print("END testGetParticipationData");
    }
 
    /** Test of addParticipationData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testAddParticipationData() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddParticipationData");             
        System.out.println("testAddParticipationData");
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        
        System.out.println("Getting Summary before add");
        SortData sd = new SortData();
        sd.setSortField(ParticipationData.SORT_BY_GROUP);
        Collection summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        int count = 0;
        if (summaryList == null) {
            System.out.println("Count BEFORE ADD = NULL");
        }
        else {
            System.out.println("Count BEFORE ADD = " + summaryList.size());
            count = summaryList.size();
        }
        
        ParticipationData pd = setUpParticipation();

        System.out.println("ADDING NEW PARTICIPATION");
        maintainMembers.addParticipationData(pd);
        summaryList = maintainMembers.getParticipationSummary(new Integer("10000380"),sd);
        if (summaryList == null) {
            System.out.println("Add failed.  Summary is NULL after add");            
            fail("Add failed.  Summary is NULL after add");
        } else {
            System.out.println("Count After Add = " + summaryList.size());
            if ((summaryList.size() - 1) == count) {
                System.out.println("PASS");
            } else {
                fail("Summary count same as before add");
            }
        }
        maintainMembers.deleteParticipationData(new Integer("10000380"),new Integer("1"));
                
        maintainMembers.remove();            
        // Add your test code below by replacing the default call to fail.
        System.out.print("END testAddPariticpationData");
    }

    private ParticipationData setUpParticipation(String comment) {
        ParticipationData pd = new ParticipationData();
        ParticipationOutcomeData pod = new ParticipationOutcomeData();
        RecordData rd = new RecordData();
        CommentData cd = new CommentData();
        
        rd.setCreatedBy(new Integer("10001812"));
        rd.setModifiedBy(new Integer("10001812"));
        rd.setCreatedDate(DateUtil.getCurrentDateAsTimestamp());
        rd.setModifiedDate(DateUtil.getCurrentDateAsTimestamp());
        
        cd.setComment(comment);
        cd.setCommentDt(DateUtil.getCurrentDateAsTimestamp());
        cd.setRecordData(rd);
        
        pod.setDetailPk(new Integer("1"));
        pod.setOutcomePk(new Integer("1"));
        
        pd.setPersonPk(new Integer("10000380"));
        pd.setParticipDetailPk(new Integer("1"));
        pd.setMbrParticipDt(DateUtil.getCurrentDateAsTimestamp());
        pd.setTheRecordData(rd);
        pd.setTheCommentData(cd);
        pd.setTheParticipationOutcomeData(pod);
        return pd;
    }
    
    private ParticipationData setUpParticipation() {
        return setUpParticipation("Test Comment");
    }
    
    /** Test of searchMembers method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testSearchMembers() throws NamingException, CreateException, RemoveException {
        System.out.println("testSearchMembers");
        System.out.println();
        System.out.println("testSearchMembers");
        ArrayList results = new ArrayList(); 
     
        MemberCriteria memberCriteria = new MemberCriteria();
        Integer dept = new Integer(4001);
        
        //create some query criteria
        memberCriteria.setAffPk(new Integer(2));
        memberCriteria.setPage(0); // page one
        memberCriteria.setPageSize(20);
        System.out.println("testSearchMembers from criteria, affPk: " + memberCriteria.getAffPk() +
        " Page: " + memberCriteria.getPage() + " PageSize: " + memberCriteria.getPageSize() );
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
               
       
        int count = maintainMembers.searchMembers(memberCriteria, dept, results);
        
        System.out.println("Result count is: " + count);
        
        assertNotNull(results);
        
        System.out.println("testSearchMembers - contents of results : " + TextUtil.toString(results));
        
        System.out.println();
        maintainMembers.remove();
        System.out.println("end testSearchMembers");
    }
    
//    /** Test of updateAffiliateEmployerData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testUpdateAffiliateEmployerData() {
//        System.out.println("testUpdateAffiliateEmployerData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//    
//    /** Test of updateMemberAffiliateData method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
//    public void testUpdateMemberAffiliateData() {
//        System.out.println("testUpdateMemberAffiliateData");
//        
//        // Add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
    
    /** Test of updateMemberDetail method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testUpdateMemberDetail() throws NamingException, CreateException, RemoveException
    {
        System.out.println();
        System.out.println("testUpdateMemberDetail");
        System.out.println();
        
        Integer userPk = new Integer(10000001);
        MemberData memberData = new MemberData();
        PersonData personData = new PersonData();
        
        personData.setPersonPk(new Integer(10000002));
        System.out.println("testUpdateMemberDetail - right after first access of PersonData object");     
        personData.setFirstNm("Jason");
        personData.setLastNm("Ancarrow");
        personData.setSsn("876543210");
        memberData.setThePersonData(personData);
        
        memberData.setMbrBarredFg(new Boolean(true));
        // need code to get and set date
        System.out.println("testUpdateMemberDetail - right before getMaitainMembersHome");     
        
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        
        System.out.println("testUpdateMemberDetail - right before updateMemberDetail");            
        maintainMembers.updateMemberDetail(memberData, userPk);
        
        maintainMembers.remove();
        System.out.println();
        System.out.println(" end of testUpdateMemberDetail");
    }
    
     /** Test of updateMemberDetail method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testInactivateMember() throws NamingException, CreateException, RemoveException
    {
        System.out.println();
        System.out.println("testInactivateMember");
        System.out.println();
        
        Integer userPk = new Integer(10000001);
        Integer affPk = new Integer(15);
        Integer mbrStatus = new Integer(30001);
        Integer personPk = new Integer(10000542);
        
        System.out.println("testInactivateMember - right before getMaintainMembersHome");     
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        
        System.out.println("testUpdateMemberDetail - right before Inactivate Member call");            
        int rc = maintainMembers.inactivateMember(personPk, affPk, mbrStatus, userPk);
        
        if (rc != 0) System.out.println("testInactivateMember - unexpected result return code not equal to 0, rc = " + rc);  
        else if (rc == 0) System.out.println("testInactivateMember - return code indicates success ! ");     
        
        maintainMembers.remove();
        System.out.println();
        System.out.println(" end of testInactivateMember");
    }
    
    
    /** Test of updateAffMbrActivity method, of class org.afscme.enterprise.member.ejb.MaintainMembersBean. */
    public void testUpdateAffMbrActivity() throws NamingException, CreateException, RemoveException
    {
        // returns void so must check database
        System.out.println();
        System.out.println("testUpdateAffMbrActivity");
        System.out.println();
             
        MaintainMembers maintainMembers = JNDIUtil.getMaintainMembersHome().create();
        Integer affPk = new Integer(2);
        Integer activityType = new Integer(30001);
        maintainMembers.updateAffMbrActivity(affPk, activityType, 1);
        
        maintainMembers.remove();
        System.out.println("testUpdateAffMbrActivity - end test");
    } // end testUpdateAff
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    
} //end class
