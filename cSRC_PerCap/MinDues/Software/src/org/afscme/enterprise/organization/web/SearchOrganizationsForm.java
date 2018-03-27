package org.afscme.enterprise.organization.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.organization.OrganizationCriteria;

/**
 * Represents the search parameters form when the user is entering organization search criteria
 *
 * @struts:form name="searchOrganizationsForm"
 */
public class SearchOrganizationsForm extends SearchForm {
    
    private String orgName;
    private String orgType;
    private String orgWebSite;
    
    /**
     * 0 is FALSE, 1 is TRUE, 2 means retrieve both marked for deletion and not marked
     * for deletion organizations
     */
    private int markedForDeletion;
    
    // for PAC
    private String employerType;
    private String divisionAgency;
    
    private String attentionLine;
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String country;
    private String zipPostal;
	private String zipPlus;
    private String county;
    private String province;
    private String lastUpdateDate;
    private String lastUpdateUser;
    
    private String officeCountryCode;
    private String officeAreaCode;
    private String officePhoneNo;
    private String faxCountryCode;
    private String faxAreaCode;
    private String faxPhoneNo;
    
    
    public SearchOrganizationsForm() {
        newSearch();
    }
    
    /** resets the search values to the default */
    public void newSearch() {
        orgName = null;
        orgType = "";
        orgWebSite = "";
        markedForDeletion = 0;
        employerType = "";
        divisionAgency = "";
        attentionLine = "";
        addr1 = "";
        addr2 = "";
        city = "";
        state = "";
        country = "";
        zipPostal = "";
		zipPlus = "";
        county = "";
        province = "";
        lastUpdateDate = "";
        lastUpdateUser = "";
        officeCountryCode = "";
        officeAreaCode = "";
        officePhoneNo = "";
        faxCountryCode = "";
        faxAreaCode = "";
        faxPhoneNo = "";
        
        sortBy= "orgName";
        order=1;
        page=0;
        total=0;
    }
    
    /**
     * getOrganizationCriteriaData method to copy all the form
     * data fields to the criteria object to process.
     */
    public OrganizationCriteria getOrganizationCriteriaData() {
        
        OrganizationCriteria data = new OrganizationCriteria();
        if (!TextUtil.isEmpty(orgName))
            data.setOrgName(orgName);
        if (!TextUtil.isEmpty(orgType))
            data.setOrgType(new Integer(orgType));
        if (!TextUtil.isEmpty(orgWebSite))
            data.setOrgWebSite(orgWebSite);
        data.setMarkedForDeletion(markedForDeletion);

        // need to add PAC fields 
        
        // Addresses search
        data.setAttentionLine(attentionLine);
        data.setAddr1(addr1);
        data.setAddr2(addr2);
        data.setCity(city);
        data.setState(state);
        if (!TextUtil.isEmpty(country))
            data.setCountry(new Integer(country));
        data.setZipPostal(zipPostal);
		data.setZipPlus(zipPlus);
        data.setCounty(county);
        data.setProvince(province);
        try {
            data.setLastUpdateDate(TextUtil.parseDate(lastUpdateDate));
        } catch (Exception e) {
            data.setLastUpdateDate(null);
        }
        data.setLastUpdateUser(lastUpdateUser);
        
        // Phone Numbers search
        if (!TextUtil.isEmpty(officeCountryCode))
            data.setOfficeCountryCode(new Integer(officeCountryCode));
        data.setOfficeAreaCode(officeAreaCode);
        data.setOfficePhoneNo(officePhoneNo);
        if (!TextUtil.isEmpty(faxCountryCode))
            data.setFaxCountryCode(new Integer(faxCountryCode));
        data.setFaxAreaCode(faxAreaCode);
        data.setFaxPhoneNo(faxPhoneNo);
        
        // set page and sort values
        data.setPage(page);
        data.setPageSize(getPageSize());
        
        if (sortBy.equals("orgName"))
            data.setSortField(OrganizationCriteria.FIELD_NAME);
        else if (sortBy.equals("orgType"))
            data.setSortField(OrganizationCriteria.FIELD_TYPE);
        
        data.setSortOrder(order);
        
        return data;
    }
    
    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("orgName: " + orgName);
        buf.append(", orgType: " + orgType);
        buf.append(", orgWebSite: " + orgWebSite);
        buf.append(", employerType: " + employerType); 
        buf.append(", divisionAgency: " + divisionAgency);        
        buf.append(", attentionLine: " + attentionLine);
        buf.append(", addr1: " + addr1);
        buf.append(", addr2: " + addr2);
        buf.append(", city: " + city);
        buf.append(", state: " + state);
        buf.append(", zipPostal: " + zipPostal);
		buf.append(", zipPlus: " + zipPlus);
        buf.append(", county: " + county);
        buf.append(", province: " + province);
        buf.append(", officeCountryCode: " + officeCountryCode);
        buf.append(", officeAreaCode: " + officeAreaCode);
        buf.append(", officePhoneNo: " + officePhoneNo);
        buf.append(", faxCountryCode: " + faxCountryCode);
        buf.append(", faxAreaCode: " + faxAreaCode);
        buf.append(", faxPhoneNo: " + faxPhoneNo);
        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        return buf.toString()+"]";
    }
    
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
    public java.lang.String getCountry() {
        return country;
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     *
     */
    public void setCountry(java.lang.String country) {
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
  
    /** Getter for property divisionAgency.
     * @return Value of property divisionAgency.
     *
     */
    public java.lang.String getDivisionAgency() {
        return divisionAgency;
    }
    
    /** Setter for property divisionAgency.
     * @param divisionAgency New value of property divisionAgency.
     *
     */
    public void setDivisionAgency(java.lang.String divisionAgency) {
        this.divisionAgency = divisionAgency;
    }

    /** Getter for property employerType.
     * @return Value of property employerType.
     *
     */
    public java.lang.String getEmployerType() {
        return employerType;
    }
    
    /** Setter for property employerType.
     * @param employerType New value of property employerType.
     *
     */
    public void setEmployerType(java.lang.String employerType) {
        this.employerType = employerType;
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
    public java.lang.String getFaxCountryCode() {
        return faxCountryCode;
    }
    
    /** Setter for property faxCountryCode.
     * @param faxCountryCode New value of property faxCountryCode.
     *
     */
    public void setFaxCountryCode(java.lang.String faxCountryCode) {
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
    public java.lang.String getLastUpdateDate() {
        return lastUpdateDate;
    }
    
    /** Setter for property lastUpdateDate.
     * @param lastUpdateDate New value of property lastUpdateDate.
     *
     */
    public void setLastUpdateDate(java.lang.String lastUpdateDate) {
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
    public java.lang.String getOfficeCountryCode() {
        return officeCountryCode;
    }
    
    /** Setter for property officeCountryCode.
     * @param officeCountryCode New value of property officeCountryCode.
     *
     */
    public void setOfficeCountryCode(java.lang.String officeCountryCode) {
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
    public java.lang.String getOrgType() {
        return orgType;
    }
    
    /** Setter for property orgType.
     * @param orgType New value of property orgType.
     *
     */
    public void setOrgType(java.lang.String orgType) {
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
	
	/**
     * validation method for this form
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        if (this.orgName == null) {
            //new, ignore
            return null;
        }
        
        ActionErrors errors = new ActionErrors();
        if (allNull()) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noSearchValueEntered"));
            this.orgName = null;  //reset to null
        }
        return errors;
    }
    
    /**
     * allNull() method to check if no search fields have been entered
     */
    public boolean allNull() {
        
        if ((!TextUtil.isEmptyOrSpaces(this.orgName)) ||
        (!TextUtil.isEmptyOrSpaces(this.orgType)) ||
        (!TextUtil.isEmptyOrSpaces(this.orgWebSite)) ||
        (!TextUtil.isEmptyOrSpaces(this.employerType)) ||
        (!TextUtil.isEmptyOrSpaces(this.divisionAgency)) ||        
        (!TextUtil.isEmptyOrSpaces(this.attentionLine)) ||
        (!TextUtil.isEmptyOrSpaces(this.addr1)) ||
        (!TextUtil.isEmptyOrSpaces(this.addr2)) ||
        (!TextUtil.isEmptyOrSpaces(this.city)) ||
        (!TextUtil.isEmptyOrSpaces(this.state)) ||
        (!TextUtil.isEmptyOrSpaces(this.country)) ||
        (!TextUtil.isEmptyOrSpaces(this.zipPostal)) ||
        (!TextUtil.isEmptyOrSpaces(this.zipPlus)) ||		
        (!TextUtil.isEmptyOrSpaces(this.county)) ||
        (!TextUtil.isEmptyOrSpaces(this.province)) ||
        (!TextUtil.isEmptyOrSpaces(this.lastUpdateDate)) ||
        (!TextUtil.isEmptyOrSpaces(this.lastUpdateUser)) ||
        (!TextUtil.isEmptyOrSpaces(this.officeCountryCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.officeAreaCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.officePhoneNo)) ||
        (!TextUtil.isEmptyOrSpaces(this.faxCountryCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.faxAreaCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.faxPhoneNo)) ||
        (this.markedForDeletion != 0))   // if not default No
        {
            return false;
        }
        
        return true;
    }
}



