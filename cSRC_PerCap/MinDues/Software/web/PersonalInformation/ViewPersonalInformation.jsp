<%@ taglib uri	="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri	="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "My Personal Information", help = "MyPersonalInformation.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="editMyInfo" name="mif" type="org.afscme.enterprise.myinfo.web.MyInfoForm">
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR>
			<TD>
				<BR>
				<afscme:button action="/editMyInfo.action">Update My Personal Information</afscme:button>  
				<BR>
				<BR> 
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<bean:define id="personData" name="mid" property="personData"/>
		<TR>
			<TD class="ContentTD" colspan="3">
				<LABEL for="label_MbrNumber">AFSCME ID Number</LABEL> &nbsp;&nbsp;&nbsp;<bean:write name="personData" property="personPk"/> <BR> <BR> 
			</TD>
		</TR>
		<TR valign="top">
			<TD colspan="3">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH>
							Prefix 
						</TH>
						<TH>
							First Name 
						</TH>
						<TH>
							Middle Name 
						</TH>
						<TH>
							Last Name 
						</TH>
						<TH>
							Suffix 
						</TH>
					</TR>
					<TR>
						<TD align="center" width="3%">
							<afscme:codeWrite name="personData" property="prefixNm" codeType="Prefix" format="{1}"/>
						</TD>
						<TD align="center" width="10%">
							<logic:notEmpty name="personData" property="firstNm">
								<bean:write name="personData" property="firstNm"/>
							</logic:notEmpty>
						</TD>
						<TD align="center" width="5%">
							<logic:notEmpty name="personData" property="middleNm">
								<bean:write name="personData" property="middleNm"/>
							</logic:notEmpty>
						</TD>
						<TD align="center" width="15%">
							<logic:notEmpty name="personData" property="lastNm">
								<bean:write name="personData" property="lastNm"/>
							</logic:notEmpty>
						</TD>
						<TD align="center" width="3%">
							<afscme:codeWrite name="personData" property="suffixNm" codeType="Suffix" format="{1}"/>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR valign="top">
			<TD colspan="3">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="8" align="left">
							Primary Mailing Address 
						</TH>
					</TR>
					<logic:notEmpty name="incorrectAddress">
						<TR>
							<TH ALIGN="left" COLSPAN="6">
								Incorrect Address
							</TH>
						</TR>
						<TR>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Address1"> Address 1 </LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="incorrectAddress" property="addr1">
									<bean:write name="incorrectAddress" property="addr1"/> 
								</logic:notEmpty> 
							</TD>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Address2"> Address 2 </LABEL> 
							</TD>
							<TD class="ContentTD" colspan="3">
								<logic:notEmpty name="incorrectAddress" property="addr2">
									<bean:write name="incorrectAddress" property="addr2"/> 
								</logic:notEmpty> 
								</TD>
						</TR>
						<TR>
							<TD width="11%" class="ContentHeaderTD">
								<LABEL for="label_City"> City</LABEL> 
							</TD>
							<TD width="30%">
								<logic:notEmpty name="incorrectAddress" property="city">
									<bean:write name="incorrectAddress" property="city"/> 
								</logic:notEmpty> 
							</TD>
							<TD width="14%" class="ContentHeaderTD">
								<LABEL for="label_state"> State </LABEL> 
							</TD>
							<TD width="20%">
								<logic:notEmpty name="incorrectAddress" property="state">
									<bean:write name="incorrectAddress" property="state"/>
								</logic:notEmpty> 
							</TD>
							<TD width="10%" class="ContentHeaderTD">
								<LABEL for="label_ZipCode"> Zip/Postal Code </LABEL> 
							</TD>
							<TD width="15%">
								<logic:notEmpty name="incorrectAddress" property="zipCode">
									<bean:write name="incorrectAddress" property="zipCode"/>
								</logic:notEmpty>
								<logic:notEmpty name="incorrectAddress" property="zipPlus">
									- 
									<bean:write name="incorrectAddress" property="zipPlus"/>
								</logic:notEmpty> 
							</TD>
						</TR>
						<TR>
							<TD class="ContentHeaderTD">
								<LABEL for="label_County"> County </LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="incorrectAddress" property="county">
									<bean:write name="incorrectAddress" property="county"/> 
								</logic:notEmpty> 
							</TD>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Province">Province</LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="incorrectAddress" property="province">
									<bean:write name="incorrectAddress" property="province"/> 
								</logic:notEmpty>
							</TD>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Country"> Country </LABEL> 
							</TD>
							<TD class="ContentTD">
								<afscme:codeWrite name="incorrectAddress" property="countryPk" codeType="Country" format="{1}"/>
							</TD>
						</TR>
						<TR>
							<TD COLSPAN="6">
								&nbsp;
							</TD>
						</TR>
					</logic:notEmpty>
					<logic:notEmpty name="incorrectAddress">
						<TR>
							<TH ALIGN="left" COLSPAN="6">
								Corrected Address
							</TH>
						</TR>
					</logic:notEmpty>
						<TR>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Address1"> Address 1 </LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="addr1">
										<bean:write name="personAddressRecord" property="addr1"/> 
									</logic:notEmpty>
								</logic:notEmpty>
							</TD>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Address2"> Address 2 </LABEL> 
							</TD>
							<TD class="ContentTD" colspan="3">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="addr2">
										<bean:write name="personAddressRecord" property="addr2"/> 
									</logic:notEmpty>
								</logic:notEmpty>
							</TD>
						</TR>
						<TR>
							<TD width="11%" class="ContentHeaderTD">
								<LABEL for="label_City"> City</LABEL> 
							</TD>
							<TD width="30%">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="city">
										<bean:write name="personAddressRecord" property="city"/> 
									</logic:notEmpty>
								</logic:notEmpty>
							</TD>
							<TD width="14%" class="ContentHeaderTD">
								<LABEL for="label_state"> State </LABEL> 
							</TD>
							<TD width="20%">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="state">
										<bean:write name="personAddressRecord" property="state"/>
									</logic:notEmpty>
								</logic:notEmpty>
							</TD>
							<TD width="10%" class="ContentHeaderTD">
								<LABEL for="label_ZipCode"> Zip/Postal Code </LABEL> 
							</TD>
							<TD width="15%">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="zipCode">
										<bean:write name="personAddressRecord" property="zipCode"/>
									</logic:notEmpty>
									<logic:notEmpty name="personAddressRecord" property="zipPlus">
										-
										<bean:write name="personAddressRecord" property="zipPlus"/>
									</logic:notEmpty>
								</logic:notEmpty>
							</TD>
						</TR>
						<TR>
							<TD class="ContentHeaderTD">
								<LABEL for="label_County"> County </LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="county">
										<bean:write name="personAddressRecord" property="county"/> 
									</logic:notEmpty>
								</logic:notEmpty>	
							</TD>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Province">Province</LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<logic:notEmpty name="personAddressRecord" property="province">
										<bean:write name="personAddressRecord" property="province"/> 
									</logic:notEmpty>
								</logic:notEmpty>
							</TD>
							<TD class="ContentHeaderTD">
								<LABEL for="label_Country"> Country </LABEL> 
							</TD>
							<TD class="ContentTD">
								<logic:notEmpty name="mid" property="personAddressRecord">
									<bean:define id="personAddressRecord" name="mid" property="personAddressRecord"/>
									<afscme:codeWrite name="personAddressRecord" property="countryPk" codeType="Country" format="{1}"/> 
								</logic:notEmpty>
							</TD>
						</TR>
						</logic:notEmpty>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD valign="top" width="50%">
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="5">
							Phone Numbers 
						</TH>
					</TR>
					<TR>
						<TH width="15%" class="small">
							Incorrect 
						</TH>
						<TH width="20%" class="small">
							Type 
						</TH>
						<TH width="20%" class="small">
							Country Code 
						</TH>
						<TH width="20%" class="small">
							Area Code 
						</TH>
						<TH width="25%" class="small">
							Number 
						</TH>
					</TR>
					<logic:notEmpty name="personData" property="thePhoneData">
						<logic:iterate id="thePhoneData" name="personData" property="thePhoneData" type="org.afscme.enterprise.common.PhoneData">
							<TR align="center">
								<TD class="ContentTD" align="center">
									<html:multibox  name="mif" property="checkedPhones" value="<%= thePhoneData.getPhonePk().toString() %>" disabled="true"/> 
								</TD>
								<TD class="ContentTD">
									<afscme:codeWrite name="thePhoneData" property="phoneType" codeType="PhoneType" format="{1}"/>
								</TD>
								<TD class="ContentTD">
									<logic:notEmpty name="thePhoneData" property="countryCode">
										<bean:write name="thePhoneData" property="countryCode"/>
									</logic:notEmpty>
								</TD>
								<TD class="ContentTD">
									<logic:notEmpty name="thePhoneData" property="areaCode">
										<bean:write name="thePhoneData" property="areaCode"/>
									</logic:notEmpty>
								</TD>
								<TD class="ContentTD">
									<bean:write name="thePhoneData" property="phoneNumber"/> 
								</TD>
							</TR>
						</logic:iterate>
					</logic:notEmpty>
					<TR>
						<TD align="center" colspan="5">&nbsp;</TD>
					</TR>
					<TR>
						<TD align="center">
							<html:link page="/addPhoneNumber.action" styleClass="action">
								Add
							</html:link>
						</TD>
						<TD align="center" colspan="2">
							New Phone Number 
						</TD>
						<TD align="center" colspan="2">&nbsp;</TD>
					</TR>
				</TABLE>
			</TD>
			<TD valign="top" width="50%">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="2">
							Email Addresses 
						</TH>
					</TR>
					<TR>
						<TH align="left" class="small">
							Type 
						</TH>
						<TH align="left" class="small">
							Address 
						</TH>
					</TR>
					<logic:iterate id="theEmailData" name="personData" property="theEmailData">
						<TR>
							<TD class="ContentTD" width="15%">
								<afscme:codeWrite name="theEmailData" property="emailType" codeType="EmailType" format="{1}"/>
							</TD>
							<TD class="ContentTD" width="85%">
								<logic:notEmpty name="theEmailData" property="personEmailAddr">
									<bean:write name="theEmailData" property="personEmailAddr"/>
								</logic:notEmpty>
							</TD>
						</TR>
					</logic:iterate>
				</TABLE>
			</TD>
		</TR>
		<TR valign="top">
			<TD width="100%" colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="7">
							Affiliate Associations 
						</TH>
					</TR>
					<TR>
						<TH class="small">
							Name
						</TH>
						<TH WIDTH="5%" class="small">
							Type 
						</TH>
						<TH WIDTH="10%" class="small">
							Local/Sub Chapter 
						</TH>
						<TH WIDTH="8%" class="small">
							State/National 
						</TH>
						<TH WIDTH="5%" class="small">
							Sub Unit 
						</TH>
						<TH WIDTH="12%" class="small">
							Council/Retiree Chapter 
						</TH>
						<TH width="4%">&nbsp;
						</TH>
					</TR>
					<logic:iterate id="memberAffiliateResult" name="mid" property="memberAffiliateResults">
					<TR>	
						<TD align="center">
							<bean:write name="memberAffiliateResult" property="abbreviatedName"/>
						</TD>
						<afscme:affiliateIdWrite name="memberAffiliateResult" property="theAffiliateIdentifier"/>
						<TD class="smallFONT" align="center">
							<logic:equal name="memberAffiliateResult" property="mbrStatus" value="<%= org.afscme.enterprise.codes.Codes.MemberStatus.I.toString() %>">
								(Past)
							</logic:equal>
						</TD>
					</TR>
					</logic:iterate>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR>
			<TD>
				<BR>
				<afscme:button action="/editMyInfo.action">Update My Personal Information</afscme:button> 
				<BR>
				<BR> 
			</TD>
		</TR>
	</TABLE>
</html:form>
<%@ include file="../include/footer.inc" %>
