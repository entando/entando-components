<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="jpcontentscheduler.integrations" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.components" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.admin.menu" />
    </li>
    <li class="page-title-container">
        <s:text name="jpcontentscheduler.admin.contentTypes" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpcontentscheduler.admin.menu" />
                <span class="pull-right">
                    <s:text name="jpcontentscheduler.section.help" var="helpVar" />
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
                        <s:text name="jpcontentscheduler.admin.general" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewEmail"/>">
                        <s:text name="jpcontentscheduler.admin.mail" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewUsers"/>">
                        <s:text name="jpcontentscheduler.admin.users" />
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="viewContentTypes"/>">
                        <s:text name="jpcontentscheduler.admin.contentTypes" />
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
    <div class="row">
        <div class="col-xs-12">
            <a class="btn btn-primary margin-base-bottom pull-right" href="<s:url action="addContentTypes" />">
                <s:text name="label.add" />
            </a>
        </div>
    </div>
    <div class="row mt-5">
        <div class="col-xs-12">
            <s:form id="configurationForm" name="configurationForm" method="post" action="saveContentTypeItem" cssClass="form-horizontal">
                <s:hidden name="_csrf" value="%{csrfToken}"/>
                <s:set var="#contentTypeElements" value="%{getTypes()}" />
                <!--                 <contentType type="NOL" startAttr="Data_inizio" endAttr="Data_fine" idContentReplace="Id_contenuto_sost" modelIdContentReplace="Model_id" suspend="true"> -->
                <table class="table table-striped table-bordered table-hover mb-20">
                    <thead>
                        <tr>
                            <th>
                                <s:text name="jpcontentscheduler.label.contentTypes.contentType" />
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="jpcontentscheduler.label.contentTypes.startDate" />
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="jpcontentscheduler.label.contentTypes.endDate" />
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="jpcontentscheduler.label.contentTypes.contentId" />
                            </th>
                            <th class="text-center table-w-10">
                                <s:text name="jpcontentscheduler.label.contentTypes.modelId" />
                            </th>
                            <th class="text-center table-w-5">
                                <s:text name="jpcontentscheduler.label.contentTypes.suspend" />
                            </th>
                            <th class="text-center table-w-5">
                                <s:text name="jpcontentscheduler.label.actions" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="#contentTypeElements" var="element">
                            <tr>
                                <td>
                                    <s:property value="#element.contentType" />
                                </td>
                                <td>
                                    <s:property value="#element.startDateAttr" />
                                </td>
                                <td>
                                    <s:property value="#element.endDateAttro" />
                                </td>
                                <td>
                                    <s:property value="#element.idContentReplace" />
                                </td>
                                <td>
                                    <s:property value="#element.ModelIdContentReplace" />
                                </td>
                                <td class="text-center">
                                    <s:if test="%{#element.suspend.equalsIgnoreCase('true')}">
                                        <s:set var="iconName" value="%{'check'}"/>
                                        <s:set var="textColor" value="%{'success'}"/>
                                        <s:set var="isSuspended" value="%{getText('label.yes')}"/>
                                    </s:if>
                                    <s:else>
                                        <s:set var="iconName" value="%{'pause'}"/>
                                        <s:set var="textColor" value="%{'warning'}"/>
                                        <s:set var="isSuspended" value="%{getText('label.no')}"/>
                                    </s:else>
                                    <span class="icon fa fa-${iconName} text-${textColor}" title="${isSuspended}"></span>
                                    <span class="sr-only"><s:property value="#isSuspended"/></span>
                                </td>
                                <td class="text-center">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <p class="sr-only">
                                            <s:text name="label.actions" />
                                        </p>
                                        <span class="btn btn-menu-right dropdown-toggle" type="button"
                                              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </span>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li>
                                                <a href="<s:url action="editContentTypes"><s:param name="contentType" value="%{#element.contentType}"/></s:url>">
                                                    <s:text name="label.edit" />
                                                </a>
                                            </li>
                                            <li>
                                                <a href="<s:url action="trashContentType"><s:param name="contentType" value="%{#element.contentType}"/></s:url>">
                                                    <s:text name="label.remove" />
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
                <%-- TODO: attivare paginazione
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
                --%>
                <div class="form-group">
                    <div class="col-xs-12">
                        <div class="pull-right">
                            <wpsf:submit name="save" type="button" cssClass="btn btn-primary" >
                                <s:text name="%{getText('label.save')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </s:form>
        </div>
    </div>
</div>