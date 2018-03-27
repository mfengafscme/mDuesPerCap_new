<%! String title = "Basic Employer Search", help = "BasicAffiliateSearch.html";%>
<%@ include file="../include/minimumduesheader.inc" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<script type="text/javascript" language="JavaScript" src="js/mdues_basicSearch_form.js"></script>

<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<html:form action="searchPreAffiliate.action">
    <input type="hidden" name="reset">
    <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContentNoWidth" width="80%" align="center">
        <TR>
            <TD class="ContentHeaderTR">
                Fields to Search: <BR>
                &nbsp; 
            </TD>
        </TR>
        <TR valign="top">
            <TD class="ContentHeaderTR">
                <TABLE cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                    <TR>
                        <TH colspan="6" align="left">Employer Identifier</TH>
                    </TR>
                    <TR>
                        <!-- <TH width="14%">Type</TH> -->
                        <TH width="14%">Local</TH>
                        <TH width="20%">State</TH>
                        <TH width="14%">Sub Unit</TH>
                        <TH width="29%" colspan="2">Council</TH>
                    </TR>
                    <TR>
                        <!-- <TD align="center" class="ContentTD">
				<!-- <html:select property="affIdType" name="affiliateSearchForm" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form);">  -->
				<!-- <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/> -->
				<!--  </html:select> -->
				<!-- <html:select property="affIdType" value="" name="affiliateSearchForm" onblur="lockAffiliateIDFields(this.form);" onchange="lockAffiliateIDFields(this.form);"> -->
				<!-- <html:option value=""></html:option> -->
			<!--	<html:option value="L">L</html:option> -->
			<!--	<html:option value="U">U</html:option> -->
			<!--	<html:option value="C">C</html:option> -->
			<!--	</html:select> -->
	
                        <!-- </TD> -->
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdLocal" onblur="checkNumeric(this,-999999999,999999999,',','.','');" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:select property="affIdState" name="affiliateSearchForm">
                                <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <html:text property="affIdSubUnit" name="affiliateSearchForm" size="10" maxlength="10"/>
                        </TD>
                        <TD align="center" class="ContentTD" colspan="2">
                            <html:text property="affIdCouncil" onblur="checkNumeric(this,-999999999,999999999,',','.','');" name="affiliateSearchForm" size="4" maxlength="4"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD colspan="6">
                            <HR>
                        </TD>
                    </TR>
					
					<TR>
                        <TD colspan="6" class="ContentHeaderTD">
                            Employer Name (Contains): &nbsp;&nbsp;&nbsp;&nbsp; <html:text property="employerNm" name="affiliateSearchForm" size="100" maxlength="150"/>
                        </TD>
                    </TR>					
                    
                </TABLE>
            </TD>
        </TR>
    </TABLE><br><br>
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
        <TR valign="top">
            <TD align="left">
                <BR>
                <html:submit styleClass="button" onclick="return checkAllFieldsBeforeSubmit();" />
            </TD>
            <TD align="right">
                <BR> 
                <%-- <afscme:button action="/viewPowerAffiliateCriteria.action">Power Search</afscme:button>  --%>
                <html:reset styleClass="button"/>
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</html:form>

<%@ include file="../include/footer.inc" %> 
