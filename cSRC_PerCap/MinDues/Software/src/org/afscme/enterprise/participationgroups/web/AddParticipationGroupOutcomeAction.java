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
 * Processing for display of Add Participation Group Outcome page
 *
 * @struts:action   path="/addParticipationGroupOutcome"
 *                  input="/Membership/ParticipationGroupAddOutcome.jsp"
 *                  name="participationCodeForm"
 *                  scope="request"
 */
public class AddParticipationGroupOutcomeAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        ParticipationCodeForm pcForm = (ParticipationCodeForm)form;

        //set the detailPk in the form
        pcForm.setPk(new Integer(request.getParameter("pk")));
        pcForm.setDetailNm(s_maintainParticipationGroups.getParticipationName(pcForm.getPk(), ParticipationGroupData.PK_DETAIL));        

        //set the groupPk in the form to be used
        pcForm.setGroupPk(s_maintainParticipationGroups.getGroupPk(pcForm.getPk(), ParticipationGroupData.PK_DETAIL, null));
        pcForm.setGroupNm(s_maintainParticipationGroups.getParticipationName(pcForm.getGroupPk(), ParticipationGroupData.PK_GROUP));        

        //set typeNm in the form to be used as header
        String typePk = (String)request.getParameter("typePk");
        if (typePk != null) {
            pcForm.setTypeNm(s_maintainParticipationGroups.getParticipationName(new Integer(typePk), ParticipationGroupData.PK_TYPE));            
        }
                
        return mapping.getInputForward();
    }
}
