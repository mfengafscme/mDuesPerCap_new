package org.afscme.enterprise.users.web;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Random;
import org.afscme.enterprise.test.AFSCMEWebTest;
import java.io.IOException;

/**
 * Contains functional tests for the MaintainPrivileges portion of the UI
 */
public class MaintainUsersWebTest extends AFSCMEWebTest
{
    public MaintainUsersWebTest(String name) throws IOException {
        super(name);
    }
    public static Test suite() {
        return new TestSuite(MaintainUsersWebTest.class);
    }

    
    private void gotoUser(String firstName, String lastName) throws Exception {
		selectLink("Main Menu");
		selectLink("Person");
        setParameter("firstNm", firstName);
        setParameter("lastNm", lastName);
        submit();
        selectLink("User");
    }
        
	/**
	 * Tests editing a user.
	 */
	public void testEditUser() throws Exception {
		
		logln("\n==== Edit User ====\n");

		//login
		loginAFSCME();

        gotoUser("Ying", "Qi");

		//change some parameters
		setParameter("userId", "ying2");
		setParameter("challengeQuestion", "1002");
		setParameter("challengeResponse", "response_changed");
		setParameter("password", "password_changed");
		setParameter("password2", "password_changed");
		setParameter("remarks", "remarks_changed");
		setParameter("department", "4002");
		setParameter("startPage", "A");
		submit();
        
        gotoUser("Ying", "Qi");
		
		//verify the edits
		assertEquals("ying2", getParameterValue("userId"));
		assertEquals("1002", getParameterValue("challengeQuestion"));
		assertEquals("4002", getParameterValue("department"));
		assertEquals("response_changed", getParameterValue("challengeResponse"));
		assertEquals("remarks_changed", getParameterValue("remarks"));
		assertEquals("A", getParameterValue("startPage"));
		
		//change some values back
		setParameter("userId", "kvogel");
		setParameter("password", "password");
		setParameter("password2", "password");
		setParameter("challengeResponse", "response");
		setParameter("remarks", "remakrs");
        setParameter("department", "4001");
        setParameter("challengeQuestion", "1002");
		submit();

        gotoUser("Ying", "Qi");
        
        //go to the roles page
		submit("selectRoles");
		checkTitle("Select Roles");

        //choose some roles
        setParameter("selected", new String[] { "1", "2", "3" });
		submit();
        checkTitle("Edit User");

        //go to the choose affiliates page
        submit("selectAffiliates");
        checkTitle("Select User Affiliates Search");
        
        //search
        submit();
        checkTitle("Select User Affiliates");

        //choose some affiliates
        setParameter("selection", new String[] { "3", "5", "7" });
        submit();

        //logout
        selectLink("LOGOUT");
    }	

	/*
	 */
}
