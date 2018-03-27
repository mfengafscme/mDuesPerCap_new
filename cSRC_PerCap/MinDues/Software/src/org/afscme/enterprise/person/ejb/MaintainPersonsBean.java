package org.afscme.enterprise.person.ejb;

import java.lang.Integer;
// AFSCME Enterprise Imports
import org.afscme.enterprise.person.*;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.users.UserData;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;

// Java Imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Iterator;
import javax.naming.NamingException;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.codes.Codes;
import org.apache.log4j.Logger;


/**
 * Encapsulates all access to Persons in the system
 * @ejb:bean name="MaintainPersons" display-name="MaintainPersons"
 * jndi-name="MaintainPersons"
 * type="Stateless" view-type="local"
 */
public class MaintainPersonsBean extends SessionBase
{
    private static Logger logger = Logger.getLogger(MaintainPersonsBean.class);

    /**
     * variables to hold reference to ejb
     */
    protected SystemAddress m_addressBean;
    protected MaintainUsers m_maintainUsers;
    protected MaintainAffiliates m_affiliates;

        /** Common Code PK for Email Type = "PRIMARY" */
    public static final Integer EMAIL_TYPE_PRIMARY = new Integer(71001);
        /** Common Code PK for Email Type = "ALTERNATE" */
    public static final Integer EMAIL_TYPE_ALTERNATE = new Integer(71002);
        /** Common Code PK for Start Page = "View Data Utility" */
//    public static final Integer START_PAGE_AFFILIATE = new Integer(7002);
      public static final String START_PAGE_AFFILIATE = "D";
        /** Common Code PK for Start Page = "VENDOR" */
//    public static final Integer START_PAGE_VENDOR = new Integer(7004);
      public static final String START_PAGE_VENDOR = "V";

    /** Columns to sort on, corresponding to the field ids in PersonCriteria.
        It is important that these are in the same order  */
    private final String[] SORT_FIELD_COLUMNS = new String[] {
        "person_nm", "personAddr", "city", "state", "personAddrPostalCode", "ssn"
    };

    /** Columns to sort on, corresponding to the field ids in PersonCriteria.
        It is important that these are in the same order  */
    private final String[] SORT_FIELD_SEARCH_COLUMNS = new String[] {
        "person_nm", "personAddr", "personAddrCity", "personAddrState", "personAddrPostalCode", "ssn", "u.user_id", "a.aff_type", "int_local", "a.aff_stateNat_type", "a.aff_SubUnit", "int_council"
    };

    /** Gets the fields for a person */
    private static String SQL_SELECT_PERSON =
        "SELECT p.person_pk, prefix_nm, first_nm, middle_nm, last_nm, suffix_nm,  " +
        "       nick_nm, alternate_mailing_nm, ssn, valid_ssn_fg, duplicate_ssn_fg,  " +
        "       marked_for_deletion_fg, comment_txt, comment_dt, " +
        "       c.created_user_pk, address_pk  " +
        "  FROM Person p " +
        "  LEFT OUTER JOIN Person_Comments c ON p.person_pk = c.person_pk " +
        "              AND c.comment_dt = (SELECT MAX(comment_dt)  " +
                    "                        FROM Person_Comments " +
        "                                   WHERE person_pk = p.person_pk) " +
        "  LEFT OUTER JOIN Person_SMA s ON p.person_pk = s.person_pk " +
        "              AND s.current_fg = 1  " +
        " WHERE p.person_pk = ? " ;

    private static final String SQL_SELECT_PERSON_ON_SSN_FNAME_LNAME =
        "SELECT person_pk " +
        "  FROM Person " +
        " WHERE ssn = ? " +
        " AND first_nm = ? " +
        " AND last_nm = ? ";

    /** Updates the fields for a person */
    private static String SQL_UPDATE_PERSON =
        "UPDATE Person " +
        "   SET prefix_nm=?, first_nm=?, middle_nm=?, last_nm=?, suffix_nm=?,  " +
        "       nick_nm=?, alternate_mailing_nm=?, ssn=?, valid_ssn_fg=?, duplicate_ssn_fg=?,  " +
        "       marked_for_deletion_fg=?, lst_mod_user_pk=?, lst_mod_dt=GetDate() " +
        " WHERE person_pk = ? ";

    /** Inserts the fields for a person */
    private static String SQL_INSERT_PERSON =
        "INSERT Person " +
        "       (prefix_nm, first_nm, middle_nm, last_nm, suffix_nm, nick_nm, " +
        "       alternate_mailing_nm, ssn, valid_ssn_fg, duplicate_ssn_fg, " +
        "       member_fg, mbr_barred_fg, marked_for_deletion_fg," +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, GetDate(), ?, GetDate()) " ;

    /** Gets the person primary key for a person */
    private static String SQL_SELECT_PERSONPK =
        "SELECT person_pk " +
        "  FROM Person  " +
        " WHERE prefix_nm = ? " +
        "   AND first_nm = ? " +
        "   AND last_nm = ? " +
        "   AND middle_nm = ? " +
        "   AND suffix_nm = ? " +
        "   AND nick_nm = ? " +
        "   AND alternate_mailing_nm = ? " +
        "   AND ssn = ? " +
        "   AND valid_ssn_fg = ? " +
        "   AND created_user_pk = ? " +
        "   AND created_dt = lst_mod_dt " +
        "   AND lst_mod_user_pk = ? " ;

   /** Gets all the Email Address fields for a person */
    private static String SQL_SELECT_EMAIL =
        "SELECT person_pk, email_type, person_email_addr, email_bad_fg, "+
                "email_marked_bad_dt, Person_Email.created_dt, Person_Email.created_user_pk, Person_Email.lst_mod_dt, "+
                "Person_Email.lst_mod_user_pk, email_pk, "+
                "CASE WHEN com_cd_sort_key = 1 THEN 1 ELSE 0 END 'isPrimary' "+
        "  FROM Person_Email " +
        "  JOIN common_codes ON email_type = com_cd_pk " +
        " WHERE person_pk = ? " +
        "ORDER BY com_cd_sort_key";

    /** Inserts the Email fields for a person */
    private static String SQL_INSERT_EMAIL =
        "INSERT INTO Person_Email " +
        "       (person_pk, person_email_addr, email_type, email_bad_fg, email_marked_bad_dt, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        " VALUES(?, ?, ?, 0, NULL, ?, GetDate(), ?, GetDate()) ";

    /** Clears the Email fields for a person */
    private static String SQL_DELETE_EMAIL =
        "UPDATE Person_Email " +
        "   SET person_email_addr=?, email_bad_fg=?, " +
        "       email_marked_bad_dt=?, lst_mod_dt=GetDate(), lst_mod_user_pk=? " +
        " WHERE email_pk=?";

    /** Updates the Email Type for a person */
    private static String SQL_UPDATE_EMAIL_TYPE =
          "UPDATE person_email " +
          "   SET email_type = ?, lst_mod_dt = GetDate(), lst_mod_user_pk = ? " +
          " WHERE email_pk = ? ";

    /** Updates the Email fields for a person */
    private static String SQL_UPDATE_EMAIL =
        "UPDATE Person_Email " +
        "   SET person_email_addr=?, email_type=?, " +
        "   lst_mod_dt=GetDate(), lst_mod_user_pk=?" +
        " WHERE person_pk=? " +
        "   AND email_pk=?";

   	/** Updates the Email bad flag fields for a person */
	private static String SQL_UPDATE_EMAIL_BAD_FLAG =
	 	"UPDATE person_email SET email_bad_fg=?, " +
	    "email_marked_bad_dt=? " +
	    "WHERE email_pk=? AND email_bad_fg=?";

    /** Gets the current datetime on the server */
    private static final String SQL_SELECT_SERVER_DATE=
        "SELECT GetDate() ServerDate";

    /** Gets all the Phone fields for a person that belongs to a department */
    private static String SQL_SELECT_PHONE_NUMBERS =
        "SELECT person_pk, area_code, phone_no, lst_mod_dt, country_cd, phone_prmry_fg, phone_bad_fg, phone_private_fg, created_user_pk, created_dt, phone_pk, phone_type, phone_do_not_call_fg, phone_marked_bad_dt, lst_mod_user_pk, phone_extension, dept FROM Person_Phone " +
        " WHERE person_pk = ?  " +
        "    AND (phone_private_fg <> 1 OR isnull(phone_private_fg, 0) <> 1" +
        "    OR dept = ? ) ";

    /** Gets all the Phone fields for a person */
    private static String SQL_SELECT_PHONE_NUMBER =
        "SELECT person_pk, area_code, phone_no, lst_mod_dt, country_cd, phone_prmry_fg, phone_bad_fg, phone_private_fg, created_user_pk, created_dt, phone_pk, phone_type, phone_do_not_call_fg, phone_marked_bad_dt, lst_mod_user_pk, phone_extension, dept FROM Person_Phone " +
        " WHERE phone_pk = ? ";

    /** Inserts the Phone fields for a person */
    private static String SQL_INSERT_PHONE =
        "INSERT INTO Person_Phone " +
        "       (person_pk, area_code, phone_no, country_cd, phone_prmry_fg, " +
        "       phone_private_fg, phone_type, phone_do_not_call_fg, phone_extension, " +
        "       dept, phone_bad_fg, phone_marked_bad_dt, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GetDate(), ?, GetDate())";

    /** Updates the Phone fields for a person */
    private static String SQL_UPDATE_PHONE =
        "UPDATE Person_Phone " +
        "   SET country_cd=?, area_code=?, phone_no=?, phone_extension=?, dept=?, " +
        "       phone_type=?, phone_prmry_fg=?, phone_private_fg=?, " +
        "       phone_do_not_call_fg=?, " +
        "       lst_mod_user_pk=?, lst_mod_dt=GetDate() " +
        "WHERE person_pk=? AND phone_pk=?";

    /** Updates the Phone bad flag fields for a person */
	private static String SQL_UPDATE_PHONE_BAD_FLAG =
	 	"UPDATE person_phone SET phone_bad_fg=?, " +
	    "phone_marked_bad_dt=? " +
	    "WHERE phone_pk=? AND phone_bad_fg=?";

    /** Updates Primary phone numbers in the user's department that are not the phonePk to not-Primary  */
    private static String SQL_UPDATE_PHONE_PRIMARY =
        "UPDATE Person_Phone " +
        "   SET phone_prmry_fg = 0, lst_mod_user_pk = ?, lst_mod_dt = GetDate() " +
        " WHERE person_pk = ? " +
        "   AND phone_pk <> ? " +
        "   AND dept = ? " +
        "   AND phone_prmry_fg = 1 ";

    /** Gets DO NOT CALL flag locking information  */
    private static String SQL_SELECT_PHONE_LOCKS =
        "SELECT CASE WHEN political_objector_fg = 1 THEN 1 ELSE 0 END 'political_objector', " +
        "       CASE WHEN political_do_not_call_fg = 1 THEN 1 ELSE 0 END 'do_not_call' " +
        "  FROM Person_Political_Legislative " +
        " WHERE person_pk = ? ";

    /** Deletes a Phone Number for a person */
    private static String SQL_DELETE_PHONE =
        "DELETE FROM person_phone  " +
        " WHERE phone_pk = ?";

    /** Gets the phone primary key for a person */
    private static String SQL_UPDATE_PHONE_PRIMARY_FLAG =
        "UPDATE Person_Phone " +
        "SET phone_prmry_fg = 1 " +
        "WHERE created_dt = (SELECT MAX(created_dt) " +
        "		      FROM Person_Phone " +
        "		     WHERE person_pk = ? ) ";

    /** Gets all the comment fields for a person */
    private static String SQL_SELECT_COMMENT_HISTORY =
        "SELECT comment_txt, comment_dt, created_user_pk, person_pk " +
        "  FROM Person_Comments  " +
        " WHERE person_pk = ? " +
        "ORDER BY comment_dt DESC ";

    /** Inserts a new comment for a person */
    private static String SQL_INSERT_COMMENT =
        "INSERT INTO Person_Comments(person_pk, comment_txt, comment_dt,  " +
        "        created_user_pk) " +
        "  VALUES(?, ?, GetDate(), ? ) ";

    /** Inserts a demographics stub for a person */
    private static String SQL_INSERT_DEMOGRAPHICS_STUB =
        "INSERT INTO Person_Demographics  " +
        "        (person_pk, lst_mod_user_pk, lst_mod_dt, created_dt, created_user_pk) " +
        "  VALUES(?, ?, GetDate(), GetDate(), ?) ";

    /** Inserts a political legislative stub for a person */
    private static String SQL_INSERT_POLITICAL_LEGISLATIVE_STUB =
        "INSERT INTO Person_Political_Legislative  " +
        "        (person_pk) " +
        "  VALUES(?) ";

    /** Checks for existing duplicate Person */
    private static String SQL_SELECT_COUNT_PERSON =
/*          "SELECT CASE " +
          "         WHEN Count(p.person_pk) > 1 THEN 1 " +
          "         ELSE 0 " +
          "          END Duplicate " +
*/
          "SELECT COUNT(p.person_pk) Duplicate " +
          "  FROM Person p " +
          "  LEFT OUTER JOIN Person_Demographics d ON p.person_pk = d.person_pk ";

    /** Gets the fields for duplicate Persons */
    private static String SQL_SELECT_DUPLICATE_PERSON =
        "SELECT p.person_pk, " +
	"(COALESCE(p.last_nm, '') +  " +
	"COALESCE(' '+(SELECT cc.com_cd_desc  " +
        "		    FROM Common_Codes cc  " +
        "		   WHERE p.suffix_nm = cc.com_cd_pk), '') +   " +
	"COALESCE(', ' +p.first_nm, ', ') +   " +
	"COALESCE(' ' +p.middle_nm,' ')) as person_nm,  " +
	"Substring(ssn, 1, 3)+'-'+Substring(ssn, 4, 2)+'-'+Substring(ssn, 6, 4) ssn, " +
	"(COALESCE(a.addr1, '') + COALESCE(', '+a.addr2, '')) AS personAddr, " +
	"(COALESCE(a.city, null)) AS personAddrCity, " +
	"(COALESCE(a.state, null)) AS personAddrState, " +
	"(COALESCE(a.zipcode, '') + COALESCE('-'+a.zip_plus, '')) AS personAddrPostalCode " +
	"FROM person p " +
	"LEFT OUTER JOIN person_address a ON a.person_pk = p.person_pk " +
        "AND address_pk IN (SELECT address_pk " +
        "                       FROM person_SMA" +
	"                      WHERE person_pk = p.person_pk" +
	"                        AND current_fg = 1) " +
        "FULL JOIN Person_Demographics d ON p.person_pk = d.person_pk ";

/*
          "SELECT p.person_pk, first_nm, middle_nm, last_nm, suffix_nm, ssn, " +
          "     addr1, addr2, city, state, zipcode, zip_plus, province, country " +
          "  FROM Person p " +
          "  FULL JOIN Person_Demographics d ON p.person_pk = d.person_pk " +
          "  LEFT OUTER JOIN Person_SMA s ON p.person_pk = s.person_pk " +
          "  LEFT OUTER JOIN Person_Address a ON p.person_pk = a.person_pk " +
          "                                   AND s.address_pk = a.address_pk " ;
*/
    /** Checks for existing duplicate SSN */
    private static String SQL_SELECT_COUNT_SSN =
          "SELECT CASE " +
          "         WHEN Count(person_pk) > 0 THEN 1 " +
          "         ELSE 0 " +
          "          END 'Duplicate' " +
          "  FROM Person " +
          " WHERE ssn = ? ";

	private static String SQL_SELECT_COUNT_SSN_GREATER_THAN_1 =
          "SELECT CASE " +
          "         WHEN Count(person_pk) > 1 THEN 1 " +
          "         ELSE 0 " +
          "          END 'Duplicate' " +
          "  FROM Person " +
          " WHERE ssn = ? ";


    /** Gets a person's SSN */
    private static String SQL_SELECT_SSN =
          "SELECT ssn " +
          "  FROM Person " +
          " WHERE person_pk = ? ";

    /** Gets the fields for a duplicate SSN */
    private static String SQL_SELECT_DUPLICATE_SSN =
        "SELECT p.person_pk, first_nm, middle_nm, last_nm, ssn,  " +
	"       user_id,  " +
	"       m.aff_pk, aff_type, aff_localSubChapter, aff_code, aff_stateNat_type,  " +
	"       aff_subUnit, aff_councilRetiree_chap, " +
	"       a.address_pk, current_fg, addr1, addr2, city, state, zipcode, zip_plus,   " +
	"       province, country " +
        "  FROM Person p " +
        "  LEFT OUTER JOIN Users u ON p.person_pk = u.person_pk " +
        "  LEFT OUTER JOIN Aff_Members m ON p.person_pk = m.person_pk " +
        "  LEFT OUTER JOIN Aff_Organizations o ON m.aff_pk = o.aff_pk " +
        "  LEFT OUTER JOIN Person_SMA s ON p.person_pk = s.person_pk " +
        "  LEFT OUTER JOIN Person_Address a ON p.person_pk = a.person_pk " +
        "                                   AND s.address_pk = a.address_pk " +
        " WHERE ssn = ? " +
        "   AND current_fg = 1 " +
        "UNION ALL " +
        "SELECT p.person_pk, first_nm, middle_nm, last_nm, ssn,  " +
        "  	user_id,  " +
        "	m.aff_pk, aff_type, aff_localSubChapter, aff_code, aff_stateNat_type,  " +
        "	aff_subUnit, aff_councilRetiree_chap, " +
        "	NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL " +
        "  FROM Person p " +
        "  LEFT OUTER JOIN Users u ON p.person_pk = u.person_pk " +
        "  LEFT OUTER JOIN Aff_Members m ON p.person_pk = m.person_pk " +
        "  LEFT OUTER JOIN Aff_Organizations o ON m.aff_pk = o.aff_pk " +
        "  LEFT OUTER JOIN Person_SMA s ON p.person_pk = s.person_pk " +
        " WHERE ssn = ? " +
        "   AND (EXISTS (SELECT person_pk " +
        "		  FROM Person_SMA " +
        "		 WHERE person_pk = p.person_pk " +
        "		GROUP BY person_pk " +
        "		HAVING SUM(current_fg) = 0) " +
        "     OR current_fg IS NULL) " ;

    /** Updates Duplicate SSN flag for all with same SSN */
    private static String SQL_UPDATE_DUPLICATE_SSN_FG =
        "UPDATE person  " +
        "SET duplicate_ssn_fg = ? " +
        " WHERE ssn = ?";


    /** Checks if a person is an AFSCME Staff */
    private static String SQL_SELECT_COUNT_AFSCME_STAFF =
        "SELECT COUNT(dept) persona " +
        "  FROM Users " +
        " WHERE dept IS NOT NULL " +
        "   AND person_pk = ? ";

    /** Checks if a person is an Affiliate Staff */
    private static String SQL_SELECT_COUNT_AFFILIATE_STAFF =
        "SELECT COUNT(aff_pk) persona " +
        "  FROM Aff_Staff " +
        " WHERE person_pk = ? ";

    /** Checks if a person is a Member */
    private static String SQL_SELECT_COUNT_MEMBER =
        "SELECT COUNT(aff_pk) persona " +
        "  FROM Aff_Members " +
        " WHERE person_pk = ? ";

    /** Checks if a person is an Organization Associate */
    private static String SQL_SELECT_COUNT_ORG_ASSOCIATE =
        "SELECT COUNT(org_pk) persona  " +
        "  FROM Ext_Org_Associates  " +
        " WHERE person_pk =  ? ";

    /** Checks if a person is a Vendor */
    private static String SQL_SELECT_COUNT_VENDOR =
        "SELECT COUNT(*) persona " +
        "  FROM Users u " +
        "  JOIN Common_Codes c ON u.start_page = c.com_cd_cd " +
        " WHERE dept IS NULL  " +
        "   AND start_page = 'V' " +
        "   AND person_pk = ? ";

    /** Returns the count of persons found in the search */
/* ===================== OLD CODE
    private static String SQL_SELECT_COUNT_SEARCH_PERSON =
    "SELECT count (distinct p.person_pk) cnt from Person p " +
    "LEFT OUTER JOIN Aff_Members am ON am.person_pk = p.person_pk " +
    "LEFT OUTER JOIN Aff_Organizations a ON am.aff_pk = a.aff_pk " +
    "LEFT OUTER JOIN Users u ON p.person_pk = u.person_pk  " +
    "LEFT OUTER JOIN Person_Email pe ON p.person_pk = pe.person_pk  " +
    "LEFT OUTER JOIN Person_Phone pp ON p.person_pk = pp.person_pk  " +
    "LEFT OUTER JOIN person_address pa ON pa.person_pk = p.person_pk  " +
    "	AND address_pk IN (SELECT address_pk  " +
    "                       	FROM person_SMA " +
    "                       WHERE person_pk = p.person_pk " +
    "                         AND current_fg = 1) " ;

 */

    private static String SQL_SELECT_COUNT_SEARCH_PERSON =
    "SELECT count (distinct p.person_pk) cnt from Person p ";

    /** Returns Person search result set */
/* ======================== OLD CODE
    private static String SQL_SELECT_SEARCH_PERSON =
        "SELECT DISTINCT p.person_pk, p.last_nm, p.first_nm,   " +
        "	(COALESCE(p.last_nm, '') +  " +
        "	COALESCE(' '+(SELECT cc.com_cd_desc  " +
        "		    	FROM Common_Codes cc  " +
        "		       WHERE p.suffix_nm = cc.com_cd_pk), '') +  " +
        "	COALESCE(', ' +p.first_nm, ', ') +   " +
        "	COALESCE(' ' +p.middle_nm,' ')) as person_nm,  " +
        " 	a.aff_pk, " +
        " 	CAST(a.aff_councilRetiree_chap AS INT) int_council,  " +
        "	CAST(a.aff_localSubChapter AS INT) int_local, " +
        "  	a.aff_type, a.aff_stateNat_type, a.aff_SubUnit, " +
        "	(COALESCE(pa.addr1, '') + COALESCE(' '+pa.addr2, '')) AS personAddr, " +
        "	(COALESCE(pa.city, null)) AS personAddrCity, " +
        "	(COALESCE(pa.state, null)) AS personAddrState, " +
        "	(COALESCE(pa.zipcode, '') + COALESCE('-'+pa.zip_plus, '')) AS personAddrPostalCode, " +
        "	Substring(p.ssn, 1, 3)+'-'+Substring(p.ssn, 4, 2)+'-'+Substring(p.ssn, 6, 4) ssn, " +
        "	u.user_id " +
        "  FROM Person p   " +
        "  LEFT OUTER JOIN Aff_Members am ON am.person_pk = p.person_pk  " +
        "  LEFT OUTER JOIN Aff_Organizations a ON am.aff_pk = a.aff_pk  " +
        "  LEFT OUTER JOIN person_address pa ON p.person_pk = pa.person_pk  " +
        "   AND address_pk IN (SELECT address_pk  " +
        "                       	FROM person_SMA " +
        "                       WHERE person_pk = p.person_pk " +
        "                         AND current_fg = 1) " +
        "  LEFT OUTER JOIN Users u ON p.person_pk = u.person_pk " +
        "  LEFT OUTER JOIN Person_Email pe ON p.person_pk = pe.person_pk " +
        "  LEFT OUTER JOIN Person_Phone pp ON p.person_pk = pp.person_pk ";
*/

    private static String SQL_SELECT_SEARCH_PERSON =
        "SELECT DISTINCT p.person_pk, p.last_nm, p.first_nm,   " +
        "	(COALESCE(p.last_nm, '') +  " +
        "	COALESCE(' '+(SELECT cc.com_cd_desc  " +
        "		    	FROM Common_Codes cc  " +
        "		       WHERE p.suffix_nm = cc.com_cd_pk), '') +  " +
        "	COALESCE(', ' +p.first_nm, ', ') +   " +
        "	COALESCE(' ' +p.middle_nm,' ')) as person_nm,  " +
        " 	a.aff_pk, " +
        " 	CAST(a.aff_councilRetiree_chap AS INT) int_council,  " +
        "	CAST(a.aff_localSubChapter AS INT) int_local, " +
        "  	a.aff_type, a.aff_stateNat_type, a.aff_SubUnit, " +
        "	(COALESCE(pa.addr1, '') + COALESCE(' '+pa.addr2, '')) AS personAddr, " +
        "	(COALESCE(pa.city, null)) AS personAddrCity, " +
        "	(COALESCE(pa.state, null)) AS personAddrState, " +
        "	(COALESCE(pa.zipcode, '') + COALESCE('-'+pa.zip_plus, '')) AS personAddrPostalCode, " +
        "	Substring(p.ssn, 1, 3)+'-'+Substring(p.ssn, 4, 2)+'-'+Substring(p.ssn, 6, 4) ssn, " +
        "	u.user_id " +
        "  FROM Person p  ";


	/** Returns Person Political Legislative data */
    private static String SQL_SELECT_PERSON_POLITICAL_LEGISLATIVE =
		"SELECT Person_Political_Legislative.political_party, " +
		"Person_Political_Legislative.political_registered_voter, Person_Political_Legislative.political_objector_fg, " +
		"Person_Political_Legislative.pac_contributor_fg, Person_Political_Legislative.political_do_not_call_fg, " +
		"Person_Political_Legislative.ward_number, Person_Political_Legislative.precinct_number, " +
		"Person_Political_Legislative.precinct_name " +
		"FROM Person_Political_Legislative " +
		"WHERE Person_Political_Legislative.person_pk = ?";

	/** Returns Person Political Legislative data */
    private static String SQL_SELECT_DISTRICTS =
		"SELECT COM_Democracy_Ranged.congdist, " +
		"COM_Democracy_Ranged.upperdist, COM_Democracy_Ranged.lowerdist " +
		"FROM Person_Political_Legislative " +
		"INNER join Person_SMA ON Person_Political_Legislative.person_pk = Person_SMA.person_pk  " +
		"INNER join Person_Address ON Person_SMA.address_pk = Person_Address.address_pk " +
		"INNER join COM_Democracy_Ranged ON Person_Address.zipcode = COM_Democracy_Ranged.zipcode " +
		"WHERE Person_Address.zip_plus >= COM_Democracy_Ranged.start_zip_plus " +
		"AND Person_Address.zip_plus <= COM_Democracy_Ranged.stop_zip_plus " +
		"AND Person_SMA.current_fg = 1 " +
		"AND Person_Political_Legislative.person_pk = ?";

	/** Returns Person Political Legislative data from the COM_AFL_CIO_COPE table*/
   	private static String SQL_SELECT_PERSON_POLITICAL_LEGISLATIVE_COM_AFL_CIO_COPE =
		"SELECT COM_AFL_CIO_COPE.political_party, " +
		"COM_AFL_CIO_COPE.political_registered_voter, Person_Political_Legislative.political_objector_fg, " +
		"Person_Political_Legislative.pac_contributor_fg, Person_Political_Legislative.political_do_not_call_fg, " +
		"COM_AFL_CIO_COPE.ward_number, COM_AFL_CIO_COPE.precinct_number, " +
		"COM_AFL_CIO_COPE.precinct_name, COM_AFL_CIO_COPE.congdist, " +
		"COM_AFL_CIO_COPE.upperdist, COM_AFL_CIO_COPE.lowerdist " +
		"FROM Person_Political_Legislative, COM_AFL_CIO_COPE " +
		"WHERE Person_Political_Legislative.person_pk = ? " +
		"AND Person_Political_Legislative.person_pk = COM_AFL_CIO_COPE.person_pk";

	/** Updates Person Political Legislative */
	private static String SQL_UPDATE_PERSON_POLITICAL_LEGISLATIVE =
		"UPDATE Person_Political_Legislative SET political_objector_fg = ?, " +
		"political_do_not_call_fg = ?, lst_mod_user_pk = ?, lst_mod_dt = getdate() " +
		"WHERE person_pk = ?";

	/** Updates Person Political Legislative Data for Political Party and Registered Voter Data (AUP) */
	private static String SQL_UPDATE_PERSON_POLITICAL_LEGISLATIVE_PARTY_VOTER =
		"UPDATE Person_Political_Legislative " +
                "SET    political_party = ?, political_registered_voter = ?, " +
                "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
		"WHERE  person_pk = ?";

        /** Returns General Demographic data*/
	private static String SQL_SELECT_GENERAL_DEMOGRAPHIC =
		"SELECT Person_Demographics.dob,  Person_Demographics.deceased_dt, " +
		"Person_Demographics.gender, Person_Demographics.ethnic_origin, Person_Demographics.citizenship, " +
		"Person_Demographics.marital_status, Person_Demographics.religion, Person_Demographics.deceased_fg " +
		"FROM Person_Demographics " +
		"WHERE person_pk = ?";

	/** Returns Person Disabilities data*/
	private static String SQL_SELECT_PERSON_DISABILITIES =
		"SELECT Person_Disabilities.disability " +
		"FROM Person_Disabilities " +
		"WHERE person_pk = ?";

	/** Returns Person Accomodation data*/
	private static String SQL_SELECT_PERSON_DISABILITY_ACCOMODATION =
		"SELECT Person_Disability_Accmdtn.disability_accmdtn " +
		"FROM Person_Disability_Accmdtn " +
		"WHERE person_pk = ?";

	/** Returns Person Language data*/
	private static String SQL_SELECT_PERSON_LANGUAGE =
		"SELECT Person_Language.primary_language_fg, Person_Language.language " +
		"FROM Person_Language " +
		"WHERE person_pk = ?";

	/** Returns Person Relation data*/
	private static String SQL_SELECT_PERSON_RELATION =
		"SELECT Person_Relation.relative_first_nm, Person_Relation.relative_middle_nm, " +
		"Person_Relation.relative_last_nm, Person_Relation.relative_suffix_nm, Person_Relation.relative_birth_dt, " +
		"Person_Relation.person_relative_pk " +
		"FROM Person_Relation " +
		"WHERE person_pk = ? " +
		"AND person_relative_type = ?";

	/** Updates Person Demographics data*/
	private static String SQL_UPDATE_PERSON_DEMOGRAPHICS =
		"UPDATE Person_Demographics set dob = ?, deceased_dt = ?, gender = ?, " +
		"ethnic_origin = ?, citizenship = ?, religion = ?, marital_status = ?, " +
		"lst_mod_user_pk = ?, lst_mod_dt = getdate(), deceased_fg = ? " +
		"WHERE person_pk = ? ";

	/** Updates Person Demographics Gender data (AUP) */
	private static String SQL_UPDATE_PERSON_DEMOGRAPHICS_GENDER =
		"UPDATE Person_Demographics " +
                "SET    gender = ?, lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
		"WHERE  person_pk = ? ";

	/** Inserts Person Disabilities data*/
	private static String SQL_INSERT_PERSON_DISABILITIES =
		"INSERT INTO Person_Disabilities (person_pk, disability, comment_txt, created_user_pk, " +
		"created_dt, lst_mod_user_pk, lst_mod_dt) " +
		"VALUES (?,?,?,?,getdate(),?,getdate())";

	/** Deletes Person Disabilities data*/
	private static String SQL_DELETE_PERSON_DISABILITIES =
		"DELETE FROM Person_Disabilities " +
		"WHERE person_pk = ? ";

	/** Inserts Person Accomodations data*/
	private static String SQL_INSERT_PERSON_DISABILITY_ACCOMODATIONS =
		"INSERT INTO Person_Disability_Accmdtn (person_pk, disability_accmdtn, comment_txt, created_user_pk, " +
		"created_dt, lst_mod_user_pk, lst_mod_dt) " +
		"VALUES (?,?,?,?,getdate(),?,getdate())";

	/** Deletes Person Disability Accommodations data*/
	private static String SQL_DELETE_PERSON_DISABILITY_ACCOMODATIONS =
		"DELETE FROM Person_Disability_Accmdtn " +
		"WHERE person_pk = ?";

	/** Inserts Person Language data*/
	private static String SQL_INSERT_PERSON_LANGUAGES =
		"INSERT INTO Person_Language (person_pk, language, primary_language_fg, created_user_pk," +
		"created_dt, lst_mod_user_pk, lst_mod_dt)" +
		"VALUES (?,?,?,?,getdate(),?,getdate())";

	/** Deletes Person Language data*/
	private static String SQL_DELETE_PERSON_LANGUAGES =
		"DELETE FROM Person_Language " +
		"WHERE person_pk = ? ";

	/** Updates Person Child data*/
	private static String SQL_UPDATE_PERSON_CHILD =
		"UPDATE Person_Relation SET person_relative_type = " + Codes.Relation.CHILD + ", relative_first_nm = ?, " +
		"relative_last_nm = ?, relative_middle_nm = ?, relative_suffix_nm = ?, relative_birth_dt = ?, " +
		"lst_mod_user_pk = ?, lst_mod_dt = getdate() " +
		"WHERE person_relative_pk = ?";

	/** Updates Person Partner data*/
	private static String SQL_UPDATE_PERSON_PARTNER =
		"UPDATE Person_Relation SET person_relative_type = " + Codes.Relation.SPOUSE_DOMESTIC_PARTNER +
		", relative_first_nm = ?, " + "relative_last_nm = ?, relative_middle_nm = ?, relative_suffix_nm = ?, " +
		"lst_mod_user_pk = ?, lst_mod_dt = getdate() " +
		"WHERE person_relative_pk = ?";

	/** Inserts Person Relation data*/
	private static String SQL_INSERT_PERSON_RELATION =
		"INSERT INTO Person_Relation (person_pk, person_relative_type, relative_first_nm, " +
		"relative_last_nm, relative_middle_nm, relative_suffix_nm, relative_birth_dt, created_user_pk, created_dt, " +
		"lst_mod_user_pk, lst_mod_dt) VALUES (?,?,?,?,?,?,?,?,getdate(),?,getdate())";

	/** Deletes Person Relation data*/
	private static String SQL_DELETE_PERSON_RELATION =
		"DELETE FROM Person_Relation WHERE person_relative_pk = ?";

	/** Selects gender from Cope table*/
	private static String SQL_SELECT_GENDER_COPE =
		"SELECT gender FROM COM_AFL_CIO_COPE WHERE person_pk = ?";

	/** Selects ethnic origin from Cope table*/
	private static String SQL_SELECT_ETHNIC_ORIGIN_COPE =
		"SELECT ethnic_origin FROM COM_AFL_CIO_COPE WHERE person_pk = ?";

    public void ejbCreate() throws CreateException {
         try {
            // need to change following when JNDIUtil.getSystemAddressHome() is available
            m_addressBean = JNDIUtil.getSystemAddressHome().create();
            m_affiliates = JNDIUtil.getMaintainAffiliatesHome().create();
            m_maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainPersonsBean.ejbCreate()" + e);
        }
    }


    /**
    /**
     * @J2EE_METHOD  --  addDemographicRelations
     * Allows data about a child to be added to the general demographics data for a
     * person.
     *
     * @param personPK Person Primary Key
     * @param childData Child Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void addDemographicRelations    (Integer personPK, RelationData childData)
    {

    }

    /**
     * @J2EE_METHOD  --  addPerson
     * This method allows a person to be added to the database. The action must dictate the
     *  flow to verify that a person doesn't already exist.
     *
     * @param userPk User Primary Key
     * @param newPerson new Person Data
     * @param deptPk Department Primary Key
     * @param affPk Affiliate Primary Key
     * @return the Person Primary Key if add was successful.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Integer addPerson    (Integer userPk, NewPerson newPerson, Integer deptPk,  Integer affPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        Integer personPk;

        //inserts new Person data to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PERSON);

            DBUtil.setNullableInt(ps, 1, newPerson.getPrefixNm());
            ps.setString(2, newPerson.getFirstNm());
            ps.setString(3, newPerson.getMiddleNm());
            ps.setString(4, newPerson.getLastNm());
            DBUtil.setNullableInt(ps, 5, newPerson.getSuffixNm());
            ps.setString(6, newPerson.getNickNm());
            ps.setString(7, newPerson.getAltMailingNm());
            ps.setString(8, newPerson.getSsn());
            DBUtil.setNullableBooleanAsShort(ps, 9, newPerson.getSsnValid());
            //Check to see if this person has a duplicate SSN
            DBUtil.setNullableBooleanAsShort(ps, 10, new Boolean(isDuplicateSSN(newPerson.getSsn())));
            DBUtil.setBooleanAsShort(ps, 11, newPerson.getMemberFg().booleanValue());
            ps.setInt(12, userPk.intValue());
            ps.setInt(13, userPk.intValue());
            personPk = DBUtil.insertAndGetIdentity(con, ps);

            ps.close();
            ps = null;

            if (newPerson.getThePhoneData()!=null) {
                addPersonPhone(userPk, deptPk, personPk, newPerson.getThePhoneData());
                // Set the newly added Phone Number as the Primary
                ps = con.prepareStatement(SQL_UPDATE_PHONE_PRIMARY_FLAG);
                ps.setInt(1, personPk.intValue());
                ps.execute();
                ps.close();
            }

            addPersonComment(userPk, personPk, newPerson.getTheCommentData());
            addPersonEmail(userPk, personPk, newPerson.getTheEmailData());
            addPersonAddress(userPk, personPk, newPerson.getThePersonAddress(), deptPk, affPk);

            // Create stub for Person Demographics
            ps = con.prepareStatement(SQL_INSERT_DEMOGRAPHICS_STUB);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, userPk.intValue());
            ps.execute();

            ps.close();
            ps = null;

            // Create stub for Person Political Legislative
            ps = con.prepareStatement(SQL_INSERT_POLITICAL_LEGISLATIVE_STUB);
            ps.setInt(1, personPk.intValue());
            ps.execute();

            // Add the Person as a User
            m_maintainUsers.addUser(personPk);

            //update any duplicate SSN flags
            //updateDuplicateSsn(newPerson.getSsn(), null);

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return personPk;
    }


    /**
     * @J2EE_METHOD  --  addPerson
     * This method allows a person to be added to the database. The action must dictate the
     *  flow to verify that a person doesn't already exist.
     *
     * @param userPk User Primary Key
     * @param newPerson new Person Data
     * @return the Person Primary Key if add was successful.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Integer addPerson    (Integer userPk, NewPerson newPerson)
    {
        Connection con = null;
        PreparedStatement ps = null;
        Integer personPk;

        //inserts new Person data to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PERSON);

            DBUtil.setNullableInt(ps, 1, newPerson.getPrefixNm());
            ps.setString(2, newPerson.getFirstNm());
            ps.setString(3, newPerson.getMiddleNm());
            ps.setString(4, newPerson.getLastNm());
            DBUtil.setNullableInt(ps, 5, newPerson.getSuffixNm());
            ps.setString(6, newPerson.getNickNm());
            ps.setString(7, newPerson.getAltMailingNm());
            ps.setString(8, newPerson.getSsn());
            DBUtil.setNullableBooleanAsShort(ps, 9, newPerson.getSsnValid());
            //Check to see if this person has a duplicate SSN
            DBUtil.setNullableBooleanAsShort(ps, 10, new Boolean(isDuplicateSSN(newPerson.getSsn())));
            DBUtil.setBooleanAsShort(ps, 11, newPerson.getMemberFg().booleanValue());
            ps.setInt(12, userPk.intValue());
            ps.setInt(13, userPk.intValue());
            personPk = DBUtil.insertAndGetIdentity(con, ps);

            // Create stub for Person Demographics
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_DEMOGRAPHICS_STUB);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, userPk.intValue());
            ps.execute();
            ps.close();

            // Create stub for Person Political Legislative
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_POLITICAL_LEGISLATIVE_STUB);
            ps.setInt(1, personPk.intValue());
            ps.execute();

            // Create stubs for Person Emails
            addPersonEmail(userPk, personPk, newPerson.getTheEmailData());

            // Add the Person as a User
            m_maintainUsers.addUser(personPk);

            //update any duplicate SSN flags
            updateDuplicateSsn(newPerson.getSsn(), null);

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return personPk;
    }

    /**
     * @J2EE_METHOD  --  addPersonAddress
     * Allows the adding of a new Address for a person.
     *
     * @param userPk User Primary Key
     * @param personPK Person Primary Key
     * @param personAddressData Address Data
     * @param deptPk Department Primary Key
     * @param affPk Affiliate Primary Key
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void addPersonAddress    (Integer userPk, Integer personPk, PersonAddress personAddressData, Integer deptPk, Integer affPk)
    {
        if (personAddressData==null) return;
        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:addPersonAddress: personAddressData="+personAddressData);
        logger.debug("----------------------------------------------------------------------");
        if(deptPk != null) {
            m_addressBean.addByDepartment( userPk, deptPk,  personPk, personAddressData );
        }else if(affPk != null) {
            m_addressBean.addByAffiliate( userPk, affPk,  personPk, personAddressData );
        }
    }

    /**
     * @J2EE_METHOD  --  addPersonPhone
     * This method allows data for one phone number to be added to the database.
     *
     * @param userPk User Primary Key
     * @param dept User Department
     * @param personPk Person Primary Key
     * @param personPhoneData Phone Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void addPersonPhone    (Integer userPk, Integer dept, Integer personPk, PhoneData personPhoneData)
    {
        Connection con = null;
        PreparedStatement ps = null;
        Integer phonePk = null;

        //inserts PhoneData to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PHONE);

            ps.setInt(1, personPk.intValue());
            ps.setString(2, personPhoneData.getAreaCode());
            ps.setString(3, personPhoneData.getPhoneNumber());
            ps.setString(4, personPhoneData.getCountryCode());
            DBUtil.setNullableBooleanAsShort(ps, 5, personPhoneData.getPhonePrmryFg());
            DBUtil.setNullableBooleanAsShort(ps, 6, personPhoneData.getPhonePrivateFg());
            ps.setInt(7, personPhoneData.getPhoneType().intValue());
            DBUtil.setNullableBooleanAsShort(ps, 8, personPhoneData.getPhoneDoNotCallFg());
            ps.setString(9, personPhoneData.getPhoneExtension());
            DBUtil.setNullableInt(ps, 10, dept);
            DBUtil.setNullableBooleanAsShort(ps, 11, personPhoneData.getPhoneBadFlag());
            ps.setTimestamp(12, personPhoneData.getPhoneBadDate());
            ps.setInt(13, userPk.intValue());
            ps.setInt(14, userPk.intValue());

            phonePk = DBUtil.insertAndGetIdentity(con, ps);

            // reset any old primary dept phones if inserting a primary phone
            if ((personPhoneData.getPhonePrmryFg() != null) &&
                (personPhoneData.getPhonePrmryFg().booleanValue() == true)) {

                resetPersonPhonePrimary(userPk, dept, personPk, phonePk);
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return;
    }

    /**
     * @J2EE_METHOD  --  getDemographicRelations
     * Retrieves the Child Data for the person indicated.
     *
     * @param personPK Person Primary Key
     * @return the Collection of RelationData objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getDemographicRelations    (Integer personPK)
    {
 return null;
    }

    /**
     * @J2EE_METHOD  --  getExistingPersons
     * Search for existing persons based on SSN, Last Name and First Name.
     *
     * @param personCriteria search criteria
     * @return the Collection of PersonResult objects
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getExistingPersons    (PersonCriteria personCriteria)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        PersonResult data = new PersonResult();
        ResultSet rs = null;
        Collection list = new ArrayList();

        //gets a group of persons with same criteria from the database
        try {
           con = DBUtil.getConnection();
            if (personCriteria.getFirstNm() != null) {
                builder.addCriterion("first_nm", personCriteria.getFirstNm());
            }
            if (personCriteria.getLastNm() != null) {
                builder.addCriterion("last_nm", personCriteria.getLastNm());
            }
            if (personCriteria.getSuffixNm() != null) {
                builder.addCriterion("suffix_nm", personCriteria.getSuffixNm());
            }
            if (personCriteria.getDob() != null) {
                builder.addCriterion("dob", personCriteria.getDob().toString());
            }
            if (personCriteria.getSsn() != null) {
                builder.addCriterion("ssn", personCriteria.getSsn());
            }
            builder.addOrderBy("person_nm asc");

            ps = builder.getPreparedStatement(SQL_SELECT_DUPLICATE_PERSON, con);
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    data = new PersonResult();
                    data.setPersonPk(new Integer (rs.getInt("person_pk")));
                    data.setPersonNm(rs.getString("person_nm"));

                    data.setSsn(rs.getString("ssn"));

                    data.setPersonAddr(rs.getString("personAddr"));

                    data.setCity(rs.getString("personAddrCity"));
                    data.setState(rs.getString("personAddrState"));
                    data.setPersonAddrPostalCode(rs.getString("personAddrPostalCode"));
                    list.add(data);

                } while (rs.next());
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }

    /**
     * @J2EE_METHOD  --  getDuplicatePersons
     * Search for existing persons based on SSN, Last Name and First Name.
     *
     * @param personCriteria search criteria
     * @param list to be passed by reference that will contain the duplicate results
     * @return int of the total count of duplicate persons
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getDuplicatePersons    (PersonCriteria personCriteria, List result)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        PersonResult data = new PersonResult();
        ResultSet rs = null;
        Collection list = new ArrayList();
        int count = 0;

        //gets a group of persons with same criteria from the database
        try {
           con = DBUtil.getConnection();
            if (personCriteria.getFirstNm() != null) {
                builder.addCriterion("first_nm", personCriteria.getFirstNm());
            }
            if (personCriteria.getLastNm() != null) {
                builder.addCriterion("last_nm", personCriteria.getLastNm());
            }
            if (personCriteria.getSuffixNm() != null) {
                builder.addCriterion("suffix_nm", personCriteria.getSuffixNm());
            }
            if (personCriteria.getDob() != null) {
                builder.addCriterion("dob", personCriteria.getDob().toString());
            }
            if (personCriteria.getSsn() != null) {
                builder.addCriterion("ssn", personCriteria.getSsn());
            }

            ps = builder.getPreparedStatement(SQL_SELECT_COUNT_PERSON, con);
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count == 0)
                return 0;

            //get the count
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            //create the order by clause
            String orderBy = null;
            if (personCriteria != null &&        //<-- don't sort if sort is null
                personCriteria.getSortField() != PersonCriteria.SORT_NONE) {  //<-- don't sort if 'none' specified

                orderBy = (String)CollectionUtil.transliterate(
                    personCriteria.getSortField(),
                    PersonCriteria.SORT_FIELD_IDS,
                    SORT_FIELD_COLUMNS);

                if (personCriteria.getSortOrder() == PersonCriteria.SORT_DESCENDING)
                    orderBy += " DESC";
            }

            //create the statement that gets the data
            builder.addOrderBy(orderBy);
            ps = builder.getPreparedStatement(SQL_SELECT_DUPLICATE_PERSON, con);
            rs = ps.executeQuery();

            if (personCriteria != null)
                rs.absolute((personCriteria.getPage() * personCriteria.getPageSize()) + 1);
            else
                rs.next();

            //put the results into a the list of PersonResult objects
            int index = 0;
            int pageSize = personCriteria != null ? personCriteria.getPageSize() : 0;
            int startIndex = personCriteria == null ? 0 : personCriteria.getPage() * pageSize;
            while (
                index + startIndex < count &&
                (personCriteria == null || index < pageSize))
            {
                PersonResult personResult = new PersonResult();
                personResult.setPersonPk(new Integer(rs.getInt("person_pk")));
                personResult.setPersonNm(rs.getString("person_nm"));
                personResult.setSsn(rs.getString("ssn"));
                personResult.setPersonAddr(rs.getString("personAddr"));
                personResult.setCity(rs.getString("personAddrCity"));
                personResult.setState(rs.getString("personAddrState"));
                personResult.setPersonAddrPostalCode(rs.getString("personAddrPostalCode"));
                result.add(personResult);
                rs.next();
                index++;
            }

        } catch (SQLException e) {
            throw new EJBException("Error getting duplicate persons MaintainPersonsBean.getDuplicatePersons()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return count;
    }

    /**
     * @J2EE_METHOD  --  getDuplicateSSN
     * Search for existing persons based on SSN.
     *
     * @param person primary key who is being checked, should not be in the result set
     * @param personCriteria search criteria
     * @param list to be passed by reference that will contain the duplicate results
     * @return int of the total count of duplicate persons
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getDuplicateSSN    (PersonCriteria personCriteria, List result)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        PersonResult data = new PersonResult();
        ResultSet rs = null;
        Collection list = new ArrayList();
        int count = 0;

        //gets a group of persons with same criteria from the database
        try {
           con = DBUtil.getConnection();
//           if (personCriteria.getSsn() == null) return 0;
            if (personCriteria.getSsn() != null) {
                builder.addCriterion("ssn", personCriteria.getSsn());
            }else return 0;

//           if (personPk == null) personPk = new Integer(0);  // this is an Add

//           ps = con.prepareStatement(SQL_SELECT_COUNT_PERSON + " WHERE ssn = ? AND p.person_pk <> ? ");
            ps = builder.getPreparedStatement(SQL_SELECT_COUNT_PERSON, con);
//           ps.setString(1, personCriteria.getSsn());
//           ps.setInt(2, personPk.intValue());

           rs = ps.executeQuery();
           rs.next();
           count = rs.getInt(1);
           if (count == 0)
               return 0;

           //get the count
           rs.close();
           rs = null;
           ps.close();
           ps = null;

           //create the order by clause
           String orderBy = null;
           if (personCriteria != null &&        //<-- don't sort if sort is null
               personCriteria.getSortField() != PersonCriteria.SORT_NONE) {  //<-- don't sort if 'none' specified

                   orderBy = (String)CollectionUtil.transliterate(
                    personCriteria.getSortField(),
                    PersonCriteria.SORT_FIELD_IDS,
                    SORT_FIELD_COLUMNS);

                if (personCriteria.getSortOrder() == PersonCriteria.SORT_DESCENDING)
                    orderBy += " DESC";
            }

logger.debug("----------------------------------------------------------------------");
logger.debug("MaintainPersonsBean:getDuplicateSSN getPage="+personCriteria.getPage());
logger.debug("MaintainPersonsBean:getDuplicateSSN getPageSize="+personCriteria.getPageSize());
//logger.debug("MaintainPersonsBean:getDuplicateSSN sql="+SQL_SELECT_DUPLICATE_PERSON + orderBy);
logger.debug("----------------------------------------------------------------------");
            //create the statement that gets the data
            builder.addOrderBy(orderBy);
            ps = builder.getPreparedStatement(SQL_SELECT_DUPLICATE_PERSON, con);
//            ps = con.prepareStatement(SQL_SELECT_DUPLICATE_PERSON + orderBy);
//            ps.setString(1, personCriteria.getSsn());
//            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();

/*           if (personCriteria != null)
                rs.absolute((personCriteria.getPage() * personCriteria.getPageSize()) + 1);
            else
*/                rs.next();

            //put the results into a the list of PersonResult objects
            int index = 0;
            int pageSize = personCriteria != null ? personCriteria.getPageSize() : 0;
            int startIndex = personCriteria == null ? 0 : personCriteria.getPage() * pageSize;
            while (
                index + startIndex < count &&
                (personCriteria == null || index < pageSize))
            {
                PersonResult personResult = new PersonResult();
                personResult.setPersonPk(new Integer(rs.getInt("person_pk")));
                personResult.setPersonNm(rs.getString("person_nm"));
                personResult.setSsn(rs.getString("ssn"));
                personResult.setPersonAddr(rs.getString("personAddr"));
                personResult.setCity(rs.getString("personAddrCity"));
                personResult.setState(rs.getString("personAddrState"));
                personResult.setPersonAddrPostalCode(rs.getString("personAddrPostalCode"));
                result.add(personResult);
                rs.next();
                index++;
            }

        } catch (SQLException e) {
            throw new EJBException("Error getting duplicate persons MaintainPersonsBean.getDuplicateSSN()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return count;
    }

    /**
     * * Retrieves the ethnic origin of the person from the cope table.
     *
	 * @param personPk Person Primary Key
	 * @return the value.
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Supports"
	 */
	public Integer getEthnicOriginFromCope(Integer personPk)
   	{
		Connection conn = DBUtil.getConnection();
	 	PreparedStatement ps = null;
	 	ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQL_SELECT_ETHNIC_ORIGIN_COPE);
			ps.setInt(1, personPk.intValue());
			rs = ps.executeQuery();
			int value = 0;
			while(rs.next()) {
				value = rs.getInt("ethnic_origin");
			}
			return new Integer(value);
		} catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn,ps, rs);
		}
	}

    /**
     * Retrieves the gender of the person from the cope table.
     *
     * @param personPk Person Primary Key
     * @return the value.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Integer getGenderFromCope(Integer personPk)
    {
 		Connection conn = DBUtil.getConnection();
	 	PreparedStatement ps = null;
 		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQL_SELECT_GENDER_COPE);
			ps.setInt(1, personPk.intValue());
			rs = ps.executeQuery();
			int value = 0;
			while(rs.next()) {
				value = rs.getInt("gender");
			}
			return new Integer(value);
		} catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn,ps, rs);
		}
	}

    /**
     * Retrieves the demographic data for a person.
     *
     * @param personPK Person Primary Key
     * @return the DemographicData object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public DemographicData getGeneralDemographics(Integer personPK)
    {
 		Connection conn = DBUtil.getConnection();
	 	PreparedStatement ps = null;
 		ResultSet rs = null;
 		DemographicData data = new DemographicData();
		try {
			// get and set general demographic info
			ps = conn.prepareStatement(SQL_SELECT_GENERAL_DEMOGRAPHIC);
			ps.setInt(1, personPK.intValue());
			rs = ps.executeQuery();
			while(rs.next()) {
				data.setDob(rs.getTimestamp(1));
				data.setDeceasedDt(rs.getTimestamp(2));
				data.setGenderCodePK(new Integer(rs.getInt(3)));
				data.setEthnicOriginCodePK(new Integer(rs.getInt(4)));
				data.setCitizenshipCodePK(new Integer(rs.getInt(5)));
				data.setMaritalStatusCodePK(new Integer(rs.getInt(6)));
				data.setReligionCodePK(new Integer(rs.getInt(7)));
				data.setDeceasedFg(new Boolean(rs.getBoolean(8)));
			}

			rs.close();
			ps.close();

			// if gender data does not exist in the Person_Demographics table
			if(data.getGenderCodePK().intValue() == 0) {
				data.setGenderCodePK(getGenderFromCope(personPK));
			}

			// if ethnic origin data does not exist in the Person_Demographics table
			if(data.getEthnicOriginCodePK().intValue() == 0) {
				data.setEthnicOriginCodePK(getEthnicOriginFromCope(personPK));
			}

			// get and set person disabilities info
			ArrayList disabilities = new ArrayList();
			ps = conn.prepareStatement(SQL_SELECT_PERSON_DISABILITIES);
			ps.setInt(1, personPK.intValue());
			rs = ps.executeQuery();
			while(rs.next()) {
				disabilities.add(new Integer(rs.getInt(1)));
			}
			data.setDisabilityCodePKs(disabilities);
			rs.close();
			ps.close();

			// get and set person accomodation info
			ArrayList disabilityAccommodationCodePKs = new ArrayList();
			ps = conn.prepareStatement(SQL_SELECT_PERSON_DISABILITY_ACCOMODATION);
			ps.setInt(1, personPK.intValue());
			rs = ps.executeQuery();
			while(rs.next()) {
				disabilityAccommodationCodePKs.add(new Integer(rs.getInt(1)));
			}
			data.setDisabilityAccommodationCodePKs(disabilityAccommodationCodePKs);
			rs.close();
			ps.close();

			// get and set person language info
			ArrayList otherLanguageCodePKs = new ArrayList();
			ps = conn.prepareStatement(SQL_SELECT_PERSON_LANGUAGE);
			ps.setInt(1, personPK.intValue());
			rs = ps.executeQuery();
			while(rs.next()) {
				// if the language is primary
				if(rs.getBoolean(1)) {
					data.setPrimaryLanguageCodePK(new Integer(rs.getInt(2)));
				}
				else {
					otherLanguageCodePKs.add(new Integer(rs.getInt(2)));
				}
			}
			data.setOtherLanguageCodePKs(otherLanguageCodePKs);
			rs.close();
			ps.close();

			// get and set person spouse info
			ps = conn.prepareStatement(SQL_SELECT_PERSON_RELATION);
			ps.setInt(1, personPK.intValue());
			ps.setInt(2, Codes.Relation.SPOUSE_DOMESTIC_PARTNER.intValue());
			rs = ps.executeQuery();
			while(rs.next()) {
				RelationData relationData = new RelationData();
				relationData.setRelativeFirstNm(rs.getString(1));
				relationData.setRelativeMiddleNm(rs.getString(2));
				relationData.setRelativeLastNm(rs.getString(3));
				relationData.setRelativeSuffixNm(new Integer(rs.getInt(4)));
				relationData.setRelativePk(new Integer(rs.getInt(6)));
				data.setThePartnerRelationData(relationData);
			}
			rs.close();
			ps.close();

			// get and set children info
			ArrayList theChildrenRelationData = new ArrayList();
			ps = conn.prepareStatement(SQL_SELECT_PERSON_RELATION);
			ps.setInt(1, personPK.intValue());
			ps.setInt(2, Codes.Relation.CHILD.intValue());
			rs = ps.executeQuery();
			while(rs.next()) {
				RelationData relationData = new RelationData();
				relationData.setRelativeFirstNm(rs.getString(1));
				relationData.setRelativeMiddleNm(rs.getString(2));
				relationData.setRelativeLastNm(rs.getString(3));
				relationData.setRelativeSuffixNm(new Integer(rs.getInt(4)));
				relationData.setRelativeBirthDt(rs.getTimestamp(5));
				relationData.setRelativePk(new Integer(rs.getInt(6)));
				theChildrenRelationData.add(relationData);
			}
			data.setTheChildrenRelationData((RelationData[])theChildrenRelationData.toArray(new RelationData[0]));
		} catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn,ps, rs);
		}
		return data;
    }

    /**
     * @J2EE_METHOD  --  getPersonAddresses
     * Retrieves the set of existing Addresses for a person.
     *
     * @param personPK Person Primary Key
     * @return the Collection of PersonAddress objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getPersonAddresses    (Integer personPK)
    {
 return null;
    }

    /**
     * @J2EE_METHOD  --  getPersonDetail
     * Retrieves the data for a specific person.
     *
     * @param personPK Person Primary Key
     * @param dept Department the User belongs to
     * @return the PersonData object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PersonData getPersonDetail    (Integer personPk, Integer dept)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PersonData data = new PersonData();
        CommentData cdata = new CommentData();
        RecordData rdata = new RecordData();
        EmailData edata = new EmailData();
        PhoneData pdata = new PhoneData();
        ResultSet rs = null;

        //gets PersonData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PERSON);
            System.out.println("**************personPk.intValue()" + personPk.intValue());
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( !rs.next())
                throw new EJBException("Person with pk " + personPk + "not found");

                data.setPersonPk(new Integer(rs.getInt("person_pk")));
                data.setPrefixNm(new Integer(rs.getInt("prefix_nm")));
                data.setFirstNm(rs.getString("first_nm"));
                data.setMiddleNm(rs.getString("middle_nm"));
                data.setLastNm(rs.getString("last_nm"));
                data.setSuffixNm(new Integer(rs.getInt("suffix_nm")));
                data.setNickNm(rs.getString("nick_nm"));
                data.setAltMailingNm(rs.getString("alternate_mailing_nm"));
                data.setSsn(rs.getString("ssn"));
                data.setSsnValid(new Boolean (rs.getBoolean("valid_ssn_fg")));
                data.setSsnDuplicate(new Boolean (rs.getBoolean("duplicate_ssn_fg")));
                data.setMarkedForDeletionFg(new Boolean(rs.getBoolean("marked_for_deletion_fg")));

                data.setAddressPk(new Integer (rs.getInt("address_pk")));

                /* Get the latest comment */
                cdata.setComment(rs.getString("comment_txt"));
                cdata.setCommentDt(rs.getTimestamp("comment_dt"));
                rdata.setCreatedBy(new Integer (rs.getInt("created_user_pk")));
                cdata.setRecordData(rdata);
                data.setTheCommentData(cdata);

                data.setTheEmailData(getPersonEmails(personPk));
                data.setThePhoneData(getPersonPhones(personPk, dept));

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * @J2EE_METHOD  --  getPersonEmails
     * Retrieves the set of email addresses and associated data for a person.
     *
     * @param personPK Person Primary Key
     * @return the Collection of EmailData objects, NULL if nothing is found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getPersonEmails    (Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        EmailData data = null;
        RecordData rdata = null;
        ResultSet rs = null;
        Collection list = new ArrayList();

        //gets EmailData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EMAIL);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    data = new EmailData();
                    rdata = new RecordData();

                    data.setEmailPk(new Integer (rs.getInt("email_pk")));
                    data.setEmailBadFg(new Boolean(rs.getBoolean("email_bad_fg")));
                    data.setEmailMarkedBadDt(rs.getTimestamp("email_marked_bad_dt"));
                    data.setEmailType(new Integer (rs.getInt("email_type")));
                    data.setPersonEmailAddr(rs.getString("person_email_addr"));
                    data.setIsPrimary(new Boolean(rs.getBoolean("isPrimary")));

                    rdata.setCreatedBy(new Integer (rs.getInt("created_user_pk")));
                    rdata.setCreatedDate(rs.getTimestamp("created_dt"));
                    rdata.setModifiedBy(new Integer (rs.getInt("lst_mod_user_pk")));
                    rdata.setModifiedDate(rs.getTimestamp("lst_mod_dt"));

                    data.setTheRecordData(rdata);
                    list.add(data);
                } while (rs.next());
            } else
               throw new EJBException("No Email Addresses found.");

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }

    /**
     * @J2EE_METHOD  --  getPersonPhones
     * Retrieves the set of phone data for a person in a particular department.
     *
     * @param personPK Person Primary Key
     * @param dept Department the User belongs to
     * @return the Collection of PhoneData objects, NULL if nothing is found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getPersonPhones    (Integer personPk, Integer dept)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PhoneData data = null;
        RecordData rdata = null;
        ResultSet rs = null;
        Collection list = new ArrayList();

        //gets PhoneData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PHONE_NUMBERS);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, dept == null ? 0 : dept.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    data = new PhoneData();
                    rdata = new RecordData();

                    data.setCountryCode(rs.getString("country_cd"));
                    data.setAreaCode(rs.getString("area_code"));
                    data.setPhoneNumber(rs.getString("phone_no"));
                    data.setPhoneBadFlag(new Boolean(rs.getBoolean("phone_bad_fg")));
                    data.setPhoneBadDate(rs.getTimestamp("phone_marked_bad_dt"));
                    data.setPhoneExtension(rs.getString("phone_extension"));
                    data.setPhonePrmryFg(new Boolean(rs.getBoolean("phone_prmry_fg")));
                    data.setPhonePrivateFg(new Boolean(rs.getBoolean("phone_private_fg")));
                    data.setPhonePk(new Integer(rs.getInt("phone_pk")));
                    data.setDept(new Integer(rs.getInt("dept")));
                    data.setPhoneType(new Integer(rs.getInt("phone_type")));
                    data.setPhoneDoNotCallFg(new Boolean(rs.getBoolean("phone_do_not_call_fg")));
                    rdata.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                    rdata.setCreatedDate(rs.getTimestamp("created_dt"));
                    rdata.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                    rdata.setModifiedDate(rs.getTimestamp("lst_mod_dt"));

                    data.setTheRecordData(rdata);
                    list.add(data);
                } while (rs.next());
            } else
                return null;

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }

    /**
     * @J2EE_METHOD  --  getPersonPhone
     * Retrieves an individual of phone number for a person.
     *
     * @param phonePk Phone Number Primary Key
     * @return a PhoneData object, NULL if nothing is found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PhoneData getPersonPhone    (Integer phonePk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PhoneData data = null;
        RecordData rdata = null;
        ResultSet rs = null;

        //gets PhoneData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PHONE_NUMBER);
            ps.setInt(1, phonePk.intValue());
            rs = ps.executeQuery();
            if ( !rs.next())
                throw new EJBException("getPersonPhone: Result set error");

            data = new PhoneData();
            rdata = new RecordData();

            data.setCountryCode(rs.getString("country_cd"));
            data.setAreaCode(rs.getString("area_code"));
            data.setPhoneNumber(rs.getString("phone_no"));
            data.setPhoneBadFlag(new Boolean(rs.getBoolean("phone_bad_fg")));
            data.setPhoneBadDate(rs.getTimestamp("phone_marked_bad_dt"));
            data.setPhoneExtension(rs.getString("phone_extension"));
            data.setPhonePrmryFg(new Boolean(rs.getBoolean("phone_prmry_fg")));
            data.setPhonePrivateFg(new Boolean(rs.getBoolean("phone_private_fg")));
            data.setPhonePk(new Integer(rs.getInt("phone_pk")));
            data.setDept(new Integer(rs.getInt("dept")));
            data.setPhoneType(new Integer(rs.getInt("phone_type")));
            data.setPhoneDoNotCallFg(new Boolean(rs.getBoolean("phone_do_not_call_fg")));
            rdata.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
            rdata.setCreatedDate(rs.getTimestamp("created_dt"));
            rdata.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
            rdata.setModifiedDate(rs.getTimestamp("lst_mod_dt"));

            data.setTheRecordData(rdata);

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Retrieves the political data for a person.
     *
     * @param personPK Person Primary Key
     * @return the PoliticalData object.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public PoliticalData getPoliticalData(Integer personPK)
    {
 		Connection conn = DBUtil.getConnection();
 		PreparedStatement ps = null;
 		ResultSet rs = null;
 		PoliticalData data = new PoliticalData();
		try {
			// retrieve records from the COM_AFL_CIO_COPE table, if none exist proceed to the
			// Person_Political_Legislative table
			ps = conn.prepareStatement(SQL_SELECT_PERSON_POLITICAL_LEGISLATIVE_COM_AFL_CIO_COPE);
			ps.setInt(1, personPK.intValue());
			rs = ps.executeQuery();
			if(rs.next()) {
				data.setPoliticalParty(new Integer(rs.getInt(1)));
				data.setPoliticalRegisteredVoter(new Integer(rs.getInt(2)));
				data.setPoliticalObjectorFg(new Boolean(rs.getBoolean(3)));
				data.setPacContributorFg(new Boolean(rs.getBoolean(4)));
				data.setPoliticalDoNotCallFg(new Boolean(rs.getBoolean(5)));
				data.setWardNumber(rs.getString(6));
				data.setPrecinctNumber(rs.getString(7));
				data.setPrecinctName(rs.getString(8));
				data.setCongDist(rs.getString(9));
				data.setUpperDist(rs.getString(10));
				data.setLowerDist(rs.getString(11));
			}
			else {
				ps.close();
				rs.close();
				ps = conn.prepareStatement(SQL_SELECT_PERSON_POLITICAL_LEGISLATIVE);
				ps.setInt(1, personPK.intValue());
				rs = ps.executeQuery();
				while(rs.next()) {
					data.setPoliticalParty(new Integer(rs.getInt(1)));
					data.setPoliticalRegisteredVoter(new Integer(rs.getInt(2)));
					data.setPoliticalObjectorFg(new Boolean(rs.getBoolean(3)));
					data.setPacContributorFg(new Boolean(rs.getBoolean(4)));
					data.setPoliticalDoNotCallFg(new Boolean(rs.getBoolean(5)));
					data.setWardNumber(rs.getString(6));
					data.setPrecinctNumber(rs.getString(7));
					data.setPrecinctName(rs.getString(8));
				}
				ps.close();
				rs.close();
				ps = conn.prepareStatement(SQL_SELECT_DISTRICTS);
				ps.setInt(1, personPK.intValue());
				rs = ps.executeQuery();
				while(rs.next()) {
					data.setCongDist(rs.getString(1));
					data.setUpperDist(rs.getString(2));
					data.setLowerDist(rs.getString(3));
				}
			}
		} catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn,ps, rs);
		}
		return data;
    }

    /**
     * @J2EE_METHOD  --  isDuplicateSSN
     * Checks to see if more one or more existing persons has this ssn in the database.
     *
     * @param ssn search for ssn
     * @return TRUE if ssn found, and FALSE if ssn is not found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isDuplicateSSN    (String ssn)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean dup = false;
        ResultSet rs = null;

        //checks for duplicate ssn in the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNT_SSN);
            ps.setString(1, ssn);
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if (rs.getInt("Duplicate") == 1) dup = true;
                } while (rs.next());
            } else
                throw new EJBException("SQL_SELECT_COUNT_SSN failed to execute correctly");

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return dup;
    }

    /**
     * @J2EE_METHOD  --  isDuplicateSSNGreaterThan1
     * Checks to see if more one or more existing persons has this ssn in the database.
     *
     * @param ssn search for ssn
     * @return TRUE if ssn found, and FALSE if ssn is not found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isDuplicateSSNGreaterThan1    (String ssn)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean dup = false;
        ResultSet rs = null;

        //checks for duplicate ssn in the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNT_SSN_GREATER_THAN_1);
            ps.setString(1, ssn);
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if (rs.getInt("Duplicate") == 1) dup = true;
                } while (rs.next());
            } else
                throw new EJBException("SQL_SELECT_COUNT_SSN failed to execute correctly");

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return dup;
    }


	/**
     * @J2EE_METHOD  --  isExistingPerson
     * This method searches for existing persons based on the person criteria.
     *
     * @param personCriteria search criteria
     * @return TRUE if criteria matches any Persons, and FALSE if criteria doesn't match
     *  any Persons.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isExistingPerson    (PersonCriteria personCriteria)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean dup = false;
        boolean criteria = false;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        ResultSet rs = null;

        //checks if a person already exists in the database
        try {
            con = DBUtil.getConnection();
            if (personCriteria.getFirstNm() != null) {
                builder.addCriterion("first_nm", personCriteria.getFirstNm());
                criteria = true;
            }
            if (personCriteria.getLastNm() != null) {
                builder.addCriterion("last_nm", personCriteria.getLastNm());
                criteria = true;
            }
            if (personCriteria.getSuffixNm() != null) {
                builder.addCriterion("suffix_nm", personCriteria.getSuffixNm());
                criteria = true;
            }
            if (personCriteria.getDob() != null) {
                builder.addCriterion("dob", personCriteria.getDob().toString());
               criteria = true;
            }
            if (personCriteria.getSsn() != null) {
                builder.addCriterion("ssn", personCriteria.getSsn());
             criteria = true;
            }

            if (criteria) {
                ps = builder.getPreparedStatement(SQL_SELECT_COUNT_PERSON, con);
                rs = ps.executeQuery();
                if ( rs.next()) {
                    do {
                        if (rs.getInt("Duplicate") > 0)
                        dup = true;
                        } while (rs.next());

                } else
                    throw new EJBException("SQL_SELECT_COUNT_PERSON failed to execute correctly");
            }

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return dup;
    }

    /**
     * @J2EE_METHOD  --  isPoliticalObjector
     * Checks to see if the person is a Political Objector in the database.
     * All phone numbers will have the DO NOT CALL flag locked.
     *
     * @param personPk Person Primary Key
     * @return TRUE if a Political Objector, and FALSE if not.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isPoliticalObjector    (Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean politicalObjector = false;
        ResultSet rs = null;

        //checks the Political Objector Flag in the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PHONE_LOCKS);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    politicalObjector = rs.getBoolean("political_objector");
                    } while (rs.next());

            } else
                throw new EJBException("SQL_SELECT_PHONE_LOCKS failed to execute correctly");

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return politicalObjector;
    }

    /**
     * @J2EE_METHOD  --  isPoliticalObjector
     * Checks to see if the person is a Political Objector in the database.
     * All phone numbers will have the DO NOT CALL flag locked.
     *
     * @param personPk Person Primary Key
     * @return TRUE if DO NOT CALL is set, and FALSE if not.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isDoNotCall    (Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean doNotCall = false;
        ResultSet rs = null;

        //checks the Political Do Not Call Flag in the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PHONE_LOCKS);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    doNotCall = rs.getBoolean("do_not_call");
                    } while (rs.next());

            } else
                throw new EJBException("SQL_SELECT_PHONE_LOCKS failed to execute correctly");

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
        return doNotCall;
    }

    /**
     * @J2EE_METHOD  --  searchPersons
     * Performs a person search using the person criteria given.
     *
     * @param personCriteria the search criteria
     * @return the Collection of PersonResult objects.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int searchPersons (PersonCriteria personCriteria, Integer dept, ArrayList results)
    {
        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:searchPersons function begin.");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();

        int count = 0;

        try {
            con = DBUtil.getConnection();
            // Create the where clause using PreparedStatementBuilder
            buildPersonWhereClause(builder, personCriteria);
            String countFromClause = buildPersonCountFromClause(personCriteria);
            String fromClause = buildPersonFromClause(personCriteria);
            // add the rest of the SQL to create the count query
            builder.getPreparedStatementSQL(SQL_SELECT_COUNT_SEARCH_PERSON + countFromClause, true);
            ps = builder.getPreparedStatement(SQL_SELECT_COUNT_SEARCH_PERSON + countFromClause, con);
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt("cnt");

            logger.debug("searchPerson - count is: " + count);

            if (count == 0)
                return 0;

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            /**
             Now create the query to retrieve the results (if > 0 rows returned)
             - first set the order by from the criteria data
             */
              //create the order by clause
            String orderBy = null;
        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:searchPersons sort field=" + personCriteria.getSortField());
        logger.debug("MaintainPersonsBean:searchPersons order="+personCriteria.getSortOrder());
        logger.debug("----------------------------------------------------------------------");
            if (personCriteria != null &&        //<-- don't sort if sort is null
                personCriteria.getSortField() != PersonCriteria.FIELD_NONE) {  //<-- don't sort if 'none' specified

                orderBy = (String)CollectionUtil.transliterate(
                    personCriteria.getSortField(),
                    PersonCriteria.SORT_FIELD_IDS,
                    SORT_FIELD_SEARCH_COLUMNS);

                if (personCriteria.getSortOrder() == PersonCriteria.SORT_DESCENDING)
                    orderBy += " DESC";
                //create the statement that gets the data
                builder.addOrderBy(orderBy);
            } else if (personCriteria.getSortField() == PersonCriteria.FIELD_NONE) {
                /* add default Sort fields:
                 * last_nm
                 * first_nm
                 * council (numeric ASC on a string field)
                 * local (numeric ASC on a string field)
                 */
                //HLM: Fix defect #38
                builder.addOrderBy(" person_nm ASC ");
                builder.addOrderBy(" a.aff_stateNat_type ASC "); // order by numeric
                builder.addOrderBy(" int_council ASC "); // order by numeric
                builder.addOrderBy(" int_local ASC "); // order by numeric
            }

/*            if (!TextUtil.isEmptyOrSpaces(personCriteria.getOrderBy())) {
                StringBuffer sb = new StringBuffer("");

                // need to handle orderby of council and local by cast to retain
                // numeric ordering - also handled in set of criteria object for user
                // selection
                sb.append(personCriteria.getOrderBy());
                if (personCriteria.getOrdering() < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                builder.addOrderBy(sb.toString().trim());
            }
            else {
                * add default Sort fields:
                 * last_nm
                 * first_nm
                 * council (numeric ASC on a string field)
                 * local (numeric ASC on a string field)
                 *
                builder.addOrderBy(" p.last_nm ASC ");
                builder.addOrderBy(" p.first_nm ASC ");
                builder.addOrderBy(" int_council ASC "); // order by numeric
                builder.addOrderBy(" int_local ASC "); // order by numeric
            }
*/
            // create the query for the results

            ps = builder.getPreparedStatement(SQL_SELECT_SEARCH_PERSON + fromClause, con);
        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:searchPersons sql=" +
                    builder.getPreparedStatementSQL(SQL_SELECT_SEARCH_PERSON + fromClause, true) );
        logger.debug("MaintainPersonsBean:searchPersons count="+count);
        logger.debug("----------------------------------------------------------------------");

            rs = ps.executeQuery();

            // position the first row, based on the page requested and the page size
            if (personCriteria != null)
                rs.absolute((personCriteria.getPage() * personCriteria.getPageSize()) + 1);
            else
                rs.next();


           // put the results into a the list of memberResult objects
            int index = 0;
            int pageSize = personCriteria != null ? personCriteria.getPageSize() : 0;
            int startIndex = personCriteria == null ? 0 : personCriteria.getPage() * pageSize;
            while (index + startIndex < count &&
                  (personCriteria == null || index < pageSize))
            {
                PersonResult personResult = new PersonResult();

                //need to create method for the readPersonSearchResult
                results.add(readPersonSearchResult(rs, personResult));

                rs.next();
                index++;
            }

       } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return count;

    }

    /**
     * Get the values out of the personCriteria object and create a where clause
     */
    private void buildPersonWhereClause(PreparedStatementBuilder pb, PersonCriteria pc) {

        // Name
        pb.addCriterion("p.prefix_nm", pc.getPrefixNm());
        // check for wildcard search, i.e. % at end of string
        if ((pc.getFirstNm() != null) && (pc.getFirstNm().indexOf("%") > 0))
        {
                pb.addCriterion("p.first_nm", pc.getFirstNm(), true);
         }
         else {
                pb.addCriterion("p.first_nm", pc.getFirstNm());
         }
        pb.addCriterion("p.middle_nm", pc.getMiddleNm());
        // check for wildcard search. i.e % at end of string
        if ((pc.getLastNm() != null) && (pc.getLastNm().indexOf("%") > 0))
        {
                pb.addCriterion("p.last_nm", pc.getLastNm(), true);
        }
        else {
            pb.addCriterion("p.last_nm", pc.getLastNm());
        }
        pb.addCriterion("p.suffix_nm", pc.getSuffixNm());

        // Address
        pb.addCriterion("pa.addr1", pc.getAddress1());
        pb.addCriterion("pa.addr2", pc.getAddress2());
        pb.addCriterion("pa.city", pc.getCity());
        pb.addCriterion("pa.state", pc.getState());
        pb.addCriterion("pa.zipcode", pc.getZipCode());
        pb.addCriterion("pa.zip_plus", pc.getZipPlus());
        pb.addCriterion("pa.county", pc.getCounty());
        pb.addCriterion("pa.province", pc.getProvince());
        pb.addCriterion("pa.country", pc.getCountry());

        // General Person Information
        pb.addCriterion("p.nick_nm", pc.getNickNm());
        pb.addCriterion("p.alternate_mailing_name", pc.getAltMailingNm());
        pb.addCriterion("p.ssn", pc.getSsn());
        // User ID
        pb.addCriterion("p.person_pk", pc.getPersonPk());
        pb.addCriterion("u.user_id", pc.getUserId());
        // phones and email address
        pb.addCriterion("pp.country_cd", pc.getCountryCode());
        pb.addCriterion("pp.area_code", pc.getAreaCode());
        pb.addCriterion("pp.phone_no", pc.getPhoneNumber());
        pb.addCriterion("pe.person_email_addr", pc.getPersonEmailAddr());
        // marked for deletion
        if (pc.getMarkedForDeletionFg() == 1 || pc.getMarkedForDeletionFg() == 0) // 1 is true, 0 is false, 2 means both valid and ssn (so omit from where clause)
            pb.addCriterion("p.marked_for_deletion_fg", new Integer(pc.getMarkedForDeletionFg()));

        org.afscme.enterprise.util.PreparedStatementBuilder.Criterion cr = null;
        //Persona
        String personaClause = null;
        if (new Integer(pc.getPersonaCode()) != null) {
            logger.debug("PersonaCode=" + pc.getPersonaCode());
            switch(pc.getPersonaCode()) {
                case 1:     //AFSCME Staff
                   cr = new Criterion("u.dept", personaClause);
                   cr.setOperator("IS NOT NULL");
                   pb.addCriterion(cr);
                   break;
                case 2:     //Affiliate Staff
                   cr = new Criterion();
                   cr.setOperator("EXISTS");
                   cr.setSubQuery("(SELECT 'X' FROM aff_staff WHERE person_pk = p.person_pk)");
                   pb.addCriterion(cr);
                   break;
                case 3:     //Other (Vendor)
                   cr = new Criterion("u.dept", personaClause);
                   cr.setOperator("IS NULL");
                   pb.addCriterion(cr);
                   pb.addCriterion("u.start_page", START_PAGE_VENDOR);
                   break;
                case 4:     //Member
                   pb.addCriterion("p.member_fg", new Integer(1));
                   break;
                case 5:     //PAC Contributor
                   cr = new Criterion();
                   cr.setField("p.person_pk");
                   cr.setSubQuery("(SELECT person_pk FROM Person_Political_Legislative WHERE pac_contributor_fg = 1)");
                   cr.setOperator("IN");
                   pb.addCriterion(cr);
                   break;
                case 6:     //Organization Associate
                    cr = new Criterion();
					cr.setField("p.person_pk");
					cr.setSubQuery("(SELECT person_pk FROM Ext_Org_Associates)");
					cr.setOperator("IN");
					pb.addCriterion(cr);
					break;
                default:
                    // do nothing...
                    break;
            }

        }

         /** DEPRECATED LOGIC - SMA now retrieved by getSystemAddress()
         When the search is dynamic, set System Mailing Address oriented where clause predicates
         * if SMA is referenced in the search criteria, or any SMA is referenced in the select list
         * since the address is hard_coded now, this predicate is required. . . .

        if (mc.getHasSelectedAddress()== true | mc.getHasAddressInWhere() == true )
        {
            pb.addCriterion("s.current_fg", new Integer(1)); // works ?

            }
        */

         /** DEPRECATED LOGIC - phones and emails retrieved by method calls currently
          only get Affiliate Relations Home Phone number & Primary email address
          pb.addCriterion("pp.dept", Codes.Department.MD);
          pb.addCriterion("pp.phone_type", Codes.PhoneType.HOME);
          pb.addCriterion("pe.email_type", Codes.EmailType.PRIMARY);
          */

        logger.debug("buildPersonWhereClause, after add criterion" +
            pb.getPreparedStatementSQL("NoneYet", true) ) ;

    }

    /* This method builds from clause base on user search parameter
     */
    private String buildPersonCountFromClause(PersonCriteria pc)
    {
        String fromClause = "";

        if ((pc.getAddress1() != null && pc.getAddress1().length() > 0) ||
            (pc.getAddress2() != null && pc.getAddress2().length() > 0) ||
            (pc.getCity() != null && pc.getCity().length() > 0) ||
            (pc.getState() != null && pc.getState().length() > 0) ||
            (pc.getZipCode() != null && pc.getZipCode().length() > 0) ||
            (pc.getZipPlus() != null && pc.getZipPlus().length() >0) ||
            (pc.getCounty() != null && pc.getCounty().length() > 0) ||
            (pc.getProvince() != null && pc.getProvince().length() > 0) ||
            (pc.getCountry() != null ))

        {
            fromClause = fromClause +
            "  INNER JOIN person_address pa ON p.person_pk = pa.person_pk  " +
            "   AND address_pk IN (SELECT address_pk  " +
            "                       	FROM person_SMA " +
            "                       WHERE person_pk = p.person_pk " +
            "                         AND current_fg = 1) ";
        }


        if ((pc.getUserId() != null && pc.getUserId().length() > 0) || (pc.getPersonaCode() == 1 || pc.getPersonaCode() == 2 || pc.getPersonaCode() == 3))
        {
            fromClause = fromClause +
            "  INNER JOIN Users u ON p.person_pk = u.person_pk ";
        }

        if (pc.getPersonEmailAddr() != null && pc.getPersonEmailAddr().length() > 0)
        {
            fromClause = fromClause +
            "  INNER JOIN Person_Email pe ON p.person_pk = pe.person_pk ";
        }

        if ((pc.getCountryCode() != null && pc.getCountryCode().length() > 0) ||
            (pc.getAreaCode() != null && pc.getAreaCode().length() > 0) ||
            (pc.getPhoneNumber() != null && pc.getPhoneNumber().length() > 0))
        {
            fromClause = fromClause +
            "  INNER JOIN Person_Phone pp ON p.person_pk = pp.person_pk ";
        }


        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:buildPersonCountFromClause countFromClause =" + fromClause);

        return fromClause;
    }
    private String buildPersonFromClause(PersonCriteria pc)
    {
        String fromClause = "";

        fromClause = "  LEFT OUTER JOIN Aff_Members am ON am.person_pk = p.person_pk  " +
        "  LEFT OUTER JOIN Aff_Organizations a ON am.aff_pk = a.aff_pk  ";

        if ((pc.getAddress1() != null && pc.getAddress1().length() > 0) ||
            (pc.getAddress2() != null && pc.getAddress2().length() > 0) ||
            (pc.getCity() != null && pc.getCity().length() > 0) ||
            (pc.getState() != null && pc.getState().length() > 0) ||
            (pc.getZipCode() != null && pc.getZipCode().length() > 0) ||
            (pc.getZipPlus() != null && pc.getZipPlus().length() >0) ||
            (pc.getCounty() != null && pc.getCounty().length() > 0) ||
            (pc.getProvince() != null && pc.getProvince().length() > 0) ||
            (pc.getCountry() != null ))

        {
            fromClause = fromClause +
            "  INNER JOIN person_address pa ON p.person_pk = pa.person_pk  " +
            "   AND address_pk IN (SELECT address_pk  " +
            "                       	FROM person_SMA " +
            "                       WHERE person_pk = p.person_pk " +
            "                         AND current_fg = 1) ";
        }else
        {
            fromClause = fromClause +
            "  LEFT OUTER JOIN person_address pa ON p.person_pk = pa.person_pk  " +
            "   AND address_pk IN (SELECT address_pk  " +
            "                       	FROM person_SMA " +
            "                       WHERE person_pk = p.person_pk " +
            "                         AND current_fg = 1) ";
        }

        if ((pc.getUserId() != null && pc.getUserId().length() > 0) || (pc.getPersonaCode() == 1 || pc.getPersonaCode() == 2 || pc.getPersonaCode() == 3))
        {
            fromClause = fromClause +
            "  INNER JOIN Users u ON p.person_pk = u.person_pk ";
        }else
        {
            fromClause = fromClause +
            "  LEFT OUTER JOIN Users u ON p.person_pk = u.person_pk ";
        }
        // if not searched for any fields in this table, then do not need to join since no fields are being displayed from this table.
        if (pc.getPersonEmailAddr() != null && pc.getPersonEmailAddr().length() > 0)
        {
            fromClause = fromClause +
            "  INNER JOIN Person_Email pe ON p.person_pk = pe.person_pk ";
        }

        if ((pc.getCountryCode() != null && pc.getCountryCode().length() > 0) ||
            (pc.getAreaCode() != null && pc.getAreaCode().length() > 0) ||
            (pc.getPhoneNumber() != null && pc.getPhoneNumber().length() > 0))
        {
            fromClause = fromClause +
            "  INNER JOIN Person_Phone pp ON p.person_pk = pp.person_pk ";
        }

        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:buildPersonFromClause fromClause = " + fromClause);

        return fromClause;

    }

    /**
     * Get the values out of the memberCriteria object and create a where clause
     */
    private PersonResult readPersonSearchResult(ResultSet rs, PersonResult pr) throws SQLException {

        pr.setPersonPk(new Integer(rs.getInt("person_pk")));

        pr.setPersonNm(rs.getString("person_nm"));
        pr.setSsn(rs.getString("ssn"));
/*
        pr.setAffType(new Character(rs.getString("aff_type").toCharArray()[0]));
        pr.setAffLocalSubChapter(rs.getString("int_local"));
        pr.setAffStateNatType(rs.getString("aff_stateNat_type"));
        pr.setAffSubUnit(rs.getString("aff_subUnit"));
        pr.setAffCouncilRetireeChap(rs.getString("int_council"));
*/
        Integer affPk = DBUtil.getIntegerOrNull(rs, "aff_pk");
        logger.debug("----------------------------------------------------------------------");
        logger.debug("MaintainPersonsBean:readPersonSearchResult affPk=" + affPk );
        logger.debug("MaintainPersonsBean:readPersonSearchResult affId=" + m_affiliates.getAffiliateData(affPk) );
        logger.debug("----------------------------------------------------------------------");

        if (affPk == null || affPk.intValue() < 1) {
            pr.setTheAffiliateIdentifier(null);
        } else {
            pr.setTheAffiliateIdentifier(m_affiliates.getAffiliateData(affPk).getAffiliateId());
        }


        pr.setPersonAddr(rs.getString("personAddr"));
        pr.setCity(rs.getString("personAddrCity"));
        pr.setState(rs.getString("personAddrState"));
        pr.setPersonAddrPostalCode(rs.getString("personAddrPostalCode"));
        pr.setUserId(rs.getString("user_id"));

        return pr;
    }

    /**
     * Updates General Demographic data for a person.
     *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
     * @param demData new Demographic Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void updateGeneralDemographics(Integer personPK, Integer userPk, DemographicData demData)
    {
		// this method is used to update all the information in the General Demographic edit screen
		updatePersonDemographics(personPK, userPk, demData);
		updatePersonDisabilities(personPK, userPk, demData.getDisabilityCodePKs());
		updatePersonAccommodations(personPK, userPk, demData.getDisabilityAccommodationCodePKs());
		updatePersonOtherLanguages(personPK, userPk, demData.getOtherLanguageCodePKs());
		updatePersonPrimaryLanguage(personPK, userPk, demData.getPrimaryLanguageCodePK());

		// delete only if all fields are blank and a record exists
		if(demData.getThePartnerRelationData().getRelativeFirstNm().equals("") &&
		   demData.getThePartnerRelationData().getRelativeMiddleNm().equals("") &&
		   demData.getThePartnerRelationData().getRelativeLastNm().equals("") &&
		   demData.getThePartnerRelationData().getRelativeSuffixNm() == null &&
		   demData.getThePartnerRelationData().getRelativePk() != null) {
		   deletePersonRelation(demData.getThePartnerRelationData().getRelativePk());
		}
		// update if the fields aren't blank
		// checking first name and last name since they are required by the UI
		else if(!demData.getThePartnerRelationData().getRelativeFirstNm().equals("") &&
				!demData.getThePartnerRelationData().getRelativeLastNm().equals("")) {
			updatePersonPartner(personPK, userPk, demData.getThePartnerRelationData());
		}
		// do nothing if there is no record

		updatePersonChildren(userPk, demData.getTheChildrenRelationData());
    }

    /**
	 * Updates Person Demographic data for a person.
	 *
	 * @param personPk Person Primary Key
	 * @param userPk User Primary Key
	 * @param demData new Demographic Data
	 * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
	public void updatePersonDemographics(Integer personPk, Integer userPk, DemographicData demData)
	{
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
 		try {
			ps = conn.prepareStatement(SQL_UPDATE_PERSON_DEMOGRAPHICS);
			ps.setTimestamp(1, demData.getDob());
			ps.setTimestamp(2, demData.getDeceasedDt());
			DBUtil.setNullableInt(ps, 3, demData.getGenderCodePK());
			DBUtil.setNullableInt(ps, 4, demData.getEthnicOriginCodePK());
			ps.setInt(5, demData.getCitizenshipCodePK().intValue());
			DBUtil.setNullableInt(ps, 6, demData.getReligionCodePK());
			DBUtil.setNullableInt(ps, 7, demData.getMaritalStatusCodePK());
			ps.setInt(8, userPk.intValue());
			ps.setBoolean(9, demData.getDeceasedFg().booleanValue());
			ps.setInt(10, personPk.intValue());
			ps.execute();
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
    }

    /**
     * Updates Person Demographic Gender data for a person.  Used by Apply Update
     *  Process since only Gender is in the file.
     *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
     * @param genderPk the pk of the Gender
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonDemographicsGender(Integer personPk, Integer userPk, Integer genderPk)
    {
        Connection conn = DBUtil.getConnection();
	PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(SQL_UPDATE_PERSON_DEMOGRAPHICS_GENDER);
            DBUtil.setNullableInt(ps, 1, genderPk);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, personPk.intValue());
            ps.execute();
	}  catch(SQLException e) {
            throw new EJBException(e);
	} finally {
            DBUtil.cleanup(conn, ps, null);
	}
    }


    /**
     * Updates Disabilities data for a person by deleting existing records and adding new ones.
     *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
     * @param disabilityCodePKs List of disability primary keys
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void updatePersonDisabilities(Integer personPk, Integer userPk, Collection disabilityCodePKs)
    {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
 		try {
			ps = conn.prepareStatement(SQL_DELETE_PERSON_DISABILITIES);
			ps.setInt(1, personPk.intValue());
			ps.execute();
			ps.close();

			// don't make a call to the database unnecessarily
			if(disabilityCodePKs.size() > 0) {
				ps = conn.prepareStatement(SQL_INSERT_PERSON_DISABILITIES);
				Iterator i = disabilityCodePKs.iterator();
				while(i.hasNext()) {
					Integer disabilityCodePK = (Integer)i.next();
					ps.setInt(1, personPk.intValue());
					ps.setInt(2, disabilityCodePK.intValue());
					ps.setNull(3, Types.VARCHAR);
					ps.setInt(4, userPk.intValue());
					ps.setInt(5, userPk.intValue());
					ps.addBatch();
				}
				ps.executeBatch();
			}
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
    }

    /**
	 * Updates Accommodations data for a person by deleting existing records and adding new ones.
	 *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
     * @param accommodationCodePKs List of accommodation primary keys
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void updatePersonAccommodations(Integer personPk, Integer userPk, Collection accommodationCodePKs)
 	{
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SQL_DELETE_PERSON_DISABILITY_ACCOMODATIONS);
			ps.setInt(1, personPk.intValue());
			ps.execute();
			ps.close();

			// don't make a call to the database unnecessarily
			if(accommodationCodePKs.size() > 0) {
				ps = conn.prepareStatement(SQL_INSERT_PERSON_DISABILITY_ACCOMODATIONS);
				Iterator i = accommodationCodePKs.iterator();
				while(i.hasNext()) {
					Integer accommodationCodePK = (Integer)i.next();
					ps.setInt(1, personPk.intValue());
					ps.setInt(2, accommodationCodePK.intValue());
					ps.setNull(3, Types.VARCHAR);
					ps.setInt(4, userPk.intValue());
					ps.setInt(5, userPk.intValue());
					ps.addBatch();
				}
				ps.executeBatch();
			}
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
 	}

    /**
	 * Updates or inserts Partner data for a person depending on whether a record already exists.
	 *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
     * @param partnerData new Relation Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
   public void updatePersonPartner(Integer personPk, Integer userPk, RelationData partnerData)
   {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQL_SELECT_PERSON_RELATION);
			ps.setInt(1, personPk.intValue());
			ps.setInt(2, Codes.Relation.SPOUSE_DOMESTIC_PARTNER.intValue());
			rs = ps.executeQuery();
			// if a record exists update, else insert
			if(rs.next()) {
				rs.close();
				ps.close();
				ps = conn.prepareStatement(SQL_UPDATE_PERSON_PARTNER);
				ps.setString(1, partnerData.getRelativeFirstNm());
				ps.setString(2, partnerData.getRelativeLastNm());
				ps.setString(3, partnerData.getRelativeMiddleNm());
				DBUtil.setNullableInt(ps, 4, partnerData.getRelativeSuffixNm());
				ps.setInt(5, userPk.intValue());
				DBUtil.setNullableInt(ps, 6, partnerData.getRelativePk());
				ps.execute();
			}
			else {
				rs.close();
				ps.close();
				partnerData.setPersonRelativeType(Codes.Relation.SPOUSE_DOMESTIC_PARTNER);
				addPersonRelation(personPk, userPk, partnerData);
			}
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
    }

    /**
	 * Updates Other Languages data for a person by deleting existing records and adding new ones.
	 * The new records are set as the person's secondary language.
     *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
	 * @param otherLanguageCodePKs List of Other Languages primary keys
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Required"
	 */
	public void updatePersonOtherLanguages(Integer personPk, Integer userPk, Collection otherLanguageCodePKs)
	{
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SQL_DELETE_PERSON_LANGUAGES);
			ps.setInt(1, personPk.intValue());
			ps.execute();
			ps.close();

			// don't make a call to the database unnecessarily
			if(otherLanguageCodePKs.size() > 0) {
				ps = conn.prepareStatement(SQL_INSERT_PERSON_LANGUAGES);
				Iterator i = otherLanguageCodePKs.iterator();
				while(i.hasNext()) {
					Integer otherLanguageCodePK = (Integer)i.next();
					ps.setInt(1, personPk.intValue());
					ps.setInt(2, otherLanguageCodePK.intValue());
					ps.setNull(3, Types.SMALLINT);
					ps.setInt(4, userPk.intValue());
					ps.setInt(5, userPk.intValue());
					ps.addBatch();
				}
				ps.executeBatch();
			}
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
    }

    /**
	 * Updates Primary Language data for a person.
	 *
     * @param personPk Person Primary Key
     * @param userPk User Primary Key
     * @param primaryLanguageCodePK Primary Language Primary Key
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonPrimaryLanguage(Integer personPk, Integer userPk, Integer primaryLanguageCodePK)
    {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			// don't make a call to the database unnecessarily
			if(primaryLanguageCodePK != null) {
				ps = conn.prepareStatement(SQL_INSERT_PERSON_LANGUAGES);
				ps.setInt(1, personPk.intValue());
				ps.setInt(2, primaryLanguageCodePK.intValue());
				ps.setBoolean(3, true);
				ps.setInt(4, userPk.intValue());
				ps.setInt(5, userPk.intValue());
				ps.execute();
			}
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
	}

    /**
	 * Updates Children data for a person.
	 *
	 * @param userPk User Primary Key
	 * @param relationData List of children
	 * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonChildren(Integer userPk, RelationData[] relationData)
    {
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			// don't make a call to the database unnecessarily
			if(relationData.length > 0) {
				ps = conn.prepareStatement(SQL_UPDATE_PERSON_CHILD);
				for(int i = 0; i < relationData.length; i++) {
					ps.setString(1, relationData[i].getRelativeFirstNm());
					ps.setString(2, relationData[i].getRelativeLastNm());
					ps.setString(3, relationData[i].getRelativeMiddleNm());
					DBUtil.setNullableInt(ps, 4, relationData[i].getRelativeSuffixNm());
					ps.setTimestamp(5, relationData[i].getRelativeBirthDt());
					ps.setInt(6, userPk.intValue());
					DBUtil.setNullableInt(ps, 7, relationData[i].getRelativePk());
					ps.addBatch();
				}
				ps.executeBatch();
			}
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
	}

	/**
     * Inserts Relation data for a person.
     *
	 * @param personPk Person Primary Key
	 * @param userPk User Primary Key
	 * @param relationData The Relative to be added
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Supports"
	 */
  	public void addPersonRelation(Integer personPk, Integer userPk, RelationData relationData)
   	{
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SQL_INSERT_PERSON_RELATION);
			ps.setInt(1, personPk.intValue());
			DBUtil.setNullableInt(ps, 2, relationData.getPersonRelativeType());
			ps.setString(3, relationData.getRelativeFirstNm());
			ps.setString(4, relationData.getRelativeLastNm());
			ps.setString(5, relationData.getRelativeMiddleNm());
			DBUtil.setNullableInt(ps, 6, relationData.getRelativeSuffixNm());
			ps.setTimestamp(7, relationData.getRelativeBirthDt());
			ps.setInt(8, userPk.intValue());
			ps.setInt(9, userPk.intValue());
			ps.execute();
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
    }

    /**
	 * Deletes Relation data for a person.
	 *
     * @param personPk Person Relative Primary Key
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Supports"
	 */
  	public void deletePersonRelation(Integer personRelativePk)
   	{
		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SQL_DELETE_PERSON_RELATION);
			ps.setInt(1, personRelativePk.intValue());
			ps.execute();
		}  catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
   	}

    /**
     * @J2EE_METHOD  --  updatePersonAddress
     * Updates an existing Address for a person.
     *
     * @param personPK Person Primary Key
     * @param personAddressData new Address Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonAddress    (Integer personPK, PersonAddress personAddressData)
    {

    }

    /**
     * @J2EE_METHOD  --  updatePersonDetail
     * Updates the Person data.
     *
     * @param personData new Person Data
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonDetail    (Integer userPk, PersonData personData)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String oldSsn = null;

        //updates PersonData to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_SSN);
            ps.setInt(1, personData.getPersonPk().intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    oldSsn = rs.getString("ssn");
                } while (rs.next());
            } else
                throw new EJBException("SQL_SELECT_SSN failed to execute correctly");

            ps = con.prepareStatement(SQL_UPDATE_PERSON);
            DBUtil.setNullableInt(ps, 1, personData.getPrefixNm());
            ps.setString(2, personData.getFirstNm());
            ps.setString(3, personData.getMiddleNm());
            ps.setString(4, personData.getLastNm());
            DBUtil.setNullableInt(ps, 5, personData.getSuffixNm());
            ps.setString(6, personData.getNickNm());
            ps.setString(7, personData.getAltMailingNm());
            ps.setString(8, personData.getSsn());
            DBUtil.setNullableBooleanAsShort(ps, 9, personData.getSsnValid());
            DBUtil.setNullableBooleanAsShort(ps, 10, new Boolean(isDuplicateSSN(personData.getSsn())));
            DBUtil.setBooleanAsShort(ps, 11, personData.getMarkedForDeletionFg().booleanValue());
            ps.setInt(12, userPk.intValue());
            ps.setInt(13, personData.getPersonPk().intValue());
            ps.execute();

logger.debug("----------------------------------------------------------------------");
logger.debug("MaintainPersonsBean:updatePersonDetail comment="+ personData.getTheCommentData());
logger.debug("----------------------------------------------------------------------");
            if (personData.getTheCommentData() != null && !TextUtil.isEmptyOrSpaces(personData.getTheCommentData().getComment()))
                addPersonComment(userPk, new Integer(personData.getPersonPk().intValue()), personData.getTheCommentData());

            //update any duplicate SSN flags
            updateDuplicateSsn(personData.getSsn(), oldSsn);

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }

    /**
     * Inserts a comment for a Person.
     *
     * @param userPk User Primary Key
     * @param personPk Person Primary Key
     * @param personData new Person Data
     */
    private void addPersonComment    (Integer userPk, Integer personPk, CommentData theCommentData) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;

        if (theCommentData==null) return;
        if (!TextUtil.isEmptyOrSpaces(theCommentData.getComment())) {
            // Insert a new comment
            try {
                con = DBUtil.getConnection();
                ps = con.prepareStatement(SQL_INSERT_COMMENT);

                ps.setInt(1, personPk.intValue());
                ps.setString(2, theCommentData.getComment());
                ps.setInt(3, userPk.intValue());

                ps.execute();
            } catch (SQLException se) {
                throw new EJBException(se);
            } catch (Exception e) {
                throw new EJBException(e);
            }
            finally {
                DBUtil.cleanup(con, ps, null);
            }
        }
    }

    /**
     * @J2EE_METHOD  --  updatePersonEmail
     * Updates the email addresses for a Person.
     *
     * @param personPK Person Primary Key
     * @param emailData the Collection of EmailData
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonEmail    (Integer userPk, Integer personPk, java.util.Collection emailData)
    {
        Connection con = null;
        PreparedStatement ps = null;
        Iterator it = emailData.iterator();
        EmailData data = null;

        //Updates EmailData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_EMAIL);

            while (it.hasNext()){
                data = (EmailData) it.next();

              	updatePersonEmailBadFlag(data.getEmailBadFg(), data.getEmailPk());
                ps.setString(1, data.getPersonEmailAddr());
                ps.setInt(2, data.getEmailType().intValue());
                ps.setInt(3, userPk.intValue());
                ps.setInt(4, personPk.intValue());
                ps.setInt(5, data.getEmailPk().intValue());

                ps.execute();
     	    }
        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
	 * Updates the email bad flag for a Person.
	 *
	 * @param isBad whether the email is bad
	 * @param emailPk the email to be updated
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Supports"
     */
    public void updatePersonEmailBadFlag(Boolean isBad, Integer emailPk)
    {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(SQL_UPDATE_EMAIL_BAD_FLAG);
			DBUtil.setBooleanAsShort(ps, 1, isBad.booleanValue());
			ps.setTimestamp(2, isBad.booleanValue() ? getServerTimestamp() : null);
			ps.setInt(3, emailPk.intValue());
			DBUtil.setBooleanAsShort(ps, 4, !isBad.booleanValue());
            ps.execute();
		} catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
	}

    /**
     * @J2EE_METHOD  --  updatePersonPhone
     * Updates phone data for a person.
     *
     * @param personPK Person Primary Key
     * @param personPhoneData the PhoneData object
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updatePersonPhone    (Integer userPk, Integer personPk, PhoneData personPhoneData)
    {
        Connection con = null;
        PreparedStatement ps = null;

        //updates PhoneData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PHONE);

            updatePersonPhoneBadFlag(personPhoneData.getPhoneBadFlag(), personPhoneData.getPhonePk());

            ps.setString(1, personPhoneData.getCountryCode());
            ps.setString(2, personPhoneData.getAreaCode());
            ps.setString(3, personPhoneData.getPhoneNumber());
            ps.setString(4, personPhoneData.getPhoneExtension());
            ps.setInt(5, personPhoneData.getDept().intValue());
            ps.setInt(6, personPhoneData.getPhoneType().intValue());
            DBUtil.setNullableBooleanAsShort(ps, 7, personPhoneData.getPhonePrmryFg());
            DBUtil.setNullableBooleanAsShort(ps, 8, personPhoneData.getPhonePrivateFg());
            DBUtil.setNullableBooleanAsShort(ps, 9, personPhoneData.getPhoneDoNotCallFg());
            ps.setInt(10, userPk.intValue());
            ps.setInt(11, personPk.intValue());
            ps.setInt(12, personPhoneData.getPhonePk().intValue());
            ps.execute();

            // reset any old primary dept phones if updating to be a primary phone
            if ((personPhoneData.getPhonePrmryFg() != null) &&
                (personPhoneData.getPhonePrmryFg().booleanValue() == true)) {

                resetPersonPhonePrimary(userPk, personPhoneData.getDept(), personPk,
                                        personPhoneData.getPhonePk());
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }

	/**
	 * Updates the phone bad flag for a Person.
	 *
	 * @param isBad whether the phone is bad
	 * @param phonePk the phone to be updated
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Supports"
     */
    public void updatePersonPhoneBadFlag(Boolean isBad, Integer phonePk)
	{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(SQL_UPDATE_PHONE_BAD_FLAG);
			DBUtil.setBooleanAsShort(ps, 1, isBad.booleanValue());
			ps.setTimestamp(2, isBad.booleanValue() ? getServerTimestamp() : null);
			ps.setInt(3, phonePk.intValue());
			DBUtil.setBooleanAsShort(ps, 4, !isBad.booleanValue());
	        ps.execute();
		} catch (SQLException se) {
		      throw new EJBException(se);
	    } catch (Exception e) {
	          throw new EJBException(e);
	    } finally {
		    DBUtil.cleanup(con, ps, null);
	    }
	}

    /**
     * @J2EE_METHOD  --  resetPersonPhonePrimary
     * Resets (turns off) the Primary Flag phone data for a person.
     *
     * @param userPk User Primary Key
     * @param dept User Department
     * @param personPk Person Primary Key
     * @param phonePk Phone Primary Key
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void resetPersonPhonePrimary    (Integer userPk, Integer dept, Integer personPk, Integer phonePk)
    {
        Connection con = null;
        PreparedStatement ps = null;

        if (phonePk == null) {
            phonePk = new Integer(0);
        }

        //updates Primary phone numbers in the user's department
        //that are not the phonePk to not-Primary
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PHONE_PRIMARY);

            ps.setInt(1, userPk.intValue());
            ps.setInt(2, personPk.intValue());
            ps.setInt(3, phonePk.intValue());
            ps.setInt(4, dept.intValue());
            ps.execute();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Updates political data for a person.
     *
     * @param personPk Person Primary Key
     * @param userPk the pk of the user performing the update
     * @param dept the department of the user performing the update
     * @param politicalData the PoliticalData object
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void updatePoliticalData(Integer personPk, Integer userPk, Integer dept, PoliticalData politicalData)
    {

		Connection conn = DBUtil.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SQL_UPDATE_PERSON_POLITICAL_LEGISLATIVE);

			// set Political Do Not Call Flag to true and set all Phone Do Not Call Flags for all Person Phones to true
			// if Political Objector Flag is true
			if(politicalData.getPoliticalObjectorFg().booleanValue()) {
				politicalData.setPoliticalDoNotCallFg(new Boolean(true));
				Collection phones = getPersonPhones(personPk, dept);
				if(phones != null) {
					Iterator i = phones.iterator();
					while(i.hasNext()) {
						PhoneData pd = (PhoneData)i.next();
						if(!pd.getPhoneDoNotCallFg().booleanValue()) {
							pd.setPhoneDoNotCallFg(new Boolean(true));
							updatePersonPhone(userPk, personPk, pd);
						}
					}
				}
			}
			// Alternatively if the Political Do Not Call Flag is true and Political Objector Flag is false, set
			// Phone Do Not Call Flags to true for a person's primary phone
			else if(politicalData.getPoliticalDoNotCallFg().booleanValue()) {
				Collection phones = getPersonPhones(personPk, dept);
				if(phones != null) {
					Iterator i = phones.iterator();
					while(i.hasNext()) {
						PhoneData pd = (PhoneData)i.next();
						if(pd.getPhonePrmryFg() != null && pd.getPhonePrmryFg().booleanValue() && !pd.getPhoneDoNotCallFg().booleanValue()) {
							pd.setPhoneDoNotCallFg(new Boolean(true));
							updatePersonPhone(userPk, personPk, pd);
						}
					}
				}
			}
			DBUtil.setBooleanAsShort(ps, 1, politicalData.getPoliticalObjectorFg().booleanValue());
			DBUtil.setBooleanAsShort(ps, 2, politicalData.getPoliticalDoNotCallFg().booleanValue());
			ps.setInt(3, userPk.intValue());
			ps.setInt(4, personPk.intValue());
			ps.execute();
		} catch(SQLException e) {
			throw new EJBException(e);
		} finally {
			DBUtil.cleanup(conn, ps, null);
		}
    }

    /**
     * Updates political data of political party and political registered voter for a person.
     * This is used by the Apply Update process since this is the only data in the file.
     *
     * @param personPk Person Primary Key
     * @param userPk the pk of the user performing the update
     * @param partyPk the pk of the political party
     * @param voterPk the pk of the political registered voter value
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void updatePoliticalDataPartyVoter(Integer personPk, Integer userPk, Integer partyPk, Integer voterPk)
    {
        Connection conn = DBUtil.getConnection();
	PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(SQL_UPDATE_PERSON_POLITICAL_LEGISLATIVE_PARTY_VOTER);
            DBUtil.setNullableInt(ps, 1, partyPk);
            DBUtil.setNullableInt(ps, 2, voterPk);
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, personPk.intValue());
            ps.execute();
	}  catch(SQLException e) {
            throw new EJBException(e);
	} finally {
            DBUtil.cleanup(conn, ps, null);
	}
    }

    /**
     * @J2EE_METHOD  --  verifyPerson
     * Verifies the existence of a Person matching the data entered.
     *
     * If the method indicates a duplicate person match or duplicate SSN match, control is
     *  assumed by the presentation layer which then must make the individual calls required
     *  for the next step in the process.
     *
     * @param personCriteria search criteria
     * @return 1 if no Person match and no duplicate SSN was found. 2 if a duplicate
     * person was found. 3 if a duplicate SSN was found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int verifyPerson    (PersonCriteria personCriteria)
    {
        int rtn = 1;
        /* Call isExistingPerson */
        if(isExistingPerson(personCriteria)) {
            /* Return TRUE then Return 2 */
            rtn = 2;
        }
        /* Return FALSE then call isDuplicateSSN */
        else if(isDuplicateSSN(personCriteria.getSsn())) {
            /* Return TRUE then Return 3 */
            rtn = 3;
        }
        /* Return FALSE then Return 1 */

        return rtn;
    }

    /**
     * @J2EE_METHOD  --  getCommentHistory
     * Retrieves the comment history for a Person.
     *
     * @param personPk Person Primary Key
     * @return the Collection of CommentData objects, NULL if nothing is found.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getCommentHistory    (Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        CommentData data = null;
        RecordData rdata = null;
        ResultSet rs = null;
        Collection list = new ArrayList();

        //gets CommentData from the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COMMENT_HISTORY);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    data = new CommentData();
                    rdata = new RecordData();
                    data.setComment(rs.getString("comment_txt"));
                    data.setCommentDt(rs.getTimestamp("comment_dt"));

                    rdata.setCreatedBy(new Integer (rs.getInt("created_user_pk")));
                    rdata.setCreatedDate(rs.getTimestamp("comment_dt"));
                    data.setRecordData(rdata);
                    list.add(data);
                } while (rs.next());

            } else
                return null;

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }

    /**
     * @J2EE_METHOD  --  addPersonEmail
     * Gets the Email Addresses for a Person.
     *
     * @param userPk User Primary Key
     * @param personPk Person Primary Key
     * @param emailData new Email Data
     */
    private void addPersonEmail    (Integer userPk, Integer personPk, java.util.Collection emailData) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        EmailData data = null;

        //inserts EmailData to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_EMAIL);

            if (CollectionUtil.isEmpty(emailData)) {
                // Create stubs for Person Emails
                ps = con.prepareStatement(SQL_INSERT_EMAIL);
                ps.setInt(1, personPk.intValue());
                ps.setString(2, "");
                ps.setInt(3, EMAIL_TYPE_PRIMARY.intValue());
                ps.setInt(4, userPk.intValue());
                ps.setInt(5, userPk.intValue());
                ps.execute();

                ps.setInt(3, EMAIL_TYPE_ALTERNATE.intValue());
                ps.execute();
            } else {
                // insert Person Emails
                Iterator it = emailData.iterator();
                while (it.hasNext()){
                    data = (EmailData) it.next();

                    ps.setInt(1, personPk.intValue());
                    ps.setString(2, data.getPersonEmailAddr());
                    ps.setInt(3, data.getEmailType().intValue());
                    ps.setInt(4, userPk.intValue());
                    ps.setInt(5, userPk.intValue());

                    ps.execute();
                }
            }

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * @J2EE_METHOD  --  deletePersonEmail
     * Clears an Email for a Person.
     *
     * @param userPk User Primary Key
     * @param personPk Person Primary Key
     * @param emailPk Email Primary Key
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void deletePersonEmail    (Integer userPk, Integer personPk, Integer emailPk) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Integer emailPrimaryPk = null;
        Integer emailPrimaryType = null;
        Integer emailAlternatePk = null;
        String emailAlternateAddr = null;
        Integer emailAlternateType = null;

        Collection emailData = null;
        Iterator it = null;
        EmailData data = null;

        //Emails are not deleted instead they are "blanked out"

        try {
            emailData = getPersonEmails(personPk);
            con = DBUtil.getConnection();
            it = emailData.iterator();

            while (it.hasNext()){
                  data = (EmailData) it.next();
                  if (data.getIsPrimary().booleanValue()) {   //get the Primary info
                    emailPrimaryPk = data.getEmailPk();
                    emailPrimaryType = data.getEmailType();
logger.debug("----------------------------------------------------------------------");
logger.debug("deletePersonEmail emailPrimaryPk: "+ emailPrimaryPk);
logger.debug("deletePersonEmail emailPrimaryType: "+ emailPrimaryType);
logger.debug("----------------------------------------------------------------------");
                } else {                                        //get the Alternate info
                    emailAlternatePk = data.getEmailPk();
                    emailAlternateAddr = data.getPersonEmailAddr();
                    emailAlternateType = data.getEmailType();
logger.debug("----------------------------------------------------------------------");
logger.debug("deletePersonEmail emailAlternatePk: "+ emailAlternatePk);
logger.debug("deletePersonEmail emailAlternateAddr: "+ emailAlternateAddr);
logger.debug("deletePersonEmail emailAlternateType: "+ emailAlternateType);
logger.debug("----------------------------------------------------------------------");
                }
            }
            //if the Primary is to be deleted and the Alternate contains a value,
            //then set the Alternate as the Primary
            if (emailPrimaryPk.equals(emailPk) && (emailAlternateAddr!=null)) {  //Delete the Primary email record?
                    // update primary
                    ps = con.prepareStatement(SQL_UPDATE_EMAIL_TYPE);
                    ps.setInt(1, emailAlternateType.intValue());
                    ps.setInt(2, userPk.intValue());
                    ps.setInt(3, emailPk.intValue());
                    ps.execute();
                    // update alternate
                    ps = con.prepareStatement(SQL_UPDATE_EMAIL_TYPE);
                    ps.setInt(1, emailPrimaryType.intValue());
                    ps.setInt(2, userPk.intValue());
                    ps.setInt(3, emailAlternatePk.intValue());
                    ps.execute();
logger.debug("----------------------------------------------------------------------");
logger.debug("deletePersonEmail SQL_UPDATE_EMAIL_TYPE");
logger.debug("----------------------------------------------------------------------");
                }
            //continue with delete of Email Address
            ps = con.prepareStatement(SQL_DELETE_EMAIL);

            ps.setString(1, null);              //Person Email Addr
            ps.setInt(2, 0);                    //Email Bad Fg
            ps.setTimestamp(3, null);           //Email Marked Bad Date
            ps.setInt(4, userPk.intValue());    //Last Modified User Pk
            ps.setInt(5, emailPk.intValue());   //Email Pk

            ps.execute();

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * @J2EE_METHOD  --  deletePhoneNumber
     * Deletes a phone number for a Person.
     *
     * @param userPk User Primary Key
     * @param personPk Person Primary Key
     * @param phonePk Phone Number Primary Key
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void deletePhoneNumber    (Integer userPk, Integer personPk, Integer phonePk) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

logger.debug("----------------------------------------------------------------------");
logger.debug("deletePhoneNumber: phonePk="+phonePk);
logger.debug("----------------------------------------------------------------------");

        try {
            con = DBUtil.getConnection();

            //Delete of Phone Number
            ps = con.prepareStatement(SQL_DELETE_PHONE);
            ps.setInt(1, phonePk.intValue());   //phone Pk
            ps.execute();

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }


    /**
     * @J2EE_METHOD  --  getPersona
     * Gets the personas for a Person.
     *
     * @param personPk Person Primary Key
     *
     * @return Collection of Personas
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Collection getPersona (Integer personPk) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection list = new ArrayList();
        String persona;

        //  Gets personas for a Person
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNT_AFSCME_STAFF);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if((rs.getInt("persona")) > 0) {
                        persona = Persona.AFSCME_STAFF;
                        list.add(persona);
                    }
                } while (rs.next());
            }

            ps.close();
            ps = null;
            rs.close();
            rs = null;

            ps = con.prepareStatement(SQL_SELECT_COUNT_AFFILIATE_STAFF);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if((rs.getInt("persona")) > 0) {
                        persona = Persona.AFFILIATE_STAFF;
                        list.add(persona);
                    }
                } while (rs.next());
            }

            ps.close();
            ps = null;
            rs.close();
            rs = null;

            ps = con.prepareStatement(SQL_SELECT_COUNT_MEMBER);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if((rs.getInt("persona")) > 0) {
                        persona = Persona.MEMBER;
                        list.add(persona);
                    }
                } while (rs.next());
            }

            ps.close();
            ps = null;
            rs.close();
            rs = null;

            ps = con.prepareStatement(SQL_SELECT_COUNT_ORG_ASSOCIATE);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if((rs.getInt("persona")) > 0) {
                        persona = Persona.ORGANIZATION_ASSOCIATE;
                        list.add(persona);
                    }
                } while (rs.next());
            }

            ps.close();
            ps = null;
            rs.close();
            rs = null;

            ps = con.prepareStatement(SQL_SELECT_COUNT_VENDOR);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if((rs.getInt("persona")) > 0) {
                        persona = Persona.VENDOR;
                        list.add(persona);
                    }
                } while (rs.next());
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return list;
    }

    /**
     * @J2EE_METHOD  --  isPersona
     * Test if a Person is a specific Persona.
     *
     * @param personPk Person Primary Key
     * @param persona Persona to test for
     *
     * @return boolean if the Person has a specific Persona
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean isPersona (Integer personPk, String persona) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean persona_fg = false;

        //  Test persona for a Person
        try {
            con = DBUtil.getConnection();
            if (persona.equals(Persona.AFSCME_STAFF)) {
                ps = con.prepareStatement(SQL_SELECT_COUNT_AFSCME_STAFF);
            }else if (persona.equals(Persona.AFFILIATE_STAFF)) {
                ps = con.prepareStatement(SQL_SELECT_COUNT_AFFILIATE_STAFF);
            }else if (persona.equals(Persona.MEMBER)) {
                ps = con.prepareStatement(SQL_SELECT_COUNT_MEMBER);
            }else if (persona.equals(Persona.ORGANIZATION_ASSOCIATE)) {
                ps = con.prepareStatement(SQL_SELECT_COUNT_ORG_ASSOCIATE);
            }else if (persona.equals(Persona.VENDOR)) {
                ps = con.prepareStatement(SQL_SELECT_COUNT_VENDOR);
            }

            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if((rs.getInt("persona")) > 0) {
                        persona_fg = true;
                    }
                } while (rs.next());
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return persona_fg;
    }

    /**
     * Returns the Current Timestamp on the server
     *
     * @return Timestamp current timestamp on the server
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Timestamp getServerTimestamp()
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp serverDate = null;

        // Gets the person primary key
        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_SERVER_DATE);

            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    serverDate = rs.getTimestamp("ServerDate");
                    } while (rs.next());
            }

       } catch (SQLException e) {
            throw new EJBException(e);
       } finally {
            DBUtil.cleanup(con, ps, null);
       }
        return serverDate;
    }

    /**
     * @J2EE_METHOD  --  updateDuplicateSsn
     * Updates the duplicate SSN flag for those with the same SSN.
     *
     * @param ssn to be used for the comparison
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updateDuplicateSsn    (String newSsn, String oldSsn)
    {
        Connection con = null;
        PreparedStatement ps = null;
        boolean dupFg = true;
        String ssn = "";
        if (newSsn != null)
            ssn = newSsn.trim();

        //if oldSsn is NOT null
        if (oldSsn != null && !TextUtil.isEmpty(oldSsn))
	{
		// editing the person info; and ssn has been changed
		if (newSsn != null && newSsn.trim().length() != 0)
		{
			if (!newSsn.equals(oldSsn))
			{
				if (!isDuplicateSSNGreaterThan1(newSsn))
				{
                                    dupFg = false;
				}
			}else
			{

                            if (!isDuplicateSSNGreaterThan1(oldSsn))
                            {
                		dupFg = false;
                            }
			}
		}else{
                    dupFg = false;
		}

        }else // adding a new ssn
	{
		if (newSsn != null && !TextUtil.isEmpty(newSsn))
		{
            	if (!isDuplicateSSNGreaterThan1(newSsn))
				{
                	dupFg = false;
				}
			}

		}

		if ((newSsn == null || newSsn.trim().length() == 0) && (oldSsn == null || oldSsn.trim().length() == 0))
		{
			dupFg = false;
		}

        //Updates the duplicate SSN flag for those with the same SSN
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_DUPLICATE_SSN_FG);
            DBUtil.setNullableBooleanAsShort(ps, 1, new Boolean(dupFg));
            ps.setString(2, ssn);
            ps.execute();

        } catch (SQLException se) {
            throw new EJBException(se);
        } catch (Exception e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * @J2EE_METHOD  --  serch for person base on personPk
     * Used by the OfficerFileProcessor.java to do match officer
     * @param personPk to be used
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean getPersonBaseOnPk(int personPk){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(this.SQL_SELECT_SSN);
            ps.setInt(1, personPk);
            rs = ps.executeQuery();
            if ( rs.next()) {
                success = true;
            }else
            {
                success = false;
            }

       } catch (SQLException e) {
            throw new EJBException(e);
       } finally {
            DBUtil.cleanup(con, ps, null);
       }
        return success;
    }

    /**
     * @J2EE_METHOD  --  serch for person base on ssn, last name, first name
     * Used by the OfficerFileProcessor.java to do match officer
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getPersonBaseOnSsnFnmLnm(String ssn, String firstName, String lastName){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int personPk = 0;

        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_PERSON_ON_SSN_FNAME_LNAME);

            ps.setString(1, ssn);
            ps.setString(2, firstName);
            ps.setString(3, lastName);

            rs = ps.executeQuery();
            if ( rs.next()) {
                personPk = rs.getInt(1);
            }

       } catch (SQLException e) {
            throw new EJBException(e);
       } finally {
            DBUtil.cleanup(con, ps, null);
       }
        return personPk;
    }

}
