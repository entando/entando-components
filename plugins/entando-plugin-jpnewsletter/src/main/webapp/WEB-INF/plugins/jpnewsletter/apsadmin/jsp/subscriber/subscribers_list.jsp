<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpnewsletter.integrations"/></li>
    <li><s:text name="jpnewsletter.components"/></li>
    <li><s:text name="jpnewsletter.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="title.jpnewsletter.subscribersManagement"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpnewsletter.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpnewsletter.section.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />">
                        <s:text name="jpnewsletter.admin.list"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />">
                        <s:text name="jpnewsletter.admin.queue"/>
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />">
                        <s:text name="jpnewsletter.admin.subscribersList"/>
                    </a>
                </li>
                <wp:ifauthorized permission="jpnewsletter_config">
                    <li>
                        <a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />">
                            <s:text name="jpnewsletter.admin.config"/>
                        </a>
                    </li>
                </wp:ifauthorized>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main" role="main">
    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <s:text name="jpnewsletter.note.subscribers.intro" />.
    </div>
    <s:form action="search" cssClass="form-horizontal" role="search">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <div class="searchPanel form-group">
            <div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10">
                <p class="search-label">
                    <s:text name="label.search"/>
                </p>

                <div class="form-group">
                    <label class="control-label col-sm-2" for="text">
                        <s:text name="label.email"/>
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textfield name="insertedMailAddress" id="search_mail_add"
                                        cssClass="form-control" placeholder="%{getText('label.email')}"
                                        title="%{getText('label.search.by')} %{getText('jpnewsletter.label.search.mailaddress')}"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-sm-2">
                        <s:text name="label.state"/>
                    </label>
                    <div class="col-sm-10">
                        <div class="input-group">
                            <div class="btn-group" data-toggle="buttons">
                                <label for="jpnewsletter_search_active"
                                       class="btn btn-default <s:if test="%{insertedActive.toString().equalsIgnoreCase('1')}"> active </s:if>">
                                    <wpsf:radio useTabindexAutoIncrement="true" checked="insertedActive == 1"
                                                name="insertedActive" value="1"
                                                id="jpnewsletter_search_active"/>
                                    <s:text name="jpnewsletter.label.search.active"/>
                                </label>
                                <label for="jpnewsletter_search_not_active"
                                       class="btn btn-default <s:if test="%{insertedActive.toString().equalsIgnoreCase('0')}"> active </s:if>">
                                    <wpsf:radio useTabindexAutoIncrement="true" checked="insertedActive == 0"
                                                name="insertedActive" value="0"
                                                id="jpnewsletter_search_not_active"/>
                                    <s:text name="jpnewsletter.label.search.notactive"/>
                                </label>
                                <label for="jpnewsletter_search_all"
                                       class="btn btn-default <s:if test="%{insertedActive==null}"> active </s:if>">
                                    <wpsf:radio useTabindexAutoIncrement="true" checked="insertedActive == null"
                                                name="insertedActive" value="" id="jpnewsletter_search_all"/>
                                    <s:text name="jpnewsletter.label.search.all"/>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-12">
                    <div class="form-group">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="label.search"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </s:form>

    <div class="subsection-light">

        <s:set var="subscribersVar" value="subscribers"/>

        <s:if test="#subscribersVar != null && #subscribersVar.size() > 0">
            <wpsa:subset source="#subscribersVar" count="10" objectName="groupSubscribers" advanced="true" offset="5">
                <s:set var="group" value="#groupSubscribers"/>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover no-mb">
                        <thead>
                            <tr>
                                <th><s:text name="label.email"/></th>
                                <th class="text-center table-w-10">
                                    <s:text name="label.subscribtionDate"/>
                                </th>
                                <th class="text-center table-w-10">
                                    <s:text name="label.state"/>
                                </th>
                                <th class="text-center table-w-10">
                                    <s:text name="label.actions"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator value="#subscribersVar" var="subscriber">
                                <tr>
                                    <td><s:property value="#subscriber.mailAddress"/></td>
                                    <td class="text-center"><s:date name="#subscriber.subscriptionDate" format="dd/MM/yyyy"/></td>
                                    <td class="text-center">
                                        <s:if test="#subscriber.active == 1">
                                            <s:set var="statusIconClassVar" value="%{'icon fa fa-check text-success'}"/>
                                            <s:set var="newsletterUserStatus">true</s:set>
                                            <s:set var="statusTextVar" value="%{getText('note.userStatus.active')}"/>
                                        </s:if>
                                        <s:else>
                                            <s:set var="statusIconClassVar" value="%{'icon fa fa-pause text-warning'}"/>
                                            <s:set var="newsletterUserStatus">false</s:set>
                                            <s:set var="statusTextVar" value="%{getText('note.userStatus.notActive')}"/>
                                        </s:else>
                                        <span class="<s:property value="#statusIconClassVar" />">
                                            <s:property value="#statusTextVar"/>
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <s:url var="removeActionVar" action="trash">
                                            <s:param name="mailAddress" value="#subscriber.mailAddress"/>
                                        </s:url>
                                        <div class="btn-group btn-group-xs">
                                            <a href="<s:property value="#removeActionVar" escapeHtml="false" />"
                                               title="<s:text name="label.remove" />:&#32;<s:property value="#subscriber.mailAddress" />">
                                                <span class="sr-only"><s:text name="label.alt.clear"/></span>
                                                <span class="fa fa-trash-o fa-lg"></span>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                </div>
                <div class="content-view-pf-pagination clearfix">
                    <div class="form-group">
                        <span>
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp"/>
                        </span>
                        <div class="mt-5">
                            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp"/>
                        </div>
                    </div>
                </div>
            </wpsa:subset>
        </s:if>
        <s:else>
            <div class="alert alert-info">
                <span class="pficon pficon-info"></span>
                <s:text name="jpnewsletter.subscribers.empty"/>
            </div>
        </s:else>
    </div>
</div>
