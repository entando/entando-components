<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpaddressbook/static/css/jpaddressbook.css"/>

<div class="jpaddressbook">
	<h2 class="title"><wp:i18n key="jpaddressbook_TITLE" /></h2>
	
	<p class="contactdetailsIntro"><wp:i18n key="jpaddressbook_CONTACT_DETAILS" /></p>
	
	<%-- START CICLO ATTRIBUTI --%>
	<s:set name="lang" value="defaultLang" /> 
	
	<dl class="contactdetails"> 
		<s:iterator value="contact.attributes" id="attribute"> 
			<s:set var="currentFieldLabel"><wp:i18n key="jpaddressbook_ATTR${attribute.name}" /></s:set>
			<s:if test="#attribute.active">
				<%-- INIZIALIZZAZIONE TRACCIATORE --%>
				<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
				
				<s:if test="%{#attribute.type == 'Monotext' && !(#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == '')}">   
					<%-- ############# ATTRIBUTO TESTO MONOLINGUA ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == ''}">
							&ndash;
						</s:if>
						<s:else>
							<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
						</s:else>
					</dd>
				</s:if>
				
				<s:elseif test="%{#attribute.type == 'Text' && !(#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == '') }">
					<%-- ############# ATTRIBUTO TESTO ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == ''}">
							&ndash;
						</s:if>
						<s:else>				
							<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
						</s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Longtext' && !(#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == '')}">
					<%-- ############# ATTRIBUTO Longtext ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.getTextForLang(#lang.code) == null || #attribute.getTextForLang(#lang.code) == ''}">
							&ndash;
						</s:if>
						<s:else>	
							<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
						</s:else>				
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Hypertext' && !(#attribute.textMap[#lang.code] == null || #attribute.textMap[#lang.code] == '')}">
					<%-- ############# ATTRIBUTO Hypertext ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.textMap[#lang.code] == null || #attribute.textMap[#lang.code] == ''}">
							&ndash;
						</s:if> 
						<s:else>	
							<s:property value="%{#attribute.textMap[#lang.code]}" />
						</s:else>				
					</dd>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Boolean'">
					<%-- ############# ATTRIBUTO Boolean ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.value == true}"><s:text name="label.yes"/></s:if>
						<s:else><s:text name="label.no"/></s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'ThreeState'">
					<%-- ############# ATTRIBUTO ThreeState ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.booleanValue == null}"><wp:i18n key="jpaddressbook_ATTRIBUTE_BOTH_VALUE" /></s:if>
						<s:elseif test="%{#attribute.booleanValue != null && #attribute.booleanValue == true}"><wp:i18n key="jpaddressbook_ATTRIBUTE_YES_VALUE" /></s:elseif>
						<s:elseif test="%{#attribute.booleanValue != null && #attribute.booleanValue == false}"><wp:i18n key="jpaddressbook_ATTRIBUTE_NO_VALUE" /></s:elseif>			
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Number' && !(#attribute.value == null || #attribute.value == '')}">
					<%-- ############# ATTRIBUTO Number ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.value == null || #attribute.value == ''}">
							&ndash;
						</s:if> 
						<s:else>	
							<s:property value="#attribute.value" />
						</s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Date' && !(#attribute.getFormattedDate('dd/MM/yyyy') == null || #attribute.getFormattedDate('dd/MM/yyyy') == '')}">
					<%-- ############# ATTRIBUTO Date ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.getFormattedDate('dd/MM/yyyy') == null || #attribute.getFormattedDate('dd/MM/yyyy') == ''}">
							&ndash;
						</s:if>
						<s:else>
							<s:property value="#attribute.getFormattedDate('dd/MM/yyyy')" />
						</s:else>
					</dd>
				</s:elseif>
				
				<s:elseif test="%{#attribute.type == 'Enumerator' && !(#attribute.getText() == null || #attribute.getText() == '')}">
					<%-- ############# ATTRIBUTO TESTO Enumerator ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:if test="%{#attribute.getText() == null || #attribute.getText() == ''}">
							&ndash;
						</s:if>
						<s:else>
							<s:property value="%{#attribute.getText()}" />
						</s:else>				
					</dd>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'CheckBox'">
					<%-- ############# ATTRIBUTO CheckBox ############# --%>
					<dt>
						<s:property value="%{#currentFieldLabel}" />
					</dt>
					<dd>
						<s:set name="checkedValue" value="%{#attribute.booleanValue != null && #attribute.booleanValue ==true}" />
						<s:if test="%{#checkedValue}"><wp:i18n key="jpaddressbook_ATTRIBUTE_YES_VALUE" /></s:if>
						<s:else><wp:i18n key="jpaddressbook_ATTRIBUTE_NO_VALUE" /></s:else> 
					</dd>
				</s:elseif>
				
			</s:if> 
		</s:iterator>
		<dt>
			<wp:i18n key="jpaddressbook_PUBLIC_CONTACT" /> 
		</dt>
		<dd>
			<s:if test="contact.publicContact"><wp:i18n key="jpaddressbook_ATTRIBUTE_YES_VALUE" /></s:if>
			<s:else><wp:i18n key="jpaddressbook_ATTRIBUTE_NO_VALUE" /></s:else>
		</dd>
		
	</dl>
	 
	
	<%-- liste --%>
	
		<s:iterator value="contact.attributes" id="attribute">
	
				<s:if test="#attribute.type == 'Monolist'">
					<%-- ############# ATTRIBUTO Monolist ############# --%>
					<%-- TODO TO-DO --%>
					<h3><s:property value="#attribute.name" /></h3>
					<s:if test="%{#attribute == null || #attribute == ''}">
						&ndash;
					</s:if>
					<s:else>
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/monolistAttribute.jsp" />
					</s:else>				
				</s:if>
				
				<s:elseif test="#attribute.type == 'List'">
					<%-- ############# ATTRIBUTO List ############# --%>
					<%-- TODO TO-DO --%>
					<h3><s:property value="#attribute.name" /></h3>
					<s:if test="%{#attribute == null || #attribute == ''}">
						&ndash;
					</s:if>
					<s:else>
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/listAttribute.jsp" />
					</s:else>
				</s:elseif>
				
				<s:elseif test="#attribute.type == 'Composite'">
					<%-- ############# ATTRIBUTO Composite ############# --%>
					<%-- TODO TO-DO --%>
					<h3><s:property value="#attribute.name" /></h3>
					<s:if test="%{#attribute == null || #attribute == ''}">
						&ndash;
					</s:if>
					<s:else>
						<s:include value="/WEB-INF/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
					</s:else>
				</s:elseif>
	
		</s:iterator> 
	
	<%-- END CICLO ATTRIBUTI --%>
	
	<p class="goback">
		<a href="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/resultIntro.action" />"><wp:i18n key="jpaddressbook_GOTO_ADDRESSBOOK" /></a>
	</p>
</div>