package org.afscme.enterprise.update.participation;

import java.sql.Timestamp;

/**
 * Represents the data in the ParticipationUpdate/MemberOutcome element
 */
public class ParticipationUpdateElement {
    
    /**
     * Determined by the ParticipationUpdate/Participation/Group/Type/@Detail attribute
     */
    protected Integer detailPk;
    
    /**
     * Determined by the ParticipationUpdate/MemberOutcome/Member@MemberNumber attribute
     */
    protected Integer personPk;
    
    /**
     * Determined by ParticipationUpdate/MemberOutcome/Member@SSN attribute.
     * 
     * Matched against Person.ssn
     */
    protected String ssn;
    
    /**
     * Determined by ParticipationUpdate/MemberOutcome/Member@FirstName attribute.
     * 
     * Matched against Person.first_nm
     */
    protected String firstName;
    
    /**
     * Determined by ParticipationUpdate/MemberOutcome/Member@LastName attribute.
     * 
     * Matched against Person.last_nm
     */
    protected String lastName;
    
    /**
     * Determined by the ParticipationUpdate/MemberOutcome/AffiliateIdentifier element
     */
    protected Integer affPk;

    
    /**
     * Looked up in Participation_Cd_Outcomes.partip_outcome_pk and stored to 
     * Member_Participation.particip_outcome_pk
     */
    protected Integer outcomePk;
    
    /**
     * Determined by the ParticipationUpdate/MemberOutcome/Outcome@Name attribute
     * 
     * Looked up in Participation_Cd_Outcomes.partip_outcome_nm and to get the 
     * Member_Participation.particip_outcome_pk
     */
    protected String outcomeName;
    
    /**
     * Determined by the ParticipationUpdate/MemberOutcome/Outcome@Date attribute
     * 
     * Stored to Member_Participation.mbr_particip_dt
     */
    protected Timestamp outcomeDate;


    public String toString() {
        return "ParticipationUpdateElement[" +
                "detailPk=" + detailPk +
                ", personPk=" + personPk +
                ", ssn=" + ssn +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", affPk=" + affPk +
                ", outcomePk=" + outcomePk +
                ", outcomeName=" + outcomeName +
                ", outcomeDate=" + outcomeDate +
                "]"
        ;
    }
    

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
    
    /** Getter for property detailPk.
     * @return Value of property detailPk.
     *
     */
    public Integer getDetailPk() {
        return detailPk;
    }
    
    /** Setter for property detailPk.
     * @param detailPk New value of property detailPk.
     *
     */
    public void setDetailPk(Integer detailPk) {
        this.detailPk = detailPk;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     *
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /** Getter for property outcomeDate.
     * @return Value of property outcomeDate.
     *
     */
    public Timestamp getOutcomeDate() {
        return outcomeDate;
    }
    
    /** Setter for property outcomeDate.
     * @param outcomeDate New value of property outcomeDate.
     *
     */
    public void setOutcomeDate(Timestamp outcomeDate) {
        this.outcomeDate = outcomeDate;
    }
    
    /** Getter for property outcomeName.
     * @return Value of property outcomeName.
     *
     */
    public String getOutcomeName() {
        return outcomeName;
    }
    
    /** Setter for property outcomeName.
     * @param outcomeName New value of property outcomeName.
     *
     */
    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }
    
    /** Getter for property outcomePk.
     * @return Value of property outcomePk.
     *
     */
    public Integer getOutcomePk() {
        return outcomePk;
    }
    
    /** Setter for property outcomePk.
     * @param outcomePk New value of property outcomePk.
     *
     */
    public void setOutcomePk(Integer outcomePk) {
        this.outcomePk = outcomePk;
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(Integer personPk) {
        this.personPk = personPk;
    }
    
    /** Getter for property ssn.
     * @return Value of property ssn.
     *
     */
    public String getSsn() {
        return ssn;
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     *
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    
}
