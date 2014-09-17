<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="showlet jpwtt">

	<c:choose>
		<c:when test="${sessionScope.currentUser != 'guest'}">

			<h3><wp:i18n key="jpwtt_TICKET_DETAILS" /></h3>

			<s:set var="ticket" value="%{ticket}" />
			<dl class="table-display">
				<dt><wp:i18n key="jpwtt_LABEL_CODE" /></dt>
					<dd><s:property value="#ticket.code"/></dd>
				<dt><wp:i18n key="jpwtt_LABEL_MESSAGE" /></dt>
					<dd><s:property value="%{#ticket.message}"/></dd>
				<dt><wp:i18n key="jpwtt_LABEL_STATUS" /></dt>
					<dd><c:set var="status_label">jpwtt_LABEL_STATUS_<s:property value="#ticket.status"/></c:set><wp:i18n key="${status_label}" /></dd>

				<dt><wp:i18n key="jpwtt_LABEL_OPERATOR" /></dt>
					<dd>
						<s:if test="#ticket.assignedTo != null"><s:property value="#ticket.assignedTo"/></s:if>
						<s:else>&ndash;</s:else>
					</dd>

				<dt><wp:i18n key="jpwtt_LABEL_DATE" /></dt>
					<dd><s:date format="dd/MM/yyyy HH:mm" name="#ticket.creationDate"/></dd>

				<dt><wp:i18n key="jpwtt_LABEL_RESOLVED" /></dt>
					<dd>
						<s:if test="#ticket.resolved">
							<wp:i18n key="jpwtt_LABEL_YES" />
						</s:if>
						<s:else>
							<wp:i18n key="jpwtt_LABEL_NO" />
						</s:else>
					</dd>
			</dl>

			<s:include value="/WEB-INF/plugins/jpwtt/aps/jsp/externalFramework/ticket/inc/viewTicket-operation-details.jsp" />

			<p>
				<a href="<wp:action path="/ExtStr2/do/jpwtt/Ticket/User/list.action" />"><wp:i18n key="jpwtt_GOTO_LIST" /></a>
			</p>

		</c:when>
		<c:otherwise>
			<p><wp:i18n key="jpwtt_PLEASE_LOGIN" /></p>
		</c:otherwise>
	</c:choose>

</div>