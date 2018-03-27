package org.afscme.enterprise.codes.ejb;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.ValueSortedMap;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.DBUtil;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.afscme.enterprise.codes.CategoryData;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.CodeTypeData;
import org.afscme.enterprise.common.ejb.SessionBase;
import javax.naming.NamingException;





/**
 * Implements data access and business logic for common codes.
 *
 * @ejb:bean name="MaintainCodes" display-name="MaintainCodes"
 *              jndi-name="MaintainCodes"
 *              type="Stateless" view-type="local"
 */
public class MaintainCodesBean extends SessionBase
{
    /** Selects all active categories */
	private static final String SQL_SELECT_CATEGORIES =
        "SELECT * FROM Common_Code_Category WHERE row_status_cd='A'";

    /** Selects all active code types */
	private static final String SQL_SELECT_CODE_TYPES =
        "SELECT * FROM Common_Code_Type WHERE row_status_cd='A'";

    /** Selects all active codes for a secific code type */
	private static final String SQL_SELECT_CODES =
        "SELECT * FROM Common_Codes WHERE com_cd_type_key=?";

	private static final String SQL_SELECT_AGREEMENT_NAME =
        "SELECT distinct agreement_name FROM mdu_agreements order by agreement_name";

    /** Inserts a new code type */
	private static final String SQL_INSERT_CODE_TYPE =
        " INSERT INTO Common_Code_Type (com_cd_type_desc, remarks, row_status_cd, category_fk, com_cd_type_key) " +
        " VALUES (?, ?, 'A', ?, ?)";

    /** Updates an existing code type */
	private static final String SQL_UPDATE_CODE_TYPE =
        " UPDATE Common_Code_Type SET com_cd_type_desc=?, remarks=?, category_fk=? " +
        " WHERE com_cd_type_key=?";

    /** Updates an existing code */
    private static final String SQL_UPDATE_CODE =
        " UPDATE Common_Codes SET com_cd_cd=?, com_cd_desc=?, com_cd_sort_key=? " +
        " WHERE com_cd_pk=?";

    /** Deletes a code type */
	private static final String SQL_DELETE_CODE_TYPE =
        "DELETE FROM Common_Code_Type WHERE com_cd_type_key=?";

    /** Deletes all the codes in a given code type */
	private static final String SQL_DELETE_CODES =
        "DELETE FROM Common_Codes WHERE com_cd_type_key=?";

    /** Adds a new code to a code type */
	private static final String SQL_INSERT_CODE =
        " INSERT INTO Common_Codes (com_cd_cd, com_cd_desc, row_status_cd, com_cd_type_key, com_cd_sort_key) " +
        " VALUES (?, ?, 'A', ?, ?)";

    /** Deletes (inactiveates) a code */
	private static final String SQL_INACTIVATE_CODE =
        "UPDATE Common_Codes SET row_status_cd='I' WHERE com_cd_pk=?";

	/** Select 1 if a the code is taken,  otherwise 0*/
    private static final String SQL_SELECT_CODE_IS_TAKEN =
        " DECLARE @taken int " +
        " SET @taken=0 " +
        " SELECT @taken=1 " +
        " WHERE EXISTS (" +
        " SELECT com_cd_cd FROM Common_Codes " +
        " WHERE row_status_cd='A' AND com_cd_cd=? AND com_cd_type_key=? AND (NOT com_cd_pk=?)" +
        " ) SELECT @taken";

    /** Retrieves the key of the code type for a code */
    private static final String SQL_SELECT_TYPE_OF_CODE =
        " SELECT com_cd_type_key FROM Common_Codes WHERE com_cd_pk=?";

    private static final String SQL_SELECT_CODE_DESCRIPTION =
        " SELECT com_cd_desc FROM Common_Codes WHERE com_cd_pk=?";


    /** Name of the list of code types, as cached in JNDI */
	private static final String JNDI_CODE_TYPES = "CodeTypes";

    /** Name of JNDI cache of codes, mapped by primary key.  Under this, is a list of code type names, then the
     * map of codes.  e.g., a java.util.Map of gender codes would be stored in "...Codes/Geneder" */
	private static final String JNDI_CODES_BY_PK = "CodesByPk";

    /** Name of JNDI cache of codes, mapped by code.  Under this, is a list of code type names, then the
     * map of codes.  e.g., a java.util.Map of gender codes would be stored in "...Codes/Geneder" */
	private static final String JNDI_CODES_BY_CODE = "CodesByCode";

    /**
     * map of all they code type categories in the system, by key
     */
    private Map m_categories;

    /**
     * Gets all the categories in the system.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public Map getCategories()
    {
		return Collections.unmodifiableMap(m_categories);
    }

    /**
     * Gets all the code types in the system.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public Map getCodeTypes()
    {
        Map codeTypes;

        //
        // Look for the code types in JNDI
        //

        try {
            codeTypes = (Map)JNDIUtil.getObject(JNDI_CODE_TYPES);
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        if (codeTypes != null)
            return codeTypes;

        //
        // Code Types weren't in JNDI, load them from the database
        //

        codeTypes = new HashMap();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        CodeTypeData codeTypeData = null;

        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_CODE_TYPES);

            while (rs.next()) {
                codeTypeData = new CodeTypeData();
                codeTypeData.setName(rs.getString("com_cd_type_desc"));
                codeTypeData.setDescription(rs.getString("remarks"));
                codeTypeData.setCategory(new Integer(rs.getInt("category_fk")));
                codeTypeData.setKey(rs.getString("com_cd_type_key"));
                codeTypes.put(codeTypeData.getKey(), codeTypeData);
            }

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }

        //
        // Put the Code Types in JNDI
        //

        try {
            JNDIUtil.setObject(JNDI_CODE_TYPES, codeTypes);
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        return codeTypes;
    }

    /**
     * Gets all the codes for a code type.
     *
     * @return a map of CodeData objects, keyed by primary key
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public Map getCodes(String codeType)
    {
        return getCodes(codeType, false);
    }

    /**
     * Gets all the codes for a code type.
     *
     * @return a map of CodeData objects, keyed by code
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public Map getCodesByCode(String codeType)
    {
        return getCodes(codeType, true);
    }

    /**
     * Gets all the codes for a code type.
     * If byCode is true, the returned map is keyed by the code value,
     * otherwise it is keyed by primary key.
     *
     * @return a map of CodeData objects.
     */
    private Map getCodes(String codeType, boolean byCode)
    {
        Map codes;

        //
        // Look for the codes in JNDI
        //
		String jndiName = null;
        if (byCode)
            jndiName = JNDI_CODES_BY_CODE+"/"+codeType;
        else
            jndiName = JNDI_CODES_BY_PK+"/"+codeType;
        try {
            codes = (Map)JNDIUtil.getObject(jndiName);
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        if (codes != null)
            return codes;

        //
        // Codes weren't in JNDI, load them from the database
        //

        codes = new HashMap();
        Connection con = null;
        PreparedStatement stmt = null;
        Statement stmtAgreement = null;
        ResultSet rs = null;
        CodeData codeData = null;

        try {
			con = DBUtil.getConnection();

			if (!(codeType.equalsIgnoreCase("agreement")))
			{
				stmt = con.prepareStatement(SQL_SELECT_CODES);
				stmt.setString(1, codeType);
				rs = stmt.executeQuery();

				while (rs.next()) {
					codeData = new CodeData();
					codeData.setPk(new Integer(rs.getInt("com_cd_pk")));
					codeData.setCode(rs.getString("com_cd_cd"));
					codeData.setDescription(rs.getString("com_cd_desc"));
					codeData.setSortKey(rs.getString("com_cd_sort_key"));
					codeData.setActive(rs.getString("row_status_cd").equals("A"));
					if (byCode) {
						if (codeData.isActive() || (codes.get(codeData.getCode()) == null)) //<-Don't clobber active codes with inactive ones
							codes.put(codeData.getCode(), codeData);
					}
					else
						codes.put(codeData.getPk(), codeData);

				}
			}
			else {
                                stmtAgreement = con.createStatement();

                                rs = stmtAgreement.executeQuery(SQL_SELECT_AGREEMENT_NAME);

				while (rs.next()) {
					codeData = new CodeData();

					codeData.setCode(rs.getString("agreement_name"));
                                        codeData.setActive(true);
					codes.put(codeData.getCode(), codeData);
				}
			}

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			if (!(codeType.equalsIgnoreCase("agreement")))
            	DBUtil.cleanup(con, stmt, rs);
			else
            	DBUtil.cleanup(con, stmtAgreement, rs);
        }

        codes = ValueSortedMap.create(codes);

        //
        // Put the Codes in JNDI
        //

        try {
            JNDIUtil.setObject(jndiName, codes);
        } catch (NamingException e) {
            throw new EJBException(e);
        }

        return codes;
    }

    /**
     * Gets a code type.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public CodeTypeData getCodeType(String codeType)
    {
		return (CodeTypeData)getCodeTypes().get(codeType);
    }

    /**
     * Adds a code type to the system.
	 *
	 * returns false iff the key provided is already taken, otherwise true.
	 *
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean addCodeType(CodeTypeData codeTypeData)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;

		if (isCodeTypeTaken(codeTypeData.getKey()))
			return false;

        try {
			con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_CODE_TYPE);
            ps.setString(1, codeTypeData.getName());
            ps.setString(2, codeTypeData.getDescription());
            ps.setInt(3, codeTypeData.getCategory().intValue());
            ps.setString(4, codeTypeData.getKey());
			ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			clearCodeTypesCache();
            DBUtil.cleanup(con, ps, null);
        }

		return true;
    }

    /**
     * Updates a code type
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void updateCodeType(CodeTypeData codeTypeData)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;

        try {
			con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_CODE_TYPE);
            ps.setString(1, codeTypeData.getName());
            ps.setString(2, codeTypeData.getDescription());
            ps.setInt(3, codeTypeData.getCategory().intValue());
            ps.setString(4, codeTypeData.getKey());
			ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			clearCodeTypesCache();
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Updates a code
	 *
	 * @return ture iff the code was updated, false if the code could not be updated because it is not unique.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean updateCode(CodeData codeData)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;

		if (isCodeTaken(null, codeData))
			return false;

        try {
			con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_UPDATE_CODE);
            ps.setString(1, codeData.getCode());
            ps.setString(2, codeData.getDescription());
            ps.setString(3, codeData.getSortKey());
            ps.setInt(4, codeData.getPk().intValue());
			ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			clearCodesCache();
            DBUtil.cleanup(con, ps, null);
        }

		return true;
    }



    /**
     * adds a code to the system
	 *
	 * @return ture iff the code was added, false if the code could not be added because it is not unique.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public boolean addCode(String codeType, CodeData codeData)
    {
        Connection con = null;
        PreparedStatement ps = null;
		ResultSet rs = null;

		if (isCodeTaken(codeType, codeData))
			return false;

		try {
			con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INSERT_CODE);
            ps.setString(1, codeData.getCode());
            ps.setString(2, codeData.getDescription());
            ps.setString(3, codeType);
            ps.setString(4, codeData.getSortKey());
			ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			clearCodesCache(codeType);
            DBUtil.cleanup(con, ps, null);
        }

		return true;
	}

    /**
     * Removes a code type from the system.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void deleteCodeType    (String codeType)
    {
        Connection con = null;
        PreparedStatement ps = null;

        try {
			con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_DELETE_CODES);
            ps.setString(1, codeType);
			ps.executeUpdate();
            ps = con.prepareStatement(SQL_DELETE_CODE_TYPE);
            ps.setString(1, codeType);
			ps.executeUpdate();
			ps.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			clearCodeTypesCache();
			clearCodesCache(codeType);
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * removed a code from a code type
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public void deleteCode    (Integer codePK)
    {
        Connection con = null;
        PreparedStatement ps = null;

        try {
			con = DBUtil.getConnection();
            ps = con.prepareStatement(SQL_INACTIVATE_CODE);
            ps.setInt(1, codePK.intValue());
			ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
			clearCodesCache();
            DBUtil.cleanup(con, ps, null);
        }
    }

	/**
	 * Load the categories
	 */
	public void ejbCreate() throws CreateException {
		loadCategories();
	}

    /** Reads the Categories from the database and stores them locally */
    protected void loadCategories() {
        m_categories = new HashMap();

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        CategoryData catData = null;

        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_CATEGORIES);

            while (rs.next()) {
                catData = new CategoryData();
                catData.setPk(new Integer(rs.getInt("com_cd_cat_pk")));
                catData.setName(rs.getString("com_cd_cat_nm"));
                m_categories.put(catData.getPk(), catData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
    }

    /**
     * Returns true if the given code type key is already in use
	 */
    protected boolean isCodeTypeTaken(String codeType)  {
		Map codeTypes = getCodeTypes();
		return codeTypes.get(codeType) != null;
	}

	/**
     * Returns true if the given code value is already in use
	 */
    protected boolean isCodeTaken(String codeType, CodeData codeData)  {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        if (codeType == null)
            codeType = getTypeOfCode(codeData.getPk());

		if (TextUtil.isEmpty(codeData.getCode()))
			return false;  //'empty' codes are ok.

        try {
            con = DBUtil.getConnection();

			stmt = con.prepareStatement(SQL_SELECT_CODE_IS_TAKEN);
			stmt.setString(1, codeData.getCode());
            stmt.setString(2, codeType);
            stmt.setInt(3, codeData.getPk() == null ? 0 : codeData.getPk().intValue());
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
    * Clear the code types from JNDI.  Next call to getCodeType.. will refresh it.
    */
    protected void clearCodeTypesCache() {
        try {
            JNDIUtil.unset(JNDI_CODE_TYPES);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    /**
    * Clear the codes for an individual type from JNDI.  Next call to getCode.. will refresh it.
    */
    protected void clearCodesCache(String codeType) {
        try {
            JNDIUtil.unset(JNDI_CODES_BY_CODE+"/"+codeType);
            JNDIUtil.unset(JNDI_CODES_BY_PK+"/"+codeType);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    /**
    * Clear all the the codes from JNDI.  Next call to getCode.. will refresh it.
    */
    protected void clearCodesCache() {
        try {
            JNDIUtil.unset(JNDI_CODES_BY_CODE);
            JNDIUtil.unset(JNDI_CODES_BY_PK);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Retrieves the code type of code from a code pk
     * @param pk The primary key of the code to key the type of
     * @return The key of the code type
     */
    protected String getTypeOfCode(Integer pk) {
        String result = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
			stmt = con.prepareStatement(SQL_SELECT_TYPE_OF_CODE);
			stmt.setInt(1, pk.intValue());
            rs = stmt.executeQuery();
            if (!rs.next())
                throw new EJBException("Type of code with primary key '" + pk + "' not found");
            result = rs.getString(1);
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }

        return result;
    }

    /**
     * Retrieves the code description of code from a code pk
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="NotSupported"
     */
    public String getCodeDescription(Integer pk) {
        String result = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DBUtil.getConnection();
			stmt = con.prepareStatement(SQL_SELECT_CODE_DESCRIPTION);
			stmt.setInt(1, pk.intValue());
            rs = stmt.executeQuery();
            if (!rs.next())
                throw new EJBException("Type of code with primary key '" + pk + "' not found");
            result = rs.getString(1);
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }

        return result;
    }

}
