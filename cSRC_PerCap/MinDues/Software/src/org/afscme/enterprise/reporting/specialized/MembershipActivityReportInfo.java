package org.afscme.enterprise.reporting.specialized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import org.afscme.enterprise.affiliate.AffiliateData;

/**
 * Provides membership activity information.
 */
public class MembershipActivityReportInfo
{
	/**
	 * List of month/state info
	 */
	private ArrayList msadrList = new ArrayList();

	/**
	 * Map of affiliates by state
	 */
	private HashMap affiliateStateMap = new HashMap();

	/**
	 * Adds month/state information to membership activity info.
	 * @param msadr The month/state info to be added.
     */
	public void addMonthStateADRCount(MonthStateADRCount msadr) {
		msadrList.add(msadr);
    }

    /**
	 * Retrieves month/state info.
	 * @param month The month to be searched.
	 * @param state The state to be searched.
	 * @return The month/state info.
     */
    public MonthStateADRCount getMonthStateADRCount(int month, String state) {
		for(int i = 0; i < msadrList.size(); i++) {
			MonthStateADRCount msadr = (MonthStateADRCount)msadrList.get(i);
			if(msadr.getMonth() == month && msadr.getState().equals(state))
				return msadr;
		}
		return null;
    }

	/**
	 * Retrieves a list of month/state info based on a particular month.
	 * @param month The month to be searched.
	 * @return A list of month/state information.
     */
    private ArrayList getMonthStateADRCountsByMonth(int month) {
		ArrayList list = new ArrayList();
		for(int i = 0; i < msadrList.size(); i++) {
			MonthStateADRCount msadr = (MonthStateADRCount)msadrList.get(i);
			if(msadr.getMonth() == month) {
				list.add(msadr);
			}
		}
		return list;
	}

	/**
	 * Adds affiliates based on a particular state.
     * @param affData The affiliate to be added.
	 * @param state The state the affiliate should be added to.
     */
	public void addAffiliateByState(AffiliateData affData, String state) {
		ArrayList affiliates = (ArrayList)affiliateStateMap.get(state);
		if(affiliates == null) {
			affiliates = new ArrayList();
			affiliates.add(affData);
			affiliateStateMap.put(state, affiliates);
		}
		else {
			boolean exists = false;
			for(int i = 0; i < affiliates.size(); i++) {
				AffiliateData affiliateData = (AffiliateData)affiliates.get(i);
				if(affiliateData.getAffPk().equals(affData.getAffPk())) {
					exists = true;
					break;
				}
			}
			if(!exists) {
				affiliates.add(affData);
			}
		}
	}

	/**
	 * Adds affiliates based on a particular state.
     * @param state The state the affiliate should be added to.
     */
	public ArrayList getAffiliatesByState(String state) {
		return (ArrayList)affiliateStateMap.get(state);
    }

   /**
	* Gets the add count for a month.
	* @param month The month to be searched on.
	* @return The add count for the month.
    */
    public int getAddCount(int month) {
		ArrayList list = getMonthStateADRCountsByMonth(month);
		int total = 0;
		for(int i = 0; i < list.size(); i++) {
			MonthStateADRCount msadr = (MonthStateADRCount)list.get(i);
			total += msadr.getAddCount();
		}
		return total;
    }

    /**
	 * Gets the drop count for a month.
	 * @param month The month to be searched on.
	 * @return The drop count for the month.
     */
    public int getDropCount(int month) {
		ArrayList list = getMonthStateADRCountsByMonth(month);
		int total = 0;
		for(int i = 0; i < list.size(); i++) {
			MonthStateADRCount msadr = (MonthStateADRCount)list.get(i);
			total += msadr.getDropCount();
		}
		return total;
    }

	/**
	 * Gets the revised count for a month.
	 * @param month The month to be searched on.
	 * @return The revised count for the month.
     */
    public int getRevisedCount(int month) {
		ArrayList list = getMonthStateADRCountsByMonth(month);
		int total = 0;
		for(int i = 0; i < list.size(); i++) {
			MonthStateADRCount msadr = (MonthStateADRCount)list.get(i);
			total += msadr.getRevisedCount();
		}
		return total;
    }
}

