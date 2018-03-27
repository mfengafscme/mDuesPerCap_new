package org.afscme.enterprise.person.web;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.person.PersonCriteria;
import org.apache.log4j.Logger;

/**
 * Represents the parameters form when the user is entering verify person criteria
 *
 * @struts:form name="verifyPersonForm"
 */
public class VerifyPersonForm extends SearchForm {
    
    private static Logger logger =  Logger.getLogger(VerifyPersonForm.class);      
    
    private String m_firstNm;
    private String m_lastNm;
    private Integer m_suffixNm;
    private String m_dob;
    private String m_ssn;
    
    // Social Security Number - Area Number - first 3 digits
    private String m_ssn1;
    // Social Security Number - Group Number - middle 2 digits
    private String m_ssn2;
    // Social Security Number - Serial Number - last 4 digits
    private String m_ssn3;

    /** Collection of PersonResult objects */
    private Collection m_personResult;
    
    private String personNm;
    private String personAddr;
    private String personAddrCity;
    private String personAddrState;
    private String personAddrPostalCode;

    private String back;
   
    

    public VerifyPersonForm() {
        newSearch();
    }
    
    /** resets the search values to the default */
    public void newSearch() {
        m_firstNm=null;
        m_lastNm=null;
        m_suffixNm=null;
        m_dob=null;
        m_ssn=null;
        m_ssn1=null;
        m_ssn2=null;
        m_ssn3=null;
        order=1;
        page=0;
        total=0;
        sortBy="personNm";
    }
    
    /** Getter for property personResult.
     * @return Value of property personResult.
     *
     */
    public java.util.Collection getPersonResult() {
        return m_personResult;
    }
    
    /**
     * Setter for property personResult 
     * @param personResult New value of property personResult.
     */
    public void setPersonResult(Collection personResult) {
        this.m_personResult = personResult;
    }

    /**
     * getPersonCriteriaData method to copy all the person 
     * data fields to the criteria object to process.
     */
    public PersonCriteria getPersonCriteriaData() {

        PersonCriteria data = new PersonCriteria();

        if (!TextUtil.isEmpty(m_firstNm)) data.setFirstNm(m_firstNm);
        if (!TextUtil.isEmpty(m_lastNm)) data.setLastNm(m_lastNm);
        if (!TextUtil.isEmpty(m_ssn)) data.setSsn(m_ssn);
        try {
            if (!TextUtil.isEmpty(m_dob)) data.setDob(TextUtil.parseDate(m_dob));
        } catch (Exception e) {
            data.setDob(null);
        }
        data.setSuffixNm(m_suffixNm);

            data.setPage(page);
            data.setPageSize(getPageSize());
		
        logger.debug("----------------------------------------------------------------------");
        logger.debug("VerifyPersonForm:getPersonCriteriaData sortBy="+sortBy);
        logger.debug("----------------------------------------------------------------------");
            if (sortBy.equals("personNm"))
                data.setSortField(PersonCriteria.FIELD_NAME);
            else if (sortBy.equals("personAddr"))
                data.setSortField(PersonCriteria.FIELD_ADDR);
            else if (sortBy.equals("personAddrCity"))
                data.setSortField(PersonCriteria.FIELD_CITY);
            else if (sortBy.equals("personAddrState"))
                data.setSortField(PersonCriteria.FIELD_STATE);
            else if (sortBy.equals("personAddrPostalCode"))
                data.setSortField(PersonCriteria.FIELD_POSTALCODE);
            else if (sortBy.equals("ssn"))
                data.setSortField(PersonCriteria.FIELD_SSN);

            data.setSortOrder(order);
                    
        return data;
    }

    /**
     * toString method to convert all the data fields of the class
     * to be printable.
     */    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("firstNm: " + m_firstNm);
        buf.append(", lastNm: " + m_lastNm);
        buf.append(", suffixNm: " + m_suffixNm);
        buf.append(", dob: " + m_dob);
        buf.append(", ssn: " + m_ssn);
        buf.append(", ssn1: " + m_ssn1);
        buf.append(", ssn2: " + m_ssn2);
        buf.append(", ssn3: " + m_ssn3);
        buf.append(", order: " + order);
        buf.append(", sortBy: " + sortBy);
        buf.append(", page: " + page);
        buf.append(", total: " + total);
        buf.append(", back: " + back);
        return buf.toString()+"]";
    }
    
    /**
     * validation method for this form
     */    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        logger.debug("VerifyPersonForm:validate firstNm="+m_firstNm);
        logger.debug("VerifyPersonForm:validate lastNm="+m_lastNm);
        logger.debug("VerifyPersonForm:validate ssn="+getSsn());
      ActionErrors errors = new ActionErrors();
	if (m_firstNm == null && m_lastNm == null && getSsn() == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } else if (m_firstNm != null && m_lastNm == null && getSsn() == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } else if (m_firstNm == null && m_lastNm != null && getSsn() == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } 
        
        if (m_firstNm != null && m_firstNm.length() > 0)
        {
            this.nameMatch(errors, m_firstNm, "firstNm");            
        }	
	return errors;
    }
    
    /** Getter for property m_dob.
     * @return Value of property m_dob.
     *
     */
    public java.lang.String getDob() {
        return m_dob;
    }
    
    /** Setter for property m_dob.
     * @param m_dob New value of property m_dob.
     *
     */
    public void setDob(java.lang.String m_dob) {
        this.m_dob = m_dob;
    }
    
    /** Getter for property m_firstNm.
     * @return Value of property m_firstNm.
     *
     */
    public java.lang.String getFirstNm() {
        return m_firstNm;
    }
    
    /** Setter for property m_firstNm.
     * @param m_firstNm New value of property m_firstNm.
     *
     */
    public void setFirstNm(java.lang.String m_firstNm) {
        if (TextUtil.isEmptyOrSpaces(m_firstNm)) {
            this.m_firstNm = null;
        } else {
            this.m_firstNm = m_firstNm;
        }
    }
    
    /** Getter for property m_lastNm.
     * @return Value of property m_lastNm.
     *
     */
    public java.lang.String getLastNm() {
        return m_lastNm;
    }
    
    /** Setter for property m_lastNm.
     * @param m_lastNm New value of property m_lastNm.
     *
     */
    public void setLastNm(java.lang.String m_lastNm) {
        if (TextUtil.isEmptyOrSpaces(m_lastNm)) {
            this.m_lastNm = null;
        } else {
            this.m_lastNm = m_lastNm;
        }
    }
    
    /** Getter for property m_ssn.
     * @return Value of property m_ssn.
     *
     */
    public java.lang.String getSsn() {
        if (m_ssn1==null || m_ssn2==null || m_ssn3==null) {
            m_ssn = null;
        }else if (TextUtil.isEmptyOrSpaces(m_ssn1) || TextUtil.isEmptyOrSpaces(m_ssn2) || TextUtil.isEmptyOrSpaces(m_ssn3)) {
            m_ssn = null;
        }else m_ssn = m_ssn1 + m_ssn2 + m_ssn3;
        
        return m_ssn;
    }
    
    /** Setter for property m_ssn.
     * @param m_ssn New value of property m_ssn.
     *
     */
    public void setSsn(java.lang.String m_ssn) {
        if (TextUtil.isEmptyOrSpaces(m_ssn)) {
            this.m_ssn = null;
        } else {
            this.m_ssn = m_ssn;
        }
    }
    
    /** Getter for property m_suffixNm.
     * @return Value of property m_suffixNm.
     *
     */
    public java.lang.Integer getSuffixNm() {
/*        if(m_suffixNm.equals(new Integer(0))){
            m_suffixNm = null;
        }
*/        return m_suffixNm;
    }
    
    /** Setter for property m_suffixNm.
     * @param m_suffixNm New value of property m_suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer m_suffixNm) {
        this.m_suffixNm = m_suffixNm;
    }
    
    /** Getter for property m_ssn1.
     * @return Value of property m_ssn1.
     *
     */
    public java.lang.String getSsn1() {
        return m_ssn1;
    }
    
    /** Setter for property m_ssn1.
     * @param m_ssn1 New value of property m_ssn1.
     *
     */
    public void setSsn1(java.lang.String m_ssn1) {
        this.m_ssn1 = m_ssn1;
    }
    
    /** Getter for property m_ssn2.
     * @return Value of property m_ssn2.
     *
     */
    public java.lang.String getSsn2() {
        return m_ssn2;
    }
    
    /** Setter for property m_ssn2.
     * @param m_ssn2 New value of property m_ssn2.
     *
     */
    public void setSsn2(java.lang.String m_ssn2) {
        this.m_ssn2 = m_ssn2;
    }
    
    /** Getter for property m_ssn3.
     * @return Value of property m_ssn3.
     *
     */
    public java.lang.String getSsn3() {
        return m_ssn3;
    }
    
    /** Setter for property m_ssn3.
     * @param m_ssn3 New value of property m_ssn3.
     *
     */
    public void setSsn3(java.lang.String m_ssn3) {
        this.m_ssn3 = m_ssn3;
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
    
    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: name on jsp
     */
    private void nameMatch(ActionErrors errors, String name, String prop) 
    {
        try
        {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9]{0,24})", name);
            if (match == false ){
                logger.debug("VerifyPersonForm:nameMatch -- An error is added.");
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.incorrect.name", "First Name"));
            }
        } catch (PatternSyntaxException pse)
        {
            logger.debug("VerifyPersonForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
    }    

}
