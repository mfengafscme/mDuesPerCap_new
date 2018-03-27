
package org.afscme.enterprise.roles.web;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.roles.RoleData;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.afscme.enterprise.controller.UserSecurityData;



/**
 * Lists the roles in the system.
 *
 * @struts:action   path="/listRoles"
 *					name="listRolesForm"
 *					scope="request"
 *					input="/Admin/ListRoles.jsp"
 *
 * @struts:action-forward   name="View"  path="/Admin/ListRoles.jsp"
 */
public class ListRolesAction extends AFSCMEAction {
	
	/** Gets a map of roles in the system, places it in the request under the key "RoleMap", and forwards to /Admin/ListRoles.jsp */
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {
		
		ListRolesForm listRolesForm = (ListRolesForm)form;
		
		//get the roles
		Map roles = s_maintainPrivileges.getRoles();
		
		//sort the roles
		//NOTE:	Sorting is done in Java code here beacuse this screen only display 1 page of results.
		//		For a multi-page result set, the sorting should be done by the database.
		List sortedRoles = new LinkedList(roles.values());
        if (listRolesForm.getSortBy() != null)
    		sort(sortedRoles, listRolesForm.getSortBy(), listRolesForm.getOrder());
		
		//display the roles
		listRolesForm.setResults(sortedRoles);
		return mapping.findForward("View");
	}
	
	/** Sorts the list of roles by the given column
	 * @param roles the list of roles to sort
	 * @param SORT_BY_NAME or SORT_BY_DESCRIPTION
	 * @param order 1 (ascending) or -1 (descending)
	 **/
	private static void sort(List roles, final String sortBy, final int order) {
		Collections.sort(roles, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (sortBy.equals("name"))
					return order * ((RoleData)o1).getName().compareTo(((RoleData)o2).getName());
				else
					return order * ((RoleData)o1).getDescription().compareTo(((RoleData)o2).getDescription());
			}
		}
		);
	}
		
}
