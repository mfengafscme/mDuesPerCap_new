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
import org.afscme.enterprise.affiliate.officer.*;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.PreparedUpdateStatementBuilder;

/**
 * @struts:action   path="/saveOfficerTitles"
 *                  name="editOfficerTitlesForm"
 *                  validate="true"
 *                  scope="request"
 *                  input="/Membership/OfficerTitlesEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/viewOfficerTitles.action"
 */
public class SaveOfficerTitlesAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response,
                                UserSecurityData usd) throws Exception {
        
        EditOfficerTitlesForm oForm = (EditOfficerTitlesForm)form;
     
        if (!isCancelled(request)) {
            //validate
            ActionErrors errors = oForm.validate(mapping, request);            
            
            // Present errors if any exists
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
            
            Integer aff_pk = getCurrentAffiliatePk(request);
            
            //update auto delegate provision flag
            
            ArrayList officerTitlesList = oForm.getOfficerTitlesList(null);
            
            OfficeData officeData = new OfficeData();
            
            for (int i=0; i<officerTitlesList.size(); i++) {            
                           
                officeData = (OfficeData) officerTitlesList.get(i);
            
                System.out.println("TermEnd  (in)     = " + officeData.getTermEnd());
                
                if (officeData.getTermEnd() == null) {
                    officeData.setTermEnd(new Integer(9999));
                }
                
                System.out.println("TermEnd  (out)     = " + officeData.getTermEnd());
                
                s_maintainAffiliateOfficers.updateOfficerTitles(officeData, usd.getPersonPk());
        
            }
        
            System.out.println("AFF_PK        = " + aff_pk);
            System.out.println("Del Provision = " + oForm.getAutoDelegateProvision());
            System.out.println("User Pk       = " + usd.getPersonPk());
            
                    
            //update auto delegate provision flag               
            s_maintainAffiliateOfficers.updateAutoDelegateProvision(aff_pk, oForm.getAutoDelegateProvision(), usd.getPersonPk()); 
                        
            //update autoexec board titles
            s_maintainAffiliateOfficers.updateAutoExecBoardTitles(aff_pk, oForm.getAffiliateTitlePk(), oForm.getSubAffiliateTitlePk(), usd.getPersonPk()); 
            
            //insert comment data    
            s_maintainAffiliateOfficers.insertCommentForOfficerTitles(aff_pk, oForm.getComment(), usd.getPersonPk());   
 
        }
               
        //show the results page
        return mapping.findForward("View");
    }
}