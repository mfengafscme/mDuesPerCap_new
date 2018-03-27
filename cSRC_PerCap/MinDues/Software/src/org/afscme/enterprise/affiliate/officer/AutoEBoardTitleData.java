package org.afscme.enterprise.affiliate.officer;


/**
 * Represents an Office that is held by an Auto Eboard member from a Sub-Affiliate.
 */
public class AutoEBoardTitleData 
{
    private Integer affiliateTitlePk;
    private Integer subAffiliateTitlePk;
    
// Getter and Setter Methods...
    
    /** Getter for property affiliateTitlePk.
     * @return Value of property affiliateTitlePk.
     *
     */
    public Integer getAffiliateTitlePk() {
        return affiliateTitlePk;
    }
    
    /** Setter for property affiliateTitlePk.
     * @param affiliateTitlePk New value of property affiliateTitlePk.
     *
     */
    public void setAffiliateTitlePk(Integer affiliateTitlePk) {
        this.affiliateTitlePk = affiliateTitlePk;
    }
    
    /** Getter for property subAffiliateTitlePk.
     * @return Value of property subAffiliateTitlePk.
     *
     */
    public Integer getSubAffiliateTitlePk() {
        return subAffiliateTitlePk;
    }
    
    /** Setter for property subAffiliateTitlePk.
     * @param subAffiliateTitlePk New value of property subAffiliateTitlePk.
     *
     */
    public void setSubAffiliateTitlePk(Integer subAffiliateTitlePk) {
        this.subAffiliateTitlePk = subAffiliateTitlePk;
    }
    
}
