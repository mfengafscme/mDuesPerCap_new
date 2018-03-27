
package org.afscme.enterprise.users.web;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.roles.RoleData;
import org.afscme.enterprise.controller.UserSecurityData;


/**
 * Reads the user's data and displays the 'Edit User' page.
 * Params:
 *      pk - primary key of the user to edit
 *
 * @struts:action   path="/editUser"
 *                    name="userForm"
 *                  scope="request"
 *
 * @struts:action-forward   name="Edit"  path="/Admin/EditUser.jsp"
 */
public class EditUserAction extends AFSCMEAction {
    
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

        UserForm userForm = (UserForm)form;

        Integer personPk = getCurrentPersonPk(request, "personPk");
        
        //put user data into form
        userForm.setUserData(s_maintainUsers.getUser(personPk));

        //put role data into form
        Set roles = s_maintainUsers.getRoles(personPk);
        Map allRoles = s_maintainPrivileges.getRoles();
        DelimitedStringBuffer roleNames = new DelimitedStringBuffer(", ");
        Iterator it = roles.iterator();
        for (int i = 0; it.hasNext(); i++)
            roleNames.append(((RoleData)allRoles.get((Integer)it.next())).getName());
        userForm.setRoles(roleNames.toString());
        
        //
        return mapping.findForward("Edit");
    }
}
