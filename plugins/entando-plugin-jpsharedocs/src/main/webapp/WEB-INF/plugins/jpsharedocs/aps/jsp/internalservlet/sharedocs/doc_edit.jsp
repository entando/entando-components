<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />

<h3><wp:i18n key="jpsharedocs_EDIT_STEP1" /></h3>

<p><wp:i18n key="jpsharedocs_EDIT_INFO" /></p> 

<form action="<wp:action path="/ExtStr2/do/jpsharedocs/Document/submitFields" />" method="post" enctype="multipart/form-data" >
	
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
		<wpsf:hidden name="step" value="1" />
		<s:if test="%{isEdit()}">
			<wpsf:hidden name="contentId" />
			<wpsf:hidden name="author" />
			<wpsf:hidden name="title" />
		</s:if>
		<s:if test="%{tmpFileName!=null && document!=null}">
			<wpsf:hidden name="documentContentType" />
			<wpsf:hidden name="documentFileName" />
			<wpsf:hidden name="tmpFileName" />
		</s:if>
		
		<s:iterator value="categories" id="categoryCode" status="rowstatus">
			<wpsf:hidden name="categories" value="%{#categoryCode}" id="categories-%{#rowstatus.index}"/>
		</s:iterator>
		<s:iterator value="extraGroups" id="groupName" status="rowstatus">
			<wpsf:hidden name="extraGroups" value="%{#groupName}" id="extraGroups-%{#rowstatus.index}"/>
		</s:iterator>
		<wpsf:hidden name="mainGroup" />
	</p>
	
	<%--
		nuovo documento:
			Title (edit)
			Description Document (edit)
			Author (edit)
			Document (edit)
			Description Versione (edit)

		modifica documento:
			Title (no)
			Description Document (edit)
			Author (no)
			Document (edit)
			Description Versione (edit)
	 --%>
	
	<%-- titolo --%>
	<p>
		<s:if test="%{isEdit()}">
			<wp:i18n key="jpsharedocs_DOC_TITLE" />: <s:property value="title" />
		</s:if>
		<s:else>
			<label for="jpsharedocs_doc_title"><wp:i18n key="jpsharedocs_DOC_TITLE" /></label><br />
			<wpsf:textfield cssClass="text" id="jpsharedocs_doc_title" name="title" />
		</s:else>
	</p>
	
	<%-- descrizione documento (sempre visibile) --%>
	<p> 
		<label for="jpsharedocs_doc_description"><wp:i18n key="jpsharedocs_DOC_DESCRIPTION" /></label><br />
		<wpsf:textarea cols="15" rows="5" cssClass="text" id="jpsharedocs_doc_description" name="description" />
	</p>
	
	
	<%-- autore --%>
	<p>
		<s:if test="%{!isEdit()}">
			<label for="jpsharedocs_doc_author"><wp:i18n key="jpsharedocs_DOC_AUTHOR" /></label><br />
			<wpsf:textfield cssClass="text" id="jpsharedocs_doc_author" name="author" />
		</s:if>
		<s:else>
				<wp:i18n key="jpsharedocs_DOC_AUTHOR" />:&#32; <s:property value="author" />
		</s:else>
	</p>
	
	<s:if test="%{tmpFileName!=null && document!=null}">
		<%-- versione, sempre presente cambiano le etichette --%>
		<p>
			<s:if test="%{!isEdit()}">
				<label for="jpsharedocs_doc_document"><wp:i18n key="jpsharedocs_DOC_DOCUMENT" /></label><br />
				<s:property value="documentFileName"/>
			</s:if>
			<s:else>
				<label for="jpsharedocs_doc_document"><wp:i18n key="jpsharedocs_DOC_NEW_VERSION" /></label><br />
				<s:property value="documentFileName"/>
			</s:else>
			&nbsp;
			<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_DOC_REMOVE_ATTACH" /></s:set> 
			<s:set var="iconImagePath" ><wp:resourceURL />plugins/jpsharedocs/static/img/delete.png</s:set>
			<wpsf:submit type="image" action="releaseDocument" value="%{#submitLabel}" title="%{#submitLabel}" src="%{iconImagePath}" />
		</p>
	</s:if>
	<s:else>
		<%-- versione, sempre presente cambiano le etichette --%>
		<p>
			<s:if test="%{!isEdit()}">
					<label for="jpsharedocs_doc_document"><wp:i18n key="jpsharedocs_DOC_DOCUMENT" /></label><br />
					<input id="jpsharedocs_doc_document" type="file" name="document" value="" tabindex="<wpsa:counter/>" />
			</s:if>
			<s:else>
					<label for="jpsharedocs_doc_document"><wp:i18n key="jpsharedocs_DOC_NEW_VERSION" /></label><br />
					<input id="jpsharedocs_doc_document" type="file" name="document" value="" tabindex="<wpsa:counter/>" />
			</s:else>
		</p>
	</s:else>
	
	<%-- descrizione versione, sempre presente cambiano le etichette --%>
	<p>
		<s:if test="%{!isEdit()}">
			<label for="jpsharedocs_doc_version_descr"><wp:i18n key="jpsharedocs_DOC_VERSION_DESCR" /></label><br />
			<wpsf:textfield cssClass="text" id="jpsharedocs_doc_version_descr" name="docDescription" />
		</s:if>
		<s:else>
			<label for="jpsharedocs_doc_version_descr"><wp:i18n key="jpsharedocs_DOC_NEWVERSION_DESCR" /></label><br />
			<wpsf:textfield cssClass="text" id="jpsharedocs_doc_version_descr" name="docDescription" /> 
		</s:else>
	</p>			

	<p>
		<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_EDIT_NEXT_STEP" /></s:set>
		<wpsf:submit cssClass="button" value="%{#submitLabel}" />
	</p>
	
</form>