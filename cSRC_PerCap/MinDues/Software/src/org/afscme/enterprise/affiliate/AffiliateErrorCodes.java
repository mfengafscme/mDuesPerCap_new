package org.afscme.enterprise.affiliate;

/**
 * Contains error codes that originate from the Affiliate Component.
 */
public interface AffiliateErrorCodes {
    
    /** 
     * Indicates that an Affiliate Identifier's sequence no longer has any 
     * values making the Affiliate Identifier unusable.
     */
    public static final int ERROR_ADD_AFFILIATE_SEQUENCE_FULL = -1;
    
    /** 
     * Indicates that an attempted add action was not completed. 
     */
    public static final int ERROR_ADD_UNSUCCESSFUL = -2;
    
    /** 
     * Indicates that more than one result was found. 
     */
    public static final int ERROR_GET_PK_MORE_THAN_ONE_RESULT = -3;
    
    /** 
     * Indicates that a Sub Local was being added without belonging to a Local. 
     *
     * This probably will be caught at validation time.
     */
    public static final int ERROR_ADDING_SUB_LOCAL_WITH_NO_PARENT = -4;
    
    /** 
     * Indicates that a Sub Local was being added to a Local that is not allowed 
     * to have Sub Locals.
     */
    public static final int ERROR_ADDING_SUB_LOCAL_TO_LOCAL_NOT_ALLOWED = -5;
    
    /**
     * Indicates that a Council or a Retiree Chapter is being Affiliated with a 
     * Council that is not an Administrative/Legislative Council.
     */
    public static final int ERROR_NEW_COUNCIL_AFFILIATION_NOT_ADMIN_COUNCIL = -6;
    
    /**
     * Indicates that the user was attempting to add a Local to an Affiliate that 
     * was not a District Council.
     */
    public static final int ERROR_NEW_COUNCIL_AFFILIATION_LOCAL_TO_DIST_COUNCIL = -7;
    
    /**
     * Indicates that the user was attempting to add a Sub Chapter to an Affiliate 
     * that was not a Retiree Chapter.
     */
    public static final int ERROR_NEW_COUNCIL_AFFILIATION_SUB_CHAP_TO_RET_CHAP = -8;
    
    /**
     * Indicates that the Affiliates parent in the hierarchy could not be updated.
     */
    public static final int ERROR_PARENT_AFFILIATE_NOT_UPDATED = -9;
        
}
