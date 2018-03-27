/*
 * LabelConfigurationData.java
 *
 * Use to store configurable data for label formatter
 */

package org.afscme.enterprise.reporting.base;

import java.io.Serializable;

public class LabelConfigurationData implements Serializable {
    
    /** The number of labels horizontally on a page */
    protected int labelsPerLine;
    
    /** The number of labels vertically on a page */
    protected int linesPerPage;
    
    /** The total number of characters (width) for a label before
     * the next label starts.
     */ 
    protected int labelWidth;
    
    /** The number of characters the "city" data field will occupy */
    protected int cityWidth;
  
    /** Getter for property labelsPerLine.
     * @return Value of property labelsPerLine.
     */
    public int getLabelsPerLine() {
        return labelsPerLine;
    }    
    
    /** Setter for property labelsPerLine.
     * @param labelsPerLine New value of property labelsPerLine.
     */
    public void setLabelsPerLine(int labelsPerLine) {
        this.labelsPerLine = labelsPerLine;
    }
    
    /** Getter for property linesPerPage.
     * @return Value of property linesPerPage.
     */
    public int getLinesPerPage() {
        return linesPerPage;
    }
    
    /** Setter for property linesPerPage.
     * @param linesPerPage New value of property linesPerPage.
     */
    public void setLinesPerPage(int linesPerPage) {
        this.linesPerPage = linesPerPage;
    }
    
    /** Getter for property labelWidth.
     * @return Value of property labelWidth.
     */
    public int getLabelWidth() {
        return labelWidth;
    }
    
    /** Setter for property labelWidth.
     * @param labelWidth New value of property labelWidth.
     */
    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }
    
    /** Getter for property cityWidth.
     * @return Value of property cityWidth.
     */
    public int getCityWidth() {
        return cityWidth;
    }
    
    /** Setter for property cityWidth.
     * @param cityWidth New value of property cityWidth.
     */
    public void setCityWidth(int cityWidth) {
        this.cityWidth = cityWidth;
    }
    
}
