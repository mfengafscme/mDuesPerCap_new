<%@ page language="java" import="java.util.*,org.afscme.enterprise.test.*" %>
<%
try {
        String className = request.getParameter("class");
        String methodName = request.getParameter("method");
        Tester result = new Tester();
        result.perform(className, methodName);
        response.setContentType("text/xml");
      
%>
<testsuite
    name="<%=className%>"
    tests="<%=result.runCount()%>"
    failures="<%=result.failureCount()%>"
    errors="<%=result.errorCount()%>"
    time="<%=result.getTime()%>"
    >
    <properties/>

<%
    Iterator it = result.getTimes().keySet().iterator();
    while (it.hasNext()) {
        String name = (String)it.next();
%>
    <testcase name="<%=name%>" time="<%=result.getTimes().get(name)%>">

<%
    Throwable e = (Throwable)result.getErrors().get(name);
    if (e != null) {
        %><error message="<%=result.escapeHTML(e.getMessage())%>" type="<%=e.getClass().getName()%>"><%=result.getStackTrace(e)%></error>
    <%}%>
<%
    Throwable f = (Throwable)result.getFailures().get(name);
    if (f != null) {
        %><failure message="<%=result.escapeHTML(f.getMessage())%>" type="<%=f.getClass().getName()%>"><%=result.getStackTrace(f)%></failure>
    <%}%>
    </testcase>

<%}%>

    <system-out><%=result.getStdOut()%></system-out>
    <system-err><%=result.getStdErr()%></system-err>
<%
} catch (Exception e) {
    response.setContentType("text/plain");
    e.printStackTrace(response.getWriter());
    }
%>
    

</testsuite>

