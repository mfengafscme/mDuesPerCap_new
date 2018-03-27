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
 * @struts:action   path="/EditDataEntry"
 *                  name="EditDataEntryForm"
 *                  scope="request"
 *                  validate="false"
 *
 *
 * @struts:action-forward   name="editDataEntry"  path="/Minimumdues/EditDataEntry.jsp"
 * @struts:action-forward   name="deleteDataEntry"  path="/viewBasicAffiliateCriteria.action?new"
 * @struts:action-forward   name="updateAndView"  path="/Minimumdues/ViewDataEntry.jsp"
 * @struts:action-forward   name="backToView"  path="/Minimumdues/ViewDataEntry.jsp"
 */

public final class EditDataEntryAction extends AFSCMEAction {

  public EditDataEntryAction() {
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
	String target = null;
	EditDataEntryForm edef = (EditDataEntryForm)form;
	ArrayList percentWageIncList = null;
	ArrayList amountWageIncList = null;
	DataEntryDAO dataEntryDao = null;
	WageIncForm wageIncForm = null;
	String tmpForm_employer_name = null;
	Collection agreements = null;
	StatMembership statMembership = null;


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

	AgreementDAO agreementDao = new AgreementDAO();
	agreements = agreementDao.getAllAgreementsPkNamePair();

	if (request.getParameter("deleteButton") != null)
	{
		target = "deleteDataEntry";

  		dataEntryDao = new DataEntryDAO();

		//dataEntryDao.setEmpAffPk(empAffPk);
		//dataEntryDao.setYear(year);

		dataEntryDao.deleteFormDataRecord(empAffPk, year);
	}
	else if (request.getParameter("cancelEditDataButton") != null)
	{
		target = "backToView";
		dataEntryDao = new DataEntryDAO();

		// go back to view page for the same employer same year
		ViewDataEntryForm vdef = new ViewDataEntryForm();
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

		wageIncForm = null;
		wageIncForm = dataEntryDao.getWageIncForm(empAffPk, year);
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
		WageIncData tmpWid = null;

		percentWageIncList = new ArrayList();
		amountWageIncList = new ArrayList();
		PercentWageIncBean tmpPwib = null;
		AmountWageIncBean tmpAwib = null;

		ArrayList wageIncDataList = dataEntryDao.getWageIncDataList(wifFk);
		vdef.setSecType("A");
		vdef.setAmountType("cent/hr");

		for (int i = 0; i < wageIncDataList.size(); i++) {
			tmpWid = (WageIncData) wageIncDataList.get(i);
			if (tmpWid.getWageIncType() == 1) {  // sec A data
				tmpPwib = new PercentWageIncBean(
	    					TextUtil.formatDouble(tmpWid.getWageInc()),
    						tmpWid.getEffectiveDate(),
    						TextUtil.formatInt(tmpWid.getNumAffected()),
    						tmpWid.getPaymentTypeAdj(),
    						TextUtil.formatDouble(tmpWid.getWageIncAdj()),
    						TextUtil.formatInt(tmpWid.getNumAffectedAdj()),
    						TextUtil.formatDoubleThreeDec(tmpWid.getNumTimesInc())
							);
				percentWageIncList.add(tmpPwib);
				vdef.setPercentWageIncList(percentWageIncList);
			}
			else {
				tmpAwib = new AmountWageIncBean(
	    					TextUtil.formatDouble(tmpWid.getWageInc()),
    						tmpWid.getEffectiveDate(),
    						TextUtil.formatInt(tmpWid.getNumAffected()),
    						tmpWid.getPaymentTypeAdj(),
    						TextUtil.formatDouble(tmpWid.getWageIncAdj()),
    						TextUtil.formatInt(tmpWid.getNumAffectedAdj()),
    						TextUtil.formatDoubleThreeDec(tmpWid.getNumTimesInc())
							);
				amountWageIncList.add(tmpAwib);
				vdef.setAmountWageIncList(amountWageIncList);

				if (tmpWid.getWageIncType() == 3) {
					vdef.setAmountType("dollar/yr");
				}
			}

		}

		if (amountWageIncList.size() != 0) {
			vdef.setSecType("B");
		}

	    vdef.setPersonName(wageIncForm.getContact_name());
		vdef.setTelephone(wageIncForm.getContact_phone());
		vdef.setEmail(wageIncForm.getContact_email());

		// stat and membership ct
		dataEntryDao.setYear(year);
		dataEntryDao.setAffIdType(employerData.getType());
		dataEntryDao.setAffIdState(employerData.getState());
		dataEntryDao.setAffIdCouncil(""+employerData.getCouncil());
		dataEntryDao.setAffIdLocal(""+employerData.getLocal());
		dataEntryDao.setAffIdSubUnit(employerData.getChapter());
		dataEntryDao.setAffIdStatus(employerData.getStatus());

		int affPk = dataEntryDao.getEmployerAffFk(empAffPk);

		// statMembership = dataEntryDao.getStatMembership(year, affPk);
		if (wageIncForm.getAgreementPk() == 0) {
			statMembership = dataEntryDao.getStatMembership(year, affPk, 0);
		}
		else {
			statMembership = dataEntryDao.getStatMembership(year, 0, wageIncForm.getAgreementPk());
		}

		//dataEntryForm.setAffIdState(affIdState);
		vdef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		vdef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		vdef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

		vdef.setH_formCompleted(""+wageIncForm.getFormCompleted());
		vdef.setH_correspondence(""+wageIncForm.getCorrespondence());
		vdef.setH_inNegotiations(""+wageIncForm.getInNegotiations());
		vdef.setH_agreementReceived(""+wageIncForm.getAgreementReceived());

		if (wageIncForm.getAgreementDesc() == null)
			vdef.setAgreementReceived("");
		else
			vdef.setAgreementReceived(""+wageIncForm.getAgreementDesc());

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

		if (request.getSession() != null) {
			    request.getSession().removeAttribute("secARowList");
			    request.getSession().removeAttribute("secBRowList");
		}

		request.getSession().setAttribute("secTypeChoose", "");
		request.setAttribute("viewDataEntry", vdef);
	}
	else if (request.getParameter("saveEditDataButton") != null) {
		target = "updateAndView";

		// use DAO to update the data
		ActionErrors errors = new ActionErrors();
		percentWageIncList = edef.getPercentWageIncList();
		amountWageIncList = edef.getAmountWageIncList();


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

		HttpSession session = request.getSession();

		String numberOfMember = edef.getNumberOfMember();
		String averageWage = edef.getAverageWage();

		String personName = edef.getPersonName();
		String telephone = edef.getTelephone();
		String email = edef.getEmail();

		String inNegotiations = edef.getInNegotiations();
		String comments = edef.getComments();

		//String moreLocals = edef.getMoreLocals();
		//String agreementPk1 = edef.getAgreementPk();
		//System.out.println("****** agreementPk1 = " + agreementPk1);
		String agreementPk = edef.getAgreementName();
		//System.out.println("****** agreementPk = " + agreementPk);

		String durationFrom = edef.getDurationFrom();
		String durationTo = edef.getDurationTo();
		String formCompleted = edef.getFormCompleted();
		String agreementReceived = edef.getAgreementReceived();
		String agreementDesc = edef.getAgreementDesc();
		String correspondence = edef.getCorrespondence();
		String correspondenceDate = edef.getCorrespondenceDate();

		String amountType = edef.getAmountType();

		/*
		* Having received and validated the data submitted
		* from the View, we now update the model
		*/
		dataEntryDao = new DataEntryDAO();
		employerData = s_maintainAffiliates.getEmployerData(empAffPk);

		dataEntryDao.setPercentWageIncList(percentWageIncList);
		dataEntryDao.setAmountWageIncList(amountWageIncList);


		dataEntryDao.setYear(year);
		dataEntryDao.setEmpAffPk(empAffPk);
		dataEntryDao.setForm_employer_name(employerData.getEmployer());

		dataEntryDao.setNumberOfMember(numberOfMember);
		dataEntryDao.setAverageWage(averageWage);

		dataEntryDao.setPersonName(personName);
		dataEntryDao.setTelephone(telephone);
		dataEntryDao.setEmail(email);

		dataEntryDao.setInNegotiations(inNegotiations);
		dataEntryDao.setComments(comments);
		//dataEntryDao.setMoreLocals(moreLocals);
		dataEntryDao.setAgreementPk(Integer.parseInt(agreementPk));
		//dataEntryDao.setAgreementPk(agreementPk);
		dataEntryDao.setDurationFrom(durationFrom);
		dataEntryDao.setDurationTo(durationTo);
		dataEntryDao.setFormCompleted(formCompleted);
		dataEntryDao.setAgreementReceived(agreementReceived);
		dataEntryDao.setAgreementDesc(agreementDesc);

		dataEntryDao.setCorrespondence(correspondence);
		dataEntryDao.setCorrespondenceDate(correspondenceDate);

		dataEntryDao.setAmountType(amountType);

		dataEntryDao.updateIncreaseInfo();

		(edef.percentWageIncList).clear();
		(edef.amountWageIncList).clear();

		percentWageIncList.clear();
		amountWageIncList.clear();

		if (session != null) {
			session.removeAttribute("secARowList");
			session.removeAttribute("secBRowList");
		}

		session.setAttribute("secTypeChoose", "");

		// go back to view page for the same employer same year
		ViewDataEntryForm vdef = new ViewDataEntryForm();

		// Set form fields from EmployerData
		vdef.setAffIdType(employerData.getType());
		vdef.setAffIdState(employerData.getState());
		vdef.setAffIdCouncil(""+employerData.getCouncil());
		vdef.setAffIdLocal(""+employerData.getLocal());
		vdef.setAffIdSubUnit(employerData.getChapter());

		//???
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

		wageIncForm = null;
		wageIncForm = dataEntryDao.getWageIncForm(empAffPk, year);
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
		else
			vdef.setAgreementName(wageIncForm.getAgreementName());

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
		WageIncData tmpWid = null;

		percentWageIncList = new ArrayList();
		amountWageIncList = new ArrayList();
		PercentWageIncBean tmpPwib = null;
		AmountWageIncBean tmpAwib = null;

		ArrayList wageIncDataList = dataEntryDao.getWageIncDataList(wifFk);
		vdef.setSecType("A");
		vdef.setAmountType("cent/hr");

		for (int i = 0; i < wageIncDataList.size(); i++) {
			tmpWid = (WageIncData) wageIncDataList.get(i);
			if (tmpWid.getWageIncType() == 1) {  // sec A data
				tmpPwib = new PercentWageIncBean(
                                        TextUtil.formatDouble(tmpWid.getWageInc()),
                                        tmpWid.getEffectiveDate(),
                                        TextUtil.formatInt(tmpWid.getNumAffected()),
                                        tmpWid.getPaymentTypeAdj(),
                                        TextUtil.formatDouble(tmpWid.getWageIncAdj()),
                                        TextUtil.formatInt(tmpWid.getNumAffectedAdj()),
                                        TextUtil.formatDoubleThreeDec(tmpWid.getNumTimesInc())
					);
				percentWageIncList.add(tmpPwib);
				vdef.setPercentWageIncList(percentWageIncList);
			}
			else {
				tmpAwib = new AmountWageIncBean(
                                        TextUtil.formatDouble(tmpWid.getWageInc()),
                                        tmpWid.getEffectiveDate(),
                                        TextUtil.formatInt(tmpWid.getNumAffected()),
                                        tmpWid.getPaymentTypeAdj(),
                                        TextUtil.formatDouble(tmpWid.getWageIncAdj()),
                                        TextUtil.formatInt(tmpWid.getNumAffectedAdj()),
                                        TextUtil.formatDoubleThreeDec(tmpWid.getNumTimesInc())
					);
				amountWageIncList.add(tmpAwib);
				vdef.setAmountWageIncList(amountWageIncList);

				if (tmpWid.getWageIncType() == 3) {
					vdef.setAmountType("dollar/yr");
				}
			}

		}

		if (amountWageIncList.size() != 0) {
			vdef.setSecType("B");
		}

		//DataEntryContact dataEntryContact = dataEntryDao.getDataEntryContactInfo(wifFk);
		//vdef.setPersonName(dataEntryContact.getName());
		//vdef.setTelephone(dataEntryContact.getPhone());
		//vdef.setEmail(dataEntryContact.getEmail());

	    vdef.setPersonName(wageIncForm.getContact_name());
		vdef.setTelephone(wageIncForm.getContact_phone());
		vdef.setEmail(wageIncForm.getContact_email());

		// stat and membership ct
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
		vdef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		vdef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

		if (request.getSession() != null) {
			request.getSession().removeAttribute("secARowList");
			request.getSession().removeAttribute("secBRowList");
		}

      	request.getSession().setAttribute("secTypeChoose", "");

		// needed for jsp
		request.setAttribute("viewDataEntry", vdef);
		//request.setAttribute("empActive", empActive);
	}
	else if (request.getParameter("addPencentageIncRowButton") != null || request.getParameter("addAmountIncRowButton") != null) {
		target = "editDataEntry";
		percentWageIncList = new ArrayList();
		amountWageIncList = new ArrayList();
		dataEntryDao = new DataEntryDAO();

		if (request.getParameter("addPencentageIncRowButton") != null) {
                        percentWageIncList = edef.getPercentWageIncList();
			if (request.getSession() != null) {
				request.getSession().setAttribute("secARowList", percentWageIncList);
				request.getSession().setAttribute("secTypeChoose", "A");
			}
                        edef.setSecType("A");
		}
		else if (request.getParameter("addAmountIncRowButton") != null) {
                        amountWageIncList = edef.getAmountWageIncList();
                        if (request.getSession() != null) {
				request.getSession().setAttribute("secBRowList", amountWageIncList);
				request.getSession().setAttribute("secTypeChoose", "B");
			}
                        edef.setSecType("B");
		}

		employerData = s_maintainAffiliates.getEmployerData(empAffPk);

		// Set form fields from EmployerData
		edef.setAffIdType(employerData.getType());
		edef.setAffIdState(employerData.getState());
		edef.setAffIdCouncil(""+employerData.getCouncil());
		edef.setAffIdLocal(""+employerData.getLocal());
		edef.setAffIdSubUnit(employerData.getChapter());

        try {
     		tmpForm_employer_name = dataEntryDao.getCurrentFormEmployerName(empAffPk, year);
			//System.out.println(" ********* tmpForm_employer_name = " + tmpForm_employer_name);
		}
        catch (Exception nfe) {
            nfe.printStackTrace();
        }
        edef.setEmployerName(tmpForm_employer_name);


		int affFk = dataEntryDao.getEmployerAffFk(empAffPk);

		int tmpAgreementPk = Integer.valueOf(request.getParameter("agreementPk")).intValue();

		if (tmpAgreementPk != 0)
			statMembership = dataEntryDao.getStatMembership(year, 0, tmpAgreementPk);
		else
			statMembership = dataEntryDao.getStatMembership(year, affFk, 0);

		edef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		edef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		edef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

		employerData = s_maintainAffiliates.getEmployerData(empAffPk);

		request.setAttribute("agreements", agreements);
		request.setAttribute("editDataEntry", edef);
	}
	else if (request.getParameter("editButton") != null) {
		target = "editDataEntry";
		dataEntryDao = new DataEntryDAO();

		employerData = s_maintainAffiliates.getEmployerData(empAffPk);

		// Set form fields from EmployerData
		edef.setAffIdType(employerData.getType());
		edef.setAffIdState(employerData.getState());
		edef.setAffIdCouncil(""+employerData.getCouncil());
		edef.setAffIdLocal(""+employerData.getLocal());
		edef.setAffIdSubUnit(employerData.getChapter());

		//???
		//edef.setEmployerName(employerData.getEmployer());

        try {
     		tmpForm_employer_name = dataEntryDao.getCurrentFormEmployerName(empAffPk, year);
		}
        catch (Exception nfe) {
            nfe.printStackTrace();
        }
        edef.setEmployerName(tmpForm_employer_name);


		edef.setAffIdStatus((employerData.getStatus().trim().equalsIgnoreCase("1")) ? "Yes" : "No");

        edef.setViewYear(year);
		edef.setEmpAffPk(empAffPk);

		dataEntryDao = new DataEntryDAO();
		wageIncForm = null;
		wageIncForm = dataEntryDao.getWageIncForm(empAffPk, year);

		edef.setH_formCompleted(""+wageIncForm.getFormCompleted());
		edef.setH_correspondence(""+wageIncForm.getCorrespondence());
		edef.setH_inNegotiations(""+wageIncForm.getInNegotiations());
		edef.setH_agreementReceived(""+wageIncForm.getAgreementReceived());

		if (wageIncForm.getAgreementDesc() == null)
			edef.setAgreementDesc("");
		else
			edef.setAgreementDesc(""+wageIncForm.getAgreementDesc().trim());

		if (wageIncForm.getComments() == null)
			edef.setComments("");
		else
			edef.setComments(""+wageIncForm.getComments());

		/*
		if (wageIncForm.getMoreLocals() == null)
			edef.setMoreLocals("");
		else
			edef.setMoreLocals(""+wageIncForm.getMoreLocals());
		*/

		if (wageIncForm.getAgreementPk() == 0)
			edef.setAgreementPk("");
		else {
			edef.setAgreementPk(""+wageIncForm.getAgreementPk());
		}

		if (wageIncForm.getAgreementName() == null)
			edef.setAgreementName("");
		else {
			edef.setAgreementName(""+wageIncForm.getAgreementPk());
		}

		if (wageIncForm.getCorrespondenceDate() == null || wageIncForm.getCorrespondenceDate().equalsIgnoreCase("01/01/1900"))
			edef.setCorrespondenceDate("");
		else
			edef.setCorrespondenceDate(""+wageIncForm.getCorrespondenceDate());

		if (wageIncForm.getDurationTo() == null || wageIncForm.getDurationTo().equalsIgnoreCase("01/01/1900"))
			edef.setDurationTo("");
		else
			edef.setDurationTo(""+wageIncForm.getDurationTo());

		if (wageIncForm.getDurationFrom() == null || wageIncForm.getDurationFrom().equalsIgnoreCase("01/01/1900"))
			edef.setDurationFrom("");
		else
			edef.setDurationFrom(""+wageIncForm.getDurationFrom());

		dataEntryDao = new DataEntryDAO();

		wageIncForm = wageIncForm = dataEntryDao.getWageIncForm(empAffPk, year);
		edef.setNumberOfMember(TextUtil.formatToNumber(TextUtil.formatInt(wageIncForm.getTot_num_mem())));

		/*
		edef.setAverageWage(""+wageIncForm.getAverage_wages());

	    String tmpAverageWageConv = ""+wageIncForm.getAverage_wages();
	    if ((tmpAverageWageConv.indexOf(".")) == (tmpAverageWageConv.length() - 2)) {
				  tmpAverageWageConv = tmpAverageWageConv + "0";
	    }

	    if (tmpAverageWageConv.trim().equalsIgnoreCase("0.00")) {
			edef.setAverageWage("");
		}
		else {
			edef.setAverageWage(tmpAverageWageConv);
		}
		*/

		edef.setAverageWage(TextUtil.formatToNumber(TextUtil.formatDouble(wageIncForm.getAverage_wages())));

		int wifFk = wageIncForm.getWifPk();
		WageIncData tmpWid = null;

		percentWageIncList = new ArrayList();
		amountWageIncList = new ArrayList();
		PercentWageIncBean tmpPwib = null;
		AmountWageIncBean tmpAwib = null;

		ArrayList wageIncDataList = dataEntryDao.getWageIncDataList(wifFk);

		for (int i = 0; i < wageIncDataList.size(); i++) {
			tmpWid = (WageIncData) wageIncDataList.get(i);
			if (tmpWid.getWageIncType() == 1) {  // sec A data
				tmpPwib = new PercentWageIncBean(
	    					TextUtil.formatDouble(tmpWid.getWageInc()),
    						tmpWid.getEffectiveDate(),
    						TextUtil.formatInt(tmpWid.getNumAffected()),
    						tmpWid.getPaymentTypeAdj(),
    						TextUtil.formatDouble(tmpWid.getWageIncAdj()),
    						TextUtil.formatInt(tmpWid.getNumAffectedAdj()),
    						TextUtil.formatDoubleThreeDec(tmpWid.getNumTimesInc())
							);
				percentWageIncList.add(tmpPwib);
				edef.setPercentWageIncList(percentWageIncList);
			}
			else {
				tmpAwib = new AmountWageIncBean(
	    					TextUtil.formatDouble(tmpWid.getWageInc()),
    						tmpWid.getEffectiveDate(),
    						TextUtil.formatInt(tmpWid.getNumAffected()),
    						tmpWid.getPaymentTypeAdj(),
    						TextUtil.formatDouble(tmpWid.getWageIncAdj()),
    						TextUtil.formatInt(tmpWid.getNumAffectedAdj()),
    						TextUtil.formatDoubleThreeDec(tmpWid.getNumTimesInc())
							);
				amountWageIncList.add(tmpAwib);
				edef.setAmountWageIncList(amountWageIncList);

				if (tmpWid.getWageIncType() == 2) {
					edef.setAmountType("cent/hr");
				}
                else if (tmpWid.getWageIncType() == 3) {
					edef.setAmountType("dollar/yr");
				}
			}

		}

		if (amountWageIncList.size() > 0) {
			edef.setSecType("B");
			request.getSession().setAttribute("secTypeChoose", "B");
		}
		else {
			edef.setSecType("A");
			request.getSession().setAttribute("secTypeChoose", "A");
		}

		//DataEntryContact dataEntryContact = dataEntryDao.getDataEntryContactInfo(wifFk);
		//edef.setPersonName(dataEntryContact.getName());
		//edef.setTelephone(dataEntryContact.getPhone());
		//edef.setEmail(dataEntryContact.getEmail());

	    edef.setPersonName(wageIncForm.getContact_name());
		edef.setTelephone(wageIncForm.getContact_phone());
		edef.setEmail(wageIncForm.getContact_email());

		// stat and membership ct
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
		edef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		edef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		edef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));


		if (request.getSession() != null) {
			request.getSession().setAttribute("secBRowList", amountWageIncList);
			request.getSession().setAttribute("secARowList", percentWageIncList);
			//request.getSession().setAttribute("secTypeChoose", "B");
		}

		// needed for jsp
		request.setAttribute("agreements", agreements);
		request.setAttribute("editDataEntry", edef);
	}
	else if (request.getParameter("selectAgreement") != null) {
		target = "editDataEntry";

		dataEntryDao = new DataEntryDAO();
		int affFk = dataEntryDao.getEmployerAffFk(empAffPk);

		int tmpAgreementPk = Integer.valueOf(request.getParameter("agreementPk")).intValue();

		if (tmpAgreementPk != 0)
			statMembership = dataEntryDao.getStatMembership(year, 0, tmpAgreementPk);
		else
			statMembership = dataEntryDao.getStatMembership(year, affFk, 0);

		edef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		edef.setH_statAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
		edef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

		employerData = s_maintainAffiliates.getEmployerData(empAffPk);

		// Set form fields from EmployerData
		edef.setAffIdType(employerData.getType());
		edef.setAffIdState(employerData.getState());
		edef.setAffIdCouncil(""+employerData.getCouncil());
		edef.setAffIdLocal(""+employerData.getLocal());
		edef.setAffIdSubUnit(employerData.getChapter());

        try {
     		tmpForm_employer_name = dataEntryDao.getCurrentFormEmployerName(empAffPk, year);
		}
        catch (Exception nfe) {
            nfe.printStackTrace();
        }
        edef.setEmployerName(tmpForm_employer_name);

		// needed for jsp
		request.setAttribute("agreements", agreements);
		request.setAttribute("editDataEntry", edef);
	}

	return mapping.findForward(target);
  }
}
