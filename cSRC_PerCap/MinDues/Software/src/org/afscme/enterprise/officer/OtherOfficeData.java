package org.afscme.enterprise.officer;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * Represents info about a position held in another Affiliate.
 */
public class OtherOfficeData {
    
    private String afscmeTitleNm;
    private AffiliateIdentifier affId;
    
    /**
     * This is a String instead of a codePk because it represents either one of the 
     * Person/Member record's address, which is a common code, but it also could be 
     * 'Affiliate', which is NOT a common code.
     */
    private String officerMailTo;
    
}
