package org.afscme.enterprise.person;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;



/**
 * Represents the Check Data that is associated with a Rebate.
 */
public class PRBCheckInfo 
{
    private Integer personPK;
    private Integer rebateYear;
    private Integer caseNumber;
    private Integer checkNumber;
    private Double amount;
    private Timestamp date;
    private Boolean returnedFlag;
    private Integer checkNumber2;
    private Double amount2;
    private Timestamp date2;
    private Integer supplCheckNumber;
    private Double supplAmount;
    private Timestamp supplDate;
    private String comment;
    
    private String amountString;
    private String amount2String;
    private String supplAmountString;
    
    
    /** Getter for property caseNumber.
     * @return Value of property caseNumber.
     *
     */
    public java.lang.Integer getCaseNumber() {
        return caseNumber;
    }
    
    /** Setter for property caseNumber.
     * @param caseNumber New value of property caseNumber.
     *
     */
    public void setCaseNumber(java.lang.Integer caseNumber) {
        this.caseNumber = caseNumber;
    }
    
    /** Getter for property checkNumber.
     * @return Value of property checkNumber.
     *
     */
    public java.lang.Integer getCheckNumber() {
        return checkNumber;
    }
    
    /** Setter for property checkNumber.
     * @param checkNumber New value of property checkNumber.
     *
     */
    public void setCheckNumber(java.lang.Integer checkNumber) {
        this.checkNumber = checkNumber;
    }
    
    /** Getter for property checkNumber2.
     * @return Value of property checkNumber2.
     *
     */
    public java.lang.Integer getCheckNumber2() {
        return checkNumber2;
    }
    
    /** Setter for property checkNumber2.
     * @param checkNumber2 New value of property checkNumber2.
     *
     */
    public void setCheckNumber2(java.lang.Integer checkNumber2) {
        this.checkNumber2 = checkNumber2;
    }
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public java.lang.String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }
    
    /** Getter for property date.
     * @return Value of property date.
     *
     */
    public java.sql.Timestamp getDate() {
        return date;
    }
    
    /** Setter for property date.
     * @param date New value of property date.
     *
     */
    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }
    
    /** Getter for property date2.
     * @return Value of property date2.
     *
     */
    public java.sql.Timestamp getDate2() {
        return date2;
    }
    
    /** Setter for property date2.
     * @param date2 New value of property date2.
     *
     */
    public void setDate2(java.sql.Timestamp date2) {
        this.date2 = date2;
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
    
    /** Getter for property rebateYear.
     * @return Value of property rebateYear.
     *
     */
    public java.lang.Integer getRebateYear() {
        return rebateYear;
    }
    
    /** Setter for property rebateYear.
     * @param rebateYear New value of property rebateYear.
     *
     */
    public void setRebateYear(java.lang.Integer rebateYear) {
        this.rebateYear = rebateYear;
    }
    
    /** Getter for property returnedFlag.
     * @return Value of property returnedFlag.
     *
     */
    public Boolean getReturnedFlag() {
        return returnedFlag;
    }
    
    /** Setter for property returnedFlag.
     * @param returnedFlag New value of property returnedFlag.
     *
     */
    public void setReturnedFlag(Boolean returnedFlag) {
        this.returnedFlag = returnedFlag;
    }
    
    /** Getter for property supplCheckNumber.
     * @return Value of property supplCheckNumber.
     *
     */
    public java.lang.Integer getSupplCheckNumber() {
        return supplCheckNumber;
    }
    
    /** Setter for property supplCheckNumber.
     * @param supplCheckNumber New value of property supplCheckNumber.
     *
     */
    public void setSupplCheckNumber(java.lang.Integer supplCheckNumber) {
        this.supplCheckNumber = supplCheckNumber;
    }
    
    /** Getter for property supplDate.
     * @return Value of property supplDate.
     *
     */
    public java.sql.Timestamp getSupplDate() {
        return supplDate;
    }
    
    /** Setter for property supplDate.
     * @param supplDate New value of property supplDate.
     *
     */
    public void setSupplDate(java.sql.Timestamp supplDate) {
        this.supplDate = supplDate;
    }
    
    /** Getter for property amount.
     * @return Value of property amount.
     *
     */
    public Double getAmount() {
        return amount;
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     *
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }
    
    /** Getter for property amount2.
     * @return Value of property amount2.
     *
     */
    public java.lang.Double getAmount2() {
        return amount2;
    }
    
    /** Setter for property amount2.
     * @param amount2 New value of property amount2.
     *
     */
    public void setAmount2(java.lang.Double amount2) {
        this.amount2 = amount2;
    }
    
    /** Getter for property supplAmount.
     * @return Value of property supplAmount.
     *
     */
    public java.lang.Double getSupplAmount() {
        return supplAmount;
    }
    
    /** Setter for property supplAmount.
     * @param supplAmount New value of property supplAmount.
     *
     */
    public void setSupplAmount(java.lang.Double supplAmount) {
        this.supplAmount = supplAmount;
    }

    public String getAmountString() {
        return formatDollar(amount);        
    }    
    
    public void setAmountString(String as){
        this.amountString = as;
    }
    
    public String getAmount2String() {
        return formatDollar(amount2);        
    }    
    
    public void setAmount2String(String as2){
        this.amount2String = as2;
    }

    public String getSupplAmountString() {
        return formatDollar(supplAmount);        
    }    
    
    public void setSupplAmountString(String s){
        this.supplAmountString = s;
    }    
    
    public String formatDollar(Double value)
    {
        String newValue = "";

        if (value != null)
        {
            NumberFormat dollar = null;

            dollar = NumberFormat.getInstance();
            dollar.setMaximumFractionDigits(2);
            dollar.setMinimumFractionDigits(2);
			
        	newValue = dollar.format(value.doubleValue());    	
        }
          return newValue;        
    }
	
}
