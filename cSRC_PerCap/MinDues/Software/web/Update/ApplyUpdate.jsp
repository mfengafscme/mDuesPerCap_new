<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.afscme.enterprise.update.Codes.UpdateFileStatus, org.afscme.enterprise.update.Codes.Sort, org.afscme.enterprise.update.Codes.UpdateFileType" %>
<%! String title = "Apply Update", help = "ApplyUpdate.html";%>
<%@ include file="../include/header.inc" %> 
<html:form action="viewApplyUpdateProcessLog.action" method="post">
<bean:define id="view"      name="view"/>
<logic:notEqual name="view" value="true">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="right">
                <BR>
                    <afscme:button action="/viewApplyUpdateProcessLog.action" >Search Log</afscme:button>
                    
                 <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:notEqual>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TH width="7%">Select</TH>
        <TH width="5%">Error Report</TH>
        <TH width="28%" colspan="5">Source of Update</TH>
        <%String url = "/viewApplyUpdateQueue.action?sort=";%>
        <th width="8%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.FTYPE).toString()%>" title="Sort By Description">Description</afscme:sortLink></th>
        <th width="7%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.RDATE).toString()%>" title="Sort By Description">Received</afscme:sortLink></th>
        <th width="7%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.VALID_DATE).toString()%>" title="Sort By Description">Valid As Of</afscme:sortLink></th>
        <th width="7%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.PROCESSED).toString()%>" title="Sort By Description">Processed</afscme:sortLink></th>
        <th width="6%"><afscme:sortLink styleClass="TH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.UPDATE_TYPE).toString()%>" title="Sort By Description">Update Type</afscme:sortLink></th>
        <th ><afscme:sortLink styleClass="TH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.COMMENTS).toString()%>" title="Sort By Description">Comments</afscme:sortLink></th>
        
       
        
        
    </TR>
    <TR>
        <TD colspan="2">&nbsp;</TD>
        <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.TYPE).toString()%>" title="Sort By Description">Type</afscme:sortLink></th>
        <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.LOC).toString()%>" title="Sort By Description">Loc/Sub Chap</afscme:sortLink></th>
        <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.STATE).toString()%>" title="Sort By Description">State/Nat'l</afscme:sortLink></th>
        <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.SUB_UNIT).toString()%>" title="Sort By Description">Sub Unit</afscme:sortLink></th>
        <th class="small"><afscme:sortLink styleClass="smallTH" action="/viewApplyUpdateQueue.action" formName="processLogForm" field="<%=new Integer(Sort.CN_CHAP).toString()%>" title="Sort By Description">CN/Ret Chap</afscme:sortLink></th>

        <TD colspan="6">&nbsp;</TD>
    </TR>
   
    <logic:present name="fileQueues" scope="request">
        <% //String url = null;%>
        <logic:iterate id="fileEntry" name="fileQueues" scope="request" type="org.afscme.enterprise.update.filequeue.FileEntry">
            <bean:define id="queuePk" name="fileEntry" property="queuePk"/>
                <!--****************************************************************************************************//-->
                <!--This part will be executed for data utility//-->
                <!--***************************************************************************************************//-->
                <logic:equal name="view"    value="true">
                <%if( (fileEntry.getStatus() != UpdateFileStatus.UPLOADED) && (fileEntry.getStatus() != UpdateFileStatus.REJECTED)) {%>
                   <TR>     
                        <TD align="center" class="DataFontBI">
                            <!-- Links for member file -->
                            <!--datamenuoption-->                           
                            
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
                                <% url = "/viewApplyUpdateMemberReviewSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                                <afscme:link page="<%=url%>" styleClass="action" title="View Review Preprocessing Report">Processed</afscme:link>
                            </logic:equal>    
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REVIEW))%>">
                                <% url = "/viewApplyUpdateMemberEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=true"; %>
                                <afscme:link page="<%=url%>"styleClass="action" title="View Rejected Preprocessing Report">Review</afscme:link>
                            </logic:equal>
                        </TD>
              		<TD align="center" class="DataFontBI">
				<logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
					<% String reportUrl = "/membershipBatchUpdateReport.action?queuePk=" + queuePk; %>
			       		<afscme:link page="<%=reportUrl%>" styleClass="action" title="View Report">View</afscme:link>
				</logic:equal>
				<logic:notEqual name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
					&nbsp;
				</logic:notEqual>
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
            </logic:equal>
            <!--************************************************************************************************//-->
            <!--Data Utility section ends here and apply update section //-->
            <!--************************************************************************************************//-->
            <TR>
            <logic:equal name="view" value="false">
                <%if (fileEntry.getFileType() != UpdateFileType.PARTICIPATION)  {%>

                    <TD align="center" class="DataFontBI">
                        <logic:equal name="fileEntry" property="fileType" value="<%=(Integer.toString(UpdateFileType.MEMBER))%>">
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REVIEW))%>">
                                <% url = "/viewApplyUpdateMemberEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                                <afscme:link page="<%=url%>" styleClass="action" title="View Preprocessing Report">Review</afscme:link>                                         
                            </logic:equal>
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PENDING))%>">
                                Pending
                            </logic:equal>
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.INVALID))%>">
                                Invalid
                            </logic:equal>
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.UPLOADED))%>">
                                Uploaded
                            </logic:equal>
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
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REVIEW))%>">
                            <% url = "/viewApplyUpdateOfficerEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                            <afscme:link page="<%=url%>" styleClass="action" title="View Preprocessing Report">Review</afscme:link>                                         
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PENDING))%>">
                            Pending
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.INVALID))%>">
                            Invalid
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.UPLOADED))%>">
                            Uploaded
                        </logic:equal>
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
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.REVIEW))%>">
                            <% url = "/viewApplyUpdateRebateEditSummary.action?queuePk=" + queuePk + "&affPk=" + fileEntry.getAffPk() + "&updateType=" + fileEntry.getUpdateType() + "&rejected=false"; %>
                            <afscme:link page="<%=url%>" styleClass="action" title="View Preprocessing Report">Review</afscme:link>                                         
                       </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PENDING))%>">
                            Pending
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.INVALID))%>">
                            Invalid
                        </logic:equal>
                        <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.UPLOADED))%>">
                            Uploaded
                        </logic:equal>
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
              	<TD align="center" class="DataFontBI">
                    <logic:notEqual name="fileEntry" property="fileType" value="<%=(Integer.toString(UpdateFileType.REBATE))%>">
                        <logic:notEqual name="fileEntry" property="fileType" value="<%=(Integer.toString(UpdateFileType.OFFICER))%>">
                            <logic:equal name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
                                    <% String reportUrl = "/membershipBatchUpdateReport.action?queuePk=" + queuePk; %>
                                    <afscme:link page="<%=reportUrl%>" styleClass="action" title="View Report">View</afscme:link>
                            </logic:equal>
                            <logic:notEqual name="fileEntry" property="status" value="<%=(Integer.toString(UpdateFileStatus.PROCESSED))%>">
                                    &nbsp;
                            </logic:notEqual>
                       </logic:notEqual>
                    </logic:notEqual>    
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
                    <%}%>
                </TR>
             </logic:equal>
        </logic:iterate>
    </logic:present>
</TABLE>
<logic:notEqual name="view" value="true">
    <TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
        <TR valign="top">
            <TD align="right">
                <BR>
                    <afscme:button action="/viewApplyUpdateProcessLog.action" >Search Log</afscme:button> 
                <BR> <BR> 
            </TD>
        </TR>
    </TABLE>
</logic:notEqual>

</html:form>
<%@ include file="../include/footer.inc" %>

