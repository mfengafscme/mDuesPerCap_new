package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Processing for display of Add Participation Group page
 *
 * @struts:action   path="/addParticipationGroup"
 *                  input="/Membership/ParticipationGroupAddGroup.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 */
public class AddParticipationGroupAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        return mapping.getInputForward();
    }
}
