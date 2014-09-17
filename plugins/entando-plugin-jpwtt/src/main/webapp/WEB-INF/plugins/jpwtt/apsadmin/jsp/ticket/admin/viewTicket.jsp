<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="wtt.title" />"><s:text name="wtt.title" /></a></h1>
<div id="main">
	<h2><s:text name="wtt.title.tickedDetails" /></h2>
	<dl class="table-display">
		<dt><s:text name="label.code" /></dt>
			<dd><s:property value="ticket.code"/><s:if test="%{0>=ticket.code.length()}">&nbsp;</s:if></dd>
		<dt><s:text name="label.creationDate" /></dt>
			<dd><s:date name="ticket.creationDate" format="dd/MM/yyyy HH:mm" /><s:if test="%{0>=ticket.creationDate.length()}">&nbsp;</s:if></dd>
		<dt><s:text name="label.user" /></dt>
			<dd><s:property value="ticket.nome"/>&#32;<s:property value="ticket.cognome"/><s:if test="%{0>=ticket.nome.length()}">&nbsp;</s:if></dd>
		<dt><s:text name="label.codicefiscale" /></dt>
			<dd><s:property value="ticket.codFisc"/><s:if test="%{0>=ticket.codFisc.length()}">&nbsp;</s:if></dd>
		<dt><s:text name="label.city" /></dt>
			<dd><s:property value="ticket.comune"/><s:if test="%{0>=ticket.comune.length()}">&nbsp;</s:if></dd>
		<dt><s:text name="label.location" /></dt>
			<dd><s:property value="ticket.localita"/><s:if test="%{0>=ticket.localita.length()}">&nbsp;</s:if></dd>
		<dt><s:text name="label.address" /></dt>
			<dd><s:property value="ticket.tipoIndirizzo"/>&nbsp;<s:property value="ticket.indirizzo"/><s:if test="%{ticket.getNumeroIndirizzo()!=null && ticket.getNumeroIndirizzo().length()>0}">
			&#32;
			n°
			&#32;
			<s:property value="ticket.numeroIndirizzo"/></s:if>
		</dd>

		<dt><s:text name="label.telephone" /></dt>
			<dd><s:property value="ticket.telefono"/><s:if test="%{0>=ticket.telefono.length()}">&nbsp;</s:if></dd>

		<dt><s:text name="label.email" /></dt>
			<dd><a href="mailto:<s:property value="ticket.email"/>"><s:property value="ticket.email"/></a><s:if test="%{0>=ticket.email.length()}">&nbsp;</s:if></dd>

		<dt><s:text name="label.note" /></dt>
			<dd><s:property value="ticket.message"/><s:if test="%{0>=ticket.localita.length()}">&nbsp;</s:if></dd>

		<s:set name="interventionType" value="%{getInterventionType(ticket.userInterventionType)}" />
		<s:if test="%{#interventionType!=null}">
			<dt><s:text name="userInterventionType" /></dt>
				<dd><s:property value="#interventionType.descr"/></dd>
		</s:if>

		<s:set name="assignedInterventionType" value="%{getInterventionType(ticket.opInterventionType)}" />
		<s:if test="%{#assignedInterventionType!=null}">
			<dt><s:text name="label.assignedInterventionType" /></dt>
				<dd><s:property value="#assignedInterventionType.descr"/></dd>
		</s:if>

		<s:set name="priority" value="%{getPriority(ticket.priority)}" />
		<s:if test="%{#priority!=null}">
			<dt><s:text name="priority" /></dt>
				<dd><s:property value="#priority"/></dd>
		</s:if>

		<dt><s:text name="status" /></dt>
			<dd>
				<s:if test="%{ticket.status==1}"><%-- STATUS OPENED --%>
					<s:text name="label.states.opened" />
				</s:if>
				<s:if test="%{ticket.status==2}"><%-- STATUS WORKING --%>
					<s:text name="label.states.working" />
				</s:if>
				<s:if test="%{ticket.status==5}"><%-- STATUS ASSIGNABLE --%>
					<s:text name="label.states.assignable" />
				</s:if>
				<s:if test="%{ticket.status==10}"><%-- STATUS ASSIGNED --%>
					<s:text name="label.states.assigned" />
				</s:if>
				<s:if test="%{ticket.status==20}"><%-- STATUS CLOSED --%>
					<s:text name="label.states.closed" />
				</s:if>
			</dd>

		<dt><s:text name="label.closingDate" /></dt>
			<dd>
				<s:if test="%{ticket.closingDate != null}">
					<s:date name="ticket.closingDate" format="dd/MM/yyyy HH:mm" />
				</s:if>
				<s:else>&mdash;</s:else>
			</dd>
		<dt><s:text name="label.resolved" /></dt>
			<dd><s:if test="%{ticket.resolved}"><s:text name="label.yes" /></s:if><s:else><s:text name="label.no" /></s:else></dd>
	</dl>

	<%-- <s:include value="viewTicketOperations.jsp" />	--%>
	<jsp:include page="viewTicketOperations.jsp" />

</div>