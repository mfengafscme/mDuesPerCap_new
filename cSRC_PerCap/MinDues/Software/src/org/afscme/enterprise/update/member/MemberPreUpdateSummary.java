package org.afscme.enterprise.update.member;

import java.util.Map;
import java.util.HashMap;
import org.afscme.enterprise.update.PreUpdateSummary;
import org.afscme.enterprise.update.Codes;
import org.afscme.enterprise.update.FieldChange;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents the data of an individual the member pre update summary.
 */
public class MemberPreUpdateSummary extends PreUpdateSummary {
    /**
     * Per affiliate statistics.
     * key -- AffiliateIdentifier, value -- MemberChanges
     */
    // Note we did not use affPK as the key since some inFile affiliate may not have affPk (error).
    protected Map memberCounts;
    
    /**
     * the total statistics.
     * Note, this is not calculated and stored into the database when the backgroud process
     * is run. It is only used (calculated and displayed) for display purpose when the
     * user is reviewing the pre-process result.
     */
    protected MemberChanges totalCounts;
    
    /**
     * A map to keep track of aggregated field value changes for all matched members of all affiliates.
     *
     * key -- field name(Common Code -- Integer), value -- FieldChange
     */
    protected Map fieldChanges;
    
    
    public MemberPreUpdateSummary() {
        super();
        memberCounts = new HashMap();
        totalCounts = new MemberChanges();
        fieldChanges = new HashMap();
        exceptions = new HashMap();
    }
    
    public void prepopulateFieldChanges() {
        if (fieldChanges == null || fieldChanges.size() > 0) {
            fieldChanges = new HashMap();
        }
        fieldChanges.put(Codes.MemberUpdateFields.PREFIX, new FieldChange(MemberUpdateElement.PREFIX));
        fieldChanges.put(Codes.MemberUpdateFields.FIRST_NAME, new FieldChange(MemberUpdateElement.FIRST_NAME));
        fieldChanges.put(Codes.MemberUpdateFields.MIDDLE_NAME, new FieldChange(MemberUpdateElement.MIDDLE_NAME));
        fieldChanges.put(Codes.MemberUpdateFields.MIDDLE_NAME, new FieldChange(MemberUpdateElement.MIDDLE_NAME));
        fieldChanges.put(Codes.MemberUpdateFields.MIDDLE_NAME, new FieldChange(MemberUpdateElement.MIDDLE_NAME));
        fieldChanges.put(Codes.MemberUpdateFields.LAST_NAME, new FieldChange(MemberUpdateElement.LAST_NAME));
        fieldChanges.put(Codes.MemberUpdateFields.SUFFIX, new FieldChange(MemberUpdateElement.SUFFIX));
        fieldChanges.put(Codes.MemberUpdateFields.ADDR1, new FieldChange(MemberUpdateElement.ADDR1));
        fieldChanges.put(Codes.MemberUpdateFields.ADDR2, new FieldChange(MemberUpdateElement.ADDR2));
        fieldChanges.put(Codes.MemberUpdateFields.CITY, new FieldChange(MemberUpdateElement.CITY));
        fieldChanges.put(Codes.MemberUpdateFields.STATE, new FieldChange(MemberUpdateElement.STATE));
        fieldChanges.put(Codes.MemberUpdateFields.ZIP, new FieldChange(MemberUpdateElement.ZIP));
        fieldChanges.put(Codes.MemberUpdateFields.MAILABLE_ADDRESS, new FieldChange(MemberUpdateElement.MAILABLE_ADDRESS));
        fieldChanges.put(Codes.MemberUpdateFields.NO_MAIL, new FieldChange(MemberUpdateElement.NO_MAIL));
        fieldChanges.put(Codes.MemberUpdateFields.PHONE, new FieldChange(MemberUpdateElement.PHONE));
        fieldChanges.put(Codes.MemberUpdateFields.STATUS, new FieldChange(MemberUpdateElement.STATUS));
        fieldChanges.put(Codes.MemberUpdateFields.GENDER, new FieldChange(MemberUpdateElement.GENDER));
        fieldChanges.put(Codes.MemberUpdateFields.DATE_JOINED, new FieldChange(MemberUpdateElement.DATE_JOINED));
        fieldChanges.put(Codes.MemberUpdateFields.REGISTERED_VOTER, new FieldChange(MemberUpdateElement.REGISTERED_VOTER));
        fieldChanges.put(Codes.MemberUpdateFields.POLITICAL_PARTY, new FieldChange(MemberUpdateElement.POLITICAL_PARTY));
        fieldChanges.put(Codes.MemberUpdateFields.SSN, new FieldChange(MemberUpdateElement.SSN));
        fieldChanges.put(Codes.MemberUpdateFields.INFORMATION_SOURCE, new FieldChange(MemberUpdateElement.INFORMATION_SOURCE));
        fieldChanges.put(Codes.MemberUpdateFields.AFFILIATE_MEMBER_ID, new FieldChange(MemberUpdateElement.AFFILIATE_MEMBER_ID));
    }
    
    public String toString() {
        return "MemberPreUpdateSummary [" +
            "\n\t memberCounts=" + CollectionUtil.toString(memberCounts) +
            "\n\t fieldChanges=" + CollectionUtil.toString(fieldChanges) +
            "\n\t exceptions=" + CollectionUtil.toString(exceptions) +
            "\n\t totalCounts=" + totalCounts +
            "\n]"
        ;
    }
    
    /** Getter for property memberCounts.
     * @return Value of property memberCounts.
     *
     */
    public Map getMemberCounts() {
        return memberCounts;
    }
    
    /** Setter for property memberCounts.
     * @param memberCounts New value of property memberCounts.
     *
     */
    public void setMemberCounts(Map memberCounts) {
        this.memberCounts = memberCounts;
    }
    
    /** Getter for property totalCounts.
     * @return Value of property totalCounts.
     *
     */
    public MemberChanges getTotalCounts() {
        return totalCounts;
    }
    
    /** Setter for property totalCounts.
     * @param totalCounts New value of property totalCounts.
     *
     */
    public void setTotalCounts(MemberChanges totalCounts) {
        this.totalCounts = totalCounts;
    }
    
    /** Getter for property fieldChanges.
     * @return Value of property fieldChanges.
     *
     */
    public Map getFieldChanges() {
        return fieldChanges;
    }
    
    /** Setter for property fieldChanges.
     * @param fieldChanges New value of property fieldChanges.
     *
     */
    public void setFieldChanges(Map fieldChanges) {
        this.fieldChanges = fieldChanges;
    }
    
}
