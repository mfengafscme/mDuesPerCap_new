package org.afscme.enterprise.reporting.specialized.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;

/**
 * Display the output format page.
 *
 * @struts:action   name="enrichedDataForm"
 *                  path="/enrichedDataReportOutputFormat"
 *		    input="/Reporting/Specialized/ViewEnrichedDataCriteria.jsp"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="Output"  path="/Reporting/Specialized/DownloadOutputFormat.jsp"
 */

public class EnrichedDataReportOutputFormatAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        EnrichedDataForm eForm = (EnrichedDataForm)form;
        
        // get pk from form in case finder was used...
        Integer affPk = eForm.getAffPk();
        if (affPk == null || affPk.intValue() < 1) {
            // otherwise find it...
            affPk = s_maintainAffiliates.getAffiliatePk(eForm.getAffId());
        }
        
        // display error if affPk is not selected
        if (affPk == null) {
            log.debug("affPk was null");
            return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.update.invalidAffiliateId");
        } else if (affPk.intValue() < 1 ) {
            log.debug("affPk was " + affPk);
            return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.codes.affiliate." + affPk.intValue());
        }
        
        // forward to Output Format selection screen
        SpecializedReportForm sForm = new SpecializedReportForm();
        sForm.setAffPk(affPk);
        sForm.setReportName("Enriched Data");
        request.getSession().setAttribute("specializedReportForm", sForm);
        return mapping.findForward("Output");
    }
}
