package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="affiliateFinderForm"
 */
public class AffiliateFinderForm extends SearchForm {
    
    private Character affIdType;
    private Character affIdCode;
    private String affIdLocal;
    private String affIdState;
    private String affIdSubUnit;
    private String affIdCouncil;
    private Integer affPk;
    private boolean finder;
    private String linkAction;
    private String cancelAction;
    
    /** Creates a new instance of AffiliateFinderForm */
    public AffiliateFinderForm() {
        this.init();
    }
    
// Struts methods...
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (allNull()) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noSearchValueEntered"));
        }
        return errors;
    }
    
// General methods...
    
    protected void init() {
        this.affIdType = null;
        this.affIdCode = null;
        this.affIdLocal = null;
        this.affIdState = null;
        this.affIdSubUnit = null;
        this.affIdCouncil = null;
        this.affPk = null;
        this.finder = false;
    }
    
    public boolean allNull() {
        if (!TextUtil.isEmptyOrSpaces(this.affIdType)) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdCode)) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdLocal)) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdState)) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdSubUnit)) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdCouncil)) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("AffiliateFinderForm {");
        sb.append("affIdType = ");sb.append(this.affIdType);
        sb.append(", affIdLocal = ");sb.append(this.affIdLocal);
        sb.append(", affIdState = ");sb.append(this.affIdState);
        sb.append(", affIdSubUnit = ");sb.append(this.affIdSubUnit);
        sb.append(", affIdCouncil = ");sb.append(this.affIdCouncil);
        sb.append(", affIdCode = ");sb.append(this.affIdCode);
        sb.append(", page = ");sb.append(this.page);
        sb.append(", order = ");sb.append(this.order);
        sb.append(", resultsCount = ");sb.append(((this.results == null) ? "null" : ""+this.results.size()));
        sb.append(", page = ");sb.append(this.page);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter and Setter methods...
    
    public AffiliateCriteria getCriteria() {
        AffiliateCriteria ac = new AffiliateCriteria();
        ac.setAffiliateIdCouncil(this.affIdCouncil);
        ac.setAffiliateIdLocal(this.affIdLocal);
        ac.setAffiliateIdState(this.affIdState);
        ac.setAffiliateIdSubUnit(this.affIdSubUnit);
        ac.setAffiliateIdType(this.affIdType);
        ac.setAffiliateIdCode(this.affIdCode);
        return ac;
    }
    
    /** Getter for property affIdCode.
     * @return Value of property affIdCode.
     *
     */
    public Character getAffIdCode() {
        return affIdCode;
    }
    
    /** Setter for property affIdCode.
     * @param affIdCode New value of property affIdCode.
     *
     */
    public void setAffIdCode(Character affIdCode) {
        if (TextUtil.isEmptyOrSpaces(affIdCode)) {
            this.affIdCode = null;
        } else {
            this.affIdCode = affIdCode;
        }
    }
    
    /** Getter for property affIdCouncil.
     * @return Value of property affIdCouncil.
     *
     */
    public String getAffIdCouncil() {
        return affIdCouncil;
    }
    
    /** Setter for property affIdCouncil.
     * @param affIdCouncil New value of property affIdCouncil.
     *
     */
    public void setAffIdCouncil(String affIdCouncil) {
        if (TextUtil.isEmptyOrSpaces(affIdCouncil)) {
            this.affIdCouncil = null;
        } else {
            this.affIdCouncil = affIdCouncil;
        }
    }
    
    /** Getter for property affIdLocal.
     * @return Value of property affIdLocal.
     *
     */
    public String getAffIdLocal() {
        return affIdLocal;
    }
    
    /** Setter for property affIdLocal.
     * @param affIdLocal New value of property affIdLocal.
     *
     */
    public void setAffIdLocal(String affIdLocal) {
        if (TextUtil.isEmptyOrSpaces(affIdLocal)) {
            this.affIdLocal = null;
        } else {
            this.affIdLocal = affIdLocal;
        }
    }
    
    /** Getter for property affIdState.
     * @return Value of property affIdState.
     *
     */
    public String getAffIdState() {
        return affIdState;
    }
    
    /** Setter for property affIdState.
     * @param affIdState New value of property affIdState.
     *
     */
    public void setAffIdState(String affIdState) {
        if (TextUtil.isEmptyOrSpaces(affIdState)) {
            this.affIdState = null;
        } else {
            this.affIdState = affIdState;
        }
    }
    
    /** Getter for property affIdSubUnit.
     * @return Value of property affIdSubUnit.
     *
     */
    public String getAffIdSubUnit() {
        return affIdSubUnit;
    }
    
    /** Setter for property affIdSubUnit.
     * @param affIdSubUnit New value of property affIdSubUnit.
     *
     */
    public void setAffIdSubUnit(String affIdSubUnit) {
        if (TextUtil.isEmptyOrSpaces(affIdSubUnit)) {
            this.affIdSubUnit = null;
        } else {
            this.affIdSubUnit = affIdSubUnit;
        }
    }
    
    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public Character getAffIdType() {
        return affIdType;
    }
    
    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(Character affIdType) {
        if (TextUtil.isEmptyOrSpaces(affIdType)) {
            this.affIdType = null;
        } else {
            this.affIdType = affIdType;
        }
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        if (affPk == null || affPk.intValue() < 1) {
            this.affPk = null;
        } else {
            this.affPk = affPk;
        }
    }
    
    /** Getter for property finder.
     * @return Value of property finder.
     *
     */
    public boolean isFinder() {
        return finder;
    }
    
    /** Setter for property finder.
     * @param finder New value of property finder.
     *
     */
    public void setFinder(boolean finder) {
        this.finder = finder;
    }
    
    /** Getter for property cancelAction.
     * @return Value of property cancelAction.
     *
     */
    public String getCancelAction() {
        return cancelAction;
    }
    
    /** Setter for property cancelAction.
     * @param cancelAction New value of property cancelAction.
     *
     */
    public void setCancelAction(String cancelAction) {
        if (TextUtil.isEmptyOrSpaces(cancelAction)) {
            this.cancelAction = null;
        } else {
            this.cancelAction = cancelAction;
        }
    }
    
    /** Getter for property linkAction.
     * @return Value of property linkAction.
     *
     */
    public String getLinkAction() {
        return linkAction;
    }
    
    /** Setter for property linkAction.
     * @param linkAction New value of property linkAction.
     *
     */
    public void setLinkAction(String linkAction) {
        if (TextUtil.isEmptyOrSpaces(linkAction)) {
            this.linkAction = null;
        } else {
            this.linkAction = linkAction;
        }
    }
    
}
