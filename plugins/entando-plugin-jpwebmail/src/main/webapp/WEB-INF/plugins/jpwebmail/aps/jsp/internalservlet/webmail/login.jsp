<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/doLogin.action"/>" method="post">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-block">
			<p><strong><wp:i18n key="ERRORS"/></strong></p>
			<ul class="unstyled">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<br />
	USER <c:out value="${sessionScope.currentUser.username}"/>
	<br />
	**INSERT PASSWORD in order to login your mail box**
	<br />
	
	<wpsf:password name="webmailPassword" maxlength="254" />
	
	<p class="form-actions">
		<wp:i18n key="jpwebmail_SUBMIT" var="labelSubmitVar" />
		<wpsf:submit cssClass="btn btn-primary" useTabindexAutoIncrement="true" value="%{#attr.labelSubmitVar}" />
	</p>
	
</form>
