<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Query Saved Confirmation", help = "SaveConfirmation.html";%>
<%@ include file="../include/header.inc" %> 

<TABLE align="center" valign=TOP cellpadding="0" cellspacing="0" border="0" class="ContentTable">
  <TR>
    <TD align="center">
      <P><H3>The query is saved.<H3></P>
    </TD>
  </TR>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td align="center">
      <button class='button' onclick='window.location="<html:rewrite page='/listQueries.action'/>"'>Return To Maintain Queries</button>
    </td>
  </tr>
</TABLE>


<%@ include file="../include/footer.inc" %> 