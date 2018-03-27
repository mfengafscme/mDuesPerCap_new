package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.generator.MediaType;

/**
 * Handles the start of generating a specialized report for download.
 * For now, it's dealing with Enriched Data report.
 *
 * @struts:action   name="specializedReportForm"
 *                  path="/startSpecializedReportGeneration"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="GenerateSave"   path="/specializedReportSaveAs" redirect="true"
 * @struts:action-forward   name="Return"   path="/showMain.action"
 */
public class StartSpecializedReportGenerationAction extends AFSCMEAction {
      
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // check which button pushed
        SpecializedReportForm sForm = (SpecializedReportForm)form;
        if (!isCancelled(request)) {
            return mapping.findForward("GenerateSave");        
        }
        return mapping.findForward("Return");          
    }
}
