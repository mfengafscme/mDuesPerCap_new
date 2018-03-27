package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/viewAffiliateDetail"
 *
 * @struts:action-forward   name="View"  path="/Membership/AffiliateChooseAdd.jsp"
 */
public class ViewAffiliateDetailAction extends AFSCMEAction {

    /** Creates a new instance of ViewAffiliateDetail */
    public ViewAffiliateDetailAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        Integer empId = null;
        try {
            empId = new Integer(request.getParameter("empId"));
            setCurrentAffiliatePk(request, empId);
        }
        catch (NumberFormatException nfe) {
            empId = getCurrentAffiliatePk(request);
        }

        if (empId == null) {
            throw new JspException("No primary key was specified in the request.");
        }

        AffiliateDetailForm adf = new AffiliateDetailForm();
        AffiliateData data = s_maintainAffiliates.getAffiliateData(empId);
        if (data == null) {
            throw new JspException("No affiliate found with empAffPk = " + empId);
        }
        // Set form fields from AffiliateData
        adf.setAffiliateData(data);

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
        // needed for header and footer tags
        setCurrentAffiliate(request, data);

        // needed for jsp
        request.setAttribute("affiliateDetail", adf);
        return mapping.findForward("View");
    }

}
