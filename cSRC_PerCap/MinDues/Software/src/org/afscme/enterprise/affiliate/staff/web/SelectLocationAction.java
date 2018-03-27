
package org.afscme.enterprise.affiliate.staff.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Processing for the action to select a new Location to associate to 
 * either an affiliate staff or organization associate
 *
 * @struts:action   path="/selectLocation"
 *
 * @struts:action-forward   name="ViewStaff"  path="/viewAffiliateStaff.action" redirect="true"
 * @struts:action-forward   name="ViewAssociate"  path="/viewOrganizationAssociateDetail.action" redirect="true"
 */
public class SelectLocationAction extends AFSCMEAction {
	
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        //determine which type of person either affiliate staff or org associate
        String forward = request.getParameter("back");        
        if (forward.equals("AssociateDetail")) {
        
            //ORGANIZATION ASSOCIATE
            Integer personPk = getCurrentPersonPk(request);
            Integer orgPk = getCurrentOrganizationPk(request);
        
            s_maintainOrganizations.setOrgAssociateLocation(orgPk, personPk, Integer.valueOf(request.getParameter("pk")));
            
            return mapping.findForward("ViewAssociate");            
        }
        else {
            
            //AFFILIATE STAFF            
            Integer personPk = getCurrentPersonPk(request);
            Integer affPk = getCurrentAffiliatePk(request);
        
            s_maintainAffiliateStaff.setAffiliateStaffLocation(affPk, personPk, Integer.valueOf(request.getParameter("pk")));
            
            return mapping.findForward("ViewStaff");
        }
    }
}
