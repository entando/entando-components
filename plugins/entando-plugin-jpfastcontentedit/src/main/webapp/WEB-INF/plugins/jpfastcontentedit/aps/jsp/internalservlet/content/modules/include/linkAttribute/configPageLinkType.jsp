<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/jquery.entando.js" />
<c:set var="category_tree_js_code">
	$(document).ready(function() {
		jQuery("#pageTree").EntandoWoodMenu({
			menuToggler: "subTreeToggler",
			menuRetriever: function(toggler) {
				return $(toggler).parent().children("ul");
			},
			showTools: true, 
			onOpen: function(toggler, menu) {
				//console.log("open", toggler, menu);
			},
			onClose: function(toggler, menu) {
				//console.log("close", toggler, menu);
			},
			onStart: function() {
				this.collapseAll();
			},
			type: "tree",
			openClass: "node_open",
			closedClass: "node_closed",
			expandAllLabel: "<s:text name="label.expandAll" />",
			collapseAllLabel: "<s:text name="label.collapseAll" />",
			toolTextIntro: "<s:text name="label.introExpandAll" />",
			toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
			toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />",
			<s:if test="%{selectedNode != null && !(selectedNode.equalsIgnoreCase(''))}">
				startIndex: "fagianonode_<s:property value="selectedNode" />"
			</s:if>
		});
	});
</c:set>
<wp:headInfo type="JS_RAW" info="${category_tree_js_code}" />
<wp:headInfo type="CSS" info="../../plugins/jpfastcontentedit/static/css/tree.css" />

<h1><wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE" /></h1>

<s:include value="linkAttributeConfigIntro.jsp" />
<h2><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_2_OF_2" /></h2>
<s:include value="linkAttributeConfigReminder.jsp" />
<p>
	<wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_TO_PAGE" />
	<s:if test="contentId != null">&#32;<wp:i18n key="jpfastcontentedit_FOR_THE_CONTENT" />: <s:property value="contentId"/> &ndash; <s:property value="%{getContentVo(contentId).descr}"/></s:if>.
</p>
<form 
	class="form-horizontal"
	action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/joinPageLink.action" />" 
	method="post" >

		<s:if test="hasFieldErrors()">
			<div class="alert alert-block">
				<p><strong><wp:i18n key="jpfastcontentedit_ERRORS" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
							<s:iterator value="value">
									<li><s:property escape="false" /></li>
							</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<p class="noscreen">
			<wpsf:hidden name="contentOnSessionMarker" />
			<s:if test="contentId == null">
				<wpsf:hidden name="linkType" value="2" />
			</s:if>
			<s:else>
				<wpsf:hidden name="contentId" />
				<wpsf:hidden name="linkType" value="4" />
			</s:else>
		</p>

		<fieldset>
			<legend><wp:i18n key="jpfastcontentedit_PAGE_TREE" /></legend>
			<ul id="pageTree" class="unstyled jpfastcontentedit-tree">
				<s:set name="inputFieldName" value="'selectedNode'" />
				<s:set name="selectedTreeNode" value="selectedNode" />
				<s:set name="liClassName" value="'page'" />
				<s:set name="currentRoot" value="treeRootNode" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
			</ul>
		</fieldset>

		<wp:i18n key="jpfastcontentedit_CONFIRM" var="jpfastcontentedit_CONFIRM"/>
		<p class="form-actions">
			<wpsf:submit 
				useTabindexAutoIncrement="true" 
				value="%{#attr.jpfastcontentedit_CONFIRM}" 
				title="%{#attr.jpfastcontentedit_CONFIRM}" 
				cssClass="btn btn-primary" />
			&emsp;
			<a class="btn btn-link"
				href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/backToEntryContent.action">
				<wp:parameter name="contentOnSessionMarker"><s:property value="%{contentOnSessionMarker}" /></wp:parameter>
				</wp:action>
				" >
				<wp:i18n key="jpfastcontentedit_BACK_TO_CONTENT" />
			</a>
		</p>
</form>