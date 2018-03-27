/*
 * MemberAffiliateInformationForm.java
 *
 * This form supports both the Member Affiliate Information and Member Affiliate Information - Edit
 * pages
 *
 * Created on August 18, 2003, 8:09 PM
 *
 * Modified on October 27, 2003 to support View Data Utility data access control 
 *
 */

package org.afscme.enterprise.member.web;

import org.afscme.enterprise.member.MemberOfficerTitleAddressInfo;
import org.afscme.enterprise.member.MemberAffiliateData;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;


/**
 * @struts:form name="memberAffiliateInformationForm"
 */
public class MemberAffiliateInformationForm extends org.apache.struts.action.ActionForm {

    private static Logger logger =  Logger.getLogger(MemberAffiliateInformationForm.class);     
    
    /**
 * Contains data that is unique to a members involvement in a specific affiliate
 */

    private Integer personPk;
    private Integer affPk;

    private String affType; // needed for business rule

    private Integer mbrStatus;
    private Integer mbrType;
    private String mbrNoLocal;
    private java.sql.Timestamp mbrCardSentDt;
    private Integer primaryInformationSource;
    private Boolean lostTimeLanguageFg;
    private java.sql.Timestamp mbrJoinDt;
    // for edit of date
    private Integer monthJoined; // for edit
    private String yearJoined;  // for edit

    private Integer mbrDuesType;
    private Double mbrDuesRate;
    private Integer mbrDuesFrequency;
    private java.sql.Timestamp mbrRetiredDt;
    // for edit of Retired Date
    private Integer monthRetired;
    private String yearRetired;

    private Boolean mbrRetRenewalDuesFg;
    private Boolean noCardsFg;
    private Boolean noMailFg;
    private Boolean noPublicEmpFg;
    private Boolean noLegislativeMailFg;

    // hidden fields
    private Boolean noMailFlag;
    private Boolean noCardsFlag;
	private Boolean noPublicEmpFlag;
	private Boolean noLegislativeMailFlag;

    private Integer afscmeOfficePk;
    private String  afscmeTitleNm;
   // if an officer, one of these should be returned, but not both
   // assume that the presentation layer will translate into 'Affiliate" or address type
   // for person addresses
    private Integer posAddrFromPersonPk;
    private Integer addressTypePk;
    private Integer posAddrFromOrgPk;
    private Collection theOfficerInfo;
    private boolean AffRestrictedAdmin = false;

    private String vduFlag; 

    /** Creates a new instance of MemberAffiliateInformationForm */
    public MemberAffiliateInformationForm() {
        vduFlag = null;
    }

    /** set the memberaffiliatedata retrieved from the database in the form */
    public void setMemberAffiliateData(MemberAffiliateData data) {
         this.setPersonPk(data.getPersonPk());
         this.setAffPk(data.getAffPk());
         this.setMbrStatus(data.getMbrStatus());
         this.setMbrType(data.getMbrType());
         this.setMbrNoLocal(data.getMbrNoLocal());
         this.setLostTimeLanguageFg(data.getLostTimeLanguageFg());
         this.setMbrCardSentDt(data.getMbrCardSentDt());
         this.setMbrDuesFrequency(data.getMbrDuesFrequency());
         this.setMbrDuesRate(data.getMbrDuesRate());
         this.setMbrDuesType(data.getMbrDuesType());
         this.setPrimaryInformationSource(data.getPrimaryInformationSource());
         this.setMbrJoinDt(data.getMbrJoinDt()); //Member Affiliate Information
         Calendar cal = DateUtil.getCalendar(data.getMbrJoinDt()); //Member Affiliate Information - Edit
         if (data.getMbrJoinDt() != null)
         {
            int month = cal.get(cal.MONTH);
            Integer year = new Integer(cal.get(cal.YEAR));
            this.setMonthJoined(new Integer(month + 1));
            this.setYearJoined(year.toString());
         } else {
            this.setMonthJoined(null);
            this.setYearJoined(null);
         }

         this.setMbrRetRenewalDuesFg(data.getMbrRetRenewalDuesFg());
         this.setMbrRetiredDt(data.getMbrRetiredDt()); //Member Affiliate Information
         Calendar cal2 = DateUtil.getCalendar(data.getMbrRetiredDt()); //Member Affiliate Information - Edit
         if (data.getMbrRetiredDt() != null)
         {
            int month = cal2.get(cal2.MONTH);
            Integer year = new Integer(cal2.get(cal2.YEAR));
            this.setMonthRetired(new Integer(month + 1));
            this.setYearRetired(year.toString());
         } else {
            this.setMonthRetired(new Integer("0"));
            this.setYearRetired(null);
         }

         this.setNoMailFg(data.getNoMailFg());
         this.setNoCardsFg(data.getNoCardsFg());
         this.setNoLegislativeMailFg(data.getNoLegislativeMailFg());
         this.setNoPublicEmpFg(data.getNoPublicEmpFg());
         this.setAfscmeOfficePk(data.getAfscmeOfficePk());
         this.setAfscmeTitleNm(data.getAfscmeTitleNm());
         this.setPosAddrFromPersonPk(data.getPosAddrFromPersonPk());
         this.setAddressTypePk(data.getAddressTypePk());
         this.setPosAddrFromOrgPk(data.getPosAddrFromOrgPk());
         this.setTheOfficerInfo(data.getTheOfficerInfo());
    }

   /** get the data from the form and place into a MemberAffiliateData bject so that the database can be updated */
    public MemberAffiliateData getMemberAffiliateData()
    {
        MemberAffiliateData data = new MemberAffiliateData();
        data.setPersonPk(this.getPersonPk());
        data.setAffPk(this.getAffPk());
        data.setMbrStatus(this.getMbrStatus());
        data.setMbrType(this.getMbrType());
        data.setMbrNoLocal(this.getMbrNoLocal());
        data.setMbrCardSentDt(this.getMbrCardSentDt());
        data.setPrimaryInformationSource(this.getPrimaryInformationSource());
        data.setLostTimeLanguageFg(this.getLostTimeLanguageFg());
        data.setMbrJoinDt(this.getMbrJoinDt());
         if (this.getMonthJoined() == null ||
            TextUtil.isEmptyOrSpaces(this.getYearJoined())
        ) {
         // join dt is not ever supposed to be null, if resset, should be caught by validate, but if not, dont just set it to null. . .
        } else {
            data.setMbrJoinDt(DateUtil.getTimestamp(
                                        this.getMonthJoined().intValue(),
                                        Integer.parseInt(this.getYearJoined()),
                                        false
                                   )
            );
        }
        data.setMbrDuesFrequency(this.getMbrDuesFrequency());
        data.setMbrDuesRate(this.getMbrDuesRate());
        data.setMbrDuesType(this.getMbrDuesType());
        data.setMbrRetiredDt(this.getMbrRetiredDt());
        if (this.getMonthRetired() == null ||
            TextUtil.isEmptyOrSpaces(this.getYearRetired())
        ) {
            data.setMbrRetiredDt(null);
        } else {
            data.setMbrRetiredDt(DateUtil.getTimestamp(
                                        this.getMonthRetired().intValue(),
                                        Integer.parseInt(this.getYearRetired()),
                                        false
                                   )
            );
        }
        data.setMbrRetRenewalDuesFg(this.getMbrRetRenewalDuesFg());
        data.setNoMailFg(this.getNoMailFg());
        data.setNoCardsFg(this.getNoCardsFg());
        data.setNoPublicEmpFg(this.getNoPublicEmpFg());
        data.setNoLegislativeMailFg(this.getNoLegislativeMailFg());
        data.setAfscmeOfficePk(this.getAfscmeOfficePk());
        data.setAfscmeTitleNm(this.getAfscmeTitleNm());
        data.setPosAddrFromPersonPk(this.getPosAddrFromPersonPk());
        data.setAddressTypePk(this.getAddressTypePk());
        data.setPosAddrFromOrgPk(this.getPosAddrFromOrgPk());
        data.setTheOfficerInfo(this.getTheOfficerInfo());
        return(data);
    }

    // toString or not to string
      public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");

        buf.append("personPk: " + personPk);
        buf.append(", affPk: " + affPk);
        buf.append(", mbrStatus: " + mbrStatus);
        buf.append(", mbrType: " + mbrType);
        buf.append(", mbrNoLocal: " + mbrNoLocal);
        buf.append(", primaryInformationSource: " + primaryInformationSource);
        buf.append(", lostTimeLanguageFg: " + lostTimeLanguageFg);
        buf.append(", mbrJoinDt: " + mbrJoinDt);
        buf.append(", monthJoined: " + monthJoined);
        buf.append(", yearJoined: " + yearJoined);
        buf.append(", mbrDuesFrequency: " + mbrDuesFrequency);
        buf.append(", mbrDuesRate: " + mbrDuesRate);
        buf.append(", mbrRetiredDt: " + mbrRetiredDt);
        buf.append(", monthRetired: " + monthRetired);
        buf.append(", yearRetired: " + yearRetired);
        buf.append(", mbrRetRenewalDuesFg: " + mbrRetRenewalDuesFg);
        buf.append(", noMailFg: " + noMailFg);
        buf.append(", noCardsFg: " + noCardsFg);
        buf.append(", noPublicEmpFg: " + noPublicEmpFg);
        buf.append(", noLegislativeMailFg: " + noLegislativeMailFg);
        buf.append(", afscmeOfficePk: " + afscmeOfficePk);
        buf.append(", afscmeTitleNm: " + afscmeTitleNm);
        buf.append(", posAddrFromPersonPk: " + posAddrFromPersonPk);
        buf.append(", addressTypePk: " + addressTypePk);
        buf.append(", posAddrFromOrgPk: " + posAddrFromOrgPk);
        buf.append(", theMemberOfficerTitleAddressInfo: " + theOfficerInfo);

        return buf.toString()+"]";
    }

     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {

        logger.debug("Inside MemberAffiliateInformationForm.validate() affType is : " + getAffType().trim()
        + " year retired is : " + getYearRetired() + " month retired is : "  + getMonthRetired() );

        ActionErrors errors = new ActionErrors();

        //Check Required fields
        if (TextUtil.isEmptyOrSpaces(this.getYearJoined()) || this.getMonthJoined() ==null) {
           errors.add("yearJoined", new ActionError("error.field.required.generic", "Year Joined"));
        }
        if (TextUtil.isEmptyOrSpaces(this.getYearRetired()) & !TextUtil.isEmpty(this.getMonthRetired())) {
           errors.add("yearRetired", new ActionError("error.field.required.generic", "Year Retired"));
        }
        if (TextUtil.isEmpty(this.getMonthRetired()) & !TextUtil.isEmptyOrSpaces(this.getYearRetired())) {
           errors.add("monthRetired", new ActionError("error.field.required.generic", "Month Retired"));
        }

        if (!TextUtil.isEmpty(this.getYearJoined()) && !TextUtil.isInt(this.getYearJoined().toString())) {
			errors.add("yearJoined", new ActionError("error.affiliate.charterdate.numeric"));
		}
        if (!TextUtil.isEmpty(this.getYearJoined())&& this.getYearJoined().toString().length() < 4) {
			errors.add("yearJoined", new ActionError("error.field.length.tooShort", "4"));
	    }

        if (!TextUtil.isEmpty(this.getYearRetired()) && !TextUtil.isInt(this.getYearRetired().toString())) {
		 	errors.add("yearRetired", new ActionError("error.affiliate.charterdate.numeric"));
		}
		if (!TextUtil.isEmpty(this.getYearRetired()) && this.getYearRetired().toString().length() < 4) {
		   	errors.add("yearRetired", new ActionError("error.field.length.tooShort", "4"));
        }

        return errors;
     }



    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }

    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }

    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
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

    /** Getter for property mbrStatus.
     * @return Value of property mbrStatus.
     *
     */
    public java.lang.Integer getMbrStatus() {
        return mbrStatus;
    }

    /** Setter for property mbrStatus.
     * @param mbrStatus New value of property mbrStatus.
     *
     */
    public void setMbrStatus(java.lang.Integer mbrStatus) {
        this.mbrStatus = mbrStatus;
    }

    /** Getter for property mbrType.
     * @return Value of property mbrType.
     *
     */
    public java.lang.Integer getMbrType() {
        return mbrType;
    }

    /** Setter for property mbrType.
     * @param mbrType New value of property mbrType.
     *
     */
    public void setMbrType(java.lang.Integer mbrType) {
        this.mbrType = mbrType;
    }

    /** Getter for property mbrNoLocal.
     * @return Value of property mbrNoLocal.
     *
     */
    public java.lang.String getMbrNoLocal() {
        return mbrNoLocal;
    }

    /** Setter for property mbrNoLocal.
     * @param mbrNoLocal New value of property mbrNoLocal.
     *
     */
    public void setMbrNoLocal(java.lang.String mbrNoLocal) {
        this.mbrNoLocal = mbrNoLocal;
    }

    /** Getter for property mbrCardSentDt.
     * @return Value of property mbrCardSentDt.
     *
     */
    public java.sql.Timestamp getMbrCardSentDt() {
        return mbrCardSentDt;
    }

    /** Setter for property mbrCardSentDt.
     * @param mbrCardSentDt New value of property mbrCardSentDt.
     *
     */
    public void setMbrCardSentDt(java.sql.Timestamp mbrCardSentDt) {
        this.mbrCardSentDt = mbrCardSentDt;
    }

    /** Getter for property primaryInformationSource.
     * @return Value of property primaryInformationSource.
     *
     */
    public java.lang.Integer getPrimaryInformationSource() {
        return primaryInformationSource;
    }

    /** Setter for property primaryInformationSource.
     * @param primaryInformationSource New value of property primaryInformationSource.
     *
     */
    public void setPrimaryInformationSource(java.lang.Integer primaryInformationSource) {
        this.primaryInformationSource = primaryInformationSource;
    }

    /** Getter for property lostTimeLanguageFg.
     * @return Value of property lostTimeLanguageFg.
     *
     */
    public java.lang.Boolean getLostTimeLanguageFg() {
        return lostTimeLanguageFg;
    }

    /** Setter for property lostTimeLanguageFg.
     * @param lostTimeLanguageFg New value of property lostTimeLanguageFg.
     *
     */
    public void setLostTimeLanguageFg(java.lang.Boolean lostTimeLanguageFg) {
        this.lostTimeLanguageFg = lostTimeLanguageFg;
    }

    /** Getter for property mbrJoinDt.
     * @return Value of property mbrJoinDt.
     *
     */
    public java.sql.Timestamp getMbrJoinDt() {
        return mbrJoinDt;
    }

    /** Setter for property mbrJoinDt.
     * @param mbrJoinDt New value of property mbrJoinDt.
     *
     */
    public void setMbrJoinDt(java.sql.Timestamp mbrJoinDt) {
        this.mbrJoinDt = mbrJoinDt;
    }

    /** Getter for property mbrDuesType.
     * @return Value of property mbrDuesType.
     *
     */
    public java.lang.Integer getMbrDuesType() {
        return mbrDuesType;
    }

    /** Setter for property mbrDuesType.
     * @param mbrDuesType New value of property mbrDuesType.
     *
     */
    public void setMbrDuesType(java.lang.Integer mbrDuesType) {
        if (mbrDuesType.intValue() == 0) this.mbrDuesType = null;
        else this.mbrDuesType = mbrDuesType;
    }

    /** Getter for property mbrDuesRate.
     * @return Value of property mbrDuesRate.
     *
     */
    public java.lang.Double getMbrDuesRate() {
        return mbrDuesRate;
    }

    /** Setter for property mbrDuesRate.
     * @param mbrDuesRate New value of property mbrDuesRate.
     *
     */
    public void setMbrDuesRate(java.lang.Double mbrDuesRate) {
        this.mbrDuesRate = mbrDuesRate;
    }

    /** Getter for property mbrDuesFrequency.
     * @return Value of property mbrDuesFrequency.
     *
     */
    public java.lang.Integer getMbrDuesFrequency() {
        return mbrDuesFrequency;
    }

    /** Setter for property mbrDuesFrequency.
     * @param mbrDuesFrequency New value of property mbrDuesFrequency.
     *
     */
    public void setMbrDuesFrequency(java.lang.Integer mbrDuesFrequency) {
        if (mbrDuesFrequency.intValue() == 0) this.mbrDuesFrequency = null;
        else this.mbrDuesFrequency = mbrDuesFrequency;
    }

    /** Getter for property mbrRetiredDt.
     * @return Value of property mbrRetiredDt.
     *
     */
    public java.sql.Timestamp getMbrRetiredDt() {
        return mbrRetiredDt;
    }

    /** Setter for property mbrRetiredDt.
     * @param mbrRetiredDt New value of property mbrRetiredDt.
     *
     */
    public void setMbrRetiredDt(java.sql.Timestamp mbrRetiredDt) {
        this.mbrRetiredDt = mbrRetiredDt;
    }

    /** Getter for property mbrRetRenewalDuesFg.
     * @return Value of property mbrRetRenewalDuesFg.
     *
     */
    public java.lang.Boolean getMbrRetRenewalDuesFg() {
        return mbrRetRenewalDuesFg;
    }

    /** Setter for property mbrRetRenewalDuesFg.
     * @param mbrRetRenewalDuesFg New value of property mbrRetRenewalDuesFg.
     *
     */
    public void setMbrRetRenewalDuesFg(java.lang.Boolean mbrRetRenewalDuesFg) {
        this.mbrRetRenewalDuesFg = mbrRetRenewalDuesFg;
    }

    /** Getter for property noCardsFg.
     * @return Value of property noCardsFg.
     *
     */
    public java.lang.Boolean getNoCardsFg() {
        return noCardsFg;
    }

    /** Setter for property noCardsFg.
     * @param noCardsFg New value of property noCardsFg.
     *
     */
    public void setNoCardsFg(java.lang.Boolean noCardsFg) {
        this.noCardsFg = noCardsFg;
    }

    /** Getter for property noMailFg.
     * @return Value of property noMailFg.
     *
     */
    public java.lang.Boolean getNoMailFg() {
        return noMailFg;
    }

    /** Setter for property noMailFg.
     * @param noMailFg New value of property noMailFg.
     *
     */
    public void setNoMailFg(java.lang.Boolean noMailFg) {
        this.noMailFg = noMailFg;
    }

    /** Getter for property noPublicEmpFg.
     * @return Value of property noPublicEmpFg.
     *
     */
    public java.lang.Boolean getNoPublicEmpFg() {
        return noPublicEmpFg;
    }

    /** Setter for property noPublicEmpFg.
     * @param noPublicEmpFg New value of property noPublicEmpFg.
     *
     */
    public void setNoPublicEmpFg(java.lang.Boolean noPublicEmpFg) {
        this.noPublicEmpFg = noPublicEmpFg;
    }

    /** Getter for property noCardsFlag.
     * @return Value of property noCardsFlag.
     *
     */
    public java.lang.Boolean getNoCardsFlag() {
        return noCardsFlag;
    }

    /** Setter for property noCardsFlag.
     * @param noCardsFlag New value of property noCardsFlag.
     *
     */
    public void setNoCardsFlag(java.lang.Boolean noCardsFlag) {
        this.noCardsFlag = noCardsFlag;
    }

    /** Getter for property noMailFlag.
     * @return Value of property noMailFlag.
     *
     */
    public java.lang.Boolean getNoMailFlag() {
        return noMailFlag;
    }

    /** Setter for property noMailFlag.
     * @param noMailFlag New value of property noMailFlag.
     *
     */
    public void setNoMailFlag(java.lang.Boolean noMailFlag) {
        this.noMailFlag = noMailFlag;
    }

    /** Getter for property noPublicEmpFlag.
     * @return Value of property noPublicEmpFlag.
     *
     */
    public java.lang.Boolean getNoPublicEmpFlag() {
        return noPublicEmpFlag;
    }

    /** Setter for property noPublicEmpFlag.
     * @param noPublicEmpFlag New value of property noPublicEmpFlag.
     *
     */
    public void setNoPublicEmpFlag(java.lang.Boolean noPublicEmpFlag) {
        this.noPublicEmpFlag = noPublicEmpFlag;
    }


    /** Getter for property afscmeOfficePk.
     * @return Value of property afscmeOfficePk.
     *
     */
    public java.lang.Integer getAfscmeOfficePk() {
        return afscmeOfficePk;
    }

    /** Setter for property afscmeOfficePk.
     * @param afscmeOfficePk New value of property afscmeOfficePk.
     *
     */
    public void setAfscmeOfficePk(java.lang.Integer afscmeOfficePk) {
        this.afscmeOfficePk = afscmeOfficePk;
    }

    /** Getter for property posAddrFromPersonPk.
     * @return Value of property posAddrFromPersonPk.
     *
     */
    public java.lang.Integer getPosAddrFromPersonPk() {
        return posAddrFromPersonPk;
    }

    /** Setter for property posAddrFromPersonPk.
     * @param posAddrFromPersonPk New value of property posAddrFromPersonPk.
     *
     */
    public void setPosAddrFromPersonPk(java.lang.Integer posAddrFromPersonPk) {
        this.posAddrFromPersonPk = posAddrFromPersonPk;
    }

    /** Getter for property addressTypePk.
     * @return Value of property addressTypePk.
     *
     */
    public java.lang.Integer getAddressTypePk() {
        return addressTypePk;
    }

    /** Setter for property addressTypePk.
     * @param addressTypePk New value of property addressTypePk.
     *
     */
    public void setAddressTypePk(java.lang.Integer addressTypePk) {
        this.addressTypePk = addressTypePk;
    }

    /** Getter for property posAddrFromOrgPk.
     * @return Value of property posAddrFromOrgPk.
     *
     */
    public java.lang.Integer getPosAddrFromOrgPk() {
        return posAddrFromOrgPk;
    }

    /** Setter for property posAddrFromOrgPk.
     * @param posAddrFromOrgPk New value of property posAddrFromOrgPk.
     *
     */
    public void setPosAddrFromOrgPk(java.lang.Integer posAddrFromOrgPk) {
        this.posAddrFromOrgPk = posAddrFromOrgPk;
    }

    /** Getter for property noLegislativeMailFg.
     * @return Value of property noLegislativeMailFg.
     *
     */
    public java.lang.Boolean getNoLegislativeMailFg() {
        return noLegislativeMailFg;
    }

    /** Setter for property noLegislativeMailFg.
     * @param noLegislativeMailFg New value of property noLegislativeMailFg.
     *
     */
    public void setNoLegislativeMailFg(java.lang.Boolean noLegislativeMailFg) {
        this.noLegislativeMailFg = noLegislativeMailFg;
    }

    /** Getter for property noLegislativeMailFlag.
     * @return Value of property noLegislativeMailFlag.
     *
     */
    public java.lang.Boolean getNoLegislativeMailFlag() {
        return noLegislativeMailFlag;
    }

    /** Setter for property noLegislativeMailFlag.
     * @param noLegislativeMailFlag New value of property noLegislativeMailFlag.
     *
     */
    public void setNoLegislativeMailFlag(java.lang.Boolean noLegislativeMailFlag) {
        this.noLegislativeMailFlag = noLegislativeMailFlag;
    }

    /** Getter for property afscmeTitleNm.
     * @return Value of property afscmeTitleNm.
     *
     */
    public java.lang.String getAfscmeTitleNm() {
        return afscmeTitleNm;
    }

    /** Setter for property afscmeTitleNm.
     * @param afscmeTitleNm New value of property afscmeTitleNm.
     *
     */
    public void setAfscmeTitleNm(java.lang.String afscmeTitleNm) {
        this.afscmeTitleNm = afscmeTitleNm;
    }

    /** Getter for property theOfficerInfo.
     * @return Value of property theOfficerInfo.
     *
     */
    public java.util.Collection getTheOfficerInfo() {
        return theOfficerInfo;
    }

    /** Setter for property theOfficerInfo.
     * @param theOfficerInfo New value of property theOfficerInfo.
     *
     */
    public void setTheOfficerInfo(java.util.Collection theOfficerInfo) {
        this.theOfficerInfo = theOfficerInfo;
    }

    /** Getter for property monthJoined.
     * @return Value of property monthJoined.
     *
     */
    public java.lang.Integer getMonthJoined() {
        return monthJoined;
    }

    /** Setter for property monthJoined.
     * @param monthJoined New value of property monthJoined.
     *
     */
    public void setMonthJoined(java.lang.Integer monthJoined) {
        this.monthJoined = monthJoined;
    }

    /** Getter for property yearJoined.
     * @return Value of property yearJoined.
     *
     */
    public java.lang.String getYearJoined() {
        return yearJoined;
    }

    /** Setter for property yearJoined.
     * @param yearJoined New value of property yearJoined.
     *
     */
    public void setYearJoined(java.lang.String yearJoined) {
        this.yearJoined = yearJoined;
    }

    /** Getter for property monthRetired.
     * @return Value of property monthRetired.
     *
     */
    public java.lang.Integer getMonthRetired() {
        return monthRetired;
    }

    /** Setter for property monthRetired.
     * @param monthRetired New value of property monthRetired.
     *
     */
    public void setMonthRetired(java.lang.Integer monthRetired) {
        if (monthRetired.intValue() == 0) this.monthRetired = null;
        else this.monthRetired = monthRetired;
    }

    /** Getter for property yearRetired.
     * @return Value of property yearRetired.
     *
     */
    public java.lang.String getYearRetired() {
        return yearRetired;
    }

    /** Setter for property yearRetired.
     * @param yearRetired New value of property yearRetired.
     *
     */
    public void setYearRetired(java.lang.String yearRetired) {
        this.yearRetired = yearRetired;
    }

    /** Getter for property affType.
     * @return Value of property affType.
     *
     */
    public java.lang.String getAffType() {
        return affType;
    }

    /** Setter for property affType.
     * @param affType New value of property affType.
     *
     */
    public void setAffType(java.lang.String affType) {
        this.affType = affType;
    }

    /** Getter for property AffRestrictedAdmin.
     * @return Value of property AffRestrictedAdmin.
     *
     */
    public boolean isAffRestrictedAdmin() {
        return AffRestrictedAdmin;
    }

    /** Setter for property AffRestrictedAdmin.
     * @param AffRestrictedAdmin New value of property AffRestrictedAdmin.
     *
     */
    public void setAffRestrictedAdmin(boolean AffRestrictedAdmin) {
        this.AffRestrictedAdmin = AffRestrictedAdmin;
    }

    /** Getter for property vduFlag.
     * @return Value of property vduFlag.
     *
     */
    public java.lang.String getVduFlag() {
        return vduFlag;
    }
    
    /** Setter for property vduFlag.
     * @param vduFlag New value of property vduFlag.
     *
     */
    public void setVduFlag(java.lang.String vduFlag) {
        this.vduFlag = vduFlag;
    }
    
}
