package org.afscme.enterprise.controller.ejb;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.ConfigUtil;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.sql.SQLException;
import org.afscme.enterprise.users.UserData;
import java.rmi.RemoteException;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.ChallengeQuestionData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import javax.naming.NamingException;

public class AccessControlTest extends TestCase {

	private UserData m_testUser;
	
	public AccessControlTest(java.lang.String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(AccessControlTest.class);
		
		return suite;
	}
	
	public void tearDown() throws Exception {
        System.out.println("Tear Down: Deleting test user");
        DBUtil.execute("DELETE FROM [User_Affiliates] WHERE person_pk=10000101");
        DBUtil.execute("DELETE FROM [User_Roles] WHERE person_pk=10000101");
        DBUtil.execute("DELETE FROM [Users] WHERE person_pk=10000101");
	}
	
	public void testCreate() throws NamingException, CreateException, RemoteException, RemoveException {
		
		System.out.println("Creating AccessControl EJB");
		AccessControl accessControl = JNDIUtil.getAccessControlHome().create();
		assertNotNull(accessControl);
		accessControl.remove();
	}
	
	public void testBadLogin() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Attempting login with bad userId");
		AccessControl accessControl = JNDIUtil.getAccessControlHome().create();
		UserSecurityData data = new UserSecurityData();
		int result = accessControl.login("baduserId", "badpassword", data);
		assertEquals(AccessControlStatus.LOGIN_RESULT_FAILED, result);
		accessControl.remove();
	}

	public void testGoodLogin() throws NamingException, CreateException, RemoteException, RemoveException, SQLException {
		System.out.println("Adding a user to login as");
		MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
		m_testUser = maintainUsers.addUser(new Integer(10000101));
		m_testUser.setUserId("chicken");
		maintainUsers.updateUser(m_testUser);
		maintainUsers.remove();
		AccessControl accessControl = JNDIUtil.getAccessControlHome().create();
		accessControl.changePassword(m_testUser.getPersonPk(), "noodle");

		System.out.println("Logging in as that user");
		UserSecurityData data = new UserSecurityData();
		int result = accessControl.login("chicken", "noodle", data);
		assertEquals(AccessControlStatus.LOGIN_RESULT_OK, result);
		accessControl.remove();
	}
	
	public void testLockoutAndUnlock() throws NamingException, CreateException, RemoteException, RemoveException, SQLException {
		System.out.println("Adding a user to get locked out");
		MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
		m_testUser = maintainUsers.addUser(new Integer(10000101));
		m_testUser.setUserId("lockheed");
		maintainUsers.updateUser(m_testUser);
		maintainUsers.remove();

		AccessControl accessControl = JNDIUtil.getAccessControlHome().create();
		accessControl.changePassword(m_testUser.getPersonPk(), "martin");

		int maxAttempts = ConfigUtil.getConfigurationData().getMaxLoginAttempts();
		System.out.println("Logging in as that user with the wrong password " + maxAttempts + " times");
		UserSecurityData data = new UserSecurityData();
		for (int i = 0; i < maxAttempts; i++) {
			int result = accessControl.login("lockheed", "douglas", data);
			assertEquals(AccessControlStatus.LOGIN_RESULT_FAILED, result);
		}

		System.out.println("Logging in again, with the correct password");
		int result = accessControl.login("lockheed", "martin", data);
		assertEquals(AccessControlStatus.LOGIN_RESULT_LOCKED_OUT, result);

		System.out.println("Logging in one more time, with the correct password");
		result = accessControl.login("lockheed", "martin", data);
		assertEquals(AccessControlStatus.LOGIN_RESULT_LOCKED_OUT, result);
		accessControl.remove();

		System.out.println("Unlocking the account");
		accessControl.resetLockout(m_testUser);
		assertEquals(AccessControlStatus.LOGIN_RESULT_LOCKED_OUT, result);

		System.out.println("Logging in with the correct password");
		result = accessControl.login("lockheed", "martin", data);
		assertEquals(AccessControlStatus.LOGIN_RESULT_OK, result);
		accessControl.remove();
	}
	
	public void testGetChallengeQuestion() throws NamingException, CreateException, RemoteException, RemoveException, SQLException {
		System.out.println("Adding a user to get password question for");
		MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
		m_testUser = maintainUsers.addUser(new Integer(10000101));

		System.out.println("Setting password question for test user");
		m_testUser.setChallengeQuestion(new Integer(2));
		maintainUsers.updateUser(m_testUser);
		maintainUsers.remove();
		
		System.out.println("Getting password question for test user");
		AccessControl accessControl = JNDIUtil.getAccessControlHome().create();
		ChallengeQuestionData data = accessControl.getChallengeQuestionData(m_testUser.getUserId());
		accessControl.remove();

		assertEquals(new Integer(2), data.getQuestion());
		assertEquals(m_testUser.getPersonPk(), data.getPersonPk());
	}

	public void testResetPassword() throws NamingException, CreateException, RemoteException, RemoveException, SQLException {
		System.out.println("Adding a user to reset password for");
		MaintainUsers maintainUsers = JNDIUtil.getMaintainUsersHome().create();
		m_testUser = maintainUsers.addUser(new Integer(10000101));

		System.out.println("Setting challenge response for user");
		m_testUser.setChallengeResponse("Energy Star Compliant");
		maintainUsers.updateUser(m_testUser);
		maintainUsers.remove();
		
		System.out.println("Resetting password");
		AccessControl accessControl = JNDIUtil.getAccessControlHome().create();
		assertEquals(AccessControlStatus.REQUEST_PASSWORD_OK, accessControl.requestPassword(m_testUser.getPersonPk(), "Energy Star Compliant"));
		accessControl.remove();

	}
	
	
}
