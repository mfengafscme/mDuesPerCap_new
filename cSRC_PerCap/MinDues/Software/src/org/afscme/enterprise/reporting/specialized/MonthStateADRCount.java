package org.afscme.enterprise.reporting.specialized;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides membership activity information for a month/state combination.
 */
public class MonthStateADRCount
{
	/**
	 * A list of CouncilADRCounts.
     */
	private ArrayList cadrList = new ArrayList();

    /**
	 * The month which is being counted.
     */
    private int month;

    /**
	 * The state which is being counted.
     */
    private String state;

	/**
	 * Constructs month/state info.
	 * @param month New value of property month.
	 * @param state New value of property state.
     */
	public MonthStateADRCount(int month, String state) {
		this.month = month;
		this.state = state;
	}

	public MonthStateADRCount() {
		super();
	}

	/**
	 * Adds a CouncilADRCount.
	 * @param cadr The CouncilADRCount to be added.
     */
	public void addCouncilADRCount(CouncilADRCount cadr) {
		cadrList.add(cadr);
    }

	/**
	 * Gets all CouncilADRCounts.
	 * @return A list of CouncilADRCount.
     */
	public ArrayList getCouncilADRCounts() {
		return cadrList;
    }

	/**
	 * Gets a CouncilADRCount based on the councilRetireeChap.
	 * @param The councilRetireeChap to be searched on.
     * @return A CouncilADRCount.
     */
	public CouncilADRCount getCouncilADRCount(String councilRetireeChap) {
		for(int i = 0; i < cadrList.size(); i++) {
			CouncilADRCount cadr = (CouncilADRCount)cadrList.get(i);
			if(cadr.getCouncilRetireeChap().equals(councilRetireeChap))
				return cadr;
		}
		return null;
    }

    /**
	 * Gets the revised count for a month/state.
	 * @return The revised count for the month/state.
     */
	public int getRevisedCount() {
		int total = 0;
		for(int i = 0; i < cadrList.size(); i++) {
			total += ((CouncilADRCount)cadrList.get(i)).getRevisedCount();
		}
		return total;
	}

	/**
	 * Gets the add count for a month/state.
	 * @return The add count for the month/state.
     */
	public int getAddCount() {
		int total = 0;
		for(int i = 0; i < cadrList.size(); i++) {
			total += ((CouncilADRCount)cadrList.get(i)).getAddCount();
		}
		return total;
	}

	/**
	 * Gets the drop count for a month/state.
	 * @return The drop count for the month/state.
     */
	public int getDropCount() {
		int total = 0;
		for(int i = 0; i < cadrList.size(); i++) {
			total += ((CouncilADRCount)cadrList.get(i)).getDropCount();
		}
		return total;
	}

	/** Getter for property month.
	 * @return Value of property month.
	 *
	 */
	public int getMonth() {
		return month;
	}

	/** Setter for property month.
	 * @param month New value of property month.
	 *
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/** Getter for property state.
	 * @return Value of property state.
	 */
	public String getState() {
	 	return state;
	}

	/** Setter for property state.
	 * @param state New value of property state.
	 */
	public void setState(String state) {
		this.state = state;
    }
}

