package org.afscme.enterprise.rebate.web;

import java.util.Collection;
import java.math.BigDecimal;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.rebate.PRBConstants;
import org.afscme.enterprise.person.PRBAffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.codes.Codes.RebateDuration;

public class DuesPaidInfoForm extends ActionForm {
    private int affCount;
    private Integer affPk_1;
    private Character type_1;
    private String local_1;
    private String state_1;
    private String council_1;
    private String subUnit_1;
    private Character code_1;
    private Integer duration_1;
    private Integer filedWith_1;
    private Integer acceptanceCode_1;
    private Integer mbrType_1;
    private Integer mbrStatus_1;
    private Integer duesType_1;
    private Integer affPk_2;
    private Character type_2;
    private String local_2;
    private String state_2;
    private String council_2;
    private String subUnit_2;
    private Character code_2;
    private Integer duration_2;
    private Integer filedWith_2;
    private Integer acceptanceCode_2;
    private Integer mbrType_2;
    private Integer mbrStatus_2;
    private Integer duesType_2;
    private Integer affPk_3;
    private Character type_3;
    private String local_3;
    private String state_3;
    private String council_3;
    private String subUnit_3;
    private Character code_3;
    private Integer duration_3;
    private Integer filedWith_3;
    private Integer acceptanceCode_3;
    private Integer mbrType_3;
    private Integer mbrStatus_3;
    private Integer duesType_3;
    private boolean search;
    
    public Integer setStringToInteger(String s) {
        return (s == null || s.length()<1) ? null : new Integer(s);
    }
    
    public BigDecimal setStringToDecimal(String s) {
        return (s == null || s.length()<1) ? null : new BigDecimal(s);
    }

    public Double setStringToDouble(String s) {
        return (s == null || s.length()<1) ? null : new Double(s);
    }

    public Integer setInteger(Integer i) {
        return (i == null || i.intValue()<1) ? null : i;
    }
    
    /** Creates a new instance of AffiliateDetailForm */
    public DuesPaidInfoForm() {
        this.initDuesPaidInfo();
    }
    
    /** Creates a new instance of AffiliateDetailForm */
    public void initDuesPaidInfo() {    
        this.affCount = PRBConstants.MAX_REQUEST_AFFILIATE_ALLOWED;
        this.search = false;
        this.affPk_1 = null;
        this.type_1 = null;
        this.local_1 = null;
        this.state_1 = null;
        this.council_1 = null;
        this.subUnit_1 = null;
        this.code_1 = null;
        this.duration_1 = null;
        this.filedWith_1 = null;
        this.acceptanceCode_1 = null;
        this.mbrType_1 = null;
        this.mbrStatus_1 = null;
        this.duesType_1 = null;
        this.affPk_2 = null;
        this.type_2 = null;
        this.local_2 = null;
        this.state_2 = null;
        this.council_2 = null;
        this.subUnit_2 = null;
        this.code_2 = null;
        this.duration_2 = null;
        this.filedWith_2 = null;
        this.acceptanceCode_2 = null;
        this.mbrType_2 = null;
        this.mbrStatus_2 = null;
        this.duesType_2 = null;
        this.affPk_3 = null;
        this.type_3 = null;
        this.local_3 = null;
        this.state_3 = null;
        this.council_3 = null;
        this.subUnit_3 = null;
        this.code_3 = null;
        this.duration_3 = null;
        this.filedWith_3 = null;
        this.acceptanceCode_3 = null;
        this.mbrType_3 = null;
        this.mbrStatus_3 = null;
        this.duesType_3 = null;
    }
    
    public String toString() {
        return
        "1st Associated Affiliate:" +
        "Affiliate Pk: " + affPk_1 +
        "Affiliate Type: " + type_1 +
        "Affiliate Local: " + local_1 +
        "Affiliate State: " + state_1 +
        "Affiliate Council: " + council_1 +
        "Affiliate Sub Unit: " + subUnit_1 +
        "Affiliate Code: " + code_1 +
        "Duration: " + duration_1 +
        "Filed With " + filedWith_1 +
        "Acceptance Code: " + acceptanceCode_1 +
        "Member Type: " + mbrType_1 +
        "Member Status: " + mbrStatus_1 +
        "Dues Type: " + duesType_1 +
        "2nd Associated Affiliate:" +
        "Affiliate Pk: " + affPk_2 +
        "Affiliate Type: " + type_2 +
        "Affiliate Local: " + local_2 +
        "Affiliate State: " + state_2 +
        "Affiliate Council: " + council_2 +
        "Affiliate Sub Unit: " + subUnit_2 +
        "Affiliate Code: " + code_2 +
        "Duration: " + duration_2 +
        "Filed With " + filedWith_2 +
        "Acceptance Code: " + acceptanceCode_2 +
        "Member Type: " + mbrType_2 +
        "Member Status: " + mbrStatus_2 +
        "Dues Type: " + duesType_2 +
        "3rd Associated Affiliate:" +
        "Affiliate Pk: " + affPk_3 +
        "Affiliate Type: " + type_3 +
        "Affiliate Local: " + local_3 +
        "Affiliate State: " + state_3 +
        "Affiliate Council: " + council_3 +
        "Affiliate Sub Unit: " + subUnit_3 +
        "Affiliate Code: " + code_3 +
        "Duration: " + duration_3 +
        "Filed With " + filedWith_3 +
        "Acceptance Code: " + acceptanceCode_3 +
        "Member Type: " + mbrType_3 +
        "Member Status: " + mbrStatus_3 +
        "Dues Type: " + duesType_3;
    }
    
    public void setPRBDuesPaid(PRBAffiliateData prb, int index) {
        if (prb != null) {     
            // Set Duration default to "12 Months" if not specified
            if (prb.getDurationPk()==null || prb.getDurationPk().intValue()==0) {
                prb.setDurationPk(RebateDuration.TWELVE);
            }
            switch (index) {
                case 1:
                    setAffPk_1(prb.getAffPk());
                    setType_1(prb.getTheAffiliateIdentifier().getType());
                    setLocal_1(prb.getTheAffiliateIdentifier().getLocal());
                    setState_1(prb.getTheAffiliateIdentifier().getState());
                    setCouncil_1(prb.getTheAffiliateIdentifier().getCouncil());
                    setSubUnit_1(prb.getTheAffiliateIdentifier().getSubUnit());
                    setCode_1(prb.getTheAffiliateIdentifier().getCode());
                    setDuration_1(prb.getDurationPk());
                    setFiledWith_1(prb.getFiledWithPk());
                    setAcceptanceCode_1(prb.getAcceptanceCodePk());
                    setMbrType_1(prb.getRbtMbrTypePk());
                    setMbrStatus_1(prb.getRbtMbrStatusPk());
                    setDuesType_1(prb.getDuesTypePk());
                    break;
                case 2:
                    setAffPk_2(prb.getAffPk());
                    setType_2(prb.getTheAffiliateIdentifier().getType());
                    setLocal_2(prb.getTheAffiliateIdentifier().getLocal());
                    setState_2(prb.getTheAffiliateIdentifier().getState());
                    setCouncil_2(prb.getTheAffiliateIdentifier().getCouncil());
                    setSubUnit_2(prb.getTheAffiliateIdentifier().getSubUnit());
                    setCode_2(prb.getTheAffiliateIdentifier().getCode());
                    setDuration_2(prb.getDurationPk());
                    setFiledWith_2(prb.getFiledWithPk());
                    setAcceptanceCode_2(prb.getAcceptanceCodePk());
                    setMbrType_2(prb.getRbtMbrTypePk());
                    setMbrStatus_2(prb.getRbtMbrStatusPk());
                    setDuesType_2(prb.getDuesTypePk());
                    break;
                case (3):
                    setAffPk_3(prb.getAffPk());
                    setType_3(prb.getTheAffiliateIdentifier().getType());
                    setLocal_3(prb.getTheAffiliateIdentifier().getLocal());
                    setState_3(prb.getTheAffiliateIdentifier().getState());
                    setCouncil_3(prb.getTheAffiliateIdentifier().getCouncil());
                    setSubUnit_3(prb.getTheAffiliateIdentifier().getSubUnit());
                    setCode_3(prb.getTheAffiliateIdentifier().getCode());
                    setDuration_3(prb.getDurationPk());
                    setFiledWith_3(prb.getFiledWithPk());
                    setAcceptanceCode_3(prb.getAcceptanceCodePk());
                    setMbrType_3(prb.getRbtMbrTypePk());
                    setMbrStatus_3(prb.getRbtMbrStatusPk());
                    setDuesType_3(prb.getDuesTypePk());
                    break;
            }
        }
    }
    
    public PRBAffiliateData getPRBDuesPaid(int index) {
        PRBAffiliateData prb = new PRBAffiliateData();
        switch (index) {
            case 1:
                prb.setAffPk(affPk_1);
                prb.setDurationPk(duration_1);
                prb.setFiledWithPk(filedWith_1);
                prb.setAcceptanceCodePk(acceptanceCode_1);
                prb.setRbtMbrTypePk(mbrType_1);
                prb.setRbtMbrStatusPk(mbrStatus_1);
                prb.setDuesTypePk(duesType_1);                
                break;
            case 2:
                prb.setAffPk(affPk_2);
                prb.setDurationPk(duration_2);
                prb.setFiledWithPk(filedWith_2);
                prb.setAcceptanceCodePk(acceptanceCode_2);
                prb.setRbtMbrTypePk(mbrType_2);
                prb.setRbtMbrStatusPk(mbrStatus_2);
                prb.setDuesTypePk(duesType_2);                
                break;
            case (3):
                prb.setAffPk(affPk_3);
                prb.setDurationPk(duration_3);
                prb.setFiledWithPk(filedWith_3);
                prb.setAcceptanceCodePk(acceptanceCode_3);
                prb.setRbtMbrTypePk(mbrType_3);
                prb.setRbtMbrStatusPk(mbrStatus_3);
                prb.setDuesTypePk(duesType_3);                
                break;
        }
        return prb;
    }
    
    public ActionErrors validateDuesPaidToAffIDs() {
        ActionErrors errors = new ActionErrors();
            
        // Check for Affilate Identifiers, atleast one full Affiliate Identifier is required
        if (getType_1()==null && getLocal_1()==null && getState_1()==null && getCouncil_1()==null) {
            errors.add("affPk_1", new ActionError("error.field.affID.required"));
        }
        if (getAffPk_2()!=null) {
            if (getAffPk_1()!=null && getAffPk_1().intValue()==getAffPk_2().intValue()) {
                setAffPk_2(null);
                errors.add("affPk_2", new ActionError("error.field.affID.duplicate", "Affiliate Identifer #2"));
            }
        }        
        if (getAffPk_3()!=null) {
            if (getAffPk_1()!=null && getAffPk_2()!=null) {
                if (getAffPk_1().intValue()==getAffPk_3().intValue() || getAffPk_2().intValue()==getAffPk_3().intValue()) {
                    setAffPk_3(null);
                    errors.add("affPk_3", new ActionError("error.field.affID.duplicate", "Affiliate Identifer #3"));
                }
            }
        }    
        return errors;
    }
    
    /** Getter for property acceptanceCode_1.
     * @return Value of property acceptanceCode_1.
     *
     */
    public java.lang.Integer getAcceptanceCode_1() {
        return acceptanceCode_1;
    }
    
    /** Setter for property acceptanceCode_1.
     * @param acceptanceCode_1 New value of property acceptanceCode_1.
     *
     */
    public void setAcceptanceCode_1(java.lang.Integer acceptanceCode_1) {
        this.acceptanceCode_1 = setInteger(acceptanceCode_1);
    }
    
    /** Getter for property acceptanceCode_2.
     * @return Value of property acceptanceCode_2.
     *
     */
    public java.lang.Integer getAcceptanceCode_2() {
        return acceptanceCode_2;
    }
    
    /** Setter for property acceptanceCode_2.
     * @param acceptanceCode_2 New value of property acceptanceCode_2.
     *
     */
    public void setAcceptanceCode_2(java.lang.Integer acceptanceCode_2) {
        this.acceptanceCode_2 = setInteger(acceptanceCode_2);
    }
    
    /** Getter for property acceptanceCode_3.
     * @return Value of property acceptanceCode_3.
     *
     */
    public java.lang.Integer getAcceptanceCode_3() {
        return acceptanceCode_3;
    }
    
    /** Setter for property acceptanceCode_3.
     * @param acceptanceCode_3 New value of property acceptanceCode_3.
     *
     */
    public void setAcceptanceCode_3(java.lang.Integer acceptanceCode_3) {
        this.acceptanceCode_3 = setInteger(acceptanceCode_3);
    }
    
    /** Getter for property council_1.
     * @return Value of property council_1.
     *
     */
    public java.lang.String getCouncil_1() {
        return council_1;
    }
    
    /** Setter for property council_1.
     * @param council_1 New value of property council_1.
     *
     */
    public void setCouncil_1(java.lang.String council_1) {
        this.council_1 = (TextUtil.isEmptyOrSpaces(council_1)) ? null : council_1;
    }
    
    /** Getter for property council_2.
     * @return Value of property council_2.
     *
     */
    public java.lang.String getCouncil_2() {
        return council_2;
    }
    
    /** Setter for property council_2.
     * @param council_2 New value of property council_2.
     *
     */
    public void setCouncil_2(java.lang.String council_2) {
        this.council_2 = (TextUtil.isEmptyOrSpaces(council_2)) ? null : council_2;
    }
    
    /** Getter for property council_3.
     * @return Value of property council_3.
     *
     */
    public java.lang.String getCouncil_3() {
        return council_3;
    }
    
    /** Setter for property council_3.
     * @param council_3 New value of property council_3.
     *
     */
    public void setCouncil_3(java.lang.String council_3) {
        this.council_3 = (TextUtil.isEmptyOrSpaces(council_3)) ? null : council_3;
    }
    
    /** Getter for property duesType_1.
     * @return Value of property duesType_1.
     *
     */
    public java.lang.Integer getDuesType_1() {
        return duesType_1;
    }
    
    /** Setter for property duesType_1.
     * @param duesType_1 New value of property duesType_1.
     *
     */
    public void setDuesType_1(java.lang.Integer duesType_1) {
        this.duesType_1 = setInteger(duesType_1);
    }
    
    /** Getter for property duesType_2.
     * @return Value of property duesType_2.
     *
     */
    public java.lang.Integer getDuesType_2() {
        return duesType_2;
    }
    
    /** Setter for property duesType_2.
     * @param duesType_2 New value of property duesType_2.
     *
     */
    public void setDuesType_2(java.lang.Integer duesType_2) {
        this.duesType_2 = setInteger(duesType_2);
    }
    
    /** Getter for property duesType_3.
     * @return Value of property duesType_3.
     *
     */
    public java.lang.Integer getDuesType_3() {
        return duesType_3;
    }
    
    /** Setter for property duesType_3.
     * @param duesType_3 New value of property duesType_3.
     *
     */
    public void setDuesType_3(java.lang.Integer duesType_3) {
        this.duesType_3 = setInteger(duesType_3);
    }
    
    /** Getter for property duration_1.
     * @return Value of property duration_1.
     *
     */
    public java.lang.Integer getDuration_1() {
        return duration_1;
    }
    
    /** Setter for property duration_1.
     * @param duration_1 New value of property duration_1.
     *
     */
    public void setDuration_1(java.lang.Integer duration_1) {
        this.duration_1 = setInteger(duration_1);
    }
    
    /** Getter for property duration_2.
     * @return Value of property duration_2.
     *
     */
    public java.lang.Integer getDuration_2() {
        return duration_2;
    }
    
    /** Setter for property duration_2.
     * @param duration_2 New value of property duration_2.
     *
     */
    public void setDuration_2(java.lang.Integer duration_2) {
        this.duration_2 = setInteger(duration_2);
    }
    
    /** Getter for property duration_3.
     * @return Value of property duration_3.
     *
     */
    public java.lang.Integer getDuration_3() {
        return duration_3;
    }
    
    /** Setter for property duration_3.
     * @param duration_3 New value of property duration_3.
     *
     */
    public void setDuration_3(java.lang.Integer duration_3) {
        this.duration_3 = setInteger(duration_3);
    }
    
    /** Getter for property filedWith_1.
     * @return Value of property filedWith_1.
     *
     */
    public java.lang.Integer getFiledWith_1() {
        return filedWith_1;
    }
    
    /** Setter for property filedWith_1.
     * @param filedWith_1 New value of property filedWith_1.
     *
     */
    public void setFiledWith_1(java.lang.Integer filedWith_1) {
        this.filedWith_1 = setInteger(filedWith_1);
    }
    
    /** Getter for property filedWith_2.
     * @return Value of property filedWith_2.
     *
     */
    public java.lang.Integer getFiledWith_2() {
        return filedWith_2;
    }
    
    /** Setter for property filedWith_2.
     * @param filedWith_2 New value of property filedWith_2.
     *
     */
    public void setFiledWith_2(java.lang.Integer filedWith_2) {
        this.filedWith_2 = setInteger(filedWith_2);
    }
    
    /** Getter for property filedWith_3.
     * @return Value of property filedWith_3.
     *
     */
    public java.lang.Integer getFiledWith_3() {
        return filedWith_3;
    }
    
    /** Setter for property filedWith_3.
     * @param filedWith_3 New value of property filedWith_3.
     *
     */
    public void setFiledWith_3(java.lang.Integer filedWith_3) {
        this.filedWith_3 = setInteger(filedWith_3);
    }
    
    /** Getter for property local_1.
     * @return Value of property local_1.
     *
     */
    public java.lang.String getLocal_1() {
        return local_1;
    }
    
    /** Setter for property local_1.
     * @param local_1 New value of property local_1.
     *
     */
    public void setLocal_1(java.lang.String local_1) {
        this.local_1 = TextUtil.isEmptyOrSpaces(local_1) ? null : local_1;
    }
    
    /** Getter for property local_2.
     * @return Value of property local_2.
     *
     */
    public java.lang.String getLocal_2() {
        return local_2;
    }
    
    /** Setter for property local_2.
     * @param local_2 New value of property local_2.
     *
     */
    public void setLocal_2(java.lang.String local_2) {
        this.local_2 = TextUtil.isEmptyOrSpaces(local_2) ? null : local_2;
    }
    
    /** Getter for property local_3.
     * @return Value of property local_3.
     *
     */
    public java.lang.String getLocal_3() {
        return local_3;
    }
    
    /** Setter for property local_3.
     * @param local_3 New value of property local_3.
     *
     */
    public void setLocal_3(java.lang.String local_3) {
        this.local_3 = TextUtil.isEmptyOrSpaces(local_3) ? null : local_3;
    }
    
    /** Getter for property mbrStatus_1.
     * @return Value of property mbrStatus_1.
     *
     */
    public java.lang.Integer getMbrStatus_1() {
        return mbrStatus_1;
    }
    
    /** Setter for property mbrStatus_1.
     * @param mbrStatus_1 New value of property mbrStatus_1.
     *
     */
    public void setMbrStatus_1(java.lang.Integer mbrStatus_1) {
        this.mbrStatus_1 = setInteger(mbrStatus_1);
    }
    
    /** Getter for property mbrStatus_2.
     * @return Value of property mbrStatus_2.
     *
     */
    public java.lang.Integer getMbrStatus_2() {
        return mbrStatus_2;
    }
    
    /** Setter for property mbrStatus_2.
     * @param mbrStatus_2 New value of property mbrStatus_2.
     *
     */
    public void setMbrStatus_2(java.lang.Integer mbrStatus_2) {
        this.mbrStatus_2 = setInteger(mbrStatus_2);
    }
    
    /** Getter for property mbrStatus_3.
     * @return Value of property mbrStatus_3.
     *
     */
    public java.lang.Integer getMbrStatus_3() {
        return mbrStatus_3;
    }
    
    /** Setter for property mbrStatus_3.
     * @param mbrStatus_3 New value of property mbrStatus_3.
     *
     */
    public void setMbrStatus_3(java.lang.Integer mbrStatus_3) {
        this.mbrStatus_3 = setInteger(mbrStatus_3);
    }
    
    /** Getter for property mbrType_1.
     * @return Value of property mbrType_1.
     *
     */
    public java.lang.Integer getMbrType_1() {
        return mbrType_1;
    }
    
    /** Setter for property mbrType_1.
     * @param mbrType_1 New value of property mbrType_1.
     *
     */
    public void setMbrType_1(java.lang.Integer mbrType_1) {
        this.mbrType_1 = setInteger(mbrType_1);
    }
    
    /** Getter for property mbrType_2.
     * @return Value of property mbrType_2.
     *
     */
    public java.lang.Integer getMbrType_2() {
        return mbrType_2;
    }
    
    /** Setter for property mbrType_2.
     * @param mbrType_2 New value of property mbrType_2.
     *
     */
    public void setMbrType_2(java.lang.Integer mbrType_2) {
        this.mbrType_2 = setInteger(mbrType_2);
    }
    
    /** Getter for property mbrType_3.
     * @return Value of property mbrType_3.
     *
     */
    public java.lang.Integer getMbrType_3() {
        return mbrType_3;
    }
    
    /** Setter for property mbrType_3.
     * @param mbrType_3 New value of property mbrType_3.
     *
     */
    public void setMbrType_3(java.lang.Integer mbrType_3) {
        this.mbrType_3 = setInteger(mbrType_3);
    }
    
    /** Getter for property state_1.
     * @return Value of property state_1.
     *
     */
    public java.lang.String getState_1() {
        return state_1;
    }
    
    /** Setter for property state_1.
     * @param state_1 New value of property state_1.
     *
     */
    public void setState_1(java.lang.String state_1) {
        this.state_1 = TextUtil.isEmptyOrSpaces(state_1) ? null : state_1;
    }
    
    /** Getter for property state_2.
     * @return Value of property state_2.
     *
     */
    public java.lang.String getState_2() {
        return state_2;
    }
    
    /** Setter for property state_2.
     * @param state_2 New value of property state_2.
     *
     */
    public void setState_2(java.lang.String state_2) {
        this.state_2 = TextUtil.isEmptyOrSpaces(state_2) ? null : state_2;
    }
    
    /** Getter for property state_3.
     * @return Value of property state_3.
     *
     */
    public java.lang.String getState_3() {
        return state_3;
    }
    
    /** Setter for property state_3.
     * @param state_3 New value of property state_3.
     *
     */
    public void setState_3(java.lang.String state_3) {
        this.state_3 = TextUtil.isEmptyOrSpaces(state_3) ? null : state_3;
    }
    
    /** Getter for property type_1.
     * @return Value of property type_1.
     *
     */
    public java.lang.Character getType_1() {
        return type_1;
    }
    
    /** Setter for property type_1.
     * @param type_1 New value of property type_1.
     *
     */
    public void setType_1(java.lang.Character type_1) {
        this.type_1 = TextUtil.isEmptyOrSpaces(type_1) ? null : type_1;
    }
    
    /** Getter for property type_2.
     * @return Value of property type_2.
     *
     */
    public java.lang.Character getType_2() {
        return type_2;
    }
    
    /** Setter for property type_2.
     * @param type_2 New value of property type_2.
     *
     */
    public void setType_2(java.lang.Character type_2) {
        this.type_2 = TextUtil.isEmptyOrSpaces(type_2) ? null : type_2;
    }
    
    /** Getter for property type_3.
     * @return Value of property type_3.
     *
     */
    public java.lang.Character getType_3() {
        return type_3;
    }
    
    /** Setter for property type_3.
     * @param type_3 New value of property type_3.
     *
     */
    public void setType_3(java.lang.Character type_3) {
        this.type_3 = TextUtil.isEmptyOrSpaces(type_3) ? null : type_3;
    }
    
    /** Getter for property affCount.
     * @return Value of property affCount.
     *
     */
    public int getAffCount() {
        return affCount;
    }
    
    /** Setter for property affCount.
     * @param affCount New value of property affCount.
     *
     */
    public void setAffCount(int affCount) {
        this.affCount = affCount;
    }
    
    /** Getter for property code_1.
     * @return Value of property code_1.
     *
     */
    public java.lang.Character getCode_1() {
        return code_1;
    }
    
    /** Setter for property code_1.
     * @param code_1 New value of property code_1.
     *
     */
    public void setCode_1(java.lang.Character code_1) {
        this.code_1 = TextUtil.isEmptyOrSpaces(code_1) ? null : code_1;
    }
    
    /** Getter for property code_2.
     * @return Value of property code_2.
     *
     */
    public java.lang.Character getCode_2() {
        return code_2;
    }
    
    /** Setter for property code_2.
     * @param code_2 New value of property code_2.
     *
     */
    public void setCode_2(java.lang.Character code_2) {
        this.code_2 = TextUtil.isEmptyOrSpaces(code_2) ? null : code_2;
    }
    
    /** Getter for property code_3.
     * @return Value of property code_3.
     *
     */
    public java.lang.Character getCode_3() {
        return code_3;
    }
    
    /** Setter for property code_3.
     * @param code_3 New value of property code_3.
     *
     */
    public void setCode_3(java.lang.Character code_3) {
        this.code_3 = TextUtil.isEmptyOrSpaces(code_3) ? null : code_3;
    }
    
    /** Getter for property subUnit_1.
     * @return Value of property subUnit_1.
     *
     */
    public java.lang.String getSubUnit_1() {
        return subUnit_1;
    }
    
    /** Setter for property subUnit_1.
     * @param subUnit_1 New value of property subUnit_1.
     *
     */
    public void setSubUnit_1(java.lang.String subUnit_1) {
        this.subUnit_1 = TextUtil.isEmptyOrSpaces(subUnit_1) ? null : subUnit_1;
    }
    
    /** Getter for property subUnit_2.
     * @return Value of property subUnit_2.
     *
     */
    public java.lang.String getSubUnit_2() {
        return subUnit_2;
    }
    
    /** Setter for property subUnit_2.
     * @param subUnit_2 New value of property subUnit_2.
     *
     */
    public void setSubUnit_2(java.lang.String subUnit_2) {
        this.subUnit_2 = TextUtil.isEmptyOrSpaces(subUnit_2) ? null : subUnit_2;
    }
    
    /** Getter for property subUnit_3.
     * @return Value of property subUnit_3.
     *
     */
    public java.lang.String getSubUnit_3() {
        return subUnit_3;
    }
    
    /** Setter for property subUnit_3.
     * @param subUnit_3 New value of property subUnit_3.
     *
     */
    public void setSubUnit_3(java.lang.String subUnit_3) {
        this.subUnit_3 = TextUtil.isEmptyOrSpaces(subUnit_3) ? null : subUnit_3;
    }
    
    /** Getter for property affPk_1.
     * @return Value of property affPk_1.
     *
     */
    public java.lang.Integer getAffPk_1() {
        return affPk_1;
    }
    
    /** Setter for property affPk_1.
     * @param affPk_1 New value of property affPk_1.
     *
     */
    public void setAffPk_1(java.lang.Integer affPk_1) {
        this.affPk_1 = setInteger(affPk_1);
    }
    
    /** Getter for property affPk_2.
     * @return Value of property affPk_2.
     *
     */
    public java.lang.Integer getAffPk_2() {
        return affPk_2;
    }
    
    /** Setter for property affPk_2.
     * @param affPk_2 New value of property affPk_2.
     *
     */
    public void setAffPk_2(java.lang.Integer affPk_2) {
        this.affPk_2 = setInteger(affPk_2);
    }
    
    /** Getter for property affPk_3.
     * @return Value of property affPk_3.
     *
     */
    public java.lang.Integer getAffPk_3() {
        return affPk_3;
    }
    
    /** Setter for property affPk_3.
     * @param affPk_3 New value of property affPk_3.
     *
     */
    public void setAffPk_3(java.lang.Integer affPk_3) {
        this.affPk_3 = setInteger(affPk_3);
    }
    
    /** Getter for property search.
     * @return Value of property search.
     *
     */
    public boolean isSearch() {
        return search;
    }
    
    /** Setter for property search.
     * @param search New value of property search.
     *
     */
    public void setSearch(boolean search) {
        this.search = search;
    }
    
}
