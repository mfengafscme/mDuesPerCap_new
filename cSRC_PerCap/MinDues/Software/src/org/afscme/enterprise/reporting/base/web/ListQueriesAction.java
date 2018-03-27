package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Handles displaying a list of user defined queries. 
 *
 * @struts:action
 *                  path="/listQueries"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/Reporting/ListQueries.jsp"
 */

public class ListQueriesAction extends AFSCMEAction {
        
    private static String ATTRIBUTE_NAME = "myQueries";
    
    /** Retrieve a list of queries accessible by the current login user.
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // get the user info from the User Security Data
        Integer userPK = usd.getPersonPk();
        
        // retrieve the queries
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        List queries = reportAccess.getQueriesForUser(userPK);
        
        request.setAttribute(ATTRIBUTE_NAME, queries);
        return mapping.findForward("View");     
    }
    
}
