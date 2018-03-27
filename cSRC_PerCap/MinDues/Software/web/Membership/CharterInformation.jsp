<%! String title = "Charter Information", help = "CharterInformation.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="charterForm" id="charterForm" type="org.afscme.enterprise.affiliate.web.CharterForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="CharterInformation"/>
<%@ include file="../include/affiliate_tab.inc" %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

<!-- Start Main Content -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TH colspan="2" align="left">Charter Name</TH>
    </TR>
    <TR>
        <TD colspan="2">
            <bean:write name="charterForm" property="name"/>
        </TD>
    </TR>
    <TR>
        <TH colspan="2" align="left">Charter Jurisdiction</TH>
    </TR>
    <TR>
        <TD colspan="2">
            <bean:write name="charterForm" property="jurisdiction"/>
        </TD>
    </TR>
    <TR>
        <TH colspan="2" align="left">General Charter Information</TH>
    </TR>
    <TR valign="top">
        <TD width="50%">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="40%" class="ContentHeaderTD">Affiliate Status</TD>
                    <TD width="60%" class="ContentTD">
                        <afscme:codeWrite name="charterForm" property="status" codeType="AffiliateStatus" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Charter Code</TD>
                    <TD class="ContentTD">
                        <afscme:codeWrite name="charterForm" property="code" codeType="CharterCode" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Last Change Effective Date</TD>
                    <TD class="ContentTD">
                        <logic:present name="charterForm" property="lastChangeEffectiveDateMonth">
                            <logic:present name="charterForm" property="lastChangeEffectiveDateYear">                                
                                <logic:lessThan name="charterForm" property="lastChangeEffectiveDateMonth" value="10">0</logic:lessThan><bean:write name="charterForm" property="lastChangeEffectiveDateMonth"/>/<bean:write name="charterForm" property="lastChangeEffectiveDateYear"/>
                            </logic:present>
                        </logic:present>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Charter Date</TD>
                    <TD class="ContentTD">
                        <logic:present name="charterForm" property="dateMonth">
                            <logic:present name="charterForm" property="dateYear">
                                <logic:lessThan name="charterForm" property="dateMonth" value="10">0</logic:lessThan><bean:write name="charterForm" property="dateMonth"/>/<bean:write name="charterForm" property="dateYear"/>
                            </logic:present>
                        </logic:present>
                    </TD>
                </TR>
            </TABLE>
        </TD>
        <TD width="50%">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="40%" class="ContentHeaderTD">County</TD>
                    <TD width="60%" class="ContentTD">
                        <bean:write name="charterForm" property="county1"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">&nbsp;</TD>
                    <TD class="ContentTD">
                        <bean:write name="charterForm" property="county2"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">&nbsp;</TD>
                    <TD class="ContentTD">
                        <bean:write name="charterForm" property="county3"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR>
        <TH colspan="2" align="left">
            Council Affiliations
        </TH>
    </TR>
    <TR>
        <TD colspan="2">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable" align="center">
                <TR>
                    <TH width="10%">Select</TH>
                    <TH width="45%">Abbreviated Affiliate Name</TH>
                    <TH width="45%" colspan="5">Affiliate Identifier</TH>
                </TR>
                <TR>
                    <TD colspan="2">&nbsp;</TD>
                    <TH class="small">Type</TH>
                    <TH class="small">Local/Sub Chapter</TH>
                    <TH class="small">State/National Type</TH>
                    <TH class="small">Sub Unit</TH>
                    <TH class="small">Council/Retiree Chapter</TH>
                </TR>
                <logic:notPresent name="charterForm" property="councilAffiliations">
                    <TR>
                        <TD align="center">
                            <logic:equal name="charterForm" property="canAssociateWithCouncil" value="true">
                                <afscme:link page='/addCouncilAffiliation.action' styleClass='action' title='Associate with new Council'>Associate</afscme:link>
                            </logic:equal>
                            <logic:equal name="charterForm" property="canAssociateWithCouncil" value="false">&nbsp;</logic:equal>
                        </TD>
                        <TD ALIGN="center">
                            <bean:message key="message.charter.noCouncilAffiliations"/>
                        </TD>
                        <TD colspan="5">&nbsp;</TD>
                    </TR>
                </logic:notPresent>
                <logic:present name="charterForm" property="councilAffiliations">
                    <logic:iterate id="council" name="charterForm" property="councilAffiliations" type="org.afscme.enterprise.affiliate.AffiliateData">
                        <TR>
                            <TD align="center">
                                <logic:equal name="charterForm" property="canAssociateWithCouncil" value="false">&nbsp;</logic:equal>
                                <logic:equal name="charterForm" property="canAssociateWithCouncil" value="true">
                                    <logic:notPresent name="charterForm" property="reportingCouncilPk">&nbsp;</logic:notPresent>
                                    <logic:present name="charterForm" property="reportingCouncilPk">
                                        <bean:define id="reportingCouncilPk" name="charterForm" property="reportingCouncilPk"/>
                                        <logic:equal name="council" property="affPk" value="<%=reportingCouncilPk.toString()%>">
                                            <afscme:link page='/deleteCouncilAffiliation.action' styleClass='action' title='Disassociate from Council' confirm='Are you sure you want to disassociate this Affiliate from its Council?'>Disassociate</afscme:link>
                                        </logic:equal>
                                        <logic:notEqual name="council" property="affPk" value="<%=reportingCouncilPk.toString()%>">&nbsp;</logic:notEqual>
                                    </logic:present>
                                </logic:equal>
                            </TD>
                            <TD align="center">
                                <bean:write name="council" property="abbreviatedName"/>
                            </TD>
                            <afscme:affiliateIdWrite name="council" property="affiliateId"/>
                        </TR>
                    </logic:iterate>
                </logic:present>
            </TABLE>
        </TD>
    </TR>
</TABLE>
<!-- End Main Content -->

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR">
            <BR>
            <afscme:currentAffiliate />
        </TD>
    </TR>
</TABLE>

<!-- Something for tabs. -->
<%@ include file="/include/affiliate_tab.inc" %>

<%@ include file="../include/footer.inc" %> 