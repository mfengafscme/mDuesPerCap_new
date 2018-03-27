<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%@ page import="org.afscme.enterprise.controller.web.*" %>
<%! String title = "Person Verify Results", help = "PersonVerifyResults.html";%>
<%@ include file="../include/header.inc" %>

<table width="100%" align="center">
    <tr>
        <td align="left">
            <afscme:searchNav formName="verifyStaffForm" action="/verifyStaff.action"/></td>
        <td align="right">
            <afscme:button page="/editAffiliateStaff.action?newPerson=true">Add New</afscme:button>
    </tr>
</table>

<BR>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="BodyContent" align="center">
    <tr align="center">
        <th width="8%">Select</th>
        <th colspan="5">Affiliate Identifier</th>
        <th><afscme:sortLink styleClass="TH" action="/verifyStaff.action" formName="verifyStaffForm" field="name" title="Sort By Name">Name</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/verifyStaff.action" formName="verifyStaffForm" field="addr" title="Sort By Address">Address</afscme:sortLink></th>
        <th><afscme:sortLink styleClass="TH" action="/verifyStaff.action" formName="verifyStaffForm" field="city" title="Sort By City">City</afscme:sortLink></th>
        <th width="3%"><afscme:sortLink styleClass="TH" action="/verifyStaff.action" formName="verifyStaffForm" field="addrState" title="Sort By State">State</afscme:sortLink></th>
        <th width="8%"><afscme:sortLink styleClass="TH" action="/verifyStaff.action" formName="verifyStaffForm" field="zip" title="Sort By Zip/Postal Code">Zip/Postal Code</afscme:sortLink></th>
        <th width="8%"><afscme:sortLink styleClass="TH" action="/verifyStaff.action" formName="verifyStaffForm" field="ssn" title="Sort By SSN">SSN</afscme:sortLink></th>
    </tr>
		<tr>
			<td>&nbsp;</td>
            <TH width="2%">
                <afscme:sortLink styleClass="smallTH" action="/verifyStaff.action" formName="verifyStaffForm" field="type">Type</afscme:sortLink>
            </TH>
            <TH width="8%">
                <afscme:sortLink styleClass="smallTH" action="/verifyStaff.action" formName="verifyStaffForm" field="local">Local/Sub Chapter</afscme:sortLink>
            </TH>
            <TH width="6%">
                <afscme:sortLink styleClass="smallTH" action="/verifyStaff.action" formName="verifyStaffForm" field="state">State/National Type</afscme:sortLink>
            </TH>
            <TH width="5%">
                <afscme:sortLink styleClass="smallTH" action="/verifyStaff.action" formName="verifyStaffForm" field="subUnit">Sub Unit</afscme:sortLink>
            </TH>
            <TH width="7%">
                <afscme:sortLink styleClass="smallTH" action="/verifyStaff.action" formName="verifyStaffForm" field="council">Council/Retiree Chapter</afscme:sortLink>
            </TH>
			<td colspan="6">&nbsp;</td>
		</tr>
<logic:iterate id="element" name="verifyStaffForm" property="results" type="org.afscme.enterprise.person.PersonResult">
    <tr>
        <td align="center" nowrap>
            <logic:notEqual name="element" property="affPk" value="<%=AFSCMEAction.getCurrentAffiliatePk(request).toString()%>">
                <afscme:link page="/editAffiliateStaff.action" paramId="personPk" paramName="element" paramProperty="personPk" title="Add Staff" styleClass="action">Add</afscme:link>
            </logic:notEqual>
            <logic:notEmpty name="element" property="affPk">
                    <%String viewStaffAction="/viewAffiliateStaff.action?back=/viewStaffMaintainence.action?affPk="+element.getAffPk();%>
                &nbsp;<afscme:link page="<%=viewStaffAction%>" paramId="personPk" paramName="element" paramProperty="personPk" title="View Staff" styleClass="action">View</afscme:link>
            </logic:notEmpty>
        </td>
        <logic:empty name="element" property="affPk">
            <td colspan="5">&nbsp;</td>
        </logic:empty>
        <logic:notEmpty name="element" property="affPk">
            <afscme:affiliateIdWrite
                    name="element"
                    affIdTypeProperty="affType"
                    affIdSubUnitProperty="affSubUnit"
                    affIdStateProperty="affStateNatType"
                    affIdCouncilProperty="affCouncilRetireeChap"
                    affIdAdminCouncilProperty="affAdminCouncil"
                    affIdLocalProperty="affLocalSubChapter"/>
        </logic:notEmpty>
        <td align="center"><bean:write name="element" property="personNm"/></td>
        <td align="center"><bean:write name="element" property="personAddr"/></td>
        <td align="center"><bean:write name="element" property="city"/></td>
        <td align="center"><afscme:codeWrite name="element" property="state" codeType="State" useCode="true" format="{0}"/></td>
        <td align="center"><bean:write name="element" property="personAddrPostalCode"/></td>
        <td align="center"><bean:write name="element" property="ssn"/></td>
    </tr>
    </logic:iterate>
</table>
<BR>
<table width="100%" align="center">
    <tr>
        <td align="left">
           <afscme:searchNav formName="verifyStaffForm" action="/verifyStaff.action"/></td>
        <td align="right">
            <afscme:button page="/editAffiliateStaff.action?newPerson=true">Add New</afscme:button>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>