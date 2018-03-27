package org.afscme.enterprise.update.ejb;

import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.afscme.enterprise.update.Codes.UpdateFileType;
import org.afscme.enterprise.update.Codes.UpdateType;
import org.afscme.enterprise.util.JNDIUtil;



public class FileQueueBeanTest extends TestCase {
    
    public FileQueueBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FileQueueBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.reporting.base.ejb.ReportAccessBean. */
    public void testEjbCreate() {
        System.out.println("testEjbCreate");
        
        // Add your test code below by replacing the default call to fail.
        //fail("The test case is empty.");
    }
    
    /** Test of storeFile method */
    public void testStoreFile() throws NamingException, CreateException, RemoveException {
        System.out.println("\n*** Start 'storeFile' test *****\n");
        
        // get the bean's home object and create a remote object for it.
        FileQueue fileQueueBean = JNDIUtil.getFileQueueHome().create();
        
        // call the test target method.
        byte[] content = "This is test file upload content.".getBytes();
        Integer affPk = new Integer(1);
        Timestamp validDate = new Timestamp(new Date().getTime());
        fileQueueBean.storeFile(content, affPk, validDate, UpdateType.FULL, UpdateFileType.MEMBER, new Integer(10000001));

        System.out.println("\n**** End 'storeFileL' test ******\n");
            
        fileQueueBean.remove();
    }
    
    
}
