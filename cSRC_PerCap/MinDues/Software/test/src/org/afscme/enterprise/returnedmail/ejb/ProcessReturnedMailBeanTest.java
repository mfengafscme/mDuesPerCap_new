/*
 * ProcessReturnedMailBeanTest.java
 * JUnit based test
 *
 * Created on May 14, 2003, 3:48 PM
 */

package org.afscme.enterprise.returnedmail.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import javax.naming.NamingException;
import javax.naming.Context;
import junit.framework.*;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.returnedmail.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import org.apache.log4j.Logger;

/**
 *
 * @author hmaiwald
 */
public class ProcessReturnedMailBeanTest extends TestCase {
    
    
    public ProcessReturnedMailBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ProcessReturnedMailBeanTest.class);
        
        return suite;
    }
    
    /** Test of ejbCreate method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testEjbCreate() throws Exception {
        System.out.println("testEjbCreate: Creating ProcessReturnedMail bean");
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        if (prmBean == null)
            fail("ProcessReturnedMail was null");
        prmBean.remove();
    }
    
    /** Test of processReturnedAddresses method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testProcessReturnedAddresses() throws Exception {
        System.out.println("testProcessReturnedAddresses");
        
        LinkedList addressIds = new LinkedList();
        addressIds.add("112");
        addressIds.add("213");
        addressIds.add("123abc");
        System.out.println("Process Returned Mail for AddressIds = " + addressIds + ", modified by 10001516");
        Integer modPk = new Integer(10001516);
        ReturnedMailSummary summary = null;
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        summary = prmBean.processReturnedAddresses((java.util.Collection)addressIds, modPk);
        prmBean.remove();
        if (summary == null) {
            fail("Processed Returned Mail result summary was null.");
        } else {
            System.out.println("Summary results of after Processed Returned Mail = " + summary);
        }
    }
    
    /** Test of isMemberStatusInactive method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testIsMemberStatusInactive() throws Exception {
        System.out.println("testIsMemberStatusInactive");
        
        // Add your test code below by replacing the default call to fail.
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        boolean inactive = prmBean.isMemberStatusInactive(new Integer(12));
        System.out.println("Is Person_pk = 12 is inactive? " + inactive);
        prmBean.remove();
    }
    
    /** Test of isPersonDeceased method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testIsPersonDeceased() throws Exception {
        System.out.println("testIsPersonDeceased");
        
        // Add your test code below by replacing the default call to fail.
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        boolean deceased = prmBean.isPersonDeceased(new Integer(12));
        System.out.println("Is Person_pk = 12 deceased? " + deceased);
        prmBean.remove();
    }
    
    /** Test of isSMA method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testIsSMA() throws Exception {
        System.out.println("testIsSMA");
        
        // Add your test code below by replacing the default call to fail.
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        boolean sma = prmBean.isSMA(new Integer(12));
        System.out.println("Is address_pk = 12 in the System Mailing Address? " + sma);
        prmBean.remove();
    }
    
    /** Test of getPersonAddress method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testGetPersonAddress() throws Exception {
        System.out.println("testGetPersonAddress");
        
        // Add your test code below by replacing the default call to fail.
        ReturnedPersonAddress data = null;
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        System.out.println("Retrieving Person Address with address_pk = " + 12);
        data = prmBean.getPersonAddress(new Integer(12));
        prmBean.remove();
        if (data == null) {
            fail("Person Address Data was null.");
        } else {
            System.out.println("+++++++++ Person Address Data retrieved successfully: " + data);
        }
    }
    
    /** Test of getOrganizationAddress method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testGetOrganizationAddress() throws Exception {
        System.out.println("testGetOrganizationAddress");
        
        // Add your test code below by replacing the default call to fail.
        ReturnedOrganizationAddress data = null;
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        System.out.println("Retrieving Organization Address with address_pk = " + 12);
        data = prmBean.getOrganizationAddress(new Integer(12));
        prmBean.remove();
        if (data == null) {
            fail("Organization Address Data was null.");
        } else {
            System.out.println("+++++++++ Organization Address Data retrieved successfully: " + data);
        }
    }
    
    /** Test of getAffAbbreviatedName method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testGetAffAbbreviatedName() throws Exception {
        System.out.println("testGetAffAbbreviatedName");
        
        // Add your test code below by replacing the default call to fail.
        String name = null;
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        System.out.println("Retrieving Affiliate Abbreviated Name with aff_pk = " + 12);
        name = prmBean.getAffAbbreviatedName(new Integer(12));
        prmBean.remove();
        if (name == null) {
            fail("Affiliate Abbreviated Name was null.");
        } else {
            System.out.println("+++++++++ Affiliate Abbreviated Name (with aff_pk=12) retrieved is: " + name);
        }
    }
    
    /** Test of saveOrganizationAddress method, of class org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMailBean. */
    public void testSaveOrganizationAddress() throws Exception {
        System.out.println("testSaveOrganizationAddress");
        
        // Add your test code below by replacing the default call to fail.
        ReturnedOrganizationAddress address = new ReturnedOrganizationAddress();
        RecordData recData = new RecordData();
        ProcessReturnedMail prmBean = JNDIUtil.getProcessReturnedMailHome().create();
        recData.setModifiedDate(new java.sql.Timestamp(new java.util.Date().getTime()));
        recData.setModifiedBy(new Integer(10001516));
        address.setTheRecordData(recData);
        address.setAddressPK(new Integer(12));
        address.setBad(true);
        address.setBadDate(new java.sql.Timestamp(new java.util.Date().getTime()));
        prmBean.saveOrganizationAddress(address);
        prmBean.remove();
        System.out.println("Saving Organization Address = " + address);
    }
    
}
