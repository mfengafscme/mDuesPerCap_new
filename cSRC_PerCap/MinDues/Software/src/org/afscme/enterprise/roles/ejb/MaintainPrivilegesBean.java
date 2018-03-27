package org.afscme.enterprise.roles.ejb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Set;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.roles.ReportPrivilegeData;
import org.afscme.enterprise.roles.PrivilegeData;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.roles.RoleData;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeMap;
import javax.naming.NamingException;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.roles.*;
import org.afscme.enterprise.util.ValueSortedMap;



/**
 * Implements the business logic of maintain roles and their associated privileges.
 *
 * @ejb:bean name="MaintainPrivileges" display-name="MaintainPrivileges"
 *              jndi-name="MaintainPrivileges"
 *              type="Stateless" view-type="local"
 */
public class MaintainPrivilegesBean extends SessionBase
{
    /** Selects all Roles */
    private static final String SQL_SELECT_ROLES =
    "SELECT role_pk, role_name, role_description FROM Roles";
    
    /** Inserts a Role */
    private static final String SQL_INSERT_ROLE =
        "INSERT INTO Roles (role_name, role_description) VALUES (?, ?)";
    
    /** Updates a Role */
    private static final String SQL_UPDATE_ROLE =
        "UPDATE Roles SET role_name = ?, role_description = ? WHERE role_pk = ?";
    
    /** Deletes a Role */
    private static final String SQL_DELETE_ROLE =
        "DELETE FROM Roles WHERE role_pk = ?";
    
    /** Selects all the Fields, along with their parent id, if any */
    private static final String SQL_SELECT_FIELDS =
        " SELECT report_field_pk, field_entity_type, field_display_name, field_category_name, parent_field_pk, child_field_pk " +
        " FROM Report_Fields LEFT OUTER JOIN Report_Field_Aggregate " +
        " ON child_field_pk = report_field_pk";
    
    /** Selects all the Privileges */
    private static final String SQL_SELECT_PRIVILEGES =
        "SELECT privilege_key, privilege_name, privilege_verb, privilege_category, privilege_is_data_utility FROM Privileges ORDER BY privilege_name";
    
    /** Selects all the Reports */
    private static final String SQL_SELECT_REPORTS =
        "SELECT report_pk, report_nm, report_desc, report_category FROM Report WHERE report_custom_fg = 0 AND report_status = 1";
    
    /** Selects all Reports for a specific role */
    private static final String SQL_SELECT_ROLE_REPORTS =
        "SELECT role_pk, report_pk FROM Roles_Reports WHERE role_pk = ?";
    
    /** Selects all Fields for a specific role */
    private static final String SQL_SELECT_ROLE_FIELDS =
        "SELECT role_pk, report_field_pk FROM Roles_Report_Fields WHERE role_pk = ?";
    
    /** Selects all Privileges for a specific role */
    private static final String SQL_SELECT_ROLE_PRIVILEGES =
        "SELECT role_pk, privilege_key FROM Roles_Privileges WHERE role_pk = ?";
    
    /** Deletes all Reports for a specific role */
    private static final String SQL_DELETE_ROLE_REPORTS =
        "DELETE FROM Roles_Reports WHERE role_pk = ?";
    
    /** Deletes all Reports for a specific role */
    private static final String SQL_DELETE_ROLE_FIELDS =
        "DELETE FROM Roles_Report_Fields WHERE role_pk = ?";
    
    /** Deletes all Reports for a specific role */
    private static final String SQL_DELETE_ROLE_PRIVILEGES =
        "DELETE FROM Roles_Privileges WHERE role_pk = ?";
    
    /** Associates a Report with a role*/
    private static final String SQL_INSERT_ROLE_REPORT =
        "INSERT INTO Roles_Reports (role_pk, report_pk) VALUES (?, ?)";
    
    /** Associates a Field with a role*/
    private static final String SQL_INSERT_ROLE_FIELD =
        "INSERT INTO Roles_Report_Fields (role_pk, report_field_pk) VALUES (?, ?)";
    
    /** Associates a Privilege with a role*/
    private static final String SQL_INSERT_ROLE_PRIVILEGE =
        "INSERT INTO Roles_Privileges (role_pk, privilege_key) VALUES (?, ?)";
    
    /** Gets memeber privileges from the Affiliate table */
    private static final String SQL_SELECT_MEMBER_PRIVILEGES =
        "SELECT mbr_allow_edit_fg, mbr_allow_view_fg FROM Aff_Organizations WHERE aff_pk=?";
    
    /** Sets memeber privileges in the Affiliate table */
    private static final String SQL_UPDATE_MEMBER_PRIVILEGES =
        "UPDATE Aff_Organizations SET mbr_allow_edit_fg=?, mbr_allow_view_fg=? WHERE aff_pk=?";

    /**
     * Map of all the fields in the system, by primary key
     */
    protected Map m_fields;
    
    /**
     * Map of all the privileges in the system.
     */
    protected Map m_privileges;
    
    /**
     * Gets a map of all the roles in the system, by primary key.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Map getRoles()
    { 
        Map roles;
        
        //
        // Look for the roles in JNDI
        //
        
        try {
            roles = (Map)JNDIUtil.getObject("Roles");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
        
        if (roles != null)
            return roles;
        
        //
        // Roles weren't in JNDI, load them from the database
        //
        
        roles = new TreeMap();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        RoleData roleData = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ROLES);
            
            while (rs.next()) {
                roleData = new RoleData();
                roleData.setPk(new Integer(rs.getInt("role_pk")));
                roleData.setName(rs.getString("role_name"));
                roleData.setDescription(rs.getString("role_description"));
                roles.put(roleData.getPk(), roleData);
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        //
        // Put the Roles in JNDI
        //
        
        try {
            JNDIUtil.setObject("Roles", roles);
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        return roles;
    }
    
    /**
     * Gets a map of all the reports in the system, by primary key.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Map getReports()  
    { 
        Map reports;
        
        //
        // Look for the Reports in JNDI
        //
        
        try {
            reports = (Map)JNDIUtil.getObject("Reports");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
        
        if (reports != null)
            return reports;
        
        //
        // The Reports weren't in JNDI, load them from the database
        //
        
        reports = new TreeMap();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ReportPrivilegeData reportData = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_REPORTS);
            
            while (rs.next()) {
                reportData = new ReportPrivilegeData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCategory(rs.getString("report_category"));
                reports.put(reportData.getPk(), reportData);
            }
            
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        //
        // Put the reports in JNDI
        //
        
        try {
            JNDIUtil.setObject("Reports", reports);
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        return reports;
    }
    
    /**
     * Gets a map of all the fields in the system, by primary key.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     **/
    public Map getFields()  
    {
        return Collections.unmodifiableMap(m_fields);
    }
    
    /**
     * Gets a list of all the privileges in the system.
     *
     * @return A List of PrivilegeData objects
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPrivilegesList()  
    { 
        return new LinkedList(m_privileges.values());
    }
    
    /**
     * Gets a map of all the privileges in the system.
     *
     * @return A map of PrivilegeData objects by key
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Map getPrivilegesMap()  
    { 
        return Collections.unmodifiableMap(m_privileges);
    }
    
    /**
     * Gets the primary keys of all the reports associated with a role.
     *
     * @param rolePK Primary key of the role to get reports for.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getReports(Integer rolePK)  
    { 
        return DBUtil.getAssociation(rolePK, SQL_SELECT_ROLE_REPORTS, "report_pk");
    }
    
    /**
     * Gets the primary keys of all the fields associated with a role.
     *
     * @param rolePK Primary key of the role to get fields for.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getFields(Integer rolePK)  
    { 
        return DBUtil.getAssociation( rolePK, SQL_SELECT_ROLE_FIELDS, "report_field_pk");
    }
    
    /**
     * Gets the primary keys of all the privileges associated with a role.
     *
     * @param rolePK Primary key of the role to get privileges for.
     * @return A set of privilege primary key strings
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Set getPrivileges(Integer rolePK)  
    { 
        return DBUtil.getAssociation(rolePK, SQL_SELECT_ROLE_PRIVILEGES, "privilege_key");
    }
    

    /**
     * Gets the RoleData for a specific role.  If the role is not found, retuns null;
     * 
     * @param rolePK Primary key of the role to get.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public RoleData getRole(Integer rolePK) throws FinderException
    { 
        Map roles = getRoles();
        RoleData roleData = (RoleData)roles.get(rolePK);
        
        if (roleData == null) {
            throw new FinderException("Role with id '" + rolePK + "' not found.");
        }

        return roleData;
    }
    
    
    /**
     * Sets the reports for a role
     * 
     * @param rolePK Primary key of the role to set reports for.
     * @param reportPKs Set of primary keys of reports to associate with this role.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public void setReports(Integer rolePK, Set reportPKs)  
    { 
        DBUtil.setAssociation(rolePK, reportPKs, SQL_DELETE_ROLE_REPORTS, SQL_INSERT_ROLE_REPORT);
    }
    
    
    /**
     * Sets the fields for a role
     * 
     * @param rolePK Primary key of the role to set fields for.
     * @param fieldPKs Set of primary keys of fields to associate with this role.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void setFields(Integer rolePK, Set fieldPKs)  
    { 
        DBUtil.setAssociation(rolePK, fieldPKs, SQL_DELETE_ROLE_FIELDS, SQL_INSERT_ROLE_FIELD);
    }
    
    /**
     * Sets the privileges for a role
     * 
     * @param rolePK Primary key of the role to set privileges for.
     * @param privilegePKs Set of primary keys of privileges to associate with this role.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void setPrivileges(Integer rolePK, Set privilegePKs)  
    { 
        DBUtil.setAssociation(rolePK, privilegePKs, SQL_DELETE_ROLE_PRIVILEGES, SQL_INSERT_ROLE_PRIVILEGE);
    }
    
    /**
     * Adds a new role to the system.
     * 
     * @param roleData Data of the role to add
     * @return The pk in the roleData object is set, and the same object is retuned.
     *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Required"
     */
    public RoleData addRole(RoleData roleData)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        
        //validate the roleData
        checkRequiredField(roleData.getName(), "name", "Add Role");
        checkRequiredField(roleData.getDescription(), "description", "Add Role");

        //insert the RoleData object into the database
        try {
            
            con = DBUtil.getConnection();
        
            ps = con.prepareStatement(SQL_INSERT_ROLE);

            ps.setString(1, roleData.getName());
            ps.setString(2, roleData.getDescription());
            roleData.setPk(DBUtil.insertAndGetIdentity(con, ps));
           
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
            clearRoleCache();
        }
       
        return roleData;
    }
    
    /**
     * Stores the values of the role to the database.
     * 
     * @param roleData Data to update the role with
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updateRole    (RoleData roleData)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        
        //validate the roleData
        checkRequiredField(roleData.getName(), "name", "Update Role");
        checkRequiredField(roleData.getDescription(), "description", "Update Role");

        //insert the RoleData object into the database
        try {
            con = DBUtil.getConnection();
        
            ps = con.prepareStatement(SQL_UPDATE_ROLE);

            ps.setString(1, roleData.getName());
            ps.setString(2, roleData.getDescription());
            ps.setInt(3, roleData.getPk().intValue());
            ps.executeUpdate();
           
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
            clearRoleCache();
        }
    }
    
    /**
     * Deletes a role from the system.
     * 
     * @param rolePK primary key of the role to delete
     * 
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void deleteRole    (Integer rolePK)  
    { 
        Connection con = null;
        PreparedStatement ps = null;

        //insert the RoleData object into the database
        try {
            con = DBUtil.getConnection();
        
            ps = con.prepareStatement(SQL_DELETE_ROLE);

            ps.setInt(1, rolePK.intValue());
            ps.executeUpdate();
           
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
            clearRoleCache();
        }

    }

    /**
     * Gets Affiliate member privileges.
     * 
	 * Return value indicates the level of privileges.  See <code>MemberPrivileges</code>
	 *
	 * @return one of the values from the MemberPrivileges interface.
     * @param affiliatePk primary key of the affiliate
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public int getMemberPrivileges(Integer affiliatePk)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		int result;

        //insert the RoleData object into the database
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_SELECT_MEMBER_PRIVILEGES);
            ps.setInt(1, affiliatePk.intValue());
            rs = ps.executeQuery();
			if (!rs.next())
				throw new EJBException("Affiliate: " + affiliatePk + " not found");
			
			if (rs.getInt("mbr_allow_edit_fg") != 0)
				result = MemberPrivileges.VIEW_AND_EDIT;
			else if (rs.getInt("mbr_allow_view_fg") != 0)
				result = MemberPrivileges.VIEW;
			else
				result = MemberPrivileges.NONE;
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, rs);
        }
		
		return result;

    }
    /**
     * Sets member privileges
     * 
     * @param affiliatePk affiliate to set the member privileges for
     * @param privileges value from the MemberPrivileges interface (NONE, VIEW, or VIEW_AND_EDIT)
	 *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void setMemberPrivileges(Integer affiliatePk, int privileges)  
    { 
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DBUtil.getConnection();
        
            ps = con.prepareStatement(SQL_UPDATE_MEMBER_PRIVILEGES);

            ps.setInt(1, privileges == MemberPrivileges.VIEW_AND_EDIT ? 1 : 0);
            ps.setInt(2, privileges != MemberPrivileges.NONE ? 1 : 0);
            ps.setInt(3, affiliatePk.intValue());
            ps.executeUpdate();
           
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, ps, null);
        }
    }

	/** Loads all non-changeable data into local member variables (Fields and Privileges) */
    public void ejbCreate() throws CreateException {

        loadFields();
        loadPrivileges();
    }

    /** Reads the Report_Fields from the database and stores them locally */
    protected void loadFields() {
        
        m_fields = new TreeMap();
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ReportField fieldData = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_FIELDS);

            while (rs.next()) {
                fieldData = new ReportField();
                fieldData.setPk(new Integer(rs.getInt("report_field_pk")));
                fieldData.setEntityType(rs.getString("field_entity_type").charAt(0));
                fieldData.setDisplayName(rs.getString("field_display_name"));
                fieldData.setCategoryName(rs.getString("field_category_name"));
                fieldData.setAccessible(true);
                int parentPk = rs.getInt("parent_field_pk");
                if (parentPk != 0)
                    fieldData.setParentPK(new Integer(parentPk));
                m_fields.put(fieldData.getPk(), fieldData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
            
        //put children under parents
        Iterator it = m_fields.keySet().iterator();
        while (it.hasNext()) {
            fieldData = (ReportField)m_fields.get(it.next());
            if (fieldData.getParentPK() != null) {
                ReportField parent = ((ReportField)m_fields.get(fieldData.getParentPK()));
                Set children = parent.getChildren();
                if (children == null) {
                    children = new TreeSet();
                    parent.setChildren(children);
                }
                children.add(fieldData);
                it.remove();
            }
        }
    }

    /** Reads the Privigles from the database and stores them locally */
    protected void loadPrivileges() {
        m_privileges = new TreeMap();
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PrivilegeData privilegeData = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_PRIVILEGES);

            while (rs.next()) {
                privilegeData = new PrivilegeData();
                privilegeData.setKey(rs.getString("privilege_key"));
                privilegeData.setName(rs.getString("privilege_name"));
                privilegeData.setVerb(rs.getString("privilege_verb").charAt(0));
                privilegeData.setCategory(rs.getString("privilege_category").charAt(0));
                privilegeData.setDataUtility(rs.getInt("privilege_is_data_utility") == 0 ? false : true);
                m_privileges.put(privilegeData.getKey(), privilegeData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        m_privileges = ValueSortedMap.create(m_privileges);
    }
    
    
    /**
    * Clear the roles from JNDI.  Next call to getRole.. will refresh it.
    */
    protected static void clearRoleCache() {
        try {
            JNDIUtil.unset("Roles");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    /**
     * Throws an EJBException if val is null or zero-length.
     * 
     * @param val the value to test
     * @param fieldName the name of the field (for formatting the error message)
     * @param operation the operation performing the check (for formatting the error message)
     */
    protected static void checkRequiredField(String val, String fieldName, String operation) throws EJBException
    {
        if (val == null || val.length() == 0)
            throw new EJBException("Required Field " + fieldName + " missing in " + operation);
    }
    
}
