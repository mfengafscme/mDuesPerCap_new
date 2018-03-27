package org.afscme.enterprise.organization.web;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.organization.OrganizationAssociateData;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.web.WebUtil;
import org.apache.log4j.Logger;


/**
 * Represents the form for an individual Organization Associate Detail
 *
 * @struts:form name="organizationAssociateDetailForm"
 */
public class OrganizationAssociateDetailForm extends ActionForm {

    private static Logger logger =  Logger.getLogger(OrganizationAssociateDetailForm.class);    
    
    private OrganizationAssociateData organizationAssociateData;
    
    private String comment;
    private boolean update;
    private boolean newPerson;
    private boolean ignoreSsnDup;    
    
    // Social Security Number - Area Number - first 3 digits
    private String ssn1;
    // Social Security Number - Group Number - middle 2 digits
    private String ssn2;
    // Social Security Number - Serial Number - last 4 digits
    private String ssn3;

	private String previousSsn;
	
    public OrganizationAssociateDetailForm() {
        organizationAssociateData = new OrganizationAssociateData();
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
        //reset the ssnValid checkbox for unchecked possibility
        PersonData personData = organizationAssociateData.getPersonData();
        personData.setSsnValid(new Boolean(false));
        organizationAssociateData.setPersonData(personData);
    }
    
    public String toString() {
        return
            "organizationAssociateData="+organizationAssociateData+", "+
            "comment="+comment+", "+
            "update="+update+", "+
            "newPerson="+newPerson+", "+
            "ignoreSsnDup="+ignoreSsnDup+", "+
            "ssn1="+ssn1+", "+
            "ssn2="+ssn2+", "+            
            "ssn3="+ssn3;
    }

    /** Returns true if this organization associate is a new Person
     * @return true if newPerson property has been set.
     *
     */    
    public boolean isNewPerson() {
        return newPerson;
    }

    /** Getter for new Person data
     * @return Value of property NewPerson.
     *
     */
    public NewPerson getNewPerson() {
        return new NewPerson(organizationAssociateData.getPersonData());
    }

    /** Setter for property newPerson.
     * @param newPerson New value of property newPerson.
     *
     */
    public void setNewPerson(boolean newPerson) {
        this.newPerson = newPerson;
    }
    
    /** Getter for property update.
     * @return Value of property update.
     *
     */
    public boolean isUpdate() {
        return update;
    }

    /** Setter for property update.
     * @param update New value of property update.
     *
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    /** Getter for property ignoreSsnDup.
     * @return Value of property ignoreSsnDup.
     *
     */
    public boolean isIgnoreSsnDup() {
        return ignoreSsnDup;
    }
    
    /** Setter for property ignoreSsnDup.
     * @param ignoreSsnDup New value of property ignoreSsnDup.
     *
     */
    public void setIgnoreSsnDup(boolean ignoreSsnDup) {
        this.ignoreSsnDup = ignoreSsnDup;
    }
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public String getComment() {
        return comment;
    }

    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /** Getter for property personData.
     * @return Value of property personData.
     *
     */
    public PersonData getPersonData() {
        return organizationAssociateData.getPersonData();
    }

    /** Setter for property personData.
     * @param personData New value of property personData.
     *
     */
    public void setPersonData(PersonData personData) {
        organizationAssociateData.setPersonData(personData);		
    }
    
    /** Getter for property ssn.
     * @return Value of property ssn.
     *
     */
    public java.lang.String getSsn() {
        return organizationAssociateData.getPersonData().getSsn();
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     *
     */
    public void setSsn(java.lang.String ssn) {
        
        if ((!(TextUtil.isEmptyOrSpaces(ssn))) && 
            (!(TextUtil.isEmptyOrSpaces(organizationAssociateData.getPersonData().getSsn())))) {
            this.ssn1 = organizationAssociateData.getPersonData().getSsn().substring(0, 3);
            this.ssn2 = organizationAssociateData.getPersonData().getSsn().substring(3, 5);
            this.ssn3 = organizationAssociateData.getPersonData().getSsn().substring(5, 9);
        }    
    }

    /** Getter for property organizationAssociateData.
     * @return Value of property organizationAssociateData.
     *
     */
    public OrganizationAssociateData getOrganizationAssociateData() {
        
        PersonData personData = organizationAssociateData.getPersonData();

        if (ssn1==null || ssn2==null || ssn3==null) {
            personData.setSsn(null);
        } else if (TextUtil.isEmptyOrSpaces(ssn1) || TextUtil.isEmptyOrSpaces(ssn2) || TextUtil.isEmptyOrSpaces(ssn3)) {
            personData.setSsn(null);
        } else personData.setSsn(ssn1 + ssn2 + ssn3);
        
        organizationAssociateData.setPersonData(personData);

        return organizationAssociateData;
    }

    /** Setter for property organizationAssociateData.
     * @param organizationAssociateData New value of property organizationAssociateData.
     *
     */
    public void setOrganizationAssociateData(OrganizationAssociateData organizationAssociateData) {
        this.organizationAssociateData = organizationAssociateData;
		this.setPreviousSsn(organizationAssociateData.getPersonData().getSsn());
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

	public void setPreviousSsn(String s)
	{
		this.previousSsn = s;
	}
	
	public String getPreviousSsn()
	{
		return previousSsn;
	}
	
    /** Validation method for this form
     *
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
       
        if (comment == null)
            return null; //form is new

        ActionErrors errors = new ActionErrors();
        if (TextUtil.isEmpty(organizationAssociateData.getPersonData().getFirstNm())) {
            errors.add("personData.firstNm", new ActionError("error.field.required.generic", "First Name"));
        }else{ // JZhang:  check invalid characters in name
            this.nameMatch(errors, organizationAssociateData.getPersonData().getFirstNm(), "personData.firstNm");
        }
            
        if (TextUtil.isEmpty(organizationAssociateData.getPersonData().getLastNm())) {
            errors.add("personData.lastNm", new ActionError("error.field.required.generic", "Last Name"));
        }//else{ // JZhang:  check invalid characters in name
//            this.nameMatch(errors, organizationAssociateData.getPersonData().getLastNm(), "personData.lastNm");
//        }
/*
        // JZhang:  check invalid characters in name
        if (TextUtil.isEmptyOrSpaces(organizationAssociateData.getPersonData().getMiddleNm()) == false)
            this.nameMatch(errors, organizationAssociateData.getPersonData().getMiddleNm(), "personData.middleNm");
*/        
        return errors;

    }    
    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     */
    private void nameMatch(ActionErrors errors, String name, String prop) 
    {

        try
        {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9]{0,24})", name);

            if (match == false )
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
        }catch (PatternSyntaxException pse)
        {
            logger.debug("OrganizationAssociateDetailForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
 
    }    
    
}
