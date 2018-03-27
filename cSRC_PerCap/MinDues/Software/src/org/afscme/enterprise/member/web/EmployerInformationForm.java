
package org.afscme.enterprise.member.web;

// Afscme imports
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.afscme.enterprise.member.EmployerData;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 * Suports the Employer Information and Employer Information - Edit pages for a Member relating to a specific Affiliate.
 */

/**
 * @struts:form name="employerInformationForm"
 */
public class EmployerInformationForm extends org.apache.struts.action.ActionForm
{
    private static Logger logger =  Logger.getLogger(EmployerInformationForm.class);        
    
    private Integer personPk;
    private Integer affPk;
    private String employerNm;
    private Integer jobTitle;
    private Integer employeeSector;
    private String jobSite;
    private String salary;
    private Integer salaryRange;

    public EmployerData getEmployerData() {

        EmployerData data = new EmployerData();
        data.setAffPk(this.getAffPk());
        data.setPersonPk(this.getPersonPk());
        data.setEmployerNm(this.getEmployerNm());
        data.setJobTitle(this.getJobTitle());
        data.setEmployeeSector(this.getEmployeeSector());
        data.setJobSite(this.getJobSite());
        if (!TextUtil.isEmptyOrSpaces(this.getSalary()))
        {
            data.setSalary(new java.math.BigDecimal(removeInvalidCharacter(this.getSalaryString())));
        }
        else data.setSalary(null);
        data.setSalaryRange(this.getSalaryRange());
        return data;
    }

    public void setEmployerData (EmployerData data) {
        this.setAffPk(data.getAffPk());
        this.setPersonPk(data.getPersonPk());
        this.setEmployerNm(data.getEmployerNm());
        this.setJobTitle(data.getJobTitle());
        this.setEmployeeSector(data.getEmployeeSector());
        this.setJobSite(data.getJobSite());
        if (data.getSalary() != null) this.setSalary(data.getSalary().toString());
            else this.setSalary(null);
        this.setSalaryRange(data.getSalaryRange());


    }

    private String removeInvalidCharacter(String value)
    {

        String objRegExp = "[\\$, ]";
        //replace all matches with empty strings
        value = value.replaceAll(objRegExp,"");
    
        return value;
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();        
        
        if (TextUtil.isEmpty(salary) && TextUtil.isEmpty(salaryRange))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.required.generic", "Either Salary or Salary Range"));
        }
        else if(!TextUtil.isEmpty(salary) && !TextUtil.isEmpty(salaryRange)) 
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.mutuallyExclusive.generic", "Salary", "Salary Range"));
	}
        else if(salary != null && !TextUtil.isEmpty(salary))            
        {
            this.currencyFormat(errors, salary);    
        }
               
        return errors;

    }

    // toString or not to string
      public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");

        buf.append("personPk: " + personPk);
        buf.append(", affPk: " + affPk);
        buf.append(", employerNm: " + employerNm);
        buf.append(", jobTitle: " + jobTitle);
        buf.append(", employeeSector: " + employeeSector);
        buf.append(", jobSite: " + jobSite);
        buf.append(", salary: " + salary);
        buf.append(", salaryRange: " + salaryRange);

        return buf.toString()+"]";
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
     * @return Value of property salary that is NOT formated.
     *
     */
    public String getSalaryString() {
        return this.salary;
    }
    
    /** Getter for property salary.
     * @return Value of property salary.
     *
     */
    public String getSalary() {
        
        return this.formatDollar(this.salary);

    }



    /** Setter for property salary that takes a String as a parameter.
     * @param salary New value of property salary.
     *
     */
    public void setSalary(String salary) {
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
        if (jobTitle != null && jobTitle.intValue() == 0) this.jobTitle = null;
        else this.jobTitle = jobTitle;
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
        if (employeeSector != null && employeeSector.intValue() == 0) this.employeeSector = null;
        else this.employeeSector = employeeSector;
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
        if (salaryRange != null && salaryRange.intValue() == 0) this.salaryRange = null;
        else this.salaryRange = salaryRange;
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
    
    public String formatDollar(String value)
    {

        String newValue = "";
        
        if (value != null && value.length() > 0)
        {
            value = removeInvalidCharacter(value);            

            Number num = null;
            NumberFormat dollar = null;

          try{

            dollar = NumberFormat.getCurrencyInstance();
            dollar.setMaximumFractionDigits(2);
            dollar.setMinimumFractionDigits(2);

            NumberFormat form = NumberFormat.getInstance();            
            num = form.parse(value);     
            newValue = dollar.format(num.doubleValue());
          }catch(ParseException e)
          { 
            logger.debug("Parse Exception");                    
            e.printStackTrace();
          }
        }
          return newValue;        
    }

    /* JZhang
     * @parm errors: ActionErrors object
     * @parm value: value that need to be verified
     */
    private void currencyFormat(ActionErrors errors, String value) 
    {
        try
        {
            value = removeInvalidCharacter(value);
            //100   100,000.00  100.00  $100    $100.00
            boolean match = Pattern.matches("^((\\$\\d*)|(\\$\\d*\\.\\d{2})|(\\d*)|(\\d*\\.\\d{2}))$", value);
            if (match == false ){
                logger.debug("EmployerInformationForm:currencyFormat -- An error is added.");
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.required.currencyFormat", "Salary"));
            }
        }catch (PatternSyntaxException pse)
        {
            logger.debug("EmployerInformationForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
    }    
    
}
