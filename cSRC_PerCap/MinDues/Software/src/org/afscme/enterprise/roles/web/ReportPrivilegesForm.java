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
import org.afscme.enterprise.roles.ReportPrivilegeData;


/**
 * Contains the data displayed on the SelectReportPrivileges page. 
 *
 * @struts:form name="reportPrivilegesForm"
 */
public class ReportPrivilegesForm extends ActionForm
{
	private Map m_reports;
	private Set m_categories;
	private Integer[] m_selected;
	private Integer m_rolePk;
	private boolean m_update;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		m_selected = new Integer[0];
	}
	
	public Collection getReports(String category) {
		return (Collection)m_reports.get(category);
	}

	public Set getCategories() {
		return m_categories;
	}
	
	/** Setter for property reports.
	 * @param reports New value of property reports.
	 */
	public void setReports(Collection reports) {

		//divide the reports up by category
		//  (This is done in the form class, instead of the EJB, because category has no meaning anywhere except on this screen.
		//   The JSP could have been simply hard-coded to put the reports in the right place, but this way is nicer)
		m_reports = new HashMap();
		m_categories = new TreeSet();
		Iterator it = reports.iterator();
		while (it.hasNext()) {
			ReportPrivilegeData data = (ReportPrivilegeData)it.next();
			String category = data.getCategory();
			List reportList = (List)m_reports.get(category);
			if (reportList == null) {
				reportList = new LinkedList();
				m_reports.put(category, reportList);
				m_categories.add(category);
			}
			reportList.add(data);
		}
	}
	
	/** Getter for property selected.
	 * @return Value of property selected.
	 */
	public Integer[] getSelected() {
		return m_selected;
	}
	
	/** Setter for property selected.
	 * @param selected New value of property selected.
	 */
	public void setSelected(Integer[] selected) {
		m_selected = selected;
	}
	
	/** Getter for property rolePk.
	 * @return Value of property rolePk.
	 */
	public java.lang.Integer getPk() {
		return m_rolePk;
	}
	
	/** Setter for property rolePk.
	 * @param rolePk New value of property rolePk.
	 */
	public void setPk(java.lang.Integer rolePk) {
		m_rolePk = rolePk;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("RolePk: " + m_rolePk);
		buf.append(", Reports: " + m_reports);
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



