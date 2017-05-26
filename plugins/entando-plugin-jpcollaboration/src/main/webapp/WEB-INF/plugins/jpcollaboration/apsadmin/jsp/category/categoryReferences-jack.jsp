<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="jacmswpsa" uri="/jpcrowdsourcing-apsadmin-core"%>


<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="margin-none">
			<s:text name="title.category.ideaReferenced" />
		</h3>
	</div>
	<div class="panel-body">

		<s:if test="null != references['jpcrowdsourcingIdeaManagerUtilizers']">
			<wpsa:subset
				source="references['jpcrowdsourcingIdeaManagerUtilizers']"
				count="10" objectName="contentReferences" advanced="true" offset="5"
				pagerId="contentManagerReferences">
				<s:set var="group" value="#contentReferences" />

				<table class="table table-striped table-bordered table-hover no-mb"
					id="contentListTable">
					<thead>
						<tr>
							<th class="text-center"><s:text
									name="jpcrowdsourcing.label.title" /></th>
							<th class="text-center"><s:text
									name="jpcrowdsourcing.label.author" /></th>
							<th class="text-center"><s:text
									name="jpcrowdsourcing.label.date" /></th>
							<th class="text-center"><s:text
									name="jpcrowdsourcing.label.comments" /></th>
							<th class="text-center"><s:text
									name="jpcrowdsourcing.label.approved" /></th>
							<th class="text-center"><s:text name="label.actions" /></th>
						</tr>
					</thead>
					<tbody>
						<s:iterator var="idea">
							<tr class="text-center">
								<s:url action="edit" var="editAction"
									namespace="/do/collaboration/Idea">
									<s:param name="ideaId" value="#idea.id" />
								</s:url>
								<td><s:property value="#idea.title" /></td>
								<td><code>
										<s:property value="#idea.username" />
									</code></td>
								<td><code>
										<s:date name="#idea.pubDate" format="dd/MM/yyyy" />
									</code></td>
								<td><s:if test="#idea.comments.size > 0">
										<s:url var="commentListAction" action="list"
											namespace="/do/collaboration/Idea/Comment">
											<s:param name="ideaId" value="#idea.id" />
										</s:url>
										<span class="monospace"><a
											href="<s:property value="#commentListAction" />"
											title="<s:text name="jpcrowdsourcing.label.goToComment" />"><s:property
													value="#idea.comments.size" /></a></span>
									</s:if> <s:else>
										<span class="monospace"><s:text
												name="jpcrowdsourcing.label.zero" /></span>
									</s:else></td>
								<td><s:if test="#idea.status == 3">
										<s:set var="iconImage" id="iconImage">
											<wp:resourceURL />icon fa fa-check text-success</s:set>
										<s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
									</s:if> <s:if test="#idea.status == 2">
										<s:set var="iconImage" id="iconImage">
											<wp:resourceURL />icon fa fa-pause text-warning</s:set>
										<s:set var="isOnlineStatus"
											value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
									</s:if> <s:if test="#idea.status == 1">
										<s:set var="iconImage" id="iconImage">
											<wp:resourceURL />icon fa fa-pause text-warning</s:set>
										<s:set var="isOnlineStatus" value="%{getText('label.no')}" />
									</s:if> <span class="<s:property value="iconImage" />"
									title="<s:property value="isOnlineStatus" />"></span></td>
								<td class="table-view-pf-actions">
									<div class="dropdown dropdown-kebab-pf">
										<button class="btn btn-menu-right dropdown-toggle"
											type="button" data-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false">
											<span class="fa fa-ellipsis-v"></span>
										</button>
										<ul class="dropdown-menu dropdown-menu-right">
											<s:url action="trashReference" var="trashAction"
												namespace="/do/collaboration/Idea" escapeAmp="false">
												<s:param name="ideaId" value="#idea.id" />
												<s:param name="selectedNode"
													value="#request['selectedNode']" />
											</s:url>
											<li><a
												title="<s:text name="label.edit" />: <s:property value="#idea.title" />"
												href="<s:property value="#editAction"/>"
												title="<s:text name="label.edit" />"><s:text
														name="label.edit" /></a></li>
											<li><a href="<s:property value="#trashAction" />"
												title="<s:text name="label.delete" />"><s:text
														name="label.delete" /></a></li>
										</ul>
									</div>
								</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>

				<div class="pager">
					<s:include
						value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>

			</wpsa:subset>
		</s:if>
		<s:else>
			<div class="alert alert-info">
				<s:text name="note.category.referencedContents.empty" />
			</div>
		</s:else>
	</div>
</div>
