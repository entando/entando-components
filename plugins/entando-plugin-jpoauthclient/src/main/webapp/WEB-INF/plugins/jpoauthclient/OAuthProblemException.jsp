<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="net.oauth.OAuthProblemException"%>
<%@page import="net.oauth.server.OAuthServlet"%>
<HTML>
<body>
OAuthProblemException:<br/>
<form>
<table>
<%
    OAuthProblemException p = (OAuthProblemException) request.getAttribute("OAuthProblemException");
    for (Iterator i = p.getParameters().entrySet().iterator(); i.hasNext(); ) {
        Map.Entry parameter = (Map.Entry) i.next();
        Object v = parameter.getValue();
        if (v != null) {
            String value = v.toString();
            %>
    <tr valign="top">
        <td align="right"><%=OAuthServlet.htmlEncode((String) parameter.getKey())%>:&nbsp;</td>
        <td><%
            if (value == null) {
                %>&nbsp;<%
            } else if (value.length() < 60 && value.indexOf('\n') < 0) {
                %><%=OAuthServlet.htmlEncode(value)%><%
            } else {
                %><textarea cols="60" rows="4" wrap="off" readonly="true"><%=OAuthServlet.htmlEncode(value)%></textarea><%
            }
            %></td>
    </tr><%
        }
    }
%>
</table>
</form>
</body>
</HTML>
