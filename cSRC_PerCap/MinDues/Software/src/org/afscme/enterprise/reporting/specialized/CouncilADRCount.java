package org.afscme.enterprise.reporting.specialized;

import java.util.ArrayList;

/**
 * Provides membership activity information for a council.
 */
public class CouncilADRCount
{
	/**
	 * A list of AffiliateADRCounts.
     */
    private ArrayList aadrList = new ArrayList();

    /**
	 * The unique identifier for the council.
     */
    private String councilRetireeChap;

	/**
	 * Constructs council info.
	 * @param councilRetireeChap New value of property councilRetireeChap.
     */
	public CouncilADRCount(String councilRetireeChap) {
		this.councilRetireeChap = councilRetireeChap;
	}

	public CouncilADRCount() {
		super();
	}

	/**
	 * Adds a AffiliateADRCount.
	 * @param cadr The AffiliateADRCount to be added.
     */
	public void addAffiliateADRCount(AffiliateADRCount aadr) {
		aadrList.add(aadr);
		return;
    }

	/**
	 * Gets all AffiliateADRCounts.
	 * @return A list of AffiliateADRCounts.
     */
	public ArrayList getAffiliateADRCounts() {
		return aadrList;
    }

    /**
	 * Gets a AffiliateADRCount based on the affPk.
	 * @param The affPk to be searched on.
	 * @return A AffiliateADRCount.
     */
    public AffiliateADRCount getAffiliateADRCount(int affPk) {
		for(int i = 0; i < aadrList.size(); i++) {
			AffiliateADRCount aadr = (AffiliateADRCount)aadrList.get(i);
			if(aadr.getAffiliateData().getAffPk().equals(new Integer(affPk)))
				return aadr;
		}
		return null;
    }

    /**
	 * Gets the revised count for a council.
	 * @return The revised count for the council.
     */
	public int getRevisedCount() {
		int total = 0;
		for(int i = 0; i < aadrList.size(); i++) {
			total += ((AffiliateADRCount)aadrList.get(i)).getRevisedCount();
		}
		return total;
	}

	/**
	 * Gets the add count for a council.
	 * @return The add count for the council.
     */
	public int getAddCount() {
		int total = 0;
		for(int i = 0; i < aadrList.size(); i++) {
			total += ((AffiliateADRCount)aadrList.get(i)).getAddCount();
		}
		return total;
	}

	/**
	 * Gets the drop count for a council.
	 * @return The drop count for the council.
     */
	public int getDropCount() {
		int total = 0;
		for(int i = 0; i < aadrList.size(); i++) {
			total += ((AffiliateADRCount)aadrList.get(i)).getDropCount();
		}
		return total;
	}

	/** Getter for property councilRetireeChap.
	 * @return Value of property councilRetireeChap.
	 *
	 */
  	public String getCouncilRetireeChap() {
		return councilRetireeChap;
	}

	/** Setter for property councilRetireeChap.
	 * @param councilRetireeChap New value of property councilRetireeChap.
	 *
	 */
	public void setCouncilRetireeChap(String councilRetireeChap) {
		this.councilRetireeChap = councilRetireeChap;
    }
}

