<%! String title = "Affiliate Staff Add Local Serviced", help = "AddLocalServiced.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD 
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>

<bean:define id="form" name="addLocalServicedForm" type="org.afscme.enterprise.affiliate.staff.web.AddLocalServicedForm"/>
<html:form action="addLocalServiced">
	<html:hidden property="affCode" value=""/>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="80%" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="5" align="left">Affiliate Identifier - <afscme:affiliateFinder formName="addLocalServicedForm" affIdTypeParam="affIdType" affIdCouncilParam="affIdCouncil" affIdLocalParam="affIdLocal" affIdStateParam="affIdState" affIdSubUnitParam="affIdSubUnit" affIdCodeParam="affCode"/></TH>
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
                            <html:select property="affIdType">
                                <afscme:codeOptions useCode="true" codeType="AffiliateType" format="{0}" excludeCodes="<%=form.getAffTypeExcludes()%>"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <logic:equal name="form" property="localLocked" value="true">
                                <bean:write name="addLocalServicedForm" property="affIdLocal"/>
                                <html:hidden name="addLocalServicedForm" property="affIdLocal"/>
                            </logic:equal>
                            <logic:equal name="form" property="localLocked" value="false">
                                <html:text property="affIdLocal" maxlength="4"  size="4"/>
                            </logic:equal>
                        </TD>
                        <TD align="center" class="ContentTD">
                        	<logic:equal name="form" property="stateLocked" value="true">
                            	<afscme:codeWrite name="addLocalServicedForm" property="affIdState" useCode="true" codeType="AffiliateState" format="{0}"/>
                            	<html:hidden name="addLocalServicedForm" property="affIdState"/>
                            </logic:equal>
                           	<logic:equal name="form" property="stateLocked" value="false">
	                            <html:select property="affIdState" name="addLocalServicedForm">
	                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
	                            </html:select>
	                        </logic:equal>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdSubUnit" maxlength="4" size="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <bean:write name="addLocalServicedForm" property="affIdCouncil"/>
                            <html:hidden name="addLocalServicedForm" property="affIdCouncil"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="5">
                            <HR>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <html:reset styleClass="button"/>&nbsp;
                <html:cancel styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<%@ include file="../include/footer.inc" %> 
