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
import org.afscme.enterprise.minimumdues.AgreementDAO;


/**
 * @struts:action   path="/AddAgreement"
 *                  name="AgreementForm"
 *                  scope="request"
 *		    		validate="false"
 *
 *
 * @struts:action-forward   name="addAgreement"  path="/Minimumdues/AddAgreement.jsp"
 * @struts:action-forward   name="AgreementDetail"  path="/Minimumdues/AgreementDetail.jsp"
 *
 */

public class AddAgreementAction extends AFSCMEAction {
  	private Connection con = null;
  	private Statement stmt = null;
  	private ResultSet rs = null;

  	//private int tmpEmpAffPk = 0;
  	private int empAffPk = 0;
  	private int affPk = 0;
  	private int empPk = 0;
  	private int wifPk = 0;
  	//private int empAffPkStr = 0;

    /** Creates a new instance of AddAgreementAction */
    public AddAgreementAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

		AgreementForm aef = (AgreementForm)form;
		ActionErrors errors = new ActionErrors();
		String target = "addAgreement";
		int agreementPk = 0;
		Collection results = null;

		if (request.getParameter("addNewAgreementBtn") != null) {

			AgreementDAO agreementDao = new AgreementDAO();

			if (!((aef.getAgreementName() == null) || (aef.getAgreementName().trim().equalsIgnoreCase(""))))
			{
				target = "AgreementDetail";

				agreementPk = agreementDao.getAgreementPkByName(aef.getAgreementName());

				if (agreementPk == 0) {
					agreementDao.setAgreementName(aef.getAgreementName());
					agreementDao.setStartDate(aef.getStartDate());
					agreementDao.setEndDate(aef.getEndDate());
					agreementDao.setComments(aef.getComments());
					agreementPk = agreementDao.addAgreement();

					aef.setAgreementPk(""+agreementPk);
				}
				else {
					errors.add("duplicateAgreementAddError",new ActionError("error.duplicateAgreementAdd"));
					saveErrors(request, errors);
				}
			}

			results = new ArrayList();
			aef.setResults1(results);
			System.out.println("*********** size1 = " + results.size());

			results = agreementDao.getAllEmployers();

			aef.setResults2(results);
			System.out.println("*********** size2 = " + results.size());
		}

		request.setAttribute("agreementPk", ""+agreementPk);
		request.setAttribute("add", "yes");
		request.setAttribute("agreementForm", aef);

		return mapping.findForward(target);
  }

}
