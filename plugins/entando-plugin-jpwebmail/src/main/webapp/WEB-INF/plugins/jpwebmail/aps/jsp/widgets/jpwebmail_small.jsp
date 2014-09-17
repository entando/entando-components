<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="webmail" uri="/webmail-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="../../plugins/jpwebmail/static/css/webmail.css" />
<div class="webmailIntro">
<p><wp:i18n key="jpwebmail_SERVICE_INTRO" /></p>
<webmail:webmailIntro var="infoBeanVar" requiredLoginVar="requiredLoginParamVar" />
<c:choose>
	<c:when test="${null != infoBeanVar}">
		<c:choose>
			<c:when test="${infoBeanVar.existMailbox}">
				<p><wp:i18n key="jpwebmail_HAVE" /> &#32;<strong><c:out value="${infoBeanVar.messageCount}"/></strong>&#32;<wp:i18n key="jpwebmail_MSG_IN_ACCOUNT" /></p>
				<p><wp:i18n key="jpwebmail_HAVE" /> &#32;<strong><c:out value="${infoBeanVar.unreadMessageCount}"/></strong>&#32;<wp:i18n key="jpwebmail_MSG_NOT_READ" /></p>
				<wp:pageWithWidget widgetTypeCode="jpwebmail_navigation" var="jpwebmailNavigationPageVar" listResult="false" />
				<p><wp:i18n key="jpwebmail_GO_TO" />&#32;<a href="<wp:url page="${jpwebmailNavigationPageVar.code}" />"><wp:i18n key="jpwebmail_EMAIL_ACCOUNT" /></a></p>
			</c:when>
			<c:otherwise>
				<p><c:out value="${sessionScope.currentUser}"/>&#32;<wp:i18n key="jpwebmail_NO_ACCOUNT_INTRO" /></p>
				<%-- 
				<p><wp:i18n key="jpwebmail_GO_TO" /> &#32;<a href="<wp:url page="webmail_intro" />"><wp:i18n key="jpwebmail_EMAIL_ACCOUNT" /></a></p>
				--%>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${requiredLoginParamVar}">
				<br />
				USER <c:out value="${sessionScope.currentUser.username}"/>
				<br />
				<form class="navbar-search pull-left" action="<wp:url />" method="get">
					<input type="password" name="webmailPassword" class="search-query span2" placeholder="<wp:i18n key="jpwebmail_WEBMAIL_PASSWORD" />" x-webkit-speech="x-webkit-speech" />
				</form>
			</c:when>
			<c:otherwise>
				<p><wp:i18n key="jpwebmail_NO_LOGIN" /></p>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
</div>