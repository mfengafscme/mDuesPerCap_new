package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Display the list of Participation Groups
 *
 * @struts:action   path="/viewParticipationGroupMaintenance"
 *                  name="participationCodeListForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/ParticipationGroupMaintenance.jsp"
 */
public class ViewParticipationGroupMaintenanceAction extends AFSCMEAction {

	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, UserSecurityData usd) throws Exception {

        ParticipationCodeListForm pclForm = (ParticipationCodeListForm)form;

        //put the results in the form
        pclForm.setResults(s_maintainParticipationGroups.getParticipationGroups());

        return mapping.findForward("View");
    }
}
