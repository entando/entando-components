<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpavatar.admin.menu.integration"/></li>
    <li>
        <s:text name="jpavatar.admin.menu.uxcomponents"/>
    </li>
    <li class="page-title-container">
        <s:text name="title.avatarManagement"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <%-- DA CAPIRE --%>
                <s:text name="title.avatarManagement"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="TO be inserted" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url namespace="/do/jpavatar/Config" action="management" />"><s:text name="title.avatarManagement"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpavatar/Config" action="edit" />"><s:text name="title.avatar.settings"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <strong><s:text name="message.title.FieldErrors" /></strong>.
            <ul>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul>
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:if test="hasActionMessages()">
        <div class="alert alert-success alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-ok"></span>
            <strong><s:text name="messages.confirm" /></strong>
            <s:iterator value="actionMessages">
                <span><s:property escapeHtml="false" /></span> &nbsp;
            </s:iterator>
        </div>
    </s:if>

    <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="style" />
    <p><s:text name="jpavatar.label.current.avatar" /></p>
    <img src="<s:url action="avatarStream" namespace="/do/currentuser/avatar"><s:param name="gravatarSize">34</s:param></s:url>"/>
    <c:if test="${style == 'local'}">
        <c:choose>
            <c:when test="${null == currentAvatar}">
                <s:form cssClass="form-horizontal" namespace="/do/jpavatar/Avatar" action="save" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="jpavatar_file"><s:text name="label.avatarImage" /></label>
                            <s:file name="avatar" />
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                                <s:text name="label.save" />
                            </wpsf:submit>
                        </div>
                    </div>
                </s:form>
            </c:when>
            <c:otherwise>
                <s:form namespace="/do/jpavatar/Avatar" action="bin" cssClass="form-horizontal">
                    <div class="form-group">
                        <div class="col-sm-12">
                            <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                                <s:text name="%{getText('label.remove')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </s:form>
            </c:otherwise>
        </c:choose>
    </c:if>
</div>