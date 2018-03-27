<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.afscme.enterprise.update.Codes.UpdateFileStatus, org.afscme.enterprise.update.Codes.Sort, org.afscme.enterprise.update.Codes.UpdateFileType" %>
<%! String title = "Apply Update Process Log", help = "ApplyUpdateProcessLog.html";%>

<%@ include file="../include/header.inc" %> 


<html:form  action="/viewApplyUpdateSearchLog.action" method="post" enctype="multipart/form-data" method="post" styleId="form" focus="fromDate">
<bean:define id="form" name="searchLogForm" type="org.afscme.enterprise.update.web.SearchLogForm"/>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="80%" align="center">
    <TR valign="top">
        <TD align="center">
            <html:errors/><BR>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTableNoWidth" width="100%" align="center">
    <TR valign="top">
       
        <TD align="left">
            <BR> 
            <afscme:button action="/viewApplyUpdateQueue.action" >Return</afscme:button>

            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD colspan="2" class="ContentHeaderTR">
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR>
                    <TH colspan="2">
                            Processed 
                    </TH>
                    
                    <TH colspan="5" align="left">
                        Affiliate Identifier - <afscme:affiliateFinder formName="searchLogForm" affIdTypeParam="affType" affIdCouncilParam="affCouncil" affIdLocalParam="affLocal" affIdStateParam="affState" affIdSubUnitParam="affSubunit" affIdCodeParam="affCode"/>
                    </TH>
                                       
                    <TH>
                            Update Type 
                    </TH>
                </TR>
                <TR>
                    <TH class="small">
                            From Date 
                    </TH>
                    <TH class="small">
                            To Date 
                    </TH>
                    <TH class="small">
                            Type 
                    </TH>
                    <TH class="small">
                            Loc/Sub Chap 
                    </TH>
                    <TH class="small">
                            State/Nat'l 
                    </TH>
                    <TH class="small">
                            Sub Unit 
                    </TH>
                    <TH class="small">
                            CN/Ret Chap 
                    </TH>
                    <TD>&nbsp;
                    </TD>
                </TR>
                <TR>
                    <TD align="center">
                            <html:text property="fromDate" name="searchLogForm" styleId="validDate" size="10" maxlength="10"/>
                                <a href="javascript:show_calendar('form.fromDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"/></a>
                    </TD>
                    <TD align="center">
                            <html:text property="toDate" name="searchLogForm" styleId="validDate" size="10" maxlength="10"/> 
                                <a href="javascript:show_calendar('form.toDate');" onMouseOver="window.status='Date Picker';return true;" onMouseOut="window.status='';return true;"><IMG src="../images/calendar.gif" width="24" height="22" border="0" alt="Calendar"/></a>
                    </TD>
                    <TD align="center" class="ContentTD">
                            <html:select property="affType" name="searchLogForm" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateType" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select> 
                    </TD>
                    <TD align="center" class="ContentTD">
                            <html:text      property="affLocal" name="searchLogForm" size="4" maxlength="4" onchange="clearHiddenFields(this.form);"/> 
                            <html:hidden      property="sort"/> 
                             
                    </TD>
                    <TD align="center" class="ContentTD">
                            <html:select property="affState" name="searchLogForm" onchange="clearHiddenFields(this.form);">
                               <afscme:codeOptions useCode="true" codeType="AffiliateState" allowNull="true" nullDisplay="" format="{0}"/>
                            </html:select> 
                    </TD>
                    <TD align="center" class="ContentTD">
                            <html:text property="affSubunit" name="searchLogForm" size="4" maxlength="4" onchange="clearHiddenFields(this.form);"/>
                             
                    </TD>
                    <TD align="center" class="ContentTD">
                            <html:text property="affCouncil" name="searchLogForm" size="4" maxlength="4" onchange="clearHiddenFields(this.form);"/>

                    </TD>
                    <TD align="center">
                            <html:select property="updateType" name="searchLogForm" styleId="updateType">
                                <afscme:codeOptions codeType="UpdateType" allowNull="true" format="{1}" />
                            </html:select> 
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
                <TD align="left">
                        <BR> <html:submit styleClass="button" value="Submit"/>
                        <BR> <BR> 
                </TD>
                
                <TD align="center" class="DataFontI">
                        <BR>
                        To show the entire log, press the Submit button without entering any search criteria. 
                </TD>
                <TD align="right">
                        <BR>
                        <html:reset styleClass="button" value="Reset"/>
                

                </TD>
        </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR>
                <TD>
                        <BR>
                        <HR>
                        <BR> 
                </TD>
        </TR>
</TABLE>

<!--*********************************************************************************************************************//-->
<!--Results of the search start from here//-->
<!--**********************************************************************************************************************//-->

<%//if(theForm.getAffLocal() == null){
  //  out.write("null") ;
  //}else{  
  //  out.write("local" + theForm.getAffLocal()) ;
  //}
%>

<logic:present name="fileQueues" scope="request">
    
    <%if(request.getAttribute("fileQueues") != null){%>
    
        <TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
            <TR>
		<Th align="left" colspan="12">
			<afscme:searchNav formName="searchLogForm" action="/viewApplyUpdateSearchLog.action"/>
		</Th>
            </TR>
            <TR>
                <TH width="7%">Select</TH>
                <TH width="28%" colspan="5">Source of Update</TH>
                <%String url = "document.searchLogForm.submit()";%>
                <th width="8%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.FTYPE).toString()%>" title="Sort By Description">Description</afscme:sortLink></th>
                <th width="7%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.RDATE).toString()%>" title="Sort By Description">Received</afscme:sortLink></th>
                <th width="7%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.VALID_DATE).toString()%>" title="Sort By Description">Valid As Of</afscme:sortLink></th>
                <th width="7%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.PROCESSED).toString()%>" title="Sort By Description">Processed</afscme:sortLink></th>
                <th width="6%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.UPDATE_TYPE).toString()%>" title="Sort By Description">Update Type</afscme:sortLink></th>
                <th ><afscme:sortLink styleClass="TH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.COMMENTS).toString()%>" title="Sort By Description">Comments</afscme:sortLink></th>


            </TR>
            <TR>
                <TD>&nbsp;</TD>
                <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.TYPE).toString()%>" title="Sort By Description">Type</afscme:sortLink></th>
                <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.LOC).toString()%>" title="Sort By Description">Loc/Sub Chap</afscme:sortLink></th>
                <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.STATE).toString()%>" title="Sort By Description">State/Nat'l</afscme:sortLink></th>
                <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.SUB_UNIT).toString()%>" title="Sort By Description">Sub Unit</afscme:sortLink></th>
                <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateSearchLog.action" formName="searchLogForm" field="<%=new Integer(Sort.CN_CHAP).toString()%>" title="Sort By Description">CN/Ret Chap</afscme:sortLink></th>

                <TD colspan="6">&nbsp;</TD>
            </TR>

            <logic:iterate id="fileEntry" name="fileQueues" scope="request" type="org.afscme.enterprise.update.filequeue.FileEntry">
            <bean:define id="queuePk" name="fileEntry" property="queuePk"/>


            <%if( (fileEntry.getStatus() == UpdateFileStatus.PROCESSED) || (fileEntry.getStatus() == UpdateFileStatus.REJECTED)) {%>

            <TR>    
                
                    <TD align="center" class="DataFontBI">
                        <logic:equal name="fileEntry" property="fileType" value="<%=(Integer.toString(UpdateFileType.MEMBER))%>">
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
                                <% url = "/viewApplyUpdateMemberReviewSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                                <afscme:link page="<%=url%>" styleClass="action" title="View Review Preprocessing Report">Processed</afscme:link>
                            </logic:equal>
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REJECTED))%>">
                                <% url = "/viewApplyUpdateMemberEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=true"; %>
                                <afscme:link page="<%=url%>"styleClass="action" title="View Rejected Preprocessing Report">Rejected</afscme:link>
                            </logic:equal>
                        </logic:equal>

                    <!-- Links for Officer file -->
                    <logic:equal name="fileEntry" property="fileType" value="<%=(Integer.toString(UpdateFileType.OFFICER))%>">
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
                            <% url = "/viewApplyUpdateOfficerReviewSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                            <afscme:link page="<%=url%>" styleClass="action" title="View Review Preprocessing Report">Processed</afscme:link>
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REJECTED))%>">
                            <% url = "/viewApplyUpdateOfficerEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=true"; %>
                            <afscme:link page="<%=url%>"styleClass="action" title="View Rejected Preprocessing Report">Rejected</afscme:link>
                        </logic:equal>
                    </logic:equal>
                    <!-- Links for Rebate file -->
                    <logic:equal name="fileEntry" property="fileType" value="<%=(Integer.toString(UpdateFileType.REBATE))%>">
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
                            <% url = "/viewApplyUpdateRebateReviewSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                            <afscme:link page="<%=url%>" styleClass="action" title="View Review Preprocessing Report">Processed</afscme:link>
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REJECTED))%>">
                            <% url = "/viewApplyUpdateRebateEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=true"; %>
                            <afscme:link page="<%=url%>"styleClass="action" title="View Rejected Preprocessing Report">Rejected</afscme:link>
                        </logic:equal>
                    </logic:equal>
                </TD>
                <afscme:affiliateIdWrite name="fileEntry" property="affId"/>
                    <TD align="center" class="ContentTD">
                        <afscme:codeWrite name="fileEntry" property="fileType" codeType="UpdateFileType" format="{1}"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <afscme:dateWrite name="fileEntry" property="receivedDate"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <afscme:dateWrite name="fileEntry" property="validDate"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <afscme:dateWrite name="fileEntry" property="processedDate"/>
                    </TD>
                    <TD align="center" class="ContentTD">
                        <afscme:codeWrite name="fileEntry" property="updateType" codeType="UpdateType" format="{1}"/>
                    </TD>
                    <TD>
                        <logic:present name="fileEntry" property="comments">
                            <bean:write name="fileEntry" property="comments"/>
                        </logic:present>
                        <logic:notPresent name="fileEntry" property="comments">
                            &nbsp;
                        </logic:notPresent>
                    </TD>
                </TR>
                 
             <%}%>
        </logic:iterate>

    </TABLE>
    <%}%>
</logic:present>
<%String start = (String) request.getAttribute("start");%>
<%if((start != null) && (start.equals("true")) ){%>
   
        <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
            <TR>
                <TD class="ContentHeaderTD" align="center">
                    <bean:message key="error.noResultsFound"/>
                </TD>
            </TR>
        </TABLE>
    
<%}%>
</html:form>
<%@ include file="../include/footer.inc" %> 
</html:html>
