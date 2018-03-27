package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.organization.LocationData;

/**
 * @struts:action   path="/editAffiliateDetail"
 *                  name="affiliateDetailForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/MinimumDuesAffiliateDetailEdit.jsp"
 */
public class EditAffiliateDetailAction extends AFSCMEAction {

    /** Creates a new instance of EditAffiliateDetailAction */
    public EditAffiliateDetailAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        AffiliateDetailForm adf = (AffiliateDetailForm)form;
        if (adf.getAffPk() == null) {
            if (getCurrentAffiliatePk(request) == null) {
                throw new JspException("No primary key was specified in the session. User probably didn't follow the proper path to this screen.");
            }
            adf.setAffPk(getCurrentAffiliatePk(request));
        }
        AffiliateData data = s_maintainAffiliates.getAffiliateData(adf.getAffPk());

        // Set form fields from AffiliateData
        adf.setAffiliateData(data);
        adf.setComment(null);

        // get location using affPk
        LocationData location = s_maintainOrgLocations.getOrgPrimaryLocation(adf.getAffPk());
        if (location != null) {
            adf.setLocationPk(location.getOrgLocationPK());
            adf.setLocationData(location);
        }

        // Set New Affiliate ID Info Source fields.
        if (data.getNewAffiliateIDSourcePk() != null && data.getNewAffiliateIDSourcePk().intValue() > 0) {
            AffiliateData newAffId = s_maintainAffiliates.getAffiliateData(data.getNewAffiliateIDSourcePk());
            if (newAffId != null) {
                adf.setNewAffIdData(newAffId);
            }
        }

        return mapping.findForward("Edit");
    }

}
