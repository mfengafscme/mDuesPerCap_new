package org.afscme.enterprise.officer;

import java.util.Collection;

public class LocationAddressAssociationData {
    
    private static final int ASSOCIATION_TYPE_ADDRESS = 1;
    private static final int ASSOCIATION_TYPE_LOCATION = 2;
    
    private Collection personAddresses;
    private Collection affiliateLocations;
    private Integer currentAssociation;
    
    /**
     * Indicates if the current association is a Person Address or an Affiliate 
     * Location. Uses the ASSOCIATION_TYPE_XXX constants to set this.
     */
    private int associationType;
    
    /**
     * Sets the currentAssociation with the addressPk parameter. Also sets the 
     * associationType = ASSOCIATION_TYPE_ADDRESS.
     * @param addressPk
     */
    public void setCurrentAddressAssociation(Integer addressPk) {
     
    }
    
    /**
     * Sets the currentAssociation with the locationPk parameter. Also sets the 
     * associationType = ASSOCIATION_TYPE_LOCATION.
     * @param locationPk
     */
    public void setCurrentLocationAssociation(Integer locationPk) {
     
    }
    
}
