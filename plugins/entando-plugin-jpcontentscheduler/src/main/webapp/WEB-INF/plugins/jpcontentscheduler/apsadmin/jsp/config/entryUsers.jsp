<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpcontentscheduler.integrations"/></li>
    <li><s:text name="jpcontentscheduler.components"/></li>
    <li><s:text name="jpcontentscheduler.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="title.contentList"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpcontentscheduler.admin.menu"/>
                <span class="pull-right">
                    <s:text name="jpcontentscheduler.section.help" var="helpVar"/>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="${helpVar}" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.general"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.mail"/>
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="viewUsers"/>">
                        <s:text name="jpcontentscheduler.admin.users"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewContentTypes"/>">
                        <s:text name="jpcontentscheduler.admin.contentTypes"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main" role="main">

    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:set var="isAllTypeSelected" value="false"/>
    <s:if test="%{null != username}">
        <s:set var="username" value="username"/>
        <s:if test="%{getUsersContentType()[#username].get(0).equalsIgnoreCase('*')}">
            <s:set var="isAllTypeSelected" value="true"/>
        </s:if>
    </s:if>
    <s:else>
        <s:set var="username" value="%{''}"/>
    </s:else>


    <s:form id="configurationForm" name="configurationForm" method="post" action="#" cssClass="form-horizontal" accept-charset="utf-8">
        <legend><s:text name="legend.User" /></legend>
        <div class="form-group">
            <label class="col-sm-2 control-label"><s:text name="jpcontentscheduler.label.username"/></label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['username']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="username" id="username" placeholder="%{getText('jpcontentscheduler.label.username')}" cssClass="form-control" value="%{#username}"/>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"><s:text name="jpcontentscheduler.label.contentTypes"/></label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.contentTypes']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <div class="input-group">
                    <wpsf:select name="contentType" id="threadConfig_contentTypes" list="%{getContentTypes()}" 
                                 listKey="code" listValue="description" headerKey="*" headerValue="%{getText('label.all')}" cssClass="form-control" disabled="%{#isAllTypeSelected}"/>
                    <span class="input-group-btn">
                        <wpsf:submit type="button" action="addUserContentType" cssClass="btn btn-primary" disabled="%{#isAllTypeSelected}">
                            <s:text name="label.add"/>
                        </wpsf:submit>
                    </span>
                </div>
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>

                <s:if test="%{getUsersContentType()[#username].size()>0}">
                    <h3 class="sr-only">
                        <s:text name="title.contentType.list" />
                    </h3>
                    <ul class="list-inline mt-20">
                        <s:iterator value="%{getUsersContentType()[#username]}" var="currentContentTypeCode" status="rowstatus">
                            <s:if test="%{#currentContentTypeCode.equalsIgnoreCase('*')}">
                                <li>
                                    <span class="label label-tag">
                                        <s:text name="jpcontentscheduler.label.contentTypes.all"/>
                                        <wpsa:actionParam action="removeUserContentType" var="actionName">
                                            <wpsa:actionSubParam name="contentType" value="%{#currentContentTypeCode}" />
                                            <wpsa:actionSubParam name="username" value="%{username}" />
                                        </wpsa:actionParam>
                                        <wpsf:submit type="button" action="%{#actionName}"
                                                     title="%{getText('label.remove')"
                                                     cssClass="btn btn-link">
                                            <span class="pficon pficon-close white"></span>
                                            <span class="sr-only">x</span>
                                        </wpsf:submit>
                                    </span>
                                </li>
                            </s:if>
                            <s:else>
                                <li>
                                    <span class="label label-tag">
                                        <wpsa:set name="currentContentType" value="%{getContentType(#currentContentTypeCode)}" />
                                        <abbr title="<s:property value="#currentContentType.description"/>">
                                            <s:property value="#currentContentTypeCode" />
                                        </abbr>
                                        &#32; &#32;
                                        <wpsa:actionParam action="removeUserContentType" var="actionName">
                                            <wpsa:actionSubParam name="contentType" value="%{#currentContentTypeCode}" />
                                            <wpsa:actionSubParam name="username" value="%{username}" />
                                        </wpsa:actionParam>

                                        <wpsf:submit type="button" action="%{#actionName}"
                                                     title="%{getText('label.remove') + ' ' + getDefaultFullTitle(#currentContentTypeCode)}"
                                                     cssClass="btn btn-link">
                                            <span class="pficon pficon-close white"></span>
                                            <span class="sr-only">x</span>
                                        </wpsf:submit>

                                    </span>
                                </li>
                            </s:else>
                        </s:iterator>
                    </ul>
                </s:if>
                <s:else>
                    <div class="alert alert-info mt-20">
                        <span class="pficon pficon-info"></span>
                        <s:text name="jpcontentscheduler.contentTypes.empty"/>
                    </div>
                </s:else>

            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-12">
                <div class="pull-right">
                    <wpsf:submit name="save" type="button" action="saveUsersContentType" cssClass="btn btn-primary" >
                        <s:text name="%{getText('label.add')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>