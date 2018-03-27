<%-- Program Name: OfficerExpiration.jsp
     Date Written: 20030915
     Author      : Kyung A. Callahan
     Description : This collects user input prior to generating the Officer Expiration rpt
     Note: 
     Maintenance : Kyung 20030915
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%! String title = "Officer Expiration Report", help = "OfficerExpiration.html";%>
<%@ include file="../../include/header.inc" %> 
<html:form action="/OfficerExpirationReport">
<p><i>This report shows officer's term expiration date.</i></p>
Report Type:
<html:select property="reportType" onblur="reportType(this.form);" onchange="reportType(this.form)">
<html:option value="Select a Report Type List">Select a Report Type List</html:option>
<html:option value="Detail">Detail</html:option>
<html:option value="Summary">Summary</html:option>
</html:select>
<br><br>
<html:submit styleClass="BUTTON"/>
</html:form>
<%@ include file="../../include/footer.inc" %>