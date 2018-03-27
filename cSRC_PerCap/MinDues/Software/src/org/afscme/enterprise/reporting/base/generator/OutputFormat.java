/*
 * OutputFormat.java
 *
 * Specifies the output format for report generation.
 */

package org.afscme.enterprise.reporting.base.generator;


import java.io.Serializable;


public class OutputFormat implements Serializable {
    
    public static final int TAB = 0;
    
    public static final int COMMA = 1;
    
    public static final int SEMICOLON = 2;
    
    public static final int PDF = 3;
    
    public static final int MAIL_MERGE = 4;
    
    public static final int MAILING_HOUSE = 5;
    
    public static final int LABEL = 6;
    
    public static final int SPECIALIZED = 7;
    
    
    protected int format = PDF;
    
    /** Creates a new instance of OutputFormat */
    public OutputFormat() {
    }
    
    public OutputFormat(int format) {
        this.format = format;
    }
    
    /** Getter for property format.
     * @return Value of property format.
     */
    public int getFormat() {
        return format;
    }    
    
    /** Setter for property format.
     * @param format New value of property format.
     */
    public void setFormat(int format) {
        this.format = format;
    }
    
    /** get the printable format */
    public String getFormatString() {
        String ft = "PDF";
        
        switch (format) {
            case TAB:
                ft = "TAB";
                break;
            case COMMA:
                ft = "COMMA";
                break;
            case SEMICOLON:
                ft = "SEMICOLON";
                break;
            case PDF:
                ft = "PDF";
                break;
            case MAIL_MERGE:
                ft = "MAIL_MERGE";
                break;
            case MAILING_HOUSE:
                ft = "MAILING_HOUSE";
                break;
            case LABEL:
                ft = "LABEL";
                break;
            case SPECIALIZED:
                ft = "SPECIALIZED";
                break;
        }
        
        return ft;
    }
    
    
}
