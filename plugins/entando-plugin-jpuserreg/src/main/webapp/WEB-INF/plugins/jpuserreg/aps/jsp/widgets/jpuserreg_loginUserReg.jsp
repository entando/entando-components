<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1><wp:i18n key="LOGIN" /></h1>

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		<p><wp:i18n key="WELCOME" />, <c:out value="${sessionScope.currentUser}"/>!</p>
		
		<c:if test="${sessionScope.currentUser.japsUser}">
			<p>
				<wp:i18n key="CREATION_DATE" />: <c:out value="${sessionScope.currentUser.creationDate}"/> <br />
				<wp:i18n key="LAST_ACCESS" />: <c:out value="${sessionScope.currentUser.lastAccess}"/> <br />
				<wp:i18n key="LAST_PASSWORD_CHANGE" />: <c:out value="${sessionScope.currentUser.lastPasswordChange}"/> <br />
			</p>
		
			<c:if test="${!sessionScope.currentUser.credentialsNotExpired}">
				<p><wp:i18n key="PASSWORD_EXPIRED" /></p>
				<p><wp:i18n key="GO_TO" /> <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/editPassword.action"><wp:i18n key="CHANGE_PASSWORD" /></a></p>
			</c:if>
		
		</c:if>
		
		<wp:ifauthorized permission="enterBackend">
		<p>
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<wp:info key="currentLang" />"><wp:i18n key="ADMINISTRATION" /></a>		
		</p>
		</wp:ifauthorized>
		<p>
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout.action"><wp:i18n key="LOGOUT" /></a>
		</p>

		<wp:ifauthorized permission="superuser" >
			<c:set var="ctrl" value="admin" />
		</wp:ifauthorized>
	
		<c:if test="${ctrl != 'admin'}">
			<p><a href="<wp:url page="disactiv" />" ><wp:i18n key="jpuserreg_ACCOUNT_SUSPENSION" /></a></p>
		</c:if>
		<wp:pageWithWidget var="editProfilePageVar" widgetTypeCode="userprofile_editCurrentUser" />
		<c:if test="${null != editProfilePageVar}" >
		<p><a href="<wp:url page="${editProfilePageVar.code}" />" ><wp:i18n key="jpuserreg_PROFILE_CONFIGURATION" /></a></p>
		</c:if>
		
	</c:when>
	<c:otherwise>
	
	<c:if test="${accountExpired}">
		<div class="alert alert-block"><wp:i18n key="USER_STATUS_EXPIRED" /></div>
	</c:if>
	<c:if test="${wrongAccountCredential}">
		<div class="alert alert-block"><wp:i18n key="USER_STATUS_CREDENTIALS_INVALID" /></div>
	</c:if>
	
	<form action="<wp:url/>" method="post" class="form-horizontal">
		<div class="control-group">
			<label for="username" class="control-label"><wp:i18n key="USERNAME" />:</label>
			<div class="controls">
				<input id="username" type="text" name="username" />
			</div>
		</div>
		<div class="control-group">
			<label for="password" class="control-label"><wp:i18n key="PASSWORD" />:</label>
			<div class="controls">
				<input id="password" type="password" name="password" />
			</div>
		</div>
		<p class="form-actions">
			<input type="submit" value="<wp:i18n key="OK" />" class="btn btn-primary"/>
		</p>
	</form>
	
	<p><a href="<wp:url page="registr" />" ><wp:i18n key="jpuserreg_REGISTRATION" /></a></p>
	<p><a href="<wp:url page="userrecover" />" ><wp:i18n key="jpuserreg_PASSWORD_RECOVER" /></a></p>
	
	</c:otherwise>
</c:choose>