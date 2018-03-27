package org.afscme.enterprise.person.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;

/**
 * Display the Basic Person Search page, based on criteria in the searchPersonForm, if any already exists.
 * Put reset page and total to 0 in preparation for a new search. Also set current person to null , as the
 * user is now starting over
 *
 *
 * @struts:action   path="/viewBasicPersonCriteria"
 *                  name="searchPersonForm"
 *                  validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/BasicPersonSearch.jsp"
 */
public class ViewBasicPersonCriteriaAction extends AFSCMEAction {

    /** Creates a new instance of ViewBasicMemberCriteriaAction */
    public ViewBasicPersonCriteriaAction() {
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
        setCurrentPerson(request,  null);
        
        SearchPersonForm personForm = (SearchPersonForm)form;
        
        if (request.getParameter("new") != null) {
             clear(request);
        }
        
        //log.debug("ViewBasicCriteriaAction: form is " + personForm.toString());

        //reset the page parms
        personForm.setPage(0);
        personForm.setTotal(0);
        personForm.setSortBy("");
        
        return mapping.findForward("viewSearch");
    
    }
        public void clear(HttpServletRequest req) {
               req.getSession().removeAttribute("searchPersonForm");
               SearchPersonForm pf = new SearchPersonForm();
               pf.setSearchPage(0); 
               req.getSession().setAttribute("searchPersonForm", pf);
        }
        
    

}
