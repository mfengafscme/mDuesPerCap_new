<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%!String title = "Selection Criteria Fields", help = "SelectionCriteriaFields.html";%>
<%@ include file="../include/header.inc" %>

<script type="text/javascript" language="JavaScript" src="../js/checkbox.js">
</script>

<script language=javascript>
  function submitForm(linkName) {
    document.getElementById("linkClicked").setAttribute("value", linkName);
    document.forms[0].submit();    
  }
  
  function restrict() {
  	alert("This section does not apply to Count Queries.");
    	return;
  }
</script>

<html:form action="/selectionCriteriaFields">
  <bean:define id="form" name="queryForm" scope="session" type="org.afscme.enterprise.reporting.base.web.QueryForm"/>

    <% String pageName = "Selection Criteria Fields";%>
    <%@ include file="../include/query_wizard.inc" %>
  
    <%String formName="queryForm";%>  
    <%String fieldsProperty="selectedCriteriaFields";%>  
    <%@ include file="../include/field_selection.inc" %>
    
</html:form>

<%@ include file="../include/footer.inc" %>

