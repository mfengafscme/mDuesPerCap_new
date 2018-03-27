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
import org.afscme.enterprise.affiliate.web.AffiliateFinderForm;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="councilAffiliationForm"
 */
public class CouncilAffiliationForm extends AffiliateFinderForm {
    
    /** Creates a new instance of CouncilAffiliationForm */
    public CouncilAffiliationForm() {
        this.init();
    }
    
// General Methods
    
    protected void init() {
        super.init();
    }
    
    public String toString() {
        return super.toString();
    }
    
// Struts methods...
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmptyOrSpaces(this.getAffIdCouncil())) {
            errors.add("affIdCouncil", new ActionError("error.field.required.generic", "Affiliate Identifier Council"));
        }
        if (TextUtil.isEmptyOrSpaces(this.getAffIdState())) {
            errors.add("affIdState", new ActionError("error.field.required.generic", "Affiliate Identifier State"));
        }
        if (TextUtil.isEmptyOrSpaces(this.getAffIdType())) {
            errors.add("affIdType", new ActionError("error.field.required.generic", "Affiliate Identifier Type"));
        }
        return errors;
    }
    
}
