package org.afscme.enterprise.update;

import java.io.Serializable;

/**
 * Represents the data in the address element in the update file.
 */
public class AddressElement implements Serializable {
    
    protected String addr1 = null;
    protected String addr2 = null;
    protected String city = null;
    protected String state = null;
    protected String province = null;
    protected String zipCode = null;
    protected String zipPlus = null;
    protected String country = null;
    
    public String toString() {
        return "AddressElement[" + 
                    "addr1=" + addr1 + 
                    ", addr2=" + addr2 + 
                    ", city=" + city + 
                    ", state=" + state + 
                    ", province=" + province +                     
                    ", zipCode=" + zipCode + 
                    ", zipPlus=" + zipPlus +
                    ", country=" + country +                    
                "]"
        ;
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
    
    /** Getter for property country.
     * @return Value of property country.
     *
     */
    public String getCountry() {
        return country;
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     *
     */
    public void setCountry(String country) {
        this.country = country;
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
    
}
