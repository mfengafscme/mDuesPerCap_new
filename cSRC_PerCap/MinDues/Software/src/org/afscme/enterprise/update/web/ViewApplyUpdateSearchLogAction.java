/***********************************************************************************/
/* Called by:               ApplyUpdateProcessLog.jsp
 *
 * Forwards request to:     ApplyUpdateProcessLog.jsp
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.update.Codes.UpdateFileStatus;
import org.afscme.enterprise.update.ejb.FileQueue;
import org.afscme.enterprise.update.filequeue.FileEntry;
import org.afscme.enterprise.util.JNDIUtil;

/**
 * @struts:action   path="/viewApplyUpdateSearchLog"
 *                  input="/Update/ApplyUpdateProcessLog.jsp"
 *                  name="searchLogForm"
 *                  validate="true"
 *                  scope="session"
 *                  
 *
 * @struts:action-forward   name="View"             path="/Update/ApplyUpdateProcessLog.jsp"
 */
public class ViewApplyUpdateSearchLogAction extends AFSCMEAction
{

    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response,
        UserSecurityData usd)
        throws Exception
    {

        SearchLogForm searchForm = (SearchLogForm) form;
        log.debug("form.getAffState()======>" + searchForm.getAffState());
        /****************************************************************************/
        //if this is data utility menu option then direct it to the data utility menu
        /****************************************************************************/
        String start = (String) request.getParameter("start");

        if (start == null)
        {
            start = "true";
        }
        /*****************************************************************************/
        // get the fileQueue bean
        FileQueue fileQueue = JNDIUtil.getFileQueueHome().create();
        int sort = 0;
        if (searchForm.getSortBy() != null)
        {
            sort = new Integer(searchForm.getSortBy()).intValue();
        }
        else
        {
            sort = 0;
        }

        log.debug("start=========>" + start);
        log.debug("sort=========>" + sort);

        // retreive all file queue contents into a list of FileEntry objects
        List fileQueueList;
        /**************************************************************************
         *If the user has selected an affiliate then make sure you are showing the
         *user records only for that affiliate 
         **************************************************************************/
        if (usd.isActingAsAffiliate())
        {
            /**********************************************************************
             *get the list of the affiliates
             **********************************************************************/
            Set affs = usd.getAccessibleAffiliates();
            /**********************************************************************/
            fileQueueList = fileQueue.getFileList(affs);
            /*********************************************************************/
        }
        else
        {
            /************************************************************************
             *the user has not seleted an affiliate thus donot pass the affiliate
             ************************************************************************/
            if (sort != 0)
            {

                fileQueue.setSortOrder(sort);
                log.debug("set Sort order to =================>" + sort);
                fileQueueList =
                    fileQueue.getFileList(
                        searchForm.getAffCouncil(),
                        searchForm.getAffLocal(),
                        searchForm.getAffState(),
                        searchForm.getAffSubunit(),
                        searchForm.getAffType(),
                        searchForm.getFromDate(),
                        searchForm.getToDate(),
                        searchForm.getUpdateType());

                //fileQueueList = fileQueue.getFileList();

            }
            else
            {
                fileQueueList =
                    fileQueue.getFileList(
                        searchForm.getAffCouncil(),
                        searchForm.getAffLocal(),
                        searchForm.getAffState(),
                        searchForm.getAffSubunit(),
                        searchForm.getAffType(),
                        searchForm.getFromDate(),
                        searchForm.getToDate(),
                        searchForm.getUpdateType());

                //fileQueueList = fileQueue.getFileList();
            }
            /************************************************************************/
        }
        boolean foundFile = false;
        log.debug("fileQueueList =================>" + fileQueueList);
        if ((fileQueueList != null))
        {
            for (Iterator it = fileQueueList.iterator(); it.hasNext();)
            {
                FileEntry fe = (FileEntry) it.next();
                log.debug("fe.getStatus()=======>" + fe.getStatus());
                if ((fe.getStatus() == UpdateFileStatus.PROCESSED)
                    || (fe.getStatus() == UpdateFileStatus.REJECTED))
                    foundFile = true;
                else
                    it.remove();
            }
        }
        if (!foundFile)
        {
            request.setAttribute("fileQueues", null);
            start = "true";
        }
        else
        {
            request.setAttribute("fileQueues", fileQueueList);
            start = "false";
        }
        searchForm.setResults(fileQueueList);
        searchForm.setTotal(fileQueueList.size());

        //request.setAttribute("sortForm", new SearchLogSortForm());        
        request.setAttribute("start", start);

        ActionErrors errors = new ActionErrors();
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
            //go back to the edit page
        }
        else
        {
            log.debug("------------------------------------------------------");
            log.debug("Forwarding to view page");
            log.debug("------------------------------------------------------");
            return mapping.findForward("View"); //go back to the view page
        }

    }

}
