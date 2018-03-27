package org.afscme.enterprise.users.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.controller.ejb.AccessControl;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.users.AffiliateSortData;
import org.afscme.enterprise.users.UserData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.CryptoUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;


/**
 * Handles maintainence of user accounts.
 *
 * @ejb:bean name="MaintainUsers" display-name="MaintainUsers"
 *              jndi-local-name="MaintainUsers"
 *              type="Stateless"  view-type="local"
 */
public class MaintainUsersBean extends SessionBase
{
    /** Gets all the fields in a user account, by userId */
    private static String SQL_SELECT_USER =
       " SELECT person_pk, user_id, pwd, last_session, remarks, lockout_dt, bad_login_attempt_ct, challenge_response, pwd_expiration, start_page, challenge_question, dept " +
       " FROM Users ";

    /** Gets all the fields in a user account, by userId */
    private static String SQL_SELECT_USER_BY_UID =
       SQL_SELECT_USER + " WHERE user_id=?";

    /** Gets all the fields in a user account, by pk */
    private static String SQL_SELECT_USER_BY_PK =
       SQL_SELECT_USER + " WHERE person_pk=?";

    /** Gets the user id field for a user account */
    private static String SQL_SELECT_USER_ID =
       " SELECT user_id FROM  Users WHERE person_pk=?";

    /** Gets the person pk field for a user account */
    private static String SQL_SELECT_PERSON_PK =
       " SELECT person_pk FROM  Users WHERE user_id=?";

    /** Inserts a new user */
    private static final String SQL_INSERT_USER =
        " INSERT INTO Users " +
        " (person_pk, user_id, pwd, last_session, remarks, lockout_dt, bad_login_attempt_ct, challenge_response, pwd_expiration, start_page, challenge_question, dept) " +
        " VALUES (?, ?, ?, null, null, null, 0, null, ?, ?, ?, null)";


    /** Updates a user (not all fields, only ones 'editable' by admins) */
    private static final String SQL_UPDATE_USER =
        " UPDATE Users SET " +
        " user_id=?, remarks=?, challenge_response=?, challenge_question=?, dept=?, start_page=? " +
        " WHERE person_pk = ?";

    /** Selects all the roles for a particular user */
    private static final String SQL_SELECT_USER_ROLES =
        "SELECT person_pk, role_pk FROM User_Roles WHERE person_pk=?";

    /** Deletes the association to roles for a particular user */
    private static final String SQL_DELETE_USER_ROLES =
        "DELETE FROM User_Roles WHERE person_pk=?";

    /** Inserts a role association for a particular user */
    private static final String SQL_INSERT_USER_ROLE =
        "INSERT INTO User_Roles (person_pk, role_pk) VALUES (?, ?)";

    /** Selects all the affiliates for a user */
    private static final String SQL_SELECT_USER_AFFILIATES =
        "SELECT person_pk, aff_pk FROM User_Affiliates WHERE person_pk=?";

    /** Selects the count of user affiliates */
    private static final String SQL_SELECT_USER_AFFILIATE_COUNT =
        "SELECT COUNT(*) FROM User_Affiliates WHERE person_pk=";

    /** Inserts a row into the User_Affiliates table */
    private static final String SQL_INSERT_USER_AFFILIATE =
        "INSERT INTO User_Affiliates (person_pk, aff_pk) VALUES(?, ?)";

    /** Deletes a row from the User_Affiliates table */
    private static final String SQL_DELETE_USER_AFFILIATES =
        "DELETE FROM User_Affiliates WHERE person_pk=? AND aff_pk IN ";

    /** Deletes a rows from the User_Affiliates table that match a specific user */
    private static final String SQL_DELETE_ALL_USER_AFFILIATES =
        "DELETE FROM User_Affiliates WHERE person_pk=?";

    /** Join clause of a query tha selects Affiliate data for a specific user */
    private static final String SQL_JOIN_USER_AFFILIATES =
        " LEFT OUTER JOIN User_Affiliates ON Aff_Organizations.aff_pk = User_Affiliates.aff_pk";

    /** Join clause of a query tha selects Affiliate data *not* associated with a specific user */
    private static final String SQL_JOIN_USER_AFFILIATES_NOT_ASSOCIATED =
        " WHERE NOT EXISTS (" +
        " SELECT person_pk FROM User_Affiliates " +
        " WHERE User_Affiliates.aff_pk = Aff_Organizations.aff_pk AND person_pk =?)";

    /** Select 1 if a diferent user with the given userId exists, otherwise 0*/
    private static final String SQL_SELECT_UID_IS_TAKEN =
        " DECLARE @uid_taken int " +
        " SET @uid_taken=0 " +
        " SELECT @uid_taken=1 " +
        " WHERE EXISTS (SELECT user_id FROM Users WHERE user_id=? AND NOT person_pk=?) " +
        " SELECT @uid_taken ";

    /** Select the count of affiliates */
    private static final String SQL_SELECT_AFFILIATE_COUNT =
        " SELECT COUNT(*) FROM Aff_Organizations ";

    /** Select all fields from affiliates */
    private static final String SQL_SELECT_AFFILIATES =
        " SELECT * FROM Aff_Organizations ";

    /** Gets the name for a pesron */
    private static final String SQL_SELECT_PERSON_NAME =
       " SELECT first_nm, middle_nm, last_nm FROM Person WHERE person_pk=? ";
    
    /** Gets a person's organization's phone number */
    private static final String SQL_SELECT_ORG_PHONE = 
        " SELECT TOP 1 p.country_cd, p.area_code, p.phone_no FROM Org_Phone p " +
        " INNER JOIN Org_Locations l ON p.org_locations_pk = l.org_locations_pk " +
        " LEFT OUTER JOIN Aff_Members m ON l.org_pk = m.aff_pk " +
        " LEFT OUTER JOIN Aff_Staff  s ON l.org_pk = s.aff_pk " +
        " LEFT OUTER JOIN Ext_Org_Associates o ON l.org_pk = o.org_pk " +
        " WHERE phone_bad_fg != 1 AND (m.person_pk = ? OR s.person_pk = ? or o.person_pk = ?) " +
        " ORDER BY org_phone_type ";

    /** Gets all users for a given role */
    private static final String SQL_SELECT_SUPER_USERS =     
        " Select person_pk from Roles a, User_Roles b where a.role_name = 'Super User'  and b.role_pk = a.role_pk ";
    
    /** Static reference to the logger for the class */
    static Logger log = Logger.getLogger(MaintainUsersBean.class);

    /** Reference to the AccessControl ejb */
    protected AccessControl m_accessControl;

    /** Columns to sort on, corresponding to the field ids in AffiliateSortData.  It is important that these are in the same order */
    private final String[] SORT_FIELD_COLUMNS = new String[] {
        "aff_abbreviated_nm", "Aff_type", "CAST(aff_localSubChapter AS int)", "Aff_stateNat_type", "CAST(aff_councilRetiree_chap AS int)", "CAST(aff_subUnit AS int)", "person_pk",
    };


    /**
     * Gets references to the dependent EJBs.
     * This method is called by the container.
     */
    public void ejbCreate() throws CreateException {
        try {
            m_accessControl = JNDIUtil.getAccessControlHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainUsersBean.ejbCreate()" + e);
        }
    }

    /**
     * Removes references to the dependent EJBs
     * This method is called by the container.
     */
    public void ejbRemove() {
        try {
            if (m_accessControl != null)
                m_accessControl.remove();
        } catch (RemoveException e) {
                throw new EJBException("Unable to remove dependent EJBs in MaintainUsersBean.ejbRemove()" + e);
        }
    }

    /**
     * Adds a user to the system.
     *
     * @param personPk The primary key of the person to create a user account for
     * @return the UserData An object representing the newly created user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public UserData addUser (Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        UserData data = new UserData();
        ResultSet rs = null;

        //insert the UserData object into the database
        try {

            //initialize the UserData object
            data.setPasswordExpirationDate(null);
            data.setUserId(String.valueOf(personPk));
            data.setPersonPk(personPk);
            data.setStartPage(UserData.START_PAGE_MEMBER);
            data.setChallengeQuestion(ConfigUtil.getConfigurationData().getDefaultChallengeQuestion());

            //insert the UserData to the database
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_USER);
            ps.setInt(1, data.getPersonPk().intValue());
            ps.setString(2, data.getUserId());
            ps.setString(3, null);
            ps.setTimestamp(4, data.getPasswordExpirationDate());
            ps.setString(5, String.valueOf(data.getStartPage()));
            ps.setInt(6, data.getChallengeQuestion().intValue());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return data;
    }


    /**
     * Gets a UserData object by it's primary key.
     *
     * @param personPk The primary key of the user to retrieve data for
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public UserData getUser(Integer personPk) {
        return getUser((Object)personPk, SQL_SELECT_USER_BY_PK);
    }

    /**
     * Gets a UserData object by it's user id.
     *
     * @param userId The id of the user to retrieve data for
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public UserData getUser(String userId) {
        return getUser((Object)userId, SQL_SELECT_USER_BY_UID);
    }

    /**
     * Runs a query for getting user data, either by primary key, or userId
     *
     * @param key The user's primary key or userId
     * @param sql The SQL string to execute.
     * @return the loaded user data.
     */
    protected UserData getUser(Object key, String sql) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setObject(1, key);
            rs = ps.executeQuery();
            if (rs.next()) {
                data = readUserData(rs);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Reads user data from the given result set and returns it as a UserData object.
     *
     * @param rs The ResultSet to read user data from.
     * @return A new UserData object containing the data from 'rs'
     */
    protected UserData readUserData(ResultSet rs) throws SQLException {
        UserData data = new UserData();
        data.setPasswordExpirationDate(rs.getTimestamp("pwd_expiration"));
        data.setChallengeResponse(rs.getString("challenge_response"));
        data.setChallengeQuestion(new Integer(rs.getInt("challenge_question")));
        data.setLockoutDate(rs.getTimestamp("lockout_dt"));
        data.setBadLoginAttemptCount(rs.getInt("bad_login_attempt_ct"));
        data.setLastLoginDate(rs.getTimestamp("last_session"));
        data.setUserId(rs.getString("user_id"));
        data.setPersonPk(new Integer(rs.getInt("person_pk")));
        int department = rs.getInt("dept");
        if (department != 0)
            data.setDepartment(new Integer(department));
        data.setStartPage(rs.getString("start_page").charAt(0));
        data.setRemarks(rs.getString("remarks"));
        return data;
    }

    /**
     * Updates user data.
     * Only updates UserId, Remarks, ChallengeQuestion, ChallengeResponse, Department and StartPage.
     * Bad login attempts count/date, last session and password information are updated by the AccessControl component.
     *
     * @return -1 if the userId provided is not unique, otherwise 0.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int updateUser(UserData userData)
    {
        Connection con = null;
        PreparedStatement ps = null;

        if (TextUtil.isInt(userData.getUserId()) &&
            !userData.getUserId().equals(String.valueOf(userData.getPersonPk()))) {
                throw new EJBException("Cannot change userId to numeric id, which differs from the user's person id");
        }

        if (isUserIdTaken(userData.getPersonPk(), userData.getUserId()))
            return -1;

        //insert the userData into the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_USER);
            ps.setString(1, userData.getUserId());
            ps.setString(2, userData.getRemarks());
            ps.setString(3, userData.getChallengeResponse());
            DBUtil.setNullableInt(ps, 4, userData.getChallengeQuestion());
            DBUtil.setNullableInt(ps, 5, userData.getDepartment());
            ps.setString(6,String.valueOf(userData.getStartPage()));
            ps.setInt(7, userData.getPersonPk().intValue());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return 0;
    }


    /**
     * Gets the roles for a particular user.
     *
     * @param personPk The primary key of the use who's roles are to be retrieved
     * @return A Set of role primary keys
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getRoles(Integer personPk)
    {
        return DBUtil.getAssociation(personPk, SQL_SELECT_USER_ROLES, "role_pk");
    }

    /**
     * Gets the affiliates associated with a a particular user.
     *
     * @param personPk The primary key of the user who's affiliates are to be retrieved
     * @return A Set of affiliate primary keys
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getAffiliates(Integer personPk)  {
        return DBUtil.getAssociation(personPk, SQL_SELECT_USER_AFFILIATES, "aff_pk");
    }


    /**
     * Gets the number affiliates associated with a particular user.
     *
     * @param personPk The primary key of the use who's affiliates are to be counted
     * @return The number of affiliates a user is associated with.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getAffiliateCount(Integer personPk)
    {
        int affiliateCount = 0;

        try {
            affiliateCount = ((Integer)DBUtil.executeScalar(SQL_SELECT_USER_AFFILIATE_COUNT + personPk)).intValue();
        } catch (SQLException e) {
            throw new EJBException("Could not get affiliate count for user with id " + personPk, e);
        }

        return affiliateCount;
    }

    /**
     * Searches for affiliates related to a user
     *
     * @param personPk The primary key of the user to search related affilaites for
     * @param criteria The search criteria
     * @param sort Specifies how to sort the results
     * @param result Upon return, this will contain a list of AffiliateData objects found in the search
     * @return The number of affiliates a user is associated with.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return the total number of affiliates that match the criteria.
     */
    public int getAffiliates(Integer personPk, AffiliateData criteria, AffiliateSortData sort, List result)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        try {

            //make the statement builder
            PreparedStatementBuilder builder = new PreparedStatementBuilder(2);
            builder.addCriterion("aff_localSubChapter", criteria.getLocal());
            builder.addCriterion("aff_type", criteria.getType());
            builder.addCriterion("aff_councilRetiree_chap", criteria.getCouncil());
            builder.addCriterion("aff_stateNat_type", criteria.getState());
            builder.addCriterion("aff_subUnit", criteria.getSubUnit());

            //Make the join clause
            StringBuffer joinBuffer = new StringBuffer();
            boolean searchOnSelection = false;
            if (criteria.isSelected() != null) {
                searchOnSelection = true;
                if (criteria.isSelected().booleanValue()) {
                    joinBuffer.append(SQL_JOIN_USER_AFFILIATES);
                    joinBuffer.append(" WHERE person_pk=?");
                }
                else {
                    joinBuffer.append(SQL_JOIN_USER_AFFILIATES_NOT_ASSOCIATED);
                }
            } else {
                joinBuffer.append(SQL_JOIN_USER_AFFILIATES);
                joinBuffer.append(" AND person_pk=?");
            }

            String join = joinBuffer.toString();

            //make the statement that gets the count
            con = DBUtil.getConnection();
            ps = builder.getPreparedStatement(SQL_SELECT_AFFILIATE_COUNT + join, con, !searchOnSelection);
            ps.setInt(1, personPk.intValue());

            //get the count
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count == 0)
                return 0;

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            //create the order by clause
            String orderBy = null;
            if (sort != null &&        //<-- don't sort if sort is null
                sort.getSortField() != AffiliateSortData.SORT_NONE &&  //<-- don't sort if 'none' specified
                (!(sort.getSortField() == AffiliateSortData.FIELD_SELECTED && searchOnSelection))) {  //<-- don't sort if sort is on 'selected' and we're not searching by selection

                orderBy = (String)CollectionUtil.transliterate(
                    sort.getSortField(),
                    AffiliateSortData.SORT_FIELD_IDS,
                    SORT_FIELD_COLUMNS);

                if (sort.getSortOrder() == AffiliateSortData.SORT_DESCENDING)
                    orderBy += " DESC";
            }

            if (orderBy != null)
                builder.addOrderBy(orderBy);

            //create the statement that gets the data
            ps = builder.getPreparedStatement(SQL_SELECT_AFFILIATES + join, con, !searchOnSelection);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();

            if (sort != null)
                rs.absolute((sort.getPage() * sort.getPageSize()) + 1);
            else
                rs.next();

            //put the results into a the list of AffiliateData objects
            int index = 0;
            int pageSize = sort != null ? sort.getPageSize() : 0;
            int startIndex = sort == null ? 0 : sort.getPage() * pageSize;
            while (
                index + startIndex < count &&
                (sort == null || index < pageSize))
            {
                AffiliateData data = new AffiliateData();
                data.setPk(new Integer(rs.getInt("aff_pk")));
                data.setLocal(rs.getString("Aff_localSubChapter"));
                data.setCouncil(rs.getString("Aff_councilRetiree_chap"));
                data.setState(rs.getString("Aff_stateNat_type"));
                data.setSubUnit(rs.getString("Aff_subUnit"));
                data.setType(new Character(rs.getString("Aff_type").charAt(0)));
                data.setName(rs.getString("aff_abbreviated_nm"));
                if (searchOnSelection)
                    data.setSelected(criteria.isSelected());
                else
                    data.setSelected(new Boolean(rs.getString("person_pk") != null));
                result.add(data);
                rs.next();
                index++;
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return count;
    }

    /**
     * Sets the roles for a user.
     *
     * @param personPk The primary key of the use who's roles are to be set
     * @param roles A set of role primary keys to associate with the user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void setRoles(Integer personPk, Set roles)
    {
        DBUtil.setAssociation(personPk, roles, SQL_DELETE_USER_ROLES, SQL_INSERT_USER_ROLE);
    }

    /**
     *  Adds or removes a set of affilaites to a user, based on a set of search criteria.
     *
     * @param personPk The primary key of the user to add/remove affiliates to
     * @param criteira The criteria to use for the affiliate search
     * @param val If true, adds the affiliates, otherwise removes the affiliates
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void setAffiliates(Integer personPk, AffiliateData criteria, boolean val)
    {
        //search for the affiliates
        List affiliates = new LinkedList();
        criteria = criteria.copy();
        criteria.setSelected(new Boolean(!val));
        int count = getAffiliates(personPk, criteria, null, affiliates);
        if (count == 0)
            return;

        //make a set of the affiliate primary keys
        Iterator it = affiliates.iterator();
        Set affiliateKeys = new HashSet();
        while (it.hasNext())
            affiliateKeys.add(((AffiliateData)it.next()).getPk());

        //add or remove the affilaites
        if (val)
            addAffiliates(personPk, affiliateKeys);
        else
            removeAffiliates(personPk, affiliateKeys);
    }

    /**
     * Adds a set of affiliates to a user
     *
     * @param personPk The user to add affiliates to
     * @param affiliates The set of affiliates to add to the user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void addAffiliates(Integer personPk, Set affiliates)
    {
        DBUtil.addAssociation(personPk, affiliates, SQL_SELECT_USER_AFFILIATES, "aff_pk", SQL_INSERT_USER_AFFILIATE);
    }

    /**
     * Removes a set of affiliates from a user.
     *
     * @param personPk The user to remove affiliates from
     * @param affiliates The set of affiliates to remove from the user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void removeAffiliates(Integer personPk, Set affiliates)
    {
        DBUtil.removeAssociation(personPk, affiliates, SQL_DELETE_USER_AFFILIATES);
    }

    /**
     * Gets a UserData object by it's user id.
     *
     * @param userId The id of the user to retrieve data for
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getUserId(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;

        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_USER_ID);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();

            if (!rs.next())
                throw new EJBException("User with personPk=" + personPk + " not found");

            result = rs.getString(1);

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }

    /**
     * Gets a PersonPk by it's user id.
     *
     * @param userId The id of the user to retrieve data for
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Integer getPersonPK(String userId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer result = null;

        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_PERSON_PK);
            ps.setString(1, userId);
            rs = ps.executeQuery();

            if (!rs.next())
                throw new EJBException("User with personPk=" + userId + " not found");

            result = new Integer(rs.getInt(1));

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }

    //
    // getPersonName TEMPRORY UNTIL MAINTAIN PERSON IS COMPLETE
    //

    /**
     * Gets a full name for a person
     *
     * @param personPk The primary key of the person
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getPersonName(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;

        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_PERSON_NAME);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if (!rs.next())
                throw new EJBException("Could not find person with pk '" + personPk + "' in MaintainUsers.getPersonName()");

            DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");
            buf.append(rs.getString("first_nm"));
            buf.append(rs.getString("middle_nm"));
            buf.append(rs.getString("last_nm"));
            result = buf.toString();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }

    /**
     * Gets the contact phone for a user.  This is the affiliate's contact phone if available, or an AFSCME number otherwise.
     *
     * @param personPk The primary key of the user to get the affiliate phone number for
     * @return The phone number of the affiliate, or null if not available.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getContactPhone(Integer personPk)
    {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String result = null;

        try {
            con = DBUtil.getConnection();

            stmt = con.prepareStatement(SQL_SELECT_ORG_PHONE);
            stmt.setInt(1, personPk.intValue());
            stmt.setInt(2, personPk.intValue());
            stmt.setInt(3, personPk.intValue());
            rs = stmt.executeQuery();

            if (rs.next()) {
                result = new DelimitedStringBuffer(" ")
                .append(rs.getString("country_cd"))
                .append(rs.getString("area_code"))
                .append(rs.getString("phone_no"))
                .toString();
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }

        return result;
    }

    /**
     * Returns true iff the given userId is already being used by another user
     *
     * @param personPk The primary key of a user to exclude from the check
     * @param userId An id to check for uniqueness
     * @return true if no other user is using this id
     */
    protected boolean isUserIdTaken(Integer personPk, String userId) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            con = DBUtil.getConnection();

            stmt = con.prepareStatement(SQL_SELECT_UID_IS_TAKEN);
            stmt.setString(1, userId);
            stmt.setInt(2, personPk.intValue());
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
     * Adds to User_Affiliates a record to each super user so they can access
     * a newly added affiliate.
     *
     * @param affPk  The primary key of the newly added affiliate.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"     
     */
    public void addAffToSuperUsers(Integer affPk) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();

            stmt = con.prepareStatement(SQL_SELECT_SUPER_USERS);            
            rs = stmt.executeQuery();
            
            Integer personPk;
            while (rs.next()) {
                personPk = new Integer(rs.getInt(1));
                stmt = con.prepareStatement(SQL_INSERT_USER_AFFILIATE);
                stmt.setInt(1,personPk.intValue());
                stmt.setInt(2,affPk.intValue());
                stmt.executeUpdate();                
            }          
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
    }    
    
}
