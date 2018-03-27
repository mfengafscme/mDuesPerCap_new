package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.ArrayList;

import org.afscme.enterprise.util.TextUtil;

/**
 * This class represents data required to create a new affiliate. Based on the 
 * current data model, this data will be used to populate (create) records in 
 * several tables, including the affiliate table and charter table.
 */
public class NewAffiliate {
    
    private Integer affPk;
    private AffiliateIdentifier affiliateId;
    private String  affiliateName;
    private Integer affiliateStatusCodePk;
    private Integer annualCardRunTypeCodePk;
    private Integer afscmeLegislativeDistrict;
    private Integer affiliateRegionCodePk;
    private Boolean multipleEmployers;
    private Boolean allowSubLocals;
    private String charterName;
    private String charterJurisdiction;
    private Integer charterCode;
    private Timestamp charterDate;
    private Collection counties;
    private Boolean approvedConstitution;
    private Timestamp effectiveDate;
    private Boolean generateDefaultOffices;
    private Timestamp affiliateAgreementDate;
    private Boolean memberAllowEdit;
    private Boolean memberAllowView;
    private Integer parentAffPk;
    
    public NewAffiliate() {
        this.affiliateId = null;
        this.affiliateName = null;
        this.affiliateRegionCodePk = null;
        this.affiliateStatusCodePk = null;
        this.affPk = null;
        this.afscmeLegislativeDistrict = null;
        this.allowSubLocals = null;
        this.annualCardRunTypeCodePk = null;
        this.charterName = null;
        this.charterJurisdiction = null;
        this.charterCode = null;
        this.charterDate = null;
        this.counties = null;
        this.approvedConstitution = null;
        this.effectiveDate = null;
        this.generateDefaultOffices = null;
        this.affiliateAgreementDate = null;
        this.memberAllowEdit = new Boolean(true);
        this.memberAllowView = new Boolean(true);
        this.multipleEmployers = null;
        this.parentAffPk = null;
    }
    
// Getter and Setter Methods...
    
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
        this.affiliateId = new AffiliateIdentifier();
        this.affiliateId.setCode(affiliateId.getCode());
        if (TextUtil.isEmptyOrSpaces(affiliateId.getCouncil())) {
            this.affiliateId.setCouncil(AffiliateIdentifier.DEFAULT_ID_NUMBER);
        } else {
            this.affiliateId.setCouncil(affiliateId.getCouncil());
        }
        if (TextUtil.isEmptyOrSpaces(affiliateId.getLocal())) {
            this.affiliateId.setLocal(AffiliateIdentifier.DEFAULT_ID_NUMBER);
        } else {
            this.affiliateId.setLocal(affiliateId.getLocal());
        }
        this.affiliateId.setState(affiliateId.getState());
        if (TextUtil.isEmptyOrSpaces(affiliateId.getSubUnit())) {
            this.affiliateId.setSubUnit(AffiliateIdentifier.DEFAULT_ID_NUMBER);
        } else {
            this.affiliateId.setSubUnit(affiliateId.getSubUnit());
        }
        this.affiliateId.setType(affiliateId.getType());
    }
    
    /** Getter for property affiliateName.
     * @return Value of property affiliateName.
     *
     */
    public String getAffiliateName() {
        return affiliateName;
    }
    
    /** Setter for property affiliateName.
     * @param affiliateName New value of property affiliateName.
     *
     */
    public void setAffiliateName(String affiliateName) {
        this.affiliateName = affiliateName;
    }
    
    /** Getter for property affiliateRegionCodePk.
     * @return Value of property affiliateRegionCodePk.
     *
     */
    public Integer getAffiliateRegionCodePk() {
        return affiliateRegionCodePk;
    }
    
    /** Setter for property affiliateRegionCodePk.
     * @param affiliateRegionCodePk New value of property affiliateRegionCodePk.
     *
     */
    public void setAffiliateRegionCodePk(Integer affiliateRegionCodePk) {
        this.affiliateRegionCodePk = affiliateRegionCodePk;
    }
    
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
        this.afscmeLegislativeDistrict = afscmeLegislativeDistrict;
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
    
    /** Getter for property charterDate.
     * @return Value of property charterDate.
     *
     */
    public Timestamp getCharterDate() {
        return charterDate;
    }
    
    /** Setter for property charterDate.
     * @param charterDate New value of property charterDate.
     *
     */
    public void setCharterDate(Timestamp charterDate) {
        this.charterDate = charterDate;
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
    
    /** Getter for property parentAffPk.
     * @return Value of property parentAffPk.
     *
     */
    public Integer getParentAffPk() {
        return parentAffPk;
    }
    
    /** Setter for property parentAffPk.
     * @param parentAffPk New value of property parentAffPk.
     *
     */
    public void setParentAffPk(Integer parentAffPk) {
        this.parentAffPk = parentAffPk;
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
    
    /** Getter for property memberAllowView.
     * @return Value of property memberAllowView.
     *
     */
    public Boolean getMemberAllowView() {
        return memberAllowView;
    }
    
    /** Setter for property memberAllowView.
     * @param memberAllowView New value of property memberAllowView.
     *
     */
    public void setMemberAllowView(Boolean memberAllowView) {
        this.memberAllowView = memberAllowView;
    }
    
    /** Getter for property memberAllowEdit.
     * @return Value of property memberAllowEdit.
     *
     */
    public Boolean getMemberAllowEdit() {
        return memberAllowEdit;
    }
    
    /** Setter for property memberAllowEdit.
     * @param memberAllowEdit New value of property memberAllowEdit.
     *
     */
    public void setMemberAllowEdit(Boolean memberAllowEdit) {
        this.memberAllowEdit = memberAllowEdit;
    }
    
    /** Getter for property charterJurisdiction.
     * @return Value of property charterJurisdiction.
     *
     */
    public String getCharterJurisdiction() {
        return charterJurisdiction;
    }
    
    /** Setter for property charterJurisdiction.
     * @param charterJurisdiction New value of property charterJurisdiction.
     *
     */
    public void setCharterJurisdiction(String charterJurisdiction) {
        this.charterJurisdiction = charterJurisdiction;
    }
    
    /** Getter for property charterName.
     * @return Value of property charterName.
     *
     */
    public String getCharterName() {
        return charterName;
    }
    
    /** Setter for property charterName.
     * @param charterName New value of property charterName.
     *
     */
    public void setCharterName(String charterName) {
        this.charterName = charterName;
    }
    
    /** Getter for property charterCode.
     * @return Value of property charterCode.
     *
     */
    public Integer getCharterCode() {
        return charterCode;
    }
    
    /** Setter for property charterCode.
     * @param charterCode New value of property charterCode.
     *
     */
    public void setCharterCode(Integer charterCode) {
        this.charterCode = charterCode;
    }
    
    /** Getter for property counties.
     * @return Value of property counties.
     *
     */
    public Collection getCounties() {
        return counties;
    }
    
    /** Setter for property counties.
     * @param counties New value of property counties.
     *
     */
    public void setCounties(Collection counties) {
        this.counties = new ArrayList(counties);
    }
    
    public void addCounty(String county) {
        if (!TextUtil.isEmptyOrSpaces(county)) {
            if (this.counties == null) {
                this.counties = new ArrayList();
            }
            this.counties.add(county);
        }
    }
    
    /** Getter for property approvedConstitution.
     * @return Value of property approvedConstitution.
     *
     */
    public Boolean getApprovedConstitution() {
        return approvedConstitution;
    }
    
    /** Setter for property approvedConstitution.
     * @param approvedConstitution New value of property approvedConstitution.
     *
     */
    public void setApprovedConstitution(Boolean approvedConstitution) {
        this.approvedConstitution = approvedConstitution;
    }
    
    /** Getter for property generateDefaultOffices.
     * @return Value of property generateDefaultOffices.
     *
     */
    public Boolean getGenerateDefaultOffices() {
        return generateDefaultOffices;
    }
    
    /** Setter for property generateDefaultOffices.
     * @param generateDefaultOffices New value of property generateDefaultOffices.
     *
     */
    public void setGenerateDefaultOffices(Boolean generateDefaultOffices) {
        this.generateDefaultOffices = generateDefaultOffices;
    }
    
    /** Getter for property affiliateAgreementDate.
     * @return Value of property affiliateAgreementDate.
     *
     */
    public Timestamp getAffiliateAgreementDate() {
        return affiliateAgreementDate;
    }
    
    /** Setter for property affiliateAgreementDate.
     * @param affiliateAgreementDate New value of property affiliateAgreementDate.
     *
     */
    public void setAffiliateAgreementDate(Timestamp affiliateAgreementDate) {
        this.affiliateAgreementDate = affiliateAgreementDate;
    }
    
    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     *
     */
    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }
    
    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     *
     */
    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("[");
        buf.append("affPk = ");
        buf.append(this.affPk);
        buf.append(", affiliateId = ");
        buf.append(this.affiliateId);
        buf.append(", affiliateName = ");
        buf.append(this.affiliateName);
        buf.append(", affiliateRegionCodePk = ");
        buf.append(this.affiliateRegionCodePk);
        buf.append(", affiliateStatusCodePk = ");
        buf.append(this.affiliateStatusCodePk);
        buf.append(", afscmeLegislativeDistrict = ");
        buf.append(this.afscmeLegislativeDistrict);
        buf.append(", allowSubLocals = ");
        buf.append(this.allowSubLocals);
        buf.append(", annualCardRunTypeCodePk = ");
        buf.append(this.annualCardRunTypeCodePk);
        buf.append(", memberAllowEdit = ");
        buf.append(this.memberAllowEdit);
        buf.append(", memberAllowView = ");
        buf.append(this.memberAllowView);
        buf.append(", multipleEmployers = ");
        buf.append(this.multipleEmployers);
        buf.append(", parentAffPk = ");
        buf.append(this.parentAffPk);
        buf.append(", charterDate = ");
        buf.append(this.charterDate);
        buf.append(", charterName = ");
        buf.append(this.charterName);
        buf.append(", charterJurisdiction = ");
        buf.append(this.charterJurisdiction);
        buf.append(", charterCode = ");
        buf.append(this.charterCode);
        buf.append(", counties = ");
        buf.append(this.counties);
        buf.append(", approvedConstitution = ");
        buf.append(this.approvedConstitution);
        buf.append(", effectiveDate = ");
        buf.append(this.effectiveDate);
        buf.append(", generateDefaultOffices = ");
        buf.append(this.generateDefaultOffices);
        buf.append(", affiliateAgreementDate = ");
        buf.append(this.affiliateAgreementDate);
        buf.append("]");
        return buf.toString().trim();
    }
    
}
