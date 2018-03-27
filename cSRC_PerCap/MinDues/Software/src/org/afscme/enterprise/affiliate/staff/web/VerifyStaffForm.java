package org.afscme.enterprise.affiliate.staff.web;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.staff.StaffCriteria;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.affiliate.staff.StaffResult;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.PersonCriteria;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * @struts:form name="verifyStaffForm"
 */
public class VerifyStaffForm extends SearchForm {

    protected String dob;
    protected String firstNm;
    protected String lastNm;
    protected Integer suffixNm;
    protected String ssn1;
    protected String ssn2;
    protected String ssn3;
    protected String m_ssn;    

    public void clear() {
        dob = null;
        firstNm = null;
        lastNm = null;
        ssn1 = null;
        ssn2 = null;
        ssn3 = null;
        m_ssn = null;
        suffixNm = null;
        page = 0;
        sortBy = null;
        order = 0;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
	if (TextUtil.isEmptyOrSpaces(firstNm) && TextUtil.isEmptyOrSpaces(lastNm) && TextUtil.isEmptyOrSpaces(getSsn())) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } else if (!TextUtil.isEmptyOrSpaces(firstNm) && TextUtil.isEmptyOrSpaces(lastNm) && TextUtil.isEmptyOrSpaces(getSsn())) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } else if (TextUtil.isEmptyOrSpaces(firstNm) && !TextUtil.isEmptyOrSpaces(lastNm) && TextUtil.isEmptyOrSpaces(getSsn())) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.verify.required"));
        } 
        
        if (firstNm != null && lastNm.length() > 0)
        {
            this.nameMatch(errors, firstNm, "firstNm");            
        }	
	return errors;
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
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.field.incorrect.name", "First Name"));
            }
        } catch (PatternSyntaxException pse)
        {
            // do nothing
        }       
    }       
    
    public StaffCriteria getStaffCriteria() {

        StaffCriteria criteria = new StaffCriteria();

        if (!TextUtil.isEmpty(firstNm)) criteria.setFirstName(firstNm);
        if (!TextUtil.isEmpty(lastNm)) criteria.setLastName(lastNm);
        if (!TextUtil.isEmpty(ssn1)) criteria.setSsn(ssn1+ssn2+ssn3);
        if (suffixNm != null && suffixNm.intValue() != 0) criteria.setSuffixName(suffixNm);
        
        try {
            if (!TextUtil.isEmpty(dob)) criteria.setDob(TextUtil.parseDate(dob));
        } catch (ParseException e) {
        }
        
        SortData sortData = criteria.getSortData();
        sortData.setPage(page);
        sortData.setSortField(StaffResult.sortStringToCode(sortBy));
        sortData.setDirection(order);
        
        return criteria;
    }
    
    
    /** Getter for property m_ssn.
     * @return Value of property m_ssn.
     *
     */
    public java.lang.String getSsn() {
        if (ssn1==null || ssn2==null || ssn3==null) {
            m_ssn = null;
        }else if (TextUtil.isEmptyOrSpaces(ssn1) || TextUtil.isEmptyOrSpaces(ssn2) || TextUtil.isEmptyOrSpaces(ssn3)) {
            m_ssn = null;
        }else m_ssn = ssn1 + ssn2 + ssn3;
        
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
    
    /** Getter for property dob.
     * @return Value of property dob.
     *
     */
    public java.lang.String getDob() {
        return dob;
    }
    
    /** Setter for property dob.
     * @param dob New value of property dob.
     *
     */
    public void setDob(java.lang.String dob) {
        this.dob = dob;
    }
    
    /** Getter for property firstNm.
     * @return Value of property firstNm.
     *
     */
    public java.lang.String getFirstNm() {
        return firstNm;
    }
    
    /** Setter for property firstNm.
     * @param firstNm New value of property firstNm.
     *
     */
    public void setFirstNm(java.lang.String firstNm) {
        this.firstNm = firstNm;
    }
    
    /** Getter for property lastNm.
     * @return Value of property lastNm.
     *
     */
    public java.lang.String getLastNm() {
        return lastNm;
    }
    
    /** Setter for property lastNm.
     * @param lastNm New value of property lastNm.
     *
     */
    public void setLastNm(java.lang.String lastNm) {
        this.lastNm = lastNm;
    }
    
    /** Getter for property ssn1.
     * @return Value of property ssn1.
     *
     */
    public java.lang.String getSsn1() {
        return ssn1;
    }
    
    /** Setter for property ssn1.
     * @param ssn1 New value of property ssn1.
     *
     */
    public void setSsn1(java.lang.String ssn1) {
        this.ssn1 = ssn1;
    }
    
    /** Getter for property ssn2.
     * @return Value of property ssn2.
     *
     */
    public java.lang.String getSsn2() {
        return ssn2;
    }
    
    /** Setter for property ssn2.
     * @param ssn2 New value of property ssn2.
     *
     */
    public void setSsn2(java.lang.String ssn2) {
        this.ssn2 = ssn2;
    }
    
    /** Getter for property ssn3.
     * @return Value of property ssn3.
     *
     */
    public java.lang.String getSsn3() {
        return ssn3;
    }
    
    /** Setter for property ssn3.
     * @param ssn3 New value of property ssn3.
     *
     */
    public void setSsn3(java.lang.String ssn3) {
        this.ssn3 = ssn3;
    }
    
    /** Getter for property suffixNm.
     * @return Value of property suffixNm.
     *
     */
    public java.lang.Integer getSuffixNm() {
        return suffixNm;
    }    
    
    /** Setter for property suffixNm.
     * @param suffixNm New value of property suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer suffixNm) {
        this.suffixNm = suffixNm;
    }
    
}
