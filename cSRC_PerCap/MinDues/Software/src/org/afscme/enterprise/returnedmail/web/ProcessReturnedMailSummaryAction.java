
package org.afscme.enterprise.returnedmail.web;

import javax.servlet.ServletException;
import java.util.StringTokenizer;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
import java.io.IOException;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ejb.AccessControl;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.controller.web.AFSCMEAction;

/**
 * Handles the actions from the login page.
 *
 * @struts:action  
 *                  path="/processReturnedMailSummary"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Membership/ProcessReturnedMailSummary.jsp"
 *
 * @struts:action-forward   name="Summary"  path="/Membership/ProcessReturnedMailSummary.jsp"
 */
public class ProcessReturnedMailSummaryAction extends AFSCMEAction {
    
    static Logger log = Logger.getLogger(ProcessReturnedMailSummaryAction.class);
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd /* usd will always be null here */ ) throws Exception {            
                
	if (request.getParameter("done") != null) {
            request.getSession().removeAttribute("processReturnedMailForm");
            return mapping.findForward("MainMenu");
        }
        return mapping.findForward("Summary");
    }
}
