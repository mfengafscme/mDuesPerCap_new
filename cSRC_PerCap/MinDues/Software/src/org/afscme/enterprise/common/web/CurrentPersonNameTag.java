package org.afscme.enterprise.common.web;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.taglib.bean.WriteTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;


/**
 * Writes the full name of the 'current person'.  See AFSCMEAction
 */
public class CurrentPersonNameTag extends TagSupport {
    
    protected boolean showPk;
    protected boolean displayAsHeader;
    
    public int doStartTag() throws JspException {
        
        String name = AFSCMEAction.getCurrentPersonName(pageContext.getSession());
        
        if (showPk)
            name += " - " + AFSCMEAction.getCurrentPersonPk(pageContext.getSession());
        
        if (displayAsHeader) {
            String flow = AFSCMEAction.getCurrentFlow(pageContext.getSession());
            if (flow != null && flow.equalsIgnoreCase("Member"))
                name += " - " + AFSCMEAction.getCurrentPersonPk(pageContext.getSession());            
        }
        
        ResponseUtils.write(pageContext, name);
        
        return (SKIP_BODY);
    }
    
    public void setShowPk(boolean showPk) {
        this.showPk = showPk;
    }

    public void setDisplayAsHeader(boolean displayAsHeader) {
        this.displayAsHeader = displayAsHeader;
    }
    
}
