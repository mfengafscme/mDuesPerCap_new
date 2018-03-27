<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<% request.setAttribute("onload", "initForm();"); %> 

<%@ page import="org.apache.struts.action.Action" %> 
<%@ page import="org.apache.struts.action.ActionErrors" %>

<%!String title = "", help = "MainMenu.html";%>
<%@ include file="../include/minimumduesheader.inc" %>

<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-blue.css" title="winter" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-brown.css" title="summer" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-green.css" title="green" />
<link rel="stylesheet" type="text/css" media="all" href="css/calendar-win2k-1.css" title="win2k-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-2.css" title="win2k-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-1.css" title="win2k-cold-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-system.css" title="system" />

<script type="text/javascript" language="JavaScript" src="js/mdues_dataentry_form.js"></script>
	
<body background="images/background.jpg">
<div align="center"><font color="#808000" size="5"><strong>Wage Increase Form </strong></font><br>
</div>
<br>
<html:form action="/DataEntry.do">
  <B>YEAR:</B> 
  <html:select property="year">
     <%   
	  //java.util.Calendar cal = new java.util.GregorianCalendar();
	  //int currentDuesYear = cal.get(java.util.Calendar.YEAR) + 1; 
	  int currentDuesYear = Integer.parseInt((String) request.getSession().getAttribute("currentDuesYear")); 
                
	  int fiveYearAhead = currentDuesYear + 4;

	  java.util.ArrayList otherYearList = new java.util.ArrayList();
	  for (int i = currentDuesYear; i <= fiveYearAhead; i++) { 
      %>
	    <html:option value="<%= ""+i %>"></html:option>
      <% } %>  
  </html:select>

  <BR>
  <BR>
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
    <td width="99">Type:
          <html:select property="affIdType" name="DataEntryForm" onblur="empTypeChanged();" onchange="empTypeChanged();">
                <html:option value=""></html:option>
                <html:option value="L">L</html:option>
		<html:option value="U">U</html:option>
		
	  </html:select>
    </td>
    <td width="165">Local: 
      <html:text property="affIdLocal" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="10" maxlength="10"/> </td>
    <td width="99">State: 
      <html:select property="affIdState">
        <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/> 
      </html:select> </td>
    <td width="40"> <afscme:affiliateFinder formName="DataEntryForm" affIdStateParam="affIdState" affIdCodeParam="affCode"/> 
    </td>
    <td width="20"> &nbsp;&nbsp; </td>
    <td width="200">Sub Unit: 
      <html:text property="affIdSubUnit" size="10" maxlength="10" onblur="changeAffIdType();"/> </td>
    <td width="204">Council: 
      <html:text property="affIdCouncil" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="10" maxlength="10"/> </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr> 
    <td colspan="5">Name of Employer: 
      <html:text property="employerName" size="60" maxlength="60"/> 
    </td>
  </tr>
  </table>
  <br><br><br>
    
  <logic:notPresent name="<%=Action.ERROR_KEY%>"> 
  <table width="90%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td align="center"><html:submit property="enterDataButton" styleClass="button" onclick="return checkAllFieldsBeforeSubmit();">Enter Data</html:submit></td>
	  <td align="left">&nbsp;<html:reset styleClass="button"/>&nbsp;</td>
    </tr>
  </table>
  </logic:notPresent>
  
  <logic:present name="<%=Action.ERROR_KEY%>"> 
  <table width="90%" border="0" cellspacing="0" cellpadding="1">
    <tr>
	<td align="center"><B><html:errors /></B></td>

	<logic:present name="addEmployer"> 
      		<td align="right"><html:submit property="addEmployerButton" styleClass="button" onclick="return checkAllFieldsBeforeAddEmployer();">&nbsp;Add&nbsp;</html:submit></td>
      		<td>&nbsp;&nbsp;</td>
      		<td align="left"><afscme:button page="/DataEntry.action">Cancel</afscme:button></td>
      	</logic:present>
	<logic:present name="inActive"> 
      		<td align="right"><html:submit property="activateEmployerBtn" styleClass="button">&nbsp;Yes</html:submit></td>
      		<td>&nbsp;&nbsp;</td>
      		<td align="left"><afscme:button page="/DataEntry.action">&nbsp;&nbsp;No&nbsp;</afscme:button></td>
      	</logic:present>
	<logic:present name="errorStateCouncilLocal"> 
      		<td align="right"><afscme:button page="/DataEntry.action">New Data Entry Form</afscme:button></td>
      	</logic:present>
    </tr>

  </table>
  </logic:present>
  <br>
</html:form>
<br>
<br>
<%@ include file="../include/footer.inc" %> 

