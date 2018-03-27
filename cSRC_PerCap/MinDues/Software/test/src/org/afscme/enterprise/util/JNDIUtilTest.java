/*j
 * JNDIUtilTest.java
 * JUnit based test
 *
 * Created on July 15, 2002, 11:04 AM
 */

package org.afscme.enterprise.util;

import org.afscme.enterprise.codes.ejb.MaintainCodesHome;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;

import org.afscme.enterprise.roles.ejb.MaintainPrivilegesHome;
import org.afscme.enterprise.controller.ejb.AccessControlHome;
import org.afscme.enterprise.users.ejb.MaintainUsersHome;
import org.afscme.enterprise.common.ConfigurationData;
import org.afscme.enterprise.reporting.base.*;

import javax.naming.Context;
import javax.naming.NamingException;

import org.afscme.enterprise.controller.ActionPrivileges;

/**
 *
 * @author kvogel
 */
public class JNDIUtilTest extends TestCase {
    
    public JNDIUtilTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(JNDIUtilTest.class);
        
        return suite;
    }
    
	public void testPrintAll() throws NamingException {
        System.out.println("====BASE CONTEXT====");
		JNDIUtil.printAll(System.out);
        System.out.println("====java: CONTEXT====");
		JNDIUtil.printAll("java:", "", System.out);
	}
	
    /** Test of getInitialContext method, of class org.afscme.enterprise.util.JNDIUtil. */
    public void testGetInitialContext() throws NamingException {

		System.out.println("Getting initial context");
        Context ctx = JNDIUtil.getInitialContext();

        if (ctx == null)
            fail("Didn't get the initial context");
    }
    
    /** Test of getMaintainCodesHome method, of class org.afscme.enterprise.util.JNDIUtil. */
    public void testGetMaintainCodesHome() throws NamingException{
        
		System.out.println("Getting MaintainCodesHome");
        MaintainCodesHome home = JNDIUtil.getMaintainCodesHome();
        
        if (home == null) 
            fail("Didn't get home interface");
    }
    
    /** Test of getMaintainPrivilegesHome method, of class org.afscme.enterprise.util.JNDIUtil. */
    public void testGetMaintainPrivilegesHome() throws NamingException {
		System.out.println("Getting MaintainPrivilegesHome");
        MaintainPrivilegesHome home = JNDIUtil.getMaintainPrivilegesHome();
        
        if (home == null) 
            fail("Didn't get home interface");
    }
    
    /** Test of getMaintainUsersHome method, of class org.afscme.enterprise.util.JNDIUtil. */
    public void testGetMaintainUsersHome() throws NamingException {
		System.out.println("Getting MaintainUsersHome");
        MaintainUsersHome home = JNDIUtil.getMaintainUsersHome();
        
        if (home == null) 
            fail("Didn't get home interface");
    }

    /** Test of getMaintainUsersHome method, of class org.afscme.enterprise.util.JNDIUtil. */
    public void testGetAccessControlHome()  throws NamingException{
		System.out.println("Getting MaintainAccessControlHome");
        AccessControlHome home = JNDIUtil.getAccessControlHome();
        
        if (home == null) 
            fail("Didn't get home interface");
    }

}
