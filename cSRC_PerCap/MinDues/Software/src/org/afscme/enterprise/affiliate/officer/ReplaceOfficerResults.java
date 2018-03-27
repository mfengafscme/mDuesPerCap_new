package org.afscme.enterprise.affiliate.officer;

import java.io.Serializable;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;


/**
 * Represents elements on the Affiliate Officer Maintenance screen
 */
public class ReplaceOfficerResults implements Serializable {
   
    
    public ReplaceOfficerResults () {
        super();
    }    
    
    private Integer personPk;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer suffix;
    private AffiliateIdentifier ai;
    private String addr1;
    private String addr2;    
    private String city;
    private String state;
    private String zip;
    private Integer affPk;
    
    
    
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public Integer getPersonPk() {
        return personPk;
    }    
    
    /** Setter for property personPk.
     * @param officerPersonPk New value of property personPk.
     *
     */
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
    }        
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     *
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param firstName New value of property middleName.
     *
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }    
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     *
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
                     
    /** Getter for property suffix.
     * @return Value of property suffix.
     *
     */
    public java.lang.Integer getSuffix() {
        return suffix;
    }
    
    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     *
     */
    public void setSuffix(java.lang.Integer suffix) {
        this.suffix = suffix;
    }    
    
    /** Getter for property ai
     * @return Value of property ai.
     *
     */
    public AffiliateIdentifier getAi() {
        return ai;
    }
    
    /** Setter for property ai.
     * @param ai New value of property ai.
     *
     */
    public void setAi(AffiliateIdentifier ai) {
        this.ai = ai;
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
     * @param city New value of property state.
     *
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }          
    
    /** Getter for property zip.
     * @return Value of property zip.
     *
     */
    public java.lang.String getZip() {
        return zip;
    }
    
    /** Setter for property zip.
     * @param city New value of property zip.
     *
     */
    public void setZip(java.lang.String zip) {
        this.zip = zip;
    }          
    
   /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }    
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }        
    
}
