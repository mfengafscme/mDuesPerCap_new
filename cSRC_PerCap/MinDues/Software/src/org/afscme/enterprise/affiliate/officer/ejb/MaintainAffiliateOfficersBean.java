package org.afscme.enterprise.affiliate.officer.ejb;


// AFSCME Enterprise Imports
import org.afscme.enterprise.affiliate.ejb.*;
import org.afscme.enterprise.affiliate.officer.*;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.common.SortData;
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
import org.afscme.enterprise.officer.*;
import org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance;
import org.afscme.enterprise.affiliate.officer.ReplaceOfficerResults;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;

// Java Imports
import java.util.GregorianCalendar;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import java.util.Calendar;

// Other Imports 
import org.apache.log4j.Logger;

/**
 * Encapsulates access to affiliate data.
 *
 * @ejb:bean name="MaintainAffiliateOfficers" display-name="MaintainAffiliateOfficers"
 * jndi-name="MaintainAffiliateOfficers"
 * type="Stateless" view-type="local"
 */
public class MaintainAffiliateOfficersBean extends SessionBase {
    
    static Logger logger = Logger.getLogger(MaintainAffiliateOfficersBean.class);

    /** Reference to the MaintainOrganizations EJB */
    private MaintainOrganizations orgsBean;
    
    /** Reference to the MaintainCodes EJB */
    private MaintainCodes codesBean;
    
    /** Reference to the MaintainCodes EJB */
    private MaintainAffiliates affilBean;

    
    /** Select if the given officer is already holding a position in a given Affiliate except Steward. 
     They can, however, hold positions at different levels within the Affiliate's Hierarchy */
    private static final String SQL_SELECT_ELIGIBLE_MAINTAIN_OFFICER = 
        "SELECT 1                                           " + 
        "FROM   officer_history h                           " +
        "   INNER JOIN afscme_offices  a                    " +
        "   ON    h.afscme_office_pk = a.afscme_office_pk   " +
        "   AND   a.afscme_title_nm != 6046                 " +
        "WHERE  h.person_pk=?                               " +
        "AND    h.aff_pk=?                                  " + 
        "AND    h.pos_end_dt IS NULL                        ";
    
    
    /** Selects the most recent comment from Aff_Office_Comments for an Affiliate */
    private static final String SQL_SELECT_MOST_RECENT_COMMENT_FOR_OFFICES = 
        "SELECT     comment_txt,                                " +
        "           comment_dt,                                 " +
        "           created_user_pk                             " +
        "FROM       Aff_Office_Comments                         " + 
        "WHERE      (aff_pk = ?)                                " + 
        "AND        (comment_dt =                               " +
        "               (SELECT     MAX(comment_dt)             " +
        "                FROM       Aff_Office_Comments         " + 
        "                WHERE      aff_pk = ?)                 " +
        "           )                                           ";
   
    private static final String SQL_SELECT_COMMENTS_FOR_OFFICER_TITLES = 
        "SELECT   *                                               " +
        "FROM     Aff_Office_Comments                             " +
        "WHERE    aff_pk=?                                        " + 
        "ORDER BY comment_dt DESC                                 ";
    
   /** Inserts a new Comment for Affiliate Officer Titles */
    private static final String SQL_INSERT_COMMENT_FOR_OFFICER_TITLES =
        "INSERT INTO Aff_Office_Comments                           " +
        "	     (aff_pk,                                      " +
        "             created_user_pk,                             " +
        "             comment_txt,                                 " + 
        "             comment_dt)                                  " +
        "VALUES	(?, ?, ?, getDate())                               "; 
    
    private static final String SQL_RESET_REPORTING_OFFICER_FLAGS =
        "UPDATE Aff_Officer_Groups                              " + 
        "SET    reporting_officer_fg = 0                        " +
        "WHERE  aff_pk = ?                                      ";
     
    private static final String SQL_UPDATE_AUTO_EBOARD_TITLES =
        "UPDATE aff_organizations                               " +
        "SET    auto_eboard_parent_title =                      " +
        "       (select afscme_office_pk                        " +
        "        from AFSCME_Offices                            " + 
        "        where afscme_title_nm = ?),                    " +
        "       auto_eboard_aff_title  =                        " +
        "       (select afscme_office_pk                        " +
        "        from AFSCME_Offices                            " + 
        "        where afscme_title_nm = ?),                    " +
        "       lst_mod_user_pk = ?,                            " +
        "       lst_mod_dt = getDate()                          " +
        "WHERE  aff_pk = ?                                      ";
                
    /** */
    private static final String SQL_INSERT_OFFICER_TITLE = 
        "INSERT INTO Aff_Officer_Groups                         " + 
        "       (aff_pk,                                        " +
        "       afscme_office_pk,                               " +
        "       office_group_id,                                " +
        "       max_number_in_office,                           " + 
        "       month_of_election,                              " +
        "       affiliate_office_title,                         " +
        "       executive_board_fg,                             " + 
        "       reporting_officer_fg,                           " +
        "       delegate_priority,                              " +
        "       length_of_term,                                 " + 
        "       current_term_end,                               " +
        "       created_user_pk,                                " +
        "       lst_mod_user_pk,                                " +
        "       created_dt,                                     " +
        "       lst_mod_dt )                                    " +
        "VALUES                                                 " +
        "       (?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        ?,                                             " +
        "        getDate(),                                     " +
        "        getDate())                                     ";
    
    /** */
    private static final String SQL_UPDATE_OFFICER_TITLES = 
        "UPDATE Aff_Officer_Groups                              " + 
        "SET    max_number_in_office = ?,                       " +
        "       month_of_election = ?,                          " +
        "       affiliate_office_title = ?,                     " +
        "       executive_board_fg = ?,                         " +
        "       reporting_officer_fg =  ?,                      " +
        "       delegate_priority = ?,                          " +
        "       length_of_term = ?,                             " +
        "       current_term_end = ?,                           " +
        "       lst_mod_user_pk = ?,                            " +
        "       lst_mod_dt = getDate()                          " +                                    
        "WHERE  aff_pk=?                                        " + 
        "AND    afscme_office_pk=?                              " + 
        "AND    office_group_id =?                              ";
    
    /** */
    private static final String SQL_SELECT_MAX_GROUP_FOR_OFFICE = 
        "SELECT office_group_id                                 " + 
        "FROM   Aff_Officer_Groups                              " + 
        "WHERE  aff_pk=?                                        " + 
        "AND    afscme_office_pk=?                              " + 
        "AND    office_group_id =                               " + 
        "       (SELECT     MAX(office_group_id)                " + 
        "        FROM       Aff_Officer_Groups                  " +
        "        WHERE      aff_pk=?                            " +
        "        AND        afscme_office_pk=?)                 ";
   
    private static final String SQL_GET_AFSCME_OFFICER_TITLES = 
        "SELECT   afscme_office_pk,                             " +
        "         afscme_title_nm                               " +
        "FROM     AFSCME_Offices                                " +
        "ORDER BY priority                                      ";
   
   /** */
    private static final String SQL_IS_OFFICER_TITLE_REMOVABLE =
        "SELECT     office_group_id                             " +
        "FROM       Officer_History                             " +
        "WHERE      aff_pk = ?                                  " +
        "AND        afscme_office_pk =                          " +
        "           (select afscme_office_pk                    " +
        "            FROM AFSCME_Offices                        " + 
        "            WHERE afscme_title_nm = ?)                 " + 
        "AND        office_group_id = ?                         ";   
    
   /** */
    private static final String SQL_CHECK_FOR_PRESIDENT =
        "SELECT     office_group_id                             " +
        "FROM       Aff_Officer_Groups                          " +
        "WHERE      aff_pk = ?                                  " +
        "AND        afscme_office_pk = 9                        ";
        
    private static final String SQL_GET_OFFICER_HISTORY =
       "SELECT   title,                                         " +
       "         full_name,                                     " +
       "         pos_start_dt,                                  " +
       "         pos_end_dt,                                    " +
       "         v_person.person_pk as personpk, aff_pk         " +
       "FROM     V_Officer INNER JOIN V_Person ON               " +  
       "         V_Officer.person_pk = V_Person.person_pk       " +
       "WHERE    aff_pk = ?                                     " +
       "ORDER BY pos_start_dt desc                              ";
  
    private static final String SQL_GET_OFFICER_TITLES =
       "SELECT groups.aff_pk,                                    " +
       "       groups.afscme_office_pk,                          " +
       "       groups.office_group_id,                           " +
       "       groups.affiliate_office_title,                    " + 
       "       groups.length_of_term,                            " + 
       "       groups.max_number_in_office,                      " +
       "       groups.month_of_election,                         " +
       "       groups.current_term_end,                          " +
       "       groups.delegate_priority,                         " +
       "       groups.executive_board_fg,                        " +
       "       groups.reporting_officer_fg,                      " +
       "       offices.afscme_title_nm,                          " +
       "       offices.priority                                  " +
       "FROM   Aff_Officer_Groups groups INNER JOIN              " +         
       "       AFSCME_Offices offices ON groups.afscme_office_pk " +
       "       = offices.afscme_office_pk                        " +
       "WHERE  aff_pk = ?                                        ";
       
    
    private static final String SQL_GET_OFFICERS =              
       "SELECT   groups.aff_pk,                                 " + 
       "         o.person_pk as personpk,                       " +
       "         o.pos_addr_from_person_pk,                     " +
       "         o.pos_addr_from_org_pk,                        " + 
       "         offices.priority,                              " +
       "         titles.com_cd_desc AS title,                   " +
       "         o.pos_start_dt,                                " + 
       "         o.pos_end_dt,                                  " +
       "         o.pos_expiration_dt,                           " +
       "         pos_steward_fg,                                " +
       "         suspended_fg,                                  " +
       "         suspended_dt,                                  " +
       "         addr.*,                                        " +
       "         ISNULL(addr.addr1, '') +                       " +
       "         ISNULL(' ' + addr.addr2, '') +                 " +
       "         ISNULL(' ' + addr.city + ',', '') +            " +
       "         ISNULL(' ' + addr.state, '') +                 " +
       "         ISNULL(' ' + addr.zipcode, '') +               " +  
       "         ISNULL('-' + addr.zip_plus, '')                " +
       "         AS full_address,                               " +
       "         o.position_mbr_affiliation                     " +
       "         as positionAffiliation                         " +
       "FROM     Officer_History o                              " +
       "         LEFT OUTER JOIN V_Officer_Address addr         " +
       "         ON addr.person_pk =                    " +
       "         o.person_pk LEFT OUTER JOIN Org_Address oa     " +
       "         ON o.pos_addr_from_org_pk = oa.org_addr_pk     " +
       "         INNER JOIN                                     " +
       "         Aff_Officer_Groups groups ON                   " +
       "         groups.office_group_id = o.office_group_id     " +
       "         AND o.aff_pk =                                 " +
       "         groups.aff_pk INNER JOIN                       " +
       "         AFSCME_Offices offices ON o.afscme_office_pk   " +
       "         = offices.afscme_office_pk                     " +
       "         INNER JOIN Common_Codes titles ON              " +
       "         titles.com_cd_pk = offices.afscme_title_nm     " +
       "WHERE    groups.aff_pk = ?                              " +
       "         and pos_end_dt is null                         " +
       "ORDER BY PRIORITY                                       "; 
    
    private static final String SQL_SET_VACANT_OFFICER =
       "UPDATE   Officer_History                                " +
       "SET      pos_end_dt = getdate(),                        " +
       "         lst_mod_user_pk = ?,                           " +
       "         lst_mod_dt = getDate()                         " +
       "WHERE    aff_pk = ?                                     " +
       "         and officer_history_surrogate_key = ?          ";
    
    private static final String SQL_REMOVE_OFFICER_TITLE =
       "DELETE FROM Aff_Officer_Groups                          " +
       "WHERE       AFF_PK = ?                                  " + 
       "            AND AFSCME_OFFICE_PK =                      " + 
       "            (select afscme_office_pk                    " +
       "             FROM AFSCME_Offices                        " + 
       "             WHERE afscme_title_nm = ?)                 " +
       "             AND OFFICE_GROUP_ID = ?                    "; 
    
    private static final String SQL_SET_RENEW_OFFICER_GROUPS =
       "UPDATE   Aff_Officer_Groups                             " +
       "SET      current_term_end = ?                           " +
       "WHERE    AFF_PK = ?                                     " +
       "         AND  afscme_office_pk= ?                       " +
       "         AND OFFICE_GROUP_ID =?                         ";
    
    private static final String SQL_SET_RENEW_OFFICER_HISTORY =
       "UPDATE   Officer_History                               " +
       "SET      pos_expiration_dt = ?,                        " +
       "         lst_mod_user_pk = ?,                          " +
       "         lst_mod_dt = getDate()                       " +
       "WHERE    officer_history_surrogate_key = ?             ";
    
    
    private static final String SQL_IS_DUPLICATE_AFF_OFFICER =
       "SELECT   affiliate_office_title                         " + 
       "FROM     Aff_Officer_Groups                             " +
       "WHERE    aff_pk = ?                                     " +
       "         and firstNm = ?                                " + 
       "         and middleNm = ?                               " + 
       "         and lastNm = ?                                 " + 
       "         and suffix = ?                                 " +
       "ORDER BY lastNm,                                        " +
       "         suffix,                                        " +
       "         firstNm,                                       " +
       "         middleNm                                       "; 
    
    private static final String SQL_GET_AUTO_EBOARD_TITLES_AFFILIATE =
     "SELECT     AFSCME_Offices.afscme_title_nm                 " +
     "FROM       Aff_Organizations INNER JOIN                   " +
     "           AFSCME_Offices ON                              " +
     "           Aff_Organizations.auto_eboard_parent_title =   " +
     "           AFSCME_Offices.afscme_office_pk                " + 
     "WHERE      dbo.Aff_Organizations.aff_pk = ?               ";
    
    private static final String SQL_GET_AUTO_EBOARD_TITLES_SUBAFFILIATE =
     "SELECT     AFSCME_Offices.afscme_title_nm                 " +
     "FROM       Aff_Organizations INNER JOIN                   " +
     "           AFSCME_Offices ON                              " +
     "           Aff_Organizations.auto_eboard_aff_title =      " +
     "           AFSCME_Offices.afscme_office_pk                " + 
     "WHERE      dbo.Aff_Organizations.aff_pk = ?               ";
    
     private static final String SQL_UPDATE_AUTO_DELEGATE_PROVISION_FLAG =
     "UPDATE     Aff_Constitution                               " +
     "SET        auto_delegate_prvsn_fg = ?,                    " +
     "           lst_mod_user_pk = ?,                           " +
     "           lst_mod_dt = getDate()                         " + 
     "WHERE      aff_pk = ?                                     "; 
    
     private static final String SQL_SELECT_MAINTAIN_OFFICERS =              
       "SELECT   h.person_pk,                                  " + 
       "         c.com_cd_desc,                                " +
       "         g.month_of_election,                          " +       
       "         g.current_term_end,                           " +
       "         h.suspended_fg,                               " + 
       "         m.mbr_status,                                 " +
       "         p.first_nm,                                   " +
       "         p.middle_nm,                                  " +       
       "         p.last_nm,                                    " + 
       "         p.suffix_nm,                                  " +
       "         g.reporting_officer_fg,                       " +
       "         h.pos_steward_fg,                             " +
       "         g.executive_board_fg,                         " +
       "         o.aff_type,                                   " + 
       "         o.aff_localSubChapter,                        " + 
       "         o.aff_stateNat_type,                          " + 
       "         o.aff_subUnit,                                " + 
       "         o.aff_councilRetiree_chap,                    " +
       "         o.aff_code,                                   " +
       "         g.max_number_in_office,                       " +
       "         g.afscme_office_pk,                           " +
       "         g.length_of_term,                             " + 
       "         a.afscme_office_pk,                           " +
       "         g.office_group_id,                            " +      
       "         h.officer_history_surrogate_key,              " +       
       "         h.pos_expiration_dt,                          " +
       "         a.elected_officer_fg                          " +
       "FROM     Aff_Officer_Groups g                          " +     
       "    LEFT OUTER JOIN Officer_History h ON               " +
       "         g.office_group_id = h.office_group_id         " +
       "         AND h.aff_pk = g.aff_pk                       " +
       "         AND h.afscme_office_pk = g.afscme_office_pk   " +  
       "         AND h.pos_end_dt IS NULL                      " +              
       "    LEFT OUTER JOIN Aff_Members m ON                   " +
       "         h.person_pk = m.person_pk                     " +
       "         AND h.aff_pk = m.aff_pk                       " +
       "    LEFT OUTER JOIN Person p ON                        " +
       "         h.person_pk = p.person_pk	               " +       
       "    LEFT OUTER JOIN Aff_Organizations o ON             " +
       "         m.aff_pk = o.aff_pk	                       " +       
       "    INNER JOIN AFSCME_Offices a ON                     " +
       "         g.afscme_office_pk = a.afscme_office_pk       " +
       "    INNER JOIN Common_Codes c ON                       " +
       "         c.com_cd_pk = a.afscme_title_nm               " +  
       " WHERE   g.aff_pk = ?                       	       " ;
     
     private static final String SQL_SELECT_MAINTAIN_AN_OFFICER =              
       "SELECT   h.person_pk,                                  " + 
       "         c.com_cd_desc,                                " +
       "         g.month_of_election,                          " +       
       "         g.current_term_end,                           " +
       "         h.suspended_fg,                               " + 
       "         m.mbr_status,                                 " +
       "         p.first_nm,                                   " +
       "         p.middle_nm,                                  " +       
       "         p.last_nm,                                    " + 
       "         p.suffix_nm,                                  " +
       "         g.reporting_officer_fg,                       " +
       "         h.pos_steward_fg,                             " +
       "         g.executive_board_fg,                         " +
       "         o.aff_type,                                   " + 
       "         o.aff_localSubChapter,                        " + 
       "         o.aff_stateNat_type,                          " + 
       "         o.aff_subUnit,                                " + 
       "         o.aff_councilRetiree_chap,                    " +
       "         o.aff_code,                                   " +
       "         g.max_number_in_office,                       " +
       "         g.afscme_office_pk,                           " +
       "         g.length_of_term,                             " + 
       "         a.afscme_office_pk,                           " +
       "         g.office_group_id,                            " +      
       "         h.officer_history_surrogate_key,              " +       
       "         h.pos_expiration_dt,                          " +
       "         a.elected_officer_fg                          " +
       "FROM     Aff_Officer_Groups g                          " +     
       "    LEFT OUTER JOIN Officer_History h ON               " +
       "         g.office_group_id = h.office_group_id         " +
       "         AND h.aff_pk = g.aff_pk                       " +
       "         AND h.afscme_office_pk = g.afscme_office_pk   " +  
       "         AND h.pos_end_dt IS NULL                      " +              
       "    LEFT OUTER JOIN Aff_Members m ON                   " +
       "         h.person_pk = m.person_pk                     " +
       "         AND h.aff_pk = m.aff_pk                       " +
       "    LEFT OUTER JOIN Person p ON                        " +
       "         h.person_pk = p.person_pk	               " +       
       "    LEFT OUTER JOIN Aff_Organizations o ON             " +
       "         h.aff_pk = o.aff_pk	                       " +       
       "    INNER JOIN AFSCME_Offices a ON                     " +
       "         g.afscme_office_pk = a.afscme_office_pk       " +
       "    INNER JOIN Common_Codes c ON                       " +
       "         c.com_cd_pk = a.afscme_title_nm               " +  
       " WHERE   h.officer_history_surrogate_key = ?           " +
       " ORDER BY a.priority                                   ";      
   
     private static final String SQL_SELECT_EBOARD_EXIST_PARENT =
     "   SELECT  afscme_title_nm, c.com_cd_desc            " +
     "   FROM Aff_Organizations o                          " +                
     "   INNER JOIN AFSCME_Offices a ON                    " +                 
     "   o.auto_eboard_parent_title = a.afscme_office_pk   " +
     "   INNER JOIN Common_codes c ON                      " +
     "   c.com_cd_pk = afscme_title_nm                     " +
     "   WHERE o.aff_pk = ?  ";         
     
     private static final String SQL_SELECT_EBOARD_EXIST_SUB =
     "   SELECT  afscme_title_nm, c.com_cd_desc            " +
     "   FROM Aff_Organizations o                          " +                
     "   INNER JOIN AFSCME_Offices a ON                    " +                 
     "   o.auto_eboard_aff_title = a.afscme_office_pk      " +
     "   INNER JOIN Common_codes c ON                      " +
     "   c.com_cd_pk = afscme_title_nm                     " +     
     "   WHERE o.aff_pk = ?  ";          
     
     private static final String SQL_SELECT_MAINTAIN_EBOARD =          
	"  SELECT h.person_pk,                             " +
        "         g.current_term_end,                      " +          
        "         g.month_of_election,                     " +
        "         h.suspended_fg,                          " +  
        "         m.mbr_status,                            " +
        "         p.first_nm,                              " +
        "         p.middle_nm,                             " +      
        "         p.last_nm,                               " +
        "         p.suffix_nm,                             " +                  
        "         o.aff_type,                              " +
        "         o.aff_localSubChapter,                   " +
        "         o.aff_stateNat_type,                     " +
        "         o.aff_subUnit,                           " +
        "         o.aff_councilRetiree_chap,               " +  
        "         o.aff_code,                              " + 
        "         g.max_number_in_office,                  " +
        "         o.aff_pk                                 " +
        " FROM    Officer_History h                        " +
        " INNER JOIN Aff_Officer_Groups g ON               " +
        "         h.office_group_id = g.office_group_id    " +
        "     AND h.aff_pk = g.aff_pk                      " +
        "     AND h.afscme_office_pk = g.afscme_office_pk  " +                                                             
        " INNER JOIN AFSCME_Offices a ON                   " +
        "         h.afscme_office_pk = a.afscme_office_pk  " +
        " INNER JOIN Person p ON                           " +
        "         h.person_pk = p.person_pk	           " +
        " INNER JOIN Aff_Organizations o ON                " + 
        "         h.aff_pk = o.aff_pk	                   " +                           
        " LEFT OUTER JOIN Aff_Members m ON                 " +
        "         m.person_pk = h.person_pk                " +
        " WHERE   o.parent_aff_fk = ?                      " +
	"     AND a.afscme_title_nm = ?                    " +	     
        " ORDER BY a.priority                              ";                   
     
     private static final String MAINTAIN_OFFICER_SEARCH =         
     " SELECT p.person_pk,                                 " +
     "        p.first_nm,                                  " +
     "        p.middle_nm,                                 " +
     "        p.last_nm,                                   " +
     "        p.suffix_nm,                                 " +
     "        o.aff_type,                                  " +
     "        CAST(o.aff_localSubChapter AS int),          " +
     "        o.aff_stateNat_type,                         " +
     "        CAST(o.aff_subUnit AS int),                  " +
     "        CAST(o.aff_councilRetiree_chap AS int),      " +
     "        o.aff_code,                                  " +
     "        a.addr1,                                     " +
     "        a.addr2,                                     " +
     "        a.city,                                      " +
     "        a.state,                                     " +
     "        a.zipcode,                                    " +
     "        o.aff_pk                                     " +     
     " FROM Aff_Organizations o                            " +
     " INNER Join Aff_Members m                            " +
     "    ON m.aff_pk = o.aff_pk                           " +
     "    AND m.mbr_type != 29003                          " +
     "    AND m.mbr_type != 29006                          " +
     " INNER Join Person p                                 " +
     "    ON p.person_pk = m.person_pk                     " +
     "    AND p.mbr_barred_fg != 1                         " +
     " LEFT OUTER JOIN Person_Address a                    " +
     "    ON a.person_pk = p.person_pk and                 " +
     "       address_pk IN (SELECT address_pk              " +
     "                      FROM person_SMA                " +
     "                      WHERE person_pk = p.person_pk AND " +
     "			          current_fg = 1)          ";

     private static final String MAINTAIN_OFFICER_SEARCH_NON_ELECTED_UNION1 =      
     "  UNION                                              " +        
     "         SELECT  p.person_pk,                        " +            
     "                 p.first_nm,                         " +            
     "                 p.middle_nm,                        " +            
     "                 p.last_nm,                          " +            
     "                 p.suffix_nm,                        " +            
     "                 o.aff_type,                         " +            
     "                 CAST(o.aff_localSubChapter AS int),      " +
     "                 o.aff_stateNat_type,                     " +
     "                 CAST(o.aff_subUnit AS int),              " +
     "                 CAST(o.aff_councilRetiree_chap AS int),  " +
     "                 o.aff_code,                         " +         
     "                 a.addr1,                            " +            
     "                 a.addr2,                            " +            
     "                 a.city,                             " +            
     "                 a.state,                            " +            
     "                 a.zipcode,                          " +
     "                 o.aff_pk                            " +       
     "          FROM Users u                               " +                       
     "          INNER Join Person p                        " +            
     "             ON p.person_pk = u.person_pk            " +            
     "             AND p.mbr_barred_fg != 1                " +            
     "          LEFT OUTER JOIN Person_Address a           " +            
     "             ON a.person_pk = p.person_pk and        " +            
     "                address_pk IN (SELECT address_pk     " +            
     "                               FROM person_SMA       " +            
     "                               WHERE person_pk = p.person_pk AND    " +
     "                                     current_fg = 1) " +                              
     "          LEFT OUTER Join Aff_Members m              " +                 
     "             ON m.person_pk = p.person_pk            " +                  
     "             AND m.mbr_type != 29003                 " +            
     "             AND m.mbr_type != 29006                 " + 
     "          LEFT OUTER Join Aff_Organizations o        " +                       
     "             ON o.aff_pk = m.aff_pk                  " +            
     "          WHERE u.dept IS NOT NULL                   " ; 
     
     private static final String MAINTAIN_OFFICER_SEARCH_NON_ELECTED_UNION2 =           
     "   UNION                                                  " +   
     "          SELECT p.person_pk,                             " +       
     "                 p.first_nm,                              " +       
     "                 p.middle_nm,                             " +       
     "                 p.last_nm,                               " +       
     "                 p.suffix_nm,                             " +       
     "                 o.aff_type,                              " +       
     "                 CAST(o.aff_localSubChapter AS int),      " +
     "                 o.aff_stateNat_type,                     " +
     "                 CAST(o.aff_subUnit AS int),              " +
     "                 CAST(o.aff_councilRetiree_chap AS int),  " +
     "                 o.aff_code,                              " +    
     "                 a.addr1,                                 " +       
     "                 a.addr2,                                 " +       
     "                 a.city,                                  " +       
     "                 a.state,                                 " +       
     "                 a.zipcode,                                " + 
     "                 o.aff_pk                                 " +       
     "          FROM Aff_Organizations o                        " +       
     "          INNER Join Aff_Staff s                          " +     
     "             ON s.aff_pk = o.aff_pk                       " +                          
     "          INNER Join Person p                             " +       
     "             ON p.person_pk = s.person_pk                 " +       
     "             AND p.mbr_barred_fg != 1                     " +       
     "          LEFT OUTER JOIN Person_Address a                " +       
     "             ON a.person_pk = p.person_pk and             " +       
     "                address_pk IN (SELECT address_pk          " +       
     "                               FROM person_SMA            " +       
     "                               WHERE person_pk = p.person_pk AND  " +  
     "                                     current_fg = 1)      "; 

     private static final String ENTIRE_AFF_HIERARCHY = 
     " SELECT aff_pk                    " +
     " FROM Aff_Organizations           " +
     " WHERE aff_pk = ? OR              " +     
     "       parent_aff_fk = ? OR       " +
     "       parent_aff_fk IN (SELECT aff_pk              " +
     "                         FROM Aff_Organizations     " +
     "                         WHERE parent_aff_fk = ?)  " +
     " UNION                                              " +
     " SELECT aff_pk                " +
     " FROM aff_organizations       " +
     " WHERE aff_pk IN (SELECT parent_aff_fk   " +
     "                  FROM aff_organizations " +
     "                  WHERE aff_pk = ?) OR   " +
     "       aff_pk IN (SELECT parent_aff_fk   " +
     "                  FROM aff_organizations " +
     "                  WHERE aff_pk IN (SELECT parent_aff_fk   " +
     "                                   FROM aff_organizations " +
     "                                   WHERE aff_pk = ?))    ";          
     
     private static final String SQL_INSERT_OFFICER_HISTORY =     
     "   INSERT INTO Officer_History                      " +     
     "              (person_pk,                           " +
     "               office_group_id,                     " +
     "               aff_pk,                              " +
     "               afscme_office_pk,                    " +
     "               pos_start_dt,                        " +
     "               position_mbr_affiliation,            " +
     "               pos_end_dt,                          " +
     "               pos_expiration_dt,                   " +
     "               pos_steward_fg,                      " +
     "               office_card_sent_dt,                 " +
     "               suspended_fg,                        " +
     "               suspended_dt,                        " +
     "               pos_addr_from_person_pk,             " +
     "               pos_addr_from_org_pk,                " +
     "               created_user_pk,                     " +
     "               created_dt,                          " +
     "               lst_mod_user_pk,                     " +
     "               lst_mod_dt)                          " +
     "        VALUES(?,                                   " +
     "               ?,                                   " +
     "               ?,                                   " +
     "               ?,                                   " +
     "               getDate(),                           " +
     "               ?,                                   " +
     "               null,                                " +
     "               ?,                                   " +
     "               ?,                                   " +
     "               null,                                " +
     "               0,                                   " +
     "               null,                                " +
     "               null,                                " +
     "               null,                                " +
     "               ?,                                   " +
     "               getDate(),                           " +
     "               ?,                                   " +
     "               getDate())                           " ;
     
    private static final String SQL_UPDATE_OFFICER_HISTORY_STEWARD =
        "UPDATE officer_history                                 " +
        "SET    pos_steward_fg = ?,                              " +
        "       lst_mod_user_pk = ?                              " +        
        "WHERE  officer_history_surrogate_key = ?               " ;   

    private static String SQL_UPDATE_AFF_OFF_ACTIVITY =
    "UPDATE Aff_Off_Activity set off_activity_cnt = (off_activity_cnt + ?) " +
    "where aff_pk = ? and time_pk = ? and off_activity_type = ?";

    private static String SQL_GET_TIME_PK =
    "SELECT time_pk from Time_Dim where calendar_year = ? and calendar_month = ?";        
    
    private static String SQL_INSERT_INTO_AFF_OFF_ACTIVITY =
    "INSERT INTO Aff_Off_Activity(aff_pk, time_pk, off_activity_type, " +
    "off_activity_cnt) VALUES(?, ?, ?, ?)";    

    private static String SQL_SELECT_COUNT_FROM_AFF_OFF_ACTIVITY =
    "SELECT count(*) as rcount from Aff_Off_Activity where aff_pk = ? and time_pk = ? " +
    "and off_activity_type = ? ";
    
    /** Creates a new instance of MaintainAffiliateOfficersBean */
    public MaintainAffiliateOfficersBean() {
    }
            
// EJB METHODS
        
    /** Gets references to the dependent EJBs
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
        try {
            orgsBean = JNDIUtil.getMaintainOrganizationsHome().create();
            codesBean = JNDIUtil.getMaintainCodesHome().create();
            affilBean = JNDIUtil.getMaintainAffiliatesHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainAffiliatesBean.ejbCreate()" + e);
        }
    }
        
// BUSINESS METHODS
    
    /** Add a new Officer Title to an affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return the Office Primary Key.
     * @param data
     * @param userPk
     */
    public int addOfficerTitle(OfficeData data, Integer userPk)  { 
        if (data == null || data.getAffPk() == null || data.getAffPk().intValue() < 1 ||
            data.getOfficePk() == null || data.getOfficePk().intValue() < 1 ||
            data.getMonthOfElection() == null || data.getMonthOfElection().intValue() < 1 ||
            data.getNumWithTitle() == null || data.getNumWithTitle().intValue() < 1 ||
            userPk == null || userPk.intValue() < 1
        ) {
            throw new EJBException("Invalid parameters.");
        }
        
        Connection con = null;
        PreparedStatement ps = null;          
              
	try {
            // Get the Affiliate ID's Code and set the code on the new Affiliate.
            con = DBUtil.getConnection();
            
            // insert 
            ps = con.prepareStatement(SQL_INSERT_OFFICER_TITLE);
            ps.setInt(1, data.getAffPk().intValue());
            ps.setInt(2, data.getOfficePk().intValue());
            data.setOfficeGroupID(new Integer(getNextOfficeGroupID(data.getAffPk(), data.getOfficePk(), con)));
            ps.setInt(3, data.getOfficeGroupID().intValue());
            ps.setInt(4, data.getNumWithTitle().intValue());
            ps.setInt(5, data.getMonthOfElection().intValue());
            DBUtil.setNullableVarchar(ps, 6, data.getAffiliateTitle());
            DBUtil.setNullableBooleanAsShort(ps, 7, data.getExecBoard());
            DBUtil.setNullableBooleanAsShort(ps, 8, data.getReportingOfficer());
            DBUtil.setNullableInt(ps, 9, data.getDelegatePriority());
            DBUtil.setNullableInt(ps, 10, data.getLengthOfTerm());
            DBUtil.setNullableInt(ps, 11, data.getTermEnd());
            ps.setInt(12, userPk.intValue());
            ps.setInt(13, userPk.intValue());
            int add = ps.executeUpdate();
            logger.debug("addOfficerTitle command returned: " + add);
            
            // record changes to history
            affilBean.recordChangeToHistory(null, data, userPk);
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        return 0;
    }
    
    private int getNextOfficeGroupID(Integer affPk, Integer officePk, Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(SQL_SELECT_MAX_GROUP_FOR_OFFICE);
        ps.setInt(1, affPk.intValue());
        ps.setInt(2, officePk.intValue());
        ps.setInt(3, affPk.intValue());
        ps.setInt(4, officePk.intValue());
        ResultSet rs = ps.executeQuery();
        int retVal = 1;
        if (rs.next()) {
            retVal = rs.getInt("office_group_id") + 1;
        } 
        DBUtil.cleanup(null, ps, rs);
        logger.debug("getNextOfficeGroupID returning: " + retVal);
        return retVal;
    }
    
     /** Reset reporting officer flags.
      *
      * @ejb:interface-method view-type="local"
      * @ejb:transaction type="Required"
      * @param affPk Affiliate Primary Key
      * @throws SQLException
      */
   public void resetReportingOfficerFlags(Integer affPk)
      
      throws SQLException {
          if (affPk == null
        ) {
            throw new EJBException("Invalid parameter.");
        }             
            Connection con = null;
            PreparedStatement ps = null;
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_RESET_REPORTING_OFFICER_FLAGS);
            ps.setInt(1, affPk.intValue());
            ps.executeUpdate();
            DBUtil.cleanup(con, ps, null);
        }
      
    
    /** Insert Comment for Officer Titles.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @param affPk Affiliate Primary Key
     * @param comment
     * @param userPk
     * @throws SQLException
     */
   public void insertCommentForOfficerTitles(Integer affPk, String comment, Integer userPk) 
      
      throws SQLException {
          if (affPk == null || comment == null || userPk == null
        ) {
            throw new EJBException("Invalid parameters.");
        } 
           
          
          if (!TextUtil.isEmptyOrSpaces(comment)) {
           
            Connection con = null;
            PreparedStatement ps = null;
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_COMMENT_FOR_OFFICER_TITLES);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, userPk.intValue());
            ps.setString(3, comment);
            ps.executeUpdate();
            DBUtil.cleanup(con, ps, null);
        }
      }
    
    /**
     * Retrieves all of the AFSCME Office Titles from the AFSCME_Offices table.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @return The Collection of AFSCMETitleData
     */
   public Collection getAFSCMEOfficerTitles() { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection afscmeTitles = null;
        AFSCMETitleData data = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_GET_AFSCME_OFFICER_TITLES);
            rs = ps.executeQuery();
            if (rs.next()) {
                afscmeTitles = new ArrayList();
                do {
                    data = new AFSCMETitleData();          
                    data.setAfscmeOfficePk(DBUtil.getIntegerOrNull(rs, "afscme_office_pk"));
                    data.setAfscmeTitleName(rs.getString("afscme_title_nm"));
                    afscmeTitles.add(data);
                } while (rs.next());
            } // else do nothing, AFSCMETitleData Collection is null;
        } catch (Exception e) {
            if (e instanceof EJBException) {
                throw (EJBException)e;
            } else {   
                throw new EJBException(e);
            }
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return afscmeTitles;
    }    

    /**
     * Retrieves the Auto EBoard Title information for an Affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * 
     * @return The AutoEBoardTitleData object representing the Affiliate to Sub Affiliate
     * title mapping for Auto EBoard positions.
     */
    public AutoEBoardTitleData getAutoEBoardTitleData(Integer affPk) { 
           Connection con = null;
           PreparedStatement ps = null;
           ResultSet rs = null;
           AutoEBoardTitleData data = null;
        
            try {
                con = DBUtil.getConnection();
                ps = con.prepareStatement(SQL_GET_AUTO_EBOARD_TITLES_AFFILIATE);
                ps.setInt(1, affPk.intValue());
                rs = ps.executeQuery();
                data = new AutoEBoardTitleData();
                if (rs.next()) {
                    data.setAffiliateTitlePk(DBUtil.getIntegerOrNull(rs, 1));
                    if (rs.next()) {
                        // should never happen
                        throw new EJBException("Too many results returned by the query.");
                    }
                    //logger.debug("++++++ Comments Found for pk = " + affPk + ". ++++++");
                } else {
                    //logger.debug("------ Comments NOT Found for pk = " + affPk + ". ------");
                }
                
                DBUtil.cleanup(con, ps, rs);
                
                con = DBUtil.getConnection();
                ps = con.prepareStatement(SQL_GET_AUTO_EBOARD_TITLES_SUBAFFILIATE);
                ps.setInt(1, affPk.intValue());
                rs = ps.executeQuery();
                if (rs.next()) {
                    data.setSubAffiliateTitlePk(DBUtil.getIntegerOrNull(rs, 1));
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
     * Retrieves the most recent comment associated with the Officer Titles.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk The Affiliate Primary Key
     * 
     * @return The CommentData object.
     */
     public CommentData getCommentForOfficerTitles(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CommentData data = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MOST_RECENT_COMMENT_FOR_OFFICES);
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
     * Retrieves the comment history for the Officer Titles.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @return the Collection of CommentData objects
     */
     public Collection getCommentHistoryForOfficerTitles(Integer affPk) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection comments = null;
        CommentData data = null;
        RecordData rd = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COMMENTS_FOR_OFFICER_TITLES);
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
     * Retrieves the entire history officer data for the affiliate.  
     * 
     * Initial sort order will be by Beginning Term Date descending chronologically.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @return the Collection of AffiliateOfficerData objects
     */
    public Collection getOfficerHistory(Integer affPk)  { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection results = null;
        OfficerData data = null;
        RecordData rd = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_GET_OFFICER_HISTORY);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                results = new ArrayList();
                do {
                    data = new OfficerData();
                    data.setOfficerTitle(rs.getString("title"));
                    data.setOfficerFullNM(rs.getString("full_name"));
                    data.setPosStartDt(rs.getTimestamp("pos_start_dt"));
                    data.setPosEndDt(rs.getTimestamp("pos_end_dt"));
                    data.setOfficerPersonPK(DBUtil.getIntegerOrNull(rs, "personpk"));
                    results.add(data);
                } while (rs.next());
            } // else do nothing, OfficerData Collection is null;
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
     * Retrieves the current officers for the affiliate.  
     * 
     * Initial Sort Order is by the Officer Title precedence codes, which is defined by the
     *  priority column in the AFSCME_Offices table, in ascending order.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @return A Collection of OfficerData objects.
     */
    public Collection getOfficers(Integer affPk) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection results = null;
        OfficerData data = null;
        RecordData rd = null;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_GET_OFFICERS);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                results = new ArrayList();
                do {
                    data = new OfficerData();
                    data.setPosAddrFromPersonPk(DBUtil.getIntegerOrNull(rs, "pos_addr_from_person_pk"));
                    data.setPosAddrFromOrgPk(DBUtil.getIntegerOrNull(rs, "pos_addr_from_org_pk"));
                    data.setPosStartDt(rs.getTimestamp("pos_start_dt"));
                    data.setPosEndDt(rs.getTimestamp("pos_end_dt"));
                    data.setSuspendedFg(DBUtil.getBooleanFromShort(rs.getShort("suspended_fg")));
                    data.setSuspendedDt(rs.getTimestamp("suspended_dt"));
                    data.setPosStewardFg(DBUtil.getBooleanFromShort(rs.getShort("pos_steward_fg")));
                   /** Common Code Key for the Member Status if this Officer is a Member. Will be null  if the Officer is not a Member.*/
                    //data.setmbrStatusCodePk(DBUtil.getIntegerOrNull(rs, ""));
                    data.setPositionAffiliation(DBUtil.getIntegerOrNull(rs, "positionAffiliation"));
                    //private AffiliateIdentifier theAffiliateIdentifier;
                    data.setOfficerTitle(rs.getString("Title"));
                    //data.setofficerFullNM(rs.getString(""));
                    data.setOfficerPersonPK(DBUtil.getIntegerOrNull(rs, "personpk"));
                    results.add(data);
                } while (rs.next());
            } // else do nothing, OfficerData Collection is null;
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
    
    /** Retrieves the Officer Title data for this affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return A Collection of OfficeData representing the  Affiliate Offices.
     * @param sortData
     * @param affPk Affiliate Primary Key
     */ 
     public Collection getOfficerTitles(Integer affPk, SortData sortData) { 
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       Collection results = null;
       OfficeData data = null;
       RecordData rd = null;
       
       con = DBUtil.getConnection();
       
       PreparedStatementBuilder builder = new PreparedStatementBuilder();
       
       try {
        String sortColumn = getSortColumn(sortData);
        if (sortColumn != null) {
            builder.addOrderBy(sortColumn);
        } else {
            builder.addOrderBy("priority");
        }
        ps = builder.getPreparedStatement(SQL_GET_OFFICER_TITLES, con);
        ps.setInt(1, affPk.intValue());
        rs = ps.executeQuery();
        if (rs.next()) {
            results = new ArrayList();
            do {
               data = new OfficeData();
               data.setAffPk(DBUtil.getIntegerOrNull(rs, "aff_Pk"));
               data.setOfficePk(DBUtil.getIntegerOrNull(rs, "afscme_office_pk"));
               data.setOfficeGroupID(DBUtil.getIntegerOrNull(rs, "office_group_id"));
               data.setAffiliateTitle(rs.getString("affiliate_office_title"));
               data.setNumWithTitle(DBUtil.getIntegerOrNull(rs, "max_number_in_office"));
               data.setMonthOfElection(DBUtil.getIntegerOrNull(rs, "month_of_election"));
               data.setLengthOfTerm(DBUtil.getIntegerOrNull(rs, "length_of_term"));
               data.setTermEnd(DBUtil.getIntegerOrNull(rs, "current_term_end"));
               data.setDelegatePriority(DBUtil.getIntegerOrNull(rs, "delegate_priority"));
               data.setReportingOfficer(DBUtil.getBooleanFromShort(rs.getShort("reporting_officer_fg")));
               data.setExecBoard(DBUtil.getBooleanFromShort(rs.getShort("executive_board_fg")));
               data.setAfscmeTitle(new Integer(rs.getInt("Afscme_title_nm")));
               data.setPriority(new Integer(rs.getInt("priority")));
               results.add(data);
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
        return results;
    }
    
    /**
     * Removes an Officer Title from an affiliate.
     * 
     * Should display a warning popup to indicate that any association to a person 
     * holding the office being removed will also be removed when the office is 
     * deleted.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param afscmeOfficePk afscmeOffice Primary Key
     * @param officePk Office Primary Key
     * @return 'true' if the Office was removed, and 'false' otherwise.
     */
    public boolean removeOfficerTitle(Integer affPk, Integer officePk, Integer afscmeOfficePk) { 
        Connection con = null;
        PreparedStatement ps  = null;
	PreparedStatement ps2 = null;
                ResultSet rs  = null;
                ResultSet rs2 = null;
        int removable=0;
        boolean removed = false;
        
        try {          
	    con = DBUtil.getConnection();
            
            ps2 = con.prepareStatement(SQL_IS_OFFICER_TITLE_REMOVABLE);
            ps2.setInt(1, affPk.intValue());
            ps2.setInt(2, afscmeOfficePk.intValue());
            ps2.setInt(3, officePk.intValue());
            rs2 = ps2.executeQuery();
            
            if (!rs2.next()) {
                ps = con.prepareStatement(SQL_REMOVE_OFFICER_TITLE);
                ps.setInt(1, affPk.intValue());
                ps.setInt(2, afscmeOfficePk.intValue());
                ps.setInt(3, officePk.intValue());
                ps.executeUpdate();
                removed = true;
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
            DBUtil.cleanup(con, ps2, null);
        }
	
         return removed;
    }
    
    /** Check to see if an affiliate already has a President
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @returns true if a president exists or false if no president exists
     * @param affPk The primary key of the affiliate.
     * @return
     */
     public boolean checkForPresident(Integer affPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean presFg = false;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_CHECK_FOR_PRESIDENT);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                presFg = true;
            } 
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return presFg;
        
    }  
    
    /**
     * Allows the Affiliate Officer data to be updated.  Separate methods will be used 
     * to perform the update functions of Extend, Vacate and Replace. This methods 
     * determines the need and calls the appropriate method.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param affiliateOfficerData new Affiliate Officer Data
     *
     * @return 'true' if the update is completed, and 'false' otherwise
     */
    public boolean updateOfficers(Integer affPk, NewAffiliateOfficerData affiliateOfficerData) { 
        return true;
    }
    
    /** Allows the Officer Title data to be updated for the affiliate.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return 'true' if the update is completed, and 'false' otherwise
     * @param data
     * @param userPk
     */
    public boolean updateOfficerTitles(OfficeData data, Integer userPk) { 
        if (data == null || data.getAffPk() == null || data.getAffPk().intValue() < 1 ||
            data.getOfficePk() == null || data.getOfficePk().intValue() < 1 ||
            data.getMonthOfElection() == null || data.getMonthOfElection().intValue() < 1 ||
            data.getNumWithTitle() == null || data.getNumWithTitle().intValue() < 1 ||
            userPk == null || userPk.intValue() < 1
        ) {
            throw new EJBException("Invalid parameters.");
        }
        
        Connection con = null;
        PreparedStatement ps = null;
	
	try {
            con = DBUtil.getConnection();
            
            // update
            ps = con.prepareStatement(SQL_UPDATE_OFFICER_TITLES);
            ps.setInt(1, data.getNumWithTitle().intValue());
            ps.setInt(2, data.getMonthOfElection().intValue());
            DBUtil.setNullableVarchar(ps, 3, data.getAffiliateTitle());
            DBUtil.setNullableBooleanAsShort(ps, 4, data.getExecBoard());
            DBUtil.setNullableBooleanAsShort(ps, 5, data.getReportingOfficer());
            DBUtil.setNullableInt(ps, 6, data.getDelegatePriority());
            DBUtil.setNullableInt(ps, 7, data.getLengthOfTerm());
            DBUtil.setNullableInt(ps, 8, data.getTermEnd());
            ps.setInt(9, userPk.intValue());
            ps.setInt(10, data.getAffPk().intValue());
            ps.setInt(11, data.getOfficePk().intValue());
            ps.setInt(12, data.getOfficeGroupID().intValue());
            int add = ps.executeUpdate();
            logger.debug("updated OfficerTitle command returned: " + add);
            
            // record changes to history
            affilBean.recordChangeToHistory(null, data, userPk);
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
     * Determines if there are multiple persons that meet the criteria for assigning 
     * the replacement officer.
     * 
     * @param affPk Affiliate Primary Key
     * @param firstNm Person's First Name
     * @param middleNm Person's Middle Name
     * @param lastNm Person's Last Name
     * @param suffix
     */
    private boolean isDuplicateAffOfficer(Integer affPk, String firstNm, String middleNm, String lastNm, String suffix)  {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;

		//if (isCodeTaken(null, codeData))
		//	return false;
		
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_IS_DUPLICATE_AFF_OFFICER);
            ps.setInt(1, affPk.intValue());
            ps.setString(2, firstNm);
            ps.setString(3, middleNm);
            ps.setString(4, lastNm);
            ps.setString(5, suffix);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
        }
	
                return true;
    }
    
    /** Renews the Officer to another term, updates groups table.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required" *
     * @return 'true' if Extension is made, and 'false' otherwise
     * @param newEndTerm
     * @param affPk Affiliate Primary Key
     * @param officePk Office Primary Key
     * @param groupPk Person Primary Key
     */
    public boolean setRenewOfficerGroups(Integer newEndTerm, Integer affPk, Integer officePk, Integer groupPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
	
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SET_RENEW_OFFICER_GROUPS);
            ps.setInt(1, newEndTerm.intValue());
            ps.setInt(2, affPk.intValue());
            ps.setInt(3, officePk.intValue());
            ps.setInt(4, groupPk.intValue());
            logger.debug(SQL_SET_RENEW_OFFICER_GROUPS  + " | " + newEndTerm.intValue() + " | " + affPk.intValue() +  " | " + officePk.intValue() + " | " + groupPk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
        }
	
        return true;
    }
    
    /** Renews the Officer to another term, updates groups table.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required" *
     * @return 'true' if Extension is made, and 'false' otherwise
     * @param personPk
     * @param ts New pos_end_dt
     * @param surrKey History key
     */
    public boolean setRenewOfficerHistory(Timestamp ts, Integer surrKey, Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
	
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SET_RENEW_OFFICER_HISTORY);
            ps.setTimestamp(1, ts);
            ps.setInt(3, surrKey.intValue());
            ps.setInt(2, personPk.intValue());
      
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
        }
	
                return true;
    }    
    
    /**
     * Assigns the new person to the officer position. The Officer that is currently holding
     *  the position with have his position terminated, by have the Officer_History.pos_end_dt
     *  set to the current date.
     * 
     * @param affPk Affiliate Primary Key
     * @param officePk Office Primary Key
     * @param personPk Person Primary Key
     * @param homeAffPk Person's home Affiliate Primary Key if s/he is a member.
     * @return 'true' if replace is made, and 'false' otherwise
     */
    private void setReplaceOfficer(Integer affPk, Integer officePk, Integer personPk, Integer homeAffPk) {

    }
    
    /** Assigns the office as Vacant by ending the term of the Officer currently holding the
     *  position.
     *
     * Set the Officer_History.pos_end_dt field equal to the current date.
     * " +
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return 'true' if office is vacated, and 'false' otherwise
     * @param personPk
     * @param affPk Affiliate Primary Key
     * @param officePk Office Primary Key
     */
    public boolean setVacantOfficer(Integer affPk, Integer officePk, Integer personPk)  { 
        Connection con = null;
        PreparedStatement ps = null;
		
		//if (isCodeTaken(null, codeData))
		//	return false;
		
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SET_VACANT_OFFICER);
            ps.setInt(2, affPk.intValue());
            ps.setInt(3, officePk.intValue());
            ps.setInt(1, personPk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
        }
        
        return true;
    }
   
    /** Update Auto Delegate Provision Flag in Aff_Constitution Table
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return 'true' if affiliate constitution data is updated, and 'false' otherwise
     * @param aff_pk
     * @param autoDelProvisionFg
     * @param userPk
     */
    public boolean updateAutoDelegateProvision(Integer aff_pk, Boolean autoDelProvisionFg, Integer userPk)  { 
        Connection con = null;
        PreparedStatement ps = null;
	      
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AUTO_DELEGATE_PROVISION_FLAG);
            DBUtil.setNullableBooleanAsShort(ps, 1, autoDelProvisionFg);
            ps.setBoolean(1, autoDelProvisionFg.booleanValue());
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, aff_pk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
        }
	
                return true;
    }
    
    /** Update Auto Executive Board Offices Aff_Organization Table
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return 'true' if affiliate auto eboard titles are updated, and 'false' otherwise
     * @param aff_pk
     * @param affiliateTitlePk
     * @param subAffiliateTitlePk
     * @param userPk
     */
     public boolean updateAutoExecBoardTitles(Integer aff_pk, Integer affiliateTitlePk, Integer subAffiliateTitlePk, Integer userPk) { 
        Connection con = null;
        PreparedStatement ps = null;
	                     
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AUTO_EBOARD_TITLES);
            ps.setInt(1, affiliateTitlePk.intValue());
            ps.setInt(2, subAffiliateTitlePk.intValue());
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, aff_pk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			
            DBUtil.cleanup(con, ps, null);
        } 
        
        return true;
     }
          
    /**
     * Returns the list of officers for a given affiliate.
     * 
     * If an officer title is not taken by a person,
     * a blank record for that title is generated and added to the
     * Map to be returned
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"      
     *
     * @return Map of Officers.  Key = Integer 1-n, Value = AffiliateOfficerMaintenance     
     *
     * @param affPk Affiliate Primary Key
     * @param affStatus Integer that indicates the status of the affiliate
     * @param isVdu Boolean that indicates if the user is accessing from the VDU or not.
     */
    public Map getOfficerMaintenanceList(Integer affPk, Integer affStatus, boolean isVdu)  {
        logger.debug("Inside getOfficerMaintenanceList");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        int fieldPK = 0;
        int numberInOffice = 0;
        int numberInOfficeProcessed = 1;
        int currentOfficePk = 0;
        int newOfficePk = 0;
        boolean firstTime = true;
        
        AffiliateOfficerMaintenance aom = new AffiliateOfficerMaintenance();      
        Map officerMap = new TreeMap();  
        
        try {
            StringBuffer sql = new StringBuffer(SQL_SELECT_MAINTAIN_OFFICERS);
            
            if (affStatus.equals(AffiliateStatus.RA) && isVdu) {
                sql.append("  AND a.afscme_title_nm IN (6004, 6005)  ");    
            }
            
            // If status is unrestricted admin, AffiliateStatus.UA, then the sort
            // needs to sort the Administrator and Deputy Administrator to the top.
            // Need to add a different ORDER BY then the one below.            
            if (affStatus.equals(AffiliateStatus.UA)) {
                sql.append(" ORDER BY " +
                           "CASE WHEN a.afscme_title_nm=" + AfscmeTitle.ADMINISTRATOR + " THEN 1 " +
                           "WHEN a.afscme_title_nm=" + AfscmeTitle.DEPUTY_ADMINISTRATOR + " THEN 2 " +
                           "ELSE 3 " +
                           "END, a.priority ");
            } else {            
                sql.append(" ORDER BY a.priority ");
            }                    
            
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(sql.toString());
            
            ps.setInt(1, affPk.intValue());
            logger.debug("QUERY IS " + sql + " " + affPk.intValue());
            
            rs = ps.executeQuery();
            while (rs.next()) {
                newOfficePk = rs.getInt(21);
                
                // First loop through, set up the current office as the first one
                if (firstTime) {
                    currentOfficePk = newOfficePk;
                    numberInOffice = rs.getInt(20); 
                    firstTime = false;
                }
                
                else {
                // HLM: 1/22/2004
                // This logic will cause a problem in the case when the same afscme_office_pk returned from the
                // result set. So, remove this and change to else to fix the problem.
                // if (newOfficePk != currentOfficePk) {                                                                               
                    
                    // Check if 1 record exists in the return Map
                    // for each of the possible officers in each office
                    // These excess records contain only the title information
                    while (numberInOfficeProcessed <= numberInOffice) {
                        AffiliateOfficerMaintenance stubAom = new AffiliateOfficerMaintenance();
                        setUpEmptyOfficerMaintenanceTitle(stubAom, aom);
                        officerMap.put(new Integer(fieldPK++), stubAom);
                        numberInOfficeProcessed++;
                    }                    
                    currentOfficePk = newOfficePk;
                    numberInOffice = rs.getInt(20); 
                    numberInOfficeProcessed = 1;                    
                }   
                
                numberInOfficeProcessed++;   
                aom = readOfficerMaintenanceRecord(rs);
                officerMap.put(new Integer(fieldPK++), aom);                                                  
            }
            
            // check last record and see if a few last stubs need to be added
            while (numberInOfficeProcessed <= numberInOffice) {
                AffiliateOfficerMaintenance stubAom = new AffiliateOfficerMaintenance();
                setUpEmptyOfficerMaintenanceTitle(stubAom, aom);
                officerMap.put(new Integer(fieldPK++), stubAom);
                numberInOfficeProcessed++;
            }            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {			
            DBUtil.cleanup(con, ps, rs);
        }
	
        return officerMap;
    }
    
    /**
     * Retrieves all the Auto EBoard Officers for the affiliate from each sub-affiliate of
     * that affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key - the current affiliate
     * @return the Collection of AutoEBoardOfficerData objects.
     */
    public ArrayList getAutoEBoardOfficers(Integer affPk) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        ArrayList eBoardMembers = new ArrayList();
        
        String affTitle = null;
        String subAffTitle = null;
        
        Integer subAffTitleCode = new Integer(0);
        
        
        try {
            
            // First check and see if this affiliate has Eboard titles set up
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EBOARD_EXIST_PARENT);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                affTitle = rs.getString(2);              
            }

            DBUtil.cleanup(null, ps, rs);            
            
            ps = con.prepareStatement(SQL_SELECT_EBOARD_EXIST_SUB);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                subAffTitleCode = new Integer(rs.getInt(1));
                subAffTitle = rs.getString(2);                            
            }
            
            
            if ((affTitle == null) || (subAffTitle == null)) {
               // no e board officer titles set up
               return null;
            }
            
            // E board titles set up
            // now find e board members
            DBUtil.cleanup(null, ps, rs);
            ps = con.prepareStatement(SQL_SELECT_MAINTAIN_EBOARD);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, subAffTitleCode.intValue());
            logger.debug("SQL IS " + SQL_SELECT_MAINTAIN_EBOARD + " | " + affPk.intValue() + " | " + subAffTitle);
            rs = ps.executeQuery();
            
            EBoardMaintenance ebm = new EBoardMaintenance();
            int numberInAff = 0;
            int numberInAffProcessed = 1;
            int currentAffPk = 0;
            int newAffPk = 0;
            boolean firstTime = true;
            
            while (rs.next()) {
                newAffPk = rs.getInt(17);
                
                // First loop through, set up the current affiliate as the first one
                // and set up the number of this title in the aff
                if (firstTime) {
                    currentAffPk = newAffPk;
                    numberInAff = rs.getInt(16);
                    // no longer the first time through
                    firstTime = false;
                }
                
                // If its a different aff being processed
                if (newAffPk != currentAffPk) {                                                                               
                    
                    // Check if records have been added for each of the number in office                
                    // If not, add records with empty person information
                    // as no person is in that office, but the office exists
                    while (numberInAffProcessed < numberInAff) {
                        EBoardMaintenance stubEbm = new EBoardMaintenance();
                                               
                        stubEbm.setEndTerm(ebm.getEndTerm());
                        stubEbm.setMonthOfElection(ebm.getMonthOfElection());
                        stubEbm.setAi(ebm.getAi());
                        stubEbm.setOfficerTitle(affTitle);
                        stubEbm.setSubAffiliateTitle(subAffTitle);                        
                        
                        eBoardMembers.add(stubEbm);
                        numberInAffProcessed++;
                    }                    
                    currentAffPk = newAffPk;
                    numberInAff = rs.getInt(16); 
                    numberInAffProcessed = 1;                    
                }
                
                ebm = readEBoardMaintenanceRecord(rs);              
                ebm.setOfficerTitle(affTitle);
                ebm.setSubAffiliateTitle(subAffTitle);
                
                eBoardMembers.add(ebm);                
            }
            // do number in office check for last record through
            while (numberInAffProcessed < numberInAff) {
                EBoardMaintenance stubEbm = new EBoardMaintenance();

                stubEbm.setEndTerm(ebm.getEndTerm());
                stubEbm.setMonthOfElection(ebm.getMonthOfElection());
                stubEbm.setAi(ebm.getAi());
                stubEbm.setOfficerTitle(affTitle);
                stubEbm.setSubAffiliateTitle(subAffTitle);                        

                eBoardMembers.add(stubEbm);
                numberInAffProcessed++;
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
                                
        return eBoardMembers;
    }       

    /**
     * Update Auto Executive Board Offices Aff_Organization Table      
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required" 
     *
     * @param roc The criteria to search by
     *
     * @return Collection of officers that match the criteria
     */
     public Collection maintainOfficerSearch(ReplaceOfficerCriteria roc) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = new String();
        Collection potentialOfficers = new ArrayList();        
	                     
        try {
            // get a list of all the affilites in the hierarchy, up and down the tree
            String hierInClause = getHierarchySqlString(roc.getAffPk());
            
            // form base query passing in the criteria and the IN clause
            sql = getMaintainOfficerSearchQuery(roc, hierInClause);
            
            logger.debug("SQL IS ******************************" + sql);
            
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ReplaceOfficerResults ror = readReplaceOfficerResultsRecord(rs);                
                logger.debug("PERSON PK = " + rs.getInt(1));
                potentialOfficers.add(ror);
            }
                        
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {			
            DBUtil.cleanup(con, ps, rs);
        } 
        
        return potentialOfficers;
     }    
     
     /** Insert Officer History Record.
      *
      * @ejb:interface-method view-type="local"
      * @ejb:transaction type="Required"
      * @return
      * @param personPk
      * @param officeGroupId
      * @param afscmeOfficePk
      * @param mbrAff
      * @param expirationDate
      * @param steward
      * @param affPk Affiliate Primary Key
      * @param userPk
      * @throws SQLException
      */
     public Integer insertOfficerHistory(Integer personPk, Integer officeGroupId, 
                                               Integer affPk, Integer afscmeOfficePk, Integer mbrAff,
                                               Timestamp expirationDate, Integer userPk, boolean steward)  throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        Integer surrKey = null;
        
        try{
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_OFFICER_HISTORY);
            ps.setInt(1, personPk.intValue());
            ps.setInt(2, officeGroupId.intValue());
            ps.setInt(3, affPk.intValue());
            ps.setInt(4, afscmeOfficePk.intValue());   
            ps.setInt(5, mbrAff.intValue());              
            ps.setTimestamp(6, expirationDate);
            DBUtil.setNullableBooleanAsShort(ps, 7, new Boolean(steward));            
            ps.setInt(8, userPk.intValue());
            ps.setInt(9, userPk.intValue());
            
            logger.debug("SQL = " + SQL_INSERT_OFFICER_HISTORY + " | " + personPk.intValue() + " | " + officeGroupId.intValue() + " | " + affPk.intValue() + " | " + afscmeOfficePk.intValue() + " | " +mbrAff.intValue());
            surrKey = DBUtil.insertAndGetIdentity(con, ps);                      
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {			
            DBUtil.cleanup(con, ps, null);
        }
        return surrKey;
    }     
    
     
    /**
     * Returns a single affiliateOfficerMaintenance record for the given surrogate Key
     *    
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required" 
     *
     * @param surrKey Unique key in officer_history table
     *
     * @return AffiliateOfficerMaintenance
     */
     public AffiliateOfficerMaintenance getSingleOfficerHistory(Integer surrKey) { 
         Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null; 
         AffiliateOfficerMaintenance aom = new AffiliateOfficerMaintenance();
	                     
         try {                       
	     con = DBUtil.getConnection();
             ps = con.prepareStatement(SQL_SELECT_MAINTAIN_AN_OFFICER);
             ps.setInt(1, surrKey.intValue());        
             rs = ps.executeQuery();
             rs.next();
             aom = readOfficerMaintenanceRecord(rs);
         } catch (SQLException e) {
             throw new EJBException(e);
         }
         finally {			
             DBUtil.cleanup(con, ps, rs);
         } 
        
         return aom;
     }         
     
    /**
     * returns a list of aff pks that are in the hierarchy, both up and down
     * in a format that can be placed in an IN SQL clause      
     *
     * @param aff_Pk Affiliate Primary Key
     *
     * @return 'true' if affiliate auto eboard titles are updated, and 'false' otherwise
     */
     private String getHierarchySqlString(Integer aff_pk) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer returnString = new StringBuffer();
        boolean firstTime = true;
	                     
        try {                       
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(ENTIRE_AFF_HIERARCHY);
            ps.setInt(1, aff_pk.intValue());
            ps.setInt(2, aff_pk.intValue());
            ps.setInt(3, aff_pk.intValue());
            ps.setInt(4, aff_pk.intValue());
            ps.setInt(5, aff_pk.intValue());            
            rs = ps.executeQuery();
            while (rs.next()) {
                if (firstTime) {
                    firstTime = false;
                    returnString.append(rs.getInt(1));                   
                } else {
                    returnString.append(", ");
                    returnString.append(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {			
            DBUtil.cleanup(con, ps, rs);
        } 
        
        return returnString.toString();
     }    
        
    
     /** ReplaceOfficerSearch as called by edit screen
      *
      * @ejb:interface-method view-type="local"
      * @ejb:transaction type="Required"
      * @return 'true' if affiliate auto eboard titles are updated, and 'false' otherwise
      * @param roc ReplaceOfficerCriteria object used to do search
      * @param results Result of search, collection of ReplaceOfficerResults records
      * @param needCount boolean that indicates whether count is needed to be returned
      */
    public int maintainOfficerSearch(ReplaceOfficerCriteria roc, Collection results, boolean needCount) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = new String();
        int countTotal = 0;       
	                     
        try {
            // get a list of all the affilites in the hierarchy, up and down the tree
            String hierInClause = getHierarchySqlString(roc.getAffPk());
            
            // form base query passing in the criteria and the IN clause
            sql = getMaintainOfficerSearchQuery(roc, hierInClause);
             
            logger.debug("SQL IS ******************************" + sql);

	    con = DBUtil.getConnection();
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);             
            rs = ps.executeQuery();
            
            if (needCount) {
                while (rs.next())
                    countTotal++;                  
                rs.first();
            }
            
            if (rs.next()) {
                int count = 1;
                
                // set the page of results to return
                rs.absolute(roc.getPage() * roc.getPageSize() + 1);
                
                do {                
                    ReplaceOfficerResults ror = readReplaceOfficerResultsRecord(rs);                
                    results.add(ror);
                } while ( rs.next() && ((count++) < roc.getPageSize()) );                    
            }                        
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {			
            DBUtil.cleanup(con, ps, rs);
        } 
        
        return countTotal;
    }        
      
    
    /**
     * Update Auto Executive Board Offices Aff_Organization Table      
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required" 
     *
     * @param surrKey key field for the history table
     * @param steward new value for DB
     * @param userPk user doing updating
     *
     * @return 'true' if affiliate steward is updated
     */
    public boolean updateOfficerHistoryStewardFlag(Integer surrKey,                                               
                                                boolean steward, Integer userPk) { 
        Connection con = null;
        PreparedStatement ps = null;
	                     
        try {
	    con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_OFFICER_HISTORY_STEWARD);
            DBUtil.setNullableBooleanAsShort(ps, 1, new Boolean(steward));
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, surrKey.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {			
            DBUtil.cleanup(con, ps, null);
        }         
        return true;
    }    
     
    /** Updates the RPT_Aff_Mbr_Activity table
     *
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @param affPk
     * @param activityType
     * @param numberOf
     */

    public void updateAffOffActivity(Integer affPk, Integer activityType, int numberOf) {

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

        /** if the RPT_Aff_Off_Activity table does not have a record that represents this
         * month/year date combination for this activity type for this affiliate, then a row
         * needs to be inserted into the table for this combination - set to zero
         * initially and then let update add to zero (null + 1) = unknown
         */

        insertRowIntoAffOffActivityIfNeeded(affPk, timePk, activityType);

        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AFF_OFF_ACTIVITY);
            ps.setInt(1, numberOf);
            ps.setInt(2, affPk.intValue());
            ps.setInt(3, timePk.intValue());
            ps.setInt(4, activityType.intValue());

            int rt = ps.executeUpdate();

        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Ocurred in MaintainAffiliateOfficersBean.updateAffOffActivity" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, null);
        }

    } // updateAffOffActivity    
    
    /**
     *
     * Determines whether a row exists in (RPT) Aff_Off_Activity for a given
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
     */
    private void insertRowIntoAffOffActivityIfNeeded(Integer affPk, Integer timePk, Integer activityType) {

        int initialNum = 0;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;


        // see if the row exists for this aff, timePk and activityType in Aff_Mbr_Activity
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_COUNT_FROM_AFF_OFF_ACTIVITY);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, timePk.intValue());
            ps.setInt(3, activityType.intValue());
            rs = ps.executeQuery();
            rs.next(); // select count(*) will return only one row

            if (rs.getInt("rcount") == 0) { // if no row exists for the aff, time and activity
                ps2 = con.prepareStatement(SQL_INSERT_INTO_AFF_OFF_ACTIVITY);
                ps2.setInt(1, affPk.intValue());
                ps2.setInt(2, timePk.intValue());
                ps2.setInt(3, activityType.intValue());
                ps2.setInt(4, initialNum);
                ps2.executeUpdate();

            }
        }

        catch (SQLException e) {
            throw new EJBException
            ("SQL Error Occurred in MaintainAffiliateOfficersBean.insertRowIntoAffOffActivityIfNeeded method" + e);
        }

        finally {
            DBUtil.cleanup(null, ps2, null);
            DBUtil.cleanup(con, ps, rs);

        }
    } // insertRowIntoAffOffActivityIfNeeded    
    
    /**
     *
     * Retrieves the timePk from the RPT_Time_Dimension table
     *
     * @param int - month integer representation 1-12, not 0-11, so increment
     *   if you pulled the current month from a function that number month 0-11
     * @param int - year integer representation of the year, form YYYY
     * @return Integer - a timePk from the RPT_Time_Dimension table
     *
     */
    private Integer getTimePk    (int month, int year) {

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
               throw new EJBException("Error Occurred in MaintainAffiliateOfficersBean.getTimePk method - timePk not found");
            }
        }

        catch (SQLException e) {
            throw new EJBException("SQL Error Occurred in MaintainAffiliateOfficersBean.getTimePk method" + e);
        }

        finally {
            DBUtil.cleanup(con, ps, rs);
        }

        return timePk;

    } // getTimePk    
    
    /**
     * Creates the SQL needed for the replace officer results functionality      
     *
     * @return completed sql string
     */    
    private void setUpEmptyOfficerMaintenanceTitle(AffiliateOfficerMaintenance stubAom, AffiliateOfficerMaintenance aom){
        stubAom.setOfficerTitle(aom.getOfficerTitle());
        stubAom.setEndTerm(aom.getEndTerm());
        stubAom.setMonthOfElection(aom.getMonthOfElection());                
        stubAom.setLengthOfTerm(aom.getLengthOfTerm());                
        stubAom.setReportingOfficer(aom.isReportingOfficer());
        stubAom.setExecutiveBoard(aom.isExecutiveBoard());
        stubAom.setOriginalEndTerm(aom.getOriginalEndTerm());
        stubAom.setElectedOfficerFg(aom.getElectedOfficerFg());
        stubAom.setAfscmeOfficePk(aom.getAfscmeOfficePk());
        stubAom.setOfficeGroupPk(aom.getOfficeGroupPk());        
        stubAom.setAi(new AffiliateIdentifier());                        
    }
    
    /**
     * Creates the SQL needed for the replace officer results functionality      
     *
     * @return completed sql string
     */    
    private String getMaintainOfficerSearchQuery(ReplaceOfficerCriteria roc, String inClause){
        StringBuffer sql = new StringBuffer(MAINTAIN_OFFICER_SEARCH);
        
        // set up initial query
        sql.append(" WHERE o.aff_pk IN ( " + inClause + ") ");
        if (!TextUtil.isEmptyOrSpaces(roc.getFirstName()))
            sql.append(" AND p.first_nm = '" + roc.getFirstName() + "'  ");
        if (!TextUtil.isEmptyOrSpaces(roc.getMiddleName()))
            sql.append(" AND p.middle_nm = '" + roc.getMiddleName() + "'  ");     
        if (!TextUtil.isEmptyOrSpaces(roc.getLastName()))
            sql.append(" AND p.last_nm = '" + roc.getLastName() + "'  "); 
        if (roc.getSuffix() != null && roc.getSuffix().intValue() > 0)
            sql.append(" AND p.suffix_nm = " + roc.getSuffix().intValue() + "  ");                

        // if non elected office, tack on union pieces of query for all staff and AFSCME staff
        if (!roc.isElected().booleanValue()) {
            // union in AFSCME staff
            sql.append(MAINTAIN_OFFICER_SEARCH_NON_ELECTED_UNION1);
            if (!TextUtil.isEmptyOrSpaces(roc.getFirstName()))
                sql.append(" AND p.first_nm = '" + roc.getFirstName() + "'  ");
            if (!TextUtil.isEmptyOrSpaces(roc.getMiddleName()))
                sql.append(" AND p.middle_nm = '" + roc.getMiddleName() + "'  ");     
            if (!TextUtil.isEmptyOrSpaces(roc.getLastName()))
                sql.append(" AND p.last_nm = '" + roc.getLastName() + "'  "); 
            if (roc.getSuffix() != null && roc.getSuffix().intValue() > 0)
                sql.append(" AND p.suffix_nm = " + roc.getSuffix().intValue() + "  ");                

            // Union in all staff
            sql.append(MAINTAIN_OFFICER_SEARCH_NON_ELECTED_UNION2);
            sql.append(" WHERE o.aff_pk IN ( " + inClause + ") ");
            if (!TextUtil.isEmptyOrSpaces(roc.getFirstName()))
                sql.append(" AND p.first_nm = '" + roc.getFirstName() + "'  ");
            if (!TextUtil.isEmptyOrSpaces(roc.getMiddleName()))
                sql.append(" AND p.middle_nm = '" + roc.getMiddleName() + "'  ");     
            if (!TextUtil.isEmptyOrSpaces(roc.getLastName()))
                sql.append(" AND p.last_nm = '" + roc.getLastName() + "'  "); 
            if (roc.getSuffix() != null && roc.getSuffix().intValue() > 0)
                sql.append(" AND p.suffix_nm = " + roc.getSuffix().intValue() + "  ");                  
        }

        // handle order by
        if (!TextUtil.isEmptyOrSpaces(roc.getOrderBy())) {
            StringBuffer sb = new StringBuffer(roc.getOrderBy());
            if (roc.getOrdering() < 0) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
            sql.append(" ORDER BY " + sb.toString());
        } else {
            /* add default Sort fields:
             * last name
             */
            sql.append(" ORDER BY p.last_nm ASC ");
        }
        
        return sql.toString();
    }            
    
    /**
     * Creates a AffiliateOfficerMaintenance object from a given row in the result set      
     *
     */              
    private AffiliateOfficerMaintenance readOfficerMaintenanceRecord(ResultSet rs) throws SQLException
    {    
        AffiliateOfficerMaintenance aom = new AffiliateOfficerMaintenance();
        aom.setOfficerPersonPk(new Integer(rs.getInt(1)));
        aom.setOfficerTitle(rs.getString(2));
        aom.setMonthOfElection(new Integer(rs.getInt(3)));
        aom.setEndTerm(new Integer(rs.getInt(4)));
        aom.setOriginalEndTerm(new Integer(rs.getInt(4)));
        aom.setSuspended(rs.getBoolean(5));
        
        Integer status = new Integer(rs.getInt(6));
        if (MemberStatus.T.equals(status))
            aom.setTemporaryMember(true);
        else
            aom.setTemporaryMember(false);
                
        aom.setFirstName(rs.getString(7));
        aom.setMiddleName(rs.getString(8));
        aom.setLastName(rs.getString(9));        
        aom.setSuffix(new Integer(rs.getInt(10)));
        aom.setReportingOfficer(rs.getBoolean(11));
        aom.setSteward(new Boolean(rs.getBoolean(12)));
        aom.setExecutiveBoard(rs.getBoolean(13));
        
        AffiliateIdentifier ai = new AffiliateIdentifier();
        if (rs.getString(14) != null)
            ai.setType(new Character(rs.getString(14).toCharArray()[0]));
        else
            ai.setType(null);
        ai.setLocal(rs.getString(15));
        ai.setState(rs.getString(16));
        ai.setSubUnit(rs.getString(17));
        ai.setCouncil(rs.getString(18));
        if (rs.getString(19) != null)        
            ai.setCode(new Character(rs.getString(19).toCharArray()[0]));
        else
            ai.setCode(null);

        aom.setAi(ai);
        int termLengthCode = rs.getInt(22);
        int termLength = 0;
        switch (termLengthCode) {
            case (63001) :
                termLength = 1;
                break;
            case (63002) :
                termLength = 2;
                break;
            case (63003) : 
                termLength = 3;
                break;
            case (63004) :
                termLength = 4;
                break;
       }
       aom.setLengthOfTerm(new Integer(termLength));
       aom.setAfscmeOfficePk(new Integer(rs.getInt(23)));
       aom.setOfficeGroupPk(new Integer(rs.getInt(24)));
       aom.setSurrKey(new Integer(rs.getInt(25)));
       Calendar cal = DateUtil.getCalendar(rs.getTimestamp(26));
       if (cal != null) {
           aom.setExpirationYear(new Integer(cal.get(Calendar.YEAR)));
           aom.setExpirationMonth(new Integer(cal.get(Calendar.MONTH) + 1));
           aom.setOriginalExpirationYear(new Integer(cal.get(Calendar.YEAR)));
       } 
       aom.setElectedOfficerFg(new Boolean(rs.getBoolean(27)));       
       return aom;
    }
    
    /**
     * Creates a EBoardMaintenance object from a given row in the result set      
     *
     */             
    private EBoardMaintenance readEBoardMaintenanceRecord(ResultSet rs) throws SQLException
    {   
        EBoardMaintenance ebm = new EBoardMaintenance();
        
        ebm.setOfficerPersonPk(new Integer(rs.getInt(1)));
        ebm.setEndTerm(new Integer(rs.getInt(2)));
        ebm.setMonthOfElection(new Integer(rs.getInt(3)));
        
        ebm.setSuspended(rs.getBoolean(4));
        
        Integer status = new Integer(rs.getInt(5));
        if (MemberStatus.T.equals(status))
            ebm.setTemporaryMember(true);
        else
            ebm.setTemporaryMember(false);
                
        ebm.setFirstName(rs.getString(6));
        ebm.setMiddleName(rs.getString(7));
        ebm.setLastName(rs.getString(8));        
        ebm.setSuffix(new Integer(rs.getInt(9)));        
        
        AffiliateIdentifier ai = new AffiliateIdentifier();
        if (rs.getString(10) != null)
            ai.setType(new Character(rs.getString(10).toCharArray()[0]));
        else
            ai.setType(null);
        ai.setLocal(rs.getString(11));
        ai.setState(rs.getString(12));
        ai.setSubUnit(rs.getString(13));
        ai.setCouncil(rs.getString(14));
        if (rs.getString(15) != null)        
            ai.setCode(new Character(rs.getString(15).toCharArray()[0]));
        else
            ai.setCode(null);
        
        ebm.setAi(ai);
        
        return ebm;
    }    
    
    /**
     * Creates a ReplaceOfficerResults object from a given row in the result set      
     *
     */       
    private ReplaceOfficerResults readReplaceOfficerResultsRecord(ResultSet rs) throws SQLException
    {   
        ReplaceOfficerResults ror = new ReplaceOfficerResults();
        
        ror.setPersonPk(new Integer(rs.getInt(1)));
        ror.setFirstName(rs.getString(2));
        ror.setMiddleName(rs.getString(3));
        ror.setLastName(rs.getString(4));        
        ror.setSuffix(new Integer(rs.getInt(5)));     

        AffiliateIdentifier ai = new AffiliateIdentifier();
        if (rs.getString(6) != null)
            ai.setType(new Character(rs.getString(6).toCharArray()[0]));
        else
            ai.setType(null);
        if (rs.getString(7) != null)          
            ai.setLocal(rs.getString(7));
        if (rs.getString(8) != null)        
            ai.setState(rs.getString(8));
        if (rs.getString(9) != null)        
            ai.setSubUnit(rs.getString(9));
        if (rs.getString(10) != null)
            ai.setCouncil(rs.getString(10));
        if (rs.getString(11) != null)        
            ai.setCode(new Character(rs.getString(11).toCharArray()[0]));
        else
            ai.setCode(null);        
        ror.setAi(ai);
        
        ror.setAddr1(rs.getString(12));
        ror.setAddr2(rs.getString(13));
        ror.setCity(rs.getString(14));        
        ror.setState(rs.getString(15));
        ror.setZip(rs.getString(16)); 
        ror.setAffPk(new Integer(rs.getInt(17)));
        return ror;
    }            
     
    protected String getSortColumn(SortData sortData)
    {
        String sortColumn = null;
        
        switch (sortData.getSortField()) {
            case OfficeData.SORT_FIELD_CONSTITUTIONALTITLE:
                sortColumn = "afscme_title_nm";
                break;
            case OfficeData.SORT_FIELD_AFFILIATETITLE:
                sortColumn = "affiliate_office_title";
                break;
            case OfficeData.SORT_FIELD_NUMWITHTITLE:
                sortColumn = "max_number_in_office";
                break;
            case OfficeData.SORT_FIELD_MONTHOFELECTION:
                sortColumn = "month_of_election";
                break;
            case OfficeData.SORT_FIELD_LENGTHOFTERM:
                sortColumn = "length_of_term";
                break;
            case OfficeData.SORT_FIELD_DELEGATEPRIORITY:
                sortColumn = "delegate_priority";
                break;
            case OfficeData.SORT_FIELD_TERMEND:
                sortColumn = "current_term_end";
                break;
            case OfficeData.SORT_FIELD_RO:
                sortColumn = "reporting_officer_fg";
                break;
            case OfficeData.SORT_FIELD_EBOARD:
                sortColumn = "executive_board_fg";
                break;
            case OfficeData.SORT_FIELD_PRIORITY:
                sortColumn = "priority";
                break;
            case OfficeData.SORT_FIELD_NONE:
                sortColumn = null;
                break;
            default:
                throw new EJBException("Invalid sort field: "+sortData.getSortField());
        }
        
        if (sortColumn != null && sortData.getDirection() == SortData.DIRECTION_DESCENDING)
            sortColumn = sortColumn + " DESC";
        
        return sortColumn;
    }                     
    
    protected String getSortColumnOfficerHistory(SortData sortData)
    {
        String sortColumn = null;
        
        switch (sortData.getSortField()) {
            case OfficerData.SORT_FIELD_OFFICERTITLE:
                sortColumn = "title";
                break;
            case OfficerData.SORT_FIELD_OFFICERFULLNM:
                sortColumn = "full_name";
                break;
            case OfficerData.SORT_FIELD_TERMSTART:
                sortColumn = "pos_start_dt";
                break;
            case OfficerData.SORT_FIELD_TERMEND:
                sortColumn = "pos_end_dt";
                break;
            case OfficerData.SORT_FIELD_PERSONPK:
                sortColumn = "personpk";
                break;
            case OfficerData.SORT_FIELD_NONE:
                sortColumn = null;
                break;
            default:
                throw new EJBException("Invalid sort field: "+sortData.getSortField());
        }
        
        if (sortColumn != null && sortData.getDirection() == SortData.DIRECTION_DESCENDING)
            sortColumn = sortColumn + " DESC";
        
        return sortColumn;
    }                     
    
    /** Returns true if the officer can hold the position in a given Affiliate.
     *
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * @return boolean
     * @param personPk
     * @param affPk
     */
     public boolean isOfficerEligibleToHoldPosition(Integer personPk, Integer affPk) { 
         Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null; 
         boolean eligible = true;
	                     
         try {                       
	     con = DBUtil.getConnection();
             ps = con.prepareStatement(SQL_SELECT_ELIGIBLE_MAINTAIN_OFFICER);
             ps.setInt(1, personPk.intValue());        
             ps.setInt(2, affPk.intValue());        
             rs = ps.executeQuery();
             if (rs.next()) {
                 eligible = false;
             } 
         } catch (SQLException e) {
             throw new EJBException(e);
         }
         finally {			
             DBUtil.cleanup(con, ps, rs);
         } 
        
         return eligible;
     }         
    
}
