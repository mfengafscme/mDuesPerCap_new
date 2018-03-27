<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%!String title, help = "Message.html";%>
<% title = (String)request.getAttribute("title"); %>
<%@ include file="../include/header.inc" %>

<center>
    <bean:write name="content"/>
</center>

<%@ include file="../include/footer.inc" %>

