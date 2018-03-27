<%! String title = "Basic Member Search", help = "BasicMemberSearch.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<SCRIPT language="JavaScript" src="../js/date.js"></SCRIPT>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD 
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<html:form action="searchMembers.action" focus="firstNm">
    <input type="hidden" name="reset">
    <html:hidden name="searchMembersForm" property="affPk"/>
    <html:hidden name="searchMembersForm" property="affCode"/>
    <html:hidden name="searchMembersForm" value="basic" property="input"/>
    
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR> 
                <afscme:button action="/viewPowerMemberCriteria.action">Power Search</afscme:button>
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
                        <TH width="28%">First Name</TH>
                        <TH width="28%">Middle Name</TH>
                        <TH width="28%">Last Name</TH>
                       <TH width="16%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD nowrap align="center"><html:text property="firstNm" size="25" maxlength="25"/><html:errors property="firstNm"/></TD>
			<TD nowrap align="center"><html:text property="middleNm" size="25" maxlength="25"/></TD>
                	<TD nowrap align="center"><html:text property="lastNm" size="25" maxlength="25"/><html:errors property="lastNm"/></TD>
			<TD align="center"><html:select property="suffixNm">
                            <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay=""/>
			 </html:select></TD>
                    </TR>
               </TABLE>
            </TD
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
                        <TD <TD align="center" class="ContentTD">
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
            <TD>
         </TR>   
	<TR valign="top">
        <!-- General Member Information -->    
            <TD>
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
			<TH nowrap colspan="2" width="100%" align="left">General Member Information</TH>
                    </TR>
		    <TR>
                       <TD nowrap width="40%" class="ContentHeaderTD" align="left"><LABEL for="personPk"> Member Number </LABEL></TD>
		       <TD nowrap width="60%" class="ContentHeader" align="left" ><html:text property="personPk" size="10" maxlength="10"/></TD>
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
			<TD class="ContentHeaderTD"><LABEL for="state"> State (System Mailing Address)</LABEL></TD>
			<TD>
                            <html:select property="state"><afscme:codeOptions useCode="true" codeType="State" format="{0}" allowNull="true" nullDisplay=""/>
                            </html:select>
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
					
				</TABLE>
			</TD>
			<TD width="34%">
                        <!-- Second column of fields to display -->	    
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
							<LABEL for="selectList">Member Number</LABEL> 
						</TD>
					</TR>
				</TABLE>
			</TD>
			<TD width="33%">
			<!-- Third column of fields to display -->	
                                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD>
							<html:multibox property="selectList" value="p.ssn"/>
							<LABEL for="ssn">Social Security Number </LABEL> 
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
							<html:multibox property="selectList" value="am.lst_mod_dt"/>
							<LABEL for="selectList">Last Updated</LABEL> 
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
                <afscme:button action="/viewPowerMemberCriteria.action">Power Search</afscme:button>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<!-- Footer include -->
<%@ include file="../include/footer.inc" %> 


