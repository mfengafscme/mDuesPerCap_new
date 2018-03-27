package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.officer.AutoEBoardTitleData;
import org.afscme.enterprise.affiliate.ConstitutionData;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * Searches for affiliates, based on criteria in the selectUserAffiliatesSearchForm.
 * Params:
 *      new - if present, the user is shown the search form.
 *
 * @struts:action   path="/viewOfficerTitlesEdit"
 *		    name="editOfficerTitlesForm"
 *		    validate="false"
 *                  scope="session"
 *
 * @struts:action-forward   name="View"  path="/Membership/OfficerTitlesEdit.jsp"
 */  
public class ViewOfficerTitlesEditAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Integer affPk = getCurrentAffiliatePk(request);
                
        OfficerTitlesForm oForm = (OfficerTitlesForm)form;
        if (oForm==null) {
            oForm = new OfficerTitlesForm();
        }    
                       
        ConstitutionData constitutionData = s_maintainAffiliates.getConstitutionData(affPk);
        oForm.setApprovedConstitution(constitutionData.getApproved());
        oForm.setAutoDelegateProvision(constitutionData.getAutomaticDelegate());
        
        Collection results = s_maintainAffiliateOfficers.getOfficerTitles(affPk, oForm.getSortData());
        oForm.setResults(results);
        
        AutoEBoardTitleData autoEBoardTitleData = s_maintainAffiliateOfficers.getAutoEBoardTitleData(affPk);
        oForm.setAffiliateTitlePk(autoEBoardTitleData.getAffiliateTitlePk()); 
        oForm.setSubAffiliateTitlePk(autoEBoardTitleData.getSubAffiliateTitlePk()); 
        
        CommentData commentData = s_maintainAffiliateOfficers.getCommentForOfficerTitles(affPk);        
        request.setAttribute("comment", (commentData == null) ? null : commentData.getComment());
        
        request.setAttribute("officerTitlesForm", oForm);
                
        return mapping.findForward("View");
    }
}

