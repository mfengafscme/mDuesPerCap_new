package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Handles the deleting of a custom query. 
 *
 * @struts:action
 *                  path="/deleteQuery"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"     path="/listQueries.action"
 * @struts:action-forward   name="Denied"   path="/listQueries.action"
 */

public class DeleteQueryAction extends AFSCMEAction {
    
    /** delete a query
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // get the report PK from the request parameter
        Integer reportPk = new Integer(request.getParameter("rpk"));
        
        // check if this query is owned by the current user
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        List userQueries = reportAccess.getQueriesForUser(usd.getPersonPk());
        Iterator qIt = userQueries.iterator();
        ReportData reportData;
        boolean found = false;
        while (qIt.hasNext()) {
            reportData = (ReportData)qIt.next();
            if (reportData.getPk().equals(reportPk)) {
                found = true;
                break;
            }
        }
        if (!found) 
            return mapping.findForward("Denied");
        
        // now proceed with deletion
        reportAccess.deleteReport(reportPk);
        
        return mapping.findForward("View");     
    }
    
}
