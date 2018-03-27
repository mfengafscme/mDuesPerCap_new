<%! String title = "Employer Information", help = "EmployerInformation.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="employerInformationForm" id="eif" type="org.afscme.enterprise.member.web.EmployerInformationForm"/>

<!-- tabs need member affiliate tab -->
<bean:define id="screen" value="EmployerInformation"/>
<%@ include file="../include/member_aff_tab.inc" %>


<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
    	<TD class="ContentHeaderTR">
            <afscme:currentPersonName showPk="true" /> <BR>
            <afscme:currentAffiliate />
            <BR> <BR> 
	</TD>
    </TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top">
			<TD>
				<TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
					<TR>
						<TD class="ContentHeaderTD">Employer Name</TD>
						<TD>
							<bean:write name="eif" property="employerNm"/>
						</TD>
					</TR>
					<TR>
						<TD width="15%" class="ContentHeaderTD">Employee Job Title</TD>
						<TD width="85%">
							<afscme:codeWrite name="eif" property="jobTitle" codeType="EmployeeJobTitle" format="{1}" />
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Employee Sector</TD>
						<TD>
							<afscme:codeWrite name="eif" property="employeeSector" codeType="EmployeeSector" format="{1}" />
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Job Site</TD>
						<TD>
							<bean:write name="eif" property="jobSite"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Salary</TD>
						<TD>
                                                    <bean:write name="eif" property="salary"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Salary Range</TD>
						<TD>
							<afscme:codeWrite name="eif" property="salaryRange" codeType="EmployeeSalaryRange" format="{1}" />
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
</TABLE>

<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentHeaderTR">
				<BR>
				<afscme:currentPersonName showPk="true" /> <BR>
                                <afscme:currentAffiliate /> 
			</TD>
		</TR>
</TABLE>
	

<!-- tabs -->
<%@ include file="/include/member_aff_tab.inc" %>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 

