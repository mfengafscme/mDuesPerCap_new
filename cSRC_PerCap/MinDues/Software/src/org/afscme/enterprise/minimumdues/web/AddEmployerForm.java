package org.afscme.enterprise.minimumdues.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;

/**
 * @struts:form name="AddEmployerForm"
 */
public class AddEmployerForm extends ActionForm {
    private String affIdType;
    private String affIdLocal;
    private String affIdState;
    private String affIdSubUnit;
    private String affIdCouncil;
    private String affilName;
    private String affIdStatus;
    private String addEmployerButton;

    /** Creates a new instance of AddEmployerForm */
    public AddEmployerForm() {
        this.init();
    }

    protected void init() {
		this.affIdType = "";
		this.affIdLocal = "";
		this.affIdState = "";
		this.affIdSubUnit = "";
		this.affIdCouncil = "";
		this.affilName = "";
    }

    // Struts Methods...
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

	    if (request.getParameter("addEmployerButton") != null) {
        	if (TextUtil.isEmptyOrSpaces(this.getAffIdState())) {
        	    errors.add("affIdState", new ActionError("error.field.required.generic", "Affiliate Identifier State"));
        	}
        	if (TextUtil.isEmptyOrSpaces(this.getAffIdCouncil())) {
        	    errors.add("affIdCouncil", new ActionError("error.field.required.generic", "Affiliate Identifier Council"));
        	}
        	//if (TextUtil.isEmptyOrSpaces(this.getAffIdLocal())) {
        	//     errors.add("affIdLocal", new ActionError("error.field.required.generic", "Affiliate Identifier Local"));
        	//}
        	if (TextUtil.isEmptyOrSpaces(this.getAffilName())) {
        	    errors.add("affilName", new ActionError("error.field.required.generic", "Employer Name"));
        	}

	        request.setAttribute("errors", errors);
		}

        return errors;
    }

    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public java.lang.String getAffIdType() {
        return affIdType;
    }

    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(java.lang.String affIdType) {
        this.affIdType = affIdType;
    }

    /** Getter for property affIdLocal.
     * @return Value of property affIdLocal.
     *
     */
    public java.lang.String getAffIdLocal() {
        return affIdLocal;
    }

    /** Setter for property affIdLocal.
     * @param affIdLocal New value of property affIdLocal.
     *
     */
    public void setAffIdLocal(java.lang.String affIdLocal) {
        this.affIdLocal = affIdLocal;
    }

    /** Getter for property affIdState.
     * @return Value of property affIdState.
     *
     */
    public java.lang.String getAffIdState() {
        return affIdState;
    }

    /** Setter for property affIdState.
     * @param affIdState New value of property affIdState.
     *
     */
    public void setAffIdState(java.lang.String affIdState) {
        this.affIdState = affIdState;
    }

    /** Getter for property affIdSubUnit.
     * @return Value of property affIdSubUnit.
     *
     */
    public java.lang.String getAffIdSubUnit() {
        return affIdSubUnit;
    }

    /** Setter for property affIdSubUnit.
     * @param affIdSubUnit New value of property affIdSubUnit.
     *
     */
    public void setAffIdSubUnit(java.lang.String affIdSubUnit) {
        this.affIdSubUnit = affIdSubUnit;
    }

    /** Getter for property affIdCouncil.
     * @return Value of property affIdCouncil.
     *
     */
    public java.lang.String getAffIdCouncil() {
        return affIdCouncil;
    }

    /** Setter for property affIdCouncil.
     * @param affIdCouncil New value of property affIdCouncil.
     *
     */
    public void setAffIdCouncil(java.lang.String affIdCouncil) {
        this.affIdCouncil = affIdCouncil;
    }

    /** Getter for property affilName.
     * @return Value of property affilName.
     *
     */
    public java.lang.String getAffilName() {
        return affilName;
    }

    /** Setter for property affilName.
     * @param affilName New value of property affilName.
     *
     */
    public void setAffilName(java.lang.String affilName) {
        this.affilName = affilName;
    }

    /** Getter for property addEmployerButton.
     * @return Value of property addEmployerButton.
     *
     */
    public java.lang.String getAddEmployerButton() {
        return addEmployerButton;
    }

    /** Setter for property addEmployerButton.
     * @param addEmployerButton New value of property addEmployerButton.
     *
     */
    public void setAddEmployerButton(java.lang.String addEmployerButton) {
        this.addEmployerButton = addEmployerButton;
    }
    
    /** Getter for property affIdStatus.
     * @return Value of property affIdStatus.
     *
     */
    public java.lang.String getAffIdStatus() {
        return affIdStatus;
    }
    
    /** Setter for property affIdStatus.
     * @param affIdStatus New value of property affIdStatus.
     *
     */
    public void setAffIdStatus(java.lang.String affIdStatus) {
        this.affIdStatus = affIdStatus;
    }
    
}
