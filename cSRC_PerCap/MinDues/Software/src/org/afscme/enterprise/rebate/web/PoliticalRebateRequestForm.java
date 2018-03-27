package org.afscme.enterprise.rebate.web;

import java.util.List;
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
import org.afscme.enterprise.person.PRBRequestData;


/** Holds the data on the Political Rebate Request screen
 * @struts:form name="politicalRebateRequestForm"
 */
public class PoliticalRebateRequestForm extends DuesPaidInfoForm {
    private Integer pk;         // Person or Member primary key
    private Integer rqstPk;     // PRB Request primary key
    private Integer appPk;      // PRB Application primary key
    private boolean edit = false;
    private String prbYear;
    private String requestDate;
    private String certifiedMailNumber;
    private boolean denied;
    private Integer deniedReason;
    private String deniedDate;
    private boolean resubmitted;
    private Integer resubmittedDeniedReason;
    private String resubmittedDeniedDate;
    private String keyedDate;
    private String comment;
    private String back;
    private boolean prbYearEditable = false;
    private boolean prbRequestEditable = true;
    private boolean save = false;
    
    public void setPRBRequestData(PRBRequestData prb) {
        if (prb != null) {
            setRqstPk(new Integer(prb.getRqstPk()));
            setAppPk(prb.getPrbAppPK());
            setPrbYear(prb.getRqstRebateYear());
            setCertifiedMailNumber(prb.getRqstCertMailNum());
            setComment(prb.getCommentsTxt());
            requestDate = prb.getRqstDt()==null ? null : TextUtil.format(prb.getRqstDt());
            keyedDate = prb.getRqstKeyedDt()==null ? null : TextUtil.format(prb.getRqstKeyedDt());
            deniedDate = prb.getRqstDeniedDt()==null ? null : TextUtil.format(prb.getRqstDeniedDt());
            resubmittedDeniedDate = prb.getRqstResubmitDeniedDt()==null ? null : TextUtil.format(prb.getRqstResubmitDeniedDt());
            deniedReason = prb.getRqstDeniedReason();
            resubmittedDeniedReason = prb.getRqstResubmitDeniedReason();
            denied = prb.getRqstDeniedFg().booleanValue();
            resubmitted = prb.getRqstResubmitFg().booleanValue();
        }
    }
    
    public PRBRequestData getPRBRequestData() {
        PRBRequestData prb = new PRBRequestData();
        try {
            prb.setRqstDt(TextUtil.parseDate(this.requestDate));
        } catch (Exception e) {
            prb.setRqstDt(null);
        }
        try {
            prb.setRqstDeniedDt(TextUtil.parseDate(this.deniedDate));
        } catch (Exception e) {
            prb.setRqstDeniedDt(null);
        }
        try {
            prb.setRqstResubmitDeniedDt(TextUtil.parseDate(this.resubmittedDeniedDate));
        } catch (Exception e) {
            prb.setRqstResubmitDeniedDt(null);
        }
        try {
            prb.setRqstKeyedDt(TextUtil.parseDate(this.keyedDate));
        } catch (Exception e) {
            prb.setRqstKeyedDt(null);
        }
        prb.setPersonPk(pk.intValue());
        prb.setRqstCertMailNum(certifiedMailNumber);
        prb.setRqstDeniedReason(deniedReason);
        prb.setRqstResubmitDeniedReason(resubmittedDeniedReason);
        prb.setRqstRebateYear(prbYear);
        prb.setRqstDeniedFg(new Boolean(denied));
        prb.setRqstResubmitFg(new Boolean(resubmitted));
        prb.setCommentsTxt(comment);
        if (rqstPk != null) {
            prb.setRqstPk(rqstPk.intValue());
        }
        prb.setPrbAppPK(appPk);
        return prb;
    }
    
// Struts Methods...    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        denied = false;
        resubmitted = false;
    }    
        
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = null;
        
        // Validations ..
        if (isSave()) {
            // Dues Paid To Affiliate Identifiers checking
            errors = validateDuesPaidToAffIDs(); 
            
            // Rebate year
            if (TextUtil.isEmptyOrSpaces(getPrbYear())) {
                errors.add("prbYear", new ActionError("error.field.required.generic", "Rebate Year"));
            } else if (!TextUtil.isInt(getPrbYear())) {
                errors.add("prbYear", new ActionError("error.field.mustBeInt.generic", "Rebate Year"));
            }
            
            // Request Date
            if (TextUtil.isEmptyOrSpaces(getRequestDate())) {
                errors.add("requestDate", new ActionError("error.field.required.generic", "Request Date"));
            } else {
                try {
                    TextUtil.parseDate(getRequestDate(), DateFormat.SHORT);
                } catch (ParseException pe) {
                    errors.add("requestDate", new ActionError("error.field.mustBeDate.generic", "Request Date"));
                }
            }
            
            // Request Denial
            if (getDenied()) {
                // Reason Denied
                if (getDeniedReason()== null || getDeniedReason().intValue()<=0) {
                    errors.add("deniedReason", new ActionError("error.field.required.generic", "Reason Denied"));
                }
                // Denied Date
                if (TextUtil.isEmptyOrSpaces(getDeniedDate())) {
                    errors.add("deniedDate", new ActionError("error.field.required.generic", "Date Denied"));
                } else {
                    try {
                        TextUtil.parseDate(getDeniedDate(), DateFormat.SHORT);
                    } catch (ParseException pe) {
                        errors.add("deniedDate", new ActionError("error.field.mustBeDate.generic", "Date Denied"));
                    }
                }
            }
            
            // Request Resubmitted Denial
            if (getResubmitted() && (getResubmittedDeniedReason()!=null && getResubmittedDeniedReason().intValue()>0)) {
                // Resubmitted Denied Date is required if Resubmitted Denied Reason has some value
                if (TextUtil.isEmptyOrSpaces(getResubmittedDeniedDate())) {
                    errors.add("resubmittedDeniedDate", new ActionError("error.field.required.generic", "Date Resubmitted Denied"));
                } else {
                    try {
                        TextUtil.parseDate(getResubmittedDeniedDate(), DateFormat.SHORT);
                    } catch (ParseException pe) {
                        errors.add("resubmittedDeniedDate", new ActionError("error.field.mustBeDate.generic", "Date Resubmitted Denied"));
                    }
                }
            }
        }
        return errors;
    }
    
    public String toString() {
        return
        "Person PK: " + pk +
        "Rebate Year: " + prbYear +
        "Rebate Request Date: " + requestDate +
        "Certified Mail Number: " + certifiedMailNumber +
        "Rebate Request Denied: " + denied +
        "Rebate Request Denied Reason: " + deniedReason +
        "Rebate Request Denied Date: " + deniedDate +
        "Rebate Request Resubmitted: " + resubmitted +
        "Rebate Request Resubmitted Denied Reason: " + resubmittedDeniedReason +
        "Rebate Request Resubmitted Denied Date: " + resubmittedDeniedDate +
        "Rebate Keyed Date: " + keyedDate +
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
        this.prbYear = TextUtil.isEmptyOrSpaces(prbYear) ? null : prbYear;
    }
    
    /** Getter for property denied.
     * @return Value of property denied.
     *
     */
    public boolean getDenied() {
        return denied;
    }
    
    /** Setter for property denied.
     * @param denied New value of property denied.
     *
     */
    public void setDenied(boolean denied) {
        this.denied = denied;
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
    
    /** Getter for property deniedReason.
     * @return Value of property deniedReason.
     *
     */
    public java.lang.Integer getDeniedReason() {
        return deniedReason;
    }
    
    /** Setter for property deniedReason.
     * @param deniedReason New value of property deniedReason.
     *
     */
    public void setDeniedReason(java.lang.Integer deniedReason) {
        this.deniedReason = deniedReason;
    }
    
    /** Getter for property certifiedMailNumber.
     * @return Value of property certifiedMailNumber.
     *
     */
    public java.lang.String getCertifiedMailNumber() {
        return certifiedMailNumber;
    }
    
    /** Setter for property certifiedMailNumber.
     * @param certifiedMailNumber New value of property certifiedMailNumber.
     *
     */
    public void setCertifiedMailNumber(java.lang.String certifiedMailNumber) {
        this.certifiedMailNumber = TextUtil.isEmptyOrSpaces(certifiedMailNumber) ? null : certifiedMailNumber;
    }
    
    /** Getter for property keyedDate.
     * @return Value of property keyedDate.
     *
     */
    public java.lang.String getKeyedDate() {
        return keyedDate;
    }
    
    /** Setter for property keyedDate.
     * @param keyedDate New value of property keyedDate.
     *
     */
    public void setKeyedDate(java.lang.String keyedDate) {
        this.keyedDate = TextUtil.isEmptyOrSpaces(keyedDate) ? null : keyedDate;
    }
    
    /** Getter for property deniedDate.
     * @return Value of property deniedDate.
     *
     */
    public java.lang.String getDeniedDate() {
        return deniedDate;
    }
    
    /** Setter for property deniedDate.
     * @param deniedDate New value of property deniedDate.
     *
     */
    public void setDeniedDate(java.lang.String deniedDate) {
        this.deniedDate = TextUtil.isEmptyOrSpaces(deniedDate) ? null : deniedDate;
    }
    
    /** Getter for property requestDate.
     * @return Value of property requestDate.
     *
     */
    public java.lang.String getRequestDate() {
        return requestDate;
    }
    
    /** Setter for property requestDate.
     * @param requestDate New value of property requestDate.
     *
     */
    public void setRequestDate(java.lang.String requestDate) {
        this.requestDate = TextUtil.isEmptyOrSpaces(requestDate) ? null : requestDate;
    }
    
    /** Getter for property resubmitted.
     * @return Value of property resubmitted.
     *
     */
    public boolean getResubmitted() {
        return resubmitted;
    }
    
    /** Setter for property resubmitted.
     * @param resubmitted New value of property resubmitted.
     *
     */
    public void setResubmitted(boolean resubmitted) {
        this.resubmitted = resubmitted;
    }
    
    /** Getter for property resubmittedDeniedDate.
     * @return Value of property resubmittedDeniedDate.
     *
     */
    public java.lang.String getResubmittedDeniedDate() {
        return resubmittedDeniedDate;
    }
    
    /** Setter for property resubmittedDeniedDate.
     * @param resubmittedDeniedDate New value of property resubmittedDeniedDate.
     *
     */
    public void setResubmittedDeniedDate(java.lang.String resubmittedDeniedDate) {
        this.resubmittedDeniedDate = TextUtil.isEmptyOrSpaces(resubmittedDeniedDate) ? null : resubmittedDeniedDate;
    }
    
    /** Getter for property resubmittedDeniedReason.
     * @return Value of property resubmittedDeniedReason.
     *
     */
    public java.lang.Integer getResubmittedDeniedReason() {
        return resubmittedDeniedReason;
    }
    
    /** Setter for property resubmittedDeniedReason.
     * @param resubmittedDeniedReason New value of property resubmittedDeniedReason.
     *
     */
    public void setResubmittedDeniedReason(java.lang.Integer resubmittedDeniedReason) {
        this.resubmittedDeniedReason = resubmittedDeniedReason;
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
        this.back = TextUtil.isEmptyOrSpaces(back) ? null : back;
    }
    
    /** Getter for property prbYearEditable.
     * @return Value of property prbYearEditable.
     *
     */
    public boolean isPrbYearEditable() {
        return prbYearEditable;
    }
    
    /** Setter for property prbYearEditable.
     * @param prbYearEditable New value of property prbYearEditable.
     *
     */
    public void setPrbYearEditable(boolean prbYearEditable) {
        this.prbYearEditable = prbYearEditable;
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
    
    /** Getter for property rqstPk.
     * @return Value of property rqstPk.
     *
     */
    public java.lang.Integer getRqstPk() {
        return rqstPk;
    }
    
    /** Setter for property rqstPk.
     * @param rqstPk New value of property rqstPk.
     *
     */
    public void setRqstPk(java.lang.Integer rqstPk) {
        this.rqstPk = rqstPk;
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
    
    /** Getter for property prbRequestEditable.
     * @return Value of property prbRequestEditable.
     *
     */
    public boolean isPrbRequestEditable() {
        return prbRequestEditable;
    }

    /** Getter for property prbRequestEditable.
     * @return Value of property prbRequestEditable.
     *
     */
    public boolean getPrbRequestEditable() {
        return prbRequestEditable;
    }
    
    /** Setter for property prbRequestEditable.
     * @param prbRequestEditable New value of property prbRequestEditable.
     *
     */
    public void setPrbRequestEditable(boolean prbRequestEditable) {
        this.prbRequestEditable = prbRequestEditable;
    }
    
}