<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%! String title = "Political Rebate Check File Report", help = "RebateCheckFileReport.html";%>
<%@ include file="../../include/header.inc" %> 

<html:form action="rebateCheckFileReport">
<p><i>This report provides a listing of the individuals that are getting checks, 
a listing of the members that have received Rebate checks, 
<br>and the final listing of the individuals that were given a Rebate for the current rebate year. 
</i></p>

Starting Check Number:
 <html:text property="checkNumber" size="10" maxlength="10"/>
 <html:errors property="checkNumber"/>
 <BR>

<html:submit styleClass="BUTTON"/>

</html:form>

<%@ include file="../../include/footer.inc" %> 