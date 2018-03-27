package org.afscme.enterprise.common.web;

import java.util.Collection;
import org.apache.struts.action.ActionForm;
import org.afscme.enterprise.util.ConfigUtil;



public class SearchForm extends ActionForm
{
	//results
	protected Collection results;
	protected String[] selection;
	protected int total;
	protected int page;

	//configuration
	protected int pageSize;

	//sort
	protected int order;
	protected String sortBy;

	public SearchForm() {
		pageSize = ConfigUtil.getConfigurationData().getSearchResultPageSize();
	}

    /** Gets what page of results are to be shown. */
    public int getPage() {
        return page;
    }

	/** Sets what page of results are to be shown. */
    public void setPage(int page) {
        this.page = page;
    }

    /** Gets the index of the next page to be shown. */
    public int getNextPage() {
        return page + 1;
    }

    /** Sets the index of the previous page to be shown. */
    public int getPrevPage() {
        return page - 1;
    }

    /** Returns true iff there is a next page in the results list. */
    public boolean isNextPageExists() {
        if (results != null)
            return (getTotal() > (getPageSize() * (1 + getPage())));
        else
            return false;
    }

    /** Returns true iff there is a previous page in the results list. */
    public boolean isPrevPageExists() {
            return (getPage() > 0);
    }

    public Collection getResults() {
        return results;
    }

    public void setResults(Collection results) {
        this.results = results;
    }

    /** Gets the number of results in a page. */
    public int getPageSize() {
        return pageSize;
    }

    /** Gets the total number of results that matched the criteria. */
    public int getTotal() {
        return total;
    }

    /** Sets the total number of results that matched the criteria. */
    public void setTotal(int total) {
        this.total = total;
    }

    public int getStartIndex() {
        if (getTotal() == 0)
            return 0;
        return  (getPage() * getPageSize()) + 1;
    }

    public int getEndIndex() {
        return Math.min(getStartIndex() + getPageSize() - 1, getTotal());
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
        this.order = order;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

    public int getPageCount() {
        return (total / pageSize) + 1;
    }

    public String toString() {
        return
            "order="+order+", " +
            "page="+page+", " +
            "sortBy="+sortBy+", " +
            "total="+total+", " +
            "pageSize="+pageSize+", " +
            "results="+results;
    }

}
