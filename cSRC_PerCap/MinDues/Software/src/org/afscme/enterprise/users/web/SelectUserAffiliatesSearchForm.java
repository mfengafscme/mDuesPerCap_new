package org.afscme.enterprise.users.web;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.common.web.AffiliateSearchForm;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;



/**
 * Represents the form on the select user affiliates search page
 *
 * @struts:form name="selectUserAffiliatesSearchForm"
 */
public class SelectUserAffiliatesSearchForm extends AffiliateSearchForm
{
	private String selected;

	//buttons
	private String selectAllResults;
	private String deSelectAllResults;
	
	public SelectUserAffiliatesSearchForm() {
		newSearch();
	}
	
	public void newSearch() {
        super.newSearch();
		selected = null;
	}
    
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		selection = new String[0];  //<- very important to do this, otherwise, when the user select no checkboxes, this value will not be changed.
		selectAllResults = null;
		deSelectAllResults = null;
	}
	
	public AffiliateData getAffiliateData() {
		AffiliateData data = new AffiliateData();
		data.setType(type);
		if (!TextUtil.isEmpty(council))
			data.setCouncil(council);
		if (!TextUtil.isEmpty(local))
			data.setLocal(local);
		if (!TextUtil.isEmpty(state))
			data.setState(state);
		if (!TextUtil.isEmpty(subUnit))
			data.setSubUnit(subUnit);
        
        if (!TextUtil.isEmpty(selected))
            data.setSelected(new Boolean(selected.equals("true")));
		
		return data;
	}
	
	public AffiliateSortData getAffiliateSortData() {
		AffiliateSortData data = new AffiliateSortData();
		data.setPage(page);
		data.setPageSize(getPageSize());
		
		if (sortBy.equals("name"))
			data.setSortField(AffiliateSortData.FIELD_NAME);
        else if (sortBy.equals("type"))
			data.setSortField(AffiliateSortData.FIELD_TYPE);
		else if (sortBy.equals("local"))
			data.setSortField(AffiliateSortData.FIELD_LOCAL);
		else if (sortBy.equals("council"))
			data.setSortField(AffiliateSortData.FIELD_COUNCIL);
		else if (sortBy.equals("state"))
			data.setSortField(AffiliateSortData.FIELD_STATE);
		else if (sortBy.equals("subUnit"))
			data.setSortField(AffiliateSortData.FIELD_SUBUNIT);
		else if (sortBy.equals("selected"))
			data.setSortField(AffiliateSortData.FIELD_SELECTED);

		data.setSortOrder(order);
		
		return data;
	}
		
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return null;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getName() + "[");
		buf.append("state: " + state);
		buf.append(", type: " + type);
		buf.append(", local: " + local);
		buf.append(", subUnit: " + subUnit);
		buf.append(", council: " + council);
		buf.append(", order: " + order);
		buf.append(", sortBy: " + sortBy);
		buf.append(", page: " + page);
		buf.append(", total: " + total);
		buf.append(", selected: " + selected);
		buf.append(", selection: " + CollectionUtil.toString(selection));
		return buf.toString()+"]";
	}
	
    /** Sets a List of WidgetData objects, the results of the search. */
    public void setResults(Collection results) {
		super.setResults(results);
		Iterator it = results.iterator();
		LinkedList selectionList = new LinkedList();
		while (it.hasNext()) {
			AffiliateData data = ((AffiliateData)it.next());
			if (data.isSelected() != null && data.isSelected().booleanValue())
				selectionList.add(String.valueOf(data.getPk()));
		}
		setSelection((String[])selectionList.toArray(new String[0]));
    }
    
	public String getSelected() {
		return selected;
	}
	
	public void setSelected(String val) {
		selected = val;
	}
	
	public String[] getSelection() {
		return selection;
	}
	
	public void setSelection(String[] selection) {
		this.selection = selection;
	}

	
	//
	// Button properties
	//
	
	public String getSelectAllResults() {
		return this.selectAllResults;
	}
	public void setSelectAllResults(String val) {
		this.selectAllResults = val;
	}
	public boolean isSelectAllResultsButton() {
		return this.selectAllResults != null;
	}
	public String getDeSelectAllResults() {
		return this.selectAllResults;
	}
	public void setDeSelectAllResults(String val) {
		this.deSelectAllResults = val;
	}
	public boolean isDeSelectAllResultsButton() {
		return this.deSelectAllResults != null;
	}
	
	
}



