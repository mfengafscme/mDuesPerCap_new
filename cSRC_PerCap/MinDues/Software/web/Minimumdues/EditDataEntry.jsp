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

<script type="text/javascript" language="JavaScript" src="js/mdues_dataedit_form.js"></script>
<script type="text/javascript" language="JavaScript" src="js/date_format_check.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendarhelper.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar-en.js"></script>
	
<body background="images/background.jpg">
<div align="center"><font color="#808000" size="5"><strong>Edit Wage Increase Data Entry</strong></font><br>
</div>
<img src="images/yellow.jpg" width="7" height="11" border="1"> &nbsp;&nbsp;Required Field
<br><br>
<font size="3" color ="blue">

<html:form action="/EditDataEntry.do">
<bean:define id="editDataEntry" name="editDataEntry" type="org.afscme.enterprise.minimumdues.web.EditDataEntryForm"/>

  <%
	java.lang.String sectionType = (java.lang.String) session.getAttribute("secTypeChoose");
  %>

  <html:hidden name="editDataEntry" property="secType" value="<%= sectionType %>"/>
  
  <html:hidden name="editDataEntry" property="empAffPk"/> 
  <html:hidden name="editDataEntry" property="viewYear"/>  
  <html:hidden name="editDataEntry" property="h_statAverage"/> 
  <html:hidden name="editDataEntry" property="h_formCompleted"/> 
  <html:hidden name="editDataEntry" property="h_correspondence"/> 
  <html:hidden name="editDataEntry" property="h_inNegotiations"/> 
  <html:hidden name="editDataEntry" property="h_agreementReceived"/> 
  <html:hidden property="empActive" value='<%= (String) request.getParameter("empActive")%>' />
  <html:hidden name="editDataEntry" property="agreementPk"/> 
  <html:hidden name="editDataEntry" property="selectAgreement"/> 


  <B>YEAR:
  <bean:write name="editDataEntry" scope="request" property="viewYear"/></B> 
  <br>
  
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
    <td width="99">Type:
		<bean:write name="editDataEntry" scope="request" property="affIdType"/>
	</td>
    <td width="165">Local: 
      <bean:write name="editDataEntry" scope="request" property="affIdLocal"/>
    </td>
    <td width="99">State: 
      <bean:write name="editDataEntry" scope="request" property="affIdState"/>
    </td>
    <td width="200">Sub Unit: 
      <bean:write name="editDataEntry" scope="request" property="affIdSubUnit"/>
    </td>
    <td width="20"> &nbsp;&nbsp; </td>
    <td width="204">Council: 
      <bean:write name="editDataEntry" scope="request" property="affIdCouncil"/>
    </td>
  </tr>
  <tr> 
    <td colspan="5" align="left">Name of Employer: 
      <bean:write name="editDataEntry" scope="request" property="employerName"/>
    </td>
    <td align="left">Active: 
      <bean:write name="editDataEntry" scope="request" property="affIdStatus"/>
    </td>
  </tr></table>
  <br>
</font> 

<br>
  <table width="100%" border="0" cellspacing="0" cellpadding="1"/>
  <tr> 
    <td colspan="5">Total Number of Members and Fee Payers: 
      <html:text name="editDataEntry" property="numberOfMember" onblur="checkNumeric(this,-999999999,999999999,',','','');" size="20" maxlength="20"/> </td>
    <td>STAT Average: 
      <bean:write name="editDataEntry" scope="request" property="statAverage"/> 
    </td>
    <td>Membership Count: 
      <bean:write name="editDataEntry" scope="request" property="membershipCt"/> 
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
      From: &nbsp;<html:text name="editDataEntry" property="durationFrom" onblur="validateAgreementDate(this);" size="10" maxlength="10"/> <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('durationFrom', 'mm/dd/y');"> 
    </td>
    <td colspan= "3">
      To: &nbsp;<html:text name="editDataEntry" property="durationTo" onblur="validateAgreementDate(this);" size="10" maxlength="10"/> <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('durationTo', 'mm/dd/y');"> 
    </td>
  </tr>
  </table>
  <br>
  
  
  <!-- Sec A Part -->
  <logic:present name="editDataEntry" property="secType">
  <logic:notEqual name="editDataEntry" property="secType" value="B">
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
    
    <%
	int i = -1;
    %>
    <nested:iterate name="editDataEntry" property="percentWageIncList"> 
    <%
        i = i + 1;
	    String calendarParameter_a = "percentWageIncList[" + i + "].effective_a";
    %>
    <tr> 
      <td>&nbsp;</td>
      <td><nested:text property="percent_a" onblur="calMemberTimesIncALoop();"  onchange="checkNumeric(this,-999999999,999999999,',','.','');" size="5" maxlength="5"/>&nbsp;%</td>
      <td>&nbsp;</td>
      <td colspan= "2"> <nested:text property="effective_a" onblur="validateEffectiveDate(this, 'viewYear');" size="10" maxlength="10"/> 
        <img src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('<%=calendarParameter_a%>', 'mm/dd/y');"> 
      </td>
      <td>&nbsp;</td>
      <td><nested:text property="noOfMember_a" onblur="calMemberTimesIncALoop();" onchange="checkNumeric(this,-999999999,999999999,',','','');" size="10" maxlength="10"/>
        <img src="images/calculator.gif" width="12" height="22" border="0" onclick="fillLastRowNoOfMemberA('<%=i%>')"> 
      </td>
      <td><nested:text property="typeOfPayment_adj_a" size="20" maxlength="20"/></td>
      <td>&nbsp;</td>
      <td><nested:text property="percentInc_adj_a" onblur="calMemberTimesIncALoop();" onchange="checkNumeric(this,-999999999,999999999,',','.','');" size="5" maxlength="5"/>&nbsp;%</td>
      <td>&nbsp;</td>
      <td colspan= "2"><nested:text property="noOfMember_adj_a" onblur="calMemberTimesIncALoop();" onchange="checkNumeric(this,-999999999,999999999,',','','');" size="10" maxlength="10"/></td>
      <td>&nbsp;</td>
      <td><nested:text property="mbrTimesInc_a" size="10" maxlength="10"/></td>
    </tr>
    </nested:iterate> 
    <tr> 
      <td colspan="4"> <html:submit property="addPencentageIncRowButton" styleClass="button">Add New Row</html:submit> </td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>
  </logic:notEqual>
  
  <br>

  <!-- Sec B Part -->
  <logic:equal name="editDataEntry" property="secType" value="B">
  
  
<p><b><font color="#FF0080">Note: Section A is not editable 
  because you entered Section B data before. You can edit Section B below and the 
  corresponding percentage data in Section A will be automatically updated. </font></b></p>
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
      <td><nested:text property="amountInc_b" onblur="calMemberTimesIncBLoop();" onchange="checkNumeric(this,-999999999,999999999,',','.','');" size="5" maxlength="5"/>&nbsp;</td>
      <td>&nbsp;</td>
      <td colspan= "2"> <nested:text property="effective_b" onblur="validateEffectiveDate(this, 'viewYear');" size="10" maxlength="10"/> 
        <img src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('<%=calendarParameter_b%>', 'mm/dd/y');"> 
      </td>
      <td>&nbsp;</td>
      <td><nested:text property="noOfMember_b" onblur="calMemberTimesIncBLoop();" onchange="checkNumeric(this,-999999999,999999999,',','','');" size="10" maxlength="10"/>
        <img src="images/calculator.gif" width="12" height="22" border="0" onclick="fillLastRowNoOfMemberB('<%=j%>')"> 
      </td>
      <td><nested:text property="typeOfPayment_adj_b" size="20" maxlength="20"/></td>
      <td>&nbsp;</td>
      <td><nested:text property="amountInc_adj_b" onblur="calMemberTimesIncBLoop();" onchange="checkNumeric(this,-999999999,999999999,',','.','');" size="5" maxlength="5"/></td>
      <td>&nbsp;</td>
      <td colspan= "2"><nested:text property="noOfMember_adj_b" onblur="calMemberTimesIncBLoop();" onchange="checkNumeric(this,-999999999,999999999,',','','');" size="10" maxlength="10"/></td>
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
  </logic:equal>
  </logic:present>
    
  Average Wages: &nbsp;&nbsp;&nbsp; 
  <html:text name="editDataEntry" property="averageWage" onblur="checkNumeric(this,-999999999,999999999,',','.','');" size="20" maxlength="20"/>
  <br>
  <br>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>Agreement Received: 
        <html:checkbox property="agreementReceived" value="no"/>
        </td>
        <td>Agreement Description: 
           <html:select property="agreementDesc">
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
      <td><html:text name="editDataEntry" property="personName" size="20" maxlength="20"/></td>
      <td colspan="2">&nbsp;</td>
      <td>Form Completed:</td>
      <td><html:checkbox property="formCompleted" value="no"/></td>
    </tr>
    <tr> 
      <td>Telephone Number:</td>
      <td><html:text name="editDataEntry" property="telephone" size="20" maxlength="20"/></td>
	  <td colspan="2">&nbsp;</td>
      <td>Correspondence:</td>
      <td><html:checkbox property="correspondence" value="no" onclick="manipulateCorrespondenceDate()"/></td>
      <td>Date:</td>
	  <td><html:text name="editDataEntry" property="correspondenceDate" onblur="validateAgreementDate(this);" size="10" maxlength="10"/> <IMG src="images/calendar.gif" width="24" height="22" border="0" alt="Calendar" onclick="return showCalendar('correspondenceDate', 'mm/dd/y');"></td> 
     </tr>
	 <tr> 
      <td>Email</td>
	  <td><html:text name="editDataEntry" property="email" size="20" maxlength="20"/></td>
    </tr>
    <tr> 
    <tr> 
      <td>&nbsp;</td>
    </tr>
	</table>
	
	<table width="100%">
    <tr> 
      <td colspan="2">Comments:</td>
    </tr>
    <tr> 
      <td colspan="2"><html:textarea name="editDataEntry" property="comments" cols="100" rows="7" /></td>
    </tr>
   <tr> 
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr> 
      <td width="10%">Agreement:</td>
      <td width="90%">
	  		<html:select property="agreementName" onchange="newAgreements(this.form, this);">
			<html:options collection="agreements" property="agreementPk" labelProperty="agreementName"/>
			</html:select>
			&nbsp;&nbsp;
			<html:button property="viewAgreementBtn" styleClass="button" onclick="viewAgreement(this.form);">View</html:button>
	  </td>
    </tr>
    <tr> 
      <td colspan="2">&nbsp;</td>
    </tr>
  </table>
  <br><br>
  <table width="90%" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td align="right"><html:submit property="saveEditDataButton" styleClass="button" onclick="return checkAllFieldsBeforeSubmit();">&nbsp;Update&nbsp;</html:submit></td>
      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>	
      <td align="left"><html:submit property="cancelEditDataButton" styleClass="button">Cancel</html:submit></td>
    </tr>
  </table>
  <br>
</html:form>
<br>
<br>
<%@ include file="../include/footer.inc" %> 

