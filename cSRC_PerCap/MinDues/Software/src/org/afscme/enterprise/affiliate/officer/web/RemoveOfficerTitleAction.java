package org.afscme.enterprise.affiliate.officer.web;

import java.util.*;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.officer.OfficeData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.officer.AutoEBoardTitleData;
import org.afscme.enterprise.affiliate.ConstitutionData;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.util.*;


/**
 * Handles the submits from the 'Add/Edit CodeType' page. 
 *
 * @struts:action   path="/removeOfficerTitle"
 *					name="officerTitlesForm"
 *                                      scope="request"
 *                                      validate="false"
 *                                      input="/Membership/OfficerTitles.jsp"
 *
 * @struts:action-forward   name="Done"  path="/viewOfficerTitles.action" 
 */
public class RemoveOfficerTitleAction extends AFSCMEAction {

    
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
	        Integer affPk = getCurrentAffiliatePk(request);
                String afscmeOfficePk = (String) request.getParameter("afscmeOfficePk");
                String officeGroupId = (String) request.getParameter("officeGroupId");  
                                
                if (afscmeOfficePk == null || officeGroupId == null){
                   throw new Exception("AFSCME Office Pk and Office Group Id require values");
                }
                
                boolean removed = s_maintainAffiliateOfficers.removeOfficerTitle(affPk, new Integer (officeGroupId), new Integer (afscmeOfficePk));
		
                ActionErrors errors = new ActionErrors();
                if (removed == false){
                    errors.add("removed", new ActionError("error.officer.title.removal"));        
                    saveErrors(request, errors);
                                      
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

                    return mapping.getInputForward();            
                
                } else { 
                
                    return mapping.findForward("Done");
                }
	}
}
