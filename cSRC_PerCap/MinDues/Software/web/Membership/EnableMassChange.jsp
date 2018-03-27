<%@ page import="org.afscme.enterprise.masschange.web.MassChangeForm" %>
<%! String title = "Enable Mass Change", help = "EnableMassChange.html"; %>
<% request.setAttribute("onload", "initializeChangeForm();"); %>
<SCRIPT language="JavaScript" src="/js/masschange.js"></SCRIPT>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<!-- Start Main Content -->
<html:form action="saveEnableMassChange">
    <html:hidden property="mergedAffiliatePk"/>
    <html:hidden property="mergedAffiliate.code"/>
    <html:hidden property="splitAffiliatePk"/>
    <html:hidden property="splitAffiliate.code"/>
    <html:hidden property="infoSourceCurrent"/>
    <html:hidden property="statusCurrent"/>
    <html:hidden property="affiliateIdCurrent.type"/>
    <html:hidden property="affiliateIdCurrent.local"/>
    <html:hidden property="affiliateIdCurrent.state"/>
    <html:hidden property="affiliateIdCurrent.subUnit"/>
    <html:hidden property="affiliateIdCurrent.council"/>
    <html:hidden property="affiliateIdCurrent.code"/>

    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button" title="Apply Mass Change" onclick="return confirm('Are you sure you want to apply the requested change on this Affiliate and all its Sub-Affiliates?');"/>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewMembershipReportingInformation.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <afscme:currentAffiliate />
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>

    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TH align="left" width="4%">Select</TH>
            <TH align="left" width="29%">Field Name</TH>
            <TH align="left" width="25%">Current Value</TH>
            <TH align="left" width="41%">New Value</TH>
        </TR>
        <TR>
            <TD ALIGN="center">
                <html:radio property="massChangeSelect" value="<%=String.valueOf(MassChangeForm.MASS_CHANGE_TYPE_STATUS)%>" onclick="setFieldsForStatusChange(massChangeForm);"/>
            </TD>
            <TD class="ContentHeaderTD">
                Affiliate Status Change
            </TD>
            <TD class="ContentTD">
                <afscme:codeWrite property="statusCurrent" codeType="AffiliateStatus" format="{1}"/> 
            </TD>
            <TD class="ContentHeaderTD">
                <!--INPUT type="text" name="NewStatus" value="" size="30" style="border:none;font-weight:bold" readonly-->
                <INPUT type="text" name="NewStatus" value="" size="30" class="smallNoBorder" readonly>
            </TD>
        </TR>
        <TR>
            <TD rowspan="2">&nbsp;</TD>
            <TD rowspan="2" class="ContentTD">
                <html:radio property="statusChangeSelect" value="<%=String.valueOf(MassChangeForm.STATUS_CHANGE_MERGED)%>" onclick="setFieldsForMergedStatus(this.form);" disabled="true"/>
                Set to Merged
            </TD>
            <TD rowspan="2">
                <INPUT type="text" name="MergedStatusText" value="" size="35" maxlength="25" class="smallNoBorder" readonly> 
            </TD>
            <TD class="ContentTD">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH class="small">Type</TH>
                        <TH class="small">Loc/Sub Chap</TH>
                        <TH class="small">State/Nat'l</TH>
                        <TH class="small">Sub Unit</TH>
                        <TH class="small">CN/Ret Chap</TH>
                        <TD>&nbsp;</TD>
                    </TR>
                    <TR>
                        <TD align="center">
                            <html:select property="mergedAffiliate.type" onchange="lockMergeAffiliateID(this.form, this);" disabled="true">
                                <OPTION value="">*</OPTION>
                                <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="false" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center">
                            <html:text property="mergedAffiliate.local" size="4" maxlength="4" onfocus="this.select();" onblur="MergedAffiliateLocalJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <html:select property="mergedAffiliate.state" onchange="MergedAffiliateStateJS = this.selectedIndex;" disabled="true">
                                <OPTION value="">*</OPTION>
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="false" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center">
                            <html:text property="mergedAffiliate.subUnit" size="4" maxlength="4" onfocus="this.select();" onblur="MergedAffiliateSubunitJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <html:text property="mergedAffiliate.council" size="4" maxlength="4" onfocus="this.select();" onblur="MergedAffiliateCouncilJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <afscme:affiliateFinder formName="massChangeForm" affPkParam="mergedAffiliatePk" affIdTypeParam="mergedAffiliate.type" affIdCouncilParam="mergedAffiliate.council" affIdLocalParam="mergedAffiliate.local" affIdStateParam="mergedAffiliate.state" affIdSubUnitParam="mergedAffiliate.subUnit" affIdCodeParam="mergedAffiliate.code" linkName="Finder1" styleClass="action" title="Retrieve your Affilitate Identifier" onclick="if (disabled) return false;" onblur="resetMergedAffiliateJSvars(massChangeForm);"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TD>
                <html:errors property="mergedAffiliate"/>
            </TD>
        </TR>
        <TR>
            <TD rowspan="2">&nbsp;</TD>
            <TD rowspan="2" class="ContentTD">
                <html:radio property="statusChangeSelect" value="<%=String.valueOf(MassChangeForm.STATUS_CHANGE_SPLIT)%>" onclick="setFieldsForSplitStatus(this.form);" disabled="true"/>
                Set to Split
            </TD>
            <TD rowspan="2" class="ContentTD">
                <INPUT type="Text" name="SplitStatusText" value="" size="35" maxlength="25" class="smallNoBorder" readonly> 
            </TD>
            <TD class="ContentTD">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH class="small">Type</TH>
                        <TH class="small">Loc/Sub Chap</TH>
                        <TH class="small">State/Nat'l</TH>
                        <TH class="small">Sub Unit</TH>
                        <TH class="small">CN/Ret Chap</TH>
                        <TD>&nbsp;</TD>
                    </TR>
                    <TR>
                        <TD align="center">
                            <html:select property="splitAffiliate.type" onchange="lockSplitAffiliateID(this.form, this);" disabled="true">
                                <OPTION value="">*</OPTION>
                                <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="false" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center">
                            <html:text property="splitAffiliate.local" size="4" maxlength="4" onfocus="this.select();" onblur="SplitAffiliateLocalJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <html:select property="splitAffiliate.state" onchange="SplitAffiliateStateJS = this.selectedIndex;" disabled="true">
                                <OPTION value="">*</OPTION>
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="false" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center">
                            <html:text property="splitAffiliate.subUnit" size="4" maxlength="4" onfocus="this.select();" onblur="SplitAffiliateSubunitJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <html:text property="splitAffiliate.council" size="4" maxlength="4" onfocus="this.select();" onblur="SplitAffiliateCouncilJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <afscme:affiliateFinder formName="massChangeForm" affPkParam="splitAffiliatePk" affIdTypeParam="splitAffiliate.type" affIdCouncilParam="splitAffiliate.council" affIdLocalParam="splitAffiliate.local" affIdStateParam="splitAffiliate.state" affIdSubUnitParam="splitAffiliate.subUnit" affIdCodeParam="splitAffiliate.code" linkName="Finder2" styleClass="action" title="Retrieve your Affilitate Identifier" onclick="if (disabled) return false;" onblur="resetSplitAffiliateJSvars(massChangeForm)"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TD>
                <html:errors property="splitAffiliate"/>
            </TD>
        </TR>
        <TR>
            <TD rowspan="2">&nbsp;</TD>
            <TD class="ContentTD">
                <html:radio property="statusChangeSelect" value="<%=String.valueOf(MassChangeForm.STATUS_CHANGE_DEACTIVATED)%>" onclick="setFieldsForDeactivatedStatus(this.form);" disabled="true"/>
                Set to Deactivated 
            </TD>
            <TD class="ContentTD">
                <INPUT type="Text" name="DeactivatedStatusText" value="" size="35" maxlength="25" class="smallNoBorder" readonly> 
            </TD>
            <TD class="ContentTD">&nbsp;</TD>
        </TR>
        <TR>
            <TD COLSPAN="3">
                <html:errors property="statusChangeSelect"/>
            </TD>
        </TR>
        <TR>
            <TD ALIGN="center">
                <html:radio property="massChangeSelect" value="<%=String.valueOf(MassChangeForm.MASS_CHANGE_TYPE_OTHER)%>" onclick="setFieldsForOtherChanges(massChangeForm);"/>
            </TD>
            <TD class="ContentHeaderTD" COLSPAN="3">
                Other Mass Change Types
            </TD>
        </TR>
        <TR>
            <TD rowspan="2" class="ContentHeaderTD">&nbsp;</TD>
            <TD rowspan="2" class="ContentTD">
                <html:checkbox property="infoSourceFg" onclick="setFieldsForInfoSource(this.form);" disabled="true"/>
                Membership Information Reporting Source 
            </TD>
            <TD rowspan="2" class="ContentTD">
                <afscme:codeWrite property="infoSourceCurrent" codeType="InformationSource" format="{1}"/> 
            </TD>
            <TD class="ContentTD">
                <html:select property="infoSourceNew" onchange="infosourceJS = this.selectedIndex;" disabled="true">
                    <OPTION value="-1">*</OPTION>
                    <afscme:codeOptions allowNull="false" codeType="InformationSource" format="{1}"/>
                </html:select>
            </TD>
        </TR>
        <TR>
            <TD>
                <html:errors property="infoSourceNew"/>
            </TD>
        </TR>
        <TR>
            <TD class="ContentHeaderTD">&nbsp;</TD>
            <TD class="ContentTD">
                <html:checkbox property="mbrCardBypassFg" onclick="setFieldsForNoMbrCards(this.form);" disabled="true"/>
                Unit Wide No Member Cards 
            </TD>
            <TD class="ContentHeaderTD">
                <html:checkbox property="mbrCardBypassFgCurrent" disabled="true"/>
            </TD>
            <TD class="ContentHeaderTD">
                <html:checkbox property="mbrCardBypassFgNew" disabled="true"/>
            </TD>
        </TR>
        <TR>
            <TD class="ContentHeaderTD">&nbsp;</TD>
            <TD class="ContentTD">
                <html:checkbox property="peMailBypassFg" onclick="setFieldsForNoPEMail(this.form);" disabled="true"/>
                Unit Wide No PE Mail
            </TD>
            <TD class="ContentHeaderTD">
                <html:checkbox property="peMailBypassFgCurrent" disabled="true"/>
            </TD>
            <TD class="ContentHeaderTD">
                <html:checkbox property="peMailBypassFgNew" disabled="true"/>
            </TD>
        </TR>
        <TR>
            <TD rowspan="2" class="ContentHeaderTD">&nbsp;</TD>
            <TD class="ContentTD">
                <html:checkbox property="mbrRenewalFg" onclick="setFieldsForMemberRenewal(this.form);" disabled="true"/>
                Retiree Annual Dues Renewal 
            </TD>
            <TD class="ContentHeaderTD">&nbsp;</TD>
            <TD class="ContentHeaderTD">
                <html:checkbox property="mbrRenewalFgNew" disabled="true"/>
            </TD>
        </TR>
        <TR>
            <TD colspan="3">
                <html:errors property="mbrRenewalFg"/>
            </TD>
        </TR>
        <TR>
            <TD rowspan="3" class="ContentHeaderTD">&nbsp;</TD>
            <TD rowspan="2" class="ContentTD">
                <html:checkbox property="newAffiliateFg" onclick="setFieldsForNewAffiliateIdentifier(this.form);" disabled="true"/>
                Affiliate Identifier                 
            </TD>
            </TD>
            <TD rowspan="2" class="ContentTD">
                <afscme:affiliateIdWrite name="massChangeForm" scope="request" property="affiliateIdCurrent" tdClass="ContentTD" wrapTable="true"/>
            </TD>
            <TD class="ContentTD">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH class="small">Type</TH>
                        <TH class="small">Loc/Sub Chap</TH>
                        <TH class="small">State/Nat'l</TH>
                        <TH class="small">Sub Unit</TH>
                        <TH class="small">CN/Ret Chap</TH>
                        <TD>&nbsp;</TD>
                    </TR>
                    <TR>
                        <TD align="center">                    
                        <html:select property="newAffiliate.type" onchange="lockNewAffiliateID(this.form, this);" disabled="true">
                            	<OPTION value="">*</OPTION>
                        <logic:equal name="massChangeForm" property="affiliateIdCurrent.type" value="C">
                                <OPTION value="C">C</OPTION>                            
                        </logic:equal>                  
                        <logic:equal name="massChangeForm" property="affiliateIdCurrent.type" value="R">
                                <OPTION value="R">R</OPTION>                            
                        </logic:equal>                   
                        <logic:equal name="massChangeForm" property="affiliateIdCurrent.type" value="L">
                                <OPTION value="L">L</OPTION>
                                <OPTION value="#">#</OPTION>                                
                        </logic:equal>
                        <logic:equal name="massChangeForm" property="affiliateIdCurrent.type" value="S">
                                <OPTION value="S">S</OPTION>
                                <OPTION value="#">#</OPTION>                                                 
                        </logic:equal>
                        <logic:equal name="massChangeForm" property="affiliateIdCurrent.type" value="U">                       
                                <OPTION value="U">U</OPTION>                                                   
                        </logic:equal>
                        </html:select>
                        </TD>                        
                  

                        <TD align="center">
                            <html:text property="newAffiliate.local" size="4" maxlength="4" onfocus="this.select();" onblur="NewAffiliateLocalJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <html:select property="newAffiliate.state" onchange="NewAffiliateStateJS = this.selectedIndex;" disabled="true">
                                <OPTION value="">*</OPTION>
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="false" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center">
                            <html:text property="newAffiliate.subUnit" size="4" maxlength="4" onfocus="this.select();" onblur="NewAffiliateSubunitJS = this.value;" disabled="true"/>
                        </TD>
                        <TD align="center">
                            <html:text property="newAffiliate.council" size="4" maxlength="4" onfocus="this.select();" onblur="NewAffiliateCouncilJS = this.value;" disabled="true"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TD>
                <html:errors property="newAffiliate"/>
            </TD>
        </TR>
        <TR>
            <TD COLSPAN="3">
                <html:errors property="otherChangeSelect"/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TD class="ContentHeaderTR">
                <BR>
                <afscme:currentAffiliate/>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button" title="Apply Mass Change" onclick="return confirm('Are you sure you want to apply the requested change on this Affiliate and all its Sub-Affiliates?');"/>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewMembershipReportingInformation.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="56%" align="center">
        <TR>
            <TD align="center" class="smallFontI">
                * Note: Performing a Mass Change on any of these fields, will apply that change to all 
                Affiliates, below this one, in the hierarchy. Changes made on this screen will be
		reflected after the Mass Change process is executed (at a scheduled time). 
		To change a field for this Affiliate only, use the 'Edit' tab on the previous screen. 
		<br><br>
		# Note: Selecting this option when editing the Affiliate Identifier will 
		associate/disassociate Locals and Sub-Chapters from Councils.  If a Council
		is already associated to this Affiliate, the only option available is no Council.
            </TD>
        </TR>
    </TABLE>
</html:form>
<!-- End Main Content -->


<%@ include file="../include/footer.inc" %> 
