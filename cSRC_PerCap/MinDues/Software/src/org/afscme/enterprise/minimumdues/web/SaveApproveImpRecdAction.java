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
import org.afscme.enterprise.affiliate.web.PreAffiliateDetailForm;


/**
 * @struts:action   path="/saveApproveImpRecd"
 *                  name="preAffiliateDetailForm"
 *                  scope="request"
 *                  validate="false"
 *					input="/Minimumdues/MinimumDuesPreAffiliateDetailEdit.jsp"
 *
 *
 * @struts:action-forward   name="saved"    	path="/searchPreAffiliate.action"
 * @struts:action-forward   name="approved"  	path="/searchPreAffiliate.action"
 * @struts:action-forward   name="back"  		path="/searchPreAffiliate.action"
 *
 */
public final class SaveApproveImpRecdAction extends AFSCMEAction {

  public SaveApproveImpRecdAction() {
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

	PreAffiliateDetailForm padf = (PreAffiliateDetailForm)form;

	String batch_ID = padf.getBatch_ID();
	padf.setBatch_ID(batch_ID);
	String agmtEffDate  = request.getParameter("agmtEffDate");
	padf.setAgmtEffDate(agmtEffDate);
	String agmtExpDate = request.getParameter("agmtExpDate");
	padf.setAgmtExpDate(agmtExpDate);
	String noMemFeePayer = request.getParameter("noMemFeePayer");
	padf.setNoMemFeePayer(noMemFeePayer);
	String increase_type = request.getParameter("increase_type");
	padf.setIncrease_type(increase_type);
	String mbrsAfps_Affected  = request.getParameter("mbrsAfps_Affected");
	padf.setMbrsAfps_Affected(mbrsAfps_Affected);
	String adj_MbrsAfps_Affected = request.getParameter("adj_MbrsAfps_Affected");
	padf.setAdj_MbrsAfps_Affected(adj_MbrsAfps_Affected);
	String ifRecInc = request.getParameter("ifRecInc");
	padf.setIfRecInc(ifRecInc);
	String ifInNego = request.getParameter("ifInNego");
	padf.setIfInNego(ifInNego);
	String contactName = request.getParameter("contactName");
	padf.setContactName(contactName);
	String contactPhoneEmail = request.getParameter("contactPhoneEmail");
	padf.setContactPhoneEmail(contactPhoneEmail);
	String comment = request.getParameter("comment");
	padf.setComment(comment);
System.out.println("comment" + comment );
	String percentWageInc = request.getParameter("percentWageInc");
	padf.setPercentWageInc(percentWageInc);
	String wageIncEffDate = request.getParameter("wageIncEffDate");
	padf.setWageIncEffDate(wageIncEffDate);
	String noMemFeePayerAff1 = request.getParameter("noMemFeePayerAff1");
	padf.setNoMemFeePayerAff1(noMemFeePayerAff1);
	String centPerHrDoLumpSumBonus = request.getParameter("centPerHrDoLumpSumBonus");
	padf.setCentPerHrDoLumpSumBonus(centPerHrDoLumpSumBonus);
	String avgWagePerHrYr = request.getParameter("avgWagePerHrYr");
	padf.setAvgWagePerHrYr(avgWagePerHrYr);
	String effDateInc = request.getParameter("effDateInc");
	padf.setEffDateInc(effDateInc);
	String noMemFeePayerAff2 = request.getParameter("noMemFeePayerAff2");
	padf.setNoMemFeePayerAff2(noMemFeePayerAff2);

	String speWageAgj = request.getParameter("speWageAgj");
	padf.setSpeWageAgj(speWageAgj);
	String percentInc = request.getParameter("percentInc");
	padf.setPercentInc(percentInc);
System.out.println("speWageAgj" + speWageAgj );
	String dollarCent = request.getParameter("dollarCent");
	padf.setDollarCent(dollarCent);
	System.out.println("dollarCent" + dollarCent );
	String avgPay = request.getParameter("avgPay");
	padf.setAvgPay(avgPay);
	String noMemFeePayerAff3 = request.getParameter("noMemFeePayerAff3");
	padf.setNoMemFeePayerAff3(noMemFeePayerAff3);
	String notes = request.getParameter("notes");
	padf.setComment(notes);

	/*
   	try {
    	empAffPk = Integer.valueOf((String) request.getParameter("empAffPk")).intValue();
  	}
   	catch (Exception nfe) {
       	nfe.printStackTrace();
   	}
   	*/

	ActionErrors formErrors = null;

    if (request.getParameter("saveImpRecdBtn") != null)
    {
		target = "saved";
		String saveResult = "1";

        System.out.println("111111111**************************************************************************************");
        /*
        padf.setAffIdType(employerData.getType());
        padf.setAffIdState(employerData.getState());
        padf.setAffIdCouncil(""+employerData.getCouncil());
        padf.setAffIdLocal(""+employerData.getLocal());
        padf.setAffIdSubUnit(employerData.getChapter());
        padf.setEmployerName(employerData.getEmployer());
        padf.setAffIdStatus(employerData.getStatus());
        */

		saveResult = s_maintainAffiliates.updatePreAffInfo(batch_ID, agmtEffDate,agmtExpDate,
						noMemFeePayer, increase_type, mbrsAfps_Affected, adj_MbrsAfps_Affected,
						ifRecInc, ifInNego, contactName, contactPhoneEmail, comment, notes, percentWageInc,
						wageIncEffDate, noMemFeePayerAff1,centPerHrDoLumpSumBonus, avgWagePerHrYr,
						effDateInc, noMemFeePayerAff2, speWageAgj,percentInc, dollarCent, avgPay,
						noMemFeePayerAff3);

		if (saveResult.equals("1")) {
			// saved successfully and forward to search result screen
			target = "saved";
		}
		else {
			// save failed and foward to the same screen
			formErrors = new ActionErrors();

			formErrors.add("preaffiliateapprove", new ActionError("error.preemployersave.nosave"));
			saveErrors(request, formErrors);

			return mapping.getInputForward();
		}

        request.setAttribute("savedForm", padf);
    }
    else if (request.getParameter("approveImpRecdBtn") != null) {
		target = "approved";
		String insertResult = "1";

        System.out.println("22222222222**************************************************************************************");
       	// dataEntryDao = new DataEntryDAO();
		// dataEntryDao.deleteEmployer(empAffPk);
		// call SP
		insertResult = s_maintainAffiliates.insertPreAffInfoProc(batch_ID, agmtEffDate,agmtExpDate,
						noMemFeePayer, increase_type, mbrsAfps_Affected, adj_MbrsAfps_Affected,
						ifRecInc, ifInNego, contactName, contactPhoneEmail, comment, notes, percentWageInc,
						wageIncEffDate, noMemFeePayerAff1,centPerHrDoLumpSumBonus, avgWagePerHrYr,
						effDateInc, noMemFeePayerAff2, speWageAgj,percentInc, dollarCent, avgPay,
						noMemFeePayerAff3);


		if (insertResult.equals("1")) {
			// saved successfully and forward to search result screen
			target = "approved";

		}
		else {
			// insert failed and foward to the same screen
			formErrors = new ActionErrors();

			formErrors.add("preaffiliateapprove", new ActionError("error.preemployerapp.noapprove"));
			saveErrors(request, formErrors);

			return mapping.getInputForward();
		}
    }
    else {
		target = "back";


	}

    return (mapping.findForward(target));
  }
}
