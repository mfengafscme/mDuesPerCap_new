package org.afscme.enterprise.affiliate.officer.web;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.CommentHistoryForm;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.LocationData;


/**
 * @struts:action   path="/viewOfficerTitlesCommentHistory"
 *                  name="commentHistoryForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/CommentHistory.jsp"
 */
public class ViewOfficerTitlesCommentHistory extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        
        Integer affPk = getCurrentAffiliatePk(request, "affPk");
        
        Collection comments = s_maintainAffiliateOfficers.getCommentHistoryForOfficerTitles(affPk);

        CommentHistoryForm chForm = (CommentHistoryForm)form;
        chForm.setReturnAction("/viewOfficerTitles.action");
        chForm.setCommentHistoryFor(CommentHistoryForm.COMMENT_HISTORY_FOR_OFFICER_TITLES);
        chForm.setComments(comments);
                
        return mapping.findForward("View");
 }

}
