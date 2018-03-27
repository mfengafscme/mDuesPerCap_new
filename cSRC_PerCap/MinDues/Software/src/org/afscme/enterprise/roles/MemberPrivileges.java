package org.afscme.enterprise.roles;

/** Contains constants used by get/setMemberPrivileges in the MaintainPrivileges EJB */
public interface MemberPrivileges {

    /** Members may not view or edit their data */
	public static final int NONE = 0;

    /** Members may only view their data */
	public static final int VIEW = 1;

    /** Members may edit and view their data */
	public static final int VIEW_AND_EDIT = 2;
	
}
