package org.afscme.enterprise.controller.web;

import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Random;
import org.afscme.enterprise.test.AFSCMEWebTest;
import java.io.IOException;

/**
 * Contains functional tests for the AccessControl portion of the UI
 */
public class ControllerTest extends AFSCMEWebTest
{
    public ControllerTest(String name) throws IOException {
        super(name);
    }
    public static Test suite() {
        return new TestSuite(ControllerTest.class);
    }
	
	public void testAFSCMELogin() throws Exception {
		
		logln("==== AFSCME Login ====");
		
		loginAFSCME();

        assertNotNull(getResponse().getLinkWith("Maintain Privileges"));

		selectLink("LOGOUT");
    }
}
