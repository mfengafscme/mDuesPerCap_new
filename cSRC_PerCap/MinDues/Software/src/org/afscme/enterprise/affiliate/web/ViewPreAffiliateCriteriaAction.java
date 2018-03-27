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
import org.afscme.enterprise.minimumdues.*;

/**
 * @struts:action   path="/viewPreAffiliateCriteria"
 *                  name="affiliateSearchForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/MinimumDuesPreAffiliateSearch.jsp"
 */
public class ViewPreAffiliateCriteriaAction extends AFSCMEAction {

    /** Creates a new instance of ViewPreAffiliateCriteriaAction */
    public ViewPreAffiliateCriteriaAction() {
    }

    /**
     * Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        AffiliateSearchForm asf = (AffiliateSearchForm)form;
        if (request.getParameter("new") != null) {
            clear(request);
        }
        asf.setPage(0);
        asf.setTotal(0);
        asf.setSortBy(null);
        return mapping.findForward("viewSearch");
    }

    public void clear(HttpServletRequest req) {
        req.getSession().removeAttribute("affiliateSearchForm");
        AffiliateSearchForm asf = new AffiliateSearchForm();
        //asf.setAffIdType(new Character('L'));
        asf.setIncludeSubUnits(new Boolean(false));
        req.getSession().setAttribute("affiliateSearchForm", asf);
    }

}
