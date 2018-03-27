<%! String title = "Affiliate Detail Add", help = "AffiliateDetailAdd.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<html:form action="saveAffiliateDetailAdd.action" focus="affIdType">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR>
                <html:submit styleClass="button"/>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR> 
                <html:reset styleClass="button"/>
                <afscme:button action="/showMain.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD colspan="2" class="ContentHeaderTR">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="6" align="left">Affiliate Identifier</TH>
                    </TR>
                    <TR>
                        <TH width="17%">* Type</TH>
                        <TH width="17%">* Local/Sub Chapter</TH>
                        <TH width="24%">* State/National Type</TH>
                        <TH width="17%">* Sub Unit</TH>
                        <TH width="25%">* Council/Retiree Chapter</TH>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdType" name="affiliateDetailAddForm" onblur="lockAffiliateIDFieldsAdd(this.form);" onchange="lockAffiliateIDFieldsAdd(this.form);">
                                <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdLocal" name="affiliateDetailAddForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdState" name="affiliateDetailAddForm">
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdSubUnit" name="affiliateDetailAddForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdCouncil" name="affiliateDetailAddForm" size="4" maxlength="4"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD>
                            <html:errors property="affIdType"/>
                        </TD>
                        <TD align="center">
                            <html:errors property="affIdLocal"/>
                        </TD>
                        <TD align="center">
                            <html:errors property="affIdState"/>
                        </TD>
                        <TD align="center">
                            <html:errors property="affIdSubUnit"/>
                        </TD>
                        <TD align="center">
                            <html:errors property="affIdCouncil"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="55%" class="ContentHeaderTD">
                                * Abbreviated Affiliate Name 
                        </TD>
                        <TD width="45%" class="ContentTD">
                            <html:text property="affilName" name="affiliateDetailAddForm" size="29" maxlength="29"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="affilName"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                                * Affiliate Status 
                        </TD>
                        <TD>
                            <html:select property="affilStatus" name="affiliateDetailAddForm">
                                <afscme:codeOptions useCode="false" codeType="AffiliateStatus" allowNull="true" nullDisplay="" format="{1}" excludeCodes="D,PD,M,S" />
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="affilStatus"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                                * Annual Card Run Type 
                        </TD>
                        <TD>
                            <html:select property="annualCardRunType" name="affiliateDetailAddForm">
                                <afscme:codeOptions useCode="false" codeType="AffiliateCardRunType" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="annualCardRunType"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
            <TD width="50%" valign="top">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD class="ContentHeaderTD">
                            AFSCME Legislative District 
                        </TD>
                        <TD>
                            <html:select property="afscmeLegislativeDistrict" name="affiliateDetailAddForm">
                                <afscme:codeOptions useCode="false" codeType="LegislativeDistrict" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                                * AFSCME Region 
                        </TD>
                        <TD>
                            <html:select property="afscmeRegion" name="affiliateDetailAddForm">
                                <afscme:codeOptions useCode="false" codeType="Region" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="afscmeRegion"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                            Multiple Employers 
                        </TD>
                        <TD>
                            <html:checkbox property="multipleEmployers" name="affiliateDetailAddForm"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                            Sub-Locals Allowed
                        </TD>
                        <TD>
                            <html:checkbox property="subLocalsAllowed" name="affiliateDetailAddForm"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TH colspan="2" align="left">
                Charter Information 
            </TH>
        </TR>
        <TR>
            <TD colspan="2" class="ContentHeaderTD">
                * Charter Name 
            </TD>
        </TR>
        <TR>
            <TD colspan='2'>
                <html:errors property="charterName"/>
            </TD>
        </TR>
        <TR>
            <TD colspan="2" class="ContentHeaderTD">
                <html:textarea property="charterName" name="affiliateDetailAddForm" onkeyup="validateCharterName(this);" cols="105" rows="3"></html:textarea> 
            </TD>
        </TR>
        <TR>
            <TD colspan="2" class="ContentHeaderTD">
                * Charter Jurisdiction 
            </TD>
        </TR>
        <TR>
            <TD colspan='2'>
                <html:errors property="charterJurisdiction"/>
            </TD>
        </TR>
        <TR>
            <TD colspan="2" class="ContentHeaderTD">
                <html:textarea property="charterJurisdiction" name="affiliateDetailAddForm" onkeyup="validateCharterJurisdiction(this);" cols="105" rows="5"></html:textarea> 
            </TD>
        </TR>
        <TR valign="top">
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="45%" class="ContentHeaderTD">
                            * Charter Code 
                        </TD>
                        <TD width="55%" class="ContentTD">
                            <html:select property="charterCode" name="affiliateDetailAddForm">
                                <afscme:codeOptions useCode="false" codeType="CharterCode" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="charterCode"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                            * Charter Date 
                        </TD>
                        <TD class="ContentTD">
                            <html:select property="charterDateMonth" name="affiliateDetailAddForm">
                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:text property="charterDateYear" name="affiliateDetailAddForm" size="4" maxlength="4"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD>
                            <html:errors property="charterDateMonth"/>
                        </TD>
                        <TD>
                            <html:errors property="charterDateYear"/>
                        </TD>                        
                    </TR>
                </TABLE>
                <html:hidden property="effectiveDateMonth" name="affiliateDetailAddForm"/>
                <html:hidden property="effectiveDateYear" name="affiliateDetailAddForm"/>
            </TD>
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="40%" class="ContentHeaderTD">
                                County 
                        </TD>
                        <TD width="60%" class="ContentTD">
                            <html:text property="charterCounty1" name="affiliateDetailAddForm" size="25" maxlength="25"/> *
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="charterCounty1"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">&nbsp;
                        </TD>
                        <TD class="ContentTD">
                            <html:text property="charterCounty2" name="affiliateDetailAddForm" size="25" maxlength="25"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">&nbsp;
                        </TD>
                        <TD class="ContentTD">
                            <html:text property="charterCounty3" name="affiliateDetailAddForm" size="25" maxlength="25"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
                <TH colspan="2" align="left">
                        Constitution Information 
                </TH>
        </TR>
        <TR valign="top">
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="55%" class="ContentHeaderTD">
                                Approved Constitution 
                        </TD>
                        <TD width="45%">
                            <html:checkbox property="approvedConstitution" name="affiliateDetailAddForm"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                                Generate Default Officer Titles 
                        </TD>
                        <TD>
                            <html:checkbox property="generateDefaultOffices" name="affiliateDetailAddForm"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="45%" class="ContentHeaderTD">
                                Affiliate Agreement Date 
                        </TD>
                        <TD class="ContentTD">
                            <html:select property="affilAgreementDateMonth" name="affiliateDetailAddForm">
                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:text property="affilAgreementDateYear" name="affiliateDetailAddForm" size="4" maxlength="4"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR>
                <html:submit styleClass="button"/>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR> 
                <html:reset styleClass="button"/>
                <afscme:button action="/showMain.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align="center">
                <BR><B><I>* Indicates Required Fields</I></B>
                <BR>
            </TD>
        </TR>
    </TABLE>
</html:form>


<%@ include file="../include/footer.inc" %> 
