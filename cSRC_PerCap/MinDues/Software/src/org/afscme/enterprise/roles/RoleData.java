package org.afscme.enterprise.roles;

import java.io.Serializable;

/**
 * Represents a set of privileges, fields and reports a user may have access to.
 */
public class RoleData implements Serializable
{
	
	/**
	 * primary key
	 */
	protected Integer pk;
	
	/**
	 * Role name
	 */
	protected String name;
	
	/**
	 * role description
	 */
	protected String description;
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getPk() {
        return pk;
    }
    
    public void setPk(Integer pk) {
        this.pk = pk;
    }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("name: " + name);
		buf.append(", pk: " + pk);
		buf.append(", description: " + description);
		return buf.toString();
	}
    
}
