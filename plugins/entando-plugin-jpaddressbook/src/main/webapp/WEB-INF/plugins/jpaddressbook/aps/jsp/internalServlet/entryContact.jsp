<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpaddressbook/static/css/jpaddressbook.css"/>

<div class="jpaddressbook">
	<div class="jpaddressbook_entryContact">

		<h2 class="title"><wp:i18n key="jpaddressbook_TITLE" /></h2> 
		
		<s:if test="contact.id == null">
			<h3><wp:i18n key="jpaddressbook_TITLE_NEWCONTACT" /></h3>
		</s:if>
		<s:else>
			<h3><wp:i18n key="jpaddressbook_TITLE_EDITCONTACT" /></h3>
		</s:else>
		
		<form action="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/save.action" />" method="post" >
		
			<s:if test="hasFieldErrors()">
				<h4><wp:i18n key="FIELD_ERROS" /></h4> 
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>   
						</s:iterator>
					</s:iterator>
				</ul>
			</s:if> 
			
			<s:set name="lang" value="defaultLang"></s:set>
			
			<%-- START CICLO ATTRIBUTI --%>
			<s:iterator value="contact.attributes" id="attribute">
				<s:if test="#attribute.active">
				<%-- INIZIALIZZAZIONE TRACCIATORE --%>
				<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
				<s:set var="i18n_attribute_name">jpaddressbook_ATTR<s:property value="#attribute.name" /></s:set>
				<s:set var="attribute_id">jpaddressbook_<s:property value="#attribute.name" /></s:set>
				
				<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/iteratorAttribute.jsp" />
				</s:if>
			</s:iterator>
			<%-- END CICLO ATTRIBUTI --%>
			
			<s:set var="submitLabel"><wp:i18n key="jpaddressbook_SAVE_CONTACT" /></s:set> 
			<p class="save"><wpsf:submit useTabindexAutoIncrement="true" value="%{#submitLabel}" cssClass="button" /></p>   
		</form>	
	</div>
</div>