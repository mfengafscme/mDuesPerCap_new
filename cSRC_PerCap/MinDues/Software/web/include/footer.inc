<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<br>
<center class="footer">
    <% if (session.getAttribute("SESSION_USER_SECURITY_DATA") != null) { %>
        <html:link styleClass="footer" forward="Logout">LOGOUT</html:link>&nbsp;|&nbsp;
    <%}%>
    <a class="footer" href="http://www.afscme.org">AFSCME</a> 

</center>

</body>

</html>
