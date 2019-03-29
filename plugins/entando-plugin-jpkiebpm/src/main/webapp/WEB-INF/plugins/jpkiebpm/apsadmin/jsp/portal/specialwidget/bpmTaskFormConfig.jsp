<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .btn-success , .btn-warning, .btn-default {
        min-width: 170px;
    }
    legend.overrides {
        margin-left: -20px;
    }
</style>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />:
           <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.configPage"/>
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="title.configPage"/>
</h1>
<div class="text-right">
    <div class="form-group-separator">
    </div>
</div>
<br>

<div class="mb-20" >

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmTaskFormViewer" class="form-horizontal">
        <p class="noscreen">
            <wpsf:hidden name="pageCode"/>
            <wpsf:hidden name="frame"/>
            <wpsf:hidden name="widgetTypeCode"/>
        </p>

        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp"/>
            </div>
            <div class="panel-body">
                <p class="h5 margin-small-vertical">
                    <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}"/>
                </p>
                <s:include value="/WEB-INF/plugins/jpkiebpm/apsadmin/jsp/common/errors.jsp"/>

                <s:set var="isknowledgeSourcePathSetted" value="%{knowledgeSourcePath != null && knowledgeSourcePath != ''}"/>
                <s:set var="isProcessPathSetted" value="%{processPath != null && processPath != ''}"/>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-8 col-xs-8">
                            <s:include value="/WEB-INF/plugins/jpkiebpm/apsadmin/jsp/common/knowledge-source-select.jsp"/>
                        </div>
                    </div>

                    <s:if test="#isknowledgeSourcePathSetted">
                        <div class="row">
                            <div class="col-lg-8 col-md-8 col-xs-8">
                                <div class="form-group">
                                    <label for="processPath">
                                        <s:text name="Process"/>
                                    </label>
                                    <div class="input-group">
                                        <s:select 
                                            disabled="#isProcessPathSetted"
                                            list="process"
                                            id="processPath"
                                            name="processPath"
                                            listKey="%{processId + '@' + containerId + '@' + kieSourceId}"
                                            listValue="%{processName + ' @ ' + containerId}"
                                            class="form-control">
                                        </s:select>
                                        <s:if test="#isProcessPathSetted">
                                            <s:hidden name="processPath" />
                                        </s:if>
                                        <span class="input-group-btn">
                                            <s:if test="#isProcessPathSetted">
                                                <wpsf:submit
                                                    action="changeForm"
                                                    value="%{getText('label.changeForm')}"
                                                    cssClass="btn btn-warning"
                                                    />
                                            </s:if>
                                            <s:else>
                                                <wpsf:submit 
                                                    action="chooseForm" 
                                                    value="%{getText('label.chooseForm')}"
                                                    cssClass="btn btn-success" 
                                                    />
                                            </s:else>
                                        </span>
                                    </div>
                                </div>

                                <s:if test="%{#isProcessPathSetted && fields != null && !fields.isEmpty()}">
                                    <legend class="overrides">
                                        <s:text name="label.override" />
                                    </legend>
                                    
                                    <wpsf:submit action="addFormOverride" type="button" cssClass="btn btn-success pull-right">
                                        <s:text name="label.add" />
                                    </wpsf:submit>
                                    
                                    <div class="clearfix"></div>
                                    <br/>
                                    
                                    <s:iterator value="overrides" var="override" status="iteration">
                                        
                                        <s:hidden name="overrides[%{#iteration.index}].id" />
                                        
                                        <div class="well">

                                            <wpsa:actionParam action="deleteFormOverride" var="deleteFormOverrideActionName" >
                                                <wpsa:actionSubParam name="overrideToDeleteIndex" value="%{#iteration.index}" />
                                            </wpsa:actionParam>
                                            <wpsf:submit action="%{#deleteFormOverrideActionName}" type="button" cssClass="btn btn-danger pull-right">
                                                <s:text name="label.remove" />
                                            </wpsf:submit>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label" for="field">
                                                    <s:text name="formModel.field" />
                                                    <i class="fa fa-asterisk required-icon"></i>
                                                </label>
                                                <div class="col-sm-3">
                                                    <s:select list="fields" id="formModel_field"
                                                              name="overrides[%{#iteration.index}].field"
                                                              listKey="%{name}" listValue="%{name}" ></s:select>
                                                    <s:set var="fieldErrorsVar" value="%{fieldErrors['overrides[' + #iteration.index + '].field']}"/>
                                                    <s:set var="fieldHasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()"/>
                                                    <s:if test="#fieldHasFieldErrorVar">
                                                        <div class="text-danger">
                                                            <s:iterator value="%{#fieldErrorsVar}">
                                                                <s:property escapeHtml="false"/>
                                                                &#32;
                                                            </s:iterator>
                                                        </div>
                                                    </s:if>
                                                </div>
                                                <div class="col-sm-4">
                                                    <wpsf:checkbox name="%{'overrides[' + #iteration.index + '].active'}" id="%{'active_' + #iteration.index }" cssClass="bootstrap-switch" />
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label" for="placeHolderOverride">
                                                    <s:text name="placeHolderOverride" />
                                                </label>
                                                <s:textfield name="overrides[%{#iteration.index}].placeHolderValue" />
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label" for="defaultValueOverride">
                                                    <s:text name="defaultValueOverride" />
                                                </label>
                                                <s:textfield name="overrides[%{#iteration.index}].defaultValue" />
                                            </div>	            
                                        </div>
                                    </s:iterator>
                                </s:if>
                            </div>

                        </div>

                    </s:if>

                </div>
            </div>
        </div>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit disabled="!#isProcessPathSetted" type="button" cssClass="btn btn-primary pull-right"
                                 action="save">
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
