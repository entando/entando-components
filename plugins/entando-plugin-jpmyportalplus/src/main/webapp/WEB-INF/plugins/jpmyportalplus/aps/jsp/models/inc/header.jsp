<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 	
<wp:info key="currentLang" var="currentLang" />
	<c:set var="pageheader" value="0"></c:set>
	<c:set var="counter" value="0"></c:set>
	<wp:nav var="page" spec="code(header).subtree(1)">
		<c:set var="pageheader" value="${pageheader+1}" />
	</wp:nav>
	<%-- not pageheader-1 because homepage --%>

<div id="header">
	<h1><wp:i18n key="H1"/></h1>
	<div id="header-top"><wp:show frame="0" /></div>
	<div id="header-background">
		<div id="header-logo">
			<a href="<wp:url page="homepage"/>" title="<wp:i18n key="back_homepage_logo"/>"><img src="<wp:imgURL/>logo.jpg" alt="" /></a>
		</div>
			<div id="header-tools">
				<div class="header-tools-wrapper">
					<div class="header-tools-menu">
						<ul class="language">
							[ <li <c:if test="${currentLang == 'it'}">class="selected" </c:if>><a href="<wp:url lang="it" paramRepeat="true" />">IT</a></li> |
							<li <c:if test="${currentLang == 'en'}">class="selected" </c:if>><a	href="<wp:url lang="en" paramRepeat="true" />">EN</a></li> ]
						</ul>
						<ul class="page-tools">
							<wp:nav var="page1" spec="code(homepage)+code(header).subtree(1)">
								<c:if test="${page1.code != 'header'}">
									<li><a href="<c:out value="${page1.url}" />"><c:out value="${page1.title}" /></a><c:if test="${pageheader != counter}"> | </c:if></li>
								</c:if>
								<c:set var="counter" value="${counter+1}" />
							</wp:nav>
						</ul>
					</div>
				</div>
				<div class="header-tools-fixed">
					<wp:show frame="1" />
				</div>
			</div>		
	</div>

	<a name="top-content"></a>
		<wp:show frame="2" />
</div>
