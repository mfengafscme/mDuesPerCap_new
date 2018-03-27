package org.afscme.enterprise.codes.ejb;

import java.util.Iterator;
import java.util.Map;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.CodeTypeData;





public class MaintainCodesTest extends TestCase {

    MaintainCodes maintainCodes;
    
	public void setUp() throws Exception {
        System.out.println("Setting Up");
        maintainCodes = JNDIUtil.getMaintainCodesHome().create();
        clean();
    }
    
	public void tearDown() throws Exception {
        System.out.println("Tearing Down");
        maintainCodes.remove();
        clean();
	}
    
	public void clean() throws Exception {
        maintainCodes.deleteCodeType("TestCodeType");
    }
	
	public MaintainCodesTest(java.lang.String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(MaintainCodesTest.class);
		
		return suite;
	}
	
	public void testAddCodeType() throws NamingException, CreateException, RemoteException, RemoveException {

		System.out.println("Adding a Code Type");
        addCodeType();
	}

	public void testAddCodes() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Adding some Codes");
        addCodeTypeWithCodes();
	}

	public void testGetCategories() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Getting Code Categories");
		Map categories = maintainCodes.getCategories();
		if (categories == null)
			fail("categories was null");
		if (categories.size() == 0)
			fail("categories was empty");
		
		System.out.println("Categories are:");
		Iterator it = categories.keySet().iterator();
		while (it.hasNext())
			System.out.println("    " + categories.get(it.next()));
	}
	
	public void testGetCodeTypes() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Getting Code Types");
		Map codeTypes = maintainCodes.getCodeTypes();
		if (codeTypes == null)
			fail("code types was null");
		if (codeTypes.size() == 0)
			fail("code types was empty");

		System.out.println("Code Types are:");
		Iterator it = codeTypes.keySet().iterator();
		while (it.hasNext())
			System.out.println("    " + codeTypes.get(it.next()));
	}
	
	public void testGetCodeType() throws NamingException, CreateException, RemoteException, RemoveException {
		System.out.println("Getting TestCodeType");
        addCodeType();
		CodeTypeData codeType = maintainCodes.getCodeType("TestCodeType");
		System.out.println(codeType);
	}

	public void testDeleteCode() throws NamingException, CreateException, RemoteException, RemoveException {
        addCodeTypeWithCodes();
		System.out.println("Getting a code to delete");
		Map codes = maintainCodes.getCodes("TestCodeType");
		CodeData codeData = (CodeData)codes.values().iterator().next();
		System.out.println("Deleting " + codeData);
		maintainCodes.deleteCode(codeData.getPk());
	}
	
	public void testGetCodes() throws NamingException, CreateException, RemoteException, RemoveException {
        addCodeTypeWithCodes();
		System.out.println("Getting Test Codes");
		Map codes = maintainCodes.getCodes("TestCodeType");
		assertTrue("Codes is null", codes != null);
		assertTrue("Codes is empty ", codes.size() != 0);
		System.out.println("Codes are:");
		Iterator it = codes.keySet().iterator();
		while (it.hasNext())
			System.out.println("    " + codes.get(it.next()));
	}
    
	protected void addCodeType() throws NamingException, CreateException, RemoteException, RemoveException {
		CodeTypeData data = new CodeTypeData();
		data.setCategory(new Integer(1));
		data.setDescription("This is a test code type");
		data.setName("TestCodeType");
		data.setKey("TestCodeType");
		maintainCodes.addCodeType(data);
	}

	protected void addCodeTypeWithCodes() throws NamingException, CreateException, RemoteException, RemoveException {
        addCodeType();
		CodeData code = new CodeData();
		code.setCode("A");
		code.setDescription("Code A");
		code.setSortKey("2");
		maintainCodes.addCode("TestCodeType", code);
		code.setCode("B");
		code.setDescription("Code B");
		code.setSortKey("1");
		maintainCodes.addCode("TestCodeType", code);
		code.setCode("C");
		code.setDescription("Code C");
		code.setSortKey("3");
		maintainCodes.addCode("TestCodeType", code);
		code.setCode("D");
		code.setDescription("Code D");
		code.setSortKey("4");
		maintainCodes.addCode("TestCodeType", code);
    }
    
    
}
