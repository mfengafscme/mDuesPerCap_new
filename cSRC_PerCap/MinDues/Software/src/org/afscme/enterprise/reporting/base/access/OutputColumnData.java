/*
 * This class is used for PDF formatter. 
 */

package org.afscme.enterprise.reporting.base.access;

public class OutputColumnData {

	private String columnName;  // the display name
	private float columnWidth;
	
	public OutputColumnData(String columnName, float columnWidth) {
		this.columnName = columnName;
		this.columnWidth = columnWidth;
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public float getColumnWidth() {
		return columnWidth;
	}
	
	public void setColumnWidth(float columnWidth) {
		this.columnWidth = columnWidth;
	}
	
}