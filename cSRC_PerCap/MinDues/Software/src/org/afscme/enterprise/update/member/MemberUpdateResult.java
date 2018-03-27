package org.afscme.enterprise.update.member;

import org.afscme.enterprise.affiliate.AffiliateIdentifier;

/**
 * Holds all the information about updates to a single affiliate in a review 
 * summary
 */
public class MemberUpdateResult {
    
    protected AffiliateIdentifier ffiliateIdentifier;
    protected MemberChanges memberChanges;
    
    /** Getter for property ffiliateIdentifier.
     * @return Value of property ffiliateIdentifier.
     *
     */
    public AffiliateIdentifier getFfiliateIdentifier() {
        return ffiliateIdentifier;
    }
    
    /** Setter for property ffiliateIdentifier.
     * @param ffiliateIdentifier New value of property ffiliateIdentifier.
     *
     */
    public void setFfiliateIdentifier(AffiliateIdentifier ffiliateIdentifier) {
        this.ffiliateIdentifier = ffiliateIdentifier;
    }
    
    /** Getter for property memberChanges.
     * @return Value of property memberChanges.
     *
     */
    public MemberChanges getMemberChanges() {
        return memberChanges;
    }
    
    /** Setter for property memberChanges.
     * @param memberChanges New value of property memberChanges.
     *
     */
    public void setMemberChanges(MemberChanges memberChanges) {
        this.memberChanges = memberChanges;
    }
    
}
