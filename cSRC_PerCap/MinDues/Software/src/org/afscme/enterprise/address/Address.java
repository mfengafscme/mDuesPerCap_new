
package org.afscme.enterprise.address;

import java.util.Collection;
import java.util.List;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * Contains basic address data common to Member, Affiliate etc... addresses
 */
public class Address
{
    /**
     * City is required, but not provided.
     */
    public static final Integer ERROR_CITY_EMPTY=new Integer(-1);

    /**
     * State is required, but not provided.
     */
    public static final Integer ERROR_STATE_EMPTY=new Integer(-2);

    /**
     * Province was required, but not provided.
     */
    public static final Integer ERROR_PROVINCE_EMPTY=new Integer(-3);

    /**
     * Zip code was required, but not provided.
     */
    public static final Integer ERROR_ZIPCODE_EMPTY=new Integer(-4);

    /**
     * Zip code was provided, but was invalid.
     */
    public static final Integer ERROR_ZIPCODE_INVALID=new Integer(-5);

    /**
     * Zip plus was provided, but was invalid.
     */
    public static final Integer ERROR_ZIPPLUS_INVALID=new Integer(-6);

    /**
     * The zip code provided did not match the state provided.
     */
    public static final Integer ERROR_STATE_ZIP_MISMATCH=new Integer(-7);

    /**
     * The given address cannot be stored as a member's address, as it matches one of the
     *  member's affiliate's addresses.
     */
    public static final Integer ERROR_MATCHES_AFFILIATE=new Integer(-8);

    /**
     * Address was marked primary, but it is not the home address
     */
    public static final Integer ERROR_PRIMARY_NOT_HOME=new Integer(-8);

    /**
     * Order of error codes for use in getErrorXXX methods below
     */
    protected static final Integer[] ERROR_CODES = {
        ERROR_CITY_EMPTY,
        ERROR_STATE_EMPTY,
        ERROR_PROVINCE_EMPTY,
        ERROR_ZIPCODE_EMPTY,
        ERROR_ZIPCODE_INVALID,
        ERROR_ZIPPLUS_INVALID,
        ERROR_STATE_ZIP_MISMATCH,
        ERROR_PRIMARY_NOT_HOME
    };

    /**
     * Order of error fields for use in getErrorFields methods below
     */
    protected static final String[] ERROR_FIELDS= {
        "city",
        "state",
        "province",
        "zipCode",
        "zipCode",
        "zipPlus",
        "zipCode",
        "type"
    };

    /**
     * Order of error message keys for use in getErrorMessages methods below
     */
    protected static final String[] ERROR_MESSAGES = {
        "error.address.city.empty",
        "error.address.state.empty",
        "error.address.province.empty",
        "error.address.zipCode.empty",
        "error.address.zipCode.invalid",
        "error.address.zipPlus.invalid",
        "error.address.zipCode.stateZipMismatch",
        "error.address.primaryNotHome"
    };

    /**
     * Retrieves a list of error field names, given a list of error code Integer objects
     */
    public static List getErrorFields(Collection errorCodes) {
        return CollectionUtil.transliterate(errorCodes, ERROR_CODES, ERROR_FIELDS);
    }

    /**
     * Retrieves a list of error message keys, given a list of error code Integer objects
     */
    public static List getErrorMessages(Collection errorCodes) {
        return CollectionUtil.transliterate(errorCodes, ERROR_CODES, ERROR_MESSAGES);
    }

    /**
     * First line of street address
     */
    protected String addr1;

    /**
     * Second line of street address
     */
    protected String addr2;

    /**
     * City name
     */
    protected String city;

    /**
     * State common code.
     */
    protected String state;

    /**
     * Zip code, or 0 if not present
     */
    protected String zipCode;

    /**
     * Zip plus, or 0 if not present
     */
    protected String zipPlus;

    /**
     * Province name
     */
    protected String province;

    /**
     * The county name
     */
    protected String county;

    /**
     * Carrier route code
     */
    protected String carrierRoute;

    /**
     * Primary key to the country common code.
     */
    protected Integer countryPk;

    public String toString() {
        return "Address[" +
        "addr1="+addr1+", "+
        "addr2="+addr2+", "+
        "city="+city+", "+
        "county="+county+", "+
        "state="+state+", "+
        "province="+province+", "+
        "coutryPk="+countryPk+", "+
        "zipCode="+zipCode+", "+
        "zipPlus="+zipPlus+", "+
        "carrierRoute="+carrierRoute+"]";
    }


    public void copyFrom(Address from) {
        addr1 = from.addr1;
        addr2 = from.addr2;
        city = from.city;
        state = from.state;
        county = from.county;
        province = from.province;
        countryPk = from.countryPk;
        zipCode = from.zipCode;
        zipPlus = from.zipPlus;
        carrierRoute = from.carrierRoute;
    }

    /**
     * Returns true iff the content in this address is equal to the content of the
     * given address.
     * @param address
     * @return boolean
     */
    public boolean equals(Address address)
    {
        return contentEquals(address);
    }

    /**
     * Returns true iff the content in this address is equal to the content of the
     * given address.
     * @param address
     * @return boolean
     */
    public boolean contentEquals(Address address)
    {
        return
            TextUtil.equals(addr1, address.addr1) &&
            TextUtil.equals(addr2, address.addr2) &&
            TextUtil.equals(city, address.city) &&
            TextUtil.equals(county, address.county) &&
            TextUtil.equals(province, address.province) &&
            TextUtil.equals(countryPk, address.countryPk) &&
            TextUtil.equals(carrierRoute, address.carrierRoute) &&
            TextUtil.equals(state, address.state) &&
        	TextUtil.equals(zipCode, address.zipCode) &&
        	TextUtil.equals(zipPlus, address.zipPlus);
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

    /** Getter for property carrierRoute.
     * @return Value of property carrierRoute.
     *
     */
    public String getCarrierRoute() {
        return carrierRoute;
    }

    /** Setter for property carrierRoute.
     * @param carrierRoute New value of property carrierRoute.
     *
     */
    public void setCarrierRoute(String carrierRoute) {
        this.carrierRoute = carrierRoute;
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
        this.countryPk = countryPk;
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

}
