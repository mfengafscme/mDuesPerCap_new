package org.afscme.enterprise.codes.web;

import java.io.IOException;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.test.AFSCMEWebTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.CryptoUtil;


/**
 * Contains functional tests for the MaintainCodes portion of the UI
 */
public class MaintainCodesWebTest extends AFSCMEWebTest
{
    public MaintainCodesWebTest(String name) throws IOException {
        super(name);
    }
    public static Test suite() {
        return new TestSuite(MaintainCodesWebTest.class);
    }
    
	public void testAddCodeType() throws Exception {

		loginAFSCME();

        //go to maintain codes
        selectLink("Maintain Codes");
        checkTitle("Code Types");

        //go to add code type
        selectLink("Add Code Type");
        checkTitle("Add Code Type");
        
        //create the code type
        setParameter("name", "Test Code Name");
        setParameter("codeTypeKey", String.valueOf(Math.abs(CryptoUtil.randomInt())));
        setParameter("description", "Test Code Description");
        submit();
        checkTitle("Edit Code Type");

        //go to the add code page
        submit("addCode");
        checkTitle("Add Code");
        
        //create the code
        setParameter("code", "Code");
        setParameter("codeDescription", "Description");
        setParameter("sortKey", "1");
        submit();
        checkTitle("Edit Code Type");
        
        //go to the edit page for that code again
        selectRowLink("Edit", "Code");
        checkTitle("Edit Code");

        //update the code
        setParameter("code", "Code2");
        setParameter("codeDescription", "Description2");
        setParameter("sortKey", "2");
        submit();
        checkTitle("Edit Code Type");
        
        //finish with the code type
        submit();
        checkTitle("Code Types");
        
        //go back to the code type
        selectLink("Test Code Name");
        checkTitle("Edit Code Type");
        
        //delete
        selectLink("Delete");
        checkTitle("Code Types");
        
		selectLink("LOGOUT");
    }
}
