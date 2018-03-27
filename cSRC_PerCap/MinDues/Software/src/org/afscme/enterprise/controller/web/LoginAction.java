
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
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
import java.io.IOException;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ejb.AccessControl;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.users.UserData;

/**
 * Handles the actions from the login page.
 *
 * @struts:action   name="loginForm"
 *                  path="/login"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Common/Login.jsp"
 *
 * @struts:action-forward   name="RequestPassword"  path="/requestPassword.action"
*/


public class LoginAction extends AFSCMEAction {

    static Logger log = Logger.getLogger(LoginAction.class);

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd /* usd will always be null here */ ) throws Exception {

        //Check if this action was invoked by the login page, or another page.
        LoginForm loginForm = (LoginForm)form;
        if (loginForm.getUserId() == null) {
            //invoked by another page, just display the login form.
            return new ActionForward(mapping.getInput());
        }

        //Check if the user clicked the 'Request Password' button.
        //If so, go to that action
        if (loginForm.isRequestPasswordButton()) {
            request.setAttribute("userId", loginForm.getUserId());
            return mapping.findForward("RequestPassword");
        }

        //log the user in
        usd = new UserSecurityData();
        int result = s_accessControl.login(loginForm.getUserId(), loginForm.getPassword(), usd);

        if (AccessControlStatus.LOGIN_RESULT_OK == result) {

            //login was ok
            //pace the user's data into the session
            request.getSession(true).setAttribute(AFSCMEAction.SESSION_USER_SECURITY_DATA, usd);

            // dues year
            String currentDuesYear = s_accessControl.getCurrentDuesYear();
			request.getSession(true).setAttribute("currentDuesYear", currentDuesYear);

            //if (log.isDebugEnabled())
             //   log.debug("User Logged In:\n" + usd);


			//String action = getActionPart(request.getRequestURI());
            //if ( (usd.isForceChangePassword()) && (action.indexOf("editAccountInfo") == -1) )
            //    return mapping.findForward("ForceToChangePassword");
            //else
            	//return getInitialPage(mapping, usd);
            	return mapping.findForward("MinimumDuesMainMenu");

        } else {

            //the login was not ok
            ActionErrors errors = new ActionErrors();
            switch (result) {
                case AccessControlStatus.LOGIN_RESULT_FAILED:
                    return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.login.uidOrPasswordIncorrect");
					//return makeLoginErrorForward(request, mapping, "error.login.uidOrPasswordIncorrect");
                case AccessControlStatus.LOGIN_RESULT_LOCKED_OUT:
                    //return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.login.accountLockedOut");
					return makeLoginErrorForward(request, mapping, "error.login.accountLockedOut");
                default:
                    throw new RuntimeException("Unknown login result: " + result);
            }
        }
    }

    /** Returns false.  Login is not required for the login action */
    protected boolean isLoginRequired() {
        return false;
    }

	/** Gets the initial action a user should be forward to after logging on */
    protected static ActionForward getInitialPage(ActionMapping mapping, UserSecurityData usd) {
        //send the user to their preferred start page

        /*
        switch (usd.getStartPage()) {
            case UserData.START_PAGE_AFSCME:
                if (usd.getPrivileges().contains(ActionPrivileges.PRIVILEGE_AFSCME_INTERNATIONAL_USER))
                    return mapping.findForward("MinimumDuesMainMenu");
                break;
            case UserData.START_PAGE_AFFILIATE:
                if (usd.getPrivileges().contains(ActionPrivileges.PRIVILEGE_DATA_UTILITY_USER))
                    return mapping.findForward("AffiliateDataUtility");
            case UserData.START_PAGE_MEMBER:
                if (usd.getPrivileges().contains(ActionPrivileges.PRIVILEGE_VIEW_PERSONAL_INFORMATION))
                    return mapping.findForward("ViewPersonalInformation");
                else
                    return mapping.findForward("ErrorNoPrivileges");
            case UserData.START_PAGE_VENDOR:
                if (usd.getPrivileges().contains("SearchVendorMember"))
                    return mapping.findForward("VendorMemberSearch");

        }
        */

        //they don't have rights to their preferred start page, punt them to the main menu
        //return mapping.findForward("MainMenu");
        return mapping.findForward("MinimumDuesMainMenu");
    }
}
