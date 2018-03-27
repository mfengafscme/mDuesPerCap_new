package org.afscme.enterprise.update.web;

import java.util.Map;
import java.io.*;
import org.apache.struts.upload.*;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.ejb.MaintainCodes;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.update.ejb.FileQueue;
import org.afscme.enterprise.util.JNDIUtil;


/**
 * @struts:action   path="/viewUploadAffiliateOfficer"
 *                  scope="request"
 *
 * @struts:action-forward   name="View"  path="/Update/UploadAffiliateOfficerData.jsp"
 */
public class ViewUploadAffiliateOfficerAction extends AFSCMEAction {
    
    
    /** Implemented by subclasses to perform the work of handling the request
     *
     * @param mapping The struts action mapping for this action
     * @param form The struts form submitted with this action, if any
     * @param request The servlet requiest that is being processed
     * @param usd Security data for the user performing this action
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {      
        return mapping.findForward("View");
    }
    
}
