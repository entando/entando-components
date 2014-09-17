<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:form action="searchRecipients">
	<wpsf:hidden name="actualRecipient" />
<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
	<wpsf:hidden name="selectedReceivers" value="%{#selectedReceiver}" id="selectedReceivers-%{#rowstatus.index}"/>
</s:iterator>

Selezionare gli indirizzi da aggiungere nel campo&#32;
<s:if test="actualRecipient == 1 ">TO</s:if>
<s:elseif test="actualRecipient == 2 ">CC</s:elseif>
<s:elseif test="actualRecipient == 3 ">BCC</s:elseif>
<s:else>
	ERRORE!!! TIPO RECIPIENT NON RICONOSCIUTO
</s:else>

<br /><br />

<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" />

<s:set name="iconSearch"><wp:resourceURL />plugins/jpwebmail/static/img/system-search.png</s:set>
<s:set name="addressSearch"><wp:i18n key="jpwebmail_SEARCH" /></s:set>	
<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconSearch}" value="%{getText('label.search')}" title="%{#addressSearch}" />

</s:form>

<s:form action="searchRecipients">

<wpsf:hidden name="text" />
<wpsf:hidden name="actualRecipient" />
<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
	<wpsf:hidden name="selectedReceivers" value="%{#selectedReceiver}" id="selectedReceivers-%{#rowstatus.index}"/>
</s:iterator>

<s:if test="%{searchedReceivers.size() > 0}">
	<wpsa:subset source="searchedReceivers" count="10" objectName="contacts" advanced="true" offset="5">
	<s:set name="group" value="#contacts" />

	<div class="pager"><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /> 
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<s:iterator id="contact" status="rowstatus" >
		<input type="checkbox" name="selectedReceivers"	id="selectedReceivers_<s:property value="#counter.index"/>" value="<s:property value="#contact.username"/>" /><label for="selectedReceivers_<s:property value="#counter.index"/>"><s:property value="#contact.fullName" />&#32;&lt;<s:property value="#contact.emailAddress"/>&gt;</label>
	<br />
	</s:iterator>
	<div class="pager"><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" /></div>
	<p><wpsf:submit useTabindexAutoIncrement="true" action="addRecipients" value="AGGIUNGI" title="AGGIUNGI" cssClass="button" /></p>			

</wpsa:subset>
</s:if>
<s:else>
	<br />NESSUN RISULTATO<br />
</s:else>

<br />


<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
	<s:set name="currentUser" value="%{getContact(#selectedReceiver)}"/>
	<s:property value="#currentUser.fullName" />&#32;&lt;<s:property value="#currentUser.emailAddress"/>&gt;
	<wpsa:actionParam action="removeRecipient" var="removeRecipientActionName" >
		<wpsa:actionSubParam name="actualReceiver" value="%{#currentUser.username}" />
	</wpsa:actionParam>
	<wpsf:submit useTabindexAutoIncrement="true" action="%{#removeRecipientActionName}" value="RIMUOVI" title="RIMUOVI" />
	<br />
</s:iterator>

<br /><br />

<wpsf:submit useTabindexAutoIncrement="true" action="joinRecipients" value="FINE" title="FINE" cssClass="button" />

</s:form>