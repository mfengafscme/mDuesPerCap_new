package org.afscme.enterprise.participationgroups.web;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import javax.servlet.*;
import javax.servlet.http.*;
import org.afscme.enterprise.participationgroups.*;
import org.afscme.enterprise.participationgroups.web.ExportParticipationCodesForm;

public class SaveAsServlet extends HttpServlet { 
    private static String DATA      = "participationMaintenance";
    private static String FILENAME  = "ParticipationGroup";
    private static String DELIMITER = "delimiter";
    
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
        
        //processNoCache(response);
        PrintWriter out = response.getWriter();
        
        // Get the selected delimiter from the request.
        String delimiter = (String)request.getSession().getAttribute(DELIMITER);
        if (delimiter == null) {
            throw new ServletException("Delimiter is not selected.");
        }
        // Get participation group data from the request.
        ParticipationMaintenance data = (ParticipationMaintenance) request.getSession().getAttribute(DATA);
        if (data == null) {
            throw new ServletException("Could not find participation group data to do save as.");
        }
                
        if (new Integer(delimiter).intValue() == ExportParticipationCodesForm.TAB) {
            delimiter = "\t";
        } else {
            delimiter = (new Integer(delimiter).intValue() == ExportParticipationCodesForm.COMMA) ? "," : ";";
        }
        
        String fileName = FILENAME + ".tab";    
        
        // Parse the participation groups data
        response.setContentType("application/text");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        out.println("Participation Group Name" +delimiter+
                    "Participation Type Name" +delimiter+
                    "Participation Detail Name" +delimiter+
                    "Shortcut"+delimiter+
                    "Participation Outcome Name");
        
        Integer groupPk = data.getGroup();
        if (groupPk != null) {
            List types = data.getGroupTypes(groupPk.intValue());
            if (types == null) {
                out.println(data.getGroupNm()+delimiter+delimiter+delimiter+delimiter);
            } else {
                Iterator itTypes = types.iterator();
                while (itTypes.hasNext()) {
                    ParticipationTypeData itemType = (ParticipationTypeData)itTypes.next();
                    List details = data.getTypeDetails(itemType.getTypePk().intValue());
                    if (details == null) {
                        out.println(data.getGroupNm()+delimiter+itemType.getName()+delimiter+delimiter+delimiter);                        
                    } else {
                        Iterator itDetails = details.iterator();
                        while (itDetails.hasNext()) {
                            ParticipationDetailData itemDetail = (ParticipationDetailData)itDetails.next();
                            List outcomes = data.getDetailOutcomes(itemDetail.getDetailPk().intValue());
                            if (outcomes == null) {
                                out.println(data.getGroupNm()+delimiter+itemType.getName()+delimiter+itemDetail.getName()+delimiter+itemDetail.getShortcut()+delimiter);                        
                            } else {
                                Iterator itOutcomes = outcomes.iterator();
                                while (itOutcomes.hasNext()) {
                                    ParticipationOutcomeData itemOutcome = (ParticipationOutcomeData)itOutcomes.next();
                                    out.println(data.getGroupNm()+delimiter+itemType.getName()+delimiter+itemDetail.getName()+delimiter+itemDetail.getShortcut()+delimiter+itemOutcome.getOutcomeNm());
                                }
                            }
                        }
                    }
                }
            }
        }
        out.flush();
        out.close();        
        
        // Remove these attributes from session.. we are done!
        request.getSession().removeAttribute(DELIMITER);
        request.getSession().removeAttribute(DATA);
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
