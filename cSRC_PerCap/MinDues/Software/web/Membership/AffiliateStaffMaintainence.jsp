<%! String title = "Affiliate Staff Maintenance", help = "AffiliateStaffMaintenance.html";%>
<%@ include file="../include/header.inc" %>

<!-- Something for tabs. -->
    <% String imageType = "top"; %>
    <% String screen = "AffiliateStaff"; %>
    <% String left = null; %>
    <% String right = null; %>
    <%@ include file="../include/affiliate_tab.inc" %>

    <bean:define id="form" name="staffMaintainenceForm" type="org.afscme.enterprise.affiliate.staff.web.StaffMaintainenceForm"/>

    <br>

    <logic:notEqual name="staffMaintainenceForm" property="total" value="0">
        <afscme:searchNav formName="form" action="/viewStaffMaintainence.action"/>
    </logic:notEqual>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<tr valign="top">
			<td class="ContentHeaderTR">
				<afscme:currentAffiliate/><br> 
			</td>
		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<tr>
			<th>
				Select 
			</th>

			<th>
                <afscme:sortLink styleClass="TH" action="/viewStaffMaintainence.action" formName="form" field="name">Name</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewStaffMaintainence.action" formName="form" field="title">Affiliate Staff Title</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewStaffMaintainence.action" formName="form" field="pocFor">POC For</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewStaffMaintainence.action"formName="form" field="location">Location</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewStaffMaintainence.action" formName="form" field="phone">Phone Number</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewStaffMaintainence.action" formName="form" field="email">Email Address</afscme:sortLink>
			</th>
		</tr>
        <logic:iterate name="form" property="results" type="org.afscme.enterprise.affiliate.staff.StaffResult" id="staff">

		<tr>
			<td align="center">
				<afscme:link page="/viewAffiliateStaff.action" paramName="staff" paramProperty="personPk" paramId="personPk" styleClass="action" title="View Affiliate Staff Detail">View</afscme:link> 
				<afscme:link page="/removeAffiliateStaff.action" paramName="staff" paramProperty="personPk" paramId="personPk" styleClass="action" title="Remove from Affiliate Staff" confirm="Are you sure you want to remove this person from the Staff?">Remove</afscme:link> 
			</td>

			<td align="center">
                <bean:write name="staff" property="fullName"/>
			</td>
			<td align="center">
                <bean:write name="staff" property="title"/>
			</td>
			<td align="center">
                <bean:write name="staff" property="pocFor"/>
			</td>
			<td align="center">
                <bean:write name="staff" property="locationName"/>
			</td>
			<td align="center">
                <bean:write name="staff" property="phoneNumber"/>
			</td>
			<td align="center">
                <bean:write name="staff" property="email"/>
			</td>
		</tr>

    </logic:iterate>
				<TR>
					<TD align="center">
						<afscme:link page="/viewVerifyStaff.action" styleClass="action">Add</afscme:link> 
					</TD>
				</TR>

	</table>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<tr valign="top">
			<td class="ContentHeaderTR">
				<afscme:currentAffiliate/>
			</td>
		</tr>

	</table>

    <logic:notEqual name="staffMaintainenceForm" property="total" value="0">
        <afscme:searchNav formName="form" action="/viewStaffMaintainence.action"/>
    </logic:notEqual>

    <br><br>

<!-- Something for tabs. -->
    <% imageType = "bottom"; %>
    <%@ include file="/include/affiliate_tab.inc" %>


<%@ include file="../include/footer.inc" %> 
