package org.afscme.enterprise.reporting.base;

import java.io.Serializable;

/**
 * Configuration data for generating PDFs
 */
public class PDFConfigurationData implements Serializable {
	
	/**
	 * the page width for a  output file.
	 */
	protected String pageWidth; //  (in inches)
	
	/**
	 * the page height for a  output file.
	 */
	protected String pageHeight; // (in inches)
	
	/**
	 * the left margin of a  output file.
	 */
	protected String leftMargin; // (in inches)
	
	/**
	 * the right margin of a  output file.
	 */
	protected String rightMargin; //  (in inches)

	/**
	 * the top margin of a  output file.
	 */
	protected String topMargin; // (in inches)

	/**
	 * the bottom margin of a  output file.
	 */
	protected String bottomMargin; //  (in inches)
	
	/**
	 * the before region of a  output file.
	 */
	protected String beforeRegion; // (in inches)
	
	/**
	 * the after region of a  output file.
	 */ 
	protected String afterRegion; //  (in inches)

	/**
	 * the column padding of an output file.
	 */
	protected String columnPadding; // (in inches)
	 
	/**
	 * the page header/footer font of an output file.
	 * can only specifiy "Helvetica", "Times Roman", or "Courier".
	 */
	protected String pageHeaderFooterFont; 
	
	/**
	 * the page header/footer font size of an output file.
	 */
	protected String pageHeaderFooterFontSize; 

	/**
	 * the table column	header font of an output file.
	 * can only specifiy "Helvetica", "Times Roman", or "Courier".
	 */
	protected String tableColumnHeaderFont;  
	
	/**
	 * the table column header font size of an output file.
	 */
	protected String tableColumnHeaderFontSize;  
	
	/**
	 * the table content font of an output file.
	 * can only specifiy "Helvetica", "Times Roman", or "Courier".
	 */
	protected String tableContentFont;  
	
	/**
	 * the table content font size of a output file
	 */
	protected String tableContentFontSize;  

	/**
	 * the flag for hyphenation on or off for a output file
	 */
	protected boolean hyphenationOn; 

	/**
	 * the maximum rows in a page sequence of a output file.
	 */
	protected int maxRowsInSequence;  

    /**
     * the title font
     */
    protected String titleFont;
    
    /**
     * the title font size
     */
    protected String titleFontSize;
    
    /**
     * the content font on the title page
     */
    protected String titleContentFont;
    
    /**
     * the content font size for the title page.
     */
    protected String titleContentFontSize;
    
    /** Getter for property pageWidth.
     * @return Value of property pageWidth.
     */
    public String getPageWidth() {
    	return pageWidth;
    }
    
    /** Setter for perpery pageWidth.
     * @param pageWidth New value of propery pageWidth.
     */
    public void setPageWidth(String pageWidth) {
    	this.pageWidth = pageWidth;
    }
    
    /** Getter for property pageHeight.
     * @return Value of property pageHeight.
     */
    public String getPageHeight() {
    	return pageHeight;
    }
    
    /** Setter for perpery pageHeight.
     * @param pageHeight New value of propery pageHeight.
     */
    public void setPageHeight(String pageHeight) {
    	this.pageHeight = pageHeight;
    }
    
    /** Getter for property leftMargin.
     * @return Value of property leftMargin.
     */
    public String getLeftMargin() {
    	return leftMargin;
    }
    
    /** Setter for perpery pageHeight.
     * @param leftMargin New value of propery leftMargin.
     */
    public void setLeftMargin(String leftMargin) {
    	this.leftMargin = leftMargin;
    }

    /** Getter for property rightMargin.
     * @return Value of property rightMargin.
     */
    public String getRightMargin() {
    	return rightMargin;
    }
    
    /** Setter for perpery rightMargin.
     * @param rightMargin New value of propery rightMargin.
     */
    public void setRightMargin(String rightMargin) {
    	this.rightMargin = rightMargin;
    }

    /** Getter for property topMargin.
     * @return Value of property topMargin.
     */
    public String getTopMargin() {
    	return topMargin;
    }
    
    /** Setter for perpery topMargin.
     * @param topMargin New value of propery topMargin.
     */
    public void setTopMargin(String topMargin) {
    	this.topMargin = topMargin;
    }

    /** Getter for property bottomMargin.
     * @return Value of property bottomMargin.
     */
    public String getBottomMargin() {
    	return bottomMargin;
    }
    
    /** Setter for perpery bottomMargin.
     * @param bottomMargin New value of propery bottomMargin.
     */
    public void setBottomMargin(String bottomMargin) {
    	this.bottomMargin = bottomMargin;
    }

    /** Getter for property beforeRegion.
     * @return Value of property beforeRegion.
     */
    public String getBeforeRegion() {
    	return beforeRegion;
    }
    
    /** Setter for perpery beforeRegion.
     * @param beforeRegion New value of propery beforeRegion.
     */
    public void setBeforeRegion(String beforeRegion) {
    	this.beforeRegion = beforeRegion;
    }
    
    /** Getter for property afterRegion.
     * @return Value of property afterRegion.
     */
    public String getAfterRegion() {
    	return afterRegion;
    }
    
    /** Setter for perpery afterRegion.
     * @param afterRegion New value of propery afterRegion.
     */
    public void setAfterRegion(String afterRegion) {
    	this.afterRegion = afterRegion;
    }
    
    /** Getter for property columnPadding.
     * @return Value of property columnPadding.
     */
    public String getColumnPadding() {
    	return columnPadding;
    }
    
    /** Setter for property columnPadding.
     * @param columnPadding New value of property columnPadding.
     */
    public void setColumnPadding(String columnPadding) {
    	this.columnPadding = columnPadding;
    }
    
    /** Getter for property pageHeaderFooterFont.
     * @return Value of property pageHeaderFooterFont.
     */
    public String getPageHeaderFooterFont() {
    	return pageHeaderFooterFont;
    }
    
    /** Setter for property pageHeaderFooterFont.
     * @param pageHeaderFooterFont New value of property pageHeaderFooterFont.
     */
    public void setPageHeaderFooterFont(String pageHeaderFooterFont) {
    	this.pageHeaderFooterFont = pageHeaderFooterFont;
    }
    
    /** Getter for property pageHeaderFooterFontSize.
     * @return Value of property pageHeaderFooterFontSize.
     */
    public String getPageHeaderFooterFontSize() {
    	return pageHeaderFooterFontSize;
    }
    
    /** Setter for property pageHeaderFooterFontSize.
     * @param pageHeaderFooterFontSize New value of property pageHeaderFooterFontSize.
     */
    public void setPageHeaderFooterFontSize(String pageHeaderFooterFontSize) {
    	this.pageHeaderFooterFontSize = pageHeaderFooterFontSize;
    }
    
    /** Getter for property tableColumnHeaderFont.
     * @return Value of property tableColumnHeaderFont.
     */
    public String getTableColumnHeaderFont() {
    	return tableColumnHeaderFont;
    }
    
    /** Setter for property tableColumnHeaderFont.
     * @param tableColumnHeaderFont New value of property tableColumnHeaderFont.
     */
    public void setTableColumnHeaderFont(String tableColumnHeaderFont) {
    	this.tableColumnHeaderFont = tableColumnHeaderFont;
    }
    
    /** Getter for property tableContentFont.
     * @return Value of property tableContentFont.
     */
    public String getTableContentFont() {
    	return tableContentFont;
    }
    
    /** Setter for property tableContentFont.
     * @param tableContentFont New value of property tableContentFont.
     */
    public void setTableContentFont(String tableContentFont) {
    	this.tableContentFont = tableContentFont;
    }
    
    /** Getter for property tableContentFontSize.
     * @return Value of property tableContentFontSize.
     */
    public String getTableContentFontSize() {
    	return tableContentFontSize;
    }
    
    /** Setter for property tableContentFontSize.
     * @param tableContentFontSize New value of property tableContentFontSize.
     */
    public void setTableContentFontSize(String tableContentFontSize) {
    	this.tableContentFontSize = tableContentFontSize;
    }
    
    /** Getter for property hyphenationOn.
     * @return Value of property hyphenationOn.
     */
    public boolean isHyphenationOn() {
    	return hyphenationOn;
    }
    
    /** Setter for property hyphenationOn.
     * @param hyphenationOn New value of property hyphenationOn.
     */
    public void setHyphenationOn(boolean hyphenationOn) {
    	this.hyphenationOn = hyphenationOn;
    }
    
    /** Getter for property maxRowsInSequence.
     * @return Value of property maxRowsInSequence.
     */
    public int getMaxRowsInSequence() {
    	return maxRowsInSequence;
    }
    
    /** Setter for property maxRowsInSequence.
     * @param maxRowsInSequence New value of property maxRowsInSequence.
     */
    public void setMaxRowsInSequence(int maxRowsInSequence) {
    	this.maxRowsInSequence = maxRowsInSequence;
    }
    
    /** Getter for property tableColumnHeaderFontSize.
     * @return Value of property tableColumnHeaderFontSize.
     */
    public java.lang.String getTableColumnHeaderFontSize() {
        return tableColumnHeaderFontSize;
    }
    
    /** Setter for property tableColumnHeaderFontSize.
     * @param tableColumnHeaderFontSize New value of property tableColumnHeaderFontSize.
     */
    public void setTableColumnHeaderFontSize(java.lang.String tableColumnHeaderFontSize) {
        this.tableColumnHeaderFontSize = tableColumnHeaderFontSize;
    }
    
    /** Getter for property titleFont.
     * @return Value of property titleFont.
     */
    public java.lang.String getTitleFont() {
        return titleFont;
    }
    
    /** Setter for property titleFont.
     * @param titleFont New value of property titleFont.
     */
    public void setTitleFont(java.lang.String titleFont) {
        this.titleFont = titleFont;
    }
    
    /** Getter for property titleFontSize.
     * @return Value of property titleFontSize.
     */
    public java.lang.String getTitleFontSize() {
        return titleFontSize;
    }
    
    /** Setter for property titleFontSize.
     * @param titleFontSize New value of property titleFontSize.
     */
    public void setTitleFontSize(java.lang.String titleFontSize) {
        this.titleFontSize = titleFontSize;
    }
        
    /** Getter for property titleContentFont.
     * @return Value of property titleContentFont.
     */
    public java.lang.String getTitleContentFont() {
        return titleContentFont;
    }
    
    /** Setter for property titleContentFont.
     * @param titleContentFont New value of property titleContentFont.
     */
    public void setTitleContentFont(java.lang.String titleContentFont) {
        this.titleContentFont = titleContentFont;
    }
    
    /** Getter for property titleContentFontSize.
     * @return Value of property titleContentFontSize.
     */
    public java.lang.String getTitleContentFontSize() {
        return titleContentFontSize;
    }
    
    /** Setter for property titleContentFontSize.
     * @param titleContentFontSize New value of property titleContentFontSize.
     */
    public void setTitleContentFontSize(java.lang.String titleContentFontSize) {
        this.titleContentFontSize = titleContentFontSize;
    }
    
}
