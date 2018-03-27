<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Output Fields Order", help = "OutputFieldsOrder.html";%>
<%@ include file="../include/header.inc" %> 

<script language=javascript>
  function submitForm(linkName) {
    document.getElementById("linkClicked").setAttribute("value", linkName);
    document.forms[0].submit();    
  }
</script>
  
<html:form action="/outputFieldsOrder">
  <bean:define id="form" name="queryForm" scope="session" type="org.afscme.enterprise.reporting.base.web.QueryForm"/>

    <% String pageName = "Output Fields Order";%>
    <%@ include file="../include/query_wizard.inc" %>
  
  <% int fieldCount = form.getSelectedOutputFields().length; %>
      
  <% if (!form.isCountQuery()) { %>   
  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">
    <tr>
      <th nowrap>Field</th>
      <th nowrap>Output Order</th>
    </tr>  
    <logic:iterate id="field" name="form" property="outputFieldsForOrder" type="org.afscme.enterprise.reporting.base.access.ReportField">
    <bean:define id="fieldPk" name="field" property="pk"/>
      <%-- For each field --%> 
      <tr>
        <td valign="top" align="left"><bean:write name="field" property="fullName"/></td>
        <td>
          <% String mappedPropertyStr = "fieldOrder(" + fieldPk.toString() + ")"; %>
          <html:select property='<%=mappedPropertyStr%>'>
          <% String v = "";                     
            for (int c=1; c<=fieldCount; c++) { %>
              <html:option value='<%=v+c%>'><%=v+c%></html:option>
          <% } %>
          </html:select>
        </td>
      </tr>
    </logic:iterate>
  </table>
  <% } %>      

</html:form>

<%@ include file="../include/footer.inc" %> 
