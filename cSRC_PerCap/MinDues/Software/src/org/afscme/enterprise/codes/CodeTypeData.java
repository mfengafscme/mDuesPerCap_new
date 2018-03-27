package org.afscme.enterprise.codes;

import java.io.Serializable;

/**
 * Represents a code type.  e.g.: Officer Title
 */
public class CodeTypeData implements Serializable, Comparable
{
	
	/**
	 * primary key
	 */
	protected String key;
	
	/**
	 * Name, like 'Officer Title'
	 */
	protected String name;
	
	/**
	 * Description, like "This is the title of the officer".
	 */
	protected String description;
	
	/**
	 * Key of the associated CategoryData
	 */
	protected Integer category;
    
    /** Getter for property category.
     * @return Value of property category.
     */
    public Integer getCategory() {
        return category;
    }
    
    /** Setter for property category.
     * @param category New value of property category.
     */
    public void setCategory(Integer category) {
        this.category = category;
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
    
    /** Getter for property key.
     * @return Value of property key.
     */
    public String getKey() {
        return key;
    }
    
    /** Setter for property key.
     * @param key New value of property key.
     */
    public void setKey(String key) {
        this.key = key;
    }

	public String toString() {
		return "key: " + key + ", name: " + name + ", description: " + description + ", category: " + category;
	}
    
    private Comparable comparableValue() {
        return category+key+name+description;
    }
        
    public int compareTo(Object o) {
        return comparableValue().compareTo(((CodeTypeData)o).comparableValue());
    }
}
