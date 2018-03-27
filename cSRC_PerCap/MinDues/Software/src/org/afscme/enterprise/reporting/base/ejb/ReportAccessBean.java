package org.afscme.enterprise.reporting.base.ejb;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.Collections;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.reporting.base.BRUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.reporting.base.access.ReportField;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.access.ReportData;
import org.afscme.enterprise.reporting.base.access.ReportOutputFieldData;
import org.afscme.enterprise.reporting.base.access.ReportSortFieldData;
import org.afscme.enterprise.reporting.base.access.ReportCriterionData;
import org.afscme.enterprise.util.DelimitedStringBuffer;


/**
 * This session bean retrieves and stores data related to base reporting from and into database.
 * @ejb:bean name="ReportAccess" display-name="ReportAccess"
 * jndi-name="ReportAccess"
 * type="Stateless" view-type="local"
 */

public class ReportAccessBean extends SessionBase {
    
    /* --------  SQL statements used ----------------------------------- */
    
    /* select all report fields and their parent fields if any */
    private static final String SQL_SELECT_FIELDS = "SELECT * FROM Report_Fields LEFT JOIN Report_Field_Aggregate ON child_field_pk = report_field_pk";
    
    private static final String SQL_SELECT_ALL_REPORTS = "SELECT * FROM Report WHERE report_status = 1";
    
    private static final String SQL_SELECT_REPORT_NAMES_FOR_USER = "SELECT report_nm FROM Report WHERE report_custom_fg = 1 AND report_owner_pk = ?  AND report_status = 1";
    
    private static final String SQL_SELECT_ALL_MAILING_REPORTS = "SELECT * FROM Report WHERE report_mailing_list_fg = 1  AND report_status = 1";
   
    private static final String SQL_SELECT_ALL_REGULAR_REPORTS = "SELECT * FROM Report WHERE report_mailing_list_fg = 0  AND report_status = 1";
    
    private static final String SQL_SELECT_ALL_SPECIAL_REGULAR_REPORTS = "SELECT * FROM Report WHERE report_custom_fg = 0 AND report_mailing_list_fg = 0  AND report_status = 1";
    
    private static final String SQL_SELECT_ALL_SPECIAL_MAILING_REPORTS = "SELECT * FROM Report WHERE report_custom_fg = 0 AND report_mailing_list_fg = 1 AND report_status = 1";
    
    private static final String SQL_SELECT_QUERIES_FOR_USER = "SELECT * FROM Report WHERE report_custom_fg = 1 AND report_owner_pk = ? AND report_status = 1"; 
    
    private static final String SQL_SELECT_REPORTS_FOR_USER = "SELECT * FROM Report WHERE report_custom_fg = 1 AND report_owner_pk = ? AND report_mailing_list_fg = ? AND report_status = 1";
    
    private static final String SQL_SELECT_A_REPORT_INFO = "SELECT * FROM Report WHERE report_pk = ?";

    private static final String SQL_SELECT_REPORT_OUTPUT_FIELDS = "SELECT * FROM Report_Output_Fields WHERE report_pk = ? ORDER BY field_output_order";
    
    private static final String SQL_SELECT_REPORT_SORT_FIELDS = "SELECT * FROM Report_Sort_Fields WHERE report_pk = ? ORDER BY field_sort_order";
    
    private static final String SQL_SELECT_REPORT_CRITERIA_FIELDS = "SELECT t1.*, t2.com_cd_pk FROM Report_Selection_Criteria AS t1 LEFT JOIN Criteria_Codevalue AS t2 " +
        "ON t1.report_pk=t2.report_pk AND t1.report_field_pk=t2.report_field_pk AND t1.report_criterion_sequence_pk=t2.report_criterion_sequence_pk " +
        "WHERE t1.report_pk = ? ORDER BY t1.report_field_pk, t1.report_criterion_sequence_pk"; 

    private static final String SQL_INSERT_REPORT_INFO =
        " INSERT INTO Report (report_nm, report_desc, report_pend_entity_fg, report_mailing_list_fg, report_custom_fg, report_update_corresp_fg, report_handler_class, report_is_count_fg, report_last_update_dt, report_last_update_uid, report_owner_pk, report_category) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Query') ";

    private static final String SQL_INSERT_REPORT_OUTPUT_FIELDS =
        " INSERT INTO Report_Output_Fields " +
        " (report_pk, report_field_pk, field_output_order) " +
        " VALUES (?, ?, ?)";
   
    private static final String SQL_INSERT_REPORT_SORT_FIELDS =
        " INSERT INTO Report_Sort_Fields " +
        " (report_field_pk, report_pk, field_sort_order, field_sort_direction) " +
        " VALUES (?, ?, ?, ?) ";
    
    private static final String SQL_INSERT_REPORT_CRITERIA_FIELDS =
        " INSERT INTO Report_Selection_Criteria " +
        " (criterion_editable_fg, criterion_value, criterion_operator, criterion_value2, report_pk, report_field_pk, report_criterion_sequence_pk) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?) ";

    private static final String SQL_INSERT_REPORT_CRITERIA_CODEVALUE =
        " INSERT INTO Criteria_Codevalue " +
        " (com_cd_pk, report_pk, report_field_pk, report_criterion_sequence_pk) " +
        " VALUES (?, ?, ?, ?) ";
    
    // assume cascade deletion triggers are available in the database -- deleting the report will delete all rows related to this report in other tables.
    private static final String SQL_DELETE_REPORT_INFO = "UPDATE Report SET report_status = 0 WHERE report_pk = ?";
    
    private static final String SQL_DELETE_REPORT_OUTPUT_FIELDS = "DELETE FROM Report_Output_Fields WHERE report_pk = ?";
    
    private static final String SQL_DELETE_REPORT_SORT_FIELDS = "DELETE FROM Report_Sort_Fields WHERE report_pk = ?";
    
    private static final String SQL_DELETE_REPORT_CRITERIA_FIELDS = "DELETE FROM Report_Selection_Criteria WHERE report_pk = ?";
    
    private static final String SQL_DELETE_REPORT_CRITERIA_CODEVALUE = "DELETE FROM Criteria_Codevalue WHERE report_pk = ?";
    
    // we only update the "ReportData" table when updating the entire report.  Data in other tables will be deleted first, then new ones will be inserted.
    private static final String SQL_UPDATE_REPORT_INFO = "UPDATE Report SET " +
        " report_nm=?, report_desc=?, report_pend_entity_fg=?, report_mailing_list_fg=?, report_custom_fg=?, report_update_corresp_fg=?, report_handler_class=?, report_is_count_fg=?, report_last_update_dt=?, report_last_update_uid=?, report_owner_pk=?" +
        " WHERE report_pk = ?";
    
    
    /* --------  Loaded data by this bean at creation ------------------ */
    protected Map reportFields = null;  // the most static content within the Base Reporting module

    
    /* --------  Bean Creation method ---------------------------------- */
    /** Loads all non-changeable data into local member variables */
    public void ejbCreate() throws CreateException {
        loadReportFields();
    }
    
    /* ---------  Shared static information for all reports ------------ */
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public Map getReportFields() {
       return Collections.unmodifiableMap(reportFields);
    }
    
    
    /* ---------  Get methods on multiple reports  --------------------- */
    
    /**
     * A shallow get method getting a list of "ReportData" object of all reports.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getAllReports() {
        List reports = null;
        ReportData reportData = null;
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL_REPORTS);
            
            // make sure the "reports" list has items in it when it is not "null"
            // Note:  "isBeforeFirst" is NOT supported at Mecromedia JDBC driver
            //if (rs.isBeforeFirst()) reports = new ArrayList();
            reports = new ArrayList();

            while (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
                
                reports.add(reportData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        return reports;
    }
    
    /**
     * Retrieves a list of names for all reports.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getAllReportNames() {
        List reportNames = null;
        String reportName = null;
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL_REPORTS);
            
            // make sure the "reports" list has items in it when it is not "null"
            // Note:  "isBeforeFirst" is NOT supported at Mecromedia JDBC driver
            //if (rs.isBeforeFirst()) reports = new ArrayList();
            reportNames = new ArrayList();

            while (rs.next()) {
                reportName = rs.getString("report_nm");
                
                reportNames.add(reportName);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        return reportNames;
    }
    
    /** Get report names owned by a particular user
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getReportNamesForUser(Integer ownerPk) {
        List reportNames = null;
        String reportName = null;
        
        Connection con = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            preStmt = con.prepareStatement(SQL_SELECT_REPORT_NAMES_FOR_USER);
            preStmt.setInt(1, ownerPk.intValue());
            rs = preStmt.executeQuery();
            
            reportNames = new ArrayList();
            while (rs.next()) {
                reportName = rs.getString(1);
                
                reportNames.add(reportName);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, preStmt, rs);
        }
        
        return reportNames;        
    }
   
    
    /**
     * Retrieves a list of "ReportData" objects that are all mailing list reports.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getAllMailingReports() {
        List reports = null;
        ReportData reportData = null;
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL_MAILING_REPORTS);
            
            // make sure the "reports" list has items in it when it is not "null"
            // Note:  "isBeforeFirst" is NOT supported at Mecromedia JDBC driver
            //if (rs.isBeforeFirst()) reports = new ArrayList();
            reports = new ArrayList();

            while (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
                
                reports.add(reportData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        return reports;
    }
    
    /**
     * Retrieves a list of "ReportData" objects that are all regular reports (not mailing list)
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getAllRegularReports() {
        List reports = null;
        ReportData reportData = null;
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL_REGULAR_REPORTS);
            
            // make sure the "reports" list has items in it when it is not "null"
            // Note:  "isBeforeFirst" is NOT supported at Mecromedia JDBC driver
            //if (rs.isBeforeFirst()) reports = new ArrayList();
            reports = new ArrayList();

            while (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
                
                reports.add(reportData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        return reports;
    }
    
    /**
     * Retrieves a list of "ReportData" objects that are custom queries accessible by the specified user.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */    
    public List getQueriesForUser(Integer userPK) {
        List queries = null;
        ReportData reportData = null;
        
        Connection con = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();
            preStmt = con.prepareStatement(SQL_SELECT_QUERIES_FOR_USER);
            preStmt.setInt(1, userPK.intValue());
            
            rs = preStmt.executeQuery();
      
            queries = new ArrayList();
            while (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
                
                queries.add(reportData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, preStmt, rs);
        }
        
        return queries;
    }
    
    /** Retrieve a list of all specialized reports, either regular or mailing list
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getSpecializedReports(boolean mailingList) {
        List reports = null;
        ReportData reportData = null;
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            if (!mailingList)
                rs = stmt.executeQuery(SQL_SELECT_ALL_SPECIAL_REGULAR_REPORTS);
            else
                rs = stmt.executeQuery(SQL_SELECT_ALL_SPECIAL_MAILING_REPORTS);
            
            reports = new ArrayList();
            while (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
                
                reports.add(reportData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
        
        return reports;                
    }
    
    /** Retrieve a list of user owned reports, either regular or mailing list
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getReportsForUser(boolean mailingList, Integer personPk) {
        List reports = null;
        ReportData reportData = null;
        
        Connection con = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.getConnection();

            preStmt = con.prepareStatement(SQL_SELECT_REPORTS_FOR_USER);
            
            preStmt.setInt(1, personPk.intValue());
            preStmt.setBoolean(2, mailingList);
            rs = preStmt.executeQuery();
            
            reports = new ArrayList();
            while (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
                
                reports.add(reportData);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        finally {
            DBUtil.cleanup(con, preStmt, rs);
        }
        
        return reports;                
    }    
    
    
    /* ---------  Get methods on multiple reports for a single entity -- */
    
    /**
     * Retrieves all pending reports for an entity (no ACL applied yet).
     * A subset of this list (after ACL) will form the top table of "Maintain Pending Report" screen.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPendingReportsForEntity(Integer entityPK, Integer userPK) {
        return null;
    }
    
    /**
     * Retrieves all pend-available reports for an entity (no ACL applied yet).
     * A subset of this list (after ACL) will form the bottom table of "Maintain Pending Report" screen.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public List getPendAvailableReportsForEntity(Integer entityPK, Integer userPK) {
        return null;
    }
    
    
    /* ---------  Get methods on single report ------------------------- */ 
    
    /**
     * A shallow get method getting the "ReportData" object of a report.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public ReportData getReportData(Integer reportPK) throws FinderException {
        
        Connection con = DBUtil.getConnection();
        
        ReportData reportData = loadReportData(con, reportPK);

        DBUtil.cleanup(con, null, null);
        
        if (reportData == null) throw new FinderException("Report " + reportPK.toString() + " not found.");
        
        return reportData;
    }
    
    /**
     * A deep get method for getting everything about a report.
     * @ejb:interface-method view-type="local"
     * @ejb:tansaction type="Supports"
     */
    public Report getReport(Integer reportPK) throws FinderException {
        Report report = null;
        
        // let's share a database connection
        Connection con = DBUtil.getConnection();
        
        // get the reportData object.
        ReportData reportData = loadReportData(con, reportPK);
        if (reportData == null) throw new FinderException("Report " + reportPK.toString() + " not found.");
            
        // get report's output fields
        Map outputFields = loadReportOutputFields(con, reportPK);
        
        // get report's sort fields
        Map sortFields = loadReportSortFields(con, reportPK);
        
        // get report's selection criteria fields
        Map criteriaFields = loadReportCriteriaFields(con, reportPK);
        
        report = new Report();
        report.setReportPK(reportPK);
        report.setReportData(reportData);
        report.setOutputFields(outputFields);
        report.setSortFields(sortFields);
        report.setCriteriaFields(criteriaFields);
        
        DBUtil.cleanup(con, null, null);
        
        return report;
            
    }
    
    
    /**
     * Create a new report.
     * @ejb:interface-method view-type="local"
     * @ejb:tansaction type="Supports"
     */    
    public ReportData createReport(Report report) {
        Connection con = DBUtil.getConnection();
        
        ReportData reportData = insertReportData(con, report.getReportData());
        Integer reportPK = reportData.getPk();
        
        if (!report.getReportData().isCountReport() &&
            report.getOutputFields().size() == 0) {
            throw new EJBException("Cannot create report with no output fields");
        }
        
        insertReportOutputFields(con, reportPK, report.getOutputFields());
        
        insertReportSortFields(con, reportPK, report.getSortFields());
        
        // this insert data in Report_Selection_Criteria and Criteria_Codevalue tables.
        insertReportCriteriaFields(con, reportPK, report.getCriteriaFields());
        
        DBUtil.cleanup(con, null, null);
        
        return reportData;
    }

    /**
     * Delete a report.
     * @ejb:interface-method view-type="local"
     * @ejb:tansaction type="Supports"
     */     
    public void deleteReport(Integer reportPK) {
        Connection con = DBUtil.getConnection();
        
        deleteReportData(con, SQL_DELETE_REPORT_INFO, reportPK);
        
        deleteReportData(con, SQL_DELETE_REPORT_OUTPUT_FIELDS, reportPK);
        deleteReportData(con, SQL_DELETE_REPORT_SORT_FIELDS, reportPK);
        deleteReportData(con, SQL_DELETE_REPORT_CRITERIA_CODEVALUE, reportPK);        
        deleteReportData(con, SQL_DELETE_REPORT_CRITERIA_FIELDS, reportPK);
        
        
        DBUtil.cleanup(con, null, null);
    }

    /**
     * Update a report.
     * @ejb:interface-method view-type="local"
     * @ejb:tansaction type="Supports"
     */     
    public ReportData updateReport(Report updatedReport) {
        ReportData reportData = updatedReport.getReportData();
        Integer reportPK = updatedReport.getReportPK();
        
        Connection con = DBUtil.getConnection();
        
        // update the ReportData table
        updateReportData(con, reportData);
        
        // delete output, sort, criteria data
        deleteReportData(con, SQL_DELETE_REPORT_OUTPUT_FIELDS, reportPK);
        deleteReportData(con, SQL_DELETE_REPORT_SORT_FIELDS, reportPK);       
        deleteReportData(con, SQL_DELETE_REPORT_CRITERIA_CODEVALUE, reportPK);
        deleteReportData(con, SQL_DELETE_REPORT_CRITERIA_FIELDS, reportPK);        
        
        // insert updated output, sort, and criteria data
        insertReportOutputFields(con, reportPK, updatedReport.getOutputFields());
        insertReportSortFields(con, reportPK, updatedReport.getSortFields());
        insertReportCriteriaFields(con, reportPK, updatedReport.getCriteriaFields());
        
        DBUtil.cleanup(con, null, null);
        
        return reportData;
    }
    
    /**
     * Retrieve the selection criteria data for a report.
     * This is used by "Generate Reports" functional specification when a report has runtime editable criteria.
     * This is useful then the GUI does not need the entire report.
     * @ejb:interface-method view-type="local"
     * @ejb:tansaction type="Supports"
     */
    public Map getReportCriteriaFields(Integer reportPK) {
        Connection con = DBUtil.getConnection();
        
        Map criteriaFields = loadReportCriteriaFields(con, reportPK);
        
        DBUtil.cleanup(con, null, null);
        
        return criteriaFields;
    }
    
    

    /* ----------  Private methods ------------------------------------- */
    
    private void loadReportFields() {
        reportFields = new TreeMap();
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ReportField rf = null;
        
        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_FIELDS);

            while (rs.next()) {
                rf = new ReportField();
                
                // set the primary key
                rf.setPk(new Integer(rs.getInt("report_field_pk")));
                
                // set the rest of fields
                rf.setPrintWidth(rs.getFloat("field_print_width"));
                rf.setEntityType(rs.getString("field_entity_type").charAt(0));
                rf.setCategoryName(rs.getString("field_category_name"));
                rf.setTableName(rs.getString("field_table_nm"));
                rf.setColumnName(rs.getString("field_column_name"));
                rf.setDisplayName(rs.getString("field_display_name"));
                rf.setDisplayType(rs.getString("field_display_type").charAt(0));
                rf.setCommonCodeTypeKey(rs.getString("com_cd_type_key"));

                ReportField parent = null;
                int parentPk = rs.getInt("parent_field_pk");
                if (parentPk != 0) {
                    rf.setParentPK(new Integer(parentPk));
                    parent = (ReportField)reportFields.get(rf.getParentPK());
                }
               
                String fullName = 
                    new DelimitedStringBuffer(" ")
                    .append(BRUtil.getEntityName(rf.getEntityType()))
                    .append(rf.getCategoryName().equals("Detail") ? null : rf.getCategoryName())
//                    .append(parent == null ? null : parent.getDisplayName())
                    .append(rf.getDisplayName())
                    .toString();
                    
                rf.setFullName(fullName);
                    
                // insert the data into the map
                reportFields.put(rf.getPk(), rf);
            }
        } catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(con, stmt, rs);
        }
            
        // re-arrange the "reportFileds" map so that children are under parents
        // The process makes an heriartical structure in the "reportFileds" map instead of the orginal linear one. 
        Integer currPK = null;
        ReportField currReportField = null;        
        
        Integer parentPK = null;
        ReportField parent = null;
        Set children = null;

        Map.Entry currEntry;      
        Iterator it = reportFields.entrySet().iterator();
        while (it.hasNext()) {
            currEntry = (Map.Entry)it.next();
            currPK = (Integer)currEntry.getKey();
            currReportField = (ReportField)currEntry.getValue();
            
            parentPK = currReportField.getParentPK();  
            if (parentPK != null) {     // if the field has parent, let put this child under its parent's children set.
                parent = ((ReportField)reportFields.get(parentPK));
                children = parent.getChildren();
                if (children == null) {
                    children = new TreeSet(new FieldOrder());
                    parent.setChildren(children);
                }
                children.add(currReportField);   // put this child in its parent's children set.
                it.remove();            // remove this child from the top level. Note this is the only time we are modifying the map.
            }
        }
    }
    
    private ReportData loadReportData(Connection con, Integer reportPK) {
        ReportData reportData = null;
        
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            preStmt = con.prepareStatement(SQL_SELECT_A_REPORT_INFO);
            preStmt.setInt(1, reportPK.intValue());
            rs = preStmt.executeQuery();
            
            if (rs.next()) {
                reportData = new ReportData();
                reportData.setPk(new Integer(rs.getInt("report_pk")));
                reportData.setName(rs.getString("report_nm"));
                reportData.setDescription(rs.getString("report_desc"));
                reportData.setCanAddEntities(rs.getBoolean("report_pend_entity_fg"));
                reportData.setMailingList(rs.getBoolean("report_mailing_list_fg"));
                reportData.setCustom(rs.getBoolean("report_custom_fg"));
                reportData.setNeedUpdateCorrespondence(rs.getBoolean("report_update_corresp_fg"));
                reportData.setCustomHandlerClassName(rs.getString("report_handler_class"));
                reportData.setCountReport(rs.getBoolean("report_is_count_fg"));
                reportData.setLastUpdateUID(rs.getString("report_last_update_uid"));
                reportData.setLastUpateDate(rs.getTimestamp("report_last_update_dt"));
                reportData.setOwnerPK(new Integer(rs.getInt("report_owner_pk")));
            }
        } catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, rs);
        }
        
        return reportData;
    }
    
    private Map loadReportOutputFields(Connection con, Integer reportPK) {
        Map outputFields = null;
        ReportOutputFieldData outputFieldData = null;
        
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            preStmt = con.prepareStatement(SQL_SELECT_REPORT_OUTPUT_FIELDS);
            preStmt.setInt(1, reportPK.intValue());
            rs = preStmt.executeQuery();
            
            outputFields = new TreeMap();
            Integer fieldPK = null;
            while (rs.next()) {
                fieldPK = new Integer(rs.getInt("report_field_pk"));
                outputFieldData = new ReportOutputFieldData();
                outputFieldData.setFieldPK(fieldPK);
                outputFieldData.setOutputOrder(rs.getShort("field_output_order"));
                
                outputFields.put(fieldPK, outputFieldData);
            }
            
        } catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, rs);
        }
        
        return outputFields;
    }
    
    private Map loadReportSortFields(Connection con, Integer reportPK) {
        Map sortFields = null;
        ReportSortFieldData sortFieldData = null;
        
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            preStmt = con.prepareStatement(SQL_SELECT_REPORT_SORT_FIELDS);
            preStmt.setInt(1, reportPK.intValue());
            rs = preStmt.executeQuery();
            
            sortFields = new TreeMap();
            Integer fieldPK = null;
            while (rs.next()) {
                fieldPK = new Integer(rs.getInt("report_field_pk"));
                sortFieldData = new ReportSortFieldData();
                sortFieldData.setFieldPK(fieldPK);
                sortFieldData.setFieldSortOrder(rs.getShort("field_sort_order"));
                sortFieldData.setFieldSortDirection(rs.getString("field_sort_direction"));
                
                sortFields.put(fieldPK, sortFieldData);
            }
        } catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, rs);
        }
        
        return sortFields;
    }
    
    private Map loadReportCriteriaFields(Connection con, Integer reportPK) {
        Map criteriaFields = null;  // each field maps to a list of "ReportCriterionData".
        List currFieldCriteria = null; // stores a list of "ReportCriterionData" objects for a single field.
        ReportCriterionData criterionData = null;
        
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            preStmt = con.prepareStatement(SQL_SELECT_REPORT_CRITERIA_FIELDS);
            preStmt.setInt(1, reportPK.intValue());
            rs = preStmt.executeQuery();
            
            criteriaFields = new TreeMap();
            Integer fieldPK = null;
            String opStr = null;
            while (rs.next()) {
                fieldPK = new Integer(rs.getInt("report_field_pk"));
                criterionData = new ReportCriterionData();
                criterionData.setFieldPK(fieldPK);
                criterionData.setCriterionSequence(rs.getInt("report_criterion_sequence_pk"));
                criterionData.setEditable(rs.getBoolean("criterion_editable_fg"));
                criterionData.setOperator(rs.getString("criterion_operator"));
                criterionData.setValue1(rs.getString("criterion_value"));
                criterionData.setValue2(rs.getString("criterion_value2"));
                
                //HLM: Fix defect #190
                if (criterionData.getOperator().equals("IN")) {
                    criterionData.setCodePK(new Integer(rs.getInt("com_cd_pk")));
                    criterionData.setCodeField(true);
                }
                /////////////////////////////
                
                if (criteriaFields.containsKey(fieldPK)) {
                    currFieldCriteria = (List)criteriaFields.get(fieldPK);
                    currFieldCriteria.add(criterionData);
                } else {
                    currFieldCriteria = new ArrayList();
                    currFieldCriteria.add(criterionData);
                    criteriaFields.put(fieldPK, currFieldCriteria);
                }
            }
        } catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, rs);
        }
        
        return criteriaFields;
    }
          
    private ReportData insertReportData(Connection con, ReportData reportData) {
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(SQL_INSERT_REPORT_INFO);
            preStmt.setString(1, reportData.getName());
            DBUtil.setNullableVarchar(preStmt, 2, reportData.getDescription());
            preStmt.setBoolean(3, reportData.getCanAddEntities());
            preStmt.setBoolean(4, reportData.isMailingList());
            preStmt.setBoolean(5, reportData.isCustom());
            preStmt.setBoolean(6, reportData.getNeedUpdateCorrespondence());
            DBUtil.setNullableVarchar(preStmt, 7, reportData.getCustomHandlerClassName());
            preStmt.setBoolean(8, reportData.isCountReport());
            DBUtil.setNullableTimestamp(preStmt, 9, reportData.getLastUpdateDate());
            DBUtil.setNullableVarchar(preStmt, 10, reportData.getLastUpdateUID());
            preStmt.setInt(11, reportData.getOwnerPK().intValue());
        
            // Note, the preStmt is cleaned by DBUtil.insertAntGetIdentity().
            reportData.setPk(DBUtil.insertAndGetIdentity(con, preStmt));
        }
        catch (SQLException exp) {
            throw new EJBException(exp);
        }
        
        return reportData;
    }
    
    private void insertReportOutputFields(Connection con, Integer reportPK, Map outputFields) {
        if ((outputFields == null) || (outputFields.isEmpty()))
            return;
        
        PreparedStatement preStmt = null;
        
        try {
            preStmt = con.prepareStatement(SQL_INSERT_REPORT_OUTPUT_FIELDS);
            
            Iterator it = outputFields.values().iterator();
            ReportOutputFieldData outputField;
            while (it.hasNext()) {
                outputField = (ReportOutputFieldData)it.next();
                preStmt.setInt(1, reportPK.intValue());
                preStmt.setInt(2, outputField.getFieldPK().intValue());
                preStmt.setShort(3, outputField.getOutputOrder());
                preStmt.addBatch();
            }
            
            preStmt.executeBatch();
        }
        catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, null);
        }
    }
    
    
    private void insertReportSortFields(Connection con, Integer reportPK, Map sortFields) {
        if ((sortFields == null) || (sortFields.isEmpty()))
            return;
        
        PreparedStatement preStmt = null;
        
        try {
            preStmt = con.prepareStatement(SQL_INSERT_REPORT_SORT_FIELDS);
            
            Iterator it = sortFields.values().iterator();
            ReportSortFieldData sortField;
            while (it.hasNext()) {
                sortField = (ReportSortFieldData)it.next();
                preStmt.setInt(2, reportPK.intValue());
                preStmt.setInt(1, sortField.getFieldPK().intValue());
                preStmt.setShort(3, sortField.getFieldSortOrder());
                DBUtil.setNullableChar(preStmt, 4, sortField.getFieldSortDirection());
                preStmt.addBatch();
            }
            
            preStmt.executeBatch();
        }
        catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, null);
        }
    }


    private void insertReportCriteriaFields(Connection con, Integer reportPK, Map criteriaFields) {
        if ((criteriaFields == null) || (criteriaFields.isEmpty()))
            return;
        
        PreparedStatement preStmt1 = null;
        PreparedStatement preStmt2 = null;
        boolean hasSecondData = false;
        
        try {
            preStmt1 = con.prepareStatement(SQL_INSERT_REPORT_CRITERIA_FIELDS);
            preStmt2 = con.prepareStatement(SQL_INSERT_REPORT_CRITERIA_CODEVALUE);
                        
            Iterator fieldsIt = criteriaFields.values().iterator();
            ListIterator criterionIt;
            List fieldCriteriaList = null;
            ReportCriterionData fieldCriterion;
            int criterionIndex = 0;
            while (fieldsIt.hasNext()) {
                fieldCriteriaList = (List)fieldsIt.next();
                
                // we use "ListIterator" so that we can get item's index. This index will be used as
                // the "report_criterion_sequence_pk".
                criterionIt = fieldCriteriaList.listIterator();
                while (criterionIt.hasNext()) {
                    fieldCriterion = (ReportCriterionData)criterionIt.next();
                    // get the right index (starting from 1)
                    criterionIndex = criterionIt.nextIndex();
                    
                    preStmt1.setInt(5, reportPK.intValue());
                    preStmt1.setInt(6, fieldCriterion.getFieldPK().intValue());
                    preStmt1.setInt(7, criterionIndex);
                    preStmt1.setBoolean(1, fieldCriterion.isEditable());
                    preStmt1.setString(3, fieldCriterion.getOperator());
                    
                    if (fieldCriterion.isCodeField()) {     // prepare for the second table if necessary
                        preStmt2.setInt(1, fieldCriterion.getCodePK().intValue());
                        preStmt2.setInt(2, reportPK.intValue());
                        preStmt2.setInt(3, fieldCriterion.getFieldPK().intValue());
                        preStmt2.setInt(4, criterionIndex);
                        preStmt2.addBatch();
                        
                        DBUtil.setNullableVarchar(preStmt1, 2, null);
                        DBUtil.setNullableVarchar(preStmt1, 4, null);
                        
                        if (!hasSecondData) hasSecondData = true;
                    }
                    else {
                        DBUtil.setNullableVarchar(preStmt1, 2, fieldCriterion.getValue1());
                        DBUtil.setNullableVarchar(preStmt1, 4, fieldCriterion.getValue2());
                    }
                    
                    preStmt1.addBatch();
                }
            }
            
            preStmt1.executeBatch();
            if (hasSecondData) preStmt2.executeBatch();
        }
        catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt1, null);
            DBUtil.cleanup(null, preStmt2, null);
        }
    } 
    
    // Since deleting report data from any table in the report data model is all the same,
    // we use one method to handle all.
    private void deleteReportData(Connection con, String sql, Integer reportPK) {
        PreparedStatement preStmt = null;
        
        try {
            preStmt = con.prepareStatement(sql);
            preStmt.setInt(1, reportPK.intValue());
            
            preStmt.executeUpdate();
        }
        catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, null);
        }        
    }
    
    private void updateReportData(Connection con, ReportData reportData) {
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        
        try {
            preStmt = con.prepareStatement(SQL_UPDATE_REPORT_INFO);
            preStmt.setString(1, reportData.getName());
            DBUtil.setNullableVarchar(preStmt, 2, reportData.getDescription());
            preStmt.setBoolean(3, reportData.getCanAddEntities());
            preStmt.setBoolean(4, reportData.isMailingList());
            preStmt.setBoolean(5, reportData.isCustom());
            preStmt.setBoolean(6, reportData.getNeedUpdateCorrespondence());
            DBUtil.setNullableVarchar(preStmt, 7, reportData.getCustomHandlerClassName());
            preStmt.setBoolean(8, reportData.isCountReport());
            DBUtil.setNullableTimestamp(preStmt, 9, reportData.getLastUpdateDate());
            DBUtil.setNullableVarchar(preStmt, 10, reportData.getLastUpdateUID());
            preStmt.setInt(11, reportData.getOwnerPK().intValue());
            preStmt.setInt(12, reportData.getPk().intValue());
        
            preStmt.executeUpdate();
        }
        catch (SQLException exp) {
            throw new EJBException(exp);
        }
        finally {
            DBUtil.cleanup(null, preStmt, null);
        }
    }    

    static class FieldOrder implements Comparator {
        public int compare(Object o1, Object o2) {
            int v1 = ((ReportField)o1).getPk().intValue();
            int v2 = ((ReportField)o2).getPk().intValue();
            return v1-v2;
        }
    }

}
