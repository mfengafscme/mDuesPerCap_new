/*
 * TextUtilTest.java
 * JUnit based test
 *
 * Created on July 11, 2002, 8:34 PM
 */

package org.afscme.enterprise.util;

import java.util.Calendar;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import java.text.ParseException;
import java.sql.Timestamp;
import java.util.*;

public class TextUtilTest extends TestCase {
    
    public TextUtilTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TextUtilTest.class);
        
        return suite;
    }
    
    /** Test of format method, of class org.afscme.enterprise.util.TextUtil. */
    public void testFormat() {
		System.out.println("testing TextUtil date formating");
		Calendar endOf1999 = Calendar.getInstance();
        TextUtil.format(new Timestamp(endOf1999.getTime().getTime()));
    }
    
    /** Test of parseDate method, of class org.afscme.enterprise.util.TextUtil. */
    public void testParseDate() throws ParseException {
		System.out.println("testing TextUtil date parsing");

		TextUtil.parseDate("3/4/1995");
        TextUtil.parseDate("3/4/93");
        TextUtil.parseDate("8/9/21");
    }
	
	public void testPad() {
		System.out.println("testing TextUtil pad functions");
		
		assertEquals("abcd", TextUtil.padLeading("abcd", 4, '0'));
		assertEquals("00ab", TextUtil.padLeading("ab", 4, '0'));
		assertEquals("xyzEEEEE", TextUtil.padTrailing("xyz", 8, 'E'));
	}
	
	public void testFormatMessage() {
		System.out.println("testing message formatting");
		assertEquals("This is a message with a parameter which is Apple", TextUtil.format("test.message", new Object[] { "Apple" }));
	}

	public void testIsEmpty() {
        String nullString = null;
		System.out.println("testing isEmpty");
		assertEquals(true, TextUtil.isEmpty(nullString));
		assertEquals(true, TextUtil.isEmpty(""));
		assertEquals(false, TextUtil.isEmpty("value"));
        List list = new LinkedList();
		assertEquals(true, TextUtil.isEmpty(list));
        list.add("");
		assertEquals(true, TextUtil.isEmpty(list));
        list.add(nullString);
		assertEquals(true, TextUtil.isEmpty(list));
        list.add("value");
		assertEquals(false, TextUtil.isEmpty(list));
	}

}
