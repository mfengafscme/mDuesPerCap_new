package org.afscme.enterprise.reporting.specialized.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

// Java imports
import java.util.*;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * @struts:action   path="/viewPreliminaryRosterAffiliate"
 *                  name="preliminaryRosterAffiliateForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="ViewAffiliates"  path="/Membership/PreliminaryRosterAffiliateSelection.jsp"
 */
public class ViewPreliminaryRosterAffiliateAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        PreliminaryRosterAffiliateForm pra = (PreliminaryRosterAffiliateForm)form;
        if (pra == null) {
            pra = new PreliminaryRosterAffiliateForm();
        }
        
        // Get all Preliminary Roster Affiliates
        ArrayList affiliateList = s_maintainPoliticalRebate.getPreliminaryRosterAffiliates(pra.getOrder(), pra.getSortBy());
        pra.setAffiliateList(affiliateList);        
        pra.setSize(affiliateList.size());        
        request.setAttribute("preliminaryRosterAffiliateForm", pra);
        
        // Forward to the Preliminary Roster Affiliates selection page
        return mapping.findForward("ViewAffiliates");
    }
}
