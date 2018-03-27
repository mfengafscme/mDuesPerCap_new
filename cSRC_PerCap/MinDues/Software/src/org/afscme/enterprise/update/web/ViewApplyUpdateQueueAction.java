/***********************************************************************************/
/* Called by:               MainMenu.jsp
 *
 * Forwards request to:     ApplyUpdate.jsp
 *
 * Purpose:                 This action uses the methods of FileQueue EJB to get data
 *                          from the database and display it on the screen using ApplyUpdate.jsp.
 *
 * Required:                None
 *
 *
 *
 *************************************************************************************/


package org.afscme.enterprise.update.web;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import org.afscme.enterprise.update.ejb.FileQueue;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import javax.servlet.http.*;
import javax.servlet.*;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.update.Codes.Sort;
import org.afscme.enterprise.update.web.ProcessLogForm;

/**
 * @struts:action   path="/viewApplyUpdateQueue"
 *                  scope="session"
 *
 * @struts:action-forward   name="View"             path="/Update/ApplyUpdate.jsp"
 * @struts:action-forward    name="DataUtilityMain"  path="/Common/DataUtilityMainMenu.jsp?noMain=true"
 */
public class ViewApplyUpdateQueueAction extends AFSCMEAction {
    
    private static Logger logger = Logger.getLogger(ViewApplyUpdateQueueAction.class);
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
        
        ProcessLogForm pLf  =   (ProcessLogForm) form;
        
        /****************************************************************************/
        //if this is data utility menu option then direct it to the data utility menu
        /****************************************************************************/
        String  viewType     = (String) request.getParameter("view");
        
        if(     viewType     == null && !usd.isActingAsAffiliate()){
            viewType     = "false";
        }        
        if(viewType == null && usd.isActingAsAffiliate()){
            viewType     = "true";
        }
        
        /*****************************************************************************/
        // get the fileQueue bean
        FileQueue fileQueue = JNDIUtil.getFileQueueHome().create();
        int sort             =  0;
        if(request.getParameter("sortBy") !=null){
            sort             =  new Integer((String) request.getParameter("sortBy")).intValue();
        }
        
        logger.debug("Sort=================>" + sort);
        // retreive all file queue contents into a list of FileEntry objects
        
        List fileQueueList;
        /**************************************************************************
         *If the user has selected an affiliate then make sure you are showing the
         *user records only for that affiliate
         **************************************************************************/
        if (usd.isActingAsAffiliate()) {
            /**********************************************************************
             *get the list of the affiliates
             **********************************************************************/
            Set affs = usd.getAccessibleAffiliates();
            /**********************************************************************/
            fileQueueList = fileQueue.getFileList(affs);
            /*********************************************************************/
        } else {
            /************************************************************************
             *the user has not seleted an affiliate thus donot pass the affiliate
             ************************************************************************/
            if(sort != 0){
                fileQueue.setSortOrder(sort);
                logger.debug("set Sort order to =================>" + sort);
                fileQueueList = fileQueue.getFileList();
                if(pLf != null){
                    logger.debug("plf not null =================>" + pLf );
                    pLf.setResults(fileQueueList);
                }
                
            }else{
                
                fileQueueList = fileQueue.getFileList();
                if(pLf != null){
                    logger.debug("plf not null =================>" + pLf );
                    pLf.setResults(fileQueueList);
                }
            }
            /************************************************************************/
        }
        
        request.setAttribute("fileQueues", fileQueueList);
        request.setAttribute("view", viewType);
               
        return mapping.findForward("View");
    }
    
}
