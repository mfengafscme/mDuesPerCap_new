<%-- Program Name: NonMailableRoster.jsp
     Date Written: 20031024
     Author      : Kyung A. Callahan
     Description : This collects user input prior to generating the report.
     Note: 
     Maintenance : Kyung 20031024 created
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%! String title = "Non Mailable Roster", help = "NonMailableRoster.html";%>
<%@ include file="../../include/header.inc" %> 
<html:form action="/nonMailableRosterReport">
<p><i>This report will show the active members with bad address.</i></p>
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