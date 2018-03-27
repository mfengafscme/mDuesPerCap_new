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
if (screen == "Bypassed") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Bypassed</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AnnualMembershipCardRunBypassed.htm" title="View Bypassed Membership Cards" class="TabFONT">Bypassed</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "Scheduled") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Scheduled</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AnnualMembershipCardRunScheduled.htm" title="View Scheduled Membership Cards" class="TabFONT">Scheduled</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "Completed") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Completed</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AnnualMembershipCardRunCompleted.htm" title="View Completed Membership Cards" class="TabFONT">Completed</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
if (screen == "PerformCardRun") {
	document.write('									<TD align="center" class="TabTD"><FONT class="CurrentTabFONT">Perform Card Run</FONT></TD>');
} else {
	document.write('									<TD align="center" class="TabTD"><A href="AnnualMembershipCardRunPerformCardRun.htm" title="Generate Membership Cards" class="TabFONT">Perform Card Run</A></TD>');
}
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
document.write('						<TD>');
document.write('							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">');
document.write('								<TR>');
document.write('									<TD class="TabImageTD"><IMG src="' + left + '" class="TabImage"></TD>');
document.write('									<TD align="center" class="TabTD"><A href="AnnualMembershipCardRunCompleted.htm" title="Generate Membership Cards" class="TabFONT" onClick="return confirm(\'Are you sure you want to Initialize the system for a New Annual Card Run?\');">Initialize for New Annual Card Run</A></TD>');
document.write('									<TD class="TabImageTD" align="right"><IMG src="' + right + '" class="TabImage"></TD>');
document.write('								</TR>');
document.write('							</TABLE>');
document.write('						</TD>');
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