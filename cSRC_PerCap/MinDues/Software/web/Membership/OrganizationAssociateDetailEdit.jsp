<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define id="form" name="organizationAssociateDetailForm" type="org.afscme.enterprise.organization.web.OrganizationAssociateDetailForm"/>
<bean:define id="organizationAssociateData" name="form" property="organizationAssociateData" type="org.afscme.enterprise.organization.OrganizationAssociateData"/>
<bean:define id="personData" name="organizationAssociateData" property="personData" type="org.afscme.enterprise.person.PersonData"/>

<%!String title, help;%>
<%title = "Organization Associate Detail " + (form.isUpdate() ? "Edit" : "Add");%>
<%help = "OrganizationAssociateDetail" + (form.isUpdate() ? "Edit" : "Add") + ".html";%>
<%@ include file="../include/header.inc" %>

<html:form action="saveOrganizationAssociate">

    <html:hidden property="update"/>
    <html:hidden property="newPerson"/>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td align="left"><html:submit styleClass="button"/></td>
        <td align="right">
            <html:reset styleClass="button"/>
            <html:cancel styleClass="button"/>
        </td>
    </tr>
    <tr>
        <td colspan="3"><BR></td>
    </tr>         
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
<% if (form.isUpdate()) { %>
            <afscme:currentPersonName /><BR>
<% } %>
            <afscme:currentOrganizationName /><BR><BR> 
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th width="14%">Prefix</th>
<%if (form.isNewPerson() || form.isUpdate()) { %>
                    <th width="24%">* First Name</th>
<% } else { %>
                    <th width="24%">First Name</th>
<% } %>
                    <th width="24%">Middle Name</th>
<%if (form.isNewPerson() || form.isUpdate()) { %>
                    <th width="24%">* Last Name</th>
<% } else { %>
                    <th width="24%">Last Name</th>
<% } %>

                    <th width="14%">Suffix</th>
                </tr>
                <tr>
<%if (form.isNewPerson() || form.isUpdate()) { %>
                    <td align="center">
                        <html:select property="personData.prefixNm">
                            <afscme:codeOptions codeType="Prefix" allowNull="true" format="{1}"/>
                        </html:select>
                    </td>
                    <td align="center">
                        <html:text property="personData.firstNm" size="25" maxlength="25"/><br>
                        <html:errors property="personData.firstNm"/>
                    </td>
                    <td align="center">
                        <html:text property="personData.middleNm" size="25" maxlength="25"/><br>
                        <html:errors property="personData.middleNm"/>
                    </td>
                    <td align="center">
                        <html:text property="personData.lastNm" size="25" maxlength="25"/><br>
                        <html:errors property="personData.lastNm"/> 
                    </td>
                    <td align="center">
                        <html:select property="personData.suffixNm">
                            <afscme:codeOptions codeType="Suffix" allowNull="true" format="{1}"/>
                        </html:select>
                    </td>
<% } else { %>
                    <td align="center">
                        <afscme:codeWrite codeType="Prefix" property="personData.prefixNm" format="{1}"/> 
                    </td>
                    <td align="center">
                        <bean:write name="personData" property="firstNm" format="{1}"/> 
                    </td>
                    <td align="center">
                        <bean:write name="personData" property="middleNm"/> 
                    </td>
                    <td align="center">
                        <bean:write name="personData" property="lastNm"/> 
                    </td>
                    <td align="center">
                        <afscme:codeWrite codeType="Suffix" property="personData.suffixNm" format="{1}"/> 
                    </td>
<% } %>
                </tr>
            </table>
        </td>
    </tr>
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
<%if (form.isNewPerson() || form.isUpdate()) { %>
                    <td class="ContentHeaderTD"><label for="personData.nickNm">Nickname</label></td>
                    <td width="19%" class="ContentTD">
                        <html:text property="personData.nickNm" size="25" maxlength="25"/><br>
                        <html:errors property="personData.nickNm"/>
                    </td>
                    <td width="5%" class="ContentHeaderTD"><label for="label_SSN">SSN</label> 
                    </td>
                    <td width="18%" class="ContentTD">
                        <html:text name="form" property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>
                      - <html:text name="form" property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/>
                      - <html:text name="form" property="ssn3" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
                        <html:errors property="personData.ssn"/>
                    </td>
                    <td width="8%" class="ContentHeaderTD"><label for="personData.ssnValid">Valid SSN</label></td>
                    <td width="10%">
                        <html:checkbox property="personData.ssnValid"/> 
                    </td>
<% } else { %>
                    <td class="ContentHeaderTD"><label for="personData.nickNm">Nickname</label></td>
                    <td width="19%" class="ContentTD">
                        <bean:write name="personData" property="nickNm"/>
                    </td>
                    <td width="5%" class="ContentHeaderTD"><label for="label_SSN">SSN</label> 
                    </td>
                    <td width="18%" class="ContentTD">
                        <bean:write name="form" property="ssn1" />
                      - <bean:write name="form" property="ssn2" />
                      - <bean:write name="form" property="ssn3" />
                    </td>
                    <td width="8%" class="ContentHeaderTD"><label for="personData.ssnValid">Valid SSN</label></td>
                    <td width="10%">
                        <html:checkbox property="personData.ssnValid" disabled="true" />
                    </td>
<% } %>
<% if (form.isUpdate()) { %>
                    <td width="10%" class="ContentHeaderTD">Duplicate SSN</td>
                    <td width="10%">
                        <html:checkbox property="personData.ssnDuplicate" disabled="true"/>
                    </td>
<% } %>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="personData.altMailingNm">Alt Mail Name</label></td>
                    <td class="ContentTD" colspan="7">
<%if (form.isNewPerson() || form.isUpdate()) { %>
			<html:text property="personData.altMailingNm" size="120" maxlength="130"/><br>
                        <html:errors property="personData.altMailingNm"/>
<% } else { %>
                        <bean:write name="personData" property="altMailingNm"/> 
<% } %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr valign="top">
                    <th WIDTH="50%">Organization Name</th>
                    <th>Organization Title</th>
                </tr>
                <tr>
                    <td class="ContentTD" align="center"><bean:write name="organizationAssociateData" property="orgName"/></td>
                    <td align="center">
                        <html:select property="organizationAssociateData.orgPositionTitle">
                            <afscme:codeOptions codeType="OrgPositionTitle" allowNull="true" format="{1}"/>
                        </html:select>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

<% if (form.isUpdate()) { %>
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="6" align="left" class="large">Location Information</th>
                </tr>

<!-- Organization Associate Location -->
<logic:notEmpty name="organizationAssociateData" property="locationData">
    <bean:define id="location" name="organizationAssociateData" property="locationData" type="org.afscme.enterprise.organization.LocationData"/>

        <%@ include file="../include/location_primary_content.inc" %>

</logic:notEmpty>
<logic:empty name="organizationAssociateData" property="locationData">

    <%@ include file="../include/location_noprimary_content.inc" %>

</logic:empty>

            </table>
        </td>
    </tr>
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="2" align="left">Email Addresses</th>
                </tr>
                <tr>
                    <th class="small" align="left" width="15%">Type</th>
                    <th class="small" align="left">Email Address</th>
                </tr>
            <logic:iterate id="emailData" name="personData" property="theEmailData" type="org.afscme.enterprise.person.EmailData">
                <tr>
                    <td width="15%">
                        <afscme:codeWrite name="emailData" property="emailType" codeType="EmailType" format="{1}" />
                    </td>
                    <td>
                        <bean:write name="emailData" property="personEmailAddr" />
                    </td>
                </tr>
            </logic:iterate>
            </table>
        </td>
    </tr>
<% } %> <%-- end (form.isUpdate()) --%>

    <tr>
        <td valign="top">
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th align="left">Enter New Comments</th>
                </tr>
                <tr>
                    <td>
                        <html:textarea property="comment" cols="115" rows="3" onkeyup="validateComments(this);"/><br>
                        <html:errors property="comment"/> 
                    </td>
                </tr>
            </table>
        </td>
    </tr>

<% if (form.isUpdate()) { %>
    <tr>
        <td>
            <table cellpadding="2" cellspacing="0" border="0" class="InnerContentTable">
                <tr>
                    <td class="ContentHeaderTD">Last Updated</td>
                    <td class="ContentTD">
                        <afscme:dateWrite name="organizationAssociateData" property="recordData.modifiedDate" />
                    </td>
                    <td class="ContentHeaderTD">Updated By</td>
                    <td class="ContentTD">
                        <afscme:userWrite name="organizationAssociateData" property="recordData.modifiedBy" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
<% } %> <%-- end (form.isUpdate()) --%>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR"><BR>
<% if (form.isUpdate()) { %>
            <afscme:currentPersonName /><BR>
<% } %>
            <afscme:currentOrganizationName />
        </td>
    </tr>
</table>

<table  width="100%" cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" align="center">
    <tr valign="top">
        <td><BR><html:submit styleClass="button"/></td>
        <td align="right"><BR>
            <html:reset styleClass="button"/>
            <html:cancel styleClass="button"/>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
    </tr>
</table>

</html:form>

<%@ include file="../include/footer.inc" %>
