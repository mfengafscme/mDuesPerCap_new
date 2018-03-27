<%! String title = "Member Detail Add", help = "MemberDetailAdd.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<html:form action="saveMemberDetailAdd.action" focus="prefixNm">
    <html:hidden property="ssn"/>
    <html:hidden  property="affPk"/>
    <input type="hidden" name="another"/>

<!-- Display global errors -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
        <TR></TR>
    </TABLE>

 <!-- Display buttons -->		
   <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
 	<TR valign="top">
     		<TD align="left">
     			<BR>
     			<input type="button" onclick="submitAndAddAnother(this.form)" class="button" value="Submit & Add Another"/>
                 	<html:submit styleClass="button"/>
     			<BR>
     			<BR>
     		</TD>
     		<TD align="right">
     			<BR>
     			<html:reset styleClass="button"/>&nbsp;
                 	<afscme:button action="saveMemberDetailAdd.action?cancel=goBack" prefix="&nbsp;">Cancel</afscme:button>
     		</TD>
     	</TR>
   </TABLE>

    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
<!-- Display Person Name Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="14%">Prefix</TH>
                        <TH width="24%">* First Name</TH>
                        <TH width="24%">Middle Name</TH>
                        <TH width="24%">* Last Name</TH>
                        <TH width="14%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="prefixNm">
                                <afscme:codeOptions codeType="Prefix" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="firstNm" size="25" maxlength="25"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="middleNm" size="25" maxlength="25"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="lastNm" size="25" maxlength="25"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="suffixNm">
                                <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay="[Select]"/>
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
<!-- Display Person Detail Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="12%" class="ContentHeaderTD">Nickname</TD>
                        <TD width="25%" class="ContentTD"><html:text property="nickNm" size="25" maxlength="25"/></TD>
                        <TD width="10%" class="ContentHeaderTD">SSN</TD>
                        <TD width="20%" class="ContentTD">
                            <html:text property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/> - <html:text property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/> - <html:text property="ssn3" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
                        </TD>
                        <TD width="15%" class="ContentHeaderTD">Valid SSN</TD>
                        <TD width="18%"><html:checkbox property="ssnValid"/></TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="label_AlternateMailingName">Alt Mail Name</LABEL></TD>
                        <TD class="ContentTD" colspan="7"><html:text property="altMailingNm" size="130" maxlength="130"/></TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
<!-- Display Person System Mailing Address Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH align="left" colspan="6">System Mailing Address</TH>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Address 1</TD>
                        <TD class="ContentTD"><html:text property="addr1" size="50" maxlength="50"/></TD>
                        <TD class="ContentHeaderTD">Address 2</TD>
                        <TD class="ContentTD" colspan="3"><html:text property="addr2" size="50" maxlength="50"/></TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD></TD>
                        <TD align="center"><html:errors property="addr1"/></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="addr2"/></TD>
                    </TR>
                    <TR>
                        <TD width="10%" class="ContentHeaderTD">City</TD>
                        <TD width="35%"><html:text property="city" size="25" maxlength="25"/></TD>
                        <TD width="10%" class="ContentHeaderTD">State</TD>
                        <TD width="20%">
                            <html:select property="state">
                                <afscme:codeOptions codeType="State" useCode="true" format="{0}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                        </TD>
                        <TD width="10%" class="ContentHeaderTD">Zip/Postal Code</TD>
                        <TD width="15%">
                            <html:text property="zipCode" size="5" maxlength="12" onkeyup="return autoTab(this, 12, event);"/> - <html:text property="zipPlus" size="4" maxlength="4"/>
                        </TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD></TD>
                        <TD align="center"><html:errors property="city"/></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="state"/></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="zipCode"/></TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">County</TD>
                        <TD class="ContentTD"><html:text property="county" size="25" maxlength="25"/></TD>
                        <TD class="ContentHeaderTD">Province</TD>
                        <TD class="ContentTD"><html:text property="province" size="25" maxlength="25"/></TD>
                        <TD class="ContentHeaderTD">Country</TD>
                        <TD class="ContentTD">
                            <html:select property="countryPk">
                                <afscme:codeOptions codeType="Country" format="{1}" allowNull="false"/>
                            </html:select>
                        </TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="province"/></TD>
                        <TD></TD>
                        <TD></TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Code</TD>
                        <TD class="ContentTD">Home</TD>
                        <TD class="ContentHeaderTD">Bad Address</TD>
                        <TD class="ContentTD"><html:checkbox property="bad"/></TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD></TD>
                        <TD align="center"><html:errors property="code"/></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="bad"/></TD>
                    </TR>
                 </TABLE>
            </TD>
        </TR>
<!-- Display affiliate and member fields -->
        <TR align="center">
            <TD colspan="2" class="ContentHeaderTR">
		<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
		   <TR>
			<TH colspan="5" align="left">* Affiliate Identifier - <afscme:affiliateFinder formName="memberDetailAddForm" affIdTypeParam="theAffiliateIdentifier.type" affIdCouncilParam="theAffiliateIdentifier.council" affIdLocalParam="theAffiliateIdentifier.local" affIdStateParam="theAffiliateIdentifier.state" affIdSubUnitParam="theAffiliateIdentifier.subUnit" affIdCodeParam="theAffiliateIdentifier.code"/></TH>
                        <TH colspan="2" align="center">* Member</TH>
                        <TH colspan="2" align="center">* Join Date</TH>
                 </TR>
		  <TR>
			<TH width="14%">Type</TH>
			<TH width="14%">Local/Sub Chapter</TH>
			<TH width="20%">State/National Type</TH>
			<TH width="14%">Sub Unit</TH>
			<TH width="29%">Council/Retiree Chapter</TH>
                        <TH>Type</TH>
                        <TH>Status</TH>
                        <TH>Month</TH>
                        <TH>Year</TH>
		 </TR>
		 <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="theAffiliateIdentifier.type" onchange="clearHiddenFields(this.form);">

                                <afscme:codeOptions useCode="true" codeType="AffiliateType" format="{0}" allowNull="true" nullDisplay="" excludeCodes="C"/>
								
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD" >
                            <html:text property="theAffiliateIdentifier.local" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/></TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="theAffiliateIdentifier.state" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select></TD>
                        <TD align="center" class="ContentTD"> 
                             <html:text property="theAffiliateIdentifier.subUnit" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/></TD>
                        <TD align="center" class="ContentTD">
                             <html:text property="theAffiliateIdentifier.council" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/></TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="mbrType">
                                <afscme:codeOptions codeType="MemberType" format="{1}" allowNull="false" nullDisplay=""/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="mbrStatus">
                                <afscme:codeOptions codeType="MemberStatus" format="{1}" allowNull="false" nullDisplay=""/>
                            </html:select>
                        </TD> 
                        <TD align="center" class="ContentTD">
                            <html:select property="monthJoined">
                                <afscme:monthOptions allowNull="false" nullDisplay=""/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="yearJoined" size="4" maxlength="4"/>
                        </TD>
                 </TR>
                 <TR>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="mbrType"/></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="yearJoined"/></TD>
                 </TR>
                </TABLE>
            <TD>
        </TR>    
<!-- Display Person Phone Numbers -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="0" cellspacing="0" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="35%" valign="top">
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TH colspan="4" align="left">Phone Numbers</TH>
                                </TR>
                                <TR>
                                    <TH align="left" width="20%" class="small">Type</TH>
                                    <TH align="left" width="25%" class="small">Country Code</TH>
                                    <TH align="left" width="20%" class="small">Area Code</TH>
                                    <TH align="left" width="35%" class="small">Number</TH>
                                </TR>
                                <TR>
                                    <TD>
                                        <html:select property="phoneType">
                                            <afscme:codeOptions codeType="PhoneType" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                                        </html:select>
                                    </TD>
                                    <TD><html:text property="countryCode" size="5" maxlength="5"/></TD>
                                    <TD><html:text property="areaCode" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/></TD>
                                    <TD><html:text property="phoneNumber" size="15" maxlength="15"/></TD>
                                </TR>
<!-- Display validation errors -->		
                                <TR>
                                    <TD colspan="4" align="center"><html:errors property="phoneType"/></TD>
                                </TR>
                            </TABLE>
                        </TD>
<!-- Display Person Email Info -->
                        <TD width="65%" valign="top">
                            <TABLE cellpadding="2" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TH colspan="2" align="left">Email Addresses</TH>
                                </TR>
                                <TR>
                                    <TH align="left" width="20%" class="small">Type</TH>
                                    <TH align="left" width="20%" class="small">Email</TH>
                                </TR>
                                <TR>
                                    <TD width="20%">Primary</TD>
                                    <TD width="80%"><html:text property="personEmailAddrPrimary" size="50" maxlength="50"/></TD>
                                </TR>
                                <TR>
                                    <TD>Alternate</TD>
                                    <TD width="80%"><html:text property="personEmailAddrAlternate" size="50" maxlength="50"/></TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>

<!-- Display Person Comment -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH align="left">Enter New Comments</TH>
                    </TR>
                    <TR>
                        <TD>
                            <html:textarea property="comment" onkeyup="validateComments(this);" cols="105" rows="3"></html:textarea>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>

<!-- Display buttons -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
        <tr>
            <td align="left">
            <input type="button" onclick="submitAndAddAnother(this.form)" class="button" value="Submit & Add Another"/>
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="saveMemberDetailAdd.action?cancel=goBack" prefix="&nbsp;">Cancel</afscme:button>
           </td>
        </tr>      
        <tr>
            <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
        </tr>
    </TABLE>
</html:form>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
