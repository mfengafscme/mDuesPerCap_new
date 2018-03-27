<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%! String title = "Person Mailing List by Mail Code", help = "MailingListInput.html";%>
<%@ include file="../../include/header.inc" %> 

<html:form action="mailingListReport">
    <html:hidden property="filterDuplicateAddresses" value='<%=(String)request.getParameter("filterDuplicateAddresses")%>'/>
	Mailing List:
 	<html:select property="mailingListPk">
		<afscme:mailingListOptions type="person" allowNull="true" nullDisplay="[Select a Mailing List]"/>
	</html:select>
        <bean:define id="reportName" name="ReportName" />
        <%session.setAttribute("ReportName", reportName);%>
	<html:submit styleClass="BUTTON"/> <BR>
</html:form>
<html:errors property="mailingListPk"/>
<html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
<%@ include file="../../include/footer.inc" %> 
