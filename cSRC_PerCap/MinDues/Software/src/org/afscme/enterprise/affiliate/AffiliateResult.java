package org.afscme.enterprise.affiliate;


/**
 * Represents a single result from an Affiliate search.
 */
public class AffiliateResult
{
	private int empAffPk;
    private Integer affPk;
    private AffiliateIdentifier affiliateId;
    private String affAbreviatedNm;
    private String active;
    private String batch_ID;

// Getter and Setter Methods...

    /** Getter for property affAbreviatedNm.
     * @return Value of property affAbreviatedNm.
     *
     */
    public String getAffAbreviatedNm() {
        return affAbreviatedNm;
    }

    /** Setter for property affAbreviatedNm.
     * @param affAbreviatedNm New value of property affAbreviatedNm.
     *
     */
    public void setAffAbreviatedNm(String affAbreviatedNm) {
        this.affAbreviatedNm = affAbreviatedNm;
    }

    /** Getter for property active.
     * @return Value of property active.
     *
     */
    public String getActive() {
        return active;
    }

     public String getBatch_ID() {
        return batch_ID;
    }

    public void setBatch_ID(String batch_ID) {
        this.batch_ID = batch_ID;
    }   
    
    /** Setter for property active.
     * @param active New value of property active.
     *
     */
    public void setActive(String active) {
        this.active = active;
    }

    /** Getter for property affiliateId.
     * @return Value of property affiliateId.
     *
     */
    public AffiliateIdentifier getAffiliateId() {
        return affiliateId;
    }

    /** Setter for property affiliateId.
     * @param affiliateId New value of property affiliateId.
     *
     */
    public void setAffiliateId(AffiliateIdentifier affiliateId) {
        if (affiliateId == null) {
            this.affiliateId = new AffiliateIdentifier();
        } else {
            this.affiliateId = affiliateId;
        }
    }

    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }

    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }

    public int getEmpAffPk() {
        return empAffPk;
    }

    public void setEmpAffPk(int empAffPk) {
        this.empAffPk = empAffPk;
    }
}
