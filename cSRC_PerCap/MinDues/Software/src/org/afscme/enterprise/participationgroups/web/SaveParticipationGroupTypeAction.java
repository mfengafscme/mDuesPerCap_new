package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.participationgroups.ParticipationTypeData;


/**
 * Handles the submit from the 'Add Participation Group Type' page.
 *
 * @struts:action   path="/saveParticipationGroupType"
 *                  input="/Membership/ParticipationGroupAddType.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 *                  validate="true"
 *
 * @struts:action-forward   name="View"  path="/viewParticipationGroup.action"
 */
public class SaveParticipationGroupTypeAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        //retrieve the groupPk for the add
        Integer pk = pcForm.getGroupPk();

        if (isCancelled(request)) {
            return mapping.findForward("View");
        }
        
        //add a new participation group type
        ParticipationTypeData type = new ParticipationTypeData(pcForm.getName(), pcForm.getDescription());
        type.setGroupPk(pk);
        type = s_maintainParticipationGroups.addParticipationTypeData(type);

        // go to participation group view once everything is saved
        return mapping.findForward("View");
    }
}
