package org.afscme.enterprise.users.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.JNDIUtil;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.apache.struts.action.ActionError;


/**
 * Handles the submissions from the 'Edit User' page. 
 *
 * @struts:action   name="userForm"
 *                  path="/saveUser"
 *                  scope="request"
 *                  validate="true"
 *                  input="/Admin/EditUser.jsp"
 *
 * @struts:action-forward   name="SelectAffiliates"  path="/selectUserAffiliatesSearch.action?new" redirect="true"
 * @struts:action-forward   name="SelectRoles"  path="/selectRoles.action" 
 * @struts:action-forward   name="PersonDetail"  path="viewPersonDetail.action?back=ListPersons" redirect="true" 
 */
public class SaveUserAction extends AFSCMEAction {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception
    {
        UserForm userForm = (UserForm)form;

        if (userForm.isUnlockButton()) {
            
            //unlock the account
            s_accessControl.resetLockout(userForm.getUserData());
            userForm.setLockoutDate(null);
            
        } else if (!isCancelled(request)) {
            
            //save the updates
            if (-1 == s_maintainUsers.updateUser(userForm.getUserData()))
                return makeErrorForward(request, mapping, "userId", "error.editUser.userId.taken");
            
            if (userForm.isPasswordChanged())
                s_accessControl.changePassword(getCurrentPersonPk(request), userForm.getPassword());
        }

        //go to the next screen
        if (userForm.isSelectAffiliatesButton())
            return mapping.findForward("SelectAffiliates");
        else if (userForm.isSelectRolesButton()) 
            return mapping.findForward("SelectRoles");
        else if (userForm.isUnlockButton()) 
            return new ActionForward(mapping.getInput());
        else
            return mapping.findForward("PersonDetail");
    }
}
