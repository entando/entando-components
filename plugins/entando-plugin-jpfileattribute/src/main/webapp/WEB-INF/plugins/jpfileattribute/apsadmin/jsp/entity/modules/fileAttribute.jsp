<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:set var="currentAttachedFileVar" value="#attribute.attachedFile"></s:set>

<s:if test="#lang.default">
<%-- Lingua di DEFAULT --%>
	<s:if test="#currentAttachedFileVar != null">
		<a class="noborder" href="<s:url action="viewFileResource" namespace="/do/jpfileattribute/Content/Resource" >
			<s:param name="parentAttributeName" value="%{#parentAttribute.name}" />
			<s:param name="attributeName" value="%{#attribute.name}" />
			<s:param name="elementIndex" value="%{#elementIndex}" />
		</s:url>"><img class="noborder" src="<wp:resourceURL/>administration/common/img/icons/resourceTypes/22x22/<s:property value="%{getIconFile(#currentAttachedFileVar.filename)}"/>" alt="<s:property value="%{#currentAttachedFileVar.filename}"/>" title="<s:property value="%{#currentAttachedFileVar.filename}"/>" /></a>
		<s:property value="#currentAttachedFileVar.filename" />
		<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
			<wpsa:actionParam action="removeFileResource" var="removeFileResourceActionName" >
				<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
				<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
				<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
			</wpsa:actionParam>
			<s:set var="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
			<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#removeFileResourceActionName}" 
				value="%{getText('label.remove')}" title="%{getText('label.remove')}" src="%{#iconImagePath}" />
		</s:if>
	</s:if>
	<s:else>
		<wpsa:actionParam action="loadFileResource" var="loadFileResourceActionName" >
			<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
			<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
			<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
		</wpsa:actionParam>
		<s:set var="iconImagePath" ><wp:resourceURL/>administration/common/img/icons/22x22/attachment.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#loadFileResourceActionName}" 
			value="%{getText('jpfileattribute.label.load')}" title="%{#attribute.name + ': ' + getText('jpfileattribute.label.load')}" src="%{#iconImagePath}" />
	</s:else>
</s:if>
<s:else>
	<%-- Lingua NON di DEFAULT --%>
	<s:text name="note.editEntity.doThisInTheDefaultLanguage.must" />.
</s:else>