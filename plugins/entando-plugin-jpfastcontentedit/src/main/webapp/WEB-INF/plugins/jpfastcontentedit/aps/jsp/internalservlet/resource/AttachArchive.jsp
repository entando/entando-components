<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/jquery.entando.js" />

<c:set var="category_tree_js_code">
	$(document).ready(function() {
		jQuery("#categoryTree").EntandoWoodMenu({
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
<form 
	class="form-horizontal"
	action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/search.action" />" 
	method="post"
	>
	<p class="noscreen">
		<wpsf:hidden name="resourceTypeCode" />
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>
	<fieldset class="well well-small"> 
		<legend><wp:i18n key="jpfastcontentedit_SEARCH_FILTERS" /></legend>
		<div class="control-group">
			<label class="control-label" for="jpfastcontentedit-search-text"><wp:i18n key="jpfastcontentedit_DESCRIPTION" /></label>
			<div class="controls">
				<wpsf:textfield 
					useTabindexAutoIncrement="true" 
					name="text" 
					id="jpfastcontentedit-search-text" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><wp:i18n key="jpfastcontentedit_CATEGORY" /></label>
			<div class="controls">
				<ul id="categoryTree" class="jpfastcontentedit-tree unstyled">
					<s:set name="inputFieldName" value="'categoryCode'" />
					<s:set name="selectedTreeNode" value="selectedNode" />
					<s:set name="liClassName" value="'category'" />
					<s:set name="currentRoot" value="categoryRoot" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
				</ul>
			</div>
		</div>
		<p class="form-actions">
			<wp:i18n key="jpfastcontentedit_SEARCH" var="search_label" />
			<wpsf:submit useTabindexAutoIncrement="true" value="%{#attr.search_label}" cssClass="btn" />
		</p>
	</fieldset>
	<fieldset>
		<legend><wp:i18n key="jpfastcontentedit_CHOOSE_ATTACH" /></legend>
		<%--
		<s:set var="res" value="#{
			10:7,
			11:10,
			12:14,
			13:15,
			14:13,
			15:11,
			16:12,
			17:7,
			18:10,
			19:14,
			20:15,
			21:13,
			22:11,
			23:12,
			24:7,
			25:10,
			26:14,
			27:15,
			28:13,
			29:11
		}" /><%-- #res.values resources --%>
		<wpsa:subset source="resources" count="12" objectName="groupResource" advanced="true" offset="5">
				<s:set name="group" value="#groupResource" />
				<%--
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				--%>
				<div class="row-fluid">
					<div class="span12">
						<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/resource/inc/pagerBlock.jsp" />
					</div>
				</div>

				<s:iterator var="resourceid" status="status">

					<s:if test="#status.count == 1 || #status.count == 7">
						<div class="row-fluid">
					</s:if>

					<s:set var="resource" value="%{loadResource(#resourceid)}" />
					<s:set var="resourceInstance" value="%{#resource.getInstance()}" />
					
					<div class="span2">
						<dl class="media pull-left clearfix">
							<dt class="pull-left">
								<a href="<s:property value="%{#resource.documentPath}" />" >
									<img  class="media-object" 
										src="<wp:resourceURL/>administration/common/img/icons/resourceTypes/<s:property value="%{getIconFile(#resourceInstance.fileName)}"/>" alt="<s:property value="%{#resourceInstance.fileName}"/>" title="<s:property value="%{#resourceInstance.fileName}"/>" />
								</a>
								<s:property value="%{#resourceInstance.fileLenght}"/>
							</dt>
							<dt class="media-body">
								<a class="media-heading btn btn-primary btn-small" href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/joinResource.action" >
										<wp:parameter name="resourceId"><s:property value="%{#resourceid}"/></wp:parameter>
										<wp:parameter name="contentOnSessionMarker"><s:property value="%{contentOnSessionMarker}" /></wp:parameter>
									</wp:action>#<s:property value="currentAttributeLang" />_tab" title="<wp:i18n key="jpfastcontentedit_JOIN_TO" />: <s:property value="content.descr" />" ><wp:i18n key="jpfastcontentedit_JOIN" /></a>
							</dt>
							<dd class="media-body">
								<p><s:property value="#resource.descr" /></p>
							</dd>
						</dl>
					</div>

					<s:if test="#status.count == 6 || #status.count == 12 || #status.last">
						</div>
					</s:if>
				</s:iterator>

				<div class="row-fluid">
					<div class="span12">
						<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/resource/inc/pagerBlock.jsp" />
					</div>
				</div>
		</wpsa:subset>
	</fieldset>
	<fieldset class="well well-small">
		<legend><wp:i18n key="jpfastcontentedit_LABEL_NEW_RESOURCE" /></legend>
		<p>
			<a class="btn btn-primary" href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/new.action">
					<wp:parameter name="resourceTypeCode">Attach</wp:parameter>
					<wp:parameter name="contentOnSessionMarker"><s:property value="%{contentOnSessionMarker}" /></wp:parameter>
				</wp:action>">
				<wp:i18n key="jpfastcontentedit_LABEL_NEW_ATTACH" />
			</a>
		</p>
	</fieldset>
	<p>
		<a class="btn btn-link"
			href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/backToEntryContent.action">
			<wp:parameter name="contentOnSessionMarker"><s:property value="%{contentOnSessionMarker}" /></wp:parameter>
			</wp:action>
			" >
			<wp:i18n key="jpfastcontentedit_BACK_TO_CONTENT" />
		</a>
	</p>
</form>