package org.afscme.enterprise.organization.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.CommentHistoryForm;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;


/**
 * Displays the Organization Associate Comment History.
 *
 * @struts:action   path="/viewOrganizationAssociateCommentHistory"
 *                  name="commentHistoryForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/CommentHistory.jsp"
 */
public class ViewOrganizationAssociateCommentHistoryAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        
        //retrieve the orgPK and personPk
        Integer personPk = getCurrentPersonPk(request, "personPk");
        Integer orgPk = getCurrentOrganizationPk(request, "orgPK");
        
        //perform the comments history for org associate
        List comments = s_maintainOrganizations.getCommentHistory(orgPk, personPk);
        
        CommentHistoryForm chForm = (CommentHistoryForm)form;
        chForm.setReturnAction("/viewOrganizationAssociateDetail.action");
        chForm.setCommentHistoryFor(CommentHistoryForm.COMMENT_HISTORY_FOR_ORG_ASSOCIATE);
        chForm.setComments(comments);
        
        return mapping.findForward("View");
    }
}
