<TABLE cellpadding="0" cellspacing="0" border="0" align="center" class="ContentTable">
    <TR bgcolor="#FCFFFF">
        <TD>
            <TABLE cellpadding="0" cellspacing="1" border="0" class="InnerContentTable" align="center">
                <TR bgcolor="#CCCC99">
                    <TD valign="bottom" height="3" colspan="30">
                    </TD>
                </TR>
                <TR>
                    <!--       For Member Affiliate & related processing . . . . -->
                    <!--      build appropriate Edit tab based on which screen we're on. -->

                    <logic:equal name="screen" value="MemberAffiliateInformation">
                        <afscme:link page="/editMemberAffiliateInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Member Affiliate Information">Edit</afscme:link>
                    </logic:equal>
                    
                    <logic:equal name="screen" value="EmployerInformation">
                        <afscme:link page="/editEmployerInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Employer Information">Edit</afscme:link>
                    </logic:equal>
                                
                    <!-- Member Affiliate tab -->
                    <logic:equal name="screen" value="MemberAffiliateInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Member Affiliate</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="MemberAffiliateInformation">
                        <afscme:link page="/viewMemberAffiliateInformation.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Member Affiliate Information">Member Affiliate</afscme:link>
                    </logic:notEqual>

			<!-- Employer tab -->
                    <logic:equal name="screen" value="EmployerInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Employer</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="EmployerInformation">
                        <afscme:link page="/viewEmployerInformation.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Employer Information">Employer</afscme:link>
                    </logic:notEqual>
	
                    
                     <!-- Generate Mbr Card Always . . .  there if tab is displayed. . . . . -->
                       <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <afscme:link page="/generateMbrCard.action" styleClass="TabFONT" onclick="alert(\'The member has been added to weekly run for generating Membership Cards.\');" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Generate Member Card">Generate Mbr Card</afscme:link>
                       </TR></TABLE></TD>
 
                    <!-- Member Detail tab - always on if this tab "bar" is on -->
                    
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <afscme:link page="/viewMemberDetail.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Member Detail">Member Detail</afscme:link>
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
