
package org.afscme.enterprise.codes.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.codes.CodeTypeData;

/**
 * @struts:action   path="/listCodeTypes"
 *
 * @struts:action-forward name="View" path="/Admin/ListCodeTypes.jsp"
 */
public class ListCodeTypesAction extends AFSCMEAction {
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, UserSecurityData usd) throws Exception {

		Map codeTypes = s_maintainCodes.getCodeTypes();
		Map allCategories = s_maintainCodes.getCategories();
		Map categoryTypes = new HashMap();
		Map usedCategories = new HashMap();

		Iterator it = codeTypes.values().iterator();
		while (it.hasNext()) {
			CodeTypeData codeTypeData = (CodeTypeData)it.next();
			Integer category = codeTypeData.getCategory();
			List types = (List)categoryTypes.get(category);
			if (types == null) {
				types = new LinkedList();
				categoryTypes.put(category, types);
				usedCategories.put(category, allCategories.get(category));
			}
			types.add(codeTypeData);
		}
		
		request.setAttribute("Categories", usedCategories);
		request.setAttribute("CategoryTypes", categoryTypes);

		return mapping.findForward("View");
	}
}
