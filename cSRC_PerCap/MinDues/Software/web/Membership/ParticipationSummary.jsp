<%! String title = "Participation Summary", help = "ParticipationSummary.html";%>
<%@ include file="../include/header.inc" %>

<!-- tabs -->
<bean:define id="screen" value="ParticipationSummary"/>
<%@ include file="../include/member_tab.inc" %>

<bean:define id="form" name="participationSummaryForm" type="org.afscme.enterprise.member.web.ParticipationSummaryForm"/>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <afscme:currentPersonName showPk="true" /> 
            <BR> <BR> 
	</TD>
    </TR>
</TABLE>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent">
    <tr>
	<th>
	    Select 
	</th>
	<th>
	    <afscme:sortLink styleClass="TH" action="/viewParticipationSummary.action" formName="form" field="group">Group</afscme:sortLink>
	</th>
	<th>
	    <afscme:sortLink styleClass="TH" action="/viewParticipationSummary.action" formName="form" field="type">Type</afscme:sortLink>
	</th>
	<th>
	    <afscme:sortLink styleClass="TH" action="/viewParticipationSummary.action" formName="form" field="detail">Detail</afscme:sortLink>
	</th>
	<th>
	    <afscme:sortLink styleClass="TH" action="/viewParticipationSummary.action" formName="form" field="outcome">Outcome</afscme:sortLink>
	</th>
	<th>
	    <afscme:sortLink styleClass="TH" action="/viewParticipationSummary.action" formName="form" field="date">Date</afscme:sortLink>
	</th>
    </tr>   
    <logic:iterate id="participationList" name="form" property="memberParticipations">  
    <bean:define id="data" name="participationList" type="org.afscme.enterprise.member.ParticipationData"/> 
    <bean:define id="outcome" name="data" type="org.afscme.enterprise.participationgroups.ParticipationOutcomeData" property="theParticipationOutcomeData" />  
    <tr>
	<td align="center">
	    <afscme:link page="/viewParticipationDetail.action" paramName="data" paramProperty="participDetailPk" paramId="participDetailPk" styleClass="action" title="View Affiliate Staff Detail">View</afscme:link> 
	    <afscme:link page="/deleteParticipationDetail.action" paramName="data" paramProperty="participDetailPk" paramId="participDetailPk" styleClass="action" title="Remove from Affiliate Staff" confirm="Are you sure you want to permanently Delete this Participation Record?">Remove</afscme:link> 
	</td>
	<td align="center">
	    <bean:write name="outcome" property="groupNm"/>
	</td>
	<td align="center">
	    <bean:write name="outcome" property="typeNm"/>
	</td>
	<td align="center">
	    <bean:write name="outcome" property="detailNm"/>
	</td>
	<td align="center">
	    <bean:write name="outcome" property="outcomeNm"/>
	</td>
	<td align="center">
	    <afscme:dateWrite name="data" property="mbrParticipDt"/>	
	</td>
    </tr>
    </logic:iterate>
    <TR>
	<TD align="center">
	    <afscme:link page="/addParticipationDetail.action" styleClass="action">Add</afscme:link> 
	</TD>
    </TR>
</table>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <afscme:currentPersonName showPk="true" /> 
            <BR> <BR> 
	</TD>
    </TR>
</TABLE>

<!-- Something for tabs. -->
<%@ include file="/include/member_tab.inc" %>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
