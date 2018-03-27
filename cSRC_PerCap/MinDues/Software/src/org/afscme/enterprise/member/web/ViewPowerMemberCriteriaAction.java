package org.afscme.enterprise.member.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;

import org.afscme.enterprise.member.web.SearchMembersForm;

/**
 * Display the Power Member Search page, based on criteria in the searchMembersForm, if any exists.
 * However, reset page and total to zero in preparation of doing a new search
 *
 * @struts:action   path="/viewPowerMemberCriteria"
 *                  name="searchMembersForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/PowerMemberSearch.jsp"
 */
public class ViewPowerMemberCriteriaAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewPowerMemberCriteriaAction */
    public ViewPowerMemberCriteriaAction() {
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
        
        // Remove any current person - start over
        setCurrentPerson(request, null);
        
        SearchMembersForm mbrForm = (SearchMembersForm)form;
        
        mbrForm = (SearchMembersForm)request.getSession().getAttribute("searchMembersForm");
                
        //reset the page parms
        mbrForm.setPage(0);
        mbrForm.setTotal(0);
        // handled in ejb
        //  mbrForm.setSortBy("last_nm asc, first_nm asc, aff_councilRetiree_chap asc, aff_localSubChapter asc");
        
        request.getSession().setAttribute("searchMembersForm", mbrForm);
        return mapping.findForward("viewSearch");        
    }    
}
