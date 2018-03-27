<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Detail Add", help = "OrganizationDetailAdd.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="saveOrganizationDetail" focus="orgName">

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    
    <tr valign="top">
        <td>
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th width="34%">* Organization Name</th>
                    <th width="33%">* Organization Type</th>
                    <th width="33%">Organization Website</th>
                </tr>
                <tr>
                    <td nowrap align="center">
                        <html:text property="orgName" size="29" maxlength="29"/><html:errors property="orgName"/></td>
                    <td nowrap align="center">
                        <html:select property="orgType">
                            <afscme:codeOptions codeType="OrganizationType" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                        </html:select>
                        <html:errors property="orgType"/>
                    </td>
                    <td nowrap align="center"><html:text property="orgWebSite" size="25" maxlength="100"/>
                        <html:errors property="orgWebSite"/>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td colspan="3"><BR></td>
    </tr>     
    <tr>
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right">
            <html:reset styleClass="button"/>
            <afscme:button forward="MainMenu" prefix="&nbsp;">Cancel</afscme:button>
        </td>
    </tr>      
    <tr>
        <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>
