package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;

/**
 * Represents the result of an Affiliate Change search.
 */
public class AffiliateChangeResult {
    private Integer affiliatePk;
    private Integer sectionCodePk;
    private Timestamp changedDate;
    private Map attributes;
    
    public AffiliateChangeResult() {
        this.affiliatePk = null;
        this.sectionCodePk = null;
        this.changedDate = null;
        this.attributes = null;
    }
    
// General Methods...
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append("affiliatePk = ");
        sb.append(this.affiliatePk);
        sb.append(", sectionCodePk = ");
        sb.append(this.sectionCodePk);
        sb.append(", changedDate = ");
        sb.append(DateUtil.getSimpleDateString(this.changedDate));
        sb.append("]");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property affiliatePk.
     * @return Value of property affiliatePk.
     *
     */
    public Integer getAffiliatePk() {
        return affiliatePk;
    }
    
    /** Setter for property affiliatePk.
     * @param affiliatePk New value of property affiliatePk.
     *
     */
    public void setAffiliatePk(Integer affiliatePk) {
        this.affiliatePk = affiliatePk;
    }
    
    /** Getter for property changedDate.
     * @return Value of property changedDate.
     *
     */
    public Timestamp getChangedDate() {
        return changedDate;
    }
    
    /** Setter for property changedDate.
     * @param changedDate New value of property changedDate.
     *
     */
    public void setChangedDate(Timestamp changedDate) {
        this.changedDate = changedDate;
    }
    
    /** Getter for property sectionCodePk.
     * @return Value of property sectionCodePk.
     *
     */
    public Integer getSectionCodePk() {
        return sectionCodePk;
    }
    
    /** Setter for property sectionCodePk.
     * @param sectionCodePk New value of property sectionCodePk.
     *
     */
    public void setSectionCodePk(Integer sectionCodePk) {
        this.sectionCodePk = sectionCodePk;
    }
    
    /** Getter for property attributes.
     * @return Value of property attributes.
     *
     */
    public Map getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap();
            this.attributes.put("affPk", this.getAffiliatePk().toString());
            this.attributes.put("section", this.sectionCodePk.toString());
            this.attributes.put("changedDate", DateUtil.getSimpleDateString(this.changedDate));
        }
        return this.attributes;
    }
    
}
