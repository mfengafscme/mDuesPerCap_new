package org.afscme.enterprise.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * A container for the mapping from struts action names to privileges.  The values 
 * are stored in a configuration file and read at system start up.
 */
public class ActionPrivileges implements Serializable
{
    private static Logger log = Logger.getLogger(ActionPrivileges.class);
    
    /**
     * This is a special privilege, determined by a member's affiliate, rather than through the user's roles.
     * This privilege allows the user to view their personal information.
     */
    public static final String PRIVILEGE_VIEW_PERSONAL_INFORMATION="ViewPersonalInformation";

    /**
     * This is a special privilege, determined by a member's affiliate, rather than through the user's roles.
     * This privilege allows the user to edit their personal information.
     */
    public static final String PRIVILEGE_EDIT_PERSONAL_INFORMATION="EditPersonalInformation";

    /**
     * This is a special privilege, given to all users.
     */
    public static final String PRIVILEGE_USER="User";
    
    /**
     * This is a special privilege, given to AFSCME International staff.
     */
    public static final String PRIVILEGE_AFSCME_INTERNATIONAL_USER="AFSCMEUser";
    
    /**
     * This is a special privilege, given to users that can run the data utility as an affiliate.
     */
    public static final String PRIVILEGE_DATA_UTILITY_USER="DataUtilityUser";

    /**
     * This is a special privilege, given to users that have more that one affiliate they can run the Data Utility as.
     */
    public static final String PRIVILEGE_MULTIPLE_AFFILIATES="MultipleAffiliates";
    
    /**
     * A map of action names, to a Set of privileges that will grant access to
	 */
    Map actionPrivileges;
    
    /**
     * Consturctor
     *
     * @param actions A map of action names, to a Set of privileges that will grant access to
     * that action
     */
    public ActionPrivileges(Map actionPrivileges) {
        this.actionPrivileges = actionPrivileges;
    }
	
	/**
	 * Returns true iff the specified action is allowed for the given user.
	 * @param action - The action to check permissions for.
	 * @param securityData - The user for which to check action permissions.
	 * @return boolean
	 */
	public boolean isActionAllowed(String action, UserSecurityData securityData) 
	{
        Set privileges = (Set)actionPrivileges.get(action);
        
		if (privileges == null) {
            log.warn("Action '" + action + "' does not exist in the access control list (acl.xml file)");
            return true;
            //throw new RuntimeException("Action '" + action + "' does not exist in the access control list (acl.xml file)");
        }

        Iterator it = privileges.iterator();

        Set userPrivileges = null;
        if (securityData.getActingAsAffiliate() != null)
            userPrivileges = securityData.getDataUtilityPrivileges();
        else
            userPrivileges = securityData.getPrivileges();

        while (it.hasNext()) {
            String privilege = (String)it.next();
            if (userPrivileges.contains(privilege)) {
                return true;
            }
        }
        
        return false;
	}
}
