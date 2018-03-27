package org.afscme.enterprise.returnedmail.web;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Random;
import org.afscme.enterprise.test.AFSCMEWebTest;
import java.io.IOException;

/**
 * Contains functional tests for the ProcessReturnedMail portion of the UI
 */
public class ProcessReturnedMailWebTest extends AFSCMEWebTest {
    public ProcessReturnedMailWebTest(String name) throws IOException {
        super(name);
    }
    public static Test suite() {
        return new TestSuite(ProcessReturnedMailWebTest.class);
    }
    
    /**
     * Tests process returned mail.
     */
    public void testProcessReturnedMail() throws Exception {
        
        logln("\n==== Process Returned Mail ====\n");
        
        //login
        loginAFSCME();
        
        //go to the 'Process Returned Mail' page.
        selectLink("Process Returned Mail");
                
        //fill in the parameters
        setParameter("addressIds", "120\n221" );
        
        submit("submit");
        //logout
        selectLink("LOGOUT");
    }
}
