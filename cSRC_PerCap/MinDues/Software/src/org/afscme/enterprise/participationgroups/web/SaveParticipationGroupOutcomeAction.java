package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;


/**
 * Handles the submit from the 'Add Participation Group Outcome' page.
 *
 * @struts:action   path="/saveParticipationGroupOutcome"
 *                  input="/Membership/ParticipationGroupAddOutcome.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 *                  validate="true"
 *
 * @struts:action-forward   name="View"  path="/viewParticipationGroup.action"
 */
public class SaveParticipationGroupOutcomeAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        //retrieve the detailPk to associate new outcome to participation detail
        Integer pk = pcForm.getPk();

        if (isCancelled(request)) {
            return mapping.findForward("View");
        }
        
        //add a new outcome and associate it to a participation group detail
        ParticipationOutcomeData outcome = new ParticipationOutcomeData(pcForm.getName(), pcForm.getDescription());
        outcome.setDetailPk(pk);
        outcome = s_maintainParticipationGroups.addParticipationOutcomeData(outcome, usd.getPersonPk());

        // go to participation group view once everything is saved
        return mapping.findForward("View");
    }
}
