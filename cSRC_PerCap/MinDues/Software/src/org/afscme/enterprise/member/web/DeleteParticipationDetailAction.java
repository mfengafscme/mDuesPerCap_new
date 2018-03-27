package org.afscme.enterprise.member.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.DBUtil;

/* This action will delete a member participation record.
 * 
 */
 
/**
 * @struts:action   path="/deleteParticipationDetail"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="NewResults"  path="/viewParticipationSummary.action" 
 *
 */

public class DeleteParticipationDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction{
    
    /** Creates a new instance of DeleteParticipationDetailAction */
    public DeleteParticipationDetailAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        String participDetailPk = request.getParameter("participDetailPk");
        if (participDetailPk != null) {
            s_maintainMembers.deleteParticipationData(getCurrentPersonPk(request, "personPk"), new Integer(participDetailPk));
        }
        return mapping.findForward("NewResults");
    }    
    
}
