package org.afscme.enterprise.masschange;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.codes.Codes.AffiliateStatus;

/**
 * Data needed to perform a Mass Change.
 */
public class MassChangeData {
    
    private static final String CHANGE_TYPE_STATUS_DEACTIVATED = "SD";
    private static final String CHANGE_TYPE_STATUS_MERGED = "SM";
    private static final String CHANGE_TYPE_STATUS_SPLIT = "SP";
    private static final String CHANGE_TYPE_NEW_AFFILIATE_ID = "NA";
    private static final String CHANGE_TYPE_MEMBER_RENEWAL = "MR";
    private static final String CHANGE_TYPE_UNIT_WIDE_NO_CARDS = "NC";
    private static final String CHANGE_TYPE_UNIT_WIDE_NO_PE_MAIL = "NP";
    private static final String CHANGE_TYPE_MBRSP_INFO_RPT_SRC = "IS";
    
    private Integer newAffPk;
    
    /** Indicates the type of Mass Change. */
    private Integer massChangeType;
    
    /** Used in the case of a Mass Change involving a Status value. */
    private Integer statusChangeType;
    
    /**
     * Used in the case of a Mass Change involving an Affiliate Identifier value. 
     * Currently used for 'New Affiliate Identifier', 'Set to Merged', and 'Set to 
     * Split' changes.
     */
    private AffiliateIdentifier newAffiliateID;
    
    /**
     * Used in the case of a Mass Change involving a checkbox value. True means 
     * checked, and false means unchecked. Currently used for 'Unit Wide No Member 
     * Cards', 'Unit Wide No PE Mail', and 'Member Renewal' changes.
     */
    private Boolean newFlag;
    
    /**
     * Used in the case of a Mass Change involving a dropdown value. Int is the key to 
     * the common code. Currently only used for Membership Information Reporting 
     * Source change.
     */
    private Integer newSelect;
    
// Getter and Setter Methods...
    
    /** Getter for property massChangeType.
     * @return Value of property massChangeType.
     *
     */
    public Integer getMassChangeType() {
        return massChangeType;
    }
    
    /** Setter for property massChangeType.
     * @param massChangeType New value of property massChangeType.
     *
     */
    public void setMassChangeType(Integer massChangeType) {
        this.massChangeType = massChangeType;
    }
    
    /** Getter for property newAffiliateID.
     * @return Value of property newAffiliateID.
     *
     */
    public AffiliateIdentifier getNewAffiliateID() {
        return newAffiliateID;
    }
    
    /** Setter for property newAffiliateID.
     * @param newAffiliateID New value of property newAffiliateID.
     *
     */
    public void setNewAffiliateID(AffiliateIdentifier newAffiliateID) {
        this.newAffiliateID = newAffiliateID;
    }
    
    /** Getter for property newFlag.
     * @return Value of property newFlag.
     *
     */
    public Boolean getNewFlag() {
        return newFlag;
    }
    
    /** Setter for property newFlag.
     * @param newFlag New value of property newFlag.
     *
     */
    public void setNewFlag(Boolean newFlag) {
        this.newFlag = newFlag;
    }
    
    /** Getter for property newSelect.
     * @return Value of property newSelect.
     *
     */
    public Integer getNewSelect() {
        return newSelect;
    }
    
    /** Setter for property newSelect.
     * @param newSelect New value of property newSelect.
     *
     */
    public void setNewSelect(Integer newSelect) {
        this.newSelect = newSelect;
    }
    
    /** Getter for property statusChangeType.
     * @return Value of property statusChangeType.
     *
     */
    public Integer getStatusChangeType() {
        return statusChangeType;
    }
    
    /** Setter for property statusChangeType.
     * @param statusChangeType New value of property statusChangeType.
     *
     */
    public void setStatusChangeType(Integer statusChangeType) {
        this.statusChangeType = statusChangeType;
    }
    
    /** Getter for property newAffPk.
     * @return Value of property newAffPk.
     *
     */
    public java.lang.Integer getNewAffPk() {
        return newAffPk;
    }
    
    /** Setter for property newAffPk.
     * @param newAffPk New value of property newAffPk.
     *
     */
    public void setNewAffPk(java.lang.Integer newAffPk) {
        this.newAffPk = newAffPk;
    }
    
}
