package org.afscme.enterprise.update;

/**
 * Contains error codes that originate from the Apply Update Component.
 */
public interface UpdateErrorCodes {

    /**
     * Indicates that the entire Update File has an IO Error.
     */
    public static final int ERROR_FILE_IO = -1;
    
    /**
     * Indicates that an Update File has a file length that is less than the 
     * minimum configurable value and is invalid.
     */
    public static final int ERROR_FILE_LENGTH_LESS_THAN_CONFIG_VALUE = -2;

    /**
     * Indicates that an Update File has a file length that is not processable
     * and is invalid.  File length error for Member Update file.
     */
    public static final int ERROR_FILE_LENGTH_INVALID_MEMBER = -3;

    /**
     * Indicates that the entire Update File is missing the Affiliate Number.
     */
    public static final int ERROR_FILE_MISSING_AFFILIATE_NUMBER = -4;

    /**
     * Indicates that the entire Update File is missing the Affiliate Member Number.
     */
    public static final int ERROR_FILE_MISSING_AFFILIATE_MEMBER_NUMBER = -5;

    /**
     * Indicates that the entire Update File has zip code as all zeroes or blanks.
     */
    public static final int ERROR_FILE_ZIP_ZERO_OR_BLANK = -6;


    /**
     * Indicates that an Update File has a file length that is not processable
     * and is invalid.  File length error for Rebate Update file.
     */
    public static final int ERROR_FILE_LENGTH_INVALID_REBATE = -7;

    /**
     * Indicates that the entire Update File is missing the Afscme Member Number.
     */
    public static final int ERROR_FILE_MISSING_AFSCME_MEMBER_NUMBER = -8;

    /**
     * Indicates that the entire Update File is missing or has invalid Acceptance Code.
     */
    public static final int ERROR_FILE_INVALID_ACCEPTANCE_CODE = -9;

   
    /**
     * Error Message for Update File with an IO Error.
     */
    public static final String COMMENT_ERROR_FILE_IO = "Update File Error - File I/O Error ";
    
    /**
     * Error Message for Update File with less than configurable value.
     */
    public static final String COMMENT_ERROR_FILE_LENGTH_LESS_THAN_CONFIG_VALUE = "Update File Length is Less than Configurable Length Value ";

    /**
     * Error Message for Update File with an invalid file length.  File length error for Member Update file.
     */
    public static final String COMMENT_ERROR_FILE_LENGTH_INVALID_MEMBER = "Update File Length has Invalid Length (must be 200 character length) ";

    /**
     * Error Message for entire Update File with missing Affiliate Number.
     */
    public static final String COMMENT_ERROR_FILE_MISSING_AFFILIATE_NUMBER = "Update File has all Affiliate Numbers Missing ";

    /**
     * Error Message for entire Update File with missing Affiliate Member Number.
     */
    public static final String COMMENT_ERROR_FILE_MISSING_AFFILIATE_MEMBER_NUMBER = "Update File has all Affiliate Member Numbers Missing ";

    /**
     * Error Message for entire Update File with zip codes as all zeroes or blanks.
     */
    public static final String COMMENT_ERROR_FILE_ZIP_ZERO_OR_BLANK = "Update File has all Zip Codes with Zeroes or Blanks ";

    /**
     * Error Message for Update File with an invalid file length.  File length error for Rebate Update file.
     */
    public static final String COMMENT_ERROR_FILE_LENGTH_INVALID_REBATE = "Update File Length has Invalid Length (must be 284 character length) ";

    /**
     * Error Message for entire Update File with missing Afscme Member Number.  File error for Rebate Update file.
     */
    public static final String COMMENT_ERROR_FILE_MISSING_AFSCME_MEMBER_NUMBER = "Update File has all AFSCME Member Numbers Missing ";

    /**
     * Error Message for entire Update File with missing or invalid Acceptance Code.  File error for Rebate Update file.
     */
    public static final String COMMENT_ERROR_FILE_INVALID_ACCEPTANCE_CODE = "Update File has all Acceptance Codes Missing or Invalid ";
    
}
