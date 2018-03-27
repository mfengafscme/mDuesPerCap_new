package org.afscme.enterprise.affiliate.staff.web;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrganizationAssociateData;


/**
 * Processing for the display of Location Selection action
 *
 * @struts:action   path="/viewLocationSelection"
 *
 * @struts:action-forward   name="View"  path="/Membership/LocationSelection.jsp"
 */
public class ViewLocationSelectionAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        LocationSelection locationSelection = new LocationSelection();

        //check if affiliate staff or organization associate
        String forward = request.getParameter("back");
        Integer locationPk = null;
        List locations = null;

        if (forward.equals("AssociateDetail")) {
            
            //process organization associate
            Integer personPk = getCurrentPersonPk(request, "personPk");
            Integer orgPk = getCurrentOrganizationPk(request, "orgPK");

            locations = s_maintainOrgLocations.getOrgLocations(orgPk);
            OrganizationAssociateData orgAssocData = s_maintainOrganizations.getOrgAssociateDetail(orgPk, personPk);
            locationPk = orgAssocData.getLocationPk();
        }
        else {
            
            //process affiliate staff
            Integer personPk = getCurrentPersonPk(request, "personPk");
            Integer affPk = getCurrentAffiliatePk(request, "affPk");

            locations = s_maintainOrgLocations.getOrgLocations(affPk);
            StaffData staffData = s_maintainAffiliateStaff.getAffiliateStaff(affPk, personPk);
            locationPk = staffData.getLocationPk();
        }
        
        //put the selected one first
        Iterator it = locations.iterator();
        LocationData ld = null;
        while (it.hasNext()) {
            ld = (LocationData)it.next();
            if (ld.getOrgLocationPK().equals(locationPk)) {
                it.remove();
                break;
            } else {
                ld = null;
            }
        }
        if (ld != null)
            locations.add(0, ld);
            
        // Set form fields from collection of locations
        locationSelection.setLocations(locations);
        locationSelection.setCurrentLocation(locationPk);

        //set Cancel button action
        if (forward.equals("StaffDetail"))
            locationSelection.setReturnAction("/viewAffiliateStaff.action");
        else if (forward.equals("AssociateDetail"))  
            locationSelection.setReturnAction("/viewOrganizationAssociateDetail.action");
        else
            throw new ServletException("Invalid back parameter " + forward + " passed to viewLocationSelection.action");
        
        request.setAttribute("locationSelection", locationSelection);
        return mapping.findForward("View");
 }

}
