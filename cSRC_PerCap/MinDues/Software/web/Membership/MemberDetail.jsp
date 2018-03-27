<%! String title = "Member Detail", help = "MemberDetail.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="memberDetail" id="memberDetail" type="org.afscme.enterprise.member.web.MemberDetailForm"/>
<!--  <bean:define name="memberDetail" id="personPk" property="personPk"/> -->


<!-- tabs -->
<bean:define id="screen" value="MemberDetail"/>
<%@ include file="../include/member_tab.inc" %>


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
					<TH width="24%">First Name</TH>
					<TH width="24%">Middle Name</TH>
					<TH width="24%">Last Name</TH>
					<TH width="14%">Suffix</TH>
				</TR>
				<TR>
					<TD align="center" class="ContentTD">
						<afscme:codeWrite name="memberDetail" scope="request" property="prefixNm" codeType="Prefix" format="{1}" /></TD>
					</TD>
					<TD align="center" class="ContentTD">
						<bean:write name="memberDetail" scope="request" property="firstNm"/>
					</TD>
					<TD align="center" class="ContentTD">
						<bean:write name="memberDetail" scope="request" property="middleNm"/>
					</TD>
					<TD align="center" class="ContentTD">
						<bean:write name="memberDetail" scope="request" property="lastNm"/>
					</TD>
					<TD align="center" class="ContentTD">
						<afscme:codeWrite name="memberDetail" scope="request" property="suffixNm" codeType="Suffix" format="{1}" /></TD>
                                       </TD>
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
							 <bean:write name="memberDetail" scope="request" property="nickNm"/>
						</TD>
						<TD width="15%" class="ContentHeaderTD">Member Number</TD>
						<TD width="10%" class="ContentTD">
							 <bean:write name="memberDetail" scope="request" property="personPk"/>
						</TD>
						<TD width="5%" class="ContentHeaderTD">SSN</TD>
						<TD width="18%" class="ContentTD">
							<bean:write name="memberDetail" scope="request" property="ssn1"/>-<bean:write name="memberDetail" scope="request" property="ssn2"/>-<bean:write name="memberDetail" scope="request" property="ssn3"/>
						</TD>
						<TD width="12%" class="ContentHeaderTD">Valid SSN</TD>
						<TD width="5%">
							 <html:checkbox property="ssnValid" name="memberDetail" disabled="true"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Alt Mail Name</TD>
						<TD class="ContentTD" colspan="7">
							<bean:write name="memberDetail" scope="request" property="altMailingNm"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Expelled Date</TD>
						<TD class="ContentTD">
							<afscme:dateWrite name="memberDetail" property="mbrExpelledDt"/>
						</TD>
						<TD class="ContentHeaderTD" colspan="3">Barred (from Holding Union Office)</TD>
						<TD>
							<html:checkbox property="mbrBarredFg" name="memberDetail" disabled="true"/>
						</TD>
						<TD class="ContentHeaderTD">Duplicate SSN</TD>
						<TD>
							<html:checkbox property="ssnDuplicate" name="memberDetail" disabled="true"/>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR align="center">
			<TD>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH align="left" colspan="6">
							<afscme:link page="/viewAddressMaintainence.action?back=MemberDetail" paramId="personPk" paramName="memberDetail" paramProperty="personPk" styleClass="TH">Maintain Addresses</afscme:link>
						</TH>
					</TR>
				  <!-- if no System Mailing Address then show message -->
                                     <logic:notPresent name="memberDetail" property="personAddressRecord">
                                          <TR>
                                                        <TH align="left" colspan="6">No System Mailing Address</TH>
                                          </TR>
                                        </logic:notPresent>
                                        
                                        <!-- check for a System Mailing Address -->            
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
						<TH width="16%">View</TH>
						<!-- minimum is 13 -->
						<TH colspan="5">Affiliate Associations</TH>
						<TH colspan="2">Member</TH>
						<TH width="22%">Officer Position Held</TH>
						<TH width="13%">Officer Mail To</TH>
						<!-- minimum is 9 -->
					</TR>
					<TR>
						<TD>&nbsp;
						</TD>
						<TH width="3%" class="small">Type</TH>
						<TH width="8%" class="small">Loc/Sub Chap</TH>
						<TH width="6%" class="small">State/Nat'l</TH>
						<TH width="6%" class="small">Sub Unit</TH>
						<TH width="8%" class="small">CN/Ret Chap</TH>
						<TH width="13%" class="small">Type</TH>
						<TH width="7%" class="small">Status</TH>
						<TD>&nbsp;</TD>
                                                <TD>&nbsp;</TD>
					</TR>
					<logic:iterate name="memberDetail" property="memberAffiliateData" id="mar" type="org.afscme.enterprise.member.MemberAffiliateResult">
                                        <TR valign="top">
						<TD>
                                                     <!--   <bean:define id="result" name="mar" type="org.afscme.enterprise.member.MemberAffiliateResult"/>  -->
                                                     <!-- Need to write code to put together the URL, since the link tag can only handle one param this puts one param in the action string, and the other through the one param that link supports-->
                                                        <%String action="/viewMemberAffiliateInformation.action?pk="+mar.getPersonPk();%>
                                                     <!-- the mar data object will return a '&nbsp;' if the AfscmeTitle is null in order to support the UI when the member is not an officer -->
                                                    
                                                        <afscme:link page="<%=action%>" paramId="affPk" paramName="mar" paramProperty="affPk" title="View Member Affiliate Information" styleClass="action">Member</afscme:link> 
                                                        &nbsp;|&nbsp;  
                                                        <afscme:link page="/viewAffiliateDetail.action" paramId="affPk" paramName="mar" paramProperty="affPk" title="View Affiliate Detail" styleClass="action">Affiliate</afscme:link>
                                               
                                                </TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.type"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.local"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.state"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.subUnit"/></TD>
						<TD align="center"><bean:write name="mar" property="theAffiliateIdentifier.council"/></TD>
							
						<TD align="center"><afscme:codeWrite name="mar" property="mbrType" codeType="MemberType" format="{1}"/></TD>
						<TD align="center"><afscme:codeWrite name="mar" property="mbrStatus" codeType="MemberStatus" format="{1}"/></TD>
                                                <!-- logic to display the officer title and create link if the member is an officer -->
                                                
                                                
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
					<TR>
						
                                                <TD> <afscme:link page="/viewMemberDetailAddAffiliation.action" paramId="personPk" paramName="memberDetail" paramProperty="personPk" title="Add Member to another Affiliate" styleClass="action">Add</afscme:link> </TD>
                                                <TD align="center" colspan="5">Add Member to another Affiliate</TD>
						<TD colspan="4">&nbsp;</TD>
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
										<afscme:link page="/viewPhoneNumberInformation.action?back=MemberDetail" paramId="personPk" paramName="memberDetail" paramProperty="personPk" styleClass="TH">Maintain Phone Numbers</afscme:link>
									</TH>
								</TR>
								<TR>
									<TH align="left" width="20%" class="small">Type</TH>
                                                                        <TH align="left" width="25%" class="small">Country Code</TH>
                                                                        <TH align="left" width="20%" class="small">Area Code</TH>
                                                                        <TH align="left" width="35%" class="small">Number</TH>
								</TR>
								<TR>
								    <logic:present name="memberDetail" property="phoneData">
                                                                        <logic:iterate name="memberDetail" property="phoneData" id="phoneData" type="org.afscme.enterprise.common.PhoneData">
                                                                            <TR>
                                                                            <TD><afscme:codeWrite name="phoneData" property="phoneType" codeType="PhoneType" format="{1}"/></TD>
                                                                            <TD><bean:write name="phoneData" property="countryCode"/></TD>
                                                                            <TD><bean:write name="phoneData" property="areaCode"/></TD>
                                                                            <TD><bean:write name="phoneData" property="phoneNumber"/></TD>
                                                                            </TR>
                                                                        </logic:iterate>
                                                                    </logic:present> 
                                                                </TR>        
							</TABLE>
						</TD>
						<!-- Display Person Email Info -->
                                                 <TD width="65%" valign="top">
							<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TH colspan="2" align="left">
										<afscme:link page="/viewEmailAddresses.action?back=MemberDetail" paramId="personPk" paramName="memberDetail" paramProperty="personPk" styleClass="TH">Maintain Email Addresses</afscme:link>
									</TH>
								</TR>
								<TR>
									<TD colspan="2" class="small">&nbsp;
									</TD>
								</TR>
								<TR>
								   <logic:present name="memberDetail" property="emailData">
                                                                    <logic:iterate name="memberDetail" property="emailData" id="emailData" type="org.afscme.enterprise.person.EmailData">
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
				
                                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                   <logic:present name="memberDetail" property="comment">    
                                        <TR>
						<TH align="left">
							<afscme:link page="/viewMemberCommentHistory.action" paramId="personPk" paramName="memberDetail" paramProperty="personPk" styleClass="TH">View More Comments</afscme:link>
						</TH>
					</TR>
					<TR>
						<TD><bean:write name="memberDetail" scope="request" property="comment"/></TD>
					</TR>
                                   </logic:present>
                                    <!-- if no Comment then show message -->
                                    <logic:notPresent name="memberDetail" property="comment">
                                    <tr>
                                        <th align="left" colspan="6">No Comments</th>
                                    </tr>
                                    </logic:notPresent>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
                            <logic:present name="memberDetail" property="comment">    
                                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTD">Last Updated</TD>
						<TD class="ContentTD"><afscme:dateWrite name="memberDetail" scope="request" property="commentDt"/></TD>
						<TD class="ContentHeaderTD">Updated By</TD>
						<TD class="ContentTD"><afscme:userWrite name="memberDetail" scope="request" property="commentBy"/></TD>
					</TR>
				</TABLE>
                            </logic:present>
                            
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
	

<!-- Something for tabs. -->
<%@ include file="/include/member_tab.inc" %>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
