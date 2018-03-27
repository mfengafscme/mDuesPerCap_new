package org.afscme.enterprise.participationgroups.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.participationgroups.ParticipationDetailData;


/**
 * Handles the submit from the 'Add Participation Group Detail' page.
 *
 * @struts:action   path="/saveParticipationGroupDetail"
 *                  input="/Membership/ParticipationGroupAddDetail.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 *                  validate="true"
 *
 * @struts:action-forward   name="View"  path="/viewParticipationGroup.action"
 */
public class SaveParticipationGroupDetailAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        //retrieve the typePk to associate new detail to participation type
        Integer pk = pcForm.getPk();

        if (isCancelled(request)) {
            return mapping.findForward("View");
        }
                
        //add a new detail for a participation group type
        ParticipationDetailData detail = new ParticipationDetailData(pcForm.getName(), pcForm.getDescription());
        detail.setTypePk(pk);
        detail = s_maintainParticipationGroups.addParticipationDetailData(detail, usd.getPersonPk());

        // go to participation group view once everything is saved
        return mapping.findForward("View");
    }
}
