<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<s:if test="#categoryTreeStyleVar == 'classic'">
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
</s:if>

<h1><wp:i18n key="jpfastcontentedit_LABEL_NEW_RESOURCE" /></h1>
	
	<form 
		class="form-horizontal"
		action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/save.action" />"  
		enctype="multipart/form-data" 
		method="post"
		>
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
			<wpsf:hidden name="strutsAction" />
			<wpsf:hidden name="resourceTypeCode" />
			<wpsf:hidden name="resourceId" />
			<wpsf:hidden name="contentOnSessionMarker" />

			<s:iterator value="categoryCodes" var="categoryCode" status="rowstatus">
				<input 
					type="hidden" 
					name="categoryCodes" 
					value="<s:property value="#categoryCode" />" 
					id="categoryCodes-<s:property value="#rowstatus.index" />" />
			</s:iterator>
			<s:if test="#categoryTreeStyleVar == 'request'">
				<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
					<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
				</s:iterator>
			</s:if>
		</p>
		
		<fieldset>
			<legend><wp:i18n key="jpfastcontentedit_CATEGORIES_MANAGEMENT" /></legend>
			
			
			<div class="control-group">
				<label class="control-label" for="categories"><s:text name="label.join" />&#32;<s:text name="label.category" /></label>
				<div class="controls">
					<s:set name="removeIcon" var="removeIcon"><wp:resourceURL/>administration/common/img/icons/list-remove.png</s:set>
					<s:set var="contentCategories" value="%{categoryCodes}" />
					<s:if test="#contentCategories != null && #contentCategories.size() > 0">
						<ul class="unstyled">
							<s:iterator value="#contentCategories" var="contentCategory">
								<s:set var="contentCategory" value="%{getCategory(#contentCategory)}" />
								<li>
									<wpsa:actionParam action="removeCategory" var="actionName" >
										<wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
									</wpsa:actionParam>
									<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#removeIcon}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />: <s:property value="#contentCategory.getFullTitle(currentLang.code)"/>
								</li>
							</s:iterator>
						</ul>
					</s:if>

					<wp:categories var="categoriesVar" titleStyle="full" />
					<wpsf:select list="%{#attr.categoriesVar}" name="categoryCode" listKey="key" listValue="value" />
					<wp:i18n key="jpfastcontentedit_JOIN" var="jpfastcontentedit_JOIN" />
					<wpsf:submit 
						useTabindexAutoIncrement="true" 
						action="joinCategory" 
						value="%{#attr.jpfastcontentedit_JOIN}" 
						cssClass="btn" />
				</div>
			</div>
		</fieldset>

		<fieldset>
			<legend><wp:i18n key="jpfastcontentedit_INFO" /></legend>
			<div class="control-group">
				<label class="control-label"  for="jpfastcontentedit-new-descr">
					<wp:i18n key="jpfastcontentedit_DESCRIPTION" />
					<abbr title="<wp:i18n key="jpfastcontentedit_REQUIRED_FULL" />"><wp:i18n key="jpfastcontentedit_REQUIRED_SHORT" /></abbr>
				</label>
				<div class="controls">
					<wpsf:textfield 
						useTabindexAutoIncrement="true" 
						name="descr" 
						id="jpfastcontentedit-new-descr" 
						/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jpfastcontentedit-new-mainGroup"><wp:i18n key="jpfastcontentedit_GROUP" /></label>
				<div class="controls">
					<wpsf:select 
						useTabindexAutoIncrement="true" 
						name="mainGroup" 
						id="jpfastcontentedit-new-mainGroup" 
						list="allowedGroups" 
						value="mainGroup"	
						listKey="name" 
						listValue="descr" 
						 />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jpfastcontentedit-new-upload">
					<wp:i18n key="jpfastcontentedit_FILE" />&#32;
					<abbr title="<wp:i18n key="jpfastcontentedit_REQUIRED_FULL" />">
					<wp:i18n key="jpfastcontentedit_REQUIRED_SHORT" /></abbr>
				</label>
				<div class="controls">
					<s:file name="upload" id="jpfastcontentedit-new-upload" label="label.file"/>
				</div>
			</div>
			<%--
			<div class="control-group">
				<label class="control-label" for="jpfastcontentedit-new-normalizeFileName"><s:text name="label.normalize" />&#32;<s:text name="label.filename" /></label>
				<div class="controls">
					<wpsf:checkbox 
						useTabindexAutoIncrement="true" 
						name="normalizeFileName" 
						id="jpfastcontentedit-new-normalizeFileName" 
						/>
				</div>
			</div>	
                        --%>
		</fieldset>

		<p class="form-actions">
			<wp:i18n key="jpfastcontentedit_SAVE" var="jpfastcontentedit_SAVE" />
			<wpsf:submit 
				useTabindexAutoIncrement="true" 
				cssClass="btn btn-primary" 
				value="%{#attr.jpfastcontentedit_SAVE}" 
				/>
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