package org.afscme.enterprise.member.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;

import java.util.GregorianCalendar;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Calendar;

import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.afscme.enterprise.common.ejb.*;
import org.afscme.enterprise.member.*;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.person.EmailData;

import org.afscme.enterprise.person.ejb.MaintainPersons;

import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.AffiliateErrorCodes;
import org.afscme.enterprise.affiliate.AffiliateData;

import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.PersonAddressRecord;

import org.afscme.enterprise.organization.ejb.MaintainOrgLocations;
import org.afscme.enterprise.organization.LocationData;

import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.codes.Codes;

import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.TextUtil;

import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;

import org.apache.log4j.Logger;


/**
 * Encapsulates operations against member data
 *
 * @ejb:bean name="MaintainMembers" display-name="MaintainMembers"
 * jndi-name="MaintainMembers"
 * type="Stateless" view-type="local"
 *
 */
public class MaintainMembersBean extends SessionBase {
    /**
     * Attributes declaration
     */

    private static Logger log = Logger.getLogger(MaintainMembersBean.class);

    /**
     * variables to hold reference to ejb
     */
    protected MaintainPersons m_persons;
    protected MaintainAffiliates m_affiliates;
    protected SystemAddress m_systemAddress;
    protected MaintainOrgLocations m_orgLocations;


    /** SQL String for updating participation data */
    private static String SQL_UPDATE_PARTICIPATION =
    "UPDATE Member_Participation set particip_detail_pk = ?, particip_outcome_pk = ?, "
    + "mbr_particip_dt = ?, comment_txt = ?, lst_mod_user_pk = ?, lst_mod_dt = GetDate() "
    + "where person_pk = ? and particip_detail_pk = ?";

    private static String SQL_INSERT_PARTICIPATION =
    "INSERT into Member_Participation (particip_detail_pk, person_pk, particip_outcome_pk, mbr_particip_dt, "
    + "comment_txt, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) "
    + "values (?,?,?,?,?,?,GetDate(),?,GetDate())" ;

    /** SQL String for deleting participation data */
    private static String SQL_DELETE_PARTICIPATION =
    "Delete from Member_Participation where person_pk = ? and particip_detail_pk = ?";

    /** SQL String. Gets the details of a patricipation for a member */
    private static String SQL_GET_PARTICIPATION_DTL =
    "Select x.particip_detail_pk, x.mbr_particip_dt, x.person_pk, x.comment_txt, x.created_user_pk, x.created_dt, "
    + "x.lst_mod_dt, x.lst_mod_user_pk, z.particip_detail_nm, z.particip_detail_pk, z.particip_detail_shortcut, "
    + "y.particip_outcome_nm, a.particip_type_nm, b.particip_group_nm, b.particip_group_pk, a.particip_type_pk, y.particip_outcome_pk "
    + "from Member_Participation x, Participation_Cd_Outcomes y, Participation_Cd_Dtl z, "
    + "Participation_Cd_Type a, Participation_Cd_Group b "
    + "where x.particip_detail_pk = z.particip_detail_pk "
    + "and x.particip_outcome_pk = y.particip_outcome_pk "
    + "and z.particip_type_pk = a.particip_type_pk "
    + "and a.particip_group_pk = b.particip_group_pk "
    + "and x.person_pk = ? and x.particip_detail_pk = ?";

    /** SQL String. Selects all the participation rows for a member    */
    private static String SQL_GET_PARTICIPATION_RSLT =
    "Select x.particip_detail_pk, x.mbr_particip_dt, x.person_pk, x.comment_txt, x.created_user_pk, x.created_dt, "
    + "x.lst_mod_dt, x.lst_mod_user_pk, z.particip_detail_nm, z.particip_detail_pk, z.particip_detail_shortcut, "
    + "y.particip_outcome_nm, a.particip_type_nm, b.particip_group_nm, b.particip_group_pk, a.particip_type_pk, y.particip_outcome_pk "
    + "from Member_Participation x, Participation_Cd_Outcomes y, Participation_Cd_Dtl z, "
    + "Participation_Cd_Type a, Participation_Cd_Group b "
    + "where x.particip_detail_pk = z.particip_detail_pk "
    + "and x.particip_outcome_pk = y.particip_outcome_pk "
    + "and z.particip_type_pk = a.particip_type_pk "
    + "and a.particip_group_pk = b.particip_group_pk "
    + "and x.person_pk = ?";

    /** Inserts a record into the weekly card run table for the member    */
    private static String SQL_ADD_MEMBER_CARD_RUN =
    "INSERT into COM_Weekly_Mbr_Card_Run (person_pk, aff_pk) values (?,?)" ;

	private static String SQL_IS_MEMBER_ALREADY_ON_CARD_RUN =
	"SELECT count(*) as mcount from COM_Weekly_Mbr_Card_Run where person_pk = ? and aff_pk = ?";

	/** Inserts a record into the weekly card run table for the member  - - not done yet  */
    private static String SQL_ADD_MEMBER_CARD_RUN_FOR_ALL_MBRS_AFFS =
    "INSERT into COM_Weekly_Mbr_Card_Run (person_pk, aff_pk) values (?,?)" ;

    /** Insert basic member (affiliate) data for an existing person */
    private static String SQL_ADD_MEMBER =
    "INSERT INTO Aff_Members(aff_pk, person_pk, mbr_status, mbr_type,  "
    + "mbr_join_dt, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) "
    + "VALUES(?, ?, ?, ?, ?, ?, GetDate(), ?, GetDate())";

    private static String SQL_UPDATE_PERSON_AS_MEMBER =
    "UPDATE Person set member_fg = 1 where person_pk = ?";


    /** Inserts a new comment for a person */
    private static String SQL_INSERT_COMMENT =
    "INSERT INTO Person_Comments(person_pk, comment_txt, comment_dt,  " +
    "        created_user_pk) " +
    "  VALUES(?, ?, GetDate(), ? ) ";

    /** Updates affiliate member data */
    private static String SQL_UPDATE_MEMBER_AFF_DATA =
    "UPDATE Aff_Members " +
    "set mbr_type = ?, mbr_status = ?, mbr_join_dt = ?,  lost_time_language_fg = ?, " +
    "mbr_dues_type = ?,   mbr_dues_rate = ?, mbr_dues_frequency = ?, mbr_retired_dt = ?, " +
    "mbr_ret_dues_renewal_fg = ?, no_cards_fg = ?, no_mail_fg = ?, no_public_emp_fg = ?, " +
    "no_legislative_mail_fg = ?, lst_mod_user_pk = ?, lst_mod_dt = GetDate() " +
    "where person_pk = ? and aff_pk = ? ";

    /** Selects affiliate member data for a member by person_pk */
    private static String SQL_SELECT_MEMBER_AFF_DATA =
    "SELECT mbr_type, mbr_status, mbr_join_dt, mbr_no_local, " +
    "lost_time_language_fg, primary_information_source, mbr_card_sent_dt,  " +
    "mbr_dues_type, mbr_dues_rate, mbr_dues_frequency, mbr_retired_dt, " +
    "mbr_ret_dues_renewal_fg, no_cards_fg, no_mail_fg, no_public_emp_fg, " +
    "no_legislative_mail_fg, created_user_pk, created_dt, " +
    "lst_mod_user_pk, lst_mod_dt " +
    "from Aff_Members " +
    "where person_pk = ? and aff_pk = ?";

    private static String SQL_SELECT_MEMBER_AFF_EMPLOYER_DATA =
    "SELECT employer_name, emp_job_title, emp_sector, emp_job_site, employee_salary, emp_salary_range " +
    " from Aff_Mbr_Employers where person_pk = ? and aff_pk = ?";

    private static String SQL_SELECT_COUNT_FROM_AFF_MBR_EMPLOYER =
    "SELECT count(*) as rcount from Aff_Mbr_Employers where person_pk = ? and aff_pk = ?";

    private static String SQL_INSERT_INTO_AFF_MBR_EMPLOYER =
    "INSERT into Aff_Mbr_Employers (person_pk, aff_pk, created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) values "
    + " (?,?,?,getDate(),?,getDate())";

    private static String SQL_UPDATE_AFF_MBR_EMPLOYER =
    "UPDATE Aff_Mbr_Employers set employer_name = ?, emp_job_title = ?, emp_sector = ?, " +
    " emp_job_site = ? , employee_salary = ?,  emp_salary_range = ?, " +
    " lst_mod_user_pk = ?, lst_mod_dt = getDate() where person_pk = ? and aff_pk = ?";

    private static String SQL_SELECT_MEMBER_BARRED =
    "SELECT mbr_barred_fg from Person where person_pk = ?";

    private static String SQL_SELECT_MEMBER_DETAIL_FROM_PERSON =
    "SELECT mbr_expelled_dt, mbr_barred_fg, lst_mod_user_pk, lst_mod_dt from Person " +
    "where person_pk = ?";

    private static String SQL_UPDATE_AFF_MEMBER_ACTIVITY =
    "UPDATE Aff_Mbr_Activity set membership_activity_count = (membership_activity_count + ?) " +
    "where aff_pk = ? and time_pk = ? and membership_activity_type = ?";

    // note changed to remove RPT from time dimension
    private static String SQL_GET_TIME_PK =
    "SELECT time_pk from Time_Dim where calendar_year = ? and calendar_month = ?";

    private static String SQL_UPDATE_MEMBER_DETAIL =
    "UPDATE Person set mbr_expelled_dt = ?, mbr_barred_fg = ? where person_pk = ? ";

    private static String SQL_SELECT_AFFILIATE_RESULTS =
    "SELECT Aff_Members.aff_pk, mbr_status, mbr_type, aff_status, aff_type, aff_localSubChapter, aff_stateNat_type, " +
    "aff_subUnit, aff_councilRetiree_chap, aff_abbreviated_nm  from Aff_Members " +
    "JOIN Aff_Organizations ON (Aff_Members.aff_pk = Aff_Organizations.aff_pk)" +
    "WHERE person_pk = ? ";

    private static String SQL_SELECT_AFFILIATE_RESULTS_WITHOUT_WHERE =
    "SELECT Aff_Members.aff_pk, mbr_status, mbr_type, aff_status, aff_type, aff_localSubChapter, aff_stateNat_type, " +
    "aff_subUnit, aff_councilRetiree_chap, aff_abbreviated_nm  from Aff_Members " +
    "JOIN Aff_Organizations ON (Aff_Members.aff_pk = Aff_Organizations.aff_pk)" ;

    private static String SQL_SELECT_OFFICER_INFO_FOR_MEMBER =
    "SELECT suspended_fg, pos_addr_from_person_pk, pos_addr_from_org_pk, " +
    "afscme_title_nm from Officer_History " +
    "JOIN AFSCME_Offices ON (Officer_History.afscme_office_pk = AFSCME_Offices.afscme_office_pk) "   +
    "where pos_end_dt IS NULL and person_pk = ? and aff_pk = ? and " +
    "position_mbr_affiliation = ?";

    // table name may change RPT_
    private static String SQL_SELECT_COUNT_FROM_AFF_MBR_ACTIVITY =
    "SELECT count(*) as rcount from Aff_Mbr_Activity where aff_pk = ? and time_pk = ? " +
    "and membership_activity_type = ? ";

    // table name may change RPT_
    private static String SQL_INSERT_INTO_AFF_MBR_ACTIVITY =
    "INSERT INTO Aff_Mbr_Activity(aff_pk, time_pk, membership_activity_type, " +
    "membership_activity_count) VALUES(?, ?, ?, ?)";

   /** Deprecated, because if the member does not have an SMA, Phone or Email data, will not return rows
    use correlated queries to retrieve actual values for some columns to support order by
    evaluated referencing the values in the order by but will not work because multiple joins on same column
    private static String SQL_SELECT_SEARCH_MEMBERS = "SELECT  p.prefix_nm, p.first_nm, p.middle_nm, p.last_nm, p.suffix_nm, " +
        "p.nick_nm, p.alternate_mailing_nm, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.primary_information_source = cc.com_cd_pk) as \"primary_information_source\", " +
        "p.ssn, p.valid_ssn_fg, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_type = cc.com_cd_pk) as \"mbr_type\", " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_status = cc.com_cd_pk) as \"mbr_status\", " +
        "am.mbr_card_sent_dt, am.person_pk, am.no_mail_fg, am.no_cards_fg, " +
        "am.no_legislative_mail_fg, am.no_public_emp_fg, a.aff_type, a.aff_localSubChapter, a.aff_SubUnit, " +
        "a.aff_stateNat_type, a.aff_councilRetiree_chap, " +
        "(select u.user_id from Users u where am.lst_mod_user_pk = u.person_pk) as \"updated_by\", " +
        "am.lst_mod_dt, pp.country_cd, pp.area_code, pp.phone_no. pe.person_email_addr, " +
        "pa.addr1, pa.addr2, pa.city, " +
        "(select cc.com_cd_desc from Common_Codes cc where pa.state = cc.com_cd_pk) as \"state\", " +
        "pa.zipcode, " +
        "(select u.user_id from Users u where am.lst_mod_user_pk = u.person_pk) as \"addr_updated_by\", " +
        "pa.lst_mod_dt " +
        " FROM Aff_Members am "; */

  /** changing to return concatenated name private static String SQL_SELECT_SEARCH_MEMBERS = "SELECT  p.prefix_nm, p.first_nm, p.middle_nm, p.last_nm, " +
        "p.suffix_nm, p.nick_nm, p.alternate_mailing_nm, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.primary_information_source = cc.com_cd_pk) as \"primary_information_source\", " +
        "p.ssn, p.valid_ssn_fg, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_type = cc.com_cd_pk) as \"mbr_type\", " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_status = cc.com_cd_pk) as \"mbr_status\", " +
        "am.mbr_card_sent_dt, am.person_pk, am.no_mail_fg, am.no_cards_fg, " +
        "am.no_legislative_mail_fg, am.no_public_emp_fg, a.aff_type, a.aff_localSubChapter, a.aff_SubUnit, " +
        "a.aff_stateNat_type, a.aff_councilRetiree_chap, " +
        "(select u.user_id from Users u where am.lst_mod_user_pk = u.person_pk) as \"updated_by\", " +
        "am.lst_mod_dt " +
        " FROM Aff_Members am "; */

    /* These SQL snippets are used dynamically used when searching members.*/
    private static String SQL_FULL_NAME =
    	" p.prefix_nm, p.last_nm, p.first_nm, " +
        "(COALESCE(p.last_nm, '') +  COALESCE(' ' +(select cc.com_cd_desc from Common_Codes cc where p.suffix_nm = cc.com_cd_pk), '') + ', ' + COALESCE(p.first_nm, '') + ' ' + COALESCE(p.middle_nm,' ')) as \"person_nm\"";

    private static String SQL_PRIMARY_INFORMATION_SOURCE_DESC =
    	"(select cc.com_cd_desc from Common_Codes cc where am.primary_information_source = cc.com_cd_pk) as \"primary_information_source\"";

	private static String SQL_MEMBER_TYPE_DESC =
		"(select cc.com_cd_desc from Common_Codes cc where am.mbr_type = cc.com_cd_pk) as \"mbr_type\"";

	private static String SQL_MEMBER_STATUS_DESC =
		"(select cc.com_cd_desc from Common_Codes cc where am.mbr_status = cc.com_cd_pk) as \"mbr_status\"";

	private static String SQL_USER_ID =
		"(select u.user_id from Users u where am.lst_mod_user_pk = u.person_pk) as \"updated_by\"";

	private static String SQL_ADDRESS =
		"pa.addr1, pa.addr2, pa.city, pa.state, pa.zipcode";

	private static String SQL_AFF_ID =
		"a.aff_pk, cast(a.aff_councilRetiree_chap AS int) as \"int_council\",  cast(a.aff_localSubChapter AS int) as \"int_local\", " +
        " a.aff_type, a.aff_stateNat_type, a.aff_SubUnit";

  	private static String SQL_MAIL =
        "am.no_cards_fg, " +
        "am.no_legislative_mail_fg, am.no_public_emp_fg, am.no_mail_fg";

  	private static String SQL_PHONE =
		"pp.country_cd, pp.area_code, pp.phone_no";

  	private static String SQL_EMAIL =
        "pe.person_email_addr";
  	/* End of SQL snippets*/


    /*private static String SQL_SELECT_SEARCH_MEMBERS = "SELECT DISTINCT p.prefix_nm, p.last_nm, p.first_nm, " +
       "(COALESCE(p.last_nm, '') + ' ' + COALESCE((select cc.com_cd_desc from Common_Codes cc where p.suffix_nm = cc.com_cd_pk), '') + ', ' + COALESCE(p.first_nm, '') + ' ' + COALESCE(p.middle_nm,' ')) as \"person_nm\", " +
        " p.nick_nm, p.alternate_mailing_nm, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.primary_information_source = cc.com_cd_pk) as \"primary_information_source\", " +
        "p.ssn, p.valid_ssn_fg, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_type = cc.com_cd_pk) as \"mbr_type\", " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_status = cc.com_cd_pk) as \"mbr_status\", " +
        "am.mbr_card_sent_dt, am.person_pk, am.no_mail_fg, am.no_cards_fg, " +
        "am.no_legislative_mail_fg, am.no_public_emp_fg, a.aff_pk, " +
        "(select u.user_id from Users u where am.lst_mod_user_pk = u.person_pk) as \"updated_by\", " +
        "am.lst_mod_dt, cast(a.aff_councilRetiree_chap AS int) as \"int_council\",  cast(a.aff_localSubChapter AS int) as \"int_local\", " +
        " a.aff_type, a.aff_stateNat_type, a.aff_SubUnit, " +
        " (pa.addr1 + ' ' + pa.addr2) as \"address\", pa.city, pa.state, pa.zipcode " +
        " FROM Aff_Members am ";*/


    // separated out from select to support dynamic creation of the select clause later
    private static String SQL_JOIN_SEARCH_MEMBERS =
        " JOIN Person p ON am.person_pk = p.person_pk " +
        "LEFT OUTER JOIN Aff_Organizations a ON am.aff_pk = a.aff_pk " +
        "LEFT OUTER JOIN Person_Address pa ON am.person_pk = pa.person_pk " +
        " AND address_pk IN (SELECT address_pk  " +
        "                       	FROM person_SMA " +
        "                       WHERE person_pk = am.person_pk " +
        "                         AND current_fg = 1) " +
        "LEFT OUTER JOIN Person_Email pe ON am.person_pk = pe.person_pk " +
        "LEFT OUTER JOIN Person_Phone pp ON am.person_pk = pp.person_pk ";


    private static String SQL_JOIN_SMA_SEARCH_MEMBERS =
        " INNER JOIN Person_SMA s ON pa.address_pk = s.address_pk ";

    private static String SQL_SELECT_COUNT_SEARCH_MEMBERS =
    "SELECT count (am.person_pk) from Aff_Members am ";

      /** Checks for existing duplicate Person */
    private static String SQL_SELECT_COUNT_PERSON =
          "SELECT COUNT(p.person_pk) Duplicate " +
          "  FROM Person p " +
          "  LEFT OUTER JOIN Person_Demographics d ON p.person_pk = d.person_pk " +
          "  LEFT OUTER JOIN aff_members am ON p.person_pk =  am.person_pk " ;

    /** Gets the fields for duplicate Persons */
    private static String SQL_SELECT_DUPLICATE_PERSON =
        "SELECT p.person_pk, " +
	"(COALESCE(p.last_nm, '') +  " +
	"COALESCE(' '+(SELECT cc.com_cd_desc  " +
        "		    FROM Common_Codes cc  " +
        "		   WHERE p.suffix_nm = cc.com_cd_pk), '') +   " +
	"COALESCE(', ' +p.first_nm, ', ') +   " +
	"COALESCE(' ' +p.middle_nm,' ')) as person_nm,  " +
	"(COALESCE(Substring(ssn, 1, 3)+'-'+Substring(ssn, 4, 2)+'-'+Substring(ssn, 6, 4), null)) AS ssn, p.member_fg, " +
	"(COALESCE(a.addr1 + a.addr2, a.addr1, a.addr2)) AS personAddr, " +
	"(COALESCE(a.city, null)) AS personAddrCity, " +
	"(COALESCE(a.state, null)) AS personAddrState, " +
	"(COALESCE(a.zipcode +'-'+ a.zip_plus, a.zipcode, a.zip_plus)) AS personAddrPostalCode, " +
        "ao.aff_type, cast(ao.aff_councilRetiree_chap AS int) as \"int_council\",  cast(ao.aff_localSubChapter AS int) as \"int_local\", " +
        " ao.aff_stateNat_type, ao.aff_subUnit, ao.aff_code " +
	"FROM person p " +
	"LEFT OUTER JOIN person_address a ON a.person_pk = p.person_pk " +
        "AND address_pk IN (SELECT address_pk " +
        "                       FROM person_SMA" +
	"                      WHERE person_pk = p.person_pk" +
	"                        AND current_fg = 1) " +
        "FULL JOIN Person_Demographics d ON p.person_pk = d.person_pk " +
        "LEFT OUTER JOIN aff_members am ON p.person_pk =  am.person_pk " +
        "LEFT OUTER JOIN aff_organizations ao ON am.aff_pk = ao.aff_pk  ";

    private static String SQL_INACTIVATE_MEMBER = "UPDATE Aff_Members set mbr_Status = ?, " +
    "lst_mod_dt = getDate(), lst_mod_user_pk = ? where aff_pk = ? and person_pk = ?" ;

    private static String SQL_SELECT_SEARCH_VENDOR_MEMBERS =
        "SELECT DISTINCT am.person_pk, p.last_nm, p.first_nm, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_status = cc.com_cd_pk) as \"mbr_status\", " +
        "(COALESCE(p.last_nm, '') + ' ' + COALESCE((select cc.com_cd_desc from Common_Codes cc where p.suffix_nm = cc.com_cd_pk), '') + ', ' + COALESCE(p.first_nm, '') + ' ' + COALESCE(p.middle_nm,' ')) as \"person_nm\", " +
        "a.aff_pk, a.aff_type, a.aff_stateNat_type, a.aff_SubUnit, " +
        "cast(a.aff_councilRetiree_chap AS int) as \"int_council\", cast(a.aff_localSubChapter AS int) as \"int_local\", " +
        "(COALESCE(pa.addr1, '')) + ' ' + (COALESCE(pa.addr2, '')) as \"address\", " +
        "pa.city, pa.state, pa.zipcode, pa.zip_plus, " +
        "(select cc.com_cd_desc from Common_Codes cc where am.mbr_type = cc.com_cd_pk) as \"mbr_type\" " +        
        "FROM Aff_Members am ";

    private static String SQL_JOIN_SEARCH_VENDOR_MEMBERS =
        " JOIN Person p ON am.person_pk = p.person_pk " +
        "LEFT OUTER JOIN Aff_Organizations a ON am.aff_pk = a.aff_pk " +
        "LEFT OUTER JOIN Person_Address pa ON am.person_pk = pa.person_pk " +
        "LEFT OUTER JOIN Person_SMA psma ON pa.person_pk = psma.person_pk and pa.address_pk = psma.address_pk ";

    private static String SQL_SELECT_MBR_STATUS =
        "SELECT mbr_status FROM Aff_Members WHERE person_pk=? ";

	/** Dynamic join with Aff_Organizations table */
	private static String SQL_JOIN_AFFS =
		" JOIN Aff_Organizations a ON am.aff_pk = a.aff_pk ";

	/** Dynamic join with Person_Email table */
	private static String SQL_JOIN_EMAIL =
		" JOIN Person_Email pe ON am.person_pk = pe.person_pk " +
        //"FROM Person_Email " +
        //"WHERE person_pk = am.person_pk " +
        "AND email_type = " + Codes.EmailType.PRIMARY + " ";

	/** Dynamic join with Person_Phone table */
	private static String SQL_JOIN_PHONE =
		" JOIN Person_Phone pp ON am.person_pk = pp.person_pk " +
        //"FROM Person_Phone " +
        //"WHERE person_pk = am.person_pk " +
        "AND phone_type = " + Codes.PhoneType.HOME + " ";

	/** Dynamic join with Person_Address table */
	private static String SQL_JOIN_ADDRESS =
		" JOIN Person_Address pa ON am.person_pk = pa.person_pk " +
        "AND address_pk IN (SELECT address_pk  " +
        "FROM person_SMA " +
        "WHERE person_pk = am.person_pk " +
        "AND current_fg = 1) ";

	/** Dynamic join with Person table */
	private static String SQL_JOIN_PERSON =
		"JOIN Person p ON am.person_pk = p.person_pk ";
        
        /** Get any officer position for this person that is current */
        private static String SQL_COUNT_CURRENT_OFFICE_FOR_PERSON = 
        "Select count(*) as oCount from Officer_History where person_pk = ? and pos_end_dt IS NULL";
        
     /** may not need
     *  private static String SQL_SELECT_PERSON_ADDRESS_TYPE_FOR_OFFICER =
     * "SELECT addr_type from Person_Address where address_pk = ? ";
     */

    /** may not need
     * private static String SQL_SELECT_ORG_ADDRESS_LOCATION_FOR_OFFICER =
     * "SELECT location_nm from Org_Locations JOIN Org_Address ON " +
     * "(Org_Locations.org_locations_pk = Org_Address.org_locations_pk) " +
     * "where org_addr_pk = ?";
     */

    /** ejbCreate() method call - initialize home objects to enable access to other
     * beans whose services are needed.
     */
    public void ejbCreate() throws CreateException {
        try {
            // need to get the reference to OrgLocations when it is available
            m_persons = JNDIUtil.getMaintainPersonsHome().create();
            m_affiliates = JNDIUtil.getMaintainAffiliatesHome().create();
            m_systemAddress = JNDIUtil.getSystemAddressHome().create();
            m_orgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();

        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainMembersBean.ejbCreate()" + e);
        }
    }

    /**
     *
     * Adds a new Member to the database.
     *
     * If adding from an existing Person, the personPk will have a value.
     * If not, then the first steop is to add the person
     *
     * There is a requirement to create stub records for the Person_Demographics and
     * Person_Political_legislative when a new member is created, but this really maps to
     * when a new person is created and will be performed by the MaintainPersonsBean.
     *
     * There is also a need to set the Person.member_fg when a person is created through this
     * path (see code comments)
     *
     * If addPerson completes sucessfully, NewMember object will updated with the personPk if
     *  they didn't previously exist.
     *
     * AffiliateData() actually completes the add and will enforce the associated
     * business rules and update RPT_Aff_Mbr_Activity table
     *
     * @param newMember new Member Data
     * @param Integer - userPk - userid of the person who is adding the new member 05/10/03 GRD
     * @param Integer - deptPk - identity of the department of the user adding the person
     *                  in order to add addresses & phone numbers correctly
     * @param Integer - affPk - assumably the affiliate to which the member is being added
     *
     * @return 0 if completes successful. Error Code if the add process fails by violation
     * of a business rule
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int addMember(NewMember newMember, Integer userPk, Integer deptPk, Integer affPk) {
        Integer thePersonPk = null;

        // if PersonPk is null, then a new person needs to be added
        if (newMember.getPersonPk() == null) {
            NewPerson newPerson = newMember.getTheNewPerson();
            if (newPerson != null) {

                //set that this person is being added as a new member, so that addPerson will
                //set the Person.member_fg correctly
                newPerson.setMemberFg(new Boolean(true));
                thePersonPk = m_persons.addPerson(userPk, newPerson, deptPk, affPk);
                
                // set back into newMember, as it will be passed to another method to complete
                // the association of the person and the affiliate as a member
                newMember.setPersonPk(thePersonPk);
               // log.debug("MaintainMembers.addMember after call to MaintainPersons.addPerson, "
               //     + "personPk set in newMember is: " + newMember.getPersonPk());
            }
            else {
                // log an error and return error code (personPk and theNewPerson should not both be null)
                return MemberErrorCodes.MISSING_PERSON_DATA;
            } // end if (newPerson != null)
        }
        else {
            // if person is a member who is barred, then abort the add member
            if (isMemberBarred(newMember.getPersonPk()) ) {
                return MemberErrorCodes.BARRED_MEMBER;
            }
        }      
        // use addMemberAffiliateData to add the member to the affiliate
        return(this.addMemberAffiliateData(newMember, userPk));

    }

    /**
     *
     * Supports Apply Update - Member need to inactivate members.
     *
     * @param Integer - personPk - person_pk of member to be inactivated
     * @param Integer - affPk - the affiliate in which the member is to be deactivate
     * @param Integer - mbrStatus - presumably the integer representing Inactive for a member, or temporary for a member who holds a current office and can't be inactivated
     * @param Integer - userPk - userid of the person who is inactivating the member
     *
     *
     * @return 0 if completes successful. Error Code if the inactivate process fails
     * because the member can not be found in that affiliate
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int inactivateMember(Integer personPk, Integer affPk, Integer mbrStatus, Integer userPk) {

        Connection con = null;
        PreparedStatement ps = null;


        //updates Member Affiliate data to set a member's staus as inactive or temporary
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INACTIVATE_MEMBER);

            ps.setInt(1, mbrStatus.intValue());
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, affPk.intValue());
            ps.setInt(4, personPk.intValue());

            int rt = ps.executeUpdate();
            if (rt ==1) {

                // update to inactivate member in the affiliate was successful

                return 0;

            }
            else if (rt == 0) {
                return MemberErrorCodes.MEMBER_AFFILIATION_NOT_FOUND;
            }
            else if (rt > 1) {
                return MemberErrorCodes.MULTIPLE_MEMBER_AFFILIATION_RECORDS_FOUND;
            }

        } catch (SQLException e) {
            throw new EJBException("SQL Error occurred in MaintainMembersBean.inactivateMember method ", e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return 0;
    }

    /**
     *
     * Adds an association with an Affiliate to a Member. In this case, we already know
     * that the person exists, as opposed to a call to addMember() . This method can be called
     * directly in order to support Member Detail - Add Affiliation functionality. Since either
     * an existing member or a person(but non-member) can be submitted to this method, the final
     * act of the method is to update person to set the member_fg column = 1. This saves needing
     * to know whether its a non-member or member that is being affiliated.
     *
     * However, it is not
     * known whether an affiliate primary key or an affiliate identifier has been passed.
     * Currently the code checks and sets the affPk (or returns an error if 0 or more than one
     * affPk is returned, however, due to changes in the UI functionality, it is likely that the
     * affPk will be passed. The affPk should be valid, but the validation check may be retained
     * in this method.
     *
     * * The UI developer must be aware whether a new person is being created or an
     * existing person is being used and set the comment values accordingly. If the person
     * exists, set the comment value in NewMember object. If a new person set the comment in
     * the NewPerson object. This method only deals with comments set in the NewMember object
     * for an existing person.
     *
     * When completing the "add" process, the RPT_Aff_Mbr_Activity Table  must be updated
     *
     * The following business rules need to be implemted when member affiliate data is added:
     * If the member type = "agency Fee Payer" then a)no_cards_fg and legislative_mail_fg columns
     *   should be set to true b) political_do_not_call_fg field in the Person_Political_legislative
     *   table should be set to true.
     *
     * If the member type equals 'Retiree Spouse' then the no_public_emp_fg column should be set to
     *   true
     * If the member type equals 'Union Shop Objector' then a) political_objector_fg and
     *   political_do_not_call_fg in Person_Political_Legislative needs to be set to true
     *   b)phone_do_not_call_fg field in the Person_Phone table should be set to true for all
     *     phone numbers
     *
     *
     * @param NewMember newMember - Person or Member to be associated
     * @param Integer userPk - Person making the Member association
     * @return 0 if association could be made and negative return codes for different errors
     * based on violation of a business rule
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int addMemberAffiliateData(org.afscme.enterprise.member.NewMember newMember, Integer userPk) {
        Integer theAffPk = null;

        // check to see if an affPk was provided, if not, then the affPk needs to be
        // determined first

        if(newMember.getAffPk() == null) {
            // find affPk. if affPk can not be determined or there is more than one
            // returned, then return an error code indicating which

            theAffPk = m_affiliates.getAffiliatePk(newMember.getTheAffiliateIdentifier());
            log.debug("maintainMembers.addMemberAffiliateData, affPk null from form, getAffiliatePk returned: " + theAffPk );
            if (theAffPk == null) {

                return MemberErrorCodes.AFFILIATE_NOT_FOUND;
            }
            else {
                // check for duplicates return code
                if (theAffPk.intValue() == AffiliateErrorCodes.ERROR_GET_PK_MORE_THAN_ONE_RESULT) {
                    return AffiliateErrorCodes.ERROR_GET_PK_MORE_THAN_ONE_RESULT;
                }
            } // end if (theAffPk == null)

        } // end if (newMember.getAffPk() == null)
        else {
            // else the affPk was provided, set it to theAffPk for consistency
            theAffPk = newMember.getAffPk();
        }
        /*
         * if the affiliate does not have a status of either Chartered, Not Chartered,
         *  Restricted Administratorship, Unrestricted Administratorship, then the Member
         *  can not be added, so return an error
         */
        //       if (! m_affiliates.(isGoodAffiliate(theAffPK))) {
        //            return AFFILIATE_NO_GOOD_FOR_ADD_MEMBER;
        //        }

        /* now verify that if member type if 'Retiree' or 'Retiree Spouse' that
         *   affiliate type is 'R' or 'S'  For now, we assume that the affiliate identifier is
         *   OK and present, even if the affiliate finder was used to set the AffPk (question there)
         */
        if ((newMember.getMbrType() == Codes.MemberType.S) || (newMember.getMbrType() == Codes.MemberType.T)) {
             if (!((newMember.getTheAffiliateIdentifier().getType().toString().equals("S")) ||
                 (newMember.getTheAffiliateIdentifier().getType().toString().equals("R"))) ) {

                return MemberErrorCodes.MEMBER_TYPE_CONFLICT_WITH_AFF_TYPE;
            }


        }

        // OK finally we are ready to add the member  (yea !)
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_ADD_MEMBER);

            log.debug("maintainMembers.addMemberAffiliateData on insert, SQL params are: personPk: "
                + newMember.getPersonPk().intValue() + "mbrJoinDt: " + newMember.getMbrJoinDt()
                + " AffPk: " + theAffPk.intValue() + " mbrStatus: " + newMember.getMbrStatus()
                + " mbrType: " + newMember.getMbrType() + " userPk: " + userPk.intValue());
            ps.setInt(2, newMember.getPersonPk().intValue());
            ps.setInt(1, theAffPk.intValue());
            ps.setInt(3, newMember.getMbrStatus().intValue());
            ps.setInt(4, newMember.getMbrType().intValue());
            ps.setTimestamp(5, newMember.getMbrJoinDt());
            // created_user_pk
            ps.setInt(6, userPk.intValue());
            // created_dt - set using SQL Server GetDate() function
            // lst_mod_user_pk
            ps.setInt(7, userPk.intValue());
            // lst_mod_dt - set using SQL Server GetDate() function


            int rt = ps.executeUpdate();

           // if the person already existed, and a non blank comment was added. . .
            if ((newMember.getExistingPersonComment() != null) && (newMember.getExistingPersonComment().length() > 0)) {
                ps3 = con.prepareStatement(SQL_INSERT_COMMENT);
                ps3.setInt(1, newMember.getPersonPk().intValue());
                ps3.setString(2, newMember.getExistingPersonComment());
                ps3.setInt(3, userPk.intValue());
                ps3.executeUpdate();
            }

            /* now that the other inserts and updates have been done, set the member_fg in person
             * to 1 (true) in case the person being associated was not previously a member
             */
            ps2 = con.prepareStatement(SQL_UPDATE_PERSON_AS_MEMBER);
            ps2.setInt(1, newMember.getPersonPk().intValue());
            ps2.executeUpdate();

            // don't forget to add the new member to the weekly card run
            // (if it passes business rules // yet to be redefined . . .
            this.addToWeeklyCardRun(newMember.getPersonPk(), theAffPk, newMember);

            // update RPT_Aff_MBr_Activity with the add activity for this affiliate
            this.updateAffMbrActivity(theAffPk, Codes.ActivityType.A, 1);

        }


        catch (SQLException e) {
            throw new EJBException
            ("SQL Error Occurred in MaintainMembersBean.addMemberAffiliateData : " + e);
        }

        finally {           
            DBUtil.cleanup(null, ps2, null);         
            DBUtil.cleanup(null, ps3,  null);        
            DBUtil.cleanup(con,  ps, null); 
        }

        return 0; // the documented successful return code for this method


    } //addMemberAffiliateData

    /**
     *
     * Supports application of business rules which involve knowing whether
     * a person is barred from membership
     *
     * @param Integer : personPK Person Primary Key
     * @return boolean : boolean value of Person.mbr_barred_fg column
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

    public boolean isMemberBarred(Integer personPk) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean memberBarred = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_BARRED);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();


            if (rs.next()) {
            /** note mbr barred flag is not null in database, should always retrieve
             *   a value that equates to true or false from this column , therefore there is no
             *   checking for null values when pulled from the resultset*/

               memberBarred = DBUtil.getBooleanFromShort(rs.getShort("mbr_barred_fg"));
            }
            else {
                throw new EJBException("Error Occurred in MaintainMembersBean.isMemberBarred method - row was not returned for personPk = " + personPk);
            }
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.isMemberBarred method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return(memberBarred.booleanValue());


    } // isMemberBarred


    /**
     *
     * Determines whether a row exists in (RPT) Aff_Mbr_Activity for a given
     * affPk, timePk and activityType. If the row does not exist, inserts a row
     * into the table with an initial _count of 0
     *
     * This code could be rolled into a stored procedure, as it makes two calls to the
     * database without requiring input from the user
     *
     * @param Integer : affPk Affiliate Primary Key
     * @param Integer : timePk Affiliate Primary Key
     * @param Integer : activityType Common code Key value for the activity type in question
     * @return void
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void insertRowIntoAffMbrActivityIfNeeded(Integer affPk, Integer timePk, Integer activityType) {

        int initialNum = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;


        // see if the row exists for this aff, timePk and activityType in Aff_Mbr_Activity
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNT_FROM_AFF_MBR_ACTIVITY);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, timePk.intValue());
            ps.setInt(3, activityType.intValue());
            rs = ps.executeQuery();
            rs.next(); // select count(*) will return only one row

            if (rs.getInt("rcount") == 0) { // if no row exists for the aff, time and activity
                ps2 = con.prepareStatement(SQL_INSERT_INTO_AFF_MBR_ACTIVITY);
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, timePk.intValue());
                ps2.setInt(3, activityType.intValue());
                ps2.setInt(4, initialNum);
                ps2.executeUpdate();

            }
        }

        catch (SQLException e) {
            throw new EJBException
            ("SQL Error Occurred in MaintainMembersBean.insertRowIntoAffMbrActivityIfNeeded method" + e);
        }

        finally {
            DBUtil.cleanup(null, ps2, null);
            DBUtil.cleanup(con, ps, rs);

        }



    } // insertRowIntoAffMbrActivityIfNeeded

    /**
     *
     * Determines whether a row exists in  Aff_Mbr_Employer for a given
     * affPk & personPk . If the row does not exist, inserts a row
     * into the table with initial data
     *
     * @param Integer : personPk Person Primary Key
     * @param Integer : affPk Affiliate Primary Key
     * @return void
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void insertRowIntoAffMbrEmployerIfNeeded(Integer personPk, Integer affPk, Integer userPk) {

        int initialNum = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;


        // see if the row exists for this aff, timePk and activityType in Aff_Mbr_Activity
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNT_FROM_AFF_MBR_EMPLOYER);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            rs = ps.executeQuery();
            rs.next(); // select count(*) will return only one row

            if (rs.getInt("rcount") == 0) { // if no row exists for the aff and person pk, then insert into table
                ps2 = con.prepareStatement(SQL_INSERT_INTO_AFF_MBR_EMPLOYER);
                ps2.setInt(2, affPk.intValue());
                ps2.setInt(1, personPk.intValue());
                ps2.setInt(3, userPk.intValue());
                ps2.setInt(4, userPk.intValue());
                ps2.executeUpdate();

            }
        }

        catch (SQLException e) {
            throw new EJBException
            ("SQL Error Occurred in MaintainMembersBean.insertRowIntoAffMbrEmployerIfNeeded method" + e);
        }

        finally {
            DBUtil.cleanup(null, ps2, null);
            DBUtil.cleanup(con, ps, rs);

        }

    }


    /**
     *
     * Adds a participation detail to a member
     *
     * @param participationData new Participation Data
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void addParticipationData(ParticipationData pd) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_PARTICIPATION);
            ps.setInt(1, pd.getTheParticipationOutcomeData().getDetailPk().intValue());
            ps.setInt(2, pd.getPersonPk().intValue());
            ps.setInt(3, pd.getTheParticipationOutcomeData().getOutcomePk().intValue());
            ps.setTimestamp(4, pd.getMbrParticipDt());
            ps.setString(5, pd.getTheCommentData().getComment());
            ps.setInt(6, pd.getTheRecordData().getCreatedBy().intValue());
            ps.setInt(7, pd.getTheRecordData().getCreatedBy().intValue());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.addParticipationData method");
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }

    } // addParticipationData


    /**
     *
     * Edits a participation detail belonging to a member
     *
     * @param participationData new Participation Data
     * @param oldDetailPk Pk for record being updated.  part of update can be change to Pk.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void editParticipationData(ParticipationData pd, Integer oldDetailPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationData data = null;

        try {
            log.debug("Inside TRY *********************");
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_PARTICIPATION);
            log.debug("SQL = ***********" + SQL_UPDATE_PARTICIPATION + " *** " + pd.getTheParticipationOutcomeData().getDetailPk().intValue() );
            ps.setInt(1, pd.getTheParticipationOutcomeData().getDetailPk().intValue());
            ps.setInt(2, pd.getTheParticipationOutcomeData().getOutcomePk().intValue());
            ps.setTimestamp(3, pd.getMbrParticipDt());
            ps.setString(4, pd.getTheCommentData().getComment());
            ps.setInt(5, pd.getTheRecordData().getCreatedBy().intValue());
            ps.setInt(7, oldDetailPk.intValue());
            ps.setInt(6, pd.getPersonPk().intValue());
            log.debug("Executing update *********************");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.editParticipationData method");
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }

    } // addParticipationData

    /**
     *
     *  Updates a table (COM_Weekly_Mbr_Card_Run) that gathers the Members that require a
     *  new Member card to be generated.
     *
     * There are a number of rules which may determine whether a person can be added to the
     * weekly card run. These rules are currently under evaluation. This version of this
     * method ignores any business rules and just adds the person passed to that aff's
     * for the weekly card run.
     *
     * @param Integer - personPk Primary Key of the Person who needs a Member card
     * @param Integer - affPk The Affiliate for which the Member needs a card
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

    public void addToWeeklyCardRun    (Integer personPk, Integer affPk) {

	if (this.isAlreadyOnWeeklyCardRun(personPk, affPk) == true) {
            log.debug("MaintainMembers.addToWeeklyCardRun(personPk, affPk, newMember) - member already on weekly card run ");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;


        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_ADD_MEMBER_CARD_RUN);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            int rt = ps.executeUpdate();
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.addToWeeklyCardRun method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }


    /**
     *
     *  Updates a table (COM_Weekly_Mbr_Card_Run) that gathers the Members that require a
     *  new Member card to be generated.
     *
     * There are a number of rules which may determine whether a person can be added to the
     * weekly card run. These rules are currently under evaluation. Therefore, this method may
     * change signature. The newMember parameter exists to help evaluate the business rules
     *
     * @param Integer - personPk Primary Key of the Person who needs a Member card
     * @param Integer - affPk The Affiliate for which the Member needs a card
     * @param NewMember - the newmember object which contains data that is used to
     * evaluate business rules
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

    public void addToWeeklyCardRun    (Integer personPk, Integer affPk, NewMember newMember) {


        if (this.isAlreadyOnWeeklyCardRun(personPk, affPk) == true) {
            log.debug("MaintainMembers.addToWeeklyCardRun(personPk, affPk, newMember) - member already on weekly card run ");
            return;
        }
        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_ADD_MEMBER_CARD_RUN);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            int rt = ps.executeUpdate();
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.addToWeeklyCardRun method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }

    /**
     *
     *  Updates a table (COM_Weekly_Mbr_Card_Run) that gathers the Members that require a
     *  new Member card to be generated.
     *
     * There are a number of rules which may determine whether a person can be added to the
     * weekly card run. These rules are currently under evaluation and this method and
     * its signature may change based on those rules
     *
     * @param Integer - personPk Primary Key of the Person who needs a Member card
     * @param Integer - The Affiliate for which the Member needs a card
     * @param MemberData - The MemberData object passed in for the update. Contains data
     * required to evaluate the business rules for adding a member to weekly card run
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

    public void addToWeeklyCardRun    (Integer personPk, Integer affPk, MemberAffiliateData memberAffData) {

        if (isAlreadyOnWeeklyCardRun(personPk, affPk) == true) {
            log.debug("MaintainMembers.addToWeeklyCardRun(personPk, affPk, memberAffData) - member already on weekly card run ");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_ADD_MEMBER_CARD_RUN);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            int rt = ps.executeUpdate();
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.addToWeeklyCardRun method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }

     /**
     *
     *  Updates a table (COM_Weekly_Mbr_Card_Run) that gathers the Members that require a
     *  new Member card to be generated.
     *
     * This method supports processing where the person name (primarily) or other data
     * non affiliate data has changed, and the change requires that a new member card be
     * generated for each affiliate that the member belongs to.
     *
     * @param Integer - personPk Primary Key of the Person who needs a Member card
     *
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

    public void addToWeeklyCardRun    (Integer personPk) {

      /* First get the collection of affiliates that person is a member of. Set could be zero.
        then loop through the set and check to see if the person is already on the weekly card run
        via if (isAlreadyOnWeeklyCardRun(personPk, affPk) != false)
        If not alreay on the weekly card run, then add. Else ignore and process next affiliate
       */

        // first get affiliates for which this person is a member
        Collection list = new ArrayList();

        // get a set of memberAffiliateResult objects for the affiliates this person belongs to
        // warning, the list could be null if the person is not in an affiliate. . .
        list = getMemberAffiliation(personPk);


        Iterator memIter = list.iterator();

        Connection con = null;
        PreparedStatement ps = null;


        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_ADD_MEMBER_CARD_RUN);
            ps.setInt(1, personPk.intValue());

            while (memIter.hasNext()) {

                //get the member affiliation object
                MemberAffiliateResult memberAffResult = (MemberAffiliateResult)memIter.next();

                // check to see if member is already on the weekly card run for this affiliate, if not, add them
                if (this.isAlreadyOnWeeklyCardRun(personPk, memberAffResult.getAffPk()) == false)
                {

                    ps.setInt(2, memberAffResult.getAffPk().intValue());
                    int rt = ps.executeUpdate();
                }

            } // while
        } // try
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.addToWeeklyCardRun method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }

    /**
     * @J2EE_METHOD  --  deleteParticipationData
     * Deletes the association of a participation detail and outcome with a person
     *
     * @param personPK Person Primary Key
     * @param participationPK Participation Primary Key
     * @return 'true' if deletion completes, and 'false' otherwise.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public boolean deleteParticipationData    (Integer personPk, Integer participationPk) {
        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_DELETE_PARTICIPATION);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, participationPk.intValue());
            int rt = ps.executeUpdate();
            if (rt <= 0) {
                return false;
            }
            else return true;
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.deleteParticipationData method");
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    }

     /**
     * Retrieves the Employer information for a Member at an Affiliate.
     *
     * @param personPk the Person Primary Key
     * @param affPk the Affiliate Primary Key
     *
     * @return the EmployerData object
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public org.afscme.enterprise.member.EmployerData getAffiliateEmployerData(Integer personPk, Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EmployerData employerData = new EmployerData();

        try {

            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_SELECT_MEMBER_AFF_EMPLOYER_DATA);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
            { // should only return one row per natural key but there may not be a row

                // set data from the table
                employerData.setPersonPk(personPk);
                employerData.setAffPk(affPk);
                employerData.setEmployerNm(rs.getString("employer_name"));
                employerData.setJobTitle(new Integer(rs.getInt("emp_job_title")));
                employerData.setEmployeeSector(new Integer(rs.getInt("emp_sector")));
                employerData.setJobSite(rs.getString("emp_job_site"));
                employerData.setSalaryRange
                    (new Integer(rs.getInt("emp_salary_range")));
                employerData.setSalary(rs.getBigDecimal("employee_salary"));
            }
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getAffiliateEmployerData method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return employerData;
    }

    /**
     *
     * Updates the Employer information for a Member at an Affiliate.
     * First calls a method to check to see if there is an employer record
     * for that member at that affiliate.
     *
     * @param EmployerData the Employer Data
     *
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void updateAffiliateEmployerData(EmployerData data, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;

        // checks to see if a row exist, if it doesn't, inserts row into Mbr_Aff_Employers table
        insertRowIntoAffMbrEmployerIfNeeded(data.getPersonPk(), data.getAffPk(), userPk);

        //updates Affiliate Employer data to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AFF_MBR_EMPLOYER);

            ps.setInt(8, data.getPersonPk().intValue());
            ps.setInt(9, data.getAffPk().intValue());
            DBUtil.setNullableVarchar(ps, 1, data.getEmployerNm());
            DBUtil.setNullableInt(ps, 2, data.getJobTitle());
            DBUtil.setNullableInt(ps, 3, data.getEmployeeSector());
            DBUtil.setNullableVarchar(ps, 4, data.getJobSite());
            ps.setBigDecimal(5, data.getSalary());
            DBUtil.setNullableInt(ps, 6, data.getSalaryRange());
            ps.setInt(7, userPk.intValue());
            ps.executeUpdate();
        }
       catch (SQLException e) {
            throw new EJBException("SQL Error occurred in MaintainMembersBean.updateAffiliateEmployerData method ", e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }


    }



    /**
     *
     * Retrieves the comment history for a Member.
     *
     * Member - Member Affiliation uses Person Comments - no separate table.
     * This method is provided as an convenience "interface" to the MaintainPersonBean method
     *
     * @param personPk Person Primary Key
     * @return the Collection of CommentData objects.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public java.util.Collection getCommentHistory(Integer personPk) {
        return (m_persons.getCommentHistory(personPk));
    }

    /**
     * @J2EE_METHOD  --  getMemberAffiliateData
     * Retrieves the affiliate specific member data for a member in an affiliate.
     * Gets information on whether the member is a current officer for that affiliate
     * If so, retrieves the AFSCME Office Title Nm and some indication of whether the
     * Address for the officer is an Affiliate address or Person(al) address
     *
     * @param personPK Person Primary Key
     * @param affPK Affiliate Primary Key
     * @return the MemberAffiliateData object.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public MemberAffiliateData getMemberAffiliateData    (Integer personPk, Integer affPk) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MemberAffiliateData affMemberData = new MemberAffiliateData();
        RecordData recordData = new RecordData();
        Integer affStatus;
        org.afscme.enterprise.affiliate.AffiliateData affData;

        // do some work to get the affiliate status, which is needed for some display rules
        affData = m_affiliates.getAffiliateData(affPk);
        affStatus = affData.getStatusCodePk();


        try {

            con = DBUtil.getConnection();

            /** ?? first get affiliate identifier and determine if business rules are violated
             *  if any are, do not perform the additional work. Return false
             */

            ps = con.prepareStatement(SQL_SELECT_MEMBER_AFF_DATA);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue());
            rs = ps.executeQuery();
            rs.next(); // should only return one row per natural key

            // set data from Aff_Members table
            affMemberData.setPersonPk(personPk);
            affMemberData.setAffPk(affPk);
            if (affStatus == Codes.AffiliateStatus.RA) affMemberData.setAffRestrictedAdmin(true);
            affMemberData.setMbrStatus(new Integer(rs.getInt("mbr_status")));
            affMemberData.setMbrType(new Integer(rs.getInt("mbr_type")));
            affMemberData.setMbrNoLocal(rs.getString("mbr_no_local"));
            affMemberData.setMbrJoinDt(rs.getTimestamp("mbr_join_dt"));
            affMemberData.setPrimaryInformationSource
            (new Integer(rs.getInt("primary_information_source")));
            affMemberData.setMbrCardSentDt(rs.getTimestamp("mbr_card_sent_dt"));
            affMemberData.setMbrDuesType(new Integer(rs.getInt("mbr_dues_type")));
            affMemberData.setMbrDuesRate(new Double(rs.getDouble("mbr_dues_rate")));
            affMemberData.setMbrDuesFrequency(new Integer(rs.getInt("mbr_dues_frequency")));
            affMemberData.setLostTimeLanguageFg
            (DBUtil.getBooleanFromShort(rs.getShort("lost_time_language_fg")));

            affMemberData.setMbrRetiredDt(rs.getTimestamp("mbr_retired_dt"));
            affMemberData.setMbrRetRenewalDuesFg
            (DBUtil.getBooleanFromShort(rs.getShort("mbr_ret_dues_renewal_fg")));

            affMemberData.setNoCardsFg(DBUtil.getBooleanFromShort(rs.getShort("no_cards_fg")));
            affMemberData.setNoMailFg(DBUtil.getBooleanFromShort(rs.getShort("no_mail_fg")));
            affMemberData.setNoPublicEmpFg(DBUtil.getBooleanFromShort(rs.getShort("no_public_emp_fg")));
            affMemberData.setNoLegislativeMailFg
            (DBUtil.getBooleanFromShort(rs.getShort("no_legislative_mail_fg")));

            recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
            recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
            recordData.setCreatedDate(rs.getTimestamp("created_dt"));
            recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
            // add the record data to the affMemberObject
            affMemberData.setTheRecordData(recordData);

            /*  due some work to enforce the business rules (section 3.4.16.2.4 of the Membership UI document)
                in Member Affiliate Information - edit : must enforce on update and retrieval, as js within the page will
                set and lock(disable) the fields - but a current limitation of the union of http and html is that the
                disabled fields are not returned in the request - therefore struts can't set the properties in the bean
             */


            // based on certain member types, set certain mail flags to true (checked in the UI)
            Integer mbrType = affMemberData.getMbrType();
            if (mbrType == Codes.MemberType.A)
            {
                affMemberData.setNoCardsFg(new Boolean(true));
                affMemberData.setNoLegislativeMailFg(new Boolean(true));
            }
            if (mbrType == Codes.MemberType.O)
            {
                affMemberData.setNoCardsFg(new Boolean(true));
            }
            if (mbrType == Codes.MemberType.S)
            {
                 affMemberData.setNoPublicEmpFg(new Boolean(true));
            }


            /** get officer information and get data for afscme office title and officer address
             * if the member is currently an officer in this affiliate */
            affMemberData.setTheOfficerInfo(this.getOfficerInfoForMemberAffiliation(affPk, personPk, affStatus));

        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getMemberAffiliateData method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return affMemberData;

    }

    /**
     *
     * This method retrieves the "set" of affiliates to which the person
     * belongs as a member.
     * This method is very similar to the getMemberAffiliateSummary method
     * except it does not process the officer information
     *
     * @param Integer - personPk Person Primary Key
     * @return Collection - of MemberAffiliateResult objects with affPks
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

     public java.util.Collection getMemberAffiliation(Integer personPk) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.util.Collection list = new ArrayList();

        /** first create a collection of MemberAffiliateResults objects with member
         * affiliate data */
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_RESULTS);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();

            while ( rs.next()) {

                    MemberAffiliateResult memberAffResult = new MemberAffiliateResult();
                    memberAffResult.setPersonPk(personPk);
                    memberAffResult.setAbbreviatedName(new String(rs.getString("aff_abbreviated_nm")));
                    memberAffResult.setAffPk(new Integer(rs.getInt("aff_pk")));
                    log.debug("personPk is: " + memberAffResult.getPersonPk() + "affPk is: " + memberAffResult.getAffPk());
                    memberAffResult.setMbrStatus(new Integer(rs.getInt("mbr_status")));
                    memberAffResult.setMbrType(new Integer(rs.getInt("mbr_type")));
                    memberAffResult.setAffStatus(new Integer(rs.getInt("aff_status")));

                    if (memberAffResult.getAffStatus().equals(Codes.AffiliateStatus.RA)) {
                        memberAffResult.setIsAffRestrictedAdmin(true);

                    }
                    // don't bother creating or setting an AffiliateIdentifier object from the query results

                    // add the result object to the collection
                    list.add(memberAffResult);


                } // while

        } // try
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getMemberAffiliation method" + e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return (list);

     } // getMemberAffiliation


    /**
     *
     * Retrieves affiliate summary data to support the affiliate associations displayed on
     *  the member detail page.
     *
     * This method must return data from four or more tables in order to sdatisfy the data
     * requirements of the UI. The initial construction decision is to get information
     * from Aff_Members and Aff_organizations first, then loop through the hopefully small
     * collection and determine which of those affiliations the member is an officer.
     * Since most members will be affiliated with only one affiliate, and only an
     * extraordinary case would go past three affiliations, this should be relatively small.
     * Still, this could be re-written as fewer queries if analysis proves this would be
     * better
     *
     * Once officer affiliation is determined, each officers address pointer information
     * will be retrieved based on whether their address is Person or Affiliate based
     *
     * @param personPK Person Primary Key
     * @return the Collection of MemberAffiliateResult objects.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public java.util.Collection getMemberAffiliatesSummary(Integer personPk, Collection vduAffiliates) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection list = new ArrayList();
        Collection listAfterOfficerInfo = new ArrayList();
        PreparedStatementBuilder pb = new PreparedStatementBuilder();

        /** first create a collection of MemberAffiliateResults objects with member
         * affiliate data */
        try {
            con = DBUtil.getConnection();
            // ps = con.prepareStatement(SQL_SELECT_AFFILIATE_RESULTS);
            //pb.addCriterion("person_pk", personPk);

            /* The following is performed in order to support data level access control in the
            * view data utility - adds an in clause if access to member search is through the view
            * data utility
            */
            if (vduAffiliates != null)
            {
                log.debug("MaintainMembersBean.getMemberAffiliatesSummary - vduAffiliates not null, adding additional where clause ");
                Criterion criterion = new Criterion();
                criterion.setField("Aff_Organizations.aff_pk");
                criterion.setOperator("IN");
                criterion.setType(Types.INTEGER);
                criterion.setValues(new LinkedList(vduAffiliates));
                pb.addCriterion(criterion);
            }

              pb.addCriterion("person_pk", personPk);

              ps = pb.getPreparedStatement(SQL_SELECT_AFFILIATE_RESULTS_WITHOUT_WHERE, con, true);
             // ps.setInt(1, personPk.intValue()); // criterion above is first parameter
              rs = ps.executeQuery();

            while ( rs.next()) { // note look at this logic in conjunction with the do while

                    MemberAffiliateResult memberAffResult = new MemberAffiliateResult();
                    memberAffResult.setPersonPk(personPk);
                    memberAffResult.setAbbreviatedName(new String(rs.getString("aff_abbreviated_nm")));
                    memberAffResult.setAffPk(new Integer(rs.getInt("aff_pk")));
                    log.debug("personPk is: " + memberAffResult.getPersonPk() + "affPk is: " + memberAffResult.getAffPk());
                    memberAffResult.setMbrStatus(new Integer(rs.getInt("mbr_status")));
                    memberAffResult.setMbrType(new Integer(rs.getInt("mbr_type")));
                    memberAffResult.setAffStatus(new Integer(rs.getInt("aff_status")));

                    if (memberAffResult.getAffStatus().equals(Codes.AffiliateStatus.RA)) {
                        memberAffResult.setIsAffRestrictedAdmin(true);

                    }
                    // create an AffiliateIdentifier object and populate
                    AffiliateIdentifier affId = new AffiliateIdentifier();
                    affId.setType(new Character(rs.getString("aff_type").toCharArray()[0]));
                    affId.setLocal(rs.getString("aff_localSubChapter"));
                    affId.setState(rs.getString("aff_stateNat_type"));
                    affId.setSubUnit(rs.getString("aff_subUnit"));
                    affId.setCouncil(rs.getString("aff_councilRetiree_chap"));

                    log.debug("affiliate identifier is: " + affId.toString());

                    // set the AffiliateIdentifier object in result
                    memberAffResult.setTheAffiliateIdentifier(affId);

                    // add the result object to the collection
                    list.add(memberAffResult);


                } // while


        }   catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getMemberAffiliatesSummary method" + e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        /**
         * then check to see whether this member is a current officer of the affiliate
         * getOfficerInfoForMember(list);
         */

        listAfterOfficerInfo =  getOfficerInfoForMember(list);

        return (listAfterOfficerInfo);
    }

    /**
    * Gets officer info (title and address data) for a member in a specific affiliate
    * Similar to getOfficerInfoForMember - which gets the data for all affiliates
    * the member belongs to.
    *
    * @param Integer - affiliate primary key
    * @param Integer - person primary key
    * @param Integer - affiliate status common code primary key
    * @return Collection - of MemberOfficerTitleAddressInfo objects.
    *
    * @ejb:interface-method view-type="local"
    * @ejb:transaction type="Required"
    */
    public java.util.Collection getOfficerInfoForMemberAffiliation (Integer affPk, Integer personPk, Integer affStatus)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Collection officerInfo = new ArrayList();

        con = DBUtil.getConnection();

        /** see if the member is a current officer
         * for this specific affiliate Query the officer
         * history table for this data
         */

          try
          {

             ps = con.prepareStatement(SQL_SELECT_OFFICER_INFO_FOR_MEMBER);

             ps.setInt(1, personPk.intValue());

             ps.setInt(2, affPk.intValue());
             // yes this is correct, setAffPk twice. aff_pk and
             // position_mbr_affiliation should be the same
             ps.setInt(3, affPk.intValue());

             rs = ps.executeQuery();


             // get the results out of the result set and stick them in an object

             while (rs.next()) { // this should mean the member is a current officer

                    log.debug("MaintainMembers.getOfficerInfoForMemberAffiliation, inside retrieval loop on officer history: pos_address_from_Person_pk is "+ rs.getInt("pos_addr_from_org_pk"));

                    MemberOfficerTitleAddressInfo mOffInfo = new MemberOfficerTitleAddressInfo();
                    mOffInfo.setPosAddrFromPersonPk(DBUtil.getIntegerOrNull(rs, "pos_addr_from_person_pk"));
                    mOffInfo.setPosAddrFromOrgPk(DBUtil.getIntegerOrNull(rs, "pos_addr_from_org_pk"));
                    mOffInfo.setMbrSuspendedFg(DBUtil.getBooleanFromShort(rs.getShort("suspended_fg")));
                    mOffInfo.setAfscmeTitleNm(new Integer(rs.getString("afscme_title_nm")));

                    // if aff not in administratorship and officer not suspended and Person Addr
                    // get the addr type don't make the call if suspended or in administratorship
                    if (mOffInfo.getMbrSuspendedFg().booleanValue() == false &
                        mOffInfo.getPosAddrFromPersonPk() != null &
                        !(affStatus == Codes.AffiliateStatus.RA) ) {
                            PersonAddress pAddr = new PersonAddress();
                            pAddr = m_systemAddress.getPersonAddress(mOffInfo.getPosAddrFromPersonPk());
                            mOffInfo.setPersonAddrType(pAddr.getType());
                            log.debug("getOfficerInfo - getting person address" + pAddr.getType());
                    }

                    // if aff not in administratorship and officer not suspended and Org Addr
                    // get the location name
                    if (mOffInfo.getMbrSuspendedFg().booleanValue() == false &
                        mOffInfo.getPosAddrFromOrgPk() != null &
                        !(affStatus == Codes.AffiliateStatus.RA) ) {

                        LocationData locData = new LocationData();
                        locData = m_orgLocations.getOrgLocation(mOffInfo.getPosAddrFromOrgPk()); // needs to be locationPk not address Pk
                        mOffInfo.setOrgAddrLocationNm(locData.getLocationNm());
                        log.debug("getOfficerInfo - get affiliate location for officer" + locData.getLocationNm());

                    }

                    // add each officer title for the member to this collection

                    officerInfo.add(mOffInfo);

            }// end while

            } // end try

            catch (SQLException e) {
                throw new EJBException("SQL Error Occurred in MaintainMembersBean.getOfficerInfoForMember method");
            }
            finally {
                DBUtil.cleanup(con, ps, rs);
            }

     return officerInfo;

    }




    /**
     *
     * Retrieves officer information for a member across all affiliates they belong to.
     * This method iterates through a collection of MemberAffiliateResult objects and attempts
     * to retrieve information on any current officer positions held by that member in that
     * affiliate. The requirements used to assume that a person may hold only one current position
     * in one affiliate. The requirements now allow a member to hold multiple curent positions in
     * the same affiliate
     *
     * Members will generally only be affiliated with one affiliate and a 99.9% of the time
     * maximum of 3 affiliates. If the member is not a current officer of that affiliate,
     * no data will be returned. If they are an officer, the appropriate data is added to
     * the MemberAffiliateResult object in the collection
     *
     * @param Collection - of MemberAffiliateResult objects
     * @return Collection - of MemberAffiliateResult objects.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public java.util.Collection getOfficerInfoForMember(Collection memberAffCol) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        con = DBUtil.getConnection();

        /** see if the member is a current officer
         * Iterate through the collection of memberAffResults & query the officer
         * history table for each
         */
         Iterator memIter = memberAffCol.iterator();

         try {

            ps = con.prepareStatement(SQL_SELECT_OFFICER_INFO_FOR_MEMBER);


            while (memIter.hasNext()) {

                //get the member affiliation object
                MemberAffiliateResult memberAffResult = (MemberAffiliateResult)memIter.next();

                log.debug(" In while, in try, in getOfficerInfo personPk is: " + memberAffResult.getPersonPk() + "affPk is: " + memberAffResult.getAffPk());

                if (ps == null) { log.debug("ps is null"); }

                ps.setInt(1, memberAffResult.getPersonPk().intValue());

                ps.setInt(2, memberAffResult.getAffPk().intValue());
                // yes this is correct, setAffPk twice. aff_pk and
                // position_mbr_affiliation should be the same
                ps.setInt(3, memberAffResult.getAffPk().intValue());

                rs = ps.executeQuery();
                /** this code is changed because the rule has been changed to allow a member
                 * to hold multiple officer positions within an
                 * affiliate concurrently. The code here needs to change to retrieve
                 * multiple records and create a collection of OfficerTitleAddress objects
                 * within a single MemberAffiliateResult object
                 **/
                Collection officerTitleCollection = new ArrayList();

                while (rs.next()) { // this should mean the member is a current officer

                    log.debug
                    ("MaintainMembers.getOfficerInfoForMember, inside retrieval loop on officer history: pos_address_from_Person_pk is "
                    + rs.getInt("pos_addr_from_org_pk"));

                    MemberOfficerTitleAddressInfo mOffInfo = new MemberOfficerTitleAddressInfo();
                    mOffInfo.setPosAddrFromPersonPk(DBUtil.getIntegerOrNull(rs, "pos_addr_from_person_pk"));
                    mOffInfo.setPosAddrFromOrgPk(DBUtil.getIntegerOrNull(rs, "pos_addr_from_org_pk"));
                    mOffInfo.setMbrSuspendedFg(DBUtil.getBooleanFromShort(rs.getShort("suspended_fg")));
                    mOffInfo.setAfscmeTitleNm(new Integer(rs.getString("afscme_title_nm")));

                    // if aff not in administratorship and officer not suspended and Person Addr
                    // get the addr type don't make the call if suspended or in administratorship
                    if (mOffInfo.getMbrSuspendedFg().booleanValue() == false &
                    mOffInfo.getPosAddrFromPersonPk() != null &
                    !(memberAffResult.getAffStatus().equals(Codes.AffiliateStatus.RA)) ) {
                        PersonAddress pAddr = new PersonAddress();
                        pAddr = m_systemAddress.getPersonAddress(mOffInfo.getPosAddrFromPersonPk());
                        mOffInfo.setPersonAddrType(pAddr.getType());
                        log.debug("getOfficerInfo - getting person address" + pAddr.getType());
                    }

                    // if aff not in administratorship and officer not suspended and Org Addr
                    // get the location name
                    if (mOffInfo.getMbrSuspendedFg().booleanValue() == false &
                    mOffInfo.getPosAddrFromOrgPk() != null &
                    !(memberAffResult.getAffStatus().equals(Codes.AffiliateStatus.RA)) ) {

                        LocationData locData = new LocationData();
                        locData = m_orgLocations.getOrgLocation(mOffInfo.getPosAddrFromOrgPk()); // needs to be locationPk not address Pk
                        mOffInfo.setOrgAddrLocationNm(locData.getLocationNm());
                        log.debug("getOfficerInfo - get affiliate location for officer" + locData.getLocationNm());

                    }
                    // add each officer title for the member to this collection
                    officerTitleCollection.add(mOffInfo);


                } // while (rs.next())

                  // for each member, set the set of current officer positions that they hold
                  memberAffResult.setTheOfficerInfo(officerTitleCollection);

              } // while (memIter)

            } // end try

            catch (SQLException e) {
                throw new EJBException("SQL Error Occurred in MaintainMembersBean.getOfficerInfoForMember method");
            }
            finally {
                DBUtil.cleanup(con, ps, rs);
            }


        return(memberAffCol);

    } //getOfficerInfoForMember


    /**
     *
     * Retrieves Member Detail data.
     *
     * @param Integer - personPK Person Primary Key
     * @param Integer - dept - Department of the user accessing the data
     * @return MemberData - the MemberData object
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public org.afscme.enterprise.member.MemberData getMemberDetail(Integer personPk, Integer dept, Collection vduAffiliates)
    {

        MemberData memberData = new MemberData();
        MemberAffiliateResult memberAffResult = new MemberAffiliateResult();

        //get person oriented data first and set it in the data bject
        memberData.setThePersonData(m_persons.getPersonDetail(personPk, dept));

        //next get the member affiliate result data and set it in memberData
        memberData.setTheMemberAffiliateResults(getMemberAffiliatesSummary(personPk, vduAffiliates));

        // Set the modified attributes using those from the Person table for now
        RecordData recordData = new RecordData();

        /**
         * get the Member specific(not member affiliate specific) data that now resides
         * in the Person table with the following SQL
         */
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_DETAIL_FROM_PERSON);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
            {
                memberData.setMbrBarredFg(DBUtil.getBooleanFromShort(rs.getShort("mbr_barred_fg")));
                memberData.setMbrExpelledDt(rs.getTimestamp("mbr_expelled_dt"));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                memberData.setTheRecordData(recordData);
            }
            else
            {
                throw new EJBException("Error Occurred in MaintainMembersBean, no row found for Person "
                                    + "when trying to access mbr_barred_fg and mbr_expelled_dt");

            }
            // finally finished and ready to return memberData

        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getMemberDetail method");
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return memberData;
    }

/**
     *
     * Search for existing persons based on SSN, Last Name and First Name.
     * Similar to getDuplicatePerson functionality in MaintainPersons EJB,
     * only this is different ! Query must return affiliate identifier, since
     * and so different query is used, and MemberCriteria class is used in place
     * of PersonCriteria
     *
     * @param MemberCriteria mc - search criteria
     * @param list to be passed by reference that will contain the duplicate results
     * @return int of the total count of duplicate persons
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getDuplicatePersonMembers    (MemberCriteria mc, ArrayList result)
    {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        MemberResult data = new MemberResult();
        ResultSet rs = null;
        Collection list = new ArrayList();
        int count = 0;

        //gets a group of persons with same criteria from the database
        try {
           con = DBUtil.getConnection();
            if (mc.getFirstNm() != null) {
                builder.addCriterion("first_nm", mc.getFirstNm());
            }
            if (mc.getLastNm() != null) {
                builder.addCriterion("last_nm", mc.getLastNm());
            }
            if (mc.getSuffixNm() != null) {
                builder.addCriterion("suffix_nm", mc.getSuffixNm());
            }
            if (mc.getDob() != null) {
                builder.addCriterion("dob", mc.getDob().toString());
            }
            if (mc.getSsn() != null) {
                builder.addCriterion("ssn", mc.getSsn());
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
            if (!TextUtil.isEmptyOrSpaces(mc.getOrderBy())) {
                StringBuffer sb = new StringBuffer("");

                // need to handle orderby of council and local by cast to retain
                // numeric ordering - also handled in set of criteria object for user
                // selection
                sb.append(mc.getOrderBy());
                if (mc.getOrdering() < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                builder.addOrderBy(sb.toString().trim());
            }
            else {
                /* add default Sort fields:
                 * last_n
                 * first_nm
                 * council (numeric ASC on a string field)
                 * local (numeric ASC on a string field)
                 */
                builder.addOrderBy(" p.person_nm ASC ");
                builder.addOrderBy(" personAddrState ASC ");
                builder.addOrderBy(" int_council ASC "); // order by numeric
                builder.addOrderBy(" int_local ASC "); // order by numeric
            }
            //create the statement that gets the data

            ps = builder.getPreparedStatement(SQL_SELECT_DUPLICATE_PERSON, con);
            rs = ps.executeQuery();

            if (mc != null)
                rs.absolute((mc.getPage() * mc.getPageSize()) + 1);
            else
                rs.next();

            //put the results into a the list of PersonResult objects
            int index = 0;
            int pageSize = mc != null ? mc.getPageSize() : 0;
            int startIndex = mc == null ? 0 : mc.getPage() * pageSize;
            while (
                index + startIndex < count &&
                (mc == null || index < pageSize))
            {
                MemberResult memberResult = new MemberResult();
                memberResult.setPersonPk(new Integer(rs.getInt("person_pk")));
                //get affiliate data if this person is a member and set in the memberResult
                AffiliateIdentifier affId = new AffiliateIdentifier();
                if (rs.getString("aff_type") != null) affId.setType(new Character(rs.getString("aff_type").toCharArray()[0]) );
                if (rs.getString("aff_code") != null) affId.setCode(new Character(rs.getString("aff_code").toCharArray()[0]) );
                affId.setState(rs.getString("aff_stateNat_type"));
                affId.setSubUnit(rs.getString("aff_subUnit"));
                affId.setCouncil(new Integer(rs.getInt("int_council")).toString());
                affId.setLocal(new Integer(rs.getInt("int_local")).toString());

                memberResult.setTheAffiliateIdentifier(affId);
                memberResult.setPersonNm(rs.getString("person_nm"));
                memberResult.setSsn(rs.getString("ssn"));
                memberResult.setAddress(rs.getString("personAddr"));
                memberResult.setCity(rs.getString("personAddrCity"));
                memberResult.setState(rs.getString("personAddrState"));
                memberResult.setZipCode(rs.getString("personAddrPostalCode"));
                result.add(memberResult);
                rs.next();
                index++;
            }

        } catch (SQLException e) {
            throw new EJBException("Error getting duplicate persons MaintainMembersBean.getDuplicatePersonMembers()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return count;
    }

    /**
     *
     * Retrieves the association of a participation detail and outcome with a person.
     *
     * @param personPK Person Primary Key
     * @param participationPK Participation Primary Key
     * @return the ParticipationData object
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public ParticipationData getParticipationData    (Integer personPK, Integer participationPK) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationData data = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_GET_PARTICIPATION_DTL);
            ps.setInt(1, personPK.intValue());
            ps.setInt(2, participationPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data = readParticipationData(rs);
            }

        }

        catch (SQLException e) {
            return null;
          //  throw new EJBException("SQL Error Occurred in MaintainMembersBean.getParticipationData method");
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;

    } // getParticipationData


    /**
     *
     * Retrieves the timePk from the RPT_Time_Dimension table
     *
     * @param int - month integer representation 1-12, not 0-11, so increment
     *   if you pulled the current month from a function that number month 0-11
     * @param int - year integer representation of the year, form YYYY
     * @return Integer - a timePk from the RPT_Time_Dimension table
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Integer getTimePk    (int month, int year) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer timePk = null;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_GET_TIME_PK);

            ps.setInt(2, month);
            ps.setInt(1, year);
            rs = ps.executeQuery();

            // assumes only one value will be returned
            if (rs.next()) {
                timePk = new Integer(rs.getInt("time_pk"));
            }
            else {
               throw new EJBException("Error Occurred in MaintainMembersBean.getTimePk method - timePk not found");
            }
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getTimePk method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return timePk;

    } // getTimePk

    /**
     *
     * Determines whether a person has any current officer positions (in any affiliate)
     *
     * @param Integer - personPk
     * @return boolean - true if the person holds a current office in any affiliate 
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public boolean isPersonACurrentOfficer(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        
        try {
        
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_COUNT_CURRENT_OFFICE_FOR_PERSON);
            /** Get any officer position (regardless of affiliate) for this person that is current
            i.e. select count(*) from officer_history where person_pk is ? pos_end_dt IS NULL
            */
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("oCount");
            if (count > 0) return true;
                else return false;
        }
        catch (SQLException e) {
            throw new EJBException
                ("SQL Error Occurred in MaintainMembersBean.isPersonACurrentOficer method" + e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
    }

    protected ParticipationData readParticipationData(ResultSet rs) throws SQLException {

        ParticipationData readData = new ParticipationData();
        readData.setPersonPk(new Integer(rs.getInt("person_pk")));
        readData.setMbrParticipDt(rs.getTimestamp("mbr_particip_dt"));
        readData.setParticipDetailPk(new Integer(rs.getInt("particip_detail_pk")));

        ParticipationOutcomeData outcomeData = new ParticipationOutcomeData();
        outcomeData.setDetailNm(rs.getString("particip_detail_nm"));
        outcomeData.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
        outcomeData.setOutcomeNm(rs.getString("particip_outcome_nm"));
        outcomeData.setOutcomePk(new Integer(rs.getInt("particip_outcome_pk")));
        outcomeData.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
        outcomeData.setGroupNm(rs.getString("particip_group_nm"));
        outcomeData.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
        outcomeData.setTypeNm(rs.getString("particip_type_nm"));
        outcomeData.setTypePk(new Integer(rs.getInt("particip_type_pk")));
        readData.setTheParticipationOutcomeData(outcomeData);

        CommentData commentData = new CommentData();
        commentData.setComment(rs.getString("comment_txt"));
        readData.setTheCommentData(commentData);

        RecordData recordData = new RecordData();
        recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
        recordData.setCreatedDate(rs.getTimestamp("created_dt"));
        recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
        recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
        readData.setTheRecordData(recordData);

        return readData;
    }

   /**
     * @J2EE_METHOD  --  getParticipationSummary
     * Retrieves a Collection of activities a member has participated in, as captured in
     *  participation details from the participation group hierarchy.
     *
     * @param personPK Person Primary Key
     * @param sortData Sort Information
     * @return the Collection of ParticipationData objects.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction  type="Required"
     */
    public java.util.Collection getParticipationSummary(Integer personPK, SortData sortData) {
        /** possibly use the data in the collection returned from this and dont
         * need the method to get the data for a single detail/outcome
         *  for a member, for reads. Should get the latest data if edit */


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationData parData = null;
        Collection colParData = new ArrayList();

        try {

            con = DBUtil.getConnection();
            String sql = SQL_GET_PARTICIPATION_RSLT;
            String sortColumn = getParticipationSortColumn(sortData);
            if (sortColumn != null)
                sql += " ORDER BY " + sortColumn;
            ps = con.prepareStatement(sql);
            ps.setInt(1, personPK.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                do {
                    parData = readParticipationData(rs);
                    colParData.add(parData);

                } while (rs.next());
            } // if
            // put data into set and return set or collection
            else {
                /** no participation data for this person,
                collection will be returned without any members */
            }
        } // try

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getParticipationSummary method");
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        // return Collection;
        return colParData;

    }

    /**
     * Gets a SQL column name from the sortData provided
     */
    protected String getParticipationSortColumn(SortData sortData)
    {
        String sortColumn = null;

        switch (sortData.getSortField()) {
            case ParticipationData.SORT_BY_GROUP:
                sortColumn = "particip_group_nm";
                break;
            case ParticipationData.SORT_BY_TYPE:
                sortColumn = "particip_type_nm";
                break;
           case ParticipationData.SORT_BY_DETAIL:
                sortColumn = "particip_detail_nm";
                break;
            case ParticipationData.SORT_BY_OUTCOME:
                sortColumn = "particip_outcome_nm";
                break;
            case ParticipationData.SORT_BY_DATE:
                sortColumn = "mbr_particip_dt";
                break;
            default:
                throw new EJBException("Invalid sort field: "+sortData.getSortField());
        }

        if (sortColumn != null && sortData.getDirection() == SortData.DIRECTION_DESCENDING)
            sortColumn = sortColumn + " DESC";

        return sortColumn;
    }

    /**
     *
     * Searches for members based on a given set of search criteria.
     * This functionality is slightly different from other searches, as the
     * Select list may be tailored by the user. A separate method will be called
     * to create the Select clause from the Collection of column names passed in
     * Initial sort list will be passed though the memberCriteria object as well as
     * any change in the sort column. Note that if sorts are performed on mbr_status,
     * mbr_type, a user_pk, or other common code columns then the actual display values
     * must be used in the select list (not the pks) in order to give the users the desired result
     * Such ordering can not be performed in the order by clause alone, when more than one col
     * is needed to be ordered in this way.
     *
     * A single prepared JOIN clause is used, rather than creating the JOIN clause on
     * the fly based on observation that the SQL Server optimizer will ignore tables not
     * referenced in the SELECT and WHERE clause. However, the Select clause does need to
     * be tailored in order to reference the fewest tables possible for performance and
     * scalability issues.
     *
     * Also, the where clause will always contain the predicates:
     * "marked_for_deletion_fg = 0" in order to select only members who are
     * not marked for deletion. member_fg = 1 is not used, becuase the main table is aff_members
     *
     * In addition, for each row in the initial result set to be returned, getPersonPhones is called to retrieve
     * the affiliate relations home phone and
     * getPersonEmail will be called and the primary email is placed in the result object. A call will
     * be made to SystemAddressBean.getSystemAddress(person_pk) to retrieve the SMA.
     *
     * This method first creates a query to determine the number of rows that will be returned by this criteria
     * The count returned is used in the display, and also to drive the paging mechanism for the result set.
     *
     * The search query is then built. Separate methods are used to create the where clause and process the result set
     *
     * @param MemberCriteria  criteria for the search
     * @param ArrayLiist - results passed by reference, contains results of search
     * @return int - the number of records returned
     *
     *n
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int searchMembers(MemberCriteria memberCriteria, Integer dept, ArrayList results) {

        // create dynamic query
        StringBuffer query = new StringBuffer();
        query.append(createSelectForMemberSearch(memberCriteria));
        query.append(" FROM Aff_Members am " + SQL_JOIN_PERSON);
        String joinClause = createJoinForMemberSearch(memberCriteria);
        query.append(joinClause);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();

        int count = 0;

        // Just use the max columns for now, later will implement createSelect...
        // to optimize the query -- String selectClause = createSelect(memberCriteria);



        /**
          * Removed functionality
          * set the join clause based on whether address fields have been referenced, when the select is dynamically generated
        if (memberCriteria.getHasSelectedAddress()== true | memberCriteria.getHasAddressInWhere() == true )
        {
          joinClause = SQL_JOIN_SEARCH_MEMBERS + SQL_JOIN_SMA_SEARCH_MEMBERS;
          log.debug("SearchMember (SMA) : joinClause is: " + joinClause);
        }
         else {
             joinClause = SQL_JOIN_SEARCH_MEMBERS;
             log.debug("SearchMember (No SMA clause) : joinClause is: " + joinClause);
         }
        */


        try {
            con = DBUtil.getConnection();
            // Create the where clause using PreparedStatementBuilder
            buildMemberWhereClause(builder, memberCriteria);

            // add the rest of the SQL to create the count query
  //          log.debug("searchMembers, before count ps:  " +
    //                builder.getPreparedStatementSQL(SQL_SELECT_COUNT_SEARCH_MEMBERS + SQL_JOIN_PERSON + joinClause, true) ) ;
            ps = builder.getPreparedStatement(SQL_SELECT_COUNT_SEARCH_MEMBERS  + SQL_JOIN_PERSON + joinClause, con);
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);

            log.debug("searchMembers - count is: " + count);

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
            if (!TextUtil.isEmptyOrSpaces(memberCriteria.getOrderBy())) {
                StringBuffer sb = new StringBuffer("");
                // need to handle orderby of council and local by cast to retain
                // numeric ordering - also handled in set of criteria object for user
                // selection
                sb.append(memberCriteria.getOrderBy());
                if (memberCriteria.getOrdering() < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                String column = sb.toString().trim();
                if(column.equals("address DESC")) {
                    builder.addOrderBy("addr1 DESC");
                    builder.addOrderBy("addr2 DESC");
		} else {
                    if (column.equals("address ASC")){
                        builder.addOrderBy("addr1 ASC");
                        builder.addOrderBy("addr2 ASC");                                            
                    } else {    
                        builder.addOrderBy(column);
                    }
                }
            }
            else {
                /* add default Sort fields:
                 * person_nm
                 * council (numeric ASC on a string field)
                 * local (numeric ASC on a string field)
                 */
                builder.addOrderBy(" person_nm ASC ");
                

                // order by affiliate id only if it has been selected
                boolean orderByAffId = false;
                ArrayList columnNames = (ArrayList)memberCriteria.getSelectList();
                for(int i = 0; i < columnNames.size(); i++) {
                    String columnName = (String)columnNames.get(i);
                    if(columnName.equals("affId")) {
                        orderByAffId = true;
                        break;
                    }
                }
		if(orderByAffId) {
                    builder.addOrderBy(" int_council ASC "); // order by numeric
                    builder.addOrderBy(" int_local ASC "); // order by numeric
		}
            }


            // create the query for the results
            ps = builder.getPreparedStatement(query.toString(), con);
            log.debug("searchMembers, after search  ps:  " +
                    builder.getPreparedStatementSQL(query.toString(), true) ) ;

            rs = ps.executeQuery();

            // position the first row, based on the page requested and the page size
            if (memberCriteria != null)
                rs.absolute((memberCriteria.getPage() * memberCriteria.getPageSize()) + 1);
            else
                rs.next();


           // put the results into a the list of memberResult objects
            int index = 0;
            int pageSize = memberCriteria != null ? memberCriteria.getPageSize() : 0;
            int startIndex = memberCriteria == null ? 0 : memberCriteria.getPage() * pageSize;
            while (

                index + startIndex < count &&
                (memberCriteria == null || index < pageSize))
            {

                MemberResult mbrResult = new MemberResult();

                results.add(readMemberSearchResult(rs, mbrResult, (ArrayList)memberCriteria.getSelectList()));

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
     * Populates MemberResult object based on selected output
     */
    private MemberResult readMemberSearchResult(ResultSet rs, MemberResult mr, ArrayList columnNames) throws SQLException {

        // iterate through columns and set properties based on selected columns
        for(int i = 0; i < columnNames.size(); i++) {
			String columnName = (String)columnNames.get(i);
			if(columnName.equals("am.primary_information_source")) {
				mr.setPrimaryInformationSource(rs.getString("primary_information_source"));
			}
			else if(columnName.equals("am.mbr_type")) {
				mr.setMbrType(rs.getString("mbr_type"));
			}
			else if(columnName.equals("am.mbr_status")) {
				mr.setMbrStatus(rs.getString("mbr_status"));
			}
			else if(columnName.equals("am.lst_mod_user_pk")) {
				mr.setLstModUserPk(rs.getString("updated_by"));
			}
			else if(columnName.equals("sma")) {
                String address;
                String addr1 = rs.getString("addr1");
                String addr2 = rs.getString("addr2");
                address = (addr1 == null ? "" : addr1) + (addr2 == null ? "" : addr2); 
		        
		        mr.setAddress(address);
		        mr.setCity(rs.getString("city"));
		        mr.setState(rs.getString("state"));
		        mr.setZipCode(rs.getString("zipcode"));
			}
			else if(columnName.equals("affId")) {
				Integer affPk = DBUtil.getIntegerOrNull(rs, "aff_pk");
				if (affPk == null || affPk.intValue() < 1) {
					mr.setTheAffiliateIdentifier(null);
				} else {
					AffiliateIdentifier affiliateIdentifier = new AffiliateIdentifier();
					affiliateIdentifier.setType(new Character(rs.getString("aff_type").charAt(0)));
					affiliateIdentifier.setLocal(rs.getString("int_local"));
					affiliateIdentifier.setState(rs.getString("aff_stateNat_type"));
					affiliateIdentifier.setCouncil(rs.getString("int_council"));
					affiliateIdentifier.setSubUnit(rs.getString("aff_subUnit"));
					mr.setTheAffiliateIdentifier(affiliateIdentifier);
        		}
			}
			else if(columnName.equals("person_nm")) {
        		mr.setPrefixNm(rs.getString("prefix_nm"));
        		mr.setPersonNm(rs.getString("person_nm"));
			}
			else if(columnName.equals("pa.lst_mod_dt")) {
				mr.setAddrUpdatedDt(rs.getTimestamp("lst_mod_dt"));
			}
			else if(columnName.equals("pa.lst_mod_user_pk")) {
				mr.setAddrUpdatedByInt(new Integer(rs.getInt("lst_mod_user_pk")));
			}
        	else if(columnName.equals("p.nick_nm")) {
        		mr.setNickNm(rs.getString("nick_nm"));
			}
       		else if(columnName.equals("p.alternate_mailing_nm")) {
       			mr.setAltMailingNm(rs.getString("alternate_mailing_nm"));
			}
        	else if(columnName.equals("p.ssn")) {
        		mr.setSsn(rs.getString("ssn"));
			}
        	else if(columnName.equals("p.valid_ssn_fg")) {
        		mr.setValidSsn(DBUtil.getBooleanFromShort(rs.getShort("valid_ssn_fg")));
			}
			else if(columnName.equals("am.mbr_type")) {
        		mr.setMbrType(rs.getString("mbr_type"));
			}
        	else if(columnName.equals("am.mbr_status")) {
        		mr.setMbrStatus(rs.getString("mbr_status"));
			}
        	else if(columnName.equals("mail")) {
        		mr.setNoCardsFg(DBUtil.getBooleanFromShort(rs.getShort("no_cards_fg")));
        		mr.setNoMailFg(DBUtil.getBooleanFromShort(rs.getShort("no_mail_fg")));
        		mr.setNoPublicEmpFg(DBUtil.getBooleanFromShort(rs.getShort("no_public_emp_fg")));
        		mr.setNoLegislativeMailFg(DBUtil.getBooleanFromShort(rs.getShort("no_legislative_mail_fg")));
			}
        	else if(columnName.equals("am.lst_mod_dt")) {
        		mr.setLstModDt(rs.getTimestamp("lst_mod_dt_mbr"));
			}
			else if(columnName.equals("phone")) {
				mr.setCountryCode(rs.getString("country_cd"));
				mr.setAreaCode(rs.getString("area_code"));
				mr.setPhoneNumber(rs.getString("phone_no"));
			}
			else if(columnName.equals("email")) {
				mr.setPersonEmailAddr(rs.getString("person_email_addr"));
			}
		}
		// no column for person_pk
		mr.setPersonPk(new Integer(rs.getInt("person_pk")));
		return mr;
    }

    /* Dynamically create join clause based on MemberCriteria object*/
    private String createJoinForMemberSearch(org.afscme.enterprise.member.MemberCriteria memberCriteria) {
		StringBuffer join = new StringBuffer();
		if(memberCriteria.needAffiliateJoin()) {
			join.append(memberCriteria.getAffiliateLeftOrInner());
			join.append(SQL_JOIN_AFFS);
		}
		if(memberCriteria.needEmailJoin()) {
			join.append(memberCriteria.getEmailLeftOrInner());
			join.append(SQL_JOIN_EMAIL);
		}
		if(memberCriteria.needPhoneJoin()) {
			join.append(memberCriteria.getPhoneLeftOrInner());
			join.append(SQL_JOIN_PHONE);
		}
		if(memberCriteria.needAddressJoin()) {
			join.append(memberCriteria.getAddressLeftOrInner());
			join.append(SQL_JOIN_ADDRESS);
		}
		return join.toString();
	}




    /**
     *
     * Takes a collection of Strings, representing column names and returns a
     * single String which represents the Select List for the
     * search
     *
     *
     *
     * @param memberCriteria criteria for the search
     * @return String - the select list for the query
     *
     *
     */
    private String createSelectForMemberSearch(org.afscme.enterprise.member.MemberCriteria memberCriteria) {

        ArrayList colList = (ArrayList) memberCriteria.getSelectList();
        int size = colList.size();
        int counter = 0;

        Iterator i = colList.iterator();
        StringBuffer select = new StringBuffer("SELECT am.person_pk,");
	boolean nameAppended = false;
       	while(i.hasNext()) {
            String columnName = (String)i.next();          
            
            if(columnName.equals("am.primary_information_source")) {
                    select.append(SQL_PRIMARY_INFORMATION_SOURCE_DESC);
            }
            else if(columnName.equals("am.mbr_type")) {
                    select.append(SQL_MEMBER_TYPE_DESC);
            }
            else if(columnName.equals("am.mbr_status")) {
                    select.append(SQL_MEMBER_STATUS_DESC);
            }
            else if(columnName.equals("am.lst_mod_user_pk")) {
                    select.append(SQL_USER_ID);
            }
            else if(columnName.equals("am.lst_mod_dt")) {
                    select.append(columnName + " AS lst_mod_dt_mbr");
            }
            else if(columnName.equals("sma")) {
                    select.append(SQL_ADDRESS);
            }
            else if(columnName.equals("affId")) {
                    select.append(SQL_AFF_ID);
            }
            else if(columnName.equals("mail")) {
                    select.append(SQL_MAIL);
            }
            else if(columnName.equals("person_nm")) {
                    select.append(SQL_FULL_NAME);
                    nameAppended = true;
            }
            else if(columnName.equals("phone")) {
                    select.append(SQL_PHONE);
            }
            else if(columnName.equals("email")) {
                    select.append(SQL_EMAIL);
            }
            else {
                    select.append(columnName);
            }
            // do not append comma for last column in list
            if(++counter != size) {
                    select.append(",");
            }
        }
        // name must be appended, even if it isn't being displayed since sorting is based on it
        if(!nameAppended) {
                select.append("," + SQL_FULL_NAME);
        }

        return select.toString();
    }

    /**
     * Get the values out of the memberCriteria object and create a where clause
     */
    private void buildMemberWhereClause(PreparedStatementBuilder pb, MemberCriteria mc) {
        // Decided not to set the affPk through the affiliate finnder - if aff_pk was set through the affiliate finder
       // pb.addCriterion("a.aff_pk", mc.getAffPk());


        //   if (mc.getAffPk()==null) {
            pb.addCriterion("a.aff_type", mc.getAffType());
            pb.addCriterion("a.aff_localSubChapter", mc.getAffLocalSubChapter());
            pb.addCriterion("a.aff_stateNat_type", mc.getAffStateNatType());
            pb.addCriterion("a.aff_subUnit", mc.getAffSubUnit());
            pb.addCriterion("a.aff_councilRetiree_chap", mc.getAffCouncilRetireeChap());
            pb.addCriterion("a.aff_code", mc.getAffCode());
       // }
        pb.addCriterion("p.prefix_nm", mc.getPrefixNm());
        // check for wildcard search, i.e. % at end of string
        if ((mc.getFirstNm() != null) && (mc.getFirstNm().indexOf("%") > 0))
        {
                pb.addCriterion("p.first_nm", mc.getFirstNm(), true);
         }
         else {
                pb.addCriterion("p.first_nm", mc.getFirstNm());
         }
        pb.addCriterion("p.middle_nm", mc.getMiddleNm());
        // check for wildcard search. i.e % at end of string
        if ((mc.getLastNm() != null) && (mc.getLastNm().indexOf("%") > 0))
        {
                pb.addCriterion("p.last_nm", mc.getLastNm(), true);
        }
        else {
            pb.addCriterion("p.last_nm", mc.getLastNm());
        }
        pb.addCriterion("p.suffix_nm", mc.getSuffixNm());
        pb.addCriterion("p.nick_nm", mc.getNickNm());
        pb.addCriterion("p.ssn", mc.getSsn());
        // empty or null should evaluate to the UI selection of ALL
        if (mc.getValidSsn() == 1 || mc.getValidSsn() ==0) // 1 is true, 0 is false, 2 means both valid and ssn (so omit from where clause)
         pb.addCriterion("p.valid_ssn_fg", new Integer(mc.getValidSsn()));

        pb.addCriterion("p.alternate_mailing_name", mc.getAltMailingNm());

        pb.addCriterion("pa.addr1", mc.getAddress1());
        pb.addCriterion("pa.addr2", mc.getAddress2());
        pb.addCriterion("pa.city", mc.getCity());
        pb.addCriterion("pa.state", mc.getState());
        pb.addCriterion("pa.county", mc.getCounty());
        pb.addCriterion("pa.country", mc.getCountry());
        pb.addCriterion("pa.zipcode", mc.getZipCode());
        pb.addCriterion("pa.province", mc.getProvince());
        pb.addCriterion("pa.lst_mod_user_pk", mc.getAddrUpdatedBy());
        pb.addCriterion("pa.lst_mod_dt", mc.getAddrUpdatedDt());

        pb.addCriterion("am.mbr_type", mc.getMbrType());
        pb.addCriterion("am.mbr_status", mc.getMbrStatus());
        pb.addCriterion("am.person_pk", mc.getPersonPk());

        pb.addCriterion("am.mbr_card_sent_dt", mc.getMbrCardSentDt());
        pb.addCriterion("am.lst_mod_user_pk", mc.getLstModUserPk());

		if (mc.getLstModDt() != null)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(new java.util.Date(mc.getLstModDt().getTime()));
			int yyyy = c.get(Calendar.YEAR);
			int mm = c.get(Calendar.MONTH) + 1;
			int dd = c.get(Calendar.DATE);
			pb.addCriterion("DATEPART(yyyy, am.lst_mod_dt)", new Integer(yyyy));		
			pb.addCriterion("DATEPART(mm, am.lst_mod_dt)", new Integer(mm));		
			pb.addCriterion("DATEPART(dd, am.lst_mod_dt)", new Integer(dd));				
		}else{
			pb.addCriterion("am.lst_mod_dt", mc.getLstModDt());	
		}
		        
        // empty or null for these flag fields should aevaluate to the UI selection of All
        pb.addCriterion("am.no_mail_fg", mc.getNoMailFg());
        pb.addCriterion("am.no_cards_fg", mc.getNoCardsFg());
        pb.addCriterion("am.no_legislative_mail_fg", mc.getNoLegislativeMailFg());
        pb.addCriterion("am.no_public_emp_fg", mc.getNoPublicEmpFg());

        // only return member who have not been deactivated - not a search criteria, fixed
        pb.addCriterion("p.marked_for_deletion_fg", new Integer(0));

        // phones and email address
        pb.addCriterion("pp.country_cd", mc.getCountryCode());
        pb.addCriterion("pp.area_code", mc.getAreaCode());
        pb.addCriterion("pp.phone_no", mc.getPhoneNumber());
        pb.addCriterion("pe.person_email_addr", mc.getPersonEmailAddr());

        /* The following is performed in order to support data level access control in the
         * view data utility - adds an in clause if access to member search is through the view
         * data utility
         */
         if (mc.getVduAffiliates() != null)
         {
            log.debug("MaintainMembersBean.buildMemberWhereClause get ");
            Criterion criterion = new Criterion();
            criterion.setField("a.aff_pk");
            criterion.setOperator("IN");
            criterion.setType(Types.INTEGER);
            criterion.setValues(new LinkedList(mc.getVduAffiliates()));
            pb.addCriterion(criterion);
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
          pb.addCriterion("pp.phone_type", Codes.PersonPhoneType.HOME);
          pb.addCriterion("pe.email_type", Codes.EmailType.PRIMARY);
          */

        log.debug("buildMemberWhereClause, after add criterion" +
            pb.getPreparedStatementSQL("NoneYet", true) ) ;

    }

    /**
     * Updates the affiliate specific data for one affiliate for a member
     *
     * If the member type is changed from Agency Fee Payer to an eligible member type
     * ('Regular', 'Union Shop Member', ('Retiree', 'Retiree Spouse' - under certain conditions)
     * then they should be added to the weekly card run table - waiting for these rules to be
     * unscrambled
     *
     * Apply business rules associated with Member Type: TBW
     *
     * Also will change return to int and write error codes
     *
     * @param MemberAffiliateData memberAffData - new Member Affiliate Data
     * @param Integer userPk - the userPk of the user making the update
     * @return 'true' if update completes, and 'false' otherwise.
     *
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public boolean updateMemberAffiliateData    (MemberAffiliateData memberAffData, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;

        //Perform Member Type oriented business rules here
        Integer mbrType = memberAffData.getMbrType();
        if (mbrType == Codes.MemberType.A)
        {
           memberAffData.setNoCardsFg(new Boolean(true));
           memberAffData.setNoLegislativeMailFg(new Boolean(true));
        }
        if (mbrType == Codes.MemberType.O)
        {
           memberAffData.setNoCardsFg(new Boolean(true));
        }
        if (mbrType == Codes.MemberType.S)
        {
           memberAffData.setNoPublicEmpFg(new Boolean(true));
        }

        //updates Affiliate Information data to the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_MEMBER_AFF_DATA);

            ps.setInt(1, memberAffData.getMbrType().intValue());
            ps.setInt(2, memberAffData.getMbrStatus().intValue());
            ps.setTimestamp(3, memberAffData.getMbrJoinDt());
            DBUtil.setNullableBooleanAsShort(ps, 4, memberAffData.getLostTimeLanguageFg());

            DBUtil.setNullableInt(ps, 5, memberAffData.getMbrDuesType());
            DBUtil.setNullableDouble(ps, 6, memberAffData.getMbrDuesRate());
            DBUtil.setNullableInt(ps, 7, memberAffData.getMbrDuesFrequency());
            DBUtil.setNullableTimestamp(ps, 8, memberAffData.getMbrRetiredDt());

            DBUtil.setNullableBooleanAsShort(ps, 9, memberAffData.getMbrRetRenewalDuesFg());
            DBUtil.setNullableBooleanAsShort(ps, 10, memberAffData.getNoCardsFg());
            DBUtil.setNullableBooleanAsShort(ps, 11, memberAffData.getNoMailFg());
            DBUtil.setNullableBooleanAsShort(ps, 12, memberAffData.getNoPublicEmpFg());
            DBUtil.setNullableBooleanAsShort(ps, 13, memberAffData.getNoLegislativeMailFg());
            ps.setInt(14, userPk.intValue());
            ps.setInt(15, memberAffData.getPersonPk().intValue());
            ps.setInt(16, memberAffData.getAffPk().intValue());

            int rt = ps.executeUpdate();
            if (rt ==1) {

                // update RPT_Aff_MBr_Activity with the update activity for this affiliate
                this.updateAffMbrActivity(memberAffData.getAffPk(), Codes.ActivityType.U, 1);

                // if appropriate, call add to weekly card run (need info on business rules)
                this.addToWeeklyCardRun(memberAffData.getPersonPk(), memberAffData.getAffPk(), memberAffData);

                return true;

            }
            else {
                return false;
            }
        } catch (SQLException e) {
            throw new EJBException("SQL Error occurred in MaintainMembersBean.updateMemberAffiliateData method ", e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }

    } //


    /**
     *
     * Updates the member detail data for a member.
     *
     * This method first gets the PersonData object from the MemberData object
     * and passes it to MaintainPersonsBean.updatePersonDetail() method to update
     * It then updates the member specific data in the Person table, namely
     * the mbr_barred_fg and mbr_expelled_dt. It does not change the lst_mod_dt
     * in the Person table no lst_mod_user_pk as these will have just been set by
     * the updatePersonDetail() method
     *
     * @param MemberData - memberData data from the UI to use in processing the update
     * @param Integer - userPk - identifier of the person making the update
     * @return 0 if update completes. Error Code if update fails.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int updateMemberDetail(org.afscme.enterprise.member.MemberData memberData, Integer userPk) {

        Connection con = null;
        PreparedStatement ps = null;


        try {


            // first, get the person data and call MaintainPersonsBean to update data

            m_persons.updatePersonDetail(userPk, memberData.getThePersonData());


            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_MEMBER_DETAIL);
            ps.setTimestamp(1, memberData.getMbrExpelledDt());

            
            DBUtil.setNullableBooleanAsShort(ps, 2, memberData.getMbrBarredFg());
            
            if(memberData.getMbrExpelledDt() != null) { // not a nullable field

            	// change member status for all member/affiliates to inactive
            	Collection memberAffiliateResults = getMemberAffiliation(memberData.getThePersonData().getPersonPk());
            	Iterator iterator = memberAffiliateResults.iterator();
                while(iterator.hasNext()) {
					MemberAffiliateResult mar = (MemberAffiliateResult)iterator.next();
					MemberAffiliateData memberAffiliateData = getMemberAffiliateData(memberData.getThePersonData().getPersonPk(), mar.getAffPk());
					memberAffiliateData.setMbrStatus(Codes.MemberStatus.I);
					updateMemberAffiliateData(memberAffiliateData, userPk);
				}

				
			}
            
            ps.setInt(3, memberData.getThePersonData().getPersonPk().intValue());

            int rt = ps.executeUpdate();

            /**
           not needed unless this code needs to return error codes

           if (rt <= 0) {
                return MemberErrorCodes.MEMBER_DETAIL_UPDATE_FAILED;
            }
            else {
                return 0; // good result
            }
             */

        }

        catch (SQLException e) {
            throw new EJBException
            ("SQL Error occurred in MaintainMembersBean.updateMemberDetail" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

        return 0;
    }

    /**
     * Updates the RPT_Aff_Mbr_Activity table
     *
     *
     * @param int activityType - type of activity being recorded (add, update, inactivate)
     * @return void
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */

    public void updateAffMbrActivity(Integer affPk, Integer activityType, int numberOf) {

        Connection con = null;
        PreparedStatement ps = null;
        int month = 0;
        int year = 0000;
        Integer timePk = null;
        GregorianCalendar current = new GregorianCalendar();

        // determine date, then get time key
        // get int representations of month and year - java.util.GregorianCalendar
        // Note java.util.GregorianCalendar indicates it returns months as 0-11
        month = current.get(current.MONTH);
        year = current.get(current.YEAR);
        // increment by one to get human month numbering
        timePk = getTimePk((month + 1), year);

        /** if the RPT_Aff_Mbr_Activity table does not have a record that represents this
         * month/year date combination for this activity type for this affiliate, then a row
         * needs to be inserted into the table for this combination - set to zero
         * initially and then let update add to zero (null + 1) = unknown
         */

        insertRowIntoAffMbrActivityIfNeeded(affPk, timePk, activityType);

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AFF_MEMBER_ACTIVITY);
            ps.setInt(1, numberOf);
            ps.setInt(2, affPk.intValue());
            ps.setInt(3, timePk.intValue());
            ps.setInt(4, activityType.intValue());

            int rt = ps.executeUpdate();

        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Ocurred in MaintainMembersBean.updateAffMbrActivity" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    } // updateAffMbrActivity

    /**
     * Constructor for MaintainMembersBean
     */
    public MaintainMembersBean    () {

    }

    /**
	 * @param Integer personPk Person Primary Key
	 * @param Integer affPk Affiliate Primary Key
	 * @return boolean true means already on, false means not on weekly card run
	 *
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Required"
	 */
	 public boolean isAlreadyOnWeeklyCardRun (Integer personPk, Integer affPk)
	 {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try
            {
		con = DBUtil.getConnection();
	        ps = con.prepareStatement(SQL_IS_MEMBER_ALREADY_ON_CARD_RUN);
	        ps.setInt(1, personPk.intValue());
	        ps.setInt(2, affPk.intValue());
	        rs = ps.executeQuery();
	        rs.next();
	        int mcount = rs.getInt("mcount");
	        if (mcount == 0) return false;
	        else return true;
	    } catch (SQLException e) {
	      	throw new EJBException("SQL Error Occurred in MaintainMembersBean.isAlreadyOnWeeklyCardRun " + e);
	    }
		finally {
	    	DBUtil.cleanup(con, ps, rs);
	    }
    }

    /**
     * Retrieves the vendor members based on the search criteria
     *
     * @param MemberCriteria - criteria for the search
     * @param ArrayList - results passed by reference, contains results of search
     * @return int - the number of records returned
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int searchVendorMembers(MemberCriteria mc, ArrayList results) {
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBUtil.getConnection();

            // Create the where clause using PreparedStatementBuilder
            if ((mc.getFirstNm() != null) && (mc.getFirstNm().indexOf("%") > 0)) {
                builder.addCriterion("p.first_nm", mc.getFirstNm(), true);
            } else {
                builder.addCriterion("p.first_nm", mc.getFirstNm());
            }
            builder.addCriterion("p.middle_nm", mc.getMiddleNm());
            if ((mc.getLastNm() != null) && (mc.getLastNm().indexOf("%") > 0)) {
                builder.addCriterion("p.last_nm", mc.getLastNm(), true);
            } else {
                builder.addCriterion("p.last_nm", mc.getLastNm());
            }
            builder.addCriterion("am.person_pk", mc.getPersonPk());
            builder.addCriterion("p.ssn", mc.getSsn());
            builder.addCriterion("pa.city", mc.getCity());
            builder.addCriterion("pa.state", mc.getState());
            builder.addCriterion("pa.zipcode", mc.getZipCode());
            builder.addCriterion("pa.zip_plus", mc.getZipPlus());
            builder.addCriterion("a.aff_type", mc.getAffType());
            builder.addCriterion("a.aff_localSubChapter", mc.getAffLocalSubChapter());
            builder.addCriterion("a.aff_stateNat_type", mc.getAffStateNatType());
            builder.addCriterion("a.aff_subUnit", mc.getAffSubUnit());
            builder.addCriterion("a.aff_councilRetiree_chap", mc.getAffCouncilRetireeChap());
            builder.addCriterion("a.aff_code", mc.getAffCode());
            builder.addCriterion("psma.current_fg", "1");

            // add the rest of the SQL to create the count query
            ps = builder.getPreparedStatement(SQL_SELECT_COUNT_SEARCH_MEMBERS + SQL_JOIN_SEARCH_VENDOR_MEMBERS, con);
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);

            if (count == 0)
                return 0;

            rs.close();
            rs = null;
            ps.close();
            ps = null;

            /**
             * Now create the query to retrieve the results (if > 0 rows returned)
             * - first set the order by from the criteria data
             */
            if (!TextUtil.isEmptyOrSpaces(mc.getOrderBy())) {
                StringBuffer sb = new StringBuffer("");

                // need to handle orderby of council and local by cast to retain
                // numeric ordering - also handled in set of criteria object for user
                // selection
                sb.append(mc.getOrderBy());
                if (mc.getOrdering() < 0) {
                    sb.append(" DESC ");
                } else {
                    sb.append(" ASC ");
                }
                builder.addOrderBy(sb.toString().trim());
            }
            else {
                builder.addOrderBy(" person_nm ASC ");
                builder.addOrderBy(" a.aff_stateNat_type ASC ");
                builder.addOrderBy(" int_council ASC ");
                builder.addOrderBy(" int_local ASC ");
            }

            // create the query for the results
            ps = builder.getPreparedStatement(SQL_SELECT_SEARCH_VENDOR_MEMBERS + SQL_JOIN_SEARCH_VENDOR_MEMBERS, con);
            rs = ps.executeQuery();

            // position the first row, based on the page requested and the page size
            if (mc != null)
                rs.absolute((mc.getPage() * mc.getPageSize()) + 1);
            else
                rs.next();

            // put the results into a the list of memberResult objects
            int index = 0;
            int pageSize = (mc != null) ? mc.getPageSize() : 0;
            int startIndex = (mc == null) ? 0 : mc.getPage() * pageSize;
            while (index+startIndex < count && (mc == null || index < pageSize)) {
                // Parse result set
                MemberResult mbrResult = new MemberResult();
                mbrResult.setPersonPk(new Integer(rs.getInt("person_pk")));
                mbrResult.setMbrStatus(rs.getString("mbr_status"));
                mbrResult.setPersonNm(rs.getString("person_nm"));
                Integer affPk = DBUtil.getIntegerOrNull(rs, "aff_pk");
                if (affPk == null || affPk.intValue() < 1) {
                    mbrResult.setTheAffiliateIdentifier(null);
                } else {
                    mbrResult.setTheAffiliateIdentifier(m_affiliates.getAffiliateData(affPk).getAffiliateId());
                }
                mbrResult.setAddress(rs.getString("address"));
                mbrResult.setCity(rs.getString("city"));
                mbrResult.setState(rs.getString("state"));
                if (!TextUtil.isEmptyOrSpaces(rs.getString("zipcode")) &&
                    !TextUtil.isEmptyOrSpaces(rs.getString("zip_plus")))
                    mbrResult.setZipCode(rs.getString("zipcode")+"-"+rs.getString("zip_plus"));
                else
                    mbrResult.setZipCode(rs.getString("zipcode"));
                mbrResult.setMbrType(rs.getString("mbr_type"));
                
                results.add(mbrResult);
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
     * Retrieves the Member Status for a Member.
     *
     * @param personPk the Person Primary Key
     *
     * @return int the member status
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public int getMemberStatus(Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mbrStatus = 0;

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MBR_STATUS);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                mbrStatus = rs.getInt(1);
            }
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainMembersBean.getMemberStatus method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return mbrStatus;
    }

} // end class
