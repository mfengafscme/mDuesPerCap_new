<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%!String title = "Password Coul Not Be Sent", help = "PasswordNotSend.html";%>
<%@ include file="../include/header.inc" %>

<center>
Your do not have an challenge question selected, please call your affiliate at <%=request.getAttribute("AffiliatePhone")%>
</center>

<%@ include file="../include/footer.inc" %>

