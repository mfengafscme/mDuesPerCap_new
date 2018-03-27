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
 * @struts:action   path="/viewAgreement"
 *                  name="AgreementForm"
 *                  scope="request"
 *		    		validate="false"
 *
 *
 * @struts:action-forward   name="viewAgreement"  path="/Minimumdues/ViewAgreement.jsp"
 *
 */


public class ViewAgreementAction extends AFSCMEAction {
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
    public ViewAgreementAction() {
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

		String target = "viewAgreement";

		Collection results = null;
		int agreementPk = 0;  //38895
		String agreementName = null;
		String tmpAgreementName = null;
		String tmpAgreementPk = null;

		AgreementForm af = (AgreementForm)form;
		AgreementDAO agreementDao = new AgreementDAO();

		tmpAgreementPk = (String) request.getParameter("agreementPk");
		//System.out.println("*********** tmpAgreementPk edit = " + tmpAgreementPk);
		if ((tmpAgreementPk != null) && (tmpAgreementPk.trim().length() != 0)) {
			agreementPk = Integer.parseInt(tmpAgreementPk);
			agreementName = agreementDao.getAgreementNameByPk(agreementPk);
			//System.out.println("*********** agreementPk edit = " + agreementPk);
			//System.out.println("*********** agreementName edit = " + agreementName);
		}

		AgreementBean agreementBean = agreementDao.getAgreementBean(agreementPk);
		af.setAgreementName(agreementBean.getAgreementName());
		af.setStartDate(agreementBean.getStartDate());
		af.setEndDate(agreementBean.getEndDate());
		af.setComments(agreementBean.getComments());

        results = agreementDao.getAgreementCoveredEmployersWithStat(agreementPk);
		af.setResults1(results);

        request.setAttribute("agreementPk", ""+agreementPk);
        request.setAttribute("agreementForm", af);

		return mapping.findForward(target);
  }

}
