<%! String title = "Financial Information", help = "FinancialInformation.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="financialForm" id="financialForm" type="org.afscme.enterprise.affiliate.web.FinancialForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="FinancialInformation"/>
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
    <TR valign="top">
        <TD width="55%">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="45%" class="ContentHeaderTD">Employer Identification Number</TD>
                    <TD width="55%" class="ContentTD">
                        <bean:write name="financialForm" property="employerIDNumber"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Per Capita Statistical Average</TD>
                    <TD class="ContentTD">
                        <bean:write name="financialForm" property="perCapitaStatAvg"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Per Capita Tax Payment Method</TD>
                    <TD class="ContentTD">
                        <bean:write name="financialForm" property="perCapitaTaxPaymentMethod"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
        <TD width="45%">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="70%" class="ContentHeaderTD">Per Capita Tax Last Paid Date</TD>
                    <TD width="30%" class="ContentTD">
                        <bean:write name="financialForm" property="perCapitaTaxLastPaidDate"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Per Capita Tax Last Member Count</TD>
                    <TD class="ContentTD">
                        <bean:write name="financialForm" property="perCapitaTaxLastMemberCount"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">Per Capita Tax Information Last Update Date</TD>
                    <TD class="ContentTD">
                        <bean:write name="financialForm" property="perCapitaTaxInfoLastUpdateDate"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR align="center">
        <TD COLSPAN="2">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH align="left">Comments</TH>
                </TR>
                <TR>
                    <TD>
                        <bean:write name="financialForm" property="comment"/>
                    </TD>
                </TR>
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


<%@ include file="../include/footer.inc" %> 