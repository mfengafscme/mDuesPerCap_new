package org.afscme.enterprise.util;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import java.security.NoSuchAlgorithmException;


public class CryptoUtilTest extends TestCase {
	
	public CryptoUtilTest(java.lang.String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(CryptoUtilTest.class);
		
		return suite;
	}
	
	public void testHash() throws NoSuchAlgorithmException {
		
		String password = "password";
		System.out.println("Hashing '"+password+"'");
		String result = CryptoUtil.hash(password);
		System.out.println("Result is '"+result+"'");
		assertEquals(result, CryptoUtil.hash(password));
	}
	
	public void testBin2hex() {
		
		System.out.println("Converting 0xab 0xc1 0x23 to hex");
		byte[] input = new byte[] {(byte)0xab, (byte)0xc1, (byte)0x23};
		String result = CryptoUtil.bin2hex(input);
		System.out.println("Result is '"+result+"'");
		assertEquals("ABC123", result);
	}

	public void testRandomIntMT() {
		System.out.print("Getting 100 random integers from 10 threads");

		class CryptoRunner implements Runnable {
			private boolean done;
			public void run()
			{
				for (int i = 0; i < 10; i++)
  				    CryptoUtil.randomInt();
				setDone();
			}
			public synchronized void setDone() {
				System.out.print(".");
				done = true;
				notifyAll();
			}
			public synchronized void waitUntilDone() {
				while (!done) {
					try {
						wait();
					} catch (InterruptedException e) { }
				}
			}
		}
		
		CryptoRunner[] t = new CryptoRunner[10];
		for (int i = 0; i < 10; i++) {
			t[i] = new CryptoRunner();
			new Thread(t[i]).start();
		}
		for (int i = 0; i < 10; i++) {
			t[i].waitUntilDone();
		}
		System.out.println("Done");
	}

	public void testRandomPassword() throws NoSuchAlgorithmException {

		System.out.println("Generating random password");
		String str = CryptoUtil.randomPassword(10);
		assertEquals(10, str.length());
		System.out.println("Random password is '" + str + "'");
	}
	
	
}
