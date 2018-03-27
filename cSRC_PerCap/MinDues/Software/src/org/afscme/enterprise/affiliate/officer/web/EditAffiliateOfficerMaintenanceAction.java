package org.afscme.enterprise.affiliate.officer.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.*;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance;
import org.afscme.enterprise.affiliate.AffiliateData;


/** 
 * @struts:action   path="/editAffiliateOfficerMaintenance"
 *                  name="affiliateOfficerMaintenanceForm"
 *                  validate="false"
 *                  scope="session"
 *                  input="/viewAffiliateOfficerMaintenance"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/AffiliateOfficerMaintenanceEdit.jsp" 
 */
public class EditAffiliateOfficerMaintenanceAction extends AFSCMEAction {
        
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) throws Exception {
        
        AffiliateOfficerMaintenanceForm aomf = (AffiliateOfficerMaintenanceForm)form;        
        
        Integer aff_pk = getCurrentAffiliatePk(request);        
        
        // get affiliate data so affiliate status can be determined.
        AffiliateData ad = s_maintainAffiliates.getAffiliateData(aff_pk);
        
        // fetch officers, passing in affiliate and affiliate status
        Map officers = s_maintainAffiliateOfficers.getOfficerMaintenanceList(aff_pk, ad.getStatusCodePk(), usd.isActingAsAffiliate());
        log.debug("OfficerList Size = " + aomf.getOfficerList().size());
        
        // set type into form for steward screen validation
        // steward only valid for types L and U.
        aomf.setType(ad.getAffiliateId().getType());
        aomf.setOfficerList(officers);        
        return mapping.findForward("Edit");
    }    
}
