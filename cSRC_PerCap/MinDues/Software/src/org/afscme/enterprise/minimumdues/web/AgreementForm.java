package org.afscme.enterprise.minimumdues.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import java.util.Collection;

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
import org.afscme.enterprise.common.web.SearchForm;


/**
 * @struts:form name="AgreementForm"
 */
public class AgreementForm extends SearchForm {

    private String agreementPk;
    private String agreementId;
    private String agreementName;
    private String startDate;
    private String endDate;
    private String comments;

    private String updateAgreementBtn;
    private String removeFromAgreementBtn;
    private String addToAgreementBtn;
    private String addNewAgreementBtn;
    private String deleteAgreementBtn;

    private Collection results1;
    private Collection results2;

    private String[] selectedItems1 = {};
    private String[] selectedItems2 = {};

    private String selectAgreement;

	private String[] agreementNames = {"",""};


    /** Creates a new instance of AgreementForm */
    public AgreementForm() {
        this.init();
    }

    protected void init() {
		this.agreementName = "";
    	this.startDate = "";
    	this.endDate = "";
    	this.comments = "";
    	this.results1 = null;
    	this.results2 = null;
    }

    // Struts Methods...
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }

    /*
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
	*/

	public String[] getAgreementNames() {
	    return this.agreementNames;
	}

	public void setAgreementNames(String agreementNames[]) {
	    this.agreementNames = agreementNames;
	}

    /** Getter for property agreementName.
     * @return Value of property agreementName.
     *
     */
    public java.lang.String getAgreementName() {
        return agreementName;
    }

    /** Setter for property agreementName.
     * @param agreementName New value of property agreementName.
     *
     */
    public void setAgreementName(java.lang.String agreementName) {
        this.agreementName = agreementName;
    }

    /** Getter for property startDate.
     * @return Value of property startDate.
     *
     */
    public java.lang.String getStartDate() {
        return startDate;
    }

    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     *
     */
    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }

    /** Getter for property endDate.
     * @return Value of property endDate.
     *
     */
    public java.lang.String getEndDate() {
        return endDate;
    }

    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     *
     */
    public void setEndDate(java.lang.String endDate) {
        this.endDate = endDate;
    }

    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public java.lang.String getComments() {
        return comments;
    }

    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }

    /** Getter for property updateAgreementBtn.
     * @return Value of property updateAgreementBtn.
     *
     */
    public java.lang.String getUpdateAgreementBtn() {
        return updateAgreementBtn;
    }

    /** Setter for property updateAgreementBtn.
     * @param updateAgreementBtn New value of property updateAgreementBtn.
     *
     */
    public void setUpdateAgreementBtn(java.lang.String updateAgreementBtn) {
        this.updateAgreementBtn = updateAgreementBtn;
    }

    /** Getter for property removeFromAgreementBtn.
     * @return Value of property removeFromAgreementBtn.
     *
     */
    public java.lang.String getRemoveFromAgreementBtn() {
        return removeFromAgreementBtn;
    }

    /** Setter for property removeFromAgreementBtn.
     * @param removeFromAgreementBtn New value of property removeFromAgreementBtn.
     *
     */
    public void setRemoveFromAgreementBtn(java.lang.String removeFromAgreementBtn) {
        this.removeFromAgreementBtn = removeFromAgreementBtn;
    }

    /** Getter for property addToAgreementBtn.
     * @return Value of property addToAgreementBtn.
     *
     */
    public java.lang.String getAddToAgreementBtn() {
        return addToAgreementBtn;
    }

    /** Setter for property addToAgreementBtn.
     * @param addToAgreementBtn New value of property addToAgreementBtn.
     *
     */
    public void setAddToAgreementBtn(java.lang.String addToAgreementBtn) {
        this.addToAgreementBtn = addToAgreementBtn;
    }

    /** Getter for property addNewAgreementBtn.
     * @return Value of property addNewAgreementBtn.
     *
     */
    public java.lang.String getAddNewAgreementBtn() {
        return addNewAgreementBtn;
    }

    /** Setter for property addNewAgreementBtn.
     * @param addNewAgreementBtn New value of property addNewAgreementBtn.
     *
     */
    public void setAddNewAgreementBtn(java.lang.String addNewAgreementBtn) {
        this.addNewAgreementBtn = addNewAgreementBtn;
    }

    /** Getter for property deleteAgreementBtn.
     * @return Value of property deleteAgreementBtn.
     *
     */
    public java.lang.String getDeleteAgreementBtn() {
        return deleteAgreementBtn;
    }

    /** Setter for property deleteAgreementBtn.
     * @param deleteAgreementBtn New value of property deleteAgreementBtn.
     *
     */
    public void setDeleteAgreementBtn(java.lang.String deleteAgreementBtn) {
        this.deleteAgreementBtn = deleteAgreementBtn;
    }

    /** Getter for property results1.
     * @return Value of property results1.
     *
     */
    public java.util.Collection getResults1() {
        return results1;
    }

    /** Setter for property results1.
     * @param results1 New value of property results1.
     *
     */
    public void setResults1(java.util.Collection results1) {
        this.results1 = results1;
    }

    /** Getter for property results2.
     * @return Value of property results2.
     *
     */
    public java.util.Collection getResults2() {
        return results2;
    }

    /** Setter for property results2.
     * @param results2 New value of property results2.
     *
     */
    public void setResults2(java.util.Collection results2) {
        this.results2 = results2;
    }

    /** Getter for property selectedItems1.
     * @return Value of property selectedItems1.
     *
     */
    public java.lang.String[] getSelectedItems1() {
        return this.selectedItems1;
    }

    /** Setter for property selectedItems1.
     * @param selectedItems1 New value of property selectedItems1.
     *
     */
    public void setSelectedItems1(java.lang.String[] selectedItems1) {
        this.selectedItems1 = selectedItems1;
    }

    /** Getter for property selectedItems2.
     * @return Value of property selectedItems2.
     *
     */
    public java.lang.String[] getSelectedItems2() {
        return this.selectedItems2;
    }

    /** Setter for property selectedItems2.
     * @param selectedItems2 New value of property selectedItems2.
     *
     */
    public void setSelectedItems2(java.lang.String[] selectedItems2) {
        this.selectedItems2 = selectedItems2;
    }

    /** Getter for property selectAgreement.
     * @return Value of property selectAgreement.
     *
     */
    public java.lang.String getSelectAgreement() {
        return selectAgreement;
    }

    /** Setter for property selectAgreement.
     * @param selectAgreement New value of property selectAgreement.
     *
     */
    public void setSelectAgreement(java.lang.String selectAgreement) {
        this.selectAgreement = selectAgreement;
    }

    /** Getter for property agreementPk.
     * @return Value of property agreementPk.
     *
     */
    public java.lang.String getAgreementPk() {
        return agreementPk;
    }

    /** Setter for property agreementPk.
     * @param agreementPk New value of property agreementPk.
     *
     */
    public void setAgreementPk(java.lang.String agreementPk) {
        this.agreementPk = agreementPk;
    }

    /** Getter for property agreementId.
     * @return Value of property agreementId.
     *
     */
    public java.lang.String getAgreementId() {
        return agreementId;
    }
    
    /** Setter for property agreementId.
     * @param agreementId New value of property agreementId.
     *
     */
    public void setAgreementId(java.lang.String agreementId) {
        this.agreementId = agreementId;
    }
    
}
