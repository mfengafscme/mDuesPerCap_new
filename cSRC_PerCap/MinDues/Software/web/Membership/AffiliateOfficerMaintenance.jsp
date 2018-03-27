<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%!String title = "Affiliate Officer Maintenance", help = "AffiliateOfficerMaintenance.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="affiliateOfficerMaintenanceForm" type="org.afscme.enterprise.affiliate.officer.web.AffiliateOfficerMaintenanceForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="MaintainAffiliateOfficers"/>
<%@ include file="../include/affiliate_tab.inc" %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
	<TD colspan="14" class="ContentHeaderTR">
	    Officers 
	</TD>
    </TR>
    <TR>
	<TH width="5%">
	    Select 
	</TH>
	<TH width="20%">
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.officerTitle">Officer Title</afscme:sortLink>	
	</TH>
	<TH width="8%">
		End Term
	</TH>
	<TH width="3%">
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.suspended">Susp</afscme:sortLink>	
	</TH>
	<TH width="2%">
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.temporaryMember">T</afscme:sortLink>	
	</TH>
	<TH>
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.fullName">Name</afscme:sortLink>	
	</TH>
	<TH width="2%">
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.reportingOfficer">RO</afscme:sortLink>	
	</TH>
	<TH width="3%">
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.steward">Stwd</afscme:sortLink>	
	</TH>
	<TH width="4%">
		<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.executiveBoard">E-Bd</afscme:sortLink>	
	</TH>
	<TH COLSPAN="5">Affiliate Identifier</TH>
    </TR>
    <TR>
	<TD COLSPAN="9">&nbsp;</TD>
	<TH width="2%" class="small">
		<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.ai.type">Type</afscme:sortLink>	
	</TH>
	<TH width="8%" class="small">
		<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.ai.local">Loc/Sub Chap</afscme:sortLink>	
	</TH>
	<TH width="5%" class="small">
		<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.ai.state">State/Nat'l</afscme:sortLink>	
	</TH>
	<TH width="5%" class="small">
		<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.ai.subUnit">Sub Unit</afscme:sortLink>	
	</TH>
	<TH width="7%" class="small">
		<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="officer.ai.council">CN/Ret Chap</afscme:sortLink>	
	</TH>
    </TR>
<logic:present name="form" property="officerList">
    <logic:iterate id="element" name="form" property="officerList" type="java.util.Map.Entry">    
        <bean:define id="fieldPk" name="element" property="key" type="java.lang.Integer"/>
	  <bean:define id="officerData" name="element" property="value" type="org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance"/>         
        <% String valueObjectGetString1 = "officerData(" + fieldPk + ").officerTitle";
	     String valueObjectGetString2 = "officerData(" + fieldPk + ").monthOfElection";        
           String valueObjectGetString3 = "officerData(" + fieldPk + ").endTerm";
           String valueObjectGetString4 = "officerData(" + fieldPk + ").suspended";
           String valueObjectGetString5 = "officerData(" + fieldPk + ").temporaryMember";
           String valueObjectGetString6 = "officerData(" + fieldPk + ").firstName";
           String valueObjectGetString7 = "officerData(" + fieldPk + ").middleName";
           String valueObjectGetString8 = "officerData(" + fieldPk + ").lastName";
           String valueObjectGetString9 = "officerData(" + fieldPk + ").suffix";
           String valueObjectGetString10 = "officerData(" + fieldPk + ").reportingOfficer";
           String valueObjectGetString11 = "officerData(" + fieldPk + ").steward";
           String valueObjectGetString12 = "officerData(" + fieldPk + ").executiveBoard";
           String valueObjectGetString13 = "officerData(" + fieldPk + ").ai.type";
           String valueObjectGetString14 = "officerData(" + fieldPk + ").ai.local";
           String valueObjectGetString15 = "officerData(" + fieldPk + ").ai.state";
           String valueObjectGetString16 = "officerData(" + fieldPk + ").ai.subUnit";
           String valueObjectGetString17 = "officerData(" + fieldPk + ").ai.council";
           String valueObjectGetString18 = "officerData(" + fieldPk + ").ai.code"; 
           String valueObjectGetString19 = "officerData(" + fieldPk + ").lengthOfTerm";
           String valueObjectGetString20 = "officerData(" + fieldPk + ").officerPersonPk";
           String valueObjectGetString21 = "officerData(" + fieldPk + ").officerAction";    
           String valueObjectGetString22 = "officerData(" + fieldPk + ").expirationYear";     
           String valueObjectGetString23 = "officerData(" + fieldPk + ").expirationMonth";
        %>
          
    <TR align="center">
	<TD>
	   <% if (officerData.getOfficerPersonPk() != null && officerData.getOfficerPersonPk().intValue() != 0) { 
	   
	   // put below here the link to officer detail pages
	   %>
	    
	   <% } %>
	</TD>
	<TD>
	    <bean:write name="form" property='<%=valueObjectGetString1%>' />	
	</TD>
	<TD nowrap>
	    <% if (officerData.getExpirationYear() != null && officerData.getExpirationYear().intValue() != 0) { 
	    	   if (officerData.getExpirationMonth().intValue() < 10) { %>   
		       0<bean:write name="form" property='<%=valueObjectGetString23%>' />/<bean:write name="form" property='<%=valueObjectGetString22%>' />
		   <% } else { %>
                       <bean:write name="form" property='<%=valueObjectGetString23%>' />/<bean:write name="form" property='<%=valueObjectGetString22%>' />		   
                   <% } 
	       } else { 
		   if (officerData.getMonthOfElection().intValue() < 78010) { 
		   %>
		   0<afscme:codeWrite name="form" property='<%=valueObjectGetString2%>' codeType="MonthOffcrElection" format="{0}"/>/<bean:write name="form" property='<%=valueObjectGetString3%>' />
		   <% } else { %>	 	    	 	  
			<afscme:codeWrite name="form" property='<%=valueObjectGetString2%>' codeType="MonthOffcrElection" format="{0}"/>/<bean:write name="form" property='<%=valueObjectGetString3%>' />
		   <% } %>
	    <% } %>
	</TD>
	<TD>
	    <html:checkbox name="form" property='<%=valueObjectGetString4%>' disabled="true" />
	</TD>
	<TD>
	    <html:checkbox name="form" property='<%=valueObjectGetString5%>' disabled="true" /> 
	</TD>
	<TD>
	    <% if (officerData.getFirstName() != null) { %>
	        <bean:write name="form" property='<%=valueObjectGetString8%>' /><% if (officerData.getSuffix() != null) { %> <afscme:codeWrite name="form" property='<%=valueObjectGetString9%>' codeType="Suffix" format="{1}"/><% } %>, <bean:write name="form" property='<%=valueObjectGetString6%>' /> <bean:write name="form" property='<%=valueObjectGetString7%>' />
	    <% } else { %>
		  &nbsp;
	    <% } %>
	</TD>
	<TD>
	    <html:radio name="form" property='<%=valueObjectGetString10%>' disabled="true" value="true" />
	</TD>
	<TD>
	    <html:checkbox name="form" property='<%=valueObjectGetString11%>' disabled="true" />
	</TD>
	<TD>
	    <html:checkbox name="form" property='<%=valueObjectGetString12%>' disabled="true" /> 
	</TD>	
	<TD>
	    <% if (officerData.getAi().getType() != null) { %>
	   	 <bean:write name="form" property='<%=valueObjectGetString13%>' />
	    <% } else { %>
		  &nbsp;
	    <% } %>
	</TD>
	<TD>
 	    <% if (officerData.getAi().getLocal() != null && officerData.getAi().getLocal() != "") { %>
  	       <bean:write name="form" property='<%=valueObjectGetString14%>' />
	    <% } else { %>
		  &nbsp;
	    <% } %>
	</TD>
	<TD>
 	    <% if (officerData.getAi().getState() != null && officerData.getAi().getState() != "") { %>
  	       <bean:write name="form" property='<%=valueObjectGetString15%>' />
	    <% } else { %>
		  &nbsp;
	    <% } %>
	</TD>
	<TD>
 	    <% if (officerData.getAi().getSubUnit() != null && officerData.getAi().getSubUnit() != "") { %>
  	       <bean:write name="form" property='<%=valueObjectGetString16%>' />
	    <% } else { %>
		  &nbsp;
	    <% } %>
	</TD>
	<TD>
 	    <% if (officerData.getAi().getCouncil() != null && officerData.getAi().getCouncil() != "") { %>
  	       <bean:write name="form" property='<%=valueObjectGetString17%>' />
	    <% } else { %>
		  &nbsp;
	    <% } %>
	</TD>		
    </TR>
    </logic:iterate>
</logic:present>

</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR>
		<TD colspan="12" class="ContentHeaderTR">
			Sub-Affiliate Auto Executive Board Members 
		</TD>
	</TR>
	<TR>
		<TH width="4%">
			Select 
		</TH>
		<TH width="15%">
			<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.officerTitle">Affiliate Title</afscme:sortLink>
		</TH>
		<TH width="15%">
			<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.subAffiliateTitle">Sub-Affiliate Title</afscme:sortLink>
		</TH>
		<TH width="8%">
			<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.endTerm">End Term</afscme:sortLink>
		</TH>
		<TH width="3%">
			<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.suspended">Susp</afscme:sortLink>		
		</TH>
		<TH width="2%">
			<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.temporaryMember">T</afscme:sortLink>		
		</TH>
		<TH>
			<afscme:sortLink styleClass="TH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.fullName">Name</afscme:sortLink>		
		</TH>
		<TH colspan="5">
			Source Affiliate 
		</TH>
	</TR>
	<TR>
		<TD colspan="7">&nbsp;
		</TD>
		<TH width="2%" class="small">
			<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.ai.type">Type</afscme:sortLink>		
		</TH>
		<TH width="8%" class="small">
			<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.ai.local">Loc/Sub Chap</afscme:sortLink>		
		</TH>
		<TH width="5%" class="small">
			<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.ai.state">State/Nat'l</afscme:sortLink>		
		</TH>
		<TH width="5%" class="small">
			<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.ai.subUnit">Sub Unit</afscme:sortLink>		
		</TH>
		<TH width="7%" class="small">
			<afscme:sortLink styleClass="smallTH" action="/viewAffiliateOfficerMaintenance.action" formName="affiliateOfficerMaintenanceForm" field="eboard.ai.council">CN/Ret Chap</afscme:sortLink>		
		</TH>
	</TR>
	<logic:present name="form" property="executives">
    	    <logic:iterate id="executives" name="form" property="executives" type="org.afscme.enterprise.affiliate.officer.EBoardMaintenance"> 		    	    
	    <TR align="center">
		<TD>
	    	    <% if (executives.getOfficerPersonPk() != null && executives.getOfficerPersonPk().intValue() != 0) { %>		
	   	    View - Disabled
	   	    <% } %>
		</TD>
		<TD>
		    <bean:write name="executives" property="officerTitle" />
		</TD>
		<TD>
		    <bean:write name="executives" property="subAffiliateTitle" />
		</TD>
		<TD>
		    <logic:lessThan name="executives" property="monthOfElection" value="78010">
		         0<afscme:codeWrite name="executives" property="monthOfElection" codeType="MonthOffcrElection" format="{0}"/>/<bean:write name="executives" property="endTerm" />
		    </logic:lessThan>
		    <logic:greaterEqual name="executives" property="monthOfElection" value="78010">
		         <afscme:codeWrite name="executives" property="monthOfElection" codeType="MonthOffcrElection" format="{0}"/>/<bean:write name="executives" property="endTerm" />
		    </logic:greaterEqual>
		</TD>
		<TD>
		    <html:checkbox name="executives" property="suspended" disabled="true" />			
		</TD>
		<TD>			
		    <html:checkbox name="executives" property="temporaryMember" disabled="true" />		
		</TD>
		<TD>
		    <% if (executives.getFirstName() != null) { %>
		        <bean:write name="executives" property="lastName" /><% if (executives.getSuffix() != null) { %> <afscme:codeWrite name="executives" property="suffix" codeType="Suffix" format="{1}"/><% } %>, <bean:write name="executives" property="firstName" /> <bean:write name="executives" property="middleName" />
	    	    <% } %>
		</TD>
		<TD>
		    <bean:write name="executives" property="ai.type" />
		</TD>
		<TD>
		    <bean:write name="executives" property="ai.local" />
		</TD>
		<TD>
		    <bean:write name="executives" property="ai.state" />
		</TD>
		<TD>
		    <bean:write name="executives" property="ai.subUnit" />
		</TD>
		<TD>
		    <bean:write name="executives" property="ai.council" />
		</TD>
	</TR>
	    </logic:iterate>
	</logic:present>	
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<%@ include file="/include/affiliate_tab.inc" %>
<%@ include file="../include/footer.inc" %>
