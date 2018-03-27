package org.afscme.enterprise.roles.web;

import java.util.HashSet;
import java.util.Set;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * Handles displaying and editing on the 'SelectFieldPrivileges' page. 
 *
 * @struts:action   name="fieldPrivilegesForm"
 *                  path="/selectFieldPrivileges"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/SelectFieldPrivileges.jsp"
 *
 * @struts:action-forward   name="View"  path="/Admin/SelectFieldPrivileges.jsp"
 * @struts:action-forward   name="Done"  path="/editRole.action"
 */
public class SelectFieldPrivilegesAction extends AFSCMEAction {

	//used to type the toArray() call in the perform(..) method
	private static final Integer[] INT_ARRAY = new Integer[0];
	
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		FieldPrivilegesForm fpForm = (FieldPrivilegesForm)form;

		//if the user arrived here from the 'add role' page, the new pk was passed in as a request attribute
		Integer newPk = (Integer)request.getAttribute("pk");
		if (newPk != null)
			fpForm.setPk(newPk);
		
		if (!fpForm.isUpdate()) {

			//READ
			//User arrived here from the add/edit role page.  Get and display the Field privileges

			fpForm.setFields(s_maintainPrivileges.getFields().values());
			Integer[] selectedFields = (Integer[])s_maintainPrivileges.getFields(fpForm.getPk()).toArray(INT_ARRAY);
			fpForm.setSelected(selectedFields);
			return mapping.findForward("View");
		} else if (!isCancelled(request)) {

			//WRITE
			//User submitted the Field privileges form.  Save the results and go back to the edit role page
			
			Integer[] selected = fpForm.getSelected();
			Set selectedSet = new HashSet();
			for (int i=0; i < selected.length; i++)
				selectedSet.add(selected[i]);
			s_maintainPrivileges.setFields(fpForm.getPk(), selectedSet);
		}

		return mapping.findForward("Done");
	}
}
