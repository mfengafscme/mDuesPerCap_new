package org.afscme.enterprise.users.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.roles.*;



/**
 * Contains the data displayed on the SelectprivilegeRoles page. 
 *
 * @struts:form name="rolesForm"
 */
public class RolesForm extends ActionForm
{
	private Collection m_allRoles;
	private String[] m_selected;
	private boolean m_update;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		m_selected = new String[0];
	}
	
	public Collection getAllRoles() {
		return m_allRoles;
	}

	public void setAllRoles(Collection roles) {

		m_allRoles = roles;
	}
	
	public String[] getSelected() {
		return m_selected;
	}
	
	public void setSelected(String[] selected) {
		m_selected = selected;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(", allRoles: " + m_allRoles);
		buf.append(", selected: " + m_selected);
		buf.append(", update: " + m_update);
		return buf.toString();
	}
	
	public boolean isUpdate() {
		return m_update;
	}
	
	public void setUpdate(boolean update) {
		m_update = update;
	}
}
