
package org.afscme.enterprise.controller.web;

import javax.servlet.ServletException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ChallengeQuestionData;
import java.io.IOException;
import org.afscme.enterprise.controller.ejb.AccessControl;
import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.util.ConfigUtil;


/**
 * Handles requests coming from the account info page.
 *
 * @struts.action   path="/editAccountInfo"
 *                  name="accountInfoForm"
 *                  scope="request"
 *                  validate="true"
 *					input="/Common/AccountInfoEdit.jsp"
 *
 * @struts.action-set-property property="cancellable" value="true"
*/


public class AccountInfoAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		AccountInfoForm aiForm = (AccountInfoForm)form;

		if (aiForm.getNewPassword() == null) {
			//This means the action came from another page, not the AccountInfoEdit jsp.
            //Read the data and display the page
			ChallengeQuestionData cqd = s_accessControl.getChallengeQuestionData(usd.getUserId());
            if (cqd.getQuestion() == null) {
                //default them to the first question
                aiForm.setChallengeQuestion(ConfigUtil.getConfigurationData().getDefaultChallengeQuestion());
            } else {
    			aiForm.setChallengeQuestion(cqd.getQuestion());
            }
			aiForm.setChallengeResponse(cqd.getResponse());
			return new ActionForward(mapping.getInput());
		}
		else if (isCancelled(request)) {
			if (usd.isForceChangePassword())
				return mapping.findForward("Welcome");
			else
				return mapping.findForward("MainMenu");
		} else {

            //they submitted the form, handle it.

			int result = 0;
			if (usd.isForceChangePassword()) {
				result = s_accessControl.changePassword(
					usd.getPersonPk(),
					aiForm.getNewPassword());
			} else {
				result = s_accessControl.changePassword(
					usd.getPersonPk(),
					aiForm.getPassword(),
					aiForm.getNewPassword());
			}
			if (result == AccessControlStatus.CHANGE_PASSWORD_BAD_OLD_PASSWORD) {
                return makeErrorForward(request, mapping, "password", "error.field.incorrect");
			} else {
				s_accessControl.updateChallenge(usd.getPersonPk(), aiForm.getChallengeQuestion(), aiForm.getChallengeResponse());
				usd.setForceChangePassword(false);
                return LoginAction.getInitialPage(mapping, usd);
			}
		}
	}
}
