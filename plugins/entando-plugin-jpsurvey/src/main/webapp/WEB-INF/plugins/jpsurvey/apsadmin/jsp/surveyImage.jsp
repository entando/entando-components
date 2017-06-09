<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<s:set var="currentSurveyId" value="%{surveyId}" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="title.surveyList" /></li>
	<s:if test="questionnaire">
		<li><s:text name="title.jpsurvey.survey.main" /></li>
		<li class="page-title-container"><s:text
				name="title.jpsurvey.surveyEditing" /></li>
	</s:if>
	<s:else>
		<li><s:text name="title.jpsurvey.poll.main" /></li>
		<li class="page-title-container"><s:text
				name="title.jpsurvey.pollEditing" /></li>
	</s:else>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:if test="questionnaire">
					<s:text name="title.jpsurvey.surveyEditing" />
					<span class="pull-right"> <a tabindex="0" role="button"
						data-toggle="popover" data-trigger="focus" data-html="true"
						title=""
						data-content="<s:text name="title.jpsurvey.surveyEditing.help" />"
						data-placement="left" data-original-title=""> <i
							class="fa fa-question-circle-o" aria-hidden="true"></i>
					</a>
					</span>
				</s:if>
				<s:else>
					<s:text name="title.jpsurvey.pollEditing" />
					<span class="pull-right"> <a tabindex="0" role="button"
						data-toggle="popover" data-trigger="focus" data-html="true"
						title=""
						data-content="<s:text name="title.jpsurvey.pollEditing.help" />"
						data-placement="left" data-original-title=""> <i
							class="fa fa-question-circle-o" aria-hidden="true"></i>
					</a>
					</span>
				</s:else>
			</h1>
		</div>
		<wp:ifauthorized permission="superuser">
			<div class="col-sm-6">
				<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
					<s:if test="questionnaire">
						<li class="active"><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
								<s:text name="jpsurvey.label.questionnaires" />
						</a></li>
					</s:if>
					<s:else>
						<li><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
								<s:text name="jpsurvey.label.questionnaires" />
						</a></li>
					</s:else>
					<s:if test="questionnaire">
						<li><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
								<s:text name="jpsurvey.label.polls" />
						</a></li>
					</s:if>
					<s:else>
						<li class="active"><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
								<s:text name="jpsurvey.label.polls" />
						</a></li>
					</s:else>
				</ul>
			</div>
		</wp:ifauthorized>
	</div>
</div>
<br>
<div id="main">
	<div class="panel panel-default">
		<div class="panel-body">
			<s:text name="note.survey.youwork" />
			:&#32;
			<s:property value="currentSurveyId.descr" />
			<s:set var="defaultLanguage" value="defaultLangCode" />
			<code>
				<s:property value="titles[#defaultLanguage]" />
			</code>
		</div>
	</div>

	<h3 class="margin-bit-top">
		<s:text name="title.chooseImage" />
	</h3>

	<s:form action="search" cssClass="form-horizontal">
		<div class="searchPanel form-group">
			<div class="well col-md-offset-3 col-md-6  ">
				<p class="noscreen">
					<input type="hidden" name="surveyId"
						value="<s:property value="surveyId"/>" /> <input type="hidden"
						name="resourceTypeCode" value="Image" /> <input type="hidden"
						name="questionnaire" value="<s:property value="questionnaire"/>" />
				</p>
				<p class="search-label">
					<s:text name="label.search.label" />
				</p>

				<div class="form-group">
					<label class="col-sm-2 control-label"><s:text
							name="label.code" /></label>
					<div class="col-sm-9">
						<wpsf:textfield name="text" id="text"
							cssClass="form-control input-lg" />
					</div>
					<br> <br> <label class="control-label col-sm-2"><s:text
							name="label.category" /></label>
					<div class="col-sm-9">
						<div>
							<p class="toolClass">
								<a href="#" rel="expand" title="Expand All">expand all</a> <a
									href="#" rel="collapse" title="Collapse All">collapse all</a>
							</p>
							<ul id="categoryTree" class="icons-ul list-unstyled">
								<li class="tree_node_flag"><span
									class="icon fa-li fa fa-folder"></span> <input type="radio"
									name="categoryCode"
									id="<s:property value="categoryRoot.code" />"
									value="<s:property value="categoryRoot.code" />"
									class="subTreeToggler" /> <label
									for="<s:property value="categoryRoot.code" />"> <s:if
											test="categoryRoot.titles[currentLang.code] == null">
											<s:property value="categoryRoot.code" />
										</s:if> <s:else>
											<s:property value="categoryRoot.titles[currentLang.code]" />
										</s:else>
								</label> <s:if test="categoryRoot.children.size > 0">
										<ul class="treeToggler icons-ul" id="tree_root">
											<s:set var="currentCategoryRoot" value="categoryRoot" />
											<s:include
												value="/WEB-INF/apsadmin/jsp/resource/categoryTreeBuilder.jsp" />
										</ul>
									</s:if></li>
							</ul>
						</div>
					</div>
					<br> <br>
					<div class="col-sm-12">
						<wpsf:submit type="button" cssClass="btn btn-primary pull-right"
							title="%{getText('label.search')}">
							<s:text name="%{getText('label.search')}" />
						</wpsf:submit>
					</div>
				</div>
			</div>
		</div>
	</s:form>

	<div class="subsection-light">
		<s:form action="search">
			<p class="noscreen">
				<input type="hidden" name="questionnaire"
					value="<s:property value="questionnaire"/>" /> <input
					type="hidden" name="text" /> <input type="hidden" name="surveyId"
					value="<s:property value="surveyId"/>" /> <input type="hidden"
					name="categoryCode" value="<s:property value="categoryCode" /> " />
				<input type="hidden" name="resourceTypeCode" value="Image" />
			</p>

			<div class="container-fluid">
				<div class="toolbar-pf">
					<div class="toolbar-pf-action-right">
						<div class="form-group toolbar-pf-view-selector">
							<button class="btn btn-link" data-toggle="tab" href="#table-view">
								<i class="fa fa-th-large fa-2x"></i>
							</button>
							<button class="btn btn-link" data-toggle="tab" href="#list-view">
								<i class="fa fa-th-list fa-2x"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
			<div class="tab-content">
				<div id="table-view" class="tab-pane fade">

					<s:form action="search" class="container-fluid container-cards-pf">
						<p class="sr-only">
							<wpsf:hidden name="text" />
							<wpsf:hidden name="categoryCode" />
							<wpsf:hidden name="resourceTypeCode" />
							<wpsf:hidden name="fileName" />
							<wpsf:hidden name="ownerGroupName" />
							<s:if test="#categoryTreeStyleVar == 'request'">
								<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
									<wpsf:hidden name="treeNodesToOpen"
										value="%{#treeNodeToOpenVar}" />
								</s:iterator>
							</s:if>
							<wpsf:hidden name="contentOnSessionMarker" />
						</p>
						<wpsa:subset source="resources" count="10"
							objectName="groupResource" advanced="true" offset="5">
							<div class="row row-cards-pf">
								<s:set var="group" value="#groupResource" />
								<s:set var="imageDimensionsVar" value="imageDimensions" />
								<s:iterator var="resourceid" status="status">
									<s:set var="resource" value="%{loadResource(#resourceid)}" />
									<s:set var="resourceInstance"
										value='%{#resource.getInstance(0,null)}' />
									<s:set var="URLoriginal" value="%{#resource.getImagePath(0)}" />
									<s:url var="URLedit" action="edit"
										namespace="/do/jacms/Resource">
										<s:param name="resourceId" value="%{#resourceid}" />
									</s:url>
									<s:url var="URLuse" action="joinResource"
										namespace="/do/jacms/Content/Resource">
										<s:param name="resourceId" value="%{#resourceid}" />
										<s:param name="contentOnSessionMarker"
											value="contentOnSessionMarker" />
									</s:url>
									<s:url var="URLtrash" action="trash"
										namespace="/do/jacms/Resource">
										<s:param name="resourceId" value="%{#resourceid}" />
										<s:param name="resourceTypeCode" value="%{#resource.type}" />
										<s:param name="text" value="%{text}" />
										<s:param name="categoryCode" value="%{categoryCode}" />
										<s:param name="fileName" value="%{fileName}" />
										<s:param name="ownerGroupName" value="%{ownerGroupName}" />
										<s:param name="treeNodesToOpen" value="%{treeNodesToOpen}" />
									</s:url>
									<div class="col-xs-6 col-sm-4 col-md-3">
										<div class="card-pf card-pf-view card-pf-view-select">
											<div class="card-pf-body">
												<div class="card-pf-heading-kebab">
													<div class="dropdown pull-right dropdown-kebab-pf">
														<button class="btn btn-link dropdown-toggle" type="button"
															id="dropdownKebabRight1" data-toggle="dropdown"
															aria-haspopup="true" aria-expanded="true">
															<span class="fa fa-ellipsis-v"></span>
														</button>
														<ul class="dropdown-menu dropdown-menu-right"
															aria-labelledby="dropdownKebabRight1">
															<li><a
																href="<s:url action="joinImage" namespace="/do/jpsurvey/Survey">
                                           <s:param name="resourceId" value="%{#resourceid}" />
                                           <s:param name="surveyId" value="surveyId" />
                                           <s:param name="questionnaire" value="questionnaire" />
                                       </s:url>"
																title="<s:text name="note.joinThisToThat" />: TITOLO_SONDAGGIO_CORRENTE"><s:text
																		name="label.join" /></a></li>
														</ul>
													</div>

												</div>

												<div class="card-pf-top-element">
													<img data-toggle="popover"
														data-title="<s:property value="#resource.descr" />"
														data-original-title="" title=""
														src="<s:property value="%{#resource.getImagePath(1)}"/>"
														alt=" " style="height: 90px; max-width: 130px"
														class="img-responsive center-block" />
													<script>
														$(
																"[data-toggle=popover]")
																.popover(
																		{
																			html : true,
																			placement : "top",
																			content : '<div class="list-group margin-small-top">\n\
                                        <a href="<s:property value="%{#resource.getImagePath(1)}"/>" class="list-group-item text-center">View full size</a>\n\
                                        </div>'
																		});
													</script>
												</div>
												<h2 class="card-pf-title text-center">
													<s:set var="fileNameVar" value="#resource.masterFileName" />
													<s:if test='%{#fileNameVar.length()>15}'>
														<s:set var="fileNameVar"
															value='%{#fileNameVar.substring(0,7)+"..."+#fileNameVar.substring(#fileNameVar.length()-5)}' />
														<s:property value="#fileNameVar" />
													</s:if>
													<s:else>
														<s:property value="#fileNameVar" />
													</s:else>
												</h2>
											</div>
										</div>
									</div>
								</s:iterator>
							</div>
							<div class="pager clear margin-more-top">
								<s:include
									value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
							</div>
						</wpsa:subset>
					</s:form>
				</div>
				<div id="list-view" class="tab-pane fade in active">
					<s:form action="search" class="container-fluid">
						<p class="sr-only">
							<wpsf:hidden name="text" />
							<wpsf:hidden name="categoryCode" />
							<wpsf:hidden name="resourceTypeCode" />
							<wpsf:hidden name="fileName" />
							<wpsf:hidden name="ownerGroupName" />
							<s:if test="#categoryTreeStyleVar == 'request'">
								<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
									<wpsf:hidden name="treeNodesToOpen"
										value="%{#treeNodeToOpenVar}" />
								</s:iterator>
							</s:if>
							<wpsf:hidden name="contentOnSessionMarker" />
						</p>
						<wpsa:subset source="resources" count="10"
							objectName="groupResource" advanced="true" offset="5">
							<div class="list-group list-view-pf list-view-pf-view">
								<s:set var="group" value="#groupResource" />
								<s:set var="imageDimensionsVar" value="imageDimensions" />
								<s:iterator var="resourceid" status="status">
									<s:set var="resource" value="%{loadResource(#resourceid)}" />
									<s:set var="resourceInstance"
										value='%{#resource.getInstance(0,null)}' />
									<s:set var="URLoriginal" value="%{#resource.getImagePath(0)}" />
									<s:url var="URLedit" action="edit"
										namespace="/do/jacms/Resource">
										<s:param name="resourceId" value="%{#resourceid}" />
									</s:url>
									<s:url var="URLuse" action="joinResource"
										namespace="/do/jacms/Content/Resource">
										<s:param name="resourceId" value="%{#resourceid}" />
										<s:param name="contentOnSessionMarker"
											value="contentOnSessionMarker" />
									</s:url>
									<s:url var="URLtrash" action="trash"
										namespace="/do/jacms/Resource">
										<s:param name="resourceId" value="%{#resourceid}" />
										<s:param name="resourceTypeCode" value="%{#resource.type}" />
										<s:param name="text" value="%{text}" />
										<s:param name="categoryCode" value="%{categoryCode}" />
										<s:param name="fileName" value="%{fileName}" />
										<s:param name="ownerGroupName" value="%{ownerGroupName}" />
										<s:param name="treeNodesToOpen" value="%{treeNodesToOpen}" />
									</s:url>

									<div class="list-group-item">
										<div class="list-view-pf-actions">
											<div class="dropdown pull-right dropdown-kebab-pf">
												<button class="btn btn-link dropdown-toggle" type="button"
													id="dropdownKebabRight2" data-toggle="dropdown"
													aria-haspopup="true" aria-expanded="true">
													<span class="fa fa-ellipsis-v"></span>
												</button>
												<ul class="dropdown-menu dropdown-menu-right"
													aria-labelledby="dropdownKebabRight1">
													<li><a
														href="<s:url action="joinImage" namespace="/do/jpsurvey/Survey">
                                           <s:param name="resourceId" value="%{#resourceid}" />
                                           <s:param name="surveyId" value="surveyId" />
                                           <s:param name="questionnaire" value="questionnaire" />
                                       </s:url>"
														title="<s:text name="note.joinThisToThat" />: TITOLO_SONDAGGIO_CORRENTE"><s:text
																name="label.join" /></a></li>
												</ul>
											</div>
										</div>
										<div class="list-view-pf-main-info">
											<div class="list-view-pf-left col-o" style="width: 130px">
												<img data-toggle="popover"
													data-title="<s:property value="#resource.descr" />"
													data-original-title="" title=""
													src="<s:property value="%{#resource.getImagePath(1)}"/>"
													alt=" " class="img-responsive center-block" />
												<script>
													$("[data-toggle=popover]")
															.popover(
																	{
																		html : true,
																		placement : "top",
																		content : '<div class="list-group margin-small-top">\n\
                                        <a href="<s:property value="%{#resource.getImagePath(1)}"/>" class="list-group-item text-center">View full size</a>\n\
                                        </div>'
																	});
												</script>
											</div>
											<div class="list-view-pf-body">
												<div class="list-view-pf">
													<div class="list-group-item-heading"
														style="font-size: 16px">
														<s:set var="fileNameVar" value="#resource.masterFileName" />
														<s:set var="fileDescVar" value="#resource.description" />
														<s:if test='%{#fileNameVar.length()>24}'>
															<s:set var="fileNameVar"
																value='%{#fileNameVar.substring(0,10)+"..."+#fileNameVar.substring(#fileNameVar.length()-10)}' />
															<s:property value="#fileNameVar" />
														</s:if>
														<s:else>
															<s:property value="#fileNameVar" />
														</s:else>
													</div>
													<div class="list-group-item-text">
														<s:property value="#fileDescVar" />
													</div>
													<br>
													<div class="list-view-pf-additional-info"
														style="width: 100%">


														<s:set var="dimensionId" value="0" />
														<s:set var="resourceInstance"
															value='%{#resource.getInstance(#dimensionId,null)}' />
														<a
															href="<s:property value="%{#resource.getImagePath(#dimensionId)}" />"
															class="list-view-pf-additional-info-item"> <s:text
																name="label.size.original" />&nbsp; <span class="badge">
																<s:property
																	value='#resourceInstance.fileLength.replaceAll(" ", "&nbsp;")'
																	escapeXml="false" escapeHtml="false"
																	escapeJavaScript="false" />
														</span>
														</a>
														<s:set var="dimensionId" value="null" />
														<s:set var="resourceInstance" value="null" />
														<s:iterator value="#imageDimensionsVar" var="dimInfo">
															<s:set var="dimensionId" value="#dimInfo.idDim" />
															<s:set var="resourceInstance"
																value='%{#resource.getInstance(#dimensionId,null)}' />
															<s:if test="#resourceInstance != null">
                                                        &nbsp;|&nbsp;
                                                        <a
																	href="<s:property value="%{#resource.getImagePath(#dimensionId)}"/>"
																	class="list-view-pf-additional-info-item"> <s:property
																		value="#dimInfo.dimx" /> x<s:property
																		value="#dimInfo.dimy" />&nbsp;px <span class="badge">
																		<s:property
																			value='#resourceInstance.fileLength.replaceAll(" ", "&nbsp;")'
																			escapeXml="false" escapeHtml="false"
																			escapeJavaScript="false" />
																</span>
																</a>
															</s:if>
														</s:iterator>
													</div>
												</div>
											</div>
										</div>
									</div>
								</s:iterator>
							</div>
							<div class="pager clear margin-more-top">
								<s:include
									value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
							</div>
						</wpsa:subset>
					</s:form>
				</div>
			</div>
		</s:form>
	</div>
</div>