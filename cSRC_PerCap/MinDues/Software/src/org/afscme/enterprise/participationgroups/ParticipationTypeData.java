package org.afscme.enterprise.participationgroups;


/**
 * Represents a Participation Type for a specific Group.
 */
public class ParticipationTypeData 
{
    private Integer typePk;
    private Integer groupPk;
    private String name;
    private String description;
    
    /** Static variable for default type for new group.
     *
     */
    public static final String TYPE_GENERAL = "General";

    
    /** Constructor
     *
     */
    public ParticipationTypeData() {
    }
        
    /** Constructor passing in name and description.
     *
     */
    public ParticipationTypeData(String typeName, String typeDescription) {
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
