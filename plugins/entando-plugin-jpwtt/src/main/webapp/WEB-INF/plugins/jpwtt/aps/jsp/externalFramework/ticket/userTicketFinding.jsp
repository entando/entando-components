<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="showlet jpwtt">

		<c:choose>
			<c:when test="${sessionScope.currentUser != 'guest'}">
				<s:if test="hasActionErrors()">
					<div class="message message_error">
						<h3><s:text name="message.title.ActionErrors" /></h3>
						<ul>
							<s:iterator value="ActionErrors">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</ul>
					</div>
				</s:if>
				<s:if test="%{null!=ticketIds && ticketIds.size() > 0}">
					<form action="<wp:action path="/ExtStr2/do/jpwtt/Ticket/User/search.action" />" method="post">
						<wpsa:subset source="ticketIds" count="10" objectName="groupTickets" advanced="true" offset="5">
							<s:set name="group" value="#groupTickets" />
							<div class="pager">
								<s:include value="/WEB-INF/aps/jsp/widgets/inc/pagerBlock.jsp" />
							</div>

							<table class="generic" summary="<wp:i18n key="jpwt_LIST_SUMMARY" />">
								<caption><wp:i18n key="jpwtt_LIST_CAPTION" /></caption>
								<tr>
									<th scope="col"><abbr title="<wp:i18n key="jpwtt_LABEL_CODE" />"><wp:i18n key="jpwtt_LABEL_CODE_SHORT" /></abbr></th>
									<th scope="col"> <wp:i18n key="jpwtt_LABEL_MESSAGE" /></th>
									<th scope="col"><wp:i18n key="jpwtt_LABEL_STATUS" /></th>
									<%--
									<th scope="col"><s:text name="priority"/></th>
									--%>
									<th scope="col"><wp:i18n key="jpwtt_LABEL_OPERATOR" /></th>
									<th scope="col"><wp:i18n key="jpwtt_LABEL_DATE" /></th>
									<th scope="col"><abbr title="<wp:i18n key="jpwtt_LABEL_RESOLVED" />"><wp:i18n key="jpwtt_LABEL_RESOLVED_SHORT" /></abbr></th>
								</tr>
								<s:iterator var="ticketId">
									<s:set var="ticket" value="%{getTicket(#ticketId)}" ></s:set>
									<tr>
										<c:set var="viewActionHref"><wp:action path="/ExtStr2/do/jpwtt/Ticket/User/view.action" ><wp:parameter name="code"><s:property value="#ticketId" /></wp:parameter></wp:action></c:set>
										<td>
											<a title="<wp:i18n key="jpwtt_VIEW_TICKET" />:&#32;<s:property value="#ticketId"/>" href="<c:out value="${viewActionHref}" escapeXml="false" />"><s:property value="#ticketId"/></a>
										</td>
										<td>
											<a title="<wp:i18n key="jpwtt_VIEW_TICKET" />:&#32;<s:property value="#ticketId"/>" href="<c:out value="${viewActionHref}" escapeXml="false" />">
												<s:if test="%{#ticket.message.length()>32}">
													<s:property value="%{#ticket.message.substring(0,50)}"/> ...
												</s:if>
												<s:else>
													<s:property value="%{#ticket.message}"/>
												</s:else>
											</a>
										</td>
										<td>
											<c:set var="status_label">jpwtt_LABEL_STATUS_<s:property value="#ticket.status"/></c:set>
											<wp:i18n key="${status_label}" />
										</td>
										<td>
											<s:if test="#ticket.assignedTo != null">
												<s:property value="#ticket.assignedTo"/>
											</s:if>
											<s:else>
												&ndash;
											</s:else>
										</td>
										<td><s:date format="dd/MM/yyyy HH:mm" name="#ticket.creationDate"/></td>
										<td>
											<s:if test="#ticket.resolved">
												<wp:i18n key="jpwtt_LABEL_YES" />
											</s:if>
											<s:else>
												<wp:i18n key="jpwtt_LABEL_NO" />
											</s:else>
										</td>
									</tr>
								</s:iterator>
							</table>

							<div class="pager">
								<s:include value="/WEB-INF/aps/jsp/widgets/inc/pagerBlock.jsp" />
							</div>
						</wpsa:subset>
					</form>
				</s:if>
				<s:else>
					<p>
						 <wp:i18n key="jpwtt_NOTICKETS" />
					</p>
				</s:else>

				<form action="<wp:action path="/ExtStr2/do/jpwtt/Ticket/User/new.action" />" method="post">
					<p>
						<s:set var="new_ticket_label"><wp:i18n key="jpwtt_BUTTON_NEW" /></s:set>
						<wpsf:submit useTabindexAutoIncrement="true" action="new" value="%{#new_ticket_label}"  cssClass="button"/>
					</p>
				</form>

			</c:when>
			<c:otherwise>
			<%-- please login... --%>
				<p><wp:i18n key="jpwtt_PLEASE_LOGIN" /></p>
			</c:otherwise>
		</c:choose>

</div>