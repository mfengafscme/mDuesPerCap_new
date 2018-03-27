package org.afscme.enterprise.codes;

import java.io.Serializable;

/**
 * Represents a Code Type Category, e.g. Members
 */
public class CategoryData implements Serializable
{
	
	/**
	 * primary key
	 */
	protected Integer pk;
	
	/**
	 * category name
	 */
	protected String name;
    
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
	
	public String toString() {
		return "pk: " + pk + ", name: " + name;
	}
    
}
