package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateCriteria;

/**
 * @struts:form name="affiliateSearchForm"
 */
public class AffiliateSearchForm extends SearchForm {

    private Character affIdType;
    private Character affIdCode;
    private String affIdLocal;
    private String affIdState;
    private String affIdSubUnit;
    private String affIdCouncil;
    private String employerNm;
    private Boolean includeSubUnits;
    private Boolean includeInactive;
    private Integer affiliateStatus;
    private Integer afscmeLegislativeDistrict;
    private Integer afscmeRegion;
    private String multipleEmployers;
    private Integer employerSector;
    private String allowSubLocals;
    private Character newAffiliateIdentifierSourceType;
    private Character newAffiliateIdentifierSourceCode;
    private String newAffiliateIdentifierSourceLocal;
    private String newAffiliateIdentifierSourceState;
    private String newAffiliateIdentifierSourceSubUnit;
    private String newAffiliateIdentifierSourceCouncil;
    private String multipleOffices;
    private String website;
    private String locationAddressAttention;
    private String locationAddress1;
    private String locationAddress2;
    private String locationAddressCity;
    private String locationAddressState;
    private String locationAddressZip;
    private String locationAddressCounty;
    private String locationAddressProvince;
    private Integer locationAddressCountry;
    private String locationAddressUpdatedDate;
    private String locationAddressUpdatedByUserID;
    private String locationPhoneOfficeCountry;
    private String locationPhoneOfficeAreaCode;
    private String locationPhoneOfficeNumber;
    private String locationPhoneFaxCountry;
    private String locationPhoneFaxAreaCode;
    private String locationPhoneFaxNumber;
    private String searchAction;


    /** number of fields that must be filled in */
    private static final int NUM_FIELDS_REQUIRED = 2;

    /** Creates a new instance of AffiliateSearchForm */
    public AffiliateSearchForm() {
        this.init();
    }


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        if (request.getParameter("reset") != null) {
            // only check for fields that could be disabled...
            if (request.getParameter("affIdLocal") == null) {
                this.affIdLocal = null;
            }
            if (request.getParameter("affIdSubUnit") == null) {
                this.affIdSubUnit = null;
            }
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        //if (fieldsFilledInNumberCorrect()) {
        //    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.affiliate.required.fields"));
        //}

        return errors;
    }

// General methods...

    protected void init() {
        this.affIdType = null;
        this.affIdCode = null;
        this.affIdLocal = null;
        this.affIdState = null;
        this.affIdSubUnit = null;
        this.affIdCouncil = null;
        this.includeSubUnits = null;
        this.includeInactive = new Boolean("false");
        this.affiliateStatus = null;
        this.afscmeLegislativeDistrict = null;
        this.afscmeRegion = null;
        this.multipleEmployers = null;
        this.employerSector = null;
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
        this.locationAddressCounty = null;
        this.locationAddressProvince = null;
        this.locationAddressCountry = null;
        this.locationAddressUpdatedDate = null;
        this.locationAddressUpdatedByUserID = null;
        this.locationPhoneOfficeCountry = null;
        this.locationPhoneOfficeAreaCode = null;
        this.locationPhoneOfficeNumber = null;
        this.locationPhoneFaxCountry = null;
        this.locationPhoneFaxAreaCode = null;
        this.locationPhoneFaxNumber = null;
    }

    public boolean fieldsFilledInNumberCorrect() {
        int count = 0;
        if (!TextUtil.isEmptyOrSpaces(this.affIdType)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdCode)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdLocal)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdState)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdSubUnit)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.affIdCouncil)) {
            count++;
        }

        if (count >= NUM_FIELDS_REQUIRED)
            return false;

        if (this.affiliateStatus != null && this.affiliateStatus.intValue() > 0) {
            count++;
        }
        if (this.afscmeLegislativeDistrict != null && this.afscmeLegislativeDistrict.intValue() > 0) {
            count++;
        }
        if (this.afscmeRegion != null && this.afscmeRegion.intValue() > 0) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.multipleEmployers)) {
            count++;
        }
        if (this.employerSector != null && this.employerSector.intValue() > 0) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.allowSubLocals)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.newAffiliateIdentifierSourceType)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.newAffiliateIdentifierSourceCode)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.newAffiliateIdentifierSourceLocal)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.newAffiliateIdentifierSourceState)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.newAffiliateIdentifierSourceSubUnit)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.newAffiliateIdentifierSourceCouncil)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.multipleOffices)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.website)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressAttention)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddress1)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddress2)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressCity)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressState)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressZip)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressCounty)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressProvince)) {
            count++;
        }
        if (this.locationAddressCountry != null && this.locationAddressCountry.intValue() > 0) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressUpdatedDate)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationAddressUpdatedByUserID)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationPhoneOfficeCountry)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationPhoneOfficeAreaCode)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationPhoneOfficeNumber)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationPhoneFaxCountry)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationPhoneFaxAreaCode)) {
            count++;
        }
        if (!TextUtil.isEmptyOrSpaces(this.locationPhoneFaxNumber)) {
            count++;
        }

        if (count >= NUM_FIELDS_REQUIRED)
            return false;

        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("AffiliateSearchForm {");
        sb.append("affIdType = ");sb.append(this.affIdType);
        sb.append(", affIdLocal = ");sb.append(this.affIdLocal);
        sb.append(", affIdState = ");sb.append(this.affIdState);
        sb.append(", affIdSubUnit = ");sb.append(this.affIdSubUnit);
        sb.append(", affIdCouncil = ");sb.append(this.affIdCouncil);
        sb.append(", affIdCode = ");sb.append(this.affIdCode);
        sb.append(", affiliateStatus = ");sb.append(this.affiliateStatus);
        sb.append(", afscmeLegislativeDistrict = ");sb.append(this.afscmeLegislativeDistrict);
        sb.append(", afscmeRegion = ");sb.append(this.afscmeRegion);
        sb.append(", allowSubLocals = ");sb.append(this.allowSubLocals);
        sb.append(", employerSector = ");sb.append(this.employerSector);
        sb.append(", includeSubUnits = ");sb.append(this.includeSubUnits);
        sb.append(", includeInactive = ");sb.append(this.includeInactive);
        sb.append(", locationAddressAttention = ");sb.append(this.locationAddressAttention);
        sb.append(", locationAddress1 = ");sb.append(this.locationAddress1);
        sb.append(", locationAddress2 = ");sb.append(this.locationAddress2);
        sb.append(", locationAddressCity = ");sb.append(this.locationAddressCity);
        sb.append(", locationAddressState = ");sb.append(this.locationAddressState);
        sb.append(", locationAddressZip = ");sb.append(this.locationAddressZip);
        sb.append(", locationAddressCounty = ");sb.append(this.locationAddressCounty);
        sb.append(", locationAddressProvince = ");sb.append(this.locationAddressProvince);
        sb.append(", locationAddressCountry = ");sb.append(this.locationAddressCountry);
        sb.append(", locationAddressUpdatedDate = ");sb.append(this.locationAddressUpdatedDate);
        sb.append(", locationAddressUpdatedByUserID = ");sb.append(this.locationAddressUpdatedByUserID);
        sb.append(", locationPhoneOfficeCountry = ");sb.append(this.locationPhoneOfficeCountry);
        sb.append(", locationPhoneOfficeAreaCode = ");sb.append(this.locationPhoneOfficeAreaCode);
        sb.append(", locationPhoneOfficeNumber = ");sb.append(this.locationPhoneOfficeNumber);
        sb.append(", locationPhoneFaxCountry = ");sb.append(this.locationPhoneFaxCountry);
        sb.append(", locationPhoneFaxAreaCode = ");sb.append(this.locationPhoneFaxAreaCode);
        sb.append(", locationPhoneFaxNumber = ");sb.append(this.locationPhoneFaxNumber);
        sb.append(", multipleEmployers = ");sb.append(this.multipleEmployers);
        sb.append(", multipleOffices = ");sb.append(this.multipleOffices);
        sb.append(", newAffiliateIdentifierSourceType = ");sb.append(this.newAffiliateIdentifierSourceType);
        sb.append(", newAffiliateIdentifierSourceLocal = ");sb.append(this.newAffiliateIdentifierSourceLocal);
        sb.append(", newAffiliateIdentifierSourceState = ");sb.append(this.newAffiliateIdentifierSourceState);
        sb.append(", newAffiliateIdentifierSourceSubUnit = ");sb.append(this.newAffiliateIdentifierSourceSubUnit);
        sb.append(", newAffiliateIdentifierSourceCouncil = ");sb.append(this.newAffiliateIdentifierSourceCouncil);
        sb.append(", newAffiliateIdentifierSourceCode = ");sb.append(this.newAffiliateIdentifierSourceCode);
        sb.append(", website = ");sb.append(this.website);
        sb.append(", page = ");sb.append(this.page);
        sb.append(", pageSize = ");sb.append(this.pageSize);
        sb.append(", total = ");sb.append(this.total);
        sb.append("}");
        return sb.toString().trim();
    }

// Getter and Setter methods...

    public AffiliateCriteria getCriteria() {
        AffiliateCriteria ac = new AffiliateCriteria();
        ac.setAffiliateIdCouncil(this.affIdCouncil);
        ac.setAffiliateIdLocal(this.affIdLocal);
        ac.setAffiliateIdState(this.affIdState);
        ac.setAffiliateIdSubUnit(this.affIdSubUnit);
        ac.setAffiliateIdType(this.affIdType);
        ac.setIncludeSubUnits(this.includeSubUnits);
        ac.setIncludeInactive(this.includeInactive);
        ac.setAffiliateStatusCodePk(this.affiliateStatus);
        ac.setAfscmeLegislativeDistrictCodePk(this.afscmeLegislativeDistrict);
        ac.setAfscmeRegionCodePk(this.afscmeRegion);
        if (!TextUtil.isEmptyOrSpaces(this.allowSubLocals)) {
            ac.setAllowSubLocals(new Boolean(this.allowSubLocals));
        }
        ac.setEmployerSectorCodePk(this.employerSector);
        ac.setLocationAddress1(this.locationAddress1);
        ac.setLocationAddress2(this.locationAddress2);
        ac.setLocationAddressAttention(this.locationAddressAttention);
        ac.setLocationAddressCity(this.locationAddressCity);
        ac.setLocationAddressCountryCodePk(this.locationAddressCountry);
        ac.setLocationAddressCounty(this.locationAddressCounty);
        ac.setLocationAddressProvince(this.locationAddressProvince);
        ac.setLocationAddressState(this.locationAddressState);
        ac.setLocationAddressUpdatedByUserID(this.locationAddressUpdatedByUserID);
        ac.setLocationAddressUpdatedBeginDate(DateUtil.getTimestamp(this.locationAddressUpdatedDate));
        ac.setLocationAddressUpdatedEndDate(DateUtil.incrementTimestampByDay(DateUtil.getTimestamp(this.locationAddressUpdatedDate)));
        ac.setLocationAddressZip(this.locationAddressZip);
        ac.setLocationPhoneOfficeCountryCode(this.locationPhoneOfficeCountry);
        ac.setLocationPhoneOfficeAreaCode(this.locationPhoneOfficeAreaCode);
        ac.setLocationPhoneOfficeNumber(this.locationPhoneOfficeNumber);
        ac.setLocationPhoneFaxCountryCode(this.locationPhoneFaxCountry);
        ac.setLocationPhoneFaxAreaCode(this.locationPhoneFaxAreaCode);
        ac.setLocationPhoneFaxNumber(this.locationPhoneFaxNumber);
        if (!TextUtil.isEmptyOrSpaces(this.multipleEmployers)) {
            ac.setMultipleEmployers(new Boolean(this.multipleEmployers));
        }
        if (!TextUtil.isEmptyOrSpaces(this.multipleOffices)) {
            ac.setMultipleOffices(new Boolean(this.multipleOffices));
        }
        ac.setNewAffiliateIdentifierSourceCode(this.newAffiliateIdentifierSourceCode);
        ac.setNewAffiliateIdentifierSourceCouncil(this.newAffiliateIdentifierSourceCouncil);
        ac.setNewAffiliateIdentifierSourceLocal(this.newAffiliateIdentifierSourceLocal);
        ac.setNewAffiliateIdentifierSourceState(this.newAffiliateIdentifierSourceState);
        ac.setNewAffiliateIdentifierSourceSubUnit(this.newAffiliateIdentifierSourceSubUnit);
        ac.setNewAffiliateIdentifierSourceType(this.newAffiliateIdentifierSourceType);
        ac.setWebsite(this.website);
        return ac;
    }

    /** Getter for property afscmeLegislativeDistrict.
     * @return Value of property afscmeLegislativeDistrict.
     *
     */
    public Integer getAfscmeLegislativeDistrict() {
        return afscmeLegislativeDistrict;
    }

    /** Setter for property afscmeLegislativeDistrict.
     * @param afscmeLegislativeDistrict New value of property afscmeLegislativeDistrict.
     *
     */
    public void setAfscmeLegislativeDistrict(Integer afscmeLegislativeDistrict) {
        if (afscmeLegislativeDistrict != null && afscmeLegislativeDistrict.intValue() < 1) {
            this.afscmeLegislativeDistrict = null;
        } else {
            this.afscmeLegislativeDistrict = afscmeLegislativeDistrict;
        }
    }

    /** Getter for property afscmeRegion.
     * @return Value of property afscmeRegion.
     *
     */
    public Integer getAfscmeRegion() {
        return afscmeRegion;
    }

    /** Setter for property afscmeRegion.
     * @param afscmeRegion New value of property afscmeRegion.
     *
     */
    public void setAfscmeRegion(Integer afscmeRegion) {
        if (afscmeRegion != null && afscmeRegion.intValue() < 1) {
            this.afscmeRegion = null;
        } else {
            this.afscmeRegion = afscmeRegion;
        }
    }

    /** Getter for property allowSubLocals.
     * @return Value of property allowSubLocals.
     *
     */
    public String getAllowSubLocals() {
        return allowSubLocals;
    }

    /** Setter for property allowSubLocals.
     * @param allowSubLocals New value of property allowSubLocals.
     *
     */
    public void setAllowSubLocals(String allowSubLocals) {
        this.allowSubLocals = allowSubLocals;
    }

    /** Getter for property employerSector.
     * @return Value of property employerSector.
     *
     */
    public Integer getEmployerSector() {
        return employerSector;
    }

    /** Setter for property employerSector.
     * @param employerSector New value of property employerSector.
     *
     */
    public void setEmployerSector(Integer employerSector) {
        if (employerSector != null && employerSector.intValue() < 1) {
            this.employerSector = null;
        } else {
            this.employerSector = employerSector;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddress1)) {
            this.locationAddress1 = null;
        } else {
            this.locationAddress1 = locationAddress1;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddress2)) {
            this.locationAddress2 = null;
        } else {
            this.locationAddress2 = locationAddress2;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressAttention)) {
            this.locationAddressAttention = null;
        } else {
            this.locationAddressAttention = locationAddressAttention;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressCity)) {
            this.locationAddressCity = null;
        } else {
            this.locationAddressCity = locationAddressCity;
        }
    }

    /** Getter for property locationAddressCountry.
     * @return Value of property locationAddressCountry.
     *
     */
    public Integer getLocationAddressCountry() {
        return locationAddressCountry;
    }

    /** Setter for property locationAddressCountry.
     * @param locationAddressCountry New value of property locationAddressCountry.
     *
     */
    public void setLocationAddressCountry(Integer locationAddressCountry) {
        if (locationAddressCountry != null && locationAddressCountry.intValue() < 1) {
            this.locationAddressCountry = null;
        } else {
            this.locationAddressCountry = locationAddressCountry;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressCounty)) {
            this.locationAddressCounty = null;
        } else {
            this.locationAddressCounty = locationAddressCounty;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressProvince)) {
            this.locationAddressProvince = null;
        } else {
            this.locationAddressProvince = locationAddressProvince;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressState)) {
            this.locationAddressState = null;
        } else {
            this.locationAddressState = locationAddressState;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressUpdatedByUserID)) {
            this.locationAddressUpdatedByUserID = null;
        } else {
            this.locationAddressUpdatedByUserID = locationAddressUpdatedByUserID;
        }
    }

    /** Getter for property locationAddressUpdatedDate.
     * @return Value of property locationAddressUpdatedDate.
     *
     */
    public String getLocationAddressUpdatedDate() {
        return locationAddressUpdatedDate;
    }

    /** Setter for property locationAddressUpdatedDate.
     * @param locationAddressUpdatedDate New value of property locationAddressUpdatedDate.
     *
     */
    public void setLocationAddressUpdatedDate(String locationAddressUpdatedDate) {
        if (TextUtil.isEmptyOrSpaces(locationAddressUpdatedDate)) {
            this.locationAddressUpdatedDate = null;
        } else {
            this.locationAddressUpdatedDate = locationAddressUpdatedDate;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationAddressZip)) {
            this.locationAddressZip = null;
        } else {
            this.locationAddressZip = locationAddressZip;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationPhoneFaxAreaCode)) {
            this.locationPhoneFaxAreaCode = null;
        } else {
            this.locationPhoneFaxAreaCode = locationPhoneFaxAreaCode;
        }
    }

    /** Getter for property locationPhoneFaxCountry.
     * @return Value of property locationPhoneFaxCountry.
     *
     */
    public String getLocationPhoneFaxCountry() {
        return locationPhoneFaxCountry;
    }

    /** Setter for property locationPhoneFaxCountry.
     * @param locationPhoneFaxCountry New value of property locationPhoneFaxCountry.
     *
     */
    public void setLocationPhoneFaxCountry(String locationPhoneFaxCountry) {
        this.locationPhoneFaxCountry = locationPhoneFaxCountry;
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
        if (TextUtil.isEmptyOrSpaces(locationPhoneFaxNumber)) {
            this.locationPhoneFaxNumber = null;
        } else {
            this.locationPhoneFaxNumber = locationPhoneFaxNumber;
        }
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
        if (TextUtil.isEmptyOrSpaces(locationPhoneOfficeAreaCode)) {
            this.locationPhoneOfficeAreaCode = null;
        } else {
            this.locationPhoneOfficeAreaCode = locationPhoneOfficeAreaCode;
        }
    }

    /** Getter for property locationPhoneOfficeCountry.
     * @return Value of property locationPhoneOfficeCountry.
     *
     */
    public String getLocationPhoneOfficeCountry() {
        return locationPhoneOfficeCountry;
    }

    /** Setter for property locationPhoneOfficeCountry.
     * @param locationPhoneOfficeCountry New value of property locationPhoneOfficeCountry.
     *
     */
    public void setLocationPhoneOfficeCountry(String locationPhoneOfficeCountry) {
        this.locationPhoneOfficeCountry = locationPhoneOfficeCountry;
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
        if (TextUtil.isEmptyOrSpaces(locationPhoneOfficeNumber)) {
            this.locationPhoneOfficeNumber = null;
        } else {
            this.locationPhoneOfficeNumber = locationPhoneOfficeNumber;
        }
    }

    /** Getter for property multipleEmployers.
     * @return Value of property multipleEmployers.
     *
     */
    public String getMultipleEmployers() {
        return multipleEmployers;
    }

    /** Setter for property multipleEmployers.
     * @param multipleEmployers New value of property multipleEmployers.
     *
     */
    public void setMultipleEmployers(String multipleEmployers) {
        this.multipleEmployers = multipleEmployers;
    }

    /** Getter for property multipleOffices.
     * @return Value of property multipleOffices.
     *
     */
    public String getMultipleOffices() {
        return multipleOffices;
    }

    /** Setter for property multipleOffices.
     * @param multipleOffices New value of property multipleOffices.
     *
     */
    public void setMultipleOffices(String multipleOffices) {
        this.multipleOffices = multipleOffices;
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
        if (TextUtil.isEmptyOrSpaces(newAffiliateIdentifierSourceCode)) {
            this.newAffiliateIdentifierSourceCode = null;
        } else {
            this.newAffiliateIdentifierSourceCode = newAffiliateIdentifierSourceCode;
        }
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
        if (TextUtil.isEmptyOrSpaces(newAffiliateIdentifierSourceCouncil)) {
            this.newAffiliateIdentifierSourceCouncil = null;
        } else {
            this.newAffiliateIdentifierSourceCouncil = newAffiliateIdentifierSourceCouncil;
        }
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
        if (TextUtil.isEmptyOrSpaces(newAffiliateIdentifierSourceLocal)) {
            this.newAffiliateIdentifierSourceLocal = null;
        } else {
            this.newAffiliateIdentifierSourceLocal = newAffiliateIdentifierSourceLocal;
        }
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
        if (TextUtil.isEmptyOrSpaces(newAffiliateIdentifierSourceState)) {
            this.newAffiliateIdentifierSourceState = null;
        } else {
            this.newAffiliateIdentifierSourceState = newAffiliateIdentifierSourceState;
        }
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
        if (TextUtil.isEmptyOrSpaces(newAffiliateIdentifierSourceSubUnit)) {
            this.newAffiliateIdentifierSourceSubUnit = null;
        } else {
            this.newAffiliateIdentifierSourceSubUnit = newAffiliateIdentifierSourceSubUnit;
        }
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
        if (TextUtil.isEmptyOrSpaces(newAffiliateIdentifierSourceType)) {
            this.newAffiliateIdentifierSourceType = null;
        } else {
            this.newAffiliateIdentifierSourceType = newAffiliateIdentifierSourceType;
        }
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
        if (TextUtil.isEmptyOrSpaces(website)) {
            this.website = null;
        } else {
            this.website = website;
        }
    }

    /** Getter for property affIdCode.
     * @return Value of property affIdCode.
     *
     */
    public Character getAffIdCode() {
        return affIdCode;
    }

    /** Setter for property affIdCode.
     * @param affIdCode New value of property affIdCode.
     *
     */
    public void setAffIdCode(Character affIdCode) {
        if (TextUtil.isEmptyOrSpaces(affIdCode)) {
            this.affIdCode = null;
        } else {
            this.affIdCode = affIdCode;
        }
    }

    /** Getter for property affIdCouncil.
     * @return Value of property affIdCouncil.
     *
     */
    public String getAffIdCouncil() {
        return affIdCouncil;
    }

    /** Setter for property affIdCouncil.
     * @param affIdCouncil New value of property affIdCouncil.
     *
     */
    public void setAffIdCouncil(String affIdCouncil) {
        if (TextUtil.isEmptyOrSpaces(affIdCouncil)) {
            this.affIdCouncil = null;
        } else {
            this.affIdCouncil = affIdCouncil;
        }
    }

    /** Getter for property affIdLocal.
     * @return Value of property affIdLocal.
     *
     */
    public String getAffIdLocal() {
        return affIdLocal;
    }

    /** Setter for property affIdLocal.
     * @param affIdLocal New value of property affIdLocal.
     *
     */
    public void setAffIdLocal(String affIdLocal) {
        if (TextUtil.isEmptyOrSpaces(affIdLocal)) {
            this.affIdLocal = null;
        } else {
            this.affIdLocal = affIdLocal;
        }
    }

    /** Getter for property affIdState.
     * @return Value of property affIdState.
     *
     */
    public String getAffIdState() {
        return affIdState;
    }

    /** Setter for property affIdState.
     * @param affIdState New value of property affIdState.
     *
     */
    public void setAffIdState(String affIdState) {
        if (TextUtil.isEmptyOrSpaces(affIdState)) {
            this.affIdState = null;
        } else {
            this.affIdState = affIdState;
        }
    }

    /** Getter for property affIdSubUnit.
     * @return Value of property affIdSubUnit.
     *
     */
    public String getAffIdSubUnit() {
        return affIdSubUnit;
    }

    /** Setter for property affIdSubUnit.
     * @param affIdSubUnit New value of property affIdSubUnit.
     *
     */
    public void setAffIdSubUnit(String affIdSubUnit) {
        if (TextUtil.isEmptyOrSpaces(affIdSubUnit)) {
            this.affIdSubUnit = null;
        } else {
            this.affIdSubUnit = affIdSubUnit;
        }
    }

    public String getEmployerNm() {
        return employerNm;
    }

    public void setEmployerNm(String employerNm) {
        if (TextUtil.isEmptyOrSpaces(employerNm)) {
            this.employerNm = null;
        } else {
            this.employerNm = employerNm;
        }
    }

    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public Character getAffIdType() {
        return affIdType;
    }

    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(Character affIdType) {
        if (TextUtil.isEmptyOrSpaces(affIdType)) {
            this.affIdType = new Character('T');
        } else {
            this.affIdType = affIdType;
        }
    }

    /** Getter for property affiliateStatus.
     * @return Value of property affiliateStatus.
     *
     */
    public Integer getAffiliateStatus() {
        return affiliateStatus;
    }

    /** Setter for property affiliateStatus.
     * @param affiliateStatus New value of property affiliateStatus.
     *
     */
    public void setAffiliateStatus(Integer affiliateStatus) {
        if (affiliateStatus != null && affiliateStatus.intValue() < 1) {
            this.affiliateStatus = null;
        } else {
            this.affiliateStatus = affiliateStatus;
        }
    }

    /** Getter for property searchAction.
     * @return Value of property searchAction.
     *
     */
    public String getSearchAction() {
        return searchAction;
    }

    /** Setter for property searchAction.
     * @param searchAction New value of property searchAction.
     *
     */
    public void setSearchAction(String searchAction) {
        this.searchAction = searchAction;
    }

}
