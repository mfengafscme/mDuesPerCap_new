package org.afscme.enterprise.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;



/**
 * Contains the information the application needs to handle access control for the 
 * user.  This object is kept in the HttpSession for each  logged in user.
 */
public class UserSecurityData implements Serializable
{
	/**
	 * Pk of the logged in person
	 */
	protected Integer personPk;
	
	/**
	 * The set of (non-data utility) privileges this user has access to.
	 */
	protected Set privileges;

    /**
	 * The set of data utility privileges this user has access to.
	 */
	protected Set dataUtilityPrivileges;
	
	/**
	 * Pk of the user's department, or null if none.
	 */
	protected Integer department;
	
	/**
	 * Pk of the affiliate this user is acting as, for the DataUtility flows, or null if not using the data utility.
	 */
	protected Integer actingAsAffiliate;
	
	/**
     * If actingAsAffiliate is set, this property will contain that affiliate, and all it's sub affiliates,
     * other wilse it will be null
	 */
	protected Set accessibleAffiliates;
    
	/** Name of the affiliate the user is acting as */
	protected String affiliateName;

	/**
	 * The set of reports this user has access to.
	 * This value is initially null when 
	 * this structure is returned from AccessControl.login().  It is filled in after a 
	 * call to AccessControl.getReportsPrivileges().
	 */
	protected Set reports;
	
	/**
	 * The set of fields this user has access to for creating reports.
	 * This value is initially null when this structure is returned from AccessControl.login().  It 
	 * is filled in after a call to AccessControl.getReportsPrivileges().
	 */
	protected Set fields;
    	
	/** Holds value of property userId. */
	protected String userId;
	
	/** True iff the user's password is expired, or this is their first login */
	protected boolean forceChangePassword;

    /** The user's preffered start page */
    protected char startPage;
    
	/**
	 * Displays this object's data as a String
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("personPk=" + personPk + ", ");
		buf.append("actingAsAffiliate=" + actingAsAffiliate + ", ");
		buf.append("accessibleAffiliates=" + accessibleAffiliates + ", ");
		buf.append("department=" + department + ", ");
		buf.append("fields=" + fields + ", ");
		buf.append("privileges=" + privileges + ", ");
		buf.append("dataUtilityPrivileges=" + dataUtilityPrivileges + ", ");
		buf.append("reports=" + reports + ", ");
		buf.append("forceChangePassword=" + forceChangePassword + ", ");
		buf.append("startPage=" + startPage);
		
		return buf.toString();
	}
	
    /**
     * See {@link UserSecurityData#reports}
     */    
    public Set getReports() {
        return reports;
    }
    
    /**
     * See {@link UserSecurityData#reports}
     */    
    public void setReports(Set reports) {
        this.reports = reports;
    }
    
    /**
     * See {@link UserSecurityData#privileges}
     */    
    public Set getPrivileges() {
        return privileges;
    }
    
    /**
     * See {@link UserSecurityData#privileges}
     */    
    public void setPrivileges(Set privileges) {
        this.privileges = privileges;
    }
    
    /**
     * See {@link UserSecurityData#fields}
     */    
    public Set getFields() {
        return fields;
    }
    
    /**
     * See {@link UserSecurityData#fields}
     */    
    public void setFields(Set fields) {
        this.fields = fields;
    }
    
    /**
     * See {@link UserSecurityData#department}
     */    
    public Integer getDepartment() {
        return department;
    }
    
    /**
     * See {@link UserSecurityData#department}
     */    
    public void setDepartment(Integer department) {
        this.department = department;
    }
    
    /**
     * See {@link UserSecurityData#actingAsAffiliate}
     */    
    public Integer getActingAsAffiliate() {
        return actingAsAffiliate;
    }
    
    /**
     * See {@link UserSecurityData#actingAsAffiliate}
     */    
    public void setActingAsAffiliate(Integer actingAsAffiliate) {
        this.actingAsAffiliate = actingAsAffiliate;
    }
    
    /**
     * Returns true iff the logged in user is currently using the data utility
     */
    public boolean isActingAsAffiliate() {
        return actingAsAffiliate != null;
    }
    
    /**
     * See {@link UserSecurityData#userId}
     */    
	public String getUserId() {
		return this.userId;
	}
	
    /**
     * See {@link UserSecurityData#userId}
     */    
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
    /**
     * See {@link UserSecurityData#forceChangePassword}
     */    
	public boolean isForceChangePassword() {
		return forceChangePassword;
	}
	
    /**
     * See {@link UserSecurityData#forceChangePassword}
     */    
	public void setForceChangePassword(boolean forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}
	
    /**
     * See {@link UserSecurityData#affiliateName}
     */    
    public String getAffiliateName() {
        return affiliateName;
    }
    
    /**
     * See {@link UserSecurityData#affiliateName}
     */    
    public void setAffiliateName(String affiliateName) {
        this.affiliateName = affiliateName;
    }
    
    /**
     * See {@link UserSecurityData#startPage}
     */    
    public char getStartPage() {
        return startPage;
    }
    
    /**
     * See {@link UserSecurityData#startPage}
     */    
    public void setStartPage(char startPage) {
        this.startPage = startPage;
    }
    
    /**
     * See {@link UserSecurityData#dataUtilityPrivileges}
     */    
    public Set getDataUtilityPrivileges() {
        return dataUtilityPrivileges;
    }
    
    /**
     * See {@link UserSecurityData#dataUtilityPrivileges}
     */    
    public void setDataUtilityPrivileges(Set dataUtilityPrivileges) {
        this.dataUtilityPrivileges = dataUtilityPrivileges;
    }
    
    /**
     * See {@link UserSecurityData#personPk}
     */    
    public Integer getPersonPk() {
        return personPk;
    }
    
    /**
     * See {@link UserSecurityData#personPk}
     */    
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
    }
    
    /**
     * See {@link UserSecurityData#accessibleAffiliates}
     */    
    public Set getAccessibleAffiliates() {
        return accessibleAffiliates;
    }
    
    /**
     * See {@link UserSecurityData#accessibleAffiliates}
     */    
    public void setAccessibleAffiliates(Set accessibleAffiliates) {
        this.accessibleAffiliates = accessibleAffiliates;
    }
    
}
