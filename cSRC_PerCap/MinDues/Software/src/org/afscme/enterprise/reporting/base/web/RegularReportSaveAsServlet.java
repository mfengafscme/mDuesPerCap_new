package org.afscme.enterprise.reporting.base.web;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.afscme.enterprise.util.*;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.reporting.base.ejb.ReportAccess;
import org.afscme.enterprise.reporting.base.access.Report;
import org.afscme.enterprise.reporting.base.generator.ReportGenerator;

/**
 * Handles the report save as option. HLM Fix defect #755
**/
public class RegularReportSaveAsServlet extends HttpServlet { 
    
    private static Logger log = Logger.getLogger(RegularReportSaveAsServlet.class);    
    private static final String REPORT_FORM = "regularReportForm";
    private static final String SESSION_USER_SECURITY_DATA = "SESSION_USER_SECURITY_DATA";
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportAccess reportAccess;                
        RegularReportForm rForm = (RegularReportForm)request.getSession().getAttribute(REPORT_FORM);
        UserSecurityData usd = (UserSecurityData)request.getSession().getAttribute(SESSION_USER_SECURITY_DATA);
        if (rForm == null || usd == null) {
            log.debug("SaveAsGenerationServlet: Either RegularReportForm or UserSecurityData is not in the session.");
            throw new ServletException("SaveAsGenerationServlet: Could not find report to do save as.");
        }
                
        try {
            reportAccess = JNDIUtil.getReportAccessHome().create();
        } catch (NamingException e) {
            log.debug("Unable to find ReportAccess EJB in SaveAsGenerationServlet");
            throw new ServletException("Unable to find dependent EJBs in SaveAsGenerationServlet", e);
        } catch (CreateException e) {
            log.debug("Unable to find ReportAccess EJB in SaveAsGenerationServlet");
            throw new ServletException("Unable to find dependent EJBs in ASaveAsGenerationServlet", e);
        }
        
        // get the reportPK
        try {
            Report report = reportAccess.getReport(rForm.getReportPk());        
            response.setContentType("text/data");
            response.setHeader("Content-Disposition", "attachment; filename=" + report.getReportData().getName() + ".txt");
            ReportGenerator reportGenerator = new ReportGenerator(reportAccess.getReportFields(), report, rForm.getOutputFormatObject(), usd.getAccessibleAffiliates());
            reportGenerator.generate(response.getOutputStream());
        } catch (Exception e) {
            log.debug("SaveAsGenerationServlet: Unable to generate report. "+e.getMessage());            
            throw new ServletException("Unable to generate report in SaveAsGenerationServlet.", e);
        }
    }     
    
   /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }    
}
