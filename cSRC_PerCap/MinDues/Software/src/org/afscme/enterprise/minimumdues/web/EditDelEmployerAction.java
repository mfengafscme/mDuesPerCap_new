package org.afscme.enterprise.minimumdues.web;

import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
//import org.apache.struts.action.ActionError;
//import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.afscme.enterprise.controller.UserSecurityData;

import org.afscme.enterprise.minimumdues.DataEntryDAO;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.EmployerData;


/**
 * @struts:action   path="/EditDelEmployer"
 *                  name="EditDelEmployerForm"
 *                  scope="request"
 *                  validate="false"
 *
 *
 *
 * @struts:action-forward   name="editEmployer"    		path="/Minimumdues/EditEmployer.jsp"
 * @struts:action-forward   name="deleteEmployer"  		path="/viewBasicAffiliateCriteria.action?new"
 * @struts:action-forward   name="updateEmployer"  		path="/AffiliateChooseAdd.action"
 * @struts:action-forward   name="cancelEditEmployer"  	path="/AffiliateChooseAdd.action"
 *
 */
public final class EditDelEmployerAction extends AFSCMEAction {

  public EditDelEmployerAction() {
  }

  public ActionForward perform(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response,
		 		 UserSecurityData usd)
    throws Exception {

    int empAffPk = 0;
    EmployerData employerData = null;
	String target = null;

    //ActionErrors errors = new ActionErrors();
    DataEntryDAO dataEntryDao = null;
    //System.out.println("***************** = " +  form.getClass().getName());
	EditDelEmployerForm edef = (EditDelEmployerForm)form;

   	try {
    	empAffPk = Integer.valueOf((String) request.getParameter("empAffPk")).intValue();
  	}
   	catch (Exception nfe) {
       	nfe.printStackTrace();
   	}

   	if (empAffPk == 0) {
    	throw new JspException("No employer ID was specified in the request.");
   	}

    if (request.getParameter("editEmpButton") != null)
    {
		target = "editEmployer";
		employerData = s_maintainAffiliates.getEmployerData(empAffPk);

		//edef = new EditDelEmployerForm();
        // Set form fields from EmployerData
        edef.setAffIdType(employerData.getType());
        edef.setAffIdState(employerData.getState());
        edef.setAffIdCouncil(""+employerData.getCouncil());
        edef.setAffIdLocal(""+employerData.getLocal());
        edef.setAffIdSubUnit(employerData.getChapter());
        edef.setEmployerName(employerData.getEmployer());
        edef.setAffIdStatus(employerData.getStatus());

 		edef.setEmpAffPk(empAffPk);

        request.setAttribute("editEmployer", edef);
    }
    else if (request.getParameter("deleteEmpButton") != null) {
		target = "deleteEmployer";

       	dataEntryDao = new DataEntryDAO();
		dataEntryDao.deleteEmployer(empAffPk);
    }
    else if (request.getParameter("saveButton") != null) {  // save button clicked to update the employer
		ActionErrors errors = new ActionErrors();
		target = "updateEmployer";

		dataEntryDao = new DataEntryDAO();
		dataEntryDao.setAffIdType(edef.getAffIdType());
		dataEntryDao.setAffIdState(edef.getAffIdState());
		dataEntryDao.setAffIdCouncil(edef.getAffIdCouncil());
		dataEntryDao.setAffIdLocal(edef.getAffIdLocal());
		dataEntryDao.setAffIdSubUnit(edef.getAffIdSubUnit());
		dataEntryDao.setEmployerName(edef.getEmployerName());
		dataEntryDao.setAffIdStatus(edef.getAffIdStatus());
		dataEntryDao.setDuesyear(edef.getDuesyear());

 		int aff_fk = dataEntryDao.checkAddOrModifyEmployerPossibility();

 		if (aff_fk != 0) {
        	dataEntryDao.updateEmployer(empAffPk, aff_fk);
		}
		else {
			errors.add("stateCouncilLocalError",new ActionError("error.stateCouncilLocalError"));
			saveErrors(request, errors);
			request.setAttribute("empAffPk", ""+empAffPk);
			target = "editEmployer";

			request.setAttribute("editEmployer", edef);

			//return (new ActionForward("/EditDelEmployer"));
			//return (new ActionForward(mapping.getInput()));
		}

      	request.setAttribute("empAffPk", ""+empAffPk);
	}
    else { // (request.getParameter("cancelEditEmpButton") != null)
		target = "cancelEditEmployer";

       	request.setAttribute("empAffPk", ""+empAffPk);
    }
//System.out.println("target ***************** = " + target);
    // Forward control to the specified success URI
    return (mapping.findForward(target));
  }
}
