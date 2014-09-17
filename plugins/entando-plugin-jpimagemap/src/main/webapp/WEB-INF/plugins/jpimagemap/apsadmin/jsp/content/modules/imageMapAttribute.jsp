<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="currentResource" value="#attribute.resource" />
<s:set var="defaultResource" value="#attribute.resource" />
<s:if test="null != #attribute.description"><s:set var="attributeLabelVar" value="#attribute.description" /></s:if>
<s:else><s:set var="attributeLabelVar" value="#attribute.name" /></s:else>
<label class="display-block">
	<s:property value="#attributeLabelVar" />
	&#32;<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
</label>

	<s:if test="#lang.default"><%-- default language --%>
		<s:if test="#currentResource != null && #currentResource.id != 0"><%-- default lang when valorized --%>
				<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
					<div class="panel panel-default margin-small-top">
					<%-- remove resource button --%>
						<div class="panel-heading text-right">
							<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/removeResourceSubmit.jsp">
								<s:param name="resourceTypeCode">Image</s:param>
							</s:include>
						</div>
				</s:if>
				<div class="row panel-body">
					<%-- download --%>
						<div class="col-xs-12 col-sm-3 col-lg-2 text-center">
							<a href="<s:property value="#defaultResource.getImagePath('0')" />" title="<s:text name="label.img.original" />">
								<img class="img-thumbnail" src="<s:property value="#defaultResource.getImagePath('1')"/>" alt="<s:property value="#defaultResource.descr"/>" style="height:90px; max-width:130px" />
							</a>
						</div>
					<%-- label and input --%>
						<div class="col-xs-12 col-sm-9 col-lg-10 form-horizontal margin-large-top">
					  	<div class="form-group">
								<label class="col-xs-2 control-label text-right" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
									<abbr title="<s:text name="label.img.text.long" />"><s:text name="label.img.text.short" /></abbr>
								</label>
								<div class="col-xs-10">
									<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
								</div>
							</div>
						</div>
				</div>
				<div class="row panel-body">
					<div class="col-xs-12">
						<s:if test="%{#attribute.areas.size() > 0}">
								<span class="sr-only">
									Areas of the imagemap can be set by button Add
								</span>
								<ul class="list-group">
									<s:iterator value="#attribute.areas" id="area" status="elementStatus">
										<wpsa:actionParam action="removeImageMapArea" var="removeAreaActionName" ><wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" /><wpsa:actionSubParam name="elementIndex" value="%{#elementStatus.index}" /><wpsa:actionSubParam name="langCode" value="%{#lang.code}" /></wpsa:actionParam>
										<s:set var="htmlPrefixId" value="%{'jpimagemap_'+#attribute.name+'_'+#lang.code+'_'}"  />
										<li class="list-group-item text-left form-horizontal">
											<div class="form-group">
												<wpsf:submit
													action="%{#removeAreaActionName}"
													type="button"
													title="%{getText('label.remove')}"
													cssClass="btn btn-warning btn-xs margin-base-left"
													>
													<span class="icon fa fa-times-circle-o"></span>
													<span class="sr-only"><s:property value="%{getText('label.remove')}" /></span>
												</wpsf:submit>
												<label>Area&#32;<s:property value="#elementStatus.count"/></label>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label text-right" for="<s:property value="%{#htmlPrefixId+'shape'+#elementStatus.count}"/>">
													Shape
												</label>
												<div class="col-xs-10">
													<wpsf:textfield
														cssClass="form-control input-sm"
														id="%{#htmlPrefixId+'shape'+#elementStatus.count}"
														name="%{#attribute.name}_shape_%{#elementStatus.index}"
														value="%{#area.shape}"
														disabled="disabled"/>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label text-right" for="<s:property value="%{#htmlPrefixId+'coords'+#elementStatus.count}"/>">
													Coords
												</label>
												<div class="col-xs-10">
													<div class="input-group">
														<wpsf:textfield
															cssClass="form-control input-sm"
															id="%{#htmlPrefixId+'coords'+#elementStatus.count}"
															name="%{#attribute.name}_coords_%{#elementStatus.index}"
															value="%{#area.coords}" />
													  <span class="input-group-btn">
															<%-- define area button --%>
															<wpsa:actionParam action="defineImageMapArea" var="defineAreaActionName" ><wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" /><wpsa:actionSubParam name="elementIndex" value="%{#elementStatus.index}" /><wpsa:actionSubParam name="langCode" value="%{#lang.code}" /></wpsa:actionParam>
															<wpsf:submit
																type="button"
																cssClass="btn btn-default btn-sm"
																action="%{#defineAreaActionName}"
																title="%{getText('label.defineArea')}"
																>
																	<span class="icon fa fa-edit"></span>
																	<span class="sr-only">
																		<s:property value="%{getText('label.defineArea')}" />
																	</span>
															</wpsf:submit>
												  	</span>
													</div>
												</div>
											</div>
											<div class="form-group">
												<label class="col-xs-2 control-label text-right" for="<s:property value="%{#htmlPrefixId+'link'+#elementStatus.count}"/>">
													Link
												</label>
												<div class="col-xs-10">
													<s:set var="currentAreaLink" value="#area.link"/>
													<s:if test="%{#currentAreaLink.symbolicLink != null}">
														<div class="input-group">
															<s:if test="#currentAreaLink.symbolicLink.destType == 2 || #currentAreaLink.symbolicLink.destType == 4">
																<s:set var="linkedPage" value="%{getPage(#currentAreaLink.symbolicLink.pageDest)}"></s:set>
															</s:if>
															<s:if test="#currentAreaLink.symbolicLink.destType == 3 || #currentAreaLink.symbolicLink.destType == 4">
																<s:set var="linkedContent" value="%{getContentVo(#currentAreaLink.symbolicLink.contentDest)}"></s:set>
															</s:if>
															<s:set var="validLink" value="true" />
															<s:if test="(#currentAreaLink.symbolicLink.destType == 2 || #currentAreaLink.symbolicLink.destType == 4) && #linkedPage == null">
																<%-- invalid link to page  --%>
																<s:set var="validLink" value="false" />
															</s:if>
															<s:if test="(#currentAreaLink.symbolicLink.destType == 3 || #currentAreaLink.symbolicLink.destType == 4) && (#linkedContent == null || !#linkedContent.onLine)">
																<%-- invalid link to content --%>
																<s:set var="validLink" value="false" />
															</s:if>
															<s:if test="#validLink"><%-- valid link --%>
																<s:if test="#currentAreaLink.symbolicLink.destType == 1">
																	<s:set var="statusIconVar">btn btn-default disabled icon fa fa-globe</s:set>
																	<s:set var="linkDestination" value="%{getText('note.URLLinkTo') + ': ' + #currentAreaLink.symbolicLink.urlDest}" />
																</s:if>
																<s:if test="#currentAreaLink.symbolicLink.destType == 2">
																	<s:set var="statusIconVar">btn btn-default disabled icon fa fa-folder</s:set>
																	<s:set var="linkDestination" value="%{getText('note.pageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
																</s:if>
																<s:if test="#currentAreaLink.symbolicLink.destType == 3">
																	<s:set var="statusIconVar">btn btn-default disabled icon fa fa-file-text-o</s:set>
																	<s:set var="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #currentAreaLink.symbolicLink.contentDest + ' - ' + #linkedContent.descr}" />
																</s:if>
																<s:if test="#currentAreaLink.symbolicLink.destType == 4">
																	<s:set var="statusIconVar">btn btn-default disabled icon fa fa-file-text</s:set>
																	<s:set var="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #currentAreaLink.symbolicLink.contentDest + ' - ' + #linkedContent.descr + ', ' + getText('note.contentOnPageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
																</s:if>
																		<span class="input-group-addon btn-sm <s:property value="#statusIconVar" />"></span>
																	<%-- field text --%>
																	<wpsf:textfield
																		id="%{#htmlPrefixId+'link'+#elementStatus.count}"
																		name="%{#attributeTracer.getFormFieldName(#currentAreaLink)}_%{#elementStatus.index}"
																		value="%{#currentAreaLink.getTextForLang(#lang.code)}"
																		maxlength="254"
																		placeholder="%{getText('label.alt.text')}"
																		title="%{getText('label.alt.text')}"
																		cssClass="form-control input-sm" />
															</s:if><%-- valid link // end --%>
													</s:if>
														<span class="input-group-btn">
														<%-- configure link button --%>
															<wpsa:actionParam action="chooseLink" var="chooseLinkActionName" ><wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" /><wpsa:actionSubParam name="elementIndex" value="%{#elementStatus.index}" /><wpsa:actionSubParam name="langCode" value="%{#lang.code}" /></wpsa:actionParam>
															<wpsf:submit
																cssClass="btn btn-sm %{#currentAreaLink.symbolicLink != null ? ' btn-warning' : ' btn-default'}"
																type="button" action="%{#chooseLinkActionName}"
																title="%{#attributeLabelVar + ', Area ' + #elementStatus.count + ' link: ' + ( #currentAreaLink.symbolicLink != null ? getText('label.remove'):getText('label.configure'))}">
																	<span class="sr-only"><s:property value="#attributeLabelVar" />:</span>
																	<s:if test="%{#currentAreaLink.symbolicLink != null}">
																		<span class="icon fa fa-times"></span>
																		<span class="sr-only"><s:text name="label.remove" /></span>
																	</s:if>
																	<s:else>
																		<span class="icon fa fa-link"></span>
																		<s:text name="label.configure" />
																	</s:else>
															</wpsf:submit>
														</span>
													<s:if test="%{#currentAreaLink.symbolicLink != null}"></div></s:if>
												</div>
											</div>
										</li>
									</s:iterator>
								</ul>
						</s:if>
						<%-- add area --%>
							<wpsa:actionParam action="addImageMapArea" var="actionName" >
								<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
								<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
							</wpsa:actionParam>
							<wpsf:submit
								type="button"
								action="%{#actionName}"
								cssClass="btn btn-default btn-sm"
								title="%{getText('label.add')}">
								 	<span class="icon fa fa-plus"></span>&#32;
									<s:property value="%{getText('label.add')}" />
							 </wpsf:submit>

					</div>
				</div>

				<s:if test="!(#attributeTracer.monoListElement) || ((#attributeTracer.monoListElement) && (#attributeTracer.compositeElement))">
					</div>
				</s:if>

		</s:if><%-- default lang when valorized //end --%>

		<s:else><%-- default lang when empty --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/chooseResourceSubmit.jsp">
					<s:param name="resourceTypeCode">Image</s:param>
				</s:include>
		</s:else><%-- default lang when empty //end --%>
	</s:if><%-- default lang //end --%>

	<s:else><%-- other langs --%>
		<s:if test="#currentResource != null && #currentResource.id != 0"><%-- other langs when valorized --%>
				<s:set var="attributeIsNestedVar" value="%{
					(#attributeTracer.monoListElement && #attributeTracer.compositeElement)
					||
					(#attributeTracer.monoListElement==true && #attributeTracer.compositeElement==false)
					||
					(#attributeTracer.monoListElement==false && #attributeTracer.compositeElement==true)
				}" />
				<%-- attributeIsNestedVar: <s:property value="#attributeIsNestedVar" /><br /> --%>
				<s:if test="!#attributeIsNestedVar">
					<div class="panel panel-default margin-small-top">
				</s:if>
					<div class="<s:if test="#attributeIsNestedVar">pull-right margin-none</s:if><s:else>panel-heading text-right</s:else>">
						<span class="text-muted btn btn-xs">&nbsp;</span>
					</div><%-- pull-righ / panel-heading end --%>
					<div class="row panel-body">
						<%-- download icon + button --%>
							<div class="col-xs-12 col-sm-3 col-lg-2 text-center">
								<a href="<s:property value="#currentResource.getImagePath('0')" />" title="<s:text name="label.img.original" />">
									<img class="img-thumbnail" src="<s:property value="#currentResource.getImagePath('1')"/>" alt="<s:property value="#currentResource.descr"/>" style="height:90px; max-width:130px" />
								</a>
							</div>
						<%-- text field --%>
							<div class="col-xs-12 col-sm-9 col-lg-10 form-horizontal margin-large-top">
								<div class="form-group">
									<label class="col-xs-2 control-label text-right" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
										<s:text name="label.text" />
									</label>
									<div class="col-xs-10">
										<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
									</div>
								</div>
							</div>
					</div>
				<s:if test="!#attributeIsNestedVar">
					</div>
				</s:if>

		</s:if><%-- other language when valorized //end --%>

		<s:else><%-- other langs when empty --%>
			<span class="form-control-static text-info">
				<s:text name="note.editContent.doThisInTheDefaultLanguage" />.
			</span>
		</s:else><%-- other langs when empty //end --%>

	</s:else><%-- other languages //end --%>
