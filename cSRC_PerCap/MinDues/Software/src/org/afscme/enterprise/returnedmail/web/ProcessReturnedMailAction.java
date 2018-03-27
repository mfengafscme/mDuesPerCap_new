
package org.afscme.enterprise.returnedmail.web;

import java.util.*;
import javax.servlet.ServletException;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;
import java.io.IOException;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.ejb.AccessControl;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.returnedmail.ReturnedMailSummary;
import org.afscme.enterprise.returnedmail.ejb.ProcessReturnedMail;


/**
 * Handles the actions from the login page.
 *
 * @struts:action   name="processReturnedMailForm"
 *                  path="/processReturnedMail"
 *                  scope="session"
 *                  validate="true"
 *                  input="/Membership/ProcessReturnedMail.jsp"
 *
 * @struts:action-forward   name="Search"  path="/searchMemberBasic.action"
 * @struts:action-forward   name="Summary"  path="/Membership/ProcessReturnedMailSummary.jsp"
 * @struts:action-forward   name="Reload"  path="/Membership/ProcessReturnedMail.jsp"
 */
public class ProcessReturnedMailAction extends AFSCMEAction {
    
    static Logger log = Logger.getLogger(ProcessReturnedMailAction.class);
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd /* usd will always be null here */ ) throws Exception {
        
        ProcessReturnedMailForm returnedMailForm = (ProcessReturnedMailForm)form;
        String forwardPg = "Reload";
        
        // Request to cancel
        if (isCancelled(request)) {
            forwardPg = "MainMenu";
        }        
        // Request for basic member search
        else if (returnedMailForm.isSearchButton()) {            
            forwardPg = "Search";
        }
        // Request is submitted
        else if (returnedMailForm.isSubmitButton()) {
            // Build a collection of Address IDs
            LinkedList l_addressIds = new LinkedList();
            String s_addressIds = returnedMailForm.getAddressIds();
            StringTokenizer token = new StringTokenizer(s_addressIds,"\r\n");
            while(token.hasMoreTokens()){                
                l_addressIds.add(token.nextToken());
            }
            
            // Access bean to process returned address
            ProcessReturnedMail myBean = JNDIUtil.getProcessReturnedMailHome().create();
            ReturnedMailSummary summary = new ReturnedMailSummary();
            summary = myBean.processReturnedAddresses(l_addressIds, usd.getPersonPk());
            returnedMailForm.setSummary(summary);

            if (summary.getInvalidAddrCount().intValue() > 0 &&
                summary.getSuccessfulCount().intValue() == 0 &&
                summary.getAttemptedCount().compareTo(summary.getInvalidAddrCount()) == 0) {                    
                // Reload the page with embedded errors if invalid address Id exists
                returnedMailForm.clearActions();                    
                return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.addressId.unreadable");                
            } else {
                // Forward to the Process Returned Mail Summary page
                forwardPg = "Summary";
            }                    
        }
        returnedMailForm.clearAll();
        return mapping.findForward(forwardPg);
    }    
}

