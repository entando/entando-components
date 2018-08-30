<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>

<script src="<wp:resourceURL />plugins/jpkiebpm/administration/js/jbpm-component-config.js"></script>-->



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

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmFormViewer" class="form-horizontal">
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
                <s:if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                            <span class="pficon pficon-close"></span>
                        </button>
                        <span class="pficon pficon-error-circle-o"></span>
                        <strong><s:text name="message.title.FieldErrors"/></strong>
                        <ul>
                            <s:iterator value="fieldErrors">
                                <s:iterator value="value">
                                    <li><s:property/></li>
                                </s:iterator>
                            </s:iterator>
                        </ul>
                    </div>
                </s:if>

                <s:set var="isknowledgeSourcePathSetted" value="%{knowledgeSourcePath != null && knowledgeSourcePath != ''}"/>
                <s:set var="isProcessPathSetted" value="%{processPath != null && processPath != ''}"/>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-8 col-xs-8">

                            <div class="form-group">
                                <label for="Knowledge Source"><s:text name="Knowledge Source"/></label>
                                <div class="input-group">

                                    <s:select list="knowledgeSource" id="knowledgeSourcePath" name="knowledgeSourcePath"
                                              listKey="value.id"
                                              listValue="value.name" class="form-control">
                                    </s:select>
                                    <span class="input-group-btn">
                                        <s:if test="#isknowledgeSourcePathSetted">
                                            <wpsf:submit action="changeKnowledgeSourceForm" value="Change Knowledge Source"
                                                         cssClass="btn btn-warning"/>
                                        </s:if>
                                        <s:else>

                                            <wpsf:submit action="chooseKnowledgeSourceForm" value="Choose Knowledge Source"
                                                         cssClass="btn btn-success"/>
                                        </s:else>
                                    </span>
                                </div>
                            </div>
                        </div>

                    </div>

                    <s:if test="#isknowledgeSourcePathSetted">
                        <div class="row">
                            <div class="col-lg-8 col-md-8 col-xs-8">

                                <div class="form-group">
                                    <label class="control-label col-xs-2" for="processPath">
                                        <s:text name="Process"/>
                                    </label>
                                    <div class="col-xs-5">
                                        <s:if test="!#isProcessPathSetted">
                                            <s:select list="process" id="processPath" name="processPath"  listKey="%{processId + '@' + containerId + '@' + kieSourceId}" listValue="%{processName + ' @ ' + containerId}">
                                            </s:select>
                                        </s:if>
                                        <s:else>
                                            <s:select disabled="true" list="process" id="processPath" name="processPath"  listKey="%{processId + '@' + containerId + '@' + kieSourceId}" listValue="%{processName + ' @ ' + containerId}">
                                            </s:select>
                                            <s:hidden name="processPath" />

                                        </s:else>
                                    </div>
                                    <s:if test="#isProcessPathSetted">
                                        <div class="col-xs-2">
                                            <wpsf:submit action="changeForm" value="%{getText('label.changeForm')}" cssClass="btn btn-warning pull-right" />
                                        </div>
                                    </s:if>
                                    <s:else>
                                        <div class="col-xs-2">
                                            <wpsf:submit action="chooseForm" value="%{getText('label.chooseForm')}" cssClass="btn btn-success pull-right" />
                                        </div>
                                    </s:else>
                                </div>

                                <s:if test="#isProcessPathSetted">
                                    <legend>
                                        <s:text name="label.override.found" />
                                    </legend>
                                    <s:set var="ovMap" value="formOverridesMap" />
                                    <s:iterator var="item" value="#ovMap">
                                        <s:set var="checked" value="ovrd.contains(#item.value.id)"/>
                                        <div class="form-group">
                                            <label class="control-label col-xs-2" for="processPath">
                                                <s:property value="#item.key"/>

                                            </label>
                                            <div class="col-xs-10">
                                                <input <s:if test="#checked">checked="checked"</s:if> type="checkbox" id="bootstrap-switch-state" class="bootstrap-switch" name="ovrd" value="<s:property value="#item.value.id" />">
                                            </div>
                                        </div>

                                        <div class="well">
                                            <s:iterator value="#item.value.overrides.list" var="override" >
                                                <s:if test="#override.type.equals('defaultValueOverride')">
                                                    <b><s:text name="defaultValueOverride" /></b> &nbsp; &nbsp;
                                                    <s:property value="#override.defaultValue"/>
                                                </s:if>
                                                <br>
                                                <s:elseif test="#override.type.equals('placeHolderOverride')">
                                                    <b><s:text name="placeHolderOverride" /></b>  &nbsp; &nbsp;
                                                    <s:property value="#override.placeHolder"/>
                                                </s:elseif>
                                                <s:else>
                                                    <code>
                                                        TODO: <s:property value="#override.type"/>
                                                    </code>
                                                </s:else>
                                            </s:iterator>
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
    <pre style="display:none;">
    <!--Saved Configurations-->
    <br />
    <br />
        <!--kieContainerListJson value-->
    kieContainerListJson value
    <br />
        <s:if test="kieContainerListJson != null">
            <s:property value="kieContainerListJson" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
        <!--/kieContainerListJson value-->
    <br />
    <br />
        <!--knowledgeSourceJson value-->
    knowledgeSourceJson value
    <br />
        <s:if test="knowledgeSourceJson != null">
            <s:property value="knowledgeSourceJson" escapeJavaScript="false" escapeHtml="false"/>
        </s:if>
        <!--/knowledgeSourceJson value-->
    </pre>
</div>
