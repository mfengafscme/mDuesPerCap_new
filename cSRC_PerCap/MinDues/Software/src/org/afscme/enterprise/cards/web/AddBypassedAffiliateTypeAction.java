package org.afscme.enterprise.cards.web;

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

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * @struts:action   name="addBypassedAffiliateTypeAction"
 *                  path="/addBypassedAffiliateType"
 *                  validate="false"
 *                  scope="request"
 *
 *
 * @struts:action-forward   name="Add"  path="/Membership/GetBypassedAffiliate.jsp"
 */
public class AddBypassedAffiliateTypeAction extends AFSCMEAction {
    
    /** Creates a new instance of AddABypassedAffiliateTypeAction */
    public AddBypassedAffiliateTypeAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response,
    UserSecurityData usd)
    throws Exception {
        return mapping.findForward("Add");
    }
    
}
