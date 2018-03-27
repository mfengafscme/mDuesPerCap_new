/*
 * MaintainPersonsBeanTest.java
 * JUnit based test
 *
 * Created on May 14, 2003, 12:10 PM
 */

package org.afscme.enterprise.person.ejb;

import java.lang.Integer;
import junit.framework.*;
import org.afscme.enterprise.person.*;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.users.UserData;
import org.afscme.enterprise.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Iterator;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.DateUtil;

/**
 *
 * @author pevon
 */
public class MaintainPersonsBeanTest extends TestCase {

    public MaintainPersonsBeanTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainPersonsBeanTest.class);

        return suite;
    }

    /** Test of addCorrespondence method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    /*public void testAddCorrespondences() {
        try {
            System.out.println("testAddCorrespondence");
            MaintainPersonMailingLists bean = JNDIUtil.getMaintainPersonMailingListsHome().create();
            bean.addCorrespondences(1);
            bean.remove();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/
/** Test of addDemographicRelations method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testAddDemographicRelations() {
        System.out.println("testAddDemographicRelations");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of addPerson method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testAddPerson() {
        System.out.println("testAddPerson");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of addPersonAddress method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testAddPersonAddress() {
        System.out.println("testAddPersonAddress");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/

    /** Test of addPersonPhone method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testAddPersonPhone() {
        System.out.println("testAddPersonPhone");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/

    /** Test of getCorrespondenceHistory method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    /* public void testGetCorrespondenceHistory() {
        try {
            System.out.println("testGetCorrespondenceHistory");
            MaintainPersonMailingLists bean = JNDIUtil.getMaintainPersonMailingListsHome().create();
            Collection list = bean.getCorrespondenceHistory(new Integer(10001781), new SortData());
            Iterator it = list.iterator();
            while(it.hasNext()){
                CorrespondenceData cd = (CorrespondenceData)it.next();
                System.out.println(cd.getCorrespondenceDt());
                System.out.println(cd.getCorrespondenceName());
        }
        bean.remove();
        } catch(Exception e){
        }
    }
    */

    /** Test of getDemographicRelations method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testGetDemographicRelations() {
        System.out.println("testGetDemographicRelations");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of getDuplicateSSN method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testGetDuplicateSSN() throws Exception {
        System.out.println();
        System.out.println("testGetDuplicateSSN");
        List list = new ArrayList();

        // Add your test code below by replacing the default call to fail.
		MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        PersonCriteria personCriteria = new PersonCriteria();
        personCriteria.setSsn("773844400");
        System.out.println("True: 773844400");
        maintainPersons.getDuplicateSSN(personCriteria, list);
        System.out.println("Duplicate SSN: " + TextUtil.toString(list));

        System.out.println("False: 469590928");
		list.clear();
        personCriteria.setSsn("469590928");
        maintainPersons.getDuplicateSSN(personCriteria, list);
        System.out.println("Duplicate SSN: " + TextUtil.toString(list));

        System.out.println("end testGetDuplicateSSN");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of getExistingPersons method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
   public void testGetExistingPersons() throws Exception {
        System.out.println();
        System.out.println("testGetExistingPersons");
        Collection list = new ArrayList();
        PersonCriteria personCriteria = new PersonCriteria();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();

        System.out.println("True: Adam Adams");
        personCriteria.setFirstNm("Adam");
        personCriteria.setLastNm("Adams");
        list = maintainPersons.getExistingPersons(personCriteria);
        assertNotNull(list);
        System.out.println("Duplicate Person: " + TextUtil.toString(list));

        System.out.println("False: Larry thePenguin");
        personCriteria.setFirstNm("Larry");
        personCriteria.setLastNm("thePenguin");
        list = maintainPersons.getExistingPersons(personCriteria);
        assertNotNull(list);
        System.out.println("Duplicate Person: " + TextUtil.toString(list));

        System.out.println("end testGetExistingPersons");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of getGeneralDemographics method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testGetGeneralDemographics() {
        System.out.println("testGetGeneralDemographics");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of getPersonAddresses method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testGetPersonAddresses() {
        System.out.println("testGetPersonAddresses");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of getPersonDetail method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testGetPersonDetail() throws Exception {
        System.out.println();
        System.out.println("testGetPersonDetail");
        PersonData data = null;
        Integer personPk = new Integer(10000002);
        Integer dept = null;

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        data = maintainPersons.getPersonDetail(personPk, dept);
        assertNotNull(data);
        System.out.println("Person Detail: " + TextUtil.toString(data));

        System.out.println("end testGetPersonDetail");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of getPersonEmails method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testGetPersonEmails() throws Exception {
        System.out.println();
        System.out.println("testGetPersonEmails");
        Integer personPk = null;
        Collection list = new ArrayList();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        System.out.println("True: 10000002");
        personPk = new Integer(10000002);
        list = maintainPersons.getPersonEmails(personPk);
        assertNotNull(list);
        System.out.println("Person Emails: " + TextUtil.toString(list));

        System.out.println("end testGetPersonEmails");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of getPersonPhones method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean.
    public void testGetPersonPhones() throws Exception {
        System.out.println();
        System.out.println("testGetPersonPhones");
        Integer personPk = null;
        Integer dept = null;
        Collection list = new ArrayList();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        System.out.println("True: 10000002");
        personPk = new Integer(10000002);
        list = maintainPersons.getPersonPhones(personPk, dept);
        assertNotNull(list);
        System.out.println("Person Phones: " + TextUtil.toString(list));

        System.out.println("False: 999");
        personPk = new Integer(999);
        list = maintainPersons.getPersonPhones(personPk, dept);
        System.out.println("Person Phones: " + TextUtil.toString(list));

        System.out.println("end testGetPersonPhones");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of getPoliticalData method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testGetPoliticalData() {
        System.out.println("testGetPoliticalData");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/

    /** Test of isDuplicateSSN method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testIsDuplicateSSN() throws Exception {
        System.out.println();
        System.out.println("testIsDuplicateSSN");

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        // TRUE 642342070
        boolean dup = maintainPersons.isDuplicateSSN(new String("115071614"));
        System.out.println("Is SSN = 115071614 duplicate? " + dup);
        if (!dup) {
            System.out.println("No duplicates found!");
        } else {
            System.out.println("Duplicates have been found!!!");
        }

        // FALSE 999999999
        dup = maintainPersons.isDuplicateSSN(new String("469590928"));
        System.out.println("Is SSN = 469590928 duplicate? " + dup);
        if (!dup) {
            System.out.println("No duplicates found!");
        } else {
            System.out.println("Duplicates have been found!!!");
        }

        System.out.println("end testIsDuplicateSSN");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();

    }

    /** Test of isExistingPerson method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testIsExistingPerson() throws Exception {
        System.out.println();
        System.out.println("testIsExistingPerson");
        boolean dup;

        // Add your test code below by replacing the default call to fail.
        PersonCriteria personCriteria = new PersonCriteria();

        MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        // TRUE Adam Adams
        personCriteria.setFirstNm("Adam");
        personCriteria.setLastNm("Adams");
        dup = maintainPersons.isExistingPerson(personCriteria);
        System.out.println("Does a duplicate person exist for 'Adam Adams'? " + dup);
        if (!dup) {
            System.out.println("No duplicates found!");
        } else {
            System.out.println("Duplicates have been found!!!");
        }

        // FALSE Adam Adams 12/12/1950
        personCriteria.setFirstNm("Adam");
        personCriteria.setLastNm("Adams");
        personCriteria.setDob(Timestamp.valueOf("1950-12-12 00:00:00.00"));
        dup = maintainPersons.isExistingPerson(personCriteria);
        System.out.println("Does a duplicate person exist 'Adam Adams 12/12/1950'? " + dup);
        if (!dup) {
            System.out.println("No duplicates found!");
        } else {
            System.out.println("Duplicates have been found!!!");
        }

        System.out.println("end testIsExistingPerson");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();

    }

    /** Test of searchPersons method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testSearchPersons() throws Exception {
        System.out.println("testSearchPersons");
        System.out.println();
        boolean dup;
        int count = 0;
        Integer dept = new Integer(4001);
        ArrayList results = new ArrayList();

        MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
        // Add your test code below by replacing the default call to fail.
        PersonCriteria personCriteria = new PersonCriteria();
        personCriteria.setPage(0); // page one
        personCriteria.setPageSize(20);
        personCriteria.setFirstNm("Adam");
        personCriteria.setLastNm("Adams");
        System.out.println("testSearchPersons from criteria, FirstName: " + personCriteria.getFirstNm() +
        " LastName: " + personCriteria.getLastNm() +
        " Page: " + personCriteria.getPage() + " PageSize: " + personCriteria.getPageSize() );

        // TRUE Adam Adams
        count = maintainPersons.searchPersons(personCriteria, dept, results);
        System.out.println("Result count is: " + count);

        assertNotNull(results);

        System.out.println("testSearchMembers - contents of results : " + TextUtil.toString(results));

        System.out.println();
        System.out.println("end testSearchPersons");
        maintainPersons.remove();

    }

    /** Test of updateGeneralDemographics method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testUpdateGeneralDemographics() {
        System.out.println("testUpdateGeneralDemographics");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of updatePersonAddress method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testUpdatePersonAddress() {
        System.out.println("testUpdatePersonAddress");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of updatePersonDetail method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testUpdatePersonDetail() {
        System.out.println("testUpdatePersonDetail");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of updatePersonEmail method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testUpdatePersonEmail() throws Exception{
        System.out.println();
        System.out.println("testUpdatePersonEmail");
        Integer personPk = new Integer(10000002);
        Integer userPk = new Integer(10000001);
        Collection list = new ArrayList();
        EmailData data = new EmailData();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
/*        data.setEmailBadFg(new Boolean(false));
        data.setEmailMarkedBadDt(null);
        data.setEmailType(new Integer(2));
        data.setPersonEmailAddr("bad@address.com");
        list.add(data);
*/
        data.setEmailBadFg(new Boolean(false));
        data.setEmailMarkedBadDt(null);
        data.setEmailPk(new Integer(2));
        data.setEmailType(new Integer(71001));
        data.setPersonEmailAddr("good@address.com");
        list.add(data);
        data = new EmailData();
        data.setEmailPk(new Integer(1488));
        data.setEmailBadFg(new Boolean(true));
        data.setEmailMarkedBadDt(null);
        data.setEmailType(new Integer(71002));
        data.setPersonEmailAddr("bad@address.com");
        list.add(data);

        maintainPersons.updatePersonEmail(userPk, personPk, list);

        System.out.println("Update Emails: " + TextUtil.toString(list));
        System.out.println("end testUpdatePersonEmail");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of updatePersonPhone method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testUpdatePersonPhone() throws Exception{
        System.out.println();
        System.out.println("testUpdatePersonPhone");
        Integer personPk = new Integer(10000002);
        Integer userPk = new Integer(10000001);
        PhoneData data = new PhoneData();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();

        data.setCountryCode("555");
        data.setAreaCode("703");
        data.setPhoneNumber("5555566");
        data.setPhoneBadFlag(new Boolean(false));
        data.setPhoneBadDate(null);
        data.setPhoneExtension(null);
        data.setPhonePrmryFg(new Boolean(true));
        data.setPhonePrivateFg(new Boolean(false));
        data.setPhonePk(new Integer(1));
        data.setDept(new Integer(1));
        data.setPhoneType(new Integer(2));
        data.setPhoneDoNotCallFg(new Boolean(true));

        maintainPersons.updatePersonPhone(userPk, personPk, data);

        System.out.println("Update Phone: " + TextUtil.toString(data));
        System.out.println("end testUpdatePersonPhone");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of updatePoliticalData method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testUpdatePoliticalData() {
        System.out.println("testUpdatePoliticalData");

        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
*/
    /** Test of verifyPerson method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testVerifyPerson() throws Exception {
        System.out.println();
        System.out.println("testVerifyPerson");
        int rtn = 0;
        PersonCriteria personCriteria;

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();

        System.out.println("Person not is system (1): Larry thePenguin");
        personCriteria = new PersonCriteria();
        personCriteria.setFirstNm("Larry");
        personCriteria.setLastNm("thePenguin");
        rtn = maintainPersons.verifyPerson(personCriteria);
        System.out.println("Verify Person: " + rtn);

        System.out.println("Existing Person (2): Adam Adams");
        personCriteria = new PersonCriteria();
        personCriteria.setFirstNm("Adam");
        personCriteria.setLastNm("Adams");
        rtn = maintainPersons.verifyPerson(personCriteria);
        System.out.println("Verify Person: " + rtn);

        System.out.println("Duplicate SSN (3): 773844400");
        personCriteria = new PersonCriteria();
        personCriteria.setSsn("773844400");
        rtn = maintainPersons.verifyPerson(personCriteria);
        System.out.println("Verify Person: " + rtn);

        System.out.println("end testVerifyPerson");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of getCommentHistory method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testGetCommentHistory() throws Exception {
        System.out.println();
        System.out.println("testGetCommentHistory");
        Integer personPk = new Integer(10000002);
        Collection list = new ArrayList();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();

        System.out.println("Should return a result:");
        list = maintainPersons.getCommentHistory(personPk);
        assertNotNull(list);
        System.out.println("Comments: " + personPk);
        System.out.println("Result: " + TextUtil.toString(list));

        System.out.println("No result should be returned:");
        personPk = new Integer(10000003);
        list = maintainPersons.getCommentHistory(personPk);
        assertNull(list);
        System.out.println("Comments: " + personPk);
        System.out.println("Result: " + TextUtil.toString(list));

        System.out.println("end testGetCommentHistory");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }

    /** Test of updatePoliticalData, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testUpdatePoliticalData()
    {
		System.out.println();
        System.out.println("testUpdatePoliticalData");
		try {
			MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
			PoliticalData pd = new PoliticalData();
			pd.setPoliticalObjectorFg(new Boolean(true));
			pd.setPoliticalDoNotCallFg(new Boolean(true));
			maintainPersons.updatePoliticalData(new Integer(10001798), new Integer(10001798), new Integer(4001), pd);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Test of getPoliticalData, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
	public void testGetPoliticalData()
	{
		System.out.println();
        System.out.println("testGetPoliticalData");
		try {
			MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
			PoliticalData pd = maintainPersons.getPoliticalData(new Integer(10001798));
			System.out.println(pd.getPoliticalObjectorFg());
			System.out.println(pd.getPoliticalDoNotCallFg());
			System.out.println(pd.getPacContributorFg());
			System.out.println(pd.getPoliticalRegisteredVoter());
			System.out.println(pd.getPoliticalParty());
			System.out.println(pd.getCongDist());
			System.out.println(pd.getUpperDist());
			System.out.println(pd.getLowerDist());
			System.out.println(pd.getWardNumber());
			System.out.println(pd.getPrecinctNumber());
			System.out.println(pd.getPrecinctName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Test of updateGeneralDemographics, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
	public void testUpdateGeneralDemographics()
	{
		System.out.println();
        System.out.println("testUpdateGeneralDemographics");
		try {
			MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
			DemographicData demographicData = new DemographicData();
			demographicData.setDob(new Timestamp(System.currentTimeMillis()));
			demographicData.setDeceasedDt(new Timestamp(System.currentTimeMillis()));
			demographicData.setDeceasedFg(new Boolean(true));
			demographicData.setGenderCodePK(new Integer(33001));
			demographicData.setEthnicOriginCodePK(new Integer(46001));
			demographicData.setCitizenshipCodePK(new Integer(19001));
			demographicData.setMaritalStatusCodePK(new Integer(49001));
			demographicData.setPrimaryLanguageCodePK(new Integer(48003));
			demographicData.setReligionCodePK(new Integer(62001));
			ArrayList disabilityList = new ArrayList();
			disabilityList.add(new Integer(40001));
			disabilityList.add(new Integer(40002));
			demographicData.setDisabilityCodePKs(disabilityList);
			ArrayList disabilityAccommodationList = new ArrayList();
			disabilityAccommodationList.add(new Integer(37001));
			disabilityAccommodationList.add(new Integer(37002));
			demographicData.setDisabilityAccommodationCodePKs(disabilityAccommodationList);
			ArrayList otherLanguageList = new ArrayList();
			otherLanguageList.add(new Integer(48001));
			otherLanguageList.add(new Integer(48002));
			demographicData.setOtherLanguageCodePKs(otherLanguageList);
			RelationData partnerData = new RelationData();
			partnerData.setRelativeFirstNm("Salim");
			partnerData.setRelativeMiddleNm("Javed");
			partnerData.setRelativeLastNm("Malik");
			partnerData.setRelativeSuffixNm(new Integer(35001));
			demographicData.setThePartnerRelationData(partnerData);
			ArrayList childrenRelationData = new ArrayList();
			String[] childrenFirstNames = {"Waqar", "Wasim"};
			String[] childrenMiddleNames = {"Saleh", "Aaqib"};
			String[] childrenLastNames = {"Younis", "Akram"};
			Integer[] childrenSuffixNames = {new Integer(35002), new Integer(35003)};
			String[] childrenBirthDates = {"12/12/2000", "05/01/1926"};
			Integer[] childrenPks = {new Integer(17), new Integer(20)};
			for(int i = 0; i < childrenFirstNames.length; i++) {
				RelationData rd = new RelationData();
				rd.setRelativeFirstNm(childrenFirstNames[i]);
				rd.setRelativeMiddleNm(childrenMiddleNames[i]);
				rd.setRelativeLastNm(childrenLastNames[i]);
				rd.setRelativeSuffixNm(childrenSuffixNames[i]);
				rd.setRelativeBirthDt(DateUtil.getTimestamp(childrenBirthDates[i]));
				rd.setRelativePk(childrenPks[i]);
				childrenRelationData.add(rd);
			}
			demographicData.setTheChildrenRelationData((RelationData[])childrenRelationData.toArray(new RelationData[childrenRelationData.size()]));
			maintainPersons.updateGeneralDemographics(new Integer(10001798), new Integer(10001798), demographicData);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Test of getGeneralDemographics, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
		public void testGetGeneralDemographics()
		{
			System.out.println();
	        System.out.println("testUpdateGeneralDemographics");
			try {
				MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
				DemographicData demographicData = maintainPersons.getGeneralDemographics(new Integer(10001798));
				System.out.println(demographicData.getDob());
				System.out.println(demographicData.getDeceasedDt());
				System.out.println(demographicData.getGenderCodePK());
				System.out.println(demographicData.getEthnicOriginCodePK());
				System.out.println(demographicData.getCitizenshipCodePK());
				System.out.println((demographicData.getDisabilityCodePKs()).toString());
				System.out.println((demographicData.getDisabilityAccommodationCodePKs()).toString());
				System.out.println(demographicData.getReligionCodePK());
				System.out.println(demographicData.getMaritalStatusCodePK());
				System.out.println((demographicData.getOtherLanguageCodePKs()).toString());
				System.out.println(demographicData.getDeceasedFg());

				RelationData[] relationData = demographicData.getTheChildrenRelationData();
				RelationData rd = demographicData.getThePartnerRelationData();

				if(rd != null) {
					System.out.println(rd.getRelativeFirstNm());
					System.out.println(rd.getRelativeMiddleNm());
					System.out.println(rd.getRelativeLastNm());
					System.out.println(rd.getRelativeSuffixNm());
					System.out.println(rd.getRelativePk());
				}
				for(int i = 0;i < relationData.length; i++) {
					System.out.println(relationData[i].getRelativeFirstNm());
					System.out.println(relationData[i].getRelativeMiddleNm());
					System.out.println(relationData[i].getRelativeLastNm());
					System.out.println(relationData[i].getRelativeSuffixNm());
					System.out.println(relationData[i].getRelativeBirthDt());
					System.out.println(relationData[i].getRelativePk());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
	}

	/** Test of addPersonRelation, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testAddPersonRelation()
    {
		System.out.println();
        System.out.println("testAddPersonRelation");
		try {
			MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
			RelationData rd = new RelationData();
			rd.setRelativeFirstNm("Shoaib");
			rd.setRelativeMiddleNm("Kashif");
			rd.setRelativeLastNm("Akhtar");
			rd.setPersonRelativeType(new Integer(80001));
			rd.setRelativeSuffixNm(new Integer(35001));
			rd.setRelativeBirthDt(new Timestamp(System.currentTimeMillis()));
			maintainPersons.addPersonRelation(new Integer(10001798), new Integer(10001798), rd);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Test of deletePersonRelation, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
    public void testDeletePersonRelation()
    {
		System.out.println();
        System.out.println("testDeletePersonRelation");
		try {
			MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
			maintainPersons.deletePersonRelation(new Integer(17));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Test of updatePersonEmailBadFlag, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
	public void testUpdatePersonEmailBadFlag()
	{
		try {
			MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
			maintainPersons.updatePersonEmailBadFlag(new Boolean(true), new Integer(3595));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    /** Test of getPersona method, of class org.afscme.enterprise.person.ejb.MaintainPersonsBean. */
/*    public void testGetPersona() throws Exception {
        System.out.println();
        System.out.println("testGetCommentHistory");
        Integer personPk = null;
        Collection list = new ArrayList();

        // Add your test code below by replacing the default call to fail.
	MaintainPersons maintainPersons = JNDIUtil.getMaintainPersonsHome().create();

        System.out.println("No result should be returned:");
        personPk = new Integer(10000002);
        list = maintainPersons.getPersona(personPk);
        assertNull(list);
        System.out.println("Person PK: " + personPk);
        System.out.println("Result: " + TextUtil.toString(list));

        System.out.println("end testGetCommentHistory");
        System.out.println();
        //fail("The test case is empty.");
        maintainPersons.remove();
    }
*/

    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}


}
