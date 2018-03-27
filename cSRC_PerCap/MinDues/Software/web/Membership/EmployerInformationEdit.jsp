<%! String title = "Employer Information Edit", help = "EmployerInformationEdit.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="employerInformationForm" id="eif" type="org.afscme.enterprise.member.web.EmployerInformationForm"/>

<html:form action="saveEmployerInformation.action" focus="employerNm">

<!-- Send required fields back with form parameters -->
<html:hidden property="personPk"/>
<html:hidden property="affPk"/>

<!-- Display global errors -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
            <TD align='center'>
                <html:errors property="org.apache.struts.action.GLOBAL_ERROR"/>
            </TD>
        </TR>
</TABLE>
<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewEmployerInformation.action">Cancel</afscme:button>
           </td>
        </tr>      
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
</TABLE>
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
							<html:text name="eif" property="employerNm"/>
						</TD>
					</TR>
					<TR>
						<TD width="15%" class="ContentHeaderTD">Employee Job Title</TD>
						<TD width="85%">
                                                    <html:select name="eif" property="jobTitle">
							<afscme:codeOptions codeType="EmployeeJobTitle" format="{1}" allowNull="true" nullDisplay=""/>
                                                    </html:select>    
                                                </TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Employee Sector</TD>
						<TD>
                                                    <html:select name="eif" property="employeeSector">	
                                                        <afscme:codeOptions codeType="EmployeeSector" format="{1}" allowNull="true" nullDisplay=""/>
                                                    </html:select>    
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">Job Site</TD>
						<TD>
							<html:text name="eif" property="jobSite"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">* Salary</TD>
						<TD>
                                                    <html:text name="eif" property="salary" onchange="clearTheOtherSalaryField(this.form, 'salary');"/>
						</TD>
					</TR>
					<TR>
						<TD class="ContentHeaderTD">* Salary Range</TD>
						<TD>
                                                    <html:select name="eif" property="salaryRange" onchange="clearTheOtherSalaryField(this.form, 'salaryRange');">	
                                                        <afscme:codeOptions codeType="EmployeeSalaryRange" format="{1}" allowNull="true" nullDisplay="" />
                                                    </html:select>        
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
				<afscme:currentPersonName showPk="true" /><BR>
                                <afscme:currentAffiliate /> 
			</TD>
		</TR>
</TABLE>
<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <tr valign="top">
                <td colspan="3"><BR></td>
            </tr>   
            <tr>
                <td align="left"><html:submit styleClass="button"/></td>
                <td align="right">
                    <html:reset styleClass="button"/>&nbsp;
                    <afscme:button action="/viewEmployerInformation.action">Cancel</afscme:button>
                </td>
            </tr>      
            <tr>
                <!-- Required Content -->
                <td colspan="3" align="center"><BR><B><I>* Note: A value can only be entered in either of Employee Salary or Employee Salary Range. Entering a value into one of them will automatically clear the other value.</I></B><BR></td>
            </tr>
</TABLE>	
</html:form>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 

