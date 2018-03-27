
package org.afscme.enterprise.controller.web;

import javax.servlet.ServletException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
import java.io.IOException;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.ChallengeQuestionData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ejb.AccessControl;

/**
 * Handles submits from the request password screen.
 *
 * @struts:action   name="requestPasswordForm"
 *                  path="/requestPassword"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Common/RequestPassword.jsp"
 *
 * @struts:action-forward   name="Login"  path="/Common/Login.jsp"
 * @struts:action-forward   name="Success"  path="/Common/PasswordSent.jsp"
 * @struts:action-forward   name="NoEmail"  path="/Common/NoEmail.jsp"
 * @struts:action-forward   name="NoChallengeQuestion"  path="/Common/NoChallengeQuestion.jsp"
 *
*/
public class RequestPasswordAction extends AFSCMEAction {

	static Logger log = Logger.getLogger(RequestPasswordAction.class);
	
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd /* this will always be null here */) throws Exception {

		ActionErrors errors	= new ActionErrors();
		RequestPasswordForm rpForm;
		String userId = (String)request.getAttribute("userId");

		if (userId != null) {

			//this action was invoked by the login page

            rpForm = new RequestPasswordForm();

			//get the user id
			if (TextUtil.isEmpty(userId)) {
				throw new ServletException("userId not provided in RequestPassword action.  LoginAction is required to pass a value for this parameter");
			}
			request.removeAttribute("userId");

			//get the challenge question
            ChallengeQuestionData cqd = s_accessControl.getChallengeQuestionData(userId);

            if (cqd == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.requestPassword.userNotFound"));
                saveErrors(request, errors);
                return mapping.findForward("Login");
            }  else if (cqd.getQuestion() == null) {
                request.setAttribute("AffiliatePhone", s_maintainUsers.getContactPhone(cqd.getPersonPk()));
                return mapping.findForward("NoChallengeQuestion");
            } else {
                rpForm.setChallengeQuestion(cqd.getQuestion());
                rpForm.setPersonPk(cqd.getPersonPk());
                request.setAttribute("requestPasswordForm", rpForm);
                return new ActionForward(mapping.getInput());
            }
		} else {
			
			//RequestPassword.jsp invoked this action

            //request the password
			rpForm = (RequestPasswordForm)form;
            int result = s_accessControl.requestPassword(rpForm.getPersonPk(), rpForm.getResponse());
			
            //check the result
			if (result == AccessControlStatus.REQUEST_PASSWORD_OK) {
				return mapping.findForward("Success");
			} else {
				switch (result) {
					case AccessControlStatus.REQUEST_PASSWORD_NO_EMAIL:
                        request.setAttribute("AffiliatePhone", s_maintainUsers.getContactPhone(rpForm.getPersonPk()));
						return mapping.findForward("NoEmail");
					case AccessControlStatus.REQUEST_PASSWORD_BAD_RESPONSE:
                        return makeErrorForward(request, mapping, "response", "error.requestPassword.field.response.incorrect");
					default:
						throw new RuntimeException("Unknown value " + result + " returned from AccessControl.requestPassword()");
				}
			}
		}
	}
    
    /** Returns false.  Login is not required for this action */
    protected boolean isLoginRequired() {
        return false;
    }
}
