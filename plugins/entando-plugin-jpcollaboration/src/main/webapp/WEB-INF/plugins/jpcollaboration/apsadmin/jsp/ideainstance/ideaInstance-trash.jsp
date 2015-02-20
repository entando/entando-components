<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.title.ideaInstanceManagement" /></span></h1>

<div id="main">
	<s:form action="delete" cssClass="form-horizontal">
		<div class="alert alert-warning">
			<wpsf:hidden name="strutsAction" />
			<wpsf:hidden name="code" />
			<s:text name="note.deleteIdeaInstance.areYouSure" />&#32;<code><s:property value="code"/></code>?
                        <div class="text-center margin-large-top">
                            <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                                <span class="icon fa fa-times-circle"></span>&#32;
                                <s:text name="%{getText('label.remove')}"/>
                            </wpsf:submit> 
                            <p class="text-center margin-small-top">
                                <a class="btn btn-link" href="<s:url action="list"/>">
                                    <s:text name="%{getText('label.back')}"/>
                                </a>
                            </p>    
                        </div>        

		</div>

		<s:set name="ideaInstance_var" value="%{getIdeaInstance(code)}" />
		<s:if test="#ideaInstance_var.children.size > 0">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="margin-none"><s:text name="jpcrowdsourcing.label.delete.reference"/></h3>   
                        </div>
                        <div class="panel-body">
			<table class="table table-bordered">
				<tr>
					<th><s:text name="jpcrowdsourcing.label.title" /></th>
					<th><s:text name="jpcrowdsourcing.label.instance" /></th>
					<th><s:text name="jpcrowdsourcing.label.author" /></th>
					<th class="text-center"><s:text name="jpcrowdsourcing.label.date" /></th>
					<th class="text-right"><s:text name="jpcrowdsourcing.label.comments" /></th>
				</tr>
				<s:iterator value="#ideaInstance_var.children" var="ideaId">
				<tr>
					<s:set var="idea" value="%{getIdea(#ideaId)}" />
					<s:url action="edit" namespace="/do/collaboration/Idea" var="editAction">
						<s:param name="ideaId" value="#idea.id" />
					</s:url>
					<td>
						<a href="<s:property value="#editAction"/>" title="<s:text name="label.edit" />"><s:property value="#idea.title"/></a>
					</td>
					<td>
						<s:url action="list" namespace="/do/collaboration/IdeaInstance" var="link_to_instancelist">
							<s:param name="code" value="#idea.instanceCode" />
						</s:url>
						<a href="<s:property value="#link_to_instancelist" />">
                                                    <s:property value="#idea.instanceCode"/>
						</a>
					</td>
					<td>
                                            <code><s:property value="#idea.username"/></code>
					</td>
					<td class="text-center">
                                            <code><s:date name="#idea.pubDate" format="dd/MM/yyyy" /></code>
					</td>
					<td class="text-right">
						<s:if test="#idea.comments.size > 0">
							<s:url var="commentListAction" action="list" namespace="/do/collaboration/Idea/Comment">
								<s:param name="ideaId" value="#idea.id" />
							</s:url>
							<span class="monospace"><a href="<s:property value="#commentListAction" />" title="<s:text name="jpcrowdsourcing.label.goToComment" />"><s:property value="#idea.comments.size"/></a></span>
						</s:if>
						<s:else>
							<span class="monospace"><s:text name="jpcrowdsourcing.label.zero" /></span>
						</s:else>
					</td>

					</tr>
				</s:iterator>
			</table>
                        </div>
                    </div>

		</s:if>
	</s:form>
</div>