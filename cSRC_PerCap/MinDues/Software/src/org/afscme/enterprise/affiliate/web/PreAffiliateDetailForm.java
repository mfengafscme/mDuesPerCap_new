package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.organization.LocationData;
import org.apache.log4j.Logger;

/**
 * @struts:form name="preAffiliateDetailForm"
 */
public class PreAffiliateDetailForm extends org.apache.struts.action.ActionForm {

    private static Logger logger =  Logger.getLogger(PreAffiliateDetailForm.class);

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

	private String saveImpRecdBtn;
	private String approveImpRecdBtn;

    /*
    private String comment;
    private Integer createdBy;
    private String createdDate;
    private Integer modifiedBy;
    private String modifiedDate;
    */

    /** Creates a new instance of PreAffiliateDetailForm */
    public PreAffiliateDetailForm() {
        this.init();
    }

	// Struts Methods...
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        /*
        if (TextUtil.isEmptyOrSpaces(this.getAffilName())) {
            errors.add("affilName", new ActionError("error.field.required.generic", "Abbreviated Affiliate Name"));
        }
        if (this.getAfscmeRegion() == null) {
            errors.add("afscmeRegion", new ActionError("error.field.required.generic", "AFSCME Region"));
        }
        if (this.getAnnualCardRunType() == null) {
            errors.add("annualCardRunType", new ActionError("error.field.required.generic", "Annual Card Run Type"));
        }
        if (this.isContainsSubLocals() && !this.isSubLocalsAllowed()) {
            errors.add("subLocalsAllowed", new ActionError("error.affiliate.hasSubLocals"));
        }

        if (this.getMemberRenewal() == null && (this.getAffIdType().charValue() == 'R' || this.getAffIdType().charValue() == 'S')) {
            errors.add("memberRenewal", new ActionError("error.affiliate.renewal.required"));
        }
		*/

        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }

	// General Methods...
    protected void init() {
		//this.rowId = null;

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

	/*
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", parentFk = ");
        sb.append(this.parentFk);
        sb.append(", affIdType = ");
        sb.append(this.affIdType);
        sb.append(", affIdLocal = ");
        sb.append(this.affIdLocal);
        sb.append(", affIdState = ");
        sb.append(this.affIdState);
        sb.append(", affIdSubUnit = ");
        sb.append(this.affIdSubUnit);
        sb.append(", affIdCouncil = ");
        sb.append(this.affIdCouncil);
        sb.append(", affIdCode = ");
        sb.append(this.affIdCode);
        sb.append(", affilName = ");
        sb.append(this.affilName);
        sb.append(", affilStatus = ");
        sb.append(this.affilStatus);
        sb.append(", afscmeLegislativeDistrict = ");
        sb.append(this.afscmeLegislativeDistrict);
        sb.append(", afscmeRegion = ");
        sb.append(this.afscmeRegion);
        sb.append(", multipleEmployers = ");
        sb.append(this.multipleEmployers);
        sb.append(", employerSectors = ");
        sb.append(CollectionUtil.toString(this.employerSectors));
        sb.append(", subLocalsAllowed = ");
        sb.append(this.subLocalsAllowed);
        sb.append(", containsSubLocals = ");
        sb.append(this.containsSubLocals);
        sb.append(", newAffIdPk = ");
        sb.append(this.newAffIdPk);
        sb.append(", newAffIdType = ");
        sb.append(this.newAffIdType);
        sb.append(", newAffIdLocal = ");
        sb.append(this.newAffIdLocal);
        sb.append(", newAffIdState = ");
        sb.append(this.newAffIdState);
        sb.append(", newAffIdSubUnit = ");
        sb.append(this.newAffIdSubUnit);
        sb.append(", newAffIdCouncil = ");
        sb.append(this.newAffIdCouncil);
        sb.append(", multipleOffices = ");
        sb.append(this.multipleOffices);
        sb.append(", annualCardRunType = ");
        sb.append(this.annualCardRunType);
        sb.append(", annualCardRunPerformed = ");
        sb.append(this.annualCardRunPerformed);
        sb.append(", memberRenewal = ");
        sb.append(this.memberRenewal);
        sb.append(", website = ");
        sb.append(this.website);
        sb.append(", locationPk = ");
        sb.append(this.locationPk);
        sb.append(", comment = ");
        sb.append(this.comment);
        sb.append(", createdBy = ");
        sb.append(this.createdBy);
        sb.append(", createdDate = ");
        sb.append(this.createdDate);
        sb.append(", modifiedBy = ");
        sb.append(this.modifiedBy);
        sb.append(", modifiedDate = ");
        sb.append(this.modifiedDate);
        sb.append("}");
        return sb.toString().trim();
    }
    */

	// Getter and Setter Methods...
	/*
    public AffiliateData getPreAffiliateData() {
        AffiliateData data = new AffiliateData();
        //AffiliateIdentifier affId = new AffiliateIdentifier();

        //RecordData rData = new RecordData();

        data.setAbbreviatedName(this.getAffilName());
        data.setAffPk(this.getAffPk());


        affId.setCode(this.getAffIdCode());
        affId.setCouncil(this.getAffIdCouncil());
        affId.setLocal(this.getAffIdLocal());
        affId.setState(this.getAffIdState());
        affId.setSubUnit(this.getAffIdSubUnit());
        affId.setType(this.getAffIdType());
        affId.setAdministrativeLegislativeCouncil(this.getAffIdAdminLegisCouncil());


        // data.setAffiliateId(affId);

        data.setComment(this.getComment());



        rData.setCreatedBy(this.getCreatedBy());
        rData.setCreatedDate(DateUtil.getTimestamp(this.getCreatedDate()));
        rData.setModifiedBy(this.getModifiedBy());
        rData.setModifiedDate(DateUtil.getTimestamp(this.getModifiedDate()));


        return data;
    }

    public void setPreAffiliateData(AffiliateData data) {
        if (data.getAffiliateId() != null) {
            this.setAffIdCode(data.getAffiliateId().getCode());
            this.setAffIdCouncil(data.getAffiliateId().getCouncil());
            this.setAffIdLocal(data.getAffiliateId().getLocal());
            this.setAffIdState(data.getAffiliateId().getState());
            this.setAffIdSubUnit(data.getAffiliateId().getSubUnit());
            this.setAffIdType(data.getAffiliateId().getType());
            this.setAffIdAdminLegisCouncil(data.getAffiliateId().getAdministrativeLegislativeCouncil());
        }
        this.setAffPk(data.getAffPk());
        this.setAffilName(data.getAbbreviatedName());
        this.setAffilStatus(data.getStatusCodePk());
        this.setAfscmeLegislativeDistrict(data.getAfscmeLegislativeDistrictCodePk());
        this.setAfscmeRegion(data.getAfscmeRegionCodePk());
        this.setAnnualCardRunPerformed(TextUtil.getPrimitiveBoolean(data.getAnnualCardRunPerformed()));
        this.setAnnualCardRunType(data.getAnnualCardRunTypeCodePk());
        this.setComment(data.getComment());
        this.setCreatedBy(data.getRecordData().getCreatedBy());
        this.setCreatedDate(TextUtil.format(data.getRecordData().getCreatedDate()));
        this.setEmployerSectors(data.getEmployerSector());
        this.setLocationPk(data.getLocationPk());
        this.setMemberRenewal(data.getMemberRenewalCodePk());
        this.setModifiedBy(data.getRecordData().getModifiedBy());
        this.setModifiedDate(TextUtil.format(data.getRecordData().getModifiedDate()));
        this.setMultipleEmployers(TextUtil.getPrimitiveBoolean(data.getMultipleEmployers()));
        this.setMultipleOffices(TextUtil.getPrimitiveBoolean(data.getMultipleOffices()));
        this.setNewAffIdPk(data.getNewAffiliateIDSourcePk());
        this.setParentFk(data.getParentFk());
        this.setSubLocalsAllowed(TextUtil.getPrimitiveBoolean(data.getAllowSubLocals()));
        this.setContainsSubLocals(TextUtil.getPrimitiveBoolean(data.getContainsSubLocals()));
        this.setWebsite(data.getWebsite());
    }

    public void setAffIdData(AffiliateData data) {
        //this.setNewAffIdPk(data.getAffPk());
        if (data.getAffiliateId() != null) {
            this.setNewAffIdCode(data.getAffiliateId().getCode());
            this.setNewAffIdCouncil(data.getAffiliateId().getCouncil());
            this.setNewAffIdLocal(data.getAffiliateId().getLocal());
            this.setNewAffIdState(data.getAffiliateId().getState());
            this.setNewAffIdSubUnit(data.getAffiliateId().getSubUnit());
            this.setNewAffIdType(data.getAffiliateId().getType());
        }
    }
	*/

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

	public String getSaveImpRecdBtn() {
		return saveImpRecdBtn;
	}

	public void setSaveImpRecdBtn(String saveImpRecdBtn) {
		this.saveImpRecdBtn = saveImpRecdBtn;
	}

	public String getApproveImpRecdBtn() {
		return approveImpRecdBtn;
	}

	public void setApproveImpRecdBtn(String approveImpRecdBtn) {
		this.approveImpRecdBtn = approveImpRecdBtn;
	}

}
