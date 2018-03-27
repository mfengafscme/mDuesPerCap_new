package org.afscme.enterprise.affiliate.staff.web;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.web.WebUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;


/**
 * @struts:form name="staffForm"
 */
public class StaffForm extends SearchForm {

    private static Logger logger =  Logger.getLogger(StaffForm.class);       
    
    protected PersonData personData;
    protected StaffData staffData;
    protected String comment;
    protected boolean update;
    protected boolean newPerson;
    protected boolean ignoreSsnDup;

    public StaffForm() {
        personData = new PersonData();
        staffData = new StaffData();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        if (comment == null)
            return null; //form is new
        
        ActionErrors errors = new ActionErrors();

        // staff form is being used by EditAffiliateStaffAction and Save Affiliate Staff Action.
        // In both cases, first name and last name and ssn has to be validated
//        if (AFSCMEAction.getCurrentPersonPk(request) == null) {
            WebUtil.checkFieldLength("personData.firstNm", personData.getFirstNm(), 1, 25, errors);
            WebUtil.checkFieldLength("personData.lastNm", personData.getLastNm(), 1, 25, errors);
            WebUtil.checkFieldLength("personData.ssn", personData.getSsn(), 0, 9, errors);
//        }
        WebUtil.checkFieldLength("comment", comment, 0, 255, errors);
        
        if (personData.getFirstNm() != null && personData.getFirstNm().trim().length() > 0)
            this.nameMatch(errors, personData.getFirstNm(), "personData.firstNm");
/*
        if (personData.getMiddleNm() != null && personData.getMiddleNm().trim().length() > 0)
            this.nameMatch(errors, personData.getMiddleNm(), "personData.middleNm");
        if (personData.getLastNm() != null && personData.getLastNm().trim().length() > 0)
            this.nameMatch(errors, personData.getLastNm(), "personData.lastNm");        
*/        
        return errors;
    }
    
    
    public String toString() {
        return
            "personData="+personData+", "+
            "staffData="+personData+", "+
            "personData="+personData+", "+
            "comment="+comment+", "+
            "update="+update+", "+
            "newPerson="+newPerson;
    }
    
    public boolean isNewPerson() {
        return newPerson;
    }
    
    public NewPerson getNewPerson() {
        return new NewPerson(personData);
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
        return personData;
    }
    
    /** Setter for property personData.
     * @param personData New value of property personData.
     *
     */
    public void setPersonData(PersonData personData) {
        this.personData = personData;
    }
    
    /** Getter for property staffData.
     * @return Value of property staffData.
     *
     */
    public StaffData getStaffData() {
        return staffData;
    }
    
    /** Setter for property staffData.
     * @param staffData New value of property staffData.
     *
     */
    public void setStaffData(StaffData staffData) {
        this.staffData = staffData;
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
    
    /** Setter for property newPerson.
     * @param newPerson New value of property newPerson.
     *
     */
    public void setNewPerson(boolean newPerson) {
        this.newPerson = newPerson;
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
            logger.debug("StaffForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
    }        
    
}
