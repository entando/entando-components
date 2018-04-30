<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <s:text name="jpkiebpm.admin.menu.config" />
    </li>
    <li class="page-title-container">
        <s:text name="label.kie.list" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpkiebpm.admin.menu.config" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.kiebpms.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern" id="frag-tab">
                <li>
                    <a href="<s:url action="list" namespace="/do/jpkiebpm/Config" />" role="tab"> 
                        list
                    </a>
                </li>
                <li  class="active">
                    <a href="<s:url action="list" namespace="/do/jpkiebpm/form/override"/>" role="tab"> 
                        <s:text name="label.kie.list" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br/>

<!-- Tab panes -->
<div class="tab-content margin-large-bottom">
    <div class="tab-pane active" id="frag-list">

        <s:form action="list" cssClass="form-horizontal">
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                        <span class="pficon pficon-close"></span>
                    </button>
                    <span class="pficon pficon-error-circle-o"></span>
                    <s:text name="message.title.ActionErrors" />
                    <ul class="margin-base-top">
                        <s:iterator value="actionErrors">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>

            <div class="form-group ">
                <div class="well col-md-offset-3 col-md-6  ">

                    <p class="search-label"><s:text name="label.search.label"/></p>

                    <div class="form-group">
                        <label class="control-label col-sm-2" for="processList">
                            <s:text name="label.process"/>
                        </label>
                        <div class="col-sm-9">
                            <s:select  headerKey="" headerValue="" cssClass="col-sm-12" list="processList" id="processPath" name="processPath"  listKey="%{packageName +'.'+processName + '@' + containerId + '@' + kieSourceId}" listValue="%{processName + ' @ ' + containerId}"  >
                            </s:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="search-code" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.code"/></label>
                        <label class="col-sm-2 control-label"><s:text name="label.code"/></label>
                        <div class="col-sm-9">
                            <wpsf:textfield id="guiFragment_code" name="code" cssClass="form-control" title="%{getText('label.search.by')+' '+getText('label.code')}" placeholder="%{getText('label.code')}" />
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <div class="form-group">
                            <s:submit type="button" cssClass="btn btn-primary pull-right">
                                <s:text name="label.search" />
                            </s:submit>
                        </div>
                    </div>
                </div>
            </div>
        </s:form>
        <div class="col-xs-12 mb-20">
            <a class="btn btn-primary pull-right" href="<s:url action="new" namespace="/do/jpkiebpm/form/override"/>" role="tab">
                <s:text name="label.add" />
            </a>
        </div>
        <div class="col-xs-12">
            <s:form action="search">
                <p class="sr-only">
                    <wpsf:hidden name="code" />
                    <wpsf:hidden name="widgetTypeCode" />
                    <wpsf:hidden name="pluginCode" />
                </p>
                <s:set var="oveddide_list" value="list" />
                <s:if test="#oveddide_list.size > 0">
                    <wpsa:subset source="#oveddide_list" count="10" objectName="groupGuiFragments" advanced="true" offset="5">
                        <s:set var="group" value="#groupGuiFragments" />
                        <div class="col-xs-12 no-padding">
                            <table class="table table-striped table-bordered table-hover no-mb">
                                <tr>
                                    <th class="table-w-5"><s:text name="label.id" /></th>
                                    <th class="table-w-10"><s:text name="label.date" /></th>
                                    <th><s:text name="label.field" /></th>
                                    <th><s:text name="label.containerId" /></th>
                                    <th><s:text name="label.processId" /></th>
                                    <th class="text-center table-w-5"><s:text name="label.actions" /></th>
                                </tr>
                                <s:iterator var="codeVar">
                                    <s:set var="currentItemVar" value="%{getKieFormOverride(#codeVar)}" />

                                    <s:url action="edit" var="editActionVar"><s:param name="id" value="#codeVar"/></s:url>
                                    <s:url action="trash" var="trashActionVar"><s:param name="id" value="#codeVar"/></s:url>
                                        <tr>
                                            <td>
                                                <a href="<s:property value="#editActionVar"/>">
                                                <s:property value="#codeVar"/>
                                            </a>
                                        </td>
                                        <td>
                                            <s:date name="#currentItemVar.date" format="dd/MM/yyyy" />
                                        </td>
                                        <td class="text-overflow ">
                                            <s:property value="#currentItemVar.field"/>
                                        </td>
                                        <td class="text-overflow ">
                                            <s:property value="#currentItemVar.containerId"/>
                                        </td>
                                        <td class="text-overflow ">
                                            <s:property value="#currentItemVar.processId"/>
                                        </td>
                                        <td class=" text-center table-view-pf-actions">
                                            <div class="dropdown dropdown-kebab-pf">
                                                <button class="btn btn-menu-right dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                    <span class="fa fa-ellipsis-v"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li>
                                                        <a href="<s:property value="#trashActionVar" escapeHtml="false" />"
                                                           title="<s:text name="label.remove" />: <s:property value="#codeVar" />">
                                                            <span class="sr-only"><s:text name="label.alt.clear" /></span>
                                                            <s:text name="label.delete" />
                                                        </a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </table>
                        </div>
                        <div class="content-view-pf-pagination clearfix">
                            <div class="form-group">
                                <span><s:include
                                        value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                                <div class="mt-5">
                                    <s:include
                                        value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                                </div>
                            </div>
                        </div>
                    </wpsa:subset>
                </s:if>
                <s:else>
                    <div class="alert alert-warning">
                        <s:text name="jpkiebpm.list.empty" />
                    </div>
                </s:else>
            </s:form>
        </div>
    </div>
    <div class="tab-pane" id="frag-settings">
    </div>
</div>
