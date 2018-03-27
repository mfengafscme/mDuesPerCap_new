package org.afscme.enterprise.cards;

import java.sql.Timestamp;

/**
 * Represents the totals that will be performed for each affiliate and run type.
 */
public class RunSummary {
    
    /**
     * Total number of members in the affiliate. Total Members
     */
    public int totalMembers;
    
    /**
     * Number of cards that will be generated in this run.
     */
    public int cardsToGenerate;
    
    /**
     * Number of cards that will not be generated because the member did not have a
     * mailable address.
     */
    public int nonMailable;
    
    /**
     * Primary key of the affiliate this run summary is for.
     */
    public Integer affPk;
    
    public String affType;
    
    public String afflocalSubChapter;
    
    public String affCode;
    
    public String affStateNatType;
    
    public String affsubUnit;
    
    public String councilRetireeChap;
    
    public Timestamp amcCardRunDt;
    
    
    
    /**
     * Common code primary key for the 'CardRunType' common code type
     */
    public Integer runType;
    
    /** Getter for property affCode.
     * @return Value of property affCode.
     *
     */
    
    public String toString() {
        return "RunSummary[" +
            "affPk="+affPk+", "+
            "affType="+affType+", "+
            "afflocalSubChapter="+afflocalSubChapter+","+
            "affCode="+   affCode +"," +
            "affStateNatType="+   affStateNatType + "," +
            "affsubUnit+"+affsubUnit+ "," +
            "councilRetireeChap="+councilRetireeChap+ ","  +
            "runType+"+runType+","+
            "totalMembers="+totalMembers+","+
            "cardsToGenerate="+cardsToGenerate+"," +
            "nonMailable="+nonMailable+"," +
            "amcCardRunDt="+amcCardRunDt+",";
    }
            
            
    
    
    public java.lang.String getAffCode() {
        return affCode;
    }
    
    /** Setter for property affCode.
     * @param affCode New value of property affCode.
     *
     */
    public void setAffCode(java.lang.String affCode) {
        this.affCode = affCode;
    }
    
    /** Getter for property afflocalSubChapter.
     * @return Value of property afflocalSubChapter.
     *
     */
    public java.lang.String getAfflocalSubChapter() {
        return afflocalSubChapter;
    }
    
    /** Setter for property afflocalSubChapter.
     * @param afflocalSubChapter New value of property afflocalSubChapter.
     *
     */
    public void setAfflocalSubChapter(java.lang.String afflocalSubChapter) {
        this.afflocalSubChapter = afflocalSubChapter;
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public java.lang.Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(java.lang.Integer affPk) {
        this.affPk = affPk;
    }
    
    /** Getter for property affStateNatType.
     * @return Value of property affStateNatType.
     *
     */
    public java.lang.String getAffStateNatType() {
        return affStateNatType;
    }
    
    /** Setter for property affStateNatType.
     * @param affStateNatType New value of property affStateNatType.
     *
     */
    public void setAffStateNatType(java.lang.String affStateNatType) {
        this.affStateNatType = affStateNatType;
    }
    
    /** Getter for property affsubUnit.
     * @return Value of property affsubUnit.
     *
     */
    public java.lang.String getAffsubUnit() {
        return affsubUnit;
    }
    
    /** Setter for property affsubUnit.
     * @param affsubUnit New value of property affsubUnit.
     *
     */
    public void setAffsubUnit(java.lang.String affsubUnit) {
        this.affsubUnit = affsubUnit;
    }
    
    /** Getter for property affType.
     * @return Value of property affType.
     *
     */
    public java.lang.String getAffType() {
        return affType;
    }
    
    /** Setter for property affType.
     * @param affType New value of property affType.
     *
     */
    public void setAffType(java.lang.String affType) {
        this.affType = affType;
    }
    
    /** Getter for property cardsToGenerate.
     * @return Value of property cardsToGenerate.
     *
     */
    public int getCardsToGenerate() {
        return cardsToGenerate;
    }
    
    /** Setter for property cardsToGenerate.
     * @param cardsToGenerate New value of property cardsToGenerate.
     *
     */
    public void setCardsToGenerate(int cardsToGenerate) {
        this.cardsToGenerate = cardsToGenerate;
    }
    
    /** Getter for property councilRetireeChap.
     * @return Value of property councilRetireeChap.
     *
     */
    public java.lang.String getCouncilRetireeChap() {
        return councilRetireeChap;
    }
    
    /** Setter for property councilRetireeChap.
     * @param councilRetireeChap New value of property councilRetireeChap.
     *
     */
    public void setCouncilRetireeChap(java.lang.String councilRetireeChap) {
        this.councilRetireeChap = councilRetireeChap;
    }
    
    /** Getter for property nonMailable.
     * @return Value of property nonMailable.
     *
     */
    public int getNonMailable() {
        return nonMailable;
    }
    
    /** Setter for property nonMailable.
     * @param nonMailable New value of property nonMailable.
     *
     */
    public void setNonMailable(int nonMailable) {
        this.nonMailable = nonMailable;
    }
    
    /** Getter for property runType.
     * @return Value of property runType.
     *
     */
    public java.lang.Integer getRunType() {
        return runType;
    }
    
    /** Setter for property runType.
     * @param runType New value of property runType.
     *
     */
    public void setRunType(java.lang.Integer runType) {
        this.runType = runType;
    }
    
    /** Getter for property totalMembers.
     * @return Value of property totalMembers.
     *
     */
    public int getTotalMembers() {
        return totalMembers;
    }
    
    /** Setter for property totalMembers.
     * @param totalMembers New value of property totalMembers.
     *
     */
    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }
    
    /** Getter for property amcCardRunDt.
     * @return Value of property amcCardRunDt.
     *
     */
    public java.sql.Timestamp getAmcCardRunDt() {
        return amcCardRunDt;
    }
    
    /** Setter for property amcCardRunDt.
     * @param amcCardRunDt New value of property amcCardRunDt.
     *
     */
    public void setAmcCardRunDt(java.sql.Timestamp amcCardRunDt) {
        this.amcCardRunDt = amcCardRunDt;
    }
    
}
