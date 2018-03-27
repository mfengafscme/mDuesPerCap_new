package org.afscme.enterprise.person;


/**
 * Data about an individual mailing list assigned to a person.
 */
public class PersonMailingListData 
{
    private Integer mlbpMailingListPk;
    private Integer addressPk;
    
    /** Getter for property addressPk.
     * @return Value of property addressPk.
     *
     */
    public java.lang.Integer getAddressPk() {
        return addressPk;
    }
    
    /** Setter for property addressPk.
     * @param addressPk New value of property addressPk.
     *
     */
    public void setAddressPk(java.lang.Integer addressPk) {
        this.addressPk = addressPk;
    }
    
    /** Getter for property mlbpMailingListPk.
     * @return Value of property mlbpMailingListPk.
     *
     */
    public java.lang.Integer getMlbpMailingListPk() {
        return mlbpMailingListPk;
    }
    
    /** Setter for property mlbpMailingListPk.
     * @param mlbpMailingListPk New value of property mlbpMailingListPk.
     *
     */
    public void setMlbpMailingListPk(java.lang.Integer mlbpMailingListPk) {
        this.mlbpMailingListPk = mlbpMailingListPk;
    }
    
}
