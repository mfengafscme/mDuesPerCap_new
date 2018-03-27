package org.afscme.enterprise.roles.ejb;

import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.roles.RoleData;
import javax.ejb.RemoveException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import junit.textui.TestRunner;
import javax.naming.NamingException;



/**
 * Tests the MaintainPrivileges Session bean.
 */
public class MaintainPrivilegesTest extends TestCase {
    
    public MaintainPrivilegesTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainPrivilegesTest.class);
        
        return suite;
    }
    
    /** Test creation of the MaintainPrivileges Session Bean */
    public void testCreate()  throws NamingException, CreateException, FinderException, RemoveException, RemoteException {
		System.out.println("Creating MaintainPrivileges EJB");
		MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
        maintainPrivileges.remove();
    }

        /** Test of getReports method, of class org.afscme.enterprise.roles.ejb.MaintainPrivilegesBean. */
    public void testGetReports()  throws NamingException, CreateException, FinderException, RemoveException, RemoteException {
		System.out.println("Getting All Report Privileges");
        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
        Map reports = maintainPrivileges.getReports();
        maintainPrivileges.remove();

        if (reports == null || reports.size() == 0)
           fail("Map of reports was null or empty.");
    }
    
    public void testGetSetRoleFields()  throws NamingException, CreateException, FinderException, RemoveException, RemoteException {
		Integer rolePK = new Integer(1);
        Set fields = new HashSet();
        fields.add(new Integer(1));
        fields.add(new Integer(2));
        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();

		System.out.println("Setting the field privileges for a role");
		maintainPrivileges.setFields(rolePK, fields);

		System.out.println("Getting the field privileges for a role");
        Set fieldsResult = maintainPrivileges.getFields(rolePK);
        maintainPrivileges.remove();
        
        if (fieldsResult == null || fieldsResult.size() == 0)
            fail("Fields was null or empty");
        
        if (!fieldsResult.equals(fields))
            fail("Fields returned was wrong");
    }

    public void testGetSetRolePrivileges()  throws NamingException, CreateException, FinderException, RemoveException, RemoteException {

        Integer rolePK = new Integer(2);
        Set privs = new HashSet();
        privs.add("MaintainUsers");
        privs.add("MaintainPrivileges");
        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();

		System.out.println("Setting the privileges for a role");
        maintainPrivileges.setPrivileges(rolePK, privs);

		System.out.println("Setting the privileges for a role");
        Set privsResult = maintainPrivileges.getPrivileges(rolePK);
        maintainPrivileges.remove();
        
        if (privsResult == null || privs.size() == 0)
            fail("Privileges was null or empty");
        
        if (!privsResult.equals(privs))
            fail("Privileges returned was wrong");
    }
    
    
    /** Test of getPrivileges method, of class org.afscme.enterprise.roles.ejb.MaintainPrivilegesBean. */
    public void testGetPrivileges() throws NamingException, CreateException, FinderException, RemoveException, RemoteException {

		System.out.println("Getting all privileges");

		MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
        List privileges = maintainPrivileges.getPrivilegesList();
        maintainPrivileges.remove();

        if (privileges == null || privileges.size() == 0)
           fail("Map of privileges was null or empty.");
    }
    
    /** Test of addRole, getRole, updateRole and deleteRole methods, of class org.afscme.enterprise.roles.ejb.MaintainPrivilegesBean. */
    public void testSetGetRoleReports() throws NamingException, CreateException, FinderException, RemoveException, RemoteException {
        Integer reportPK = new Integer(1);
        Integer rolePK = new Integer(1);
        Set reports = new HashSet();
        reports.add(reportPK);
        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();

		System.out.println("Setting the reports for a role");
        maintainPrivileges.setReports(rolePK, reports);

		System.out.println("Getting the reports for a role");
        reports = maintainPrivileges.getReports(rolePK);
        maintainPrivileges.remove();
        
        if (reports == null || reports.size() == 0)
            fail("Reports was null or empty");
    }
    
    /** Test of addRole, getRole, updateRole and deleteRole methods, of class org.afscme.enterprise.roles.ejb.MaintainPrivilegesBean. */
    public void testAddGetUpdateDeleteRole() throws NamingException, CreateException, FinderException, RemoveException, RemoteException {
        
        RoleData roleData = new RoleData();
        roleData.setName("testName");
        roleData.setDescription("testDescription");

        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();

        try {

			System.out.println("Adding a role to the system");
            roleData = maintainPrivileges.addRole(roleData);
            if (roleData == null)
                fail("Adding role failed, return value was null");
            Integer pk = roleData.getPk();
            if (pk == null) 
                fail("Adding role failed, role object has no primary key");

			System.out.println("Getting the role just added");
            roleData = maintainPrivileges.getRole(pk);
            if (roleData == null) 
                fail("Get role failed");

            roleData.setName("newName");
            roleData.setDescription("newDescription");
			System.out.println("Updating the role");
            maintainPrivileges.updateRole(roleData);
			System.out.println("Getting the updated role");
            roleData = maintainPrivileges.getRole(roleData.getPk());
            if (!roleData.getName().equals("newName")) 
                fail("Role name didn't update");
            if (!roleData.getDescription().equals("newDescription")) 
                fail("Role description didn't update");

			System.out.println("Deleting the role");
            maintainPrivileges.deleteRole(pk);
			System.out.println("Deleting the role, making sure it's deleted");
            Map roles = maintainPrivileges.getRoles();
            if (roles.get(pk) != null)
                fail("Failed to delete role");
        }
        finally {
                maintainPrivileges.remove();
        }
        
    }

    /** Test of addRole, getRole, updateRole and deleteRole methods, of class org.afscme.enterprise.roles.ejb.MaintainPrivilegesBean. */
    public void testGetFields() throws NamingException, CreateException, RemoveException, RemoteException {
        
        MaintainPrivileges maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
		System.out.println("Getting all the field privileges in the system");
        Map fields = maintainPrivileges.getFields();
        maintainPrivileges.remove();
        if (fields == null || fields.size() == 0)
            fail("Didn't get fields");
    }
}
