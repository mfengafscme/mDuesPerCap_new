package org.afscme.enterprise.users.web;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * Handles displaying and editing on the 'SelectReportRoles' page. 
 *
 * @struts:action   name="rolesForm"
 *                  path="/selectRoles"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/SelectRoles.jsp"
 *
 * @struts:action-forward   name="View"  path="/Admin/SelectRoles.jsp"
 * @struts:action-forward   name="Done"  path="/editUser.action"
 */
public class SelectRolesAction extends AFSCMEAction {

	//used to type the toArray() call in the perform(..) method
	private static final Integer[] INT_ARRAY = new Integer[0];
	
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
		RolesForm rolesForm = (RolesForm)form;

        Integer personPk = getCurrentPersonPk(request);
        
		if (!rolesForm.isUpdate()) {

			//READ
			//User arrived here from the edit user page.  Get and display the roles
			rolesForm.setAllRoles(new LinkedList(s_maintainPrivileges.getRoles().values()));
			Set selected = s_maintainUsers.getRoles(personPk);
			rolesForm.setSelected(CollectionUtil.toStringArray(selected));
			return mapping.findForward("View");
		} else if (!isCancelled(request)) {

			//WRITE
			//User submitted the roles form.  Save the results and go back to the edit role page
			
			String[] selected = rolesForm.getSelected();
			Set selectedSet = new HashSet();
			for (int i=0; i < selected.length; i++)
				selectedSet.add(selected[i]);
			s_maintainUsers.setRoles(personPk, selectedSet);
		}

		return mapping.findForward("Done");
	}
}
