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
import org.afscme.enterprise.affiliate.AffiliateErrorCodes;
import org.afscme.enterprise.update.ejb.FileQueue;
import org.afscme.enterprise.update.Codes.UpdateFileType;
import org.afscme.enterprise.util.JNDIUtil;
import org.apache.log4j.Logger;


/**
 * @struts:action   path="/uploadAffiliateMember"
 *					name="affiliateFileUploadForm"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Update/UploadAffiliateMemberData.jsp"
 *
 * @struts:action-forward   name="Done"  path="/Update/FileUploadConfirmation.jsp"
 */
public class UploadAffiliateMemberAction extends AFSCMEAction {
    
    private static Logger logger = Logger.getLogger(UploadAffiliateMemberAction.class);
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, 
                    HttpServletRequest request, HttpServletResponse response, 
                    UserSecurityData usd) 
    throws Exception {
        logger.debug("----------------------------------------------------");
        logger.debug("perform called.");
        
        AffiliateFileUploadForm aForm = (AffiliateFileUploadForm)form;
        return processAffiliateFile(mapping, aForm, request, response, usd, "Done", org.afscme.enterprise.update.Codes.UpdateFileType.MEMBER);
    }
    
}
