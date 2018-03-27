<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "First Name Count", help = "FirstNameCount.html";%>
<%@ include file="../../include/header.inc" %> 

<html:form action="nameCountReport">
<p><i>This report will show a count of the people in each state with a given first name</i></p>

First Name:
 <html:text property="firstName"/>
 <br>

<html:submit styleClass="BUTTON"/>

</html:form>

<%@ include file="../../include/footer.inc" %> 