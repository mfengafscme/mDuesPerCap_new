/*
 * MailUtilTest.java
 * JUnit based test
 *
 * Created on July 17, 2002, 7:51 PM
 */

package org.afscme.enterprise.util;

import javax.mail.MessagingException;
import junit.framework.TestSuite;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Tests the MailUtil class.
 */
public class MailUtilTest extends TestCase {
    
    public MailUtilTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MailUtilTest.class);
        
        return suite;
    }
    
    /** Test of sendMail method, of class org.afscme.enterprise.util.MailUtil. */
    public void testSendMail() throws IOException, MessagingException {
        
		System.out.println("Sending email to kvogel@grci.com");
        MailUtil.sendMail("kvogel@grci.com",
								"Kent Vogel",
								"kvogel@grci.com",
								System.getProperty("user.name"),
								"Test Subject",
								"This is a test message");
    }
        
    /** Test of sendMail method, of class org.afscme.enterprise.util.MailUtil. */
    public void testSendMailWithAttachment() throws IOException, MessagingException {

		System.out.println("Sending email to kvogel@grci.com with an attachment");
        ByteArrayInputStream bais = new ByteArrayInputStream("This is the contents of the file".getBytes());
        MailUtil.sendMail("kvogel@grci.com",
								"Kent Vogel",
								"kvogel@grci.com",
								System.getProperty("user.name"),
								"Test Subject",
								"This is a test message with an attachment",
                                bais,
                                "attached_file.txt",
                                "text/plain");
    }
    
}
