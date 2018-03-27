package org.afscme.enterprise.affiliate;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents WageIncForm data.
 */
public class WageIncForm {
    private int wifPk;
    private int empAffFk;
    private int duesyear;
    private int agreementFk;
    private int tot_num_mem;
    private double average_wages;

    private int formCompleted;
    private int correspondence;
    private String correspondenceDate;
    private String comments;
    //private String moreLocals;
    private String durationTo;
    private String durationFrom;
    private int inNegotiations;
    private int agreementReceived;
    private String agreementDesc;

    private String employer_name;
    private String contact_name;
    private String contact_email;
    private String contact_phone;

    private int created_user_pk;
    private Timestamp created_dt;
    private int lst_mod_user_pk;
    private Timestamp lst_mod_dt;

    private int agreementPk;
    private String agreementName;

    public WageIncForm() {
        this.wifPk = 0;
        this.empAffFk = 0;
        this.duesyear = 0;
        this.agreementFk = 0;
        this.tot_num_mem = 0;
        this.average_wages = 0.0;
        this.formCompleted = 0;
        this.correspondence = 0;
        this.correspondenceDate = null;
        this.comments = null;

        this.agreementPk = 0;
        this.agreementName = null;

        this.durationTo = null;
        this.durationFrom = null;
        this.inNegotiations = 0;
    	this.employer_name = null;
    	this.contact_name = null;
    	this.contact_email = null;
    	this.contact_phone = null;

        this.created_user_pk = 0;
        this.created_dt = null;
        this.lst_mod_user_pk = 0;
        this.lst_mod_dt = null;
    }

    /** Getter for property wifPk.
     * @return Value of property wifPk.
     *
     */
    public int getWifPk() {
        return wifPk;
    }

    /** Setter for property wifPk.
     * @param wifPk New value of property wifPk.
     *
     */
    public void setWifPk(int wifPk) {
        this.wifPk = wifPk;
    }

    /** Getter for property duesyear.
     * @return Value of property duesyear.
     *
     */
    public int getDuesyear() {
        return duesyear;
    }

    /** Setter for property duesyear.
     * @param duesyear New value of property duesyear.
     *
     */
    public void setDuesyear(int duesyear) {
        this.duesyear = duesyear;
    }

    /** Getter for property tot_num_mem.
     * @return Value of property tot_num_mem.
     *
     */
    public int getTot_num_mem() {
        return tot_num_mem;
    }

    /** Setter for property tot_num_mem.
     * @param tot_num_mem New value of property tot_num_mem.
     *
     */
    public void setTot_num_mem(int tot_num_mem) {
        this.tot_num_mem = tot_num_mem;
    }

    /** Getter for property average_wages.
     * @return Value of property average_wages.
     *
     */
    public double getAverage_wages() {
        return average_wages;
    }

    /** Setter for property average_wages.
     * @param average_wages New value of property average_wages.
     *
     */
    public void setAverage_wages(double average_wages) {
        this.average_wages = average_wages;
    }

    /** Getter for property created_user_pk.
     * @return Value of property created_user_pk.
     *
     */
    public int getCreated_user_pk() {
        return created_user_pk;
    }

    /** Setter for property created_user_pk.
     * @param created_user_pk New value of property created_user_pk.
     *
     */
    public void setCreated_user_pk(int created_user_pk) {
        this.created_user_pk = created_user_pk;
    }

    /** Getter for property created_dt.
     * @return Value of property created_dt.
     *
     */
    public java.sql.Timestamp getCreated_dt() {
        return created_dt;
    }

    /** Setter for property created_dt.
     * @param created_dt New value of property created_dt.
     *
     */
    public void setCreated_dt(java.sql.Timestamp created_dt) {
        this.created_dt = created_dt;
    }

    /** Getter for property lst_mod_user_pk.
     * @return Value of property lst_mod_user_pk.
     *
     */
    public int getLst_mod_user_pk() {
        return lst_mod_user_pk;
    }

    /** Setter for property lst_mod_user_pk.
     * @param lst_mod_user_pk New value of property lst_mod_user_pk.
     *
     */
    public void setLst_mod_user_pk(int lst_mod_user_pk) {
        this.lst_mod_user_pk = lst_mod_user_pk;
    }

    /** Getter for property lst_mod_dt.
     * @return Value of property lst_mod_dt.
     *
     */
    public java.sql.Timestamp getLst_mod_dt() {
        return lst_mod_dt;
    }

    /** Setter for property lst_mod_dt.
     * @param lst_mod_dt New value of property lst_mod_dt.
     *
     */
    public void setLst_mod_dt(java.sql.Timestamp lst_mod_dt) {
        this.lst_mod_dt = lst_mod_dt;
    }

    /** Getter for property formCompleted.
     * @return Value of property formCompleted.
     *
     */
    public int getFormCompleted() {
        return formCompleted;
    }

    /** Setter for property formCompleted.
     * @param formCompleted New value of property formCompleted.
     *
     */
    public void setFormCompleted(int formCompleted) {
        this.formCompleted = formCompleted;
    }

    /** Getter for property correspondence.
     * @return Value of property correspondence.
     *
     */
    public int getCorrespondence() {
        return correspondence;
    }

    /** Setter for property correspondence.
     * @param correspondence New value of property correspondence.
     *
     */
    public void setCorrespondence(int correspondence) {
        this.correspondence = correspondence;
    }

    /** Getter for property correspondenceDate.
     * @return Value of property correspondenceDate.
     *
     */
    public java.lang.String getCorrespondenceDate() {
        return correspondenceDate;
    }

    /** Setter for property correspondenceDate.
     * @param correspondenceDate New value of property correspondenceDate.
     *
     */
    public void setCorrespondenceDate(java.lang.String correspondenceDate) {
        this.correspondenceDate = correspondenceDate;
    }

    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public java.lang.String getComments() {
        return comments;
    }

    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(java.lang.String comments) {
	  if (comments != null)
      	//this.comments = comments.trim().replaceAll("\'", "\'\'");
      	this.comments = comments.trim();
      else
      	this.comments = "";
    }

    /*
    public java.lang.String getMoreLocals() {
        return moreLocals;
    }

    public void setMoreLocals(java.lang.String moreLocals) {
        this.moreLocals = moreLocals;
    }
    */

    /** Getter for property inNegotiations.
     * @return Value of property inNegotiations.
     *
     */
    public int getInNegotiations() {
        return inNegotiations;
    }

    /** Setter for property inNegotiations.
     * @param inNegotiations New value of property inNegotiations.
     *
     */
    public void setInNegotiations(int inNegotiations) {
        this.inNegotiations = inNegotiations;
    }

    /** Getter for property durationTo.
     * @return Value of property durationTo.
     *
     */
    public java.lang.String getDurationTo() {
        return durationTo;
    }

    /** Setter for property durationTo.
     * @param durationTo New value of property durationTo.
     *
     */
    public void setDurationTo(java.lang.String durationTo) {
        this.durationTo = durationTo;
    }

    /** Getter for property durationFrom.
     * @return Value of property durationFrom.
     *
     */
    public java.lang.String getDurationFrom() {
        return durationFrom;
    }

    /** Setter for property durationFrom.
     * @param durationFrom New value of property durationFrom.
     *
     */
    public void setDurationFrom(java.lang.String durationFrom) {
        this.durationFrom = durationFrom;
    }

    /** Getter for property agreementReceived.
     * @return Value of property agreementReceived.
     *
     */
    public int getAgreementReceived() {
        return agreementReceived;
    }

    /** Setter for property agreementReceived.
     * @param agreementReceived New value of property agreementReceived.
     *
     */
    public void setAgreementReceived(int agreementReceived) {
        this.agreementReceived = agreementReceived;
    }

    /** Getter for property agreementDesc.
     * @return Value of property agreementDesc.
     *
     */
    public java.lang.String getAgreementDesc() {
        return agreementDesc;
    }

    /** Setter for property agreementDesc.
     * @param agreementDesc New value of property agreementDesc.
     *
     */
    public void setAgreementDesc(java.lang.String agreementDesc) {
        this.agreementDesc = agreementDesc;
    }

    /** Getter for property empAffFk.
     * @return Value of property empAffFk.
     *
     */
    public int getEmpAffFk() {
        return empAffFk;
    }

    /** Setter for property empAffFk.
     * @param empAffFk New value of property empAffFk.
     *
     */
    public void setEmpAffFk(int empAffFk) {
        this.empAffFk = empAffFk;
    }

    /** Getter for property agreementFk.
     * @return Value of property agreementFk.
     *
     */
    public int getAgreementFk() {
        return agreementFk;
    }

    /** Setter for property agreementFk.
     * @param agreementFk New value of property agreementFk.
     *
     */
    public void setAgreementFk(int agreementFk) {
        this.agreementFk = agreementFk;
    }

    /** Getter for property employer_name.
     * @return Value of property employer_name.
     *
     */
    public java.lang.String getEmployer_name() {
        return employer_name;
    }

    /** Setter for property employer_name.
     * @param employer_name New value of property employer_name.
     *
     */
    public void setEmployer_name(java.lang.String employer_name) {
	  if (employer_name != null)
      	this.employer_name = employer_name.trim();
      else
      	this.employer_name = "";
    }

    /** Getter for property contact_name.
     * @return Value of property contact_name.
     *
     */
    public java.lang.String getContact_name() {
        return contact_name;
    }

    /** Setter for property contact_name.
     * @param contact_name New value of property contact_name.
     *
     */
    public void setContact_name(java.lang.String contact_name) {
	  if (contact_name != null)
      	this.contact_name = contact_name.trim();
      else
      	this.contact_name = "";
    }

    /** Getter for property contact_email.
     * @return Value of property contact_email.
     *
     */
    public java.lang.String getContact_email() {
        return contact_email;
    }

    /** Setter for property contact_email.
     * @param contact_email New value of property contact_email.
     *
     */
    public void setContact_email(java.lang.String contact_email) {
        this.contact_email = contact_email;
    }

    /** Getter for property contact_phone.
     * @return Value of property contact_phone.
     *
     */
    public java.lang.String getContact_phone() {
        return contact_phone;
    }

    /** Setter for property contact_phone.
     * @param contact_phone New value of property contact_phone.
     *
     */
    public void setContact_phone(java.lang.String contact_phone) {
	  if (contact_phone != null)
      	this.contact_phone = contact_phone.trim();
      else
      	this.contact_phone = "";
    }

    /** Getter for property agreementPk.
     * @return Value of property agreementPk.
     *
     */
    public int getAgreementPk() {
        return agreementPk;
    }

    /** Setter for property agreementPk.
     * @param agreementPk New value of property agreementPk.
     *
     */
    public void setAgreementPk(int agreementPk) {
        this.agreementPk = agreementPk;
    }

    /** Getter for property agreementName.
     * @return Value of property agreementName.
     *
     */
    public java.lang.String getAgreementName() {
        String rtv = null;

        if (agreementName == null)
        	rtv = "";
        else
        	rtv = agreementName;

        return rtv;
    }

    /** Setter for property agreementName.
     * @param agreementName New value of property agreementName.
     *
     */
    public void setAgreementName(java.lang.String agreementName) {
        this.agreementName = agreementName;
    }

}
