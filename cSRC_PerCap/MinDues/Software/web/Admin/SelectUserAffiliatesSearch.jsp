<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Select Affiliate Privileges Search", help = "SelectUserAffiliatesSearch.html";%>
<%@ include file="../include/header.inc" %>

<SCRIPT language="JavaScript" src="../js/membership.js"></SCRIPT>

<html:form action="selectUserAffiliatesSearch" focus="local">
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="80%" align="center">
		<TR>
			<TD class="ContentHeaderTR">
				Fields to Search: <BR>
				&nbsp; 
			</TD>
		</TR>
		<TR valign="top">
			<TD class="ContentHeaderTR">
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TH nowrap>
							Type
						</TH>
						<TH nowrap>
							Local/Sub Chapter 
						</TH>
						<TH nowrap>
							State/National Type 
						</TH>
						<TH nowrap>
							Sub Unit 
						</TH>
						<TH nowrap>
							Council/Retiree Chapter 
						</TH>
						<TH nowrap>
							Selected 
						</TH>
					</TR>
					<TR>
						<TD align="center" class="ContentTD">
                            <html:select property="type" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form)">

                                <afscme:codeOptions useCode="true" codeType="AffiliateType" format="{0}" allowNull="true" nullDisplay=""/>
                            </html:select>

						</TD>
						<TD align="center" class="ContentTD">
							<html:text property="local" size="4" maxlength="4"/> 
						</TD>
						<TD align="center" class="ContentTD">
                            <html:select property="state">
                                <afscme:codeOptions codeType="AffiliateState" useCode="true" allowNull="true" nullDisplay="" format="{0}" />
                            </html:select>
						</TD>
						<TD align="center" class="ContentTD">
							<html:text property="subUnit" size="4" maxlength="4"/> 
						</TD>
						<TD align="center" class="ContentTD">
							<html:text property="council" size="4" maxlength="4"/> 
						</TD>
						<TD align="center" class="ContentTD">
                            <html:select property="selected">
                                <html:option value=""></html:option>
                                <html:option value="true">Yes</html:option>
                                <html:option value="false">No</html:option>
                            </html:select>
						</TD>
					</TR>
				</TABLE>
			</TD>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
        <tr><td colspan="2" align="center"><html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
		<TR valign="top">
			<TD align="left">
                <html:submit styleClass="button"/>
			</TD>
			<TD align="right">
                <html:reset styleClass="button"/>
                <html:cancel styleClass="button"/>
			</TD>
		</TR>
	</TABLE>
</html:form>

<%@ include file="../include/footer.inc" %>

