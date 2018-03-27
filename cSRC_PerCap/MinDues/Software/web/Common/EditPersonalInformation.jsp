<%--
This page is not implemented yet, it will be implemented as part of Membership.  It was
placed here to demostrade the privileges for this screen.
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Edit Personal Information", help = "EditPersonalInformation.html";%>
<%@ include file="../include/header.inc" %>

<FORM action="/viewPersonalInformation.action" name="form">
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentTD" colspan="3">
				<LABEL for="label_MbrNumber">Member Number</LABEL> &nbsp;&nbsp;&nbsp;10000004 <BR> <BR> 
			</TD>
		</TR>
		<TR valign="top">
			<TD colspan="3">
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
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
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
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
						<TH class="small">
							Incorrect 
						</TH>
						<TH width="18%" class="small">
							Type 
						</TH>
						<TH width="19%" class="small">
							Intl Code 
						</TH>
						<TH width="19%" class="small">
							Area Code 
						</TH>
						<TH width="24%" class="small">
							Number 
						</TH>
					</TR>
					<TR align="center">
						<TD class="ContentTD" align="center">
							<INPUT type="checkbox" name="HomePhoneBad"> 
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
							<INPUT type="checkbox" name="WorkPhoneBad"> 
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
							<INPUT type="checkbox" name="CellPhoneBad"> 
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
				</TABLE>
			</TD>
			<TD valign="top" width="50%">
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
					<TR>
						<TH colspan=2>
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
							<INPUT type="text" name="email" value="john.doe@work.com" id="label_email" size="50"> 
						</TD>
					</TR>
					<TR>
						<TD class="ContentTD">
							Alternate 
						</TD>
						<TD class="ContentTD">
							<INPUT type="text" name="email2" value="john.doe@home.com" id="email" size="50"> 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR valign="top">
			<TD width="100%" colspan="2">
				<TABLE cellpadding="1" cellspacing="1" class="InnerContentTable">
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
			<TD>
				<BR> <INPUT type="submit" class="BUTTON" value="Submit"> 
			</TD>
			<TD align="right">
				<BR> <INPUT type="reset" class="BUTTON" name="ResetButton" value="Reset"> <INPUT type="submit" class="BUTTON" value="Cancel"> 
			</TD>
		</TR>
	</TABLE>
	<!-- End Action Bar and footer -->
</FORM>


<%@ include file="../include/footer.inc" %>

