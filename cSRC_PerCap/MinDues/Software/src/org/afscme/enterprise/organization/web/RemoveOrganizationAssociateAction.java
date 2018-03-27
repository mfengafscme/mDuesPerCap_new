package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Processing for Remove Organization Associate action
 *
 * @struts:action   path="/removeOrganizationAssociate"
 *
 * @struts:action-forward   name="Done"  path="/viewOrganizationAssociateList.action" redirect="true"
 */
public class RemoveOrganizationAssociateAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        //retrieve the orgPK
        Integer orgPk = getCurrentOrganizationPk(request, "orgPK");
        Integer personPk = Integer.valueOf(request.getParameter("personPk"));
        s_maintainOrganizations.removePersonAssoc(orgPk, personPk);
        
        return mapping.findForward("Done");
    }
}
