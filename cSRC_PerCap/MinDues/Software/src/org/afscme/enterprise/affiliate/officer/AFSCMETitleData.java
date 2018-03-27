package org.afscme.enterprise.affiliate.officer;


/**
 * Represents a Title Code and Name for Officer Title dropdowns.
 */
public class AFSCMETitleData 
{
    private Integer afscmeOfficePk;
    private String afscmeTitleName;
    
    public String toString() {
        return "AFSCMETitleData [" +
                "afscmeOfficePk=" + afscmeOfficePk + 
                ", afscmeTitleName=" + afscmeTitleName +
                "]"
        ;
    }
    
// Getter and Setter Methods...
    
    /** Getter for property afscmeOfficePk.
     * @return Value of property afscmeOfficePk.
     *
     */
    public Integer getAfscmeOfficePk() {
        return afscmeOfficePk;
    }
    
    /** Setter for property afscmeOfficePk.
     * @param afscmeOfficePk New value of property afscmeOfficePk.
     *
     */
    public void setAfscmeOfficePk(Integer afscmeOfficePk) {
        this.afscmeOfficePk = afscmeOfficePk;
    }
    
    /** Getter for property afscmeTitleName.
     * @return Value of property afscmeTitleName.
     *
     */
    public String getAfscmeTitleName() {
        return afscmeTitleName;
    }
    
    /** Setter for property afscmeTitleName.
     * @param afscmeTitleName New value of property afscmeTitleName.
     *
     */
    public void setAfscmeTitleName(String afscmeTitleName) {
        this.afscmeTitleName = afscmeTitleName;
    }
    
}
