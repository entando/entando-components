<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<form class="pluginsForm" action="<wp:action path="/ExtStr2/do/jpnewsletter/Front/RegSubscriber/addSubscription.action" />" method="post" >

<s:if test="hasFieldErrors()">
	<div>
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
	<p>
		<label for="jpnewsletter_email"><wp:i18n key="jpnewsletter_LABEL_EMAIL" /></label><br />
		<wpsf:textfield useTabindexAutoIncrement="true" id="jpnewsletter_email" name="mailAddress" cssClass="text" /> 
	</p>
	<p>
		<s:set var="jpnewsletter_REGISTER"><wp:i18n key="jpnewsletter_REGISTER" /></s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="addSubscription" value="%{#jpnewsletter_REGISTER}" cssClass="button" />
	</p>
</form>


