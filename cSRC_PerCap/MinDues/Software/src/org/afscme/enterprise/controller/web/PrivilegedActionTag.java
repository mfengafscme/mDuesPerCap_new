package org.afscme.enterprise.controller.web;

import java.util.Map;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.net.MalformedURLException;

/**
 * Base class for PrivilegedTags which perform actions.
 */
public abstract class PrivilegedActionTag extends PrivilegedTag 
{
	boolean m_disallowed;
        String onclick = null;

        public void setOnclick(String onclick){
            this.onclick = onclick;
        }
        
        public String getOnclick(){
            return (this.onclick);
        }
        
            /**
	 * Displays the open tag.  This is only called when the user has the required permissions.
	 */
	public int doPrivilegedStartTag() throws JspException
	{
        // Generate the opening element
        StringBuffer results = new StringBuffer("<" + getTagString());
		
		results.append(prepareLocation());
		results.append(prepareStyles());
                if (getOnclick() != null)
                {
                    results.append(" onclick=\"");
                    results.append(onclick);
                    results.append("\"");
                }
		results.append(">");

		// Print this element to our output writer
        ResponseUtils.write(pageContext, results.toString());

		// Evaluate the body of this tag
		return (Tag.EVAL_BODY_INCLUDE);
	}

    /**
	 * Displays the closing tag.  This is only called when the user has the required permissions.
	 */
	public int doPrivilegedEndTag() throws JspException
	{
		ResponseUtils.write(pageContext, "</" + getTagString() + ">");
        
        return Tag.EVAL_PAGE;
    }
	/**
	 * Resurns an element attribute that specifies the link target of the element. (e.g. ' href=<i>url</i>')
	 */
	protected abstract String prepareLocation() throws JspException;
}
