package org.afscme.enterprise.minimumdues.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.*;
import java.sql.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.minimumdues.DataEntryDAO;


/**
 * @struts:action   path="/AddEmployer"
 *                  name="AddEmployerForm"
 *                  scope="request"
 *		    		validate="false"
 *		    		input="/Minimumdues/AddEmployer.jsp"
 *
 *
 * @struts:action-forward   name="addEmployer"  path="/Minimumdues/AddEmployer.jsp"
 *
 */

public class AddEmployerAction extends AFSCMEAction {
  	private Connection con = null;
  	private Statement stmt = null;
  	private ResultSet rs = null;

  	//private int tmpEmpAffPk = 0;
  	private int empAffPk = 0;
  	private int affPk = 0;
  	private int empPk = 0;
  	private int wifPk = 0;
  	//private int empAffPkStr = 0;

    /** Creates a new instance of AddAffiliateDetailAction */
    public AddEmployerAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

      AddEmployerForm aef = (AddEmployerForm)form;
      ActionErrors errors = new ActionErrors();
      String target = "addEmployer";

	  if (request.getParameter("addEmployerButton") != null) {
			String affIdType = aef.getAffIdType();
			String affIdState = aef.getAffIdState();
			String affIdCouncil = aef.getAffIdCouncil();
			String affIdLocal = aef.getAffIdLocal();
			String affIdSubUnit = aef.getAffIdSubUnit();
			String employerName = aef.getAffilName();
			//String affIdStatus = aef.getAffIdStatus();
			String affIdStatus = "1";

			DataEntryDAO dataEntryDao = new DataEntryDAO();
			dataEntryDao.setAffIdState(affIdState);
			dataEntryDao.setAffIdType(affIdType);
			dataEntryDao.setAffIdCouncil(affIdCouncil);
			dataEntryDao.setAffIdLocal(affIdLocal);
			dataEntryDao.setAffIdSubUnit(affIdSubUnit);
			dataEntryDao.setEmployerName(employerName);
			dataEntryDao.setAffIdStatus(affIdStatus);


			int aff_fk = dataEntryDao.checkAddOrModifyEmployerPossibility();

			if (aff_fk != 0) {
				int empAffPk = dataEntryDao.checkEmployerExist();

				if (empAffPk != 0)  {
					errors.add("duplicateEmployerAddError",new ActionError("error.duplicateEmpAdd"));
					saveErrors(request, errors);
					return (new ActionForward(mapping.getInput()));
				}

				dataEntryDao.addEmployer(aff_fk);
			}
			else {
				errors.add("stateCouncilLocalError",new ActionError("error.stateCouncilLocalError"));
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}


			//target = "backToMain";
			aef.setAffIdType("");
			aef.setAffIdState("");
			aef.setAffIdCouncil("");
			aef.setAffIdLocal("");
			aef.setAffIdSubUnit("");
			aef.setAffilName("");
	  }


      return mapping.findForward(target);
  }

}
