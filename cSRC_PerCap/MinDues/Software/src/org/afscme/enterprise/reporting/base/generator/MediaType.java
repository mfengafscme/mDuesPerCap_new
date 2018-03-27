/*
 * MediaType.java
 *
 * This is used for specifying the output media type when generating a report.
 */

package org.afscme.enterprise.reporting.base.generator;


import java.io.Serializable;


public class MediaType implements Serializable {
    
    public static final String SCREEN = "SCREEN";
    
    public static final String PRINT = "PRINT";
    
    public static final String CD = "CD";
    
    public static final String DISKETTE = "DISKETTE";
    
    public static final String TAPE = "TAPE";
    
    public static final String MAILING = "MAILING_LIST";
    
    public static final String SAVE = "SAVE";
    
    protected String type = CD;
    
    /** Creates a new instance of MediaType */
    public MediaType() {
    }
    
    public MediaType(String type) {
        this.type = type;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     */
    public String getType() {
        return type;
    }    
    
    /** Setter for property type.
     * @param type New value of property type.
     */
    public void setType(String type) {
        this.type = type;
    }    
   
}
