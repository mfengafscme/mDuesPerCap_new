package org.afscme.enterprise.person;

import java.util.Collection;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.util.TextUtil;

/**
 * Contains data required to create a new person
 */
public class NewPerson 
{
    private static Integer EMAIL_TYPE_PRIMARY = new Integer(71001);
    private static Integer EMAIL_TYPE_ALTERNATE = new Integer(71002);
    
    private Integer prefixNm;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private Integer suffixNm;
    private String nickNm;
    private String altMailingNm;
    private String ssn;
    private Boolean ssnValid;
    private Boolean ssnDuplicate;

    private String personEmailAddrPrimary;
    private String personEmailAddrAlternate;
    private Collection theEmailData;

    private CommentData theCommentData;

    private PhoneData thePhoneData;
    private PersonAddress thePersonAddress;
    // This flag is the subtype discriminator for the parent Person and 
    //indicates whether the row represents the Member subtype of all 
    //other subtype.
    private Boolean memberFg;
    
    public NewPerson () {
        memberFg = new Boolean(false);
        ssnValid = new Boolean(true);
    }

    /**
     * Creates a new NewPerson object with data initialized from a PersonData object
     */
    public NewPerson (PersonData data) {
        this();
        altMailingNm = data.getAltMailingNm();
        firstNm = data.getFirstNm();
        middleNm = data.getMiddleNm();
        lastNm = data.getLastNm();
        prefixNm = data.getPrefixNm();
        suffixNm = data.getSuffixNm();
        ssn = data.getSsn();
        ssnValid = data.getSsnValid();
        suffixNm = data.getSuffixNm();
        theCommentData = data.getTheCommentData();
        theEmailData = data.getTheEmailData();
        //thePhoneData = data.getThePhoneData();
    }
        
    /** Getter for property altMailingNm.
     * @return Value of property altMailingNm.
     *
     */
    public java.lang.String getAltMailingNm() {
        return altMailingNm;
    }    
    
    /** Setter for property altMailingNm.
     * @param altMailingNm New value of property altMailingNm.
     *
     */
    public void setAltMailingNm(java.lang.String altMailingNm) {
        this.altMailingNm = altMailingNm;
    }
    
    /** Getter for property firstNm.
     * @return Value of property firstNm.
     *
     */
    public java.lang.String getFirstNm() {
        return firstNm;
    }
    
    /** Setter for property firstNm.
     * @param firstNm New value of property firstNm.
     *
     */
    public void setFirstNm(java.lang.String firstNm) {
        this.firstNm = firstNm;
    }
    
    /** Getter for property lastNm.
     * @return Value of property lastNm.
     *
     */
    public java.lang.String getLastNm() {
        return lastNm;
    }
    
    /** Setter for property lastNm.
     * @param lastNm New value of property lastNm.
     *
     */
    public void setLastNm(java.lang.String lastNm) {
        this.lastNm = lastNm;
    }
    
    /** Getter for property middleNm.
     * @return Value of property middleNm.
     *
     */
    public java.lang.String getMiddleNm() {
        return middleNm;
    }
    
    /** Setter for property middleNm.
     * @param middleNm New value of property middleNm.
     *
     */
    public void setMiddleNm(java.lang.String middleNm) {
        this.middleNm = middleNm;
    }
    
    /** Getter for property nickNm.
     * @return Value of property nickNm.
     *
     */
    public java.lang.String getNickNm() {
        return nickNm;
    }
    
    /** Setter for property nickNm.
     * @param nickNm New value of property nickNm.
     *
     */
    public void setNickNm(java.lang.String nickNm) {
        this.nickNm = nickNm;
    }
    
    /** Getter for property prefixNm.
     * @return Value of property prefixNm.
     *
     */
    public java.lang.Integer getPrefixNm() {
        return prefixNm;
    }
    
    /** Setter for property prefixNm.
     * @param prefixNm New value of property prefixNm.
     *
     */
    public void setPrefixNm(java.lang.Integer prefixNm) {
        this.prefixNm = prefixNm;
    }
    
    /** Getter for property ssn.
     * @return Value of property ssn.
     *
     */
    public java.lang.String getSsn() {
        return ssn;
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     *
     */
    public void setSsn(java.lang.String ssn) {
        this.ssn = ssn;
    }
    
    /** Getter for property ssnValid.
     * @return Value of property ssnValid.
     *
     */
    public java.lang.Boolean getSsnValid() {
        return ssnValid;
    }
    
    /** Setter for property ssnValid.
     * @param ssnValid New value of property ssnValid.
     *
     */
    public void setSsnValid(java.lang.Boolean ssnValid) {
        this.ssnValid = ssnValid;
    }
    
    /** Getter for property suffixNm.
     * @return Value of property suffixNm.
     *
     */
    public java.lang.Integer getSuffixNm() {
        return suffixNm;
    }
    
    /** Setter for property suffixNm.
     * @param suffixNm New value of property suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer suffixNm) {
        this.suffixNm = suffixNm;
    }
    
    /** Getter for property theCommentData.
     * @return Value of property theCommentData.
     *
     */
    public org.afscme.enterprise.common.CommentData getTheCommentData() {
        return theCommentData;
    }
    
    /** Setter for property theCommentData.
     * @param theCommentData New value of property theCommentData.
     *
     */
    public void setTheCommentData(org.afscme.enterprise.common.CommentData theCommentData) {
        this.theCommentData = theCommentData;
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
    
    /** Getter for property thePersonAddress.
     * @return Value of property thePersonAddress.
     *
     */
    public org.afscme.enterprise.address.PersonAddress getThePersonAddress() {
        return thePersonAddress;
    }
    
    /** Setter for property thePersonAddress.
     * @param thePersonAddress New value of property thePersonAddress.
     *
     */
    public void setThePersonAddress(org.afscme.enterprise.address.PersonAddress thePersonAddress) {
        this.thePersonAddress = thePersonAddress;
    }
    
    /** Getter for property thePhoneData.
     * @return Value of property thePhoneData.
     *
     */
    public org.afscme.enterprise.common.PhoneData getThePhoneData() {
        if (thePhoneData==null) return null;
        
        if(thePhoneData.getPhoneType()==null ||
        TextUtil.isEmpty(thePhoneData.getPhoneNumber())) return null;

        return thePhoneData;
    }
    
    /** Setter for property thePhoneData.
     * @param thePhoneData New value of property thePhoneData.
     *
     */
    public void setThePhoneData(org.afscme.enterprise.common.PhoneData thePhoneData) {
        this.thePhoneData = thePhoneData;
    }
    
    /** Getter for property memberFg.
     * @return Value of property memberFg.
     *
     */
    public java.lang.Boolean getMemberFg() {
        return memberFg;
    }
    
    /** Setter for property memberFg.
     * @param memberFg New value of property memberFg.
     *
     */
    public void setMemberFg(java.lang.Boolean memberFg) {
        this.memberFg = memberFg;
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
    
    /** Getter for property ssnDuplicate.
     * @return Value of property ssnDuplicate.
     *
     */
    public java.lang.Boolean getSsnDuplicate() {
        return ssnDuplicate;
    }
    
    /** Setter for property ssnDuplicate.
     * @param ssnDuplicate New value of property ssnDuplicate.
     *
     */
    public void setSsnDuplicate(java.lang.Boolean ssnDuplicate) {
        this.ssnDuplicate = ssnDuplicate;
    }
    
}
