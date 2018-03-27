package org.afscme.enterprise.update.web;

import java.io.*;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.afscme.enterprise.update.ProcessUpdateMessage;

/**
 * Creation date: (11/10/2003)
 * @author: Holly Maiwald
 */
public class ApplyUpdateServlet extends HttpServlet {

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {        
    }    
    
    /**
     * Process incoming requests for information
     *
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public void processRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        
        // returning them
        try {
            // Apply updates
            ProcessUpdateMessage applyUpdate = new ProcessUpdateMessage();                
            applyUpdate.processMessage();

            // Notify callee app
            ObjectOutputStream p = new ObjectOutputStream(response.getOutputStream());
            p.writeObject("Apply Update is processed.");
            p.flush();
            p.close();
        } catch (Exception e) {
            e.printStackTrace();
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