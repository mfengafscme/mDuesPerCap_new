package org.afscme.enterprise.organization;

import java.io.Serializable;
import java.sql.Timestamp;
import org.afscme.enterprise.util.TextUtil;

/**
 * Contains data used to query for organizations.  Includes information about 
 * which result fields are desired, sort information and paging information.
 */
public class OrganizationCriteria implements Serializable
{
    public String orgName;
    public Integer orgType;
    public String orgWebSite;
    
    /**
     * 0 is FALSE, 1 is TRUE, 2 means retrieve both marked for deletion and not marked 
     * for deletion organizations
     */
    public int markedForDeletion;
    public String attentionLine;
    public String addr1;
    public String addr2;
    public String city;
    public String state;
    public Integer country;
    public String zipPostal;
	public String zipPlus;
    public String county;
    public String province;
    public Timestamp lastUpdateDate;
    public String lastUpdateUser;
    public Integer officeCountryCode;
    public String officeAreaCode;
    public String officePhoneNo;
    public Integer faxCountryCode;
    public String faxAreaCode;
    public String faxPhoneNo;

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
    public static final int FIELD_TYPE = 2;
    public static final int[] SORT_FIELD_IDS = new int[] {
        FIELD_NAME, FIELD_TYPE };

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


    /** Getter for property addr1.
     * @return Value of property addr1.
     *
     */
    public java.lang.String getAddr1() {
        return addr1;
    }
    
    /** Setter for property addr1.
     * @param addr1 New value of property addr1.
     *
     */
    public void setAddr1(java.lang.String addr1) {
        this.addr1 = addr1;
    }
    
    /** Getter for property addr2.
     * @return Value of property addr2.
     *
     */
    public java.lang.String getAddr2() {
        return addr2;
    }
    
    /** Setter for property addr2.
     * @param addr2 New value of property addr2.
     *
     */
    public void setAddr2(java.lang.String addr2) {
        this.addr2 = addr2;
    }
    
    /** Getter for property attentionLine.
     * @return Value of property attentionLine.
     *
     */
    public java.lang.String getAttentionLine() {
        return attentionLine;
    }
    
    /** Setter for property attentionLine.
     * @param attentionLine New value of property attentionLine.
     *
     */
    public void setAttentionLine(java.lang.String attentionLine) {
        this.attentionLine = attentionLine;
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
    
    /** Getter for property faxAreaCode.
     * @return Value of property faxAreaCode.
     *
     */
    public java.lang.String getFaxAreaCode() {
        return faxAreaCode;
    }
    
    /** Setter for property faxAreaCode.
     * @param faxAreaCode New value of property faxAreaCode.
     *
     */
    public void setFaxAreaCode(java.lang.String faxAreaCode) {
        this.faxAreaCode = faxAreaCode;
    }
    
    /** Getter for property faxCountryCode.
     * @return Value of property faxCountryCode.
     *
     */
    public java.lang.Integer getFaxCountryCode() {
        return faxCountryCode;
    }
    
    /** Setter for property faxCountryCode.
     * @param faxCountryCode New value of property faxCountryCode.
     *
     */
    public void setFaxCountryCode(java.lang.Integer faxCountryCode) {
        this.faxCountryCode = faxCountryCode;
    }
    
    /** Getter for property faxPhoneNo.
     * @return Value of property faxPhoneNo.
     *
     */
    public java.lang.String getFaxPhoneNo() {
        return faxPhoneNo;
    }
    
    /** Setter for property faxPhoneNo.
     * @param faxPhoneNo New value of property faxPhoneNo.
     *
     */
    public void setFaxPhoneNo(java.lang.String faxPhoneNo) {
        this.faxPhoneNo = faxPhoneNo;
    }
    
    /** Getter for property lastUpdateDate.
     * @return Value of property lastUpdateDate.
     *
     */
    public java.sql.Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }
    
    /** Setter for property lastUpdateDate.
     * @param lastUpdateDate New value of property lastUpdateDate.
     *
     */
    public void setLastUpdateDate(java.sql.Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    
    /** Getter for property lastUpdateUser.
     * @return Value of property lastUpdateUser.
     *
     */
    public java.lang.String getLastUpdateUser() {
        return lastUpdateUser;
    }
    
    /** Setter for property lastUpdateUser.
     * @param lastUpdateUser New value of property lastUpdateUser.
     *
     */
    public void setLastUpdateUser(java.lang.String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    
    /** Getter for property markedForDeletion.
     * @return Value of property markedForDeletion.
     *
     */
    public int getMarkedForDeletion() {
        return markedForDeletion;
    }
    
    /** Setter for property markedForDeletion.
     * @param markedForDeletion New value of property markedForDeletion.
     *
     */
    public void setMarkedForDeletion(int markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }
    
    /** Getter for property officeAreaCode.
     * @return Value of property officeAreaCode.
     *
     */
    public java.lang.String getOfficeAreaCode() {
        return officeAreaCode;
    }
    
    /** Setter for property officeAreaCode.
     * @param officeAreaCode New value of property officeAreaCode.
     *
     */
    public void setOfficeAreaCode(java.lang.String officeAreaCode) {
        this.officeAreaCode = officeAreaCode;
    }
    
    /** Getter for property officeCountryCode.
     * @return Value of property officeCountryCode.
     *
     */
    public java.lang.Integer getOfficeCountryCode() {
        return officeCountryCode;
    }
    
    /** Setter for property officeCountryCode.
     * @param officeCountryCode New value of property officeCountryCode.
     *
     */
    public void setOfficeCountryCode(java.lang.Integer officeCountryCode) {
        this.officeCountryCode = officeCountryCode;
    }
    
    /** Getter for property officePhoneNo.
     * @return Value of property officePhoneNo.
     *
     */
    public java.lang.String getOfficePhoneNo() {
        return officePhoneNo;
    }
    
    /** Setter for property officePhoneNo.
     * @param officePhoneNo New value of property officePhoneNo.
     *
     */
    public void setOfficePhoneNo(java.lang.String officePhoneNo) {
        this.officePhoneNo = officePhoneNo;
    }
    
    /** Getter for property orgName.
     * @return Value of property orgName.
     *
     */
    public java.lang.String getOrgName() {
        return orgName;
    }
    
    /** Setter for property orgName.
     * @param orgName New value of property orgName.
     *
     */
    public void setOrgName(java.lang.String orgName) {
        this.orgName = orgName;
    }
    
    /** Getter for property orgType.
     * @return Value of property orgType.
     *
     */
    public java.lang.Integer getOrgType() {
        return orgType;
    }
    
    /** Setter for property orgType.
     * @param orgType New value of property orgType.
     *
     */
    public void setOrgType(java.lang.Integer orgType) {
        this.orgType = orgType;
    }
    
    /** Getter for property orgWebSite.
     * @return Value of property orgWebSite.
     *
     */
    public java.lang.String getOrgWebSite() {
        return orgWebSite;
    }
    
    /** Setter for property orgWebSite.
     * @param orgWebSite New value of property orgWebSite.
     *
     */
    public void setOrgWebSite(java.lang.String orgWebSite) {
        this.orgWebSite = orgWebSite;
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
    
    /** Getter for property zipPostal.
     * @return Value of property zipPostal.
     *
     */
    public java.lang.String getZipPostal() {
        return zipPostal;
    }
    
    /** Setter for property zipPostal.
     * @param zipPostal New value of property zipPostal.
     *
     */
    public void setZipPostal(java.lang.String zipPostal) {
        this.zipPostal = zipPostal;
    }

    /** Getter for property zipPlus.
     * @return Value of property zipPlus.
     *
     */
    public java.lang.String getZipPlus() {
        return zipPlus;
    }
    
    /** Setter for property zipPlus.
     * @param zipPostal New value of property zipPlus.
     *
     */
    public void setZipPlus(java.lang.String zipPlus) {
        this.zipPlus = zipPlus;
    }
	

    /** Getter for property page.
     * @return Value of property page.
     */
    public int getPage() {
        return page;
    }

    /** Setter for property page.
     * @param page New value of property page.
     */
    public void setPage(int page) {
        this.page = page;
    }

    /** Getter for property pageSize.
     * @return Value of property pageSize.
     */
    public int getPageSize() {
        return pageSize;
    }

    /** Setter for property pageSize.
     * @param pageSize New value of property pageSize.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /** Getter for property sortField.
     * @return Value of property sortField.
     */
    public int getSortField() {
        return sortField;
    }

    /** Setter for property sortField.
     * @param sortField New value of property sortField.
     */
    public void setSortField(int sortField) {
        this.sortField = sortField;
    }

    /** Getter for property sortOrder.
     * @return Value of property sortOrder.
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /** Setter for property sortOrder.
     * @param sortOrder New value of property sortOrder.
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public boolean hasAddressSearchCriteria() {

        if ((!TextUtil.isEmptyOrSpaces(this.attentionLine)) ||
            (!TextUtil.isEmptyOrSpaces(this.addr1)) ||
            (!TextUtil.isEmptyOrSpaces(this.addr2)) ||
            (!TextUtil.isEmptyOrSpaces(this.city)) ||
            (!TextUtil.isEmptyOrSpaces(this.state)) ||            
            ( this.country != null ) ||
            (!TextUtil.isEmptyOrSpaces(this.zipPostal)) ||
			(!TextUtil.isEmptyOrSpaces(this.zipPlus)) ||
            (!TextUtil.isEmptyOrSpaces(this.county)) ||
            (!TextUtil.isEmptyOrSpaces(this.province)) ||
            ( this.lastUpdateDate != null ) ||
            (!TextUtil.isEmptyOrSpaces(this.lastUpdateUser)))
        {                
               return true;
        }
        return false;
    }
    
    public boolean hasOfficePhoneSearchCriteria() {

        if (( this.officeCountryCode != null) ||
            (!TextUtil.isEmptyOrSpaces(this.officeAreaCode)) ||
            (!TextUtil.isEmptyOrSpaces(this.officePhoneNo)))
        {                
               return true;
        }
        return false;
    }
    
    public boolean hasFaxPhoneSearchCriteria() {

        if (( this.faxCountryCode != null) ||
            (!TextUtil.isEmptyOrSpaces(this.faxAreaCode)) ||
            (!TextUtil.isEmptyOrSpaces(this.faxPhoneNo)))
        {                
               return true;
        }
        return false;
    }        
}
