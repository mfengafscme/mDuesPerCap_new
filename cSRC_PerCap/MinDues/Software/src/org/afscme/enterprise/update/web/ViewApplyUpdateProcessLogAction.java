/***********************************************************************************/
/* Called by:               ApplyUpdate.jsp
 *
 * Forwards request to:     ApplyUpdateProcessLog.jsp
 *
 * Purpose:                 This action displays the search log screen which enables the 
 *                          user to search the log.
 *
 * Required:                None
 *
 *
 *
 *************************************************************************************/


package org.afscme.enterprise.update.web;

import java.util.Map;
import java.io.*;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.update.ejb.FileQueue;
import org.afscme.enterprise.util.JNDIUtil;


/**
 * @struts:action   path="/viewApplyUpdateProcessLog"
 *                  name="processLogForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Update/ApplyUpdateProcessLog.jsp"
 *
 * @struts:action-forward   name="View"  path="/Update/ApplyUpdateProcessLog.jsp"
 */
public class ViewApplyUpdateProcessLogAction extends AFSCMEAction {
    
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {      
        log.debug("Perform called================>");
        HttpSession session = request.getSession();
        if(session.getAttribute("searchLogForm") != null){
            session.removeAttribute("searchLogForm");
        }
        return mapping.findForward("View");
    }
    
}
