package org.afscme.enterprise.member.web;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.member.web.SearchMembersForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.member.MemberCriteria;
import org.afscme.enterprise.member.MemberResult;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.member.MemberData;


/**
 * Searches for vendor members, based on criteria in the SearchVendorMembersForm.
 * Params:
 *      new - if present, the user is shown the search form.
 *
 * @struts:action   path="/searchVendorMembers"
 *                  input="/Common/VendorMemberSearch.jsp?noMain=true"
 *                  name="searchVendorMembersForm"
 *                  validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="VendorMemberSearch" path="/Common/VendorMemberSearch.jsp?noMain=true"
 */
public class SearchVendorMembersAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        SearchVendorMembersForm vendorMbrForm = (SearchVendorMembersForm)form;

        // This is a new search
        if (request.getParameter("newSearch") != null) {
            vendorMbrForm.newSearch();
        }
                
        //get the search criteria from the form
        MemberCriteria criteria = vendorMbrForm.getVendorMemberCriteriaData();
        vendorMbrForm.setHasCriteria(true);
        ArrayList results = new ArrayList();
                
        //perform the search
        int count = s_maintainMembers.searchVendorMembers(criteria, results);
        
        //put the search result in the form
        vendorMbrForm.setTotal(count);
        vendorMbrForm.setResults(results);
        return mapping.findForward("VendorMemberSearch");
    }
}
