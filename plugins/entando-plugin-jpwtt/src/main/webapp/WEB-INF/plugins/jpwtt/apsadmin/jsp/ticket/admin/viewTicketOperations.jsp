<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<table class="generic" summary="<s:text name="operation.history.summary" />">
	<caption><span><s:text name="label.executedOperations" /></span></caption>
	<tr>
		<th><s:text name="label.operationDate" /></th>
		<th><s:text name="label.operator" /></th>
		<th><s:text name="label.operation" /></th>
	</tr>
	<s:iterator id="operation" value="ticketOperations">
		<tr>
			<td class="centerText monospace"><s:date name="#operation.date" format="dd/MM/yyyy HH:mm"/></td>
			<td><s:property value="#operation.operator"/></td>
			<td>
			<%-- Operazione UPDATE --%>
				<s:if test="%{#operation.operationCode==1}">
					<%--
					[ <s:property value="#operation.interventionType"/> | <s:property value="#operation.priority"/> ]
					--%>
					<s:text name="label.ticket.change.interventionType">
						<s:param><s:if test="%{#operation.interventionType != null && #operation.interventionType != 0 }"><span class="important"><s:property value="%{getInterventionType(#operation.interventionType).descr}"/></span></s:if><s:else><span class="important"><s:text name="label.noOperationType" /></span></s:else></s:param>
						<s:param><s:if test="%{#operation.priority != null && #operation.priority != 0}"><span class="important"><s:property value="%{getPriority(#operation.priority)}"/></span></s:if><s:else><span class="important"><s:text name="label.noPriorityType" /></span></s:else></s:param>
					</s:text>
					<s:if test="%{#operation.note != null && #operation.note != '' }">
						<br /><span class="important"><s:text name="label.operation.comment" /></span>: <s:property value="#operation.note"/>
					</s:if>
				</s:if>

				<%-- Operazione ANSWER --%>
				<s:if test="%{#operation.operationCode==5}">
					<s:text name="label.operation.sentAnswer" ><s:param><s:property value="#operation.note"/></s:param></s:text>
				</s:if>

				<%-- Operazione SETASSIGNABLE --%>
				<s:if test="%{#operation.operationCode==10}">
					<s:text name="label.ticket.assignedToRole"></s:text>:
					<s:set var="assignedRole" value="%{getRole(#operation.wttRole)}" />
					<s:if test="%{#assignedRole!=null}"><s:property value="%{#assignedRole.description}"/></s:if><s:else><s:property value="%{#operation.wttRole}"/></s:else>
				</s:if>

				<%-- Operazione TAKEINCHARGE --%>
				<s:if test="%{#operation.operationCode==15}">
					<s:text name="label.operation.takenInChargeBy">
						<s:param><span class="important"><s:property value="%{#operation.operator}"/></span></s:param>
					</s:text>
				</s:if>

				<%-- Operazione REOPEN --%>
				<s:if test="%{#operation.operationCode==25}">
					<s:text name="label.operation.againNotAssigned" />
				</s:if>

				<%-- Operazione CLOSE --%>
				<s:if test="%{#operation.operationCode==30}">
					<s:text name="label.operation.closedTicket" />
				</s:if>

			</td>
		</tr>
	</s:iterator>
</table>