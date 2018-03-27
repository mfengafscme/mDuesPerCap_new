<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%! String title = "Output Format", help = "OutputFormatOne.html";%>
<%@ include file="../include/header.inc" %> 

<html:form action="/generateSpecializedReport">
  <bean:define id="form" name="regularReportForm" scope="session" type="org.afscme.enterprise.reporting.base.web.RegularReportForm"/>
  <html:hidden property="filterDuplicateAddresses"/>

  <table width="60%" align="center" border=3 class="BodyContentNoWidth">
    <tr> 
      <td><B>Select</B></td>
    </tr>
    <tr> 
      <td>
        <table border="0" class="InnerContentTable">
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="SCREEN" styleId="optionScreen"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionScreen">Send Report To Screen</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="PRINT" styleId="optionPrint"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionPrint">Print Report on Paper</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="CD" styleId="optionCD"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionCD">Extract Report to CD</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="DISKETTE" styleId="optionDisc"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionDisc">Extract Report to Diskette</label>
            </td>
          </tr>
          <tr>
            <td>&nbsp;&nbsp;
              <html:radio name="form" property="media" value="TAPE" styleId="optionTape"/>
            </td>
            <td>&nbsp;&nbsp;
              <label for="optionTape">Extract Report to Tape</label>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

</html:form>

<%@ include file="../include/footer.inc" %> 
