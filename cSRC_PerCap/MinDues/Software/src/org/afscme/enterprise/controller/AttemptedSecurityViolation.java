package org.afscme.enterprise.controller;

/**
 * Thrown by actions when they detect a user attempting to circumvent security.
 */
public class AttemptedSecurityViolation extends Exception {
	
	protected UserSecurityData m_usd;
	
	public AttemptedSecurityViolation(UserSecurityData usd, String message) {
		super(message + " Attempted by: " + usd);
		m_usd = usd;
	}
	
	public UserSecurityData getUserSecurityData() {
		return m_usd;
	}
	
	public void setUserSecurityData(UserSecurityData usd) {
		m_usd = usd;
	}
	
}
