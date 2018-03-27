package org.afscme.enterprise.person;

import java.sql.Timestamp;

/**
 * Contains data used to query for persons. Includes information about which 
 * result fields are desired, sort information and pagining information.
 */
public class PersonCriteria 
{
    private Integer prefixNm;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private Integer suffixNm;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String zipPlus;
    private String county;
    private String province;
    private Integer country;
    
    private String addrUpdatedBy;    // added by GRD for member search 
    private Timestamp addrUpdatedDt;  // added by GRD for member search - convert to timestamp on get
        
    private String nickNm;
    private String altMailingNm;
    private String ssn;
    private int markedForDeletionFg;  // 0 false, 1, true, 2 means both (omit from search where clause)
   //Persona values: 1 is AFSCME Staff, 2 is Affiliate Staff, 
   //3 is Other, 4 is Member, 5 is PAC Contributor, 6 is Organization Associate, 
   //0 means all persona types
    private int personaCode;
    
    // private Boolean validSsn;        // added by GRD for member search - issues with html:radio tag and three values
    private int validSsn;    // 0 false, 1, true, 2 means both (omit from search where clause)
    
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private String personEmailAddr;
    private String persona;
    private Timestamp dob;
    private String userId;
    private Integer personPk;
    
    //
    // Sort Directions
    //
    public static final int SORT_ASCENDING = 1;
    public static final int SORT_DESCENDING = -1;
    public static final int SORT_NONE = 0;

    //
    // Fields
    //
    public static final int FIELD_NONE = 0;
    public static final int FIELD_NAME = 1;
    public static final int FIELD_ADDR = 2;
    public static final int FIELD_CITY = 3;
    public static final int FIELD_STATE = 4;
    public static final int FIELD_POSTALCODE = 5;
    public static final int FIELD_SSN = 6;
    public static final int FIELD_USERID = 7;
    public static final int FIELD_AFF_TYPE = 8;
    public static final int FIELD_AFF_LOCAL = 9;
    public static final int FIELD_AFF_STATE = 10;
    public static final int FIELD_AFF_SUBUNIT = 11;
    public static final int FIELD_AFF_COUNCIL = 12;
    public static final int[] SORT_FIELD_IDS = new int[] {
        FIELD_NAME, FIELD_ADDR, FIELD_CITY, FIELD_STATE, FIELD_POSTALCODE, FIELD_SSN, FIELD_USERID, FIELD_AFF_TYPE, FIELD_AFF_LOCAL, FIELD_AFF_STATE, FIELD_AFF_SUBUNIT, FIELD_AFF_COUNCIL };

    /**
     * The field to sort on.  Must be one of the values in this class's FIELD_XXX
     * constants.
     */
    protected int sortField = FIELD_NONE;

    /**
     * The sort order.  Must be one of the values in this class's SORT_XXX constants.
     */
    protected int sortOrder = SORT_NONE;

    /**
     * The page to search for.  e.g, if pageSize is 10 and the search is for page 3,
     * records 20-29 will be returned.
     */
    protected int page = 0;

    /**
     * The size of the result page to return.
     */
    protected int pageSize;

    protected String orderBy;
    protected int ordering; 
    

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
    public java.lang.Integer getCountry() {
        return country;
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     *
     */
    public void setCountry(java.lang.Integer country) {
        this.country = country;
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
    
    /** Getter for property county.
     * @return Value of property county.
     *
     */
    public java.lang.String getCounty() {
        return county;
    }
    
    /** Setter for property county.
     * @param county New value of property county.
     *
     */
    public void setCounty(java.lang.String county) {
        this.county = county;
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
    
    /** Getter for property persona.
     * @return Value of property persona.
     *
     */
    public java.lang.String getPersona() {
        return persona;
    }
    
    /** Setter for property persona.
     * @param persona New value of property persona.
     *
     */
    public void setPersona(java.lang.String persona) {
        this.persona = persona;
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
    
    /** Getter for property prefixNm.
     * @return Value of property prefixNm.
     *
     */
    public java.lang.Integer getPrefixNm() {
       if (this.prefixNm != null)
             if (this.prefixNm.equals(new Integer(0)))
                  return null;
      return prefixNm;
    }
    
    /** Setter for property prefixNm.
     * @param prefixNm New value of property prefixNm.
     *
     */
    public void setPrefixNm(java.lang.Integer prefixNm) {
        this.prefixNm = prefixNm;
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
    
    /** Getter for property suffixNm.
     * @return Value of property suffixNm.
     *
     */
    public java.lang.Integer getSuffixNm() {
        if (this.suffixNm != null)
             if (this.suffixNm.equals(new Integer(0)))
                  return null;
        return this.suffixNm;
    }
    
    /** Setter for property suffixNm.
     * @param suffixNm New value of property suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer suffixNm) {
        this.suffixNm = suffixNm;
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
    
    /** Getter for property dob.
     * @return Value of property dob.
     *
     */
    public Timestamp getDob() {
        return dob;
    }
    
    /** Setter for property dob.
     * @param dob New value of property dob.
     *
     */
    public void setDob(Timestamp dob) {
        this.dob = dob;
    }
    
    /** Getter for property validSsn.
     * @return Value of property validSsn.
     *
     */
    public int getValidSsn() {
        return validSsn;
    }
    
    /** Setter for property validSsn.
     * @param validSsn New value of property validSsn.
     *
     */
    public void setValidSsn(int validSsn) {
        this.validSsn = validSsn;
    }
    
    /** Getter for property addrUpdatedDt.
     * @return Value of property addrUpdatedDt.
     *
     */
    public Timestamp getAddrUpdatedDt() {
        return addrUpdatedDt;
    }
    
    /** Setter for property addrUpdatedDt.
     * @param addrUpdatedDt New value of property addrUpdatedDt.
     *
     */
    public void setAddrUpdatedDt(Timestamp addrUpdatedDt) {
        this.addrUpdatedDt = addrUpdatedDt;
    }
    
    /** Getter for property addrUpdatedBy.
     * @return Value of property addrUpdatedBy.
     *
     */
    public String getAddrUpdatedBy() {
        return addrUpdatedBy;
    }
    
    /** Setter for property addrUpdatedBy.
     * @param addrUpdatedBy New value of property addrUpdatedBy.
     *
     */
    public void setAddrUpdatedBy(String addrUpdatedBy) {
        this.addrUpdatedBy = addrUpdatedBy;
    }
    
    /** Getter for property page.
     * @return Value of property page.
     *
     */
    public int getPage() {
        return page;
    }
    
    /** Setter for property page.
     * @param page New value of property page.
     *
     */
    public void setPage(int page) {
        this.page = page;
    }
    
    /** Getter for property pageSize.
     * @return Value of property pageSize.
     *
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /** Setter for property pageSize.
     * @param pageSize New value of property pageSize.
     *
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /** Getter for property sortField.
     * @return Value of property sortField.
     *
     */
    public int getSortField() {
        return sortField;
    }
    
    /** Setter for property sortField.
     * @param sortField New value of property sortField.
     *
     */
    public void setSortField(int sortField) {
        this.sortField = sortField;
    }
    
    /** Getter for property sortOrder.
     * @return Value of property sortOrder.
     *
     */
    public int getSortOrder() {
        return sortOrder;
    }
    
    /** Setter for property sortOrder.
     * @param sortOrder New value of property sortOrder.
     *
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    /** Getter for property orderBy.
     * @return Value of property orderBy.
     *
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }
    
    /** Setter for property orderBy.
     * @param orderBy New value of property orderBy.
     *
     */
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;
    }
    
    /** Getter for property ordering.
     * @return Value of property ordering.
     *
     */
    public int getOrdering() {
        return ordering;
    }
    
    /** Setter for property ordering.
     * @param ordering New value of property ordering.
     *
     */
    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }
    
    /** Getter for property markedForDeletionFg.
     * @return Value of property markedForDeletionFg.
     *
     */
    public int getMarkedForDeletionFg() {
        return markedForDeletionFg;
    }
    
    /** Setter for property markedForDeletionFg.
     * @param markedForDeletionFg New value of property markedForDeletionFg.
     *
     */
    public void setMarkedForDeletionFg(int markedForDeletionFg) {
        this.markedForDeletionFg = markedForDeletionFg;
    }
    
    /** Getter for property personaCode.
     * @return Value of property personaCode.
     *
     */
    public int getPersonaCode() {
        return personaCode;
    }
    
    /** Setter for property personaCode.
     * @param personaCode New value of property personaCode.
     *
     */
    public void setPersonaCode(int personaCode) {
        this.personaCode = personaCode;
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
    
}
