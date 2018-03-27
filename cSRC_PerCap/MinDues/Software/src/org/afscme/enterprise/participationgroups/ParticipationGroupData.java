package org.afscme.enterprise.participationgroups;


/**
 * Represents a Participation Group.
 */
public class ParticipationGroupData {
    
    public static final int PK_GROUP = 0;
    public static final int PK_TYPE = 1;
    public static final int PK_DETAIL = 2;
    public static final int PK_OUTCOME = 3;
        
    private Integer groupPk;
    private String name;
    private String description;


    /** Constructor
     *
     */
    public ParticipationGroupData() {
    }
    
    /** Constructor passing in name and description.
     *
     */
    public ParticipationGroupData(String groupName, String groupDescription) {
        name = groupName;
        description = groupDescription;
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
    
}
