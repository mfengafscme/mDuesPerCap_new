<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Sort Criteria", help = "SortCriteria.html";%>
<%@ include file="../include/header.inc" %> 

<script language=javascript>
  function submitForm(linkName) {
    document.getElementById("linkClicked").setAttribute("value", linkName);
    document.forms[0].submit();    
  }
</script>

<html:form action="/sortCriteria">
  <bean:define id="form" name="queryForm" scope="session" type="org.afscme.enterprise.reporting.base.web.QueryForm"/>
    <% String pageName = "Sort Criteria";%>
    <%@ include file="../include/query_wizard.inc" %>
  
  <% int fieldCount = form.getSelectedSortFields().length; %>
       
  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">
    <tr>
      <th nowrap>Field</th>
      <th nowrap>Sort Order</tb>
      <th nowrap>&nbsp;</th>
    </tr>
    <logic:iterate id="field" name="form" property="sortFields" type="org.afscme.enterprise.reporting.base.access.ReportField"> 
    <bean:define id="fieldPk" name="field" property="pk" type="java.lang.Integer"/>  
    <tr>
      <td valign="top" align="left"><bean:write name="field" property="displayName"/></td>
      <td>
        <% String mappedSortOrderKey = "sortOrder(" + fieldPk.toString() + ")"; %>
        <html:select property='<%=mappedSortOrderKey%>'>
        <% String v = "";                     
          for (int c=1; c<=fieldCount; c++) { %>
            <html:option value='<%=v+c%>'><%=v+c%></html:option>
         <% } %>
        </html:select>
      </td>
      <% String mappedPropertyStr="orderDirection(" + fieldPk.toString()+ ")"; %>
      <td>
        <html:radio name="form" property='<%=mappedPropertyStr%>' value="A" styleId="<%=fieldPk.toString()+'a'%>">
          <label for="<%=fieldPk.toString()+'a'%>">Ascending</label>
        </html:radio>&nbsp;&nbsp;
        <html:radio name="form" property='<%=mappedPropertyStr%>' value="D" styleId="<%=fieldPk.toString()+'d'%>">
          <label for="<%=fieldPk.toString()+'d'%>">Descending</label>
        </html:radio>
      </td>
    </tr>
    </logic:iterate>
  </table>
    

</html:form>

<%@ include file="../include/footer.inc" %> 
