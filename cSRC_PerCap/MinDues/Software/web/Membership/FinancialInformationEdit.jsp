<%! String title = "Financial Information Edit", help = "FinancialInformationEdit.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<!-- Something for tabs. -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<!-- Start Main Content -->
<html:form action="saveFinancialInformation.action" focus="employerIDNumber">
    <html:hidden name="financialForm" property="affPk"/>

    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD width="55%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="45%" class="ContentHeaderTD">Employer Identification Number</TD>
                        <TD width="55%" class="ContentTD">
                            <html:text name="financialForm" property="employerIDNumber" size="9" maxlength="9"/>
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
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD>
                <BR>
                <html:submit styleClass="button"/>
                <BR> <BR> 
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewFinancialInformation.action">Cancel</afscme:button>
            </TD>
        </TR>
    </TABLE>
</html:form>
<!-- End Main Content -->
<%@ include file="../include/footer.inc" %> 
