package org.afscme.enterprise.affiliate.web;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;
import org.apache.log4j.Logger;

public class ViewConstitutionDocumentServlet extends HttpServlet {
    
    private static Logger logger =  Logger.getLogger(ViewConstitutionDocumentServlet.class);     
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        logger.debug(toString(request));
        
        HttpSession session = request.getSession(true);
        Integer affPk = (Integer)session.getAttribute("SESSION_CURRENT_AFF_PK");
        if (affPk == null) {
            throw new ServletException("No current Affiliate is defined for which to retrieve a Constitution Document.");
        }
        String sql = 
            "SELECT const_doc_file_content_type, const_doc_file_nm, " +
            "       aff_constitution_doc " +
            "FROM Aff_Constitution " + 
            "WHERE aff_pk=?"
        ;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream in = null;
        response.reset();
        ServletOutputStream out = response.getOutputStream();
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, affPk.intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("const_doc_file_content_type");
                String name = rs.getString("const_doc_file_nm");
                in = rs.getBinaryStream("aff_constitution_doc");
                
                response.setContentType(type);
                response.setHeader("Content-Disposition", "inline; filename=" + name);
                int bytesread = 0;
                byte[] buffer = new byte[8192];
                bytesread = in.read(buffer, 0, 8192);
                logger.debug("Read " + bytesread + " bytes.");
                while (bytesread != -1) {
                    out.write(buffer, 0, bytesread);
                    out.flush();
                    bytesread = in.read(buffer, 0, 8192);
                }
                logger.debug("Done writing to file.");
            } else {
                throw new ServletException("No document found.");
            }
        } catch (SQLException se) {
            logger.debug(se.getMessage());
            throw new ServletException(se);
        } finally {
            try { out.close(); logger.debug("out closed."); } catch (Exception e1) {}
            try { in.close(); logger.debug("in closed."); } catch (Exception e2) {}
            DBUtil.cleanup(con, ps, rs);
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
    /** Returns a string representation of HttpServletRequest */
    protected static String toString(HttpServletRequest request) {
        StringBuffer buf = new StringBuffer();
        buf.append(request.toString());
        buf.append("\nParameters:\n");
        Enumeration enum = request.getParameterNames();
        while (enum.hasMoreElements()) {
            String key = (String)enum.nextElement();
            String value = TextUtil.toString(request.getParameter(key));
            
            buf.append("\t" + key + "=" + value + "\n");
        }
        buf.append("Attributes:\n");
        enum = request.getAttributeNames();
        while (enum.hasMoreElements()) {
            String key = (String)enum.nextElement();
            String value = TextUtil.toString(request.getAttribute(key));
            
            buf.append("\t" + key + "=" + value + "\n");
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            buf.append("Session:\n");
            enum = session.getAttributeNames();
            while (enum.hasMoreElements()) {
                String key = (String)enum.nextElement();
                String value = TextUtil.toString(session.getAttribute(key));
                buf.append("\t" + key + "=" + value + "\n");
            }
        }
        return buf.toString().trim();
    }
    
}
