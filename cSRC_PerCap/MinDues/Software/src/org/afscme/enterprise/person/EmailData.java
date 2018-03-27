package org.afscme.enterprise.person;

import java.sql.Timestamp;
import org.afscme.enterprise.common.RecordData;

/**
 * Data about an individual email address belonging to a person
 */
public class EmailData 
{
    private Integer emailPk;
    private String personEmailAddr;
    private Integer emailType;
    private Boolean emailBadFg;
    private Timestamp emailMarkedBadDt;
    private Boolean isPrimary;
    private RecordData theRecordData;

    public EmailData () {
        this.personEmailAddr = null; 
        this.emailType = null;
        this.emailBadFg = new Boolean(false);
        this.emailMarkedBadDt = null;
    }
    
    /** Getter for property emailBadFg.
     * @return Value of property emailBadFg.
     *
     */
    public java.lang.Boolean getEmailBadFg() {
        return emailBadFg;
    }
    
    /** Setter for property emailBadFg.
     * @param emailBadFg New value of property emailBadFg.
     *
     */
    public void setEmailBadFg(java.lang.Boolean emailBadFg) {
        this.emailBadFg = emailBadFg;
    }
    
    /** Getter for property emailMarkedBadDt.
     * @return Value of property emailMarkedBadDt.
     *
     */
    public java.sql.Timestamp getEmailMarkedBadDt() {
        return emailMarkedBadDt;
    }
    
    /** Setter for property emailMarkedBadDt.
     * @param emailMarkedBadDt New value of property emailMarkedBadDt.
     *
     */
    public void setEmailMarkedBadDt(java.sql.Timestamp emailMarkedBadDt) {
        this.emailMarkedBadDt = emailMarkedBadDt;
    }
    
    /** Getter for property emailType.
     * @return Value of property emailType.
     *
     */
    public java.lang.Integer getEmailType() {
        return emailType;
    }
    
    /** Setter for property emailType.
     * @param emailType New value of property emailType.
     *
     */
    public void setEmailType(java.lang.Integer emailType) {
        this.emailType = emailType;
    }
    
    /** Getter for property personEmailAddr.
     * @return Value of property personEmailAddr.
     *
     */
    public java.lang.String getPersonEmailAddr() {
        return personEmailAddr;
    }
    
    /** Setter for property personEmailAddr.
     * @param personEmailAddr New value of property personEmailAddr.
     *
     */
    public void setPersonEmailAddr(java.lang.String personEmailAddr) {
        this.personEmailAddr = personEmailAddr;
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
    
    /** Getter for property emailAddressPk.
     * @return Value of property emailAddressPk.
     *
     */
    public java.lang.Integer getEmailPk() {
        return emailPk;
    }
    
    /** Setter for property emailAddressPk.
     * @param emailAddressPk New value of property emailAddressPk.
     *
     */
    public void setEmailPk(java.lang.Integer emailPk) {
        this.emailPk = emailPk;
    }
    
    /** Getter for property isPrimary.
     * @return Value of property isPrimary.
     *
     */
    public java.lang.Boolean getIsPrimary() {
        return isPrimary;
    }
    
    /** Setter for property isPrimary.
     * @param isPrimary New value of property isPrimary.
     *
     */
    public void setIsPrimary(java.lang.Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    
}
