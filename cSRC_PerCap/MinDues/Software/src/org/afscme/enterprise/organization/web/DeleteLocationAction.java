package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.LocationData;


/**
 * Processing for Delete Location action
 *
 * @struts:action   path="/deleteLocation"
 *
 * @struts:action-forward   name="Done"  path="/viewLocationInformation.action"
 */
public class DeleteLocationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response, UserSecurityData usd) 
    throws Exception {
        
        Integer orgLocationPk = Integer.valueOf(request.getParameter("pk"));
        
        LocationData location = s_maintainOrgLocations.getOrgLocation(orgLocationPk);
        Integer orgPk = location.getOrgPk(); 

        s_maintainOrgLocations.removeOrgLocation(orgLocationPk, orgPk, usd.getPersonPk());

        //needed to set orgPK to return to view Location 
        request.setAttribute("orgPK", orgPk);       
        
        return mapping.findForward("Done");
    }
}
