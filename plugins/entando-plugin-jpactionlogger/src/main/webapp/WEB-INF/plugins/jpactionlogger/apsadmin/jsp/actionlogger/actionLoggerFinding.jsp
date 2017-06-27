<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<% pageContext.setAttribute("newLineChar", "\n");%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations" /></li>
    <li><s:text name="breadcrumb.integrations.components" /></li>
    <li class="page-title-container">
        <s:text name="title.actionLogger.management" />
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.actionLogger.management" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.actionLogger.management.help" />"
           data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
    <s:form action="list" cssClass="form-horizontal">
        <div class="searchPanel form-group">
            <div class="well col-md-offset-3 col-md-6">
                <p class="search-label">
                    <s:text name="label.search.label" />
                </p>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><s:text
                            name="%{getText('actiondate')}" /></label>
                    <div class="col-sm-9">
                        <wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control  datepicker" id="jpactionlogger_dateStart_cal" name="start" placeholder="dd/MM/yyyy" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="jpactionlogger_dateEnd_cal"
                           class="control-label col-sm-2"><s:text name="end" /></label>
                    <div class="col-sm-9">
                        <wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control  datepicker" id="jpactionlogger_dateEnd_cal" name="end" placeholder="dd/MM/yyyy" />
                    </div>
                </div>
                <br> <br>
                <div class="panel-group advanced-search" id="accordion-markup">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <p class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion-markup"
                                   href="#collapseOne"><s:text name="label.search.advanced" /></a>
                            </p>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="form-group">
                                    <label for="jpactionlogger_username"
                                           class="control-label col-sm-2"><s:text name="username" />
                                    </label>
                                    <div class="col-sm-9">
                                        <wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control " id="jpactionlogger_username"  name="username" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="jpactionlogger_namespace"
                                           class="control-label col-sm-2">
                                        <s:text name="namespace" />
                                    </label>
                                    <div class="col-sm-9">
                                        <wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control " id="jpactionlogger_namespace" name="namespace" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="jpactionlogger_actionName"
                                           class="control-label col-sm-2">
                                        <s:text name="actionName" />
                                    </label>
                                    <div class="col-sm-9">
                                        <wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control "  id="jpactionlogger_actionName" name="actionName" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="jpactionlogger_params"
                                           class="control-label col-sm-2">
                                        <s:text name="params" />
                                    </label>
                                    <div class="col-sm-9">
                                        <wpsf:textfield useTabindexAutoIncrement="true" cssClass="form-control " id="jpactionlogger_params" name="params" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-12">
                    <wpsf:submit useTabindexAutoIncrement="true"
                                 value="%{getText('label.search')}" type="button"
                                 cssClass="btn btn-primary pull-right">
                        <s:text name="label.search" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
    <s:form action="search">
        <p class="sr-only">
            <s:hidden name="username" />
            <s:hidden name="namespace" />
            <s:hidden name="actionName" />
            <s:hidden name="params" />
            <s:hidden name="start" />
            <s:hidden name="end" />
        </p>
        <wpsa:subset source="actionRecords" count="10" objectName="groupActions" advanced="true" offset="5">
            <s:set var="group" value="#groupActions" />
            <div class="col-xs-12 no-padding">
                <table class="table table-striped table-bordered table-hover no-mb">
                    <caption class="sr-only">
                        <s:text name="actionLogger.list" />
                    </caption>
                    <thead>
                        <tr>
                            <th class="text-center table-w-10">
                                <s:text name="username" />
                            </th>
                            <th class="text-center table-w-15">
                                <s:text name="actiondate" />
                            </th>
                            <th class="text-center table-w-20">
                                <s:text name="namespace" />/
                                <s:text name="actionName" />
                            </th>
                            <th class="text-center table-w-30">
                                <s:text name="params" />
                            </th>
                            <th class="text-center table-w-5">
                                <s:text name="label.actions" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator var="id">
                            <s:set var="logRecord" value="%{getActionRecord(#id)}" />
                            <tr>
                                <td class="text-center">
                                    <s:property value="#logRecord.username" />
                                </td>
                                <td class="text-center">
                                    <s:set var="logRecord" value="%{getActionRecord(#id)}" /> <span>
                                        <s:date name="#logRecord.actionDate" format="dd/MM/yyyy" />
                                    </span>
                                    <span>
                                        <s:date name="#logRecord.actionDate" format="HH:mm:ss" />
                                    </span>
                                </td>
                                <td class="text-center">
                                    <s:property value="#logRecord.actionName" />
                                </td>
                                <td>
                                    <s:set var="recordParametersStringVar"
                                           value="#logRecord.parameters" scope="page" /> <c:choose>
                                        <c:when test="${empty recordParametersStringVar}">
                                            <span class="sr-only">No parameters.</span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="recordParametersArrayVar"
                                                   value="${fn:split(recordParametersStringVar, newLineChar )}" />
                                            <c:forEach items="${recordParametersArrayVar}"
                                                       var="currentParameter" begin="0" end="2" varStatus="status">
                                                <c:out value="${currentParameter}" />
                                                <c:if test="${not status.last}">
                                                    <br />
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${fn:length(recordParametersArrayVar) > 3}">
                                                <div id="parameter-item-<c:out value="${logRecord.id}"/>" class="collapse">
                                                    <c:forEach items="${recordParametersArrayVar}"
                                                               var="currentParameter" begin="3" varStatus="status">
                                                        <c:out value="${currentParameter}" />
                                                        <c:if test="${not status.last}">
                                                            <br />
                                                        </c:if>
                                                    </c:forEach>
                                                </div>
                                                <a class="cursor-pointer" data-toggle="collapse" data-target="#parameter-item-<c:out value='${logRecord.id}'/>" title="All parameters">
                                                    <i class="fa fa-plus" aria-hidden="true"></i>
                                                </a>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="monospace text-center">
                                    <div class="btn-group btn-group-xs">
                                        <a href="<s:url action="delete"> ><s:param name="id" value="#logRecord.id"></s:param></s:url>" title="<s:text name="label.remove" />:
                                           <s:date name="#logRecord.actionDate" format="dd/MM/yyyy HH:mm:ss" />">
                                            <span class="fa fa-trash-o fa-lg">
                                            </span>
                                            &#32;
                                            <span class="sr-only"><s:text name="label.alt.clear" /></span>
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
                    <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                    <div class="mt-5">
                        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                    </div>
                </div>
            </div>
        </wpsa:subset>
    </s:form>
</div>
