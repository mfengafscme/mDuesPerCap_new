<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Organization Associate Detail", help = "OrganizationAssociateDetail.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="organizationAssociateDetailForm" type="org.afscme.enterprise.organization.web.OrganizationAssociateDetailForm"/>
<bean:define id="associate" name="form" property="organizationAssociateData" type="org.afscme.enterprise.organization.OrganizationAssociateData"/>
<bean:define id="personData" name="associate" property="personData" type="org.afscme.enterprise.person.PersonData"/>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td align="left">
            <afscme:button forward="ViewOrganizationAssociateList">Return</afscme:button>
        </td>
        <td align="right">
            <afscme:button page="/editOrganizationAssociateDetail.action?update=true">Edit</afscme:button><BR><BR>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentPersonName /><BR>
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
                    <th width="24%">First Name</th>
                    <th width="24%">Middle Name</th>
                    <th width="24%">Last Name</th>
                    <th width="14%">Suffix</th>
                </tr>
                <tr>
                    <td align="center">
                        <afscme:codeWrite name="personData" codeType="Prefix" property="prefixNm" format="{1}" />
                    </td>
                    <td align="center">
                        <bean:write name="personData" property="firstNm" />
                    </td>
                    <td align="center">
                        <bean:write name="personData" property="middleNm" />
                    </td>
                    <td align="center">
                        <bean:write name="personData" property="lastNm" />
                    </td>
                    <td align="center">
                        <afscme:codeWrite name="personData" codeType="Suffix" property="suffixNm" format="{1}" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td WIDTH="20%" class="ContentHeaderTD"><label for="label_nickNm">Nickname</label></td>
                    <td width="19%" class="ContentTD">
                        <bean:write name="personData" property="nickNm" />  
                    </td>
                    <td width="5%" class="ContentHeaderTD"><label for="label_ssn">SSN</label></td>
                    <td width="18%" class="ContentTD">
                        <bean:write name="form" property="ssn1" />
                      - <bean:write name="form" property="ssn2" />
                      - <bean:write name="form" property="ssn3" />
                    </td>
                    <td width="8%" class="ContentHeaderTD"><label for="label_ssnValid">Valid SSN</label></td>
                    <td width="10%">
                        <html:checkbox name="personData" property="ssnValid" disabled="true" />
                    </td>
                    <td width="10%" class="ContentHeaderTD"><label for="label_ssnDuplicate">Duplicate SSN</label></td>
                    <td width="10%">
                        <html:checkbox name="personData" property="ssnDuplicate" disabled="true" />
                    </td>
                </tr>
                <tr>
                    <td class="ContentHeaderTD"><label for="label_altMailingNm">Alt Mail Name</label></td>
                    <td class="ContentTD" colspan="7">
                        <bean:write name="personData" property="altMailingNm" />
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
                    <td class="ContentTD" align="center">
                        <bean:write name="associate" property="orgName" />
                    </td>
                    <td align="center">
                        <afscme:codeWrite name="associate" codeType="OrgPositionTitle" property="orgPositionTitle" format="{1}" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td valign="top" colspan="2">
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="6" align="left">
                        <afscme:link page="/viewLocationSelection.action?back=AssociateDetail" styleClass="largeTH" title="Maintain Location Association">Maintain Location Association</afscme:link>
                    </th>
                </tr>

<!-- Organization Associate Location -->
<logic:notEmpty name="associate" property="locationData">
    <bean:define id="location" name="associate" property="locationData" type="org.afscme.enterprise.organization.LocationData"/>

        <%@ include file="../include/location_primary_content.inc" %>

</logic:notEmpty>
<logic:empty name="associate" property="locationData">

    <%@ include file="../include/location_noprimary_content.inc" %>

</logic:empty>

            </table>
        </td>
    </tr>
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="2" align="left">
                        <afscme:link page="/viewEmailAddresses.action?back=AssociateDetail" paramId="personPk" paramName="personData" paramProperty="personPk" styleClass="TH">Maintain Email Addresses</afscme:link>
                    </th>
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
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
        <logic:present name="associate" property="latestCommentText">
                <tr>
                    <th align="left">
                        <afscme:link page="/viewOrganizationAssociateCommentHistory.action" styleClass="TH">View More Comments</afscme:link>
                    </TH>
                </tr>
                <tr>
                    <td>
<pre>
<bean:write name="associate" property="latestCommentText" />
</pre>
                    </td>
                </tr>
        </logic:present>
        <logic:notPresent name="associate" property="latestCommentText">
                <tr>
                    <th align="left" colspan="6">No Comments</th>
                </tr>
        </logic:notPresent>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td class="ContentHeaderTD">Last Updated</td>
                    <td class="ContentTD">
                        <afscme:dateWrite name="associate" property="recordData.modifiedDate" />
                    </td>
                    <td class="ContentHeaderTD">Updated By</td>
                    <td class="ContentTD">
                        <afscme:userWrite name="associate" property="recordData.modifiedBy" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR"><BR>
            <afscme:currentPersonName /><BR>
            <afscme:currentOrganizationName />
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td align="left">
            <BR><afscme:button forward="ViewOrganizationAssociateList">Return</afscme:button>
        </td>
        <td align="right">
            <BR><afscme:button page="/editOrganizationAssociateDetail.action?update=true">Edit</afscme:button><BR><BR>
        </td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
