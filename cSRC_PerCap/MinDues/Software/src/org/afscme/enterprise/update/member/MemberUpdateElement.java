package org.afscme.enterprise.update.member;

import java.sql.Timestamp;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.update.AddressElement;

/**
 * Represents the data in a member element in the member update file.
 */
public class MemberUpdateElement {
    
    /* Field names used by UI */
    public static final String PREFIX = "Prefix";   
    public static final String FIRST_NAME = "First Name";
    public static final String MIDDLE_NAME = "Middle Name";    
    public static final String LAST_NAME = "Last Name";
    public static final String SUFFIX = "Suffix";
    public static final String ADDR1 = "Primary Address 1";
    public static final String ADDR2 = "Primary Address 2";
    public static final String CITY = "City";
    public static final String STATE = "State";
    public static final String ZIP = "Zip/Postal Code";
    public static final String MAILABLE_ADDRESS = "Mailable Address Flag";
    public static final String NO_MAIL = "No Mail Flag";
    public static final String PHONE = "PHone Number";
    public static final String STATUS = "Member Status";
    public static final String GENDER = "Gender";
    public static final String DATE_JOINED = "Date Joined";
    public static final String REGISTERED_VOTER = "Registered Voter";
    public static final String POLITICAL_PARTY = "Political Party";
    public static final String SSN = "Social Security Number";
    public static final String INFORMATION_SOURCE = "Primary Information Source";
    public static final String AFFILIATE_MEMBER_ID = "Unique Affiliate Member ID"; 
    
    
    // NOTE, if the input value from File for some code is "U", then its
    // corresponding code pk value is null.
    
    /**
     * Affiliate Identifier
     */
    protected AffiliateIdentifier affId = null;
    
    /*
    public String local;        // Affiliate Number
    public char affType;        // Affiliate Identifier
    public String sub-local;    // Sub-Local Number
    */
    
    /**
     * Full Name -- Last Name
     * 
     * Stored to Person.last_nm
     */
    protected String lastName = null; 
    
    /**
     * Full Name -- First Name
     * 
     * Stored to Person.first_nm
     */
    protected String firstName = null;
    
    /**
     * Full Name -- Middle Name
     * 
     * Stored to Person.middle_nm
     */
    protected String middleName = null;
    
    /**
     * Full Name -- Prefix
     * 
     * Stored to Person.prefix_nm
     */
    protected Integer prefixCodePk = null;
    
    /**
     * Full Name -- Suffix
     * 
     * Stored to Person.suffix_nm
     */
    protected Integer suffixCodePk = null;
    
    /**
     * Primary Address
     */
    protected AddressElement primaryAddr = null;
    
    /**
     * Determined by the MailableAddressFlag attribute in the update XML document.
     *
     * Corresponds to Person_Address.addr_bad_fg.  Note, the logic is reversed here.
     */
    protected boolean mailableAddr;
    
    /**
     * Determinted by the Affiliate/Member@NoMailFlag attribute in the update XML 
     * document.
     * 
     * Corresponds to Aff_Member.no_mail_fg
     */
    protected int noMailFlag;
    
    /**
     * Primary Phone -- Area Code
     */
    protected String areaCode = null;
    
    /**
     * Primary Phone -- Phone Number
     */
    protected String phoneNumber = null;
    
    /** 
     * Maps to Aff_Members.mbr_status and Aff_Members.mbr_type
     */
    protected String status = null; 
    
    /**
     * Pointer to a Gender common code.
     * 
     * Stored to Person_Demographics.gender
     */
    protected Integer genderCodePk = null;
    
    /**
     * Determinted by the Affiliate/Member@DateJoined attribute in the update XML 
     * document.
     * 
     * Stored to Affiliate_Members.mbr_join_dt
     */
    protected Timestamp dateJoined = null;
    
    /**
     * Determinted by the Affiliate/Member@RegisteredVoter attribute in the update XML 
     * document.
     * 
     * Corresponds to Person_Political_Legislative.political_registered_voter
     */
    protected Integer registeredVoterCodePk = null;
    
    /**
     * Pointer to a PoliticalParty common code.
     * 
     * Determinted by the Affiliate/Member@PoliticalParty attribute in the update XML 
     * document.
     * 
     * Stored to Person_Political_Legislative.political_party
     */
    protected Integer politicalPartyCodePk = null;
    
    /**
     * Determinted by the SSN attribute in the update XML document.
     * 
     * Stored to Person.ssn
     */
    protected String ssn = null; 
    
    /**
     * Stored to Aff_Members.primary_information_source
     */
    protected Integer infoSourceCodePk = null;
    
    /**
     * Determinted by the AffilaiteMbrNum attribute in the update XML document.
     * 
     * Stored to Affiliate_Member.mbr_no_local
     */
    protected String affMbrNumber = null;
    
    
    // other information needed for actual update when there is a match
    
    protected Integer affPk = null;
    
    protected Integer personPk = null; 
    
    protected Integer addressPk = null;
    
    protected Integer phonePk = null;
    
    protected Timestamp personMstModifiedDate = null;
    protected Timestamp mbrMstModifiedDate = null;
    
    public String toString() {
        return "MemberUpdateElement[" + 
                    "addressPk=" + addressPk +
                    ", affId=" + affId +
                    ", affMbrNumber=" + affMbrNumber +
                    ", affPk=" + affPk +
                    ", areaCode=" + areaCode +
                    ", dateJoined=" + dateJoined +
                    ", firstName=" + firstName +
                    ", genderCodePk=" + genderCodePk +
                    ", infoSourceCodePk=" + infoSourceCodePk +
                    ", lastName=" + lastName +
                    ", mailableAddr=" + mailableAddr +
                    ", mbrMstModifiedDate=" + mbrMstModifiedDate +
                    ", middleName=" + middleName +
                    ", noMailFlag=" + noMailFlag +
                    ", personMstModifiedDate=" + personMstModifiedDate +
                    ", personPk=" + personPk +                    
                    ", phoneNumber=" + phoneNumber +
                    ", phonePk=" + phonePk +
                    ", politicalPartyCodePk=" + politicalPartyCodePk +
                    ", prefixCodePk=" + prefixCodePk +
                    ", primaryAddr=" + primaryAddr +
                    ", registeredVoterCodePk=" + registeredVoterCodePk +
                    ", ssn=" + ssn +
                    ", status=" + status +
                    ", suffixCodePk=" + suffixCodePk +
                "]"
        ;
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
    
    /** Getter for property prefixCodePk.
     * @return Value of property prefixCodePk.
     *
     */
    public Integer getPrefixCodePk() {
        return prefixCodePk;
    }
    
    /** Setter for property prefixCodePk.
     * @param prefixCodePk New value of property prefixCodePk.
     *
     */
    public void setPrefixCodePk(Integer prefixCodePk) {
        this.prefixCodePk = prefixCodePk;
    }
    
    /** Getter for property suffixCodePk.
     * @return Value of property suffixCodePk.
     *
     */
    public Integer getSuffixCodePk() {
        return suffixCodePk;
    }
    
    /** Setter for property suffixCodePk.
     * @param suffixCodePk New value of property suffixCodePk.
     *
     */
    public void setSuffixCodePk(Integer suffixCodePk) {
        this.suffixCodePk = suffixCodePk;
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
    
    /** Getter for property noMailFlag.
     * @return Value of property noMailFlag.
     *
     */
    public int getNoMailFlag() {
        return noMailFlag;
    }
    
    /** Setter for property noMailFlag.
     * @param noMailFlag New value of property noMailFlag.
     *
     */
    public void setNoMailFlag(int noMailFlag) {
        this.noMailFlag = noMailFlag;
    }
    
    /** Getter for property mailableAddr.
     * @return Value of property mailableAddr.
     *
     */
    public boolean isMailableAddr() {
        return mailableAddr;
    }
    
    /** Setter for property mailableAddr.
     * @param mailableAddr New value of property mailableAddr.
     *
     */
    public void setMailableAddr(boolean mailableAddr) {
        this.mailableAddr = mailableAddr;
    }
    
    /** Getter for property areaCode.
     * @return Value of property areaCode.
     *
     */
    public String getAreaCode() {
        return areaCode;
    }
    
    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     *
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    
    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     *
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     *
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public String getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /** Getter for property genderCodePk.
     * @return Value of property genderCodePk.
     *
     */
    public Integer getGenderCodePk() {
        return genderCodePk;
    }
    
    /** Setter for property genderCodePk.
     * @param genderCodePk New value of property genderCodePk.
     *
     */
    public void setGenderCodePk(Integer genderCodePk) {
        this.genderCodePk = genderCodePk;
    }
    
    /** Getter for property dateJoined.
     * @return Value of property dateJoined.
     *
     */
    public Timestamp getDateJoined() {
        return dateJoined;
    }
    
    /** Setter for property dateJoined.
     * @param dateJoined New value of property dateJoined.
     *
     */
    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }
    
    /** Getter for property registeredVoterCodePk.
     * @return Value of property registeredVoterCodePk.
     *
     */
    public Integer getRegisteredVoterCodePk() {
        return registeredVoterCodePk;
    }
    
    /** Setter for property registeredVoterCodePk.
     * @param registeredVoterCodePk New value of property registeredVoterCodePk.
     *
     */
    public void setRegisteredVoterCodePk(Integer registeredVoterCodePk) {
        this.registeredVoterCodePk = registeredVoterCodePk;
    }
    
    /** Getter for property politicalPartyCodePk.
     * @return Value of property politicalPartyCodePk.
     *
     */
    public Integer getPoliticalPartyCodePk() {
        return politicalPartyCodePk;
    }
    
    /** Setter for property politicalPartyCodePk.
     * @param politicalPartyCodePk New value of property politicalPartyCodePk.
     *
     */
    public void setPoliticalPartyCodePk(Integer politicalPartyCodePk) {
        this.politicalPartyCodePk = politicalPartyCodePk;
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
    
    /** Getter for property infoSourceCodePk.
     * @return Value of property infoSourceCodePk.
     *
     */
    public Integer getInfoSourceCodePk() {
        return infoSourceCodePk;
    }
    
    /** Setter for property infoSourceCodePk.
     * @param infoSourceCodePk New value of property infoSourceCodePk.
     *
     */
    public void setInfoSourceCodePk(Integer infoSourceCodePk) {
        this.infoSourceCodePk = infoSourceCodePk;
    }
    
    /** Getter for property affMbrNumber.
     * @return Value of property affMbrNumber.
     *
     */
    public String getAffMbrNumber() {
        return affMbrNumber;
    }
    
    /** Setter for property affMbrNumber.
     * @param affMbrNumber New value of property affMbrNumber.
     *
     */
    public void setAffMbrNumber(String affMbrNumber) {
        this.affMbrNumber = affMbrNumber;
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
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
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
    
    /** Getter for property phonePk.
     * @return Value of property phonePk.
     *
     */
    public Integer getPhonePk() {
        return phonePk;
    }
    
    /** Setter for property phonePk.
     * @param phonePk New value of property phonePk.
     *
     */
    public void setPhonePk(Integer phonePk) {
        this.phonePk = phonePk;
    }

    /** Getter for property personMstModifiedDate.
     * @return Value of property personMstModifiedDate.
     *
     */
    public Timestamp getPersonMstModifiedDate() {
        return personMstModifiedDate;
    }
    
    /** Setter for property personMstModifiedDate.
     * @param personMstModifiedDate New value of property personMstModifiedDate.
     *
     */
    public void setPersonMstModifiedDate(Timestamp personMstModifiedDate) {
        this.personMstModifiedDate = personMstModifiedDate;
    }
    
    /** Getter for property mbrMstModifiedDate.
     * @return Value of property mbrMstModifiedDate.
     *
     */
    public Timestamp getMbrMstModifiedDate() {
        return mbrMstModifiedDate;
    }
    
    /** Setter for property mbrMstModifiedDate.
     * @param mbrMstModifiedDate New value of property mbrMstModifiedDate.
     *
     */
    public void setMbrMstModifiedDate(Timestamp mbrMstModifiedDate) {
        this.mbrMstModifiedDate = mbrMstModifiedDate;
    }
   
}
