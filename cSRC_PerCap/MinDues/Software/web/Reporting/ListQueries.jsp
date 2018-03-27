<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Maintain Queries Main Menu", help = "ListQueries.html";%>
<%@ include file="../include/header.inc" %> 
      
  <TABLE align="center" valign=TOP cellpadding="0" cellspacing="0" border="0" class="ContentTable">
    <TR>
      <td align="right">
        <button type="button" class="BUTTON" onclick='window.location="<html:rewrite page='/addQuery.action'/>"'>Add Query</button>
      </td>
    </TR>

    <TR>
      <TD align="center">
        <!-- This table list all the queries the user has created before. -->
        <TABLE align="center" width="95%" border="2" cellpadding=1 cellspacing=1 class="InnerContentTable">
          <TR>
            <TH colspan="2" align="center" nowrap>Action</TH>
            <TH align="center" nowrap>Name</TH>
            <TH align="center" nowrap>Description</TH>
            <TH align="center" nowrap>Last Updated User ID</TH>
            <TH align="center" nowrap>Last Updated Date</TH>
          </TR>
          
          <logic:present name="myQueries" scope="request">
          <logic:iterate id="reportData" name="myQueries" scope="request" type="org.afscme.enterprise.reporting.base.access.ReportData">
          <bean:define id="reportPK" value="<%=reportData.getPk().toString()%>"/>
          <tr>
            <td align="center">
              <html:link page="/editQuery.action" paramId="rpk" paramName="reportPK" styleClass="action"><i>Edit</i></html:link>
            </td>
            <td align="center">
              <html:link page="/deleteQuery.action" paramId="rpk" paramName="reportPK" onclick="return confirm('Are you sure you want to delete this Query?')" styleClass="action"><I>Delete</I></html:link>
            </td>
            <td>
              <bean:write name="reportData" property="name"/>
            </td>
            <td>
              <bean:write name="reportData" property="description"/>
            </td>
            <td>
              <bean:write name="reportData" property="lastUpdateUID"/>
            </td>
            <td>
              <afscme:dateWrite name="reportData" property="lastUpdateDate"/>
            </td>
          </tr>
          </logic:iterate>
          </logic:present>
        </TABLE>
      </TD>
    </TR>

    <TR>
      <TD align="right">
        <button type="button" class="BUTTON" onclick='window.location="<html:rewrite page='/addQuery.action'/>"'>Add Query</button>
      </TD>
    </TR>

</TABLE>      

<%@ include file="../include/footer.inc" %> 
