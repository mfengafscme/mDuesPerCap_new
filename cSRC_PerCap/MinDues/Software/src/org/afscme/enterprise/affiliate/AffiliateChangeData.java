package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;

/**
 * Data representing the change of an Affiliate-related field.
 */
public class AffiliateChangeData 
{
    private Integer transactionPk;
    private Integer affiliatePk;
    private Integer sectionCodePk;
    private Integer changedByUserPk;
    private Timestamp changedDate;
    private Integer fieldChangedCodePk;
    private String oldValue;
    private String newValue;
    
// Getter and Setter Methods...
    
    /** Getter for property affiliatePk.
     * @return Value of property affiliatePk.
     *
     */
    public Integer getAffiliatePk() {
        return affiliatePk;
    }
    
    /** Setter for property affiliatePk.
     * @param affiliatePk New value of property affiliatePk.
     *
     */
    public void setAffiliatePk(Integer affiliatePk) {
        this.affiliatePk = affiliatePk;
    }
    
    /** Getter for property changedByUserPk.
     * @return Value of property changedByUserPk.
     *
     */
    public Integer getChangedByUserPk() {
        return changedByUserPk;
    }
    
    /** Setter for property changedByUserPk.
     * @param changedByUserPk New value of property changedByUserPk.
     *
     */
    public void setChangedByUserPk(Integer changedByUserPk) {
        this.changedByUserPk = changedByUserPk;
    }
    
    /** Getter for property changedDate.
     * @return Value of property changedDate.
     *
     */
    public java.sql.Timestamp getChangedDate() {
        return changedDate;
    }
    
    /** Setter for property changedDate.
     * @param changedDate New value of property changedDate.
     *
     */
    public void setChangedDate(java.sql.Timestamp changedDate) {
        this.changedDate = changedDate;
    }
    
    /** Getter for property fieldChangedCodePk.
     * @return Value of property fieldChangedCodePk.
     *
     */
    public Integer getFieldChangedCodePk() {
        return fieldChangedCodePk;
    }
    
    /** Setter for property fieldChangedCodePk.
     * @param fieldChangedCodePk New value of property fieldChangedCodePk.
     *
     */
    public void setFieldChangedCodePk(Integer fieldChangedCodePk) {
        this.fieldChangedCodePk = fieldChangedCodePk;
    }
    
    /** Getter for property newValue.
     * @return Value of property newValue.
     *
     */
    public String getNewValue() {
        return newValue;
    }
    
    /** Setter for property newValue.
     * @param newValue New value of property newValue.
     *
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    /** Getter for property oldValue.
     * @return Value of property oldValue.
     *
     */
    public String getOldValue() {
        return oldValue;
    }
    
    /** Setter for property oldValue.
     * @param oldValue New value of property oldValue.
     *
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    /** Getter for property sectionCodePk.
     * @return Value of property sectionCodePk.
     *
     */
    public Integer getSectionCodePk() {
        return sectionCodePk;
    }
    
    /** Setter for property sectionCodePk.
     * @param sectionCodePk New value of property sectionCodePk.
     *
     */
    public void setSectionCodePk(Integer sectionCodePk) {
        this.sectionCodePk = sectionCodePk;
    }
    
    /** Getter for property transactionPk.
     * @return Value of property transactionPk.
     *
     */
    public Integer getTransactionPk() {
        return transactionPk;
    }
    
    /** Setter for property transactionPk.
     * @param transactionPk New value of property transactionPk.
     *
     */
    public void setTransactionPk(Integer transactionPk) {
        this.transactionPk = transactionPk;
    }
    
}
