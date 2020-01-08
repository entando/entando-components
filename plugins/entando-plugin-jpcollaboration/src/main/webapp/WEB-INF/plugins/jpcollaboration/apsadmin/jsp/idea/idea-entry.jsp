<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <s:text name="jpcrowdsourcing.admin.title" />
    </li>
    <li class="page-title-container">
        <s:text name="jpcrowdsourcing.title.ideaEntry" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.title" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:text name="jpcrowdsourcing.title.ideaEntry.help" />" data-placement="left"
                        data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />">
                            <s:text name="jpcrowdsourcing.ideaInstance.list" />
                        </a>
                    </li>
                    <li class="active">
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea" />">
                            <s:text name="jpcrowdsourcing.idea.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />">
                            <s:text name="jpcrowdsourcing.comment.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />">
                            <s:text name="jpcrowdsourcing.config" />
                        </a>
                    </li>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>
<div id="main">
    <s:form action="save" cssClass="form-horizontal">
        <div id="messages">
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
        </div>
        <!-- Info -->
        <fieldset class="col-xs-12">
            <legend>
                <s:text name="label.info" />
            </legend>
            <p class="noscreen">
                <wpsf:hidden name="idea.id" value="%{idea.id}" />
            </p>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="idea_instanceCode">
                    <s:text name="jpcrowdsourcing.label.instance" />
                </label>
                <div class="col-sm-10">
                    <wpsf:select name="idea.instanceCode" id="idea_instanceCode" list="ideaInstances" listKey="code"
                        listValue="code" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="idea.title">
                    <s:text name="jpcrowdsourcing.label.title" />
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield id="idea.title" name="idea.title" value="%{idea.title}" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="idea.descr" class="alignTop">
                    <s:text name="jpcrowdsourcing.label.description" />
                </label>
                <div class="col-sm-10">
                    <wpsf:textarea id="idea.descr" name="idea.descr" value="%{idea.descr}" cols="50" rows="3"
                        cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <s:set var="statusMap"
                    value="#{
                       1:'jpcrowdsourcing.label.status_not_approved.singular',
                           3:'jpcrowdsourcing.label.status_approved.singular'
                       }" />
                <label class="col-sm-2 control-label" for="idea.descr" class="alignTop">
                    <s:text name="jpcrowdsourcing.label.approved" />
                </label>
                <div class="col-sm-10">
                    <div class="btn-group" data-toggle="buttons">
                        <label for="idea.status.1" class="btn btn-default active">
                            <wpsf:radio name="idea.status" value="1" id="idea.status.1"
                                checked="idea.status==1||idea.status==2" />
                            <s:text name="jpcrowdsourcing.label.status_not_approved.singular" />
                        </label>
                        <label for="idea.status.3" class="btn btn-default">
                            <wpsf:radio name="idea.status" value="3" id="idea.status.3" checked="idea.status==3" />
                            <s:text name="jpcrowdsourcing.label.status_approved.singular" />
                        </label>
                    </div>
                </div>
            </div>
            <s:if test="idea.id != null">
                <div class="form-group">
                    <div class="col-sm-10 col-sm-offset-2">
                        <s:if test="%{idea.comments.size()>0}">
                            <div class="panel panel-default panel-body">
                                <s:property value="idea.comments.size" />
                                &#32;
                                <s:text name="jpcrowdsourcing.label.comments" />
                            </div>
                        </s:if>
                        <s:else>
                            <div class="alert alert-info">
                              <span class="pficon pficon-info"></span>
                              <s:text name="jpcrowdsourcing.note.nocomments" />
                            </div>
                        </s:else>
                    </div>
                </div>
            </s:if>
        </fieldset>
        <!-- Categories -->
        <fieldset class="col-xs-12">
            <legend>
                <s:text name="jpcrowdsourcing.title.tag" />
            </legend>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="tag">
                    <s:text name="jpcrowdsourcing.label.tagSet" />
                </label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <wpsf:select list="%{getIdeaTags(false)}" name="tag" id="tag" headerKey="" headerValue="%{getText('note.choose')}" listKey="code" listValue="title"
                            cssClass="form-control" />
                        <span class="input-group-btn">
                            <wpsf:submit type="button" action="joinCategory" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.add')}" />
                            </wpsf:submit>
                        </span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-10 col-sm-offset-2">
                    <s:set var="ideaCategories" value="idea.tags" />
                    <s:if test="#ideaCategories != null && #ideaCategories.size() > 0">
                        <s:iterator value="#ideaCategories" var="ideaCategoryCode">
                            <p class="noscreen">
                                <wpsf:hidden name="idea.tags" value="%{#ideaCategoryCode}" />
                            </p>
                        </s:iterator>
                        
                        <ul class="list-inline">
                            <s:iterator value="#ideaCategories" var="ideaCategoryCode">
                                <s:set var="ideaCategory" value="%{getCategory(#ideaCategoryCode)}" />
                                <li>
                                    <span class="label label-info">
                                        <span class="icon fa fa-tag"></span>&#32;
                                        <abbr title="<s:property value="%{getFullTitle(#ideaCategory, currentLang.code)}"/>"><s:property value="%{getShortFullTitle(#ideaCategory, currentLang.code)}" /></abbr>&#32;
                                        <wpsa:actionParam action="removeCategory" var="actionName" >
                                            <wpsa:actionSubParam name="categoryCode" value="%{#ideaCategory.code}" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + getDefaultFullTitle(#ideaCategory)}" cssClass="btn btn-link">
                                            <span class="pficon pficon-close white"></span>
                                            <span class="sr-only">x</span>
                                        </wpsf:submit>
                                    </span>
                                </li>
                            </s:iterator>
                        </ul>
                    </s:if>
                </div>
            </div>
        </fieldset>
        
        <div class="col-xs-12">
            <wpsf:submit action="save" type="button" cssClass="btn btn-primary pull-right">
                <s:text name="label.save"/>
            </wpsf:submit>
        </div>
    </s:form>
</div>
