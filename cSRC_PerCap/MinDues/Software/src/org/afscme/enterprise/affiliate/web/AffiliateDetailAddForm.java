package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;

/**
 * @struts:form name="affiliateDetailAddForm"
 */
public class AffiliateDetailAddForm extends ActionForm {
    
    private static Logger logger =  Logger.getLogger(AffiliateDetailAddForm.class);       
    
    private Integer affPk;
    private Character affIdType;
    private String affIdLocal;
    private String affIdState;
    private String affIdSubUnit;
    private String affIdCouncil;
    private Character affIdCode;
    private String affilName;
    private Integer affilStatus;
    private Integer annualCardRunType;
    private Integer afscmeLegislativeDistrict;
    private Integer afscmeRegion;
    private boolean multipleEmployers;
    private boolean subLocalsAllowed;
    private String charterName;
    private String charterJurisdiction;
    private Integer charterCode;
    private Integer charterDateMonth;
    private String charterDateYear;
    private String charterCounty1;
    private String charterCounty2;
    private String charterCounty3;
    private boolean approvedConstitution;
    private boolean generateDefaultOffices;
    private Integer affilAgreementDateMonth;
    private String affilAgreementDateYear;
    private Integer effectiveDateMonth;
    private String effectiveDateYear;
    private Integer parentFk;
    private Integer createdBy;
    private String createdDate;
    
    private final static Integer[] VALID_COUNCIL_STATUS = {
                AffiliateStatus.AC, AffiliateStatus.C, AffiliateStatus.D, 
                AffiliateStatus.N, AffiliateStatus.PC, AffiliateStatus.PD, 
                AffiliateStatus.RA, AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_RETIREECHAPTER_STATUS = {
                AffiliateStatus.C, AffiliateStatus.D, AffiliateStatus.N, 
                AffiliateStatus.PC, AffiliateStatus.PD, AffiliateStatus.RA, 
                AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_SUBCHAPTER_STATUS = {
                AffiliateStatus.C, AffiliateStatus.D, AffiliateStatus.M, 
                AffiliateStatus.N, AffiliateStatus.PC, AffiliateStatus.PD, 
                AffiliateStatus.RA, AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_LOCAL_STATUS = {
                AffiliateStatus.C, AffiliateStatus.D, AffiliateStatus.M, 
                AffiliateStatus.N, AffiliateStatus.PC, AffiliateStatus.PD, 
                AffiliateStatus.RA, AffiliateStatus.UA
    };
    
    private final static Integer[] VALID_SUBLOCAL_STATUS = {
                AffiliateStatus.CU, AffiliateStatus.DU
    };
    
    /** Creates a new instance of AffiliateDetailAddForm */
    public AffiliateDetailAddForm() {
        this.init();
    }
    
// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        // evaluate if parentFk should have a value
        if (shouldHaveParent() && !hasParent() ) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.affiliate.noParent"));
        }
        if (TextUtil.isEmptyOrSpaces(this.getAffIdType())) {
            errors.add("affIdType", new ActionError("error.field.required.generic", "Affiliate Identifier Type"));
        } else {
            switch (this.getAffIdType().charValue()) {
                // Check for valid status based on Affiliate Type.
                case 'U':
                    if (TextUtil.isEmptyOrSpaces(this.getAffIdSubUnit())) {
                        errors.add("affIdSubUnit", new ActionError("error.affiliate.conditional.subUnit"));
                    } else if (!TextUtil.isAlphaNumeric(this.getAffIdSubUnit())) {
                        errors.add("affIdSubUnit", new ActionError("error.field.mustBeAlphaNumeric.generic", "Affiliate Identifier Sub Unit"));
                    }
                    
                case 'S':
                    if (TextUtil.isEmptyOrSpaces(this.getAffIdCouncil()) && this.getAffIdType().charValue() == 'S') {
                        errors.add("affIdCouncil", new ActionError("error.affiliate.conditional.council"));
                        break;
                    } // else drop into next case and test those rules...                    
                case 'L':
                    if (TextUtil.isEmptyOrSpaces(this.getAffIdLocal())) {
                        errors.add("affIdLocal", new ActionError("error.affiliate.conditional.local"));
                    } else if (!TextUtil.isInt(this.getAffIdLocal())) {
                        errors.add("affIdLocal", new ActionError("error.field.mustBeInt.generic", "Affiliate Identifier Local/Sub Chapter"));
                    } 
                    /* if Local is unaffiliated, break there since no need to 
                     * check the Council Number field
                     */
                    if (TextUtil.isEmptyOrSpaces(this.getAffIdCouncil())) {
                        break;
                    } // else drop into next case and test the isInt rule...
                case 'R':
                case 'C':
                    if (TextUtil.isEmptyOrSpaces(this.getAffIdCouncil())) {
                        errors.add("affIdCouncil", new ActionError("error.affiliate.conditional.council"));
                    } else if (!TextUtil.isInt(this.getAffIdCouncil())) {
                        errors.add("affIdCouncil", new ActionError("error.field.mustBeInt.generic", "Affiliate Identifier Council/Retiree Chapter"));
                    } 
                    break;
                default:
                    // should never happen...
                    break;
            }
        }
        if (TextUtil.isEmptyOrSpaces(this.getAffIdState())) {
            errors.add("affIdState", new ActionError("error.field.required.generic", "Affiliate Identifier State"));
        }
        if (TextUtil.isEmptyOrSpaces(this.getAffilName())) {
            errors.add("affilName", new ActionError("error.field.required.generic", "Abbreviated Affiliate Name"));
        }
        if (this.getAffilStatus() == null) {
            errors.add("affilStatus", new ActionError("error.field.required.generic", "Affiliate Status"));
        } else {
            // make sure status is valid for each type
            if (!TextUtil.isEmptyOrSpaces(this.getAffIdType())) {
                switch (this.getAffIdType().charValue()) {
                    case 'C':
                        if (!CollectionUtil.contains(VALID_COUNCIL_STATUS, this.getAffilStatus())) {
                            errors.add("affilStatus", new ActionError("error.affiliate.invalidStatus", "Council"));
                        }
                        break;
                    case 'R':
                        if (!CollectionUtil.contains(VALID_RETIREECHAPTER_STATUS, this.getAffilStatus())) {
                            errors.add("affilStatus", new ActionError("error.affiliate.invalidStatus", "Retiree Chapter"));
                        }
                        break;
                    case 'L':
                        if (!CollectionUtil.contains(VALID_LOCAL_STATUS, this.getAffilStatus())) {
                            errors.add("affilStatus", new ActionError("error.affiliate.invalidStatus", "Local"));
                        }
                        break;
                    case 'S':
                        if (!CollectionUtil.contains(VALID_SUBCHAPTER_STATUS, this.getAffilStatus())) {
                            errors.add("affilStatus", new ActionError("error.affiliate.invalidStatus", "Sub Chapter"));
                        }
                        break;
                    case 'U':
                        if (!CollectionUtil.contains(VALID_SUBLOCAL_STATUS, this.getAffilStatus())) {
                            errors.add("affilStatus", new ActionError("error.affiliate.invalidStatus", "Sub Local"));
                        }
                        break;
                    default:
                        // should never happen...
                }
            }
        }
        if (this.getAfscmeRegion() == null) {
            errors.add("afscmeRegion", new ActionError("error.field.required.generic", "AFSCME Region"));
        }
        if (this.getAnnualCardRunType() == null) {
            errors.add("annualCardRunType", new ActionError("error.field.required.generic", "Annual Card Run Type"));
        }
        
        if (this.getAffIdType() != null
        && !AffiliateStatus.N.equals(this.getAffilStatus()) 
        && !AffiliateStatus.PC.equals(this.getAffilStatus())
        &&  this.getAffIdType().charValue() != 'U')        
         {
            if (TextUtil.isEmptyOrSpaces(this.getCharterName())) {
                errors.add("charterName", new ActionError("error.affiliate.conditional.charter"));
            }           
            if (TextUtil.isEmptyOrSpaces(this.getCharterJurisdiction())) {
                errors.add("charterJurisdiction", new ActionError("error.affiliate.conditional.jurisdiction"));
            }
            if (this.getCharterCode() == null) {
                errors.add("charterCode", new ActionError("error.affiliate.conditional.chartercode"));
            }
            if (TextUtil.isEmptyOrSpaces(this.getCharterCounty1()) &&
                TextUtil.isEmptyOrSpaces(this.getCharterCounty2()) && 
                TextUtil.isEmptyOrSpaces(this.getCharterCounty3()))   {
                errors.add("charterCounty1", new ActionError("error.charter.required.one.county"));
            }
            if  (this.getCharterDateMonth() == null || TextUtil.isEmptyOrSpaces(this.getCharterDateYear())) {          
                errors.add("charterDateMonth", new ActionError("error.affiliate.conditional.charterdate"));
            }            
        }      
        if (!TextUtil.isEmptyOrSpaces(this.getCharterDateYear()) &&(!TextUtil.isInt(this.getCharterDateYear()))) {
            errors.add("charterDateYear", new ActionError("error.affiliate.charterdate.numeric", "Charter Year"));
        }        

        logger.debug("    ---- returning " + errors.size() + " error(s).");
        return errors;
    }
    
    /**
     * Evaluates if a parentFk should have been found based on the AffiliateIdentifier 
     * fields that have values entered. If the appropriate fields were not completed, 
     * then this method will return false. 
     */
    private boolean shouldHaveParent() {
        if (TextUtil.isEmptyOrSpaces(this.getAffIdType())) {
            return false;
        }
        switch (this.getAffIdType().charValue()) {
            case 'L':
            case 'S':
                return !TextUtil.isEmptyOrSpaces(this.getAffIdCouncil()) && !TextUtil.isEmptyOrSpaces(this.getAffIdState());
            case 'U':
                return !TextUtil.isEmptyOrSpaces(this.getAffIdLocal()) && !TextUtil.isEmptyOrSpaces(this.getAffIdState());
            default:
                return false;
        }
    }
    
    private boolean hasParent() {
        return (this.getParentFk() != null && this.getParentFk().intValue() > 0);
    }
    
// General Methods...
    
    protected void init() {
        this.affPk = null;
        this.affIdType = null;
        this.affIdLocal = null;
        this.affIdState = null;
        this.affIdSubUnit = null;
        this.affIdCouncil = null;
        this.affIdCode = null;
        this.affilName = null;
        this.affilStatus = null;
        this.annualCardRunType = null;
        this.afscmeLegislativeDistrict = null;
        this.afscmeRegion = null;
        this.multipleEmployers = false;
        this.subLocalsAllowed = false;
        this.charterName = null;
        this.charterJurisdiction = null;
        this.charterCode = null;
        this.charterDateMonth = null;
        this.charterDateYear = null;
        this.charterCounty1 = null;
        this.charterCounty2 = null;
        this.charterCounty3 = null;
        this.approvedConstitution = false;
        this.generateDefaultOffices = false;
        this.affilAgreementDateMonth = null;
        this.affilAgreementDateYear = null;
        this.effectiveDateMonth = null;
        this.effectiveDateYear = null;
        this.parentFk = null;
        this.createdBy = null;
        this.createdDate = null;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", affIdType = ");
        sb.append(this.affIdType);
        sb.append(", affIdLocal = ");
        sb.append(this.affIdLocal);
        sb.append(", affIdState = ");
        sb.append(this.affIdState);
        sb.append(", affIdSubUnit = ");
        sb.append(this.affIdSubUnit);
        sb.append(", affIdCouncil = ");
        sb.append(this.affIdCouncil);
        sb.append(", affIdCode = ");
        sb.append(this.affIdCode);
        sb.append(", affilName = ");
        sb.append(this.affilName);
        sb.append(", affilStatus = ");
        sb.append(this.affilStatus);
        sb.append(", annualCardRunType = ");
        sb.append(this.annualCardRunType);
        sb.append(", afscmeLegislativeDistrict = ");
        sb.append(this.afscmeLegislativeDistrict);
        sb.append(", afscmeRegion = ");
        sb.append(this.afscmeRegion);
        sb.append(", multipleEmployers = ");
        sb.append(this.multipleEmployers);
        sb.append(", subLocalsAllowed = ");
        sb.append(this.subLocalsAllowed);
        sb.append(", charterName = ");
        sb.append(this.charterName);
        sb.append(", charterJurisdiction = ");
        sb.append(this.charterJurisdiction);
        sb.append(", charterCode = ");
        sb.append(this.charterCode);
        sb.append(", charterDateMonth = ");
        sb.append(this.charterDateMonth);
        sb.append(", charterDateYear = ");
        sb.append(this.charterDateYear);
        sb.append(", charterCounty1 = ");
        sb.append(this.charterCounty1);
        sb.append(", charterCounty2 = ");
        sb.append(this.charterCounty2);
        sb.append(", charterCounty3 = ");
        sb.append(this.charterCounty3);
        sb.append(", approvedConstitution = ");
        sb.append(this.approvedConstitution);
        sb.append(", generateDefaultOffices = ");
        sb.append(this.generateDefaultOffices);
        sb.append(", affilAgreementDateMonth = ");
        sb.append(this.affilAgreementDateMonth);
        sb.append(", affilAgreementDateYear = ");
        sb.append(this.affilAgreementDateYear);
        sb.append(", effectiveDateMonth = ");
        sb.append(this.effectiveDateMonth);
        sb.append(", effectiveDateYear = ");
        sb.append(this.effectiveDateYear);
        sb.append(", parentFk = ");
        sb.append(this.parentFk);
        sb.append(", createdBy = ");
        sb.append(this.createdBy);
        sb.append(", createdDate = ");
        sb.append(this.createdDate);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    public NewAffiliate getNewAffiliate() {
        NewAffiliate newAffil = new NewAffiliate();
        AffiliateIdentifier affId = new AffiliateIdentifier();
        affId.setCouncil(this.getAffIdCouncil());
        affId.setLocal(this.getAffIdLocal());
        affId.setState(this.getAffIdState());
        affId.setSubUnit(this.getAffIdSubUnit());
        affId.setType(this.getAffIdType());
        newAffil.setAffiliateId(affId);
        newAffil.setAffiliateName(this.getAffilName());
        newAffil.setAffiliateStatusCodePk(this.getAffilStatus());
        newAffil.setAnnualCardRunTypeCodePk(this.getAnnualCardRunType());
        newAffil.setAfscmeLegislativeDistrict(this.getAfscmeLegislativeDistrict());
        newAffil.setAffiliateRegionCodePk(this.getAfscmeRegion());
        newAffil.setMultipleEmployers(new Boolean(this.isMultipleEmployers()));
        newAffil.setAllowSubLocals(new Boolean(this.isSubLocalsAllowed()));
        newAffil.setCharterName(this.getCharterName());
        newAffil.setCharterJurisdiction(this.getCharterJurisdiction());
        newAffil.setCharterCode(this.getCharterCode());
        if (this.getCharterDateMonth() == null || 
            TextUtil.isEmptyOrSpaces(this.getCharterDateYear()) ||
            !TextUtil.isInt(this.getCharterDateYear())
        ) {
            newAffil.setCharterDate(null);
        } else {
            newAffil.setCharterDate(DateUtil.getTimestamp(
                                        this.getCharterDateMonth().intValue(), 
                                        Integer.parseInt(this.getCharterDateYear()), 
                                        false
                                   )
            );
        }
        newAffil.addCounty(this.getCharterCounty1());
        newAffil.addCounty(this.getCharterCounty2());
        newAffil.addCounty(this.getCharterCounty3());
        newAffil.setApprovedConstitution(new Boolean(this.isApprovedConstitution()));
        if (this.getEffectiveDateMonth() == null || 
            TextUtil.isEmptyOrSpaces(this.getEffectiveDateYear())
        ) {
            newAffil.setEffectiveDate(null);
        } else {
            newAffil.setEffectiveDate(DateUtil.getTimestamp(this.getEffectiveDateMonth().intValue(), 
                                            Integer.parseInt(this.getEffectiveDateYear()), 
                                            false
                                     )
            );
        }
        newAffil.setGenerateDefaultOffices(new Boolean(this.isGenerateDefaultOffices()));
        if (this.getAffilAgreementDateMonth() == null || 
            TextUtil.isEmptyOrSpaces(this.getAffilAgreementDateYear())
        ) {
            newAffil.setAffiliateAgreementDate(null);
        } else {
            newAffil.setAffiliateAgreementDate(DateUtil.getTimestamp(
                                                this.getAffilAgreementDateMonth().intValue(), 
                                                Integer.parseInt(this.getAffilAgreementDateYear()), 
                                                false
                                              )
                            
            );
        }
        newAffil.setParentAffPk(this.getParentFk());
        
        return newAffil;
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
        this.affIdCouncil = affIdCouncil;
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
        this.affIdLocal = affIdLocal;
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
        this.affIdSubUnit = affIdSubUnit;
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
            this.affIdType = null;
        } else {
            this.affIdType = affIdType;
        }
    }
    
    /** Getter for property affilAgreementDateMonth.
     * @return Value of property affilAgreementDateMonth.
     *
     */
    public Integer getAffilAgreementDateMonth() {
        return affilAgreementDateMonth;
    }
    
    /** Setter for property affilAgreementDateMonth.
     * @param affilAgreementDateMonth New value of property affilAgreementDateMonth.
     *
     */
    public void setAffilAgreementDateMonth(Integer affilAgreementDateMonth) {
        if (affilAgreementDateMonth != null && affilAgreementDateMonth.intValue() == 0) {
            this.affilAgreementDateMonth = null;
        } else {
            this.affilAgreementDateMonth = affilAgreementDateMonth;
        }
    }
    
    /** Getter for property affilAgreementDateYear.
     * @return Value of property affilAgreementDateYear.
     *
     */
    public String getAffilAgreementDateYear() {
        return affilAgreementDateYear;
    }
    
    /** Setter for property affilAgreementDateYear.
     * @param affilAgreementDateYear New value of property affilAgreementDateYear.
     *
     */
    public void setAffilAgreementDateYear(String affilAgreementDateYear) {
        if (TextUtil.isEmptyOrSpaces(affilAgreementDateYear)) {
            this.affilAgreementDateYear = null;
        } else {
            this.affilAgreementDateYear = affilAgreementDateYear;
        }
    }
    
    /** Getter for property affilName.
     * @return Value of property affilName.
     *
     */
    public String getAffilName() {
        return affilName;
    }
    
    /** Setter for property affilName.
     * @param affilName New value of property affilName.
     *
     */
    public void setAffilName(String affilName) {
        if (TextUtil.isEmptyOrSpaces(affilName)) {
            this.affilName = null;
        } else {
            this.affilName = affilName;
        }
    }
    
    /** Getter for property affilStatus.
     * @return Value of property affilStatus.
     *
     */
    public Integer getAffilStatus() {
        return affilStatus;
    }
    
    /** Setter for property affilStatus.
     * @param affilStatus New value of property affilStatus.
     *
     */
    public void setAffilStatus(Integer affilStatus) {
        if (affilStatus != null && affilStatus.intValue() < 1) {
            this.affilStatus = null;
        } else {
            this.affilStatus = affilStatus;
        }
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
        if (affPk != null && affPk.intValue() < 1) {
            this.affPk = null;
        } else {
            this.affPk = affPk;
        }
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
    
    /** Getter for property approvedConstitution.
     * @return Value of property approvedConstitution.
     *
     */
    public boolean isApprovedConstitution() {
        return approvedConstitution;
    }
    
    /** Setter for property approvedConstitution.
     * @param approvedConstitution New value of property approvedConstitution.
     *
     */
    public void setApprovedConstitution(boolean approvedConstitution) {
        this.approvedConstitution = approvedConstitution;
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
        if (charterCode != null && charterCode.intValue() < 1) {
            this.charterCode = null;
        } else {
            this.charterCode = charterCode;
        }
    }
    
    /** Getter for property charterCounty1.
     * @return Value of property charterCounty1.
     *
     */
    public String getCharterCounty1() {
        return charterCounty1;
    }
    
    /** Setter for property charterCounty1.
     * @param charterCounty1 New value of property charterCounty1.
     *
     */
    public void setCharterCounty1(String charterCounty1) {
        if (TextUtil.isEmptyOrSpaces(charterCounty1)) {
            this.charterCounty1 = null;
        } else {
            this.charterCounty1 = charterCounty1;
        }
    }
    
    /** Getter for property charterCounty2.
     * @return Value of property charterCounty2.
     *
     */
    public String getCharterCounty2() {
        return charterCounty2;
    }
    
    /** Setter for property charterCounty2.
     * @param charterCounty2 New value of property charterCounty2.
     *
     */
    public void setCharterCounty2(String charterCounty2) {
        if (TextUtil.isEmptyOrSpaces(charterCounty2)) {
            this.charterCounty2 = null;
        } else {
            this.charterCounty2 = charterCounty2;
        }
    }
    
    /** Getter for property charterCounty3.
     * @return Value of property charterCounty3.
     *
     */
    public String getCharterCounty3() {
        return charterCounty3;
    }
    
    /** Setter for property charterCounty3.
     * @param charterCounty3 New value of property charterCounty3.
     *
     */
    public void setCharterCounty3(String charterCounty3) {
        if (TextUtil.isEmptyOrSpaces(charterCounty3)) {
            this.charterCounty3 = null;
        } else {
            this.charterCounty3 = charterCounty3;
        }
    }
    
    /** Getter for property charterDateMonth.
     * @return Value of property charterDateMonth.
     *
     */
    public Integer getCharterDateMonth() {
        return charterDateMonth;
    }
    
    /** Setter for property charterDateMonth.
     * @param charterDateMonth New value of property charterDateMonth.
     *
     */
    public void setCharterDateMonth(Integer charterDateMonth) {
        if (charterDateMonth != null && charterDateMonth.intValue() == 0) {
            this.charterDateMonth = null;
        } else {
            this.charterDateMonth = charterDateMonth;
        }
    }
    
    /** Getter for property charterDateYear.
     * @return Value of property charterDateYear.
     *
     */
    public String getCharterDateYear() {
        return charterDateYear;
    }
    
    /** Setter for property charterDateYear.
     * @param charterDateYear New value of property charterDateYear.
     *
     */
    public void setCharterDateYear(String charterDateYear) {
        if (TextUtil.isEmptyOrSpaces(charterDateYear)) {
            this.charterDateYear = null;
        } else {
            this.charterDateYear = charterDateYear;
        }
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
        if (TextUtil.isEmptyOrSpaces(charterJurisdiction)) {
            this.charterJurisdiction = null;
        } else {
            this.charterJurisdiction = charterJurisdiction;
        }
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
        if (TextUtil.isEmptyOrSpaces(charterName)) {
            this.charterName = null;
        } else {
            this.charterName = charterName;
        }
    }
    
    /** Getter for property createdBy.
     * @return Value of property createdBy.
     *
     */
    public Integer getCreatedBy() {
        return createdBy;
    }
    
    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     *
     */
    public void setCreatedBy(Integer createdBy) {
        if (createdBy != null && createdBy.intValue() < 1) {
            this.createdBy = null;
        } else {
            this.createdBy = createdBy;
        }
    }
    
    /** Getter for property createdDate.
     * @return Value of property createdDate.
     *
     */
    public String getCreatedDate() {
        return createdDate;
    }
    
    /** Setter for property createdDate.
     * @param createdDate New value of property createdDate.
     *
     */
    public void setCreatedDate(String createdDate) {
        if (TextUtil.isEmptyOrSpaces(createdDate)) {
            this.createdDate = null;
        } else {
            this.createdDate = createdDate;
        }
    }
    
    /** Getter for property effectiveDateMonth.
     * @return Value of property effectiveDateMonth.
     *
     */
    public Integer getEffectiveDateMonth() {
        return effectiveDateMonth;
    }
    
    /** Setter for property effectiveDateMonth.
     * @param effectiveDateMonth New value of property effectiveDateMonth.
     *
     */
    public void setEffectiveDateMonth(Integer effectiveDateMonth) {
        if (effectiveDateMonth != null && effectiveDateMonth.intValue() == 0) {
            this.effectiveDateMonth = null;
        } else {
            this.effectiveDateMonth = effectiveDateMonth;
        }
    }
    
    /** Getter for property effectiveDateYear.
     * @return Value of property effectiveDateYear.
     *
     */
    public String getEffectiveDateYear() {
        return effectiveDateYear;
    }
    
    /** Setter for property effectiveDateYear.
     * @param effectiveDateYear New value of property effectiveDateYear.
     *
     */
    public void setEffectiveDateYear(String effectiveDateYear) {
        if (TextUtil.isEmptyOrSpaces(effectiveDateYear)) {
            this.effectiveDateYear = null;
        } else {
            this.effectiveDateYear = effectiveDateYear;
        }
    }
    
    /** Getter for property generateDefaultOffices.
     * @return Value of property generateDefaultOffices.
     *
     */
    public boolean isGenerateDefaultOffices() {
        return generateDefaultOffices;
    }
    
    /** Setter for property generateDefaultOffices.
     * @param generateDefaultOffices New value of property generateDefaultOffices.
     *
     */
    public void setGenerateDefaultOffices(boolean generateDefaultOffices) {
        this.generateDefaultOffices = generateDefaultOffices;
    }
    
    /** Getter for property multipleEmployers.
     * @return Value of property multipleEmployers.
     *
     */
    public boolean isMultipleEmployers() {
        return multipleEmployers;
    }
    
    /** Setter for property multipleEmployers.
     * @param multipleEmployers New value of property multipleEmployers.
     *
     */
    public void setMultipleEmployers(boolean multipleEmployers) {
        this.multipleEmployers = multipleEmployers;
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
        if (parentFk != null && parentFk.intValue() < 1) {
            this.parentFk = null;
        } else {
            this.parentFk = parentFk;
        }
    }
    
    /** Getter for property subLocalsAllowed.
     * @return Value of property subLocalsAllowed.
     *
     */
    public boolean isSubLocalsAllowed() {
        return subLocalsAllowed;
    }
    
    /** Setter for property subLocalsAllowed.
     * @param subLocalsAllowed New value of property subLocalsAllowed.
     *
     */
    public void setSubLocalsAllowed(boolean subLocalsAllowed) {
        this.subLocalsAllowed = subLocalsAllowed;
    }
    
    /** Getter for property annualCardRunType.
     * @return Value of property annualCardRunType.
     *
     */
    public Integer getAnnualCardRunType() {
        return annualCardRunType;
    }
    
    /** Setter for property annualCardRunType.
     * @param annualCardRunType New value of property annualCardRunType.
     *
     */
    public void setAnnualCardRunType(Integer annualCardRunType) {
        if (annualCardRunType != null && annualCardRunType.intValue() < 1) {
            this.annualCardRunType = null;
        } else {
            this.annualCardRunType = annualCardRunType;
        }
    }
    
}
