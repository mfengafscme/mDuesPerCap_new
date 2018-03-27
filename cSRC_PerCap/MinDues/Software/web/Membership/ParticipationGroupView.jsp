<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Participation Group View", help = "ParticipationGroupView.html";%>
<%@ include file="../include/header.inc" %>

<bean:define name="participationMaintenance" id="participationMaintenance" type="org.afscme.enterprise.participationgroups.web.ParticipationMaintenance"/>
<bean:define id="groupPk" name="participationMaintenance" property="group"/>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td>
            <BR><afscme:button forward="ViewParticipationGroupMaintenance">Return</afscme:button><BR><BR> 
	</td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr>
        <td class="ContentHeaderTR">
            Group - <%=participationMaintenance.getGroupNm()%><BR>
        </td>
    </tr>
    <tr valign="top">
        <td>
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th align="left" colspan="2">Type</th>
                    <th align="left" colspan="3">Detail</th>
                    <th align="left">Outcome</th>
                </tr>
                <tr>
                    <th class="small" width="23%" align="left">Name</th>
                    <th class="small" width="9%">Select</th>
                    <th class="small" width="23%" align="left">Name</th>
                    <th class="small" width="6%">Shortcut</th>
                    <th class="small" width="16%">Select</th>
                    <th class="small" width="23%" align="left">Name</th>
                </tr>
                <tr>
                    <td colspan="6"><HR></td>
                </tr>

<%String groupProp = "groupTypes["+groupPk+"]";%>
<logic:iterate id="types" name="participationMaintenance" property="<%=groupProp%>" type="org.afscme.enterprise.participationgroups.ParticipationTypeData">
    <bean:define id="typePk" name="types" property="typePk"/>
    <%String typeProp = "typeDetails["+typePk+"]";%>

                <tr>
                    <td class="BoldListContent">
                        <bean:write name="types" property="name"/>
                    </td>
                    <td class="BoldListContent" align="center">
			<afscme:link page='<%="/viewExportParticipationCodes.action?groupPk="+groupPk+"&typePk="+typePk%>' styleClass="action">Save As</afscme:link>
                    </td>
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="5"><HR></td>
                </tr>


    <logic:notEmpty name="participationMaintenance" property="<%=typeProp%>">
        <logic:iterate id="details" name="participationMaintenance" property="<%=typeProp%>" type="org.afscme.enterprise.participationgroups.ParticipationDetailData">
        <bean:define id="detailPk" name="details" property="detailPk"/>

                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td class="BoldListContent">
                        <bean:write name="details" property="name"/>
                    </td>
                    <td class="BoldListContent" align="center">
                        <bean:write name="details" property="shortcut"/> 
                    </td>

		<logic:equal name="details" property="status" value="true">
                    <td class="BoldListContent" align="center">
                        <afscme:link page='<%="/viewParticipationGroup.action?inactivate&groupPk="+groupPk+"&detailPk="+detailPk%>' styleClass="action" title="Deactivate Participation Detail" confirm="Are you sure you want to Deactivate this Detail? Once deactivated, it will no longer be used or maintained.">Deactivate</afscme:link>
                        &nbsp;|&nbsp; 
			<afscme:link page='<%="/viewExportParticipationCodes.action?groupPk="+groupPk+"&typePk="+typePk+"&detailPk="+detailPk%>' styleClass="action">Save As</afscme:link>
                    </td>
                    <td class="BoldListContent">&nbsp;</td>
		</logic:equal>

                </tr>

        <%String detailProp = "detailOutcomes["+detailPk+"]";%>
        <logic:notEmpty name="participationMaintenance" property="<%=detailProp%>">
            <logic:iterate id="outcomes" name="participationMaintenance" property="<%=detailProp%>" type="org.afscme.enterprise.participationgroups.ParticipationOutcomeData">

                <tr>
                    <td class="BoldListContent" colspan="5">&nbsp;</td>
                    <td class="BoldListContent">
                        <bean:write name="outcomes" property="outcomeNm"/>
                    </td>
                </tr>

            </logic:iterate>
        </logic:notEmpty>

	    <logic:equal name="details" property="status" value="true">
                <tr>
                    <td class="BoldListContent" colspan="5">&nbsp;</td>
                    <td class="BoldListContent">
			<afscme:link page='<%="/addParticipationGroupOutcome.action?typePk="+typePk+"&pk="+detailPk%>' styleClass="action" title="Add a New Outcome">Add An Outcome</afscme:link>
                    </td>
                </tr>
	    </logic:equal>

                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="4"><HR></td>
                </tr>
        </logic:iterate>
    </logic:notEmpty>

                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="4">
                        <afscme:link page="/addParticipationGroupDetail.action" paramId="pk" paramName="typePk" styleClass="action" title="Add a New Detail">Add A Detail</afscme:link>
                    </td>
                </tr>
                <tr>
                    <td colspan="6"><HR></td>
                </tr>

</logic:iterate>

                <tr>
                    <td class="BoldListContent" colspan="5">
                        <afscme:link page="/addParticipationGroupType.action" paramId="groupPk" paramName="groupPk" styleClass="action" title="Add a New Type">Add A Type</afscme:link>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td>
            <BR><afscme:button forward="ViewParticipationGroupMaintenance">Return</afscme:button><BR><BR> 
	</td>
    </tr>
</table>

<%@ include file="../include/footer.inc" %>
