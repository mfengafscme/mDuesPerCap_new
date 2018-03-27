/*
 * MaintainUsersTest.java
 * JUnit based test
 *
 * Created on July 15, 2002, 4:22 PM
 */

package org.afscme.enterprise.users.ejb;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.UserData;
import org.afscme.enterprise.users.AffiliateSortData;
import java.rmi.RemoteException;
import java.util.Iterator;
import junit.textui.TestRunner;
import javax.naming.NamingException;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;


public class MaintainUsersTest extends TestCase {
    
    public MaintainUsersTest(java.lang.String testName) {
        super(testName);
    }

	public static Test suite() {
        TestSuite suite = new TestSuite(MaintainUsersTest.class);
		return suite;
    }

	public void tearDown() throws Exception {
        DBUtil.execute("DELETE FROM [User_Affiliates] WHERE person_pk IN (10000101, 10000102)");
        DBUtil.execute("DELETE FROM [User_Roles] WHERE person_pk IN (10000101, 10000102)");
        DBUtil.execute("DELETE FROM [Users] WHERE person_pk IN (10000101, 10000102)");
	}
	
	public void testCreate() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Creating MaintainUsers EJB");
        MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
		maintainUsers.remove();
	}
    
    public void testAddUpdateUser() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Adding User");
        MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        UserData user = maintainUsers.addUser(new Integer(10000101));
        if (user == null)
            fail("addUser() returned null");

		System.out.println("Updating User");
        user.setRemarks("These are the remarks");
        assertEquals(0, maintainUsers.updateUser(user));
        maintainUsers.remove();
    }

    public void testAddNonMemberUser() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Adding Non-Member User");
        MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        UserData user = maintainUsers.addUser(new Integer(10000102));
        if (user == null)
            fail("addUser() returned null");
        maintainUsers.remove();
    }
	
	
    public void testSetGetRoles() throws NamingException, CreateException, RemoteException, RemoveException {

        System.out.println("Adding User");
        MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        maintainUsers.addUser(new Integer(10000101));

        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
        maintainPrivileges.getRoles();
        Iterator it = maintainPrivileges.getRoles().keySet().iterator();
        Set roles = new HashSet();
        roles.add(it.next());
        roles.add(it.next());
		System.out.println("Setting Roles for a user");
        maintainUsers.setRoles(new Integer(10000101), roles);

		System.out.println("Getting Roles for a user");
        Set resultRoles = maintainUsers.getRoles(new Integer(10000101));
        if (resultRoles == null)
            fail("Result roles was null");
        if (!resultRoles.equals(roles))
            fail("Result roles was wrong");
        maintainUsers.remove();
    }
        
    public void testAddRemoveAffiliates() throws NamingException, CreateException, RemoteException, RemoveException {
        MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        maintainUsers.addUser(new Integer(10000101));

        Set affiliates = new HashSet();
        affiliates.add(new Integer(1));
        affiliates.add(new Integer(2));
		System.out.println("Adding affiliates to a user");
        maintainUsers.addAffiliates(new Integer(10000101), affiliates);
		System.out.println("Removing affiliates from a user");
        maintainUsers.removeAffiliates(new Integer(10000101), affiliates);
        maintainUsers.remove();
    }

	public void testGetSetAffiliates() throws NamingException, CreateException, RemoteException, RemoveException {
        MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        maintainUsers.addUser(new Integer(10000101));
		AffiliateData data = new AffiliateData();
		AffiliateSortData sortData = new AffiliateSortData();
		List result = new LinkedList();

		try {
			System.out.println("Getting affiliates for user");
			maintainUsers.getAffiliates(new Integer(10000101), data, sortData, result);

			System.out.println("Getting the test affilaite for the user");
			data.setLocal("102"); //this is the unit code in the test affiliate
			data.setSelected(new Boolean(true));
			sortData.setSortField(AffiliateSortData.FIELD_LOCAL);
			sortData.setSortOrder(AffiliateSortData.SORT_DESCENDING);
            sortData.setPageSize(25);
			result.clear();
			int count = maintainUsers.getAffiliates(new Integer(10000101), data, sortData, result);
			assertEquals(0, count);
            assertEquals(result.size(), count);

			System.out.println("Associating the test affiliate with the user");
			//now associate the user with the affiliate
			maintainUsers.setAffiliates(new Integer(10000101), data, true);

			System.out.println("Getting the test affilaite for the user");
			result.clear();
			count = maintainUsers.getAffiliates(new Integer(10000101), data, sortData, result);
			assertEquals(1, count);
            assertEquals(result.size(), count);

			System.out.println("Disassociating the test affiliate with the user");
			maintainUsers.setAffiliates(new Integer(10000101), data, false);

			System.out.println("Getting the test affilaite for the user");
			result.clear();
			count = maintainUsers.getAffiliates(new Integer(10000101), data, sortData, result);
			assertEquals(0, count);
            assertEquals(result.size(), count);
		} finally {
			maintainUsers.remove();
		}
	}
}
