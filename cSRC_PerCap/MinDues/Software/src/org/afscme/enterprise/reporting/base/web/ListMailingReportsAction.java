package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Set;
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
 * Handles displaying a list of specialized mailing reports the user can access and a list
 * of user's own mailing reports.
 *
 * @struts:action
 *                  path="/listMailingReports"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="View"  path="/Reporting/ListMailingReports.jsp"
 */

public class ListMailingReportsAction extends AFSCMEAction {
        
    private static String ATTRIBUTE_NAME_1 = "specializedReports";
    private static String ATTRIBUTE_NAME_2 = "myReports";
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // get the user info from the User Security Data
        Integer userPK = usd.getPersonPk();

        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        
        // retrieve the specialized mailing reports and get rid of the one the user don't have access
        List specializedReports = reportAccess.getSpecializedReports(true);
        Set allowedReportPks = usd.getReports();
        
        ReportData reportData;
        if ((specializedReports != null) && (!specializedReports.isEmpty())) {
            Iterator it = specializedReports.iterator();
            while (it.hasNext()) {
                reportData = (ReportData)it.next();
                if (!allowedReportPks.contains(reportData.getPk()))
                    it.remove();
            }
        }
        
        // retrieve user's own mailing reports
        List myReports = reportAccess.getReportsForUser(true, userPK);
        
        request.setAttribute(ATTRIBUTE_NAME_1, specializedReports);
        request.setAttribute(ATTRIBUTE_NAME_2, myReports);
        
        return mapping.findForward("View");     
    }
    
}
