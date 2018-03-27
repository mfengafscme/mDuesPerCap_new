package org.afscme.enterprise.affiliate.web;

// Struts imports 
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.RequestUtils;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.common.web.CommentHistoryForm;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;

/** 
 * @struts:action   path="/viewAffiliateCommentHistory"
 *                  name="commentHistoryForm"
 *                  validate="false"
 *                  scope="request"
 */
public class ViewAffiliateCommentHistoryAction extends AFSCMEAction {
    
    /** Creates a new instance of ViewAffiliateCommentHistory */
    public ViewAffiliateCommentHistoryAction() {
    }
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        CommentHistoryForm chf = (CommentHistoryForm)form;
        Integer affPk = chf.getOrgPk();
        chf.setCommentHistoryFor(CommentHistoryForm.COMMENT_HISTORY_FOR_AFFILIATE);
        chf.setReturnAction("/viewAffiliateDetail.action");
        
        Collection comments = s_maintainAffiliates.getCommentHistoryForAffiliate(affPk);
        chf.setComments(comments);
        return mapping.findForward("ViewCommentHistory");
    }
    
}
