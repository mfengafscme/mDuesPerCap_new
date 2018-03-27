<%! String title = "Member Detail - Add (Affiliation)", help = "MemberDetailAddAffiliation.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="memberDetailAddForm" id="memberDetailAddForm" type="org.afscme.enterprise.member.web.MemberDetailAddForm"/>

<html:form action="saveMemberDetailAddAffiliation.action" focus="theAffiliateIdentifier.type">
    <html:hidden property="ssn"/>
    <html:hidden  property="affPk"/>
    <input type="hidden" name="another"/>
 <%--   <html:hidden property="personPk"/> --%> 
 
<!-- Display global errors -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
        <TR></TR>
    </TABLE>

    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
<!-- Display Person Name Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="14%">Prefix</TH>
                        <TH width="24%">* First Name</TH>
                        <TH width="24%">Middle Name</TH>
                        <TH width="24%">* Last Name</TH>
                        <TH width="14%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <afscme:codeWrite name="memberDetailAddForm" property="prefixNm" codeType="Prefix" format="{1}" />
                        </TD>
                        <TD align="center" class="ContentTD">
                            <bean:write name="memberDetailAddForm" property="firstNm" />
                        </TD>
                        <TD align="center" class="ContentTD">
                            <bean:write name="memberDetailAddForm" property="middleNm" />
                        </TD>
                        <TD align="center" class="ContentTD">
                            <bean:write name="memberDetailAddForm" property="lastNm" />
                        </TD>
                        <TD align="center" class="ContentTD">
                            <afscme:codeWrite name="memberDetailAddForm" property="suffixNm" codeType="Suffix" format="{1}" />
                         </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
<!-- Display Person Detail Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="12%" class="ContentHeaderTD">Nickname</TD>
                        <TD width="25%" class="ContentTD"><bean:write name="memberDetailAddForm" property="nickNm" /></TD>
                        <TD width="10%" class="ContentHeaderTD">SSN</TD>
                        <TD width="20%" class="ContentTD">
                            <bean:write name="memberDetailAddForm" property="ssn1" /> - <bean:write name="memberDetailAddForm" property="ssn2" /> - <bean:write name="memberDetailAddForm" property="ssn3" />
                        </TD>
                        <TD width="15%" class="ContentHeaderTD">Valid SSN</TD>
                        <TD width="18%"><html:checkbox name="memberDetailAddForm" property="ssnValid" disabled="true"/></TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="label_AlternateMailingName">Alt Mail Name</LABEL></TD>
                        <TD class="ContentTD" colspan="7"><bean:write name="memberDetailAddForm" property="altMailingNm" /></TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
<!-- Display Person System Mailing Address Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <!-- if no System Mailing Address then show message -->
                                     <logic:notPresent name="memberDetailAddForm" property="personAddressRecord">
                                          <TR>
                                                <TH align="left" colspan="6">No System Mailing Address</TH>
                                          </TR>
                                     </logic:notPresent>
                                        
                                        <!-- check for a System Mailing Address -->            
                                     <logic:present name="memberDetailAddForm" property="personAddressRecord">    
                                     <bean:define id="address" name="memberDetailAddForm" property ="personAddressRecord" type="org.afscme.enterprise.address.PersonAddressRecord"/>
   
                                          <TR>
						            <TH align="left" colspan="6">System Mailing Address</TH>
					            </TR>
					            <TR>
						            <TD class="ContentHeaderTD">Address 1</TD>
						            <TD class="ContentTD"><bean:write name="address" property="addr1"/></TD>
						            <TD class="ContentHeaderTD">Address 2</TD>
						            <TD class="ContentTD" colspan="3"><bean:write name="address" property="addr2"/></TD>
					            </TR>
					            <TR>
						            <TD width="11%" class="ContentHeaderTD">City</TD>
						            <TD width="29%"><bean:write name="address" property="city"/></TD>
						            <TD width="15%" class="ContentHeaderTD">State</TD>
						            <TD width="25%"><afscme:codeWrite name="address" property="state" useCode="true" codeType="State" format="{1}"/></TD>
						            <TD width="10%" class="ContentHeaderTD">Zip/Postal Code</TD>
						            <TD width="15%">
							          <logic:notEqual name="address" property="zipCode" value="0">
                                                       <bean:write name="address" property="zipCode"/>
                                                    </logic:notEqual>
                                                    <logic:notEqual name="address" property="zipPlus" value="0">
                                                       -<bean:write name="address" property="zipPlus"/>
                                                    </logic:notEqual>
						            </TD>
					            </TR>
					            <TR>
						            <TD class="ContentHeaderTD">County</TD>
						            <TD class="ContentTD"><bean:write name="address" property="county"/></TD>
						            <TD class="ContentHeaderTD">Province</TD>
                                                <TD class="ContentTD"><bean:write name="address" property="province"/></TD>
						            <TD class="ContentHeaderTD">Country</TD>
                                                <TD class="ContentTD"><afscme:codeWrite name="address" property="countryPk" codeType="Country" format="{1}"/></TD>
					            </TR>
					            <TR>
						            <TD class="ContentHeaderTD">Bad Address</TD>
                                                <TD class="ContentTD"><html:checkbox property="bad" name="address" disabled="true"/></TD>
                                                <TD class="ContentHeaderTD">Date Marked Bad</TD>
                                                <TD class="ContentTD" colspan="3"><afscme:dateWrite name="address" property="badDate"/></TD>
					            </TR>
					
                                    </logic:present>    

                 </TABLE>
            </TD>
        </TR>
<!-- Display affiliate and member fields -->
        <TR align="center">
            <TD colspan="2" class="ContentHeaderTR">
		<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
		     <TR>
			      <TH colspan="5" align="left">* Affiliate Identifier - <afscme:affiliateFinder formName="memberDetailAddForm" affIdTypeParam="theAffiliateIdentifier.type" affIdCouncilParam="theAffiliateIdentifier.council" affIdLocalParam="theAffiliateIdentifier.local" affIdStateParam="theAffiliateIdentifier.state" affIdSubUnitParam="theAffiliateIdentifier.subUnit" affIdCodeParam="theAffiliateIdentifier.code"/></TH>
                        <TH colspan="2" align="center">* Member</TH>
                        <TH colspan="2" align="center">* Join Date</TH>
                 </TR>
		     <TR>
			      <TH width="14%">Type</TH>
			      <TH width="14%">Local/Sub Chapter</TH>
			      <TH width="20%">State/National Type</TH>
			      <TH width="14%">Sub Unit</TH>
			      <TH width="29%">Council/Retiree Chapter</TH>
                        <TH>Type</TH>
                        <TH>Status</TH>
                        <TH>Month</TH>
                        <TH>Year</TH>
		    </TR>
		    <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="theAffiliateIdentifier.type" onchange="clearHiddenFields(this.form);">

                                <afscme:codeOptions useCode="true" codeType="AffiliateType" format="{0}" allowNull="true" nullDisplay="" excludeCodes="C"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD" >
                            <html:text property="theAffiliateIdentifier.local" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="theAffiliateIdentifier.state" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD"> 
                             <html:text property="theAffiliateIdentifier.subUnit" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                             <html:text property="theAffiliateIdentifier.council" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="mbrType">
                                <afscme:codeOptions codeType="MemberType" format="{1}" allowNull="false" nullDisplay=""/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="mbrStatus">
                                <afscme:codeOptions codeType="MemberStatus" format="{1}" allowNull="false" nullDisplay=""/>
                            </html:select>
                        </TD> 
                        <TD align="center" class="ContentTD">
                            <html:select property="monthJoined">
                                <afscme:monthOptions allowNull="false" nullDisplay=""/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="yearJoined" size="4" maxlength="4"/>
                        </TD>
                 </TR>
                 <TR>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="mbrType"/></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="yearJoined"/></TD>
                 </TR>
                </TABLE>
            <TD>
        </TR>    
<!-- Display Person Phone Numbers -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="0" cellspacing="0" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="35%" valign="top">
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TH colspan="4" align="left">Phone Numbers</TH>
                                </TR>
                                <TR>
                                    <TH align="left" width="20%" class="small">Type</TH>
                                    <TH align="left" width="25%" class="small">Country Code</TH>
                                    <TH align="left" width="20%" class="small">Area Code</TH>
                                    <TH align="left" width="35%" class="small">Number</TH>
                                </TR>
                                    <logic:present name="memberDetailAddForm" property="thePhoneData">
                                        <logic:iterate name="memberDetailAddForm" property="thePhoneData" id="phoneData" type="org.afscme.enterprise.common.PhoneData">
                                            <TR>
                                               <TD><afscme:codeWrite name="phoneData" property="phoneType" codeType="PhoneType" format="{1}"/></TD>
                                               <TD><bean:write name="phoneData" property="countryCode"/></TD>
                                               <TD><bean:write name="phoneData" property="areaCode"/></TD>
                                               <TD><bean:write name="phoneData" property="phoneNumber"/></TD>
                                            </TR>
                                       </logic:iterate>
                                   </logic:present>     
                                </TR>
                            </TABLE>
                        </TD>
<!-- Display Person Email Info -->
                        <TD width="65%" valign="top">
                            <TABLE cellpadding="2" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TH colspan="2" align="left">Email Addresses</TH>
                                </TR>
                                <TR>
                                    <TH align="left" width="20%" class="small">Type</TH>
                                    <TH align="left" width="20%" class="small">Email</TH>
                                </TR>
                                <TR>
                                    <logic:present name="memberDetailAddForm" property="theEmailData">
                                        <logic:iterate name="memberDetailAddForm" property="theEmailData" id="emailData" type="org.afscme.enterprise.person.EmailData">
                                            <TR>
                                              <TD  width="20%">
                                                  <afscme:codeWrite name="emailData" property="emailType" codeType="EmailType" format="{1}"/>
                                              </TD>
                                              <TD width="80%">
                                                  <bean:write name="emailData" property="personEmailAddr"/>
                                              </TD>
                                           </TR>
                                        </logic:iterate>
              		           </logic:present>	
                               </TR>
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>

<!-- Display Person Comment -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH align="left">Enter New Comments</TH>
                    </TR>
                    <TR>
                        <TD>
                            <html:textarea property="comment" onkeyup="validateComments(this);" cols="105" rows="3"></html:textarea>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
        </TR>
    </TABLE>

<!-- Display buttons -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
        <tr>
            <td align="left">
            <input type="button" onclick="submitAndAddAnother(this.form)" class="button" value="Submit & Add Another"/>
                <html:submit styleClass="button"/>
            </td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
            <afscme:button action="saveMemberDetailAddAffiliation.action?cancel=goBack" prefix="&nbsp;">Cancel</afscme:button>
           </td>
        </tr>      
        <tr>
            <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
        </tr>
    </TABLE>
</html:form>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
