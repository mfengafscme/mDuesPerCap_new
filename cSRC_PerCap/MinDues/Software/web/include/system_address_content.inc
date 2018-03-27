		<TR valign="top">
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH colspan="2">
							Source
						</TH>
						<TH>
							Code
						</TH>
						<TH >
							Bad
						</TH>
						<TH >
							Date Marked Bad
						</TH>
					</TR>
					<TR>
						<TD class="ContentTD" align="center" colspan="2">
							<!--html:checkbox name="systemAddress" property="primary" disabled="true"/-->
							<% if (systemAddress.getSource() == 'U')
								out.print("Affiliate Update file");
								else if (systemAddress.getSource() == 'S')
								out.print("Affiliate Staff");								
								else if (systemAddress.getSource() == 'O')
								out.print("Owner");								
								else if (systemAddress.getSource() == 'A')
								out.print("AFSCME Staff");								
								else if (systemAddress.getSource() == 'N')
								out.print("NCOA");
							%>							
						</TD>
						<TD class="ContentTD" align="center">
							<afscme:codeWrite name="systemAddress" property="type" codeType="PersonAddressType"/>
						</TD>
						<TD class="ContentTD" align="center">
							<html:checkbox name="systemAddress" property="bad" disabled="true"/>
						</TD>
						<TD class="ContentTD" align="center">
							<afscme:dateWrite name="systemAddress" property="badDate"/>
						</TD>
					</TR>
					<bean:define id="address" name="systemAddress" type="org.afscme.enterprise.address.PersonAddressRecord"/>
                    <%@ include file="../include/address_content.inc" %>
				</TABLE>
			</TD>
		</TR>
