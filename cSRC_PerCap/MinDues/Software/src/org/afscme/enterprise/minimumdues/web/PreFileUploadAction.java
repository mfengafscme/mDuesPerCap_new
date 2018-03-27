package org.afscme.enterprise.minimumdues.web;

import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.afscme.enterprise.controller.UserSecurityData;

import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.EmployerData;
import org.afscme.enterprise.affiliate.web.PreAffiliateDetailForm;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.output.*;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.upload.FormFile;


/**
 * @struts:action   path="/preFileUploadAction"
 *                  name="fileUploadActionForm"
 *                  scope="request"
 *                  validate="false"
 *
 *
 *
 * @struts:action-forward   name="uploadPage"  		path="/Minimumdues/FileUpload.jsp"
 *
 */
public final class PreFileUploadAction extends AFSCMEAction {

  public PreFileUploadAction() {
  }

  public ActionForward perform(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response,
		 		 UserSecurityData usd)
    throws Exception {
		FileUploadActionForm fulaf = (FileUploadActionForm)form;

		return mapping.findForward("uploadPage");

		/*
		if (uploadResult == 1) {
			// saved successfully and forward to search result screen

			return mapping.getInputForward();
		}
		else {
			// save failed and foward to the same screen
			formErrors = new ActionErrors();

			formErrors.add("preaffiliateapprove", new ActionError("error.preemployersave.nosave"));
			saveErrors(request, formErrors);

			return mapping.getInputForward();
		}

        request.setAttribute("uploadForm", fulaf);
		*/
    }

}
