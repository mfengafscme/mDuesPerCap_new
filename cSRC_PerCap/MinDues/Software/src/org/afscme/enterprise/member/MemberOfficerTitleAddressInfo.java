/*
 * MemberOfficerTitleAddressInfo.java
 *
 * Created on May 20, 2003, 2:09 PM
 */

package org.afscme.enterprise.member;

/**
 * Contains information on title and address information for a member
 * who is also an officer. 
 * 
 * @author  gdecorte
 */
public class MemberOfficerTitleAddressInfo {
    
    
    protected Integer posAddrFromPersonPk;
    protected Integer personAddrType;
    
    protected Integer posAddrFromOrgPk;
    protected String orgAddrLocationNm;
    
    protected Boolean mbrSuspendedFg;
    
    /** a code value is stored in the string field in the database, but the codeWriteTag needs an 
    * needs an Integer for a PK to work correctly. so stored here as an Integer to support UI 
    */
    protected Integer afscmeTitleNm = null;
    
    
    
    /** Creates a new instance of MemberOfficerTitleAddressInfo */
    public MemberOfficerTitleAddressInfo() {
   
    
    }
    
    /** Getter for property posAddrFromPersonPk.
     * @return Value of property posAddrFromPersonPk.
     *
     */
    public java.lang.Integer getPosAddrFromPersonPk() {
        return this.posAddrFromPersonPk;
    }
    
    /** Setter for property posAddrFromPersonPk.
     * @param posAddrFromPersonPk New value of property posAddrFromPersonPk.
     *
     */
    public void setPosAddrFromPersonPk(java.lang.Integer posAddrFromPersonPk) {
        this.posAddrFromPersonPk = posAddrFromPersonPk;
    }
    
    /** Getter for property personAddrType.
     * @return Value of property personAddrType.
     *
     */
    public java.lang.Integer getPersonAddrType() {
        return personAddrType;
    }
    
    /** Setter for property personAddrType.
     * @param personAddrType New value of property personAddrType.
     *
     */
    public void setPersonAddrType(java.lang.Integer personAddrType) {
        this.personAddrType = personAddrType;
    }
    
    /** Getter for property posAddrFromOrgPk.
     * @return Value of property posAddrFromOrgPk.
     *
     */
    public java.lang.Integer getPosAddrFromOrgPk() {
        return this.posAddrFromOrgPk;
    }
    
    /** Setter for property posAddrFromOrgPk.
     * @param posAddrFromOrgPk New value of property posAddrFromOrgPk.
     *
     */
    public void setPosAddrFromOrgPk(java.lang.Integer posAddrFromOrgPk) {
        this.posAddrFromOrgPk = posAddrFromOrgPk;
    }
    
    /** Getter for property orgAddrLocationNm.
     * @return Value of property orgAddrLocationNm.
     *
     */
    public java.lang.String getOrgAddrLocationNm() {
        return this.orgAddrLocationNm;
    }
    
    /** Setter for property orgAddrLocationNm.
     * @param orgAddrLocationNm New value of property orgAddrLocationNm.
     *
     */
    public void setOrgAddrLocationNm(java.lang.String orgAddrLocationNm) {
        this.orgAddrLocationNm = orgAddrLocationNm;
    }
    
    /** Getter for property mbrSuspendedFg.
     * @return Value of property mbrSuspendedFg.
     *
     */
    public Boolean getMbrSuspendedFg() {
        return this.mbrSuspendedFg;
    }
    
    /** Setter for property mbrSuspendedFg.
     * @param mbrSuspendedFg New value of property mbrSuspendedFg.
     *
     */
    public void setMbrSuspendedFg(Boolean mbrSuspendedFg) {
        this.mbrSuspendedFg = mbrSuspendedFg;
    }
    
    /** Getter for property afscmeTitleNm.
     * @return Value of property afscmeTitleNm.
     *
     */
    public java.lang.Integer getAfscmeTitleNm() {
          
            return this.afscmeTitleNm;
               
    }
    
    /** Setter for property afscmeTitleNm.
     * @param afscmeTitleNm New value of property afscmeTitleNm.
     *
     */
    public void setAfscmeTitleNm(java.lang.Integer afscmeTitleNm) {
        this.afscmeTitleNm = afscmeTitleNm;
    }
    
}
