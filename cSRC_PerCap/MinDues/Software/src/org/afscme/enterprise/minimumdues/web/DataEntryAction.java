package org.afscme.enterprise.minimumdues.web;

import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

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
import org.afscme.enterprise.minimumdues.*;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:action   path="/DataEntry"
 *                  name="DataEntryForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Minimumdues/DataEntry.jsp"
 *
 * @struts:action-forward   name="nothing"  path="/Minimumdues/DataEntry.jsp"
 * @struts:action-forward   name="errorStateCouncilLocal"  path="/Minimumdues/DataEntry.jsp?errorStateCouncilLocal"
 * @struts:action-forward   name="inActive"  path="/Minimumdues/DataEntry.jsp?inActive"
 * @struts:action-forward   name="addEmployer"  path="/Minimumdues/DataEntry.jsp?addEmployer"
 * @struts:action-forward   name="addData"  path="/Minimumdues/AddDataEntry.jsp"
 * @struts:action-forward   name="viewData"  path="/Minimumdues/ViewDataEntry.jsp"
 *
 */
public final class DataEntryAction extends AFSCMEAction {

  public DataEntryAction() {
  }

  public ActionForward perform(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			UserSecurityData usd)
    throws Exception {

		String target = new String("nothing");
		int empAffPk = 0;
		String empActive = "yes";
		Collection agreements = null;

		AgreementDAO agreementDao = new AgreementDAO();
		agreements = agreementDao.getAllAgreementsPkNamePair();

    	// for future use
    	// MessageResources messages = getResources(request);

    	DataEntryForm dataEntryForm = (DataEntryForm)form;

	    ActionErrors errors = new ActionErrors();
	    HttpSession session = request.getSession();

	    String year = dataEntryForm.getYear();

	    String affIdType = dataEntryForm.getAffIdType();
	    String affIdState = dataEntryForm.getAffIdState();
	    String affIdCouncil = dataEntryForm.getAffIdCouncil();
	    String affIdLocal = dataEntryForm.getAffIdLocal();
	    String affIdSubUnit = dataEntryForm.getAffIdSubUnit();
	    String employerName = dataEntryForm.getEmployerName();
	    String affIdStatus = dataEntryForm.getAffIdStatus();

	    if (affIdType != null)
	    	affIdType = affIdType.trim();
	    if (affIdState != null)
	    	affIdState = affIdState.trim();
	    if (affIdCouncil != null)
	    	affIdCouncil = affIdCouncil.trim();
	    if (affIdLocal != null)
	    	affIdLocal = affIdLocal.trim();
	    if (affIdSubUnit != null)
	    	affIdSubUnit = affIdSubUnit.trim();
	    if (employerName != null)
	    	employerName = employerName.trim();
	    if (affIdStatus != null)
	    	affIdStatus = affIdStatus.trim();

		/*
		* Having received and validated the data submitted
       	* from the View, we now update the model
       	*/
      	DataEntryDAO dataEntryDao = new DataEntryDAO();

  	    dataEntryDao.setYear(year);
  	    dataEntryDao.setAffIdType(affIdType);
  	    dataEntryDao.setAffIdState(affIdState);
  	    dataEntryDao.setAffIdCouncil(affIdCouncil);
  	    dataEntryDao.setAffIdLocal(affIdLocal);
  	    dataEntryDao.setAffIdSubUnit(affIdSubUnit);

  	    dataEntryDao.setEmployerName(employerName);

  	    dataEntryDao.setAffIdStatus(affIdStatus);

		if (request.getParameter("enterDataButton") != null) { // enter data button clicked
			int employerYrDataExist = dataEntryDao.checkEmployerYrDataExist();

			if (employerYrDataExist == DataEntryDAO.EMPLOYER_NOT_EXIST) {
				// go to add employer
				target = "addEmployer";
				errors.add("employernoexist",new ActionError("error.employer.noexist"));
				saveErrors(request, errors);

				request.setAttribute(Action.ERROR_KEY, errors);

				request.setAttribute("empActive", empActive);
				request.setAttribute("addEmployer", "");
			}
			else {  // employer exists
				empAffPk = dataEntryDao.checkEmployerExist();
				empActive = dataEntryDao.checkEmployerActive(empAffPk);

				if (employerYrDataExist == DataEntryDAO.EMPLOYER_SAME_YR_EXIST) {  // same yr exists, so display view page
					target = "viewData";

					ViewDataEntryForm vdef = new ViewDataEntryForm();

					// Set form fields from EmployerData
					vdef.setAffIdType(affIdType);
					vdef.setAffIdState(affIdState);
					vdef.setAffIdCouncil(affIdCouncil);
					vdef.setAffIdLocal(affIdLocal);
					vdef.setAffIdSubUnit(affIdSubUnit);
					vdef.setEmployerName(employerName);

					if (empActive.trim().equalsIgnoreCase("Yes"))
						vdef.setAffIdStatus("Yes");
					else
						vdef.setAffIdStatus("No");

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

					if (amountWageIncList.size() != 0) {
						vdef.setSecType("B");
					}

					vdef.setPersonName(wageIncForm.getContact_name());
					vdef.setTelephone(wageIncForm.getContact_phone());
					vdef.setEmail(wageIncForm.getContact_email());

					// stat and membership ct
					StatMembership statMembership = null;

					int affPk = dataEntryDao.getEmployerAffFk(empAffPk);

					if (wageIncForm.getAgreementPk() == 0) {
						statMembership = dataEntryDao.getStatMembership(year, affPk, 0);
					}
					else {
						statMembership = dataEntryDao.getStatMembership(year, 0, wageIncForm.getAgreementPk());
					}

					//dataEntryForm.setAffIdState(affIdState);
					vdef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
					vdef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

					errors.add("employersameyrexist",new ActionError("error.employer.sameyr.exist"));
					saveErrors(request, errors);

					// needed for jsp
					request.setAttribute("showSearchResult", "false");
					request.setAttribute("empAffPk", ""+empAffPk);
					request.setAttribute("empActive", empActive);
					request.setAttribute("viewDataEntry", vdef);
				}
				else {  // employer exists but same yr does not exist, so add new data entry form
					if (empActive.trim().equalsIgnoreCase("Yes")) {

						// add DataEntryForm
						target = "addData";

						AddDataEntryForm adef = new AddDataEntryForm();

						// Set form fields from EmployerData
						adef.setAffIdType(affIdType);
						adef.setAffIdState(affIdState);
						adef.setAffIdCouncil(affIdCouncil);
						adef.setAffIdLocal(affIdLocal);
						adef.setAffIdSubUnit(affIdSubUnit);
						adef.setEmployerName(employerName);

						adef.setAffIdStatus("Yes");

						adef.setAddYear(year);
						adef.setEmpAffPk(empAffPk);

						StatMembership statMembership = null;

						int affPk = dataEntryDao.getEmployerAffFk(empAffPk);
						statMembership = dataEntryDao.getStatMembership(year, affPk, 0);

						adef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
						adef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

						// needed for jsp
						request.setAttribute("agreements", agreements);
						request.setAttribute("showSearchResult", "false");
						request.setAttribute("empAffPk", ""+empAffPk);
						request.setAttribute("empActive", empActive);
						request.setAttribute("addDataEntry", adef);
					}
					else {
						// go back to the same data entry screen to waring
						// cannot add new data entry form for deactive employer
						errors.add("employerInactive",new ActionError("error.employer.inactive"));
						saveErrors(request, errors);

						request.setAttribute(Action.ERROR_KEY, errors);
						request.setAttribute("empAffPk", ""+empAffPk);
						request.setAttribute("empActive", empActive);
						request.setAttribute("inActive", "");

						target = "inActive";
					}
				}
			}
	  	}
	  	else if (request.getParameter("addEmployerButton") != null) { // add employer button clicked
			empAffPk = dataEntryDao.checkEmployerExist();

			if (empAffPk != 0)  {
				errors.add("duplicateEmployerAddError",new ActionError("error.duplicateEmpAdd"));
				saveErrors(request, errors);

				return (new ActionForward(mapping.getInput()));
			}

			int aff_fk = dataEntryDao.checkAddOrModifyEmployerPossibility();

			if (aff_fk != 0) {
				dataEntryDao.addEmployer(aff_fk);
				empAffPk = dataEntryDao.checkEmployerExist();
			}
			else {
				errors.add("stateCouncilLocalError",new ActionError("error.stateCouncilLocalError"));
				saveErrors(request, errors);

				target = "errorStateCouncilLocal";
				request.setAttribute("errorStateCouncilLocal", "");

				return (new ActionForward(mapping.getInput()));
			}

			target = "addData";
			AddDataEntryForm adef = new AddDataEntryForm();

			// Set form fields from EmployerData
			adef.setAffIdType(affIdType);
			adef.setAffIdState(affIdState);
			adef.setAffIdCouncil(affIdCouncil);
			adef.setAffIdLocal(affIdLocal);
			adef.setAffIdSubUnit(affIdSubUnit);
			adef.setEmployerName(employerName);
			adef.setAffIdStatus("Yes");

			adef.setAddYear(year);
			adef.setEmpAffPk(empAffPk);

			StatMembership statMembership = null;

			int affPk = dataEntryDao.getEmployerAffFk(empAffPk);
			statMembership = dataEntryDao.getStatMembership(year, affPk, 0);

			//dataEntryForm.setAffIdState(affIdState);
			adef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
			adef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

			// needed for jsp
			request.setAttribute("agreements", agreements);
			request.setAttribute("showSearchResult", "false");
			request.setAttribute("empAffPk", ""+empAffPk);
			request.setAttribute("addDataEntry", adef);
			request.setAttribute("empActive", "Yes");
		}
		else if (request.getParameter("activateEmployerBtn") != null) { // add employer button clicked
			empAffPk = dataEntryDao.checkEmployerExist();
        	dataEntryDao.changeEmployerActiveStatus(empAffPk);

			// add DataEntryForm
			target = "addData";
			AddDataEntryForm adef = new AddDataEntryForm();

			// Set form fields from EmployerData
			adef.setAffIdType(affIdType);
			adef.setAffIdState(affIdState);
			adef.setAffIdCouncil(affIdCouncil);
			adef.setAffIdLocal(affIdLocal);
			adef.setAffIdSubUnit(affIdSubUnit);
			adef.setEmployerName(employerName);

			adef.setAffIdStatus("Yes");

			adef.setAddYear(year);
			adef.setEmpAffPk(empAffPk);

			StatMembership statMembership = null;

			int affPk = dataEntryDao.getEmployerAffFk(empAffPk);
			statMembership = dataEntryDao.getStatMembership(year, affPk, 0);

			adef.setStatAverage(TextUtil.formatInt(statMembership.getStatMbrCt()));
			adef.setMembershipCt(TextUtil.formatInt(statMembership.getMbrshpCt()));

			// needed for jsp
			request.setAttribute("agreements", agreements);
			request.setAttribute("showSearchResult", "false");
			request.setAttribute("empAffPk", ""+empAffPk);
			request.setAttribute("empActive", "Yes");
			request.setAttribute("addDataEntry", adef);
		}

    	// Forward control to the specified success URI
    	return (mapping.findForward(target));
  }
}
