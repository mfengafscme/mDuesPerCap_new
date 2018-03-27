<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Officer Titles Edit", help = "OfficerTitlesEdit.html";%>
<%@ include file="../include/header.inc" %>
<SCRIPT LANGUAGE="JavaScript">

function indefiniteTemporaryTerm() {
	
	var rowCounter    = titlesTable.rows.length;
		
	for (var i=2; i < rowCounter; i++) {
	
	    //alert("checking indefinite or temporary terms: " + i)
	    //alert(titlesTable.rows(i).cells(4).childNodes(0).value)
	
    	if (titlesTable.rows(i).cells(4).childNodes(0).value == "63005") {
                titlesTable.rows(i).cells(4).childNodes(0).value = "63005";
    		titlesTable.rows(i).cells(5).childNodes(0).value = "9999";
    		titlesTable.rows(i).cells(5).childNodes(0).disabled = false;
    	} else if (titlesTable.rows(i).cells(4).childNodes(0).value == "63006") {
    		titlesTable.rows(i).cells(4).childNodes(0).value = "63006";
                titlesTable.rows(i).cells(5).childNodes(0).value = "9999";
    		titlesTable.rows(i).cells(5).childNodes(0).disabled = false;
    	} else {
    		titlesTable.rows(i).cells(5).childNodes(0).disabled = false;	
        }

    }
}

function checkReportingOfficer(cRow) {
    
  var rowCounter    = titlesTable.rows.length;
         
  for (var i=2; i < rowCounter; i++) {
    
    //alert("checking reporting officer flags " + i + " " + cRow)
    
    if (cRow != i) {
       
       if (titlesTable.rows(cRow).cells(7).childNodes(0).checked == true) {
                    
       titlesTable.rows(i).cells(7).childNodes(0).checked = false;   	  
    
       }
    
    }
  
  }
    
}

function checkNumWithTitle() {
  
  var rowCount = titlesTable.rows.length;
  
  for (var i=2; i < rowCount; i++) {
	   
	   if (titlesTable.rows(i).cells(2).childNodes(0).value == 1) {
	       titlesTable.rows(i).cells(7).childNodes(0).disabled = false; 
	   } else {   
	       titlesTable.rows(i).cells(7).childNodes(0).checked = false;
	       titlesTable.rows(i).cells(7).childNodes(0).disabled = true; 
	   } 
  }
  
}


</SCRIPT>

<bean:define id="eof" name="editOfficerTitlesForm" type="org.afscme.enterprise.affiliate.officer.web.EditOfficerTitlesForm"/>

<html:form action="/saveOfficerTitles.action" focus="autoDelegateProvision">
<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR>
				<html:submit styleClass="button"/>  
				<BR><BR> 
			</TD>
			<TD align="right">
				<BR>
				<html:reset styleClass="button"/>  
	            <html:cancel styleClass="button"/>  

			</TD>
		</TR>
	</TABLE>
<!-- Display current Affiliate Information -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentAffiliate />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<!-- Display Affiliate Constitution & Approved Constitution Flags -->
       <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top">
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTD" width="25%">
					                <LABEL for="label_ApprovedConstitutionFlag"> Approved Constitution</LABEL> 
						</TD>
                                    <TD width="25%">
							<html:checkbox name="eof" property="approvedConstitution" disabled="true"/>
                                                </TD>
						<TD class="ContentHeaderTD" width="25%">
							<LABEL for="label_AutomaticDelegateProvision"> Automatic Delegate Provision</LABEL> 
						</TD>
						<TD width="25%">
						<% if(eof.getApprovedConstitution().booleanValue() == true){ %>
                                            <html:checkbox name="eof" property="autoDelegateProvision" disabled="false"/>
                                    <% } else { %>
                                            <html:checkbox name="eof" property="autoDelegateProvision" disabled="true"/>
                                    <% } %>
                                    </TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>

<!-- Display Column Headers, etc -->
	<table id="titlesTable" align="center" cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<TR>
			<TD colspan="10" class="ContentHeaderTR">
				Offices 
			</TD>
		</TR>
		<TR>
			<TH>Constitutional Title</TH>
			<TH>Affiliate Title</TH>
			<TH width="7%">* # with Title</TH>
			<TH width="7%">* Month of Election</TH>
			<TH width="7%">* Length of Term</TH>
			<TH width="7%">Term End</TH>
			<TH width="7%">Delegate Priority</TH>
			<TH width="2%">RO</TH>
			<TH width="5%">E-Bd</TH>
		</TR>
<!-- Display current officer titles for Affiliate -->
<% int cRow=2; %>
<nested:iterate name="eof" id="officerTitlesList" property="officerTitlesList">
<bean:define name="officerTitlesList" id="officeData" type="org.afscme.enterprise.affiliate.officer.OfficeData"/>
<input type="hidden" property="currentRow" value="<%= cRow %>"/>
		<TR onClick="checkReportingOfficer(<%= cRow %>);">    
		            
                <nested:hidden property="affPk" />
	            <nested:hidden property="officePk" />
                <nested:hidden property="officeGroupID" />      	
			<td align="center">
			  <nested:hidden property="afscmeTitle"/>			
	                  <afscme:codeWrite name="officeData" codeType="AFSCMETitle" property="afscmeTitle" format="{1}"/>
			</td>
			<TD align="center" class="ContentTD">
			    <nested:text property="affiliateTitle" size="30" maxlength="30"/>
			</TD>			
			<td align="center">
			       <nested:text property="numWithTitle" size="2" maxlength="3" onchange="checkNumWithTitle();"/>
			</td>
			<td align="center">
      	          <nested:select property="monthOfElection" >
                     <afscme:codeOptions useCode="false" codeType="MonthOffcrElection" allowNull="true" nullDisplay="" format="{1}"/>
                  </nested:select>
			</td>
			<td align="center">
      	          <nested:select property="lengthOfTerm" onchange="indefiniteTemporaryTerm();">
                     <afscme:codeOptions useCode="false" codeType="TermLength" allowNull="true" nullDisplay="" format="{1}" />
                  </nested:select>             
			</td>
			<td align="center">
			      <% if(officeData.getTermEnd().intValue() == 9999){ %>  
            	    <nested:text property="termEnd" size="4" maxlength="4" disabled="false"/>
            	  <% } else { %>
            	    <nested:text property="termEnd" size="4" maxlength="4" disabled="false"/>
            	  <% } %> 
			</td>
            <td align="center">
      	          <nested:text property="delegatePriority" size="2" maxlength="2"/>
			</td>
            <td align="center">
                    <% if(officeData.getAfscmeTitle().intValue() == 6046){ %>  
                          <nested:radio value="true" property="reportingOfficer" disabled="true" />
                     <% } else if ((officeData.getAfscmeTitle().intValue() != 6046) && (officeData.getNumWithTitle().intValue() > 1)){ %>
                            <nested:radio value="true" property="reportingOfficer" disabled="true"/>
                     <% } else if ((officeData.getAfscmeTitle().intValue() != 6046) && (officeData.getNumWithTitle().intValue() == 1)) { %>
                             <nested:radio value="true" property="reportingOfficer" disabled="false" />
                     <% } %>  
            </td>
            <td align="center">
                  <% if(officeData.getAfscmeTitle().intValue() == 6046){ %>  
	                   <nested:checkbox property="execBoard" disabled="true"/>
	              <% } else { %>
	                   <nested:checkbox property="execBoard" disabled="false"/>
	              <% } %>         
            </td>
		</tr>
		<% cRow++; %>
</nested:iterate>
            <!-- Display validation errors -->		
		    <TR>
			            <TD></TD>
                        <TD></TD>
			            <TD align="middle"><html:errors property="numWithTitle"/></TD>
			            <TD align="middle"><html:errors property="monthOfElection"/></TD>
		                <TD align="middle"><html:errors property="lengthOfTerm"/></TD>
                        <TD align="middle"><html:errors property="termEnd"/></TD>
                        <TD></TD>
                        <TD></TD>
                        <TD></TD>
            </TR>	

</TABLE>
<!-- Auto Executive Board Members -->	
    <table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD colspan="3" class="ContentHeaderTR">
				Auto Executive Board Title 
			</TD>
		</TR>
		<TR>
			<TH width="15%">
				Affiliate Title
			</TH>
			<TH width="15%">
				Sub-Affiliate Title
			</TH>
			<TH width="8%">
				# with Title 
			</TH>
		</TR>
		<TR align="center">
			<TD>
			<html:select property="affiliateTitlePk" name="eof">
                              <afscme:codeOptions codeType="AFSCMETitle" allowNull="true" nullDisplay="[Select]" format="{1}" excludeCodes="1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52"/>
                        </html:select>
 
			</TD>
			<TD>
			<html:select property="subAffiliateTitlePk" name="eof">
                              <afscme:codeOptions codeType="AFSCMETitle" allowNull="true" nullDisplay="[Select]" format="{1}"/>
                        </html:select>
			</TD>
			<TD>
				Variable 
			</TD>
		</TR>
		<TR>
<!-- comment for Officer Titles -->
            <TD COLSPAN="3">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH align="left">Enter New Comments</TH>
                    </TR>
                    <TR>
                        <TD>
                            <html:textarea name="eof" property="comment" onkeyup="validateComments(this);" cols="105" rows="3"></html:textarea>
                        </TD>
                    </TR>
                </TABLE>
            </TD>  
        </TR>
        </table>              
       <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR>
				<html:submit styleClass="button"/>  
				<BR><BR> 
			</TD>
			<TD align="right">
				<BR>
				<html:reset styleClass="button"/>  
	            <html:cancel styleClass="button"/>  

			</TD>
		</TR>
	</TABLE>
	
</html:form>	
<!-- Display AFSCME Footer
<%@ include file="../include/footer.inc" %> 
