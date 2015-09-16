<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<s:set var="operationButtonDisabled" value="false" />
<p class="noscreen"><s:text name="note.monolist.intro" /></p>

<s:if test="#attribute.attributes.size() != 0">
<ul class="list-container">
</s:if>
<s:set name="masterListAttributeTracer" value="#attributeTracer" />
<s:set name="masterListAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
<s:set name="attributeTracer" value="#masterListAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>

<s:set name="elementIndex" value="#elementStatus.index" />

	<s:if test="#attribute.type == 'Composite'">
<li class="list-item">	
		<span class="important" title="<s:text name="label.compositeAttribute.element" />"><s:property value="#elementStatus.index + 1" /></span>
		<s:if test="!#lang.default">
			<s:set var="operationButtonDisabled" value="true" />
		</s:if>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/listAttributes/allList_operationModule.jsp" />
	</s:if>
	<s:else>
<li class="list-item">		
		<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#elementStatus.index + 1" /></label>
		<s:if test="!#lang.default">
			<s:set var="operationButtonDisabled" value="true" />
		</s:if>
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/listAttributes/allList_operationModule.jsp" />
	</s:else>	 
	
	<s:if test="#attribute.type == 'Monotext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Text'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Longtext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Hypertext'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
		<%-- hypertext javascript editor --%>
		<script type="text/javascript">
			<!--//--><![CDATA[//><!--
			jQuery(document).ready(function () { 
				var textareaOriginalWidth = jQuery("#<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />").width()+"px";
				try {
					CKEDITOR.remove(CKEDITOR.instances["<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"]);
				} catch(e) {}
				try {
					CKEDITOR.editor.prototype.destroy(CKEDITOR.instances["<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"]);
				} catch(e) {}
				var currentEditor = CKEDITOR.replace("<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />", {
					customConfig : '<wp:resourceURL />plugins/jpfrontshortcut/static/js/ckeditor/entando-ckeditor_config.js',
					EntandoLinkActionPath: "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/Hypertext/entandoInternalLink.action",
					language: '<s:property value="locale" />',
					width: textareaOriginalWidth
				});
				currentEditor.on('change', function (ev) {
					ev.editor.updateElement();
				});
			});
			//--><!]]>
		</script>
	</s:elseif>
	<s:elseif test="#attribute.type == 'Enumerator'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'EnumeratorMap'">
		<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Image'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/imageAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Attach'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/attachAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Link'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/linkAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Composite'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/compositeAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Boolean'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/booleanAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/checkBoxAttribute.jsp" />
	</s:elseif>
</li>
</s:iterator>

<s:set name="attributeTracer" value="#masterListAttributeTracer" />
<s:set name="attribute" value="#masterListAttribute" />
<s:set name="elementIndex" value="" />
<s:if test="#attribute.attributes.size() != 0">
</ul>
</s:if>

<s:if test="#lang.default">
<p class="button-align"><s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/include/listAttributes/allList_addElementButton.jsp" /></p>
</s:if>