document.write('	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">');
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
	document.write('						<TD valign="bottom" height="3" colspan="20">');
	document.write('						</TD>');
	document.write('					</TR>');
}

document.write('					<TR>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
// build appropriate Edit tab based on which screen we're on.
if (screen == "AffiliateDetail") {
	document.write('									<TD align="center" class="TabTD"><A href="AffiliateDetailEdit.htm" title="Edit Affiliate Detail" class="TabFONT">Edit</A></TD>');
} else if (screen == "OfficerTitles") {
	document.write('									<TD align="center" class="TabTD"><A href="OfficerTitlesEdit.htm" title="Edit Officer Titles" class="TabFONT">Edit</A></TD>');
} else if (screen == "FinancialInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="FinancialInformationEdit.htm" title="Edit Financial Information" class="TabFONT">Edit</A></TD>');
} else if (screen == "MembershipReportingInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="MembershipReportingInformationEdit.htm" title="Edit Membership Reporting Information" class="TabFONT">Edit</A></TD>');
} else if (screen == "MaintainAffiliateOfficers") {
	//document.write('									<TD align="center" class="TabTD"><A href="SetOfficerFlags.htm" title="Set Officer Flags" class="TabFONT">Set Officer Flags</A></TD>');
	document.write('									<TD align="center" class="TabTD"><A href="AffiliateOfficerMaintenanceEdit.htm" title="Edit Affiliate Officers" class="TabFONT">Edit</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "AffiliateDetail") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Affiliate Detail</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AffiliateDetail.htm" title="Affiliate Detail" class="TabFONT">Affiliate Detail</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "OfficerTitles") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Office</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="OfficerTitles.htm" title="View Officer Titles" class="TabFONT">Officer Titles</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "FinancialInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Financial</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="FinancialInformation.htm" title="View Financial Information" class="TabFONT">Financial</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MembershipReportingInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Membership Reporting</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="MembershipReportingInformation.htm" title="View Membership Reporting Information" class="TabFONT">Membership Reporting</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MaintainAffiliateOfficers") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Maintain Officers</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AffiliateOfficerMaintenance.htm" title="Maintain Affiliate Officers" class="TabFONT">Maintain Officers</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="MailingListsInformation_ForAffiliate.htm" title="View Mailing Lists" class="TabFONT">Mailing Lists</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="AffiliateChangeHistory_SearchOnly.htm" title="View Affiliate Change History" class="TabFONT">Change History</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="OfficerHistoryForAffiliate.htm" title="View Officer History" class="TabFONT">Officer History</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><FONT class="TabFONT">Delegate History</FONT></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "AffiliateStaff") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Staff</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AffiliateStaffMaintenance.htm" title="Maintain Affiliate Staff Information" class="TabFONT">Staff</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="AffiliateSearchResults.htm" title="Return to Search Results" class="TabFONT">Search Results</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="BasicAffiliateSearch.htm" title="Perform a New Search" class="TabFONT">New Search</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('					</TR>');
if (imageType == "top") {
	document.write('					<TR bgcolor="#CCCC99">');
	document.write('						<TD valign="bottom" height="3" colspan="20">');
	document.write('						</TD>');
	document.write('					</TR>');
}
document.write('				</TABLE>');
document.write('			</TD>');
document.write('		</TR>');
document.write('	</TABLE>');