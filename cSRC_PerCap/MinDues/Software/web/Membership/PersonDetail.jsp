<%@ page import="org.afscme.enterprise.person.Persona"%>
<%! String title = "Person Detail", help = "PersonDetail.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<bean:define name="personDetail" id="personDetail" type="org.afscme.enterprise.person.web.PersonDetailForm"/>
<bean:define name="personDetail" id="personPk" property="personPk"/>

<!-- Tabs -->
<bean:define id="screen" value="PersonDetail"/>
<%@ include file="../include/person_tab.inc" %>

<!-- Set the person header -->		
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <afscme:currentPersonName />
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>

<!-- Display Persona Info -->
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD>
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR valign="top">
                    <TD class="ContentHeaderTD" width="8%">
                        Persona
                    </TD>
                    <TD class="ContentTD">
                        <!-- check for any Persona -->            
                        <logic:present name="personDetail" property="persona">
                            <logic:iterate name="personDetail" property="persona" id="persona">
                                <logic:equal name="persona" value="<%=Persona.AFSCME_STAFF%>">
                                    <%=Persona.AFSCME_STAFF%>
                                    <BR>
                                </logic:equal>
                                <logic:equal name="persona" value="<%=Persona.MEMBER%>">
                                    <afscme:link page="/viewMemberDetail.action" paramId="personPk" paramName="personDetail" paramProperty="personPk" styleClass="action">
                                        <%=Persona.MEMBER%>
                                    </afscme:link><BR>
                                </logic:equal>
                                <logic:equal name="persona" value="<%=Persona.AFFILIATE_STAFF%>">
                                    <afscme:link page="/viewPersonStaff.action" styleClass="action">
                                        <%=Persona.AFFILIATE_STAFF%>
                                    </afscme:link><BR>
                                </logic:equal>
                                <logic:equal name="persona" value="<%=Persona.ORGANIZATION_ASSOCIATE%>">
                                    <afscme:link page="/viewPersonOrgAssociateList.action" styleClass="action">
                                        <%=Persona.ORGANIZATION_ASSOCIATE%>
                                    </afscme:link><BR>
                                </logic:equal>
                                <logic:equal name="persona" value="<%=Persona.VENDOR%>">
                                    <%=Persona.VENDOR%><BR>
                                </logic:equal>
                            </logic:iterate>
                        </logic:present>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
<!-- Display Person Name Info -->
    <TR align="center">
        <TD>
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH width="14%">Prefix</TH>
                    <TH width="24%">First Name</TH>
                    <TH width="24%">Middle Name</TH>
                    <TH width="24%">Last Name</TH>
                    <TH width="14%">Suffix</TH>
                </TR>
                <TR>
                    <TD align="center" class="ContentTD">
                        <afscme:codeWrite name="personDetail" property="prefixNm" codeType="Prefix" format="{1}"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <bean:write name="personDetail" scope="request" property="firstNm"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <bean:write name="personDetail" scope="request" property="middleNm"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <bean:write name="personDetail" scope="request" property="lastNm"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <afscme:codeWrite name="personDetail" property="suffixNm" codeType="Suffix" format="{1}"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
<!-- Display Person Detail Info -->
    <TR align="center">
        <TD>
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TD width="15%" class="ContentHeaderTD">Nickname</TD>
                    <TD width="20%" class="ContentTD">
                        <bean:write name="personDetail" scope="request" property="nickNm"/>
                    </TD>
                    <TD width="5%" class="ContentHeaderTD">SSN</TD>
                    <TD width="20%" class="ContentTD">
                        <logic:present name="personDetail" property="ssn">
                            <bean:write name="personDetail" scope="request" property="ssn1"/>-<bean:write name="personDetail" scope="request" property="ssn2"/>-<bean:write name="personDetail" scope="request" property="ssn3"/>
                        </logic:present>
                    </TD>
                    <TD width="15%" class="ContentHeaderTD">Valid SSN</TD>
                    <TD width="5%">
                        <html:checkbox property="ssnValid" name="personDetail" disabled="true"/>
                    </TD>
                    <TD width="15%" class="ContentHeaderTD">Duplicate SSN</TD>
                    <TD width="5%">
                        <html:checkbox property="ssnDuplicate" name="personDetail" disabled="true"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="ContentHeaderTD">
                        <LABEL for="label_AlternateMailingName">Alt Mail Name</LABEL>
                    </TD>
                    <TD class="ContentTD" colspan="7">
                        <bean:write name="personDetail" scope="request" property="altMailingNm"/>
                    </TD>
                </TR>
                <TR>
                    <TD CLASS="ContentHeaderTD">Marked for Deletion</TD>
                    <TD CLASS="ContentTD">
                        <html:checkbox property="markedForDeletionFg" name="personDetail" disabled="true"/>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
<!-- Display Person System Mailing Address Info -->
    <TR align="center">
        <TD>
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH align="left" colspan="6">
                        <afscme:link page="/viewAddressMaintainence.action?back=PersonDetail" paramId="personPk" paramName="personDetail" paramProperty="personPk" styleClass="TH">Maintain Addresses</afscme:link>
                    </TH>
                </TR>
    <!-- if no Primary Address then show message -->
                <logic:notPresent name="personDetail" property="personAddressRecord">
                    <TR>
                        <TH align="left" colspan="6">No Primary Address</TH>
                    </TR>
                </logic:notPresent>
    <!-- check for a Primary Address -->            
                <logic:present name="personDetail" property="personAddressRecord">
                    <bean:define id="address" name="personDetail" property ="personAddressRecord" type="org.afscme.enterprise.address.PersonAddressRecord"/>
                    <TR>
                        <TH align="left" colspan="6">Primary Address</TH>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Address 1</TD>
                        <TD class="ContentTD"><bean:write name="address" property="addr1"/></TD>
                        <TD class="ContentHeaderTD">Address 2</TD>
                        <TD class="ContentTD" colspan="3"><bean:write name="address" property="addr2"/></TD>
                    </TR>
                    <TR>
                        <TD width="11%" class="ContentHeaderTD">City</TD>
                        <TD width="29%"><bean:write name="address" property="city"/></TD>
                        <TD width="15%" class="ContentHeaderTD">State</TD>
                        <TD width="25%"><afscme:codeWrite name="address" property="state" codeType="State" useCode="true" format="{0}"/></TD>
                        <TD width="10%" class="ContentHeaderTD">Zip/Postal Code</TD>
                        <TD width="15%">
                            <logic:notEqual name="address" property="zipCode" value="">
                                <bean:write name="address" property="zipCode"/>
                            </logic:notEqual>
                            <logic:notEqual name="address" property="zipPlus" value="">
                                -<bean:write name="address" property="zipPlus"/>
                            </logic:notEqual>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">County</TD>
                        <TD class="ContentTD"><bean:write name="address" property="county"/></TD>
                        <TD class="ContentHeaderTD">Province</TD>
                        <TD class="ContentTD"><bean:write name="address" property="province"/></TD>
                        <TD class="ContentHeaderTD">Country</TD>
                        <TD class="ContentTD"><afscme:codeWrite name="address" property="countryPk" codeType="Country" format="{1}"/></TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Bad Address</TD>
                        <TD class="ContentTD"><html:checkbox property="bad" name="address" disabled="true"/></TD>
                        <TD class="ContentHeaderTD">Date Marked Bad</TD>
                        <TD class="ContentTD" colspan="3"><afscme:dateWrite name="address" property="badDate"/></TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Last Updated</TD>
                        <TD class="ContentTD"><afscme:dateWrite name="address" property="recordData.modifiedDate"/></TD>
                        <TD class="ContentHeaderTD">Updated By</TD>
                        <TD class="ContentTD" colspan="3"><afscme:userWrite name="address" property="recordData.modifiedBy"/></TD>
                   </TR>
                </logic:present>
             </TABLE>
        </TD>
    </TR>
<!-- Display Person Phone Numbers -->
    <TR align="center">
        <TD>
            <TABLE cellpadding="0" cellspacing="0" border="0" class="InnerContentTable">
                <TR>
                    <TD width="35%" valign="top">
                        <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                            <TR>
                                <TH colspan="4" align="left">
                                    <afscme:link page="/viewPhoneNumberInformation.action?back=PersonDetail" paramId="personPk" paramName="personDetail" paramProperty="personPk" styleClass="TH">Maintain Phone Numbers</afscme:link>
                                </TH>
                            </TR>
                            <TR>
                                <TH align="left" width="20%" class="small">Type</TH>
                                <TH align="left" width="25%" class="small">Country Code</TH>
                                <TH align="left" width="20%" class="small">Area Code</TH>
                                <TH align="left" width="35%" class="small">Number</TH>
                            </TR>
    <!-- check for any Phone Numbers -->            
                            <logic:present name="personDetail" property="phoneData">
                                 <logic:iterate name="personDetail" property="phoneData" id="phoneData" type="org.afscme.enterprise.common.PhoneData">
                                    <TR>
                                        <TD><afscme:codeWrite name="phoneData" property="phoneType" codeType="PhoneType" format="{1}"/></TD>
                                        <TD><bean:write name="phoneData" property="countryCode"/></TD>
                                        <TD><bean:write name="phoneData" property="areaCode"/></TD>
                                        <TD><bean:write name="phoneData" property="phoneNumber"/></TD>
                                    </TR>
                                </logic:iterate>
                            </logic:present>
                        </TABLE>
                    </TD>
<!-- Display Person Email Info -->
                    <TD width="65%" valign="top">
                        <TABLE cellpadding="2" cellspacing="1" border="0" class="InnerContentTable">
                            <TR>
                                <TH colspan="2" align="left">
                                    <afscme:link page="/viewEmailAddresses.action?back=PersonDetail" paramId="personPk" paramName="personDetail" paramProperty="personPk" styleClass="TH">Maintain Email Addresses</afscme:link>
                                </TH>
                            </TR>
    <!-- check for any Email Addresses -->            
                            <logic:present name="personDetail" property="emailData">
                                <logic:iterate name="personDetail" property="emailData" id="emailData" type="org.afscme.enterprise.person.EmailData">
                                    <TR>
                                        <TD  width="20%">
                                            <afscme:codeWrite name="emailData" property="emailType" codeType="EmailType" format="{1}"/>
                                        </TD>
                                        <TD width="80%">
                                            <bean:write name="emailData" property="personEmailAddr"/>
                                        </TD>
                                    </TR>
                                </logic:iterate>
                            </logic:present>
                        </TABLE>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
<!-- Display Person Comment -->
    <TR align="center">
        <TD>
            <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
    <!-- check for Comment -->            
                <logic:present name="personDetail" property="comment">
                    <TR>
                        <TH align="left">
                            <afscme:link page="/viewCommentHistory.action" paramId="personPk" paramName="personDetail" paramProperty="personPk" styleClass="TH">View More Comments</afscme:link>
                        </TH>
                    </TR>
                    <TR>
                        <TD>
                            <bean:write name="personDetail" scope="request" property="comment"/>
                        </TD>
                    </TR>
                </logic:present>
    <!-- if no Comment then show message -->
                <logic:notPresent name="personDetail" property="comment">
                    <tr>
                        <th align="left" colspan="6">No Comments</th>
                    </tr>
                 </logic:notPresent>
            </TABLE>
        </TD>
    </TR>
</TABLE>

<!-- Set the person footer -->		
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR">
            <BR>
            <afscme:currentPersonName />
        </TD>
    </TR>
</TABLE>

<!-- Tabs -->
<%@ include file="/include/person_tab.inc" %>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 
