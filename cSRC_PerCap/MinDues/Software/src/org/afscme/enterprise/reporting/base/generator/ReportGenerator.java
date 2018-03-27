/*
 * ReportGenerator.java
 *
 * This is a utility class used by both the ReportGeneratorMDB and the front end
 * Java scripts.
 */

package org.afscme.enterprise.reporting.base.generator;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.Codes;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.reporting.ReportHandler;
import org.afscme.enterprise.reporting.base.BRUtil;
import org.afscme.enterprise.reporting.base.access.OutputColumnData;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.ReportOutputFieldData;
import org.afscme.enterprise.reporting.base.access.ReportSortFieldData;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.PreparedStatementBuilder.Criterion;
import org.afscme.enterprise.util.PreparedStatementBuilder.Parameter;

public class ReportGenerator implements ReportHandler
{
    public static int ALL_ROWS = -1;
    /** Inserts correspondences */
    private static String SQL_INSERT_CORRESPONDENCES =
        "INSERT INTO Person_Correspondence_History "
            + "(person_pk, correspondence_dt, report_pk) "
            + "SELECT person_pk, getdate(), ? ";

    private static String[] ADDRESS_COLUMNS =
        new String[] {
            "full_address",
            "address_id",
            "addr1",
            "addr2",
            "city",
            "state",
            "state_cd",
            "zipcode",
            "zip_plus",
            "carrier_route_info",
            "province",
            "country",
            };

    protected static Logger log = Logger.getLogger(ReportGenerator.class);

    protected Map reportFields; // a map of "fieldPK' to 'ReportField'
    protected MaintainCodes codeBean;
    protected OutputFormat outputFormat;
    protected Report report;
    protected int maxRows;
    protected List columnInfo;
    protected Set accessibleAffs;
    protected PreparedStatement statement;
    protected Connection connection;
    protected ResultSet resultSet;
    protected boolean filterDuplicateAddresses;

    /** Creates a new instance of ReportGenerator */
    public ReportGenerator(
        Map reportFields,
        Report report,
        OutputFormat outputFormat,
        Set accessibleAffs)
    {
        this(
            reportFields,
            report,
            outputFormat,
            ALL_ROWS,
            accessibleAffs,
            false);
    }

    /** Creates a new instance of ReportGenerator */
    public ReportGenerator(Map reportFields, Report report, Set accessibleAffs)
    {
        this(reportFields, report, null, ALL_ROWS, accessibleAffs, false);
    }

    /** Creates a new instance of ReportGenerator */
    public ReportGenerator(
        Map reportFields,
        Report report,
        int maxRows,
        Set accessibleAffs)
    {
        this(reportFields, report, null, maxRows, accessibleAffs, false);
    }

    /** Creates a new instance of ReportGenerator */
    public ReportGenerator(
        Map reportFields,
        Report report,
        OutputFormat outputFormat,
        int maxRows,
        Set accessibleAffs,
        boolean filterDuplicateAddresses)
    {
        this.report = report;
        this.reportFields = reportFields;
        this.outputFormat = outputFormat;
        this.maxRows = maxRows;
        this.columnInfo = new LinkedList();
        this.accessibleAffs = accessibleAffs;
        this.filterDuplicateAddresses = filterDuplicateAddresses;
    }

    /**
     * Closes a statement opened by a call to generate
     */
    public void close() throws Exception
    {
        DBUtil.cleanup(connection, statement, resultSet);
        if (codeBean != null)
            codeBean.remove();

        codeBean = null;
        connection = null;
        statement = null;
        resultSet = null;
    }

    /** Generate a report and returns the result as a ResultSet.
     *  This method is mainly used by the front end JSP/Servlet to either "Preview"
     *  a report or to generate an "On Screen" report.
     *
     * Caller should call close() on this object when done with the ResultSet
     */
    public ResultSet generateReport()
        throws SQLException, NamingException, CreateException
    {

        connection = DBUtil.getConnection();
        codeBean = JNDIUtil.getMaintainCodesHome().create();
        statement = createQueryStatement(connection);
        resultSet = statement.executeQuery();

        return resultSet;
    }

    /** Generate the report preview result and store it in a row-column map */
    public int generateReport(List result) throws Exception
    {

        // generate the report
        generateReport();
        ResultSetMetaData rsmd = resultSet.getMetaData();

        // get the data
        while (resultSet.next())
        {
            List row = new LinkedList();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
                row.add(TextUtil.format(resultSet, i, "&nbsp;"));
            result.add(row);
        }
        close();

        //figure out the total row count
        int total;
        if (report.getReportData().isCountReport())
            total = 1;
        else if (maxRows == -1 || maxRows > result.size())
            total = result.size();
        else
        {
            report.getReportData().setCountReport(true);
            generateReport();
            resultSet.next();
            total = resultSet.getInt(1);
            report.getReportData().setCountReport(false);
            close();
        }

        return total;
    }

    /**
     * Generates custom query to the given output stream.
     *
     * @return The count of records generated.
     */
    public int generate(OutputStream stream) throws Exception
    {

        ReportFormatter formatter;

        try
        {
            connection = DBUtil.getConnection();
            codeBean = JNDIUtil.getMaintainCodesHome().create();
            statement = createQueryStatement(connection);
            resultSet = statement.executeQuery();

            int fType = outputFormat.getFormat();
            switch (fType)
            {
                case OutputFormat.TAB :
                case OutputFormat.COMMA :
                case OutputFormat.SEMICOLON :
                case OutputFormat.MAIL_MERGE :
                case OutputFormat.MAILING_HOUSE :
                    formatter =
                        new DelimitedTextFormatter(
                            report.getReportData(),
                            outputFormat,
                            columnInfo,
                            stream);
                    break;
                case OutputFormat.LABEL :
                    formatter =
                        new LabelFormatter(
                            report.getReportData(),
                            stream,
                            resultSet);
                    break;
                case OutputFormat.PDF :
                    formatter =
                        new PDFFormatter(
                            report.getReportData(),
                            columnInfo,
                            stream);
                    break;
                default :
                    throw new RuntimeException("Illegal report type " + fType);
            }

            formatter.readData(resultSet);

        }
        finally
        {
            close();
        }

        int count = formatter.formatReport();

        if (report.getReportData().getNeedUpdateCorrespondence())
            updateCorrespondenceHistory();

        return count;
    }

    /**
     * Gets the PreparedStatement for the query
     */
    private PreparedStatement createQueryStatement(Connection con)
        throws SQLException
    {
        if (report.getReportData().isMailingList())
            return createMailingListQueryStatement(con);

        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        addParameters(builder);
        addAffiliateFilter(builder);
        addOrderBys(builder);

        String selectClause = getSelectClause();
        String fromClause = getFromClause();
        String whereClause = builder.getWhereClause(true);
        String orderByClause = builder.getOrderByClause();
        List parameters = builder.getWhereClauseParameters();

        DelimitedStringBuffer sqlBuffer = new DelimitedStringBuffer(" ");
        sqlBuffer.append(selectClause);
        sqlBuffer.append(fromClause);

        sqlBuffer.append(whereClause);

        sqlBuffer.append(orderByClause);

        String sql = sqlBuffer.toString();

        log.debug("Generated SQL ====> " + sql);

        return PreparedStatementBuilder.createPreparedStatement(
            sql,
            parameters,
            con);
    }

    private PreparedStatement createMailingListQueryStatement(Connection con)
        throws SQLException
    {
        PreparedStatementBuilder builder = new PreparedStatementBuilder();
        addParameters(builder);
        addAffiliateFilter(builder);
        addOrderBys(builder);

        String complexAdditionalWhereClause = null;
        String drivingAddressTable = getDrivingAddressTable();
        Set joinTableNames = getJoinTableNames();
        if (drivingAddressTable.equals(BRUtil.OFFICER_VIEW))
        {
            {
                Criterion crit = new Criterion();
                crit.setField(BRUtil.OFFICER_VIEW + ".pos_end_dt");
                crit.setOperator("IS NULL");
                builder.addCriterion(crit);
            }
            {
                Criterion crit = new Criterion();
                crit.setField(BRUtil.OFFICER_VIEW + ".address_id");
                crit.setOperator("IS NOT NULL");
                builder.addCriterion(crit);
            }
            {
                complexAdditionalWhereClause =
                    "("
                        + "v_affiliate.aff_status not in ('Restricted Administratorship', 'Unrestricted Administratorship')"
                        + " or "
                        + "("
                        + "v_affiliate.aff_status in ('Restricted Administratorship', 'Unrestricted Administratorship')"
                        + "and v_officer.title = 'Administrator'"
                        + ")"
                        + ")";

                //17010 = 'Restricted Administratorship'
                //17012 = 'Unrestricted Administratorship'
                //TODO uncomment the lines below (replacing the ones above) once the pk is added to the V_affiliate view
                //                complexAdditionalWhereClause =
                //                    "("
                //                        + "v_affiliate.aff_status_pk not in (17010,17012)"
                //                        + " or "
                //                        + "("
                //                        + "v_affiliate.aff_status in (17010,17012)"
                //                        + "and v_officer.title = 'Administrator'"
                //                        + ")"
                //                        + ")";
            }
        }
        if (joinTableNames.contains(BRUtil.PERSON_VIEW))
        {
            {
                Criterion crit = new Criterion();
                crit.setField(
                    "ISNULL(" + BRUtil.PERSON_VIEW + ".deceased_fg, 'False')");
                crit.setOperator("=");
                crit.setType(Types.VARCHAR);
                crit.addValue("False");
                builder.addCriterion(crit);
            }
            {
                Criterion crit = new Criterion();
                crit.setField(
                    "ISNULL("
                        + BRUtil.PERSON_VIEW
                        + ".marked_for_deletion_fg, 'False')");
                crit.setOperator("=");
                crit.setType(Types.VARCHAR);
                crit.addValue("False");
                builder.addCriterion(crit);
            }
        }
        if (joinTableNames.contains(BRUtil.MEMBER_VIEW))
        {
            {
                Criterion crit = new Criterion();
                crit.setField(
                    "ISNULL(" + BRUtil.MEMBER_VIEW + ".no_mail_fg, 'False')");
                crit.setOperator("=");
                crit.setType(Types.VARCHAR);
                crit.addValue("False");
                builder.addCriterion(crit);
            }
            {
                Criterion crit = new Criterion();
                crit.setField(BRUtil.MEMBER_VIEW + ".mbr_status_pk");
                crit.setOperator("<>");
                crit.setType(Types.INTEGER);
				crit.addValue(Codes.MemberStatus.I); //Inactive
                builder.addCriterion(crit);
            }
        }
        if (joinTableNames.contains(BRUtil.AFFILIATE_VIEW))
        {
            {
                Criterion crit = new Criterion();
                crit.setField(BRUtil.AFFILIATE_VIEW + ".aff_status_pk");
                crit.setOperator("NOT IN");
                crit.setType(Types.INTEGER);
                crit.addValue(Codes.AffiliateStatus.PD); //peding deactivation
                crit.addValue(Codes.AffiliateStatus.D); //deactivated
                builder.addCriterion(crit);
            }
        }
        if (joinTableNames.contains(BRUtil.AFFILIATE_ADDRESS_VIEW))
        {
            {
                Criterion crit = new Criterion();
                crit.setField(BRUtil.AFFILIATE_ADDRESS_VIEW + ".org_addr_type");
                crit.setOperator("=");
                crit.setType(Types.INTEGER);
                crit.addValue(Codes.OrgAddressType.REGULAR); //regular
                builder.addCriterion(crit);
            }
        }

        String selectClause = getSelectClause();
        String fromClause = getFromClause();
        String whereClause = builder.getWhereClause(true);
        if (complexAdditionalWhereClause != null
            && complexAdditionalWhereClause.length() > 0)
        {
            if (whereClause.length() > 0)
                whereClause += " AND " + complexAdditionalWhereClause;
            else
                whereClause = " WHERE " + complexAdditionalWhereClause;
        }
        String orderByClause = builder.getOrderByClause();
        List parameters = builder.getWhereClauseParameters();

        DelimitedStringBuffer sqlBuffer = new DelimitedStringBuffer(" ");
        sqlBuffer.append(selectClause);

        if (drivingAddressTable.equals(BRUtil.PERSON_ADDRESS_VIEW))
        {
            fromClause
                += " inner join person_sma on v_person_address.person_pk = person_sma.person_pk and v_person_address.address_id = '1'+CONVERT(varchar, person_sma.address_pk) and person_sma.current_fg = 1";
        }
        sqlBuffer.append(fromClause);
        sqlBuffer.append(whereClause);

        if (filterDuplicateAddresses)
        {
            String pk = getDrivingTable() + "." + getDrivingTablePK();

            if (pk != null && drivingAddressTable != null)
            {
                drivingAddressTable += ".";
                if (whereClause.length() == 0)
                    sqlBuffer.append("where");
                else
                    sqlBuffer.append("and");

                sqlBuffer.append(pk);
                sqlBuffer.append("IN (");
                sqlBuffer.append("Select min(" + pk + ")");
                sqlBuffer.append(fromClause);
                sqlBuffer.append(whereClause);
                sqlBuffer.append(
                    "group by "
                        + drivingAddressTable
                        + "addr1, "
                        + drivingAddressTable
                        + "addr2, "
                        + drivingAddressTable
                        + "city, "
                        + drivingAddressTable
                        + "state, "
                        + drivingAddressTable
                        + "zipcode, "
                        + drivingAddressTable
                        + "zip_plus");
                sqlBuffer.append(")");

                parameters.addAll(
                    Parameter.copyParameters(
                        parameters,
                        parameters.size() + 1));
            }
        }

        sqlBuffer.append(orderByClause);

        String sql = sqlBuffer.toString();

        log.debug("Generated Mailing List SQL ====> " + sql);

        return PreparedStatementBuilder.createPreparedStatement(
            sql,
            parameters,
            con);
    }

    private String getDrivingTablePK()
    {
        String drivingTable = getDrivingTable();

        if (drivingTable == null)
            return null;

        String pk = null;
        if (BRUtil.isAffiliateView(drivingTable))
            return "aff_pk";
        else
            return "person_pk";
    }

    private String getDrivingAddressTable()
    {
        final String[] ADDRESS_TABLES =
            new String[] {
                BRUtil.OFFICER_VIEW,
                BRUtil.AFFILIATE_ADDRESS_VIEW,
                BRUtil.PERSON_ADDRESS_VIEW };

        List addressColumnsList = Arrays.asList(ADDRESS_COLUMNS);

        List sortedOutputFields = new ArrayList();
        //explicitly using the report's output fields instead of the
        //private function in this class.
        sortedOutputFields.addAll(report.getOutputFields().values());
        Collections.sort(sortedOutputFields);

        int bestAddress = ADDRESS_TABLES.length;

        for (Iterator i = sortedOutputFields.iterator();
            i.hasNext() && bestAddress != 0;
            )
        {
            ReportOutputFieldData outputField =
                (ReportOutputFieldData) i.next();

            ReportField field = lookupFieldData(outputField.getFieldPK());
            String tableName = field.getTableName();
            String columnName = field.getColumnName();

            for (int j = 0, max = ADDRESS_TABLES.length; j < max; j++)
                if (j < bestAddress
                    && tableName.equals(ADDRESS_TABLES[j])
                    && (addressColumnsList.contains(columnName)
                        || columnName.equals("title")))
                    bestAddress = j;

        }
        if (bestAddress != ADDRESS_TABLES.length)
            return ADDRESS_TABLES[bestAddress];

        return BRUtil.PERSON_ADDRESS_VIEW;
    }

    private String getDrivingTable()
    {
        Iterator i = getJoinTableNames().iterator();
        if (!i.hasNext())
            return null;

        String drivingTable = (String) i.next();
        return drivingTable;
    }

    /**
     * Gets the PreparedStatement that updates correspondence
     */
    private PreparedStatement createUpdateCorrespondenceStatement(Connection con)
        throws SQLException
    {

        PreparedStatementBuilder builder = new PreparedStatementBuilder(2);
        addParameters(builder);
        addAffiliateFilter(builder);

        String sql = SQL_INSERT_CORRESPONDENCES + " " + getFromClause();

        PreparedStatement ps = builder.getPreparedStatement(sql, con);
        ps.setInt(1, report.getReportPK().intValue());

        return ps;
    }

    /**
     * Gets field data for the given field pk
     */
    private ReportField lookupFieldData(Integer fieldPK)
    {
        ReportField field = null;

        // first, search the top-level fields
        field = (ReportField) reportFields.get(fieldPK);

        if (field != null)
            return field;
        else
        { // we have to traverse the entire map to find who has this child field.
            ReportField topField;
            ReportField childField;
            Iterator cir;

            Iterator tir = reportFields.values().iterator();
            lookchild : while (tir.hasNext())
            {
                topField = (ReportField) tir.next();

                if (topField.hasChildren())
                { // look into its children
                    cir = topField.getChildren().iterator();
                    while (cir.hasNext())
                    {
                        childField = (ReportField) cir.next();

                        if (fieldPK.equals(childField.getPk()))
                        { // we found the field
                            field = childField;
                            break lookchild;
                        }
                    }
                }
            }

            return field;
        }

    }

    /**
      * Gets field data for the given table and column name
      */
    private ReportField lookupFieldData(String tableName, String columnName)
    {
        ReportField field = null;

        // we have to traverse the entire map to find who has this child field.
        ReportField topField;
        ReportField childField;
        Iterator cir;

        Iterator tir = reportFields.values().iterator();
        lookchild : while (tir.hasNext())
        {
            topField = (ReportField) tir.next();
            if (topField.getColumnName().equalsIgnoreCase(columnName)
                && topField.getTableName().equalsIgnoreCase(tableName))
            {
                field = topField;
                break;
            }
            if (topField.hasChildren())
            { // look into its children
                cir = topField.getChildren().iterator();
                while (cir.hasNext())
                {
                    childField = (ReportField) cir.next();

                    if (childField.getColumnName().equalsIgnoreCase(columnName)
                        && childField.getTableName().equalsIgnoreCase(tableName))
                    {
                        field = childField;
                        break lookchild;
                    }
                }
            }
        }

        return field;
    }

    private ReportOutputFieldData getReportOutputFieldData(
        String tableName,
        String columnName,
        short position)
    {
        ReportOutputFieldData retVal = null;
        ReportField field = lookupFieldData(tableName, columnName);
        if (field != null)
        {
            retVal = new ReportOutputFieldData();
            retVal.setFieldPK(field.getPk());
            retVal.setOutputOrder(position);
        }
        return retVal;

    }

    /**
     * Returns a join clause joining view2 to view 1
     */
    private String join(String view1, String view2)
    {
        String pk;
        String joinType = "INNER";
        if (BRUtil.isAffiliateView(view1) || BRUtil.isAffiliateView(view2))
            pk = "aff_pk";
        else
            pk = "person_pk";
        if (BRUtil.isOneToManyView(view2))
        {
            log.debug("join(" + view1 + ", " + view2 + ") -> getJoinType()");
            joinType = getJoinType(view2);
        }

        log.debug("join(" + view1 + ", " + view2 + ") = " + joinType);
        return joinType
            + " JOIN "
            + view2
            + " ON "
            + view1
            + "."
            + pk
            + " = "
            + view2
            + "."
            + pk
            + " ";
    }

    private String getJoinType(String view)
    {
        Map criteriaFields = report.getCriteriaFields();
        if ((criteriaFields != null) && (!criteriaFields.isEmpty()))
        {
            Iterator it = criteriaFields.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry mapEntry = (Map.Entry) it.next();
                List fieldCriteria = (List) mapEntry.getValue();
                Iterator criteriaIt = fieldCriteria.iterator();
                while (criteriaIt.hasNext())
                {
                    ReportCriterionData field =
                        (ReportCriterionData) criteriaIt.next();
                    ReportField fieldData = lookupFieldData(field.getFieldPK());
                    if (fieldData.getTableName().equals(view))
                    {
                        return "INNER";
                    }
                }
            }
        }
        return "LEFT OUTER";
    }

    /**
     * Gets a set of all the table names needed for the join
     */
    private Set getJoinTableNames()
    {
        Set result = new TreeSet(CollectionUtil.orderAs(BRUtil.VIEW_NAMES));

        //add table names from output fields
        addTables(getOutputFields(), result);

        //add table names from criteria fields
        addTables(report.getCriteriaFields(), result);

        //add table names from sort fields
        addTables(report.getSortFields(), result);

        //if this is an affiliate, we need to add the affiliate view so the filter will work
        if (accessibleAffs != null && !result.contains(BRUtil.AFFILIATE_VIEW))
            result.add(BRUtil.AFFILIATE_VIEW);

        //if we're joining Person, Person_Address or Person_Phone to Affiliate, need to add Member view
        if (BRUtil.collectionContainsAny(result, BRUtil.PERSON_VIEWS)
            && BRUtil.collectionContainsAny(result, BRUtil.AFFILIATE_VIEWS)
            && !(result.contains(BRUtil.OFFICER_VIEW)
                || result.contains(BRUtil.MEMBER_VIEW)))
            result.add(BRUtil.MEMBER_VIEW);
        //Affiliate requirements TBD treating like officer view.

        return result;
    }

    public List getOutputFieldNames()
    {

        List result = new LinkedList();

        //add table names from output fields
        List fields = new LinkedList(getOutputFields().values());
        Collections.sort(fields);
        Iterator it = fields.iterator();
        while (it.hasNext())
        {
            ReportOutputFieldData field = (ReportOutputFieldData) it.next();
            ReportField fieldData = lookupFieldData(field.getFieldPK());
            result.add(fieldData.getDisplayName());
        }

        return result;
    }

    /**
     * Adds all the tables names of the given fields to the given tables set
     *
     * @param fieldPks Map who's keys are a set of field pk Integer objects
     * @param tables (out) String table names are put here
     */
    private void addTables(Map fieldPks, Set tables)
    {
        if (fieldPks != null)
        {
            Iterator it = fieldPks.keySet().iterator();
            while (it.hasNext())
            {
                ReportField fieldData = lookupFieldData((Integer) it.next());
                tables.add(fieldData.getTableName());
            }
        }
    }

    /**
     Gets column clause SQL string (the part after the SELECT and before the FROM)
     */
    private String getColumnsClause()
    {
        columnInfo.clear();

        // sort the output fields with "output order" in ascending order
        List sortedOutputFields = new ArrayList(getOutputFields().values());
        Collections.sort(sortedOutputFields);

        DelimitedStringBuffer selectClause = new DelimitedStringBuffer(", ");

        for (Iterator i = sortedOutputFields.iterator(); i.hasNext();)
        {
            ReportOutputFieldData outputField =
                (ReportOutputFieldData) i.next();
            ReportField fieldData = lookupFieldData(outputField.getFieldPK());
            selectClause.append(
                fieldData.getTableName() + "." + fieldData.getColumnName());
            columnInfo.add(
                new OutputColumnData(
                    fieldData.getDisplayName(),
                    fieldData.getPrintWidth()));
        }

        return selectClause.toString();
    }

    /**
     * Adds all the search parameters to 'builder'
     */
    private void addParameters(PreparedStatementBuilder builder)
    {

        Map criteriaFields = report.getCriteriaFields();
        if ((criteriaFields != null) && (!criteriaFields.isEmpty()))
        {
            Iterator it = criteriaFields.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry mapEntry = (Map.Entry) it.next();
                List fieldCriteria = (List) mapEntry.getValue();
                ReportField fieldData =
                    lookupFieldData((Integer) mapEntry.getKey());
                Iterator criteriaIt = fieldCriteria.iterator();

                nextField : while (criteriaIt.hasNext())
                { // loop through all criteria for this field
                    ReportCriterionData criterion =
                        (ReportCriterionData) criteriaIt.next();
                    Criterion stmtCriterion = new Criterion();
                    stmtCriterion.setField(
                        fieldData.getTableName()
                            + "."
                            + fieldData.getColumnName());

                    if (fieldData.getDisplayType()
                        == ReportField.DISPLAY_TYPE_STRING
                        && criterion.getValue1() != null && criterion.getValue1().endsWith("%"))
                        stmtCriterion.setOperator("LIKE");
                    else
                        stmtCriterion.setOperator(
                            BRUtil.getOperatorDBString(
                                criterion.getOperator()));

                    if (criterion.isCodeField())
                    {
                        if (criterion.getCodePK() == null)
                            break nextField;
                        stmtCriterion.setType(Types.VARCHAR);
                        Map codes =
                            codeBean.getCodes(fieldData.getCommonCodeTypeKey());
                        // all codes for this type
                        stmtCriterion.addValue(
                            ((CodeData) codes.get(criterion.getCodePK()))
                                .getDescription());
                        while (criteriaIt.hasNext())
                        {
                            criterion = (ReportCriterionData) criteriaIt.next();
                            stmtCriterion.addValue(
                                ((CodeData) codes.get(criterion.getCodePK()))
                                    .getDescription());
                        }

                        builder.addCriterion(stmtCriterion);
                        break nextField;
                    }
                    else
                    {
                        Object val1 = criterion.getValue1();
                        Object val2 = criterion.getValue2();

                        //set the type
                        if (fieldData.getDisplayType()
                            == ReportField.DISPLAY_TYPE_STRING)
                            stmtCriterion.setType(Types.VARCHAR);
                        if (fieldData.getDisplayType()
                            == ReportField.DISPLAY_TYPE_INTEGER)
                            stmtCriterion.setType(Types.INTEGER);
                        if (fieldData.getDisplayType()
                            == ReportField.DISPLAY_TYPE_DATE)
                            stmtCriterion.setType(Types.VARCHAR);
                        if (fieldData.getDisplayType()
                            == ReportField.DISPLAY_TYPE_BOOLEAN)
                            stmtCriterion.setType(Types.VARCHAR);

                        //add the values to the criterion
                        stmtCriterion.addValue(criterion.getValue1());
                        if (criterion.getOperator().equals("BT"))
                            stmtCriterion.addValue(criterion.getValue2());

                        //add the criterion
                        builder.addCriterion(stmtCriterion);
                    }
                }
            }
        }
    }

    /**
     * Filters out affiliates not accessible by the user
     */
    private void addAffiliateFilter(PreparedStatementBuilder builder)
    {

        if (accessibleAffs == null)
            return;

        Criterion crit = new Criterion();
        crit.setField(BRUtil.AFFILIATE_VIEW + ".aff_pk");
        crit.setOperator("IN");
        crit.setType(Types.INTEGER);
        crit.setValues(new LinkedList(accessibleAffs));
        builder.addCriterion(crit);
    }

    /**
     * Adds all the order by's to 'builder'
     */
    private void addOrderBys(PreparedStatementBuilder builder)
    {

        ReportData reportData = report.getReportData();
        Map sortFields = report.getSortFields();

        if (!reportData.isCountReport()
            && sortFields != null
            && !sortFields.isEmpty())
        {
            List sortedSortFields = new ArrayList();
            sortedSortFields.addAll(sortFields.values());
            Collections.sort(sortedSortFields); // sort the list

            ReportSortFieldData sortField;
            Iterator it = sortedSortFields.iterator();
            while (it.hasNext())
            {
                sortField = (ReportSortFieldData) it.next();
                ReportField fieldData = lookupFieldData(sortField.getFieldPK());
                String dirStr =
                    sortField.getFieldSortDirection().equals(
                        ReportSortFieldData.ASCEND)
                        ? "ASC"
                        : "DESC";
                builder.addOrderBy(
                    fieldData.getTableName()
                        + "."
                        + fieldData.getColumnName()
                        + " "
                        + dirStr);
            }
        }
    }

    /**
     * Gets the SELECT clause (everything up to the FROM)
     */
    private String getSelectClause()
    {

        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");

        buf.append("SELECT");

        ReportData reportData = report.getReportData();
        if (reportData.isCountReport())
        {
            buf.append("COUNT(*)");
        }
        else if (!getOutputFields().isEmpty())
        {
            if (isOneToManyRelationship())
                buf.append("DISTINCT");

            if (maxRows != -1)
                buf.append("TOP " + maxRows);

            buf.append(getColumnsClause());
        }

        return buf.toString();
    }

    private boolean isOneToManyRelationship()
    {
        Set outputTables =
            new TreeSet(CollectionUtil.orderAs(BRUtil.VIEW_NAMES));
        addTables(report.getOutputFields(), outputTables);

        Set criteriaTables =
            new TreeSet(CollectionUtil.orderAs(BRUtil.VIEW_NAMES));
        addTables(report.getCriteriaFields(), criteriaTables);

        for (int i = 0, max = BRUtil.ONE_TO_MANY_VIEWS.length; i < max; i++)
        {
            if (criteriaTables.contains(BRUtil.ONE_TO_MANY_VIEWS[i])
                && !outputTables.contains(BRUtil.ONE_TO_MANY_VIEWS[i]))
                return true;
        }

        return false;
    }

    private void updateCorrespondenceHistory() throws Exception
    {
        Connection con = null;
        PreparedStatement ps = null;

        try
        {
            con = DBUtil.getConnection();
            ps = createUpdateCorrespondenceStatement(con);
            ps.executeUpdate();
        }
        finally
        {
            DBUtil.cleanup(con, ps, null);
        }
    }

    /**
     * Gets the FROM clause
     */
    private String getFromClause()
    {

        Set tableNames = getJoinTableNames();

        if (tableNames.isEmpty())
            return "";

        DelimitedStringBuffer buf = new DelimitedStringBuffer(" ");

        Iterator it = tableNames.iterator();
        String drivingTable = (String) it.next();
        buf.append("FROM").append(drivingTable);

        while (it.hasNext())
        {
            String table = (String) it.next();
            buf.append(join(drivingTable, table));
        }

        return buf.toString();
    }

    /**
     * Returns the name to give to the file in the email attachment.  Iff null, the report name is used.
     */
    public String getFileName()
    {
        return null;
    }

    private Map getOutputFields()
    {
        Map retVal = null;
        if (!report.getReportData().isMailingList())
            retVal = report.getOutputFields();
        else
        {
            retVal = new HashMap();

            boolean includePerson = true;
            String personTable = BRUtil.PERSON_VIEW;
            String[] personColumns =
                new String[] {
                    "person_pk",
                    "full_name",
                    "prefix_nm",
                    "first_nm",
                    "middle_nm",
                    "last_nm",
                    "suffix_nm",
                    "alternate_mailing_nm" };

            String mainTable = null;
            String[] mainColumns = null;

            String infoTable = null;
            String[] infoColumns = null;

            String addressTable = getDrivingAddressTable();
            String[] addressColumns = ADDRESS_COLUMNS;

            if (addressTable.equals(BRUtil.PERSON_ADDRESS_VIEW))
            {
            }
            else if (addressTable.equals(BRUtil.AFFILIATE_ADDRESS_VIEW))
            {
                includePerson = false;
                mainTable = BRUtil.AFFILIATE_VIEW;

                mainColumns =
                    new String[] {
                        "aff_abbreviated_nm",
                        "affiliate_id",
                        "aff_type",
                        "aff_localSubChapter",
                        "aff_stateNat_type",
                        "aff_stateNat_type_cd",
                        "aff_subUnit" };
                addressTable = BRUtil.AFFILIATE_ADDRESS_VIEW;
            }
            else if (addressTable.equals(BRUtil.OFFICER_VIEW))
            {
                mainTable = BRUtil.AFFILIATE_VIEW;

                mainColumns =
                    new String[] {
                        "affiliate_id",
                        "aff_type",
                        "aff_localSubChapter",
                        "aff_stateNat_type",
                        "aff_stateNat_type_cd",
                        "aff_subUnit" };

                infoTable = BRUtil.OFFICER_VIEW;
                infoColumns = new String[] { "title" };

                addressTable = BRUtil.OFFICER_VIEW;
            }
            else
                throw new RuntimeException("Unable to determine a driving address table.  check the getDrivingAddressTable() method");
            int startPos = 1;

            if (includePerson)
                startPos
                    += addOutputFields(
                        personTable,
                        personColumns,
                        startPos,
                        retVal);
            if (mainTable != null)
                startPos
                    += addOutputFields(mainTable, mainColumns, startPos, retVal);
            if (infoTable != null)
                startPos
                    += addOutputFields(infoTable, infoColumns, startPos, retVal);
            if (addressTable != null)
                startPos
                    += addOutputFields(
                        addressTable,
                        addressColumns,
                        startPos,
                        retVal);
        }

        if (isOneToManyRelationship())
        {
            log.debug("getOutputFields() ONE-TO-MANY");
            List sortedOrderByFields =
                new ArrayList(report.getSortFields().values());
            Collections.sort(sortedOrderByFields);
            short position = (short) (retVal.size() + 1);

            for (Iterator i = sortedOrderByFields.iterator(); i.hasNext();)
            {
                ReportSortFieldData sortField = (ReportSortFieldData) i.next();
                if (!retVal.containsKey(sortField.getFieldPK()))
                {
                    log.debug("getOutputFields() ONE-TO-MANY Adding Column");
                    ReportOutputFieldData outputField =
                        new ReportOutputFieldData();
                    outputField.setFieldPK(sortField.getFieldPK());
                    outputField.setOutputOrder(position);
                    retVal.put(outputField.getFieldPK(), outputField);
                    position++;
                }
            }

        }
        return retVal;

    }

    /**
     * 
     * @param tableName
     * @param columnNames
     * @param startIndex
     * @param fields
     * @return the number of items added to the map
     */
    private int addOutputFields(
        String tableName,
        String[] columnNames,
        int startIndex,
        Map fields)
    {
        int retVal = 0;

        for (int i = 0, max = columnNames.length; i < max; i++)
        {
            ReportOutputFieldData field =
                getReportOutputFieldData(
                    tableName,
                    columnNames[i],
                    (short) (startIndex + i));
            if (field != null)
            {
                fields.put(field.getFieldPK(), field);
                retVal++;
            }
        }

        return retVal;
    }

}
