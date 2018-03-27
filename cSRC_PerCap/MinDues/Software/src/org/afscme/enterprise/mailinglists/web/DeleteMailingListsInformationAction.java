package org.afscme.enterprise.mailinglists.web;

import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.mailinglists.web.MailingListsInformationForm;

/**
 * @struts:action   path="/deleteMailingListsInformation"
 *        	    name="mailingListsInformationForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Done"  path="/viewMailingListsInformation.action" redirect="true"
 */
public class DeleteMailingListsInformationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        // Get Mailing Lists Information in session
        String mailingListPk = request.getParameter("mailingListPk");
        MailingListsInformationForm mlInfo = (MailingListsInformationForm) request.getSession().getAttribute("mailingListsInformationForm");
        if (mlInfo == null) {
            throw new RuntimeException("Mailing Lists Information is not in session.");
        }
        
        if (mailingListPk != null) {
            if (mlInfo.isMLBP()) {
                // Remove this person/member from Mailing List
                s_maintainPersonMailingLists.removePersonMailingList(mlInfo.getPk(), new Integer(mailingListPk));
            } else {
                // Remove this organization/affiliate from Mailing List
                s_maintainOrgMailingLists.removeMailingList(mlInfo.getPk(), new Integer(mailingListPk));
            }
        }
        return mapping.findForward("Done");
    }
}
