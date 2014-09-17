<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<jacms:contentInfo param="authToEdit" var="canEditThisVar" />
<jacms:contentInfo param="contentId" var="contentIdVar" />
<c:if test="${canEditThisVar}">
<div class="bar-content-edit">
	<p>
		<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/edit.action?contentId=<c:out value="${contentIdVar}" />" title="<wp:i18n key="EDIT_THIS_CONTENT" />" class="btn btn-info">
		<!--&dArr;-->
                <wp:i18n key="EDIT_THIS_CONTENT" />
                <span class="icon icon-edit icon-white"></span>
		<!--&dArr;-->
		</a>
		<c:if test="${canEditThisVar}">
<!--			&#32;|&#32;-->
			<wp:info key="systemParam" paramName="jpfrontshortcut_activeContentFrontEndEditing" var="activeContentEditingVar" />
			<c:if test="${null != activeContentEditingVar && activeContentEditingVar  == 'true'}">
				<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
				<%-- modal popup //start--%>
				<a id="<c:out value="options_anchor_${contentIdVar}_${random}" />" href="javascript:void(0)"><wp:i18n key="jpfrontshortcut_EDIT_THIS_CONTENT" escapeXml="true" /></a>
				<script type='text/javascript'>
					<!--//--><![CDATA[//><!--
					jQuery(document).ready(function () { 
						jQuery.struts2_jquery.bind(jQuery('#<c:out value="options_anchor_${contentIdVar}_${random}" />'),{
							"opendialog": "widgetDialog",
							"jqueryaction": "anchor",
							"opendialogtitle": "<wp:i18n key="jpfrontshortcut_EDIT_THIS_CONTENT" /><c:out value=" " /><c:out value="${contentIdVar}" />",
							"id": "<c:out value="options_anchor_${contentIdVar}_${random}" />",
							"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Content/introView?contentId=<c:out value="${contentIdVar}" />&modelId=<jacms:contentInfo param="modelId" />&request_locale=<wp:info key="currentLang" />&langCode=<wp:info key="currentLang" />",
							"button": false
						});
					 });
					//--><!]]>
				</script>
				<%-- modal popup //end--%>
			</c:if>
		</c:if>
	</p>
</div>
</c:if>
<jacms:content publishExtraTitle="true" />