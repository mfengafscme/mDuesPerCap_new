package org.afscme.enterprise.roles.web;

import java.util.HashSet;
import java.util.Set;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.afscme.enterprise.controller.UserSecurityData;






/**
 * Handles displaying and editing on the 'SelectReportPrivileges' page. 
 *
 * @struts:action   name="privilegesForm"
 *                  path="/selectPrivileges"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/SelectPrivileges.jsp"
 *
 * @struts:action-forward   name="View"  path="/Admin/SelectPrivileges.jsp"
 * @struts:action-forward   name="Done"  path="/editRole.action"
 */
public class SelectPrivilegesAction extends AFSCMEAction {

	//used to type the toArray() call in the perform(..) method
	private static final Integer[] INT_ARRAY = new Integer[0];
	
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		PrivilegesForm privilegesForm = (PrivilegesForm)form;

		//if the user arrived here from the 'add role' page, the new pk was passed in as a request attribute
		Integer newPk = (Integer)request.getAttribute("pk");
		if (newPk != null)
			privilegesForm.setPk(newPk);
		
		if (!privilegesForm.isUpdate()) {

			//READ
			//User arrived here from the add/edit role page.  Get and display the privileges

			privilegesForm.setPrivileges(s_maintainPrivileges.getPrivilegesList());
			Set selected = s_maintainPrivileges.getPrivileges(privilegesForm.getPk());
			privilegesForm.setSelected(CollectionUtil.toStringArray(selected));
			return mapping.findForward("View");
		} else if (!isCancelled(request)) {

			//WRITE
			//User submitted the privileges form.  Save the results and go back to the edit role page

            //these 2 lines are necessary because browsers do not submit the values in any disabled 'view' checkboxes
            privilegesForm.setPrivileges(s_maintainPrivileges.getPrivilegesList());
            privilegesForm.addViewPrivileges();
            
			String[] selected = privilegesForm.getSelected();
			Set selectedSet = new HashSet();
			for (int i=0; i < selected.length; i++)
				selectedSet.add(selected[i]);
			s_maintainPrivileges.setPrivileges(privilegesForm.getPk(), selectedSet);
		}

		return mapping.findForward("Done");
	}

}
