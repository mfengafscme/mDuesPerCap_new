package org.afscme.enterprise.member.web;

// Afscme imports
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.ParticipationData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/** Holds the data on the Member Participation Search screen
 * @struts:form name="participationDetailForm"
 */

public class ParticipationDetailForm extends org.apache.struts.action.ActionForm {
    
    private static Logger logger =  Logger.getLogger(ParticipationDetailForm.class);     
    
    private Integer detailShortcut;    
    private Integer groupPk;
    private String groupNm;
    private Integer typePk;
    private String typeNm;
    private Integer detailPk;
    private String detailNm;
    private Integer outcomePk;
    private String outcomeNm;
    private String comments;
    private String participationDate;
    private String lastUpdatedDate;
    private Integer lastUpdatedBy;
    
    private Integer oldDetailPk;
    private Integer oldDetailShortcut;
    
    /** Creates a new instance of ParticipationDetailForm */
    public ParticipationDetailForm() {
    }    
    
    /**
     * toString method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getClass().getName() + "[");
        buf.append("detailShortcut: " + detailShortcut);
        buf.append(" groupPk: " + groupPk);
        buf.append(" groupNm: " + groupNm);        
        buf.append(" typePk: " + typePk);
        buf.append(" typeNm: " + typeNm);          
        buf.append(" detailPk: " + detailPk);
        buf.append(" detailNm: " + detailNm);
        buf.append(" outcomePk: " + outcomePk);
        buf.append(" outcomeNm: " + outcomeNm);        
        buf.append(" comments: " + comments);
        buf.append(" participationDate: " + participationDate);
        buf.append(" lastUpdatedDate: " + lastUpdatedDate); 
        buf.append(" lastUpdatedBy: " + lastUpdatedBy);         
        return buf.toString()+"]";
    }
    
    
   /**
     * validate method
     */    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if (getGroupPk() == null) {
            errors.add("groupPk", new ActionError("error.field.required.generic", "Group"));
        }
        if (getTypePk() == null) {
            errors.add("typePk", new ActionError("error.field.required.generic", "Type"));
        }
        if (getDetailPk() == null) {
            errors.add("detailPk", new ActionError("error.field.required.generic", "Detail"));
        }
        if (getOutcomePk() == null) {
            errors.add("outcomePk", new ActionError("error.field.required.generic", "Outcome"));
        }
        if (TextUtil.isEmptyOrSpaces(getParticipationDate())) {
            errors.add("participationDate", new ActionError("error.field.required.generic", "Date"));
        }        

        logger.debug("    ---- returning " + errors.size() + " error(s).");
        return errors;
    }
    
    
   /**
     * creates and populates participation data object from current form values
     */    
    public ParticipationData getParticipationData(Integer userPk, Integer personPk) {
        ParticipationData pd = new ParticipationData();
        CommentData cd = new CommentData();
        ParticipationOutcomeData pod = new ParticipationOutcomeData();
        RecordData rd = new RecordData();
        pd.setPersonPk(userPk);
        pd.setMbrParticipDt(DateUtil.getTimestamp(participationDate));
        
        rd.setCreatedBy(personPk);
 
        cd.setComment(this.comments);
        
        pod.setOutcomePk(this.outcomePk);
        pod.setDetailPk(this.detailPk);
        
        pd.setTheCommentData(cd);        
        pd.setTheParticipationOutcomeData(pod);
        pd.setTheRecordData(rd);        
        return pd;
    }
    
    
   /**
     * takes a ParticipationData object and popoulates the form.
     */    
    public void setAll(ParticipationData pd) {
      
        CommentData cd = pd.getTheCommentData();
        ParticipationOutcomeData pod = pd.getTheParticipationOutcomeData();
        RecordData rd = pd.getTheRecordData();
        
        setDetailShortcut(new Integer(pod.getDetailShortcut()));
        setGroupPk(pod.getGroupPk());
        setGroupNm(pod.getGroupNm());
        setTypePk(pod.getTypePk());
        setTypeNm(pod.getTypeNm());
        setDetailPk(pod.getDetailPk());
        setOldDetailPk(pod.getDetailPk());
        setDetailNm(pod.getDetailNm());
        setOutcomePk(pod.getOutcomePk());
        setOutcomeNm(pod.getOutcomeNm());
        setComments(cd.getComment());
        setParticipationDate(DateUtil.getSimpleDateString(pd.getMbrParticipDt()));
        setLastUpdatedDate(DateUtil.getSimpleDateString(rd.getModifiedDate()));
        setLastUpdatedBy(rd.getModifiedBy());
    }    
    
    
    /** Getter for property detailShortcut.
     * @return Value of property detailShortcut.
     *
     */
    public Integer getDetailShortcut() {
        return detailShortcut;
    }
    
    /** Setter for property detailShortcut.
     * @param detailShortcut New value of property detailShortcut.
     *
     */
    public void setDetailShortcut(Integer detailShortcut) {        
        if (detailShortcut != null && detailShortcut.intValue() == 0) this.detailShortcut = null;
            else this.detailShortcut = detailShortcut;        
    }    
    
    /** Getter for property groupPk.
     * @return Value of property groupPk.
     *
     */
    public java.lang.Integer getGroupPk() {
        return groupPk;
    }
    
    /** Setter for property groupPk.
     * @param groupPk New value of property groupPk.
     *
     */
    public void setGroupPk(java.lang.Integer groupPk) {
        if (groupPk != null && groupPk.intValue() == 0) this.groupPk = null;
            else this.groupPk = groupPk;   
    }
    
    /** Getter for property groupNm.
     * @return Value of property groupNm.
     *
     */
    public java.lang.String getGroupNm() {
        return groupNm;
    }
    
    /** Setter for property groupNm.
     * @param comments New value of property groupNm.
     *
     */
    public void setGroupNm(java.lang.String groupNm) {
        this.groupNm = groupNm;
    }    
    
    /** Getter for property typePk.
     * @return Value of property typePk.
     *
     */
    public java.lang.Integer getTypePk() {
        return typePk;
    }
    
    /** Setter for property typePk.
     * @param typePk New value of property typePk.
     *
     */
    public void setTypePk(java.lang.Integer typePk) {
        if (typePk != null && typePk.intValue() == 0) this.typePk = null;
            else this.typePk = typePk;
    }
    
    /** Getter for property typeNm.
     * @return Value of property typeNm.
     *
     */
    public java.lang.String getTypeNm() {
        return typeNm;
    }
    
    /** Setter for property typeNm.
     * @param comments New value of property typeNm.
     *
     */
    public void setTypeNm(java.lang.String typeNm) {
        this.typeNm = typeNm;
    }      
    
    /** Getter for property detailPk.
     * @return Value of property detailPk.
     *
     */
    public java.lang.Integer getDetailPk() {
        return detailPk;
    }
    
    /** Setter for property detailPk.
     * @param detailPk New value of property detailPk.
     *
     */
    public void setDetailPk(java.lang.Integer detailPk) {
        if (detailPk != null && detailPk.intValue() == 0) this.detailPk = null;
            else this.detailPk = detailPk;
    }
    
   /** Getter for property detailNm.
     * @return Value of property detailNm.
     *
     */
    public java.lang.String getDetailNm() {
        return detailNm;
    }
    
    /** Setter for property detailNm.
     * @param comments New value of property detailNm.
     *
     */
    public void setDetailNm(java.lang.String detailNm) {
        this.detailNm = detailNm;
    }    
    
    /** Getter for property outcomePk.
     * @return Value of property outcomePk.
     *
     */
    public java.lang.Integer getOutcomePk() {
        return outcomePk;
    }
    
    /** Setter for property outcomePk.
     * @param outcomePk New value of property outcomePk.
     *
     */
    public void setOutcomePk(java.lang.Integer outcomePk) {
        if (outcomePk != null && outcomePk.intValue() == 0) this.outcomePk = null;
            else this.outcomePk = outcomePk;
    }
    
   /** Getter for property outcomeNm.
     * @return Value of property outcomeNm.
     *
     */
    public java.lang.String getOutcomeNm() {
        return outcomeNm;
    }
    
    /** Setter for property outcomeNm.
     * @param comments New value of property outcomeNm.
     *
     */
    public void setOutcomeNm(java.lang.String outcomeNm) {
        this.outcomeNm = outcomeNm;
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
        this.comments = comments;
    }
    
    /** Getter for property participationDate.
     * @return Value of property participationDate.
     *
     */
    public String getParticipationDate() {
        return participationDate;
    }
    
    /** Setter for property participationDate.
     * @param participationDate New value of property participationDate.
     *
     */
    public void setParticipationDate(String participationDate) { 
        this.participationDate = participationDate;
    }    
    
    /** Getter for property lastUpdatedDate.
     * @return Value of property lastUpdatedDate.
     *
     */
    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }
    
    /** Setter for property lastUpdatedDate.
     * @param lastUpdatedDate New value of property lastUpdatedDate.
     *
     */
    public void setLastUpdatedDate(String lastUpdatedDate) { 
        this.lastUpdatedDate = lastUpdatedDate;
    }        
    
    /** Getter for property lastUpdatedBy.
     * @return Value of property lastUpdatedBy.
     *
     */
    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    
    /** Setter for property lastUpdatedBy.
     * @param lastUpdatedBy New value of property lastUpdatedBy.
     *
     */
    public void setLastUpdatedBy(Integer lastUpdatedBy) { 
        this.lastUpdatedBy = lastUpdatedBy;
    }           
    
    /** Getter for property detailPk.
     * @return Value of property detailPk.
     *
     */
    public java.lang.Integer getOldDetailPk() {
        return oldDetailPk;
    }
    
    /** Setter for property detailPk.
     * @param detailPk New value of property detailPk.
     *
     */
    public void setOldDetailPk(java.lang.Integer oldDetailPk) {
        if (oldDetailPk != null && oldDetailPk.intValue() == 0) this.oldDetailPk = null;
            else this.oldDetailPk = oldDetailPk;
    }    
    
    /** Getter for property oldDetailShortcut.
     * @return Value of property oldDetailShortcut.
     *
     */
    public Integer getOldDetailShortcut() {
        return oldDetailShortcut;
    }
    
    /** Setter for property oldDetailShortcut.
     * @param detailShortcut New value of property oldDetailShortcut.
     *
     */
    public void setOldDetailShortcut(Integer oldDetailShortcut) {        
        if (oldDetailShortcut != null && oldDetailShortcut.intValue() == 0) this.oldDetailShortcut = null;
            else this.oldDetailShortcut = oldDetailShortcut;        
    }        
}

