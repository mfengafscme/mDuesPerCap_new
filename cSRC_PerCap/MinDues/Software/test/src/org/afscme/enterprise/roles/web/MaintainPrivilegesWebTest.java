package org.afscme.enterprise.roles.web;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Random;
import org.afscme.enterprise.test.AFSCMEWebTest;
import java.io.IOException;

/**
 * Contains functional tests for the MaintainPrivileges portion of the UI
 */
public class MaintainPrivilegesWebTest extends AFSCMEWebTest
{
    public MaintainPrivilegesWebTest(String name) throws IOException {
        super(name);
    }
    public static Test suite() {
        return new TestSuite(MaintainPrivilegesWebTest.class);
    }

	/**
	 * Tests adding, and deleting a role.
	 */
	public void testAddDeleteRole() throws Exception {
		
		String roleName = "Test Role " + new Random().nextInt();
		
		logln("\n==== Add / Delete Role ====\n");

		//login
		loginAFSCME();

		//got to the 'list roles' page.
		selectLink("Maintain Privileges");

		//go to the add role page
		selectLink("Add Role");

		//fill in the role parameters
		setParameter("name", roleName);
		setParameter("description", "Test Description");

		//go select some privileges
		submit("selectPrivileges");
		checkTitle("Select Privileges");
		setParameter("selected", new String[] { "MaintainCodes", "MaintainUsers", "MaintainPrivileges" });
		submit();
		checkTitle("Edit Role");

		//go select some reports
		submit("selectReportPrivileges");
		checkTitle("Select Report Privileges");
		setParameter("selected", new String[] { "1" });
		submit();

		//go select some fields
		submit("selectFieldPrivileges");
		checkTitle("Select Field Privileges");
		setParameter("selected", new String[] { "1", "2", "3" });
		submit();

		//click submit on the edit role page
		checkTitle("Edit Role");
		submit();

		//delete the role
		assertEquals("Roles", getResponse().getTitle());
		selectRowLink("Delete", roleName);

		selectLink("LOGOUT");
	}	
	
	/** Tests sorting the role listing columns */
	public void testSortRoles() throws Exception {
		
		logln("\n==== Sort Roles ====\n");

		loginAFSCME();
		
		//got to the 'list roles' page.
		selectLink("Maintain Privileges");

		//test sorting the columns
		selectLink("Name");
		selectLink("Name");
		selectLink("Description");
		selectLink("Description");
		assertEquals("Roles", getResponse().getTitle());

		selectLink("LOGOUT");
	}

  	 /**
	 * Tests updating a role.
	 */
	public void testUpdateRole() throws Exception {

		logln("\n==== Update Role ====\n");

		//login
		loginAFSCME();

		//go to the 'list roles' page.
		selectLink("Maintain Privileges");
		
		//change the name of Super User
		selectRowLink("Edit", "Super User");
		setParameter("name", "Super User Updated");
		submit();
		
		//change it back
		selectRowLink("Edit", "Super User Updated");
		setParameter("name", "Super User");
		submit();
		
		selectLink("LOGOUT");
	}
}
