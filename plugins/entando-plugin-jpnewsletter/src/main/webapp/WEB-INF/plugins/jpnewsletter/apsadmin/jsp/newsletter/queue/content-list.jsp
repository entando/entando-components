<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpnewsletter.integrations"/></li>
    <li><s:text name="jpnewsletter.components"/></li>
    <li><s:text name="jpnewsletter.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="jpnewsletter.title.newsletterQueue"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpnewsletter.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="jpnewsletter.section.help" data-placement="left" data-original-title="">
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
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />">
                        <s:text name="jpnewsletter.admin.queue"/>
                    </a>
                </li>
                <li>
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

<div class="alert alert-info">
    <span class="pficon pficon-info"></span>
    <s:if test="newsletterConfig.active">
        <p>
            <s:text name="jpnewsletter.label.newsletterQueue.intro" />
        </p>
        <p>
            <s:text name="jpnewsletter.label.newsletterQueue.intro.bis" />
        </p>
        <p>
            <s:text name="jpnewsletter.label.newsletterQueue.startInfo">
                <s:param>
                    <em class="important">
                        <s:date name="newsletterConfig.nextTaskTime" format="dd/MM/yyyy" />
                    </em>
                </s:param>
                <s:param>
                    <em class="important">
                        <s:date name="newsletterConfig.nextTaskTime" format="HH:mm" />
                    </em>
                </s:param>
                <s:param value="hoursDelay" />
                <s:param value="minutesDelay" />
            </s:text>
        </p>
    </s:if>
    <s:else>
        <p>
            <s:text name="jpnewsletter.label.newsletterQueue.notActive" />
        </p>
    </s:else>
</div>

<div id="main" role="main">

    <s:set var="contentIdsVar" value="contentIds"/>

    <s:if test="%{#contentIdsVar.size() > 0}">

        <s:form action="search" cssClass="form-horizontal" role="search">

            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp"/>

            <wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
                <s:set var="group" value="#groupContent"/>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover no-mb" id="contentListTable">
                        <thead>
                            <tr>
                                <th><s:text name="label.description"/></th>
                                <th class="table-w-10"><s:text name="label.code"/></th>
                                <th class="table-w-10 text-center">
                                    <s:text name="label.actions" />
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                        <s:iterator var="contentId">
                            <s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
                            <tr>
                                <td><s:property value="#content.descr"/></td>
                                <td><s:property value="#content.id"/></td>
                                <td class="text-center">
                                    <div class="btn-group btn-group-xs">
                                        <a title="<s:text name="label.remove" />"
                                           href="<s:url action="removeFromQueue" ><s:param name="contentId" value="#content.id" /></s:url>">
                                            <span class="fa fa-trash-o fa-lg"></span>
                                            <span class="sr-only"><s:text name="label.remove"/></span>
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

            <s:if test="newsletterConfig.active">
                <div class="col-xs-12 mt-20 no-padding">
                    <a class="btn btn-primary pull-right"
                       href="<s:url action="send" />">
                        <s:text name="jpnewsletter.label.sendNow"/>
                    </a>
                </div>
            </s:if>
        </s:form>
    </s:if>
    <s:else>
        <div class="alert alert-info">
            <span class="pficon pficon-info"></span>
            <p><s:text name="jpnewsletter.label.newsletterQueue.empty"/></p>
        </div>
    </s:else>
</div>
