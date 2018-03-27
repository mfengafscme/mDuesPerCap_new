

package org.afscme.enterprise.member;

import org.afscme.enterprise.common.RecordData;


/**
 * Contains data that is unique to a members involvment in a specific affiliate
 */
public class MemberAffiliateData 
{
    protected Integer personPk;
    protected Integer affPk;
    protected Integer mbrStatus;
    protected Integer mbrType;
    protected String mbrNoLocal;
    protected java.sql.Timestamp mbrCardSentDt;
    protected Integer primaryInformationSource;
    protected Boolean lostTimeLanguageFg;
    protected java.sql.Timestamp mbrJoinDt;
    protected Integer mbrDuesType;
    protected Double mbrDuesRate;
    protected Integer mbrDuesFrequency;
    protected java.sql.Timestamp mbrRetiredDt;
    protected Boolean mbrRetRenewalDuesFg;
    protected Boolean noCardsFg;
    protected Boolean noMailFg;
    protected Boolean noPublicEmpFg;
    protected Boolean noLegislativeMailFg;
    protected Integer afscmeOfficePk;
    protected String  afscmeTitleNm;
   // if an officer, one of these should be returned, but not both
   // assume that the presentation layer will translate into 'Affiliate" or address type
   // for person addresses 
    protected Integer posAddrFromPersonPk;
    protected Integer addressTypePk;
    protected Integer posAddrFromOrgPk;
    protected java.util.Collection theOfficerInfo;	// Collection of MemberOfficerTitleAddressInfo objects 
    protected boolean AffRestrictedAdmin = false; // needed for Officer Title & address display rules
   
    
    protected RecordData theRecordData;
    
    /** Getter for property personPK.
     * @return Value of property personPK.
     *
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPK.
     * @param personPK New value of property personPK.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property affPK.
     * @return Value of property affPK.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPK.
     * @param affPK New value of property affPK.
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
        this.mbrDuesType = mbrDuesType;
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
        this.mbrDuesFrequency = mbrDuesFrequency;
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
    
    /** Getter for property theRecordData.
     * @return Value of property theRecordData.
     *
     */
    public RecordData getTheRecordData() {
        return theRecordData;
    }
    
    /** Setter for property theRecordData.
     * @param theRecordData New value of property theRecordData.
     *
     */
    public void setTheRecordData(RecordData theRecordData) {
        this.theRecordData = theRecordData;
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
    
}
