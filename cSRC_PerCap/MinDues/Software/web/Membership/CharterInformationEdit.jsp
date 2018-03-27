<%! String title = "Charter Information - Edit", help = "CharterInformationEdit.html";%>
<%@ include file="../include/header.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>


<!-- Start Main Content -->
<html:form action="saveCharterInformation.action" focus="name">
    <html:hidden name="charterForm" property="affPk"/>
    <html:hidden name="charterForm" property="status"/>
    <html:hidden name="charterForm" property="reportingCouncilPk"/>
    <html:hidden name="charterForm" property="canAssociateWithCouncil"/>

    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewCharterInformation.action">Cancel</afscme:button>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <afscme:currentAffiliate />
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR>
            <TH colspan="2" align="left">* Charter Name</TH>
        </TR>
        <TR>
            <TD colspan='2'>
                <html:errors property="name"/>
            </TD>
        </TR>
        <TR>

            <TD colspan="2">
                <html:textarea property="name" name="charterForm" onkeyup="validateCharterName(this);" cols="105" rows="3"></html:textarea> 
            </TD>
        </TR>
        <TR>
            <TH colspan="2" align="left">* Charter Jurisdiction</TH>
        </TR>
        <TR>
            <TD colspan='2'>
                <html:errors property="jurisdiction"/>
            </TD>
        </TR>
        <TR>
            <logic:equal name="charterForm" property="affIdType" value="U">        
            <TD colspan="2">
                <html:textarea property="jurisdiction" name="charterForm" onkeyup="validateCharterJurisdiction(this);" cols="105" rows="5" disabled="true"></html:textarea>
            </TD>
            </logic:equal>
            <logic:notEqual name="charterForm" property="affIdType" value="U">
            <TD colspan="2">
                <html:textarea property="jurisdiction" name="charterForm" onkeyup="validateCharterJurisdiction(this);" cols="105" rows="5"></html:textarea>
            </TD>            
            </logic:notEqual>            
        </TR>
        <TR>
            <TH colspan="2" align="left">General Charter Information</TH>
        </TR>
        <TR valign="top">
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TD width="40%" class="ContentHeaderTD">Affiliate Status</TD>
                        <TD width="60%" class="ContentTD">
                            <afscme:codeWrite name="charterForm" property="status" codeType="AffiliateStatus" format="{1}"/>
                        </TD>
                        <html:hidden name="charterForm" property="status" />
                        <html:hidden name="charterForm" property="affIdType" />
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">* Charter Code</TD>
                        <TD class="ContentTD">
                        <logic:equal name="charterForm" property="affIdType" value="U">
                            <html:select property="code" name="charterForm" disabled="true">
                                <afscme:codeOptions useCode="false" codeType="CharterCode" allowNull="true" nullDisplay="" format="{1}" />
                            </html:select>
            		</logic:equal>
            		<logic:notEqual name="charterForm" property="affIdType" value="U">
                           <html:select property="code" name="charterForm">
                                <afscme:codeOptions useCode="false" codeType="CharterCode" allowNull="true" nullDisplay="" format="{1}"/>
                            </html:select>
           		</logic:notEqual>                          
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="code"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">Last Change Effective Date</TD>
                        <TD class="ContentTD">
                       <logic:equal name="charterForm" property="affIdType" value="U">                        
                            <html:select property="lastChangeEffectiveDateMonth" name="charterForm" disabled="true">
                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:text property="lastChangeEffectiveDateYear" name="charterForm" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);" disabled="true"/>
           		</logic:equal>
            		<logic:notEqual name="charterForm" property="affIdType" value="U">
                            <html:select property="lastChangeEffectiveDateMonth" name="charterForm">
                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:text property="lastChangeEffectiveDateYear" name="charterForm" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);"/>            		
          		</logic:notEqual>               		
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">* Charter Date</TD>
                        <TD class="ContentTD">
                       <logic:equal name="charterForm" property="affIdType" value="U">                        
                            <html:select property="dateMonth" name="charterForm" disabled="true">
                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:text property="dateYear" name="charterForm" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);" disabled="true" />
           		</logic:equal>
            		<logic:notEqual name="charterForm" property="affIdType" value="U">                            
                           <html:select property="dateMonth" name="charterForm">
                                <afscme:monthOptions allowNull="true" nullDisplay=""/>
                            </html:select>
                            <html:text property="dateYear" name="charterForm" size="4" maxlength="4" onkeyup="return autoTab(this, 4, event);" />            		
          		</logic:notEqual>              		
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="dateMonth"/>
                        </TD>
                    </TR>
                </TABLE>
            </TD>
            <TD width="50%">
                <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <logic:equal name="charterForm" property="affIdType" value="U">                  
                    <TR>
                        <TD width="40%" class="ContentHeaderTD">County</TD>
                        <TD width="60%" class="ContentTD">
                            <html:text property="county1" name="charterForm" size="25" maxlength="25" disabled="true" /> *
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="county1"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">&nbsp;</TD>
                        <TD class="ContentTD">
                            <html:text property="county2" name="charterForm" size="25" maxlength="25" disabled="true"/>
                        </TD>
                    </TR>
                   <TR>
                        <TD colspan='2'>
                            <html:errors property="county2"/>
                        </TD>
                    </TR>                    
                    <TR>
                        <TD class="ContentHeaderTD">&nbsp;</TD>
                        <TD class="ContentTD">
                            <html:text property="county3" name="charterForm" size="25" maxlength="25" disabled="true" />
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="county3"/>
                        </TD>
                    </TR>                    
          	</logic:equal>
            	<logic:notEqual name="charterForm" property="affIdType" value="U">                    
                    <TR>
                        <TD width="40%" class="ContentHeaderTD">County</TD>
                        <TD width="60%" class="ContentTD">
                            <html:text property="county1" name="charterForm" size="25" maxlength="25"/> *
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="county1"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="ContentHeaderTD">&nbsp;</TD>
                        <TD class="ContentTD">
                            <html:text property="county2" name="charterForm" size="25" maxlength="25"/>
                        </TD>
                    </TR>
	            <TR>
                        <TD colspan='2'>
                            <html:errors property="county2"/>
                        </TD>
                    </TR>                    
                    <TR>
                        <TD class="ContentHeaderTD">&nbsp;</TD>
                        <TD class="ContentTD">
                            <html:text property="county3" name="charterForm" size="25" maxlength="25"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan='2'>
                            <html:errors property="county3"/>
                        </TD>
                    </TR>                    
		</logic:notEqual>            	
                </TABLE>
            </TD>
        </TR>
        <TR>
            <TH colspan="2" align="left">
                Council Affiliations 
            </TH>
        </TR>
        <TR>
            <TD colspan="2">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable" width="100%" align="center">
                    <TR>
                        <TH width="50%">Abbreviated Affiliate Name</TH>
                        <TH width="50%" colspan="5">Affiliate Identifier</TH>
                    </TR>
                    <TR>
                        <TD>&nbsp;</TD>
                        <TH class="small">Type</TH>
                        <TH class="small">Local/Sub Chapter</TH>
                        <TH class="small">State/National Type</TH>
                        <TH class="small">Sub Unit</TH>
                        <TH class="small">Council/Retiree Chapter</TH>
                    </TR>
                    <logic:notPresent name="charterForm" property="councilAffiliations">
                        <TR>
                            <TD align="center">
                                <bean:message key="message.charter.noCouncilAffiliations"/>
                            </TD>
                            <TD colspan="5">&nbsp;</TD>
                        </TR>
                    </logic:notPresent>
                    <logic:present name="charterForm" property="councilAffiliations">
                        <logic:iterate id="council" name="charterForm" property="councilAffiliations" type="org.afscme.enterprise.affiliate.AffiliateData">
                            <TR>
                                <TD align="center">
                                    <bean:write name="council" property="abbreviatedName"/>
                                </TD>
                                <afscme:affiliateIdWrite name="council" property="affiliateId"/>
                            </TR>
                        </logic:iterate>
                    </logic:present>
                </TABLE>
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <afscme:currentAffiliate />
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button"/>
            </TD>
            <TD align="right">
                <BR>
                <html:reset styleClass="button"/>&nbsp;
                <afscme:button action="/viewCharterInformation.action">Cancel</afscme:button>
                <BR> <BR> 
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
<!-- End Main Content -->
<%@ include file="../include/footer.inc" %> 
