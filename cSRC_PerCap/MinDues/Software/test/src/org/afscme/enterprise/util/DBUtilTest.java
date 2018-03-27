/*
 * DBUtilTest.java
 * JUnit based test
 *
 * Created on July 15, 2002, 1:40 PM
 */

package org.afscme.enterprise.util;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import java.sql.Connection;

public class DBUtilTest extends TestCase {
    
    public DBUtilTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DBUtilTest.class);
        
        return suite;
    }
    
    /** Test of getConnection method, of class org.afscme.enterprise.util.DBUtil. */
    public void testGetConnection() {
		System.out.println("Getting Database Connection");
        Connection con = DBUtil.getConnection();
        if (con == null)
            fail("Didn't get connection");
        
		System.out.println("Releasing Database Connection");
        DBUtil.cleanup(con, null, null);
    }
    
}
