package org.afscme.enterprise.common.web;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;


/** 
 * Base class of a struts form used for entering affiliate search criteria
 */
public class AffiliateSearchForm extends SearchForm
{
	protected String state;
	protected Character type;
	protected String local;
	protected String subUnit;
	protected String council;
	
	public AffiliateSearchForm() {
		newSearch();
	}

    /** resets the search values to the default */
	public void newSearch() {
		type = new Character('L');
        state = "";
		local = "";
		subUnit = "";
		council = "";
		sortBy = "type";
		order = 1;
		page=0;
		total=0;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public Character getType() {
		return type;
	}
	
	public void setType(Character type) {
		this.type = type;
	}
	
	public String getLocal() {
		return local;
	}
	
	public void setLocal(String local) {
		this.local = local;
	}
	
	public String getSubUnit() {
		return subUnit;
	}
	
	public void setSubUnit(String subUnit) {
		this.subUnit = subUnit;
	}
	
	public String getCouncil() {
		return council;
	}
	
	public void setCouncil(String council) {
		this.council = council;
	}
	
}



