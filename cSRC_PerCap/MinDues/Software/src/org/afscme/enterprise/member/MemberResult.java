

package org.afscme.enterprise.member;

import org.afscme.enterprise.affiliate.AffiliateIdentifier; 
import java.sql.Timestamp;

/**
 * Contains data that is the result of a member search and is tuned to support the 
 * presentation layer requirements for displaying the results of a member search
 */
public class MemberResult 
{
    // person fields
    protected Integer personPk;
    protected String prefixNm;
    protected String personNm; // a concatenation of last, suffix, first, middle, 
//    protected String firstNm;
//    protected String middleNm;
//    protected String lastNm;
//    protected String suffixNm;
    protected String nickNm;
    protected String ssn;
    protected Boolean validSsn;
    protected String altMailingNm;
    
    // SMA address fields
//    protected String addr1;
//    protected String addr2; // replaced by address
    protected String address;
    protected String city;
    protected String state;
    protected String zipCode;
   
    protected Integer addrUpdatedByInt; // maybe temp, actually need userid for sorting purposes
    protected java.sql.Timestamp addrUpdatedDt;
//  not needed according to UI document, member result page
//    protected String zipPlus;
//    protected String county;
//    protected String province;
//    protected String country;
    
    // phone and email fields, may need to support multiple, right now just primary email
    // and Affiliate Relations Home Address. 
    protected String countryCode;
    protected String areaCode;
    protected String phoneNumber;
    protected String personEmailAddr;
    
    protected String primaryInformationSource;
    protected String mbrType;
    protected String mbrStatus;
    protected java.sql.Timestamp mbrCardSentDt;
    protected String lstModUserPk;
    protected java.sql.Timestamp lstModDt;
    protected Boolean noCardsFg;
    protected Boolean noMailFg;
    protected Boolean noPublicEmpFg;
    protected Boolean noLegislativeMailFg;
   
    protected AffiliateIdentifier theAffiliateIdentifier;
    
    /** Getter for property prefixNm.
     * @return Value of property prefixNm.
     *
     */
    public java.lang.String getPrefixNm() {
        return prefixNm;
    }
    
    /** Setter for property prefixNm.
     * @param prefixNm New value of property prefixNm.
     *
     */
    public void setPrefixNm(java.lang.String prefixNm) {
        this.prefixNm = prefixNm;
    }
    
    /** Getter for property city.
     * @return Value of property city.
     *
     */
    public java.lang.String getCity() {
        return city;
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     *
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     *
     */
    public java.lang.String getState() {
        return state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     *
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }
    
    /** Getter for property zipCode.
     * @return Value of property zipCode.
     *
     */
    public java.lang.String getZipCode() {
        return zipCode;
    }
    
    /** Setter for property zipCode.
     * @param zipCode New value of property zipCode.
     *
     */
    public void setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
    }
   
    /** Getter for property nickNm.
     * @return Value of property nickNm.
     *
     */
    public java.lang.String getNickNm() {
        return nickNm;
    }
    
    /** Setter for property nickNm.
     * @param nickNm New value of property nickNm.
     *
     */
    public void setNickNm(java.lang.String nickNm) {
        this.nickNm = nickNm;
    }
    
    /** Getter for property altMailingNm.
     * @return Value of property altMailingNm.
     *
     */
    public java.lang.String getAltMailingNm() {
        return altMailingNm;
    }
    
    /** Setter for property altMailingNm.
     * @param altMailingNm New value of property altMailingNm.
     *
     */
    public void setAltMailingNm(java.lang.String altMailingNm) {
        this.altMailingNm = altMailingNm;
    }
    
    /** Getter for property ssn.
     * @return Value of property ssn.
     *
     */
    public java.lang.String getSsn() {
        return ssn;
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     *
     */
    public void setSsn(java.lang.String ssn) {
        this.ssn = ssn;
    }
    
    /** Getter for property countryCode.
     * @return Value of property countryCode.
     *
     */
    public java.lang.String getCountryCode() {
        return countryCode;
    }
    
    /** Setter for property countryCode.
     * @param countryCode New value of property countryCode.
     *
     */
    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }
    
    /** Getter for property areaCode.
     * @return Value of property areaCode.
     *
     */
    public java.lang.String getAreaCode() {
        return areaCode;
    }
    
    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     *
     */
    public void setAreaCode(java.lang.String areaCode) {
        this.areaCode = areaCode;
    }
    
    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     *
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }
    
    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     *
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /** Getter for property personEmailAddr.
     * @return Value of property personEmailAddr.
     *
     */
    public java.lang.String getPersonEmailAddr() {
        return personEmailAddr;
    }
    
    /** Setter for property personEmailAddr.
     * @param personEmailAddr New value of property personEmailAddr.
     *
     */
    public void setPersonEmailAddr(java.lang.String personEmailAddr) {
        this.personEmailAddr = personEmailAddr;
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
    
    /** Getter for property lstModUserPk.
     * @return Value of property lstModUserPk.
     *
     */
    public java.lang.String getLstModUserPk() {
        return lstModUserPk;
    }
    
    /** Setter for property lstModUserPk.
     * @param lstModUserPk New value of property lstModUserPk.
     *
     */
    public void setLstModUserPk(java.lang.String lstModUserPk) {
        this.lstModUserPk = lstModUserPk;
    }
    
    /** Getter for property lstModDt.
     * @return Value of property lstModDt.
     *
     */
    public java.sql.Timestamp getLstModDt() {
        return lstModDt;
    }
    
    /** Setter for property lstModDt.
     * @param lstModDt New value of property lstModDt.
     *
     */
    public void setLstModDt(java.sql.Timestamp lstModDt) {
        this.lstModDt = lstModDt;
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
    
    /** Getter for property theAffiliateIdentifier.
     * @return Value of property theAffiliateIdentifier.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getTheAffiliateIdentifier() {
        return theAffiliateIdentifier;
    }
    
    /** Setter for property theAffiliateIdentifier.
     * @param theAffiliateIdentifier New value of property theAffiliateIdentifier.
     *
     */
    public void setTheAffiliateIdentifier(org.afscme.enterprise.affiliate.AffiliateIdentifier theAffiliateIdentifier) {
        this.theAffiliateIdentifier = theAffiliateIdentifier;
    }
    
    /** Getter for property mbrType.
     * @return Value of property mbrType.
     *
     */
    public java.lang.String getMbrType() {
        return mbrType;
    }    
  
    /** Setter for property mbrType.
     * @param mbrType New value of property mbrType.
     *
     */
    public void setMbrType(java.lang.String mbrType) {
        this.mbrType = mbrType;
    }    
    
    /** Getter for property mbrStatus.
     * @return Value of property mbrStatus.
     *
     */
    public java.lang.String getMbrStatus() {
        return mbrStatus;
    }
    
    /** Setter for property mbrStatus.
     * @param mbrStatus New value of property mbrStatus.
     *
     */
    public void setMbrStatus(java.lang.String mbrStatus) {
        this.mbrStatus = mbrStatus;
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
    
    /** Getter for property validSsn.
     * @return Value of property validSsn.
     *
     */
    public java.lang.Boolean getValidSsn() {
        return validSsn;
    }
    
    /** Setter for property validSsn.
     * @param validSsn New value of property validSsn.
     *
     */
    public void setValidSsn(java.lang.Boolean validSsn) {
        this.validSsn = validSsn;
    }
    
    /** Getter for property primaryInformationSource.
     * @return Value of property primaryInformationSource.
     *
     */
    public java.lang.String getPrimaryInformationSource() {
        return primaryInformationSource;
    }
    
    /** Setter for property primaryInformationSource.
     * @param primaryInformationSource New value of property primaryInformationSource.
     *
     */
    public void setPrimaryInformationSource(java.lang.String primaryInformationSource) {
        this.primaryInformationSource = primaryInformationSource;
    }
   
  
    /** Getter for property addrUpdatedDt.
     * @return Value of property addrUpdatedDt.
     *
     */
    public java.sql.Timestamp getAddrUpdatedDt() {
        return addrUpdatedDt;
    }
    
    /** Setter for property addrUpdatedDt.
     * @param addrUpdatedDt New value of property addrUpdatedDt.
     *
     */
    public void setAddrUpdatedDt(java.sql.Timestamp addrUpdatedDt) {
        this.addrUpdatedDt = addrUpdatedDt;
    }
    
     
    /** Getter for property addrUpdatedByInt.
     * @return Value of property addrUpdatedByInt.
     *
     */
    public java.lang.Integer getAddrUpdatedByInt() {
        return addrUpdatedByInt;
    }
    
    /** Setter for property addrUpdatedByInt.
     * @param addrUpdatedByInt New value of property addrUpdatedByInt.
     *
     */
    public void setAddrUpdatedByInt(java.lang.Integer addrUpdatedByInt) {
        this.addrUpdatedByInt = addrUpdatedByInt;
    }
    
    /** Getter for property address.
     * @return Value of property address.
     *
     */
    public java.lang.String getAddress() {
        return address;
    }
    
    /** Setter for property address.
     * @param address New value of property address.
     *
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }
    
    /** Getter for property personNm.
     * @return Value of property personNm.
     *
     */
    public java.lang.String getPersonNm() {
        return personNm;
    }
    
    /** Setter for property personNm.
     * @param personNm New value of property personNm.
     *
     */
    public void setPersonNm(java.lang.String personNm) {
        this.personNm = personNm;
    }
    
}
