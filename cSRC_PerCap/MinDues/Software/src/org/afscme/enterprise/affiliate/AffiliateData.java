package org.afscme.enterprise.affiliate;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.CollectionUtil;
import java.util.Collection;

/**
 * Represents affilaite data.  Roughly the data on the 'Affiliate Detail' screen.
 */
public class AffiliateData {
    private Integer affPk;
    private Integer parentFk;
    private String abbreviatedName;
    private Integer statusCodePk;
    private Integer afscmeLegislativeDistrictCodePk;
    private Integer afscmeRegionCodePk;
    private Boolean multipleEmployers;
    private Boolean allowSubLocals;
    private Boolean containsSubLocals;
    private Integer newAffiliateIDSourcePk;
    private Boolean multipleOffices;
    private Boolean annualCardRunPerformed;
    private Integer annualCardRunTypeCodePk;
    private Integer memberRenewalCodePk;
    private Integer locationPk;
    private String website;
    private String legacyKey;
    private String legacyKeyOther;
    private Boolean allowedMbrView;
    private Boolean allowedMbrEdit;
    private AffiliateIdentifier affiliateId;
    private String comment;
    private RecordData recordData;
    
    /** List of codePKs for the Employer Sector types.*/
    private Collection employerSector;
    
    public AffiliateData() {
        this.allowSubLocals = null;
        this.containsSubLocals = null;
        this.allowedMbrEdit = null;
        this.allowedMbrView = null;
        this.annualCardRunPerformed = null;
        this.affPk = null;
        this.multipleEmployers = null;
        this.multipleOffices = null;
        this.afscmeLegislativeDistrictCodePk = null;
        this.afscmeRegionCodePk = null;
        this.annualCardRunTypeCodePk = null;
        this.locationPk = null;
        this.memberRenewalCodePk = null;
        this.newAffiliateIDSourcePk = null;
        this.parentFk = null;
        this.statusCodePk = null;
        this.abbreviatedName = null;
        this.comment = null;
        this.legacyKey = null;
        this.legacyKeyOther = null;
        this.website = null;
        this.employerSector = null;
        this.affiliateId = null;
        this.recordData = null;
    }
    
// General Methods...
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", parentFk = ");
        sb.append(this.parentFk);
        sb.append(", affiliateId = ");
        sb.append(this.affiliateId);
        sb.append(", abbreviatedName = ");
        sb.append(this.abbreviatedName);
        sb.append(", statusCodePk = ");
        sb.append(this.statusCodePk);
        sb.append(", locationPk = ");
        sb.append(this.locationPk);
        sb.append(", afscmeLegislativeDistrictCodePk = ");
        sb.append(this.afscmeLegislativeDistrictCodePk);
        sb.append(", afscmeRegionCodePk = ");
        sb.append(this.afscmeRegionCodePk);
        sb.append(", annualCardRunPerformed = ");
        sb.append(this.annualCardRunPerformed);
        sb.append(", annualCardRunTypeCodePk = ");
        sb.append(this.annualCardRunTypeCodePk);
        sb.append(", allowSubLocals = ");
        sb.append(this.allowSubLocals);
        sb.append(", containsSubLocals = ");
        sb.append(this.containsSubLocals);
        sb.append(", allowedMbrView = ");
        sb.append(this.allowedMbrView);
        sb.append(", allowedMbrEdit = ");
        sb.append(this.allowedMbrEdit);
        sb.append(", multipleEmployers = ");
        sb.append(this.multipleEmployers);
        sb.append(", multipleOffices = ");
        sb.append(this.multipleOffices);
        sb.append(", memberRenewalCodePk = ");
        sb.append(this.memberRenewalCodePk);
        sb.append(", newAffiliateIDSourcePk = ");
        sb.append(this.newAffiliateIDSourcePk);
        sb.append(", website = ");
        sb.append(this.website);
        sb.append(", employerSector = [");
        sb.append(CollectionUtil.toString(this.employerSector));
        sb.append("], comment = ");
        sb.append(this.comment);
        sb.append(", recordData = ");
        sb.append(this.recordData);
        sb.append(", legacyKey = ");
        sb.append(this.legacyKey);
        sb.append(", legacyKeyOther = ");
        sb.append(this.legacyKeyOther);
        sb.append("]");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property abbreviatedName.
     * @return Value of property abbreviatedName.
     *
     */
    public String getAbbreviatedName() {
        return abbreviatedName;
    }
    
    /** Setter for property abbreviatedName.
     * @param abbreviatedName New value of property abbreviatedName.
     *
     */
    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }
    
    /** Getter for property affiliateId.
     * @return Value of property affiliateId.
     *
     */
    public AffiliateIdentifier getAffiliateId() {
        return affiliateId;
    }
    
    /** Setter for property affiliateId.
     * @param affiliateId New value of property affiliateId.
     *
     */
    public void setAffiliateId(AffiliateIdentifier affiliateId) {
        this.affiliateId = affiliateId;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
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
    
    /** Getter for property allowedMbrEdit.
     * @return Value of property allowedMbrEdit.
     *
     */
    public Boolean getAllowedMbrEdit() {
        return allowedMbrEdit;
    }
    
    /** Setter for property allowedMbrEdit.
     * @param allowedMbrEdit New value of property allowedMbrEdit.
     *
     */
    public void setAllowedMbrEdit(Boolean allowedMbrEdit) {
        this.allowedMbrEdit = allowedMbrEdit;
    }
    
    /** Getter for property allowedMbrView.
     * @return Value of property allowedMbrView.
     *
     */
    public Boolean getAllowedMbrView() {
        return allowedMbrView;
    }
    
    /** Setter for property allowedMbrView.
     * @param allowedMbrView New value of property allowedMbrView.
     *
     */
    public void setAllowedMbrView(Boolean allowedMbrView) {
        this.allowedMbrView = allowedMbrView;
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
    
    /** Getter for property annualCardRunPerformed.
     * @return Value of property annualCardRunPerformed.
     *
     */
    public Boolean getAnnualCardRunPerformed() {
        return annualCardRunPerformed;
    }
    
    /** Setter for property annualCardRunPerformed.
     * @param annualCardRunPerformed New value of property annualCardRunPerformed.
     *
     */
    public void setAnnualCardRunPerformed(Boolean annualCardRunPerformed) {
        this.annualCardRunPerformed = annualCardRunPerformed;
    }
    
    /** Getter for property annualCardRunTypeCodePk.
     * @return Value of property annualCardRunTypeCodePk.
     *
     */
    public Integer getAnnualCardRunTypeCodePk() {
        return annualCardRunTypeCodePk;
    }
    
    /** Setter for property annualCardRunTypeCodePk.
     * @param annualCardRunTypeCodePk New value of property annualCardRunTypeCodePk.
     *
     */
    public void setAnnualCardRunTypeCodePk(Integer annualCardRunTypeCodePk) {
        this.annualCardRunTypeCodePk = annualCardRunTypeCodePk;
    }
    
    /** Getter for property employerSector.
     * @return Value of property employerSector.
     *
     */
    public java.util.Collection getEmployerSector() {
        return employerSector;
    }
    
    /** Setter for property employerSector.
     * @param employerSector New value of property employerSector.
     *
     */
    public void setEmployerSector(java.util.Collection employerSector) {
        this.employerSector = employerSector;
    }
    
    /** Getter for property legacyKey.
     * @return Value of property legacyKey.
     *
     */
    public String getLegacyKey() {
        return legacyKey;
    }
    
    /** Setter for property legacyKey.
     * @param legacyKey New value of property legacyKey.
     *
     */
    public void setLegacyKey(String legacyKey) {
        this.legacyKey = legacyKey;
    }
    
    /** Getter for property legacyKeyOther.
     * @return Value of property legacyKeyOther.
     *
     */
    public String getLegacyKeyOther() {
        return legacyKeyOther;
    }
    
    /** Setter for property legacyKeyOther.
     * @param legacyKeyOther New value of property legacyKeyOther.
     *
     */
    public void setLegacyKeyOther(String legacyKeyOther) {
        this.legacyKeyOther = legacyKeyOther;
    }
    
    /** Getter for property locationPk.
     * @return Value of property locationPk.
     *
     */
    public Integer getLocationPk() {
        return locationPk;
    }
    
    /** Setter for property locationPk.
     * @param locationPk New value of property locationPk.
     *
     */
    public void setLocationPk(Integer locationPk) {
        this.locationPk = locationPk;
    }
    
    /** Getter for property memberRenewalCodePk.
     * @return Value of property memberRenewalCodePk.
     *
     */
    public Integer getMemberRenewalCodePk() {
        return memberRenewalCodePk;
    }
    
    /** Setter for property memberRenewalCodePk.
     * @param memberRenewalCodePk New value of property memberRenewalCodePk.
     *
     */
    public void setMemberRenewalCodePk(Integer memberRenewalCodePk) {
        this.memberRenewalCodePk = memberRenewalCodePk;
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
    
    /** Getter for property newAffiliateIDSourcePk.
     * @return Value of property newAffiliateIDSourcePk.
     *
     */
    public Integer getNewAffiliateIDSourcePk() {
        return newAffiliateIDSourcePk;
    }
    
    /** Setter for property newAffiliateIDSourcePk.
     * @param newAffiliateIDSourcePk New value of property newAffiliateIDSourcePk.
     *
     */
    public void setNewAffiliateIDSourcePk(Integer newAffiliateIDSourcePk) {
        this.newAffiliateIDSourcePk = newAffiliateIDSourcePk;
    }
    
    /** Getter for property parentFk.
     * @return Value of property parentFk.
     *
     */
    public Integer getParentFk() {
        return parentFk;
    }
    
    /** Setter for property parentFk.
     * @param parentFk New value of property parentFk.
     *
     */
    public void setParentFk(Integer parentFk) {
        this.parentFk = parentFk;
    }
    
    /** Getter for property statusCodePk.
     * @return Value of property statusCodePk.
     *
     */
    public Integer getStatusCodePk() {
        return statusCodePk;
    }
    
    /** Setter for property statusCodePk.
     * @param statusCodePk New value of property statusCodePk.
     *
     */
    public void setStatusCodePk(Integer statusCodePk) {
        this.statusCodePk = statusCodePk;
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
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
    }
    
    /** Getter for property containsSubLocals.
     * @return Value of property containsSubLocals.
     *
     */
    public Boolean getContainsSubLocals() {
        return containsSubLocals;
    }
    
    /** Setter for property containsSubLocals.
     * @param containsSubLocals New value of property containsSubLocals.
     *
     */
    public void setContainsSubLocals(Boolean containsSubLocals) {
        this.containsSubLocals = containsSubLocals;
    }
    
}
