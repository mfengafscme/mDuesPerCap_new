<%!String title = "", help = "MainMenu.html";%>
<%@ include file="../include/minimumduesheader.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-blue.css" title="winter" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-brown.css" title="summer" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-green.css" title="green" />
<link rel="stylesheet" type="text/css" media="all" href="css/calendar-win2k-1.css" title="win2k-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-2.css" title="win2k-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-1.css" title="win2k-cold-1" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<link rel="alternate stylesheet" type="text/css" media="all" href="css/calendar-system.css" title="system" />

<script language=JavaScript src=js/edit_agreement.js></script>
<script type="text/javascript" language="JavaScript" src="js/date_format_check.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendarhelper.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar.js"></script>
<script type="text/javascript" language="JavaScript" src="js/calendar-en.js"></script>

<body>

<div align="center"><font color="#008040" size="4"><strong>Agreement Detail</strong></font>
</div><br>

<html:form action="/agreementDetail.do">
<html:hidden property="agreementPk"/> 
  
<bean:define id="agreementForm" name="agreementForm" type="org.afscme.enterprise.minimumdues.web.AgreementForm"/>

<table width="80%" align="center">
<tr>
    <td align="left"><font color="#008040"><strong>Agreement Name: </strong></font>
	<strong><bean:write name="agreementForm" scope="request" property="agreementName"/></strong>
    </td>
</tr>
</table>


<table width="80%" cellpadding="3" cellspacing="2" border="0" align="center">
        <tr>
  	      	<td width="50%" height="28" align="left"><font color="#008040"><strong>Starting Date: &nbsp;</strong></font>
			<strong><bean:write name="agreementForm" scope="request" property="startDate"/></strong>
		</td>
          	<td width="50%" height="28" align="right"><font color="#008040"><strong>Ending Date: &nbsp;</strong></font>
            		<strong><bean:write name="agreementForm" scope="request" property="endDate"/></strong>
          	</td>
	</tr>
</table>

<table width="80%" cellpadding="3" cellspacing="2" border="0" align="center">
        <tr>
          	<td width="81%" align="left"><font color="#008040"><strong>Comments:&nbsp;&nbsp;</strong></font>		
  			<strong><bean:write name="agreementForm" scope="request" property="comments"/></strong>
		</td>
 	</tr>
</table>

<table width="80%" cellpadding="3" cellspacing="2" border="1" align="center">
<tr>
<td width="49%">
	  <table border=1 id=tLeft align=right style="border-color: black; border-style: solid; border-width: 1;" width="100%">
	    <col width="10%"><col width="7%"><col width="10%"><col width="10%"><col width="12%"><col width="41%"><col width="5%">
            <tr> 
              <th width="10%" align="left" bgcolor='#008040' style="color: white">Type</th>
              <th width="7%" align="left" bgcolor='#008040' style="color: white">St</th>
              <th width="10%" align="left" bgcolor='#008040' style="color: white">Coun</th>
              <th width="10%" align="left" bgcolor='#008040' style="color: white">Lcl</th>
              <th width="12%" align="left" bgcolor='#008040' style="color: white">SubU</th>
              <th width="41%" align="left" bgcolor='#008040' style="color: white">Name</th>
              <th width="5%" align="left" bgcolor='#008040' style="color: white"><input type="checkbox" name="checkbox1" value="checkbox" onclick="checkUncheckAll1(this)"></th>
            </tr>
            </table>
</td>
<td width="2%">&nbsp;</td>
<td width="49%">
	  <table border=1 id=tLeft align="center" style="border-color: black; border-style: solid; border-width: 1;" width="100%">
	    <col width="10%"><col width="7%"><col width="10%"><col width="10%"><col width="12%"><col width="41%"><col width="5%">
            <tr> 
              <th width="10%" align="left" bgcolor='#008040' style="color: white">Type</th>
              <th width="7%" align="left" bgcolor='#008040' style="color: white">St</th>
              <th width="10%" align="left" bgcolor='#008040' style="color: white">Coun</th>
              <th width="10%" align="left" bgcolor='#008040' style="color: white">Lcl</th>
              <th width="12%" align="left" bgcolor='#008040' style="color: white">SubU</th>
              <th width="41%" align="left" bgcolor='#008040' style="color: white">Name</th>
              <th width="5%" align="left" bgcolor='#008040' style="color: white"><input type="checkbox" name="checkbox2" value="checkbox" onclick="checkUncheckAll2(this)"></th>
            </tr>
            </table>
</td>
</tr>
<tr>
<td width="49%" align="center" valign="top"> 
	<div style="overflow: auto; width: 380px; height: 300;  padding:0px; margin: 0px">
	<table border=1 cellspacing="0" cellpadding="2" width="100%">
	    <col width="10%"><col width="7%"><col width="10%"><col width="10%"><col width="12%"><col width="41%"><col width="5%">
            
            <logic:iterate name="agreementForm" property="results1" id="result" type="org.afscme.enterprise.affiliate.EmployerData">
              <bean:define id="empAffPk" name="result" property="empAffPk" />
              <tr> 
                <td width="10%"><font size="-3"> 
                  <bean:write name="result" property="type"/>
                  </font> </td>
                <td width="7%"><font size="-3"> 
                  <bean:write name="result" property="state"/>
                  </font> </td>
                <td width="10%"><font size="-3"> 
                  <bean:write name="result" property="council"/>
                  </font> </td>
                <td width="10%"><font size="-3"> 
                  <bean:write name="result" property="local"/>
                  </font> </td>
                <td width="12%"><font size="-3"> 
                  <bean:write name="result" property="chapter"/>
                  </font> </td>
                <td width="41%" align="left"> <font size="-3"> 
                  <bean:write name="result" property="employer"/>
                  </font> </td>
                <td width="5%"><font size="-3"> 
                  <html:multibox property="selectedItems1">
                  	<bean:write name="result" property="empAffPk"/> 
                  </html:multibox>
                  </font>
                </td>
              </tr>
            </logic:iterate>

        </table>
</td>

<td width="2%" align="center" valign="top"></td>

<td width="49%" align="center" valign="top"> 
	<div style="overflow: auto; width: 380px; height: 300;  padding:0px; margin: 0px">
	<table border=1 cellspacing="0" cellpadding="2" width="100%">
	    <col width="10%"><col width="7%"><col width="10%"><col width="10%"><col width="12%"><col width="41%"><col width="5%">
            
            <logic:iterate name="agreementForm" property="results2" id="result" type="org.afscme.enterprise.affiliate.EmployerData">
              <bean:define id="empAffPk" name="result" property="empAffPk" />
              <tr> 
                <td width="10%"><font size="-3"> 
                  <bean:write name="result" property="type"/>
                  </font> </td>
                <td width="7%"><font size="-3"> 
                  <bean:write name="result" property="state"/>
                  </font> </td>
                <td width="10%"><font size="-3"> 
                  <bean:write name="result" property="council"/>
                  </font> </td>
                <td width="10%"><font size="-3"> 
                  <bean:write name="result" property="local"/>
                  </font> </td>
                <td width="12%"><font size="-3"> 
                  <bean:write name="result" property="chapter"/>
                  </font> </td>
                <td width="41%" align="left"> <font size="-3"> 
                  <bean:write name="result" property="employer"/>
                  </font> </td>
                <td width="5%"><font size="-3"> 
                  <html:multibox property="selectedItems2">
                  	<bean:write name="result" property="empAffPk"/> 
                  </html:multibox>
                  </font></td>
                  </font></td>
              </tr>
            </logic:iterate>
        </table>
</td>


</tr>

<tr>
<td width="50%" align="center" bgcolor='#008040' style="color: white">
Employers Currently Covered
</td>
<td width="3%" align="center"></td>
<td width="47%" align="center" bgcolor='#008040' style="color: white">
Employers Eligible for Adding
</td>
</tr>

<tr>
<td width="50%" align="right">
<html:submit property="removeFromAgreementBtn" styleClass="button">Remove</html:submit>
</td>
<td width="3%" align="center"></td>
<td width="47%" align="right">
<html:submit property="addToAgreementBtn" styleClass="button">&nbsp;&nbsp;Add&nbsp;&nbsp;</html:submit>
</td>
</tr>

</table>


</html:form>

<br><br><br>
<%@ include file="../include/footer.inc" %> 
</body>
