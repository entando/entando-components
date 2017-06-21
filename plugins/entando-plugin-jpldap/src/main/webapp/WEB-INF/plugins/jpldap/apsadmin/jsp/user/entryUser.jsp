<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url namespace="/do/BaseAdmin" action="settings" />"><s:text name="menu.configure" /></a></li>
    <li><a href="<s:url namespace="/do/User" action="list" />"><s:text name="title.userManagement" /></a></li>
    <li class="page-title-container">	
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.userManagement.userNew" />
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="title.userManagement.userEdit" />
        </s:elseif>
    </li>
</ol>

<h1 class="page-title-container">
    <s:if test="getStrutsAction() == 1">
        <s:text name="title.userManagement.userNew" />
    </s:if>
    <s:elseif test="getStrutsAction() == 2">
        <s:text name="title.userManagement.userEdit" />
    </s:elseif>
    <s:text name="jpldap.name"/>
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="jpldap.title.help" />" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<br>
<s:form action="save" cssClass="form-horizontal">
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.FieldErrors" /></strong>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.ActionErrors" /></strong>
            <ul class="margin-base-top">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <s:if test="getStrutsAction() == 2">
            <wpsf:hidden name="username" />
            <wpsf:hidden name="remoteUser" />
        </s:if>
    </p>

    <%-- username --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['username']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label for="username"><s:text name="username" /></label>
            <wpsf:textfield name="username" id="username" disabled="%{getStrutsAction() == 2}" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>
    <%-- password --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['password']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label for="password"><s:text name="password" /></label>
            <wpsf:password name="password" id="password" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>
    <%-- confirm password --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['passwordConfirm']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label for="passwordConfirm"><s:text name="passwordConfirm" /></label>
            <wpsf:password name="passwordConfirm" id="passwordConfirm" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>
    <%-- active --%>
    <s:if test="(strutsAction == 1 && !isWriteUserEnable()) || (strutsAction == 2 && !isRemoteUser())">
        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['active']}" />
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-12">
                <label class="checkbox">
                    <wpsf:checkbox name="active" id="active" />
                    <s:text name="note.userStatus.active" />
                </label>
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
    </s:if>
    <%-- additional info when edit mode --%>
    <s:if test="getStrutsAction() == 2 && !isRemoteUser()">
        <div class="panel panel-default">
            <div class="panel-body">
                <%-- registration date --%>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label><s:text name="label.date.registration" /></label>
                        <p class="form-control-static">
                            <s:date name="user.creationDate" format="dd/MM/yyyy HH:mm" />
                        </p>
                    </div>
                </div>
                <%-- last login --%>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label><s:text name="label.date.lastLogin" /></label>
                        <p class="form-control-static">
                            <s:if test="user.lastAccess != null">
                                <s:date name="user.lastAccess" format="dd/MM/yyyy HH:mm" />
                                <s:if test="!user.accountNotExpired">
                                    <span class="text-muted">&#32;(<s:text name="note.userStatus.expiredAccount" />)</span>
                                </s:if>
                            </s:if>
                            <s:else><span class="icon fa fa-minus" title="<s:text name="label.none" />"><span class="sr-only"><s:text name="label.none" /></span></span></s:else>
                            </p>
                        </div>
                    </div>
                <%-- last password change --%>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label><s:text name="label.date.lastPasswordChange" /></label>
                        <p class="form-control-static">
                            <s:if test="user.lastPasswordChange != null">
                                <s:date name="user.lastPasswordChange" format="dd/MM/yyyy HH:mm" />
                                <s:if test="!user.credentialsNotExpired">
                                    <span class="text-muted">&#32;(<s:text name="note.userStatus.expiredPassword" />)</span>
                                </s:if>
                            </s:if>
                            <s:else><span class="icon fa fa-minus" title="<s:text name="label.none" />"><span class="sr-only"><s:text name="label.none" /></span></span></s:else>
                            </p>
                        </div>
                    </div>
                <%-- reset info --%>
                <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['reset']}" />
                <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
                <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
                    <div class="col-xs-12">
                        <label class="checkbox">
                            <wpsf:checkbox name="reset" />
                            <s:text name="note.userStatus.reset" />
                        </label>
                        <s:if test="#fieldHasFieldErrorVar">
                            <span class="help-block text-danger">
                                <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                                </span>
                        </s:if>
                    </div>
                </div>
            </div>
        </div>
    </s:if>
    <s:else>
        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['profileTypeCode']}" />
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-12">
                <label for="profileType"><s:text name="profileType" /></label>
                <wpsf:select name="profileTypeCode" id="profileType" list="profileTypes" listKey="code" listValue="description" cssClass="form-control" />
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
    </s:else>
    <%-- save buttons --%>
    <div class="form-group">
        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
            <wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
                <span class="icon fa fa-floppy-o"></span>&#32;
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
        <s:if test="strutsAction == 1">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" action="saveAndContinue" cssClass="btn btn-default btn-block">
                    <span class="icon fa fa-arrow-right"></span>&#32;
                    <s:text name="label.saveAndEditProfile" />
                </wpsf:submit>
            </div>
        </s:if>
    </div>	
</s:form>