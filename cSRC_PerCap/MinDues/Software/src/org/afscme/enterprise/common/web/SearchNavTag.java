package org.afscme.enterprise.common.web;

import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.jsp.JspException;
import java.net.MalformedURLException;


/**
 * Like struts bean:write, but puts a code description, instead of a code primary key, on the page.
 */
public class SearchNavTag extends TagSupport {
	
	protected String formName;
	protected String action;
	
	/**
	 * Retrieve the required property and expose it as a scripting variable.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
		
		// Look up the requested property value
		if (formName == null)
			formName = Constants.BEAN_KEY;
		
		SearchForm form =  (SearchForm)RequestUtils.lookup(pageContext, formName, null, null);
		String url = null;
		try {
			url = RequestUtils.computeURL(pageContext, null, null, null, action, null, null, false);
		} catch (MalformedURLException e) {
			throw new JspException(e);
		}
		
		StringBuffer buf = new StringBuffer();
		if (form.isPrevPageExists())
			buf.append("<a class=\"action\" href=\""+url+"?page="+form.getPrevPage()+"\">Previous</a> | ");
		buf.append("Showing " + form.getStartIndex() + "-" + form.getEndIndex() + " of " + form.getTotal());
		if (form.isNextPageExists())
			buf.append(" | <a class=\"action\" href=\""+url+"?page="+form.getNextPage()+"\">Next</a>");
		
		ResponseUtils.write(pageContext, buf.toString());
		
		// Continue processing this page
		return (SKIP_BODY);
	}
	
	public String getFormName() {
		return formName;
	}
	
	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}

}
