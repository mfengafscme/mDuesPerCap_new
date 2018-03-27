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
                    <logic:equal name="screen" value="OrganizationDetail">
                        <afscme:link page="/editOrganizationDetail.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Organization Detail">Edit</afscme:link>
                    </logic:equal>

                    <logic:equal name="screen" value="MailingListsInformation">
                        <logic:equal name="mailingListsInformationForm" property="editable" value="true">
                            <afscme:link page="/editMailingListsInformation.action" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" styleClass="TabFONT" title="Edit Mailing List Information">Edit</afscme:link>
                        </logic:equal>
                    </logic:equal>

                    <!-- Organization Detail tab -->
                    <logic:equal name="screen" value="OrganizationDetail">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                           <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Organization Detail</FONT></TD>
                       </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="OrganizationDetail">
                        <afscme:link page="/viewOrganizationDetail.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Organization Detail">Organization Detail</afscme:link>
                    </logic:notEqual>

                    <!-- Mailing Lists tab -->
                    <logic:equal name="screen" value="MailingListsInformation">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Mailing Lists</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="MailingListsInformation">
                        <afscme:link page="/viewMailingListsInformation.action?origin=Organization" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TR></TABLE></TD>" title="Mailing Lists Information" styleClass="TabFONT">Mailing Lists</afscme:link></TD>
                    </logic:notEqual>

                    <!-- Organization Associates tab -->
                    <logic:equal name="screen" value="OrganizationAssociate">
                        <TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR>
                            <TD align='center' class='TabTD' height='30'><FONT class="CurrentTabFONT">Organization Associates</FONT></TD>
                        </TR></TABLE></TD>
                    </logic:equal>
                    <logic:notEqual name="screen" value="OrganizationAssociate">
                        <afscme:link page="/viewOrganizationAssociateList.action?new" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Maintain Organization Associates" styleClass="TabFONT">Organization Associates</afscme:link>
                    </logic:notEqual>

                    <!-- Search Results tab -->
                    <logic:present name="searchOrganizationsForm">
                        <logic:present name="searchOrganizationsForm"  property="results"> 
                            <afscme:link page="/searchOrganizations.action" styleClass="TabFONT" prefix="<TD><TABLE class='InnerContentTable' border='0' cellspacing='0' cellpadding='0'><TR><TD align='center' class='TabTD' height='30'>" postfix="</TD></TR></TABLE></TD>" title="Return to Search Results">Search Results</afscme:link>
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
