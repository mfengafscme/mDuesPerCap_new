<%!String title = "", help = "MainMenu.html";%>

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


<body>

<div align="center"><font color="#008040" size="4"><strong>Agreement Detail</strong></font>
</div><br>

<html:form action="/agreementDetail.do">
<html:hidden property="agreementPk"/> 
  
<bean:define id="agreementForm" name="agreementForm" type="org.afscme.enterprise.minimumdues.web.AgreementForm"/>

<table cellspacing="0" cellpadding="2" width="80%" align="center">
<tr>
    <td align="left"><font color="#008040"><strong>&nbsp;Agreement Name: </strong></font>
	<strong><bean:write name="agreementForm" scope="request" property="agreementName"/></strong>
    </td>
</tr>
</table>


<table width="80%" cellspacing="0" cellpadding="2" border="0" align="center">
        <tr>
  	      	<td width="50%" height="28" align="left"><font color="#008040"><strong>&nbsp;Starting Date: &nbsp;</strong></font>
			<strong><bean:write name="agreementForm" scope="request" property="startDate"/></strong>
		</td>
          	<td width="50%" height="28" align="right"><font color="#008040"><strong>Ending Date: &nbsp;</strong></font>
            		<strong><bean:write name="agreementForm" scope="request" property="endDate"/></strong>
          	</td>
	</tr>
</table>

<table width="80%" cellpadding="0" cellspacing="2" border="0" align="center">
        <tr>
          	<td width="81%" align="left"><font color="#008040"><strong>&nbsp;Comments:&nbsp;&nbsp;</strong></font>		
  			<strong><bean:write name="agreementForm" scope="request" property="comments"/></strong>
		</td>
 	</tr>
</table>

<TABLE cellspacing=0 cellpadding=2 id=header border=1>
        <COL width="35">
        <COL width="20">
        <COL width="35">
        <COL width="20">
        <COL width="25">
        <COL width="250">
        <COL width="30">
        <COL width="30">
  <tr>
          <TH style="COLOR: white" align=left
          bgColor=#008040>Type</TH>
          <TH style="COLOR: white" align=left bgColor=#008040>St</TH>
          <TH style="COLOR: white" align=left
          bgColor=#008040>Coun</TH>
          <TH style="COLOR: white" align=left bgColor=#008040>Lcl</TH>
          <TH style="COLOR: white" align=left
          bgColor=#008040>SubU</TH>
          <TH style="COLOR: white" align=left
          bgColor=#008040>Name</TH>
          <TH style="COLOR: white" align=left
          bgColor=#008040>Stat</TH>
          <TH style="COLOR: white" align=left
          bgColor=#008040>MemCt</TH></TR></TBODY>
  </tr>
</TABLE>

<DIV STYLE="overflow: auto; width: 545px; height: 300;
            border-left: 1px #0000ff solid; border-bottom: 1px #0000ff solid; border-right: 1px #0000ff solid; border-top: 1px #0000ff solid;
            padding:0px; margin: 0px">
<TABLE cellspacing=3 cellpadding=2>
        <COL width="35">
        <COL width="20">
        <COL width="35">
        <COL width="20">
        <COL width="25">
        <COL width="250">
        <COL width="30">
        <COL width="30">

            <logic:iterate name="agreementForm" property="results1" id="result" type="org.afscme.enterprise.affiliate.EmployerData">
              <bean:define id="empAffPk" name="result" property="empAffPk" />
              <tr> 
                <td><font size="-3"> 
                  <bean:write name="result" property="type"/>
                  </font> </td>
                <td><font size="-3"> 
                  <bean:write name="result" property="state"/>
                  </font> </td>
                <td><font size="-3"> 
                  <bean:write name="result" property="council"/>
                  </font> </td>
                <td><font size="-3"> 
                  <bean:write name="result" property="local"/>
                  </font> </td>
                <td><font size="-3"> 
                  <bean:write name="result" property="chapter"/>
                  </font> </td>
                <td align="left"> <font size="-3"> 
                  <bean:write name="result" property="employer"/>
                  </font> </td>
                <td><font size="-3"> 
                  <bean:write name="result" property="stat"/>
                  </font> </td>
                <td><font size="-3"> 
                  <bean:write name="result" property="memCt"/>
                  </font> </td>
              </tr>
            </logic:iterate>

        </table>

</html:form>

</body>
