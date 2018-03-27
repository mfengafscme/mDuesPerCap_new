<%--
This page is not implemented yet, it will be implemented as part of Membership.  It was
placed here to demostrade the privileges for this screen.
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "View Personal Information", help = "ViewPersonalInformation.html";%>
<%@ include file="../include/header.inc" %>

<FORM action="#" name="form">
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentTD" colspan="3">
				<LABEL for="label_MbrNumber">Member Number</LABEL> &nbsp;&nbsp;&nbsp;10000004 <BR> <BR> 
			</TD>
		</TR>
		<TR valign="top">
			<TD colspan="3">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH>
							Prefix 
						</TH>
						<TH>
							First Name 
						</TH>
						<TH>
							Middle Name 
						</TH>
						<TH>
							Last Name 
						</TH>
						<TH>
							Suffix 
						</TH>
					</TR>
					<TR>
						<TD align="center" width="3%">
							Mr. 
						</TD>
						<TD align="center" width="10%">
							John 
						</TD>
						<TD align="center" width="5%">
							Michael 
						</TD>
						<TD align="center" width="15%">
							Doe 
						</TD>
						<TD align="center" width="3%">
							III 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR valign="top">
			<TD colspan="3">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="8" align="left">
							Primary Mailing Address 
						</TH>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_Address1"> Address 1 </LABEL> 
						</TD>
						<TD class="ContentTD">
							111 Some Street 
						</TD>
						<TD class="ContentHeaderTD">
							<LABEL for="label_Address2"> Address 2 </LABEL> 
						</TD>
						<TD class="ContentTD" colspan="3">
							Apt. 1 
						</TD>
					</TR>
					<TR>
						<TD width="11%" class="ContentHeaderTD">
							<LABEL for="label_City"> City</LABEL> 
						</TD>
						<TD width="30%">
							Vienna 
						</TD>
						<TD width="14%" class="ContentHeaderTD">
							<LABEL for="label_state"> State </LABEL> 
						</TD>
						<TD width="20%">
							VA 
						</TD>
						<TD width="10%" class="ContentHeaderTD">
							<LABEL for="label_ZipCode"> Zip Code </LABEL> 
						</TD>
						<TD width="15%">
							00000-0000 
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">
							<LABEL for="label_County"> County </LABEL> 
						</TD>
						<TD class="ContentTD">
							Fairfax 
						</TD>
						<TD class="ContentHeaderTD">
							<LABEL for="label_Province">Province</LABEL> 
						</TD>
						<TD class="ContentTD">&nbsp;
						</TD>
						<TD class="ContentHeaderTD">
							<LABEL for="label_Country"> Country </LABEL> 
						</TD>
						<TD class="ContentTD">
							US
						</TD>
					</TR>
					<TR>
						<TD COLSPAN="6">
							&nbsp;
						</TD>
					</TR>
					<TR>
						<TD>
							<A href="MyPersonalInformationCorrectAddress.htm" class="action" title="Correct Incorrect Address">Correct</A>
						</TD>
						<TD>
							Incorrect Address
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD width="50%">
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="5">
							Phone Numbers 
						</TH>
					</TR>
					<TR>
						<TH width="15%" class="small">
							Incorrect 
						</TH>
						<TH width="20%" class="small">
							Type 
						</TH>
						<TH width="20%" class="small">
							Intl Code 
						</TH>
						<TH width="20%" class="small">
							Area Code 
						</TH>
						<TH width="25%" class="small">
							Number 
						</TH>
					</TR>
					<TR align="center">
						<TD class="ContentTD" align="center">
							<INPUT type="checkbox" name="HomePhoneBad" disabled> 
						</TD>
						<TD class="ContentTD">
							Home 
						</TD>
						<TD class="ContentTD">&nbsp;
						</TD>
						<TD class="ContentTD">
							703 
						</TD>
						<TD class="ContentTD">
							555-1212 
						</TD>
					</TR>
					<TR align="center">
						<TD class="ContentTD" align="center">
							<INPUT type="checkbox" name="WorkPhoneBad" disabled> 
						</TD>
						<TD class="ContentTD">
							Work 
						</TD>
						<TD class="ContentTD">&nbsp;
						</TD>
						<TD class="ContentTD">
							202 
						</TD>
						<TD class="ContentTD">
							111-1111 
						</TD>
					</TR>
					<TR align="center">
						<TD class="ContentTD" align="center">
							<INPUT type="checkbox" name="CellPhoneBad" disabled> 
						</TD>
						<TD class="ContentTD">
							Cell 
						</TD>
						<TD class="ContentTD">&nbsp;
						</TD>
						<TD class="ContentTD">&nbsp;
						</TD>
						<TD class="ContentTD">&nbsp;
						</TD>
					</TR>
					<TR>
						<TD align="center" colspan="5">&nbsp;</TD>
					</TR>
					<TR>
						<TD align="center">
							<A href="MyPersonalInformationAddPhoneNumber.htm" class="action">Add</A> 
						</TD>
						<TD align="center" colspan="2">
							New Phone Number 
						</TD>
						<TD align="center" colspan="2">&nbsp;</TD>
					</TR>
				</TABLE>
			</TD>
			<TD valign="top" width="50%">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="2">
							Email Addresses 
						</TH>
					</TR>
					<TR>
						<TH align="left" class="small">
							Type 
						</TH>
						<TH align="left" class="small">
							Address 
						</TH>
					</TR>
					<TR>
						<TD class="ContentTD" width="15%">
							Primary 
						</TD>
						<TD class="ContentTD" width="85%">
							john.doe@work.com 
						</TD>
					</TR>
					<TR>
						<TD class="ContentTD">
							Alternate 
						</TD>
						<TD class="ContentTD">
							john.doe@home.com 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR valign="top">
			<TD width="100%" colspan="2">
				<TABLE width="100%" cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan="7">
							Affiliate Associations 
						</TH>
					</TR>
					<TR>
						<TH class="small">
							Name
						</TH>
						<TH WIDTH="5%" class="small">
							Type 
						</TH>
						<TH WIDTH="10%" class="small">
							Local/Sub Chapter 
						</TH>
						<TH WIDTH="8%" class="small">
							State/National 
						</TH>
						<TH WIDTH="5%" class="small">
							Sub Unit 
						</TH>
						<TH WIDTH="12%" class="small">
							Council/Retiree Chapter 
						</TH>
						<TH width="4%">&nbsp;
						</TH>
					</TR>
					<TR>
						<TD class="ContentTD" align="middle">
							New York Local 100
						</TD>
						<TD class="ContentTD" align="middle">
							L 
						</TD>
						<TD class="ContentTD" align="middle">
							100 
						</TD>
						<TD class="ContentTD" align="middle">
							NY 
						</TD>
						<TD class="ContentTD" align="middle">
							&nbsp;
						</TD>
						<TD class="ContentTD" align="middle">
							10
						</TD>
						<TD class="smallFONT">&nbsp;
						</TD>
					</TR>
					<TR>
						<TD class="ContentTD" align="middle">
							Virginia Local 30
						</TD>
						<TD class="ContentTD" align="middle">
							L
						</TD>
						<TD class="ContentTD" align="middle">
							30
						</TD>
						<TD class="ContentTD" align="middle">
							VA 
						</TD>
						<TD class="ContentTD" align="middle">
							&nbsp;
						</TD>
						<TD class="ContentTD" align="middle">
							100 
						</TD>
						<TD class="smallFONT">&nbsp;
						</TD>
					</TR>
					<TR>
						<TD class="ContentTD" align="middle">
							Local 500, PB Sub-Local
						</TD>
						<TD class="ContentTD" align="middle">
							U
						</TD>
						<TD class="ContentTD" align="middle">
							500
						</TD>
						<TD class="ContentTD" align="middle">
							NT 
						</TD>
						<TD class="ContentTD" align="middle">
							PB
						</TD>
						<TD class="ContentTD" align="middle">
							3
						</TD>
						<TD class="smallFONT" align="middle">
							(Past) 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR>
			<TD align="right">
				<BR> <afscme:button page="/editPersonalInformation.action">Update My Personal Information</afscme:button> 
				<BR> <BR> 
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR>
			<TD valign="TOP" align="center">
				<SCRIPT language="JavaScript" src="../js/footer.js">
         			</SCRIPT> 
			</TD>
		</TR>
	</TABLE>
	<!-- End Action Bar and footer -->
</FORM>

<%@ include file="../include/footer.inc" %>

