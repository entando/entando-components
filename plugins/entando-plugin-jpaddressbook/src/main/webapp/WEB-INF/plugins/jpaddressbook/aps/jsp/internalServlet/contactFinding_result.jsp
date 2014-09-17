<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="../../plugins/jpaddressbook/static/css/jpaddressbook.css"/>

<div class="jpaddressbook">
	<h2 class="title"><wp:i18n key="jpaddressbook_TITLE" /></h2>

	<form action="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/resultIntro.action" />" method="post" >
		<p class="noscreen">
		<s:iterator var="attribute" value="#searcheableAttributes">
			<s:if test="#attribute.textAttribute">
				<s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
				<wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
			</s:if>
			<s:elseif test="#attribute.type == 'Date'">
				<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
				<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
				<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
				<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Number'">
				<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
				<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
				<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
				<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
			</s:elseif>
			<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'"> 
				<s:set name="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
				<wpsf:hidden name="%{#booleanInputFieldName}" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
			</s:elseif>
		</s:iterator>
		</p>
	
		<s:if test="hasFieldErrors()">
			<h3><wp:i18n key="FIELD_ERROS" /></h3> 
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</s:if>
		 
		<s:set var="contactIds" value="searchResult" />
		<s:if test="#contactIds.isEmpty()">
			<p><wp:i18n key="jpaddressbook_NOCONTACTSFOUND" /></p>
		</s:if>
		<s:else>
	
			<wpsa:subset source="#contactIds" count="5" objectName="groupContact" advanced="true" offset="5">
				<s:set name="group" value="#groupContact" />
				
				<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/pagerInfo.jsp" />
				
				<s:set value="userProfilePrototype" var="userProfilePrototype" ></s:set>
				
				<table>
					<tr>
						<s:if test="#userProfilePrototype.surnameAttributeName == null && #userProfilePrototype.firstNameAttributeName == null && #userProfilePrototype.mailAttributeName == null">
						<th>Id</th>
						</s:if>
						<s:if test="#userProfilePrototype.surnameAttributeName != null"><th><wp:i18n key="jpaddressbook_SURNAME" /></th></s:if>
						<s:if test="#userProfilePrototype.firstNameAttributeName != null"><th><wp:i18n key="jpaddressbook_FIRSTNAME" /></th></s:if>
						<s:if test="#userProfilePrototype.mailAttributeName != null"><th><wp:i18n key="jpaddressbook_EMAIL" /></th></s:if>
						<c:if test="${sessionScope.currentUser != 'guest'}">
							<th><wp:i18n key="jpaddressbook_ACTION" /></th>
						</c:if> 
					</tr>
				
					<s:iterator var="contactId">
						<s:set var="contact" value="%{getContact(#contactId)}" />
						<tr>
							<s:if test="#userProfilePrototype.surnameAttributeName == null && #userProfilePrototype.firstNameAttributeName == null && #userProfilePrototype.mailAttributeName == null">
							<td>
								<s:set var="viewContactActionPath"><wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/view.action" ><wp:parameter name="entityId"><s:property value="#contactId" /></wp:parameter></wp:action></s:set>
								<a title="<wp:i18n key="jpaddressbook_VIEW_CONTACT" />" href="<s:property value="#viewContactActionPath" escape="false" />" tabindex="<wpsa:counter />"><s:property value="#contactId" /></a>
							</td>
							</s:if>
							<%-- EVENTUALI COLONNE CONFIGURABILI IN BASE AL MODELLO DEL PROFILO --%>
							<s:if test="#userProfilePrototype.surnameAttributeName != null">
								<td>
									<s:set var="editContactActionPath"><wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/view.action" ><wp:parameter name="entityId"><s:property value="#contactId" /></wp:parameter></wp:action></s:set>
									<a title="<wp:i18n key="jpaddressbook_VIEW_CONTACT" />" href="<s:property value="#editContactActionPath" escape="false" />"><s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/></a>
								</td>
							</s:if>
							<s:if test="#userProfilePrototype.firstNameAttributeName != null">
								<td>
									<s:set var="editContactActionPath"><wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/view.action" ><wp:parameter name="entityId"><s:property value="#contactId" /></wp:parameter></wp:action></s:set>
									<a title="<wp:i18n key="jpaddressbook_VIEW_CONTACT" />" href="<s:property value="#editContactActionPath" escape="false" />"><s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/></a>
								</td>
							</s:if>
							<s:if test="#userProfilePrototype.mailAttributeName != null">
								<td>
									<s:property value="#contact.getValue(#userProfilePrototype.mailAttributeName)"/>
								</td>
							</s:if>
							<c:if test="${sessionScope.currentUser != 'guest'}">
								<td>
								<s:if test="#contact.owner.equals(#session.currentUser.username)">
									<s:set var="editContactActionPath"><wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/edit.action" ><wp:parameter name="entityId"><s:property value="#contactId" /></wp:parameter></wp:action></s:set>
									<a href="<s:property value="#editContactActionPath" escape="false" />" tabindex="<wpsa:counter />"><wp:i18n key="jpaddressbook_EDITACTION" /></a>
									&#32;
									<s:set var="trashContactActionPath"><wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/trash.action" ><wp:parameter name="entityId"><s:property value="#contactId" /></wp:parameter></wp:action></s:set>
									<a href="<s:property value="#trashContactActionPath" escape="false" />" tabindex="<wpsa:counter />"><wp:i18n key="jpaddressbook_DELETEACTION" /></a>
								</s:if>
								<s:else>&#32;</s:else>
								</td>
							</c:if>
							
						</tr>
					</s:iterator>
				</table>
				
				<s:include value="/WEB-INF/plugins/jpaddressbook/aps/jsp/internalServlet/inc/pager_formBlock.jsp" />
				
			</wpsa:subset>
			
		</s:else>
		
	</form>
	  
	<c:if test="${sessionScope.currentUser != 'guest'}">
		<p class="goback">
			<a href="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/new.action" />"><wp:i18n key="jpaddressbook_CREATENEWCONTACT" /></a>
		</p>
	</c:if>	 
</div>