
package org.afscme.enterprise.users;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * Reporesents all the information directly related to a user.
 * Roughly, all the data that is on the 'edit user' screen.  
 */
public class UserData implements Serializable
{
	/**
	 * Indicates the users's start page is the AFSCME main menu.
	 */
	public static final char START_PAGE_AFSCME = 'A';
	
	/**
	 * Indicates the user's start page is tha affilaite data utility.
	 */
	public static final char START_PAGE_AFFILIATE = 'D';
	
	/**
	 * Indicates the users's start page is the My Member Detail screen.
	 */
	public static final char START_PAGE_MEMBER = 'M';
	
	/**
	 * Indicates the users's start page is the Vendor Member Search screen.
	 */
	public static final char START_PAGE_VENDOR = 'V';

	/**
	 * The 'user id' used for login.  Initially the person_pk
	 */
	protected String userId;
	
	/**
	 * primary key of the user's challenge question
	 */
	protected Integer challengeQuestion;
	
	/**
	 * The user's answer to their challenge question.
	 */
	protected String challengeResponse;
	
	/**
	 * Administrator entered remarks about this user account.
	 */
	protected String remarks;
	
	/**
	 * Primary key of the user's department (or null for none).
	 */
	protected Integer department;
	
	/**
	 * The number of times in a row this user has logged in incorrectly.
	 */
	protected int badLoginAttemptCount;
	
	/**
	 * The last time this user logged in incorrectly.
	 */
	protected Timestamp lockoutDate;
	
	/**
	 * The time that the user's password expires.
	 */
	protected Timestamp passwordExpirationDate;
	
	/**
	 * The time of the user's last login.
	 */
	protected Timestamp lastLoginDate;
    
    /**
     * Indicates the initial page for the user.
     * Must be one of the values from the START_PAGE_XXX constants in this class.
     */
    protected char startPage;

    /**
     * ID of the associated person
     */
    protected Integer personPk;    
    
    /** Getter for property badLoginAttemptCount.
     * @return Value of property badLoginAttemptCount.
     */
    public int getBadLoginAttemptCount() {
        return badLoginAttemptCount;
    }
    
    /** Setter for property badLoginAttemptCount.
     * @param badLoginAttemptCount New value of property badLoginAttemptCount.
     */
    public void setBadLoginAttemptCount(int badLoginAttemptCount) {
        this.badLoginAttemptCount = badLoginAttemptCount;
    }
    
    /** Getter for property badLoginAttemptDate.
     * @return Value of property badLoginAttemptDate.
     */
    public Timestamp getLockoutDate() {
        return lockoutDate;
    }
    
    /** Setter for property badLoginAttemptDate.
     * @param badLoginAttemptDate New value of property badLoginAttemptDate.
     */
    public void setLockoutDate(Timestamp lockoutDate) {
        this.lockoutDate = lockoutDate;
    }
    
    /** Getter for property challengeQuestion.
     * @return Value of property challengeQuestion.
     */
    public Integer getChallengeQuestion() {
        return challengeQuestion;
    }
    
    /** Setter for property challengeQuestion.
     * @param challengeQuestion New value of property challengeQuestion.
     */
    public void setChallengeQuestion(Integer challengeQuestion) {
        this.challengeQuestion = challengeQuestion;
    }
    
    /** Getter for property challengeResponse.
     * @return Value of property challengeResponse.
     */
    public String getChallengeResponse() {
        return challengeResponse;
    }
    
    /** Setter for property challengeResponse.
     * @param challengeResponse New value of property challengeResponse.
     */
    public void setChallengeResponse(String challengeResponse) {
        this.challengeResponse = challengeResponse;
    }
    
    /** Getter for property department.
     * @return Value of property department.
     */
    public Integer getDepartment() {
        return department;
    }
    
    /** Setter for property department.
     * @param department New value of property department.
     */
    public void setDepartment(Integer department) {
        this.department = department;
    }
    
    /** Getter for property lastLoginDate.
     * @return Value of property lastLoginDate.
     */
    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }
    
    /** Setter for property lastLoginDate.
     * @param lastLoginDate New value of property lastLoginDate.
     */
    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    /** Getter for property passwordExpirationDate.
     * @return Value of property passwordExpirationDate.
     */
    public Timestamp getPasswordExpirationDate() {
        return passwordExpirationDate;
    }
    
    /** Setter for property passwordExpirationDate.
     * @param passwordExpirationDate New value of property passwordExpirationDate.
     */
    public void setPasswordExpirationDate(Timestamp passwordExpirationDate) {
        this.passwordExpirationDate = passwordExpirationDate;
    }
    
    /** Getter for property remarks.
     * @return Value of property remarks.
     */
    public String getRemarks() {
        return remarks;
    }
    
    /** Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /** Getter for property userId.
     * @return Value of property userId.
     */
    public String getUserId() {
        return userId;
    }
    
    /** Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /** Getter for property startPage.
     * @return Value of property startPage.
     */
    public char getStartPage() {
        return startPage;
    }
    
    /** Setter for property startPage.
     * @param startPage New value of property startPage.
     */
    public void setStartPage(char startPage) {
        this.startPage = startPage;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     */
    public Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     */
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
    }

    /**
     * Returns a string representation of the data in this object
     */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("UserData [");
		buf.append(", userId: " + userId);
		buf.append(", personPk: " + personPk);
		buf.append(", passwordExpirationDate: " + passwordExpirationDate);
		buf.append(", lastLoginDate: " + lastLoginDate);
		buf.append(", lockoutDate: " + lockoutDate);
		buf.append(", badLoginAttemptCount: " + badLoginAttemptCount);
		buf.append(", department: " + department);
		buf.append(", challengeQuestion: " + challengeQuestion);
		buf.append(", challengeResponse: " + challengeResponse);
		buf.append(", startPage: " + startPage);
		buf.append(", remarks: " + remarks);
		buf.append("]");
		return buf.toString();
	}
}
