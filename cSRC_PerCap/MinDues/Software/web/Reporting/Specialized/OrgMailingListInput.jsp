<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Org Mailing List Input", help = "OrgMailingListInput.html";%>
<%@ include file="../../include/header.inc" %> 

<html:form action="orgMailingListReport">
    <html:hidden property="filterDuplicateAddresses" value='<%=(String)request.getParameter("filterDuplicateAddresses")%>'/>
	<p>
		<i>This report will show a list of organizations within a given mailing list</i>
	</p>

	Mailing Lists:
 	<html:select property="mailingListPk">
		<afscme:mailingListOptions type="organization" allowNull="true" nullDisplay="[Select a Mailing List]" format="{1}"/>
	</html:select>
	<html:submit styleClass="BUTTON"/> <BR>
</html:form>
<html:errors property="mailingListPk"/>
<html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
<%@ include file="../../include/footer.inc" %> 
