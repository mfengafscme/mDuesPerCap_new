<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Vendor Member Search", help = "VendorMemberSearch.html";%>
<bean:define id="form" name="searchVendorMembersForm" type="org.afscme.enterprise.member.web.SearchVendorMembersForm"/>
<%@ include file="../include/header.inc" %>

<html:form action="searchVendorMembers.action?newSearch" focus="firstNm">
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top" align="center">
        <TD 
            <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR" colspan="3">
			Fields to Search: 
		</TD>
	</TR>
	<TR>
		<TD colspan="2" class="ContentHeaderTR">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH width="33%">
						First Name 
					</TH>
					<TH width="33%">
						Middle Name 
					</TH>
					<TH width="34%">
						Last Name 
					</TH>
				</TR>
				<TR>
					<TD align="center">
						<html:text property="firstNm" size="25" maxlength="25"/>
					</TD>
					<TD align="center">
						<html:text property="middleNm" size="25" maxlength="25"/>
					</TD>
					<TD align="center">
						<html:text property="lastNm" size="25" maxlength="25"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD class="ContentHeaderTD">
						<LABEL for="label_memberNumber"> Member Number </LABEL> 
					</TD>
					<TD>
						<html:text property="personPk" size="8" maxlength="8"/>
					</TD>
					<TD class="ContentHeaderTD">
						<LABEL for="label_ssn"> Social Security Number </LABEL> 
					</TD>
					<TD>
						<html:text property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>
						- 
						<html:text property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/>
						- 
						<html:text property="ssn3" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/> 
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD>
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TD width="11%" class="ContentHeaderTD">
						<LABEL for="label_city"> City</LABEL> 
					</TD>
					<TD width="29%">
						<html:text property="city" size="25" maxlength="25"/>
					</TD>
					<TD width="15%" class="ContentHeaderTD">
						<LABEL for="label_state"> State </LABEL> 
					</TD>
					<TD width="20%">
						<html:select property="state">
							<afscme:codeOptions codeType="State" format="{0}" allowNull="true" nullDisplay="" useCode="true"/>
                            			</html:select>
					</TD>
					<TD width="10%" class="ContentHeaderTD">
						<LABEL for="label_zip"> Zip/Postal Code</LABEL> 
					</TD>
					<TD width="15%">
						<html:text property="zipCode" size="5" maxlength="12" onkeyup="return autoTab(this, 12, event);"/>
						- 
						<html:text property="zipPlus" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR valign="top">
		<TD colspan="2" class="ContentHeaderTR">
			<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
				<TR>
					<TH colspan="6" align="left">
						Affiliate Identifier - <afscme:affiliateFinder formName="searchVendorMembersForm" affIdTypeParam="affIdType" affIdCouncilParam="affIdCouncil" affIdLocalParam="affIdLocal" affIdStateParam="affIdState" affIdSubUnitParam="affIdSubUnit" affIdCodeParam="affCode"/>						
					</TH>
				</TR>
				<TR>
					<TH width="14%">
						Type 
					</TH>
					<TH width="14%">
						Local/Sub Chapter 
					</TH>
					<TH width="20%">
						State/National Type 
					</TH>
					<TH width="14%">
						Sub Unit 
					</TH>
					<TH width="29%">
						Council/Retiree Chapter Number 
					</TH>
				</TR>
				<TR>
					<TD align="center" class="ContentTD">
						<html:select property="affIdType" name="searchVendorMembersForm" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form);">
							<afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
						</html:select>
					</TD>
					<TD align="center" class="ContentTD">
						<html:text property="affIdLocal" name="searchVendorMembersForm" size="4" maxlength="4"/>
					</TD>
					<TD align="center" class="ContentTD">
						<html:select property="affIdState" name="searchVendorMembersForm">
							<afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
						</html:select>
					</TD>
					<TD align="center" class="ContentTD">
						<html:text property="affIdSubUnit" name="searchVendorMembersForm" size="4" maxlength="4"/>
					</TD>
					<TD align="center" class="ContentTD">
						<html:text property="affIdCouncil" name="searchVendorMembersForm" size="4" maxlength="4"/>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
	<TR valign="top">
		<TD align="left">
			<BR> 
			<html:submit styleClass="button"/>
		</TD>
		<TD align="right">
			<BR>
			<html:reset styleClass="button"/>
			<BR><BR> 
		</TD>
	</TR>
</TABLE>


<logic:equal name="form" property="hasCriteria" value="true">
<logic:present name="form" property="results">

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
	<TR valign="top">
		<TD class="ContentHeaderTR" colspan="13">
			Search Results:
		</TD>
	</TR>
	<TR>
		<TD align="left" colspan="13">
			<afscme:searchNav formName="form" action="/searchVendorMembers.action"/>
		</TD>
	</TR>
	<TR>
		<TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="mbr_status">Status</afscme:sortLink>
                </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="person_nm">Name</afscme:sortLink>
                </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="address">Address</afscme:sortLink>
                </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="pa.city">City</afscme:sortLink>
                </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="pa.state">State</afscme:sortLink>
                </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="pa.zipcode">Zip</afscme:sortLink>
                </TH>
		    <TH colspan="5">
			Affiliate Association
		    </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="am.person_pk">Mbr #</afscme:sortLink>
                </TH>
                <TH>
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="mbr_type">Mbr Type</afscme:sortLink>
                </TH>
	</TR>
	<TR>
		<TD colspan="6">&nbsp;</TD>
		<TH class="small">
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="a.aff_type">Type</afscme:sortLink>
		</TH>
		<TH class="small">
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="int_local">Loc/Sub Chap</afscme:sortLink>
		</TH>
		<TH class="small">
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="a.aff_stateNat_type">State/Nat'l</afscme:sortLink>
		</TH>
		<TH class="small">
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="a.aff_SubUnit">Sub Unit</afscme:sortLink>
		</TH>
		<TH class="small">
			<afscme:sortLink styleClass="TH" action="/searchVendorMembers.action" formName="form" field="int_council">CN/Ret Chap</afscme:sortLink>
		</TH>
		<TD colspan="2">&nbsp;</TD>
	</TR>
	
	<logic:iterate id="result" name="form" property="results" type="org.afscme.enterprise.member.MemberResult">
	<TR>
		<TD align="center">
			<bean:write name="result" property="mbrStatus"/>
		</TD>
		<TD align="center">
			<bean:write name="result" property="personNm"/>
		</TD>
		<TD align="center">
			<bean:write name="result" property="address"/>
		</TD>
		<TD align="center">
			<bean:write name="result" property="city"/>
		</TD>
		<TD align="center">
			<bean:write name="result" property="state"/>
		</TD>
		<TD align="center">
			<bean:write name="result" property="zipCode"/>
		</TD>
                <afscme:affiliateIdWrite name="result" property="theAffiliateIdentifier"/> 
		<TD align="center">
			<bean:write name="result" property="personPk"/>
		</TD>
		<TD align="center">
			<bean:write name="result" property="mbrType"/>
		</TD>
	</TR>
	</logic:iterate>

</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
	<TR>
		<TD align="left">
			<afscme:searchNav formName="form" action="/searchVendorMembers.action"/>
		</TD>
	</TR>
</TABLE>

</logic:present>
</logic:equal>
</html:form>
<%@ include file="../include/footer.inc" %>

