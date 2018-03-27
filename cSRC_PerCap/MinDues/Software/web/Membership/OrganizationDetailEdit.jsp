<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Detail Edit", help = "OrganizationDetailEdit.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="organizationDetail" name="organizationDetailForm" type="org.afscme.enterprise.organization.web.OrganizationDetailForm"/>

<html:form action="saveOrganizationDetail" focus="orgName">

    <html:hidden property="orgPK"/>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right">
            <html:reset styleClass="button"/>
            <afscme:button forward="ViewOrganizationDetail" prefix="&nbsp;">Cancel</afscme:button>
        </td>
    </tr>
    <tr>
        <td colspan="3"><BR></td>
    </tr>         
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentOrganizationName /><BR><BR> 
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th WIDTH="50%">* Organization Name</th>
                    <th>* Organization Type</th>
                </tr>
                <tr>
                    <td ALIGN="center">
                        <html:text property="orgName" size="29" maxlength="29"/><html:errors property="orgName"/></td>
                    <td ALIGN="center">
                        <html:select property="orgType">
                            <afscme:codeOptions codeType="OrganizationType" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                        </html:select><html:errors property="orgType"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr VALIGN="top">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td width="20%" CLASS="ContentHeaderTD">Organization Website</TD>
                    <td CLASS="ContentTD"><html:text property="orgWebSite" size="100" maxlength="100"/>
                            <html:errors property="orgWebSite"/>
                    </td>
                </tr>
                <tr>
                    <td CLASS="ContentHeaderTD">Marked for Deletion</td>
                    <td CLASS="ContentTD"><html:checkbox name="organizationDetail" property="markedForDeletion"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <th align="left" class="large">Location Information</th>
    </tr>

<!-- Organization Primary Location -->
<logic:notEmpty name="organizationDetail" property="orgPrimaryLocation">
    <bean:define id="location" name="organizationDetail" property="orgPrimaryLocation" type="org.afscme.enterprise.organization.LocationData"/>

    <%@ include file="../include/location_primary_content.inc" %>

</logic:notEmpty>

<logic:empty name="organizationDetail" property="orgPrimaryLocation">

    <%@ include file="../include/location_noprimary_content.inc" %>

</logic:empty>

</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <BR><afscme:currentOrganizationName />
        </td>
    </tr>
</table>

<table  width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td colspan="3"><BR></td>
    </tr>   
    <tr>
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right">
            <html:reset styleClass="button"/>
            <afscme:button forward="ViewOrganizationDetail" prefix="&nbsp;">Cancel</afscme:button>
        </td>
    </tr>      
    <tr>
        <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>
