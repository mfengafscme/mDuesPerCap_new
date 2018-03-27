package org.afscme.enterprise.users.web;

import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.users.UserData;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * Represents the HTML form on the 'Edit User' page.
 *
 * @struts:form name="userForm"
 */
public class UserForm extends ActionForm
{
    // dummy passwords, these will be displayed as ****** on the form.
    private static final String DUMMY_PASSWORD = "xxxxxx";
    private static final String DUMMY_PASSWORD2 = "yyyyyy";

    // editable properties
    private UserData m_userData;
    private String m_password;
    private String m_password2;

    // Buttons
    private String m_selectAffiliates;
    private String m_selectRoles;
    private String m_unlock;

    // Read only properties
    private String m_lastLoginDate;
    private String m_passwordExpirationDate;
    private String m_lockoutDate;
    private String m_roles;

    /**
     * Default constructor
     */
    public UserForm() {
        m_userData = new UserData();
        m_password = DUMMY_PASSWORD;
        m_password2 = DUMMY_PASSWORD2;
    }
    
    /**
     * Validates information entered by the user.
     *
     * @return An ActionError object containing the errors found.  The list may be empty
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors  errors = new ActionErrors();
        
        if (getUserId() == null)
            return null; //form has not been used yet, don't validate
        
        m_userData.setPersonPk(AFSCMEAction.getCurrentPersonPk(request));

        if (TextUtil.isEmpty(m_userData.getUserId()))
            errors.add("userId", new ActionError("error.field.required"));
        
        if (!TextUtil.isWord(m_userData.getUserId()))
            errors.add("userId", new ActionError("error.field.mustBeWord"));
        
        if (isPasswordChanged()) {
            if (!m_password.equals(m_password2)) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.passwordsMustMatch"));
            }
        }
        
        if (TextUtil.isEmpty(m_password)) {
            errors.add("password", new ActionError("error.field.required"));
        } else if (m_password.length() < 4) {
            errors.add("password", new ActionError("error.field.length.tooShort", new Integer(4)));
        }

        if (TextUtil.isEmpty(m_password2)) {
            errors.add("password2", new ActionError("error.field.required"));
        } else if (m_password.length() < 4) {
            errors.add("password2", new ActionError("error.field.length.tooShort", new Integer(4)));
        }
            
        //disallow choosing an integer userId that is not the person id.
        if (TextUtil.isInt(m_userData.getUserId()) &&
            !m_userData.getUserId().equals(String.valueOf(m_userData.getPersonPk()))) {
            errors.add("userId", new ActionError("error.editUser.userId.userIdCantBeInt"));
        }
        
        return errors;
    }

    /**
     * Sets this object's data
     * 
     * @param data The UserData to retrieve information from
     */
    public void setUserData(UserData data) {
        m_userData = data;
        if (data.getPasswordExpirationDate() == null)
            m_passwordExpirationDate = "None";
        else
            m_passwordExpirationDate = TextUtil.format(data.getPasswordExpirationDate());

        if (data.getLastLoginDate() == null)
            m_lastLoginDate = "None";
        else
            m_lastLoginDate = TextUtil.formatDateTime(data.getLastLoginDate());

        if (data.getLockoutDate() != null)
            m_lockoutDate = TextUtil.formatDateTime(data.getLockoutDate());
    }
    
    /**
     * Gets the UserData object being edited by this form 
     */
    public UserData getUserData() {
        return m_userData;
    }
    
    /**
     * Gets the user's id
     */
    public String getUserId() {
        return m_userData.getUserId();
    }
    
    /**
     * Sets the user's id
     */
    public void setUserId(String userId) {
        m_userData.setUserId(userId.trim());
    }
    
    /**
     * Gets the password property
     */
    public String getPassword() {
        return m_password;
    }
    
    /**
     * Sets the password property
     */
    public void setPassword(String password) {
        m_password = password.trim();
    }

    /**
     * Gets the password2 property
     */
    public String getPassword2() {
        return m_password2;
    }
    
    /**
     * Sets the password property
     */
    public void setPassword2(String password2) {
        m_password2 = password2.trim();
    }

    /**
     * Returns true if the user is locked out
     */
    public boolean isLockedOut() {
        return m_lockoutDate != null;
    }
    
    /**
     * Gets the user's challenge question primary key
     */
    public Integer getChallengeQuestion() {
        return m_userData.getChallengeQuestion();
    }
    
    /**
     * Sets the user's challenge question primary key
     */
    public void setChallengeQuestion(Integer challengeQuestion) {
        m_userData.setChallengeQuestion(challengeQuestion);
    }
    
    /**
     * Gets the user's challenge response
     */
    public String getChallengeResponse() {
        return m_userData.getChallengeResponse();
    }
    
    /**
     * Sets the user's challenge response
     */
    public void setChallengeResponse(String challengeResponse) {
        m_userData.setChallengeResponse(challengeResponse);
    }
    
    /**
     * Gets the user remarks
     */
    public String getRemarks() {
        return m_userData.getRemarks();
    }
    
    /**
     * Sets the user remarks
     */
    public void setRemarks(String remarks) {
        m_userData.setRemarks(remarks);
    }
    
    /**
     * Gets the user's department's primary key as a string.
     * Returns "" if the user has no department
     */
    public String getDepartment() {
        if (m_userData.getDepartment() == null)
            return "";
        else
            return m_userData.getDepartment().toString();
    }
    
    /**
     * Sets the user's department's primary key as a string.
     * 
     * @param department the new department.  passing null or "" indicates no department
     */
    public void setDepartment(String department) {
        if (TextUtil.isEmpty(department))
            m_userData.setDepartment(null);
        else
            m_userData.setDepartment(Integer.valueOf(department));
    }

    /**
     * Gets the user's preferred start page
     */
    public String getStartPage() {
        return String.valueOf(m_userData.getStartPage());
    }
    
    /**
     * Sets the user's preferred start page
     */
    public void setStartPage(String startPage) {
        m_userData.setStartPage(startPage.charAt(0));
    }
    
    /**
     * Returns true if the user edited their password
     */
    public boolean isPasswordChanged() {
        return (!(m_password.equals(DUMMY_PASSWORD) && m_password2.equals(DUMMY_PASSWORD2)));
    }
    
    //
    // READ-ONLY PROPERTIES
    //
    // These properties are 'read-only'.
    // Setters exist only to allow the form to be re-displayed correctly in the event of an error
    //

    /**
     * Gets the date on which the user's password expires
     */
    public String getPasswordExpirationDate() {
        return m_passwordExpirationDate;
    }
    public void setPasswordExpirationDate(String value) {
        m_passwordExpirationDate = value;
    }
    
    /**
     * Gets the date and time of the user's last login
     */
    public String getLastLoginDate() {
        return m_lastLoginDate;
    }
    public void setLastLoginDate(String value) {
        m_lastLoginDate = value;
    }
    
    /**
     * Gets the date on which the user was locked out
     */
    public String getLockoutDate() {
        return m_lockoutDate;
    }
    public void setLockoutDate(String value) {
        m_lockoutDate = value;
    }

    /**
     * Gets a string representation of the user's list of roles
     */
    public String getRoles() {
        return m_roles;
    }
    public void setRoles(String roles) {
        m_roles = roles;
    }
    
    // END READ-ONLY PROPERTIES

    //
    // BUTTON PROPERTIES
    //
    // These properties correspond to alternate submit buttons on the form.
    //
    
    //Select Affiliates
    public boolean isSelectAffiliatesButton() {
        return m_selectAffiliates != null;
    }
    public String getSelectAffiliates() {
        return m_selectAffiliates;
    }
    public void setSelectAffiliates(String selectAffiliates) {
        m_selectAffiliates = selectAffiliates;
    }
    
    //Select Roles
    public boolean isSelectRolesButton() {
        return m_selectRoles != null;
    }
    public String getSelectRoles() {
        return m_selectRoles;
    }
    public void setSelectRoles(String selectRoles) {
        m_selectRoles = selectRoles;
    }
    
    //Unlock
    public boolean isUnlockButton() {
        return m_unlock != null;
    }
    public String getUnlock() {
        return m_unlock;
    }
    public void setUnlock(String unlock) {
        m_unlock = unlock;
    }

    // END BUTTON PROPERTIES


    /**
     * Returns a string representation of this form's data
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(m_userData.toString());
        buf.append(", roles: " + m_roles);
        buf.append(", selectAffiliates: " + m_selectAffiliates);
        buf.append(", isSelectAffiliatesButton: " + isSelectAffiliatesButton());
        buf.append(", isSelectRolesButton: " + isSelectAffiliatesButton());
        buf.append(", password: " + m_password);
        buf.append(", password2: " + m_password2);
        return buf.toString();
    }
}



