package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;

/**
 * Contains data used to query for affiliates.  Includes information about which
 * result fields are desired, sort information and pagining information.
 */
public class AffiliateCriteria {

    //public static final String SORT_BY_ABBR_NAME       = "aff_abbreviated_nm";
    //public static final String SORT_BY_AFF_ID_TYPE     = "aff_type";
    //public static final String SORT_BY_AFF_ID_LOCAL    = "aff_localSubChapter";
    //public static final String SORT_BY_AFF_ID_STATE    = "aff_stateNat_type";
    //public static final String SORT_BY_AFF_ID_SUB_UNIT = "aff_subUnit";
    //public static final String SORT_BY_AFF_ID_COUNCIL  = "aff_councilRetiree_chap";

    public static final String SORT_BY_ABBR_NAME       = "curr_employer_name";
    public static final String SORT_BY_AFF_ID_TYPE     = "type";
    public static final String SORT_BY_AFF_ID_LOCAL    = "local";
    public static final String SORT_BY_AFF_ID_STATE    = "state";
    public static final String SORT_BY_AFF_ID_SUB_UNIT = "chapter";
    public static final String SORT_BY_AFF_ID_COUNCIL  = "council";

    private Character affiliateIdType;
    private Character affiliateIdCode;
    private String affiliateIdLocal;
    private String affiliateIdState;
    private String affiliateIdSubUnit;
    private String affiliateIdCouncil;
    private Boolean includeSubUnits;

    private String employerNm;

    private Boolean includeInactive;
    private Integer affiliateStatusCodePk;
    private Integer afscmeLegislativeDistrictCodePk;
    private Integer afscmeRegionCodePk;
    private Boolean multipleEmployers;
    private Integer employerSectorCodePk;
    private Boolean allowSubLocals;
    private Character newAffiliateIdentifierSourceType;
    private Character newAffiliateIdentifierSourceCode;
    private String newAffiliateIdentifierSourceLocal;
    private String newAffiliateIdentifierSourceState;
    private String newAffiliateIdentifierSourceSubUnit;
    private String newAffiliateIdentifierSourceCouncil;
    private Boolean multipleOffices;
    private String website;
    private String locationAddressAttention;
    private String locationAddress1;
    private String locationAddress2;
    private String locationAddressCity;
    private String locationAddressState;
    private String locationAddressZip;
    private String locationAddressZip4;
    private String locationAddressCounty;
    private String locationAddressProvince;
    private Integer locationAddressCountryCodePk;
    private Timestamp locationAddressUpdatedBeginDate;
    private Timestamp locationAddressUpdatedEndDate;
    private String locationAddressUpdatedByUserID;
    private String locationPhoneOfficeCountryCode;
    private String locationPhoneOfficeAreaCode;
    private String locationPhoneOfficeNumber;
    private String locationPhoneFaxCountryCode;
    private String locationPhoneFaxAreaCode;
    private String locationPhoneFaxNumber;
    private Integer parentAffFk;

    // The remaining attributes are for pagination and sorting only
    private int page;
    private int pageSize;
    private String orderBy;
    private int ordering;

    public AffiliateCriteria() {
        this.affiliateIdType = null;
        this.affiliateIdCode = null;
        this.affiliateIdLocal = null;
        this.affiliateIdState = null;
        this.affiliateIdSubUnit = null;
        this.affiliateIdCouncil = null;
        this.employerNm = null;
        this.includeSubUnits = null;
        this.includeInactive = null;
        this.affiliateStatusCodePk = null;
        this.afscmeLegislativeDistrictCodePk = null;
        this.afscmeRegionCodePk = null;
        this.multipleEmployers = null;
        this.employerSectorCodePk = null;
        this.allowSubLocals = null;
        this.newAffiliateIdentifierSourceType = null;
        this.newAffiliateIdentifierSourceCode = null;
        this.newAffiliateIdentifierSourceLocal = null;
        this.newAffiliateIdentifierSourceState = null;
        this.newAffiliateIdentifierSourceSubUnit = null;
        this.newAffiliateIdentifierSourceCouncil = null;
        this.multipleOffices = null;
        this.website = null;
        this.locationAddressAttention = null;
        this.locationAddress1 = null;
        this.locationAddress2 = null;
        this.locationAddressCity = null;
        this.locationAddressState = null;
        this.locationAddressZip = null;
        this.locationAddressZip4 = null;
        this.locationAddressCounty = null;
        this.locationAddressProvince = null;
        this.locationAddressCountryCodePk = null;
        this.locationAddressUpdatedBeginDate = null;
        this.locationAddressUpdatedEndDate = null;
        this.locationAddressUpdatedByUserID = null;
        this.locationPhoneOfficeCountryCode = null;
        this.locationPhoneOfficeAreaCode = null;
        this.locationPhoneOfficeNumber = null;
        this.locationPhoneFaxCountryCode = null;
        this.locationPhoneFaxAreaCode = null;
        this.locationPhoneFaxNumber = null;

        this.page = 0;
        this.pageSize = 10000; // Defaults to some high number for back end method use. This is overriden by the ActionForm class for UI use.
        this.orderBy = null;
        this.ordering = 0;
    }

// Getter and Setter Methods...

    /** Getter for property affiliateStatusCodePk.
     * @return Value of property affiliateStatusCodePk.
     *
     */
    public Integer getAffiliateStatusCodePk() {
        return affiliateStatusCodePk;
    }

    /** Setter for property affiliateStatusCodePk.
     * @param affiliateStatusCodePk New value of property affiliateStatusCodePk.
     *
     */
    public void setAffiliateStatusCodePk(Integer affiliateStatusCodePk) {
        this.affiliateStatusCodePk = affiliateStatusCodePk;
    }

    /** Getter for property afscmeLegislativeDistrictCodePk.
     * @return Value of property afscmeLegislativeDistrictCodePk.
     *
     */
    public Integer getAfscmeLegislativeDistrictCodePk() {
        return afscmeLegislativeDistrictCodePk;
    }

    /** Setter for property afscmeLegislativeDistrictCodePk.
     * @param afscmeLegislativeDistrictCodePk New value of property afscmeLegislativeDistrictCodePk.
     *
     */
    public void setAfscmeLegislativeDistrictCodePk(Integer afscmeLegislativeDistrictCodePk) {
        this.afscmeLegislativeDistrictCodePk = afscmeLegislativeDistrictCodePk;
    }

    /** Getter for property afscmeRegionCodePk.
     * @return Value of property afscmeRegionCodePk.
     *
     */
    public Integer getAfscmeRegionCodePk() {
        return afscmeRegionCodePk;
    }

    /** Setter for property afscmeRegionCodePk.
     * @param afscmeRegionCodePk New value of property afscmeRegionCodePk.
     *
     */
    public void setAfscmeRegionCodePk(Integer afscmeRegionCodePk) {
        this.afscmeRegionCodePk = afscmeRegionCodePk;
    }

    /** Getter for property allowSubLocals.
     * @return Value of property allowSubLocals.
     *
     */
    public Boolean getAllowSubLocals() {
        return allowSubLocals;
    }

    /** Setter for property allowSubLocals.
     * @param allowSubLocals New value of property allowSubLocals.
     *
     */
    public void setAllowSubLocals(Boolean allowSubLocals) {
        this.allowSubLocals = allowSubLocals;
    }

    /** Getter for property employerSectorCodePk.
     * @return Value of property employerSectorCodePk.
     *
     */
    public Integer getEmployerSectorCodePk() {
        return employerSectorCodePk;
    }

    /** Setter for property employerSectorCodePk.
     * @param employerSectorCodePk New value of property employerSectorCodePk.
     *
     */
    public void setEmployerSectorCodePk(Integer employerSectorCodePk) {
        this.employerSectorCodePk = employerSectorCodePk;
    }

    /** Getter for property includeSubUnits.
     * @return Value of property includeSubUnits.
     *
     */
    public Boolean getIncludeSubUnits() {
        return includeSubUnits;
    }

    /** Setter for property includeSubUnits.
     * @param includeSubUnits New value of property includeSubUnits.
     *
     */
    public void setIncludeSubUnits(Boolean includeSubUnits) {
        this.includeSubUnits = includeSubUnits;
    }

    /** Getter for property includeInactive.
     * @return Value of property includeInactive.
     *
     */
    public Boolean getIncludeInactive() {
        return includeInactive;
    }

    /** Setter for property includeInactive.
     * @param includeInactive New value of property includeInactive.
     *
     */
    public void setIncludeInactive(Boolean includeInactive) {
        this.includeInactive = includeInactive;
    }

    /** Getter for property locationAddress1.
     * @return Value of property locationAddress1.
     *
     */
    public String getLocationAddress1() {
        return locationAddress1;
    }

    /** Setter for property locationAddress1.
     * @param locationAddress1 New value of property locationAddress1.
     *
     */
    public void setLocationAddress1(String locationAddress1) {
        this.locationAddress1 = locationAddress1;
    }

    /** Getter for property locationAddress2.
     * @return Value of property locationAddress2.
     *
     */
    public String getLocationAddress2() {
        return locationAddress2;
    }

    /** Setter for property locationAddress2.
     * @param locationAddress2 New value of property locationAddress2.
     *
     */
    public void setLocationAddress2(String locationAddress2) {
        this.locationAddress2 = locationAddress2;
    }

    /** Getter for property locationAddressAttention.
     * @return Value of property locationAddressAttention.
     *
     */
    public String getLocationAddressAttention() {
        return locationAddressAttention;
    }

    /** Setter for property locationAddressAttention.
     * @param locationAddressAttention New value of property locationAddressAttention.
     *
     */
    public void setLocationAddressAttention(String locationAddressAttention) {
        this.locationAddressAttention = locationAddressAttention;
    }

    /** Getter for property locationAddressCity.
     * @return Value of property locationAddressCity.
     *
     */
    public String getLocationAddressCity() {
        return locationAddressCity;
    }

    /** Setter for property locationAddressCity.
     * @param locationAddressCity New value of property locationAddressCity.
     *
     */
    public void setLocationAddressCity(String locationAddressCity) {
        this.locationAddressCity = locationAddressCity;
    }

    /** Getter for property locationAddressCountryCodePk.
     * @return Value of property locationAddressCountryCodePk.
     *
     */
    public Integer getLocationAddressCountryCodePk() {
        return locationAddressCountryCodePk;
    }

    /** Setter for property locationAddressCountryCodePk.
     * @param locationAddressCountryCodePk New value of property locationAddressCountryCodePk.
     *
     */
    public void setLocationAddressCountryCodePk(Integer locationAddressCountryCodePk) {
        this.locationAddressCountryCodePk = locationAddressCountryCodePk;
    }

    /** Getter for property locationAddressCounty.
     * @return Value of property locationAddressCounty.
     *
     */
    public String getLocationAddressCounty() {
        return locationAddressCounty;
    }

    /** Setter for property locationAddressCounty.
     * @param locationAddressCounty New value of property locationAddressCounty.
     *
     */
    public void setLocationAddressCounty(String locationAddressCounty) {
        this.locationAddressCounty = locationAddressCounty;
    }

    /** Getter for property locationAddressProvince.
     * @return Value of property locationAddressProvince.
     *
     */
    public String getLocationAddressProvince() {
        return locationAddressProvince;
    }

    /** Setter for property locationAddressProvince.
     * @param locationAddressProvince New value of property locationAddressProvince.
     *
     */
    public void setLocationAddressProvince(String locationAddressProvince) {
        this.locationAddressProvince = locationAddressProvince;
    }

    /** Getter for property locationAddressState.
     * @return Value of property locationAddressState.
     *
     */
    public String getLocationAddressState() {
        return locationAddressState;
    }

    /** Setter for property locationAddressState.
     * @param locationAddressState New value of property locationAddressState.
     *
     */
    public void setLocationAddressState(String locationAddressState) {
        this.locationAddressState = locationAddressState;
    }

    /** Getter for property locationAddressUpdatedByUserID.
     * @return Value of property locationAddressUpdatedByUserID.
     *
     */
    public String getLocationAddressUpdatedByUserID() {
        return locationAddressUpdatedByUserID;
    }

    /** Setter for property locationAddressUpdatedByUserID.
     * @param locationAddressUpdatedByUserID New value of property locationAddressUpdatedByUserID.
     *
     */
    public void setLocationAddressUpdatedByUserID(String locationAddressUpdatedByUserID) {
        this.locationAddressUpdatedByUserID = locationAddressUpdatedByUserID;
    }

    /** Getter for property locationAddressUpdatedBeginDate.
     * @return Value of property locationAddressUpdatedBeginDate.
     *
     */
    public Timestamp getLocationAddressUpdatedBeginDate() {
        return locationAddressUpdatedBeginDate;
    }

    /** Setter for property locationAddressUpdatedBeginDate.
     * @param locationAddressUpdatedBeginDate New value of property locationAddressUpdatedBeginDate.
     *
     */
    public void setLocationAddressUpdatedBeginDate(Timestamp locationAddressUpdatedBeginDate) {
        this.locationAddressUpdatedBeginDate = locationAddressUpdatedBeginDate;
    }

    /** Getter for property locationAddressUpdatedEndDate.
     * @return Value of property locationAddressUpdatedEndDate.
     *
     */
    public Timestamp getLocationAddressUpdatedEndDate() {
        return locationAddressUpdatedEndDate;
    }

    /** Setter for property locationAddressUpdatedEndDate.
     * @param locationAddressUpdatedEndDate New value of property locationAddressUpdatedEndDate.
     *
     */
    public void setLocationAddressUpdatedEndDate(Timestamp locationAddressUpdatedEndDate) {
        this.locationAddressUpdatedEndDate = locationAddressUpdatedEndDate;
    }

    /** Getter for property locationAddressZip.
     * @return Value of property locationAddressZip.
     *
     */
    public String getLocationAddressZip() {
        return locationAddressZip;
    }

    /** Setter for property locationAddressZip.
     * @param locationAddressZip New value of property locationAddressZip.
     *
     */
    public void setLocationAddressZip(String locationAddressZip) {
        this.locationAddressZip = locationAddressZip;
    }

    /** Getter for property locationAddressZip4.
     * @return Value of property locationAddressZip4.
     *
     */
    public String getLocationAddressZip4() {
        return locationAddressZip4;
    }

    /** Setter for property locationAddressZip4.
     * @param locationAddressZip4 New value of property locationAddressZip4.
     *
     */
    public void setLocationAddressZip4(String locationAddressZip4) {
        this.locationAddressZip4 = locationAddressZip4;
    }

    /** Getter for property locationPhoneFaxAreaCode.
     * @return Value of property locationPhoneFaxAreaCode.
     *
     */
    public String getLocationPhoneFaxAreaCode() {
        return locationPhoneFaxAreaCode;
    }

    /** Setter for property locationPhoneFaxAreaCode.
     * @param locationPhoneFaxAreaCode New value of property locationPhoneFaxAreaCode.
     *
     */
    public void setLocationPhoneFaxAreaCode(String locationPhoneFaxAreaCode) {
        this.locationPhoneFaxAreaCode = locationPhoneFaxAreaCode;
    }

    /** Getter for property locationPhoneFaxCountryCode.
     * @return Value of property locationPhoneFaxCountryCode.
     *
     */
    public String getLocationPhoneFaxCountryCode() {
        return locationPhoneFaxCountryCode;
    }

    /** Setter for property locationPhoneFaxCountryCode.
     * @param locationPhoneFaxCountryCode New value of property locationPhoneFaxCountryCode.
     *
     */
    public void setLocationPhoneFaxCountryCode(String locationPhoneFaxCountryCode) {
        this.locationPhoneFaxCountryCode = locationPhoneFaxCountryCode;
    }

    /** Getter for property locationPhoneFaxNumber.
     * @return Value of property locationPhoneFaxNumber.
     *
     */
    public String getLocationPhoneFaxNumber() {
        return locationPhoneFaxNumber;
    }

    /** Setter for property locationPhoneFaxNumber.
     * @param locationPhoneFaxNumber New value of property locationPhoneFaxNumber.
     *
     */
    public void setLocationPhoneFaxNumber(String locationPhoneFaxNumber) {
        this.locationPhoneFaxNumber = locationPhoneFaxNumber;
    }

    /** Getter for property locationPhoneOfficeAreaCode.
     * @return Value of property locationPhoneOfficeAreaCode.
     *
     */
    public String getLocationPhoneOfficeAreaCode() {
        return locationPhoneOfficeAreaCode;
    }

    /** Setter for property locationPhoneOfficeAreaCode.
     * @param locationPhoneOfficeAreaCode New value of property locationPhoneOfficeAreaCode.
     *
     */
    public void setLocationPhoneOfficeAreaCode(String locationPhoneOfficeAreaCode) {
        this.locationPhoneOfficeAreaCode = locationPhoneOfficeAreaCode;
    }

    /** Getter for property locationPhoneOfficeCountryCode.
     * @return Value of property locationPhoneOfficeCountryCode.
     *
     */
    public String getLocationPhoneOfficeCountryCode() {
        return locationPhoneOfficeCountryCode;
    }

    /** Setter for property locationPhoneOfficeCountryCode.
     * @param locationPhoneOfficeCountryCode New value of property locationPhoneOfficeCountryCode.
     *
     */
    public void setLocationPhoneOfficeCountryCode(String locationPhoneOfficeCountryCode) {
        this.locationPhoneOfficeCountryCode = locationPhoneOfficeCountryCode;
    }

    /** Getter for property locationPhoneOfficeNumber.
     * @return Value of property locationPhoneOfficeNumber.
     *
     */
    public String getLocationPhoneOfficeNumber() {
        return locationPhoneOfficeNumber;
    }

    /** Setter for property locationPhoneOfficeNumber.
     * @param locationPhoneOfficeNumber New value of property locationPhoneOfficeNumber.
     *
     */
    public void setLocationPhoneOfficeNumber(String locationPhoneOfficeNumber) {
        this.locationPhoneOfficeNumber = locationPhoneOfficeNumber;
    }

    /** Getter for property multipleEmployers.
     * @return Value of property multipleEmployers.
     *
     */
    public Boolean getMultipleEmployers() {
        return multipleEmployers;
    }

    /** Setter for property multipleEmployers.
     * @param multipleEmployers New value of property multipleEmployers.
     *
     */
    public void setMultipleEmployers(Boolean multipleEmployers) {
        this.multipleEmployers = multipleEmployers;
    }

    /** Getter for property multipleOffices.
     * @return Value of property multipleOffices.
     *
     */
    public Boolean getMultipleOffices() {
        return multipleOffices;
    }

    /** Setter for property multipleOffices.
     * @param multipleOffices New value of property multipleOffices.
     *
     */
    public void setMultipleOffices(Boolean multipleOffices) {
        this.multipleOffices = multipleOffices;
    }

    /** Getter for property website.
     * @return Value of property website.
     *
     */
    public String getWebsite() {
        return website;
    }

    /** Setter for property website.
     * @param website New value of property website.
     *
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /** Getter for property affiliateIdCode.
     * @return Value of property affiliateIdCode.
     *
     */
    public Character getAffiliateIdCode() {
        return affiliateIdCode;
    }

    /** Setter for property affiliateIdCode.
     * @param affiliateIdCode New value of property affiliateIdCode.
     *
     */
    public void setAffiliateIdCode(Character affiliateIdCode) {
        this.affiliateIdCode = affiliateIdCode;
    }

    /** Getter for property affiliateIdCouncil.
     * @return Value of property affiliateIdCouncil.
     *
     */
    public String getAffiliateIdCouncil() {
        return affiliateIdCouncil;
    }

    /** Setter for property affiliateIdCouncil.
     * @param affiliateIdCouncil New value of property affiliateIdCouncil.
     *
     */
    public void setAffiliateIdCouncil(String affiliateIdCouncil) {
        this.affiliateIdCouncil = affiliateIdCouncil;
    }

    public String getEmployerNm() {
        return employerNm;
    }

    public void setEmployerNm(String employerNm) {
        this.employerNm = employerNm;
    }


    /** Getter for property affiliateIdLocal.
     * @return Value of property affiliateIdLocal.
     *
     */
    public String getAffiliateIdLocal() {
        return affiliateIdLocal;
    }

    /** Setter for property affiliateIdLocal.
     * @param affiliateIdLocal New value of property affiliateIdLocal.
     *
     */
    public void setAffiliateIdLocal(String affiliateIdLocal) {
        this.affiliateIdLocal = affiliateIdLocal;
    }

    /** Getter for property affiliateIdState.
     * @return Value of property affiliateIdState.
     *
     */
    public String getAffiliateIdState() {
        return affiliateIdState;
    }

    /** Setter for property affiliateIdState.
     * @param affiliateIdState New value of property affiliateIdState.
     *
     */
    public void setAffiliateIdState(String affiliateIdState) {
        this.affiliateIdState = affiliateIdState;
    }

    /** Getter for property affiliateIdSubUnit.
     * @return Value of property affiliateIdSubUnit.
     *
     */
    public String getAffiliateIdSubUnit() {
        return affiliateIdSubUnit;
    }

    /** Setter for property affiliateIdSubUnit.
     * @param affiliateIdSubUnit New value of property affiliateIdSubUnit.
     *
     */
    public void setAffiliateIdSubUnit(String affiliateIdSubUnit) {
        this.affiliateIdSubUnit = affiliateIdSubUnit;
    }

    /** Getter for property affiliateIdType.
     * @return Value of property affiliateIdType.
     *
     */
    public Character getAffiliateIdType() {
        return affiliateIdType;
    }

    /** Setter for property affiliateIdType.
     * @param affiliateIdType New value of property affiliateIdType.
     *
     */
    public void setAffiliateIdType(Character affiliateIdType) {
        this.affiliateIdType = affiliateIdType;
    }

    /** Getter for property newAffiliateIdentifierSourceCode.
     * @return Value of property newAffiliateIdentifierSourceCode.
     *
     */
    public Character getNewAffiliateIdentifierSourceCode() {
        return newAffiliateIdentifierSourceCode;
    }

    /** Setter for property newAffiliateIdentifierSourceCode.
     * @param newAffiliateIdentifierSourceCode New value of property newAffiliateIdentifierSourceCode.
     *
     */
    public void setNewAffiliateIdentifierSourceCode(Character newAffiliateIdentifierSourceCode) {
        this.newAffiliateIdentifierSourceCode = newAffiliateIdentifierSourceCode;
    }

    /** Getter for property newAffiliateIdentifierSourceCouncil.
     * @return Value of property newAffiliateIdentifierSourceCouncil.
     *
     */
    public String getNewAffiliateIdentifierSourceCouncil() {
        return newAffiliateIdentifierSourceCouncil;
    }

    /** Setter for property newAffiliateIdentifierSourceCouncil.
     * @param newAffiliateIdentifierSourceCouncil New value of property newAffiliateIdentifierSourceCouncil.
     *
     */
    public void setNewAffiliateIdentifierSourceCouncil(String newAffiliateIdentifierSourceCouncil) {
        this.newAffiliateIdentifierSourceCouncil = newAffiliateIdentifierSourceCouncil;
    }

    /** Getter for property newAffiliateIdentifierSourceLocal.
     * @return Value of property newAffiliateIdentifierSourceLocal.
     *
     */
    public String getNewAffiliateIdentifierSourceLocal() {
        return newAffiliateIdentifierSourceLocal;
    }

    /** Setter for property newAffiliateIdentifierSourceLocal.
     * @param newAffiliateIdentifierSourceLocal New value of property newAffiliateIdentifierSourceLocal.
     *
     */
    public void setNewAffiliateIdentifierSourceLocal(String newAffiliateIdentifierSourceLocal) {
        this.newAffiliateIdentifierSourceLocal = newAffiliateIdentifierSourceLocal;
    }

    /** Getter for property newAffiliateIdentifierSourceState.
     * @return Value of property newAffiliateIdentifierSourceState.
     *
     */
    public String getNewAffiliateIdentifierSourceState() {
        return newAffiliateIdentifierSourceState;
    }

    /** Setter for property newAffiliateIdentifierSourceState.
     * @param newAffiliateIdentifierSourceState New value of property newAffiliateIdentifierSourceState.
     *
     */
    public void setNewAffiliateIdentifierSourceState(String newAffiliateIdentifierSourceState) {
        this.newAffiliateIdentifierSourceState = newAffiliateIdentifierSourceState;
    }

    /** Getter for property newAffiliateIdentifierSourceSubUnit.
     * @return Value of property newAffiliateIdentifierSourceSubUnit.
     *
     */
    public String getNewAffiliateIdentifierSourceSubUnit() {
        return newAffiliateIdentifierSourceSubUnit;
    }

    /** Setter for property newAffiliateIdentifierSourceSubUnit.
     * @param newAffiliateIdentifierSourceSubUnit New value of property newAffiliateIdentifierSourceSubUnit.
     *
     */
    public void setNewAffiliateIdentifierSourceSubUnit(String newAffiliateIdentifierSourceSubUnit) {
        this.newAffiliateIdentifierSourceSubUnit = newAffiliateIdentifierSourceSubUnit;
    }

    /** Getter for property newAffiliateIdentifierSourceType.
     * @return Value of property newAffiliateIdentifierSourceType.
     *
     */
    public Character getNewAffiliateIdentifierSourceType() {
        return newAffiliateIdentifierSourceType;
    }

    /** Setter for property newAffiliateIdentifierSourceType.
     * @param newAffiliateIdentifierSourceType New value of property newAffiliateIdentifierSourceType.
     *
     */
    public void setNewAffiliateIdentifierSourceType(Character newAffiliateIdentifierSourceType) {
        this.newAffiliateIdentifierSourceType = newAffiliateIdentifierSourceType;
    }

    /** Getter for property parentAffFk.
	 * @return Value of property parentAffFk.
	 *
	 */
	public void setParentAffFk(Integer parentAffFk) {
		this.parentAffFk = parentAffFk;
	}

	/** Setter for property parentAffFk.
	 * @param parentAffFk New value of property parentAffFk.
	 *
	 */
	public Integer getParentAffFk() {
		return parentAffFk;
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

    /** Getter for property orderBy.
     * @return Value of property orderBy.
     *
     */
    public String getOrderBy() {
        return orderBy;
    }

    /** Setter for property orderBy.
     * @param orderBy New value of property orderBy.
     *
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

}
