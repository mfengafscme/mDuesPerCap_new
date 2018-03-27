<%! String title = "Affiliate Hierarchy", help = "AffiliateHierarchy.html";%>
<%@ include file="../include/header.inc" %>

<!-- Start Main Content -->
<% String typeLastProcessed = ""; %>
<TABLE cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
    <logic:iterate name="affiliateHierarchy" id="entry" type="org.afscme.enterprise.affiliate.AffiliateHierarchyEntry">
        <bean:define name="entry" property="affiliateId" id="affiliateId" type="org.afscme.enterprise.affiliate.AffiliateIdentifier"/>
        <TR>
            <TD>
		<%
                String currentCouncilType = "false";
                if (affiliateId.getType().equals(new Character('C')) || affiliateId.getType().equals(new Character('R'))) {
                	currentCouncilType = "true";
                }
                if (currentCouncilType.equals("true")) {
                %>                
                    <TABLE cellpadding="1" cellspacing="1" border="0" class="ContentTable">
                    	<% if (!typeLastProcessed.equals("C")) { %>                        
                            <TR>
                                <TH class="large">Select</TH>
                                <TH align="left" class="large">Affiliate Name</TH>
                                <TH class="large">Type</TH>
                                <TH class="large">State/National Type</TH>
                                <TH class="large">Council/Retiree Chapter</TH>
                            </TR>
                        <% } %>
                        <TR>
                            <TD class="largeListFONT" width="6%" align="center">
                                <logic:equal name="entry" property="inPrimarySubHierarchy" value="true">
                                    <afscme:link page="/viewAffiliateDetail.action" paramId="affPk" paramName="entry" paramProperty="affPk" styleClass="actionLARGE">View</afscme:link>
                                </logic:equal>
                                <logic:equal name="entry" property="inPrimarySubHierarchy" value="false">
                                    &nbsp;
                                </logic:equal>
                            </TD>
                            <TD class="largeListFONT">
                                <bean:write name="entry" property="name"/>
                            </TD>
                            <afscme:affiliateIdWrite 
                                    name="affiliateId" 
                                    tdClass="largeListFONT"
                                    width="4%,20%,22%"
                                    includeSubs="false"
                            />
                        </TR>
                    </TABLE>
                <%   typeLastProcessed = "C";                
                }
                String currentLocalType = "false";
                if (affiliateId.getType().equals(new Character('L')) || affiliateId.getType().equals(new Character('S'))) {
                	currentLocalType = "true";
                }
                if (currentLocalType.equals("true")) {
                %>
                    <BLOCKQUOTE>
                        <TABLE cellpadding="1" cellspacing="1" border="0" class="ContentTableNoWidth" width="95%">
                     	    <% if (!typeLastProcessed.equals("L")) { %>
                                <TR>
                                    <TD colspan="6"><HR></TD>
                                </TR>
                                <TR>
                                    <TH>Select</TH>
                                    <TH align="left">Affiliate Name</TH>
                                    <TH>Type</TH>
                                    <TH>Local/Sub Chapter</TH>
                                    <TH>State/National Type</TH>
                                    <TH>Council/Retiree Chapter</TH>
                                </TR>
                            <% } %>
                            <TR>
                                <TD class="normalListFONT" width="5%" align="center">
                                    <logic:equal name="entry" property="inPrimarySubHierarchy" value="true">
                                        <afscme:link page="/viewAffiliateDetail.action" paramId="affPk" paramName="entry" paramProperty="affPk" styleClass="action">View</afscme:link>
                                    </logic:equal>
                                    <logic:equal name="entry" property="inPrimarySubHierarchy" value="false">
                                        &nbsp;
                                    </logic:equal>
                                </TD>
                                <TD class="normalListFONT">
                                    <bean:write name="entry" property="name"/>
                                </TD>
                                <afscme:affiliateIdWrite 
                                        name="affiliateId" 
                                        tdClass="normalListFONT"
                                        width="5%,15%,16%,19%"
                                        includeSubs="false"
                                />
                            </TR>
                        </TABLE>
                    </BLOCKQUOTE>
                <%   typeLastProcessed = "L";
                 } %>
                <logic:equal name="affiliateId" property="type" value="U">
                    <BLOCKQUOTE>
                        <BLOCKQUOTE>
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="ContentTableNoWidth" width="85%">
                    		<% if (!typeLastProcessed.equals("U")) { %>
                                    <TR>
                                        <TD colspan="7"><HR></TD>
                                    </TR>
                                    <TR>
                                        <TH class="small">Select</TH>
                                        <TH class="small" align="left">Affiliate Name</TH>
                                        <TH class="small">Type</TH>
                                        <TH class="small">Local/Sub Chapter</TH>
                                        <TH class="small">State/National Type</TH>
                                        <TH class="small">Sub Unit</TH>
                                        <TH class="small">Council/Retiree Chapter</TH>
                                    </TR>
                                <% } %>
                                <TR>
                                    <TD class="smallListFONT" width="4%" align="center">
                                        <logic:equal name="entry" property="inPrimarySubHierarchy" value="true">
                                            <afscme:link page="/viewAffiliateDetail.action" paramId="affPk" paramName="entry" paramProperty="affPk" styleClass="actionSMALL">View</afscme:link>
                                        </logic:equal>
                                        <logic:equal name="entry" property="inPrimarySubHierarchy" value="false">
                                            &nbsp;
                                        </logic:equal>
                                    </TD>
                                    <TD class="smallListFONT">
                                        <bean:write name="entry" property="name"/>
                                    </TD>
                                    <afscme:affiliateIdWrite 
                                            name="affiliateId" 
                                            tdClass="smallListFONT"
                                            width="3%,12%,12%,6%,15%"
                                            includeSubs="false"
                                    />
                                </TR>
                            </TABLE>
                        </BLOCKQUOTE>
                    </BLOCKQUOTE>
                <%   typeLastProcessed = "U"; %>
                </logic:equal>
            </TD>
        </TR>        
    </logic:iterate>
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>    
</TABLE>
<!-- End Main Content -->

<%@ include file="../include/footer.inc" %> 
