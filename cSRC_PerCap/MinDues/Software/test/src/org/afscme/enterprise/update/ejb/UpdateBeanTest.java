package org.afscme.enterprise.update.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.NamingException;
import junit.framework.*;
import org.afscme.enterprise.update.ejb.*;
import org.afscme.enterprise.update.member.MemberPreUpdateSummary;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author frusso
 */
public class UpdateBeanTest extends TestCase {
    
    private static Update updateBean;
    static {
        try {
            updateBean = JNDIUtil.getUpdateHome().create();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public UpdateBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(UpdateBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testEjbCreate() {
        System.out.println("testEjbCreate");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of ejbRemove method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testEjbRemove() {
        System.out.println("testEjbRemove");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of applyUpdate method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testApplyUpdate() {
        System.out.println("testApplyUpdate");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of generatePreUpdateSummary method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGeneratePreUpdateSummary() {
        System.out.println("testGeneratePreUpdateSummary");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getMemberPreUpdateSummary method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    public void testGetMemberPreUpdateSummary() {
        System.out.println("start testGetMemberPreUpdateSummary");
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        
        // Add your test code below by replacing the default call to fail.
        MemberPreUpdateSummary summary = updateBean.getMemberPreUpdateSummary(new Integer(36));
        System.out.println("summary: " + summary);
        
        System.out.println(" ");System.out.println(" ");System.out.println(" ");
        System.out.println("end testGetMemberPreUpdateSummary");
        
    }
    
    /** Test of getReviewSummary method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetReviewSummary() {
        System.out.println("testGetReviewSummary");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getMemberCount method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetMemberCount() {
        System.out.println("testGetMemberCount");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of matchMember method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testMatchMember() {
        System.out.println("testMatchMember");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of matchOfficer method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testMatchOfficer() {
        System.out.println("testMatchOfficer");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of matchRebate method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testMatchRebate() {
        System.out.println("testMatchRebate");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of matchParticipation method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testMatchParticipation() {
        System.out.println("testMatchParticipation");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of calculateMemberInSystemCounts method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testCalculateMemberInSystemCounts() {
        System.out.println("testCalculateMemberInSystemCounts");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of calculateMemberOfficers method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testCalculateMemberOfficers() {
        System.out.println("testCalculateMemberOfficers");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAffiliateOfficers method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetAffiliateOfficers() {
        System.out.println("testGetAffiliateOfficers");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of doMemberUpdate method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testDoMemberUpdate() {
        System.out.println("testDoMemberUpdate");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getPositionChanges method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetPositionChanges() {
        System.out.println("testGetPositionChanges");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of rejectUpdate method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testRejectUpdate() {
        System.out.println("testRejectUpdate");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of storePreUpdateSummary method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testStorePreUpdateSummary() {
        System.out.println("testStorePreUpdateSummary");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of storePositionChanges method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testStorePositionChanges() {
        System.out.println("testStorePositionChanges");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getOfficerElements method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetOfficerElements() {
        System.out.println("testGetOfficerElements");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of updateOfficers method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testUpdateOfficers() {
        System.out.println("testUpdateOfficers");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of updateRebates method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testUpdateRebates() {
        System.out.println("testUpdateRebates");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getAffiliateStatusCode method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetAffiliateStatusCode() {
        System.out.println("testGetAffiliateStatusCode");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of getUpdateFile method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testGetUpdateFile() {
        System.out.println("testGetUpdateFile");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }
    
    /** Test of storeUpdateSummary method, of class org.afscme.enterprise.update.ejb.UpdateBean. */
    /*public void testStoreUpdateSummary() {
        System.out.println("testStoreUpdateSummary");
        
        // Add your test code below by replacing the default call to fail.
        fail("The test case is empty.");
    }*/
    
    
}
