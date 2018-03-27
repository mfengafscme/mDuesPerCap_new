package org.afscme.enterprise.affiliate;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents affilaite data.  Roughly the data on the 'Affiliate Detail' screen.
 */
public class EmployerData {
    private int empAffPk;
    private int affPk;
    private String type;
    private String state;
    private int council;
    private int local;
    private String chapter;
    private String employer;
    private String status;
    private int stat;
    private int memCt;

    private int created_user_pk;
    private Timestamp created_dt;
    private int lst_mod_user_pk;
    private Timestamp lst_mod_dt;
    private ArrayList existing_year;

    public EmployerData() {
        this.empAffPk = 0;
        this.affPk = 0;
        this.type = null;
        this.state = null;
        this.council = 0;
        this.local = 0;
        this.chapter = null;
        this.empAffPk = 0;
        this.employer = null;
        this.status = null;
        this.created_user_pk = 0;
        this.created_dt = null;
        this.lst_mod_user_pk = 0;
        this.lst_mod_dt = null;
        this.existing_year = null;
    }

    /** Getter for property empAffPk.
     * @return Value of property empAffPk.
     *
     */
    public int getEmpAffPk() {
        return empAffPk;
    }

    /** Setter for property empAffPk.
     * @param empAffPk New value of property empAffPk.
     *
     */
    public void setEmpAffPk(int empAffPk) {
        this.empAffPk = empAffPk;
    }

    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public int getAffPk() {
        return affPk;
    }

    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(int affPk) {
        this.affPk = affPk;
    }

    /** Getter for property state.
     * @return Value of property state.
     *
     */
    public java.lang.String getState() {
        return state;
    }

    /** Setter for property state.
     * @param state New value of property state.
     *
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }

    /** Getter for property council.
     * @return Value of property council.
     *
     */
    public int getCouncil() {
        return council;
    }

    /** Setter for property council.
     * @param council New value of property council.
     *
     */
    public void setCouncil(int council) {
        this.council = council;
    }

    /** Getter for property local.
     * @return Value of property local.
     *
     */
    public int getLocal() {
        return local;
    }

    /** Setter for property local.
     * @param lcl New value of property local.
     *
     */
    public void setLocal(int local) {
        this.local = local;
    }

    /** Getter for property chapter.
     * @return Value of property chapter.
     *
     */
    public java.lang.String getChapter() {
        return chapter;
    }

    /** Setter for property chapter.
     * @param chapter New value of property chapter.
     *
     */
    public void setChapter(java.lang.String chapter) {
        this.chapter = chapter;
    }

    /** Getter for property employer.
     * @return Value of property employer.
     *
     */
    public java.lang.String getEmployer() {
        return employer;
    }

    /** Setter for property employer.
     * @param employer New value of property employer.
     *
     */
    public void setEmployer(java.lang.String employer) {
        this.employer = employer;
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

    /** Getter for property existing_year.
     * @return Value of property existing_year.
     *
     */
    public java.util.ArrayList getExisting_year() {
        return existing_year;
    }

    /** Setter for property existing_year.
     * @param existing_year New value of property existing_year.
     *
     */
    public void setExisting_year(java.util.ArrayList existing_year) {
        this.existing_year = existing_year;
    }

    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public java.lang.String getType() {
        return type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public java.lang.String getStatus() {
        return status;
    }

    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /** Getter for property stat.
     * @return Value of property stat.
     *
     */
    public int getStat() {
        return stat;
    }
    
    /** Setter for property stat.
     * @param stat New value of property stat.
     *
     */
    public void setStat(int stat) {
        this.stat = stat;
    }
    
    /** Getter for property memCt.
     * @return Value of property memCt.
     *
     */
    public int getMemCt() {
        return memCt;
    }
    
    /** Setter for property memCt.
     * @param memCt New value of property memCt.
     *
     */
    public void setMemCt(int memCt) {
        this.memCt = memCt;
    }
    
}
