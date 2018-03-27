package org.afscme.enterprise.affiliate.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.DateUtil;

/** 
 * @struts:action   path="/viewAffiliateChangeHistoryDetail"
 *                  name="changeHistoryDetailForm"
 *                  validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="viewDetail"  path="/Membership/AffiliateChangeHistoryDetail.jsp"
 */
public class ViewAffiliateChangeHistoryDetailAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewAffiliateChangeHistoryDetailAction */
    public ViewAffiliateChangeHistoryDetailAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ChangeHistoryDetailForm chdf = (ChangeHistoryDetailForm)form;
        log.debug("section:     " + chdf.getSection());
        log.debug("change date: " + chdf.getChangedDate());
        
        chdf.setChanges(s_maintainAffiliates.getChangeHistoryData(chdf.getAffPk(), chdf.getSection(), DateUtil.getTimestamp(chdf.getChangedDate())));
        
        return mapping.findForward("viewDetail");
    }
    
}
