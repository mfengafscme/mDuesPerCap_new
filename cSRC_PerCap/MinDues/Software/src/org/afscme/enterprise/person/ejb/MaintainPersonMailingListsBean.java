package org.afscme.enterprise.person.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import javax.naming.NamingException;
import javax.naming.Context;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.ValueSortedMap;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.mailinglists.MailingListData;
import org.afscme.enterprise.reporting.specialized.MailingListReport;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.person.CorrespondenceData;
import java.rmi.RemoteException;
import javax.ejb.*;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.apache.log4j.Logger;


/**
 * Encapsulates access to process person mailing lists data.
 *
 * @ejb:bean name="MaintainPersonMailingLists" display-name="MaintainPersonMailingLists"
 * jndi-name="MaintainPersonMailingLists"
 * type="Stateless" view-type="local"
 */

public class MaintainPersonMailingListsBean extends SessionBase {
    
    private static Logger log = Logger.getLogger(MaintainPersonMailingListsBean.class);
    
    /** Name of the list of mailing list codes/names, as cached in JNDI */
    private static final String JNDI_PERSON_MAIL_CODES = "PersonMailCodes";
    
    /** Select person mailing lists */
    private static String SQL_SELECT_PERSON_MAILING_LISTS =
    "SELECT a.mlbp_mailing_list_pk, mlbp_mailing_list_nm, address_pk " +
    "FROM MLBP_Persons a " +
    "JOIN Mailing_Lists_by_Person b on a.mlbp_mailing_list_pk = b.mlbp_mailing_list_pk " +
    "WHERE a.person_pk=?";
    
    /** Select person address type */
    private static String SQL_SELECT_PERSON_ADDRESS_TYPE =
    "SELECT addr_type " +
    "FROM Person_Address " +
    "WHERE address_pk=?";
    
    /** Selects all mailing list names */
    private static final String SQL_SELECT_MAILING_LIST_NAMES =
    "SELECT * " +
    "FROM Mailing_Lists_by_Person " +
    "ORDER BY mlbp_mailing_list_pk";
    
    /** Selects the mailing list name */
    private static final String SQL_SELECT_MAILING_LIST_NAME =
    "SELECT mlbp_mailing_list_nm " +
    "FROM Mailing_Lists_by_Person " +
    "WHERE mlbp_mailing_list_pk=? ";
    
    /** Gets all the addresses of a person with bad address flag is not set */
    private static final String SQL_SELECT_PERSON_ADDRESSES =
    "SELECT * FROM Person_Address WHERE person_pk=? and addr_bad_fg <> 1";
    
    /** Adds person to mailing list */
    private static String SQL_INSERT_PERSON_MAILING_LIST =
    "INSERT MLBP_Persons " +
    "(person_pk, mlbp_mailing_list_pk, address_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) " +
    "VALUES(?, ?, ?, ?, ?, ?, ?) " ;
    
    /** Update the mailing list address */
    private static final String SQL_UPDATE_MAILING_LIST_ADDRESS =
    "UPDATE MLBP_Persons " +
    "SET address_pk=?, lst_mod_dt=?, lst_mod_user_pk=? " +
    "WHERE person_pk=? AND mlbp_mailing_list_pk=? ";
    
    /** Removes person from mailing list */
    private static final String SQL_DELETE_PERSON_MAILING_LIST =
    "DELETE FROM MLBP_Persons " +
    "WHERE person_pk=? AND mlbp_mailing_list_pk=? ";
    
    /** Gets Correspondence History fields for a person or member */
	private static String SQL_SELECT_CORRESPONDENCE_HISTORY =
        " SELECT c.correspondence_dt, " +
        " ISNULL(m.MLBP_Mailing_List_nm, r.report_nm) AS correspondence_name " +
        " FROM Person_Correspondence_History c " +
        " INNER JOIN Report r ON c.report_pk = r.report_pk " +
        " LEFT OUTER JOIN Mailing_Lists_by_Person m ON m.MLBP_mailing_list_pk = c.MLBP_Mailing_List_pk" +
        " WHERE c.person_pk = ? ";

    /** Inserts correspondences for a mailing list */
    private static String SQL_INSERT_MAILING_LIST_CORRESPONDENCES =
        " INSERT INTO Person_Correspondence_History " +
        " (person_pk, correspondence_dt, MLBP_mailing_list_pk, report_pk ) " +
        " SELECT person_pk, getdate(), MLBP_mailing_list_pk, " + MailingListReport.REPORT_PK  +
        " FROM MLBP_Persons " +
        " WHERE MLBP_mailing_list_pk = ?";

    /** Inserts correspondences */
    private static String SQL_INSERT_CORRESPONDENCES =
        "INSERT INTO Person_Correspondence_History " +
        "(person_pk, correspondence_dt, report_pk) " +
        "SELECT person_pk, getdate(), ? " +
        "FROM Person ";
    
    
    /* --------  Bean Creation method ---------------------------------- */
    /** Loads all non-changeable data into local member variables */
    public void ejbCreate() throws CreateException {
    }
    
    public void ejbRemove() {
    }
    
    /**
     * @J2EE_METHOD  --  getPersonMailingLists
     * Retrieves the set of the mailing lists to which a person belongs.
     *
     * @param personPK Person Primary Key
     * @return the List of MailingListData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPersonMailingLists(Integer personPK) {
        List list = new LinkedList();
        MailingListData result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_MAILING_LISTS);
            ps.setInt(1, personPK.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                result = new MailingListData();
                result.setMailingListPk(new Integer(rs.getInt(1)));
                result.setMailingListNm(rs.getString(2));
                result.setAddressPk(new Integer(rs.getInt(3)));
                if (result.getAddressPk() != null) {
                    result.setAddressType(getPersonAddressType(result.getAddressPk()));
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
     * @J2EE_METHOD  --  addPersonMailingList
     * Allows a person to be added to a mailing list.
     *
     * @param personPK Person Primary Key
     * @param mailingListPK Mailing List Code
     * @param addressPK Address Association Primary Key
     * @param userPK - primary key of the user who is currently logged in
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void addPersonMailingList(Integer personPK, Integer mailingListPK, Integer addressPK, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PERSON_MAILING_LIST);
            ps.setInt(1, personPK.intValue());
            ps.setInt(2, mailingListPK.intValue());
            DBUtil.setNullableInt(ps, 3, addressPK);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setInt(5, userPK.intValue());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setInt(7, userPK.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            // Mail code for this person is already exists, do update instead
            setMailingListAddress(personPK, mailingListPK, addressPK, userPK);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  removePersonMailingList
     * Remove the person from a particular mailing list. Returns TRUE if the
     * association could be broken, FALSE if the Person could not be removed
     * from the Mailing List
     *
     * @param personPK Person Primary Key
     * @param mailingListPK Mailing List Code
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean removePersonMailingList(Integer personPK, Integer mailingListPK) {
        Connection con = null;
        PreparedStatement ps = null;
        int result=0;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_DELETE_PERSON_MAILING_LIST);
            ps.setInt(1, personPK.intValue());
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
     * @J2EE_METHOD  --  setMailingListAddress
     * Sets the Address for a person on a mailing list. If the person is already
     * associated with an address for that mailing list, this operation replaces the existing
     * address
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return void
     * @param personPK - primary key for the person
     * @param mailingListPK - primary key of the person mailing list
     * @param addressPK - primary key of the Address to be associated with the person
     * @param userPK - primary key of the user who is currently logged in
     *  on the specified mailing list
     */
    public void setMailingListAddress(Integer personPK, Integer mailingListPK, Integer addressPK, Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_MAILING_LIST_ADDRESS);
            DBUtil.setNullableInt(ps, 1, addressPK);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, userPK.intValue());
            ps.setInt(4, personPK.intValue());
            ps.setInt(5, mailingListPK.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * @J2EE_METHOD  --  getMailingListNames
     * Retrieves the set of Person Mailing List names in total. Returns a collection
     *  of MailingList objects, where each object represents a Mailing List. Takes no parameter,
     *  returns the entire set of person mailing list names
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return Map - a set of MailingList objects
     */
    public Map getMailingListNames() {
        Map mailingLists;
        
        //
        // Look for the person mail codes in JNDI
        //
        try {
            mailingLists = (Map)JNDIUtil.getObject(JNDI_PERSON_MAIL_CODES);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
        
        if (mailingLists != null)
            return mailingLists;
        
        //
        // Person Mail Codes weren't in JNDI, load them from the database
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
                mailingLists.put(toString().valueOf(rs.getInt("mlbp_mailing_list_pk")), (String)rs.getString("mlbp_mailing_list_nm"));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        
        mailingLists = ValueSortedMap.create(mailingLists);
        
        //
        // Put the Person Mail Codes in JNDI
        //
        try {
            JNDIUtil.setObject(JNDI_PERSON_MAIL_CODES, mailingLists);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
        
        return mailingLists;
    }
    
    /**
     * @J2EE_METHOD  --  getMailingListName
     * Retrieves the Person Mailing List name
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
     * gets the address type
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return String - address type
     * @param Integer - address PK
     */
    public Integer getPersonAddressType(Integer addressPK) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer addressType = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_ADDRESS_TYPE);
            ps.setInt(1, addressPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                addressType = new Integer(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new EJBException("SQL error in MaintainPersonMailingListsBean.getPersonAddressType()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return addressType;
    }
    
    /**
     * Gets a list of the person's addresses.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return List of PersonAddressRecord objects
     */
    public List getPersonAddresses(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new LinkedList();
        PersonAddressRecord personAddress = null;
        RecordData recordData = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_ADDRESSES);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                personAddress = new PersonAddressRecord();
                recordData = new RecordData();
                personAddress.setAddr1(rs.getString("addr1"));
                personAddress.setAddr2(rs.getString("addr2"));
                personAddress.setCity(rs.getString("city"));
                personAddress.setState(rs.getString("state"));
                personAddress.setZipCode(rs.getString("zipcode"));
                personAddress.setZipPlus(rs.getString("zip_plus"));
                personAddress.setCarrierRoute(rs.getString("carrier_route_info"));
                personAddress.setCountryPk(new Integer(rs.getInt("country")));
                personAddress.setCounty(rs.getString("county"));
                personAddress.setCity(rs.getString("city"));
                personAddress.setProvince(rs.getString("province"));
                personAddress.setSource(rs.getString("addr_source").charAt(0));
                if (personAddress.getSource() == PersonAddress.SOURCE_AFFILIATE_STAFF)
                    personAddress.setUpdateSource(new Integer(rs.getInt("addr_source_if_aff_apply_upd")));
                else
                    personAddress.setUpdateSource(null);
                personAddress.setType(new Integer(rs.getInt("addr_type")));
                personAddress.setBad(rs.getInt("addr_bad_fg") == 1);
                if (personAddress.isBad())
                    personAddress.setBadDate(rs.getTimestamp("addr_marked_bad_dt"));
                else
                    personAddress.setBadDate(null);
                int dept = rs.getInt("dept");
                personAddress.setDepartment(dept == 0 ? null : new Integer(dept));
                personAddress.setPrimary(rs.getInt("addr_prmry_fg") == 1);
                personAddress.setPrivate(rs.getInt("addr_private_fg") == 1);
                recordData.setPk(new Integer(rs.getInt("address_pk")));
                recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                recordData.setCreatedDate(rs.getTimestamp("created_dt"));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                personAddress.setRecordData(recordData);
                list.add(personAddress);
            }
            
        } catch (SQLException e) {
            throw new EJBException("SQL error in MaintainPersonMailingListsBean.getPersonAddresses()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return list;
    }
    
     /**
     * Adds correspondence data to people's correspondence history.
     *
     * @param mailingListId The unique mailing list used to send the correspondences
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void addCorrespondences(int mailingListId)
    {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection();
		   	ps = con.prepareStatement(SQL_INSERT_MAILING_LIST_CORRESPONDENCES);
		    ps.setInt(1, mailingListId);
		    ps.execute();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(con, ps, null);
		}
    }
    
    /**
     * @J2EE_METHOD  --  getCorrespondenceHistory
     * Retrieves correspondence history data for a person.
     *
     * @param personPK Person Primary Key
     * @return the Collection of CorrespondenceData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getCorrespondenceHistory(Integer personPK, SortData sortData) {
        List list = new LinkedList();
        CorrespondenceData result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sortField = sortData.getSortField() == CorrespondenceData.SORT_BY_DATE ? " correspondence_dt " : " correspondence_name ";
        String direction = sortData.getDirection() == SortData.DIRECTION_DESCENDING ? " DESC " : "";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CORRESPONDENCE_HISTORY + " ORDER BY " + sortField + direction);
            ps.setInt(1, personPK.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                result = new CorrespondenceData();
                result.setCorrespondenceDt(rs.getTimestamp("correspondence_dt"));
                result.setCorrespondenceName(rs.getString("correspondence_name"));
                list.add(result);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }


   
}
