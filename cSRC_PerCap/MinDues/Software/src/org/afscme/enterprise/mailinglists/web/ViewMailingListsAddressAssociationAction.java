package org.afscme.enterprise.mailinglists.web;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts:action   path="/viewMailingListsAddressAssociation"
 *                  scope="request"
 *                  validate="true"
 *
 * @struts:action-forward   name="ViewMailingLists"  path="/Membership/MailingListsInformation.jsp"
 * @struts:action-forward   name="ViewAddressAssociations"  path="/Membership/MailingListsAddressAssociation.jsp"
 */
public class ViewMailingListsAddressAssociationAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        // Return to Mailing Lists Information page
        if (request.getParameter("done") != null || isCancelled(request)) {
            return mapping.findForward("ViewMailingLists");
        }
        
        // Get mailing lists information in session
        MailingListsInformationForm mlInfo = (MailingListsInformationForm) request.getSession().getAttribute("mailingListsInformationForm");
        if (mlInfo == null) {
            throw new RuntimeException("Mailing Lists Information is not in session.");
        }
        
        // Get request parameters - addressPk and mailingListPk
        MailingListsAddressAssociationForm thisForm = (MailingListsAddressAssociationForm)form;
        if (thisForm==null) {
            thisForm = new MailingListsAddressAssociationForm();
        }
        String addressPk = (String)request.getParameter("pk");
        if (request.getParameter("mailingListPk") != null) {
            thisForm.setMailingListPk(new Integer(request.getParameter("mailingListPk")));
        }
        // Set header/footer
        String mailingListName = null;
        if (mlInfo.isMLBP()) {
            mailingListName = s_maintainPersonMailingLists.getMailingListName(thisForm.getMailingListPk());             
            thisForm.setHeader("Mail Code "+thisForm.getMailingListPk()+" - "+mailingListName);            
        } else {
            mailingListName = s_maintainOrgMailingLists.getMailingListName(thisForm.getMailingListPk());             
            thisForm.setHeader("Mailing List - "+mailingListName);
        }
        // Set primary key
        thisForm.setPk(mlInfo.getPk());
        
        // Associate the selected address/location with person/organization
        if (request.getParameter("select") != null) {
            List mailingList = null;
            if (addressPk != null) {
                // Associate mailing list address with person
                if (mlInfo.isMLBP()) {
                    s_maintainPersonMailingLists.setMailingListAddress(mlInfo.getPk(), thisForm.getMailingListPk(), new Integer(addressPk), usd.getPersonPk());
                    mailingList = s_maintainPersonMailingLists.getPersonMailingLists(thisForm.getPk());
                }
                // Associate mailing list location with organization
                else {
                    s_maintainOrgMailingLists.setMailingListLocation(mlInfo.getPk(), thisForm.getMailingListPk(), new Integer(addressPk), usd.getPersonPk());
                    mailingList = s_maintainOrgMailingLists.getMailingLists(thisForm.getPk());                    
                }
            }
            // reset mailing lists information
            mlInfo.setMailingLists(mailingList);            
            mlInfo.setPrivileges(mailingList);
            return mapping.findForward("ViewMailingLists");
        }
        
        // Get the list of addresses/locations associated with person/organization
        else {
            Iterator itr;                        
            List list = null;
            addressPk = (addressPk == null) ? "0" : addressPk;
            thisForm.setCurrentAddressPk(new Integer(addressPk));

            if (mlInfo.isMLBP()) {
                // Get the list of the person's addresses
                list = s_maintainPersonMailingLists.getPersonAddresses(thisForm.getPk());
            } else {
                // Get the list of the organization's locations
                list = s_maintainOrgMailingLists.getOrgLocations(thisForm.getPk());
            }
            // Set information into request form and load the Mailing Lists Address Association page
            thisForm.setAddressList(list);
            request.setAttribute("mailingListsAddressAssociationForm", thisForm);
            return mapping.findForward("ViewAddressAssociations");
        }
    }
}