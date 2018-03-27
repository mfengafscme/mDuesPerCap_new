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
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/searchPreAffiliate"
 *                  name="affiliateSearchForm"
 *                  validate="true"
 *                  scope="session"
 *                  input="/Membership/MinimumDuesPreAffiliateSearchResults.jsp"
 *
 *
 * @struts:action-forward   name="search" path="/Membership/MinimumDuesPreAffiliateSearchResults.jsp"
 */
public class SearchPreAffiliateAction extends AFSCMEAction {

    /** Creates a new instance of SearchAffiliateAction */
    public SearchPreAffiliateAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        AffiliateSearchForm asf = (AffiliateSearchForm)form;
        AffiliateCriteria ac = new AffiliateCriteria();
        ac.setAffiliateIdCouncil(asf.getAffIdCouncil());
        ac.setAffiliateIdLocal(asf.getAffIdLocal());
        ac.setAffiliateIdState(asf.getAffIdState());
        ac.setAffiliateIdSubUnit(asf.getAffIdSubUnit());

        //System.out.println(" -------------- " + asf.getEmployerNm());
		ac.setEmployerNm(asf.getEmployerNm());

        //if (asf.getAffIdType().toString().equalsIgnoreCase("T"))
        //    ac.setAffiliateIdType(null);
        //else
        //    ac.setAffiliateIdType(asf.getAffIdType());

        //ac.setIncludeSubUnits(asf.getIncludeSubUnits());
        //ac.setIncludeInactive(asf.getIncludeInactive());

        ac.setPage(asf.getPage());
        ac.setPageSize(asf.getPageSize());
        ac.setOrderBy(asf.getSortBy());
        ac.setOrdering(asf.getOrder());
        //asf.setTotal(s_maintainAffiliates.getSearchEmployerCount(ac));

        asf.setSearchAction("/searchPreAffiliate.action");

        Collection results = s_maintainAffiliates.searchPreAffiliates(ac);
		asf.setTotal(results.size());
        System.out.println(" ------- asf.getTotal ------- " + asf.getTotal());
        //System.out.println(" ------- results.size ------- " + results.size());

        //if ((results.size() < asf.getTotal()) && (results.size() < 100) && (ac.getEmployerNm() != null) && (ac.getEmployerNm().trim().length()>0))
        //	asf.setTotal(results.size());




        if (CollectionUtil.isEmpty(results)) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noResultsFound"));
            saveErrors(request, errors);
            asf.setResults(results);

            return mapping.getInputForward();
        //} else if (results.size() == 1 && asf.getTotal() == 1)  {
        //    AffiliateResult ar = (AffiliateResult)results.toArray()[0];
        //    setCurrentAffiliatePk(request, ar.getAffPk());
       //     return mapping.findForward("ViewAffiliateDetail");
        } else {
            asf.setResults(results);
            return mapping.findForward("search");
        }
    }

}
