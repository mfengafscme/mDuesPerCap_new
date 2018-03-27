package org.afscme.enterprise.controller.web;

import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;




/**
 * Performs a search of affiliates associated with a user.
 *
 * @struts:action   path="/selectAffiliateSearch"
 *                  input="/Common/SelectAffiliateSearch.jsp"
 *                  name="selectAffiliateSearchForm"
 *                  validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="SearchForm" path="/Common/SelectAffiliateSearch.jsp"
 * @struts:action-forward name="SearchResults" path="/Common/SelectAffiliateSearchResults.jsp"
 *
 */
public class SelectAffiliateSearchAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

            SelectAffiliateSearchForm sasForm = (SelectAffiliateSearchForm)form;
            
            if (request.getParameter("cancel") != null) {                                     
                String cancelString = "/" + request.getParameter("cancel") + ".action";
                if (request.getParameter("pk") != null)
                    cancelString += "?pk=" + request.getParameter("pk");                
                sasForm.setCancel(cancelString);
            }

            if (request.getParameter("submit") != null) {
                sasForm.setCancel("/selectAffiliateSearch.action");                                             
            }
            
                      
            request.setAttribute("searchForm", sasForm);            

            if (request.getParameter("new") != null) {           
                // new parameter was given, this means the user has just selected to
                // go to the data utility, but hasn't searched for an affiliate yet
                int affiliateCount = s_maintainUsers.getAffiliateCount(usd.getPersonPk());
                
                if (affiliateCount == 1) {
                    //The user only has 1 affiliate.
                    //Don't bother searching, just use that one.
                    Integer affiliatePk = (Integer)s_maintainUsers.getAffiliates(usd.getPersonPk()).iterator().next();
                    return new ActionForward("/selectAffiliate.action?pk="+affiliatePk);
                } else 
                    if (affiliateCount > sasForm.getPageSize()) {
                        //The user has <= 1 page of affiliates.
                        //Don't bother with the search parameters form, just show the 1 page of results.                     
                        return mapping.findForward("SearchForm");
                    } else {
			sasForm.setType(null);
			sasForm.setShowNewSearch("NO");			
                    }
            }   
            
            		
            //perform the search.
            AffiliateData data = sasForm.getAffiliateData();
            AffiliateSortData sortData = sasForm.getAffiliateSortData();
            List result = new LinkedList();
            int count = s_maintainUsers.getAffiliates(usd.getPersonPk(), data, sortData, result);
        
            if (count == 0) {
                //no results - go back to the search form
                return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.noResults");
            } else 
                if (count == 1) {
                    //1 result - select that affiliate
                    Integer affiliatePk = ((AffiliateData)result.get(0)).getPk();
                    return new ActionForward("/selectAffiliate.action?pk="+affiliatePk);

                } else {
                    //multiple results - display the results
                    sasForm.setResults(result);
                    sasForm.setTotal(count);
                    return mapping.findForward("SearchResults");
                }
	}
}

