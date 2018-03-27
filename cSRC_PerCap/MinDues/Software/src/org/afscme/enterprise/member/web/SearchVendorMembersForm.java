package org.afscme.enterprise.member.web;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.MemberCriteria;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.CollectionUtil;
import org.apache.log4j.Logger;

/**
 * Represents the search parameters form when the user is entering vendor member search criteria
 * Result data to be displayed by the VendorMemberSearch.jsp through the SearchVendorMembersAction is
 * stored in the parent classes result attribute
 *
 * @struts:form name="searchVendorMembersForm"
 */
public class SearchVendorMembersForm extends SearchForm {
    
    private static Logger logger =  Logger.getLogger(SearchVendorMembersForm.class);     
    
    private boolean hasCriteria;
    private String personPk; // member number
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String ssn1;
    private String ssn2;
    private String ssn3;
    private String city;
    private String state;
    private String zipCode; 
    private String zipPlus;        
    private Integer affPk;
    private String affCode;
    private String affIdType;
    private String affIdLocal;
    private String affIdState;
    private String affIdSubUnit;
    private String affIdCouncil;
    
    public SearchVendorMembersForm() {
        newSearch();
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        if (request.getParameter("reset") != null) {
            if (request.getParameter("affIdLocal") == null) {
                affIdLocal = null;
            }
            if (request.getParameter("affIdSubUnit") == null) {
                affIdSubUnit = null;
            }
        }
    }    
    
    /** resets the search values to null  */
    public void newSearch() {     
        sortBy= "";
        order=1;
        page=0;
        total=0;
        hasCriteria = false;
        setResults(null);
    }
    
    /**
     * getMemberCriteriaData method to copy all the form
     * data fields to the criteria object to process.
     */
    public MemberCriteria getVendorMemberCriteriaData() {
        
        MemberCriteria data = new MemberCriteria();
        if (!TextUtil.isEmptyOrSpaces(firstNm))
            data.setFirstNm(firstNm);
        if (!TextUtil.isEmptyOrSpaces(middleNm))
            data.setMiddleNm(middleNm);
        if (!TextUtil.isEmptyOrSpaces(lastNm))
            data.setLastNm(lastNm);
        if (!TextUtil.isEmptyOrSpaces(personPk))
            data.setPersonPk(new Integer(personPk));
        if (!(TextUtil.isEmptyOrSpaces(ssn1) && !TextUtil.isEmptyOrSpaces(ssn2) && !TextUtil.isEmptyOrSpaces(ssn3)))
            data.setSsn(ssn1+ssn2+ssn3);
        if (!TextUtil.isEmptyOrSpaces(city))
            data.setCity(city);
        if (!TextUtil.isEmptyOrSpaces(state))
            data.setState(state);
        if (!TextUtil.isEmptyOrSpaces(zipCode))
            data.setZipCode(zipCode);
        if (!TextUtil.isEmptyOrSpaces(zipPlus))
            data.setZipPlus(zipPlus);
        if (!TextUtil.isEmpty(affPk)) 
            data.setAffPk(affPk);
        if (!TextUtil.isEmpty(affCode))
            data.setAffCode(affCode);
        if (!TextUtil.isEmptyOrSpaces(affIdType))
            data.setAffType(affIdType);
        if (!TextUtil.isEmptyOrSpaces(affIdLocal))
            data.setAffLocalSubChapter(affIdLocal);
        if (!TextUtil.isEmptyOrSpaces(affIdState))
            data.setAffStateNatType(affIdState);
        if (!TextUtil.isEmptyOrSpaces(affIdSubUnit))
            data.setAffSubUnit(affIdSubUnit);
        if (!TextUtil.isEmptyOrSpaces(affIdCouncil))
            data.setAffCouncilRetireeChap(affIdCouncil);        
        // set page and sort values
        data.setPage(page);
        data.setPageSize(getPageSize());        
        data.setOrderBy(sortBy);        
        data.setOrdering(order);        
        return data;
    }
    
    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("firstNm: " + firstNm);
        buf.append(", middleNm: " + middleNm);
        buf.append(", lastNm: " + lastNm);
        buf.append(", mbr number(personPk): " + personPk);
        buf.append(", ssn1: " + ssn1);
        buf.append(", ssn2: " + ssn2);
        buf.append(", ssn3 " + ssn3);
        buf.append(", city: " + city);
        buf.append(", state: " + state);
        buf.append(", zipCode: " + zipCode);
        buf.append(", zipPlus: " + zipPlus);
        buf.append(", Aff Type: " +  affIdType);
        buf.append(", Aff Local: " + affIdLocal);
        buf.append(", Aff State: " + affIdState);
        buf.append(", Aff SubUnit: " + affIdSubUnit);
        buf.append(", Aff Council: " + affIdCouncil);
        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        return buf.toString()+"]";
    }
    
    /**
     * validation method for the Person Verify - Member functionality form
     */
    public ActionErrors personValidate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmptyOrSpaces(this.getFirstNm() )  && TextUtil.isEmptyOrSpaces(this.getLastNm() ) && TextUtil.isEmptyOrSpaces(this.getSsn()) ) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } else if (!TextUtil.isEmptyOrSpaces(this.getFirstNm() ) && TextUtil.isEmptyOrSpaces(this.getLastNm() ) && TextUtil.isEmptyOrSpaces(this.getSsn()) ) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } else if (TextUtil.isEmptyOrSpaces(this.getFirstNm()) && !TextUtil.isEmptyOrSpaces(this.getLastNm() ) && TextUtil.isEmptyOrSpaces(this.getSsn()) ) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        }
        return errors;
    }
    
    /**
     * validation method for this form
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        // no criteria entered
        if (allNull()) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.noSearchValueEntered"));
            setHasCriteria(false);
        }
        if (lastNm != null && lastNm.indexOf('%') > 0) { // lastNm has a percent wildcard
            int locCard = lastNm.indexOf('%');
            StringBuffer sb = new StringBuffer(lastNm);
            int lengthNm = sb.length();
            if (locCard != (lengthNm - 1)) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.search.wildCardPercentMustBeLast"));
            }
        }
        
        if (firstNm != null && firstNm.length() > 0)
        {
            this.nameMatch(errors, firstNm, ActionErrors.GLOBAL_ERROR);                                 
        }
        if (firstNm != null && firstNm.indexOf('%') > 0) { // firstNm has a percent wildcard
            int locCard = firstNm.indexOf('%');
            StringBuffer sb = new StringBuffer(firstNm);
            int lengthNm = sb.length();
            if (locCard != (lengthNm - 1)) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.search.wildCardPercentMustBeLast"));
            }
        }                
        return errors;
    }
    
    /**
     * allNull() method to check if no search fields have been entered
     */
    public boolean allNull() {
        
        // the code fields return 0 if nothing is selected . . so set to null, for empty checking        
        if (
        (!TextUtil.isEmptyOrSpaces(this.firstNm)) ||
        (!TextUtil.isEmptyOrSpaces(this.lastNm)) ||
        (!TextUtil.isEmptyOrSpaces(this.middleNm)) ||
        (!TextUtil.isEmpty(this.personPk)) ||
        ((!TextUtil.isEmptyOrSpaces(this.ssn1) && !TextUtil.isEmptyOrSpaces(this.ssn2) && !TextUtil.isEmptyOrSpaces(this.ssn3))) ||
        (!TextUtil.isEmptyOrSpaces(this.city)) ||
        (!TextUtil.isEmptyOrSpaces(this.state)) ||
        (!TextUtil.isEmptyOrSpaces(this.zipCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.zipPlus)) ||
        (!TextUtil.isEmptyOrSpaces(this.affIdType)) ||
        (!TextUtil.isEmptyOrSpaces(this.affIdLocal)) ||
        (!TextUtil.isEmptyOrSpaces(this.affIdState)) ||
        (!TextUtil.isEmptyOrSpaces(this.affIdSubUnit)) ||
        (!TextUtil.isEmptyOrSpaces(this.affIdCouncil))
        ) {
            return false;
        }        
        return true;
    }
    
    /** Getter for property affCode.
     * @return Value of property affCode.
     *
     */
    public java.lang.String getAffCode() {
        return affCode;
    }
    
    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffCode(java.lang.String affCode) {
        this.affCode = affCode;
    }
    
    /** Getter for property affIdCouncil.
     * @return Value of property affIdCouncil.
     *
     */
    public java.lang.String getAffIdCouncil() {
        return affIdCouncil;
    }
    
    /** Setter for property affIdCouncil.
     * @param affIdCouncil New value of property affIdCouncil.
     *
     */
    public void setAffIdCouncil(java.lang.String affIdCouncil) {
        this.affIdCouncil = affIdCouncil;
    }
    
    /** Getter for property affIdLocal.
     * @return Value of property affIdLocal.
     *
     */
    public java.lang.String getAffIdLocal() {
        return affIdLocal;
    }
    
    /** Setter for property affIdLocal.
     * @param affIdLocal New value of property affIdLocal.
     *
     */
    public void setAffIdLocal(java.lang.String affIdLocal) {
        this.affIdLocal = affIdLocal;
    }
    
    /** Getter for property affIdState.
     * @return Value of property affIdState.
     *
     */
    public java.lang.String getAffIdState() {
        return affIdState;
    }
    
    /** Setter for property affIdState.
     * @param affIdState New value of property affIdState.
     *
     */
    public void setAffIdState(java.lang.String affIdState) {
        this.affIdState = affIdState;
    }
    
    /** Getter for property affIdSubUnit.
     * @return Value of property affIdSubUnit.
     *
     */
    public java.lang.String getAffIdSubUnit() {
        return affIdSubUnit;
    }
    
    /** Setter for property affIdSubUnit.
     * @param affIdSubUnit New value of property affIdSubUnit.
     *
     */
    public void setAffIdSubUnit(java.lang.String affIdSubUnit) {
        this.affIdSubUnit = affIdSubUnit;
    }
    
    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public java.lang.String getAffIdType() {
        return affIdType;
    }
    
    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(java.lang.String affIdType) {
        this.affIdType = affIdType;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
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
    
    /** Getter for property firstNm.
     * @return Value of property firstNm.
     *
     */
    public java.lang.String getFirstNm() {
        return firstNm;
    }
    
    /** Setter for property firstNm.
     * @param firstNm New value of property firstNm.
     *
     */
    public void setFirstNm(java.lang.String firstNm) {
        this.firstNm = firstNm;
    }
        
    /** Getter for property lastNm.
     * @return Value of property lastNm.
     *
     */
    public java.lang.String getLastNm() {
        return lastNm;
    }
    
    /** Setter for property lastNm.
     * @param lastNm New value of property lastNm.
     *
     */
    public void setLastNm(java.lang.String lastNm) {
        this.lastNm = lastNm;
    }
    
    /** Getter for property middleNm.
     * @return Value of property middleNm.
     *
     */
    public java.lang.String getMiddleNm() {
        return middleNm;
    }
    
    /** Setter for property middleNm.
     * @param middleNm New value of property middleNm.
     *
     */
    public void setMiddleNm(java.lang.String middleNm) {
        this.middleNm = middleNm;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public java.lang.String getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(java.lang.String personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property ssn1.
     * @return Value of property ssn1.
     *
     */
    public java.lang.String getSsn1() {
        return ssn1;
    }
    
    /** Setter for property ssn1.
     * @param ssn1 New value of property ssn1.
     *
     */
    public void setSsn1(java.lang.String ssn1) {
        this.ssn1 = ssn1;
    }
    
    /** Getter for property ssn2.
     * @return Value of property ssn2.
     *
     */
    public java.lang.String getSsn2() {
        return ssn2;
    }
    
    /** Setter for property ssn2.
     * @param ssn2 New value of property ssn2.
     *
     */
    public void setSsn2(java.lang.String ssn2) {
        this.ssn2 = ssn2;
    }
    
    /** Getter for property ssn3.
     * @return Value of property ssn3.
     *
     */
    public java.lang.String getSsn3() {
        return ssn3;
    }
    
    /** Setter for property ssn3.
     * @param ssn3 New value of property ssn3.
     *
     */
    public void setSsn3(java.lang.String ssn3) {
        this.ssn3 = ssn3;
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
    
    /** Getter for property zipCode.
     * @return Value of property zipCode.
     *
     */
    public java.lang.String getZipCode() {
        return zipCode;
    }
    
    /** Setter for property zipCode.
     * @param zipCode New value of property zipCode.
     *
     */
    public void setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
    }
    
    /** Getter for property zipPlus.
     * @return Value of property zipPlus.
     *
     */
    public java.lang.String getZipPlus() {
        return zipPlus;
    }
    
    /** Setter for property zipPlus.
     * @param zipPlus New value of property zipPlus.
     *
     */
    public void setZipPlus(java.lang.String zipPlus) {
        this.zipPlus = zipPlus;
    }

    public java.lang.String getSsn() {
        return (ssn1+ssn2+ssn3);
    }    
    
    /** Getter for property hasCriteria.
     * @return Value of property hasCriteria.
     *
     */
    public boolean isHasCriteria() {
        return hasCriteria;
    }
    
    /** Setter for property hasCriteria.
     * @param hasCriteria New value of property hasCriteria.
     *
     */
    public void setHasCriteria(boolean hasCriteria) {
        this.hasCriteria = hasCriteria;
    }
    
    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: name on jsp
     */
    private void nameMatch(ActionErrors errors, String name, String prop) 
    {
        try
        {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9 %]{0,24})", name);
            if (match == false ){
                logger.debug("SearchVendorMembersForm:nameMatch -- An error is added.");
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
            }
        }catch (PatternSyntaxException pse)
        {
            logger.debug("SearchVendorMembersForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
    }            
}

