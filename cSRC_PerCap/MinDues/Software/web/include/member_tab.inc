<TABLE cellpadding="0" cellspacing="0" border="0" align="center" class="ContentTable">
    <TR bgcolor="#FCFFFF">
        <TD>
            <TABLE cellpadding="0" cellspacing="1" border="0" class="InnerContentTable" align="center">
                <TR bgcolor="#CCCC99">
                    <TD valign="bottom" height="3" colspan="30">
                    </TD>
                </TR>
                <TR>

                    <!--      build appropriate Edit tab based on which screen we're on. -->

                    <logic:equal name="screen" value="MemberDetail">
                        <afscme:link page="/editMemberDetail.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Member Detail">Edit</afscme:link>
                    </logic:equal>
                    
                    <logic:equal name="screen" value="GeneralDemographicInformation">
                        <afscme:link page="/editDemographicData.action?origin=Member" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit General Demographic Information">Edit</afscme:link>
                    </logic:equal>

                    <logic:equal name="screen" value="PoliticalLegislativeInformation">
                        <afscme:link page="/editPoliticalData.action?origin=Member" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Political/Legislative Information">Edit</afscme:link>
                    </logic:equal>
            
                    <!-- Member Detail tab -->
                    <logic:equal name="screen" value="MemberDetail">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Member Detail</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="MemberDetail">
                        <afscme:link page="/viewMemberDetail.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Member Detail">Member Detail</afscme:link>
                    </logic:notEqual>
                    
                    <!-- General Demographic tab -->
                    <logic:equal name="screen" value="GeneralDemographicInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">General Demographic</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="GeneralDemographicInformation">
                        <afscme:link page="/viewDemographicData.action?origin=Member" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="General Demographic Information">General Demographic</afscme:link>
                    </logic:notEqual>
                            
                    <!-- Political Legislative tab -->
                    <logic:equal name="screen" value="PoliticalLegislativeInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Political/Legislative</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="PoliticalLegislativeInformation">
                        <afscme:link page="/viewPoliticalData.action?origin=Member" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Political/Legislative Information">Political/Legislative</afscme:link>
                    </logic:notEqual>

                    <!-- Mailing Lists tab -->
                    <logic:equal name="screen" value="MailingListsInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Mailing Lists</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="MailingListsInformation">
                        <!-- Update this later -->
                        <afscme:link page="/viewMailingListsInformation.action?origin=Member" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Mailing Lists Information">Mailing Lists</afscme:link>
                    </logic:notEqual>
                    
		    <!-- Mailing Lists tab --> 
                    <logic:equal name="screen" value="ParticipationSummary">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Participation</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>		    
                    <logic:notEqual name="screen" value="ParticipationSummary">
                        <afscme:link page="/viewParticipationSummary.action?origin=Member" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Participation">Participation</afscme:link>
                    </logic:notEqual>

                    <!-- Political Rebate tab -->
                    <logic:equal name="screen" value="PoliticalRebateList">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Political Rebate</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="PoliticalRebateList">
                        <!-- Update this later -->
                        <afscme:link page="/viewPoliticalRebateSummaryByYear.action?origin=Member" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="View Political Rebate List">Political Rebate</afscme:link>
                    </logic:notEqual>

                    <!-- Correspondence History tab -->
                    <logic:equal name="screen" value="CorrespondenceHistory">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Correspondence History</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="CorrespondenceHistory">
                        <!-- Update this later -->
                        <afscme:link page="/viewCorrespondenceHistory.action?origin=Member" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="View Correspondence History">Correspondence History</afscme:link>
                    </logic:notEqual>

                    <!-- Pending Reports tab -->
                    <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                        <TD align='center' class='TabTD' height='30'><FONT class="TabFONT">Maintain Pending Reports</FONT></TD>
                    </TR></TABLE></TD>

                    <!-- PAC tab -->
                    <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                        <TD align='center' class='TabTD' height='30'><FONT class="TabFONT">PAC</FONT></TD>
                    </TR></TABLE></TD>

                    <!-- Organizing tab -->
                    <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                        <TD align='center' class='TabTD' height='30'><FONT class="TabFONT">Organizing Details</FONT></TD>
                    </TR></TABLE></TD>
                    
                    <!-- Search Results tab -->
                    <logic:present name="searchMembersForm">
                        <logic:present name="searchMembersForm"  property="results"> 
                            <afscme:link page="/searchMembers.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Return to Search Results">Search Results</afscme:link>
                        </logic:present>
                    </logic:present>

		</TR>
                <TR bgcolor="#CCCC99">
                    <TD valign="bottom" height="3" colspan="30">
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
</TABLE>
