<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Officer Titles", help = "OfficerTitles.html";%>
<%@ include file="../include/header.inc" %>
 
<bean:define id="form" name="officerTitlesForm" type="org.afscme.enterprise.affiliate.officer.web.OfficerTitlesForm"/>

<!-- Something for tabs. -->
<bean:define id="screen" value="OfficerTitles"/>
<%@ include file="../include/affiliate_tab.inc" %>

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
							<html:checkbox name="form" property="approvedConstitution" disabled="true"/>
                                                </TD>
						<TD class="ContentHeaderTD" width="25%">
							<LABEL for="label_AutomaticDelegateProvision"> Automatic Delegate Provision</LABEL> 
						</TD>
						<TD width="25%">
							<html:checkbox name="form" property="autoDelegateProvision" disabled="true"/>
                                                </TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>

<!-- Display Column Headers, etc -->
	<TABLE align="center" cellpadding="0" cellspacing="0" border="1" class="BodyContent">
		<tr>
			<td colspan="12" class="ContentHeaderTR">
				Offices        <html:errors property="removed"/> 
			</td> 
		</tr>
		<tr>
			<th width="6%">
				Select 
			</th>
 			<th>
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="constitutionalTitle">Constitutional Title</afscme:sortLink>
            </th>
            <th width="20%">
		            <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="affiliateTitle">Affiliate Title</afscme:sortLink>
			</th>
			<th width="5%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="numWithTitle"># with Title</afscme:sortLink>
			</th>
			<th width="7%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="monthOfElection">Month of Election</afscme:sortLink>
			</th>
			<th width="7%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="lenghtOfTerm">Length of Term</afscme:sortLink>
			</th>
			<th width="5%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="termEnd">Term End</afscme:sortLink>
			</th>
			<th width="7%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="delegatePriority">Delegate Priority</afscme:sortLink>
			</th>
			<th width="2%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="reportingOfficer">RO</afscme:sortLink>
			</th>
			<th width="4%">
                    <afscme:sortLink styleClass="TH" action="/viewOfficerTitles.action" formName="form" field="eboard">E-Bd</afscme:sortLink>
		    </th>
		</tr>

<!-- Display current officer titles for Affiliate -->
<logic:notEmpty name="form" property="results">
<logic:iterate name="form" id="results" property="results">
<bean:define name="results" id="officerTitles" type="org.afscme.enterprise.affiliate.officer.OfficeData"/>
		<TR>
	             <!-- Link to remove officer -->    
		         <TD align="center">	
		         <% if(officerTitles.getNumWithTitle().intValue() == 1){ %>	        
                      <afscme:link page='<%="/removeOfficerTitle.action?afscmeOfficePk="+officerTitles.getAfscmeTitle()+"&officeGroupId="+officerTitles.getOfficeGroupID()%>' styleClass="action" confirm="Are you sure you wish to remove this Officer Title?">Remove</afscme:link>
          		 <% } %> 
          		 </TD>                    	
			<td align="center">
	                <afscme:codeWrite name="officerTitles" codeType="AFSCMETitle" property="afscmeTitle" format="{1}"/> 
			</td>
			<td align="center">
	                <bean:write name="officerTitles" property="affiliateTitle"/>
			</td>
			<td align="center">
      	          <bean:write name="officerTitles" property="numWithTitle"/>
			</td>
			<td align="center">
	                <afscme:codeWrite name="officerTitles" codeType="MonthOffcrElection" property="monthOfElection" format="{1}"/> 
                	</td>
			<td align="center">
      	          <afscme:codeWrite name="officerTitles" codeType="TermLength" property="lengthOfTerm" format="{1}"/>               
			</td>
			<td align="center">
            	    <bean:write name="officerTitles" property="termEnd"/>
			</td>
                  <td align="center">
      	          <bean:write name="officerTitles" property="delegatePriority"/>
			</td>
                  <td align="center">
	                <html:radio name="officerTitles" property="reportingOfficer" value="true" disabled="true"/>
                	</td>
                  <td align="center">
	                <html:checkbox name="officerTitles" property="execBoard" disabled="true"/>
                	</td>
		</tr>
</logic:iterate>
</logic:notEmpty>

<!-- Display link to Add New Officer Title -->
		<TR>
			<TD colspan="12">&nbsp;</TD>
		</TR>

		<TR>
			<TD align="center">
		    <afscme:link page="/addOfficerTitle.action?add" styleClass="action">Add</afscme:link> 
  	                </TD>
			<TD align="center">New Officer Title</TD>
			<TD colspan="10">&nbsp;</TD>
		</TR>
	</TABLE>
 
<!-- TODO: Auto Executive Board Members -->	
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
			<afscme:codeWrite name="form" codeType="AFSCMETitle" property="affiliateTitlePk" format="{1}"/> 
			</TD>
			<TD>
			<afscme:codeWrite name="form" codeType="AFSCMETitle" property="subAffiliateTitlePk" format="{1}"/> 
			</TD>
			<TD>
				Variable 
			</TD>
		</TR>
<!-- Display most recent comment for Officer Titles -->
                <table cellpadding="1" cellspacing="1" border="1" class="BodyContent">
                    <logic:present name="comment">
                        <tr>
                            <th align="left">
                                <afscme:link page="/viewOfficerTitlesCommentHistory.action" styleClass="TH">View More Comments</afscme:link>
                            </th>
                        </tr>

                        <tr>
                            <td>
<pre>
<bean:write name="comment" />


</pre>
                            </td>
                        </tr>
                    </logic:present>

                <logic:notPresent name="comment">
                       <tr>
                           <th align="left" colspan="6">No Comments</th>
                       </tr>
                </logic:notPresent>
</table>

<!-- Display current Affiliate Information -->
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
          <TR>
            <TD class="ContentHeaderTR">
             <BR>
             <afscme:currentAffiliate />
          </TD>
        </TR>
        </TABLE>

<!-- Display Menu Tabs -->
<!-- Something for tabs. -->
<%@ include file="/include/affiliate_tab.inc" %>

<!-- Display AFSCME Footer
<%@ include file="../include/footer.inc" %> 
