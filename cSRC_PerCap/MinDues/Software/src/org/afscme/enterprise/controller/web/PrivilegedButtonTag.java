package org.afscme.enterprise.controller.web;

import javax.servlet.jsp.JspException;


/**
 * Renders a link to an action.  Does not display if the user does not have access to that action.
 */
public class PrivilegedButtonTag extends PrivilegedActionTag 
{
   	protected String confirm;

	public int doStartTag() throws JspException {
		setStyleClass("button");
		return super.doStartTag();
	}
	
    /**
     * For button, the location is set with an onclick handler that sets the window location.
     */
	protected String prepareLocation() throws JspException {
        if (isJavaScriptEnabled()) {
            if (confirm != null) {
    			return " onclick=\"javascript:if (confirm('" + confirm + "')) { window.location = '" + getURL() + "'; }\" ";
            } else {
    			return " onclick=\"javascript:window.location = '" + getURL() + "'\" ";
            }
        } else {
			return " href=\"" + getURL() + "\"";
        }
	}

	protected String getTagString() {
		if (isJavaScriptEnabled())
			return "button";
		else
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
	
    public void release() {
		super.release();
		m_disallowed = false;
	}
}
