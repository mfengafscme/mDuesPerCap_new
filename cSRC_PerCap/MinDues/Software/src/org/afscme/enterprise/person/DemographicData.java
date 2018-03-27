package org.afscme.enterprise.person;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * A person's demographic data.  Also contains a colleciton of the person's
 * children.
 */
public class DemographicData
{
    private Timestamp dob;
    private Timestamp deceasedDt;
    private Boolean deceasedFg;
    private Integer genderCodePK;
    private Integer ethnicOriginCodePK;
    private Integer citizenshipCodePK;

    /**
     * Collection of Integer objects.
     */
    private Collection disabilityCodePKs;

    /**
     * Collection of Integer objects.
     */
    private Collection disabilityAccommodationCodePKs;
    private Integer religionCodePK;
    private Integer maritalStatusCodePK;

    /**
     * Collection of Integer objects.
     */
    private Collection otherLanguageCodePKs;
    private Integer primaryLanguageCodePK;
    private RelationData theChildrenRelationData[];
    private RelationData thePartnerRelationData;

    /** Getter for property citizenshipCodePK.
     * @return Value of property citizenshipCodePK.
     *
     */
    public java.lang.Integer getCitizenshipCodePK() {
        return citizenshipCodePK;
    }

    /** Setter for property citizenshipCodePK.
     * @param citizenshipCodePK New value of property citizenshipCodePK.
     *
     */
    public void setCitizenshipCodePK(java.lang.Integer citizenshipCodePK) {
        this.citizenshipCodePK = citizenshipCodePK;
    }

    /** Getter for property deceasedDt.
     * @return Value of property deceasedDt.
     *
     */
    public java.sql.Timestamp getDeceasedDt() {
        return deceasedDt;
    }

    /** Setter for property deceasedDt.
     * @param deceasedDt New value of property deceasedDt.
     *
     */
    public void setDeceasedDt(java.sql.Timestamp deceasedDt) {
        this.deceasedDt = deceasedDt;
    }

    /** Getter for property disabilityAccommodationCodePKs.
     * @return Value of property disabilityAccommodationCodePKs.
     *
     */
    public java.util.Collection getDisabilityAccommodationCodePKs() {
        return disabilityAccommodationCodePKs;
    }

    /** Setter for property disabilityAccommodationCodePKs.
     * @param disabilityAccommodationCodePKs New value of property disabilityAccommodationCodePKs.
     *
     */
    public void setDisabilityAccommodationCodePKs(java.util.Collection disabilityAccommodationCodePKs) {
        this.disabilityAccommodationCodePKs = disabilityAccommodationCodePKs;
    }

    /** Getter for property disabilityCodePKs.
     * @return Value of property disabilityCodePKs.
     *
     */
    public java.util.Collection getDisabilityCodePKs() {
        return disabilityCodePKs;
    }

    /** Setter for property disabilityCodePKs.
     * @param disabilityCodePKs New value of property disabilityCodePKs.
     *
     */
    public void setDisabilityCodePKs(java.util.Collection disabilityCodePKs) {
        this.disabilityCodePKs = disabilityCodePKs;
    }

    /** Getter for property dob.
     * @return Value of property dob.
     *
     */
    public java.sql.Timestamp getDob() {
        return dob;
    }

    /** Setter for property dob.
     * @param dob New value of property dob.
     *
     */
    public void setDob(java.sql.Timestamp dob) {
        this.dob = dob;
    }

    /** Getter for property ethnicOriginCodePK.
     * @return Value of property ethnicOriginCodePK.
     *
     */
    public java.lang.Integer getEthnicOriginCodePK() {
        return ethnicOriginCodePK;
    }

    /** Setter for property ethnicOriginCodePK.
     * @param ethnicOriginCodePK New value of property ethnicOriginCodePK.
     *
     */
    public void setEthnicOriginCodePK(java.lang.Integer ethnicOriginCodePK) {
        this.ethnicOriginCodePK = ethnicOriginCodePK;
    }

    /** Getter for property genderCodePK.
     * @return Value of property genderCodePK.
     *
     */
    public java.lang.Integer getGenderCodePK() {
        return genderCodePK;
    }

    /** Setter for property genderCodePK.
     * @param genderCodePK New value of property genderCodePK.
     *
     */
    public void setGenderCodePK(java.lang.Integer genderCodePK) {
        this.genderCodePK = genderCodePK;
    }

    /** Getter for property maritalStatusCodePK.
     * @return Value of property maritalStatusCodePK.
     *
     */
    public java.lang.Integer getMaritalStatusCodePK() {
        return maritalStatusCodePK;
    }

    /** Setter for property maritalStatusCodePK.
     * @param maritalStatusCodePK New value of property maritalStatusCodePK.
     *
     */
    public void setMaritalStatusCodePK(java.lang.Integer maritalStatusCodePK) {
        this.maritalStatusCodePK = maritalStatusCodePK;
    }

    /** Getter for property otherLanguageCodePKs.
     * @return Value of property otherLanguageCodePKs.
     *
     */
    public java.util.Collection getOtherLanguageCodePKs() {
        return otherLanguageCodePKs;
    }

    /** Setter for property otherLanguageCodePKs.
     * @param otherLanguageCodePKs New value of property otherLanguageCodePKs.
     *
     */
    public void setOtherLanguageCodePKs(java.util.Collection otherLanguageCodePKs) {
        this.otherLanguageCodePKs = otherLanguageCodePKs;
    }

    /** Getter for property primaryLanguageCodePK.
     * @return Value of property primaryLanguageCodePK.
     *
     */
    public java.lang.Integer getPrimaryLanguageCodePK() {
        return primaryLanguageCodePK;
    }

    /** Setter for property primaryLanguageCodePK.
     * @param primaryLanguageCodePK New value of property primaryLanguageCodePK.
     *
     */
    public void setPrimaryLanguageCodePK(java.lang.Integer primaryLanguageCodePK) {
        this.primaryLanguageCodePK = primaryLanguageCodePK;
    }

    /** Getter for property religionCodePK.
     * @return Value of property religionCodePK.
     *
     */
    public java.lang.Integer getReligionCodePK() {
        return religionCodePK;
    }

    /** Setter for property religionCodePK.
     * @param religionCodePK New value of property religionCodePK.
     *
     */
    public void setReligionCodePK(java.lang.Integer religionCodePK) {
        this.religionCodePK = religionCodePK;
    }

    /** Getter for property theChildrenRelationData.
     * @return Value of property theChildrenRelationData.
     *
     */
    public org.afscme.enterprise.person.RelationData[] getTheChildrenRelationData() {
        return this.theChildrenRelationData;
    }

    /** Setter for property theChildrenRelationData.
     * @param theChildrenRelationData New value of property theChildrenRelationData.
     *
     */
    public void setTheChildrenRelationData(org.afscme.enterprise.person.RelationData[] theChildrenRelationData) {
        this.theChildrenRelationData = theChildrenRelationData;
    }

    /** Getter for property thePartnerRelationData.
     * @return Value of property thePartnerRelationData.
     *
     */
    public org.afscme.enterprise.person.RelationData getThePartnerRelationData() {
        return thePartnerRelationData;
    }

    /** Setter for property thePartnerRelationData.
     * @param thePartnerRelationData New value of property thePartnerRelationData.
     *
     */
    public void setThePartnerRelationData(org.afscme.enterprise.person.RelationData thePartnerRelationData) {
        this.thePartnerRelationData = thePartnerRelationData;
    }

    /** Getter for property deceasedFg.
     * @return Value of property deceasedFg.
     *
     */
    public Boolean getDeceasedFg() {
        return deceasedFg;
    }

    /** Setter for property deceasedFg.
     * @param thePartnerRelationData New value of property deceasedFg.
     *
     */
    public void setDeceasedFg(Boolean deceasedFg) {
        this.deceasedFg = deceasedFg;
    }

}
