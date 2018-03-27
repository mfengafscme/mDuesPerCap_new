package org.afscme.enterprise.member.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;

/**
 * Display the Basic Member Search page, based on criteria in the searchMembersForm, if any already exists.
 * But reset page and total to 0 in preparation for a new search. Also set current person to null , as the
 * user is now starting over
 *
 *
 * @struts:action   path="/viewBasicMemberCriteria"
 *                  name="searchMembersForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="viewSearch"  path="/Membership/BasicMemberSearch.jsp"
 */
public class ViewBasicMemberCriteriaAction extends AFSCMEAction {

    /** Creates a new instance of ViewBasicMemberCriteriaAction */
    public ViewBasicMemberCriteriaAction() {
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

        SearchMembersForm mbrForm = (SearchMembersForm)form;

        Integer affPk = usd.getActingAsAffiliate();
        if (affPk != null) {
            mbrForm.setVduAffiliates(usd.getAccessibleAffiliates());
            log.debug("ViewBasicMemberCriteriaAction - affPk set from usd.getActingAsAffiliate not null : " + affPk +
            " and usd.getAccessibleAffiliates is : " + usd.getAccessibleAffiliates());
        }
        
        if (request.getParameter("new") != null) {
            clear(request);
        }else{
            mbrForm = (SearchMembersForm)request.getSession().getAttribute("searchMembersForm");
        }

        // log.debug("ViewBasicCriteriaAction: form is " + mbrForm.toString());

        //reset the page parms
        mbrForm.setPage(0);
        mbrForm.setTotal(0);
        mbrForm.setSortBy("");

        request.getSession().setAttribute("searchMembersForm", mbrForm);
        return mapping.findForward("viewSearch");

    }
        public void clear(HttpServletRequest req) {
               req.getSession().removeAttribute("searchMembersForm");
               SearchMembersForm mf = new SearchMembersForm();
               req.getSession().setAttribute("searchMembersForm", mf);
        }



}
