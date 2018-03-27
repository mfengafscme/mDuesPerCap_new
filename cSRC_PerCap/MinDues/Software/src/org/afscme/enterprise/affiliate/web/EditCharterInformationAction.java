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
import org.afscme.enterprise.affiliate.CharterData;
import org.afscme.enterprise.affiliate.AffiliateData;

/** 
 * @struts:action   path="/editCharterInformation"
 *                  name="charterForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="viewCharterEdit"  path="/Membership/CharterInformationEdit.jsp"
 */
public class EditCharterInformationAction extends AFSCMEAction {
    
    /** Creates a new instance of EditCharterInformationAction */
    public EditCharterInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve a Charter.");
        }
        
        CharterForm cf = (CharterForm)form;
        AffiliateData ad = s_maintainAffiliates.getAffiliateData(affPk);
        cf.setAffIdType(ad.getAffiliateId().getType());
        cf.setAffPk(affPk);
        CharterData data = s_maintainAffiliates.getCharterData(affPk);
        cf.setCharterData(data);
        return mapping.findForward("viewCharterEdit");
    }
    
}
