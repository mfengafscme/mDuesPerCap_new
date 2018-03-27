package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;

/**
 * Represents the data on the account info page
 *
 * @struts:form name="accountInfoForm"
 */
public class AccountInfoForm extends ActionForm
{
    private String m_password;
    private String m_newPassword;
    private String m_newPassword2;
    private Integer m_challengeQuestion;
    private String m_challengeResponse;

    /** This attribute is not part of the form data, it specifies if the is the 'force change password' page or the 'change password' page. */
    private boolean m_forceChangePassword;
	
	public String getPassword() {
		return m_password;
	}
	
	public void setPassword(String password) {
		m_password = password;
	}
	
	public String getNewPassword() {
		return m_newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		m_newPassword = newPassword;
	}
	
	public String getNewPassword2() {
		return m_newPassword2;
	}
	
	public void setNewPassword2(String newPassword2) {
		m_newPassword2 = newPassword2;
	}
	
	public Integer getChallengeQuestion() {
		return m_challengeQuestion;
	}
	
	public void setChallengeQuestion(Integer challengeQuestion) {
		m_challengeQuestion = challengeQuestion;
	}
	
	public String getChallengeResponse() {
		return m_challengeResponse;
	}
	
	public void setChallengeResponse(String challengeResponse) {
		m_challengeResponse = challengeResponse;
	}
	
	public boolean isForceChangePassword() {
		return m_forceChangePassword;
	}
	
	public void setForceChangePassword(boolean val) {
		m_forceChangePassword = val;
	}

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		
		if (m_newPassword == null) {
			//new, ignore
			return null;
		}
		
		ActionErrors errors = new ActionErrors();
		if (!m_newPassword.equals(m_newPassword2)) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.passwordsMustMatch"));
		}
		if (m_newPassword.length() == 0) {
			errors.add("newPassword", new ActionError("error.field.required"));
        } else if (m_newPassword.length() < 4) {
            errors.add("newPassword", new ActionError("error.field.length.tooShort", new Integer(4)));
        }
		if (m_newPassword2.length() == 0) {
			errors.add("newPassword2", new ActionError("error.field.required"));
        } else if (m_newPassword2.length() < 4) {
            errors.add("newPassword2", new ActionError("error.field.length.tooShort", new Integer(4)));
        }
		if (m_challengeResponse.length() == 0)
			errors.add("challengeResponse", new ActionError("error.field.required"));
		
		return errors;
	}
}
