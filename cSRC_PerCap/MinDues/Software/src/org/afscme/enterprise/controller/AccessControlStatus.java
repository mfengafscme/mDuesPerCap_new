package org.afscme.enterprise.controller;


/**
 * This interface is used to define int constants returned from methods on the 
 * AccessControl object.
 */
public interface AccessControlStatus 
{
	
	/**
	 * Login succeeded.
	 */
	public static final int LOGIN_RESULT_OK = 0;
	
	/**
	 * Username and/or password supplied were not correct.
	 */
	public static final int LOGIN_RESULT_FAILED = 1;
	
	/**
	 * The user's account is locked out.
	 */
	public static final int LOGIN_RESULT_LOCKED_OUT = 2;
	
	/**
	 * The users password will be emailed to them.
	 */
	public static final int REQUEST_PASSWORD_OK = 0;
	
	/**
	 * The challenge response provided was not correct.
	 */
	public static final int REQUEST_PASSWORD_BAD_RESPONSE = 1;

	/**
	 * The user's response was correct, but email could not be send as their account
	 * does not have a proper email address to send the password to.  Password was not reset.
	 */
	public static final int REQUEST_PASSWORD_NO_EMAIL = 3;

	/**
	 * The user successfully updated their account info (password)
	 */
	public static final int CHANGE_PASSWORD_OK = 0;

	/**
	 * The user attempted to update their account info (password), and the value they entered
	 * for 'old password' was incorrect.
	 */
	public static final int CHANGE_PASSWORD_BAD_OLD_PASSWORD = 1;

}
