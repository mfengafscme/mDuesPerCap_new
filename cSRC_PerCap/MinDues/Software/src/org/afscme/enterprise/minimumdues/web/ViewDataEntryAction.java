package org.afscme.enterprise.minimumdues.web;

import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.afscme.enterprise.controller.UserSecurityData;

import org.afscme.enterprise.minimumdues.DataEntryDAO;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.minimumdues.*;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/ViewDataEntry"
 *                  name="ViewDataEntryForm"
 *                  scope="request"
 *                  validate="false"
 *
 *
 * @struts:action-forward   name="viewDataEntry"  path="/Minimumdues/ViewDataEntry.jsp"
 */

public final class ViewDataEntryAction extends AFSCMEAction {

  public ViewDataEntryAction() {
  }

  public ActionForward perform(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response,
		 		 UserSecurityData usd)
    throws Exception {

        int empAffPk = 0;
        String year = null;
        EmployerData employerData = null;
        ActionErrors errors = new ActionErrors();
		DataEntryDAO dataEntryDao = new DataEntryDAO();
		String tmpForm_employer_name = null;

        try {
            empAffPk = Integer.valueOf((String) request.getParameter("empAffPk")).intValue();
            year = request.getParameter("viewYear");
        }
        catch (Exception nfe) {
            nfe.printStackTrace();
        }

        if (empAffPk == 0) {
            throw new JspException("No employer ID was specified in the request.");
        }
        if (year == null || year.length() == 0) {
            throw new JspException("No year was specified in the request.");
        }

        ViewDataEntryForm vdef = (ViewDataEntryForm)form;
        employerData = s_maintainAffiliates.getEmployerData(empAffPk);

        // Set form fields from EmployerData
        vdef.setAffIdType(employerData.getType());
        vdef.setAffIdState(employerData.getState());
        vdef.setAffIdCouncil(""+employerData.getCouncil());
        vdef.setAffIdLocal(""+employerData.getLocal());
        vdef.setAffIdSubUnit(employerData.getChapter());
        //vdef.setEmployerName(employerData.getEmployer());

        try {
     		tmpForm_employer_name = dataEntryDao.getCurrentFormEmployerName(empAffPk, year);
		}
        catch (Exception nfe) {
            nfe.printStackTrace();
        }
        vdef.setEmployerName(tmpForm_employer_name);

        vdef.setAffIdStatus((employerData.getStatus().trim().equalsIgnoreCase("1")) ? "Yes" : "No");

        vdef.setViewYear(year);
        vdef.setEmpAffPk(empAffPk);

		WageIncForm wageIncForm = dataEntryDao.getWageIncForm(empAffPk, year);
	    vdef.setNumberOfMember(TextUtil.formatInt(wageIncForm.getTot_num_mem()));

		vdef.setAverageWage(TextUtil.formatDouble(wageIncForm.getAverage_wages()));

		vdef.setH_formCompleted(""+wageIncForm.getFormCompleted());
		vdef.setH_correspondence(""+wageIncForm.getCorrespondence());
		vdef.setH_inNegotiations(""+wageIncForm.getInNegotiations());
		vdef.setH_agreementReceived(""+wageIncForm.getAgreementReceived());

		if (wageIncForm.getAgreementDesc() == null)
			vdef.setAgreementDesc("");
		else
			vdef.setAgreementDesc(""+wageIncForm.getAgreementDesc());

		if (wageIncForm.getComments() == null)
			vdef.setComments("");
		else
			vdef.setComments(""+wageIncForm.getComments());

		/*
		if (wageIncForm.getMoreLocals() == null)
			vdef.setMoreLocals("");
		else
			vdef.setMoreLocals(""+wageIncForm.getMoreLocals());
		*/

		if (wageIncForm.getAgreementPk() == 0)
			vdef.setAgreementPk("");
		else {
			vdef.setAgreementPk(""+wageIncForm.getAgreementPk());
		}

		if (wageIncForm.getAgreementName() == null)
			vdef.setAgreementName("");
		else {
			vdef.setAgreementName(wageIncForm.getAgreementName());
		}

		if (wageIncForm.getCorrespondenceDate() == null || wageIncForm.getCorrespondenceDate().equalsIgnoreCase("01/01/1900"))
			vdef.setCorrespondenceDate("");
		else
			vdef.setCorrespondenceDate(""+wageIncForm.getCorrespondenceDate());

		if (wageIncForm.getDurationTo() == null || wageIncForm.getDurationTo().equalsIgnoreCase("01/01/1900"))
			vdef.setDurationTo("");
		else
			vdef.setDurationTo(""+wageIncForm.getDurationTo());

		if (wageIncForm.getDurationFrom() == null || wageIncForm.getDurationFrom().equalsIgnoreCase("01/01/1900"))
			vdef.setDurationFrom("");
		else
			vdef.setDurationFrom(""+wageIncForm.getDurationFrom());


		int wifFk = wageIncForm.getWifPk();
		WageIncData wageIncData = null;

		ArrayList percentWageIncList = new ArrayList();
		ArrayList amountWageIncList = new ArrayList();
		PercentWageIncBean tmpPwib = null;
		AmountWageIncBean tmpAwib = null;

		ArrayList wageIncDataList = dataEntryDao.getWageIncDataList(wifFk);
		vdef.setSecType("A");
		//vdef.setAmountType("cent/hr");
		int totalNumAffected_A = 0;
		int totalNumAffected_B = 0;
		int totalNumAffected = 0;

		for (int i = 0; i < wageIncDataList.size(); i++) {
			wageIncData = (WageIncData) wageIncDataList.get(i);
			if (wageIncData.getWageIncType() == 1) {  // sec A data
				tmpPwib = new PercentWageIncBean(
	    					TextUtil.formatDouble(wageIncData.getWageInc()),
    						wageIncData.getEffectiveDate(),
    						TextUtil.formatInt(wageIncData.getNumAffected()),
    						wageIncData.getPaymentTypeAdj(),
    						TextUtil.formatDouble(wageIncData.getWageIncAdj()),
    						TextUtil.formatInt(wageIncData.getNumAffectedAdj()),
    						TextUtil.formatDoubleThreeDec(wageIncData.getNumTimesInc())
							);
				percentWageIncList.add(tmpPwib);

				totalNumAffected_A = totalNumAffected_A + wageIncData.getNumAffected();
				vdef.setPercentWageIncList(percentWageIncList);
			}
			else {
				tmpAwib = new AmountWageIncBean(
	    					TextUtil.formatDouble(wageIncData.getWageInc()),
    						wageIncData.getEffectiveDate(),
    						TextUtil.formatInt(wageIncData.getNumAffected()),
    						wageIncData.getPaymentTypeAdj(),
    						TextUtil.formatDouble(wageIncData.getWageIncAdj()),
    						TextUtil.formatInt(wageIncData.getNumAffectedAdj()),
    						TextUtil.formatDoubleThreeDec(wageIncData.getNumTimesInc())
							);
				amountWageIncList.add(tmpAwib);

				totalNumAffected_B = totalNumAffected_B + wageIncData.getNumAffected();
				vdef.setAmountWageIncList(amountWageIncList);

				if (wageIncData.getWageIncType() == 3) {
					vdef.setAmountType("dollar/yr");
					vdef.setH_amountType("dollar/yr");
				}
				else if (wageIncData.getWageIncType() == 2) {
					vdef.setAmountType("cent/hr");
				}
			}

		}

		if (amountWageIncList.size() > 0) {
			vdef.setSecType("B");
			totalNumAffected = totalNumAffected_B;
		}
		else {
			vdef.setSecType("A");
			totalNumAffected = totalNumAffected_A;
		}

		if (wageIncForm.getTot_num_mem() != totalNumAffected) {
			// warning
			errors.add("numOfMemNotEqual",new ActionError("error.wif.numOfMemNotEqual"));
			saveErrors(request, errors);

			request.setAttribute(Action.ERROR_KEY, errors);
		}

	    vdef.setPersonName(wageIncForm.getContact_name());
		vdef.setTelephone(wageIncForm.getContact_phone());
		vdef.setEmail(wageIncForm.getContact_email());

		// stat and membership ct
		StatMembership statMembership = null;

		dataEntryDao.setYear(year);
		dataEntryDao.setAffIdType(employerData.getType());
		dataEntryDao.setAffIdState(employerData.getState());
		dataEntryDao.setAffIdCouncil(""+employerData.getCouncil());
		dataEntryDao.setAffIdLocal(""+employerData.getLocal());
		dataEntryDao.setAffIdSubUnit(employerData.getChapter());
		dataEntryDao.setAffIdStatus(employerData.getStatus());

		int affPk = dataEntryDao.getEmployerAffFk(empAffPk);

		//statMembership = dataEntryDao.getStatMembership(year, affPk);
		if (wageIncForm.getAgreementPk() == 0) {
			statMembership = dataEntryDao.getStatMembership(year, affPk, 0);
		}
		else {
			statMembership = dataEntryDao.getStatMembership(year, 0, wageIncForm.getAgreementPk());
		}

		//dataEntryForm.setAffIdState(affIdState);
		vdef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		vdef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

        // needed for jsp
        request.setAttribute("viewDataEntry", vdef);

        return mapping.findForward("viewDataEntry");
  }
}
