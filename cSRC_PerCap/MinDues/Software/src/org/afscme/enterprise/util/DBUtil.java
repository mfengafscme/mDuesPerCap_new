package org.afscme.enterprise.util;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import javax.ejb.EJBException;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.StringBuffer;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * Contains static methods for common db functions.
 */
public class DBUtil
{

    /** SQL for retrieving the primary key after an insert. */
    protected static final String SQL_GETKEY = "select @@identity as autoid";

    /** Static instance of the data course */
    protected static DataSource s_dataSource;

    /** Static reference to the logger for the class */
    static Logger log = Logger.getLogger(DBUtil.class);

	/**
	 * Gets a connection from the default datasource.
	 * The connection must be closed.
	 * @return Connection
	 */
	public static Connection getConnection()
	{

        Connection con;
        String dsn = "java:/oltp";

        if (s_dataSource == null) {
            try {
                Context context = JNDIUtil.getInitialContext();
                s_dataSource = (DataSource)context.lookup(dsn);
            } catch (NamingException e) {
                throw new RuntimeException("Could not find the AFSCME data source '" + dsn + "' because: " + e.toString());
            }
        }

        try {
            con = s_dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Could not find the AFSCME data source '" + dsn + "' because: " + e.toString());
        }

        return con;
	}

	/**
	 * Closes the connection, statement and results set if they are not null.
	 */
	public static void cleanup(Connection con, Statement stmt, ResultSet rs)
	{

        Exception cleanupException = null;

        try {
            if (rs != null)
              rs.close();
        } catch (Exception e) {
            cleanupException = e;
            e.printStackTrace();
        }

        try {
            if (stmt != null)
              stmt.close();
        } catch (Exception e) {
            cleanupException = e;
            e.printStackTrace();
        }

        try {
          if (con != null)
              con.close();
        } catch (Exception e) {
            cleanupException = e;
            e.printStackTrace();
        }

        if (cleanupException != null)
            throw new EJBException(cleanupException);
	}

    /**
     * Performs an insert specified by stmt, and returns the identity column value of the new row.
	 * After the call, stmt is closed, <em>con is NOT closed</em>.
     * @param con Connection used to create ps
     * @param stmt The prepared statement conatining the INSERT to perform
     *
     * @exception SQLException propigated from JDBC calls.
     */
    public static Integer insertAndGetIdentity(Connection con, PreparedStatement stmt) throws SQLException {

        ResultSet rs = null;
        Integer identity = null;

        try {
            stmt.executeUpdate();
			stmt.close();
			stmt = con.prepareStatement(SQL_GETKEY);
            rs = stmt.executeQuery();
            rs.next();
            identity = new Integer(rs.getInt("autoid"));
        }
        finally {
			DBUtil.cleanup(null, stmt, rs);
        }

        return identity;
    }

    /** Sets an int parameter on a prepared statement.  If the given integer is null,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableInt(PreparedStatement ps, int index, Integer val) throws SQLException {
        if (val == null)
                ps.setNull(index, Types.INTEGER);
        else
            ps.setInt(index, val.intValue());
    }

    /** Sets an int parameter on a prepared statement.  If the given integer is 0,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableInt(PreparedStatement ps, int index, int val) throws SQLException {
        if (val == 0)
            ps.setNull(index, Types.INTEGER);
        else
            ps.setInt(index, val);
    }

    /** Sets an int parameter on a prepared statement.  If the given string is null,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableInt(PreparedStatement ps, int index, String val) throws SQLException {
        if (val == null)
            ps.setNull(index, Types.INTEGER);
        else
            ps.setInt(index, new Integer(val).intValue());
    }

    public static void setNullableDouble(PreparedStatement ps, int index, Double val) throws SQLException {
        if (val == null)
            ps.setNull(index, Types.DOUBLE);
        else
            ps.setDouble(index, val.doubleValue());
    }

    /** Sets a Varchar parameter on a prepared statement.  If the given Varchar is null,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableVarchar(PreparedStatement ps, int index, String val) throws SQLException {
        if (val == null)
                ps.setNull(index, Types.VARCHAR);
        else
            ps.setString(index, val);
    }

    /** Sets a char(1) parameter on a prepared statement.  If the given char is null,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableChar(PreparedStatement ps, int index, String val) throws SQLException {
        if (val == null)
                ps.setNull(index, Types.CHAR);
        else
            ps.setString(index, val);
    }

    /** Sets a char(1) parameter on a prepared statement.  If the given char is null,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableChar(PreparedStatement ps, int index, Character val) throws SQLException {
        if (val == null)
            ps.setNull(index, Types.CHAR);
        else
            ps.setString(index, val.toString());
    }

    /** Sets a smallint parameter on a prepared statement that is represented by a Boolean.
     * If the given Boolean is null, sets the parameter to null instead
     *
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableBooleanAsShort(PreparedStatement ps, int index, Boolean val) throws SQLException {
        if (val == null) {
            ps.setNull(index, Types.SMALLINT);
        } else {
            setBooleanAsShort(ps, index, val.booleanValue());
        }
    }

    /** Sets a smallint parameter on a prepared statement that is represented by a boolean.
     * This method is used when the associated field in not nullable.
     *
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setBooleanAsShort(PreparedStatement ps, int index, boolean val) throws SQLException {
        short s = (short)(val ? 1 : 0);
        ps.setShort(index, s);
    }

    public static Boolean getBooleanFromShort(short s) {
        return new Boolean((s == (short)1) ? true : false);
    }

    /** Sets a Timestamp parameter on a prepared statement.  If the given Timestamp is null,
     * sets the parameter to null instead
     * @param ps The statement to set the parameter on
     * @param index Index of the parameter in the prepared statement
     * @param val The value to set
     */
    public static void setNullableTimestamp(PreparedStatement ps, int index, Timestamp val) throws SQLException {
        if (val == null)
                ps.setNull(index, Types.TIMESTAMP);
        else
            ps.setTimestamp(index, val);
    }

    /**
     * Gets a set of primary keys assocaited with an entity
     *
     * @param pk Primary key of the entity to get associated keys for
     * @param sql The SQL that gets the fields.
     * @param resultField The name of the column in the result set, containing the result fields.
     * @return A set of primary keys
     */
    public static Set getAssociation(Integer pk, String sql, String resultField)
    {
        return getAssociation(Collections.singletonList(pk), sql, resultField);
    }

    /**
     * Gets a set of primary keys assocaited with an entity
     *
     * @param pkList Compound primary key of the entity to get associated keys for
     * @param sql The SQL that gets the fields.
     * @param resultField The name of the column in the result set, containing the result fields.
     * @return A set of primary keys
     */
    public static Set getAssociation(List pkList, String sql, String resultField)
    {
        Set result = new HashSet();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();

            ps = con.prepareStatement(sql);
            int index = 0;
            Iterator pkIt = pkList.iterator();
            while (pkIt.hasNext())
                ps.setObject(++index, pkIt.next());
            rs = ps.executeQuery();

            while (rs.next())
                result.add(rs.getObject(resultField));

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            cleanup(con, ps, rs);
        }

        return result;
    }

    /**
     * Sets the set of primary keys assocaited with an entity
     *
     * @param ok Primary key of the role to set associated items for
     * @param assocPks Set of primary keys of the items to associate with the entity.
     * @param deleteSQL SQL for deleting the currently associated items.
     * @param insertSQL SQL for inserting a newly associated item.
     */
    public static void setAssociation(Integer pk, Set assocPks, String deleteSQL, String insertSQL)
    {
        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = getConnection();

            //delete the old keys
            ps = con.prepareStatement(deleteSQL);
            ps.setInt(1, pk.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            cleanup(con, ps, null);
        }

        //add the new keys
        addAssociation(pk, assocPks, null, null, insertSQL);
    }

    /**
     * Adds a set of primary keys to an entity association
     *
     * @param pkList Compound Primary key of the entity to add associated items for
     * @param assocPk Primary keys of the item to associate with the entity.
     * @param getSQL SQL for getting the currently associated items.  If null, no get is performed
     * @param resultField Name of the field denoting the assiciation, returned by getSQL.  Can be null iff getSQL is null.
     * @param insertSQL SQL for inserting a newly associated item.
     */
    public static void addAssociation(List pkList, Integer assocPk, String getSQL, String resultField, String insertSQL) {
        addAssociation(pkList, Collections.singleton(assocPk), getSQL, resultField, insertSQL);
    }

    /**
     * Adds a set of primary keys to an entity association
     *
     * @param pk Primary key of the entity to add associated items for
     * @param assocPks Set of primary keys of the items to associate with the entity.
     * @param getSQL SQL for getting the currently associated items.  If null, no get is performed
     * @param resultField Name of the field denoting the assiciation, returned by getSQL.  Can be null iff getSQL is null.
     * @param insertSQL SQL for inserting a newly associated item.
     */
    public static void addAssociation(Integer pk, Set assocPks, String getSQL, String resultField, String insertSQL)
    {
        addAssociation(Collections.singletonList(pk), assocPks, getSQL, resultField, insertSQL);
    }

    /**
     * Adds a set of primary keys to an entity association
     *
     * @param pkList Compound primary key of the entity to add associated items for
     * @param assocPks Set of primary keys of the items to associate with the entity.
     * @param getSQL SQL for getting the currently associated items.  If null, no get is performed
     * @param resultField Name of the field denoting the assiciation, returned by getSQL.  Can be null iff getSQL is null.
     * @param insertSQL SQL for inserting a newly associated item.
     */
    public static void addAssociation(List pkList, Set assocPks, String getSQL, String resultField, String insertSQL)
    {
        Connection con = null;
        PreparedStatement ps = null;
        Set currentKeys = null;

        if (getSQL != null) {
            currentKeys = getAssociation(pkList, getSQL, resultField);
            if (currentKeys.containsAll(assocPks))
                return; //nothing to do, all keys are already associated.
        }

        try {

            con = getConnection();
            StringBuffer debug = null;
            if (log.isDebugEnabled())
                log.debug("Associating " + pkList + " with " + assocPks + " using " + getSQL);

            //insert the new keys
            ps = con.prepareStatement(insertSQL);
            Iterator it = assocPks.iterator();
            while (it.hasNext()) {
                Object assocPk = it.next();
                if (getSQL == null || !currentKeys.contains(assocPk)) {
                    int index = 0;
                    Iterator pkIt = pkList.iterator();
                    while (pkIt.hasNext())
                        ps.setObject(++index, pkIt.next());
                    ps.setObject(++index, assocPk);
                    ps.addBatch();
                }
            }
            ps.executeBatch();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            cleanup(con, ps, null);
        }
    }

    /**
     * Removes a set of primary keys to an entity association
     *
     * @param pk Compound Primary key of the entity to add associated items for
     * @param assocPk Primary key of the item to disassociate with the entity.
     * @param deleteSQL SQL for delete the items.
     */
    public static void removeAssociation(List pkList, Object assocPk, String deleteSQL)
    {
        removeAssociation(pkList, Collections.singleton(assocPk), deleteSQL);
    }

    /**
     * Removes a set of primary keys to an entity association
     *
     * @param pk Primary key of the entity to add associated items for
     * @param assocPks Set of primary keys of the items to disassociate with the entity.
     * @param deleteSQL SQL for delete the items.
     */
    public static void removeAssociation(Object pk, Set assocPks, String deleteSQL)
    {
        removeAssociation(Collections.singletonList(pk), assocPks, deleteSQL);
    }

    /**
     * Removes a set of primary keys to an entity association
     *
     * @param pk Compound Primary key of the entity to add associated items for
     * @param assocPks Set of primary keys of the items to disassociate with the entity.
     * @param deleteSQL SQL for delete the items.
     */
    public static void removeAssociation(List pkList, Set assocPks, String deleteSQL)
    {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(deleteSQL);

        try {

            con = getConnection();

            sql.append("(");
            Iterator it = assocPks.iterator();
            while (it.hasNext()) {
                Object assocPk = it.next();
                sql.append(assocPk.toString());
                if (it.hasNext())
                    sql.append(" , ");
            }
            sql.append(")");

            ps = con.prepareStatement(sql.toString());
            int index = 0;
            Iterator pkIt = pkList.iterator();
            while (pkIt.hasNext())
                ps.setObject(++index, pkIt.next());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            cleanup(con, ps, null);
        }
    }

    /**
     * Runs the given sql statement
     **/
    public static void execute(String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;

		try {
	        con = getConnection();
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            cleanup(con, stmt, null);
        }
    }

    /**
     * Runs the given sql query and gets a single valued result
     **/
    public static Object executeScalar(String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
		Object result = null;

		try {
	        con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
			if (rs.next())
				result = rs.getObject(1);
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            cleanup(con, stmt, null);
        }

        return result;
    }



    /**
     * Returns a string representation of all the data in the given result set
     * @param table The name of the table to dump.
     */
    public static String dump(ResultSet rs) throws SQLException {
            int totalRows = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            ResultSetMetaData rsmd = rs.getMetaData();

            //get the names and widths of the columns
            int cols = rsmd.getColumnCount();
            int totalWidth = 0;
            int[] widths = new int[cols];
            writer.print("    ");
            for (int i = 1; i <= cols; i++) {
                    String name = rsmd.getColumnName(i);
                    widths[i-1] = Math.max(rsmd.getPrecision(i), name.length() + 2);
                    totalWidth += widths[i-1];
                    name = TextUtil.padTrailing(name, widths[i-1], ' ');
                    writer.print(name);
            }

            //print the underline
            writer.println("");
            writer.println("    " + TextUtil.getFill(totalWidth, '-'));

            //get the row data.
            while (rs.next()) {
                    writer.print("    ");
                    for (int i = 1; i <= cols; i++) {
                            String val = rs.getString(i);
                            writer.print(TextUtil.padTrailing(val == null ? "null" : val, widths[i-1], ' '));
                    }
                    writer.println("");
                    totalRows++;
            }

            //trailer
            writer.println("");
            writer.println("    Count: " + totalRows);

            writer.close();
            return baos.toString();
    }

    /**
     * Returns a string representation of all the data in the given table
     * @param table The name of the table to dump.
     */
    public static String dump(String table) throws SQLException {

        Connection con = null;
        Statement stmt = null;
		ResultSet rs = null;
		String result = "";

		try {
			//query the table
	        con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + table);
			result =  dump(rs);
		} finally {
            cleanup(con, stmt, rs);
        }

        return ("Contents of table: "+table+"\n"+result);
    }

    /**
     * Retrieves an Integer or NULL from the ResultSet with the given column name.
     *
     * @param rs        The ResultSet from which to retrieve the return value
     * @param colName   The name of the column holding the return value
     *
     * @return  An Integer representing the int field in the ResultSet.
     */
    public static Integer getIntegerOrNull(ResultSet rs, String colName) throws SQLException {
        if (TextUtil.isEmptyOrSpaces(colName)) {
            return null;
        }
        int value = rs.getInt(colName);
        if (rs.wasNull()) {
            return null;
        } else {
            return new Integer(value);
        }
    }

    /**
     * Retrieves an Integer or NULL from the ResultSet with the given column index.
     *
     * @param rs        The ResultSet from which to retrieve the return value
     * @param colIndex  The index of the column holding the return value
     *
     * @return  An Integer representing the int field in the ResultSet.
     */
    public static Integer getIntegerOrNull(ResultSet rs, int colIndex) throws SQLException {
        if (colIndex > 1) {
            return null;
        }
        int value = rs.getInt(colIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return new Integer(value);
        }
    }

    /**
     * Retrieves an Integer or NULL from the ResultSet with the given column name.
     * If the Integer is a negative value, null is returned.
     *
     * @param rs        The ResultSet from which to retrieve the return value
     * @param colIndex  The index of the column holding the return value
     *
     * @return  An Integer representing the int field in the ResultSet.
     */
    public static Integer getPositiveIntegerOrNull(ResultSet rs, String colName) throws SQLException {
        Integer retVal = getIntegerOrNull(rs, colName);
        if (retVal != null && retVal.intValue() < 1) {
            return null; // only return positive numbers...
        }
        return retVal;
    }

    /**
     * Retrieves an Integer or NULL from the ResultSet with the given column index.
     * If the Integer is a negative value, null is returned.
     *
     * @param rs        The ResultSet from which to retrieve the return value
     * @param colIndex  The index of the column holding the return value
     *
     * @return  An Integer representing the int field in the ResultSet.
     */
    public static Integer getPositiveIntegerOrNull(ResultSet rs, int colIndex) throws SQLException {
        Integer retVal = getIntegerOrNull(rs, colIndex);
        if (retVal != null && retVal.intValue() < 1) {
            return null; // only return positive numbers...
        }
        return retVal;
    }

}
