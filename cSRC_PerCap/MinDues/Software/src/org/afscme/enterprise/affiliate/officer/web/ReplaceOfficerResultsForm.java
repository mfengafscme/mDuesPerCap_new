/*
 * ReplaceOfficerResultsForm.java
 *
 * Created on December 16, 2003, 12:51 PM
 */

package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.affiliate.AffiliateCriteria;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;

/**
 *
 * @struts:form name="replaceOfficerResultsForm"
 */
public class ReplaceOfficerResultsForm extends SearchForm {
    
     private String firstName; 
     private String middleName;
     private String lastName;
     private Integer suffix;
     private Integer personPk;
     private Integer affPk;
     private Boolean elected;
    
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
     * @param middleName New value of property middleName.
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
     * @param last New value of property lastName.
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
     
   /** Getter for property elected.
     * @return Value of property elected.
     *
     */
    public Boolean isElected() {
        return elected;
    }    
    
    /** Setter for property elected.
     * @param expirationMonth New value of property elected.
     *
     */
    public void setElected(Boolean elected) {
        this.elected = elected;
    }            
    
}
