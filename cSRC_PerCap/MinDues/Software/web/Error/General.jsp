<%@ page isErrorPage="true" %>
<%@ page import="org.afscme.enterprise.controller.web.AFSCMEAction" %>
<%!String title = "Error", help = "Error.html";%>
<%@ include file="../include/header.inc" %>

<center>
We have encountered a problem with our system.  Please try again later.
</center>

<%@ include file="../include/footer.inc" %>
</script>
<b><p>Exception</p></b>
<pre>
    <%
    if (exception == null)
        exception = (Exception)request.getAttribute("javax.servlet.error.exception");
	if (exception != null) {
		String msg = AFSCMEAction.toString(exception);
		if (msg != null) {
			java.util.StringTokenizer msgTok = new java.util.StringTokenizer(msg, "\n");
			while (msgTok.hasMoreTokens()) {
				String tok = msgTok.nextToken();
				if (tok.indexOf("org.afscme.enterprise") != -1) {
					%><font color="red"><b><%=tok%></b></font><%
				} else {
					%><%=tok%><%
				}
			}
		}
	} else {
		%>No exception<%
	}
	%>
</pre>
<b><p>Request</p></b>
<pre>
    <%=AFSCMEAction.toString(request)%>
</pre>


