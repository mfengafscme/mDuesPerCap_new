package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Data needed to search for Affiliate Changes.
 */
public class AffiliateChangeCriteria {
    
    public static final String SORT_BY_SECTION          = "aff_section";
    public static final String SORT_BY_CHANGED_DATE     = "chng_dt";
    
    private Integer sectionCodePk;
    private Timestamp changeDateFrom;
    private Timestamp changeDateTo;
    private Integer affPk;
    
    protected int page;
    protected int pageSize;
    protected int ordering;
    protected String orderBy;
    
    
    // Getter and Setter Methods...
    
    /** Getter for property changeDateFrom.
     * @return Value of property changeDateFrom.
     *
     */
    public Timestamp getChangeDateFrom() {
        return changeDateFrom;
    }
    
    /** Setter for property changeDateFrom.
     * @param changeDateFrom New value of property changeDateFrom.
     *
     */
    public void setChangeDateFrom(Timestamp changeDateFrom) {
        this.changeDateFrom = changeDateFrom;
    }
    
    /** Getter for property changeDateTo.
     * @return Value of property changeDateTo.
     *
     */
    public Timestamp getChangeDateTo() {
        return changeDateTo;
    }
    
    /** Setter for property changeDateTo.
     * @param changeDateTo New value of property changeDateTo.
     *
     */
    public void setChangeDateTo(Timestamp changeDateTo) {
        this.changeDateTo = changeDateTo;
    }
    
    /** Getter for property sectionCodePk.
     * @return Value of property sectionCodePk.
     *
     */
    public Integer getSectionCodePk() {
        return sectionCodePk;
    }
    
    /** Setter for property sectionCodePk.
     * @param sectionCodePk New value of property sectionCodePk.
     *
     */
    public void setSectionCodePk(Integer sectionCodePk) {
        this.sectionCodePk = sectionCodePk;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property orderBy.
     * @return Value of property orderBy.
     *
     */
    public int getOrdering() {
        return ordering;
    }
    
    /** Setter for property ordering.
     * @param ordering New value of property ordering.
     *
     */
    public void setOrdering(int ordering) {
        this.ordering = ordering;
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
    
    /** Getter for property orderBy.
     * @return Value of property orderBy.
     *
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }
    
    /** Setter for property orderBy.
     * @param orderBy New value of property sortBy.
     *
     */
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;
    }
    
}
