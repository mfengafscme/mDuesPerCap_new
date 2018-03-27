<%! String title = "GetBypassedAffiliate", help = "GetBypassedAffiliate.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="getBypassed" id="getBypassed" type="org.afscme.enterprise.cards.web.GetBypassedAffiliatesForm"/>
<bean:define name="getBypassed" id="affPk" property="affPk"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="GetBypassedAffiliates"/>
<%@ include file="../include/affiliate_tab.inc" %>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD colspan="2" class="ContentHeaderTR">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH colspan="6" align="left">
                        Affiliate Identifier 
                    </TH>
                </TR>
                <TR>
                    <TH width="17%">
                        Type 
                    </TH>
                    <TH width="17%">
                        Local/Sub Chapter 
                    </TH>
                    <TH width="24%">
                        State/National Type 
                    </TH>
                    <TH width="17%">
                        Sub Unit 
                    </TH>
                    <TH width="25%">
                        Council/Retiree Chapter 
                    </TH>
                </TR>
                <TR>
                    <afscme:affiliateIdWrite 
                            name="affiliateDetail" 
                            scope="request" 
                            affIdTypeProperty="affIdType"
                            affIdLocalProperty="affIdLocal"
                            affIdStateProperty="affIdState"
                            affIdSubUnitProperty="affIdSubUnit"
                            affIdCouncilProperty="affIdCouncil"
                            affIdAdminCouncilProperty="affIdAdminLegisCouncil"
                            tdClass="ContentTD"
                    />
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR valign="top">
        <TD width="50%">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="55%" class="ContentHeaderTD">
                        Abbreviated Affiliate Name 
                    </TD>
                    <TD width="45%" class="ContentTD">
                        <bean:write name="affiliateDetail" scope="request" property="affilName"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        Affiliate Status 
                    </TD>
                    <TD>
                        <afscme:codeWrite name="affiliateDetail" property="affilStatus" codeType="AffiliateStatus" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        AFSCME Legislative District 
                    </TD>
                    <TD>
                        <afscme:codeWrite name="affiliateDetail" property="afscmeLegislativeDistrict" codeType="LegislativeDistrict" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        AFSCME Region 
                    </TD>
                    <TD>
                        <afscme:codeWrite name="affiliateDetail" property="afscmeRegion" codeType="Region" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Multiple Employers</TD>
                    <TD>
                        <html:checkbox property="multipleEmployers" name="affiliateDetail" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Employer Sector</TD>
                    <TD>
                        <logic:present name="affiliateDetail" property="employerSectors">
                            <logic:iterate id="es" name="affiliateDetail" property="employerSectors">
                                <afscme:codeWrite name="es" codeType="EmployerSector" format="{1}"/>
                                <BR>
                            </logic:iterate>
                        </logic:present>
                        <logic:notPresent name="es">&nbsp;</logic:notPresent>
                    </TD>
                </TR>
            </TABLE>
        </TD>
        <TD width="50%" valign="top">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="50%" class="ContentHeaderTD">
                        <LABEL for="label_SubLocal">Sub-Locals Allowed</LABEL> 
                    </TD>
                    <TD width="50%">
                        <html:checkbox property="subLocalsAllowed" name="affiliateDetail" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        New Affiliate Identifier Source 
                    </TD>
                    <TD>
                        <logic:present name="affiliateDetail" property="newAffIdPk">
                            <bean:write name="affiliateDetail" scope="request" property="newAffIdType"/>
                            <bean:write name="affiliateDetail" scope="request" property="newAffIdLocal"/>
                            <bean:write name="affiliateDetail" scope="request" property="newAffIdState"/>
                            - <bean:write name="affiliateDetail" scope="request" property="newAffIdSubUnit"/>
                            - <bean:write name="affiliateDetail" scope="request" property="newAffIdCouncil"/>
                        </logic:present>
                        <logic:notPresent name="affiliateDetail" property="newAffIdPk">
                            &nbsp;
                        </logic:notPresent>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        <LABEL for="label_MultipleOffices">Multiple Offices</LABEL> 
                    </TD>
                    <TD>
                        <html:checkbox property="multipleOffices" name="affiliateDetail" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        <LABEL for="label_AnnualCardRunPerformed">Annual Card Run Performed</LABEL> 
                    </TD>
                    <TD>
                        <html:checkbox property="annualCardRunPerformed" name="affiliateDetail" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        Annual Card Run Type 
                    </TD>
                    <TD>
                        <afscme:codeWrite name="affiliateDetail" property="annualCardRunType" codeType="AffiliateCardRunType" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        <LABEL for="label_MemberRenewal">Member Renewal</LABEL> 
                    </TD>
                    <TD>
                        <afscme:codeWrite name="affiliateDetail" property="memberRenewal" codeType="MemberRenewal" format="{1}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        Affiliate Website 
                    </TD>
                    <TD>
                        <bean:write name="affiliateDetail" scope="request" property="website"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR>
        <TD valign="top" colspan="2">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH align="left" COLSPAN="2">
                        <afscme:link page="/viewLocationInformation.action" paramId="orgPK" paramName="affiliateDetail" paramProperty="affPk" styleClass="largeTH" title="Maintain Locations">Maintain Location Information</afscme:link>
                    </TH>
                </TR>
                <!-- Organization Primary Location -->
                <logic:notEmpty name="affiliateDetail" property="locationData">
                    <bean:define id="location" name="affiliateDetail" property="locationData" type="org.afscme.enterprise.organization.LocationData"/>
                    <%@ include file="../include/location_primary_content.inc" %>
                </logic:notEmpty>
                <logic:empty name="affiliateDetail" property="locationData">
                    <%@ include file="../include/location_noprimary_content.inc" %>
                </logic:empty>
            </TABLE>
        </TD>
    </TR>
    <TR align="center">
        <TD COLSPAN="2">
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH align="left">
                        <logic:present name="affiliateDetail" property="comment">
                            <afscme:link page="/viewAffiliateCommentHistory.action" paramId="orgPk" paramName="affiliateDetail" paramProperty="affPk" styleClass="TH">View More Comments</afscme:link>
                        </logic:present>
                        <logic:notPresent name="affiliateDetail" property="comment">
                            No Comments
                        </logic:notPresent>
                    </TH>
                </TR>
                <TR>
                    <TD>
                        <bean:write name="affiliateDetail" scope="request" property="comment"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
        </TR>
</TABLE>
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