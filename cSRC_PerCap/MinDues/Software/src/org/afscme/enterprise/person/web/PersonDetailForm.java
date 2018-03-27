package org.afscme.enterprise.person.web;

import java.lang.Integer;
import java.sql.Timestamp;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.address.ejb.SystemAddressBean;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.person.PersonCriteria;

import org.apache.log4j.Logger;

/**
 * Represents the form when the user is viewing the person detail
 *
 * @struts:form name="personDetailForm"
 */
public class PersonDetailForm extends org.apache.struts.action.ActionForm {
    
    private static Logger logger =  Logger.getLogger(VerifyPersonForm.class);    
    
    private Integer m_personPk;
    private Integer m_prefixNm;
    private String m_firstNm;
    private String m_middleNm;
    private String m_lastNm;
    private Integer m_suffixNm;
    private String m_nickNm;
    private String m_altMailingNm;

    private String m_ssn;
    // Social Security Number - Area Number - first 3 digits
    private String m_ssn1;
    // Social Security Number - Group Number - middle 2 digits
    private String m_ssn2;
    // Social Security Number - Serial Number - last 4 digits
    private String m_ssn3;

    private Boolean m_ssnValid;
    private Boolean m_ssnDuplicate;
    private Boolean m_markedForDeletionFg;
    private Integer m_addressPk;

    private String m_comment;
    private Timestamp m_commentDt;

    /**
     * Collection of EmailData objects.
     */
    private Collection m_emailData;
    
    /**
     * Collection of PhoneData objects.
     */
    private Collection m_phoneData;

    /**
     * SMA PersonAddressRecord object
     */
    private PersonAddressRecord m_personAddressRecord;

    /**
     * Persona
     */
    private Collection m_persona;

    private String prevAction;
    private String previousSsn;
	
    /** Creates a new instance of PersonDetailForm */
    public PersonDetailForm() {
        m_markedForDeletionFg = new Boolean(false);
        m_comment = "";
    }
    
// Struts Methods...
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();

        //Check Required fields
        if (TextUtil.isEmptyOrSpaces(this.getFirstNm())) {
            errors.add("firstNm", new ActionError("error.field.required.generic", "First Name"));
        }else{
            this.nameMatch(errors, this.getFirstNm(), "firstNm");
        }
/*        
        if (TextUtil.isEmptyOrSpaces(this.getMiddleNm()) == false) {
            this.nameMatch(errors, this.getMiddleNm(), "middleNm");
        }
 */
        if (TextUtil.isEmptyOrSpaces(this.getLastNm())) {
            errors.add("lastNm", new ActionError("error.field.required.generic", "Last Name"));
        }//else{
//            this.nameMatch(errors, this.getLastNm(), "lastNm");            
//        }

        logger.debug("**************** Returning " + errors.size() + " error(s).");
        return errors;
    }
    
// General Methods...
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        
        buf.append("prefixNm: " + m_prefixNm);
        buf.append(", firstNm: " + m_firstNm);
        buf.append(", middleNm: " + m_middleNm);
        buf.append(", lastNm: " + m_lastNm);
        buf.append(", suffixNm: " + m_suffixNm);
        buf.append(", nickNm: " + m_nickNm);
        buf.append(", altMailingNm: " + m_altMailingNm);
        buf.append(", ssn: " + m_ssn);
        buf.append(", ssn1: " + m_ssn1);
        buf.append(", ssn2: " + m_ssn2);
        buf.append(", ssn3: " + m_ssn3);
        buf.append(", ssnValid: " + m_ssnValid);
        buf.append(", ssnDuplicate: " + m_ssnDuplicate);
        buf.append(", markedForDeletionFg: " + m_markedForDeletionFg);

        buf.append(", addressPk: " + m_addressPk);

        buf.append(", comment: " + m_comment);
        buf.append(", commentDt: " + m_commentDt);

        buf.append(", emailData: " + m_emailData);

        buf.append(", phoneData: " + m_phoneData);

 //       buf.append(", personAddressRecord: " + m_personAddressRecord);    
        buf.append(", persona: " + m_persona);
        buf.append(", prevAction: " + prevAction);
        buf.append(", previousSsn: " + previousSsn);        

        return buf.toString()+"]";
    }
    
// Getter and Setter Methods...
    
    public PersonData getPersonData() {
        PersonData data = new PersonData();
        data.setPersonPk(this.getPersonPk());
        data.setPrefixNm(this.getPrefixNm());
        data.setFirstNm(this.getFirstNm());
        data.setMiddleNm(this.getMiddleNm());
        data.setLastNm(this.getLastNm());
        data.setSuffixNm(this.getSuffixNm());
        data.setNickNm(this.getNickNm());
        data.setAltMailingNm(this.getAltMailingNm());
        data.setSsn(this.getSsn());
        
        data.setSsnValid(this.getSsnValid());
        data.setSsnDuplicate(this.getSsnDuplicate());
        data.setMarkedForDeletionFg(this.getMarkedForDeletionFg());
        data.setAddressPk(this.getAddressPk());
        
        CommentData commentData = new CommentData();
        commentData.setComment(this.getComment());
        commentData.setCommentDt(this.getCommentDt());
        data.setTheCommentData(commentData);
        
        return data;
    }
    
    public void setPersonData(PersonData data) {
        this.setPersonPk(data.getPersonPk());

        this.setPrefixNm(data.getPrefixNm());
        this.setFirstNm(data.getFirstNm());
        this.setMiddleNm(data.getMiddleNm());
        this.setLastNm(data.getLastNm());
        this.setSuffixNm(data.getSuffixNm());
        
        this.setNickNm(data.getNickNm());
        this.setAltMailingNm(data.getAltMailingNm());
        this.setSsn(data.getSsn());
        if (data.getSsn() != null) {
            this.setSsn1(data.getSsn().substring(0, 3));
            this.setSsn2(data.getSsn().substring(3, 5));
            this.setSsn3(data.getSsn().substring(5, 9));
        }
        this.setSsnValid(data.getSsnValid());
        this.setSsnDuplicate(data.getSsnDuplicate());
        this.setMarkedForDeletionFg(data.getMarkedForDeletionFg());

        CommentData cdata = new CommentData();
        cdata = data.getTheCommentData();
        this.setComment(cdata.getComment());

        m_emailData = data.getTheEmailData();
        
        m_phoneData = data.getThePhoneData();
    }
    
    /** Getter for property m_addressPk.
     * @return Value of property m_addressPk.
     *
     */
    public java.lang.Integer getAddressPk() {
        return m_addressPk;
    }
    
    /** Setter for property m_addressPk.
     * @param m_addressPk New value of property m_addressPk.
     *
     */
    public void setAddressPk(java.lang.Integer m_addressPk) {
        this.m_addressPk = m_addressPk;
    }
    
    /** Getter for property m_altMailingNm.
     * @return Value of property m_altMailingNm.
     *
     */
    public java.lang.String getAltMailingNm() {
        return m_altMailingNm;
    }
    
    /** Setter for property m_altMailingNm.
     * @param m_altMailingNm New value of property m_altMailingNm.
     *
     */
    public void setAltMailingNm(java.lang.String m_altMailingNm) {
        this.m_altMailingNm = m_altMailingNm;
    }
    
    /** Getter for property m_comment.
     * @return Value of property m_comment.
     *
     */
    public java.lang.String getComment() {
        return m_comment;
    }
    
    /** Setter for property m_comment.
     * @param m_comment New value of property m_comment.
     *
     */
    public void setComment(java.lang.String m_comment) {
        this.m_comment = m_comment;
    }
    
    /** Getter for property m_commentDt.
     * @return Value of property m_commentDt.
     *
     */
    public java.sql.Timestamp getCommentDt() {
        return m_commentDt;
    }
    
    /** Setter for property m_commentDt.
     * @param m_commentDt New value of property m_commentDt.
     *
     */
    public void setCommentDt(java.sql.Timestamp m_commentDt) {
        this.m_commentDt = m_commentDt;
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
        this.m_firstNm = m_firstNm;
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
        this.m_lastNm = m_lastNm;
    }
    
    /** Getter for property m_markedForDeletionFg.
     * @return Value of property m_markedForDeletionFg.
     *
     */
    public java.lang.Boolean getMarkedForDeletionFg() {
        return m_markedForDeletionFg;
    }
    
    /** Setter for property m_markedForDeletionFg.
     * @param m_markedForDeletionFg New value of property m_markedForDeletionFg.
     *
     */
    public void setMarkedForDeletionFg(java.lang.Boolean m_markedForDeletionFg) {
        if (m_markedForDeletionFg==null) {
            this.m_markedForDeletionFg = new Boolean(false);
        } else {
            this.m_markedForDeletionFg = m_markedForDeletionFg;
        }
    }
    
    /** Getter for property m_middleNm.
     * @return Value of property m_middleNm.
     *
     */
    public java.lang.String getMiddleNm() {
        return m_middleNm;
    }
    
    /** Setter for property m_middleNm.
     * @param m_middleNm New value of property m_middleNm.
     *
     */
    public void setMiddleNm(java.lang.String m_middleNm) {
        this.m_middleNm = m_middleNm;
    }
    
    /** Getter for property m_nickNm.
     * @return Value of property m_nickNm.
     *
     */
    public java.lang.String getNickNm() {
        return m_nickNm;
    }
    
    /** Setter for property m_nickNm.
     * @param m_nickNm New value of property m_nickNm.
     *
     */
    public void setNickNm(java.lang.String m_nickNm) {
        this.m_nickNm = m_nickNm;
    }
    
    /** Getter for property m_prefixNm.
     * @return Value of property m_prefixNm.
     *
     */
    public java.lang.Integer getPrefixNm() {
        return m_prefixNm;
    }
    
    /** Setter for property m_prefixNm.
     * @param m_prefixNm New value of property m_prefixNm.
     *
     */
    public void setPrefixNm(java.lang.Integer m_prefixNm) {
        this.m_prefixNm = m_prefixNm;
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
        } else if (m_ssn==null) {
            this.m_ssn = null;
        } else {
            this.m_ssn = m_ssn;
            this.m_ssn1 = m_ssn.substring(0, 3);
            this.m_ssn2 = m_ssn.substring(3, 5);
            this.m_ssn3 = m_ssn.substring(5, 9);        
        }
    }
    
    /** Getter for property m_ssnDuplicate.
     * @return Value of property m_ssnDuplicate.
     *
     */
    public java.lang.Boolean getSsnDuplicate() {
        return m_ssnDuplicate;
    }
    
    /** Setter for property m_ssnDuplicate.
     * @param m_ssnDuplicate New value of property m_ssnDuplicate.
     *
     */
    public void setSsnDuplicate(java.lang.Boolean m_ssnDuplicate) {
        this.m_ssnDuplicate = m_ssnDuplicate;
    }
    
    /** Getter for property m_ssnValid.
     * @return Value of property m_ssnValid.
     *
     */
    public java.lang.Boolean getSsnValid() {
        return m_ssnValid;
    }
    
    /** Setter for property m_ssnValid.
     * @param m_ssnValid New value of property m_ssnValid.
     *
     */
    public void setSsnValid(java.lang.Boolean m_ssnValid) {
        this.m_ssnValid = m_ssnValid;
    }
    
    /** Getter for property m_suffixNm.
     * @return Value of property m_suffixNm.
     *
     */
    public java.lang.Integer getSuffixNm() {
        return m_suffixNm;
    }
    
    /** Setter for property m_suffixNm.
     * @param m_suffixNm New value of property m_suffixNm.
     *
     */
    public void setSuffixNm(java.lang.Integer m_suffixNm) {
        this.m_suffixNm = m_suffixNm;
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
    
    /** Getter for property emailData.
     * @return Value of property emailData.
     *
     */
    public java.util.Collection getEmailData() {
        return m_emailData;
    }
    
    /** Setter for property emailData.
     * @param emailData New value of property emailData.
     *
     */
    public void setEmailData(java.util.Collection m_emailData) {
        this.m_emailData = m_emailData;
    }
    
    /** Getter for property phoneData.
     * @return Value of property phoneData.
     *
     */
    public java.util.Collection getPhoneData() {
        return m_phoneData;
    }
    
    /** Setter for property phoneData.
     * @param phoneData New value of property phoneData.
     *
     */
    public void setPhoneData(java.util.Collection m_phoneData) {
        this.m_phoneData = m_phoneData;
    }
    
    /** Getter for property m_personAddressRecord.
     * @return Value of property m_personAddressRecord.
     *
     */
    public org.afscme.enterprise.address.PersonAddressRecord getPersonAddressRecord() {
        return m_personAddressRecord;
    }
    
    /** Setter for property m_personAddressRecord.
     * @param m_personAddressRecord New value of property m_personAddressRecord.
     *
     */
    public void setPersonAddressRecord(org.afscme.enterprise.address.PersonAddressRecord m_personAddressRecord) {
        this.m_personAddressRecord = m_personAddressRecord;
    }
    
    /** Getter for property m_persona.
     * @return Value of property m_persona.
     *
     */
    public java.util.Collection getPersona() {
        return m_persona;
    }
    
    /** Setter for property m_persona.
     * @param m_persona New value of property m_persona.
     *
     */
    public void setPersona(java.util.Collection m_persona) {
        this.m_persona = m_persona;
    }

    public String getPrevAction()
    {
            return prevAction;
    }

    public void setPrevAction(String a)
    {
            this.prevAction = a;
    }

    public String getPreviousSsn()
    {
            return previousSsn;
    }

    public void setPreviousSsn(String s)
    {
            this.previousSsn = s;
    }
	
    /* JZhang
     * @parm errors: ActionErrors object
     * @parm name: value that need to be verified
     * @parm prop: the name used on jsp 
     */
    private void nameMatch(ActionErrors errors, String name, String prop) 
    {
        try
        {
            boolean match = Pattern.matches("([a-z A-Z]{1}[a-z A-Z 0-9]{0,24})", name);
            if (match == false ){
                logger.debug("PersonDetailForm:nameMatch -- An error is added.");
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
            }
        }catch (PatternSyntaxException pse)
        {
            logger.debug("PersonDetailForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }       
    }    
}
