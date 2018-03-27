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
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.ConstitutionData;

/** 
 * @struts:action   path="/editConstitutionInformation"
 *                  name="constitutionForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="viewConstitutionEdit"  path="/Membership/ConstitutionInformationEdit.jsp"
 */
public class EditConstitutionInformationAction extends AFSCMEAction {
    
    /** Creates a new instance of EditConstitutionInformationAction */
    public EditConstitutionInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve a Charter.");
        }
        
        ConstitutionForm cf = (ConstitutionForm)form;
        cf.setAffPk(affPk);
        ConstitutionData data = s_maintainAffiliates.getConstitutionData(affPk);
        cf.setConstitutionData(data);
        return mapping.findForward("viewConstitutionEdit");
    }
    
}
