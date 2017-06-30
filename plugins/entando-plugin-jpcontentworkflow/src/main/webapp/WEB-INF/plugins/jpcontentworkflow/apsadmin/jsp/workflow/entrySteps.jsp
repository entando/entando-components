<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" />">
            <s:text name="title.workflowManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.workflowManagement.editSteps" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.workflowManagement.editSteps" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="jpcontentworkflow.title.general.help" />" data-placement="left"
               data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div class="mb-20">
    <s:form action="saveSteps" cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert">
                    <span class="icon fa fa-times"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.FieldErrors" />
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li>
                                <s:property escapeHtml="false" />
                            </li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert">
                    <span class="icon fa fa-times"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.FieldErrors" />
                <ul>
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <p class="text-info">
            <s:text name="note.workingOn" />
            :&#32;
            <s:text name="label.contentType" />
            &#32; <em><s:property value="contentType.descr" /></em>
        </p>
        <p class="sr-only hide">
            <wpsf:hidden name="typeCode" />
            <wpsf:hidden name="stepCodes" />
        </p>
        <s:if test="%{steps == null || steps.size() == 0}">
            <p class="sr-only">
                <s:text name="note.stepList.empty" />
            </p>
        </s:if>
        <%-- <s:else> --%>
        <table class="table table-striped table-bordered table-hover no-mb">
            <caption class="sr-only">
                <s:text name="note.stepList.caption" />
            </caption>
            <thead>
                <tr>
                    <th><s:text name="label.code" /></th>
                    <th><s:text name="label.descr" /></th>
                    <th><s:text name="label.role" /></th>
                    <th class="text-center table-w-5">
                        <s:text name="label.actions" />
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>DRAFT</td>
                    <td><s:text name="name.contentStatus.DRAFT" /></td>
                    <td><span class="sr-only">none</span>&ndash;</td>
                    <td><span class="sr-only">none</span></td>
                </tr>
                <s:if test="%{steps != null && steps.size() > 0}">
                    <s:iterator value="steps" var="step" status="rowstatus">
                        <tr>
                            <td>
                                <s:property value="#step.code" />
                            </td>
                            <td>
                                <wpsf:textfield name="%{code + '_SEP_descr'}" value="%{#step.descr}" cssClass="form-control input-sm display-block" />
                            </td>
                            <td>
                                <wpsf:select name="%{code + '_SEP_role'}" headerKey="" headerValue=" - " list="roles" listKey="name" listValue="description" value="%{#step.role}"
                                             cssClass="form-control input-sm display-block" />
                            </td>
                            <td class="table-view-pf-actions text-center">
                                <div class="dropdown dropdown-kebab-pf">
                                    <div class="btn-group btn-group-xs">
                                        <button class="btn btn-menu-right dropdown-toggle"
                                                type="button" data-toggle="dropdown" aria-haspopup="true"
                                                aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li>
                                                <wpsa:actionParam action="moveStep" var="actionName">
                                                    <wpsa:actionSubParam name="movement" value="UP" />
                                                    <wpsa:actionSubParam name="elementIndex" value="%{#rowstatus.index}" />
                                                </wpsa:actionParam>
                                                <wpsf:submit action="%{#actionName}"  cssClass="btn btn-default %{(#rowstatus.first) ? ' disabled ' : '' }" type="button" title="%{getText('label.moveUp')+': '+#step.code}">
                                                    <span><s:text name="label.moveUp" /></span>
                                                    <span class="sr-only">
                                                        <s:property value="%{getText('label.moveUp')+': '+#step.code}" />
                                                    </span>
                                                </wpsf:submit>
                                            </li>

                                            <li>
                                                <wpsa:actionParam action="moveStep" var="actionName">
                                                    <wpsa:actionSubParam name="movement" value="DOWN" />
                                                    <wpsa:actionSubParam name="elementIndex"
                                                                         value="%{#rowstatus.index}" />
                                                </wpsa:actionParam> 
                                                <wpsf:submit action="%{#actionName}" cssClass="btn btn-default  %{(#rowstatus.last) ? ' disabled ' : '' }" type="button" title="%{getText('label.moveDown')+': '+#step.code}">
                                                    <span>
                                                        <s:text name="label.moveDown" />
                                                    </span>
                                                    <span class="sr-only">
                                                        <s:property value="%{getText('label.moveDown')+': '+#step.code}" />
                                                    </span>
                                                </wpsf:submit>
                                            </li>
                                            <li>
                                                <wpsa:actionParam action="removeStep" var="actionName">
                                                    <wpsa:actionSubParam name="stepCode" value="%{#step.code}" />
                                                </wpsa:actionParam>
                                                <wpsf:submit action="%{#actionName}"  cssClass="btn btn-warning" type="button"  value="%{getText('label.remove')}" title="%{getText('label.remove')+': '+#step.code}">
                                                    <span><s:text name="label.remove" /></span>
                                                    <span class="sr-only">
                                                        <s:property  value="%{getText('label.remove')+': '+#step.code}" /></span>
                                                    </wpsf:submit>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </s:iterator>
                </s:if>
                <tr>
                    <td>READY</td>
                    <td><s:text name="name.contentStatus.READY" /></td>
                    <td><span class="sr-only">none</span>&ndash;</td>
                    <td><span class="sr-only">none</span></td>
                </tr>
            </tbody>
        </table>
        <fieldset class="mt-20">
            <legend>
                <s:text name="title.newStep" />
            </legend>
            <div class="form-group">

                <label class="col-sm-2 control-label"
                       for="jpcontentworkflow_stepCode">
                    <s:text name="label.code" /></label>
                <div class="col-sm-10">
                    <wpsf:textfield maxlength="12" name="stepCode" id="jpcontentworkflow_stepCode" cssClass="form-control" />
                </div>

            </div>
            <div class="form-group">

                <label class="col-sm-2 control-label"
                       for="jpcontentworkflow_stepDescr">
                    <s:text  name="label.descr" /></label>
                <div class="col-sm-10">
                    <wpsf:textfield name="stepDescr" id="jpcontentworkflow_stepDescr"  cssClass="form-control" />
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"
                       for="jpcontentworkflow_stepRole"><s:text name="label.role" /></label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <wpsf:select id="jpcontentworkflow_stepRole" name="stepRole"
                                     headerKey="" headerValue="" list="roles" listKey="name"
                                     listValue="description" cssClass="form-control" />
                        <span class="input-group-btn"> 
                            <wpsa:actionParam action="addStep" var="actionName" /> 
                            <wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-primary">
                                <s:text name="label.addStep"></s:text>
                            </wpsf:submit>
                        </span>
                    </div>
                </div>
                &#32;
            </div>
        </fieldset>
        <div class="col-xs-12 mb-20">
            <div class="form-group">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
