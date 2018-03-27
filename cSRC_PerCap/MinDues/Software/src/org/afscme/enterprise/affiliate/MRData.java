package org.afscme.enterprise.affiliate;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.TextUtil;

/**
 * Affiliate Membership Reporting data
 */
public class MRData 
{
    private Integer affPk;
    private Integer informationSource;
    private AffiliateIdentifier newAffiliateId;
    private Integer affStatus;
    private boolean noCards;
    private boolean noPEMail;
    private String comment;
    private RecordData recordData;

    public boolean equals(Object other) {
        if (!(other instanceof MRData))
            return false;
        
        MRData mrData = (MRData)other;
        return
            TextUtil.equals(mrData.informationSource, informationSource) &&
            TextUtil.equals(mrData.affStatus, affStatus) &&
            mrData.noCards == noCards &&
            mrData.noPEMail == noPEMail &&
            TextUtil.equals(mrData.comment, comment);
    }
            
        
        
        
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public java.lang.String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }
    
    /** Getter for property newAffiliateId.
     * @return Value of property newAffiliateId.
     *
     */
    public org.afscme.enterprise.affiliate.AffiliateIdentifier getNewAffiliateId() {
        return newAffiliateId;
    }
    
    /** Setter for property newAffiliateId.
     * @param newAffiliateId New value of property newAffiliateId.
     *
     */
    public void setNewAffiliateId(org.afscme.enterprise.affiliate.AffiliateIdentifier newAffiliateId) {
        this.newAffiliateId = newAffiliateId;
    }
    
    /** Getter for property noPEMail.
     * @return Value of property noPEMail.
     *
     */
    public boolean isNoPEMail() {
        return noPEMail;
    }
    
    /** Setter for property noPEMail.
     * @param noPEMail New value of property noPEMail.
     *
     */
    public void setNoPEMail(boolean noPEMail) {
        this.noPEMail = noPEMail;
    }
    
    /** Getter for property informationSource.
     * @return Value of property informationSource.
     *
     */
    public java.lang.Integer getInformationSource() {
        return informationSource;
    }
    
    /** Setter for property informationSource.
     * @param informationSource New value of property informationSource.
     *
     */
    public void setInformationSource(java.lang.Integer informationSource) {
        this.informationSource = informationSource;
    }
    
    /** Getter for property recordData.
     * @return Value of property recordData.
     *
     */
    public RecordData getRecordData() {
        return recordData;
    }
    
    /** Setter for property recordData.
     * @param recordData New value of property recordData.
     *
     */
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
    }
    
    /** Getter for property noCards.
     * @return Value of property noCards.
     *
     */
    public boolean isNoCards() {
        return noCards;
    }
    
    /** Setter for property noCards.
     * @param noCards New value of property noCards.
     *
     */
    public void setNoCards(boolean noCards) {
        this.noCards = noCards;
    }
    
    /** Getter for property affStatus.
     * @return Value of property affStatus.
     *
     */
    public java.lang.Integer getAffStatus() {
        return affStatus;
    }
    
    /** Setter for property affStatus.
     * @param affStatus New value of property affStatus.
     *
     */
    public void setAffStatus(java.lang.Integer affStatus) {
        this.affStatus = affStatus;
    }
    
}
