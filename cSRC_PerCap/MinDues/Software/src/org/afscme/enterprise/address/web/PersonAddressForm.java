package org.afscme.enterprise.address.web;

import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.codes.Codes.PersonAddressType;
import org.afscme.enterprise.codes.Codes.Country;


/**
 * @struts:form name="personAddressForm"
 */
public class PersonAddressForm extends SearchForm
{
    private Integer addrPk; 
    
    private String addr1;
    private String addr2;
    private String city;
    private String county;
    private String province;
    private String state;
    private Integer countryPk;
    private String zipCode;
    private String zipPlus;
    private Integer type;
    private boolean primary;
    private boolean privateFg;
    private boolean bad;
    private Timestamp badDate;
    private RecordData recordData;
    private Integer oldType; 
    
    private String back;
	
    public void setPersonAddress(PersonAddressRecord pa) {
        addr1 = pa.getAddr1();
        addr2 = pa.getAddr2();
        city = pa.getCity();
        county = pa.getCounty();
        province = pa.getProvince();
        state= pa.getState();
        countryPk = pa.getCountryPk();
        zipCode = pa.getZipCode();
        zipPlus = pa.getZipPlus();
        type = pa.getType();
        privateFg = pa.isPrivate();
        primary = pa.isPrimary();
        bad = pa.isBad();
        badDate = pa.getBadDate();
        recordData = pa.getRecordData();
    }
        
    public PersonAddress getPersonAddress() {
        PersonAddress pa = new PersonAddress();
        pa.setAddr1(addr1);
        pa.setAddr2(addr2);
        pa.setCity(city);
        pa.setCounty(county);
        pa.setProvince(province);
        pa.setState(state);
        pa.setCountryPk(countryPk);
        pa.setZipCode(zipCode);
        pa.setZipPlus(zipPlus);
        pa.setType(type);
        pa.setPrivate(privateFg);
        pa.setPrimary(primary);
        pa.setBad(bad);
        return pa;
    }
    
    public String toString() {
        return
            "[addrPk=" + addrPk + ", " +
            "addr1=" + addr1 + ", " +
            "addr2=" + addr2 + ", " +
            "city=" + city + ", " +
            "state=" + state + ", " +
            "countryPk=" + countryPk + ", " +
            "zipCode=" + zipCode + ", " +
            "zipPlus=" + zipPlus + ", " +
            "primary=" + primary + ", " +
            "private=" + privateFg + ", " + 
            "type=" + type + ", " +
            "bad=" + bad + ", " +
            "badDate=" + badDate + ", ";
    }
    

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if (countryPk != null && countryPk.equals(Country.US))
        {
            if (!TextUtil.isEmpty(zipCode) && !TextUtil.isInt(zipCode))
                errors.add("zipCode", new ActionError("error.address.zipCode.invalid"));
            if (!TextUtil.isEmpty(zipPlus) && !TextUtil.isInt(zipPlus))
                errors.add("zipPlus", new ActionError("error.address.zipPlus.invalid"));
        }
        if ((type != null) && (!type.equals(PersonAddressType.HOME)) && (isPrimary())) {
            errors.add("type", new ActionError("error.address.primaryNotHome"));
            if (getOldType() != null && getOldType().equals(PersonAddressType.HOME))
                setType(PersonAddressType.HOME);
        }
        return errors;
    }
    
    /**
     * Returns true iff this form is being used for an add operation (as opposed to edit)
     */
    public boolean isAdd() {
        return addrPk == null || addrPk.intValue() == 0;
    }
        
    /** Getter for property addr1.
     * @return Value of property addr1.
     *
     */
    public String getAddr1() {
        return addr1;
    }
    
    /** Setter for property addr1.
     * @param addr1 New value of property addr1.
     *
     */
    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }
    
    /** Getter for property addr2.
     * @return Value of property addr2.
     *
     */
    public String getAddr2() {
        return addr2;
    }
    
    /** Setter for property addr2.
     * @param addr2 New value of property addr2.
     *
     */
    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }
    
    /** Getter for property city.
     * @return Value of property city.
     *
     */
    public String getCity() {
        return city;
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     *
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /** Getter for property zipPlus.
     * @return Value of property zipPlus.
     *
     */
    public String getZipPlus() {
        return zipPlus;
    }
    
    /** Setter for property zipPlus.
     * @param zipPlus New value of property zipPlus.
     *
     */
    public void setZipPlus(String zipPlus) {
        this.zipPlus = zipPlus;
    }
    
    /** Getter for property zipCode.
     * @return Value of property zipCode.
     *
     */
    public String getZipCode() {
        return zipCode;
    }
    
    /** Setter for property zipCode.
     * @param zipCode New value of property zipCode.
     *
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     *
     */
    public String getState() {
        return state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     *
     */
    public void setState(String state) {
            this.state = state;
    }
    
    /** Getter for property countryPk.
     * @return Value of property countryPk.
     *
     */
    public Integer getCountryPk() {
        return countryPk;
    }
    
    /** Setter for property countryPk.
     * @param countryPk New value of property countryPk.
     *
     */
    public void setCountryPk(Integer countryPk) {
        if (countryPk != null && countryPk.intValue() != 0)
            this.countryPk = countryPk;
        else
            this.countryPk = null;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public Integer getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(Integer type) {
        this.type = type;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public Integer getOldType() {
        return oldType;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setOldType(Integer oldType) {
        this.oldType = oldType;
    }    
    
    /** Getter for property primary.
     * @return Value of property primary.
     *
     */
    public boolean isPrimary() {
        return primary;
    }
    
    /** Setter for property primary.
     * @param primary New value of property primary.
     *
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
    
    /** Getter for property privateFg.
     * @return Value of property privateFg.
     *
     */
    public boolean isPrivate() {
        return privateFg;
    }
    
    /** Setter for property privateFg.
     * @param privateFg New value of property privateFg.
     *
     */
    public void setPrivate(boolean privateFg) {
        this.privateFg = privateFg;
    }
    
    /** Getter for property bad.
     * @return Value of property bad.
     *
     */
    public boolean isBad() {
        return bad;
    }
    
    /** Setter for property bad.
     * @param bad New value of property bad.
     *
     */
    public void setBad(boolean bad) {
        this.bad = bad;
    }
    
    /** Getter for property badDate.
     * @return Value of property badDate.
     *
     */
    public Timestamp getBadDate() {
        return badDate;
    }
    
    /** Setter for property badDate.
     * @param badDate New value of property badDate.
     *
     */
    public void setBadDate(Timestamp badDate) {
        this.badDate = badDate;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public RecordData getRecordData() {
        if (recordData == null)
            recordData = new RecordData();
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
    }
    
    /** Getter for property county.
     * @return Value of property county.
     *
     */
    public String getCounty() {
        return county;
    }
    
    /** Setter for property county.
     * @param county New value of property county.
     *
     */
    public void setCounty(String county) {
        this.county = county;
    }
    
    /** Getter for property province.
     * @return Value of property province.
     *
     */
    public String getProvince() {
        return province;
    }
    
    /** Setter for property province.
     * @param province New value of property province.
     *
     */
    public void setProvince(String province) {
        this.province = province;
    }
    
    /** Getter for property back.
     * @return Value of property back.
     *
     */
    public java.lang.String getBack() {
        return back;
    }
    
    /** Setter for property back.
     * @param back New value of property back.
     *
     */
    public void setBack(java.lang.String back) {
        this.back = back;
    }
    
    /** Getter for property addrPk.
     * @return Value of property addrPk.
     *
     */
    public java.lang.Integer getAddrPk() {
        return addrPk;
    }
    
    /** Setter for property addrPk.
     * @param addrPk New value of property addrPk.
     *
     */
    public void setAddrPk(java.lang.Integer addrPk) {
        this.addrPk = addrPk;
    }
    
}



