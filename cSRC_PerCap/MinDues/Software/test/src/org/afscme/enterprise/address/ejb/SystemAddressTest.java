package org.afscme.enterprise.address.ejb;

import java.util.Iterator;
import java.util.Map;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import org.afscme.enterprise.address.ejb.*;
import org.afscme.enterprise.address.*;
import org.afscme.enterprise.codes.Codes.Country;
import org.afscme.enterprise.codes.Codes.PersonAddressType;
import org.afscme.enterprise.util.CryptoUtil;
import org.afscme.enterprise.util.TextUtil;


public class SystemAddressTest extends TestCase {

	public SystemAddressTest(java.lang.String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(SystemAddressTest.class);
		return suite;
	}
	
	public void testCreate() throws Exception {
		System.out.println("Creating SystemAddressTest bean");
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();
		if (systemAddress == null)
			fail("SystemAddress was null");
		systemAddress.remove();
	}

	public void testValidate() throws Exception {
        
        Address addr;
        Set errors;
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();

        //test a valid address
        addr = new Address();
        addr.setAddr1("123 Maple");
        addr.setAddr2("Apt 301");
        addr.setZipCode("22182");
        System.out.println("Testing valid address " + addr);
        errors = systemAddress.validate(addr);
        assertNull(errors);
        assertNotNull(addr.getState());
        assertNotNull(addr.getCity());
        assertNotNull(addr.getCountryPk());

        //test city empty
        addr = new Address();
        addr.setAddr1("123 Maple");
        addr.setCountryPk(Country.CA);
        addr.setProvince("Alberta");
        System.out.println("Testing missing city " + addr);
        errors = systemAddress.validate(addr);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(Address.ERROR_CITY_EMPTY, errors.iterator().next());

        //test province empty
        addr = new Address();
        addr.setAddr1("123 Maple");
        addr.setCountryPk(Country.CA);
        addr.setCity("Toronto");
        System.out.println("Testing missing province" + addr);
        errors = systemAddress.validate(addr);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(Address.ERROR_PROVINCE_EMPTY, errors.iterator().next());
        
        //test a state that doesn't match the zip code
        addr = new Address();
        addr.setAddr1("123 Maple");
        addr.setAddr2("Apt 301");
        addr.setState("DC");//virginia
        addr.setZipCode("22182");
        System.out.println("Testing state/zip mismatch " + addr);
        errors = systemAddress.validate(addr);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(Address.ERROR_STATE_ZIP_MISMATCH, errors.iterator().next());
        
        //test a zip that doesn't exist
        addr = new Address();
        addr.setAddr1("123 Maple");
        addr.setAddr2("Apt 301");
        addr.setCity("Anchorage");
        addr.setState("VA");//alaska
        addr.setZipCode("99999");
        System.out.println("Testing non-matching zip " + addr);
        errors = systemAddress.validate(addr);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertTrue(errors.contains(Address.ERROR_ZIPCODE_INVALID));
        
        systemAddress.remove();
    }
    
    public void testInsertByOwner() throws Exception {
        
        Address addr;
        Set errors;
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();

        //test a valid address
        addr = new Address();
        addr.setAddr1(String.valueOf(Math.abs(CryptoUtil.randomInt()) + " Pine"));
        addr.setAddr2("Apt 301");
        addr.setZipCode("22182");
        
        Integer ownerPk = new Integer(10000001);
        Integer creatorPk = ownerPk;
        errors = systemAddress.updateByOwner(ownerPk,  null, addr);
        assertNull(errors);
    }

    public void testAddByDepartment() throws Exception {
        
        PersonAddress addr;
        Set errors;
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();

        //test a valid address
        addr = new PersonAddress();
        addr.setType(PersonAddressType.HOME);
        addr.setPrivate(false);
        addr.setPrimary(true);
        addr.setAddr1(String.valueOf(Math.abs(CryptoUtil.randomInt()) + " K Street"));
        addr.setAddr2("Suite 100");
        addr.setZipCode("22182");
        
        Integer ownerPk = new Integer(10000001);
        Integer creatorPk = ownerPk;
        Integer dept = new Integer(4001); // Headquarters
        errors = systemAddress.addByDepartment(creatorPk,  dept, ownerPk, addr);
        System.out.println("addDepartment errors: " + TextUtil.toString(errors));
        assertNull(errors);
    }

    public void testAddByAffiliate() throws Exception {
        
        PersonAddress addr;
        Set errors;
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();

        //test a valid address
        addr = new PersonAddress();
        addr.setType(PersonAddressType.HOME);
        addr.setPrivate(false);
        addr.setPrimary(true);
        addr.setAddr1(String.valueOf(Math.abs(CryptoUtil.randomInt()) + " Apple Way"));
        addr.setZipCode("84105");
        
        Integer ownerPk = new Integer(10000001);
        Integer creatorPk = ownerPk;
        Integer affPk = new Integer(1);
        errors = systemAddress.addByAffiliate(creatorPk,  affPk, ownerPk, addr);
        System.out.println("addByAffiliate errors: " + TextUtil.toString(errors));
        assertNull(errors);
        
        systemAddress.remove();
    }

    public void testGetAddresses() throws Exception {
        
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();
        Integer ownerPk = new Integer(10000001);
        List addrs = systemAddress.getPersonAddresses(ownerPk);
        assertNotNull(addrs);
        
        System.out.println("Addresses: " + TextUtil.toString(addrs));
        
        systemAddress.remove();
    }

    public void testGetAddressesByDepartment() throws Exception {
        
		SystemAddress systemAddress = JNDIUtil.getSystemAddressHome().create();
        Integer ownerPk = new Integer(10000001);
        Integer departmentPk = new Integer(40001);
        List addrs = systemAddress.getPersonAddressesForDepartment(ownerPk, departmentPk);
        assertNotNull(addrs);
        
        System.out.println("Addresses: " + TextUtil.toString(addrs));
        
        systemAddress.remove();
    }

}
