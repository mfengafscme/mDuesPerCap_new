package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;

/**
 * Form data from the login page.
 *
 * @struts:form name="loginForm"
 */
public class LoginForm extends ActionForm
{
    private String m_userId;
    private String m_password;
    private String m_requestPassword;
		
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
		
        if (getUserId() == null)
            return null; //'new' mode.  All props are null.

        if (TextUtil.isEmpty(m_userId))
            errors.add("userId", new ActionError("error.field.required.userId"));

        if (!isRequestPasswordButton() && TextUtil.isEmpty(m_password))
            errors.add("password", new ActionError("error.field.required.password"));

		return errors;
    }
	
	public String getUserId() {
		return m_userId;
	}
	
	public void setUserId(String userId) {
		m_userId = userId;
	}
	
	public String getPassword() {
		return m_password;
	}
	
	public void setPassword(String password) {
		m_password = password;
	}
	
	public boolean isRequestPasswordButton() {
		return m_requestPassword != null;
	}

	public void setRequestPassword(String requestPassword) {
		m_requestPassword = requestPassword;
	}

	public String getRequestPassword() {
		return m_requestPassword;
	}

	
	public String toString() {
		return "UserId: " + m_userId + ", Password: " + m_password + "Request Password: " + isRequestPasswordButton();
	}
	
}
