<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<html>

<head>
    <link rel="stylesheet" type="text/css" href="../css/afscme.css">
    <title><%=title%></title>
    <SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>
    <SCRIPT language="JavaScript" src="../js/membership.js"></SCRIPT>
</head>

<%String onload =  (String)request.getAttribute("onload"); %>
<% if (onload != null) { %>
    <body onload="<%=onload%>">
<% } else { %>
    <body>
<% } %>

<table class="pageHeader">
	<tr>
        <td rowspan="2"><img src="/images/logoSmall.jpg"></td>
		<td width="100%" align="left">
                <span class="appTitle">AFSCME Minimum Dues Application</span><br>
		    	<span class="appSubTitle">AMERICAN FEDERATION OF STATE, COUNTY AND MUNICIPAL EMPLOYEES, AFL-CIO</span>
		</td>
        <logic:present name="SESSION_USER_SECURITY_DATA" scope="session" property="affiliateName">
            <td valign="top" class="appUserName" nowrap><bean:write name="SESSION_USER_SECURITY_DATA" scope="session" property="affiliateName"/></td>
        </logic:present>
	</tr>
</table>

<h1><%=title%></h1>

<table width="95%" align="center">
<tr>
    <td align="right">
        &nbsp;
    </td>
</tr>
</table>

