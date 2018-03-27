package org.afscme.enterprise.affiliate;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents WageIncData data.
 */
public class WageIncData {
    private int widPk;
    private int wifFk;

    private int wageIncType;
    private double wageInc;
    private String effectiveDate;
    private int numAffected;
    private String paymentTypeAdj;
    private double wageIncAdj;
    private int numAffectedAdj;
    private double numTimesInc;

    private int created_user_pk;
    private Timestamp created_dt;
    private int lst_mod_user_pk;
    private Timestamp lst_mod_dt;


    public WageIncData() {
    	this.widPk = 0;
    	this.wifFk = 0;

		this.wageIncType = 0;
		this.wageInc = 0.0;
		this.effectiveDate = null;
		this.numAffected = 0;
		this.paymentTypeAdj = null;
		this.wageIncAdj = 0.0;
		this.numAffectedAdj = 0;
		this.numTimesInc = 0.0;

        this.created_user_pk = 0;
        this.created_dt = null;
        this.lst_mod_user_pk = 0;
        this.lst_mod_dt = null;
    }

    /** Getter for property widPk.
     * @return Value of property widPk.
     *
     */
    public int getWidPk() {
        return widPk;
    }

    /** Setter for property widPk.
     * @param widPk New value of property widPk.
     *
     */
    public void setWidPk(int widPk) {
        this.widPk = widPk;
    }

    /** Getter for property wifFk.
     * @return Value of property wifFk.
     *
     */
    public int getWifFk() {
        return wifFk;
    }

    /** Setter for property wifFk.
     * @param wifFk New value of property wifFk.
     *
     */
    public void setWifFk(int wifFk) {
        this.wifFk = wifFk;
    }

    /** Getter for property wageIncType.
     * @return Value of property wageIncType.
     *
     */
    public int getWageIncType() {
        return wageIncType;
    }

    /** Setter for property wageIncType.
     * @param wageIncType New value of property wageIncType.
     *
     */
    public void setWageIncType(int wageIncType) {
        this.wageIncType = wageIncType;
    }

    /** Getter for property wageInc.
     * @return Value of property wageInc.
     *
     */
    public double getWageInc() {
        return wageInc;
    }

    /** Setter for property wageInc.
     * @param wageInc New value of property wageInc.
     *
     */
    public void setWageInc(double wageInc) {
        this.wageInc = wageInc;
    }

    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     *
     */
    public java.lang.String getEffectiveDate() {
        return effectiveDate;
    }

    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     *
     */
    public void setEffectiveDate(java.lang.String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /** Getter for property numAffected.
     * @return Value of property numAffected.
     *
     */
    public int getNumAffected() {
        return numAffected;
    }

    /** Setter for property numAffected.
     * @param numAffected New value of property numAffected.
     *
     */
    public void setNumAffected(int numAffected) {
        this.numAffected = numAffected;
    }

    /** Getter for property paymentTypeAdj.
     * @return Value of property paymentTypeAdj.
     *
     */
    public java.lang.String getPaymentTypeAdj() {
        return paymentTypeAdj;
    }

    /** Setter for property paymentTypeAdj.
     * @param paymentTypeAdj New value of property paymentTypeAdj.
     *
     */
    public void setPaymentTypeAdj(java.lang.String paymentTypeAdj) {
        this.paymentTypeAdj = paymentTypeAdj;
    }

    /** Getter for property wageIncAdj.
     * @return Value of property wageIncAdj.
     *
     */
    public double getWageIncAdj() {
        return wageIncAdj;
    }

    /** Setter for property wageIncAdj.
     * @param wageIncAdj New value of property wageIncAdj.
     *
     */
    public void setWageIncAdj(double wageIncAdj) {
        this.wageIncAdj = wageIncAdj;
    }

    /** Getter for property numAffectedAdj.
     * @return Value of property numAffectedAdj.
     *
     */
    public int getNumAffectedAdj() {
        return numAffectedAdj;
    }

    /** Setter for property numAffectedAdj.
     * @param numAffectedAdj New value of property numAffectedAdj.
     *
     */
    public void setNumAffectedAdj(int numAffectedAdj) {
        this.numAffectedAdj = numAffectedAdj;
    }

    /** Getter for property numTimesInc.
     * @return Value of property numTimesInc.
     *
     */
    public double getNumTimesInc() {
        return numTimesInc;
    }

    /** Setter for property numTimesInc.
     * @param numTimesInc New value of property numTimesInc.
     *
     */
    public void setNumTimesInc(double numTimesInc) {
        this.numTimesInc = numTimesInc;
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

}
