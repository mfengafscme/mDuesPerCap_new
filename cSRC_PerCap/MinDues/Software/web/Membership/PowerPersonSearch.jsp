<%! String title = "Power Person Search", help = "PowerPersonSearch.html";%>
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
<html:form action="searchPowerPerson.action" focus="firstNm">
    <input type="hidden" name="reset">
<%--    <html:hidden name="searchPersonForm" property="personPk"/>
   
--%>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewBasicPersonCriteria.action">Basic Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
   <!-- Search Criteria Tables -->
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="100%" align="center">
        <TR>
            <TD class="ContentHeaderTR" colspan="2">
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
			<TD nowrap align="center"><html:text property="middleNm" value="" size="25" maxlength="25"/></TD>
                	<TD nowrap align="center"><html:text property="lastNm" value="" size="25" maxlength="25"/><html:errors property="lastNm"/></TD>
			<TD align="center"><html:select property="suffixNm">
                            <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay=""/>
			 </html:select></TD>
                    </TR>
        <!-- Display validation errors -->   
                    <TR>
                        <TD></TD>
                        <TD><html:errors property="firstNm"/></TD>
			<TD></TD>
                	<TD><html:errors property="lastNm"/></TD>
			<TD></TD>
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
		</TABLE>
	   </TD>
	</TR>	 
        
        
        <!-- General Person Information -->    
        <TR>
             <TH nowrap align="left">General Person Information</TH>
             <TH nowrap align="left">Search by Persona</TH>
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
                        <TD class="ContentHeaderTD"><LABEL for="ssn1"> Social Security Number </LABEL></TD>
                        <TD nowrap class="ContentHeader" align="left"><html:text property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>
							- 
							<html:text property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/>
							- 
							<html:text property="ssn3" size="4" maxlength="4"/> 
			</TD>
                    </TR>
                    <TR>
                       <TD nowrap  class="ContentHeaderTD" align="left"><LABEL for="personPk"> User Id </LABEL></TD>
		       <TD nowrap  class="ContentHeader" align="left" ><html:text property="personPk" size="10" maxlength="10"/></TD>
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
				    <TD nowrap width="23%" class="ContentHeader" align="left"><html:text property="areaCode" size="3" maxlength="3"/>			
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
                       <TD nowrap  class="ContentHeaderTD" align="left"> Marked for Deletion </TD>
		       <TD nowrap  class="smallFont">
                            <html:radio property="markedForDeletionFg" value="1" styleId="markedForDeletionFgYes"/>
                                Yes
                            <html:radio property="markedForDeletionFg" value="0" styleId="markedForDeletionFgNo"/>
                                No
                            <html:radio property="markedForDeletionFg" value="2" styleId="markedForDeletionFgAll"/>
                                All
                       </TD>
		    </TR>
		</TABLE>
	   </TD>
           <TD> <!-- second column -->
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="1" styleId="afscmeStaff">
                                <label for="afscmeStaff">AFSCME Staff</label></html:radio>
                       </TD>
                    </TR>
                    <TR>
   		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="2" styleId="affiliateStaff">
                                <label for="affiliateStaff">Affiliate Staff</label></html:radio>
                       </TD>
                    </TR>
                    <TR>
   		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="3" styleId="other">
                                <label for="other">Other</label></html:radio>
                       </TD>
                    </TR>
                    <TR>
   		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="4" styleId="member">
                                <label for="member">Member</label></html:radio>
                       </TD>
                    </TR>
                    <TR>
   		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="5" styleId="pacContributor">
                                <label for="pacContributor">PAC Contributor</label></html:radio>
                       </TD>
                    </TR>
                    <TR>
   		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="6" styleId="organizationAssociate">
                                <label for="organizationAssociate">Organization Associate</label></html:radio>
                       </TD>
                    </TR>
                    <TR>
   		       <TD nowrap  class="ContentHeader" align="left" >
                            <html:radio property="personaCode" value="0" styleId="all">
                                <label for="all">All</label></html:radio>
                       </TD>
		    </TR>
                </TABLE>
           </TD>

	 </TR>
    </TABLE> <!-- end of criteria table -->

  <!-- Bottom Buttons -->
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewBasicPersonCriteria.action">Basic Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<!-- Footer include -->
<%@ include file="../include/footer.inc" %> 


