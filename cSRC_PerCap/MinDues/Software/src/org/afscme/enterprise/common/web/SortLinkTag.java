package org.afscme.enterprise.common.web;



import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import javax.servlet.jsp.JspException;
import java.net.MalformedURLException;

/**
 * Displays a sort link above a columns in a search result display.
 * To be used in conjunction with a form derived from org.afscme.enterprise.common.web.SearchForm.
 */
public class SortLinkTag extends TagSupport {
	
	protected String formName;
	protected String action;
	protected SearchForm form;
	protected String field;
	protected String styleClass;
	protected String title;
	
	/**
	 * Retrieve the required property and expose it as a scripting variable.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
		
		// Look up the requested property value
		if (formName == null)
			formName = Constants.BEAN_KEY;
		
		form =  (SearchForm)RequestUtils.lookup(pageContext, formName, null, null);
		String url = null;
		try {
			url = RequestUtils.computeURL(pageContext, null, null, null, action, null, null, false);
		} catch (MalformedURLException e) {
			throw new JspException(e);
		}
		
		StringBuffer buf = new StringBuffer();
		String separator = url.indexOf('?') == -1 ? "?" : "&";
		String reset = "";
		int order = 1;
		String pageParam = "";
		if (form.getSortBy() != null && form.getSortBy().equals(field))
			order = form.getOrder() * -1;
		else
			pageParam = "&page=0";
		buf.append("<a href=\""+url+separator+"sortBy="+field+"&order="+order+pageParam+"\"");
		if (styleClass != null)
			buf.append(" class=\""+styleClass+"\"");
		if (title != null)
			buf.append(" title=\""+title+"\"");
		buf.append(">");
		
		ResponseUtils.write(pageContext, buf.toString());
		
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		String sortIndicator="";
		if (field.equals(form.getSortBy())) {
			if (form.getOrder() == 1)
				sortIndicator="+";
			else
				sortIndicator="-";
		}
		
		ResponseUtils.write(pageContext, "</a>"+sortIndicator);
			
        release();  //This should not be needed. Spec says release() is called after doEndTag(), Weblogic does this properly, Tomcat does not)
            
		return EVAL_PAGE;
	}
	
	public void release() {
		form = null;
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
	

	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}

	public String getStyleClass() {
		return styleClass;
	}
	
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
