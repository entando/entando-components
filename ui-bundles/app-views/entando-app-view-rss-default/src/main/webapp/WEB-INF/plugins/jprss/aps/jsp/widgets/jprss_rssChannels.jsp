<%@ taglib prefix="jprss" uri="/jprss-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jprss:rssList listName="rssList" />
<wp:info key="langs" var="langs" />

<h1><wp:i18n key="jprss_FEED_LIST"/></h1>
<dl class="dl-horizontal">
<c:if test="${!(empty rssList)}">
	<c:forEach var="channel" items="${rssList}">
		<h2><span class="icon  icon-th-list"></span>&#32;<c:out value="${channel.title}"/></h2>
		<p><c:out value="${channel.description}"/></p> 
		<ul class="unstyled inline margin-medium-bottom">
			<c:forEach var="channelLang" items="${langs}">
				<li class="inline">
					<a 
						href="<wp:info key="systemParam" paramName="applicationBaseURL"/>do/jprss/Rss/Feed/show.action?id=<c:out value="${channel.id}"/>&amp;lang=<c:out value="${channelLang.code}"/>"
						class="badge"
						>
						<c:out value="${channelLang.descr}"/>
					</a>
				</li>
			</c:forEach>
		</ul>
	</c:forEach>
</c:if>
