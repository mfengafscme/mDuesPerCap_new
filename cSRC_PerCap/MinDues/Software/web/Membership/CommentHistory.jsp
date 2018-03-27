<%! String title = "Comment History", help = "CommentHistory.html";%>
<%@ include file="../include/header.inc" %>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/afscme.tld" prefix="afscme" %>

<bean:define id="action" name="commentHistoryForm" property="returnAction" />
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR>
            <afscme:button action="<%=action.toString()%>">Return</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR valign="top">
        <TD class="ContentHeaderTR">
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_AFFILIATE%>">
                <afscme:currentAffiliate />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_AFFILIATE_STAFF%>">
                <afscme:currentAffiliate /><br>
                <afscme:currentPersonName />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_OFFICER_TITLES%>">
                <afscme:currentAffiliate />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_ORG_ASSOCIATE%>">
                <afscme:currentPersonName /><BR>
                <afscme:currentOrganizationName />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_PERSON%>">
                <afscme:currentPersonName />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_MEMBER%>">
                <afscme:currentPersonName showPk="true"/>
            </logic:equal>
            <BR><BR>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR align="center">
        <TD>
            <TABLE width="100%" cellpadding="1" cellspacing="1" border="0" class="InnerContentTable">
                <TR valign="top">
                    <TH width="10%">Date Created</TH>
                    <TH align="left">Comment</TH>
                    <TH width="13%">Entered By</TH>
                </TR>
                <logic:iterate id="commentData" name="commentHistoryForm" property="comments">
                    <TR valign="top">
                        <TD align="center" class="ContentTD">
                            <afscme:dateWrite name="commentData" property="commentDt"/>
                        </TD>
                        <TD class="ContentTD">
                            <pre><bean:write name="commentData" property="comment"/></pre>
                        </TD>
                        <TD align="center" class="ContentTD">
                            <afscme:userWrite name="commentData" property="recordData.createdBy"/>
                        </TD>
                    </TR>
                </logic:iterate>
            </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="1" class="BodyContent" align="center">
    <TR>
        <TD class="ContentHeaderTR">
            <BR>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_AFFILIATE%>">
                <afscme:currentAffiliate />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_AFFILIATE_STAFF%>">
                Show Affiliate Staff Header 
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_OFFICER_TITLES%>">
                <afscme:currentAffiliate />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_ORG_ASSOCIATE%>">
                <afscme:currentPersonName /><BR>
                <afscme:currentOrganizationName />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_PERSON%>">
                <afscme:currentPersonName />
            </logic:equal>
            <logic:equal name="commentHistoryForm" property="commentHistoryFor" value="<%=org.afscme.enterprise.common.web.CommentHistoryForm.COMMENT_HISTORY_FOR_MEMBER%>">
                <afscme:currentPersonName showPk="true"/>
            </logic:equal>
        </TD>
    </TR>
</TABLE>
<TABLE cellpadding="0" cellspacing="0" border="0" class="ContentTable" align="center">
    <TR valign="top">
        <TD>
            <BR>
            <afscme:button action="<%=action.toString()%>">Return</afscme:button>
            <BR> <BR> 
        </TD>
    </TR>
</TABLE>



<%@ include file="../include/footer.inc" %> 