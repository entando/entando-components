<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpavatar.admin.menu.integration"/></li>
    <li>
        <s:text name="breadcrumb.integrations.components"/>
    </li>
    <li><s:text name="jpavatar.title.avatar" /></li>
    <li class="page-title-container">
        <s:text name="title.avatar.settings"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="jpavatar.title.avatar" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="title.avatarManagement.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url namespace="/do/jpavatar/Config" action="management" />"><s:text name="title.avatarManagement"/></a>
                </li>
                <li class="active">
                    <a href="<s:url namespace="/do/jpavatar/Config" action="edit" />"><s:text name="title.avatar.settings"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">
    <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />
    <s:form action="saveConfig" >
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
                <ul class="margin-base-vertical">
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <fieldset class="main">
            <div class="form-group">
                <span class="important"><s:property value="SYTLE" /></span>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label">
                    <span class="display-block"><s:text name="label.chooseYourEditor"/></span>
                </div>
                <div class="col-xs-10 ">
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-default <c:if test="${null == style || style == 'default'}"> active</c:if>">
                            <s:text name="label.avatarConfig.style.default"/>
                            <wpsf:radio name="avatarConfig.style" value="default" id="default_avatarConfig_style" checked="%{null == avatarConfig.style || avatarConfig.style == 'default'}" cssClass="radiocheck hidden" />
                        </label>
                        <label class="btn btn-default <c:if test="${style == 'local'}"> active</c:if>">
                            <s:text name="label.avatarConfig.style.local"/>
                            <wpsf:radio name="avatarConfig.style" value="local" id="local_avatarConfig_style" checked="%{avatarConfig.style == 'local'}" cssClass="radiocheck hidden" />
                        </label>
                        <label class="btn btn-default <c:if test="${style == 'gravatar'}"> active</c:if>">
                            <s:text name="label.avatarConfig.style.gravatar"/>
                            <wpsf:radio name="avatarConfig.style" value="gravatar" id="gravatar_avatarConfig_style" checked="%{avatarConfig.style == 'gravatar'}" cssClass="radiocheck hidden" />
                        </label>
                    </div>
                </div>
            </div>
        </fieldset>
        <br>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right" >
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
