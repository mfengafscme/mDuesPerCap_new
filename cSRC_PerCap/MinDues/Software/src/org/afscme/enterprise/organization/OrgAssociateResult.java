package org.afscme.enterprise.organization;


/**
 * Represents a row in the Organization Associate Result set. Closely associated
 * with the Organization Associate Maintenance page data in the User Interface
 */
public class OrgAssociateResult
{

    public static final int SORT_FIELD_NONE = 0;
    public static final int SORT_FIELD_NAME = 1;
    public static final int SORT_FIELD_TITLE = 2;
    public static final int SORT_FIELD_LOCATION = 3;
    public static final int SORT_FIELD_PHONE = 4;
    public static final int SORT_FIELD_EMAIL = 5;
    
    private Integer orgPk;
    private Integer personPk;
    
    /**
     * Concatenation of last & first names - see UI description document for full
     * details
     */
    private String name;
    private String orgTitle;
    private String locationName;

    /**
     * Concatenation of location phone numbers. See UI description document for details
     */
    private String phoneNumber;

    /**
     * Email address
     */
    private String emailAddress;

    
    /** toString method 
     */    
    public String toString() {
        return "OrgAssociateResult["+
        "orgPk="+orgPk+","+
        "personPk="+personPk+","+
        "name="+name+","+        
        "orgTitle="+orgTitle+","+
        "locationName="+locationName+","+
        "phoneNumber="+phoneNumber+","+
        "emailAddress="+emailAddress+"]";
    }    
    
    /** Getter for property orgPk.
     * @return Value of property orgPk.
     *
     */
    public java.lang.Integer getOrgPk() {
        return orgPk;
    }
    
    /** Setter for property orgPk.
     * @param orgPk New value of property orgPk.
     *
     */
    public void setOrgPk(java.lang.Integer orgPk) {
        this.orgPk = orgPk;
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
    
    /** Getter for property orgTitle.
     * @return Value of property orgTitle.
     *
     */
    public java.lang.String getOrgTitle() {
        return orgTitle;
    }
    
    /** Setter for property orgTitle.
     * @param orgTitle New value of property orgTitle.
     *
     */
    public void setOrgTitle(java.lang.String orgTitle) {
        this.orgTitle = orgTitle;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
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
}
