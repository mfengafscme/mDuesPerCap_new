package org.afscme.enterprise.common.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.ResponseUtils;
import org.afscme.enterprise.controller.web.AFSCMEAction;

/**
 * Writes the name of the 'current organization'.  See AFSCMEAction
 */
public class CurrentOrganizationNameTag extends TagSupport {

    public int doStartTag() throws JspException {
        
        String name = AFSCMEAction.getCurrentOrganizationName(pageContext.getSession());
        ResponseUtils.write(pageContext, name);
        
        return (SKIP_BODY);
    }
}
