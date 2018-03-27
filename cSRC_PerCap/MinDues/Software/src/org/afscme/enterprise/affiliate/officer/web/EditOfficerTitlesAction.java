package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.*;
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
import org.afscme.enterprise.common.*;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/editOfficerTitles"
 *		    name="editOfficerTitlesForm"
 *		    validate="false"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/OfficerTitlesEdit.jsp"
 */  
public class EditOfficerTitlesAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        Integer affPk = getCurrentAffiliatePk(request);
                
        EditOfficerTitlesForm oForm = new EditOfficerTitlesForm();
                               
        ConstitutionData constitutionData = s_maintainAffiliates.getConstitutionData(affPk);
        oForm.setApprovedConstitution(constitutionData.getApproved());
        oForm.setAutoDelegateProvision(constitutionData.getAutomaticDelegate());
        
        ArrayList officerTitlesList = new ArrayList();
        
        SortData sortData = new SortData();
        sortData.setSortField(10);
        officerTitlesList.addAll(s_maintainAffiliateOfficers.getOfficerTitles(affPk, sortData));
        
        AutoEBoardTitleData autoEBoardTitleData = s_maintainAffiliateOfficers.getAutoEBoardTitleData(affPk);
        oForm.setAffiliateTitlePk(autoEBoardTitleData.getAffiliateTitlePk()); 
        oForm.setSubAffiliateTitlePk(autoEBoardTitleData.getSubAffiliateTitlePk()); 
        
        oForm.setOfficerTitlesList(officerTitlesList);
        request.setAttribute("editOfficerTitlesForm", oForm);
        return mapping.findForward("Edit");
    }
}

