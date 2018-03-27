/*
 * MaintainAffiliatesBeanTest.java
 * JUnit based test
 *
 * Created on April 28, 2003, 9:20 AM
 */

package org.afscme.enterprise.affiliate.staff.ejb;

import java.util.LinkedList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.affiliate.staff.StaffResult;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.codes.Codes.Department;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.util.JNDIUtil;

public class MaintainAffiliateStaffBeanTest extends TestCase {
    
    protected MaintainAffiliateStaff affStaff;
    
    protected Integer affPk1;
    protected Integer affPk2;
    protected Integer personPk;
    protected Integer userPk;
    protected StaffData staffData;
    protected String comment;
    protected PersonData personData;
    
    public MaintainAffiliateStaffBeanTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MaintainAffiliateStaffBeanTest.class);
        return suite;
    }
    
    public void setUp() throws Exception {
		affStaff = JNDIUtil.getMaintainAffiliateStaffHome().create();
        affPk1 = new Integer(1);
        affPk2 = new Integer(2);
        personPk = new Integer(10000501);
        userPk = new Integer(10000001);
        
        staffData = new StaffData();
        staffData.setStaffTitlePk(new Integer(32001));
        staffData.setPocForPk(Department.MD);
        
        comment = "Test Comment";
        
        personData = new PersonData();
        personData.setPersonPk(personPk);
        personData.setFirstNm("UpdatedFirstName");
        personData.setLastNm("UpdatedLastName");
        personData.setSsnValid(new Boolean(true));
        personData.setSsnDuplicate(new Boolean(false));
        personData.setMarkedForDeletionFg(new Boolean(false));

        CommentData cd = new CommentData();
        cd.setComment("Person Comment");
        personData.setTheCommentData(cd);
    }
    
    public void tearDown() throws Exception {
        affStaff.remove();
    }
        
    public void testGetAffiliateStaff() throws Exception {
		try {
			addStaff();
			int total = affStaff.getAffiliateStaff(affPk1, new SortData(), new LinkedList());
		}
		finally {
			removeStaff();
		}
    }
    
    public void testAddStaff() throws Exception {
		try {
			addStaff();
		}
		finally {
			removeStaff();
		}
    }
    
    public void testAddLocalServiced() throws Exception {
		try {
			addStaff();
			affStaff.addLocalServiced(affPk1, personPk, affPk2);
		}
		finally {
			removeStaff();
		}
    }
    
    public void testRemoveLocalServiced() throws Exception {
		try {
			addStaff();
			affStaff.addLocalServiced(affPk1, personPk, affPk2);
			affStaff.removeLocalServiced(affPk1, personPk, affPk2);
		}
		finally {
			removeStaff();
		}
    }
    
    
    public void testGetSingleAffiliateStaff() throws Exception {
		try {
			addStaff();
			StaffData data = affStaff.getAffiliateStaff(affPk1, personPk);
		}
		finally {
			removeStaff();
		}
    }
    
    public void testGetComment() throws Exception {
		try {
			addStaff();
			CommentData comment = affStaff.getComment(affPk1, personPk);
			assertNotNull(comment);
		}
		finally {
			removeStaff();
		}
    }
    
    public void testUpdateStaff() throws Exception {
		try {
			addStaff();
			affStaff.updateAffiliateStaff(affPk1, personData, staffData, comment, userPk);
		}
		finally {
			removeStaff();
		}
    }
    
    protected void removeStaff() throws Exception {
        assertTrue(affStaff.removeAffiliateStaff(affPk1, personPk));
    }
        
    protected void addStaff() throws Exception {
        addStaff(affPk1, personPk, userPk);
    }
    
    protected void addStaff(Integer affPk1, Integer personPk, Integer userPk) throws Exception {
        affStaff.addAffiliateStaff(affPk1, personPk, staffData, comment, userPk);
    }
    
}
