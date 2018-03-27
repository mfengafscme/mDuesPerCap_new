<%! String title = "Member Detail Edit", help = "MemberDetailEdit.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<!-- Member Detail Edit page (modeled from Person Detail Edit)-->

<bean:define name="memberDetailForm" id="memberDetail" type="org.afscme.enterprise.member.web.MemberDetailForm"/>
<!--  <bean:define name="memberDetail" id="personPk" property="personPk"/> -->



<html:form action="saveMemberDetail.action" focus="prefixNm">
    <html:hidden property="personPk"/>
    <html:hidden property="ssn"/>

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
                <afscme:button action="/viewMemberDetail.action">Cancel</afscme:button>
           </td>
        </tr>      
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <afscme:currentPersonName showPk="true" /> 
            <BR> <BR> 
	</TD>
    </TR>
</TABLE> 

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR align="center">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="14%">Prefix</TH>
					<TH width="24%">* First Name</TH>
					<TH width="24%">Middle Name</TH>
					<TH width="24%">* Last Name</TH>
					<TH width="14%">Suffix</TH>
				</TR>
				<TR>
					<TD align="center" class="ContentTD">
                                            <html:select name="memberDetailForm" property="prefixNm">
                                                <afscme:codeOptions codeType="Prefix" format="{1}" nullDisplay="[Select]" allowNull="true"/></TD>
                                            </html:select>    
                                        </TD>
					<TD align="center" class="ContentTD">
						<html:text name="memberDetailForm" property="firstNm" size="25" maxlength="25"/>
					</TD>
					<TD align="center" class="ContentTD">
						<html:text name="memberDetailForm" property="middleNm" size="25" maxlength="25"/>
					</TD>
					<TD align="center" class="ContentTD">
						<html:text name="memberDetailForm" property="lastNm" size="25" maxlength="25"/>
					</TD>
					<TD align="center" class="ContentTD">
                                            <html:select name="memberDetailForm" property="suffixNm">
                                                <afscme:codeOptions codeType="Suffix" format="{1}" nullDisplay="[Select]" allowNull="true"/></TD>
                                            </html:select>
                                       </TD>
				</TR>
                                <!-- Display validation errors -->		
                                <TR>
                                    <TD></TD>
                                    <TD align="center"><html:errors property="firstNm"/></TD>
                                    <TD></TD>
                                    <TD align="center"><html:errors property="lastNm"/></TD>
                                    <TD></TD>
                                </TR>
			</TABLE>
			</TD>
		</TR>
		<TR align="center">
			<TD>
				<TABLE width="100%" cellpadding="2" cellspacing="0" border="0" class="InnerContentTable">
					<TR>
						<TD width="15%" class="ContentHeaderTD">Nickname</TD>
						<TD width="20%" class="ContentTD">
							 <html:text name="memberDetailForm" property="nickNm" size="25" maxlength="25"/>
						</TD>
						<TD width="15%" class="ContentHeaderTD">Member Number</TD>
						<TD width="10%" class="ContentTD">
							 <bean:write name="memberDetailForm" property="personPk"/>
						</TD>
						<TD width="5%" class="ContentHeaderTD">SSN</TD>
						<TD width="18%" class="ContentTD">
							<html:text name="memberDetailForm" property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>-<html:text name="memberDetailForm" property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/>-<html:text name="memberDetailForm" property="ssn3" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
						</TD>        
						<TD width="12%" class="ContentHeaderTD">Valid SSN</TD>
						<TD width="5%">
							 <html:checkbox property="ssnValid" name="memberDetailForm"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Alt Mail Name</TD>
						<TD class="ContentTD" colspan="7">
							<html:text name="memberDetailForm" property="altMailingNm" size="130" maxlength="130"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Expelled Date</TD>
						<TD class="ContentTD">
							<html:select name="memberDetailForm" property="monthExpelled">
                                                            <afscme:monthOptions allowNull="true" nullDisplay=""/>
                                                        </html:select>
                                                        <html:text name="memberDetailForm" property="yearExpelled" size="4" maxlength="4"/>
						</TD>
						<TD class="ContentHeaderTD" colspan="3">Barred (from Holding Union Office)</TD>
						<TD>
							<html:checkbox property="mbrBarredFg" name="memberDetailForm"/>
						</TD>
						<TD class="ContentHeaderTD">Duplicate SSN</TD>
						<TD>
							<html:checkbox property="ssnDuplicate" name="memberDetailForm"/>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR align="center">
			<TD>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                       <logic:notPresent name="memberDetail" property="personAddressRecord">
                                          <TR>
                                              <TH align="left" colspan="6">No System Mailing Address</TH>
                                          </TR>
                                       </logic:notPresent>
					
                                      <logic:present name="memberDetail" property="personAddressRecord">    
                                        <bean:define id="address" name="memberDetail" property ="personAddressRecord" type="org.afscme.enterprise.address.PersonAddressRecord"/>
                                        <TR>
                                            	<TH align="left" colspan="6">System Mailing Address</TH>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Address 1</TD>
						<TD class="ContentTD"><bean:write name="address" property="addr1"/></TD>
						<TD class="ContentHeaderTD">Address 2</TD>
						<TD class="ContentTD" colspan="3"><bean:write name="address" property="addr2"/></TD>
					</TR>
					<TR>
						<TD width="11%" class="ContentHeaderTD">City</TD>
						<TD width="29%"><bean:write name="address" property="city"/></TD>
						<TD width="15%" class="ContentHeaderTD">State</TD>
						<TD width="25%"><afscme:codeWrite name="address" property="state" useCode="true" codeType="State" format="{0}"/></TD>
						<TD width="10%" class="ContentHeaderTD">Zip/Postal Code</TD>
						<TD width="15%">
							<logic:notEqual name="address" property="zipCode" value="0">
                                                              <bean:write name="address" property="zipCode"/>
                                                        </logic:notEqual>
                                                        <logic:notEqual name="address" property="zipPlus" value="0">
                                                             -<bean:write name="address" property="zipPlus"/>
                                                        </logic:notEqual>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">County</TD>
						<TD class="ContentTD"><bean:write name="address" property="county"/></TD>
						<TD class="ContentHeaderTD">Province</TD>
                                                <TD class="ContentTD"><bean:write name="address" property="province"/></TD>
						<TD class="ContentHeaderTD">Country</TD>
                                                <TD class="ContentTD"><afscme:codeWrite name="address" property="countryPk" codeType="Country" format="{1}"/></TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Bad Address</TD>
                                                <TD class="ContentTD"><html:checkbox property="bad" name="address" disabled="true"/></TD>
                                                <TD class="ContentHeaderTD">Date Marked Bad</TD>
                                                <TD class="ContentTD" colspan="3"><afscme:dateWrite name="address" property="badDate"/></TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Last Updated</TD>
                                                <TD class="ContentTD"><afscme:dateWrite name="address" property="recordData.modifiedDate"/></TD>
                                                <TD class="ContentHeaderTD">Updated By</TD>
                                                <TD class="ContentTD" colspan="3"><afscme:userWrite name="address" property="recordData.modifiedBy"/></TD>
					</TR> 
                                      </logic:present>  
                                </TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR valign="top">
						
						<!-- minimum is 13 -->
						<TH colspan="5">Affiliate Associations</TH>
						<TH colspan="2">Member</TH>
						<TH width="24%">Officer Position Held</TH>
						<TH width="11%">Officer Mail To</TH>
						<!-- minimum is 9 -->
					</TR>
					<TR>
						
						<TH width="3%" class="small">Type</TH>
						<TH width="8%" class="small">Loc/Sub Chap</TH>
						<TH width="6%" class="small">State/Nat'l</TH>
						<TH width="6%" class="small">Sub Unit</TH>
						<TH width="8%" class="small">CN/Ret Chap</TH>
						<TH width="13%" class="small">Type</TH>
						<TH width="7%" class="small">Status</TH>
						<TD colspan="2">&nbsp;
						</TD>
					</TR>
					<logic:iterate name="memberDetailForm" property="memberAffiliateData" id="mar" type="org.afscme.enterprise.member.MemberAffiliateResult">
                                        <TR valign="top">
						
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.type"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.local"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.state"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.subUnit"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.council"/></TD>
							
						<TD align="center"><afscme:codeWrite name="mar" property="mbrType" codeType="MemberType" format="{1}"/></TD>
						<TD align="center"><afscme:codeWrite name="mar" property="mbrStatus" codeType="MemberStatus" format="{1}"/></TD>
                                                <!-- in this case no links if person is an officer :   logic to display the officer title  -->
                                                <TD>
                                                <Table>
                                                   <logic:iterate name="mar" property="theOfficerInfo" id="moi" type="org.afscme.enterprise.member.MemberOfficerTitleAddressInfo"> 
                                                    <TR>
                                                        <%if (mar.getTheOfficerInfo() != null) { %>   
                                                             <TD align="center"><afscme:link page="/viewOfficerListing.action" paramId="affPk" paramName="mar" paramProperty="affPk" title="View Affiliate Officer Listing" styleClass="action"><afscme:codeWrite name="moi" property="afscmeTitleNm" codeType="AFSCMETitle" format="{1}"/></afscme:link></TD>
                                                        <% } %> 
                                                        <%if (mar.getTheOfficerInfo() == null) { %> 
                                                            <TD &nbsp; </TD> 
                                                        <% } %>   
                                                     </TR>                       
                                                 </logic:iterate> 
                                                 </Table>
                                                 </TD>
                                             <TD>
                                                 <Table>
                                                 <logic:iterate name="mar" property="theOfficerInfo" id="moi2" type="org.afscme.enterprise.member.MemberOfficerTitleAddressInfo"> 
                                                    <TR>
                                                        <!-- logic to display the officer mail to and create link to officer address if the member is an officer who is not suspended in an affiliate not under restricted administratorship, otherwise display SUSPENDED or ADMINISTRATORSHIP -->
                                                            <TD align="center">
                                                            <%if (mar.getTheOfficerInfo() != null && mar.isAffRestrictedAdmin() == false) { 
                                                            if (moi2.getMbrSuspendedFg().booleanValue() != true) { 
                                                                if(moi2.getPosAddrFromPersonPk() != null) {%>     
                                                                <afscme:link page="/viewOfficerAddress.action" paramId="persAddPk" paramName="moi" paramProperty="posAddrFromPersonPk" title="View Officer Address" styleClass="action"><afscme:codeWrite name="moi" property="personAddrType" codeType="PersonAddressType" format="{1}"/></afscme:link>
                                                            <% } } } %>
                                                            <%if (mar.getTheOfficerInfo() != null && mar.isAffRestrictedAdmin() == false) { 
                                                            if (moi2.getMbrSuspendedFg().booleanValue() != true) { 
                                                                if(moi2.getPosAddrFromOrgPk() != null) {%>     
                                                                <afscme:link page="/viewOfficerAddress.action" paramId="orgAddrId" paramName="moi" paramProperty="posAddrFromOrgPk" title="View Officer Address" styleClass="action"><bean:write name="moi" property="orgAddrLocationNm"/></afscme:link>
                                                            <% } } } %>
                                                            <!-- if the affiliate is under restricted AdministratorShip display ADMINISTRATORSHIP-->
                                                            <%if (mar.isAffRestrictedAdmin() == true ) { %>
                                                                ADMINISTRATORSHIP
                                                            <% } %> 
                                                            <!-- if the officer is suspended display SUSPENDED -->
                                                            <%if (mar.getTheOfficerInfo() != null && mar.isAffRestrictedAdmin() == false) { 
                                                                if (moi2.getMbrSuspendedFg().booleanValue() == true) { %>
                                                                SUSPENDED
                                                            <% } } %>
                                                            </TD>
                                                    </TR>
                                                  </logic:iterate>      
                                               </Table>
                                            </TD>    
                                        </TR>
					 </logic:iterate>
                                       	<TR>
						<TD colspan="10">&nbsp;</TD>
					</TR>
					
				</TABLE>
			</TD>
		</TR>
		<TR align="center">
			<TD>
				<TABLE width="100%" cellpadding="0" cellspacing="0" border="0" class="InnerContentTable">
					<TR>
						<TD width="35%" valign="top">
							<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TH colspan="4" align="left">
										Phone Numbers
									</TH>
								</TR>
								<TR>
									<TH align="left" width="20%" class="small">Type</TH>
                                                                        <TH align="left" width="25%" class="small">Country Code</TH>
                                                                        <TH align="left" width="20%" class="small">Area Code</TH>
                                                                        <TH align="left" width="35%" class="small">Number</TH>
								</TR>
								<TR>
								    <logic:present name="memberDetailForm" property="phoneData">
                                                                        <logic:iterate name="memberDetailForm" property="phoneData" id="phoneData" type="org.afscme.enterprise.common.PhoneData">
                                                                            <TR>
                                                                            <TD><afscme:codeWrite name="phoneData" property="phoneType" codeType="PhoneType" format="{1}"/></TD>
                                                                            <TD><bean:write name="phoneData" property="countryCode"/></TD>
                                                                            <TD><bean:write name="phoneData" property="areaCode"/></TD>
                                                                            <TD><bean:write name="phoneData" property="phoneNumber"/></TD>
                                                                            </TR>
                                                                        </logic:iterate>
                                                                    </logic:present>        
							</TABLE>
						</TD>
						<!-- Display Person Email Info -->
                                                 <TD width="65%" valign="top">
							<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TH colspan="2" align="left">
										Email Addresses
									</TH>
								</TR>
								<TR>
									<TD colspan="2" class="small">&nbsp;
									</TD>
								</TR>
								<TR>
								   <logic:present name="memberDetailForm" property="emailData">
                                                                    <logic:iterate name="memberDetailForm" property="emailData" id="emailData" type="org.afscme.enterprise.person.EmailData">
                                                                     <TR>
                                                                            <TD  width="20%">
                                                                                <afscme:codeWrite name="emailData" property="emailType" codeType="EmailType" format="{1}"/>
                                                                            </TD>
                                                                            <TD width="80%">
                                                                                <bean:write name="emailData" property="personEmailAddr"/>
                                                                            </TD>
                                                                    </TR>
                                                                </logic:iterate>
							       </logic:present>	
                                                                </TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR align="center">
			<TD>
				
                               <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH align="left">Enter New Comments</TH>
                    </TR>
                    <TR>
                        <TD>
                            <html:textarea name="memberDetailForm" property="comment" onkeyup="validateComments(this);" cols="105" rows="3"></html:textarea>
                        </TD>
                    </TR>
                </TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
                                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTD">Last Updated</TD>
                                    <%--        <TD class="ContentTD"><afscme:dateWrite name="memberDetailForm" property="commentDt"/></TD>  --%>
						<TD class="ContentHeaderTD">Updated By</TD>
				    <%--	<TD class="ContentTD"><afscme:userWrite name="memberDetailForm" property="commentBy"/></TD> --%>
					</TR>
				</TABLE>
                           
                            
			</TD> 
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentHeaderTR">
				<BR>
				<afscme:currentPersonName showPk="true" /> 
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
                    <afscme:button action="/viewMemberDetail.action">Cancel</afscme:button>
                </td>
            </tr>      
            <tr>
                <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
            </tr>
    </TABLE>
</html:form>


<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
