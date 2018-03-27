<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Generate Mailing Lists Main Menu", help = "ListMailingListReports.html";%>
<%@ include file="../include/header.inc" %> 
      
  <TABLE align="center" valign=TOP cellpadding="0" cellspacing="0" border="0" class="ContentTable">
    <tr>
      <td>
        <form name="filterDuplicateForm" action="">
            <input type="checkbox" name="cbxFilterDuplicate" value="filter" id="dup"></input>
            <label for="dup">Do Not Send Duplicate Addresses</label>
        </form>
      </td>
    </tr>

    <tr>
      <td align="center" nowrap><b>Specialized Mailing List Reports</b></td> 
    </tr>  
  
    <TR>
      <TD align="center">
        <!-- This table list all the specialized regular reports the user has access to. -->
        <TABLE align="center" width="95%" border="1" cellpadding=1 cellspacing=1 class="InnerContentTable">
          <TR>
            <TH align="center" nowrap>Name</TH>
            <TH align="center" nowrap>Description</TH>
            <TH align="center" nowrap>Last Updated User ID</TH>
            <TH align="center" nowrap>Last Updated Date</TH>
          </TR>
          
          <logic:present name="specializedReports" scope="request">
          <logic:iterate id="reportData" name="specializedReports" scope="request" type="org.afscme.enterprise.reporting.base.access.ReportData">
          <tr>
            <td>
              <a href="javascript:document.location='/runSpecializedReport.action?rpk=<bean:write name="reportData" property="pk"/>&name=\'<bean:write name="reportData" property="name"/>\'&filterDuplicateAddresses='+document.filterDuplicateForm.cbxFilterDuplicate.checked">
               <bean:write name="reportData" property="name"/> 
             </a>
            </td>
            <td>
              <bean:write name="reportData" property="description"/>
            </td>
            <td>
              <bean:write name="reportData" property="lastUpdateUID"/>
            </td>
            <td>
              <bean:write name="reportData" property="lastUpdateDate"/>
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
      <td align="center" nowrap><b>My Mailing Lists</b></td> 
    </tr>    
    <TR>
      <TD align="center">
        <!-- This table list all the regular reports the user owns . -->
        <TABLE align="center" width="95%" border="1" cellpadding=1 cellspacing=1 class="InnerContentTable">
          <TR>
            <TH align="center" nowrap>Name</TH>
            <TH align="center" nowrap>Description</TH>
            <TH align="center" nowrap>Last Updated User ID</TH>
            <TH align="center" nowrap>Last Updated Date</TH>
          </TR>
          
          <logic:present name="myReports" scope="request">
          <logic:iterate id="reportData" name="myReports" scope="request" type="org.afscme.enterprise.reporting.base.access.ReportData">
          <bean:define id="reportPK" value="<%=reportData.getPk().toString()%>"/>
          <tr>
            <td>
                <a href="javascript:document.location='/showMailingOutputFormat.action?rpk=<bean:write name="reportData" property="pk"/>&filterDuplicateAddresses='+document.filterDuplicateForm.cbxFilterDuplicate.checked">
                   <bean:write name="reportData" property="name"/> 
                </a>
            </td>
            <td>
              <bean:write name="reportData" property="description"/>
            </td>
            <td>
              <bean:write name="reportData" property="lastUpdateUID"/>
            </td>
            <td>
              <bean:write name="reportData" property="lastUpdateDate"/>
            </td>
          </tr>
          </logic:iterate>
          </logic:present>
        </TABLE>
      </TD>
    </TR>       


</TABLE>      

<%@ include file="../include/footer.inc" %> 
