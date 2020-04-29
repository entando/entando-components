<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set var="linkTypeVar" value="3"></s:set>
<s:push value="linkTypeVar">
    <%@include file="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/entando-link.jsp" %>
</s:push>

<%--
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/hypertextAttribute/entando-link.jsp" >
	<s:param name="linkTypeVar" value="3" />
</s:include>
--%>

