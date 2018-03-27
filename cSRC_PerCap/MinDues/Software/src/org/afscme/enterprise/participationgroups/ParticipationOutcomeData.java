package org.afscme.enterprise.participationgroups;


/**
 * Represents a Participation Outcome for a specific Group, Type and Detail.
 */
public class ParticipationOutcomeData 
{
    protected Integer groupPk;
    protected String groupNm;
    protected Integer typePk;
    protected String typeNm; 
    protected Integer detailPk;
    protected String detailNm;
    protected int detailShortcut;
    protected Integer outcomePk;
    protected String outcomeNm;
    protected String description;

    
    /** Constructor
     *
     */
    public ParticipationOutcomeData() {
    }
    
    /** Constructor passing in name and description.
     *
     */
    public ParticipationOutcomeData(String outcomeName, String outcomeDescription) {
        outcomeNm = outcomeName;
        description = outcomeDescription;
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
        this.groupPk = groupPk;
    }
    
    /** Getter for property groupNm.
     * @return Value of property groupNm.
     *
     */
    public java.lang.String getGroupNm() {
        return groupNm;
    }
    
    /** Setter for property groupNm.
     * @param groupNm New value of property groupNm.
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
        this.typePk = typePk;
    }
    
    /** Getter for property typeNm.
     * @return Value of property typeNm.
     *
     */
    public java.lang.String getTypeNm() {
        return typeNm;
    }
    
    /** Setter for property typeNm.
     * @param typeNm New value of property typeNm.
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
        this.detailPk = detailPk;
    }
    
    /** Getter for property detailNm.
     * @return Value of property detailNm.
     *
     */
    public java.lang.String getDetailNm() {
        return detailNm;
    }
    
    /** Setter for property detailNm.
     * @param detailNm New value of property detailNm.
     *
     */
    public void setDetailNm(java.lang.String detailNm) {
        this.detailNm = detailNm;
    }
    
    /** Getter for property detailShortcut.
     * @return Value of property detailShortcut.
     *
     */
    public int getDetailShortcut() {
        return detailShortcut;
    }
    
    /** Setter for property detailShortcut.
     * @param detailShortcut New value of property detailShortcut.
     *
     */
    public void setDetailShortcut(int detailShortcut) {
        this.detailShortcut = detailShortcut;
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
        this.outcomePk = outcomePk;
    }
    
    /** Getter for property outcomeNm.
     * @return Value of property outcomeNm.
     *
     */
    public java.lang.String getOutcomeNm() {
        return outcomeNm;
    }
    
    /** Setter for property outcomeNm.
     * @param outcomeNm New value of property outcomeNm.
     *
     */
    public void setOutcomeNm(java.lang.String outcomeNm) {
        this.outcomeNm = outcomeNm;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
}
