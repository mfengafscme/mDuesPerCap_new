package org.afscme.enterprise.organization.web;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Display the list of Organization Associates for an external organizations
 *
 * @struts:action   path="/viewOrganizationAssociateList"
 *                  input="/Membership/OrganizationAssociateMaintenance.jsp"
 *                  name="organizationAssociateListForm"
 *                  validate="true"
 *                  scope="session"
 *
 * @struts:action-forward name="OrgAssociateResults" path="/Membership/OrganizationAssociateMaintenance.jsp"
 */
public class ViewOrganizationAssociateListAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        //retrieve the orgPK
        Integer orgPk = getCurrentOrganizationPk(request, "orgPK");
      
        OrganizationAssociateListForm oalForm = (OrganizationAssociateListForm)form;
        List results = new LinkedList();
        
        //if the request contained the 'new' parameter, reset the sort order to the default
        if (request.getParameter("new") != null) {
            oalForm.newList();
        }

        //perform the retrieve of org associate for the external org
        int total = s_maintainOrganizations.getOrgAssociates(orgPk, oalForm.getSortData(), results);

        //put the results in the form
        oalForm.setResults(results);
        oalForm.setTotal(total);
        
        // remove attributes that were set before when starting new
        HttpSession session = request.getSession();
        session.removeAttribute("verifyPersonForm");
        session.removeAttribute("organizationAssociateDetailForm");
        
        return mapping.findForward("OrgAssociateResults");
    }
}
