package org.afscme.enterprise.update.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.GregorianCalendar;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import javax.ejb.*;
import java.util.Calendar;
import javax.naming.NamingException;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.address.ejb.*;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.officer.ejb.*;
import org.afscme.enterprise.codes.ejb.*;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.Codes.*;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.member.MemberAffiliateData;
import org.afscme.enterprise.member.NewMember;
import org.afscme.enterprise.member.ejb.*;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.person.EmailData;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.ejb.*;
import org.afscme.enterprise.update.*;
import org.afscme.enterprise.update.Codes.RebateAcceptanceCode;
import org.afscme.enterprise.update.Codes.UpdateFileType;
import org.afscme.enterprise.update.Codes.UpdateFileQueue;
import org.afscme.enterprise.update.filequeue.FileEntry;
import org.afscme.enterprise.update.member.*;
import org.afscme.enterprise.update.officer.*;
import org.afscme.enterprise.update.rebate.*;
import org.afscme.enterprise.update.participation.*;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.MailUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;
import org.afscme.enterprise.log.SystemLog;
/*********************************************************************************************/
//imports for officer methods
/********************************************************************************************/


/**
 * Contains the business methods that implement importing member, officer, rebate and
 *  participation files
 *
 * @ejb:bean name="Update" display-name="Update"
 * jndi-name="Update"
 * type="Stateless" view-type="local"
 */
public class UpdateBean extends SessionBase {

    protected static Logger logger = Logger.getLogger(UpdateBean.class);

    private static Integer DEPT_PK = org.afscme.enterprise.codes.Codes.Department.MD;
    private static int DEPT_PK_VALUE = org.afscme.enterprise.codes.Codes.Department.MD.intValue();
    private static int PHONE_HOME_PK_VALUE = org.afscme.enterprise.codes.Codes.PhoneType.HOME.intValue();

    private static final String REBATE_MEMBER_TYPE = "RebateMbrType";
    private static final String REBATE_MEMBER_STATUS = "RebateMbrStatus";
    private static final String DUES_TYPE = "DuesType";
    private static final String REBATE_DURATION = "RebateDuration";
    private static final String REBATE_ACCEPTANCE_CODE = "RebateAcceptanceCode";

    private FileQueue fqBean = null;
    private MaintainAffiliates affilBean = null;
    private MaintainCodes codesBean = null;
    private MaintainMembers membersBean = null;
    private MaintainPersons personsBean = null;
    private SystemAddress smaBean = null;
    private MaintainAffiliateOfficers officersBean = null;

    private static String CONFIG_VARIABLE_NAME = "AUPUpdateRecentTimeLimit";

    /**
     * Config value for last update configurable time limit (in days)
     */
    private String lastUpdateConfigTimeLimit;


    /**********************************************************************************/
    /* Member Pre-Update SQL                                                          */
    /**********************************************************************************/
    private static final String SQL_MEMBER_MATCH =
        "SELECT p.person_pk, first_nm, last_nm, middle_nm, prefix_nm, ssn, " +
        "       suffix_nm, address_pk, addr1, addr2, city, state, zipcode, zip_plus, " +
        "       addr_bad_fg, phone_pk, area_code, phone_no, political_party, " +
        "       political_registered_voter, mbr_no_local, mbr_join_dt, " +
        "       primary_information_source, mbr_status, mbr_type, gender, " +
        "       no_mail_fg, no_cards_fg, no_legislative_mail_fg, no_public_emp_fg, " +
        "       person_mst_lst_mod_dt, mbr_mst_lst_mod_dt " +
        "FROM Person p LEFT OUTER JOIN Aff_Members a " +
        "       ON p.person_pk = a.person_pk LEFT OUTER JOIN Person_Address ad " +
        "       ON p.person_pk = ad.person_pk AND ad.addr_source='" +
                    PersonAddress.SOURCE_AFFILIATE_STAFF +
        "' LEFT OUTER JOIN Person_Phone ph  ON p.person_pk = ph.person_pk " +
        "                   AND ph.dept = " + DEPT_PK_VALUE + " AND ph.phone_type = " + PHONE_HOME_PK_VALUE +
        "                   AND ph.phone_prmry_fg = 1 " +
        "LEFT OUTER JOIN Person_Political_Legislative l     ON p.person_pk = l.person_pk " +
        "LEFT OUTER JOIN Person_Demographics d              ON p.person_pk = d.person_pk ";

    private static final String SQL_WHERE_MEMBER_MATCH_AFF_MEMBER_NUMBER =
        "WHERE (aff_pk = ?) AND (mbr_no_local = ?)";

    private static final String SQL_WHERE_MEMBER_MATCH_SSN =
        "WHERE (aff_pk = ?) AND (ssn = ?)";

    /** Selects all states for Affiliates with the same info in same council or region.
        Where clause should be built dynamically. */
    private static final String SQL_SELECT_FIRST_STATE_FOR_SIMILAR_LOCALS =
        "SELECT aff_stateNat_type FROM Aff_Organizations ";

    /** Selects variable_name from app_config_data table  */
    private static final String SQL_SELECT_APP_CONFIG_VALUE =
        "SELECT variable_value " +
        "FROM   COM_App_Config_Data " +
        "WHERE  variable_name = ?";

    /** Selects all members for an affiliate that are not Inactive members  */
    private static final String SQL_SELECT_ALL_MEMBERS_FOR_AFFILIATE =
        "SELECT DISTINCT person_pk FROM Aff_Members WHERE aff_pk = ? " +
        "AND    mbr_status <> '" + MemberStatus.I + "'";

    private static final String SQL_SELECT_ALL_OFFICERS_FOR_AFFILIATE =
        "SELECT DISTINCT pos_expiration_dt, length_of_term " +
        "FROM Aff_Members AS t1 INNER JOIN Officer_History AS t2 " +
        "       ON (t1.aff_pk = t2.aff_pk) AND (t1.person_pk = t2.person_pk) " +
        "       INNER JOIN Aff_Officer_Groups AS t3 " +
        "       ON (t2.aff_pk = t3.aff_pk) AND (t2.office_group_id = t3.office_group_id) " +
        "       AND (t2.afscme_office_pk = t3.afscme_office_pk) " +
        "WHERE (t1.aff_pk = ?) AND (t1.person_pk = ?)";


    /**********************************************************************************/
    /* Rebate Pre-Update SQL                                                          */
    /**********************************************************************************/
    private static final String SQL_REBATE_MEMBER_MATCH =
        "SELECT rp.person_pk, first_nm, last_nm, middle_nm, ssn, duplicate_ssn_fg, " +
        "       address_pk, addr1, addr2, city, province, state, zipcode, zip_plus, country, " +
        "       mbr_status, mbr_type, mbr_dues_type, " +
        "       rebate_year_mbr_type, rebate_year_mbr_status, rebate_year_mbr_dues_rate,  " +
        "       roster_duration_in_aff, roster_acceptance_cd " +
        "FROM PRB_Roster_Persons rp " +
        "JOIN Person p                      ON p.person_pk = rp.person_pk " +
        "LEFT OUTER JOIN Person_Address ad  ON p.person_pk = ad.person_pk AND ad.addr_source='" +
                    PersonAddress.SOURCE_AFFILIATE_STAFF + "' " +
        "LEFT OUTER JOIN Aff_Members a      ON p.person_pk = a.person_pk ";

    private static final String SQL_WHERE_REBATE_MEMBER_MATCH_AFSCME_MEMBER_NUMBER =
        "WHERE (rp.aff_pk = ?) AND (rp.person_pk = ?) AND (rbt_year = ?) ";

    private static final String SQL_WHERE_REBATE_MEMBER_MATCH_SSN =
        "WHERE (rp.aff_pk = ?) AND (ssn = ?) AND (rbt_year = ?) ";

    private static final String SQL_WHERE_MATCH_FILE_GENERATED_DATE =
        "AND (file_generated_dt = ?) ";

    /** Selects the first person pk matched from the rebate update file and determines
        the file generated date to check for people removed or added to file  */
    private static final String SQL_SELECT_REBATE_FILE_GENERATED_DATE =
        "SELECT file_generated_dt " +
        "FROM   PRB_Roster_Persons " +
        "WHERE  aff_pk = ? " +
        "AND    rbt_year = ? " +
        "AND    roster_aff_status = ? " +
        "AND    person_pk = ? ";

    /** Selects count of rebate records for an affiliate on the preliminary roster for a certain file date  */
    private static final String SQL_SELECT_COUNT_REBATES_FOR_AFFILIATE_PRE_ROSTER =
        "SELECT COUNT(*) " +
        "FROM   PRB_Roster_Persons " +
        "WHERE  aff_pk = ? " +
        "AND    rbt_year = ? " +
        "AND    roster_aff_status = ? " +
        "AND    file_generated_dt = ? ";

    /** Selects personPk of rebate records for an affiliate on the preliminary roster for a certain file date  */
    private static final String SQL_SELECT_PERSON_REBATES_FOR_AFFILIATE_PRE_ROSTER =
        "SELECT person_pk " +
        "FROM   PRB_Roster_Persons " +
        "WHERE  aff_pk = ? " +
        "AND    rbt_year = ? " +
        "AND    roster_aff_status = ? " +
        "AND    file_generated_dt = ? ";    

    
    /**********************************************************************************/
    /* Member Select Methods                                                          */
    /**********************************************************************************/
    private static final String SQL_SELECT_MEMBER_PRE_UPDATE_SUMMARY =
        "SELECT	queue_pk, aff_pk, mbr_file_cnt, mbr_system_cnt, mbr_added_cnt, " +
        "       mbr_inactivated_cnt, mbr_changed_cnt, t_mbrs_created_cnt, " +
        "       mbr_nonmatched_cnt, mbr_matched_cnt, aff_warning_fg, aff_error_fg, " +
        "       aff_err_aff_type, aff_err_aff_localSubChapter, " +
        "       aff_err_aff_subUnit, aff_err_aff_councilRetiree_chap " +
        "FROM 	AUP_Member_Pre_Upd_Smry " +
        "WHERE	queue_pk=?";

    private static final String SQL_SELECT_PRE_ERR_SUMMARY =
        "Select     aff_pk                           "   +
        "           , queue_pk                       "   +
        "           , record_id                      "   +
        "           , person_pk                      "   +
        "           , last_nm                        "   +
        "           , middle_nm                      "   +
        "           , first_nm                       "   +
        "           , suffix_nm                      "   +
        "           , upd_error_type                 "   +
        "From       AUP_Pre_Err_Smry                 "   +
        "Where      aff_pk              =       ?    "   +
        "And        queue_pk            =       ?    "   ;

    private static final String SQL_SELECT_PRE_ERR_DETAIL =
       "Select           dtl.aff_pk                             "   +
       "                , dtl.queue_pk                          "   +
       "                , dtl.record_id                         "   +
       "                , dtl.upd_fld_nm                        "   +
       "                , dtl.fld_value_in_system               "   +
       "                , dtl.fld_value_in_file                 "   +
       "                , dtl.fld_error_fg                      "   +
       "                , cc.com_cd_desc                        "   +
       "From                                                    "   +
       "		AUP_Pre_Err_Dtl     	dtl             "   +
       " 		, Common_Codes		cc              "   +
       "Where                                                   "   +
       "                dtl.aff_pk         	=       ?       "   +
       "	And 	dtl.queue_pk       	=       ?       "   +
       "	And	dtl.record_id      	=       ?       "   +
       "        And	cc.com_cd_pk		= dtl.upd_fld_nm"   ;

    private static final String SQL_SELECT_PRE_FLD_CHG_DETAIL =
        "Select         queue_pk                                    "   +
        "               , upd_field_nm                              "   +
        "               , upd_field_chg_cnt                         "   +
        "               , cc.com_cd_desc                            "   +
        "From                                                       "   +
        "               AUP_Pre_Fld_Chg_Smry    smry                "   +
        "               , Common_Codes		cc                  "   +
        "Where                                                      "   +
        "               smry.queue_pk           =       ?           "   +
        "        And	cc.com_cd_pk		= smry.upd_field_nm "   +
        "ORDER BY	upd_field_nm                                "   ;

    private static final String SQL_SELECT_REVIEW_SUMMARY =
        "Select     aff_pk                              "   +
        "           , queue_pk                          "   +
        "           , trans_cmpltd_cnt                  "   +
        "           , trans_attempted_cnt               "   +
        "           , trans_err_cnt                     "   +
        "           , adds_attempted_cnt                "   +
        "           , adds_cmpltd_cnt                   "   +
        "           , inactReplace_cmpltd_cnt           "   +
        "           , inactReplace_attempted_cnt        "   +
        "           , chg_attempted_cnt                 "   +
        "           , chg_cmpltd_cnt                    "   +
        "           , inactReplace_madeTemp_cnt         "   +
        "           , vacant_cmpltd_cnt                 "   +
        "           , vacant_attempted_cnt              "   +
        "From                                           "   +
        "           AUP_Rv_Smry         smry            "   +
        "Where                                          "   +
        "           smry.queue_pk   =   ?               "   ;

    private static final String SQL_SELECT_RV_ERROR_SUMMARY =
        "Select     aff_pk                           "   +
        "           , queue_pk                       "   +
        "           , record_id                      "   +
        "           , person_pk                      "   +
        "           , last_nm                        "   +
        "           , middle_nm                      "   +
        "           , first_nm                       "   +
        "           , suffix_nm                      "   +
        "           , fld_error_type                 "   +
        "From       AUP_Rv_Err_Smry                  "   +
        "Where      aff_pk              =       ?    "   +
        "And        queue_pk            =       ?    "   ;

    private static final String SQL_SELECT_RV_ERR_DETAIL =
       "Select           dtl.aff_pk                             "   +
       "                , dtl.queue_pk                          "   +
       "                , dtl.record_id                         "   +
       "                , dtl.upd_fld_nm                        "   +
       "                , dtl.fld_value_in_system               "   +
       "                , dtl.fld_value_in_file                 "   +
       "                , dtl.fld_error_fg                      "   +
       "                , cc.com_cd_desc                        "   +
       "From                                                    "   +
       "		AUP_Rv_Err_Dtl     	dtl             "   +
       " 		, Common_Codes		cc              "   +
       "Where                                                   "   +
       "                dtl.aff_pk         	=       ?       "   +
       "	And 	dtl.queue_pk       	=       ?       "   +
       "	And	dtl.record_id      	=       ?       "   +
       "        And	cc.com_cd_pk		= dtl.upd_fld_nm"   ;

    /**********************************************************************************/    
    /* Officer Select Methods                                                         */
    /**********************************************************************************/
    
    private static final String SQL_SELECT_OFF_PRE_EDIT_SUMMARY =
        "Select     aff_pk                                      "   +
        "           , queue_pk                                  "   +
        "           , off_cards_cnt                             "   +
        "           , off_vacant_cnt                            "   +
        "           , off_chg_cnt                               "   +
        "           , off_replaced_cnt                          "   +
        "           , off_file_cnt                              "   +
        "           , off_system_cnt                            "   +
        "           , off_add_cnt                               "   +
        "           , aff_error_fg                              "   +
        "           , aff_warning_fg                            "   +
        "           , aff_err_aff_type                          "   +
        "           , aff_err_aff_localSubChapter               "   +
        "           , aff_err_aff_subUnit                       "   +
        "           , aff_err_aff_councilRetiree_chap           "   +
        "           , aff_err_aff_stateNat_type                 "   +
        "From       AUP_Officer_Pre_Upd_Smry                    "   +
        "Where      queue_pk            =       ?               "   ;
    //
    private static final String SQL_SELECT_OFF_FLD_CHG_DETAIL =
        "Select         queue_pk                                "   +
        "               , upd_field_nm                          "   +
        "               , upd_field_chg_cnt                       "   +
        "               , cc.com_cd_desc                        "   +
        "From                                                   "   +
        "               AUP_Pre_Fld_Chg_Smry    smry        "   +
        "               , Common_Codes              cc          "   +
        "Where                                                  "   +
        "               smry.queue_pk   =       ?               "   +
        "        And	cc.com_cd_pk	= smry.upd_field_nm     "   ;

     private static final String SQL_SELECT_OFF_EDIT_DETAIL =
     "select                                                        "   +
     "           pre.aff_pk                                         "   +           
     "           , pre.queue_pk                                     "   +
     "		 , pre.office_group_id                              "   +
     "		 , pre.afscme_office_pk                             "   +                       
     "           , com_cd_cd 'month_of_election'                    "   +          
     "           , grp.affiliate_office_title                       "   +          
     "           , grp.current_term_end                             "   +          
     "           , grp.office_group_id                              "   +          
     "           , grp.aff_pk                                       "   +          
     "           , grp.afscme_office_pk                             "   +          
     "           , aoff.afscme_title_desc                           "   +
     " 	         , pre.officers_in_file_cnt                         "   +           
     "           , pre.officers_allowed_num                         "   +              
     " from                                                         "   +    
     "		AFSCME_Offices 		aoff                        "   +
     "	,	AUP_Officer_Pre_Off_dtl	pre                         "   +
     "	,	Aff_Officer_Groups	grp                         "   +
     "	,	common_codes		cc                          "   +
     " where                                                            "   +
     "          pre.aff_pk              =   ?                           "   +
     "  and     pre.queue_pk            =   ?                           "   +
     "	and	pre.office_group_id	=	grp.office_group_id     "   +
     "	and	pre.afscme_office_pk	=	grp.afscme_office_pk    "   +
     "	and	pre.afscme_office_pk	=	aoff.afscme_office_pk	"   +		
     "	and	pre.aff_pk		=	grp.aff_pk		"   +
     "	and	grp.month_of_election	=	cc.com_cd_pk  	   	"   ;
     private static final String SQL_SELECT_ALL_OFF_EDIT_DETAIL =
     "select                                                        "   +
     "           grp.aff_pk                                         "   +           
     "           , grp.afscme_office_pk                             "   +
     "		 , grp.office_group_id                              "   +
     "		 , grp.afscme_office_pk                             "   +                       
     "           , aoff.afscme_title_desc                           "   +          
     "           , com_cd_cd 'month_of_election'                    "   +          
     "           , grp.affiliate_office_title                       "   +          
     "           , grp.current_term_end                             "   +          
     "           , grp.max_number_in_office                         "   +          
//     "           , grp.afscme_office_pk                             "   +          
//     "           , aoff.afscme_title_desc                           "   +                
     " from                                                         "   +    
     "		AFSCME_Offices 		aoff                        "   +
     "	,	Aff_Officer_Groups	grp                         "   +
     "	,	common_codes		cc                         "   +
     " where                                                            "   +
     "          grp.aff_pk              =   ?                           "   +
     "	and	grp.afscme_office_pk	=	aoff.afscme_office_pk	"   +		
     "	and	grp.month_of_election	=	cc.com_cd_pk     	"   ;
     
        /*"select                                                             "   +
        "        pre.aff_pk                                                 "   +
        "        , pre.queue_pk                                             "   +
        "        , grps.month_of_election                                   "   +
        "        , grps.affiliate_office_title                              "   +
        "        , grps.current_term_end                                    "   +
        "        , grps.office_group_id                                     "   +
        "        , grps.aff_pk                                              "   +
        "        , grps.afscme_office_pk                                    "   +
        "        , aoff.afscme_title_desc                                   "   +
        //"    --  , cc.com_cd_desc                                           "   +
        "        , pre.officers_in_file_cnt                                 "   +
        "        , pre.officers_allowed_num                                 "   +
        "from                                                               "   +
        "        Aff_Officer_Groups		grps                        "   +
        "        , AFSCME_Offices		aoff                        "   +
        "    	, Common_Codes			cc                          "   +
        "        , AUP_Officer_Pre_Off_Dtl	pre                         "   +
        "        , AUP_Queue_Mgmt		q                           "   +
        "where                                                              "   +
        "               pre.aff_pk              =   ?                       "   +
        "        And    pre.queue_pk            =   ?                       "   +
        "        And	pre.aff_pk              =   grps.aff_pk             "   +
        "        And	pre.queue_pk            =   q.queue_pk              "   +
        "        And    grps.afscme_office_pk	=   aoff.afscme_office_pk   "   +
        "	 And	cc.com_cd_pk            =   aoff.afscme_office_pk   "   +
        "        And    cc.com_cd_pk            in  (?)                     "   ;
        */
        private static final String SQL_SELECT_AFFILIATE_OFFICERS_OLD =
         "select    DISTINCT                                                "   +
         "          hist.aff_pk                                           "   +
         "          ,mem.person_pk                                           "   +
         "          ,grp.length_of_term                                     "   +
         "          ,max(hist.pos_expiration_dt)   'date'                   "   +
         "          ,max(hist.officer_history_surrogate_key) 'key'          "   +
         "from                                                              "   +
         "        Aff_Members		mem                                 "   +
         "        , Officer_History	hist                                "   +
         "        , Aff_Officer_Groups	grp                                 "   +
         //        "--        , Aff_Organizations	aff                                 "   +
         "where                                                              "   +
         "       mem.aff_pk              =       ?                           "   +
         //        "--	mem.aff_pk		=	aff.aff_pk                  "   +
         "And	mem.person_pk		=	hist.person_pk              "   +
         "And	mem.aff_pk		=	hist.aff_pk                 "   +
         "And	mem.aff_pk		=	grp.aff_pk                  "   +
         "And	hist.office_group_id	=	grp.office_group_id         "   +
         "Group by mem.person_pk,    grp.length_of_term, hist.aff_pk         "   ;
        
        private static final String SQL_SELECT_AFFILIATE_OFFICERS =
        "select    DISTINCT                         " +
	" hist.aff_pk                               " +
        ",hist.person_pk                            " +
        ",grp.length_of_term                        " +
        ",hist.pos_expiration_dt   'date'           " +               
        ",hist.officer_history_surrogate_key 'key'  " +
	",hist.pos_end_dt                           " +
        "from                                       " +                              
        "Officer_History	hist                " +                             
        ", Aff_Officer_Groups	grp                 " +                             
        "where                                      " +                                                                           
	"hist.aff_pk              =       ?         " +
        "And	hist.aff_pk   =	grp.aff_pk          " +       
        "And	hist.office_group_id = grp.office_group_id  " +       
        "And    hist.pos_end_dt is null ";
        
                
   /********************************************************************************************/        
         private static final String SQL_SELECT_OFF_AFFILIATE =
        "select                                                             "   +
        "        aff_pk                                                     "   +
        "from                                                               "   +
        "        Aff_Members                                                "   +
        "where                                                              "   +
        "        person_pk              =   ?                               "   ;
         

        private String SQL_SELECT_AFF_PK_FROM_AFF_ORG =
        "select                                                             "   +
        "        aff_pk                                                     "   +
        "from                                                               "   +
        "        Aff_Organizations                                          "   ;

     private static final String SQL_SELECT_UNA_AFFILIATE =
        "select                                                             "   +
        "        aff_sub_locals_allowed_fg                                  "   +
        "from                                                               "   +
        "        Aff_Organizations                                          "   +
        "where                                                              "   +
        "        aff_pk                 =   ?                               "   ;
     private static final String SQL_SELECT_OFF_TITLE =
        "select                                                             "   +
        "        afscme_title_nm                                            "   +
        "from                                                               "   +
        "        AFSCME_Offices                                             "   +
        "        , common_codes		cc                                  "   +        
        "where                                                              "   +
        "        cc.com_cd_cd        =   ?                               "   +
        "and    AFSCME_Offices.afscme_title_desc = cc.com_cd_desc           ";

     private static final String SQL_SELECT_STAFF_AFFILIATE =
        "select                                                             "   +
        "        aff_pk                                                     "   +
        "from                                                               "   +
        "        Aff_Staff                                                  "   +
        "where                                                              "   +
        "        person_pk              =   ?                               "   ;

     private static final String SQL_SELECT_AFSCME_STAFF =
        "select                                                             "   +
        "        dept                                                       "   +
        "from                                                               "   +
        "        Users                                                      "   +
        "where                                                              "   +
        "        person_pk              =   ?                               "   +
        "        And    dept            Is Not Null                         "   ;

     private static final String SQL_SELECT_OFF_POS_DETAILS =
        "select  distinct                                                   "   +
        "        oh.aff_pk                                                  "   +
        "        , oh.person_pk                                             "   +
        "        , oh.pos_steward_fg                                        "   +
        "        , afscme_title_nm                                          "   +
        "        , afscme_title_desc                                        "   +
        "        , ogrp.current_term_end                                    "   +
        "        , com_cd_cd 'month_of_election'                            "   +
        "        , ogrp.max_number_in_office                                "   +
        "        , ogrp.afscme_office_pk                                    "   +
        "        , ogrp.office_group_id                                     "   +
        "        , oh.position_mbr_affiliation                              "   +
        "from                                                               "   +
        "        Officer_History        oh                                  "   +
        "        , Aff_Officer_Groups	ogrp                                "   +
        "        , AFSCME_Offices	ao                                  "   +
        "        , common_codes		cc                                  "   +
        "where                                                              "   +
        "        oh.person_pk                   =   ?                       "   +
        "        And    oh.aff_pk               =   ?                       "   +
        "        And    oh.pos_end_dt           Is Null                     "   +
        //"        And    oh.person_pk            =   per.person_pk           "   +
        "        And    oh.aff_pk               =   ogrp.aff_pk             "   +
        "        and    oh.office_group_id      =   ogrp.office_group_id    "   +
        "        And    oh.afscme_office_pk     =   ogrp.afscme_office_pk   "   +
        "        And    ogrp.afscme_office_pk	=   ao.afscme_office_pk     "   +
        "        and    ogrp.month_of_election	=	cc.com_cd_pk        "   ;
    
    private static final String SQL_SELECT_OFFICER_ON_AFF_MBR_NUM_FNAM_LNAME =
        "select                                                              "   +
        "        per.person_pk                                               "   +
        "        , mem.mbr_no_local                                          "   +
        "        , per.first_nm                                              "   +
        "        , per.last_nm                                               "   +
        "from                                                                "   +
        "          Aff_Members 		mem                                  "   +
        "        , Person               per                                  "   +    
        "where                                                               "   +
        " mem.person_pk           =	per.person_pk                        "   +        
        " and mem.mbr_no_local = ?                                           "   +
        " and per.first_nm = ?                                               "   +
        " and per.last_nm = ?                                                ";     
    
    private static final String SQL_SELECT_AFF_OFFICERS =
        "select                                                              "   +
        "        distinct                                                    "   +
        "         mem.person_pk                                              "   +
        "        , mem.aff_pk                                                "   +
        "        , per.ssn                                                   "   +
        "        , per.first_nm                                              "   +
        "        , per.last_nm                                               "   +
        "        , his.position_mbr_affiliation                              "   +
        "from                                                                "   +
        "        Officer_History        his                                  "   +
        "        , Aff_Officer_Groups	grps                                 "   +
        "        , Aff_Members 		mem                                  "   +
        "        , Person               per                                  "   +
        "where                                                               "   +        
        "               mem.aff_pk              =       ?                    "   +
        "        and    mem.aff_pk              = 	his.aff_pk           "   +        
        "        And    mem.person_pk           =	his.person_pk        "   +
        "        and    mem.person_pk           =	per.person_pk        "   +
        "        And    his.aff_pk		=	grps.aff_pk          "   +
        "        And    his.office_group_id	=	grps.office_group_id "   +
        "        And    his.afscme_office_pk    =	grps.afscme_office_pk"  ;

    
    private static final String SQL_SELECT_PRIMARY_ADDRESS_PK =
        "Select     address_pk                                  "   +      
        "From       Person_SMA                                  "   +
        "Where      person_pk            =       ?               "   ;
    private static final String SQL_UPDATE_END_DATE =
        "update     Officer_History                                  "   +      
        "set        pos_end_dt = getDate()                           "   +
        "Where      person_pk               =       ?                "   +
        "           AND aff_pk              =       ?                "   +
        "           and pos_end_dt          is  null                 "   ;

    private static final String SQL_SELECT_OFFICE_PK =
        "Select     grps.afscme_office_pk                       "   +  
        "           , grps.office_group_id                      "   + 
         "          , afscme_title_nm                           "   +
        "           , afscme_title_desc                         "   +
        "           , grps.length_of_term                       "   +
        "           , grps.current_term_end                     "   +
        "           , com_cd_cd 'month_of_election'             "   +
        "           , grps.max_number_in_office                 "   +
        "From       Aff_Officer_Groups     grps                 "   +
        "           ,AFSCME_offices        offices              "   +
        "           , common_codes		cc                 "   +
        "Where         afscme_title_nm =            ?           "   +
        "       and    current_term_end         =   ?           "   +
//        "--       and    month_of_election        =   ?           "   +
        "       and    aff_pk                   =   ?           "   +
        "       and    grps.month_of_election	= cc.com_cd_pk   "   ;

    private static final String SQL_SELECT_OFFICE_PK_NEW =
        "Select " +
        " grps.aff_pk, " +
        " grps.office_group_id, " +
        " grps.afscme_office_pk, " +
        " offs.afscme_title_desc, " +
        " cc2.com_cd_cd title_code, " +
        " grps.length_of_term, " +
        " grps.current_term_end, " +
        " grps.max_number_in_office, " +
        " cc1.com_cd_cd as election_month " +
        " From " +
        " Aff_Officer_Groups grps " +
        " inner join AFSCME_Offices offs " +
        " on grps.afscme_office_pk = offs.afscme_office_pk " +
        " inner join common_codes cc1 " +
        " on grps.month_of_election = cc1.com_cd_pk " +
        " inner join common_codes cc2 " +
        " on offs.afscme_title_nm = cc2.com_cd_pk " +
        " Where " +
        " cc2.com_cd_cd = ? " +       
        " and grps.current_term_end = ? " +
        " and cc1.com_cd_cd = ? " +
        " and grps.aff_pk = ? ";
    
    private static final String SQL_SELECT_OFFICE_AND_OFFICER_INFO =
        "Select " +
        " grps.aff_pk, " +
        " grps.office_group_id, " +
        " grps.afscme_office_pk, " +
        " offs.afscme_title_desc, " +
        " cc2.com_cd_cd title_code, " +
        " grps.length_of_term, " +
        " grps.current_term_end, " +
        " grps.max_number_in_office, " +
        " cc1.com_cd_cd as election_month, " +
        " hist.person_pk, " +
        " hist.pos_steward_fg, " +
        " hist.pos_end_dt " +
        " From " +
        " Aff_Officer_Groups grps " +
        " inner join AFSCME_Offices offs " +
        " on grps.afscme_office_pk = offs.afscme_office_pk " +
        " inner join common_codes cc1 " +
        " on grps.month_of_election = cc1.com_cd_pk " +
        " inner join common_codes cc2 " +
        " on offs.afscme_title_nm = cc2.com_cd_pk " +
	" left outer join Officer_History hist " +
	" on grps.aff_pk = hist.aff_pk " +       
        " and grps.office_group_id = hist.office_group_id " +       
        " Where " +
        " cc2.com_cd_cd = ? " +       
        " and grps.current_term_end = ? " +
        " and cc1.com_cd_cd = ? " +
        " and grps.aff_pk = ? ";
    
    
    private static final String SQL_SELECT_TERM_END =
        "Select     length_of_term                                      "   +  
        "           , current_term_end                                  "   +
        "           , com_cd_cd 'month_of_election'                     "   +       
        "From       Aff_Officer_Groups     grp                          "   +
        "           ,Officer_History        his                         "   +
        "           , common_codes		cc                      "   +
        "Where          his.person_pk           =       ?               "   +
        "       and    his.aff_pk               =       ?               "   +
        "       and    his.aff_pk               =   grp.aff_pk          "   +
        "       and    his.office_group_id      =   grp.office_group_id "   +
        "       and    his.afscme_office_pk     =   grp.afscme_office_pk"   +
        "       and    his.pos_end_dt          is  null                 "   +
        "       and    grp.month_of_election	=	cc.com_cd_pk    "   ;
    
     private static final String SQL_UPDATE_TERM_END =
        "update     Officer_History                                       "   +      
        "set        pos_expiration_dt       =       ?                     "   +
        "Where      person_pk               =       ?                     "   +
        "           AND aff_pk              =       ?                     "   +
        "           and pos_end_dt          is  null                      "   ;
    private static final String SQL_INSERT_OFFICER_HIS =
        "INSERT INTO Officer_history " +
        "       (aff_pk, person_pk, office_group_id, afscme_office_pk, pos_start_dt," +
        "        position_mbr_affiliation,  " +
        "       suspended_fg, " +
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +       
        "VALUES (?, ?, ?, ?, getDate(), ?, 0, ? , getDate(), ? , getDate())";
    private static final String SQL_INSERT_OFFICER_CARD_RUN =
        "INSERT INTO COM_Officer_Card_Run " +
        "       (officer_history_surrogate_key, aff_pk, person_pk, " +        
        "       created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +       
        "VALUES (?, ?, ?, ?,getDate(), ?,   getDate())";
    
    private static final String SQL_SELECT_OFFICER_CARD_RUN_SURROGATE_KEY =
        "SELECT officer_history_surrogate_key " + 
        "FROM COM_Officer_Card_Run " +
        "WHERE aff_pk = ? ";    
    
    /**********************************************************************************/     
    /* Rebate Select Methods                                                          */
    /**********************************************************************************/
    private static final String SQL_SELECT_RBT_PRE_EDIT_SUMMARY =
        "Select     aff_pk                                      "   +
        "           , queue_pk                                  "   +
        "           , records_sent_cnt                          "   +
        "           , records_received_cnt                      "   +
        "           , mbr_unknown_cnt                           "   +
        "           , council_acc_chg_cnt                       "   +
        "           , local_acc_chg_cnt                         "   +
        "           , no_chg_cnt                                "   +
        "           , aff_error_fg                              "   +
        "           , aff_warning_fg                            "   +
        "From       AUP_Rebate_Pre_Upd_Smry                     "   +
        "Where      queue_pk            =       ?               "   ;
    
    /**********************************************************************************/
    /*  Update SQL for Shared AUP tables                                              */
    /**********************************************************************************/
    private static final String SQL_INSERT_PRE_FIELD_CHANGE_SUMMARY =
        "INSERT INTO AUP_Pre_Fld_Chg_Smry " +
        "       (queue_pk, upd_field_nm, upd_field_chg_cnt) " +
        "VALUES (?, ?, ?)";

    private static final String SQL_INSERT_PRE_ERROR_SUMMARY_EXCEPTIONS =
        "INSERT INTO AUP_Pre_Err_Smry " +
        "       (aff_pk, queue_pk, record_id, person_pk, last_nm, middle_nm, " +
        "       first_nm, suffix_nm, upd_error_type) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_PRE_ERROR_DETAIL_EXCEPTIONS =
        "INSERT INTO AUP_Pre_Err_Dtl " +
        "       (aff_pk, queue_pk, record_id, upd_fld_nm, fld_value_in_system, " +
        "       fld_value_in_file, fld_error_fg) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_REVIEW_SUMMARY =
        "INSERT INTO AUP_Rv_Smry " +
        "       (aff_pk, queue_pk, trans_attempted_cnt, trans_cmpltd_cnt, " +
        "       trans_err_cnt, adds_attempted_cnt, adds_cmpltd_cnt, " +
        "       chg_attempted_cnt, chg_cmpltd_cnt, inactReplace_attempted_cnt, " +
        "       inactReplace_cmpltd_cnt, inactReplace_madeTemp_cnt, " +
        "       vacant_attempted_cnt, vacant_cmpltd_cnt) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_REVIEW_ERROR_SUMMARY_EXCEPTIONS =
        "INSERT INTO AUP_Rv_Err_Smry " +
        "       (aff_pk, queue_pk, record_id, person_pk, last_nm, middle_nm, " +
        "       first_nm, suffix_nm, fld_error_type) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_REVIEW_ERROR_DETAIL_EXCEPTIONS =
        "INSERT INTO AUP_Rv_Err_Dtl " +
        "       (aff_pk, queue_pk, record_id, upd_fld_nm, fld_value_in_system, " +
        "       fld_value_in_file, fld_error_fg) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?) ";


    /**********************************************************************************/
    /* Member Update SQL                                                              */
    /**********************************************************************************/
     private static final String SQL_INSERT_MEMBER_PRE_UPDATE_SUMMARY =
        "INSERT INTO AUP_Member_Pre_Upd_Smry" +
        "       (queue_pk, aff_pk, mbr_system_cnt, mbr_file_cnt, mbr_added_cnt, " +
        "       mbr_inactivated_cnt, mbr_changed_cnt, t_mbrs_created_cnt, mbr_matched_cnt, " +
        "       mbr_nonmatched_cnt, aff_warning_fg, aff_error_fg, " +
        "       aff_err_aff_type, aff_err_aff_localSubChapter, " +
        "       aff_err_aff_subUnit, aff_err_aff_councilRetiree_chap) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_AFFILIATE_STATUS =
        "SELECT aff_status FROM Aff_Organizations WHERE aff_pk = ?";

    private static final String SQL_SELECT_AFFILIATE_SUBLOCALS_ALLOWED =
        "SELECT CASE WHEN aff_sub_locals_allowed_fg = 1 THEN 1 ELSE 0 END 'sub_locals_allowed' " +
        "FROM   Aff_Organizations " +
        "WHERE aff_pk = ? ";

    private static final String SQL_UPDATE_REMAINING_MEMBER =
        "UPDATE Aff_Members " +
        "SET    no_cards_fg = ?, no_mail_fg = ?, no_legislative_mail_fg = ?,  " +
        "       no_public_emp_fg = ?, mbr_no_local = ?, primary_information_source = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  aff_pk = ? AND person_pk = ? ";

    private static final String SQL_UPDATE_MEMBER_PERSON =
        "UPDATE Person " +
        "SET prefix_nm=?, first_nm=?, last_nm=?, middle_nm=?, suffix_nm=?, " +
        "       lst_mod_dt=?, ssn=?, lst_mod_user_pk=? " +
        "WHERE person_pk=?";


    /**********************************************************************************/
    /* Officer Update SQL                                                             */
    /**********************************************************************************/
     private static final String SQL_INSERT_OFFICER_PRE_UPDATE_SUMMARY =
        "INSERT INTO AUP_Officer_Pre_Upd_Smry" +
        "       (queue_pk, aff_pk, off_system_cnt, off_file_cnt, off_add_cnt, off_vacant_cnt, off_chg_cnt, off_replaced_cnt, off_cards_cnt, aff_warning_fg, aff_error_fg, " +
        "       aff_err_aff_type, aff_err_aff_localSubChapter, aff_err_aff_subUnit, aff_err_aff_councilRetiree_chap, aff_err_aff_stateNat_type)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
        "        ?, ?, ?, ?, ?)";

     private static final String SQL_INSERT_OFFICER_PRE_OFFICE_DETAIL =
        "INSERT INTO AUP_Officer_Pre_Off_Dtl" +
        "       (queue_pk, aff_pk, office_group_id, afscme_office_pk, officers_in_file_cnt, officers_allowed_num) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

     private static final String SQL_INSERT_OFF_FIELD_CHANGE_SUMMARY =
        "INSERT INTO AUP_Pre_Fld_Chg_Smry " +
        "       (queue_pk, upd_field_nm, upd_field_chg_cnt) " +
        "VALUES (?, ?, ?)";


    /**********************************************************************************/
    /* Rebate Update SQL                                                              */
    /**********************************************************************************/
     private static final String SQL_INSERT_REBATE_PRE_UPDATE_SUMMARY =
        "INSERT INTO AUP_Rebate_Pre_Upd_Smry" +
        "       (queue_pk, aff_pk, records_sent_cnt, records_received_cnt, mbr_unknown_cnt, " +
        "       council_acc_chg_cnt, local_acc_chg_cnt, no_chg_cnt, " +
        "       aff_warning_fg, aff_error_fg, " +
        "       aff_err_aff_type, aff_err_aff_localSubChapter, " +
        "       aff_err_aff_subUnit, aff_err_aff_councilRetiree_chap) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_REBATE_MATCH_PRB_ROSTER_PERSONS =
        "UPDATE PRB_Roster_Persons " +
        "SET    rebate_year_mbr_type = ?, rebate_year_mbr_status = ?, " +
        "       rebate_year_mbr_dues_rate = ?, roster_duration_in_aff = ?, " +
        "       roster_acceptance_cd = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  aff_pk = ? AND person_pk = ? AND rbt_year = ? ";


    /**********************************************************************************/
    /* Participation Update SQL                                                       */
    /**********************************************************************************/
    /** SELECTS a Participation Detail by its Group, Type and Detail Names */
    private static final String SQL_SELECT_PARTICIPATION_DETAIL_BY_NAMES =
        "SELECT pcg.particip_group_pk, pcg.particip_group_nm, " +
        "       pct.particip_type_pk, pct.particip_type_nm, " +
        "       particip_detail_pk, particip_detail_nm, " +
        "       particip_detail_status, particip_detail_shortcut " +
        "FROM   Participation_Cd_Dtl pcd " +
        "JOIN   Participation_Cd_Type pct   ON pct.particip_type_pk = pcd.particip_type_pk " +
        "JOIN   Participation_Cd_Group pcg  ON pcg.particip_group_pk = pct.particip_group_pk " +
        "WHERE  pcg.particip_group_nm = ? " +
        "AND    pct.particip_type_nm = ? " +
        "AND    particip_detail_nm = ? ";

    /** SELECTS all of the Participation Outcomes for a Detail */
    private static final String SQL_SELECT_PARTICIPATION_OUTCOMES_BY_DETAIL =
        "SELECT pco.particip_outcome_pk, particip_outcome_nm " +
        "FROM   Participation_Cd_Outcomes   pco " +
        "JOIN   Participation_Dtl_Outcomes  pdo  ON pdo.particip_outcome_pk = pco.particip_outcome_pk " +
        "JOIN   Participation_Cd_Dtl        pcd  ON pcd.particip_detail_pk = pdo.particip_detail_pk " +
        "WHERE  pdo.particip_detail_pk = ? ";

    /** SELECTS to match the personPk for the member */
    private static final String SQL_MEMBER_MATCH_PARTICIPATION =
        "SELECT distinct p.person_pk, first_nm, last_nm, ssn " +
        "FROM            Person p " +
        "INNER JOIN      Aff_Members a  ON  p.person_pk = a.person_pk ";

    /** ANDs where clause for person_pk match */
    private static final String SQL_WHERE_MEMBER_MATCH_PARTICIPATION_MEMBER_NUMBER =
        "WHERE           p.person_pk = ? ";

    /** ANDs where clause for ssn match */
    private static final String SQL_WHERE_MEMBER_MATCH_PARTICIPATION_SSN =
        "WHERE           ssn = ? ";

    /** SELECTS the count of Member Participation records that match to check existence */
    private static final String SQL_SELECT_EXIST_MATCH_PARTICIPATION_OUTCOME =
        "SELECT COUNT(*) " +
        "FROM   Member_Participation " +
        "WHERE  particip_detail_pk = ? " +
        "AND    person_pk = ? ";

    /** INSERTS a new Member Participation (Outcome) */
    private static final String SQL_INSERT_MEMBER_PARTICIPATION_OUTCOME =
        "INSERT INTO Member_Participation " +
        "   (particip_detail_pk, person_pk, particip_outcome_pk, " +
        "    mbr_particip_dt, comment_txt, " +
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, ?, ?, ?, GETDATE(), ?, GETDATE() )";

    /** UPDATES a Member Participation (Outcome) */
    private static final String SQL_UPDATE_MEMBER_PARTICIPATION_OUTCOME =
        "UPDATE Member_Participation " +
        "SET    particip_outcome_pk = ?, mbr_particip_dt = ?, comment_txt = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  particip_detail_pk = ? " +
        "AND    person_pk = ? ";
    
    /** Gets E-mail information for Apply Update */
    private static final String SQL_SELECT_UPDATE_EMAIL_INFO =
        "SELECT pe.person_email_addr, p.first_nm, p.last_nm " +
        "FROM   Person_Email pe " +
        "JOIN   Person p ON p.person_pk = pe.person_pk " +
        "JOIN   COM_Application_Notification can ON can.email_pk = pe.email_pk " +
        "JOIN   COM_Application_Functions caf ON caf.application_function_pk = can.application_function_pk " +
        "WHERE  caf.application_function_nm = 'Apply Update' "; 

     
    /******************************************************************************************/
        private int recordId     =   0;
        private int rvRecId      =   0;

    //******************************************************************************************
    public void ejbCreate() {

        try {
            fqBean = JNDIUtil.getFileQueueHome().create();
            affilBean = JNDIUtil.getMaintainAffiliatesHome().create();
            codesBean = JNDIUtil.getMaintainCodesHome().create();
            membersBean = JNDIUtil.getMaintainMembersHome().create();
            personsBean = JNDIUtil.getMaintainPersonsHome().create();
            smaBean = JNDIUtil.getSystemAddressHome().create();
            officersBean = JNDIUtil.getMaintainAffiliateOfficersHome().create();
            loadConfigValues();
        }
        catch (NamingException ne) {
            throw new EJBException(ne);
        }
        catch (CreateException ce) {
            throw new EJBException(ce);
        }
    }

    public void ejbRemove()  {
        try {
            fqBean.remove();
            affilBean.remove();
            codesBean.remove();
            membersBean.remove();
            personsBean.remove();
            smaBean.remove();
            officersBean.remove();
        } catch (RemoveException re) {
            throw new EJBException(re);
        }
    }

    /** Reads the Config Value from the database and stores it locally */
    protected void loadConfigValues() {

        //retrieve the last update configurable time limit (in days) value
        lastUpdateConfigTimeLimit = getAppConfigData(CONFIG_VARIABLE_NAME);
    }


    /************************************************************************************************/
    /* **** Note: METHODS *** BELOW *** HERE ARE ALL FOR THE PROCESSING OF THE APPLY UPDATE!!       */
    /************************************************************************************************/

    /**
     * Performs the updates for the specified file
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPks The affiliates the user has selected for update
     * @param queuePk The file queue to import
     * @param userPk The primary key of the user initiating the action
     */
    public void applyUpdate(List affPks, Integer queuePk, Integer userPk) {
        logger.debug("----------------------------------------------------");
        logger.debug("applyUpdate called.");

        // retrieve FileEntry for the reqeusted file queue from the database
        FileEntry fileEntry = fqBean.getFileEntry(queuePk);
        FileProcessor fp = null;
        StringBuffer bodyMessage;
        String affMessage;

        switch (fileEntry.getFileType()) {
            case  UpdateFileType.MEMBER:
                logger.debug("Updating with a Member file.");
                fp = new MemberFileProcessor(fqBean, affilBean, codesBean);

                fp.setFileEntry(fileEntry);
                if (fileEntry.getFileQueue() == UpdateFileQueue.BAD) // bad file from pre-processing
                    return;     // we do nothing

                fqBean.markFilePending(queuePk, userPk);

                fp.applyUpdate(affPks, queuePk, userPk);

                // mark the update_file_status to be "Processed" (in place of "Review")
                fqBean.markFileProcessed(queuePk, userPk);
                
                // send e-mail
                bodyMessage = new StringBuffer("Apply Update of Member File successfully updated the following Affiliates : ");
                affMessage = generateAffiliateList(affPks);
                bodyMessage.append(affMessage);
                sendApplyUpdateMail(bodyMessage.toString());
                
                break;
            case UpdateFileType.REBATE:
                logger.debug("Updating with a Rebate file.");
                fp = new RebateFileProcessor(affilBean, codesBean);

                fp.setFileEntry(fileEntry);
                if (fileEntry.getFileQueue() == UpdateFileQueue.BAD) // bad file from pre-processing
                    return;     // we do nothing

                fqBean.markFilePending(queuePk, userPk);

                fp.applyUpdate(affPks, queuePk, userPk);

                // mark the update_file_status to be "Processed" (in place of "Review")
                fqBean.markFileProcessed(queuePk, userPk);

                // send e-mail
                bodyMessage = new StringBuffer("Apply Update of Rebate File successfully updated the following Affiliates : ");
                affMessage = generateAffiliateList(affPks);
                bodyMessage.append(affMessage);
                sendApplyUpdateMail(bodyMessage.toString());
                
                break;
            case UpdateFileType.OFFICER:
                fp = new OfficerFileProcessor(fqBean, affilBean, codesBean);
                fp.setFileEntry(fileEntry);
                if (fileEntry.getFileQueue() == UpdateFileQueue.BAD) // bad file from pre-processing
                    return;     // we do nothing

                fqBean.markFilePending(queuePk, userPk);

                fp.applyUpdate(affPks, queuePk, userPk);

                // mark the update_file_status to be "Processed" (in place of "Review")
                fqBean.markFileProcessed(queuePk, userPk);
                
                // send e-mail
                bodyMessage = new StringBuffer("Apply Update of Officer File successfully updated the following Affiliates : ");
                affMessage = generateAffiliateList(affPks);
                bodyMessage.append(affMessage);
                sendApplyUpdateMail(bodyMessage.toString());
                
                break;
            case UpdateFileType.PARTICIPATION:
                logger.debug("Updating with a Participation file.");
                fp = new ParticipationFileProcessor();

                fp.setFileEntry(fileEntry);
                if (fileEntry.getFileQueue() == UpdateFileQueue.BAD) // bad file from pre-processing
                    return;     // we do nothing

                fqBean.markFilePending(queuePk, userPk);

                fp.applyUpdate(affPks, queuePk, userPk);

                // mark the update_file_status to be "Processed" (in place of "Review")
                fqBean.markFileProcessed(queuePk, userPk);
                
                // send e-mail
                bodyMessage = new StringBuffer("Apply Update of Participation File successfully updated. ");
                sendApplyUpdateMail(bodyMessage.toString());
                
                break;                
        }
    }

    
   /**
     * Assembles a list of affiliates that were processed
     *
     */    
    private String generateAffiliateList(List affPks) {    
        Iterator affIt = affPks.iterator();
        StringBuffer affMessage = new StringBuffer(" ");
        boolean firstTime = true;
        while (affIt.hasNext()) {
            
            if (firstTime) {
                firstTime = false;
            } else {
                affMessage.append(", ");
            }
            
            Integer anAffPk = (Integer)affIt.next();
            AffiliateData ad = affilBean.getAffiliateData(anAffPk);
            AffiliateIdentifier ai = ad.getAffiliateId();
            affMessage.append(ai.getType().toString());
            
            if (new Character('L').equals(ai.getType()) || new Character('S').equals(ai.getType()) ) {
                affMessage.append(ai.getLocal());
            } else {
                if (new Character('C').equals(ai.getType()) || new Character('R').equals(ai.getType()) ) {
                    affMessage.append(ai.getCouncil());
                } else {
                    affMessage.append(ai.getSubUnit());   
                }
            }
            
            affMessage.append(ai.getState());
        } 
        return affMessage.toString();
    }
    
    
   /**
     * Sends mail out for the apply update.
     */    
    private void sendApplyUpdateMail(String affMessage) {    
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_UPDATE_EMAIL_INFO); 
            rs = ps.executeQuery();
            while (rs.next()) {
                String addy = rs.getString(1);
                String fullName = rs.getString(2) + " " + rs.getString(3);               
                MailUtil.sendMail(addy, fullName, "user@AFSCME.org", "AFSCME IT", "Apply Update Successful" ,affMessage);
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        catch (java.io.IOException ioe) {
            throw new EJBException(ioe);
        }
        catch (javax.mail.MessagingException me) {
            throw new EJBException(me);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }
    
    
    /**
     * Creates the pre-update summary for the specified file
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk The file queue to generaet the pre-update summary for
     * @param userPk The primary key of the user initiating the action
     * @param fileType The file type code (M, O, R, P)
     */
    public void generatePreUpdateSummary(Integer queuePk, Integer userPk, int fileType) {
        logger.debug("----------------------------------------------------");
        logger.debug("Generate Pre Update Summary method entered.");
        
        //Define variables 
        PreUpdateSummary        result                      =   null;
        FileProcessor           fp                          =   null;
        Map.Entry               parentEntry                 =   null;
        ArrayList               list                        =   null;
        Iterator                lt                          =   null;
        HashMap                 map                         =   null;
        ExceptionData           eData                       =   null;
        Map                     exceptions                  =   null;
        Map                     officerCount                =   null;
        Map                     memberCount                 =   null;
        java.sql.Timestamp      endTime                     =   null;                
        String                  startSecs                   =   null;
        String                  endSecs                     =   null;
        double                  elapsedTime                 =   0.0;
        MemberChanges           totalCount                  =   null;
        MemberChanges           memChanges                  =   null;
        MemberPreUpdateSummary  memberPreUpdateSummary      =   null;
        OfficerChanges          offChanges                  =   null;
        OfficerPreUpdateSummary officerPreUpdateSummary     =   null;
        OfficerChanges          offTCount                   =   null;
        RebatePreUpdateSummary  rebatePreUpdateSummary      =   null;
        Map                     rebateCount                 =   null;
        RebateChanges           rTotalCount                 =   null;
        RebateChanges           rebateChanges               =   null;
        //********************************************************************************************
        //Timer to get the elapsed time for purposes of logging
        java.sql.Timestamp startTime = new java.sql.Timestamp(new java.util.Date().getTime());
        //**********************************************************************************************
        switch (fileType) {
            case UpdateFileType.MEMBER:
                fp = new MemberFileProcessor(fqBean, affilBean, codesBean);
                logger.debug("Processing a Member file.");
                break;
            case UpdateFileType.OFFICER:
                fp = new OfficerFileProcessor(fqBean, affilBean, codesBean);
                logger.debug("Processing an Officer file.");
                break;
            case UpdateFileType.PARTICIPATION:
                fp = new ParticipationFileProcessor();
                logger.debug("Processing a Participation file.");
                break;
            case UpdateFileType.REBATE:
                fp = new RebateFileProcessor(affilBean, codesBean);
                logger.debug("Processing a Rebate file.");
                break;
            default:
                throw new RuntimeException("File Type was invalid. Could not generate the preUpdateSummary.");
        }
        // retrieve the file information from db
        FileEntry fileEntry = fqBean.getFileEntry(queuePk);
        fp.setFileEntry(fileEntry);
        
        int valid = fp.validate();
        if (valid != 0) {   // a bad file
            fqBean.markFileBad(queuePk, userPk);

            // mark the update_file_status to be "Invalid"
            String comments = null;
            switch (valid) {
                case UpdateErrorCodes.ERROR_FILE_IO:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_IO;
                    break;
                case UpdateErrorCodes.ERROR_FILE_LENGTH_LESS_THAN_CONFIG_VALUE:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_LENGTH_LESS_THAN_CONFIG_VALUE;
                    break;
                case UpdateErrorCodes.ERROR_FILE_LENGTH_INVALID_MEMBER:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_LENGTH_INVALID_MEMBER;
                    break;
                case UpdateErrorCodes.ERROR_FILE_LENGTH_INVALID_REBATE:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_LENGTH_INVALID_REBATE;
                    break;
                case UpdateErrorCodes.ERROR_FILE_MISSING_AFFILIATE_NUMBER:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_MISSING_AFFILIATE_NUMBER;
                    break;
                case UpdateErrorCodes.ERROR_FILE_MISSING_AFFILIATE_MEMBER_NUMBER:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_MISSING_AFFILIATE_MEMBER_NUMBER;
                    break;
                case UpdateErrorCodes.ERROR_FILE_ZIP_ZERO_OR_BLANK:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_ZIP_ZERO_OR_BLANK;
                    break;
                case UpdateErrorCodes.ERROR_FILE_MISSING_AFSCME_MEMBER_NUMBER:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_MISSING_AFSCME_MEMBER_NUMBER;
                    break;
                case UpdateErrorCodes.ERROR_FILE_INVALID_ACCEPTANCE_CODE:
                    comments = UpdateErrorCodes.COMMENT_ERROR_FILE_INVALID_ACCEPTANCE_CODE;
                    break;
            }
            fqBean.markFileInvalid(queuePk, comments, userPk);
            logger.info("File invalid and marked bad with queuePk = " + queuePk);
            return;
        }
        
        // this method will return the PreUpdateSummary object
        result = fp.generatePreUpdateSummary();
        // mark the file queue "GOOD"
        fqBean.markFileGood(queuePk, userPk);
        
        // insert preUpdateSummary into the database.
        switch (fileType) {
            case UpdateFileType.MEMBER:
                logger.debug("Storing the Member PreUpdateSummary.");
                storePreUpdateSummary(queuePk, (MemberPreUpdateSummary)result);
                // mark the update_file_status to be "Review" (in place of "Uploaded")
                fqBean.markFileForReview(queuePk, userPk);
                //***********************************************************************************
                //start logging if the file has affiliate error
                //***********************************************************************************
                logger.debug("logging if the summary has an error");                
                memChanges              =   new MemberChanges();
                memberPreUpdateSummary  =   getMemberPreUpdateSummary(queuePk);
                memberCount             =   memberPreUpdateSummary.getMemberCounts();
                exceptions              =   memberPreUpdateSummary.getExceptions();
                totalCount              =   memberPreUpdateSummary.getTotalCounts();
                logger.debug("memberCount=>" + memberCount);
                //******************************************************************************************
                //After getting the preupdatesummary now lloop through the map to find if there was an error
                //and if there was then try to log the error 
                if((memberCount != null) && !(memberCount.isEmpty())){
                    Iterator it     = memberCount.entrySet().iterator();
                    parentEntry     = (Map.Entry)it.next();
                    logger.debug("parentEntry.getValue()===>" + parentEntry.getValue());
                    memChanges      = (MemberChanges) parentEntry.getValue();   
                    logger.debug("memChanges.getHasError()=>" + memChanges.getHasError());
                    if(memChanges.getHasError()){
                        SystemLog.logAffiliateFileError(fileEntry.getAffPk().toString(), memChanges.getAffPk().toString(), fileEntry.getFileName(), fileEntry.getReceivedDate(), userPk.toString());                        
                    }
                    
                }
                 //*************************************************************************************************
                //this will log that upload member data has been processed
                endTime      = new java.sql.Timestamp(new java.util.Date().getTime());                
                startSecs    = startTime.toString().substring(startTime.toString().indexOf(':' , 16)).substring(1);
                endSecs      = endTime.toString().substring(endTime.toString().indexOf(':' , 16)).substring(1);        
                elapsedTime  = new Double((new Double(endSecs)).doubleValue() - (new Double(startSecs)).doubleValue()).doubleValue();        

                if(fileEntry != null){  
                    logger.debug("got inside of to log");
                    //MemberPreUpdateSummary memberPreUpdateSummary = getMemberPreUpdateSummary(queuePk);
                    //MemberChanges totalCount                      = memberPreUpdateSummary.getTotalCounts();                  
                    SystemLog.logAffiliateDataProcessed(fileEntry.getAffPk().toString(), fileEntry.getReceivedDate(), totalCount.getInFile(), elapsedTime,userPk.toString());
                }
                //*************************************************************************************************
                break;
            case UpdateFileType.OFFICER:                
                logger.debug("File Type is Officer Upload.");
                storePreUpdateSummary(queuePk, (OfficerPreUpdateSummary)result);
                storePositionChanges(queuePk, ((OfficerPreUpdateSummary)result).getPositionChanges());
                //******************************************************************************************
                //data are obtained from DB; the below code is for logging purposes
                offChanges                  =   new OfficerChanges();
                officerPreUpdateSummary     =   getOfficerPreUpdateSummary(queuePk);
                officerCount                =   officerPreUpdateSummary.getOfficerCounts();
                exceptions                  =   officerPreUpdateSummary.getExceptions();
                offTCount                   =   officerPreUpdateSummary.getTotalCounts();
                //************************************************************************************************
                
                //After getting the preupdatesummary now lloop through the map to find if there was an error
                //and if there was then try to log the error 
                if((officerCount != null) && !(officerCount.isEmpty())){
                    Iterator it     = officerCount.entrySet().iterator();
                    parentEntry     = (Map.Entry)it.next();
                    logger.debug("parentEntry.getValue()===>" + parentEntry.getValue());
                    offChanges      = (OfficerChanges) parentEntry.getValue();   
                    logger.debug("offChanges.getHasError()=>" + offChanges.getHasError());
                    if(offChanges.getHasError()){
                        SystemLog.logAffiliateFileError(fileEntry.getAffPk().toString(), offChanges.getAffPk().toString(), fileEntry.getFileName(), fileEntry.getReceivedDate(), userPk.toString());                        
                    }
                    
                }
                 //*************************************************************************************************
                //this will log that upload member data has been processed
                endTime      = new java.sql.Timestamp(new java.util.Date().getTime());                
                startSecs    = startTime.toString().substring(startTime.toString().indexOf(':' , 16)).substring(1);
                endSecs      = endTime.toString().substring(endTime.toString().indexOf(':' , 16)).substring(1);        
                elapsedTime  = new Double((new Double(endSecs)).doubleValue() - (new Double(startSecs)).doubleValue()).doubleValue();        
                if(fileEntry != null){  
                    logger.debug("got inside of to log");
                    if (offTCount != null)
                    SystemLog.logAffiliateDataProcessed(fileEntry.getAffPk().toString(), fileEntry.getReceivedDate(), offTCount.getInFile(), elapsedTime,userPk.toString());
                    else
                    SystemLog.logAffiliateDataProcessed(fileEntry.getAffPk().toString(), fileEntry.getReceivedDate(), 0, elapsedTime,userPk.toString());                        
                }
                //*****************************************************************************************************************
                break;
            case UpdateFileType.PARTICIPATION:
                logger.debug("Storing the Participation PreUpdateSummary.");
                break;
            case UpdateFileType.REBATE:
                logger.debug("Storing the Rebate PreUpdateSummary.");
                storePreUpdateSummary(queuePk, (RebatePreUpdateSummary)result);
                //***********************************************************************************
                //start logging if the file has affiliate error
                //***********************************************************************************
                logger.debug("logging if the summary has an error");                
                rebateChanges           =   new RebateChanges();
                rebatePreUpdateSummary  =   getRebatePreUpdateSummary(queuePk);
                rebateCount             =   rebatePreUpdateSummary.getRebateCounts();
                exceptions              =   rebatePreUpdateSummary.getExceptions();
                rTotalCount             =   rebatePreUpdateSummary.getTotalCounts();
                logger.debug("memberCount=>" + rebateChanges);
                //******************************************************************************************
                //After getting the preupdatesummary now lloop through the map to find if there was an error
                //and if there was then try to log the error 
                if((rebateCount != null) && !(rebateCount.isEmpty())){
                    Iterator it     = rebateCount.entrySet().iterator();
                    parentEntry     = (Map.Entry)it.next();
                    logger.debug("parentEntry.getValue()===>" + parentEntry.getValue());
                    rebateChanges   = (RebateChanges) parentEntry.getValue();   
                    logger.debug("memChanges.getHasError()=>" + rebateChanges.getHasError());
                    if(rebateChanges.getHasError()){
                        SystemLog.logAffiliateFileError(fileEntry.getAffPk().toString(), rebateChanges.getAffPk().toString(), fileEntry.getFileName(), fileEntry.getReceivedDate(), userPk.toString());                        
                    }
                    
                }
                 //*************************************************************************************************
                //this will log that upload member data has been processed
                endTime      = new java.sql.Timestamp(new java.util.Date().getTime());                
                startSecs    = startTime.toString().substring(startTime.toString().indexOf(':' , 16)).substring(1);
                endSecs      = endTime.toString().substring(endTime.toString().indexOf(':' , 16)).substring(1);        
                elapsedTime  = new Double((new Double(endSecs)).doubleValue() - (new Double(startSecs)).doubleValue()).doubleValue();        

                if(fileEntry != null){  
                    logger.debug("got inside of to log");
                    //MemberPreUpdateSummary memberPreUpdateSummary = getMemberPreUpdateSummary(queuePk);
                    //MemberChanges totalCount                      = memberPreUpdateSummary.getTotalCounts();                  
                    SystemLog.logAffiliateDataProcessed(fileEntry.getAffPk().toString(), fileEntry.getReceivedDate(), rTotalCount.getAfscmeMemberNumCountInFile(), elapsedTime,userPk.toString());
                }
                //*************************************************************************************************
                break;
            default:
                throw new RuntimeException("File Type was invalid. Could not store the preUpdateSummary.");
        }

        // send email to Affiliate Member POC and the user who requested the update
        // stating the update is complete and the report is available to review online

        // mark the update_file_status to be "Review" (in place of "Uploaded")
        fqBean.markFileForReview(queuePk, userPk);
        
        //*************************************************************************************************
        //this will log that upload member data has been processed
        /*java.sql.Timestamp endTime      = new java.sql.Timestamp(new java.util.Date().getTime());                
        String startSecs                = startTime.toString().substring(startTime.toString().indexOf(':' , 16)).substring(1);
        String endSecs                  = endTime.toString().substring(endTime.toString().indexOf(':' , 16)).substring(1);        
        double elapsedTime              = new Double((new Double(endSecs)).doubleValue() - (new Double(startSecs)).doubleValue()).doubleValue();        
                
        if(fileEntry != null){  
            logger.debug("got inside of to log");
            MemberPreUpdateSummary memberPreUpdateSummary = getMemberPreUpdateSummary(queuePk);
            MemberChanges totalCount                      = memberPreUpdateSummary.getTotalCounts();                  
            SystemLog.logAffiliateDataProcessed(fileEntry.getAffPk().toString(), fileEntry.getReceivedDate(), totalCount.getInFile(), elapsedTime,userPk.toString());
        }
        */
        
        logger.debug("Generate Pre Update Summary method existed.");
        
    }

    /**
     * Gets a review summary
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the review summary was generated for
     */
    public void getReviewSummary(Integer queuePk) {

    }

    /**
     * Stores an UpdateSummary
     *
     */
    public void storeUpdateSummary(Integer queuePk, UpdateSummary summary) {
        logger.debug("----------------------------------------------------");
        logger.debug("storeUpdateSummary called.");

        if (summary instanceof MemberUpdateSummary) {
            storeMemberUpdateSummary(queuePk, (MemberUpdateSummary)summary);
        } else if (summary instanceof RebateUpdateSummary) {
            storeRebateUpdateSummary(queuePk, (RebateUpdateSummary)summary);
        }/*else if (summary instanceof OfficerReviewSummary) {
            storeOfficerUpdateSummary(queuePk, (OfficerReviewSummary)summary);
        }*/
    }

    
/*****************************************************************************************************************/    

    /************************************************************************************************/
    /* **** Note: METHODS *** BELOW *** HERE ARE ALL FOR THE    MEMBER    UPDATE PROCESSING!!       */
    /************************************************************************************************/

    /**
     * Gets the counts for member only appears in system (inSystem, newTRecords, inactivated)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * Checks using the following criteria for affiliate warnings:
     *  1. checks if member should be made ‘Temporary’ Status (is used by the system when
     *     the Member update does not contain the Member and that Member is assigned to an
     *     Officer position.  The system gives the Member the ‘Temporary’ status instead of
     *     ‘Inactive’ status and includes a count of it on the report. Once the Officer record
     *     is replaced, the Member record’s status is changed to ‘Inactive’.
     * 2. checks if however the Officer has a term of ‘Indefinite’, then the Member record is
     *    set to ‘Inactive’, and the Officer position is vacated.
     *
     *
     * @param affPks The set of affiliate Pks for all affiliates in the system below the hierarchy
     * @param preUpdateSummary  The pre-update summary object to be filled.
     */
    public void calculateMemberInSystemCounts(Set affPks, MemberPreUpdateSummary updateSummary) {
        logger.debug("----------------------------------------------------");
        logger.debug("calculateMemberInSystemCounts called.");

        // retrieve all members (personPK) in system first.
        Connection con = DBUtil.getConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1;
        ResultSet rs2;

        /** @TODO: Replace this code with a call to either the member bean, or the affiliate bean. */
        try {
            ps1 = con.prepareStatement(SQL_SELECT_ALL_MEMBERS_FOR_AFFILIATE);
            ps2 = con.prepareStatement(SQL_SELECT_ALL_OFFICERS_FOR_AFFILIATE);

            Integer affPk = null;
            AffiliateData data = null;
            AffiliateIdentifier affId = null;
            for (Iterator it1 = affPks.iterator(); it1.hasNext(); ) {
                // get Affiliate info
                affPk = (Integer)it1.next();
                logger.debug("    affPk = " + affPk);
                data = affilBean.getAffiliateData(affPk);
                if (data == null) {
                    throw new EJBException("No affiliate found.");
                }
                affId = data.getAffiliateId();
                // clear the code and admin council, since affiliate ids were created with null values for those fields.
                //affId.setCode(null);
                affId.setAdministrativeLegislativeCouncil(null);

                ps1.setInt(1, affPk.intValue());
                rs1 = ps1.executeQuery();

                Set membersInSystem = new HashSet();
                while (rs1.next()) {
                    membersInSystem.add(new Integer(rs1.getInt(1)));
                }
                DBUtil.cleanup(null, null, rs1);

                // update the "inSystem" count
                MemberChanges memberChanges = (MemberChanges)updateSummary.getMemberCounts().get(affId);
                logger.debug("    memberChanges = " + memberChanges);
                memberChanges.setInSystem(membersInSystem.size());

                // get those members in system, but not in file.
                logger.debug("    membersInSystem:  " + membersInSystem);
                logger.debug("    matchedMemberPks: " + memberChanges.getMatchedMemberPks());
                membersInSystem.removeAll(memberChanges.getMatchedMemberPks());

                Set nonMatchedMembers = membersInSystem;

                // check these members if they are officers
                for (Iterator it2 = nonMatchedMembers.iterator(); it2.hasNext(); ) {
                    Integer personPk = (Integer)it2.next();
                    logger.debug("checking if person with pk = " + personPk + " is an officer.");

                    ps2.setInt(1, affPk.intValue());
                    ps2.setInt(2, personPk.intValue());
                    rs2 = ps2.executeQuery();

                    if (rs2.next()) {
                        do {
                            Timestamp expirationDate = rs2.getTimestamp(1);
                            Integer termLengthCodePk = new Integer(rs2.getInt(2));
                            if (expirationDate == null) { // this inSystem member is currently an officer
                                memberChanges.incrementOfficerCount();
                                // get the termLength Code
                                if (TextUtil.equals(termLengthCodePk, TermLength.INDEFINITE)) { // The officer has an Indefinite term
                                    memberChanges.incrementInactivated();
                                } else {
                                    memberChanges.incrementNewTRecords();
                                }
                                break;
                            }
                        } while (rs2.next());
                    } else {    // this inSystem member is NOT an officer
                        memberChanges.incrementInactivated();
                    }
                    DBUtil.cleanup(null, null, rs2);
                }
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);
        }
    }

    /**
     * Check if affiliate can have sublocals
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk The primary key of the affiliate
     * @return  true if local can have sublocals, false if not allowed
     */
    public boolean checkSubLocalsAllowed(Integer affPk) {

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean allowed = false;

        try {
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_SUBLOCALS_ALLOWED);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
                allowed = rs.getBoolean("sub_locals_allowed");
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return allowed;
    }

    /**
     * Get the first state if same affiliate info but no state
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affId The affiliate identifier
     * @return  String first state from list
     */
    public String determineStateForSimilarLocals(AffiliateIdentifier affId) {

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        ResultSet rs = null;
        String state = null;

        try {
            builder.addCriterion("aff_type", affId.getType());
            builder.addCriterion("aff_localSubChapter", affId.getLocal());
            builder.addCriterion("aff_stateNat_type", affId.getState());
            builder.addCriterion("aff_subUnit", affId.getSubUnit());
            builder.addCriterion("aff_councilRetiree_chap", affId.getCouncil());
            builder.addCriterion("aff_code", affId.getCode());

            builder.addOrderBy("aff_stateNat_type");
            ps = builder.getPreparedStatement(SQL_SELECT_FIRST_STATE_FOR_SIMILAR_LOCALS, con);
            rs = ps.executeQuery();
            if (rs.next())
                state = rs.getString("aff_stateNat_type");
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return state;
    }

    /**
     * Performs the member updates into the database for the Update process
     *
     * @ejb:interface-method view-typ="lcoal"
     * @ejb:transaction type="Required"
     */
    public void doMemberUpdate(Integer userPk, Integer queuePk, MemberUpdateSummary updateSummary, MemberUpdateTabulation tab) {
        logger.debug("----------------------------------------------------");
        logger.debug("doMemberUpdate called.");

        Set updateErrors = new HashSet();
        Set addErrors = new HashSet();
        // do member updates
        MemberUpdateElement member;
        boolean updateDone = false;
        for (Iterator it = tab.getUpdates().values().iterator(); it.hasNext(); ) {
            member = (MemberUpdateElement)it.next();
            logger.debug("member: " + member);
            try {
                updateDone = updateMember(userPk, member, updateErrors);
                if (updateDone) {
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(member.getAffPk())).incrementTransCompleted();
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(member.getAffPk())).incrementChangesCompleted();
                }
            } catch (EJBException e) {
                logger.debug("", e);
                /** @TODO: do something with the updateErrors. */
            }
            updateDone = false;
        }

        // do member additions
        boolean addDone = false;
        for (Iterator it = tab.getAdditions().iterator(); it.hasNext(); ) {
            member = (MemberUpdateElement)it.next();
            try {
                addDone = addMember(userPk, member, addErrors);
                if (addDone) {
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(member.getAffPk())).incrementTransCompleted();
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(member.getAffPk())).incrementAddsCompleted();
                }
            } catch (EJBException e) {
                logger.debug("", e);
                /** @TODO: do something with the addErrors. */
            }
            addDone = false;
        }

        // do member inactivation
        boolean inactivateDone = false;
        Map.Entry entry = null;
        Integer affPk = null;
        Integer personPk = null;
        List members = null;
        for (Iterator it = tab.getInactivated().entrySet().iterator(); it.hasNext(); ) {
            entry = (Map.Entry)it.next();
            affPk = (Integer)entry.getKey();
            members = (List)entry.getValue();
            for (Iterator it2 = members.iterator(); it2.hasNext(); ) {
                personPk = (Integer)it2.next();
                inactivateDone = removeMember(userPk, affPk, personPk);
                if (inactivateDone) {
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(affPk)).incrementTransCompleted();
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(affPk)).incrementInacCompleted();
                }
                inactivateDone = false;
            }
        }

        // do officer T record
        boolean tRecordDone = false;
        for (Iterator it = tab.getTRecords().entrySet().iterator(); it.hasNext(); ) {
            entry = (Map.Entry)it.next();
            affPk = (Integer)entry.getKey();
            members = (List)entry.getValue();
            for (Iterator it2 = members.iterator(); it2.hasNext(); ) {
                personPk = (Integer)entry.getValue();
                tRecordDone = makeTempRecord(userPk, affPk, personPk);
                if (tRecordDone) {
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(affPk)).incrementTransCompleted();
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(affPk)).incrementInacTCount();
                }
                tRecordDone = false;
            }
        }

        // store new update summary. This should be the last thing to do (after all other transactions)
        storeUpdateSummary(queuePk, updateSummary);
        //*************************************************************************************
        //System logging starts here event: Update Applied logging o        
        MemberUpdateSummary mSummary        =   getMemberUpdateSummary(queuePk);
        Map                 uRSmryMap       =   mSummary.getAffUpdateSummary();        
        PersonReviewData    tCount          =   mSummary.getTotalCounts();        
        SystemLog.logUpdateApplied(tCount.getAddsCompleted(), tCount.getChangesCompleted(), tCount.getInacCompleted(), userPk.toString());                
        //******************************************************************************************        
        Map.Entry           parentEntry                 =   null;
        ArrayList           list                        =   null;
        Iterator            lt                          =   null;
        HashMap             map                         =   null;
        ExceptionData       eData                       =   null;
        Map                 exceptions                  =   mSummary.getExceptions();
        //**************************************************************************
        //log if the summary has exceptions    
        //*****************************************************************************
        logger.debug("exceptions=>" + exceptions);
        if((exceptions != null) && !(exceptions.isEmpty())){
            Iterator it     = exceptions.entrySet().iterator();
            parentEntry     = (Map.Entry)it.next();
            logger.debug("parentEntry.getValue()===>" + parentEntry.getValue());
            list   = (ArrayList) parentEntry.getValue();
            //map   = (HashMap) parentEntry.getValue();
            lt    = list.iterator();
            while(lt.hasNext()){
                //logger.debug("lt.next()===>" + lt.next());
                eData               =   (ExceptionData) lt.next();
                logger.debug("eData===>" + eData);
                Map eComp           =   eData.getFieldChangeDetails();
                logger.debug("eComp===>" + eComp);
                Iterator eCompIt    =   eComp.values().iterator();
                logger.debug("eCompIt===>" + eCompIt);
                while(eCompIt.hasNext()){
                    ArrayList al            =   (ArrayList) eCompIt.next();
                    Iterator alIt           =   al.iterator();
                    logger.debug("got alIt===>" + alIt);
                    while(alIt.hasNext()){
                        ExceptionComparison ec = (ExceptionComparison) alIt.next();
                        logger.debug(" ec===>" + ec);
                        if(ec.getError()){
                            logger.debug(" calling logRecordUpdateError===>" );
                            SystemLog.logRecordUpdateError(ec.getField(), new java.sql.Timestamp(new java.util.Date().getTime()), "System value different", userPk.toString());
                        }
                    }//end of innermost while
                }//end of inner while                                                        
            }//end of while
        }//end of if
        //***********************************************************************************

        

    }
  
    /**
     * Gets affiliate status code
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk The primary key of the affiliate
     * @return status code value
     */
    public Integer getAffiliateStatusCode(Integer affPk) {
        Integer codeValue = null;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_AFFILIATE_STATUS);
            ps.setInt(1, affPk.intValue());

            rs = ps.executeQuery();

            if (rs.next())
                codeValue = new Integer(rs.getInt(1));

        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return codeValue;
    }

    /**
     * Retrieves the value of the specified configuration data
     *
     * @param variableName The Config Variable Name
     * @return the Config Value of the variableName
     */
    public String getAppConfigData(String variableName) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_APP_CONFIG_VALUE);
            ps.setString(1, variableName);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return result;
    }


    /**
     * Gets the number of members in an affiliate.  This is used to compare the number of
     *  members in the file.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getMemberCount(Integer affPk) {
        int count = 0;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_ALL_MEMBERS_FOR_AFFILIATE);
            ps.setInt(1, affPk.intValue());

            rs = ps.executeQuery();

            while (rs.next())
                count++;
        }
        catch(SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return count;
    }

    /**
     * Looks for a match for the given element during the Pre-Update Summary process.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param member The member to attempt to match
     * @param preUpdateSummary The pre-update summary object to be filled.
     */
    public void matchMember(MemberUpdateElement member, MemberPreUpdateSummary updateSummary) {
        logger.debug(" matchMember called========> " );
        //*****************************************************************************************
        //timer to log the elapsed time
        java.sql.Timestamp startTime = new java.sql.Timestamp(new java.util.Date().getTime());
        //*******************************************************************************************
        MemberChanges memberChanges = (MemberChanges)updateSummary.getMemberCounts().get(member.getAffId());
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exceptionFound = false;

        // Data Match Rule 1. - try to match the Affiliate Member Number
        try {
            boolean matchFound = false;
            String firstName = null;
            String lastName = null;
            MemberUpdateElement memberInSystem = null;
            logger.debug("executing sql for member affpk member.getAffMbrNumber()========> " + member.getAffMbrNumber());
            if (!TextUtil.isEmptyOrSpaces(member.getAffMbrNumber())) {

                // increment count since valid affiliate member number exists
                memberChanges.incrementAffMemberNumCountInFile();

                ps = con.prepareStatement(SQL_MEMBER_MATCH + SQL_WHERE_MEMBER_MATCH_AFF_MEMBER_NUMBER);
                ps.setInt(1, member.getAffPk().intValue());
                ps.setString(2, member.getAffMbrNumber());
                logger.debug("executing sql for member affpk ========> " + member.getAffPk().intValue());
                logger.debug("executing sql for member num ========> " + member.getAffMbrNumber());
                rs = ps.executeQuery();

                while (rs.next()) {
                    logger.debug("found for member ========> " + rs.getString("first_nm"));
                    firstName = rs.getString("first_nm");
                    lastName = rs.getString("last_nm");
                    
                    //logger.debug("----------------------------------------------------");
                    //logger.debug("File firstName: " + member.getFirstName());
                    //logger.debug("Syst firstName: " + firstName);
                    //logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(firstName, member.getFirstName()));
                    //logger.debug("----------------------------------------------------");
                    //logger.debug("File lastName:  " + member.getLastName());
                    //logger.debug("Syst lastName:  " + lastName);
                    //logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(lastName, member.getLastName()));
                    //logger.debug("----------------------------------------------------");

                    // Data Match Rule 1a. - if match Affiliate Member Number, try to confirm match on Name
                    if (TextUtil.equalsIgnoreCase(firstName, member.getFirstName()) &&
                        TextUtil.equalsIgnoreCase(lastName, member.getLastName())
                    ) {
                        //if true then no exceptions found
                        if (processMatchedMemberUpdateElement(rs, member, memberInSystem, memberChanges, updateSummary)) {
                            matchFound = true;
                        }
                        else {
                            exceptionFound = true;
                        }
                        break;
                    }
                }
                DBUtil.cleanup(null, ps, rs);
            }

            if ((!matchFound) && (!exceptionFound)) {

                // Data Match Rule 2. - try to match on SSN
                ps = con.prepareStatement(SQL_MEMBER_MATCH + SQL_WHERE_MEMBER_MATCH_SSN);
                ps.setInt(1, member.getAffPk().intValue());
                ps.setString(2, member.getSsn());

                rs = ps.executeQuery();

                while (rs.next()) {
                    firstName = rs.getString("first_nm");
                    lastName = rs.getString("last_nm");

                    //logger.debug("----------------------------------------------------");
                    //logger.debug("File firstName: " + member.getFirstName());
                    //logger.debug("Syst firstName: " + firstName);
                    //logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(firstName, member.getFirstName()));
                    //logger.debug("----------------------------------------------------");
                    //logger.debug("File lastName:  " + member.getLastName());
                    //logger.debug("Syst lastName:  " + lastName);
                    //logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(lastName, member.getLastName()));

                    // Data Match Rule 2a. - if match SSN, try to confirm match on Name
                    if (TextUtil.equalsIgnoreCase(firstName, member.getFirstName()) &&
                        TextUtil.equalsIgnoreCase(lastName, member.getLastName())
                    ) {
                        //if true then no exceptions found
                        if (processMatchedMemberUpdateElement(rs, member, memberInSystem, memberChanges, updateSummary)) {
                            matchFound = true;
                        }
                        else {
                            exceptionFound = true;
                        }
                        break;
                    }
                }
            }

            // Update the match and no-match counts (based only on if no Aff Mbr Nbr comes in)
            if ((TextUtil.isEmptyOrSpaces(member.getAffMbrNumber())) && (!exceptionFound)) {
                if (matchFound)
                    memberChanges.incrementMatch();
                else
                    memberChanges.incrementNonMatch();
            }

            // Update the remaining summary fields and check for no data matches
            if ((!matchFound) && (!exceptionFound)) {
                // perform error checks, and update add count if no errors
                if (!performExceptionChecksForMember(member, updateSummary, null)) {
                    memberChanges.incrementAdded();
                }
            }

            // we will do the "inSystem" altogether later so that we don't double count
            // those members who in both "inFile" and "inSystem"
            memberChanges.incrementInFile();
            //*****************************************************************************************
            //log the matched and non matched count
            //*******************************************************************************************
                       
            java.sql.Timestamp endTime      = new java.sql.Timestamp(new java.util.Date().getTime());                
            String startSecs                = startTime.toString().substring(startTime.toString().indexOf(':' , 16)).substring(1);
            String endSecs                  = endTime.toString().substring(endTime.toString().indexOf(':' , 16)).substring(1);        
            double elapsedTime              = new Double((new Double(endSecs)).doubleValue() - (new Double(startSecs)).doubleValue()).doubleValue();        
            //if(fileEntry != null){  
                logger.debug("got inside of to log");
                MemberChanges tCount        = updateSummary.getTotalCounts();                           
                SystemLog.logDataMatchingPerformed("1", tCount.getMatch(), tCount.getNonMatch(), startTime, elapsedTime,"");//, fileEntry.getFileName());
            //}
            //*************************************************************************************************
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

    /**
     * Looks for a match for the given element during the Update process.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param member The member to attempt to match
     * @param UpdateSummary The update summary object to be filled.
     * @param tab The memberUpdateTabluation object
     *
     * Checks using the following criteria for member exceptions (record errors):
     *  1. check that Member cannot occur more than once in the same transmittal file for the same affiliate.
     */
    public void matchMember(MemberUpdateElement member,
                            MemberUpdateSummary updateSummary,
                            MemberUpdateTabulation tab )
    {
        logger.debug(" matchMember called========> " );
        PersonReviewData affReviewSummary = (PersonReviewData)updateSummary.getAffUpdateSummary().get(member.getAffPk());

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exceptionFound = false;

        // Data Match Rule 1. - try to match the Affiliate Member Number
        try {
            boolean matchFound = false;
            String firstName = null;
            String lastName = null;
            MemberUpdateElement memberInSystem = null;
            logger.debug("executing sql for member affpk member.getAffMbrNumber()========> " + member.getAffMbrNumber());
            if (!TextUtil.isEmptyOrSpaces(member.getAffMbrNumber())) {

                ps = con.prepareStatement(SQL_MEMBER_MATCH + SQL_WHERE_MEMBER_MATCH_AFF_MEMBER_NUMBER);
                ps.setInt(1, member.getAffPk().intValue());
                ps.setString(2, member.getAffMbrNumber());

                rs = ps.executeQuery();
                logger.debug("executing sql for member affpk based on aff member number========> " + member.getAffPk().intValue());
                logger.debug("executing sql for member num ========> " + member.getAffMbrNumber());
                    
                while (rs.next()) {
                    firstName = rs.getString("first_nm");
                    lastName = rs.getString("last_nm");
                    logger.debug("found for member based on aff member number========> " + rs.getString("first_nm"));
                    logger.debug("----------------------------------------------------");
                    logger.debug("Aff Mbr # Match attempted.");
                    logger.debug("File firstName: " + member.getFirstName());
                    logger.debug("Syst firstName: " + firstName);
                    logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(firstName, member.getFirstName()));
                    logger.debug("----------------------------------------------------");
                    logger.debug("File lastName:  " + member.getLastName());
                    logger.debug("Syst lastName:  " + lastName);
                    logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(lastName, member.getLastName()));

                    // Data Match Rule 1a. - if match Affiliate Member Number, try to confirm match on Name
                    if (TextUtil.equalsIgnoreCase(firstName, member.getFirstName()) &&
                        TextUtil.equalsIgnoreCase(lastName, member.getLastName())
                    ) {
                        //if true then no exceptions found
                        if (processMatchedMemberUpdateElement(rs, member, memberInSystem, tab, updateSummary)) {
                            matchFound = true;
                        }
                        else {
                            exceptionFound = true;
                        }
                        break;
                    }
                }
            }

            DBUtil.cleanup(null, ps, rs);

            if ((!matchFound) && (!exceptionFound)) {

                // Data Match Rule 2 - try to match on SSN
                ps = con.prepareStatement(SQL_MEMBER_MATCH + SQL_WHERE_MEMBER_MATCH_SSN);
                ps.setInt(1, member.getAffPk().intValue());
                ps.setString(2, member.getSsn());

                rs = ps.executeQuery();
                logger.debug("executing sql for member affpk based on ssn number========> " + member.getAffPk().intValue());
                logger.debug("executing sql for member num ========> " + member.getAffMbrNumber());
                    

                while (rs.next()) {
                    firstName = rs.getString("first_nm");
                    lastName = rs.getString("last_nm");
                    logger.debug("found for member based on aff member number========> " + rs.getString("first_nm"));
                    logger.debug("----------------------------------------------------");
                    logger.debug("SSN Match attempted.");
                    logger.debug("File firstName: " + member.getFirstName());
                    logger.debug("Syst firstName: " + firstName);
                    logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(firstName, member.getFirstName()));
                    logger.debug("----------------------------------------------------");
                    logger.debug("File lastName:  " + member.getLastName());
                    logger.debug("Syst lastName:  " + lastName);
                    logger.debug(" equal?         " + TextUtil.equalsIgnoreCase(lastName, member.getLastName()));

                    // Data Match Rule 2a - if match SSN, try to confirm match on Name
                    if (TextUtil.equalsIgnoreCase(firstName, member.getFirstName()) &&
                        TextUtil.equalsIgnoreCase(lastName, member.getLastName())
                    ) {
                        //if true then no exceptions found
                        if (processMatchedMemberUpdateElement(rs, member, memberInSystem, tab, updateSummary)) {
                            matchFound = true;
                        }
                        else {
                            exceptionFound = true;
                        }
                        break;
                    }
                }
            }

            if ((!matchFound) && (!exceptionFound)) {
                logger.debug("not found member based on aff member and ssn number========> " );
                // perform error checks, and update add count if no errors
                if (!performExceptionChecksForMember(member, updateSummary, null)) {
                    logger.debug("passed exception check========> " );
                    // Rule 1. - check for duplicate member exception for add
                    if (tab.getAdditions().contains(member)) {
                        logger.debug("duplicate member found========> " );
                        MemberUpdateElement dup = (MemberUpdateElement) tab.getAdditions().get(tab.getAdditions().indexOf(member));
                        tab.getNotIncluded().add(dup);
                        tab.getAdditions().remove(member);

                        //add the duplicate exception with first record
                        ExceptionData eData = new ExceptionData(ExceptionData.MEMBER);
                        eData.setFirstName(dup.getFirstName());
                        eData.setLastName(dup.getLastName());
                        eData.setMiddleName(dup.getMiddleName());
                        eData.setPersonPk(dup.getPersonPk());
                        eData.setSuffix(getCodeDescription("Suffix", dup.getSuffixCodePk()));
                        eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
                        updateSummary.addException(dup.getAffPk(), eData);

                        affReviewSummary.decrementTransAttempted();
                        affReviewSummary.decrementAddsAttempted();

                        //add the duplicate exception with this record
                        eData = new ExceptionData(ExceptionData.MEMBER);
                        eData.setFirstName(member.getFirstName());
                        eData.setLastName(member.getLastName());
                        eData.setMiddleName(member.getMiddleName());
                        eData.setPersonPk(member.getPersonPk());
                        eData.setSuffix(getCodeDescription("Suffix", member.getSuffixCodePk()));
                        eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
                        updateSummary.addException(member.getAffPk(), eData);

                        tab.getNotIncluded().add(member);
                        exceptionFound = true;
                    }
                    else {
                        // increment Add and place on the Add list
                        tab.getAdditions().add(member);
                        affReviewSummary.incrementTransAttempted();
                        affReviewSummary.incrementAddsAttempted();
                    }
                }
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }

    }

    /**
     * Get members that are officers (inSystem, newTRecords, inactivated)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void processInactiveMembers(List affPks,
                                       MemberUpdateSummary updateSummary,
                                       MemberUpdateTabulation tab
    ) {
        logger.debug("----------------------------------------------------");
        logger.debug("processInactiveMembers called.");

        // retrieve all members (personPK) in system first.

        Connection con = DBUtil.getConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            ps1 = con.prepareStatement(SQL_SELECT_ALL_MEMBERS_FOR_AFFILIATE);
            ps2 = con.prepareStatement(SQL_SELECT_ALL_OFFICERS_FOR_AFFILIATE);


            for (Iterator it1 = affPks.iterator(); it1.hasNext(); ) {
                Integer affPk = (Integer)it1.next();
                ps1.setInt(1, affPk.intValue());
                rs1 = ps1.executeQuery();

                Set membersInSystem = new HashSet();
                while (rs1.next()) {
                    membersInSystem.add(new Integer(rs1.getInt(1)));
                }

                // get those members in system, but not in file.
                membersInSystem.removeAll(tab.getMatchedMemberPks());
                Set nonMatchedMembers = membersInSystem;

                // check these members if they are officers
                for (Iterator it2 = nonMatchedMembers.iterator(); it2.hasNext(); ) {
                    Integer personPk = (Integer)it2.next();
                    ps2.setInt(1, affPk.intValue());
                    ps2.setInt(2, personPk.intValue());
                    rs2 = ps2.executeQuery();

                    boolean isCurrentOfficer = false;
                    while (rs2.next()) {
                        Timestamp expirationDate = rs2.getTimestamp(1);
                        Integer termLengthCodePk = new Integer(rs2.getInt(2));
                        if (expirationDate == null) { // this inSystem member is currently an officer
                            isCurrentOfficer = true;
                            tab.addCurrentOfficer(affPk, personPk);
                            if (TextUtil.equals(termLengthCodePk, TermLength.INDEFINITE)) { // The officer has an Indefinite term
                                tab.addInactivated(affPk, personPk);
                            } else {
                                tab.addTRecords(affPk, personPk);
                            }
                            break;
                        }
                    }
                    if (!isCurrentOfficer) { // this inSystem member is NOT an officer
                        tab.addInactivated(affPk, personPk);
                    }
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(affPk)).incrementTransAttempted();
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(affPk)).incrementInacAttempted();
                }
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(null, ps1, rs1);
            DBUtil.cleanup(con, ps2, rs2);
        }
    }

    /**
     * Stores an MemberPreUpdateSummary
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void storePreUpdateSummary(Integer queuePk, MemberPreUpdateSummary summary) {
        logger.debug("----------------------------------------------------");
        logger.debug("storePreUpdateSummary(Integer, MemberPreUpdateSummary) called.");

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        Map.Entry entry = null;
        Integer affPk = null;

        try {
            // save Affiliates to AUP_Member_Pre_Upd_Smry table
            logger.debug("saving member counts...");
            ps = con.prepareStatement(SQL_INSERT_MEMBER_PRE_UPDATE_SUMMARY);
            AffiliateIdentifier affId = null;
            MemberChanges memChanges = null;
            logger.debug("summary: " + summary);
            for (Iterator it = summary.getMemberCounts().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affId = (AffiliateIdentifier)entry.getKey();
                memChanges = (MemberChanges)entry.getValue();
                affPk = memChanges.getAffPk();
                logger.debug("    values: ");
                logger.debug("            affPk: " + affPk);
                logger.debug("            affId: " + affId);
                logger.debug("            stats: " + memChanges);

                ps.setInt(1, queuePk.intValue());
                DBUtil.setNullableInt(ps, 2, affPk);
                ps.setInt(3, memChanges.getInSystem());
                ps.setInt(4, memChanges.getInFile());
                ps.setInt(5, memChanges.getAdded());
                ps.setInt(6, memChanges.getInactivated());
                ps.setInt(7, memChanges.getChanged());
                ps.setInt(8, memChanges.getNewTRecords());
                ps.setInt(9, memChanges.getMatch());
                ps.setInt(10, memChanges.getNonMatch());
                DBUtil.setBooleanAsShort(ps, 11, memChanges.getHasWarning());
                DBUtil.setBooleanAsShort(ps, 12, memChanges.getHasError());

                // only fill in aff_err fields if affPk does not exist (not valid affiliate)
                if (affPk == null) {
                    DBUtil.setNullableVarchar(ps, 13, affId.getType().toString());
                    DBUtil.setNullableVarchar(ps, 14, affId.getLocal());
                    DBUtil.setNullableVarchar(ps, 15, affId.getSubUnit());
                    DBUtil.setNullableVarchar(ps, 16, affId.getCouncil());
                }
                else {
                    ps.setNull(13, Types.CHAR);
                    ps.setNull(14, Types.VARCHAR);
                    ps.setNull(15, Types.VARCHAR);
                    ps.setNull(16, Types.VARCHAR);
                }

                ps.addBatch();
            }
            ps.executeBatch();
            DBUtil.cleanup(null, ps, null);
            logger.debug("...member saved...");

            // save Exceptions to the AUP_Pre_Err_Smry and AUP_Pre_Err_Dtl tables
            logger.debug("saving exceptions...");
            ps = con.prepareStatement(SQL_INSERT_PRE_ERROR_SUMMARY_EXCEPTIONS);
            ps2 = con.prepareStatement(SQL_INSERT_PRE_ERROR_DETAIL_EXCEPTIONS);

            List exceptions = null;
            ExceptionData exData = null;
            Integer updateFieldPk = null;
            ExceptionComparison compar = null;
            int recordId = 1; /* Leave this line out here and not in the first loop.
                               * The recordId is an overall counter of the exceptions
                               * in the file.
                               **/
            for (Iterator it = summary.getExceptions().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affPk = (Integer)entry.getKey();
                exceptions = (List)entry.getValue();
                for (Iterator it2 = exceptions.iterator(); it2.hasNext(); recordId++) {
                    exData = (ExceptionData)it2.next();
                    ps.setInt(1, affPk.intValue());
                    ps.setInt(2, queuePk.intValue());
                    ps.setInt(3, recordId);
                    DBUtil.setNullableInt(ps, 4, exData.getPersonPk());
                    DBUtil.setNullableVarchar(ps, 5, exData.getLastName());
                    DBUtil.setNullableVarchar(ps, 6, exData.getMiddleName());
                    DBUtil.setNullableVarchar(ps, 7, exData.getFirstName());
                    DBUtil.setNullableVarchar(ps, 8, exData.getSuffix());
                    DBUtil.setNullableInt(ps, 9, exData.getUpdateErrorCodePk());
                    ps.addBatch();

                    for (Iterator it3 = exData.getFieldChangeDetails().entrySet().iterator(); it3.hasNext(); ) {
                        entry = (Map.Entry)it3.next();
                        updateFieldPk = (Integer)entry.getKey();
                        compar = (ExceptionComparison)entry.getValue();
                        ps2.setInt(1, affPk.intValue());
                        ps2.setInt(2, queuePk.intValue());
                        ps2.setInt(3, recordId);
                        ps2.setInt(4, updateFieldPk.intValue());
                        DBUtil.setNullableVarchar(ps2, 5, compar.getValueInSystem());
                        DBUtil.setNullableVarchar(ps2, 6, compar.getValueInFile());
                        DBUtil.setBooleanAsShort(ps2, 7, compar.isError());
                        ps2.addBatch();
                    }
                }
            }
            ps.executeBatch();
            ps2.executeBatch();
            DBUtil.cleanup(null, ps, null);
            DBUtil.cleanup(null, ps2, null);
            logger.debug("...exceptions saved...");

            // save Field Change counts to the AUP_Pre_Fld_Chg_Smry
            logger.debug("saving field change counts...");
            ps = con.prepareStatement(SQL_INSERT_PRE_FIELD_CHANGE_SUMMARY);

            FieldChange fChange = null;
            for (Iterator it = summary.getFieldChanges().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                updateFieldPk = (Integer)entry.getKey();
                fChange = (FieldChange)entry.getValue();
                ps.setInt(1, queuePk.intValue());
                ps.setInt(2, updateFieldPk.intValue());
                ps.setInt(3, fChange.getCount());
                ps.addBatch();
            }
            ps.executeBatch();
            logger.debug("...field change counts saved.");
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * Updates a list member.
     *
     * @ejb:interface-method view-typ="lcoal"
     * @ejb:transaction type="Required"
     *
     */
    public boolean updateMember(Integer userPk, MemberUpdateElement member, Set totalErrors) {
        logger.debug("----------------------------------------------------");
        logger.debug("updateMember called.");

        AffiliateData affData = affilBean.getAffiliateData(member.getAffPk());
        if (affData == null) {
            throw new EJBException("Member's Affiliate not found.");
        }

        logger.debug("Updating Person...");
        PersonData person = new PersonData();
        person.setAddressPk(member.getAddressPk());
        person.setFirstNm(member.getFirstName());
        person.setMiddleNm(member.getMiddleName());
        person.setLastNm(member.getLastName());
        person.setPersonPk(member.getPersonPk());
        person.setPrefixNm(member.getPrefixCodePk());
        person.setSsn(member.getSsn());
        person.setSsnValid(new Boolean(false));
        person.setSuffixNm(member.getSuffixCodePk());
        personsBean.updatePersonDetail(userPk, person);
        logger.debug("... Person updated");
        
        // only process phone if not null or spaces or all zeroes
        if (checkPhoneExists(member)) {
            logger.debug("Processing Phone...");
            
            if (member.getPhonePk() != null && member.getPhonePk().intValue() > 0) {
                logger.debug("Updating Phone...");
                PhoneData phone = toPhoneData(member);
                personsBean.updatePersonPhone(userPk, member.getPersonPk(), phone);
                logger.debug("... Phone updated");
            }
            else {
                logger.debug("Adding Phone...");
                PhoneData phone = toPhoneData(member);
                personsBean.addPersonPhone(userPk, DEPT_PK, member.getPersonPk(), phone);
                logger.debug("... Phone added");                
            }
        }    

        if (member.getPrimaryAddr() != null) {
            logger.debug("Processing Address...");
            PersonAddress address = toPersonAddress(member.getPrimaryAddr());
            Set updateErrors = null;
            if (member.getAddressPk() == null || member.getAddressPk().intValue() < 1) {
                logger.debug("    ...Adding Address...");
                updateErrors = smaBean.addByAffiliate(userPk, affData.getAffPk(), member.getPersonPk(), address);
            } else {
                logger.debug("    ...Updating Address...");
                updateErrors = smaBean.updateByAffiliate(userPk, affData.getAffPk(), member.getPersonPk(), member.getAddressPk(), address);
            }
            if (!CollectionUtil.isEmpty(updateErrors)) {
                totalErrors.addAll(updateErrors);
                logger.debug("****************************************************");
                logger.debug("Address errors found: " + CollectionUtil.toString(updateErrors));
                logger.debug("****************************************************");
                //***************************************************************************************
                //logging starts here Event:Apply Update Error
                //***************************************************************************************
                SystemLog.logApplyUpdateError("Member Address", "Address errors found",userPk.toString()) ;
                //****************************************************************************************
                throw new EJBException("Address had errors.");
            }
            logger.debug("... Address processed");
        }

        logger.debug("Updating Demo Info...");
        personsBean.updatePersonDemographicsGender(member.getPersonPk(), userPk, member.getGenderCodePk());
        logger.debug("... Demo Info updated");

        logger.debug("Updating Poli Info...");
        personsBean.updatePoliticalDataPartyVoter(member.getPersonPk(), userPk, 
                                                  member.getPoliticalPartyCodePk(), 
                                                  member.getRegisteredVoterCodePk());
        logger.debug("... Poli Info updated");


        // for file status code = X (complimentary), do not update member
        logger.debug("Determining if Member...");
        if (getMemberType(member.getStatus(), affData.getAffiliateId().getType().charValue()) != null) {

            logger.debug("Updating Member...");
            MemberAffiliateData mbrAff = new MemberAffiliateData();
            mbrAff.setAffPk(member.getAffPk());
            mbrAff.setMbrJoinDt(member.getDateJoined());
            mbrAff.setMbrStatus(org.afscme.enterprise.codes.Codes.MemberStatus.A); // initialize to active.
            mbrAff.setMbrType(getMemberType(member.getStatus(), affData.getAffiliateId().getType().charValue()));
            mbrAff.setPersonPk(member.getPersonPk());
            mbrAff.setPrimaryInformationSource(member.getInfoSourceCodePk());
            if (!membersBean.updateMemberAffiliateData(mbrAff, userPk)) {
                throw new EJBException("Affiliate fields had errors.");
            }
            logger.debug("... Member updated");

            logger.debug("Updating Remaining Info...");
            updateRemainingFields(userPk, member.getPersonPk(), member);
            logger.debug("... Remaining Info updated");
        }    
            
        return true;
    }

    
    /**
     * Adds a member to an affiliate.
     */
    private boolean addMember(Integer userPk, MemberUpdateElement member, Set totalErrors) {
        logger.debug("----------------------------------------------------");
        logger.debug("addMember called.");
        logger.debug("member.getAffPk()=============>" + member.getAffPk());
        AffiliateData affData = affilBean.getAffiliateData(member.getAffPk());
        if (affData == null) {
            return false;
        }

        logger.debug("Adding person...");
        NewPerson newPerson = new NewPerson();
        newPerson.setFirstNm(member.getFirstName());
        newPerson.setLastNm(member.getLastName());
        newPerson.setMemberFg(new Boolean(true));
        newPerson.setMiddleNm(member.getMiddleName());
        newPerson.setPrefixNm(member.getPrefixCodePk());
        newPerson.setSsn(member.getSsn());
        newPerson.setSsnValid(new Boolean(false));
        newPerson.setSuffixNm(member.getSuffixCodePk());
        Integer personPk = personsBean.addPerson(userPk, newPerson);
        if (personPk == null || personPk.intValue() < 1) {
            throw new EJBException("Person had errors.");
        }
        logger.debug("... Person added");

        if (member.getPrimaryAddr() != null) {
            logger.debug("Adding Address...");
            PersonAddress address = toPersonAddress(member.getPrimaryAddr());
            newPerson.setThePersonAddress(address);
            Set addErrors = smaBean.addByAffiliate(userPk, member.getAffPk(), personPk, address);
            if (!CollectionUtil.isEmpty(addErrors)) {
                totalErrors.addAll(addErrors);
                logger.debug("****************************************************");
                logger.debug("Address errors found: " + CollectionUtil.toString(addErrors));
                logger.debug("****************************************************");
                throw new EJBException("Address had errors.");
            } else {
                logger.debug("... Address added");
            }
        }

        // only add phone if not null or spaces or all zeroes
        if (checkPhoneExists(member)) {
            logger.debug("Adding Phone...");
            PhoneData phone = toPhoneData(member);
            newPerson.setThePhoneData(phone);
            personsBean.addPersonPhone(userPk, DEPT_PK, personPk, phone);
            logger.debug("... Phone added");
        }    

        // update the following fields:
        logger.debug("Adding Demo Info...");
        personsBean.updatePersonDemographicsGender(personPk, userPk, member.getGenderCodePk());
        logger.debug("... Demo Info added");

        logger.debug("Adding Poli Info...");
        personsBean.updatePoliticalDataPartyVoter(personPk, userPk, 
                                                  member.getPoliticalPartyCodePk(), 
                                                  member.getRegisteredVoterCodePk());        
        logger.debug("... Poli Info added");


        // for file status code = X (complimentary), do not add member
        logger.debug("Determining if Member...");
        if (getMemberType(member.getStatus(), affData.getAffiliateId().getType().charValue()) != null) {
        
            logger.debug("Adding Member...");
            NewMember newMember = new NewMember();
            newMember.setTheNewPerson(newPerson);
            newMember.setAffPk(member.getAffPk());
            newMember.setMbrJoinDt(member.getDateJoined());
            newMember.setMbrStatus(org.afscme.enterprise.codes.Codes.MemberStatus.A); // initialize to active.
            newMember.setMbrType(getMemberType(member.getStatus(), affData.getAffiliateId().getType().charValue()));
            newMember.setPersonPk(member.getPersonPk());
            newMember.setTheAffiliateIdentifier(affData.getAffiliateId());
            newMember.setPersonPk(personPk);
            logger.debug("addmember===========================================>");
            int code = membersBean.addMember(newMember, userPk, DEPT_PK, member.getAffPk());
            logger.debug("code=============>" + code);
            
            //if (membersBean.addMember(newMember, userPk, DEPT_PK, member.getAffPk()) != 0) {
            if(code !=0){
                throw new EJBException("Member had errors.");
            }
            logger.debug("... Member added");

            logger.debug("Adding Remaining fields...");
            updateRemainingFields(userPk, personPk, member);
            logger.debug("... Remaining fields added");
        }    

        return true;
    }

    private int getLegacyNoMailCode(boolean noMail, boolean noCards, boolean noPEMail, boolean noLegMail) {
        if (!noMail && !noCards && !noPEMail && !noLegMail) {
            return 0;
        }
        if (!noMail && noCards && !noPEMail && !noLegMail) {
            return 1;
        }
        if (!noMail && !noCards && noPEMail && !noLegMail) {
            return 2;
        }
        if (!noMail && noCards && noPEMail && !noLegMail) {
            return 3;
        }
        return 9;
    }

    private String getLegacyStatus(int mbrStatus, int mbrType) {
        // If status == I or T, return "I". this will show an affiliate reactivating a member
        if (mbrStatus == org.afscme.enterprise.codes.Codes.MemberStatus.I.intValue() ||
            mbrStatus == org.afscme.enterprise.codes.Codes.MemberStatus.T.intValue()
        ) {
            return "I";
        }   // If type == A, legacy status = "N"
        else if (mbrType == org.afscme.enterprise.codes.Codes.MemberType.A.intValue()) {
            return "N";
        } else {    // all other cases should be active memebers if they exist...
            return "A";
        }
    }

    /**
     */
    private Integer getMemberType(String fileStatus, char affType) {
        logger.debug("getMemberType called.");
        // set to default values
        logger.debug("fileStatus===>" + fileStatus);
        logger.debug("affType===>" + affType);
        if (!TextUtil.isEmptyOrSpaces(fileStatus)) { // determine based on business rules
            // If Status Code == N, status='A' and type = 'A'
            if (fileStatus.equalsIgnoreCase("N")) {
                return org.afscme.enterprise.codes.Codes.MemberType.A;
            }
            // If Status Code == X, insert person only, return false
            else if (fileStatus.equalsIgnoreCase("X")) {
                logger.debug("returning null from getMemberType===>" );
                return null;
            }
        }// Otherwise, Status Code == A or null. base return value on affType...
        switch(affType) {
            case 'R':
            case 'S':
                return org.afscme.enterprise.codes.Codes.MemberType.T;
            default:
                return org.afscme.enterprise.codes.Codes.MemberType.R;
        }
    }

    /**
     * Sets a member to a 'T' record.
     *
     */
    private boolean makeTempRecord(Integer userPk, Integer affPk, Integer personPk) {
        membersBean.inactivateMember(personPk, affPk, org.afscme.enterprise.codes.Codes.MemberStatus.T, userPk);
        return true;
    }

    /**
     * Checks for exceptions and updates the MemberPreUpdateSummary accordingly.
     *
     * @param member    The Member data in the file.
     * @param summary   The current preupdate summary information being generated.
     * @param memberInSystem    The Member data in the system (if exists).
     *
     * @return  Returns true if an error is found.
     *
     * Checks using the following criteria for member exceptions (record errors):
     *  1. each record must have Required fields which are First Name, Last Name and Local Number.
     *  2. each record must not have Member Zip that is all zeros.  Zeros indicate this is an unmailable address.
     *  3. each record must not have a SSN that is all zeros.
     *  4. member record has recently been updated.
     */
    private boolean performExceptionChecksForMember(MemberUpdateElement member, MemberPreUpdateSummary summary, MemberUpdateElement memberInSystem) {

        boolean error = false;
        ExceptionData eData = new ExceptionData(ExceptionData.MEMBER);
        eData.setFirstName(member.getFirstName());
        eData.setLastName(member.getLastName());
        eData.setMiddleName(member.getMiddleName());
        eData.setPersonPk(member.getPersonPk());
        eData.setSuffix(getCodeDescription("Suffix", member.getSuffixCodePk()));
        if (memberInSystem == null)
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.ADD);
        else
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.CHANGE);

        String systemValue = null;
        // compare prefix
        if (memberInSystem != null) {
            systemValue = getCodeDescription("Prefix", memberInSystem.getPrefixCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.PREFIX, getCodeDescription("Prefix", member.getPrefixCodePk()), systemValue, false);

        // compare first name
        if (memberInSystem != null) {
            systemValue = memberInSystem.getFirstName();
        }
        if (TextUtil.isEmptyOrSpaces(member.getFirstName())) {
            // Rule 1a. - each record must have Required fields which are First Name, Last Name and Local Number.
            error = true;
            logger.debug("member.getFirstName()===>" +member.getFirstName()+ " has errors");
            
            updateExceptionData(eData, Codes.MemberUpdateFields.FIRST_NAME, member.getFirstName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.MemberUpdateFields.FIRST_NAME, member.getFirstName(), systemValue, false);
        }

        // compare middle name
        if (memberInSystem != null) {
            systemValue = memberInSystem.getMiddleName();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.MIDDLE_NAME, member.getMiddleName(), systemValue, false);

        // compare last name
        if (memberInSystem != null) {
            systemValue = memberInSystem.getLastName();
        }
        if (TextUtil.isEmptyOrSpaces(member.getLastName())) {
            // Rule 1b. - each record must have Required fields which are First Name, Last Name and Local Number.
            error = true;
            logger.debug("member.getLastName()===>" +member.getLastName()+ " has errors");
            updateExceptionData(eData, Codes.MemberUpdateFields.LAST_NAME, member.getLastName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.MemberUpdateFields.LAST_NAME, member.getLastName(), systemValue, false);
        }

        // compare suffix
        if (memberInSystem != null) {
            systemValue = getCodeDescription("Suffix", memberInSystem.getSuffixCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.SUFFIX, getCodeDescription("Suffix", member.getSuffixCodePk()), systemValue, false);

        // compare addr1
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getAddr1();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.ADDR1, member.getPrimaryAddr().getAddr1(), systemValue, false);

        // compare addr2
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getAddr2();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.ADDR2, member.getPrimaryAddr().getAddr2(), systemValue, false);

        // compare city
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getCity();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.CITY, member.getPrimaryAddr().getCity(), systemValue, false);

        // compare state
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getState();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.STATE, member.getPrimaryAddr().getState(), systemValue, false);

        // compare zip
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getZipCode() + "-" + memberInSystem.getPrimaryAddr().getZipPlus();
        }
        if ((member.getPrimaryAddr().getZipCode().length() != 5) ||
            (TextUtil.isAllZeros(member.getPrimaryAddr().getZipCode())) ||
            (TextUtil.isEmptyOrSpaces(member.getPrimaryAddr().getZipCode()))) {
            // Rule 2. - Member Zip that is all zeros.  Zeros indicate this is an unmailable address.
            error = true;
            logger.debug("member.getPrimaryAddr().getZipCode().length()===>" +member.getPrimaryAddr().getZipCode().length()+ " has errors because of length");
            updateExceptionData(eData, Codes.MemberUpdateFields.ZIP,
                                member.getPrimaryAddr().getZipCode() + "-" + member.getPrimaryAddr().getZipPlus(),
                                systemValue, true);
        } else {
            updateExceptionData(eData, Codes.MemberUpdateFields.ZIP,
                                member.getPrimaryAddr().getZipCode() + "-" + member.getPrimaryAddr().getZipPlus(),
                                systemValue, false);
        }

        // compare mailable address flag
        if (memberInSystem != null) {
            systemValue = Boolean.toString(memberInSystem.isMailableAddr());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.MAILABLE_ADDRESS,
                            Boolean.toString(member.isMailableAddr()),
                            systemValue, false);

        // compare no mail flag
        if (memberInSystem != null) {
            systemValue = Integer.toString(memberInSystem.getNoMailFlag());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.NO_MAIL,
                            Integer.toString(member.getNoMailFlag()), systemValue, false);

        // compare phone
        if (memberInSystem != null) {
            systemValue = memberInSystem.getAreaCode() + "-" + memberInSystem.getPhoneNumber();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.PHONE,
                            member.getAreaCode() + "-" + member.getPhoneNumber(),
                            systemValue, false);

        // compare member status
        if (memberInSystem != null) {
            systemValue = memberInSystem.getStatus();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.STATUS, member.getStatus(), systemValue, false);

        // compare gender
        if (memberInSystem != null) {
            systemValue = getCodeDescription("Gender", memberInSystem.getGenderCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.GENDER, getCodeDescription("Gender", member.getGenderCodePk()), systemValue, false);

        // compare date joined
        if (memberInSystem != null) {
            systemValue = DateUtil.getSimpleDateString(memberInSystem.getDateJoined());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.DATE_JOINED, DateUtil.getSimpleDateString(member.getDateJoined()), systemValue, false);

        // compare registered voter
        if (memberInSystem != null) {
            systemValue = getCodeDescription("RegisteredVoter", memberInSystem.getRegisteredVoterCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.REGISTERED_VOTER, getCodeDescription("RegisteredVoter", member.getRegisteredVoterCodePk()), systemValue, false);

        // compare political party
        if (memberInSystem != null) {
            systemValue = getCodeDescription("PoliticalParty", memberInSystem.getPoliticalPartyCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.POLITICAL_PARTY, getCodeDescription("PoliticalParty", member.getPoliticalPartyCodePk()), systemValue, false);

        // compare ssn
        if (memberInSystem != null) {
            systemValue = memberInSystem.getSsn();
        }
        if (isSSNValid(member.getSsn())) {
            updateExceptionData(eData, Codes.MemberUpdateFields.SSN, member.getSsn(), systemValue, false);
        } else {
            // Rule 3. - Member with a SSN that is all zeros.
            logger.debug("member.getSsn()===>" +member.getSsn()+ " has errors because isSSNValid(member.getSsn())" + isSSNValid(member.getSsn()));

            error = true;
            updateExceptionData(eData, Codes.MemberUpdateFields.SSN, member.getSsn(), systemValue, true);
        }

        // compare information source
        if (memberInSystem != null) {
            systemValue = getCodeDescription("InformationSource", memberInSystem.getInfoSourceCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.INFORMATION_SOURCE, getCodeDescription("InformationSource", member.getInfoSourceCodePk()), systemValue, false);

        // compare affiliate member id
        if (memberInSystem != null) {
            systemValue = memberInSystem.getAffMbrNumber();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.AFFILIATE_MEMBER_ID, member.getAffMbrNumber(), systemValue, false);

        // Rule 4. - Member record update date is recent (configurable).
        if (memberInSystem != null) {

            // Check if the person last update is more recent than the config number of days allowed,
            // then set the update error as Recent type
            if (memberInSystem.getPersonMstModifiedDate() != null) {
                long dayDiff = (System.currentTimeMillis() - memberInSystem.getPersonMstModifiedDate().getTime()) / 86400000;
                if (lastUpdateConfigTimeLimit!=null && dayDiff < new Integer(lastUpdateConfigTimeLimit).longValue()) {
                    eData.setUpdateErrorCodePk(Codes.UpdateFieldError.RECENT);
                    error = true;
                }
            }
            // Check if the member last update is more recent than the config number of days allowed,
            // then set the update error as Recent type
            if (memberInSystem.getMbrMstModifiedDate() != null) {
                long dayDiff = (System.currentTimeMillis() - memberInSystem.getMbrMstModifiedDate().getTime()) / 86400000;
                if (lastUpdateConfigTimeLimit!=null && dayDiff < new Integer(lastUpdateConfigTimeLimit).longValue()) {
                    eData.setUpdateErrorCodePk(Codes.UpdateFieldError.RECENT);
                    error = true;
                }
            }
        }

        // report errors
        if (error) {
            logger.debug("error inside==>" + error);
            summary.addException(member.getAffPk(), eData);
        }
        logger.debug("error outside==>" + error);
        return error;
    }

    /**
     * Checks for exceptions and updates the MemberUpdateSummary accordingly.
     *
     * @param member    The Member data in the file.
     * @param summary   The current preupdate summary information being generated.
     * @param memberInSystem    The Member data in the system (if exists).
     *
     * @return  Returns true if an error is found.
     *
     * Checks using the following criteria for member exceptions (record errors):
     *  1. each record must have Required fields which are First Name, Last Name and Local Number.
     *  2. each record must not have Member Zip that is all zeros.  Zeros indicate this is an unmailable address.
     *  3. each record must not have a SSN that is all zeros.
     *  4. member record has recently been updated.
     */
    private boolean performExceptionChecksForMember(MemberUpdateElement member, MemberUpdateSummary summary, MemberUpdateElement memberInSystem) {
        logger.debug("----------------------------------------------------");
        logger.debug("performExceptionChecksForMember(MemberUpdateElement) called.");
        logger.debug("member======>" + member);
        boolean error = false;
        ExceptionData eData = new ExceptionData(ExceptionData.MEMBER);
        eData.setFirstName(member.getFirstName());
        eData.setLastName(member.getLastName());
        eData.setMiddleName(member.getMiddleName());
        eData.setPersonPk(member.getPersonPk());
        eData.setSuffix(getCodeDescription("Suffix", member.getSuffixCodePk()));
        if (memberInSystem == null)
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.ADD);
        else
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.CHANGE);

        String systemValue = null;

        // compare prefix
        if (memberInSystem != null) {
            systemValue = getCodeDescription("Prefix", memberInSystem.getPrefixCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.PREFIX, getCodeDescription("Prefix", member.getPrefixCodePk()), systemValue, false);

        // compare first name
        if (memberInSystem != null) {
            systemValue = memberInSystem.getFirstName();
        }
        if (TextUtil.isEmptyOrSpaces(member.getFirstName())) {
            // Rule 1a. - each record must have Required fields which are First Name, Last Name and Local Number.
            logger.debug("member.getFirstName()===>" +member.getFirstName()+ " has errors");
            error = true;
            updateExceptionData(eData, Codes.MemberUpdateFields.FIRST_NAME, member.getFirstName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.MemberUpdateFields.FIRST_NAME, member.getFirstName(), systemValue, false);
        }

        // compare middle name
        if (memberInSystem != null) {
            systemValue = memberInSystem.getMiddleName();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.MIDDLE_NAME, member.getMiddleName(), systemValue, false);

        // compare last name
        if (memberInSystem != null) {
            systemValue = memberInSystem.getLastName();
        }
        if (TextUtil.isEmptyOrSpaces(member.getLastName())) {
            // Rule 1b. - each record must have Required fields which are First Name, Last Name and Local Number.
            logger.debug("member.getLastName()===>" +member.getLastName()+ " has errors");
            error = true;
            updateExceptionData(eData, Codes.MemberUpdateFields.LAST_NAME, member.getLastName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.MemberUpdateFields.LAST_NAME, member.getLastName(), systemValue, false);
        }

        // compare suffix
        if (memberInSystem != null) {
            systemValue = getCodeDescription("Suffix", memberInSystem.getSuffixCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.SUFFIX, getCodeDescription("Suffix", member.getSuffixCodePk()), systemValue, false);

        // compare addr1
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getAddr1();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.ADDR1, member.getPrimaryAddr().getAddr1(), systemValue, false);

        // compare addr2
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getAddr2();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.ADDR2, member.getPrimaryAddr().getAddr2(), systemValue, false);

        // compare city
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getCity();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.CITY, member.getPrimaryAddr().getCity(), systemValue, false);

        // compare state
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getState();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.STATE, member.getPrimaryAddr().getState(), systemValue, false);

        // compare zip
        if (memberInSystem != null) {
            systemValue = memberInSystem.getPrimaryAddr().getZipCode() + "-" + memberInSystem.getPrimaryAddr().getZipPlus();
        }
        if ((member.getPrimaryAddr().getZipCode().length() != 5) ||
            (TextUtil.isAllZeros(member.getPrimaryAddr().getZipCode())) ||
            (TextUtil.isEmptyOrSpaces(member.getPrimaryAddr().getZipCode()))) {
            // Rule 2. - Member Zip that is all zeros.  Zeros indicate this is an unmailable address.
            logger.debug("member.getPrimaryAddr().getZipCode().length()===>" +member.getPrimaryAddr().getZipCode().length()+ " has errors because of length");    
            error = true;
            updateExceptionData(eData, Codes.MemberUpdateFields.ZIP,
                                member.getPrimaryAddr().getZipCode() + "-" + member.getPrimaryAddr().getZipPlus(),
                                systemValue, true);
        } else {
            updateExceptionData(eData, Codes.MemberUpdateFields.ZIP,
                                member.getPrimaryAddr().getZipCode() + "-" + member.getPrimaryAddr().getZipPlus(),
                                systemValue, false);
        }

        // address is not required. only validate if we have one.
        PersonAddress address = toPersonAddress(member.getPrimaryAddr());
        if (address != null) {
            Set errors = smaBean.validate(address);
            logger.debug("address.getZipCode()==>" + address.getZipCode());;
            logger.debug("error inside  address==>" + error);
            logger.debug("errors for address==>" + errors);
            if (!CollectionUtil.isEmpty(errors)) {
                logger.debug("error inside==>" + error);
                error = true;
            }
        }

        // compare mailable address flag
        if (memberInSystem != null) {
            systemValue = Boolean.toString(memberInSystem.isMailableAddr());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.MAILABLE_ADDRESS,
                            Boolean.toString(member.isMailableAddr()),
                            systemValue, false);

        // compare no mail flag
        if (memberInSystem != null) {
            systemValue = Integer.toString(memberInSystem.getNoMailFlag());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.NO_MAIL,
                            Integer.toString(member.getNoMailFlag()), systemValue, false);

        // compare phone
        if (memberInSystem != null) {
            systemValue = memberInSystem.getAreaCode() + "-" + memberInSystem.getPhoneNumber();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.PHONE,
                            member.getAreaCode() + "-" + member.getPhoneNumber(),
                            systemValue, false);

        // compare member status
        if (memberInSystem != null) {
            systemValue = memberInSystem.getStatus();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.STATUS, member.getStatus(), systemValue, false);

        // compare gender
        if (memberInSystem != null) {
            systemValue = getCodeDescription("Gender", memberInSystem.getGenderCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.GENDER, getCodeDescription("Gender", member.getGenderCodePk()), systemValue, false);

        // compare date joined
        if (memberInSystem != null) {
            systemValue = DateUtil.getSimpleDateString(memberInSystem.getDateJoined());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.DATE_JOINED, DateUtil.getSimpleDateString(member.getDateJoined()), systemValue, false);

        // compare registered voter
        if (memberInSystem != null) {
            systemValue = getCodeDescription("RegisteredVoter", memberInSystem.getRegisteredVoterCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.REGISTERED_VOTER, getCodeDescription("RegisteredVoter", member.getRegisteredVoterCodePk()), systemValue, false);

        // compare political party
        if (memberInSystem != null) {
            systemValue = getCodeDescription("PoliticalParty", memberInSystem.getPoliticalPartyCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.POLITICAL_PARTY, getCodeDescription("PoliticalParty", member.getPoliticalPartyCodePk()), systemValue, false);

        // compare ssn
        if (memberInSystem != null) {
            systemValue = memberInSystem.getSsn();
        }
        if (isSSNValid(member.getSsn())) {
            updateExceptionData(eData, Codes.MemberUpdateFields.SSN, member.getSsn(), systemValue, false);
        } else {
            // Rule 3. - Member with a SSN that is all zeros.
            logger.debug("member.getSsn()===>" +member.getSsn()+ " has errors because isSSNValid(member.getSsn())" + isSSNValid(member.getSsn()));
            error = true;
            updateExceptionData(eData, Codes.MemberUpdateFields.SSN, member.getSsn(), systemValue, true);
        }

        // compare information source
        if (memberInSystem != null) {
            systemValue = getCodeDescription("InformationSource", memberInSystem.getInfoSourceCodePk());
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.INFORMATION_SOURCE, getCodeDescription("InformationSource", member.getInfoSourceCodePk()), systemValue, false);

        // compare affiliate member id
        if (memberInSystem != null) {
            systemValue = memberInSystem.getAffMbrNumber();
        }
        updateExceptionData(eData, Codes.MemberUpdateFields.AFFILIATE_MEMBER_ID, member.getAffMbrNumber(), systemValue, false);

        // Rule 4. - Member record update date is recent (configurable).
        if (memberInSystem != null) {
            logger.debug("memberInSystem===>" + memberInSystem);

            // Check if the person last update is more recent than the config number of days allowed,
            // then set the update error as Recent type
            if (memberInSystem.getPersonMstModifiedDate() != null) {
                long dayDiff = (System.currentTimeMillis() - memberInSystem.getPersonMstModifiedDate().getTime()) / 86400000;
                if (lastUpdateConfigTimeLimit!=null && dayDiff < new Integer(lastUpdateConfigTimeLimit).longValue()) {
                    eData.setUpdateErrorCodePk(Codes.UpdateFieldError.RECENT);
                    logger.debug("dayDiff  error===>" + dayDiff + "memberInSystem.getPersonMstModifiedDate()" + memberInSystem.getPersonMstModifiedDate());
                    error = true;
                }
            }
            // Check if the member last update is more recent than the config number of days allowed,
            // then set the update error as Recent type
            if (memberInSystem.getMbrMstModifiedDate() != null) {
                long dayDiff = (System.currentTimeMillis() - memberInSystem.getMbrMstModifiedDate().getTime()) / 86400000;
                if (lastUpdateConfigTimeLimit!=null && dayDiff < new Integer(lastUpdateConfigTimeLimit).longValue()) {
                    eData.setUpdateErrorCodePk(Codes.UpdateFieldError.RECENT);
                     logger.debug("dayDiff  error===>" + dayDiff + "memberInSystem.getMbrMstModifiedDate()" + memberInSystem.getMbrMstModifiedDate());

                    error = true;
                }
            }
        }

        // report errors
        if (error) {
            logger.debug("error inside==>" + error);
            summary.addException(member.getAffPk(), eData);
        }
        logger.debug("error outside==>" + error);
        return error;
    }
    
    /**
     * Checks for field changes and updates the UpdateSummary
     * accordingly. A field change is indicated by the return value.
     *
     * @param fMember   The Member data in the file.
     * @param sMember   The Member data in the system.
     * @param summary   The current preupdate summary information being generated.
     *
     * @return  Returns true if at least one field changes.
     */
    private boolean performFieldChangeChecksForMatchedMembers(MemberUpdateElement fMember, MemberUpdateElement sMember) {
        // compare prefix
        if (!TextUtil.equals(fMember.getPrefixCodePk(), sMember.getPrefixCodePk()))
            return true;

        // compare first name
        if (!TextUtil.equalsIgnoreCase(fMember.getFirstName(), sMember.getFirstName()))
            return true;

        // compare middle name
        if (!TextUtil.equalsIgnoreCase(fMember.getMiddleName(), sMember.getMiddleName()))
            return true;

        // compare last name
        if (!TextUtil.equalsIgnoreCase(fMember.getLastName(), sMember.getLastName()))
            return true;

        // compare suffix
        if (!TextUtil.equals(fMember.getSuffixCodePk(), sMember.getSuffixCodePk()))
            return true;

        // compare addr1
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getAddr1(), sMember.getPrimaryAddr().getAddr1()))
            return true;

        // compare addr2
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getAddr2(), sMember.getPrimaryAddr().getAddr2()))
            return true;

        // compare city
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getCity(), sMember.getPrimaryAddr().getCity()))
            return true;

        // compare state
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getState(), sMember.getPrimaryAddr().getState()))
            return true;

        // compare zip
        if ((!TextUtil.equals(fMember.getPrimaryAddr().getZipCode(), sMember.getPrimaryAddr().getZipCode())) ||
            (!TextUtil.equals(fMember.getPrimaryAddr().getZipPlus(), sMember.getPrimaryAddr().getZipPlus())))
            return true;

        // compare mailable address flag
        if (fMember.isMailableAddr() != sMember.isMailableAddr())
            return true;

        // compare no mail flag
        if (fMember.getNoMailFlag() != sMember.getNoMailFlag())
            return true;

        // compare phone
        if ((!TextUtil.equals(fMember.getAreaCode(), sMember.getAreaCode())) ||
            (!TextUtil.equals(fMember.getPhoneNumber(), sMember.getPhoneNumber())))
            return true;

        // compare member status
        if (!TextUtil.equals(fMember.getStatus(), sMember.getStatus()))
            return true;

        // compare gender
        if (!TextUtil.equals(fMember.getGenderCodePk(), sMember.getGenderCodePk()))
            return true;

        // compare date joined
        if (!TextUtil.equals(fMember.getDateJoined(), sMember.getDateJoined()))
            return true;

        // compare registered voter
        if (!TextUtil.equals(fMember.getRegisteredVoterCodePk(), sMember.getRegisteredVoterCodePk()))
            return true;

        // compare political party
        if (!TextUtil.equals(fMember.getPoliticalPartyCodePk(), sMember.getPoliticalPartyCodePk()))
            return true;

        // compare ssn
        if (!TextUtil.equals(fMember.getSsn(), sMember.getSsn()))
            return true;

        // compare information source
        if (!TextUtil.equals(fMember.getInfoSourceCodePk(), sMember.getInfoSourceCodePk()))
            return true;

        // compare affiliate member id
        if (!TextUtil.equals(fMember.getAffMbrNumber(), sMember.getAffMbrNumber()))
            return true;

        return false;
    }

    /**
     * Checks for field changes and updates the MemberPreUpdateSummary
     * accordingly. A field change is indicated by the return value.
     *
     * @param fMember   The Member data in the file.
     * @param sMember   The Member data in the system.
     * @param summary   The current preupdate summary information being generated.
     *
     * @return  Returns true if at least one field changes.
     */
    private boolean performFieldChangeChecksForMatchedMembers(MemberUpdateElement fMember, MemberUpdateElement sMember, MemberPreUpdateSummary summary) {

        boolean changed = false;

        // compare prefix
        if (!TextUtil.equals(fMember.getPrefixCodePk(), sMember.getPrefixCodePk())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.PREFIX)).incrementCount();
            logger.debug("Prefix change found.");
            logger.debug("Value in File:    " + fMember.getPrefixCodePk());
            logger.debug("Value in System:  " + sMember.getPrefixCodePk());
            changed = true;
        }

        // compare first name
        if (!TextUtil.equalsIgnoreCase(fMember.getFirstName(), sMember.getFirstName())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.FIRST_NAME)).incrementCount();
            logger.debug("First Name change found.");
            logger.debug("Value in File:    " + fMember.getFirstName());
            logger.debug("Value in System:  " + sMember.getFirstName());
            changed = true;
        }

        // compare middle name
        if (!TextUtil.equalsIgnoreCase(fMember.getMiddleName(), sMember.getMiddleName())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.MIDDLE_NAME)).incrementCount();
            logger.debug("Middle Name change found.");
            logger.debug("Value in File:    " + fMember.getMiddleName());
            logger.debug("Value in System:  " + sMember.getMiddleName());
            changed = true;
        }

        // compare last name
        if (!TextUtil.equalsIgnoreCase(fMember.getLastName(), sMember.getLastName())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.LAST_NAME)).incrementCount();
            logger.debug("Last Name change found.");
            logger.debug("Value in File:    " + fMember.getLastName());
            logger.debug("Value in System:  " + sMember.getLastName());
            changed = true;
        }

        // compare suffix
        if (!TextUtil.equals(fMember.getSuffixCodePk(), sMember.getSuffixCodePk())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.SUFFIX)).incrementCount();
            logger.debug("Suffix change found.");
            logger.debug("Value in File:    " + fMember.getSuffixCodePk());
            logger.debug("Value in System:  " + sMember.getSuffixCodePk());
            changed = true;
        }

        // compare addr1
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getAddr1(), sMember.getPrimaryAddr().getAddr1())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.ADDR1)).incrementCount();
            logger.debug("Address 1 change found.");
            logger.debug("Value in File:    " + fMember.getPrimaryAddr().getAddr1());
            logger.debug("Value in System:  " + sMember.getPrimaryAddr().getAddr1());
            changed = true;
        }

        // compare addr2
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getAddr2(), sMember.getPrimaryAddr().getAddr2())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.ADDR2)).incrementCount();
            logger.debug("Address 2 change found.");
            logger.debug("Value in File:    " + fMember.getPrimaryAddr().getAddr2());
            logger.debug("Value in System:  " + sMember.getPrimaryAddr().getAddr2());
            changed = true;
        }

        // compare city
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getCity(), sMember.getPrimaryAddr().getCity())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.CITY)).incrementCount();
            logger.debug("City change found.");
            logger.debug("Value in File:    " + fMember.getPrimaryAddr().getCity());
            logger.debug("Value in System:  " + sMember.getPrimaryAddr().getCity());
            changed = true;
        }

        // compare state
        if (!TextUtil.equalsIgnoreCase(fMember.getPrimaryAddr().getState(), sMember.getPrimaryAddr().getState())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.STATE)).incrementCount();
            logger.debug("State change found.");
            logger.debug("Value in File:    " + fMember.getPrimaryAddr().getState());
            logger.debug("Value in System:  " + sMember.getPrimaryAddr().getState());
            changed = true;
        }

        // compare zip
        if ((!TextUtil.equals(fMember.getPrimaryAddr().getZipCode(), sMember.getPrimaryAddr().getZipCode())) ||
            (!TextUtil.equals(fMember.getPrimaryAddr().getZipPlus(), sMember.getPrimaryAddr().getZipPlus()))
        ) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.ZIP)).incrementCount();
            logger.debug("Zip change found.");
            logger.debug("Value in File:    " + fMember.getPrimaryAddr().getZipCode() + "-" + fMember.getPrimaryAddr().getZipPlus());
            logger.debug("Value in System:  " + sMember.getPrimaryAddr().getZipCode() + "-" + sMember.getPrimaryAddr().getZipPlus());
            changed = true;
        }

        // compare mailable address flag
        if (fMember.isMailableAddr() != sMember.isMailableAddr()) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.MAILABLE_ADDRESS)).incrementCount();
            logger.debug("Mailable Address change found.");
            logger.debug("Value in File:    " + fMember.isMailableAddr());
            logger.debug("Value in System:  " + sMember.isMailableAddr());
            changed = true;
        }

        // compare no mail flag
        if (fMember.getNoMailFlag() != sMember.getNoMailFlag()) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.NO_MAIL)).incrementCount();
            logger.debug("No Mail change found.");
            logger.debug("Value in File:    " + fMember.getNoMailFlag());
            logger.debug("Value in System:  " + sMember.getNoMailFlag());
            changed = true;
        }

        // only check if phone in file is not all zeros and phone in system is not null
        if ((!fMember.getAreaCode().equals("000")) && (!fMember.getPhoneNumber().equals("0000000")) &&
            (sMember.getAreaCode() != null) && (sMember.getPhoneNumber() != null)) {

            // compare phone
            if ((!TextUtil.equals(fMember.getAreaCode(), sMember.getAreaCode())) ||
                (!TextUtil.equals(fMember.getPhoneNumber(), sMember.getPhoneNumber())) )
            {
                ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.PHONE)).incrementCount();
                logger.debug("Phone change found.");
                logger.debug("Value in File:    " + fMember.getAreaCode() + "-" + fMember.getPhoneNumber());
                logger.debug("Value in System:  " + sMember.getAreaCode() + "-" + sMember.getPhoneNumber());
                changed = true;
            }
        }

        // compare member status
        if (!TextUtil.equals(fMember.getStatus(), sMember.getStatus())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.STATUS)).incrementCount();
            logger.debug("Status change found.");
            logger.debug("Value in File:    " + fMember.getStatus());
            logger.debug("Value in System:  " + sMember.getStatus());
            changed = true;
        }

        // compare gender
        if (!TextUtil.equals(fMember.getGenderCodePk(), sMember.getGenderCodePk())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.GENDER)).incrementCount();
            logger.debug("Gender change found.");
            logger.debug("Value in File:    " + fMember.getGenderCodePk());
            logger.debug("Value in System:  " + sMember.getGenderCodePk());
            changed = true;
        }

        // compare date joined
        if (!TextUtil.equals(fMember.getDateJoined(), sMember.getDateJoined())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.DATE_JOINED)).incrementCount();
            logger.debug("Date Joined change found.");
            logger.debug("Value in File:    " + fMember.getDateJoined());
            logger.debug("Value in System:  " + sMember.getDateJoined());
            changed = true;
        }

        // compare registered voter
        if (!TextUtil.equals(fMember.getRegisteredVoterCodePk(), sMember.getRegisteredVoterCodePk())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.REGISTERED_VOTER)).incrementCount();
            logger.debug("Registered Voter change found.");
            logger.debug("Value in File:    " + fMember.getRegisteredVoterCodePk());
            logger.debug("Value in System:  " + sMember.getRegisteredVoterCodePk());
            changed = true;
        }

        // compare political party
        if (!TextUtil.equals(fMember.getPoliticalPartyCodePk(), sMember.getPoliticalPartyCodePk())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.POLITICAL_PARTY)).incrementCount();
            logger.debug("Political Party change found.");
            logger.debug("Value in File:    " + fMember.getPoliticalPartyCodePk());
            logger.debug("Value in System:  " + sMember.getPoliticalPartyCodePk());
            changed = true;
        }

        // compare ssn
        if (!TextUtil.equals(fMember.getSsn(), sMember.getSsn())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.SSN)).incrementCount();
            logger.debug("SSN change found.");
            logger.debug("Value in File:    " + fMember.getSsn());
            logger.debug("Value in System:  " + sMember.getSsn());
            changed = true;
        }

        // compare information source
        if (!TextUtil.equals(fMember.getInfoSourceCodePk(), sMember.getInfoSourceCodePk())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.INFORMATION_SOURCE)).incrementCount();
            logger.debug("Info Source change found.");
            logger.debug("Value in File:    " + fMember.getInfoSourceCodePk());
            logger.debug("Value in System:  " + sMember.getInfoSourceCodePk());
            changed = true;
        }

        // compare affiliate member id
        if (!TextUtil.equals(fMember.getAffMbrNumber(), sMember.getAffMbrNumber())) {
            ((FieldChange)summary.getFieldChanges().get(Codes.MemberUpdateFields.AFFILIATE_MEMBER_ID)).incrementCount();
            logger.debug("Affiliate Member Number change found.");
            logger.debug("Value in File:    " + fMember.getAffMbrNumber());
            logger.debug("Value in System:  " + sMember.getAffMbrNumber());
            changed = true;
        }
        return changed;
    }

    /**
     * Processes a matched Member update during the Pre-Update Summary process.
     *
     * @param resultSet The data for the potential member matched in the system
     * @param member The member in file attempting to match
     * @param memberInSystem The member in the system if matched
     * @param memberChanges The counts for member changes
     * @param preUpdateSummary The pre-update summary object to be filled.
     *
     * @return  true if no exception found, false if exception found
     *
     * Checks using the following criteria for member exceptions (record errors):
     *  1. check that Member cannot occur more than once in the same transmittal file for the same affiliate.
     */
    private boolean processMatchedMemberUpdateElement(ResultSet rs,
                                                   MemberUpdateElement member,
                                                   MemberUpdateElement memberInSystem,
                                                   MemberChanges memberChanges,
                                                   MemberPreUpdateSummary updateSummary)
    throws SQLException {

        // retrieve all system data
        memberInSystem = toMemberUpdateElement(rs);
        memberInSystem.setAffMbrNumber(member.getAffMbrNumber());
        memberInSystem.setAffPk(member.getAffPk());

        boolean changed = false;
        boolean errors = performExceptionChecksForMember(member, updateSummary, memberInSystem);

        // Rule 1. - check for duplicate member exception for update
        if (memberChanges.getMatchedMemberPks().contains(memberInSystem.getPersonPk())) {

            //add the duplicate exception with this record
            ExceptionData eData = new ExceptionData(ExceptionData.MEMBER);
            eData.setFirstName(member.getFirstName());
            eData.setLastName(member.getLastName());
            eData.setMiddleName(member.getMiddleName());
            eData.setPersonPk(member.getPersonPk());
            eData.setSuffix(getCodeDescription("Suffix", member.getSuffixCodePk()));
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
            updateSummary.addException(member.getAffPk(), eData);

            memberChanges.decrementChanged();
            errors = true;
        }

        if (!errors) {
            changed = performFieldChangeChecksForMatchedMembers(member, memberInSystem, updateSummary);
        }

        // update the summary
        if (changed && !errors) {
            memberChanges.incrementChanged();
        }

        // if member match, then add to list of ones not to inactivate (even if no chg or exception)
        if (!memberChanges.getMatchedMemberPks().contains(memberInSystem.getPersonPk())) {
            memberChanges.getMatchedMemberPks().add(memberInSystem.getPersonPk());
        }

        memberChanges.incrementMatchedAffMemberNumCount();

        //return the status of match (opposite of errors found)
        return !errors;
    }

    /**
     * Processes a matched Member update during the Update process.
     *
     * @param resultSet The data for the potential member matched in the system
     * @param member The member in file attempting to match
     * @param memberInSystem The member in the system if matched
     * @param tab The memberUpdateTabluation object with counts
     * @param updateSummary The update summary object to be filled.
     *
     * @return  true if no exception found, false if exception found
     *
     * Checks using the following criteria for member exceptions (record errors):
     *  1. check that Member cannot occur more than once in the same transmittal file for the same affiliate.
     */
    private boolean processMatchedMemberUpdateElement(ResultSet rs,
                                                   MemberUpdateElement member,
                                                   MemberUpdateElement memberInSystem,
                                                   MemberUpdateTabulation tab,
                                                   MemberUpdateSummary updateSummary)
    throws SQLException {

        PersonReviewData affReviewSummary = (PersonReviewData)updateSummary.getAffUpdateSummary().get(member.getAffPk());

        // retrieve all system data
        memberInSystem = toMemberUpdateElement(rs);
        memberInSystem.setAffMbrNumber(member.getAffMbrNumber());
        memberInSystem.setAffPk(member.getAffPk());

        boolean changed = false;
        boolean errors = performExceptionChecksForMember(member, updateSummary, memberInSystem);

        // Rule 1. - check for duplicate member exception for update
        if (tab.getUpdates().containsKey(memberInSystem.getPersonPk())) {

            MemberUpdateElement dup = (MemberUpdateElement) tab.getUpdates().remove(memberInSystem.getPersonPk());
            tab.getNotIncluded().add(dup);

            //add the duplicate exception with first record
            ExceptionData eData = new ExceptionData(ExceptionData.MEMBER);
            eData.setFirstName(dup.getFirstName());
            eData.setLastName(dup.getLastName());
            eData.setMiddleName(dup.getMiddleName());
            eData.setPersonPk(dup.getPersonPk());
            eData.setSuffix(getCodeDescription("Suffix", dup.getSuffixCodePk()));
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
            updateSummary.addException(dup.getAffPk(), eData);

            affReviewSummary.decrementTransAttempted();
            affReviewSummary.decrementChangesAttempted();

            //add the duplicate exception with this record
            eData = new ExceptionData(ExceptionData.MEMBER);
            eData.setFirstName(member.getFirstName());
            eData.setLastName(member.getLastName());
            eData.setMiddleName(member.getMiddleName());
            eData.setPersonPk(member.getPersonPk());
            eData.setSuffix(getCodeDescription("Suffix", member.getSuffixCodePk()));
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
            updateSummary.addException(member.getAffPk(), eData);

            tab.getNotIncluded().add(member);
            errors = true;
        }

        if (!errors) {
            changed = performFieldChangeChecksForMatchedMembers(member, memberInSystem);
        }

        // update
        if (changed && !errors) {
            if (!tab.getUpdates().containsKey(memberInSystem.getPersonPk())) {
                member.setPersonPk(memberInSystem.getPersonPk());
                member.setAddressPk(memberInSystem.getAddressPk());
                member.setPhonePk(memberInSystem.getPhonePk());
                tab.getUpdates().put(memberInSystem.getPersonPk(), member);
                affReviewSummary.incrementTransAttempted();
                affReviewSummary.incrementChangesAttempted();
            }
        }

        // if member match, then add to list of ones not to inactivate (even if no chg or exception)
        if (!tab.getMatchedMemberPks().contains(memberInSystem.getPersonPk())) {
            tab.getMatchedMemberPks().add(memberInSystem.getPersonPk());
        }

        //return the status of match (opposite of errors found)
        return !errors;
    }

    /**
     * Removes (or inactivates) a member from an affiliate.
     */
    private boolean removeMember(Integer userPk, Integer affPk, Integer personPk) {
        logger.debug("----------------------------------------------------");
        logger.debug("removeMember called.");

        logger.debug("Removing member with pk = " + personPk + "...");
        membersBean.inactivateMember(personPk, affPk, org.afscme.enterprise.codes.Codes.MemberStatus.I, userPk);
        logger.debug("... member removed");
        return true;
    }

    private void storeMemberUpdateSummary(Integer queuePk, MemberUpdateSummary summary) {
        logger.debug("----------------------------------------------------");
        logger.debug("storeMemberUpdateSummary called.");

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try {
            // store all top "AUP_Rv_Smry" entry
            ps = con.prepareStatement(SQL_INSERT_REVIEW_SUMMARY);
            Map.Entry entry;
            Integer affPk;
            PersonReviewData summaryData;
            for (Iterator it = summary.getAffUpdateSummary().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affPk = (Integer)entry.getKey();
                summaryData = (PersonReviewData)entry.getValue();

                ps.setInt(1, affPk.intValue());
                ps.setInt(2, queuePk.intValue());
                ps.setInt(3, summaryData.getTransAttempted());
                ps.setInt(4, summaryData.getTransCompleted());
                ps.setInt(5, summaryData.getTransError());
                ps.setInt(6, summaryData.getAddsAttempted());
                ps.setInt(7, summaryData.getAddsCompleted());
                ps.setInt(8, summaryData.getChangesAttempted());
                ps.setInt(9, summaryData.getChangesCompleted());
                ps.setInt(10, summaryData.getInacAttempted());
                ps.setInt(11, summaryData.getInacCompleted());
                ps.setInt(12, summaryData.getInacTCount());
                ps.setInt(13, summaryData.getVacantAttempted());
                ps.setInt(14, summaryData.getVacantCompleted());

                ps.addBatch();
            }
            ps.executeBatch();
            DBUtil.cleanup(null, ps, null);

            // save Exceptions to the AUP_Rv_Err_Smry and AUP_Rv_Err_Dtl tables
            ps = con.prepareStatement(SQL_INSERT_REVIEW_ERROR_SUMMARY_EXCEPTIONS);
            ps2 = con.prepareStatement(SQL_INSERT_REVIEW_ERROR_DETAIL_EXCEPTIONS);

            List exceptions = null;
            ExceptionData exData = null;
            ExceptionComparison compar = null;
            Integer fieldPk = null;
            int recordId = 1; /* Leave this line out here and not in the first loop.
                               * The recordId is an overall counter of the exceptions
                               * in the file.
                               **/
            for (Iterator it = summary.getExceptions().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affPk = (Integer)entry.getKey();
                exceptions = (List)entry.getValue();
                for (Iterator it2 = exceptions.iterator(); it2.hasNext(); recordId++) {
                    exData = (ExceptionData)it2.next();
                    ps.setInt(1, affPk.intValue());
                    ps.setInt(2, queuePk.intValue());
                    ps.setInt(3, recordId);
                    DBUtil.setNullableInt(ps, 4, exData.getPersonPk());
                    DBUtil.setNullableVarchar(ps, 5, exData.getLastName());
                    DBUtil.setNullableVarchar(ps, 6, exData.getMiddleName());
                    DBUtil.setNullableVarchar(ps, 7, exData.getFirstName());
                    DBUtil.setNullableVarchar(ps, 8, exData.getSuffix());
                    DBUtil.setNullableInt(ps, 9, exData.getUpdateErrorCodePk());
                    ps.addBatch();

                    for (Iterator it3 = exData.getFieldChangeDetails().entrySet().iterator(); it3.hasNext(); ) {
                        entry = (Map.Entry)it3.next();
                        fieldPk = (Integer)entry.getKey();
                        compar = (ExceptionComparison)entry.getValue();
                        ps2.setInt(1, affPk.intValue());
                        ps2.setInt(2, queuePk.intValue());
                        ps2.setInt(3, recordId);
                        ps2.setInt(4, fieldPk.intValue());
                        DBUtil.setNullableVarchar(ps2, 5, compar.getValueInSystem());
                        DBUtil.setNullableVarchar(ps2, 6, compar.getValueInFile());
                        DBUtil.setBooleanAsShort(ps2, 7, compar.getError());
                        ps2.addBatch();
                    }
                }
            }
            ps.executeBatch();
            ps2.executeBatch();
            logger.debug("...exceptions saved...");
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(null, ps, null);
            DBUtil.cleanup(con, ps2, null);
        }
    }

    private MemberUpdateElement toMemberUpdateElement(ResultSet rs) throws SQLException {
        MemberUpdateElement member = new MemberUpdateElement();
        AddressElement addr = new AddressElement();
        member.setLastName(rs.getString("last_nm"));
        member.setFirstName(rs.getString("first_nm"));
        member.setMiddleName(rs.getString("middle_nm"));
        member.setPrefixCodePk(DBUtil.getIntegerOrNull(rs, "prefix_nm"));
        member.setSuffixCodePk(DBUtil.getIntegerOrNull(rs, "suffix_nm"));
        addr.setAddr1(rs.getString("addr1"));
        addr.setAddr2(rs.getString("addr2"));
        addr.setCity(rs.getString("city"));
        addr.setState(rs.getString("state"));
        addr.setZipCode(rs.getString("zipcode"));
        addr.setZipPlus(rs.getString("zip_plus"));
        member.setPrimaryAddr(addr);
        member.setMailableAddr((rs.getBoolean("addr_bad_fg") ? false : true));
        // map no_XXX_fg fields to legacy noMail
        boolean noMailFg = DBUtil.getBooleanFromShort(rs.getShort("no_mail_fg")).booleanValue();
        boolean noCardsFg = DBUtil.getBooleanFromShort(rs.getShort("no_cards_fg")).booleanValue();
        boolean noPEMailFg = DBUtil.getBooleanFromShort(rs.getShort("no_public_emp_fg")).booleanValue();
        boolean noLegMailFg = DBUtil.getBooleanFromShort(rs.getShort("no_legislative_mail_fg")).booleanValue();
        member.setNoMailFlag(getLegacyNoMailCode(noMailFg, noCardsFg, noPEMailFg, noLegMailFg));
        member.setAreaCode(rs.getString("area_code"));
        member.setPhoneNumber(rs.getString("phone_no"));
        member.setGenderCodePk(DBUtil.getIntegerOrNull(rs, "gender"));
        member.setDateJoined(rs.getTimestamp("mbr_join_dt"));
        member.setRegisteredVoterCodePk(DBUtil.getIntegerOrNull(rs, "political_registered_voter"));
        member.setPoliticalPartyCodePk(DBUtil.getIntegerOrNull(rs, "political_party"));
        member.setSsn(rs.getString("ssn"));
        member.setInfoSourceCodePk(DBUtil.getIntegerOrNull(rs, "primary_information_source"));
        member.setPersonPk(DBUtil.getIntegerOrNull(rs, "person_pk"));
        member.setAddressPk(DBUtil.getIntegerOrNull(rs, "address_pk"));
        member.setPhonePk(DBUtil.getIntegerOrNull(rs, "phone_pk"));
        member.setAffMbrNumber(rs.getString("mbr_no_local"));
        // map status and type to the legacy status...
        int status = rs.getInt("mbr_status");
        int type = rs.getInt("mbr_type");
        member.setStatus(getLegacyStatus(status, type));
        member.setPersonMstModifiedDate(rs.getTimestamp("person_mst_lst_mod_dt"));
        member.setMbrMstModifiedDate(rs.getTimestamp("mbr_mst_lst_mod_dt"));
        return member;
    }

    /**
     * Updates the remaining Aff_Members data fields (not being updated with Member Bean method)
     *
     * @param userPk - User Pk of the user changing the data
     * @param personPk - Primary Pk of the Member
     * @param member - MemberUpdateElement object that contains member data to be updated
     */
    private void updateRemainingFields(Integer userPk, Integer personPk, MemberUpdateElement member) {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;

        boolean noMail = false;
        boolean noCards = false;
        boolean noLegMail = false;
        boolean noPEMail = false;
        switch (member.getNoMailFlag()) {
            case 0:// do nothing...
                break;
            case 1:
                noCards = true;
                break;
            case 2:
                noPEMail = true;
                break;
            case 3:
                noCards = true;
                noPEMail = true;
                break;
            case 9:
                noMail = true;
                noCards = true;
                noPEMail = true;
                noLegMail = true;
                break;
        }
        try {
            ps = con.prepareStatement(SQL_UPDATE_REMAINING_MEMBER);
            DBUtil.setBooleanAsShort(ps, 1, noCards);
            DBUtil.setBooleanAsShort(ps, 2, noMail);
            DBUtil.setBooleanAsShort(ps, 3, noLegMail);
            DBUtil.setBooleanAsShort(ps, 4, noPEMail);
            DBUtil.setNullableVarchar(ps, 5, member.getAffMbrNumber());
            DBUtil.setNullableInt(ps, 6, member.getInfoSourceCodePk());
            ps.setInt(7, userPk.intValue());
            ps.setInt(8, member.getAffPk().intValue());
            ps.setInt(9, personPk.intValue());
            ps.executeUpdate();
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /************************************************************************************************/
    /* **** Note: METHODS *** ABOVE *** HERE ARE ALL FOR THE    MEMBER    UPDATE PROCESSING!!       */
    /************************************************************************************************/

    
/*****************************************************************************************************************/

    
    /************************************************************************************************/
    /* **** Note: METHODS *** BELOW *** HERE ARE ALL FOR THE    REBATE    UPDATE PROCESSING!!       */
    /************************************************************************************************/
    
    /**
     * Gets the number of rebate records (persons/members) in an affiliate
     * that was produced on the same rebate update file.  This is used to
     * compare to the number of rebates received in the file and to determine
     * added or removed persons from the incoming rebate update file.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPks The set of affiliate Pks for all affiliates in the system below the hierarchy
     * @param preUpdateSummary  The pre-update summary object to be filled.
     */
    public void calculateRebateSentCounts(Set affPks, RebatePreUpdateSummary updateSummary) {

        logger.debug("----------------------------------------------------");
        logger.debug("calculateRebateSentCounts called.");

        // retrieve all rebate records (personPK) for the file generated date first.
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_COUNT_REBATES_FOR_AFFILIATE_PRE_ROSTER);

            int count = 0;
            Integer affPk = null;
            AffiliateData data = null;
            AffiliateIdentifier affId = null;
            for (Iterator it1 = affPks.iterator(); it1.hasNext(); ) {
                // get Affiliate info
                affPk = (Integer)it1.next();
                logger.debug("    affPk = " + affPk);
                data = affilBean.getAffiliateData(affPk);
                if (data == null) {
                    throw new EJBException("No affiliate found.");
                }
                affId = data.getAffiliateId();
                affId.setAdministrativeLegislativeCouncil(null);

                RebateChanges rebateChanges = (RebateChanges)updateSummary.getRebateCounts().get(affId);
                Timestamp fileGenDate = rebateChanges.getFileGeneratedDt();

                // if no file generated date, then no corresponding records in database sent
                if (fileGenDate != null) {

                    ps.setInt(1, affPk.intValue());
                    ps.setInt(2, getCurrentRebateYear());
                    ps.setInt(3, PRBRosterStatus.P.intValue());
                    ps.setTimestamp(4, fileGenDate);

                    rs = ps.executeQuery();

                    rs.next();
                    count = rs.getInt(1);
                }

                // update the "sent" count
                rebateChanges.setSent(count);
                logger.debug("    rebateChanges = " + rebateChanges);

                DBUtil.cleanup(null, null, rs);

            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

    /**
     * Performs the rebate updates into the database for the Update process
     *
     * @ejb:interface-method view-typ="lcoal"
     * @ejb:transaction type="Required"
     */
    public void doRebateUpdate(Integer userPk, Integer queuePk, RebateUpdateSummary updateSummary) {

        logger.debug("----------------------------------------------------");
        logger.debug("doRebateUpdate called.");

        Set updateErrors = new HashSet();

        // do rebate updates
        RebateUpdateElement rebate;
        boolean updateDone = false;
        for (Iterator it = updateSummary.getUpdates().values().iterator(); it.hasNext(); ) {
            rebate = (RebateUpdateElement)it.next();
            logger.debug("rebate: " + rebate);
            try {
                updateDone = updateRebate(userPk, rebate, updateErrors);
                if (updateDone) {
                    ((PersonReviewData)updateSummary.getAffUpdateSummary().get(rebate.getAffPk())).incrementTransCompleted();
                }
            } catch (EJBException e) {
                logger.debug("", e);
                /** @TODO: do something with the updateErrors. */
            }
            updateDone = false;
        }

        // store new update summary. This should be the last thing to do (after all other transactions)
        storeUpdateSummary(queuePk, updateSummary);
        //********************************************************************************************************************
        //System logging starts here event: Update Applied logging 
        //********************************************************************************************************************
        RebateReviewSummary    rSummary         =   getRebateReviewSummary(queuePk);
        PersonReviewData[]     rRSmry           =   rSummary.getRebateReviewData();
        PersonReviewData       tCount           =   rSummary.getTotals();
        ExceptionData[]        exceptions       =   rSummary.getExceptionResult();
        //logger.debug("current tCount==>" + updateSummary.getTotalCounts());
        //logger.debug("tCount==>" + tCount);
        if(tCount != null){
            SystemLog.logUpdateApplied(tCount.getAddsCompleted(), tCount.getChangesCompleted(), tCount.getInacCompleted(), userPk.toString());                
        }
        //******************************************************************************************        
        Map.Entry           parentEntry                 =   null;
        ArrayList           list                        =   null;
        Iterator            lt                          =   null;
        HashMap             map                         =   null;
        ExceptionData       eData                       =   null;
        
        //**************************************************************************
        //log if the summary has exceptions    
        //*****************************************************************************
        //logger.debug("exceptions=>" + exceptions.length);        
        if(exceptions != null) {
            for(int i=0; i< exceptions.length; i++){               
                    eData               =   (ExceptionData) exceptions[i];                    
                    Map eComp           =   eData.getFieldChangeDetails();                    
                    Iterator eCompIt    =   eComp.values().iterator();
                    
                    while(eCompIt.hasNext()){
                        ArrayList al            =   (ArrayList) eCompIt.next();
                        Iterator alIt           =   al.iterator();                        
                        
                        while(alIt.hasNext()){
                            ExceptionComparison ec = (ExceptionComparison) alIt.next();                            
                            if(ec.getError()){
                                logger.debug(" calling logRecordUpdateError===>" );
                                SystemLog.logRecordUpdateError(ec.getField(), new java.sql.Timestamp(new java.util.Date().getTime()), "System value different", userPk.toString());
                            }                        
                        }//end of innermost while
                        
                    }//end of inner while                                                        
                
            }//end of for loop
        }//end of if
        //***********************************************************************************
    }
   
    /**
     * For the first personPk for an affiliate, this method will return 
     * the file generated date for the preliminary roster and rebate update 
     * file it was produced on.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Timestamp getRebateFileGeneratedDate(Integer affPk, Integer personPk) {

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp fileDate = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_REBATE_FILE_GENERATED_DATE);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, getCurrentRebateYear());
            ps.setInt(3, PRBRosterStatus.P.intValue());
            ps.setInt(4, personPk.intValue());

            rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getTimestamp(1) != null) {
                    fileDate = new Timestamp(rs.getTimestamp(1).getTime());
                }    
            }
        }
        catch(SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return fileDate;
    }

    /**
     * Gets the personPk for rebate records (persons/members) in an affiliate
     * that was produced on the same rebate update file.  This is used to
     * compare to the personPks sent in the incoming rebate update file and 
     * to determine if anyone has been removed.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk The affiliate Pk to retrieve the personPks
     * @param fileGenDate The file generated date for this affiliate
     */
    public List getRebatePersons(Integer affPk, Timestamp fileGenDate) {

        logger.debug("----------------------------------------------------");
        logger.debug("getRebatePersons called.");

        // retrieve all rebate records (personPK) for the affiliate with file generated date.
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List rebatePersons = new ArrayList();

        try {
            
            ps = con.prepareStatement(SQL_SELECT_PERSON_REBATES_FOR_AFFILIATE_PRE_ROSTER);

            ps.setInt(1, affPk.intValue());
            ps.setInt(2, getCurrentRebateYear());
            ps.setInt(3, PRBRosterStatus.P.intValue());
            ps.setTimestamp(4, fileGenDate);

            rs = ps.executeQuery();

            while (rs.next()) {            
                rebatePersons.add(new Integer(rs.getInt("person_pk")));
            }
            
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return rebatePersons;
    }

    /**
     * Looks for a match for the given element during the Pre-Update Summary process.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param rebate The rebate to attempt to match
     * @param preUpdateSummary The pre-update summary object to be filled.
     */
    public void matchRebate(RebateUpdateElement rebate, RebatePreUpdateSummary updateSummary) {
        //*****************************************************************************************
        //timer to log the elapsed time
        java.sql.Timestamp startTime = new java.sql.Timestamp(new java.util.Date().getTime());
        //*******************************************************************************************
        RebateChanges rebateChanges = (RebateChanges)updateSummary.getRebateCounts().get(rebate.getAffId());
        Timestamp fileGenDate = rebateChanges.getFileGeneratedDt();
        StringBuffer sql = null;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exceptionFound = false;

        // Data Match Rule 1. - try to match the AFSCME Member Number
        try {
            boolean matchFound = false;
            String firstName = null;
            String lastName = null;
            RebateUpdateElement rebateInSystem = null;

            if ((rebate.getAfscmeMemberNumber() != null) && (rebate.getAfscmeMemberNumber().intValue() != 0)) {

                // increment count since valid afscme member number exists
                rebateChanges.incrementAfscmeMemberNumCountInFile();

                sql = new StringBuffer(SQL_REBATE_MEMBER_MATCH + SQL_WHERE_REBATE_MEMBER_MATCH_AFSCME_MEMBER_NUMBER);
                if (fileGenDate != null) {
                    sql.append(SQL_WHERE_MATCH_FILE_GENERATED_DATE);
                }
                ps = con.prepareStatement(sql.toString());
                ps.setInt(1, rebate.getAffPk().intValue());
                ps.setInt(2, rebate.getAfscmeMemberNumber().intValue());
                ps.setInt(3, getCurrentRebateYear());
                if (fileGenDate != null) {
                    ps.setTimestamp(4, fileGenDate);
                }

                rs = ps.executeQuery();

                while (rs.next()) {
                    if (processMatchedRebateUpdateElement(rs, rebate, rebateInSystem, rebateChanges, updateSummary)) {
                            matchFound = true;
                    }
                    else {
                        exceptionFound = true;
                    }
                    break;
                }
                DBUtil.cleanup(null, ps, rs);
            }

            if ((!matchFound) && (!exceptionFound)) {

                // Data Match Rule 2. - try to match on SSN
                sql = new StringBuffer(SQL_REBATE_MEMBER_MATCH + SQL_WHERE_REBATE_MEMBER_MATCH_SSN);
                if (fileGenDate != null) {
                    sql.append(SQL_WHERE_MATCH_FILE_GENERATED_DATE);
                }                
                ps = con.prepareStatement(sql.toString());
                ps.setInt(1, rebate.getAffPk().intValue());
                ps.setString(2, rebate.getSsn());
                ps.setInt(3, getCurrentRebateYear());
                if (fileGenDate != null) {
                    ps.setTimestamp(4, fileGenDate);
                }

                rs = ps.executeQuery();

                while (rs.next()) {
                    firstName = rs.getString("first_nm");
                    lastName = rs.getString("last_nm");

                    // Data Match Rule 2a. - if match SSN, try to confirm match on Name
                    if (TextUtil.equalsIgnoreCase(firstName, rebate.getFirstName()) &&
                        TextUtil.equalsIgnoreCase(lastName, rebate.getLastName()) )
                    {
                        if (processMatchedRebateUpdateElement(rs, rebate, rebateInSystem, rebateChanges, updateSummary)) {
                            matchFound = true;
                        }
                        else {
                            exceptionFound = true;
                        }
                        break;
                    }
                }
            }

            // if no match on afscme member number or ssn with name combo, then an add (affiliate error)
            if ((!matchFound) && (!exceptionFound)) {

                // set affiliate error flag
                rebateChanges.setHasError(true);

                // add exception for added rebate
                performExceptionChecksForRebate(rebate, updateSummary, null, rebateChanges);
            }

            // count the ones in the file received
            rebateChanges.incrementReceived();
            //*****************************************************************************************
            //log the matched and non matched count
            //*******************************************************************************************
                       
            java.sql.Timestamp endTime      = new java.sql.Timestamp(new java.util.Date().getTime());                
            String startSecs                = startTime.toString().substring(startTime.toString().indexOf(':' , 16)).substring(1);
            String endSecs                  = endTime.toString().substring(endTime.toString().indexOf(':' , 16)).substring(1);        
            double elapsedTime              = new Double((new Double(endSecs)).doubleValue() - (new Double(startSecs)).doubleValue()).doubleValue();        
            //if(fileEntry != null){  
                logger.debug("got inside of to log");
                RebateChanges tCount        =   updateSummary.getTotalCounts();  
                Set matchSet                =   tCount.getMatchedPersonPks();
                int matchCount              =   matchSet.size();
                int inFileCount             =   tCount.getAfscmeMemberNumCountInFile();
                int nonMatchCount           =   inFileCount - matchCount;
                SystemLog.logDataMatchingPerformed("1", matchCount, nonMatchCount, startTime, elapsedTime,"");//, fileEntry.getFileName());
            //}
            //*************************************************************************************************
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

    /**
     * Looks for a match for the given element during the Update process.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param rebate The rebate to attempt to match
     * @param updateSummary The update summary object to be filled.
     *
     * Checks using the following criteria for rebate exceptions (record errors):
     *  1. check that Rebate Member cannot occur more than once in the same transmittal file for the same affiliate.
     */
    public void matchRebate(RebateUpdateElement rebate, RebateUpdateSummary updateSummary) {

        PersonReviewData affReviewSummary = (PersonReviewData)updateSummary.getAffUpdateSummary().get(rebate.getAffPk());
        Timestamp fileGenDate = (Timestamp)updateSummary.getAffFileGenerated().get(rebate.getAffPk());
        StringBuffer sql = null;
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exceptionFound = false;

        // Data Match Rule 1. - try to match the AFSCME Member Number
        try {

            boolean matchFound = false;
            String firstName = null;
            String lastName = null;
            RebateUpdateElement rebateInSystem = null;

            if ((rebate.getAfscmeMemberNumber() != null) && (rebate.getAfscmeMemberNumber().intValue() != 0)) {

                // process if afscme member number exists
                sql = new StringBuffer(SQL_REBATE_MEMBER_MATCH + SQL_WHERE_REBATE_MEMBER_MATCH_AFSCME_MEMBER_NUMBER);
                ps = con.prepareStatement(sql.toString());
                ps.setInt(1, rebate.getAffPk().intValue());
                ps.setInt(2, rebate.getAfscmeMemberNumber().intValue());
                ps.setInt(3, getCurrentRebateYear());

                rs = ps.executeQuery();

                while (rs.next()) {
                    //if true then no exceptions found
                    if (processMatchedRebateUpdateElement(rs, rebate, rebateInSystem, updateSummary)) {
                        matchFound = true;
                    }
                    else {
                        exceptionFound = true;
                    }
                    break;
                }
            }

            DBUtil.cleanup(null, ps, rs);

            if ((!matchFound) && (!exceptionFound)) {

                // Data Match Rule 2 - try to match on SSN
                sql = new StringBuffer(SQL_REBATE_MEMBER_MATCH + SQL_WHERE_REBATE_MEMBER_MATCH_SSN);
                ps = con.prepareStatement(sql.toString());                
                ps.setInt(1, rebate.getAffPk().intValue());
                ps.setString(2, rebate.getSsn());
                ps.setInt(3, getCurrentRebateYear());

                rs = ps.executeQuery();

                while (rs.next()) {
                    firstName = rs.getString("first_nm");
                    lastName = rs.getString("last_nm");

                    // Data Match Rule 2a - if match SSN, try to confirm match on Name
                    if (TextUtil.equalsIgnoreCase(firstName, rebate.getFirstName()) &&
                        TextUtil.equalsIgnoreCase(lastName, rebate.getLastName())
                    ) {
                        //if true then no exceptions found
                        if (processMatchedRebateUpdateElement(rs, rebate, rebateInSystem, updateSummary)) {
                            matchFound = true;
                        }
                        else {
                            exceptionFound = true;
                        }
                        break;
                    }
                }
            }

            // if no match on afscme member number or ssn with name combo, then an add (rebate exception)
            if ((!matchFound) && (!exceptionFound)) {

                // add exception for added rebate
                performExceptionChecksForRebate(rebate, updateSummary, null);

                // increment transactions in error count
                affReviewSummary.incrementTransError();
            }

            // increment transactions attempted count
            affReviewSummary.incrementTransAttempted();

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
    }

    /**
     * Stores an RebatePreUpdateSummary
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void storePreUpdateSummary(Integer queuePk, RebatePreUpdateSummary summary) {

        logger.debug("----------------------------------------------------");
        logger.debug("storePreUpdateSummary(Integer, RebatePreUpdateSummary) called.");

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        Map.Entry entry = null;
        Integer affPk = null;

        try {
            // save Affiliates to AUP_Rebate_Pre_Upd_Smry table
            logger.debug("saving rebate counts...");
            ps = con.prepareStatement(SQL_INSERT_REBATE_PRE_UPDATE_SUMMARY);
            AffiliateIdentifier affId = null;
            RebateChanges rbtChanges = null;
            logger.debug("summary: " + summary);
            for (Iterator it = summary.getRebateCounts().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affId = (AffiliateIdentifier)entry.getKey();
                rbtChanges = (RebateChanges)entry.getValue();
                affPk = rbtChanges.getAffPk();
                logger.debug("    values: ");
                logger.debug("            affPk: " + affPk);
                logger.debug("            affId: " + affId);
                logger.debug("            stats: " + rbtChanges);

                ps.setInt(1, queuePk.intValue());
                DBUtil.setNullableInt(ps, 2, affPk);
                ps.setInt(3, rbtChanges.getSent());
                ps.setInt(4, rbtChanges.getReceived());
                ps.setInt(5, rbtChanges.getUnknown());
                ps.setInt(6, rbtChanges.getCouncil());
                ps.setInt(7, rbtChanges.getLocal());
                ps.setInt(8, rbtChanges.getUnchanged());

                DBUtil.setBooleanAsShort(ps, 9, rbtChanges.getHasWarning());
                DBUtil.setBooleanAsShort(ps, 10, rbtChanges.getHasError());

                // only fill in aff_err fields if affPk does not exist (not valid affiliate)
                if (affPk == null) {
                    DBUtil.setNullableVarchar(ps, 11, affId.getType().toString());
                    DBUtil.setNullableVarchar(ps, 12, affId.getLocal());
                    DBUtil.setNullableVarchar(ps, 13, affId.getSubUnit());
                    DBUtil.setNullableVarchar(ps, 14, affId.getCouncil());
                }
                else {
                    ps.setNull(11, Types.CHAR);
                    ps.setNull(12, Types.VARCHAR);
                    ps.setNull(13, Types.VARCHAR);
                    ps.setNull(14, Types.VARCHAR);
                }

                ps.addBatch();
            }
            ps.executeBatch();
            DBUtil.cleanup(null, ps, null);
            logger.debug("...rebate saved...");

            // save Exceptions to the AUP_Pre_Err_Smry and AUP_Pre_Err_Dtl tables
            logger.debug("saving exceptions...");
            ps = con.prepareStatement(SQL_INSERT_PRE_ERROR_SUMMARY_EXCEPTIONS);
            ps2 = con.prepareStatement(SQL_INSERT_PRE_ERROR_DETAIL_EXCEPTIONS);

            List exceptions = null;
            ExceptionData exData = null;
            Integer updateFieldPk = null;
            ExceptionComparison compar = null;
            int recordId = 1;  /* Leave this line out here and not in the first loop.
                               * The recordId is an overall counter of the exceptions
                               * in the file.
                               **/

            for (Iterator it = summary.getExceptions().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affPk = (Integer)entry.getKey();
                exceptions = (List)entry.getValue();
                for (Iterator it2 = exceptions.iterator(); it2.hasNext(); recordId++) {
                    exData = (ExceptionData)it2.next();
                    ps.setInt(1, affPk.intValue());
                    ps.setInt(2, queuePk.intValue());
                    ps.setInt(3, recordId);
                    DBUtil.setNullableInt(ps, 4, exData.getPersonPk());
                    DBUtil.setNullableVarchar(ps, 5, exData.getLastName());
                    DBUtil.setNullableVarchar(ps, 6, exData.getMiddleName());
                    DBUtil.setNullableVarchar(ps, 7, exData.getFirstName());
                    DBUtil.setNullableVarchar(ps, 8, exData.getSuffix());
                    DBUtil.setNullableInt(ps, 9, exData.getUpdateErrorCodePk());
                    ps.addBatch();

                    for (Iterator it3 = exData.getFieldChangeDetails().entrySet().iterator(); it3.hasNext(); ) {
                        entry = (Map.Entry)it3.next();
                        updateFieldPk = (Integer)entry.getKey();
                        compar = (ExceptionComparison)entry.getValue();
                        ps2.setInt(1, affPk.intValue());
                        ps2.setInt(2, queuePk.intValue());
                        ps2.setInt(3, recordId);
                        ps2.setInt(4, updateFieldPk.intValue());
                        DBUtil.setNullableVarchar(ps2, 5, compar.getValueInSystem());
                        DBUtil.setNullableVarchar(ps2, 6, compar.getValueInFile());
                        DBUtil.setBooleanAsShort(ps2, 7, compar.isError());
                        ps2.addBatch();
                    }
                }
            }
            ps.executeBatch();
            ps2.executeBatch();
            DBUtil.cleanup(null, ps, null);
            DBUtil.cleanup(null, ps2, null);
            logger.debug("...exceptions saved...");

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Updates a rebate.
     *
     * @ejb:interface-method view-typ="lcoal"
     * @ejb:transaction type="Required"
     *
     */
    public boolean updateRebate(Integer userPk, RebateUpdateElement rebate, Set totalErrors) {

        logger.debug("----------------------------------------------------");
        logger.debug("updateRebate called.");

        AffiliateData affData = affilBean.getAffiliateData(rebate.getAffPk());
        if (affData == null) {
            throw new EJBException("Rebate's Affiliate not found.");
        }

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;

        try {
            logger.debug("Updating Rebate...");
            ps = con.prepareStatement(SQL_UPDATE_REBATE_MATCH_PRB_ROSTER_PERSONS);

            DBUtil.setNullableInt(ps, 1, rebate.getMemberTypePk());
            DBUtil.setNullableInt(ps, 2, rebate.getMemberStatusPk());
            DBUtil.setNullableInt(ps, 3, rebate.getDuesTypePk());
            DBUtil.setNullableInt(ps, 4, rebate.getNumMonthsPk());
            DBUtil.setNullableInt(ps, 5, rebate.getAcceptanceCodePk());
            ps.setInt(6, userPk.intValue());
            ps.setInt(7, rebate.getAffPk().intValue());
            ps.setInt(8, rebate.getAfscmeMemberNumber().intValue());    //person_pk
            ps.setInt(9, getCurrentRebateYear());

            ps.executeUpdate();

            logger.debug("Update Rebate for aff_pk = " + rebate.getAffPk() +
                         " and person_pk = " + rebate.getAfscmeMemberNumber() + " successful! ");

        } catch (SQLException se) {
            throw new EJBException("Error updating Rebate record in updateBean.updateRebate()", se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }

        logger.debug("... Rebate updated");
        return true;
    }

    
    /**
     * @return int - The current rebate year
     */
    private int getCurrentRebateYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR)-1;
    }

    /**
     * Checks for exceptions and updates the RebatePreUpdateSummary accordingly.
     *
     * @param rebate    The Rebate data in the file.
     * @param summary   The current preupdate summary information being generated.
     * @param rebateInSystem    The Rebate data in the system (if exists).
     *
     * @return  Returns true if an error is found.
     *
     * Checks using the following criteria for rebate exceptions (record errors):
     *  1. each record must have Required fields which are First Name, Last Name and Local Number.
     *  2. each record must have not have an unknown member type.
     *  3. each record that has council or local accepted must have one of the valid member types.
     *  4a. each record that has council or local accepted must have member status filled.
     *  4b. each record that has council or local accepted must have dues type filled.
     *  4c. each record that has council or local accepted must have member type filled.
     *  5. each record must have a valid acceptance code
     */
    private boolean performExceptionChecksForRebate(RebateUpdateElement rebate,
                                                    RebatePreUpdateSummary summary,
                                                    RebateUpdateElement rebateInSystem,
                                                    RebateChanges rbtChanges) {

        boolean error = false;
        ExceptionData eData = new ExceptionData(ExceptionData.REBATE);
        eData.setFirstName(rebate.getFirstName());
        eData.setLastName(rebate.getLastName());
        eData.setMiddleName(rebate.getMiddleName());

        eData.setPersonPk(rebate.getAfscmeMemberNumber());
        if ((rebateInSystem != null) && (rebate.getAfscmeMemberNumber() != null) &&
            (rebate.getAfscmeMemberNumber().intValue() != rebateInSystem.getAfscmeMemberNumber().intValue())) {
            
                // if afscme mbr nbr in file does not match system, change id to system
                eData.setPersonPk(rebateInSystem.getAfscmeMemberNumber());
        }

        if (rebateInSystem == null) {

            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.ADD);

            // if adding an unmatched rebate, then create exception
            error = true;
        }
        else
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.CHANGE);

        String systemValue = null;

        // compare afscme member id
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getAfscmeMemberNumber().toString();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.AFSCME_MEMBER_ID,
                            ((rebate.getAfscmeMemberNumber() == null) ? "" : rebate.getAfscmeMemberNumber().toString()),
                            systemValue, false);

        // compare ssn
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getSsn();
        }

        // if add exception, then highlight SSN as error
        updateExceptionData(eData, Codes.RebateUpdateFields.SSN, rebate.getSsn(),
                            systemValue, ((rebateInSystem == null) ? true : false));

        // compare ssn duplicate flag
        if (rebateInSystem != null) {
            systemValue = Boolean.toString(rebateInSystem.getSsnDuplicate());
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.DUP_SSN,
                            Boolean.toString(rebate.getSsnDuplicate()),
                            systemValue, false);

        // compare first name
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getFirstName();
        }
        if (TextUtil.isEmptyOrSpaces(rebate.getFirstName())) {
            // Rule 1a. - each record must have Required fields which are First Name, Last Name and Local Number.
            error = true;
            updateExceptionData(eData, Codes.RebateUpdateFields.FIRST_NAME, rebate.getFirstName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.RebateUpdateFields.FIRST_NAME, rebate.getFirstName(), systemValue, false);
        }

        // compare middle name
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getMiddleName();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.MIDDLE_NAME, rebate.getMiddleName(), systemValue, false);

        // compare last name
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getLastName();
        }
        if (TextUtil.isEmptyOrSpaces(rebate.getLastName())) {
            // Rule 1b. - each record must have Required fields which are First Name, Last Name and Local Number.
            error = true;
            updateExceptionData(eData, Codes.RebateUpdateFields.LAST_NAME, rebate.getLastName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.RebateUpdateFields.LAST_NAME, rebate.getLastName(), systemValue, false);
        }

        // compare addr1
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getAddr1();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ADDR1, rebate.getPrimaryAddr().getAddr1(), systemValue, false);

        // compare addr2
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getAddr2();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ADDR2, rebate.getPrimaryAddr().getAddr2(), systemValue, false);

        // compare city
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getCity();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.CITY, rebate.getPrimaryAddr().getCity(), systemValue, false);

        // compare province
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getProvince();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.PROVINCE, rebate.getPrimaryAddr().getProvince(), systemValue, false);

        // compare state
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getState();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.STATE, rebate.getPrimaryAddr().getState(), systemValue, false);

        // compare zip
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getZipCode();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ZIP, rebate.getPrimaryAddr().getZipCode(), systemValue, false);

        // compare zip plus
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getZipPlus();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ZIP_PLUS, rebate.getPrimaryAddr().getZipPlus(), systemValue, false);

//need to add country

        // compare rebate member type
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_MEMBER_TYPE, rebateInSystem.getMemberTypePk());
        }
        if ((rebate.getMemberTypePk() == null) || (rebate.getMemberTypePk().intValue() == 0)) {
            // Rule 2. - each record must have not have an unknown member type.
            error = true;
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.UNKNOWN);
            updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_TYPE,
                                getCodeDescription(REBATE_MEMBER_TYPE,
                                rebate.getMemberTypePk()), systemValue, true);

            // increment local accepted count
            rbtChanges.incrementUnknown();

        } else {
            if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
                  TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) &&
                 (!( TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.REGULAR) ||
                     TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.RETIREE) ||
                     TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.UNION_SHOP) ))) {

                // Rule 3a. - each record that has council or local accepted must have one of the valid member types.
                error = true;
                eData.setUpdateErrorCodePk(Codes.UpdateFieldError.INVALID);
                updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_TYPE,
                                    getCodeDescription(REBATE_MEMBER_TYPE,
                                    rebate.getMemberTypePk()), systemValue, true);
            }
            else {
                updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_TYPE,
                                    getCodeDescription(REBATE_MEMBER_TYPE,
                                    rebate.getMemberTypePk()), systemValue, false);
            }
        }

        // compare rebate member status
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_MEMBER_STATUS, rebateInSystem.getMemberStatusPk());
        }
        if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
              TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) &&
             ((rebate.getMemberStatusPk() == null) || (rebate.getMemberStatusPk().intValue() == 0)) ) {

                // Rule 4a. - each record that has council or local accepted must have member status filled.
                error = true;
                eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
                updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_STATUS,
                                    getCodeDescription(REBATE_MEMBER_STATUS,
                                    rebate.getMemberStatusPk()), systemValue, true);
        }
        else {
            updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_STATUS,
                                getCodeDescription(REBATE_MEMBER_STATUS,
                                rebate.getMemberStatusPk()), systemValue, false);
        }

        // compare dues type
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(DUES_TYPE, rebateInSystem.getDuesTypePk());
        }
        if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
              TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) &&
             ((rebate.getDuesTypePk() == null) || (rebate.getDuesTypePk().intValue() == 0)) ) {

                // Rule 4b. - each record that has council or local accepted must have dues type filled.
                error = true;
                eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
                updateExceptionData(eData, Codes.RebateUpdateFields.DUES_TYPE,
                                    getCodeDescription(DUES_TYPE,
                                    rebate.getDuesTypePk()), systemValue, true);
        }
        else {
            updateExceptionData(eData, Codes.RebateUpdateFields.DUES_TYPE,
                                getCodeDescription(DUES_TYPE,
                                rebate.getDuesTypePk()), systemValue, false);
        }

        // compare number of months
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_DURATION, rebateInSystem.getNumMonthsPk());
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.NUM_MONTHS,
                            getCodeDescription(REBATE_DURATION,
                            rebate.getNumMonthsPk()), systemValue, false);

        // compare acceptance code
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_ACCEPTANCE_CODE, rebateInSystem.getAcceptanceCodePk());
        }
        if ((rebate.getAcceptanceCodePk() == null) || (rebate.getAcceptanceCodePk().intValue() == 0)) {

            // Rule 5. - each record must have a valid acceptance code.
            error = true;
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                rebate.getAcceptanceCodePk()), systemValue, true);

            // increment unchanged acceptance code count
            rbtChanges.incrementUnchanged();

        } else {
            if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
                  TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) ) {

                // increment council accepted count
                if (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED)) {
                    rbtChanges.incrementCouncil();
                }

                // increment local accepted count
                if (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) {
                    rbtChanges.incrementLocal();
                }

                if ( (rebate.getMemberTypePk() != null) &&
                     (!( TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.REGULAR) ||
                         TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.RETIREE) ||
                         TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.UNION_SHOP) )) ) {

                            // Rule 3b. - each record that has council or local accepted must have one of the valid member types.
                            error = true;
                            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.INVALID);
                            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                                rebate.getAcceptanceCodePk()), systemValue, true);
                }
                else if ( ((rebate.getMemberTypePk() == null) || (rebate.getMemberTypePk().intValue() == 0)) ||
                          ((rebate.getMemberStatusPk() == null) || (rebate.getMemberStatusPk().intValue() == 0)) ||
                          ((rebate.getDuesTypePk() == null) || (rebate.getDuesTypePk().intValue() == 0)) ) {

                            // Rule 4c. - each record that has council or local accepted must have type, status and dues type filled.
                            error = true;
                            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
                            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                                rebate.getAcceptanceCodePk()), systemValue, true);
                }
                else {
                            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                                rebate.getAcceptanceCodePk()), systemValue, false);
                }
            }
            else {
                updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                    getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                    rebate.getAcceptanceCodePk()), systemValue, false);
            }
        }

        // report errors
        if (error) {
            summary.addException(rebate.getAffPk(), eData);
        }
        return error;
    }

    /**
     * Checks for exceptions and updates the RebateUpdateSummary accordingly.
     *
     * @param rebate    The Rebate data in the file.
     * @param summary   The current update summary information being generated.
     * @param rebateInSystem    The Rebate data in the system (if exists).
     *
     * @return  Returns true if an error is found.
     *
     * Checks using the following criteria for rebate exceptions (record errors):
     *  1. each record must have Required fields which are First Name, Last Name and Local Number.
     *  2. each record must have not have an unknown member type.
     *  3. each record that has council or local accepted must have one of the valid member types.
     *  4a. each record that has council or local accepted must have member status filled.
     *  4b. each record that has council or local accepted must have dues type filled.
     *  4c. each record that has council or local accepted must have member type filled.
     *  5. each record must have a valid acceptance code
     */
    private boolean performExceptionChecksForRebate(RebateUpdateElement rebate,
                                                    RebateUpdateSummary summary,
                                                    RebateUpdateElement rebateInSystem) {

        boolean error = false;
        ExceptionData eData = new ExceptionData(ExceptionData.REBATE);
        eData.setFirstName(rebate.getFirstName());
        eData.setLastName(rebate.getLastName());
        eData.setMiddleName(rebate.getMiddleName());
        
        eData.setPersonPk(rebate.getAfscmeMemberNumber());
        if ((rebateInSystem != null) && (rebate.getAfscmeMemberNumber() != null) &&
            (rebate.getAfscmeMemberNumber().intValue() != rebateInSystem.getAfscmeMemberNumber().intValue())) {
            
                // if afscme mbr nbr in file does not match system, change id to system
                eData.setPersonPk(rebateInSystem.getAfscmeMemberNumber());
        }
        
        if (rebateInSystem == null) {

            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.ADD);

            // if adding an unmatched rebate, then create exception
            error = true;
        }
        else
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.CHANGE);

        String systemValue = null;

        // compare afscme member id
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getAfscmeMemberNumber().toString();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.AFSCME_MEMBER_ID,
                            ((rebate.getAfscmeMemberNumber() == null) ? "" : rebate.getAfscmeMemberNumber().toString()),
                            systemValue, false);

        // compare ssn
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getSsn();
        }

        // if add exception, then highlight SSN as error
        updateExceptionData(eData, Codes.RebateUpdateFields.SSN, rebate.getSsn(),
                            systemValue, ((rebateInSystem == null) ? true : false));

        // compare ssn duplicate flag
        if (rebateInSystem != null) {
            systemValue = Boolean.toString(rebateInSystem.getSsnDuplicate());
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.DUP_SSN,
                            Boolean.toString(rebate.getSsnDuplicate()),
                            systemValue, false);

        // compare first name
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getFirstName();
        }
        if (TextUtil.isEmptyOrSpaces(rebate.getFirstName())) {
            // Rule 1a. - each record must have Required fields which are First Name, Last Name and Local Number.
            error = true;
            updateExceptionData(eData, Codes.RebateUpdateFields.FIRST_NAME, rebate.getFirstName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.RebateUpdateFields.FIRST_NAME, rebate.getFirstName(), systemValue, false);
        }

        // compare middle name
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getMiddleName();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.MIDDLE_NAME, rebate.getMiddleName(), systemValue, false);

        // compare last name
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getLastName();
        }
        if (TextUtil.isEmptyOrSpaces(rebate.getLastName())) {
            // Rule 1b. - each record must have Required fields which are First Name, Last Name and Local Number.
            error = true;
            updateExceptionData(eData, Codes.RebateUpdateFields.LAST_NAME, rebate.getLastName(), systemValue, true);
        } else {
            updateExceptionData(eData, Codes.RebateUpdateFields.LAST_NAME, rebate.getLastName(), systemValue, false);
        }

        // compare addr1
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getAddr1();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ADDR1, rebate.getPrimaryAddr().getAddr1(), systemValue, false);

        // compare addr2
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getAddr2();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ADDR2, rebate.getPrimaryAddr().getAddr2(), systemValue, false);

        // compare city
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getCity();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.CITY, rebate.getPrimaryAddr().getCity(), systemValue, false);

        // compare province
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getProvince();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.PROVINCE, rebate.getPrimaryAddr().getProvince(), systemValue, false);

        // compare state
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getState();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.STATE, rebate.getPrimaryAddr().getState(), systemValue, false);

        // compare zip
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getZipCode();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ZIP, rebate.getPrimaryAddr().getZipCode(), systemValue, false);

        // compare zip plus
        if (rebateInSystem != null) {
            systemValue = rebateInSystem.getPrimaryAddr().getZipPlus();
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.ZIP_PLUS, rebate.getPrimaryAddr().getZipPlus(), systemValue, false);

//need to add country

        // compare rebate member type
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_MEMBER_TYPE, rebateInSystem.getMemberTypePk());
        }
        if ((rebate.getMemberTypePk() == null) || (rebate.getMemberTypePk().intValue() == 0)) {
            // Rule 2. - each record must have not have an unknown member type.
            error = true;
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.UNKNOWN);
            updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_TYPE,
                                getCodeDescription(REBATE_MEMBER_TYPE,
                                rebate.getMemberTypePk()), systemValue, true);
        } else {
            if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
                  TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) &&
                 (!( TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.REGULAR) ||
                     TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.RETIREE) ||
                     TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.UNION_SHOP) ))) {

                // Rule 3a. - each record that has council or local accepted must have one of the valid member types.
                error = true;
                eData.setUpdateErrorCodePk(Codes.UpdateFieldError.INVALID);
                updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_TYPE,
                                    getCodeDescription(REBATE_MEMBER_TYPE,
                                    rebate.getMemberTypePk()), systemValue, true);
            }
            else {
                updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_TYPE,
                                    getCodeDescription(REBATE_MEMBER_TYPE,
                                    rebate.getMemberTypePk()), systemValue, false);
            }
        }

        // compare rebate member status
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_MEMBER_STATUS, rebateInSystem.getMemberStatusPk());
        }
        if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
              TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) &&
             ((rebate.getMemberStatusPk() == null) || (rebate.getMemberStatusPk().intValue() == 0)) ) {

                // Rule 4a. - each record that has council or local accepted must have member status filled.
                error = true;
                eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
                updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_STATUS,
                                    getCodeDescription(REBATE_MEMBER_STATUS,
                                    rebate.getMemberStatusPk()), systemValue, true);
        }
        else {
            updateExceptionData(eData, Codes.RebateUpdateFields.MEMBER_STATUS,
                                getCodeDescription(REBATE_MEMBER_STATUS,
                                rebate.getMemberStatusPk()), systemValue, false);
        }

        // compare dues type
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(DUES_TYPE, rebateInSystem.getDuesTypePk());
        }
        if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
              TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) &&
             ((rebate.getDuesTypePk() == null) || (rebate.getDuesTypePk().intValue() == 0)) ) {

                // Rule 4b. - each record that has council or local accepted must have dues type filled.
                error = true;
                eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
                updateExceptionData(eData, Codes.RebateUpdateFields.DUES_TYPE,
                                    getCodeDescription(DUES_TYPE,
                                    rebate.getDuesTypePk()), systemValue, true);
        }
        else {
            updateExceptionData(eData, Codes.RebateUpdateFields.DUES_TYPE,
                                getCodeDescription(DUES_TYPE,
                                rebate.getDuesTypePk()), systemValue, false);
        }

        // compare number of months
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_DURATION, rebateInSystem.getNumMonthsPk());
        }
        updateExceptionData(eData, Codes.RebateUpdateFields.NUM_MONTHS,
                            getCodeDescription(REBATE_DURATION,
                            rebate.getNumMonthsPk()), systemValue, false);

        // compare acceptance code
        if (rebateInSystem != null) {
            systemValue = getCodeDescription(REBATE_ACCEPTANCE_CODE, rebateInSystem.getAcceptanceCodePk());
        }
        if ((rebate.getAcceptanceCodePk() == null) || (rebate.getAcceptanceCodePk().intValue() == 0)) {

            // Rule 5. - each record must have a valid acceptance code.
            error = true;
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                rebate.getAcceptanceCodePk()), systemValue, true);
        } else {
            if ( (TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.COUNCIL_ACCEPTED) ||
                  TextUtil.equals(rebate.getAcceptanceCodePk(), Codes.RebateAcceptanceCode.LOCAL_ACCEPTED)) ) {

                if ( (rebate.getMemberTypePk() != null) &&
                     (!( TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.REGULAR) ||
                         TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.RETIREE) ||
                         TextUtil.equals(rebate.getMemberTypePk(), Codes.RebateMbrType.UNION_SHOP) )) ) {

                            // Rule 3b. - each record that has council or local accepted must have one of the valid member types.
                            error = true;
                            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.INVALID);
                            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                                rebate.getAcceptanceCodePk()), systemValue, true);
                }
                else if ( ((rebate.getMemberTypePk() == null) || (rebate.getMemberTypePk().intValue() == 0)) ||
                          ((rebate.getMemberStatusPk() == null) || (rebate.getMemberStatusPk().intValue() == 0)) ||
                          ((rebate.getDuesTypePk() == null) || (rebate.getDuesTypePk().intValue() == 0)) ) {

                            // Rule 4c. - each record that has council or local accepted must have type, status and dues type filled.
                            error = true;
                            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.REQUIRED);
                            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                                rebate.getAcceptanceCodePk()), systemValue, true);
                }
                else {
                            updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                                getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                                rebate.getAcceptanceCodePk()), systemValue, false);
                }
            }
            else {
                updateExceptionData(eData, Codes.RebateUpdateFields.ACCEPTANCE_CODE,
                                    getCodeDescription(REBATE_ACCEPTANCE_CODE,
                                    rebate.getAcceptanceCodePk()), systemValue, false);
            }
        }

        // report errors
        if (error) {
            summary.addException(rebate.getAffPk(), eData);
        }
        return error;
    }

    /**
     * Checks for field changes and updates the MemberPreUpdateSummary
     * accordingly. A field change is indicated by the return value.
     *
     * @param fRebate   The Member data in the file.
     * @param sRebate   The Member data in the system.
     * @param summary   The current preupdate summary information being generated.
     *
     * @return  Returns true if at least one field changes.
     */
    private boolean performFieldChangeChecksForMatchedRebates(RebateUpdateElement fRebate,
                                                              RebateUpdateElement sRebate) {

        // compare rebate member type
        if (!TextUtil.equals(fRebate.getMemberTypePk(), sRebate.getMemberTypePk()))
            return true;

        // compare rebate member status
        if (!TextUtil.equals(fRebate.getMemberStatusPk(), sRebate.getMemberStatusPk()))
            return true;

        // compare dues type
        if (!TextUtil.equals(fRebate.getDuesTypePk(), sRebate.getDuesTypePk()))
            return true;

        // compare number of months
        if (!TextUtil.equals(fRebate.getNumMonthsPk(), sRebate.getNumMonthsPk()))
            return true;

        // compare acceptance code
        if (!TextUtil.equals(fRebate.getAcceptanceCodePk(), sRebate.getAcceptanceCodePk()))
            return true;

        return false;
    }

    /**
     * Processes a matched Rebate update during the Pre-Update Summary process.
     *
     * @param resultSet The data for the potential rebate matched in the system
     * @param rebate The rebate in file attempting to match
     * @param rebateInSystem The rebate in the system if matched
     * @param rebateChanges The counts for rebate changes
     * @param preUpdateSummary The pre-update summary object to be filled.
     *
     * @return  true if no exception found, false if exception found
     *
     * Checks using the following criteria for rebate exceptions (record errors):
     *  1. check that Member cannot occur more than once in the same transmittal file for the same affiliate.
     */
    private boolean processMatchedRebateUpdateElement(ResultSet rs,
                                                      RebateUpdateElement rebate,
                                                      RebateUpdateElement rebateInSystem,
                                                      RebateChanges rebateChanges,
                                                      RebatePreUpdateSummary updateSummary)
    throws SQLException {

        // retrieve all system data
        rebateInSystem = toRebateUpdateElement(rs);
        rebateInSystem.setAffPk(rebate.getAffPk());

        boolean changed = false;
        boolean errors = performExceptionChecksForRebate(rebate, updateSummary, rebateInSystem, rebateChanges);

        // Rule 1. - check for duplicate rebate exception for update
        if (rebateChanges.getMatchedPersonPks().contains(rebateInSystem.getAfscmeMemberNumber())) {

            //add the duplicate exception with this record
            ExceptionData eData = new ExceptionData(ExceptionData.REBATE);
            eData.setFirstName(rebate.getFirstName());
            eData.setLastName(rebate.getLastName());
            eData.setMiddleName(rebate.getMiddleName());
            eData.setPersonPk(rebate.getAfscmeMemberNumber());
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
            updateSummary.addException(rebate.getAffPk(), eData);

            errors = true;
        }

        if (!errors) {
            changed = performFieldChangeChecksForMatchedRebates(rebate, rebateInSystem);
        }

        // if rebate match, then add to list to use for duplicate check
        if (!rebateChanges.getMatchedPersonPks().contains(rebateInSystem.getAfscmeMemberNumber())) {
            rebateChanges.getMatchedPersonPks().add(rebateInSystem.getAfscmeMemberNumber());
        }

        //return the status of match (opposite of errors found)
        return !errors;
    }

    /**
     * Processes a matched Rebate update during the Update process.
     *
     * @param resultSet The data for the potential rebate matched in the system
     * @param rebate The rebate in file attempting to match
     * @param rebateInSystem The rebate in the system if matched
     * @param updateSummary The update summary object to be filled.
     *
     * @return  true if no exception found, false if exception found
     *
     * Checks using the following criteria for rebate exceptions (record errors):
     *  1. check that Member cannot occur more than once in the same transmittal file for the same affiliate.
     */
    private boolean processMatchedRebateUpdateElement(ResultSet rs,
                                                      RebateUpdateElement rebate,
                                                      RebateUpdateElement rebateInSystem,
                                                      RebateUpdateSummary updateSummary)
    throws SQLException {

        PersonReviewData affReviewSummary = (PersonReviewData)updateSummary.getAffUpdateSummary().get(rebate.getAffPk());

        // retrieve all system data
        rebateInSystem = toRebateUpdateElement(rs);
        rebateInSystem.setAffPk(rebate.getAffPk());

        boolean changed = false;
        boolean errors = performExceptionChecksForRebate(rebate, updateSummary, rebateInSystem);

        // Rule 1. - check for duplicate rebate exception for update
        if (updateSummary.getUpdates().containsKey(rebateInSystem.getAfscmeMemberNumber())) {

            RebateUpdateElement dup = (RebateUpdateElement) updateSummary.getUpdates().remove(rebateInSystem.getAfscmeMemberNumber());

            //add the duplicate exception with first record
            ExceptionData eData = new ExceptionData(ExceptionData.REBATE);
            eData.setFirstName(dup.getFirstName());
            eData.setLastName(dup.getLastName());
            eData.setMiddleName(dup.getMiddleName());
            eData.setPersonPk(dup.getAfscmeMemberNumber());
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
            updateSummary.addException(dup.getAffPk(), eData);

            //add the duplicate exception with this record
            eData = new ExceptionData(ExceptionData.REBATE);
            eData.setFirstName(rebate.getFirstName());
            eData.setLastName(rebate.getLastName());
            eData.setMiddleName(rebate.getMiddleName());
            eData.setPersonPk(rebate.getAfscmeMemberNumber());
            eData.setUpdateErrorCodePk(Codes.UpdateFieldError.DUPLICATE);
            updateSummary.addException(rebate.getAffPk(), eData);

            errors = true;
        }

        if (!errors) {
            changed = performFieldChangeChecksForMatchedRebates(rebate, rebateInSystem);
        }

        // update
        if (changed && !errors) {
            if (!updateSummary.getUpdates().containsKey(rebateInSystem.getAfscmeMemberNumber())) {
                rebate.setAfscmeMemberNumber(rebateInSystem.getAfscmeMemberNumber());
                updateSummary.getUpdates().put(rebateInSystem.getAfscmeMemberNumber(), rebate);
            }
        }

        if (errors) {
            // increment transactions in error count
            affReviewSummary.incrementTransError();
        }

        //return the status of match (opposite of errors found)
        return !errors;
    }

    /**
     * Stores an UpdateSummary
     *
     */
    private void storeRebateUpdateSummary(Integer queuePk, RebateUpdateSummary summary) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("storeRebateUpdateSummary called.");

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try {
            // store all top "AUP_Rv_Smry" entry
            ps = con.prepareStatement(SQL_INSERT_REVIEW_SUMMARY);
            Map.Entry entry;
            Integer affPk;
            PersonReviewData summaryData;
            for (Iterator it = summary.getAffUpdateSummary().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affPk = (Integer)entry.getKey();
                summaryData = (PersonReviewData)entry.getValue();

                ps.setInt(1, affPk.intValue());
                ps.setInt(2, queuePk.intValue());
                ps.setInt(3, summaryData.getTransAttempted());
                ps.setInt(4, summaryData.getTransCompleted());
                ps.setInt(5, summaryData.getTransError());
                ps.setInt(6, summaryData.getAddsAttempted());
                ps.setInt(7, summaryData.getAddsCompleted());
                ps.setInt(8, summaryData.getChangesAttempted());
                ps.setInt(9, summaryData.getChangesCompleted());
                ps.setInt(10, summaryData.getInacAttempted());
                ps.setInt(11, summaryData.getInacCompleted());
                ps.setInt(12, summaryData.getInacTCount());
                ps.setInt(13, summaryData.getVacantAttempted());
                ps.setInt(14, summaryData.getVacantCompleted());

                ps.addBatch();
            }
            ps.executeBatch();
            DBUtil.cleanup(null, ps, null);

            // save Exceptions to the AUP_Rv_Err_Smry and AUP_Rv_Err_Dtl tables
            ps = con.prepareStatement(SQL_INSERT_REVIEW_ERROR_SUMMARY_EXCEPTIONS);
            ps2 = con.prepareStatement(SQL_INSERT_REVIEW_ERROR_DETAIL_EXCEPTIONS);

            List exceptions = null;
            ExceptionData exData = null;
            ExceptionComparison compar = null;
            Integer fieldPk = null;
            int recordId = 1; /* Leave this line out here and not in the first loop.
                               * The recordId is an overall counter of the exceptions
                               * in the file.
                               **/
            for (Iterator it = summary.getExceptions().entrySet().iterator(); it.hasNext(); ) {
                entry = (Map.Entry)it.next();
                affPk = (Integer)entry.getKey();
                exceptions = (List)entry.getValue();
                for (Iterator it2 = exceptions.iterator(); it2.hasNext(); recordId++) {
                    exData = (ExceptionData)it2.next();
                    ps.setInt(1, affPk.intValue());
                    ps.setInt(2, queuePk.intValue());
                    ps.setInt(3, recordId);
                    DBUtil.setNullableInt(ps, 4, exData.getPersonPk());
                    DBUtil.setNullableVarchar(ps, 5, exData.getLastName());
                    DBUtil.setNullableVarchar(ps, 6, exData.getMiddleName());
                    DBUtil.setNullableVarchar(ps, 7, exData.getFirstName());
                    DBUtil.setNullableVarchar(ps, 8, exData.getSuffix());
                    DBUtil.setNullableInt(ps, 9, exData.getUpdateErrorCodePk());
                    ps.addBatch();

                    for (Iterator it3 = exData.getFieldChangeDetails().entrySet().iterator(); it3.hasNext(); ) {
                        entry = (Map.Entry)it3.next();
                        fieldPk = (Integer)entry.getKey();
                        compar = (ExceptionComparison)entry.getValue();
                        ps2.setInt(1, affPk.intValue());
                        ps2.setInt(2, queuePk.intValue());
                        ps2.setInt(3, recordId);
                        ps2.setInt(4, fieldPk.intValue());
                        DBUtil.setNullableVarchar(ps2, 5, compar.getValueInSystem());
                        DBUtil.setNullableVarchar(ps2, 6, compar.getValueInFile());
                        DBUtil.setBooleanAsShort(ps2, 7, compar.getError());
                        ps2.addBatch();
                    }
                }
            }
            ps.executeBatch();
            ps2.executeBatch();
            logger.debug("...exceptions saved...");
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(null, ps, null);
            DBUtil.cleanup(con, ps2, null);
        }
    }

    /**
     * Copies data from result set to RebateUpdateElement object.
     *
     * @param resultSet The result set data for the potential rebate matched in the system
     * @return RebateUpdateElement object filled in
     */
    private RebateUpdateElement toRebateUpdateElement(ResultSet rs) throws SQLException {

        RebateUpdateElement rebate = new RebateUpdateElement();
        AddressElement addr = new AddressElement();

        rebate.setAfscmeMemberNumber(new Integer(rs.getInt("person_pk")));
        rebate.setSsn(rs.getString("ssn"));
        rebate.setSsnDuplicate((rs.getBoolean("duplicate_ssn_fg") ? false : true));

        rebate.setLastName(rs.getString("last_nm"));
        rebate.setFirstName(rs.getString("first_nm"));
        rebate.setMiddleName(rs.getString("middle_nm"));

        addr.setAddr1(rs.getString("addr1"));
        addr.setAddr2(rs.getString("addr2"));
        addr.setCity(rs.getString("city"));
        addr.setProvince(rs.getString("province"));
        addr.setState(rs.getString("state"));
        addr.setZipCode(rs.getString("zipcode"));
        addr.setZipPlus(rs.getString("zip_plus"));
//get country
        rebate.setPrimaryAddr(addr);
        rebate.setAddressPk(DBUtil.getIntegerOrNull(rs, "address_pk"));

        rebate.setMemberTypePk(DBUtil.getIntegerOrNull(rs, "rebate_year_mbr_type"));
        rebate.setMemberStatusPk(DBUtil.getIntegerOrNull(rs, "rebate_year_mbr_status"));
        rebate.setDuesTypePk(DBUtil.getIntegerOrNull(rs, "rebate_year_mbr_dues_rate"));
        rebate.setNumMonthsPk(DBUtil.getIntegerOrNull(rs, "roster_duration_in_aff"));
        rebate.setAcceptanceCodePk(DBUtil.getIntegerOrNull(rs, "roster_acceptance_cd"));

        return rebate;
    }

    /************************************************************************************************/
    /* **** Note: METHODS *** ABOVE *** HERE ARE ALL FOR THE    REBATE    UPDATE PROCESSING!!       */
    /************************************************************************************************/

    
/*****************************************************************************************************************/

    
    /************************************************************************************************/
    /* **** Note: METHODS *** BELOW *** HERE ARE ALL FOR THE    OFFICER    UPDATE PROCESSING!!      */
    /************************************************************************************************/

    /**
     * Gets the person pks of the officer's for an affiliate.  This is used to determine
     *  if persons are officers during an update.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getAffiliateOfficers(Integer affPk) {
        /*
        Set officersInSystem = new HashSet();

        Connection con = DBUtil.getConnection();
        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement(SQL_SELECT_ALL_OFFICERS_FOR_AFFILIATE);
            ps.setInt(1, affPk.intValue());

            rs = ps.executeQuery();

            while (rs.next())
                membersInSystem.add(new Integer(rs.getInt(1)));

        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return membersInSystem;
         */
        return null;
    }

    /**
     * Gets a Map of OfficerElement objects for the given affiliate, by person primary
     * key.  This is used by the edit summary generation and update application.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void getOfficerElements(Integer affPk) {

    }

    /**
     * Gets a list of position changes for a specific update, and affiliate.
     */
    public List getPositionChanges(Integer queuePk, Integer affPk) {
        return null;
    }

    /**
     * Looks for a match for the given element.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param OfficerUpdateElement
     * @return personPk
     */
    public int matchOfficer(OfficerUpdateElement officer) {
        
        Connection conn          =   null;
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;
        int personPk            =   0;
        
        if(officer.getAfscmeMemberNumber() != null)
        {
            if (personsBean.getPersonBaseOnPk(officer.getAfscmeMemberNumber().intValue()))                
                return officer.getAfscmeMemberNumber().intValue();

        }else if((officer.getAffiliateMemberNumber() != null && officer.getAffiliateMemberNumber().length() > 0) &&                      
                 (officer.getFirstName() != null && officer.getFirstName().length() > 0 ) && 
                 (officer.getLastName() != null && officer.getLastName().length() > 0))
        {
            conn = DBUtil.getConnection();
            try {
                ps = conn.prepareStatement(SQL_SELECT_OFFICER_ON_AFF_MBR_NUM_FNAM_LNAME);

                ps.setString(1, officer.getAffiliateMemberNumber());
                ps.setString(2, officer.getFirstName());
                ps.setString(3, officer.getLastName());

                rs = ps.executeQuery();

                if (rs.next()){ 
                    personPk = rs.getInt(1);
                }

            }
            catch (SQLException se) {
                throw new EJBException(se);
            }
            finally {
                DBUtil.cleanup(conn, ps, rs);
            }        
            return personPk;

        }else if((officer.getSsn() != null && officer.getSsn().length() > 0) && 
                 (officer.getFirstName() != null && officer.getFirstName().length() > 0 ) && 
                 (officer.getLastName() != null && officer.getLastName().length() > 0))
        {
            return personsBean.getPersonBaseOnSsnFnmLnm(officer.getSsn(), officer.getFirstName(), officer.getLastName());
        }                

        return personPk;
    }

    /**
     * Store PositionChanges info for an officer
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void storePositionChanges(Integer queuePk, Map positionChanges) {

        logger.debug("Store Position Changes(PositionChanges positionChanges) method entered.");
        
        if (positionChanges == null || positionChanges.size() == 0)
            return;
        
        Map.Entry           entry           =   null;
        Connection          con             =   DBUtil.getConnection();
        PreparedStatement   ps              =   null;
        PositionChanges     posChanges      =   null;
        Integer             affPk           =   null;
        try {
            // save Affiliates to AUP_Member_Pre_Upd_Smry table
            ps           = con.prepareStatement(SQL_INSERT_OFFICER_PRE_OFFICE_DETAIL);
            for (Iterator it = positionChanges.entrySet().iterator(); it.hasNext(); ) {
                entry        = (Map.Entry)       it.next();
                affPk        = (Integer)         entry.getKey();
                logger.debug("sending to db affPk============>" + affPk);
                List list = (List)entry.getValue();
                Iterator itr = list.iterator();
                while(itr.hasNext()){
                    posChanges   = (PositionChanges) itr.next();                                        
                    ps.setInt(1, queuePk.intValue());                                       
                    ps.setInt(2, affPk.intValue());                    
                    ps.setInt(3, posChanges.getGroupId().intValue());
                    ps.setInt(4, posChanges.getOfficePk().intValue());
                    ps.setInt(5, posChanges.getInFile());
                    ps.setInt(6, posChanges.getAllowed());

                    ps.addBatch();
                }
            }
            
            ps.executeBatch();
            logger.debug("ps.getUpdateCount()===>" + ps.getUpdateCount());
            
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        logger.debug("Store Position Changes(PositionChanges positionChanges) method existed.");
    }
    
    /**
     * Stores an OfficerPreUpdateSummary
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void storePreUpdateSummary(Integer queuePk, OfficerPreUpdateSummary summary) {
        logger.debug("----------------------------------------------------");
        logger.debug("Store Pre Update Summary(Integer, OfficerPreUpdateSummary) method entered.");
        
        Connection con          = DBUtil.getConnection();
        PreparedStatement ps    = null;
        PreparedStatement ps2   = null;
        Map.Entry entry         = null;
        Integer affPk           = null;

        try {
            
            if (summary.getOfficerCounts() != null && summary.getOfficerCounts().size() > 0){
                logger.debug("Saving officers statistic counts to AUP_Member_Pre_Upd_Smry ");
                ps = con.prepareStatement(SQL_INSERT_OFFICER_PRE_UPDATE_SUMMARY);

                AffiliateIdentifier affId   = null;
                OfficerChanges offChanges   = null;

                for (Iterator it = summary.getOfficerCounts().entrySet().iterator(); it.hasNext(); ) {
                    entry       = (Map.Entry)it.next();
                    logger.debug("name of key is " + entry.getKey().getClass().getName());
                    if (entry.getKey().getClass().getName().equals("org.afscme.enterprise.affiliate.AffiliateIdentifier")){
                        affId       = (AffiliateIdentifier)entry.getKey();
                    }
                    offChanges  = (OfficerChanges)entry.getValue();
                    affPk       = offChanges.getAffPk();
                    
                    logger.debug("queue pk : " + queuePk.intValue());
                    logger.debug("affPk: " + affPk);
                    logger.debug("affId: " + affId);
                    logger.debug("stats: " + offChanges);
                    logger.debug("in system: " + offChanges.getInSystem());
                    ps.setInt(1, queuePk.intValue());
                    DBUtil.setNullableInt(ps, 2, affPk);
                    ps.setInt(3, offChanges.getInSystem());
                    ps.setInt(4, offChanges.getInFile());
                    ps.setInt(5, offChanges.getAdded());
                    ps.setInt(6, offChanges.getVacant());
                    ps.setInt(7, offChanges.getChanged());
                    ps.setInt(8, offChanges.getReplaced());
                    ps.setInt(9, offChanges.getCards());
                    DBUtil.setBooleanAsShort(ps, 10, offChanges.getHasWarning());
                    DBUtil.setBooleanAsShort(ps, 11, offChanges.getHasError());

                    // only fill in aff_err fields if affPk does not exist (not valid affiliate)
                    if (affPk == null) {                        
                        DBUtil.setNullableVarchar(ps, 12, affId.getType().toString());
                        DBUtil.setNullableVarchar(ps, 13, affId.getLocal());
                        DBUtil.setNullableVarchar(ps, 14, affId.getSubUnit());
                        DBUtil.setNullableVarchar(ps, 15, affId.getCouncil());
                        DBUtil.setNullableVarchar(ps, 16, affId.getState());
                    }                    
                    else {
                        ps.setNull(12, Types.CHAR);
                        ps.setNull(13, Types.VARCHAR);
                        ps.setNull(14, Types.VARCHAR);
                        ps.setNull(15, Types.VARCHAR);
                        ps.setNull(16, Types.VARCHAR);                        
                    }

                    ps.addBatch();

                }
                int row[] = ps.executeBatch();
                logger.debug("Add row count : " + row.length);
                logger.debug("Add row count : " + row[0]);

                DBUtil.cleanup(null, ps, null);
            }
            //******************************************************************
            
            // save Exceptions to the AUP_Pre_Err_Smry and AUP_Pre_Err_Dtl tables
            logger.debug("Saving officer exceptions to AUP_Pre_Err_Smry and AUP_Pre_Err_Dtl tables.");
            ps = con.prepareStatement(SQL_INSERT_PRE_ERROR_SUMMARY_EXCEPTIONS);
            ps2 = con.prepareStatement(SQL_INSERT_PRE_ERROR_DETAIL_EXCEPTIONS);

            List exceptions = null;
            ExceptionData exData = null;
            Integer updateFieldPk = null;
            ExceptionComparison compar = null;
            int recordId = 1; /* Leave this line out here and not in the first loop.
                               * The recordId is an overall counter of the exceptions
                               * in the file.
                               **/

            if(summary.getExceptions() != null){
                logger.debug("Officer exceptions have been found." );
                for (Iterator it = summary.getExceptions().entrySet().iterator(); it.hasNext(); ) {
                    entry = (Map.Entry)it.next();                    
                    //affPk = (Integer)entry.getKey();                                        
                    exceptions = (List)entry.getValue();
                    for (Iterator it2 = exceptions.iterator(); it2.hasNext(); recordId++) {
                        exData = (ExceptionData)it2.next();
                        affPk = exData.getAffPk();
                        logger.debug("Exception summary affPk: " + affPk);                        
                        ps.setInt(1, affPk.intValue());
                        ps.setInt(2, queuePk.intValue());
                        ps.setInt(3, recordId);
                        DBUtil.setNullableInt(ps, 4, exData.getPersonPk());
                        DBUtil.setNullableVarchar(ps, 5, exData.getLastName());
                        DBUtil.setNullableVarchar(ps, 6, exData.getMiddleName());
                        DBUtil.setNullableVarchar(ps, 7, exData.getFirstName());
                        DBUtil.setNullableVarchar(ps, 8, exData.getSuffix());
                        DBUtil.setNullableInt(ps, 9, exData.getUpdateErrorCodePk());
                        ps.addBatch();

                        for (Iterator it3 = exData.getFieldChangeDetails().entrySet().iterator(); it3.hasNext(); ) {
                            
                            entry = (Map.Entry)it3.next();
                            updateFieldPk       = (Integer)entry.getKey();
                            ArrayList list      = (ArrayList) entry.getValue();
                            Iterator  listIt    =  list.iterator();
                            while(listIt.hasNext()){
                                compar = (ExceptionComparison)listIt.next();
                                logger.debug("Officer exception detail affPk: " + affPk);
                                ps2.setInt(1, affPk.intValue());
                                ps2.setInt(2, queuePk.intValue());
                                ps2.setInt(3, recordId);
                                ps2.setInt(4, updateFieldPk.intValue());
                                DBUtil.setNullableVarchar(ps2, 5, compar.getValueInSystem());
                                DBUtil.setNullableVarchar(ps2, 6, compar.getValueInFile());
                                DBUtil.setBooleanAsShort(ps2, 7, compar.isError());
                                ps2.addBatch();
                            }
                        }
                    }
                }
                ps.executeBatch();
                ps2.executeBatch();
                DBUtil.cleanup(null, ps, null);
                DBUtil.cleanup(null, ps2, null);

                /*Comment the below code for now*/
                // save Field Change counts to the AUP_Pre_Fld_Chg_Smry
            }
            
            logger.debug("Saving officer field change count.");
            ps = con.prepareStatement(SQL_INSERT_OFF_FIELD_CHANGE_SUMMARY);

            FieldChange fChange = null;
            if(summary.getFieldChanges() != null){
                for (Iterator it = summary.getFieldChanges().entrySet().iterator(); it.hasNext(); ) {
                    entry = (Map.Entry)it.next();
                    updateFieldPk = (Integer)entry.getKey();
                    fChange = (FieldChange)entry.getValue();
                    ps.setInt(1, queuePk.intValue());
                    ps.setInt(2, updateFieldPk.intValue());
                    ps.setInt(3, fChange.getCount());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException se) {
            logger.debug("SQLException : ");
            logger.debug("SQLException : " + se.getMessage());
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
         logger.debug("Store Pre Update Summary(Integer, OfficerPreUpdateSummary) method existed.");       
    }


   /**
     * Updates a list of officers in the affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updateOfficers(Map officers, Integer queuePk, Integer userPk,OfficerReviewSummary oReviewSmry) {
        logger.debug("----------------------------------------------------");
        logger.debug("updateOfficers(Map officers, Integer queuePk, Integer userPk) called.");
        Iterator it = officers.values().iterator();
        Set offToAdd    =   new HashSet();
        Set offToVacate =   new HashSet();
        Set offToRenew  =   new HashSet();
        Set offToRemove =   new HashSet();
        OfficerReviewData     reviewData  =   new OfficerReviewData();
        
                
        //OfficerReviewData []  reviewData  =   new OfficerReviewData[];
        logger.debug("officers.values()." + officers.values());
        while(it.hasNext()){
            OfficerUpdateElement officer = (OfficerUpdateElement) it.next();
            if (officer.getAfscmeMemberNumber() == null || officer.getAfscmeMemberNumber().toString().length() == 0 || officer.getAfscmeMemberNumber().intValue() ==0)
                officer.setAfscmeMemberNumber(new Integer(matchOfficer(officer)));
            
            if(officer.getTransactionType().equals("Add")){
                logger.debug("adding officer to set add" + officer.getTransactionType() + "firstName :" + officer.getFirstName());
                // HLM: 1/26/2004. Update Aff_Off_Activity table
                officersBean.updateAffOffActivity(officer.getAffPk(), ActivityType.A, 1);
                offToAdd.add(officer);
            }else if(officer.getTransactionType().equals("Remove")){
                logger.debug("adding officer to set remove" + officer.getTransactionType()+ "firstName :" + officer.getFirstName());
                // HLM: 1/26/2004. Update Aff_Off_Activity table
                officersBean.updateAffOffActivity(officer.getAffPk(), ActivityType.D, 1);
                offToRemove.add(officer);
            }else if(officer.getTransactionType().equals("Renew")){
                logger.debug("adding officer to set renew" + officer.getTransactionType()+ "firstName :" + officer.getFirstName());
                // HLM: 1/26/2004. Update Aff_Off_Activity table
                officersBean.updateAffOffActivity(officer.getAffPk(), ActivityType.U, 1);
                offToRenew.add(officer);    
            }else if(officer.getTransactionType().equals("Vacate")){
                logger.debug("adding officer to set vacate" + officer.getTransactionType()+ "firstName :" + officer.getFirstName());
                // HLM: 1/26/2004. Update Aff_Off_Activity table
                officersBean.updateAffOffActivity(officer.getAffPk(), ActivityType.D, 1);
                offToVacate.add(officer);    
            }
        }//end of while
        //***********************************************************************************
        //now we separated the officer update according to the trans type do the update to the db
        //***********************************************************************************
        if(!offToVacate.isEmpty()){
            logger.debug("----------------------------------------------------");
            logger.debug("calleing vacateOfficers.");
            vacateOfficers(offToVacate, userPk, oReviewSmry);
        }
        if(!offToRemove.isEmpty()){
            removeOfficers(offToRemove, userPk, oReviewSmry);
        }
        if(!offToRenew.isEmpty()){
            renewOfficers(offToRenew, userPk, oReviewSmry);
        }
        if(!offToAdd.isEmpty()){
            addOfficers(offToAdd, userPk, oReviewSmry);
        }
        
        addOfficersToCardRun(officers, userPk);
        logger.debug("calling storeOfficerUpdateSummary===>" +oReviewSmry);
        storeOfficerUpdateSummary(queuePk, oReviewSmry);
        //********************************************************************************************************************
        //System logging starts here event: Update Applied logging 
        //********************************************************************************************************************
        OfficerReviewSummary    oSummary        =   getOfficerReviewSummary(queuePk);
        OfficerReviewData[]     oRSmry          =   oSummary.getOfficerReviewData();
        OfficerReviewData       tCount          =   oSummary.getTotals();   
        ExceptionData[]         exceptions      =   oSummary.getExceptionResult();
        if (tCount != null)
            SystemLog.logUpdateApplied(tCount.getAddsCompleted(), tCount.getChangesCompleted(), tCount.getInacCompleted(), userPk.toString());                
//        else
//            SystemLog.logUpdateApplied(0, 0, 0, userPk.toString());                
        //******************************************************************************************        
        Map.Entry           parentEntry                 =   null;
        ArrayList           list                        =   null;
        Iterator            lt                          =   null;
        HashMap             map                         =   null;
        ExceptionData       eData                       =   null;
        
        //**************************************************************************
        //log if the summary has exceptions    
        //*****************************************************************************
        if (exceptions != null)
        logger.debug("exceptions=>" + exceptions.length);
        
        if(exceptions != null) {
            for(int i=0; i< exceptions.length; i++){               
                    eData               =   (ExceptionData) exceptions[i];                    
                    Map eComp           =   eData.getFieldChangeDetails();                    
                    Iterator eCompIt    =   eComp.values().iterator();
                    
                    while(eCompIt.hasNext()){
                        ArrayList al            =   (ArrayList) eCompIt.next();
                        Iterator alIt           =   al.iterator();                        
                        
                        while(alIt.hasNext()){
                            ExceptionComparison ec = (ExceptionComparison) alIt.next();                            
                            if(ec.getError()){
                                logger.debug(" calling logRecordUpdateError===>" );
                                SystemLog.logRecordUpdateError(ec.getField(), new java.sql.Timestamp(new java.util.Date().getTime()), "System value different", userPk.toString());
                            }                        
                        }//end of innermost while
                        
                    }//end of inner while                                                        
                
            }//end of for loop
        }//end of if
        //***********************************************************************************

    }//end of updateOfficers method
    //*******************************************************************************************************
    private boolean vacateOfficers(Set offToVacate, Integer userPk, OfficerReviewSummary oReviewSmry){
        
        Iterator it =   offToVacate.iterator();
        
        while(it.hasNext()){
            OfficerUpdateElement officer = (OfficerUpdateElement) it.next();
            
            AffiliateData affData = affilBean.getAffiliateData(officer.getAffPk());
            if (affData == null) {
                throw new EJBException("Member's Affiliate not found.");
            }
            logger.debug("executing update sql officer..." + officer.getFirstName());
            StringTokenizer tok     = new StringTokenizer(officer.getTermExpiration(), "-");
            int    year             = new Integer(tok.nextToken()).intValue();
            int    month            = new Integer(tok.nextToken()).intValue();
            OfficerReviewData data  = getReviewData(affData.getAffPk(), oReviewSmry);
            logger.debug("incrementVacantAttempted and incrementTransAttempted for: ==>" + officer.getFirstName());
            data.incrementVacantAttempted();
            data.incrementTransAttempted();
                
            int rows = execUpdate(SQL_UPDATE_END_DATE, officer.getAfscmeMemberNumber(), affData.getAffPk(), month, year, officer.getTitle());
            
            if(rows > 0 ){
                logger.debug("incrementVacantCompleted and incrementTransCompleted for: ==>" + officer.getFirstName());
                data.incrementVacantCompleted();
                data.incrementTransCompleted();
                officer.setUpdated(true);
            }else{
                logger.debug("incrementTransError  for: ==>" + officer.getFirstName());
                data.incrementTransError();
            }
            
            updatePersonalDetails(officer,affData,userPk);

            
            
        }//end of while
            return true;
    

    }
    
    private boolean removeOfficers(Set offToRemove, Integer userPk, OfficerReviewSummary oReviewSmry){
        
        Iterator it =   offToRemove.iterator();
        
        while(it.hasNext()){
            OfficerUpdateElement officer = (OfficerUpdateElement) it.next();
            StringTokenizer tok     = new StringTokenizer(officer.getTermExpiration(), "-");
            int    year             = new Integer(tok.nextToken()).intValue();
            int    month            = new Integer(tok.nextToken()).intValue();
            AffiliateData affData = affilBean.getAffiliateData(officer.getAffPk());
            if (affData == null) {
                throw new EJBException("Member's Affiliate not found.");
            }
            logger.debug("executing update sql to remove officer..." + officer.getFirstName());
            
            OfficerReviewData data  = getReviewData(affData.getAffPk(), oReviewSmry);
            logger.debug("incrementVacantAttempted and incrementTransAttempted for: ==>" + officer.getFirstName());
            data.incrementInacAttempted();
            data.incrementTransAttempted();
            
            int rows = execUpdate(SQL_UPDATE_END_DATE, officer.getAfscmeMemberNumber(), affData.getAffPk(), month, year, officer.getTitle());
            
            if(rows > 0 ){
                logger.debug("incrementVacantCompleted and incrementTransCompleted for: ==>" + officer.getFirstName());
                data.incrementInacCompleted();
                data.incrementTransCompleted();
                officer.setUpdated(true);
            }else{
                logger.debug("incrementTransError  for: ==>" + officer.getFirstName());
                data.incrementTransError();
            }
            updatePersonalDetails(officer,affData,userPk);

            
            
        }//end of while
            return true;
    

    }
    private boolean renewOfficers(Set offToRenew, Integer userPk, OfficerReviewSummary oReviewSmry){
        
        Iterator it =   offToRenew.iterator();
        
        while(it.hasNext()){
            OfficerUpdateElement officer = (OfficerUpdateElement) it.next();
            StringTokenizer tok     = new StringTokenizer(officer.getTermExpiration(), "-");
            int    year             = new Integer(tok.nextToken()).intValue();
            int    month            = new Integer(tok.nextToken()).intValue();
            AffiliateData affData = affilBean.getAffiliateData(officer.getAffPk());
            if (affData == null) {
                throw new EJBException("Member's Affiliate not found.");
            }
            logger.debug("executing update sql to renew officer..." + officer.getFirstName());
            
            OfficerReviewData data  = getReviewData(affData.getAffPk(), oReviewSmry);
            logger.debug("incrementChangesAttempted and incrementTransAttempted for: ==>" + officer.getFirstName());
            data.incrementChangesAttempted();
            data.incrementTransAttempted();
            int rows = execUpdate(SQL_UPDATE_TERM_END, officer.getAfscmeMemberNumber(), affData.getAffPk(), month, year, officer.getTitle());
            if(rows > 0 ){
                logger.debug("incrementChangesCompleted and incrementTransCompleted for: ==>" + officer.getFirstName());
                data.incrementChangesCompleted();
                data.incrementTransCompleted();
                officer.setUpdated(true);
            }else{
                logger.debug("incrementTransError  for: ==>" + officer.getFirstName());
                data.incrementTransError();
            }
            updatePersonalDetails(officer,affData,userPk);

            
            
        }//end of while
            return true;
    

    }
    
    
    private boolean addOfficers(Set offToAdd, Integer userPk, OfficerReviewSummary oReviewSmry){        
        logger.debug("Add Officers method entered.");        
        
        Iterator it =   offToAdd.iterator();        
        
        while(it.hasNext()){
            
            OfficerUpdateElement officer = (OfficerUpdateElement) it.next();                        
            AffiliateData affData = affilBean.getAffiliateData(officer.getAffPk());
            
            if (affData == null) {
                throw new EJBException("Officer's Affiliate not found.");
            }
            
            logger.debug("executing update sql to add officer..." + officer.getFirstName());
            
            OfficerReviewData data  = getReviewData(affData.getAffPk(), oReviewSmry);
            logger.debug("incrementAddsAttempted and incrementTransAttempted for: ==>" + officer.getFirstName());
            data.incrementAddsAttempted();
            data.incrementTransAttempted();                      
                                
            // Check if the officer is in the office already
            HashMap posMap = getPositionDetailsForAnOfficer(officer.getAffPk(), officer.getAfscmeMemberNumber());
            // The officer is not in the office yet
            if (!posMap.isEmpty()){                
                logger.debug("incrementTransError  for: ==>" + officer.getFirstName());
                data.incrementTransError();                    
                continue;
            }         
            
            if( (officer.getTermExpiration().equals("")) || (officer.getTitle()== null) || (officer.getTitle().intValue()==0) ){
                logger.debug("incrementTransError  for: ==>" + officer.getFirstName());
                data.incrementTransError();                    
                continue;                
            }                
            
            StringTokenizer tok     = new StringTokenizer(officer.getTermExpiration(), "-");
            int    year             = new Integer(tok.nextToken()).intValue();
            int    month            = new Integer(tok.nextToken()).intValue();

            int success = execInsert(officer.getAfscmeMemberNumber(), affData.getAffPk(), year, officer.getTitle().intValue(), month,userPk, officer.getHomeAffPk());
            logger.debug("success inside addOfficer======>" + success);
            if(success > 0){
                logger.debug("incrementAddsCompleted and incrementTransCompleted for: ==>" + officer.getFirstName());
                data.incrementAddsCompleted();
                data.incrementTransCompleted();
                officer.setUpdated(true);
                updatePersonalDetails(officer,affData,userPk);                                
            }else{
                logger.debug("incrementTransError  for: ==>" + officer.getFirstName());
                data.incrementTransError();
            }
            
        }//end of while
        
        logger.debug("Add Officers method existed.");                
        return true;            
    }
    
    
    
    private OfficerReviewData getReviewData(Integer affPk, OfficerReviewSummary oReviewSmry){
        logger.debug("Get Review Data method entered.");                
        logger.debug("Get Review Data method: affPk = " + affPk);                
        OfficerReviewData data = null;
        if(oReviewSmry != null){
            OfficerReviewData [] reviewList =  oReviewSmry.getOfficerReviewData();
            if (reviewList != null){
                int len = reviewList.length;
                for(int i =0; i < reviewList.length; i++){
                    data = reviewList[i];
                    logger.debug("affPk stored in oReviewSmry = " + data.getAffPk());                
                    if(data.getAffPk() != null && data.getAffPk().intValue() == affPk.intValue()){
                        logger.debug("The affPk is found in OfficerReviewSummary object.");
                        break;
                    }                                
                }
            }
        }   
        logger.debug("Get Review Data method existed.");                        
        return data;
    }
    private boolean updatePersonalDetails(OfficerUpdateElement officer, AffiliateData affData, Integer userPk){
        logger.debug("Update Personal Details method entered.");                        
        Integer addPk   =   null;
        addPk  = getPrimaryAddressPk(officer.getAfscmeMemberNumber());
        
        logger.debug("Updating officer...");
        PersonData person = new PersonData();
        if(addPk != null){
            person.setAddressPk(addPk);
        }
        person.setFirstNm(officer.getFirstName());
        person.setMiddleNm(officer.getMiddleName());
        person.setLastNm(officer.getLastName());
        person.setPersonPk(officer.getAfscmeMemberNumber());
        if(!officer.getPrefix().equals("")){
            person.setPrefixNm(new Integer(officer.getPrefix()));
        }
        person.setSsn(officer.getSsn());
        if(!officer.getSuffix().equals("")){
            person.setSuffixNm(new Integer(officer.getSuffix()));
        }
        personsBean.updatePersonDetail(officer.getAfscmeMemberNumber(), person);
        logger.debug("... Person updated");
        updateAddress(officer, affData, userPk, addPk);
        logger.debug("... officer.getPhoneData() in personal details" + officer.getPhoneData());
        updatePhone(officer,   userPk);
        
        logger.debug("Update Personal Details method existed.");                                
        return true;
   }
   private boolean updateAddress(OfficerUpdateElement officer, AffiliateData affData , Integer userPk, Integer addPk){
       
       
       try{
           if (officer.getAddressElement() != null) {
                    logger.debug("Processing Address...");
                    PersonAddress address = toPersonAddress(officer.getAddressElement());
                    Set updateErrors = null;

                    if(addPk   !=null){
                        logger.debug("    ...Updating Address...");
                        
                        updateErrors = smaBean.updateByAffiliate(userPk, affData.getAffPk(), officer.getAfscmeMemberNumber(), addPk, address);
                    }else  {
                        logger.debug("    ...Adding Address...");
                        updateErrors = smaBean.addByAffiliate(userPk, affData.getAffPk(), officer.getAfscmeMemberNumber(), address);
                    } 
                    if (!CollectionUtil.isEmpty(updateErrors)) {
                        //totalErrors.addAll(updateErrors);
                        logger.debug("****************************************************");
                        logger.debug("Address errors found: " + CollectionUtil.toString(updateErrors));
                        logger.debug("****************************************************");
                        //***************************************************************************************
                        //logging starts here Event:Apply Update Error
                        //***************************************************************************************
                        SystemLog.logApplyUpdateError("Officer Address", "Officer Address errors found",userPk.toString()) ;
                        //****************************************************************************************
                        throw new EJBException("Address had errors.");
                    }
                    
                    logger.debug("... Address processed");
                    return true;
                }
        } catch (EJBException se) {
            throw new EJBException(se);
        } 
       return false;
   }
   
   private boolean updatePhone(OfficerUpdateElement officer, Integer userPk){
       logger.debug("... updatePhone called");
       logger.debug("... officer.getPhoneData()" + officer.getPhoneData());
       if(officer.getPhoneData() != null){
           PhoneData phone = toPhoneData(officer);
           if ((officer.getPhoneData().getPhonePk() != null) && (officer.getPhoneData().getPhonePk().intValue() > 0)) {
                    logger.debug("Updating Phone...");
                    
                    //ArrayList al = (ArrayList) personsBean.getPersonPhones(officer.getAfscmeMemberNumber(), null);
                    //assumption if a person has one phone number that means he has primary
                    //if(al.isEmpty()){
                    //   personsBean.addPersonPhone(userPk, null, officer.getAfscmeMemberNumber(), phone);
                    //}else{
                        personsBean.updatePersonPhone(userPk, officer.getAfscmeMemberNumber(), phone);
                    //}
                    logger.debug("... Phone updated");
                    return true;
           }else if((officer.getPhoneData().getAreaCode() !=null) && (officer.getPhoneData().getCountryCode() != null) 
                                                                 &&  (officer.getPhoneData().getPhoneNumber() != null))  {
               personsBean.addPersonPhone(userPk, null, officer.getAfscmeMemberNumber(), phone);
           }    
       }
       logger.debug("returning false from update phones...");
       return false;
   }//end of updatePhone
   
    private void addOfficersToCardRun(Map officers, Integer userPk){
        logger.debug("Add Officers To Card Run method entered.");                                
        Iterator it = officers.values().iterator();                           
        logger.debug("officers.values()." + officers.values());
        
        Connection con = DBUtil.getConnection();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;        
        
        ResultSet rs1;
        ResultSet rs2;
        ResultSet rs4;
        HashSet list = new HashSet();
        HashSet cardRunSet = new HashSet();
        
        while(it.hasNext()){
            OfficerUpdateElement officer = (OfficerUpdateElement) it.next();
            StringTokenizer tok     = new StringTokenizer(officer.getTermExpiration(), "-");
            int    year             = new Integer(tok.nextToken()).intValue();
            int    month            = new Integer(tok.nextToken()).intValue();
            
            Calendar cal = new GregorianCalendar();

            int currentMonth = cal.get(Calendar.MONTH) + 1;           // 0=Jan, 1=Feb, ...                                
            int currentYear = cal.get(Calendar.YEAR);          
            if (year - currentYear < 0)
                continue;

            if ((year - currentYear) == 0){
                if (month - currentMonth < 3)
                    continue;
            }
            if ((year - currentYear) == 1){
                if (12 - currentMonth + month < 3)
                    continue;
            }            
            
            
            int    affPk            = officer.getAffPk().intValue();
            
            int    personPk         = officer.getAfscmeMemberNumber().intValue();
            try{
                
                    ps1 = con.prepareStatement(SQL_SELECT_AFFILIATE_OFFICERS);
                    ps2 = con.prepareStatement(SQL_INSERT_OFFICER_CARD_RUN);
                    ps3 = con.prepareStatement(SQL_UPDATE_END_DATE);
                    ps4 = con.prepareStatement(SQL_SELECT_OFFICER_CARD_RUN_SURROGATE_KEY);

                    //Integer             affPk       = null;
                    int                 pPk         = 0;
                    String              date        = null;
                    int                 currAffPk   =   0;
                    int                 length      = 0;
                    int                 key         = 0;
                    AffiliateData       data        = null;
                    AffiliateIdentifier affId       = null;                

                    logger.debug("    affPk = " + affPk);
                    //data = affilBean.getAffiliateData(affPk);
                    //if (data == null) {
                    //    throw new EJBException("No affiliate found.");
                    //}
                    //affId = data.getAffiliateId();
                    ps1.setInt(1, affPk);
                    rs1 = ps1.executeQuery();
                    
                    ps4.setInt(1, affPk);
                    rs4 = ps4.executeQuery();
                    
                    while (rs4.next())
                    {
                        cardRunSet.add(new Integer(rs4.getInt("officer_history_surrogate_key")));
                    }
                    
                    
                    while (rs1.next()) {
                        pPk      = rs1.getInt("person_pk");
                        length   = rs1.getInt("length_of_term");
                        date     = rs1.getString("date");   // pos_expiration_date
                        key      = rs1.getInt("key");
                        currAffPk= rs1.getInt("aff_pk");
                        logger.debug("key======>" + key);
                        logger.debug("personPk======>" + pPk);
                        logger.debug("length======>" + length);
                        logger.debug("date======>" + date);
                        
                        if(date != null && !cardRunSet.contains(new Integer(key))){
                            //this method must be implemented in the officer bean later its method must be called                                                                
                                            tok         = new StringTokenizer(date, "-");
                            int    posyear              = new Integer(tok.nextToken()).intValue();
                            int    posmonth             = new Integer(tok.nextToken()).intValue();
                            if(!list.contains(new Integer(pPk))){
                                if((pPk == personPk) && (affPk == currAffPk) &&!((posyear == year) && (posmonth < month)) 
                                   && ((length != (TermLength.INDEFINITE).intValue()) || (length != (TermLength.TEMPORARY).intValue()))){
                                    ps2.setInt(1, key);
                                    ps2.setInt(2, affPk);
                                    ps2.setInt(3, pPk);
                                    ps2.setInt(4, userPk.intValue());
                                    ps2.setInt(5, userPk.intValue());
                                    logger.debug("adding======>" );
                                    logger.debug("key======>" + key);
                                    logger.debug("personPk======>" + pPk);
                                    logger.debug("affPk======>" + affPk);

                                    ps2.executeUpdate();
                                }else if((pPk != personPk) && (affPk == currAffPk) && (length != (TermLength.INDEFINITE).intValue())){//inactivate the person not coming 
                                    ps3.setInt(1, affPk);
                                    ps3.setInt(2, pPk);
                                    ps3.executeUpdate();
                                }
                                list.add(new Integer(pPk));
                            }
                        }                        
                        
                    }//end of inner while
               
            }catch (SQLException se) {
                throw new EJBException(se);
            }
        }//end of while
        DBUtil.cleanup(con, ps1, null);
        logger.debug("Add Officers To Card Run method existed.");                                        
    }
    private void storeOfficerUpdateSummary(Integer queuePk, OfficerReviewSummary summary) {
        logger.debug("----------------------------------------------------");
        logger.debug("storeOfficerUpdateSummary called.");

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try {
            // store all top "AUP_Rv_Smry" entry
            ps = con.prepareStatement(SQL_INSERT_REVIEW_SUMMARY);
            Map.Entry entry;
            Integer affPk;
            PersonReviewData summaryData;
            if(summary != null){
                OfficerReviewData [] reviewList =  summary.getOfficerReviewData();
            
                for(int i =0; i < reviewList.length; i++){
                    logger.debug("reviewList.length==>" +reviewList.length + "  Now===>" + i);
                  
                    summaryData = reviewList[i];
                    if (summaryData.getAffPk() != null){
                        affPk   =   summaryData.getAffPk();
                        logger.debug("affPk = " + affPk);
                        logger.debug("queuePk = " + queuePk);  
                        ps.setInt(1, affPk.intValue());
                        ps.setInt(2, queuePk.intValue());
                        ps.setInt(3, summaryData.getTransAttempted());
                        ps.setInt(4, summaryData.getTransCompleted());
                        ps.setInt(5, summaryData.getTransError());
                        ps.setInt(6, summaryData.getAddsAttempted());
                        ps.setInt(7, summaryData.getAddsCompleted());
                        ps.setInt(8, summaryData.getChangesAttempted());
                        ps.setInt(9, summaryData.getChangesCompleted());
                        ps.setInt(10, summaryData.getInacAttempted());
                        ps.setInt(11, summaryData.getInacCompleted());
                        ps.setInt(12, summaryData.getInacTCount());
                        ps.setInt(13, summaryData.getVacantAttempted());
                        ps.setInt(14, summaryData.getVacantCompleted());
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
                DBUtil.cleanup(null, ps, null);

                // save Exceptions to the AUP_Rv_Err_Smry and AUP_Rv_Err_Dtl tables
                ps = con.prepareStatement(SQL_INSERT_REVIEW_ERROR_SUMMARY_EXCEPTIONS);
                ps2 = con.prepareStatement(SQL_INSERT_REVIEW_ERROR_DETAIL_EXCEPTIONS);

                List exceptions = null;
                ExceptionData exData = null;
                ExceptionComparison compar = null;
                Integer fieldPk = null;
                int recordId = 1; /* Leave this line out here and not in the first loop.
                                   * The recordId is an overall counter of the exceptions
                                   * in the file.
                                   **/
                ExceptionData [] expList =  summary.getExceptionResult();
                logger.debug("expList==>" + expList);
                if(expList != null){
                    for(int i =0; i < expList.length; i++){
                        exData = expList[i];
                        logger.debug("expList[i];==>" + expList[i]);
                        //for (Iterator it = summary.getExceptions().entrySet().iterator(); it.hasNext(); ) {
                        //    entry = (Map.Entry)it.next();
                        //    affPk = (Integer)entry.getKey();
                        //    exceptions = (List)entry.getValue();
                        //    for (Iterator it2 = exceptions.iterator(); it2.hasNext(); recordId++) {
                                //exData = (ExceptionData)it2.next();
                        if (exData.getAffPk() != null){  
                                affPk = exData.getAffPk();
                                ps.setInt(1, affPk.intValue());
                                ps.setInt(2, queuePk.intValue());
                                ps.setInt(3, i);
                                DBUtil.setNullableInt(ps, 4, exData.getPersonPk());
                                DBUtil.setNullableVarchar(ps, 5, exData.getLastName());
                                DBUtil.setNullableVarchar(ps, 6, exData.getMiddleName());
                                DBUtil.setNullableVarchar(ps, 7, exData.getFirstName());
                                DBUtil.setNullableVarchar(ps, 8, exData.getSuffix());
                                DBUtil.setNullableInt(ps, 9, exData.getUpdateErrorCodePk());
                                ps.addBatch();

                                for (Iterator it3 = exData.getFieldChangeDetails().entrySet().iterator(); it3.hasNext(); ) {
                                    entry = (Map.Entry)it3.next();
                                    fieldPk = (Integer)entry.getKey();
                                    ArrayList list = (ArrayList) entry.getValue();
                                    Iterator it = list.iterator();
                                    while(it.hasNext()){
                                        //compar = (ExceptionComparison)entry.getValue();
                                        compar = (ExceptionComparison) it.next();
                                        ps2.setInt(1, affPk.intValue());
                                        ps2.setInt(2, queuePk.intValue());
                                        ps2.setInt(3, i);
                                        ps2.setInt(4, fieldPk.intValue());
                                        DBUtil.setNullableVarchar(ps2, 5, compar.getValueInSystem());
                                        DBUtil.setNullableVarchar(ps2, 6, compar.getValueInFile());
                                        DBUtil.setBooleanAsShort(ps2, 7, compar.getError());
                                        ps2.addBatch();
                                    }
                                }
                         //   }
                        }
                        ps.executeBatch();
                        ps2.executeBatch();
                        logger.debug("...exceptions saved...");
                    }
                }//end of exception check
            }//end of summary check
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(null, ps, null);
            DBUtil.cleanup(con, ps2, null);
        }
    }
    /************************************************************************************************/
    /* **** Note: METHODS *** ABOVE *** HERE ARE ALL FOR THE    OFFICER    UPDATE PROCESSING!!      */
    /************************************************************************************************/

    
/*****************************************************************************************************************/

    
    /************************************************************************************************/
    /* **** Note: METHODS *** BELOW *** HERE ARE ALL FOR THE  PARTICIPATION  UPDATE PROCESSING!!    */
    /************************************************************************************************/
    
    /**
     * Retrieves the detail data for a Participation Detail.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param groupName the Participation Group Name
     * @param typeName the Participation Type Name
     * @param detailName the Participation Detail Name
     * @return the ParticipationOutcomeData object representing Participation object for Detail.
     */
    public ParticipationOutcomeData getParticipationDetailData(String groupName, String typeName, String detailName)
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ParticipationOutcomeData outcome = null;
        
        try {

            //retrieve a Participation Detail 
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_DETAIL_BY_NAMES);
            ps.setString(1, groupName);
            ps.setString(2, typeName);
            ps.setString(3, detailName);
            rs = ps.executeQuery();

            //put the result into a ParticipationOutcomeData object
            if (rs.next()) {
                outcome = new ParticipationOutcomeData();
                outcome.setGroupPk(new Integer(rs.getInt("particip_group_pk")));
                outcome.setGroupNm(rs.getString("particip_group_nm"));
                outcome.setTypePk(new Integer(rs.getInt("particip_type_pk")));
                outcome.setTypeNm(rs.getString("particip_type_nm"));
                outcome.setDetailPk(new Integer(rs.getInt("particip_detail_pk")));
                outcome.setDetailNm(rs.getString("particip_detail_nm"));
                outcome.setDetailShortcut(rs.getInt("particip_detail_shortcut"));
            }
        } catch (SQLException e) {
            throw new EJBException("Error retrieving a Participation Detail in UpdateBean.getParticipationDetailData()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the outcome data object
        return outcome;
    }
    
    /**
     * Retrieves all of the Participation Outcomes for a given Participation Detail
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param detailPk the Participation Detail Primary Key
     * @return a Collection of Participation Outcomes objects.
     */
    public Map getParticipationOutcomes(Integer detailPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map result = new HashMap();
        
        try {

            //retrieve all the Participation Outcomes for a Group Detail
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_PARTICIPATION_OUTCOMES_BY_DETAIL);
            ps.setInt(1, detailPk.intValue());            
            rs = ps.executeQuery();
            
            //put the results into a map of Participation Outcomes
            while (rs.next()) {
                
                // place all outcomes in uppercase to match file better
                String outcomeName = rs.getString("particip_outcome_nm").trim().toUpperCase();
                Integer outcomePk = new Integer(rs.getInt("particip_outcome_pk"));
                result.put(outcomeName, outcomePk);
            }
        } catch (SQLException e) {
            throw new EJBException("Error retrieving Participation Outcomes for a Detail in UpdateBean.getParticipationOutcomes()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        //return the map of outcomes
        return result;
    }
 
    /**
     * Looks for a match for the given element.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param memberParticipation The member to attempt to match
     * @return boolean - true if member is found as valid participation
     */
    public boolean matchParticipation(ParticipationUpdateElement memberParticipation) {

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exceptionFound = false;
        boolean matchFound = false;        

        // Data Match Rule 1. - try to match the AFSCME Member Number (personPk)
        try {

            String firstName = null;
            String lastName = null;
           
            if ((memberParticipation.getPersonPk() != null) && (memberParticipation.getPersonPk().intValue() != 0)) {

                ps = con.prepareStatement(SQL_MEMBER_MATCH_PARTICIPATION + SQL_WHERE_MEMBER_MATCH_PARTICIPATION_MEMBER_NUMBER);
                ps.setInt(1, memberParticipation.getPersonPk().intValue());

                rs = ps.executeQuery();

                while (rs.next()) {
                    matchFound = true;
                    break;
                }
            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return matchFound;
    }

    /**
     * Performs the member updates into the database for the Update process
     *
     * @ejb:interface-method view-typ="lcoal"
     * @ejb:transaction type="Required"
     *
     * @param userPk The user making the update
     * @param updateSummary The ParticipationUpdateSummary that contains all the ParticipationUpdateElements
     */
    public void doParticipationUpdate(Integer userPk, ParticipationUpdateSummary updateSummary) {
        
        logger.debug("----------------------------------------------------");
        logger.debug("doParticipationUpdate called.");

        Set updateErrors = new HashSet();
        
        // do participation updates
        ParticipationUpdateElement participation;
        int     add         =   0;
        int     update      =   0;
        boolean updateDone = false;
        for (Iterator it = updateSummary.getUpdates().values().iterator(); it.hasNext(); ) {
            participation = (ParticipationUpdateElement)it.next();
            logger.debug("participation: " + participation);
            try {
                if (isExistingMemberParticipation(participation.getDetailPk(), participation.getPersonPk())) {
                    updateDone = updateMemberParticipationOutcome(participation, userPk);
                    update++;
                }
                else {
                    updateDone = addMemberParticipationOutcome(participation, userPk);
                    add++;
                }    
                if (!updateDone) {
                    /** @TODO: do something with the updateErrors. */
                }
            } catch (EJBException e) {
                logger.debug("", e);
                /** @TODO: do something with the updateErrors. */
            }
            updateDone = false;
        }
        //**************************************************************************
        //log that participation update has been performed
        SystemLog.logUpdateApplied(add, update, 0, userPk.toString());
        //***************************************************************************
    }

    /**
     * Checks for an existing Member Participation for the detailPk and personPk.
     * 
     * @param detailPk the Participation Detail Primary Key
     * @param personPk - the Participation User Person Primary Key
     * @return boolean returns TRUE if member participation already exists, 
     *                         FALSE if the combo key does not exist.
     */
    private boolean isExistingMemberParticipation(Integer detailPk, Integer personPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        // check if the member participation already exists
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EXIST_MATCH_PARTICIPATION_OUTCOME);
            ps.setInt(1, detailPk.intValue());
            ps.setInt(2, personPk.intValue());
            
            //get the count
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count != 0)
                return true;
            
        } catch (SQLException e) {
            throw new EJBException("Error checking for existing member participation in UpdateBean.isExistingMemberParticipation()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return false;        
    }

    /**
     * Updates an existing Member Participation Outcome.
     * 
     * @param outcome the Member Participation Outcome data
     * @param updatedByUserPk - User Pk of the user changing the data
     * @return boolean returns TRUE if operation completes successfully, FALSE if the operation
     *  does not complete successfully.
     */
    private boolean updateMemberParticipationOutcome(ParticipationUpdateElement outcome, Integer updatedByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;
        
        // updates the member participation outcome
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_MEMBER_PARTICIPATION_OUTCOME);
            
            //set the values to update
            ps.setInt(1, outcome.getOutcomePk().intValue());
            ps.setTimestamp(2, outcome.getOutcomeDate());
            ps.setString(3, "Updated by Upload Participation Data File");
            ps.setInt(4, updatedByUserPk.intValue());
            ps.setInt(5, outcome.getDetailPk().intValue());
            ps.setInt(6, outcome.getPersonPk().intValue());
            result = (ps.executeUpdate() != 0);

        } catch (SQLException e) {
            throw new EJBException("Error updating member participation in UpdateBean.updateMemberParticipationOutcome()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return result;
    }

    /**
     * Adds a new Member Participation Outcome.
     * 
     * @param outcome the Member Participation Outcome data
     * @param updatedByUserPk - User Pk of the user adding the data
     * @return boolean returns TRUE if operation completes successfully, FALSE if the operation
     *  does not complete successfully.
     */
    private boolean addMemberParticipationOutcome(ParticipationUpdateElement outcome, Integer updatedByUserPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;
        
        //adds the member participation 
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_MEMBER_PARTICIPATION_OUTCOME);

            //set the values to insert
            ps.setInt(1, outcome.getDetailPk().intValue());
            ps.setInt(2, outcome.getPersonPk().intValue());
            ps.setInt(3, outcome.getOutcomePk().intValue());
            ps.setTimestamp(4, outcome.getOutcomeDate());
            ps.setString(5, "Updated by Upload Participation Data File");
            ps.setInt(6, updatedByUserPk.intValue());
            ps.setInt(7, updatedByUserPk.intValue());
            result = (ps.executeUpdate() != 0);            
            
        } catch (SQLException e) {
            throw new EJBException("Error inserting member participation in UpdateBean.addMemberParticipationOutcome()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return result;
    }
    
    /************************************************************************************************/
    /* **** Note: METHODS *** ABOVE *** HERE ARE ALL FOR THE  PARTICIPATION  UPDATE PROCESSING!!    */
    /************************************************************************************************/
 

/*****************************************************************************************************************/
    
    /**
     * Gets all affiliates which are children of the given affiliate.
     *
     * @deprecated
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return A Map of AffiliateIdentifier objects, by primary key.
     */
    public Map getSubAffiliates(Integer affPk) {
        return null;
    }

    /**
     * Gets ths update file for the specified affiliate (in fixed-length format)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param affPk The primary key of the affiliate to get the update file for
     * @return byte array of the file data.
     */
    public byte[] getUpdateFile(int affPk) {
        return null;
    }

    private CodeData getCodeData(String codeType, Integer codePk) {
        Map codes = codesBean.getCodes(codeType);
        return (CodeData)codes.get(codePk);
    }

    private CodeData getCodeData(String codeType, String code) {
        Map codes = codesBean.getCodesByCode(codeType);
        return (CodeData)codes.get(code);
    }

    private String getCodeDescription(String codeType, Integer codePk) {
        CodeData data = getCodeData(codeType, codePk);
        if (data == null) {
            return "";
        }
        return data.getDescription();
    }

    private Integer getCodePk(String codeType, String code) {
        CodeData data = getCodeData(codeType, code);
        return (data == null ? null : data.getPk());

    }

    private boolean isSSNValid(String ssn) {
        if (TextUtil.isEmptyOrSpaces(ssn)) {
            return false;
        }
        return !TextUtil.isAllZeros(ssn);
    }

    private PersonAddress toPersonAddress(AddressElement address) {
        if (address == null) {
            return null;
        }
        PersonAddress pa = new PersonAddress();
        pa.setAddr1(address.getAddr1());
        pa.setAddr2(address.getAddr2());
        pa.setCity(address.getCity());
        pa.setState(address.getState());
        logger.debug("address.getZipCode() inside toPersonAddress==>" +address.getZipCode()); 
        pa.setZipCode(address.getZipCode());
        pa.setZipPlus(address.getZipPlus());
        pa.setCountryPk(org.afscme.enterprise.codes.Codes.Country.US);
        pa.setDepartment(DEPT_PK);
        pa.setPrimary(true);
        pa.setSource(PersonAddress.SOURCE_AFFILIATE_STAFF);
        pa.setType(org.afscme.enterprise.codes.Codes.PersonAddressType.HOME);
        return pa;
    }

    private boolean checkPhoneExists(MemberUpdateElement member) {

        // return false if null or all spaces or all zeros
        if ((TextUtil.isEmptyOrSpaces(member.getAreaCode())) && 
            (TextUtil.isEmptyOrSpaces(member.getPhoneNumber()))) {
            return false;
        }
        else if ((TextUtil.isAllZeros(member.getAreaCode())) &&
                 (TextUtil.isAllZeros(member.getPhoneNumber()))) {
            return false;
        }
        else {
            return true;
        }
    }

    private PhoneData toPhoneData(MemberUpdateElement member) {
        PhoneData phone = new PhoneData();
        phone.setPhonePk(member.getPhonePk());
        phone.setAreaCode(member.getAreaCode());
        phone.setPhoneNumber(member.getPhoneNumber());
        phone.setCountryCode("1");
        phone.setDept(DEPT_PK);
        phone.setPhoneType(org.afscme.enterprise.codes.Codes.PhoneType.HOME);
        phone.setPhonePrmryFg(new Boolean(true));
        return phone;
    }

    private PhoneData toPhoneData(OfficerUpdateElement member) {
        PhoneData phone = new PhoneData();
        phone.setPhonePk(member.getPhoneData().getPhonePk());
        phone.setAreaCode(member.getPhoneData().getAreaCode());
        phone.setPhoneNumber(member.getPhoneData().getPhoneNumber());
        phone.setCountryCode("1");
        phone.setDept(DEPT_PK);
        phone.setPhoneType(org.afscme.enterprise.codes.Codes.PhoneType.HOME);
        phone.setPhonePrmryFg(new Boolean(true));
        return phone;
    }
    
    private void updateExceptionData(ExceptionData data, Integer fieldTypePk,
                                     String fileValue, String systemValue,
                                     boolean isError
    ) {
        ExceptionComparison compar = (ExceptionComparison)data.getFieldChangeDetails().get(fieldTypePk);
        if (compar == null) {
            compar = new ExceptionComparison();
            data.getFieldChangeDetails().put(fieldTypePk, compar);
        }
        compar.setError(isError);
        compar.setValueInFile(fileValue);
        compar.setValueInSystem(systemValue);
    }

    /************************************************************************************************/
    /* **** Note: METHODS *** ABOVE *** HERE ARE ALL FOR THE PROCESSING OF THE APPLY UPDATE!!       */
    /************************************************************************************************/

    
/*****************************************************************************************************************/
/*****************************************************************************************************************/

    
    /************************************************************************************************/
    /* *** Note: METHODS *** BELOW *** HERE ARE ALL FOR THE DISPLAY OF THE SCREENS!!                */
    /************************************************************************************************/

    /***********************************************************************************************/
    /*The following method will get pre update summary from the database and populate the map data  */
    /* structure. This method is used to display the results of the the upload file process         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Member Pre-Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the pre-update summary was generated for
     */
    public MemberPreUpdateSummary getMemberPreUpdateSummary(Integer queuePk) {
        logger.debug("----------------------------------------------------");
        logger.debug("getMemberPreUpdateSummary called with queuePk = " + queuePk);
        if (queuePk == null || queuePk.intValue() < 1) {
            return null;
        }
        HashMap sumMap  =   new HashMap();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MemberPreUpdateSummary summary = null;
        /********************************************************************************/
        //moved the two lines here
        summary = new MemberPreUpdateSummary();
        MemberChanges totalChanges = new MemberChanges();
        //*********************************************************************************
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_PRE_UPDATE_SUMMARY);
            ps.setInt(1, queuePk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                //summary = new MemberPreUpdateSummary();
                Integer affPk = null;
                AffiliateIdentifier affId = null;
                //MemberChanges currChanges = new MemberChanges();
                //MemberChanges totalChanges = new MemberChanges();
                do {
                    MemberChanges currChanges = new MemberChanges();
                    affPk = new Integer(rs.getInt("aff_pk"));
                    if (affPk == null || affPk.intValue() == 0) {
                        affId = new AffiliateIdentifier();
                        affId.setType(new Character(rs.getString("aff_err_aff_type").charAt(0)));
                        affId.setLocal(rs.getString("aff_err_aff_localSubChapter"));
                        affId.setSubUnit(rs.getString("aff_err_aff_subUnit"));
                        affId.setCouncil(rs.getString("aff_err_aff_councilRetiree_chap"));
                    }
                    else {
                        affId = affilBean.getAffiliateData(affPk).getAffiliateId();
                    }
                    currChanges.setAffPk(affPk);
                    currChanges.setInFile(rs.getInt("mbr_file_cnt"));
                    currChanges.setInSystem(rs.getInt("mbr_system_cnt"));
                    currChanges.setAdded(rs.getInt("mbr_added_cnt"));
                    currChanges.setInactivated(rs.getInt("mbr_inactivated_cnt"));
                    currChanges.setChanged(rs.getInt("mbr_changed_cnt"));
                    currChanges.setNewTRecords(rs.getInt("t_mbrs_created_cnt"));
                    currChanges.setNonMatch(rs.getInt("mbr_nonmatched_cnt"));
                    currChanges.setMatch(rs.getInt("mbr_matched_cnt"));
                    currChanges.setHasWarning(rs.getBoolean("aff_warning_fg"));
                    currChanges.setHasError(rs.getBoolean("aff_error_fg"));

                    totalChanges.addToValues(currChanges);

                    sumMap.put(affId, currChanges);

                    //summary.getMemberCounts().put(affId, currChanges);
                    /*****************************************************************************/
                    /************> Exception logic starts from here <****************************/
                    /****************************************************************************/
                    logger.debug("----------------------------------------------------");
                    logger.debug("calling getExceptions with  queuePK===> " + queuePk);
                    logger.debug("calling getExceptions with  affPK===> " + affPk);
                    /****************************************************************************/
                    boolean result = getExceptions(affPk, queuePk, summary, affId);
                    /*logger.debug("----------------------------------------------------");
                    logger.debug("FirstName===> " + excepData.getFirstName());
                    logger.debug("----------------------------------------------------");

                    logger.debug("----------------------------------------------------");
                    logger.debug("recCount===> " + recordId);
                    logger.debug("----------------------------------------------------");
                    summary.addException(   new Integer(recordId)  , excepData);
                    summary.addAffId(       new Integer(recordId)  , affId);
                    logger.debug("done calling getExceptions ===> " );
                    /****************************************************************************/
                    currChanges = null;

                } while (rs.next());
                summary.setMemberCounts(sumMap);
                summary.setTotalCounts(totalChanges);
                DBUtil.cleanup(null, ps, rs);
                boolean result = getFieldChanges(queuePk, summary);
                /** @TODO: Get field changes counts */
                /** @TODO: Get Exceptions */

            }
        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return summary;
    }

    /***********************************************************************************************/
    /*The following method will get the exception from the database and populate the map data       */
    /* structure. This method is used to display the exceptions of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    public boolean getExceptions(Integer affPk, Integer queuePk, MemberPreUpdateSummary mSummary, AffiliateIdentifier affId){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getException called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1) || (affPk == null || affPk.intValue() < 1) ) {
            return false;
        }
        logger.debug("Passed checking queuepk and affpk " + queuePk);

        //**********************************************************
        //define the local variables
        //**********************************************************
        final int           MEMBER              =   0; //define this constant for now
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        PreparedStatement   ps2                  =   null;        
        ResultSet           rsSummary           =   null;
        ResultSet           rsDetail            =   null;
        ExceptionData       eSummary            =   null;
        ExceptionComparison eDetail             =   null;
        HashMap             eSumMap             =   new HashMap();   //dataStructure to hold the parent exception
        HashMap             eDetMap             =   null;           //datastructure to hold the child exception

        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_PRE_ERR_SUMMARY);
            //
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, queuePk.intValue());

            rsSummary       = ps.executeQuery();



            while (rsSummary.next()) {
                eSummary            = new ExceptionData();

                eSummary.setAffId(                          affId                           );
                eSummary.setFirstName(          rsSummary.getString("first_nm")             );
                eSummary.setLastName(           rsSummary.getString("last_nm")              );
                eSummary.setMiddleName(         rsSummary.getString("middle_nm")            );
                eSummary.setSuffix(             rsSummary.getString("suffix_nm")            );
                eSummary.setPersonPk(           new Integer(rsSummary.getInt("person_pk"))  );
                eSummary.setRecordId(           new Integer(rsSummary.getInt("record_id"))  );
                eSummary.setAffPk(              new Integer(rsSummary.getInt("aff_pk"))     );
                eSummary.setUpdateErrorCodePk(  new Integer(rsSummary.getInt("upd_error_type")));

                recordId            = eSummary.getRecordId().intValue();

                /*************************************************************************/
                /*now that we have the summary get the details of the exception          */
                /*************************************************************************/               
                ps2 = con.prepareStatement(SQL_SELECT_PRE_ERR_DETAIL);
                //
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, queuePk.intValue());
                ps2.setInt(3, recordId);

                rsDetail        = ps2.executeQuery();
                while(rsDetail.next()){
                    eDetail     = new ExceptionComparison();
                    //*****************************************************************
                    //Set the value in the object based on the int value in the database
                    //*****************************************************************
                    // 0    : False
                    // 1    : True
                    //*****************************************************************

                    if(rsDetail.getInt("fld_error_fg") == 0){
                        eDetail.setError(false);
                    } else {
                        eDetail.setError(true);
                    }
                    //************************************************************************
                    eDetail.setField        (       rsDetail.getString("com_cd_desc")                                                                                   );
                    //                                                      if(true)                    ? this      else    This
                    eDetail.setValueInFile  (       rsDetail.getString("fld_value_in_file")    == null  ? ("")      :       (rsDetail.getString("fld_value_in_file"))   );
                    eDetail.setValueInSystem(       rsDetail.getString("fld_value_in_system")  == null  ? ("")      :       (rsDetail.getString("fld_value_in_system")) );
                    eDetail.setRecordId     (                            recordId                                                                                       );

                    eSummary.addDetails(new Integer(recordId), eDetail);
                    //*******************************************************************
                }//end of inner exception detail data loop
                //Add the child map object to the summary object
                //eSummary.setFieldChangeDetails(eDetMap);
                mSummary.addException(   new Integer(recordId)  , eSummary);
                mSummary.addAffId(       new Integer(recordId)  , affId);
            } //end of outer exception summary data loop

            DBUtil.cleanup(null, ps, rsSummary);
            DBUtil.cleanup(null, ps2, rsDetail);

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsSummary);
        }
        //******************************************************************
        if(recordId < 0){
            return true; //exception exists
        }else{
            return false;//exception doesnot exists
        }

    }//End of getException method
    /***********************************************************************************************/
    /*The following method will get the field Changes from the database and populate the map data       */
    /* structure. This method is used to display the field Changes of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    public boolean getFieldChanges(Integer queuePk, MemberPreUpdateSummary mSummary){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getFieldChanges called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return false;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        final int           MEMBER              =   0; //define this constant for now
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        ResultSet           rsFChanges          =   null;
        HashMap             fChangeMap          =   new HashMap();   //dataStructure to hold the fieldChange
        int                 counter             =   0;
        FieldChange         fChange             =   null;
        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_PRE_FLD_CHG_DETAIL);
            //

            ps.setInt(1, queuePk.intValue());

            rsFChanges                    = ps.executeQuery();



            while (rsFChanges.next()) {
                fChange                   = new FieldChange();
                fChange.setName(          rsFChanges.getString("com_cd_desc")             );
                fChange.setCount(         rsFChanges.getInt("upd_field_chg_cnt")          );
                counter++;
                fChangeMap.put(new Integer(counter), fChange);
            }
            if(counter > 0){
                mSummary.setFieldChanges(fChangeMap);
            }else{
                mSummary.setFieldChanges(null);
            }

        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsFChanges);
        }
        if(counter > 0){
            return true;
        }else{
            return false;
        }
    }//end of getFieldChanges method

    /***********************************************************************************************/
    /*The following method will get the update summary from the database and populate the map data  */
    /* structure. This method is used to display the results of the the upload file process         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Member Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     */
    public MemberUpdateSummary getMemberUpdateSummary(Integer queuePk){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getMemberUpdateSummary called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return null;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        final   int         FILLED              =   1;
        final   int         EMPTY               =   0;
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        ResultSet           rsUSummary          =   null;
        HashMap             uRSumMap            =   new HashMap();   //dataStructure to hold the fieldChange
        int                 counter             =   0;
        PersonReviewData    pRData              =   null;
        PersonReviewData    tCount              =   null;
        MemberUpdateSummary mSummary            =   new MemberUpdateSummary();
        AffiliateIdentifier affId               =   null;
        Integer             affPk               =   null;
        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_REVIEW_SUMMARY);
            //

            ps.setInt(1, queuePk.intValue());

            rsUSummary                    = ps.executeQuery();
            tCount                        = new PersonReviewData();
            while (rsUSummary.next()) {
                affPk                     = new Integer(        rsUSummary.getInt("aff_pk")      );
                affId                     = affilBean.getAffiliateData(affPk).getAffiliateId();

                pRData                    = new PersonReviewData();


                pRData.setAffId(                        affId                                    );
                logger.debug("----------------------------------------------------");
                logger.debug("got AffialiateId===========>" + pRData.getAffId());
                logger.debug("----------------------------------------------------");

                pRData.setTransCompleted(      rsUSummary.getInt("trans_cmpltd_cnt")             );
                pRData.setTransAttempted(      rsUSummary.getInt("trans_attempted_cnt")          );
                pRData.setTransError(          rsUSummary.getInt("trans_err_cnt")                );
                pRData.setAddsCompleted(       rsUSummary.getInt("adds_cmpltd_cnt")              );
                pRData.setAddsAttempted(       rsUSummary.getInt("adds_attempted_cnt")           );
                pRData.setInacCompleted(       rsUSummary.getInt("inactReplace_cmpltd_cnt")      );
                pRData.setInacAttempted(       rsUSummary.getInt("inactReplace_attempted_cnt")   );
                pRData.setChangesCompleted(    rsUSummary.getInt("chg_cmpltd_cnt")               );
                pRData.setChangesAttempted(    rsUSummary.getInt("chg_attempted_cnt")            );
                tCount.addToValues(                         pRData                               );
                logger.debug("<=================total Count=============>");
                logger.debug(tCount);
                logger.debug("<=================total Count=============>");

                counter++;
                logger.debug("----------------------------------------------------");
                logger.debug("pRData = " + pRData + "counter========>" + counter);
                logger.debug("----------------------------------------------------");
                //******************************************************************************
                //get the exceptions for the particular queue and aff_pk and associate it with
                //particular affid
                //******************************************************************************
                logger.debug("<=================Calling getExceptions=============>");
                boolean result  =   getExceptions(affPk, queuePk,   mSummary, affId);
                logger.debug("<=================done Calling getExceptions=============>");

                //******************************************************************************
                uRSumMap.put(new Integer(counter), pRData);
            }
            if(counter > 0){
                logger.debug("----------------------------------------------------");
                logger.debug("setting UpdateSummary");
                logger.debug("----------------------------------------------------");
                counter++;
                mSummary.setTotalCounts(        tCount      );
                mSummary.setAffUpdateSummary(   uRSumMap    );

            }else{
                mSummary.setAffUpdateSummary(null);
            }
        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsUSummary);
        }

        return mSummary;

    }//end of getMemberUpdateSummary() method

    /***********************************************************************************************/
    /*The following method will get the update exception from the database and populate the map data       */
    /* structure. This method is used to display the exceptions of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Member Update exception report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     * @param affPk primary key of the association
     * @param affPk a reference to the MemberUpdateSummary
     * @param affId affiliateIdentifier to associate the exceptions with the data
     */
    public boolean getExceptions(Integer affPk, Integer queuePk, MemberUpdateSummary mSummary, AffiliateIdentifier affId){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getReviewException called with queuePk = " + queuePk );
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1) || (affPk == null || affPk.intValue() < 1) ) {
            return false;
        }
        logger.debug("Passed checking queuepk and affpk " + queuePk);


        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        PreparedStatement   ps2                  =   null;        
        ResultSet           rsRvSummary         =   null;
        ResultSet           rsRvDetail          =   null;
        ExceptionData       eSummary            =   null;
        ExceptionComparison eDetail             =   null;
        HashMap             eSumMap             =   new HashMap();   //dataStructure to hold the parent exception
        HashMap             eDetMap             =   null;           //datastructure to hold the child exception

        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_RV_ERROR_SUMMARY);

            ps.setInt(1, affPk.intValue());
            ps.setInt(2, queuePk.intValue());

            rsRvSummary             = ps.executeQuery();

            while (rsRvSummary.next()) {
                eSummary            = new ExceptionData();
                eSummary.setAffId(              affId                                         );
                eSummary.setFirstName(          rsRvSummary.getString("first_nm")             );
                eSummary.setLastName(           rsRvSummary.getString("last_nm")              );
                eSummary.setMiddleName(         rsRvSummary.getString("middle_nm")            );
                eSummary.setSuffix(             rsRvSummary.getString("suffix_nm")            );
                eSummary.setPersonPk(           new Integer(rsRvSummary.getInt("person_pk"))  );
                eSummary.setRecordId(           new Integer(rsRvSummary.getInt("record_id"))  );
                eSummary.setAffPk(              new Integer(rsRvSummary.getInt("aff_pk"))     );
                eSummary.setUpdateErrorCodePk(  new Integer(rsRvSummary.getInt("fld_error_type")));



                rvRecId    = eSummary.getRecordId().intValue();



                /*************************************************************************/
                /*now that we have the summary get the details of the exception          */
                /*************************************************************************/
                ps2      = con.prepareStatement(SQL_SELECT_RV_ERR_DETAIL);
                //
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, queuePk.intValue());
                ps2.setInt(3, rvRecId);
                //
                logger.debug("executed SQL = " + SQL_SELECT_PRE_ERR_DETAIL);
                //
                rsRvDetail        = ps2.executeQuery();
                while(rsRvDetail.next()){
                    eDetail     = new ExceptionComparison();
                    //*****************************************************************
                    //Set the value in the object based on the int value in the database
                    //*****************************************************************
                    // 0    : False
                    // 1    : True
                    //*****************************************************************

                    if(rsRvDetail.getInt("fld_error_fg") == 0){
                        eDetail.setError(false);
                    } else {
                        eDetail.setError(true);
                    }
                    //************************************************************************
                    eDetail.setField(           rsRvDetail.getString("com_cd_desc")                                                                                     );
                    //                                                  if(true)                      ? this      else    This
                    eDetail.setValueInFile(     rsRvDetail.getString("fld_value_in_file")    == null  ? ("")      :       (rsRvDetail.getString("fld_value_in_file"))   );
                    eDetail.setValueInSystem(   rsRvDetail.getString("fld_value_in_system")  == null  ? ("")      :       (rsRvDetail.getString("fld_value_in_system")) );
                    eDetail.setRecordId(                            rvRecId                                                                                             );




                   logger.debug("----------------------------------------------------");
                    logger.debug(" recordId for detail rec =====> "    + rvRecId);
                    logger.debug("----------------------------------------------------");

                    logger.debug(" Calling addDetials with = "  + eDetail);

                    eSummary.addDetails(new Integer(rvRecId), eDetail);
                    //*******************************************************************
                }//end of inner exception detail data loop
                //Add the child map object to the summary object
                //eSummary.setFieldChangeDetails(eDetMap);
                mSummary.addException(   new Integer(rvRecId)  , eSummary   );
                //mSummary.addAffId(       new Integer(rvRecId)  , affId      );
            } //end of outer exception summary data loop
            DBUtil.cleanup(null, ps, rsRvSummary);
            DBUtil.cleanup(null, ps2, rsRvDetail);

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsRvSummary);
        }
        //******************************************************************
        if(rvRecId < 0){
            return true; //exception exists
        }else{
            return false;//exception doesnot exists
        }

    }//End of getReviewException method

    /***********************************************************************************************/
    /*The following method will get the update summary from the database and populate the map data  */
    /* structure. This method is used to display the results of the the upload file process         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Officer Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     */
    public OfficerPreUpdateSummary getOfficerPreUpdateSummary(Integer queuePk){
        logger.debug("Get Officer Pre Update Summary(Integer queuePk) method entered.");
        logger.debug("queuePk = " + queuePk  );
        
        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return null;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection              con                 =   null;
        PreparedStatement       ps                  =   null;
        ResultSet               rsUSummary          =   null;
        HashMap                 sumMap              =   new HashMap();   //dataStructure to hold the fieldChange
        int                     counter             =   0;
        OfficerChanges          oChanges            =   null;
        OfficerChanges          tCount              =   null;
        OfficerPreUpdateSummary oSummary            =   new OfficerPreUpdateSummary();
        AffiliateIdentifier     affId               =   null;
        Integer                 affPk               =   null;
        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_OFF_PRE_EDIT_SUMMARY);
            //

            ps.setInt(1, queuePk.intValue());

            rsUSummary                    = ps.executeQuery();
            tCount                        = new OfficerChanges();
            while (rsUSummary.next()) {
                affPk                     = new Integer(        rsUSummary.getInt("aff_pk")      );
                
                
                if (affPk != null && affPk.intValue() != 0){
                    affId = affilBean.getAffiliateData(affPk).getAffiliateId();                    
                }else{
                    affId = new AffiliateIdentifier();
                    affId.setType(new Character(rsUSummary.getString("aff_err_aff_type").charAt(0)));                    
                    affId.setLocal((rsUSummary.getString("aff_err_aff_localSubChapter") != null ? (rsUSummary.getString("aff_err_aff_localSubChapter")) : ("")));
                    affId.setState(rsUSummary.getString("aff_err_aff_stateNat_type") != null ? rsUSummary.getString("aff_err_aff_stateNat_type") : "");
                    affId.setSubUnit(rsUSummary.getString("aff_err_aff_subUnit") != null ? rsUSummary.getString("aff_err_aff_subUnit") : "");
                    affId.setCouncil(rsUSummary.getString("aff_err_aff_councilRetiree_chap") != null ? rsUSummary.getString("aff_err_aff_councilRetiree_chap"): "");
               
                }

                oChanges                  = new OfficerChanges();                
                oChanges.setAffPk   (                        affPk                             );
                oChanges.setCards   (      rsUSummary.getInt("off_cards_cnt"        )          );
                oChanges.setAdded   (      rsUSummary.getInt("off_add_cnt"          )          );
                oChanges.setChanged (      rsUSummary.getInt("off_chg_cnt"          )          );
                oChanges.setInFile  (      rsUSummary.getInt("off_file_cnt"         )          );
                oChanges.setInSystem(      rsUSummary.getInt("off_system_cnt"       )          );
                oChanges.setReplaced(      rsUSummary.getInt("off_replaced_cnt"     )          );
                oChanges.setVacant  (      rsUSummary.getInt("off_vacant_cnt"       )          );
                oChanges.setHasError(      rsUSummary.getBoolean("aff_error_fg"     )          );
                oChanges.setHasWarning(    rsUSummary.getBoolean("aff_warning_fg"   )          );
                tCount.addToValues  (                         oChanges                         );

                counter++;
                logger.debug("----------------------------------------------------");
                logger.debug("officer changes = " + oChanges + "counter========>" + counter);
                logger.debug("----------------------------------------------------");
                //******************************************************************************
                //get the exceptions for the particular queue and aff_pk and associate it with
                //particular affid
                 //******************************************************************************
                boolean result  =   getExceptions(affPk, queuePk,   oSummary, affId);

                //******************************************************************************
                //This can be extremly harmfull if serialized JVM won't be able to reconstruct
                //However this is done here as it is done in same way for members and to maintain
                //simlarilty
                sumMap.put(affId, oChanges);
                //*******************************************************************************
            }
            if(counter > 0){
                logger.debug("----------------------------------------------------");
                logger.debug("setting UpdateSummary");
                logger.debug("----------------------------------------------------");
                counter++;
                oSummary.setTotalCounts     (   tCount                  );
                oSummary.setOfficerCounts   (   sumMap                  );
                getFieldChanges             (   queuePk    , oSummary   );
            }else{
                oSummary.setOfficerChanges  (   null       );
            }
        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsUSummary);
        }
        
        logger.debug("Get Officer Pre Update Summary(Integer queuePk) method existed.");
        
        return oSummary;

    }//end of getOfficerPreUpdateSummary() method
    
    
    
    /***********************************************************************************************/
    /*The following method will get the field Changes from the database and populate the map data       */
    /* structure. This method is used to display the field Changes of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    public boolean getFieldChanges(Integer queuePk, OfficerPreUpdateSummary oSummary){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getFieldChanges called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return false;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        ResultSet           rsFChanges          =   null;
        HashMap             fChangeMap          =   new HashMap();   //dataStructure to hold the fieldChange
        int                 counter             =   0;
        FieldChange         fChange             =   null;
        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_OFF_FLD_CHG_DETAIL);
            //

            ps.setInt(1, queuePk.intValue());

            rsFChanges                    = ps.executeQuery();



            while (rsFChanges.next()) {
                fChange                   = new FieldChange();
                fChange.setName (           rsFChanges.getString  ("com_cd_desc"      )      );
                fChange.setCount(           rsFChanges.getInt     ("upd_field_chg_cnt")      );
                counter++;
                fChangeMap.put(new Integer(counter), fChange);
            }
            if(counter > 0){
                oSummary.setFieldChanges(fChangeMap);
            }else{
                oSummary.setFieldChanges(null);
            }

        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsFChanges);
        }
        if(counter > 0){
            return true;
        }else{
            return false;
        }
    }//end of getFieldChanges method
     /***********************************************************************************************/
    /*The following method will get the exception from the database and populate the map data       */
    /* structure. This method is used to display the exceptions of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    public boolean getExceptions(Integer affPk, Integer queuePk, OfficerPreUpdateSummary oSummary, AffiliateIdentifier affId){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getException called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1) || (affPk == null || affPk.intValue() < 1) ) {
            return false;
        }
        logger.debug("Passed checking queuepk and affpk " + queuePk);

        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        PreparedStatement   ps2                  =   null;        
        ResultSet           rsSummary           =   null;
        ResultSet           rsDetail            =   null;
        ExceptionData       eSummary            =   null;
        ExceptionComparison eDetail             =   null;
        HashMap             eSumMap             =   new HashMap();   //dataStructure to hold the parent exception
        HashMap             eDetMap             =   null;           //datastructure to hold the child exception

        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_PRE_ERR_SUMMARY);
            //
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, queuePk.intValue());

            rsSummary       = ps.executeQuery();



            while (rsSummary.next()) {
                eSummary            = new ExceptionData();

                eSummary.setAffId(                          affId                           );
                eSummary.setFirstName(          rsSummary.getString("first_nm")             );
                eSummary.setLastName(           rsSummary.getString("last_nm")              );
                eSummary.setMiddleName(         rsSummary.getString("middle_nm")            );
                eSummary.setSuffix(             rsSummary.getString("suffix_nm")            );
                eSummary.setPersonPk(           new Integer(rsSummary.getInt("person_pk"))  );
                eSummary.setRecordId(           new Integer(rsSummary.getInt("record_id"))  );
                eSummary.setAffPk(              new Integer(rsSummary.getInt("aff_pk"))     );
                eSummary.setUpdateErrorCodePk(  new Integer(rsSummary.getInt("upd_error_type")));

                recordId            = eSummary.getRecordId().intValue();
                logger.debug("added summry" + recordId  + " for :" + eSummary.getFirstName());
                /*************************************************************************/
                /*now that we have the summary get the details of the exception          */
                /*************************************************************************/           
                ps2      = con.prepareStatement(SQL_SELECT_PRE_ERR_DETAIL);
                //
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, queuePk.intValue());
                ps2.setInt(3, recordId);

                rsDetail        = ps2.executeQuery();
                while(rsDetail.next()){
                    eDetail     = new ExceptionComparison();
                    //*****************************************************************
                    //Set the value in the object based on the int value in the database
                    //*****************************************************************
                    // 0    : False
                    // 1    : True
                    //*****************************************************************

                    if(rsDetail.getInt("fld_error_fg") == 0){
                        eDetail.setError(false);
                    } else {
                        eDetail.setError(true);
                    }
                    //************************************************************************
                    eDetail.setField        (       rsDetail.getString("com_cd_desc")                                                                                   );
                    //                                                      if(true)                    ? this      else    This
                    eDetail.setValueInFile  (       rsDetail.getString("fld_value_in_file")    == null  ? ("")      :       (rsDetail.getString("fld_value_in_file"))   );
                    eDetail.setValueInSystem(       rsDetail.getString("fld_value_in_system")  == null  ? ("")      :       (rsDetail.getString("fld_value_in_system")) );
                    eDetail.setRecordId     (                            recordId                                                                                       );

                    eSummary.addDetails(new Integer(recordId), eDetail);
                    //*******************************************************************
                }//end of inner exception detail data loop
                //Add the child map object to the summary object
                //eSummary.setFieldChangeDetails(eDetMap);
                oSummary.addException(   new Integer(recordId)  , eSummary);
                oSummary.addAffId(       new Integer(recordId)  , affId);
            } //end of outer exception summary data loop

            DBUtil.cleanup(null, ps, rsSummary);
            DBUtil.cleanup(null, ps2, rsDetail);

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsSummary);
        }
        //******************************************************************
        if(recordId < 0){
            return true; //exception exists
        }else{
            return false;//exception doesnot exists
        }

    }//End of getException method
    /***********************************************************************************************/
    /*The following method will get the officer position changes from the database                */
    /*  This method is used to display the results of the the officer position changes         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Officer Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     * @param affPk primary key of the file queue that the update summary was generated for
     */
    public Map getOfficerPreOfficeDetails(Integer queuePk, Integer affPk){
        //
        logger.debug("----------------------------------------------------"             );
        logger.debug("getOfficerPreOfficeDetails called with queuePk    = " + queuePk   );
        logger.debug("getOfficerPreOfficeDetails called with affPk      = " + affPk     );

        logger.debug("----------------------------------------------------"             );

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1) || (affPk == null || affPk.intValue() < 1) ) {
            return null;
        }
        logger.debug("Passed checking queuepk and affpk " + queuePk);


        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection              con                 =   null;
        PreparedStatement       ps                  =   null;
        ResultSet               rsPRChanges         =   null;
        HashMap                 prChangesMap        =   new HashMap();   //dataStructure to hold the PositionChange
        int                     counter             =   0;
        PositionChangesResult   prChanges           =   null;
        AffiliateIdentifier     affId               =   null;
        int                     month               =   0;
        int                     year                =   0;
        Calendar                cal                 =   Calendar.getInstance();
        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_OFF_EDIT_DETAIL);
            //
            logger.debug("executing SQL=========> " + SQL_SELECT_OFF_EDIT_DETAIL);

            ps.setString(1, affPk.intValue() + ""   );
            ps.setString(2, queuePk.intValue() + "" );

            rsPRChanges                     = ps.executeQuery();


            while (rsPRChanges.next()) {
                //affPk                     = new Integer(        rsUSummary.getInt("aff_pk")      );
                //affId                     = affilBean.getAffiliateData(affPk).getAffiliateId();
                //tStamp                    = new Timestamp(0);
                prChanges                       =      new PositionChangesResult();
                month                           =      rsPRChanges.getInt("month_of_election"                   );
                year                            =      rsPRChanges.getInt("current_term_end"                    );

                prChanges.setAffPk              (      new Integer(rsPRChanges.getInt("aff_pk")              )   );
                prChanges.setInFile             (      rsPRChanges.getInt("officers_in_file_cnt"             )   );
                prChanges.setAllowed            (      rsPRChanges.getInt("officers_allowed_num"             )   );
                /***********************************************************************************************************/
                                                //       if(true)    Then      this     else    this        and concatenate year
                prChanges.setDate               (      (((month < 10) ?    ("0" + month)   :  ("" + month)) + "/" + year   )   );
                /************************************************************************************************************/
                prChanges.setAffiliateTitle     (      rsPRChanges.getString("affiliate_office_title"         )   );
                prChanges.setConstitutionalTitle(      rsPRChanges.getString("afscme_title_desc"              )   );
                prChanges.setOfficePk           (      new Integer(rsPRChanges.getInt("afscme_office_pk")     )   );
                prChanges.setGroupId            (      new Integer(rsPRChanges.getInt("office_group_id")      )   );
                
                logger.debug("----------------------------------------------------");
                logger.debug("officer changes = " + prChanges + "counter========>" + counter);
                logger.debug("----------------------------------------------------");

                prChangesMap.put(new Integer(counter++), prChanges);
             }
                ps      = con.prepareStatement(SQL_SELECT_ALL_OFF_EDIT_DETAIL);
                //
                logger.debug("executing SQL=========> " + SQL_SELECT_ALL_OFF_EDIT_DETAIL);

                ps.setString(1, affPk.intValue() + ""   );
                
                rsPRChanges                     =       ps.executeQuery();
                boolean foundPos                =       false; 
                PositionChangesResult prChange  =       null;
                while (rsPRChanges.next()) {
                    logger.debug("got in=========> " + rsPRChanges);

                    
                    for(Iterator it = prChangesMap.values().iterator(); it.hasNext();){
                        PositionChangesResult   oldPrChanges    =     (PositionChangesResult) it.next();
                        if(oldPrChanges != null){
                            logger.debug("looking at =========> " + oldPrChanges);
                            int oPk =   rsPRChanges.getInt("afscme_office_pk");
                            int gId =   rsPRChanges.getInt("office_group_id");
                            logger.debug("looking at oPk=========> " + oPk);
                            logger.debug("looking at gId=========> " + gId);
                            int oldOffPk    =   oldPrChanges.getOfficePk()  != null     ? oldPrChanges.getOfficePk().intValue() :   0;
                            int oldGrpId    =   oldPrChanges.getGroupId()   != null     ? oldPrChanges.getGroupId().intValue()  :   0;

                            if((oldPrChanges != null) && ( oldOffPk == oPk) && ( oldGrpId == gId)){
                                    foundPos = true;
                                    logger.debug("set  foundPos=========> " + foundPos);
                            }//end of if
                        }else{
                            foundPos = false;
                        }
                    }//end of for
                    
                    if(!foundPos){
                        prChange                       =      new PositionChangesResult();
                        logger.debug("set  foundPos=========> " + foundPos);
                        month                           =      rsPRChanges.getInt("month_of_election"                   );
                        year                            =      rsPRChanges.getInt("current_term_end"                    );
                        prChange.setAffPk              (      new Integer(rsPRChanges.getInt("aff_pk")              )   );
                        prChange.setInFile             (      0                                                         );
                        prChange.setAllowed            (      rsPRChanges.getInt("max_number_in_office"             )   );
                        /***********************************************************************************************************/
                                                        //       if(true)    Then      this     else    this        and concatenate year
                        prChange.setDate               (      (((month < 10) ?    ("0" + month)   :  ("" + month)) + "/" + year   )   );
                        /************************************************************************************************************/
                        prChange.setAffiliateTitle     (      rsPRChanges.getString("affiliate_office_title"         )   );
                        prChange.setConstitutionalTitle(      rsPRChanges.getString("afscme_title_desc"              )   );
                        prChange.setOfficePk           (      new Integer(rsPRChanges.getInt("afscme_office_pk")     )   );
                        prChange.setGroupId            (      new Integer(rsPRChanges.getInt("office_group_id")      )   );
                        prChangesMap.put(new Integer(counter++), prChange);
                    }//end of if
                    foundPos    =   false;
                    logger.debug("set  foundPos=========> " + foundPos);
                    
                }//end of while
                    
                
        }catch (SQLException se) {
            se.getMessage();
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsPRChanges);
        }

        return prChangesMap;

    }//end of getOfficerPositionChanges() method

    /***********************************************************************************************/
    /*The following method will get the update summary from the database and populate the map data  */
    /* structure. This method is used to display the results of the the upload file process         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Officer Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     */
    public OfficerReviewSummary getOfficerReviewSummary(Integer queuePk){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getOfficerReviewSummary called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return null;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection              con                 =   null;
        PreparedStatement       ps                  =   null;
        ResultSet               rsUSummary          =   null;
        ArrayList               oReviewList         =   new ArrayList();   //dataStructure to hold the OfficerSummary
        ArrayList               excepData           =   new ArrayList();
        int                     counter             =   0;
        OfficerReviewData       oRData              =   null;
        OfficerReviewData       tCount              =   null;
        OfficerReviewSummary    oSummary            =   new OfficerReviewSummary();
        AffiliateIdentifier     affId               =   null;
        Integer                 affPk               =   null;
        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_REVIEW_SUMMARY);
            //

            ps.setInt(1, queuePk.intValue());

            rsUSummary                    = ps.executeQuery();
            tCount                        = new OfficerReviewData();
            while (rsUSummary.next()) {
                affPk                     = new Integer(        rsUSummary.getInt("aff_pk")      );
                affId                     = affilBean.getAffiliateData(affPk).getAffiliateId();

                oRData                    = new OfficerReviewData();


                oRData.setAffId(                        affId                                    );
                //logger.debug("----------------------------------------------------");
                //logger.debug("got AffialiateId===========>" + oRData.getAffId());
                //logger.debug("----------------------------------------------------");

                oRData.setTransCompleted(      rsUSummary.getInt("trans_cmpltd_cnt"             )   );
                oRData.setTransAttempted(      rsUSummary.getInt("trans_attempted_cnt"          )   );
                oRData.setTransError(          rsUSummary.getInt("trans_err_cnt"                )   );
                oRData.setAddsCompleted(       rsUSummary.getInt("adds_cmpltd_cnt"              )   );
                oRData.setAddsAttempted(       rsUSummary.getInt("adds_attempted_cnt"           )   );
                oRData.setInacCompleted(       rsUSummary.getInt("inactReplace_cmpltd_cnt"      )   );
                oRData.setInacAttempted(       rsUSummary.getInt("inactReplace_attempted_cnt"   )   );
                oRData.setChangesCompleted(    rsUSummary.getInt("chg_cmpltd_cnt"               )   );
                oRData.setChangesAttempted(    rsUSummary.getInt("chg_attempted_cnt"            )   );
                oRData.setVacantCompleted(     rsUSummary.getInt("vacant_cmpltd_cnt"            )   );
                oRData.setVacantAttempted(     rsUSummary.getInt("vacant_attempted_cnt"         )   );
                tCount.addToValues(                         oRData                                  );
                //******************************************************************************
                //get the exceptions for the particular queue and aff_pk and associate it with
                //particular affid
                //******************************************************************************
                logger.debug("<=================Calling getExceptions=============>");
                logger.debug("affPk=============>" + affPk);
                logger.debug("queuePk=============>" + queuePk);
                //logger.debug("getExceptions(affPk, queuePk,  affId)===>" + getExceptions(affPk, queuePk,  affId));
                //if(getExceptions(affPk, queuePk,  affId) != null){
                    logger.debug("<=============adding exception data to list =============>" );
                    boolean result = getExceptions(affPk, queuePk,  affId, excepData);
                    logger.debug("<=================excepData.size()=============>" + excepData.size());
                    //excepData.add(counter, e);
                //}
                logger.debug("<=================done Calling getExceptions=============>");

                //******************************************************************************
                oReviewList.add(counter, oRData);
                counter++;
            }
            if(counter > 0){
                logger.debug("----------------------------------------------------");
                logger.debug("setting UpdateSummary");
                logger.debug("----------------------------------------------------");
                counter++;
                oSummary.setTotals              (    tCount                        );

                oSummary.setOfficerReviewData   ( (OfficerReviewData [] )       oReviewList.toArray(new OfficerReviewData[oReviewList.size()]));


                oSummary.setExceptionResult     ( (ExceptionData []     )       excepData.toArray(new ExceptionData[excepData.size()] ));


            }else{
                oSummary.setOfficerReviewData(null);
            }
        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsUSummary);
        }

        return oSummary;

    }//end of getMemberUpdateSummary() method


    /***********************************************************************************************************/
    /***********************************************************************************************/
    /*The following method will get the update exception from the database and populate the map data       */
    /* structure. This method is used to display the exceptions of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Officer Update exception report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     * @param affPk primary key of the association
     * @param affPk a reference to the MemberUpdateSummary
     * @param affId affiliateIdentifier to associate the exceptions with the data
     */
    public boolean getExceptions(Integer affPk, Integer queuePk, AffiliateIdentifier affId, ArrayList al){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getExceptions called with queuePk = " + queuePk );
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1) || (affPk == null || affPk.intValue() < 1) ) {
            return false;
        }
        logger.debug("Passed checking queuepk and affpk " + queuePk);


        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        PreparedStatement   ps2                  =   null;        
        ResultSet           rsRvSummary         =   null;
        ResultSet           rsRvDetail          =   null;
        ExceptionData       eSummary            =   null;
        ExceptionComparison eDetail             =   null;
        ArrayList           eSumMap             =   new ArrayList();   //dataStructure to hold the parent exception
        HashMap             eDetMap             =   null;           //datastructure to hold the child exception

        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_RV_ERROR_SUMMARY);

            ps.setInt(1, affPk.intValue());
            ps.setInt(2, queuePk.intValue());

            rsRvSummary             = ps.executeQuery();

            while (rsRvSummary.next()) {

                eSummary            = new ExceptionData();
                eSummary.setAffId(              affId                                               );
                eSummary.setFirstName(          rsRvSummary.getString("first_nm")                   );
                eSummary.setLastName(           rsRvSummary.getString("last_nm")                    );
                eSummary.setMiddleName(         rsRvSummary.getString("middle_nm")                  );
                eSummary.setSuffix(             rsRvSummary.getString("suffix_nm")                  );
                eSummary.setPersonPk(           new Integer(rsRvSummary.getInt("person_pk"))        );
                eSummary.setRecordId(           new Integer(rsRvSummary.getInt("record_id"))        );
                eSummary.setAffPk(              new Integer(rsRvSummary.getInt("aff_pk"))           );
                eSummary.setUpdateErrorCodePk(  new Integer(rsRvSummary.getInt("fld_error_type"))   );
                rvRecId             = eSummary.getRecordId().intValue();



                /*************************************************************************/
                /*now that we have the summary get the details of the exception          */
                /*************************************************************************/
                ps2  = con.prepareStatement(SQL_SELECT_RV_ERR_DETAIL);
                //
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, queuePk.intValue());
                ps2.setInt(3, rvRecId);
                //
                logger.debug("executed SQL = " + SQL_SELECT_PRE_ERR_DETAIL);
                //
                rsRvDetail = ps2.executeQuery();
                while(rsRvDetail.next()){
                    eDetail         = new ExceptionComparison();
                    //*****************************************************************
                    //Set the value in the object based on the int value in the database
                    //*****************************************************************
                    // 0    : False
                    // 1    : True
                    //*****************************************************************

                    if(rsRvDetail.getInt("fld_error_fg") == 0){
                        eDetail.setError(false);
                    } else {
                        eDetail.setError(true);
                    }
                    //************************************************************************
                    eDetail.setField(           rsRvDetail.getString("com_cd_desc")                                                                                     );
                    //                                                  if(true)                      ? this      else    This
                    eDetail.setValueInFile(     rsRvDetail.getString("fld_value_in_file")    == null  ? ("")      :       (rsRvDetail.getString("fld_value_in_file"))   );
                    eDetail.setValueInSystem(   rsRvDetail.getString("fld_value_in_system")  == null  ? ("")      :       (rsRvDetail.getString("fld_value_in_system")) );
                    eDetail.setRecordId(                            rvRecId                                                                                             );

                    eSummary.addDetails(new Integer(rvRecId), eDetail);

                    //*******************************************************************
                }//end of inner exception detail data loop
                //Add the child map object to the summary object
                logger.debug("----------------------------------------------------");
                al.add(eSummary);
                logger.debug("<====================added smry " + rvRecId + " for==> " + eSummary.getFirstName() );
                logger.debug("----------------------------------------------------");

            } //end of outer exception summary data loop

            DBUtil.cleanup(null, ps, rsRvSummary);
            DBUtil.cleanup(null, ps2, rsRvDetail);

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsRvSummary);
        }
        //******************************************************************
        if(rvRecId < 0){
           return true; //exception exists
        }else{
           return false;//exception doesnot exists
        }

    }//End of getReviewException method

    /***********************************************************************************/
    //Rebate select methods
    //*********************************************************************************
    /***********************************************************************************************/
    /*The following method will get the update summary from the database and populate the map data  */
    /* structure. This method is used to display the results of the the upload file process         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Officer Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     */
    public RebatePreUpdateSummary getRebatePreUpdateSummary(Integer queuePk){
        //
        logger.debug("----------------------------------------------------"         );
        logger.debug("getRebatePreUpdateSummary called with queuePk = " + queuePk  );
        logger.debug("----------------------------------------------------"         );

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return null;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection              con                 =   null;
        PreparedStatement       ps                  =   null;
        ResultSet               rsUSummary          =   null;
        HashMap                 sumMap              =   new HashMap();   //dataStructure to hold the fieldChange
        int                     counter             =   0;
        RebateChanges           rChanges            =   null;
        RebateChanges           tCount              =   null;
        RebatePreUpdateSummary  rSummary            =   new RebatePreUpdateSummary();
        AffiliateIdentifier     affId               =   null;
        Integer                 affPk               =   null;
        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_RBT_PRE_EDIT_SUMMARY);
            //

            ps.setInt(1, queuePk.intValue());

            rsUSummary                    = ps.executeQuery();
            tCount                        = new RebateChanges();
            while (rsUSummary.next()) {
                affPk                     = new Integer(        rsUSummary.getInt("aff_pk")      );

                affId                     = affilBean.getAffiliateData(affPk).getAffiliateId();

                rChanges                  = new RebateChanges();


                rChanges.setAffPk    (                        affPk                             );

                rChanges.setSent     (      rsUSummary.getInt("records_sent_cnt"        )       );
                rChanges.setReceived (      rsUSummary.getInt("records_received_cnt"    )       );
                rChanges.setUnknown  (      rsUSummary.getInt("mbr_unknown_cnt"         )       );
                rChanges.setCouncil  (      rsUSummary.getInt("council_acc_chg_cnt"     )       );
                rChanges.setLocal    (      rsUSummary.getInt("local_acc_chg_cnt"       )       );
                rChanges.setUnchanged(      rsUSummary.getInt("no_chg_cnt"              )       );
                rChanges.setHasError (      rsUSummary.getBoolean("aff_error_fg"        )       );
                rChanges.setHasWarning(     rsUSummary.getBoolean("aff_warning_fg"      )       );
                tCount.addToValues   (                         rChanges                         );

                counter++;
                logger.debug("----------------------------------------------------");
                logger.debug("rebate changes = " + rChanges + "counter========>" + counter);
                logger.debug("----------------------------------------------------");
                //******************************************************************************
                //get the exceptions for the particular queue and aff_pk and associate it with
                //particular affid
                //******************************************************************************
                boolean result  =   getExceptions(affPk, queuePk,   rSummary, affId);

                //******************************************************************************
                //This can be extremly harmfull if serialized JVM won't be able to reconstruct
                //However this is done here as it is done in same way for members and to maintain
                //simlarilty
                sumMap.put(affId, rChanges);
                //*******************************************************************************

            }
            if(counter > 0){
                logger.debug("----------------------------------------------------");
                logger.debug("setting UpdateSummary");
                logger.debug("----------------------------------------------------");
                counter++;
                rSummary.setTotalCounts     (   tCount                  );
                rSummary.setRebateCounts    (   sumMap                  );
                //getFieldChanges             (   queuePk    , rSummary   );
            }else{
                rSummary.setRebateChanges   (   null                     );
            }

        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsUSummary);
        }

        return rSummary;

    }//end of getRebatePreUpdateSummary() method
    /***********************************************************************************************/
    /*The following method will get the exception from the database and populate the map data       */
    /* structure. This method is used to display the exceptions of the upload file process          */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    public boolean getExceptions(Integer affPk, Integer queuePk, RebatePreUpdateSummary rSummary, AffiliateIdentifier affId){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getException called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1) || (affPk == null || affPk.intValue() < 1) ) {
            return false;
        }
        logger.debug("Passed checking queuepk and affpk " + queuePk);

        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection          con                 =   null;
        PreparedStatement   ps                  =   null;
        PreparedStatement   ps2                  =   null;        
        ResultSet           rsSummary           =   null;
        ResultSet           rsDetail            =   null;
        ExceptionData       eSummary            =   null;
        ExceptionComparison eDetail             =   null;
        HashMap             eSumMap             =   new HashMap();   //dataStructure to hold the parent exception
        HashMap             eDetMap             =   null;           //datastructure to hold the child exception

        /************************************************************/
        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_PRE_ERR_SUMMARY);
            //
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, queuePk.intValue());

            rsSummary       = ps.executeQuery();

            while (rsSummary.next()) {
                eSummary            = new ExceptionData();

                eSummary.setAffId(                          affId                           );
                eSummary.setFirstName(          rsSummary.getString("first_nm")             );
                eSummary.setLastName(           rsSummary.getString("last_nm")              );
                eSummary.setMiddleName(         rsSummary.getString("middle_nm")            );
                eSummary.setSuffix(             rsSummary.getString("suffix_nm")            );
                eSummary.setPersonPk(           new Integer(rsSummary.getInt("person_pk"))  );
                eSummary.setRecordId(           new Integer(rsSummary.getInt("record_id"))  );
                eSummary.setAffPk(              new Integer(rsSummary.getInt("aff_pk"))     );
                eSummary.setUpdateErrorCodePk(  new Integer(rsSummary.getInt("upd_error_type")));

                recordId            = eSummary.getRecordId().intValue();

                /*************************************************************************/
                /*now that we have the summary get the details of the exception          */
                /*************************************************************************/             
                ps2 = con.prepareStatement(SQL_SELECT_PRE_ERR_DETAIL);
                //
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, queuePk.intValue());
                ps2.setInt(3, recordId);

                rsDetail        = ps2.executeQuery();
                while(rsDetail.next()){
                    eDetail     = new ExceptionComparison();
                    //*****************************************************************
                    //Set the value in the object based on the int value in the database
                    //*****************************************************************
                    // 0    : False
                    // 1    : True
                    //*****************************************************************

                    if(rsDetail.getInt("fld_error_fg") == 0){
                        eDetail.setError(false);
                    } else {
                        eDetail.setError(true);
                    }
                    //************************************************************************
                    eDetail.setField        (       rsDetail.getString("com_cd_desc")                                                                                   );
                    //                                                      if(true)                    ? this      else    This
                    eDetail.setValueInFile  (       rsDetail.getString("fld_value_in_file")    == null  ? ("")      :       (rsDetail.getString("fld_value_in_file"))   );
                    eDetail.setValueInSystem(       rsDetail.getString("fld_value_in_system")  == null  ? ("")      :       (rsDetail.getString("fld_value_in_system")) );
                    eDetail.setRecordId     (                            recordId                                                                                       );

                    eSummary.addDetails(new Integer(recordId), eDetail);
                    //*******************************************************************
                }//end of inner exception detail data loop
                //Add the child map object to the summary object
                //eSummary.setFieldChangeDetails(eDetMap);
                rSummary.addException(   new Integer(recordId)  , eSummary);
                rSummary.addAffId(       new Integer(recordId)  , affId);
            } //end of outer exception summary data loop

            DBUtil.cleanup(null, ps, rsSummary);
            DBUtil.cleanup(null, ps2, rsDetail);

        } catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsSummary);
        }
        //******************************************************************
        if(recordId < 0){
            return true; //exception exists
        }else{
            return false;//exception doesnot exists
        }

    }//End of getException method

     /***********************************************************************************************/
    /*The following method will get the update summary from the database and populate the map data  */
    /* structure. This method is used to display the results of the the upload file process         */
    /* based on the business rules                                                                  */
    /************************************************************************************************/
    /**
     * Gets a Officer Update Summary report.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     */
    public RebateReviewSummary getRebateReviewSummary(Integer queuePk){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getRebateReviewSummary called with queuePk = " + queuePk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((queuePk == null || queuePk.intValue() < 1)  ) {
            return null;
        }
        logger.debug("Passed checking queuepk " + queuePk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection              con                 =   null;
        PreparedStatement       ps                  =   null;
        ResultSet               rsUSummary          =   null;
        ArrayList               rReviewList         =   new ArrayList();   //dataStructure to hold the rebateSummary
        ArrayList               excepData           =   new ArrayList();
        int                     counter             =   0;
        PersonReviewData        pRData              =   null;
        PersonReviewData        tCount              =   null;
        RebateReviewSummary     rSummary            =   new RebateReviewSummary();
        AffiliateIdentifier     affId               =   null;
        Integer                 affPk               =   null;
        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            ps      = con.prepareStatement(SQL_SELECT_REVIEW_SUMMARY);
            //

            ps.setInt(1, queuePk.intValue());

            rsUSummary                    = ps.executeQuery();
            tCount                        = new PersonReviewData();
            while (rsUSummary.next()) {
                affPk                     = new Integer(        rsUSummary.getInt("aff_pk")      );
                affId                     = affilBean.getAffiliateData(affPk).getAffiliateId();

                pRData                    = new PersonReviewData();


                pRData.setAffId(                        affId                                    );
                //logger.debug("----------------------------------------------------");
                //logger.debug("got AffialiateId===========>" + oRData.getAffId());
                //logger.debug("----------------------------------------------------");

                pRData.setTransCompleted(      rsUSummary.getInt("trans_cmpltd_cnt"             )   );
                pRData.setTransAttempted(      rsUSummary.getInt("trans_attempted_cnt"          )   );
                pRData.setTransError(          rsUSummary.getInt("trans_err_cnt"                )   );
                tCount.addToValues(                         pRData                                  );
                //******************************************************************************
                //get the exceptions for the particular queue and aff_pk and associate it with
                //particular affid
                //******************************************************************************
                logger.debug("<=================Calling getExceptions=============>");
                logger.debug("affPk=============>" + affPk);
                logger.debug("queuePk=============>" + queuePk);
                //logger.debug("getExceptions(affPk, queuePk,  affId)===>" + getExceptions(affPk, queuePk,  affId));
                //if(getExceptions(affPk, queuePk,  affId) != null){
                    logger.debug("<=============adding exception data to list =============>" );
                    boolean result = getExceptions(affPk, queuePk,  affId, excepData);
                    //logger.debug("<=================excepData.size()=============>" + excepData.size());
                    //excepData.add(counter, e);
                //}
                logger.debug("<=================done Calling getExceptions=============>");

                //******************************************************************************
                rReviewList.add(counter, pRData);
                counter++;
            }
            if(counter > 0){
                logger.debug("----------------------------------------------------");
                logger.debug("setting UpdateSummary");
                logger.debug("----------------------------------------------------");
                counter++;
                rSummary.setTotals              (    tCount                        );

                rSummary.setRebateReviewData    ( (PersonReviewData [] )       rReviewList.toArray(new PersonReviewData[rReviewList.size()] ));


                rSummary.setExceptionResult     ( (ExceptionData []     )      excepData.toArray(new ExceptionData[excepData.size()]        ));


            }else{
                rSummary.setRebateReviewData    (null);
            }

        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsUSummary);
        }

        return rSummary;

    }//end of getRebateUpdateSummary() method
    
    /************************************************************************************************/
    /* *** Note: METHODS *** ABOVE *** HERE ARE ALL FOR THE DISPLAY OF THE SCREENS!!                */
    /************************************************************************************************/

    /**********************************************************************************************/

    /**
     *getAffiliatePK gets the affiliate PK
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param personPk The primary key of the officer
     *@return status code value
     */
    public Integer getAffiliatePk(Integer personPk) {
        Integer codeValue       = null;

        Connection con          = DBUtil.getConnection();
        PreparedStatement ps    = null;
        ResultSet rs            = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_OFF_AFFILIATE);
            ps.setInt(1, personPk.intValue());

            rs = ps.executeQuery();

            if (rs.next()){
                codeValue = new Integer(rs.getInt("aff_pk"));
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return codeValue;
    }
    
    /**
     *getAffPk gets the affiliate PK from the aff_organizations table.
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param personPk The primary key of the officer
     *@return aff_pk
     */
    public Integer getAffPkFromAffOrg(Character type, String local, String state, String subUnit, String council){
        logger.debug("Get Aff Pk From Aff Org method is entered");
        Integer pk       = null;
        Connection con          = DBUtil.getConnection();
        PreparedStatement ps    = null;
        ResultSet rs            = null;

        String whereClause      = 
        "where                                                              "   +
        "        aff_type              =   ?                                "   +
        "and     aff_stateNat_type     =   ?                                "   ;        
        int index = 2;
       
        try {
                        
            if (local != null && local.length() > 0) {
                whereClause = whereClause + " and aff_localSubChapter   =   ? ";
            }
            if (council != null && council.length() > 0){
                whereClause = whereClause + " and aff_councilRetiree_chap = ? ";                
            }
            if (subUnit != null && subUnit.length() > 0){
                whereClause = whereClause + " and aff_subUnit = ? ";
            }
                                
            ps = con.prepareStatement(SQL_SELECT_AFF_PK_FROM_AFF_ORG + whereClause);                
            DBUtil.setNullableChar(ps, 1, type);
            DBUtil.setNullableVarchar(ps, 2, state);                            

            if (local != null && local.length() > 0) {
                DBUtil.setNullableVarchar(ps, index+=1, local);
            }
            if (council != null && council.length() > 0){
                DBUtil.setNullableVarchar(ps, index+=1, council);                
            }
            if (subUnit != null && subUnit.length() > 0){
                DBUtil.setNullableVarchar(ps, index+=1, subUnit);                                            
            }       
            
            rs = ps.executeQuery();

            if (rs.next()){
                pk = new Integer(rs.getInt("aff_pk"));
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }        
        logger.debug("Get Aff Pk From Aff Org method is existed");        
        return pk;
    }
    /***********************************************************************************/
    /**
     * reads an affiliate flag that indicates whether it is suppose to have sub locals
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="supports"
     *
     *@param affPk Primary key for affiliate org
     *@return boolean flag
     */
    public boolean subLocalsAllowed(Integer affPk) {
        boolean codeValue       = false;

        Connection con          = DBUtil.getConnection();
        PreparedStatement ps    = null;
        ResultSet rs            = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_UNA_AFFILIATE);
            ps.setInt(1, affPk.intValue());

            rs = ps.executeQuery();

            if (rs.next()){
                codeValue = rs.getBoolean("aff_sub_locals_allowed_fg");
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return codeValue;
    }
    /***********************************************************************************/
    /**
     * reads an affiliate flag that indicates whether it is suppose to have sub locals
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="supports"
     *
     *@param affPk Primary key for affiliate org
     *@return boolean flag
     */
    public Integer officerTitleExists(Integer title) {
        Integer             posValue    = null;
        Connection          con         = DBUtil.getConnection();
        PreparedStatement   ps          = null;
        ResultSet           rs          = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_OFF_TITLE);
            ps.setString(1, title.toString());
            rs = ps.executeQuery();
            if (rs.next()){
                posValue       = new Integer(rs.getString("afscme_title_nm"));
                       
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return posValue;
    }
    

    
    //********************************************************************************
    /**
     *getAffiliatePK gets the affiliate PK
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param personPk The primary key of the officer
     *@return status code value
     */
    public Integer getStaffPk(Integer personPk) {
        Integer codeValue       = null;

        Connection con          = DBUtil.getConnection();
        PreparedStatement ps    = null;
        ResultSet rs            = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_STAFF_AFFILIATE);
            ps.setInt(1, personPk.intValue());

            rs = ps.executeQuery();

            if (rs.next()){
                codeValue = new Integer(rs.getInt("aff_pk"));
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return codeValue;
    }
//********************************************************************************
    /**
     *getAffiliatePK gets the affiliate PK
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param personPk The primary key of the officer
     *@return status code value
     */
    public Integer getAFSCMEStaffDept(Integer personPk) {
        Integer codeValue       = null;

        Connection con          = DBUtil.getConnection();
        PreparedStatement ps    = null;
        ResultSet rs            = null;

        try {
            ps = con.prepareStatement(SQL_SELECT_AFSCME_STAFF);
            ps.setInt(1, personPk.intValue());

            rs = ps.executeQuery();

            if (rs.next()){
                codeValue = new Integer(rs.getInt("dept"));
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return codeValue;
    }

    /***********************************************************************************/

    /**
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *@return Map
     */
    
    public HashMap getPositionDetailsForAnOfficer(Integer affPk, Integer personPk) {
        logger.debug("Get Position Details For An Officer method is entered.");

        HashMap posDtl          =   new HashMap();
        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;

        try {
            ps = con.prepareStatement(SQL_SELECT_OFF_POS_DETAILS);

            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue()   );

            rs = ps.executeQuery();

            if (rs.next()){
                posDtl.put(new Integer(1), new Boolean((rs.getBoolean (   "pos_steward_fg"          ))                                                                ));
                posDtl.put(new Integer(2), new Integer((rs.getInt     (   "afscme_title_nm"         ) != 0    )?  rs.getInt     (   "afscme_title_nm"     ) : 0       ));
                posDtl.put(new Integer(3), new String ((rs.getString  (   "afscme_title_desc"       ) != null )?  rs.getString  (   "afscme_title_desc"   ) : ""      ));
                posDtl.put(new Integer(4), new Integer((rs.getInt     (   "current_term_end"        ) != 0    )?  rs.getInt     (   "current_term_end"    ) : 0       ));
                posDtl.put(new Integer(5), new Integer((rs.getInt     (   "month_of_election"       ) != 0    )?  rs.getInt     (   "month_of_election"   ) : 0       ));
                posDtl.put(new Integer(6), new Integer((rs.getInt     (   "max_number_in_office"    ) != 0    )?  rs.getInt     (   "max_number_in_office") : 0       ));
                posDtl.put(new Integer(7), new Integer((rs.getInt     (   "afscme_office_pk"        ) != 0    )?  rs.getInt     (   "afscme_office_pk"    ) : 0       ));
                posDtl.put(new Integer(8), new Integer((rs.getInt     (   "office_group_id"         ) != 0    )?  rs.getInt     (   "office_group_id"     ) : 0       ));
                posDtl.put(new Integer(9), new Integer((DBUtil.getIntegerOrNull(rs, "position_mbr_affiliation") != null    )?  rs.getInt     (   "office_group_id"     ) : 0       ));
            }
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        logger.debug("Get Position Details For An Officer method is existed.");
        return posDtl;
    }    
    
    
    /***********************************************************************************/

    /**
     *getOffCurrentPositionDetails gets Map of position details
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param personPk The primary key of the affiliate
     *@param personPk The primary key of the officer
     *@return Map
     */
    public Map getOffCurrentPositionDetails(Integer affPk, Integer personPk) {
        //********************************************************************
        //Define local vars
        boolean stFlag          =   false;
        int     posCode         =   0;
        boolean success         =   false;
        HashMap posDtl          =   new HashMap();

        //*********************************************************************

        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;

        try {
                        
            ps = con.prepareStatement(SQL_SELECT_OFF_POS_DETAILS);

            ps.setInt(1, personPk.intValue());
            ps.setInt(2, affPk.intValue()   );

            rs = ps.executeQuery();

            if (rs.next()){

                success = true;
                posDtl.put(new Integer(1), new Boolean((rs.getBoolean (   "pos_steward_fg"          ))                                                                ));
                posDtl.put(new Integer(2), new Integer((rs.getInt     (   "afscme_title_nm"         ) != 0    )?  rs.getInt     (   "afscme_title_nm"     ) : 0       ));
                posDtl.put(new Integer(3), new String ((rs.getString  (   "afscme_title_desc"       ) != null )?  rs.getString  (   "afscme_title_desc"   ) : ""      ));
                posDtl.put(new Integer(4), new Integer((rs.getInt     (   "current_term_end"        ) != 0    )?  rs.getInt     (   "current_term_end"    ) : 0       ));
                posDtl.put(new Integer(5), new Integer((rs.getInt     (   "month_of_election"       ) != 0    )?  rs.getInt     (   "month_of_election"   ) : 0       ));
                posDtl.put(new Integer(6), new Integer((rs.getInt     (   "max_number_in_office"    ) != 0    )?  rs.getInt     (   "max_number_in_office") : 0       ));
                posDtl.put(new Integer(7), new Integer((rs.getInt     (   "afscme_office_pk"        ) != 0    )?  rs.getInt     (   "afscme_office_pk"    ) : 0       ));
                posDtl.put(new Integer(8), new Integer((rs.getInt     (   "office_group_id"         ) != 0    )?  rs.getInt     (   "office_group_id"     ) : 0       ));
                posDtl.put(new Integer(9), new Integer((DBUtil.getIntegerOrNull(rs, "position_mbr_affiliation") != null    )?  rs.getInt     (   "office_group_id"     ) : 0       ));
                
            }

/*
            //if(!success){
                logger.debug("executing SQL_SELECT_OFF_POS_DETAILS previous sql didn't find data");
                ps = con.prepareStatement(SQL_SELECT_OFFICE_AND_OFFICER_INFO);

                ps.setInt(1, titleCode.intValue());
                ps.setInt(2, year);
                ps.setInt(3, month);
                ps.setInt(4, affPk.intValue());                
                

                rs = ps.executeQuery();
                
                //if (rs.next()){
                while (rs.next()){                    
                    HashMap posDtl          =   new HashMap();                    
                    posDtl.put(new Integer(1), new Boolean((rs.getBoolean (   "pos_steward_fg" ) != null ) ? rs.getBoolean(    "pos_steward_fg" ) : false   ));
                    posDtl.put(new Integer(2), new Integer((rs.getInt     (   "title_code"     ) != 0    )?  rs.getInt     (   "title_code"     ) : 0       ));
                    posDtl.put(new Integer(3), new String ((rs.getString  (   "afscme_title_desc"   ) != null )?  rs.getString  (   "afscme_title_desc"   ) : ""      ));
                    posDtl.put(new Integer(4), new Integer((rs.getInt     (   "current_term_end"    ) != 0    )?  rs.getInt     (   "current_term_end"    ) : 0       ));
                    posDtl.put(new Integer(5), new Integer((rs.getInt     (   "election_month"   ) != 0    )?  rs.getInt     (   "election_month"   ) : 0       ));
                    posDtl.put(new Integer(6), new Integer((rs.getInt     (   "max_number_in_office") != 0    )?  rs.getInt     (   "max_number_in_office") : 0       ));
                    posDtl.put(new Integer(7), new Integer((rs.getInt     (   "afscme_office_pk"    ) != 0    )?  rs.getInt     (   "afscme_office_pk"    ) : 0       ));
                    posDtl.put(new Integer(8), new Integer((rs.getInt     (   "office_group_id"     ) != 0    )?  rs.getInt     (   "office_group_id"     ) : 0       ));
                    posDtl.put(new Integer(9), new Integer((rs.getInt     (   "person_pk"           ) != 0    )?  rs.getInt     (   "person_pk"             ) : 0       ));

                    
                    list.add(posDtl);

                }
            //}
 */
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return posDtl;
    }

    
    
    /**
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     */    
    public List getOfficersInOffice(Integer affPk, int month, int year, Integer titleCode){
        logger.debug("Get OfficersInOffice method is entered.");
        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;
        List list               =   new ArrayList();

        try {

                ps = con.prepareStatement(SQL_SELECT_OFFICE_AND_OFFICER_INFO);

                ps.setInt(1, titleCode.intValue());
                ps.setInt(2, year);
                ps.setInt(3, month);
                ps.setInt(4, affPk.intValue());                
                
                rs = ps.executeQuery();
               
                while (rs.next()){                    
                    HashMap posDtl = new HashMap();                    
                    posDtl.put(new Integer(1), new Boolean((rs.getBoolean (   "pos_steward_fg"          ))                                                                ));
                    posDtl.put(new Integer(2), new Integer((rs.getInt     (   "title_code"     ) != 0    )?  rs.getInt     (   "title_code"     ) : 0       ));
                    posDtl.put(new Integer(3), new String ((rs.getString  (   "afscme_title_desc"   ) != null )?  rs.getString  (   "afscme_title_desc"   ) : ""      ));
                    posDtl.put(new Integer(4), new Integer((rs.getInt     (   "current_term_end"    ) != 0    )?  rs.getInt     (   "current_term_end"    ) : 0       ));
                    posDtl.put(new Integer(5), new Integer((rs.getInt     (   "election_month"   ) != 0    )?  rs.getInt     (   "election_month"   ) : 0       ));
                    posDtl.put(new Integer(6), new Integer((rs.getInt     (   "max_number_in_office") != 0    )?  rs.getInt     (   "max_number_in_office") : 0       ));
                    posDtl.put(new Integer(7), new Integer((rs.getInt     (   "afscme_office_pk"    ) != 0    )?  rs.getInt     (   "afscme_office_pk"    ) : 0       ));
                    posDtl.put(new Integer(8), new Integer((rs.getInt     (   "office_group_id"     ) != 0    )?  rs.getInt     (   "office_group_id"     ) : 0       ));
                    posDtl.put(new Integer(9), new Integer((rs.getInt     (   "person_pk"           ) != 0    )?  rs.getInt     (   "person_pk"             ) : 0       ));
                    posDtl.put(new Integer(10),rs.getDate("pos_end_dt") != null ? "DateFound" : "" );                    
                    
                    list.add(posDtl);
                }            
        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        logger.debug("Get OfficersInOffice method is existed.");
        return list;    
    }
    
    /***********************************************************************************************/
    /*The following method will get the officers per affiliate from the database and                 */
    /* return the count         */

    /************************************************************************************************/
    /**
     * Gets a Officer count per affiliate .
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @param queuePk primary key of the file queue that the update summary was generated for
     */
    public int getOfficerCount(Integer affPk){
        //
        logger.debug("----------------------------------------------------");
        logger.debug("getOfficerCount called with queuePk = " + affPk);
        logger.debug("----------------------------------------------------");

        //**********************************************************
        //Make sure the values are passed for the keys
        //*********************************************************
        if ((affPk == null || affPk.intValue() < 1)  ) {
            return 0;
        }
        logger.debug("Passed checking affPk " + affPk);
        //**********************************************************
        //define the local variables
        //**********************************************************
        Connection              con                 =   null;
        PreparedStatement       ps                  =   null;
        ResultSet               rsUSummary          =   null;

        int                     counter             =   0;

        /************************************************************/

        try {
            con     = DBUtil.getConnection();
            logger.debug("Executing sql=========>" + SQL_SELECT_AFFILIATE_OFFICERS);
            ps      = con.prepareStatement(SQL_SELECT_AFFILIATE_OFFICERS);
            //

            ps.setInt(1, affPk.intValue());

            rsUSummary                    = ps.executeQuery();

            while (rsUSummary.next()) {

                counter++;
            }


        }catch (SQLException se) {
            throw new EJBException(se);
        } finally {
            DBUtil.cleanup(con, ps, rsUSummary);
        }

        return counter;

    }//end of getOfficerCount() method
    
    /***********************************************************************************/

    /**
     *getOffCurrentPositionDetails gets Map of position details
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param affPk The primary key of the affiliate
     *@return Map
     */
    public Map getAffOfficers(Integer affPk) {
        //********************************************************************
        //Define local vars
        boolean stFlag          =   false;
        int     posCode         =   0;
        HashMap offDtl          =   null;
        HashMap affOff          =   new HashMap();
        int     counter         =   0;
        //*********************************************************************

        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;

        try {
            ps = con.prepareStatement(SQL_SELECT_AFF_OFFICERS);

            
            ps.setInt(1, affPk.intValue()   );

            rs = ps.executeQuery();

            while (rs.next()){ 
                offDtl  =   new HashMap();
                offDtl.put(new Integer(1), new Integer((rs.getInt     (   "person_pk"            ) != 0    )?  rs.getInt     (   "person_pk"       ) : 0       ));
                offDtl.put(new Integer(2), new Integer((rs.getInt     (   "aff_pk"               ) != 0    )?  rs.getInt     (   "aff_pk"          ) : 0       ));
                offDtl.put(new Integer(3), new String ((rs.getString  (   "ssn"                  ) != null )?  rs.getString  (   "ssn"             ) : ""      ));
                offDtl.put(new Integer(4), new String ((rs.getString  (   "first_nm"             ) != null )?  rs.getString  (   "first_nm"        ) : ""      ));
                offDtl.put(new Integer(5), new String ((rs.getString  (   "last_nm"              ) != null )?  rs.getString  (   "last_nm"         ) : ""      ));                                                                      
                
                offDtl.put(new Integer(6), new Integer((DBUtil.getIntegerOrNull(rs, "position_mbr_affiliation") != null )?  rs.getInt     (   "aff_pk"          ) : 0));
                affOff.put(new Integer(counter++), offDtl);
            }

        }
        catch (SQLException se) {
            throw new EJBException(se);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return affOff;
    }
    
    /**
     *getOffCurrentPositionDetails gets Map of position details
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param affPk The primary key of the affiliate
     *@return Map
     */
    public Integer getPrimaryAddressPk(Integer personPk) {
        //********************************************************************
        //Define local vars

        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;
        //*********************************************************************
        try {
            ps = con.prepareStatement(SQL_SELECT_PRIMARY_ADDRESS_PK);

            ps.setInt(1, personPk.intValue());
            

            rs = ps.executeQuery();

            if (rs.next()){ 
                return new Integer(rs.getInt     (   "address_pk"            ));
            }
        }catch (SQLException se) {
            throw new EJBException(se);
        }finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return null;
    }
    
    /**
     *getOffCurrentPositionDetails gets Map of position details
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param affPk The primary key of the affiliate
     *@return Map
     */
    public int execUpdate(String sql, Integer personPk, Integer affPk, int imonth, int iyear, Integer titleCode) {
        logger.debug("execUpdate method is entered. ");        
        //********************************************************************
        //Define local vars

        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;
        int     rows            =   0;
        int     month           =   0;
        int     year            =   0;
        String  length          =   "";
        boolean success         =   false;
        //*********************************************************************
        try {
            
            if(sql.equals(SQL_UPDATE_TERM_END)){
                
                ps = con.prepareStatement(SQL_SELECT_TERM_END);

                ps.setInt(1, personPk.intValue());
                ps.setInt(2, affPk.intValue());
                rs = ps.executeQuery();

                if (rs.next()){ 
                     logger.debug("Select term end statement was successful using personPk and affPk.");
                     success = true;
                     length = rs.getString  (   "length_of_term"      );
                     month  = rs.getInt     (   "month_of_election"   );
                     year   = rs.getInt     (   "current_term_end"    );
                }
                if(!success){
                    logger.debug("executing SQL_SELECT_OFFICE_PK previous sql didn't find data");
                    ps = con.prepareStatement(SQL_SELECT_OFFICE_PK_NEW);
                    ps.setInt(1, titleCode.intValue());
                    ps.setInt(2, iyear);
                    ps.setInt(3, imonth);
                    ps.setInt(4, affPk.intValue());                
                    rs = ps.executeQuery();
                    if (rs.next()){
                        length = rs.getString  (   "length_of_term"      );
                        month  = rs.getInt     (   "election_month"   );
                        year   = rs.getInt     (   "current_term_end"    );
                        
                    }
                    
                }
                logger.debug("personPk====>" + personPk);
                logger.debug("affPk====>" + affPk);
                logger.debug("year====>" + year);
                logger.debug("length====>" + length);
                logger.debug("month====>" + month);
                if(length != null){
                    length = length.substring(4);
                    logger.debug("length after parsing=========>" + length);
                }else{
                    length = "1";
                }
                logger.debug("new Integer(length).intValue()===>" + new Integer(length).intValue());
                if(new Integer(length).intValue() > 4){
                    logger.debug("previously length ==>" + length);
                    logger.debug("setting length to zero");
                    length  =   "0";
                }
                year =  year + new Integer(length).intValue();
                String date = month + "/" + "01" + "/" + year ;
                logger.debug("date==>" + date);
                ps = con.prepareStatement(sql);
                ps.setString(1, date);
                ps.setInt(2, personPk.intValue());
                ps.setInt(3, affPk.intValue());
                rows = ps.executeUpdate();
            }else{
                ps = con.prepareStatement(sql);
                ps.setInt(1, personPk.intValue());
                ps.setInt(2, affPk.intValue());
                rows = ps.executeUpdate();
            }

            
        }catch (SQLException se) {
            throw new EJBException(se);
        }finally {
            DBUtil.cleanup(con, ps, null);
        }
        logger.debug("execUpdate method is existed. ");                
        return rows;
    }
    /**
     *getOffCurrentPositionDetails gets Map of position details
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@param affPk The primary key of the affiliate
     *@return Map
     */
    public int execInsert(Integer personPk, Integer affPk, int currentTermEnd, int titleCode, int month, Integer userPk, Integer posMbrAff) {
        logger.debug("execInsert method is entered. ");
        //********************************************************************
        //Define local vars

        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;
        int     groupId         =   0;
        int     officePk        =   0;
        boolean success         =   false;
        int     rows            =   0;
        int     max_allowed     =   0;
        //*********************************************************************
        try {
                logger.debug("personPk======>" + personPk);
                logger.debug("affPk======>" + affPk);
                logger.debug("titleCode======>" + titleCode);
                logger.debug("currentTermEnd======>" + currentTermEnd);
                logger.debug("month======>" + month);            
                logger.debug("userPk======>" + userPk);            
                
                ps = con.prepareStatement(SQL_SELECT_OFFICE_PK_NEW);

                ps.setInt(1, titleCode);
                ps.setInt(2, currentTermEnd);
                ps.setInt(3, month);
                ps.setInt(4, affPk.intValue());
                rs = ps.executeQuery();

                if (rs.next()){ 
                     officePk = rs.getInt     (   "afscme_office_pk"      );
                     groupId  = rs.getInt     (   "office_group_id"   );
                     logger.debug("officePk======>" + officePk);
                     logger.debug("groupId======>" + groupId);
                     
                     max_allowed = rs.getInt("max_number_in_office");
                     
                }else
                {
                    return 0;
                }
                ps = con.prepareStatement(SQL_INSERT_OFFICER_HIS);
                //logger.debug("SQL_INSERT_OFFICER_HIS======>" + SQL_INSERT_OFFICER_HIS);
                //logger.debug("personPk======>" + personPk);
                //logger.debug("affPk======>" + affPk);
                //logger.debug("userPk======>" + userPk);
                
                ps.setInt(1, affPk.intValue());
                ps.setInt(2, personPk.intValue());
                ps.setInt(3, groupId);
                ps.setInt(4, officePk);
                DBUtil.setNullableInt(ps, 5, posMbrAff);
                //ps.setInt(5, posMbrAff.intValue());                
                ps.setInt(6, userPk.intValue());
                ps.setInt(7, userPk.intValue());
               
                rows = ps.executeUpdate();
                logger.debug("rows inside execInsert======>" + rows);
        }catch (SQLException se) {
            throw new EJBException(se);
        }finally {
            DBUtil.cleanup(con, ps, null);
        }
        logger.debug("returning rows from execInsert======>" + rows);
        logger.debug("execInsert method is existed. ");                
        return rows;
    }
    /**
     *
     *@ejb:interface-method view-type="local"
     *@ejb:transaction type="Supports"
     *
     *@return String
     */    
    public String getTitleDesc(Integer affPk, int currentTermEnd, int titleCode, int month){
        logger.debug("Get Title Desc method is entered. ");                
        
        Connection con          =   DBUtil.getConnection();
        PreparedStatement ps    =   null;
        ResultSet rs            =   null;
        String desc             =   "";
        
        try{
            ps = con.prepareStatement(SQL_SELECT_OFFICE_PK_NEW);
            ps.setInt(1, titleCode);
            ps.setInt(2, currentTermEnd);
            ps.setInt(3, month);
            ps.setInt(4, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()){ 
                 desc = rs.getString(   "afscme_title_desc"      );
                 logger.debug("title desc = " + desc);
            }
        }catch (SQLException se) {
            throw new EJBException(se);
        }finally {
            DBUtil.cleanup(con, ps, null);
        }                
        
        logger.debug("Get Title Desc method is existed. ");                        
        return desc;
    }
}