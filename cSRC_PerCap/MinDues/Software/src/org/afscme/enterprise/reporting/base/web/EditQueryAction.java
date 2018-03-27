package org.afscme.enterprise.reporting.base.web;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.codes.CodeTypeData;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportOutputFieldData;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.access.ReportSortFieldData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * Handles the start of editing a custom query. 
 *
 * @struts:action   name="queryForm"
 *                  path="/editQuery"
 *                  scope="session"
 *                  validate="false"
 *
 * @struts:action-forward   name="OutputFields"  path="/Reporting/OutputFields.jsp"
 * @struts:action-forward   name="Denied"        path="/listQueries.action"
 */

public class EditQueryAction extends AFSCMEAction {
    
    /** start the first page of query wizard in editing mode
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
        
        
        // now proceed with the rest
        QueryForm qForm = (QueryForm)form;
        qForm.clearForm();
        
        MaintainCodes maintainCodes = JNDIUtil.getMaintainCodesHome().create();        
        
        /*---- Prepare the base data for the UI ---*/
        // get all fields if not gotton yet
        if (qForm.getQueryFields() == null) {
            Map allFields = reportAccess.getReportFields();
            qForm.setQueryFields(allFields);
        }
        
        if (qForm.getCodes() == null) {
            Map allCodes = new HashMap();
            Map allTypes = maintainCodes.getCodeTypes();
            
            CodeTypeData codeType;
            CodeData code;
            String codeTypeKey;
            Map codes;
            Iterator ctIt = allTypes.values().iterator();
            while (ctIt.hasNext()) {
                codeType = (CodeTypeData)ctIt.next();
                codeTypeKey = codeType.getKey();
                // get all the code values for this type key
                codes = maintainCodes.getCodes(codeTypeKey);
                allCodes.put(codeType, codes);
            }
            
            qForm.setCodes(allCodes);
        }
                
        
        // set up those fields the current user can access
        qForm.setUpUserFields(usd.getFields());
        
        /*----  retrieve the specific data for this query from the database ----*/
        // retrieve the entire query
        Report query = reportAccess.getReport(reportPk);
        Iterator it;
        
        // set up the query form with the report data
        // 1. base query data
        ReportData queryData = query.getReportData();
        qForm.setPk(queryData.getPk());
        qForm.setName(queryData.getName());
        qForm.setOriName(queryData.getName());
        qForm.setDescription(queryData.getDescription());
        qForm.setMailingList(queryData.isMailingList());
        qForm.setUpdateCorrespondenceHistory(queryData.getNeedUpdateCorrespondence());
        log.debug("@@@@@ update Corres = " + qForm.isUpdateCorrespondenceHistory());
        qForm.setCountQuery(queryData.isCountReport());
        
        // 2. output fields and orders
        Map outputFields = query.getOutputFields();
        Integer[] selectedOutputFields = new Integer[outputFields.size()];
        Integer[] previousSelectedOutputFields = new Integer[outputFields.size()];
        Map outputFieldsOrder = new HashMap();
        ReportOutputFieldData outputFieldData;
        int i = 0;
        it = outputFields.values().iterator();
        while (it.hasNext()) {
            outputFieldData = (ReportOutputFieldData)it.next();
            selectedOutputFields[i] = outputFieldData.getFieldPK();
            previousSelectedOutputFields[i++] = outputFieldData.getFieldPK();
            outputFieldsOrder.put(outputFieldData.getFieldPK().toString(), new Short(outputFieldData.getOutputOrder()));
        }
        qForm.setSelectedOutputFields(selectedOutputFields);
        qForm.setPreviousSelectedOutputFields(previousSelectedOutputFields);
        qForm.setOutputFieldsOrder(outputFieldsOrder); 
        
        // 3. selection criteria
        Map criteria = query.getCriteriaFields();
        Integer[] selectedCriteriaFields = new Integer[criteria.size()];
        Integer[] previousSelectedCriteriaFields = new Integer[criteria.size()];
        
        Integer fieldPk;
        int index = 0;
        it = criteria.keySet().iterator();
        while (it.hasNext()) {
            fieldPk = (Integer)it.next();
            
            selectedCriteriaFields[index] = fieldPk;
            previousSelectedCriteriaFields[index++] = fieldPk;
        }
        qForm.setSelectedCriteriaFields(selectedCriteriaFields);
        qForm.setPreviousSelectedCriteriaFields(previousSelectedCriteriaFields);
        qForm.setUpCriteria(criteria);
 
        
        // 4. sort criteria
        Map sortCriteria = query.getSortFields();
        ReportSortFieldData sortFieldData;
        Integer[] selectedSortFields = new Integer[sortCriteria.size()];
        Integer[] previousSelectedSortFields = new Integer[sortCriteria.size()];
        Map sortFieldsOrder = new HashMap();
        Map orderDirections = new HashMap();
        
        it = sortCriteria.values().iterator();
        index = 0;
        while (it.hasNext()) {
            sortFieldData = (ReportSortFieldData)it.next();
            fieldPk = sortFieldData.getFieldPK();
            
            selectedSortFields[index] = fieldPk;
            previousSelectedSortFields[index++] = fieldPk;
            sortFieldsOrder.put(fieldPk.toString(), new Short(sortFieldData.getFieldSortOrder()));        
            orderDirections.put(fieldPk.toString(), sortFieldData.getFieldSortDirection());
        }
        qForm.setSelectedSortFields(selectedSortFields);
        qForm.setPreviousSelectedSortFields(previousSelectedSortFields);
        qForm.setSortFieldsOrder(sortFieldsOrder);
        qForm.setOrderDirections(orderDirections);
        
        // set the owner information (user_id and pk)
        qForm.setPersonPk(usd.getPersonPk());
        qForm.setUserId(usd.getUserId());
        
        return mapping.findForward("OutputFields");   
    }
    
}
