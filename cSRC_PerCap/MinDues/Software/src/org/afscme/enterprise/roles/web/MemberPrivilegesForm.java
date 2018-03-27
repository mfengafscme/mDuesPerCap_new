package org.afscme.enterprise.roles.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Set;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.roles.*;



/**
 * Represents the form on the member privileges page.
 *
 * @struts:form name="memberPrivilegesForm"
 */
public class MemberPrivilegesForm extends ActionForm
{
	private boolean m_allowEdit;
	private boolean m_allowView;
	
	public int getData() {
		if (m_allowEdit)
			return MemberPrivileges.VIEW_AND_EDIT;
		else if (m_allowView)
			return MemberPrivileges.VIEW;
		else
			return MemberPrivileges.NONE;
	}
	
	public void setData(int data) {
		switch (data) {
			case MemberPrivileges.NONE:
				m_allowView = false;
				m_allowEdit = false;
				break;
			case MemberPrivileges.VIEW:
				m_allowView = true;
				m_allowEdit = false;
				break;
			case MemberPrivileges.VIEW_AND_EDIT:
				m_allowView = true;
				m_allowEdit = true;
				break;
		}
	}
	
	public void setAllowEdit(boolean val) {
		m_allowEdit = val;
	}
	
	public boolean isAllowEdit() {
		return m_allowEdit;
	}

	public void setAllowView(boolean val) {
		m_allowView = val;
	}
	
	public boolean isAllowView() {
		return m_allowView;
	}

}



