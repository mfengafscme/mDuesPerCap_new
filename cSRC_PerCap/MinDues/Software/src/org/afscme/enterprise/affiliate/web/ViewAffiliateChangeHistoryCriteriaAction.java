package org.afscme.enterprise.affiliate.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/** 
 * @struts:action   path="/viewAffiliateChangeHistoryCriteria"
 *                  name="changeHistorySearchForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/AffiliateChangeHistory.jsp"
 */
public class ViewAffiliateChangeHistoryCriteriaAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewAffiliateChangeCriteriaAction */
    public ViewAffiliateChangeHistoryCriteriaAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ChangeHistorySearchForm chsf = (ChangeHistorySearchForm)form;
        chsf.setHasCriteria(false);
        chsf.setChangedFrom(null);
        chsf.setChangedTo(null);
        chsf.setSection(null);
        chsf.setResults(null);
        return mapping.findForward("viewSearch");
    }
    
}
