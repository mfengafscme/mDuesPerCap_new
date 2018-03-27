<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<%!String title = "Address Maintenance", help = "AddressMaintenance.html";%>
<bean:define name="addressMaintainence" id="addressMaintainence" type="org.afscme.enterprise.address.web.AddressMaintainence"/>
<bean:define id="returnAction" name="addressMaintainence" property="returnAction" />
<%@ include file="../include/header.inc" %>

	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			<TD align="left">
				<BR> <input class="BUTTON" type="button" value="Return" onclick="window.location='<%=returnAction%>'">
                                 <BR><BR>
                        </TD>
                        <logic:notPresent name="addressMaintainence" property="vduFlag">    
                            <TD align="right">
				<BR> <afscme:button page="/editPersonAddress.action" paramProperty="back" paramId="back" paramName="addressMaintainence">Add</afscme:button>
				<BR> <BR> 
			   </TD>
                       </logic:notPresent>    
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR valign="top">
			<TD class="ContentHeaderTR">
				<afscme:currentPersonName/><BR><BR> 
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <logic:present name="addressMaintainence" property="systemAddress">
		<TR>
			<TD class="ContentHeaderTD" colspan="2">
				System Mailing Address 
			</TD>
		</TR>
		<TR valign="top">
			<TD class="ContentHeaderTD">
				&nbsp;
			</TD>
			<TD>
				<TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                        <bean:define id="systemAddress" name="addressMaintainence" property="systemAddress" type="org.afscme.enterprise.address.PersonAddressRecord"/>
                        <%@ include file="../include/system_address_content.inc" %>
				</TABLE>
			</TD>
		</TR>
        </logic:present>

    <logic:iterate name="addressMaintainence" property="departments" type="java.lang.Integer" id="deptPk">

		<TR>
			<TD class="ContentHeaderTD" colspan="2">
				<afscme:codeWrite codeType="Department" pk="<%=deptPk%>" format="{1}"/> Department
			</TD>
		</TR>

        <%String addrProp = "departmentAddresses["+deptPk+"]";%>
        <logic:iterate name="addressMaintainence" property="<%=addrProp%>" type="org.afscme.enterprise.address.PersonAddressRecord" id="personAddress">

            <%@ include file="../include/person_address_content.inc" %>

        </logic:iterate>

    </logic:iterate>

    </table>
	<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
		<TR>
			<TD class="ContentHeaderTR">
				<BR><afscme:currentPersonName/>
			</TD>
		</TR>
	</TABLE>
	<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
		<TR valign="top">
			
                        
                        <TD align="left">
				<BR> <input class="BUTTON" type="button" value="Return" onclick="window.location='<%=returnAction%>'">
			</TD>
                         
			<logic:notPresent name="addressMaintainence" property="vduFlag">
                           <TD align="right">
				<BR> <afscme:button page="/editPersonAddress.action" paramProperty="back" paramId="back" paramName="addressMaintainence">Add</afscme:button>
                                <BR> <BR> 
                           </TD>      
                        </logic:notPresent>  
                           
		</TR>
	</TABLE>


<%@ include file="../include/footer.inc" %>
