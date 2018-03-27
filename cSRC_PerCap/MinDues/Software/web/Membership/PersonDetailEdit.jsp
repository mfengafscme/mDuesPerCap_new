<%@ page import="org.afscme.enterprise.person.Persona"%>
<%! String title = "Person Detail - Edit", help = "PersonDetailEdit.html";%>
<!-- Header -->
<%@ include file="../include/header.inc" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<html:form action="savePersonDetail.action" focus="firstNm">
    <html:hidden property="personPk"/>
    <html:hidden property="ssn"/>
    <html:hidden property="prevAction" />
    <html:hidden property="previousSsn" />
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
                <afscme:button action="/viewPersonDetail.action">Cancel</afscme:button>
           </td>
        </tr>      
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
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

    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
<!-- Display Person Name Info -->
        <TR align="center">
            <TD>
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH width="14%">Prefix</TH>
                        <TH width="24%">* First Name</TH>
                        <TH width="24%">Middle Name</TH>
                        <TH width="24%">* Last Name</TH>
                        <TH width="14%">Suffix</TH>
                    </TR>
                    <TR>
                        <TD align="center" class="ContentTD">
                            <html:select property="prefixNm">
                                <afscme:codeOptions codeType="Prefix" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text name="personDetailForm" property="firstNm" size="25" maxlength="25"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text name="personDetailForm" property="middleNm" size="25" maxlength="25"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text name="personDetailForm" property="lastNm" size="25" maxlength="25"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="suffixNm">
                                <afscme:codeOptions codeType="Suffix" format="{1}" allowNull="true" nullDisplay="[Select]"/>
                            </html:select>
                        </TD>
                    </TR>
<!-- Display validation errors -->		
                    <TR>
                        <TD></TD>
                        <TD align="center"><html:errors property="firstNm"/></TD>
                        <TD></TD>
                        <TD align="center"><html:errors property="lastNm"/></TD>
                        <TD></TD>
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
                            <html:text name="personDetailForm" property="nickNm" size="25" maxlength="25"/>
                        </TD>
                        <TD width="5%" class="ContentHeaderTD">SSN</TD>
                        <TD width="20%" class="ContentTD">
                            <html:text name="personDetailForm" property="ssn1" size="3" maxlength="3" onkeyup="return autoTab(this, 3, event);"/>-<html:text name="personDetailForm" property="ssn2" size="2" maxlength="2" onkeyup="return autoTab(this, 2, event);"/>-<html:text name="personDetailForm" property="ssn3" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>
                        </TD>
                        <TD width="15%" class="ContentHeaderTD">Valid SSN</TD>
                        <TD width="5%">
                            <html:checkbox name="personDetailForm" property="ssnValid"/>
                        </TD>
                        <TD width="15%" class="ContentHeaderTD">Duplicate SSN</TD>
                        <TD width="5%">
                            <html:checkbox name="personDetailForm" property="ssnDuplicate" disabled="true"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">
                            <LABEL for="label_AlternateMailingName">Alt Mail Name</LABEL>
                        </TD>
                        <TD class="ContentTD" colspan="7">
                            <html:text name="personDetailForm" property="altMailingNm" size="130" maxlength="130"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD CLASS="ContentHeaderTD">Marked for Deletion</TD>
                        <TD CLASS="ContentTD">
                            <html:checkbox name="personDetailForm" property="markedForDeletionFg"/>
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
                        <TH align="left" colspan="6">Maintain Addresses</TH>
                    </TR>
    <!-- if no Primary Address then show message -->
                    <logic:notPresent name="personDetailForm" property="personAddressRecord">
                        <TR>
                            <TH align="left" colspan="6">No Primary Address</TH>
                        </TR>
                    </logic:notPresent>
    <!-- check for a Primary Address -->            
                    <logic:present name="personDetailForm" property="personAddressRecord">
                        <bean:define name="personDetailForm" id="address" property ="personAddressRecord" type="org.afscme.enterprise.address.PersonAddressRecord"/>
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
                                    <TH colspan="4" align="left">Maintain Phone Numbers</TH>
                                </TR>
                                <TR>
                                    <TH align="left" width="20%" class="small">Type</TH>
                                    <TH align="left" width="25%" class="small">Country Code</TH>
                                    <TH align="left" width="20%" class="small">Area Code</TH>
                                    <TH align="left" width="35%" class="small">Number</TH>
                                </TR>
    <!-- check for any Phone Numbers -->            
                                <logic:present name="personDetailForm" property="phoneData">
                                     <logic:iterate name="personDetailForm" property="phoneData" id="phoneData" type="org.afscme.enterprise.common.PhoneData">
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
                                    <TH colspan="2" align="left">Maintain Email Addresses</TH>
                                </TR>
    <!-- check for any Email Addresses -->            
                                <logic:present name="personDetailForm" property="emailData">
                                    <logic:iterate name="personDetailForm" property="emailData" id="emailData" type="org.afscme.enterprise.person.EmailData">
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
                    <TR>
                        <TH align="left">Enter New Comments</TH>
                    </TR>
                    <TR>
                        <TD>
                            <html:textarea name="personDetailForm" property="comment" onkeyup="validateComments(this);" cols="105" rows="3"></html:textarea>
                        </TD>
                    </TR>
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
<!-- Display buttons -->		
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <tr valign="top">
            <td colspan="3"><BR></td>
        </tr>   
        <tr>
            <td align="left"><html:submit styleClass="button"/></td>
            <td align="right">
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewPersonDetail.action">Cancel</afscme:button>
           </td>
        </tr>      
        <tr>
            <td colspan="3" align="center"><BR><B><I>* Indicates Required Fields</I></B><BR></td>
        </tr>
    </TABLE>
</html:form>

<!-- Footer -->
<%@ include file="../include/footer.inc" %> 