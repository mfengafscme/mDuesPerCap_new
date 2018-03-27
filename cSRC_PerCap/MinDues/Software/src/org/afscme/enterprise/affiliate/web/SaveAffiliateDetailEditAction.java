package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.RequestUtils;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.affiliate.AffiliateResult;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.util.DBUtil;
    
/** 
 * @struts:action   path="/saveAffiliateDetailEdit"
 *                  name="affiliateDetailForm"
 *                  validate="true"
 *                  scope="request"
 *                  input="/Membership/AffiliateDetailEdit.jsp"
 */
public class SaveAffiliateDetailEditAction extends AFSCMEAction {
    
    /** Creates a new instance of SaveAffiliateDetailEditAction */
    public SaveAffiliateDetailEditAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        log.debug("Inside SaveAffiliateDetailEditAction.perform().");
        AffiliateDetailForm adf = (AffiliateDetailForm)form;
        
        AffiliateData data = adf.getAffiliateData();
        s_maintainAffiliates.updateAffiliateData(data, usd.getPersonPk());
        data = s_maintainAffiliates.getAffiliateData(adf.getAffPk());
        
        setCurrentAffiliate(request, data); // in case any related data changed...
        
        return mapping.findForward("ViewAffiliateDetail");
    }
    
}
