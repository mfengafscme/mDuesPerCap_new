package org.afscme.enterprise.affiliate.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateChangeCriteria;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;

/** 
 * @struts:action   path="/searchAffiliateChangeHistory"
 *                  name="changeHistorySearchForm"
 *                  validate="true"
 *                  scope="session"
 *                  input="/Membership/AffiliateChangeHistory.jsp"
 *
 * @struts:action-forward   name="viewResults"  path="/Membership/AffiliateChangeHistory.jsp"
 */
public class SearchAffiliateChangeHistoryAction extends AFSCMEAction {
    
    /** Creates a new instance of SearchAffiliateChangeHistoryAction */
    public SearchAffiliateChangeHistoryAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ChangeHistorySearchForm chsf = (ChangeHistorySearchForm)form;
        Integer affPk = getCurrentAffiliatePk(request);
        
        // This is a new search
        if (request.getParameter("new") != null) {
            chsf.newSearch();
        }
        
        AffiliateChangeCriteria acc = new AffiliateChangeCriteria();
        acc.setAffPk(affPk);
        acc.setChangeDateFrom(DateUtil.getTimestamp(chsf.getChangedFrom()));
        acc.setChangeDateTo(DateUtil.getTimestamp(chsf.getChangedTo()));
        acc.setSectionCodePk(chsf.getSection());
        acc.setPage(chsf.getPage());
        acc.setPageSize(chsf.getPageSize());
        acc.setOrderBy(chsf.getSortBy());
        acc.setOrdering(chsf.getOrder());
        
        chsf.setHasCriteria(true);
        
        // Get the total search count
        chsf.setTotal(s_maintainAffiliates.getSearchChangeHistoryCount(acc));
        
        // Get the search result
        chsf.setResults(s_maintainAffiliates.searchChangeHistory(acc));
        if (CollectionUtil.isEmpty(chsf.getResults())) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noResultsFound"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        return mapping.findForward("viewResults");
    }    
}
