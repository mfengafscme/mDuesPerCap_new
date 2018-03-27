package org.afscme.enterprise.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Aids in building JDBC Prepared statements
 */
public class PreparedStatementBuilder
{

    static Logger log = Logger.getLogger(PreparedStatementBuilder.class);

    /** Map of Criterion objects */
    // key -- fieldName, value -- List of criterion for this field
    protected Map m_criteria;

    /* List of order by clause Strings */
    protected List m_orderBys;

    /* The index of the first parameter this PreparedStatementBuilder is responsible for */
    protected int m_paramIndex;

    /**
     * @param initialParamIndex Initial index to use for parameters
     */
    public PreparedStatementBuilder()
    {
        this(1);
    }

    /**
     * @param initialParamIndex Initial index to use for parameters
     */
    public PreparedStatementBuilder(int paramIndex)
    {
        m_criteria = new HashMap();
        m_orderBys = new ArrayList();
        m_paramIndex = paramIndex;
    }

    /**
     * Adds a parameter to the where clause
     */
    public void addCriterion(Criterion criterion)
    {
        List criteria;
        String field = criterion.getField();

        if (field == null && !criterion.isSubSelect() )
            return;

        if (criterion.isNullCheck()|| criterion.isSubSelect()
            || !TextUtil.isEmpty(criterion.getValues()))
        {
            if (m_criteria.containsKey(field))
            {
                criteria = (List) m_criteria.get(field);
                criteria.add(criterion);
            }
            else
            {
                criteria = new ArrayList();
                criteria.add(criterion);
                m_criteria.put(field, criteria);
            }
        }
    }

    /**
     * Adds a parameter to the where clause.
     * Converts the Boolean to a short for smallint fields.
     */
    public void addCriterion(String column, Boolean val)
    {
        if (val != null)
        {
            Short shortVal = new Short((val.booleanValue()) ? "1" : "0");
            addCriterion(new Criterion(column, shortVal));
        }
    }

    /**
     * Adds a parameter to the where clause.
     * Converts the Boolean to a short for smallint fields.
     */
    public void addCriterion(String column, Boolean val, String all)
    {
    	if (val != null && !val.booleanValue())
    	{
    		Short shortVal = new Short("1");
    		addCriterion(new Criterion(column, shortVal));
   		}
    }

    /**
     * Adds a parameter to the where clause
     */
    public void addCriterion(String column, Character val)
    {
        if (val != null)
            addCriterion(new Criterion(column, val.toString()));
    }

    /**
     * Adds a IN and/or BETWEEN parameters to the where clause that match the collection of Integers
     */
    public void addCriterion(String column, Collection values)
    {

        //make an IN out of the lone values
        Criterion crit = new Criterion();
        crit.setField(column);
        crit.setOperator("IN");
        crit.setType(Types.INTEGER);
        crit.setValues(new LinkedList(values));
        addCriterion(crit);
    }

    /**
     * Adds a parameter to the where clause
     */
    public void addCriterion(String column, Integer val)
    {
        if (val != null)
            addCriterion(new Criterion(column, val));
    }

    /**
     * Adds a BETWEEN parameter to the where clause of type Integer
     */
    public void addCriterion(String column, Integer i1, Integer i2)
    {
        Criterion crit = new Criterion();
        crit.setField(column);
        crit.setOperator("BETWEEN");
        crit.setType(Types.INTEGER);
        crit.addValue(i1);
        crit.addValue(i2);
        addCriterion(crit);
    }

    /**
     * Adds a parameter to the where clause
     */
    public void addCriterion(String column, String val)
    {
        if (!TextUtil.isEmpty(val))
            addCriterion(new Criterion(column, val));
    }

    /**
     * Adds a parameter to the where clause for wildcards
     */
    public void addCriterion(String column, String val, boolean useLike)
    {
        if (!TextUtil.isEmpty(val))
        {
            addCriterion(new Criterion(column, val, useLike));
        }
    }

    /**
     * Adds a parameter to the where clause for Timestamp.
     */
    public void addCriterion(String column, Timestamp val)
    {
        if (val != null)
            addCriterion(new Criterion(column, val));
    }

    /**
     * Adds a BETWEEN parameter to the where clause of type Timestamp
     */
    public void addCriterion(String column, Timestamp val, Timestamp val2)
    {
        Criterion crit = new Criterion();
        crit.setField(column);
        crit.setOperator("BETWEEN");
        crit.setType(Types.TIMESTAMP);
        crit.addValue(val);
        crit.addValue(val2);
        addCriterion(crit);
    }

    /**
     * Add orderBy columns
     */
    public void addOrderBy(String column)
    {
        if (column != null)
            m_orderBys.add(column);
    }

    public String getOrderByClause()
    {
        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");
        // set up the "order by" clause
        if (!m_orderBys.isEmpty())
        {
            buf.append("ORDER BY");
            Iterator oit = m_orderBys.iterator();
            DelimitedStringBuffer orderByCols = new DelimitedStringBuffer(",");
            while (oit.hasNext())
                orderByCols.append((String) oit.next());
            buf.append(orderByCols.toString());
        }
        return buf.toString();
    }

    public PreparedStatement getPreparedStatement(
        String baseSQLString,
        Connection con)
        throws SQLException
    {
        return getPreparedStatement(baseSQLString, con, true);
    }

    public static PreparedStatement createPreparedStatement(
        String sql,
        List parameters,
        Connection connection)
        throws SQLException
    {
        PreparedStatement retVal = null;

        try
        {
            // fill in the '?' in the prepared statement
            retVal =
                connection.prepareStatement(
                    sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLException(e.toString());
        }

        // set the where parameter values
        setWhereClauseParameters(retVal, parameters);

        return retVal;
    }

    public PreparedStatement getPreparedStatement(
        String baseSQLString,
        Connection con,
        boolean includeWhere)
        throws SQLException
    {

        String sql = getPreparedStatementSQL(baseSQLString, includeWhere);

        System.out.println("*************sql in getPreparedStatement ******************" + sql);

        // DEBUG
        if (log.isDebugEnabled())
            log.debug(
                "Created prepared statement: ["
                    + sql
                    + "] criteria: "
                    + m_criteria.values().toString());

        return createPreparedStatement(sql, getWhereClauseParameters(), con);
    }

    /** Gets the SQL string for the prepared statement */
    public String getPreparedStatementSQL(
        String baseSQLString,
        boolean includeWhere)
    {
        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");

        buf.append(baseSQLString);

        // build the where clause
        buf.append(getWhereClause(includeWhere));

        // build the order by clause
        buf.append(getOrderByClause());

        return buf.toString();
    }

    public String getWhereClause(boolean includeWhere)
    {
        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");
        Iterator fit;
        Iterator cit;
        Criterion criterion;

        // set up the where clause with '?'
        if (!m_criteria.isEmpty())
        {
            if (includeWhere)
                buf.append("WHERE");
            else
                buf.append("AND");

            List criteria;
            // going through each field for its criteria
            fit = m_criteria.values().iterator();
            int counter = 0;
            while (fit.hasNext())
            {
                counter++;
                criteria = (List) fit.next();
                // going through each criterion for this field
                // Their relationship sould be "OR"
                cit = criteria.iterator();
                int count = 0;
                while (cit.hasNext())
                {
                    criterion = (Criterion) cit.next();
                    if (count == 0 && counter != 1)
                    {
                        buf.append(") ");
                        if (criterion.getORFg())
                        {
                            buf.append("OR");
                        }
                        else
                        {
                            buf.append("AND");
                        }
                    }
                    if (count++ == 0)
                    {
                        buf.append('(');
                    }

                    if (criterion.getOperator().equalsIgnoreCase("IN")
                        || criterion.getOperator().equalsIgnoreCase("NOT IN"))
                    { // f IN (?, ?, ..., ?)
                        buf.append(criterion.getField());
                        buf.append(criterion.getOperator());

                        if (criterion.isSubSelect())
                        {
                            log.debug("IN statement with SubSelect");
                            buf.append(criterion.getSubQuery());
                        }
                        else
                        {
                            buf.append('(');

                            DelimitedStringBuffer inVals =
                                new DelimitedStringBuffer(",");
                            for (int i = 0;
                                i < criterion.getValues().size();
                                i++)
                                inVals.append('?');
                            buf.append(inVals.toString());

                            buf.append(')');
                        }
                    }
					else if (criterion.getOperator().equalsIgnoreCase("EXISTS")
						|| criterion.getOperator().equalsIgnoreCase("NOT EXISTS"))
					{ // EXISTS (SUBQUERY)
						buf.append(criterion.getOperator());
						log.debug("exists statement with SubSelect");
						buf.append(criterion.getSubQuery());
					}
                    else
                    {

                        if (criterion
                            .getOperator()
                            .equalsIgnoreCase("BETWEEN"))
                        {
                            buf.append('(');
                            buf.append(criterion.getField());
                            buf.append(criterion.getOperator());
                            buf.append("? AND ? )");
                        }
                        else if (
                            criterion.getOperator().equalsIgnoreCase(
                                "IS NULL"))
                        {
                            buf.append(criterion.getField());
                            buf.append("IS NULL");
                        }
                        else if (
                            criterion.getOperator().equalsIgnoreCase(
                                "IS NOT NULL"))
                        {
                            buf.append(criterion.getField());
                            buf.append("IS NOT NULL");
                        }
                        else
                        {
                            buf.append(criterion.getField());
                            buf.append(criterion.getOperator());
                            buf.append('?');
                        }
                    }

                    if (cit.hasNext())
                        buf.append("OR");
                }
            }
            if (counter != 0)
            {
                buf.append(')');
            }
        }
        return buf.toString();
    }

    //TODO DOCUMENT
    public List getWhereClauseParameters()
    {
        int paramIndex = m_paramIndex;
        List retVal = new ArrayList(m_criteria.size());
        Iterator fit = m_criteria.values().iterator();
        Iterator cit;
        Iterator codeIt;
        List criteria;
        Criterion criterion;
        while (fit.hasNext())
        {
            criteria = (List) fit.next();

            cit = criteria.iterator();
            while (cit.hasNext())
            {
                criterion = (Criterion) cit.next();

				if (!criterion.isNullCheck())
				{
	                for(Iterator i = criterion.getValues().iterator(); i.hasNext();)
	                {
	                        retVal.add(
	                            new Parameter(
	                                i.next(),
	                                criterion.getType(),
	                                paramIndex++));
	                }
                }
            }
        }
        return retVal;
    }

    protected static void setWhereClauseParameters(
        PreparedStatement ps,
        List parameters)
        throws SQLException
    {
        for (Iterator i = parameters.iterator(); i.hasNext();)
        {
            Parameter p = (Parameter) i.next();
            ps.setObject(p.getIndex(), p.getValue(), p.getType());
            log.debug(
                "Parameters  index="
                    + p.getIndex()
                    + " value="
                    + p.getValue()
                    + " type="
                    + p.getType());
        }

    }

    /*
     * Criterion.java
     *
     * Used by PreparedStatementBuilder to store each field's field name, operator, " AND " values.
     * This will accomodate all types of field, operator, " AND " values combinations.
     * Note, the type of the values for each field must be the same.
     *
     * Single value:
     *      field, op, val
     *
     * Double value: (op == BETWEEN)
     *      field, op, val1, val2
     *
     * Coded values: (op == IN)
     *      field, op, val1, val2, ..., valn
     *
     */
    public static class Criterion
    {

        private String field = null;
        private String operator = null;
        private boolean oRFg = false; // default is AND
        private int type; // must be one of the Types
        private List values;
        private String subQuery = null;

        /** Creates a new instance of Criterion */
        public Criterion()
        {
            values = new ArrayList();
        }

        /** Creates a new instance of Criterion */
        public Criterion(String name, Integer value)
        {
            this(name, value, Types.INTEGER);
        }

        /** Creates a new instance of Criterion for characters.  Operator is set to 'like' if true*/
        public Criterion(String name, Object value, boolean useLike)
        {
            this();
            addValue(value);
            setField(name);

            if (useLike)
                setOperator("LIKE");
            else
                setOperator("=");

            setType(Types.VARCHAR);
        }

        /** Creates a new instance of Criterion.  Operator is defaulted to '=' */
        public Criterion(String name, Object value, int type)
        {
            this();
            addValue(value);
            setField(name);
            setOperator("=");
            setType(type);
        }

        /** Creates a new instance of Criterion */
        public Criterion(String name, Short value)
        {
            this(name, value, Types.SMALLINT);
        }

        /** Creates a new instance of Criterion */
        public Criterion(String name, String value)
        {
            this(name, value, Types.VARCHAR);
        }

        /** Creates a new instance of Criterion */
        public Criterion(String name, Timestamp value)
        {
            this(name, value, Types.TIMESTAMP);
        }

        /** Add a value for this field.
         * @param val a new value for this field.
         */
        public void addValue(Object val)
        {
            values.add(val);
        }

        /** Getter for property field.
         * @return Value of property field.
         */
        public String getField()
        {
            return field;
        }

        /** Getter for property operator.
         * @return Value of property operator.
         */
        public String getOperator()
        {
            return operator;
        }

        /** Getter for property oRFg.
         * @return Value of property oRFg.
         */
        public boolean getORFg()
        {
            return oRFg;
        }

        /** Getter for property type.
         * @return Value of property type.
         */
        public int getType()
        {
            return type;
        }

        /** Getter for property values.
         * @return Value of property values.
         */
        public List getValues()
        {
            return values;
        }

        public boolean isNullCheck()
        {
            return (
                operator.equalsIgnoreCase("IS NULL")
                    || operator.equalsIgnoreCase("IS NOT NULL"));
        }

        /** Setter for property field.
         * @param field New value of property field.
         */
        public void setField(String field)
        {
            this.field = field;
        }

        /** Setter for property operator.
         * @param operator New value of property operator.
         */
        public void setOperator(String operator)
        {
            this.operator = operator;
        }

        /** Setter for property oRFg.
         * @param type New value of property oRFg.
         */
        public void setORFg(boolean oRFg)
        {
            this.oRFg = oRFg;
        }

        /** Setter for property type.
         * @param type New value of property type.
         */
        public void setType(int type)
        {
            this.type = type;
        }

        /** Setter for property values.
         * @param values New value of property values.
         */
        public void setValues(List values)
        {
            this.values = values;
        }

        public String toString()
        {
            return "<" + field + " " + operator + " " + values + ">";
        }
        /**
         * @return Returns the subSelect.
         */
        public boolean isSubSelect()
        {
            return getSubQuery() != null;
        }



        /**
         * @return Returns the subQuery.
         */
        public String getSubQuery()
        {
            return subQuery;
        }

        /**
         * @param subQuery The subQuery to set.
         */
        public void setSubQuery(String subQuery)
        {
            this.subQuery = subQuery;
        }

    }

    public static class Parameter implements Serializable
    {
        public static List copyParameters(
            List parameters,
            int newStartingIndex)
        {
            List retVal = new ArrayList(parameters.size());

            int newIndex = newStartingIndex;
            for (Iterator i = parameters.iterator(); i.hasNext(); newIndex++)
            {
                Parameter p = (Parameter) i.next();

                if (newStartingIndex == -1)
                    newIndex = p.getIndex();

                retVal.add(new Parameter(p.getValue(), p.getType(), newIndex));
            }
            return retVal;
        }

        private int index = -1;
        private int type;
        private Object value;

        public Parameter()
        {
        }

        public Parameter(Object value, int type)
        {
            this(value, type, -1);
        }

        public Parameter(Object value, int type, int index)
        {
            this.value = value;
            this.type = type;
            this.index = index;
        }

        /**
         * @return Returns the index.
         */
        public int getIndex()
        {
            return index;
        }

        /**
         * @return Returns the type.
         */
        public int getType()
        {
            return type;
        }

        /**
         * @return Returns the value.
         */
        public Object getValue()
        {
            return value;
        }

    }

}
