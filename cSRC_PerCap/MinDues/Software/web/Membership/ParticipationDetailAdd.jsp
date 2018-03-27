<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%! String title = "Participation Detail Add", help = "ParticipationDetailAdd.html";%>
<%@ include file="../include/header.inc" %>

<SCRIPT language="JavaScript" src="../js/membership.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
    <afscme:generateParticipationVariables />
</SCRIPT>
<bean:define id="form" name="participationDetailForm" type="org.afscme.enterprise.member.web.ParticipationDetailForm"/>
<html:form action="/saveParticipationDetailAdd.action" focus="detailShortcut">
	<input type="hidden" name="oldDetailShortcut" value=""/>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors property="org.apache.struts.action.GLOBAL_ERROR" /><BR>
        </TD>
    </TR>
</TABLE>
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
			* Group
		    </TH>
		    <TH width="30%">
			* Type
		    </TH>
		    <TH width="30%">
			* Detail
		    </TH>
		</TR>   		
		<TR>
		    <TD class="ContentTD" align="center">
                        <html:text property="detailShortcut" name="participationDetailForm" size="5" maxlength="6" onblur="shortcutExecute(this.form);"/>
                        <br>
			<html:errors property="detailShortcut"/>                         
                     <logic:notEmpty property="detailShortcut" name="participationDetailForm">
                        <html:hidden property="oldDetailShortcut" name="participationDetailForm" value="<%=form.getDetailShortcut().toString()%>" />
                        </logic:notEmpty>
		    </TD>
		    <TD class="ContentTD" align="center">
			<afscme:idSelect value="<%=form.getGroupPk()%>" whichKind="distinctGroups" property="groupPk" id="firstChoice" name="participationDetailForm" onchange="selectChange(this, participationDetailForm.secondChoice, arrItems1, arrItemsGrp1); clearShortcut(this.form);" />                      
			<br>
                        <html:errors property="groupPk"/>                        
		    </TD>
		    <TD class="ContentTD" align="center">
                        <afscme:idSelect value="<%=form.getTypePk()%>" whichKind="prepopTypes" property="typePk" id="secondChoice" name="participationDetailForm" onchange="selectChange(this, participationDetailForm.thirdChoice, arrItems2, arrItemsGrp2);clearShortcut(this.form);" />                        
                        <br>
			<html:errors property="typePk"/>                        
		    </TD>
		    <TD class="ContentTD" align="center">
                        <afscme:idSelect value="<%=form.getDetailPk()%>" whichKind="prepopDetails" property="detailPk" id="thirdChoice" name="participationDetailForm" onchange="selectChange(this, participationDetailForm.fourthChoice, arrItems3, arrItemsGrp3);clearShortcut(this.form);" />
                        <br>
			<html:errors property="detailPk"/>                        
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
	 	         * Outcome 
		    </TD>
		    <TD class="ContentTD" width="40%">
                        <afscme:idSelect whichKind="prepopOutcome" value="<%=form.getOutcomePk()%>" property="outcomePk" id="fourthChoice" name="participationDetailForm" />
                        <br>
			<html:errors property="outcomePk"/>                            
		    </TD>
		    <TD class="ContentHeaderTD" width="10%">
		        * Date
		    </TD>
		    <TD class="ContentTD" width="40%">
                        <html:text property="participationDate" name="participationDetailForm" size="10" maxlength="10"/>
                        <A href="javascript:show_calendar('participationDetailForm.participationDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width=24 height=22 border=0 alt="Calendar"></A>
                        <br>
			<html:errors property="participationDate"/>                            
                    </TD>
		</TR>
		<TR>
		    <TD class="ContentHeaderTD" colspan="4">
			Enter New Comments
		    </TD>
		</TR>
		<TR>
		    <TD class="ContentTD" colspan="4">
			<html:textarea property="comments" name="participationDetailForm" onkeyup="validateComments(this);" cols="105" rows="3"></html:textarea>
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
	    <html:submit styleClass="button"/>
	    <BR> <BR> 
        </TD>
        <TD align="right">
	    <BR> 
	    <html:reset styleClass="button" onclick="resetParticipation(this.form)" />
	    <afscme:button action="/viewParticipationSummary.action">Cancel</afscme:button>
        </TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR>
        <TD align="center">
 	    <BR><B><I>* Indicates Required Fields</I></B>
	    <BR>
        </TD>
    </TR>
</TABLE>
</html:form>


<%@ include file="../include/footer.inc" %> 

