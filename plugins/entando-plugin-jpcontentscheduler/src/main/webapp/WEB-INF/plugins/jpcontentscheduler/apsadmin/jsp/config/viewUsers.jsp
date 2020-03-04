<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpcontentscheduler.integrations"/></li>
    <li><s:text name="jpcontentscheduler.components"/></li>
    <li><s:text name="jpcontentscheduler.admin.menu"/></li>
    <li class="page-title-container">
        <s:text name="jpcontentscheduler.admin.users"/>
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
                    <a href="<s:url action="viewEmail"/>">
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

<div id="main">

    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>
    <div class="row">
        <div class="col-xs-12">
            <a class="btn btn-primary margin-base-bottom pull-right" href="<s:url action="addUser" />"><s:text name="label.add" /></a>
        </div>
    </div>

    <br/>
    <div class="row mt-5">
        <div class="col-xs-12">
            <p>
                <s:text name="legend.contentThreadconfigSettings.users.help" />
            </p>
            <s:form id="configurationForm" name="configurationForm" method="post" action="saveUsersItem" cssClass="form-horizontal">
                <s:set var="#users" value="%{getUsersContentType()}"/>

                <table class="table table-striped table-bordered table-hover mb-20">
                    <thead>
                        <tr>
                            <th><s:text name="jpcontentscheduler.label.username"/></th>
                            <th><s:text name="jpcontentscheduler.label.contentTypes"/></th>
                            <th class="text-center table-w-5"><s:text name="jpcontentscheduler.label.actions"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="#users" var="user">
                            <tr>
                                <td><s:property value="#user.key"/></td>
                                <td>
                                    <s:iterator value="#user.value" var="contentType" status="status">
                                        <s:if test="%{!#status.first}">,</s:if>
                                        <s:property value="#contentType"/>
                                    </s:iterator>
                                </td>
                                <td class="text-center">
                                    <div class="dropdown dropdown-kebab-pf">
                                        <p class="sr-only"><s:text name="label.actions"/></p>
                                        <span class="btn btn-menu-right dropdown-toggle" type="button"
                                              data-toggle="dropdown" aria-haspopup="true"
                                              aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </span>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li>
                                                <a href="<s:url action="editUser"><s:param name="user" value="#user.key" /></s:url>">
                                                    <s:text name="label.edit"/>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="<s:url action="trashUser"><s:param name="user" value="#user.key" /></s:url>">
                                                    <s:text name="label.remove"/>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>

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