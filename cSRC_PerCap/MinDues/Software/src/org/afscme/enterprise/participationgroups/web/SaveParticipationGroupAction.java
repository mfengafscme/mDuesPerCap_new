package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.participationgroups.ParticipationGroupData;


/**
 * Handles the submit from the 'Add Participation Group' page.
 *
 * @struts:action   path="/saveParticipationGroup"
 *                  input="/Membership/ParticipationGroupAddGroup.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/viewParticipationGroup.action"
 */
public class SaveParticipationGroupAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        if (isCancelled(request)) {
            return mapping.findForward("ViewParticipationGroupMaintenance");
        }

        //validate manually since need to check db for some rules
        ActionErrors errors = pcForm.validate(mapping, request);

        //check for duplicate group name rule
        if (s_maintainParticipationGroups.isDuplicateGroup(pcForm.getName())) {
            errors.add("name", new ActionError("error.participation.groups.name.duplicate", ""));
        }

        //return to input page if errors
        if (errors != null && !errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        //add a new participation group
        ParticipationGroupData group = s_maintainParticipationGroups.addParticipationGroupData(
                        new ParticipationGroupData(pcForm.getName(), pcForm.getDescription()));

        //set the groupPk
        pcForm.setGroupPk(group.getGroupPk());

        // go to participation group view once everything is saved
        return mapping.findForward("View");
    }
}
