/*
 * MemberErrorCodes.java
 *
 * Created on May 12, 2003, 2:56 PM
 */

package org.afscme.enterprise.member;

/**
 * 
 * This classes contains error codes that can be used through out the 
 * member package, though mosly returned by the MaintainMembersAffiliateBean
 * and recorded in this class so that they can be referenced by the user 
 * interface code in the servlet container. Many of the errors defined are for
 * reporting when business rules have been violated
 *  
 * @author  gdecorte
 */

public class MemberErrorCodes {
    
    /** Creates a new instance of MemberErrorCodes */
    public MemberErrorCodes() {
    }
 
    /**
     * User tried to add from an existing Member who is Expelled.
     */
    public static final int ADD_MEMBER_EXPELLED_ERROR = -1;
    
    /**
     * User tried to set the Barred Flag for a Member who is currently holding an Office.
     */
    public static final int UPDATE_MEMBER_BARRED_ERROR = -2;
    
    /**
     * User input an affiliate identifier for which no records were found
     */
    public static final int AFFILIATE_NOT_FOUND = -3;
    
    /**
     * User input an affiliate identifier that returns duplicates
     */
    // use AffiliateErrorCodes.ERROR_GET_PK_MORE_THAN_ONE_RESULT
    
    /**
     * PersonPk passed is null and so is NewMember.theNewPerson reference 
     */
    public static final int MISSING_PERSON_DATA = -4;
    
    /**
     * Affiliate status is neither Chartered, Not Chartered, Restricted Administratorship
     * or Unrestricted Administratorship 
     */
    public static final int AFFILIATE_NO_GOOD_FOR_ADD_MEMBER = -5;
    
    /**
     * Member Type is 'Retiree" or 'Retiree Spouse' and Affiliate Type is not "R' or 'S'
     */
   public static final int MEMBER_TYPE_CONFLICT_WITH_AFF_TYPE = -6;
    
   /**
     * Person is currently barred from membership
     */
   public static final int BARRED_MEMBER = -8; 
    
   /**
     * Update of member detail failed for some reason
     */
   public static final int MEMBER_DETAIL_UPDATE_FAILED = -9; 
   
   /**
     * Update of member affiliate failed because the person in that affiliate was not found
     */
   public static final int MEMBER_AFFILIATION_NOT_FOUND = -10; 
   
   /**
     * Update of member affiliate succeeded, but multiple records for that person were found for the affiliate (there should be only one)
     */
   public static final int MULTIPLE_MEMBER_AFFILIATION_RECORDS_FOUND = -11; 
   
}
