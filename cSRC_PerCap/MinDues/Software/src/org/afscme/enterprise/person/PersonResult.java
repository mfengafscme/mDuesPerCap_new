package org.afscme.enterprise.person;

import org.afscme.enterprise.affiliate.AffiliateIdentifier; 

/**
 * Contains data about a single row returned as the result of a person search 
 * searchPersons
 */
public class PersonResult 
{
    private Integer personPk;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private Character affType;
    private String affLocalSubChapter;
    private Character affCode;
    private Integer affPk;
    private String affStateNatType;
    private String affSubUnit;
    private String affCouncilRetireeChap;
    private String affAdminCouncil;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String zipPlus;
    private String province;
    private String country;
    private String ssn;
    private String userId;
    
    private String personNm;
    private String personAddr;
    private String personAddrPostalCode;
    
    protected AffiliateIdentifier theAffiliateIdentifier;
    
    /** Getter for property address1.
     * @return Value of property address1.
     *
     */
    public java.lang.String getAddress1() {
        return address1;
    }    
    
    /** Setter for property address1.
     * @param address1 New value of property address1.
     *
     */
    public void setAddress1(java.lang.String address1) {
        this.address1 = address1;
    }
    
    /** Getter for property address2.
     * @return Value of property address2.
     *
     */
    public java.lang.String getAddress2() {
        return address2;
    }
    
    /** Setter for property address2.
     * @param address2 New value of property address2.
     *
     */
    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }
    
    /** Getter for property affCouncilRetireeChap.
     * @return Value of property affCouncilRetireeChap.
     *
     */
    public java.lang.String getAffCouncilRetireeChap() {
        return affCouncilRetireeChap;
    }
    
    /** Setter for property affCouncilRetireeChap.
     * @param affCouncilRetireeChap New value of property affCouncilRetireeChap.
     *
     */
    public void setAffCouncilRetireeChap(java.lang.String affCouncilRetireeChap) {
        this.affCouncilRetireeChap = affCouncilRetireeChap;
    }
    
    /** Getter for property affLocalSubChapter.
     * @return Value of property affLocalSubChapter.
     *
     */
    public java.lang.String getAffLocalSubChapter() {
        return affLocalSubChapter;
    }
    
    /** Setter for property affLocalSubChapter.
     * @param affLocalSubChapter New value of property affLocalSubChapter.
     *
     */
    public void setAffLocalSubChapter(java.lang.String affLocalSubChapter) {
        this.affLocalSubChapter = affLocalSubChapter;
    }
    
    /** Getter for property affStateNatType.
     * @return Value of property affStateNatType.
     *
     */
    public java.lang.String getAffStateNatType() {
        return affStateNatType;
    }
    
    /** Setter for property affStateNatType.
     * @param affStateNatType New value of property affStateNatType.
     *
     */
    public void setAffStateNatType(java.lang.String affStateNatType) {
        this.affStateNatType = affStateNatType;
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
    
    /** Getter for property country.
     * @return Value of property country.
     *
     */
    public java.lang.String getCountry() {
        return country;
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     *
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }
    
    /** Getter for property firstNm.
     * @return Value of property firstNm.
     *
     */
    public java.lang.String getFirstNm() {
        return firstNm;
    }
    
    /** Setter for property firstNm.
     * @param firstNm New value of property firstNm.
     *
     */
    public void setFirstNm(java.lang.String firstNm) {
        this.firstNm = firstNm;
    }
    
    /** Getter for property lastNm.
     * @return Value of property lastNm.
     *
     */
    public java.lang.String getLastNm() {
        return lastNm;
    }
    
    /** Setter for property lastNm.
     * @param lastNm New value of property lastNm.
     *
     */
    public void setLastNm(java.lang.String lastNm) {
        this.lastNm = lastNm;
    }
    
    /** Getter for property middleNm.
     * @return Value of property middleNm.
     *
     */
    public java.lang.String getMiddleNm() {
        return middleNm;
    }
    
    /** Setter for property middleNm.
     * @param middleNm New value of property middleNm.
     *
     */
    public void setMiddleNm(java.lang.String middleNm) {
        this.middleNm = middleNm;
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
    
    /** Getter for property province.
     * @return Value of property province.
     *
     */
    public java.lang.String getProvince() {
        return province;
    }
    
    /** Setter for property province.
     * @param province New value of property province.
     *
     */
    public void setProvince(java.lang.String province) {
        this.province = province;
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
    
    /** Getter for property userId.
     * @return Value of property userId.
     *
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /** Setter for property userId.
     * @param userId New value of property userId.
     *
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
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
    
    /** Getter for property zipPlus.
     * @return Value of property zipPlus.
     *
     */
    public java.lang.String getZipPlus() {
        return zipPlus;
    }
    
    /** Setter for property zipPlus.
     * @param zipPlus New value of property zipPlus.
     *
     */
    public void setZipPlus(java.lang.String zipPlus) {
        this.zipPlus = zipPlus;
    }
    
    /** Getter for property affSubUnit.
     * @return Value of property affSubUnit.
     *
     */
    public java.lang.String getAffSubUnit() {
        return affSubUnit;
    }
    
    /** Setter for property affSubUnit.
     * @param affSubUnit New value of property affSubUnit.
     *
     */
    public void setAffSubUnit(java.lang.String affSubUnit) {
        this.affSubUnit = affSubUnit;
    }
    
    /** Getter for property affCode.
     * @return Value of property affCode.
     *
     */
    public Character getAffCode() {
        return affCode;
    }
    
    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffCode(Character affCode) {
        this.affCode = affCode;
    }
    
    /** Getter for property affType.
     * @return Value of property affType.
     *
     */
    public Character getAffType() {
        return affType;
    }
    
    /** Setter for property affType.
     * @param affType New value of property affType.
     *
     */
    public void setAffType(Character affType) {
        this.affType = affType;
    }
    
    /** Getter for property personAddr.
     * @return Value of property personAddr.
     *
     */
    public java.lang.String getPersonAddr() {
        return personAddr;
    }
    
    /** Setter for property personAddr.
     * @param personAddr New value of property personAddr.
     *
     */
    public void setPersonAddr(java.lang.String personAddr) {
        this.personAddr = personAddr;
    }
    
    /** Getter for property personAddrPostalCode.
     * @return Value of property personAddrPostalCode.
     *
     */
    public java.lang.String getPersonAddrPostalCode() {
        return personAddrPostalCode;
    }
    
    /** Setter for property personAddrPostalCode.
     * @param personAddrPostalCode New value of property personAddrPostalCode.
     *
     */
    public void setPersonAddrPostalCode(java.lang.String personAddrPostalCode) {
        this.personAddrPostalCode = personAddrPostalCode;
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
    
    /** Getter for property affAdminCouncil.
     * @return Value of property affAdminCouncil.
     *
     */
    public java.lang.String getAffAdminCouncil() {
        return affAdminCouncil;
    }
    
    /** Setter for property affAdminCouncil.
     * @param affAdminCouncil New value of property affAdminCouncil.
     *
     */
    public void setAffAdminCouncil(java.lang.String affAdminCouncil) {
        this.affAdminCouncil = affAdminCouncil;
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
    
}
