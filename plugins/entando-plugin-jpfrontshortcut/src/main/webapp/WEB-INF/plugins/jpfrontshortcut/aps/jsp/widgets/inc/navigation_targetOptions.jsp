<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpcs" uri="/jpfrontshortcut-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<c:if test="${sessionScope.currentUser != 'guest'}">
	<wp:pageInfo pageCode="${currentTarget.code}" info="owner" var="currentPageOwner" />
	<wp:ifauthorized permission="managePages" groupName="${currentPageOwner}">
		<wp:info key="systemParam" paramName="jpfrontshortcut_activePageFrontEndEditing" var="activePageEditingVar" />
		<c:if test="${null != activePageEditingVar && activePageEditingVar  == 'true'}">
			<wp:info key="currentLang" var="currentLang" />
			<wp:currentPage param="code" var="currentViewCode" />
			<wp:pageInfo pageCode="${currentTarget.code}" info="code" var="targetPageCode" />
			<wp:pageInfo pageCode="${currentTarget.code}" info="title" var="targetPageTitle" />
			<wp:pageInfo pageCode="${currentTarget.code}" info="hasChild" var="targetPageHasChildVar" />
			<wpcs:requestContextParam param="currentFrame" var="framePosVar" />
			<%-- NEW PAGE --%>
			<a id="<c:out value="options_anchor_NEW_PAGE_${targetPageCode}_${framePosVar}_${random}" />" href="javascript:void(0)" title="<wp:i18n key="jpfrontshortcut_NEWPAGE" escapeXml="true" />&#32;<c:out value="${currentTarget.title}" />"><img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/new.png" alt="<wp:i18n key="jpfrontshortcut_NEWPAGE" escapeXml="true" />&#32;<c:out value="${currentTarget.title}" />" /></a>
			<script type='text/javascript'>
				<!--//--><![CDATA[//><!--
				jQuery(document).ready(function () { 
					jQuery.struts2_jquery.bind(jQuery('#<c:out value="options_anchor_NEW_PAGE_${targetPageCode}_${framePosVar}_${random}" />'),{
						"opendialog": "widgetDialog",
						"jqueryaction": "anchor",
						"opendialogtitle": "<wp:i18n key="jpfrontshortcut_NEWPAGE" /><c:out value=" " /><c:out value="${targetPageTitle}" />",
						"id": "<c:out value="options_anchor_NEW_PAGE_${targetPageCode}_${framePosVar}_${random}" />",
						"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/new?langCode=<c:out value="${currentLang}" />&selectedNode=<c:out value="${targetPageCode}" />",
						"button": false
					});
				});
				//--><!]]>
			</script>
			<%-- EDIT PAGE --%>
			<a id="<c:out value="options_anchor_EDIT_PAGE_${targetPageCode}_${framePosVar}_${random}" />" href="javascript:void(0)" title="<wp:i18n key="jpfrontshortcut_EDITPAGE" escapeXml="true" />&#32;<c:out value="${currentTarget.title}" />"><img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/edit.png" alt="<wp:i18n key="jpfrontshortcut_EDITPAGE" escapeXml="true" />&#32;<c:out value="${currentTarget.title}" />" /></a>
			<script type='text/javascript'>
				<!--//--><![CDATA[//><!--
				jQuery(document).ready(function () { 
					jQuery.struts2_jquery.bind(jQuery('#<c:out value="options_anchor_EDIT_PAGE_${targetPageCode}_${framePosVar}_${random}" />'),{
						"opendialog": "widgetDialog",
						"jqueryaction": "anchor",
						"opendialogtitle": "<wp:i18n key="jpfrontshortcut_EDITPAGE" /><c:out value=" " /><c:out value="${targetPageTitle}" />",
						"id": "<c:out value="options_anchor_EDIT_PAGE_${targetPageCode}_${framePosVar}_${random}" />",
						"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/edit?langCode=<c:out value="${currentLang}" />&selectedNode=<c:out value="${targetPageCode}" />",
						"button": false
					});
				});
				//--><!]]>
			</script>
			<%-- DELETE PAGE --%>
			<c:if test="${currentViewCode != targetPageCode && !targetPageHasChildVar}">
				<a id="<c:out value="options_anchor_DELETE_PAGE_${targetPageCode}_${framePosVar}_${random}" />" href="javascript:void(0)" title="<wp:i18n key="jpfrontshortcut_DELETEPAGE" escapeXml="true" />&#32;<c:out value="${currentTarget.title}" />"><img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/delete.png" alt="<wp:i18n key="jpfrontshortcut_DELETEPAGE" escapeXml="true" />&#32;<c:out value="${currentTarget.title}" />" /></a>
				<script type='text/javascript'>
					<!--//--><![CDATA[//><!--
					jQuery(document).ready(function () { 
						jQuery.struts2_jquery.bind(jQuery('#<c:out value="options_anchor_DELETE_PAGE_${targetPageCode}_${framePosVar}_${random}" />'),{
							"opendialog": "widgetDialog",
							"jqueryaction": "anchor",
							"opendialogtitle": "<wp:i18n key="jpfrontshortcut_DELETEPAGE" /><c:out value=" " /><c:out value="${targetPageTitle}" />",
							"id": "<c:out value="options_anchor_DELETE_PAGE_${targetPageCode}_${framePosVar}_${random}" />",
							"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/trash?langCode=<c:out value="${currentLang}" />&selectedNode=<c:out value="${targetPageCode}" />",
							"button": false
						});
					});
					//--><!]]>
				</script>
			</c:if>
		</c:if>
	</wp:ifauthorized>
</c:if>
