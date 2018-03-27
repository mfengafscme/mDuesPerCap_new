/*
 * MassChangeBeanTest.java
 * JUnit based test
 *
 * Created on August 14, 2003, 1:10 PM
 */

package org.afscme.enterprise.masschange.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Iterator;
import javax.ejb.*;
import javax.naming.NamingException;
import junit.framework.*;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.masschange.MassChangeData;
import org.afscme.enterprise.masschange.MassChangeRequest;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author hmaiwald
 */
public class MassChangeBeanTest extends TestCase {
    
    public MassChangeBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MassChangeBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.masschange.ejb.MassChangeBean. */
    public void testEjbCreate() throws Exception {
        MassChange mcBean = JNDIUtil.getMassChangeHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testEjbCreate: Creating MassChangeBean bean.");
        if (mcBean == null)
            fail("Mass Change was null.");
        mcBean.remove();
    }
    
    /** Test of scheduleMassChange method, of class org.afscme.enterprise.masschange.ejb.MassChangeBean. */
    public void testScheduleMassChange() throws Exception {
        MassChange mcBean = JNDIUtil.getMassChangeHome().create();
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("testScheduleMassChange: Updates the nightly run table for the Mass Change Request that is being made.");
        
        // Setup Mass Change request
        MassChangeRequest request = new MassChangeRequest();        
        request.setUserPk(new Integer(10000001));                
        request.setAffPk(new Integer(1));

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Test #1: Setup Mass Change data for Mass Change type = set MembershipInfoReportingSource.");
        System.out.println("Set Reporting Information Source to Local (MANUAL)");
        MassChangeData mcData = new MassChangeData();
        mcData.setMassChangeType(new Integer(22001)); 
        mcData.setNewSelect(new Integer(47003));
        request.addToChangePriorityList(mcData);
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Test #2: Setup Mass Change data for Mass Change type = set New Affiliate Identifier.");
        AffiliateIdentifier newAffID = new AffiliateIdentifier();
        request.setAffPk(new Integer(83));
        mcData = new MassChangeData();
        mcData.setMassChangeType(new Integer(22003)); 
        newAffID.setType(new Character('L'));
        newAffID.setLocal("300");
        newAffID.setState("VA");
        mcData.setNewAffiliateID(newAffID);
        request.addToChangePriorityList(mcData);

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Test #3: Setup Mass Change data for Mass Change type = set UnitWideNoCards.");
        mcData = new MassChangeData();
        mcData.setMassChangeType(new Integer(22004)); 
        mcData.setNewFlag(new Boolean(false));        
        request.addToChangePriorityList(mcData);

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Test #4: Setup Mass Change data for Mass Change type = set UnitWideNoPEMail.");
        mcData = new MassChangeData();
        mcData.setMassChangeType(new Integer(22005)); 
        mcData.setNewFlag(new Boolean(false));        
        request.addToChangePriorityList(mcData);
        
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Test #5: Setup Mass Change data for Mass Change type = set Affiliate Status Deactivated.");
        mcData = new MassChangeData();        
        request.setAffPk(new Integer(83));
        mcData.setMassChangeType(new Integer(22006)); 
        mcData.setStatusChangeType(new Integer(17004));
        request.addToChangePriorityList(mcData);

        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Test #6: Setup Mass Change data for Mass Change type = set Affiliate Status Merged.");
        System.out.println("");
        System.out.println("");
        newAffID = new AffiliateIdentifier();
        mcData = new MassChangeData();        
        request.setAffPk(new Integer(83));
        mcData.setMassChangeType(new Integer(22007)); 
        mcData.setStatusChangeType(new Integer(17006));
        mcData.setNewAffPk(new Integer(1));
        newAffID.setType(new Character('L'));
        newAffID.setCode(new Character('A'));
        newAffID.setState("VA");
        newAffID.setLocal("100");
        mcData.setNewAffiliateID(newAffID);
        request.addToChangePriorityList(mcData);

        // Schedult Mass Change requests - Update the Mass_Change_Batch_Control table
        // Records in this table will be picked up and process nightly via
        // the SQL Server scheduler..
        mcBean.scheduleMassChange(request);        
    }
        
}
