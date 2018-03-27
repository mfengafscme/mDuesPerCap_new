<%! String title = "Affiliate Staff Detail", help = "AffiliateStaffDetail.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>
<table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <tr valign="top">
        <td align="left">
            <br>
			
            <afscme:button action='<%= request.getParameter("back") != null? (String)request.getParameter("back") : "/viewStaffMaintainence.action"%>'>Return</afscme:button>


        </td>

        <td align="right">
            <br>

            <afscme:button action="/editAffiliateStaff.action?update=true">Edit</afscme:button>

            <br>

            <br>
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr valign="top">
        <td class="ContentHeaderTR">
            <afscme:currentPersonName />

            <br>

            <afscme:currentAffiliate />

            <br>

            <br>
        </td>
    </tr>
</table>

<table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th width="14%">Prefix</th>

                    <th width="24%">First Name</th>

                    <th width="24%">Middle Name</th>

                    <th width="24%">Last Name</th>

                    <th width="14%">Suffix</th>
                </tr>

                <tr>
                    <td align="center">
                        <afscme:codeWrite name="personData" codeType="Prefix" property="prefixNm" format="{1}" />
                    </td>

                    <td align="center">
                        <bean:write name="personData" property="firstNm" />
                    </td>

                    <td align="center">
                        <bean:write name="personData" property="middleNm" />
                    </td>

                    <td align="center">
                        <bean:write name="personData" property="lastNm" />
                    </td>

                    <td align="center">
                        <afscme:codeWrite name="personData" codeType="Suffix" property="suffixNm" format="{1}" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <td class="ContentHeaderTD">
                        <label for="label_Nickname">Nickname</label>
                    </td>

                    <td class="ContentTD">
                        <bean:write name="personData" property="nickNm" />
                    </td>

                    <td class="ContentHeaderTD">
                        <label for="label_MemberNumber">Staff Number</label>
                    </td>

                    <td class="ContentTD">
				<bean:write name="personData" property="personPk"/>
			  </td>
                </tr>

                <tr>
                    <td class="ContentHeaderTD">
                        <label for="label_AlternateMailingName">Alt Mail Name</label>
                    </td>

                    <td class="ContentTD" colspan="7">
                        <bean:write name="personData" property="altMailingNm" />
                    </td>
                </tr>

                <tr>
                    <td width="15%" class="ContentHeaderTD">
                        <label for="label_SSN">SSN</label>
                    </td>

                    <td width="25%" class="ContentTD">
                        <bean:write name="personData" property="ssn" />
                    </td>

                    <td width="20%" class="ContentHeaderTD">
                        <label for="label_ValidSSN">Valid SSN</label>
                    </td>

                    <td width="10%">
                        <html:checkbox name="personData" property="ssnValid" disabled="true" />
                    </td>

                    <td width="20%" class="ContentHeaderTD">Duplicate SSN</td>

                    <td width="10%">
                         <html:checkbox name="personData" property="ssnDuplicate" disabled="true" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr valign="top">
                    <th width="30%">Staff Title</th>

                    <th width="25%">POC For</th>
                </tr>

                <tr>
                    <td class="ContentTD" align="center">
                        <afscme:codeWrite codeType="AffStaffTitle" name="staffData" property="staffTitlePk" format="{1}" />
                    </td>

                    <td align="center">
                        <afscme:codeWrite codeType="POC" name="staffData" property="pocForPk" format="{1}" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr align="center">
        <td>
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th align="left">Select</th>

                    <th align="left" colspan="6">Locals Serviced</th>
                </tr>

                <tr>
                    <td width="5%">&nbsp;</td>

                    <th width="5%" class="small">Type</th>

                    <th width="10%" class="small">Loc/Sub Chap</th>

                    <th width="10%" class="small">State/Nat'l</th>

                    <th width="10%" class="small">Sub Unit</th>

                    <th width="10%" class="small">CN/Ret Chap</th>

                    <th width="50%" class="small">Affiliate Name</th>
                </tr>

                <logic:iterate name="locals" id="local" type="org.afscme.enterprise.affiliate.AffiliateResult">
                    <tr>
                        <td align="center">
                            <afscme:link page="/removeLocalServiced.action" paramId="localPk" paramName="local" paramProperty="affPk" styleClass="action" confirm="Are you sure you want to delete this Local Serviced?">Delete</afscme:link>
                        </td>

                        <afscme:affiliateIdWrite name="local" property="affiliateId" />

                        <td align="center">
                            <bean:write name="local" property="affAbreviatedNm" />
                        </td>
                    </tr>
                </logic:iterate>

                <tr>		    
                    <td class="ContentHeaderTD">
                    	<logic:notEqual name="affiliateId" property="type" value="S">
                    	<logic:notEqual name="affiliateId" property="type" value="U">
                            <afscme:link page="/viewAddLocalServiced.action" styleClass="action" title="Add a new Local Serviced">Add</afscme:link>
                        </logic:notEqual>
                        </logic:notEqual>
                    </td>
                    	<logic:notEqual name="affiliateId" property="type" value="S">
                    	<logic:notEqual name="affiliateId" property="type" value="U">
                    <td colspan="3">Another Local Serviced</td>

                        </logic:notEqual>
                        </logic:notEqual>

                    <td colspan="3">&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>

    <tr valign="top">
        <td valign="top" colspan="2">
            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <tr>
                    <th colspan="6" align="left">
                        <afscme:link page="/viewLocationSelection.action?back=StaffDetail" styleClass="largeTH" title="Maintain Location Association">Maintain Location Association</afscme:link>
                    </th>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <td>
            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">

            <%if (request.getAttribute("location") == null) { %>
                    <%@ include file="../include/location_noprimary_content.inc" %>
            <% } else { %>

                    <tr>
                        <th colspan="6">Title of Location</th>
                    </tr>

                    <tr>
                        <td class="ContentTD" align="center" colspan="6">
                            <bean:write name="location" property="locationNm" />
                        </td>
                    </tr>

                    <tr valign="top">
                        <td COLSPAN="6">
                            <table width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
<!-- check for any Organization Location Addresses -->
                                <logic:present name="location" property="orgAddressData">
<!-- Organization Location Addresses -->
                                    <logic:iterate id="address" name="location" property="orgAddressData" type="org.afscme.enterprise.organization.OrgAddressRecord">
<!-- Primary Addresses -->
                                        <logic:equal name="address" property="type" value="<%= org.afscme.enterprise.codes.Codes.OrgAddressType.REGULAR.toString() %>">
                                            <tr>
                                                <th align="left" colspan="6">Primary Address</th>
                                            </tr>

                                            <%@ include file="../include/location_address_content.inc" %>
                                        </logic:equal>
                                    </logic:iterate>
                                </logic:present>

<!-- Organization Location Phones -->
                                <tr>
                                    <th align="left" colspan="6">Phone Numbers</th>
                                </tr>

                                <tr>
                                    <th width="15%" class="small">Type</th>

                                    <th width="15%" class="small">Country Code</th>

                                    <th width="15%" class="small">Area Code</th>

                                    <th width="25%" class="small">Number</th>

                                    <th width="10%" class="small">Bad Phone Flag</th>

                                    <th width="20%" class="small">Date&nbsp;Marked&nbsp;Bad</th>
                                </tr>

                                <logic:iterate id="phone" name="location" property="orgPhoneData" type="org.afscme.enterprise.organization.OrgPhoneData">
<!-- Organization Location Office Phone Number -->
                                    <logic:equal name="phone" property="phoneType" value="<%= org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_OFFICE.toString() %>">
                                        <%@ include file="../include/location_phone_content.inc" %>
                                        <logic:equal name="location" property="hasOnlyOfficePhone" value="true">
                                            <tr>
                                                <td align="center">Fax</td>

                                                <td align="center">&nbsp;</td>

                                                <td align="center">&nbsp;</td>

                                                <td align="center">&nbsp;</td>

                                                <td align="center" class="ContentTD">
                                                    <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                                </td>

                                                <td align="center" class="ContentTD">&nbsp;</td>
                                            </tr>
                                        </logic:equal>
                                    </logic:equal>

<!-- Organization Location Fax Phone Number -->
                                    <logic:equal name="phone" property="phoneType" value="<%= org.afscme.enterprise.codes.Codes.OrgPhoneType.LOC_PHONE_FAX.toString() %>">
                                        <logic:equal name="location" property="hasOnlyFaxPhone" value="true">
                                            <tr>
                                                <td align="center">Office</td>

                                                <td align="center">&nbsp;</td>

                                                <td align="center">&nbsp;</td>

                                                <td align="center">&nbsp;</td>

                                                <td align="center" class="ContentTD">
                                                    <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                                </td>

                                                <td align="center" class="ContentTD">&nbsp;</td>
                                            </tr>
                                        </logic:equal>

                                        <%@ include file="../include/location_phone_content.inc" %>
                                    </logic:equal>
                                </logic:iterate>

<!-- Organization Location Phones -->
<!-- if no Organization Location Phones then show empty ones -->
                                <logic:notPresent name="location" property="orgPhoneData">
                                    <tr>
                                        <td align="center">Office</td>

                                        <td align="center">&nbsp;</td>

                                        <td align="center">&nbsp;</td>

                                        <td align="center">&nbsp;</td>

                                        <td align="center" class="ContentTD">
                                            <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                        </td>

                                        <td align="center" class="ContentTD">&nbsp;</td>
                                    </tr>

                                    <tr>
                                        <td align="center">Fax</td>

                                        <td align="center">&nbsp;</td>

                                        <td align="center">&nbsp;</td>

                                        <td align="center">&nbsp;</td>

                                        <td align="center" class="ContentTD">
                                            <input name="phoneBadFlag" type="checkbox" disabled="true" />
                                        </td>

                                        <td align="center" class="ContentTD">&nbsp;</td>
                                    </tr>
                                </logic:notPresent>
                            </table>
                        </td>
                    </tr>
            <% } %> <%-- location --%>

                <tr>
                    <td>
<!-- Display Person Email Info -->
                        <table width="100%" cellpadding="0" cellspacing="0" border="1" class="InnerContentTable">
                            <tr>
                                <th colspan="2" align="left">
                                    <afscme:link page="/viewEmailAddresses.action?back=StaffDetail" paramId="personPk" paramName="personData" paramProperty="personPk" styleClass="TH">Maintain Email Addresses</afscme:link>
                                </th>
                            </tr>

                            <tr>
                                <th class="small" align="left" width="15%">Type</th>

                                <th class="small" align="left">Email Address</th>
                            </tr>

                            <tr>
                                <td colspan="2">
                                    <table width="100%">
                                        <logic:iterate name="emails" id="emailData" type="org.afscme.enterprise.person.EmailData">
                                            <tr>
                                                <td width="15%">
                                                    <afscme:codeWrite name="emailData" property="emailType" codeType="EmailType" format="{1}" />
                                                </td>

                                                <td>
                                                <bean:write name="emailData" property="personEmailAddr" />

                                                &nbsp;</td>
                                            </tr>
                                        </logic:iterate>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <table cellpadding="1" cellspacing="1" border="1" class="BodyContent">
                    <logic:present name="comment">
                        <tr>
                            <th align="left">
                                <afscme:link page="/viewStaffCommentHistory.action" styleClass="TH">View More Comments</afscme:link>
                            </th>
                        </tr>

                        <tr>
                            <td>
<pre>
<bean:write name="comment" />




</pre>
                            </td>
                        </tr>
                    </logic:present>

<!-- if no Comment then show message -->
                    <logic:notPresent name="comment">
                        <tr>
                            <th align="left" colspan="6">No Comments</th>
                        </tr>
                    </logic:notPresent>
                </table>

                <table cellpadding="0" cellspacing="0" border="0" class="BodyContent" align="center">
                    <tr>
                        <td>
                            <table cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                                <tr>
<%--								
                                    <td class="ContentHeaderTD">Date Created</td>

                                    <td class="ContentTD">
                                        <afscme:dateWrite name="staffData" property="recordData.createdDate" />
                                    </td>

                                    <td class="ContentHeaderTD">
                                        <LABEL for="recordData.createdBy">Created By</LABEL>
                                    </td>

                                    <td class="ContentTD">
                                        <afscme:userWrite name="staffData" property="recordData.createdBy" />
                                    </td>
--%>
                                    <td class="ContentHeaderTD">
                                        <LABEL for="recordDate.modifiedDate">Last Updated</LABEL>
                                    </td>

                                    <td class="ContentTD">
                                        <afscme:dateWrite name="staffData" property="recordData.modifiedDate" />
                                    </td>

                                    <td class="ContentHeaderTD">
                                        <LABEL for="recordData.createdBy">Updated By</LABEL>
                                    </td>

                                    <td class="ContentTD">
                                        <afscme:userWrite name="staffData" property="recordData.modifiedBy" />
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <table cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
                    <tr>
                        <td class="ContentHeaderTR">
                            <br>

                            <afscme:currentPersonName />

                            <br>

                            <afscme:currentAffiliate />
                        </td>
                    </tr>
                </table>

            </table>
                <table cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
                    <tr valign="top">
                        <td align="left">
                            <br>
                            <afscme:button action='<%=(String)request.getAttribute("back")%>'>Return</afscme:button>
                        </td>

                        <td align="right">
                            <br>

                            <afscme:button action="/editAffiliateStaff.action?update=true">Edit</afscme:button>

                            <br>

                            <br>
                        </td>
                    </tr>
                </table>

                <%@ include file="../include/footer.inc" %>
        </td>
    </tr>
</table>

