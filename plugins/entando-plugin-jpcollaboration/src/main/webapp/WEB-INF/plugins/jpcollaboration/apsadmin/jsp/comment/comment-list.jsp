<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpcrowdsourcing" uri="/jpcrowdsourcing-apsadmin-core" %>

<jpcrowdsourcing:idea key="%{#comments.ideaId}" var="idea"/>
<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;<s:text name="jpcrowdsourcing.title.comments" /></span></h1>

<div id="main">

<s:form action="search" cssClass="form-horizontal">

	<s:set var="statusMap" value="#{
	1:'jpcrowdsourcing.label.status_not_approved_m',
	2:'jpcrowdsourcing.label.status_to_approve_m',
	3:'jpcrowdsourcing.label.status_approved_m'
	}" />

    <div class="form-group">
        <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <span class="input-group-addon">
                <span class="icon fa fa-file-text-o fa-lg" 
                    title="<s:text name="label.search.by"/>&#32;<s:text name="label.code" />">
                </span>
            </span>  
<!--            <label for="commentText"><s:text name="label.search.by"/>&#32;<s:text name="label.description"/></label>-->
            <wpsf:textfield name="commentText" id="commentText" cssClass="form-control input-lg"  />    
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
        
        <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div id="search-advanced" class="collapse well collapse-input-group">
                
                <div class="form-group">
                    <label for="status" class="control-label col-sm-2 text-right"><s:text name="label.state"/></label>
                    <div class="col-sm-5" id="content_list-changeContentType">                 
                        <wpsf:select cssClass="form-control" name="searchStatus" id="status" list="%{#statusMap}" headerKey="" listValue="%{getText(value)}" headerValue="%{getText('jpcrowdsourcing.label.anystatus_m')}" />
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


<div class="subsection-light">

<s:form action="search" >
	<p class="noscreen">
		<wpsf:hidden name="commentText" />
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

<wpsa:subset source="comments" count="10" objectName="groupComment" advanced="true" offset="5">
<s:set name="group" value="#groupComment" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<s:if test="%{getComments().size() > 0}">
<table class="table table-bordered">
	<tr>
		<th class="text-center text-nowrap"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
		<th><s:text name="jpcrowdsourcing.label.description" /></th>
		<th><s:text name="jpcrowdsourcing.label.author" /></th>
		<th class="text-center text-nowrap"><s:text name="jpcrowdsourcing.label.date" /></th>
	</tr>
	<s:iterator var="commentId">
		<s:set var="comment" value="%{getComment(#commentId)}" />
		<tr>                  
			<td class="text-center text-nowrap">
				<s:if test="#comment.status == 3">
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/collaboration/administration/img/status_ok.png</s:set>
					<s:set name="isOnlineStatus" value="%{getText('label.yes')}" />
				</s:if>
				<s:if test="#comment.status == 2">
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/collaboration/administration/img/status_standby.png</s:set>
					<s:set name="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
				</s:if>
				<s:if test="#comment.status == 1">
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/collaboration/administration/img/status_ko.png</s:set>
					<s:set name="isOnlineStatus" value="%{getText('label.no')}" />
				</s:if>
                            <div class="btn-group btn-group-xs">
				<s:iterator value="#statusMap" var="entry">
					<s:if test="#comment.status != #entry.key">
						<s:url action="changeStatus" var="changeStatusAction">
							<s:param name="commentId" value="#comment.id" />
							<s:param name="status" value="#entry.key" />
						</s:url>
						<s:if test="#entry.key == 1">
                                                    <a class="btn btn-default" 
                                                       title="<s:text name="jpcrowdsourcing.status.approved" />. <s:text name="jpcrowdsourcing.label.suspend" />" 
                                                       href="<s:property value="#changeStatusAction" escapeHtml="false" />">
                                                        <span class="icon fa fa-check"></span>
                                                        <span class="sr-only"><s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" /></span>
                                                    </a>
						</s:if>
						<s:if test="#entry.key == 3">
                                                    <a class="btn btn-default" 
                                                       title="<s:text name="jpcrowdsourcing.status.suspended" />. <s:text name="jpcrowdsourcing.label.approve" />" 
                                                       href="<s:property value="#changeStatusAction" escapeHtml="false" />">
                                                        <span class="icon fa fa-pause"></span>
                                                        <span class="sr-only"><s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" /></span>
                                                    </a>
						</s:if>
					</s:if>
				</s:iterator>
                                <a class="btn btn-default" title="<s:text name="jpcrowdsourcing.title.comments.view" />" href="<s:url action="view"><s:param name="commentId" value="#comment.id" /></s:url>">
                                    <span class="icon fa fa-info"></span>
                                    <span class="sr-only"><s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" /></span>
                                </a>
                            </div>                                        
                            <div class="btn-group btn-group-xs">   
                                <a class="btn btn-warning btn-xs" href="<s:url action="trash"><s:param name="commentId" value="#comment.id" /></s:url>" title="<s:text name="jpcrowdsourcing.label.delete" />">
                                    <span class="sr-only"></span>
                                    <span class="icon fa fa-times-circle-o"></span>
                                </a>
                            </div> 
			</td>
			<td>
				<s:property value="#comment.title" /><s:set var="currentComment" value="%{#comment.comment.trim()}" /><s:if test="%{#currentComment.length()>80}"><s:property value="%{#currentComment.substring(0,80)}" />&hellip;</s:if><s:else><s:property value="%{#currentComment}" /></s:else>
			</td>
			<td>
                                <code><s:property value="#comment.username"/></code>
			</td>
			<td class="text-center text-nowrap">
                            <code><s:date name="#comment.creationDate" format="dd/MM/yyyy" /></code>
			</td>
		</tr>
	</s:iterator>
</table>
</s:if>
<s:else>
<div class="alert alert-info"><s:text name="jpcrowdsourcing.note.comments.empty"></s:text></div>
</s:else>

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

</s:form>
</div>
</div>