package org.afscme.enterprise.organization;

/**
 * Represents data returned to propulate a search result set (and so is tied to 
 * the presentation layer requirement)
 */
public class OrganizationResult 
{

    public Integer orgPK;
    public String orgNm;
    public Integer orgType;
        
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
    public static final int[] SORT_FIELD_IDS = new int[] {
		FIELD_NAME, FIELD_TYPE };
    
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
        

    /** Getter for property orgPK.
     * @return Value of property orgPK.
     *
     */
    public java.lang.Integer getOrgPK() {
        return orgPK;
    }
    
    /** Setter for property orgPK.
     * @param orgPK New value of property orgPK.
     *
     */
    public void setOrgPK(java.lang.Integer orgPK) {
        this.orgPK = orgPK;
    }
    
    /** Getter for property orgNm.
     * @return Value of property orgNm.
     *
     */
    public java.lang.String getOrgNm() {
        return orgNm;
    }
    
    /** Setter for property orgNm.
     * @param orgNm New value of property orgNm.
     *
     */
    public void setOrgNm(java.lang.String orgNm) {
        this.orgNm = orgNm;
    }
    
    /** Getter for property orgType.
     * @return Value of property orgType.
     *
     */
    public java.lang.Integer getOrgType() {
        return orgType;
    }
    
    /** Setter for property orgType.
     * @param orgType New value of property orgType.
     *
     */
    public void setOrgType(java.lang.Integer orgType) {
        this.orgType = orgType;
    }


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

    public String toString() {
        return "OrganizationResult[" +
        "orgPK="+orgPK+", "+
        "orgNm="+orgNm+", "+        
        "orgType="+orgType+"]";
    }
}

