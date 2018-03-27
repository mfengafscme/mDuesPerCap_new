<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<%!String title = "", help = "MainMenu.html";%>
<% request.setAttribute("onload", "initForm();"); %>
<%@ include file="../include/minimumdues_simple_header.inc" %>

<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-blue.css" title="winter" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-brown.css" title="summer" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-green.css" title="green" />
<link rel="stylesheet" type="text/css" media="all" href="css/calendar-win2k-1.css" title="win2k-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-2.css" title="win2k-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-1.css" title="win2k-cold-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-system.css" title="system" />

<script type="text/javascript" language="JavaScript" src="js/mdues_dataview_form.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendarhelper.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar-en.js"></script>
	
<body background="images/background.jpg">

<% 
int empAffPk = 0;
String showSearchResult = null;

if (request.getParameter("empAffPk") != null)
	empAffPk = Integer.valueOf(((String) request.getParameter("empAffPk"))).intValue(); 
else if ( (empAffPk == 0) && (request.getAttribute("empAffPk") != null))
	empAffPk = Integer.valueOf((String) request.getAttribute("empAffPk")).intValue(); 
	
showSearchResult = (String) request.getAttribute("showSearchResult"); 
%> 

<table width="95%" align="center">
<tr>
	<td width="90%" align="right">
		<% 
		String url = null;
		if (showSearchResult != null)
			url = "/AffiliateChooseAdd.action?empAffPk=" + empAffPk + "&showSearchResult"; 
		else
			url = "/AffiliateChooseAdd.action?empAffPk=" + empAffPk; 
		%>
		<afscme:link page="<%=url%>" styleClass="action" title="Employer Data">Employer Data</afscme:link>
	</td>
	<td align="right">
		<afscme:link forward="MainMenu" title="Return to AFSCME Main Menu" styleClass="action" postfix="&nbsp;&nbsp;">Main Menu</afscme:link> 
	</td>
</tr>
</table>

<font size="3" color ="blue"> 
<div align="center"><font color="#808000" size="5"><strong>Wage Increase Entry </strong></font><br>
</div>
<html:form action="/EditDataEntry.do">
<bean:define id="viewDataEntry" name="viewDataEntry" type="org.afscme.enterprise.minimumdues.web.ViewDataEntryForm"/>

  <html:hidden name="viewDataEntry"   property="empAffPk"/> 
  <html:hidden name="viewDataEntry"   property="viewYear"/>   
  <html:hidden name="viewDataEntry"   property="h_amountType"/> 
  <html:hidden name="viewDataEntry"   property="h_inNegotiations"/> 
  <html:hidden name="viewDataEntry"   property="h_formCompleted"/> 
  <html:hidden name="viewDataEntry"   property="h_correspondence"/> 
  <html:hidden name="viewDataEntry"   property="h_agreementReceived"/>
  <html:hidden name="viewDataEntry"   property="agreementPk"/>

<% 
String empActive = null;

if (request.getParameter("empActive") != null)
	empActive = (String) request.getParameter("empActive"); 
else if (request.getAttribute("empActive") != null)
	empActive = (String) request.getAttribute("empActive"); 
%> 

  <html:hidden property="empActive" value='<%= empActive %>' />
	  
  YEAR:
  <bean:write name="viewDataEntry" scope="request" property="viewYear"/>
  <br><br>
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
    <td width="99">Type: 
      <bean:write name="viewDataEntry" scope="request" property="affIdType"/>
    </td>    
    <td width="165">Local: 
      <bean:write name="viewDataEntry" scope="request" property="affIdLocal"/>
    </td>
    <td width="99">State: 
      <bean:write name="viewDataEntry" scope="request" property="affIdState"/>
    </td>
    <td width="200">Sub Unit: 
      <bean:write name="viewDataEntry" scope="request" property="affIdSubUnit"/>
    </td>
    <td width="20"> &nbsp;&nbsp; </td>
    <td width="204">Council: 
      <bean:write name="viewDataEntry" scope="request" property="affIdCouncil"/>
    </td>
  </tr>
  <tr> 
    <td colspan="5" align="left">Name of Employer: 
      <bean:write name="viewDataEntry" scope="request" property="employerName"/>
    </td>
	<td align="left">Active: 
      <bean:write name="viewDataEntry" scope="request" property="affIdStatus"/>
    </td>
  </tr>
  </table>
  </font>
  
  <br>

  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
    <td colspan="5">Total Number of Members and Fee payers: <bean:write name="viewDataEntry" scope="request" property="numberOfMember"/> 
    </td>
    <td>STAT Average: 
      <bean:write name="viewDataEntry" scope="request" property="statAverage"/>
    </td>
    <td>Membership Count: 
      <bean:write name="viewDataEntry" scope="request" property="membershipCt"/> 
     </td>
  </tr></table>
  <table width="100%">
    <tr> 
      <td colspan= "40">Duration of Agreement: &nbsp;</td> 
      <td colspan= "3">In Negotiations: &nbsp;  
      <html:radio property="inNegotiations" value="no"  />No&nbsp;&nbsp;&nbsp; 
      <html:radio property="inNegotiations" value="yes"  />Yes 
      </td> 
    </tr>
    <tr> 
      <td colspan= "40"> 
        From: &nbsp;<bean:write name="viewDataEntry" scope="request" property="durationFrom"/> 
      </td>
      <td colspan= "3"> 
        To: &nbsp;<bean:write name="viewDataEntry" scope="request" property="durationTo"/>   
      </td>
    </tr>
  </table>
  <br>
  <table width="100%" border="1" cellspacing="0" cellpadding="1">
    <tr> 
      <th>A.</th>
      <th colspan="6" align="left">Percent Wage Increases</th>
      <th colspan= "9" align="left">C. Other Wage Adjustments</th>
    </tr>
    <tr> 
	  <td>&nbsp;</td>
      <td height="41" colspan="3">Percentage Increase</td>
      <td>Effective</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td>Type of Payment</td>
      <td>Percentage Increase<br>
        Adjustment</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td colspan= "2">Members Times <br>
        Increase</td>
    </tr>
    <logic:iterate name="viewDataEntry" property="percentWageIncList" id="percentWageInc" type="org.afscme.enterprise.minimumdues.PercentWageIncBean"> 
    <tr> 
      <td>&nbsp;</td>
      <TD height="41" colspan="3"><bean:write name="percentWageInc" property="percent_a" /></TD>
      <TD><bean:write name="percentWageInc" property="effective_a" /></TD>
      <TD colspan= "2"><bean:write name="percentWageInc" property="noOfMember_a" /></TD>
      <TD><bean:write name="percentWageInc" property="typeOfPayment_adj_a" /></TD>
      <TD><bean:write name="percentWageInc" property="percentInc_adj_a" /></TD>
      <TD colspan= "2"><bean:write name="percentWageInc" property="noOfMember_adj_a" /></TD>
      <TD colspan= "2"><bean:write name="percentWageInc" property="mbrTimesInc_a" /></TD>
    </tr>
    </logic:iterate> 
  </table>
  
  <br>
  <br>
  
  <logic:present name="viewDataEntry" property="secType">
  <logic:equal name="viewDataEntry" property="secType" value="B">
  <table width="100%" border="1" cellspacing="0" cellpadding="1">
    <tr> 
      <th>B.</th>
      <th colspan="6" align="left">Amount Wage Increase</th>
      <th colspan= "9" align="left">C. Other Wage Adjustments</th>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td colspan="3"> Amount Increase<br> <html:radio property="amountType" value="cent/hr" />
        cent/hr<br> <html:radio property="amountType" value="dollar/yr" />
        dollar/yr </td>
      <td>Effective</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td>Type of Payment</td>
      <td>Amount Increase<br>
        Adjustment</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td colspan= "2">Members Times <br>
        Increase</td>
    </tr>
    <logic:iterate name="viewDataEntry" property="amountWageIncList" id="amountWageInc" type="org.afscme.enterprise.minimumdues.AmountWageIncBean"> 
    <tr> 
      <td>&nbsp;</td>
      <td height="41" colspan="3"><bean:write name="amountWageInc" property="amountInc_b" /></td>
      <td><bean:write name="amountWageInc" property="effective_b" /></td>
      <td colspan= "2"><bean:write name="amountWageInc" property="noOfMember_b" /></td>
      <td><bean:write name="amountWageInc" property="typeOfPayment_adj_b" /></td>
      <td><bean:write name="amountWageInc" property="amountInc_adj_b" /></td>
      <td colspan= "2"><bean:write name="amountWageInc" property="noOfMember_adj_b" /></td>
      <td colspan= "2"><bean:write name="amountWageInc" property="mbrTimesInc_b" /></td>
    </tr>
    </logic:iterate> 
  </table>
  </logic:equal>

  <logic:notEqual name="viewDataEntry" property="secType" value="B">
  <h5>No Section B Data Available.</h5>
  </logic:notEqual>
  </logic:present>

  <br>
  <br>
  <br>
  
  Average Wages: &nbsp;&nbsp;&nbsp; 
  <bean:write name="viewDataEntry" scope="request" property="averageWage"/>
  <br>
  <br>
  
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>Agreement Received: &nbsp;&nbsp;&nbsp; 
			<html:checkbox property="agreementReceived" value="no"/>
            <!-- <bean:write name="viewDataEntry" scope="request" property="agreementReceived"/> -->
        </td>
        <td>Agreement Description: 
           <bean:write name="viewDataEntry" scope="request" property="agreementDesc"/>	
    	</td>
      </tr>
  </table>
  <br>  
   
  <table width="80%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td width="31%">Name of Person Completing Form:</td>
      <td width="28%" align="left"><bean:write name="viewDataEntry" scope="request" property="personName"/></td>
      <td width="1%">&nbsp;</td>
      <td width="29%">Form Completed:</td>
      <td width="1%"><html:checkbox property="formCompleted" value="no"/></td>
    </tr>
    <tr> 
      <td>Telephone Number:</td>
      <td align="left"><bean:write name="viewDataEntry" scope="request" property="telephone"/></td>
      <td>&nbsp;</td>
      <td>Correspondence:</td>
      <td><html:checkbox property="correspondence" value="no"/></td>
      <td width="9%">Date:</td>
      <td width="1%"><bean:write name="viewDataEntry" scope="request" property="correspondenceDate"/></td>
    </tr>
    <tr> 
      <td>Email:</td>
      <td align="left"><bean:write name="viewDataEntry" scope="request" property="email"/></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>
  
  <br>
  
<table width="100%">
    <tr> 
     <td width="10%" align="left">Comments:</td>
     <td width="90%" align="left"><bean:write name="viewDataEntry" scope="request" property="comments"/></td>
    </tr>
   <tr> 
      <td>&nbsp;</td>
   </tr>
   <tr> 
      <td width="10%" align="left">Agreement:</td>
      <td width="90%" align="left"><font size="3"><strong><a href="javascript:viewAgreement_V()"><bean:write name="viewDataEntry" scope="request" property="agreementName"/></a></strong></font></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>
  <br>
  <table width="100%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td align="right"><html:submit property="editButton" styleClass="button" onclick="return checkActive();">&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;</html:submit></td>
      <td>&nbsp;</td>
      <td align="left">
      <html:submit property="deleteButton" onclick="return confirmDeleteEmpYear();" styleClass="button">Delete</html:submit> 
      <!-- <afscme:button page='<%="/deleteEmpYearFormData.do?empAffPk="+viewDataEntry.getEmpAffPk()+"&year="+viewDataEntry.getViewYear()%>'>Delete</afscme:button> -->
      </td>
    </tr>
  </table>
  <br>
</html:form>
<br>
<br>
<%@ include file="../include/footer.inc" %> 
