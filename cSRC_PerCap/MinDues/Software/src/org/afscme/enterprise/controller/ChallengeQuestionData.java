package org.afscme.enterprise.controller;

import java.io.Serializable;

/**
 * Data Structre that holds data used by the challenge question / request password process
 */
public class ChallengeQuestionData implements Serializable {

    /** Primary key of the user's challenge question */
	Integer question;

    /** Primary key of the user */
	Integer personPk;

    /** The user's response */
	String response;

	public Integer getQuestion() {
		return question;
	}
	
	public void setQuestion(Integer question) {
		this.question = question;
	}
	
	public Integer getPersonPk() {
		return personPk;
	}
	
	public void setPersonPk(Integer personPk) {
		this.personPk = personPk;
	}
	
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public String toString() {
		StringBuffer buf= new StringBuffer();
		buf.append("ChallengeQuestion[");
		buf.append("Question: " + question);
		buf.append(", Response: " + response);
		buf.append(", UserPK: " + personPk + "]");
		return buf.toString();
	}
}