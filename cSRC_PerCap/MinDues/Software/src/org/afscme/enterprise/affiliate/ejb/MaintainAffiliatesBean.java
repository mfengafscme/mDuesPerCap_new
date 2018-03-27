package org.afscme.enterprise.affiliate.ejb;

// AFSCME Enterprise Imports
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.affiliate.officer.*;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.Codes.*;
import org.afscme.enterprise.organization.ejb.*;
import org.afscme.enterprise.codes.ejb.*;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAddressData;
import org.afscme.enterprise.organization.OrgPhoneData;
import org.afscme.enterprise.users.ejb.*;

// Java Imports
import java.io.File;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.*;
import java.sql.Types;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

// Other Imports
import org.apache.log4j.Logger;

/**
 * Encapsulates access to affiliate data.
 *
 * @ejb:bean name="MaintainAffiliates" display-name="MaintainAffiliates"
 * jndi-name="MaintainAffiliates"
 * type="Stateless" view-type="local"
 */
public class MaintainAffiliatesBean extends SessionBase {

    static Logger logger = Logger.getLogger(MaintainAffiliatesBean.class);

    /** Reference to the MaintainOrganizations EJB */
    private MaintainOrganizations orgsBean;

    /** Reference to the MaintainCodes EJB */
    private MaintainCodes codesBean;

    private MaintainUsers usersBean;

    private static String CHANGE_HISTORY_VALUE_EMPTY = " ";

    /** Inserts a new Affiliate */
    private static final String SQL_INSERT_NEW_AFFILIATE =
        "INSERT INTO Aff_Organizations (parent_aff_fk, aff_type, " +
        "       aff_localSubChapter, aff_stateNat_type, aff_subUnit, " +
        "       aff_councilRetiree_chap, aff_code, aff_abbreviated_nm, " +
        "       aff_status, aff_afscme_region, aff_mult_employers_fg, " +
        "       aff_afscme_leg_district, aff_sub_locals_allowed_fg, " +
        "       aff_ann_mbr_card_run_group, created_user_pk, created_dt, " +
        "       lst_mod_user_pk, lst_mod_dt, aff_pk, mbr_allow_view_fg, " +
        "       mbr_allow_edit_fg) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /** Selects an Affiliate's PK. Where clause should be built dynamically. */
    private static final String SQL_SELECT_AFF_PK_FOR_AFF_ID =
        "SELECT aff_pk " +
        "FROM   Aff_Organizations ";

    /** Selects a 0 or 1 based on if an AffiliateIdentifier already exists in the database. */
    private static final String SQL_SELECT_AFFILIATE_ID_EXISTS =
        "SELECT CASE " +
        "           WHEN Count(aff_pk) = 1 " +
        "           THEN 1 " +
        "           ELSE 0 " +
        "       END id_exists " +
        "FROM   Aff_Organizations " +
        "WHERE  aff_type=? AND aff_code=? AND aff_localSubChapter=? AND " +
        "       aff_stateNat_type=? AND aff_subUnit=? AND aff_councilRetiree_chap=?";

    /** Searches for Affiliates matching the given criteria. */
    private static final String SQL_SEARCH_AFFILIATE_OLD =
        "SELECT DISTINCT a.aff_pk aff_pk, a.parent_aff_fk parent_aff_fk, a.aff_type aff_type, " +
        "       CAST(a.aff_localSubChapter AS int) aff_localSubChapter, " +
        "       a.aff_stateNat_type aff_stateNat_type, " +
        "       CAST(a.aff_subUnit AS int) aff_subUnit, " +
        "       CAST(a.aff_councilRetiree_chap AS int) aff_councilRetiree_chap, " +
        "       a.aff_code aff_code, a.aff_abbreviated_nm aff_abbreviated_nm " +
        "FROM   Aff_Organizations a " +
        "       LEFT OUTER JOIN Aff_Organizations n " +
        "           ON n.aff_pk=a.new_aff_id_src " +
        "       LEFT OUTER JOIN Aff_Employer_Sector es " +
        "           ON a.aff_pk=es.aff_pk " +
        "       LEFT OUTER JOIN Org_Locations l " +
        "           ON a.aff_pk = l.org_pk " +
        "       LEFT OUTER JOIN Org_Address adr " +
        "           ON adr.org_locations_pk = l.org_locations_pk " +
        "       LEFT OUTER JOIN  Org_Phone offph " +
        "           ON offph.org_locations_pk = l.org_locations_pk " +
        "           AND offph.org_phone_type=73001 " +
        "       LEFT OUTER JOIN Org_Phone faxph " +
        "           ON faxph.org_locations_pk = l.org_locations_pk " +
        "           AND faxph.org_phone_type=73002 ";

    /** Searches for employer matching the given criteria. */
    private static final String SQL_SEARCH_AFFILIATE =
        "SELECT DISTINCT a.empAffPk empAffPk, " +
        "       a.local+0 local, " +
        "       a.type type, " +
        "       a.state state, " +
        "       a.chapter chapter, " +
        "       a.council council, " +
        "       a.curr_employer_name, " +
        "       a.active " +
        "FROM   v_MDU_Emp_Aff a ";

    private static final String SQL_SEARCH_PRE_AFFILIATE =
        "SELECT DISTINCT a.empAffPk empAffPk, " +
        "       a.aff_fk aff_fk, " +
        "       a.Batch_ID batch_id, " +
	"       a.C local, " +
        "       a.A state, " +
        "       a.D chapter, " +
        "       a.B council, " +
        "       a.E curr_employer_name " +
        "FROM   Sample_Minimum_Dues_2018_Data_Analytics_TEST_2 a ";

    /** Searches for employer matching the given criteria. */
    private static final String SQL_SEARCH_EMPLOYER_COUNT =
        "SELECT DISTINCT COUNT(*) FROM v_MDU_Emp_Aff a ";

    private static final String SQL_SEARCH_AFFILIATE_COUNT =
        "SELECT COUNT(*) " +
        "FROM   v_MDU_Emp_Aff a ";

    private static final String SQL_SEARCH_AFFILIATE_COUNT_OLD =
        "SELECT COUNT(DISTINCT a.aff_pk) " +
        "FROM   Aff_Organizations a " +
        "       LEFT OUTER JOIN Aff_Organizations n " +
        "           ON n.aff_pk=a.new_aff_id_src " +
        "       LEFT OUTER JOIN Aff_Employer_Sector es " +
        "           ON a.aff_pk=es.aff_pk " +
        "       LEFT OUTER JOIN Org_Locations l " +
        "           ON a.aff_pk = l.org_pk " +
        "       LEFT OUTER JOIN Org_Address adr " +
        "           ON adr.org_locations_pk = l.org_locations_pk " +
        "       LEFT OUTER JOIN  Org_Phone offph " +
        "           ON offph.org_locations_pk = l.org_locations_pk " +
        "           AND offph.org_phone_type=73001 " +
        "       LEFT OUTER JOIN Org_Phone faxph " +
        "           ON faxph.org_locations_pk = l.org_locations_pk " +
        "           AND faxph.org_phone_type=73002 ";

    /** Inserts into the Org_Parent table */
    private static final String SQL_INSERT_ORG_PARENT =
        "INSERT INTO Org_Parent (org_subtype) VALUES (?)";

    /** Updates the Aff_Organizations table */
    private static final String SQL_UPDATE_AFFILIATE_DETAIL =
        "UPDATE Aff_Organizations " +
        "SET    aff_abbreviated_nm=?, aff_afscme_region=?, " +
    	"		aff_afscme_leg_district=?, " +
        "       aff_mult_employers_fg=?, aff_sub_locals_allowed_fg=?, " +
        "       aff_ann_mbr_card_run_group=?, mbr_renewal=?, aff_web_url=?, " +
        "       aff_multiple_offices_fg=?, lst_mod_dt=getDate(), " +
        "       lst_mod_user_pk=? " +
        "WHERE aff_pk=?";

    /** Select 0 or 1 indicating if an Affiliate has Sub Affiliates. */
    private static final String SQL_SELECT_HAS_SUB_AFFILIATES =
        "SELECT CASE " +
        "           WHEN Count(aff_pk) > 0 " +
        "           THEN 1 " +
        "           ELSE 0 " +
        "       END has_subs " +
        "FROM   Aff_Organizations " +
        "WHERE  parent_aff_fk=?";

    /** Select 0 or 1 indicating if an Affiliate has Sub Affiliates. Needed extra join to get aff_type = 'L'. */
    private static final String SQL_SELECT_HAS_SUB_LOCALS =
        "SELECT	CASE " +
        "           WHEN Count(parent.aff_pk) > 0 " +
        "               THEN 1 " +
        "               ELSE 0 " +
        "       END has_subs " +
        "FROM   Aff_Organizations parent JOIN Aff_Organizations child " +
        "       ON parent.aff_pk = child.parent_aff_fk" +
        "WHERE  parent.aff_type = 'L' AND child.parent_aff_fk=?";

    /** Selects an Affiliate Parent based on it's primary key. */
    private static final String SQL_SELECT_PARENT_AFF_PK =
        "SELECT parent_aff_fk " +
        "FROM Aff_Organizations " +
        "WHERE aff_pk=?";

    /** Updates an Affiliate Parent. */
    private static final String SQL_UPDATE_PARENT_AFF_PK =
        "UPDATE Aff_Organizations " +
        "SET    parent_aff_fk=?, lst_mod_dt=getDate(), lst_mod_user_pk=? " +
        "WHERE  aff_pk=?";

    /** Updates the Council Number and the Code for an Affiliate's Identifier */
    private static final String SQL_UPDATE_COUNCIL_NUMBER_AND_CODE =
        "UPDATE Aff_Organizations " +
        "SET    aff_councilRetiree_chap=?, aff_code=?, lst_mod_dt=getDate(), " +
        "       lst_mod_user_pk=? " +
        "WHERE  aff_pk=?";

    /** Selects an Affiliate based on it's primary key. */
    private static final String SQL_SELECT_AFFILIATE_DETAIL =
        "SELECT a.aff_abbreviated_nm, a.aff_pk, a.aff_code, " +
        "       a.aff_councilRetiree_chap, a.aff_localSubChapter, " +
        "       a.aff_stateNat_type, a.aff_subUnit, a.aff_type, " +
        "       a.aff_afscme_leg_district, a.aff_afscme_region, " +
        "       a.aff_sub_locals_allowed_fg, a.mbr_allow_edit_fg, " +
        "       a.mbr_allow_view_fg, a.mbr_yearly_card_run_done_fg, " +
        "       a.aff_ann_mbr_card_run_group, a.old_aff_unit_cd_legacy, " +
        "       a.old_aff_no_other, a.mbr_renewal, a.aff_mult_employers_fg, " +
        "       a.aff_multiple_offices_fg, a.new_aff_id_src, a.parent_aff_fk, " +
        "       a.aff_status, a.aff_web_url, a.lst_mod_dt, a.lst_mod_user_pk, " +
        "       a.created_user_pk, a.created_dt " +
        "FROM   Aff_Organizations a " +
        "WHERE a.aff_pk=?";

     private static final String SQL_SELECT_PRE_AFFILIATE_DETAIL =
        "SELECT a.A state, a.B council, a.C local, a.D chapter, a.E employerNm, " +
        "       a.F agmtEffDate, a.G agmtExpDate, " +
        "       a.H noMemFeePayer, a.I ifRecInc, a.J ifInNego, " +
        "       a.K percentWageInc, a.L wageIncEffDate, " +
        "       a.M noMemFeePayerAff1, a.O centPerHrDoLumpSumBonus, " +
        "       a.P avgWagePerHrYr, a.Q effDateInc, " +
        "       a.R noMemFeePayerAff2, a.S speWageAgj, " +
        "       a.T percentInc , a.V dollarCent, a.W avgPay, " +
        "       a.X noMemFeePayerAff3, a.Y contactName, a.Z contactPhoneEmail, " +
        //"       a.AA notes, " +
        ////////////// db field not in excel
        "       a.Load_ID, a.Batch_ID, a.Processed, " +
        "       a.EmpAffPk, a.aff_fk, " +
        "       a.Increase_type, a.statMbrCount, " +
        "       a.MbrsAfps_Affected, a.Adj_MbrsAfps_Affected, " +
        "       a.UserPosting, a.DoNotProcess, " +
        "       a.Comment, a.wifPk, a.widPk " +
	"FROM   Sample_Minimum_Dues_2018_Data_Analytics_TEST_2 a " +
        "WHERE a.Batch_ID=?";

     private static final String SQL_UPDATE_PRE_AFFILIATE_DETAIL =
        " UPDATE  Sample_Minimum_Dues_2018_Data_Analytics_TEST_2 set " +
        "       F=?, G=?, " +
        "       H=?, increase_type=?, mbrsAfps_Affected=?, adj_MbrsAfps_Affected=?, I=?, J=?, " +
        "       Y=?, Z=?, " +
        "       notes=?, K=?, L=?, " +
        "       M=?, O=?, " +
        "       P=?, Q=?, " +
        "       R=?, S=?, " +
        "       T=?, V=?, W=?, " +
        "       X=? " +
        " WHERE Batch_ID=?";


    /** Selects an Affiliate based on it's primary key. */
    private static final String SQL_SELECT_EMPLOYER_DETAIL =
        "SELECT a.Type, a.curr_employer_name, a.State, a.Council, a.Local, a.Chapter, a.active " +
        "FROM   v_MDU_Emp_Aff a " +
        "WHERE a.empAffPk=?";

    private static final String SQL_SELECT_EMPLOYER_EXISTING_YEAR =
        "SELECT distinct a.duesyear " +
        "FROM   mdu_wageincrease_form a " +
        "WHERE a.empAffFk=?";

    /** Queries for a District Council's affiliated Admin Council. */
    private static final String SQL_SELECT_COUNCIL_AFFILIATION_FOR_DIST_CN =
        "SELECT regular_council_aff_pk, admin_legislative_cncl_aff_pk " +
        "FROM Aff_Admin_Councils " +
        "WHERE regular_council_aff_pk=?";

    /** Queries for an Admin Council's affiliated District Councils. */
    private static final String SQL_SELECT_COUNCIL_AFFILIATION_FOR_ADMIN_CN =
        "SELECT regular_council_aff_pk, admin_legislative_cncl_aff_pk " +
        "FROM Aff_Admin_Councils " +
        "WHERE admin_legislative_cncl_aff_pk=?";

    /** Inserts a new District Council/Admin Council affiliation. */
    private static final String SQL_INSERT_COUNCIL_AFFILIATION =
        "INSERT INTO Aff_Admin_Councils " +
        "       (regular_council_aff_pk, admin_legislative_cncl_aff_pk) " +
        "VALUES (?, ?)";

    /** Deletes a District Council's affiliation to an Admin Council. */
    private static final String SQL_DELETE_COUNCIL_AFFILIATION =
        "DELETE Aff_Admin_Councils WHERE regular_council_aff_pk=? ";

    /** Selects all the comments from Aff_Comments for an Affiliate */
    private static final String SQL_SELECT_COMMENTS_FOR_AFFILIATE =
        "SELECT * " +
        "FROM Aff_Comments " +
        "WHERE aff_pk=? " +
        "ORDER BY comment_dt DESC";

    /** Selects the most recent comment from Aff_Comments for an Affiliate */
    private static final String SQL_SELECT_MOST_RECENT_COMMENT_FOR_AFFILIATE =
        "SELECT     comment_txt, comment_dt, created_user_pk " +
        "FROM       Aff_Comments " +
        "WHERE      (aff_pk = ?) " +
        "           AND (comment_dt = " +
        "               (SELECT     MAX(comment_dt) " +
        "               FROM          Aff_Comments " +
        "               WHERE      aff_pk = ?) " +
        "           )";

    /** Inserts a new Comment for an Affiliate */
    private static final String SQL_INSERT_COMMENT_FOR_AFFILIATE =
        "INSERT INTO Aff_Comments " +
        "	(aff_pk, created_user_pk, comment_txt, comment_dt) " +
        "VALUES	(?, ?, ?, getDate())";

    /** Selects all the employer sectors from Aff_Employer_Sector for an Affiliate */
    private static final String SQL_SELECT_EMPLOYER_SECTORS_FOR_AFFILIATE =
        "SELECT aff_employer_sector " +
        "FROM Aff_Employer_Sector " +
        "WHERE aff_pk=?";

    /** Deletes all the employer sectors from Aff_Employer_Sector for an Affiliate */
    private static final String SQL_DELETE_AFF_EMPLOYER_SECTOR =
        "DELETE Aff_Employer_Sector WHERE aff_pk=?";

    private static final String SQL_INSERT_AFF_EMPLOYER_SECTOR =
        "INSERT INTO Aff_Employer_Sector " +
        "           (aff_pk, aff_employer_sector) " +
        "VALUES     (?, ?)";

    /** Inserts Change History information. */
    private static final String SQL_INSERT_AFF_CHANGE_HISTORY =
        "INSERT INTO Aff_Chng_History " +
        "   	(aff_pk, aff_section, chng_user_pk, chng_dt) " +
        "VALUES 	(?, ?, ?, getDate())";

    /** Inserts Change History Detail information. */
    private static final String SQL_INSERT_AFF_CHANGE_HISTORY_DETAIL =
        "INSERT INTO Aff_Chng_History_Dtl " +
        "	(aff_transaction_pk, aff_field_chnged, old_value, new_value) " +
        "VALUES (?, ?, ?, ?)";

    /** Inserts Change History Detail information. */
    private static final String SQL_INSERT_AFF_CHANGE_HISTORY_DETAIL_JURISDICTION =
        "INSERT INTO Aff_Chng_History_Juris_Dtl " +
        "	(aff_transaction_pk, juris_value_old_new, juris_value) " +
        "VALUES (?, ?, ?)";

    /** */
    private static final String SQL_SEARCH_CHANGE_HISTORY =
        "SELECT	distinct aff_pk, aff_section, " +
        "	CAST(CAST(chng_dt AS varchar(12)) AS datetime) AS chng_dt " +
        "FROM 	Aff_Chng_History h  ";

    private static final String SQL_SELECT_CHANGE_HISTORY_DETAIL =
        "SELECT	h.aff_pk, h.aff_section, h.chng_dt, d.aff_field_chnged, " +
        "       d.old_value, d.new_value, h.chng_user_pk " +
        "FROM 	Aff_Chng_History h JOIN Aff_Chng_History_Dtl d " +
        "       ON h.aff_transaction_pk = d.aff_transaction_pk " +
        "WHERE  h.aff_pk=? " +
        "       AND h.aff_section = ? " +
        "       AND CAST(CAST(h.chng_dt AS varchar(12)) AS datetime) = ?";

    private static final String SQL_SELECT_CHANGE_HISTORY_DETAIL_JURISDICTION =
        "SELECT	h.aff_pk, h.aff_section, h.chng_user_pk, h.chng_dt, " +
        "       j.juris_value old_value, j2.juris_value new_value " +
        "FROM 	Aff_Chng_History_Juris_Dtl j JOIN Aff_Chng_History_Juris_Dtl j2 " +
        "       ON j.juris_value_old_new < j2.juris_value_old_new  " +
        "       AND j.aff_transaction_pk=j2.aff_transaction_pk JOIN Aff_Chng_History h  " +
        "       ON j.aff_transaction_pk = h.aff_transaction_pk " +
        "WHERE  h.aff_pk=? " +
        "       AND h.aff_section = ? " +
        "       AND CAST(CAST(h.chng_dt AS varchar(12)) AS datetime) = ?";

    /** Inserts Charter Information. */
    private static final String SQL_INSERT_CHARTER_DATA =
        "INSERT INTO Aff_Charter " +
        "       (aff_pk, charter_nm, charter_juris, charter_dt,  " +
        "       charter_lst_chg_eff_dt, charter_cd, lst_mod_user_pk,  " +
        "       lst_mod_dt, created_dt, created_user_pk) " +
        "VALUES	(?, ?, ?, ?, ?, ?, ?, getDate(), getDate(), ?)";

    /** Inserts Charter County information */
    private static final String SQL_INSERT_CHARTER_COUNTIES =
        "INSERT INTO Aff_Charter_County (aff_pk, county_nm) VALUES (?, ?)";

    /** Selects Charter Information for an Affiliate */
    private static final String SQL_SELECT_CHARTER_DATA =
        "SELECT c.aff_pk, c.charter_cd, c.charter_dt, c.charter_juris, " +
        "       c.charter_lst_chg_eff_dt, c.charter_nm, a.aff_status, a.aff_type " +
        "FROM Aff_Charter c JOIN Aff_Organizations a ON a.aff_pk = c.aff_pk " +
        "WHERE a.aff_pk = ?";

    /** Selects Charter Counties for an Affiliate */
    private static final String SQL_SELECT_CHARTER_COUNTIES =
        "SELECT county_nm FROM Aff_Charter_County WHERE aff_pk = ?";

    /** Updates the Aff_Charter table */
    private static final String SQL_UPDATE_CHARTER_DATA =
        "UPDATE Aff_Charter " +
        "SET    charter_nm=?, charter_juris=?, charter_cd=?, " +
        "       charter_lst_chg_eff_dt=?, charter_dt=?, lst_mod_user_pk=?, " +
        "       created_user_pk=?, lst_mod_dt=getDate(), created_dt=getDate() " +
        "WHERE aff_pk=?";

    /** Deletes all the employer sectors from Aff_Employer_Sector for an Affiliate */
    private static final String SQL_DELETE_CHARTER_COUNTIES =
        "DELETE Aff_Charter_County WHERE aff_pk=?";

    /** Inserts Constitution information */
    private static final String SQL_INSERT_CONSTITUTION_DATA =
        "INSERT INTO Aff_Constitution " +
        "       (aff_pk, aff_agreement_dt, most_current_approval_dt, " +
        "       approved_const_fg, created_dt, created_user_pk, lst_mod_dt, " +
        "       lst_mod_user_pk) " +
        "VALUES	(?, ?, ?, ?, getDate(), ?, getDate(), ?)";

    /** Selects Constitution Information for an Affiliate */
    private static final String SQL_SELECT_CONSTITUTION_DATA =
        "SELECT CASE " +
        "           WHEN    const_doc_file_nm IS NOT NULL AND " +
        "                   const_doc_file_content_type IS NOT NULL AND " +
        "                   aff_constitution_doc IS NOT NULL " +
        "           THEN    1 " +
        "           ELSE    0 " +
        "       END has_document, aff_agreement_dt, off_election_method, " +
        "       auto_delegate_prvsn_fg, const_regions_fg, most_current_approval_dt, " +
        "       meeting_frequency, approved_const_fg, created_dt, created_user_pk, " +
        "       lst_mod_dt, lst_mod_user_pk " +
        "FROM	Aff_Constitution " +
        "WHERE	aff_pk = ?";

    /** Retrieves an Affiliates' Sub Affiliates */
    private static final String SQL_SELECT_SUB_AFFILIATES =
        "SELECT aff_pk " +
        "FROM Aff_Organizations " +
        "WHERE parent_aff_fk=? " +
        "ORDER BY CAST(aff_councilRetiree_chap AS int) ASC, " +
        "           CAST(aff_localSubChapter AS int) ASC, " +
        "           CAST(aff_subUnit AS int) ASC";

    /** Retrieves an Affiliate's  Membership Reporting Info */
    private static final String SQL_SELECT_MEMBERSHIP_REPORTING =
        " SELECT a.aff_status, mr.* " +
        " FROM Aff_Mbr_Rpt_Info mr " +
        " INNER JOIN Aff_Organizations a " +
        " ON a.aff_pk = mr.aff_pk " +
        " WHERE a.aff_pk=? ";

    /** Inserts an Affiliate's  Membership Reporting Info */
    private static final String SQL_INSERT_MEMBERSHIP_REPORTING =
        "INSERT INTO Aff_Mbr_Rpt_Info " +
        "   (aff_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) " +
        "VALUES " +
        "   (?, getDate(), ?, getDate(), ?)";

    /** Updates an Affiliate's  Membership Reporting Info */
    private static final String SQL_UPDATE_MEMBERSHIP_REPORTING =
        " UPDATE Aff_Mbr_Rpt_Info " +
        " SET aff_info_reporting_source=?, unit_wide_no_mbr_cards_fg=?, comment_txt=?, unit_wide_no_pe_mail_fg=?, lst_mod_dt=getdate(), lst_mod_user_pk=? " +
        " WHERE aff_pk=? ";

    /** Updates an Affiliate's Status */
    private static final String SQL_UPDATE_AFFILIATE_STATUS =
        " UPDATE Aff_Organizations " +
        " SET aff_status=? " +
        " WHERE aff_pk=? ";

    /** Inserts an Affiliate's  Financial Information */
    private static final String SQL_INSERT_FINANCIAL_DATA =
        "INSERT INTO Aff_Fin_Info (aff_pk) VALUES (?)";

    /** Selects an Affiliate's  Financial Information */
    private static final String SQL_SELECT_FINANCIAL_DATA =
        "SELECT aff_pk, employer_identification_num, per_cap_stat_avg, " +
        "       per_cap_tax_payment_method, per_cap_last_paid_dt, " +
        "       per_cap_tax_last_mbr_cnt, per_cap_tax_lst_info_upd_dt, " +
        "       comment_txt " +
        "FROM Aff_Fin_Info " +
        "WHERE aff_pk=?";

    /** Updates an Affiliate's  Membership Reporting Info */
    private static final String SQL_UPDATE_FINANCIAL_DATA =
        " UPDATE Aff_Fin_Info " +
        " SET employer_identification_num=? " +
        " WHERE aff_pk=? ";

    private static final String SQL_UPDATE_MEMBER_INACTIVATE =
    	" UPDATE Aff_Members " +
	" SET    mbr_status = " + MemberStatus.I + ", " +
	"        lst_mod_dt = getDate(), " +
        "        lst_mod_user_pk = ? " +
	" WHERE  aff_pk = ? ";

    private static final String SQL_UPDATE_OFFICER_INACTIVATE =
        " UPDATE Officer_History " +
        " SET    pos_end_dt = getDate(), " +
        "        lst_mod_dt = getDate(), " +
        "        lst_mod_user_pk = ? " +
	" WHERE  aff_pk = ?  and " +
        "        pos_end_dt IS null " ;

    private static final String SQL_UPDATE_OFFICER_SUSPEND =
        " UPDATE Officer_History " +
        " SET    suspended_fg = 1, " +
        "        suspended_dt = getDate(), " +
        "        lst_mod_dt = getDate(), " +
        "        lst_mod_user_pk = ? " +
	" WHERE  aff_pk = ? and " +
        "        pos_end_dt IS null and " +
        "        suspended_fg != 1 and " +
        "        afscme_office_pk NOT IN (SELECT afscme_office_pk        " +
        "                        FROM AFSCME_Offices                     " +
        "                        WHERE afscme_title_nm IN (6004, 6005))  " ;

// EJB METHODS

    /** Gets references to the dependent EJBs */
    public void ejbCreate() throws CreateException {
        try {
            orgsBean = JNDIUtil.getMaintainOrganizationsHome().create();
            codesBean = JNDIUtil.getMaintainCodesHome().create();
            usersBean = JNDIUtil.getMaintainUsersHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainAffiliatesBean.ejbCreate()" + e);
        }
    }

    public void ejbRemove() {
        orgsBean = null;
        codesBean = null;
        usersBean = null;
    }

// BUSINESS METHODS

    /**
     * Adds a new Affiliate. The processing includes a check to see if an affiliate with
     * the same identifier already exists. If so, the Affiliate Identifier is automatically
     * adjusted to be unique, by manipulating the value of the code field within the affiliate
     * identifier.
     *
     * Based on the current database design, this method needs to create new rows in
     * Aff_Orgaonization, Aff_Charter, Aff_Constitution, Aff_Charter_County, and
     * Org_Parent tables.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param newAffiliateData Affiliate information to add
     * @return Returns the Primary Key of the newly added Affiliate.
     */
    public int addAffiliate(NewAffiliate newAffiliate, Integer userPk) {
        if (newAffiliate == null) {
            throw new EJBException("No Affiliate information is available to be inserted.");
        }

        Connection con = null;
        PreparedStatement ps = null;
	ResultSet rs = null;
        Integer affPk = null;

	try {
            AffiliateData parent = getAffiliateData(newAffiliate.getParentAffPk());

            // Get the Affiliate ID's Code and set the code on the new Affiliate.
            Character code = getNextAffiliateSequenceCode(newAffiliate.getAffiliateId());
            if (code == null) {
                return AffiliateErrorCodes.ERROR_ADD_AFFILIATE_SEQUENCE_FULL;
            }
            newAffiliate.getAffiliateId().setCode(code);


            switch (newAffiliate.getAffiliateId().getType().charValue()) {
                // if new affiliate is a Sub Local, check that the parent is allowed to have Sub Locals
                case 'U':
                    if (parent != null) {
                        if (!parent.getAllowSubLocals().booleanValue()) {
                            return AffiliateErrorCodes.ERROR_ADDING_SUB_LOCAL_TO_LOCAL_NOT_ALLOWED;
                        }
                    } else {
                        return AffiliateErrorCodes.ERROR_ADDING_SUB_LOCAL_WITH_NO_PARENT;
                    }
                    break;
                // if new affiliate is a Local, make sure that the parent, if exists, is not an Admin Council
                case 'L':
                    if (parent != null && parent.getStatusCodePk().equals(AffiliateStatus.AC)) {
                        return AffiliateErrorCodes.ERROR_NEW_COUNCIL_AFFILIATION_LOCAL_TO_DIST_COUNCIL;
                    }
                    break;
                default: // do nothing for the others...
                    break;
            }

            con = DBUtil.getConnection();

            Integer codePk = (Integer)OrganizationSubType.A;

            // insert into Org_Parent
            ps = con.prepareStatement(SQL_INSERT_ORG_PARENT);
            ps.setInt(1, codePk.intValue());
            affPk = DBUtil.insertAndGetIdentity(con, ps);
            newAffiliate.setAffPk(affPk);
            DBUtil.cleanup(null, ps, null);

            // Add the Affiliate...
            ps = con.prepareStatement(SQL_INSERT_NEW_AFFILIATE);
            DBUtil.setNullableInt(ps, 1, newAffiliate.getParentAffPk());
            ps.setString(2, newAffiliate.getAffiliateId().getType().toString());
            ps.setString(3, newAffiliate.getAffiliateId().getLocal());
            ps.setString(4, newAffiliate.getAffiliateId().getState());
            ps.setString(5, newAffiliate.getAffiliateId().getSubUnit());
            ps.setString(6, newAffiliate.getAffiliateId().getCouncil());
            ps.setString(7, newAffiliate.getAffiliateId().getCode().toString());
            ps.setString(8, newAffiliate.getAffiliateName());
            ps.setInt(9, newAffiliate.getAffiliateStatusCodePk().intValue());
            DBUtil.setNullableInt(ps, 10, newAffiliate.getAffiliateRegionCodePk());
            DBUtil.setNullableBooleanAsShort(ps, 11, newAffiliate.getMultipleEmployers());
            DBUtil.setNullableInt(ps, 12, newAffiliate.getAfscmeLegislativeDistrict());
            DBUtil.setNullableBooleanAsShort(ps, 13, newAffiliate.getAllowSubLocals());
            DBUtil.setNullableInt(ps, 14, newAffiliate.getAnnualCardRunTypeCodePk());
            ps.setInt(15, userPk.intValue());
            ps.setDate(16, new Date(System.currentTimeMillis()));
            ps.setInt(17, userPk.intValue());
            ps.setDate(18, new Date(System.currentTimeMillis()));
            ps.setInt(19, newAffiliate.getAffPk().intValue());
            DBUtil.setBooleanAsShort(ps, 20, newAffiliate.getMemberAllowEdit().booleanValue());
            DBUtil.setBooleanAsShort(ps, 21, newAffiliate.getMemberAllowView().booleanValue());

            logger.debug("NEW AFFILIATE: " + newAffiliate.toString());
            // insert into Aff_Organizations
            int returnValue = ps.executeUpdate();
            if (returnValue == 1) {
                logger.debug("++++++++++ AFFILIATE ADD SUCCESSFUL ++++++++++");
            } else {
                // this should never happen.
                logger.debug("---------- AFFILIATE ADD UNSUCCESSFUL : Return value = " + returnValue);
                throw new EJBException("ERROR: Affiliate not added.");
            }

            // insert charter info
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_CHARTER_DATA);
            ps.setInt(1, newAffiliate.getAffPk().intValue());
            DBUtil.setNullableVarchar(ps, 2, newAffiliate.getCharterName());
            DBUtil.setNullableVarchar(ps, 3, newAffiliate.getCharterJurisdiction());
            DBUtil.setNullableTimestamp(ps, 4, newAffiliate.getCharterDate());
            DBUtil.setNullableTimestamp(ps, 5, newAffiliate.getEffectiveDate());
            DBUtil.setNullableInt(ps, 6, newAffiliate.getCharterCode());
            ps.setInt(7, userPk.intValue());
            ps.setInt(8, userPk.intValue());

            returnValue = ps.executeUpdate();
            if (returnValue == 1) {
                logger.debug("++++++++++ CHARTER ADD SUCCESSFUL ++++++++++");
            } else if (returnValue == 0) {
                logger.debug("---------- CHARTER ADD UNSUCCESSFUL : NOTHING ADDED ----------");
                throw new EJBException("ERROR: Charter not added.");
            } else {
                // this should never happen.
                logger.debug("---------- CHARTER ADD UNSUCCESSFUL : TOO MANY RECORDS ADDED ----------");
                throw new EJBException("FATAL ERROR: Charter Insert returned multiple results.");
            }
            // insert charter counties
            if (!CollectionUtil.isEmpty(newAffiliate.getCounties())) {
                DBUtil.cleanup(null, ps, null);
                ps = con.prepareStatement(SQL_INSERT_CHARTER_COUNTIES);
                for (Iterator it = newAffiliate.getCounties().iterator(); it.hasNext(); ) {
                    ps.setInt(1, newAffiliate.getAffPk().intValue());
                    ps.setObject(2, it.next(), Types.VARCHAR);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // insert constitution info
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_CONSTITUTION_DATA);
            ps.setInt(1, newAffiliate.getAffPk().intValue());
            DBUtil.setNullableTimestamp(ps, 2, newAffiliate.getAffiliateAgreementDate());
            if (newAffiliate.getApprovedConstitution() != null &&
                newAffiliate.getApprovedConstitution().booleanValue()
            ) {
                ps.setTimestamp(3, DateUtil.getCurrentDateAsTimestamp());
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            DBUtil.setNullableBooleanAsShort(ps, 4, newAffiliate.getApprovedConstitution());
            ps.setInt(5, userPk.intValue());
            ps.setInt(6, userPk.intValue());
            returnValue = ps.executeUpdate();
            if (returnValue == 1) {
                logger.debug("++++++++++ CONSTITUTION ADD SUCCESSFUL ++++++++++");
            } else if (returnValue == 0) {
                logger.debug("---------- CONSTITUTION ADD UNSUCCESSFUL : NOTHING ADDED ----------");
                throw new EJBException("ERROR: Constitution not added.");
            } else {
                // this should never happen.
                logger.debug("---------- CONSTITUTION ADD UNSUCCESSFUL : TOO MANY RECORDS ADDED ----------");
                throw new EJBException("FATAL ERROR: Constitution Insert returned multiple results.");
            }

            /** @TODO: generate default offices */

            // Insert Financial Info
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_FINANCIAL_DATA);
            ps.setInt(1, newAffiliate.getAffPk().intValue());
            returnValue = ps.executeUpdate();
            if (returnValue == 1) {
                logger.debug("++++++++++ FINANCIAL INFO ADD SUCCESSFUL ++++++++++");
            } else if (returnValue == 0) {
                logger.debug("---------- FINANCIAL INFO ADD UNSUCCESSFUL : NOTHING ADDED ----------");
                throw new EJBException("ERROR: Financial Info not added.");
            } else {
                // this should never happen.
                logger.debug("---------- FINANCIAL INFO ADD UNSUCCESSFUL : TOO MANY RECORDS ADDED ----------");
                throw new EJBException("FATAL ERROR: Financial Info Insert returned multiple results.");
            }

            // Insert Mbrsp Rptg Info
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_MEMBERSHIP_REPORTING);
            ps.setInt(1, newAffiliate.getAffPk().intValue());
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, userPk.intValue());
            returnValue = ps.executeUpdate();
            if (returnValue == 1) {
                logger.debug("++++++++++ MEMBERSHIP REPORTING ADD SUCCESSFUL ++++++++++");
            } else if (returnValue == 0) {
                logger.debug("---------- MEMBERSHIP REPORTING ADD UNSUCCESSFUL : NOTHING ADDED ----------");
                throw new EJBException("ERROR: Membership Reporting Info not added.");
            } else {
                // this should never happen.
                logger.debug("---------- FINANCIAL INFO ADD UNSUCCESSFUL : TOO MANY RECORDS ADDED ----------");
                throw new EJBException("FATAL ERROR: Membership Reporting Info Insert returned multiple results.");
            }

            // record changes to history
            logger.debug("newAffiliate affPk = " + newAffiliate.getAffPk());
            recordChangeToHistory(newAffiliate, userPk);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        // Return the new Affiliate's PK
        return affPk.intValue();
    }

    /**
     * Retrieves the parent Administrative/Legislative Council for an Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk Primary Key of the Affiliate
     * @return      an AffiliateData object representing the Admin/Legis Council.
     *              Returns null if Affiliate passed in is an Admin/Legis Council
     *              or if no Admin/Legis Council exists for this Affiliate.
     */
    public AffiliateData getAffiliatedAdminCouncil(Integer affPk) {
        AffiliateData data = getAffiliateData(affPk);
        return getAffiliatedAdminCouncil(data);
    }

    /**
     * Retrieves the parent Administrative/Legislative Council for an Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param data  The Affiliate
     * @return      an AffiliateData object representing the Admin/Legis Council.
     */
    public AffiliateData getAffiliatedAdminCouncil(AffiliateData data) {
        if (data == null ||
            data.getAffiliateId() == null ||
            TextUtil.isEmptyOrSpaces(data.getAffiliateId().getType())
        ) {
            return null;
        }
        switch (data.getAffiliateId().getType().charValue()) {
            case 'C':
                if (data.getStatusCodePk().equals(AffiliateStatus.AC)) {
                    return data;
                }
            case 'R':
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {
                    con = DBUtil.getConnection();
                    ps = con.prepareStatement(SQL_SELECT_COUNCIL_AFFILIATION_FOR_DIST_CN);
                    ps.setInt(1, data.getAffPk().intValue());
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        return getAffiliateData(DBUtil.getIntegerOrNull(rs, "admin_legislative_cncl_aff_pk"));
                    } else {
                        return null;
                    }
                } catch (SQLException se) {
                    throw new EJBException(se);
                } finally {
                    DBUtil.cleanup(con, ps, rs);
                }
            case 'L':
            case 'S':
            case 'U':
                return getAffiliatedAdminCouncil(getAffiliateData(data.getParentFk()));
            default:
                return null;
        }
    }

    /**
     * Allows the set of associated or affiliated Councils to be returned for
     * the Affiliate whose pk is provided.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk PK of the Affiliate
     * @return the Collection of AffiliateResult objects.
     */
    public List getAffiliatedCouncils(Integer affPk) {
        if (affPk == null) {
            throw new EJBException("Affiliate PK was null.");
        }
        return getAffiliatedCouncils(getAffiliateData(affPk));
    }

    /**
     * Allows the set of associated or affiliated Councils to be returned for
     * the Affiliate provided.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param data the Affiliate
     * @return the Collection of AffiliateResult objects.
     */
    public List getAffiliatedCouncils(AffiliateData data) {
        if (data == null ||
            data.getAffPk() == null ||
            data.getAffiliateId() == null ||
            TextUtil.isEmptyOrSpaces(data.getAffiliateId().getType())
        ) {
            throw new EJBException("Affiliate data was null.");
        }
        switch (data.getAffiliateId().getType().charValue()) {
            case 'C':
                if (data.getStatusCodePk().equals(AffiliateStatus.AC)) {
                    return getSubDistrictCouncilsForAdminCouncil(data);
                }
            case 'R':
            case 'L':
            case 'S':
            case 'U':
                AffiliateData adminCn = getAffiliatedAdminCouncil(data);
                AffiliateData distCn = getAffiliatedDistrictCouncil(data);
                if (adminCn == null && distCn == null) {
                    return null;
                }
                List councils = new ArrayList();
                if (adminCn != null) {
                    councils.add(adminCn);
                }
                // only add distCn if not equal to data
                if (distCn != null && !distCn.getAffPk().equals(data.getAffPk())) {
                    councils.add(distCn);
                }
                return councils;
            default:
                return null;
        }
    }

    private List getSubDistrictCouncilsForAdminCouncil(AffiliateData data) {
        if (data == null) {
            return null;
        }
        if (!data.getStatusCodePk().equals(AffiliateStatus.AC)) {
            throw new EJBException("Affiliate was not an Admin Council.");
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List councils = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNCIL_AFFILIATION_FOR_ADMIN_CN);
            ps.setInt(1, data.getAffPk().intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                councils = new ArrayList();
                int pk = 0;
                do {
                    councils.add(getAffiliateData(DBUtil.getIntegerOrNull(rs, "regular_council_aff_pk")));
                } while (rs.next());
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return councils;
    }

    /**
     * Retrieves the parent District Council for an Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk Primary Key of the Affiliate
     * @return      an AffiliateData object representing the parent District
     *              Council. Returns null if a Council or Retiree Chapter is
     *              passed in. Also returns null for non-Affiliate Locals and
     *              their Sub Locals, including Retiree Sub Chapters.
     */
    public AffiliateData getAffiliatedDistrictCouncil(Integer affPk) {
        AffiliateData data = getAffiliateData(affPk);
        return getAffiliatedDistrictCouncil(data);
    }

    /**
     * Retrieves the parent District Council for an Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param data  the Affiliate
     * @return      an AffiliateData object representing the parent District
     *              Council. Returns null if a Council or Retiree Chapter is
     *              passed in. Also returns null for non-Affiliate Locals and
     *              their Sub Locals, including Retiree Sub Chapters.
     */
    public AffiliateData getAffiliatedDistrictCouncil(AffiliateData data) {
        if (data == null ||
            data.getAffiliateId() == null ||
            TextUtil.isEmptyOrSpaces(data.getAffiliateId().getType())
        ) {
            return null;
        }
        switch (data.getAffiliateId().getType().charValue()) {
            case 'L':
            case 'S':
                return getAffiliateData(data.getParentFk());
            case 'U':
                return getAffiliatedDistrictCouncil(getAffiliateData(data.getParentFk()));
            case 'C':
                if (data.getStatusCodePk().equals(AffiliateStatus.AC)) {
                    return null;
                }
            case 'R':
                return data;
            default:
                return null;
        }
    }

    /**
     * Retieves the affiliate detail data for the affiliate specified.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk Affiliate Primary Key
     * @return the AffiliateData Object. Returns null if Affiliate doesn't exist.
     */
    public AffiliateData getAffiliateData(Integer affPk) {

        if (affPk == null) {
            return null;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AffiliateData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_DETAIL);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data = new AffiliateData();
                AffiliateIdentifier affID = new AffiliateIdentifier();

                data.setAbbreviatedName(rs.getString("aff_abbreviated_nm"));
                data.setAffPk(new Integer(rs.getInt("aff_pk")));
                // set the affiliate identifier fields.
                affID.setCode(new Character(rs.getString("aff_code").toCharArray()[0]));
                affID.setCouncil(rs.getString("aff_councilRetiree_chap"));
                affID.setLocal(rs.getString("aff_localSubChapter"));
                affID.setState(rs.getString("aff_stateNat_type"));
                affID.setSubUnit(rs.getString("aff_subUnit"));

                affID.setType(new Character(rs.getString("aff_type").toCharArray()[0]));

                data.setAffiliateId(affID);
                data.setAfscmeLegislativeDistrictCodePk(DBUtil.getIntegerOrNull(rs, "aff_afscme_leg_district"));
                data.setAfscmeRegionCodePk(DBUtil.getIntegerOrNull(rs, "aff_afscme_region"));
                data.setAllowSubLocals(DBUtil.getBooleanFromShort(rs.getShort("aff_sub_locals_allowed_fg")));
                data.setAllowedMbrEdit(DBUtil.getBooleanFromShort(rs.getShort("mbr_allow_edit_fg")));
                data.setAllowedMbrView(DBUtil.getBooleanFromShort(rs.getShort("mbr_allow_view_fg")));
                data.setAnnualCardRunPerformed(DBUtil.getBooleanFromShort(rs.getShort("mbr_yearly_card_run_done_fg")));
                data.setAnnualCardRunTypeCodePk(DBUtil.getIntegerOrNull(rs, "aff_ann_mbr_card_run_group"));
                data.setLegacyKey(rs.getString("old_aff_unit_cd_legacy"));
                data.setLegacyKeyOther(rs.getString("old_aff_no_other"));
                data.setMemberRenewalCodePk(DBUtil.getIntegerOrNull(rs, "mbr_renewal"));
                data.setMultipleEmployers(DBUtil.getBooleanFromShort(rs.getShort("aff_mult_employers_fg")));
                data.setMultipleOffices(DBUtil.getBooleanFromShort(rs.getShort("aff_multiple_offices_fg")));
                data.setNewAffiliateIDSourcePk(DBUtil.getIntegerOrNull(rs, "new_aff_id_src"));
                data.setParentFk(DBUtil.getIntegerOrNull(rs, "parent_aff_fk"));
                data.setStatusCodePk(DBUtil.getIntegerOrNull(rs, "aff_status"));
                data.setWebsite(rs.getString("aff_web_url"));
                RecordData rData = new RecordData();
                rData.setCreatedBy(DBUtil.getIntegerOrNull(rs, "created_user_pk"));
                rData.setCreatedDate(rs.getTimestamp("created_dt"));
                rData.setModifiedBy(DBUtil.getIntegerOrNull(rs, "lst_mod_user_pk"));
                rData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                data.setRecordData(rData);
                // set the admin/legis council field.
                data.getAffiliateId().setAdministrativeLegislativeCouncil(getAdminCouncilNumber(data));
                // if Affiliate is a local, check if it has Sub Locals
                data.setContainsSubLocals(hasSubLocals(data));

                // get comment
                CommentData cData = getCommentForAffiliate(affPk);
                if (cData != null) {
                    data.setComment(cData.getComment());
                }

                // employer sector
                Collection sectors = getEmployerSectors(affPk);
                data.setEmployerSector(sectors);
            } else { // will return null
                logger.debug("---------- Affiliate Data NOT Found for pk = " + affPk + ". ---------- ");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return data;
    }

     /**
     * Retieves the affiliate detail data for the affiliate specified.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param rowId Affiliate Primary Key
     * @return the PreAffiliateData Object. Returns null if Affiliate doesn't exist.
     */
    public PreAffiliateData getPreAffiliateData(String rowId) {

        if (rowId == null) {
            return null;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreAffiliateData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PRE_AFFILIATE_DETAIL);

            ps.setString(1, rowId);

            rs = ps.executeQuery();

            if (rs.next()) {
                data = new PreAffiliateData();

                // data.setRowId(rs.getString("rowId"));

                data.setAffPk(rs.getString("aff_fk"));
                data.setEmpAffPk(rs.getString("EmpAffPk"));

                data.setAffIdState(rs.getString("state"));
                data.setAffIdCouncil(rs.getString("council"));
                data.setAffIdLocal(rs.getString("local"));
                data.setAffIdSubUnit(rs.getString("chapter"));
                data.setEmployerName(rs.getString("employerNm"));

                ///////////////
                data.setAgmtEffDate(rs.getString("agmtEffDate"));
                data.setAgmtExpDate(rs.getString("agmtExpDate"));
                data.setNoMemFeePayer(rs.getString("noMemFeePayer"));
                data.setIfRecInc(rs.getString("ifRecInc"));
                data.setIfInNego(rs.getString("ifInNego"));
                data.setPercentWageInc(rs.getString("percentWageInc"));
                data.setWageIncEffDate(rs.getString("wageIncEffDate"));
                data.setNoMemFeePayerAff1(rs.getString("noMemFeePayerAff1"));
                data.setCentPerHrDoLumpSumBonus(rs.getString("centPerHrDoLumpSumBonus"));
                data.setAvgWagePerHrYr(rs.getString("avgWagePerHrYr"));
                data.setEffDateInc(rs.getString("effDateInc"));
                data.setNoMemFeePayerAff2(rs.getString("noMemFeePayerAff2"));
                data.setSpeWageAgj(rs.getString("speWageAgj"));
                data.setPercentInc(rs.getString("percentInc"));
                data.setDollarCent(rs.getString("dollarCent"));
                data.setAvgPay(rs.getString("avgPay"));
                data.setNoMemFeePayerAff3(rs.getString("noMemFeePayerAff3"));
                data.setContactName(rs.getString("contactName"));
                data.setContactPhoneEmail(rs.getString("contactPhoneEmail"));
                //data.setNotes(rs.getString("notes"));
                data.setLoad_ID(rs.getString("Load_ID"));
                data.setBatch_ID(rs.getString("Batch_ID"));
                data.setProcessed(rs.getString("Processed"));
                data.setIncrease_type(rs.getString("Increase_type"));
                data.setStatMbrCount(rs.getString("statMbrCount"));
                data.setMbrsAfps_Affected(rs.getString("MbrsAfps_Affected"));
                data.setAdj_MbrsAfps_Affected(rs.getString("Adj_MbrsAfps_Affected"));
                data.setUserPosting(rs.getString("UserPosting"));
                data.setDoNotProcess(rs.getString("DoNotProcess"));
                data.setComment(rs.getString("Comment"));
                data.setWifPk(rs.getString("wifPk"));
                data.setWidPk(rs.getString("widPk"));

            } else { // will return null
                logger.debug("---------- Affiliate Data NOT Found for pk = " + rowId + ". ---------- ");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return data;
    }

    /**
     * Retieves the employer detail data for the employer specified.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param empId Employer Primary Key
     * @return the EmployerData Object. Returns null if Employer doesn't exist.
     */
    public EmployerData getEmployerData(int empAffPk) {

        if (empAffPk == 0) {
            return null;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EmployerData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EMPLOYER_DETAIL);
            //System.out.println("SQL_SELECT_EMPLOYER_DETAIL = " + SQL_SELECT_EMPLOYER_DETAIL);
            ps.setInt(1, empAffPk);
            rs = ps.executeQuery();
            if (rs.next()) {
                data = new EmployerData();

				data.setType(rs.getString("type"));
                data.setEmployer(rs.getString("curr_employer_name"));
                data.setState(rs.getString("state"));
                data.setCouncil(rs.getInt("council"));
                data.setLocal(rs.getInt("local"));
                data.setChapter(rs.getString("chapter"));
                data.setStatus(rs.getString("active"));
                data.setEmpAffPk(empAffPk);

             } else { // will return null
                logger.debug("---------- Affiliate Data NOT Found for empAffPk = " + empAffPk + ". ---------- ");
             }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

		// get exisitng data entry years for an employer
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EMPLOYER_EXISTING_YEAR);
            //System.out.println("SQL_SELECT_EMPLOYER_DETAIL = " + SQL_SELECT_EMPLOYER_DETAIL);
            ps.setInt(1, empAffPk);
            rs = ps.executeQuery();
            ArrayList tmpYearList = new ArrayList();

            while (rs.next()) {
				tmpYearList.add(""+rs.getInt("duesyear"));
            }

            data.setExisting_year(tmpYearList);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return data;
    }

    /**
     * Indicates true or false if the Local has Sub Locals. Return null if the
     * Affiliate is not a Local.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk     The Affiliate's Primary Key
     *
     * @return      'true' iff the Affiliate has Sub Locals. 'false' iff the
     *              Affiliate doesn't have Sub Locals. NULL if the Affiliate is
     *              not a Local.
     */
    public Boolean hasSubLocals(Integer affPk) {
        if (affPk == null || affPk.intValue() < 1) {
            return null;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean hasSubs = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_HAS_SUB_LOCALS);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                hasSubs = DBUtil.getBooleanFromShort(rs.getShort("has_subs"));
            } // else do nothing. should never happen since query always returns 0 or 1.
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return hasSubs;
    }

    /**
     * Indicates true or false if the Local has Sub Locals. Return null if the
     * Affiliate is not a Local.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param data  The Affiliate
     *
     * @return      'true' iff the Affiliate has Sub Locals. 'false' iff the
     *              Affiliate doesn't have Sub Locals. NULL if the Affiliate is
     *              not a Local.
     */
    public Boolean hasSubLocals(AffiliateData data) {
        if (data == null || data.getAffiliateId() == null ||
            !TextUtil.equals(data.getAffiliateId().getType(), new Character('L'))
        ) {
            return null;
        }
        return hasSubAffiliates(data.getAffPk());
    }

    /**
     * Indicates true or false if the Affiliate has Sub Affiliates.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param data  The Affiliate
     *
     * @return      'true' iff the Affiliate has Sub Locals. 'false' iff the
     *              Affiliate doesn't have Sub Locals. NULL if the Affiliate is
     *              not a Local.
     */
    public Boolean hasSubAffiliates(Integer affPk) {
        //logger.debug("Inside hasSubAffiliates(Integer)");
        if (affPk == null || affPk.intValue() < 1) {
            logger.debug("returning null");
            return null;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean hasSubs = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_HAS_SUB_AFFILIATES);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                hasSubs = DBUtil.getBooleanFromShort(rs.getShort("has_subs"));
            } // else do nothing. should never happen since query always returns 0 or 1.
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        //logger.debug("returning value: " + hasSubs);
        return hasSubs;

    }

    /**
     * Retrieves the primary key of the Affiliate with the given identifier.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param the AffiliateIdentifier
     *
     * @return Returns the Primary Key, if found. Null if the Affiliate Identifier
     * cannot be found. Error Code if too many results are found.
     */
    public Integer getAffiliatePk(AffiliateIdentifier affId) {
        //logger.debug("getAffiliatePk called.");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Integer affPk = null;

        try {
            con = DBUtil.getConnection();
            builder.addCriterion("aff_type", affId.getType());
            builder.addCriterion("aff_localSubChapter", affId.getLocal());
            builder.addCriterion("aff_stateNat_type", affId.getState());
            builder.addCriterion("aff_subUnit", affId.getSubUnit());
            builder.addCriterion("aff_councilRetiree_chap", affId.getCouncil());
            builder.addCriterion("aff_code", affId.getCode());
            ps = builder.getPreparedStatement(SQL_SELECT_AFF_PK_FOR_AFF_ID, con);
            logger.debug("executing query...");
            rs = ps.executeQuery();
            if (rs.next()) {
                affPk = DBUtil.getPositiveIntegerOrNull(rs, 1);
                if (rs.next()) {
                    // too many results
                    return new Integer(AffiliateErrorCodes.ERROR_GET_PK_MORE_THAN_ONE_RESULT);
                }
            } // else do nothing. affPk is already null...
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return affPk;
    }

    /**
     * Checks the database for an instance of the AffiliateIdentifier. Useful
     * for validating that an AffiliateIdentifier change won't break the unique
     * constraint for these fields in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affId     the AffiliateIdentifier
     *
     * @return  Returns 'true' if found, 'false' if not found or if the affID or
     *          one of it's fields is null.
     */
    public boolean doesAffiliateIdentifierExist(AffiliateIdentifier affId) {
        if (affId == null) {
            return false;
        }
        return doesAffiliateIdentifierExist(affId.getType(), affId.getCode(),
                                            affId.getLocal(), affId.getState(),
                                            affId.getSubUnit(), affId.getCouncil()
        );
    }

    /**
     * Checks the database for an instance of the AffiliateIdentifier. Useful
     * for validating that an AffiliateIdentifier change won't break the unique
     * constraint for these fields in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param type      Affiliate Identifier type field
     * @param code      Affiliate Identifier code field
     * @param local     Affiliate Identifier local field
     * @param state     Affiliate Identifier state field
     * @param subUnit   Affiliate Identifier subUnit field
     * @param council   Affiliate Identifier council field
     *
     * @return  Returns 'true' if found, 'false' if not found or if one of the
     *          AffiliateIdentifier fields is null.
     */
    public boolean doesAffiliateIdentifierExist(Character type, Character code,
                                                String local, String state,
                                                String subUnit, String council
    ) {
        if (TextUtil.isEmptyOrSpaces(type) || TextUtil.isEmptyOrSpaces(code) ||
            TextUtil.isEmptyOrSpaces(local) || TextUtil.isEmptyOrSpaces(state) ||
            TextUtil.isEmptyOrSpaces(subUnit) || TextUtil.isEmptyOrSpaces(council)
        ) {
            return false;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_ID_EXISTS);
            ps.setString(1, type.toString());
            ps.setString(2, code.toString());
            ps.setString(3, local);
            ps.setString(4, state);
            ps.setString(5, subUnit);
            ps.setString(6, council);
            rs = ps.executeQuery();
            if (rs.next()) {
                int value = rs.getInt(1);
                if (value == 1) {
                    exists = true;
                }
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return exists;
    }

    /**
     * Retrieves the entire hierarchy for an Affiliate including all the
     * Affiliates above and below with siblings on all levels.
     *
     * The Collection is returned in hierarchical order, with the Admin/Legis
     * Council atthe top, followed by the District Council, followed each Local
     * with their Sub Locals if any exist.
     *
     * If the Affiliate that is passed in is a Local/Sub Chapter or a Sub Local,
     * the ordering will list that Local, or the Sub Local's Local, first after
     * the District Council. The remaining Locals/Sub Locals will be ordered by
     * Local Number and Sub Local Number respectively.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param   affPk   Affiliate Primary Key
     * @return  the List of AffiliateHierarchyEntry objects representing
     *          an Affiliate's Hierarchy structure.
     */
    public List getAffiliateHierarchy(Integer affPk) {
        if (affPk == null || affPk.intValue() < 1) {
            throw new EJBException("No Affiliate PK specified.");
        }
        AffiliateData mainAffil = getAffiliateData(affPk);
        if (mainAffil == null) {
            throw new EJBException("No Affiliate found with pk = " + affPk);
        }
        switch (mainAffil.getAffiliateId().getType().charValue()) {
            case 'C':
                if (mainAffil.getStatusCodePk().equals(AffiliateStatus.AC)) {
                    return getAffiliateHierarchyForAdminCouncil(mainAffil);
                } // else return the same as the 'R' case...
            case 'R':
                return getAffiliateHierarchyForCouncilRetireeChapter(mainAffil);
            case 'L':
            case 'S':
                return getAffiliateHierarchyForLocalRetireeSubChapter(mainAffil);
            case 'U':
                return getAffiliateHierarchyForSubLocal(mainAffil);
            default:
                // should never happen
                throw new EJBException("Affiliate Type entered does not match a valid code.");
        }
    }

    /**
     * Retrieves an Affiliate and all Affiliates below in the hierarchy with
     * siblings on all levels.
     *
     * The Collection is returned in hierarchical order, with the requested Affiliate
     * first.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk Affiliate Primary Key
     * @return the Collection of AffiliateHierarchyEntry objects representing an Affiliate's Sub
     * Hierarchy structure.
     */
    public List getAffiliateSubHierarchy(Integer affPk) {
        return getAffiliateSubHierarchy(affPk, true);
    }

    /**
     * Retrieves an Affiliate and all Affiliates below in the hierarchy with
     * siblings on all levels.
     *
     * The Collection is returned in hierarchical order, with the requested Affiliate
     * first.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param data  The Affiliate's Data
     * @return  the List of AffiliateHierarchyEntry objects representing
     *          an Affiliate's Sub Hierarchy structure.
     */
    public List getAffiliateSubHierarchy(AffiliateData data) {
        return getAffiliateSubHierarchy(data, true);
    }

    /**
     * Retrieves an Affiliate and all Affiliates below in the hierarchy with
     * siblings on all levels.
     *
     * The Collection is returned in hierarchical order, with the requested Affiliate
     * first.
     *
     * @param affPk     Affiliate Primary Key
     * @param isPrimarySubHierarchy     Indicates if the sub hierarchy being queried
     *                                  is the primary section of the overall hierarchy.
     * @return  the Collection of AffiliateHierarchyEntry objects representing an Affiliate's Sub
     * Hierarchy structure.
     */
    private List getAffiliateSubHierarchy(Integer affPk, boolean isPrimarySubHierarchy) {
        return getAffiliateSubHierarchy(getAffiliateData(affPk), isPrimarySubHierarchy);
    }

    /**
     * Retrieves an Affiliate and all Affiliates below in the hierarchy with
     * siblings on all levels.
     *
     * The Collection is returned in hierarchical order, with the requested Affiliate
     * first.
     *
     * @param data  the Affiliate's Data
     * @param isPrimarySubHierarchy     Indicates if the sub hierarchy being queried
     *                                  is the primary section of the overall hierarchy.
     * @return  the Collection of AffiliateHierarchyEntry objects representing an Affiliate's Sub
     * Hierarchy structure.
     */
    private List getAffiliateSubHierarchy(AffiliateData data, boolean isPrimarySubHierarchy) {
        if (data == null || data.getAffPk() == null || data.getAffPk().intValue() < 1) {
            return null;
        }

        List hierarchy = new ArrayList();
        AffiliateHierarchyEntry current = getAffiliateHierarchyEntryFromData(data, isPrimarySubHierarchy);
        //logger.debug("Adding pk = " + data.getAffPk() + " with id = " + current.getAffiliateId());
        hierarchy.add(current);
        List subAffiliates = getSubAffiliates(data.getAffPk());
        if (!CollectionUtil.isEmpty(subAffiliates)) {
            List subHierarchy = null;
            AffiliateData sub = null;
            AffiliateHierarchyEntry subEntry = null;
            for (Iterator it = subAffiliates.iterator(); it.hasNext(); ) {
                sub = (AffiliateData)it.next();
                subHierarchy = getAffiliateSubHierarchy(sub, isPrimarySubHierarchy);
                if (!CollectionUtil.isEmpty(subHierarchy)) {
                    hierarchy.addAll(subHierarchy);
                }
            }
        }
        return hierarchy;
    }

    private List getAffiliateHierarchyForAdminCouncil(AffiliateData data) {
        List hierarchy = new ArrayList();
        // add the admin council
        hierarchy.add(getAffiliateHierarchyEntryFromData(data, true));

        // get all of the district councils, retrieve their sub hierarchies, and add them
        List councils = getAffiliatedCouncils(data);
        List subAffils = null;
        if (!CollectionUtil.isEmpty(councils)) {
            AffiliateData council = null;
            for (Iterator it = councils.iterator(); it.hasNext(); ) {
                council = (AffiliateData)it.next();
                subAffils = getAffiliateSubHierarchy(council);
                if (!CollectionUtil.isEmpty(subAffils)) {
                    hierarchy.addAll(subAffils);
                }
            }
        }
        return hierarchy;
    }

    private List getAffiliateHierarchyForCouncilRetireeChapter(AffiliateData data) {
        if (data.getParentFk() == null) {
            return getAffiliateSubHierarchy(data);
        }
        List hierarchy = new ArrayList();

        // add the Administrative/Legislative Council if one exists
        AffiliateData adminCn = getAffiliatedAdminCouncil(data);
        logger.debug(adminCn + " found for affPk = " + data.getAffPk());
        if (adminCn != null) {
            hierarchy.add(getAffiliateHierarchyEntryFromData(adminCn, false));
        }

        // add the District Council and all of it's sub affiliates
        List subAffils = getAffiliateSubHierarchy(data);
        if (!CollectionUtil.isEmpty(subAffils)) {
            hierarchy.addAll(subAffils);
        }
        return hierarchy;
    }

    private List getAffiliateHierarchyForLocalRetireeSubChapter(AffiliateData data) {
        if (data.getParentFk() == null) {
            // this is an unaffiliated local
            return getAffiliateSubHierarchy(data);
        }
        List hierarchy = new ArrayList();

        // add the Administrative/Legislative Council if one exists
        AffiliateData adminCn = getAffiliatedAdminCouncil(data);
        if (adminCn != null) {
            hierarchy.add(getAffiliateHierarchyEntryFromData(adminCn, false));
        }
        // Add the District Council
        AffiliateData distCn = getAffiliateData(data.getParentFk());
        hierarchy.add(getAffiliateHierarchyEntryFromData(distCn, false));
        // Add the Local with it's Sub Locals
        hierarchy.addAll(getAffiliateSubHierarchy(data, true));

        // get the rest of the locals, and add them if they are not equal to this local
        List subAffils = getSubAffiliates(distCn.getAffPk());
        AffiliateData current = null;
        for (Iterator it = subAffils.iterator(); it.hasNext(); ) {
            current = (AffiliateData)it.next();
            if (!current.getAffPk().equals(data.getAffPk())) {
                hierarchy.addAll(getAffiliateSubHierarchy(current, false));
            }
        }
        return hierarchy;
    }

    private List getAffiliateHierarchyForSubLocal(AffiliateData data) {
        List hierarchy = new ArrayList();

        AffiliateData adminCn = null;
        AffiliateData distCn = null;
        AffiliateData local = getAffiliateData(data.getParentFk());
        if (local.getParentFk() != null) {
            adminCn = getAffiliatedAdminCouncil(data);
            // add the Administrative/Legislative Council if one exists
            if (adminCn != null) {
                hierarchy.add(getAffiliateHierarchyEntryFromData(adminCn, false));
            }
            // add the District Council
            distCn = getAffiliateData(local.getParentFk());
            hierarchy.add(getAffiliateHierarchyEntryFromData(distCn, false));
        }
        hierarchy.add(getAffiliateHierarchyEntryFromData(local, false));
        hierarchy.add(getAffiliateHierarchyEntryFromData(data, true));
        // get the rest of the sublocals, and add them if they are not equal to this sublocal
        List subAffils = getSubAffiliates(local.getAffPk());
        AffiliateData current = null;
        for (Iterator it = subAffils.iterator(); it.hasNext(); ) {
            current = (AffiliateData)it.next();
            if (!current.getAffPk().equals(data.getAffPk())) {
                hierarchy.add(getAffiliateHierarchyEntryFromData(current, false));
            }
        }
        if (distCn != null) {
            // this is an affiliated local
            // get the rest of the locals, and add them if they are not equal to this sublocal's local
            subAffils = getSubAffiliates(distCn.getAffPk());
            current = null;
            for (Iterator it = subAffils.iterator(); it.hasNext(); ) {
                current = (AffiliateData)it.next();
                if (!current.getAffPk().equals(local.getAffPk())) {
                    hierarchy.addAll(getAffiliateSubHierarchy(current, false));
                }
            }
        }
        return hierarchy;
    }

    private AffiliateHierarchyEntry getAffiliateHierarchyEntryFromData(AffiliateData data, boolean inPrimarySubHierarchy) {
        AffiliateHierarchyEntry ahe = new AffiliateHierarchyEntry();
        ahe.setAffPk(data.getAffPk());
        ahe.setAffiliateId(data.getAffiliateId());
        ahe.setName(data.getAbbreviatedName());
        ahe.setStatus(data.getStatusCodePk());
        ahe.setInPrimarySubHierarchy(inPrimarySubHierarchy);
        return ahe;
    }

    private void addToHierarchy(List hierarchy, Set addedPks, AffiliateData aff) {
        if (!addedPks.contains(aff.getAffPk())) {
            hierarchy.add(aff);
            addedPks.add(aff.getAffPk());
        }
    }

    /**
     * Retrieves a Collection of specific changes associated with an AffiliateChangeResult.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @param sectionCodePk section code for retrieving changes
     * @param changedDate the date the changes were made
     * @return the Collection of AffiliateChangeData objects
     */
    public Collection getChangeHistoryData(Integer affPk, Integer sectionCodePk, Timestamp changedDate) {
        logger.debug("Inside getChangeHistoryData(Integer, Integer, Timestamp).");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection results = null;
        AffiliateChangeData acd = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CHANGE_HISTORY_DETAIL);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, sectionCodePk.intValue());
            ps.setTimestamp(3, changedDate);
            rs = ps.executeQuery();
            if (rs.next()) {
                results = new ArrayList();
                do{
                    acd = new AffiliateChangeData();
                    acd.setFieldChangedCodePk(DBUtil.getIntegerOrNull(rs, "aff_field_chnged"));
                    acd.setOldValue(rs.getString("old_value"));
                    acd.setNewValue(rs.getString("new_value"));
                    acd.setChangedByUserPk(DBUtil.getIntegerOrNull(rs, "chng_user_pk"));
                    results.add(acd);
                } while (rs.next());
            } else {
                logger.debug("ResultSet for detail table was null.");
            }
            // check for Charter Jurisdiction changes
            if (sectionCodePk.equals(AffiliateSections.CHARTER)) {
                DBUtil.cleanup(null, ps, rs);
                ps = con.prepareStatement(SQL_SELECT_CHANGE_HISTORY_DETAIL_JURISDICTION);
                ps.setInt(1, affPk.intValue());
                ps.setInt(2, sectionCodePk.intValue());
                ps.setTimestamp(3, changedDate);
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (results == null) {
                        results = new ArrayList();
                    }
                    do {
                        acd = new AffiliateChangeData();
                        acd.setFieldChangedCodePk(ChangeHistoryFields.CHARTER_JURISDICTION);
                        acd.setOldValue(rs.getString("old_value"));
                        acd.setNewValue(rs.getString("new_value"));
                        acd.setChangedByUserPk(DBUtil.getIntegerOrNull(rs, "chng_user_pk"));
                        results.add(acd);
                    } while (rs.next());
                } else {
                    logger.debug("ResultSet for jurisdiction detail table was null.");
                }
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return results;
    }

    /**
     * Retrieves the Charter Data for this affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk Affiliate Primary Key
     * @return the CharterData object. Returns NULL if no Charter found.
     */
    public CharterData getCharterData(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CharterData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CHARTER_DATA);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data = new CharterData();
                data.setAffPk(affPk);
                data.setCharterCodeCodePk(DBUtil.getIntegerOrNull(rs, "charter_cd"));
                data.setCharterDate(rs.getTimestamp("charter_dt"));
                data.setJurisdiction(rs.getString("charter_juris"));
                data.setLastChangeEffectiveDate(rs.getTimestamp("charter_lst_chg_eff_dt"));
                data.setName(rs.getString("charter_nm"));
                data.setStatusCodePk(DBUtil.getIntegerOrNull(rs, "aff_status"));
                data.setAffIdType(new Character(rs.getString("aff_type").toCharArray()[0]));

                // get the nearest council in the hierarchy.
                AffiliateData council = null;
                switch (data.getAffIdType().charValue()) {
                    case 'C' :
                        if (!data.getStatusCodePk().equals(AffiliateStatus.AC)) {
                            council = getAffiliatedAdminCouncil(affPk);
                        } // else council remains null
                        break;
                    case 'L':
                    case 'S':
                    case 'U':
                        council = getAffiliatedDistrictCouncil(affPk);
                        break;
                    default: // council remains null
                        break;
                }
                if (council == null) {
                    data.setReportingCouncilPk(null);
                } else {
                    data.setReportingCouncilPk(council.getAffPk());
                }
                // get charter counties...
                DBUtil.cleanup(null, ps, rs);
                ps = con.prepareStatement(SQL_SELECT_CHARTER_COUNTIES);
                ps.setInt(1, affPk.intValue());
                rs = ps.executeQuery();
                Collection counties = null;
                if (rs.next()) {
                    counties = new ArrayList();
                    do {
                        counties.add(rs.getString("county_nm"));
                    } while (rs.next());
                    logger.debug("Found " + counties.size() + " Charter Counties found for affPk = " + affPk.intValue());
                } else {
                    logger.debug("No Charter Counties found for affPk = " + affPk.intValue());
                }
                data.setCounties(counties);

                // Get Council Affiliations
                data.setCouncilAffiliations(getAffiliatedCouncils(affPk));

                logger.debug("+++++++ Found Charter Data: " + data);
            } else {
                logger.debug("Charter not found.");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Retrieves the most recent comment associated with the Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk The Affiliate Primary Key
     *
     * @return The CommentData object. Returns null if no comments found.
     */
    public CommentData getCommentForAffiliate(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CommentData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MOST_RECENT_COMMENT_FOR_AFFILIATE);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data = new CommentData();
                data.setComment(rs.getString("comment_txt"));
                data.setCommentDt(rs.getTimestamp("comment_dt"));
                RecordData rd = new RecordData();
                rd.setCreatedBy(DBUtil.getIntegerOrNull(rs, "created_user_pk"));
                rd.setCreatedDate(rs.getTimestamp("comment_dt"));
                rd.setModifiedBy(rd.getCreatedBy());
                rd.setModifiedDate(rd.getCreatedDate());
                data.setRecordData(rd);
                if (rs.next()) {
                    // should never happen
                    throw new EJBException("Too many results returned by the query.");
                }
                //logger.debug("++++++ Comments Found for pk = " + affPk + ". ++++++");
            } else {
                //logger.debug("------ Comments NOT Found for pk = " + affPk + ". ------");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Retrieves the comment history for an Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return the Collection of CommentData objects. NULL if no comments found.
     */
    public Collection getCommentHistoryForAffiliate(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection comments = null;
        CommentData data = null;
        RecordData rd = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COMMENTS_FOR_AFFILIATE);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                comments = new ArrayList();
                do {
                    data = new CommentData();
                    rd = new RecordData();
                    data.setComment(rs.getString("comment_txt"));
                    data.setCommentDt(rs.getTimestamp("comment_dt"));
                    rd.setCreatedBy(DBUtil.getIntegerOrNull(rs, "created_user_pk"));
                    rd.setCreatedDate(rs.getTimestamp("comment_dt"));
                    rd.setModifiedBy(rd.getCreatedBy());
                    rd.setModifiedDate(rd.getCreatedDate());
                    data.setRecordData(rd);
                    comments.add(data);
                } while (rs.next());
            } // else do nothing, comments Collection is null;
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return comments;
    }

    /**
     * Retrieves the Constitution Data for this affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk
     * @return the ConstitutionData object
     */
    public ConstitutionData getConstitutionData(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ConstitutionData data = null;
        RecordData rData = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_CONSTITUTION_DATA);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data = new ConstitutionData();
                rData = new RecordData();
                data.setAffPk(affPk);
                data.setDocumentUploaded(DBUtil.getBooleanFromShort(rs.getShort("has_document")).booleanValue());
                data.setAffiliationAgreementDate(rs.getTimestamp("aff_agreement_dt"));
                data.setMethodOfOfficerElectionCodePk(DBUtil.getIntegerOrNull(rs, "off_election_method"));
                data.setAutomaticDelegate(DBUtil.getBooleanFromShort(rs.getShort("auto_delegate_prvsn_fg")));
                data.setConstitutionalRegions(DBUtil.getBooleanFromShort(rs.getShort("const_regions_fg")));
                data.setMostCurrentApprovalDate(rs.getTimestamp("most_current_approval_dt"));
                data.setMeetingFrequencyCodePk(DBUtil.getIntegerOrNull(rs, "meeting_frequency"));
                data.setApproved(DBUtil.getBooleanFromShort(rs.getShort("approved_const_fg")));
                rData.setCreatedDate(rs.getTimestamp("created_dt"));
                rData.setCreatedBy(DBUtil.getIntegerOrNull(rs, "created_user_pk"));
                rData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                rData.setModifiedBy(DBUtil.getIntegerOrNull(rs, "lst_mod_user_pk"));
                data.setRecordData(rData);
            } // else do nothing. will return null;
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Retrieves the name or location of Constitution document.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return the name or location of the pdf, or null if it doesn't exist.
     *
     * @deprecated  This method is no longer needed since code was moved directly
     *              to ViewConstitutionDocumentServlet class for performance
     *              issues. This method has been coded to return null.
     */
    public byte[] getConstitutionDocument(Integer affPk) {
        return null;
    }

    /**
     * Retrieves the Employer Sectors pk for the given Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk The Affiliate Primary Key
     *
     * @return The Collection of Integer objects representing the sector code pk's.
     *         Returns null if no Employer Sectors found.
     */
    public Collection getEmployerSectors(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection sectors = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EMPLOYER_SECTORS_FOR_AFFILIATE);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                sectors = new ArrayList();
                do {
                    sectors.add(DBUtil.getIntegerOrNull(rs, 1));
                } while (rs.next());
                //logger.debug("++++++ Found " + sectors.size() + " Employer Sectors for pk = " + affPk + ". ++++++");
            } else {
                //logger.debug("------ Employer Sectors NOT Found for pk = " + affPk + ". ------");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return sectors;
    }

    /**
     * Retrieves the Financial Data for an affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return the FinancialData object, or null if an error occurs.
     */
    public FinancialData getFinancialData(Integer affPk) {
        if (affPk == null) {
            return null;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FinancialData data = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_FINANCIAL_DATA);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                data = new FinancialData();
                data.setAffPk(affPk);
                data.setEmployerIDNumber(rs.getString("employer_identification_num"));
                data.setPerCapitaStatAvg(DBUtil.getIntegerOrNull(rs, "per_cap_stat_avg"));
                data.setPerCapitaTaxPaymentMethod(rs.getString("per_cap_tax_payment_method"));
                data.setPerCapitaTaxLastPaidDate(rs.getTimestamp("per_cap_last_paid_dt"));
                data.setPerCapitaTaxLastMemberCount(DBUtil.getIntegerOrNull(rs, "per_cap_tax_last_mbr_cnt"));
                data.setPerCapitaTaxInfoLastUpdateDate(rs.getTimestamp("per_cap_tax_lst_info_upd_dt"));
                data.setComment(rs.getString("comment_txt"));
            } // else do nothing
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Retrieves the Membership Reporting data for the affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return the MRData object
     */
    public MRData getMembershipReportingData(Integer affPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MRData mrData = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBERSHIP_REPORTING);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {

                //get MRData
                mrData = new MRData();
                mrData.setAffPk(affPk);
                mrData.setAffStatus(new Integer(rs.getInt("aff_status")));
                int informationSource = rs.getInt("aff_info_reporting_source");
                mrData.setInformationSource(informationSource == 0 ? null : new Integer(informationSource));
                mrData.setNoCards(rs.getInt("unit_wide_no_mbr_cards_fg") == 1);
                mrData.setComment(rs.getString("comment_txt"));
                mrData.setNoPEMail(rs.getInt("unit_wide_no_pe_mail_fg") == 1);

                //get Record Data
                RecordData recordData = new RecordData();
                recordData.setCreatedDate(rs.getTimestamp("created_dt"));
                recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
                recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
                recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
                mrData.setRecordData(recordData);

                //get identifier
                mrData.setNewAffiliateId(getAffiliateData(affPk).getAffiliateId());

            } else {
                throw new EJBException("No Membership Reporting information found for affiliate with pk " + affPk);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return mrData;
    }

    /**
     * Retrieves the Affiliate directly above this one in the hierarchy.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk the Affiliate whose parent is to be retrieved
     * @return the AffiliateData object representing the parent Affiliate.
     */
    public AffiliateData getParentAffiliate(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AffiliateData data = null;
        Integer parentPk = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARENT_AFF_PK);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                int pk = rs.getInt(1);
                if (!rs.wasNull()) {
                    parentPk = new Integer(pk);
                    data = getAffiliateData(parentPk);
                }
            } // else do nothing. no parent is found, so just return null...
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return data;
    }

    /**
     * Retrieves the Affiliates that could be above this one based on the values
     * in the AffiliateIdentifier
     *
     * NOTE: THIS IS A NEW METHOD NOT IDENTIFIED IN DESIGN.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affId     the Affiliate whose parent is to be retrieved
     * @return          a Collection of AffiliateResults objects representing
     *                  the potential parent Affiliates. Returns NULL if no
     *                  parents found.
     */
    public Collection findParentCriteria(AffiliateIdentifier affId)  {
        AffiliateCriteria criteria = new AffiliateCriteria();
        criteria.setPage(0);
        criteria.setOrderBy(null);
        return findParentCriteria(affId, criteria);
    }

    /**
     * Retrieves the Affiliates that could be above this one based on the values
     * in the AffiliateIdentifier.
     *
     * NOTE: THIS IS A NEW METHOD NOT IDENTIFIED IN DESIGN.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affId     the Affiliate whose parent is to be retrieved
     * @param criteria  the AffiliateCriteria to be used for performing the
     *                  parent search
     * @return          a Collection of AffiliateResults objects representing
     *                  the potential parent Affiliates. Returns NULL if no
     *                  parents found.
     */
    public Collection findParentCriteria(AffiliateIdentifier affId, AffiliateCriteria criteria)  {
        //logger.debug("Inside findParentCriteria()");
        //logger.debug("    affId:     " + affId);
        //logger.debug("    criteria:  " + criteria);
        if (affId == null ||
            TextUtil.isEmptyOrSpaces(affId.getType()) ||
            TextUtil.isEmptyOrSpaces(affId.getState())
        ) {
            return null;
        }
        switch (affId.getType().charValue()) {
            case 'U':
                if (TextUtil.isEmptyOrSpaces(affId.getLocal())) {       // no Local for Sub Local was specified. No need to search.
                    return null;
                }
                criteria.setAffiliateIdType(new Character('L'));
                criteria.setAffiliateIdLocal(affId.getLocal());
                criteria.setAffiliateIdState(affId.getState());
                break;
            case 'L':
                if (TextUtil.isEmptyOrSpaces(affId.getCouncil())) {     // no Council for Local was specified. No need to search.
                    return null;
                }
                criteria.setAffiliateIdType(new Character('C'));
                // state not needed in criteria as council is unique and councils can have
                // locals across many states
                break;
            case 'S':
                if (TextUtil.isEmptyOrSpaces(affId.getCouncil())) {     // no Retiree Chapter for Sub Chapter was specified. No need to search.
                    return null;
                }
                criteria.setAffiliateIdType(new Character('R'));
                criteria.setAffiliateIdState(affId.getState());
                break;
            default:
                return null;
        }
        criteria.setAffiliateIdCouncil(affId.getCouncil());
        logger.debug("    calling searchAffiliates...");
        return searchAffiliates(criteria);
    }

    /**
     * Retrieves all of the Affiliates below an Affiliate in the hierarchy NOT including
     *  any children the Sub Affiliates may have.
     *
     * Ordering will be determined as follows:
     *
     * If an Admin Council is passed in, the Collection will contain the District Councils
     *  ordered by Council Number, if any exist.
     *
     * If a District Council or Retiree Chapter is passed in, the Collection will contain
     *  the Locals or Retiree Sub Chapters ordered by Local Number.
     *
     * If a Local is passed in, the Collection will contain the Sub Locals ordered by the
     *  Sub Unit Number, if any exist.
     *
     * If a Retiree Sub Chapter or a Sub Local is passed in, the Collection will be NULL.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return a Collection of AffiliateData objects representing the Sub Affiliates. NULL
     *  if no Sub Affiliates exist.
     */
    public List getSubAffiliates(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List subAffils = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_SUB_AFFILIATES);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                Integer subPk = null;
                AffiliateData sub = null;
                subAffils = new ArrayList();
                do {
                    subPk = DBUtil.getIntegerOrNull(rs, "aff_pk");
                    sub = getAffiliateData(subPk);
                    subAffils.add(sub);
                } while (rs.next());
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return subAffils;
    }

    /**
     * Retrieves all of the Affiliate PKs below an Affiliate in the hierarchy NOT including
     *  any children the Sub Affiliates may have.
     *
     * Ordering will be determined as follows:
     *
     * If an Admin Council is passed in, the Collection will contain the District Councils
     *  ordered by Council Number, if any exist.
     *
     * If a District Council or Retiree Chapter is passed in, the Collection will contain
     *  the Locals or Retiree Sub Chapters ordered by Local Number.
     *
     * If a Local is passed in, the Collection will contain the Sub Locals ordered by the
     *  Sub Unit Number, if any exist.
     *
     * If a Retiree Sub Chapter or a Sub Local is passed in, the Collection will be NULL.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return a List of Integer objects representing the Sub Affiliates. NULL
     *  if no Sub Affiliates exist.
     */
    public List getSubAffiliatePks(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List subAffils = null;
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_SUB_AFFILIATES);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                Integer subPk = null;
                AffiliateData sub = null;
                subAffils = new ArrayList();
                do {
                    subPk = DBUtil.getIntegerOrNull(rs, "aff_pk");
                    if (subPk != null) { // should never be null...
                        subAffils.add(subPk);
                    }
                } while (rs.next());
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return subAffils;
    }

    /**
     * Determines if a Constitution Document exists for this Affiliate.
     *
     * @deprecated  This method is no longer needed and has been coded to return false.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @return 'true' if a constitution document exists or 'false' if a document
     * doesn't exist in the database for this affiliate
     */
    public boolean hasConstitutionDocument(Integer affPk) {
        return false;
    }

    /**
     * Allows an Affiliate to be disassociated from the council that is above it in
     * the hierarchy.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk
     * @return 'true' if the association was removed, and 'false' otherwise.
     */
    public boolean removeAffiliatedCouncil(Integer affPk, Integer userPk) {
        //logger.debug("Inside removeAffiliatedCouncil.");
        if (affPk == null) {
            throw new EJBException("No Affiliate PK specified.");
        }
        AffiliateData data = getAffiliateData(affPk);
        if (data == null) {
            throw new EJBException("Affiliate cannot be found.");
        }
        if (data.getAffiliateId().getType().charValue() == 'U') {
            return false;
        } else {
            return updateParentAffiliate(affPk, null, userPk);
        }
    }


    /**
     * Allows an Affiliate to be associated with a new council.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk
     * @return '0' if the association was made, or some error code if association fails.
     */
    public int addAffiliatedCouncil(Integer affPk, Integer newCouncilPk, Integer userPk) {
        //logger.debug("Inside addAffiliatedCouncil.");
        if (affPk == null) {
            throw new EJBException("No Affiliate PK specified.");
        }
        AffiliateData data = getAffiliateData(affPk);
        AffiliateData council = getAffiliateData(newCouncilPk);
        if (data == null) {
            throw new EJBException("Affiliate cannot be found.");
        }
        if (data.getAffiliateId().getType().charValue() == 'U') {
            throw new EJBException("This action is not valid for Sub Locals.");
        }
        if (council != null) {
            // validate business rules
            switch (data.getAffiliateId().getType().charValue()) {
                case 'C':
                case 'R':
                    if (!council.getStatusCodePk().equals(AffiliateStatus.AC)) {
                        return AffiliateErrorCodes.ERROR_NEW_COUNCIL_AFFILIATION_NOT_ADMIN_COUNCIL;
                    }
                    break;
                case 'L':
                    if (council.getAffiliateId().getType().charValue() != 'C' ||
                        council.getStatusCodePk().equals(AffiliateStatus.AC)
                    ) {
                        return AffiliateErrorCodes.ERROR_NEW_COUNCIL_AFFILIATION_LOCAL_TO_DIST_COUNCIL;
                    }
                    break;
                case 'S':
                    if (council.getAffiliateId().getType().charValue() != 'R') {
                        return AffiliateErrorCodes.ERROR_NEW_COUNCIL_AFFILIATION_SUB_CHAP_TO_RET_CHAP;
                    }
                    break;
                default:
                    throw new EJBException("Invalid Affiliate Type found for pk = " + affPk);
            }
        }
        if (updateParentAffiliate(affPk, newCouncilPk, userPk)) {
            return 0;
        } else {
            return AffiliateErrorCodes.ERROR_PARENT_AFFILIATE_NOT_UPDATED;
        }

    }

    /**
     * Allows an Affiliate's parent to be changed. Automatically adjusts the
     * Council and Local numbers in the Affiliate Identifier fields accordingly.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk
     * @param newParentFk
     *
     * @return 'true' if the update was successful, and 'false' otherwise.
     */
    public boolean updateParentAffiliate(Integer affPk, Integer newParentFk, Integer userPk) {
        //logger.debug("Inside updateParentAffiliate.");
        //logger.debug("    Called with the following params:");
        //logger.debug("        affPk       = " + affPk);
        //logger.debug("        newParentFk = " + newParentFk);
        //logger.debug("        userPk      = " + userPk);
        if (affPk == null) {
            throw new EJBException("No Affiliate was specified for update.");
        }
        AffiliateData data = getAffiliateData(affPk);
        if (data == null) {
            throw new EJBException("Affiliate with pk = " + affPk + " was not found.");
        }
        Connection con = null;
        List changedAffils = null;

        try {
            con = DBUtil.getConnection();
            Integer oldParentFk = data.getParentFk();
            switch (data.getAffiliateId().getType().charValue()) {
                case 'C':
                    // if this is a District Council, oldParentFk must come from Admin Council, not from parentFk
                    AffiliateData adminCn = getAffiliatedAdminCouncil(data);
                    if (adminCn != null) {
                        oldParentFk = adminCn.getAffPk();
                    }
                case 'R':
                    updateAdminCouncilToDistCouncilAffiliation(newParentFk, affPk, con, userPk);
                    recordCouncilAffiliationChangeToHistory(affPk, oldParentFk, newParentFk, userPk);
                    return true;
                case 'S':
                case 'U': /** @TODO: Evaluate if needed during Mass Change functionality. */
                case 'L':
                    updateParentFk(affPk, newParentFk, con, userPk);
                    recordCouncilAffiliationChangeToHistory(affPk, oldParentFk, newParentFk, userPk);
                    String newCnNum = AffiliateIdentifier.DEFAULT_ID_NUMBER;
                    if (newParentFk != null) {
                        AffiliateData parent = getAffiliateData(newParentFk);
                        if (parent != null) {
                            newCnNum = parent.getAffiliateId().getCouncil();
                        }
                    }
                    changedAffils = updateCouncilNumber(affPk, newCnNum, con, userPk); // change history recorded inside the individual method.
                    if (data.getAffiliateId().getType().charValue() == 'U') {
                        /**  @TODO: update the local number as well */
                    }
                    return true;
                default:
                    return false;
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    private boolean updateAdminCouncilToDistCouncilAffiliation(Integer adminCnPk,
                                Integer distCnPk, Connection con, Integer userPk)
    throws SQLException {
        if (distCnPk == null) {
            throw new EJBException("District Council was not specified.");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (adminCnPk == null) {
            // delete existing council affiliation
            ps = con.prepareStatement(SQL_DELETE_COUNCIL_AFFILIATION);
            ps.setInt(1, distCnPk.intValue());
            int delete = ps.executeUpdate();
            if (delete != 1) {
                // not sure if we care...
            }
        } else {
            // make sure council affiliation doesn't already exist.
            ps = con.prepareStatement(SQL_SELECT_COUNCIL_AFFILIATION_FOR_DIST_CN);
            ps.setInt(1, distCnPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                // should never happen...
                throw new EJBException("Affiliate already has a Council Affiliation.");
            }
            DBUtil.cleanup(null, ps, rs);

            // insert new council affiliation.
            ps = con.prepareStatement(SQL_INSERT_COUNCIL_AFFILIATION);
            ps.setInt(1, distCnPk.intValue());
            ps.setInt(2, adminCnPk.intValue());
            int update = ps.executeUpdate();
            if (update != 1) {
                // not sure if we care...
            }
        }
        DBUtil.cleanup(null, ps, rs);
        return true;
    }

    private int updateParentFk(Integer affPk, Integer newParentFk, Connection con, Integer userPk)
    throws SQLException {
        //logger.debug("Inside updateParentFk.");
        PreparedStatement ps = con.prepareStatement(SQL_UPDATE_PARENT_AFF_PK);
        DBUtil.setNullableInt(ps, 1, newParentFk);
        ps.setInt(2, userPk.intValue());
        ps.setInt(3, affPk.intValue());
        int retVal = ps.executeUpdate();
        logger.debug("    updateParentFk returning: " + retVal);
        DBUtil.cleanup(null, ps, null);
        return retVal;
    }

    private List updateCouncilNumber(Integer affPk, String newCouncilNumber, Connection con, Integer userPk)
    throws SQLException {
        //logger.debug("Inside updateCouncilNumber.");
        List returnAffils = new ArrayList();
        returnAffils.add(getAffiliateData(affPk));
        List subAffils = getSubAffiliates(affPk);
        if (!CollectionUtil.isEmpty(subAffils)) {
            returnAffils.addAll(subAffils);
        }

        //Update one at a time since we have to check that the new affiliate id is unique, and if not, adjust it.
        AffiliateData currentAffil = null;
        AffiliateIdentifier currentId = null;
        AffiliateIdentifier oldId = null;
        Character newCode = null;
        PreparedStatement ps = con.prepareStatement(SQL_UPDATE_COUNCIL_NUMBER_AND_CODE);
        for (Iterator it = returnAffils.iterator(); it.hasNext(); ) {
            // check if doesAffiliateIdentifierExist(type, code, local, state, subUnit, council);
            currentAffil = (AffiliateData)it.next();
            currentId = currentAffil.getAffiliateId();
            oldId = new AffiliateIdentifier(currentId.getType(), currentId.getLocal(),
                                currentId.getState(), currentId.getSubUnit(),
                                currentId.getCouncil(), currentId.getCode(),
                                currentId.getAdministrativeLegislativeCouncil()
            );
            currentId.setCouncil(newCouncilNumber);

            if (doesAffiliateIdentifierExist(currentId)) {
                logger.debug("    for pk = " + currentAffil.getAffPk() + " updating council number and code.");
                newCode = getNextAffiliateSequenceCode(currentId);
                currentId.setCode(newCode);
            } else {
                logger.debug("    for pk = " + currentAffil.getAffPk() + " updating council number only.");
                newCode = currentId.getCode();
            }
            ps.setString(1, newCouncilNumber);
            ps.setString(2, newCode.toString());
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, currentAffil.getAffPk().intValue());
            ps.addBatch();
            recordChangeToHistory(currentAffil.getAffPk(), oldId, currentId, userPk);
        }
        int[] update = ps.executeBatch();

        DBUtil.cleanup(null, ps, null);
        if (update.length == returnAffils.size()) {
            return returnAffils;
        } else {
            throw new EJBException("MaintainAffiliateBean.updateCouncilNumber: Query and Update didn't match. DB probably didn't lock.");
        }
    }

    /**
     * Retrieves a Collection of Affiliates that match the criteria entered for the search.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affiliateCriteria criteria for which to search
     *
     * @return the Collection of AffiliateResult objects. Collection will be null
     *         if no results are found for the criteria entered.
     */
    public Collection searchAffiliates(AffiliateCriteria criteria) {
        //logger.debug("Inside searchAffiliates.");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Collection results = null;
        Character tmpTypeHolder = null;
        boolean tmpTypeChanged = false;
        //boolean includeInactive = false;

        try {
            con = DBUtil.getConnection();

		//	if (criteria.getIncludeInactive().booleanValue()) {
		//		includeInactive = true;
		//	}
		//	else {
		//		includeInactive = false;
		//	}

            int includeSubUnitType = 0;
            if ( (criteria.getIncludeSubUnits() != null) && (!criteria.getIncludeSubUnits().booleanValue()))
            {
				Character charU = new Character('U');
				Character charL = new Character('L');
				Character charC = new Character('C');
				tmpTypeHolder = criteria.getAffiliateIdType();

				if (charU.equals(criteria.getAffiliateIdType()))
					includeSubUnitType = 1;
				else if (charL.equals(criteria.getAffiliateIdType()))
					includeSubUnitType = 2;
          		else if (charC.equals(criteria.getAffiliateIdType()))
          			includeSubUnitType = 3;

          		criteria.setAffiliateIdType(null);
          		tmpTypeChanged = true;
			}

            buildAffiliateCriteriaQuery(builder, criteria);

            if (tmpTypeChanged)
            	criteria.setAffiliateIdType(tmpTypeHolder);

            if (!TextUtil.isEmptyOrSpaces(criteria.getOrderBy())) {
                StringBuffer sb = new StringBuffer(criteria.getOrderBy());
                if (criteria.getOrdering() < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                builder.addOrderBy(sb.toString().trim());
            } else {
                /* add default Sort fields:
                 * State/National Type
                 * Council
                 * Local
                 */
                builder.addOrderBy("state ASC");
                builder.addOrderBy("council ASC");
                builder.addOrderBy("local ASC");
            }

            if (includeSubUnitType == 1) {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.type = 'U' " , con, false);
				//else
				//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' AND a.type = 'U' " , con, false);
	    	}
            else if (includeSubUnitType == 2) {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE (a.type = 'L' OR a.type = 'U' OR a.type = '')" , con, false);
	    		//else
	    		//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' AND (a.type = 'L' OR a.type = 'U' OR a.type = '')" , con, false);
	    	}
            else {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE, con);
				//else
				//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' ", con);
	    	}


            rs = ps.executeQuery();


            if (rs.next()) {
                int count = 1;
                results = new ArrayList();
                AffiliateResult ar = null;
                AffiliateIdentifier affId = null;
                rs.absolute(criteria.getPage() * criteria.getPageSize() + 1);
                do {
                    ar = new AffiliateResult();
                    affId = new AffiliateIdentifier();

                    ar.setAffPk(DBUtil.getIntegerOrNull(rs, "empaffpk"));

					//////////////////
					String tmpEmp = rs.getString("curr_employer_name");

					String searchEmployerNm = criteria.getEmployerNm();

					if ((searchEmployerNm != null) && (searchEmployerNm.trim().length() > 0)) {
						if (tmpEmp.trim().toLowerCase().indexOf(searchEmployerNm.trim().toLowerCase()) < 0)
							continue;
					}

                    ar.setAffAbreviatedNm(tmpEmp);
                    /////////////

                    ar.setAffAbreviatedNm(rs.getString("curr_employer_name"));
                    ar.setActive(((rs.getString("active")).trim().equalsIgnoreCase("1"))? "Yes": "No");
                    ar.setEmpAffPk(rs.getInt("empAffPk"));

                    String tmpType = rs.getString("type");
                    if (tmpType != null)
                    	affId.setType(new Character(tmpType.charAt(0)));
                    //else
                    //	affId.setType(null);

                    //affId.setCode(new Character(rs.getString("aff_code").charAt(0)));// no need to check for null since field has NOT NULL constraint
                    affId.setLocal(rs.getString("local"));
                    affId.setState(rs.getString("state"));
                    affId.setSubUnit(rs.getString("chapter"));
                    affId.setCouncil(rs.getString("council"));

                    // set the admin/legis council field.
                    //affId.setAdministrativeLegislativeCouncil(getAdminCouncilNumber(ar.getAffPk()));
                    //logger.debug("    ************** affId = " + affId.toString());
                    ar.setAffiliateId(affId);
                    results.add(ar);
                } while ( rs.next() && ((count++) < criteria.getPageSize()) );
                logger.debug("Results built with " + results.size() + " item(s).");
            } else {
                logger.debug("Nothing returned.");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return results;
    }

    /**
     * Retrieves a Collection of Affiliates that match the criteria entered for the search.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affiliateCriteria criteria for which to search
     *
     * @return the Collection of AffiliateResult objects. Collection will be null
     *         if no results are found for the criteria entered.
     */
    public Collection searchPreAffiliates(AffiliateCriteria criteria) {
        //logger.debug("Inside searchPreAffiliates.");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Collection results = null;
        Character tmpTypeHolder = null;
        boolean tmpTypeChanged = false;
        //boolean includeInactive = false;
		System.out.println("------------------------searchPreAffiliates-----------------");
        try {
            con = DBUtil.getConnection();

            int includeSubUnitType = 0;

            buildPreAffiliateCriteriaQuery(builder, criteria);

            //if (tmpTypeChanged)
            //	criteria.setAffiliateIdType(tmpTypeHolder);

            if (!TextUtil.isEmptyOrSpaces(criteria.getOrderBy())) {
                StringBuffer sb = new StringBuffer(criteria.getOrderBy());
                if (criteria.getOrdering() < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                builder.addOrderBy(sb.toString().trim());
            } else {
                /* add default Sort fields:
                 * State/National Type
                 * Council
                 * Local
                 */

                builder.addOrderBy("council ASC");
                builder.addOrderBy("local ASC");
                builder.addOrderBy("curr_employer_name ASC");
 		}

	    /*
            if (includeSubUnitType == 1) {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.type = 'U' " , con, false);
				//else
				//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' AND a.type = 'U' " , con, false);
	    	}
            else if (includeSubUnitType == 2) {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE (a.type = 'L' OR a.type = 'U' OR a.type = '')" , con, false);
	    		//else
	    		//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' AND (a.type = 'L' OR a.type = 'U' OR a.type = '')" , con, false);
	    	}
            else {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE, con);
				//else
				//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' ", con);
	    	}
	    */

	    ps = builder.getPreparedStatement(SQL_SEARCH_PRE_AFFILIATE, con);
            rs = ps.executeQuery();


            if (rs.next()) {
                int count = 1;
                results = new ArrayList();
                AffiliateResult ar = null;
                AffiliateIdentifier affId = null;
                rs.absolute(criteria.getPage() * criteria.getPageSize() + 1);
                do {
                    ar = new AffiliateResult();
                    ar.setBatch_ID(rs.getString("Batch_ID"));
                    affId = new AffiliateIdentifier();

                    ar.setAffPk(DBUtil.getIntegerOrNull(rs, "empaffpk"));

					//////////////////
					String tmpEmp = rs.getString("curr_employer_name");

					String searchEmployerNm = criteria.getEmployerNm();

					if ((searchEmployerNm != null) && (searchEmployerNm.trim().length() > 0)) {
						if (tmpEmp.trim().toLowerCase().indexOf(searchEmployerNm.trim().toLowerCase()) < 0)
							continue;
					}

                    ar.setAffAbreviatedNm(tmpEmp);
                    /////////////

                    ar.setAffAbreviatedNm(rs.getString("curr_employer_name"));
                    // ar.setActive(((rs.getString("active")).trim().equalsIgnoreCase("1"))? "Yes": "No");
                    ar.setEmpAffPk(rs.getInt("empAffPk"));

                    //String tmpType = rs.getString("type");
                   // if (tmpType != null)
                   // 	affId.setType(new Character(tmpType.charAt(0)));
                    //else
                    //	affId.setType(null);

                    //affId.setCode(new Character(rs.getString("aff_code").charAt(0)));// no need to check for null since field has NOT NULL constraint
                    affId.setLocal(rs.getString("local"));
                    affId.setState(rs.getString("state"));
                    affId.setSubUnit(rs.getString("chapter"));
                    affId.setCouncil(rs.getString("council"));

                    // set the admin/legis council field.
                    //affId.setAdministrativeLegislativeCouncil(getAdminCouncilNumber(ar.getAffPk()));
                    //logger.debug("    ************** affId = " + affId.toString());
                    ar.setAffiliateId(affId);
                    results.add(ar);
                } while ( rs.next() && ((count++) < criteria.getPageSize()) );
                logger.debug("Results built with " + results.size() + " item(s).");
            } else {
                logger.debug("Nothing returned.");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return results;
    }

    /**
     * Retrieves the count of the number of Affiliates that match the criteria
     * entered for the search.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affiliateCriteria criteria for which to search
     *
     * @return the number of matching Affiliates.
     */
    public int getSearchAffiliatesCount(AffiliateCriteria criteria) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        int resultCount = 0;

        try {
            con = DBUtil.getConnection();
            buildAffiliateCriteriaQuery(builder, criteria);
            ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE_COUNT, con);
            rs = ps.executeQuery();
            if (rs.next()) {
                resultCount = rs.getInt(1);
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return resultCount;
    }

   /**
     * Retrieves the count of the number of employers that match the criteria
     * entered for the search.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affiliateCriteria criteria for which to search
     *
     * @return the number of matching Affiliates.
     */
    public int getSearchEmployerCount(AffiliateCriteria criteria) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Collection results = null;
		int total = 0;
		Character tmpTypeHolder = null;
		boolean tmpTypeChanged = false;
		//boolean includeInactive = false;

        try {
            con = DBUtil.getConnection();

			//if (criteria.getIncludeInactive().booleanValue()) {
			//	includeInactive = true;
			//}
			//else {
			//	includeInactive = false;
			//}

	    	int includeSubUnitType = 0;
            if ((criteria.getIncludeSubUnits() != null) && (!criteria.getIncludeSubUnits().booleanValue()))
            {
				Character charU = new Character('U');
				Character charL = new Character('L');
				Character charC = new Character('C');
				tmpTypeHolder = criteria.getAffiliateIdType();

				if (charU.equals(tmpTypeHolder))
					includeSubUnitType = 1;
				else if (charL.equals(tmpTypeHolder))
					includeSubUnitType = 2;
				else if (charC.equals(tmpTypeHolder))
				includeSubUnitType = 3;

				criteria.setAffiliateIdType(null);
				tmpTypeChanged = true;
	    	}

            buildAffiliateCriteriaQuery(builder, criteria);

			if (tmpTypeChanged)
            	criteria.setAffiliateIdType(tmpTypeHolder);


            // if sub units radio checked, and searching for councils
            // make sure unaffiliated locals are not included.
            // if (criteria.getIncludeSubUnits() != null
            //    && criteria.getIncludeSubUnits().booleanValue() ) {
            //    //&& new Character('C').equals(criteria.getAffiliateIdType())) {
            //     ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE, con);
            // }
            // else {
            //     ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.chapter = '' " , con, false);
            // }

            if (includeSubUnitType == 1) {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.type = 'U' " , con, false);
				//else
				//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' AND a.type = 'U' " , con, false);
	    	}
            else if (includeSubUnitType == 2) {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE (a.type = 'L' OR a.type = 'U' OR a.type = '')" , con, false);
	    		//else
	    		//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' AND (a.type = 'L' OR a.type = 'U' OR a.type = '')" , con, false);
	    	}
            else {
				//if (includeInactive)
					ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE, con);
				//else
				//	ps = builder.getPreparedStatement(SQL_SEARCH_AFFILIATE + " WHERE a.active = '1' ", con);
	    	}

            rs = ps.executeQuery();


            while (rs.next()) {
                total = total + 1;
            }

        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return total;
    }

    /**
     * Retrieves the count of the number of Affiliates Change History that match
     * the criteria entered for the search.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param AffiliateChangeCriteria criteria for which to search
     *
     * @return the number of matching Affiliates Change History.
     */
    public int getSearchChangeHistoryCount(AffiliateChangeCriteria acc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        org.afscme.enterprise.util.PreparedStatementBuilder.Criterion cr = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        int resultCount = 0;

        try {
            con = DBUtil.getConnection();
            // add affPk
            builder.addCriterion("aff_pk", acc.getAffPk());
            // add search from date
            if (acc.getChangeDateTo() != null) {
                acc.setChangeDateTo(DateUtil.incrementTimestampByDay(acc.getChangeDateTo()));
            }
            if (acc.getChangeDateFrom() != null && acc.getChangeDateTo() != null) {
                cr = new Criterion("chng_dt", acc.getChangeDateFrom());
                cr.setOperator("BETWEEN");
                cr.addValue(acc.getChangeDateTo());
                builder.addCriterion(cr);
            } else if (acc.getChangeDateFrom() != null) {
                cr = new Criterion("chng_dt", acc.getChangeDateFrom());
                cr.setOperator(">=");
                builder.addCriterion(cr);
            } else if (acc.getChangeDateTo() != null) {
                cr = new Criterion("chng_dt", acc.getChangeDateTo());
                cr.setOperator("<=");
                builder.addCriterion(cr);
            }

            // add search from section pk
            builder.addCriterion("aff_section", acc.getSectionCodePk());
            ps = builder.getPreparedStatement(SQL_SEARCH_CHANGE_HISTORY, con);
            rs = ps.executeQuery();

            while (rs.next()) {
                resultCount++;
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return resultCount;
    }

    /**
     * Retrieves a Collection of Affiliate Changes that match the criteria entered for the
     * search.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affiliateChangeCriteria criteria for which to search
     * @return the Collection of AffiliateChangeResult objects
     */
    public Collection searchChangeHistory(AffiliateChangeCriteria acc) {
        logger.debug("Inside searchChangeHistory");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        Collection results = null;
        AffiliateChangeResult changeResult = null;
        org.afscme.enterprise.util.PreparedStatementBuilder.Criterion cr = null;
        logger.debug("\n\n\n\nsearchChangeHistory ***************** affPk=" + acc.getAffPk() + "\n\n\n\n");

        try {
            con = DBUtil.getConnection();

            // add affPk
            builder.addCriterion("aff_pk", acc.getAffPk());
            // add search from date
            if (acc.getChangeDateTo() != null) {
                //toSearch = Timestamp.valueOf(acc.getChangeDateTo().toString().substring(0, 10) + " 23:59:59.999");
                acc.setChangeDateTo(DateUtil.incrementTimestampByDay(acc.getChangeDateTo()));
            }
            if (acc.getChangeDateFrom() != null && acc.getChangeDateTo() != null) {
                cr = new Criterion("chng_dt", acc.getChangeDateFrom());
                cr.setOperator("BETWEEN");
                cr.addValue(acc.getChangeDateTo());
                builder.addCriterion(cr);
            } else if (acc.getChangeDateFrom() != null) {
                cr = new Criterion("chng_dt", acc.getChangeDateFrom());
                cr.setOperator(">=");
                builder.addCriterion(cr);
            } else if (acc.getChangeDateTo() != null) {
                cr = new Criterion("chng_dt", acc.getChangeDateTo());
                cr.setOperator("<=");
                builder.addCriterion(cr);
            }

            // add search from section pk
            builder.addCriterion("aff_section", acc.getSectionCodePk());

            // add sort fields
            if (!TextUtil.isEmptyOrSpaces(acc.getOrderBy())) {
                StringBuffer sb = new StringBuffer(acc.getOrderBy());
                if (acc.getOrdering() < 0) {
                    sb.append(" DESC");
                } else {
                    sb.append(" ASC");
                }
                builder.addOrderBy(sb.toString().trim());
            } else {
                // Add default Sort field
                builder.addOrderBy("aff_section ASC");
            }

            ps = builder.getPreparedStatement(SQL_SEARCH_CHANGE_HISTORY, con);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = 1;
                results = new ArrayList();
                rs.absolute(acc.getPage() * acc.getPageSize() + 1);
                do {
                    changeResult = new AffiliateChangeResult();
                    changeResult.setAffiliatePk(acc.getAffPk());
                    changeResult.setChangedDate(rs.getTimestamp("chng_dt"));
                    changeResult.setSectionCodePk(DBUtil.getIntegerOrNull(rs, "aff_section"));
                    results.add(changeResult);
                } while ( rs.next() && ((count++) < acc.getPageSize()) );
                logger.debug("Results built with " + results.size() + " item(s).");
            } else {
                logger.debug("ResultSet was empty.");
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return results;
    }

    /**
     * Associates an Affiliate with a Council.
     *
     * If the Affiliate is a Local, then the Council must be a District council. If
     * the Affiliate is a District Council, then the Council must be an Administrative/
     * Legislative council.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affililate Primary Key
     * @param councilPk Council Primary Key
     * @return 'true' if association is made, and 'false' otherwise
     */
    public boolean setAffiliatedCouncil(Integer affPk, Integer councilPk)
    {
        return true;
    }

    /**
     * Allows for Affiliate Detail data to be updated.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affiliateData new Affiliate Data
     * @return 'true' if the update is completed, and 'false' otherwise
     */
    public void updateAffiliateData(AffiliateData data, Integer userPk) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        AffiliateData oldData = getAffiliateData(data.getAffPk());
        try {
            logger.debug("Update query: " + SQL_UPDATE_AFFILIATE_DETAIL);
            logger.debug("Update data:  " + data.toString().trim());
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AFFILIATE_DETAIL);
            DBUtil.setNullableVarchar(ps, 1, data.getAbbreviatedName());
            DBUtil.setNullableInt(ps, 2, data.getAfscmeRegionCodePk());
			DBUtil.setNullableInt(ps, 3, data.getAfscmeLegislativeDistrictCodePk());
            DBUtil.setNullableBooleanAsShort(ps, 4, data.getMultipleEmployers());
            DBUtil.setNullableBooleanAsShort(ps, 5, data.getAllowSubLocals());
            DBUtil.setNullableInt(ps, 6, data.getAnnualCardRunTypeCodePk());
            DBUtil.setNullableInt(ps, 7, data.getMemberRenewalCodePk());
            DBUtil.setNullableVarchar(ps, 8, data.getWebsite());
            DBUtil.setNullableBooleanAsShort(ps, 9, data.getMultipleOffices());
            ps.setInt(10, userPk.intValue());
            ps.setInt(11, data.getAffPk().intValue());

            int retVal = ps.executeUpdate();
            if (retVal == 1) {
                logger.debug("++++++++++ UPDATE Successful ++++++++++");
                // update the Employer Sector info
                updateEmployerSector(con, ps2, data.getAffPk(), data.getEmployerSector());
                // insert the new comment
                insertCommentForAffiliate(con, ps3, data.getAffPk(), data.getComment(), userPk);
            } else { // should not happen. will probably throw an exception.
                logger.debug("---------- UPDATE NOT Successful: Returned " + retVal + " ----------");
                throw new EJBException("Update not performed.");
            }
            recordChangeToHistory(oldData, data, userPk);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(null, ps, null);
            DBUtil.cleanup(null, ps2, null);
            DBUtil.cleanup(con, ps3, null);
        }
    }

    /**
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affililate batch_ID
     * @return 1 if association is made, and 0 otherwise
     */
    public String updatePreAffInfo(String batch_ID, String agmtEffDate, String agmtExpDate,
								String noMemFeePayer, String increase_type, String mbrsAfps_Affected, String adj_MbrsAfps_Affected,
								String ifRecInc, String ifInNego, String contactName, String contactPhoneEmail, String comment, String notes, String percentWageInc,
								String wageIncEffDate, String noMemFeePayerAff1, String centPerHrDoLumpSumBonus, String avgWagePerHrYr,
								String effDateInc, String noMemFeePayerAff2, String speWageAgj, String percentInc, String dollarCent, String avgPay,
								String noMemFeePayerAff3)
    {
        Connection con = null;
        PreparedStatement ps = null;
		int retVal = 1;

        try {
            con = DBUtil.getConnection();

            ps = con.prepareStatement(SQL_UPDATE_PRE_AFFILIATE_DETAIL);

            ps.setString(1, agmtEffDate); //F
            ps.setString(2, agmtExpDate);  //G
            ps.setString(3, noMemFeePayer); //H
            ps.setString(4, increase_type);
            ps.setString(5, mbrsAfps_Affected);
            ps.setString(6, adj_MbrsAfps_Affected);
            ps.setString(7, ifRecInc);
            ps.setString(8, ifInNego);
            ps.setString(9, contactName);
            ps.setString(10, contactPhoneEmail);
            //ps.setString(11, comment);
            ps.setString(11, notes);
            ps.setString(12, percentWageInc);
            ps.setString(13, wageIncEffDate);
            ps.setString(14, noMemFeePayerAff1); //M

            ps.setString(15, centPerHrDoLumpSumBonus); //O
            ps.setString(16, avgWagePerHrYr); //P
            ps.setString(17, effDateInc);	//Q
            ps.setString(18, noMemFeePayerAff2);  //R

            ps.setString(19, speWageAgj);  //S
            ps.setString(20, percentInc);	//T


            ps.setString(21, dollarCent); //V
            ps.setString(22, avgPay);
            ps.setString(23, noMemFeePayerAff3); //X
            ps.setString(24, batch_ID);

            //retVal = ps.executeUpdate();

            if (retVal == 1) {
                logger.debug("++++++++++ UPDATE Successful ++++++++++");
                // update the Employer Sector info
                retVal = 1;

            } else { // should not happen. will probably throw an exception.
                logger.debug("---------- UPDATE NOT Successful: Returned " + retVal + " ----------");
                // throw new EJBException("Update not performed.");
                retVal = 0;
            }
        } catch (Exception e) {

           // if (e instanceof EJBException) {
           //     throw (EJBException)e;
           // } else {
           //     throw new EJBException(e);
            //}

            retVal = 0;
        } finally {
            DBUtil.cleanup(con, ps, null);
        }

        return ""+retVal;

    }


    /**
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affililate batch_ID
     * @return "1" if association is made, and "0" otherwise
     */
    public String insertPreAffInfoProc(String batch_ID, String agmtEffDate, String agmtExpDate,
								String noMemFeePayer, String increase_type, String mbrsAfps_Affected, String adj_MbrsAfps_Affected,
								String ifRecInc, String ifInNego, String contactName, String contactPhoneEmail, String comment, String notes, String percentWageInc,
								String wageIncEffDate, String noMemFeePayerAff1, String centPerHrDoLumpSumBonus, String avgWagePerHrYr,
								String effDateInc, String noMemFeePayerAff2, String speWageAgj, String percentInc, String dollarCent, String avgPay,
								String noMemFeePayerAff3)
    {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        //PreparedStatement ps = null;
		int retVal = 1;
/*
        try {
            con = DBUtil.getConnection();

            cs = con.prepareCall("{? = call renjieProc(?)}");

            cs.registerOutParameter(1, java.sql.Types.STRING);
            cs.setString(2, batch_ID);

			cs.execute();

            retVal = cs.getInt(1);

            if (retVal == 1) {
                logger.debug("++++++++++ UPDATE Successful ++++++++++");
                // update the Employer Sector info
                retVal = 1;

            } else { // should not happen. will probably throw an exception.
                logger.debug("---------- UPDATE NOT Successful: Returned " + retVal + " ----------");
                // throw new EJBException("Update not performed.");
                retVal = 0;
            }
        } catch (Exception e) {
            retVal = 0;
        } finally {
            DBUtil.cleanup(con, cs, rs);
        }
*/
        return ""+retVal;
    }

    private void updateEmployerSector(Connection con, PreparedStatement ps,
                                         Integer affPk, Collection sectors)
    	throws SQLException {
        ps = con.prepareStatement(SQL_DELETE_AFF_EMPLOYER_SECTOR);
        ps.setInt(1, affPk.intValue());
        ps.executeUpdate();
        DBUtil.cleanup(null, ps, null);
        if (sectors != null && sectors.size() > 0) {
            ps = con.prepareStatement(SQL_INSERT_AFF_EMPLOYER_SECTOR);
            for (Iterator it = sectors.iterator(); it.hasNext(); ) {
                ps.setInt(1, affPk.intValue());
                ps.setObject(2, it.next());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertCommentForAffiliate(Connection con, PreparedStatement ps,
                                              Integer affPk, String comment,
                                              Integer userPk)
    	throws SQLException {
        if (!TextUtil.isEmptyOrSpaces(comment)) {
            ps = con.prepareStatement(SQL_INSERT_COMMENT_FOR_AFFILIATE);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, userPk.intValue());
            ps.setString(3, comment);
            ps.executeUpdate();
        }
    }

    /**
     * Allows the affiliates charter data to be updated. Does not update associated
     * councils, which is handled by other methods
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @param data new Charter Data
     * @return 'true' if the update is completed, and 'false' otherwise
     */
    public boolean updateCharterData(CharterData data, Integer userPk) {
        if (data == null || data.getAffPk() == null || data.getAffPk().intValue() < 1) {
            logger.debug(data);
            throw new EJBException("CharterData was invalid.");
        }
        if (userPk == null || userPk.intValue() < 1) {
            throw new EJBException("User was invalid.");
        }
        Connection con = null;
        PreparedStatement ps = null;
        CharterData oldData = getCharterData(data.getAffPk());
        try {
            logger.debug("Update query: " + SQL_UPDATE_CHARTER_DATA);
            logger.debug("Update data:  " + data.toString().trim());
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_CHARTER_DATA);
            DBUtil.setNullableVarchar(ps, 1, data.getName());
            DBUtil.setNullableVarchar(ps, 2, data.getJurisdiction());
            DBUtil.setNullableInt(ps, 3, data.getCharterCodeCodePk());
            DBUtil.setNullableTimestamp(ps, 4, data.getLastChangeEffectiveDate());
            DBUtil.setNullableTimestamp(ps, 5, data.getCharterDate());
            ps.setInt(6, userPk.intValue());
            ps.setInt(7, userPk.intValue());
            ps.setInt(8, data.getAffPk().intValue());

            int retVal = ps.executeUpdate();
            if (retVal == 1) {
                logger.debug("++++++++++ CHARTER UPDATE Successful ++++++++++");

                // update the charter counties
                if (!updateCharterCounties(con, data.getAffPk(), data.getCounties())) {
                    throw new EJBException("Charter Update not performed. Counties could not be inserted.");
                }
            } else { // should not happen. will probably throw an exception.
                logger.debug("---------- UPDATE NOT Successful: Return value = " + retVal + " ----------");
                throw new EJBException("Charter Update not performed.");
            }
            recordChangeToHistory(oldData, data, userPk);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return true;
    }

    private boolean updateCharterCounties(Connection con, Integer affPk,
                                          Collection counties)
    throws SQLException {
        PreparedStatement ps = con.prepareStatement(SQL_DELETE_CHARTER_COUNTIES);
        ps.setInt(1, affPk.intValue());
        ps.executeUpdate();
        if (!CollectionUtil.isEmpty(counties)) {
            DBUtil.cleanup(null, ps, null);
            ps = con.prepareStatement(SQL_INSERT_CHARTER_COUNTIES);
            for (Iterator it = counties.iterator(); it.hasNext(); ) {
                ps.setInt(1, affPk.intValue());
                ps.setObject(2, it.next(), Types.VARCHAR);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        DBUtil.cleanup(null, ps, null);
        return true;
    }

    /**
     * Allows constitution data to be updated for the affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @param constitutionData new Constitution Data
     *
     * @return 'true' if the update is completed, and 'false' otherwise
     *
     * @deprecated  This method is no longer needed since code was moved directly
     *              to SaveConstitutionInformationAction class for performance
     *              issues. This method has been coded to return false.
     */
    public boolean updateConstitutionData(ConstitutionData data, Integer userPk) {
        return false;
    }

    /**
     * Allows financial data to be updated for the affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param data      new Financial Data
     * @param userPk    The User's Primary Key
     *
     * @return 'true' if the update is completed, and 'false' otherwise
     */
    public boolean updateFinancialData(FinancialData data, Integer userPk) {
        if (data == null || data.getAffPk() == null || data.getAffPk().intValue() < 1) {
            logger.debug(data);
            throw new EJBException("FinancialData was invalid.");
        }
        if (userPk == null || userPk.intValue() < 1) {
            throw new EJBException("User was invalid.");
        }
        Connection con = null;
        PreparedStatement ps = null;
        FinancialData oldData = getFinancialData(data.getAffPk());

        try {
            logger.debug("Update query: " + SQL_UPDATE_FINANCIAL_DATA);
            logger.debug("Update data:  " + data.toString().trim());
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_FINANCIAL_DATA);
            DBUtil.setNullableVarchar(ps, 1, data.getEmployerIDNumber());
            ps.setInt(2, data.getAffPk().intValue());

            int retVal = ps.executeUpdate();
            if (retVal == 1) {
                logger.debug("++++++++++ FINANCIAL INFO UPDATE Successful ++++++++++");
            } else { // should not happen. will probably throw an exception.
                logger.debug("---------- FINANCIAL INFO UPDATE NOT Successful: Return value = " + retVal + " ----------");
                throw new EJBException("Financial Info Update not performed.");
            }
            recordChangeToHistory(oldData, data, userPk);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return true;
    }

    /**
     * Allows the affiliates Membership Reporting data to be updated. Does not update associated
     *  affiliates, which is handled by other methods for a Mass Change.
     *
     * Changes can be applied to 2 tables, the Aff_Organizations table for Affiliate Status
     *  and the Aff_Mbr_Rpt_Info for all other fields.
     *
     * The NewAffiliateIdentifier field cannot be updated from within this method. That is
     *  considered a Mass Change.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param affPk Affiliate Primary Key
     * @param mrData new Membership Reporting Data
     * @param userPk user making the change
     */
    public void updateMembershipReportingData(Integer affPk, MRData mrData, Integer userPk)
    {
        Connection con = null;
        PreparedStatement ps = null;

        MRData oldData = getMembershipReportingData(affPk);

        try {
            con = DBUtil.getConnection();

            if (mrData.getAffStatus() != null && !oldData.getAffStatus().equals(mrData.getAffStatus())) {
                ps = con.prepareStatement(SQL_UPDATE_AFFILIATE_STATUS);
                ps.setInt(1, mrData.getAffStatus().intValue());
                ps.setInt(2, affPk.intValue());
                ps.executeUpdate();
                ps.close();
                ps = null;
            }

            if (!oldData.equals(mrData)) {
                ps = con.prepareStatement(SQL_UPDATE_MEMBERSHIP_REPORTING);
                DBUtil.setNullableInt(ps, 1, mrData.getInformationSource());
                ps.setInt(2, mrData.isNoCards() ? 1 : 0);
                ps.setString(3, mrData.getComment());
                ps.setInt(4, mrData.isNoPEMail() ? 1 : 0);
                ps.setInt(5, mrData.isNoPEMail() ? 1 : 0);
                ps.setInt(6, affPk.intValue());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }

        recordChangeToHistory(oldData, mrData, userPk);
    }

    /**
     * Checks for one of the following 'valid' status':
     *      Administrative/Legislative Council
     *      Chartered
     *      Certified
     *      Not Chartered
     *      Pending Charter
     *      Restricted Administratorship
     *      Unrestricted Administratorship
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param The Affiliate's PK
     *
     * @return 'true' if one of the above status'. 'false' otherwise.
     */
    public boolean isValidAffiliate(Integer affPk) {
        AffiliateData data = getAffiliateData(affPk);
        if (data == null || data.getStatusCodePk() == null) {
            return false;
        }
        if (data.getStatusCodePk().equals(AffiliateStatus.AC) ||
            data.getStatusCodePk().equals(AffiliateStatus.C) ||
            data.getStatusCodePk().equals(AffiliateStatus.CU) ||
            data.getStatusCodePk().equals(AffiliateStatus.N) ||
            data.getStatusCodePk().equals(AffiliateStatus.PC) ||
            data.getStatusCodePk().equals(AffiliateStatus.RA) ||
            data.getStatusCodePk().equals(AffiliateStatus.UA)
        ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Records new Affiliate as changes for applicable fields in Affiliate Data,
     * Charter Data, and Constitution Data.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param newData AffiliateData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(NewAffiliate newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(NewAffiliate)");
        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        logger.debug("newData affPk         = " + newData.getAffPk());
        acd.setAffiliatePk(newData.getAffPk());
        logger.debug("changeData affPk      = " + acd.getAffiliatePk());
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setOldValue(CHANGE_HISTORY_VALUE_EMPTY);

        try {
            con = DBUtil.getConnection();

            acd.setSectionCodePk(AffiliateSections.DETAIL);

            // Affiliate Identifier
            AffiliateIdentifier newId = newData.getAffiliateId();
            recordChangeToHistory(newData.getAffPk(), null, newId, userPk);

            // Status
            acd.setFieldChangedCodePk(ChangeHistoryFields.STATUS);
            acd.setNewValue(getChangeHistoryValue(newData.getAffiliateStatusCodePk(), "AffiliateStatus"));
            insertChangeHistoryData(con, acd);

            // Legislative District
            acd.setFieldChangedCodePk(ChangeHistoryFields.AFSCME_LEG_DISTRICT);
            acd.setNewValue(getChangeHistoryValue(newData.getAfscmeLegislativeDistrict(), "LegislativeDistrict"));
            insertChangeHistoryData(con, acd);

            AffiliateData newAff = new AffiliateData();
            newAff.setAffPk(newData.getAffPk());
            // Abbr Name
            newAff.setAbbreviatedName(newData.getAffiliateName());
            // Card Run Type
            newAff.setAnnualCardRunTypeCodePk(newData.getAnnualCardRunTypeCodePk());
            // Region
            newAff.setAfscmeRegionCodePk(newData.getAffiliateRegionCodePk());
            // Multiple Employers
            newAff.setMultipleEmployers(newData.getMultipleEmployers());
            // Sub Locals allowed
            newAff.setAllowSubLocals(newData.getAllowSubLocals());
            recordChangeToHistory(null, newAff, userPk);

            CharterData newCharter = new CharterData();
            newCharter.setAffPk(newData.getAffPk());
            // Charter Name
            newCharter.setName(newData.getCharterName());
            // Charter Jurisdiction
            newCharter.setJurisdiction(newData.getCharterJurisdiction());
            // Charter Code
            newCharter.setCharterCodeCodePk(newData.getCharterCode());
            // Charter Date
            newCharter.setCharterDate(newData.getCharterDate());
            // Charter Counties
            newCharter.setCounties(newData.getCounties());
            recordChangeToHistory(null, newCharter, userPk);

            ConstitutionData constData = new ConstitutionData();
            constData.setAffPk(newData.getAffPk());
            // Approved Constitution
            constData.setApproved(newData.getApprovedConstitution());
            // Affiliation Agreement Date
            constData.setAffiliationAgreementDate(newData.getAffiliateAgreementDate());
            recordChangeToHistory(null, constData, userPk, con);

            // record new Council Affiliations (based on parentFk)
            recordCouncilAffiliationChangeToHistory(newData.getAffPk(), null, newData.getParentAffPk(), userPk);

            /** @TODO: Needs to be done as part of Generate Default Offices */
            // from default offices
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Affiliate Data and the new Affiliate Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData AffiliateData before the change
     * @param newData AffiliateData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(AffiliateData originalData, AffiliateData newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(AffiliateData)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for AffiliateData.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.DETAIL);

        try {
            con = DBUtil.getConnection();
            if (originalData == null) {
                originalData = new AffiliateData();
                originalData.setAffPk(newData.getAffPk());
            }
            acd.setAffiliatePk(originalData.getAffPk());
            logger.debug("\n\n\n\nOLD Data: " + originalData.toString());
            logger.debug("NEW Data: " + newData.toString()+ "\n\n\n\n");

            //Codes.ChangeHistoryFields.ABBREVIATED_NAME
            if (!TextUtil.equals(originalData.getAbbreviatedName(), newData.getAbbreviatedName())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.ABBREVIATED_NAME);
                acd.setOldValue(getChangeHistoryValue(originalData.getAbbreviatedName()));
                acd.setNewValue(getChangeHistoryValue(newData.getAbbreviatedName()));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.AFSCME_REGION
            if (!TextUtil.equals(originalData.getAfscmeRegionCodePk(), newData.getAfscmeRegionCodePk())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFSCME_REGION);
                acd.setOldValue(getChangeHistoryValue(originalData.getAfscmeRegionCodePk(), "Region"));
                acd.setNewValue(getChangeHistoryValue(newData.getAfscmeRegionCodePk(), "Region"));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.SUB_LOCALS_ALLOWED
            if (!TextUtil.equals(originalData.getAllowSubLocals(), newData.getAllowSubLocals())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.SUB_LOCALS_ALLOWED);
                acd.setOldValue(getChangeHistoryValue(originalData.getAllowSubLocals()));
                acd.setNewValue(getChangeHistoryValue(newData.getAllowSubLocals()));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.ANNUAL_CARD_RUN_TYPE
            if (!TextUtil.equals(originalData.getAnnualCardRunTypeCodePk(), newData.getAnnualCardRunTypeCodePk())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.ANNUAL_CARD_RUN_TYPE);
                acd.setOldValue(getChangeHistoryValue(originalData.getAnnualCardRunTypeCodePk(), "AffiliateCardRunType"));
                acd.setNewValue(getChangeHistoryValue(newData.getAnnualCardRunTypeCodePk(), "AffiliateCardRunType"));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.EMPLOYER_SECTORS
            if (!CollectionUtil.equals(originalData.getEmployerSector(), newData.getEmployerSector())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.EMPLOYER_SECTORS);
                acd.setOldValue(getChangeHistoryValue(originalData.getEmployerSector(), "EmployerSector"));
                acd.setNewValue(getChangeHistoryValue(newData.getEmployerSector(), "EmployerSector"));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.MEMBER_RENEWAL
            if (!TextUtil.equals(originalData.getMemberRenewalCodePk(), newData.getMemberRenewalCodePk())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.MEMBER_RENEWAL);
                acd.setOldValue(getChangeHistoryValue(originalData.getMemberRenewalCodePk(), "MemberRenewal"));
                acd.setNewValue(getChangeHistoryValue(newData.getMemberRenewalCodePk(), "MemberRenewal"));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.MULT_EMPLOYERS
            if (!TextUtil.equals(originalData.getMultipleEmployers(), newData.getMultipleEmployers())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.MULT_EMPLOYERS);
                acd.setOldValue(getChangeHistoryValue(originalData.getMultipleEmployers()));
                acd.setNewValue(getChangeHistoryValue(newData.getMultipleEmployers()));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.MULT_OFFICES
            if (!TextUtil.equals(originalData.getMultipleOffices(), newData.getMultipleOffices())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.MULT_OFFICES);
                acd.setOldValue(getChangeHistoryValue(originalData.getMultipleOffices()));
                acd.setNewValue(getChangeHistoryValue(newData.getMultipleOffices()));
                insertChangeHistoryData(con, acd);
            }
            //Codes.ChangeHistoryFields.WEBSITE
            if (!TextUtil.equals(originalData.getWebsite(), newData.getWebsite())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.WEBSITE);
                acd.setOldValue(getChangeHistoryValue(originalData.getWebsite()));
                acd.setNewValue(getChangeHistoryValue(newData.getWebsite()));
                insertChangeHistoryData(con, acd);
            }
			//Codes.ChangeHistoryFields.AFSCME_LEG_DISTRICT
			if (!TextUtil.equals(originalData.getAfscmeLegislativeDistrictCodePk(), newData.getAfscmeLegislativeDistrictCodePk())) {
				acd.setFieldChangedCodePk(ChangeHistoryFields.AFSCME_LEG_DISTRICT);
				acd.setOldValue(getChangeHistoryValue(originalData.getAfscmeLegislativeDistrictCodePk(),"LegislativeDistrict"));
				acd.setNewValue(getChangeHistoryValue(newData.getAfscmeLegislativeDistrictCodePk(),"LegislativeDistrict"));
				insertChangeHistoryData(con, acd);
			}
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Charter Data and the new Charter Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData CharterData before the change
     * @param newData CharterData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(CharterData originalData, CharterData newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(CharterData)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for CharterData.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.CHARTER);

        try {
            con = DBUtil.getConnection();
            if (originalData == null) {
                originalData = new CharterData();
                originalData.setAffPk(newData.getAffPk());
            }
            acd.setAffiliatePk(originalData.getAffPk());

            logger.debug("\n\n\n\nOLD Data: " + originalData.toString());
            logger.debug("NEW Data: " + newData.toString() + "\n\n\n\n");
            //ChangeHistoryFields.CHARTER_CODE
            if (!TextUtil.equals(originalData.getCharterCodeCodePk(), newData.getCharterCodeCodePk())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.CHARTER_CODE);
                acd.setOldValue(getChangeHistoryValue(originalData.getCharterCodeCodePk(), "CharterCode"));
                acd.setNewValue(getChangeHistoryValue(newData.getCharterCodeCodePk(), "CharterCode"));
                insertChangeHistoryData(con, acd);
            }
            //ChangeHistoryFields.CHARTER_DATE
            String oldCD = DateUtil.getSimpleDateString(originalData.getCharterDate());
            String newCD = DateUtil.getSimpleDateString(newData.getCharterDate());
            if (!TextUtil.equals(oldCD, newCD)) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.CHARTER_DATE);
                acd.setOldValue(getChangeHistoryValue(oldCD));
                acd.setNewValue(getChangeHistoryValue(newCD));
                insertChangeHistoryData(con, acd);
            }
            //ChangeHistoryFields.CHARTER_JURISDICTION
            if (!TextUtil.equals(originalData.getJurisdiction(), newData.getJurisdiction())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.CHARTER_JURISDICTION);
                acd.setOldValue(getChangeHistoryValue(originalData.getJurisdiction()));
                acd.setNewValue(getChangeHistoryValue(newData.getJurisdiction()));
                insertChangeHistoryData(con, acd, true);
            }
            //ChangeHistoryFields.CHARTER_NAME
            if (!TextUtil.equals(originalData.getName(), newData.getName())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.CHARTER_NAME);
                acd.setOldValue(getChangeHistoryValue(originalData.getName()));
                acd.setNewValue(getChangeHistoryValue(newData.getName()));
                insertChangeHistoryData(con, acd);
            }
            //ChangeHistoryFields.COUNTIES
            if (!CollectionUtil.equals(originalData.getCounties(), newData.getCounties())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.COUNTIES);
                acd.setOldValue(getChangeHistoryValue(originalData.getCounties()));
                acd.setNewValue(getChangeHistoryValue(newData.getCounties()));
                insertChangeHistoryData(con, acd);
            }
            //ChangeHistoryFields.LAST_CHANGE_EFFECTIVE_DATE
            String oldLED = DateUtil.getSimpleDateString(originalData.getLastChangeEffectiveDate());
            String newLED = DateUtil.getSimpleDateString(newData.getLastChangeEffectiveDate());
            if (!TextUtil.equals(oldLED, newLED)) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.LAST_CHANGE_EFFECTIVE_DATE);
                acd.setOldValue(getChangeHistoryValue(oldLED));
                acd.setNewValue(getChangeHistoryValue(newLED));
                insertChangeHistoryData(con, acd);
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Constitution Data and the new Constitution Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData  ConstitutionData before the change
     * @param newData       ConstitutionData after the change
     * @param userPk        User who requested the change
     */
    public void recordChangeToHistory(ConstitutionData originalData, ConstitutionData newData, Integer userPk) {
        Connection con = DBUtil.getConnection();
        recordChangeToHistory(originalData, newData, userPk, con);
        DBUtil.cleanup(con, null, null);
    }

    /**
     * Compares the original Constitution Data and the new Constitution Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData  ConstitutionData before the change
     * @param newData       ConstitutionData after the change
     * @param userPk        User who requested the change
     * @param con           Connection to use for making these updates.
     */
    public void recordChangeToHistory(ConstitutionData originalData, ConstitutionData newData, Integer userPk, Connection con) {
        //logger.debug("Inside recordChangeToHistory(ConstitutionData)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for ConstitutionData.");
            return;
        }

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.CONSTITUTION);

        try {
            if (originalData == null) {
                originalData = new ConstitutionData();
                originalData.setAffPk(newData.getAffPk());
            }
            acd.setAffiliatePk(originalData.getAffPk());

            logger.debug("Old Data: " + originalData);
            logger.debug("New Data: " + newData);

            // Most Currernt Approval Date
            String oldCAD = DateUtil.getSimpleDateString(originalData.getMostCurrentApprovalDate());
            String newCAD = DateUtil.getSimpleDateString(newData.getMostCurrentApprovalDate());
            if (!TextUtil.equals(oldCAD, newCAD)) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.MOST_CURRENT_APPROVAL_DATE);
                acd.setOldValue(getChangeHistoryValue(oldCAD));
                acd.setNewValue(getChangeHistoryValue(newCAD));
                insertChangeHistoryData(con, acd);
            }
            // Affiliation Agreement Date
            String oldAAD = DateUtil.getSimpleDateString(originalData.getAffiliationAgreementDate());
            String newAAD = DateUtil.getSimpleDateString(newData.getAffiliationAgreementDate());
            if (!TextUtil.equals(oldAAD, newAAD)) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFFILIATION_AGREEMENT_DATE);
                acd.setOldValue(getChangeHistoryValue(oldAAD));
                acd.setNewValue(getChangeHistoryValue(newAAD));
                insertChangeHistoryData(con, acd);
            }
            // Method of Officer Election
            if (!TextUtil.equals(originalData.getMethodOfOfficerElectionCodePk(), newData.getMethodOfOfficerElectionCodePk())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.METHOD_OF_OFFICER_ELECTION);
                acd.setOldValue(getChangeHistoryValue(originalData.getMethodOfOfficerElectionCodePk(), "MethodOffcrElection"));
                acd.setNewValue(getChangeHistoryValue(newData.getMethodOfOfficerElectionCodePk(), "MethodOffcrElection"));
                insertChangeHistoryData(con, acd);
            }
            // Constitutional Regions
            if (!TextUtil.equals(originalData.getConstitutionalRegions(), newData.getConstitutionalRegions())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.CONSTITUTIONAL_REGIONS);
                acd.setOldValue(getChangeHistoryValue(originalData.getConstitutionalRegions()));
                acd.setNewValue(getChangeHistoryValue(newData.getConstitutionalRegions()));
                insertChangeHistoryData(con, acd);
            }
            // Meeting Frequency for Affiliate Meetings
            logger.debug(originalData.getMeetingFrequencyCodePk());
            logger.debug(newData.getMeetingFrequencyCodePk());
            logger.debug("" + !TextUtil.equals(originalData.getMeetingFrequencyCodePk(), newData.getMeetingFrequencyCodePk()));
            if (!TextUtil.equals(originalData.getMeetingFrequencyCodePk(), newData.getMeetingFrequencyCodePk())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.MEETING_FREQUNECY);
                acd.setOldValue(getChangeHistoryValue(originalData.getMeetingFrequencyCodePk(), "MeetingFrequency"));
                acd.setNewValue(getChangeHistoryValue(newData.getMeetingFrequencyCodePk(), "MeetingFrequency"));
                insertChangeHistoryData(con, acd);
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        }
    }

    /**
     * Compares the original Financial Data and the new Financial Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData FinancialData before the change
     * @param newData FinancialData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(FinancialData originalData, FinancialData newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(FinancialData)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for FinancialData.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.FINANCIAL);

        try {
            con = DBUtil.getConnection();
            if (originalData == null) {
                originalData = new FinancialData();
                originalData.setAffPk(newData.getAffPk());
            }
            acd.setAffiliatePk(originalData.getAffPk());

            // Employer Identification Number
            if (!TextUtil.equals(originalData.getEmployerIDNumber(), newData.getEmployerIDNumber())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.EMPLOYER_ID_NUMBER);
                acd.setOldValue(getChangeHistoryValue(originalData.getEmployerIDNumber()));
                acd.setNewValue(getChangeHistoryValue(newData.getEmployerIDNumber()));
                insertChangeHistoryData(con, acd);
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Location Data and the new Location Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData LocationData before the change
     * @param newData LocationData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(LocationData originalData, LocationData newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(LocationData)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for LocationData.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.LOCATION);

        try {
            con = DBUtil.getConnection();
            if (originalData == null) {
                originalData = new LocationData();
                originalData.setOrgPk(newData.getOrgPk());
            } else if (newData == null) {
                newData = new LocationData();
                newData.setOrgPk(originalData.getOrgPk());
            }
            acd.setAffiliatePk(originalData.getOrgPk());

            logger.debug("\n\n\n\nOLD Data: " + originalData.toString());
            logger.debug("NEW Data: " + newData.toString() + "\n\n\n\n");
            // TITLE_OF_LOCATION
            if (!TextUtil.equals(originalData.getLocationNm(), newData.getLocationNm())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.TITLE_OF_LOCATION);
                acd.setOldValue(getChangeHistoryValue(originalData.getLocationNm()));
                acd.setNewValue(getChangeHistoryValue(newData.getLocationNm()));
                insertChangeHistoryData(con, acd);
            }
            // PRIMARY_LOCATION_FLAG
            if (!TextUtil.equals(originalData.getPrimaryLocationBoolean(), newData.getPrimaryLocationBoolean())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.PRIMARY_LOCATION_FLAG);
                acd.setOldValue(getChangeHistoryValue(originalData.getPrimaryLocationBoolean()));
                acd.setNewValue(getChangeHistoryValue(newData.getPrimaryLocationBoolean()));
                insertChangeHistoryData(con, acd);
            }
            if (!CollectionUtil.equals(originalData.getOrgAddressData(), newData.getOrgAddressData())) {
                OrgAddressData oldMainAddress = new OrgAddressData();
                OrgAddressData newMainAddress = new OrgAddressData();
                OrgAddressData oldShipAddress = new OrgAddressData();
                OrgAddressData newShipAddress = new OrgAddressData();
                OrgAddressData temp = null;

                if (!CollectionUtil.isEmpty(originalData.getOrgAddressData())) {
                    for (Iterator it = originalData.getOrgAddressData().iterator(); it.hasNext(); ) {
                        temp = (OrgAddressData)it.next();
                        if (temp.getType().equals(OrgAddressType.REGULAR)) {
                            oldMainAddress = temp;
                        } else if (temp.getType().equals(OrgAddressType.SHIPPING)) {
                            oldShipAddress = temp;
                        }
                    }
                }
                if (!CollectionUtil.isEmpty(newData.getOrgAddressData())) {
                    for (Iterator it = newData.getOrgAddressData().iterator(); it.hasNext(); ) {
                        temp = (OrgAddressData)it.next();
                        if (temp.getType().equals(OrgAddressType.REGULAR)) {
                            newMainAddress = temp;
                        } else if (temp.getType().equals(OrgAddressType.SHIPPING)) {
                            newShipAddress = temp;
                        }
                    }
                }

                // MAIN_ATTENTION
                if (!TextUtil.equals(oldMainAddress.getAttentionLine(), newMainAddress.getAttentionLine())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ATTENTION);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getAttentionLine()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getAttentionLine()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_ADDRESS_1
                if (!TextUtil.equals(oldMainAddress.getAddr1(), newMainAddress.getAddr1())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ADDRESS_1);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getAddr1()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getAddr1()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_ADDRESS_2
                if (!TextUtil.equals(oldMainAddress.getAddr2(), newMainAddress.getAddr2())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ADDRESS_2);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getAddr2()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getAddr2()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_CITY
                if (!TextUtil.equals(oldMainAddress.getCity(), newMainAddress.getCity())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_CITY);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getCity()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getCity()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_STATE
                if (!TextUtil.equals(oldMainAddress.getState(), newMainAddress.getState())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_STATE);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getState()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getState()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_ZIP_CODE
                if (oldMainAddress.getZipCode() != newMainAddress.getZipCode()) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ZIP_CODE);
                    acd.setOldValue(String.valueOf(oldMainAddress.getZipCode()));
                    acd.setNewValue(String.valueOf(newMainAddress.getZipCode()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_ZIP_4
                if (oldMainAddress.getZipPlus() != newMainAddress.getZipPlus()) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ZIP_4);
                    acd.setOldValue(String.valueOf(oldMainAddress.getZipPlus()));
                    acd.setNewValue(String.valueOf(newMainAddress.getZipPlus()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_COUNTY
                if (!TextUtil.equals(oldMainAddress.getCounty(), newMainAddress.getCounty())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_COUNTY);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getCounty()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getCounty()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_PROVINCE
                if (!TextUtil.equals(oldMainAddress.getProvince(), newMainAddress.getProvince())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_PROVINCE);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getProvince()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getProvince()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_COUNTRY
                if (!TextUtil.equals(oldMainAddress.getCountryPk(), newMainAddress.getCountryPk())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_COUNTRY);
                    // HLM: Fix defect 793
                    if (oldMainAddress.getCountryPk()!=null && oldMainAddress.getCountryPk().intValue()>0) {
                        acd.setOldValue(getChangeHistoryValue(oldMainAddress.getCountryPk(), "Country"));
                    } else {
                        acd.setOldValue(" ");
                    }
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getCountryPk(), "Country"));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_BAD_ADDRESS_FLAG
                if (oldMainAddress.getBad() != newMainAddress.getBad()) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_BAD_ADDRESS_FLAG);
                    acd.setOldValue(String.valueOf(oldMainAddress.getBad()));
                    acd.setNewValue(String.valueOf(newMainAddress.getBad()));
                    insertChangeHistoryData(con, acd);
                }
                // MAIN_DATE_MARKED_BAD
                if (!TextUtil.equals(oldMainAddress.getBadDate(), newMainAddress.getBadDate())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_DATE_MARKED_BAD);
                    acd.setOldValue(getChangeHistoryValue(oldMainAddress.getBadDate()));
                    acd.setNewValue(getChangeHistoryValue(newMainAddress.getBadDate()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_ATTENTION
                if (!TextUtil.equals(oldShipAddress.getAttentionLine(), newShipAddress.getAttentionLine())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ATTENTION);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getAttentionLine()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getAttentionLine()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_ADDRESS_1
                if (!TextUtil.equals(oldShipAddress.getAddr1(), newShipAddress.getAddr1())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ADDRESS_1);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getAddr1()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getAddr1()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_ADDRESS_2
                if (!TextUtil.equals(oldShipAddress.getAddr2(), newShipAddress.getAddr2())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ADDRESS_2);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getAddr2()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getAddr2()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_CITY
                if (!TextUtil.equals(oldShipAddress.getCity(), newShipAddress.getCity())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_CITY);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getCity()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getCity()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_STATE
                if (!TextUtil.equals(oldShipAddress.getState(), newShipAddress.getState())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_STATE);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getState()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getState()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_ZIP_CODE
                if (oldShipAddress.getZipCode() != newShipAddress.getZipCode()) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ZIP_CODE);
                    acd.setOldValue(String.valueOf(oldShipAddress.getZipCode()));
                    acd.setNewValue(String.valueOf(newShipAddress.getZipCode()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_ZIP_4
                if (oldShipAddress.getZipPlus() != newShipAddress.getZipPlus()) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_ZIP_4);
                    acd.setOldValue(String.valueOf(oldShipAddress.getZipPlus()));
                    acd.setNewValue(String.valueOf(newShipAddress.getZipPlus()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_COUNTY
                if (!TextUtil.equals(oldShipAddress.getCounty(), newShipAddress.getCounty())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_COUNTY);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getCounty()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getCounty()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_PROVINCE
                if (!TextUtil.equals(oldShipAddress.getProvince(), newShipAddress.getProvince())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_PROVINCE);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getProvince()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getProvince()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_COUNTRY
                if (!TextUtil.equals(oldShipAddress.getCountryPk(), newShipAddress.getCountryPk())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_COUNTRY);
                    // HLM: Fix defect 793
                    if (oldShipAddress.getCountryPk()!=null && oldShipAddress.getCountryPk().intValue()>0) {
                        acd.setOldValue(getChangeHistoryValue(oldShipAddress.getCountryPk(), "Country"));
                    } else {
                        acd.setOldValue(" ");
                    }
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getCountryPk(), "Country"));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_BAD_ADDRESS_FLAG
                if (oldShipAddress.getBad() != newShipAddress.getBad()) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_BAD_ADDRESS_FLAG);
                    acd.setOldValue(String.valueOf(oldShipAddress.getBad()));
                    acd.setNewValue(String.valueOf(newShipAddress.getBad()));
                    insertChangeHistoryData(con, acd);
                }
                // SHIP_DATE_MARKED_BAD
                if (!TextUtil.equals(oldShipAddress.getBadDate(), newShipAddress.getBadDate())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.MAIN_DATE_MARKED_BAD);
                    acd.setOldValue(getChangeHistoryValue(oldShipAddress.getBadDate()));
                    acd.setNewValue(getChangeHistoryValue(newShipAddress.getBadDate()));
                    insertChangeHistoryData(con, acd);
                }
            }

            if (!CollectionUtil.equals(originalData.getOrgPhoneData(), newData.getOrgPhoneData())) {
                OrgPhoneData oldOfficePhone = new OrgPhoneData();
                OrgPhoneData newOfficePhone = new OrgPhoneData();
                OrgPhoneData oldFaxPhone = new OrgPhoneData();
                OrgPhoneData newFaxPhone = new OrgPhoneData();
                OrgPhoneData temp = null;
                if (!CollectionUtil.isEmpty(originalData.getOrgPhoneData())) {
                    for (Iterator it = originalData.getOrgPhoneData().iterator(); it.hasNext(); ) {
                        temp = (OrgPhoneData)it.next();
                        if (temp.getPhoneType().equals(OrgPhoneType.LOC_PHONE_OFFICE)) {
                            oldOfficePhone = temp;
                        } else if (temp.getPhoneType().equals(OrgPhoneType.LOC_PHONE_FAX)) {
                            oldFaxPhone = temp;
                        }
                    }
                }
                if (!CollectionUtil.isEmpty(newData.getOrgPhoneData())) {
                    for (Iterator it = newData.getOrgPhoneData().iterator(); it.hasNext(); ) {
                        temp = (OrgPhoneData)it.next();
                        if (temp.getPhoneType().equals(OrgPhoneType.LOC_PHONE_OFFICE)) {
                            newOfficePhone = temp;
                        } else if (temp.getPhoneType().equals(OrgPhoneType.LOC_PHONE_FAX)) {
                            newFaxPhone = temp;
                        }
                    }
                }
                // OFFICE_COUNTRY_CODE
                if (!TextUtil.equals(oldOfficePhone.getCountryCode(), newOfficePhone.getCountryCode())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_COUNTRY_CODE);
                    acd.setOldValue(getChangeHistoryValue(oldOfficePhone.getCountryCode()));
                    acd.setNewValue(getChangeHistoryValue(newOfficePhone.getCountryCode()));
                    insertChangeHistoryData(con, acd);
                }
                // OFFICE_AREA_CODE
                if (!TextUtil.equals(oldOfficePhone.getAreaCode(), newOfficePhone.getAreaCode())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_AREA_CODE);
                    acd.setOldValue(getChangeHistoryValue(oldOfficePhone.getAreaCode()));
                    acd.setNewValue(getChangeHistoryValue(newOfficePhone.getAreaCode()));
                    insertChangeHistoryData(con, acd);
                }
                // OFFICE_PHONE_NUMBER
                if (!TextUtil.equals(oldOfficePhone.getPhoneNumber(), newOfficePhone.getPhoneNumber())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_PHONE_NUMBER);
                    acd.setOldValue(getChangeHistoryValue(oldOfficePhone.getPhoneNumber()));
                    acd.setNewValue(getChangeHistoryValue(newOfficePhone.getPhoneNumber()));
                    insertChangeHistoryData(con, acd);
                }
                // OFFICE_BAD_PHONE_FLAG
                if (!TextUtil.equals(oldOfficePhone.getPhoneBadFlag(), newOfficePhone.getPhoneBadFlag())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_BAD_PHONE_FLAG);
                    acd.setOldValue(getChangeHistoryValue(oldOfficePhone.getPhoneBadFlag()));
                    acd.setNewValue(getChangeHistoryValue(newOfficePhone.getPhoneBadFlag()));
                    insertChangeHistoryData(con, acd);
                }
                // OFFICE_DATE_MARKED_BAD
                if (!TextUtil.equals(oldOfficePhone.getPhoneBadDate(), newOfficePhone.getPhoneBadDate())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_DATE_MARKED_BAD);
                    acd.setOldValue(getChangeHistoryValue(oldOfficePhone.getPhoneBadDate()));
                    acd.setNewValue(getChangeHistoryValue(newOfficePhone.getPhoneBadDate()));
                    insertChangeHistoryData(con, acd);
                }
                // FAX_COUNTRY_CODE
                if (!TextUtil.equals(oldFaxPhone.getCountryCode(), newFaxPhone.getCountryCode())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_COUNTRY_CODE);
                    acd.setOldValue(getChangeHistoryValue(oldFaxPhone.getCountryCode()));
                    acd.setNewValue(getChangeHistoryValue(newFaxPhone.getCountryCode()));
                    insertChangeHistoryData(con, acd);
                }
                // FAX_AREA_CODE
                if (!TextUtil.equals(oldFaxPhone.getAreaCode(), newFaxPhone.getAreaCode())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_AREA_CODE);
                    acd.setOldValue(getChangeHistoryValue(oldFaxPhone.getAreaCode()));
                    acd.setNewValue(getChangeHistoryValue(newFaxPhone.getAreaCode()));
                    insertChangeHistoryData(con, acd);
                }
                // FAX_PHONE_NUMBER
                if (!TextUtil.equals(oldFaxPhone.getPhoneNumber(), newFaxPhone.getPhoneNumber())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_PHONE_NUMBER);
                    acd.setOldValue(getChangeHistoryValue(oldFaxPhone.getPhoneNumber()));
                    acd.setNewValue(getChangeHistoryValue(newFaxPhone.getPhoneNumber()));
                    insertChangeHistoryData(con, acd);
                }
                // FAX_BAD_PHONE_FLAG
                if (!TextUtil.equals(oldFaxPhone.getPhoneBadFlag(), newFaxPhone.getPhoneBadFlag())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_BAD_PHONE_FLAG);
                    acd.setOldValue(getChangeHistoryValue(oldFaxPhone.getPhoneBadFlag()));
                    acd.setNewValue(getChangeHistoryValue(newFaxPhone.getPhoneBadFlag()));
                    insertChangeHistoryData(con, acd);
                }
                // FAX_DATE_MARKED_BAD
                if (!TextUtil.equals(oldFaxPhone.getPhoneBadDate(), newFaxPhone.getPhoneBadDate())) {
                    acd.setFieldChangedCodePk(ChangeHistoryFields.OFFICE_DATE_MARKED_BAD);
                    acd.setOldValue(getChangeHistoryValue(oldFaxPhone.getPhoneBadDate()));
                    acd.setNewValue(getChangeHistoryValue(newFaxPhone.getPhoneBadDate()));
                    insertChangeHistoryData(con, acd);
                }
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Membership Reporting Data and the new Membership
     * Reporting Data and records any changes between the individual fields. One
     * entry per field will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData MRData before the change
     * @param newData MRData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(MRData originalData, MRData newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(MRData)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for MRData.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.MEMBERSHIP_REPORTING);

        try {
            con = DBUtil.getConnection();
            if (originalData == null) {
                originalData = new MRData();
                originalData.setAffPk(newData.getAffPk());
            }
            acd.setAffiliatePk(originalData.getAffPk());

            //originalData.getAffStatus()
            if (!TextUtil.equals(originalData.getAffStatus(), newData.getAffStatus())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.STATUS);
                acd.setOldValue(getChangeHistoryValue(originalData.getAffStatus(), "AffiliateStatus"));
                acd.setNewValue(getChangeHistoryValue(newData.getAffStatus(), "AffiliateStatus"));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getMbrshpInfoRptgSrcCodePk()
            if (!TextUtil.equals(originalData.getInformationSource(), newData.getInformationSource())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.MBRSHP_INFO_RPRTG_SOURCE);
                acd.setOldValue(getChangeHistoryValue(originalData.getInformationSource(), "InformationSource"));
                acd.setNewValue(getChangeHistoryValue(newData.getInformationSource(), "InformationSource"));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getNoCards()
            if (originalData.isNoCards() != newData.isNoCards()) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.UNIT_WIDE_NO_MEMBER_CARDS);
                acd.setOldValue(getChangeHistoryValue(String.valueOf(originalData.isNoCards())));
                acd.setNewValue(getChangeHistoryValue(String.valueOf(newData.isNoCards())));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getNoPEMail()
            if (originalData.isNoPEMail() != newData.isNoPEMail()) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.UNIT_WIDE_NO_PE_MAIL);
                acd.setOldValue(getChangeHistoryValue(String.valueOf(originalData.isNoPEMail())));
                acd.setNewValue(getChangeHistoryValue(String.valueOf(newData.isNoPEMail())));
                insertChangeHistoryData(con, acd);
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Affiliate Identifier Data and the new Affiliate
     * Identifier Data and records any changes between the individual fields.
     * One entry per field will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData AffiliateIdentifier before the change
     * @param newData AffiliateIdentifier after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(Integer affPk, AffiliateIdentifier originalData, AffiliateIdentifier newData, Integer userPk) {
        logger.debug("Inside recordChangeToHistory(AffiliateIdentifier)");
        if (originalData == null && newData == null) {
            logger.debug("Nothing to process in recordChangeToHistory for AffiliateIdentifiers.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.AFFILIATE_ID);

        try {
            con = DBUtil.getConnection();
            if (originalData == null) {
                originalData = new AffiliateIdentifier(null, null, null, null, null, null, null);
            } else if (newData == null) {
                newData = new AffiliateIdentifier(null, null, null, null, null, null, null);
            }
            acd.setAffiliatePk(affPk);

            //originalData.getCouncil()
            if (!TextUtil.equals(originalData.getCouncil(), newData.getCouncil())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFF_ID_COUNCIL);
                acd.setOldValue(getChangeHistoryValue(originalData.getCouncil()));
                acd.setNewValue(getChangeHistoryValue(newData.getCouncil()));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getLocal()
            if (!TextUtil.equals(originalData.getLocal(), newData.getLocal())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFF_ID_LOCAL);
                acd.setOldValue(getChangeHistoryValue(originalData.getLocal()));
                acd.setNewValue(getChangeHistoryValue(newData.getLocal()));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getState()
            if (!TextUtil.equals(originalData.getState(), newData.getState())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFF_ID_STATE);
                acd.setOldValue(getChangeHistoryValue(originalData.getState()));
                acd.setNewValue(getChangeHistoryValue(newData.getState()));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getSubUnit()
            if (!TextUtil.equals(originalData.getSubUnit(), newData.getSubUnit())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFF_ID_SUB_UNIT);
                acd.setOldValue(getChangeHistoryValue(originalData.getSubUnit()));
                acd.setNewValue(getChangeHistoryValue(newData.getSubUnit()));
                insertChangeHistoryData(con, acd);
            }
            //originalData.getType()
            if (!TextUtil.equals(originalData.getType(), newData.getType())) {
                acd.setFieldChangedCodePk(ChangeHistoryFields.AFF_ID_TYPE);
                acd.setOldValue(getChangeHistoryValue(originalData.getType()));
                acd.setNewValue(getChangeHistoryValue(newData.getType()));
                insertChangeHistoryData(con, acd);
            }
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    public void recordCouncilAffiliationChangeToHistory(Integer affPk, Integer oldCouncilPk, Integer newCouncilPk, Integer userPk) {
        logger.debug("Inside recordCouncilAffiliationChangeToHistory");
        if (oldCouncilPk == null && newCouncilPk == null) {
            logger.debug("Nothing to process in recordCouncilAffiliationChangeToHistory.");
            return;
        }

        Connection con = null;

        AffiliateChangeData acd = new AffiliateChangeData();
        acd.setAffiliatePk(affPk);
        acd.setChangedByUserPk(userPk);
        acd.setChangedDate(new Timestamp(System.currentTimeMillis()));
        acd.setSectionCodePk(AffiliateSections.CHARTER);

        try {
            con = DBUtil.getConnection();

            AffiliateData oldAffil = getAffiliateData(oldCouncilPk);
            AffiliateIdentifier oldId = new AffiliateIdentifier(null, null, null, null, null, null, null);
            if (oldAffil != null) {
                oldId = oldAffil.getAffiliateId();
            }

            AffiliateData newAffil = getAffiliateData(newCouncilPk);
            AffiliateIdentifier newId = new AffiliateIdentifier(null, null, null, null, null, null, null);
            if (newAffil != null) {
                newId = newAffil.getAffiliateId();
            }

            acd.setFieldChangedCodePk(ChangeHistoryFields.COUNCIL_AFFILIATIONS);
            acd.setOldValue(getChangeHistoryValue(oldId));
            acd.setNewValue(getChangeHistoryValue(newId));
            insertChangeHistoryData(con, acd);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, null, null);
        }
    }

    /**
     * Compares the original Office Data and the new Office Data and
     * records any changes between the individual fields. One entry per field
     * will be made in the database.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param originalData OfficeData before the change
     * @param newData OfficeData after the change
     * @param userPk User who requested the change
     */
    public void recordChangeToHistory(OfficeData originalData, OfficeData newData, Integer userPk) {

    }

    private void insertChangeHistoryData(Connection con, AffiliateChangeData data) throws SQLException {
        insertChangeHistoryData(con, data, false);
    }

    /**
     * Inserts information about a changed field into the database.
     *
     * @param data the AffiliateChangeData information to be inserted.
     */
    private void insertChangeHistoryData(Connection con, AffiliateChangeData data, boolean isJurisdiction) throws SQLException {
        logger.debug("Inside insertChangeHistoryData");
        PreparedStatement ps = con.prepareStatement(SQL_INSERT_AFF_CHANGE_HISTORY);
        ps.setInt(1, data.getAffiliatePk().intValue());
        ps.setInt(2, data.getSectionCodePk().intValue());
        ps.setInt(3, data.getChangedByUserPk().intValue());

        Integer transactionPk = DBUtil.insertAndGetIdentity(con, ps);
        DBUtil.cleanup(null, ps, null);

        if (isJurisdiction) {
            //logger.debug("    **** INSERTING INTO Aff_Chng_History_Juris_Dtl");
            ps = con.prepareStatement(SQL_INSERT_AFF_CHANGE_HISTORY_DETAIL_JURISDICTION);
            ps.setInt(1, transactionPk.intValue());
            ps.setShort(2, new Short("0").shortValue());
            ps.setString(3, data.getOldValue());
            ps.addBatch();
            ps.setInt(1, transactionPk.intValue());
            ps.setShort(2, new Short("1").shortValue());
            ps.setString(3, data.getNewValue());
            ps.addBatch();
            ps.executeBatch();
        } else {
            ps = con.prepareStatement(SQL_INSERT_AFF_CHANGE_HISTORY_DETAIL);
            ps.setInt(1, transactionPk.intValue());
            ps.setInt(2, data.getFieldChangedCodePk().intValue());
            ps.setString(3, data.getOldValue());
            ps.setString(4, data.getNewValue());
            ps.executeUpdate();
        }
        DBUtil.cleanup(null, ps, null);
    }

    /**
     * Retrieves the next code value for the given Affiliate Identifier.
     * @J2EE_METHOD  -- getNextAffiliateSequenceCode
     * @param id AffiliateIdentifier
     *
     * @return The Character representing the next code in the sequence.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public Character getNextAffiliateSequenceCode(AffiliateIdentifier id) {
        logger.debug("Inside getNextAffiliateSequenceCode");
        // Check for matching Affiliate Identifier
        AffiliateCriteria affIdSearch = new AffiliateCriteria();
        //affIdSearch.setAffiliateIdCode(id.getCode());
        affIdSearch.setAffiliateIdCouncil(id.getCouncil());
        affIdSearch.setAffiliateIdLocal(id.getLocal());
        affIdSearch.setAffiliateIdState(id.getState());
        affIdSearch.setAffiliateIdSubUnit(id.getSubUnit());
        affIdSearch.setAffiliateIdType(id.getType());
        Collection affIds = searchAffiliates(affIdSearch);
        if (CollectionUtil.isEmpty(affIds)) {
            logger.debug("    # of matching Affiliates: null");
        } else {
            logger.debug("    # of matching Affiliates: " + affIds.size());
        }

        char code = 'A';
        if (affIds != null && affIds.size() > 0) {
            char resultCode;
            for (Iterator it = affIds.iterator(); it.hasNext(); ) {
                resultCode = ((AffiliateResult)it.next()).getAffiliateId().getCode().charValue();
                code = (code >= resultCode) ? code : resultCode;
            }
            // increment code to the next value
            code += 1;
        }
        logger.debug("    getNextAffiliateSequenceCode returning: " + code);
        if (code >= 'A' || code <= 'Z') {
            return new Character(code);
        } else {
            return null;
        }
    }

   private void buildAffiliateCriteriaQuery(PreparedStatementBuilder pb, AffiliateCriteria ac) {
        pb.addCriterion("type", ac.getAffiliateIdType());
        pb.addCriterion("state", ac.getAffiliateIdState());
        pb.addCriterion("local", ac.getAffiliateIdLocal());
        pb.addCriterion("chapter", ac.getAffiliateIdSubUnit());
        pb.addCriterion("council", ac.getAffiliateIdCouncil());
        pb.addCriterion("active", ac.getIncludeInactive(), "ALL");

		if ( (ac.getEmployerNm() != null) && (ac.getEmployerNm().trim().length() >= 1) )
        		pb.addCriterion("curr_employer_name", "%"+ac.getEmployerNm()+"%", true);
    }

   private void buildPreAffiliateCriteriaQuery(PreparedStatementBuilder pb, AffiliateCriteria ac) {
        //pb.addCriterion("type", ac.getAffiliateIdType());
        pb.addCriterion("A", ac.getAffiliateIdState());
        pb.addCriterion("C", ac.getAffiliateIdLocal());
        pb.addCriterion("D", ac.getAffiliateIdSubUnit());
        pb.addCriterion("B", ac.getAffiliateIdCouncil());
        //pb.addCriterion("active", ac.getIncludeInactive(), "ALL");

	if ( (ac.getEmployerNm() != null) && (ac.getEmployerNm().trim().length() >= 1) )
		pb.addCriterion("E", "%"+ac.getEmployerNm()+"%", true);
    }

   private void buildEmployerCriteriaQuery(PreparedStatementBuilder pb, AffiliateCriteria ac, boolean includeChapter) {
        pb.addCriterion("type", ac.getAffiliateIdType());
        pb.addCriterion("state", ac.getAffiliateIdState());
        pb.addCriterion("local", ac.getAffiliateIdLocal());

        if (includeChapter)
        	pb.addCriterion("chapter", ac.getAffiliateIdSubUnit());
        else
        	pb.addCriterion("chapter", "\'\'");

        pb.addCriterion("council", ac.getAffiliateIdCouncil());
        //pb.addCriterion("n.employer", ac.getEmployerName());
    }

    /**
     *
     */
    private void buildAffiliateCriteriaQuery_old(PreparedStatementBuilder pb, AffiliateCriteria ac) {
        //ac.getNewAffiliateIdentifierSourceType()
        pb.addCriterion("n.aff_type", ac.getNewAffiliateIdentifierSourceType());
        //ac.getNewAffiliateIdentifierSourceLocal()
        pb.addCriterion("n.Aff_localSubChapter", ac.getNewAffiliateIdentifierSourceLocal());
        //ac.getNewAffiliateIdentifierSourceState()
        pb.addCriterion("n.Aff_stateNat_type", ac.getNewAffiliateIdentifierSourceState());
        //ac.getNewAffiliateIdentifierSourceSubUnit()
        pb.addCriterion("n.Aff_subUnit", ac.getNewAffiliateIdentifierSourceSubUnit());
        //ac.getNewAffiliateIdentifierSourceCouncil()
        pb.addCriterion("n.aff_councilRetiree_chap", ac.getNewAffiliateIdentifierSourceCouncil());
        //ac.getNewAffiliateIdentifierSourceCode()
        pb.addCriterion("n.Aff_code", ac.getNewAffiliateIdentifierSourceCode());
        //ac.getAffiliateStatusCodePk()
        pb.addCriterion("a.aff_status", ac.getAffiliateStatusCodePk());
        //ac.getAffiliateIdType()
        pb.addCriterion("a.aff_type", ac.getAffiliateIdType());
        //ac.getAffiliateIdLocal()
        pb.addCriterion("a.Aff_localSubChapter", ac.getAffiliateIdLocal());
        //ac.getAffiliateIdState()
        pb.addCriterion("a.Aff_stateNat_type", ac.getAffiliateIdState());
        //ac.getAffiliateIdSubUnit()
        pb.addCriterion("a.Aff_subUnit", ac.getAffiliateIdSubUnit());
        //ac.getAffiliateIdCouncil()
        pb.addCriterion("a.aff_councilRetiree_chap", ac.getAffiliateIdCouncil());
        //ac.getAffiliateIdCode()
        pb.addCriterion("a.Aff_code", ac.getAffiliateIdCode());
        // check for including sublocals
        if (ac.getIncludeSubUnits() != null &&
            ac.getIncludeSubUnits().booleanValue() &&
            ac.getAffiliateIdType() != null
        ) {
            switch(ac.getAffiliateIdType().charValue()) {
                case 'C':
                    pb.addCriterion("a.aff_type", new Character('L'));
                case 'L':
                    pb.addCriterion("a.aff_type", new Character('U'));
                    break;
                case 'R':
                    pb.addCriterion("a.aff_type", new Character('S'));
                    break;
                default:
                    // do nothing...
                    break;
            }
        }
        //ac.getAfscmeLegislativeDistrictCodePk()
        pb.addCriterion("a.aff_afscme_leg_district", ac.getAfscmeLegislativeDistrictCodePk());
        //ac.getAfscmeRegionCodePk()
        pb.addCriterion("a.aff_afscme_region", ac.getAfscmeRegionCodePk());
        //ac.getEmployerSectorCodePk()
        pb.addCriterion("es.aff_employer_sector", ac.getEmployerSectorCodePk());
        //ac.getAllowSubLocals()
        pb.addCriterion("a.aff_sub_locals_allowed_fg", ac.getAllowSubLocals());
        //ac.getMultipleEmployers()
        pb.addCriterion("a.aff_mult_employers_fg", ac.getMultipleEmployers());
        //ac.getMultipleOffices()
        pb.addCriterion("a.aff_multiple_offices_fg", ac.getMultipleOffices());
        //ac.getWebsite()
        pb.addCriterion("a.aff_web_url", ac.getWebsite());
        /*
         "       Org_Phone offph ON offph.org_locations_pk = l.org_locations_pk LEFT OUTER JOIN " +
         "       Org_Phone faxph ON faxph.org_locations_pk = l.org_locations_pk ";
         */
        //ac.getLocationAddress1()
        pb.addCriterion("adr.addr1", ac.getLocationAddress1());
        //ac.getLocationAddress2()
        pb.addCriterion("adr.addr2", ac.getLocationAddress2());
        //ac.getLocationAddressAttention()
        pb.addCriterion("adr.attention_line", ac.getLocationAddressAttention());
        //ac.getLocationAddressCity()
        pb.addCriterion("adr.city", ac.getLocationAddressCity());
        //ac.getLocationAddressCountryCodePk()
        pb.addCriterion("adr.country", ac.getLocationAddressCountryCodePk());
        //ac.getLocationAddressCounty()
        pb.addCriterion("adr.county", ac.getLocationAddressCounty());
        //ac.getLocationAddressProvince()
        pb.addCriterion("adr.province", ac.getLocationAddressProvince());
        //ac.getLocationAddressState()
        pb.addCriterion("adr.state", ac.getLocationAddressState());
        //ac.getLocationAddressUpdatedByUserID()
        if (!TextUtil.isEmptyOrSpaces(ac.getLocationAddressUpdatedByUserID())) {
            Integer personPk = usersBean.getPersonPK(ac.getLocationAddressUpdatedByUserID());
            pb.addCriterion("adr.lst_mod_user_pk", personPk);
        }
        //ac.getLocationAddressUpdatedDate()
        pb.addCriterion("adr.lst_mod_dt", ac.getLocationAddressUpdatedBeginDate(), ac.getLocationAddressUpdatedEndDate());
        //ac.getLocationAddressZip()
        pb.addCriterion("adr.zipcode", ac.getLocationAddressZip());
        //ac.getLocationAddressZip4()
        pb.addCriterion("adr.zip_plus", ac.getLocationAddressZip4());
        //ac.getLocationPhoneFaxAreaCode()
        pb.addCriterion("faxph.area_code", ac.getLocationPhoneFaxAreaCode());
        //ac.getLocationPhoneFaxCountryCode()
        pb.addCriterion("faxph.country_cd", ac.getLocationPhoneFaxCountryCode());
        //ac.getLocationPhoneFaxNumber()
        pb.addCriterion("faxph.phone_no", ac.getLocationPhoneFaxNumber());
        //ac.getLocationPhoneOfficeAreaCode()
        pb.addCriterion("offph.area_code", ac.getLocationPhoneOfficeAreaCode());
        //ac.getLocationPhoneOfficeCountryCode()
        pb.addCriterion("offph.country_cd", ac.getLocationPhoneOfficeCountryCode());
        //ac.getLocationPhoneOfficeNumber()
        pb.addCriterion("offph.phone_no", ac.getLocationPhoneOfficeNumber());

        if(ac.getParentAffFk() != null) {
			// restrict query to return current affiliate and subs
			Criterion parentAffPk = new Criterion("a.parent_aff_fk", ac.getParentAffFk());
        	pb.addCriterion(parentAffPk);


        	// return current affiliate only if there is a criteria match
        	AffiliateData ad  = getAffiliateData(ac.getParentAffFk());
        	AffiliateIdentifier ai = ad.getAffiliateId();
			if((ac.getAffiliateIdType() == null || ai.getType().equals(ac.getAffiliateIdType())) &&
			   (ac.getAffiliateIdLocal() == null || ai.getLocal().equals(ac.getAffiliateIdLocal())) &&
			   (ac.getAffiliateIdState() == null || ai.getState().equals(ac.getAffiliateIdState())) &&
			   (ac.getAffiliateIdSubUnit() == null || ai.getSubUnit().equals(ac.getAffiliateIdSubUnit())) &&
			   (ac.getAffiliateIdCouncil() == null || ai.getCouncil().equals(ac.getAffiliateIdCouncil())))
		   {
				Criterion affPk = new Criterion("a.aff_pk", ac.getParentAffFk());
				// use OR for field a.aff_pk
				affPk.setORFg(true);
        		pb.addCriterion(affPk);
			}
		}
    }

    private String getAdminCouncilNumber(AffiliateData data) {
        AffiliateData adminCn = getAffiliatedAdminCouncil(data);
        if (adminCn == null ||
            adminCn.getAffiliateId() == null ||
            TextUtil.equals(adminCn.getAffiliateId().getCouncil(), data.getAffiliateId().getCouncil())
        ) {
            return null;
        }
        return adminCn.getAffiliateId().getCouncil();
    }

    private String getAdminCouncilNumber(Integer affPk) {
        return getAdminCouncilNumber(getAffiliateData(affPk));
    }

    private String getChangeHistoryValue(String fieldValue) {
        if (TextUtil.isEmptyOrSpaces(fieldValue)) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        return fieldValue;
    }

    private String getChangeHistoryValue(Character fieldValue) {
        if (TextUtil.isEmptyOrSpaces(fieldValue)) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        return fieldValue.toString();
    }

    private String getChangeHistoryValue(Integer codePk, String codeType) {
        if (codePk == null) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        Map codeMap = codesBean.getCodes(codeType);
        CodeData code = (CodeData)codeMap.get(codePk);
        if (code == null) {
            throw new EJBException("Invalid Code with pk = " + codePk + " found in the database for Code Type " + codeType);
        } else {
            return code.getDescription();
        }
    }

    private String getChangeHistoryValue(Boolean fieldValue) {
        if (fieldValue == null) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        return fieldValue.toString().trim();
    }

    private String getChangeHistoryValue(Collection fieldValues) {
        if (CollectionUtil.isEmpty(fieldValues)) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        DelimitedStringBuffer dsb = new DelimitedStringBuffer(", ");
        for (Iterator it = fieldValues.iterator(); it.hasNext(); ) {
            dsb.append(getChangeHistoryValue((String)it.next()));
        }
        return dsb.toString().trim();
    }

    private String getChangeHistoryValue(Collection codePks, String codeType) {
        if (CollectionUtil.isEmpty(codePks)) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        DelimitedStringBuffer dsb = new DelimitedStringBuffer(", ");
        for (Iterator it = codePks.iterator(); it.hasNext(); ) {
            dsb.append(getChangeHistoryValue(((Integer)it.next()), codeType));
        }
        return dsb.toString().trim();
    }

    private String getChangeHistoryValue(Timestamp fieldValue) {
        return getChangeHistoryValue(DateUtil.getSimpleDateString(fieldValue));
    }

    private String getChangeHistoryValue(AffiliateIdentifier fieldValue) {
        if (fieldValue == null ||
            TextUtil.isEmptyOrSpaces(fieldValue.getCode()) ||
            TextUtil.isEmptyOrSpaces(fieldValue.getCouncil()) ||
            TextUtil.isEmptyOrSpaces(fieldValue.getLocal()) ||
            TextUtil.isEmptyOrSpaces(fieldValue.getState()) ||
            TextUtil.isEmptyOrSpaces(fieldValue.getSubUnit()) ||
            TextUtil.isEmptyOrSpaces(fieldValue.getType())
        ) {
            return CHANGE_HISTORY_VALUE_EMPTY;
        }
        return fieldValue.toDisplayString();
    }


    /**
     * @J2EE_METHOD  --  inactivateAffiliatesMembers
     *
     *  This method will set all members of the passed in affiliate to inactive.
     *
     * @param updatingPersonPk Person Primary Key
     * @param affPK Affiliate Primary Key

     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
    */
    public void inactivateAffiliatesMembers (Integer affPk, Integer updatingPersonPk) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_MEMBER_INACTIVATE);
            ps.setInt(2, affPk.intValue());
            ps.setInt(1, updatingPersonPk.intValue());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainAffiliatesBean.inactivateAffiliatesMembers method" + e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }


    /**
     * @J2EE_METHOD  --  inactivateAffiliatesOfficers
     *
     *  This method will set all officers of the passed in affiliate to inactive.
     *
     * @param updatingPersonPk Person Primary Key
     * @param affPK Affiliate Primary Key

     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
    */
    public void inactivateAffiliatesOfficers (Integer affPk, Integer updatingPersonPk) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_OFFICER_INACTIVATE);
            ps.setInt(2, affPk.intValue());
            ps.setInt(1, updatingPersonPk.intValue());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainAffiliatesBean.inactivateAffiliatesOfficers method" + e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

    /**
     * @J2EE_METHOD  --  suspendAffiliatesOfficers
     *
     *  This method will set all officers of the passed in affiliate to suspended.
     *
     * @param updatingPersonPk Person Primary Key
     * @param affPK Affiliate Primary Key

     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
    */
    public void suspendAffiliatesOfficers (Integer affPk, Integer updatingPersonPk) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_OFFICER_SUSPEND);
            ps.setInt(2, affPk.intValue());
            ps.setInt(1, updatingPersonPk.intValue());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainAffiliatesBean.suspendAffiliatesOfficers method" + e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

     /**
     * @J2EE_METHOD  --  getAffiliateAllowedSublocal
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
    */

    public boolean getAffiliateAllowedSublocal(Integer affPk) {
        logger.debug("Get Affiliate Allowed Sub Local (Integer affPk) method is entered.");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean allowed = true;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_DETAIL);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                logger.debug("The allowed sublocal is " + rs.getShort("aff_sub_locals_allowed_fg"));
                allowed = DBUtil.getBooleanFromShort(rs.getShort("aff_sub_locals_allowed_fg")).booleanValue();
                logger.debug("The allowed sublocal is " + allowed);
            }
         } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        logger.debug("Get Affiliate Allowed Sub Local (Integer affPk) method is existed.");
        return allowed;
    }

}
