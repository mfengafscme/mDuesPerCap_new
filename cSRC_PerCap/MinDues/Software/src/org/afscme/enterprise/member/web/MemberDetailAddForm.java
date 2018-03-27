package org.afscme.enterprise.member.web;

import java.lang.Integer;
import java.sql.Timestamp;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.address.ejb.SystemAddressBean;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.codes.Codes.PersonAddressType;
import org.afscme.enterprise.codes.Codes.Country;
import org.afscme.enterprise.codes.Codes.MemberType;
import org.afscme.enterprise.codes.Codes.AffiliateType;
import org.afscme.enterprise.codes.Codes.EmailType;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.member.NewMember;
import org.apache.log4j.Logger;

/* This form supports adding a new Member, whether the member is already a person or not
 * In other words, this form supports both Member Detail - Add and Member Detail - Add Affiliation
 * from the different paths.
 *
 */

/**
 * Represents the form when the user is adding a new member
 *
 * @struts:form name="memberDetailAddForm"
 */
public class MemberDetailAddForm extends org.apache.struts.action.ActionForm {
    
    private static Logger logger =  Logger.getLogger(MemberDetailAddForm.class);
    
    // to support cancel processing from the MemberDetailAdd page
    private String back;
    private boolean searched;
    
    private static Integer EMAIL_TYPE_PRIMARY = EmailType.PRIMARY;
    private static Integer EMAIL_TYPE_ALTERNATE = EmailType.ALTERNATE;
    
    private Integer m_personPk;
    private Integer m_prefixNm;
    private String m_firstNm;
    private String m_middleNm;
    private String m_lastNm;
    private Integer m_suffixNm;
    private String m_nickNm;
    private String m_altMailingNm;
    private Boolean m_ssnValid;
    private Boolean m_ssnDuplicate;
    
    private String m_ssn;
    // Social Security Number - Area Number - first 3 digits
    private String m_ssn1;
    // Social Security Number - Group Number - middle 2 digits
    private String m_ssn2;
    // Social Security Number - Serial Number - last 4 digits
    private String m_ssn3;
    
    // member related data
    private Integer m_mbrType;
    private Integer m_mbrStatus;
    private Integer m_monthJoined;
    private String m_yearJoined;
    private Timestamp m_mbrJoinDt;
    
    private Integer affPk;
    private AffiliateIdentifier theAffiliateIdentifier;
    
    // address related data
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String zipCode;
    private String zipPlus;
    private String county;
    private String province;
    private Integer countryPk;
    private Integer type;
    private boolean primary;
    private boolean bad;
    private Timestamp badDate;
    private RecordData recordData;
    
    private Integer addressPk; // for Member Detail Add version 2 (Add Affiliation)
    private PersonAddressRecord personAddressRecord;
    
    // phone data
    private Integer phoneType;
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private Boolean phonePrmryFg;
    private Integer dept;
    
    // phone collection of Member Detail Add version 2 (Add Affiliaiton)
    private Collection thePhoneData;
    
    // email data
    private String personEmailAddrPrimary;
    private String personEmailAddrAlternate;
    
    // email collection data for Member Detail Add version 2 (Add Affiliation)
    private Collection theEmailData;
    
    private String m_comment;
    
    private Set vduAffiliates;
    
    
    /**
     * Collection of EmailData objects.
     */
    private Collection m_emailData;
    
    /**
     * PhoneData object.
     */
    private PhoneData m_phoneData;
    
    /**
     * SMA PersonAddressRecord object
     */
    private PersonAddress m_personAddress;
    
    /**
     * Action to use for the return button back to the last screen. Must include
     * parameters needed to return to previous screen.
     */
    private String returnAction;
    
    
    /** Creates a new instance of PersonDetailAddForm */
    public MemberDetailAddForm() {
        m_comment = "";
        primary = true;
        phonePrmryFg = new Boolean(true);
        personEmailAddrPrimary = "";
        personEmailAddrAlternate = "";
        m_personAddress = new PersonAddress();
        m_phoneData = new PhoneData();
        m_ssn1 = null;
        m_ssn2 = null;
        m_ssn3 = null;
        affPk = null;
        searched = false;
        m_personPk = null;
        theAffiliateIdentifier = new AffiliateIdentifier(null, null, null, null, null);
        vduAffiliates = null;
        
    }
    
    // Struts Methods...
    
    public ActionErrors validateForAddAffiliation(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        logger.debug("ValidateForAddAffiliation - affPk is: " + this.affPk + " getAffPk " + this.getAffPk());
        logger.debug("ValidateForAddAffiliation - personPk is: " + this.m_personPk + " getPersonPk " + this.getPersonPk());
        
        //Check Required fields
        if ((TextUtil.isEmpty(this.getAffPk()) || (this.getAffPk().intValue() == 0)) & TextUtil.isEmpty(this.getTheAffiliateIdentifier().getType()) ) {  //sometimes gets passed in as 0 from html, intention is null
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.addmember.mustSelectAnAffiliate"));
        }
        
        if (TextUtil.isEmptyOrSpaces(this.getYearJoined())) {
            errors.add("yearJoined", new ActionError("error.field.required.generic", "Year Joined"));
        }
        
        // HLM: Fix defect #159
        // perform validation to ensure the correct member type has been selected
        if (!isValidMemberType()) {
            errors.add("mbrType", new ActionError("error.addmember.mustSelectCorrectMemberType", theAffiliateIdentifier.getType()));
        }
        
        logger.debug("**MemberDetailAddForm.validateForAddAffiliation******** Returning " + errors.size() + " error(s).");
        return errors;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        logger.debug("Validate - affPk is: " + this.affPk + " getAffPk " + this.getAffPk());
        
        //Check Required fields
        if ((TextUtil.isEmpty(this.getAffPk()) || (this.getAffPk().intValue() == 0)) & TextUtil.isEmpty(this.getTheAffiliateIdentifier().getType()) ) {  //sometimes gets passed in as 0 from html, intention is null
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.addmember.mustSelectAnAffiliate"));
        }
        
        if (TextUtil.isEmptyOrSpaces(this.getFirstNm())) {
            errors.add("firstNm", new ActionError("error.field.required.generic", "First Name"));
        } else{
            this.nameMatch(errors, this.getFirstNm(), "firstNm");
        }
/*
        if (TextUtil.isInt(this.getFirstNm())) {
            errors.add("firstNm", new ActionError("error.field.mustHaveOneAlpha.generic", "First Name"));
        }
 */
        if (TextUtil.isEmptyOrSpaces(this.getLastNm())) {
            errors.add("lastNm", new ActionError("error.field.required.generic", "Last Name"));
        }
        
        if (TextUtil.isEmptyOrSpaces(this.getYearJoined())) {
            errors.add("yearJoined", new ActionError("error.field.required.generic", "Year Joined"));
        }
                        
        // ? add validation to check for at least one aff id field entered, or an affpk befoer performing a search                
/*        //if Phone Type has a value, then Phone Number must have a value
        if ((this.getPhoneType()!=null) && this.getPhoneType().intValue() != 0) {
            if (TextUtil.isEmptyOrSpaces(this.getPhoneNumber())) {
                errors.add("phoneNumber", new ActionError("error.field.required.generic", "Phone Number"));
            }
        }
 
       //if Phone Number has a value, then Phone Type must have a value
        if (!TextUtil.isEmptyOrSpaces(this.getPhoneNumber())) {
            if (this.getPhoneType().equals(new Integer(0)) || (this.getPhoneType()==null)) {
                errors.add("phoneType", new ActionError("error.field.required.generic", "Phone Type"));
            }
 */      //If one Phone Number field contains a value, then all phone number fields must contain a value
        boolean phoneTypeTest = false;
        boolean phoneCountryCodeTest = false;
        boolean phoneAreaCodeTest = false;
        boolean phoneNumberTest = false;
        //check if Phone Type has a value
        if ((this.getPhoneType()!=null) && this.getPhoneType().intValue() != 0) {
            phoneTypeTest = true;
        }
        //check if Country Code has a value
        if (!TextUtil.isEmptyOrSpaces(this.getCountryCode())) {
            phoneCountryCodeTest = true;
        }
        //check if Area Code has a value
        if (!TextUtil.isEmptyOrSpaces(this.getAreaCode())) {
            phoneAreaCodeTest = true;
        }
        //check if Phone Number has a value
        if (!TextUtil.isEmptyOrSpaces(this.getPhoneNumber())) {
            phoneNumberTest = true;
        }
        if ((phoneTypeTest && phoneCountryCodeTest && phoneAreaCodeTest && phoneNumberTest) ||
        (!phoneTypeTest && !phoneCountryCodeTest && !phoneAreaCodeTest && !phoneNumberTest)) {
        }else{
            errors.add("phoneType", new ActionError("error.phone.allRequired", "Phone Fields"));
        }
        
        // perform validation to ensure the correct member type has been selected
        if (!isValidMemberType()) {
            errors.add("mbrType", new ActionError("error.addmember.mustSelectCorrectMemberType", theAffiliateIdentifier.getType()));
        }
        
        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }
    
        
    // General Methods...
    
    public boolean isValidMemberType() {       
        // perform validation to ensure the correct member type has been selected
        if (theAffiliateIdentifier.getType() != null && (theAffiliateIdentifier.getType().toString().equals("S") ||
            theAffiliateIdentifier.getType().toString().equals("R")) &&
            ((!m_mbrType.equals(MemberType.S) && !m_mbrType.equals(MemberType.T)))) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("personPk: " + m_personPk);
        buf.append("prefixNm: " + m_prefixNm);
        buf.append(", firstNm: " + m_firstNm);
        buf.append(", middleNm: " + m_middleNm);
        buf.append(", lastNm: " + m_lastNm);
        buf.append(", suffixNm: " + m_suffixNm);
        buf.append(", nickNm: " + m_nickNm);
        buf.append(", altMailingNm: " + m_altMailingNm);
        buf.append(", ssn: " + m_ssn);
        buf.append(", ssn1: " + m_ssn1);
        buf.append(", ssn2: " + m_ssn2);
        buf.append(", ssn3: " + m_ssn3);
        buf.append(", ssnValid: " + m_ssnValid);
        buf.append(", ssnDuplicate: " + m_ssnDuplicate);        
        buf.append(", comment: " + m_comment);        
        buf.append(", emailData: " + m_emailData);        
        buf.append(", mbrType: " + m_mbrType);        
        buf.append(", mbrStatus: " + m_mbrStatus);
        buf.append(", monthJoined: " + m_monthJoined);
        buf.append(", yearJoined: " + m_yearJoined);
        buf.append(", mbrJoinDt: " + m_mbrJoinDt);
        buf.append(", affPk: " + affPk);
        buf.append(", vduAffiliates : " + vduAffiliates);
        // buf.append(", affiliateIdentifier: " + theAffiliateIdentifier);
        
        //       buf.append(", personAddressRecord: " + m_personAddressRecord);        
        return buf.toString()+"]";
    }
    
    // Getter and Setter Methods...
    
    public NewPerson getNewPerson() {
        NewPerson data = new NewPerson();
        data.setPrefixNm(this.getPrefixNm());
        data.setFirstNm(this.getFirstNm());
        data.setMiddleNm(this.getMiddleNm());
        data.setLastNm(this.getLastNm());
        data.setSuffixNm(this.getSuffixNm());
        data.setNickNm(this.getNickNm());
        data.setAltMailingNm(this.getAltMailingNm());
        data.setSsn(this.getSsn());
        data.setSsnValid(this.getSsnValid());
        data.setSsnDuplicate(this.getSsnDuplicate());
        
        data.setThePersonAddress(this.getPersonAddress());
        
        data.setThePhoneData(this.getPhoneData());
        
        data.setTheEmailData(this.getEmailData());
        
        CommentData cdata = new CommentData();
        cdata.setComment(this.getComment());
        data.setTheCommentData(cdata);
        
        return data;
    }
    
    // This method is used when adding a new person as a member. . . to get the data, personPk is set after person is added
    // and is set in the business method
    public NewMember getNewMember() {
        NewPerson npData = getNewPerson();
        NewMember newMember = new NewMember();
        newMember.setTheNewPerson(npData);
        //  newMember.setPersonPk(this.getPersonPk());
        newMember.setMbrType(this.getMbrType());
        newMember.setMbrStatus(this.getMbrStatus());
        newMember.setMbrJoinDt(this.getMbrJoinDt());
        newMember.setAffPk(this.getAffPk());
        newMember.setTheAffiliateIdentifier(this.getTheAffiliateIdentifier());
        
        return newMember;
        
    }
    
    // for existing persons who will have an affiliation added
    public NewMember getDataForAffiliation() {
        
        
        NewMember newMember = new NewMember();
        
        newMember.setPersonPk(this.getPersonPk());
        newMember.setMbrType(this.getMbrType());
        newMember.setMbrStatus(this.getMbrStatus());
        newMember.setMbrJoinDt(this.getMbrJoinDt());
        newMember.setAffPk(this.getAffPk());
        newMember.setTheAffiliateIdentifier(this.getTheAffiliateIdentifier());
        // need to set comment when the person is an existing person, into the NewMember data object
        newMember.setExistingPersonComment(this.getComment());
        
        return newMember;
        
    }
    
    
    // for existing persons
    public void setPersonDetailData(PersonData data) {
        this.setPersonPk(data.getPersonPk());
        logger.debug("MemberDetailAddForm personPk from getPersonPk is : " + this.getPersonPk());
        this.setPrefixNm(data.getPrefixNm());
        this.setFirstNm(data.getFirstNm());
        this.setMiddleNm(data.getMiddleNm());
        this.setLastNm(data.getLastNm());
        this.setSuffixNm(data.getSuffixNm());
        
        this.setNickNm(data.getNickNm());
        this.setAltMailingNm(data.getAltMailingNm());
        
        this.setSsn(data.getSsn());
        if (data.getSsn() != null) {
            this.setSsn1(data.getSsn().substring(0, 3));
            this.setSsn2(data.getSsn().substring(3, 5));
            this.setSsn3(data.getSsn().substring(5, 9));
        } // else leave these fields null
        
        this.setSsnValid(data.getSsnValid());
        this.setSsnDuplicate(data.getSsnDuplicate());
        
        this.setAddressPk(data.getAddressPk());
        
        this.setThePhoneData(data.getThePhoneData());
        
        this.setEmailData(data.getTheEmailData());
        
        this.setComment("");
    }
    
    
    public void setNewPerson(NewPerson data) {
        
        this.setPrefixNm(data.getPrefixNm());
        this.setFirstNm(data.getFirstNm());
        this.setMiddleNm(data.getMiddleNm());
        this.setLastNm(data.getLastNm());
        this.setSuffixNm(data.getSuffixNm());
        
        this.setNickNm(data.getNickNm());
        this.setAltMailingNm(data.getAltMailingNm());
        
        this.setSsn(data.getSsn());
        if (data.getSsn() != null) {
            this.setSsn1(data.getSsn().substring(0, 3));
            this.setSsn2(data.getSsn().substring(3, 5));
            this.setSsn3(data.getSsn().substring(5, 9));
        } // else leave these fields null
        
        this.setSsnValid(data.getSsnValid());
        this.setSsnDuplicate(data.getSsnDuplicate());
        
        this.setPersonAddress(data.getThePersonAddress());
        
        this.setPhoneData(data.getThePhoneData());
        
        this.setEmailData(data.getTheEmailData());
        
        CommentData cdata = new CommentData();
        cdata = data.getTheCommentData();
        this.setComment(cdata.getComment());
    }
    
    /** Getter for property m_altMailingNm.
     * @return Value of property m_altMailingNm.
     *
     */
    public java.lang.String getAltMailingNm() {
        return m_altMailingNm;
    }
    
    /** Setter for property m_altMailingNm.
     * @param m_altMailingNm New value of property m_altMailingNm.
     *
     */
    public void setAltMailingNm(java.lang.String m_altMailingNm) {
        this.m_altMailingNm = m_altMailingNm;
    }
    
    /** Getter for property m_comment.
     * @return Value of property m_comment.
     *
     */
    public java.lang.String getComment() {
        return m_comment;
    }
    
    /** Setter for property m_comment.
     * @param m_comment New value of property m_comment.
     *
     */
    public void setComment(java.lang.String m_comment) {
        this.m_comment = m_comment;
    }
    
    /** Getter for property m_firstNm.
     * @return Value of property m_firstNm.
     *
     */
    public java.lang.String getFirstNm() {
        return m_firstNm;
    }
    
    /** Setter for property m_firstNm.
     * @param m_firstNm New value of property m_firstNm.
     *
     */
    public void setFirstNm(java.lang.String m_firstNm) {
        this.m_firstNm = m_firstNm;
    }
    
    /** Getter for property m_lastNm.
     * @return Value of property m_lastNm.
     *
     */
    public java.lang.String getLastNm() {
        return m_lastNm;
    }
    
    /** Setter for property m_lastNm.
     * @param m_lastNm New value of property m_lastNm.
     *
     */
    public void setLastNm(java.lang.String m_lastNm) {
        this.m_lastNm = m_lastNm;
    }
    
    /** Getter for property m_middleNm.
     * @return Value of property m_middleNm.
     *
     */
    public java.lang.String getMiddleNm() {
        return m_middleNm;
    }
    
    /** Setter for property m_middleNm.
     * @param m_middleNm New value of property m_middleNm.
     *
     */
    public void setMiddleNm(java.lang.String m_middleNm) {
        this.m_middleNm = m_middleNm;
    }
    
    /** Getter for property m_nickNm.
     * @return Value of property m_nickNm.
     *
     */
    public java.lang.String getNickNm() {
        return m_nickNm;
    }
    
    /** Setter for property m_nickNm.
     * @param m_nickNm New value of property m_nickNm.
     *
     */
    public void setNickNm(java.lang.String m_nickNm) {
        this.m_nickNm = m_nickNm;
    }
    
    /** Getter for property m_prefixNm.
     * @return Value of property m_prefixNm.
     *
     */
    public java.lang.Integer getPrefixNm() {
        return m_prefixNm;
    }
    
    /** Setter for property m_prefixNm.
     * @param m_prefixNm New value of property m_prefixNm.
     *
     */
    public void setPrefixNm(java.lang.Integer m_prefixNm) {
        this.m_prefixNm = m_prefixNm;
    }
    
    /** Getter for property m_ssn.
     * @return Value of property m_ssn.
     *
     */
    public java.lang.String getSsn() {
        if (m_ssn1==null || m_ssn2==null || m_ssn3==null) {
            m_ssn = null;
        }else if (TextUtil.isEmptyOrSpaces(m_ssn1) || TextUtil.isEmptyOrSpaces(m_ssn2) || TextUtil.isEmptyOrSpaces(m_ssn3)) {
            m_ssn = null;
        }else m_ssn = m_ssn1 + m_ssn2 + m_ssn3;
        return m_ssn;
    }
    
    /** Setter for property m_ssn.
     * @param m_ssn New value of property m_ssn.
     *
     */
    public void setSsn(java.lang.String m_ssn) {
        if (TextUtil.isEmptyOrSpaces(m_ssn)) {
            this.m_ssn = null;
        } else if (m_ssn==null) {
            this.m_ssn = null;
        } else {
            this.m_ssn = m_ssn;
            this.m_ssn1 = m_ssn.substring(0, 3);
            this.m_ssn2 = m_ssn.substring(3, 5);
            this.m_ssn3 = m_ssn.substring(5, 9);
        }
    }
    
    /** Getter for property m_ssnDuplicate.
     * @return Value of property m_ssnDuplicate.
     *
     */
    public java.lang.Boolean getSsnDuplicate() {
        return m_ssnDuplicate;
    }
    
    /** Setter for property m_ssnDuplicate.
     * @param m_ssnDuplicate New value of property m_ssnDuplicate.
     *
     */
    public void setSsnDuplicate(java.lang.Boolean m_ssnDuplicate) {
        this.m_ssnDuplicate = m_ssnDuplicate;
    }
    
    /** Getter for property m_ssnValid.
     * @return Value of property m_ssnValid.
     *
     */
    public java.lang.Boolean getSsnValid() {
        return m_ssnValid;
    }
    
    /** Setter for property m_ssnValid.
     * @param m_ssnValid New value of property m_ssnValid.
     *
     */
    public void setSsnValid(java.lang.Boolean m_ssnValid) {
        this.m_ssnValid = m_ssnValid;
    }
    
    /** Getter for property m_suffixNm.
     * @return Value of property m_suffixNm.
     *
     */
    public java.lang.Integer getSuffixNm() {
        return m_suffixNm;
    }
    
    /** Setter for property m_suffixNm.
     * @param m_suffixNm New value of property m_suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer m_suffixNm) {
        this.m_suffixNm = m_suffixNm;
    }
    
    /** Getter for property m_personPk.
     * @return Value of property m_personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return m_personPk;
    }
    
    /** Setter for property m_personPk.
     * @param m_personPk New value of property m_personPk.
     *
     */
    public void setPersonPk(java.lang.Integer m_personPk) {
        this.m_personPk = m_personPk;
    }
    
    /** Getter for property m_ssn1.
     * @return Value of property m_ssn1.
     *
     */
    public java.lang.String getSsn1() {
        return m_ssn1;
    }
    
    /** Setter for property m_ssn1.
     * @param m_ssn1 New value of property m_ssn1.
     *
     */
    public void setSsn1(java.lang.String ssn1) {
        if (ssn1 == "") m_ssn1 = null;
        else this.m_ssn1 = ssn1;
    }
    
    /** Getter for property m_ssn2.
     * @return Value of property m_ssn2.
     *
     */
    public java.lang.String getSsn2() {
        return m_ssn2;
    }
    
    /** Setter for property m_ssn2.
     * @param m_ssn2 New value of property m_ssn2.
     *
     */
    public void setSsn2(java.lang.String ssn2) {
        if (ssn2 == "") m_ssn2 = null;
        else this.m_ssn2 = ssn2;
    }
    
    /** Getter for property m_ssn3.
     * @return Value of property m_ssn3.
     *
     */
    public java.lang.String getSsn3() {
        return m_ssn3;
    }
    
    /** Setter for property m_ssn3.
     * @param m_ssn3 New value of property m_ssn3.
     *
     */
    public void setSsn3(java.lang.String ssn3) {
        if (ssn3 == "") m_ssn3 = null;
        else this.m_ssn3 = ssn3;
    }
    
    /** Getter for property emailData.
     * @return Value of property emailData.
     *
     */
    public java.util.Collection getEmailData() {
        EmailData data = new EmailData();
        Collection m_emailData = new ArrayList();
        
        data.setEmailType(EMAIL_TYPE_PRIMARY);
        data.setPersonEmailAddr(getPersonEmailAddrPrimary());
        m_emailData.add(data);
        
        data = new EmailData();
        data.setEmailType(EMAIL_TYPE_ALTERNATE);
        data.setPersonEmailAddr(getPersonEmailAddrAlternate());
        m_emailData.add(data);
        
        return m_emailData;
    }
    
    /** Setter for property emailData.
     * @param emailData New value of property emailData.
     *
     */
    public void setEmailData(java.util.Collection m_emailData) {
        EmailData data = null;
        Iterator it = m_emailData.iterator();
        int count = 0;
        
        while (it.hasNext()){
            data = (EmailData) it.next();
            count++;
            
            if (count == 1) {
                this.personEmailAddrPrimary = data.getPersonEmailAddr();
            } else if (count == 2) {
                this.personEmailAddrAlternate = data.getPersonEmailAddr();
            }
            
        }
    }
    
    /** Getter for property phoneData.
     * @return Value of property phoneData.
     *
     */
    public PhoneData getPhoneData() {
        PhoneData data = new PhoneData();
        data.setPhoneType(this.phoneType);
        data.setCountryCode(this.countryCode);
        data.setAreaCode(this.areaCode);
        data.setPhoneNumber(this.phoneNumber);
        data.setPhonePrmryFg(this.phonePrmryFg);
        data.setDept(this.dept);
        
        return data;
    }
    
    /** Setter for property phoneData.
     * @param phoneData New value of property phoneData.
     *
     */
    public void setPhoneData(PhoneData m_phoneData) {
        this.m_phoneData = m_phoneData;
        
        this.phoneType = m_phoneData.getPhoneType();
        this.countryCode = m_phoneData.getCountryCode();
        this.areaCode = m_phoneData.getAreaCode();
        this.phoneNumber = m_phoneData.getPhoneNumber();
        this.phonePrmryFg = m_phoneData.getPhonePrmryFg();
        this.dept = m_phoneData.getDept();
    }
    
    /** Determines if an Address was populated.
     * @return boolean.
     *
     */
    public boolean hasAddress() {
        if (TextUtil.isEmpty(this.getCity()) &&
            TextUtil.isEmpty(this.getAddr1()) &&
            TextUtil.isEmpty(this.getAddr2()) &&
            TextUtil.isEmpty(this.getCounty()) &&
            TextUtil.isEmpty(this.getProvince()) &&
            TextUtil.isEmpty(this.getZipCode()) &&
            TextUtil.isEmpty(this.getZipPlus()) &&
            TextUtil.isEmpty(this.getState()) ) {
            return false;
        } else return true;
    }
    
    /** Getter for property m_personAddressRecord.
     * @return Value of property m_personAddressRecord.
     *
     */
    public org.afscme.enterprise.address.PersonAddress getPersonAddress() {
        if (TextUtil.isEmpty(this.getCity()) &&
            TextUtil.isEmpty(this.getAddr1()) &&
            TextUtil.isEmpty(this.getAddr2()) &&
            TextUtil.isEmpty(this.getCounty()) &&
            TextUtil.isEmpty(this.getProvince()) &&
            TextUtil.isEmpty(this.getZipCode()) &&
            TextUtil.isEmpty(this.getZipPlus()) &&
            TextUtil.isEmpty(this.getState())) {
            logger.debug("MemberDetailAddForm:getPersonAddress: personAddressData=null");
            return null;
        }
        m_personAddress.setAddr1(this.getAddr1());
        m_personAddress.setAddr2(this.getAddr2());
        m_personAddress.setCity(this.getCity());
        m_personAddress.setState(this.getState());
        m_personAddress.setZipCode(this.getZipCode());
        m_personAddress.setZipPlus(this.getZipPlus());
        m_personAddress.setCounty(this.getCounty());
        m_personAddress.setProvince(this.getProvince());
        m_personAddress.setCountryPk(this.getCountryPk());
        
        m_personAddress.setType(PersonAddressType.HOME);
        m_personAddress.setPrimary(this.isPrimary());
        m_personAddress.setBad(this.isBad());
        
        logger.debug("MemberAddForm:getPersonAddress: personAddressData="+m_personAddress);
        logger.debug("MemberDetailAddForm:getPersonAddress: personAddressData:addr1=|"+this.getAddr1()+"|");
        logger.debug("MemberDetailAddForm:getPersonAddress: personAddressData:city=|"+this.getCity()+"|");
        return m_personAddress;
    }
    
    /** Setter for property m_personAddress.
     * @param m_personAddress New value of property m_personAddress.
     *
     */
    public void setPersonAddress(org.afscme.enterprise.address.PersonAddress m_personAddress) {
        this.setAddr1(m_personAddress.getAddr1());
        this.setAddr2(m_personAddress.getAddr2());
        this.setCity(m_personAddress.getCity());
        this.setState(m_personAddress.getState());
        this.setZipCode(m_personAddress.getZipCode());
        this.setZipPlus(m_personAddress.getZipPlus());
        this.setCounty(m_personAddress.getCounty());
        this.setProvince(m_personAddress.getProvince());
        this.setCountryPk(m_personAddress.getCountryPk());
        
        this.setType(m_personAddress.getType());
        this.setPrimary(m_personAddress.isPrimary());
        this.setBad(m_personAddress.isBad());
        
    }
    
    /** Getter for property addr1.
     * @return Value of property addr1.
     *
     */
    public java.lang.String getAddr1() {
        return addr1;
    }
    
    /** Setter for property addr1.
     * @param addr1 New value of property addr1.
     *
     */
    public void setAddr1(java.lang.String addr1) {
        this.addr1 = addr1;
    }
    
    /** Getter for property addr2.
     * @return Value of property addr2.
     *
     */
    public java.lang.String getAddr2() {
        return addr2;
    }
    
    /** Setter for property addr2.
     * @param addr2 New value of property addr2.
     *
     */
    public void setAddr2(java.lang.String addr2) {
        this.addr2 = addr2;
    }
    
    /** Getter for property areaCode.
     * @return Value of property areaCode.
     *
     */
    public java.lang.String getAreaCode() {
        return areaCode;
    }
    
    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     *
     */
    public void setAreaCode(java.lang.String areaCode) {
        this.areaCode = areaCode;
    }
    
    /** Getter for property bad.
     * @return Value of property bad.
     *
     */
    public boolean isBad() {
        return bad;
    }
    
    /** Setter for property bad.
     * @param bad New value of property bad.
     *
     */
    public void setBad(boolean bad) {
        this.bad = bad;
    }
    
    /** Getter for property badDate.
     * @return Value of property badDate.
     *
     */
    public java.sql.Timestamp getBadDate() {
        return badDate;
    }
    
    /** Setter for property badDate.
     * @param badDate New value of property badDate.
     *
     */
    public void setBadDate(java.sql.Timestamp badDate) {
        this.badDate = badDate;
    }
    
    /** Getter for property city.
     * @return Value of property city.
     *
     */
    public java.lang.String getCity() {
        return city;
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     *
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }
    
    /** Getter for property countryCode.
     * @return Value of property countryCode.
     *
     */
    public java.lang.String getCountryCode() {
        return countryCode;
    }
    
    /** Setter for property countryCode.
     * @param countryCode New value of property countryCode.
     *
     */
    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }
    
    /** Getter for property countryPk.
     * @return Value of property countryPk.
     *
     */
    public java.lang.Integer getCountryPk() {
        return countryPk;
    }
    
    /** Setter for property countryPk.
     * @param countryPk New value of property countryPk.
     *
     */
    public void setCountryPk(java.lang.Integer countryPk) {
        this.countryPk = countryPk;
    }
    
    /** Getter for property county.
     * @return Value of property county.
     *
     */
    public java.lang.String getCounty() {
        return county;
    }
    
    /** Setter for property county.
     * @param county New value of property county.
     *
     */
    public void setCounty(java.lang.String county) {
        this.county = county;
    }
    
    /** Getter for property dept.
     * @return Value of property dept.
     *
     */
    public java.lang.Integer getDept() {
        return dept;
    }
    
    /** Setter for property dept.
     * @param dept New value of property dept.
     *
     */
    public void setDept(java.lang.Integer dept) {
        this.dept = dept;
    }
    
    /** Getter for property personEmailAddrAlternate.
     * @return Value of property personEmailAddrAlternate.
     *
     */
    public java.lang.String getPersonEmailAddrAlternate() {
        return personEmailAddrAlternate;
    }
    
    /** Setter for property personEmailAddrAlternate.
     * @param personEmailAddrAlternate New value of property personEmailAddrAlternate.
     *
     */
    public void setPersonEmailAddrAlternate(java.lang.String personEmailAddrAlternate) {
        this.personEmailAddrAlternate = personEmailAddrAlternate;
    }
    
    /** Getter for property personEmailAddrPrimary.
     * @return Value of property personEmailAddrPrimary.
     *
     */
    public java.lang.String getPersonEmailAddrPrimary() {
        return personEmailAddrPrimary;
    }
    
    /** Setter for property personEmailAddrPrimary.
     * @param personEmailAddrPrimary New value of property personEmailAddrPrimary.
     *
     */
    public void setPersonEmailAddrPrimary(java.lang.String personEmailAddrPrimary) {
        this.personEmailAddrPrimary = personEmailAddrPrimary;
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
    
    /** Getter for property phonePrmryFg.
     * @return Value of property phonePrmryFg.
     *
     */
    public java.lang.Boolean getPhonePrmryFg() {
        return phonePrmryFg;
    }
    
    /** Setter for property phonePrmryFg.
     * @param phonePrmryFg New value of property phonePrmryFg.
     *
     */
    public void setPhonePrmryFg(java.lang.Boolean phonePrmryFg) {
        this.phonePrmryFg = phonePrmryFg;
    }
    
    /** Getter for property phoneType.
     * @return Value of property phoneType.
     *
     */
    public java.lang.Integer getPhoneType() {
        return phoneType;
    }
    
    /** Setter for property phoneType.
     * @param phoneType New value of property phoneType.
     *
     */
    public void setPhoneType(java.lang.Integer phoneType) {
        this.phoneType = phoneType;
    }
    
    /** Getter for property primary.
     * @return Value of property primary.
     *
     */
    public boolean isPrimary() {
        return primary;
    }
    
    /** Setter for property primary.
     * @param primary New value of property primary.
     *
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
    
    /** Getter for property province.
     * @return Value of property province.
     *
     */
    public java.lang.String getProvince() {
        return province;
    }
    
    /** Setter for property province.
     * @param province New value of property province.
     *
     */
    public void setProvince(java.lang.String province) {
        this.province = province;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(org.afscme.enterprise.common.RecordData recordData) {
        this.recordData = recordData;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     *
     */
    public java.lang.String getState() {
        return state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     *
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public java.lang.Integer getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }
    
    /** Getter for property zipCode.
     * @return Value of property zipCode.
     *
     */
    public java.lang.String getZipCode() {
        return zipCode;
    }
    
    /** Setter for property zipCode.
     * @param zipCode New value of property zipCode.
     *
     */
    public void setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
    }
    
    /** Getter for property zipPlus.
     * @return Value of property zipPlus.
     *
     */
    public java.lang.String getZipPlus() {
        return zipPlus;
    }
    
    /** Setter for property zipPlus.
     * @param zipPlus New value of property zipPlus.
     *
     */
    public void setZipPlus(java.lang.String zipPlus) {
        this.zipPlus = zipPlus;
    }
    
    /** Getter for property returnAction.
     * @return Value of property returnAction.
     *
     */
    public String getReturnAction() {
        return returnAction;
    }
    
    /** Setter for property returnAction.
     * @param returnAction New value of property returnAction.
     *
     */
    public void setReturnAction(String returnAction) {
        if (TextUtil.isEmptyOrSpaces(returnAction)) {
            this.returnAction = null;
        } else {
            this.returnAction = returnAction;
        }
    }
    
    /** Getter for property m_mbrType.
     * @return Value of property m_mbrType.
     *
     */
    public java.lang.Integer getMbrType() {
        return m_mbrType;
    }
    
    /** Setter for property m_mbrType.
     * @param m_mbrType New value of property m_mbrType.
     *
     */
    public void setMbrType(java.lang.Integer m_mbrType) {
        this.m_mbrType = m_mbrType;
    }
    
    /** Getter for property m_mbrStatus.
     * @return Value of property m_mbrStatus.
     *
     */
    public java.lang.Integer getMbrStatus() {
        return m_mbrStatus;
    }
    
    /** Setter for property m_mbrStatus.
     * @param m_mbrStatus New value of property m_mbrStatus.
     *
     */
    public void setMbrStatus(java.lang.Integer m_mbrStatus) {
        this.m_mbrStatus = m_mbrStatus;
    }
    
    /** Getter for property m_monthJoined.
     * @return Value of property m_monthJoined.
     *
     */
    public java.lang.Integer getMonthJoined() {
        return m_monthJoined;
    }
    
    /** Setter for property m_monthJoined.
     * @param m_monthJoined New value of property m_monthJoined.
     *
     */
    public void setMonthJoined(java.lang.Integer m_monthJoined) {
        this.m_monthJoined = m_monthJoined;
    }
    
    /** Getter for property m_yearJoined.
     * @return Value of property m_yearJoined.
     *
     */
    public java.lang.String getYearJoined() {
        return m_yearJoined;
    }
    
    /** Setter for property m_yearJoined.
     * @param m_yearJoined New value of property m_yearJoined.
     *
     */
    public void setYearJoined(java.lang.String m_yearJoined) {
        this.m_yearJoined = m_yearJoined;
    }
    
    /** Getter for property m_mbrJoinDt.
     * @return Value of property m_mbrJoinDt.
     *
     */
    public java.sql.Timestamp getMbrJoinDt() {
        if (m_monthJoined != null && !TextUtil.isEmptyOrSpaces(m_yearJoined) ) { // for MemBerDetailEdit
            Timestamp joinDt = DateUtil.getTimestamp(m_monthJoined.intValue(),new Integer(m_yearJoined).intValue(),false);
            logger.debug("MemberDetailAddForm.getMbrJoinDt, values are month: " + m_monthJoined + " year: "
                        + m_yearJoined + " calculated mbrJoinDt: " + joinDt);
            return(joinDt);
        }
        else return this.m_mbrJoinDt; // for MemberDetail
        
    }
    
    /** Setter for property m_mbrJoinDt.
     * @param m_mbrJoinDt New value of property m_mbrJoinDt.
     *
     */
    public void setMbrJoinDt(java.sql.Timestamp m_mbrJoinDt) {
        
        this.m_mbrJoinDt = m_mbrJoinDt;
        
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
        if (affPk.intValue() == 0) this.affPk = null;
        else this.affPk = affPk;
    }
    
    /** Getter for property theAffiliateIdentifier.
     * @return Value of property theAffiliateIdentifier.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getTheAffiliateIdentifier() {
        return theAffiliateIdentifier;
    }
    
    /** Setter for property theAffiliateIdentifier.
     * @param theAffiliateIdentifier New value of property theAffiliateIdentifier.
     *
     */
    public void setTheAffiliateIdentifier(org.afscme.enterprise.affiliate.AffiliateIdentifier theAffiliateIdentifier) {
        this.theAffiliateIdentifier = theAffiliateIdentifier;
    }
    
    /** Getter for property back.
     * @return Value of property back.
     *
     */
    public java.lang.String getBack() {
        return back;
    }
    
    /** Setter for property back.
     * @param back New value of property back.
     *
     */
    public void setBack(java.lang.String back) {
        this.back = back;
    }
    
    /** Getter for property thePhoneData.
     * @return Value of property thePhoneData.
     *
     */
    public java.util.Collection getThePhoneData() {
        return thePhoneData;
    }
    
    /** Setter for property thePhoneData.
     * @param thePhoneData New value of property thePhoneData.
     *
     */
    public void setThePhoneData(java.util.Collection thePhoneData) {
        this.thePhoneData = thePhoneData;
    }
    
    /** Getter for property theEmailData.
     * @return Value of property theEmailData.
     *
     */
    public java.util.Collection getTheEmailData() {
        return theEmailData;
    }
    
    /** Setter for property theEmailData.
     * @param theEmailData New value of property theEmailData.
     *
     */
    public void setTheEmailData(java.util.Collection theEmailData) {
        this.theEmailData = theEmailData;
    }
    
    /** Getter for property addressPk.
     * @return Value of property addressPk.
     *
     */
    public java.lang.Integer getAddressPk() {
        return addressPk;
    }
    
    /** Setter for property addressPk.
     * @param addressPk New value of property addressPk.
     *
     */
    public void setAddressPk(java.lang.Integer addressPk) {
        this.addressPk = addressPk;
    }
    
    /** Getter for property personAddressRecord.
     * @return Value of property personAddressRecord.
     *
     */
    public org.afscme.enterprise.address.PersonAddressRecord getPersonAddressRecord() {
        return personAddressRecord;
    }
    
    /** Setter for property personAddressRecord.
     * @param personAddressRecord New value of property personAddressRecord.
     *
     */
    public void setPersonAddressRecord(org.afscme.enterprise.address.PersonAddressRecord personAddressRecord) {
        this.personAddressRecord = personAddressRecord;
    }
    
    /** Getter for property searched.
     * @return Value of property searched.
     *
     */
    public boolean isSearched() {
        return searched;
    }
    
    /** Setter for property searched.
     * @param searched New value of property searched.
     *
     */
    public void setSearched(boolean searched) {
        this.searched = searched;
    }
    
    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: the name used on jsp
     */
    private void nameMatch(ActionErrors errors, String name, String prop) {
        try {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9]{0,24})", name);
            if (match == false ){
                logger.debug("MemberDetailAddForm:nameMatch -- An error is added.");
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
            }
        } catch (PatternSyntaxException pse) {
            logger.debug("MemberDetailAddForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }
    }
    
    /** Getter for property vduAffiliates.
     * @return Value of property vduAffiliates.
     *
     */
    public java.util.Set getVduAffiliates() {
        return vduAffiliates;
    }
    
    /** Setter for property vduAffiliates.
     * @param vduAffiliates New value of property vduAffiliates.
     *
     */
    public void setVduAffiliates(java.util.Set vduAffiliates) {
        this.vduAffiliates = vduAffiliates;
    }
    
}
