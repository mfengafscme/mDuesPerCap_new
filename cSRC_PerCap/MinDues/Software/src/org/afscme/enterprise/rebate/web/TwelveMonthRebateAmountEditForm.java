package org.afscme.enterprise.rebate.web;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import org.afscme.enterprise.rebate.PRB12MonthRebateAmount;

/**
 * Form data from the 12 Month Rebate Amount Add/Edit page.
 *
 * @struts:form name="twelveMonthRebateAmountEditForm"
 */
public class TwelveMonthRebateAmountEditForm extends ActionForm {
    private boolean edit = false;
    
    /**
     * Contains the Rebate Year
     */
    private String prbYear;
    
    /**
     * Contains the Rebate Percentage.  This field only retains the percentage and is
     * not used in any calculations.
     */
    private String prbPercentage;
    
    /**
     * This is the annual amount to be paid to a member with a Full Time dues status.
     */
    private String prbFullTime;
    
    /**
     * This is the annual amount to be paid to a member with a PartTime dues status.
     */
    private String prbPartTime;
    
    /**
     * This is the annual amount to be paid to a member with a Lower PartTime dues
     * status.
     */
    private String prbLowerPartTime;
    
    /**
     * This is the annual amount to be paid to a member with a Retiree dues status.
     */
    private String prbRetiree;

    
    public void setPRB12MonthRebateAmount(PRB12MonthRebateAmount prb) {
        if (prb != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            prbYear          = prb.getPrbYear().toString();
            prbPercentage    = df.format(prb.getPrbPercentage().doubleValue());
            prbFullTime      = df.format(prb.getPrbFullTime().doubleValue());
            prbPartTime      = df.format(prb.getPrbPartTime().doubleValue());
            prbLowerPartTime = df.format(prb.getPrbLowerPartTime().doubleValue());
            prbRetiree       = df.format(prb.getPrbRetiree().doubleValue());
        }
    }

    public PRB12MonthRebateAmount getPRB12MonthRebateAmount() {
        PRB12MonthRebateAmount prb = new PRB12MonthRebateAmount();
        prb.setPrbYear(new Integer(prbYear));
        prb.setPrbPercentage(new Double(prbPercentage));
        prb.setPrbFullTime(new Double(prbFullTime));
        prb.setPrbPartTime(new Double(prbPartTime));
        prb.setPrbLowerPartTime(new Double(prbLowerPartTime));
        prb.setPrbRetiree(new Double(prbRetiree));
        return prb;
    }
    
    public String toString() {
        return
        "Rebate Year " + prbYear +
        "Rebate Percentage" + prbPercentage +
        "Full Time Rebate Amount" + prbFullTime +
        "Part Time Rebate Amount" + prbPartTime +
        "Lower PartTime Rebate Amount" + prbLowerPartTime +
        "Retiree Rebate Amount" + prbRetiree;
    }
    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
    
    /** Getter for property prbFullTime.
     * @return Value of property prbFullTime.
     *
     */
    public java.lang.String getPrbFullTime() {
        return prbFullTime;
    }
    
    /** Setter for property prbFullTime.
     * @param prbFullTime New value of property prbFullTime.
     *
     */
    public void setPrbFullTime(java.lang.String prbFullTime) {
        this.prbFullTime = prbFullTime;
    }
    
    /** Getter for property prbLowerPartTime.
     * @return Value of property prbLowerPartTime.
     *
     */
    public java.lang.String getPrbLowerPartTime() {
        return prbLowerPartTime;
    }
    
    /** Setter for property prbLowerPartTime.
     * @param prbLowerPartTime New value of property prbLowerPartTime.
     *
     */
    public void setPrbLowerPartTime(java.lang.String prbLowerPartTime) {
        this.prbLowerPartTime = prbLowerPartTime;
    }
    
    /** Getter for property prbPartTime.
     * @return Value of property prbPartTime.
     *
     */
    public java.lang.String getPrbPartTime() {
        return prbPartTime;
    }
    
    /** Setter for property prbPartTime.
     * @param prbPartTime New value of property prbPartTime.
     *
     */
    public void setPrbPartTime(java.lang.String prbPartTime) {
        this.prbPartTime = prbPartTime;
    }
    
    /** Getter for property prbPercentage.
     * @return Value of property prbPercentage.
     *
     */
    public java.lang.String getPrbPercentage() {
        return prbPercentage;
    }
    
    /** Setter for property prbPercentage.
     * @param prbPercentage New value of property prbPercentage.
     *
     */
    public void setPrbPercentage(java.lang.String prbPercentage) {
        this.prbPercentage = prbPercentage;
    }
    
    /** Getter for property prbRetiree.
     * @return Value of property prbRetiree.
     *
     */
    public java.lang.String getPrbRetiree() {
        return prbRetiree;
    }
    
    /** Setter for property prbRetiree.
     * @param prbRetiree New value of property prbRetiree.
     *
     */
    public void setPrbRetiree(java.lang.String prbRetiree) {
        this.prbRetiree = prbRetiree;
    }
    
    /** Getter for property prbYear.
     * @return Value of property prbYear.
     *
     */
    public java.lang.String getPrbYear() {
        return prbYear;
    }
    
    /** Setter for property prbYear.
     * @param prbYear New value of property prbYear.
     *
     */
    public void setPrbYear(java.lang.String prbYear) {
        this.prbYear = prbYear;
    }
    
    /** Getter for property edit.
     * @return Value of property edit.
     *
     */
    public boolean isEdit() {
        return edit;
    }
    
    /** Setter for property edit.
     * @param edit New value of property edit.
     *
     */
    public void setEdit(boolean edit) {
        this.edit = edit;
    }    
}



