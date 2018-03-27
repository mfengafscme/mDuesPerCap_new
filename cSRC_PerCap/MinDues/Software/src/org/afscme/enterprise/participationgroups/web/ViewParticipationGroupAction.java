package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.participationgroups.ParticipationGroupData;

/**
 * Display the detail information for a Participation Group
 *
 * @struts:action   path="/viewParticipationGroup"
 *                  name="participationCodeForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/ParticipationGroupView.jsp"
 */
public class ViewParticipationGroupAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, UserSecurityData usd) throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        //inactivate the selected participation detail
        if (request.getParameter("inactivate") != null) {
            String detailPk = request.getParameter("detailPk");
            if (detailPk!=null) {
                s_maintainParticipationGroups.inactivateParticipationDetail(new Integer(detailPk), usd.getPersonPk());
            }                            
        }
        
        Integer groupPk = null;
        String pk = request.getParameter("groupPk");

        //if groupPk does not come in on the request line, check the form for it
        if (pk == null)
            groupPk = pcForm.getGroupPk();
        else 
            groupPk = new Integer(pk);
        
        ParticipationMaintenance participationMaintenance = new ParticipationMaintenance();
        participationMaintenance.setGroup(groupPk);
        participationMaintenance.setGroupNm(s_maintainParticipationGroups.getParticipationName(groupPk, ParticipationGroupData.PK_GROUP));        
        
        //retrieve all of the participation type, detail and outcome objects for a group
        participationMaintenance.setTypes(s_maintainParticipationGroups.getParticipationTypes(groupPk));
        participationMaintenance.setDetails(s_maintainParticipationGroups.getParticipationDetails(groupPk));
        participationMaintenance.setOutcomes(s_maintainParticipationGroups.getParticipationOutcomes(groupPk));

        //set the hierarchy data into the request
        request.setAttribute("participationMaintenance", participationMaintenance);
        
        return mapping.findForward("View");
    }
}
