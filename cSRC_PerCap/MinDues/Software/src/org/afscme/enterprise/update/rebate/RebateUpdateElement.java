package org.afscme.enterprise.update.rebate;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.update.AddressElement;
import org.afscme.enterprise.update.PersonUpdateElement;

/**
 * Represents the Affiliate/Person/Member Rebate element.
 */
public class RebateUpdateElement extends PersonUpdateElement {
    
    /* Field names used by UI */
    public static final String AFSCME_MEMBER_ID = "AFSCME Member ID";
    public static final String SSN = "Social Security Number";
    public static final String DUP_SSN = "SSN Duplicate Flag";    
    public static final String FIRST_NAME = "First Name";
    public static final String MIDDLE_NAME = "Middle Name";    
    public static final String LAST_NAME = "Last Name";
    public static final String ADDR1 = "Address 1";
    public static final String ADDR2 = "Address 2";
    public static final String CITY = "City";
    public static final String PROVINCE = "Province";    
    public static final String STATE = "State";
    public static final String ZIP = "Zip/Postal Code";
    public static final String ZIP_PLUS = "Zip + 4";    
    public static final String COUNTRY = "Country";
    public static final String MEMBER_TYPE = "Member Type";
    public static final String MEMBER_STATUS = "Member Status";
    public static final String DUES_TYPE = "Dues Type";
    public static final String NUM_MONTHS = "Number of Months";
    public static final String ACCEPTANCE_CODE = "Acceptance Code";
    
    
    /**
     * Affiliate Identifier
     */
    protected AffiliateIdentifier affId = null;
    

    /**
     * Duplicate SSN Flag
     */    
    protected boolean ssnDuplicate;
    
    /**
     * Determined by the MemberType attribute in the update file.
     * 
     * Corresponds to PRB_Roster_Persons.rebate_year_mbr_type
     */
    protected Integer memberTypePk = null;
    
    /**
     * Determined by the MemberStatus attribute in the update file.
     * 
     * Corresponds to PRB_Roster_Persons.rebate_year_mbr_status
     */
    protected Integer memberStatusPk = null;
    
    /**
     * Determined by the DuesType attribute in the update file.
     * 
     * Corresponds to PRB_Roster_Persons.rebate_year_mbr_dues_rate
     */
    protected Integer duesTypePk = null;

    /**
     * Determined by the NumberOfMonths attribute in the update file.
     * 
     * Corresponds to PRB_Roster_Persons.roster_duration_in_aff
     */
    protected Integer numMonthsPk = null;
    
    /**
     * Determined by the AcceptanceCode attribute in the update file.
     * 
     * Corresponds to PRB_Roster_Persons.roster_acceptance_cd
     */
    protected Integer acceptanceCodePk = null;

    
    /**
     * Primary Address
     */
    protected AddressElement primaryAddr = null;
    
    
    // other information needed for actual update when there is a match
    protected Integer affPk = null;
    protected Integer addressPk = null;
    
    
    /** Getter for property acceptanceCodePk.
     * @return Value of property acceptanceCodePk.
     *
     */
    public Integer getAcceptanceCodePk() {
        return acceptanceCodePk;
    }
    
    /** Setter for property acceptanceCodePk.
     * @param acceptanceCodePk New value of property acceptanceCodePk.
     *
     */
    public void setAcceptanceCodePk(Integer acceptanceCodePk) {
        this.acceptanceCodePk = acceptanceCodePk;
    }
    
    /** Getter for property addressPk.
     * @return Value of property addressPk.
     *
     */
    public Integer getAddressPk() {
        return addressPk;
    }
    
    /** Setter for property addressPk.
     * @param addressPk New value of property addressPk.
     *
     */
    public void setAddressPk(Integer addressPk) {
        this.addressPk = addressPk;
    }
    
    /** Getter for property affId.
     * @return Value of property affId.
     *
     */
    public AffiliateIdentifier getAffId() {
        return affId;
    }
    
    /** Setter for property affId.
     * @param affId New value of property affId.
     *
     */
    public void setAffId(AffiliateIdentifier affId) {
        this.affId = affId;
    }

    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property duesTypePk.
     * @return Value of property duesTypePk.
     *
     */
    public java.lang.Integer getDuesTypePk() {
        return duesTypePk;
    }
    
    /** Setter for property duesTypePk.
     * @param duesTypePk New value of property duesTypePk.
     *
     */
    public void setDuesTypePk(java.lang.Integer duesTypePk) {
        this.duesTypePk = duesTypePk;
    }
    
    /** Getter for property memberStatusPk.
     * @return Value of property memberStatusPk.
     *
     */
    public java.lang.Integer getMemberStatusPk() {
        return memberStatusPk;
    }
    
    /** Setter for property memberStatusPk.
     * @param memberStatusPk New value of property memberStatusPk.
     *
     */
    public void setMemberStatusPk(java.lang.Integer memberStatusPk) {
        this.memberStatusPk = memberStatusPk;
    }
    
    /** Getter for property memberTypePk.
     * @return Value of property memberTypePk.
     *
     */
    public java.lang.Integer getMemberTypePk() {
        return memberTypePk;
    }
    
    /** Setter for property memberTypePk.
     * @param memberTypePk New value of property memberTypePk.
     *
     */
    public void setMemberTypePk(java.lang.Integer memberTypePk) {
        this.memberTypePk = memberTypePk;
    }
    
    /** Getter for property numMonthsPk.
     * @return Value of property numMonthsPk.
     *
     */
    public java.lang.Integer getNumMonthsPk() {
        return numMonthsPk;
    }
    
    /** Setter for property numMonthsPk.
     * @param numMonthsPk New value of property numMonthsPk.
     *
     */
    public void setNumMonthsPk(java.lang.Integer numMonthsPk) {
        this.numMonthsPk = numMonthsPk;
    }
    
    /** Getter for property primaryAddr.
     * @return Value of property primaryAddr.
     *
     */
    public AddressElement getPrimaryAddr() {
        return primaryAddr;
    }
    
    /** Setter for property primaryAddr.
     * @param primaryAddr New value of property primaryAddr.
     *
     */
    public void setPrimaryAddr(AddressElement primaryAddr) {
        this.primaryAddr = primaryAddr;
    }
    
    /** Getter for property ssnDuplicate.
     * @return Value of property ssnDuplicate.
     *
     */
    public boolean getSsnDuplicate() {
        return ssnDuplicate;
    }
    
    /** Getter for property ssnDuplicate.
     * @return Value of property ssnDuplicate.
     *
     */
    public boolean isSsnDuplicate() {
        return ssnDuplicate;
    }
    
    /** Setter for property ssnDuplicate.
     * @param ssnDuplicate New value of property ssnDuplicate.
     *
     */
    public void setSsnDuplicate(boolean ssnDuplicate) {
        this.ssnDuplicate = ssnDuplicate;
    }
    
}
