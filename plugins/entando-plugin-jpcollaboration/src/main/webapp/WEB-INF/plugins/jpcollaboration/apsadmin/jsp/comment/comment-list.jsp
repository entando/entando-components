<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="jpcrowdsourcing" uri="/jpcrowdsourcing-apsadmin-core"%>

<jpcrowdsourcing:idea key="%{#comments.ideaId}" var="idea" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpcrowdsourcing.admin.title" /></li>
	<li class="page-title-container"><s:text
			name="jpcrowdsourcing.title.comments" /></li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:text name="jpcrowdsourcing.title.comments" />
				<span class="pull-right"> <a tabindex="0" role="button"
					data-toggle="popover" data-trigger="focus" data-html="true"
					title=""
					data-content="<s:text name="jpcrowdsourcing.title.comments.help"/>"
					data-placement="left" data-original-title=""> <i
						class="fa fa-question-circle-o" aria-hidden="true"></i>
				</a>
				</span>
			</h1>
		</div>
		<wp:ifauthorized permission="superuser">
			<div class="col-sm-6">
				<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
								name="jpcrowdsourcing.ideaInstance.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea" />"><s:text
								name="jpcrowdsourcing.idea.list" /></a></li>
					<li class="active"><a
						href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />"><s:text
								name="jpcrowdsourcing.comment.list" /></a></li>
					<li><a
						href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />"><s:text
								name="jpcrowdsourcing.config" /></a></li>
				</ul>
			</div>
		</wp:ifauthorized>
	</div>
</div>
<br>

<div id="main">

	<s:form action="search" cssClass="form-horizontal">

		<s:set var="statusMap"
			value="#{
					1:'jpcrowdsourcing.label.status_not_approved_m',
					2:'jpcrowdsourcing.label.status_to_approve_m',
					3:'jpcrowdsourcing.label.status_approved_m'
					}" />

		<div class="searchPanel form-group">
			<div class="well col-md-offset-3 col-md-6  ">
				<p class="search-label">
					<s:text name="label.search.label" />
				</p>

				<div class="form-group">
					<label class="col-sm-2 control-label"><s:text
							name="label.code" /></label>
					<div class="col-sm-10">
						<wpsf:textfield name="commentText" id="commentText"
							cssClass="form-control input-lg" />
					</div>
				</div>
				<div class="form-group">
					<label for="status" class="control-label col-sm-2"> <s:text
							name="label.state" />
					</label>
					<div class="col-sm-10" id="content_list-changeContentType">
						<wpsf:select cssClass="form-control input-lg" name="searchStatus"
							id="status" list="%{#statusMap}" headerKey=""
							listValue="%{getText(value)}"
							headerValue="%{getText('jpcrowdsourcing.label.anystatus_m')}" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
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
				<wpsf:hidden name="commentText" />
				<wpsf:hidden name="searchStatus" />
			</p>

			<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.ActionErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="ActionErrors">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasActionMessages()">
				<div class="alert alert-info alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="messages.confirm" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="actionMessages">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>

			<wpsa:subset source="comments" count="10" objectName="groupComment"
				advanced="true" offset="5">
				<s:set var="group" value="#groupComment" />

				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include
						value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>

				<s:if test="%{getComments().size() > 0}">
					<table class="table table-striped table-bordered table-hover no-mb">
						<thead>
							<tr>
								<th><s:text name="jpcrowdsourcing.label.description" /></th>
								<th><s:text name="jpcrowdsourcing.label.author" /></th>
								<th class="text-center"><s:text
										name="jpcrowdsourcing.label.date" /></th>
								<th class="text-center"><s:text name="label.actions" /></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator var="commentId">
								<s:set var="comment" value="%{getComment(#commentId)}" />
								<tr>
									<td><s:property value="#comment.title" /> <s:set
											var="currentComment" value="%{#comment.comment.trim()}" /> <s:if
											test="%{#currentComment.length()>80}">
											<s:property value="%{#currentComment.substring(0,80)}" />&hellip;
                                    </s:if> <s:else>
											<s:property value="%{#currentComment}" />
										</s:else></td>
									<td><code>
											<s:property value="#comment.username" />
										</code></td>
									<td class="text-center text-nowrap"><code>
											<s:date name="#comment.creationDate" format="dd/MM/yyyy" />
										</code></td>
									<td class="text-center"><s:if test="#comment.status == 3">
											<s:set var="iconImagePath">
												<wp:resourceURL />plugins/collaboration/administration/img/status_ok.png</s:set>
											<s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
										</s:if> <s:if test="#comment.status == 2">
											<s:set var="iconImagePath">
												<wp:resourceURL />plugins/collaboration/administration/img/status_standby.png
                                        </s:set>
											<s:set var="isOnlineStatus"
												value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
										</s:if> <s:if test="#comment.status == 1">
											<s:set var="iconImagePath">
												<wp:resourceURL />plugins/collaboration/administration/img/status_ko.png
                                        </s:set>
											<s:set var="isOnlineStatus" value="%{getText('label.no')}" />
										</s:if>

										<div class="dropdown dropdown-kebab-pf">
											<span class="btn btn-menu-right dropdown-toggle"
												type="button" data-toggle="dropdown" aria-haspopup="true"
												aria-expanded="false"> <span class="fa fa-ellipsis-v"></span>
											</span>
											<ul class="dropdown-menu dropdown-menu-right">
												<s:iterator value="#statusMap" var="entry">

													<s:if test="#comment.status != #entry.key">
														<s:url action="changeStatus" var="changeStatusAction">
															<s:param name="commentId" value="#comment.id" />
															<s:param name="status" value="#entry.key" />
														</s:url>
														<s:if test="#entry.key == 1">
															<li><a
																title="<s:text name="jpcrowdsourcing.status.approved" />. <s:text name="jpcrowdsourcing.label.suspend" />"
																href="<s:property value="#changeStatusAction" escapeHtml="false" />">
																	<s:text name="label.edit" />: <s:property
																		value="#ideaInstance_var.code" />
															</a></li>
														</s:if>
														<s:if test="#entry.key == 3">
															<li><a
																title="<s:text name="jpcrowdsourcing.status.suspended" />. <s:text name="jpcrowdsourcing.label.approve" />"
																href="<s:property value="#changeStatusAction" escapeHtml="false" />">
																	<s:text name="label.edit" />: <s:property
																		value="#ideaInstance_var.code" />
															</a></li>
														</s:if>
													</s:if>
												</s:iterator>
												<li><a
													title="<s:text name="jpcrowdsourcing.title.comments.view" />"
													href="<s:url action="view"><s:param name="commentId" value="#comment.id" /></s:url>">
														<s:text name="label.edit" />: <s:property
															value="#ideaInstance_var.code" />
												</a></li>
												<li><a
													href="<s:url action="trash"><s:param name="commentId" value="#comment.id" /></s:url>"
													title="<s:text name="jpcrowdsourcing.label.delete" />">
														<s:text name="jpcrowdsourcing.label.delete" />
												</a></li>
											</ul>
										</div></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</s:if>
				<s:else>
					<div class="alert alert-info">
						<s:text name="jpcrowdsourcing.note.comments.empty"></s:text>
					</div>
				</s:else>

				<div class="pager">
					<s:include
						value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
			</wpsa:subset>
		</s:form>
	</div>
</div>
