<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
			<s:text name="jpcontentrefs.title.contentTypesManagement" />
	</span>
</h1>
<div id="main">
	<p class="text-info">
		<s:text name="jpcontentrefs.note.youAreConfigure" />: <em class="important"><s:property value="%{getContentType(contentTypeCode).getDescr()}" /></em>
	</p>
	<s:form method="get" cssClass="form-horizontal action-form">
		<p class="noscreen">
			<wpsf:hidden name="contentTypeCode" />
			<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
		</p>

		<fieldset class="panel panel-default">
			<div class="panel-heading"><s:text name="jpcontentrefs.title.categoryTree" /></div>
			<%-- Category tree --%>
				<ul id="categoryTree" class="fa-ul list-unstyled">
					<s:set var="inputFieldName" value="%{'selectedNode'}" />
					<s:set var="selectedTreeNode" value="%{selectedNode}" />
					<s:set var="liClassName" value="'category'" />
					<s:set var="treeItemIconName" value="'fa-folder'" />

					<s:if test="%{#categoryTreeStyleVar == 'classic'}">
						<s:set var="currentRoot" value="allowedTreeRootNode" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
					</s:if>
					<s:elseif test="%{#categoryTreeStyleVar == 'request'}">
						<s:set var="openTreeActionName" value="'openCloseFacetTree'" />
						<s:set var="closeTreeActionName" value="'openCloseFacetTree'" />
						<s:set var="currentRoot" value="showableTree" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
					</s:elseif>
				</ul>

			<%-- mocked category tree
				<div class="panel-body">
						<ul id="categoryTree" class="fa-ul list-unstyled">
								<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder"></span>&#32;
										<input type="radio" name="selectedNode" id="fagianonode_home" value="home" class="subTreeToggler tree_home" />&#32;
										<label for="fagianonode_home">All</label>
										<ul class="treeToggler fa-ul" id="tree_home">
												<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder"></span>&#32;
														<input type="radio" name="selectedNode" id="fagianonode_1" value="1" class="subTreeToggler tree_1" />&#32;
														<label for="fagianonode_1">1</label>
														<ul class="treeToggler fa-ul" id="tree_1">
																<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder"></span>&#32;
																		<input type="radio" name="selectedNode" id="fagianonode_11" value="11" class="subTreeToggler tree_11" />&#32;
																		<label for="fagianonode_11">11</label>
																		<ul class="treeToggler fa-ul" id="tree_11">
																				<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder-o"></span>&#32;
																						<input type="radio" name="selectedNode" id="fagianonode_111" value="111" />&#32;
																						<label for="fagianonode_111">111</label>
																				</li>
																		</ul>
																</li>
														</ul>
												</li>
												<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder"></span>&#32;
														<input type="radio" name="selectedNode" id="fagianonode_2" value="2" class="subTreeToggler tree_2" />&#32;
														<label for="fagianonode_2">2</label>
														<ul class="treeToggler fa-ul" id="tree_2">
																<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder"></span>&#32;
																		<input type="radio" name="selectedNode" id="fagianonode_22" value="22" class="subTreeToggler tree_22" />&#32;
																		<label for="fagianonode_22">22</label>
																		<ul class="treeToggler fa-ul" id="tree_22">
																				<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder-o"></span>&#32;
																						<input type="radio" name="selectedNode" id="fagianonode_222" value="222" />&#32;
																						<label for="fagianonode_222">222</label>
																				</li>
																		</ul>
																</li>
														</ul>
												</li>
												<li class="category tree_node_flag"><span class="icon fa fa-li fa-folder-o"></span>&#32;
														<input type="radio" name="selectedNode" id="fagianonode_3" value="3" />&#32;
														<label for="fagianonode_3">3</label>
												</li>
										</ul>
								</li>
						</ul>
				</div>
			 --%>
			<%-- mocked category tree --%>
		</fieldset>

		<%-- Fieldset button --%>
			<fieldset data-toggle="tree-toolbar"><legend><s:text name="label.actions" /></legend>
				<div class="btn-toolbar" data-toggle="tree-toolbar-actions">
						<%-- Join button --%>
							<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
								<wpsf:submit action="join" type="button" title="%{getText('label.join')}" cssClass="btn btn-info" data-toggle="tooltip">
									<span class="icon fa fa-plus">&#32;
									</span>
								</wpsf:submit>
							</div>
				</div>
			</fieldset>

			<s:set var="joinedCategoryVar" value="%{getCategories(contentTypeCode)}"/>
			<s:if test="#joinedCategoryVar != null && #joinedCategoryVar.size() > 0">
				<div class="panel panel-default">
					<div class="panel-heading"><s:text name="jpcontentrefs.title.categoriesAssociated" /></div>
					<table class="table table-bordered" summary="<s:text name="jpcontentrefs.note.categories.summary" />">
						<thead>
							<tr>
								<th class="text-center padding-large-left padding-large-right col-xs-1 col-sm-1 col-md-1 col-lg-1"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
								<th><s:text name="jpcontentrefs.label.description" /></th>
							</tr>
						</thead>
						<s:iterator value="#joinedCategoryVar" var="joinedCategory">
							<wpsa:actionParam action="remove" var="actionName" >
								<wpsa:actionSubParam name="selectedNode" value="%{#joinedCategory.code}" />
							</wpsa:actionParam>
							<tr>
								<td class="text-center text-nowrap">
									<div class="btn-group btn-group-xs">
										<wpsf:submit
											cssClass="btn btn-warning"
											type="button"
											action="%{#actionName}"
											title="%{getText('label.remove')}: %{#joinedCategory.getFullTitle(currentLang.code)}">
												<span class="sr-only"><s:text name="label.remove" /></span>
												<span class="icon fa fa-times-circle-o"></span>
										</wpsf:submit>

									</div>
								</td>
								<td><s:property value="%{#joinedCategory.getFullTitle(currentLang.code)}" /></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</s:if>
			<s:else><p><em><s:text name="jpcontentrefs.note.categories.empty" /></em></p></s:else>
	</s:form>
</div>
