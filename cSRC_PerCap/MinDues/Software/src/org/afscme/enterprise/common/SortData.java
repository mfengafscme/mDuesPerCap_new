package org.afscme.enterprise.common;

import org.afscme.enterprise.util.ConfigUtil;

public class SortData {
    
    public static final int DIRECTION_ASCENDING = 1;
    public static final int DIRECTION_DESCENDING = -1;
    public static final int DIRECTION_NONE = 0;
    
    protected int sortField;
    protected int direction;
    protected int page;
    protected int pageSize;
    
    public SortData() {
        pageSize = ConfigUtil.getConfigurationData().getSearchResultPageSize();
    }
    
    /** Getter for property page.
     * @return Value of property page.
     *
     */
    public int getPage() {
        return page;
    }
    
    /** Setter for property page.
     * @param page New value of property page.
     *
     */
    public void setPage(int page) {
        this.page = page;
    }
    
    /** Getter for property pageSize.
     * @return Value of property pageSize.
     *
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /** Setter for property pageSize.
     * @param pageSize New value of property pageSize.
     *
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /** Getter for property sortField.
     * @return Value of property sortField.
     *
     */
    public int getSortField() {
        return sortField;
    }
    
    /** Setter for property sortField.
     * @param sortField New value of property sortField.
     *
     */
    public void setSortField(int sortField) {
        this.sortField = sortField;
    }
    
    /** Getter for property direction.
     * @return Value of property direction.
     *
     */
    public int getDirection() {
        return direction;
    }
    
    /** Setter for property direction.
     * @param direction New value of property direction.
     *
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
}
