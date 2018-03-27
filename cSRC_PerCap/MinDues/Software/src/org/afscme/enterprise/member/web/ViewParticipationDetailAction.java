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
import org.afscme.enterprise.member.ParticipationData;

/* This action displays an individial member participation record in detail.  Initially
 * arrived at by click on "View" from the Participation Summary Page.  
 * 
 */
 
/**
 * @struts:action   path="/viewParticipationDetail"
 *                  name="participationDetailForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/Membership/ParticipationDetail.jsp"
 *
 */

public class ViewParticipationDetailAction extends org.afscme.enterprise.controller.web.AFSCMEAction{
    
    /** Creates a new instance of ViewParticipationDetailAction */
    public ViewParticipationDetailAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        ParticipationDetailForm pdf = (ParticipationDetailForm)form;        
        Integer participDetailPk = null;
        
        // get detail pk out of request or from form,
        // depending on where page reached from,
        // and use it to find the participation data        
        String participDetailPkStr = request.getParameter("participDetailPk");
        if (participDetailPkStr == null) {
            participDetailPk = pdf.getDetailPk();
        } else {
            participDetailPk = new Integer(participDetailPkStr);
        }
        ParticipationData pd = s_maintainMembers.getParticipationData(getCurrentPersonPk(request), participDetailPk);
        pdf.setAll(pd);
        
        return mapping.findForward("View");
    }    
}
