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
import org.afscme.enterprise.affiliate.CharterData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.web.CharterForm;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;

/** 
 * @struts:action   path="/deleteCouncilAffiliation"
 */
public class DeleteCouncilAffiliationAction extends AFSCMEAction {
    
    /** Creates a new instance of DeleteCouncilAffiliationAction */
    public DeleteCouncilAffiliationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        log.debug("Inside DeleteCouncilAffiliationAction.perform().");
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve a Charter.");
        }
        if (s_maintainAffiliates.removeAffiliatedCouncil(affPk, usd.getPersonPk())) {
            setCurrentAffiliate(request, s_maintainAffiliates.getAffiliateData(affPk));
        } 
        return mapping.findForward("ViewCharter");
    }
    
}
