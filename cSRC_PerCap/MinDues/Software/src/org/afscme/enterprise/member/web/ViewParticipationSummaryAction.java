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
import org.afscme.enterprise.member.web.ParticipationSummaryForm;

/* This action displays the member Participation Information Summary.  Initially
 * Arrived at by clicking the "Participation" tab in the member section 
 * 
 */
 
/**
 * @struts:action   path="/viewParticipationSummary"
 *                  name="participationSummaryForm"
 *                  scope="request"
 *                  validate="true"
 *
 * @struts:action-forward   name="View"  path="/Membership/ParticipationSummary.jsp" redirect="false"
 *
 */

public class ViewParticipationSummaryAction extends org.afscme.enterprise.controller.web.AFSCMEAction{
    
    /** Creates a new instance of ViewParticipationSummaryAction */
    public ViewParticipationSummaryAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        
        ParticipationSummaryForm psf = (ParticipationSummaryForm)form;
        
        // set the current flow
        setCurrentFlow(request, psf.getOrigin());
        
        // get the members participation list and set it in the form
        psf.setMemberParticipations(s_maintainMembers.getParticipationSummary(getCurrentPersonPk(request), psf.getSortData()));        
        
        return mapping.findForward("View");
    }    
    
}
