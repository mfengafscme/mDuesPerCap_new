package org.afscme.enterprise.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class PreparedUpdateStatementBuilder extends PreparedStatementBuilder
{

    /** List of UpdateField objects. */
    private List m_updateFields;

    static Logger log = Logger.getLogger(PreparedUpdateStatementBuilder.class);

    /** Creates a new instance of PreparedUpdateStatementBuilder */
    public PreparedUpdateStatementBuilder()
    {
        this(1);
    }

    /** Creates a new instance of PreparedUpdateStatementBuilder */
    public PreparedUpdateStatementBuilder(int paramIndex)
    {
        super(paramIndex);
        this.m_updateFields = new ArrayList();
    }

    public void addUpdateField(UpdateField uc)
    {
        if (!TextUtil.isEmptyOrSpaces(uc.getField())
            && !TextUtil.isEmpty(uc.getValue()))
        {
            this.m_updateFields.add(uc);
        }
    }

    public void addUpdateField(String column, String value)
    {
        addUpdateField(new UpdateField(column, value));
    }

    public void addUpdateField(String column, Integer value)
    {
        addUpdateField(new UpdateField(column, value));
    }

    public void addUpdateField(String column, Short value)
    {
        addUpdateField(new UpdateField(column, value));
    }

    public void addUpdateField(String column, Timestamp value)
    {
        addUpdateField(new UpdateField(column, value));
    }

    public void addUpdateField(String column, InputStream value, int fileSize)
    {
        if (!TextUtil.isEmptyOrSpaces(column) && value != null)
        {
            this.m_updateFields.add(
                new UpdateField(column, value, new Integer(fileSize)));
        }
    }

    public String getPreparedStatementSQL(
        String baseSQLString,
        boolean includeSet,
        boolean includeWhere)
    {
        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");

        // build the set fields clause
        addUpdateFieldsClauseToSQL(buf, includeSet);

        // build the where clause 
        buf.append(this.getWhereClause(includeWhere));

        buf.prepend(baseSQLString);
        return buf.toString();
    }

    public PreparedStatement getPreparedStatement(
        String baseSQLString,
        Connection con,
        boolean includeWhere)
        throws SQLException
    {
        if (!includeWhere && !CollectionUtil.isEmpty(this.m_updateFields))
        {
            throw new SQLException("This method is being used improperly for this class. Please see javadocs for proper use.");
        }
        if (CollectionUtil.isEmpty(this.m_updateFields))
        {
            return getPreparedStatement(
                baseSQLString,
                con,
                false,
                includeWhere);
        }
        else
        {
            return getPreparedStatement(baseSQLString, con, true, includeWhere);
        }
    }

    public PreparedStatement getPreparedStatement(
        String baseSQLString,
        Connection con,
        boolean includeSet,
        boolean includeWhere)
        throws SQLException
    {
        String sql =
            getPreparedStatementSQL(baseSQLString, includeSet, includeWhere);

        // DEBUG
        if (log.isDebugEnabled())
            log.debug(
                "Created prepared statement: ["
                    + sql
                    + "] update: "
                    + CollectionUtil.toString(m_updateFields)
                    + " criteria: "
                    + m_criteria.toString());

        PreparedStatement ps = null;
        try
        {
            // fill in the '?' in the prepared statement
            ps =
                con.prepareStatement(
                    sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLException(e.toString());
        }

        // set the set parameter values
        setUpdateClauseParameters(ps);

        // set the where parameter values
        setWhereClauseParameters(ps, getWhereClauseParameters());

        return ps;
    }

    protected void setUpdateClauseParameters(PreparedStatement ps)
        throws SQLException
    {
        if (!CollectionUtil.isEmpty(this.m_updateFields))
        {
            UpdateField uc = null;
            for (Iterator it = this.m_updateFields.iterator(); it.hasNext();)
            {
                uc = (UpdateField) it.next();
                log.debug(
                    "setting object: \n"
                        + "    paramIndex = "
                        + m_paramIndex
                        + "\n"
                        + "    value = "
                        + uc.getValue()
                        + "\n"
                        + "    type = "
                        + uc.getType());
                if (uc.getType() == Types.LONGVARBINARY)
                {
                    ps.setBinaryStream(
                        m_paramIndex++,
                        (InputStream) uc.getValue(),
                        uc.getFileSize());
                }
                else
                {
                    ps.setObject(m_paramIndex++, uc.getValue(), uc.getType());
                }
            }
        }
    }

    protected void addUpdateFieldsClauseToSQL(
        DelimitedStringBuffer buf,
        boolean includeSet)
    {
        if (!CollectionUtil.isEmpty(this.m_updateFields))
        {
            if (includeSet)
            {
                buf.append("SET");
            }
            else
            {
                buf.append(", ");
            }
            DelimitedStringBuffer updateFields =
                new DelimitedStringBuffer(", ");
            String key;
            for (Iterator it = this.m_updateFields.iterator(); it.hasNext();)
            {
                updateFields.append(
                    ((UpdateField) it.next()).getField() + "=?");
            }
            buf.append(updateFields.toString().trim());
        }
    }
    // Override xxxOrderBy methods that are not applicable for updates...

    public void addOrderBy(String column)
    {
        // does nothing...
    }

    public static class UpdateField
    {

        private String field = null;
        private Object value = null;
        private int type; // must be one of the java.sql.Types
        private int fileSize; // only valid if value is InputStream

        /** Creates a new instance of UpdateField */
        public UpdateField()
        {
            this.field = null;
            this.value = null;
            this.type = -1;
            this.fileSize = -1;
        }

        /** Creates a new instance of UpdateField */
        public UpdateField(String name, String value)
        {
            this(name, value, Types.VARCHAR);
        }

        /** Creates a new instance of UpdateField */
        public UpdateField(String name, Integer value)
        {
            this(name, value, Types.INTEGER);
        }

        /** Creates a new instance of UpdateField */
        public UpdateField(String name, Short value)
        {
            this(name, value, Types.SMALLINT);
        }

        /** Creates a new instance of Criterion */
        public UpdateField(String name, Timestamp value)
        {
            this(name, value, Types.TIMESTAMP);
        }

        /** Creates a new instance of Criterion */
        public UpdateField(String name, InputStream value, Integer size)
        {
            this(name, value, Types.LONGVARBINARY);
            this.fileSize = size.intValue();
        }

        /** Creates a new instance of Criterion.  Operator is defaulted to '=' */
        public UpdateField(String name, Object value, int type)
        {
            this();
            this.field = name;
            this.value = value;
            this.type = type;
        }

        /** Getter for property field.
         * @return Value of property field.
         */
        public String getField()
        {
            return field;
        }

        /** Setter for property field.
         * @param field New value of property field.
         *
         */
        public void setField(String field)
        {
            this.field = field;
        }

        /** Getter for property value.
         * @return Value of property value.
         *
         */
        public Object getValue()
        {
            return value;
        }

        /** Setter for property value.
         * @param value New value of property value.
         *
         */
        public void setValue(Object value)
        {
            this.value = value;
        }

        /** Getter for property type.
         * @return Value of property type.
         *
         */
        public int getType()
        {
            return type;
        }

        /** Setter for property type.
         * @param value New value of property type.
         *
         */
        public void setType(int type)
        {
            this.type = type;
        }

        /** Getter for property fileSize.
         * @return Value of property fileSize.
         *
         */
        public int getFileSize()
        {
            return fileSize;
        }

        /** Setter for property fileSize.
         * @param value New value of property fileSize.
         *
         */
        public void setFileSize(int fileSize)
        {
            this.fileSize = fileSize;
        }

        public String toString()
        {
            return "PreparedUpdateStatementBuilder.UpdateField {field="
                + field
                + ", value="
                + this.value
                + ", type="
                + this.type
                + "}";
        }

    }
}
