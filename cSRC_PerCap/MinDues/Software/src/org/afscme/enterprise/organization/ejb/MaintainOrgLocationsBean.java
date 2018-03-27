package org.afscme.enterprise.organization.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.codes.Codes.OrganizationSubType;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAddressData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.organization.OrgPhoneData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.apache.log4j.Logger;


/**
 * Provides services for creating, associating and removing organization locations. Services
 *  provided can be used for both affiliates and external organizations
 *
 * @ejb:bean name="MaintainOrgLocations" display-name="MaintainOrgLocations"
 *              jndi-name="MaintainOrgLocations"
 *              type="Stateless" view-type="local" 
 */
public class MaintainOrgLocationsBean extends SessionBase {

    static Logger logger = Logger.getLogger(MaintainOrgLocationsBean.class);

    /** Reference to the MaintainAffiliates EJB */
    private MaintainAffiliates affiliatesBean;
       
    /** Select the primary location for an Organization */
    private static final String SQL_SELECT_PRIMARY_ORG_LOCATION =
        "SELECT org_locations_pk, location_nm, location_primary_fg, " + 
        "       created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk " + 
        "FROM   Org_Locations " + 
        "WHERE  org_pk = ? " + 
        "AND    location_primary_fg = 1";
  
    /** Selects all of the locations for an Organization */
    private static final String SQL_SELECT_ORG_LOCATIONS =
        "SELECT     org_locations_pk, location_nm, location_primary_fg, " + 
        "           created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk " + 
        "FROM       Org_Locations " + 
        "WHERE      org_pk = ? " +
        "ORDER BY   location_primary_fg DESC, location_nm ASC";

    /** Select the addresses associated with an Organization Location */
    private static final String SQL_SELECT_LOCATION_ADDRESSES =
        "SELECT     org_addr_pk, org_addr_type, " + 
        "           attention_line, addr1, addr2, city, state, zipcode, zip_plus, " + 
        "           county, province, country, addr_bad_fg, addr_bad_dt, " + 
        "           created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk " + 
        "FROM       Org_Address " + 
        "WHERE      org_locations_pk = ? " +
        "ORDER BY   org_addr_type";

    /** Select the phones associated with an Organization Location */
    private static final String SQL_SELECT_LOCATION_PHONES =
        "SELECT     org_phone_pk, org_phone_type, " + 
        "           country_cd, area_code, phone_no, phone_extension, " + 
        "           phone_bad_fg, phone_bad_dt, " + 
        "           created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk " + 
        "FROM       Org_Phone " + 
        "WHERE      org_locations_pk = ? " +
        "ORDER BY   org_phone_type";
    
    /** Select the org_subtype for the orgPK */
    private static final String SQL_SELECT_ORG_SUBTYPE =
        "SELECT org_subtype " + 
        "FROM   Org_Parent " + 
        "WHERE  org_pk = ? ";

    /** Inserts a new Location */
    private static final String SQL_INSERT_NEW_ORG_LOCATION = 
        "INSERT INTO Org_Locations " + 
        "   (location_nm, location_primary_fg, org_pk, " +
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " + 
        "VALUES (?, ?, ?, ?, GETDATE(), ?, GETDATE() )";

    /** Inserts a new Address for a Location */
    private static final String SQL_INSERT_NEW_LOCATION_ADDRESS =
        "INSERT INTO Org_Address " +  
        "   (org_locations_pk, org_addr_type, " +
        "   attention_line, addr1, addr2, city, state, zipcode, zip_plus, " + 
        "   county, province, country, addr_bad_fg, addr_bad_dt, " + 
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?, GETDATE() )";
    
    /** Inserts a new Phone for a Location */
    private static final String SQL_INSERT_NEW_LOCATION_PHONE =
        "INSERT INTO Org_Phone " +  
        "   (org_locations_pk, org_phone_type, " +
        "   country_cd, area_code, phone_no, phone_extension, " + 
        "   phone_bad_fg, phone_bad_dt, " + 
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?, GETDATE() )";
  
    /** Updates an Organization Location */
    private static final String SQL_UPDATE_ORG_LOCATION = 
        "UPDATE Org_Locations " +
        "SET    location_nm = ?, location_primary_fg = ?, " +  
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " + 
        "WHERE  org_locations_pk = ?";

    /** Updates an Organization Location Address */
    private static final String SQL_UPDATE_ORG_LOCATION_ADDRESS = 
        "UPDATE Org_Address " +
        "SET    attention_line = ?, addr1 = ?, addr2 = ?, city = ?, state = ?, zipcode = ?, zip_plus = ?, " +
        "       county = ?, province = ?, country = ?, addr_bad_fg = ?, addr_bad_dt = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " + 
        "WHERE  org_addr_pk = ?";
    
    /** Updates an Organization Location Phone */
    private static final String SQL_UPDATE_ORG_LOCATION_PHONE = 
        "UPDATE Org_Phone " +
        "SET    country_cd = ?, area_code = ?, phone_no = ?, phone_extension = ?, " +
        "       phone_bad_fg = ?, phone_bad_dt = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " + 
        "WHERE  org_phone_pk = ?";

    /** Updates the old Primary Organization Location since a new one is being set */
    private static final String SQL_UPDATE_OLD_PRIMARY_ORG_LOCATION = 
        "UPDATE Org_Locations " +
        "SET    location_primary_fg = ?, " +  
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " + 
        "WHERE  org_locations_pk = ?";
    
    /** Select all Mailing Lists that has the specified OrgLocationPK (which is being deleted) */
    private static final String SQL_SELECT_ORG_LOCATION_MAILING_LIST_ORGS = 
        "SELECT MLBO_mailing_list_pk, org_pk " + 
        "FROM   Mailing_List_Orgs " + 
        "WHERE  org_locations_pk = ? ";

    /** Updates all Mailing Lists to Null for specified OrgLocationPK (which is being deleted) */
    private static final String SQL_UPDATE_ORG_LOCATION_MAILING_LIST_ORGS = 
        "UPDATE Mailing_List_Orgs " +
        "SET    org_locations_pk = NULL " + 
        "WHERE  MLBO_mailing_list_pk = ? " + 
        "AND    org_pk = ? ";

    /** Select all Officer History Org Addresses for the specified OrgLocationPK (which is being deleted) */
    private static final String SQL_SELECT_ORG_LOCATION_OFFICER_HISTORY = 
        "SELECT officer_history_surrogate_key, person_pk, aff_pk, pos_start_dt, " + 
        "       office_group_id, afscme_office_pk " + 
        "FROM   Officer_History oh " + 
        "JOIN   Org_Address oa      ON oa.org_addr_pk       = oh.pos_addr_from_org_pk " + 
        "JOIN   Org_Locations ol    ON ol.org_locations_pk  = oa.org_locations_pk " + 
        "WHERE  ol.org_locations_pk = ? ";

    /** Updates all Officer History Org Addresses to Null for specified OrgLocationPK (which is being deleted) */
    private static final String SQL_UPDATE_ORG_LOCATION_OFFICER_HISTORY = 
        "UPDATE Officer_History " +
        "SET    pos_addr_from_org_pk = NULL " + 
        "WHERE  officer_history_surrogate_key = ? ";

    /** Select all Affiliate Staff that has the specified OrgLocationPK (which is being deleted) */
    private static final String SQL_SELECT_ORG_LOCATION_AFF_STAFF = 
        "SELECT aff_pk, person_pk " + 
        "FROM   Aff_Staff " + 
        "WHERE  org_locations_pk = ? ";

    /** Updates all Affiliate Staff to Null for specified OrgLocationPK (which is being deleted) */
    private static final String SQL_UPDATE_ORG_LOCATION_AFF_STAFF = 
        "UPDATE Aff_Staff " +
        "SET    org_locations_pk = NULL " + 
        "WHERE  aff_pk = ? " +
        "AND    person_pk = ? ";

    /** Select all Org Associates that has the specified OrgLocationPK (which is being deleted) */
    private static final String SQL_SELECT_ORG_LOCATION_EXT_ORG_ASSOCIATES = 
        "SELECT org_pk, person_pk " + 
        "FROM   Ext_Org_Associates " + 
        "WHERE  org_locations_pk = ? ";

    /** Updates all Org Associates to Null for specified OrgLocationPK (which is being deleted) */
    private static final String SQL_UPDATE_ORG_LOCATION_EXT_ORG_ASSOCIATES = 
        "UPDATE Ext_Org_Associates " +
        "SET    org_locations_pk = NULL " + 
        "WHERE  org_pk = ? " +
        "AND    person_pk = ? ";        

    /** Deletes an Organization Location */
    private static final String SQL_DELETE_ORG_LOCATION = 
        "DELETE Org_Locations " +
        "WHERE org_locations_pk = ?";

    /** Select the location for an OrgLocationPK */
    private static final String SQL_SELECT_ORG_LOCATION_BY_PK =
        "SELECT org_pk, location_nm, location_primary_fg, " + 
        "       created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk " + 
        "FROM   Org_Locations " + 
        "WHERE  org_locations_pk = ? ";
    
    /** Select the orgLocationsPK for the Primary location for an Organization */
    private static final String SQL_SELECT_PRIMARY_ORG_LOCATION_PK =
        "SELECT org_locations_pk " + 
        "FROM   Org_Locations " + 
        "WHERE  org_pk = ? " + 
        "AND    location_primary_fg = 1";

    /** Select the count for Primary location for an Organization */
    private static final String SQL_SELECT_PRIMARY_ORG_LOCATION_COUNT =
        "SELECT COUNT(*) " + 
        "FROM   Org_Locations " + 
        "WHERE  org_pk = ? " + 
        "AND    location_primary_fg = 1";
    
    /** Select the count of Locations for an Organization */
    private static final String SQL_SELECT_ORG_LOCATIONS_COUNT =
        "SELECT COUNT(*) " + 
        "FROM   Org_Locations " + 
        "WHERE  org_pk = ? ";

    /** Select the Org Locations Pk by its Org Address PK (for Process Return Mail) */
    private static final String SQL_SELECT_ORG_LOCATIONS_BY_LOC_ADDR_PK =
        "SELECT org_locations_pk " + 
        "FROM   Org_Address " + 
        "WHERE  org_addr_pk = ? ";    

    
    /** Gets references to the dependent EJBs */
    public void ejbCreate() throws CreateException {
        try {
            affiliatesBean = JNDIUtil.getMaintainAffiliatesHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainOrgLocationsBean.ejbCreate()" + e);
        }
    }
        
    /**
     * retrieves the primary location for a particular organization
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return LocationData
     * @param orgPK - the primary key of the organization in question
     */
    public LocationData getOrgPrimaryLocation(Integer orgPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LocationData locationData = null;

        try {
            con = DBUtil.getConnection();
            
            //
            // ORG_LOCATION
            //
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Primary Location Select statement: [" + SQL_SELECT_PRIMARY_ORG_LOCATION + "] "
                            + "criteria [" + orgPK.toString() + "] ");
            
            //retrieve the Primary Location of an Organization for the orgPK
            ps = con.prepareStatement(SQL_SELECT_PRIMARY_ORG_LOCATION);

            // retrieve the organization location key for the primary location                   
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                locationData = new LocationData();
                locationData.setOrgPk(orgPK);                
                locationData.setOrgLocationPK(new Integer(rs.getInt("org_locations_pk")));
                locationData.setLocationNm(rs.getString("location_nm"));
                locationData.setPrimaryLocationBoolean(DBUtil.getBooleanFromShort(rs.getShort("location_primary_fg")));
                
                RecordData recordData = new RecordData();
                recordData.setCreatedDate(rs.getTimestamp("created_dt"));
                recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                locationData.setRecordData(recordData);
            }
            
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            // check for org address or phone only if there is a primary location
            if (locationData != null) {
                
                //
                // ORG_ADDRESS
                //

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Primary Location Address Select statement: [" + SQL_SELECT_LOCATION_ADDRESSES + "] "
                                + "criteria [" + locationData.getOrgLocationPK().toString() + "] ");

                // retrieve the addresses for the primary location by org locations key
                ps = con.prepareStatement(SQL_SELECT_LOCATION_ADDRESSES);
                ps.setInt(1, locationData.getOrgLocationPK().intValue());
                rs = ps.executeQuery();

                // create the storage for the location addresses
                LinkedList locationAddresses = null;

                while (rs.next()) {
                    if (locationAddresses == null)
                        locationAddresses = new LinkedList();

                    OrgAddressRecord orgAddress = null;
                    orgAddress = read(rs, new OrgAddressRecord());
                    locationAddresses.add(orgAddress);
                }

                if (locationAddresses != null) {
                    locationData.setOrgAddressData(locationAddresses);                
                }    

                rs.close();
                rs = null;
                ps.close();
                ps = null;

                //
                // ORG_PHONE
                //

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Primary Location Phone Select statement: [" + SQL_SELECT_LOCATION_PHONES + "] "
                                + "criteria [" + locationData.getOrgLocationPK().toString() + "] ");

                // retrieve the phones for the primary location by org locations key
                ps = con.prepareStatement(SQL_SELECT_LOCATION_PHONES);
                ps.setInt(1, locationData.getOrgLocationPK().intValue());
                rs = ps.executeQuery();

                // create the storage for the location phones
                LinkedList locationPhones = null;

                while (rs.next()) {
                    if (locationPhones == null)
                        locationPhones = new LinkedList();

                    OrgPhoneData orgPhone = null;
                    orgPhone = read(rs, new OrgPhoneData());                

                    locationPhones.add(orgPhone);
                }

                if (locationPhones != null) {
                    locationData.setOrgPhoneData(locationPhones);                
                }
            }            
        } catch (SQLException e) {
            throw new EJBException("Error getting Org Primary location MaintainOrgLocationsBean.getOrgPrimaryLocation()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return locationData;     
    }
    
    /**
     * gets the set of locations for the organization. Returns a collection of LocationData
     *  objects 
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return collection - of LocationData objects
     * @param orgPK - An Integer representing the primary key of the organization in question
     */
    public List getOrgLocations(Integer orgPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List locationList = new LinkedList();

        try {
            con = DBUtil.getConnection();
            
            //
            // ORG_LOCATION
            //
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Location Select statement: [" + SQL_SELECT_ORG_LOCATIONS + "] "
                            + "criteria [" + orgPK.toString() + "] ");
            
            //retrieve all of the Locations of an Organization for the orgPK
            ps = con.prepareStatement(SQL_SELECT_ORG_LOCATIONS);
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                LocationData locationData = new LocationData();
                locationData.setOrgPk(orgPK);
                locationData.setOrgLocationPK(new Integer(rs.getInt("org_locations_pk")));
                locationData.setLocationNm(rs.getString("location_nm"));
                locationData.setPrimaryLocationBoolean(DBUtil.getBooleanFromShort(rs.getShort("location_primary_fg")));

                RecordData recordData = new RecordData();
                recordData.setCreatedDate(rs.getTimestamp("created_dt"));
                recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                locationData.setRecordData(recordData);
                
                //
                // ORG_ADDRESS
                //

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Location Address Select statement: [" + SQL_SELECT_LOCATION_ADDRESSES + "] "
                                + "criteria [" + locationData.getOrgLocationPK().toString() + "] ");

                // retrieve the addresses for the primary location by org locations key
                ps2 = con.prepareStatement(SQL_SELECT_LOCATION_ADDRESSES);
                ps2.setInt(1, locationData.getOrgLocationPK().intValue());
                rs2 = ps2.executeQuery();

                // create the storage for the location addresses
                LinkedList locationAddresses = null;

                while (rs2.next()) {
                    if (locationAddresses == null)
                        locationAddresses = new LinkedList();

                    OrgAddressRecord orgAddress = null;
                    orgAddress = read(rs2, new OrgAddressRecord());
                    locationAddresses.add(orgAddress);
                }

                if (locationAddresses != null) {
                    locationData.setOrgAddressData(locationAddresses);                
                }    

                rs2.close();
                rs2 = null;
                ps2.close();
                ps2 = null;

                //
                // ORG_PHONE
                //

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Location Phone Select statement: [" + SQL_SELECT_LOCATION_PHONES + "] "
                                + "criteria [" + locationData.getOrgLocationPK().toString() + "] ");

                // retrieve the phones for the primary location by org locations key
                ps2 = con.prepareStatement(SQL_SELECT_LOCATION_PHONES);
                ps2.setInt(1, locationData.getOrgLocationPK().intValue());
                rs2 = ps2.executeQuery();

                // create the storage for the location phones
                LinkedList locationPhones = null;

                while (rs2.next()) {
                    if (locationPhones == null)
                        locationPhones = new LinkedList();

                    OrgPhoneData orgPhone = null;
                    orgPhone = read(rs2, new OrgPhoneData());                

                    locationPhones.add(orgPhone);
                }

                if (locationPhones != null) {
                    locationData.setOrgPhoneData(locationPhones);                
                }

                rs2.close();
                rs2 = null;
                ps2.close();
                ps2 = null;                

                
                // add the location to the collection of locations for the org
                locationList.add(locationData);                
            }                
            
        } catch (SQLException e) {
            throw new EJBException("Error getting Org locations MaintainOrgLocationsBean.getOrgLocations()", e);
        }
        finally {
            DBUtil.cleanup(null, ps2, rs2);
            DBUtil.cleanup(con, ps, rs);
        }

        return locationList;     
    }
    
    /**
     * Updates the location data which includes addresses and phones for the location. 
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @return void
     * @param LocationData - an object containing the location data to be updated
     * @param orgPK - the organization (affiliate or external) that the Location is associated
     *  with
     * @param updatedByUserPk - primary key of the user that updated the location.
     */
    public void updateOrgLocation(LocationData locationData, Integer orgPK, Integer updatedByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;        
        Integer orgLocationPK = null;
        
        // attributes for affiliate change history
        boolean isAffiliate = false;
        LocationData oldLocation = null;    // also used for location change comparison
        LocationData newLocation = null;
        
        // for saving old primary orgLocationPk
        Integer previousPrimaryPK = null;
        LocationData primaryOldLocation = null;
        LocationData primaryNewLocation = null;        

        orgLocationPK = locationData.getOrgLocationPK();

        //update the Organization Location 
        try {

            // retrieve the location data in db for this location for change comparison check and aff check
            oldLocation = getOrgLocation(orgLocationPK);
            
            // check to see if any of the location data changed before trying to update
            if (!(locationData.equals(oldLocation))) {
          
                // check to see if orgPK is an affiliate 
                if (isAffiliate(orgPK)) {
                    isAffiliate = true;
                }

                // find out the primary location pk for the organization if primary is being set
                if ((locationData.getPrimaryLocationBoolean() != null) && 
                    (locationData.getPrimaryLocationBoolean().booleanValue() == true)) {
                    previousPrimaryPK = getOrgPrimaryLocationPK(orgPK);
                    if ((previousPrimaryPK != null) && (previousPrimaryPK.intValue() != orgLocationPK.intValue())) 
                        primaryOldLocation = getOrgPrimaryLocation(orgPK);
                    else 
                        previousPrimaryPK = null;
                }

                con = DBUtil.getConnection();

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Update Org_Location statement: [" + SQL_UPDATE_ORG_LOCATION + "] ");

                ps1 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION);

                // update all the organization location data by orgLocationPK
                ps1.setString(1, locationData.getLocationNm());
                
                if ((locationData.getPrimaryLocationBoolean() == null) || 
                    (locationData.getPrimaryLocationBoolean().booleanValue() == false)) 
                    ps1.setNull(2, Types.SMALLINT);
                else
                    DBUtil.setNullableBooleanAsShort(ps1, 2, locationData.getPrimaryLocationBoolean());
                
                ps1.setInt(3, updatedByUserPk.intValue());            

                // set the orgLocationPK to update
                ps1.setInt(4, orgLocationPK.intValue());

                // update into Org_Locations
                ps1.executeUpdate();


                //
                // ORG_ADDRESS
                //

                // loop for any addresses (should be at most two - Regular, Shipping)
                if (locationData.getOrgAddressData() != null) {

                    LinkedList addressList = locationData.getOrgAddressData();
                    Iterator it = addressList.iterator();
                    while (it.hasNext()) {

                        OrgAddressRecord orgAddress = (OrgAddressRecord)it.next();
                        Integer orgAddressPK = orgAddress.recordData.getPk();

                        // ADD
                        if ((orgAddressPK == null) || (orgAddressPK.intValue() == 0)) {

                            // DEBUG
                            if (logger.isDebugEnabled())
                                logger.debug("Insert Org_Address statement: [" + SQL_INSERT_NEW_LOCATION_ADDRESS + "] ");

                            // Add the Location Addresses 
                            ps2 = con.prepareStatement(SQL_INSERT_NEW_LOCATION_ADDRESS);

                            ps2.setInt(1, orgLocationPK.intValue());

                            writeAdd(ps2, orgAddress);

                            ps2.setInt(15, updatedByUserPk.intValue());
                            ps2.setInt(16, updatedByUserPk.intValue());

                            orgAddressPK = DBUtil.insertAndGetIdentity(con, ps2);

                            // DEBUG
                            if (logger.isDebugEnabled())
                                logger.debug("New Org_Address PK: [" + orgAddressPK + "] ");
                        }
                        else {  // UPDATE  

                            // check to see if any of the address data changed before trying to update
                            OrgAddressRecord oldAddress = oldLocation.getOrgAddressByPK(orgAddressPK);
                            
                            if (!(orgAddress.equals(oldAddress))) {

                                // DEBUG
                                if (logger.isDebugEnabled())
                                    logger.debug("Update Org_Address statement: [" + SQL_UPDATE_ORG_LOCATION_ADDRESS + "] ");

                                // Update the Location Addresses 
                                ps2 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION_ADDRESS);

                                DBUtil.setNullableVarchar(ps2, 1, orgAddress.getAttentionLine());
                                ps2.setString(2, orgAddress.getAddr1());
                                DBUtil.setNullableVarchar(ps2, 3, orgAddress.getAddr2());
                                DBUtil.setNullableVarchar(ps2, 4, orgAddress.getCity());
                                DBUtil.setNullableChar(ps2, 5, orgAddress.getState());
                                DBUtil.setNullableVarchar(ps2, 6, orgAddress.getZipCode());
                                DBUtil.setNullableVarchar(ps2, 7, orgAddress.getZipPlus());
                                DBUtil.setNullableVarchar(ps2, 8, orgAddress.getCounty()); 
                                DBUtil.setNullableVarchar(ps2, 9, orgAddress.getProvince());
                                if ((orgAddress.getCountryPk() == null) || (orgAddress.getCountryPk().intValue() == 0)) 
                                    ps2.setNull(10, Types.INTEGER);
                                else
                                    DBUtil.setNullableInt(ps2, 10, orgAddress.getCountryPk());
                                DBUtil.setNullableBooleanAsShort(ps2, 11, new Boolean(orgAddress.isBad()));
                                if (orgAddress.isBad()) 
                                    ps2.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
                                else    
                                    ps2.setNull(12, Types.TIMESTAMP);            

                                ps2.setInt(13, updatedByUserPk.intValue());

                                // set the org_addr_pk for the location to update
                                ps2.setInt(14, orgAddressPK.intValue());

                                // update into Org_Address record
                                ps2.executeUpdate();

                                // DEBUG
                                if (logger.isDebugEnabled())
                                    logger.debug("Updated Org_Address PK: [" + orgAddressPK + "] ");
                            }
                        }

                        if (ps2 != null) {
                            ps2.close();
                            ps2 = null;
                        }
                    }
                }              


                //
                // ORG_PHONE
                //

                // loop for any phones (should be at most two - Office, Fax)
                if (locationData.getOrgPhoneData() != null) {

                    LinkedList phoneList = locationData.getOrgPhoneData();
                    Iterator it = phoneList.iterator();
                    while (it.hasNext()) {

                        OrgPhoneData orgPhone = (OrgPhoneData)it.next();
                        Integer orgPhonePK = orgPhone.getTheRecordData().getPk();

                        // ADD
                        if ((orgPhonePK == null) || (orgPhonePK.intValue() == 0)) {

                            // DEBUG
                            if (logger.isDebugEnabled())
                                logger.debug("Insert Org_Phone statement: [" + SQL_INSERT_NEW_LOCATION_PHONE + "] ");

                            // Add the Location Phones 
                            ps2 = con.prepareStatement(SQL_INSERT_NEW_LOCATION_PHONE);

                            ps2.setInt(1, orgLocationPK.intValue());

                            writeAdd(ps2, orgPhone);

                            ps2.setInt(9, updatedByUserPk.intValue());
                            ps2.setInt(10, updatedByUserPk.intValue());

                            orgPhonePK = DBUtil.insertAndGetIdentity(con, ps2);

                            // DEBUG
                            if (logger.isDebugEnabled())
                                logger.debug("New Org_Phone PK: [" + orgPhonePK + "] ");
                        }
                        else {  // UPDATE  

                            // check to see if any of the phone data changed before trying to update
                            OrgPhoneData oldPhone = oldLocation.getOrgPhoneByPK(orgPhonePK);
                            
                            if (!(orgPhone.equals(oldPhone))) {

                                // DEBUG
                                if (logger.isDebugEnabled())
                                    logger.debug("Update Org_Phone statement: [" + SQL_UPDATE_ORG_LOCATION_PHONE + "] ");

                                // Update the Location Phones 
                                ps2 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION_PHONE);

                                DBUtil.setNullableVarchar(ps2, 1, orgPhone.getCountryCode());
                                DBUtil.setNullableVarchar(ps2, 2, orgPhone.getAreaCode());
                                ps2.setString(3, orgPhone.getPhoneNumber());

                                // set phone extension as null until PAC
                                ps2.setNull(4, Types.VARCHAR);

                                DBUtil.setNullableBooleanAsShort(ps2, 5, orgPhone.getPhoneBadFlag());
                                if ((orgPhone.getPhoneBadFlag() != null) && (orgPhone.getPhoneBadFlag().booleanValue())) 
                                    ps2.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                                else    
                                    ps2.setNull(6, Types.TIMESTAMP);            

                                ps2.setInt(7, updatedByUserPk.intValue());

                                // set the org_phone_pk for the location to update
                                ps2.setInt(8, orgPhonePK.intValue());

                                // update into Org_Address record
                                ps2.executeUpdate();

                                // DEBUG
                                if (logger.isDebugEnabled())
                                    logger.debug("Updated Org_Phone PK: [" + orgPhonePK + "] ");
                            }
                        }

                        if (ps2 != null) {
                            ps2.close();
                            ps2 = null;
                        }    
                    }            
                }

                // unset the old primary location since a new primary has been chosen
                if (previousPrimaryPK != null) {

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Update old Primary Org_Location statement: [" 
                                        + SQL_UPDATE_OLD_PRIMARY_ORG_LOCATION 
                                        + "] - orgPK: " + previousPrimaryPK );

                    ps1.close();
                    ps1 = null;

                    ps1 = con.prepareStatement(SQL_UPDATE_OLD_PRIMARY_ORG_LOCATION);

                    // update the primary flag for the old primary orgLocationPK
                    ps1.setNull(1, Types.SMALLINT);                
                    ps1.setInt(2, updatedByUserPk.intValue());            
                    ps1.setInt(3, previousPrimaryPK.intValue());
                    ps1.executeUpdate();
                }

                // if orgPK is an affiliate then call the change history
                if (isAffiliate) {
                    newLocation = getOrgLocation(orgLocationPK);
                    affiliatesBean.recordChangeToHistory(oldLocation, newLocation, updatedByUserPk);

                    // if updated previous primary, then call the change history too
                    if (previousPrimaryPK != null) {
                        primaryNewLocation = getOrgLocation(previousPrimaryPK);
                        affiliatesBean.recordChangeToHistory(primaryOldLocation, primaryNewLocation, updatedByUserPk);
                    }
                }

            }   // only update if data has changed
            
        } catch (SQLException e) {
            throw new EJBException("Error updating Org Locations (includes Addresses and Phones) MaintainOrgLocationsBean.updateOrgLocation()", e);
        }
        finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);            
        }
        
        // DEBUG
        if (logger.isDebugEnabled())
            logger.debug("Updated Org_Locations PK: [" + orgLocationPK + "] ");
    }
    
    /**
     * Removes(aka deletes) an Affiliate Address Location from the system.  Areas that reference
     *  this address location will have to be updated with a null value.  Affected areas
     *  include Affiliate Staff (Aff_Staff), Mailing Lists (MailingList_Orgs), Officer (Officer_History)
     *  and Organization Associates (Ext_Org_Associates) .
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @return void
     * @param orgLocationPK - primary key of the Location to be removed from the system
     * @param orgPK - primary key of the organization to which the location is associated
     * @param updatedByUserPk - primary key of the user that deleted the location.
     */
    public void removeOrgLocation(Integer orgLocationPK, Integer orgPK, Integer updatedByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int count = 0;

        // attributes for affiliate change history
        boolean isAffiliate = false;
        LocationData oldLocation = null;
        LocationData newLocation = null;

        //delete the Organization Location 
        try {

            // check to see if orgPK is an affiliate 
            if (isAffiliate(orgPK)) {
                isAffiliate = true;
                oldLocation = getOrgLocation(orgLocationPK);
            }
                        
            con = DBUtil.getConnection();

            // Check all the dependent tables first to set them to NULL before delete
            //
            // MAILING_LIST_ORGS 
            // 
            if (logger.isDebugEnabled())
                logger.debug("Select Org_Location_Mailing_List_Orgs statement: [" 
                            + SQL_SELECT_ORG_LOCATION_MAILING_LIST_ORGS + "] ");

            ps1 = con.prepareStatement(SQL_SELECT_ORG_LOCATION_MAILING_LIST_ORGS);

            // retrieve all of the mailing lists key for orgLocationPK
            ps1.setInt(1, orgLocationPK.intValue());
            rs = ps1.executeQuery();
            
            LinkedList mloList = new LinkedList();
            
            while (rs.next()) {
                HashMap mlHashMap = new HashMap();
                mlHashMap.put("MLBO_mailing_list_pk", new Integer(rs.getInt("MLBO_mailing_list_pk")));
                mlHashMap.put("org_pk", new Integer(rs.getInt("org_pk")));
                mloList.add(mlHashMap);
                count++;
            }
            
            // there are mailing lists records to update
            if (count > 0) {
                
                if (logger.isDebugEnabled())
                logger.debug("Update Org_Location_Mailing_List_Orgs statement: [" 
                                + SQL_UPDATE_ORG_LOCATION_MAILING_LIST_ORGS + "] ");

                Iterator it1 = mloList.iterator();
                while (it1.hasNext()) {

                    HashMap mlHashMap = (HashMap)it1.next();
                    Integer mlPK = (Integer) mlHashMap.get("MLBO_mailing_list_pk");
                    Integer mlOrgPK = (Integer) mlHashMap.get("org_pk");

                    // set the orgLocationPK to null
                    ps2 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION_MAILING_LIST_ORGS);
                    ps2.setInt(1, mlPK.intValue());
                    ps2.setInt(2, mlOrgPK.intValue());
                    ps2.executeUpdate();

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Updated MLBO_mailing_list_pk = [" + mlPK + "] " 
                                        + "org_pk = [" + mlOrgPK + "] to set org_locations_pk to NULL! ");
                }
                
                ps2.close();
                ps2 = null;                
            }

            rs.close();
            rs = null;
            ps1.close();
            ps1 = null;

            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Total Number of MAILING_LIST_ORGS records set to NULL = " + count 
                                + " for Org_Locations_PK = [" + orgLocationPK + "]");
            
            
            //
            // OFFICER_HISTORY 
            // 
            if (logger.isDebugEnabled())
                logger.debug("Select Org_Location_Officer_History statement: [" 
                            + SQL_SELECT_ORG_LOCATION_OFFICER_HISTORY + "] ");

            ps1 = con.prepareStatement(SQL_SELECT_ORG_LOCATION_OFFICER_HISTORY);

            // retrieve all of the officer history key for orgLocationPK
            ps1.setInt(1, orgLocationPK.intValue());
            rs = ps1.executeQuery();
            
            LinkedList ohList = new LinkedList();
            count = 0;
            
            while (rs.next()) {
                HashMap ohHashMap = new HashMap();
                ohHashMap.put("officer_history_surrogate_key", new Integer(rs.getInt("officer_history_surrogate_key")));
                ohList.add(ohHashMap);
                count++;
            }
            
            // there are officer history records to update
            if (count > 0) {
                
                if (logger.isDebugEnabled())
                    logger.debug("Update Org_Location_Officer_History statement: [" 
                                    + SQL_UPDATE_ORG_LOCATION_OFFICER_HISTORY + "] ");

                Iterator it2 = ohList.iterator();
                while (it2.hasNext()) {

                    HashMap ohHashMap = (HashMap)it2.next();
                    Integer ohsPK = (Integer) ohHashMap.get("officer_history_surrogate_key");

                    // set the orgLocationPK to null
                    ps2 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION_OFFICER_HISTORY);
                    ps2.setInt(1, ohsPK.intValue());
                    ps2.executeUpdate();

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Updated officer_history_surrogate_key = [" + ohsPK + "] " 
                                        + " to set pos_addr_from_org_pk to NULL! ");
                }
                
                ps2.close();
                ps2 = null;                
            }

            rs.close();
            rs = null;
            ps1.close();
            ps1 = null;
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Total Number of OFFICER_HISTORY records set to NULL = " + count 
                                + " for Org_Locations_PK = [" + orgLocationPK + "]");

            
            //
            // AFF_STAFF
            // 
            if (logger.isDebugEnabled())
                logger.debug("Select Org_Location_Aff_Staff statement: [" 
                            + SQL_SELECT_ORG_LOCATION_AFF_STAFF + "] ");

            ps1 = con.prepareStatement(SQL_SELECT_ORG_LOCATION_AFF_STAFF);

            // retrieve all of the affiliate staff key for orgLocationPK
            ps1.setInt(1, orgLocationPK.intValue());
            rs = ps1.executeQuery();
            
            LinkedList asList = new LinkedList();
            count = 0;
            
            while (rs.next()) {
                HashMap asHashMap = new HashMap();
                asHashMap.put("aff_pk", new Integer(rs.getInt("aff_pk")));
                asHashMap.put("person_pk", new Integer(rs.getInt("person_pk")));
                asList.add(asHashMap);
                count++;
            }
            
            // there are affiliate staff records to update
            if (count > 0) {
                
                if (logger.isDebugEnabled())
                    logger.debug("Update Org_Location_Aff_Staff statement: [" 
                                    + SQL_UPDATE_ORG_LOCATION_AFF_STAFF + "] ");

                Iterator it3 = asList.iterator();
                while (it3.hasNext()) {

                    HashMap asHashMap = (HashMap)it3.next();
                    Integer asAffPK = (Integer) asHashMap.get("aff_pk");
                    Integer asPersonPK = (Integer) asHashMap.get("person_pk");

                    // set the orgLocationPK to null
                    ps2 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION_AFF_STAFF);
                    ps2.setInt(1, asAffPK.intValue());
                    ps2.setInt(2, asPersonPK.intValue());
                    ps2.executeUpdate();

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Updated aff_pk = [" + asAffPK + "] and person_pk = [" 
                                        + asPersonPK + "] to set pos_addr_from_org_pk to NULL! ");
                }
                
                ps2.close();
                ps2 = null;                
            }

            rs.close();
            rs = null;
            ps1.close();
            ps1 = null;

            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Total Number of AFF_STAFF records set to NULL = " + count 
                                + " for Org_Locations_PK = [" + orgLocationPK + "]");


            //
            // EXT_ORG_ASSOCIATES
            // 
            if (logger.isDebugEnabled())
                logger.debug("Select Org_Location_Ext_Org_Associates statement: [" 
                            + SQL_SELECT_ORG_LOCATION_EXT_ORG_ASSOCIATES + "] ");

            ps1 = con.prepareStatement(SQL_SELECT_ORG_LOCATION_EXT_ORG_ASSOCIATES);

            // retrieve all of the organization associates key for orgLocationPK
            ps1.setInt(1, orgLocationPK.intValue());
            rs = ps1.executeQuery();
            
            LinkedList eoaList = new LinkedList();
            count = 0;
            
            while (rs.next()) {
                HashMap eoaHashMap = new HashMap();
                eoaHashMap.put("org_pk", new Integer(rs.getInt("org_pk")));
                eoaHashMap.put("person_pk", new Integer(rs.getInt("person_pk")));
                eoaList.add(eoaHashMap);
                count++;
            }
            
            // there are org associate records to update
            if (count > 0) {
                
                if (logger.isDebugEnabled())
                    logger.debug("Update Org_Location_Ext_Org_Associates statement: [" 
                                    + SQL_UPDATE_ORG_LOCATION_EXT_ORG_ASSOCIATES + "] ");

                Iterator it4 = eoaList.iterator();
                while (it4.hasNext()) {

                    HashMap eoaHashMap = (HashMap)it4.next();
                    Integer asOrgPK = (Integer) eoaHashMap.get("org_pk");
                    Integer asPersonPK = (Integer) eoaHashMap.get("person_pk");

                    // set the orgLocationPK to null
                    ps2 = con.prepareStatement(SQL_UPDATE_ORG_LOCATION_EXT_ORG_ASSOCIATES);
                    ps2.setInt(1, asOrgPK.intValue());
                    ps2.setInt(2, asPersonPK.intValue());
                    ps2.executeUpdate();

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Updated org_pk = [" + asOrgPK + "] and person_pk = [" 
                                        + asPersonPK + "] to set org_locations_pk to NULL! ");
                }
                
                ps2.close();
                ps2 = null;                
            }

            rs.close();
            rs = null;
            ps1.close();
            ps1 = null;

            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Total Number of EXT_ORG_ASSOCIATES records set to NULL = " + count 
                                + " for Org_Locations_PK = [" + orgLocationPK + "]");
            
            
            //
            // ORG_LOCATIONS
            // 
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Delete Org_Location statement (with Org_Address and Org_Phones: [" 
                                + SQL_DELETE_ORG_LOCATION + "] Org_PK = " + orgPK 
                                + " and Org_Locations_PK = " + orgLocationPK );
            
            ps1 = con.prepareStatement(SQL_DELETE_ORG_LOCATION);
           
            // delete the organization location data by orgLocationPK
            ps1.setInt(1, orgLocationPK.intValue());

            // delete from Org_Locations record and delete cascade for Org_Address and Org_Phones
            ps1.executeUpdate();
            
            // if orgPK is an affiliate then call the change history
            if (isAffiliate) {
                affiliatesBean.recordChangeToHistory(oldLocation, newLocation, updatedByUserPk);
            }            
            
        } catch (SQLException e) {
            throw new EJBException("Error deleting Org Locations (includes Addresses and Phones) MaintainOrgLocationsBean.removeOrgLocation()", e);
        }
        finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, rs);
        }
        
        // DEBUG
        if (logger.isDebugEnabled())
            logger.debug("DELETED Org_Locations PK (with associated Org_Addresses and Org_Phones) : [" + 
                            orgLocationPK + "] ");
    }
    
    /**
     * Adds a new location to an organization by first creating the location and then associating
     *  the new location with the organization. 
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @return orgLocationPK primary key of the newly created location
     * @param orgPK - primary key of the organization to which the location is to be added.
     *  Note that the organization can be either an affiliate or an external organization
     * @param locationData - a LocationData object which contains data to create the new
     *  location with. As the location does not exist yet, the OrgLocationPK will be null
     *  in this object.  Note that the organization can be either an affiliate or an 
     *  external organization.
     * @param createdByUserPk - primary key of the user that added the new location.
     */
    public Integer addOrgLocation(LocationData locationData, Integer orgPK, Integer createdByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        Integer orgLocationPK = null;

        // attributes for affiliate change history
        boolean isAffiliate = false;
        LocationData oldLocation = null;
        LocationData newLocation = null;
        
        // for saving old primary orgLocationPk
        Integer previousPrimaryPK = null;
        LocationData primaryOldLocation = null;
        LocationData primaryNewLocation = null;        

        try {

            // check to see if orgPK is an affiliate 
            if (isAffiliate(orgPK)) {
                isAffiliate = true;
            }

            // find out the primary location pk for the organization if primary is being set
            if ((locationData.getPrimaryLocationBoolean() != null) && 
                (locationData.getPrimaryLocationBoolean().booleanValue() == true)) {
                    
                previousPrimaryPK = getOrgPrimaryLocationPK(orgPK);
                primaryOldLocation = getOrgPrimaryLocation(orgPK);
            }

            con = DBUtil.getConnection();
           
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert Org_Location statement: [" + SQL_INSERT_NEW_ORG_LOCATION + "] ");

            // insert into Org_Locations
            ps1 = con.prepareStatement(SQL_INSERT_NEW_ORG_LOCATION);

            // set the values for the location add
            ps1.setString(1, locationData.getLocationNm());

            if ((locationData.getPrimaryLocationBoolean() == null) || 
                (locationData.getPrimaryLocationBoolean().booleanValue() == false)) 
                ps1.setNull(2, Types.SMALLINT);
            else
                DBUtil.setNullableBooleanAsShort(ps1, 2, locationData.getPrimaryLocationBoolean());            

            ps1.setInt(3, orgPK.intValue());
             
            ps1.setInt(4, createdByUserPk.intValue());
            ps1.setInt(5, createdByUserPk.intValue());
            orgLocationPK = DBUtil.insertAndGetIdentity(con, ps1);


            //
            // ORG_ADDRESS
            //
            
            // loop for all the addresses (should only be two - Regular, Shipping)
            if (locationData.getOrgAddressData() != null) {
                
                LinkedList addressList = locationData.getOrgAddressData();
                Iterator it = addressList.iterator();
                while (it.hasNext()) {

                    OrgAddressRecord orgAddress = (OrgAddressRecord)it.next();

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Insert Org_Address statement: [" + SQL_INSERT_NEW_LOCATION_ADDRESS + "] ");

                    // Add the Location Addresses 
                    ps2 = con.prepareStatement(SQL_INSERT_NEW_LOCATION_ADDRESS);

                    ps2.setInt(1, orgLocationPK.intValue());
                    
                    writeAdd(ps2, orgAddress);
                    
                    ps2.setInt(15, createdByUserPk.intValue());
                    ps2.setInt(16, createdByUserPk.intValue());
                    
                    Integer orgAddressPK = DBUtil.insertAndGetIdentity(con, ps2);

                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("New Org_Address PK: [" + orgAddressPK + "] ");

                    ps2.close();
                    ps2 = null;
                }            
            }              
                
                
            //
            // ORG_PHONE
            //

            // loop for all the phones (should only be two - Office, Fax)
            if (locationData.getOrgPhoneData() != null) {

                LinkedList phoneList = locationData.getOrgPhoneData();
                Iterator it = phoneList.iterator();
                while (it.hasNext()) {

                    OrgPhoneData orgPhone = (OrgPhoneData)it.next();
        
                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("Insert Org_Phone statement: [" + SQL_INSERT_NEW_LOCATION_PHONE + "] ");

                    // Add the Location Phones 
                    ps2 = con.prepareStatement(SQL_INSERT_NEW_LOCATION_PHONE);

                    ps2.setInt(1, orgLocationPK.intValue());
                    
                    writeAdd(ps2, orgPhone);
                    
                    ps2.setInt(9, createdByUserPk.intValue());
                    ps2.setInt(10, createdByUserPk.intValue());

                    Integer orgPhonePK = DBUtil.insertAndGetIdentity(con, ps2);
                    
                    // DEBUG
                    if (logger.isDebugEnabled())
                        logger.debug("New Org_Phone PK: [" + orgPhonePK + "] ");

                    ps2.close();
                    ps2 = null;
                }            
            }
            
            // unset the old primary location since a new primary has been chosen
            if (previousPrimaryPK != null) {

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Update old Primary Org_Location statement: [" 
                                    + SQL_UPDATE_OLD_PRIMARY_ORG_LOCATION 
                                    + "] - orgPK: " + previousPrimaryPK );

                ps1.close();
                ps1 = null;
                    
                ps1 = con.prepareStatement(SQL_UPDATE_OLD_PRIMARY_ORG_LOCATION);

                // update the primary flag for the old primary orgLocationPK
                ps1.setNull(1, Types.SMALLINT);                
                ps1.setInt(2, createdByUserPk.intValue());            
                ps1.setInt(3, previousPrimaryPK.intValue());
                ps1.executeUpdate();
            }
            
            // if orgPK is an affiliate then call the change history
            if (isAffiliate) {
                newLocation = getOrgLocation(orgLocationPK);
                affiliatesBean.recordChangeToHistory(oldLocation, newLocation, createdByUserPk);

                // if updated previous primary, then call the change history too
                if (previousPrimaryPK != null) {
                    primaryNewLocation = getOrgLocation(previousPrimaryPK);
                    affiliatesBean.recordChangeToHistory(primaryOldLocation, primaryNewLocation, createdByUserPk);
                }                
            }            
            
        } catch (SQLException e) {
            throw new EJBException("Error inserting Org Locations (with Addresses and Phones) MaintainOrgLocationsBean.addOrgLocation()", e);
        } finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);
        }
        
        // DEBUG
        if (logger.isDebugEnabled())
            logger.debug("New Org_Locations PK: [" + orgLocationPK + "] ");
        
        // Return the new Organization Location PK
        return orgLocationPK;
    }
    
    /**
     * returns the information for a single location by OrgLocationPK, versus for an organization
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @return LocationData - an object that contains data about the location
     * @param OrgLocationPK - the primary key of the location
     */
    public LocationData getOrgLocation(Integer OrgLocationPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LocationData locationData = null;

        try {
            con = DBUtil.getConnection();
            
            //
            // ORG_LOCATION
            //
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Location Select by OrgLocationPK statement: [" + SQL_SELECT_ORG_LOCATION_BY_PK + "] "
                            + "criteria [" + OrgLocationPK.toString() + "] ");
            
            //retrieve the Location of an Organization for the OrgLocationPK
            ps = con.prepareStatement(SQL_SELECT_ORG_LOCATION_BY_PK);

            // retrieve the location by pk                  
            ps.setInt(1, OrgLocationPK.intValue());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                locationData = new LocationData();
                locationData.setOrgPk(new Integer(rs.getInt("org_pk")));                
                locationData.setOrgLocationPK(OrgLocationPK);
                locationData.setLocationNm(rs.getString("location_nm"));
                locationData.setPrimaryLocationBoolean(DBUtil.getBooleanFromShort(rs.getShort("location_primary_fg")));
                
                RecordData recordData = new RecordData();
                recordData.setCreatedDate(rs.getTimestamp("created_dt"));
                recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                locationData.setRecordData(recordData);
            }
            
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            // check for org address or phone only if location exists
            if (locationData != null) {
                
                //
                // ORG_ADDRESS
                //

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Location Address Select by OrgLocationPK statement: [" + SQL_SELECT_LOCATION_ADDRESSES + "] "
                                + "criteria [" + locationData.getOrgLocationPK().toString() + "] ");

                // retrieve the addresses for the location by org locations key
                ps = con.prepareStatement(SQL_SELECT_LOCATION_ADDRESSES);
                ps.setInt(1, locationData.getOrgLocationPK().intValue());
                rs = ps.executeQuery();

                // create the storage for the location addresses
                LinkedList locationAddresses = null;

                while (rs.next()) {
                    if (locationAddresses == null)
                        locationAddresses = new LinkedList();

                    OrgAddressRecord orgAddress = null;
                    orgAddress = read(rs, new OrgAddressRecord());
                    locationAddresses.add(orgAddress);
                }

                if (locationAddresses != null) {
                    locationData.setOrgAddressData(locationAddresses);                
                }    

                rs.close();
                rs = null;
                ps.close();
                ps = null;

                //
                // ORG_PHONE
                //

                // DEBUG
                if (logger.isDebugEnabled())
                    logger.debug("Location Phone Select by OrgLocationPK statement: [" + SQL_SELECT_LOCATION_PHONES + "] "
                                + "criteria [" + locationData.getOrgLocationPK().toString() + "] ");

                // retrieve the phones for location by org locations key
                ps = con.prepareStatement(SQL_SELECT_LOCATION_PHONES);
                ps.setInt(1, locationData.getOrgLocationPK().intValue());
                rs = ps.executeQuery();

                // create the storage for the location phones
                LinkedList locationPhones = null;

                while (rs.next()) {
                    if (locationPhones == null)
                        locationPhones = new LinkedList();

                    OrgPhoneData orgPhone = null;
                    orgPhone = read(rs, new OrgPhoneData());                

                    locationPhones.add(orgPhone);
                }

                if (locationPhones != null) {
                    locationData.setOrgPhoneData(locationPhones);                
                }
            }            
        } catch (SQLException e) {
            throw new EJBException("Error getting Location by OrgLocationPK MaintainOrgLocationsBean.getOrgLocation()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return locationData;
    }
    
    /**
     * returns the orgLocationPK for the primary location of the organization. 
     * Returns null if no location is primary for that organization (which includes no 
     * existing locations for that organization)
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @param orgPK - the primary key of the organization (which can be an affiliate 
     * or external organization)
     * @return Integer - the primary key of the location
     */
    public Integer getOrgPrimaryLocationPK(Integer orgPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer orgPrimaryLocationPK = null;
        int count = 0;

        //retrieve the Primary Location PK for an Organization
        try {
            //make the statement that gets the OrgLocationPK
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRIMARY_ORG_LOCATION_PK);

            // set the organization key
            ps.setInt(1, orgPK.intValue());
            
            //get the count
            rs = ps.executeQuery();
            
            while (rs.next()) {
                orgPrimaryLocationPK = new Integer(rs.getInt("org_locations_pk"));
                count++;
            }            

            // return null if more than one primary location
            if (count > 1)
                return null;
            

        } catch (SQLException e) {
            throw new EJBException("Error finding PK for Org Primary Location MaintainOrgLocationsBean.getOrgPrimaryLocationPK()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return orgPrimaryLocationPK;
    }
    
    /**
     * Determines whether a primary location exists for the organization. The organization
     *  may be an affiliate or an external organization
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return boolean - TRUE means a primary location exists for this location. FALSE means
     *  that no primary location exists for this organization, which can also mean that
     *  no location exists 
     * @param orgPK - the primary key of the organization
     */
    public boolean isPrimaryLocation(Integer orgPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        //retrieve the Primary Location for an Organization for the Pk
        try {
            //make the statement that gets the count
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRIMARY_ORG_LOCATION_COUNT);

            // set the organization key
            ps.setInt(1, orgPK.intValue());
            
            //get the count
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count > 0)
                return true;

        } catch (SQLException e) {
            throw new EJBException("Error finding if Org has Primary Location MaintainOrgLocationsBean.isPrimaryLocation()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return false;
    }
    
    /**
     * Determines if an Organization has at least one Location associated with it.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param orgPK Organization Primary Key
     * @return TRUE if at least one Location exists for the Organization, and FALSE if 
     * there are no Location for the Organization.
     */
    public boolean hasLocations(Integer orgPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        //retrieve all Locations for an Organization for the Pk
        try {
            //make the statement that gets the count
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORG_LOCATIONS_COUNT);

            // set the organization key
            ps.setInt(1, orgPK.intValue());
            
            //get the count
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count > 0)
                return true;

        } catch (SQLException e) {
            throw new EJBException("Error finding if Org has Locations MaintainOrgLocationsBean.hasLocations()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return false;
    }
    
    /**
     * returns the orgLocationPK for a location address. 
     * Returns null if no location exists.  
     * This is used for Process Return Mail which only knows the address pk
     * of the location.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @param orgAddressPK - the primary key of the organization address
     * @return Integer - the primary key of the location
     */
    public Integer getOrgLocationPKForLocationAddress(Integer orgAddressPK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer orgLocationPK = null;
        int count = 0;

        //retrieve the org Location PK for an location address
        try {
            //make the statement that gets the OrgLocationPK
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORG_LOCATIONS_BY_LOC_ADDR_PK);

            // set the org address key
            ps.setInt(1, orgAddressPK.intValue());
            
            //get the count
            rs = ps.executeQuery();
            
            while (rs.next()) {
                orgLocationPK = new Integer(rs.getInt("org_locations_pk"));
                count++;
            }            

            // return null if more than one location for address
            if (count > 1)
                return null;
            

        } catch (SQLException e) {
            throw new EJBException("Error finding PK for Location by Address PK MaintainOrgLocationsBean.getOrgLocationPKForLocationAddress()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return orgLocationPK;
    }
    
    /**
     * Reads address data from the given result set into the given address object.
     *
     * @return Address - returns the filled in object
     * @param rs Result Set to extract data from
     * @param address an Address object that contains the data for the set 
     */
    protected Address read(ResultSet rs, Address address) throws SQLException
    {
        address.setAddr1(rs.getString("addr1"));
        address.setAddr2(rs.getString("addr2"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setZipCode(rs.getString("zipcode"));
        address.setZipPlus(rs.getString("zip_plus"));
        address.setCountryPk(new Integer(rs.getInt("country")));
        address.setCounty(rs.getString("county"));
        address.setCity(rs.getString("city"));
        address.setProvince(rs.getString("province"));
        
        return address;
    }
    
    /**
     * Reads address data from the given result set into the given organization address object.
     * @return The orgAddress object provided, filled in
     *
     * @return OrgAddressData - returns the filled in object
     * @param rs Result Set to extract data from
     * @param orgAddress an OrgAddressData object that contains the data for the set 
     */
    protected OrgAddressData read(ResultSet rs, OrgAddressData orgAddress) throws SQLException
    {
        read(rs, (Address)orgAddress);
        orgAddress.setType(new Integer(rs.getInt("org_addr_type")));
        orgAddress.setAttentionLine(rs.getString("attention_line"));
        orgAddress.setBad(rs.getInt("addr_bad_fg") == 1);
        if (orgAddress.isBad())
            orgAddress.setBadDate(rs.getTimestamp("addr_bad_dt"));
        else
            orgAddress.setBadDate(null);
   
        return orgAddress;
    }

    /**
     * Reads record data from the given result set into the given record data object.
     *
     * @return RecordData - returns the filled in object
     * @param rs Result Set to extract data from
     * @param recordData a RecordData object that contains the data for the set 
     */
    protected RecordData read(ResultSet rs, RecordData recordData) throws SQLException
    {
        recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
        recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
        recordData.setCreatedDate(rs.getTimestamp("created_dt"));
        recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
        
        return recordData;
    }
    
    /**
     * Reads the OrgAddressRecord from the given result set into the given data object.
     *
     * @return OrgAddressRecord - returns the filled in object
     * @param rs Result Set to extract data from
     * @param orgAddressRecord an OrgAddressRecord object that contains the data for the set 
     */
    protected OrgAddressRecord read(ResultSet rs,  OrgAddressRecord orgAddressRecord) throws SQLException
    { 
        read(rs, (OrgAddressData)orgAddressRecord);
        
        RecordData recordData = new RecordData();
        recordData.setPk(new Integer(rs.getInt("org_addr_pk")));        
        
        read(rs, recordData);
        orgAddressRecord.setRecordData(recordData);
        
        return orgAddressRecord;
    }

    /**
     * Reads phone data from the given result set into the given phone object.
     */
    protected OrgPhoneData read(ResultSet rs, OrgPhoneData phone) throws SQLException
    {
        phone.setPhoneType(new Integer(rs.getInt("org_phone_type")));
        phone.setCountryCode(rs.getString("country_cd"));
        phone.setAreaCode(rs.getString("area_code"));
        phone.setPhoneNumber(rs.getString("phone_no"));
        phone.setPhoneExtension(rs.getString("phone_extension"));
        phone.setPhoneBadFlag(new Boolean(rs.getInt("phone_bad_fg") == 1));
        if (phone.getPhoneBadFlag().booleanValue())
            phone.setPhoneBadDate(rs.getTimestamp("phone_bad_dt"));
        else
            phone.setPhoneBadDate(null);        

        RecordData recordData = new RecordData();
        recordData.setPk(new Integer(rs.getInt("org_phone_pk")));        
        
        read(rs, recordData);
        phone.setTheRecordData(recordData);
        
        return phone;
    }
    
    /**
     * Sets the address data from the given orgAddressRecord object into the prepared statement.
     * Used for the Insert of a new one.
     *
     * @return PreparedStatement - returns the modified Prepared Statement
     * @param ps Prepared Statement to add values
     * @param address an OrgAddressRecord object that contains the values to set 
     */
    protected PreparedStatement writeAdd(PreparedStatement ps, OrgAddressRecord address) throws SQLException
    {                    
        ps.setInt(2, address.getType().intValue());

        DBUtil.setNullableVarchar(ps, 3, address.getAttentionLine());
        ps.setString(4, address.getAddr1());
        DBUtil.setNullableVarchar(ps, 5, address.getAddr2());
        DBUtil.setNullableVarchar(ps, 6, address.getCity());
        DBUtil.setNullableChar(ps, 7, address.getState());
        DBUtil.setNullableVarchar(ps, 8, address.getZipCode());
        DBUtil.setNullableVarchar(ps, 9, address.getZipPlus());
        DBUtil.setNullableVarchar(ps, 10, address.getCounty()); 
        DBUtil.setNullableVarchar(ps, 11, address.getProvince());
        if ((address.getCountryPk() == null) || (address.getCountryPk().intValue() == 0))
            ps.setNull(12, Types.INTEGER);
        else        
            DBUtil.setNullableInt(ps, 12, address.getCountryPk());
        DBUtil.setNullableBooleanAsShort(ps, 13, new Boolean(address.isBad()));
        if (address.isBad()) 
            ps.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
        else    
            ps.setNull(14, Types.TIMESTAMP);
        
        return ps;
    }
        
    /**
     * Sets the address data from the given orgAddressRecord object into the prepared stmt.
     * Used for the Insert of a new one.
     *
     * @return PreparedStatement - returns the modified Prepared Statement
     * @param ps Prepared Statement to add values
     * @param phone an OrgPhoneData object that contains the values to set 
     */
    protected PreparedStatement writeAdd(PreparedStatement ps, OrgPhoneData phone) throws SQLException
    {                    
        ps.setInt(2, phone.getPhoneType().intValue());

        DBUtil.setNullableVarchar(ps, 3, phone.getCountryCode());
        DBUtil.setNullableVarchar(ps, 4, phone.getAreaCode());
        ps.setString(5, phone.getPhoneNumber());

        // set phone extension as null until PAC
        ps.setNull(6, Types.VARCHAR);

        DBUtil.setNullableBooleanAsShort(ps, 7, phone.getPhoneBadFlag());
        if ((phone.getPhoneBadFlag() != null) && (phone.getPhoneBadFlag().booleanValue())) 
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
        else    
            ps.setNull(8, Types.TIMESTAMP);
        
        return ps;
    }
    
    /**
     * Checks if orgPK is an Affiliate (not External Organization) 
     * to determine if change history is needed.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports" 
     *
     * @return boolean - TRUE means organization is an Affiliate. FALSE means
     *  organization is an External Organization. 
     * @param orgPk Organization Primary Key
     */
    public boolean isAffiliate(Integer orgPk) throws SQLException 
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isAffiliate = false;        

        // check for if an affiliate
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORG_SUBTYPE);

            ps.setInt(1, orgPk.intValue());
            
            //get the org_subtype
            rs = ps.executeQuery();
            rs.next();
            Integer orgSubType = new Integer(rs.getInt("org_subtype"));
            
            if (orgSubType.equals(OrganizationSubType.A))
                isAffiliate = true;
            else
                isAffiliate = false;
            
        } 
        finally {
            DBUtil.cleanup(con, ps, rs);  
        }
        
        return isAffiliate;
    }    
        
}