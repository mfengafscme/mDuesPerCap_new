package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.util.TextUtil;


/**
 * Represents the form for detail of a Participation Group, Type, Detail 
 * or Outcome
 *
 * @struts:form name="participationCodeForm"
 */
public class ParticipationCodeForm extends ActionForm {

    protected Integer groupPk;    
    protected Integer pk;
    protected String name;
    protected String description;
    
    protected String groupNm;
    protected String typeNm;
    protected String detailNm;

    /**
     * constructor to set up values
     */
    public ParticipationCodeForm() {
    }

    /**
     * toString method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("groupPk: " + groupPk);
        buf.append(", pk: " + pk);
        buf.append(", name: " + name);
        buf.append(", description: " + description);
        return buf.toString()+"]";
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
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /** Validation method for this form
     *
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        if (name == null)
            return null; //form is new

        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmpty(name)) {
            errors.add("name", new ActionError("error.field.required.generic", "Name"));
        }
        if (TextUtil.isEmpty(description)) {
            errors.add("description", new ActionError("error.field.required.generic", "Description"));
        }

        return errors;
    }
    
    /** Getter for property detailNm.
     * @return Value of property detailNm.
     *
     */
    public java.lang.String getDetailNm() {
        return detailNm;
    }
    
    /** Setter for property detailNm.
     * @param detailNm New value of property detailNm.
     *
     */
    public void setDetailNm(java.lang.String detailNm) {
        this.detailNm = detailNm;
    }
    
    /** Getter for property groupNm.
     * @return Value of property groupNm.
     *
     */
    public java.lang.String getGroupNm() {
        return groupNm;
    }
    
    /** Setter for property groupNm.
     * @param groupNm New value of property groupNm.
     *
     */
    public void setGroupNm(java.lang.String groupNm) {
        this.groupNm = groupNm;
    }
    
    /** Getter for property typeNm.
     * @return Value of property typeNm.
     *
     */
    public java.lang.String getTypeNm() {
        return typeNm;
    }
    
    /** Setter for property typeNm.
     * @param typeNm New value of property typeNm.
     *
     */
    public void setTypeNm(java.lang.String typeNm) {
        this.typeNm = typeNm;
    }
    
}



