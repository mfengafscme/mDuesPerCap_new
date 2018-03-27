package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.OrganizationAssociateData;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.web.VerifyPersonForm;


/**
 * Processing for display of Add/Edit Organization Associate Detail pages
 *
 * @struts:action   path="/editOrganizationAssociateDetail"
 *                  input="/Membership/OrganizationAssociateDetailEdit.jsp"
 *                  name="organizationAssociateDetailForm"
 *                  scope="session"
 *                  validate="true"
 *
 * @struts:action-forward   name="Edit"  path="/Membership/OrganizationAssociateDetailEdit.jsp"
 */
public class EditOrganizationAssociateDetailAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        OrganizationAssociateDetailForm oadForm = (OrganizationAssociateDetailForm)form;

        // cancelled from dup SSN, so just re-display with form data
        if (request.getParameter("cancel") != null) {
            return mapping.findForward("Edit");            
        }
        
        Integer personPk = null;
        Integer orgPk = getCurrentOrganizationPk(request, "orgPK");

        //set person pk 
        if (oadForm.isNewPerson())
            setCurrentPerson(request, null);
        else
            personPk = getCurrentPersonPk(request, "personPk");

        //set org data
        if (oadForm.isUpdate()) {
            
            OrganizationAssociateData orgAssociateData = s_maintainOrganizations.getOrgAssociateDetail(orgPk, personPk);

            //set form fields from org associate info
            oadForm.setOrganizationAssociateData(orgAssociateData);
            oadForm.setSsn(orgAssociateData.getPersonData().getSsn());
        }
        else {

            OrganizationAssociateData orgAssociateData = new OrganizationAssociateData();
            orgAssociateData.setOrgName(s_maintainOrganizations.getOrganizationName(orgPk));
            
            //set form fields for org info
            oadForm.setOrganizationAssociateData(orgAssociateData);
        }    

        //set person data
        if (personPk != null) {
            oadForm.setPersonData(s_maintainPersons.getPersonDetail(personPk, null));
            oadForm.setSsn(oadForm.getPersonData().getSsn());
        }    
        else {
            //set form fields from person data entered in verify form            
            PersonData personData = new PersonData();
            VerifyPersonForm verifyPersonForm = (VerifyPersonForm)request.getSession().getAttribute("verifyPersonForm");
            personData.setFirstNm(verifyPersonForm.getFirstNm());
            personData.setLastNm(verifyPersonForm.getLastNm());
            personData.setSsn(verifyPersonForm.getSsn1()+verifyPersonForm.getSsn2()+verifyPersonForm.getSsn3());
            personData.setSuffixNm(verifyPersonForm.getSuffixNm());
            oadForm.setPersonData(personData);
            oadForm.setSsn(verifyPersonForm.getSsn1()+verifyPersonForm.getSsn2()+verifyPersonForm.getSsn3());
        }

        return mapping.findForward("Edit");
    }
}
