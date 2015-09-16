<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="form-container" class="widget_form jpfrontshortcut"> 
<s:if test="hasFieldErrors()">
	<div class="alert">
		<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
		<ul class="unstyled">
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
			<li><s:property escape="false" /></li>
			</s:iterator>
		</s:iterator>
		</ul>
	</div>
</s:if>	

<s:form action="save" id="formform" theme="simple">
	<wpsf:hidden name="contentOnSessionMarker" />
	<s:set var="lang" value="currentLang" scope="action" />
	<c:set var="i18n_prefix">jpfrontshortcut_<s:property value="content.typeCode" /></c:set>
	<s:iterator value="content.attributeList" var="attribute">
	<s:if test="#attribute.active">
		<div class="attribute <s:property value="#attribute.type.toLowerCase()"/>">
			<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
			<p>
				<label class="attribute-label">
					<s:set  var="attributeName" value="#attribute.name" scope="page"/>
					<wp:i18n key="${i18n_prefix}_${attributeName}" />
					<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
				</label>
 				<br class="attribute-label-separator" />
 				
			<s:if test="#attribute.type == 'Monotext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
				</p>
			</s:if>

			<s:elseif test="#attribute.type == 'Text'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Longtext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Hypertext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
				</p>

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

			<s:elseif test="#attribute.type == 'Image'">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/imageAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Attach'">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/attachAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'CheckBox'">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/checkBoxAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Boolean'">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/booleanAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'ThreeState'">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/threeStateAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Number'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Date'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
				</p>
			</s:elseif>

			<s:elseif test="#attribute.type == 'Link'">
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/linkAttribute.jsp" />
				</p>
			</s:elseif>
			
			<s:elseif test="#attribute.type == 'Enumerator'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
				</p>
			</s:elseif>
			
			<s:elseif test="#attribute.type == 'EnumeratorMap'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
				</p>
			</s:elseif>
			
			<s:elseif test="#attribute.type == 'Monolist'">
				</p>
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/monolistAttribute.jsp" />
			</s:elseif>

			<s:elseif test="#attribute.type == 'List'">
				</p>
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/listAttribute.jsp" />
			</s:elseif>

			<s:elseif test="#attribute.type == 'Composite'">
				</p>
				<s:include value="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/internalservlet/content/modules/compositeAttribute.jsp" />
			</s:elseif>
		</div>
	</s:if>
</s:iterator>

<p class="save">
	<sj:submit 
			targets="form-container" 
			value="%{getText('label.save')}" 
			indicator="indicator"
			button="true"
	/>
</p>

</s:form>
</div>