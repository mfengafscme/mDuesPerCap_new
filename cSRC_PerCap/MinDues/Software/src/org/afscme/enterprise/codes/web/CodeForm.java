package org.afscme.enterprise.codes.web;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.codes.CodeData;


/**
 * @struts:form name="codeForm"
 */
public class CodeForm extends SearchForm
{
	private CodeData m_data;
	private String m_codeTypeKey;
	
    public CodeForm() {
        init();
    }
    
	public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        init();
	}
    
    public void init() {
        m_data = new CodeData();
    }
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		if (getCode() == null) {
			//form data hasn't been set yet, don't validate
			return null;
		}
		
		ActionErrors  errors = new ActionErrors();
		
		if (TextUtil.isEmpty(m_data.getDescription()))
			errors.add("description", new ActionError("error.field.required"));

        if (m_data.getCode().length() > 8)
            errors.add("code", new ActionError("error.field.length.exceeded", new Integer(8)));
		return errors;
	}
	
	public CodeData getData() {
		return m_data;
	}
	
	public void setData(CodeData data) {
		m_data = data;
	}
	
	public String getCode() {
		return m_data.getCode();
	}
	
	public void setCode(String code) {
		m_data.setCode(code);
	}

	public Integer getPk() {
		return m_data.getPk();
	}
	
	public void setPk(Integer pk) {
		m_data.setPk(pk);
	}
	
	/** Named 'CodeDescription' instead of 'Description' so it doesn't conflict with the code type 'Description' on preceding or following page */
	public String getCodeDescription() {
		return m_data.getDescription();
	}
	
	/** Named 'CodeDescription' instead of 'Description' so it doesn't conflict with the code type 'Description' on preceding or following page */
	public void setCodeDescription(String description) {
		m_data.setDescription(description);
	}

	
	public java.lang.String getSortKey() {
		return m_data.getSortKey();
	}	
	
	public void setSortKey(java.lang.String sortKey) {
		m_data.setSortKey(sortKey);
	}

	public String getCodeTypeKey() {
		return m_codeTypeKey;
	}
	
	public void setCodeTypeKey(String codeTypeKey) {
		m_codeTypeKey = codeTypeKey;
	}
	
	public boolean isAdd() {
		return (m_data.getPk() == null) || (m_data.getPk().intValue() == 0);
	}
	
    public String toString() {
		return m_data.toString();
	}
	
}



