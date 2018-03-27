<%! String title = "Power Member Search", help = "PowerMemberSearch.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>


<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD> 
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<html:form action="searchMembers.action">
    <input type="hidden" name="reset">
    <html:hidden name="searchMembersForm" property="affPk"/>
    <html:hidden name="searchMembersForm" property="affCode"/>
    <html:hidden name="searchMembersForm" value="power" property="input"/>
   

    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewBasicMemberCriteria.action">Basic Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
   <!-- Search Criteria Tables -->
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="100%" align="center">
        <TR>
            <TD class="ContentHeaderTR">
                Fields to Search: <BR>
                &nbsp; 
            </TD>
        </TR>
       <TR valign="top">
        <!-- Name Inner Table -->
            <TD colspan="2" class="ContentHeaderTR">
               <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="10%">Prefix</TH>
                        <TH width="27%">First Name</TH>
                        <TH width="26%">Middle Name</TH>
                        <TH width="27%">Last Name</TH>
                       <TH width="10%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD nowrap align="center"><html:select property="prefixNm">
                            <afscme:codeOptions codeType="Prefix" format="{1}" allowNull="true" nullDisplay=""/>
			 </html:select></TD>
                        <TD nowrap align="center"><html:text property="firstNm" size="25" maxlength="25"/><html:errors property="firstNm"/></TD>
			<TD nowrap align="center"><html:text property="middleNm" size="25" maxlength="25"/></TD>
                	<TD nowrap align="center"><html:text property="lastNm" size="25" maxlength="25"/><html:errors property="lastNm"/></TD>
			<TD align="center"><html:select property="suffixNm">
                            <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay=""/>
			 </html:select></TD>
                    </TR>
               </TABLE>
            </TD>
         </TR>   
         
        <TR valign="top">
        <!-- Affiliate ID Inner Table -->   
            <TD colspan="2" class="ContentHeaderTR">
		<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
		   <TR>
			<TH colspan="6" align="left">Affiliate Identifier - <afscme:affiliateFinder formName="searchMembersForm" affIdTypeParam="affType" affIdCouncilParam="affCouncilRetireeChap" affIdLocalParam="affLocalSubChapter" affIdStateParam="affStateNatType" affIdSubUnitParam="affSubUnit" affIdCodeParam="affCode"/></TH>
                  </TR>
		  <TR>
			<TH width="14%">Type</TH>
			<TH width="14%">Local/Sub Chapter</TH>
			<TH width="20%">State/National Type</TH>
			<TH width="14%">Sub Unit</TH>
			<TH width="29%">Council/Retiree Chapter</TH>
		 </TR>
		 <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="affType" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}" excludeCodes="C"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD" >
                            <html:text property="affLocalSubChapter" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/></TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affStateNatType" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select></TD>
                        <TD align="center" class="ContentTD"> 
                             <html:text property="affSubUnit" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/></TD>
                        <TD align="center" class="ContentTD">
                             <html:text property="affCouncilRetireeChap" size="4" maxlength="4" onchange="clearHiddenFields(this.form);" onkeyup="blur();focus();"/></TD>
                 </TR>
               </TABLE>
            </TD>
	 </TR>

         <TR valign="top">
          <!-- System Mailing Address -->    
            <TD colspan="2" class="ContentHeaderTR">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
			<TH nowrap colspan="8" width="100%" align="left">System Mailing Address</TH>
                    </TR>
		    <TR>
                        <TD nowrap width="15%" class="ContentHeaderTD" align="left"><LABEL for="addr1"> Address 1 </LABEL></TD>
		        <TD nowrap width="35%" class="ContentHeader" align="left" ><html:text property="addr1" size="50" maxlength="50"/></TD>
                        <TD nowrap width="15%" class="ContentHeaderTD" align="left"><LABEL for="addr2"> Address 2 </LABEL></TD>
		        <TD nowrap width="35%" class="ContentHeader" align="left" ><html:text property="addr2" size="50" maxlength="50"/></TD>
                    </TR>
                    <TR>
                        <TD width="10%" class="ContentHeaderTD"><LABEL for="city"> City </LABEL></TD>
                        <TD width="35" nowrap class="ContentHeader" align="left"><html:text property="city" size="25" maxlength="25"/>    
                        <TD width="10" class="ContentHeaderTD"><LABEL for="state"> State </LABEL></TD>
                        <TD width="15"><html:select property="state"><afscme:codeOptions codeType="State" format="{0}" allowNull="true" nullDisplay="" useCode="true"/>
                            </html:select>
                        </TD>
                        <TD width="10%" class="ContentHeaderTD"><LABEL for="zipCode"> Zip/Postal Code </LABEL></TD>
                        <TD width="20" nowrap class="ContentHeader" align="left"><html:text property="zipCode" size="12" maxlength="12"/>   
		    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="county"> County </LABEL></TD>
                        <TD nowrap class="ContentHeader" align="left"><html:text property="county" size="25" maxlength="25"/>   
                        <TD class="ContentHeaderTD"><LABEL for="province"> Province </LABEL></TD>
                        <TD nowrap class="ContentHeader" align="left"><html:text property="province" size="25" maxlength="25"/>   
                        <TD class="ContentHeaderTD"><LABEL for="country"> Country </LABEL></TD>
                        <TD><html:select property="country"><afscme:codeOptions codeType="Country" format="{0}" allowNull="true" nullDisplay=""/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><label for="addrUpdatedDt">Last Updated</label></td>
			<TD class="ContentTD"><html:text property="addrUpdatedDt" size="10" maxlength="10"/>
                            <a href="javascript:show_calendar('searchMembersForm.addrUpdatedDt');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;">
                            <IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></a> 
			</TD>
                        <TD class="ContentHeaderTD"><LABEL for="addrUpdatedBy"> Updated By </LABEL></TD>
                        <TD nowrap class="ContentHeader" align="left"><html:text property="addrUpdatedBy" size="10" maxlength="10"/></TD>   
                    </TR>
		</TABLE>
	   </TD>
	</TR>	 
        
        
        <!-- General Member Information -->    
        <TR>
             <TH nowrap colspan="2" align="left">General Member Information</TH>
        </TR>    
        <TR valign="top">
            <TD width="50%"> <!-- first column of member information-->
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    
		    <TR>
                       <TD nowrap  class="ContentHeaderTD" align="left"><LABEL for="nickNm"> Nick Name </LABEL></TD>
		       <TD nowrap  class="ContentHeader" align="left" ><html:text property="nickNm" size="25" maxlength="25"/></TD>
		    </TR>
                    <TR>
                       <TD nowrap  class="ContentHeaderTD" align="left"><LABEL for="nickNm"> Alt Mail Name </LABEL></TD>
		       <TD nowrap  class="ContentHeader" align="left" ><html:text property="nickNm" size="25" maxlength="25"/></TD>
		    </TR>
                    <TR>
                       <TD nowrap  class="ContentHeaderTD" align="left"><LABEL for="personPk"> Member Number </LABEL></TD>
		       <TD nowrap  class="ContentHeader" align="left" ><html:text property="personPk" size="10" maxlength="10"/></TD>
		    </TR>
                   
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="ssn1"> Social Security Number </LABEL></TD>
                        <TD nowrap class="ContentHeader" align="left"><html:text property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>
							- 
							<html:text property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/>
							- 
							<html:text property="ssn3" size="4" maxlength="4"/> 
			</TD>
                    </TR>
                    <TR>
                       <TD nowrap  class="ContentHeaderTD" align="left"> Valid SSN </TD>
		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="validSsn" value="1" styleId="validSsnYes">
                                <label for="ValidSsnYes">Valid</label></html:radio>
                            <html:radio property="validSsn" value="0" styleId="validSsnNo">
                                <label for="validSsnNo">Invalid</label></html:radio>
                            <html:radio property="validSsn" value="2" styleId="ssnAll">
                                <label for="ssnAll">All</label></html:radio>
                       </TD>
		    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="ssn1"> Phone Number </LABEL></TD>
                        <TD>
                            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <TR>
                                    <TD class="smallFont" width="30%">Country Code</TD>
                                    <TD class="smallFont" width="25%">Area Code</TD>
                                    <TD class="smallFont" width="45%">Number</TD>
				</TR>
                                <TR>    
                                    <TD nowrap width="23%" class="ContentHeader" align="left"><html:text property="countryCode" size="3" maxlength="3"/>
				    <TD nowrap width="23%" class="ContentHeader" align="left"><html:text property="areaCode" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>			
                                    <TD nowrap width="54%" class="ContentHeader" align="left"><html:text property="phoneNumber" size="12" maxlength="12"/>			
                                </TR>
                            </TABLE>
                       </TD>
                    </TR>
                    <TR>
                        <TD nowrap class="ContentHeaderTD" align="left"><LABEL for="personEmailAddr"> Email Address </LABEL></TD>
		        <TD nowrap class="ContentHeader" align="left" ><html:text property="personEmailAddr" size="25" maxlength="50"/></TD>
		    </TR>    
                    <TR>
			<TD class="ContentHeaderTD"><LABEL for="primaryInformationSource"> Primary Information Source </LABEL></TD>
			<TD>
                            <html:select property="primaryInformationSource"><afscme:codeOptions codeType="InformationSource" format="{1}" allowNull="true" nullDisplay=""/>
                            </html:select>
			</TD>
                    </TR>
		</TABLE>
	   </TD>
           <TD> <!-- second column -->
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="mbrType"> Member Type </LABEL></TD>
			<TD>
                            <html:select property="mbrType"><afscme:codeOptions codeType="MemberType" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
			</TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="mbrStatus"> Member Status </LABEL></TD>
			<TD>
                            <html:select property="mbrStatus"><afscme:codeOptions codeType="MemberStatus" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
			</TD>
                    </TR>    
                    <TR>
                        <TD class="ContentHeaderTD"><label for="mbrCardSentDt">Date Membership Card Sent </label></TD>
			<TD class="ContentTD"><html:text property="mbrCardSentDt" size="10" maxlength="10"/>
                            <a href="javascript:show_calendar('searchMembersForm.mbrCardSentDt');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;">
                            <IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></a> 
			</TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><label for="lstModDt">Member Last Updated</label></TD>
			<TD class="ContentTD"><html:text property="lstModDt" size="10" maxlength="10"/>
                            <a href="javascript:show_calendar('searchMembersForm.lstModDt');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;">
                            <IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></a> 
			</TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD"><LABEL for="lstModUserPk"> Member Updated By </LABEL></TD>
			<TD><html:text property="lstModUserPk" size="10" maxlength="10"/></TD>
                    </TR>
		<TR valign="top">
			<TD class="ContentHeaderTD">
				Mail 
			</TD>
			<TD>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD>
							<LABEL for="label_NoMail" class="smallFONT"> Mail</LABEL> 
						</TD>
						<TD class="smallFont">
							<html:radio property="noMailFg" value="no"/>
								Yes 
							<html:radio property="noMailFg" value="yes"/>
								No 
							<html:radio property="noMailFg" value=""/>
								All 
						</TD>
					</TR>
					<TR>
					</TR>
					<TR>
					</TR>
					<TR>
						<TD>
							<LABEL for="label_NoCards" class="smallFONT"> Cards</LABEL> 
						</TD>
						<TD class="smallFont">
							<html:radio property="noCardsFg" value="no"/>
								Yes 
							<html:radio property="noCardsFg" value="yes"/>
								No 
							<html:radio property="noCardsFg" value=""/>
								All  
						</TD>
					</TR>
					<TR>
						<TD>
							<LABEL for="label_NoPublicEmployee" class="smallFONT"> Public 
								Employee</LABEL> 
						</TD>
						<TD class="smallFont">
							<html:radio property="noPublicEmpFg" value="no"/>
								Yes 
							<html:radio property="noPublicEmpFg" value="yes"/>
								No 
							<html:radio property="noPublicEmpFg" value=""/>
								All  
						</TD>
					</TR>
					<TR>
						<TD>
							<LABEL for="label_NoLegislativeMail" class="smallFONT"> Legislative 
								Mail</LABEL> 
						</TD>
						<TD class="smallFont">
							<html:radio property="noLegislativeMailFg" value="no"/>
								Yes 
							<html:radio property="noLegislativeMailFg" value="yes"/>
								No 
							<html:radio property="noLegislativeMailFg" value=""/>
								All  
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
                </TABLE>
           </TD>

	 </TR>
    </TABLE> <!-- end of criteria table -->
<!-- Selection of Columns to display tables-->
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="100%" align="center">
		<TR valign="top">
			<TD colspan="3" class="ContentHeaderTR">Fields to display:</TD>
		</TR>
		<TR VALIGN="top">
			<TD width="33%">
			    <!-- First column of fields to display -->	
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD><html:multibox property="selectList" value="person_nm"/>
                                                    <LABEL for="selectList">Name </LABEL> 
                                                </TD>
        				</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="p.nick_nm"/>
                                                    <LABEL for="selectList">Nick Name </LABEL> 
                                                </TD>
					</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="p.alternate_mailing_nm"/>
                                                    <LABEL for="selectList">Alt Mail Name </LABEL> 
                                                </TD>
					</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="am.primary_information_source"/>
                                                    <LABEL for="selectList">Primary Information Source </LABEL> 
                                                </TD>
					</TR>
				</TABLE>
			</TD>
			
			<TD width="33%">
			<!-- Second column of fields to display -->	
                                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD>
							<html:multibox property="selectList" value="p.ssn"/>
							<LABEL for="ssn">Social Security Number </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD>
							<html:multibox property="selectList" value="p.valid_ssn_fg"/>
							<LABEL for="ssn">Valid SSN </LABEL> 
						</TD>
					</TR>
					<TR>
						<TD>
							<html:multibox property="selectList" value="sma"/>
							<LABEL for="selectList">System Mailing Address </LABEL> 
						</TD>
					</TR>
					<TR>
						<TD>
							<html:multibox property="selectList" value="pa.lst_mod_dt"/>
							<LABEL for="selectList">Address Last Updated </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD>
							<html:multibox property="selectList" value="pa.lst_mod_user_pk"/>
							<LABEL for="selectList">Address Updated By </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD>
							<html:multibox property="selectList" value="phone"/>
							<LABEL for="selectList">Affiliate Relations Home Phone </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD>
							<html:multibox property="selectList" value="email"/>
							<LABEL for="selectList">Primary Email </LABEL> 
						</TD>
					</TR>
				</TABLE>
			</TD>
                        <TD width="34%">
                        <!-- Third column of fields to display -->	    
                            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD width="10%"><html:multibox property="selectList" value="affId"/>	
							<LABEL for="selectList">Affiliate Identifier</LABEL> 
						</TD>
					</TR>
					<TR>
						<TD>
							<html:multibox property="selectList" value="am.mbr_type"/>
							<LABEL for="selectList">Member Type</LABEL> 
						</TD>
					</TR>
					<TR>
						<TD>
							<html:multibox property="selectList" value="am.mbr_status"/>
							<LABEL for="selectList">Member Status </LABEL> 
						</TD>
					</TR>
					<TR>
						<TD width="10%"><html:multibox property="selectList" value="am.person_pk"/>
							<LABEL for="selectList">Member Number </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="am.mbr_card_sent_dt"/>
							<LABEL for="selectList">Date Membership Card Sent </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="mail"/>
							<LABEL for="selectList">Mail </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="am.lst_mod_dt"/>
							<LABEL for="selectList">Member Last Updated </LABEL> 
						</TD>
					</TR>
                                        <TR>
						<TD width="10%"><html:multibox property="selectList" value="am.lst_mod_user_pk"/>
							<LABEL for="selectList">Member Updated By </LABEL> 
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>

	</TABLE> <!-- End of display fields table -->

  <!-- Bottom Buttons -->
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewBasicMemberCriteria.action">Basic Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<!-- Footer include -->
<%@ include file="../include/footer.inc" %> 


