package org.afscme.enterprise.roles;

import java.io.Serializable;

/**
 * Represents a report a role may have access to.
 */
public class ReportPrivilegeData implements Serializable
{
	
	/**
	 * primary key
	 */
	protected Integer pk;
	
	/**
	 * Report name
	 */
	protected String name;
	
	/**
	 * Report Description
	 */
	protected String description;
    
	/**
	 * Report Category (for ui display only)
	 */
	protected String category;

	/** Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** Getter for property pk.
     * @return Value of property pk.
     */
    public Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     */
    public void setPk(Integer pk) {
        this.pk = pk;
    }
    
	/** Getter for property category.
	 * @return Value of property category.
	 */
	public String getCategory() {
		return category;
	}
	
	/** Setter for property category.
	 * @param category New value of property category.
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
}
