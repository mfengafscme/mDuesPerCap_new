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
	document.write('						<TD valign="bottom" height="3" colspan="12">');
	document.write('						</TD>');
	document.write('					</TR>');
}
document.write('					<TR>');
if (screen == "PersonDetail" || screen == "GeneralDemographicInformation" || screen == "PoliticalLegislativeInformation") {
	document.write('						<TD>');
	document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
	document.write('								<TR>');
	document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
	if (screen == "PersonDetail") {
		document.write('									<TD align="center" class="TabTD"><A href="PersonDetailEdit.htm" title="Edit Person Detail" class="TabFONT">Edit</A></TD>');
	} else if (screen == "GeneralDemographicInformation") {
		document.write('									<TD align="center" class="TabTD"><A href="GeneralDemographicInformationEdit_ForPerson.htm" title="Edit General Demographic Information" class="TabFONT">Edit</A></TD>');
	} else if (screen == "PoliticalLegislativeInformation") {
		document.write('									<TD align="center" class="TabTD"><A href="PoliticalLegislativeInformationEdit_ForPerson.htm" title="Edit Political/Legislative Information" class="TabFONT">Edit</A></TD>');
	}
	document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
	document.write('								</TR>');
	document.write('							</TABLE>');
	document.write('						</TD>');
}
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "PersonDetail") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Person Detail</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="PersonDetail.htm" title="Person Detail" class="TabFONT">Person Detail</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="../Administration/User.htm" title="Maintain User Information" class="TabFONT">User</A></TD>');
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
	document.write('									<TD align="center" class="TabTD"><A href="GeneralDemographicInformation_ForPerson.htm" title="View General Demographic Information" class="TabFONT">General Demographic</A></TD>');
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
	document.write('									<TD align="center" class="TabTD"><A href="PoliticalLegislativeInformation_ForPerson.htm" title="View Political/Legislative Information" class="TabFONT">Political/Legislative</A></TD>');
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
	document.write('									<TD align="center" class="TabTD"><A href="MailingListsInformation_ForPerson.htm" title="View Mailing Lists" class="TabFONT">Mailing Lists</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "PoliticalRebateList") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Political Rebate</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="PoliticalRebateSummaryByYear_ForPerson.htm" title="View Political Rebate List" class="TabFONT">Political Rebate</A></TD>');
}
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
	document.write('									<TD align="center" class="TabTD"><A href="CorrespondenceHistory_ForPerson.htm" title="View Correspondence History" class="TabFONT">Correspondence History</A></TD>');
}
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
document.write('									<TD align="center" class="TabTD"><A href="PersonSearchResults.htm" title="Return to Search Results" class="TabFONT">Search Results</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
/*document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="BasicPersonSearch.htm" title="Perform a New Search" class="TabFONT">New Search</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');*/
document.write('					</TR>');
if (imageType == "top") {
	document.write('					<TR bgcolor="#CCCC99">');
	document.write('						<TD valign="bottom" height="3" colspan="12">');
	document.write('						</TD>');
	document.write('					</TR>');
}
document.write('				</TABLE>');
document.write('			</TD>');
document.write('		</TR>');
document.write('	</TABLE>');