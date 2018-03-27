package org.afscme.enterprise.person;

/**
 * Holds the values of some predefined codes that are needed
 * to implement business rules in Person code.
 */
public interface Persona {

    /** Member Persona */
    public static final String MEMBER = "Member";

    /** Affiliate Staff Persona */
    public static final String AFFILIATE_STAFF = "Affiliate Staff";

    /** AFSCME Staff Persona */
    public static final String AFSCME_STAFF = "AFSCME Staff";
    
    /** Organization Associate Persona */
    public static final String ORGANIZATION_ASSOCIATE = "Organization Associate";
    
    /** Vendor Persona */
    public static final String VENDOR = "Vendor";
    
}
