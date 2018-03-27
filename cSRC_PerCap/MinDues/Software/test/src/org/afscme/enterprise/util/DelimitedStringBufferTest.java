package org.afscme.enterprise.util;

import junit.framework.*;

public class DelimitedStringBufferTest extends TestCase {
	
	public DelimitedStringBufferTest(java.lang.String testName) {
		super(testName);
	}
	
	
	public static Test suite() {
		TestSuite suite = new TestSuite(DelimitedStringBufferTest.class);
		
		return suite;
	}
	
	/** Test of append method, of class org.afscme.enterprise.util.DelimitedStringBuffer. */
	public void testAppend() {
		
		System.out.println("Testing Append");
		DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");
		buf.append("First").append("Last");
		assertEquals("First Last", buf.toString());

		buf = new DelimitedStringBuffer(" ");
		buf.append("First").append(null).append(null).append("Last");
		assertEquals("First Last", buf.toString());

		buf = new DelimitedStringBuffer(" ");
		buf.append(null).append(null).append("First").append(null).append("Last").append(null);
		assertEquals("First Last", buf.toString());

		buf = new DelimitedStringBuffer(" ");
		assertEquals("", buf.toString());
	}
}
