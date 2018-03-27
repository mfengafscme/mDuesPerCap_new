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
 * @struts:action   path="/viewPowerAffiliateCriteria"
 *                  name="affiliateSearchForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/PowerAffiliateSearch.jsp"
 */
public class ViewPowerAffiliateCriteriaAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewPowerAffiliateCriteriaAction */
    public ViewPowerAffiliateCriteriaAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        AffiliateSearchForm asf = (AffiliateSearchForm)form;
        if (asf.getAffIdType() == null) {
            asf.setAffIdType(new Character('L'));
        }
        if (asf.getIncludeSubUnits() == null) {
            asf.setIncludeSubUnits(new Boolean(false));
        }
        asf.setPage(0);
        asf.setTotal(0);
        asf.setSortBy(null);
        return mapping.findForward("viewSearch");
    }
    
}
