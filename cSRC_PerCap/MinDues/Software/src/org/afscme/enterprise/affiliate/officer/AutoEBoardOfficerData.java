package org.afscme.enterprise.affiliate.officer;

import org.afscme.enterprise.officer.OfficerData;

/**
 * Represents an Officer Holding an Auto Eboard position.
 */
public class AutoEBoardOfficerData extends OfficerData 
{
    private AutoEBoardTitleData theAutoEBoardTitleData;
    
// Getter and Setter Methods...
    
    /** Getter for property theAutoEBoardTitleData.
     * @return Value of property theAutoEBoardTitleData.
     *
     */
    public AutoEBoardTitleData getTheAutoEBoardTitleData() {
        return theAutoEBoardTitleData;
    }
    
    /** Setter for property theAutoEBoardTitleData.
     * @param theAutoEBoardTitleData New value of property theAutoEBoardTitleData.
     *
     */ 
    public void setTheAutoEBoardTitleData(AutoEBoardTitleData theAutoEBoardTitleData) {
        this.theAutoEBoardTitleData = theAutoEBoardTitleData;
    }
    
}
