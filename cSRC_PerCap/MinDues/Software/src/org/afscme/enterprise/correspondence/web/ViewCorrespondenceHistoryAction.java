package org.afscme.enterprise.correspondence.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
// Java imports
import java.util.*;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.person.CorrespondenceData;


/**
 * @struts:action   path="/viewCorrespondenceHistory"
 *                  scope="session"
 *                  validate="true"
 *        	    	name="correspondenceHistoryInformationForm"
 *                  input="/Membership/CorrespondenceHistoryInformation.jsp"
 *
 * @struts:action-forward   name="View"  path="/Membership/CorrespondenceHistoryInformation.jsp" redirect="false"
 */
public class ViewCorrespondenceHistoryAction extends org.afscme.enterprise.controller.web.AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        CorrespondenceHistoryInformationForm chif = (CorrespondenceHistoryInformationForm)form;

		// set the current flow
		setCurrentFlow(request, chif.getOrigin());

        chif.setCorrespondenceHistoryList(s_maintainPersonMailingLists.getCorrespondenceHistory(getCurrentPersonPk(request), chif.getSortData()));
        
        // Load the Correspondence History Information page
        return mapping.findForward("View");
    }
}
