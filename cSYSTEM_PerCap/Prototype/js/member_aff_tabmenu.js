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
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MemberAffiliateInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="MemberAffiliateInformationEdit.htm" title="Edit Member Affiliate Information" class="TabFONT">Edit</A></TD>');
} else if (screen == "EmployerInformation") {
	document.write('									<TD align="center" class="TabTD"><A href="EmployerInformationEdit.htm" title="Edit Employer Information" class="TabFONT">Edit</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "MemberAffiliateInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Member Affiliate</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="MemberAffiliateInformation.htm" title="View Member Affiliate Information" class="TabFONT">Member Affiliate</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "EmployerInformation") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Employer</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="EmployerInformation.htm" title="View Employer Information" class="TabFONT">Employer</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="#" title="Generate Membership Card" class="TabFONT" onClick="alert(\'The member has been added to weekly run for generating Membership Cards.\');">Generate Mbr Card</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="MemberDetail.htm" title="Member Detail" class="TabFONT">Member Detail</A></TD>');
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
	document.write('						<TD valign="bottom" height="3" colspan="12">');
	document.write('						</TD>');
	document.write('					</TR>');
}
document.write('				</TABLE>');
document.write('			</TD>');
document.write('		</TR>');
document.write('	</TABLE>');