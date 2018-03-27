package org.afscme.enterprise.officer.ejb;

import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.officer.LocationAddressAssociationData;
import org.afscme.enterprise.officer.OfficerData;
import org.afscme.enterprise.officer.OfficerCriteria;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.*;

/**
 * Encapsulates operations against officer data
 */
public class MaintainOfficersBean extends SessionBase {
    
    /**
     * Updates a table (COM_Officer_Card_Run) that gathers the officers that require a new
     *  officer card to be generated.  
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPK The affiliate that officer belongs to
     * @param personPK The person that requires an officer card
     * @param officePK The office the person holds
     */
    public void addToWeeklyCardRun(Integer affPK, Integer officePK, Integer personPK) { 

    }
    
    /**
     * Calls the getPersonAddressesByDepartment and getPersonAddressByAffiliate 
     * methods in the Address Component to retrieve all the possible addresses for an 
     * Officer.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param personPK Person that the Addresses are associated with.
     * @param affPK Affiliate containing the Address Locations to be retrieved.
     * @param departmentPK Department containing the Addresses to be retrieved for
     * this Person.
     * @return a Collection of Addresses for a Person.
     */
    public LocationAddressAssociationData getAddressAssociations(Integer personPK, Integer affPK, Integer departmentPK) { 
        return null;
    }
    
    /**
     * Retrieves the pos_addr_from_person_pk from Officer_History.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param affiliatePk
     * @param personPk
     * @param officePk
     * @param officeGroupId
     * @param posStartDt
     * @return an Integer containing the value in the database or NULL.
     */
    public Integer getCurrentAddressAssociation(Integer affiliatePk, Integer personPk, Integer officePk, Integer officeGroupId, Timestamp posStartDt) { 
        return null;
    }
    
    /**
     * Retrieves the pos_addr_from_org_pk from Officer_History.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param affiliatePk
     * @param personPk
     * @param officePk
     * @param officeGroupId
     * @param posStartDt
     * @return an Integer containing the value in the database or NULL.
     */
    public Integer getCurrentLocationAssociation(Integer affiliatePk, Integer personPk, Integer officePk, Integer officeGroupId, Timestamp posStartDt) { 
        return null;
    }
    
    /**
     * Retrieves Officer Detail data. This method will retrieve all the data required to
     *  render the Officer detail screen, making additional Person and Affiliate methods
     *  calls to retrieve, for example, address data and affiliate data.
     * 
     * Calls getOfficeTitles to get other positions held.
     * 
     * If pos_addr_from_person_pk is null and pos_addr_from_org_pk is NOTnull, then 
     * calls the getAddressLocation method from the Affiliate component
     * 
     * If pos_addr_from_person_pk is NOTnull and pos_addr_from_org_pk is null, then 
     * calls the getPersonAddress method from the Address component
     * 
     * SELECT executive_board_fg, reporting_officer_fg
     * FROM Aff_Offices JOIN Officer_History ON Aff_Offices.aff_pk = 
     * Officer_History.aff_pk, Aff_Offices.office_pk = Officer_History.office_pk, 
     * Aff_Offices.office_group_id = Officer_History.office_group_id
     * WHERE Officer_History.person_pk =      * @param personPK 
     * AND Aff_Offices.aff_pk =      * @param affPK
     * AND Aff_Offices.office_pk =      * @param officePK
     * AND Aff_Offices.office_group_id =      * @param officeGroupId
     * AND Officer_History.pos_start_dt =      * @param posStartDt
     * 
     * SELECT mbrStatus, primary_information_source FROM Aff_Members
     * [OUTER] JOIN Officer_History ON Aff_Members.aff_pk = 
     * Officer_History.position_mbr_affiliation AND Aff_Members.person_pk = 
     * Officer_History.person_pk 
     * 
     * SELECT comment_dt, comment_txt, created_user_pk
     * FROM Officer_Comments JOIN Officer_History ON Officer_Comments.aff_pk = 
     * Officer_History.aff_pk, Officer_Comments.office_pk = Officer_History.office_pk, 
     * Officer_Comments.office_group_id = Officer_History.office_group_id,
     * Officer_Comments.person_pk = Officer_History.person_pk,
     * Officer_Comments.pos_start_dt = Officer_History.pos_start_dt
     * WHERE Officer_Comments.person_pk =      * @param personPK 
     * AND Officer_Comments.aff_pk =      * @param affPK
     * AND Officer_Comments.office_pk =      * @param officePK
     * AND Officer_Comments.office_group_id =      * @param officeGroupId
     * AND Officer_Comments.pos_start_dt =      * @param posStartDt
     * AND Officer_Comments.comment_dt
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param affPK Affiliate that contains the office.
     * @param officePK Constitutional Office within the Affiliate
     * @param officeGroupId The specific office for the Constitutional Office - an 
     * affiliate may have more than one office with the same constitutional title
     * @param personPK Person PK that holds the Office within the Affiliate.
     * @param posStartDt The date the officer first filled the office, helps to 
     * distinguish between the history of officers who have held or hold this office 
     * title.
     * @param officeGroupID
     * @return an OfficerData object with information for a specific office and the Person holding the office.
     */
    public OfficerData getOfficerDetail(Integer affPK, Integer officePK, Integer officeGroupID, Integer personPK, Timestamp posStartDt) { 
        return null;
    }
    
    /**
     * Retrieves a collection of officer data both past and present in descending order for
     *  a specific person.  
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param personPK The reference to the person
     * @return a Collection of OfficerData
     */
    public Collection getOfficerHistory(Integer personPK) { 
        return null;
    }
    
    /**
     * Returns a collection of all the current officer titles this person/member is holding.
     * 
     * SELECT afscme_title_nm FROM AFSCME_Offices WHERE office_pk = officePK
     * 
     * SELECT pos_start_dt, pos_end_dt FROM Officer_History WHERE person_pk =      * @param 
     * personPK
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param personPK The Person for which the search is being conducted
     * @return a Collection of OfficeData
     */
    public Collection getOtherPositions(Integer personPK) { 
        return null;
    }
    
    /**
     * Searches for officers based on a given set of search criteria and returns the rows
     *  indicated. 
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param officerCriteria Data object containing the search criteria provided by the
     *  user.
     * @return Collection of OfficerResult objects based on the search criteria and the assigned
     *  privliges.
     */
    public Collection searchOfficers(OfficerCriteria officerCriteria) { 
        return null;
    }
    
    /**
     * Assigns a mailing address to the officer.  This address can be either an affiliate
     *  location address or a personal address.  
     * 
     * The posAddrFromPersonPK and posAddrFromOrgPK cannot both contain a value.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     * 
     * @param affPK The affiliate for the office position
     * @param personPK The Person currently holding the office position
     * @param officerPK The office position
     * @param posAddrFromPersonPK If not null, contains the personal address that the person
     *  holding the office wants their mail to be sent to
     * @param posAddrFromOrgPK If not null, contains the Location address that the person
     *  holding the office wants their mail to be sent to
     */
    public void setAddressAssociation(Integer affPK, Integer personPK, Integer officerPK, Integer posAddrFromPersonPK, Integer posAddrFromOrgPK) { 

    }
    
    /**
     * Updates the officer detail data for an officer.
     * 
     * UPDATE Officer_History
     * SET suspended_fg, suspended_dt 
     * WHERE aff_pk = affPK
     * AND office_pk = officePK
     * AND office_group_id = officeGroupId
     * AND person_pk = personPK 
     * AND pos_start_dt = posStartDt
     * 
     * INSERT INTO Officer_Comments
     * (aff_pk, office_pk, office_group_id, person_pk, pos_start_dt , comment_dt, 
     * comment_txt, created_user_pk)
     * VALUES (affPK, officePK, officeGroupId, personPK, posStartDt, commentDt, 
     * commentTxt, createdUserPK)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param officerData the new OfficerData
     */
    public void updateOfficerDetail(OfficerData officerData) { 

    }
    
}