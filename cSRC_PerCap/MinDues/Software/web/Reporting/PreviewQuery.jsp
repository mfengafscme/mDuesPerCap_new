<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Preview Query", help = "PreviewQuery.html";%>
<%@ include file="../include/header.inc" %> 

<SCRIPT language="JavaScript">

function HandleCorrespondenceFlag() { 
  if (window.document.forms[0].mailingList.checked) {
    window.document.forms[0].updateCorrespondenceHistory.disabled=false;
  }
  else {
    window.document.forms[0].updateCorrespondenceHistory.checked=false;
    window.document.forms[0].updateCorrespondenceHistory.disabled=true;
  }
}

  function submitForm(linkName) {
    document.getElementById("linkClicked").setAttribute("value", linkName);
    document.forms[0].submit();    
  }

</SCRIPT>

<html:form action="/previewQuery">
  <bean:define id="form" name="queryForm" scope="session" type="org.afscme.enterprise.reporting.base.web.QueryForm"/>
  <html:hidden property="pageName" value="Preview Query"/>
  <html:hidden property="linkClicked" value=""/>

  <table width="95%" align="center" valign=TOP cellpadding="0" cellspacing="3" border="0" class="ContentTable">
    <tr>
      <td align=center>
        <h4> 
          <a href="javascript:submitForm('Output Fields')">Output Fields</a> >>
          <a href="javascript:submitForm('Output Fields Order')">Output Fields Order</a> >>    
          <a href="javascript:submitForm('Selection Criteria Fields')">Selection Criteria Fields</a> >>
          <a href="javascript:submitForm('Selection Criteria')">Selection Criteria</a> >>
          <% if (form.isCountQuery()) { %>
          <span class="CurrentWizardPage">Sort Criteria Fields</span> >>
          <span class="CurrentWizardPage">Sort Criteria</span> >>
          <% } else { %>
          <a href="javascript:submitForm('Sort Criteria Fields')">Sort Criteria Fields</a> >> 
          <a href="javascript:submitForm('Sort Criteria')">Sort Criteria</a> >>
          <% } %>          
          <span class="CurrentWizardPage">Preview Query</span>
        </h4>
      </td>
    </tr>
    <tr> 
      <td align="right"><BR>
        <html:submit property="saveButton" value="Save" styleClass="button"/>
        <% if (form.getPk() != null) { %>  <%-- "new" mode --%>
        <html:submit property="saveAsButton" value="Save As" styleClass="button"/>
        <% } else { %>
        <html:submit disabled="true" property="saveAsButton" value="Save As" styleClass="button"/>
        <% } %>        
        <html:cancel styleClass="button">Cancel</html:cancel>
      </td>
    </tr>
  </table>
         
  <table class="BodyContentNoWidth" border="1" cellspacing="0" width="95%" align="center">
    <tr>
      <td>
        <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
      </td>
    </tr>
    <tr>
      <td nowrap>
        <label for="QueryName">Query Name</label>
        <html:text property="name" size="30" styleId="QueryName"/>
      </td>
    </tr>
    <tr>
      <td nowrap><html:errors property="name"/></td>
    </tr>    
    <tr>
      <td nowrap>
        <label for="ReportDesc">Query Description</label>
        <html:text property="description" size="60" styleId="QueryDesc"/>
      </td>
    </tr>
    <tr>
      <td nowrap>
        <html:checkbox property="mailingList" onclick="HandleCorrespondenceFlag()" styleId="MailingList"/>
        <label for="MailingList">Mailing List</label>
      </td>
    </tr>
    <tr>
      <td nowrap>
        <% if (form.isMailingList()) { %>
        <html:checkbox property="updateCorrespondenceHistory" styleId="CorrespondenceHistoryFlag"/>
        <% } else { %>
        <html:checkbox property="updateCorrespondenceHistory" styleId="CorrespondenceHistoryFlag" disabled="true"/>
        <% } %>
        <label for="CorrespondenceHistoryFlag">Update Correspondence History</label>
      </td>
    </tr>
  </table>
  <BR>
<br>
  <%-- start the actual query result for preview --%>
  <% if (!form.isCountQuery()) { %>
  <table border="0" align="center" width="95%">  
    <tr><td><b>Total: <bean:write name="form" property="totalRows"/></b></td></tr>
  </table>
  <% } %>
  <table class="BodyContentNoWidth" border="1" cellspacing="0" align="center" width="95%">  
    <tr>
      <logic:iterate id="fieldName" name="form" property="fieldNames">
      <th nowrap><bean:write name="fieldName"/></th>
      </logic:iterate>
    </tr>
    <logic:iterate id="row" name="form" property="previewResult" type="java.util.List">
    <tr>
      <logic:iterate id="col" name="row" type="java.lang.String">
          <td nowrap><%=col%></td>
      </logic:iterate>
    </tr>
    </logic:iterate>
  </table>
         
</html:form>

<%@ include file="../include/footer.inc" %> 
