<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Officer Credential Cards Input", help = "OfficerCredentialCardsInput.html";%>
<%@ include file="../../include/header.inc" %> 

<html:form action="officerCredentialCardsReport">
	<p><i>This report prepare information for printing onto Rebate Applications</i></p>

	From Date:
	 <html:text property="fromDate" size="10" maxlength="10"/>
	 <A href="javascript:show_calendar('officerCredentialCardsForm.fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
	 <html:errors property="fromDate"/>
	 <BR>
	To Date:
	 <html:text property="toDate" size="10" maxlength="10"/>
	 <A href="javascript:show_calendar('officerCredentialCardsForm.toDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
	 <html:errors property="toDate"/>
	 <BR>

	<html:submit styleClass="BUTTON"/>

</html:form>

<%@ include file="../../include/footer.inc" %> 
