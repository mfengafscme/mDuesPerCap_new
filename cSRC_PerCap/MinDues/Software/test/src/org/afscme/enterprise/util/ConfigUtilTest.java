/*
 * ConfigUtilTest.java
 * JUnit based test
 *
 * Created on August 28, 2002, 3:34 PM
 */

package org.afscme.enterprise.util;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import org.afscme.enterprise.reporting.base.PDFConfigurationData;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.common.ConfigurationData;
import java.util.Map;

public class ConfigUtilTest extends TestCase {
	
	public ConfigUtilTest(java.lang.String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(ConfigUtilTest.class);
		
		return suite;
	}
	
    /** Test of getConfiguration method, of class org.afscme.enterprise.util.ConfigUtil. */
    public void testGetConfiguration() {
		System.out.println("Getting Configuration");
        ConfigurationData config = ConfigUtil.getConfigurationData();

		assertNotNull(config);
		System.out.println("Config is:\n" + config);
    }

    /** Test of getConfiguration method, of class org.afscme.enterprise.util.ConfigUtil. */
    public void testGetActionPrivileges() {
		System.out.println("Getting ActionPrivileges");
        ActionPrivileges privs = ConfigUtil.getActionPrivileges();

		assertNotNull(privs);
    }
    
    public void testGetPDFConfig() {
		System.out.println("Getting PDFConfiguration");
        PDFConfigurationData pdfConfig = ConfigUtil.getPDFConfigurationData();
        
		assertNotNull(pdfConfig);
    }
	
    public void testGetMessages()  {
		System.out.println("Getting System messages");
        Map messages = ConfigUtil.getMessages();
        
		assertNotNull(messages);
    }
	
}
