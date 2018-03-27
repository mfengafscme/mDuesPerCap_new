package org.afscme.enterprise.codes.web;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.codes.CategoryData;
import org.afscme.enterprise.codes.CodeData;
import org.afscme.enterprise.codes.CodeTypeData;


/**
 * @struts:form name="codeTypeForm"
 */
public class CodeTypeForm extends SearchForm
{
	private CodeTypeData m_data;
	private String m_addCode;
	private boolean m_add;
	private String[] m_categoryNames;
	private String[] m_categoryKeys;
	
	public CodeTypeForm() {
		m_data = new CodeTypeData();
		order = 1;
		sortBy = "sortKey";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		if (getName() == null) {
			//form data hasn't been set yet, don't validate
			return null;
		}
        if (request.getParameter(org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY) != null) {
            return null;
        }
		
		ActionErrors  errors = new ActionErrors();
		
		if (TextUtil.isEmpty(m_data.getName()))
			errors.add("name", new ActionError("error.field.required"));

		if (TextUtil.isEmpty(m_data.getDescription()))
			errors.add("description", new ActionError("error.field.required"));

		if (m_add && TextUtil.isEmpty(m_data.getKey()))
			errors.add("codeTypeKey", new ActionError("error.field.required"));

		if (m_add && !TextUtil.isWord(m_data.getKey()))
			errors.add("codeTypeKey", new ActionError("error.field.mustBeWord"));

		return errors;
	}
	
	public CodeTypeData getData() {
		return m_data;
	}
	
	public void setData(CodeTypeData data) {
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

	public Integer getCategory() {
		return m_data.getCategory();
	}
	
	public void setCategory(Integer category) {
		m_data.setCategory(category);
	}
	
	
	public String getCodeTypeKey() {
		return m_data.getKey();
	}
	
	public void setCodeTypeKey(String key) {
		m_data.setKey(key);
	}

	public boolean isAddCodeButton() {
		return m_addCode != null;
	}
	public String getAddCode() {
		return m_addCode;
	}
	public void setAddCode(String addCode) {
		m_addCode= addCode;
	}
	
	public boolean isAdd() {
		return m_add;
	}
	
	public void setAdd(boolean val) {
		m_add = val;
	}


	public void setCategories(Map categories) {
		setCategoryKeys(CollectionUtil.toStringArray(categories.keySet()));
		m_categoryNames = new String[categories.size()];
		Iterator it = categories.values().iterator();
		for (int i = 0; i < m_categoryNames.length; i++)
			m_categoryNames[i] = ((CategoryData)it.next()).getName();
	}
	
	public String[] getCategoryNames() {
		return m_categoryNames;
	}
	
	public void setCategoryNames(String[] categoryNames) {
		m_categoryNames = categoryNames;
	}
	
	public String[] getCategoryKeys() {
		return m_categoryKeys;
	}
	
	public void setCategoryKeys(String[] categoryKeys) {
		m_categoryKeys = categoryKeys;
	}

	public void setResults(Collection results) {
		List sortedResults = new LinkedList();

        //remove the inactive codes
        Iterator it = results.iterator();
        while (it.hasNext()) {
            CodeData data = (CodeData)it.next();
            if (data.isActive())
                sortedResults.add(data);
        }
        
        //sort the codes
		Collections.sort(sortedResults, new Comparator() {
			public int compare(Object a, Object b) {
				CodeData codeA = (CodeData)a;
				CodeData codeB = (CodeData)b;
				if (sortBy.equals("code"))
					return TextUtil.compareAlphanumerics(codeA.getCode(), codeB.getCode()) * order;
				else if (sortBy.equals("description"))
					return TextUtil.compareAlphanumerics(codeA.getDescription(), codeB.getDescription()) * order;
				else if (sortBy.equals("sortKey"))
					return TextUtil.compareAlphanumerics(codeA.getSortKey(), codeB.getSortKey()) * order;
				else 
					return (codeB.hashCode() - codeA.hashCode()) * order;
			}
		});
					
		this.results = sortedResults;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(m_data.toString());
		buf.append(", addCode: " + m_addCode);
		buf.append(", isAddCodeButton: " + isAddCodeButton());
		return buf.toString();
	}
	
		
}



