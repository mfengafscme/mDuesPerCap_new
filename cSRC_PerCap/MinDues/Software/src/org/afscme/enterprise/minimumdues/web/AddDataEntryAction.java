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
import org.afscme.enterprise.affiliate.EmployerData;
import org.afscme.enterprise.minimumdues.*;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/AddDataEntry"
 *                  name="AddDataEntryForm"
 *                  scope="request"
 *                  validate="false"
 *
 * @struts:action-forward   name="addDataEntry"  path="/Minimumdues/AddDataEntry.jsp"
 * @struts:action-forward   name="saveAndDataEntry"  path="/Minimumdues/DataEntry.jsp"
 */

public final class AddDataEntryAction extends AFSCMEAction {

  public AddDataEntryAction() {
  }

  public ActionForward perform(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response,
		 		 UserSecurityData usd)
    throws Exception {

        int empAffPk = 0;
        String year = null;
        EmployerData data = null;
        String target = null;
        DataEntryDAO dataEntryDao = null;
        Collection agreements = null;

        try {
            empAffPk = Integer.valueOf((String) request.getParameter("empAffPk")).intValue();
            year = request.getParameter("addYear");
        }
        catch (Exception nfe) {
            nfe.printStackTrace();
        }

        if (empAffPk == 0) {
            throw new JspException("No Employer ID was specified in the request.");
        }
        if (year == null || year.length() == 0) {
            throw new JspException("No year was specified in the request.");
        }

		AddDataEntryForm adef = (AddDataEntryForm)form;

		AgreementDAO agreementDao = new AgreementDAO();
		agreements = agreementDao.getAllAgreementsPkNamePair();
		StatMembership statMembership = new StatMembership();

		if (request.getParameter("addNewYearButton") != null && request.getParameter("saveDataButton") == null) {
			target = "addDataEntry";
			data = s_maintainAffiliates.getEmployerData(empAffPk);

			// Set form fields from EmployerData
			adef.setAffIdType(data.getType());
			adef.setAffIdState(data.getState());
			adef.setAffIdCouncil(""+data.getCouncil());
			adef.setAffIdLocal(""+data.getLocal());
			adef.setAffIdSubUnit(data.getChapter());
			adef.setEmployerName(data.getEmployer());
			adef.setAffIdStatus((data.getStatus().trim().equalsIgnoreCase("1")) ? "Yes" : "No");
            adef.setAddYear(year);

			dataEntryDao = new DataEntryDAO();

			dataEntryDao.setYear(year);
			dataEntryDao.setAffIdType(data.getType());
			dataEntryDao.setAffIdState(data.getState());
			dataEntryDao.setAffIdCouncil(""+data.getCouncil());
			dataEntryDao.setAffIdLocal(""+data.getLocal());
			dataEntryDao.setAffIdSubUnit(data.getChapter());
            dataEntryDao.setAffIdStatus(data.getStatus());

			System.out.println("*********** empAffPk = "+empAffPk);
			int affPk = dataEntryDao.getEmployerAffFk(empAffPk);

			statMembership = dataEntryDao.getStatMembership(year, affPk, 0);

			//dataEntryForm.setAffIdState(affIdState);
			adef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
			adef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
			adef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

			// needed for jsp
			request.setAttribute("agreements", agreements);
			request.setAttribute("addDataEntry", adef);
	    }
	    else {
			data = s_maintainAffiliates.getEmployerData(empAffPk);
			String form_employer_name = data.getEmployer();

			ActionErrors errors = new ActionErrors();
			ArrayList percentWageIncList = adef.getPercentWageIncList();
			ArrayList amountWageIncList = adef.getAmountWageIncList();

			HttpSession session = request.getSession();

		    if (request.getParameter("addPencentageIncRowButton") != null || request.getParameter("addAmountIncRowButton") != null) {
				target = "addDataEntry";
				data = s_maintainAffiliates.getEmployerData(empAffPk);
				adef.setAffIdType(data.getType());
				adef.setAffIdState(data.getState());
				adef.setAffIdCouncil(""+data.getCouncil());
				adef.setAffIdLocal(""+data.getLocal());
				adef.setAffIdSubUnit(data.getChapter());
				adef.setEmployerName(data.getEmployer());
				adef.setAffIdStatus((data.getStatus().trim().equalsIgnoreCase("1")) ? "Yes" : "No");

				dataEntryDao = new DataEntryDAO();
				int tmpAgreementPk = 0;

				String tmpAgreementPk_str = request.getParameter("agreementPk");
				if (!((tmpAgreementPk_str == null) || (tmpAgreementPk_str.trim().length() == 0)))
					tmpAgreementPk = Integer.valueOf(request.getParameter("agreementPk")).intValue();
				else
					tmpAgreementPk = 0;

				int affFk = dataEntryDao.getEmployerAffFk(empAffPk);

				if (tmpAgreementPk != 0)
					statMembership = dataEntryDao.getStatMembership(year, 0, tmpAgreementPk);
				else
					statMembership = dataEntryDao.getStatMembership(year, affFk, 0);

				adef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
				adef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
				adef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

				if (request.getParameter("addPencentageIncRowButton") != null) {
				   if (session != null) {
					session.setAttribute("secARowList", percentWageIncList);
					session.setAttribute("secTypeChoose", "A");

					request.setAttribute("agreements", agreements);
					request.setAttribute("addDataEntry", adef);
				  }
				}
				else if (request.getParameter("addAmountIncRowButton") != null) {
				   if (session != null) {
					session.setAttribute("secBRowList", amountWageIncList);
					session.setAttribute("secTypeChoose", "B");

					request.setAttribute("agreements", agreements);
					request.setAttribute("addDataEntry", adef);
				  }
				}
			 }
			 else if ((request.getParameter("selectAgreement") != null)
				&& (request.getParameter("saveNewDataEntry") == null)) {
				target = "addDataEntry";

				data = s_maintainAffiliates.getEmployerData(empAffPk);
				adef.setAffIdType(data.getType());
				adef.setAffIdState(data.getState());
				adef.setAffIdCouncil(""+data.getCouncil());
				adef.setAffIdLocal(""+data.getLocal());
				adef.setAffIdSubUnit(data.getChapter());
				adef.setEmployerName(data.getEmployer());
				adef.setAffIdStatus((data.getStatus().trim().equalsIgnoreCase("1")) ? "Yes" : "No");

				dataEntryDao = new DataEntryDAO();
				int tmpAgreementPk = Integer.valueOf(request.getParameter("agreementPk")).intValue();
				int affFk = dataEntryDao.getEmployerAffFk(empAffPk);

				if (tmpAgreementPk != 0)
					statMembership = dataEntryDao.getStatMembership(year, 0, tmpAgreementPk);
				else
					statMembership = dataEntryDao.getStatMembership(year, affFk, 0);

				adef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
				adef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
				adef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

				// needed for jsp
				request.setAttribute("agreements", agreements);
				request.setAttribute("addDataEntry", adef);
			}
			else if ((request.getParameter("addPencentageIncRowButton") == null)
			    && (request.getParameter("addAmountIncRowButton") == null)
			    && (request.getParameter("saveNewDataEntry") != null)) { // saveButton
			      target = "saveAndDataEntry";

			      String inNegotiations = adef.getInNegotiations();

			      String numberOfMember = adef.getNumberOfMember();
			      String averageWage = adef.getAverageWage();

			      String personName = adef.getPersonName();
			      String telephone = adef.getTelephone();
			      String email = adef.getEmail();

			      String comments = adef.getComments();
			      //String agreementPk = adef.getAgreementPk();
			      String agreementPk = adef.getAgreementName();

			      //System.out.println("****** agreementPk = " + agreementPk);
			      //String agreementName = adef.getAgreementName();

			      String durationFrom = adef.getDurationFrom();
			      String durationTo = adef.getDurationTo();

			      String formCompleted = adef.getFormCompleted();
			      String correspondence = adef.getCorrespondence();

			      String agreementReceived = adef.getAgreementReceived();
			      String agreementDesc = adef.getAgreementDesc();

			      String correspondenceDate = adef.getCorrespondenceDate();

			      String initPercent_a = adef.getInitPercent_a();
			      String initEffective_a = adef.getInitEffective_a();
			      String initNoOfMember_a = adef.getInitNoOfMember_a();
			      String initTypeOfPayment_adj_a = adef.getInitTypeOfPayment_adj_a();
			      String initPercentInc_adj_a = adef.getInitPercentInc_adj_a();
			      String initNoOfMember_adj_a = adef.getInitNoOfMember_adj_a();
			      String initMbrTimesInc_a = adef.getInitMbrTimesInc_a();

			      String initAmount_b = adef.getInitAmount_b();
			      String initEffective_b = adef.getInitEffective_b();
			      String initNoOfMember_b = adef.getInitNoOfMember_b();
			      String initTypeOfPayment_adj_b = adef.getInitTypeOfPayment_adj_b();
			      String initAmountInc_adj_b = adef.getInitAmountInc_adj_b();
			      String initNoOfMember_adj_b = adef.getInitNoOfMember_adj_b();
			      String initMbrTimesInc_b = adef.getInitMbrTimesInc_b();
			      String amountType = adef.getAmountType();
			      String section = adef.getSection();

		    	  if (percentWageIncList.size() != 0) {
					for (int i = 0; i < percentWageIncList.size(); i++) {
						String testPercentEmpty = ((PercentWageIncBean) percentWageIncList.get(i)).getPercent_a();
						if (testPercentEmpty == null || testPercentEmpty.trim().length() == 0)
							percentWageIncList.remove(i);
					}
				  }
				  if (amountWageIncList.size() != 0) {
					for (int j = 0; j < amountWageIncList.size(); j++) {
						String testAmountEmpty = ((AmountWageIncBean) amountWageIncList.get(j)).getAmountInc_b();
						if (testAmountEmpty == null || testAmountEmpty.trim().length() == 0)
							amountWageIncList.remove(j);
					}
				  }

				  percentWageIncList.trimToSize();
				  amountWageIncList.trimToSize();

			      /*
			       * Having received and validated the data submitted
			       * from the View, we now update the model
			       */
			      dataEntryDao = new DataEntryDAO();

			      dataEntryDao.setPercentWageIncList(percentWageIncList);
			      dataEntryDao.setAmountWageIncList(amountWageIncList);

			      dataEntryDao.setYear(year);
			      dataEntryDao.setEmpAffPk(empAffPk);
				  dataEntryDao.setForm_employer_name(form_employer_name);

			      dataEntryDao.setNumberOfMember(numberOfMember);
			      dataEntryDao.setAverageWage(averageWage);

			      dataEntryDao.setPersonName(personName);
			      dataEntryDao.setTelephone(telephone);
			      dataEntryDao.setEmail(email);

			      dataEntryDao.setComments(comments);
			      if (agreementPk != null)
			      		dataEntryDao.setAgreementPk(Integer.parseInt(agreementPk));

			      //dataEntryDao.setAgreementName(agreementName);
			      dataEntryDao.setDurationFrom(durationFrom);
			      dataEntryDao.setDurationTo(durationTo);
			      dataEntryDao.setFormCompleted(formCompleted);
			      dataEntryDao.setAgreementReceived(agreementReceived);
			      dataEntryDao.setAgreementDesc(agreementDesc);
			      dataEntryDao.setCorrespondence(correspondence);
			      dataEntryDao.setCorrespondenceDate(correspondenceDate);
			      dataEntryDao.setInNegotiations(inNegotiations);

			      dataEntryDao.setInitPercent_a(initPercent_a);
			      dataEntryDao.setInitEffective_a(initEffective_a);
			      dataEntryDao.setInitNoOfMember_a(initNoOfMember_a);
			      dataEntryDao.setInitTypeOfPayment_adj_a(initTypeOfPayment_adj_a);
			      dataEntryDao.setInitPercentInc_adj_a(initPercentInc_adj_a);
			      dataEntryDao.setInitNoOfMember_adj_a(initNoOfMember_adj_a);
			      dataEntryDao.setInitMbrTimesInc_a(initMbrTimesInc_a);

			      dataEntryDao.setInitAmount_b(initAmount_b);
			      dataEntryDao.setInitEffective_b(initEffective_b);
			      dataEntryDao.setInitNoOfMember_b(initNoOfMember_b);
			      dataEntryDao.setInitTypeOfPayment_adj_b(initTypeOfPayment_adj_b);
			      dataEntryDao.setInitAmountInc_adj_b(initAmountInc_adj_b);
			      dataEntryDao.setInitNoOfMember_adj_b(initNoOfMember_adj_b);
			      dataEntryDao.setInitMbrTimesInc_b(initMbrTimesInc_b);
			      dataEntryDao.setAmountType(amountType);
				  dataEntryDao.setSection(section);

			      dataEntryDao.saveWIFormDataToDB();

			      (adef.percentWageIncList).clear();
			      (adef.amountWageIncList).clear();

			      percentWageIncList.clear();
			      amountWageIncList.clear();

			      if (session != null) {
				    session.removeAttribute("secARowList");
				    session.removeAttribute("secBRowList");
			      }

			      session.setAttribute("secTypeChoose", "");
			    }

		}

        return mapping.findForward(target);
  }
}
