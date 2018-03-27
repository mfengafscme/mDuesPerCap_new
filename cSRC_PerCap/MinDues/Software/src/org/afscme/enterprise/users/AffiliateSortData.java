package org.afscme.enterprise.users;

import java.io.Serializable;

/** Used by MaintainUsersBean getAffiliates() to specify how the data should be sorted */
public class AffiliateSortData implements Serializable
{
	//
    // Sort Directions
    // 
    
	public static final int SORT_ASCENDING = 1;
	public static final int SORT_DESCENDING = -1;
	public static final int SORT_NONE = 0;

    //
    // Fields
    // 
    
    public static final int FIELD_NONE = 0;
	public static final int FIELD_NAME = 1;
    public static final int FIELD_TYPE = 2;
	public static final int FIELD_LOCAL = 3;
	public static final int FIELD_STATE = 4;
	public static final int FIELD_COUNCIL = 5;
	public static final int FIELD_SUBUNIT = 6;
	public static final int FIELD_SELECTED = 7;
    public static final int[] SORT_FIELD_IDS = new int[] {
		FIELD_NAME, FIELD_TYPE, FIELD_LOCAL, FIELD_STATE, FIELD_COUNCIL, FIELD_SUBUNIT, FIELD_SELECTED };
    
	/**
	 * The field to sort on.  Must be one of the values in this class's FIELD_XXX 
	 * constants.
	 */
	protected int sortField = FIELD_NONE;
	
	/**
	 * The sort order.  Must be one of the values in this class's SORT_XXX constants.
	 */
	protected int sortOrder = SORT_NONE;
	
	/**
	 * The page to search for.  e.g, if pageSize is 10 and the search is for page 3, 
	 * records 20-29 will be returned.
	 */
	protected int page = 0;
	
	/**
	 * The size of the result page to return.
	 */
	protected int pageSize;
    
    /** Getter for property page.
     * @return Value of property page.
     */
    public int getPage() {
        return page;
    }
    
    /** Setter for property page.
     * @param page New value of property page.
     */
    public void setPage(int page) {
        this.page = page;
    }
    
    /** Getter for property pageSize.
     * @return Value of property pageSize.
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /** Setter for property pageSize.
     * @param pageSize New value of property pageSize.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /** Getter for property sortField.
     * @return Value of property sortField.
     */
    public int getSortField() {
        return sortField;
    }
    
    /** Setter for property sortField.
     * @param sortField New value of property sortField.
     */
    public void setSortField(int sortField) {
        this.sortField = sortField;
    }
    
    /** Getter for property sortOrder.
     * @return Value of property sortOrder.
     */
    public int getSortOrder() {
        return sortOrder;
    }
    
    /** Setter for property sortOrder.
     * @param sortOrder New value of property sortOrder.
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
}
