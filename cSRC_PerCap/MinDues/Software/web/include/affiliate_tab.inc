<logic:notEqual name="screen" value="MailingListInformation">
	<bean:define id="affil" value="<%=org.afscme.enterprise.controller.web.AFSCMEAction.getCurrentAffiliateId(session).getType().toString()%>" />
</logic:notEqual>

<TABLE cellpadding="0" cellspacing="0" border="0" align="center" class="ContentTable">
    <TR bgcolor="#FCFFFF">
        <TD>
            <TABLE cellpadding="0" cellspacing="1" border="0" class="InnerContentTable" align="center">
                <TR bgcolor="#CCCC99">
                    <TD valign="bottom" height="3" colspan="30">
                    </TD>
                </TR>
                <TR>
                    <!-- build appropriate Edit tab based on which screen we're on. -->
                    <logic:equal name="screen" value="AffiliateDetail">
                        <afscme:link page="/editAffiliateDetail.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Affiliate Detail">Edit</afscme:link>
                    </logic:equal>

                    <logic:equal name="screen" value="CharterInformation">
                        <afscme:link page="/editCharterInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Charter Information">Edit</afscme:link>
                    </logic:equal>
		    
		        <logic:equal name="screen" value="ConstitutionInformation">
	  	            <logic:notEqual name="affil" value="U">
                    	    <afscme:link page="/editConstitutionInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Constitution Information">Edit</afscme:link>
                        </logic:notEqual>
                    </logic:equal>

                    <logic:equal name="screen" value="OfficerTitles">
                       <afscme:link page="/editOfficerTitles.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Officer Titles">Edit</afscme:link>
                    </logic:equal>                     
                    
                    <logic:equal name="screen" value="FinancialInformation">
                        <afscme:link page="/editFinancialInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Financial Information">Edit</afscme:link>
                    </logic:equal>

                    <logic:equal name="screen" value="MembershipReportingInformation">
                        <afscme:link page="/editMembershipReportingInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Membership Reporting Information">Edit</afscme:link>
                    </logic:equal>

                    <logic:equal name="screen" value="MaintainAffiliateOfficers">
                        <afscme:link page="/editAffiliateOfficerMaintenance.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Maintain Officers">Edit</afscme:link>
                    </logic:equal>

                    <logic:equal name="screen" value="MailingListsInformation">
                        <logic:equal name="mailingListsInformation" property="editable" value="true">
                            <afscme:link page="/editMailingListsInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TR></TABLE></TD>" title="Edit Mailing List Information" styleClass="TabFONT">Edit</afscme:link></TD>
                        </logic:equal>
                    </logic:equal>

                    <logic:equal name="screen" value="AffiliateDetail">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Affiliate Detail</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="AffiliateDetail">
                        <afscme:link page="/viewAffiliateDetail.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Affiliate Detail">Affiliate Detail</afscme:link>
                    </logic:notEqual>
                    

                    <logic:equal name="screen" value="CharterInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Charter</FONT></TD>
                       </TR></TABLE></TD>                            
                    </logic:equal>
                    <logic:notEqual name="screen" value="CharterInformation">
                        <afscme:link page="/viewCharterInformation.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Charter">Charter</afscme:link>
                    </logic:notEqual>

		    <logic:equal name="screen" value="ConstitutionInformation">
		        <logic:notEqual name="affil" value="U">
				<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
				<TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Constitution</FONT></TD>
				</TR></TABLE></TD>
			</logic:notEqual>
		    </logic:equal>

	            <logic:notEqual name="screen" value="ConstitutionInformation">
	  	        <logic:notEqual name="affil" value="U">
			    <afscme:link page="/viewConstitutionInformation.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Constitution">Constitution</afscme:link>
		        </logic:notEqual>
		    </logic:notEqual>

                    <logic:equal name="screen" value="OfficerTitles">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="TabFONT">Officer Titles</FONT></TD>
                        </TR></TABLE></TD>                       
                    </logic:equal>
                    <logic:notEqual name="screen" value="OfficerTitles">
                        <afscme:link page="/viewOfficerTitles.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Officer Titles">Officer Titles</afscme:link>
                    </logic:notEqual>

                    <logic:equal name="screen" value="FinancialInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Financial</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="FinancialInformation">
                        <afscme:link page="/viewFinancialInformation.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Financial">Financial</afscme:link>
                    </logic:notEqual>

                    <logic:equal name="screen" value="MembershipReportingInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Membership Reporting</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>

                    <logic:notEqual name="screen" value="MembershipReportingInformation">
                        <afscme:link page="/viewMembershipReportingInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Membership Reporting Information" styleClass="TabFONT">Membership Reporting</afscme:link>
                    </logic:notEqual>

                    <logic:equal name="screen" value="MaintainAffiliateOfficers">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Maintain Officers</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                  
                    <logic:notEqual name="screen" value="MaintainAffiliateOfficers">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <afscme:link page="/viewAffiliateOfficerMaintenance.action?new=" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Maintain Officers">Maintain Officers</afscme:link>
                        </TR></TABLE></TD>
                    </logic:notEqual>

                    <logic:equal name="screen" value="MailingListsInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Mailing Lists</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="MailingListsInformation">
                        <afscme:link page="/viewMailingListsInformation.action?origin=Affiliate" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TR></TABLE></TD>" title="Mailing Lists Information" styleClass="TabFONT">Mailing Lists</afscme:link></TD>
                    </logic:notEqual>

                    <logic:equal name="screen" value="ChangeHistory">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Change History</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="ChangeHistory">
                        <afscme:link page="/viewAffiliateChangeHistoryCriteria.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="View Affiliate Change History">Change History</afscme:link>
                    </logic:notEqual>

                    <logic:equal name="screen" value="OfficerHistoryForAffiliate">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="TabFONT">Officer History</FONT></TD>
                        </TR></TABLE></TD>                       
                    </logic:equal>
                    <logic:notEqual name="screen" value="OfficerHistoryForAffiliate">
                        <afscme:link page="/viewOfficerHistoryForAffiliate.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Officer History">Officer History</afscme:link>
                    </logic:notEqual>

                    <logic:equal name="screen" value="AffiliateStaff">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Staff</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="AffiliateStaff">
                        <afscme:link page="/viewStaffMaintainence.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="" styleClass="TabFONT">Staff</afscme:link>
                    </logic:notEqual>

                    <logic:present name="affiliateSearchForm">
                        <logic:present name="affiliateSearchForm"  property="results"> 
                            <afscme:link page="/searchPowerAffiliate.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Return to Search Results">Search Results</afscme:link>
                        </logic:present>
                    </logic:present>

                    <afscme:link page="/viewBasicAffiliateCriteria.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Perform a New Search" styleClass="TabFONT">New Search</afscme:link>

                </TR>
                <TR bgcolor="#CCCC99">
                    <TD valign="bottom" height="3" colspan="30">
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
</TABLE>
