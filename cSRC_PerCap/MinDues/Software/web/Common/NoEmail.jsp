<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%!String title = "Password Not Sent", help = "NoEmail.html";%>
<%@ include file="../include/header.inc" %>

<center>
You do not have an email address in the system, please contact your affiliate.

<logic:present name="AffiliatePhone">
at <bean:write name="AffiliatePhone"/>
</logic:present>
<br><br>
<input type="button" class="button" onClick="window.location='/login.action'" value="Return to Login">
</center>

<%@ include file="../include/footer.inc" %>

