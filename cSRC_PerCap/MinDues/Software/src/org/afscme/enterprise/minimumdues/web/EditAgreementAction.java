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
import org.afscme.enterprise.minimumdues.AgreementBean;

/**
 * @struts:action   path="/EditAgreement"
 *                  name="AgreementForm"
 *                  scope="request"
 *		    		validate="false"
 *
 *
 * @struts:action-forward   name="editAgreement"  path="/Minimumdues/EditAgreement.jsp"
 * @struts:action-forward   name="backToMain"  path="/Common/MinimumDuesMainMenu.jsp?noMain=true"
 *
 */

public class EditAgreementAction extends AFSCMEAction {
  	private Connection con = null;
  	private Statement stmt = null;
  	private ResultSet rs = null;

  	//private int tmpEmpAffPk = 0;
  	private int empAffPk = 0;
  	private int affPk = 0;
  	private int empPk = 0;
  	private int wifPk = 0;

    /** Creates a new instance of EditAgreementAction */
    public EditAgreementAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

		AgreementForm af = (AgreementForm)form;
		ActionErrors errors = new ActionErrors();
		String target = "editAgreement";
		Collection results = null;
		Collection agreements = null;

		int agreementPk = 0;  //38895
		String agreementName = null;
		String tmpAgreementName = null;
		String tmpAgreementPk = null;

		AgreementDAO agreementDao = new AgreementDAO();

		tmpAgreementPk = (String) request.getParameter("agreementPk");

		if ((tmpAgreementPk != null) && (tmpAgreementPk.trim().length() != 0)) {
			agreementPk = Integer.parseInt(tmpAgreementPk);
			agreementName = agreementDao.getAgreementNameByPk(agreementPk);
			//System.out.println("*********** agreementPk edit = " + agreementPk);
			//System.out.println("*********** agreementName edit = " + agreementName);
		}

		agreements = agreementDao.getAllAgreementsPkNamePair();

		if (request.getParameter("removeFromAgreementBtn") != null) {
        	target = "editAgreement";
        	String[] selectedItems1 = af.getSelectedItems1();

        	for(int i = 0; i < selectedItems1.length; i++) {
				agreementDao.removeFromAgreement(Integer.parseInt(selectedItems1[i]), agreementPk);
			}

			//request.setAttribute("agreementName", agreementName);
			request.setAttribute("agreementPk", ""+agreementPk);
		}
		else if (request.getParameter("addToAgreementBtn") != null) {
        	target = "editAgreement";
        	String[] selectedItems2 = af.getSelectedItems2();

        	for(int i = 0; i < selectedItems2.length; i++) {
				agreementDao.addToAgreement(Integer.parseInt(selectedItems2[i]), agreementPk);
			}

			request.setAttribute("agreementPk", ""+agreementPk);
		}
		else if (request.getParameter("updateAgreementBtn") != null) {
        	target = "editAgreement";

        	agreementDao.setStartDate(af.getStartDate());
        	agreementDao.setEndDate(af.getEndDate());
        	agreementDao.setComments(af.getComments());

			agreementDao.updateAgreement(agreementPk);

			//request.setAttribute("agreementName", agreementName);
			request.setAttribute("agreementPk", ""+agreementPk);
		}
		else if (request.getParameter("deleteAgreementBtn") != null) {
        	target = "backToMain";
        	agreementDao.deleteAgreement(agreementPk);

        	return mapping.findForward(target);
		}
		else if (request.getParameter("selectAgreement") != null) {
        	target = "editAgreement";

			AgreementBean abean = agreementDao.getAgreementBean(agreementPk);

			af.setAgreementPk(""+agreementPk);
			// af.setAgreementName(agreementName);
			af.setAgreementName(""+agreementPk);
			af.setStartDate(abean.getStartDate());
			af.setEndDate(abean.getEndDate());
			af.setComments(abean.getComments());

			//request.setAttribute("AgreementForm", af);
			//request.setAttribute("agreementName", agreementName);
			request.setAttribute("agreementPk", ""+agreementPk);
		}
		else {
        	target = "editAgreement";
		}

        results = agreementDao.getAgreementCoveredEmployers(agreementPk);
		af.setResults1(results);
		//System.out.println("*********** size1 = " + results.size());

		if (results.size() == 0) {
			results = agreementDao.getAllEmployers();
		}
		else {
			results = agreementDao.getAgreementAddEligibleEmployers(agreementPk);
		}

		af.setResults2(results);
		//System.out.println("*********** size2 = " + results.size());

        request.setAttribute("agreements", agreements);
        request.setAttribute("AgreementForm", af);

		return mapping.findForward(target);
  }

}
