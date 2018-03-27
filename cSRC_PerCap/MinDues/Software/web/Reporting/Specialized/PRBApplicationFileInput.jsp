<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Political Rebate Application File Report", help = "PoliticalRebateApplicationFileReport.html";%>
<%@ include file="../../include/header.inc" %> 

<html:form action="prbApplicationFileReport">
<p><i>This report prepare information for printing onto Rebate Applications</i></p>

Application Mailed Date:
 <html:text property="appMailedDate" size="10" maxlength="10"/>
 <A href="javascript:show_calendar('prbApplicationFileForm.appMailedDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
 <html:errors property="appMailedDate"/>
 <BR>

<html:submit styleClass="BUTTON"/>

</html:form>

<%@ include file="../../include/footer.inc" %> 