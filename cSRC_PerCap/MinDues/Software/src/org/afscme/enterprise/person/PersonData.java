package org.afscme.enterprise.person;

import java.util.Collection;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.PhoneData;

/**
 * Data about an individual person.
 */
public class PersonData 
{
    private Integer personPk;
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
    private Boolean markedForDeletionFg;
    private Integer addressPk;
    private Integer phonePk;
    private Collection theEmailData;
    private CommentData theCommentData;
    private Collection thePhoneData;
    
    public PersonData() {
        ssnValid = new Boolean(false);
        ssnDuplicate = new Boolean(false);
        markedForDeletionFg = new Boolean(false);
    }
        
    /** toString method to view all attributes.
     * @return Value of all properties.
     */
    public String toString() {
        return "PersonData[" +
        "personPk="+personPk+", "+
        "prefixNm="+prefixNm+", "+
        "firstNm="+firstNm+", "+
        "middleNm="+middleNm+", "+
        "lastNm="+lastNm+", "+
        "suffixNm="+suffixNm+", "+
        "nickNm="+nickNm+", "+
        "altMailingNm="+altMailingNm+", "+
        "ssn="+ssn+", "+
        "ssnValid="+ssnValid+", "+
        "ssnDuplicate="+ssnDuplicate+", "+
        "markedForDeletionFg="+markedForDeletionFg+", "+
        "addressPk="+addressPk+", "+
        "phonePk="+phonePk+", "+
        "theEmailData="+theEmailData+", "+
        "theCommentData="+theCommentData+", "+
        "thePhoneData="+thePhoneData+"]";
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
    
    /** Getter for property markedForDeletionFg.
     * @return Value of property markedForDeletionFg.
     *
     */
    public java.lang.Boolean getMarkedForDeletionFg() {
        return markedForDeletionFg;
    }
    
    /** Setter for property markedForDeletionFg.
     * @param markedForDeletionFg New value of property markedForDeletionFg.
     *
     */
    public void setMarkedForDeletionFg(java.lang.Boolean markedForDeletionFg) {
        this.markedForDeletionFg = markedForDeletionFg;
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
    
    /** Getter for property phonePk.
     * @return Value of property phonePk.
     *
     */
    public java.lang.Integer getPhonePk() {
        return phonePk;
    }
    
    /** Setter for property phonePk.
     * @param phonePk New value of property phonePk.
     *
     */
    public void setPhonePk(java.lang.Integer phonePk) {
        this.phonePk = phonePk;
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
    public Collection getTheEmailData() {
        return theEmailData;
    }
    
    /** Setter for property theEmailData.
     * @param theEmailData New value of property theEmailData.
     *
     */
    public void setTheEmailData(Collection theEmailData) {
        this.theEmailData = theEmailData;
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
    
}
