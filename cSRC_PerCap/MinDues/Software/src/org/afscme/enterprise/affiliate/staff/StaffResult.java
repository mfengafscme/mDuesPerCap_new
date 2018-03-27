package org.afscme.enterprise.affiliate.staff;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.person.PersonResult;

/**
 * Represents an Affiliate Staff (or POC) data.  i.e., Data about a person's 
 * position as a point of contact for an affiliate.
 */
public class StaffResult
{
    public static final int SORT_FIELD_NONE = 0;
    public static final int SORT_FIELD_NAME = 1;
    public static final int SORT_FIELD_TITLE = 2;
    public static final int SORT_FIELD_POC_FOR = 3;
    public static final int SORT_FIELD_LOCATION = 4;
    public static final int SORT_FIELD_STATE = 5;
    public static final int SORT_FIELD_LOCAL = 6;
    public static final int SORT_FIELD_COUNCIL = 7;
    public static final int SORT_FIELD_SUBUNIT = 8;
    public static final int SORT_FIELD_TYPE = 9;
    public static final int SORT_FIELD_ZIP = 10;
    public static final int SORT_FIELD_PHONE = 11;
    public static final int SORT_FIELD_ADDRESS = 12;
    public static final int SORT_FIELD_CITY = 13;
    public static final int SORT_FIELD_ADDR_STATE = 14;
    public static final int SORT_FIELD_SSN = 15;
    public static final int SORT_FIELD_EMAIL = 16;

    protected String fullName;
    protected String title;
    protected String pocFor;
    protected String locationName;
    protected String phoneNumber;
    protected String emailAddress;
    protected Integer personPk;
    protected String email;
    protected AffiliateIdentifier affiliateIdentifier;
    protected Integer affPk;
    
    
    public static final int sortStringToCode(String sortBy) {
        if (sortBy == null)
            return StaffResult.SORT_FIELD_NONE;
        else if (sortBy.equals("name"))
            return StaffResult.SORT_FIELD_NAME;
        else if (sortBy.equals("city"))
            return StaffResult.SORT_FIELD_CITY;
        else if (sortBy.equals("addrState"))
            return StaffResult.SORT_FIELD_ADDR_STATE;
        else if (sortBy.equals("zip"))
            return StaffResult.SORT_FIELD_ZIP;
        else if (sortBy.equals("phone"))
            return StaffResult.SORT_FIELD_PHONE;
        else if (sortBy.equals("addr"))
            return StaffResult.SORT_FIELD_ADDRESS;
        else if (sortBy.equals("pocFor"))
            return StaffResult.SORT_FIELD_POC_FOR;
        else if (sortBy.equals("title"))
            return StaffResult.SORT_FIELD_TITLE;
        else if (sortBy.equals("location"))
            return StaffResult.SORT_FIELD_LOCATION;
        else if (sortBy.equals("type"))
            return StaffResult.SORT_FIELD_TYPE;
        else if (sortBy.equals("subUnit"))
            return StaffResult.SORT_FIELD_SUBUNIT;
        else if (sortBy.equals("state"))
            return StaffResult.SORT_FIELD_STATE;
        else if (sortBy.equals("council"))
            return StaffResult.SORT_FIELD_COUNCIL;
        else if (sortBy.equals("local"))
            return StaffResult.SORT_FIELD_LOCAL;
        else if (sortBy.equals("ssn"))
            return StaffResult.SORT_FIELD_SSN;
        else if (sortBy.equals("email"))
            return StaffResult.SORT_FIELD_EMAIL;
        else
            throw new RuntimeException("Invalid sort field '" + sortBy + "'");
    }
        
        
    /** Getter for property emailAddress.
     * @return Value of property emailAddress.
     *
     */
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }
    
    /** Setter for property emailAddress.
     * @param emailAddress New value of property emailAddress.
     *
     */
    public void setEmailAddress(java.lang.String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    /** Getter for property locationName.
     * @return Value of property locationName.
     *
     */
    public java.lang.String getLocationName() {
        return locationName;
    }
    
    /** Setter for property locationName.
     * @param locationName New value of property locationName.
     *
     */
    public void setLocationName(java.lang.String locationName) {
        this.locationName = locationName;
    }
    
    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     *
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }
    
    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     *
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /** Getter for property pocFor.
     * @return Value of property pocFor.
     *
     */
    public java.lang.String getPocFor() {
        return pocFor;
    }
    
    /** Setter for property pocFor.
     * @param pocFor New value of property pocFor.
     *
     */
    public void setPocFor(java.lang.String pocFor) {
        this.pocFor = pocFor;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     *
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     *
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /** Getter for property fullName.
     * @return Value of property fullName.
     *
     */
    public java.lang.String getFullName() {
        return fullName;
    }
    
    /** Setter for property fullName.
     * @param fullName New value of property fullName.
     *
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property email.
     * @return Value of property email.
     *
     */
    public java.lang.String getEmail() {
        return email;
    }
    
    /** Setter for property email.
     * @param email New value of property email.
     *
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    
    /** Getter for property affiliateIdentifier.
     * @return Value of property affiliateIdentifier.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getAffiliateIdentifier() {
        return affiliateIdentifier;
    }
    
    /** Setter for property affiliateIdentifier.
     * @param affiliateIdentifier New value of property affiliateIdentifier.
     *
     */
    public void setAffiliateIdentifier(org.afscme.enterprise.affiliate.AffiliateIdentifier affiliateIdentifier) {
        this.affiliateIdentifier = affiliateIdentifier;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }
    
}
