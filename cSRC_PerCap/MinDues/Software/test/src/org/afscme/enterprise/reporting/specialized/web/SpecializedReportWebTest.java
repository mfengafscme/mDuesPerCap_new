package org.afscme.enterprise.reporting.specialized.web;

import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Random;
import org.afscme.enterprise.test.AFSCMEWebTest;
import java.io.IOException;

/**
 * Contains UI tests for the specialized reports
 */
public class SpecializedReportWebTest extends AFSCMEWebTest
{
    public SpecializedReportWebTest(String name) throws IOException {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(SpecializedReportWebTest.class);
    }
	
	public void testNameCountReport() throws Exception {
		
		logln("==== AFSCME Login ====");
		
		loginAFSCME();

		//go to the add role page
		selectLink("Generate Reports");
        checkTitle("Generate Reports");
        
		selectLink("Name Count");
        checkTitle("First Name Count");
        
        setParameter("firstName", "John");
        submit();
        checkTitle("Report Submitted");
        
		selectLink("LOGOUT");
    }
}
