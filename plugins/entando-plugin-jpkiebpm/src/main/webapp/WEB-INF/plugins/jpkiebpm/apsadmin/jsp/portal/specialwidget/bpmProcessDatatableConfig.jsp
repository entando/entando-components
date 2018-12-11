<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>


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

<div class="mb-20">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode"/>

    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>

    <s:form action="save" namespace="/do/bpm/Page/SpecialWidget/BpmProcessDatatableViewer" class="form-horizontal">
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
                <s:set var="isGroupsSetted" value="%{groups != null && groups != ''}"/>

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

                    <s:if test="#isProcessPathSetted">
                        <div class="form-group">
                            <label class="control-label col-xs-2" for="Bpm Groups">
                                <s:text name="Bpm Groups"/>
                            </label>
                            <div class="col-xs-5">
                                <s:if test="!#isGroupsSetted">
                                    <s:checkboxlist label="Bpm Groups" list="listBpmGroups" value="selectedGroups" name="groups"/>
                                </s:if>
                                <s:else>
                                    <s:checkboxlist label="Bpm Groups" list="listBpmGroups" value="selectedGroups" name="groups"/>

                                </s:else>
                                <s:hidden name="groups"/>
                            </div>
                        </div>
                        <hr/>
                        <div class="table-responsive overflow-visible">

                            <table id="sort" class="grid table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="text-center table-w-5"><s:text name="label.position"/></th>
                                    <th class="table-w-20"><s:text name="label.colunmsName"/></th>
                                    <th class="text-center table-w-5"><s:text name="label.visible"/></th>
                                    <th class="text-center table-w-20"><s:text name="label.override"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <s:iterator var="i" status="status" value="fieldsDatatable">
                                    <tr>

                                        <td class="index text-center">${i.position}
                                            <input type="hidden" name="position_${i.name}" value="${i.position}"/>
                                        </td>
                                        <td class="field text-center"><s:property value="name"/>
                                            <input type="hidden" name="field_${name}" value="${i.field}"/>
                                        </td>
                                        <td class="text-center">
                                            <input type="checkbox" name="visible_${i.name}"
                                                   <c:if test="${i.visible}">checked</c:if> >
                                        </td>
                                        <td class="text-center">
                                            <input type="text" name="override_${name}" value="${i.override}"/>
                                        </td>
                                    </tr>
                                </s:iterator>

                                </tbody>
                            </table>

                            <style>
                                .ui-sortable-helper {
                                    display: table;
                                }
                            </style>

                            <script>
                                var fixHelper = function (e, tr) {
                                    var $originals = tr.children();
                                    var $helper = tr.clone();
                                    $helper.children().each(function (index) {
                                        $(this).width($originals.eq(index).width())

                                    });
                                    return $helper;
                                };
                                var updateIndex = function (e, ui) {

                                    $('td.index', ui.item.parent()).each(function (i) {
                                        var value = i + 1;
                                        var self = $(this);
                                        self.html(value);
                                        var fieldValue = $('td.field')[i].getElementsByTagName("input")[0].value.replace("field_", "");
                                        var inputHidden = $('<input>')
                                                .attr('type', 'hidden')
                                                .attr('name', 'position_' + fieldValue)
                                                .attr('value', i + 1);
                                        self.append(inputHidden);
                                    });
                                };

                                $("#sort tbody").sortable({
                                    opacity: 0.5,
                                    cursor: "move",
                                    helper: fixHelper,
                                    stop: updateIndex
                                }).disableSelection();
                            </script>

                        </div>
                    </s:if>
                </div>
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
