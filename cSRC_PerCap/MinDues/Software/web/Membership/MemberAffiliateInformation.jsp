<%! String title = "Member Affiliate Information", help = "MemberAffiliateInformation.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="memberAffiliateInformationForm" id="maf" type="org.afscme.enterprise.member.web.MemberAffiliateInformationForm"/>

<!-- tabs need member affiliate tab -->
<bean:define id="screen" value="MemberAffiliateInformation"/>
<%@ include file="../include/member_aff_tab.inc" %>


<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <afscme:currentPersonName showPk="true" /> <BR>
            <afscme:currentAffiliate />
            <BR> <BR> 
	</TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD class="ContentHeaderTD" WIDTH="50%">Member Type</TD>
					<TD class="ContentTD">
						<afscme:codeWrite name="maf" property="mbrType" codeType="MemberType" format="{1}" />
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Member Status</TD>
					<TD class="ContentTD">
						<afscme:codeWrite name="maf" property="mbrStatus" codeType="MemberStatus" format="{1}" />
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Affiliate Member Number</TD>
					<TD>
						<bean:write name="maf" property="mbrNoLocal"/> 
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="55%" class="ContentHeaderTD">Membership Card Sent</TD>
					<TD class="ContentTD">
						<afscme:dateWrite name="maf" property="mbrCardSentDt"/>
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Primary Information Source</TD>
					<TD class="ContentTD">
						<afscme:codeWrite name="maf" property="primaryInformationSource" codeType="InformationSource" format="{1}" />
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Lost Time Language in Contract</TD>
					<TD class="smallFont">
						<html:checkbox property="lostTimeLanguageFg" name="maf" disabled="true"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="50%" class="ContentHeaderTD">Join Date</TD>
					<TD class="ContentTD">
						<afscme:dateWrite name="maf" property="mbrJoinDt" format="MMMMM yyyy" />
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="55%" class="ContentHeaderTD">Dues Type<TD>
						<afscme:codeWrite name="maf" property="mbrDuesType" codeType="DuesType" format="{1}" />
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Dues Rate</TD>
					<TD>
						<bean:write name="maf" property="mbrDuesRate" format="0.00"/>   USD 
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Dues Frequency</TD>
					<TD>
						<afscme:codeWrite name="maf" property="mbrDuesFrequency" codeType="DuesFrequency" format="{1}" /> 
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
                                        <TD width="50%" class="ContentHeaderTD">Retired Date</TD>
					<TD class="ContentTD">
						<afscme:dateWrite name="maf" property="mbrRetiredDt"/> 
					</TD>
			</TR>
			</TABLE>
		</TD>
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="55%" class="ContentHeaderTD">Retiree Annual Dues Renewal</TD>
					<TD>
						<html:checkbox property="mbrRetRenewalDuesFg" name="maf" disabled="true"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD colspan="2">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH align="left" colspan="2">
						Mail 
					</TH>
				</TR>
				<TR>
					<TD width="35%" class="ContentHeaderTD">No Mail</TD>
					<TD>
						<html:checkbox property="noMailFg" name="maf" disabled="true"/>
					</TD>
				</TR>
				<TR>
				     <logic:notPresent property="vduFlag" name="maf">  	
                                        <TD class="ContentHeaderTD">No Cards</TD>
					<TD>
						<html:checkbox property="noCardsFg" name="maf" disabled="true"/>
					</TD>
                                      </logic:notPresent> 
                                </TR>
				<TR>
				     <logic:notPresent property="vduFlag" name="maf">    
                                        <TD class="ContentHeaderTD">No Public Employee</TD>
					<TD>
						<html:checkbox property="noPublicEmpFg" name="maf" disabled="true"/>
					</TD>
                                     </logic:notPresent>   
				</TR>
				<TR>
				     
                                        <TD class="ContentHeaderTD">No Legislative Mail</TD>
					<TD>
						<html:checkbox property="noLegislativeMailFg" name="maf" disabled="true"/>
					</TD>
				   
                                </TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TH>
			Officer Position 
		</TH>
		<TH>
			<!-- minimum is 9 -->
			Officer Mail To
		</TH>
	</TR>
	
        <logic:iterate name="maf" property="theOfficerInfo" id="moi" type="org.afscme.enterprise.member.MemberOfficerTitleAddressInfo">
           <TR valign="top">
           
               <%if (maf.getTheOfficerInfo() != null) { %>   
                    <TD class="ContentTD" align="center"><afscme:link page="/viewOfficerListing.action" paramId="affPk" paramName="maf" paramProperty="affPk" title="View Affiliate Officer Listing" styleClass="action"><afscme:codeWrite name="moi" property="afscmeTitleNm" codeType="AFSCMETitle" format="{1}"/></afscme:link></TD>
                <% } %> 
                <%if (maf.getTheOfficerInfo() == null) { %> 
                    <TD &nbsp; </TD> 
                 <% } %>   
                
                <TD class="ContentTD" align="center">
                    <%if (maf.getTheOfficerInfo() != null && maf.isAffRestrictedAdmin() == false) { 
                        if (moi.getMbrSuspendedFg().booleanValue() != true) { 
                        if(moi.getPosAddrFromPersonPk() != null) {%>     
                        <afscme:link page="/viewOfficerAddress.action" paramId="persAddPk" paramName="moi" paramProperty="posAddrFromPersonPk" title="View Officer Address" styleClass="action"><afscme:codeWrite name="moi" property="personAddrType" codeType="PersonAddressType" format="{1}"/></afscme:link>
                    <% } } } %>
                    <%if (maf.getTheOfficerInfo() != null && maf.isAffRestrictedAdmin() == false) { 
                        if (moi.getMbrSuspendedFg().booleanValue() != true) { 
                        if(moi.getPosAddrFromOrgPk() != null) {%>     
                        <afscme:link page="/viewOfficerAddress.action" paramId="orgAddrId" paramName="moi" paramProperty="posAddrFromOrgPk" title="View Officer Address" styleClass="action"><bean:write name="moi" property="orgAddrLocationNm"/></afscme:link>
                     <% } } } %>
                     <!-- if the affiliate is under restricted AdministratorShip display ADMINISTRATORSHIP-->
                     <%if (maf.isAffRestrictedAdmin() == true ) { %>
                         ADMINISTRATORSHIP
                    <% } %> 
                    <!-- if the officer is suspended display SUSPENDED -->
                    <%if (maf.getTheOfficerInfo() != null && maf.isAffRestrictedAdmin() == false) { 
                        if (moi.getMbrSuspendedFg().booleanValue() == true) { %>
                          SUSPENDED
                    <% } } %>
                </TD>
             
	   </TR>
	</logic:iterate>    
        </TABLE>

	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentHeaderTR">
				<BR>
				<afscme:currentPersonName showPk="true" />
                                <BR>
                                <afscme:currentAffiliate /> 
			</TD>
		</TR>
	</TABLE>
	

<!-- Something for tabs. -->
<%@ include file="/include/member_aff_tab.inc" %>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 


