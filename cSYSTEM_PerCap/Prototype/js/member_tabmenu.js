document.write('	<TABLE valign=TOP cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">');
document.write('		<TR bgcolor="#FCFFFF">');
document.write('			<TD>');
document.write('				<TABLE width="100%" cellpadding="0" cellspacing="1" border="0" class="InnerContentTable" align="center">');

var left;
var right;
if (imageType == "top") {
	left = "../images/edge-lft.bmp";
	right = "../images/edge-rgt.bmp";
} else {
	left = "../images/edge-btm-lft.bmp";
	right = "../images/edge-btm-rgt.bmp";
	document.write('					<TR bgcolor="#CCCC99">');
	document.write('						<TD valign="bottom" height="3" colspan="15">');
	document.write('						</TD>');
	document.write('					</TR>');
}

document.write('					<TR>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MemberDetail") {
	document.write('									<TD align="center" class="TabTD"><A href="MemberDetailEdit.htm" title="Edit Member Detail" class="TabFONT">Edit</A></TD>');
} else if (screen == "GeneralDemographicInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="GeneralDemographicInformationEdit.htm" title="Edit General Demographic Information" class="TabFONT">Edit</A></TD>');
} else if (screen == "PoliticalLegislativeInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="PoliticalLegislativeInformationEdit.htm" title="Edit Political/Legislative Information" class="TabFONT">Edit</A></TD>');
}/* else if (screen == "MailingListsInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="MailingListsInformationEdit.htm" title="Edit Mailing Lists Information" class="TabFONT">Edit</A></TD>');

} else if (screen == "ParticipationDetail") {
	document.write('									<TD align="center" class="TabTD"><A href="ParticipationDetailAdd.htm" title="Add Participation Detail" class="TabFONT">Add</A></TD>');
}*/
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
/*if (screen == "MemberDetail") {
	document.write('						<TD>');
	document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
	document.write('								<TR>');
	document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
	document.write('									<TD align="center" class="TabTD"><A href="MemberDetailAddFromPerson.htm" title="Add Member to New Affiliate" class="TabFONT">Add</A></TD>');
	document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
	document.write('								</TR>');
	document.write('							</TABLE>');
	document.write('						</TD>');
}*/
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MemberDetail") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Member Detail</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="MemberDetail.htm" title="Member Detail" class="TabFONT">Member Detail</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "GeneralDemographicInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">General Demographic</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="GeneralDemographicInformation.htm" title="View General Demographic Information" class="TabFONT">General Demographic</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "PoliticalLegislativeInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Political/Legislative</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="PoliticalLegislativeInformation.htm" title="View Political/Legislative Information" class="TabFONT">Political/Legislative</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MailingListsInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Mailing Lists</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="MailingListsInformation.htm" title="View Mailing Lists" class="TabFONT">Mailing Lists</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "ParticipationSummary") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Participation</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="ParticipationSummary.htm" title="View Participation Summary" class="TabFONT">Participation</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="PoliticalRebateSummaryByYear.htm" title="View Political Rebate List" class="TabFONT">Political Rebate</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "CorrespondenceHistory") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Correspondence History</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="CorrespondenceHistory.htm" title="View Correspondence History" class="TabFONT">Correspondence History</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
/*if (screen == "MaintainPendingReports") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Maintain Pending Report</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="../reporting/MaintainPendingReports.htm" title="Maintain Pending Report" class="TabFONT">Maintain Pending Report</A></TD>');
}*/

document.write('									<TD align="center" class="TabTD"><FONT class="TabFONT">Maintain Pending Reports</FONT></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
/*if (screen == "PACInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">PAC</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD">A href="#" title="View PAC Information" class="TabFONT">PAC</A></TD>');
}*/
document.write('									<TD align="center" class="TabTD"><FONT class="TabFONT">PAC</FONT></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
/*if (screen == "OrganizingDetails") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Organizing</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="OrganizingDetails.htm" title="View Organizing Details" class="TabFONT">Organizing</A></TD>');
}*/
document.write('									<TD align="center" class="TabTD"><FONT class="TabFONT">Organizing Details</FONT></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="MemberSearchResults.htm" title="Return to Search Results" class="TabFONT">Search Results</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
/*document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="BasicMemberSearch.htm" title="Perform a New Search" class="TabFONT">New Search</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');*/
document.write('					</TR>');
if (imageType == "top") {
	document.write('					<TR bgcolor="#CCCC99">');
	document.write('						<TD valign="bottom" height="3" colspan="15">');
	document.write('						</TD>');
	document.write('					</TR>');
}
document.write('				</TABLE>');
document.write('			</TD>');
document.write('		</TR>');
document.write('	</TABLE>');