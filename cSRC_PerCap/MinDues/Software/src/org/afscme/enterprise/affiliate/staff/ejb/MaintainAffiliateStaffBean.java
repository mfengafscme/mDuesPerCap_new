package org.afscme.enterprise.affiliate.staff.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Java Imports
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

// AFSCME Enterprise Imports
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.staff.StaffData;
import org.afscme.enterprise.affiliate.staff.StaffResult;
import org.afscme.enterprise.affiliate.*;
import org.afscme.enterprise.affiliate.staff.StaffCriteria;
import org.afscme.enterprise.organization.ejb.*;
import org.afscme.enterprise.person.NewPerson;
import org.afscme.enterprise.person.PersonData;
import org.afscme.enterprise.person.PersonResult;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.common.CommentData;
import org.afscme.enterprise.common.SortData;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.codes.Codes.EmailType;
import org.afscme.enterprise.codes.Codes.OrgPhoneType;
import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.users.AffiliateData;
import org.afscme.enterprise.util.DelimitedStringBuffer;

// Other Imports 
import org.apache.log4j.Logger;

/**
 * Encapsulates access to affiliate staff data.
 *
 * @ejb:bean name="MaintainAffiliateStaff" display-name="MaintainAffiliateStaff"
 * jndi-name="MaintainAffiliateStaff"
 * type="Stateless" view-type="local"
 */
public class MaintainAffiliateStaffBean extends SessionBase {

    static Logger log = Logger.getLogger(MaintainAffiliateStaffBean.class);

    /** INSERTS a new affiliate staff record */
    private static final String SQL_INSERT_AFFILIATE_STAFF = 
        " INSERT INTO Aff_Staff " +
        " (aff_pk, person_pk, staff_poc_for, aff_staff_title, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk) " +
        " VALUES " +
        " (?     , ?        , ?            , ?              , ?               , getdate() , ?              , getdate() , ?              ) ";

    /** UPDATES a new affiliate staff record */
    private static final String SQL_UPDATE_AFFILIATE_STAFF = 
        " UPDATE Aff_Staff SET " +
        " staff_poc_for=?, aff_staff_title=?, lst_mod_dt=getdate(), lst_mod_user_pk=? " +
        " WHERE aff_pk=? AND person_pk=? ";
    
    /** FROM clause for selecting affiliate staff */
    private static final String SQL_FROM_AFFILIATE_STAFF = 
        " FROM Person p " +
        " INNER JOIN Aff_Staff s on p.person_pk = s.person_pk " +
        " LEFT OUTER JOIN Person_Email email on email.person_pk = p.person_pk and email.email_type = " + EmailType.PRIMARY +
        " LEFT OUTER JOIN Org_Locations l on s.org_locations_pk = l.org_locations_pk " +
        " LEFT OUTER JOIN Org_Phone phone on phone.org_locations_pk = l.org_locations_pk and phone.org_phone_type = " + OrgPhoneType.LOC_PHONE_OFFICE +
        " LEFT OUTER JOIN Common_Codes titles ON titles.com_cd_pk = s.aff_staff_title " +
        " LEFT OUTER JOIN Common_Codes pocs ON pocs.com_cd_pk = s.staff_poc_for ";
    
    /** SELECTS affiliate staff */
    private static final String SQL_SELECT_AFFILIATE_STAFF = 
        " SELECT p.last_nm + ', ' + p.first_nm as full_name, phone.area_code + '-' + phone.phone_no as full_phone, email.person_email_addr, titles.com_cd_desc as title, pocs.com_cd_desc as poc_for, location_nm, p.person_pk " +
        SQL_FROM_AFFILIATE_STAFF;

    /** 
     * Selects staff information for a person
     */
    private static final String SQL_SELECT_PERSON_STAFF = 
        " SELECT " +
        " titles.com_cd_desc as title, pocs.com_cd_desc as poc_for, l.location_nm, " +
        " a.aff_pk, a.aff_abbreviated_nm, a.aff_pk, a.aff_code, " + 
        " a.aff_councilRetiree_chap, a.aff_localSubChapter, " + 
        " a.aff_stateNat_type, a.aff_subUnit, a.aff_type " + 
        " FROM Aff_Staff s " + 
        " LEFT OUTER JOIN Org_Locations l on s.org_locations_pk = l.org_locations_pk " +
        " INNER JOIN Aff_Organizations a ON s.aff_pk = a.aff_pk " +
        " LEFT OUTER JOIN Common_Codes titles ON titles.com_cd_pk = s.aff_staff_title " +
        " LEFT OUTER JOIN Common_Codes pocs ON pocs.com_cd_pk = s.staff_poc_for " + 
        " WHERE s.person_pk=? ";
    
    /** SELECTS affiliate staff count for an affiliate */
    private static final String SQL_SELECT_AFFILIATE_STAFF_COUNT = 
        " SELECT COUNT(*) FROM Aff_Staff ";
    
    /** FROM clause for exisiting staff info about a person */
    private static final String SQL_FROM_EXISTING_PERSON_STAFF = 
        " FROM Person p " + 
        " LEFT OUTER JOIN Person_Demographics d ON p.person_pk = d.person_pk " + 
        " LEFT OUTER JOIN Person_SMA sma on sma.person_pk = p.person_pk AND sma.current_fg=1 " + 
        " LEFT OUTER JOIN Person_Address pa on sma.address_pk = pa.address_pk " + 
        " LEFT OUTER JOIN Common_Codes suffixes on suffixes.com_cd_pk = p.suffix_nm " + 
        " LEFT OUTER JOIN Common_Codes prefixes on prefixes.com_cd_pk = p.prefix_nm " + 
        " LEFT OUTER JOIN Aff_Staff s ON p.person_pk = s.person_pk " + 
        " LEFT OUTER JOIN Aff_Organizations a ON s.aff_pk = a.aff_pk ";
    
    /** SELECTS staff information about persons */
    private static final String SQL_SELECT_EXISTING_PERSON_STAFF = 
        " SELECT " +
        " p.person_pk, p.first_nm, p.middle_nm, p.last_nm, " +
   		"(COALESCE(p.last_nm, '') +  " +
		"COALESCE(' '+(SELECT cc.com_cd_desc  " +
			"		    FROM Common_Codes cc  " +
			"		   WHERE p.suffix_nm = cc.com_cd_pk), '') +   " +
		" COALESCE(', ' +p.first_nm, ', ') +   " +
		" COALESCE(' ' +p.middle_nm,' ')) as full_name, " +
        " prefixes.com_cd_desc as prefix, suffixes.com_cd_desc as suffix, p.ssn, d.dob, " +
        " suffixes.com_cd_desc as suffix, prefixes.com_cd_desc as prefix, " + 
        " pa.addr1, pa.addr2, pa.city, pa.state, pa.zipcode, pa.zip_plus, " + 
		"(COALESCE(pa.addr1 + pa.addr2, pa.addr1, pa.addr2)) AS addr, " +
        " a.aff_pk, a.aff_code, " + 
        " a.aff_councilRetiree_chap, a.aff_localSubChapter, " + 
        " a.aff_stateNat_type, a.aff_subUnit, a.aff_type " + 
        SQL_FROM_EXISTING_PERSON_STAFF;

    /** SELECTS staff information about persons */
    private static final String SQL_SELECT_EXISTING_PERSON_STAFF_COUNT = 
        " SELECT COUNT(*) " +
        SQL_FROM_EXISTING_PERSON_STAFF;
    
    /** SELECTS single affiliate staff 
    private static final String SQL_SELECT_SINGLE_AFFILIATE_STAFF = 
        " SELECT aff_staff_title, staff_poc_for, org_locations_pk, created_dt, created_user_pk, lst_mod_dt, lst_mod_user_pk FROM Aff_Staff " +
        " WHERE aff_pk = ? AND person_pk = ? ";
    */
    /************************** BEGINING OF NEW CODE ************************/
    /** SELECT LAST MODIFY INFORMATION FROM THE PERSON TABLE **/
    private static final String SQL_SELECT_SINGLE_AFFILIATE_STAFF = 
        " SELECT a.aff_staff_title, a.staff_poc_for, a.org_locations_pk, a.created_dt, a.created_user_pk, p.person_mst_lst_mod_user_pk, p.person_mst_lst_mod_dt " +
        " FROM Aff_Staff a " +
        " INNER JOIN Person p    ON  p.person_pk = a.person_pk " +
        " WHERE a.aff_pk = ? AND a.person_pk = ? ";
    /** END OF NEW CODE ***************************/
    
    /** DELETES an affiliate staff record */
    private static final String SQL_DELETE_AFFILIATE_STAFF = 
        " DELETE FROM Aff_Staff WHERE aff_pk=? AND person_pk=? ";
        
    /** INSERTS a 'locals serviced' record */
    private static final String SQL_INSERT_LOCAL_SERVICED  = 
        " INSERT INTO Aff_Locals_Serviced_By_Staff " +
        " (aff_pk, person_pk, serviced_aff_pk) " +
        " VALUES " +
        " (?        , ?     , ?              ) ";
    
    /** DELETES 'locals serviced' records */
    private static final String SQL_DELETE_LOCALS_SERVICED  = 
        " DELETE FROM Aff_Locals_Serviced_By_Staff " +
        " WHERE aff_pk=? AND person_pk=? AND serviced_aff_pk IN ";

    /** SELECTS locals serviced for an affiliate staff person */
    public static final String SQL_SELECT_LOCALS_SERVICED = 
        " SELECT a.aff_abbreviated_nm, a.aff_pk, a.aff_code, " + 
        "       a.aff_councilRetiree_chap, a.aff_localSubChapter, " + 
        "       a.aff_stateNat_type, a.aff_subUnit, a.aff_type " + 
        " FROM   Aff_Organizations a " + 
        " INNER JOIN  Aff_Locals_Serviced_By_Staff s " + 
        " ON a.aff_pk = s.serviced_aff_pk " + 
        " WHERE s.aff_pk=? AND s.person_pk=? ";
    
    /** SELECTS pk's of locals serviced for an affiliate staff person */
    public static final String SQL_SELECT_LOCALS_SERVICED_PKS = 
        " SELECT serviced_aff_pk " + 
        " FROM Aff_Locals_Serviced_By_Staff " + 
        " WHERE aff_pk=? AND person_pk=? ";

    /** SELECTS the latest comment for an affiliate staff */
    public static final String SQL_SELECT_LATEST_COMMENT = 
        " SELECT TOP 1 comment_txt, comment_dt, created_user_pk FROM Aff_Staff_Comments WHERE aff_pk = ? AND person_pk = ? ORDER BY comment_dt DESC";
    
    /** SELECTS the latest comment for an affiliate staff */
    public static final String SQL_SELECT_COMMENT_HISTORY = 
        " SELECT comment_txt, comment_dt, created_user_pk FROM Aff_Staff_Comments WHERE aff_pk = ? AND person_pk = ? ORDER BY comment_dt DESC";
    
    /** INSERTS a comment for an affiliate staff*/
    private static final String SQL_INSERT_COMMENT  = 
        " INSERT INTO Aff_Staff_Comments " +
        " (aff_pk, person_pk, comment_dt, comment_txt, created_user_pk) " +
        " VALUES " +
        " (?      , ?       , getdate() , ?          , ?             ) ";
    
    /** SETS an affiliate staff location */
    private static final String SQL_SET_LOCATION  = 
        " UPDATE Aff_Staff SET org_locations_pk=? WHERE aff_pk = ? AND person_pk = ? ";
    
    protected MaintainAffiliates m_maintainAffiliates;
    protected MaintainPersons m_maintainPersons;
    protected MaintainOrgLocations m_maintainOrgLocations;
    
// BUSINESS METHODS
    
    /** Gets references to the dependent EJBs */
    public void ejbCreate() {
        try {
            m_maintainAffiliates = JNDIUtil.getMaintainAffiliatesHome().create();
            m_maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
            m_maintainOrgLocations = JNDIUtil.getMaintainOrgLocationsHome().create();
        } catch (CreateException e) {
            throw new EJBException(e);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    /** Removes references to the dependent EJBs */
    public void ejbRemove() {
		try {
			if (m_maintainAffiliates != null)
				m_maintainAffiliates.remove();
			if (m_maintainPersons != null)
				m_maintainPersons.remove();
			if (m_maintainOrgLocations != null)
				m_maintainOrgLocations .remove();
		} catch (RemoveException e) {
				throw new EJBException(e);
		}
	}
    
    /**
     * Adds a new Affiliate Staff to an affiliate after the Person Verify check has been
     * made. The overall process contains a sub process of checking for and determining
     * if the person to be added as an affiliate staff is already in the system, just as
     * in adding a person. Based on checks performed, human 
     * intervention may be required, just as in the add Person process. 
     * 
     * However, there is data unique to the association of a Person and Affiliate in the
     *  role of an affiliate staff, so different information must be collected during the
     *  Add Staff process - whether for a new person, or an existing person who is not already
     *  a staff member of that affiliate. This data for the add 
     * staff functionality is represented by a NewStaffData object 
     * 
     * A duplicate SSN check must be perfomed as part of the processing in case the user
     * has changed the SSN (when not selecting an existing person)
     * 
     * See Associated Sequence Diagram(s)
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Primary key of the person who is the affiliate staff
     * @param staffData Affiliate Staff information to add (only staffTitle and PocForPk are used)
     * @param comment Comments entered when adding the affiliate staff
     * @param userPk Person pk of the user that is performing the action
     * @return primary key of the person added.
     */
    public Integer addAffiliateStaff(Integer affPk, NewPerson newPerson, StaffData newStaffData, String comment,Integer userPk) {
        Integer personPk = m_maintainPersons.addPerson(userPk, newPerson);
        addAffiliateStaff(affPk, personPk, newStaffData, comment, userPk);
        return personPk;
    }
        
    /**
     * Adds a new Affiliate Staff to an affiliate
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Primary key of the person who is the affiliate staff
     * @param staffData Affiliate Staff information to add (only staffTitle and PocForPk are used)
     * @param userPk Person pk of the user that is performing the action
     */
    public void addAffiliateStaff(Integer affPk, Integer personPk, StaffData newStaffData, String comment, Integer userPk) {
        
        Connection con = null;
        PreparedStatement ps = null;
        Integer retval;
        
        Integer locationPk = m_maintainOrgLocations.getOrgPrimaryLocationPK(affPk);
        
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_AFFILIATE_STAFF);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            ps.setInt(3, newStaffData.getPocForPk().intValue());
            DBUtil.setNullableInt(ps, 4, newStaffData.getStaffTitlePk());
            DBUtil.setNullableInt(ps, 5, locationPk);
            ps.setInt(6, userPk.intValue());
            ps.setInt(7, userPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        //add the comment (if it's not empty)
        if (!TextUtil.isEmpty(comment))
            addComment(affPk, personPk, comment, userPk);
        
    }
    
    /**
     * Associate a Local to an Affiliate Staff for this affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Person Primary Key
     * @param localPk
     */
    public void addLocalServiced(Integer affPk, Integer personPk, Integer servicedAffPk) { 
        List pk = new LinkedList();
        pk.add(affPk);
        pk.add(personPk);
        DBUtil.addAssociation(pk, servicedAffPk, SQL_SELECT_LOCALS_SERVICED_PKS, "serviced_aff_pk", SQL_INSERT_LOCAL_SERVICED);
    }
    
    /**
     * Gets the locals serviced by an affiliate staff
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Person Primary Key
     * @return List of AffilaiteResult objects
     */
    public List getLocalsServiced(Integer affPk, Integer personPk) { 
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_LOCALS_SERVICED);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                AffiliateResult affResult = new AffiliateResult();
                affResult.setAffAbreviatedNm(rs.getString("aff_abbreviated_nm"));
                affResult.setAffPk(new Integer(rs.getInt("aff_pk")));
                affResult.setAffiliateId(read(rs, new AffiliateIdentifier()));
                result.add(affResult);
            }
            
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, rs);
        }
        
        return result;
    }
    
    /**
     * Retrieves a list of staff results for a person.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     *
     * @param personPk person to get staff results for
     * @return the total number that matched the search parameters
     */
    public List getPersonStaff(Integer personPk, SortData sortData) { 
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List result = new LinkedList();
        
        try {
            
            //make sql
            String sql = SQL_SELECT_PERSON_STAFF;
            String sortColumn = getSortColumn(sortData);
            if (sortColumn != null)
                sql += " ORDER BY " + sortColumn;
            
            //execute sql
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, personPk.intValue());
            rs = ps.executeQuery();

            //get results
            while (rs.next())
                result.add(read(rs, new StaffResult(), new AffiliateIdentifier()));
            
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return result;
    }
    
    
    /**
     * Retrieves the StaffResult for a specific affiliate staff person. 
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Person Primary Key
     * @return the StaffResult object
     */
    public StaffData getAffiliateStaff(Integer affPk, Integer personPk) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StaffData result = null;
        
        con = DBUtil.getConnection();
        
        try {
            ps = con.prepareStatement(SQL_SELECT_SINGLE_AFFILIATE_STAFF);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            if (!rs.next())
                throw new EJBException("Affiliate staff with affpk="+affPk+" and personPk="+personPk+" not found ");
            result = read(rs, new StaffData());
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return result;
    }


    /**
     * Retrieves a list of staff data for the affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk
     * @param sortData
     * @param result List to place the results in
     * @return the total number that matched the search parameters
     */
    public int getAffiliateStaff(Integer affPk, SortData sortData, List result) { 
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        int total;
        
        con = DBUtil.getConnection();
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        
        builder.addCriterion("aff_pk", affPk);
        
        try {
            //get the count
            ps = builder.getPreparedStatement(SQL_SELECT_AFFILIATE_STAFF_COUNT, con);
            rs = ps.executeQuery();
            rs.next();
            total = rs.getInt(1);
            rs.close();
            ps.close();
            
            //get the affiliate staff
            String sortColumn = getSortColumn(sortData);
            if (sortColumn != null) {
                builder.addOrderBy(sortColumn);
            } else {
                builder.addOrderBy("last_nm");
                builder.addOrderBy("first_nm");
            }
            ps = builder.getPreparedStatement(SQL_SELECT_AFFILIATE_STAFF, con);
            rs = ps.executeQuery();

            int startIndex = sortData.getPage() * sortData.getPageSize() + 1;
            if (startIndex == 1)
                rs.next();
            else
                rs.absolute(startIndex);
            
            while (count < sortData.getPageSize() && (startIndex + count) <= total) {
                result.add(read(rs, new StaffResult()));
                count++;
                rs.next();
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return total;
    }
     
    
    /**
     * Retrieves the most recent comment associated with the Affiliate Staff person.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk The Affiliate Primary Key
     * @param personPk The Person Primary Key
     * 
     * @return The CommentData object.
     */
    public CommentData getComment(Integer affPk, Integer personPk) { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CommentData result = null;
        
        con = DBUtil.getConnection();
        
        try {
            ps = con.prepareStatement(SQL_SELECT_LATEST_COMMENT);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            if (rs.next())
                result = read(rs, new CommentData());
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return result;
    }
    
    /**
     * Retrieves the comment history for an Affiliate Staff person.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key.
     * @param personPk Person Primary Key.
     * @return the Collection of CommentData objects
     */
    public List getCommentHistory(Integer affPk, Integer personPk) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LinkedList result = new LinkedList();
        
        con = DBUtil.getConnection();
        
        try {
            ps = con.prepareStatement(SQL_SELECT_COMMENT_HISTORY);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            rs = ps.executeQuery();
            while (rs.next())
                result.add(read(rs, new CommentData()));
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);
        }
            
        return result;
    }
    
    
    /**
     * Removes a person as an affiliate staff for a specific affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Person Primary Key
     * @return 'true' if the Staff person was removed, and 'false' otherwise.
     */
    public boolean removeAffiliateStaff(Integer affPk, Integer personPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        boolean result;
        
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_DELETE_AFFILIATE_STAFF);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            result = (ps.executeUpdate() != 0);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        return result;
    }
    
    /**
     * Disassociates a Local from an Affiliate Staff for this affiliate.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param personPk Person Primary Key
     * @param servicedLocalAffPk Primary Key of the Local being removed
     */
    public void removeLocalServiced(Integer affPk, Integer personPk, Integer servicedAffPk)  
    {
        LinkedList pk = new LinkedList();
        pk.add(affPk);
        pk.add(personPk);
        DBUtil.removeAssociation(pk, servicedAffPk, SQL_DELETE_LOCALS_SERVICED);
    }
    
    /**
     * Allows the association of a Staff person to an Affiliate Location.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param locationPk Location Primary Key
     * @param affPk Affiliate Primary Key
     * @param personPk Staff person Primary Key
     */
    public void setAffiliateStaffLocation(Integer affPk, Integer personPk, Integer locationPk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SET_LOCATION);
            ps.setInt(1, locationPk.intValue());
            ps.setInt(2, affPk.intValue());
            ps.setInt(3, personPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    /**
     * Allows the Affiliate Staff data to be updated. 
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     * 
     * @param affPk Affiliate Primary Key
     * @param staffData Staff Data
     * @param userPk Primary key of the user makig the update
     */
    public void updateAffiliateStaff(Integer affPk, PersonData personData, StaffData staffData, String comment, Integer userPk)
    { 
        Connection con = null;
        PreparedStatement ps = null;
        Integer retval;

        m_maintainPersons.updatePersonDetail(userPk, personData);
        
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_AFFILIATE_STAFF);
            ps.setInt(1, staffData.getPocForPk().intValue());
            DBUtil.setNullableInt(ps, 2, staffData.getStaffTitlePk());
            ps.setInt(3, userPk.intValue());
            ps.setInt(4, affPk.intValue());
            ps.setInt(5, personData.getPersonPk().intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
        
        //add the comment (if it's not empty)
        if (!TextUtil.isEmpty(comment))
            addComment(affPk, personData.getPersonPk(), comment, userPk);
    }
 
    /**
     * Searches for existing persons with the given criteria
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getExistingStaff(StaffCriteria staffCriteria, List results)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        ResultSet rs = null;
        int total;
        int count = 0;
        
        builder.addCriterion("first_nm", staffCriteria.getFirstName());
        builder.addCriterion("suffix_nm", staffCriteria.getSuffixName());
        builder.addCriterion("last_nm", staffCriteria.getLastName());
        builder.addCriterion("dob", staffCriteria.getDob());
        builder.addCriterion("ssn", staffCriteria.getSsn());

        SortData sortData = staffCriteria.getSortData();
        
        try {
            con = DBUtil.getConnection();

            //get the count
            ps = builder.getPreparedStatement(SQL_SELECT_EXISTING_PERSON_STAFF_COUNT, con);
            rs = ps.executeQuery();
            rs.next();
            total = rs.getInt(1);
            rs.close();
            ps.close();
            if(sortData.getSortField() == StaffResult.SORT_FIELD_NONE)
                sortData.setSortField(StaffResult.SORT_FIELD_NAME);
            //get the results
            builder.addOrderBy(getSortColumn(sortData));
            ps = builder.getPreparedStatement(SQL_SELECT_EXISTING_PERSON_STAFF, con);
            rs = ps.executeQuery();
            rs.absolute((sortData.getPage() * sortData.getPageSize()) + 1);
            while (count < sortData.getPageSize() && count < total) {
                results.add(read(rs, new PersonResult()));
                count++;
                rs.next();
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            DBUtil.cleanup(con, ps, rs);  
        }
        return total;
    }
    
    /** 
     * Adds an affiliate staff comment
     *
     * @param affPk, personPk Primary key of the affiliate staff to add a comment for
     * @param comment The comment string to add
     * @userPk The user making the add
     */ 
    protected void addComment(Integer affPk, Integer personPk, String comment, Integer userPk)
    {
        Connection con = null;
        PreparedStatement ps = null;
        
		try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_COMMENT);
            ps.setInt(1, affPk.intValue());
            ps.setInt(2, personPk.intValue());
            ps.setString(3, comment);
            ps.setInt(4, userPk.intValue());
            ps.executeUpdate();
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
            DBUtil.cleanup(con, ps, null);
        }
    }
    
    
    /** 
     * Gets a SQL column name from the sortData provided
     */ 
    protected String getSortColumn(SortData sortData)
    {
        String sortColumn = null;
        
        switch (sortData.getSortField()) {
            case StaffResult.SORT_FIELD_TITLE:
                sortColumn = "title";
                break;
            case StaffResult.SORT_FIELD_POC_FOR:
                sortColumn = "poc_for";
                break;
           case StaffResult.SORT_FIELD_NAME:
                sortColumn = "full_name";
                break;
            case StaffResult.SORT_FIELD_LOCATION:
                sortColumn = "location_nm";
                break;
            case StaffResult.SORT_FIELD_STATE:
                sortColumn = "aff_stateNat_type";
                break;
            case StaffResult.SORT_FIELD_LOCAL:
                sortColumn = "aff_localSubChapter";
                break;
            case StaffResult.SORT_FIELD_COUNCIL:
                sortColumn = "aff_councilRetiree_chap";
                break;
            case StaffResult.SORT_FIELD_SUBUNIT:
                sortColumn = "aff_SubUnit";
                break;
            case StaffResult.SORT_FIELD_TYPE:
                sortColumn = "aff_type";
                break;
            case StaffResult.SORT_FIELD_ZIP:
                sortColumn = "zipcode";
                break;
            case StaffResult.SORT_FIELD_PHONE:
                sortColumn = "full_phone";
                break;
            case StaffResult.SORT_FIELD_ADDRESS:
                sortColumn = "addr";
                break;
            case StaffResult.SORT_FIELD_ADDR_STATE:
                sortColumn = "state";
                break;
            case StaffResult.SORT_FIELD_SSN:
                sortColumn = "ssn";
                break;
            case StaffResult.SORT_FIELD_EMAIL:
                sortColumn = "person_email_addr";
                break;
            case StaffResult.SORT_FIELD_CITY:
                sortColumn = "city";
                break;
            case StaffResult.SORT_FIELD_NONE:
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
     * Reads a ResultSet into a StaffResult object
     * 
     * @param rs The result set to read from
     * @param result the StaffResult to write to
     * @return the StaffResult object passed in.
     */ 
    protected StaffResult read(ResultSet rs, StaffResult result) throws SQLException {
        result.setPhoneNumber(rs.getString("full_phone"));
        result.setEmail(rs.getString("person_email_addr"));
        result.setTitle(rs.getString("title"));
        result.setPocFor(rs.getString("poc_for"));
        result.setFullName(rs.getString("full_name"));
        result.setLocationName(rs.getString("location_nm"));
        result.setPersonPk(new Integer(rs.getInt("person_pk")));
        return result;
    }
    
    /** 
     * Reads a ResultSet into a StaffResult and AffiliateIdentifier objects
     * 
     * @param rs The result set to read from
     * @param staffResult the StaffResult to write to
     * @param affiliateIdentifier the AffiliateIdentifier to write to
     * @return the StaffResult object passed in.
     */ 
    protected StaffResult read(ResultSet rs, StaffResult staffResult, AffiliateIdentifier affId) throws SQLException {
        staffResult.setTitle(rs.getString("title"));
        staffResult.setPocFor(rs.getString("poc_for"));
        staffResult.setLocationName(rs.getString("location_nm"));
        staffResult.setAffPk(new Integer(rs.getInt("aff_pk")));
        staffResult.setAffiliateIdentifier(read(rs, affId));
        return staffResult;
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
     * Reads a ResultSet into a StaffData object
     * 
     * @param rs The result set to read from
     * @param data the StaffData to write to
     * @return the CommentData object passed in.
     */ 
    protected StaffData read(ResultSet rs, StaffData data) throws SQLException {

        data.setStaffTitlePk(new Integer(rs.getInt("aff_staff_title")));
        data.setPocForPk(new Integer(rs.getInt("staff_poc_for")));
        data.setLocationPk(new Integer(rs.getInt("org_locations_pk")));
        data.setRecordData(readMasterPerson(rs, new RecordData(), true));
        return data;
    }

    /** 
     * Reads a ResultSet into a AffiliateIdentifier object
     * 
     * @param rs The result set to read from
     * @param affId the AffiliateIdentifier to write to
     * @return the AffiliateIdentifier object passed in.
     */ 
    protected AffiliateIdentifier read(ResultSet rs, AffiliateIdentifier affId) throws SQLException {
        affId.setCode(new Character(rs.getString("aff_code").toCharArray()[0]));
        affId.setCouncil(rs.getString("aff_councilRetiree_chap"));
        affId.setLocal(rs.getString("aff_localSubChapter"));
        affId.setState(rs.getString("aff_stateNat_type"));
        affId.setSubUnit(rs.getString("aff_subUnit"));
        affId.setType(new Character(rs.getString("aff_type").toCharArray()[0]));
        
        return affId;
    }

    /**
     * Reads record data from the given result set into the given record data object.
     */
    protected RecordData read(ResultSet rs, RecordData recordData, boolean readMods) throws SQLException
    {
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
    
    /**
     * Reads a PersonResult from the given result set
     */
    protected PersonResult read(ResultSet rs, PersonResult personResult) throws SQLException
    {
        personResult.setCity(rs.getString("city"));
        personResult.setState(rs.getString("state"));
        personResult.setPersonAddrPostalCode(
            new DelimitedStringBuffer("-")
                .append(rs.getString("zipcode"))
                .append(rs.getString("zip_plus"))
                .toString());
        personResult.setPersonAddr(
            new DelimitedStringBuffer(" ")
                .append(rs.getString("addr1"))
                .append(rs.getString("addr2"))
                .toString());
        personResult.setPersonNm(TextUtil.formatName(
            rs.getString("prefix"),
            rs.getString("first_nm"),
            rs.getString("middle_nm"),
            rs.getString("last_nm"),
            rs.getString("suffix")));
        personResult.setPersonPk(new Integer(rs.getInt("person_pk")));
        personResult.setSsn(rs.getString("ssn"));
        int affPk = rs.getInt("aff_pk");
        if (affPk != 0) {
            personResult.setAffPk(new Integer(affPk));
            personResult.setAffType(new Character(rs.getString("aff_type").charAt(0)));
            personResult.setAffCode(new Character(rs.getString("aff_code").charAt(0)));
            personResult.setAffCouncilRetireeChap(rs.getString("aff_councilRetiree_chap"));
            personResult.setAffLocalSubChapter(rs.getString("aff_localSubChapter"));
            personResult.setAffStateNatType(rs.getString("aff_stateNat_type"));
            personResult.setAffSubUnit(rs.getString("aff_subUnit"));
            personResult.setAffAdminCouncil(null);//TODO
        }
        
        return personResult;
    }
    
}