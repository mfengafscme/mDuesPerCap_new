/*
 * MaintainPersonMailingListsBeanTest.java
 * JUnit based test
 *
 * Created on June 4, 2003, 3:36 PM
 */

package org.afscme.enterprise.person.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import javax.naming.NamingException;
import javax.naming.Context;
import junit.framework.*;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.mailinglists.MailingListData;
import org.afscme.enterprise.address.PersonAddressRecord;
import java.rmi.RemoteException;
import javax.ejb.*;
import org.apache.log4j.Logger;

/**
 *
 * @author hmaiwald
 */
public class MaintainPersonMailingListsBeanTest extends TestCase {
    
    // This is a set of Person Mailing Lists test data
    protected static Integer userPk = new Integer(10000001);    // Admin person
    protected static Integer personPk = new Integer(10000001);  // Admin person
    protected static Integer mailingListPk = new Integer(1);    // Area Directors
    protected static Integer addressPk = new Integer(1);        // 8 Lemon Road...
    
    public MaintainPersonMailingListsBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainPersonMailingListsBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testEjbCreate() throws Exception {        
        System.out.println("testEjbCreate: Creating MaintainPersonMailingLists bean.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        if (mlBean == null)
            fail("MaintainPersonMailingLists was null.");
        mlBean.remove();
    }
    
    /** Test of getPersonMailingLists method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testGetPersonMailingLists() throws Exception {
        System.out.println("testGetPersonMailingLists: Testing to retrieve the set of the mailing lists to which a person belongs.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        
        // Get the person mailing lists.
        List list = mlBean.getPersonMailingLists(personPk);        
        mlBean.remove();
        if (list == null) {
            displayError();
            fail("Person mailing lists was null for person with pk=" +personPk+".");
        } else {
            System.out.println("Person mailing lists = " + list + " for person with pk="+personPk+".");
            System.out.println("");  
        }        
    }
    
    /** Test of addPersonMailingList method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testAddPersonMailingList() throws Exception {
        System.out.println("testAddPersonMailingList: Testing to add person to a mailing list.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        
        // Adds the person to a particular mailing list
        mlBean.addPersonMailingList(personPk, mailingListPk, addressPk, userPk);
        mlBean.remove();
        System.out.println("Person "+personPk+ " was added to mailing list " +mailingListPk+".");
        System.out.println("");  
    }
    
    /** Test of removePersonMailingList method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testRemovePersonMailingList() throws Exception {
        System.out.println("testRemovePersonMailingList: Testing to remove the person from a particular mailing list.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();

        // Remove the person from mailing lists.
        boolean rs = mlBean.removePersonMailingList(personPk, mailingListPk);
        mlBean.remove();        
        if (!rs) {
            displayError();
            fail("Failed to remove Person "+personPk+ " from mailing list " +mailingListPk+".");
        } else {
            System.out.println("Person "+personPk+ " was removed from mailing list " +mailingListPk+".");
            System.out.println("");  
        }                            
    }
    
    /** Test of setMailingListAddress method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testSetMailingListAddress() throws Exception {
        System.out.println("testSetMailingListAddress: Testing to set the Address for a person on a mailing list.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        
        // set the Address for the person on a mailing list
        mlBean.setMailingListAddress(personPk, mailingListPk, addressPk, userPk);        
        mlBean.remove();
        System.out.println("AddressPk = "+addressPk+" has been set to personPk= "+personPk+" and mailingListPk="+mailingListPk);
        System.out.println("");          
    }
    
    /** Test of getMailingListNames method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testGetMailingListNames() throws Exception {
        System.out.println("testGetMailingListNames: Testing to retrieve the set of Person Mailing List names in total.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        
        // Get all person mailing lists names
        Map mlNames = mlBean.getMailingListNames();
        mlBean.remove();
        
        if (mlNames == null) {
            displayError();
            fail("Unable to get person mailing lists names.");
        } else {
            System.out.println("Person Mailing Lists Names are: "+mlNames);
            System.out.println("");  
        }
    }
    
    /** Test of getPersonAddressType method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testGetPersonAddressType() throws Exception {
        System.out.println("testGetPersonAddressType: Testing to get the address type of a particular address.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        
        // Get the address type for a particular person address
        Integer addrType = mlBean.getPersonAddressType(addressPk);
        mlBean.remove();
        if (addrType == null) {
            displayError();
            fail("Unable to get address type for addressPk="+addressPk+".");
        } else {
            System.out.println("Address Type for addressPk="+addressPk+" is: "+addrType+".");
            System.out.println("");  
        }
    }
    
    /** Test of getPersonAddresses method, of class org.afscme.enterprise.person.ejb.MaintainPersonMailingListsBean. */
    public void testGetPersonAddresses() throws Exception {
        System.out.println("testGetPersonAddresses: Testing to retrieve all addresses that belong to a person.");
        MaintainPersonMailingLists mlBean = JNDIUtil.getMaintainPersonMailingListsHome().create();
        
        // Get all addresses that belong to a person
        List list = mlBean.getPersonAddresses(personPk);
        mlBean.remove();
        if (list == null) {
            displayError();
            fail("Unable to get address for personPk="+personPk+".");
        } else {
            System.out.println("Addresses for personPk="+personPk+" are: "+list+".");
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
        System.out.println("PersonPk=10000001 - Admin person");
        System.out.println("MailingListPk=1 - Area Directors");
        System.out.println("AddressPk=1 - 8 Lemon Road..");        
    }
    
}
