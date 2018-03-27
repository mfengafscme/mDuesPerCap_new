/*
 * MaintainOrgMailingListsBeanTest.java
 * JUnit based test
 *
 * Created on June 4, 2003, 3:37 PM
 */

package org.afscme.enterprise.organization.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.naming.NamingException;
import javax.naming.Context;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.rmi.RemoteException;
import javax.ejb.*;
import junit.framework.*;
import org.apache.log4j.Logger;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.mailinglists.MailingListData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.common.RecordData;

/**
 *
 * @author hmaiwald
 */
public class MaintainOrgMailingListsBeanTest extends TestCase {

    // This is a set of Organization Mailing Lists test data
    protected static Integer userPk = new Integer(10000001);    // Admin    
    protected static Integer orgPk = new Integer(60);           // California State Library
    protected static Integer mailingListPk = new Integer(1);    // Area Directors
    protected static Integer locationPk = new Integer(1);       // Headquarters
    protected static Integer orgLocationsPk = new Integer(1);   // Headquarters    
    protected static int bulkCount = 20;
    
    
    public MaintainOrgMailingListsBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainOrgMailingListsBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testEjbCreate() throws Exception {
        System.out.println("testEjbCreate: Creating MaintainOrgMailingListsBean bean.");
        MaintainOrgMailingLists mlBean = JNDIUtil.getMaintainOrgMailingListsHome().create();
        if (mlBean == null)
            fail("MaintainOrgMailingLists was null.");
        mlBean.remove();
    }
    
    
    /** Test of getMailingLists method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetMailingLists() throws Exception {
        System.out.println("testGetMailingLists: Testing to get all Organization Mailing Lists.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();

        // Get the organization mailing lists.
        List list = maintainOrgMailingLists.getMailingLists(orgPk);        
        maintainOrgMailingLists.remove();
        if (list == null) {
            displayError();
            fail("Organization mailing lists was null for organization with pk=" +orgPk+".");
        } else {
            System.out.println("Organization mailing lists = " + list + " for organization with pk="+orgPk+".");
            System.out.println("");  
        }        
    }
     
    /** Test of addMailingList method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testAddMailingList() throws Exception {
        System.out.println("testAddMailingList: Testing to add an organization to the mailing lists.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        // Adds the organization to a particular mailing list
        maintainOrgMailingLists.addMailingList(orgPk, mailingListPk, bulkCount, locationPk, userPk);
        maintainOrgMailingLists.remove();
        System.out.println("Organization "+orgPk+ " was added to mailing list " +mailingListPk+".");
        System.out.println("");  
    }
    
    /** Test of removeMailingList method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testRemoveMailingList() throws Exception {
        System.out.println("testRemoveMailingList: Testing to remove an organization from mailing lists.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        // Remove the organization from mailing lists.
        boolean rs = maintainOrgMailingLists.removeMailingList(orgPk, mailingListPk);
        maintainOrgMailingLists.remove();        
        if (!rs) {
            displayError();
            fail("Failed to remove Organization "+orgPk+ " from mailing list " +mailingListPk+".");
        } else {
            System.out.println("Organization "+orgPk+ " was removed from mailing list " +mailingListPk+".");
            System.out.println("");  
        }                    
    }
       
    /** Test of getMailingListNames method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetMailingListNames() throws Exception {
        System.out.println("testGetMailingListNames: Testing to get all organization mailing lists names.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();        
        
        // Get all organization mailing lists names
        Map mlNames = maintainOrgMailingLists.getMailingListNames();
        maintainOrgMailingLists.remove();
        if (mlNames == null) {
            displayError();
            fail("Unable to get the organization mailing names.");
        } else {
            System.out.println("Organization Mailing Lists Names are: "+mlNames);
            System.out.println("");  
        }
    }
    
    /** Test of getMailingListName method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetMailingListName() throws Exception {
        System.out.println("testGetMailingListName: Testing to get the organization mailing list name.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        // get the particular mailing list name of an organization
        String mlName = maintainOrgMailingLists.getMailingListName(mailingListPk);
        maintainOrgMailingLists.remove();
        if (mlName == null) {
            displayError();
            fail("Unable to get the organization mailing name for mailingListPk "+mailingListPk+".");
        } else {
            System.out.println("Organization Mailing List Name for MailingListPk "+mailingListPk+" is: "+mlName);
            System.out.println("");          
        }
    }
    
    /** Test of setMailingListLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testSetMailingListLocation() throws Exception {
        System.out.println("testSetMailingListLocation: Testing to set the Location for an organization on a mailing list.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        // set the Location for an organization on a mailing list
        maintainOrgMailingLists.setMailingListLocation(orgPk, mailingListPk, locationPk, userPk);        
        maintainOrgMailingLists.remove();
        System.out.println("LocationPk = "+locationPk+" has been set to organizationPk= "+orgPk+" and mailingListPk="+mailingListPk);
        System.out.println("");          
    }
    
    /** Test of updateMailingListBulkCount method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testUpdateMailingListBulkCount() throws Exception {
        System.out.println("testUpdateMailingListBulkCount: Testing to update the bulk count for an existing organization on a mailing list.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        // update the bulk count for an existing organization on a mailing list        
        maintainOrgMailingLists.updateMailingListBulkCount(orgPk, mailingListPk, bulkCount, userPk);        
        maintainOrgMailingLists.remove();
        System.out.println("Bulk count "+bulkCount+" has been updated to mailing list (pk="+mailingListPk+").");
        System.out.println("");          
    }
    
    /** Test of getOrgPrimaryLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetOrgPrimaryLocation() throws Exception {
        System.out.println("testGetOrgPrimaryLocation: Testing to get the organization primary location.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        //get the organization primary location
        Integer primaryLocation = maintainOrgMailingLists.getOrgPrimaryLocation(orgPk);        
        maintainOrgMailingLists.remove();
        System.out.println("Primary location for orgPk="+orgPk+" is: "+primaryLocation+".");
        System.out.println("If primary location is 0 - meaning that orgPk="+orgPk+" does not have a primary location");          
        System.out.println("");          
    }
    
    /** Test of getOrgLocations method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetOrgLocations() throws Exception {
        System.out.println("testGetOrgLocations: Testing to get the set of locations for the organization.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        //get the set of locations for the organization
        List list = maintainOrgMailingLists.getOrgLocations(orgPk);       
        maintainOrgMailingLists.remove();
        if (list == null) {
            displayError();
            fail("Unable to get a list of locations that belong to organization (pk="+orgPk+")");
        } else {
            System.out.println("A list of locations that belong to organization (pk="+orgPk+") is:"+list+" .");
            System.out.println("");          
        }
    }
    
    /** Test of getOrgLocationAddress method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetOrgLocationAddress() throws Exception {
        System.out.println("testGetOrgLocationAddress: Testing to get the regular type address of a location.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        //get the address (with type=regular) of a location        
        OrgAddressRecord orgAddrRec = maintainOrgMailingLists.getOrgLocationAddress(orgLocationsPk);     
        maintainOrgMailingLists.remove();
        if (orgAddrRec == null) {
            displayError();
            fail("Unable to get the address (with type=Regular) for location "+orgLocationsPk+".");
        } else {
            System.out.println("The address (with type=Regular) of the location "+orgLocationsPk+" is :"+orgAddrRec+".");
            System.out.println("");          
        }
    }
    
    /** Test of getOrgName method, of class org.afscme.enterprise.organization.ejb.MaintainOrgMailingListsBean. */
    public void testGetOrgName() throws Exception {
        System.out.println("testGetOrgName: Testing to get the organization name based on pk.");
        MaintainOrgMailingLists maintainOrgMailingLists = JNDIUtil.getMaintainOrgMailingListsHome().create();
        
        //get the organization name based on pk.        
        String orgName = maintainOrgMailingLists.getOrgName(orgPk);
        maintainOrgMailingLists.remove();
        if (orgName == null) {
            displayError();
            fail("Unable to get the organization name for orgPk="+orgPk+".");
        } else {
            System.out.println("The name of the organization is "+orgName+" where orgPk=("+orgPk+").");
            System.out.println("");          
        }
    }

    /** Display output of test data **/
    public void displayError() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!");
        System.out.println("!!! If any of the test failed, please check the database to see if you have installed test data first !!!");
        System.out.println("!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("The following data is used for testing: ");
        System.out.println("OrgPk=60 - California State Library");
        System.out.println("MailingListPk=1 - Area Directors");
        System.out.println("LocationPk=1 - Headquarters");        
        System.out.println("UserPk=10000001 - Admin");        
    }
    
}
