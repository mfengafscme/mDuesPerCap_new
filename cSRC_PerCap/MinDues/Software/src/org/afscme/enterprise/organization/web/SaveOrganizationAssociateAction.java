package org.afscme.enterprise.organization.web;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.OrganizationAssociateData;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.web.VerifyPersonForm;
import org.afscme.enterprise.util.TextUtil;


/**
 * Handles the submits from the 'Add/Edit Organization Associate Detail' page. 
 *
 * @struts:action   path="/saveOrganizationAssociate"
 *                  input="/Membership/OrganizationAssociateDetailEdit.jsp"
 *                  name="organizationAssociateDetailForm"
 *                  scope="session"
 *                  validate="true"
 *
 * @struts:action-forward   name="ViewAssociate"  path="/viewOrganizationAssociateDetail.action" redirect="true"
 * @struts:action-forward   name="ViewResults"  path="/viewVerifyPerson.action?back=AssociateAdd" redirect="true"
 * @struts:action-forward   name="ViewVerify"  path="/verifyPerson.action?cancel" redirect="true"
 * @struts:action-forward   name="PersonDuplicateSSN"  path="/viewDuplicateSSNNotifierPerson.action"
 * @struts:action-forward   name="SelectLocation"  path="/viewLocationSelection.action?back=AssociateDetail" redirect="true"
 */
public class SaveOrganizationAssociateAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        Integer personPk = getCurrentPersonPk(request);
        Integer orgPk = getCurrentOrganizationPk(request);

        HttpSession session = request.getSession();
        
        // set the back attribute in the session so all pages can get it
        session.setAttribute("back", "AssociateAdd");
    
        if (isCancelled(request)) {

            //determine if add or edit
            boolean addNew = (!((OrganizationAssociateDetailForm)session.getAttribute("organizationAssociateDetailForm")).isUpdate());
            int resultsCount = 0;
            if (session.getAttribute("verifyPersonForm") != null) {
                resultsCount = (((VerifyPersonForm)session.getAttribute("verifyPersonForm")).getTotal());
            }    

            session.removeAttribute("organizationAssociateDetailForm");
            
            if (personPk == null)
                if (resultsCount > 0) 
                    //new org associate with verify results
                    return mapping.findForward("ViewResults");
                else
                    //new org associate with no results
                    return mapping.findForward("ViewVerify");
            else
                if (addNew)
                    if (resultsCount > 0) 
                        //new org associate with verify results
                        return mapping.findForward("ViewResults");
                    else
                        //new org associate with no results
                        return mapping.findForward("ViewVerify");
                else
                    //edit org associate
                    return mapping.findForward("ViewAssociate");
        }

        OrganizationAssociateDetailForm oadForm = (OrganizationAssociateDetailForm)form;
        OrganizationAssociateData associateData = oadForm.getOrganizationAssociateData();

        //ignore ssn dup check if already checked and continuing
        if (!oadForm.isIgnoreSsnDup()) {
            
            //only check for dup ssn if ssn can be changed 
            //no need to check for adding org associate for existing person
            if (oadForm.isNewPerson() || oadForm.isUpdate()) {
                //check for Duplicate SSN
				if (oadForm.getPreviousSsn() != null)
				{
		   			if (!oadForm.getPreviousSsn().equals(associateData.getPersonData().getSsn()))
					{
                    	if (s_maintainPersons.isDuplicateSSN(associateData.getPersonData().getSsn())) 
						{
                    		return mapping.findForward("PersonDuplicateSSN");
						}
           			}else if (oadForm.getPreviousSsn().equals(associateData.getPersonData().getSsn()))
					{
                    	if (s_maintainPersons.isDuplicateSSNGreaterThan1(associateData.getPersonData().getSsn())) 
						{
                    		return mapping.findForward("PersonDuplicateSSN");						
						}				
					}

					
				}else
				{
                	if (s_maintainPersons.isDuplicateSSN(associateData.getPersonData().getSsn())) {
                    	return mapping.findForward("PersonDuplicateSSN");
                	}
				}
            }    
        }
        
        //get org associate data from form
        Integer orgPositionTitle = associateData.getOrgPositionTitle();
        if ((orgPositionTitle != null) && (orgPositionTitle.intValue() == 0))
            orgPositionTitle = null;
        String comment = oadForm.getComment();


        //UPDATE
        if (oadForm.isUpdate()) {

            //updating organization associate detail and person detail
            PersonData personData = oadForm.getPersonData();
            personData.setPersonPk(personPk);
            s_maintainOrganizations.updateOrgAssociateDetail(orgPk, personData, orgPositionTitle, comment, usd.getPersonPk());
            
            //check for a name change.
            //If so, then add to weekly card run and reset name display on header/footer tags.
            String currentName = getCurrentPersonName(session);
            String newName = s_maintainUsers.getPersonName(personPk);
            if (!TextUtil.equals(currentName, newName)) {

                // add to weekly card run so member gets new card for changed name
                s_maintainMembers.addToWeeklyCardRun(personPk);
                // Reset person name in session so headers/footers display correctly
                setCurrentPersonName(request, newName);
            }
            
        }  
        else {

            //ADD
            if (oadForm.isNewPerson()) {

                //add new person
                NewPerson newPerson = oadForm.getNewPerson();
                String ssn = oadForm.getNewPerson().getSsn();
                personPk = s_maintainOrganizations.addPersonAssoc(orgPk, newPerson, orgPositionTitle, comment, usd.getPersonPk());
                setCurrentPerson(request, personPk);
            } 
            else {

                //adding organization associate to existing person
                s_maintainOrganizations.setPersonAssoc(orgPk, personPk, orgPositionTitle, comment, usd.getPersonPk());
            }
            
            //check if location for org associate is null and if there are locations for the org, 
            //and if so, let location selection be chosen
            if ((s_maintainOrganizations.getOrgAssociateLocation(orgPk, personPk) == null) &&
                (s_maintainOrgLocations.hasLocations(orgPk))) {
                
                request.getSession().removeAttribute("organizationAssociateDetailForm");
                return mapping.findForward("SelectLocation");
            }    
        }

        session.removeAttribute("organizationAssociateDetailForm");
        return mapping.findForward("ViewAssociate");
    }
}
