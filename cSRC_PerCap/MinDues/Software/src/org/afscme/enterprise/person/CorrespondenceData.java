package org.afscme.enterprise.person;

import java.sql.Timestamp;

/**
 * Data about an individual correspondence item for a person.
 */
public class CorrespondenceData
{
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_NAME = 2;
    
    private Timestamp correspondenceDt;
    private Integer mlbpMailingListPk;
    private String correspondenceName;
    
    /** Getter for property correspondenceDt.
     * @return Value of property correspondenceDt.
     *
     */
    public java.sql.Timestamp getCorrespondenceDt() {
        return correspondenceDt;
    }

    /** Setter for property correspondenceDt.
     * @param correspondenceDt New value of property correspondenceDt.
     *
     */
    public void setCorrespondenceDt(java.sql.Timestamp correspondenceDt) {
        this.correspondenceDt = correspondenceDt;
    }

    /** Getter for property mlbpMailingListPk.
     * @return Value of property mlbpMailingListPk.
     *
     */
    public java.lang.Integer getMlbpMailingListPk() {
        return mlbpMailingListPk;
    }

    /** Setter for property mlbpMailingListPk.
     * @param mlbpMailingListPk New value of property mlbpMailingListPk.
     *
     */
    public void setMlbpMailingListPk(java.lang.Integer mlbpMailingListPk) {
        this.mlbpMailingListPk = mlbpMailingListPk;
    }

    /** Getter for property correspondenceName.
	 * @return Value of property correspondenceName.
	 *
     */
	public java.lang.String getCorrespondenceName() {
	    return correspondenceName;
	}

	/** Setter for property correspondenceName.
	 * @param correspondenceName New value of property correspondenceName.
	 *
	 */
	public void setCorrespondenceName(java.lang.String correspondenceName) {
		this.correspondenceName = correspondenceName;
    }
}
