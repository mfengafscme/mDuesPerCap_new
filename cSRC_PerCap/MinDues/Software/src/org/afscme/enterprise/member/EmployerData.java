
package org.afscme.enterprise.member;



/**
 * Represents Employer information for a Member relating to a specific Affiliate.
 */
public class EmployerData 
{
    protected Integer personPk;
    protected Integer affPk;
    protected String employerNm;
    protected Integer jobTitle;
    protected Integer employeeSector;
    protected String jobSite;
    protected java.math.BigDecimal salary;
    protected Integer salaryRange;
    
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
    
    
  
    
    /** Getter for property salary.
     * @return Value of property salary.
     *
     */
    public java.math.BigDecimal getSalary() {
        return salary;
    }
    
    /** Setter for property salary.
     * @param salary New value of property salary.
     *
     */
    public void setSalary(java.math.BigDecimal salary) {
        this.salary = salary;
    }
    
    /** Getter for property employerNm.
     * @return Value of property employerNm.
     *
     */
    public java.lang.String getEmployerNm() {
        return employerNm;
    }    
 
    /** Setter for property employerNm.
     * @param employerNm New value of property employerNm.
     *
     */
    public void setEmployerNm(java.lang.String employerNm) {
        this.employerNm = employerNm;
    }    
    
    /** Getter for property jobTitle.
     * @return Value of property jobTitle.
     *
     */
    public java.lang.Integer getJobTitle() {
        return jobTitle;
    }
    
    /** Setter for property jobTitle.
     * @param jobTitle New value of property jobTitle.
     *
     */
    public void setJobTitle(java.lang.Integer jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    /** Getter for property employeeSector.
     * @return Value of property employeeSector.
     *
     */
    public java.lang.Integer getEmployeeSector() {
        return employeeSector;
    }
    
    /** Setter for property employeeSector.
     * @param employeeSector New value of property employeeSector.
     *
     */
    public void setEmployeeSector(java.lang.Integer employeeSector) {
        this.employeeSector = employeeSector;
    }
    
    /** Getter for property salaryRange.
     * @return Value of property salaryRange.
     *
     */
    public java.lang.Integer getSalaryRange() {
        return salaryRange;
    }
    
    /** Setter for property salaryRange.
     * @param salaryRange New value of property salaryRange.
     *
     */
    public void setSalaryRange(java.lang.Integer salaryRange) {
        this.salaryRange = salaryRange;
    }
          
    /** Setter for property jobSite.
     * @param jobSite New value of property jobSite.
     *
     */
    public void setJobSite(java.lang.String jobSite) {
        this.jobSite = jobSite;
    }
    
    /** Setter for property jobSite.
     * @param jobSite New value of property jobSite.
     *
     */
    public String getJobSite() {
        return jobSite;
    }
    
}
