	/*
 * MaintainMyInfoTest.java
 * JUnit based test
 *
 * Created on July 23, 2003, 2:17 PM
 */

package org.afscme.enterprise.myinfo.ejb;

import org.afscme.enterprise.member.MemberAffiliateResult;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import junit.framework.*;
import java.lang.Integer;
import java.util.*;
import javax.ejb.*;
import java.util.Iterator;
import javax.naming.NamingException;
// AFSCME Enterprise Imports
import org.afscme.enterprise.myinfo.MyInfoData;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.common.RecordData;

/**
 *
 * @author skhan
 */
public class MaintainMyInfoTest extends TestCase {

    public MaintainMyInfoTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainMyInfoTest.class);
        return suite;
    }

    /** Test of generate method, of class org.afscme.enterprise.reporting.specialized.MaintainMyInfo. */
    public void testUpdateMyInfoData() {
        try {
        	System.out.println("testGenerate");
			MaintainMyInfo maintainMyInfo = JNDIUtil.getMaintainMyInfoHome().create();
        	// Add your test code below by replacing the default call to fail.
        	MyInfoData mlrpt = new MyInfoData();
        	PersonData person  = new PersonData();
        	person.setPersonPk(new Integer(10000001));
        	EmailData ed = new EmailData();
        	ed.setEmailPk(new Integer(1));
        	ed.setPersonEmailAddr("something@smomething.com");
        	ed.setEmailType(new Integer(71001));
        	ed.setEmailBadFg(new Boolean(false));
        	ed.setEmailMarkedBadDt(null);
        	EmailData ed1 = new EmailData();
			ed1.setEmailPk(new Integer(2));
			ed1.setPersonEmailAddr("hui@slsl.com");
			ed1.setEmailType(new Integer(71001));
			ed1.setEmailBadFg(new Boolean(false));
        	ed1.setEmailMarkedBadDt(null);
        	ArrayList emails = new ArrayList();
        	emails.add(ed);
        	emails.add(ed1);
        	person.setTheEmailData(emails);
        	PhoneData pd = new PhoneData();
        	pd.setCountryCode("8001");
			pd.setAreaCode("516");
			pd.setPhoneNumber("7896325");
			pd.setPhoneBadFlag(new Boolean(false));
			pd.setPhoneBadDate(null);
			pd.setPhoneExtension("516");
			pd.setPhonePrmryFg(new Boolean(false));
			pd.setPhonePrivateFg(new Boolean(false));
			pd.setPhonePk(new Integer(2));
			pd.setDept(new Integer(4001));
			pd.setPhoneType(new Integer(3001));
    		pd.setPhoneDoNotCallFg(new Boolean(false));
    		ArrayList phoneList = new ArrayList();
    		phoneList.add(pd);
    		person.setThePhoneData(phoneList);
    		PersonAddressRecord pad = new PersonAddressRecord();
    		RecordData rd = new RecordData();
			rd.setPk(new Integer(3633));
    		pad.setRecordData(rd);
    		pad.setCountryPk(new Integer(9002));
    		pad.setAddr1("the addr1");
    		pad.setAddr2("the addr2");
    		pad.setCity("city");
    		pad.setCounty("county");
    		pad.setZipCode("");
    		pad.setZipPlus("");
    		pad.setProvince("province");
			mlrpt.setPersonData(person);
			mlrpt.setPersonAddressRecord(pad);
			maintainMyInfo.updateMyInfoData(mlrpt);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** Test of getMailingListId method, of class org.afscme.enterprise.reporting.specialized.MaintainMyInfo. */
    public void testGetMyInfoData() {
		try {
			System.out.println("testGetMyInfoData");

			MaintainMyInfo maintainMyInfo = JNDIUtil.getMaintainMyInfoHome().create();
			// Add your test code below by replacing the default call to fail.
			MyInfoData md = maintainMyInfo.getMyInfoData(new Integer(10000008));
			PersonData person  = md.getPersonData();
			Collection c = person.getTheEmailData();
			Iterator i = c.iterator();
			while(i.hasNext()) {
				EmailData ed = (EmailData)i.next();;
				System.out.println(ed.getEmailPk());
				System.out.println(ed.getPersonEmailAddr());
				System.out.println(ed.getEmailType());
				System.out.println(ed.getEmailBadFg());
				System.out.println(ed.getEmailMarkedBadDt());
			}
			c = person.getThePhoneData();
			if(c != null ) {
				i = c.iterator();
				while(i.hasNext()) {
					PhoneData pd = (PhoneData)i.next();;
					System.out.println(pd.getPhonePk());
					System.out.println(pd.getPhoneBadFlag());
					System.out.println(pd.getPhoneType());
					System.out.println(pd.getCountryCode());
					System.out.println(pd.getAreaCode());
					System.out.println(pd.getPhoneNumber());
				}
			}
			c = md.getMemberAffiliateResults();
			i = c.iterator();
			while(i.hasNext()) {
				MemberAffiliateResult mar = (MemberAffiliateResult)i.next();
				AffiliateIdentifier ai = mar.getTheAffiliateIdentifier();
				System.out.println(mar.getAbbreviatedName());
				System.out.println(ai.getLocal());
				System.out.println(ai.getState());
				System.out.println(ai.getSubUnit());
				System.out.println(ai.getCouncil());
			}
			System.out.println(person.getPrefixNm());
			System.out.println(person.getFirstNm());
			System.out.println(person.getMiddleNm());
			System.out.println(person.getLastNm());
			System.out.println(person.getSuffixNm());
			PersonAddressRecord par = md.getPersonAddressRecord();
			System.out.println(par.getAddr1());
			System.out.println(par.getAddr2());
			System.out.println(par.getCity());
			System.out.println(par.getState());
			System.out.println(par.getZipCode());
			System.out.println(par.getZipPlus());
			System.out.println(par.getCounty());
			System.out.println(par.getProvince());
			System.out.println(par.getCountryPk());
			System.out.println(par.getRecordData().getPk());
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
