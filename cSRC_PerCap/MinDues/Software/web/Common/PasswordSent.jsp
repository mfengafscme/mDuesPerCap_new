<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Password Sent", help = "PasswordSend.html";%>
<%@ include file="../include/header.inc" %>

<center>
Your password has been sent to your email address.<br><br>
<input type="button" class="button" onClick="window.location='/login.action'" value="Return to Login">
</center>

<%@ include file="../include/footer.inc" %>


