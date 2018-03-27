package org.afscme.enterprise.controller.web;

import javax.servlet.jsp.tagext.Tag;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.taglib.html.LinkTag;
import org.afscme.enterprise.util.ConfigUtil;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.jsp.JspException;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.controller.UserSecurityData;
import java.net.MalformedURLException;

/**
 * Base class for tags which behave differently depending on actions a user has access to
 */
public abstract class PrivilegedTag extends LinkTag
{
	protected String m_url;
    protected String prefix;
    protected String postfix;
	
	/** true iff the user doesn't have access to the specified page */
	protected boolean m_disallowed;

	/** True iff javascript is enabled for the client (i.e., if it is an automated test client)*/
	protected Boolean javaScriptEnabled;

    /** Computes the URL of the aciton based on the various attributes passed in */
	protected String getURL() throws JspException {
		if (m_url == null) {
			// Generate the hyperlink URL
			Map params = RequestUtils.computeParameters(
					pageContext,
					paramId,
					paramName,
					paramProperty,
					paramScope,
					name,
					property,
					scope,
					transaction);
			try {
				m_url = RequestUtils.computeURL(pageContext, forward, href, page, action, params, anchor, false);
			} catch (MalformedURLException e) {
				throw new JspException("Unable to compute URL for page '" + page + "' or forward '" + forward + "'", e);
			}
		}
		
		return m_url;
	}
	
	
	/**
	 * Overriedden from PrivilegedLingTag.
	 */
	public int doStartTag() throws JspException
	{
		int result;
		HttpSession session = pageContext.getSession();
		
        //verify the use is logged in
		if (session == null) {
			m_disallowed = true;
			return Tag.SKIP_BODY;
		}
		UserSecurityData usd = (UserSecurityData)session.getAttribute(AFSCMEAction.SESSION_USER_SECURITY_DATA);
		if (usd == null) {
			m_disallowed = true;
			return Tag.SKIP_BODY;
		}
		
        //get the name of the action the user is attempting to invoke
		String action;
		try {
			action = AFSCMEAction.getActionPart(getURL());
		} catch (ServletException e) {
			throw new JspException(e);
		}

        //check if the user has permission to invoke the action
		ActionPrivileges ap = ConfigUtil.getActionPrivileges();
		if (ap.isActionAllowed(action, usd)) {
            
            //user has permission, evaluate the tag
            
            if (prefix != null)
                ResponseUtils.write(pageContext, prefix);
			return doPrivilegedStartTag();

        } else {
            
            //user does not have permission, skip the tag

            m_disallowed = true;
			return Tag.SKIP_BODY;
        }
	}

	/** Overridden by subclasses to perform the work of Tag.doStartTag(), except it is only called if the user has the appropriate privileges */
	protected abstract int doPrivilegedStartTag() throws JspException;

	/** Overridden by subclasses to perform the work of Tag.doEndTag(), except it is only called if the user has the appropriate privileges */
	protected abstract int doPrivilegedEndTag() throws JspException;

    /** Gets the name of the tag element.  */
	protected abstract String getTagString() throws JspException;

	/** Returs true iff javascript is enabled for the client (i.e., if it is an automated test client)*/
	protected boolean isJavaScriptEnabled() {
		if (javaScriptEnabled == null) {
			javaScriptEnabled = new Boolean(!(((HttpServletRequest)pageContext.getRequest()).getHeader("User-Agent").equals("AFSCME Test Client")));
        }
		
		return javaScriptEnabled.booleanValue();
	}
	
    /**
     * Render the end of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        if (!m_disallowed) {
            int result = doPrivilegedEndTag();
            if (postfix != null) {
                ResponseUtils.write(pageContext, postfix);
            }
            release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)
            return result;
        }
        else {
            release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)
            return EVAL_PAGE;
        }
    }
	
	public void release() {
		super.release();
		m_url = null;
		m_disallowed = false;
		javaScriptEnabled = null;
	}

    /** Gets the prefix.  The prefix string is inserted before this tag, if the user has the correct privileges */
    public String getPrefix() {
        return prefix;
    }
    /** Sets the prefix.  The prefix string is inserted before this tag, if the user has the correct privileges */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    /** Gets the postfix.  The postfix string is inserted after this tag, if the user has the correct privileges */
    public String getPostfix() {
        return postfix;
    }
    /** Sets the postfix.  The postfix string is inserted after this tag, if the user has the correct privileges */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }
}
