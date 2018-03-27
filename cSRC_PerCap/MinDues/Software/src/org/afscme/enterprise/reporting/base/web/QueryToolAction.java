package org.afscme.enterprise.reporting.base.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.CollectionUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @struts:action-forward   name="OutputFields"             path="/Reporting/OutputFields.jsp"
 * @struts:action-forward   name="OutputFieldsOrder"        path="/Reporting/OutputFieldsOrder.jsp"
 * @struts:action-forward   name="SelectionCriteriaFields"  path="/Reporting/SelectionCriteriaFields.jsp"
 * @struts:action-forward   name="SelectionCriteria"        path="/Reporting/SelectionCriteria.jsp"
 * @struts:action-forward   name="SortCriteriaFields"       path="/Reporting/SortCriteriaFields.jsp"
 * @struts:action-forward   name="SortCriteria"             path="/Reporting/SortCriteria.jsp"
 * @struts:action-forward   name="PreviewQuery"             path="/Reporting/PreviewQuery.jsp"
 * @struts:action-forward   name="Cancel"                   path="/listQueries.action"
 * @struts:action-forward   name="Save"                     path="/Reporting/SaveConfirmation.jsp"
 * @struts:action-forward   name="SaveAs"                   path="/Reporting/SaveConfirmation.jsp"
 */

public abstract class QueryToolAction extends AFSCMEAction
{

    public ActionForward perform(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response,
        UserSecurityData usd)
        throws Exception
    {
        ActionForward actionForward = null;

        QueryForm qForm = (QueryForm) form;

        if (isCancelled(request))
        {
            qForm.clearForm();
            return mapping.findForward("Cancel");
        }

        ActionErrors errors = perform(mapping, qForm, request, usd);
        if (errors != null && !errors.isEmpty())
        {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        String linkClicked = qForm.getLinkClicked();

        String forwardName;
        if (qForm.getSaveButton() != null)
            forwardName = "Save";
        else if (qForm.getSaveAsButton() != null)
            forwardName = "SaveAs";
        else if (linkClicked == null || linkClicked.length() == 0)
        {
            qForm.clearForm();
            return mapping.findForward("Cancel");
        }
        else
            forwardName =
                (String) CollectionUtil.transliterate(
                    linkClicked,
                    QueryForm.WIZARD_PAGE_NAMES,
                    QueryForm.FORWARDS);

        if (linkClicked.equals(QueryForm.PAGE_PREVIEW_QUERY))
            qForm.generatePreviewQuery(usd);

        qForm.setPageName(null);
        qForm.setLinkClicked(null);
        qForm.setSaveButton(null);
        qForm.setSaveAsButton(null);

        return mapping.findForward(forwardName);
    }

    protected abstract ActionErrors perform(
        ActionMapping mapping,
        QueryForm qForm,
        HttpServletRequest request,
        UserSecurityData usd)
        throws Exception;
}
