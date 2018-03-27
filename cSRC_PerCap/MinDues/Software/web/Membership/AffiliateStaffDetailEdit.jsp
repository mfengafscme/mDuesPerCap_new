<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<bean:define id="form" name="staffForm" type="org.afscme.enterprise.affiliate.staff.web.StaffForm"/>
<%! String title, help; %>
<% title = "Affiliate Staff Detail " + (form.isUpdate() ? "Edit" : "Add"); %>
<% help = "AffiliateStaffDetail" + (form.isUpdate() ? "Edit.html" : "Add.html"); %>
<%@ include file="../include/header.inc" %>

<html:form action="/saveAffiliateStaff.action" focus='<%=(form.isNewPerson() || form.isUpdate()) ? "personData.prefixNm" : "staffData.staffTitlePk" %>'>

    <html:hidden property="update"/>
    <html:hidden property="newPerson"/>

    <center><html:errors property="org.apache.struts.action.GLOBAL_ERROR"/></center>

	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<tr valign="top">
			<td align="left">
				<br> <html:submit styleClass="BUTTON"/> 
				<br> <br> 
			</td>
			<td align="right">
				<br> <html:reset styleClass="BUTTON"/> 
				<html:cancel styleClass="BUTTON"/> 
			</td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<tr valign="top">
			<td class="ContentHeaderTR">
				<afscme:currentAffiliate/> 
			</td>
		</tr>

	</table>
	<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<tr align="center">
			<td >
				<table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<tr>
						<th width="14%">
							Prefix 
						</th>

						<th width="24%">
							* First Name 
						</th>
						<th width="24%">
							Middle Name 
						</th>
						<th width="24%">
							* Last Name 
						</th>
						<th width="14%">

							Suffix 
						</th>
					</tr>
					<tr>
                        <%if (form.isNewPerson() || form.isUpdate()) { %>
						<td align="center">
                           		 	<html:select  property="personData.prefixNm">
			                                <afscme:codeOptions codeType="Prefix" allowNull="true" format="{1}"/>
                  		          	</html:select>
						</td>
						<td align="center">
							<html:text property="personData.firstNm"/><br>
			                        <html:errors property="personData.firstNm"/>
						</td>

						<td align="center">
							<html:text property="personData.middleNm"/><br>
                  			      <html:errors property="personData.middleNm"/>
						</td>
						<td align="center">
							<html:text property="personData.lastNm"/> <br>
			                        <html:errors property="personData.lastNm"/>
						</td>
						<td align="center">
                  		            <html:select property="personData.suffixNm">
                                			<afscme:codeOptions codeType="Suffix" allowNull="true"/>
			                        </html:select>
						</td>
                            <% } else { %>
						<td align="center">
							<afscme:codeWrite name="form" codeType="Prefix" property="personData.prefixNm" format="{1}"/> 
						</td>
						<td align="center">
							<bean:write name="form" property="personData.firstNm" format="{1}"/> 
						</td>

						<td align="center">
							<bean:write name="form" property="personData.middleNm"/> 
						</td>
						<td align="center">
							<bean:write name="form" property="personData.lastNm"/> 
						</td>
						<td align="center">
							<afscme:codeWrite name="form" codeType="Suffix" property="personData.suffixNm" format="{1}"/> 
						</td>
                            <% } %>
					</tr>

				</table>
			</td>
		</tr>
		<tr align="center">
			<td >
				<table width="100%" cellpadding="2" cellspacing="0" border="0" class="InnerContentTable">
					<tr>
						<td class="ContentHeaderTD">
							<label for="personData.nickNm"> Nickname</label> 
						</td>
						<td class="ContentTD">
                            <%if (form.isNewPerson() || form.isUpdate()) { %>
							<html:text property="personData.nickNm"/> 
                            <% } else { %>
							<bean:write name="form" property="personData.nickNm"/> 
                            <% } %>
						</td>
                            <%if (form.isUpdate()) { %>
			                  <td class="ContentHeaderTD">
                                          <label for="label_MemberNumber">Staff Number</label>
                                    </td>

                                    <td class="ContentTD" colspan="3">
							<bean:write name="form" property="personData.personPk"/><br>
                                    </td>
                            <% } %>
					</tr>
					<tr>
						<td class="ContentHeaderTD">
							<label for="personData.altMailingNm"> Alt Mail Name</label> 
						</td>
						<td class="ContentTD" colspan="5">
                            <%if (form.isNewPerson() || form.isUpdate()) { %>
							<html:text property="personData.altMailingNm"/> 
                            <% } else { %>
							<bean:write name="form" property="personData.altMailingNm"/> 
                            <% } %>
						</td>
					</tr>
					<tr>
						<td width="15%" class="ContentHeaderTD">
							<label for="personData.ssn"> SSN </label> <br>
                            			<html:errors property="personData.ssh"/>
						</td>
						<td width="25%" class="ContentTD">
                            <%if (form.isNewPerson() || form.isUpdate()) { %>
							<html:text property="personData.ssn"/> 
                            <% } else { %>
							<bean:write name="form" property="personData.ssn"/> <br>
                                          <html:errors property="personData.ssn"/>
                            <% } %>
						</td>

						<td width="20%" class="ContentHeaderTD">
							<label for="personData.ssnValid"> Valid SSN</label> 
						</td>
						<td width="10%">
                            <%if (form.isNewPerson() || form.isUpdate()) { %>
							<html:checkbox name="form" property="personData.ssnValid"/> 
                            <% } else { %>
							<html:checkbox property="personData.ssnValid" disabled="true"/> 
                            <% } %>
						</td>

                            <%if (form.isUpdate()) { %>
			                  <td width="20%" class="ContentHeaderTD">Duplicate SSN
						</td>		
			                  <td width="10%">
							<html:checkbox property="personData.ssnDuplicate" disabled="true"/> 
                    		      </td>
                            <% } %>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td>
				<table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<tr valign="top">
						<th width="50%">
							Staff Title 
						</th>
						<th width="50%">
							POC For 
						</th>
					</tr>
					<tr>
						<td class="ContentTD" align="center">
                                          <html:select name="staffForm" property="staffData.staffTitlePk">
                                              <afscme:codeOptions codeType="AffStaffTitle"  format="{1}"/>
                                          </html:select>
						</td>

						<td align="center">
                                          <html:select name="staffForm" property="staffData.pocForPk">
                                              <afscme:codeOptions codeType="POC"  format="{1}"/>
                                          </html:select>
						</td>
					</tr>

				</table>
			</td>
		</tr>

<% if (form.isUpdate()) { %>
    <logic:notPresent name="locals">
        <tr align="center">
            <td>
                <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <tr>
                        <th align="left" colspan="6">No Locals Serviced</th>
                    </tr>
                    </table>
            </td>
        </tr>
    </logic:notPresent>

    <logic:present name="locals">
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th align="left" colspan="6">Locals Serviced</th>
                </tr>

                <tr>
                    <th width="5%" class="small">Type</th>

                    <th width="10%" class="small">Loc/Sub Chap</th>

                    <th width="10%" class="small">State/Nat'l</th>

                    <th width="10%" class="small">Sub Unit</th>

                    <th width="10%" class="small">CN/Ret Chap</th>

                    <th width="50%" class="small">Affiliate Name</th>
                </tr>

                <logic:iterate name="locals" id="local" type="org.afscme.enterprise.affiliate.AffiliateResult">
                    <tr>
                        <afscme:affiliateIdWrite name="local" property="affiliateId" />

                        <TD align="center">
                            <bean:write name="local" property="affAbreviatedNm" />
                        </TD>
                    </tr>
                </logic:iterate>

            </table>
        </td>
    </tr>
    </logic:present> <%--locals--%>

    <tr valign="top">
        <td valign="top" colspan="2">
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="6" align="left" class="large">
                        Location Information
                    </th>
                </tr>
                <tr valign="top">
                    <td COLSPAN="6">
                        <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
<!-- check for any Organization Location Addresses -->
                            <logic:present name="location" property="orgAddressData">
<!-- Organization Location Addresses -->
                                <logic:iterate id="address" name="location" property="orgAddressData" type="org.afscme.enterprise.organization.OrgAddressRecord">
<!-- Primary Addresses -->
                                    <logic:equal name="address" property="type" value="<%= org.afscme.enterprise.codes.Codes.OrgAddressType.REGULAR.toString() %>">
                                        <tr>
                                            <th align="left" colspan="6">Primary Address</th>
                                        </tr>
                                        <tr>
                                            <td class="ContentTD" align="left" colspan="2">
                                                <label>Title of Location</label>
                                            </td>
                                            <td class="ContentTD" align="left" colspan="4">
                                                <bean:write name="location" property="locationNm" />
                                            </td>
                                        </tr>
<tr><td colspan="6"><hr></td></tr>
                                        <%@ include file="../include/location_address_content.inc" %>
                                    </logic:equal>
                                </logic:iterate>
                            </logic:present>

<!-- Organization Location Phones -->
                            <tr>
                                <th align="left" colspan="6">Phone Numbers</th>
                            </tr>

                            <tr>
                                <th width="15%" class="small">Type</th>

                                <th width="15%" class="small">Country Code</th>

                                <th width="15%" class="small">Area Code</th>

                                <th width="25%" class="small">Number</th>

                                <th width="10%" class="small">Bad Phone Flag</th>

                                <th width="20%" class="small">Date&nbsp;Marked&nbsp;Bad</th>
                            </tr>
                            <logic:present name="location" >
                            <logic:iterate id="phone" name="location" property="orgPhoneData" type="org.afscme.enterprise.organization.OrgPhoneData">
<!-- Organization Location Office Phone Number -->
                                <logic:equal name="phone" property="phoneType" value="<%= org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_OFFICE.toString() %>">
                                    <%@ include file="../include/location_phone_content.inc" %>
                                    <logic:equal name="location" property="hasOnlyOfficePhone" value="true">
                                        <tr>
                                            <td align="center">Fax</td>

                                            <td align="center">&nbsp;</td>

                                            <td align="center">&nbsp;</td>

                                            <td align="center">&nbsp;</td>

                                            <td align="center" class="ContentTD">
                                                <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                            </td>

                                            <td align="center" class="ContentTD">&nbsp;</td>
                                        </tr>
                                    </logic:equal>
                                </logic:equal>

<!-- Organization Location Fax Phone Number -->
                                <logic:equal name="phone" property="phoneType" value="<%= org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_FAX.toString() %>">
                                    <logic:equal name="location" property="hasOnlyFaxPhone" value="true">
                                        <tr>
                                            <td align="center">Office</td>

                                            <td align="center">&nbsp;</td>

                                            <td align="center">&nbsp;</td>

                                            <td align="center">&nbsp;</td>

                                            <td align="center" class="ContentTD">
                                                <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                            </td>

                                            <td align="center" class="ContentTD">&nbsp;</td>
                                        </tr>
                                    </logic:equal>

                                    <%@ include file="../include/location_phone_content.inc" %>
                                </logic:equal>
                            </logic:iterate>
                            </logic:present>
<!-- Organization Location Phones -->
<!-- if no Organization Location Phones then show empty ones -->
                            <logic:notPresent name="location" property="orgPhoneData">
                                <tr>
                                    <td align="center">Office</td>

                                    <td align="center">&nbsp;</td>

                                    <td align="center">&nbsp;</td>

                                    <td align="center">&nbsp;</td>

                                    <td align="center" class="ContentTD">
                                        <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                    </td>

                                    <td align="center" class="ContentTD">&nbsp;</td>
                                </tr>

                                <tr>
                                    <td align="center">Fax</td>

                                    <td align="center">&nbsp;</td>

                                    <td align="center">&nbsp;</td>

                                    <td align="center">&nbsp;</td>

                                    <td align="center" class="ContentTD">
                                        <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                    </td>

                                    <td align="center" class="ContentTD">&nbsp;</td>
                                </tr>
                            </logic:notPresent>
                               
                            <logic:notPresent name="location">
                                <%@ include file="../include/location_noprimary_content.inc" %>
                            </logic:notPresent>
<!-- Display Person Email Info -->
                            <table width="100%" cellpadding="0" cellspacing="0" border="1" class="InnerContentTable">
                                <TR>
                                    <TH colspan="2" align="left">
                                        Email Addresses
                                    </TH>
                                </TR>

                                <tr>
                                    <th class="small" align="left" width="15%">Type</th>

                                    <th class="small" align="left">Email Address</th>
                                </tr>
                                <logic:present name="emails" >
                                <TR>
                                    <td colspan="2">
                                        <table width="100%">

                                            <logic:iterate name="emails" id="emailData" type="org.afscme.enterprise.person.EmailData">
                                                <TR>
                                                    <TD width="15%">
                                                        <afscme:codeWrite name="emailData" property="emailType" codeType="EmailType" format="{1}" />
                                                    </TD>

                                                    <TD>
                                                    <bean:write name="emailData" property="personEmailAddr" />

                                                    &nbsp;</TD>
                                                </TR>
                                            </logic:iterate>
                                        </table>
                                    </td>
                                </TR>
                                </logic:present>
                            </table>
                        </table>
                    </td>
                </tr>
<% } %> <%-- end (form.isUpdate()) --%>
		
             <tr>
			<td  valign="top">
				<table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<tr>
						<th align="left">
							Enter New Comments
						</th>
					</tr>
					<tr>
						<td>
							<html:textarea property="comment" cols="115" rows="3" onkeyup="validateComments(this);"/><br>
                                          <html:errors property="comment"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
<% if (form.isUpdate()) { %>
		<TR>
			<TD>
				<table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTD">
							Last Updated 
						</TD>
						<TD class="ContentTD">
                                        <afscme:dateWrite name="staffForm" property="staffData.recordData.modifiedDate" />
						</TD>
						<TD class="ContentHeaderTD">
							Updated By 
						</TD>
						<TD class="ContentTD">
                                        <afscme:userWrite name="staffForm" property="staffData.recordData.modifiedBy" />
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
<% } %>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<tr valign="top">
			<td align="left">
				<br> <html:submit styleClass="BUTTON"/> 
				<br> <br> 
			</td>
			<td align="right">
				<br> <html:reset styleClass="BUTTON"/> 
				<html:cancel styleClass="BUTTON"/> 
			</td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<tr>
			<td align="center">

				<b><i>* Indicates Required Fields</i></b><br> 
			</td>
		</tr>
	</table>

</html:form>

<%@ include file="../include/footer.inc" %> 
