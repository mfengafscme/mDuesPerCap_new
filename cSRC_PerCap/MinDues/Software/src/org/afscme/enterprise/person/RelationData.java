package org.afscme.enterprise.person;

import java.sql.Timestamp;

/**
 * Data about an individual associated with a person.  This can be a child, spouse
 * or domestic partner.
 */
public class RelationData
{
    private String relativeFirstNm;
    private String relativeMiddleNm;
    private String relativeLastNm;
    private Integer relativeSuffixNm;
    private Timestamp relativeBirthDt;
    private Integer personRelativeType;
    private Integer relativePk;

    /** Getter for property personRelativeType.
     * @return Value of property personRelativeType.
     *
     */
    public Integer getPersonRelativeType() {
        return personRelativeType;
    }

    /** Setter for property personRelativeType.
     * @param personRelativeType New value of property personRelativeType.
     *
     */
    public void setPersonRelativeType(Integer personRelativeType) {
        this.personRelativeType = personRelativeType;
    }

    /** Getter for property relativeBirthDt.
     * @return Value of property relativeBirthDt.
     *
     */
    public java.sql.Timestamp getRelativeBirthDt() {
        return relativeBirthDt;
    }

    /** Setter for property relativeBirthDt.
     * @param relativeBirthDt New value of property relativeBirthDt.
     *
     */
    public void setRelativeBirthDt(java.sql.Timestamp relativeBirthDt) {
        this.relativeBirthDt = relativeBirthDt;
    }

    /** Getter for property relativeFirstNm.
     * @return Value of property relativeFirstNm.
     *
     */
    public java.lang.String getRelativeFirstNm() {
        return relativeFirstNm;
    }

    /** Setter for property relativeFirstNm.
     * @param relativeFirstNm New value of property relativeFirstNm.
     *
     */
    public void setRelativeFirstNm(java.lang.String relativeFirstNm) {
        this.relativeFirstNm = relativeFirstNm;
    }

    /** Getter for property relativeLastNm.
     * @return Value of property relativeLastNm.
     *
     */
    public java.lang.String getRelativeLastNm() {
        return relativeLastNm;
    }

    /** Setter for property relativeLastNm.
     * @param relativeLastNm New value of property relativeLastNm.
     *
     */
    public void setRelativeLastNm(java.lang.String relativeLastNm) {
        this.relativeLastNm = relativeLastNm;
    }

    /** Getter for property relativeMiddleNm.
     * @return Value of property relativeMiddleNm.
     *
     */
    public java.lang.String getRelativeMiddleNm() {
        return relativeMiddleNm;
    }

    /** Setter for property relativeMiddleNm.
     * @param relativeMiddleNm New value of property relativeMiddleNm.
     *
     */
    public void setRelativeMiddleNm(java.lang.String relativeMiddleNm) {
        this.relativeMiddleNm = relativeMiddleNm;
    }

    /** Getter for property relativeSuffixNm.
     * @return Value of property relativeSuffixNm.
     *
     */
    public Integer getRelativeSuffixNm() {
        return relativeSuffixNm;
    }

    /** Setter for property relativeSuffixNm.
     * @param relativeSuffixNm New value of property relativeSuffixNm.
     *
     */
    public void setRelativeSuffixNm(Integer relativeSuffixNm) {
        this.relativeSuffixNm = relativeSuffixNm;
    }

    /** Getter for property relativePk.
	 * @return Value of property relativePk.
	 *
	 */
	public java.lang.Integer getRelativePk() {
		return relativePk;
	}

    /** Setter for property relativePk.
	 * @param relativePk New value of property relativePk.
	 *
	 */
	public void setRelativePk(java.lang.Integer relativePk) {
		this.relativePk = relativePk;
    }
}
