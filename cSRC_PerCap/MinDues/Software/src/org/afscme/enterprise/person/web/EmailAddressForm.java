package org.afscme.enterprise.person.web;

import java.sql.Timestamp;
// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.common.RecordData;
import org.apache.log4j.Logger;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.EmailData;

/**
 * Represents the form when the user is viewing the person email addresses
 *
 * @struts:form name="emailAddressForm"
 */
public class EmailAddressForm extends org.apache.struts.action.ActionForm {

    private static Logger logger =  Logger.getLogger(EmailAddressForm.class);     
    
    private Integer m_personPk;

    private Integer emailPk1;
    private Integer emailType1;
    private String personEmailAddr1;
    private Boolean emailBadFg1 = new Boolean(false);
    private Timestamp emailMarkedBadDt1;
    private Boolean isPrimary1;
    private RecordData theRecordData1;

    private Integer emailPk2;
    private Integer emailType2;
    private String personEmailAddr2;
    private Boolean emailBadFg2 = new Boolean(false);
    private Timestamp emailMarkedBadDt2;
    private Boolean isPrimary2;
    private RecordData theRecordData2;

    private String personEmailAddr1_o;
    private String personEmailAddr2_o;
    private Boolean emailBadFg1_o = new Boolean(false);
    private Boolean emailBadFg2_o = new Boolean(false);
    private Boolean isPrimary1_o;
    private Boolean isPrimary2_o;
    
    /**
     * Collection of EmailData objects.
     */
    private Collection m_emailData;

    /**
     * Action to use for the return button back to the last screen. Must include
     * parameters needed to return to previous screen.
     */
    private String returnAction;
    private String back;

    /** Creates a new instance of PersonDetailForm */
    public EmailAddressForm() {
    }


// Struts Methods...

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) { 
        
        ActionErrors errors = new ActionErrors();
        return errors;
    }

// General Methods...

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");

        buf.append("emailData: " + m_emailData);

        return buf.toString()+"]";
    }

// Getter and Setter Methods...

    /** Getter for property m_emailData.
     * @return Value of property m_emailData.
     *
     */
    public java.util.Collection getEmailData() {
        Collection list = new ArrayList();

        setIsPrimary1(isPrimary1); 
        setIsPrimary2(isPrimary2);
        
        if (!personEmailAddr1_o.equalsIgnoreCase(personEmailAddr1) ||
            emailBadFg1_o.booleanValue() != emailBadFg1.booleanValue() || 
            isPrimary1_o.booleanValue() != isPrimary1.booleanValue()) {
            list.add(getEmailData1());
        }
        if (!personEmailAddr2_o.equalsIgnoreCase(personEmailAddr2) ||
            emailBadFg2_o.booleanValue() != emailBadFg2.booleanValue() || 
            isPrimary2_o.booleanValue() != isPrimary2.booleanValue()) {
            list.add(getEmailData2());
        }
        return list;
    }
    /** Setter for property m_emailData1.
     * @param emailData New value of property m_emailData.
     *
     */
    public void setEmailData1(EmailData emailData) {
        setEmailPk1(emailData.getEmailPk());
        setEmailType1(emailData.getEmailType());
        setPersonEmailAddr1(emailData.getPersonEmailAddr());
        setEmailBadFg1(emailData.getEmailBadFg());
        setEmailMarkedBadDt1(emailData.getEmailMarkedBadDt());
        setIsPrimary1(emailData.getIsPrimary());
        setTheRecordData1(emailData.getTheRecordData());
    }

    /** Getter for property emailData1.
     * @return Value of property emailData.
     *
     */
    public EmailData getEmailData1() {
        EmailData emailData = new EmailData();
        emailData.setEmailPk(emailPk1);
        emailData.setEmailType(emailType1);
        emailData.setPersonEmailAddr(personEmailAddr1);
        emailData.setEmailBadFg(emailBadFg1);
        emailData.setEmailMarkedBadDt(emailMarkedBadDt1);
        emailData.setIsPrimary(isPrimary1);
        emailData.setTheRecordData(theRecordData1);
        return emailData;
    }

    /** Setter for property m_emailData2.
     * @param emailData New value of property m_emailData.
     *
     */
    public void setEmailData2(EmailData emailData) {
        setEmailPk2(emailData.getEmailPk());
        setEmailType2(emailData.getEmailType());
        setPersonEmailAddr2(emailData.getPersonEmailAddr());
        setEmailBadFg2(emailData.getEmailBadFg());
        setEmailMarkedBadDt2(emailData.getEmailMarkedBadDt());
        setIsPrimary2(emailData.getIsPrimary());
        setTheRecordData2(emailData.getTheRecordData());
    }

    /** Getter for property emailData2.
     * @return Value of property emailData.
     *
     */
    public EmailData getEmailData2() {
        EmailData emailData = new EmailData();
        emailData.setEmailPk(emailPk2);
        emailData.setEmailType(emailType2);
        emailData.setPersonEmailAddr(personEmailAddr2);
        emailData.setEmailBadFg(emailBadFg2);
        emailData.setEmailMarkedBadDt(emailMarkedBadDt2);
        emailData.setIsPrimary(isPrimary2);
        emailData.setTheRecordData(theRecordData2);
        return emailData;
    }

    /** Sets the Primary and Alternate Email Types.
     *
     */
    public void setPrimary() {
        Integer emailType;
        // If the first one is no longer the Primary and Alternate has a value,
        // then switch the Email Type PKs.
        if (!this.isPrimary1.booleanValue() && this.personEmailAddr2.length() > 0){
            emailType = this.emailType1;
            this.emailType1 = this.emailType2;
            this.emailType2 = emailType;
            this.isPrimary2 = new Boolean(true);
        } else if ((this.isPrimary1.booleanValue() && this.personEmailAddr1.length() < 1)) {
        // If the first one IS the Primary but does not have a value,
        // then switch the Email Type PKs.
            emailType = this.emailType1;
            this.emailType1 = this.emailType2;
            this.emailType2 = emailType;
            this.isPrimary2 = new Boolean(true);
        } else {                                       // else the first one remains as the Primary
            this.isPrimary1 = new Boolean(true);
        }
    }

    /** Getter for property m_personPk.
     * @return Value of property m_personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return m_personPk;
    }

    /** Setter for property m_personPk.
     * @param m_personPk New value of property m_personPk.
     *
     */
    public void setPersonPk(java.lang.Integer m_personPk) {
        this.m_personPk = m_personPk;
    }

    /** Getter for property returnAction.
     * @return Value of property returnAction.
     *
     */
    public String getReturnAction() {
        return returnAction;
    }

    /** Setter for property returnAction.
     * @param returnAction New value of property returnAction.
     *
     */
    public void setReturnAction(String returnAction) {
        if (TextUtil.isEmptyOrSpaces(returnAction)) {
            this.returnAction = null;
        } else {
            this.returnAction = returnAction;
        }
    }

    /**
     * Returns true if this form is being used for an add operation (as opposed to edit)
     */
    public boolean isAdd() {
        return m_emailData == null;
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

    /** Getter for property personEmailAddr2.
     * @return Value of property personEmailAddr2.
     *
     */
    public java.lang.String getPersonEmailAddr2() {
        return personEmailAddr2;
    }

    /** Setter for property personEmailAddr2.
     * @param personEmailAddr2 New value of property personEmailAddr2.
     *
     */
    public void setPersonEmailAddr2(java.lang.String personEmailAddr2) {
        this.personEmailAddr2 = personEmailAddr2;
    }

    /** Getter for property personEmailAddr1.
     * @return Value of property personEmailAddr1.
     *
     */
    public java.lang.String getPersonEmailAddr1() {
        return personEmailAddr1;
    }

    /** Setter for property personEmailAddr1.
     * @param personEmailAddr1 New value of property personEmailAddr1.
     *
     */
    public void setPersonEmailAddr1(java.lang.String personEmailAddr1) {
        this.personEmailAddr1 = personEmailAddr1;
    }

    /** Getter for property emailType1.
     * @return Value of property emailType1.
     *
     */
    public java.lang.Integer getEmailType1() {
        return emailType1;
    }

    /** Setter for property emailType1.
     * @param emailType1 New value of property emailType1.
     *
     */
    public void setEmailType1(java.lang.Integer emailType1) {
        this.emailType1 = emailType1;
    }

    /** Getter for property emailPk1.
     * @return Value of property emailPk1.
     *
     */
    public java.lang.Integer getEmailPk1() {
        return emailPk1;
    }

    /** Setter for property emailPk1.
     * @param emailPk1 New value of property emailPk1.
     *
     */
    public void setEmailPk1(java.lang.Integer emailPk1) {
        this.emailPk1 = emailPk1;
    }

    /** Getter for property theRecordData2.
     * @return Value of property theRecordData2.
     *
     */
    public org.afscme.enterprise.common.RecordData getTheRecordData2() {
        return theRecordData2;
    }

    /** Setter for property theRecordData2.
     * @param theRecordData2 New value of property theRecordData2.
     *
     */
    public void setTheRecordData2(org.afscme.enterprise.common.RecordData theRecordData2) {
        this.theRecordData2 = theRecordData2;
    }

    /** Getter for property emailMarkedBadDt1.
     * @return Value of property emailMarkedBadDt1.
     *
     */
    public java.sql.Timestamp getEmailMarkedBadDt1() {
        return emailMarkedBadDt1;
    }

    /** Setter for property emailMarkedBadDt1.
     * @param emailMarkedBadDt1 New value of property emailMarkedBadDt1.
     *
     */
    public void setEmailMarkedBadDt1(java.sql.Timestamp emailMarkedBadDt1) {
        this.emailMarkedBadDt1 = emailMarkedBadDt1;
    }

    /** Getter for property emailMarkedBadDt2.
     * @return Value of property emailMarkedBadDt2.
     *
     */
    public java.sql.Timestamp getEmailMarkedBadDt2() {
        return emailMarkedBadDt2;
    }

    /** Setter for property emailMarkedBadDt2.
     * @param emailMarkedBadDt2 New value of property emailMarkedBadDt2.
     *
     */
    public void setEmailMarkedBadDt2(java.sql.Timestamp emailMarkedBadDt2) {
        this.emailMarkedBadDt2 = emailMarkedBadDt2;
    }

    /** Getter for property isPrimary2.
     * @return Value of property isPrimary2.
     *
     */
    public java.lang.Boolean getIsPrimary2() {
        return isPrimary2;
    }

    /** Setter for property isPrimary2.
     * @param isPrimary2 New value of property isPrimary2.
     *
     */
    public void setIsPrimary2(java.lang.Boolean isPrimary2) {
        if (isPrimary2 == null) 
            this.isPrimary2 = new Boolean(false);
        else
            this.isPrimary2 = isPrimary2;
    }

    /** Getter for property emailBadFg1.
     * @return Value of property emailBadFg1.
     *
     */
    public java.lang.Boolean getEmailBadFg1() {
        return emailBadFg1;
    }

    /** Setter for property emailBadFg1.
     * @param emailBadFg1 New value of property emailBadFg1.
     *
     */
    public void setEmailBadFg1(java.lang.Boolean emailBadFg1) {
        this.emailBadFg1 = emailBadFg1;
    }

    /** Getter for property emailType2.
     * @return Value of property emailType2.
     *
     */
    public java.lang.Integer getEmailType2() {
        return emailType2;
    }

    /** Setter for property emailType2.
     * @param emailType2 New value of property emailType2.
     *
     */
    public void setEmailType2(java.lang.Integer emailType2) {
        this.emailType2 = emailType2;
    }

    /** Getter for property emailPk2.
     * @return Value of property emailPk2.
     *
     */
    public java.lang.Integer getEmailPk2() {
        return emailPk2;
    }

    /** Setter for property emailPk2.
     * @param emailPk2 New value of property emailPk2.
     *
     */
    public void setEmailPk2(java.lang.Integer emailPk2) {
        this.emailPk2 = emailPk2;
    }

    /** Getter for property isPrimary1.
     * @return Value of property isPrimary1.
     *
     */
    public java.lang.Boolean getIsPrimary1() {
        return isPrimary1;
    }

    /** Setter for property isPrimary1.
     * @param isPrimary1 New value of property isPrimary1.
     *
     */
    public void setIsPrimary1(java.lang.Boolean isPrimary1) {
        if (isPrimary1 == null) 
            this.isPrimary1 = new Boolean(false);
        else
            this.isPrimary1 = isPrimary1;
    }

    /** Getter for property theRecordData1.
     * @return Value of property theRecordData1.
     *
     */
    public org.afscme.enterprise.common.RecordData getTheRecordData1() {
        return theRecordData1;
    }

    /** Setter for property theRecordData1.
     * @param theRecordData1 New value of property theRecordData1.
     *
     */
    public void setTheRecordData1(org.afscme.enterprise.common.RecordData theRecordData1) {
        this.theRecordData1 = theRecordData1;
    }

    /** Getter for property emailBadFg2.
     * @return Value of property emailBadFg2.
     *
     */
    public java.lang.Boolean getEmailBadFg2() {
        return emailBadFg2;
    }

    /** Setter for property emailBadFg2.
     * @param emailBadFg2 New value of property emailBadFg2.
     *
     */
    public void setEmailBadFg2(java.lang.Boolean emailBadFg2) {
        this.emailBadFg2 = emailBadFg2;
    }
    
    /** Getter for property emailBadFg1_o.
     * @return Value of property emailBadFg1_o.
     *
     */
    public java.lang.Boolean getEmailBadFg1_o() {
        return emailBadFg1_o;
    }
    
    /** Setter for property emailBadFg1_o.
     * @param emailBadFg1_o New value of property emailBadFg1_o.
     *
     */
    public void setEmailBadFg1_o(java.lang.Boolean emailBadFg1_o) {
        this.emailBadFg1_o = emailBadFg1_o;
    }
    
    /** Getter for property emailBadFg2_o.
     * @return Value of property emailBadFg2_o.
     *
     */
    public java.lang.Boolean getEmailBadFg2_o() {
        return emailBadFg2_o;
    }
    
    /** Setter for property emailBadFg2_o.
     * @param emailBadFg2_o New value of property emailBadFg2_o.
     *
     */
    public void setEmailBadFg2_o(java.lang.Boolean emailBadFg2_o) {
        this.emailBadFg2_o = emailBadFg2_o;
    }
    
    /** Getter for property personEmailAddr1_o.
     * @return Value of property personEmailAddr1_o.
     *
     */
    public java.lang.String getPersonEmailAddr1_o() {
        return personEmailAddr1_o;
    }
    
    /** Setter for property personEmailAddr1_o.
     * @param personEmailAddr1_o New value of property personEmailAddr1_o.
     *
     */
    public void setPersonEmailAddr1_o(java.lang.String personEmailAddr1_o) {
        this.personEmailAddr1_o = (personEmailAddr1_o == null) ? "" : personEmailAddr1_o;
    }
    
    /** Getter for property personEmailAddr2_o.
     * @return Value of property personEmailAddr2_o.
     *
     */
    public java.lang.String getPersonEmailAddr2_o() {
        return personEmailAddr2_o;
    }
    
    /** Setter for property personEmailAddr2_o.
     * @param personEmailAddr2_o New value of property personEmailAddr2_o.
     *
     */
    public void setPersonEmailAddr2_o(java.lang.String personEmailAddr2_o) {
        this.personEmailAddr2_o = (personEmailAddr2_o == null) ? "" : personEmailAddr2_o;
    }
    
    /** Getter for property isPrimary1_o.
     * @return Value of property isPrimary1_o.
     *
     */
    public java.lang.Boolean getIsPrimary1_o() {
        return isPrimary1_o;
    }
    
    /** Setter for property isPrimary1_o.
     * @param isPrimary1_o New value of property isPrimary1_o.
     *
     */
    public void setIsPrimary1_o(java.lang.Boolean isPrimary1_o) {
        this.isPrimary1_o = (isPrimary1_o == null) ? new Boolean(false) : isPrimary1_o;
    }
    
    /** Getter for property isPrimary2_o.
     * @return Value of property isPrimary2_o.
     *
     */
    public java.lang.Boolean getIsPrimary2_o() {
        return isPrimary2_o;
    }
    
    /** Setter for property isPrimary2_o.
     * @param isPrimary2_o New value of property isPrimary2_o.
     *
     */
    public void setIsPrimary2_o(java.lang.Boolean isPrimary2_o) {
        this.isPrimary2_o = (isPrimary2_o == null) ? new Boolean(false) : isPrimary2_o;
    }    
}
