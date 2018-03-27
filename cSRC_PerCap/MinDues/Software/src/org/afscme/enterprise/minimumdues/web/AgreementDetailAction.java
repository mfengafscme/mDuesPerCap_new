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
 * @struts:action   path="/agreementDetail"
 *                  name="AgreementForm"
 *                  scope="request"
 *		    		validate="false"
 *
 *
 * @struts:action-forward   name="editAgreement"  path="/Minimumdues/AgreementDetail.jsp"
 *
 */


public class AgreementDetailAction extends AFSCMEAction {
  	private Connection con = null;
  	private Statement stmt = null;
  	private ResultSet rs = null;

  	//private int tmpEmpAffPk = 0;
  	private int empAffPk = 0;
  	private int affPk = 0;
  	private int empPk = 0;
  	private int wifPk = 0;
  	//private int empAffPkStr = 0;

    /** Creates a new instance of EditAgreementAction */
    public AgreementDetailAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

		AgreementForm af = (AgreementForm)form;
		ActionErrors errors = new ActionErrors();
		String target = "editAgreement";
		Collection results = null;
		int agreementPk = 0;

		AgreementDAO agreementDao = new AgreementDAO();

		String tmpAgreementPk_str = (String) request.getParameter("agreementPk");
		if (!((tmpAgreementPk_str == null) || (tmpAgreementPk_str.trim().length() == 0)))
			agreementPk = Integer.parseInt((String) request.getParameter("agreementPk"));
        //System.out.println("*********** agreementPk1 = " + agreementPk);

		AgreementBean agreementBean = agreementDao.getAgreementBean(agreementPk);

		if (request.getParameter("removeFromAgreementBtn") != null) {
        	target = "editAgreement";
        	String[] selectedItems1 = af.getSelectedItems1();

        	for(int i = 0; i < selectedItems1.length; i++) {
				agreementDao.removeFromAgreement(Integer.parseInt(selectedItems1[i]), agreementPk);
			}

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


        results = agreementDao.getAgreementCoveredEmployers(agreementPk);
		af.setResults1(results);

		if (results.size() == 0) {
			results = agreementDao.getAllEmployers();
		}
		else {
			results = agreementDao.getAgreementAddEligibleEmployers(agreementPk);
		}

		af.setResults2(results);


		af.setAgreementName(agreementBean.getAgreementName());
		af.setStartDate(agreementBean.getStartDate());
		af.setEndDate(agreementBean.getEndDate());
		af.setComments(agreementBean.getComments());

        //request.setAttribute("agreements", agreements);
        request.setAttribute("agreementForm", af);

		return mapping.findForward(target);
  }

}
