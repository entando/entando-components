<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
<wp:headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />
<wp:info key="langs" var="langs" />
<wp:info key="currentLang" var="currentLang" />

<ul class="nav">
	<li class="dropdown">
	<a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="icon-flag"></span> <wp:i18n key="ESLC_LANGUAGE" /> <span class="caret"></span></a>
		<ul class="dropdown-menu">
		<c:forEach var="iteratorLang" items="${langs}" varStatus="status">
			<li<c:if test="${iteratorLang.code==currentLang}"> class="active"</c:if>><a href="<wp:url lang="${iteratorLang.code}" paramRepeat="true" />"><wp:i18n key="ESLC_LANG_${iteratorLang.code}" /></a></li>
		</c:forEach>
		</ul>
	</li>
</ul>