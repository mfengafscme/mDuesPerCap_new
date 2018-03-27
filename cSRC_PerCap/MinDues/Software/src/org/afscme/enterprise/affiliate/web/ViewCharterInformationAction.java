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
import org.apache.log4j.Logger;

/** 
 * @struts:action   path="/viewCharterInformation"
 *
 * @struts:action-forward   name="view"  path="/Membership/CharterInformation.jsp"
 */
public class ViewCharterInformationAction extends AFSCMEAction {
    
    private static Logger logger = Logger.getLogger(ViewCharterInformationAction.class);
        
    /** Creates a new instance of ViewCharterInformationAction */
    public ViewCharterInformationAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve a Charter.");
        }
        CharterData data = s_maintainAffiliates.getCharterData(affPk);
        if (data == null) {
            throw new JspException("No Charter found with pk = " + affPk);
        }
        
        CharterForm cf = new CharterForm();
        cf.setCharterData(data);
        if (cf.getStatus().equals(AffiliateStatus.AC) || data.getAffIdType().charValue() == 'U') {
            cf.setCanAssociateWithCouncil(false);
        } else {
            cf.setCanAssociateWithCouncil(true);
        }
        logger.debug("Viewing: " + cf);
        request.setAttribute("charterForm", cf);
        return mapping.findForward("view");
    }
    
}
