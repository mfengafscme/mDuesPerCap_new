/*
 * ViewCommentHistoryAction.java
 *
 * Created on May 29, 2003, 11:49 AM
 */

package org.afscme.enterprise.person.web;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.afscme.enterprise.common.web.CommentHistoryForm;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.TextUtil;

/**
 * Views the Person Comment History.
 *
 * @struts:action   path="/viewCommentHistory"
 *                  name="commentHistoryForm"
 *                  validate="false"
 *                  scope="request"
 *                  input="/Membership/PersonDetail.jsp"
 *
 */
public class ViewCommentHistoryAction extends AFSCMEAction {
    //
    /** Creates a new instance of ViewCommentHistoryAction */
    public ViewCommentHistoryAction() {
    }
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     *
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response, 
            UserSecurityData usd) throws Exception {
                
        CommentHistoryForm chf = (CommentHistoryForm)form;
        chf.setCommentHistoryFor(CommentHistoryForm.COMMENT_HISTORY_FOR_PERSON);

        Integer personPk = chf.getPersonPk();
        chf.setReturnAction("/viewPersonDetail.action?personPk=" + personPk);

        Collection comments = s_maintainPersons.getCommentHistory(personPk);
        chf.setComments(comments);
        
        return mapping.findForward("ViewCommentHistory");
    }
    
}
