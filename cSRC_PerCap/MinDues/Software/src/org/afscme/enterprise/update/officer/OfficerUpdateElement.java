package org.afscme.enterprise.update.officer;

import org.afscme.enterprise.update.PersonUpdateElement;
import org.afscme.enterprise.update.AddressElement;
import java.sql.Timestamp;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.common.*;
/**
 * Determinted by the Affiliate/Officer@AffilaiteMbrNum attribute in the update 
 * XML document.
 * 
 * Stored to Affiliate_Member.mbr_no_local
 */
public class OfficerUpdateElement extends PersonUpdateElement {
    
    /**
     * Determinted by the Affiliate/Officer@OfficerTitle attribute in the update XML 
     * document.
     * 
     * Corresponds to AFSCME_Offices.office_pk
     */
    protected Integer title;
    protected String titleDesc = "";
    
    /**
     * Determinted by the Affiliate/Officer@TermExpiration attribute in the update XML 
     * document.
     * 
     * Corresponds to Officer_History.pos_expiration_dt
     */
    //***********************************************************************************
    //Originally declared as timestamp but changed to a string as the month may not be available 
    //protected Timestamp termExpiration;
    protected String termExpiration;
    //***********************************************************************************
    /**
     * Determinted by the Affiliate/Officer/PositionAt-AffiliateIdentifier element or 
     * Affiliate/Officer/Home-AffiliateIdentifier element in the XML document.
     * 
     * Corresponds to Officer_History.aff_pk
     */
    protected Integer positionAt;
    //***********************************************************************************
    /**
     * Determinted by the Affiliate/Officer/TransactionType attribute  
     *  the XML document.
     * 
     * 
     */
    protected String transactionType;
    //************************************************************************************
    //***********************************************************************************
    /**
     * indicates if the value is changed in the db  
     *  
     * 
     * 
     */
    protected boolean updated;
    
    
    protected Integer affPk;
    protected Integer homeAffPk;
    
    //**************************************************************************************
    protected AddressElement addressElement;
    protected PhoneData phoneData;
    
    /** Getter for property addressElement.
     * @return Value of property addressElement.
     *
     */
    public AddressElement getAddressElement() {
        return addressElement;
    }
    
    /** Setter for property addressElement.
     * @param addressElement New value of property addressElement.
     *
     */
    public void setAddressElement(AddressElement addressElement) {
        this.addressElement = addressElement;
    }
    
    /** Getter for property PhoneData.
     * @return Value of property PhoneData.
     *
     */
    public PhoneData getPhoneData() {
        return phoneData;
    }
    
    /** Setter for property PhoneData.
     * @param phoneData New value of property PhoneData.
     *
     */
    public void setPhoneData(PhoneData phoneData) {
        this.phoneData = phoneData;
    }
    /** Getter for property positionAt.
     * @return Value of property positionAt.
     *
     */
    public Integer getPositionAt() {
        return positionAt;
    }
    
    /** Setter for property positionAt.
     * @param positionAt New value of property positionAt.
     *
     */
    public void setPositionAt(Integer positionAt) {
        this.positionAt = positionAt;
    }
    
    /** Getter for property termExpiration.
     * @return Value of property termExpiration.
     *
     */
    public String getTermExpiration() {
        return termExpiration;
    }
    
    /** Setter for property termExpiration.
     * @param termExpiration New value of property termExpiration.
     *
     */
    public void setTermExpiration(String termExpiration) {
        this.termExpiration = termExpiration;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     *
     */
    public Integer getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     *
     */
    public void setTitle(Integer title) {
        this.title = title;
    }
    /** Getter for property getTitleDesc.
     * @return Value otitleDescf property getTitleDesc.
     *
     */
    public String getTitleDesc() {
        return titleDesc;
    }
    
    /** Setter for property titleDesc.
     * @param titleDesc New value of property getTitleDesc.
     *
     */
    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }
    //*********************************************************************************************
     /** Getter for property termExpiration.
     * @return Value of property termExpiration.
     *
     */
    public String getTransactionType() {
        return transactionType;
    }
    
    /** Setter for property termExpiration.
     * @param termExpiration New value of property termExpiration.
     *
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    /** Getter for property updated.
     * @return Value of property updated.
     *
     */
    public boolean getUpdated() {
        return updated;
    }
    
    /** Setter for property updated.
     * @param updated New value of property updated.
     *
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
    

    public void setAffPk(Integer affPk){
        this.affPk = affPk;
    }
    
    public Integer getAffPk(){
        return this.affPk;
    }
    
    public void setHomeAffPk(Integer affPk){
        this.homeAffPk = affPk;
    }
    
    public Integer getHomeAffPk(){
        return this.homeAffPk;
    }    
    //*****************************************************************************************
}
