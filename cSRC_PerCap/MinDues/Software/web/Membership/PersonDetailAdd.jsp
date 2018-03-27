<%! String title = "Person Detail - Add", help = "PersonDetailAdd.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<html:form action="savePersonDetailAdd.action" focus="prefixNm">
    <html:hidden property="ssn"/>

<!-- Display global errors -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
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
                        <TD align="center"><html:errors property="middleNm"/></TD>
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
                        <TH align="left" colspan="6">Primary Address</TH>
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
                            <html:text property="zipCode" size="5" maxlength="12"/> - <html:text property="zipPlus" size="4" maxlength="4"/>
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
                                    <TD><html:text property="areaCode" size="3" maxlength="3"/></TD>
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
                <afscme:button action="savePersonDetailAdd.action?another">Submit & Add Another</afscme:button>
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button forward="MainMenu" prefix="&nbsp;">Cancel</afscme:button>
           </td>
        </tr>      
        <tr>
            <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
        </tr>
    </TABLE>
</html:form>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
