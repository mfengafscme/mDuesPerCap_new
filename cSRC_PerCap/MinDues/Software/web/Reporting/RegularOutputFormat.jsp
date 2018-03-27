<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Select Output Format", help = "RegularOutputFormat.html";%>
<%@ include file="../include/header.inc" %> 
<bean:define id="form" name="regularReportForm" scope="session" type="org.afscme.enterprise.reporting.base.web.RegularReportForm"/>

<SCRIPT language="JavaScript">

function disableOtherOptions() {
	window.document.forms[0].optionTab.checked = false;
	window.document.forms[0].optionTab.disabled = true;
	window.document.forms[0].optionComma.checked = false;
	window.document.forms[0].optionComma.disabled = true;
	window.document.forms[0].optionSemicolon.checked = false;
	window.document.forms[0].optionSemicolon.disabled = true;
        <% if (!form.isDataUtility()) { %>
	window.document.forms[0].optionPDF.checked = false;
	window.document.forms[0].optionPDF.disabled = true;
        <% } %>
}

function enableOtherOptions() {
	window.document.forms[0].optionTab.checked = true;
	window.document.forms[0].optionTab.disabled = false;
	window.document.forms[0].optionComma.disabled = false;
	window.document.forms[0].optionSemicolon.disabled = false;
        <% if (!form.isDataUtility()) { %>
	window.document.forms[0].optionPDF.disabled = false;
        <% } %>
}

</SCRIPT>

<html:form action="/startRegularReportGeneration">
  
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
              <html:radio name="form" property="media" value="SCREEN" styleId="optionScreen" onclick="disableOtherOptions()"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionScreen">Send Report To Screen</label>
            </td>
          </tr>
        </table>
      </td>
    </tr>	
    <tr>
      <td>
        <table border="0" class="InnerContentTable">
        <% if (form.isDataUtility()) { %>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="SAVE" styleId="optionSave" onclick="enableOtherOptions()"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="option Save">Save Report As</label>
            </td>
          </tr>        
        <% } else { %>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="PRINT" styleId="optionPrint" onclick="disableOtherOptions()"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="option Print">Print Report on Paper</label>
            </td>
          </tr>        
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="CD" styleId="optionCD" onclick="enableOtherOptions()"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionCD">Extract Report to CD</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="DISKETTE" styleId="optionDisc" onclick="enableOtherOptions()"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionDisc">Extract Report to Diskette</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="TAPE" styleId="optionTape" onclick="enableOtherOptions()"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionTape">Extract Report to Tape</label>
            </td>
          </tr>
        <% } %>
        </table>
      </td>
      <td>
        <table border="0" class="InnerContentTable">
	        <tr> 
            <td colspan=2><B>Select&nbsp;&nbsp;<I>(format to be used for extracting report)</I></B></td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="Tab" styleId="optionTab" disabled="true"/>
            </td>
            <td>&nbsp;&nbsp;
              <LABEL for="optionTab">Tab Delimited</LABEL>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="Comma" styleId="optionComma" disabled="true"/>
            </td>
            <td>&nbsp;&nbsp;
              <LABEL for="optionComma">Comma Delimited</LABEL>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="Semicolon" styleId="optionSemicolon" disabled="true"/>
            </td>
            <td>&nbsp;&nbsp;
              <LABEL for="optionSemicolon">Semicolon Delimited</LABEL>
            </td>
          </tr>
        <% if (!form.isDataUtility()) { %>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="outputFormat" value="PDF" styleId="optionPDF" disabled="true"/>
            </td>
            <td>&nbsp;&nbsp;
              <LABEL for="optionPDF">PDF</LABEL>
            </td>
          </tr>
        <% } %>
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
  <html:submit property="button" value="Generate Report" styleClass="button"/>&nbsp;&nbsp;
  <html:submit property="button" value="Cancel" styleClass="button"/>
</td>
</tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %> 