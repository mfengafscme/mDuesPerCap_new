/*
 * MaintainOrgLocationsBeanTest.java
 * JUnit based test
 *
 * Created on June 12, 2003, 2:22 PM
 */

package org.afscme.enterprise.organization.ejb;

import java.util.Iterator;
import java.util.LinkedList;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.afscme.enterprise.codes.Codes.OrgAddressType;
import org.afscme.enterprise.codes.Codes.OrgPhoneType;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.organization.OrganizationData;
import org.afscme.enterprise.organization.OrgPhoneData;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;


/**
 *
 * @author lmark
 */
public class MaintainOrgLocationsBeanTest extends TestCase {
    
    public MaintainOrgLocationsBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainOrgLocationsBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testEjbCreate() throws NamingException, CreateException, RemoveException {
        System.out.println("testEjbCreate: Creating MaintainOrgLocations EJB");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();
        if (maintainOrgLocations == null)
            fail("MaintainOrgLocations was null");
        maintainOrgLocations.remove();        
    }
    
    /** Test of getOrgPrimaryLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testGetOrgPrimaryLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgPrimaryLocation: Testing Getting the Primary Location for Organizations");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();      

        // Get Org Primary Location for - OrgName: LIBRARY OF CONGRESS
        System.out.println("Retrieving Primary Location for Org: LIBRARY OF CONGRESS (OrgPK: 67)");
        Integer orgPk = new Integer("67");

        LocationData primaryLocation = maintainOrgLocations.getOrgPrimaryLocation(orgPk);

        maintainOrgLocations.remove();
        
        if (primaryLocation == null) {
            System.out.println("+++++++++ No Primary Location Data for Organization! ");
            System.out.println("");
        } else {
            System.out.println("+++++++++ Primary Location Data for Organization retrieved successfully: " + primaryLocation.toString());
            System.out.println("");
        }
    }
    
    /** Test of getOrgLocations method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testGetOrgLocations() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgLocations: Testing Getting all of the Locations for an Organization");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();      

        // Get all Locations for - OrgName: DREXEL UNIV LIBR
        System.out.println("Retrieving all Locations for Org: DREXEL UNIV LIBR (OrgPK: 61)");
        Integer orgPk = new Integer("61");

        LinkedList locations = (LinkedList) maintainOrgLocations.getOrgLocations(orgPk);

        maintainOrgLocations.remove();
        
        if (locations == null) {
            fail("No Location Data for Organization!");            
        } else {
            System.out.println("+++++++++ Results of getting all Locations for Organization = " + TextUtil.toString(locations));
            System.out.println("");
        }        
    }
    
    /** Test of addOrgLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testAddOrgLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddOrgLocation: Testing Add of an Organization Location");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();
        
        // Add a new Location to Organization - OrgName: GOLDEN GATE UNIVERSITY
        System.out.println("Adding a new Location for Org: GOLDEN GATE UNIVERSITY (OrgPK: 62)");
        Integer orgPk = new Integer("62");
        
        // Main location data
        LocationData locationData = new LocationData();
        locationData.setLocationNm("New Test Location");
        locationData.setPrimaryLocationBoolean(null);
        locationData.setOrgPk(orgPk);
        
        // Location Address data
        LinkedList addressList = new LinkedList();
        OrgAddressRecord address = new OrgAddressRecord();
        address.setType(OrgAddressType.REGULAR);
        address.setAttentionLine("ATTN: To Whom It May Concern");
        address.setAddr1("1900 Gallows Road");
        address.setAddr2("5th Floor");
        address.setCity("Vienna");
        address.setCounty("Fairfax");
        address.setState("VA");
        address.setCountryPk(new Integer(9001));  // United States
        address.setZipCode("22182");
        address.setZipPlus("1111");

        addressList.add(address);
      
        // Location Phone data
        LinkedList phoneList = new LinkedList();
        OrgPhoneData phone1 = new OrgPhoneData();
        phone1.setPhoneType(OrgPhoneType.LOC_PHONE_OFFICE);
        phone1.setCountryCode("1");
        phone1.setAreaCode("703");
        phone1.setPhoneNumber("5065000");
        phone1.setPhoneExtension("5401");
        phoneList.add(phone1);
        
        OrgPhoneData phone2 = new OrgPhoneData();
        phone2.setPhoneType(OrgPhoneType.LOC_PHONE_FAX);
        phone2.setCountryCode("1");
        phone2.setAreaCode("703");
        phone2.setPhoneNumber("5065111");
        phone1.setPhoneBadFlag(new Boolean(true));
        phoneList.add(phone2);

        locationData.setOrgAddressData(addressList);
        locationData.setOrgPhoneData(phoneList);
        
        Integer creatorPk = new Integer(10000001);
        
        Integer orgLocationPK = maintainOrgLocations.addOrgLocation(locationData, orgPk, creatorPk);      
        System.out.println("+++++++++ Location Added for Org_PK: " + orgPk + " with PK created: " + orgLocationPK);

        // retrieve the newly added org location
        System.out.println("Retrieving Location Data with Org_Locations_PK = " + orgLocationPK);
        LocationData addOrgData = maintainOrgLocations.getOrgLocation(orgLocationPK);

        maintainOrgLocations.remove();
        
        if (orgLocationPK == null) {
            fail("Location Data was not added.");
        } else {
            System.out.println("+++++++++ New Organization Data added successfully: " + addOrgData.toString());
            System.out.println("");              
        }
    }
    
    /** Test of updateOrgLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testUpdateOrgLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testUpdateOrgLocation: Testing Update of an Organization Location");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();        

        // retrieve the original org location data for Organization - OrgName: GOLDEN GATE UNIVERSITY
        System.out.println("Retrieving Org Location Data for Org: GOLDEN GATE UNIVERSITY (OrgPK: 62)");
        Integer orgPk = new Integer("62");
        LinkedList orgLocations = (LinkedList) maintainOrgLocations.getOrgLocations(orgPk);

        Integer orgLocationPk = null;
        Iterator it = orgLocations.iterator();
        while (it.hasNext()) {

            LocationData location = (LocationData)it.next();
            if (location.getLocationNm().equals("New Test Location")) {
                orgLocationPk = location.getOrgLocationPK();
            }    
        }
        
        if (orgLocationPk == null) {
            fail("Location Data retrieved for update was null.");
        }

        // Update Location 
        System.out.println("Retrieving data before Update");
        LocationData updLocation = maintainOrgLocations.getOrgLocation(orgLocationPk);
        System.out.println("+++++++++ Original Location Data: " + updLocation.toString());

        // Main location data
        updLocation.setPrimaryLocationBoolean(new Boolean(true));
       
        // Location Address data
        LinkedList addressList = updLocation.getOrgAddressData();

        OrgAddressRecord address = new OrgAddressRecord();
        address.setType(OrgAddressType.SHIPPING);
        address.setAttentionLine("ATTN: Shipping Dock");
        address.setAddr1("1900 Gallows Road");
        address.setAddr2("Back Entrance");
        address.setCity("Vienna");
        address.setState("VA");
        address.setCountryPk(new Integer(9001));  // United States
        address.setZipCode("22182");
        address.setZipPlus("2222");
        address.setBad(true);        

        RecordData recordData = new RecordData();
        recordData.setPk(new Integer(0));
        address.setRecordData(recordData);
        
        addressList.add(address);
        
        updLocation.setOrgAddressData(addressList);
      
        // Location Phone data
        LinkedList phoneList = updLocation.getOrgPhoneData();
        LinkedList updPhoneList = new LinkedList();        
        Iterator it2 = phoneList.iterator();
        while (it2.hasNext()) {

            OrgPhoneData phone = (OrgPhoneData)it2.next();
            if (phone.getPhoneType().equals(OrgPhoneType.LOC_PHONE_FAX)) {
                phone.setAreaCode("571");
                phone.setPhoneBadFlag(null);
                updPhoneList.add(phone);
            }
            else {
                updPhoneList.add(phone);
            }    
        }

        updLocation.setOrgPhoneData(updPhoneList);        
        
        Integer creatorPk = new Integer(10000001);
        
        maintainOrgLocations.updateOrgLocation(updLocation, orgPk, creatorPk);      
        System.out.println("+++++++++ Location Updated for Org_PK: " + orgPk + " with PK created: " + orgLocationPk);

        // retrieve the newly updated org location
        System.out.println("Retrieving Location Data with Org_Locations_PK = " + orgLocationPk);
        LocationData updLocData = maintainOrgLocations.getOrgLocation(orgLocationPk);

        maintainOrgLocations.remove();
        
        if (orgLocationPk == null) {
            fail("Location Data was not updated.");
        } else {
            System.out.println("+++++++++ Updated Location Data (added Address and changed Phone): " + updLocData.toString());
            System.out.println("");
        }
    }
    
    /** Test of removeOrgLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testRemoveOrgLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testRemoveOrgLocation: Testing Remove of an Organization Location");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();        

        // retrieve the primary org location Pk for Organization - OrgName: MUNICIPAL REF LIBRARY
        System.out.println("Retrieving Primary Org Location PK for Org: MUNICIPAL REF LIBRARY (OrgPK: 69)");
        Integer orgPk = new Integer("69");
        Integer orgLocationsPk = maintainOrgLocations.getOrgPrimaryLocationPK(orgPk);
        System.out.println("Primary Location PK = " + orgLocationsPk);
        
        // delete the org location
        System.out.println("+++++++++ Removing the Primary Location for Organization with Org_Locations_Pk = " + orgLocationsPk);
        Integer creatorPk = new Integer(10000001);     
        maintainOrgLocations.removeOrgLocation(orgLocationsPk, orgPk, creatorPk);
        
        maintainOrgLocations.remove();

        Integer testDeletePk = maintainOrgLocations.getOrgPrimaryLocationPK(orgPk);

        if (testDeletePk == null) {
            System.out.println("+++++++++ Deleted Location Data (with Address and Phone) successfully! ");
            System.out.println("");            

        } else {
            fail("Location Data was not deleted.");
        }
    }
    
    /** Test of getOrgLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testGetOrgLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgLocation: Testing Getting a Location for an Organization by its Org Location PK");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();      

        // Get all Locations for - OrgName: WOODBURY CNTY LABOR CN
        System.out.println("Retrieving all Locations for Org: WOODBURY CNTY LABOR CN (OrgPK: 77)");
        Integer orgPk = new Integer("77");
        LinkedList locations = (LinkedList) maintainOrgLocations.getOrgLocations(orgPk);

        if (locations.size() != 1) {
            fail("More than one Location Data for Organization.");
        }
        else {
            LocationData location = (LocationData) locations.get(0);
            Integer orgLocationsPk = location.getOrgLocationPK();
            
            System.out.println("Retrieving the Location Data by Org_Locations_Pk = " + orgLocationsPk);
            LocationData locationPkData = maintainOrgLocations.getOrgLocation(orgLocationsPk);

            maintainOrgLocations.remove();

            if (locationPkData == null) {
                fail("Location Data was not retrieved.");
            } else {
                System.out.println("+++++++++ Results of getting Location for Organization by Org Location Pk: " + locationPkData.toString());
                System.out.println("");
            }
        }
    }
    
    /** Test of getOrgPrimaryLocationPK method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testGetOrgPrimaryLocationPK() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgPrimaryLocationPK: Testing Getting the Primary Location of an Organization");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();        

        // retrieve the primary org location Pk for Organization - OrgName: LABOR EDUCATION PROJECT
        System.out.println("Retrieving Primary Org Location PK for Org: LABOR EDUCATION PROJECT (OrgPK: 65)");
        Integer orgPk = new Integer("65");
        Integer orgLocationsPk = maintainOrgLocations.getOrgPrimaryLocationPK(orgPk);

        maintainOrgLocations.remove();
        
        if (orgLocationsPk == null) {
            fail("Primary Location PK was not found.");
        } else {
            System.out.println("+++++++++ Primary Location PK was successfully retrieved: " + orgLocationsPk);
            System.out.println("");
        }        
    }
    
    /** Test of isPrimaryLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testIsPrimaryLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testIsPrimaryLocation: Testing if an Organization has a Primary Location");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();        

        // check if primary location exists for Organization - OrgName: LABOR EDUCATION PROJECT
        System.out.println("Checking if Primary Location exists for Org: LABOR EDUCATION PROJECT (OrgPK: 65)");
        Integer orgPk = new Integer("65");
        boolean hasPrimary = maintainOrgLocations.isPrimaryLocation(orgPk);
        
        System.out.println("+++++++++ Has Primary Location for Organization = 'LABOR EDUCATION PROJECT LIBRARY'? " + hasPrimary);
        System.out.println("");
        assertTrue(hasPrimary);
        maintainOrgLocations.remove(); 
    }
    
    /** Test of hasLocations method, of class org.afscme.enterprise.organization.ejb.MaintainOrgLocationsBean. */
    public void testHasLocations() throws NamingException, CreateException, RemoveException {
        System.out.println("testHasLocations: Testing if an Organization has any Locations");
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();        

        // check if any locations exist for Organization - OrgName: DETROIT PUB LIB
        System.out.println("Checking if any Locations exist for Org: DETROIT PUB LIB (OrgPK: 60)");
        Integer orgPk = new Integer("60");
        boolean hasLocations = maintainOrgLocations.hasLocations(orgPk);
        
        System.out.println("+++++++++ Has Locations for Organization = 'DETROIT PUB LIB'? " + hasLocations);
        System.out.println("");
        assertTrue(hasLocations);
        maintainOrgLocations.remove();         
    }
}
