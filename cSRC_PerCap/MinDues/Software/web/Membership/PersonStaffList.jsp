<%! String title = "Person Staff List", help = "PersonStaffList.html";%>
<%@ include file="../include/header.inc" %>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="left">
            <BR>
            <afscme:button action="/viewPersonDetail.action">Return</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<tr valign="top">
			<td class="ContentHeaderTR">
				<afscme:currentPersonName/><br> 
			</td>
		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<tr>
			<th>
				Select 
			</th>

			<th colspan="5">
                Affiliate Identifier
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewPersonStaff.action" formName="personStaffForm" field="title">Affiliate Staff Title</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewPersonStaff.action" formName="personStaffForm" field="pocFor">POC For</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="TH" action="/viewPersonStaff.action" formName="personStaffForm" field="location">Location</afscme:sortLink>
			</th>
		</tr>
		<tr>
			<td>

				&nbsp;
			</td>
			<th>
                <afscme:sortLink styleClass="smallTH" action="/viewPersonStaff.action" formName="personStaffForm" field="type">Type</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="smallTH" action="/viewPersonStaff.action" formName="personStaffForm" field="local">Loc/Sub Chap</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="smallTH" action="/viewPersonStaff.action" formName="personStaffForm" field="state">State/Nat'l</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="smallTH" action="/viewPersonStaff.action" formName="personStaffForm" field="subUnit">Sub Unit</afscme:sortLink>
			</th>
			<th>
                <afscme:sortLink styleClass="smallTH" action="/viewPersonStaff.action" formName="personStaffForm" field="council">CN/Ret Chap</afscme:sortLink>
			</th>
			<td COLSPAN="3">

				&nbsp;
			</td>
		</tr>
        <logic:iterate name="personStaffForm" property="results" type="org.afscme.enterprise.affiliate.staff.StaffResult" id="staff">

            <tr>
                <td align="center">
                    <afscme:link page="/viewAffiliateStaff.action" paramName="staff" paramProperty="affPk" paramId="affPk" styleClass="action" title="View Affiliate Staff Detail">View</afscme:link> 
                </td>

                <afscme:affiliateIdWrite name="staff" property="affiliateIdentifier" />

                <td align="center">
                    <bean:write name="staff" property="title"/>
                </td>
                <td align="center">
                    <bean:write name="staff" property="pocFor"/>
                </td>
                <td align="center">
                    <bean:write name="staff" property="locationName"/>
                </td>
            </tr>

    </logic:iterate>

	</table>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="left">
            <BR>
            <afscme:button action="/viewPersonDetail.action">Return</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

    <br><br>


<%@ include file="../include/footer.inc" %> 
