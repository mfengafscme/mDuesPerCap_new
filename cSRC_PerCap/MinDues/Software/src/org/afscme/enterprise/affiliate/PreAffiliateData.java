package org.afscme.enterprise.affiliate;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.CollectionUtil;
import java.util.Collection;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * Represents pre-affilaite data.  Roughly the data on the 'Pre Affiliate Detail' screen.
 */
public class PreAffiliateData {
	// private String rowId;

    private String affPk;
    private String empAffPk;


    private String affIdState;
    private String affIdCouncil;
    private String affIdLocal;
    private String affIdSubUnit;

    private String employerName;


    /////////////////////////////
    private String agmtEffDate;
    private String agmtExpDate;
    private String noMemFeePayer;
    private String ifRecInc;
    private String ifInNego;

    private String percentWageInc;
    private String wageIncEffDate;
    private String noMemFeePayerAff1;
    //// OR
    private String centPerHrDoLumpSumBonus;
    private String avgWagePerHrYr;
    private String effDateInc;
    private String noMemFeePayerAff2;


    private String speWageAgj;
    private String percentInc;
    //// OR
    private String dollarCent;
    private String avgPay;
    private String noMemFeePayerAff3;
    private String contactName;
    private String contactPhoneEmail;
    private String notes;

	private String load_ID;
	private String batch_ID;
	private String processed;
	private String increase_type;
	private String statMbrCount;
	private String mbrsAfps_Affected;
	private String adj_MbrsAfps_Affected;
	private String userPosting;
	private String doNotProcess;
	private String comment;
	private String wifPk;
	private String widPk;

    public PreAffiliateData() {
		// this.rowId = null;

		this.affPk = null;
		this.empAffPk = null;


		this.affIdState = null;
		this.affIdCouncil = null;
		this.affIdLocal = null;
		this.affIdSubUnit = null;

		this.employerName = null;


		/////////////////////////////
		this.agmtEffDate = null;
		this.agmtExpDate = null;
		this.noMemFeePayer = null;
		this.ifRecInc = null;
		this.ifInNego = null;

		this.percentWageInc = null;
		this.wageIncEffDate = null;
		this.noMemFeePayerAff1 = null;
		//// OR
		this.centPerHrDoLumpSumBonus = null;
		this.avgWagePerHrYr = null;
		this.effDateInc = null;
		this.noMemFeePayerAff2 = null;


		this.speWageAgj = null;
		this.percentInc = null;
		//// OR
		this.dollarCent = null;
		this.avgPay = null;
		this.noMemFeePayerAff3 = null;
		this.contactName = null;
		this.contactPhoneEmail = null;
		this.notes = null;

		this.load_ID = null;
		this.batch_ID = null;
		this.processed = null;
		this.increase_type = null;
		this.statMbrCount = null;
		this.mbrsAfps_Affected = null;
		this.adj_MbrsAfps_Affected = null;
		this.userPosting = null;
		this.doNotProcess = null;
		this.comment = null;
		this.wifPk = null;
		this.widPk = null;
    }


	public String getEmpAffPk() {
		return empAffPk;
	}

	public void setEmpAffPk(String empAffPk) {
		this.empAffPk = empAffPk;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getAgmtEffDate() {
		return agmtEffDate;
	}

	public void setAgmtEffDate(String agmtEffDate) {
		this.agmtEffDate = agmtEffDate;
	}

	public String getAgmtExpDate() {
		return agmtExpDate;
	}

	public void setAgmtExpDate(String agmtExpDate) {
		this.agmtExpDate = agmtExpDate;
	}

	public String getNoMemFeePayer() {
		return noMemFeePayer;
	}

	public void setNoMemFeePayer(String noMemFeePayer) {
		this.noMemFeePayer = noMemFeePayer;
	}

	public String getIfRecInc() {
		return ifRecInc;
	}

	public void setIfRecInc(String ifRecInc) {
		this.ifRecInc = ifRecInc;
	}

	public String getIfInNego() {
		return ifInNego;
	}

	public void setIfInNego(String ifInNego) {
		this.ifInNego = ifInNego;
	}

	public String getPercentWageInc() {
		return percentWageInc;
	}

	public void setPercentWageInc(String percentWageInc) {
		this.percentWageInc = percentWageInc;
	}

	public String getWageIncEffDate() {
		return wageIncEffDate;
	}

	public void setWageIncEffDate(String wageIncEffDate) {
		this.wageIncEffDate = wageIncEffDate;
	}

	public String getNoMemFeePayerAff1() {
		return noMemFeePayerAff1;
	}

	public void setNoMemFeePayerAff1(String noMemFeePayerAff1) {
		this.noMemFeePayerAff1 = noMemFeePayerAff1;
	}

	public String getCentPerHrDoLumpSumBonus() {
		return centPerHrDoLumpSumBonus;
	}

	public void setCentPerHrDoLumpSumBonus(String centPerHrDoLumpSumBonus) {
		this.centPerHrDoLumpSumBonus = centPerHrDoLumpSumBonus;
	}

	public String getAvgWagePerHrYr() {
		return avgWagePerHrYr;
	}

	public void setAvgWagePerHrYr(String avgWagePerHrYr) {
		this.avgWagePerHrYr = avgWagePerHrYr;
	}

	public String getEffDateInc() {
		return effDateInc;
	}

	public void setEffDateInc(String effDateInc) {
		this.effDateInc = effDateInc;
	}

	public String getNoMemFeePayerAff2() {
		return noMemFeePayerAff2;
	}

	public void setNoMemFeePayerAff2(String noMemFeePayerAff2) {
		this.noMemFeePayerAff2 = noMemFeePayerAff2;
	}

	public String getSpeWageAgj() {
		return speWageAgj;
	}

	public void setSpeWageAgj(String speWageAgj) {
		this.speWageAgj = speWageAgj;
	}

	public String getPercentInc() {
		return percentInc;
	}

	public void setPercentInc(String percentInc) {
		this.percentInc = percentInc;
	}

	public String getDollarCent() {
		return dollarCent;
	}

	public void setDollarCent(String dollarCent) {
		this.dollarCent = dollarCent;
	}

	public String getAvgPay() {
		return avgPay;
	}

	public void setAvgPay(String avgPay) {
		this.avgPay = avgPay;
	}

	public String getNoMemFeePayerAff3() {
		return noMemFeePayerAff3;
	}

	public void setNoMemFeePayerAff3(String noMemFeePayerAff3) {
		this.noMemFeePayerAff3 = noMemFeePayerAff3;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhoneEmail() {
		return contactPhoneEmail;
	}

	public void setContactPhoneEmail(String contactPhoneEmail) {
		this.contactPhoneEmail = contactPhoneEmail;
	}


	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}


	public void setAffPk(String affPk) {
		this.affPk = affPk;
	}

	public String getAffPk() {
	    return this.affPk;
	}

	public String getAffIdCouncil() {
	  return affIdCouncil;
	}


	public void setAffIdCouncil(String affIdCouncil) {
	    this.affIdCouncil = affIdCouncil;
	}

	public String getAffIdLocal() {
	    return affIdLocal;
	}

	public void setAffIdLocal(String affIdLocal) {
	    this.affIdLocal = affIdLocal;
	}

	public String getAffIdState() {
	    return affIdState;
	}

	public void setAffIdState(String affIdState) {
	    if (TextUtil.isEmptyOrSpaces(affIdState)) {
			this.affIdState = null;
	    } else {
		this.affIdState = affIdState;
	    }
	}

	public String getAffIdSubUnit() {
	    return affIdSubUnit;
	}

	public void setAffIdSubUnit(String affIdSubUnit) {
	    this.affIdSubUnit = affIdSubUnit;
	}

	/*
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getRowId() {
	    return this.rowId;
	}
	*/

	public String getLoad_ID() {
		return load_ID;
	}

	public void setLoad_ID(String load_ID) {
		this.load_ID = load_ID;
	}

	public String getBatch_ID() {
		return batch_ID;
	}

	public void setBatch_ID(String batch_ID) {
		this.batch_ID = batch_ID;
	}

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getIncrease_type() {
		return increase_type;
	}

	public void setIncrease_type(String increase_type) {
		this.increase_type = increase_type;
	}

	public String getStatMbrCount() {
		return statMbrCount;
	}

	public void setStatMbrCount(String statMbrCount) {
		this.statMbrCount = statMbrCount;
	}

	public String getMbrsAfps_Affected() {
		return mbrsAfps_Affected;
	}

	public void setMbrsAfps_Affected(String mbrsAfps_Affected) {
		this.mbrsAfps_Affected = mbrsAfps_Affected;
	}

	public String getAdj_MbrsAfps_Affected() {
		return adj_MbrsAfps_Affected;
	}

	public void setAdj_MbrsAfps_Affected(String adj_MbrsAfps_Affected) {
		this.adj_MbrsAfps_Affected = adj_MbrsAfps_Affected;
	}

	public String getUserPosting() {
		return userPosting;
	}

	public void setUserPosting(String userPosting) {
		this.userPosting = userPosting;
	}

	public String getDoNotProcess() {
		return doNotProcess;
	}

	public void setDoNotProcess(String doNotProcess) {
		this.doNotProcess = doNotProcess;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getWifPk() {
		return wifPk;
	}

	public void setWifPk(String wifPk) {
		this.wifPk = wifPk;
	}

	public String getWidPk() {
		return widPk;
	}

	public void setWidPk(String widPk) {
		this.widPk = widPk;
	}

}
