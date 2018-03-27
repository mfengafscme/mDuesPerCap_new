package org.afscme.enterprise.rebate.web;

import java.util.Calendar;
import java.text.DateFormat;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.person.PRBApplicationData;
import org.afscme.enterprise.codes.Codes.RebateAppEvalCode;


/** Holds the data on the Political Rebate Application screen
 * @struts:form name="politicalRebateApplicationForm"
 */
public class PoliticalRebateApplicationForm extends DuesPaidInfoForm {
    private Integer pk;         // Person or Member primary key
    private Integer appPk;      // PRB Application primary key
    private Integer prbYear;
    private String appMailedDate;
    private String appReturnedDate;
    private Integer appEvalCode;
    private Integer appCommentAnalCode;
    private String comment;
    private boolean prbAppEditable = true;
    private boolean edit = false;
    private boolean save = false;
    
    public void setPRBApplicationData(PRBApplicationData prb) {
        if (prb != null) {
            setAppPk(prb.getPrbAppPK());            
            setPrbYear(prb.getRbtYear());
            setComment(prb.getCommentTxt());            
            appMailedDate = prb.getAppMailedDt()==null ? null : TextUtil.format(prb.getAppMailedDt());
            appReturnedDate = prb.getAppReturnedDt()==null ? null : TextUtil.format(prb.getAppReturnedDt());
            appEvalCode = prb.getPrbEvaluationCd();
            appCommentAnalCode = prb.getPrbCommentAnalCd();
            // The PRB Application is no longer editable once it 
            // has been flagged for being sent to the Affiliate
            if (prb.getAffRosterGeneratedFg().booleanValue()) {
                setPrbAppEditable(false);
            }
        }
    }
    
    public PRBApplicationData getPRBApplicationData() {
        PRBApplicationData prb = new PRBApplicationData();
        try {
            prb.setAppMailedDt(TextUtil.parseDate(this.appMailedDate));
        } catch (Exception e) {
            prb.setAppMailedDt(null);
        }
        try {
            prb.setAppReturnedDt(TextUtil.parseDate(this.appReturnedDate));
        } catch (Exception e) {
            prb.setAppReturnedDt(null);
        }
        prb.setPersonPK(this.pk);
        prb.setCommentTxt(this.comment);
        prb.setPrbAppPK(this.appPk);
        prb.setPrbEvaluationCd(this.appEvalCode);
        prb.setPrbCommentAnalCd(this.appCommentAnalCode);
        prb.setRbtYear(this.prbYear);                    
        return prb;
    }
        
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = null;
        
        // Validations ..
        if (isSave()) {
            // Dues Paid To Affiliate Identifiers checking
            errors = validateDuesPaidToAffIDs(); 
            
            // Application Mailed Date
            if (TextUtil.isEmptyOrSpaces(getAppMailedDate())) {
                errors.add("appMailedDate", new ActionError("error.field.required.generic", "Application Mailed Date"));
            } else {
                try {
                    TextUtil.parseDate(getAppMailedDate(), DateFormat.SHORT);
                } catch (ParseException pe) {
                    errors.add("appMailedDate", new ActionError("error.field.mustBeDate.generic", "Application Mailed Date"));
                }
            }
            
            // Application Returned Date
            if (TextUtil.isEmptyOrSpaces(getAppReturnedDate())) {
                errors.add("appReturnedDate", new ActionError("error.field.required.generic", "Application Returned Date"));
            } else {
                try {
                    TextUtil.parseDate(getAppReturnedDate(), DateFormat.SHORT);
                } catch (ParseException pe) {
                    errors.add("appReturnedDate", new ActionError("error.field.mustBeDate.generic", "Application Returned Date"));
                }
            }
            
            // Check for fields mismatch:
            // If Return Date is populated, the Evaluation Code cannot be "Not Returned"
            if (this.appReturnedDate != null && this.appEvalCode.intValue() == RebateAppEvalCode.NR.intValue()) {
                errors.add("appReturnedDate", new ActionError("error.prbApplication.notReturned.mismatch"));
            }

            // If the Return Date is greater than 35 days from the Mailed Date, the only allowable 
            // value for Evaluation Code is "Not Timely"
            if (this.appReturnedDate != null && this.appMailedDate != null) {
                try {
                    Timestamp tsAppMailedDt = TextUtil.parseDate(this.appMailedDate);
                    Timestamp tsAppReturnedDt = TextUtil.parseDate(this.appReturnedDate);
                    if (((tsAppReturnedDt.getTime() - tsAppMailedDt.getTime()) / 86400000 > 35) &&
                        this.appEvalCode.intValue() != RebateAppEvalCode.NT.intValue()) {
                        errors.add("appReturnedDate", new ActionError("error.prbApplication.notTimely.mismatch"));
                    }
                } catch (Exception e) {
                }                              
            }            
            
            // If the application is determined to contain complete and positive answers to 
            // Questions 1 and 7, then a Comment Analysis Code must be provided
            if (this.appEvalCode.intValue() == RebateAppEvalCode.QB.intValue()) {
                if (this.appCommentAnalCode == null || this.appCommentAnalCode.intValue() == 0)
                    errors.add("appCommentAnalCode", new ActionError("error.field.required.generic", "Comment Analysis Code"));                
            }
        }
        return errors;
    }
    
    public String toString() {
        return
        "Person PK: " + pk +
        "Rebate Year: " + prbYear +
        "Application Mailed Date: " + appMailedDate +
        "Application Returned Date: " + appReturnedDate +
        "Application Evaluation Code: " + appEvalCode +
        "Application Comment Analysis Code: " + appCommentAnalCode +
        "Comment: " + comment;
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
        this.comment = TextUtil.isEmptyOrSpaces(comment) ? null : comment;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public java.lang.Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(java.lang.Integer pk) {
        this.pk = pk;
    }
         
    /** Getter for property save.
     * @return Value of property save.
     *
     */
    public boolean isSave() {
        return save;
    }
    
    /** Setter for property save.
     * @param save New value of property save.
     *
     */
    public void setSave(boolean save) {
        this.save = save;
    }
    
    /** Getter for property appPk.
     * @return Value of property appPk.
     *
     */
    public java.lang.Integer getAppPk() {
        return appPk;
    }
    
    /** Setter for property appPk.
     * @param appPk New value of property appPk.
     *
     */
    public void setAppPk(java.lang.Integer appPk) {
        this.appPk = appPk;
    }    
    
    /** Getter for property appCommentAnalCode.
     * @return Value of property appCommentAnalCode.
     *
     */
    public java.lang.Integer getAppCommentAnalCode() {
        return appCommentAnalCode;
    }
    
    /** Setter for property appCommentAnalCode.
     * @param appCommentAnalCode New value of property appCommentAnalCode.
     *
     */
    public void setAppCommentAnalCode(java.lang.Integer appCommentAnalCode) {
        this.appCommentAnalCode = appCommentAnalCode;
    }
    
    /** Getter for property appEvalCode.
     * @return Value of property appEvalCode.
     *
     */
    public java.lang.Integer getAppEvalCode() {
        return appEvalCode;
    }
    
    /** Setter for property appEvalCode.
     * @param appEvalCode New value of property appEvalCode.
     *
     */
    public void setAppEvalCode(java.lang.Integer appEvalCode) {
        this.appEvalCode = appEvalCode;
    }
    
    /** Getter for property appMailedDate.
     * @return Value of property appMailedDate.
     *
     */
    public java.lang.String getAppMailedDate() {
        return appMailedDate;
    }
    
    /** Setter for property appMailedDate.
     * @param appMailedDate New value of property appMailedDate.
     *
     */
    public void setAppMailedDate(java.lang.String appMailedDate) {
        this.appMailedDate = TextUtil.isEmptyOrSpaces(appMailedDate) ? null : appMailedDate;                 
    }
    
    /** Getter for property appReturnedDate.
     * @return Value of property appReturnedDate.
     *
     */
    public java.lang.String getAppReturnedDate() {
        return appReturnedDate;
    }
    
    /** Setter for property appReturnedDate.
     * @param appReturnedDate New value of property appReturnedDate.
     *
     */
    public void setAppReturnedDate(java.lang.String appReturnedDate) {
        this.appReturnedDate = TextUtil.isEmptyOrSpaces(appReturnedDate) ? null : appReturnedDate;         
    }
    
    /** Getter for property prbAppEditable.
     * @return Value of property prbAppEditable.
     *
     */
    public boolean isPrbAppEditable() {
        return prbAppEditable;
    }
 
   /** Getter for property prbAppEditable.
     * @return Value of property prbAppEditable.
     *
     */
    public boolean getPrbAppEditable() {
        return prbAppEditable;
    }
        
    /** Setter for property prbAppEditable.
     * @param prbAppEditable New value of property prbAppEditable.
     *
     */
    public void setPrbAppEditable(boolean prbAppEditable) {
        this.prbAppEditable = prbAppEditable;
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
    
}