<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%--
Jsp che contiene l'avviso di conferma cancellazione della sottoscrizione
al servizio di newsletter
--%>

<s:if test="%{!mailAddress.equalsIgnoreCase('')}">
	<form class="pluginsForm" action="<wp:action path="/ExtStr2/do/jpnewsletter/Front/RegSubscriber/unsubscription.action"/>" method="post">
		<p>
			<wpsf:hidden name="mailAddress" />
			<wp:i18n key="jpnewsletter_UNSUB_CONFIRM" />
		</p>
		
		<p>
			<s:set var="jpnewsletter_CONFIRM_REMOVE"><wp:i18n key="jpnewsletter_CONFIRM_REMOVE" /></s:set>
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{#jpnewsletter_CONFIRM_REMOVE}" action="unsubscription" />
		</p>
	</form>
</s:if>
<s:else>
	<p>
		<wp:i18n key="jpnewsletter_UNSUB_NOMAIL" />
	</p>
</s:else>