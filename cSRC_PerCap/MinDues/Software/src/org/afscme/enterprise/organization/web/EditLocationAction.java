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
 * Processing for Add/Edit Location pages
 *
 * @struts:action   path="/editLocation"
 *                  input="/Membership/LocationMaintenanceEdit.jsp"
 *                  name="locationForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/LocationMaintenanceEdit.jsp"
 * @struts:action-forward   name="Cancelled"  path="/Membership/LocationMaintenance.jsp"
 */
public class EditLocationAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        LocationForm locationForm = (LocationForm)form;
        
        // check if the orgLocationPK is empty
        if (locationForm.getPk() != null) {        

            // EDIT LOCATION
            Integer orgLocationPk = new Integer(locationForm.getPk().intValue());
     
            //when coming from process returned mail, the pk is location address pk 
            //instead of location pk (so figure out the correct location pk)
            if (locationForm.getBack().equals("ProcessReturnedMail")) {
                orgLocationPk = s_maintainOrgLocations.getOrgLocationPKForLocationAddress(orgLocationPk);
            }    
                
            //retrieve the org location detail for the pk
            LocationData data = s_maintainOrgLocations.getOrgLocation(orgLocationPk);
            
            // Set form fields from LocationData
            locationForm.setLocationData(data);
            locationForm.setIsAffiliatePk(s_maintainOrgLocations.isAffiliate(data.getOrgPk()));
            
            //set the current organization or affiliate
            if (locationForm.isAffiliatePk())
                setCurrentAffiliate(request, data.getOrgPk());
            else
                setCurrentOrganization(request, data.getOrgPk());
            
        }
        else {
            
            // ADD LOCATION
            if (request.getParameter("orgPK") != null) {
                Integer orgPk = new Integer(request.getParameter("orgPK"));
             
                // if add, get blank form and set the orgPK to determine where to add
                locationForm.setOrgPK(orgPk);
                locationForm.setIsAffiliatePk(s_maintainOrgLocations.isAffiliate(orgPk));
            }                
        }    

        return mapping.findForward("Edit");
    }
}
