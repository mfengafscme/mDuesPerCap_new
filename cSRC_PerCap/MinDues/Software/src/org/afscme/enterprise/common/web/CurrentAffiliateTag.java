package org.afscme.enterprise.common.web;

// Java imports
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;

// Struts imports
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

// AFSCME imports 
import org.afscme.enterprise.controller.web.AFSCMEAction;

public class CurrentAffiliateTag extends TagSupport {
    
    /** Creates a new instance of AffiliateHeaderFooterTag */
    public CurrentAffiliateTag() {
    }
	
	/**
	 * 
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
            String affil = AFSCMEAction.getCurrentAffiliate(pageContext.getSession());
            ResponseUtils.write(pageContext, affil);
        
            return (SKIP_BODY);
        }
    
}
