package org.afscme.enterprise.affiliate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.ArrayList;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * Affiliate charter data
 */
public class CharterData 
{
    private Integer affPk;
    private String name;
    private String jurisdiction;
    private Integer charterCodeCodePk;
    private Timestamp charterDate;
    private Timestamp lastChangeEffectiveDate;
    private Integer statusCodePk;
    
    /** List of Affiliate Pks for the Councils with which this Affiliate is associated. */
    public Collection councilAffiliations;
    /** List of Strings representing the 3 counties associated with this Charter. */
    private Collection counties;
    
    /** Affiliate's Type. Needed for business rules not for displaying. */
    private Character affIdType;
    
    /** PK of the nearest Council above this Affiliate in the hierarchy. */
    private Integer reportingCouncilPk;
    
    public CharterData() {
        this.affPk = null;
        this.name = null;
        this.jurisdiction = null;
        this.statusCodePk = null;
        this.charterCodeCodePk = null;
        this.charterDate = null;
        this.lastChangeEffectiveDate = null;
        this.councilAffiliations = null;
        this.counties = null;
        this.reportingCouncilPk = null;
        this.affIdType = null;
    }
    
// General Methods...
    
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", charterCodeCodePk = ");
        sb.append(this.charterCodeCodePk);
        sb.append(", statusCodePk = ");
        sb.append(this.statusCodePk);
        sb.append(", jurisdiction = ");
        sb.append(this.jurisdiction);
        sb.append(", name = ");
        sb.append(this.name);
        sb.append(", reportingCouncilPk = ");
        sb.append(this.reportingCouncilPk);
        sb.append(", affIdType = ");
        sb.append(this.affIdType);
        sb.append(", charterDate = ");
        sb.append(DateUtil.getSimpleDateString(this.charterDate));
        sb.append(", lastChangeEffectiveDate = ");
        sb.append(DateUtil.getSimpleDateString(this.lastChangeEffectiveDate));
        sb.append(", councilAffiliations = ");
        sb.append(CollectionUtil.toString(this.councilAffiliations));
        sb.append(", counties = ");
        sb.append(CollectionUtil.toString(this.counties));
        sb.append("}");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property charterCodeCodePk.
     * @return Value of property charterCodeCodePk.
     *
     */
    public Integer getCharterCodeCodePk() {
        return charterCodeCodePk;
    }
    
    /** Setter for property charterCodeCodePk.
     * @param charterCodeCodePk New value of property charterCodeCodePk.
     *
     */
    public void setCharterCodeCodePk(Integer charterCodeCodePk) {
        this.charterCodeCodePk = charterCodeCodePk;
    }
    
    /** Getter for property charterDate.
     * @return Value of property charterDate.
     *
     */
    public Timestamp getCharterDate() {
        return charterDate;
    }
    
    /** Setter for property charterDate.
     * @param charterDate New value of property charterDate.
     *
     */
    public void setCharterDate(Timestamp charterDate) {
        this.charterDate = charterDate;
    }
    
    /** Getter for property councilAffiliations.
     * @return Value of property councilAffiliations.
     *
     */
    public Collection getCouncilAffiliations() {
        return councilAffiliations;
    }
    
    /** Setter for property councilAffiliations.
     * @param councilAffiliations New value of property councilAffiliations.
     *
     */
    public void setCouncilAffiliations(Collection councilAffiliations) {
        if (CollectionUtil.isEmpty(councilAffiliations)) {
            this.councilAffiliations = null;
        } else {
            this.councilAffiliations = new ArrayList(councilAffiliations);
        }
    }
    
    /** Getter for property jurisdiction.
     * @return Value of property jurisdiction.
     *
     */
    public String getJurisdiction() {
        return jurisdiction;
    }
    
    /** Setter for property jurisdiction.
     * @param jurisdiction New value of property jurisdiction.
     *
     */
    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }
    
    /** Getter for property lastChangeEffectiveDate.
     * @return Value of property lastChangeEffectiveDate.
     *
     */
    public Timestamp getLastChangeEffectiveDate() {
        return lastChangeEffectiveDate;
    }
    
    /** Setter for property lastChangeEffectiveDate.
     * @param lastChangeEffectiveDate New value of property lastChangeEffectiveDate.
     *
     */
    public void setLastChangeEffectiveDate(Timestamp lastChangeEffectiveDate) {
        this.lastChangeEffectiveDate = lastChangeEffectiveDate;
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
    
    /** Getter for property counties.
     * @return Value of property counties.
     *
     */
    public Collection getCounties() {
        return counties;
    }
    
    /** Setter for property counties.
     * @param counties New value of property counties.
     *
     */
    public void setCounties(Collection counties) {
        if (CollectionUtil.isEmpty(counties)) {
            this.counties = null;
        } else {
            this.counties = new ArrayList(counties);
        }
    }
    
    public void addCounty(String county) {
        if (!TextUtil.isEmptyOrSpaces(county)) {
            if (this.counties == null) {
                this.counties = new ArrayList();
            }
            this.counties.add(county);
        }
    }
    
    /** Getter for property statusCodePk.
     * @return Value of property statusCodePk.
     *
     */
    public Integer getStatusCodePk() {
        return statusCodePk;
    }
    
    /** Setter for property statusCodePk.
     * @param statusCodePk New value of property statusCodePk.
     *
     */
    public void setStatusCodePk(Integer statusCodePk) {
        this.statusCodePk = statusCodePk;
    }
    
    /** Getter for property affIdType.
     * @return Value of property affIdType.
     *
     */
    public Character getAffIdType() {
        return affIdType;
    }
    
    /** Setter for property affIdType.
     * @param affIdType New value of property affIdType.
     *
     */
    public void setAffIdType(Character affIdType) {
        this.affIdType = affIdType;
    }
    
    /** Getter for property reportingCouncilPk.
     * @return Value of property reportingCouncilPk.
     *
     */
    public Integer getReportingCouncilPk() {
        return reportingCouncilPk;
    }
    
    /** Setter for property reportingCouncilPk.
     * @param reportingCouncilPk New value of property reportingCouncilPk.
     *
     */
    public void setReportingCouncilPk(Integer reportingCouncilPk) {
        this.reportingCouncilPk = reportingCouncilPk;
    }
    
}
