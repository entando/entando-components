<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpcc" uri="/jpcasclient" %>

<jpcc:CasConfigParamTag var="jpcasclient_is_active" param="active" />

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		<br />
		<div><p><wp:i18n key="WELCOME" />, <span><c:out value="${sessionScope.currentUser}"/></span>!</p></div>
		
		<c:if test="${sessionScope.currentUser.japsUser}">
		<div><p>
		CREATION DATE = <c:out value="${sessionScope.currentUser.creationDate}"/> <br />
		LAST ACCESS = <c:out value="${sessionScope.currentUser.lastAccess}"/> <br />
		LAST PASSWORD CHANGE = <c:out value="${sessionScope.currentUser.lastPasswordChange}"/> <br />
		</p></div>
		
		<c:if test="${!sessionScope.currentUser.credentialsNotExpired}">
		<div>
			<p>PASSWORD SCADUTA</p>
			<p>ACCEDERE ALLA PAGINA DI <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/editPassword.action">CAMBIO PASSWORD</a></p>
		</div>
		</c:if>
		</c:if>
		
		<div><p><a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout.action"><wp:i18n key="LOGOUT" /></a>
		<wp:ifauthorized permission="enterBackend">| <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action"><wp:i18n key="ADMINISTRATION" /></a>
		</wp:ifauthorized>
		</p></div>
	</c:when>
	<c:otherwise>
	
	<c:if test="${accountExpired}">
		<div><p>UTENTE SCADUTO. Contatta l'ammnistratore.</p></div>
	</c:if>
	<c:if test="${wrongAccountCredential}">
		<div><p>CREDENZIALI NON VALIDE</p></div>
	</c:if>
	
	<c:if test="${!jpcasclient_is_active}">
	<form id="loginForm" action="<wp:url/>" method="post">
		<fieldset>
			<div class="formPair">
				<label for="username">Username:</label><input id="username" type="text" name="username" />
			</div>
			<div class="formPair">
				<label for="password">Password:</label><input id="password" type="password" name="password" />
			</div>
			<div class="rightAlign">
				<input type="submit" value="Ok" />
			</div>
		</fieldset>
	</form>
	</c:if>
	
	<c:if test="${jpcasclient_is_active}">
	<p>
		<a href="<jpcc:CasConfigParamTag param="casLoginURL"/>?service=<wp:url paramRepeat="true"  />"><wp:i18n key="LOGIN" /></a>
	</p>
	</c:if>
	
	</c:otherwise>
</c:choose>