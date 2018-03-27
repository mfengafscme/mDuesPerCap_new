<%! String title = "Member Affiliate Information Edit", help = "MemberAffiliateInformationEdit.html";%>

<%@ include file="../include/header.inc" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define name="memberAffiliateInformationForm" id="maf" type="org.afscme.enterprise.member.web.MemberAffiliateInformationForm"/>

<%-- <% request.setAttribute("onload", "setMailFlagsByMemberType(this.form);"); %> --%>


<html:form action="saveMemberAffiliateInformation.action" focus="mbrType">

<!-- Send required fields back with form parameters -->

<html:hidden property="personPk"/>
<html:hidden property="affPk"/>
<html:hidden property="affType"/>

<!-- Display global errors -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
</TABLE>
<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewMemberAffiliateInformation.action">Cancel</afscme:button>
           </td>
        </tr>      
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
</TABLE>
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
					<TD class="ContentHeaderTD" WIDTH="50%">* Member Type</TD>
					<TD class="ContentTD">
                                        <%if (maf.getAffType().trim().equals("R") | maf.getAffType().trim().equals("S")) { %>   
                                           <html:select name="maf" property="mbrType" onchange="setMailFlagsByMemberType(this.form);" onblur="setMailFlagsByMemberType(this.form);" >	
                                                <afscme:codeOptions codeType="MemberType" format="{1}" allowNull="false" excludeCodes="R,A,U,O"/>
                                            </html:select>
                                        <% } else { %>
                                            <html:select name="maf" property="mbrType" onchange="setMailFlagsByMemberType(this.form);" onblur="setMailFlagsByMemberType(this.form);" >
                                                <afscme:codeOptions codeType="MemberType" format="{1}" allowNull="false" />
                                            </html:select>
                                        <% } %>        
                                        </TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">* Member Status</TD>
					<TD class="ContentTD">
                                            <html:select name="maf" property="mbrStatus">	
						<afscme:codeOptions codeType="MemberStatus" format="{1}" allowNull="false"/>
                                            </html:select>     
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
						<html:checkbox property="lostTimeLanguageFg" name="maf" disabled="false"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="50%" class="ContentHeaderTD">* Join Date</TD>
                                        <TD class="ContentTD">
                                            <html:select name="maf" property="monthJoined">
                                                <afscme:monthOptions allowNull="false" nullDisplay=""/>
                                            </html:select>
                                        </TD>
                                        <TD class="ContentTD">
                                            <html:text property="yearJoined" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
                                        </TD>
				</TR>
                                <TR>
                                        <TD></TD>
                                        <TD align="center"><html:errors property="yearJoined"/></TD>
                                </TR>
                        </TABLE>
		</TD>
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="55%" class="ContentHeaderTD">Dues Type<TD>
                                            <html:select name="maf" property="mbrDuesType">
                                                <afscme:codeOptions codeType="DuesType" format="{1}" allowNull="true" nullDisplay=""/>
                                            </html:select>    
                                        </TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Dues Rate</TD>
					<TD>
						<input type="text" name="mbrDuesRate" size="7" maxlength="7" value="<bean:write name="maf" property="mbrDuesRate" format="0.00"/>">  USD 
					</TD>
				</TR>
				<TR>
					<TD class="ContentHeaderTD">Dues Frequency</TD>
					<TD>
                                            <html:select name="maf" property="mbrDuesFrequency">
						<afscme:codeOptions codeType="DuesFrequency" format="{1}" allowNull="true" nullDisplay=""/> 
                                            </html:select>    
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
                                            <html:select name="maf" property="monthRetired">
                                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                                            </html:select>
                                        </TD>
                                        <TD class="ContentTD">
                                            <html:text name="maf" property="yearRetired" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
                                        </TD>
                                </TR>
                                <TR>
                                    <TD align="center"><html:errors property="monthRetired"/></TD>
                                    <TD align="center"><html:errors property="yearRetired"/></TD>
                                </TR>    
			</TABLE>
		</TD>
		<TD width="50%">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="55%" class="ContentHeaderTD">Retiree Annual Dues Renewal</TD>
					<TD>
						<html:checkbox property="mbrRetRenewalDuesFg" name="maf" disabled="false"/>
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
						<html:hidden property="noMailFlag" name="maf" value="<%= maf.getNoMailFg().toString() %>"/>
					<logic:notPresent property="vduFlag" name="maf"> 	
                                                <html:checkbox property="noMailFg" name="maf" disabled="false" onclick="setAllMailFlags(this.form);reverseFlag(form.noMailFlag);"/>
					</logic:notPresent>
                                        <logic:present property="vduFlag" name="maf"> 	
                                                <html:checkbox property="noMailFg" name="maf" disabled="false" onclick="setAllMailFlagsVdu(this.form);reverseFlag(form.noMailFlag);"/>
                                        </logic:present>
                                        </TD>
				</TR>
				<TR>
                                   <logic:notPresent property="vduFlag" name="maf"> 		
                                        <TD class="ContentHeaderTD">No Cards</TD>
					<TD>
                                                <!-- must be submitted regardless of disabled value -->
                                                <html:hidden property="noCardsFlag" name="maf" value="<%= maf.getNoCardsFg().toString() %>"/>
						<%if (maf.getMbrType() == org.afscme.enterprise.codes.Codes.MemberType.A | maf.getMbrType() == org.afscme.enterprise.codes.Codes.MemberType.O) { %>
                                                    <html:checkbox property="noCardsFg" name="maf" disabled="true" onclick="reverseFlag(form.noCardsFlag);"/>
                                                <%} else { %>        
                                                    <html:checkbox property="noCardsFg" name="maf" disabled="false" onclick="reverseFlag(form.noCardsFlag);"/>
                                                <% } %>     
					</TD>
                                    </logic:notPresent>           
				</TR>
				<TR>                      
                                   <logic:notPresent property="vduFlag" name="maf"> 
                                        <TD class="ContentHeaderTD">No Public Employee</TD>
					<TD>
                                                <!-- must be submitted regardless of disabled value -->
                                                <html:hidden property="noPublicEmpFlag" name="maf" value="<%= maf.getNoPublicEmpFg().toString() %>"/> 
						<%if (maf.getMbrType() == org.afscme.enterprise.codes.Codes.MemberType.S) { %>
                                                    <html:checkbox property="noPublicEmpFg" name="maf" disabled="true" onclick="reverseFlag(form.noPublicEmpFlag);"/>
                                                <%} else { %>
                                                    <html:checkbox property="noPublicEmpFg" name="maf" disabled="false" onclick="reverseFlag(form.noPublicEmpFlag);"/>
                                                <% } %>    
                                        </TD>
                                    </logic:notPresent> 
				</TR>
				<TR>
                                        <TD class="ContentHeaderTD">No Legislative Mail</TD>
					<TD>
						<!-- must be submitted regardless of disabled value -->
						<html:hidden property="noLegislativeMailFlag" name="maf" value="<%= maf.getNoLegislativeMailFg().toString() %>"/>
                                                <%if (maf.getMbrType() == org.afscme.enterprise.codes.Codes.MemberType.A) { %>
            						<html:checkbox property="noLegislativeMailFg" name="maf" disabled="true" onclick="reverseFlag(form.noLegislativeMailFlag);"/>
                                                <%} else { %>        
                                                        <html:checkbox property="noLegislativeMailFg" name="maf" disabled="false" onclick="reverseFlag(form.noLegislativeMailFlag);"/>
                                                <% } %> 
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
                    <%if (maf.getTheOfficerInfo() != null & maf.isAffRestrictedAdmin() == false) { 
                        if (moi.getMbrSuspendedFg().booleanValue() != true) { 
                        if(moi.getPosAddrFromPersonPk() != null) {%>     
                        <afscme:link page="/viewOfficerAddress.action" paramId="persAddPk" paramName="moi" paramProperty="posAddrFromPersonPk" title="View Officer Address" styleClass="action"><afscme:codeWrite name="moi" property="personAddrType" codeType="PersonAddressType" format="{1}"/></afscme:link>
                    <% } } } %>
                    <%if (maf.getTheOfficerInfo() != null & maf.isAffRestrictedAdmin() == false) { 
                        if (moi.getMbrSuspendedFg().booleanValue() != true) { 
                        if(moi.getPosAddrFromOrgPk() != null) {%>     
                        <afscme:link page="/viewOfficerAddress.action" paramId="orgAddrId" paramName="moi" paramProperty="posAddrFromOrgPk" title="View Officer Address" styleClass="action"><bean:write name="moi" property="orgAddrLocationNm"/></afscme:link>
                     <% } } } %>
                     <!-- if the affiliate is under restricted AdministratorShip display ADMINISTRATORSHIP-->
                     <%if (maf.isAffRestrictedAdmin() == true ) { %>
                         ADMINISTRATORSHIP
                    <% } %> 
                    <!-- if the officer is suspended display SUSPENDED -->
                    <%if (maf.getTheOfficerInfo() != null & maf.isAffRestrictedAdmin() == false) { 
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
<!-- Display buttons -->		
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <tr valign="top">
                <td colspan="3"><BR></td>
            </tr>   
            <tr>
                <td align="left"><html:submit styleClass="button"/></td>
                <td align="right">
                    <html:reset styleClass="button"/>&nbsp;
                    <afscme:button action="/viewMemberAffiliateInformation.action">Cancel</afscme:button>
                </td>
            </tr>      
            <tr>
                <!-- Required Content -->
                <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
            </tr>
    </TABLE>
</html:form>	

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 


