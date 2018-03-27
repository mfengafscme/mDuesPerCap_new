<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "", help = "MainMenu.html";%> 
<% request.setAttribute("onload", "initForm();"); %> 
<%@ include file="../include/minimumduesheader.inc" %>

<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-blue.css" title="winter" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-brown.css" title="summer" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-green.css" title="green" />
<link rel="stylesheet" type="text/css" media="all" href="css/calendar-win2k-1.css" title="win2k-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-2.css" title="win2k-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-1.css" title="win2k-cold-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-system.css" title="system" />

<script type="text/javascript" language="JavaScript" src="js/minimumduesform.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendarhelper.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar-en.js"></script>
<script type="text/javascript" language="JavaScript" src="js/edit_employer_form.js"></script>

<body background="images/background.jpg">
<html:errors />
<br>
<img src="images/yellow.jpg" width="7" height="11" border="1"> &nbsp;&nbsp;Required Field
<br><br><br>
<html:form action="/EditDelEmployer.do">
<bean:define id="editEmployer" name="editEmployer" type="org.afscme.enterprise.minimumdues.web.EditDelEmployerForm"/>

      
  <html:hidden name="editEmployer" property="empAffPk"/>   
  <html:hidden property="duesyear" value='<%= (String) request.getSession().getAttribute("currentDuesYear") %>' />
  
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
     <td width="99">Type:
	    <html:select name="editEmployer" property="affIdType" onblur="empTypeChanged();" onchange="empTypeChanged();">
		<html:option value=""></html:option>
		<html:option value="L">L</html:option>
		<html:option value="U">U</html:option>
		<!-- <html:option value="C">C</html:option> -->
	    </html:select>
    </td>
    <td width="20"> &nbsp;&nbsp; </td>
    <td width="99">State: 
      <html:select name="editEmployer" property="affIdState">
        <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/> 
      </html:select>
    </td>
    <td width="20"> &nbsp;&nbsp; </td>
    <td width="204">Council: 
      <html:text name="editEmployer" property="affIdCouncil" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="10" maxlength="10"/>
    </td>
    <td width="165">Local: 
      <html:text name="editEmployer" property="affIdLocal" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="10" maxlength="10"/>
    </td>
    <td width="289">Sub Unit: 
      <html:text name="editEmployer" property="affIdSubUnit" size="10" maxlength="10"/>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr> 
    <td colspan="5">Name of Employer: 
      <html:text name="editEmployer" property="employerName" size="60" maxlength="60"/>    
    </td>
    <td>Active: &nbsp;&nbsp;&nbsp;
	  <html:select name="editEmployer" property="affIdStatus">
           	<html:option value="1">Yes</html:option>
    		<html:option value="0">No</html:option>
      </html:select>   
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
        <td align="center" colspan="5"><html:submit property="saveButton" styleClass="button" onclick="return checkAllFieldsBeforeSubmit()">&nbsp;Save&nbsp;</html:submit></td>
        <td align="left"><html:submit property="cancelEditEmpButton" styleClass="button">Cancel</html:submit></td>
  </tr>
  </table>

</html:form>
<br>
<br>
<%@ include file="../include/footer.inc" %> 

