package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.RequestUtils;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * @struts:action   path="/searchAffiliateFinder"
 *                  name="affiliateFinderForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="showPopup" path="/Membership/AffiliateIdentifierFinderResults.jsp"
 * @struts:action-forward   name="noPopup" path="/Membership/DuplicateAffiliateIdentifierNotifier.jsp"
 */
public class SearchAffiliateFinderAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    /** Creates a new instance of SearchAffiliateFinderAction */
    public SearchAffiliateFinderAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        AffiliateFinderForm aff = (AffiliateFinderForm)form;
        if (request.getParameter("new") != null) {
            log.debug("Reseting page requirements.");
            aff.setPage(0);
            aff.setResults(null);
            aff.setSortBy(null);
            aff.setTotal(0);
        } else log.debug("Leaving page requirements as is.");

        AffiliateCriteria ac = new AffiliateCriteria();
        ac.setAffiliateIdType(aff.getAffIdType());
        ac.setAffiliateIdLocal(aff.getAffIdLocal());
        ac.setAffiliateIdState(aff.getAffIdState());
        ac.setAffiliateIdSubUnit(aff.getAffIdSubUnit());
        ac.setAffiliateIdCouncil(aff.getAffIdCouncil());
        ac.setPage(aff.getPage());
        ac.setPageSize(aff.getPageSize());
        ac.setOrderBy(aff.getSortBy());
        ac.setOrdering(aff.getOrder());
        ac.setIncludeInactive(new Boolean(false));

        // restrict affiliate search to subs
        ac.setParentAffFk(usd.getActingAsAffiliate());
        if (aff.getTotal() < 1) {
            aff.setTotal(s_maintainAffiliates.getSearchAffiliatesCount(ac));
        }
        Collection results = s_maintainAffiliates.searchAffiliates(ac);
        if (CollectionUtil.isEmpty(results)) {
            ActionErrors errors = new ActionErrors();
            if (usd.isActingAsAffiliate()) {
                // HLM: Fix defect #136. Display this message if VDU
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.vdu.invalidAffiliate"));
            } else {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noResultsFound"));
            }
            saveErrors(request, errors);
        } else {
            aff.setResults(results);
        }
        if (aff.isFinder()) {
            return mapping.findForward("showPopup");
        }
        return mapping.findForward("noPopup");
    }

}