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
import org.afscme.enterprise.reporting.base.web.QueryForm;
import org.apache.struts.action.ActionErrors;

/**
 * Handles all actions on the "SelectionCriteriaFields.jsp" page.
 *
 * @struts:action   name="queryForm"
 *                  path="/selectionCriteriaFields"
 *                  scope="session"
 *                  validate="false"
 */

public class SelectionCriteriaFieldsAction extends QueryToolAction {
    
    public ActionErrors perform(ActionMapping mapping, QueryForm qForm, HttpServletRequest request, UserSecurityData usd) throws Exception { 
        // if the selection has changed from the previous selection, we need to
        // clear data in page four (Selection Criteria).
        if (qForm.hasSelectionCriteriaFieldsChanged()) {
            resetPreviousSelection(qForm);
            
            // change the "criteria" according to the field changes
            qForm.resetCriteria();
        }
        
        return null;
    }
    
    private void resetPreviousSelection(QueryForm form) {
        Integer[] currentSelection = form.getSelectedCriteriaFields();
        if ((currentSelection == null) || (currentSelection.length == 0)) {
            form.setPreviousSelectedCriteriaFields(null);
            return;
        }
        
        Integer[] previousSelection = new Integer[currentSelection.length];
        for (int i = 0; i < currentSelection.length; i++) 
            previousSelection[i] = new Integer(currentSelection[i].intValue());
        
        form.setPreviousSelectedCriteriaFields(previousSelection);        
    } 
    
}
