<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Participation Detail", help = "ParticipationDetail.html";%>
<%@ include file="../include/header.inc" %>

<html:form action="/editParticipationDetail.action" focus="groupPk">
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <afscme:currentPersonName showPk="true" /> 
            <BR> <BR> 
	</TD>
    </TR>
</TABLE>

<table width="100%" cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" align="center">
    <TR>
	<TD colspan="2">
	    <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
		<TR>
		    <TH width="10%">
			Shortcut
		    </TH>
		    <TH width="30%">
			Group
		    </TH>
		    <TH width="30%">
			Type
		    </TH>
		    <TH width="30%">
			Detail
		    </TH>
		</TR>   		
		<TR>
		    <TD class="ContentTD" align="center">
		    	<bean:write property="detailShortcut" name="participationDetailForm" />
		    </TD>
		    <TD class="ContentTD" align="center">
			<bean:write property="groupNm" name="participationDetailForm" />
			<html:hidden property="groupPk"/>
		    </TD>
		    <TD class="ContentTD" align="center">
			<bean:write property="typeNm" name="participationDetailForm" />
			<html:hidden property="typePk"/>
		    </TD>
		    <TD class="ContentTD" align="center">
                        <bean:write property="detailNm" name="participationDetailForm" />
			<html:hidden property="detailPk"/>                        
		    </TD>
		</TR>
	    </TABLE>
	</TD>
    </TR>
    <TR align="center">
	<TD>
	    <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
	        <TR>
	            <TD class="ContentHeaderTD" width="10%">
	 	         Outcome
		    </TD>
		    <TD class="ContentTD" width="40%">
                        <bean:write property="outcomeNm" name="participationDetailForm" />
                        <html:hidden property="outcomePk"/>
		    </TD>
		    <TD class="ContentHeaderTD" width="10%">
		        Date
		    </TD>
		    <TD class="ContentTD" width="40%">
                        <bean:write property="participationDate" name="participationDetailForm" />
                    </TD>
		</TR>
		<TR>
		    <TD class="ContentHeaderTD" colspan="4">
			Comments
		    </TD>
		</TR>
		<TR>
		    <TD class="ContentTD" colspan="4">
                        <bean:write property="comments" name="participationDetailForm" />
		    </TD>
		</TR>
	    </TABLE>
	</TD>
    </TR>
    <TR align="center">
	<TD colspan="2">
	    <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
		<TR>
		    <TD class="ContentHeaderTD" width="10%">
			Last Updated
		    </TD>
		    <TD class="ContentTD" align="left" width="40%">
                        <bean:write property="lastUpdatedDate" name="participationDetailForm" />		    	
		    </TD>
		    <TD class="ContentHeaderTD" width="10%">
			Updated By
		    </TD>
		    <TD class="ContentTD" align="left" width="40%">
			<afscme:userWrite name="participationDetailForm" property="lastUpdatedBy"/>		    
		    </TD>
		</TR>
	    </TABLE>
	</TD>
    </TR>    
</TABLE>


<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR>
	    <afscme:button action="/viewParticipationSummary.action">Return</afscme:button> 
        </TD>
        <TD align="right">
	    <BR>
	    <html:submit styleClass="button">Edit</html:submit>
        </TD>
    </TR>
</TABLE>

</html:form>


<%@ include file="../include/footer.inc" %> 
