<%-- Program Name: NcoaAcs.jsp
     Date Written: 20031030
     Author      : Kyung A. Callahan
     Description : This collects user input prior to generating the report.
     Note: 
     Maintenance : 
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%! String title = "NCOA/ACS Report", help = "NcoaAcs.html";%>
<%@ include file="../../include/header.inc" %> 
<html:form action="/ncoaAcsReport">
<p><i>This report will show the change of member's address.</i></p>
Report Type:
<html:select property="reportType" onblur="reportType(this.form);" onchange="reportType(this.form)">
<html:option value="L">Select a Report Type</html:option>
<html:option value="L">Regular</html:option>
<html:option value="S">Retiree</html:option>
</html:select>
<br><br>
<html:submit styleClass="BUTTON"/>
</html:form>
<%@ include file="../../include/footer.inc" %>