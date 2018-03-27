/*
 * MaintainOrganizationsBeanTest.java
 * JUnit based test
 *
 * Created on May 21, 2003, 8:13 PM
 */

package org.afscme.enterprise.organization.ejb;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrganizationAssociateData;
import org.afscme.enterprise.organization.OrganizationCriteria;
import org.afscme.enterprise.organization.OrganizationData;
import org.afscme.enterprise.organization.OrganizationResult;
import org.afscme.enterprise.organization.OrgAssociateResult;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 *
 * @author lmark
 */
public class MaintainOrganizationsBeanTest extends TestCase {
    
    public MaintainOrganizationsBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainOrganizationsBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testEjbCreate() throws NamingException, CreateException, RemoveException {
        System.out.println("testEjbCreate: Creating MaintainOrganizations EJB");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();
        if (maintainOrgs == null)
            fail("MaintainOrganizations was null");
        maintainOrgs.remove();        
    }
    
    /** Test of searchOrgs method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testSearchOrgs() throws NamingException, CreateException, RemoveException {
        System.out.println("testSearchOrgs: Testing Searching for Organizations");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        // Fill the criteria object
        System.out.println("Setting up search criteria"); 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName(null);
        criteria.setOrgType(new Integer(28001));
        criteria.setOrgWebSite(null);
        criteria.setMarkedForDeletion(2);
        criteria.setCity("Washington");
        criteria.setOfficeAreaCode("202");
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        assertEquals(2, orgCount);
        assertEquals(result.size(), orgCount);
        
        System.out.println("Count for all types of Libraries in Washington with Area Code 202 = " + orgCount);

        maintainOrgs.remove();
        if (orgCount == 0) {
            fail("Search Organizations result was empty.");
        } else {
            System.out.println("+++++++++ Results of search Organizations = " + TextUtil.toString(result));
            System.out.println("");              
        }
    }

    /** Test of getOrganizationName method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetOrganizationName() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrganizationName: Testing to Get the Org Name");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        // Search for - OrgName: AFLCIO LIBRARY
        System.out.println("Searching for PK for OrgName: AFLCIO LIBRARY"); 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("AFLCIO LIBRARY");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
        }
        else { 
            fail("Multiple Organization Data objects for getOrganizationName() Test.");
        }
        
        // retrieve the org name 
        System.out.println("Retrieving Organization Name with PK = " + orgPk);
        String orgName = maintainOrgs.getOrganizationName(orgPk);
        
        System.out.println("+++++++++ Organization Name retrieved successfully: " + orgName);
        System.out.println("");

        maintainOrgs.remove();        
    }
        
    /** Test of getOrgDetail method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetOrgDetail() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgDetail: Testing to Get the Org Detail");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();
      
        // Search for - OrgName: AFLCIO LIBRARY
        System.out.println("Searching for PK for OrgName: AFLCIO LIBRARY"); 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("AFLCIO LIBRARY");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: AFLCIO LIBRARY = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for getOrgDetail() Test.");
        }
        
        // retrieve the external org detail - OrgName: AFLCIO LIBRARY
        System.out.println("Retrieving Organization with PK = " + orgPk);
        OrganizationData orgData = maintainOrgs.getOrgDetail(orgPk);
        
        maintainOrgs.remove();
        
        if (orgData == null) {
            fail("Organization Data was null.");
        } else {
            System.out.println("+++++++++ Organization Data retrieved successfully: " + orgData.toString());
            System.out.println("");              
        }
    }
    
    /** Test of updateOrgDetail method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testUpdateOrgDetail() throws NamingException, CreateException, RemoveException {
        System.out.println("testUpdateOrgDetail: Testing Update of Organization");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        // Search for - OrgName: WORD VIEW PUBLISHERS
        System.out.println("Searching for PK for OrgName: WORD VIEW PUBLISHERS"); 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("WORD VIEW PUBLISHERS");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: WORD VIEW PUBLISHERS = " + orgPk);            
        }
        else { 
            fail("Multiple Organization Data objects for updateOrgDetail() Test.");
        }
        
        // retrieve the old external org detail
        System.out.println("Retrieving Organization with PK = " + orgPk);
        OrganizationData oldData = maintainOrgs.getOrgDetail(orgPk);
       
        if (oldData == null) {
            fail("Organization Data was null.");
        } else {
            System.out.println("+++++++++ Pre-Update Organization Data retrieved: " + oldData.toString());
        }
        
        // Fill the org object to be updated
        System.out.println("Setting up update organization data"); 
        OrganizationData data = new OrganizationData();

        data.setOrgPK(orgPk);
        data.setOrgNm("AT&T");
        data.setOrgType(new Integer(28007));
        data.setOrgWebURL("www.att.com");
        data.setMarkedForDeletion(Boolean.TRUE);
        data.setOrgEmailDomain("@att.com");
       
        // update the org
        Integer updatePk = new Integer(10000001);        
        maintainOrgs.updateOrgDetail(data, updatePk);
        System.out.println("+++++++++ Organization Updated for PK = " + orgPk);
        
        // retrieve the updated external org detail
        System.out.println("Retrieving Organization with PK = " + orgPk);
        OrganizationData updData = maintainOrgs.getOrgDetail(orgPk);

        maintainOrgs.remove();
        
        if (updData == null) {
            fail("Organization Data was null.");
        } else {
            System.out.println("+++++++++ Post-Update Organization Data retrieved: " + updData.toString());
            System.out.println("");              
        }
    }
    
    /** Test of addOrg method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testAddOrg() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddOrg: Testing Add of an Organization");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();
        
        String orgName = "George Washington University";
        Integer orgType = new Integer("28002");
        String orgURL = "www.gwu.edu";
        Integer creatorPk = new Integer(10000001);
        Integer orgPK = maintainOrgs.addOrg(orgName, orgType, orgURL, creatorPk);      
        
        System.out.println("+++++++++ Organization Added for " + orgName + " with PK = " + orgPK);

        // retrieve the newly added external org detail
        System.out.println("Retrieving Organization with PK = " + orgPK);
        OrganizationData orgData = maintainOrgs.getOrgDetail(orgPK);
        
        maintainOrgs.remove();
        
        if (orgData == null) {
            fail("Organization Data was null.");
        } else {
            System.out.println("+++++++++ New Organization Data retrieved successfully: " + orgData.toString());
            System.out.println("");              
        }        
    }
    
    /** Test of getDuplicateOrgs method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetDuplicateOrgs() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetDuplicateOrgs: Testing Get DuplicateOrgs");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        // Fill the criteria object
        System.out.println("Setting up page criteria"); 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.getDuplicateOrgs("MUNICIPAL REF LIBRARY", criteria, result);
        assertEquals(3, orgCount);
        assertEquals(result.size(), orgCount);
        
        System.out.println("Count for all duplicate Organizations with name 'MUNICIPAL REF LIBRARY' = " + orgCount);

        maintainOrgs.remove();
        if (orgCount == 0) {
            fail("Duplicate Organizations result was empty.");
        } else {
            System.out.println("+++++++++ Results of get duplicate Organizations = " + TextUtil.toString(result));
            System.out.println("");  
        }        
    }
    
    /** Test of isDuplicateOrgs method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testIsDuplicateOrgs() throws NamingException, CreateException, RemoveException {
        System.out.println("testIsDuplicateOrgs: Testing for IsDuplicateOrgs");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        boolean dupOrg = maintainOrgs.isDuplicateOrgs("AFLCIO LIBRARY");
        System.out.println("Is Duplicate Organization = 'AFLCIO LIBRARY'? " + dupOrg);
        assertTrue(dupOrg);
        maintainOrgs.remove(); 
    }

    /** Test of setPersonAssoc method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testSetPersonAssoc() throws NamingException, CreateException, RemoveException {
        System.out.println("testSetPersonAssoc: Testing to associate a Person to an External Organization");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get OrgPk for OrgName: LIBRARY OF CONGRESS
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("LIBRARY OF CONGRESS");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: LIBRARY OF CONGRESS = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for setPersonAssoc() Test.");
        }
        
        //associate existing person to external org
        System.out.println("Associate an existing person to an Organization with PK = " + orgPk);
        Integer personPk = new Integer(10000501);
        Integer orgPosTitle = new Integer(76001);
        String comment = "Test Comment";
        Integer creatorPk = new Integer(10000001);
        if (maintainOrgs.setPersonAssoc(orgPk, personPk, orgPosTitle, comment, creatorPk)) {
            List results = new LinkedList();
            int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
            System.out.println("+++++++++ Results of set Person Associate to Organization = " + TextUtil.toString(results));
            System.out.println("");
        }    
        else { 
            fail("Person NOT associated to Organization for setPersonAssoc() Test.");
        }
        
        maintainOrgs.remove();
    }

    /** Test of addPersonAssoc method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testAddPersonAssoc() throws NamingException, CreateException, RemoveException {
        System.out.println("testAddPersonAssoc: Testing to add a New Person and associate to an Organization");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get OrgPk for OrgName: LIBRARY OF CONGRESS
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("LIBRARY OF CONGRESS");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: LIBRARY OF CONGRESS = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for addPersonAssoc() Test.");
        }
        
        //create person
        System.out.println("Create a new Person to Associate to an Organization with PK = " + orgPk);

        PersonData personData = new PersonData();
        personData.setFirstNm("AssociateFirstName");
        personData.setLastNm("AssociateLastName");
        personData.setSsn("111223333");
        personData.setSsnValid(new Boolean(true));
        personData.setSsnDuplicate(new Boolean(false));

        CommentData cd = new CommentData();
        cd.setComment("New Person Comment");
        personData.setTheCommentData(cd);
        
        Integer orgPosTitle = new Integer(76006);
        String comment = "New Person Associate Comment";
        Integer creatorPk = new Integer(10000001);
        
        Integer personPk = maintainOrgs.addPersonAssoc(orgPk, new NewPerson(personData), orgPosTitle, comment, creatorPk);

        if (personPk == null) {
            fail("Person NOT added or associated to Organization for addPersonAssoc() Test.");
        }    
        else { 
            OrganizationAssociateData orgAssociate = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
            System.out.println("+++++++++ Results of successful add Person Associate to Organization = " + orgAssociate);
            System.out.println("");
        }
        
        maintainOrgs.remove();
    }
    
    /** Test of removePersonAssoc method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testRemovePersonAssoc() throws NamingException, CreateException, RemoveException {
        System.out.println("testRemovePersonAssoc: Testing to remove association between Person and Organization");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get OrgPk for - OrgName: DREXEL UNIV LIBR
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("DREXEL UNIV LIBR");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: DREXEL UNIV LIBR = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for removePersonAssoc() Test.");
        }
        
        //associate existing person to external org
        System.out.println("Associate an existing person to an Organization with PK = " + orgPk);
        Integer personPk = new Integer(10000801);
        Integer orgPosTitle = new Integer(76003);
        String comment = "Test Add Remove Comment";
        Integer creatorPk = new Integer(10000001);
        if (maintainOrgs.setPersonAssoc(orgPk, personPk, orgPosTitle, comment, creatorPk)) {
            OrganizationAssociateData orgAssociate = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
            System.out.println("+++++++++ Results of add Person Associate to Organization = " + orgAssociate);
            System.out.println("");
            
            //remove org associate from external org
            System.out.println("Removing Org Associate from an Organization");
            if (maintainOrgs.removePersonAssoc(orgPk, personPk)) {
                System.out.println("+++++++++ Person =  " + personPk + " successfully removed or unassociated from Organization = " + orgPk);
                System.out.println("");
            }
            else { 
                fail("Person NOT removed from Organization for removePersonAssoc() Test.");
            }
        }    
        else { 
            fail("Person NOT associated to Organization for removePersonAssoc() Test.");
        }
        
        maintainOrgs.remove();
    }
    
    /** Test of updateOrgAssociateDetail method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testUpdateOrgAssociateDetail() throws NamingException, CreateException, RemoveException {
        System.out.println("testUpdateOrgAssociateDetail: Testing to Update Org Associate Detail information");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get OrgPk for AFLCIO LIBRARY
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("AFLCIO LIBRARY");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: AFLCIO LIBRARY = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for updateOrgAssociateDetail() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            OrgAssociateResult orgAssociateResult = (OrgAssociateResult) results.get(0);
            Integer personPk = orgAssociateResult.getPersonPk();
            
            //retrieve org associates detail before update
            OrganizationAssociateData oldData = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
            System.out.println("+++++++++ Pre-Update Organization Associate Data retrieved: " + oldData.toString());
            
            PersonData personData = (PersonData) oldData.getPersonData();
            Integer orgPosTitle = new Integer((oldData.getOrgPositionTitle().intValue()) + 1);
            String comment = "Test Update Comment";
            Integer updatePk = new Integer(10000001);
            
            //update org associates detail 
            if (maintainOrgs.updateOrgAssociateDetail(orgPk, personData, orgPosTitle, comment, updatePk)) {
                System.out.println("+++++++++ Update of Org Associate " + "(OrgPk=" + orgPk + ",PersonPk=" + personPk + ") successful!!");

                //retrieve org associates detail after update                
                OrganizationAssociateData newData = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
                System.out.println("+++++++++ Post-Update Organization Associate Data retrieved: " + newData.toString());
                System.out.println("");
            }    
            else { 
                fail("Org Associate NOT updated for updateOrgAssociateDetail() Test.");
            }
        }    
        else { 
            fail("No Persons associated to Organization for updateOrgAssociateDetail() Test.");
        }
        
        maintainOrgs.remove();
    }
    
    /** Test of getOrgAssociateLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetOrgAssociateLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgAssociateLocation: Testing to get the Location Data for an Org Associate");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get OrgPk for IND REL LIBR MIT 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("IND REL LIBR MIT");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: IND REL LIBR MIT = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for getOrgAssociateLocation() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            OrgAssociateResult orgAssociateResult = (OrgAssociateResult) results.get(0);
            Integer personPk = orgAssociateResult.getPersonPk();
            
            //retrieve org associates location
            LocationData location = maintainOrgs.getOrgAssociateLocation(orgPk, personPk);
            if (location == null) {
                fail("Location Data was null for getOrgAssociateLocation() Test.");
            } else {
                System.out.println("+++++++++ Results of Org Associate retrieval of Location " + 
                                "(OrgPk=" + orgPk + ",PersonPk=" + personPk + "): " + location.toString());
                System.out.println("");
            }
        }
        else { 
            fail("No Persons associated to Organization for getOrgAssociateLocation() Test.");
        }
        
        maintainOrgs.remove();
    }
    
    /** Test of setOrgAssociateLocation method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testSetOrgAssociateLocation() throws NamingException, CreateException, RemoveException {
        System.out.println("testSetOrgAssociateLocation: Testing to set a Location for an Org Associate");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();
        MaintainOrgLocations maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();

        //get Org Pk for UNIVERSITY OF MASSACHUSETTS 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("UNIVERSITY OF MASSACHUSETTS");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: UNIVERSITY OF MASSACHUSETTS = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for setOrgAssociateLocation() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        Integer personPk = null;
        Integer orgLocation = null;
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            OrgAssociateResult orgAssociateResult = (OrgAssociateResult) results.get(0);
            personPk = orgAssociateResult.getPersonPk();
            
            //retrieve org associates location
            OrganizationAssociateData oldData = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
            System.out.println("+++++++++ Organization Associate Data - prior to Update: " + oldData.toString());
            System.out.println("");
            
            LocationData location = maintainOrgs.getOrgAssociateLocation(orgPk, personPk);
            orgLocation = location.getOrgLocationPK();
            
            //get the primary location for organization
            Integer primaryLocation = maintainOrgLocations.getOrgPrimaryLocationPK(orgPk);
            System.out.println("Get the Primary Location for Organization - primaryLocationPk = " + primaryLocation);
            
            //set the location if not the primary location
            if (orgLocation.intValue() != primaryLocation.intValue()) {
                System.out.println("Setting the Location for the Org Associate to use locationPk = " + primaryLocation);
                if (maintainOrgs.setOrgAssociateLocation(orgPk, personPk, primaryLocation)) {
                    OrganizationAssociateData newData = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
                    System.out.println("+++++++++ Set of Organization Associate Location successful: " + newData.toString());
                    System.out.println("");
                }    
                else {
                    fail("Location NOT set to Primary Location for setOrgAssociateLocation() Test.");
                }
            }
        }    
        else { 
            fail("Org Associate Location NOT set for setOrgAssociateLocation() Test.");
        }

        //clean up data to use again
        System.out.println("Re-set the Location to old one for clean up ...");
        maintainOrgs.setOrgAssociateLocation(orgPk, personPk, orgLocation);
        
        maintainOrgs.remove();
        maintainOrgLocations.remove();
    }
    
    /** Test of setOrgAssociateLocationasPrimary method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testSetOrgAssociateLocationasPrimary() throws NamingException, CreateException, RemoveException {
        System.out.println("testSetOrgAssociateLocationasPrimary: Testing to set the Primary Location for an Org Associate");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get Org Pk for UNIVERSITY OF MASSACHUSETTS 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("UNIVERSITY OF MASSACHUSETTS");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: UNIVERSITY OF MASSACHUSETTS = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for setOrgAssociateLocationasPrimary() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        Integer personPk = null;
        Integer orgLocation = null;        
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            OrgAssociateResult orgAssociateResult = (OrgAssociateResult) results.get(0);
            personPk = orgAssociateResult.getPersonPk();
            
            //retrieve org associates location
            OrganizationAssociateData oldData = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
            System.out.println("+++++++++ Organization Associate Data - prior to Update: " + oldData.toString());
            System.out.println("");
            
            LocationData location = maintainOrgs.getOrgAssociateLocation(orgPk, personPk);
            orgLocation = location.getOrgLocationPK();
           
            //set the primary location for the org associate
            System.out.println("Setting the Location for the Org Associate to use the Primary Location ");
            if (maintainOrgs.setOrgAssociateLocationasPrimary(orgPk, personPk)) {
                OrganizationAssociateData newData = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
                System.out.println("+++++++++ Set of Organization Associate for Primary Location successful: " + newData.toString());
                System.out.println("");
            }    
            else {
                fail("Location NOT set to Primary Location for setOrgAssociateLocationasPrimary() Test.");
            }
        }    
        else { 
            fail("Org Associate Location NOT set to Primary Location for setOrgAssociateLocationasPrimary() Test.");
        }

        //clean up data to use again
        System.out.println("Re-set the Location to old one for clean up ...");
        maintainOrgs.setOrgAssociateLocation(orgPk, personPk, orgLocation);
        
        maintainOrgs.remove();
    }
    
    /** Test of getOrgAssociates method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetOrgAssociates() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgAssociates: Testing to get all the Organization Associates for an Organization");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        // Get OrgPK for AFLCIO LIBRARY
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("AFLCIO LIBRARY");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: AFLCIO LIBRARY = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for getOrgAssociates() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            System.out.println("+++++++++ Retrieved: " + associateCount + " Org Associate(s) from Organization: " 
                                + orgPk + " successfully!");
            System.out.println("+++++++++ Results for getting Organization Associate Data: " + TextUtil.toString(results));
            System.out.println("");
        }    
        else { 
            fail("No Org Associates exists for Organizations for getOrgAssociates() Test.");
        }

        maintainOrgs.remove();
    }

    /** Test of getPersonOrgAssociates method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetPersonOrgAssociates() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetPersonOrgAssociates: Testing to get all the Organizations (OrgAssociate records) for a Person");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //retrieve org associates for person
        Integer personPk = new Integer(10000101);
        List results = new LinkedList();
        System.out.println("Get all the Org Associate records for an existing person with PK = " + personPk);
        results = maintainOrgs.getPersonOrgAssociates(personPk, new SortData());
        if (results.size() > 0) {
            System.out.println("+++++++++ Retrieved: " + results.size() + " Org Associate Record(s) for Person: " 
                                + personPk + " successfully!");
            System.out.println("+++++++++ Results for getting Organization Associate Data: " + TextUtil.toString(results));
            System.out.println("");
        }    
        else { 
            fail("No Org Associate records exist for Person for getPersonOrgAssociates() Test.");
        }

        maintainOrgs.remove();
    }
    
    /** Test of getOrgAssociateDetail method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetOrgAssociateDetail() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetOrgAssociateDetail: Testing to Get the Detail information for an Organization Associate");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get Org Pk for UNIVERSITY OF MASSACHUSETTS 
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("UNIVERSITY OF MASSACHUSETTS");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: UNIVERSITY OF MASSACHUSETTS = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for getOrgAssociateDetail() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        Integer personPk = null;
        Integer orgLocation = null;
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            OrgAssociateResult orgAssociateResult = (OrgAssociateResult) results.get(0);
            personPk = orgAssociateResult.getPersonPk();
            
            //retrieve org associates detail data
            OrganizationAssociateData associate = maintainOrgs.getOrgAssociateDetail(orgPk, personPk);
            System.out.println("+++++++++ Retrieved Org Associate Detail for Person with Pk = " + personPk + " successfully!");
            System.out.println("+++++++++ Results of Organization Associate Detail Data: " + associate.toString());
            System.out.println("");
        }    
        else { 
            fail("No Org Associate Detail information exists for getOrgAssociateDetail() Test.");
        }

        maintainOrgs.remove();
    }

    /** Test of getCommentHistory method, of class org.afscme.enterprise.organization.ejb.MaintainOrganizationsBean. */
    public void testGetCommentHistory() throws NamingException, CreateException, RemoveException {
        System.out.println("testGetCommentHistory: Testing the getting of Comment History for an Org Associate ");
        MaintainOrganizations maintainOrgs = JNDIUtil.getMaintainOrganizationsHome().create();

        //get Org Pk for AFLCIO LIBRARY
        OrganizationCriteria criteria = new OrganizationCriteria();
	List result = new LinkedList();

        criteria.setOrgName("AFLCIO LIBRARY");
        criteria.setMarkedForDeletion(2);
	criteria.setSortField(OrganizationCriteria.FIELD_NAME);
	criteria.setSortOrder(OrganizationCriteria.SORT_DESCENDING);
        criteria.setPageSize(25);
        int orgCount = maintainOrgs.searchOrgs(criteria, result);
        Integer orgPk = null;
        
        if (orgCount == 1) {
            OrganizationResult orgResult = (OrganizationResult) result.get(0);
            orgPk = orgResult.getOrgPK();
            System.out.println("PK for OrgName: AFLCIO LIBRARY = " + orgPk);
        }
        else { 
            fail("Multiple Organization Data objects for getCommentHistory() Test.");
        }
        
        //retrieve org associates for organization
        System.out.println("Get all the Org Associates for Organization with PK = " + orgPk);
        List results = new LinkedList();
        Integer personPk = null;
        int associateCount = maintainOrgs.getOrgAssociates(orgPk, new SortData(), results);
        if (associateCount > 0) {
            OrgAssociateResult orgAssociateResult = (OrgAssociateResult) results.get(0);
            personPk = orgAssociateResult.getPersonPk();
            
            //retrieve comment history for org associate
            List commentResult = maintainOrgs.getCommentHistory(orgPk, personPk);
            System.out.println("+++++++++ Retrieved Comment History for Org Associate with OrgPk = " + orgPk 
                                + " and PersonPk = " + personPk + " successfully!");
            System.out.println("+++++++++ Results of Comment History for Organization Associate: " + TextUtil.toString(commentResult));
            System.out.println("");
        }    
        else { 
            fail("No Comment History for Org Associate for getCommentHistory() Test.");
        }

        maintainOrgs.remove();
    }    
}
