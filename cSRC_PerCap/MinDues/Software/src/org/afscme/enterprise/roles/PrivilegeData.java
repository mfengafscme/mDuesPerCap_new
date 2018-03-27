package org.afscme.enterprise.roles;

import java.io.Serializable;
import org.afscme.enterprise.util.TextUtil;

/**
 * Represents a privilege a role may have access to.
 */
public class PrivilegeData implements Serializable, Comparable
{
	
	/**
	 * The user has general access to this function (the fuction has no related verb)
	 */
	public static final char VERB_GENERAL = 'G';
	
	/**
	 * The user has edit access to this function.
	 */
	public static final char VERB_EDIT = 'E';
	
	/**
	 * The role has view access to the function.
	 */
	public static final char VERB_VIEW = 'V';
	
	/**
	 * The user had view access to the function.
	 */
	public static final char VERB_SEARCH = 'S';

	/**
	 * Member
	 */
	public static final char PRIVILEGE_CATEGORY_MEMBER = 'M';
	
	/**
	 * Officer
	 */
	public static final char PRIVILEGE_CATEGORY_OFFICER = 'O';
	
	/**
	 * Affiliate
	 */
	public static final char PRIVILEGE_CATEGORY_AFFILIATE = 'A';
	
	/**
	 * System
	 */
	public static final char PRIVILEGE_CATEGORY_SYSTEM = 'S';
	
	/**
	 * Privilege name
	 */
	protected String name;
	
	/**
	 * Category of the privilege.  From the PRIVILEGE_CATEGORY_XXX constants defined in this class.
	 */
	protected char category;
	
	/**
	 * Verb that is associate with the privilege, like 'Edit'.  Must be one of the 
	 * VERB_XXX values in this class.
	 */
	protected char verb;
	
	/**
	 * String key for associating this privilege to an entry in a configuration file.
	 */
	protected String key;

    /** 
     * true iff this privilege is associated with the affiliate data utility
     */
    protected boolean dataUtility;
    
    /** Getter for property category.
     * @return Value of property category.
     */
    public char getCategory() {
        return category;
    }
    
    /** Setter for property category.
     * @param category New value of property category.
     */
    public void setCategory(char category) {
        this.category = category;
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
    
    /** Getter for property verb.
     * @return Value of property verb.
     */
    public char getVerb() {
        return verb;
    }
    
    /** Setter for property verb.
     * @param verb New value of property verb.
     */
    public void setVerb(char verb) {
        this.verb = verb;
    }
    
    /** Getter for property dataUtility.
     * @return Value of property dataUtility.
     */
    public boolean isDataUtility() {
        return dataUtility;
    }
    
    /** Setter for property dataUtility.
     * @param dataUtility New value of property dataUtility.
     */
    public void setDataUtility(boolean dataUtility) {
        this.dataUtility = dataUtility;
    }
    
	/**
	 * From Comparable interface.
	 */  
	public int compareTo(Object obj) {
		if (equals(obj))
            return 0;
		else {
            int result = name.compareTo(((PrivilegeData)obj).name);
            if (result != 0)
                return result;
            else
                return key.compareTo(((PrivilegeData)obj).key);
        }
	}
        
	/**
	 * Override from java.lang.Object
	 */  
	public boolean equals(Object obj) {
		PrivilegeData other = (PrivilegeData)obj;
		return (
            key.equals(other.key) && 
            TextUtil.equals(name, other.name) &&
            category == other.category &&
            verb == other.verb);
    }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("key: " + key + "\n");
		buf.append(", name: " + name + "\n");
		buf.append(", verb: " + verb + "\n");
		buf.append(", dataUtility: " + dataUtility + "\n");
		return buf.toString();
	}
    
}
