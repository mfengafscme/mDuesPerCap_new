package org.afscme.enterprise.rebate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Iterator;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.person.PRBAffiliateData;


/** Holds the data for the Political Rebate Summary
 */
public class PRBSummary
{
    private int pk;
    private Integer rbtYear;
    private String rbtRequestDate;
    private String rbtMailedDate;    
    private String rbtStatus;
    private AffiliateIdentifier affiliateIdentifier;
    private List prbAffiliateData;

    public void setFirstAffiliateIdentifier() {
        if (prbAffiliateData!=null && prbAffiliateData.size()>0) {
            Iterator itr = prbAffiliateData.iterator();
            if (itr.hasNext()) {
                PRBAffiliateData data = (PRBAffiliateData) itr.next();
                setAffiliateIdentifier(data.getTheAffiliateIdentifier());
                this.prbAffiliateData.remove(0);
            }
        }    
    }
        
    /** Getter for property rbtStatus.
     * @return Value of property rbtStatus.
     *
     */
    public java.lang.String getRbtStatus() {
        return rbtStatus;
    }
    
    /** Setter for property rbtStatus.
     * @param rbtStatus New value of property rbtStatus.
     *
     */
    public void setRbtStatus(java.lang.String rbtStatus) {
        this.rbtStatus = rbtStatus;
    }
    
    /** Getter for property rbtYear.
     * @return Value of property rbtYear.
     *
     */
    public java.lang.Integer getRbtYear() {
        return rbtYear;
    }
    
    /** Setter for property rbtYear.
     * @param rbtYear New value of property rbtYear.
     *
     */
    public void setRbtYear(java.lang.Integer rbtYear) {
        this.rbtYear = rbtYear;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     *
     */
    public int getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     *
     */
    public void setPk(int pk) {
        this.pk = pk;
    }     
    
    /** Getter for property rbtMailedDate.
     * @return Value of property rbtMailedDate.
     *
     */
    public java.lang.String getRbtMailedDate() {
        return rbtMailedDate;
    }
    
    /** Setter for property rbtMailedDate.
     * @param rbtMailedDate New value of property rbtMailedDate.
     *
     */
    public void setRbtMailedDate(java.lang.String rbtMailedDate) {
        this.rbtMailedDate = rbtMailedDate;
    }
    
    /** Getter for property rbtRequestDate.
     * @return Value of property rbtRequestDate.
     *
     */
    public java.lang.String getRbtRequestDate() {
        return rbtRequestDate;
    }
    
    /** Setter for property rbtRequestDate.
     * @param rbtRequestDate New value of property rbtRequestDate.
     *
     */
    public void setRbtRequestDate(java.lang.String rbtRequestDate) {
        this.rbtRequestDate = rbtRequestDate;
    }

    /** Getter for property prbAffiliateData.
     * @return Value of property prbAffiliateData.
     *
     */
    public java.util.List getPrbAffiliateData() {
        return prbAffiliateData;
    }
    
    /** Setter for property prbAffiliateData.
     * @param prbAffiliateData New value of property prbAffiliateData.
     *
     */
    public void setPrbAffiliateData(java.util.List prbAffiliateData) {
        this.prbAffiliateData = prbAffiliateData;
    }
    
    /** Getter for property affiliateIdentifier.
     * @return Value of property affiliateIdentifier.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getAffiliateIdentifier() {
        return affiliateIdentifier;
    }
    
    /** Setter for property affiliateIdentifier.
     * @param affiliateIdentifier New value of property affiliateIdentifier.
     *
     */
    public void setAffiliateIdentifier(org.afscme.enterprise.affiliate.AffiliateIdentifier affiliateIdentifier) {
        this.affiliateIdentifier = affiliateIdentifier;
    }
    
}
