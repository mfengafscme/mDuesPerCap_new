package org.afscme.enterprise.minimumdues;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents Stat Membership data.
 */
public class StatMembership {
    private int affPk;
    private int duesyr;
    private int statMbrCt;
    private int mbrshpCt;


    public StatMembership() {
        this.affPk = 0;
        this.duesyr = 0;
        this.statMbrCt = 0;
        this.mbrshpCt = 0;
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

    /** Getter for property duesyr.
     * @return Value of property duesyr.
     *
     */
    public int getDuesyr() {
        return duesyr;
    }

    /** Setter for property duesyr.
     * @param duesyr New value of property duesyr.
     *
     */
    public void setDuesyr(int duesyr) {
        this.duesyr = duesyr;
    }

    /** Getter for property statMbrCt.
     * @return Value of property statMbrCt.
     *
     */
    public int getStatMbrCt() {
        return statMbrCt;
    }

    /** Setter for property statMbrCt.
     * @param statMbrCt New value of property statMbrCt.
     *
     */
    public void setStatMbrCt(int statMbrCt) {
        this.statMbrCt = statMbrCt;
    }

    /** Getter for property mbrshpCt.
     * @return Value of property mbrshpCt.
     *
     */
    public int getMbrshpCt() {
        return mbrshpCt;
    }

    /** Setter for property mbrshpCt.
     * @param mbrshpCt New value of property mbrshpCt.
     *
     */
    public void setMbrshpCt(int mbrshpCt) {
        this.mbrshpCt = mbrshpCt;
    }

}
