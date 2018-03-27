/*
 * MaintainParticipationGroupsBeanTest.java
 * JUnit based test
 *
 * Created on August 20, 2003, 1:01 PM
 */

package org.afscme.enterprise.participationgroups.ejb;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.participationgroups.*;

/**
 *
 * @author lmark
 */
public class MaintainParticipationGroupsBeanTest extends TestCase {
    protected static Integer userPk = new Integer(10000001);    // Admin    
    protected static Integer groupPk = new Integer(1); 
    protected static Integer typePk = new Integer(1);
    protected static Integer detailPk = new Integer(1);
    protected static Integer outcomePk = new Integer(1);
        
    public MaintainParticipationGroupsBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainParticipationGroupsBeanTest.class);
        
        return suite;
    }

    /** Test of addParticipationGroupData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testAddParticipationGroupData() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddParticipationGroupData: Testing the Add of a Participation Group ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        
        // setup the test data
        ParticipationGroupData inData = new ParticipationGroupData();
        inData.setName("Test Group Name");
        inData.setDescription("Test New Group Name Description");
        // add the data to db        
        ParticipationGroupData outData = maintainPart.addParticipationGroupData(inData);        
        maintainPart.remove();
        // check for results
        if (outData == null || (outData != null && outData.getGroupPk() == null)) {
            fail("Failed to add a participation group.");
        } else {
            System.out.println("Participation group was successfully added with data = " + outData);
            System.out.println("");  
        }
    }
    
    /** Test of addParticipationTypeData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testAddParticipationTypeData() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddParticipationTypeData: Testing the Add of a Participation Type ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        
        // setup test data
        ParticipationTypeData inData = new ParticipationTypeData();
        inData.setGroupPk(groupPk);
        inData.setName("Test Type Name");
        inData.setDescription("Test New Type Description");
        // add test data to db
        ParticipationTypeData outData = maintainPart.addParticipationTypeData(inData);
        maintainPart.remove();
        // check for results
        if (outData == null || (outData != null && outData.getTypePk() == null)) {
            fail("Failed to add a participation type.");
        } else {
            System.out.println("Participation type was successfully added with data = " + outData);
            System.out.println("");  
        }        
    }    
   
    /** Test of addParticipationDetailData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testAddParticipationDetailData() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddParticipationDetailData: Testing the Add of a Participation Detail ");

        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        // setup the test data
        ParticipationDetailData detail = new ParticipationDetailData();
        detail.setGroupPk(groupPk);
        detail.setTypePk(typePk);
        detail.setStatus(true);        
        detail.setName("Test Detail Name");
        detail.setDescription("Test New Detail Name Description");
        // add the data to db
        ParticipationDetailData data = maintainPart.addParticipationDetailData(detail, userPk);
        maintainPart.remove();                
        // check for results
        if (data == null || (data != null && data.getDetailPk() == null)) {
            fail("Failed to add a participation detail.");
        } else {
            System.out.println("Participation detail was successfully added with data = " + data);
            System.out.println("");  
        }
    }
    
    /** Test of addParticipationOutcomeData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testAddParticipationOutcomeData() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddParticipationOutcomeData: Testing the Add of a Participation Outcome ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        // setup test data
        ParticipationOutcomeData inData = new ParticipationOutcomeData();
        inData.setGroupPk(groupPk);
        inData.setGroupNm("Test Group Name");
        inData.setTypePk(typePk);
        inData.setTypeNm("Test Type Name");
        inData.setDetailPk(detailPk);
        inData.setDetailNm("Test Detail Name");
        inData.setOutcomeNm("Test Outcome Name");
        inData.setDescription("Test New Outcome Name Description");        
        // insert test data to db
        ParticipationOutcomeData outData = maintainPart.addParticipationOutcomeData(inData, userPk);
        maintainPart.remove();
        // check for results
        if (outData == null || (outData != null && outData.getOutcomePk() == null)) {
            fail("Failed to add a participation outcome.");
        } else {
            System.out.println("Participation outcome was successfully added with data = " + outData);
            System.out.println("");  
        }        
    }
    
    /** Test of isDuplicateGroup method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testIsDuplicateGroup() throws NamingException, CreateException, RemoveException {
        System.out.println("testIsDuplicateGroup: Testing the Duplicate Group Names ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        String groupName = "Test Group Name";
        boolean result = maintainPart.isDuplicateGroup(groupName);
        maintainPart.remove();
        // check for results
        if (result)
            System.out.println("Group Name," + groupName + ", already exists");
        else
            System.out.println("Group Name," + groupName + ", does not exist.");
        System.out.println("");  
    }
    
    /** Test of inactivateParticipationDetail method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testInactivateParticipationDetail() throws NamingException, CreateException, RemoveException {
        System.out.println("testInactivateParticipationDetail: Testing the Inactivation of a Participation Detail ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        boolean result = maintainPart.inactivateParticipationDetail(detailPk, userPk);
        
        maintainPart.remove();
        // check for results
        if (result)
            System.out.println("Detail with primary key =" + detailPk + " is inactivated successfully.");
        else
            System.out.println("Unable to inactivate participation detail with primary key = " + detailPk);        
    }
    
    /** Test of getParticipationDetailData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    /*
    public void testGetParticipationDetailData() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationDetailData: Testing Get of a Participation Detail ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        
// call testAddParticipationDetailData first - incase if this data has not been added
        testAddParticipationDetailData();
        
// test both methods by all Pk or by shortcut
        ParticipationDetailData data = maintainPart.getParticipationDetailData(groupPk, typePk, detailPk);
        // check the result
        if (data==null) {
            fail("Failed to retrieve the Participation Detail with groupPk="+groupPk+", typePk="+typePk+", detailPk="+detailPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+", detailPk="+detailPk+" is "+data);
        }
        
        data = maintainPart.getParticipationDetailData(1);        
        maintainPart.remove();        
        // check the result
        if (data==null) {
            fail("Failed to retrieve the Participation Detail with shortcut=1");
        } else {
            System.out.println("Returned data with shortcut=1 is "+data);
        }        
    }
     */
    
    /** Test of getParticipationDetails method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationDetails() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationDetails: Testing Getting of Participation Details ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();

// test all methods by all Pk or by just groupPk
        List data = maintainPart.getParticipationDetails(groupPk, typePk); 
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve the Participation Details with groupPk="+groupPk+", typePk="+typePk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+" is "+data);
        }      
                
        data = maintainPart.getParticipationDetails(groupPk, typePk, detailPk);         
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve the Participation Details with groupPk="+groupPk+", typePk="+typePk+", detailPk="+detailPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+", detailPk="+detailPk+" is "+data);
        }      
        
        data = maintainPart.getParticipationDetails(groupPk);
        maintainPart.remove();
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve the Participation Details with groupPk="+groupPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+" is "+data);
        }      
    }
    
    /** Test of getParticipationGroupData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationGroupData() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationGroupData: Testing Get of a Participation Group ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        ParticipationGroupData data = maintainPart.getParticipationGroupData(groupPk);
        maintainPart.remove();
        // check the result
        if (data==null) {
            fail("Failed to retrieve the Participation Group with groupPk="+groupPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+" is "+data);
        }      
    }
    
    /** Test of getParticipationGroups method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationGroups() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationGroups: Testing Getting of all Participation Groups ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        List data = maintainPart.getParticipationGroups();
        maintainPart.remove();
        
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve all Participation Groups.");
        } else {
            System.out.println("Returned data is "+data);
        }              
    }
    
    /** Test of getParticipationOutcomeData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationOutcomeData() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationOutcomeData: Testing Get of a Participation Outcome ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        ParticipationOutcomeData data = maintainPart.getParticipationOutcomeData(groupPk, typePk, detailPk, outcomePk);
        maintainPart.remove();
        
        // check the result
        if (data==null) {
            fail("Failed to retrieve a Participation Outcome with groupPk="+groupPk+", typePk="+typePk+", detailPk="+detailPk+", outcomePk="+outcomePk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+", detailPk="+detailPk+", outcomePk="+outcomePk+" is "+data);
        }                      
    }
    
    /** Test of getParticipationOutcomes method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationOutcomes() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationOutcomes: Testing Getting of Participation Outcomes ");

        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        List data = maintainPart.getParticipationOutcomes(groupPk, typePk);  
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve Participation Outcomes with groupPk="+groupPk+", typePk="+typePk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+" is "+data);
        }              
        
        data = maintainPart.getParticipationOutcomes(groupPk, typePk, detailPk);
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve Participation Outcomes with groupPk="+groupPk+", typePk="+typePk+", detailPk"+detailPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+", detailPk"+detailPk+" is "+data);
        }              
        
        data = maintainPart.getParticipationOutcomes(groupPk);
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve Participation Outcomes with groupPk="+groupPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+" is "+data);
        }              
        maintainPart.remove();                
    }
    
    /** Test of getParticipationTypeData method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationTypeData() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationTypeData: Testing Get of a Participation Type ");
        
        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        ParticipationTypeData data = maintainPart.getParticipationTypeData(groupPk, typePk);  
        maintainPart.remove();
        // check the result
        if (data==null) {
            fail("Failed to retrieve a Participation Type with groupPk="+groupPk+", typePk="+typePk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+" is "+data);
        }              
    }
    
    /** Test of getParticipationTypes method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationTypes() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetParticipationTypes: Testing Getting of Participation Types ");

        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        List data = maintainPart.getParticipationTypes(groupPk);
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve Participation Types with groupPk="+groupPk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+" is "+data);
        }                                      
        
        data = maintainPart.getParticipationTypes(groupPk, typePk);
        maintainPart.remove();
        // check the result
        if (data==null || (data!=null && data.size()<=0)) {
            fail("Failed to retrieve Participation Types with groupPk="+groupPk+", typePk="+typePk);
        } else {
            System.out.println("Returned data with groupPk="+groupPk+", typePk="+typePk+" is "+data);
        }                                      
    }
    
    /** Test of getGroupPk method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetGroupPk() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetGroupPk: Testing of Getting of Group Pk for any pk below it ");

        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        Integer l_groupPk = maintainPart.getGroupPk(groupPk, ParticipationGroupData.PK_GROUP, detailPk);
        maintainPart.remove();
        
        // check the result
        if (l_groupPk==null) {
            fail("Failed to retrieve Group Pk for any pk below it");
        } else {
            System.out.println("Returned groupPk is "+l_groupPk);
        }                                                      
    }

    /** Test of getParticipationName method, of class org.afscme.enterprise.participationgroups.ejb.MaintainParticipationGroupsBean. */
    public void testGetParticipationName() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetGroupPk: Testing of Getting of Participation Name for any participation pk. ");

        MaintainParticipationGroups maintainPart = JNDIUtil.getMaintainParticipationGroupsHome().create();
        String name = maintainPart. getParticipationName(groupPk, ParticipationGroupData.PK_GROUP);
        maintainPart.remove();
        
        // check the result
        if (name==null) {
            fail("Failed to retrieve Participation Name for any pk");
        } else {
            System.out.println("Returned Participation Group Name with groupPk="+groupPk+" is "+name);
        }                                                      
    }
}
