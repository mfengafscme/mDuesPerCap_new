package org.afscme.enterprise.util;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;


/**
 * Contains static methods for common cryptography functions like generating secure random
 * passwords, and hashing passwords.
 */
public class CryptoUtil {

	/** Characters for converting bytes to hex strings */
	private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * Characters suitable for use in a password.
	 * Includes all the alphabetic letters except (i, l, o, I, L, O) and all digits except (0, 1).
	 * These characters are ommited as their glphs can by mistaken for other glyphs.
	 */
	private static final char[] PASSWORD_CHARS = {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'2', '3', '4', '5', '6', '7', '8', '9'};

	private static final SecureRandom s_sr = getSecureRandom();
	
	/**
	 * Performs an MD5 has on str, returns the result as a 32 character hex string
	 */
	public static String hash(String str)  {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			//this can only happen once, make it a runtime exception
			throw new RuntimeException(e);
		}
		byte[] bin = md.digest(str.getBytes());
		return bin2hex(bin);
	}

	/**
	 * Converts the given bytes to a hex string
	 */
	public static String bin2hex(byte[] bin) {
		char[] result = new char[bin.length * 2];
		for (int i = 0; i < bin.length; i++) {
			int c = ((char)bin[i] & 0xFF);
			int j = (c >> 4);
			result[i*2] = HEX[j];
			j = (c & 0xF);
			result[(i*2)+1]=HEX[j];
		}
		return new String(result);
	}

	/**
	 * Returns a secure random integer.  (Secure in that the resulting integer cannot
	 * be predicted.)
	 */
	public static int randomInt() {
		return s_sr.nextInt();
	}

	/**
	 * Returns a secure random string.  (Secure in that the resulting string cannot be predicted.)
	 * The result string may contain any alpha letters or numbers excluding 0, O, and o.
	 * 
	 * @param the length of the resulting string
	 */
	public static String randomPassword(int length) {
	
		StringBuffer buf = new StringBuffer();
		while (0 < length--)
			buf.append(PASSWORD_CHARS[s_sr.nextInt(PASSWORD_CHARS.length - 1)]);
		return buf.toString();
	}
	
    /**
     * Gets the secure random generator form the JDK.  Called once at startup.
     */
	private static SecureRandom getSecureRandom() {
		SecureRandom sr = null;
		
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			//this can only happen once, so make it a runtime exception
			throw new RuntimeException(e);
		}
		
		return sr;
	}
}
