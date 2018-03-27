package org.afscme.enterprise.address.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.PersonAddressRecord;
import org.afscme.enterprise.address.ZipCodeEntry;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.member.MemberAffiliateResult;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.Codes.Country;
import org.afscme.enterprise.codes.Codes.PersonAddressType;
import org.afscme.enterprise.codes.Codes.Department;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.organization.ejb.MaintainOrgLocations;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;

/**
 * Handles maintainence of person system addresses.  Does not yet handle affiliate or
 *  organization address.
 *
 * @ejb:bean name="SystemAddress" display-name="SystemAddress"
 *              jndi-name="SystemAddress"
 *              type="Stateless" view-type="local"
 */
public class SystemAddressBean extends SessionBase
{
    /** Get's all the zip codes (preffered cities only) */
    private static final String SQL_GET_ZIPCODES =
        "SELECT city, state, zipcode FROM COM_ZipCityState_vld WHERE pref='P'";

    /** Gets a person address */
    private static final String SQL_SELECT_PERSON_ADDRESS =
        " SELECT * FROM Person_Address WHERE address_pk=? ";

    /** Gets a person's system address */
    private static final String SQL_SELECT_PERSON_SYSTEM_ADDRESS =
        " SELECT * FROM " +
        " Person_Address a INNER JOIN Person_SMA s ON a.address_pk = s.address_pk " +
        " WHERE current_fg=1 AND s.person_pk=? ";

    /** Sets the current SMA for a person  */
    private static final String SQL_INSERT_PERSON_SMA =
        " DECLARE @nextseq int " +
        " SET @nextseq = isnull((SELECT sequence FROM Person_SMA WHERE current_fg=1 AND person_pk=?), 0) + 1 " +
        " UPDATE Person_SMA SET current_fg=0 WHERE current_fg=1 AND person_pk=? " +
        " INSERT INTO Person_SMA  " +
        " (address_pk, person_pk, sequence, determined_dt, current_fg) " +
        " VALUES  " +
        " (?         , ?        , @nextseq,  getdate()   ,  1) ";

    /** Inserts a new address record */
/* OLD STMT
    private static final String SQL_INSERT_ADDRESS =
    	" INSERT INTO Person_Address " +
        " (addr1, addr2, city, state, zipcode, zip_plus, carrier_route_info, country, county, lst_mod_dt, addr_prmry_fg, addr_private_fg, created_user_pk, created_dt, addr_source, addr_type, province, person_pk, dept, lst_mod_user_pk, addr_source_if_aff_apply_upd) " +
        " VALUES " +
        " (?    , ?    , ?   , ?    , ?      , ?       , ?                 , ?      , ?     , getdate() , ?            , ?              , ?               , ?        , ?          , ?        , ?       , ?        , ?   , ?              , ?                           ) ";
        //(1      2      3     4      5        6         7                   8        9                   10             11               12                13         14           15         16        17         18    19               20
*/
        /** Inserts a new address record */
    private static final String SQL_INSERT_ADDRESS =
    	" INSERT INTO Person_Address " +
        " (addr1, addr2, city, state, zipcode, zip_plus, carrier_route_info, country, county, lst_mod_dt, addr_prmry_fg, addr_private_fg, created_user_pk, created_dt, addr_source, addr_type, province, person_pk, dept, lst_mod_user_pk, addr_source_if_aff_apply_upd, addr_bad_fg) " +
        " VALUES " +
        " (?    , ?    , ?   , ?    , ?      , ?       , ?                 , ?      , ?     , getdate() , ?            , ?              , ?               , ?        , ?          , ?        , ?       , ?        , ?   , ?              , ?                           , ?) ";
        //(1      2      3     4      5        6         7                   8        9                   10             11               12                13         14           15         16        17         18    19               20                           21

    
    
    /** Gets the current and prior SMA for a person */
    private static final String SQL_SELECT_PRIOR_SMAS =
        " SELECT * FROM " +
        " Person_Address a INNER JOIN Person_SMA s on a.address_pk = s.address_pk " +
        " WHERE sequence >= ((SELECT sequence FROM Person_SMA WHERE current_fg=1 AND person_pk=?) - 1) " +
        " and a.person_pk = ? ORDER BY sequence ";

    /** Gets the current address for a given source+type+department */
    private static final String SQL_SELECT_CURRENT_ADDRESS =
        " SELECT * FROM Person_Address " +
        " WHERE person_pk=? AND addr_source=? AND addr_type=? AND ? = isnull(dept, 0) AND ? = isnull(addr_source_if_aff_apply_upd, 0)" +
        " AND (end_dt IS NULL OR end_dt > getdate()) ";

    /** Clears the 'primary' flag for all address of a person+department, except the given address */
    private static final String SQL_CLEAR_PRIMARY =
        " UPDATE Person_Address " +
        " SET addr_prmry_fg=0 WHERE person_pk=? AND ?=isnull(dept,0) AND address_pk != ? ";

    /** Gets all the addresses of person */
    private static final String SQL_SELECT_PERSON_ADDRESSES =
        " SELECT * FROM Person_Address WHERE address_pk IN " +
        " (SELECT MAX(address_pk) from Person_Address WHERE person_pk=? " +
        " AND (end_dt IS NULL OR end_dt > getdate()) " +
        " GROUP BY addr_source, addr_type, dept) ";

    /** Sets the end_dt of an address to now */
    private static final String SQL_INACTIVATE_ADDRESS =
        "UPDATE Person_Address SET end_dt=getdate() WHERE address_pk=?";

    /** Sets the bad flag to true and bad date to now */
    private static final String SQL_MARK_BAD =
        "UPDATE Person_Address SET addr_bad_fg=1, addr_marked_bad_dt=getdate() WHERE address_pk=?";

    /** Select 1 if a the given address is a SMA, otherwise 0*/
    private static final String SQL_SELECT_ADDRESS_IS_SMA =
        " DECLARE @is_sma int " +
        " SET @is_sma=0 " +
        " SELECT @is_sma=1 " +
        " WHERE EXISTS (SELECT address_pk FROM Person_SMA WHERE current_fg=1 AND address_pk=?) " +
        " SELECT @is_sma ";

    /** Selects the primary key of the most recent, mailable, non-private membership or PAC home address for a person */
    private static final String SQL_SELECT_SMA_CANDIDATE =
        " SELECT TOP 1 address_pk from Person_Address " +
        " WHERE addr_private_fg=0 AND addr_bad_fg=0 AND addr_type=" + PersonAddressType.HOME +
        " AND (dept in (" + Department.MD + ", " + Department.PD + " ) )" + //SMA must belong to Membership or PEPOLE ;)
        " AND (end_dt IS NULL OR end_dt > getdate()) " +
        " AND person_pk=? " +
        " ORDER BY lst_mod_dt DESC ";

    private static final String SQL_INSERT_NCOA_ACTIVITY =
    " INSERT INTO NCOA_ACTIVITY " +
    " (NCOA_ACTIVITY_REQUESTED_DT, NCOA_TRANS_RUN_COMPLETED_FG, NCOA_TRANS_RUN_ERROR_FG, CREATED_USER_PK) " +
    " VALUES " +
    " (getdate(), ?, ?, ? ) ";
    
    /** Selects the county fips for a zip code */
    private static final String SQL_SELECT_COUNTY_FIPS =
    " SELECT countyfips FROM COM_Democracy_Ranged " +
    " WHERE zipcode = ? AND start_zip_plus <= ? AND stop_zip_plus >= ? ";

    private Map m_zipCodeEntries;

    private static Logger log = Logger.getLogger(SystemAddressBean.class);

    /**
	 * variables to hold reference to ejb
	 */
	protected MaintainOrgLocations m_maintainOrgLocations;
	protected MaintainMembers m_maintainMembers;

    /**
     * Loads the Zip code lookup table
     */
    public void ejbCreate() throws CreateException {

        //get the zip codes

        m_zipCodeEntries = new HashMap();
        Connection con = null;
        Statement stmt = null;
		ResultSet rs = null;

		try {

			m_maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();
			m_maintainMembers = JNDIUtil.getMaintainMembersHome().create();

            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_GET_ZIPCODES);

            //for each zip code
            while (rs.next()) {
                ZipCodeEntry zce = new ZipCodeEntry();

                //city
                zce.setCity(rs.getString("city"));

                //state
                zce.setState(rs.getString("state"));

                //zip
                zce.setZipCode(rs.getString("zipcode"));

                m_zipCodeEntries.put(zce.getZipCode(), zce);
            }
		} catch (SQLException e) {
			throw new EJBException("Unable to get zip code entries in SystemAddressBean.ejbCreate()" + e);
		}  catch (NamingException e) {
            throw new EJBException(e);
        }
		finally {
            DBUtil.cleanup(con, stmt, rs);
        }

        log.debug(m_zipCodeEntries.size() + " zip codes loaded");
    }

    /**
     * Updates a  person's address with changes made by that person.
     *
     *
     * @param personPk The primary key of the person performing the update (also the owner
     *  of the address)
     * @param address The address data being updated
     * @param addressPk pk of the address being updated, or null if this is a new address
     * @return set of values from the ERROR_XXX constants defined in this class.  The errors
     *  encountered.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Set updateByOwner(Integer personPk, Integer addressPk, Address address)
    {
        //make the appropriate PersonAddress from the Address
        PersonAddress personAddress = new PersonAddress();
        personAddress.copyFrom(address);
        personAddress.setSource(PersonAddress.SOURCE_OWNER);
        personAddress.setType(PersonAddressType.HOME);
        personAddress.setPrimary(true);
        personAddress.setPrivate(false);
        personAddress.setDepartment(Department.MD);

        //validate
        Set errors = validate(personAddress);
        if (errors != null)
            return errors;

        //check if this address is different
        if (equalsCurrentFromSource(personPk, personAddress)) {
            log.debug("Address not updated becasue it is equal to the current address from that source+type+department");
            return null;
        }

        //insert
        Integer newPk = insertPersonAddress(personPk, personAddress, personPk);
        if (addressPk != null)
            inactivateAddress(addressPk);

        //set SMA (in this case, valid, unique updates always become the SMA)
        setSystemAddress(personPk, newPk);

        //inactivate prior address
        if (addressPk != null)
            inactivateAddress(addressPk);

        return null;
    }

    /**
     * Adds a person address by an AFSCME International staff user.
     *
     * @param userPk The primary key of the user performing the update
     * @param deptPk The primary key of the department of the user performing the update
     * @param personPk The primary key of the owner of the address
     * @param address The address data being updated
     * @return set of values from the ERROR_XXX constants defined in this class.  The errors
     *  encountered.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Set addByDepartment(Integer userPk, Integer deptPk, Integer personPk, PersonAddress address)
    {
        //validate
        Set errors = validate(address);
        if (errors != null)
            return errors;

        //ensure the source and department are set to the right values
        address.setSource(PersonAddress.SOURCE_AFSCME_STAFF);
        address.setUpdateSource(null);
        address.setDepartment(deptPk);

        //check if this address is different
        if (equalsCurrentFromSource(personPk, address)) {
            log.debug("Address not added becasue it is equal to the current address from that source+type+department");
            return null;
        }

        //insert
        Integer newPk = insertPersonAddress(personPk, address, userPk);

        //SMA determination
        if (address.getType().equals(PersonAddressType.HOME) && !equalsPriorSMAs(personPk, address) && !address.isPrivate())
            setSystemAddress(personPk, newPk);

        return null;
    }

    /**
     * Updates a  person's address with changes made by an AFSCME Internation staff
     * user.
     *
     * @param userPk The primary key of the user performing the update
     * @param deptPk The primary key of the department of the user performing the update
     * @param personPk The primary key of the owner of the address
     * @param addressPk pk of the address being updated, or null if this is a new address
     * @param address The address data being updated
     * @return set of values from the ERROR_XXX constants defined in this class.  The errors
     *  encountered.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Set updateByDepartment(Integer userPk, Integer deptPk, Integer personPk, Integer addressPk, PersonAddress address)
    {
        //validate
        Set errors = validate(address);
        if (errors != null)
            return errors;

        //ensure the source and department are set to the right values
        address.setSource(PersonAddress.SOURCE_AFSCME_STAFF);
        address.setUpdateSource(null);
        address.setDepartment(deptPk);

        //check if this address is different
        if (equalsCurrentFromSource(personPk, address)) {
            log.debug("Address not updated becasue it is equal to the current address from that source+type+department");
            return null;
        }

        //get the old address (the one we're updating)
        PersonAddressRecord oldAddress = getPersonAddress(addressPk);

        //can't unset the primary flag (the UI enfoces this, but re-inforce here)
        if (oldAddress.isPrimary())
            address.setPrimary(true);

        //insert new
        Integer newPk = insertPersonAddress(
            personPk,
            address,
            userPk,
            oldAddress.getRecordData().getCreatedBy(),
            oldAddress.getRecordData().getCreatedDate());

        //remove old
        inactivateAddress(addressPk);

        //SMA determination
        if (address.getType().equals(PersonAddressType.HOME) &&
            !equalsPriorSMAs(personPk, address) &&
            !address.isPrivate() &&
            !address.isBad()) {

            //The new address becomes the SMA
            setSystemAddress(personPk, newPk);
        }
        else if (isSMA(addressPk)) {

            //the old address was the SMA, determine the new one
            Integer newSMA = getSMACandidate(personPk);
            if (newSMA != null) {
                setSystemAddress(personPk, newSMA);
            } else {
                //the old (and inactive) addres remains the SMA.
            }
        }

        return null;
    }


    /**
     * Adds a person's address with changes made by an affiliate staff user.
     *
     * @param userPk The primary key of the user performing the update
     * @param affPk The primary key of the affiliate performing the update
     * @param personPk The primary key of the owner of the address
     * @param address The address data being updated
     * @return set of values from the ERROR_XXX constants defined in this class.  The errors
     *  encountered.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Set addByAffiliate(Integer userPk, Integer affPk, Integer personPk, PersonAddress address)
    {
        //validate
        Set errors = validate(address);
        if (errors != null)
            return errors;

        //ensure the source and department are set to the right values
        address.setSource(PersonAddress.SOURCE_AFFILIATE_STAFF);
        address.setUpdateSource(affPk);
        address.setDepartment(null);

        //check if this address is different
        if (equalsCurrentFromSource(personPk, address)) {
            log.debug("Address not added becasue it is equal to the current address from that source+type+department");
            return null;
        }

        //insert
        Integer newPk = insertPersonAddress(personPk, address, userPk);

        //SMA determination
        if (address.getType().equals(PersonAddressType.HOME) && !equalsPriorSMAs(personPk, address))
            setSystemAddress(personPk, newPk);

        return null;
    }


    /**
     * Updates a  person's address with changes made by an affiliate staff user.
     *
     * @param userPk The primary key of the user performing the update
     * @param affPk The primary key of the affiliate performing the update
     * @param personPk The primary key of the owner of the address
     * @param addressPk pk of the address being updated, or null if this is a new address
     * @param address The address data being updated
     * @return set of values from the ERROR_XXX constants defined in this class.  The errors
     *  encountered.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Set updateByAffiliate(Integer userPk, Integer affPk, Integer personPk, Integer addressPk, PersonAddress address)
    {
        //validate
        Set errors = validate(address);
        if (errors != null)
            return errors;

        //ensure the source and department are set to the right values
        address.setSource(PersonAddress.SOURCE_AFFILIATE_STAFF);
        address.setUpdateSource(affPk);
        address.setDepartment(null);

        //check if this address is different
        if (equalsCurrentFromSource(personPk, address)) {
            log.debug("Address not updated becasue it is equal to the current address from that source+type+department");
            return null;
        }

        //insert
        Integer newPk = insertPersonAddress(personPk, address, userPk);

        //inactivate prior address
        inactivateAddress(addressPk);

        //SMA determination
        if (address.getType().equals(PersonAddressType.HOME) && !equalsPriorSMAs(personPk, address))
            setSystemAddress(personPk, newPk);
        else if (isSMA(addressPk)) {
            Integer newSMA = getSMACandidate(personPk);
            if (newSMA != null) {
                setSystemAddress(personPk, newSMA);
            } else {
                //the old (and inactive) addres remains the SMA.
            }
        }

        return null;
    }

    /**
     * Gets the system address for a person.
     *
     * @param personPk primary key of the person to get the SMA for
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PersonAddressRecord getSystemAddress(Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        PersonAddressRecord addr = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_SYSTEM_ADDRESS);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();

            if (rs.next())
                addr = read(rs, new PersonAddressRecord());

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return addr;

    }

    /**
     * Gets a list of the person's addresses.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return List of PersonAddressRecord objects
     */
    public List getPersonAddresses(Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        List list = new LinkedList();

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_ADDRESSES);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();

            while (rs.next())
                list.add(read(rs, new PersonAddressRecord()));

		} catch (SQLException e) {
			throw new EJBException("SQL error in SystemAddressBean.getPersonAddresses()", e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return list;
    }

    /**
     * Similar to getPersonAddresses(...), but only returns addresses that can be seen by
     *  a user from the given department.
     *
     * @return List of PersonAddressRecord objects.  The first item in the list is the system
     *  address.  This item may be duplicated later in the list.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPersonAddressesForDepartment(Integer personPk, Integer departmentPk)
    {
        List list = getPersonAddresses(personPk);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            PersonAddress addr = (PersonAddress)it.next();
            if (addr.isPrivate() &&
                (addr.getDepartment() != null) &&
                !addr.getDepartment().equals(departmentPk)) {
                    it.remove();
            }
        }

        return list;
    }

    /**
     * Similar to getPersonAddresses(...), but only returns addresses that can be seen by
     *  a user from the given affiliate
     *
     * @return List of PersonAddressRecord objects.  The first item in the list is the system
     *  address.  This item may be duplicated later in the list.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPersonAddressesForAffiliate(Integer personPk, Integer affPk)
    {
        //affPk is not used today.  Leave it in the method in case the rules change ;)

        List list = getPersonAddresses(personPk);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            PersonAddress addr = (PersonAddress)it.next();
            if (addr.isPrivate() && addr.getDepartment() != null)
                it.remove();
        }

        return list;
    }

    /**
     * Gets an individual PersonAddressRecord by primary key.
     *
     * @param addressPk  The primary key of the address to retrieve.
     * @throws EJBException if there is no address with the given addressPk
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PersonAddressRecord getPersonAddress(Integer addressPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        PersonAddressRecord result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON_ADDRESS);
            ps.setInt(1, addressPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
                result = read(rs, new PersonAddressRecord());
		} catch (SQLException e) {
			throw new EJBException("SQL error in SystemAddressBean.getPersonAddressesForDepartment()", e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        if (result == null)
            throw new EJBException("No address found with pk="+addressPk+" in SystemAddressBean.getPersonAddress()");

        return result;
    }

    /**
     * Deletes an address.
     *
     * @param personPk The primary key of the user performing the deleteion.
     * @param deptPk The primary key of the department of the user performing the deleteion.
     * @param addressPk  The primary key of the address to inactivate.
     * @throws EJBException if this is the person SMA
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void deleteAddress(Integer personPk, Integer departmentPk, Integer addressPk)
    {
        if (isSMA(addressPk)) {
            //this should never happen, because the UI should not allow a delete operation for the SMA
            throw new EJBException("Address [" + addressPk + "] Can't be deleted, it is a System Mailing Address");
        }

        Integer addressDepartment = getPersonAddress(addressPk).getDepartment();
        if (addressDepartment == null || !addressDepartment.equals(departmentPk)) {
            //this should never happen, because the UI should not allow a delete operation from users not in the department of the address
            throw new EJBException("Address [" + addressPk + "] Can't be deleted, it the deleting user is in the wrong department");
        }

        inactivateAddress(addressPk);
    }

    /**
     * Expires an address (sets the end_dt to now)
     *
     * @param addressPk  The primary key of the address to inactivate.
     */
    protected void inactivateAddress(Integer addressPk)
    {
        Connection con = null;
        PreparedStatement ps = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INACTIVATE_ADDRESS);
            ps.setInt(1, addressPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Gets an individual PersonAddressRecord by primary key.  Checks that the given department
     *  can access the address.
     *
     * @param addressPk  The primary key of the address to retrieve.
     * @param deptPk The primary key of the department of the user accessing the address.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PersonAddressRecord getPersonAddressForDepartment(Integer addressPk, Integer deptPk)
    {
        PersonAddressRecord address = getPersonAddress(addressPk);
        if (address.isPrivate() &&
            address.getDepartment() != null &&
            !address.getDepartment().equals(deptPk)) {
                throw new EJBException("User form department " + deptPk + " cannot access address with pk " + addressPk);
        }

        return address;
    }

    /**
     * Gets an individual PersonAddressRecord by primary key.  Checks that the given affiliate
     *  can access the address.
     *
     * @param addressPk  The primary key of the address to retrieve.
     * @param affPk The primary key of the affiliate accessing the address.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PersonAddressRecord getPersonAddressForAffiliate(Integer addressPk, Integer affPk)
    {
        PersonAddressRecord address = getPersonAddress(addressPk);
        if (address.isPrivate() &&
            address.getDepartment() != null)
        {
                throw new EJBException("User form affiliate " + affPk + " cannot access address with pk " + addressPk);
        }

        return address;
    }

    /**
     * Marks an address as bad
     *
     * @param addrPk primary key of the address to mark as bad
     * @param userPk
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void markBad(Integer addressPk)
    {
        Connection con = null;
        PreparedStatement ps = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_MARK_BAD);
            ps.setInt(1, addressPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }

        if (isSMA(addressPk)) {
            Integer personPk = getPersonAddress(addressPk).getPersonPk();
            //the old address was the SMA, determine the new one
            Integer newSMA = getSMACandidate(personPk);
            if (newSMA != null) {
                setSystemAddress(personPk, newSMA);
            } else {
                //the old (bad) addres remains the SMA.
            }
        }
    }

    /**
	 * Validates PerosnAddress the address info.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
	 * @param address The address to validate.  <b>This parameter may be updated</b>
     * @return A set of Address.ERROR_XXX Strings if an error occurs, othewise null.
	 */
    public Set validate(PersonAddress address)
    {
        Set errors = validate((Address)address);

        if (address.isPrimary() && !address.getType().equals(PersonAddressType.HOME)) {
            if (errors == null) {
                errors = new TreeSet();
            }
            errors.add(Address.ERROR_PRIMARY_NOT_HOME);
        }

        //If address is of type HOME, and zip and zip plus exist,
        //county is determined by the Democracy table.
        if (address.getType().equals(PersonAddressType.HOME) &&
            !TextUtil.isEmpty(address.getZipCode()) &&
            !TextUtil.isEmpty(address.getZipPlus())) {
                String countyFIPS = lookupCountyFIPS(address.getZipCode(), address.getZipPlus());
                if (countyFIPS != null)
                    address.setCounty(countyFIPS);
        }

        if (errors != null)
            return errors;
        else
            return null;
    }

    /**
	 * Validates the address info.
	 *
     * Checks:<br>
     *
     * <li>City is required
     * <li>If the Country is USA, the State and Zip Code are required
     * <li>If the Country is Canada, Province is required.
     *
     * Updates:<br>
     *
     * <li>If city or state are not provided, they may be automatically filled in from the zip code
     *   (The address parameter will be modified upon return from this method)
     * <li>Country defaults to US
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
	 * @param address The address to validate.  <b>This parameter may be updated</b>
     * @return A set of Address.ERROR_XXX Strings if an error occurs, othewise null.
     */
    public Set validate(Address address)
    {
        Set errors = new TreeSet();
        ZipCodeEntry zipEntry = lookupZipCode(address.getZipCode());

        //default country to US
        if (address.getCountryPk() == null)
            address.setCountryPk(Country.US);

        //fill in data from zip entry
        if (zipEntry != null) {
            if (TextUtil.isEmpty(address.getCity()))
                address.setCity(zipEntry.getCity());
            if (TextUtil.isEmpty(address.getState()))
                address.setState(zipEntry.getState());
            else if (!address.getState().equals(zipEntry.getState()))
                errors.add(Address.ERROR_STATE_ZIP_MISMATCH);
        }
        
        //check US specific rules
        if (address.getCountryPk().equals(Country.US)) {
            if (zipEntry == null)
                errors.add(Address.ERROR_ZIPCODE_INVALID);
            if (address.getState() == null)
                errors.add(Address.ERROR_STATE_EMPTY);
            if (TextUtil.isEmpty(address.getZipCode()))
                errors.add(Address.ERROR_ZIPCODE_EMPTY);
        }

        //check Canada specific rules
        if (address.getCountryPk().equals(Country.CA)) {
            if (TextUtil.isEmpty(address.getProvince()))
                errors.add(Address.ERROR_PROVINCE_EMPTY);
        }

        if (TextUtil.isEmpty(address.getCity()))
            errors.add(Address.ERROR_CITY_EMPTY);

        return errors.isEmpty() ? null : errors;
    }

    /**
     * Gets a list of affiliate addresses (for use in validating a person address)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return A list of PersonAddressRecord objects.
     */
    public Map getAffiliateAddresses(Integer affPk)
    {
        return null;
    }

    /**
     * Retrieves the ZipCodeEntry for the given zip code.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param zipCode The zip code to look up.
     * @return ZipCodeEntry object, or null if the zip code is not found
     */
    public ZipCodeEntry lookupZipCode(String zipCode)
    {
        return  (ZipCodeEntry)m_zipCodeEntries.get(zipCode);
    }

    /**
     * Designates the given address as the system address.
     *
     * @param personPk The primary key of the person to set the SMA for
     * @param addrPk  The primary key of the address to set as the SMA
     */
    protected void setSystemAddress(Integer personPk, Integer addressPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        Integer newPk = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PERSON_SMA);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, personPk.intValue());
            ps.setInt(3, addressPk.intValue());
            ps.setInt(4, personPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException("Error setting person SMA", e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

    /**
     * Inserts a new address into the database.
     *
     * @param personPk primary key of the person the address belongs to
     * @param createdByUserPk primary key of the person that created the record
     * @param address the address content
     * @return The pk of the address
     */
    protected Integer insertPersonAddress(Integer personPk, PersonAddress personAddress, Integer modifiedBy)  {
        return insertPersonAddress(personPk, personAddress, modifiedBy, modifiedBy, new Timestamp(System.currentTimeMillis()));
    }


    /**
     * Inserts a new address into the database.
     *
     * @param personPk primary key of the person the address belongs to
     * @param createdByUserPk primary key of the person that created the record
     * @param address the address content
     * @return The pk of the address
     */
    protected Integer insertPersonAddress(Integer personPk, PersonAddress personAddress, Integer modifiedBy, Integer createdBy, Timestamp createdDate)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        Integer newPk = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_ADDRESS);
            ps.setString(1, personAddress.getAddr1());
            ps.setString(2, personAddress.getAddr2());
            ps.setString(3, personAddress.getCity());
            ps.setString(4, personAddress.getState());
            ps.setString(5, personAddress.getZipCode());
            if(TextUtil.isEmpty(personAddress.getZipPlus())) {
				ps.setNull(6, Types.CHAR);
			}
			else {
            	ps.setString(6, personAddress.getZipPlus());
			}
            ps.setString(7, personAddress.getCarrierRoute());
            ps.setInt(8, personAddress.getCountryPk().intValue());
            ps.setString(9, personAddress.getCounty());
            //lst_mod_date=getdate()
            ps.setShort(10, ((short)(personAddress.isPrimary() ? 1 : 0)));
            ps.setShort(11, ((short)(personAddress.isPrivate() ? 1 : 0)));
            ps.setInt(12, createdBy.intValue());
            ps.setTimestamp(13, createdDate);
            ps.setString(14, String.valueOf(personAddress.getSource()));
            ps.setInt(15, personAddress.getType().intValue());
            ps.setString(16, personAddress.getProvince());
            ps.setInt(17, personPk.intValue());
            DBUtil.setNullableInt(ps, 18, personAddress.getDepartment());
            ps.setInt(19, modifiedBy.intValue());
            DBUtil.setNullableInt(ps, 20, personAddress.getUpdateSource());
            ps.setInt(21, ((int)(personAddress.isBad() ? 1 : 0)));
            newPk = DBUtil.insertAndGetIdentity(con, ps);

		} catch (SQLException e) {
			throw new EJBException("Error inserting address SystemAddressBean.insertPersonAddress()", e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        if (personAddress.isPrimary())
            clearPrimary(personPk, personAddress.getDepartment(), newPk);

        return newPk;
    }

    /**
     * Reads address data from the given result set into the given address object.
     */
    protected Address read(ResultSet rs, Address address) throws SQLException
    {
        address.setAddr1(rs.getString("addr1"));
        address.setAddr2(rs.getString("addr2"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setZipCode(rs.getString("zipcode"));
        address.setZipPlus(rs.getString("zip_plus"));
        address.setCarrierRoute(rs.getString("carrier_route_info"));
        address.setCountryPk(new Integer(rs.getInt("country")));
        address.setCounty(rs.getString("county"));
        address.setCity(rs.getString("city"));
        address.setProvince(rs.getString("province"));

        return address;
    }

    /**
     * Reads address data from the given result set into the given person address object.
     * @return The personAddress object provided, filled in
     */
    protected PersonAddress read(ResultSet rs, PersonAddress personAddress)  throws SQLException
    {
        read(rs, (Address)personAddress);
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

        return personAddress;
    }

    /**
     * Reads record data from the given result set into the given record data object.
     */
    protected RecordData read(ResultSet rs, RecordData recordData) throws SQLException
    {
        recordData.setPk(new Integer(rs.getInt("address_pk")));
        recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
        recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
        recordData.setCreatedDate(rs.getTimestamp("created_dt"));
        recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));

        return recordData;
    }

    /**
     * Reads the PersonAddressRecord from the given result set into the given data object.
     */
    protected PersonAddressRecord read(ResultSet rs,  PersonAddressRecord personAddressRecord) throws SQLException
    {
        read(rs, (PersonAddress)personAddressRecord);
        RecordData recordData = new RecordData();

        personAddressRecord.setPersonPk(new Integer(rs.getInt("person_pk")));

        read(rs, recordData);
        personAddressRecord.setRecordData(recordData);

        return personAddressRecord;
    }

    /**
     * Gets a list of the current and prior SMA for a person.
     * The returned list may contain 0, 1 or 2 items, if 2 the prior SMA is first
     *
     * @param personPk The primary key of the person to retrieve SMA's for.
     */
    protected List getPriorSMAs(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        List list = new LinkedList();

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRIOR_SMAS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(read(rs, new PersonAddress()));
            }

		} catch (SQLException e) {
			throw new EJBException("SQL error in SystemAddressBean.getPriorSMA()", e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return list;
    }

    /**
     * Gets the current address for a given person+source+type+department
     */
    protected PersonAddress getCurrentAddress(Integer personPk, char source, Integer type, Integer dept, Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        PersonAddress result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CURRENT_ADDRESS);
            ps.setInt(1, personPk.intValue());
            ps.setString(2, String.valueOf(source));
            ps.setInt(3, type.intValue());
            ps.setInt(4, dept == null ? 0 : dept.intValue());
            ps.setInt(5, affPk == null ? 0 : affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
                result = read(rs, new PersonAddress());

		} catch (SQLException e) {
			throw new EJBException("SQL error in SystemAddressBean.getCurrentAddress()", e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }

    /**
     * Returnes true iff the given address is equivalent to the person's current address for that source+type+department
     */
    protected boolean equalsCurrentFromSource(Integer personPk, PersonAddress address) {
        PersonAddress current = getCurrentAddress(
                personPk,
                address.getSource(),

                address.getType(),

                address.getDepartment(),

                address.getUpdateSource());

        return (current != null && current.contentEquals(address));
    }

    /**
     * Returnes true iff the given address is equivalent to the person's current or prior SMA
     */
    protected boolean equalsPriorSMAs(Integer personPk, PersonAddress address)
    {
        Iterator it = getPriorSMAs(personPk).iterator();
        while (it.hasNext()) {
            PersonAddress iter = (PersonAddress)it.next();
            if (iter.contentEquals(address))
                return true;
        }
        return false;
    }

    protected void clearPrimary(Integer personPk, Integer departmentPk, Integer addrPk) {
        Connection con = null;
        PreparedStatement ps = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_CLEAR_PRIMARY);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, departmentPk == null ? 0 : departmentPk.intValue());
            ps.setInt(3, addrPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Returns true iff the the given address is a current SMA
     *
     * @param addressPk The primary key of a user to exclude from the check
     * @return true if no other user is using this id
     */
    protected boolean isSMA(Integer addressPk) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            con = DBUtil.getConnection();

            stmt = con.prepareStatement(SQL_SELECT_ADDRESS_IS_SMA);
            stmt.setInt(1, addressPk.intValue());
            rs = stmt.executeQuery();

            rs.next();
            result = rs.getInt(1) == 1;
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }

        return result;
    }


    /**
     * Gets the most recent, non-private, mailable address for a person
     *
     * @param addressPk  The primary key of the address to retrieve.
     * @returns address_pk or null if no candidate exists
     */
    protected Integer getSMACandidate(Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        Integer result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_SMA_CANDIDATE);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
                result = new Integer(rs.getInt(1));
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }


    /**
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @parm personPk :  current user of the system
    **/
    public void insertNcoaActivity(int personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
	ResultSet rs = null;
        Integer newPk = null;

	try
        {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_NCOA_ACTIVITY);
            ps.setInt(1, 0);
            ps.setInt(2, 0);
            ps.setInt(3, personPk);
            newPk = DBUtil.insertAndGetIdentity(con, ps);
        } catch (SQLException e) {
                throw new EJBException("Error inserting NCOA process.insertNcoaActivity()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

	/**
     * Indicates whether the address passed in is equal to one of the person's affiliate's addreses.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param par The Person's address to be compared.
     * @return Flag indicating whether the address is equal.
     */
    public boolean equalsAffiliateAddress(PersonAddressRecord par)
    {
		Collection memberAffiliateResults = m_maintainMembers.getMemberAffiliation(par.getPersonPk());
		Iterator i = memberAffiliateResults.iterator();
		while(i.hasNext()){
			MemberAffiliateResult memberAffiliateResult = (MemberAffiliateResult)i.next();
			Collection orgLocations = m_maintainOrgLocations.getOrgLocations(memberAffiliateResult.getAffPk());
			Iterator iterator = orgLocations.iterator();
			while(iterator.hasNext()) {
				LocationData locationData = (LocationData)iterator.next();
				Collection orgAddresses = locationData.getOrgAddressData();
				Iterator list = orgAddresses.iterator();
				while(list.hasNext()) {
					OrgAddressRecord orgAddressRecord = (OrgAddressRecord)list.next();
					if(par.equals(orgAddressRecord)) {
						return true;
					}
				}
			}
		}
		return false;
    }

    /**
     * Looks up a county fips code from a zip code and zip plus.
     *
     * @param zipCode zip code as string, must not be null
     * @param zipPlus zip plus as stirng, must not be null
     * @return county fips String if found, otherwise null
    */
    public String lookupCountyFIPS(String zipCode, String zipPlus)
    {
        Connection con = null;
        PreparedStatement ps = null;
	ResultSet rs = null;
        String result = null;

	try
        {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNTY_FIPS);
            ps.setString(1, zipCode);
            ps.setString(2, zipPlus);
            ps.setString(3, zipPlus);
            rs = ps.executeQuery();
            if (rs.next())
                result = rs.getString(1);
        } catch (SQLException e) {
                throw new EJBException("Error looking up county fips", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
}


