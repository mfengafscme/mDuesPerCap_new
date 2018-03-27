package org.afscme.enterprise.affiliate.staff;

import java.sql.Timestamp;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.CommentData;
import java.util.Collection;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.SortData;

public class StaffCriteria
{
    protected String firstName;
    protected String lastName;
    protected String ssn;
    protected Integer suffixName;
    protected Timestamp dob;
    protected SortData sortData;
    
    
    public StaffCriteria() {
        sortData = new SortData();
    }
    /** Getter for property dob.
     * @return Value of property dob.
     *
     */
    public java.sql.Timestamp getDob() {
        return dob;
    }
    
    /** Setter for property dob.
     * @param dob New value of property dob.
     *
     */
    public void setDob(java.sql.Timestamp dob) {
        this.dob = dob;
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
    
    /** Getter for property sortData.
     * @return Value of property sortData.
     *
     */
    public org.afscme.enterprise.common.SortData getSortData() {
        return sortData;
    }
    
    /** Setter for property sortData.
     * @param sortData New value of property sortData.
     *
     */
    public void setSortData(org.afscme.enterprise.common.SortData sortData) {
        this.sortData = sortData;
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
    
    /** Getter for property ssn.
     * @return Value of property ssn.
     *
     */
    public java.lang.String getSsn() {
        return ssn;
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     *
     */
    public void setSsn(java.lang.String ssn) {
        this.ssn = ssn;
    }
    
    /** Getter for property suffixName.
     * @return Value of property suffixName.
     *
     */
    public java.lang.Integer getSuffixName() {
        return suffixName;
    }
    
    /** Setter for property suffixName.
     * @param suffixName New value of property suffixName.
     *
     */
    public void setSuffixName(java.lang.Integer suffixName) {
        this.suffixName = suffixName;
    }
    
}
