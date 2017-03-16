<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="currentAttachedFileVar" value="#attribute.attachedFile"></s:set>
<s:if test="#lang.default">
<%-- Lingua di DEFAULT --%>
	<s:if test="#currentAttachedFileVar != null">
		<a class="btn btn-default" href="<s:url action="viewFileResource" namespace="/do/jpwebform/Message/Operator" >
			<s:param name="id" value="%{#currentAttachedFileVar.id}" />
		</s:url>">
                    <span class="icon fa fa-globe" title="<s:property value="%{#currentAttachedFileVar.filename}"/>"></span>
                </a>
		<s:property value="#currentAttachedFileVar.filename" />
		<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
			<wpsa:actionParam action="removeFileResource" var="removeFileResourceActionName" >
				<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
				<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
				<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
			</wpsa:actionParam>
			<wpsf:submit type="button" action="%{#removeFileResourceActionName}" 
				 title="%{getText('label.remove')}" cssClass="btn btn-warning">
                            <span class="icon fa fa-times-circle-o"></span>
                        </wpsf:submit>
		</s:if>
	</s:if>
</s:if>
<s:else>
	<%-- Lingua NON di DEFAULT --%>
        <div class="alert alert-info"><s:text name="note.editEntity.doThisInTheDefaultLanguage.must" />.</div>
</s:else>