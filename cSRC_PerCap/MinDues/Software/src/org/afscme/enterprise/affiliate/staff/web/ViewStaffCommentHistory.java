package org.afscme.enterprise.affiliate.staff.web;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.CommentHistoryForm;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.LocationData;


/**
 * @struts:action   path="/viewStaffCommentHistory"
 *                  name="commentHistoryForm"
 *
 * @struts:action-forward   name="View"  path="/Membership/CommentHistory.jsp"
 */
public class ViewStaffCommentHistory extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {
        
        Integer personPk = getCurrentPersonPk(request, "personPk");
        Integer affPk = getCurrentAffiliatePk(request, "affPk");
        
        List comments = s_maintainAffiliateStaff.getCommentHistory(affPk, personPk);

        CommentHistoryForm chForm = (CommentHistoryForm)form;
        chForm.setReturnAction("/viewAffiliateStaff.action");
        chForm.setCommentHistoryFor(CommentHistoryForm.COMMENT_HISTORY_FOR_AFFILIATE_STAFF);
        chForm.setComments(comments);
        chForm.setPersonPk(personPk); //why?
        
        return mapping.findForward("View");
 }

}
