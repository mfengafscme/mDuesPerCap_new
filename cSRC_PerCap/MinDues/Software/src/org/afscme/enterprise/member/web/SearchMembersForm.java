package org.afscme.enterprise.member.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.MemberCriteria;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.CollectionUtil;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;
/**
 * Represents the search parameters form when the user is entering organization search criteria
 * Result data to be displayed by the MemberSearchResults.jsp through the MemberSearchAction is
 * stored in the parent classes result attribute
 *
 * @struts:form name="searchMembersForm"
 */
public class SearchMembersForm extends SearchForm {

    private static Logger logger =  Logger.getLogger(SearchMembersForm.class);     
    
    // person fields
    private String personPk; // member number on the search page !! convert in Get
    private Integer prefixNm;

    private String firstNm;
    private String middleNm;
    private String lastNm;
    private Integer suffixNm;
    private String nickNm;
    private String ssn1;
    private String ssn2;
    private String ssn3;
    //private Boolean validSsn; Boolean no work with JSP tag w/three values
    private int validSsn; // 0 is false, 1 is true, 2 means omit from search where clause (get both valid and not)
    private String altMailingNm;

    //needed soley for Verify Person (with Member functionality)
    private String dob;

    // SMA address search fields
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private Integer statePk; // state is changing to code, will no longer be needed
    private String zipCode; // also being changed to string from int in Address object
    private String addrUpdatedBy;
    private String addrUpdatedDt; //
    private String zipPlus;
    private String county;
    private String province;
    private Integer country;

    // phone and email related search fields
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private String personEmailAddr;

    // member related search fields
    private Integer primaryInformationSource;
    private Integer mbrType;
    private Integer mbrStatus;
    private String mbrCardSentDt; // convert during get
    private String lstModUserPk;
    private String lstModDt;  // convert to timestamp during get
    private String noCardsFg;
    private String noMailFg;
    private String noPublicEmpFg;
    private String noLegislativeMailFg;

    private Integer affPk;
    private String affType;
    private String affLocalSubChapter;
    private String affStateNatType;
    private String affSubUnit;
    private String affCouncilRetireeChap;
    private String affCode;

    private Set vduAffiliates; // set of affiliates allowed under the view data utility

    // the set of selected columns to display
   // private String[] selectList = {"p.first_nm", "p.middle_nm", "p.last_name", "p.suffix_nm", "sma", "affId", "am.mbr_status", "am.person_pk"};
    private String[] selectList = new String[23];
    private Collection selectCol = new ArrayList();
    private HashSet selectHash  = new HashSet();

    // the page the action was invoked from
    private String input;

    public SearchMembersForm() {
        newSearch();
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        if (request.getParameter("reset") != null) {
            if (request.getParameter("selectList") == null) {
                selectList = new String[0];
            }
            if (request.getParameter("affLocalSubChapter") == null) {
                affLocalSubChapter = null;
            }
            if (request.getParameter("affSubUnit") == null) {
                affSubUnit = null;
            }
        }
    }


    /** resets the search values to null  */
   public void newSearch() {
        prefixNm = null;
        firstNm = null;
        lastNm = null;
        middleNm = null;
        suffixNm = null;
        nickNm = null;
        ssn1 = null;
        ssn2 = null;
        ssn3 = null;
        validSsn = 2;

        addr1 = null;
        addr2 = null;
        city = null;
        state = null;
        country = null;
        zipCode = null;
        county = null;
        province = null;
        addrUpdatedBy = null;
        addrUpdatedDt = null;
        countryCode = null;
        areaCode = null;
        phoneNumber = null;
        personEmailAddr = null;
        primaryInformationSource = null;
        mbrStatus = null;
        mbrType = null;
        mbrCardSentDt = null;
        lstModUserPk = null;
        lstModDt = null;
        noCardsFg = null;
        noMailFg = null;
        noPublicEmpFg = null;
        noLegislativeMailFg = null;


        selectList[0] = "person_nm";
        selectList[1] = "sma";
        selectList[2] = "affId";
        selectList[3] = "am.mbr_status";
        selectList[4] = "am.person_pk";
        selectList[5] = "am.mbr_type";

        affPk = null;
        affType = null;
        affLocalSubChapter = null;
        affStateNatType = null;
        affSubUnit = null;
        affCouncilRetireeChap = null;
        affCode = null;

        sortBy= "";
        order=1;
        page=0;
        total=0;

        vduAffiliates = null;
    }
   
    /** resets the search values to null for Verify Member Form  */
   public void newVerifyMemberForm() {
        prefixNm = null;
        firstNm = null;
        lastNm = null;
        middleNm = null;
        suffixNm = null;
        nickNm = null;
        ssn1 = null;
        ssn2 = null;
        ssn3 = null;
        validSsn = 2;

        addr1 = null;
        addr2 = null;
        city = null;
        state = null;
        country = null;
        zipCode = null;
        county = null;
        province = null;
        addrUpdatedBy = null;
        addrUpdatedDt = null;
        countryCode = null;
        areaCode = null;
        phoneNumber = null;
        personEmailAddr = null;
        primaryInformationSource = null;
        mbrStatus = null;
        mbrType = null;
        mbrCardSentDt = null;
        lstModUserPk = null;
        lstModDt = null;
        noCardsFg = null;
        noMailFg = null;
        noPublicEmpFg = null;
        noLegislativeMailFg = null;


        selectList[0] = "person_nm";
        selectList[1] = "sma";
        selectList[2] = "affId";
        selectList[3] = "am.mbr_status";
        selectList[4] = "am.person_pk";
        selectList[5] = "am.mbr_type";

        sortBy= "";
        order=1;
        page=0;
        total=0;

        vduAffiliates = null;
    }
  

    /**
     * getMemberCriteriaData method to copy all the form
     * data fields to the criteria object to process.
     */
    public MemberCriteria getMemberCriteriaData() {

        MemberCriteria data = new MemberCriteria();
        if (!TextUtil.isEmptyOrSpaces(firstNm))
            data.setFirstNm(firstNm);
        if (!TextUtil.isEmpty(prefixNm) && (prefixNm.intValue() != 0))
            data.setPrefixNm(prefixNm);
        if (!TextUtil.isEmptyOrSpaces(lastNm))
            data.setLastNm(lastNm);
        if (!TextUtil.isEmptyOrSpaces(middleNm))
            data.setMiddleNm(middleNm);
        if (!TextUtil.isEmpty(suffixNm) && (suffixNm.intValue() != 0)) {
            logger.debug("Inside SearchMembersForm.getMemberCriteria, setting suffixNm is: " + suffixNm);
            data.setSuffixNm(suffixNm);
        }
            if (!TextUtil.isEmptyOrSpaces(nickNm))
            data.setNickNm(nickNm);
        if (!TextUtil.isEmpty(affPk)) data.setAffPk(affPk);
        if (!TextUtil.isEmpty(affCode)) data.setAffCode(affCode);
        if (!TextUtil.isEmptyOrSpaces(affType))
            data.setAffType(affType);
        if (!TextUtil.isEmptyOrSpaces(affLocalSubChapter))
            data.setAffLocalSubChapter(affLocalSubChapter);
        if (!TextUtil.isEmptyOrSpaces(affStateNatType))
            data.setAffStateNatType(affStateNatType);
         if (!TextUtil.isEmptyOrSpaces(affSubUnit))
            data.setAffSubUnit(affSubUnit);
       if (!TextUtil.isEmptyOrSpaces(affCouncilRetireeChap))
            data.setAffCouncilRetireeChap(affCouncilRetireeChap);

        if (!(TextUtil.isEmptyOrSpaces(ssn1) && !TextUtil.isEmptyOrSpaces(ssn2) && !TextUtil.isEmptyOrSpaces(ssn3)))
            data.setSsn(ssn1+ssn2+ssn3);
  //     if (!TextUtil.isEmpty(validSsn)
        data.setValidSsn(validSsn);

        if (!TextUtil.isEmptyOrSpaces(altMailingNm))
            data.setAltMailingNm(altMailingNm);
        if (!TextUtil.isEmptyOrSpaces(personPk))
            data.setPersonPk(new Integer(personPk.trim()));
        if (!TextUtil.isEmptyOrSpaces(addr1))
            data.setAddress1(addr1);
        if (!TextUtil.isEmptyOrSpaces(addr2))
            data.setAddress2(addr2);
        if (!TextUtil.isEmptyOrSpaces(state))
            data.setState(state);
        if (!TextUtil.isEmptyOrSpaces(county))
            data.setCounty(county);
        if (!TextUtil.isEmpty(country) && (country.intValue() != 0))
            data.setCountry(country);
        if (!TextUtil.isEmptyOrSpaces(zipCode))
            data.setZipCode(zipCode);
        if (!TextUtil.isEmptyOrSpaces(province))
            data.setProvince(province);
        if (!TextUtil.isEmptyOrSpaces(countryCode))
            data.setCountryCode(countryCode);
         try {
            data.setAddrUpdatedDt(TextUtil.parseDate(addrUpdatedDt));
        } catch (Exception e) {
            data.setAddrUpdatedDt(null);
        }

        try {
            data.setMbrCardSentDt(TextUtil.parseDate(mbrCardSentDt));
        } catch (Exception e) {
            data.setMbrCardSentDt(null);
        }
        try {
            data.setLstModDt(TextUtil.parseDate(lstModDt));
        } catch (Exception e) {
            data.setLstModDt(null);
        }
        if (!TextUtil.isEmpty(areaCode))
            data.setAreaCode(areaCode);

        if (!TextUtil.isEmpty(phoneNumber))
            data.setPhoneNumber(phoneNumber);
        if (!TextUtil.isEmpty(personEmailAddr))
            data.setPersonEmailAddr(personEmailAddr);

        if(noMailFg != null && noMailFg.equals("yes"))
        	data.setNoMailFg(new Boolean(true));
		else if(noMailFg != null && noMailFg.equals("no"))
			data.setNoMailFg(new Boolean(false));

        if(noCardsFg != null && noCardsFg.equals("yes"))
        	data.setNoCardsFg(new Boolean(true));
        else if(noCardsFg != null && noCardsFg.equals("no"))
        	data.setNoCardsFg(new Boolean(false));

        if(noLegislativeMailFg != null && noLegislativeMailFg.equals("yes"))
        	data.setNoLegislativeMailFg(new Boolean(true));
        else if(noLegislativeMailFg != null && noLegislativeMailFg.equals("no"))
        	data.setNoLegislativeMailFg(new Boolean(false));

        if(noPublicEmpFg != null && noPublicEmpFg.equals("yes"))
        	data.setNoPublicEmpFg(new Boolean(true));
		else if(noPublicEmpFg != null && noPublicEmpFg.equals("no"))
			data.setNoPublicEmpFg(new Boolean(false));

        if (!TextUtil.isEmpty(mbrStatus) && (mbrStatus.intValue() != 0))
            data.setMbrStatus(mbrStatus);
        if (!TextUtil.isEmpty(mbrType) &&  (mbrType.intValue() != 0))
            data.setMbrType(mbrType);
        if (!TextUtil.isEmpty(primaryInformationSource))
            data.setMbrStatus(primaryInformationSource);

        if (!TextUtil.isEmpty(selectList)) {
            selectCol = new ArrayList();
            CollectionUtil.copy(selectCol, selectList);
            data.setSelectList(selectCol);
        }

        if (vduAffiliates != null) {
            logger.debug("searchMembersForm.getMemberCriteria vduAffiliates is : " + this.vduAffiliates);
            data.setVduAffiliates(vduAffiliates);
        }

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
        buf.append("prefixNm: " + prefixNm);
        buf.append(", lastNm: " + lastNm);
        buf.append(", firstNm: " + firstNm);
        buf.append(", middleNm: " + middleNm);
        buf.append(", suffixNm: " + suffixNm);
        buf.append(", nickNm: " + nickNm);
        buf.append(", Aff Type: " +  affType);
        buf.append(", Aff Local: " + affLocalSubChapter);
        buf.append(", Aff State: " + affStateNatType);
        buf.append(", Aff SubUnit: " + affSubUnit);
        buf.append(", Aff Council: " + affCouncilRetireeChap);
        buf.append(", ssn1: " + ssn1);
        buf.append(", ssn2: " + ssn2);
        buf.append(", ssn3 " + ssn3);
        buf.append(", validSsn: " + validSsn);
        buf.append(", altMailingNm: " + altMailingNm);
        buf.append(", addr1: " + addr1);
        buf.append(", addr2: " + addr2);
        buf.append(", city: " + city);
        buf.append(", state: " + state);
        buf.append(", zipPostal: " + zipCode);
        buf.append(", county: " + county);
        buf.append(", province: " + province);
        buf.append(", country: " + country);
        buf.append(", countryCode: " + countryCode);
        buf.append(", addrUpdatedBy: " + addrUpdatedBy);
        buf.append(", addrUpdatedDt: " + addrUpdatedDt);
        buf.append(", areaCode: " + areaCode);
        buf.append(", phoneNumber: " + phoneNumber);
        buf.append(", personEmailAddr: " + personEmailAddr);
        buf.append(", primaryInformationSource: " + primaryInformationSource);
        buf.append(", mbrType: " + mbrType);
        buf.append(", mbrStatus: " + mbrStatus);
        buf.append(", mbr number(personPk): " + personPk);
        buf.append(", mbrCardSentDt: " + mbrCardSentDt);
        buf.append(", lstModUserPk: " + lstModUserPk);
        buf.append(", lstModDt: " + lstModDt);
        buf.append(", noMailFg: " + noMailFg);
        buf.append(", noCardsFg: " + noCardsFg);
        buf.append(", noPublicEmpFg: " + noPublicEmpFg);
        buf.append(", noLegislativeMailFg: " + noLegislativeMailFg);
        buf.append(", selectList: " + selectList);

        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        buf.append(", vduAffiliates " + vduAffiliates);
        return buf.toString()+"]";
    }

    /**
     * validation method for the Person Verify - Member functionality form
     */
    public ActionErrors personValidate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("personValidate - SearchMembersForm: firstNm="+getFirstNm());
        logger.debug("personValidate - SearchMembersForm: lastNm="+getLastNm());
        logger.debug("personValidate - SearchMembersForm:ssn="+this.getSsn());
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

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmptyOrSpaces(this.getFirstNm()) == false)
        {
            this.nameMatch(errors, this.getFirstNm(), ActionErrors.GLOBAL_ERROR);
        }
        return errors;
    }

    /**
     * allNull() method to check if no search fields have been entered
     */
    public boolean allNull() {

        // the code fields return 0 if nothing is selected . . so set to null, for empty checking
        if (this.prefixNm != null) {
            if (this.prefixNm.intValue() == 0) this.prefixNm = null; }

        if (this.suffixNm != null) {
            if (this.suffixNm.intValue() == 0) this.suffixNm = null; }
        if (this.mbrType != null) {
            if (this.mbrType.intValue() == 0) this.mbrType = null; }
        if (this.mbrStatus != null) {
            if (this.mbrStatus.intValue() == 0) this.mbrStatus = null; }
        if (this.primaryInformationSource != null) {
            if (this.primaryInformationSource.intValue() == 0) this.primaryInformationSource = null; }
        if (this.country != null) {
            if (this.country.intValue() == 0) this.country = null; }

        if (
        (!TextUtil.isEmpty(this.prefixNm)) ||
        (!TextUtil.isEmptyOrSpaces(this.firstNm)) ||
        (!TextUtil.isEmptyOrSpaces(this.lastNm)) ||
        (!TextUtil.isEmptyOrSpaces(this.middleNm)) ||
        (!TextUtil.isEmpty(this.suffixNm)) ||
        (!TextUtil.isEmptyOrSpaces(this.nickNm)) ||
        ((!TextUtil.isEmptyOrSpaces(this.ssn1) && !TextUtil.isEmptyOrSpaces(this.ssn2) && !TextUtil.isEmptyOrSpaces(this.ssn3)) )  ||
        (!(this.validSsn == 2)) ||
        (!TextUtil.isEmptyOrSpaces(this.addr1)) ||
        (!TextUtil.isEmptyOrSpaces(this.addr2)) ||
        (!TextUtil.isEmptyOrSpaces(this.city)) ||
        (!TextUtil.isEmptyOrSpaces(this.state)) ||
        (!TextUtil.isEmpty(this.country)) ||
        (!TextUtil.isEmptyOrSpaces(this.zipCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.county)) ||
        (!TextUtil.isEmptyOrSpaces(this.province)) ||
        (!TextUtil.isEmptyOrSpaces(this.addrUpdatedDt)) ||
        (!TextUtil.isEmptyOrSpaces(this.addrUpdatedBy)) ||
        (!TextUtil.isEmptyOrSpaces(this.countryCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.areaCode)) ||
        (!TextUtil.isEmptyOrSpaces(this.phoneNumber)) ||
        (!TextUtil.isEmptyOrSpaces(this.personEmailAddr)) ||
        (!TextUtil.isEmpty(this.primaryInformationSource)) ||
        (!TextUtil.isEmpty(this.mbrStatus)) ||
        (!TextUtil.isEmpty(this.mbrType)) ||
        (!TextUtil.isEmpty(this.personPk)) ||
        (!TextUtil.isEmptyOrSpaces(this.lstModUserPk)) ||
        (!TextUtil.isEmptyOrSpaces(this.lstModDt)) ||
        (!TextUtil.isEmptyOrSpaces(this.mbrCardSentDt)) ||
        (!TextUtil.isEmpty(this.noMailFg)) ||
        (!TextUtil.isEmpty(this.noCardsFg)) ||
        (!TextUtil.isEmpty(this.noPublicEmpFg)) ||
        (!TextUtil.isEmpty(this.noLegislativeMailFg)) ||
        (!TextUtil.isEmptyOrSpaces(this.affType)) ||
        (!TextUtil.isEmptyOrSpaces(this.affLocalSubChapter)) ||
        (!TextUtil.isEmptyOrSpaces(this.affStateNatType)) ||
        (!TextUtil.isEmptyOrSpaces(this.affSubUnit)) ||
        (!TextUtil.isEmptyOrSpaces(this.affCouncilRetireeChap))
           )

        {
            return false;
        }

        return true;
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

    /** Getter for property prefixNm.
     * @return Value of property prefixNm.
     *
     */
    public java.lang.Integer getPrefixNm() {
        return prefixNm;
    }

    /** Setter for property prefixNm.
     * @param prefixNm New value of property prefixNm.
     *
     */
    public void setPrefixNm(java.lang.Integer prefixNm) {
        this.prefixNm = prefixNm;
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

    /** Getter for property suffixNm.
     * @return Value of property Nm.
     *
     */
    public java.lang.Integer getSuffixNm() {
        return suffixNm;
    }

    /** Setter for property suffixNm.
     * @param suffixNm New value of property suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer suffixNm) {
        this.suffixNm = suffixNm;
    }

    /** Getter for property nickNm.
     * @return Value of property nickNm.
     *
     */
    public java.lang.String getNickNm() {
        return nickNm;
    }

    /** Setter for property nickNm.
     * @param nickNm New value of property nickNm.
     *
     */
    public void setNickNm(java.lang.String nickNm) {
        this.nickNm = nickNm;
    }

    /** Getter for derived property ssn.
     * @return Value of property ssn.
     *
     */
    public java.lang.String getSsn() {
        return (ssn1+ssn2+ssn3);
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

    /** Getter for property validSsn.
     * @return Value of property validSsn.
     *
     */
    public int getValidSsn() {
        return validSsn;
    }

    /** Setter for property validSsn.
     * @param validSsn New value of property validSsn.
     *
     */
    public void setValidSsn(int validSsn) {
        this.validSsn = validSsn;
    }

    /** Getter for property altMailingNm.
     * @return Value of property altMailingNm.
     *
     */
    public java.lang.String getAltMailingNm() {
        return altMailingNm;
    }

    /** Setter for property altMailingNm.
     * @param altMailingNm New value of property altMailingNm.
     *
     */
    public void setAltMailingNm(java.lang.String altMailingNm) {
        this.altMailingNm = altMailingNm;
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

    /** Getter for property statePk.
     * @return Value of property statePk.
     *
     */
    public java.lang.Integer getStatePk() {
        return statePk;
    }

    /** Setter for property statePk.
     * @param statePk New value of property statePk.
     *
     */
    public void setStatePk(java.lang.Integer statePk) {
        this.statePk = statePk;
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

    /** Getter for property addrUpdatedBy.
     * @return Value of property addrUpdatedBy.
     *
     */
    public java.lang.String getAddrUpdatedBy() {
        return addrUpdatedBy;
    }

    /** Setter for property addrUpdatedBy.
     * @param addrUpdatedBy New value of property addrUpdatedBy.
     *
     */
    public void setAddrUpdatedBy(java.lang.String addrUpdatedBy) {
        this.addrUpdatedBy = addrUpdatedBy;
    }

    /** Getter for property addrUpdatedDt.
     * @return Value of property addrUpdatedDt.
     *
     */
    public java.lang.String getAddrUpdatedDt() {
        return addrUpdatedDt;
    }

    /** Setter for property addrUpdatedDt.
     * @param addrUpdatedDt New value of property addrUpdatedDt.
     *
     */
    public void setAddrUpdatedDt(java.lang.String addrUpdatedDt) {
        this.addrUpdatedDt = addrUpdatedDt;
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

    /** Getter for property countryCode.
     * @return Value of property countryCode.
     *
     */
    public java.lang.String getCountryCode() {
        return countryCode;
    }

    /** Setter for property countryCode.
     * @param countryCode New value of property countryCode.
     *
     */
    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }

    /** Getter for property areaCode.
     * @return Value of property areaCode.
     *
     */
    public java.lang.String getAreaCode() {
        return areaCode;
    }

    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     *
     */
    public void setAreaCode(java.lang.String areaCode) {
        this.areaCode = areaCode;
    }

    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     *
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }

    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     *
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Getter for property personEmailAddr.
     * @return Value of property personEmailAddr.
     *
     */
    public java.lang.String getPersonEmailAddr() {
        return personEmailAddr;
    }

    /** Setter for property personEmailAddr.
     * @param personEmailAddr New value of property personEmailAddr.
     *
     */
    public void setPersonEmailAddr(java.lang.String personEmailAddr) {
        this.personEmailAddr = personEmailAddr;
    }

    /** Getter for property primaryInformationSource.
     * @return Value of property primaryInformationSource.
     *
     */
    public java.lang.Integer getPrimaryInformationSource() {
        return primaryInformationSource;
    }

    /** Setter for property primaryInformationSource.
     * @param primaryInformationSource New value of property primaryInformationSource.
     *
     */
    public void setPrimaryInformationSource(java.lang.Integer primaryInformationSource) {
        this.primaryInformationSource = primaryInformationSource;
    }

    /** Getter for property mbrType.
     * @return Value of property mbrType.
     *
     */
    public java.lang.Integer getMbrType() {
        return mbrType;
    }

    /** Setter for property mbrType.
     * @param mbrType New value of property mbrType.
     *
     */
    public void setMbrType(java.lang.Integer mbrType) {
        this.mbrType = mbrType;
    }

    /** Getter for property mbrStatus.
     * @return Value of property mbrStatus.
     *
     */
    public java.lang.Integer getMbrStatus() {
        return mbrStatus;
    }

    /** Setter for property mbrStatus.
     * @param mbrStatus New value of property mbrStatus.
     *
     */
    public void setMbrStatus(java.lang.Integer mbrStatus) {
        this.mbrStatus = mbrStatus;
    }

    /** Getter for property mbrCardSentDt.
     * @return Value of property mbrCardSentDt.
     *
     */
    public java.lang.String getMbrCardSentDt() {
        return mbrCardSentDt;
    }

    /** Setter for property mbrCardSentDt.
     * @param mbrCardSentDt New value of property mbrCardSentDt.
     *
     */
    public void setMbrCardSentDt(java.lang.String mbrCardSentDt) {
        this.mbrCardSentDt = mbrCardSentDt;
    }

    /** Getter for property lstModUserPk.
     * @return Value of property lstModUserPk.
     *
     */
    public java.lang.String getLstModUserPk() {
        return lstModUserPk;
    }

    /** Setter for property lstModUserPk.
     * @param lstModUserPk New value of property lstModUserPk.
     *
     */
    public void setLstModUserPk(java.lang.String lstModUserPk) {
        this.lstModUserPk = lstModUserPk;
    }

    /** Getter for property lstModDt.
     * @return Value of property lstModDt.
     *
     */
    public java.lang.String getLstModDt() {
        return lstModDt;
    }

    /** Setter for property lstModDt.
     * @param lstModDt New value of property lstModDt.
     *
     */
    public void setLstModDt(java.lang.String lstModDt) {
        this.lstModDt = lstModDt;
    }

    /** Getter for property noCardsFg.
     * @return Value of property noCardsFg.
     *
     */
    public java.lang.String getNoCardsFg() {
        return noCardsFg;
    }

    /** Setter for property noCardsFg.
     * @param noCardsFg New value of property noCardsFg.
     *
     */
    public void setNoCardsFg(java.lang.String noCardsFg) {
        this.noCardsFg = noCardsFg;
    }

    /** Getter for property noMailFg.
     * @return Value of property noMailFg.
     *
     */
    public java.lang.String getNoMailFg() {
        return noMailFg;
    }

    /** Setter for property noMailFg.
     * @param noMailFg New value of property noMailFg.
     *
     */
    public void setNoMailFg(java.lang.String noMailFg) {
        this.noMailFg = noMailFg;
    }

    /** Getter for property noPublicEmpFg.
     * @return Value of property noPublicEmpFg.
     *
     */
    public java.lang.String getNoPublicEmpFg() {
        return noPublicEmpFg;
    }

    /** Setter for property noPublicEmpFg.
     * @param noPublicEmpFg New value of property noPublicEmpFg.
     *
     */
    public void setNoPublicEmpFg(java.lang.String noPublicEmpFg) {
        this.noPublicEmpFg = noPublicEmpFg;
    }

    /** Getter for property noLegislativeMailFg.
     * @return Value of property noLegislativeMailFg.
     *
     */
    public java.lang.String getNoLegislativeMailFg() {
        return noLegislativeMailFg;
    }

    /** Setter for property noLegislativeMailFg.
     * @param noLegislativeMailFg New value of property noLegislativeMailFg.
     *
     */
    public void setNoLegislativeMailFg(java.lang.String noLegislativeMailFg) {
        this.noLegislativeMailFg = noLegislativeMailFg;
    }

    /** Getter for property selectList.
     * @return Value of property selectList.
     *
     */
    public String[] getSelectList() {
//        for(int i = 0; i < this.selectList.length; i++)
//        {
//            logger.debug("getSelectList - selectList member " + i + " is: " + selectList[i]);
//        }
            return this.selectList;
     }

    /** Setter for property selectList.
     * @param selectList New value of property selectList.
     *
     */
    public void setSelectList(String[] selectList) {

        this.selectList = selectList;
        for(int i = 0; i < this.selectList.length; i++)
        {
            logger.debug("setSelectList - selectList member " + i + " is: " + selectList[i]);
        }
    }

    /** Getter for property affType.
     * @return Value of property affType.
     *
     */
    public java.lang.String getAffType() {
        return affType;
    }

    /** Setter for property affType.
     * @param affType New value of property affType.
     *
     */
    public void setAffType(java.lang.String affType) {
        this.affType = affType;
    }

    /** Getter for property affLocalSubChapter.
     * @return Value of property affLocalSubChapter.
     *
     */
    public java.lang.String getAffLocalSubChapter() {
        return affLocalSubChapter;
    }

    /** Setter for property affLocalSubChapter.
     * @param affLocalSubChapter New value of property affLocalSubChapter.
     *
     */
    public void setAffLocalSubChapter(java.lang.String affLocalSubChapter) {
        this.affLocalSubChapter = affLocalSubChapter;
    }

    /** Getter for property affStateNatType.
     * @return Value of property affStateNatType.
     *
     */
    public java.lang.String getAffStateNatType() {
        return affStateNatType;
    }

    /** Setter for property affStateNatType.
     * @param affStateNatType New value of property affStateNatType.
     *
     */
    public void setAffStateNatType(java.lang.String affStateNatType) {
        this.affStateNatType = affStateNatType;
    }

    /** Getter for property affSubUnit.
     * @return Value of property affSubUnit.
     *
     */
    public java.lang.String getAffSubUnit() {
        return affSubUnit;
    }

    /** Setter for property affSubUnit.
     * @param affSubUnit New value of property affSubUnit.
     *
     */
    public void setAffSubUnit(java.lang.String affSubUnit) {
        this.affSubUnit = affSubUnit;
    }

    /** Getter for property affCouncilRetireeChap.
     * @return Value of property affCouncilRetireeChap.
     *
     */
    public java.lang.String getAffCouncilRetireeChap() {
        return affCouncilRetireeChap;
    }

    /** Setter for property affCouncilRetireeChap.
     * @param affCouncilRetireeChap New value of property affCouncilRetireeChap.
     *
     */
    public void setAffCouncilRetireeChap(java.lang.String affCouncilRetireeChap) {
        this.affCouncilRetireeChap = affCouncilRetireeChap;
    }

    /** Getter for property selectHash.
     * @return Value of property selectHash.
     *
     */
    public java.util.HashSet getSelectHash() {
        this.selectHash.clear();
        CollectionUtil.copy(this.selectHash, selectList);
//        Iterator iter = selectHash.iterator();
//        while (iter.hasNext()) {
//            String col = (String)iter.next();
//            logger.debug("getSelectHash, contents are: " + col);
//        }

        return selectHash;
    }

    /** Setter for property selectHash.
     * @param selectHash New value of property selectHash.
     *
     */
    public void setSelectHash(java.util.HashSet selectHash) {
        this.selectHash = selectHash;
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

    /** Getter for property dob.
     * @return Value of property dob.
     *
     */
    public java.lang.String getDob() {
        return dob;
    }

    /** Setter for property dob.
     * @param dob New value of property dob.
     *
     */
    public void setDob(java.lang.String dob) {
        this.dob = dob;
    }

    /** Getter for property input.
     * @return Value of property input.
     *
     */
    public String getInput() {
        return input;
    }

    /** Setter for property input.
     * @param input New value of property input.
     *
     */
    public void setInput(String input) {
        this.input = input;
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

    /** Getter for property vduAffiliates.
     * @return Value of property vduAffiliates.
     *
     */
    public java.util.Set getVduAffiliates() {
        return vduAffiliates;
    }

    /** Setter for property vduAffiliates.
     * @param vduAffiliates New value of property vduAffiliates.
     *
     */
    public void setVduAffiliates(java.util.Set vduAffiliates) {
        this.vduAffiliates = vduAffiliates;
        logger.debug("SearchMembersForm.setVduAffiliates() vduAffiliates is : " + this.vduAffiliates);
    }

}


