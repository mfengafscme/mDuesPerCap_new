package org.afscme.enterprise.rebate.web;

import java.util.List;
import java.util.Calendar;
import java.text.DateFormat;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.person.PRBCheckInfo;
import org.afscme.enterprise.codes.Codes.RebateAcceptanceCode;
import org.afscme.enterprise.codes.Codes.RebateMbrType;


/** Holds the data on the Political Rebate Annual Rebate Information Edit screen
 * @struts:form name="politicalRebateAnnualRebateInformationEditForm"
 */
public class PoliticalRebateAnnualRebateInformationEditForm extends DuesPaidInfoForm {
    
    private Integer prbYear;
    private String prbRosterStatus;
    private String checkNumber;
    private String amount;
    private String date;
    private String checkNumber2;
    private String amount2;
    private String date2;
    private boolean returnedFlag;
    private String caseNumber;
    private String supplCheckNumber;
    private String supplAmount;
    private String supplDate;
    private String comment;
    private boolean actingAsAffiliate;
    private boolean edit = false;
    private boolean checkAllowed;
    
    public void setPRBCheckInfo(PRBCheckInfo prb) {
        if (prb != null) {
            caseNumber = prb.getCaseNumber()==null ? null : prb.getCaseNumber().toString();
            checkNumber = prb.getCheckNumber()==null ? null : prb.getCheckNumber().toString();
            checkNumber2 = prb.getCheckNumber2()==null ? null : prb.getCheckNumber2().toString();
            supplCheckNumber = prb.getSupplCheckNumber()==null ? null : prb.getSupplCheckNumber().toString();
            amount = prb.getAmount()==null ? null : prb.getAmount().toString();
            amount2 = prb.getAmount2()==null ? null : prb.getAmount2().toString();
            supplAmount = prb.getSupplAmount()==null ? null : prb.getSupplAmount().toString();
            date = prb.getDate()==null ? null : TextUtil.format(prb.getDate());
            date2 = prb.getDate2()==null ? null :TextUtil.format(prb.getDate2());
            supplDate = prb.getSupplDate()==null ? null : TextUtil.format(prb.getSupplDate());
            returnedFlag = prb.getReturnedFlag().booleanValue();
            comment = prb.getComment();
        }
    }
    
    public PRBCheckInfo getPRBCheckInfo() {
        PRBCheckInfo prb = new PRBCheckInfo();
        prb.setCaseNumber(setStringToInteger(caseNumber));
        prb.setCheckNumber(setStringToInteger(checkNumber));
        prb.setCheckNumber2(setStringToInteger(checkNumber2));
        prb.setSupplCheckNumber(setStringToInteger(supplCheckNumber));
        prb.setAmount(setStringToDouble(amount));
        prb.setAmount2(setStringToDouble(amount2));
        prb.setSupplAmount(setStringToDouble(supplAmount));
        try {
            prb.setDate(TextUtil.parseDate(this.date));
        } catch (Exception e) {
            prb.setDate(null);
        }
        try {
            prb.setDate2(TextUtil.parseDate(this.date2));
        } catch (Exception e) {
            prb.setDate2(null);
        }
        try {
            prb.setSupplDate(TextUtil.parseDate(this.supplDate));
        } catch (Exception e) {
            prb.setSupplDate(null);
        }
        prb.setRebateYear(prbYear);
        prb.setReturnedFlag(new Boolean(returnedFlag));
        prb.setComment(comment);
        return prb;
    }
    
// Struts Methods...    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	super.reset(mapping, request);
        returnedFlag = false;
    }    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        // Validation checking
        if (isEdit()) {
            // Check for Affilate Identifiers, atleast one full Affiliate Identifier is required
            if (getType_1()==null && getLocal_1()==null && getState_1()==null && getCouncil_1()==null) {
                errors.add("code_1", new ActionError("error.field.affID.required"));
            } else {
                // Check for required fields
                if (getDuration_1()==null) 
                    errors.add("code_1", new ActionError("error.field.required.generic", "Duration #1"));                                                
                if (getFiledWith_1()==null) 
                    errors.add("filedWith_1", new ActionError("error.field.required.generic", "Filed With #1"));
                
                // Member Type, Member Status, and Dues Type fields are required IFF Acceptance Code
                // is accepted
                if (getAcceptanceCode_1() == null ||
                    getAcceptanceCode_1().intValue()==RebateAcceptanceCode.D.intValue()) {
                    setCheckAllowed(false);
                } else {
                    // Check for required fields
                    if (getMbrType_1()==null) 
                        errors.add("mbrType_1", new ActionError("error.field.required.generic", "Member Type #1"));
                    if (getMbrStatus_1()==null) 
                        errors.add("mbrStatus_1", new ActionError("error.field.required.generic", "Member Status #1"));
                    if (getDuesType_1()==null) 
                        errors.add("duesType_1", new ActionError("error.field.required.generic", "Dues Type #1"));
                    // Set eligibility for issuing a check..
                    setCheckAllowed(true);                
                }                
            }
            
            // Check for acceptable value of Acceptance Code
            if (getMbrType_1() != null) {
                if (getMbrType_1().intValue()==RebateMbrType.N.intValue() || getMbrType_1().intValue()==RebateMbrType.S.intValue() ||
                    getMbrType_1().intValue()==RebateMbrType.O.intValue() || getMbrType_1().intValue()==RebateMbrType.A.intValue())
                    if (getAcceptanceCode_1()==null || getAcceptanceCode_1().intValue()!=RebateAcceptanceCode.D.intValue())
                        errors.add("acceptanceCode_1", new ActionError("error.field.acceptanceCode.invalid", "#1"));
            }
            if (getMbrType_2() != null) {
                if (getMbrType_2().intValue()==RebateMbrType.N.intValue() || getMbrType_2().intValue()==RebateMbrType.S.intValue() ||
                    getMbrType_2().intValue()==RebateMbrType.O.intValue() || getMbrType_2().intValue()==RebateMbrType.A.intValue())
                    if (getAcceptanceCode_2()==null || getAcceptanceCode_2().intValue()!=RebateAcceptanceCode.D.intValue())
                        errors.add("acceptanceCode_2", new ActionError("error.field.acceptanceCode.invalid", "#2"));
            }
            if (getMbrType_3() != null) {
                if (getMbrType_3().intValue()==RebateMbrType.N.intValue() || getMbrType_3().intValue()==RebateMbrType.S.intValue() ||
                    getMbrType_3().intValue()==RebateMbrType.O.intValue() || getMbrType_3().intValue()==RebateMbrType.A.intValue())
                    if (getAcceptanceCode_3()==null || getAcceptanceCode_3().intValue()!=RebateAcceptanceCode.D.intValue())
                        errors.add("acceptanceCode_3", new ActionError("error.field.acceptanceCode.invalid", "#3"));
            }
            
            // Check for Duration, FiledWith, Acceptance Code, Member Type, Member Status, Dues Type
            if (getAffPk_2()!=null) {
                if (getAffPk_1()!=null && getAffPk_1().intValue()==getAffPk_2().intValue()) {
                    setAffPk_2(null);
                    errors.add("code_1", new ActionError("error.field.affID.duplicate", "Affiliate Identifer #2"));                    
                }
                // Check for required fields
                if (getDuration_2()==null)
                    errors.add("duration_2", new ActionError("error.field.required.generic", "Duration #2"));
                if (getFiledWith_2()==null)
                    errors.add("filedWith_2", new ActionError("error.field.required.generic", "Filed With #2"));
                
                // Member Type, Member Status, and Dues Type fields are required IFF Acceptance Code
                // is accepted
                if (getAcceptanceCode_2()==null ||
                    getAcceptanceCode_2().intValue()==RebateAcceptanceCode.D.intValue()) {
                    setCheckAllowed(getCheckAllowed());
                } else {
                    // Check for required field
                    if (getMbrType_2()==null)
                        errors.add("mbrType_2", new ActionError("error.field.required.generic", "Member Type #2"));
                    if (getMbrStatus_2()==null)
                        errors.add("mbrStatus_2", new ActionError("error.field.required.generic", "Member Status #2"));
                    if (getDuesType_2()==null)
                        errors.add("duesType_2", new ActionError("error.field.required.generic", "Dues Type #2"));
                    // Set eligibility for issuing a check.
                    setCheckAllowed(true);
                }
            }
            
            if (getAffPk_3()!=null) {
                if (getAffPk_1()!=null && getAffPk_2()!=null) {
                    if (getAffPk_1().intValue()==getAffPk_3().intValue() || getAffPk_2().intValue()==getAffPk_3().intValue()) {
                        setAffPk_3(null);
                        errors.add("code_1", new ActionError("error.field.affID.duplicate", "Affiliate Identifer #3"));         
                    }
                }
                if (getDuration_3()==null)
                    errors.add("duration_3", new ActionError("error.field.required.generic", "Duration #3"));
                if (getFiledWith_3()==null)
                    errors.add("filedWith_3", new ActionError("error.field.required.generic", "Filed With #3"));
                
                // Member Type, Member Status, and Dues Type fields are required IFF Acceptance Code
                // is accepted
                if (getAcceptanceCode_3()==null ||
                    getAcceptanceCode_3().intValue()==RebateAcceptanceCode.D.intValue()) {
                    setCheckAllowed(getCheckAllowed());
                } else {
                    // Check for required fields
                    if (getMbrType_3()==null)
                        errors.add("mbrType_3", new ActionError("error.field.required.generic", "Member Type #3"));
                    if (getMbrStatus_3()==null)
                        errors.add("mbrStatus_3", new ActionError("error.field.required.generic", "Member Status #3"));
                    if (getDuesType_3()==null)
                        errors.add("duesType_3", new ActionError("error.field.required.generic", "Dues Type #3"));
                    // Set eligibility for issuing a check..
                    setCheckAllowed(true);
                }
            }
            
            // Affiliates only have privilege to view Check Information data, so no need to validate
            // Check for Check Information and Rebate Challenge Information
            if (!isActingAsAffiliate()) {  
                if (!isCheckAllowed()) {
                    if (getCheckNumber()!=null || getCheckNumber2()!=null ||
                        getAmount()!=null || getAmount2()!=null ||
                        getDate()!=null || getDate2()!=null || isReturnedFlag()) {
                        errors.add("checkNumber", new ActionError("error.field.check.notAllowed"));
                    }
                }
                if (!TextUtil.isEmptyOrSpaces(getCheckNumber()) && !TextUtil.isInt(getCheckNumber()))
                    errors.add("checkNumber", new ActionError("error.field.mustBeInt.generic", "Number"));
                if (!TextUtil.isEmptyOrSpaces(getAmount()) && !TextUtil.isDouble(getAmount()))
                    errors.add("amount", new ActionError("error.field.mustBeInt.generic", "Amount"));
                if (!TextUtil.isEmptyOrSpaces(getDate())) {
                    try {
                        TextUtil.parseDate(getDate(), DateFormat.SHORT);
                    } catch (ParseException pe) {
                        errors.add("date", new ActionError("error.field.mustBeDate.generic", "Date"));
                    }
                }
                if (!TextUtil.isEmptyOrSpaces(getCheckNumber2()) && !TextUtil.isInt(getCheckNumber2()))
                    errors.add("checkNumber2", new ActionError("error.field.mustBeInt.generic", "Second Number"));
                if (!TextUtil.isEmptyOrSpaces(getAmount2()) && !TextUtil.isDouble(getAmount2()))
                    errors.add("amount2", new ActionError("error.field.mustBeInt.generic", "Second Amount"));
                if (!TextUtil.isEmptyOrSpaces(getDate2())) {
                    try {
                        TextUtil.parseDate(getDate2(), DateFormat.SHORT);
                    } catch (ParseException pe) {
                        errors.add("date2", new ActionError("error.field.mustBeDate.generic", "Second Date"));
                    }
                }                        
                if (!TextUtil.isEmptyOrSpaces(getCaseNumber()) && !TextUtil.isInt(getCaseNumber()))
                    errors.add("caseNumber", new ActionError("error.field.mustBeInt.generic", "Case Number"));
                if (!TextUtil.isEmptyOrSpaces(getSupplCheckNumber()) && !TextUtil.isInt(getSupplCheckNumber()))
                    errors.add("supplCheckNumber", new ActionError("error.field.mustBeInt.generic", "Supplemental Check Number"));
                if (!TextUtil.isEmptyOrSpaces(getSupplAmount()) && !TextUtil.isDouble(getSupplAmount()))
                    errors.add("supplAmount", new ActionError("error.field.mustBeInt.generic", "Supplemental Amount"));
                if (!TextUtil.isEmptyOrSpaces(getSupplDate())) {
                    try {
                        TextUtil.parseDate(getSupplDate(), DateFormat.SHORT);
                    } catch (ParseException pe) {
                        errors.add("supplDate", new ActionError("error.field.mustBeDate.generic", "Supplemental Date"));
                    }
                }
            }
        }
        return errors;
    }
    
    public String toString() {
        return
        "Rebate Year: " + prbYear +
        "Check Number: " + checkNumber +
        "Check Amount: " + amount +
        "Check Date: " + date +
        "Returned Flag: " + returnedFlag +
        "2nd Check Number: " + checkNumber2 +
        "2nd Check Amount: " + amount2 +
        "2nd Check Date: " + date2 +
        "Case Number: " + caseNumber +
        "Supplement Check Number: " + supplCheckNumber +
        "Supplement Check Amount: " + supplAmount +
        "Supplement Check Date: " + supplDate +
        "Comment: " + comment +
        "Using Data Utility Flow: " + actingAsAffiliate +
        "Eligible for Issuing Check: " + checkAllowed;        
    }
    
    /** Getter for property prbYear.
     * @return Value of property prbYear.
     *
     */
    public java.lang.Integer getPrbYear() {
        return prbYear;
    }
    
    /** Setter for property prbYear.
     * @param prbYear New value of property prbYear.
     *
     */
    public void setPrbYear(java.lang.Integer prbYear) {
        this.prbYear = prbYear;
    }
    
    /** Getter for property returnedFlag.
     * @return Value of property returnedFlag.
     *
     */
    public boolean isReturnedFlag() {
        return returnedFlag;
    }

    /** Getter for property returnedFlag.
     * @return Value of property returnedFlag.
     *
     */
    public boolean getReturnedFlag() {
        return returnedFlag;
    }
    
    /** Setter for property returnedFlag.
     * @param returnedFlag New value of property returnedFlag.
     *
     */
    public void setReturnedFlag(boolean returnedFlag) {
        this.returnedFlag = returnedFlag;
    }
    
    /** Getter for property checkNumber.
     * @return Value of property checkNumber.
     *
     */
    public java.lang.String getCheckNumber() {
        return checkNumber;
    }
    
    /** Setter for property checkNumber.
     * @param checkNumber New value of property checkNumber.
     *
     */
    public void setCheckNumber(java.lang.String checkNumber) {
        this.checkNumber = (TextUtil.isEmptyOrSpaces(checkNumber)) ? null : checkNumber;
    }
    
    /** Getter for property date.
     * @return Value of property date.
     *
     */
    public String getDate() {
        return date;
    }
    
    /** Setter for property date.
     * @param date New value of property date.
     *
     */
    public void setDate(String date) {
        this.date = (TextUtil.isEmptyOrSpaces(date)) ? null : date;
    }
    
    /** Getter for property supplCheckNumber.
     * @return Value of property supplCheckNumber.
     *
     */
    public java.lang.String getSupplCheckNumber() {
        return supplCheckNumber;
    }
    
    /** Setter for property supplCheckNumber.
     * @param supplCheckNumber New value of property supplCheckNumber.
     *
     */
    public void setSupplCheckNumber(java.lang.String supplCheckNumber) {
        this.supplCheckNumber = (TextUtil.isEmptyOrSpaces(supplCheckNumber)) ? null : supplCheckNumber;
    }
    
    /** Getter for property date2.
     * @return Value of property date2.
     *
     */
    public String getDate2() {
        return date2;
    }
    
    /** Setter for property date2.
     * @param date2 New value of property date2.
     *
     */
    public void setDate2(String date2) {
        this.date2 = (TextUtil.isEmptyOrSpaces(date2)) ? null : date2;
    }
    
    /** Getter for property amount2.
     * @return Value of property amount2.
     *
     */
    public java.lang.String getAmount2() {
        return this.formatDollar(amount2);
        //return amount2;
    }
    
    /** Setter for property amount2.
     * @param amount2 New value of property amount2.
     *
     */
    public void setAmount2(java.lang.String amount2) {
        this.amount2 = (TextUtil.isEmptyOrSpaces(amount2)) ? null : amount2;
    }
    
    /** Getter for property checkNumber2.
     * @return Value of property checkNumber2.
     *
     */
    public java.lang.String getCheckNumber2() {
        return checkNumber2;
    }
    
    /** Setter for property checkNumber2.
     * @param checkNumber2 New value of property checkNumber2.
     *
     */
    public void setCheckNumber2(java.lang.String checkNumber2) {
        this.checkNumber2 = (TextUtil.isEmptyOrSpaces(checkNumber2)) ? null : checkNumber2;
    }
    
    /** Getter for property caseNumber.
     * @return Value of property caseNumber.
     *
     */
    public java.lang.String getCaseNumber() {
        return caseNumber;
    }
    
    /** Setter for property caseNumber.
     * @param caseNumber New value of property caseNumber.
     *
     */
    public void setCaseNumber(java.lang.String caseNumber) {
        this.caseNumber = (TextUtil.isEmptyOrSpaces(caseNumber)) ? null : caseNumber;
    }
    
    /** Getter for property supplDate.
     * @return Value of property supplDate.
     *
     */
    public String getSupplDate() {
        return supplDate;
    }
    
    /** Setter for property supplDate.
     * @param supplDate New value of property supplDate.
     *
     */
    public void setSupplDate(String supplDate) {
        this.supplDate = (TextUtil.isEmptyOrSpaces(supplDate)) ? null : supplDate;
    }
    
    /** Getter for property amount.
     * @return Value of property amount.
     *
     */
    public java.lang.String getAmount() {
        return this.formatDollar(amount);        
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     *
     */
    public void setAmount(java.lang.String amount) {
        this.amount = (TextUtil.isEmptyOrSpaces(amount)) ? null : amount;
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
        this.comment = (TextUtil.isEmptyOrSpaces(comment)) ? null : comment;
    }
    
    /** Getter for property supplAmount.
     * @return Value of property supplAmount.
     *
     */
    public java.lang.String getSupplAmount() {
        return this.formatDollar(supplAmount);        
    }
    
    /** Setter for property supplAmount.
     * @param supplAmount New value of property supplAmount.
     *
     */
    public void setSupplAmount(java.lang.String supplAmount) {
        this.supplAmount = (TextUtil.isEmptyOrSpaces(supplAmount)) ? null : supplAmount;
    }
    
    /** Getter for property prbRosterStatus.
     * @return Value of property prbRosterStatus.
     *
     */
    public java.lang.String getPrbRosterStatus() {
        return prbRosterStatus;
    }
    
    /** Setter for property prbRosterStatus.
     * @param prbRosterStatus New value of property prbRosterStatus.
     *
     */
    public void setPrbRosterStatus(java.lang.String prbRosterStatus) {
        this.prbRosterStatus = (TextUtil.isEmptyOrSpaces(prbRosterStatus)) ? null : prbRosterStatus;
    }
    
    /** Getter for property actingAsAffiliate.
     * @return Value of property actingAsAffiliate.
     *
     */
    public boolean isActingAsAffiliate() {
        return actingAsAffiliate;
    }
    
    /** Setter for property actingAsAffiliate.
     * @param actingAsAffiliate New value of property actingAsAffiliate.
     *
     */
    public void setActingAsAffiliate(boolean actingAsAffiliate) {
        this.actingAsAffiliate = actingAsAffiliate;
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
    
    /** Getter for property checkAllowed.
     * @return Value of property checkAllowed.
     *
     */
    public boolean isCheckAllowed() {
        return checkAllowed;
    }

    /** Getter for property checkAllowed.
     * @return Value of property checkAllowed.
     *
     */
    public boolean getCheckAllowed() {
        return checkAllowed;
    }
    
    /** Setter for property checkAllowed.
     * @param checkAllow New value of property checkAllow.
     *
     */
    public void setCheckAllowed(boolean checkAllowed) {
        this.checkAllowed = checkAllowed;
    }
    public String formatDollar(String value)
    {
        String newValue = "";

        if (value != null && value.length() > 0)
        {
            Number num = null;
            NumberFormat dollar = null;

          try{

            dollar = NumberFormat.getInstance();
            dollar.setMaximumFractionDigits(2);
            dollar.setMinimumFractionDigits(2);
            newValue = dollar.format(dollar.parse(value).doubleValue());
          }catch(ParseException e)
          {                  
            e.printStackTrace();
          }
        }
          return newValue;        
    }
    
}


