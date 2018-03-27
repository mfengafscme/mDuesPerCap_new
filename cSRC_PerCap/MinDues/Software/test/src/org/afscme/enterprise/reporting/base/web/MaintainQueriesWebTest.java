package org.afscme.enterprise.reporting.base.web;

import org.afscme.enterprise.test.AFSCMEWebTest;
import junit.framework.TestSuite;
import junit.framework.Test;
import java.io.IOException;
import java.util.Random;
import com.meterware.httpunit.*;



public class MaintainQueriesWebTest extends AFSCMEWebTest {
    
    //
    // These methods are required by JUnit
    //
    public MaintainQueriesWebTest(String name) throws IOException {
        super(name);
    }
    
    public static Test suite() {
        return new TestSuite(MaintainQueriesWebTest.class);
    }
    
    // test methods
    public void testAddQueryAndGenerateReport() throws Exception {
        logln("==== Maintain Query -- Add Query ====");
        
        // login (This is implemented in AFSCMEWebTest)
        loginAFSCME();
        
        // go to the 'Maintain Queries' page.
        selectLink("Maintain Queries");
        
        // click the "Add Query" button.
        //selectLink("Add Query");
        get("/addQuery.action");
        
        // select output fields and go to the next page
        // let's select "Member Key"
        setParameter("selectedOutputFields", new String[] {"1"});
        WebForm form = getForm();
        form.getScriptableObject().setParameterValue("linkClicked", "Preview Query");
        submit();
        checkTitle("Preview Query");
        
        // fill in the name and description and save the query
        String nameStr = "Auto Test" + new Random().nextInt();
        String desStr = "Auto Desc";
        logln("Query name = " + nameStr);
        setParameter("name", nameStr);
        setParameter("description", desStr);
        WebForm form2 = getForm();
        form2.getScriptableObject().setParameterValue("linkClicked", "");
        submit("saveButton");        
        checkTitle("Save Query Confirmation");
        
        logln("==== Generate Report ===="); 
        
        selectLink("Main Menu");
        checkTitle("Enterprise Application Main Menu");
        
        selectLink("Generate Reports");
        checkTitle("List Regular Reports"); 
        
        selectLink(nameStr);
        checkTitle("Output Format");       

        
        selectLink("LOGOUT");
   
    }
    
    
}
