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
import org.afscme.enterprise.affiliate.ConstitutionData;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.web.ConstitutionForm;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;

/** 
 * @struts:action   path="/viewConstitutionInformation"
 *
 * @struts:action-forward   name="view"  path="/Membership/ConstitutionInformation.jsp"
 */

public class ViewConstitutionInformationAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewConstitutionInformationAction */
    public ViewConstitutionInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve a Constitution.");
        }
        ConstitutionData data = s_maintainAffiliates.getConstitutionData(affPk);
        if (data == null) {
            throw new JspException("No Constitution found with pk = " + affPk);
        }
        
        ConstitutionForm cf = new ConstitutionForm();
        cf.setConstitutionData(data);
        
        log.debug("form - " + cf.toString());
        request.setAttribute("constitutionForm", cf);
        return mapping.findForward("view");
    }
    
}
