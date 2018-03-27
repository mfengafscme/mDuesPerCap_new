package org.afscme.enterprise.minimumdues.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.Collection;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.AffiliateData;
import org.afscme.enterprise.affiliate.EmployerData;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.afscme.enterprise.affiliate.NewAffiliate;
import org.afscme.enterprise.affiliate.ejb.MaintainAffiliates;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;


/**
 * @struts:action   path="/AffiliateChooseAdd"
 *                  name="AffiliateChooseAddForm"
 *                  scope="request"
 *                  validate="false"
 *
 *
 * @struts:action-forward   name="viewChooseAdd"  path="/Membership/AffiliateChooseAdd.jsp"
 */
public final class AffiliateChooseAddAction extends AFSCMEAction {

  public AffiliateChooseAddAction() {
  }

  public ActionForward perform(ActionMapping mapping,
                 ActionForm form,
                 HttpServletRequest request,
                 HttpServletResponse response,
		 		 UserSecurityData usd)
    throws Exception {

		String target = "viewChooseAdd";
        int empAffPk = 0;
        try {
            empAffPk = Integer.valueOf((String) request.getParameter("empAffPk")).intValue();
            if (empAffPk == 0)
            	empAffPk = Integer.valueOf((String) request.getAttribute("empAffPk")).intValue();
            //setCurrentAffiliatePk(request, empAffPk);
            request.setAttribute("empAffPk", ""+empAffPk);
        }
        catch (NumberFormatException nfe) {
            //empAffPk = getCurrentAffiliatePk(request);
            nfe.printStackTrace();
        }

        if (empAffPk == 0) {
            throw new JspException("No primary key was specified in the request.");
        }

        AffiliateChooseAddForm acaf = new AffiliateChooseAddForm();
        EmployerData data = s_maintainAffiliates.getEmployerData(empAffPk);
        if (data == null) {
            throw new JspException("No affiliate found with empAffPk = " + empAffPk);
        }

        // Set form fields from EmployerData
        acaf.setType(data.getType());
        acaf.setState(data.getState());
        acaf.setCouncil(""+data.getCouncil());
        acaf.setLocal(""+data.getLocal());
        acaf.setChapter(data.getChapter());
        acaf.setEmployer(data.getEmployer());
        acaf.setStatus((data.getStatus().trim().equalsIgnoreCase("1")) ? "Yes" : "No");
        acaf.setEmpAffPk(data.getEmpAffPk());
        acaf.setExisting_year(data.getExisting_year());

        // needed for header and footer tags
        // setCurrentAffiliate(request, data);

        // needed for jsp
        request.setAttribute("affiliateChooseAdd", acaf);
        //request.setAttribute("employerData", data);

        request.setAttribute("existingYear", data.getExisting_year());

        //java.util.Calendar cal = new java.util.GregorianCalendar();
        //int currentDuesYear = cal.get(java.util.Calendar.YEAR) + 1;
        int currentDuesYear = Integer.parseInt((String) request.getSession().getAttribute("currentDuesYear"));

        String empEditable = "yes";
        String empActive = "yes";

        for (int i = 0; i < data.getExisting_year().size(); i++) {
                String tmpYear = (String) data.getExisting_year().get(i);

          		if (Integer.valueOf(tmpYear).intValue() < currentDuesYear)
                	empEditable = "no";
        }

		if (data.getStatus().trim().equalsIgnoreCase("0"))
			empActive = "no";

        request.setAttribute("empEditable", empEditable);
        request.setAttribute("empActive", empActive);

        return mapping.findForward(target);
  }
}
