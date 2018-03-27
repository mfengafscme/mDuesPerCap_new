package org.afscme.enterprise.organization;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.person.PersonData;


/**
 * Contains data that represents data about an Organization Associate, which 
 * includes PersonData (i.e. person oriented data), organization specific data and 
 * data related to the association (orgPosTitle)
 */
public class OrganizationAssociateData 
{
    private String orgName;
    private Integer orgPositionTitle;
    private String latestCommentText;
    private RecordData recordData;    

    private Integer locationPk;
    
    private LocationData locationData;
    private PersonData personData;
    
    
    public OrganizationAssociateData() {
        personData = new PersonData();
    }

    /** toString method to view all attributes.
     * @return Value of all properties.
     *
     */
    public String toString() {
        return "OrganizationAssociateData[" +
        "orgName="+orgName+", "+
        "orgPositionTitle="+orgPositionTitle+", "+
        "latestCommentText="+latestCommentText+", "+
        "recordData="+recordData+", "+
        "locationPk="+locationPk+", "+
        "locationData="+locationData+", "+
        "personData="+personData+"]";
    }
        
    /** Getter for property latestCommentText.
     * @return Value of property latestCommentText.
     *
     */
    public java.lang.String getLatestCommentText() {
        return latestCommentText;
    }
    
    /** Setter for property latestCommentText.
     * @param latestCommentText New value of property latestCommentText.
     *
     */
    public void setLatestCommentText(java.lang.String latestCommentText) {
        this.latestCommentText = latestCommentText;
    }
    
    /** Getter for property orgName.
     * @return Value of property orgName.
     *
     */
    public java.lang.String getOrgName() {
        return orgName;
    }
    
    /** Setter for property orgName.
     * @param orgName New value of property orgName.
     *
     */
    public void setOrgName(java.lang.String orgName) {
        this.orgName = orgName;
    }
    
    /** Getter for property orgPositionTitle.
     * @return Value of property orgPositionTitle.
     *
     */
    public java.lang.Integer getOrgPositionTitle() {
        return orgPositionTitle;
    }
    
    /** Setter for property orgPositionTitle.
     * @param orgPositionTitle New value of property orgPositionTitle.
     *
     */
    public void setOrgPositionTitle(java.lang.Integer orgPositionTitle) {
        this.orgPositionTitle = orgPositionTitle;
    }
    
    /** Getter for property locationData.
     * @return Value of property locationData.
     *
     */
    public org.afscme.enterprise.organization.LocationData getLocationData() {
        return locationData;
    }
    
    /** Setter for property locationData.
     * @param locationData New value of property locationData.
     *
     */
    public void setLocationData(org.afscme.enterprise.organization.LocationData locationData) {
        this.locationData = locationData;
    }
    
    /** Getter for property personData.
     * @return Value of property personData.
     *
     */
    public org.afscme.enterprise.person.PersonData getPersonData() {
        return personData;
    }
    
    /** Setter for property personData.
     * @param personData New value of property personData.
     *
     */
    public void setPersonData(org.afscme.enterprise.person.PersonData personData) {
        this.personData = personData;
    }
    
    /** Getter for property locationPk.
     * @return Value of property locationPk.
     *
     */
    public java.lang.Integer getLocationPk() {
        return locationPk;
    }
    
    /** Setter for property locationPk.
     * @param locationPk New value of property locationPk.
     *
     */
    public void setLocationPk(java.lang.Integer locationPk) {
        this.locationPk = locationPk;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public org.afscme.enterprise.common.RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(org.afscme.enterprise.common.RecordData recordData) {
        this.recordData = recordData;
    }
    
}
