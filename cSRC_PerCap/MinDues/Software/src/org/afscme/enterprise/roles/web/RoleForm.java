package org.afscme.enterprise.roles.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.roles.RoleData;


/**
 * Represents the data on the add/edit role page (role name and role description)
 *
 * @struts:form name="roleForm"
 */
public class RoleForm extends ActionForm
{
	private RoleData m_data;
	private String m_selectPrivileges;
	private String m_selectReportPrivileges;
	private String m_selectFieldPrivileges;
	
	public RoleForm() {
		m_data = new RoleData();
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors  errors = new ActionErrors();
		
		if (getName() == null)
			return null; //form is 'new', don't validate

		if (TextUtil.isEmpty(m_data.getName()))
			errors.add("name", new ActionError("error.field.required"));

		if (TextUtil.isEmpty(m_data.getDescription()))
			errors.add("description", new ActionError("error.field.required"));

		return errors;
	}
	
	public RoleData getData() {
		return m_data;
	}
	
	public void setData(RoleData data) {
		m_data = data;
	}
	
	public String getName() {
		return m_data.getName();
	}
	
	public void setName(String name) {
		m_data.setName(name);
	}
	
	public String getDescription() {
		return m_data.getDescription();
	}
	
	public void setDescription(String description) {
		m_data.setDescription(description);
	}
	
	public Integer getPk() {
		return m_data.getPk();
	}
	
	public void setPk(Integer pk) {
		m_data.setPk(pk);
	}
	

	//
	// The following 3 properties 'button' properties.  They are true iff they are set.
	//
	public boolean isSelectPrivilegesButton() {
		return m_selectPrivileges != null;
	}
	public boolean isSelectFieldPrivilegesButton() {
		return m_selectFieldPrivileges != null;
	}
	public boolean isSelectReportPrivilegesButton() {
		return m_selectReportPrivileges != null;
	}
	public String getSelectPrivileges() {
		return m_selectPrivileges;
	}
	public String getSelectReportPrivileges() {
		return m_selectReportPrivileges;
	}
	public String getSelectFieldPrivileges() {
		return m_selectFieldPrivileges;
	}
	public void setSelectPrivileges(String selectPrivileges) {
		m_selectPrivileges = selectPrivileges;
	}
	public void setSelectReportPrivileges(String selectReportPrivileges) {
		m_selectReportPrivileges = selectReportPrivileges;
	}
	public void setSelectFieldPrivileges(String selectFieldPrivileges) {
		m_selectFieldPrivileges = selectFieldPrivileges;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(m_data.toString());
		buf.append(", selectPrivileges: " + m_selectPrivileges);
		buf.append(", selectReportPrivileges: " + m_selectReportPrivileges);
		buf.append(", selectFieldPrivileges: " + m_selectFieldPrivileges);
		buf.append(", isSelectPrivilegesButton: " + isSelectPrivilegesButton());
		buf.append(", isSelectReportPrivilegesButton: " + isSelectReportPrivilegesButton());
		buf.append(", isSelectFieldPrivilegesButton: " + isSelectFieldPrivilegesButton());
		return buf.toString();
	}
		
}



