package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.text.ParseException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateChangeCriteria;
import org.afscme.enterprise.affiliate.AffiliateChangeResult;

/**
 * @struts:form name="changeHistorySearchForm"
 */
public class ChangeHistorySearchForm extends SearchForm {

    private Integer affPk;
    private Integer section;
    private String changedFrom;
    private String changedTo;
    private boolean hasCriteria;
    
    /** Creates a new instance of ChangeHistorySearchForm */
    public ChangeHistorySearchForm() {
        this.init();
    }
    
   /** resets the search values to the default */
    public void newSearch() {
        sortBy= "";
        order=1;
        page=0;
        total=0;
    }
    
// Struts Methods...
    
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        if (allNull()) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noSearchValueEntered"));
            this.hasCriteria = true;
        }
        return errors;
    }
    
// General Methods...
    
    protected void init() {
        this.section = null;
        this.changedFrom = null;
        this.changedTo = null;
        this.hasCriteria = false;
    }
    
    private boolean allNull() {
        if (this.getSection() != null && this.getSection().intValue() > 0) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.getChangedFrom())) {
            return false;
        }
        if (!TextUtil.isEmptyOrSpaces(this.getChangedTo())) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", changedFrom = ");
        sb.append(this.changedFrom);
        sb.append(", changedTo = ");
        sb.append(this.changedTo);
        sb.append(", section = ");
        sb.append(this.section);
        sb.append(", results = ");
        sb.append(CollectionUtil.toString(this.results));
        sb.append(", hasCriteria = ");
        sb.append(this.hasCriteria);
        sb.append("]");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property changedFrom.
     * @return Value of property changedFrom.
     *
     */
    public String getChangedFrom() {
        return changedFrom;
    }
    
    /** Setter for property changedFrom.
     * @param changedFrom New value of property changedFrom.
     *
     */
    public void setChangedFrom(String changedFrom) {
        if (TextUtil.isEmptyOrSpaces(changedFrom)) {
            this.changedFrom = null;
        } else {
            this.changedFrom = changedFrom;
        }
    }
    
    /** Getter for property changedTo.
     * @return Value of property changedTo.
     *
     */
    public String getChangedTo() {
        return changedTo;
    }
    
    /** Setter for property changedTo.
     * @param changedTo New value of property changedTo.
     *
     */
    public void setChangedTo(String changedTo) {
        if (TextUtil.isEmptyOrSpaces(changedTo)) {
            this.changedTo = null;
        } else {
            this.changedTo = changedTo;
        }
    }
    
    /** Getter for property section.
     * @return Value of property section.
     *
     */
    public Integer getSection() {
        return section;
    }
    
    /** Setter for property section.
     * @param section New value of property section.
     *
     */
    public void setSection(Integer section) {
        if (section == null || section.intValue() < 1) {
            this.section = null;
        } else {
            this.section = section;
        }
    }
    
    /** Getter for property hasCriteria.
     * @return Value of property hasCriteria.
     *
     */
    public boolean isHasCriteria() {
        return hasCriteria;
    }
    
    /** Setter for property hasCriteria.
     * @param hasCriteria New value of property hasCriteria.
     *
     */
    public void setHasCriteria(boolean hasCriteria) {
        this.hasCriteria = hasCriteria;
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
        this.affPk = affPk;
    }
    
}
