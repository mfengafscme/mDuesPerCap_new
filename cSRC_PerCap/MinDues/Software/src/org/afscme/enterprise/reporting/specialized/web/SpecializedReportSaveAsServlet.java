package org.afscme.enterprise.reporting.specialized.web;

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
import org.afscme.enterprise.reporting.specialized.EnrichedDataReport;

/**
 * Handles the specialized report save as option. HLM Fix defect #762
**/
public class SpecializedReportSaveAsServlet extends HttpServlet { 
    
    private static Logger log = Logger.getLogger(SpecializedReportSaveAsServlet.class);    
    private static final String REPORT_FORM = "specializedReportForm";
    
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
        SpecializedReportForm sForm = (SpecializedReportForm)request.getSession().getAttribute(REPORT_FORM);
        if (sForm == null) {
            log.debug("SpecializedReportSaveAsServlet: The SpecializedReportForm is not in the session.");
            throw new ServletException("SpecializedReportSaveAsServlet: Could not find report to do save as.");
        }
        
        try {
            response.setContentType("text/data");
            response.setHeader("Content-Disposition", "attachment; filename=" + sForm.getReportName() + ".txt");
        
            // This is dealing with Enrich Data report
            EnrichedDataReport enrichedDataReport = new EnrichedDataReport();
            enrichedDataReport.setAffPk(sForm.getAffPk());
            enrichedDataReport.setOutputFormat(sForm.getOutputFormat());
            enrichedDataReport.generate(response.getOutputStream());
        } catch (Exception e) {
            log.debug("SpecializedReportSaveAsServlet: Unable to generate the specialized report "+sForm.getReportName());
            throw new ServletException("SpecializedReportSaveAsServlet: Unable to generate the specialized report "+sForm.getReportName());
        }        
        
        request.getSession().removeAttribute(REPORT_FORM);                        
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
