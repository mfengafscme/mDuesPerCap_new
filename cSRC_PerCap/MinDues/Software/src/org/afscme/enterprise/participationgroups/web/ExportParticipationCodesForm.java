package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.util.TextUtil;


/**
 * Represents the form for Export Participation Codes
 *
 * @struts:form name="exportParticipationCodesForm"
 */
public class ExportParticipationCodesForm extends ActionForm {

    public static final int TAB         = 0;
    public static final int COMMA       = 1;
    public static final int SEMICOLON   = 2;

    protected Integer groupPk;
    protected Integer typePk;
    protected Integer detailPk;
    protected Integer outputFormat;
    
    /**
     * constructor to set up values
     */
    public ExportParticipationCodesForm() {
    }

    /**
     * toString method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("Output Format: " + outputFormat);
        buf.append("groupPk: " + groupPk);
        buf.append("typePk: " + typePk);
        buf.append("detailPk: " + detailPk);
        return buf.toString()+"]";
    }
    
    /** Validation method for this form
     *
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if (outputFormat == null) {
            errors.add("outputFormat", new ActionError("error.field.required.generic", "File Formats"));
        }
        return errors;
    }
    
    /** Getter for property groupPk.
     * @return Value of property groupPk.
     *
     */
    public java.lang.Integer getGroupPk() {
        return groupPk;
    }
    
    /** Setter for property groupPk.
     * @param groupPk New value of property groupPk.
     *
     */
    public void setGroupPk(java.lang.Integer groupPk) {
        this.groupPk = groupPk;
    }
    
    /** Getter for property typePk.
     * @return Value of property typePk.
     *
     */
    public java.lang.Integer getTypePk() {
        return typePk;
    }
    
    /** Setter for property typePk.
     * @param typePk New value of property typePk.
     *
     */
    public void setTypePk(java.lang.Integer typePk) {
        this.typePk = typePk;
    }
    
    /** Getter for property outputFormat.
     * @return Value of property outputFormat.
     *
     */
    public java.lang.Integer getOutputFormat() {
        return outputFormat;
    }
    
    /** Setter for property outputFormat.
     * @param outputFormat New value of property outputFormat.
     *
     */
    public void setOutputFormat(java.lang.Integer outputFormat) {
        this.outputFormat = outputFormat;
    }
    
    /** Getter for property detailPk.
     * @return Value of property detailPk.
     *
     */
    public java.lang.Integer getDetailPk() {
        return detailPk;
    }
    
    /** Setter for property detailPk.
     * @param detailPk New value of property detailPk.
     *
     */
    public void setDetailPk(java.lang.Integer detailPk) {
        this.detailPk = detailPk;
    }
    
}



