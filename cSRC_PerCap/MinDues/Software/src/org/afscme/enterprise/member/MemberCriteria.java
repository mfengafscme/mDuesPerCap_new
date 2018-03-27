
package org.afscme.enterprise.member;

import org.afscme.enterprise.person.PersonCriteria;
import org.afscme.enterprise.util.TextUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 * Contains data used to query for members.  Includes information about which
 * result fields are desired, sort information and pagining information.
 */
public class MemberCriteria extends PersonCriteria implements Serializable {
    
    private static Logger logger =  Logger.getLogger(MemberCriteria.class);     
    
    protected Integer personPk; // aka member number on search forms
    protected String affType;
    protected String affLocalSubChapter;
    protected String affStateNatType;
    protected String affSubUnit;
    protected String affCouncilRetireeChap;
    protected String affCode;
    protected Integer affPk;
    protected Integer primaryInformationSource;
    protected Integer mbrType;
    protected Integer mbrStatus;
    protected java.sql.Timestamp mbrCardSentDt;
    protected Integer lstModUserPk;
    protected java.sql.Timestamp lstModDt;
    protected Boolean noCardsFg;
    protected Boolean noMailFg;
    protected Boolean noPublicEmpFg;
    protected Boolean noLegislativeMailFg;

    protected String orderBy;
    protected int ordering;

    protected int page;
    protected int pageSize;

    protected Set vduAffiliates; // needed to implement data level security for the view Data Utility

    // set if user selects to display the SMA on Member Search Result page
    protected boolean hasSelectedAddress = false;
    // set if user inputs any criteria through the system mailing address fields
    protected boolean hasAddressInWhere = false;


    protected Collection selectList = new HashSet();


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
        return this.affLocalSubChapter;
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
        return this.affStateNatType;
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
        return this.affSubUnit;
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
        return this.affCouncilRetireeChap;
    }

    /** Setter for property affCouncilRetireeChap.
     * @param affCouncilRetireeChap New value of property affCouncilRetireeChap.
     *
     */
    public void setAffCouncilRetireeChap(java.lang.String affCouncilRetireeChap) {
        this.affCouncilRetireeChap = affCouncilRetireeChap;
    }

    /** Getter for property affCode.
     * @return Value of property affCode.
     *
     */
    public java.lang.Integer getAffPk() {
        return this.affPk;
    }

    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }

    /** Getter for property primaryInformationSource.
     * @return Value of property primaryInformationSource.
     *
     */
    public java.lang.Integer getPrimaryInformationSource() {
        return this.primaryInformationSource;
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
    public java.sql.Timestamp getMbrCardSentDt() {
        return mbrCardSentDt;
    }

    /** Setter for property mbrCardSentDt.
     * @param mbrCardSentDt New value of property mbrCardSentDt.
     *
     */
    public void setMbrCardSentDt(java.sql.Timestamp mbrCardSentDt) {
        this.mbrCardSentDt = mbrCardSentDt;
    }

    /** Getter for property lstModUserPk.
     * @return Value of property lstModUserPk.
     *
     */
    public java.lang.Integer getLstModUserPk() {
        return lstModUserPk;
    }

    /** Setter for property lstModUserPk.
     * @param lstModUserPk New value of property lstModUserPk.
     *
     */
    public void setLstModUserPk(java.lang.Integer lstModUserPk) {
        this.lstModUserPk = lstModUserPk;
    }

    /** Getter for property noCardsFg.
     * @return Value of property noCardsFg.
     *
     */
    public java.lang.Boolean getNoCardsFg() {
        return noCardsFg;
    }

    /** Setter for property noCardsFg.
     * @param noCardsFg New value of property noCardsFg.
     *
     */
    public void setNoCardsFg(java.lang.Boolean noCardsFg) {
        this.noCardsFg = noCardsFg;
    }

    /** Getter for property noMailFg.
     * @return Value of property noMailFg.
     *
     */
    public java.lang.Boolean getNoMailFg() {
        return noMailFg;
    }

    /** Setter for property noMailFg.
     * @param noMailFg New value of property noMailFg.
     *
     */
    public void setNoMailFg(java.lang.Boolean noMailFg) {
        this.noMailFg = noMailFg;
    }

    /** Getter for property noPublicEmpFg.
     * @return Value of property noPublicEmpFg.
     *
     */
    public java.lang.Boolean getNoPublicEmpFg() {
        return noPublicEmpFg;
    }

    /** Setter for property noPublicEmpFg.
     * @param noPublicEmpFg New value of property noPublicEmpFg.
     *
     */
    public void setNoPublicEmpFg(java.lang.Boolean noPublicEmpFg) {
        this.noPublicEmpFg = noPublicEmpFg;
    }

    /** Getter for property noLegislativeMailFg.
     * @return Value of property noLegislativeMailFg.
     *
     */
    public java.lang.Boolean getNoLegislativeMailFg() {
        return noLegislativeMailFg;
    }

    /** Setter for property noLegislativeMailFg.
     * @param noLegislativeMailFg New value of property noLegislativeMailFg.
     *
     */
    public void setNoLegislativeMailFg(java.lang.Boolean noLegislativeMailFg) {
        this.noLegislativeMailFg = noLegislativeMailFg;
    }

    /** Getter for property orderBy.
     * @return Value of property orderBy.
     *
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    /** Setter for property orderBy.
     * @param orderBy New value of property orderBy.
     *
     */
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;

        // make numeric sort on string
        if ("int_council".equals(orderBy)) {
            this.orderBy = " cast(aff_councilRetiree_chap AS int) ";
        }
        if ("int_local".equals(orderBy)) {
            this.orderBy = " cast(aff_localSubChapter AS int) " ;
        }
    }


    /** Getter for property ordering.
     * @return Value of property ordering.
     *
     */
    public int getOrdering() {
        return ordering;
    }

    /** Setter for property ordering.
     * @param ordering New value of property ordering.
     *
     */
    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }


    /** Getter for property lstModDt.
     * @return Value of property lstModDt.
     *
     */
    public java.sql.Timestamp getLstModDt() {
        return lstModDt;
    }

    /** Setter for property lstModDt.
     * @param lstModDt New value of property lstModDt.
     *
     */
    public void setLstModDt(java.sql.Timestamp lstModDt) {
        this.lstModDt = lstModDt;
    }

    /** Getter for property selectList.
     * @return Value of property selectList.
     *
     */
    public java.util.Collection getSelectList() {
        return selectList;
    }

    /** Setter for property selectList.
     * @param selectList New value of property selectList.
     *
     */
    public void setSelectList(java.util.Collection selectList) {
        this.selectList = selectList;
    }

    /** Getter for property hasSelectedAddress.
     * @return Value of property hasSelectedAddress.
     *
     */
    public boolean getHasSelectedAddress() {
        return hasSelectedAddress;
    }

    /** Setter for property hasSelectedAddress.
     * @param hasSelectedAddress New value of property hasSelectedAddress.
     *
     */
    public void setHasSelectedAddress(boolean hasSelectedAddress) {
        this.hasSelectedAddress = hasSelectedAddress;
    }

    /** Getter for property hasAddressInWhere.
     * @return Value of property hasAddressInWhere.
     *
     */
    public boolean getHasAddressInWhere() {
        return hasAddressInWhere;
    }

    /** Setter for property hasAddressInWhere.
     * @param hasAddressInWhere New value of property hasAddressInWhere.
     *
     */
    public void setHasAddressInWhere(boolean hasAddressInWhere) {
        this.hasAddressInWhere = hasAddressInWhere;
    }

    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }

    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }

    /** Getter for property page.
     * @return Value of property page.
     *
     */
    public int getPage() {
        return page;
    }

    /** Setter for property page.
     * @param page New value of property page.
     *
     */
    public void setPage(int page) {
        this.page = page;
    }

    /** Getter for property pageSize.
     * @return Value of property pageSize.
     *
     */
    public int getPageSize() {
        return pageSize;
    }

    /** Setter for property pageSize.
     * @param pageSize New value of property pageSize.
     *
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
    }

    /** Determines whether an affiliate join is needed.
     * @return Flag indicating whether an affiliate join is needed.
     *
     */
    public boolean needAffiliateJoin() {
        if(!TextUtil.isEmpty(affType) || !TextUtil.isEmpty(affLocalSubChapter) || !TextUtil.isEmpty(affStateNatType) ||
        !TextUtil.isEmpty(affSubUnit) || !TextUtil.isEmpty(affCouncilRetireeChap) || !TextUtil.isEmpty(affCode) ||
        (!TextUtil.isEmpty(affPk) && affPk.intValue() != 0)) {
            return true;
        }
        Collection selectList = getSelectList();
        Iterator i = selectList.iterator();
        while(i.hasNext()) {
            String column = (String)i.next();
            if(!TextUtil.isEmpty(column) && column.equals("affId")){
                return true;
            }
        }
        return false;
    }

    /** Determines whether an email join is needed.
     * @return Flag indicating whether an email join is needed.
     *
     */
    public boolean needEmailJoin() {
        if(!TextUtil.isEmpty(getPersonEmailAddr())) {
            return true;
        }
        Collection selectList = getSelectList();
        Iterator i = selectList.iterator();
        while(i.hasNext()) {
            String column = (String)i.next();
            if(!TextUtil.isEmpty(column) && column.equals("email")){
                return true;
            }
        }
        return false;
    }

    /** Determines whether a phone join is needed.
     * @return Flag indicating whether a phone join is needed.
     *
     */
    public boolean needPhoneJoin() {
        if(!TextUtil.isEmpty(getCountryCode()) || !TextUtil.isEmpty(getAreaCode()) || !TextUtil.isEmpty(getPhoneNumber())) {
            return true;
        }
        Collection selectList = getSelectList();
        Iterator i = selectList.iterator();
        while(i.hasNext()) {
            String column = (String)i.next();
            if(!TextUtil.isEmpty(column) && column.equals("phone")){
                return true;
            }
        }
        return false;
    }

    /** Determines whether an address join is needed.
     * @return Flag indicating whether an address join is needed.
     *
     */
    public boolean needAddressJoin() {
        if(!TextUtil.isEmpty(getAddress1()) || !TextUtil.isEmpty(getAddress2()) || !TextUtil.isEmpty(getCity()) ||
        !TextUtil.isEmpty(getState()) || !TextUtil.isEmpty(getZipCode()) || !TextUtil.isEmpty(getZipPlus()) ||
        !TextUtil.isEmpty(getCounty()) || !TextUtil.isEmpty(getProvince()) || !TextUtil.isEmpty(getCountry()) ||
        !TextUtil.isEmpty(getAddrUpdatedBy()) || !TextUtil.isEmpty(getAddrUpdatedDt()))  {
            return true;
        }
        Collection selectList = getSelectList();
        Iterator i = selectList.iterator();
        while(i.hasNext()) {
            String column = (String)i.next();
            if(!TextUtil.isEmpty(column) && (column.equals("sma") || column.equals("pa.lst_mod_user_pk") || column.equals("pa.lst_mod_dt"))) {
                return true;
            }
        }
        return false;
    }

    /** Determines whether an address inner or left join is needed.
     * @return The type of join.
     *
     */
    public String getAddressLeftOrInner() {
        if(!TextUtil.isEmpty(getAddress1()) || !TextUtil.isEmpty(getAddress2()) || !TextUtil.isEmpty(getCity()) ||
        !TextUtil.isEmpty(getState()) || !TextUtil.isEmpty(getZipCode()) || !TextUtil.isEmpty(getZipPlus()) ||
        !TextUtil.isEmpty(getCounty()) || !TextUtil.isEmpty(getProvince()) || !TextUtil.isEmpty(getCountry()) ||
        !TextUtil.isEmpty(getAddrUpdatedBy()) || !TextUtil.isEmpty(getAddrUpdatedDt()))  {
            return "INNER";
        }
        return "LEFT OUTER";
    }

    /** Determines whether an affiliate inner or left join is needed.
     * @return The type of join.
     *
     */
    public String getAffiliateLeftOrInner() {
        if(!TextUtil.isEmpty(affType) || !TextUtil.isEmpty(affLocalSubChapter) || !TextUtil.isEmpty(affStateNatType) ||
        !TextUtil.isEmpty(affSubUnit) || !TextUtil.isEmpty(affCouncilRetireeChap) || !TextUtil.isEmpty(affCode) ||
        (!TextUtil.isEmpty(affPk) && affPk.intValue() != 0)) {
            return "INNER";
        }
        return "LEFT OUTER";
    }

    /** Determines whether an email inner or left join is needed.
     * @return The type of join.
     *
     */
    public String getEmailLeftOrInner() {
		if(!TextUtil.isEmpty(getPersonEmailAddr())) {
            return "INNER";
        }
        return "LEFT OUTER";
    }

    /** Determines whether an phone inner or left join is needed.
     * @return The type of join.
     *
     */
    public String getPhoneLeftOrInner() {
		if(!TextUtil.isEmpty(getCountryCode()) || !TextUtil.isEmpty(getAreaCode()) || !TextUtil.isEmpty(getPhoneNumber()))
		{
            return "INNER";
        }
        return "LEFT OUTER";
    }
}
