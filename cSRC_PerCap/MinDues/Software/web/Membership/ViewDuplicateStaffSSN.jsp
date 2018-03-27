<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Duplicate Social Security Notifier", help = "PersonVerifyResults.html";%>
<%@ include file="../include/header.inc" %>

<bean:define id="form" name="verifyStaffForm" type="org.afscme.enterprise.affiliate.staff.web.VerifyStaffForm"/>

	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<tr valign="top">
			<td class="ContentHeaderTR" colspan="11" align="center">
				<br>
				The following records have a matching Social Security Number to the one just entered. 
				<br>
				Press the 'Cancel' button, to stop the Add/Edit, or press the 'Continue' button to proceed. 
				<br>
				<br> 
			</td>

		</tr>
	</table>

<table width="100%" align="center">
    <tr>
        <td align="left">
            <afscme:button page="/showMain.action">Cancel</afscme:button>&nbsp;
        <td align="right">
            <afscme:button page = "/saveAffiliateStaff.action?ignoreSsnDup=true">Continue</afscme:button></td> 
    </tr>
</table>

<BR>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="BodyContentNoWidth" align="center">
		<tr valign="top">
			<td class="ContentHeaderTR" colspan="11">
				Social Security Number: <bean:write name="ssn"/><br>
				<br> 
			</td>
		</tr>
    
    <tr align="center">
        <th width="5%" align="center">Select</th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/saveAffiliateStaff.action" formName="form" field="name" title="Sort By Person Name">Name</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/saveAffiliateStaff.action" formName="form" field="addr" title="Sort By Address">Address</afscme:sortLink></th>
        <th align="center"><afscme:sortLink styleClass="TH" action="/saveAffiliateStaff.action" formName="form" field="city" title="Sort By City">City</afscme:sortLink></th>
        <th width="3%" align="center"><afscme:sortLink styleClass="TH" action="/saveAffiliateStaff.action" formName="form" field="state" title="Sort By State">State</afscme:sortLink></th>
        <th width="8%" align="center"><afscme:sortLink styleClass="TH" action="/saveAffiliateStaff.action" formName="form" field="zip" title="Sort By Zip/Postal Code">Zip/Postal Code</afscme:sortLink></th>
    </tr>
    <logic:iterate id="element" name="dups" type="org.afscme.enterprise.person.PersonResult">
    <tr>
        <td align="center"><afscme:link page="/viewPersonDetail.action" paramId="personPk" paramName="element" paramProperty="personPk" title="View Person Details" styleClass="action">View</afscme:link></td>
        <td align="center"><bean:write name="element" property="personNm"/></td>
        <td align="center"><bean:write name="element" property="personAddr"/></td>
        <td align="center"><bean:write name="element" property="city"/></td>
        <td align="center"><afscme:codeWrite name="element" property="state" codeType="State" useCode="true" format="{0}"/></td>
        <td align="center"><bean:write name="element" property="personAddrPostalCode"/></td>
    </tr>
    </logic:iterate>
</table>
<BR>
<table width="100%" align="center">
    <tr>
        <td align="left">
            <afscme:button page="/showMain.action">Cancel</afscme:button>&nbsp;
        <td align="right">
            <afscme:button page = "/saveAffiliateStaff.action?ignoreSsnDup=true">Continue</afscme:button></td> 
    </tr>
</table>

<%@ include file="../include/footer.inc" %>