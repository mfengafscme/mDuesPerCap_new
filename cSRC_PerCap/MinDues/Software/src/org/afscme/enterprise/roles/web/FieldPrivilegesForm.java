package org.afscme.enterprise.roles.web;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Set;
import java.util.TreeMap;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.reporting.base.BRUtil;
import org.afscme.enterprise.reporting.base.access.ReportField;



/**
 * Contains the data displayed on the SelectFieldPrivileges page. 
 *
 * @struts:form name="fieldPrivilegesForm"
 */
public class FieldPrivilegesForm extends ActionForm
{
	private Map m_fields;
	private Integer[] m_selected;
	private Integer m_rolePk;
	private boolean m_update;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		m_selected = new Integer[0];
	}
	
	public Map getUserFields() {
		return m_fields;
	}

	public void setFields(Collection fields) {
		m_fields = new TreeMap(BRUtil.entityOrder());
		Iterator it = fields.iterator();
		while (it.hasNext()) {
			ReportField data = (ReportField)it.next();
			String category = data.getCategoryName();
			Character entity = new Character(data.getEntityType());
			addField(entity, category, data);
		}
	}

	private void addField(Character entity, String category, ReportField data) {
		Map entityCategories = (Map)m_fields.get(entity);
		if (entityCategories == null) {
			entityCategories = new TreeMap(BRUtil.categoryOrder());
			m_fields.put(entity, entityCategories);
		}
		Set categoryFields = (Set)entityCategories.get(category);
		if (categoryFields == null) {
			categoryFields = new TreeSet(BRUtil.fieldOrder());
			entityCategories.put(category, categoryFields);
		}
		categoryFields.add(data);
	}
	
	public Integer[] getSelected() {
		return m_selected;
	}
	
	public void setSelected(Integer[] selected) {
		m_selected = selected;
	}
	
	public java.lang.Integer getPk() {
		return m_rolePk;
	}
	
	public void setPk(java.lang.Integer rolePk) {
		m_rolePk = rolePk;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("RolePk: " + m_rolePk);
		buf.append(", Fields: " + m_fields);
		buf.append(", Selected: " + m_selected);
		buf.append(", Update: " + m_update);
		return buf.toString();
	}
	
	public boolean isUpdate() {
		return m_update;
	}
	
	public void setUpdate(boolean update) {
		m_update = update;
	}
}



