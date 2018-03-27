package org.afscme.enterprise.member;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import java.sql.Timestamp;

/**
 * Represents an individual participation and outcome for a member
 */
public class ParticipationData 
{
    public static final int SORT_BY_GROUP = 1;
    public static final int SORT_BY_TYPE = 2;
    public static final int SORT_BY_DETAIL = 3;
    public static final int SORT_BY_OUTCOME = 4;
    public static final int SORT_BY_DATE = 5;  
    
    protected Integer personPk;
    protected Integer participDetailPk;
    protected Timestamp mbrParticipDt;
    protected RecordData theRecordData;
    protected CommentData theCommentData;
    protected ParticipationOutcomeData theParticipationOutcomeData;
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public java.lang.Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(java.lang.Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property participDetailPk.
     * @return Value of property participDetailPk.
     *
     */
    public java.lang.Integer getParticipDetailPk() {
        return participDetailPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setParticipDetailPk(java.lang.Integer participDetailPk) {
        this.participDetailPk = participDetailPk;
    }    
    
    /** Getter for property mbrParticipDt.
     * @return Value of property mbrParticipDt.
     *
     */
    public java.sql.Timestamp getMbrParticipDt() {
        return mbrParticipDt;
    }
    
    /** Setter for property mbrParticipDt.
     * @param mbrParticipDt New value of property mbrParticipDt.
     *
     */
    public void setMbrParticipDt(java.sql.Timestamp mbrParticipDt) {
        this.mbrParticipDt = mbrParticipDt;
    }
    
    /** Getter for property theRecordData.
     * @return Value of property theRecordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getTheRecordData() {
        return theRecordData;
    }
    
    /** Setter for property theRecordData.
     * @param theRecordData New value of property theRecordData.
     *
     */
    public void setTheRecordData(org.afscme.enterprise.common.RecordData theRecordData) {
        this.theRecordData = theRecordData;
    }
    
    /** Getter for property theCommentData.
     * @return Value of property theCommentData.
     *
     */
    public org.afscme.enterprise.common.CommentData getTheCommentData() {
        return theCommentData;
    }
    
    /** Setter for property theCommentData.
     * @param theCommentData New value of property theCommentData.
     *
     */
    public void setTheCommentData(org.afscme.enterprise.common.CommentData theCommentData) {
        this.theCommentData = theCommentData;
    }
    
    /** Getter for property theParticipationOutcomeData.
     * @return Value of property theParticipationOutcomeData.
     *
     */
    public org.afscme.enterprise.participationgroups.ParticipationOutcomeData getTheParticipationOutcomeData() {
        return theParticipationOutcomeData;
    }
    
    /** Setter for property theParticipationOutcomeData.
     * @param theParticipationOutcomeData New value of property theParticipationOutcomeData.
     *
     */
    public void setTheParticipationOutcomeData(org.afscme.enterprise.participationgroups.ParticipationOutcomeData theParticipationOutcomeData) {
        this.theParticipationOutcomeData = theParticipationOutcomeData;
    }
    
}
