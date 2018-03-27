		<TR valign="top">
			<TD width="8%">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTD">
                            <%if (addressMaintainence.isEditable(personAddress)) {
                                String linkPage="/editPersonAddress.action?back="+addressMaintainence.getBack(); %>
							<afscme:link page="<%=linkPage%>" paramName="personAddress" paramProperty="recordData.pk" paramId="addrPk" title="Edit this Address">Edit</afscme:link> 
                            <% } else { %>
                            &nbsp;
                            <% }  %>
						</TD>
						<TD class="ContentHeaderTD">
                            <%if (addressMaintainence.isDeletable(personAddress)) { 
                                String linkPage="/deletePersonAddress.action?back="+addressMaintainence.getBack(); %>
							<afscme:link page="<%=linkPage%>" paramName="personAddress" paramProperty="recordData.pk" paramId="addrPk" title="Delete this Address" confirm="Are you sure you want to delete this Address?">Delete</afscme:link> 
                            <% } else { %>
                            &nbsp;
                            <% }  %>
						</TD>
					</TR>
				</TABLE>
			</TD>
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH width="20%">
							Primary
						</TH>
						<TH width="20%">
							Code
						</TH>
						<TH width="20%">
							Private
						</TH>
						<TH width="20%">
							Bad
						</TH>
						<TH width="20%">
							Date Marked Bad
						</TH>
					</TR>
					<TR>
						<TD class="ContentTD" align="center">
							<html:checkbox name="personAddress" property="primary" disabled="true"/>
						</TD>
						<TD class="ContentTD" align="center">
							<afscme:codeWrite name="personAddress" property="type" codeType="PersonAddressType"/>
						</TD>
						<TD class="ContentTD" align="center">
							<html:checkbox name="personAddress" property="private" disabled="true"/>
						</TD>
						<TD class="ContentTD" align="center">
							<html:checkbox name="personAddress" property="bad" disabled="true"/>
						</TD>
						<TD class="ContentTD" align="center">
							<afscme:dateWrite name="personAddress" property="badDate"/>
						</TD>
					</TR>
					<bean:define id="address" name="personAddress" type="org.afscme.enterprise.address.PersonAddressRecord"/>
                    <%@ include file="../include/address_content.inc" %>
				</TABLE>
			</TD>
		</TR>
