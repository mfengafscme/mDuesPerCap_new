<%@ page language="java" import="java.util.*,org.afscme.enterprise.test.*" %>
<html>
	<body>
<%
        String className = request.getParameter("class");
        String methodName = request.getParameter("method");
		if (className == null) {
			Iterator it=Tester.getTests().iterator();
			%>
			<ul>
			<%
			while (it.hasNext()) {
				String itName = (String)it.next();
				%>
				<li><a href="?class=<%=itName%>"><%=itName%></a><ul>
				<%
				Iterator it2=Tester.getMethods(itName).iterator();
				while (it2.hasNext()) {
					String itMethod = (String)it2.next();
					%>
					<li><a href="?class=<%=itName%>&method=<%=itMethod%>"><%=itMethod%></a></li>
					<%
				} 
				%>
				</ul></li>
				<%
			}
			%>
			</ul>
			<%
		} else {
			Tester result = new Tester();
			result.perform(className, methodName);
      
			%>
			tests:<%=result.runCount()%><br>
			failures:<%=result.failureCount()%><br>
			errorCount:<%=result.errorCount()%><br>
			time:<%=result.getTime()%><br>

			<%
				Iterator it = result.getTimes().keySet().iterator();
				while (it.hasNext()) {
					String name = (String)it.next();
			%>

			<br>
			<b>testcase:</b><%=name%><br>
			<b>time:</b><%=result.getTimes().get(name)%><br>

			<%
				Throwable e = (Throwable)result.getErrors().get(name);
				if (e != null) {
					%>>error:<%=result.escapeHTML(e.getMessage())%>, <%=e.getClass().getName()%><br>
					<font color="red"><pre><%=result.getStackTrace(e)%></pre></font>
				<%}%>
			<%
				Throwable f = (Throwable)result.getFailures().get(name);
				if (f != null) {
					%>>error:<%=result.escapeHTML(f.getMessage())%>, <%=f.getClass().getName()%><br>
					<font color="brown"><pre><%=result.getStackTrace(f)%></pre></font>
				<%}%>

			<%
			if (f == null && e == null) {
					%><font color="green">passed</font><%
			}
			%>
			<br>

		<%}%>

		<hr>
		<b>stdout</b><br>

			<pre><%=result.getStdOut()%></pre>

		<b>stderr</b><br>
			<pre><%=result.getStdErr()%></pre>
	<%}%>

	</body>
</html>


