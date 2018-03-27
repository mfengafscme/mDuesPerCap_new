package org.afscme.enterprise.update;


/**
 * Holds the data of a change in an individual field
 */
public class FieldChange {
    
    /**
     * Name of the field
     */
    protected String name;
    
    /**
     * Number of changes in that field.
     */
    protected int count;
    
    public FieldChange(){}
    
    public FieldChange(String name) {
        this.name = name;
        this.count = 0;
    }
    
    public String toString() {
        return  "FieldChange [" +
                "name=" + this.name +
                ", count=" + this.count +
                "]"
        ;
    }
    
    /** Getter for property count.
     * @return Value of property count.
     *
     */
    public int getCount() {
        return count;
    }
    
    /** Setter for property count.
     * @param count New value of property count.
     *
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    public void incrementCount() {
        this.count += 1;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
