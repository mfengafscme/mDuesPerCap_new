package org.afscme.enterprise.mailinglists.web;

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
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.person.Persona;
import org.afscme.enterprise.codes.Codes.MemberStatus;


/**
 * @struts:action   path="/viewMailingListsInformation"
 *                  scope="session"
 *                  validate="true"
 *        	    name="mailingListsInformationForm"
 *                  input="/Membership/MailingListsInformation.jsp"
 *
 * @struts:action-forward   name="View"  path="/Membership/MailingListsInformation.jsp"
 * @struts:action-forward   name="Add"  path="/Membership/MailingListsInformationAdd.jsp"
 */
public class ViewMailingListsInformationAction extends org.afscme.enterprise.controller.web.AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        MailingListsInformationForm mlInfo = (MailingListsInformationForm)form;
        if (mlInfo==null) {
            mlInfo = new MailingListsInformationForm();
        }
        
        // Forward to Mailing Lists Information Add page if add action is requested
        if (mlInfo.isAddLink()) {
            mlInfo.setAdd(null);
            
            // Fix defect 0480 - If Member Status is inactive, then do not
            // allow to add on Mailing List            
            if (mlInfo.isMLBP() &&
                s_maintainPersons.isPersona(mlInfo.getPk(), Persona.MEMBER) && 
                s_maintainMembers.getMemberStatus(mlInfo.getPk()) == MemberStatus.I.intValue()) {
                return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.mailingList.inactiveMember");
            }
            
            if (!mlInfo.isAddable()) {
                ActionErrors errors = new ActionErrors();
                if (mlInfo.isMLBP()) {
                    return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.missing.address");
                } else {
                    return makeErrorForward(request, mapping, ActionErrors.GLOBAL_ERROR, "error.field.missing.location");
                }
            }
            return mapping.findForward("Add");
        }
        
        // This page could originate from Organization, Affiliate, Person, or Member flow
        // Set header/footer, Pk, originate from, and mailing list by person flag (true if orgininated from member or person)
        // These information is mainly used for Tab, Header, and Footer supports
        String origin = (String)request.getParameter("origin");
        if (origin != null) {
            if (origin.equalsIgnoreCase("Organization")) {
                mlInfo.setPk(getCurrentOrganizationPk(request));
                mlInfo.setHeader(getCurrentOrganizationName(request));
                mlInfo.setOriginate(origin);
                mlInfo.setMLBP(false);
            } else if (origin.equalsIgnoreCase("Affiliate")) {
                mlInfo.setPk(getCurrentAffiliatePk(request));
                mlInfo.setHeader(getCurrentAffiliate(request));
                mlInfo.setOriginate(origin);
                mlInfo.setMLBP(false);
            } else if (origin.equalsIgnoreCase("Person")) {
                mlInfo.setPk(getCurrentPersonPk(request));
                mlInfo.setHeader(getCurrentPersonName(request));
                mlInfo.setOriginate(origin);
                mlInfo.setMLBP(true);
            } else if (origin.equalsIgnoreCase("Member")) {
                mlInfo.setPk(getCurrentPersonPk(request));
                mlInfo.setHeader(getCurrentPersonName(request)+" - "+mlInfo.getPk());
                mlInfo.setOriginate(origin);
                mlInfo.setMLBP(true);
            }
        }
        
        // Get mailing lists information for Organization/Affiliate or Person/Member
        List mailingList = null;
        List addressList = null;
        if (mlInfo.isMLBP()) {
            // Person Mailing Lists Information
            mailingList = s_maintainPersonMailingLists.getPersonMailingLists(mlInfo.getPk());
            addressList = s_maintainPersonMailingLists.getPersonAddresses(mlInfo.getPk());
        } else if (!mlInfo.isMLBP() && !usd.isActingAsAffiliate()) {
            // Organization Mailing Lists Information
            mailingList = s_maintainOrgMailingLists.getMailingLists(mlInfo.getPk());
            addressList = s_maintainOrgMailingLists.getOrgLocations(mlInfo.getPk());
        }
        
        // Set up some business rules ..
        mlInfo.setMailingLists(mailingList);
        mlInfo.setAddable((addressList!=null && addressList.size()>0) ? true : false);
        
        // HLM: Fix defect #103
        if (usd.isActingAsAffiliate()) {
            mlInfo.setActingAsAffiliate(true);
            mlInfo.setEditable(false);
        } else {
            mlInfo.setActingAsAffiliate(false);
            mlInfo.setPrivileges(mailingList);
        }
        
        // Load the Mailing Lists Information page
        return mapping.findForward("View");
    }
}
