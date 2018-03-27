/*
 * MemberDetailForm.java
 * Supports Member Detail and Member Detail Edit page
 * Created on June 4, 2003, 1:21 PM
 */

package org.afscme.enterprise.member.web;


// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.member.MemberAffiliateResult;
import org.afscme.enterprise.member.MemberOfficerTitleAddressInfo;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.apache.log4j.Logger;

/**
 * @struts:form name="memberDetailForm"
 */
public class MemberDetailForm extends org.apache.struts.action.ActionForm
{
    
    private static Logger logger =  Logger.getLogger(MemberDetailForm.class);     
    
   // person oriented fields
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

    private Boolean m_ssnValid = new Boolean(false);
    private Boolean m_ssnDuplicate;

    private Integer m_addressPk;

    private String m_comment;
    private java.sql.Timestamp m_commentDt;
    private Integer m_commentBy;

    private Integer m_modifiedBy;


    private java.sql.Timestamp m_modifiedDt;

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

    // member oriented fields
    private Integer m_monthExpelled; // for edit
    private String m_yearExpelled;  // for edit
    private java.sql.Timestamp mbrExpelledDt;
    private Boolean mbrBarredFg = new Boolean(false);

    // the collection of memberAffiliateResults
    private Collection memberAffiliateData;

	private String previousSsn;
	
    /** Creates a new instance of MemberDetailForm */
    public MemberDetailForm()
    {

    }

    // Struts Methods...

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {

         ActionErrors errors = new ActionErrors();

        //Check Required fields
        if (TextUtil.isEmptyOrSpaces(this.getFirstNm())) {
            errors.add("firstNm", new ActionError("error.field.required.generic", "First Name"));
        }else{
            this.nameMatch(errors, this.getFirstNm(), "firstNm");
        }

        if (TextUtil.isEmptyOrSpaces(this.getLastNm())) {
            errors.add("lastNm", new ActionError("error.field.required.generic", "Last Name"));
        }//else{
//            this.nameMatch(errors, this.getLastNm(), "lastNm");
//        }
/*
        if (TextUtil.isEmptyOrSpaces(this.getMiddleNm()) == false) {
            this.nameMatch(errors, this.getMiddleNm(), "middleNm");
        }
*/
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

        buf.append(", mbrBarredFg: " + mbrBarredFg);
        buf.append(", mbrExpelledDt: " + mbrExpelledDt);
        buf.append(", mbrAffiliateData: " + memberAffiliateData);

        buf.append(", addressPk: " + m_addressPk);

        buf.append(", comment: " + m_comment);
        buf.append(", commentDt: " + m_commentDt);

        buf.append(", emailData: " + m_emailData);

        buf.append(", phoneData: " + m_phoneData);

        buf.append(", personAddressRecord: " + m_personAddressRecord);



        return buf.toString()+"]";
    }

    // Getter and Setter Methods...

    public PersonData getPersonData()
    {
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
        data.setAddressPk(this.getAddressPk());

        // set comments data
        CommentData cdata = new CommentData();
        cdata.setComment(this.getComment());
        data.setTheCommentData(cdata);


        return data;
    }

    public MemberData getMemberData()
    {
        MemberData mData = new MemberData();
        PersonData pData = new PersonData();
        pData = this.getPersonData();
        mData.setThePersonData(pData);
        mData.setMbrBarredFg(this.mbrBarredFg);
        // set mbrExpelledDt from the month and year, taking into account null or empty entries
         if (this.getMonthExpelled() == null ||
            TextUtil.isEmptyOrSpaces(this.getYearExpelled())
        ) {
            mData.setMbrExpelledDt(null);
        } else {
            mData.setMbrExpelledDt(DateUtil.getTimestamp(
                                        this.getMonthExpelled().intValue(),
                                        Integer.parseInt(this.getYearExpelled()),
                                        false
                                   )
            );
        }

        //can't edit MemberAffiliateResult data, so assume this is not needed to be returned
        //can't edit modified by and modified date, so these are also not set.


        return mData;
    }

    public void setPersonData(PersonData data)
    {
        this.setPersonPk(data.getPersonPk());

        this.setPrefixNm(data.getPrefixNm());
        this.setFirstNm(data.getFirstNm());
        this.setMiddleNm(data.getMiddleNm());
        this.setLastNm(data.getLastNm());
        this.setSuffixNm(data.getSuffixNm());

        this.setNickNm(data.getNickNm());
        this.setAltMailingNm(data.getAltMailingNm());
        if (data.getSsn() != null) {
            this.setSsn1(data.getSsn().substring(0, 3));
            this.setSsn2(data.getSsn().substring(3, 5));
            this.setSsn3(data.getSsn().substring(5, 9));
        }
        this.setSsnValid(data.getSsnValid());
        this.setSsnDuplicate(data.getSsnDuplicate());

        CommentData cdata = new CommentData();
        cdata = data.getTheCommentData();
        this.setComment(cdata.getComment());
        this.setCommentDt(cdata.getCommentDt());
        this.setCommentBy(cdata.getRecordData().getCreatedBy());

        // need to set last mod by and last mod dt

        m_emailData = data.getTheEmailData();

        m_phoneData = data.getThePhoneData();
   }

    public void setMemberData(MemberData data)
    {
        // call appropriate method to set the person data
        this.setPersonData(data.getThePersonData());

        // set the member data
        this.setMbrBarredFg(data.getMbrBarredFg());
        this.setMbrExpelledDt(data.getMbrExpelledDt());
        Calendar cal = DateUtil.getCalendar(data.getMbrExpelledDt()); //Member Detail
        if (data.getMbrExpelledDt() != null) //Member Detail - Edit
        {
            int month = cal.get(cal.MONTH);
            Integer year = new Integer(cal.get(cal.YEAR));
            this.setMonthExpelled(new Integer(month + 1));
            this.setYearExpelled(year.toString());
        }
        // this is incorrect , and must be changed to use a flattened result
        // currently getting the unflattened results
        this.setMemberAffiliateData(data.getTheMemberAffiliateResults());
        this.setModifiedBy(data.getTheRecordData().getModifiedBy());
        this.setModifiedDt(data.getTheRecordData().getModifiedDate());
		this.setPreviousSsn(data.getThePersonData().getSsn());
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
        this.m_ssn = m_ssn;
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
    public void setSsn1(java.lang.String ssn1) {
        if (ssn1 == "") this.m_ssn1 = null;
        else this.m_ssn1 = ssn1;
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
    public void setSsn2(java.lang.String ssn2) {
        if (ssn2 == "") this.m_ssn2 = null;
        else this.m_ssn2 = ssn2;
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
    public void setSsn3(java.lang.String ssn3) {
        if (ssn3 == "") this.m_ssn3 = null;
        else this.m_ssn3 = ssn3;
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

    /** Getter for property mbrExpelledDt.
     * @return Value of property mbrExpelledDt.
     *
     */
    public java.sql.Timestamp getMbrExpelledDt() {

        if (m_monthExpelled != null && !TextUtil.isEmptyOrSpaces(m_yearExpelled) ) { // for MemBerDetailEdit
            return(DateUtil.getTimestamp(m_monthExpelled.intValue(),new Integer(m_yearExpelled).intValue(),false));
        }
        else return mbrExpelledDt; // for MemberDetail
    }

    /** Setter for property mbrExpelledDt.
     * @param mbrExpelledDt New value of property mbrExpelledDt.
     *
     */
    public void setMbrExpelledDt(java.sql.Timestamp mbrExpelledDt) {
        this.mbrExpelledDt = mbrExpelledDt;
    }

    /** Getter for property mbrBarredFg.
     * @return Value of property mbrBarredFg.
     *
     */
    public java.lang.Boolean getMbrBarredFg() {
        return mbrBarredFg;
    }

    /** Setter for property mbrBarredFg.
     * @param mbrBarredFg New value of property mbrBarredFg.
     *
     */
    public void setMbrBarredFg(java.lang.Boolean mbrBarredFg) {
        this.mbrBarredFg = mbrBarredFg;
    }

    /** Getter for property m_memberAffiliateData.
     * @return Value of property m_memberAffiliateData.
     *
     */
    public java.util.Collection getMemberAffiliateData() {
        return memberAffiliateData;
    }

    /** Setter for property m_memberAffiliateData.
     * @param m_memberAffiliateData New value of property m_memberAffiliateData.
     *
     */
    public void setMemberAffiliateData(java.util.Collection theMemberAffiliateData) {
        this.memberAffiliateData = theMemberAffiliateData;
    }

    /** Getter for property m_modifiedBy.
     * @return Value of property m_modifiedBy.
     *
     */
    public Integer getModifiedBy() {
        return m_modifiedBy;
    }

    /** Setter for property m_modifiedBy.
     * @param m_modifiedBy New value of property m_modifiedBy.
     *
     */
    public void setModifiedBy(java.lang.Integer m_modifiedBy) {
        this.m_modifiedBy = m_modifiedBy;
    }

    /** Getter for property m_modifiedDt.
     * @return Value of property m_modifiedDt.
     *
     */
    public java.sql.Timestamp getModifiedDt() {
        return m_modifiedDt;
    }

    /** Setter for property m_modifiedDt.
     * @param m_modifiedDt New value of property m_modifiedDt.
     *
     */
    public void setModifiedDt(java.sql.Timestamp m_modifiedDt) {
        this.m_modifiedDt = m_modifiedDt;
    }

    /** Getter for property m_commentBy.
     * @return Value of property m_commentBy.
     *
     */
    public java.lang.Integer getCommentBy() {
        return m_commentBy;
    }

    /** Setter for property m_commentBy.
     * @param m_commentBy New value of property m_commentBy.
     *
     */
    public void setCommentBy(java.lang.Integer m_commentBy) {
        this.m_commentBy = m_commentBy;
    }

    /** Getter for property m_monthExpelled.
     * @return Value of property m_monthExpelled.
     *
     */
    public java.lang.Integer getMonthExpelled() {
        return m_monthExpelled;
    }

    /** Setter for property m_monthExpelled.
     * @param m_monthExpelled New value of property m_monthExpelled.
     *
     */
    public void setMonthExpelled(java.lang.Integer m_monthExpelled) {
        this.m_monthExpelled = m_monthExpelled;
    }

    /** Getter for property m_yearExpelled.
     * @return Value of property m_yearExpelled.
     *
     */
    public java.lang.String getYearExpelled() {
        return m_yearExpelled;
    }

    /** Setter for property m_yearExpelled.
     * @param m_yearExpelled New value of property m_yearExpelled.
     *
     */
    public void setYearExpelled(java.lang.String m_yearExpelled) {
        this.m_yearExpelled = m_yearExpelled;
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
                errors.add(prop, new ActionError("error.field.incorrect.name", "First Name"));
            }
        }catch (PatternSyntaxException pse)
        {
            logger.debug("MemberDetailForm:Pattern syntax exception");
            logger.debug(pse.getDescription());
        }
    }
	
    public java.lang.String getPreviousSsn() {
        return previousSsn;
    }

    /** Setter for property m_yearExpelled.
     * @param m_yearExpelled New value of property m_yearExpelled.
     *
     */
    public void setPreviousSsn(java.lang.String s) {
        this.previousSsn = s;
    }
	
}
