package org.afscme.enterprise.returnedmail;

import java.sql.Timestamp;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.common.RecordData;

public class ReturnedPersonAddress extends PersonAddress {         
    private Integer addressPK;
    private Integer personPK;
    private String prefixNm;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String suffixNm;    
    private boolean mbrFlag;
    private Timestamp expelledDate;
    private boolean smaFlag;        
    private String exceptionReason;        
    private RecordData theRecordData;
    
    /** Getter for property exceptionReason.
     * @return Value of property exceptionReason.
     *
     */
    public java.lang.String getExceptionReason() {
        return exceptionReason;
    }
    
    /** Setter for property exceptionReason.
     * @param exceptionReason New value of property exceptionReason.
     *
     */
    public void setExceptionReason(java.lang.String exceptionReason) {
        this.exceptionReason = exceptionReason;
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
    
    /** Getter for property personPK.
     * @return Value of property personPK.
     *
     */
    public java.lang.Integer getPersonPK() {
        return personPK;
    }
    
    /** Setter for property personPK.
     * @param personPK New value of property personPK.
     *
     */
    public void setPersonPK(java.lang.Integer personPK) {
        this.personPK = personPK;
    }
    
    /** Getter for property prefixNm.
     * @return Value of property prefixNm.
     *
     */
    public java.lang.String getPrefixNm() {
        return prefixNm;
    }
    
    /** Setter for property prefixNm.
     * @param prefixNm New value of property prefixNm.
     *
     */
    public void setPrefixNm(java.lang.String prefixNm) {
        this.prefixNm = prefixNm;
    }
    
    /** Getter for property smaFlag.
     * @return Value of property smaFlag.
     *
     */
    public boolean isSmaFlag() {
        return smaFlag;
    }
    
    /** Setter for property smaFlag.
     * @param smaFlag New value of property smaFlag.
     *
     */
    public void setSmaFlag(boolean smaFlag) {
        this.smaFlag = smaFlag;
    }
    
    /** Getter for property suffixNm.
     * @return Value of property suffixNm.
     *
     */
    public java.lang.String getSuffixNm() {
        return suffixNm;
    }
    
    /** Setter for property suffixNm.
     * @param suffixNm New value of property suffixNm.
     *
     */
    public void setSuffixNm(java.lang.String suffixNm) {
        this.suffixNm = suffixNm;
    }
    
    /** Getter for property addressPK.
     * @return Value of property addressPK.
     *
     */
    public java.lang.Integer getAddressPK() {
        return addressPK;
    }
    
    /** Setter for property addressPK.
     * @param addressPK New value of property addressPK.
     *
     */
    public void setAddressPK(java.lang.Integer addressPK) {
        this.addressPK = addressPK;
    }
    
    /** Getter for property expelledDate.
     * @return Value of property expelledDate.
     *
     */
    public java.sql.Timestamp getExpelledDate() {
        return expelledDate;
    }
    
    /** Setter for property expelledDate.
     * @param expelledDate New value of property expelledDate.
     *
     */
    public void setExpelledDate(java.sql.Timestamp expelledDate) {
        this.expelledDate = expelledDate;
    }
    
    /** Getter for property mbrFlag.
     * @return Value of property mbrFlag.
     *
     */
    public boolean isMbrFlag() {
        return mbrFlag;
    }
    
    /** Setter for property mbrFlag.
     * @param mbrFlag New value of property mbrFlag.
     *
     */
    public void setMbrFlag(boolean mbrFlag) {
        this.mbrFlag = mbrFlag;
    }
    
    /** Getter for property theRecordData.
     * @return Value of property theRecordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getTheRecordData() {
        return theRecordData;
    }
    
    /** Setter for property theRecordData.
     * @param theRecordData New value of property theRecordData.
     *
     */
    public void setTheRecordData(org.afscme.enterprise.common.RecordData theRecordData) {
        this.theRecordData = theRecordData;
    }
    
}
