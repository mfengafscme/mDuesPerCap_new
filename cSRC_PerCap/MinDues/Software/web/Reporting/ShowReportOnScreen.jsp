<%@ page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<% String title = (String)request.getAttribute("reportName");%>
<% String help = "ReportOnScreen.html"; %>
<%@ include file="../include/header.inc" %> 


<html:form action="/onScreenGeneration">
  <bean:define id="form" name="regularReportForm" scope="session" type="org.afscme.enterprise.reporting.base.web.RegularReportForm"/>
  <html:hidden property="pageName" value="On Screen"/>

  <table width="95%" align="center" valign=TOP cellpadding="0" cellspacing="3" border="0" class="ContentTable">
    <tr> 
      <td align="right"><BR>
        <html:submit property="button" value="Return To Generate Reports" styleClass="button"/>
      </td>
    </tr>   
  </table>
         
  <BR>
  <%-- start the actual query result for preview --%>
  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">  
    <tr>
      <logic:iterate id="fieldName" name="fieldNames">
      <th nowrap><bean:write name="fieldName"/></th>
      </logic:iterate>
    </tr>
    <logic:iterate id="row" name="reportResult" type="java.util.List">
    <tr>
      <logic:iterate id="col" name="row" type="java.lang.String">
      <td nowrap><%=col%></td>
      </logic:iterate>
    </tr>
    </logic:iterate>
  </table>
         
</html:form>

<%@ include file="../include/footer.inc" %> 