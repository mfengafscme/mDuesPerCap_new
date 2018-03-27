<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Mailing List Output Format", help = "MailingOutputFormat.html";%>
<%@ include file="../include/header.inc" %> 

<html:form action="/startMailingReportGeneration">
  <bean:define id="form" name="mailingReportForm" scope="session" type="org.afscme.enterprise.reporting.base.web.MailingReportForm"/>
  <html:hidden property="filterDuplicateAddresses"/>
<TABLE align="center" valign=TOP cellpadding="0" cellspacing="0" border="0" class="ContentTable">
<tr>
<td>
  <table width="60%" align="center" border=3 class="BodyContentNoWidth">
    <tr> 
      <td colspan=2><B>Select</B></td>
    </tr>
    <tr> 
      <td colspan=2>
        <table border="0" class="InnerContentTable">
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="MailMerge" styleId="optionMailMerge" />
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionMailMerge">Mail Merge File</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="MailingHouseFile" styleId="optionMailingHouseFile"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionMailingHouseFile">Mailing House File</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="Labels" styleId="optionLabels"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionLabels">Labels</label>
            </td>
          </tr>          
        </table>
      </td>
    </tr>
  </table>	
</td>
</tr>
<tr>
<td><br></td>
</tr>
<tr>
<td align="right">
  <html:submit property="button" value="Generate Mailing List" styleClass="button"/>&nbsp;&nbsp;
  <html:submit property="button" value="Cancel" styleClass="button"/>
</td>
</tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %> 