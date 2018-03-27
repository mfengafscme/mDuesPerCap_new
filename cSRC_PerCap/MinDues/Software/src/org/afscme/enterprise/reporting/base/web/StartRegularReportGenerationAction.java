package org.afscme.enterprise.reporting.base.web;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.reporting.base.ejb.BaseReport;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.access.CriterionMap;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;
import org.afscme.enterprise.reporting.base.generator.MediaType;
import org.afscme.enterprise.reporting.base.generator.OutputFormat;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * Handles the start of generating a regular report. 
 *
 * @struts:action   name="regularReportForm"
 *                  path="/startRegularReportGeneration"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="RuntimeCriteria"  path="/Reporting/RegularRuntimeCriteria.jsp"
 * @struts:action-forward   name="GenerateScreen"   path="/Reporting/ShowReportOnScreen.jsp"
 * @struts:action-forward   name="GenerateSave"   path="/regularReportSaveAs" redirect="true"
 * @struts:action-forward   name="ListReports"   path="/listRegularReports.action"
 */

public class StartRegularReportGenerationAction extends AFSCMEAction {
  
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        // check which button pushed
        RegularReportForm rForm = (RegularReportForm)form;
        String button = rForm.getButton();
        if (button.equals("Cancel")) {
            rForm.clearForm();
            return mapping.findForward("ListReports");
        }   
        
        // get the reportPK
        Integer reportPk = rForm.getReportPk();
        
        // retrieve the form information on output formats
        String media = rForm.getMedia();      
        // retrieve the report and other information
        if (rForm.getQueryFields() == null) {
            Map allFields = s_reportAccess.getReportFields();
            rForm.setQueryFields(allFields);
        }       
        Report report = s_reportAccess.getReport(reportPk);            
        
        // Check the report to see if it has runtime editable criteria
        // if there is, we need to form the runtime criteria and 
        // go to the runtime criteria page.
        Map runtimeCriteria = new HashMap();        

        Map criteria = report.getCriteriaFields();      
        if ((criteria != null) && (!criteria.isEmpty())) {        
            Iterator it = criteria.values().iterator();
            while (it.hasNext()) {
                CriterionMap cMap = new CriterionMap();
                List criteriaList = (List)it.next();
                ReportCriterionData criterion = (ReportCriterionData)criteriaList.get(0);
                Integer fieldPk = criterion.getFieldPK();
                cMap.setFieldPk(fieldPk); 
                
                ReportField field;
                // get the field info
                if (rForm.getQueryFields().containsKey(fieldPk))
                    field = (ReportField)rForm.getQueryFields().get(fieldPk);
                else
                    field = (ReportField)rForm.getChildren().get(fieldPk);
                
                Iterator lIt = criteriaList.iterator();
                if (field.getDisplayType() == 'C') {
                    cMap.setAllCodes(s_maintainCodes.getCodes(field.getCommonCodeTypeKey()));
                    Integer[] selectedCodes = new Integer[criteriaList.size()];

                    int index = 0;
                    while (lIt.hasNext()) {
                        criterion = (ReportCriterionData)lIt.next();
                        selectedCodes[index++] = criterion.getCodePK();
                    }
                    cMap.setSelectedCodes(selectedCodes);
                }
                else {
                    while (lIt.hasNext()) {
                        criterion = (ReportCriterionData)lIt.next();
                        cMap.addCriterion(criterion); 
                    }
                }                                                                
                if (criterion.isEditable())  { // found an ediable field          
                    runtimeCriteria.put(fieldPk.toString(), cMap);
                }
            }
        }
        
        // decide where to go from here
        if (!runtimeCriteria.isEmpty()) { // go to criteria page
            rForm.setRuntimeCriteria(runtimeCriteria);
            return mapping.findForward("RuntimeCriteria");
        }
               
        if (media.equals(MediaType.SCREEN)) {     // go to on-screen generation
            doGeneration(request, report, usd);
            return mapping.findForward("GenerateScreen");
        } else if (media.equals(MediaType.SAVE)) { // Save as option
            // HLM Fix defect #755
            return mapping.findForward("GenerateSave");        
        } else {
            BaseReport baseReport = JNDIUtil.getBaseReportHome().create();
            baseReport.generateReport(usd.getUserId(), usd.getAccessibleAffiliates(), reportPk, rForm.getMediaTypeObject(), rForm.getOutputFormatObject());
        }
                           
        return mapping.findForward("ListReports");  
    }

    protected void doGeneration(HttpServletRequest request, Report report, UserSecurityData usd) throws Exception {

        List reportResult = new LinkedList();
        ReportGenerator reportGenerator = new ReportGenerator(s_reportAccess.getReportFields(), report, usd.getAccessibleAffiliates());
        reportGenerator.generateReport(reportResult);
        List fieldNames = reportGenerator.getOutputFieldNames();
        
        request.setAttribute("reportName", report.getReportData().getName());
        request.setAttribute("fieldNames", fieldNames);
        request.setAttribute("reportResult", reportResult);
    } 

}
