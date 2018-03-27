package org.afscme.enterprise.organization.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import org.afscme.enterprise.codes.Codes.EmailType;
import org.afscme.enterprise.codes.Codes.OrgPhoneType;
import org.afscme.enterprise.codes.Codes.OrganizationSubType;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAssociateResult;
import org.afscme.enterprise.organization.OrganizationAssociateData;
import org.afscme.enterprise.organization.OrganizationCriteria;
import org.afscme.enterprise.organization.OrganizationData;
import org.afscme.enterprise.organization.OrganizationResult;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.organization.ejb.MaintainOrgLocations;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.apache.log4j.Logger;


/**
 * Encapsulates access to organization data
 *
 * @ejb:bean name="MaintainOrganizations" display-name="MaintainOrganizations"
 *              jndi-name="MaintainOrganizations"
 *              type="Stateless" view-type="local"
 */
public class MaintainOrganizationsBean extends SessionBase {
    
    static Logger logger = Logger.getLogger(MaintainOrganizationsBean.class);
    
    /** Reference to the MaintainUsers EJB */
    private MaintainUsers usersBean;
    
    /** Reference to the MaintainPersons EJB */
    private MaintainPersons personsBean;
    
    /** Reference to the MaintainOrgLocations EJB */
    private MaintainOrgLocations orgLocationsBean;
    
    /** Select the count of External Organizations */
    private static final String SQL_SELECT_EXT_ORGS_COUNT =
        "SELECT             COUNT(DISTINCT eo.org_pk) " +
        "FROM               External_Organizations eo " +
        "JOIN               Org_Parent po   ON po.org_pk = eo.org_pk " +
        "LEFT OUTER JOIN    Common_Codes c  ON c.com_cd_pk  = eo.ext_org_type ";
    
    /** Selects all External Organizations */
    private static final String SQL_SELECT_EXT_ORGANIZATIONS =
        "SELECT             DISTINCT eo.org_pk, org_nm, ext_org_type, org_web_site, " +
        "                   marked_for_deletion_fg, org_email_domain, com_cd_desc " +
        "FROM               External_Organizations eo " +
        "JOIN               Org_Parent po   ON po.org_pk    = eo.org_pk " +
        "LEFT OUTER JOIN    Common_Codes c  ON c.com_cd_pk  = eo.ext_org_type ";
    
    /** Where clause to add for search and select of External Organizations */
    private static final String SQL_WHERE_EXT_ORGS =
        "WHERE  po.org_subtype = ? ";
    
    /** Join clauses for any Location search criteria */
    private static final String SQL_JOIN_WITH_LOC =
        "LEFT OUTER JOIN    Org_Locations loc   ON loc.org_pk = eo.org_pk ";
    
    /** Join clauses for Address search criteria */
    private static final String SQL_JOIN_WITH_ADDR =
        "LEFT OUTER JOIN    Org_Address adr     ON adr.org_locations_pk = loc.org_locations_pk ";
    
    /** Join clauses for Address search criteria for just office phone */
    private static final String SQL_JOIN_WITH_OFFICE_PHONE =
        "LEFT OUTER JOIN    Org_Phone offph     ON offph.org_locations_pk = loc.org_locations_pk ";
    
    /** Join clauses for Address search criteria for just fax phone */
    private static final String SQL_JOIN_WITH_FAX_PHONE =
        "LEFT OUTER JOIN    Org_Phone faxph     ON faxph.org_locations_pk = loc.org_locations_pk ";
    
    /** Verifies if any existing External Organizations with the same name */
    private static final String SQL_VERIFY_EXT_ORGANIZATIONS =
        "SELECT             eo.org_pk, org_nm, ext_org_type " +
        "FROM               External_Organizations eo " +
        "JOIN               Org_Parent po   ON po.org_pk    = eo.org_pk " +
        "LEFT OUTER JOIN    Common_Codes c  ON c.com_cd_pk  = eo.ext_org_type " +
        "WHERE              po.org_subtype = ? " +
        "AND                org_nm = ? ";
    
    /** Select the count of External Organizations with the same name */
    private static final String SQL_VERIFY_EXT_ORGS_COUNT =
        "SELECT COUNT(*) " +
        "FROM   External_Organizations eo " +
        "JOIN   Org_Parent po   ON po.org_pk = eo.org_pk " +
        "WHERE  po.org_subtype = ? " +
        "AND    org_nm = ? ";
    
    /** Inserts a new External Organization */
    private static final String SQL_INSERT_NEW_EXT_ORGANIZATION =
        "INSERT INTO External_Organizations " +
        "   (org_pk, org_nm, org_web_site, ext_org_type, marked_for_deletion_fg, " +
        "   org_email_domain, " +
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), ?, GETDATE() )";
    
    /** Inserts into the Org_Parent table */
    private static final String SQL_INSERT_INTO_ORG_PARENT =
        "INSERT INTO Org_Parent (org_subtype) VALUES (?)";
    
    /** Gets the name for an organization */
    private static String SQL_SELECT_ORGANIZATION_NAME =
        "SELECT org_nm FROM External_Organizations WHERE org_pk = ? ";
    
    /** Selects an External Organization based on its primary key. */
    private static final String SQL_SELECT_ORGANIZATION_DETAIL =
        "SELECT org_nm, ext_org_type, org_web_site, marked_for_deletion_fg, " +
        "       org_email_domain " +
        "FROM   External_Organizations " +
        "WHERE  org_pk = ?";
    
    /** Updates the External_Organizations table */
    private static final String SQL_UPDATE_ORGANIZATION_DETAIL =
        "UPDATE External_Organizations " +
        "SET org_nm = ?, ext_org_type = ?, org_web_site = ?, marked_for_deletion_fg = ?, " +
        "    org_email_domain = ?, " +
        "    lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE org_pk = ?";
    

    /** SELECTS Organization Associates count for an External Organization */
    private static final String SQL_SELECT_EXT_ORG_ASSOCIATES_COUNT =
        "SELECT COUNT(*) " +
        "FROM   Ext_Org_Associates eoa ";
    
    /** FROM clause for selecting Organization Associates */
    private static final String SQL_FROM_EXT_ORG_ASSOCIATES =
        "FROM               Ext_Org_Associates eoa " +
        "INNER JOIN         Person p                ON  p.person_pk         = eoa.person_pk " +
        "LEFT OUTER JOIN    Person_Email email      ON  email.person_pk     = p.person_pk " +
        "                                           AND email.email_type    = " + EmailType.PRIMARY + " " +
        "LEFT OUTER JOIN    Org_Locations l         ON  l.org_locations_pk  = eoa.org_locations_pk " +
        "LEFT OUTER JOIN    Org_Phone phone         ON  phone.org_locations_pk = l.org_locations_pk " +
        "                                           AND phone.org_phone_type = " + OrgPhoneType.LOC_PHONE_OFFICE + " " + 
        "LEFT OUTER JOIN    Common_Codes titles     ON  titles.com_cd_pk    = eoa.org_pos_title ";
    
    /** SELECTS Organization Associates for an External Organization */
    private static final String SQL_SELECT_EXT_ORG_ASSOCIATES =
        "SELECT p.last_nm + ', ' + p.first_nm as name, " +
        "       titles.com_cd_desc as title, " +
        "       location_nm,  " +
        "       phone.area_code + '-' + phone.phone_no as full_phone, " +
        "       email.person_email_addr, " +
        "       eoa.org_pk, p.person_pk " +
        SQL_FROM_EXT_ORG_ASSOCIATES;
    
    /** SELECTS single Organization Associate for an External Organization */

    private static final String SQL_SELECT_SINGLE_EXT_ORG_ASSOCIATE_LOC =
        "SELECT         org_nm, org_pos_title, org_locations_pk, " +
        "               eoa.created_user_pk, eoa.created_dt, eoa.lst_mod_user_pk, eoa.lst_mod_dt " +
        "FROM           Ext_Org_Associates eoa " +
        "INNER JOIN     External_Organizations eo   ON  eo.org_pk   = eoa.org_pk " +
        "WHERE          eoa.org_pk = ? " +
        "AND            person_pk = ? ";


    private static final String SQL_SELECT_SINGLE_EXT_ORG_ASSOCIATE =
        "SELECT         org_nm, org_pos_title, org_locations_pk, " +
        "               eoa.created_user_pk, eoa.created_dt, p.person_mst_lst_mod_user_pk, p.person_mst_lst_mod_dt " +
        "FROM           Ext_Org_Associates eoa " +
        "INNER JOIN     External_Organizations eo   ON  eo.org_pk   = eoa.org_pk " +
        "INNER JOIN     Person p    ON  p.person_pk = eoa.person_pk " +
        "WHERE          eoa.org_pk = ? " +
        "AND            eoa.person_pk = ? ";    
    
    /** SELECTS the latest comment for an organization associates */
    public static final String SQL_SELECT_LATEST_COMMENT =
        "SELECT     TOP 1 comment_txt, comment_dt, created_user_pk " +
        "FROM       Ext_Org_Associate_Comments " +
        "WHERE      org_pk = ? " +
        "AND        person_pk = ? " +
        "ORDER BY   comment_dt DESC";
    
    /** SELECTS all comments for an organization associate */
    public static final String SQL_SELECT_COMMENT_HISTORY =
        "SELECT     comment_txt, comment_dt, created_user_pk " +
        "FROM       Ext_Org_Associate_Comments " +
        "WHERE      org_pk = ? " +
        "AND        person_pk = ? " +
        "ORDER BY   comment_dt DESC";
    
    /** INSERTS a comment for an organization associate */
    private static final String SQL_INSERT_COMMENT  =
        "INSERT INTO Ext_Org_Associate_Comments " +
        "(org_pk, person_pk, comment_dt, comment_txt, created_user_pk) " +
        "VALUES " +
        "(?      , ?       , getdate() , ?          , ?             ) ";

    
    /** INSERTS a new organization associate record */
    private static final String SQL_INSERT_EXT_ORG_ASSOCIATE = 
        "INSERT INTO Ext_Org_Associates " +
        "   (org_pk, person_pk, org_pos_title, org_locations_pk, " + 
        "   created_user_pk, created_dt, lst_mod_user_pk, lst_mod_dt) " +
        "VALUES (?, ?, ?, ?, ?, GETDATE(), ?, GETDATE() ) ";
    
    /** UPDATES an organization associate record */
    private static final String SQL_UPDATE_EXT_ORG_ASSOCIATE = 
        "UPDATE Ext_Org_Associates " + 
        "SET    org_pos_title = ?, " +
        "       lst_mod_user_pk = ?, lst_mod_dt = GETDATE() " +
        "WHERE  org_pk = ? AND person_pk = ? ";

    /** CHECKS for existing org associate (used prior to add) */
    private static String SQL_SELECT_EXISTING_EXT_ORG_ASSOCIATE =
        "SELECT CASE " +
        "       WHEN COUNT(person_pk) > 0 THEN 1 " +
        "       ELSE 0 " +
        "       END 'Existing' " +
        "FROM Ext_Org_Associates " +
        "WHERE  org_pk = ? AND person_pk = ? ";
    
    /** DELETES an organization associate record */
    private static final String SQL_DELETE_EXT_ORG_ASSOCIATE = 
        "DELETE FROM Ext_Org_Associates WHERE org_pk=? AND person_pk=? ";

    /** SETS an organization associate location */
    private static final String SQL_SET_LOCATION  =
        "UPDATE Ext_Org_Associates " +
        "SET    org_locations_pk = ? " +
        "WHERE  org_pk = ? " +
        "AND    person_pk = ? ";

    /** SELECTS all organization associate information (associated orgs) for a person */
    private static final String SQL_SELECT_PERSON_EXT_ORG_ASSOCIATES = 
        "SELECT org_nm as name, titles.com_cd_desc as title, l.location_nm, " + 
        "       eoa.org_pk,  eoa.person_pk " + 
        "FROM               Ext_Org_Associates eoa " + 
        "INNER JOIN         External_Organizations eo   ON  eo.org_pk = eoa.org_pk " + 
        "LEFT OUTER JOIN    Org_Locations l             ON  l.org_locations_pk  = eoa.org_locations_pk " + 
        "LEFT OUTER JOIN    Common_Codes titles         ON  titles.com_cd_pk    = eoa.org_pos_title " + 
        "WHERE              eoa.person_pk = ? ";

    
    /** Columns to sort on, corresponding to the field ids in OrganizationCriteria.
     * It is important that these are in the same order
     * Sort options are by org_nm (org name) or com_cd_desc (ext_org_type) */
    private final String[] SORT_FIELD_COLUMNS = new String[] {
        "org_nm", "com_cd_desc"
    };
    
    
    /** Gets references to the dependent EJBs */
    public void ejbCreate() throws CreateException {
        try {
            usersBean = JNDIUtil.getMaintainUsersHome().create();
            personsBean = JNDIUtil.getMaintainPersonsHome().create();
            orgLocationsBean = JNDIUtil.getMaintainOrgLocationsHome().create();
        } catch (NamingException e) {
            throw new EJBException("Unable to find dependent EJBs in MaintainOrganizationsBean.ejbCreate()" + e);
        }
    }
    
    /**
     * Searches for and returns a set of OrganizationData objects thatr match the organizationCriteria
     *  established for the search
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return int - count of number of organizations that meet criteria
     * @param organCriteria - search criteria contained within an object of the OrganizationCriteria
     *  class
     * @param java.util.List - a collection of OrganizationResult objects
     */
    public int searchOrgs(OrganizationCriteria organCriteria, List result) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        
        //retrieve all External Organizations that fit the criteria
        try {
            
            PreparedStatementBuilder builder = new PreparedStatementBuilder(2);
            
            // check for like for organization name
            if ((organCriteria.getOrgName() != null) && (organCriteria.getOrgName().indexOf("%") > 0)) {
                builder.addCriterion("org_nm", organCriteria.getOrgName(), true);
            }
            else {
                builder.addCriterion("org_nm", organCriteria.getOrgName());
            }
            
            // add to where clause for org type
            builder.addCriterion("ext_org_type", organCriteria.getOrgType());
            builder.addCriterion("org_web_site", organCriteria.getOrgWebSite());
            
            // add to where clause for marked for deletion flag
            // for all, don't add to the where clause
            if (organCriteria.getMarkedForDeletion() != 2)
                builder.addCriterion("marked_for_deletion_fg", new Integer(organCriteria.getMarkedForDeletion()));
            
            // add criteria for address fields
            builder.addCriterion("attention_line", organCriteria.getAttentionLine());
            builder.addCriterion("addr1", organCriteria.getAddr1());
            builder.addCriterion("addr2", organCriteria.getAddr2());
            builder.addCriterion("city", organCriteria.getCity());
            builder.addCriterion("state", organCriteria.getState());
            builder.addCriterion("zipcode", organCriteria.getZipPostal());
			builder.addCriterion("zip_plus", organCriteria.getZipPlus());
            builder.addCriterion("county", organCriteria.getCounty());
            builder.addCriterion("province", organCriteria.getProvince());
            builder.addCriterion("country", organCriteria.getCountry());
            
            // determine the match for the whole day for query matching
            if (organCriteria.getLastUpdateDate() != null) {
                // set to the first second of the day
                Criterion updDate = new Criterion("adr.lst_mod_dt",
                Timestamp.valueOf(((organCriteria.getLastUpdateDate().toString()).substring(0,10))
                + " 00:00:00.000"));
                // set to the check for between first second and last second of the day
                updDate.setOperator("BETWEEN");
                // set to the last second of the day
                updDate.addValue(Timestamp.valueOf(((organCriteria.getLastUpdateDate().toString()).substring(0,10))
                + " 23:59:59.999"));
                builder.addCriterion(updDate);
            }
            
            // determine the update user id passed in for query matching
            if (!TextUtil.isEmptyOrSpaces(organCriteria.getLastUpdateUser())) {
                try {
                    Integer personPK = usersBean.getPersonPK(organCriteria.getLastUpdateUser());
                    builder.addCriterion("adr.lst_mod_user_pk", personPK);
                }
                catch (EJBException e)  {
                    // no match on userid, set user to be 0 for no matches
                    builder.addCriterion("adr.lst_mod_user_pk", new Integer(0));
                }
            }
            
            // add criteria for phone fields
            if (organCriteria.hasOfficePhoneSearchCriteria())  {
                builder.addCriterion("offph.country_cd", organCriteria.getOfficeCountryCode());
                builder.addCriterion("offph.area_code", organCriteria.getOfficeAreaCode());
                builder.addCriterion("offph.phone_no", organCriteria.getOfficePhoneNo());
                builder.addCriterion("offph.org_phone_type", OrgPhoneType.LOC_PHONE_OFFICE);
            }
            if (organCriteria.hasFaxPhoneSearchCriteria())  {
                builder.addCriterion("faxph.country_cd", organCriteria.getFaxCountryCode());
                builder.addCriterion("faxph.area_code", organCriteria.getFaxAreaCode());
                builder.addCriterion("faxph.phone_no", organCriteria.getFaxPhoneNo());
                builder.addCriterion("faxph.org_phone_type", OrgPhoneType.LOC_PHONE_FAX);
            }
            
            //make the statement that gets the count
            con = DBUtil.getConnection();
            
            StringBuffer sql = new StringBuffer(SQL_SELECT_EXT_ORGS_COUNT);
            
            // add the location join
            if ((organCriteria.hasAddressSearchCriteria()) ||
                (organCriteria.hasOfficePhoneSearchCriteria()) ||
                (organCriteria.hasFaxPhoneSearchCriteria()))
                sql.append(SQL_JOIN_WITH_LOC);
            
            // add the address join
            if (organCriteria.hasAddressSearchCriteria())
                sql.append(SQL_JOIN_WITH_ADDR);
            // add the phone join
            if (organCriteria.hasOfficePhoneSearchCriteria())
                sql.append(SQL_JOIN_WITH_OFFICE_PHONE);
            if (organCriteria.hasFaxPhoneSearchCriteria())
                sql.append(SQL_JOIN_WITH_FAX_PHONE);
            
            // add the org sub type where clause
            sql.append(SQL_WHERE_EXT_ORGS);
            
            ps = builder.getPreparedStatement(sql.toString(), con, false);
            
            // retrieve the external organization key for org sub type
            ps.setInt(1, OrganizationSubType.O.intValue());
            
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
            if (organCriteria != null &&        //<-- don't sort if sort is null
                organCriteria.getSortField() != OrganizationCriteria.SORT_NONE) {  //<-- don't sort if 'none' specified
                
                orderBy = (String)CollectionUtil.transliterate(
                organCriteria.getSortField(),
                OrganizationCriteria.SORT_FIELD_IDS,
                SORT_FIELD_COLUMNS);
                
                if (organCriteria.getSortOrder() == OrganizationCriteria.SORT_DESCENDING)
                    orderBy += " DESC";
            }
            
            if (orderBy != null)
                builder.addOrderBy(orderBy);
            
            //create the statement that gets the data
            sql = new StringBuffer(SQL_SELECT_EXT_ORGANIZATIONS);
            
            // add the location join
            if ((organCriteria.hasAddressSearchCriteria()) ||
                (organCriteria.hasOfficePhoneSearchCriteria()) ||
                (organCriteria.hasFaxPhoneSearchCriteria()))
                    sql.append(SQL_JOIN_WITH_LOC);
            
            // add the address join
            if (organCriteria.hasAddressSearchCriteria())
                sql.append(SQL_JOIN_WITH_ADDR);
            // add the phone join
            if (organCriteria.hasOfficePhoneSearchCriteria())
                sql.append(SQL_JOIN_WITH_OFFICE_PHONE);
            if (organCriteria.hasFaxPhoneSearchCriteria())
                sql.append(SQL_JOIN_WITH_FAX_PHONE);
            
            // add the org sub type where clause
            sql.append(SQL_WHERE_EXT_ORGS);
            
            ps = builder.getPreparedStatement(sql.toString(), con, false);
            
            // retrieve the external organization key for org sub type
            ps.setInt(1, OrganizationSubType.O.intValue());
            rs = ps.executeQuery();
            
            if (organCriteria != null)
                rs.absolute((organCriteria.getPage() * organCriteria.getPageSize()) + 1);
            else
                rs.next();
            
            //put the results into a list of OrganizationResult objects
            int index = 0;
            int pageSize = organCriteria != null ? organCriteria.getPageSize() : 0;
            int startIndex = organCriteria == null ? 0 : organCriteria.getPage() * pageSize;
            while ((index + startIndex < count) &&
                   (organCriteria == null || index < pageSize)) {

                OrganizationResult orgResult = new OrganizationResult();
                orgResult.setOrgPK(new Integer(rs.getInt("org_pk")));
                orgResult.setOrgNm(rs.getString("org_nm"));
                orgResult.setOrgType(new Integer(rs.getInt("ext_org_type")));
                result.add(orgResult);
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
     * Gets a organization name for a orgPK (used for header on detail pages)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return String - the name of the organization
     * @param orgPK The primary key of the organization
     */
    public String getOrganizationName(Integer orgPK) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        
        //retrieve the External Organization Name for the orgPK
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_NAME);
            
            // set the external organization key
            ps.setInt(1, orgPK.intValue());
            rs = ps.executeQuery();
            if (!rs.next())
                throw new EJBException("Could not find organization with pk '" + orgPK + "' in MaintainOrganizationsBean.getOrganizationName()");
            
            result = rs.getString("org_nm");
            
        } catch (SQLException e) {
            throw new EJBException("Error getting Org Name MaintainOrganizationsBean.getOrganizationName()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * retrieves detail information about an organization and returns this data in an OrganizationData
     *  object.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return OrganizationData - a single OrganizationData object is returned
     * @param orgPK - the OrgPk of the external organization for which data is to be returned
     */
    public OrganizationData getOrgDetail(Integer orgPK) {
      
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrganizationData orgData = null;
        
        //retrieve the External Organization for the orgPK
        try {
            //make the statement that gets the count
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_ORGANIZATION_DETAIL);
            
            // retrieve the external organization key for org PK
            ps.setInt(1, orgPK.intValue());
            
            //get the organization
            rs = ps.executeQuery();
            
            while (rs.next()) {
                
                orgData = new OrganizationData();
                orgData.setOrgPK(orgPK);
                orgData.setOrgNm(rs.getString("org_nm"));
                orgData.setOrgType(new Integer(rs.getInt("ext_org_type")));
                orgData.setOrgWebURL(rs.getString("org_web_site"));
                orgData.setMarkedForDeletion(DBUtil.getBooleanFromShort(rs.getShort("marked_for_deletion_fg")));
                orgData.setOrgEmailDomain(rs.getString("org_email_domain"));
                
                try {
                    LocationData orgPrimaryLocation = orgLocationsBean.getOrgPrimaryLocation(orgPK);
                    orgData.setPrimaryLocationData(orgPrimaryLocation);
                }
                catch (EJBException e)  {
                }
            }
            
        } catch (SQLException e) {
            throw new EJBException("Error getting Org detail MaintainOrganizationsBean.getOrgDetail()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return orgData;
    }
    
    /**
     * Updates the detail data for an organization. Does not update the primary location
     *  for the organization, even if the OrganizationData object contains a reference to
     *  a primary location.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return void
     * @param orgData - the organization data to be changed
     * @param updatedByUserPk - User Pk of the user changing the data
     */
    public void updateOrgDetail(OrganizationData orgData, Integer updatedByUserPk) {
        
        Connection con = null;
        PreparedStatement ps = null;
        
        //update the External Organization
        try {
            con = DBUtil.getConnection();
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Update statement: [" + SQL_UPDATE_ORGANIZATION_DETAIL + "] ");
            
            ps = con.prepareStatement(SQL_UPDATE_ORGANIZATION_DETAIL);
            
            // update all the organization by orgPK
            ps.setString(1, orgData.getOrgNm());
            ps.setInt(2, orgData.getOrgType().intValue());
            DBUtil.setNullableVarchar(ps, 3, orgData.getOrgWebURL());
            DBUtil.setBooleanAsShort(ps, 4, orgData.getMarkedForDeletion().booleanValue());
            DBUtil.setNullableVarchar(ps, 5, orgData.getOrgEmailDomain());
            ps.setInt(6, updatedByUserPk.intValue());
            
            // set the orgPK to update
            ps.setInt(7, orgData.getOrgPK().intValue());
            
            // update into External_Organizations
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException("Error updating Org detail MaintainOrganizationsBean.updateOrgDetail()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * Adds an external organization to the Enterprise Database.
     *
     * If a URL is not input for the organization web site, a NULL or  blank string will
     *  be set in the database
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return - Integer representing the newly created primary key of the organization
     * @param orgName - the name of the organization to be added
     * @param orgType - a reference to the common code value that represents the type of
     *                  the organization being added
     * @param orgURL - the URL for the website of the organization, if one exists.
     * @param createdByUserPk - User Pk of the user adding the data
     */
    public Integer addOrg(String orgName, Integer orgType, String orgURL, Integer createdByUserPk) {
        
        Connection con = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        Integer orgPk = null;
        
        try {
            
            con = DBUtil.getConnection();
            
            // insert into Org_Parent
            ps1 = con.prepareStatement(SQL_INSERT_INTO_ORG_PARENT);
            
            // retrieve the external organization key for org sub type
            ps1.setInt(1, OrganizationSubType.O.intValue());
            orgPk = DBUtil.insertAndGetIdentity(con, ps1);
            
            // DEBUG
            if (logger.isDebugEnabled())
                logger.debug("Insert statement: [" + SQL_INSERT_NEW_EXT_ORGANIZATION + "] ");
            
            // Add the External Organization...
            ps2 = con.prepareStatement(SQL_INSERT_NEW_EXT_ORGANIZATION);
            
            ps2.setInt(1, orgPk.intValue());
            ps2.setString(2, orgName);
            if (TextUtil.isEmptyOrSpaces(orgURL)) {
                orgURL = null;
            }
            DBUtil.setNullableVarchar(ps2, 3, orgURL);
            ps2.setInt(4, orgType.intValue());
            
            // default marked_for_deletion_fg to false
            DBUtil.setBooleanAsShort(ps2, 5, Boolean.FALSE.booleanValue());
            
            // set email domain as null until PAC
            ps2.setNull(6, Types.VARCHAR);
            
            ps2.setInt(7, createdByUserPk.intValue());
            ps2.setInt(8, createdByUserPk.intValue());
            
            // insert into External_Organizations
            ps2.executeUpdate();
            
        } catch (SQLException e) {
            throw new EJBException("Error inserting organization MaintainOrganizationsBean.addOrg()", e);
        } finally {
            DBUtil.cleanup(null, ps1, null);
            DBUtil.cleanup(con, ps2, null);
        }
        // Return the new Organization's PK
        return orgPk;
    }
    
    /**
     * verifies whether one or more organizations with this name already exist
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return - Integer representing number of organizations with the same name
     * @param - orgName - a string representing an organization name to be searched for
     * @param - organCriteria - Organization Criteria object 
     * @param result - a collection of OrgData objects representing organizations with the same name
     */
    public int getDuplicateOrgs(String orgName, OrganizationCriteria organCriteria, List result) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        
        //retrieve all External Organizations that match the organization name
        try {
            //make the statement that gets the count
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_VERIFY_EXT_ORGS_COUNT);
            
            // retrieve the external organization key for org sub type
            ps.setInt(1, OrganizationSubType.O.intValue());
            ps.setString(2, orgName);
            
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
            if (organCriteria != null &&        //<-- don't sort if sort is null
                organCriteria.getSortField() != OrganizationCriteria.SORT_NONE) {  //<-- don't sort if 'none' specified
                
                orderBy = (String)CollectionUtil.transliterate(
                organCriteria.getSortField(),
                OrganizationCriteria.SORT_FIELD_IDS,
                SORT_FIELD_COLUMNS);
                
                if (organCriteria.getSortOrder() == OrganizationCriteria.SORT_DESCENDING)
                    orderBy += " DESC";
            }
            
            //create the statement that gets the data
            if (orderBy != null)
                ps = con.prepareStatement(SQL_VERIFY_EXT_ORGANIZATIONS + " ORDER BY " + orderBy,
                                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            else
                ps = con.prepareStatement(SQL_VERIFY_EXT_ORGANIZATIONS,
                                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            // retrieve the external organization key for org sub type
            ps.setInt(1, OrganizationSubType.O.intValue());
            ps.setString(2, orgName);
            rs = ps.executeQuery();
            
            if (organCriteria != null)
                rs.absolute((organCriteria.getPage() * organCriteria.getPageSize()) + 1);
            else
                rs.next();
            
            //put the results into a the list of OrganizationResult objects
            int index = 0;
            int pageSize = organCriteria != null ? organCriteria.getPageSize() : 0;
            int startIndex = organCriteria == null ? 0 : organCriteria.getPage() * pageSize;
            while ((index + startIndex < count) &&
                   (organCriteria == null || index < pageSize)) {

                OrganizationResult orgResult = new OrganizationResult();
                orgResult.setOrgPK(new Integer(rs.getInt("org_pk")));
                orgResult.setOrgNm(rs.getString("org_nm"));
                orgResult.setOrgType(new Integer(rs.getInt("ext_org_type")));
                result.add(orgResult);
                rs.next();
                index++;
            }
            
        } catch (SQLException e) {
            throw new EJBException("Error getting duplicate orgs MaintainOrganizationsBean.getDuplicateOrgs()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return count;
    }
    
    /**
     * Determines whether there are any duplicate orgs based on an exact match by name.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return boolean TRUE means there is one or more organizations that match by name
     * @param orgName - a string that contains the organization name to match on
     */
    public boolean isDuplicateOrgs(String orgName) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        
        //retrieve all External Organizations that match the organization name
        try {
            //make the statement that gets the count
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_VERIFY_EXT_ORGS_COUNT);
            
            // retrieve the external organization key for org sub type
            ps.setInt(1, OrganizationSubType.O.intValue());
            ps.setString(2, orgName);
            
            //get the count
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            if (count != 0)
                return true;
            
        } catch (SQLException e) {
            throw new EJBException("Error checking for duplicate orgs MaintainOrganizationsBean.isDuplicateOrgs()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return false;
    }
    
    /**
     * Associates a person with an organization. Requires the person to exist first, of course.
     *  Records comment if one is entered when the Organization Associate is being added
     *
     * Currently associates the two entities based on PersonPK and OrgPK, and allows the
     *  associates position title to be set.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @return boolean - Returns TRUE if the association could be made, FALSE if the association fails
     * @param positionTitle - the value of the associated common code primary key from the
     *          common code table that supports associate titles for external organizations.
     * @param personPk - the primary key of the person being set as the associate for the organization
     * @param orgPk - the primary key of the organization
     * @param comments - text string of comment to be added.
     * @param userPk - User Pk of the user changing the data
     */
    public boolean setPersonAssoc(Integer orgPk, Integer personPk, Integer positionTitle, String comments, Integer userPk) {
        
        Connection con = null;
        PreparedStatement ps = null;
        boolean result;
        
        //check if org associate already exists
        if (isExistingOrgAssociate(orgPk, personPk)) {

            //update if already exists
            PersonData personData = getOrgAssociateDetail(orgPk, personPk).getPersonData();
            result = updateOrgAssociateDetail(orgPk, personData, positionTitle, comments, userPk);
        }
        else {
        
            //inserts the org associate record        
            try {
                con = DBUtil.getConnection();
                ps = con.prepareStatement(SQL_INSERT_EXT_ORG_ASSOCIATE);
                ps.setInt(1, orgPk.intValue());
                ps.setInt(2, personPk.intValue());
                DBUtil.setNullableInt(ps, 3, positionTitle);
                ps.setNull(4, Types.INTEGER);            
                ps.setInt(5, userPk.intValue());
                ps.setInt(6, userPk.intValue());
                result = (ps.executeUpdate() != 0);
            } catch (SQLException e) {
                throw new EJBException("Error inserting Org associate record in MaintainOrganizationsBean.setPersonAssoc()", e);
            } finally {
                DBUtil.cleanup(con, ps, null);
            }

            //add the comment (if it's not empty)
            if (!TextUtil.isEmpty(comments))
                addComment(orgPk, personPk, comments, userPk);
            
        }     
      
        //set the org associate location to the primary org location (if it exists)
        boolean hasPrimary = setOrgAssociateLocationasPrimary(orgPk, personPk);
        
        //if primary org location does not exist, then check if org has one location
        //and if so, set it and if more than one non-primary location, leave blank
        if (!(hasPrimary)) {
            if (orgLocationsBean.hasLocations(orgPk)) {
                List orgLocs = orgLocationsBean.getOrgLocations(orgPk);
                if (orgLocs.size() == 1) {
                    LocationData loc = (LocationData) orgLocs.get(0);
                    setOrgAssociateLocation(orgPk, personPk, loc.getOrgLocationPK());
                }
            }
        }
        
        return result;
    }
    
    /**
     * Adds a new Person to an organization by making them an Organization Associate 
     * after the Person Verify check has been made. The overall process contains 
     * a sub process of checking for and determining if the person to be added 
     * as an organization associate is already in the system, just as
     * in adding a person. 
     * 
     * A duplicate SSN check must be perfomed as part of the processing in case the user
     * has changed the SSN (when not selecting an existing person)
     * 
     * See Associated Sequence Diagram(s)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param orgPk Organization Primary Key
     * @param personPk Primary key of the person who is the organization associate
     * @param positionTitle - the value of the associated common code primary key from the
     *          common code table that supports associate titles for external organizations.
     * @param comments Comments entered when adding the organization associate
     * @param userPk Person pk of the user that is performing the action
     * @return primary key of the person added.
     */
    public Integer addPersonAssoc(Integer orgPk, NewPerson newPerson, Integer positionTitle, String comments, Integer userPk) {
        Integer personPk = personsBean.addPerson(userPk, newPerson);
        setPersonAssoc(orgPk, personPk, positionTitle, comments, userPk);
        return personPk;
    }
        
    /**
     * Allows the association of a person to an organization to be removed
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param orgPK External Organization Primary Key
     * @param personPK Person Primary Key
     * @return 'true' if the Associate person was removed, and 'false' otherwise.
     */
    public boolean removePersonAssoc(Integer orgPK, Integer personPK) {
        
        Connection con = null;
        PreparedStatement ps = null;
        boolean result;
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_DELETE_EXT_ORG_ASSOCIATE);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, personPK.intValue());
            result = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new EJBException("Error deleting org associate record in MaintainOrganizationsBean.removePersonAssoc()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return result;
    }
    
    /**
     * This method updates the detail information for an organization associate. It calls
     *  a MaintainPersons method to update information about the person that is allowed
     *  to change and then updates the External Organization Associates data.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return boolean returns TRUE if operation completes successfully, FALSE if the operation
     *  does not complete successfully.
     *
     * @param orgPk External Organization Primary Key
     * @param personData contains person data attributes which may have been updated
     * @param positionTitle - a common code key which represents the title for the organization
     *  associate
     * @param comments - Stringing containing a new comment, if one is  provided by the user
     * @param userPk - User Pk of the user changing the data
     */
    public boolean updateOrgAssociateDetail(Integer orgPk, PersonData personData, Integer positionTitle, String comments, Integer userPk) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean result;

        //update the person data
        personsBean.updatePersonDetail(userPk, personData);
        
        //update the organization associate data
        try {
            
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_EXT_ORG_ASSOCIATE);
            DBUtil.setNullableInt(ps, 1, positionTitle);
            ps.setInt(2, userPk.intValue());
            ps.setInt(3, orgPk.intValue());
            ps.setInt(4, personData.getPersonPk().intValue());
            result = (ps.executeUpdate() != 0);
            
        } catch (SQLException e) {
            throw new EJBException("Error updating org associate record in MaintainOrganizationsBean.updateOrgAssociateDetail()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        //add the comment (if it's not empty)
        if (!TextUtil.isEmpty(comments))
            addComment(orgPk, personData.getPersonPk(), comments, userPk);

        return result;        
    }
    
    /**
     * retrieves location data for the organization associate
     *
     * This method will first retrieve the OrgLocationPK for the associate and then use the
     *  MaintainOrgLocationsBean.getOrgLocation() nethod to retrieve the location data
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @return LocationData - contains the location data for the associate
     *
     * @param orgPK - the organization in question
     * @param personPK - the primary key of the person who is associated with the organization
     */
    public LocationData getOrgAssociateLocation(Integer orgPK, Integer personPK) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LocationData result = null;
        
        con = DBUtil.getConnection();
        
        //retrieve the one Organization Associate for an External Organization
        try {
            
            //retrieve the org associate main data
            ps = con.prepareStatement(SQL_SELECT_SINGLE_EXT_ORG_ASSOCIATE_LOC);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, personPK.intValue());
            rs = ps.executeQuery();
            if (!rs.next())
                throw new EJBException("Org Associate with Org_Pk="+orgPK+" and Person_Pk="+personPK+" not found ");

            //get the org location pk 
            Integer orgLocationPk = new Integer(rs.getInt("org_locations_pk"));
            result = orgLocationsBean.getOrgLocation(orgLocationPk);
            
        } catch (SQLException e) {
            throw new EJBException("Error getting org associates location data in MaintainOrganizationsBean.getOrgAssociateLocation()",e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * Allows a location to be set for an Organization Associate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @return boolean - TRUE if the operation completes successfully, FALSE if the association
     *                   could not be made.
     * @param orgPK - the organization of the associate
     * @param personPK - the person identified as the associate
     * @param orgLocationPK - the location to be associated with the associate
     */
    public boolean setOrgAssociateLocation(Integer orgPK, Integer personPK, Integer orgLocationPK) {

        Connection con = null;
        PreparedStatement ps = null;
        boolean result;        
        
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SET_LOCATION);
            ps.setInt(1, orgLocationPK.intValue());
            ps.setInt(2, orgPK.intValue());
            ps.setInt(3, personPK.intValue());
            result = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new EJBException("Error setting location for org associate in MaintainOrganizationsBean.setOrgAssociateLocation()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    
        return result;
    }
    
    /**
     * Allows the location to be associated with the associate to be set as the current primary
     *  location of the organization.
     *
     * If no primary location exists for the organization, then the operation completes,
     *  but the location for the associate is left in its current state.
     *
     * This method is generally used when a new associate is being added. Is so, if no primary
     *  location exists for the organization, the organization associate will be added with
     *  a Null location
     *
     * Makes use of MaintainOrgLocationsBean.getOrgPrimaryLocation() to return the OrgLocationPK
     *  for the organization
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @return boolean - TRUE if the primary location exists and could be set, FALSE if the
     *                   primary location does not exist or could not be set
     * @param orgPK - primary key of the organization
     * @param personPK - primary key of the person associated as the organization associate
     */
    public boolean setOrgAssociateLocationasPrimary(Integer orgPK, Integer personPK) {
        
        //check if the org has a primary location
        Integer locationPk = orgLocationsBean.getOrgPrimaryLocationPK(orgPK);
        
        if (locationPk != null) {
            //if primary exists, set it for the associate
            return (setOrgAssociateLocation(orgPK, personPK, locationPk));
        }
        
        return false;
    }
    
    /**
     * returns the set of Organization Associates for a given organization
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return java.util.Collections - a collection of OrgAssociateResult objects
     * @param orgPK - primary key of the organization
     */
    public int getOrgAssociates(Integer orgPK, SortData sortData, List result) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total;
        
        con = DBUtil.getConnection();
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        builder.addCriterion("eoa.org_pk", orgPK);
        
        //retrieve all the Organization Associates for an External Organization
        try {
            
            //get the count
            ps = builder.getPreparedStatement(SQL_SELECT_EXT_ORG_ASSOCIATES_COUNT, con);
            rs = ps.executeQuery();
            rs.next();
            total = rs.getInt(1);
            if (total == 0)
                return 0;
            
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            
            //get the org associates
            String sortColumn = getSortColumn(sortData);
            if (sortColumn != null)
                builder.addOrderBy(sortColumn);
            ps = builder.getPreparedStatement(SQL_SELECT_EXT_ORG_ASSOCIATES, con);
            rs = ps.executeQuery();
            
            rs.absolute((sortData.getPage() * sortData.getPageSize()) + 1);
            
            //put the results into a list of OrgAssociateResult objects
            int index = 0;
            int startIndex = sortData.getPage() * sortData.getPageSize();
            
            while (index < sortData.getPageSize() && index + startIndex < total) {
                result.add(read(rs, new OrgAssociateResult(), true));
                index++;
                rs.next();
            }
        } catch (SQLException e) {
            throw new EJBException("Error retrieving org associates in MaintainOrganizationsBean.getOrgAssociates()", e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return total;
    }

    /**
     * Retrieves a list of org associate results for a person.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param personPk person to get org associate results for
     * @return the total number of organizations person is associated with
     */
    public List getPersonOrgAssociates(Integer personPk, SortData sortData) { 
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        //retrieve all the Organization Associates for a Person
        try {
            
            //get the org associate records
            String sql = SQL_SELECT_PERSON_EXT_ORG_ASSOCIATES;
            String sortColumn = getSortColumn(sortData);
            if (sortColumn != null)
                sql += " ORDER BY " + sortColumn;
            
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();
            
            //put the results into a list of OrgAssociateResult objects
            while (rs.next())
                result.add(read(rs, new OrgAssociateResult(), false));
            
        } catch (SQLException e) {
            throw new EJBException("Error retrieving org associates for a person in MaintainOrganizationsBean.getPersonOrgAssociates()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return result;
    }
    
    /**
     * Retrieves the data for an organization associate. This method will make use of getPersonDetail()
     *  and will access the database directly to get Organization Name and data from Ext_Org_Associates.
     *  See sequence diagram for more details
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     *
     * @return OrganizationAssociateData
     * @param orgPK - the primary key of the organization the associate is affiliated with
     * @param personPK - the primary key of the person who is associated with the organization
     */
    public OrganizationAssociateData getOrgAssociateDetail(Integer orgPK, Integer personPK) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrganizationAssociateData result = null;
        
        con = DBUtil.getConnection();
        
        //retrieve the one Organization Associate for an External Organization
        try {
            
            //retrieve the org associate main data
            ps = con.prepareStatement(SQL_SELECT_SINGLE_EXT_ORG_ASSOCIATE);
            ps.setInt(1, orgPK.intValue());
            ps.setInt(2, personPK.intValue());
            rs = ps.executeQuery();
            if (!rs.next())
                throw new EJBException("Org Associate with Org_Pk="+orgPK+" and Person_Pk="+personPK+" not found ");
            
            result = read(rs, new OrganizationAssociateData());
            
            //retrieve the person data for the org associate
            PersonData personData = personsBean.getPersonDetail(personPK, null);
            result.setPersonData(personData);
            
            //retrieve the org location association
            Integer orgLocationPk = new Integer(rs.getInt("org_locations_pk"));
            LocationData orgAssociateLocation = getOrgAssociateLocation(orgPK, personPK);
            result.setLocationData(orgAssociateLocation);
            result.setLocationPk(orgLocationPk);
            
            //retrieve the latest comments
            CommentData commentData = getComment(orgPK, personPK);
            result.setLatestCommentText((commentData == null) ? null : commentData.getComment());
            
            
        } catch (SQLException e) {
            throw new EJBException("Error retrieving org associates detail in MaintainOrganizationsBean.getOrgAssociateDetail()",e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * Retrieves the most recent comment associated with the Org Associate person.
     *
     * @param orgPk The External Organization Primary Key
     * @param personPk The Person Primary Key
     *
     * @return The CommentData object.
     */
    protected CommentData getComment(Integer orgPk, Integer personPk) {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CommentData result = null;
        
        con = DBUtil.getConnection();
        
        try {
            ps = con.prepareStatement(SQL_SELECT_LATEST_COMMENT);
            ps.setInt(1, orgPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
                result = read(rs, new CommentData());
        } catch (SQLException e) {
            throw new EJBException("Error retrieving org associates comment in MaintainOrganizationsBean.getComment()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * Retrieves the comment history for an Organization Associate person.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param orgPk External Organization Primary Key.
     * @param personPk Person Primary Key.
     * @return the Collection of CommentData objects
     */
    public List getCommentHistory(Integer orgPk, Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LinkedList result = new LinkedList();
        
        con = DBUtil.getConnection();
        
        try {
            ps = con.prepareStatement(SQL_SELECT_COMMENT_HISTORY);
            ps.setInt(1, orgPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            while (rs.next())
                result.add(read(rs, new CommentData()));
        } catch (SQLException e) {
            throw new EJBException("Error retrieving org associates comment history in MaintainOrganizationsBean.getCommentHistory()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return result;
    }

    
    /** 
     * Adds an organization associate comment
     *
     * @param orgPk, personPk Primary key of the organization associate to add a comment for
     * @param comment The comment string to add
     * @userPk The user making the add
     */ 
    protected void addComment(Integer orgPk, Integer personPk, String comment, Integer userPk) {

        Connection con = null;
        PreparedStatement ps = null;
        
        //insert an organization associate comment
        try {

            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_COMMENT);
            ps.setInt(1, orgPk.intValue());
            ps.setInt(2, personPk.intValue());
            ps.setString(3, comment);
            ps.setInt(4, userPk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException("Error adding org associates comment in MaintainOrganizationsBean.addComment()", e);
        } finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Checks to see if there is an existing org associate record for this 
     * orgPk and personPk in the database.
     *
     * @param orgPk Primary key of the organization to search for org associate
     * @param personPk Primary key of the person to search for org associate
     * @return TRUE if org associate record found, and FALSE if no existing org associate rec is found.
     */
    protected boolean isExistingOrgAssociate(Integer orgPk, Integer personPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        //checks for existing org associate in the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_EXISTING_EXT_ORG_ASSOCIATE);
            ps.setInt(1, orgPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            if ( rs.next()) {
                do {
                    if (rs.getInt("Existing") == 1) exists = true;
                } while (rs.next());
            } else
                throw new EJBException("Error with SQL_SELECT_EXISTING_EXT_ORG_ASSOCIATE query - failed to execute correctly in MaintainOrganizationsBean.isExistingOrgAssociate()");
        } catch (SQLException e) {
            throw new EJBException("Error retrieving existing org associate check in MaintainOrganizationsBean.isExistingOrgAssociate()", e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
        return exists;
    }

    /**
     * Gets a SQL column name from the sortData provided
     */
    protected String getSortColumn(SortData sortData) {
        
        String sortColumn = null;
        
        switch (sortData.getSortField()) {
            case OrgAssociateResult.SORT_FIELD_NAME:
                sortColumn = "name";
                break;
            case OrgAssociateResult.SORT_FIELD_TITLE:
                sortColumn = "title";
                break;
            case OrgAssociateResult.SORT_FIELD_LOCATION:
                sortColumn = "location_nm";
                break;
            case OrgAssociateResult.SORT_FIELD_PHONE:
                sortColumn = "full_phone";
                break;
            case OrgAssociateResult.SORT_FIELD_EMAIL:
                sortColumn = "person_email_addr";
                break;
            case OrgAssociateResult.SORT_FIELD_NONE:
                sortColumn = null;
                break;
            default:
                throw new EJBException("Invalid sort field: "+sortData.getSortField());
        }
        
        if (sortColumn != null && sortData.getDirection() == SortData.DIRECTION_DESCENDING)
            sortColumn = sortColumn + " DESC";
        
        return sortColumn;
    }
    
    /**
     * Reads a ResultSet into an OrgAssociate Result object
     *
     * @param rs The result set to read from
     * @param result the OrgAssociateResult to write to
     * @return the OrgAssociateResult object passed in.
     */
    protected OrgAssociateResult read(ResultSet rs, OrgAssociateResult result, boolean personInfo) throws SQLException {
        
        result.setOrgPk(new Integer(rs.getInt("org_pk")));
        result.setPersonPk(new Integer(rs.getInt("person_pk")));
        result.setName(rs.getString("name"));
        result.setOrgTitle(rs.getString("title"));
        result.setLocationName(rs.getString("location_nm"));
        if (personInfo) {
            result.setPhoneNumber(rs.getString("full_phone"));
            result.setEmailAddress(rs.getString("person_email_addr"));
        }    
        
        return result;
    }
    
    /**
     * Reads a ResultSet into an OrganizationAssociateData object
     *
     * @param rs The result set to read from
     * @param data the OrganizationAssociateData to write to
     * @return the OrganizationAssociateData object passed in.
     */
    protected OrganizationAssociateData read(ResultSet rs, OrganizationAssociateData data) throws SQLException {
        
        data.setOrgName(rs.getString("org_nm"));
        data.setOrgPositionTitle(new Integer(rs.getInt("org_pos_title")));
        data.setLocationPk(new Integer(rs.getInt("org_locations_pk")));
        data.setRecordData(readMasterPerson(rs, new RecordData(), true));
        return data;
    }
    
    /**
     * Reads a ResultSet into a CommentData object
     *
     * @param rs The result set to read from
     * @param comment the CommentData to write to
     * @return the CommentData object passed in.
     */
    protected CommentData read(ResultSet rs, CommentData comment) throws SQLException {
        
        comment.setComment(rs.getString("comment_txt"));
        comment.setCommentDt(rs.getTimestamp("comment_dt"));
        
        RecordData recordData = new RecordData();
        recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));        
        comment.setRecordData(recordData);
        
        return comment;
    }
    
    /** 
     * Reads record data from the given result set into the given record data object.
     */
    protected RecordData read(ResultSet rs, RecordData recordData, boolean readMods) throws SQLException {
        
        recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
        recordData.setCreatedDate(rs.getTimestamp("created_dt"));
        if (readMods) {
            recordData.setModifiedBy(new Integer(rs.getInt("lst_mod_user_pk")));
            recordData.setModifiedDate(rs.getTimestamp("lst_mod_dt"));
        }
        
        return recordData;
    }

    /** 
     * Reads record data from the given result set into the given record data object.
     * Two new columns has been added to the Person table, person_mst_lst_mod_user_pk and person_mst_lst_mod_dt
     * Organization Associate Detail and Organization Associate Detail Edit has to display those two data from new column
     */
    protected RecordData readMasterPerson(ResultSet rs, RecordData recordData, boolean readMods) throws SQLException {
        
        recordData.setCreatedBy(new Integer(rs.getInt("created_user_pk")));
        recordData.setCreatedDate(rs.getTimestamp("created_dt"));
        if (readMods) {
            recordData.setModifiedBy(new Integer(rs.getInt("person_mst_lst_mod_user_pk")));
            recordData.setModifiedDate(rs.getTimestamp("person_mst_lst_mod_dt"));
        }
        
        return recordData;
    }    
    
}
