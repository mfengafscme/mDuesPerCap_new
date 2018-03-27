<%! String title = "Power Affiliate Search", help = "PowerAffiliateSearch.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD 
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<html:form action="searchPowerAffiliate.action" focus="affIdType">
    <input type="hidden" name="reset">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewBasicAffiliateCriteria.action">Basic Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD colspan="2" class="ContentHeaderTR">
                Fields to Search: 
                
            </TD>
        </TR>
        <TR valign="top">
            <TD colspan="2" class="ContentHeaderTR">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="5" align="left">Affiliate Identifier</TH>
                    </TR>
                    <TR>
                        <TH width="14%">Type</TH>
                        <TH width="14%">Local/Sub Chapter</TH>
                        <TH width="20%">State/National Type</TH>
                        <TH width="14%">Sub Unit</TH>
                        <TH width="29%">Council/Retiree Chapter</TH>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdType" name="affiliateSearchForm" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form);">
                                <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdLocal" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdState" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdSubUnit" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdCouncil" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="6">
                            <HR>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="2" class="ContentHeaderTD">Include Sub-Units</TD>
                        <TD class="smallFont">
                            <html:radio name="affiliateSearchForm" property="includeSubUnits" value="true"/>
                            Yes 
                            <html:radio name="affiliateSearchForm" property="includeSubUnits" value="false"/>
                            No 
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TH colspan="2" align="left">
                General Affiliate Information 
            </TH>
        </TR>
        <TR>
            <TD width="40%" valign="top">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR valign="top">
                        <TD class="ContentHeaderTD" width="50%">Affiliate Status</TD>
                        <TD>
                            <html:select property="affiliateStatus" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="false" codeType="AffiliateStatus" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">AFSCME Legislative District</TD>
                        <TD>
                            <html:select property="afscmeLegislativeDistrict" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="false" codeType="LegislativeDistrict" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">AFSCME Region</TD>
                        <TD>
                            <html:select property="afscmeRegion" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="false" codeType="Region" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">Multiple Employers</TD>
                        <TD class="smallFont">
                            <html:radio name="affiliateSearchForm" property="multipleEmployers" value="true"/>
                            Yes 
                            <html:radio name="affiliateSearchForm" property="multipleEmployers" value="false"/>
                            No 
                            <html:radio name="affiliateSearchForm" property="multipleEmployers" value=""/>
                            All 
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">Employer Sector</TD>
                        <TD>
                            <html:select property="employerSector" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="false" codeType="EmployerSector" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
            <TD valign="top">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="40%" class="ContentHeaderTD">Sub-Local</TD>
                        <TD class="smallFont">
                            <html:radio name="affiliateSearchForm" property="allowSubLocals" value="true"/>
                            Yes 
                            <html:radio name="affiliateSearchForm" property="allowSubLocals" value="false"/>
                            No 
                            <html:radio name="affiliateSearchForm" property="allowSubLocals" value=""/>
                            All 
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">New Affiliate Identifier Source</TD>
                        <TD>
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TH class="small">
                                        Type 
                                    </TH>
                                    <TH class="small">
                                        Loc/Sub Chap 
                                    </TH>
                                    <TH class="small">
                                        State/Nat'l 
                                    </TH>
                                    <TH class="small">
                                        Sub Unit 
                                    </TH>
                                    <TH class="small">
                                        CN/Ret Chap 
                                    </TH>
                                </TR>
                                <TR VALIGN="top">

                                    <TD align="center" class="ContentTD">
                                        <html:select property="newAffiliateIdentifierSourceType" name="affiliateSearchForm" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form);">
                                            <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
                                        </html:select>
                                    </TD>
                                    <TD align="center" class="ContentTD">
                                        <html:text property="newAffiliateIdentifierSourceLocal" name="affiliateSearchForm" size="4" maxlength="4"/>
                                    </TD>
                                    <TD align="center" class="ContentTD">
                                        <html:select property="newAffiliateIdentifierSourceState" name="affiliateSearchForm">
                                            <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                                        </html:select>
                                    </TD>
                                    <TD align="center" class="ContentTD">
                                        <html:text property="newAffiliateIdentifierSourceSubUnit" name="affiliateSearchForm" size="4" maxlength="4"/>
                                    </TD>
                                    <TD align="center" class="ContentTD">
                                        <html:text property="newAffiliateIdentifierSourceCouncil" name="affiliateSearchForm" size="4" maxlength="4"/>
                                    </TD>
                                </TR>
                            </TABLE>
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">Multiple Offices</TD>
                        <TD class="smallFont">
                            <html:radio name="affiliateSearchForm" property="multipleOffices" value="true"/>
                            Yes 
                            <html:radio name="affiliateSearchForm" property="multipleOffices" value="false"/>
                            No 
                            <html:radio name="affiliateSearchForm" property="multipleOffices" value=""/>
                            All 
                        </TD>
                    </TR>
                    <TR valign="top">
                        <TD class="ContentHeaderTD">Affiliate Website</TD>
                        <TD>
                            <html:text property="website" name="affiliateSearchForm" size="25" maxlength="100"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR valign="top">
            <TD colspan="2" class="ContentHeaderTR">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="6" align="left">Addresses</TH>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Attention</TD>
                        <TD class="ContentTD" COLSPAN="5">
                            <html:text property="locationAddressAttention" name="affiliateSearchForm" size="75" maxlength="75"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Address 1</TD>
                        <TD class="ContentTD">
                            <html:text property="locationAddress1" name="affiliateSearchForm" size="50" maxlength="50"/>
                        </TD>
                        <TD class="contentheadertd">Address 2</TD>
                        <TD class="ContentTD" colspan="3">
                            <html:text property="locationAddress2" name="affiliateSearchForm" size="50" maxlength="50"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD width="12%" class="ContentHeaderTD">City</TD>
                        <TD width="35%">
                            <html:text property="locationAddressCity" name="affiliateSearchForm" size="25" maxlength="25"/>
                        </TD>
                        <TD width="18%" class="ContentHeaderTD">State</TD>
                        <TD width="15%">
                            <html:select property="locationAddressState" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="true" codeType="State" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD width="10%" class="ContentHeaderTD">Zip/Postal Code</TD>
                        <TD width="10%" >
                            <html:text property="locationAddressZip" name="affiliateSearchForm" size="12" maxlength="12"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">County</TD>
                        <TD class="ContentTD">
                            <html:text property="locationAddressCounty" name="affiliateSearchForm" size="25" maxlength="25"/>
                        </TD>
                        <TD class="ContentHeaderTD">Province</TD>
                        <TD class="ContentTD">
                            <html:text property="locationAddressProvince" name="affiliateSearchForm" size="25" maxlength="25"/>
                        </TD>
                        <TD class="ContentHeaderTD">Country</TD>
                        <TD class="ContentTD">
                            <html:select property="locationAddressCountry" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="true" codeType="Country" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Last Updated</TD>
                        <TD class="ContentTD">
                            <html:text property="locationAddressUpdatedDate" name="affiliateSearchForm" size="10" maxlength="10"/>
                            <A href="javascript:show_calendar('affiliateSearchForm.locationAddressUpdatedDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A> 
                        </TD>
                        <TD class="ContentHeaderTD">Updated By</TD>
                        <TD class="ContentTD" colspan="3">
                            <html:text property="locationAddressUpdatedByUserID" name="affiliateSearchForm" size="10" maxlength="10"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="6">
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TH align="left" colspan="6">Phone Numbers</TH>
                                </TR>
                                <TR>
                                    <TH width="15%" class="small">Code</TH>
                                    <TH width="15%" class="small">Country Code</TH>
                                    <TH width="15%" class="small">Area Code</TH>
                                    <TH width="25%" class="small">Number</TH>
                                </TR>
                                <TR>
                                    <TD align="center">Office</TD>
                                    <TD align="center">
                                        <html:text property="locationPhoneOfficeCountry" name="affiliateSearchForm" size="5" maxlength="5"/>
                                    </TD>
                                    <TD align="center">
                                        <html:text property="locationPhoneOfficeAreaCode" name="affiliateSearchForm" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
                                    </TD>
                                    <TD align="center">
                                        <html:text property="locationPhoneOfficeNumber" name="affiliateSearchForm" size="15" maxlength="15"/>
                                    </TD>
                                </TR>
                                <TR>
                                    <TD align="center">Fax</TD>
                                    <TD align="center">
                                        <html:text property="locationPhoneFaxCountry" name="affiliateSearchForm" size="5" maxlength="5"/>
                                    </TD>
                                    <TD align="center">
                                        <html:text property="locationPhoneFaxAreaCode" name="affiliateSearchForm" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
                                    </TD>
                                    <TD align="center">
                                        <html:text property="locationPhoneFaxNumber" name="affiliateSearchForm" size="15" maxlength="15"/>
                                    </TD>
                                </TR>
                            </TABLE>
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
                <afscme:button action="/viewBasicAffiliateCriteria.action">Basic Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD 
            <BR><html:errors/>
        </TD>
    </TR>
</TABLE>


<%@ include file="../include/footer.inc" %>
