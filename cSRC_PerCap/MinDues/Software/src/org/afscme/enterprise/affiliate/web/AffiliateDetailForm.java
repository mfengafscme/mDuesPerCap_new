package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.organization.LocationData;
import org.apache.log4j.Logger;

/**
 * @struts:form name="affiliateDetailForm"
 */
public class AffiliateDetailForm extends org.apache.struts.action.ActionForm {
    
    private static Logger logger =  Logger.getLogger(AffiliateDetailForm.class);       
    
    private Integer affPk;
    private Integer parentFk;
    private Character affIdType;
    private String affIdLocal;
    private String affIdState;
    private String affIdSubUnit;
    private String affIdCouncil;
    private String affIdAdminLegisCouncil;
    private Character affIdCode;
    private String affilName;
    private Integer affilStatus;
    private Integer afscmeLegislativeDistrict;
    private Integer afscmeRegion;
    private boolean multipleEmployers;
    private Integer[] employerSectors;
    private boolean subLocalsAllowed;
    private boolean containsSubLocals;
    private Integer newAffIdPk;
    private Character newAffIdType;
    private String newAffIdLocal;
    private String newAffIdState;
    private String newAffIdSubUnit;
    private String newAffIdCouncil;
    private Character newAffIdCode;
    private boolean multipleOffices;
    private Integer annualCardRunType;
    private boolean annualCardRunPerformed;
    private Integer memberRenewal;
    private String website;
    private Integer locationPk;
    private LocationData locationData;
    private String comment;
    private Integer createdBy;
    private String createdDate;
    private Integer modifiedBy;
    private String modifiedDate;
    
    /** Creates a new instance of AffiliateDetailForm */
    public AffiliateDetailForm() {
        this.init();
    }
    
// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmptyOrSpaces(this.getAffilName())) {
            errors.add("affilName", new ActionError("error.field.required.generic", "Abbreviated Affiliate Name"));
        }
        if (this.getAfscmeRegion() == null) {
            errors.add("afscmeRegion", new ActionError("error.field.required.generic", "AFSCME Region"));
        }
        if (this.getAnnualCardRunType() == null) {
            errors.add("annualCardRunType", new ActionError("error.field.required.generic", "Annual Card Run Type"));
        }
        if (this.isContainsSubLocals() && !this.isSubLocalsAllowed()) {
            errors.add("subLocalsAllowed", new ActionError("error.affiliate.hasSubLocals"));
        }
// DB : added to fix bug AFSM200000108
        if (this.getMemberRenewal() == null && (this.getAffIdType().charValue() == 'R' || this.getAffIdType().charValue() == 'S')) {
            errors.add("memberRenewal", new ActionError("error.affiliate.renewal.required"));
        }
// DB : end add
        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }
    
// General Methods...
    
    protected void init() {
        this.affPk = null;
        this.parentFk = null;
        this.affIdType = null;
        this.affIdLocal = null;
        this.affIdState = null;
        this.affIdSubUnit = null;
        this.affIdCouncil = null;
        this.affIdAdminLegisCouncil = null;
        this.affIdCode = null;
        this.affilName = null;
        this.affilStatus = null;
        this.afscmeLegislativeDistrict = null;
        this.afscmeRegion = null;
        this.multipleEmployers = false;
        this.employerSectors = null;
        this.subLocalsAllowed = false;
        this.containsSubLocals = false;
        this.newAffIdPk = null;
        this.newAffIdType = null;
        this.newAffIdLocal = null;
        this.newAffIdState = null;
        this.newAffIdSubUnit = null;
        this.newAffIdCouncil = null;
        this.newAffIdCode = null;
        this.multipleOffices = false;
        this.annualCardRunType = null;
        this.annualCardRunPerformed = false;
        this.memberRenewal = null;
        this.website = null;
        this.locationPk = null;
        this.locationData = null;
        this.comment = null;
        this.createdBy = null;
        this.createdDate = null;
        this.modifiedBy = null;
        this.modifiedDate = null;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", parentFk = ");
        sb.append(this.parentFk);
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
        sb.append(", afscmeLegislativeDistrict = ");
        sb.append(this.afscmeLegislativeDistrict);
        sb.append(", afscmeRegion = ");
        sb.append(this.afscmeRegion);
        sb.append(", multipleEmployers = ");
        sb.append(this.multipleEmployers);
        sb.append(", employerSectors = ");
        sb.append(CollectionUtil.toString(this.employerSectors));
        sb.append(", subLocalsAllowed = ");
        sb.append(this.subLocalsAllowed);
        sb.append(", containsSubLocals = ");
        sb.append(this.containsSubLocals);
        sb.append(", newAffIdPk = ");
        sb.append(this.newAffIdPk);
        sb.append(", newAffIdType = ");
        sb.append(this.newAffIdType);
        sb.append(", newAffIdLocal = ");
        sb.append(this.newAffIdLocal);
        sb.append(", newAffIdState = ");
        sb.append(this.newAffIdState);
        sb.append(", newAffIdSubUnit = ");
        sb.append(this.newAffIdSubUnit);
        sb.append(", newAffIdCouncil = ");
        sb.append(this.newAffIdCouncil);
        sb.append(", multipleOffices = ");
        sb.append(this.multipleOffices);
        sb.append(", annualCardRunType = ");
        sb.append(this.annualCardRunType);
        sb.append(", annualCardRunPerformed = ");
        sb.append(this.annualCardRunPerformed);
        sb.append(", memberRenewal = ");
        sb.append(this.memberRenewal);
        sb.append(", website = ");
        sb.append(this.website);
        sb.append(", locationPk = ");
        sb.append(this.locationPk);
        sb.append(", comment = ");
        sb.append(this.comment);
        sb.append(", createdBy = ");
        sb.append(this.createdBy);
        sb.append(", createdDate = ");
        sb.append(this.createdDate);
        sb.append(", modifiedBy = ");
        sb.append(this.modifiedBy);
        sb.append(", modifiedDate = ");
        sb.append(this.modifiedDate);
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    public AffiliateData getAffiliateData() {
        AffiliateData data = new AffiliateData();
        AffiliateIdentifier affId = new AffiliateIdentifier();
        RecordData rData = new RecordData();
        
        data.setAbbreviatedName(this.getAffilName());
        data.setAffPk(this.getAffPk());
        affId.setCode(this.getAffIdCode());
        affId.setCouncil(this.getAffIdCouncil());
        affId.setLocal(this.getAffIdLocal());
        affId.setState(this.getAffIdState());
        affId.setSubUnit(this.getAffIdSubUnit());
        affId.setType(this.getAffIdType());
        affId.setAdministrativeLegislativeCouncil(this.getAffIdAdminLegisCouncil());
        data.setAffiliateId(affId);
        data.setAfscmeLegislativeDistrictCodePk(this.getAfscmeLegislativeDistrict());
        data.setAfscmeRegionCodePk(this.getAfscmeRegion());
        data.setAllowSubLocals(new Boolean(this.isSubLocalsAllowed()));
        data.setContainsSubLocals(new Boolean(this.isContainsSubLocals()));
        data.setAnnualCardRunPerformed(new Boolean(this.isAnnualCardRunPerformed()));
        data.setAnnualCardRunTypeCodePk(this.getAnnualCardRunType());
        data.setComment(this.getComment());
        if (this.getEmployerSectors() != null) {
            data.setEmployerSector(CollectionUtil.copy(new ArrayList(this.getEmployerSectors().length), this.getEmployerSectors()));
        }
        data.setLocationPk(this.getLocationPk());
        data.setMemberRenewalCodePk(this.getMemberRenewal());
        data.setMultipleEmployers(new Boolean(this.isMultipleEmployers()));
        data.setMultipleOffices(new Boolean(this.isMultipleOffices()));
        logger.debug("     data set as " + data.getMultipleOffices());
        data.setNewAffiliateIDSourcePk(this.getNewAffIdPk());
        data.setParentFk(this.getParentFk());
        rData.setCreatedBy(this.getCreatedBy());
        rData.setCreatedDate(DateUtil.getTimestamp(this.getCreatedDate()));
        rData.setModifiedBy(this.getModifiedBy());
        rData.setModifiedDate(DateUtil.getTimestamp(this.getModifiedDate()));
        data.setRecordData(rData);
        data.setStatusCodePk(this.getAffilStatus());
        data.setWebsite(this.getWebsite());
        
        return data;
    }
    
    public void setAffiliateData(AffiliateData data) {
        if (data.getAffiliateId() != null) {
            this.setAffIdCode(data.getAffiliateId().getCode());
            this.setAffIdCouncil(data.getAffiliateId().getCouncil());
            this.setAffIdLocal(data.getAffiliateId().getLocal());
            this.setAffIdState(data.getAffiliateId().getState());
            this.setAffIdSubUnit(data.getAffiliateId().getSubUnit());
            this.setAffIdType(data.getAffiliateId().getType());
            this.setAffIdAdminLegisCouncil(data.getAffiliateId().getAdministrativeLegislativeCouncil());
        }
        this.setAffPk(data.getAffPk());
        this.setAffilName(data.getAbbreviatedName());
        this.setAffilStatus(data.getStatusCodePk());
        this.setAfscmeLegislativeDistrict(data.getAfscmeLegislativeDistrictCodePk());
        this.setAfscmeRegion(data.getAfscmeRegionCodePk());
        this.setAnnualCardRunPerformed(TextUtil.getPrimitiveBoolean(data.getAnnualCardRunPerformed()));
        this.setAnnualCardRunType(data.getAnnualCardRunTypeCodePk());
        this.setComment(data.getComment());
        this.setCreatedBy(data.getRecordData().getCreatedBy());
        this.setCreatedDate(TextUtil.format(data.getRecordData().getCreatedDate()));
        this.setEmployerSectors(data.getEmployerSector());
        this.setLocationPk(data.getLocationPk());
        this.setMemberRenewal(data.getMemberRenewalCodePk());
        this.setModifiedBy(data.getRecordData().getModifiedBy());
        this.setModifiedDate(TextUtil.format(data.getRecordData().getModifiedDate()));
        this.setMultipleEmployers(TextUtil.getPrimitiveBoolean(data.getMultipleEmployers()));
        this.setMultipleOffices(TextUtil.getPrimitiveBoolean(data.getMultipleOffices()));
        this.setNewAffIdPk(data.getNewAffiliateIDSourcePk());
        this.setParentFk(data.getParentFk());
        this.setSubLocalsAllowed(TextUtil.getPrimitiveBoolean(data.getAllowSubLocals()));
        this.setContainsSubLocals(TextUtil.getPrimitiveBoolean(data.getContainsSubLocals()));
        this.setWebsite(data.getWebsite());
    }
    
    public void setNewAffIdData(AffiliateData data) {
        this.setNewAffIdPk(data.getAffPk());
        if (data.getAffiliateId() != null) {
            this.setNewAffIdCode(data.getAffiliateId().getCode());
            this.setNewAffIdCouncil(data.getAffiliateId().getCouncil());
            this.setNewAffIdLocal(data.getAffiliateId().getLocal());
            this.setNewAffIdState(data.getAffiliateId().getState());
            this.setNewAffIdSubUnit(data.getAffiliateId().getSubUnit());
            this.setNewAffIdType(data.getAffiliateId().getType());
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
    
    /** Getter for property annualCardRunPerformed.
     * @return Value of property annualCardRunPerformed.
     *
     */
    public boolean isAnnualCardRunPerformed() {
        return annualCardRunPerformed;
    }
    
    /** Setter for property annualCardRunPerformed.
     * @param annualCardRunPerformed New value of property annualCardRunPerformed.
     *
     */
    public void setAnnualCardRunPerformed(boolean annualCardRunPerformed) {
        this.annualCardRunPerformed = annualCardRunPerformed;
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
        if (TextUtil.isEmptyOrSpaces(comment)) {
            this.comment = null; 
        } else {
            this.comment = comment;
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
    
    /** Getter for property employerSectors.
     * @return Value of property employerSectors.
     *
     */
    public Integer[] getEmployerSectors() {
        return this.employerSectors;
    }
    
    /** Setter for property employerSectors.
     * @param employerSectors New value of property employerSectors.
     *
     */
    public void setEmployerSectors(Integer[] employerSectors) {
        if (employerSectors != null && employerSectors.length < 1) {
            this.employerSectors = null;
        } else {
            /* Parse out zero values. Use a Collection since we may not be using 
             * the entire array, so the length is not fixed
             */
            Collection c = new ArrayList();
            for (int i = 0; i < employerSectors.length; i++) {
                if (employerSectors[i].intValue() > 0) {
                    c.add(employerSectors[i]);
                }
            }
            if (c.size() == 0) {
                this.employerSectors = null;
            } else {
                this.employerSectors = CollectionUtil.toIntegerArray(c);
            }
        }
    }
    
    /** Setter for property employerSectors.
     * @param employerSectors New value of property employerSectors.
     *
     */
    public void setEmployerSectors(Collection employerSectors) {
        if (employerSectors != null && employerSectors.size() > 0) {
            this.employerSectors = new Integer[employerSectors.size()];
            int i = 0;
            for (Iterator it = employerSectors.iterator(); it.hasNext(); ) {
                Integer es = (Integer)it.next();
                if (es.intValue() > 0) {
                    this.employerSectors[i++] = es;
                }
            }
        } else {
            this.employerSectors = null;
        }
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
        if (locationPk != null && locationPk.intValue() < 1) {
            this.locationPk = null; 
        } else {
            this.locationPk = locationPk;
        }
    }
    
    /** Getter for property memberRenewal.
     * @return Value of property memberRenewal.
     *
     */
    public Integer getMemberRenewal() {
        return memberRenewal;
    }
    
    /** Setter for property memberRenewal.
     * @param memberRenewal New value of property memberRenewal.
     *
     */
    public void setMemberRenewal(Integer memberRenewal) {
        if (memberRenewal != null && memberRenewal.intValue() < 1) {
            this.memberRenewal = null; 
        } else {
            this.memberRenewal = memberRenewal;
        }
    }
    
    /** Getter for property modifiedBy.
     * @return Value of property modifiedBy.
     *
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }
    
    /** Setter for property modifiedBy.
     * @param modifiedBy New value of property modifiedBy.
     *
     */
    public void setModifiedBy(Integer modifiedBy) {
        if (modifiedBy != null && modifiedBy.intValue() < 1) {
            this.modifiedBy = null; 
        } else {
            this.modifiedBy = modifiedBy;
        }
    }
    
    /** Getter for property modifiedDate.
     * @return Value of property modifiedDate.
     *
     */
    public String getModifiedDate() {
        return modifiedDate;
    }
    
    /** Setter for property modifiedDate.
     * @param modifiedDate New value of property modifiedDate.
     *
     */
    public void setModifiedDate(String modifiedDate) {
        if (TextUtil.isEmptyOrSpaces(modifiedDate)) {
            this.modifiedDate = null; 
        } else {
            this.modifiedDate = modifiedDate;
        }
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
    
    /** Getter for property multipleOffices.
     * @return Value of property multipleOffices.
     *
     */
    public boolean isMultipleOffices() {
        return multipleOffices;
    }
    
    /** Setter for property multipleOffices.
     * @param multipleOffices New value of property multipleOffices.
     *
     */
    public void setMultipleOffices(boolean multipleOffices) {
        this.multipleOffices = multipleOffices;
    }
    
    /** Getter for property newAffIdCode.
     * @return Value of property newAffIdCode.
     *
     */
    public Character getNewAffIdCode() {
        return newAffIdCode;
    }
    
    /** Setter for property newAffIdCode.
     * @param newAffIdCode New value of property newAffIdCode.
     *
     */
    public void setNewAffIdCode(Character newAffIdCode) {
        if (TextUtil.isEmptyOrSpaces(newAffIdCode)) {
            this.newAffIdCode = null; 
        } else {
            this.newAffIdCode = newAffIdCode;
        }
    }
    
    /** Getter for property newAffIdCouncil.
     * @return Value of property newAffIdCouncil.
     *
     */
    public String getNewAffIdCouncil() {
        return newAffIdCouncil;
    }
    
    /** Setter for property newAffIdCouncil.
     * @param newAffIdCouncil New value of property newAffIdCouncil.
     *
     */
    public void setNewAffIdCouncil(String newAffIdCouncil) {
        this.newAffIdCouncil = newAffIdCouncil;
    }
    
    /** Getter for property newAffIdLocal.
     * @return Value of property newAffIdLocal.
     *
     */
    public String getNewAffIdLocal() {
        return newAffIdLocal;
    }
    
    /** Setter for property newAffIdLocal.
     * @param newAffIdLocal New value of property newAffIdLocal.
     *
     */
    public void setNewAffIdLocal(String newAffIdLocal) {
        this.newAffIdLocal = newAffIdLocal;
    }
    
    /** Getter for property newAffIdPk.
     * @return Value of property newAffIdPk.
     *
     */
    public Integer getNewAffIdPk() {
        return newAffIdPk;
    }
    
    /** Setter for property newAffIdPk.
     * @param newAffIdPk New value of property newAffIdPk.
     *
     */
    public void setNewAffIdPk(Integer newAffIdPk) {
        if (newAffIdPk != null && newAffIdPk.intValue() < 1) {
            this.newAffIdPk = null; 
        } else {
            this.newAffIdPk = newAffIdPk;
        }
    }
    
    /** Getter for property newAffIdState.
     * @return Value of property newAffIdState.
     *
     */
    public String getNewAffIdState() {
        return newAffIdState;
    }
    
    /** Setter for property newAffIdState.
     * @param newAffIdState New value of property newAffIdState.
     *
     */
    public void setNewAffIdState(String newAffIdState) {
        if (TextUtil.isEmptyOrSpaces(newAffIdState)) {
            this.newAffIdState = null; 
        } else {
            this.newAffIdState = newAffIdState;
        }
    }
    
    /** Getter for property newAffIdSubUnit.
     * @return Value of property newAffIdSubUnit.
     *
     */
    public String getNewAffIdSubUnit() {
        return newAffIdSubUnit;
    }
    
    /** Setter for property newAffIdSubUnit.
     * @param newAffIdSubUnit New value of property newAffIdSubUnit.
     *
     */
    public void setNewAffIdSubUnit(String newAffIdSubUnit) {
        this.newAffIdSubUnit = newAffIdSubUnit;
    }
    
    /** Getter for property newAffIdType.
     * @return Value of property newAffIdType.
     *
     */
    public Character getNewAffIdType() {
        return newAffIdType;
    }
    
    /** Setter for property newAffIdType.
     * @param newAffIdType New value of property newAffIdType.
     *
     */
    public void setNewAffIdType(Character newAffIdType) {
        if (TextUtil.isEmptyOrSpaces(newAffIdType)) {
            this.newAffIdType = null; 
        } else {
            this.newAffIdType = newAffIdType;
        }
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
    
    /** Getter for property affIdAdminLegisCouncil.
     * @return Value of property affIdAdminLegisCouncil.
     *
     */
    public String getAffIdAdminLegisCouncil() {
        return affIdAdminLegisCouncil;
    }
    
    /** Setter for property affIdAdminLegisCouncil.
     * @param affIdAdminLegisCouncil New value of property affIdAdminLegisCouncil.
     *
     */
    public void setAffIdAdminLegisCouncil(String affIdAdminLegisCouncil) {
        this.affIdAdminLegisCouncil = affIdAdminLegisCouncil;
    }
    
    /** Getter for property locationData.
     * @return Value of property locationData.
     *
     */
    public LocationData getLocationData() {
        return locationData;
    }
    
    /** Setter for property locationData.
     * @param locationData New value of property locationData.
     *
     */
    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
    }
    
    /** Getter for property containsSubLocals.
     * @return Value of property containsSubLocals.
     *
     */
    public boolean isContainsSubLocals() {
        return containsSubLocals;
    }
    
    /** Setter for property containsSubLocals.
     * @param containsSubLocals New value of property containsSubLocals.
     *
     */
    public void setContainsSubLocals(boolean containsSubLocals) {
        this.containsSubLocals = containsSubLocals;
    }
    
}
