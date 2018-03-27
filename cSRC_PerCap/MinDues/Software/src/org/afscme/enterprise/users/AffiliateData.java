package org.afscme.enterprise.users;

import java.io.Serializable;

/**
 * Basic info about an affiilate.
 */
public class AffiliateData implements Serializable
{
	
	/**
	 * primary key
	 */
	protected Integer pk;
	
	/**
	 * Affiliate type code.
	 */
	protected Character type;
	
	/**
	 * local number
	 */
	protected String local;
	
	/**
	 * primary key of the affiliate state code.
	 */
	protected String state;
	
	/**
	 * sub unit number.
	 */
	protected String subUnit;
	
	/**
	 * council number
	 */
	protected String council;
	
	/**
	 * Abbreviated name of the affiliate
     */
	protected String name;

    /**
	 * True iff the user can act as the 'affiliate user' for this affiliate.
	 */
	protected Boolean selected;
    
    /** Getter for property code.
     * @return Value of property code.
     */
    public String getLocal() {
        return local;
    }
    
    /** Setter for property code.
     * @param code New value of property code.
     */
    public void setLocal(String local) {
        this.local = local;
    }
    
    /** Getter for property council.
     * @return Value of property council.
     */
    public String getCouncil() {
        return council;
    }
    
    /** Setter for property council.
     * @param council New value of property council.
     */
    public void setCouncil(String council) {
        this.council = council;
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
    
    /** Getter for property selected.
     * @return Value of property selected.
     */
    public Boolean isSelected() {
        return selected;
    }
    
    /** Setter for property selected.
     * @param selected New value of property selected.
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     */
    public String getState() {
        return state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     */
    public void setState(String state) {
        this.state = state;
    }
    
    /** Getter for property subUnit.
     * @return Value of property subUnit.
     */
    public String getSubUnit() {
        return subUnit;
    }
    
    /** Setter for property subUnit.
     * @param subUnit New value of property subUnit.
     */
    public void setSubUnit(String subUnit) {
        this.subUnit = subUnit;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     */
    public Character getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     */
    public void setType(Character type) {
        this.type = type;
    }
	
	public AffiliateData copy() {
		AffiliateData copy = new AffiliateData();
		copy.pk = pk;
		copy.state = state;
		copy.type = type;
		copy.local = local;
		copy.council = council;
		copy.subUnit = subUnit;
		
		return copy;
	}
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
}
