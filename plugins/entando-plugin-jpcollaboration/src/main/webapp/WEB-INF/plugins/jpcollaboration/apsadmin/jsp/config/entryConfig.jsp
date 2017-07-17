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
        <s:text name="jpcrowdsourcing.title.configure" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.title" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:text name="jpcrowdsourcing.title.configure.help" />" data-placement="left"
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
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea" />">
                            <s:text name="jpcrowdsourcing.idea.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />">
                            <s:text name="jpcrowdsourcing.comment.list" />
                        </a>
                    </li>
                    <li class="active">
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
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
    <s:form action="saveConfig" class="form-horizontal">
        <fieldset class="col-xs-12">
            <legend>
                <s:text name="jpcrowdsourcing.title.moderation" />
            </legend>
            <div class="alert alert-info">
                <span class="pficon pficon-info"></span>
                <s:text name="jpcrowdsourcing.note.moderation" />
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="moderateEntries">
                    <s:text name="jpcrowdsourcing.label.idea.moderation" />
                </label>
                <div class="col-sm-10">
                    <wpsf:checkbox name="moderateEntries" id="moderateEntries" value="%{moderateEntries}"
                        cssClass="bootstrap-switch" />
                </div>
             </div>
             <div class="form-group">
                <label class="col-sm-2 control-label" for="moderateComments">
                    <s:text name="jpcrowdsourcing.label.comment.moderation" />
                </label>
                <div class="col-sm-10">
                    <wpsf:checkbox name="moderateComments" id="moderateComments" value="%{moderateComments}"
                        cssClass="bootstrap-switch" />
                </div>
            </div>
        </fieldset>
        <div class="col-xs-12">
            <s:submit type="button" cssClass="btn btn-primary pull-right">
                <s:text name="%{getText('label.save')}" />
            </s:submit>
        </div>
    </s:form>
</div>
