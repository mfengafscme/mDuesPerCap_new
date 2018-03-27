package org.afscme.enterprise.controller.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;

/**
 * This form represents data on the request password page.
 *
 * @struts:form name="requestPasswordForm"
 */
public class RequestPasswordForm extends ActionForm
{
    private String m_response;
    private Integer m_personPk;
    private Integer m_challengeQuestion;
	
	public String getResponse() {
		return m_response;
	}
	
	public void setResponse(String response) {
		m_response = response;
	}
	
	public String toString() {
		return "Response: " + m_response + " PersonPk: " + m_personPk + " Question: " + m_challengeQuestion;
	}
	
	public Integer getPersonPk() {
		return m_personPk;
	}
	
	public void setPersonPk(Integer personPk) {
		m_personPk = personPk;
	}

	public Integer getChallengeQuestion() {
		return m_challengeQuestion;
	}
	
	public void setChallengeQuestion(Integer challengeQuestion) {
		m_challengeQuestion = challengeQuestion;
	}
	
}
