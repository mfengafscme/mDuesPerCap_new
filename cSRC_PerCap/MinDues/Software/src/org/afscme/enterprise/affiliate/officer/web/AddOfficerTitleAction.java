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
import org.afscme.enterprise.affiliate.officer.OfficeData;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/addOfficerTitle"
 *		    name="addOfficerTitleForm"
 *		    validate="false"
 *                  scope="request"
 *                  input="/Membership/OfficerTitlesAdd.jsp"
 * @struts:action-forward   name="View"  path="/viewOfficerTitles.action"
 * @struts:action-forward   name="Add"   path="/Membership/OfficerTitlesAdd.jsp"
 *
 */
public class AddOfficerTitleAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
              
        AddOfficerTitleForm oForm = (AddOfficerTitleForm)form;
        if (oForm == null)
            oForm = new AddOfficerTitleForm();
        
        Integer affPk = getCurrentAffiliatePk(request);
        
        oForm.setPresidentExists(new Boolean(s_maintainAffiliateOfficers.checkForPresident(affPk)));
                
        if (request.getParameter("add") != null) {
            request.setAttribute("addOfficerTitleForm", oForm);
            return mapping.findForward("Add");
        }
        
        if (!isCancelled(request)) {
            //validate
            ActionErrors errors = oForm.validate(mapping, request);            
            
            // Present errors if any exists
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
            
            Integer aff_pk = getCurrentAffiliatePk(request);
            
            //check if need to reset reporting officer flags
            if (oForm.getReportingOfficer() != null && oForm.getReportingOfficer().booleanValue() == true){
		/* Tim, commented this out as it was not found and was causing a compiled error(could not find symbol). 
		Perhaps the EJB needs to be checked in ? */            
		//  s_maintainAffiliateOfficers.resetReportingOfficerFlags(aff_pk);   
            } 
            
            //add a new officer title here
            OfficeData officeData = new OfficeData();
            officeData.setAffPk(aff_pk);
            officeData.setAfscmeTitle(oForm.getAfscmeTitle());
            officeData.setOfficePk(oForm.getAfscmeTitle());
            officeData.setAffiliateTitle(oForm.getAffiliateTitle());
            officeData.setNumWithTitle(oForm.getNumWithTitle());
            officeData.setMonthOfElection(oForm.getMonthOfElection());
            if(oForm.getAfscmeTitle().intValue() == 46) {
               officeData.setLengthOfTerm(new Integer(63005));
               officeData.setTermEnd(new Integer(9999));
            } else if ((oForm.getLengthOfTerm().intValue() == 63005) || 
                      (oForm.getLengthOfTerm().intValue() == 63005)){
                   officeData.setTermEnd(new Integer(9999));     
            } else {
               officeData.setLengthOfTerm(oForm.getLengthOfTerm());
               officeData.setTermEnd(oForm.getTermEnd());
            }
            officeData.setDelegatePriority(oForm.getDelegatePriority());
            officeData.setReportingOfficer(oForm.getReportingOfficer()); 
            officeData.setExecBoard(oForm.getExecBoard()); 
            
                        
            s_maintainAffiliateOfficers.addOfficerTitle(officeData, usd.getPersonPk());
                        
        }
        //show the results page
        return mapping.findForward("View");
    }
}

