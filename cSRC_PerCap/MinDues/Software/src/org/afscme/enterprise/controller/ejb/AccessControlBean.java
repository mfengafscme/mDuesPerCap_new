package org.afscme.enterprise.controller.ejb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.controller.AccessControlStatus;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.controller.ChallengeQuestionData;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.afscme.enterprise.users.UserData;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliatesHome;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.util.ConfigUtil;
import org.afscme.enterprise.util.CryptoUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.MailUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.ConfigurationData;
import org.afscme.enterprise.codes.Codes.EmailType;
import org.afscme.enterprise.roles.PrivilegeData;
import org.afscme.enterprise.util.web.WebUtil;


/**
 * Implements business logic for the the access controller mechanism.
 *
 * @ejb:bean name="AccessControl" display-name="AccessControl"
 *              jndi-name="AccessControl"
 *              type="Stateless" view-type="local"
*/
public class AccessControlBean extends SessionBase
{
	/** Updates a user's bad login attempt count, and bad login attempt date */
	private static String SQL_UPDATE_BAD_LOGIN_ATTEMPTS =
        "UPDATE Users SET bad_login_attempt_ct=?, lockout_dt=? WHERE person_pk=?";

	/** Get's a user's challenge question and response */
	private static String SQL_SELECT_CHALLENGE_QUESTION_DATA =
        "SELECT person_pk, challenge_question, challenge_response FROM Users WHERE user_id=?";

	/** Get's a user's challenge response string, given their pk */
	private static String SQL_SELECT_CHALLENGE_RESPONSE =
        "SELECT challenge_response FROM Users WHERE person_pk=?";

	/** Gets the user's current password */
	private static String SQL_SELECT_PASSWORD =
        "SELECT pwd from Users WHERE person_pk=?";

	/** Updates the user's password */
	private static String SQL_UPDATE_PASSWORD =
        "UPDATE Users SET pwd=?, pwd_expiration=? WHERE person_pk=?";

	/** Updates the user's password and challenge question/response) */
    private static final String SQL_UPDATE_CHALLENGE =
        "UPDATE Users SET " +
        " challenge_response=?, challenge_question=?" +
        " WHERE person_pk = ?";

	/** Gets the primary email address for a user */
	private static final String SQL_SELECT_USER_EMAIL =
		"SELECT person_email_addr, prefix_nm, first_nm, middle_nm, last_nm, suffix_nm " +
		" FROM Person p " +
		" INNER JOIN Person_Email e ON p.person_pk = e.person_pk " +
		" INNER JOIN Users u ON p.person_pk = u.person_pk " +
		" WHERE u.person_pk=? AND e.email_type=" + EmailType.PRIMARY;

    /** Gets the 'member edit' and 'member view' flags for a user (determined at the affiliate level) */
    private static final String SQL_SELECT_MEMBER_PRIVILEGES =
        "SELECT o.mbr_allow_edit_fg, o.mbr_allow_view_fg " +
        " FROM Users u " +
        " INNER JOIN Person p ON u.person_pk = p.person_pk " +
        " INNER JOIN Aff_Members m ON m.person_pk = p.person_pk " +
        " INNER JOIN Aff_Organizations o ON o.aff_pk = m.aff_pk " +
        " WHERE u.person_pk=? ";

	/** Sets a user's last login date */
	private static String SQL_UPDATE_LAST_LOGIN_DATE =
        "UPDATE Users SET last_session=? WHERE person_pk=?";

    /** Gets the name of an affiliate (This may be moved to a membership componenent when that is ready) */
    private static String  SQL_SELECT_AFFILIATE_NAME =
        "SELECT aff_abbreviated_nm from Aff_Organizations WHERE aff_pk=?";

    /** Select info needed to make a user's password */
    private static final String SQL_SELECT_PASSWORD_INPUT =
        "SELECT " +
        " last_nm, ssn, Aff_localSubChapter " +
        " FROM Person p" +
        " LEFT OUTER JOIN Aff_Members m ON p.person_pk = m.person_pk " +
        " LEFT OUTER JOIN Aff_Organizations o ON o.aff_pk = m.aff_pk " +
        " WHERE p.person_pk=? ";

    /** Select the current dues year */
    private static final String SQL_SELECT_CURRENT_DUESYEAR =
        "SELECT CODE FROM mdu_lookup where description like 'Current Dues Year%'";

    /** Reference to the MaintainUsers EJB */
	private MaintainUsers m_maintainUsers;

    /** Reference to the MaintainPrivileges EJB */
	private MaintainPrivileges m_maintainPrivileges;

    /** Reference to the MaintainAffiliates EJB */
	private MaintainAffiliates m_maintainAffiliates;

    /** Gets references to the dependent EJBs */
    public void ejbCreate() throws CreateException {
		try {
			m_maintainUsers = JNDIUtil.getMaintainUsersHome().create();
			m_maintainPrivileges = JNDIUtil.getMaintainPrivilegesHome().create();
			m_maintainAffiliates  = JNDIUtil.getMaintainAffiliatesHome().create();
		} catch (NamingException e) {
			throw new EJBException("Unable to find dependent EJBs in AccessControlBean.ejbCreate()" + e);
		}
    }

    /** Removes references to the dependent EJBs */
    public void ejbRemove() {
		try {
			if (m_maintainUsers != null)
				m_maintainUsers.remove();
			if (m_maintainPrivileges != null)
				m_maintainPrivileges.remove();
			if (m_maintainAffiliates != null)
				m_maintainAffiliates.remove();
		} catch (RemoveException e) {
				throw new EJBException("Unable to remove dependent EJBs in AccessControlBean.ejbRemove()" + e);
		}
	}

    /**
     * Attempts to log the user on tho the system.
     *
     * @param userId user id provided by the user
     * @param password provided by the user
     * @param securityData out parameter.  Filled in by this method.
     * @return one of the LOGIN_RESULT values from the AccessCotnrolStatus interface.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
	 */
    public int login(String userId, String password, UserSecurityData securityData)
    {
		UserData userData = m_maintainUsers.getUser(userId);

		//Check if 'userId' was valid
		if (userData == null)
			return AccessControlStatus.LOGIN_RESULT_FAILED;
/*
		//Check if they're locked out
		if (userData.getLockoutDate() != null) {
			//check if they've comlpeted their lockout interval
			ConfigurationData config = ConfigUtil.getConfigurationData();
			long lockoutMS = config.getLockoutTime() * 60L * 1000L;			//amount of time user gets locked out
			long badLoginAttemptMS = userData.getLockoutDate().getTime();  //time at which user was locked out
			long currentMS = System.currentTimeMillis();				//current time

			if (currentMS - badLoginAttemptMS > lockoutMS)
				resetLockout(userData); //lockout interval is completed
			else
				return AccessControlStatus.LOGIN_RESULT_LOCKED_OUT; //lockout interval is not completed
		}
*/
		//Check if 'password' was valid
        boolean passwordValid;
        try {
            passwordValid = checkPassword(password, userData.getPersonPk());
        } catch (SQLException e) {
			throw new EJBException(e);
		}
        if (!passwordValid) {
            //addBadLoginAttempt(userData);
            return AccessControlStatus.LOGIN_RESULT_FAILED;
        } else if (userData.getBadLoginAttemptCount() != 0)
            resetLockout(userData);

        //Login is OK.  Get the user security data
        getUserSecurityData(userData, securityData);

        //update the user's 'last session' date
		updateLastLoginDate(userData);

		return AccessControlStatus.LOGIN_RESULT_OK;
    }

    /**
     * Resets the user's bad login attempt count to 0.
     *
     * @param in/out, specifies the user to reset lockout for, and gets it's bad login attempt count of 0 and lockout date set to null.
     * @return The provided user data object, with the lockout reset.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public UserData resetLockout(UserData userData)
    {
		userData.setBadLoginAttemptCount(0);
		userData.setLockoutDate(null);
		updateBadLoginAttempts(userData);
		return userData;
	}

    /**
     * Gets the user's pk and the pk of the user's challenge question.
	 *
	 * @param userId The id of the user.
	 * @return null if the user could not be found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ChallengeQuestionData getChallengeQuestionData(String userId)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
		ChallengeQuestionData result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CHALLENGE_QUESTION_DATA);
            ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = new ChallengeQuestionData();
				result.setPersonPk(new Integer(rs.getInt(1)));
                int question = rs.getInt(2);
                if (question != 0)
    				result.setQuestion(new Integer(question));
				result.setResponse(rs.getString(3));
			}

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

		return result;
    }


    /**
     * Gets the name of an affiliate (used to display the affiliate name on the top of the data utility screen)
	 *
     * @TODO: This may be moved to a Membership component when that is complete.
     *
	 * @param affiliatePk primary key of the affiliate to get the name for
	 * @return The name of the affiliate
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public String getAffiliateName(Integer affiliatePk)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        String result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_NAME);
            ps.setInt(1, affiliatePk.intValue());
			rs = ps.executeQuery();
			if (!rs.next())
                throw new EJBException("Unable to find affiliate with primary key : " + affiliatePk);
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
     * Sends an email to the user with their password.
     *
     * @param userPK primary key of the user to request passwor for
     * @param challengeResponse The user's response to the challenge question.
     * @return One of the REQUEST_PASSWORD values from the AccessControlStatus interface.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int requestPassword(Integer userPK, String challengeResponse)
    {
		String realChallengeResponse = getChallengeResponse(userPK);
		if (challengeResponse.equalsIgnoreCase(realChallengeResponse)) {
			EmailData emailData = getUserEmail(userPK);
			if (emailData == null || !TextUtil.isEmail(emailData.emailAddress)) {
				return AccessControlStatus.REQUEST_PASSWORD_NO_EMAIL;
			} else {
				String newPassword = resetPassword(userPK);
				try {
					String body = TextUtil.format("email.body.passwordReset", new Object[] {newPassword});
					String subject = TextUtil.format("email.subject.passwordReset", null);
					ConfigurationData config = ConfigUtil.getConfigurationData();
					MailUtil.sendMail(
						emailData.emailAddress,
						emailData.getFullName(),
						config.getRequestPasswordFromEmail(),
						config.getRequestPasswordFromName(),
						subject,
						body);
				} catch (IOException e) {
					throw new EJBException("Couldn't send new password email", e);
				} catch (MessagingException e) {
					throw new EJBException("Couldn't send new password email", e);
				}
			}
			return AccessControlStatus.REQUEST_PASSWORD_OK;
		} else {
			return AccessControlStatus.REQUEST_PASSWORD_BAD_RESPONSE;
		}
	}


    /**
     * Updates user's password.
	 * Checks that the old password provided matches the current password in the database.
	 *
     * @param userPK primary key of the user to change password for
     * @param oldPassword The old password provided by the user
     * @param newPassword The new password the user wishes to have.
	 * @return One of the CHANGE_PASSWORD_XXX constants in the AccessControlStatus interface.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int changePassword(Integer userPK, String oldPassword, String newPassword)  {

		String oldDBPassword = getHashedPassword(userPK);
		if (!CryptoUtil.hash(oldPassword).equals(oldDBPassword))
			return AccessControlStatus.CHANGE_PASSWORD_BAD_OLD_PASSWORD;
		else
			return changePassword(userPK, newPassword);
	}

	/**
     * Updates user's password.
	 * Similar to {@link AccessControlBean#changePassword(Integer, String, String)}, but does not check an old password first.
     *
     * @param userPK primary key of the user to change password for
     * @param newPassword The new password the user wishes to have.
	 * @return One of the CHANGE_PASSWORD_XXX constants in the AccessControlStatus interface.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int changePassword(Integer userPK, String newPassword)
    {
        Connection con = null;
        PreparedStatement ps = null;

		//update the new info
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PASSWORD);
            ps.setString(1, CryptoUtil.hash(newPassword));
			Calendar pwdExpiration = new GregorianCalendar();
			pwdExpiration.roll(Calendar.YEAR, 1);
			Timestamp oneYearFromNow = new Timestamp(pwdExpiration.getTimeInMillis());
			ps.setTimestamp(2, oneYearFromNow);
			ps.setInt(3, userPK.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

		return AccessControlStatus.CHANGE_PASSWORD_OK;
	}

    /**
     * Updates user's challenge question/response.
	 *
     * @param userPK primary key of the user to update challenge for
     * @param challengeQuestion primary key of the challenge question (from common codes)
     * @param challengeResponse user's resonse to the challenge question
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updateChallenge(Integer userPK, Integer challengeQuestion, String challengeResponse)
    {
        Connection con = null;
        PreparedStatement ps = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_CHALLENGE);
            ps.setString(1, challengeResponse);
            ps.setInt(2, challengeQuestion.intValue());
			ps.setInt(3, userPK.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
	}


    /**
     * Gets all the affiliates an affiliate with the given affPk has access to (all sub affiliates, including the given affPk)
     *
     * @param affPk Affiliate pk to get accessible affiliates for
     * @return Set of affiliat pk Integer objects
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getAccessibleAffiliates(Integer affPk)
    {
        Set result = new TreeSet();
        result.add(affPk);
        List subAffiliates = m_maintainAffiliates.getSubAffiliates(affPk);
        if (subAffiliates != null) {
            Iterator it = subAffiliates.iterator();
            while (it.hasNext()) {
                AffiliateData ad = (AffiliateData)it.next();
                result.addAll(getAccessibleAffiliates(ad.getAffPk()));
                // result.add(getAccessibleAffiliates((Integer)it.next()));
            }
        }

        return result;
    }

	/**
	 * Adds a bad login attempt to the given userData, and updates the database.  Locks the account
	 * out if the maximum number of attempts is exceeded.
	 *
     * @param userData in/out Specifies the user to update.  This stucture is updated and returned.
	 * @return the updated UserData object
	 */
	protected UserData addBadLoginAttempt(UserData userData) {
		userData.setBadLoginAttemptCount(userData.getBadLoginAttemptCount() + 1);
		int maxAttempts = ConfigUtil.getConfigurationData().getMaxLoginAttempts();

		if (userData.getBadLoginAttemptCount() == maxAttempts)
			userData.setLockoutDate(new Timestamp(System.currentTimeMillis()));

		updateBadLoginAttempts(userData);
		return userData;
	}

	/**
	 * Stores the updated bad attempt count and lockout date to the db.
     *
     * @param userData contains the bad login attempt count, lockout date, and user pk to use.
	 */
	protected void updateBadLoginAttempts(UserData userData)
    {
        Connection con = null;
        PreparedStatement ps = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_BAD_LOGIN_ATTEMPTS);
            ps.setInt(1, userData.getBadLoginAttemptCount());
            ps.setTimestamp(2, userData.getLockoutDate());
			ps.setInt(3, userData.getPersonPk().intValue());
			ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }


	/**
     * Retrieves all the security data for the user and combines it into 1 structure.
     *
	 * @see org.afscme.enterprise.controller.SecurityData
	 * @param userPK the pk of the user to get security data for
	 * @param securityData the security data object to fill in
	 */
	protected void getUserSecurityData(UserData userData, UserSecurityData securityData) {
		Set privileges = new HashSet();
		Set dataUtilityPrivileges = new HashSet();
		Set reports = new HashSet();
		Set fields = new HashSet();
        boolean forceChangePassword = false;

        //get the user's affiliates count (for the data utility)
	int affiliateCount = m_maintainUsers.getAffiliateCount(userData.getPersonPk());

        //get the user's roles
		Set roles = m_maintainUsers.getRoles(userData.getPersonPk());

        //get all the privileges in the system
        Map allPrivileges = m_maintainPrivileges.getPrivilegesMap();

        //merge together all data from the user's roles
		Iterator it = roles.iterator();
		while (it.hasNext()) {
			Integer rolePK = (Integer)it.next();
            Iterator privilegeIt = m_maintainPrivileges.getPrivileges(rolePK).iterator();
            while (privilegeIt.hasNext()) {
                String key = (String)privilegeIt.next();
                PrivilegeData privilegeData = (PrivilegeData)allPrivileges.get(key);
                if (privilegeData.isDataUtility())
                    dataUtilityPrivileges.add(key);
                else
                    privileges.add(key);
            }
			reports.addAll(m_maintainPrivileges.getReports(rolePK));
			fields.addAll(m_maintainPrivileges.getFields(rolePK));
		}

        //all users get the 'User' privilege
        privileges.add(ActionPrivileges.PRIVILEGE_USER);
        dataUtilityPrivileges.add(ActionPrivileges.PRIVILEGE_USER);

        //get the member privileges from the affiliate
        privileges.addAll(getMemberPrivileges(userData.getPersonPk()));
        dataUtilityPrivileges.addAll(getMemberPrivileges(userData.getPersonPk()));

        if (userData.getDepartment() != null) {
            //this is an AFSCME International user
            privileges.add(ActionPrivileges.PRIVILEGE_AFSCME_INTERNATIONAL_USER);
            dataUtilityPrivileges.add(ActionPrivileges.PRIVILEGE_AFSCME_INTERNATIONAL_USER);
        }

        if (affiliateCount > 0) {
            //this user can user the data utility
            privileges.add(ActionPrivileges.PRIVILEGE_DATA_UTILITY_USER);
            dataUtilityPrivileges.add(ActionPrivileges.PRIVILEGE_DATA_UTILITY_USER);

            if (affiliateCount > 1) {
                dataUtilityPrivileges.add(ActionPrivileges.PRIVILEGE_MULTIPLE_AFFILIATES);
            }
        }

        //determine if the user should be in 'force change password' mode
		if (userData.getPasswordExpirationDate() == null ||
			userData.getPasswordExpirationDate().getTime() < System.currentTimeMillis()) {
				forceChangePassword = false;
		}

        //place the data into the structure
        securityData.setUserId(userData.getUserId());
        securityData.setPersonPk(userData.getPersonPk());
		securityData.setDepartment(userData.getDepartment());
		securityData.setPrivileges(privileges);
		securityData.setDataUtilityPrivileges(dataUtilityPrivileges);
		securityData.setFields(fields);
		securityData.setReports(reports);
        securityData.setForceChangePassword(forceChangePassword);
        securityData.setStartPage(userData.getStartPage());
	}

    /**
     * Retrieves the challenge response string for a user
     *
     * @param userPK primary key of the user to retrieve a challenge response for
     * @return The challenge response string
     */
	protected String getChallengeResponse(Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CHALLENGE_RESPONSE);
            ps.setInt(1, userPK.intValue());
			rs = ps.executeQuery();
			if (!rs.next())
				throw new EJBException("User with id " + userPK + " not found");
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
     * Retrieves the current duesyear
     *
     * @return The Current Dues year as a String
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
	public String getCurrentDuesYear() {
        Connection con = null;
        Statement stmt = null;
		ResultSet rs = null;
		String result = null;

		/*
		try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_CURRENT_DUESYEAR);

			if (!rs.next())
				throw new EJBException("Current Dues Year not found");

			result = rs.getString(1);
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
		*/

 		int curCalYr = Calendar.getInstance().get(Calendar.YEAR) + 1;
		result = "" + curCalYr;

		return result;
	}

	/**
	 * Gets the primary email address for a user.
	 *
     * @param primary key of the user to get the email address for
	 * @return the email address, or null if it does not exist.
	 */
	protected EmailData getUserEmail(Integer userPK) {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
		EmailData result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_USER_EMAIL);
            ps.setInt(1, userPK.intValue());
			rs = ps.executeQuery();
			if (rs.next()) {
				result = new EmailData();
				result.emailAddress = rs.getString("person_email_addr");
				result.prefix = rs.getString("prefix_nm");
				result.firstName = rs.getString("first_nm");
				result.middleName = rs.getString("middle_nm");
				result.lastName = rs.getString("last_nm");
				result.suffix = rs.getString("suffix_nm");
			}
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

		return result;
	}

	/**
	 * Changes the password for a user to a random string.
	 *
     * @param personPk primary key of the user to reset password for
	 * @return the new password
	 */
	protected String resetPassword(Integer personPk) {

		String newPassword  = CryptoUtil.randomPassword(10);

		changePassword(personPk, newPassword);

		return newPassword;
	}

    /**
     * Updates the last login date for the user to now.
     *
     * @param userData in/out - Specifies the user to update.  Updated and returned.
     * @return the UserData object provided.
     */
	protected UserData updateLastLoginDate(UserData userData) {
        Connection con = null;
        PreparedStatement ps = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_LAST_LOGIN_DATE);
			userData.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
			ps.setTimestamp(1, userData.getLastLoginDate());
            ps.setInt(2, userData.getPersonPk().intValue());
			ps.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return userData;
	}

    /**
     * Retrieves the password for a user
     *
     * @param personPk primary key of the user to get the password for
     * @return MD5 hash of the user's password
     */
	protected String getHashedPassword(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PASSWORD);
            ps.setInt(1, personPk.intValue());
			rs = ps.executeQuery();
			if (rs.next())
				result = rs.getString(1);
			else
				throw new EJBException("Attempt to get password for non-existent user with pk: " + personPk);
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

		return result;
	}

    /**
     * Retrieves the member edit/view privileges for a user. (Determined by the affiliate)
     *
     * @return Set of Member privileges keys
     */
    protected Set getMemberPrivileges(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;
        Set result = new HashSet();

		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_PRIVILEGES);
            ps.setInt(1, personPk.intValue());
			rs = ps.executeQuery();
			if (rs.next()) {
                if (rs.getInt("mbr_allow_edit_fg") == 1) {
                    result.add(ActionPrivileges.PRIVILEGE_EDIT_PERSONAL_INFORMATION);
                    result.add(ActionPrivileges.PRIVILEGE_VIEW_PERSONAL_INFORMATION);
                } else if (rs.getInt("mbr_allow_view_fg") == 1) {
                    result.add(ActionPrivileges.PRIVILEGE_VIEW_PERSONAL_INFORMATION);
                }
            } else {
                //they aren't in an affiliate, they automatically get to edit their own data
                result.add(ActionPrivileges.PRIVILEGE_EDIT_PERSONAL_INFORMATION);
                result.add(ActionPrivileges.PRIVILEGE_VIEW_PERSONAL_INFORMATION);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }

    /**
     * Returns true iff the password is valid for the person.  If the person is a member,
     * checks for matches with the 'computed password'
     *
     * @param password The password to check
     * @param personPk The primary key of the person to check the password for
     */
    protected boolean checkPassword(String password, Integer personPk) throws SQLException {

        String hashedPassword = getHashedPassword(personPk);

        if (hashedPassword == null) {

            //possible member first login
            List memberPasswords = getMemberPasswords(personPk);
            Iterator it = memberPasswords.iterator();
            while (it.hasNext()) {
                if (((String)it.next()).equalsIgnoreCase(password)) {
                    return true;
                }
            }

            return false;
        } else {
			return CryptoUtil.hash(password).equalsIgnoreCase(hashedPassword);
        }
    }

    /**
     * Calculates the initial passwords possible for a person
     *
     * @param personPk The primary key of the person to calculate a new password for
     * @return A list of password strings
     */
    protected static List getMemberPasswords(Integer personPk) throws SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PASSWORD_INPUT);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                String affCode = rs.getString("Aff_localSubChapter");
                String ssn = rs.getString("ssn");
                String lastName = rs.getString("last_nm");
                String firstPart = TextUtil.padTrailing(lastName, 4, '0').substring(0, 4);
                String secondPart;
                if (TextUtil.isEmpty(ssn)) {
                    if (affCode == null)
                        continue;
                    secondPart = TextUtil.padLeading(affCode, 4, '0');
                }
                else
                    secondPart = ssn.substring(ssn.length() - 4);
                result.add(firstPart + secondPart);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return result;
    }


	/** Structure used by getEmailData() to return information needed to email a user */
	protected class EmailData {

		public String prefix, firstName, middleName, lastName, suffix;
		public String emailAddress;

		public String getFullName() {
			return new DelimitedStringBuffer(" ")
			.append(prefix)
			.append(firstName)
			.append(middleName)
			.append(lastName)
			.append(suffix)
			.toString();
		}
	}
}
