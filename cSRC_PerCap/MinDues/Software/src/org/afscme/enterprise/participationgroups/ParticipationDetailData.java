package org.afscme.enterprise.participationgroups;


/**
 * Represents a Participation Detail for a specific Group and Type.
 */
public class ParticipationDetailData 
{
    private Integer detailPk;
    private Integer typePk;
    private Integer groupPk;
    private String name;
    private String description;
    private int shortcut;
    private boolean status;

    /** Constructor
     *
     */
    public ParticipationDetailData() {
    }

    /** Constructor passing in name and description.
     *
     */
    public ParticipationDetailData(String typeName, String typeDescription) {
        name = typeName;
        description = typeDescription;
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
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /** Getter for property shortcut.
     * @return Value of property shortcut.
     *
     */
    public int getShortcut() {
        return shortcut;
    }
    
    /** Setter for property shortcut.
     * @param shortcut New value of property shortcut.
     *
     */
    public void setShortcut(int shortcut) {
        this.shortcut = shortcut;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public boolean isStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(boolean status) {
        this.status = status;
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
    
}
