package org.afscme.enterprise.address;


/** Used to store information about a zip code for address validation lookups. */
public class ZipCodeEntry {
    
    /** Zip code key for this entry */    
    private String zipCode;
    /** Name of the preferred city for that zip code. */    
    private String city;
    /** The state for this zip code */    
    private String state;
    
    public String toString() {
        return "ZipCodeEntry[zipCode=" + zipCode + ", city=" + city + ", state=" + state;
    }
    
    /** Getter for property zipCode.
     * @return Value of property zipCode.
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
    
}
    