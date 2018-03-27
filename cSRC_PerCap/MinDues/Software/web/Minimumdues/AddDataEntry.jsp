<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
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

<script type="text/javascript" language="JavaScript" src="js/mdues_dataadd_form.js"></script>
<script type="text/javascript" language="JavaScript" src="js/date_format_check.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendarhelper.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar-en.js"></script>
	
<body background="images/background.jpg">
<div align="center"><font color="#808000" size="5"><strong>Data Entry Form</strong></font><br>
</div>


<html:errors />
<br>

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

<img src="images/yellow.jpg" width="7" height="11" border="1"> &nbsp;&nbsp;Required Field
<br><br>

<bean:define id="addDataEntry" name="addDataEntry" type="org.afscme.enterprise.minimumdues.web.AddDataEntryForm"/>

  <B>YEAR:
  <bean:write name="addDataEntry" scope="request" property="addYear"/></B>
  <br>
  <br>
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
     <td width="99">Type:
		<bean:write name="addDataEntry" scope="request" property="affIdType"/>
	</td>
    <td width="165">Local: 
      <bean:write name="addDataEntry" scope="request" property="affIdLocal"/>
    </td>
    <td width="99">State: 
      <bean:write name="addDataEntry" scope="request" property="affIdState"/>
    </td>
    <td width="200">Sub Unit: 
      <bean:write name="addDataEntry" scope="request" property="affIdSubUnit"/>
    </td>
     <td width="20"> &nbsp;&nbsp; </td>
     <td width="204">Council: 
       <bean:write name="addDataEntry" scope="request" property="affIdCouncil"/>
    </td>
  </tr>
  <tr> 
    <td colspan="5" align="left">Name of Employer: 
      <bean:write name="addDataEntry" scope="request" property="employerName"/>
    </td>
    <td align="left">Active: 
      <bean:write name="addDataEntry" scope="request" property="affIdStatus"/>
    </td>
  </tr>
  </table>
  <br>
  
  <html:form action="/AddDataEntry.do">
  
  <%
  	java.lang.String sectionType = (java.lang.String) session.getAttribute("secTypeChoose");
  %>
  
  <html:hidden name="addDataEntry" property="addYear"/>
  <html:hidden name="addDataEntry" property="empAffPk"/> 
  <html:hidden name="addDataEntry" property="secType" value="<%= sectionType %>"/>
  <html:hidden name="addDataEntry" property="h_statAverage"/> 
  <html:hidden name="addDataEntry" property="agreementPk"/> 
  <html:hidden name="addDataEntry" property="selectAgreement"/> 
  
<% 
String empActive = null;

if (request.getParameter("empActive") != null)
	empActive = (String) request.getParameter("empActive"); 
else if (request.getAttribute("empActive") != null)
	empActive = (String) request.getAttribute("empActive"); 
%> 

  <html:hidden property="empActive" value='<%=empActive%>' />
  
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
    <td colspan="5">Total Number of Members and Fee Payers: 
      <html:text property="numberOfMember"  onblur="checkNumeric(this,-100,9999999,',','.','-');" size="20" maxlength="20"/> </td>
    <td>STAT Average: 
      <bean:write name="addDataEntry" scope="request" property="statAverage"/> 
    </td>
    <td>Membership Count: 
      <bean:write name="addDataEntry" scope="request" property="membershipCt"/> 
    </td>
  </tr>
  </table>
  <table width="100%">
  <tr> 
    <td colspan= "40">Duration of Agreement: &nbsp;</td>
    <td colspan= "3">In Negotiations: &nbsp;
    <html:radio property="inNegotiations" value="no" onclick="manipulateStartEndDate()" />No&nbsp;&nbsp;&nbsp;
    <html:radio property="inNegotiations" value="yes" onclick="manipulateStartEndDate()" />Yes
    </td>
  </tr>
  <tr> 
    <td colspan= "40">
      From: &nbsp;<html:text property="durationFrom" onblur="validateAgreementDate(this);" size="10" maxlength="10"/> <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('durationFrom', 'mm/dd/y');"> 
    </td>						     
    <td colspan= "3">
      To: &nbsp;<html:text property="durationTo" onblur="validateAgreementDate(this);" size="10" maxlength="10"/> <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('durationTo', 'mm/dd/y');"> 
    </td>
  </tr>
  </table>
  <br>

  <p><b>FILL OUT </b>&nbsp;<html:radio property="section" value="Section A" onclick="disableSectionB()"/>
    Section A&nbsp;&nbsp;&nbsp;&nbsp; <b>OR</b> &nbsp;

  <html:radio property="section" value="Section B" onclick="disableSectionA()"/>
    Section B<b>:</b></p>
  <table width="100%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <th>A.</th>
      <th colspan="6" align="left">Percent Wage Increases</th>
      <th colspan= "8" align="left">C. Other Wage Adjustments</th>
    </tr>
    <tr> 
      <td height="41" colspan="3">Percentage Increase</td>
      <td>Effective</td>
      <td>&nbsp;</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td>Type of Payment</td>
      <td>&nbsp;</td>
      <td>Percentage Increase<br> Adjustment</td>
      <td>&nbsp;</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td>&nbsp;</td>
      <td colspan= "2">Members Times <br>Increase</td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td><html:text property="initPercent_a" onchange="calInitMemberTimesIncA();" onblur="changeDeceFormetA(this);checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/> %&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td>&nbsp;</td>
      <td colspan= "2"> <html:text property="initEffective_a" onblur="validateEffectiveDate(this, 'addYear');" size="10" maxlength="10"/> 
        <img src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('initEffective_a', 'mm/dd/y');"> 
      </td>
      <td>&nbsp;</td>
      <td><html:text property="initNoOfMember_a" onchange="calInitMemberTimesIncA();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10" /></td>
      <td><html:text property="initTypeOfPayment_adj_a" size="20" maxlength="20"/></td>
      <td>&nbsp;</td>
      <td><html:text property="initPercentInc_adj_a" onchange="calInitMemberTimesIncA();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/>
        %&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td>&nbsp;</td>
      <td colspan= "2"><html:text property="initNoOfMember_adj_a" onchange="calInitMemberTimesIncA();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10"/></td>
      <td>&nbsp;</td>
      <td><html:text property="initMbrTimesInc_a" size="10" maxlength="10"/></td>
    </tr>
    <%
	int i = -1;
    %>
    <nested:iterate property="percentWageIncList"> 
    <%
        i = i + 1;
        String tmpI = ""+i;
	String calendarParameter_a = "percentWageIncList[" + i + "].effective_a";
    %>

    <tr> 
      <td>&nbsp;</td>
      <td><nested:text property="percent_a" onchange="calMemberTimesIncALoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/>&nbsp;%</td>
      <td>&nbsp;</td>
      <td colspan= "2"> <nested:text property="effective_a" onblur="validateEffectiveDate(this, 'addYear');" size="10" maxlength="10"/> 
        <img src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('<%=calendarParameter_a%>', 'mm/dd/y');"> 
      </td>
      <td>&nbsp;</td>
      <td><nested:text property="noOfMember_a"  onchange="calMemberTimesIncALoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10" />
	 <img src="images/calculator.gif" width="12" height="22" border="0" onclick="fillLastRowNoOfMemberA('<%=i%>')"> 
      </td>													
      <td><nested:text property="typeOfPayment_adj_a" size="20" maxlength="20"/></td>
      <td>&nbsp;</td>
      <td><nested:text property="percentInc_adj_a" onchange="calMemberTimesIncALoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/>&nbsp;%</td>
      <td>&nbsp;</td>
      <td colspan= "2"><nested:text property="noOfMember_adj_a" onchange="calMemberTimesIncALoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10"/></td>
      <td>&nbsp;</td>
      <td><nested:text property="mbrTimesInc_a" size="10" maxlength="10"/></td>
    </tr>
    </nested:iterate> 
    <tr> 
      <td colspan="4"><html:submit property="addPencentageIncRowButton" styleClass="button">Add New Row</html:submit> </td>							
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>
  <br>
  <b>OR</b>
  <br><br>
  <table width="100%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <th>B.</th>
      <th colspan="6" align="left">Amount Wage Increase</th>
      <th colspan= "8" align="left">C. Other Wage Adjustments</th>
    </tr>
    <tr> 
      <td colspan="3">
	     Amount Increase<br>
		 <html:radio property="amountType" value="cent/hr" />cent/hr<br>
		 <html:radio property="amountType" value="dollar/yr" />dollar/yr
	  </td>
      <td>Effective</td>
      <td>&nbsp;</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td>Type of Payment</td>
      <td>&nbsp;</td>
      <td>Amount Increase<br> Adjustment</td>
      <td>&nbsp;</td>
      <td colspan= "2">Number of Members and <br>
        Fee Payers Affected</td>
      <td>&nbsp;</td>
      <td colspan= "2">Members Times <br>Increase</td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td><html:text property="initAmount_b" onchange="calInitMemberTimesIncB();" onblur="changeDeceFormetB(this);checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/>
      </td>
      <td>&nbsp;</td>
      <td colspan= "2"> <html:text property="initEffective_b" onblur="validateEffectiveDate(this, 'addYear');" size="10" maxlength="10"/> 
        <img src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('initEffective_b', 'mm/dd/y');"> 
      </td>
      <td>&nbsp;</td>
      <td><html:text property="initNoOfMember_b" onchange="calInitMemberTimesIncB();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10"/></td>
      <td><html:text property="initTypeOfPayment_adj_b" size="20" maxlength="20"/></td>
      <td>&nbsp;</td>
      <td><html:text property="initAmountInc_adj_b" onchange="calInitMemberTimesIncB();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/>
      </td>
      <td>&nbsp;</td>
      <td colspan= "2"><html:text property="initNoOfMember_adj_b" onchange="calInitMemberTimesIncB();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10"/></td>
      <td>&nbsp;</td>
      <td><html:text property="initMbrTimesInc_b" size="10" maxlength="10"/></td>
    </tr>
    <%
	int j = -1;
  %>
    <nested:iterate property="amountWageIncList"> 
    <%
        j = j + 1;
	String calendarParameter_b = "amountWageIncList[" + j + "].effective_b";
  %>
    <tr> 
      <td>&nbsp;</td>
      <td><nested:text property="amountInc_b" onchange="calMemberTimesIncBLoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/>&nbsp;</td>
      <td>&nbsp;</td>
      <td colspan= "2"> <nested:text property="effective_b" onblur="validateEffectiveDate(this, 'addYear');" size="10" maxlength="10"/> 
        <img src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('<%=calendarParameter_b%>', 'mm/dd/y');"> 
      </td>
      <td>&nbsp;</td>
      <td><nested:text property="noOfMember_b" onchange="calMemberTimesIncBLoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10"/>
	  <img src="images/calculator.gif" width="12" height="22" border="0" onclick="fillLastRowNoOfMemberB('<%=j%>')"> 
      </td>
      <td><nested:text property="typeOfPayment_adj_b" size="20" maxlength="20"/></td>
      <td>&nbsp;</td>
      <td><nested:text property="amountInc_adj_b" onchange="calMemberTimesIncBLoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="5" maxlength="5"/></td>
      <td>&nbsp;</td>
      <td colspan= "2"><nested:text property="noOfMember_adj_b" onchange="calMemberTimesIncBLoop();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="10" maxlength="10"/></td>
      <td>&nbsp;</td>
      <td colspan= "2"><nested:text property="mbrTimesInc_b" size="10" maxlength="10"/></td>
    </tr>
    </nested:iterate> 
    <tr> 
      <td colspan="4"> <html:submit property="addAmountIncRowButton" styleClass="button">Add New Row</html:submit> </td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>
  Average Wages: &nbsp;&nbsp;&nbsp; 
  <html:text property="averageWage" onchange="calInitMemberTimesIncB();" onblur="checkNumeric(this,-100,9999999,',','.','-');" size="20" maxlength="20"/>
  <br><br>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td>Agreement Received: <html:checkbox property="agreementReceived" value="no"/></td>
      <td>Agreement Description: 
         <html:select property="agreementDesc" >
		<html:option value=""></html:option>
  		<html:option value="State">State</html:option>
  		<html:option value="City">City</html:option>
  		<html:option value="County">County</html:option>
  		<html:option value="Coalition">Coalition</html:option>
  	  </html:select>	
  	</td>
    </tr>
  </table>
  <br>  
  <table width="100%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td>Name of Person Completing Form:</td>
      <td><html:text property="personName" size="20" maxlength="20"/></td>
      <td colspan="2">&nbsp;</td>
      <td>Form Completed:</td>
      <td><html:checkbox property="formCompleted" value="no"/></td>
    </tr>
    <tr> 
      <td>Telephone Number:</td>
      <td><html:text property="telephone" size="20" maxlength="20"/></td>
	  <td colspan="2">&nbsp;</td>
      <td>Correspondence:</td>
      <td><html:checkbox property="correspondence" value="no" onclick="manipulateCorrespondenceDate()" /></td>
      <td>Date:</td>
	  <td><html:text property="correspondenceDate" onblur="validateAgreementDate(this);" size="10" maxlength="10"/> <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('correspondenceDate', 'mm/dd/y');"></td> 
     </tr>
	 <tr> 
      <td>Email</td>
	  <td><html:text property="email" size="20" maxlength="20"/></td>
    </tr>
    <tr> 
    <tr> 
      <td>&nbsp;</td>
    </tr>
	</table>
	
	<table width="100%">
    <tr> 
      <td width="12%">Comments:</td><td>&nbsp;</td>
    </tr>
    <tr> 
      <td colspan="2"><html:textarea property="comments" cols="100" rows="7" /></td>
    </tr>
   <tr> 
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td width="12%">Agreement:</td>
      <td>
			<html:select property="agreementName" onchange="newAgreements(this.form, this);">
			<html:options collection="agreements" property="agreementPk" labelProperty="agreementName"/>
			</html:select>
			&nbsp;&nbsp;
			<html:button property="viewAgreementBtn" styleClass="button" onclick="viewAgreement(this.form);">View</html:button>
	  </td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>
  
  <br>
  <table width="90%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td align="right"><html:submit property="saveNewDataEntry" styleClass="button" onclick="return checkAllFieldsBeforeSubmit();">&nbsp;Save&nbsp;</html:submit></td>
      <td>&nbsp;</td>
      <td align="left"><html:reset styleClass="button"/></td>
    </tr>
  </table>
  <br>
</html:form>
<br>
<br>

<%@ include file="../include/footer.inc" %> 
