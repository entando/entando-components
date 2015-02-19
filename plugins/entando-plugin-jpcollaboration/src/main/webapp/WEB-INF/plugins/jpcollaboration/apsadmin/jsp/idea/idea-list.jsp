<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;<s:text name="jpcrowdsourcing.title.ideaList" /></span></h1>
<div id="main">

<s:form action="search" cssClass="form-horizontal">
    <div class="form-group">
        <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <span class="input-group-addon">
                <span class="icon fa fa-file-text-o fa-lg" 
                    title="<s:text name="label.search.by"/>&#32;<s:text name="label.code" />">
                </span>
            </span>        

            <c:set var="pholder"><s:text name="label.code"/></c:set>
            <wpsf:textfield name="text" id="text" cssClass="form-control input-lg"  placeholder="${pholder}"/>
                <span class="input-group-btn">
                    <wpsf:submit type="button" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
                        <span class="sr-only"><s:text name="%{getText('label.search')}" /></span>
                        <span class="icon fa fa-search"></span>
                    </wpsf:submit>
                                
                    <button type="button" class="btn btn-primary btn-lg dropdown-toggle" 
                        data-toggle="collapse" 
                        data-target="#search-advanced" title="Refine your search">
                        <span class="sr-only"><s:text name="title.searchFilters" /></span>
                        <span class="caret"></span>
                    </button>    
                </span>
        </div>
<!--        <label for="text"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>-->
        
        <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div id="search-advanced" class="collapse well collapse-input-group">
                
                <div class="form-group">
                    <label for="status" class="control-label col-sm-2 text-right"><s:text name="label.state"/></label>
                    <div class="col-sm-5">  
                        <s:set var="statusMap" value="#{
                                    1:'jpcrowdsourcing.label.status_not_approved',
                                    2:'jpcrowdsourcing.label.status_to_approve',
                                    3:'jpcrowdsourcing.label.status_approved'
                                    }"
                        />
			<wpsf:select cssClass="form-control" name="searchStatus" id="status" list="%{#statusMap}" headerKey="" listValue="%{getText(value)}" headerValue="%{getText('jpcrowdsourcing.label.anystatus')}" />
                    </div>
                </div>
                    
                <div class="form-group">
                    <label for="ideaInstance" class="control-label col-sm-2 text-right"><s:text name="jpcrowdsourcing.label.instance"/></label>
                    <div class="col-sm-5">
                        <wpsf:select cssClass="form-control" name="ideaInstance" id="ideaInstance" list="ideaInstances" listKey="code" listValue="code" headerKey="" headerValue="%{getText('label.all')}" />   
                    </div>
                </div>
                    
                <div class="form-group">
                    <div class="col-sm-5 col-sm-offset-2">
                        <wpsf:submit type="button" cssClass="btn btn-primary">
                            <s:text name="%{getText('label.search')}" />
                            <span class="icon fa fa-search"></span>
                        </wpsf:submit>    
                    </div>    
                </div>
                    
            </div>
        </div>            
    </div>

</s:form>

<s:form action="search">
	<div class="subsection-light">

		<p class="noscreen">
			<wpsf:hidden name="text" />
			<wpsf:hidden name="searchStatus" />
		</p>

		<s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                            <ul class="margin-base-vertical">
				<s:iterator value="ActionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
                            </ul>
		</div>
		</s:if>
		<s:if test="hasActionMessages()">
                <div class="alert alert-info alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                        <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
                            <ul class="margin-base-vertical">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
                            </ul>
		</div>
		</s:if>

	<wpsa:subset source="ideas" count="10" objectName="groupIdea" advanced="true" offset="5">
	<s:set name="group" value="#groupIdea" />

	<div class="pager">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>

	<s:if test="%{getIdeas().size() > 0}">
	<table class="table table-bordered">
		<tr>
			<th class="text-center text-nowrap"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
			<th><s:text name="jpcrowdsourcing.label.title" /></th>
			<th><s:text name="jpcrowdsourcing.label.instance" /></th>
			<th><s:text name="jpcrowdsourcing.label.author" /></th>
			<th class="text-center text-nowrap"><s:text name="jpcrowdsourcing.label.date" /></th>
			<th class="text-right"><s:text name="jpcrowdsourcing.label.comments" /></th>
			<th class="text-center text-nowrap"><abbr title="<s:text name="jpcrowdsourcing.label.approved" />"><s:text name="jpcrowdsourcing.label.approved.short" /></abbr></th>
		</tr>
		<s:iterator var="ideaId">
		<tr>
			<s:set var="idea" value="%{getIdea(#ideaId)}" />
			<s:url action="edit" var="editAction">
				<s:param name="ideaId" value="#idea.id" />
			</s:url>

			<td class="text-center text-nowrap">
                            <div class="btn-group btn-group-xs">
                                <a class="btn btn-default" title="<s:text name="label.edit" />: <s:property value="#idea.title" />" href="<s:property value="#editAction"/>" title="<s:text name="label.edit" />">
                                    <span class="icon fa fa-pencil-square-o"></span>
                                    <span class="sr-only"><s:text name="label.edit" />: <s:property value="#idea.title" /></span>
                                </a>
				<s:iterator value="#statusMap" var="entry">
					<s:if test="#idea.status != #entry.key">
						<s:url action="changeStatus" var="changeStatusAction"><s:param name="ideaId" value="#idea.id" /><s:param name="status" value="#entry.key" /></s:url>
						<s:if test="#entry.key == 1">
                                                    <a class="btn btn-default" href="<s:property value="#changeStatusAction" escapeHtml="false" />">
                                                        <span class="icon fa fa-pause" title="<s:text name="jpcrowdsourcing.label.suspend" />"></span>
                                                    </a>
                                                </s:if>
						<%--<s:if test="#entry.key == 2"><a href="<s:property value="#changeStatusAction" escapeHtml="false" />"><img src="<wp:resourceURL/>plugins/collaboration/administration/img/status_standby.png" alt="" title="" />[riporta a ]</a></s:if>--%>
						<s:if test="#entry.key == 3">
                                                    <a class="btn btn-default" href="<s:property value="#changeStatusAction" escapeHtml="false" />">
                                                        <span class="icon fa fa-check" title="<s:text name="jpcrowdsourcing.label.approve" />"></span>     
                                                    </a>
                                                </s:if>
					</s:if>
				</s:iterator>
                            </div>
                                <div class="btn-group btn-group-xs">   
                                    <a class="btn btn-warning btn-xs" href="<s:url action="trash"><s:param name="ideaId" value="#idea.id" /></s:url>" title="<s:text name="jpcrowdsourcing.label.delete" />: <s:property value="#idea.title"/>">
                                        <span class="sr-only"></span>
                                        <span class="icon fa fa-times-circle-o"></span>
                                    </a>
                                </div>                                                 
			</td>
			<td>
                            <code><s:property value="#idea.title"/></code>
                                
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
			<td class="text-center text-nowrap">
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
			<td class="text-center text-nowrap">
				<s:if test="#idea.status == 3">
					<s:set name="iconImage" id="iconImage">icon fa fa-check text-success</s:set>
					<s:set name="isOnlineStatus" value="%{getText('label.yes')}" />
				</s:if>
				<s:if test="#idea.status == 2">
					<s:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
					<s:set name="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
				</s:if>
				<s:if test="#idea.status == 1">
					<s:set name="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
					<s:set name="isOnlineStatus" value="%{getText('label.no')}" />
				</s:if>
                                <span class="<s:property value="iconImage" />" title="<s:property value="isOnlineStatus" />"></span>
			</td>
			</tr>
		</s:iterator>
	</table>
	</s:if>
	<s:else>
		<div class="alert alert-info"><s:text name="jpcrowdsourcing.idea.null" /></div>
	</s:else>

	<div class="pager">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>

	</wpsa:subset>

</s:form>
</div>
</div>