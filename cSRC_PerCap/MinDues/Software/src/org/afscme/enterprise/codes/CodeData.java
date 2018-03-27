package org.afscme.enterprise.codes;

import java.io.Serializable;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents a singe code.  e.g.: VP - Vice President
 */
public class CodeData implements Serializable, Comparable
{
	
	/**
	 * primary key
	 */
	protected Integer pk;
	
	/**
	 * Short code, like 'P'
	 */
	protected String code;
	
	/**
	 * Short description, like 'Vice President'
	 */
	protected String description;
	
	/**
	 * Key used for sorting the code in dropdowns.
	 */
	protected String sortKey;
    
	/**
	 * True if this code is active.
	 */
	protected boolean active;

    /** Getter for property code.
     * @return Value of property code.
     */
    public String getCode() {
        return code;
    }
    
    /** Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(String code) {
        this.code = code;
    }
    
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
    
    /** Getter for property sortKey.
     * @return Value of property sortKey.
     */
    public String getSortKey() {
        return sortKey;
    }
    
    /** Setter for property sortKey.
     * @param sortKey New value of property sortKey.
     */
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }
	
	/**
	 * From Comparable interface.  Usese the sortKey property for sorting
	 */  
	public int compareTo(Object obj) {
		if (equals(obj))
            return 0;
		else {
            int result = TextUtil.compareAlphanumerics(sortKey, ((CodeData)obj).sortKey);
            if (result != 0)
                return result;
            else
                result = TextUtil.compareAlphanumerics(code, ((CodeData)obj).code);
            if (result != 0)
                return result;
            else
                return pk.compareTo(((CodeData)obj).pk);
        }
	}
    
    /** Getter for property active.
     * @return Value of property active.
     *
     */
    public boolean isActive() {
        return active;
    }
    
    /** Setter for property active.
     * @param active New value of property active.
     *
     */
    public void setActive(boolean active) {
        this.active = active;
    }
	
	/**
	 * Override from java.lang.Object
	 */  
	public boolean equals(Object obj) {
		CodeData other = (CodeData)obj;
		return (
            TextUtil.equals(pk, other.pk) &&
            TextUtil.equals(code, other.code) &&
            TextUtil.equals(description, other.description) &&
            TextUtil.equals(sortKey, other.sortKey));
    }
	
	public String toString() {
		return "pk: " + pk + ", code: " + code + ", description: " + description + ", sortKey: " + sortKey;
	}
    
}
