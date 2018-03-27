<%! String title = "", help = "AffiliateDetailAdd.html";%>
<%@ include file="../include/minimumduesheader.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-blue.css" title="winter" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-brown.css" title="summer" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-green.css" title="green" />
<link rel="stylesheet" type="text/css" media="all" href="css/calendar-win2k-1.css" title="win2k-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-2.css" title="win2k-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-1.css" title="win2k-cold-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-system.css" title="system" />

<script type="text/javascript" language="JavaScript" src="js/add_agreement.js"></script>
<script type="text/javascript" language="JavaScript" src="js/date_format_check.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendarhelper.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar-en.js"></script>

<body onload="initForm();">
<!-- background="images/butterflies.jpg"  -->

<div align="center"><font color="#008040" size="5"><strong>New Agreement </strong></font>
</div><br><br><br>

<html:form action="/AddAgreement.do">
<table width="80%" align="center">
	<tr>
      <td width="20%" align="left"><strong><font size="4">Agreement Name:</font></strong></td>
		<td width="80%" align="left">
		  <strong><font size="4"><html:text property="agreementName" size="50" maxlength="50"/></font></strong>
		</td>
	</tr>

    <tr>
		<td align="left"><strong>Starting Date:</strong></td>
		<td width="80%" height="33" align="left">
		  <html:text property="startDate" onblur="validateAgreementDate(this);" size="10" maxlength="10"/>
		  <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('startDate', 'mm/dd/y');">
		</td>
	</tr>

	<tr>
		<td align="left"><strong>Ending Date:</strong></td>
		<td width="80%" height="33" align="left">
		  <html:text property="endDate" onblur="validateAgreementDate(this);" size="10" maxlength="10"/>
		  <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('endDate', 'mm/dd/y');">
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
    <tr>
		<td align="left"><strong>Comments:</strong></td>
		<td width="80%" align="left">
		  <html:text property="comments" size="100" maxlength="100"/>
		</td>
    </tr>
</table>
<br><br><br>

<table width="80%" align="center">
	<tr>
		<td width="50%" align="right"><html:submit property="addNewAgreementBtn" styleClass="button" onclick="return checkAllFieldsBeforeSubmit();">&nbsp;&nbsp;Add&nbsp;&nbsp;</html:submit></td>
		<td width="2%">&nbsp;</td>
		<td width="48%" align="left"><html:reset styleClass="button"/></td>
    </tr>
</table>

</html:form>
<br><br><br>
<table width="80%" align="center">
<tr><td align="center"><html:errors /></td></tr>
</table>
</body>

<br><br><br>
<%@ include file="../include/footer.inc" %>
