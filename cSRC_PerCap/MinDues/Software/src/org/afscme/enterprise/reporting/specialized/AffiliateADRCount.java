package org.afscme.enterprise.reporting.specialized;

import org.afscme.enterprise.affiliate.AffiliateData;

/**
 * Provides membership activity information for an affiliate.
 */
public class AffiliateADRCount
{
    /**
	 * The affiliate being counted.
     */
    private AffiliateData affiliateData;

    /**
	 * The revised count for the affiliate.
     */
    private int revisedCount;

    /**
	 * The add count for the affiliate.
     */
    private int addCount;

    /**
	 * The drop count for the affiliate.
     */
    private int dropCount;

	/**
	 * Constructs an AffiliateADRCount.
	 * @param affiliateData The affiliate for which a count is being maintained.
     */
	public AffiliateADRCount(AffiliateData affiliateData) {
		affiliateData = affiliateData;
	}

	public AffiliateADRCount() {
		super();
	}

    /** Getter for property affiliateData.
     * @return Value of property affiliateData.
     *
     */
    public AffiliateData getAffiliateData() {
        return affiliateData;
    }

    /** Setter for property affiliateData.
     * @param affiliateData New value of property affiliateData.
     *
     */
    public void setAffiliateData(AffiliateData affiliateData) {
        this.affiliateData = affiliateData;
    }

    /** Getter for property revisedCount.
     * @return Value of property revisedCount.
     *
     */
    public int getRevisedCount() {
        return revisedCount;
    }

    /** Setter for property revisedCount.
     * @param revisedCount New value of property revisedCount.
     *
     */
    public void setRevisedCount(int revisedCount) {
        this.revisedCount = revisedCount;
    }

    /** Getter for property addCount.
	 * @return Value of property addCount.
	 *
     */
	public int getAddCount() {
	    return addCount;
	}

	/** Setter for property addCount.
	 * @param addCount New value of property addCount.
	 *
	 */
	public void setAddCount(int addCount) {
		this.addCount = addCount;
    }

    /** Getter for property dropCount.
	 * @return Value of property dropCount.
	 *
	 */
	public int getDropCount() {
		return dropCount;
	}

	/** Setter for property dropCount.
	 * @param dropCount New value of property dropCount.
	 *
	 */
	public void setDropCount(int dropCount) {
		this.dropCount = dropCount;
    }
}
