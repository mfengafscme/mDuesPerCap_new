<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%!String title = "Output Fields", help = "OutputFields.html";%>
<% request.setAttribute("onload", "processAllFields();"); %>
<%@ include file="../include/header.inc" %>

<script type="text/javascript" language="JavaScript" src="../js/checkbox.js">
</script>


<script language=javascript>
  function processAllFields() {
    if (document.forms[0].countQuery.checked == true) {
      // disable and uncheck all fields
      for (n=0; n < document.forms[0].selectedOutputFields.length; n++) {
        document.forms[0].selectedOutputFields[n].disabled = true;
        document.forms[0].selectedOutputFields[n].checked = false;
      }
             document.links[2].href = "javascript:restrict()";
             document.links[5].href = "javascript:restrict()";
             document.links[6].href = "javascript:restrict()";
    }
    else {
      // enable all fields
      for (n=0; n < document.forms[0].selectedOutputFields.length; n++) {
        document.forms[0].selectedOutputFields[n].disabled = false;
      }
      document.links[2].href = "javascript:submitForm(document.links[2].name)";
      document.links[5].href = "javascript:submitForm(document.links[5].name)";
      document.links[6].href = "javascript:submitForm(document.links[6].name)";
    }
  }
  
  function atLeastOneChecked() {
    var pass = false;
    var form = window.document.forms[0];
    if (form.countQuery.checked == true) {
      pass = true;
    }
    else {
      for (var i = 0; i < form.selectedOutputFields.length; i++) {
        if (form.selectedOutputFields[i].checked == true) {
          pass = true;
          break;
        }
      }
      
      if (pass == false)
        alert('You must make at least one selection below to proceed.');
    }
     
    return (pass);
  }
  
  function submitForm(linkName) {
    if (atLeastOneChecked()) {
      document.getElementById("linkClicked").setAttribute("value", linkName);
      document.forms[0].submit();
    }
  }
    
  function restrict() {
  	alert("This section does not apply to Count Queries.");
  	return;
  }

  </script>

<html:form action="/outputFields">
    <bean:define id="form" name="queryForm" scope="session" type="org.afscme.enterprise.reporting.base.web.QueryForm"/>

    <% String pageName = "Output Fields";%>
    <%@ include file="../include/query_wizard.inc" %>
    <table width="95%" align="center">
    <tr>
      <td colspan="2">
        <html:checkbox name="form" property="countQuery" onclick="javascript:processAllFields();" styleId="countQuery"/>
        <label for="countQuery">Count Query</label>
      </td>
    </table>
    <%String formName="queryForm";%>  
    <%String fieldsProperty="selectedOutputFields";%>  
    <%@ include file="../include/field_selection.inc" %>
    
</html:form>

<%@ include file="../include/footer.inc" %>

