<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />

<h3><wp:i18n key="jpsharedocs_EDIT_STEP3" /></h3>

<p>
	<wp:i18n key="jpsharedocs_EDIT_GROUPS" />
</p>

<form action="<wp:action path="/ExtStr2/do/jpsharedocs/Document/submitGroups" />" method="post" enctype="multipart/form-data" >
	
	<s:if test="hasActionErrors()">
		<h4><wp:i18n key="ERRORS" /></h4>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</s:if>
	
	<s:if test="hasFieldErrors()">
		<h4><wp:i18n key="ERRORS" /></h4>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	
	<p class="noscreen">
		<wpsf:hidden name="step" value="3" />
		<s:if test="%{isEdit()}"><wpsf:hidden name="contentId" /></s:if>
		<wpsf:hidden name="title" />
		<wpsf:hidden name="author" />
		<wpsf:hidden name="description" />
		<wpsf:hidden name="docDescription" />
		<wpsf:hidden name="documentContentType" />
		<wpsf:hidden name="documentFileName" />
		<wpsf:hidden name="tmpFileName" />
		<s:iterator value="categories" id="categoryCode" status="rowstatus">
			<wpsf:hidden name="categories" value="%{#categoryCode}" id="categories-%{#rowstatus.index}"/>
		</s:iterator>
		<s:iterator value="extraGroups" id="groupName" status="rowstatus">
			<wpsf:hidden name="extraGroups" value="%{#groupName}" id="extraGroups-%{#rowstatus.index}"/>
		</s:iterator>
	</p>
	
	<s:if test="%{!isEdit()}">
		<p> 
			<label for="jpsharedocs_doc_maingroup"><wp:i18n key="jpsharedocs_DOC_MAINGROUP" /></label><br /> 
			<wpsf:select name="mainGroup" id="jpsharedocs_doc_maingroup" list="allowedGroups" listKey="name" listValue="descr" cssClass="text" />
		</p>
	</s:if>
	<s:else>
		<p> 
			<wp:i18n key="jpsharedocs_DOC_MAINGROUP" />:&#32;<s:property value="content.mainGroup" />
		</p>
	</s:else>
	
	<%--  INIZIO BLOCCO SELEZIONE GRUPPI SUPPLEMENTARI ABILITATI ALLA VISUALIZZAZIONE --%>
	
	<p><wp:i18n key="jpsharedocs_DOC_EXTRA_GROUPS"/><span class="noscreen">:</span></p>
		<s:if test="extraGroups.size != 0">
			<ul>
			<s:iterator value="extraGroups" id="groupName">
				<li>
					<wpsa:actionParam action="removeGroup" var="actionName" >
						<wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
					</wpsa:actionParam>
					<s:set name="removeGroupLabel"><wp:i18n key="jpsharedocs_REMOVE_GROUP" /></s:set>
					<s:set var="iconImagePath" ><wp:resourceURL />plugins/jpsharedocs/static/img/list-remove.png</s:set>
					<wpsf:submit action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{#removeGroupLabel}" title="%{#removeGroupLabel}" />: <s:property value="%{getGroup(#groupName).getDescr()}"/> 
				</li>
			</s:iterator>
			</ul>
		</s:if>
		<s:else>
			<p><wp:i18n key="jpsharedocs_DOC_NO_EXTRA_GROUPS" /></p> 
		</s:else>
	<p>
		<s:set name="joinGroupLabel"><wp:i18n key="jpsharedocs_JOIN_GROUP" /></s:set>
		<label for="extraGroupName"><s:property value="#joinGroupLabel" /></label>:<br />
		<wpsf:select name="extraGroupName" id="extraGroupName" list="groups" listKey="name" listValue="descr" cssClass="text" />&#32;
		<s:set name="joinLabel"><wp:i18n key="jpsharedocs_JOIN" /></s:set>
		<wpsf:submit action="joinGroup" value="%{#joinLabel}" cssClass="button" />
	</p>
	
	<%-- FINE BLOCCO SELEZIONE GRUPPI SUPPLEMENTARI ABILITATI ALLA VISUALIZZAZIONE --%>
	
	<p>
		<s:set name="backLabel" ><wp:i18n key="jpsharedocs_EDIT_PREV_STEP" /></s:set>
		<wpsf:submit cssClass="button" action="compileCategories" value="%{#backLabel}" />&#32;
		<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_EDIT_NEXT_STEP" /></s:set> 
		<wpsf:submit cssClass="button" value="%{#submitLabel}" />
	</p>
	
</form>