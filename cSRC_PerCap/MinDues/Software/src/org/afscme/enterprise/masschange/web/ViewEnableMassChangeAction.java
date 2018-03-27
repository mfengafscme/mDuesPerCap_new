package org.afscme.enterprise.masschange.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.affiliate.MRData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.apache.log4j.Logger;

/** 
 * @struts:action   path="/viewEnableMassChange"
 *                  name="massChangeForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="viewMassChange"  path="/Membership/EnableMassChange.jsp"
 */
public class ViewEnableMassChangeAction extends AFSCMEAction {
    
    static Logger logger = Logger.getLogger(ViewEnableMassChangeAction.class);
    
    /** Creates a new instance of ViewEnableMassChangeAction */
    public ViewEnableMassChangeAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        logger.debug("----------------------------------------------------------------");
        logger.debug("Inside ViewEnableMassChangeAction.perform");
        Integer affPk = getCurrentAffiliatePk(request);
        if (affPk == null) {
            throw new JspException("No current Affiliate is defined for which to retrieve Mass Change Information.");
        }
        MassChangeForm mcf = (MassChangeForm)form;
        MRData data = s_maintainAffiliates.getMembershipReportingData(affPk);
        
        mcf.setAffPk(affPk);
        mcf.setInfoSourceCurrent(data.getInformationSource());
        mcf.setStatusCurrent(data.getAffStatus());
        mcf.setAffiliateIdCurrent(data.getNewAffiliateId());
        mcf.setMbrCardBypassFgCurrent(data.isNoCards());
        mcf.setPeMailBypassFgCurrent(data.isNoPEMail());
        
        return mapping.findForward("viewMassChange");
    }
    
}
