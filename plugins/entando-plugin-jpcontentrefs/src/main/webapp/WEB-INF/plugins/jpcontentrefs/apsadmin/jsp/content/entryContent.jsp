<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<h1><s:text name="jacms.menu.contentAdmin" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">
<h2><s:text name="title.contentEditing" /></h2>
	<s:if test="hasFieldErrors()">
<div class="message message_error">
<h3><s:text name="message.title.FieldErrors" /></h3>	
	<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
		<li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
	</ul>
</div>
	</s:if>
<p class="noscreen"><s:text name="note.editContent" /></p> 	

<s:set var="myNameIsJack" value="true" />
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />

<%--
<h3><s:text name="note.flagsLegend" /></h3>
<dl class="table-display">
<dt><s:text name="Content.attribute.flag.mandatory.short" /></dt>
	<dd><s:text name="Content.attribute.flag.mandatory.full" /></dd>
<dt><s:text name="Content.attribute.flag.searcheable.short" /></dt>
	<dd><s:text name="Content.attribute.flag.searcheable.full" /></dd>
<dt><s:text name="Content.attribute.flag.indexed.short" /></dt>
	<dd><s:text name="Content.attribute.flag.indexed.full" /></dd>
<dt><s:text name="Content.attribute.flag.minLength.short" /></dt>
	<dd><s:text name="Content.attribute.flag.minLength.full" /></dd>
<dt><s:text name="Content.attribute.flag.maxLength.short" /></dt>
	<dd><s:text name="Content.attribute.flag.maxLength.full" /></dd>				
</dl>
--%>
<h3 class="noscreen" id="quickmenu"><s:text name="title.quickMenu" /></h3>
<ul class="menu horizontal tab-toggle-bar"><li><a href="#info" id="info_tab_quickmenu" class="tab-toggle"><abbr title="<s:text name="title.contentInfo" />"><s:text name="label.general" /></abbr></a></li><s:iterator value="langs" id="lang"><li><a href="#<s:property value="#lang.code" />_tab" class="tab-toggle"><s:property value="#lang.descr" /></a></li></s:iterator></ul>

<s:set name="removeIcon" id="removeIcon"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>

<s:form cssClass="tab-container action-form">
<div id="info" class="tab">
<h3 class="js_noscreen"><s:text name="title.contentInfo" /> (<a href="#quickmenu" id="info_content_goBackToQuickMenu"><s:text name="note.goBackToQuickMenu" /></a>)</h3>
<fieldset class="margin-bit-top"><legend><s:text name="label.info" /></legend>
<p>
	<label for="descr" class="basic-mint-label"><s:text name="label.description" />:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" id="descr" name="descr" value="%{content.descr}" maxlength="255" cssClass="text" />
</p> 
<p>
	<label for="mainGroup" class="basic-mint-label"><s:text name="label.ownerGroup" />:</label>
	<s:set name="lockGroupSelect" value="%{content.id != null || content.mainGroup != null}"></s:set>
	<wpsf:select useTabindexAutoIncrement="true" name="mainGroup" id="mainGroup" list="allowedGroups" value="content.mainGroup" 
		listKey="name" listValue="descr" disabled="%{lockGroupSelect}" cssClass="text" />
</p>

<p>
<label for="status" class="basic-mint-label"><s:text name="label.state" />:</label>
<wpsf:select useTabindexAutoIncrement="true" name="status" id="status" list="avalaibleStatus" value="%{content.status}" cssClass="text" listKey="key" listValue="%{getText(value)}" />
</p>
</fieldset>
<div class="subsection">
<!--  INIZIO BLOCCO SELEZIONE GRUPPI SUPPLEMENTARI ABILITATI ALLA VISUALIZZAZIONE -->
<fieldset>
	<legend><s:text name="label.extraGroups" /></legend>
<s:if test="content.groups.size != 0">
<ul>
<s:iterator value="content.groups" id="groupName">
	<li>
		<wpsa:actionParam action="removeGroup" var="actionName" >
			<wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
		</wpsa:actionParam>
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#removeIcon}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />: <s:property value="%{getGroupsMap()[#groupName].getDescr()}"/> 
	</li>
</s:iterator>
</ul>
</s:if>
<p>
	<label for="extraGroups" class="basic-mint-label"><s:text name="label.join" />&#32;<s:text name="label.group" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="extraGroupName" id="extraGroups" list="groups" 
		listKey="name" listValue="descr" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="joinGroup" value="%{getText('label.join')}" cssClass="button" />
</p>
</fieldset>
<!-- FINE BLOCCO SELEZIONE GRUPPI SUPPLEMENTARI ABILITATI ALLA VISUALIZZAZIONE -->

<!-- INIZIO CATEGORIE -->
<s:action name="showPrivateCategoryBlockOnEntryContent" namespace="/do/jacms/Content" executeResult="true"></s:action>
<!-- FINE CATEGORIE -->
</div>

<wpsa:hookPoint key="jacms.entryContent.tabGeneral" objectName="hookPointElements_jacms_entryContent_tabGeneral">
<s:iterator value="#hookPointElements_jacms_entryContent_tabGeneral" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</div>
<!-- START CICLO LINGUA -->
<s:iterator value="langs" id="lang">
<div id="<s:property value="#lang.code" />_tab" class="tab">
<h3 class="js_noscreen"><s:property value="#lang.descr" /> (<a class="backLink" href="#quickmenu" id="<s:property value="#lang.code" />_tab_quickmenu"><s:text name="note.goBackToQuickMenu" /></a>)</h3>

<!-- START CICLO ATTRIBUTI -->
<s:iterator value="content.attributeList" id="attribute">
<div class="contentAttributeBox contentAttribute-<s:property value="#attribute.type" />" id="<s:property value="%{'contentedit_'+#lang.code+'_'+#attribute.name}" />">
<%-- INIZIALIZZAZIONE TRACCIATORE --%>
<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
<p class="important">
	<s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /><span class="monospace">&#32;(<s:text name="label.list" />)</span>:
</p>
</s:if>
<s:elseif test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState' || #attribute.type == 'Composite'">
<p>
	<span class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</span>

</s:elseif>
<s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Hypertext' || #attribute.type == 'Attach' || #attribute.type == 'Number' || #attribute.type == 'Date' || #attribute.type == 'Link' || #attribute.type == 'Enumerator'">
<p>
	<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" class="basic-mint-label"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</label>
</s:elseif>

<s:if test="#attribute.type == 'Monotext'">
<!-- ############# ATTRIBUTO TESTO MONOLINGUA ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
</p>
</s:if>

<s:elseif test="#attribute.type == 'Text'">
<!-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Longtext'">
<!-- ############# ATTRIBUTO TESTOLUNGO ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Hypertext'">
<!-- ############# ATTRIBUTO HYPERTEXT ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Image'">
<!-- ############# ATTRIBUTO Image ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Attach'">
<!-- ############# ATTRIBUTO Attach ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'CheckBox'">
<!-- ############# ATTRIBUTO CheckBox ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Boolean'">
<!-- ############# ATTRIBUTO Boolean ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'ThreeState'">
<!-- ############# ATTRIBUTO ThreeState ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Number'">
<!-- ############# ATTRIBUTO Number ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
<!-- ############# ATTRIBUTO Date ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Link'">
<!-- ############# ATTRIBUTO Link ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Enumerator'">
<!-- ############# ATTRIBUTO TESTO Enumerator ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Monolist'">
<!-- ############# ATTRIBUTO Monolist ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/monolistAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'List'">
<!-- ############# ATTRIBUTO List ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Composite'">
<!-- ############# ATTRIBUTO Composite ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/compositeAttribute.jsp" />
</p>
</s:elseif>

<wpsa:hookPoint key="jacms.entryContent.attributeExtra" objectName="hookPointElements_jacms_entryContent_attributeExtra">
<s:iterator value="#hookPointElements_jacms_entryContent_attributeExtra" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</div>
</s:iterator>
<!-- END CICLO ATTRIBUTI -->

<s:set var="showingPageSelectItems" value="showingPageSelectItems"></s:set>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/content-preview.png</s:set>	
<wpsa:actionParam action="preview" var="previewActionName" >
	<wpsa:actionSubParam name="%{'jacmsPreviewActionLangCode_' + #lang.code}" value="%{#lang.code}" />
</wpsa:actionParam>
<div class="centerText margin-more-top">
<s:if test="!#showingPageSelectItems.isEmpty()">
	<s:set var="previewActionPageCodeLabelId">jacmsPreviewActionPageCode_<s:property value="#lang.code" /></s:set>
	<p><label for="<s:property value="#previewActionPageCodeLabelId" />"><s:text name="name.preview.page" /></label>: 
	<wpsf:select useTabindexAutoIncrement="true" name="%{'jacmsPreviewActionPageCode_' + #lang.code}" id="%{#previewActionPageCodeLabelId}" list="#showingPageSelectItems" listKey="key" listValue="value" />
	<%-- <wpsf:select useTabindexAutoIncrement="true" name="jacmsPreviewActionPageCode" id="%{#previewActionPageCodeLabelId}" list="#showingPageSelectItems" listKey="key" listValue="value" /></p>  --%>
	<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" action="%{#previewActionName}" value="%{getText('label.preview')}" title="%{getText('note.button.previewContent')}" /></p>
</s:if>
<s:else>
	<p><s:text name="label.preview.noPreviewPages" /></p>
	<p><wpsf:submit useTabindexAutoIncrement="true" cssClass="button" disabled="true" action="%{#previewActionName}" value="%{getText('label.preview')}" title="%{getText('note.button.previewContent')}" /></p>	
</s:else>
</div>

</div>
</s:iterator>
<!-- END CICLO LINGUA -->

<div class="subsection">

<h3 class="noscreen"><s:text name="title.contentActionsIntro" /></h3>

<fieldset><legend><s:text name="title.contentActions" /></legend>

<wpsa:hookPoint key="jacms.entryContent.actions" objectName="hookPointElements_jacms_entryContent_actions">
<s:iterator value="#hookPointElements_jacms_entryContent_actions" var="hookPointElement">
	<wpsa:include value="%{#hookPointElement.filePath}"></wpsa:include>
</s:iterator>
</wpsa:hookPoint>

</fieldset>
</div>
</s:form>
</div>