<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Generate Reports Main Menu", help = "ListRegularReports.html";%>
<%@ include file="../include/header.inc" %> 
      
  <TABLE align="center" valign=TOP cellpadding="0" cellspacing="0" border="0" class="ContentTable">
    <tr>
      <td align="center" nowrap><b>Specialized Reports</b></td> 
    </tr>    
    <TR>
      <TD align="center">
        <!-- This table list all the specialized regular reports the user has access to. -->
        <TABLE align="center" width="95%" border="1" cellpadding=1 cellspacing=1 class="InnerContentTable">
          <TR>
            <TH align="center" nowrap width="20%">Name</TH>
            <TH align="center" nowrap width="50%">Description</TH>
            <TH align="center" nowrap width="15%">Last Updated User ID</TH>
            <TH align="center" nowrap width="15%">Last Updated Date</TH>
          </TR>
          
          <logic:present name="specializedReports" scope="request">
          <logic:iterate id="reportData" name="specializedReports" scope="request" type="org.afscme.enterprise.reporting.base.access.ReportData">
          <bean:define id="reportPK" value="<%=reportData.getPk().toString()%>"/>
          <tr>
            <td>
              <bean:define id="name1" name="reportData" property="name"/>
              <%String url="/runSpecializedReport.action?rpk="+reportPK+"&name='"+name1+"'";%>
              <html:link page="<%=url%>"  styleClass="action">
                <bean:write name="reportData" property="name"/>
              </html:link>
            </td>
            <td>
              <bean:write name="reportData" property="description"/>
                <logic:empty name="reportData" property="description">&nbsp;</logic:empty>
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
    <tr>
      <td><br></td>
    </tr>
    <tr>
      <td align="center" nowrap><b>My Reports</b></td> 
    </tr>    
    <TR>
      <TD align="center">
        <!-- This table list all the regular reports the user owns . -->
        <TABLE align="center" width="95%" border="1" cellpadding=1 cellspacing=1 class="InnerContentTable">
          <TR>
            <TH align="center" nowrap width="20%">Name</TH>
            <TH align="center" nowrap width="50%">Description</TH>
            <TH align="center" nowrap width="15%">Last Updated User ID</TH>
            <TH align="center" nowrap width="15%">Last Updated Date</TH>
          </TR>
          
          <logic:present name="myReports" scope="request">
          <logic:iterate id="reportData" name="myReports" scope="request" type="org.afscme.enterprise.reporting.base.access.ReportData">
          <bean:define id="reportPK" value="<%=reportData.getPk().toString()%>"/>
          <tr>
            <td>
              <html:link page="/showRegularOutputFormat.action" paramId="rpk" paramName="reportPK" styleClass="action">
                <bean:write name="reportData" property="name"/>
              </html:link>
            </td>
            <td>
              <bean:write name="reportData" property="description"/>
                <logic:empty name="reportData" property="description">&nbsp;</logic:empty>
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


</TABLE>      

<%@ include file="../include/footer.inc" %> 
