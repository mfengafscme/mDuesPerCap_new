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
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.affiliate.AffiliateResult;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

/** 
 * @struts:action   path="/searchPowerAffiliate"
 *                  name="affiliateSearchForm"
 *                  validate="true"
 *                  scope="session"
 *                  input="/Membership/PowerAffiliateSearch.jsp"
 *
 * @struts:action-forward   name="search" path="/Membership/AffiliateSearchResults.jsp"
 */
public class SearchPowerAffiliateAction extends AFSCMEAction {
    
    /** Creates a new instance of SearchPowerAffiliateAction */
    public SearchPowerAffiliateAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        AffiliateSearchForm asf = (AffiliateSearchForm)form;
        AffiliateCriteria ac = asf.getCriteria();
        ac.setPage(asf.getPage());
        ac.setPageSize(asf.getPageSize());
        ac.setOrderBy(asf.getSortBy());
        ac.setOrdering(asf.getOrder());
        asf.setTotal(s_maintainAffiliates.getSearchAffiliatesCount(ac));
        asf.setSearchAction("/searchPowerAffiliate.action");
        
        Collection results = s_maintainAffiliates.searchAffiliates(ac);
        if (CollectionUtil.isEmpty(results)) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noResultsFound"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } else if (results.size() == 1 && asf.getTotal() == 1)  {
            AffiliateResult ar = (AffiliateResult)results.toArray()[0];
            setCurrentAffiliatePk(request, ar.getAffPk());
            return mapping.findForward("ViewAffiliateDetail");
        } else {
            asf.setResults(results);
            return mapping.findForward("search");
        }
    }
    
}
