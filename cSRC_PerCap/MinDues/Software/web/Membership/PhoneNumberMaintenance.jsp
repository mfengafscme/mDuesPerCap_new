<%!String title = "Phone Number Maintenance", help = "PhoneNumberMaintenance.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<!-- Form -->
<%String rtnDelete = "/deletePhoneNumber.action?back="+request.getAttribute("back");%>
<%String rtnEdit = "/editPhoneNumber.action?back="+request.getAttribute("back");%>
<bean:define name="phoneNumber" id="phoneNumber" type="org.afscme.enterprise.person.web.PhoneNumberForm"/>
<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <bean:define id="action" name="phoneNumber" property="returnAction" />
    <TR valign="top">
        <TD align="left">
             <BR> <afscme:button action="<%=action.toString()%>">Return</afscme:button>
             <BR><BR>
        </TD>
        <logic:notPresent name="phoneNumber" property="vduFlag">  
           <TD align="right">
              <BR> <afscme:button page="<%=rtnEdit%>">Add</afscme:button>
              <BR> <BR> 
          </TD>
       </logic:notPresent>
    
    </TR>

</TABLE>
<!-- Set the person header -->		
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentPersonName />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<!-- Display phone numbers -->	
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <!-- if no Phone Numbers then show message -->
    <logic:notPresent name="phoneNumber" property="departments">
            <tr>
                <th align="center">No phone numbers available</th>
            </tr>
    </logic:notPresent>
    <!-- check for any Phone Numbers -->            
    <logic:present name="phoneNumber" property="departments">
        <logic:iterate name="phoneNumber" property="departments" type="java.lang.Integer" id="deptPk">
            <TR>
                <TD class="ContentHeaderTD" colspan="2">
                        <afscme:codeWrite codeType="Department" pk="<%=deptPk%>" format="{1}"/> Department
                </TD>
            </TR>   
           <%String phoneProp = "departmentPhoneNumbers["+deptPk+"]";%>
            <logic:iterate name="phoneNumber" id="phoneData" property="<%=phoneProp%>" type="org.afscme.enterprise.common.PhoneData">
                <TR valign="top">
                    <TD width="8%">
                        <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <TR>
                                <TD class="ContentHeaderTD">
                                    <%if (phoneNumber.isEditable(phoneData)) { %>
                                        <afscme:link page="<%=rtnEdit%>" paramName="phoneData" paramProperty="phonePk" paramId="phonePk" title="Edit this Phone Number">Edit</afscme:link> 
                                    <% } else { %>
                                        &nbsp;
                                    <% }  %>
                                </TD>
                                <TD class="ContentHeaderTD">
                                    <%if (phoneNumber.isDeletable(phoneData)) { %>
                                        <afscme:link page="<%=rtnDelete%>" paramName="phoneData" paramProperty="phonePk" paramId="phonePk" title="Delete this Phone Number" confirm="Are you sure you want to delete this Phone Number?">Delete</afscme:link> 
                                    <% } else { %>
                                        &nbsp;
                                    <% }  %>
                                </TD>
                            </TR>
                        </TABLE>
                    </TD>
                    <TD>
                        <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <TR>
                                <TH width="15%">Primary</TH>
                                <TH width="25%">Type</TH>
                                <TH width="15%">Private</TH>
                                <TH width="15%">DO NOT CALL</TH>
                                <TH width="15%">Bad</TH>
                                <TH width="15%">Date Marked Bad</TH>
                            </TR>
                            <TR>
                                <TD class="ContentTD" align="center">
                                    <html:checkbox name="phoneData" property="phonePrmryFg" disabled="true"/>
                                </TD>
                                <TD class="ContentTD" align="center">
                                    <afscme:codeWrite name="phoneData" property="phoneType" codeType="PhoneType"/>
                                </TD>
                                <TD class="ContentTD" align="center">
                                    <html:checkbox name="phoneData" property="phonePrivateFg" disabled="true"/>
                                </TD>
                                <TD class="ContentTD" align="center">
                                    <html:checkbox name="phoneData" property="phoneDoNotCallFg" disabled="true"/>
                                </TD>
                                <TD class="ContentTD" align="center">
                                    <html:checkbox name="phoneData" property="phoneBadFlag" disabled="true"/>
                                </TD>
                                <TD class="ContentTD" align="center">
                                    <afscme:dateWrite name="phoneData" property="phoneBadDate"/>
                                </TD>
                            </TR>
                            <TR>
                                <TH COLSPAN="2">Country Code</TH>
                                <TH COLSPAN="2">Area Code</TH>
                                <TH COLSPAN="2">Number</TH>
                            </TR>
                            <TR>
                                <TD class="ContentTD" align="center" colspan="2">
                                    <bean:write name="phoneData" property="countryCode"/>
                                </TD>
                                <TD class="ContentTD" align="center" colspan="2">
                                    <bean:write name="phoneData" property="areaCode"/>
                                </TD>
                                <TD class="ContentTD" align="center" colspan="2">
                                    <bean:write name="phoneData" property="phoneNumber"/>
                                </TD>
                            </TR>
                            <TR>
                                <TD colspan="6">
                                    <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                        <TR>
                                            <TD colspan="8">
                                                <HR>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD width="15%" class="ContentHeaderTD">
                                                Date Created
                                            </TD>
                                            <TD width="11%" class="ContentTD">
                                                <afscme:dateWrite name="phoneData" property="theRecordData.createdDate"/>
                                            </TD>
                                            <TD width="12%" class="ContentHeaderTD">
                                                <LABEL for="createdBy">Created By</LABEL>
                                            </TD>
                                            <TD width="9%" class="ContentTD">
                                                <afscme:userWrite name="phoneData" property="theRecordData.createdBy"/>
                                            </TD>
                                            <TD width="14%" class="ContentHeaderTD">
                                                <LABEL for="theRecordData.modifiedDate">Last Updated</LABEL>
                                            </TD>
                                            <TD width="11%" class="ContentTD">
                                                <afscme:dateWrite name="phoneData" property="theRecordData.modifiedDate"/>
                                            </TD>
                                            <TD width="13%" class="ContentHeaderTD">
                                                <LABEL for="theRecordData.modifiedBy">Updated By</LABEL>
                                            </TD>
                                            <TD width="15%" class="ContentTD">
                                                <afscme:userWrite name="phoneData" property="theRecordData.modifiedBy"/>
                                            </TD>
                                        </TR>
                                    </TABLE>
                                </TD>
                            </TR>
                        </TABLE>
                    </TD>
                </TR>
            </logic:iterate>
        </logic:iterate>
    </logic:present>
</table>
<!-- Set the person footer -->		
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR">
            <BR><afscme:currentPersonName/>
        </TD>
    </TR>
</TABLE>
<!-- Display buttons -->		
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD align="left">
             <BR> <afscme:button action="<%=action.toString()%>">Return</afscme:button>
        </TD>
        <logic:notPresent name="phoneNumber" property="vduFlag">  
           <TD align="right">
            <BR> <afscme:button page="<%= rtnEdit %>">Add</afscme:button>
            <BR> <BR> 
          </TD>
        </logic:notPresent>
    </TR>
</TABLE>


<%@ include file="../include/footer.inc" %>
