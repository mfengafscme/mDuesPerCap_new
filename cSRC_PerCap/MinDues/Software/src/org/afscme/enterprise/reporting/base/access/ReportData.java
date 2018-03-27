/*
 * @(#)ReportData.java
 *
 * Copyright (c) 2002 AFSCME org.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of AFSCME
 * Orgnization. ("Confidential Information"). 
 */
 
package org.afscme.enterprise.reporting.base.access;

import java.io.Serializable;
import java.sql.Timestamp;
import org.afscme.enterprise.util.TextUtil;

public class ReportData implements Serializable {
    protected Integer pk;
	protected String name;
	protected String description;
	protected boolean canAddEntities;
	protected boolean mailingList;
	protected boolean custom;
	protected boolean needUpdateCorrespondence;
	protected String customHandlerClassName;
	protected boolean countReport;
	protected String lastUpdateUID;
	protected Timestamp lastUpdateDate;
    protected Integer ownerPK;
	
	public ReportData() {}
	
	public ReportData(Integer pk,
                      String name,
					  String description,
					  boolean canAddEntities,
					  boolean mailingList,
					  boolean custom,
					  boolean needUpdateCorrespondence,
					  String customHandlerClassName,
					  boolean countReport,
					  String lastUpdateUID,
					  Timestamp lastUpdateDate,
                      Integer ownerPK) {
        this.pk = pk;
		this.name = name;
		this.description = description;
		this.canAddEntities = canAddEntities;
		this.mailingList = mailingList;
		this.custom = custom;
		this.needUpdateCorrespondence = needUpdateCorrespondence;
		this.customHandlerClassName = customHandlerClassName;
		this.countReport = countReport;
		this.lastUpdateUID = lastUpdateUID;
		this.lastUpdateDate = lastUpdateDate;
        this.ownerPK = ownerPK;
	}
    
    /** Getter for property pk.
     * @return Value of property pk.
     */
    public java.lang.Integer getPk() {
        return pk;
    }
    
    /** Setter for property pk.
     * @param pk New value of property pk.
     */
    public void setPk(java.lang.Integer pk) {
        this.pk = pk;
    }
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean getCanAddEntities() {
		return canAddEntities;
	}
	
	public void setCanAddEntities(boolean canAddEntities) {
		this.canAddEntities = canAddEntities;
	}
	
	public boolean isMailingList() {
		return mailingList;
	}
	
	public void setMailingList(boolean mailingList) {
		this.mailingList = mailingList;
	}
	
	public boolean isCustom() {
		return custom;
	}
	
	public void setCustom(boolean custom) {
		this.custom = custom;
	}
	
	public boolean getNeedUpdateCorrespondence() {
		return needUpdateCorrespondence;
	}
	
	public void setNeedUpdateCorrespondence(boolean needUpdateCorrespondence) {
		this.needUpdateCorrespondence = needUpdateCorrespondence;
	}
	
	public String getCustomHandlerClassName() {
		return customHandlerClassName;
	}
	
	public void setCustomHandlerClassName(String customHandlerClassName) {
		this.customHandlerClassName = customHandlerClassName;
	}
	
	public boolean isCountReport() {
		return countReport;
	}
	
	public void setCountReport(boolean countReport) {
		this.countReport = countReport;
	}
	
	public String getLastUpdateUID() {
		return lastUpdateUID;
	}
	
	public void setLastUpdateUID(String lastUpdateUID) {
		this.lastUpdateUID = lastUpdateUID;
	}
	
	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setLastUpateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
    
    /** Getter for property ownerPK.
     * @return Value of property ownerPK.
     */
    public java.lang.Integer getOwnerPK() {
        return ownerPK;
    }
    
    /** Setter for property ownerPK.
     * @param ownerPK New value of property ownerPK.
     */
    public void setOwnerPK(java.lang.Integer ownerPK) {
        this.ownerPK = ownerPK;
    }
    
}