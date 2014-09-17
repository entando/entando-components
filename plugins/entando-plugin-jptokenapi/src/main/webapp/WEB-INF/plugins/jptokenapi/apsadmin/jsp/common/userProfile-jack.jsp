<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpta" uri="/jptokenapi-core" %>

<wpta:myToken var="myTokenVar" />
<h2 class="margin-more-bottom"><s:text name="title.jptokenapi.myToken" /></h2>
<fieldset><legend><s:text name="label.jptokenapi.myToken" /></legend>
<p>
	<c:choose>
		<c:when test="${null != myTokenVar}"><code><c:out value="${myTokenVar}"/></code></c:when>
		<c:otherwise><s:text name="jptokenapi.nullToken" /></c:otherwise>
	</c:choose>
</p>
</fieldset>
