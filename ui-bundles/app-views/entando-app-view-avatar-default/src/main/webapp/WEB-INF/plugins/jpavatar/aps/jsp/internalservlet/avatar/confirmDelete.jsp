<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<div class="jpavatar">
	<h1><wp:i18n key="jpavatar_TITLE" /></h1>
	<div class="media">
		<span class="pull-left">
			<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  />
			<img
				class="media-object img-polaroid"
				src="<c:out value="${currentAvatar}" />"
				/>
		</span>
		<form class="media-body" action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/delete.action" />" method="post">
			<p>
				<wp:i18n key="jpavatar_CONFIRM_DELETE" />&#32;
				<s:submit cssClass="btn btn-danger" type="button">
					<wp:i18n key="jpavatar_DELETE" />
				</s:submit>
			</p>
		</form>
	</div>
</div>
