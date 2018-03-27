package org.afscme.enterprise.reporting.base.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.reporting.base.access.CriterionMap;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.ejb.BaseReport;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Handles the start of generating a mailing list report. 
 *
 * @struts:action   name="mailingReportForm"
 *                  path="/startMailingReportGeneration"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="RuntimeCriteria"  path="/Reporting/MailingRuntimeCriteria.jsp"
 * @struts:action-forward   name="Generate"         path="/listMailingReports.action"
 * @struts:action-forward   name="Cancel"           path="/listMailingReports.action"
 */

public class StartMailingReportGenerationAction extends AFSCMEAction {
  
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // check which button pushed
        MailingReportForm mForm = (MailingReportForm)form;
        String button = mForm.getButton();
        if (button.equals("Cancel")) {
            mForm.clearForm();
            return mapping.findForward("Cancel");
        }
        
        // get the reportPK
        Integer reportPk = mForm.getReportPk();
        
        // retrieve the form information on output formats
        String format = mForm.getOutputFormat();
        
        // retrieve the report and other information
        ReportAccess reportAccess = JNDIUtil.getReportAccessHome().create();
        if (mForm.getQueryFields() == null) {
            Map allFields = reportAccess.getReportFields();
            mForm.setQueryFields(allFields);
        }
        Report report = reportAccess.getReport(reportPk);                    
        
        // check the report to see if it has runtime editable criteria
        // if there is, we need to form the runtime criteria and 
        // go to the runtime criteria page.
        Map runtimeCriteria = new HashMap();        

        Map criteria = report.getCriteriaFields();
        ReportCriterionData criterion;
        CriterionMap cMap;
        List criteriaList;
        Iterator cIt;
        String fieldPkStr;
        if ((criteria != null) && (!criteria.isEmpty())) {
            Iterator it = criteria.values().iterator();
            while (it.hasNext()) {
                criteriaList = (List)it.next();
                criterion = (ReportCriterionData)criteriaList.get(0);
                fieldPkStr = criterion.getFieldPK().toString();
                if (criterion.isEditable())  { // found an ediable field
                    cMap = new CriterionMap();
                    cIt = criteriaList.iterator();
                    while (cIt.hasNext()) {
                        criterion = (ReportCriterionData)cIt.next();
                        cMap.addCriterion(criterion);
                    }
                    runtimeCriteria.put(fieldPkStr, cMap);
                }
            }
        }
        
        // decide where to go from here
        if (!runtimeCriteria.isEmpty()) { // go to criteria page
            mForm.setRuntimeCriteria(runtimeCriteria);
            return mapping.findForward("RuntimeCriteria");
        }
            
        // generate the mailing list report
        BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
        baseReport.generateReport(usd.getUserId(), usd.getAccessibleAffiliates(), reportPk, mForm.getMediaTypeObject(), mForm.getOutputFormatObject(), mForm.isFilterDuplicateAddressesEnabled());
        
                           
        return mapping.findForward("Generate");   
    }
    
}
