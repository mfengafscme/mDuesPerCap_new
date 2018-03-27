package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;

/** 
 * @struts:action   path="/viewAffiliateHierarchy"
 *                  validate="false"
 *
 * @struts:action-forward   name="ViewHierarchy"  path="/Membership/AffiliateHierarchy.jsp"
 */
public class ViewAffiliateHierarchyAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        List hierarchy = s_maintainAffiliates.getAffiliateHierarchy(usd.getActingAsAffiliate());
        request.setAttribute("affiliateHierarchy", hierarchy);
        
        AffiliateData data = s_maintainAffiliates.getAffiliateData(usd.getActingAsAffiliate());
        // needed for header and footer tags
        setCurrentAffiliate(request, data);
        
        return mapping.findForward("ViewHierarchy");
    }
    
}
