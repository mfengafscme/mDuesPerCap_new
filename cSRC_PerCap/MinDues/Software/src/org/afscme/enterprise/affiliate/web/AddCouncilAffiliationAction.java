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
import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/** 
 * @struts:action   path="/addCouncilAffiliation"
 *                  validate="false"
 *                  scope="request"
 *                  name="councilAffiliationForm"
 *                  
 * @struts:action-forward   name="viewAdd"  path="/Membership/CouncilAffiliationMaintenance.jsp"
 */
public class AddCouncilAffiliationAction extends AFSCMEAction {
    
    /** Creates a new instance of AddCouncilAffiliationAction */
    public AddCouncilAffiliationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        log.debug("Inside AddCouncilAffiliationAction.perform().");
        CouncilAffiliationForm caf = (CouncilAffiliationForm)form;
        AffiliateIdentifier ai = getCurrentAffiliateId(request);
        Character c = ai.getType();
        if (c.equals(new Character('S')))
            caf.setAffIdType(new Character('R'));        
        else
            caf.setAffIdType(new Character('C'));
        return mapping.findForward("viewAdd");
    }
    
}
