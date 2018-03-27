					<TR>
						<TD colspan="5">
							<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TD class="ContentHeaderTD">
										Address 1
									</TD>
									<TD class="ContentTD">
										<bean:write name="address" property="addr1"/>
									</TD>
									<TD class="ContentHeaderTD">
										Address 2
									</TD>
									<TD class="ContentTD" colspan="3">
										<bean:write name="address" property="addr2"/>
									</TD>
								</TR>
								<TR>
									<TD width="10%" class="ContentHeaderTD">
										City
									</TD>
									<TD width="40%" class="ContentTD">
										<bean:write name="address" property="city"/>
									</TD>
									<TD width="10%" class="ContentHeaderTD">
										State
									</TD>
									<TD width="20%" class="ContentTD">
										<afscme:codeWrite format="{0}" name="address" property="state" codeType="State" useCode="true"/>
									</TD>
									<TD width="10%" class="ContentHeaderTD">
										Zip/Postal Code 
									</TD>
									<TD width="10%" class="ContentTD">
                                        <logic:notEqual name="address" property="zipCode" value="0">
    										<bean:write name="address" property="zipCode"/>
                                        </logic:notEqual>
                                        <logic:notEqual name="address" property="zipPlus" value="0">
    										-<bean:write name="address" property="zipPlus"/>
                                        </logic:notEqual>
									</TD>
								</TR>
								<TR>
									<TD class="ContentHeaderTD">
										County 
									</TD>
									<TD class="ContentTD">
                                        <bean:write name="address" property="county"/>
									</TD>
									<TD class="ContentHeaderTD">
										Province
									</TD>
									<TD class="ContentTD">&nbsp;
                                        <bean:write name="address" property="province"/>
									</TD>
									<TD class="ContentHeaderTD">
                                        Country
									</TD>
									<TD class="ContentTD">
										<afscme:codeWrite name="address" property="countryPk" codeType="Country"/>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD colspan="5">
							<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
								<TR>
									<TD colspan="8">
										<HR>
									</TD>
								</TR>
								<TR>
									<TD width="15%" class="ContentHeaderTD">
										Date Created
									</TD>
									<TD width="11%" class="ContentTD">
										<afscme:dateWrite name="address" property="recordData.createdDate"/>
									</TD>
									<TD width="12%" class="ContentHeaderTD">
										<LABEL for="createdBy">Created By</LABEL>
									</TD>
									<TD width="9%" class="ContentTD">
										<afscme:userWrite name="address" property="recordData.createdBy"/>
									</TD>
									<TD width="14%" class="ContentHeaderTD">
										<LABEL for="recordData.modifiedDate">Last Updated</LABEL>
									</TD>
									<TD width="11%" class="ContentTD">
										<afscme:dateWrite name="address" property="recordData.modifiedDate"/>
									</TD>
									<TD width="13%" class="ContentHeaderTD">
										<LABEL for="recordData.modifiedBy">Updated By</LABEL>
									</TD>
									<TD width="15%" class="ContentTD">
										<afscme:userWrite name="address" property="recordData.modifiedBy"/>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
