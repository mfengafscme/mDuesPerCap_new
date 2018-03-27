package org.afscme.enterprise.organization.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * @struts:action   path="/viewLocationInformation"
 *                  name="locationListForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Membership/LocationMaintenance.jsp"
 */
public class ViewLocationInformationAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        Integer orgPk = null;        
        if (request.getParameter("orgPK") != null) {
            Integer requestOrgPk = new Integer(request.getParameter("orgPK"));
            if (s_maintainOrgLocations.isAffiliate(requestOrgPk)) {
                orgPk = getCurrentAffiliatePk(request);
            }
            else {
                orgPk = getCurrentOrganizationPk(request, "orgPK");
            }
        }
        else {
            //if not a parameter in the request, get it from the request attribute 
            //set in the delete action only
            orgPk = (Integer) request.getAttribute("orgPK");
            
            //cannot figure out orgPk from anywhere so throw the exception
            if (orgPk == null) 
                throw new JspException("No orgPK was specified in the request. User probably didn't follow the proper path to this screen.");
        }

        LocationListForm locListForm = (LocationListForm)form;

        //retrieve the locations for an organization by pk
        List data = s_maintainOrgLocations.getOrgLocations(orgPk);

        //set form fields from collection of locations
        locListForm.setLocations(data);
        locListForm.setPrimaryLocation(s_maintainOrgLocations.getOrgPrimaryLocation(orgPk));
        locListForm.setOrgPK(orgPk);
        locListForm.setIsAffiliatePk(s_maintainOrgLocations.isAffiliate(orgPk));        
        
        return mapping.findForward("View");
    }
}
