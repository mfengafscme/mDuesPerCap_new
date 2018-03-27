package org.afscme.enterprise.address.web;

import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.codes.Codes.Department;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * Handles the submits from the 'Add/Edit CodeType' page.
 *
 * @struts:action   path="/savePersonAddress"
 *                  name="personAddressForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/AddressMaintainenceEdit.jsp"
 *
 * @struts:action-forward   name="AddressMaintainence"  path="/viewAddressMaintainence.action"
 * @struts:action-forward   name="ProcessReturnedMail"  path="/processReturnedMailSummary.action"
 */
public class SavePersonAddressAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        PersonAddressForm paForm = (PersonAddressForm)form;
        
        if (isCancelled(request))
            return goBack(mapping, paForm);
        
        Integer personPk = getCurrentPersonPk(request);
        PersonAddress personAddress = paForm.getPersonAddress();        
        
        //Check for the existence of sma associated with this person
        PersonAddressRecord smaAddress = s_systemAddress.getSystemAddress(personPk);
        
        //At least one address has to be flagged as Primary
        ActionErrors formErrors = new ActionErrors();
        if (smaAddress == null && !paForm.isPrimary()) {
            formErrors.add("zipCode", new ActionError("error.address.noPrimary"));        
            saveErrors(request, formErrors);
            return mapping.getInputForward();
        }
        //if the address type is home , then it must be a primary address
        // since exactly one home address is allowed and only home address can be primary
        if (paForm.getType().intValue() == 12001  && !paForm.isPrimary()){
            formErrors.add("zipCode", new ActionError("error.address.noPrimary"));        
            saveErrors(request, formErrors);
            return mapping.getInputForward();            
        }
            
        //Other form validations
        formErrors = paForm.validate(mapping, request);
        if (formErrors != null && !formErrors.isEmpty()) {
            saveErrors(request, formErrors);
            return mapping.getInputForward();
        }
        
        Set errors;
        if (usd.isActingAsAffiliate()) {
            if (paForm.isAdd()) {
            // this should never get executed.    
                errors = s_systemAddress.addByAffiliate(usd.getPersonPk(), usd.getActingAsAffiliate(), personPk, personAddress);
            } else {					
            //    errors = s_systemAddress.updateByAffiliate(usd.getPersonPk(), usd.getActingAsAffiliate(), personPk, paForm.getAddrPk(), personAddress);
            // cannot give this a department of null as updateByAffiliate does.  must be default department.    
                errors = s_systemAddress.updateByDepartment(usd.getPersonPk(), Department.MD, personPk, paForm.getAddrPk(), personAddress);                
            }
        } else {
            if (paForm.isAdd()) {
                errors = s_systemAddress.addByDepartment(usd.getPersonPk(), usd.getDepartment(), personPk, personAddress);
            } else {
                errors = s_systemAddress.updateByDepartment(usd.getPersonPk(), usd.getDepartment(), personPk, paForm.getAddrPk(), personAddress);

			}
        }
        
        if (errors != null) {
            List errorFields = Address.getErrorFields(errors);
            List errorMessages = Address.getErrorMessages(errors);
            return makeErrorForward(request, mapping, errorFields, errorMessages);
        }
        
        return goBack(mapping, paForm);
    }
    
    private ActionForward goBack(ActionMapping mapping, PersonAddressForm paForm) throws ServletException {
        String back = paForm.getBack();
        if (back.equals("ProcessReturnedMail"))
            return mapping.findForward(back);
        else if (back.equals("ListPersons") || back.equals("MemberDetail") || back.equals("PersonDetail"))
            return mapping.findForward("AddressMaintainence");
        else
            throw new ServletException("Illegal forward passed to savePersonAddress.  You need to pass a 'back' parameter to viewAddressMaintainence.action");
    }
}
