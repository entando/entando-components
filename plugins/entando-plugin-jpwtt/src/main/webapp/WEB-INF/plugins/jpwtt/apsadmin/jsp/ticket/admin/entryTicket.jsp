<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<%--<s:include value="viewTicket.jsp" /> wtf?--%>
<jsp:include page="viewTicket.jsp" />

<%-- Operazione OPERATIONS_UPDATE--%>
<s:set name="updateAllowed" value="isOperationAllowed(ticket, 1)" />

<%-- Operazione OPERATIONS_ANSWER--%>
<s:set name="answerAllowed" value="isOperationAllowed(ticket, 5)" />

<%-- Operazione OPERATIONS_CLOSE--%>
<s:set name="closeAllowed" value="isOperationAllowed(ticket, 30)" />

<%-- Operazione OPERATIONS_REOPEN--%>
<s:set name="releaseAllowed" value="isOperationAllowed(ticket, 25)" />

<%-- Operazione OPERATIONS_SETASSIGNABLE--%>
<s:set name="assignableAllowed" value="isOperationAllowed(ticket, 10)" />

<%-- Operazione OPERATIONS_TAKEINCHARGE--%>
<s:set name="takeInChargeAllowed" value="isOperationAllowed(ticket, 15)" />


<s:if test="%{#updateAllowed || #answerAllowed || #closeAllowed || #releaseAllowed || #assignableAllowed || #takeInChargeAllowed}">

	<h2><s:text name="intervention" /></h2>

	<s:form action="update">

		<s:if test="hasActionMessages()">
			<div class="message message_confirm">
			<h3><s:text name="messages.confirm" /></h3>
			<ul>
				<s:iterator value="actionMessages">
					<li><s:property/></li>
				</s:iterator>
			</ul>
			</div>
		</s:if>

		<s:if test="hasFieldErrors()">
			<div class="message message_error">
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property/></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<s:if test="hasActionErrors()">
			<div class="message message_error">
				<h3><s:text name="message.title.ActionErrors" /></h3>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<p class="noscreen"><wpsf:hidden name="code" /></p>

		<s:if test="%{#updateAllowed || #closeAllowed}" >
			<fieldset class="margin-more-top">
				<legend><s:text name="label.info" /></legend>
				<p>
					<label for="interventionType" class="basic-mint-label"><s:text name="label.assignedInterventionType" />:</label>
					<wpsf:select useTabindexAutoIncrement="true"
						id="interventionType"
						name="interventionType"
						list="%{getInterventionTypes().values()}"
						listKey="id"
						listValue="descr"
						headerKey="0"
						headerValue="%{getText('label.noOperationType')}"
						value="%{ticket.opInterventionType}" />
				</p>

				<p>
					<label for="priority" class="basic-mint-label"><s:text name="priority" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="priority" id="priority" list="%{getPriorities()}"
						value="%{ticket.priority}" listKey="key" listValue="value" headerKey="0" headerValue="%{getText('label.noPriorityType')}" />
				</p>

				<p>
					<label for="note" class="basic-mint-label"><s:text name="label.note" />:</label>
					<wpsf:textarea useTabindexAutoIncrement="true" name="note" id="note" cols="50" rows="10" cssClass="text"/>
				</p>

				<p>
					<wpsf:checkbox useTabindexAutoIncrement="true" name="resolved" id="resolved" cssClass="radiocheck" />
					<label for="resolved"><s:text name="label.resolved" /></label>
				</p>
			</fieldset>

			<p>
				<%-- Operazione OPERATIONS_REOPEN --%>
				<s:if test="%{#releaseAllowed}" >
					<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.releaseTicket')}" action="release"/>
				</s:if>
				<%-- Operazione OPERATIONS_UPDATE --%>
				<s:if test="%{#updateAllowed}" >
					<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.updateTicketInformation')}" action="update"/>
				</s:if>
				<%-- Operazione OPERATIONS_CLOSE --%>
				<s:if test="%{#closeAllowed}" >
					<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.closeTicketNowAndSave')}" action="close"/>
				</s:if>
			</p>
		</s:if>

		<%-- Operazione OPERATIONS_ANSWER --%>
		<s:if test="%{#answerAllowed}" >
			<fieldset class="margin-more-top">
				<legend><s:text name="answer" /></legend>
				<p>
					<label for="message"><s:text name="label.answer" />:</label>
					<wpsf:textarea useTabindexAutoIncrement="true" name="message" id="message" cols="50" rows="10" cssClass="text" />
				</p>
			</fieldset>
			<p>
				<wpsa:actionParam action="answer" var="actionName" />
				<wpsf:submit useTabindexAutoIncrement="true" cssClass="button"  value="%{getText('label.sendAnswer')}" action="answer"/>
			</p>
		</s:if>

		<s:if test="%{#releaseAllowed || #assignableAllowed || #takeInChargeAllowed}" >

				<s:if test="%{#assignableAllowed && roles.size()>0}" >
					<fieldset class="margin-more-top">>
					<legend><s:text name="label.assignTo" /></legend>
						<%-- Operazione OPERATIONS_SETASSIGNABLE --%>
							<p>
								<label for="roleName"><s:text name="Operatore"/></label>&#32;<wpsf:select useTabindexAutoIncrement="true" name="roleName" id="roleName" list="roles"
										listKey="name" listValue="description" headerKey="" headerValue=" - %{getText('label.select')} -" />
										<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('Assegna')}" action="assign"/>
							</p>
					</fieldset>
				</s:if>



			<s:if test="%{(!#updateAllowed && !#closeAllowed && #releaseAllowed) || #takeInChargeAllowed}" >
				<p class="centerText">
					<%-- Operazione OPERATIONS_REOPEN --%>
					<s:if test="%{!#updateAllowed && !#closeAllowed && #releaseAllowed}" >
						<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.releaseTicket')}" cssClass="button" action="release"/>
					</s:if>
					<%-- Operazione OPERATIONS_TAKEINCHARGE --%>
					<s:if test="%{#takeInChargeAllowed}" >
						<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.takeInCharge')}" cssClass="button" action="takeInCharge"/>
					</s:if>
				</p>
			</s:if>
		</s:if>
	</s:form>
</s:if>