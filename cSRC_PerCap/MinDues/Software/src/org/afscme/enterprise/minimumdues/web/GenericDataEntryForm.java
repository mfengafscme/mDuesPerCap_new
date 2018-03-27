package org.afscme.enterprise.minimumdues.web;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.minimumdues.PercentWageIncBean;
import org.afscme.enterprise.minimumdues.AmountWageIncBean;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;

public class GenericDataEntryForm extends ActionForm {
    // public class GenericDataEntryForm extends org.apache.struts.validator.ValidatorForm {
    // --------------- Instance Variables --------------------
    protected String affCode;
    protected Integer affPk;
    protected String affIdType;

    protected String email = null;
    protected String telephone = null;
    protected String personName = null;

    protected String averageWage = null;

    // nested bean reference
    protected ArrayList percentWageIncList;
    protected ArrayList amountWageIncList;

    protected String formCompleted = null;
    protected String correspondence = null;
    protected String correspondenceDate = null;
    protected String comments = null;
    //protected String moreLocals = null;
    protected String inNegotiations = "no";
    protected String durationTo = null;
    protected String durationFrom = null;

    protected String amountType = null;
    protected String section = "Section A";
    protected String secType = null;

    protected String initPercent_a = null;
    protected String initEffective_a = null;
    protected String initNoOfMember_a = null;
    protected String initTypeOfPayment_adj_a = null;
    protected String initPercentInc_adj_a = null;
    protected String initNoOfMember_adj_a = null;
    protected String initMbrTimesInc_a = null;

    protected String initAmount_b = null;
    protected String initEffective_b = null;
    protected String initNoOfMember_b = null;
    protected String initTypeOfPayment_adj_b = null;
    protected String initAmountInc_adj_b = null;
    protected String initNoOfMember_adj_b = null;
    protected String initMbrTimesInc_b = null;

    protected String statAverage = null;
    protected String h_statAverage = null;
    protected String membershipCt = null;
    protected String numberOfMember = null;
    protected String employerName = null;
    protected String affIdSubUnit = null;
    protected String affIdLocal = null;
    protected String affIdCouncil = null;
    protected String affIdState = null;
    protected String affIdStatus = null;
    protected String year = null;
    protected String clearForm = null;
    protected String viewYear;
    protected String enterYear;
    protected int empAffPk;
    protected String agreementReceived;
    protected String agreementDesc;
    protected String empActive;

    protected String agreementPk;
    protected String agreementName;
    protected String selectAgreement;
    protected String viewAgreementBtn;


    public GenericDataEntryForm() {
        amountType = "cent/hr";
        section = "Section A";
        secType = null;
        inNegotiations = "no";
        percentWageIncList = new ArrayList();
        amountWageIncList = new ArrayList();
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        percentWageIncList = (ArrayList) request.getSession().getAttribute("secARowList");
        amountWageIncList = (ArrayList) request.getSession().getAttribute("secBRowList");

        if (percentWageIncList == null)
            percentWageIncList = new ArrayList();

        if (amountWageIncList == null)
            amountWageIncList = new ArrayList();
    }

    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
/*
    if ((request.getParameter("addPencentageIncRowButton") == null)
        && (request.getParameter("addAmountIncRowButton") == null) ) {
            if (TextUtil.isEmptyOrSpaces(this.getYear())) {
                errors.add("year", new ActionError("error.field.required.generic", "Year"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getAffIdState())) {
                errors.add("affIdState", new ActionError("error.field.required.generic", "Affiliate Identifier State"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getAffIdCouncil())) {
                errors.add("affIdCouncil", new ActionError("error.field.required.generic", "Affiliate Identifier Council"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getAffIdLocal())) {
                errors.add("affIdLocal", new ActionError("error.field.required.generic", "Affiliate Identifier Local"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getEmployerName())) {
                errors.add("employerName", new ActionError("error.field.required.generic", "Employer Name"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getNumberOfMember())) {
                errors.add("numberOfMember", new ActionError("error.field.required.generic", "Total Number of Members and Fee payers"));
            }

            // if inNegotiations is no, then from and to date are required
                if (this.getInNegotiations().equalsIgnoreCase("no")) {
                    if (TextUtil.isEmptyOrSpaces(this.getDurationFrom()) || TextUtil.isEmptyOrSpaces(this.getDurationTo())) {
                        errors.add("durationErrors", new ActionError("error.field.required.generic", "Since the agreement is not in negotiation, the From / To Date field(s) "));
                    }
                }

            // if sec B is chosen, average wage is required
                if (this.getSection().equalsIgnoreCase("Section B")) {
                    if (TextUtil.isEmptyOrSpaces(this.getAverageWage())) {
                        errors.add("averageWage", new ActionError("error.field.required.generic", "Since Sec B is chosen for entering data, the Average Wage field "));
                    }
                }


            // if correspondence is checked, then correspondence date is required
                if ((this.getCorrespondence() != null) && (this.getCorrespondence().equalsIgnoreCase("no"))) {
                    if (TextUtil.isEmptyOrSpaces(this.getCorrespondenceDate())) {
                        errors.add("correspondenceDate", new ActionError("error.field.required.generic", "Since Correspondence is checked, the Correspondence Date field "));
                    }
                }

            if (TextUtil.isEmptyOrSpaces(this.getPersonName())) {
                errors.add("personName", new ActionError("error.field.required.generic", "Person Complete the Form"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getTelephone())) {
                errors.add("telephone", new ActionError("error.field.required.generic", "Telephone Number"));
            }

                request.setAttribute("errors", errors);
                //request.setAttribute("GenericDataEntryForm", this);
        }
 */
        return errors;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return (this.telephone);
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonName() {
        return this.personName;
    }

    /** Getter for property averageWage.
     * @return Value of property averageWage.
     *
     */
    public String getAverageWage() {
        return averageWage;
    }

    /** Setter for property averageWage.
     * @param averageWage New value of property averageWage.
     *
     */
    public void setAverageWage(String averageWage) {
        this.averageWage = averageWage;
    }

    /** Getter for property durationTo.
     * @return Value of property durationTo.
     *
     */
    public java.lang.String getDurationTo() {
        return durationTo;
    }

    /** Setter for property durationTo.
     * @param durationTo New value of property durationTo.
     *
     */
    public void setDurationTo(java.lang.String durationTo) {
        this.durationTo = durationTo;
    }

    /** Getter for property durationFrom.
     * @return Value of property durationFrom.
     *
     */
    public java.lang.String getDurationFrom() {
        return durationFrom;
    }

    /** Setter for property durationFrom.
     * @param durationFrom New value of property durationFrom.
     *
     */
    public void setDurationFrom(java.lang.String durationFrom) {
        this.durationFrom = durationFrom;
    }

    /** Getter for property inNegotiations.
     * @return Value of property inNegotiations.
     *
     */
    public String getInNegotiations() {
        return inNegotiations;
    }

    /** Setter for property inNegotiations.
     * @param inNegotiations New value of property inNegotiations.
     *
     */
    public void setInNegotiations(String inNegotiations) {
        this.inNegotiations = inNegotiations;
    }

    /** Getter for property numberOfMember.
     * @return Value of property numberOfMember.
     *
     */
    public String getNumberOfMember() {
        return numberOfMember;
    }

    /** Setter for property numberOfMember.
     * @param numberOfMember New value of property numberOfMember.
     *
     */
    public void setNumberOfMember(String numberOfMember) {
        this.numberOfMember = numberOfMember;
    }

    /** Getter for property employerName.
     * @return Value of property employerName.
     *
     */
    public java.lang.String getEmployerName() {
        return this.employerName;
    }

    /** Setter for property employerName.
     * @param employerName New value of property employerName.
     *
     */
    public void setEmployerName(java.lang.String employerName) {
        this.employerName = employerName;
    }

    public java.lang.Integer getAffPk() {
        return affPk;
    }

    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }

    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffCode(java.lang.String affCode) {
        this.affCode = affCode;
    }

    public java.lang.String getAffCode() {
        return affCode;
    }

    /** Getter for property affIdSubUnit.
     * @return Value of property affIdSubUnit.
     *
     */
    public java.lang.String getAffIdSubUnit() {
        return affIdSubUnit;
    }

    /** Setter for property affIdSubUnit.
     * @param affIdSubUnit New value of property affIdSubUnit.
     *
     */
    public void setAffIdSubUnit(java.lang.String affIdSubUnit) {
        this.affIdSubUnit = affIdSubUnit;
    }

    /** Getter for property affIdLocal.
     * @return Value of property affIdLocal.
     *
     */
    public java.lang.String getAffIdLocal() {
        return affIdLocal;
    }

    /** Setter for property affIdLocal.
     * @param affIdLocal New value of property affIdLocal.
     *
     */
    public void setAffIdLocal(java.lang.String affIdLocal) {
        this.affIdLocal = affIdLocal;
    }

    /** Getter for property affIdCouncil.
     * @return Value of property affIdCouncil.
     *
     */
    public java.lang.String getAffIdCouncil() {
        return affIdCouncil;
    }

    /** Setter for property affIdCouncil.
     * @param affIdCouncil New value of property affIdCouncil.
     *
     */
    public void setAffIdCouncil(java.lang.String affIdCouncil) {
        this.affIdCouncil = affIdCouncil;
    }

    /** Getter for property affIdState.
     * @return Value of property affIdState.
     *
     */
    public java.lang.String getAffIdState() {
        return affIdState;
    }

    /** Setter for property affIdState.
     * @param affIdState New value of property affIdState.
     *
     */
    public void setAffIdState(java.lang.String affIdState) {
        this.affIdState = affIdState;
    }

    public java.lang.String getYear() {
        return year;
    }

    public void setYear(java.lang.String year) {
        this.year = year;
    }

    /**
     *  This setter is fired if a user clicks on the New Row button (Sec A) on the
     *  dataEntry.jsp page allowing the internals to adjust accordingly.
     */
    public void setAddPencentageIncRowButton(String empty) {
        PercentWageIncBean pwib = new PercentWageIncBean();
        pwib.init();

        this.percentWageIncList.add(pwib);
    }

    /**
     *  This setter is fired if a user clicks on the New Row button on the
     *  dataEntry.jsp page allowing the internals to adjust accordingly.
     */
    public void setAddAmountIncRowButton(String empty) {
        AmountWageIncBean awib = new AmountWageIncBean();
        awib.init();

        this.amountWageIncList.add(awib);
    }

  /*
  public String getPercentWageIncCount() {
      return Integer.toString(percentWageIncList.size());
  }
   */

    /** Getter for property initPercent_a.
     * @return Value of property initPercent_a.
     *
     */
    public java.lang.String getInitPercent_a() {
        return initPercent_a;
    }

    /** Setter for property initPercent_a.
     * @param initPercent_a New value of property initPercent_a.
     *
     */
    public void setInitPercent_a(java.lang.String initPercent_a) {
        this.initPercent_a = initPercent_a;
    }

    /** Getter for property initEffective_a.
     * @return Value of property initEffective_a.
     *
     */
    public java.lang.String getInitEffective_a() {
        return initEffective_a;
    }

    /** Setter for property initEffective_a.
     * @param initEffective_a New value of property initEffective_a.
     *
     */
    public void setInitEffective_a(java.lang.String initEffective_a) {
        this.initEffective_a = initEffective_a;
    }

    /** Getter for property initNoOfMember_a.
     * @return Value of property initNoOfMember_a.
     *
     */
    public java.lang.String getInitNoOfMember_a() {
        return initNoOfMember_a;
    }

    /** Setter for property initNoOfMember_a.
     * @param initNoOfMember_a New value of property initNoOfMember_a.
     *
     */
    public void setInitNoOfMember_a(java.lang.String initNoOfMember_a) {
        this.initNoOfMember_a = initNoOfMember_a;
    }

    /** Getter for property initAmount_b.
     * @return Value of property initAmount_b.
     *
     */
    public java.lang.String getInitAmount_b() {
        return initAmount_b;
    }

    /** Setter for property initAmount_b.
     * @param initAmount_b New value of property initAmount_b.
     *
     */
    public void setInitAmount_b(java.lang.String initAmount_b) {
        this.initAmount_b = initAmount_b;
    }

    /** Getter for property initEffective_b.
     * @return Value of property initEffective_b.
     *
     */
    public java.lang.String getInitEffective_b() {
        return initEffective_b;
    }

    /** Setter for property initEffective_b.
     * @param initEffective_b New value of property initEffective_b.
     *
     */
    public void setInitEffective_b(java.lang.String initEffective_b) {
        this.initEffective_b = initEffective_b;
    }

    /** Getter for property initNoOfMember_b.
     * @return Value of property initNoOfMember_b.
     *
     */
    public java.lang.String getInitNoOfMember_b() {
        return initNoOfMember_b;
    }

    /** Setter for property initNoOfMember_b.
     * @param initNoOfMember_b New value of property initNoOfMember_b.
     *
     */
    public void setInitNoOfMember_b(java.lang.String initNoOfMember_b) {
        this.initNoOfMember_b = initNoOfMember_b;
    }

    /** Getter for property initTypeOfPayment_adj_a.
     * @return Value of property initTypeOfPayment_adj_a.
     *
     */
    public java.lang.String getInitTypeOfPayment_adj_a() {
        return initTypeOfPayment_adj_a;
    }

    /** Setter for property initTypeOfPayment_adj_a.
     * @param initTypeOfPayment_adj_a New value of property initTypeOfPayment_adj_a.
     *
     */
    public void setInitTypeOfPayment_adj_a(java.lang.String initTypeOfPayment_adj_a) {
        this.initTypeOfPayment_adj_a = initTypeOfPayment_adj_a;
    }

    /** Getter for property initPercentInc_adj_a.
     * @return Value of property initPercentInc_adj_a.
     *
     */
    public java.lang.String getInitPercentInc_adj_a() {
        return initPercentInc_adj_a;
    }

    /** Setter for property initPercentInc_adj_a.
     * @param initPercentInc_adj_a New value of property initPercentInc_adj_a.
     *
     */
    public void setInitPercentInc_adj_a(java.lang.String initPercentInc_adj_a) {
        this.initPercentInc_adj_a = initPercentInc_adj_a;
    }

    /** Getter for property initNoOfMember_adj_a.
     * @return Value of property initNoOfMember_adj_a.
     *
     */
    public java.lang.String getInitNoOfMember_adj_a() {
        return initNoOfMember_adj_a;
    }

    /** Setter for property initNoOfMember_adj_a.
     * @param initNoOfMember_adj_a New value of property initNoOfMember_adj_a.
     *
     */
    public void setInitNoOfMember_adj_a(java.lang.String initNoOfMember_adj_a) {
        this.initNoOfMember_adj_a = initNoOfMember_adj_a;
    }

    /** Getter for property initTypeOfPayment_adj_b.
     * @return Value of property initTypeOfPayment_adj_b.
     *
     */
    public java.lang.String getInitTypeOfPayment_adj_b() {
        return initTypeOfPayment_adj_b;
    }

    /** Setter for property initTypeOfPayment_adj_b.
     * @param initTypeOfPayment_adj_b New value of property initTypeOfPayment_adj_b.
     *
     */
    public void setInitTypeOfPayment_adj_b(java.lang.String initTypeOfPayment_adj_b) {
        this.initTypeOfPayment_adj_b = initTypeOfPayment_adj_b;
    }

    /** Getter for property initAmountInc_adj_b.
     * @return Value of property initAmountInc_adj_b.
     *
     */
    public java.lang.String getInitAmountInc_adj_b() {
        return initAmountInc_adj_b;
    }

    /** Setter for property initAmountInc_adj_b.
     * @param initAmountInc_adj_b New value of property initAmountInc_adj_b.
     *
     */
    public void setInitAmountInc_adj_b(java.lang.String initAmountInc_adj_b) {
        this.initAmountInc_adj_b = initAmountInc_adj_b;
    }

    /** Getter for property initNoOfMember_adj_b.
     * @return Value of property initNoOfMember_adj_b.
     *
     */
    public java.lang.String getInitNoOfMember_adj_b() {
        return initNoOfMember_adj_b;
    }

    /** Setter for property initNoOfMember_adj_b.
     * @param initNoOfMember_adj_b New value of property initNoOfMember_adj_b.
     *
     */
    public void setInitNoOfMember_adj_b(java.lang.String initNoOfMember_adj_b) {
        this.initNoOfMember_adj_b = initNoOfMember_adj_b;
    }

    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public java.lang.String getComments() {
        return comments;
    }

    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }

    /*
    public java.lang.String getMoreLocals() {
        return moreLocals;
    }

    public void setMoreLocals(java.lang.String moreLocals) {
        this.moreLocals = moreLocals;
    }
    */

    /** Getter for property formCompleted.
     * @return Value of property formCompleted.
     *
     */
    public String getFormCompleted() {
        return formCompleted;
    }

    /** Setter for property formCompleted.
     * @param formCompleted New value of property formCompleted.
     *
     */
    public void setFormCompleted(String formCompleted) {
        this.formCompleted = formCompleted;
    }

    /** Getter for property correspondence.
     * @return Value of property correspondence.
     *
     */
    public String getCorrespondence() {
        return correspondence;
    }

    /** Setter for property correspondence.
     * @param correspondence New value of property correspondence.
     *
     */
    public void setCorrespondence(String correspondence) {
        this.correspondence = correspondence;
    }

    /** Getter for property correspondenceDate.
     * @return Value of property correspondenceDate.
     *
     */
    public java.lang.String getCorrespondenceDate() {
        return correspondenceDate;
    }

    /** Setter for property correspondenceDate.
     * @param correspondenceDate New value of property correspondenceDate.
     *
     */
    public void setCorrespondenceDate(java.lang.String correspondenceDate) {
        this.correspondenceDate = correspondenceDate;
    }

    /** Getter for property initMbrTimesInc_a.
     * @return Value of property initMbrTimesInc_a.
     *
     */
    public java.lang.String getInitMbrTimesInc_a() {
        return initMbrTimesInc_a;
    }

    /** Setter for property initMbrTimesInc_a.
     * @param initMbrTimesInc_a New value of property initMbrTimesInc_a.
     *
     */
    public void setInitMbrTimesInc_a(java.lang.String initMbrTimesInc_a) {
        this.initMbrTimesInc_a = initMbrTimesInc_a;
    }

    /** Getter for property initMbrTimesInc_b.
     * @return Value of property initMbrTimesInc_b.
     *
     */
    public java.lang.String getInitMbrTimesInc_b() {
        return initMbrTimesInc_b;
    }

    /** Setter for property initMbrTimesInc_b.
     * @param initMbrTimesInc_b New value of property initMbrTimesInc_b.
     *
     */
    public void setInitMbrTimesInc_b(java.lang.String initMbrTimesInc_b) {
        this.initMbrTimesInc_b = initMbrTimesInc_b;
    }

    /** Getter for property statAverage.
     * @return Value of property statAverage.
     *
     */
    public java.lang.String getStatAverage() {
        return statAverage;
    }

    /** Setter for property statAverage.
     * @param statAverage New value of property statAverage.
     *
     */
    public void setStatAverage(java.lang.String statAverage) {
        this.statAverage = statAverage;
    }

    /** Getter for property h_statAverage.
     * @return Value of property h_statAverage.
     *
     */
    public java.lang.String getH_statAverage() {
        return h_statAverage;
    }

    /** Setter for property h_statAverage.
     * @param h_statAverage New value of property h_statAverage.
     *
     */
    public void setH_statAverage(java.lang.String h_statAverage) {
        this.h_statAverage = h_statAverage;
    }

    /** Getter for property membershipCt.
     * @return Value of property membershipCt.
     *
     */
    public java.lang.String getMembershipCt() {
        return membershipCt;
    }

    /** Setter for property membershipCt.
     * @param membershipCt New value of property membershipCt.
     *
     */
    public void setMembershipCt(java.lang.String membershipCt) {
        this.membershipCt = membershipCt;
    }

    /** Getter for property clearForm.
     * @return Value of property clearForm.
     *
     */
    public java.lang.String getClearForm() {
        return clearForm;
    }

    /** Setter for property clearForm.
     * @param clearForm New value of property clearForm.
     *
     */
    public void setClearForm(java.lang.String clearForm) {
        this.clearForm = clearForm;
    }

    /** Getter for property amountType.
     * @return Value of property amountType.
     *
     */
    public java.lang.String getAmountType() {
        return amountType;
    }

    /** Setter for property amountType.
     * @param amountType New value of property amountType.
     *
     */
    public void setAmountType(java.lang.String amountType) {
        this.amountType = amountType;
    }

    /** Getter for property section.
     * @return Value of property section.
     *
     */
    public java.lang.String getSection() {
        return section;
    }

    /** Setter for property section.
     * @param section New value of property section.
     *
     */
    public void setSection(java.lang.String section) {
        this.section = section;
    }

    /** Getter for property secType.
     * @return Value of property secType.
     *
     */
    public java.lang.String getSecType() {
        return secType;
    }

    /** Setter for property secType.
     * @param secType New value of property secType.
     *
     */
    public void setSecType(java.lang.String secType) {
        this.secType = secType;
    }

    /** Getter for property viewYear.
     * @return Value of property viewYear.
     *
     */
    public java.lang.String getViewYear() {
        return viewYear;
    }

    /** Setter for property viewYear.
     * @param viewYear New value of property viewYear.
     *
     */
    public void setViewYear(java.lang.String viewYear) {
        this.viewYear = viewYear;
    }

    /** Getter for property enterYear.
     * @return Value of property enterYear.
     *
     */
    public java.lang.String getEnterYear() {
        return enterYear;
    }

    /** Setter for property enterYear.
     * @param enterYear New value of property enterYear.
     *
     */
    public void setEnterYear(java.lang.String enterYear) {
        this.enterYear = enterYear;
    }

    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public java.lang.String getAffIdType() {
        return affIdType;
    }

    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(java.lang.String affIdType) {
        this.affIdType = affIdType;
    }

    /** Getter for property email.
     * @return Value of property email.
     *
     */
    public java.lang.String getEmail() {
        return email;
    }

    /** Setter for property email.
     * @param email New value of property email.
     *
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    /** Getter for property empAffPk.
     * @return Value of property empAffPk.
     *
     */
    public int getEmpAffPk() {
        return empAffPk;
    }

    /** Setter for property empAffPk.
     * @param empAffPk New value of property empAffPk.
     *
     */
    public void setEmpAffPk(int empAffPk) {
        this.empAffPk = empAffPk;
    }

    /** Getter for property percentWageIncList.
     * @return Value of property percentWageIncList.
     *
     */
    public java.util.ArrayList getPercentWageIncList() {
        return percentWageIncList;
    }

    /** Setter for property percentWageIncList.
     * @param percentWageIncList New value of property percentWageIncList.
     *
     */
    public void setPercentWageIncList(java.util.ArrayList percentWageIncList) {
        this.percentWageIncList = percentWageIncList;
    }

    /** Getter for property amountWageIncList.
     * @return Value of property amountWageIncList.
     *
     */
    public java.util.ArrayList getAmountWageIncList() {
        return amountWageIncList;
    }

    /** Setter for property amountWageIncList.
     * @param amountWageIncList New value of property amountWageIncList.
     *
     */
    public void setAmountWageIncList(java.util.ArrayList amountWageIncList) {
        this.amountWageIncList = amountWageIncList;
    }

    /** Getter for property agreementReceived.
     * @return Value of property agreementReceived.
     *
     */
    public java.lang.String getAgreementReceived() {
        return agreementReceived;
    }

    /** Setter for property agreementReceived.
     * @param agreementReceived New value of property agreementReceived.
     *
     */
    public void setAgreementReceived(java.lang.String agreementReceived) {
        this.agreementReceived = agreementReceived;
    }

    /** Getter for property agreementDesc.
     * @return Value of property agreementDesc.
     *
     */
    public java.lang.String getAgreementDesc() {
        return agreementDesc;
    }

    /** Setter for property agreementDesc.
     * @param agreementDesc New value of property agreementDesc.
     *
     */
    public void setAgreementDesc(java.lang.String agreementDesc) {
        this.agreementDesc = agreementDesc;
    }

    /** Getter for property affIdStatus.
     * @return Value of property affIdStatus.
     *
     */
    public java.lang.String getAffIdStatus() {
        return affIdStatus;
    }

    /** Setter for property affIdStatus.
     * @param affIdStatus New value of property affIdStatus.
     *
     */
    public void setAffIdStatus(java.lang.String affIdStatus) {
        this.affIdStatus = affIdStatus;
    }

    /** Getter for property empActive.
     * @return Value of property empActive.
     *
     */
    public java.lang.String getEmpActive() {
        return empActive;
    }

    /** Setter for property empActive.
     * @param empActive New value of property empActive.
     *
     */
    public void setEmpActive(java.lang.String empActive) {
        this.empActive = empActive;
    }

    /** Getter for property agreementPk.
     * @return Value of property agreementPk.
     *
     */
    public java.lang.String getAgreementPk() {
        return agreementPk;
    }

    /** Setter for property agreementPk.
     * @param agreementPk New value of property agreementPk.
     *
     */
    public void setAgreementPk(java.lang.String agreementPk) {
        this.agreementPk = agreementPk;
    }

    /** Getter for property agreementName.
     * @return Value of property agreementName.
     *
     */
    public java.lang.String getAgreementName() {
        return agreementName;
    }

    /** Setter for property agreementName.
     * @param agreementName New value of property agreementName.
     *
     */
    public void setAgreementName(java.lang.String agreementName) {
        this.agreementName = agreementName;
    }

    /** Getter for property viewAgreementBtn.
     * @return Value of property viewAgreementBtn.
     *
     */
    public java.lang.String getViewAgreementBtn() {
        return viewAgreementBtn;
    }

    /** Setter for property viewAgreementBtn.
     * @param viewAgreementBtn New value of property viewAgreementBtn.
     *
     */
    public void setViewAgreementBtn(java.lang.String viewAgreementBtn) {
        this.viewAgreementBtn = viewAgreementBtn;
    }

    /** Getter for property selectAgreement.
     * @return Value of property selectAgreement.
     *
     */
    public java.lang.String getSelectAgreement() {
        return selectAgreement;
    }

    /** Setter for property selectAgreement.
     * @param selectAgreement New value of property selectAgreement.
     *
     */
    public void setSelectAgreement(java.lang.String selectAgreement) {
        this.selectAgreement = selectAgreement;
    }

}
