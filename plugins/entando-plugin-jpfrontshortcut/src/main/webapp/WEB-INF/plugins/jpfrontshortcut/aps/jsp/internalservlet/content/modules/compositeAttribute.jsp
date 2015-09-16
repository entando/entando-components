<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<div class="compositeAttribute">
<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set name="masterCompositeAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute">
<div class="compositeAttribute-element">
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>

<s:if test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
	<span class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</span>

</s:if>
<s:else>
	<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" class="basic-mint-label"><s:property value="#attribute.name"/><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</label>
</s:else>
		
		<s:if test="#attribute.type == 'Text'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
		</s:if>
		<s:elseif test="#attribute.type == 'Monotext'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
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
		<s:elseif test="#attribute.type == 'Longtext'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Enumerator'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'EnumeratorMap'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Number'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Date'">
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
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

		<%-- jacms attribute types --%>
		<s:elseif test="#attribute.type == 'Image'">
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/imageAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Attach'">
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/attachAttribute.jsp" />
		</s:elseif>
		<s:elseif test="#attribute.type == 'Link'">
			<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/linkAttribute.jsp" />
		</s:elseif>
</div>		
</s:iterator>
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set name="attribute" value="#masterCompositeAttribute" />
<s:set name="parentAttribute" value=""></s:set>
</div>