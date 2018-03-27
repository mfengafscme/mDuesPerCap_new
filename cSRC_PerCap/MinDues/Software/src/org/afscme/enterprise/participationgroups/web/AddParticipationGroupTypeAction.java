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
 * Processing for display of Add Participation Group Type page
 *
 * @struts:action   path="/addParticipationGroupType"
 *                  input="/Membership/ParticipationGroupAddType.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 */
public class AddParticipationGroupTypeAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        //set the groupPk in the form
        pcForm.setGroupPk(new Integer(request.getParameter("groupPk")));
        pcForm.setGroupNm(s_maintainParticipationGroups.getParticipationName(pcForm.getGroupPk(), ParticipationGroupData.PK_GROUP));        

        return mapping.getInputForward();
    }
}
