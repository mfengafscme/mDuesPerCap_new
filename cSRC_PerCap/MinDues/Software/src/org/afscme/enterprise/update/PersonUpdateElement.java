package org.afscme.enterprise.update;

import org.afscme.enterprise.update.AddressElement;

/**
 * Represents the data in a Member element in the update file
 */
public class PersonUpdateElement {
    
    /**
     * Determinted by the AffilaiteMbrNum attribute in the update XML document.
     * 
     * Stored to Affiliate_Member.mbr_no_local
     */
    protected String affiliateMemberNumber;
    
    /**
     * Determinted by the AfscmeMbrNum attribute in the update XML document.
     * 
     * Corresponds to Person.person_pk
     */
    protected Integer afscmeMemberNumber;
    
    /**
     * Determinted by the FirstName attribute in the update XML document.
     * 
     * Stored to Person.first_nm
     */
    protected String firstName;
    
    /**
     * Determinted by the LastName attribute in the update XML document.
     * 
     * Stored to Person.last_nm
     */
    protected String lastName;
    
    /**
     * Determinted by the MiddleName attribute in the update XML document.
     * 
     * Stored to Person.middle_nm
     */
    protected String middleName;
    
    /**
     * Determinted by the Prefix attribute in the update XML document.
     * 
     * Stored to Person.prefix_nm
     */
    protected String prefix;
    
    /**
     * Determinted by the Suffix attribute in the update XML document.
     * 
     * Stored to Person.suffix_nm
     */
    protected String suffix;
    
    /**
     * Determinted by the SSN attribute in the update XML document.
     * 
     * Stored to Person.ssn
     */
    protected String ssn;
    
    /**
     * Determinted by the Status attribute in the update XML document.
     * 
     * Use varies for Member/Officer/Rebate
     */
    protected Integer status;
    protected AddressElement addressElement;
    
    /** Getter for property addressElement.
     * @return Value of property addressElement.
     *
     */
    public AddressElement getAddressElement() {
        return addressElement;
    }
    
    /** Setter for property addressElement.
     * @param addressElement New value of property addressElement.
     *
     */
    public void setAddressElement(AddressElement addressElement) {
        this.addressElement = addressElement;
    }
    
    /** Getter for property affiliateMemberNumber.
     * @return Value of property affiliateMemberNumber.
     *
     */
    public String getAffiliateMemberNumber() {
        return affiliateMemberNumber;
    }
    
    /** Setter for property affiliateMemberNumber.
     * @param affiliateMemberNumber New value of property affiliateMemberNumber.
     *
     */
    public void setAffiliateMemberNumber(String affiliateMemberNumber) {
        this.affiliateMemberNumber = affiliateMemberNumber;
    }
    
    /** Getter for property afscmeMemberNumber.
     * @return Value of property afscmeMemberNumber.
     *
     */
    public Integer getAfscmeMemberNumber() {
        return afscmeMemberNumber;
    }
    
    /** Setter for property afscmeMemberNumber.
     * @param afscmeMemberNumber New value of property afscmeMemberNumber.
     *
     */
    public void setAfscmeMemberNumber(Integer afscmeMemberNumber) {
        this.afscmeMemberNumber = afscmeMemberNumber;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     *
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     *
     */
    public String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     *
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    /** Getter for property prefix.
     * @return Value of property prefix.
     *
     */
    public String getPrefix() {
        return prefix;
    }
    
    /** Setter for property prefix.
     * @param prefix New value of property prefix.
     *
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    /** Getter for property ssn.
     * @return Value of property ssn.
     *
     */
    public String getSsn() {
        return ssn;
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     *
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public Integer getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /** Getter for property suffix.
     * @return Value of property suffix.
     *
     */
    public String getSuffix() {
        return suffix;
    }
    
    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     *
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
}
