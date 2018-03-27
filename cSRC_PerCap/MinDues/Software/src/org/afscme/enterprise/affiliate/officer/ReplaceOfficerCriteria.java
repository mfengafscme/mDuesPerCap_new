/*
 * ReplaceOfficerCriteria.java
 *
 * Created on December 16, 2003, 3:40 PM
 */

package org.afscme.enterprise.affiliate.officer;

/**
 *
 * @author  dbashford
 */
public class ReplaceOfficerCriteria {
    
    public static final String SORT_BY_AFF_ID_TYPE     = "o.aff_type";
    public static final String SORT_BY_AFF_ID_LOCAL    = " CAST(o.aff_localSubChapter AS int) ";
    public static final String SORT_BY_AFF_ID_STATE    = "o.aff_stateNat_type";
    public static final String SORT_BY_AFF_ID_SUB_UNIT = " CAST(o.aff_subUnit AS int) ";
    public static final String SORT_BY_AFF_ID_COUNCIL  = " CAST(o.aff_councilRetiree_chap AS int) ";
    public static final String SORT_BY_NAME            = "p.last_nm";
    public static final String SORT_BY_ADDRESS         = "a.addr1";
    public static final String SORT_BY_CITY            = "a.city";
    public static final String SORT_BY_STATE           = "a.state";    
    public static final String SORT_BY_ZIP             = "a.zipcode";      
    
    private String firstName; 
    private String middleName;
    private String lastName;
    private Integer suffix;
    private Integer affPk;
    private Boolean elected;
    
    // The remaining attributes are for pagination and sorting only
    private int page;
    private int pageSize;
    private String orderBy;
    private int ordering;
    
    public ReplaceOfficerCriteria() {
        this.firstName = null;
        this.middleName = null;
        this.lastName = null;
        this.suffix = null;
        this.affPk = null;
        this.elected = null;

        this.page = 0;
        this.pageSize = 10000; // Defaults to some high number for back end method use. This is overriden by the ActionForm class for UI use.
        this.orderBy = null;
        this.ordering = 0;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     *
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     *
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }    
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param last New value of property lastName.
     *
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
                     
    /** Getter for property suffix.
     * @return Value of property suffix.
     *
     */
    public java.lang.Integer getSuffix() {
        return suffix;
    }
    
    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     *
     */
    public void setSuffix(java.lang.Integer suffix) {
        this.suffix = suffix;
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

    /** Getter for property orderBy.
     * @return Value of property orderBy.
     *
     */
    public String getOrderBy() {
        return orderBy;
    }

    /** Setter for property orderBy.
     * @param orderBy New value of property orderBy.
     *
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    /** Getter for property ordering.
     * @return Value of property ordering.
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
    
   /** Getter for property elected.
     * @return Value of property elected.
     *
     */
    public Boolean isElected() {
        return elected;
    }    
    
    /** Setter for property elected.
     * @param expirationMonth New value of property elected.
     *
     */
    public void setElected(Boolean elected) {
        this.elected = elected;
    }          
}
