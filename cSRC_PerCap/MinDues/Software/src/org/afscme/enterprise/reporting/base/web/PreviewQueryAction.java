package org.afscme.enterprise.reporting.base.web;

import java.util.List;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.reporting.base.web.QueryForm;

/**
 * Handles all actions on the "PreviewQuery.jsp" page
 *
 * @struts:action   name="queryForm"
 *                  path="/previewQuery"
 *                  scope="session"
 *                  validate="true"
 *                  input="/Reporting/PreviewQuery.jsp"
 */

public class PreviewQueryAction extends QueryToolAction {
    
    public ActionErrors perform(ActionMapping mapping, QueryForm qForm, HttpServletRequest request, UserSecurityData usd) throws Exception {        
        
        ActionErrors errors = new ActionErrors();
        
        String saveButton = qForm.getSaveButton();
        String saveAsButton = qForm.getSaveAsButton();
        
        if ((saveButton != null) && saveButton.equals(QueryForm.BUTTON_SAVE)) {
            
            // check for errors first
            if (qForm.getPk() == null) {    // "New" mode
                // need to make sure the queryName does not conflict with the exists ones 
                // that this user own.
                List queryNames = s_reportAccess.getReportNamesForUser(qForm.getPersonPk());
                Iterator it = queryNames.iterator();
                String name;
                while (it.hasNext()) {
                    name = (String)it.next();
                    if (name.equals(qForm.getName()))
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.queryName.duplicate"));
                }
            }
            else { // "Edit" mode
                // need to make sure the queryName is not changed by the user
                if (!qForm.getName().equals(qForm.getOriName())) 
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.queryName.changed"));
            }
            
            if (!errors.isEmpty())
                return errors;
            
            // now that we have pass the error tests
            Report query = qForm.getReport();
            
            // create or udpate the report in the database
            ReportData reportData;
            if (qForm.getPk() == null)
                reportData = s_reportAccess.createReport(query);
            else
                reportData = s_reportAccess.updateReport(query);

            // fill the form
            qForm.setPk(reportData.getPk());
            qForm.setName(reportData.getName());
            qForm.setOriName(reportData.getName());
        }
        else if ((saveAsButton != null) && saveAsButton.equals(QueryForm.BUTTON_SAVE_AS)) {
        
            
            // check for errors first
            // need to make sure the queryName does not conflict with the existing ones
            // owned by the user
            List queryNames = s_reportAccess.getReportNamesForUser(qForm.getPersonPk());
            Iterator it = queryNames.iterator();
            String name;
            while (it.hasNext()) {
                name = (String)it.next();
                if (name.equals(qForm.getName())) 
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.queryName.duplicate"));
            }
            
            if (!errors.isEmpty())
                return errors;
            
            // now that we have pass the error tests
            Report query = qForm.getReport();
            
            // create the report in the database
            ReportData reportData = s_reportAccess.createReport(query);

            // fill the form
            qForm.setPk(reportData.getPk());
            qForm.setName(reportData.getName());
            qForm.setOriName(reportData.getName());            
        }
        
        return errors;    
    }
}
