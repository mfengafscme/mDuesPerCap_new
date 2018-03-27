package org.afscme.enterprise.controller.web;

import org.afscme.enterprise.common.web.AffiliateSearchForm;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;



/**
 * Represents the search parameters form when the user is selectin an affiliate to user the Data Utility as.
 *
 * @struts:form name="selectAffiliateSearchForm"
 */
public class SelectAffiliateSearchForm extends AffiliateSearchForm
{
    private String cancel = "/selectAffiliateSearch.action?new=";
    private String showNewSearch = "YES";
    /** list of field names as they appear in the HTML.  It is important that these remain in the same order as AffiliateSortData.SORT_FIELD_IDS */
    private static final String[] SORT_FIELD_NAMES = new String[]
        { "name", "type", "local", "state", "council", "subUnit" };
    
	public SelectAffiliateSearchForm() {
		newSearch();
	}
	
	public void newSearch() {
		type = null;
		local = "";
		subUnit = "";
		council = "";
		sortBy = "type";
		order = 1;
		page=0;
		total=0;
	}
	
	public AffiliateData getAffiliateData() {
		AffiliateData data = new AffiliateData();
		if (!TextUtil.isEmptyOrSpaces(type))
                    data.setType(type);
		if (!TextUtil.isEmpty(council))
			data.setCouncil(council);
		if (!TextUtil.isEmpty(local))
			data.setLocal(local);
		if (!TextUtil.isEmpty(state))
			data.setState(state);
		if (!TextUtil.isEmpty(subUnit))
			data.setSubUnit(subUnit);
		data.setSelected(new Boolean(true));
		return data;
	}
	
	public AffiliateSortData getAffiliateSortData() {
            AffiliateSortData data = new AffiliateSortData();
            data.setPage(page);
            data.setPageSize(getPageSize());
            data.setSortField(CollectionUtil.transliterate(sortBy, SORT_FIELD_NAMES, AffiliateSortData.SORT_FIELD_IDS));
            data.setSortOrder(order);
            return data;
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
            return buf.toString()+"]";
	}
        
        public String getCancel() {
            return cancel;
        }
        
        public void setCancel(String cancel) {
            this.cancel = cancel;
        }
        
        public String getShowNewSearch () {
            return showNewSearch;
        }
        
        public void setShowNewSearch (String showNewSearch) {
            this.showNewSearch = showNewSearch;
        }
}



