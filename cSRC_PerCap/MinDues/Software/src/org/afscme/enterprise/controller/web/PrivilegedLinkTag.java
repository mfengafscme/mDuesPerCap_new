package org.afscme.enterprise.controller.web;

import javax.servlet.jsp.JspException;
import org.apache.struts.util.ResponseUtils;



/**
 * Renders a link to an action.  Does not display if the user does not have access to that action.
 */
public class PrivilegedLinkTag extends PrivilegedActionTag
{
	protected String confirm;
	
    /** Returns the href attribute string */
	protected String prepareLocation() throws JspException {
		if (confirm != null && isJavaScriptEnabled()) 
			return " href=\"javascript:if (confirm('" + confirm + "')) { window.location = '" + getURL() + "'; }\" ";
		else
			return " href=\"" + getURL() + "\"";
	}

    /** For a link, the location is an href attribute */
	protected String getTagString() {
		return "a";
	}

    /** Gets the confirm message.  This is the message displayed in a JavaSCript confirm dialog before the action is executed */
	public java.lang.String getConfirm() {
		return confirm;
	}

    /** Sets the confirm message.  This is the message displayed in a JavaSCript confirm dialog before the action is executed */
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
}
