package org.afscme.enterprise.organization.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.naming.NamingException;
import javax.naming.Context;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.rmi.RemoteException;
import javax.ejb.*;
import org.apache.log4j.Logger;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.ValueSortedMap;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.mailinglists.MailingListData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.common.RecordData;

/**
 * Provides services for associating organizations with mailing lists and removing organizations
 * from mailing lists. Services provided can be used for both affiliates and external
 * organizations
 *
 * @ejb:bean name="MaintainOrgMailingLists" display-name="MaintainOrgMailingLists"
 * jndi-name="MaintainOrgMailingLists"
 * type="Stateless" view-type="local"
 */
public class MaintainOrgMailingListsBean extends SessionBase {
    
    private static Logger log = Logger.getLogger(MaintainOrgMailingListsBean.class);
    
    /** Common codes description for Organization address with address type = regular */
    private static final String COMMON_CODE_ORG_ADDRESS_TYPE_REGULAR  = "Regular";
    
    /** Name of the list of mailing list codes/names, as cached in JNDI */
    private static final String JNDI_ORG_MAIL_CODES = "OrgMailCodes";
    
    /** Selects organization mailing lists */
    private static final String SQL_SELECT_ORGANIZATION_MAILING_LISTS =
    "SELECT a.mlbo_mailing_list_pk, mlbo_mailing_list_nm, mailing_list_bulk_cnt, org_locations_pk " +
    "FROM Mailing_List_Orgs a " +
    "JOIN Mailing_Lists_by_Orgs b on a.mlbo_mailing_list_pk = b.mlbo_mailing_list_pk " +
    "WHERE a.org_pk=? ";
    
    /** Selects organization primary location */
    private static final String SQL_SELECT_PRIMARY_LOCATION =
    "SELECT location_primary_fg, org_locations_pk " +
    "FROM Org_Locations " +
    "WHERE org_pk=? ";
    
    /** Selects all mailing list names */
    private static final String SQL_SELECT_MAILING_LIST_NAMES =
    "SELECT * " +
    "FROM Mailing_Lists_by_Orgs " +
    "ORDER BY mlbo_mailing_list_pk ";
    
    /** Selects the mailing list name */
    private static final String SQL_SELECT_MAILING_LIST_NAME =
    "SELECT mlbo_mailing_list_nm " +
    "FROM Mailing_Lists_by_Orgs " +
    "WHERE mlbo_mailing_list_pk=? ";
    
    /** Selects all the locations of an organization */
    private static final String SQL_SELECT_ORGANIZATION_LOCATIONS =
    "SELECT * " +
    "FROM  Org_Locations " +
    "WHERE org_pk=? ";
    
    
    /** Selects the location address that is not bad address and is type regular */
    private static final String SQL_SELECT_ORGANIZATION_LOCATION_ADDRESS =
    "SELECT * " +
    "FROM Org_Locations a, Org_Address b " +
    "JOIN Common_Codes c on b.org_addr_type = c.com_cd_pk " +
    "WHERE a.org_locations_pk = b.org_locations_pk " +
    "and a.org_locations_pk=? " +
    "and b.addr_bad_fg <> 1 " +
    "and c.com_cd_desc=? ";
    
    /** Selects the address of an organization */
    private static final String SQL_SELECT_ORGANIZATION_ADDRESS =
    "SELECT a.org_locations_pk, location_nm " +
    "FROM  Org_Locations a, Org_Address b " +
    "WHERE a.org_locations_pk = b.org_locations_pk and org_pk=? and a.org_locations_pk=? ";
    
    /** Selects the organization name */
    private static final String SQL_SELECT_ORGANIZATION_NAME =
    "SELECT org_nm " +
    "FROM External_Organizations " +
    "WHERE org_pk=? ";
    
    /** Adds organization to mailing list w/ bulk count */
    private static String SQL_INSERT_ORG_MAILING_LIST =
    "INSERT Mailing_List_Orgs " +
    "(org_pk, mlbo_mailing_list_pk, org_locations_pk, mailing_list_bulk_cnt, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) " +
    "VALUES(?, ?, ?, ?, ?, ?, ?, ?) " ;
    
    /** Update the mailing list bulk count */
    private static final String SQL_UPDATE_BULK_COUNT =
    "UPDATE Mailing_List_Orgs " +
    "SET mailing_list_bulk_cnt=?, lst_mod_dt=?, lst_mod_user_pk=? " +
    "WHERE org_pk=? AND mlbo_mailing_list_pk=? ";
    
    /** Update the mailing list location */
    private static final String SQL_UPDATE_MAILING_LIST_LOCATION =
    "UPDATE Mailing_List_Orgs " +
    "SET org_locations_pk=?, lst_mod_dt=?, lst_mod_user_pk=? " +
    "WHERE org_pk=? AND mlbo_mailing_list_pk=? ";
    
    /** Removes organization from mailing list */
    private static final String SQL_DELETE_ORG_MAILING_LIST =
    "DELETE FROM Mailing_List_Orgs " +
    "WHERE org_pk=? AND mlbo_mailing_list_pk=? ";
    
    
    
    /**
     * @J2EE_METHOD  --  getMailingLists
     * Retrieves the set of mailing lists to which the organization is already attached.
     *
     * Returns a list of OrgMailingListData objects.
     *
     * Note that the UI displays the Location Title, while the database table stores the
     *  PK of the address selected(since there can be more than one address per location).
     *  Therefore the implementation will have to join across several tables in order to
     *  retrieve the Location Title.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return List of OrgMailingListData objects
     * @param orgPK - primary key of the organization for which the mailing lists are to
     *  be returned
     */
    public List getMailingLists(Integer orgPK) {
        List list = new LinkedList();
        MailingListData result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_MAILING_LISTS);
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            Integer orgLocationsPk;
            while (rs.next()) {
                result = new MailingListData();
                result.setMailingListPk(new Integer(rs.getInt(1)));
                result.setMailingListNm(rs.getString(2));
                result.setMailingListBulkCount(rs.getInt(3));
                orgLocationsPk = new Integer(rs.getInt(4));
                if (orgLocationsPk != null) {
                    Map orgAddress = getOrgAddress(orgPK, orgLocationsPk);
                    if (orgAddress != null) {
                        Object key = orgAddress.keySet().iterator().next();
                        Integer addressPk = (Integer)key;
                        result.setAddressPk(addressPk);
                        result.setLocationNm((String) orgAddress.get(key));
                    }
                }
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }
    
    /**
     * @J2EE_METHOD  --  removeMailingList
     * Remove the organization from a particular mailing list. Returns TRUE if the
     * association could be broken, FALSE if the Organization could not be removed
     * from the Mailing List
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return boolean
     * @param orgPK - the primary key of the organization to be removed from the mailing
     *  list.
     * @param mailingListPK - the primary key of the mailing list to be removed
     */
    public boolean removeMailingList(Integer orgPK, Integer mailingListPK) {
        Connection con = null;
        PreparedStatement ps = null;
        int result=0;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_DELETE_ORG_MAILING_LIST);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, mailingListPK.intValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return (result>0) ? true : false;
    }
    
    /**
     * @J2EE_METHOD  --  addMailingList
     * Adds the organization to a particular mailing list and adds bulk mailing count
     * to the association.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param orgPK - primary key of the organization to be added to the mailing list
     * @param mailingListPK- primary key of the mailing list
     * @param bulkCount - an integer representing the number of copies (e.g.
     * magazines, letters, postcards) to be sent to the location
     * @param mailingListPK
     * @param locationPK
     * @param userPK - primary key of the user who is currently logged in
     * Location Association.
     */
    public void addMailingList(Integer orgPK, Integer mailingListPK, int bulkCount, Integer locationPK, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_ORG_MAILING_LIST);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, mailingListPK.intValue());
            DBUtil.setNullableInt(ps, 3, locationPK);
            ps.setInt(4, bulkCount);
            setRecordData(ps, userPK);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Mail code for this organization is already exists, do update instead
            setMailingListLocation(orgPK, mailingListPK, locationPK, userPK);
            updateMailingListBulkCount(orgPK, mailingListPK, bulkCount, userPK);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return;
    }
    
    /**
     * @J2EE_METHOD  --  addMailingList
     * Adds the organization to a particular mailing list and adds bulk mailing count
     * to the association.
     *
     * bulkCount for the organization is set to 1 when this method is called, as this
     * is the default when none is specified through the UI
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param orgPK - primary key of the organization to be added to the mailing list
     * @param mailingListPK- primary key of the mailing list
     * @param locationPK- primary key of the location
     * @param userPK - primary key of the user who is currently logged in
     *
     */
    public void addMailingList(Integer orgPK, Integer mailingListPK, Integer locationPK, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_ORG_MAILING_LIST);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, mailingListPK.intValue());
            DBUtil.setNullableInt(ps, 3, locationPK);
            ps.setInt(4, 1);
            setRecordData(ps, userPK);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Mail code for this organization is already exists, do update instead
            setMailingListLocation(orgPK, mailingListPK, locationPK, userPK);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return;
    }
    
    /**
     * @J2EE_METHOD  --  getMailingListNames
     * Retrieves the set of Organization Mailing List names in total. Returns a collection
     *  of MailingList objects, where each object represents a Mailing List. Takes no parameter,
     *  returns the entire set of organization mailing list names
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return Map - a set of MailingList objects
     */
    public Map getMailingListNames() {
        Map mailingLists;
        
        //
        // Look for the organization mail codes in JNDI
        //
        
        try {
            mailingLists = (Map)JNDIUtil.getObject(JNDI_ORG_MAIL_CODES);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
        
        if (mailingLists != null)
            return mailingLists;
        
        //
        // Organization Mail Codes weren't in JNDI, load them from the database
        //
        mailingLists = new HashMap();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MAILING_LIST_NAMES);
            rs = ps.executeQuery();
            while (rs.next()) {
                mailingLists.put(toString().valueOf(rs.getInt("mlbo_mailing_list_pk")), rs.getString("mlbo_mailing_list_nm"));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        mailingLists = ValueSortedMap.create(mailingLists);
        
        //
        // Put the Organization Mail Codes in JNDI
        //
        try {
            JNDIUtil.setObject(JNDI_ORG_MAIL_CODES, mailingLists);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
        
        return mailingLists;
    }
    
    /**
     * @J2EE_METHOD  --  getMailingListName
     * Retrieves the Organization Mailing List name
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return String  - the Mailing List Name
     */
    public String getMailingListName(Integer mailingListPK) {
        String mailingListNm = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MAILING_LIST_NAME);
            ps.setInt(1, mailingListPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                mailingListNm = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return mailingListNm;
    }
    
    /**
     * @J2EE_METHOD  --  setMailingListLocation
     * Sets the Location for an organization on a mailing list. If the organization is already
     *  associated with a location for that mailing list, this operation replaces the existing
     *  location
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return void
     * @param orgPK - primary key for the organization
     * @param mailingListPK - primary key of the organization mailing list
     * @param locationPK - primary key of the Location to be associated with the organization
     *  on the specified mailing list
     * @param userPK - primary key of the user who is currently logged in
     */
    public void setMailingListLocation(Integer orgPK, Integer mailingListPK, Integer locationPK, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_MAILING_LIST_LOCATION);
            DBUtil.setNullableInt(ps, 1, locationPK);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, userPK.intValue());
            ps.setInt(4, orgPK.intValue());
            ps.setInt(5, mailingListPK.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  updateMailingListBulkCount
     * Updates the bulk count for an existing organization on a mailing list
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return void
     * @param orgPK - the primary key of the organization on the mailing list - note that
     *  the organization may be either an affiliate or external organization
     * @param mailingListPK - the primary key of the mailing list
     * @param bulkCount - an integer representing the number of items to be shipped to the
     *  organization on this mailing list
     * @param userPK - primary key of the user who is currently logged in
     */
    public void updateMailingListBulkCount(Integer orgPK, Integer mailingListPK, int bulkCount, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_BULK_COUNT);
            ps.setInt(1, bulkCount);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, userPK.intValue());
            ps.setInt(4, orgPK.intValue());
            ps.setInt(5, mailingListPK.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  getOrgPrimaryLocation
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return Integer
     * @param orgPK - primary key for the organization
     */
    public Integer getOrgPrimaryLocation(Integer orgPK) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer primaryLoc=null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRIMARY_LOCATION);
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                boolean primary = rs.getBoolean(1);
                if (primary) {
                    primaryLoc = new Integer(rs.getInt(2));
                }
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return primaryLoc;
    }
    
    /**
     * gets the set of locations for the organization. Returns a list of OrgAddressData
     * objects
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return list - of OrgAddressData objects
     * @param orgPK - An Integer representing the primary key of the organization in question
     */
    public List getOrgLocations(Integer orgPK) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrgAddressRecord orgAddr = null;
        List list = new LinkedList();
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_LOCATIONS);
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                orgAddr = getOrgLocationAddress(new Integer(rs.getInt("org_locations_pk")));
                if (orgAddr != null) {
                    list.add(orgAddr);
                }
            }
        } catch (SQLException e) {
            throw new EJBException("SQL error in MaintainOrgMailingListsBean.getOrgLocations()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return list;
    }
    
    /**
     * gets the organization address type 'Regular' and bad address flag is not set.
     * Returns the OrgAddressData object
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return list - of OrgAddressData objects
     * @param orgLocationsPK - An Integer representing the primary key of the organization location
     */
    public OrgAddressRecord getOrgLocationAddress(Integer orgLocationsPK) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrgAddressRecord orgAddr = null;
        RecordData recordData = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_LOCATION_ADDRESS);
            ps.setInt(1, orgLocationsPK.intValue());
            ps.setString(2, COMMON_CODE_ORG_ADDRESS_TYPE_REGULAR);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                orgAddr = new OrgAddressRecord();
                recordData = new RecordData();
                orgAddr.setBadDate(rs.getTimestamp("addr_bad_dt"));
                orgAddr.setBad(rs.getBoolean("addr_bad_fg"));
                orgAddr.setAttentionLine(rs.getString("attention_line"));
                orgAddr.setLocationNm(rs.getString("location_nm"));
                orgAddr.setPrimary(rs.getBoolean("location_primary_fg"));
                orgAddr.setPrivate(false);
                orgAddr.setAddr1(rs.getString("addr1"));
                orgAddr.setAddr2(rs.getString("addr2"));
                orgAddr.setCity(rs.getString("city"));
                orgAddr.setState(rs.getString("state"));
                orgAddr.setZipCode(rs.getString("zipcode"));
                orgAddr.setZipPlus(rs.getString("zip_plus"));
                orgAddr.setCountryPk(new Integer(rs.getInt("country")));
                orgAddr.setCounty(rs.getString("county"));
                orgAddr.setCity(rs.getString("city"));
                orgAddr.setProvince(rs.getString("province"));
                recordData.setPk(orgLocationsPK);
                recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                recordData.setCreatedDate(rs.getTimestamp("created_dt"));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                orgAddr.setRecordData(recordData);
            }
        } catch (SQLException e) {
            throw new EJBException("SQL error in MaintainOrgMailingListsBean.getOrgLocationAddress()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return orgAddr;
    }
    
    /**
     * gets the address for an organization.
     *
     * @param orgPK - An Integer representing the primary key of the organization in question
     * @param orgLocationsPK - An Integer representing the primary key of the org locations
     */
    public Map getOrgAddress(Integer orgPK, Integer orgLocationsPK) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map result = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_ADDRESS);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, orgLocationsPK.intValue());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                result = new HashMap();
                result.put(new Integer(rs.getInt(1)), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new EJBException("SQL error in MaintainOrgMailingListsBean.getOrgAddress()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    
    /**
     * @J2EE_METHOD  --  getOrgName
     * Retrieves the Organization name
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return String  - the Organization Name
     */
    public String getOrgName(Integer orgPK) {
        String orgNm = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_NAME);
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                orgNm = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return orgNm;
    }
    
    public static void setRecordData(PreparedStatement ps, Integer userPK) throws SQLException {
        ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
        ps.setInt(6, userPK.intValue());
        ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
        ps.setInt(8, userPK.intValue());
    }
}