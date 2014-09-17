<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1><s:text name="wtt.title" /></h1>

<div id="main"><%-- main --%>
	<s:form action="search">
		<s:if test="hasActionErrors()">
			<div class="message message_error">
				<h2><s:text name="message.title.ActionErrors" /></h2>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<p>

			<label class="basic-mint-label label-search" for="status"><s:text name="label.search.by"/>&#32;<s:text name="status" />:</label>
			<select name="status" id="status" tabindex="<wpsa:counter />" >
				<option value=""><s:text name="label.states.all" /></option>
				<wp:ifauthorized permission="jpwttAdmin" var="isAdmin"/>
				<c:choose>
					<c:when test="${isAdmin}">
							<option value="1"><s:text name="label.states.opened" /></option>
							<option value="2"><s:text name="label.states.working" /></option>
							<option value="5"><s:text name="label.states.assignable" /></option>
							<option value="10"><s:text name="label.states.assigned" /></option>
							<option value="20"><s:text name="label.states.closed" /></option>
					</c:when>
					<c:otherwise>
							<option value="5"><s:text name="label.states.assignable" /></option>
							<option value="10"><s:text name="label.states.assigned" /></option>
					</c:otherwise>
				</c:choose>
			</select>
		</p>

		<fieldset>
			<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
			<div class="accordion_element">
				<p>
					<label class="basic-mint-label" for="message"><s:text name="message" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="message" id="message" />
				</p>
				<s:if test="%{priorities!=null && priorities.size()>0}">
					<p>
						<label class="basic-mint-label" for="priority"><s:text name="priority" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="priority" id="priority" list="priorities"
								headerKey="" headerValue="%{getText('label.allSelect')}" />
					</p>
				</s:if>
				<s:if test="%{interventionTypes!=null && interventionTypes.size()>0}">
					<p>
						<label class="basic-mint-label" for="userInterventionType"><s:text name="userInterventionType" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="userInterventionType" id="userInterventionType"
								headerKey="" headerValue="%{getText('label.allSelect')}"
								list="interventionTypes.values()" listKey="id" listValue="descr" />
					</p>
					<p>
						<label class="basic-mint-label" for="assignedInterventionType"><s:text name="operatorInterventionType" />:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="assignedInterventionType" id="assignedInterventionType"
								headerKey="" headerValue="%{getText('label.allSelect')}"
								list="interventionTypes.values()" listKey="id" listValue="descr" />
					</p>
				</s:if>
				<p>
					<label class="basic-mint-label" for="resolved"><s:text name="resolved" />:</label>
					<select name="resolved" id="resolved" tabindex="<wpsa:counter />" >
						<option value=""><s:text name="label.allSelect" /></option>
						<option value="0"><s:text name="label.resolved.no" /></option>
						<option value="1"><s:text name="label.resolved.yes" /></option>
					</select>
				</p>
			</div>
		</fieldset>
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" />
		</p>
	</s:form>

	<div class="subsection-light"><%-- subsection-light --%>

		<s:if test="%{ticketIds!=null && ticketIds.size()>0}" >
			<s:form action="list">
				<wpsa:subset source="ticketIds" count="10" objectName="groupTicketIds" advanced="true" offset="5">

					<s:set name="group" value="#groupTicketIds" />
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>

					<table class="generic" summary="<s:text name="note.ticketList.summary" />">
						<caption><span><s:text name="ticket.list" /></span></caption>
						<tr>
							<th><s:text name="label.code" /></th>
							<th><s:text name="label.creationDate" /></th>
							<th><s:text name="label.status" /></th>
							<th class="icon"><s:text name="label.resolved" /></th>
							<th class="icon_double"><abbr title="<s:text name="label.operations" />">&ndash;</abbr></th>
						</tr>
						<s:iterator var="ticketId">
							<s:set var="ticket" value="%{getTicket(#ticketId)}"/>
							<tr>
								<td class="monospace"><s:property value="#ticket.code" /></td>
								<td class="monospace centerText"><s:date name="#ticket.creationDate" format="dd/MM/yyyy HH:mm"/></td>
								<td>
									<s:if test="%{#ticket.status==1}"><%-- STATUS OPENED --%>
										<s:text name="label.states.opened" />
									</s:if>
									<s:if test="%{#ticket.status==2}"><%-- STATUS WORKING --%>
										<s:text name="label.states.working" />
									</s:if>
									<s:if test="%{#ticket.status==5}"><%-- STATUS ASSIGNABLE --%>
										<s:text name="label.states.assignable" />
									</s:if>
									<s:if test="%{#ticket.status==10}"><%-- STATUS ASSIGNED --%>
										<s:text name="label.states.assigned" />
									</s:if>
									<s:if test="%{#ticket.status==20}"><%-- STATUS CLOSED --%>
										<s:text name="label.states.closed" />
									</s:if>
								</td>
								<td class="icon">
									<s:if test="%{#ticket.resolved}"><s:text name="label.yes" /></s:if><s:else><s:text name="label.no" /></s:else>
								</td>
								<td class="icon">
									<a href="<s:url action="entry" namespace="/do/jpwtt/Ticket"><s:param name="code" value="#ticket.code" /></s:url>" title="<s:text name="label.edit" />: <s:property value="%{#ticket.code}" />">
										<img src="<wp:resourceURL/>administration/common/img/icons/edit-content.png" alt="<s:text name="label.edit" />: <s:property value="#ticket.code" />" />
									</a>&nbsp;
									<a href="<s:url action="view" namespace="/do/jpwtt/Ticket"><s:param name="code" value="#ticket.code" /></s:url>" title="<s:text name="label.view" />: <s:property value="%{#ticket.code}" />">
										<img src="<wp:resourceURL/>plugins/jpwtt/administration/common/img/icons/22x22/detail.png" alt="<s:text name="label.view" />: <s:property value="#ticket.code" />" />
									</a>
								</td>
							</tr>
						</s:iterator>
					</table>

					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>

				</wpsa:subset>
			</s:form>
		</s:if>
		<s:else>
			<p><s:text name="note.ticketList.none" /></p>
		</s:else>
	</div><%-- subsection-light --%>

</div><%-- main --%>