package org.afscme.enterprise.roles.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.roles.PrivilegeData;
import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;



/**
 * Contains the data displayed on the SelectprivilegePrivileges page. 
 *
 * @struts:form name="privilegesForm"
 */
public class PrivilegesForm extends ActionForm
{
	private Collection m_privileges;
	private Set m_categories;
	private String[] m_selected;
	private Integer m_rolePk;
	private boolean m_update;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		m_selected = new String[0];
	}
	
	public List getEditViewPrivileges(char category, boolean isDataUtility) {
        List result = new LinkedList();
		Iterator it = getPrivileges(category, 'E', isDataUtility).iterator();
        while (it.hasNext()) {
            PrivilegeData priv = (PrivilegeData)it.next();
            result.add(priv.getName());
        }
		it = getPrivileges(category, 'V', isDataUtility).iterator();
        while (it.hasNext()) {
            PrivilegeData priv = (PrivilegeData)it.next();
            if (!result.contains(priv.getName()))
                result.add(priv.getName());
        }
        
        return result;
	}
    
	public List getPrivileges(Character category, char verb, boolean isDataUtility) {
		return getPrivileges(category.charValue(), verb, isDataUtility);
	}

	public List getPrivileges(char category, char verb, boolean isDataUtility) {
		return getPrivileges(category, verb, isDataUtility, null);
	}

	public List getPrivileges(char category, char verb, boolean isDataUtility, String name) {
		List l = new LinkedList();
		Iterator it = m_privileges.iterator();
		while (it.hasNext()) {
			PrivilegeData p = (PrivilegeData)it.next();
			if (category == p.getCategory() &&
				verb == p.getVerb() &&
				isDataUtility == p.isDataUtility() &&
				(name == null || name.equals(p.getName())))
			{
				l.add(p);
			}
		}
		return l;
	}

	public PrivilegeData getPrivilege(char category, char verb, boolean isDataUtility, String name) {
		List l = getPrivileges(category, verb, isDataUtility, name);
		if (l.size() == 0)
			return null;
		else
			return (PrivilegeData)l.get(0);
	}
	
	public Set getCategories() {
		return m_categories;
	}
	
	public void setPrivileges(Collection privileges) {

		m_privileges = privileges;
	}
	
	/** Getter for property selected.
	 * @return Value of property selected.
	 */
	public String[] getSelected() {
		return m_selected;
	}
	
	/** Setter for property selected.
	 * @param selected New value of property selected.
	 */
	public void setSelected(String[] selected) {
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
		buf.append(", privileges: " + m_privileges);
		buf.append(", Selected: " + TextUtil.toString(m_selected));
		buf.append(", Update: " + m_update);
		return buf.toString();
	}
	
	public boolean isUpdate() {
		return m_update;
	}
	
	public void setUpdate(boolean update) {
		m_update = update;
	}
    
    public String getJavaScriptId(boolean isDataUtility, char category, String verb, String id)
    {
        return new DelimitedStringBuffer("_")
            .append(isDataUtility ? "dataUtility" : "main")
            .append(category)
            .append(verb)
            .append(id)
            .toString();
    }
    
    /**
     * Ensures that corresponding 'view' privielges are selcted for each 'edit' privilege.
     * This is necessary because when the 'view' privileges are disabled in JavaScript,
     *  the browser does not POST their values with the form.
     */
    public void addViewPrivileges() {
        
        Set selectedSet = new HashSet();
        selectedSet.addAll(Arrays.asList(m_selected));

        Iterator it = m_privileges.iterator();
		while (it.hasNext()) {
			PrivilegeData p = (PrivilegeData)it.next();
            if (p.getVerb() == PrivilegeData.VERB_EDIT) {
                PrivilegeData viewPrivilege = getPrivilege(p.getCategory(), PrivilegeData.VERB_VIEW, p.isDataUtility(), p.getName());
                if (viewPrivilege != null) {
                    String viewKey = viewPrivilege.getKey();
                    if (selectedSet.contains(p.getKey()) && !selectedSet.contains(viewKey)) {
                        selectedSet.add(viewKey);
                    }
                }
            }
        }
        
        m_selected = (String[])selectedSet.toArray(m_selected);
    }
}



